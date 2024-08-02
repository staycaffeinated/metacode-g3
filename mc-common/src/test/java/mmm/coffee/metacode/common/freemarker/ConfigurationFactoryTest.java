package mmm.coffee.metacode.common.freemarker;

import freemarker.template.Configuration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationFactoryTest {

    @Test
    void shouldCreateFactory() {
        Configuration freemarkerConfig = ConfigurationFactory.defaultConfiguration("/spring/templates");
        assertThat(freemarkerConfig).isNotNull();
    }
}
