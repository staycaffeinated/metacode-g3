<#include "/common/Copyright.ftl">
package ${project.basePackage}.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serial;

/**
 * A requested resource was not found
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ResponseStatusException {

    @Serial
    private static final long serialVersionUID = -1144457886816201247L;

    /**
     * Default Constructor
     */
    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    /**
     * Constructor
     */
    public ResourceNotFoundException(Throwable throwable) {
       super(HttpStatus.NOT_FOUND, "The requested resource was not found", throwable);
    }

    /**
     * Constructor with a reason to add to the exception
     * message as explanation.
     *
     * @param reason the associated reason (optional)
     */
    public ResourceNotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }

    /**
     * Constructor with a reason to add to the exception
     * message as explanation, as well as a nested exception.
     *
     * @param reason the associated reason (optional)
     * @param cause  a nested exception (optional)
     */
    public ResourceNotFoundException(String reason, Throwable cause) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason, cause);
    }
}