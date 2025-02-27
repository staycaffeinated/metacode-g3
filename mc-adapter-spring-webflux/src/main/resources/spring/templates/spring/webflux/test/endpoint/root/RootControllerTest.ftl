<#include "/common/Copyright.ftl">
package ${RootController.packageName()};

import ${UnprocessableEntityException.fqcn()};
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


/**
 * Unit tests of RootController
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = RootController.class)
class ${RootController.testClass()} {

    @Autowired
    protected WebTestClient webClient;

    @Test
    void testHomePage() {
        webClient.get().uri("/").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk();
    }

    @Test
    void testMonoStream() {
        webClient.get().uri("/mono").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk();
    }

    @Test
    void testMonoResponse() {
        Flux<String> msg = webClient.get().uri("/mono").exchange().expectStatus().isOk()
            .returnResult(String.class).getResponseBody().log();
        StepVerifier.create(msg).expectSubscription().expectNext("OK").verifyComplete();
    }

    @Test
    void testFluxStream() {
        webClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk();
    }

    /**
     * An example of handling errors in Flux streams.
     * This example illustrates the technique and does not exercise the root controller.
     */
    @Test
    void testFluxErrorHandling() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
            .concatWith(Flux.error(new UnprocessableEntityException("Exception Occurred")))
            .concatWith(Flux.just("D"))
            .onErrorResume(e -> {    // this block gets executed
                return Flux.just("default", "default1");
            });

   	    StepVerifier.create(stringFlux.log())
   			.expectSubscription()
   			.expectNext("A", "B", "C")
   			.expectNext("default", "default1")
   			.verifyComplete();
    }

    /**
     * An example of a flux stream returning a default value when an error occurs in the stream.
     * This example illustrates the technique and does not exercise the root controller.
     */
    @Test
    void testFluxErrorHandling_OnErrorReturn() {

   	    Flux<String> stringFlux = Flux.just("A", "B", "C")
   			.concatWith(Flux.error(new RuntimeException("Exception Occurred")))
   			.concatWith(Flux.just("D"))
   			.onErrorReturn("default");

   	    StepVerifier.create(stringFlux.log())
   			.expectSubscription()
   			.expectNext("A", "B", "C")
   			.expectNext("default")
   			.verifyComplete();
    }

    /**
     * An example of a flux stream mapping an exception to a different exception.
     * This example illustrates the technique and does not exercise the root controller.
     */
    @Test
    void testFluxErrorHandling_OnErrorMap() {

   	    Flux<String> stringFlux = Flux.just("A", "B", "C")
   			.concatWith(Flux.error(new RuntimeException("Exception Occurred")))
   			.concatWith(Flux.just("D"))
   			.onErrorMap(e -> new UnprocessableEntityException(e));

   	    StepVerifier.create(stringFlux.log())
   			.expectSubscription()
   			.expectNext("A", "B", "C")
   			.expectError(UnprocessableEntityException.class)
   			.verify();

    }

    /**
     * An example of a flux stream using retry when an exception occurs.
     * This example illustrates the technique and does not exercise the root controller.
     */
    @Test
    void whenErrorOccurs_shouldContinueUninterrupted() {

   	    Flux<String> stringFlux = Flux.just("A", "B", "C")
   			.concatWith(Flux.error(new RuntimeException("Exception Occurred")))
   			.concatWith(Flux.just("D"))
   			.onErrorMap(e -> new UnprocessableEntityException(e))
   			.retry(2);

   	    StepVerifier.create(stringFlux.log())
   			.expectSubscription()
   			.expectNext("A", "B", "C")
   			.expectNext("A", "B", "C")
   			.expectNext("A", "B", "C")
   			.expectError(UnprocessableEntityException.class)
   			.verify();
    }
}