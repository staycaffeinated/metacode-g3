<#include "/common/Copyright.ftl">
package ${endpoint.basePackage}.database.${endpoint.lowerCaseEntityName};

import ${endpoint.basePackage}.spi.DataStore;
import ${endpoint.basePackage}.domain.${endpoint.entityName};


/**
 * A dataStore for ${endpoint.entityName} domain objects. Add custom methods
 * here, such as ${endpoint.entityName}-specific query methods.
 */
public interface ${endpoint.entityName}DataStore extends DataStore<${endpoint.entityName}> {
}