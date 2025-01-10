
<#if (project.applicationName)??>
spring.application.name=${project.applicationName}
<#else>
spring.application.name=example-service
</#if>
server.port=8080
<#if (project.basePath)??>
server.servlet.context-path=${project.basePath}
<#else>
server.servlet.context-path=/
</#if>

<#if (project.isWithOpenApi())>
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
</#if>

# Obfuscate the /actuator endpoint, which is the default health probe.
# Health probes enable a liveness check and a readiness check.
# Since Docker containers are commonly deployed via Kubernetes,
# these health probes enable Kubernetes to monitor the health of this service.
# If this service is deployed via Kubernetes, the Kubernetes deployment.yaml should
# include:
#   livenessProbe:
#     httpGet:
#       path: /_internal/health/liveness
#       port: 8080
#   readinessProbe:
#     httpGet:
#       path: /_internal/health/readiness
#       port: 8080
management.endpoints.web.base-path=/_internal
management.endpoint.health.probes.enabled=true

<#if (project.isWithPostgres())>
# ---------------------------------
# Postgres settings
# ---------------------------------
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.id.new_generator_mappings=false

spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=root
spring.datasource.password=secret

    <#if (project.schema)??>
spring.datasource.url=jdbc:postgresql://localhost:5432/${project.schema}
    <#elseif (project.applicationName)??>
spring.datasource.url=jdbc:postgresql://localhost:5432/${project.applicationName}
    <#else>
spring.datasource.url=jdbc:postgresql://localhost:5432/testdb
    </#if>
</#if>

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
