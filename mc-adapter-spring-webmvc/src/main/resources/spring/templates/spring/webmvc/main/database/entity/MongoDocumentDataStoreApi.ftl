<#include "/common/Copyright.ftl">

/*
 * MongoDocumentDataStoreApi.ftl
 */

package ${ConcreteDocumentStoreApi.packageName()};

import ${EntityResource.fqcn()};
import ${AbstractDataStoreApi.fqcn()};


/**
 * A dataStore for ${endpoint.entityName} domain objects. Add custom methods
 * here, such as ${endpoint.entityName}-specific query methods.
 */
public interface ${ConcreteDocumentStoreApi.className()} extends ${AbstractDataStoreApi.className()}<${EntityResource.className()}> {
}