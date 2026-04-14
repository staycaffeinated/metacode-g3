package ${JsonDeserializer.packageName()};

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


class ${JsonDeserializer.className()}Test {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeValidObject() throws Exception {
        TestObject expected = new TestObject("widget-title", "widget-author");
        String json = objectMapper.writeValueAsString(expected);
        byte[] raw = json.getBytes();

        try (JsonDeserializer<TestObject> deserializer = new JsonDeserializer<>(TestObject.class, objectMapper)) {
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
            try (JsonDeserializer<TestObject> deserializer = new JsonDeserializer<>(TestObject.class)) {
                TestObject actual = deserializer.deserialize("fake-topic", null);
                assertThat(actual).isNull();
            }
        }

        @Test
        void whenDataIsEmpty_thenReturnsNull() {
            try (JsonDeserializer<TestObject> deserializer = new JsonDeserializer<>(TestObject.class)) {
                TestObject actual = deserializer.deserialize("fake-topic", new byte[0]);
                assertThat(actual).isNull();
            }
        }
    }

    /*
     * Helper classes
     */
    record TestObject(String title, String description) implements java.io.Serializable {}

}