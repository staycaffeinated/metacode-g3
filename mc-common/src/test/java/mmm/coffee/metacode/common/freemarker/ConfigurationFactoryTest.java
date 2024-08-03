package mmm.coffee.metacode.common.freemarker;

import freemarker.template.Configuration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationFactoryTest {

    @Test
    void shouldCreateFactory() {
        Configuration freemarkerConfig = ConfigurationFactory.defaultConfiguration("/spring/templates");
        assertThat(freemarkerConfig).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenTemplateIsNull() {
        assertThrows(NullPointerException.class, () -> ConfigurationFactory.defaultConfiguration(null));
    }
}
