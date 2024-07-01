package mmm.coffee.metacode.common.dictionary;

import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.TestCase.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EndpointArchetypeToMapTest {

    IArchetypeDescriptorFactory descriptorFactory;

    @BeforeEach
    public void setUp() throws Exception {
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
            "MongoDataStore",
            "ObjectDataStore",
            "ObjectDataStoreProvider",
            "PojoToDocumentConverter",
            "Repository",
            "Routes",
            "ServiceApi",
            "ServiceImpl"
    })
    void shouldRecognizeEndpointArchetypes(String archetypeName) {
        Map<String, ArchetypeDescriptor> mapping = EndpointArchetypeToMap.map(descriptorFactory, "Pet");
        assertThat(mapping).isNotEmpty();

        ArchetypeDescriptor descriptor = mapping.get(archetypeName);
        assertThat(mapping.get(archetypeName)).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenFactoryIsNull() {
        assertThrows(NullPointerException.class, () -> EndpointArchetypeToMap.map(null, "Book"));
    }

    @Test
    void shouldThrowExceptionWhenResourceIsNull() {
        assertThrows(NullPointerException.class, () -> EndpointArchetypeToMap.map(descriptorFactory, null));
    }

    @Test
    void shouldContainPojoToDocumentConverter() {
        for (Archetype at : EndpointArchetypeToMap.ENDPOINT_ARCHETYPES) {
            if (at == Archetype.PojoToDocumentConverter) {
                return;
            }
        }
        fail("The EndpointArchetypeToMap is missing the PojoToDocumentConverter archetype");
    }
}
