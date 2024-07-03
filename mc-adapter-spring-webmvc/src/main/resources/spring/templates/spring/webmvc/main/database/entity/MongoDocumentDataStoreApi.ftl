<#include "/common/Copyright.ftl">

/*
 * MongoDocumentDataStoreApi.ftl
 */

package ${DocumentKindStore.packageName()};

import ${EntityResource.fqcn()};
import ${DataStoreApi.fqcn()};


/**
 * A dataStore for ${endpoint.entityName} domain objects. Add custom methods
 * here, such as ${endpoint.entityName}-specific query methods.
 */
public interface ${DocumentKindStore.className()} extends ${DataStoreApi.className()}<${EntityResource.className()}> {
}