<#include "/common/Copyright.ftl">

package ${DatabaseTablePopulator.packageName()};

import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
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

    private final ${endpoint.entityName}Repository repository;
    private final ${ResourceIdSupplier.className()} resourceIdSupplier;

    /**
     * Constructor
     */
    public ${DatabaseTablePopulator.className()}(${endpoint.entityName}Repository repository, ${ResourceIdSupplier.className()} idSupplier) {
        this.repository = repository;
        this.resourceIdSupplier = idSupplier;
    }

    /**
    * Inserts sample data into the ${endpoint.tableName} table
    */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        repository.deleteAll();
        List<${endpoint.ejbName}> sampleData = createSampleData();
        repository.saveAllAndFlush(sampleData);
    }

    /**
     * Creates a collection of sample data
     */
    private List<${endpoint.ejbName}> createSampleData() {
        String[] textSamples = {"One", "Two", "Three", "Four", "Five"};
        List<${endpoint.ejbName}> list = new ArrayList<>();
        for (String s : textSamples) {
            list.add(createOne(s));
        }
        Arreturn list;
    }

    /**
     * Creates a single ${endpoint.entityName} entity bean
     */
    private ${endpoint.ejbName} createOne(String text) {
        ${endpoint.ejbName} bean = new ${endpoint.ejbName}();
        bean.setText(text);
        bean.setResourceId(resourceIdSupplier.nextResourceId());
        return bean;
    }
}