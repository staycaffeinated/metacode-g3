
package ${SerdesFactory.packageName()};

import com.fasterxml.jackson.databind.ObjectMapper;
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
    "java:S125", // allow code examples in comment blocks
    "java:S1068" // the unused objectMapper will get used once Serdes instances are added
})
public class ${SerdesFactory.className()} {

    private final ObjectMapper objectMapper;

    public SerdesFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /*!
      =======================================================================================
      Here's an example of how to create a Serde for an object.
      When the topology is built and a serializer or deserializer is needed,
      adding a call to `serdesFactory.PurchaseEvent()` accomplishes that.

      public Serde<PurchaseEvent> PurchaseEvent() {
          JsonSerializer<PurchaseEvent> jsonSerializer = new JsonSerializer<>(objectMapper);
          JsonDeserializer<PurchaseEvent> jsonDeserialize = new JsonDeserializer<>(PurchaseEvent.class, objectMapper);

          return Serdes.serdeFrom(jsonSerializer, jsonDeserialize);
      }
      =======================================================================================
    */
}