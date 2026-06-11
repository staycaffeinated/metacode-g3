<#include "/common/Copyright.ftl">
package ${ServiceApi.packageName()};

import ${EntityResource.fqcn()};
import ${OnCreateAnnotation.fqcn()};
import ${OnUpdateAnnotation.fqcn()};
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

public interface ${ServiceApi.className()} {

    List<${endpoint.entityName}> findAll${endpoint.entityName}s();

    Optional<${EntityResource.className()}> find${endpoint.entityName}ByResourceId(String id);

    Page<${EntityResource.className()}> findByText(@NonNull String text, Pageable pageable);

    ${EntityResource.className()} create${endpoint.entityName}(@NonNull @Validated(${OnCreateAnnotation.className()}.class) ${EntityResource.className()} resource);

    List<${EntityResource.className()}> update${endpoint.entityName}(@NonNull @Validated(${OnUpdateAnnotation.className()}.class) @Valid ${EntityResource.className()} resource);

    void delete${endpoint.entityName}ByResourceId(@NonNull String id);
}