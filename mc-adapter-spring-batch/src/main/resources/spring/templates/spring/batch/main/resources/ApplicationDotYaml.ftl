server:
  port: 8080
<#if (project.basePath)??>
  servlet.context-path: ${project.basePath}
<#else>
  servlet.context-path: /
</#if>

spring:
  main:
    banner-mode: "off"
    allow-circular-references: false
<#if (project.applicationName)??>
  application.name: ${project.applicationName}
<#else>
  application.name: batch-service
</#if>
  batch:
    jdbc:
        # The schema has to be defined, otherwise Batch cannot determine the database type.
        # This is the SQL script SpringBatch runs to create its own tables, like
        # BATCH_JOB_INSTANCE, BATCH_JOB_EXECUTION, BATCH_JOB_EXECUTION_PARAMS, etc.
        # See github.com/spring-projects/spring-batch/issues/1026
        schema: classpath:org/springframework/batch/core/schema-postgresql.sql

        # This property encourages Spring to create the spring-batch-specific database tables
        initialize-schema: always
  datasource:
<#if (project.isWithPostgres())>
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/postgres
    # These are example credentials; change them
    username: postgres
    password: postgres
  jpa:
    # Vendor list is found here: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/orm/jpa/vendor/Database.html
    vendor: POSTGRESQL
    properties:
      hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect
  sql:
    init:
      # If you want this Batch application to create its own schema, you can do something like this:
      # schema-location: classpath:database/postgresql/create-schema.sql
</#if>
logging:
  level:
    root: INFO
    org.springframework.batch: INFO
  management:
    endpoint:
      health:
        probes:
          enabled: true
    endpoints:
      web:
        base-path: /_internal

<#if (project.isWithKafka())>
  # -------------------------------------------------------------------------------------------
  # Kafka
  # -------------------------------------------------------------------------------------------
  kafka:
    bootstrap-servers: localhost:9092
    consumer.auto-commit-interval: 1s
    consumer.enable-auto-commit: true
    <#noparse>consumer.group-id: ${spring.application.name}</#noparse>
    consumer.key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    consumer.value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    consumer.client-id: default-client-id
    <#noparse>consumer.bootstrap-servers: ${spring.kafka.bootstrap-servers}</#noparse>

    producer.key-serializer: org.apache.kafka.common.serialization.StringSerializer
    producer.value-serializer: org.apache.kafka.common.serialization.StringSerializer
    <#noparse>producer.bootstrap-servers: ${spring.kafka.bootstrap-servers}</#noparse>
    producer.properties.acks: all
    producer.properties.retries: 10
    producer.properties.retry.backoff.ms: 1000

    listener.concurrency: 2
    listener.missing-topics-fatal: false
    <#noparse>listener.client-id: ${spring.application.name}</#noparse>
    listener.ack-count: 3
</#if>
