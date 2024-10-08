<#include "/common/Copyright.ftl">
package ${DocumentTestFixtures.packageName()};

import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${Document.fqcn()};
import net.datafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sample ${Document.className()} objects
 */
public class ${DocumentTestFixtures.className()} {

    private static final ${ResourceIdSupplier.className()} ID_SUPPLIER = new ${SecureRandomSeries.className()}();

    private static Faker faker = new Faker();

    private static final ${Document.className()} SAMPLE_ONE;
    private static final ${Document.className()} SAMPLE_TWO;
    private static final ${Document.className()} ONE_WITH_RESOURCE_ID;

    static {
        SAMPLE_ONE = aNew${Document.className()}();
        SAMPLE_TWO = aNew${Document.className()}();
        ONE_WITH_RESOURCE_ID = aNew${Document.className()}();
    }

    private static final List<${Document.className()}> ALL_ITEMS = new ArrayList<>();

    static {
        ALL_ITEMS.add(SAMPLE_ONE);
        ALL_ITEMS.add(SAMPLE_TWO);
    }
    public static List<${Document.className()}> allItems() { return ALL_ITEMS; }

    public static ${Document.className()} sampleOne() { return SAMPLE_ONE; }
    public static ${Document.className()} sampleTwo() { return SAMPLE_TWO; }
    public static ${Document.className()} oneWithResourceId() { return SAMPLE_TWO; }


    public static ${Document.className()} copyOf(${Document.className()} someDocument) {
        // @formatter:off
        return ${Document.className()}.builder()
            .text(someDocument.getText())
            .resourceId(someDocument.getResourceId())
            .build();
        // @formatter:on
    }

    /* ===============================================================================
    * HELPER METHODS
    * =============================================================================== */
    private static ${Document.className()} aNew${Document.className()}WithText(String text) {
        // @formatter:off
         return ${Document.className()}.builder()
                .text(text)
                .resourceId(ID_SUPPLIER.nextResourceId())
                .build();
         // @formatter:on
    }

    private static ${Document.className()} aNew${Document.className()}() {
        // @formatter:off
        return ${Document.className()}.builder()
                .text(faker.book().title()) // TODO: replace with something meaningful to your business object
                .resourceId(ID_SUPPLIER.nextResourceId())
                .build();
        // @formatter:on
    }
}