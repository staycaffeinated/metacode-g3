<#if (endpoint.isWithPostgres())>
    <#if (endpoint.schema?has_content)>
<#-- Postgres + specific schema -->
<#-- If you opt for using sequences instead of identity change 'serial' to 'int8' -->
<#-- and explicitly create the sequence object, _and_ fix the entity class to use the sequence object -->
create table if not exists ${endpoint.schema}.${endpoint.lowerCaseEntityName} (
    id             SERIAL PRIMARY KEY,
    resource_id    varchar(60) NOT NULL UNIQUE,
    text            varchar(255)
    );
    <#else>
<#-- Postgres w default schema -->
create table if not exists ${endpoint.lowerCaseEntityName} (
    id             SERIAL PRIMARY KEY,
    resource_id    varchar(60) NOT NULL UNIQUE,
    text           varchar(255)
    );
    </#if>
<#elseif !(endpoint.isWithMongoDB())>
    <#if (endpoint.schema?has_content)>
<#-- H2 database + named schema -->
create table if not exists ${endpoint.schema}.${endpoint.lowerCaseEntityName} (
     id int primary key,
     resourceId varchar(60) not null unique,
     text varchar(255)
     );
    </#if>
<#-- Hibernate auto-creates tables of H2 database when no specific schema is specified -->
</#if>
