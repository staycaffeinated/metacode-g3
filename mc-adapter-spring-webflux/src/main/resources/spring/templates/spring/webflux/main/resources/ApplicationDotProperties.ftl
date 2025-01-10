
<#if (project.applicationName)??>
spring.application.name=${project.applicationName}
<#else>
spring.application.name=example-service
</#if>
server.port=8080
<#if (project.basePath)??>
spring.webflux.base-path=${project.basePath}
<#else>
spring.webflux.base-path=/
</#if>
spring.main.web-application-type=reactive
<#if (project.schema?has_content)>
spring.application.schema-name=${project.schema}
</#if>

<#if (project.isWithOpenApi())>
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
</#if>

# Obfuscate the /actuator endpoint
# Health probes enable a liveness check, and a readiness check.
# Docker containers are commonly deployed via Kubernetes.
# These health probes enable K8S to monitor the health of this service.
# If this service is deployed via K8S, the K8S deployment.yaml should
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

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.id.new_generator_mappings=false

<#-- define the jdbc driver -->
<#if (project.reactiveMongo)??>
# Reactive MongoDB
# spring.datasource.driver-class-name=org.postgresql.Driver
</#if>
<#-- define the jdbc url -->
<#if (project.isWithPostgres())>
    <#if (project.schema)??>
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/postgres${project.schema}
    <#else>
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/postgres
    </#if>
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgresPlusDialect
spring.r2dbc.username=postgres
spring.r2dbc.password=postgres
<#else>
    <#if (project.schema)??>#
spring.r2dbc.url=r2dbc:h2:mem:///${project.schema}
    <#elseif (project.applicationName)??>
spring.r2dbc.url=r2dbc:h2:mem:///${project.applicationName}
    <#else>
spring.r2dbc.url=r2dbc:h2:mem:///testdb
    </#if>
spring.r2dbc.username=root
spring.r2dbc.password=secret
</#if>

# -------------------------------------------------------------------------
# Logging
# -------------------------------------------------------------------------
logging.level.root=INFO

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
