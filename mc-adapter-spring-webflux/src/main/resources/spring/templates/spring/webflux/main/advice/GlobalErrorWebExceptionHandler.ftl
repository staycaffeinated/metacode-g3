<#include "/common/Copyright.ftl">
package ${GlobalErrorWebExceptionHandler.packageName()};

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

/**
 * Exceptions not routed to the `GlobalExceptionHandler` are handled here.
 * For instance, if a request raises an exception before the request is handled by
 * any controller, the raised exception is routed here to enable providing better-than-default error messages.
 */
@Component
@Order(-2)
@Slf4j
public class ${GlobalErrorWebExceptionHandler.className()} extends AbstractErrorWebExceptionHandler {

    /*
     * Constructor
     */
    public GlobalErrorWebExceptionHandler(
            ObjectProvider<ViewResolver> viewResolvers,
            ServerCodecConfigurer serverCodecConfigurer,
            ErrorAttributes errorAttributes,
            WebProperties webProperties,
            ApplicationContext applicationContext) {

        super(errorAttributes, webProperties.getResources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setViewResolvers(viewResolvers.orderedStream().toList());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());

        int statusCode = (Integer) errorPropertiesMap.getOrDefault("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ServerResponse.status(statusCode)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}
