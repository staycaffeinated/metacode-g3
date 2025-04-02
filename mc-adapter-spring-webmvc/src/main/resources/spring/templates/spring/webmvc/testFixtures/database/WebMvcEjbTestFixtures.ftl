package ${EjbTestFixtures.packageName()};

import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${Entity.fqcn()};
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;

/**
* Sample ${Entity.className()} objects suitable for test data
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
    private static final ${Entity.className()} SAMPLE_ONE;
    private static final ${Entity.className()} SAMPLE_TWO;
    private static final ${Entity.className()} SAMPLE_THREE;
    private static final ${Entity.className()} SAMPLE_FOUR;
    private static final ${Entity.className()} SAMPLE_FIVE;
    private static final ${Entity.className()} SAMPLE_SIX;
    private static final ${Entity.className()} SAMPLE_SEVEN;

    private static final ${Entity.className()} ONE_WITH_RESOURCE_ID;
    private static final ${Entity.className()} ONE_WITHOUT_RESOURCE_ID;

    /*
     * Useful for query tests where multiple records have the same text
     */
    private static final ${Entity.className()} SAME_TEXT_ONE;
    private static final ${Entity.className()} SAME_TEXT_TWO;
    private static final ${Entity.className()} SAME_TEXT_THREE;

    static {
        SAMPLE_ONE = aNew${Entity.className()}();
        SAMPLE_TWO = aNew${Entity.className()}();
        SAMPLE_THREE = aNew${Entity.className()}();
        SAMPLE_FOUR = aNew${Entity.className()}();
        SAMPLE_FIVE = aNew${Entity.className()}();
        SAMPLE_SIX = aNew${Entity.className()}();
        SAMPLE_SEVEN = aNew${Entity.className()}();

        ONE_WITH_RESOURCE_ID = aNew${Entity.className()}();
        ONE_WITHOUT_RESOURCE_ID = aNew${Entity.className()}WithNoResourceId();

        String matchingValue = faker.book().title(); // TODO: replace with something meaningful to your business objects
        SAME_TEXT_ONE = aNew${Entity.className()}WithText(matchingValue);
        SAME_TEXT_TWO = aNew${Entity.className()}WithText(matchingValue);
        SAME_TEXT_THREE = aNew${Entity.className()}WithText(matchingValue);
    }

    public static List<${Entity.className()}> allItems() {
        return List.of(copyOf(SAMPLE_ONE),
                       copyOf(SAMPLE_TWO),
                       copyOf(SAMPLE_THREE),
                       copyOf(SAMPLE_FOUR),
                       copyOf(SAMPLE_FIVE),
                       copyOf(SAMPLE_SIX),
                       copyOf(SAMPLE_SEVEN));
    }


    public static List<${Entity.className()}> allItemsWithSameText() {
        return List.of(copyOf(SAME_TEXT_ONE),
                    copyOf(SAME_TEXT_TWO),
                    copyOf(SAME_TEXT_THREE));
    }

    public static ${Entity.className()} sampleOne() { return copyOf(SAMPLE_ONE); }
    public static ${Entity.className()} sampleTwo() { return copyOf(SAMPLE_TWO); }
    public static ${Entity.className()} sampleThree() { return copyOf(SAMPLE_THREE); }

    /**
     * For those instances when you want to verify behavior against a
     * ${Entity.className()} that has a resourceId.
     */
    public static ${Entity.className()} oneWithResourceId() {
        return copyOf(ONE_WITH_RESOURCE_ID);
    }

    /**
     * For those instances when you want to verify behavior against a
     * ${Entity.className()} that has no resourceId assigned.
     */
    public static ${Entity.className()} oneWithoutResourceId() {
        return copyOf(ONE_WITHOUT_RESOURCE_ID);
    }

    /**
     * Create a sample ${Entity.className()}
     */
    private static ${Entity.className()} aNew${Entity.className()}() {
        return ${Entity.className()}.builder()
            .resourceId(randomSeries.nextResourceId())
            .text(faker.book().title()) // TODO: replace with a value meaningful to your business object
            .build();
    }


    private static ${Entity.className()} aNew${Entity.className()}WithText(String text) {
        return ${Entity.className()}.builder()
            .text(text)
            .build();
    }

    private static ${Entity.className()} aNew${Entity.className()}WithNoResourceId() {
        return ${Entity.className()}.builder()
            .text(faker.book().title()) // TODO: replace with a value meaningful to your business object
            .build();
    }

    /**
     * Instances of test fixture entities have a risk of being modified by a test.
     * For instance, if `repository.save(someEntity)` is called, the entity
     * instance will be assigned a database ID. To avoid propagating these
     * mutations across various test cases, a new, clean copy of entities can
     * be made via this method.
     */
    private static ${Entity.className()} copyOf(${Entity.className()} entity) {
        return ${Entity.className()}.builder()
            .text(entity.getText())
            .resourceId(entity.getResourceId())
            .build();
    }

}
