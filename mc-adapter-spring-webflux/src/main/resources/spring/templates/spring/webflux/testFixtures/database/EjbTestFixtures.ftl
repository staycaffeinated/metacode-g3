
package ${EjbTestFixtures.packageName()};

import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${Entity.fqcn()};
import net.datafaker.Faker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample ${endpoint.ejbName} objects suitable for test data
 */
public class ${EjbTestFixtures.className()} {

    static final ${ResourceIdSupplier.className()} randomSeries = new ${SecureRandomSeries.className()}();

    static final Faker faker = new Faker();

    /*
     * Consider renaming these to something more meaningful to your use cases.
     * The general idea is to have names that indicate what the data point represents.
     * For example: accountWithPositiveBalance, accountWithNegativeBalance, accountWithZeroBalance,
     * activeAccount, closedAccount. With descriptive names, it's easier to discern
     * what condition is being exercised.
     */
    private static final ${endpoint.ejbName} SAMPLE_ONE;
    private static final ${endpoint.ejbName} SAMPLE_TWO;
    private static final ${endpoint.ejbName} SAMPLE_THREE;
    private static final ${endpoint.ejbName} SAMPLE_FOUR;
    private static final ${endpoint.ejbName} SAMPLE_FIVE;
    private static final ${endpoint.ejbName} SAMPLE_SIX;
    private static final ${endpoint.ejbName} SAMPLE_SEVEN;

    private static final ${endpoint.ejbName} ONE_WITH_RESOURCE_ID;
    private static final ${endpoint.ejbName} ONE_WITHOUT_RESOURCE_ID;

    /*
     * Useful for query tests where multiple records have the same text
     */
    private static final ${endpoint.ejbName} SAME_TEXT_ONE;
    private static final ${endpoint.ejbName} SAME_TEXT_TWO;
    private static final ${endpoint.ejbName} SAME_TEXT_THREE;

    static {
        SAMPLE_ONE = aNew${endpoint.ejbName}();
        SAMPLE_TWO = aNew${endpoint.ejbName}();
        SAMPLE_THREE = aNew${endpoint.ejbName}();
        SAMPLE_FOUR = aNew${endpoint.ejbName}();
        SAMPLE_FIVE = aNew${endpoint.ejbName}();
        SAMPLE_SIX = aNew${endpoint.ejbName}();
        SAMPLE_SEVEN = aNew${endpoint.ejbName}();

        ONE_WITH_RESOURCE_ID = aNew${endpoint.ejbName}();
        ONE_WITHOUT_RESOURCE_ID = aNew${endpoint.ejbName}WithNoResourceId();

        String matchingText = faker.book().title(); // TODO: Replace with something meaningful to your business objects
        SAME_TEXT_ONE = aNew${endpoint.ejbName}WithText(matchingText);
        SAME_TEXT_TWO = aNew${endpoint.ejbName}WithText(matchingText);
        SAME_TEXT_THREE = aNew${endpoint.ejbName}WithText(matchingText);
    }

    public static final List<${endpoint.ejbName}> ALL_ITEMS = new ArrayList<>();
    static {
        ALL_ITEMS.add(SAMPLE_ONE);
        ALL_ITEMS.add(SAMPLE_TWO);
        ALL_ITEMS.add(SAMPLE_THREE);
        ALL_ITEMS.add(SAMPLE_FOUR);
        ALL_ITEMS.add(SAMPLE_FIVE);
        ALL_ITEMS.add(SAMPLE_SIX);
        ALL_ITEMS.add(SAMPLE_SEVEN);
    }

    public static final List<${endpoint.ejbName}> allItems() { return ALL_ITEMS; }

    public static final Flux<${endpoint.ejbName}> FLUX_ITEMS = Flux.fromIterable(ALL_ITEMS);
    public static final Flux<${endpoint.ejbName}> allItemsAsFlux() { return FLUX_ITEMS; }


    public static final List<${endpoint.ejbName}> ALL_WITH_SAME_TEXT = new ArrayList<>() {{
        add(SAME_TEXT_ONE);
        add(SAME_TEXT_TWO);
        add(SAME_TEXT_THREE);
    }};
    public static final List<${endpoint.ejbName}> allItemsWithSameText() { return ALL_WITH_SAME_TEXT; }


    public static ${endpoint.ejbName} sampleOne() { return SAMPLE_ONE; }
    public static ${endpoint.ejbName} sampleTwo() { return SAMPLE_TWO; }
    public static ${endpoint.ejbName} sampleThree() { return SAMPLE_THREE; }

    /**
     * For those instances when you want to verify behavior against a
     * ${endpoint.ejbName} that has a resourceId.
     */
    public static ${endpoint.ejbName} oneWithResourceId() {
        return ONE_WITH_RESOURCE_ID;
    }

    /**
     * For those instances when you want to verify behavior against a
     * ${endpoint.ejbName} that has no resourceId assigned.
     */
    public static ${endpoint.ejbName} oneWithoutResourceId() {
        return ONE_WITHOUT_RESOURCE_ID;
    }

    /* ------------------------------------------------------------------------------------
     * Helper Methods
     * ------------------------------------------------------------------------------------ */

    /*
     * Create an entity with it's `text` attribute having the given value.
     * Naturally, if the real business object has some other attribute (eg, `title`, `firstName`
     * `accountNumber`, and such), then change this method to set the relevant attribute.
     */
    private static ${endpoint.ejbName} aNew${endpoint.ejbName}WithText(String text) {
        return ${endpoint.ejbName}.builder()
                .resourceId(randomSeries.nextResourceId())
                .text(text)
                .build();
    }

    private static ${endpoint.ejbName} aNew${endpoint.ejbName}() {
        return ${endpoint.ejbName}.builder()
                .resourceId(randomSeries.nextResourceId())
                .text(faker.book().title()) // TODO: replace with something meaningful to your business objects
                .build();
    }

    private static ${endpoint.ejbName} aNew${endpoint.ejbName}WithNoResourceId() {
        return ${endpoint.ejbName}.builder()
                .text(faker.book().title()) // TODO: replace with something meaningful to your business objects
                .build();
    }
}
