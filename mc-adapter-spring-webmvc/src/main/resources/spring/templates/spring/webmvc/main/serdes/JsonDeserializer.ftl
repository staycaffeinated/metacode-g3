package ${JsonDeserializer.packageName()};

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JsonDeserializer<T> implements Deserializer<T> {

    private final Class<T> destinationClass;

    @Autowired
    private ObjectMapper objectMapper;

    public JsonDeserializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }


    @Override
    @Nullable
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.readValue(data, destinationClass);
        } catch (Exception e) {
            log.error("An exception occurred in the deserializer: {}", e.getMessage(), e);
            return null;
        }
    }
}
