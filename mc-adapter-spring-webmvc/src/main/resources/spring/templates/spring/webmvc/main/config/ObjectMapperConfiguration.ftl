<#include "/common/Copyright.ftl">

package ${ObjectMapperConfiguration.packageName()};

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
@SuppressWarnings({
    "java:S125" // allow code examples in comment blocks
})
public class ${ObjectMapperConfiguration.className()} {
    // Change these to whatever makes sense for your application.
    // The format given here includes a timezone.
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS+ZZ:ZZ";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .findAndAddModules()
            .build();
    }

    @Bean
    JsonMapper jsonMapper(JsonMapper.Builder builder) {
        return builder
            .enable(SerializationFeature.INDENT_OUTPUT)
            .build();
    }
}
