<#if (endpoint.schema?has_content)>
    <#if endpoint.isWithPostgres()>
create table if not exists ${endpoint.schema}.${endpoint.lowerCaseEntityName} (
    id             SERIAL PRIMARY KEY,
    resource_id    varchar(60) NOT NULL UNIQUE,
    text            varchar(255)
);
    <#elseif !(endpoint.isWithMongoDB())>  <#-- in other words, is using H2 database -->
create table if not exists ${endpoint.schema}.${endpoint.lowerCaseEntityName} (
    id int primary key,
    resourceId varchar(60) not null unique,
    text varchar(255)
);
    </#if>
</#if>