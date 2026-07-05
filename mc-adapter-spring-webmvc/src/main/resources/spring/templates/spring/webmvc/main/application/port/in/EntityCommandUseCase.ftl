<#include "/common/Copyright.ftl">
package ${EntityCommandUseCase.packageName()};

import ${EntityResource.fqcn()};
import java.util.Optional;

public interface ${EntityCommandUseCase.className()} {
    ${EntityResource.className()} create${EntityResource.className()}(${EntityResource.className()} pojo);
    Optional<${EntityResource.className()}> update${EntityResource.className()}(${EntityResource.className()} pojo);
    Optional<${EntityResource.className()}> delete${EntityResource.className()}ByResourceId(String resourceId);
}
