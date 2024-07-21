<#include "/common/Copyright.ftl">

package ${EndpointConfiguration.packageName()};


import ${EntityToPojoConverter.fqcn()};
import ${PojoToEntityConverter.fqcn()};
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

/**
 * Configures of the converters, since WebFlux does not automatically do this.
 */
@Configuration
public class ${EndpointConfiguration.className()} {

	@Bean("${endpoint.entityVarName}Converter")
    ConversionService conversionService() {
		    var factory = new ConversionServiceFactoryBean();
		    Set<Converter<?, ?>> convSet = new HashSet<>();
		    convSet.add(new ${EntityToPojoConverter.className()}());
		    convSet.add(new ${PojoToEntityConverter.className()}());
		    factory.setConverters(convSet);
		    factory.afterPropertiesSet();
		    return factory.getObject();
	  }
}