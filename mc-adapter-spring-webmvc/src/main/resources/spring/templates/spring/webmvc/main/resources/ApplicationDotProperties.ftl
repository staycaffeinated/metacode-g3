# -------------------------------------------------------------------------------------------------------
# Custom properties (not part of Spring)
# These property names (and values, of course) can be changed to your liking.
# -------------------------------------------------------------------------------------------------------
application:
  default-page-limit: 25

# -------------------------------------------------------------------------------------------------------
# Spring properties
# -------------------------------------------------------------------------------------------------------
server:
  port: 8080
  servlet:
<#if (project.basePath)??>
    context-path: ${project.basePath}
<#else>
    context-path: /
</#if>

<#if (project.isWithOpenApi())>
# -------------------------------------------------------------------------
# OpenApi/SpringDoc/Swagger properties
# -------------------------------------------------------------------------
springdoc:
  api-docs:
    enabled: true
    path: /api-docs
  model-converters:
    pageable-converter:
      enabled: true
  swagger-ui:
    enabled: true
    operations-sorter: alpha
    tags-sorter: alpha
</#if>

# -------------------------------------------------------------------------
# Actuator properties
# -------------------------------------------------------------------------
management:
  endpoint:
    health:
      probes:
        enabled: true

# -------------------------------------------------------------------------
# Spring properties
# -------------------------------------------------------------------------

spring:
  application:
<#if (project.applicationName)??>
    name: ${project.applicationName}
<#else>
    name: example-service
</#if>
<#if (project.schema?has_content)>
    schema-name: ${project.schema}
</#if>

  jpa:
    show-sql: true
<#if (project.isWithPostgres())>
    database: POSTGRESQL
</#if>
    properties:
      hibernate:
        id:
          new_generator_mappings: false
<#if (project.schema?has_content)>
        default_schema: <#noparse>"${spring.application.schema-name}"</#noparse>
</#if>
<#if (project.isWithPostgres())>
        dialect: org.hibernate.dialect.PostgreSQLDialect
</#if>

<#-- define the jdbc driver -->
<#if (project.isWithPostgres())>
  datasource:
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/postgres
<#else>
  datasource:
    username: root
    password: secret
    driver-class-name: org.h2.Driver
<#if (project.schema)??>
    url: jdbc:h2:mem:${project.schema}
<#elseif (project.applicationName)??>
    url: jdbc:h2:mem:${project.applicationName}
<#else>
    url: jdbc:h2:mem:testdb
</#if>
</#if>
    # -------------------------------------------------------------------------------------------
    # Hikari connection pool properties. See: https://github.com/brettwooldridge/HikariCP
    # -------------------------------------------------------------------------------------------
    hikari:
      connection-timeout: "2000"
      maximum-pool-size: 20
      minimum-idle: 2
      pool-name: "springboot-hikari-cp"
      max-lifetime: 1000000
      data-source-properties:
        cachePrepStmts: true
<#if (project.schema?has_content)>
  <#if (project.isWithPostgres())>
        # Identifies the schema to be set on the search path. This schema is used to resolve unqualified object names
        # See: https://jdbc.postgresql.org/documentation/publicapi/org/postgresql/PGProperty.html#CURRENT_SCHEMA
  </#if>
        currentSchema: <#noparse>"${spring.application.schema-name}"</#noparse>
</#if>
        # Size of the prepared statement cache
        prepStmtCacheSize: 250
        # Maximum length of a statement the driver will cache
        prepStmtCacheSqlLimit: 2048
        useServerPrepStmts: true
        useLocalSessionState: true
        rewriteBatchStatements: true
        cacheResultsSetMetadata: true
        cacheServerConfiguration: true
        elideSetAutoCommits: true
        maintainTimeStats: false
<#if (project.isWithPostgres())>
        # Identifies which service the connection is for.
        # See: https://jdbc.postgresql.org/documentation/publicapi/org/postgresql/PGProperty.html#APPLICATION_NAME
        ApplicationName: <#noparse>"${spring.application.name}"</#noparse>
</#if>

<#if (project.isWithLiquibase())>
  liquibase:
    enabled: true
    change-log: "db/changelog/db.changelog-master.yaml"
</#if>

<#if (project.isWithKafka())>
  # -------------------------------------------------------------------------------------------
  # Kafka
  # -------------------------------------------------------------------------------------------
  kafka:
    bootstrap-servers: "localhost:9092"
    consumer:
      auto-commit-interval: "1s"
      bootstrap-servers: <#noparse>"${spring.kafka.bootstrap-servers}"</#noparse>
      client-id: "default-client-id"
      enable-auto-commit: true
      group-id: <#noparse>"${spring.application.name}"</#noparse>
      key-deserializer: "org.apache.kafka.common.serialization.StringDeserializer"
      value-deserializer: "org.apache.kafka.common.serialization.StringDeserializer"
    producer:
      bootstrap-servers: <#noparse>"${spring.kafka.bootstrap-servers}"</#noparse>
      key-serializer: "org.apache.kafka.common.serialization.StringSerializer"
      value-serializer: "org.apache.kafka.common.serialization.StringSerializer"
      properties:
        acks: "all"
        retries: 10
        retry.backoff.ms: "1000"
    listener:
      ack-count: 3
      client-id: <#noparse>"${spring.application.name}"</#noparse>
      concurrency: 2
      missing-topics-fatal: false
</#if>

# -------------------------------------------------------------------------
# Logging
# -------------------------------------------------------------------------
logging:
  level:
    root: INFO
