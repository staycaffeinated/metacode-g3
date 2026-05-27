
package ${JacksonSerializer.packageName()};

import com.fasterxml.jackson.core.JsonProcessingException;
import tools.jackson.databind.json.JsonMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class ${JacksonSerializer.className()}<T> implements Serializer<T> {

    private JsonMapper jsonMapper;

    public ${JacksonSerializer.className()}(@Nonnull JsonMapper jsonMapper) {
        this.jsonMapper = Objects.requireNonNull(jsonMapper, "The JsonMapper must not be null");
    }

    @Override
    @Nullable
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }
        try {
            return jsonMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException(String.format("Error serializing data for topic '%s'", topic), e);
        }
    }
}
