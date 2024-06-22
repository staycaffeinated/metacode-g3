package mmm.coffee.metacode.common.dictionary.functions;

import mmm.coffee.metacode.common.dictionary.ArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.model.Archetype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ClassNameRuleSetTest {

    ClassNameRuleSet rulesUnderTest;

    @BeforeEach
    public void setUp() throws Exception {
        rulesUnderTest = ClassNameRuleSetTestFixture.classNameRuleSet();
    }


    @Test
    void shouldResolveClassName() {
        String klassName = rulesUnderTest.resolveClassName("Application");
        assertThat(klassName).isNotBlank();
    }

    @Test
    void shouldResolveClassNameOfObj() {
        String cntrlClass = rulesUnderTest.resolveClassName("Controller", "Pet");
        assertThat(cntrlClass).isEqualTo("PetController");

        String svcApi = rulesUnderTest.resolveClassName("ServiceApi", "Pet");
        assertThat(svcApi).isEqualTo("PetService");

        String svcImpl = rulesUnderTest.resolveClassName("ServiceImpl", "Pet");
        assertThat(svcImpl).isEqualTo("PetServiceImpl");
    }
}
