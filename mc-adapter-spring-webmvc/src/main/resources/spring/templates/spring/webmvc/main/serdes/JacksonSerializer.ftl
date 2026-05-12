
package ${JacksonSerializer.packageName()};

import com.fasterxml.jackson.core.JsonProcessingException;
import tools.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class ${JacksonSerializer.className()}<T> implements Serializer<T> {

    private ObjectMapper objectMapper;

    public ${JacksonSerializer.className()}(@Nonnull ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "The ObjectMapper must not be null");
    }

    @Override
    @Nullable
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException(String.format("Error serializing data for topic '%s'", topic), e);
        }
    }
}
