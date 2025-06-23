
# -------------------------------------------------------------------------
# Server
# -------------------------------------------------------------------------
server:
  port: 8080
  webflux:
<#if (project.basePath)??>
    base-path=${project.basePath}
<#else>
    base-path=/
</#if>

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

# -------------------------------------------------------------------------------------------
# Hikari
# See https://springframework.guru/hikari-configuration-for-mysql-in-spring-boot-2/
# and https://github.com/brettwooldridge/HikariCP
# -------------------------------------------------------------------------------------------
# the maximum time a client will wait for a connection
spring.datasource.hikari.connection-timeout=2000
# the maximum size the pool can reach
spring.datasource.hikari.maximum-pool-size=20
# cache prepared statements
spring.datasource.hikari.data-source-properties.cachePrepStmts=true
<#if (project.schema?has_content)>
<#noparse>
spring.datasource.hikari.data-source-properties.currentSchema="${spring.application.schema-name}"
</#noparse>
</#if>
# size of prepared statement cache
spring.datasource.hikari.data-source-properties.prepStmtCacheSize=250
# the maximum length of a statement the driver will cache
spring.datasource.hikari.data-source-properties.prepStmtCacheSqlLimit=2048
# enable using server-side prepared statements if the DMBS supports it (eg., MySQL)
spring.datasource.hikari.data-source-properties.useServerPrepStmts=true
spring.datasource.hikari.data-source-properties.useLocalSessionState=true
spring.datasource.hikari.data-source-properties.rewriteBatchedStatements=true
spring.datasource.hikari.data-source-properties.cacheResultsSetMetadata=true
spring.datasource.hikari.data-source-properties.cacheServerConfiguration=true
# sets the default auto-commit behavior of connections
spring.datasource.hikari.data-source-properties.elideSetAutoCommits=true
spring.datasource.hikari.data-source-properties.maintainTimeStats=false
spring.datasource.hikari.pool-name=spring-boot-hikari-postgresql-cp
spring.datasource.hikari.max-lifetime=1000000

<#if (project.isWithKafka())>
# -------------------------------------------------------------------------------------------
# Kafka
# -------------------------------------------------------------------------------------------
spring.kafka.bootstrap-servers=localhost:9092

spring.kafka.consumer.auto-commit-interval=1s
spring.kafka.consumer.enable-auto-commit=true
<#noparse>spring.kafka.consumer.group-id=${spring.application.name}</#noparse>
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.client-id=default-client-id
<#noparse>spring.kafka.consumer.bootstrap-servers=${spring.kafka.bootstrap-servers}</#noparse>
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
<#noparse>spring.kafka.producer.bootstrap-servers=${spring.kafka.bootstrap-servers}</#noparse>
spring.kafka.producer.properties.acks=all
spring.kafka.producer.properties.retries=10
spring.kafka.producer.properties.retry.backoff.ms=1000

spring.kafka.listener.concurrency=2
spring.kafka.listener.missing-topics-fatal=false
<#noparse>spring.kafka.listener.client-id=${spring.application.name}</#noparse>
spring.kafka.listener.ack-count=3
</#if>
