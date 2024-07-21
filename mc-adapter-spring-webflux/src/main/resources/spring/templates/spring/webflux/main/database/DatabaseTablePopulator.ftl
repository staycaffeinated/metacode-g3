<#include "/common/Copyright.ftl">
package ${DatabaseTablePopulator.packageName()};

import ${ResourceIdSupplier.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${Repository.fqcn()};
import ${Entity.fqcn()};
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * This component populates the ${endpoint.tableName} database table with sample data.
 * This is suitable for testing and demonstration, but probably not
 * what you want in production. 
 */
@Component
@Slf4j
public class ${DatabaseTablePopulator.className()} implements ApplicationListener<ApplicationReadyEvent> {

	private final ${Repository.className()} repository;
	private final ${ResourceIdSupplier.className()} resourceIdSupplier;

	/**
	 * Constructor
	 */
	public ${DatabaseTablePopulator.className()} (${Repository.className()} repository, ${ResourceIdSupplier.className()} resourceIdSupplier)
	{
	    this.repository = repository;
	    this.resourceIdSupplier = resourceIdSupplier;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
	    repository.deleteAll()
	              .thenMany(Flux.just("One", "Two", "Three", "Four", "Five").map(this::buildSampleRecord)
 	              .flatMap(repository::save))
	              .thenMany(repository.findAll()).subscribe(pet -> log.info("Saving " + pet.toString()));
	}

	/**
	 * Creates a sample database record
	 */
	private ${endpoint.ejbName} buildSampleRecord(String text) {
	    return ${endpoint.ejbName}.builder().resourceId(resourceIdSupplier.nextResourceId()).text(text).build();
	}
}