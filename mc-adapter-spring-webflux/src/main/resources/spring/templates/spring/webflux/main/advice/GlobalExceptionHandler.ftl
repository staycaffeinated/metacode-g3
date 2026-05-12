<#include "/common/Copyright.ftl">
package ${GlobalExceptionHandler.packageName()};

import lombok.extern.slf4j.Slf4j;
import ${ResourceNotFoundException.fqcn()};
import ${UnprocessableEntityException.fqcn()};

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Handles turning exceptions into RFC-7807 problem/json responses,
 * so instead of an exception and its stack trace leaking back
 * to the client, an RFC-7807 problem description is returned instead.
 */
@SuppressWarnings("unused")
@Slf4j
@ControllerAdvice
@ResponseBody
public class ${GlobalExceptionHandler.className()} {

    private final ObjectMapper objectMapper;

    /**
     * Constructor
     */
    public ${GlobalExceptionHandler.className()}(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public Mono<ProblemDetail> handleUnprocessableEntityException(UnprocessableEntityException exception) {
        return problemDescription("The request cannot be processed", exception);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public Mono<ProblemDetail> handleResourceNotFound(ResourceNotFoundException exception) {
	    return problemDescription("Resource not found", exception, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
	public Mono<ProblemDetail> handleNumberFormatException(NumberFormatException exception) {
	    return problemDescription("Bad request: request contains an invalid parameter", exception);
	}

    @ExceptionHandler({jakarta.validation.ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ProblemDetail> handleConstraintViolationException(jakarta.validation.ConstraintViolationException exception)
    {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        ArrayNode jsonArray = objectMapper.createArrayNode();

        for (final var constraint : constraintViolations) {
            ObjectNode objectNode = objectMapper.createObjectNode();

            String className = constraint.getLeafBean().toString().split("@")[0];
            String message = constraint.getMessage();
            String propertyPath = constraint.getPropertyPath().toString().split("\\.")[0];
            Object invalidValue = constraint.getInvalidValue();

            objectNode.put("reason", message);
            if (invalidValue != null) {
                objectNode.put("invalid value", invalidValue.toString());
            }
            // You may not want to reveal the classname or method name since doing so leaks implementation details.
            // For troubleshooting internal applications, this may be useful.
            objectNode.put("class", className);
            objectNode.put("method", propertyPath);

            jsonArray.add(objectNode);
        }
        problemDetail.setProperty("errors", jsonArray);
        return Mono.just(problemDetail);
    }

    /**
     * Catch anything not caught by other handlers
     */
    @SuppressWarnings({
        "java:S1172" // `exchange` is unused but needed for the method signature
    })
    public Mono<ProblemDetail> handle(ServerWebExchange exchange, Throwable ex) {
        return problemDescription("ServerWebExchange Error", ex, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    /**
     * Build a Problem/JSON description with HttpStatus: 422 (unprocessable entity)
     */
    private Mono<ProblemDetail> problemDescription(String title, Throwable throwable) {
        return problemDescription(title, throwable, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    private Mono<ProblemDetail> problemDescription(String title, Throwable throwable, HttpStatus status) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setDetail(throwable.getMessage());
        problem.setTitle(title);
        return Mono.just(problem);
    }
}
