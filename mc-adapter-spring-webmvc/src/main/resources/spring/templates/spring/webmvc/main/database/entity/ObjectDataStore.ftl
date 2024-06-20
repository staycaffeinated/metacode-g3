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
public interface ${ObjectDataStore.className()} extends ${GenericDataStore.className()}<${EntityResource.className()}> {
    Page<${EntityResource.className()}> findByText(@NonNull Optional<String> text, Pageable pageable);
}
