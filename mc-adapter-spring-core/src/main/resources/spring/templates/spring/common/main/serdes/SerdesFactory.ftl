
package ${SerdesFactory.packageName()};

import tools.jackson.databind.json.JsonMapper;
import jakarta.annotation.Nonnull;
import java.util.Objects;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.stereotype.Component;

/**
 * This returns `Serde` instances, which have both the serializer and deserializer
 * for a given event.
 * <p></p>
 * I've seen several repositories that follow this pattern, so I've included it here.
 * The idea is that, while defining your topology, you can fetch the serdes you need
 * by calling, for example, `SerdesFactory.PurchaseEvent()` or `SerdesFactory.PaymentEvent()`.
 *
 */
@Component
@SuppressWarnings({
    "java:S125",    // allow code examples in comment blocks
    "java:S1068",   // the unused objectMapper will get used once Serdes instances are added
    "java:S100"     // allow capitalized method names
})
public class ${SerdesFactory.className()} {

    private final JsonMapper jsonMapper;

    public SerdesFactory(@Nonnull JsonMapper jsonMapper) {
        this.jsonMapper = Objects.requireNonNull(jsonMapper, "The JsonMapper must not be null");
    }

    /*!
      =======================================================================================
      Here's an example of how to create a Serde for an object.
      When the topology is built and a serializer or deserializer is needed,
      adding a call to `serdesFactory.PurchaseEvent()` accomplishes that.

      public Serde<PurchaseEvent> PurchaseEvent() {
          JacksonSerializer<PurchaseEvent> jsonSerializer = new JacksonSerializer<>(jsonMapper);
          JacksonDeserializer<PurchaseEvent> jsonDeserialize = new JacksonDeserializer<>(PurchaseEvent.class, jsonMapper);

          return Serdes.serdeFrom(jsonSerializer, jsonDeserialize);
      }
      =======================================================================================
    */
}