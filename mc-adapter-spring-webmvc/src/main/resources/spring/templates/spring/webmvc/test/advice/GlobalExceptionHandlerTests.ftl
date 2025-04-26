<#include "/common/Copyright.ftl">
package ${GlobalExceptionHandler.packageName()};

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ${Exception.packageName()}.UnprocessableEntityException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Unit tests of GlobalExceptionHandler
 *
 * Borrowed ideas from:
 *  https://github.com/spring-projects/spring-framework/blob/master/spring-webmvc/src/test/java/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandlerTests.java
 *
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandlerUnderTest = new GlobalExceptionHandler();

    /**
     * Test the condition the server raised an EntityNotFoundException
     */
    @Test
    void onEntityNotFoundException_shouldReturnBadRequest() {
        EntityNotFoundException ex = new EntityNotFoundException("some entity");
        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleEntityNotFound(ex);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        // Check the problem body for a status field matching the http status code
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }


    /**
     * Test the condition the server raised a DataIntegrityViolation
     */
    @Test
    void onDataIntegrityViolationException_shouldReturnBadRequest() {
        Set<ConstraintViolation<String>> violations = new HashSet<>();
        ConstraintViolationException cause = new ConstraintViolationException(violations);
        DataIntegrityViolationException ex = new DataIntegrityViolationException("my data integrity violation", cause);

        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleDataIntegrityViolationException(ex);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(response.getBody().getTitle()).isNotEmpty();
    }

    /**
     * Test the condition the server raised a MethodArgumentTypeMismatchException
     */
    @Test
    void onMethodArgumentTypeMismatchException_shouldReturnBadRequest() {
        WebRequest webRequest = mock(WebRequest.class);
        Class<?> stubResponse = String.class;
        MethodParameter parameter = mock(MethodParameter.class);

        // getParameterType() returns Class<?>, so the syntax shown is needed.
        // See https://stackoverflow.com/questions/16890133/cant-return-class-object-with-mockito
        Mockito.<Class<?>>when(parameter.getParameterType()).thenReturn(stubResponse);
        when(parameter.getParameterName()).thenReturn("firstName");

        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("parameterName");
        when(ex.getMessage()).thenReturn("argument type mismatch");

        // See the note above about why we use this syntax on this line
        Mockito.<Class<?>>when(ex.getRequiredType()).thenReturn(stubResponse);
        when(ex.getValue()).thenReturn("-badValue-");
        when(ex.getParameter()).thenReturn(parameter);

        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleMethodArgumentTypeMismatch(ex, webRequest);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Test the condition the server raised a SQLException
     */
    @Test
    void onSQLException_shouldReturnBadRequest() {
        WebRequest mockWebRequest = mock(WebRequest.class);
        SQLException ex = mock(SQLException.class);
        when(ex.getMessage()).thenReturn("my sql exception message");
        when(ex.getSQLState()).thenReturn("sql state");
        when(ex.getErrorCode()).thenReturn(1234);

        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleSQLException(ex, mockWebRequest);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        assertThat(response.getBody().getTitle()).isNotEmpty();
    }

    /**
     * Test our catch-all handler
     */
    @Test
    void onRuntimeException_shouldReturnServerError() {
        RuntimeException exception = new RuntimeException("I am a fake exception");

        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleUncaughtException(exception);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void whenUnprocessableEntityException_expectBadRequest() {
        var ex = Mockito.mock(UnprocessableEntityException.class);
        when(ex.getMessage()).thenReturn("A mock message");
        when(ex.getReason()).thenReturn("A mock reason");

        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleUnprocessableRequestException(ex);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getTitle()).isNotBlank();
    }

    @Test
    void whenMissingServletRequestParameter_expectBadRequest() {
        var ex = Mockito.mock(MissingServletRequestParameterException.class);
        when(ex.getMessage()).thenReturn("Mock message");

        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleMissingServletRequestParameter(ex);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getTitle()).isNotBlank();
    }

    @Test
    void whenMethodArgumentTypeMismatch_expectBadRequest() {
        var ex = Mockito.mock(MethodArgumentTypeMismatchException.class);
        when(ex.getMessage()).thenReturn("Mock message");
        when(ex.getValue()).thenReturn("STRING");
        when(ex.getRequiredType()).thenAnswer(it -> Integer.class);

        var webRequest = Mockito.mock(WebRequest.class);

        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleMethodArgumentTypeMismatch(ex, webRequest);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void whenMethodArgumentTypeMismatchAndValueIsNull_expectBadRequest() {
        var ex = Mockito.mock(MethodArgumentTypeMismatchException.class);
        when(ex.getMessage()).thenReturn("Mock message");
        when(ex.getValue()).thenReturn(null);
        when(ex.getRequiredType()).thenAnswer(it -> Integer.class);

        var webRequest = Mockito.mock(WebRequest.class);

        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleMethodArgumentTypeMismatch(ex,
        webRequest);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void whenMethodArgumentTypeMismatchAndRequiredTypeIsNull_expectBadRequest() {
        var ex = Mockito.mock(MethodArgumentTypeMismatchException.class);
        when(ex.getMessage()).thenReturn("Mock message");
        when(ex.getValue()).thenReturn("FirstName");
        when(ex.getRequiredType()).thenAnswer(it -> null);

        var webRequest = Mockito.mock(WebRequest.class);

        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleMethodArgumentTypeMismatch(ex, webRequest);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ParameterizedTest
    @MethodSource("provideSampleObject")
    void whenConstraintViolationException_expectProblemDetail(SampleObject sampleObject) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        ConstraintViolationException exception = getConstraintViolationException(factory, sampleObject);

        ResponseEntity<ProblemDetail> response = exceptionHandlerUnderTest.handleConstraintViolationException(exception);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ProblemDetail problemDetail = response.getBody();
        assertThat(problemDetail).isNotNull();
    }

    /* =======================================================================================
     * HELPER METHODS
     * ======================================================================================= */

     static Stream<Arguments> provideSampleObject() {
         // example of bad data
         SampleObject sample1 = new SampleObject("abc123", 12, false, "1234568", -1);
         // example of null data
         SampleObject sample2 = new SampleObject(null, 12, false, null, -1);

         return Stream.of(Arguments.of(sample1), Arguments.of(sample2));
     }

     private static ConstraintViolationException getConstraintViolationException(ValidatorFactory factory, SampleObject ts)
     {
         jakarta.validation.Validator validator = factory.getValidator();
         // Invoke validate() to trigger the validation
         <#noparse>Set<ConstraintViolation<SampleObject>> </#noparse>violations = validator.validate(ts);
         // Create an exception that wraps the constraint violations
         return new ConstraintViolationException(violations);
     }

     /**
      * A sample class that contains validation annotations.
      * Instances of this class can be used to verify how validation errors are handled
      * and how the ProblemDetail is populated.
      * <p>
      * This is borrowed from:
      * https://www.geeksforgeeks.org/spring-boot-data-and-field-validation-using-jakarta-validation-constraints/
      */
      private static class SampleObject {
          public interface AllLevels {}
          public interface Junior {}
          public interface MidSenior {}
          public interface Senior {}

          @Size(min = 5, max = 20, message = "Name must be between 5 and 20 characters long")
          @Pattern(regexp = "[0-9]", message = "Name must not contain numbers")
          @NotBlank(message = "Name is a mandatory field")
          final String name;

          @Min(value = 5, groups = Junior.class, message = "Junior level requires at least 5 years of experience")
          @Min(value = 10,groups = MidSenior.class, message = "Mid-Senior level requires at least 10 years of experience")
          @Min(value = 15, groups = Senior.class, message = "Senior level requires at least 15 years of experience")
          final int exp;

          @AssertTrue(message = "You are not an admin")
          final boolean isAdmin;

          @Pattern(regexp = "\\d{10}", message = "Mobile number must have exactly 10 digits")
          final String mobilNumber;

          <#noparse>final Optional<@Min(value = 0, message = "Weight must be at least 0") Long> weight;</#noparse>

          public SampleObject(String name, int exp, boolean isAdmin, String mobilNumber, long weight) {
              this.name = name;
              this.exp = exp;
              this.isAdmin = isAdmin;
              this.mobilNumber = mobilNumber;
              this.weight = Optional.of(weight);
          }
     }
}