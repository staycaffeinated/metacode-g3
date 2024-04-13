<#if (project.schema?has_content) && !(project.isWithPostgres() || project.isWithMongoDb())>
    create schema if not exists ${project.schema};
    set schema ${project.schema}
</#if>