<#include "/common/Copyright.ftl">

package ${project.basePackage}.config;

import ${project.basePackage}.math.SecureRandomSeries;
import ${project.basePackage}.spi.ResourceIdSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* Configure application dependencies
*/
@Configuration
public class ApplicationConfiguration {

@Bean
public ResourceIdSupplier resourceIdSupplier() {
return new SecureRandomSeries();
}
}