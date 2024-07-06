<#include "/common/Copyright.ftl">
package ${Repository.packageName()};

import ${CustomRepository.fqcn()};
import ${Entity.fqcn()};

public interface ${Repository.className()} extends ${CustomRepository.className()}<${Entity.className()}, Long> {
}

