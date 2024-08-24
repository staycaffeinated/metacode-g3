package mmm.coffee.metacode.spring.project.model;

import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.common.descriptor.Framework;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

class RestProjectTemplateModelBuilderTests {

    private static final String BASE_PATH = "/bookstore";
    private static final String BASE_PACKAGE = "io.example.bookstore";
    private static final String GROUP_ID = "io.example";
    private static final String APP_NAME = "bookstore";

    private static final String DEPENDENCY_FILE = "/spring/dependencies/dependencies.properties";


    @Test
    void shouldBuildTemplateModel() {
        RestProjectTemplateModel model = RestProjectTemplateModelFactory.create()
                .usingDependencyCatalog(dependencyCatalog())
                .usingProjectDescriptor(webMvcProjectDescriptor())
                .build();

        assertThat(model.getApplicationName()).isEqualTo(APP_NAME);
        assertThat(model.getBasePackage()).isEqualTo(BASE_PACKAGE);
        assertThat(model.getBasePath()).isEqualTo(BASE_PATH);
        assertThat(model.getGroupId()).isEqualTo(GROUP_ID);
        assertThat(model.isWebMvc()).isTrue();
        assertThat(model.isWebFlux()).isFalse();
    }

    private RestProjectDescriptor webMvcProjectDescriptor() {
        return RestProjectDescriptor.builder()
                .basePath(BASE_PATH)
                .applicationName(APP_NAME)
                .groupId(GROUP_ID)
                .framework(Framework.SPRING_WEBMVC)
                .basePackage(BASE_PACKAGE)
                .build();
    }

    private DependencyCatalog dependencyCatalog() {
        return new DependencyCatalog(DEPENDENCY_FILE);
    }

}
