package ${JacksonSerializer.packageName()};


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import tools.jackson.databind.json.JsonMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class ${JacksonSerializer.className()}Test {

    JsonMapper jsonMapper = new JsonMapper();

    @Test
    void shouldSerializeValidObject() throws Exception {

        try (${JacksonSerializer.className()}<TestObject> serializer = new ${JacksonSerializer.className()}<>(jsonMapper)) {
            // given
            TestObject expected = new TestObject("widget-title", "widget-description");

            // when
            byte[] serialized = serializer.serialize("fake-topic", expected);

            // then
            assertThat(serialized).isNotNull().hasSizeGreaterThan(0);
            TestObject actual = jsonMapper.readValue(serialized, TestObject.class);
            assertThat(actual).isNotNull();
            assertThat(actual.title()).isEqualTo(expected.title());
            assertThat(actual.description).isEqualTo(expected.description());
        }
    }


    @Nested
    class EdgeCases {
        @Test
        void whenDataIsNull_thenReturnsNull() {
            try (${JacksonSerializer.className()}<String> serializer = new ${JacksonSerializer.className()}<>(jsonMapper)) {
                byte[] result = serializer.serialize("fake-topic", null);
                assertThat(result).isNull();
            }
        }

        @Test
        void whenObjectCannotBeSerialized_thenThrowsSerializationException() {
            try (${JacksonSerializer.className()}<SelfReferencingObject> serializer = new ${JacksonSerializer.className()}<>(jsonMapper)) {
                // given
                SelfReferencingObject data = new SelfReferencingObject();
                data.self = data; // create circular reference to cause serialization failure

                // when/then
                assertThrows(SerializationException.class, () -> serializer.serialize("test-topic", data));
            }
        }

        @ParameterizedTest
        @NullSource
        void shouldThrowExceptionWhenJsonMapperIsNull(JsonMapper aJsonMapper) {
            assertThrows(NullPointerException.class, () -> new ${JacksonSerializer.className()}<>(aJsonMapper));
        }
    }

    /* ---------------------------------------------------------------------------------
     * HELPER CLASSES
     * --------------------------------------------------------------------------------- */
     record TestObject(String title, String description) {}

     static class SelfReferencingObject {
         public SelfReferencingObject self;
     }

}



