package ${JacksonDeserializer.packageName()};

import tools.jackson.databind.json.JsonMapper;
import jakarta.annotation.Nullable;
import jakarta.annotation.Nonnull;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public class ${JacksonDeserializer.className()}<T> implements Deserializer<T> {

    private final Class<T> destinationClass;

    private final JsonMapper jsonMapper;

    /**
     * Create a new JacksonDeserializer using a custom JsonMapper.
     * @param destinationClass the class of the object to deserialize to
     * @param jsonMapper the JsonMapper to use for deserialization
    */
    public ${JacksonDeserializer.className()}(@Nonnull Class<T> destinationClass, @Nonnull JsonMapper jsonMapper) {
        this.destinationClass = Objects.requireNonNull(destinationClass, "The destinationClass must not be null");
        this.jsonMapper = Objects.requireNonNull(jsonMapper, "The ObjectMapper must not be null");
    }

    /**
     * Create a new JacksonDeserializer with a default ObjectMapper.
     * @param destinationClass the class of the object to deserialize to
     */
    public ${JacksonDeserializer.className()}(@Nonnull Class<T> destinationClass) {
        this(destinationClass, new JsonMapper());
    }


    @Override
    @Nullable
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return jsonMapper.readValue(data, destinationClass);
        } catch (Exception e) {
            log.error("An exception occurred in the deserializer: {}", e.getMessage(), e);
            return null;
        }
    }
}
