<#include "/common/Copyright.ftl">
package ${project.basePackage}.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serial;

/**
* A requested resource was not found
*/
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends ResponseStatusException {

@Serial
private static final long serialVersionUID = 1968592496239029423L;

/**
* Default Constructor
*/
public BadRequestException() {
super(HttpStatus.BAD_REQUEST);
}

/**
* Constructor
*/
public BadRequestException(Throwable throwable) {
super(HttpStatus.BAD_REQUEST, "The request cannot be processed", throwable);
}

/**
* Constructor with a reason to add to the exception
* message as explanation.
*
* @param reason the associated reason (optional)
*/
public BadRequestException(String reason) {
super(HttpStatus.BAD_REQUEST, reason);
}

/**
* Constructor with a reason to add to the exception
* message as explanation, as well as a nested exception.
*
* @param reason the associated reason (optional)
* @param cause  a nested exception (optional)
*/
public BadRequestException(String reason, Throwable cause) {
super(HttpStatus.BAD_REQUEST, reason, cause);
}
}