
package ${SerdesFactory.packageName()};

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

/**
 * This returns `Serde` instances, which have both the serializer and deserializer
 * for a given event.
 * <p></p>
 * I've seen several repositories that follow this pattern, so I've included it here.
 * The idea is that, while defining your topology, you can fetch the serdes you need
 * by calling, for example, `SerdesFactory.PurchaseEvent()` or `SerdesFactory.PaymentEvent()`.
 *
 */
public class ${SerdesFactory.className()} {

    private SerdesFactory() {
        // empty constructor
    }

    /**!
     * =======================================================================================
     * Here's an example of how to create a Serde for an object.
     * When the topology is built and a serializer or deserializer is needed,
     * adding a call to `SerdesFactory.PurchaseEvent()` accomplishes that.
     *
     * public static Serde<PurchaseEvent> PurchaseEvent() {
     *     JsonSerializer<PurchaseEvent> jsonSerializer = new JsonSerializer<>();
     *     JsonDeserializer<PurchaseEvent> jsonDeserialize = new JsonDeserializer<>(PurchaseEvent.class);
     *
     *      return Serdes.serdeFrom(jsonSerializer, jsonDeserialize);
     * }
     * =======================================================================================
     */
}