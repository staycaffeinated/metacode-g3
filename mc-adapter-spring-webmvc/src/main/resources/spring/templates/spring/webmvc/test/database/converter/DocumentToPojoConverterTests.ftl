<#include "/common/Copyright.ftl">

package ${DocumentToPojoConverter.packageName()};

import ${Document.fqcn()};
import ${DocumentTestFixtures.fqcn()};
import ${EntityResource.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("all")
class ${DocumentToPojoConverter.testClass()} {

    ${DocumentToPojoConverter.className()} converter = new ${DocumentToPojoConverter.className()}();

    private final ${ResourceIdSupplier.className()} idSupplier = new ${SecureRandomSeries.className()}();

    @Test
    void shouldReturnNullWhenResourceIsNull() {
        assertThrows (NullPointerException.class, () ->  { converter.convert((${Document.className()}) null); });
    }

    @Test
    void shouldReturnNullWhenListIsNull() {
        assertThrows (NullPointerException.class, () -> { converter.convert((List<${Document.className()}>)null); });
    }

    /**
    * Verify that properties of the Document that must not be shared outside
    * the security boundary of the service are not copied into
    * the RESTful resource.  For example, the database ID
    * assigned to an entity bean must not be exposed to
    * external applications, thus the database ID is never
    * copied into a RESTful resource.
    */
    @Test
    void shouldCopyOnlyExposedProperties() {
        ${Document.className()} bean = ${DocumentTestFixtures.className()}.oneWithResourceId();

        ${EntityResource.className()} resource = converter.convert(bean);
        assertThat(resource.getResourceId()).isEqualTo(bean.getResourceId());
        assertThat(resource.getText()).isEqualTo(bean.getText());
    }

    @Test
    void shouldCopyList() {
        var ejbList = ${DocumentTestFixtures.className()}.allItems();

        List<${EntityResource.className()}> pojoList = converter.convert(ejbList);
        assertThat(pojoList.size()).isEqualTo(${DocumentTestFixtures.className()}.allItems().size());
    }

}
