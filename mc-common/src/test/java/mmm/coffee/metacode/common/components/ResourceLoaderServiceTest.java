package mmm.coffee.metacode.common.components;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest(classes = ResourceLoaderService.class)
class ResourceLoaderServiceTest {

    @Autowired
    ResourceLoaderService resourceLoaderService;

    @Test
    void shouldFindResources() throws Exception {
        InputStream is = resourceLoaderService.loadResourceAsInputStream("classpath:catalogs/example-catalog.yml");
        assertThat(is).isNotNull();
    }

}
