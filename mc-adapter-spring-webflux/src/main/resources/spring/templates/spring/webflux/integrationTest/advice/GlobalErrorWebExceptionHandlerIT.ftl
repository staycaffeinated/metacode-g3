<#include "/common/Copyright.ftl">
package ${GlobalErrorWebExceptionHandler.packageName()};

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class GlobalErrorWebExceptionHandlerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenEndpointIsUnknown_expectNotFound() {
        webTestClient.get()
                     .uri("/no-such-endpoint")
                     .exchange()
                     .expectStatus()
                     .isNotFound();
    }
}