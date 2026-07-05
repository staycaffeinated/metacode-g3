<#include "/common/Copyright.ftl">
package ${Exception.packageName()};

import java.io.Serial;

/**
 * An UnprocessableEntity exception indicates a well-formed request was
 * received, but could not be successfully processed.
 */
public class UnprocessableEntityException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2711067751568445348L;

    /**
     * Default Constructor
     */
    public UnprocessableEntityException() {
        super();
    }

    /**
     * Constructor
     */
    public UnprocessableEntityException(Throwable throwable) {
        super("Unable to process the resource (or entity)", throwable);
    }

    /**
     * Constructor with a reason to add to the exception
     * message as explanation.
     *
     * @param reason the associated reason (optional)
     */
    public UnprocessableEntityException(String reason) {
        super();
    }

    /**
     * Constructor with a reason to add to the exception
     * message as an explanation, as well as a nested exception.
     *
     * @param reason the associated reason (optional)
     * @param the cause for the exception (optional)
     */
    public UnprocessableEntityException(String reason, Throwable cause) {
        super(reason, cause);
    }
}