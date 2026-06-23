
<#include "/common/Copyright.ftl">
package ${GlobalErrorWebExceptionHandler.packageName()};

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class GlobalErrorWebExceptionHandlerTest {

    @MockitoBean
    private ObjectProvider<ViewResolver> viewResolvers;

    @MockitoBean
    private ServerCodecConfigurer serverCodecConfigurer;

    @MockitoBean
    private ErrorAttributes errorAttributes;

    @MockitoBean
    private ApplicationContext applicationContext;

    private GlobalErrorWebExceptionHandler handlerUnderTest;

    @BeforeEach
    void setUp() {
        when(viewResolvers.orderedStream()).thenReturn(Stream.empty());
        when(serverCodecConfigurer.getWriters()).thenReturn(Collections.emptyList());
        when(serverCodecConfigurer.getReaders()).thenReturn(Collections.emptyList());
        when(applicationContext.getClassLoader()).thenReturn(getClass().getClassLoader());

        handlerUnderTest = new GlobalErrorWebExceptionHandler(
        viewResolvers, serverCodecConfigurer, errorAttributes, new WebProperties(), applicationContext);
    }

    @Test
    void getRoutingFunction_returnsNonNull() {
        assertThat(handlerUnderTest.getRoutingFunction(errorAttributes)).isNotNull();
    }

    @Test
    void getRoutingFunction_matchesAllRequests() {
        RouterFunction<ServerResponse> routingFunction = handlerUnderTest.getRoutingFunction(errorAttributes);
        ServerRequest request = createServerRequest("/any-path");

        StepVerifier.create(routingFunction.route(request)).expectNextCount(1).verifyComplete();
    }

    @Test
    void renderErrorResponse_usesStatusFromErrorAttributes() {
        when(errorAttributes.getErrorAttributes(any(ServerRequest.class), any(ErrorAttributeOptions.class)))
            .thenReturn(Map.of("status", 404));

        RouterFunction<ServerResponse> routingFunction = handlerUnderTest.getRoutingFunction(errorAttributes);
        ServerRequest request = createServerRequest("/error");

        StepVerifier.create(routingFunction.route(request).flatMap(h -> h.handle(request)))
                    .expectNextMatches(response -> response.statusCode().value() == 404)
                    .verifyComplete();
    }

    @Test
    void renderErrorResponse_defaultsTo500WhenStatusAbsent() {
        when(errorAttributes.getErrorAttributes(any(ServerRequest.class), any(ErrorAttributeOptions.class)))
                            .thenReturn(Collections.emptyMap());

        RouterFunction<ServerResponse> routingFunction = handlerUnderTest.getRoutingFunction(errorAttributes);
        ServerRequest request = createServerRequest("/error");

        StepVerifier.create(routingFunction.route(request).flatMap(h -> h.handle(request)))
                    .expectNextMatches(response -> response.statusCode().value() == 500)
                    .verifyComplete();
    }

    @Test
    void renderErrorResponse_setsContentTypeToApplicationJson() {
        when(errorAttributes.getErrorAttributes(any(ServerRequest.class), any(ErrorAttributeOptions.class)))
                .thenReturn(Map.of("status", 200));

        RouterFunction<ServerResponse> routingFunction = handlerUnderTest.getRoutingFunction(errorAttributes);
        ServerRequest request = createServerRequest("/error");

        StepVerifier.create(routingFunction.route(request).flatMap(h -> h.handle(request)))
                    .expectNextMatches(response ->
                        MediaType.APPLICATION_JSON.equals(response.headers().getContentType()))
                    .verifyComplete();
    }

    private ServerRequest createServerRequest(String path) {
        MockServerHttpRequest httpRequest = MockServerHttpRequest.get(path).build();
        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        return ServerRequest.create(exchange, Collections.emptyList());
    }
}
