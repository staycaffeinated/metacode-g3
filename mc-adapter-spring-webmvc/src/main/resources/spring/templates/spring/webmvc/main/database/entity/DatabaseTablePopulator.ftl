<#include "/common/Copyright.ftl">

package ${DatabaseTablePopulator.packageName()};

import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${Entity.fqcn()};
import ${Repository.fqcn()};
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
    * Inserts sample data into the ${EntityResource.className()} table
    */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        repository.deleteAll();
        List<${Entity.className()}> sampleData = createSampleData();
        repository.saveAllAndFlush(sampleData);
    }

    /**
     * Creates a collection of sample data
     */
    private List<${Entity.className()}> createSampleData() {
        String[] textSamples = {"One", "Two", "Three", "Four", "Five"};
        List<${Entity.className()}> list = new ArrayList<>();
        for (String s : textSamples) {
            list.add(createOne(s));
        }
        return list;
    }

    /**
     * Creates a single ${Entity.className()} entity bean
     */
    private ${Entity.className()} createOne(String text) {
        ${Entity.className()} bean = new ${Entity.className()}();
        bean.setText(text);
        bean.setResourceId(resourceIdSupplier.nextResourceId());
        return bean;
    }
}