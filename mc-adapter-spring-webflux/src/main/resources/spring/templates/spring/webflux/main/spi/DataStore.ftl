<#include "/common/Copyright.ftl">

package ${AbstractDataStoreApi.packageName()};

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This is the SPI for database interactions
 */
public interface ${AbstractDataStoreApi.className()}<T> {

    // Find by the resource ID
    Mono<T> findByResourceId(String id);

    // Find by the database ID
    Mono<T> findById(Long id);

    // Returns the number of entities deleted
    Mono<Long> deleteByResourceId(String id);

    Flux<T> findAllByText(String text);

    Flux<T> findAll();
}
