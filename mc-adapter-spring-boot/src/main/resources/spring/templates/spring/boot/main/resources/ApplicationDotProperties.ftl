
server:
  port: 8080
  servlet:
<#if (project.basePath)??>
    context-path: ${project.basePath}
<#else>
    context-path: /
</#if>

<#if (project.isWithOpenApi())>
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
</#if>

management:
  endpoint:
    health:
      probes:
        enabled: true

spring:
  application:
<#if (project.applicationName)??>
    name: ${project.applicationName}
<#else>
    name: example-service
</#if>

<#if (project.isWithPostgres())>
  jpa:
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        id:
          new_generator_mappings: false

  datasource:
    driver-class-name: org.postgresql.Driver
    username: root
    password: secret
    url: jdbc:postgresql://localhost:5432
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
