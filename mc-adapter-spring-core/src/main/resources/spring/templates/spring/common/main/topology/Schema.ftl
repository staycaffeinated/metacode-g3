package ${Schema.packageName()};

import java.util.List;

/*
 * This class enumerates the Topics and State Stores used by the application.
 * If you prefer another style of tracking your Topics and State Stores, you can
 * delete this class and replace it with your own.  The example topics and stores
 * given below are simply examples.
 *
 * The structure of this class is inspired by some example code from Confluent.
 */

public class ${Schema.className()} {

    public static List<String> allTopics() {
        return List.of(Topics.ITEM_CREATED, Topics.ITEM_VALIDATED, Topics.ITEM_DLQ);
    }

    // These are the topics used by the application.
    public static class Topics {
        public static final String ITEM_CREATED = "example-app.items.created";
        public static final String ITEM_VALIDATED = "example-app.items.validated";
        public static final String ITEM_DLQ = "example-app.items.dlq";

        private Topics() {
            // sealed
        }
    }

    // These are the state stores used by the application.
    public static class Stores {
        public static final String ITEMS_STORE = "example-app.items.store";

        private Stores() {
            // sealed
        }
    }

    private Schema() {
        // sealed
    }
}