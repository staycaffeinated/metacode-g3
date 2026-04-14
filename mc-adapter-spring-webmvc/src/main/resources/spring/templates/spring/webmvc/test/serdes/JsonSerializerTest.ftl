package ${JsonSerializer.packageName()};


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ${JsonSerializer.className()}Test {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSerializeValidObject() throws Exception {

        try (JsonSerializer<TestObject> serializer = new JsonSerializer<>(objectMapper)) {
            // given
            TestObject expected = new TestObject("widget-title", "widget-description");

            // when
            byte[] serialized = serializer.serialize("fake-topic", expected);

            // then
            assertThat(serialized).isNotNull().hasSizeGreaterThan(0);
            TestObject actual = objectMapper.readValue(serialized, TestObject.class);
            assertThat(actual).isNotNull();
            assertThat(actual.title()).isEqualTo(expected.title());
            assertThat(actual.description).isEqualTo(expected.description());
        }
    }


    @Nested
    class EdgeCases {
        @Test
        void whenDataIsNull_thenReturnsNull() {
            try (JsonSerializer<String> serializer = new JsonSerializer<>(objectMapper)) {
                byte[] result = serializer.serialize("fake-topic", null);
                assertThat(result).isNull();
            }
        }

        @Test
        void whenObjectCannotBeSerialized_thenThrowsSerializationException() {
            try (JsonSerializer<SelfReferencingObject> serializer = new JsonSerializer<>(objectMapper)) {
                // given
                SelfReferencingObject data = new SelfReferencingObject();
                data.self = data; // create circular reference to cause serialization failure

                // when/then
                assertThrows(SerializationException.class, () -> serializer.serialize("test-topic", data));
            }
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



