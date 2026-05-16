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
    void whenEndpointIsUnknown_expectBadRequest() {
        webTestClient.get()
                     .uri("/trigger-bad-request")
                     .exchange()
                     .expectStatus()
                     .isBadRequest();
    }
}