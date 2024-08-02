package mmm.coffee.metacode.common.model;

import mmm.coffee.metacode.common.dictionary.FakeArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ArchetypeDescriptorTest {

    ArchetypeDescriptor classUnderTest;

    IArchetypeDescriptorFactory factory;

    @BeforeEach
    public void setUp() throws IOException {
        factory = new FakeArchetypeDescriptorFactory();
    }

    @Test
    void shouldProvideArchetypeName() {
        classUnderTest = factory.createArchetypeDescriptor(Archetype.Application);
        assertThat(classUnderTest.archetypeName()).isNotBlank();
        assertThat(classUnderTest.archetype()).isNotNull();
    }

    @Test
    void whenArchetypeIsNull_shouldReturnNonBlankArchetypeName() {
        classUnderTest = new ArchetypeDescriptorWithNullArchetype();
        assertThat(classUnderTest.archetypeName()).isNotBlank();
        assertThat(classUnderTest.archetype()).isNull();
    }

    static class ArchetypeDescriptorWithNullArchetype implements ArchetypeDescriptor {

        @Override
        public Archetype archetype() {
            return null;
        }
    }
}
