package mmm.coffee.metacode.common.dictionary;

import com.samskivert.mustache.MustacheException;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class EndpointArchetypeToMapTest {

    IArchetypeDescriptorFactory descriptorFactory;

    @BeforeEach
    void setUp() throws Exception {
        descriptorFactory = new FakeArchetypeDescriptorFactory();
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "Controller",
            "ConversionService",
            "DatabaseTablePopulator",
            "Document",
            "DocumentTestFixtures",
            "DocumentToPojoConverter",
            "EntityResource",
            "MongoDataStoreApi",
            "ConcreteDataStoreApi",
            "ConcreteDataStoreImpl",
            "PojoToDocumentConverter",
            "Repository",
            "Routes",
            "ServiceApi",
            "ServiceImpl"
    })
    void shouldRecognizeEndpointArchetypes(String archetypeName) {
        Map<String, ArchetypeDescriptor> mapping = EndpointArchetypeToMap.map(descriptorFactory, "Pet");
        assertThat(mapping).isNotEmpty();

        assertThat(mapping.get(archetypeName)).isNotNull();
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenFactoryIsNull(IArchetypeDescriptorFactory factory) {
        assertThrows(NullPointerException.class, () -> EndpointArchetypeToMap.map(factory, "Book"));
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenResourceIsNull(String resource) {
        assertThrows(NullPointerException.class, () -> EndpointArchetypeToMap.map(descriptorFactory, resource));
    }

    @Test
    @SuppressWarnings("java:S125") // false positive; comment does not contain real code
    void shouldRethrowMustacheExceptionWhenMappingFails() {
        // lines 53-56: catch(MustacheException), log error, rethrow
        IArchetypeDescriptorFactory badFactory = Mockito.mock(IArchetypeDescriptorFactory.class);
        Mockito.when(badFactory.createArchetypeDescriptor(any(Archetype.class)))
               .thenThrow(new MustacheException("simulated template error"));

        assertThrows(MustacheException.class, () -> EndpointArchetypeToMap.map(badFactory, "Pet"));
    }

    @Test
    void shouldContainPojoToDocumentConverter() {
        for (Archetype at : EndpointArchetypeToMap.ENDPOINT_ARCHETYPES) {
            if (at == Archetype.PojoToDocumentConverter) {
                return;
            }
        }
        Assertions.fail("The EndpointArchetypeToMap is missing the PojoToDocumentConverter archetype");
    }
}
