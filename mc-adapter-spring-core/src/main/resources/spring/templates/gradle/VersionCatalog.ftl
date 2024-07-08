[versions]
apacheKafka = '${project.apacheKafkaVersion}'
assertJ = '${project.assertJVersion}'
h2 = '${project.h2Version}'
junitSystemRules =  '${project.h2Version}'
junit = '${project.junitVersion}'
liquibase = '${project.liquibaseVersion}'
lombok = '${project.lombokVersion}'
lombokPlugin = '${project.lombokPluginVersion}'
mockito = '${project.mockitoVersion}'
nebulaLint = '19.0.2'
openApiStarterWebMvc = '${project.openApiStarterWebMvcVersion}'
openApiStarterWebflux = '${project.openApiStarterWebfluxVersion}'
postgresql = '${project.postgresqlVersion}'
rsql = '6.0.21'
r2dbcH2 = '${project.r2dbc_h2Version}'
r2dbcPostgres = '${project.r2dbc_postgresVersion}'
r2dbcSpi = '${project.r2dbc_spiVersion}'
sonar = '${project.sonarqubeVersion}'
springBoot = '${project.springBootVersion}'
springCloud = '${project.springCloudVersion}'
springDependencyManagement = '${project.springDependencyManagementVersion}'
springOrm = '6.1.8'
problemJacksonDataType = '${project.problemJacksonDataTypeVersion}'
problemSpringWeb = '${project.problemSpringWebVersion}'
reactorTest = '${project.reactorTestVersion}'
testContainers = '${project.testContainersVersion}'
truth = '${project.truthVersion}'
versions = '${project.benManesPluginVersion}'


[libraries]
apacheKafka = { module = "org.apache.kafka:kafka_2.13", version.ref = "apacheKafka" }
apacheKafkaClients = { module = "org.apache.kafka:kafka-clients", version.ref = "apacheKafka" }
assertJ = { module = "org.assertj:assertj-core", version.ref = "assertJ" }
h2 = { module = "com.h2database:h2", version.ref = "h2" }
jakartaPersistenceApi = { module = "jakarta.persistence:jakarta.persistence-api" }
jacksonDatatypeJsr310 = { module = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" }
junitBillOfMaterial = { module = "org.junit:junit-bom", version.ref="junit" }
junitJupiter = { module = "org.junit.jupiter:junit-jupiter" }
junitPlatformRunner = { module = "org.junit.platform:junit-platform-runner" }
junitSystemRules = { module = "com.github.stefanbirkner:system-rules", version.ref = "junitSystemRules" }
kafkaClients = { module = "org.apache.kafka:kafka-clients" }
kafkaStreams = { module = "org.apache.kafka:kafka-streams" }
kafkaStreamsTest = { module = "org.apache.kafka:kafka-streams-test-utils" }
lombok = { module = "org.projectlombok:lombok", version.ref = "lombok" }
liquibaseCore = { module = "org.liquibase:liquibase-core", version.ref = "liquibase" }
mockito = { module = "org.mockito:mockito-core", version.ref = "mockito" }
mongoDbDriverSync = { module = "org.mongodb:mongodb-driver-sync" }
openApiStarterWebMvcUI = { module = "org.springdoc:springdoc-openapi-starter-webmvc-ui", version.ref = "openApiStarterWebMvc" }
openApiStarterWebfluxUI = { module = "org.springdoc:springdoc-openapi-starter-webflux-ui", version.ref = "openApiStarterWebflux" }
rsqlJpaSpringBootStarter = { module = "io.github.perplexhub:rsql-jpa-spring-boot-starter", version.ref = "rsql" }
r2dbcH2 = { module = "io.r2dbc:r2dbc-h2", version.ref="r2dbcH2" }
r2dbcPostgres = { module = "io.r2dbc:r2dbc-postgresql", version.ref = "r2dbcPostgres" }
r2dbcSpi = { module = "io.r2dbc:r2dbc-spi", version.ref = "r2dbcSpi" }
postgresql = { module = "org.postgresql:postgresql", version.ref = "postgresql" }
problemSpringWeb = { module = "org.zalando:problem-spring-web-starter", version.ref = "problemSpringWeb" }
problemSpringWebFlux = { module = "org.zalando:problem-spring-webflux", version.ref = "problemSpringWeb" }
problemSpringWebStarter = { module = "org.zalando:problem-spring-web-starter", version.ref = "problemSpringWeb" }
problemJacksonDataType = { module = "org.zalando:jackson-datatype-problem", version.ref = "problemJacksonDataType" }
reactorTest = { module = "io.projectreactor:reactor-test", version.ref = "reactorTest" }
truth = { module = "com.google.truth:truth", version.ref = "truth" }

springBatchTest = { module = "org.springframework.batch:spring-batch-test" }
springBootConfigProcessor = { module = "org.springframework.boot:spring-boot-configuration-processor" }
springBootPropertiesMigrator = { module = "org.springframework.boot:spring-boot-properties-migrator" }
springBootStarterActuator = { module = "org.springframework.boot:spring-boot-starter-actuator" }
springBootStarterAop = { module = "org.springframework.boot:spring-boot-starter-aop" }
springBootStarterBatch = { module = "org.springframework.boot:spring-boot-starter-batch" }
springBootStarterDataJpa = { module = "org.springframework.boot:spring-boot-starter-data-jpa" }
springBootStarterDataMongoDb = { module = "org.springframework.boot:spring-boot-starter-data-mongodb" }
springBootStarterDataR2dbc = { module = "org.springframework.boot:spring-boot-starter-data-r2dbc" }
springBootStarterHateoas = { module = "org.springframework.boot:spring-boot-starter-hateoas" }
springBootStarterTest = { module = "org.springframework.boot:spring-boot-starter-test" }
springBootStarterWeb = { module = "org.springframework.boot:spring-boot-starter-web" }
springBootStarterWebFlux = { module = "org.springframework.boot:spring-boot-starter-webflux" }
springBootStarterValidation = { module = "org.springframework.boot:spring-boot-starter-validation" }
springBootTestContainers = { module = "org.springframework.boot:spring-boot-testcontainers" }
springCloud = { module = "org.springframework.cloud:spring-cloud-starter", version.ref = "springCloud" }
springCloudBinderKafkaStreams = { module = "org.springframework.cloud:spring-cloud-stream-binder-kafka-streams", version.ref = "springCloud" }
springCloudStarterStreamKafka = { module = "org.springframework.cloud:spring-cloud-starter-stream-kafka", version.ref = "springCloud" }
springDevTools = { module = "org.springframework.boot:spring-boot-devtools" }
springKafka = { module = "org.springframework.kafka:spring-kafka" }
springKafkaTest = { module = "org.springframework.kafka:spring-kafka-test" }
springOrm = { module = "org.springframework:spring-orm", version.ref="springOrm" }

testContainersBom = { module = "org.testcontainers:testcontainers-bom", version.ref = "testContainers" }
testContainersJupiter = { module = "org.testcontainers:junit-jupiter" }
testContainersKafka = { module = "org.testcontainers:kafka" }
testContainersMongoDb = { module = "org.testcontainers:mongodb" }
testContainersPostgres = { module = "org.testcontainers:postgresql" }
testContainersR2DBC = { module = "org.testcontainers:r2dbc" }

[plugins]
dependency-management = { id = "io.spring.dependency-management", version.ref="springDependencyManagement" }
lombok-plugin = { id = "io.freefair.lombok", version.ref="lombokPlugin" }
nebula-lint = { id = "nebula.lint", version.ref="nebulaLint" }
sonar = { id = "org.sonarqube", version.ref = "sonar" }
spring-boot = { id = "org.springframework.boot", version.ref="springBoot" }
versions = { id = "com.github.ben-manes.versions", version.ref="versions" }