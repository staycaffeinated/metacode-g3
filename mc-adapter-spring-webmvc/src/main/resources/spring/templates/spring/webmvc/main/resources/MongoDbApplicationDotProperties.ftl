
server:
  port: 8080
<#if (project.basePath)??>
  servlet:
    context-path=${project.basePath}
<#else>
    context-path=/
</#if>

logging:
  level:
    root: INFO

spring:
  application:
<#if (project.applicationName)??>
    name: ${project.applicationName}
<#else>
    name: example-service
</#if>
  data:
    mongodb:
      uri: mongodb://localhost:27017/testdb
      database: testdb

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
