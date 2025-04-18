<#include "/common/Copyright.ftl">
package ${GlobalExceptionHandler.packageName()};

import ${Exception.packageName()}.UnprocessableEntityException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.ConstraintViolation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

/**
 * Handles turning exceptions into RFC-7807 problem/json responses,
 * so instead of an exception and its stack trace leaking back
 * to the client, an RFC-7807 problem description is returned.
 */
@SuppressWarnings("java:1102")
@ControllerAdvice
public class ${GlobalExceptionHandler.className()} extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ProblemDetail> handleUnprocessableRequestException(UnprocessableEntityException exception) {
        return problemDescription("The request cannot be processed", exception);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        return problemDescription("The request contains invalid data", exception);
    }


    @ExceptionHandler({jakarta.validation.ConstraintViolationException.class})
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(jakarta.validation.ConstraintViolationException exception) {
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
        return ResponseEntity.of(problemDetail).build();
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than
     * jakarta.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFound(jakarta.persistence.EntityNotFoundException ex) {
        return problemDescription("The requested entity was not found", ex);
    }

    /**
     * Handles SQL exception.
     *
     * @param ex Exception
     * @param request WebRequest
     * @return ResponseEntity
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ProblemDetail> handleSQLException(SQLException ex, WebRequest request) {
        String message = String.format("Database Error: %s : %s ", ex.getErrorCode(), ex.getLocalizedMessage());
        return problemDescription(message, ex, HttpStatus.SERVICE_UNAVAILABLE);
    }


    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return a ResponseEntity with a body containing the problem description
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = "Invalid parameter in the request";

        // If the value and class are provided, write a detailed message
        Object value = ex.getValue();
        Class<?> klass = ex.getRequiredType();
        if (value != null && klass != null)
        message = String.format("The parameter '%s' with value '%s' could not be converted to type '%s'",
        ex.getName(), value, klass.getSimpleName());

        return problemDescription(message, ex);
    }

    /**
     * Catch anything that falls through
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleUncaughtException(RuntimeException ex) {
        return problemDescription(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex MissingServletRequestParameterException
     * @return the ApiError object
     */
    protected ResponseEntity<ProblemDetail> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        String error = String.format("The parameter '%s' is missing", ex.getParameterName());
        return problemDescription (error, ex, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    /**
     * Build a Problem/JSON description with HttpStatus: 422 (unprocessable entity)
     *
     * @param throwable the exception received by the handler
     * @return a ResponseEntity with a body containing the problem description
     */
    private ResponseEntity<ProblemDetail> problemDescription(String title, Throwable throwable) {
        return problemDescription(title, throwable, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Build a Problem/JSON description with the given http status
     *
     * @param throwable the exception received by the handler
     * @return a ResponseEntity with a body containing the problem descriptionn
     */
    private ResponseEntity<ProblemDetail> problemDescription(String title, Throwable throwable, HttpStatus status) {
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setDetail(throwable.getMessage());
        pd.setTitle(title);
        return ResponseEntity.status(status).body(pd);
    }
}

