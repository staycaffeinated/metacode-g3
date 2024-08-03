package mmm.coffee.metacode.common.model;

import lombok.Builder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaArchetypeDescriptorTest {

    @Test
    void shouldReturnValues() {
        JavaArchetypeDescriptor jad = DefaultJavaArchetypeDescriptor.builder()
                .archetype(Archetype.Application)
                .fqcn("org.example.petstore.Application")
                .packageName("org.example.petstore")
                .className("Application")
                .build();

        assertThat(jad.className()).isEqualTo("Application");
        assertThat(jad.fqcn()).isEqualTo("org.example.petstore.Application");
        assertThat(jad.packageName()).isEqualTo("org.example.petstore");
        assertThat(jad.fqcnTestFixture()).isNotBlank();
        assertThat(jad.fqcnUnitTest()).isNotBlank();
        assertThat(jad.testClass()).isNotBlank();
        assertThat(jad.integrationTestClass()).isNotBlank();
        assertThat(jad.varName()).isNotBlank();
        assertThat(jad.testFixture()).isNotBlank();
    }

    @Test
    void shouldHandleNullClassName() {
        // Since JavaArchetypeDescriptor is an interface, there's no way to enforce non-null
        JavaArchetypeDescriptor jad = new DefaultJavaArchetypeDescriptor(Archetype.Application, "org.example.Application", "org.example", null);
        assertThat(jad.varName()).isNotBlank();
    }


    @Builder
    private record DefaultJavaArchetypeDescriptor(Archetype archetype, String fqcn, String packageName,
                                                  String className) implements JavaArchetypeDescriptor {
        public String toString() {
            return "DefaultJavaArchetype[className: " + className() + ", " +
                    "fqcn: " + fqcn() + ", " +
                    "unitTestClass: " + fqcnUnitTest() + ", " +
                    "integrationTestClass: " + fqcnIntegrationTest() + ", " +
                    "packageName: " + packageName() + "]";
        }
    }

}
