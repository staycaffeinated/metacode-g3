<#include "/common/Copyright.ftl">

package ${PojoToDocumentConverter.packageName()};

import ${Document.fqcn()};
import ${EntityResource.fqcn()};
import ${DocumentTestFixtures.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("all")
class ${PojoToDocumentConverter.testClass()} {

    ${PojoToDocumentConverter.className()} converter = new ${PojoToDocumentConverter.className()}();

    final ${ResourceIdSupplier.className()} idSupplier = new ${SecureRandomSeries.className()}();

    @Test
    void shouldReturnNullWhenResourceIsNull() {
        assertThrows (NullPointerException.class, () ->  { converter.convert((${EntityResource.className()}) null); });
    }

    @Test
    void shouldReturnNullWhenListIsNull() {
        assertThrows (NullPointerException.class, () -> { converter.convert((List<${EntityResource.className()}>)null); });
    }

    @Test
    void shouldPopulateAllFields() {
        ${EntityResource.className()} resource = ${EntityResource.className()}.builder().resourceId(idSupplier.nextResourceId()).text("hello world").build();

        ${Document.className()} bean = converter.convert(resource);
        assertThat(bean.getResourceId()).isEqualTo(resource.getResourceId());
        assertThat(bean.getText()).isEqualTo(resource.getText());
    }

    @Test
    void shouldCopyList() {
        ${EntityResource.className()} resource = ${EntityResource.className()}.builder().resourceId(idSupplier.nextResourceId()).text("hello world").build();
        var pojoList = Lists.list(resource);

        List<${Document.className()}> ejbList = converter.convert(pojoList);
        assertThat(ejbList.size()).isOne();
        assertThat(ejbList.get(0).getResourceId()).isEqualTo(resource.getResourceId());
        assertThat(ejbList.get(0).getText()).isEqualTo(resource.getText());
    }
}
