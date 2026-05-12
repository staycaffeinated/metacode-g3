package ${JacksonDeserializer.packageName()};

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import tools.jackson.databind.json.JsonMapper;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;


class ${JacksonDeserializer.className()}Test {

    JsonMapper jsonMapper = new JsonMapper();

    @Test
    void shouldDeserializeValidObject() throws Exception {
        TestObject expected = new TestObject("widget-title", "widget-author");
        String json = jsonMapper.writeValueAsString(expected);
        byte[] raw = json.getBytes();

        try (${JacksonDeserializer.className()}<TestObject> deserializer = new ${JacksonDeserializer.className()}<>(TestObject.class, objectMapper)) {
            deserializer.configure(new HashMap<>(), false);

            TestObject actual = deserializer.deserialize("fake-topic", raw);
            Assertions.assertNotNull(actual);
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class EdgeCases {
        @Test
        void whenDataIsNull_thenReturnsNull() {
            try (${JacksonDeserializer.className()}<TestObject> deserializer = new ${JacksonDeserializer.className()}<>(TestObject.class)) {
                TestObject actual = deserializer.deserialize("fake-topic", null);
                assertThat(actual).isNull();
            }
        }

        @Test
        void whenBadData_thenReturnsNull() {
            try (${JacksonDeserializer.className()}<TestObject> deserializer = new ${JacksonDeserializer.className()}<>(TestObject.class)) {
                byte[] wrongStructure = "{\"wrongField\":\"value\",\"anotherWrongField\":123}".getBytes();
                TestObject actual = deserializer.deserialize("fake-topic", wrongStructure);
                assertThat(actual).isNull();
            }
        }

        @ParameterizedTest
        @NullSource
        void throwsExceptionIfTargetClassIsNull(Class<?> targetClass) {
            assertThrows(NullPointerException.class, () -> new ${JacksonDeserializer.className()}<>(targetClass));
        }

        @ParameterizedTest
        @NullSource
        void throwsExceptionIfObjectMapperIsNull(ObjectMapper objectMapper) {
            assertThrows(NullPointerException.class, () -> new ${JacksonDeserializer.className()}<>(String.class, objectMapper));
        }
    }

    /*
     * Helper classes
     */
    record TestObject(String title, String description) implements java.io.Serializable {}

}