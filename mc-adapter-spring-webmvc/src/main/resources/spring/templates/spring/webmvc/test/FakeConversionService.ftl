<#include "/common/Copyright.ftl">

package ${ConversionService.packageName()};

import ${EntityToPojoConverter.fqcn()};
import ${PojoToEntityConverter.fqcn()};
import ${EjbTestFixtures.fqcn()};
import ${ModelTestFixtures.fqcn()};

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * Creates a fake ConversionService suitable for testing
 */
public class FakeConversionService {

    static ConversionService build()   {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(new ${EntityToPojoConverter.className()}());
        service.addConverter(new ${PojoToEntityConverter.className()}());
        return service;
    }
}
