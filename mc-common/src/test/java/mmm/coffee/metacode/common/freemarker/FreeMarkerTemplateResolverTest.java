package mmm.coffee.metacode.common.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateMethodModelEx;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import mmm.coffee.metacode.common.dictionary.FakeArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import mmm.coffee.metacode.common.stereotype.MetaTemplateModel;
import mmm.coffee.metacode.common.trait.ConvertTrait;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class FreeMarkerTemplateResolverTest {

    ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Test
    void shouldThrowExceptionWhenConfigurationIsNull() {
        assertThrows(NullPointerException.class, () -> new FreemarkerTemplateResolver(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "classpath:/example-templates/service-api-template.ftl",
            "classpath:/example-templates/controller-template.ftl"
    })
    void canFindResources(String resourcePath) {
        Resource r1 = fetchResource(resourcePath);
        r1.getFilename();
        assertThat(r1.getFilename()).isNotNull();
        assertThat(r1.exists()).isTrue();
    }



    //@ParameterizedTest
    @Disabled("Resources are found but cannot be converted to file path that works with resolver")
    @ValueSource(strings = {
            "classpath:/example-templates/service-api-template.ftl",
            "classpath:/example-templates/controller-template.ftl"
    })
    void shouldRenderTemplate(String resourceName) throws Exception {
        FreemarkerTemplateResolver resolver = new FreemarkerTemplateResolver(freemarkerConfiguration());
        Resource r = fetchResource(resourceName);
        assertThat(r.exists()).isTrue();

        String templatePath = r.getFile().getAbsolutePath();
        String content = resolver.render(templatePath, getTemplateModel());
        assertThat(content).isNotNull();
    }

    /* ------------------------------------------------------------------
     * HELPER METHODS
     * ------------------------------------------------------------------ */

    Resource fetchResource(String resourcePath) {
        return resourceLoader.getResource(resourcePath);
    }


    Configuration freemarkerConfiguration() {
        Configuration config = new Configuration();
        config.setEncoding(Locale.US, "UTF-8");
        return config;
    }

    MetaTemplateModel getTemplateModel() {
        return new FakeTemplateModel();
    }

    static class FakeTemplateModel implements MetaTemplateModel {

        Map<String,Object> customProps = new HashMap<>();

        @Override
        public String getTopLevelVariable() {
            return "project";
        }

        @Override
        public ArchetypeDescriptor getArchetypeDescriptor() {
            return Mockito.mock(ArchetypeDescriptor.class);
        }

        @Override
        public Map<String, Object> getCustomProperties() {
            return customProps;
        }

        @Override
        public void setCustomProperties(Map<String, Object> customProperties) {
            customProps.putAll(customProperties);
        }
    }

}
