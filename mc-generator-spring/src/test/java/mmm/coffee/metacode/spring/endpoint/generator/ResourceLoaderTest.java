package mmm.coffee.metacode.spring.endpoint.generator;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import static com.google.common.truth.Truth.assertThat;

/**
 * Use this to confirm resource files are found and loading
 */
class ResourceLoaderTest {
    ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Test
    void should_load_resource_from_classpath() {
        Resource resource = resourceLoader.getResource("classpath:/package-layout.json");
        assertThat(resource).isNotNull();
    }


}
