<#include "/common/Copyright.ftl">

package ${DatabaseTablePopulator.packageName()};

import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${Repository.fqcn()};
import ${Document.fqcn()};
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This component loads sample data into the ${endpoint.tableName} database table.
 * This is suitable for testing and demonstration, but probably not
 * for production.
 */
@Component
@Slf4j
public class ${DatabaseTablePopulator.className()} implements ApplicationListener<ApplicationReadyEvent> {

    private final ${Repository.className()} repository;
    private final ${ResourceIdSupplier.className()} resourceIdSupplier;

    /**
     * Constructor
     */
    public ${DatabaseTablePopulator.className()}(${Repository.className()} repository, ${ResourceIdSupplier.className()} idSupplier) {
        this.repository = repository;
        this.resourceIdSupplier = idSupplier;
    }

    /**
     * Inserts sample data into the ${endpoint.tableName} table
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        repository.deleteAll();
        List<${Document.className()}> sampleData = createSampleData();
        repository.saveAll(sampleData);
        repository.flush();
    }

    /**
     * Creates a collection of sample data
     */
    private List<${Document.className()}> createSampleData() {
        String[] textSamples = {"One", "Two", "Three", "Four", "Five"};
        List<${Document.className()}> list = new ArrayList<>();
        for (String s : textSamples) {
            list.add(createOne(s));
        }
        return list;
    }

    /**
     * Creates a single ${endpoint.entityName} Document
     */
    private ${Document.className()} createOne(String text) {
        return ${endpoint.documentName}.builder()
            .text(text)
            .resourceId(resourceIdSupplier.nextResourceId())
            .build();
    }
}
