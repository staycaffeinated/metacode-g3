<#include "/common/Copyright.ftl">

package ${ObjectDataStore.packageName()};

import ${EntityResource.fqcn()};
import ${DataStoreApi.fqcn()};
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * DataStore for ${endpoint.entityName} domain objects. This interface
 * extends the basic {@code DataStore} interface, adding a
 * ${Entity.className()}-specific search API.
 */
public interface ${ObjectDataStore.className()} extends ${DataStoreApi.className()}<${EntityResource.className()}> {
    Page<${EntityResource.className()}> findByText(@NonNull Optional<String> text, Pageable pageable);

    /**
     * Returns a Page of ${endpoint.entityName} items that satisfy the {@code searchQuery}
     * The search query is expressed in RSQL; see
     * <a href="https://github.com/perplexhub/rsql-jpa-specification">RSQL Query Syntax</a> for query syntax
     */
    Page<${EntityResource.className()}> search(@NonNull String searchQuery, Pageable pageable);

}
