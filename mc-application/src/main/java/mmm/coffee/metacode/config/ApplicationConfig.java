package mmm.coffee.metacode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * TODO: Fill me in
 */
@Configuration
public class ApplicationConfig {

    /**
     * Added to resolve warning:
     * <code>
     *      WARNING: Unable to get bean of class interface java.util.List,
     *      using fallback factory picocli.CommandLine$DefaultFactory
     *      (org.springframework.beans.factory.BeanCreationException:
     *      Error creating bean with name 'java.util.List': Failed to instantiate
     *      [java.util.List]: Specified class is an interface)
     * </code>
     * Some of the Picocli command objects use lists to store multi-value options
     */
    @Bean
    java.util.List<String> stringList() {
        return new ArrayList<>();
    }
}
