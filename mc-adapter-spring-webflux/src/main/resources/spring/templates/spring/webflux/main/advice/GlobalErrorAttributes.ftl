<#include "/common/Copyright.ftl">
package ${GlobalErrorAttributes.packageName()};

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;


@Component
public class ${GlobalErrorAttributes.className()} extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        /*
         * If you want to standardize your error messages, add your own default messages to this map.
         * Examples:
         * https://github.com/hsteinmueller/spring-error-attributes-bug/blob/master/error-attributes-demo-reactive/src/main/java/org/example/errorattributesdemoreactive/CustomErrorAttributes.java
         * https://blog.softwaremill.com/spring-webflux-and-domain-validation-errors-3e0fc0f8c7a8
         */
        return super.getErrorAttributes(request, options);
    }
}
