package mmm.coffee.metacode.config;

import mmm.coffee.metacode.common.toml.DefaultPackageDataDictionary;
import mmm.coffee.metacode.common.toml.PackageDataDictionary;
import mmm.coffee.metacode.common.toml.PackageDataDictionaryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Configuration of application beans
 */
@Configuration
public class ApplicationConfig {

    @Value("${app.package-schema:packages.toml}")
    private String packageSchema;

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


    @Bean
    public PackageDataDictionaryFactory packageDataDictionaryFactory() {
        return new PackageDataDictionaryFactory();
    }

    @Bean
    public PackageDataDictionary packageDataDictionary(PackageDataDictionaryFactory factory) throws IOException {
        return factory.createDictionary(packageSchema);
    }
}
