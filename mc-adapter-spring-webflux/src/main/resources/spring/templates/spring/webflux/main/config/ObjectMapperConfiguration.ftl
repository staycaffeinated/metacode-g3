<#include "/common/Copyright.ftl">

package ${ObjectMapperConfiguration.packageName()};

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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
            .addModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
    }

    /*
     * This is a helper method to get the configured date format.
     * If the configured value is "default" then the default format is returned.
     * Otherwise the configured value is returned.
     */
    public String springJacksonDateTimeFormat() {
        if ("default".equalsIgnoreCase(cfgDateFormat) || cfgDateFormat == null || cfgDateFormat.isBlank()) {
            return DEFAULT_DATE_TIME_FORMAT;
        } else {
            return cfgDateFormat;
        }
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // Register the JavaTimeModule (auto-registered in Spring Boot, but explicit for clarity)
            builder.modules(new JavaTimeModule());

            // This is configured in application.yml via the property
            // `spring.jackson.serialization.write_dates_as_timestamps=false`
            // Uncomment the following line to explicitly disable it
            // builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // instantiate formatters
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(springJacksonDateTimeFormat());
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

            // register serializers
            builder.serializers(new LocalDateSerializer(dateFormatter),             // date only
                                new LocalDateTimeSerializer(dateTimeFormatter),     // date-time
                                new ZonedDateTimeSerializer(dateTimeFormatter));    // date-time with timezone

            // The serializers and deserializers can be registered by type as well, if preferred. For example,
            // builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter));

            // register deserializers.
            // !! A ZonedDateTimeDeserializer is missing because there is no
            // default implementation of that.
            builder.deserializers(new LocalDateDeserializer(dateFormatter),
                                  new LocalDateTimeDeserializer(dateTimeFormatter));
        };
    }
}
