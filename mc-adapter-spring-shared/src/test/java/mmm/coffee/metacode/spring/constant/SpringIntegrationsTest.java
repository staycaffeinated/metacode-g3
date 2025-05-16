package mmm.coffee.metacode.spring.constant;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class SpringIntegrationsTest {

    @ParameterizedTest
    @EnumSource(SpringIntegrations.class)
    void shouldHaveNameAndString(SpringIntegrations springIntegrations) {
        assertThat(springIntegrations.toString()).isEqualToIgnoringCase(springIntegrations.name());
    }

}
