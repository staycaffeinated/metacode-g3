package mmm.coffee.metacode.common.dictionary.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenRulesetIsNull(Map<String,String> startingRules) {
        assertThrows(NullPointerException.class,
                () -> new ClassNameRuleSet(startingRules));
    }
}
