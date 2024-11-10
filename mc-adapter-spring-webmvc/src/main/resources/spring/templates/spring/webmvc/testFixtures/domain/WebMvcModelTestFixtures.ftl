package ${ModelTestFixtures.packageName()};

import ${EntityResource.fqcn()};
import ${SecureRandomSeries.fqcn()};
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;


/**
* Sample ${EntityResource.className()} objects suitable for test data
*/
public class ${ModelTestFixtures.className()} {

    static final ${SecureRandomSeries.className()} randomSeries = new ${SecureRandomSeries.className()}();

    static final Faker faker = new Faker();

    /*
     * Consider renaming these to something more meaningful to your use cases.
     * The general idea is to have names that indicate what the data point represents.
     * For example: accountWithPositiveBalance, accountWithNegativeBalance, accountWithZeroBalance,
     * activeAccount, closedAccount. With descriptive names, it's easier to discern
     * what condition is being exercised.
     */
    private static final ${EntityResource.className()} SAMPLE_ONE;
    private static final ${EntityResource.className()} SAMPLE_TWO;
    private static final ${EntityResource.className()} SAMPLE_THREE;
    private static final ${EntityResource.className()} SAMPLE_FOUR;
    private static final ${EntityResource.className()} SAMPLE_FIVE;
    private static final ${EntityResource.className()} SAMPLE_SIX;
    private static final ${EntityResource.className()} SAMPLE_SEVEN;

    private static final ${EntityResource.className()} ONE_WITH_RESOURCE_ID;
    private static final ${EntityResource.className()} ONE_WITH_NO_RESOURCE_ID;


    static {
        SAMPLE_ONE = aNew${EntityResource.className()}();
        SAMPLE_TWO = aNew${EntityResource.className()}();
        SAMPLE_THREE = aNew${EntityResource.className()}();
        SAMPLE_FOUR = aNew${EntityResource.className()}();
        SAMPLE_FIVE = aNew${EntityResource.className()}();
        SAMPLE_SIX = aNew${EntityResource.className()}();
        SAMPLE_SEVEN = aNew${EntityResource.className()}();

        ONE_WITH_RESOURCE_ID = aNew${EntityResource.className()}();
        ONE_WITH_NO_RESOURCE_ID = aNew${EntityResource.className()}WithNoResourceId();
    }

    private static final List<${EntityResource.className()}> ALL_ITEMS = new ArrayList<>();

    static {
        ALL_ITEMS.add(SAMPLE_ONE);
        ALL_ITEMS.add(SAMPLE_TWO);
        ALL_ITEMS.add(SAMPLE_THREE);
        ALL_ITEMS.add(SAMPLE_FOUR);
        ALL_ITEMS.add(SAMPLE_FIVE);
        ALL_ITEMS.add(SAMPLE_SIX);
        ALL_ITEMS.add(SAMPLE_SEVEN);
    }

    public static List<${EntityResource.className()}> allItems() { return ALL_ITEMS; }

    public static ${EntityResource.className()} sampleOne() { return SAMPLE_ONE; }
    public static ${EntityResource.className()} sampleTwo() { return SAMPLE_TWO; }
    public static ${EntityResource.className()} sampleThree() { return SAMPLE_THREE; }

    /**
     * For those instances when you want to verify behavior against a
     * ${EntityResource.className()} that has a resourceId.
     */
    public static ${EntityResource.className()} oneWithResourceId() { return ONE_WITH_RESOURCE_ID; }

    /**
     * For those instances when you want to verify behavior against a
     * ${EntityResource.className()} that does not have a resourceId assigned.
     */
    public static ${EntityResource.className()} oneWithoutResourceId() { return ONE_WITH_NO_RESOURCE_ID; }

    public static ${EntityResource.className()} copyOf(${EntityResource.className()} copy) {
        return ${EntityResource.className()}.builder()
            .resourceId(copy.getResourceId())
            .text(copy.getText())
            .build();
    }

    /**
     * Create a sample ${EntityResource.className()}
     */
    private static ${EntityResource.className()} aNew${EntityResource.className()}() {
        return ${EntityResource.className()}.builder()
            .resourceId(randomSeries.nextResourceId())
            .text(faker.book().title()) // TODO: replace with a value meaningful to your business object
            .build();
    }


    private static ${EntityResource.className()} aNew${EntityResource.className()}WithText(String text) {
        return ${EntityResource.className()}.builder()
            .resourceId(randomSeries.nextResourceId())
            .text(text)
            .build();
    }

    private static ${EntityResource.className()} aNew${EntityResource.className()}WithNoResourceId() {
        return ${EntityResource.className()}.builder()
            .text(faker.book().title()) // TODO: replace with a value meaningful to your business object
            .build();
    }
}
