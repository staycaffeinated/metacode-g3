<#include "/common/Copyright.ftl">
package ${GlobalExceptionHandler.packageName()};

import ${ResourceNotFoundException.fqcn()};
import ${UnprocessableEntityException.fqcn()};
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests of the GlobalExceptionHandler
 */
class ${GlobalExceptionHandler.testClass()} {

    private ${GlobalExceptionHandler.className()} exceptionHandlerUnderTest;

    @BeforeEach
    public void setUp() {
        exceptionHandlerUnderTest = new GlobalExceptionHandler();
    }

    @Test
    void whenUnprocessableEntityException_expectHttpStatusIsUnprocessableEntity() {
        // when
        var publisher = exceptionHandlerUnderTest .handleUnprocessableEntityException(new UnprocessableEntityException("test"));

        // then
        StepVerifier.create(publisher).expectSubscription()
            .consumeNextWith(p -> assertThat(p.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value()))
            .verifyComplete();
    }

    @Test
    void whenResourceNotFoundException_expectHttpStatusIsNotFound() {
        // when
        var publisher = exceptionHandlerUnderTest.handleResourceNotFound(new ResourceNotFoundException("test"));

        // then
        StepVerifier.create(publisher).expectSubscription()
            .consumeNextWith(p -> assertThat(p.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value()))
            .verifyComplete();
    }

    @Test
    void whenNumberFormatExceptionException_expectHttpStatusIsUnprocessableEntity() {
        // when
        var publisher = exceptionHandlerUnderTest.handleNumberFormatException(new NumberFormatException("test"));

        // then
        StepVerifier.create(publisher).expectSubscription()
            .consumeNextWith(p -> assertThat(p.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value()))
            .verifyComplete();
    }

    @Test
    void verifyHandleMethodReturnProblemDetail() {
        var serverWebExchange = Mockito.mock(ServerWebExchange.class);
        var publisher = exceptionHandlerUnderTest.handle(serverWebExchange, new Throwable());

        StepVerifier.create(publisher)
                    .expectSubscription()
                    .consumeNextWith(p -> assertThat(p.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                    .verifyComplete();
    }
}