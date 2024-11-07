<#include "/common/Copyright.ftl">
package ${Routes.packageName()};

/**
 * Routes to ${endpoint.entityName} resources
 */
@SuppressWarnings({"java:S1075"})
// S1075: let basePath be hard-coded
public final class ${Routes.className()} {

    // sealed
    private ${Routes.className()}() {}

    public static final String ${endpoint.routeConstants.basePath} = "${endpoint.route}";
    public static final String ${endpoint.routeConstants.idParameter} = "/{id}";

    public static final String ${endpoint.routeConstants.create} = ${endpoint.routeConstants.basePath};
    public static final String ${endpoint.routeConstants.update} = ${endpoint.routeConstants.basePath} + ${endpoint.routeConstants.idParameter};
    public static final String ${endpoint.routeConstants.delete} = ${endpoint.routeConstants.basePath} + ${endpoint.routeConstants.idParameter};

    public static final String ${endpoint.routeConstants.findOne} = ${endpoint.routeConstants.basePath} + ${endpoint.routeConstants.idParameter};
    public static final String ${endpoint.routeConstants.findAll} = ${endpoint.routeConstants.basePath} + "/findAll";

    /* Use the value of a specific field to find items */
    public static final String ${endpoint.routeConstants.findByProperty} = ${endpoint.routeConstants.basePath} + "/findByProperty";

    /* Use RSQL queries to find items */
    public static final String ${endpoint.routeConstants.search} = ${endpoint.routeConstants.basePath} + "/search";

}