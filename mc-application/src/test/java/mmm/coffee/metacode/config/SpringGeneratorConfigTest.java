package mmm.coffee.metacode.config;

import mmm.coffee.metacode.Application;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.spring.config.SpringGeneratorConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = NONE, classes = Application.class)
public class SpringGeneratorConfigTest {

    @Autowired
    SpringGeneratorConfig classUnderTest;

    /**
     * A smoke test to verify the SpringGeneratorConfig class is annotated correctly
     * and the properties in application.yml are defined correctly
     */
    @Test
    void verifySpringGeneratorConfigIsCorrectlyAnnotated() {
        assertThat(classUnderTest.getDockerTemplates()).isNotNull().isNotEmpty();
        assertThat(classUnderTest.getGradleTemplates()).isNotNull().isNotEmpty();
        assertThat(classUnderTest.getLombokTemplates()).isNotNull().isNotEmpty();
        assertThat(classUnderTest.getSpringCommonTemplates()).isNotNull().isNotEmpty();
        assertThat(classUnderTest.getSpringWebMvcTemplates()).isNotNull().isNotEmpty();

        assertThat(classUnderTest.getDockerTemplates()).startsWith("/spring/templates");
        assertThat(classUnderTest.getSpringCommonTemplates()).startsWith("/spring/templates");
    }
}
