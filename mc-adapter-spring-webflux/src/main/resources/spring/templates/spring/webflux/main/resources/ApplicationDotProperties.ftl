
# -------------------------------------------------------------------------
# Server
# -------------------------------------------------------------------------
server:
  port: 8080


# -------------------------------------------------------------------------
# Logging
# -------------------------------------------------------------------------
logging:
  level:
    root: INFO

# -------------------------------------------------------------------------
# SpringDoc/OpenApi
# -------------------------------------------------------------------------
<#if (project.isWithOpenApi())>
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
</#if>

# -------------------------------------------------------------------------
# Actuator
# -------------------------------------------------------------------------
management:
  endpoint:
    health:
      probes:
        enabled: true

# -------------------------------------------------------------------------
# Spring
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

  main:
    web-application-type: reactive

  webflux:
<#if (project.basePath)??>
    base-path: ${project.basePath}
<#else>
    base-path: /
</#if>

<#if (project.isWithPostgres())>
  datasource:
    driver-class-name: org.postgresql.Driver
    hikari:
      connection-timeout: "2000"
      maximum-pool-size: 20
      minimum-idle: 2
      pool-name: springboot-hikari-cp
      max-lifetime: 1000000
      data-source-properties:
        cachePrepStmts: true
        <#if (project.schema?has_content)>
        currentSchema: <#noparse>${spring.application.schema-name}"</#noparse>
        </#if>
        prepStmtCacheSize: 250
        # The maximum length of a stmt the driver will cache
        prepStmtCacheSqlLimit: 2048
        useServerPrepStmts: true
        useLocalSessionState: true
        rewriteBatchStatements: true
        cacheResultsSetMetadata: true
        cacheServerConfiguration: true
        elideSetAutoCommits: true
        maintainTimeStats: false
        ApplicationName: <#noparse>"${spring.application.name}"</#noparse>

  jpa:
    show-sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    properties:
      hibernate:
        id:
          new_generator_mappings: false
  r2dbc:
    username: postgres
    password: postgres
    url: r2dbc:postgresql://localhost:5432/postgres
<#else>
  r2dbc:
    username: root
    password: secret
    <#if (project.schema)??>
    url: r2dbc:h2:mem:///${project.schema}
    <#else>
    url: r2dbc:h2:mem:///testdb
    </#if>
</#if>

<#if (project.isWithKafka())>
  # -------------------------------------------------------------------------------------------
  # Kafka
  # -------------------------------------------------------------------------------------------
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      auto-commit-interval: 1s
      bootstrap-servers: <#noparse>${spring.kafka.bootstrap-servers}</#noparse>
      client-id: default-client-id
      enable-auto-commit: true
      group-id: <#noparse>${spring.application.name}</#noparse>
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      bootstrap-servers: <#noparse>${spring.kafka.bootstrap-servers}</#noparse>
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      properties:
        acks: all
        retries: 10
        retry:
          backoff:
            ms: 1000
    listener:
      ack-count: 3
      client-id: <#noparse>${spring.application.name}</#noparse>
      concurrency: 2
      missing-topics-fatal: false
</#if>

