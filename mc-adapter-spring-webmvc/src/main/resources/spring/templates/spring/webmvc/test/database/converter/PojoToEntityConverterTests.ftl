<#include "/common/Copyright.ftl">

package ${PojoToEntityConverter.packageName()};

import ${Entity.fqcn()};
import ${EntityResource.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${WebMvcEjbTestFixtures.fqcn()};
import ${WebMvcModelTestFixtures.fqcn()};

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("all")
class ${PojoToEntityConverter.testClass()} {

    ${PojoToEntityConverter.className()} converter = new ${PojoToEntityConverter.className()}();

    final ${ResourceIdSupplier.className()} randomSeries = new ${SecureRandomSeries.className()}();

    @Test
    void shouldReturnNullWhenResourceIsNull() {
        assertThrows (NullPointerException.class, () ->  { converter.convert((${endpoint.pojoName}) null); });
    }

    @Test
    void shouldReturnNullWhenListIsNull() {
        assertThrows (NullPointerException.class, () -> { converter.convert((List<${endpoint.pojoName}>)null); });
    }

    @Test
    void shouldPopulateAllFields() {
        ${EntityResource.className()} resource = ${WebMvcModelTestFixtures.className()}.oneWithResourceId();

        ${Entity.className()} bean = converter.convert(resource);
        assertThat(bean.getResourceId()).isEqualTo(resource.getResourceId());
        assertThat(bean.getText()).isEqualTo(resource.getText());
    }

    @Test
    void shouldCopyList() {
        var pojoList = ${WebMvcModelTestFixtures.className()}.allItems();

        List<${Entity.className()}> ejbList = converter.convert(pojoList);
        assertThat(ejbList.size()).isSameAs(pojoList.size());
        // spot check the first entry
        assertThat(ejbList.get(0).getResourceId()).isEqualTo(pojoList.get(0).getResourceId());
        assertThat(ejbList.get(0).getText()).isEqualTo(pojoList.get(0).getText());
    }
}
