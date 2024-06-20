<#include "/common/Copyright.ftl">
package ${Repository.className()};

import ${CustomRepository.fqcn()};

public interface ${Repository.className()} extends CustomRepository<${Entity.className()}, Long> {
}

