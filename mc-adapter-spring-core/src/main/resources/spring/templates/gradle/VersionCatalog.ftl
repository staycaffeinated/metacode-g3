[versions]
assertJ = '${project.versions["assertJ"]}'
datafaker = '${project.versions["datafaker"]}'
h2 = '${project.versions["h2"]}'
junitSystemRules =  '${project.versions["junitSystemRules"]}'
junit = '${project.versions["junit"]}'
liquibase = '${project.versions["liquibase"]}'
lombok = '${project.versions["lombok"]}'
lombokPlugin = '${project.versions["lombokPlugin"]}'
mockito = '${project.versions["mockito"]}'
nebulaLint = '${project.versions["nebulaLint"]}'
openApiStarterWebMvc = '${project.versions["openApiStarterWebMvc"]}'
openApiStarterWebflux = '${project.versions["openApiStarterWebflux"]}'
postgresql = '${project.versions["postgres"]}'
rsql = '${project.versions["rsql"]}'
r2dbcH2 = '${project.versions["r2dbc_h2"]}'
r2dbcPostgres = '${project.versions["r2dbc_postgres"]}'
r2dbcSpi = '${project.versions["r2dbc_spi"]}'
sonar = '${project.versions["sonarqube"]}'
springBoot = '${project.versions["springBoot"]}'
springCloud = '${project.versions["springCloud"]}'
springDependencyManagement = '${project.versions["springDependencyManagement"]}'
springOrm = '${project.versions["springOrm"]}'
problemJacksonDataType = '${project.versions["problemJacksonDataType"]}'
problemSpringWeb = '${project.versions["problemSpringWeb"]}'
reactorTest = '${project.versions["reactorTest"]}'
testContainers = '${project.versions["testContainers"]}'
truth = '${project.versions["truth"]}'
versions = '${project.versions["benManesPlugin"]}'


[libraries]
assertJ = { module = "org.assertj:assertj-core", version.ref = "assertJ" }
datafaker = { module = "net.datafaker:datafaker", version.ref = "datafaker" }
h2 = { module = "com.h2database:h2", version.ref = "h2" }
jakarta-persistence-api = { module = "jakarta.persistence:jakarta.persistence-api" }
jackson-datatype-jsr310 = { module = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" }
junit-bom = { module = "org.junit:junit-bom", version.ref="junit" }
junit-jupiter = { module = "org.junit.jupiter:junit-jupiter" }
junit-platform-runner = { module = "org.junit.platform:junit-platform-runner" }
junit-system-rules = { module = "com.github.stefanbirkner:system-rules", version.ref = "junitSystemRules" }
lombok = { module = "org.projectlombok:lombok", version.ref = "lombok" }
liquibase-core = { module = "org.liquibase:liquibase-core", version.ref = "liquibase" }
mockito = { module = "org.mockito:mockito-core", version.ref = "mockito" }
mongodb-driver-sync = { module = "org.mongodb:mongodb-driver-sync" }
openapi-starter-webmvc-ui = { module = "org.springdoc:springdoc-openapi-starter-webmvc-ui", version.ref = "openApiStarterWebMvc" }
openapi-starter-webflux-ui = { module = "org.springdoc:springdoc-openapi-starter-webflux-ui", version.ref = "openApiStarterWebflux" }
rsql-jpa-spring-boot-starter = { module = "io.github.perplexhub:rsql-jpa-spring-boot-starter", version.ref = "rsql" }
r2dbc-h2 = { module = "io.r2dbc:r2dbc-h2", version.ref="r2dbcH2" }
r2dbc-postgres = { module = "io.r2dbc:r2dbc-postgresql", version.ref = "r2dbcPostgres" }
r2dbc-spi = { module = "io.r2dbc:r2dbc-spi", version.ref = "r2dbcSpi" }
postgresql = { module = "org.postgresql:postgresql", version.ref = "postgresql" }
problem-spring-web-starter = { module = "org.zalando:problem-spring-web-starter", version.ref = "problemSpringWeb" }
problem-spring-webflux = { module = "org.zalando:problem-spring-webflux", version.ref = "problemSpringWeb" }
problem-jackson-datatype = { module = "org.zalando:jackson-datatype-problem", version.ref = "problemJacksonDataType" }
reactor-test = { module = "io.projectreactor:reactor-test", version.ref = "reactorTest" }
truth = { module = "com.google.truth:truth", version.ref = "truth" }

spring-batch-test = { module = "org.springframework.batch:spring-batch-test" }
spring-boot-config-processor = { module = "org.springframework.boot:spring-boot-configuration-processor" }
spring-boot-properties-migrator = { module = "org.springframework.boot:spring-boot-properties-migrator" }
spring-boot-starter-actuator = { module = "org.springframework.boot:spring-boot-starter-actuator" }
spring-boot-starter-aop = { module = "org.springframework.boot:spring-boot-starter-aop" }
spring-boot-starter-batch = { module = "org.springframework.boot:spring-boot-starter-batch" }
spring-boot-starter-data-jpa = { module = "org.springframework.boot:spring-boot-starter-data-jpa" }
spring-boot-starter-data-mongodb = { module = "org.springframework.boot:spring-boot-starter-data-mongodb" }
spring-boot-starter-data-r2dbc = { module = "org.springframework.boot:spring-boot-starter-data-r2dbc" }
spring-boot-starter-hateoas = { module = "org.springframework.boot:spring-boot-starter-hateoas" }
spring-boot-starter-test = { module = "org.springframework.boot:spring-boot-starter-test" }
spring-boot-starter-web = { module = "org.springframework.boot:spring-boot-starter-web" }
spring-boot-starter-webflux = { module = "org.springframework.boot:spring-boot-starter-webflux" }
spring-boot-starter-validation = { module = "org.springframework.boot:spring-boot-starter-validation" }
spring-boot-testcontainers = { module = "org.springframework.boot:spring-boot-testcontainers" }
spring-cloud = { module = "org.springframework.cloud:spring-cloud-starter", version.ref = "springCloud" }
spring-cloud-binder-kafka-streams = { module = "org.springframework.cloud:spring-cloud-stream-binder-kafka-streams", version.ref = "springCloud" }
spring-cloud-starter-stream-kafka = { module = "org.springframework.cloud:spring-cloud-starter-stream-kafka", version.ref = "springCloud" }
spring-devtools = { module = "org.springframework.boot:spring-boot-devtools" }
spring-kafka = { module = "org.springframework.kafka:spring-kafka" }
spring-kafka-test = { module = "org.springframework.kafka:spring-kafka-test" }
spring-orm = { module = "org.springframework:spring-orm", version.ref="springOrm" }

testcontainers-bom = { module = "org.testcontainers:testcontainers-bom", version.ref = "testContainers" }
testcontainers-jupiter = { module = "org.testcontainers:junit-jupiter" }
testcontainers-kafka = { module = "org.testcontainers:kafka" }
testcontainers-mongodb = { module = "org.testcontainers:mongodb" }
testcontainers-postgres = { module = "org.testcontainers:postgresql" }
testcontainers-r2dbc = { module = "org.testcontainers:r2dbc" }

[plugins]
dependency-management = { id = "io.spring.dependency-management", version.ref="springDependencyManagement" }
lombok-plugin = { id = "io.freefair.lombok", version.ref="lombokPlugin" }
nebula-lint = { id = "nebula.lint", version.ref="nebulaLint" }
sonar = { id = "org.sonarqube", version.ref = "sonar" }
spring-boot = { id = "org.springframework.boot", version.ref="springBoot" }
versions = { id = "com.github.ben-manes.versions", version.ref="versions" }