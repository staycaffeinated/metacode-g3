package mmm.coffee.metacode.spring.constant;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpringIntegrationsTest {

    @Test
    void shouldHaveNameAndString() {
        SpringIntegrations si = SpringIntegrations.POSTGRES;
        assertThat(si.toString()).isEqualToIgnoringCase(SpringIntegrations.POSTGRES.name());
    }

}
