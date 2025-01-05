<#include "/common/Copyright.ftl">
package ${GlobalExceptionHandler.packageName()};

import lombok.extern.slf4j.Slf4j;
import ${ResourceNotFoundException.fqcn()};
import ${UnprocessableEntityException.fqcn()};

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.util.Optional;
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

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Mono<ProblemDetail> handleUnprocessableEntityException(UnprocessableEntityException exception) {
        return problemDescription("The request cannot be processed", exception);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public Mono<ProblemDetail> handleResourceNotFound(ResourceNotFoundException exception) {
	    return problemDescription("Resource not found", exception, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public Mono<ProblemDetail> handleNumberFormatException(NumberFormatException exception) {
	    return problemDescription("Bad request: request contains an invalid parameter", exception);
	}

    @ExceptionHandler({jakarta.validation.ConstraintViolationException.class})
    public Mono<ProblemDetail> handleConstraintViolationException(jakarta.validation.ConstraintViolationException exception)
    {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.createArrayNode();

        for (final var constraint : constraintViolations) {
            ObjectNode objectNode = objectMapper.createObjectNode();

            String className = constraint.getLeafBean().toString().split("@")[0];
            String message = constraint.getMessage();
            String propertyPath = constraint.getPropertyPath().toString().split("\\.")[0];
            Object invalidValue = constraint.getInvalidValue();

            objectNode.put("reason", message);
            // Since its common for REST parameters to be Optional, we unwrap the Optional for a cleaner message
            if (invalidValue instanceof Optional<?> theValue) {
                theValue.ifPresent(o -> objectNode.put("invalid value", o.toString()));
            }
            else {
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
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return null;
    }

    /**
     * Build a Problem/JSON description with HttpStatus: 422 (unprocessable entity)
     */
    private Mono<ProblemDetail> problemDescription(String title, Throwable throwable) {
        return problemDescription(title, throwable, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private Mono<ProblemDetail> problemDescription(String title, Throwable throwable, HttpStatus status) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setDetail(throwable.getMessage());
        problem.setTitle(title);
        return Mono.just(problem);
    }
}
