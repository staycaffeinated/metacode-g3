<#include "/common/Copyright.ftl">

package ${ObjectMapperConfiguration.packageName()};

import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


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

    // Prefer the configured value but fall back to a default if not set
    <#noparse>@Value("${spring.jackson.date-format:default}")</#noparse>
    private String cfgDateFormat;


    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .findAndAddModules()
            .build();
    }

    @Bean
    public JsonMapper jsonMapper(JsonMapper.Builder builder) {
        return builder.enable(SerializationFeature.INDENT_OUTPUT).build();
    }
}
