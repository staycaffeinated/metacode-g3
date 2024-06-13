<#include "/common/Copyright.ftl">

package ${ApplicationConfiguration.packageName()};

import ${project.basePackage}.math.SecureRandomSeries;
import ${project.basePackage}.spi.ResourceIdSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure application dependencies
 */
@Configuration
public class ${ApplicationConfiguration.className()} {

    @Bean
    public ResourceIdSupplier resourceIdSupplier() {
        return new SecureRandomSeries();
    }
}