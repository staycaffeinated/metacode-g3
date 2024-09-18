## Uncommitted

* Changes
  * Added an integration test of the `[Resource]Specification.java` class. This enables
    verifying the behavior of the Specification against a database.

## [11.2.0] - 2024-09-14

* Changes:
  * The Jacoco Report Aggregation plugin has been added to the `build.gradle` files,
    along with the necessary configuration changes. This plugin makes it easier to
    include code coverage from integration tests (and any other test facets you might 
    want to use) with the Sonar reports.
  * The POJO's that represent endpoint resources have a new package home, 
    `${basepath}.${endpoint}.model` (instead of `${basepath}.${endpoint}.provides.api`).
    I noticed many projects use explicit Request/Response objects, such as `AddPetRequest`
    and `AddPetResponse`. Creating a `model` package to hold those kinds of POJOs, as well
    as including the generated POJO in `model` made sense.
  * Instead of having an `applyBeforeInsertSteps` and `applyBeforeUpdateSteps` in the `DataStore`
    API, a new converter interface, `UpdateAwareConverter`, has been added. This interface
    is implemented by the PojoToEJB/PojoToDocument converters that already exist. As attributes
    are added to POJOs/EJBs, those converters have to be updated any. Not having to also maintain
    those `applyBeforeInsertSteps` and `applyBeforeUpdateSteps` simplifies the code maintenance
    when attributes are added to the POJOs/EJBs/Documents.
  * The DataFaker library has been added as a test dependency, enabling tests to leverage it
    to generate test data. This is more flexible and useful than the hard-coded values that
    the code generate was using. 
  * An new "Specification" class has been added to enable building Specification instances 
    for database queries much easier. Each EJB has a corresponding Specification class that
    has a Builder interface. Examples of this new Specification class being used can be found
    in the integration tests.
  * The support of database schema's was simplified. Rather than adding the `schema` attribute
    within the `@Entity` annotation, the schema information is contained in the `application.properties`
    file. The schema name is defined with the `spring.application.schema-name` property, along with
    these entries:
    * `spring.jpa.properties.hibernate.default_schema=${spring.application.schema-name}`
    * `spring.datasource.hikari.data-source-properties.currentSchema=${spring.application.schema-name}`


## [11.1.0] - 2024-08-25

* Changes
  * Changed the Java package layout a bit. First, a `common`
    package has been added to the base package, and `common` is now
    the parent package of `exceptions, persistence`,  and `traits`. 
    Second, the package `infra` has been renamed to `infrastructure`.

* Maintenance:
  * Updated H2 from 2.3.230 to 2.3.232
  * Updated Reactor Test from 3.6.2 to 3.6.9
  * Updated Spring Boot from 3.3.2 to 3.3.3
  * Updated Lombok Gradle plugin from 8.6 to 8.10
  * Updated Gradle Lint plugin from 19.0.2 to 19.0.3
  * Updated Sonarqube Gradle plugin from 5.0.0.4638 to 5.1.0.4882
  * Updated Spring Cloud from 4.1.3 to 4.1.4
  * Updated Jib Gradle plugin from 3.4.2 to 3.4.3

## [11.0.0-11.0.2] - 2024-08-11

* Changes:
  * The package layout of the generated code was reworked to
    better support a hexagon-style architecture. 
  * Internally, the code generator itself moved to Spring framework
    (from Guice). This is making code maintenance a little easier. 
  * Some deployment issues caused creating several minor releases on the same day


## [10.0.3] - 2024-05-20
* Changes
    * Removed deprecated spring property, `spring.mvc.throw-exception-if-no-handler-found`

* Maintenance
  * Update Spring Dependency Management plugin from 1.1.4 to 1.1.5
  * Updated OpenAPI libraries from 2.3.0 to 2.5.0
  * Updated Spring ORM from 6.1.4 to 6.1.6
  * Updated Spring Cloud Starter from 4.1.1 to 4.1.2
  * Updated TestContainers from 1.19.6 to 1.19.7 

## [10.0.2] - 2024-02-24 
                
* Maintenance
  * Updated Spring Boot plugin from 3.2.1 to 3.2.3
  * Updated Ben Manes' Version plugin from 0.51.0 to 0.52.0
  * Updated TestContainers from 1.19.3 to 1.19.6
  * Updated Spring Cloud Starter from 4.1.0 to 4.1.1 
  * Updated Spring ORM from 6.1.2 to 6.1.4
  * Updated PostgreSQL driver from 42.7.1 to 42.7.2
  * Updated AssertJ from 3.25.1 to 3.25.3

## [10.0.1] - 2023-01-13

* Fixed
  * A Spring WebMvc controller that returned ```Page<T>``` has a unit
    test that was failing. That controller method was refactored to
    use a PagedResourceAssembler to assemble the pages. This ties back
    to <link href="https://github.com/spring-projects/spring-data-commons/issues/2919">Spring Data Commons:Issues:2919<link>

* Maintenance
  * Updated junit-jupiter-bom from 5.10.0 to 5.10.1
  * Updated assertj-core from 3.24.2 to 3.25.1
  * Updated SpringCloud from 4.0.1 to 4.1.0
  * Spring ORM from 6.1.1 to 6.1.2
  * SpringDoc OpenAPI from 2.2.0 to 2.3.0
  * Postgres Driver from  42.7.0 to 42.7.1
  * Reactor Test from 3.5.10 to  3.6.2

## [10.0.0] - 2023-12-02

* Maintenance
  * Dependency management plugin from 1.1.3 to 1.1.4
  * Ben Manes Versions plugin from 0.49.0 to 0.50.0
  * TestContainers from 1.19.0 to 1.19.3         
  * Spring ORM from 6.0.13 to 6.1.1
  * PostgreSQL driver from 42.6.0 to 42.7.0
          
* Added
  * The project structure is closer to Gradle's idiomatic structure. Specifically,
    the project root directory now contains a subproject, ```the-app```,
    that contains the ```src``` directory. This makes it easier to add other
    subprojects.
  * Added these "internal" Gradle plugins for more idiomatic build.gradle files:
    * org.example.application-conventions.gradle
    * org.example.library-conventions.gradle
    * org.example.subproject-configurations.gradle

      
## [9.2.4] - 2023-10-11

* Fixed
    * Removed incorrect import statements from controller integration tests

* Bumped Versions
    * Spring Boot plugin from 3.1.4 to 3.1.5
    * Lombok plugin from 8.3 to 8.4
    * Ben Manes Versions from 0.48.0 to 0.49.0
    * Spring ORM library from 6.0.12 to 6.0.13
     
* Added
  * SpringBoot Properties Migrator library to version catalog

## [9.2.3] - 2023-09-30

* Fixed
  * Corrected a spelling error in the name of the <i>springCloudStarterStreamKafka</i> library

* Bumped Versions
  * Sonarqube from 4.3.0.3225 to 4.3.1.3277
  * Ben Manes Versions from 0.47.0 to 0.48.0
  * H2 database driver from 2.2.220 to 2.2.224
  * ReactorTest from 3.5.9 to 3.5.10
  * Nebula Lint plugin from 18.0.3 to 18.1.0
  * Lombok library from 1.18.28 to 1.18.30
    * the Lombok plugin is still pulling lombok:1.18.28  
  * Spring ORM from 6.0.11 to 6.0.12
  * Spring Boot plugin from 3.1.3 to 3.1.4

## [9.2.2] - 2023-09-02

* Bumped Versions
  * SpringBoot from 3.1.2 to 3.1.3
  * JUnit Jupiter from 5.9.3 to 5.10.0
  * OpenAPI Starter from  2.1.0 to 2.2.0
  * Spring Dependency Management plugin from 1.1.2 to 1.1.3
  * Lombok plugin from 8.1.0 to 8.3
  * Reactor Test from 3.5.8 to 3.5.9
  * TestContainers from 1.18.3 to 1.19.0

## [9.2.1] - 2023-08-12

### Fixed

* Replaced instances of hard-coded strings with variable references. 
  In particular, some test classes used "text" or "$.text" to reference the default
  property named _text_ that the code generator provides. Now, instead of using
  a hard-coded string, a variable name is used in the tests. This makes refactoring 
  easier by reducing that redundancy. 
      
### Maintenance

* Bumped Versions
    * Sonar Gradle plugin from 4.0.0.2929 to 4.3.0.3225
    * Spotless Gradle plugin from 6.18.0 to 6.20.0
    * Jib Gradle plugin from 3.3.1 to 3.3.2


## [9.2.0] - 2023-08-06

### Added

* Added support for Spring's spring-boot-testcontainers library. This caused integration
  tests to be refactored since the configuration a Testcontainer now happens in the
  ContainerConfiguration class (this class is generated by MetaCode). 

* An inner-class has been added to the POJO and EJB classes.  These inner classes
  enumerate the properties of the POJO and EJB. The integration and unit tests reference
  these inner classes instead of using hard-coded values; this makes refactoring
  significantly easier. 

### Maintenance

* Bumped Versions
  * SpringBoot from 3.1.1 to 3.1.2
  * Spring ORM from 6.0.10 to 6.0.11
  * Spring Dependency Management plugin from 1.1.0 to 1.1.2
  * Reactor Test from 3.5.7 to 3.5.8
  * H2 database from 2.1.214 to 2.2.220



## [9.1.0] - 2023-07-06
      

### Added

* The Spring WebMvc and Spring Webflux templates now generate these
  exception classes, since these are typically needed:
  * UnprocessableEntityException
  * ResourceNotFoundException
  * BadRequestException

* An ApplicationConfiguration class is now generated, which defines
  a bean for the ```ResourceIdSupplier```.

* The ```application.properties``` files contain additional Hikari
  connection pool properties, making it easier to transition to a 
  production environment by including property settings suitable for production.

### Maintenance

* Bumped versions:
  * SpringBoot Gradle plugin from 3.1.0 to 3.1.1
  * Ben Manes Version plugin from 0.46.0 to 0.47.0
  * Reactor Test library from 3.5.5 to 3.5.7
  * TestContainers-BOM from 1.18.0 to 1.18.3
  * SpringCloud Starter from 4.0.2 to 4.0.3

## [9.0.2] - 2023-06-24

### Fixed

* Removed the _@EnableBatchProcessing_ from the main Application.java file for Spring Batch
  applications.  Using _@EnableBatchProcessing_ causes Spring to create a _DataSourceTransactionManager_,
  which is unaware of JPA/Hibernate, which can cause unexpected behavior. If you need it, add it back it; 
  batch jobs _can_ work without it in Spring Framework 6.
* The generated _BatchConfiguration.java_ class now has _@Configuration_ instead of _@Component_. 
* The spring-orm:6.0.10 library is explicitly included in build.gradle file of spring-webmvc 
  and spring-webflux projects. Older versions attempt to load the PostgreSQL95Dialect, which
  Hibernate deprecated and removed from their latest jars.  This removal leads to 
  _ClassNotFoundException: PostgresSQL95Dialect_.  The new dialect is _PostgresPlusDialect_, 
  which the spring-orm:6.0.10 library uses. 
* The generated _application.properties_ files were updated to use the _PostgresPlusDialect_. 


## [9.0.1] - 2023-06-16

### Fixed

* An empty _schema.sql_ file was being written if no schema was specified. An empty _schema.sql_ file
  prevents Spring from booting up the application.  The _schema.sql_ file is only written when the 
  _--schema_ option is used to define the database schema used by the generated application.

## [9.0.0] - 2023-06-15

### Added

* Idiomatic Gradle Projects
  * The Gradle scripts have been reworked to follow the idiomatic style
    for organizing projects.  With that, the Gradle subdirectory now has three 
    subdirectories 
    * platform -- the platform configuration
    * plugins -- the project's basic configuration conventions (sonar, lint, docker, spotless, testing)
    * settings -- the project settings, such as repository URLs
    
    Version catalogs are also used; the Gradle subdirectory contains a
    _libs.versions.toml_ file, which contains the library dependencies.
  
    The new Gradle files work with Gradle 7.6 and 8.1. 

### Maintenance

* Updated various libraries
  * Lombok
  * Spring Boot Gradle plugin


## [8.0.0] - 2023-05-15

### Added

* Gradle jvm-test-suite Plugin Support

  * Previous versions used the Coditory plugin to configure integration tests.
        That plugin has been replaced with Gradle's 'jvm-test-suite' plugin. With
        this change, integration tests are now found in the 'src/integrationTest/java'
        folder.  A suite was also added for performance tests, if you want to leverage that.

* Database Schema Support

  * Support for database schemas has been added. If your tables need to be contained
        within a specific schema, use the command line option '-S [schema] or --schema [schema]'.
        If you use schemas, its suggested to also use TestContainers, since Metacode will
        create the schema within the test container (Hibernate does not do that automatically),
        so integration tests will make queries against the table within the schema.

* OpenAPI Support

  * Support for OpenAPI has been added for Spring WebMVC and Spring Webflux projects.
  When creating the project, include the command line option ```--openapi```
  to generate the OpenAPI artifacts.


### Fixed

* When generating Spring WebMVC projects that used Postgres and TestContainers,
      the integration tests were not always behaving as expected.  Those generated tests
      have been refactored to use an explicity PostgresContainerTests class which, so far,
      has proven to be more dependable.

* With the updated Problem library, the generated GlobalExceptionHandler
      does not need a handler for jakarta.validjation.ConstraintViolation; the Problem library
      now supports it.  Its worth noting the Problem library's handler _will_ return
      a stack trace in the response, which is a bad practice for client-facing applications,
      since stack traces are considered an information leak.

* The Spotless plugin is used for code formatting. In the Gradle files, a dependency was
      added between the 'check' task and the 'spotlessApply' task, so that running 'gradlew check'
      automatically triggers 'spotlessApply'.

### Maintenance

* Updated various libraries and plugins:
  * Spring Boot
  * Lombok
  * JUnit Jupiter
  * Problem Spring Web
  * TestContainers


## [7.0.0] - 2023-03-28

### Added
* A DataStore class has been added to encapsulate the business rules around
  persistence.  The DataStore API deals in Domain objects. Behind the scenes,
  the DataStore handles the EntityBeans and Repositories that enable reading
  and writing to the database. The Service components have also been refactored
  to interface with the DataStore instead of the Repository. The DataStore is
  only available in the spring-webmvc templates in this first release.

* Added MongoDB support for spring-webmvc projects. Test containers can also be
  used with MongoDB, but that's still an 'early-access' option, as there is a
  known problem that's still being investigated.

* Introduced an interface class to define the methods of implemented by the Service class.
  This helps enforce a separation-of-concerns between the controller and service
  classes.

### Maintenance

* Updated these libraries and plugins:
    * AssertJ
    * Ben Manes gradle plugin
    * Lombok
    * PostgreSQL driver
    * SonarQube plugin
    * Spotless plugin
    * Spring Boot
    * Spring Cloud

## [6.1.0] - 2023-01-21

### Added
  * Added jakarta.persistence-api library to WebFlux and WebMvc build.gradle files.
    This fixes a compile-time warning.
  * Webflux projects now generate integration tests when using PostgreSQL and test containers.
    The integration tests spin up an instance of the web application and an in-memory instance of
    a PostgreSQL database using test containers.

### Maintenance
  * Updated these libraries and plugins:
    * junit
    * reactor test
    * coditory integration test plugin
    * spotless plugin


## [6.0.0] - 2022-12-31

### Added
  * Upgrade to Spring Boot 3, Spring Framework 6.
    Generated code now targets Spring Framework 6 and Spring Boot 3.
    Naturally, this required that some other dependencies be updated.
    With Spring Framework 6, be aware that the 'jakarta' namespace replaces the 'javax'
    namespace; for example, 'javax.persistence' is now 'jakarta.persistence'.
  * Changed the SecureRandomSeries to produce alphanumeric resource Ids instead
    of all-numeric resource Ids (the method that produces the all-numeric values
    is still in the SecureRandomSeries class). This led to refactoring some
    other classes.
  * Added additional Kafka related libraries to dependencies.gradle:
    * Apache Kafka
    * Apache Kafka Client
    * Spring Cloud Starter Stream Kafka
    * Spring Cloud Binder Kakfa Streams


### Maintenance
  * Updated these libraries and plugins:
    * spring gradle plugin
    * lombok gradle plugin
    * spring cloud starter
    * reactor test
    * test containers
    * r2dbc:h2


## [5.5.0] - 2022-11-24

### Added
  * Added some basic Kafka libraries to the dependencies.gradle file. The goal is
    to make it easier to start a Kafka application. The build.gradle file is not affected.

### Maintenance
  * Updated these libraries to their latest versions:
    * ben manes gradle plugin
    * spring cloud starter
    * test containers
    * liquibase core
    * postgres jdbc driver

## [5.4.0] - 2022-11-09

### Fixed
  * Metacode allowed the user to create a resource named 'Public', which is a reserved
    word in Java. During code generation, the 'Public' resource name gets mapped to a
    package named 'public' (for example, say, "acme.cinema.public"), which leads to compile-time errors.
    This is fixed.

### Changed
  * Use Amazon Corretto as the base Docker image used by the Jib plugin.
    The default base image, OpenJDK, has been deprecated; using a supported
    base image makes more sense.
  * Pin the snakeyaml version to fix known CVEs. SpringBoot currently pulls in
    snakeyaml:1.30 which has a known CVE. We've pinned the version to 1.33 (the
    latest version at this time). This change can be found at the bottom
    of the gradle/dependencies.gradle file.

### Maintenance
  * Bumped Reactor Test library to v3.5.0

## [5.3.0] - 2022-11-01

### Fixed
  * Fixed an issue that allowed a stack trace to leak into the response from a Webflux endpoint

## [5.2.0] - 2022-10-28

### Changed
  * The help message now prints the full hierarchy of commands instead of only
    printing the next depth of commands.

### Maintenance
  * Update to latest version: Sonarqube and Jib Gradle plugins

## [5.1.1] - 2022-10-22

### Maintenance
  * Update to latest versions of Spring Boot, TestContainers, JUnit Jupiter, and
    various Gradle plugins

## [5.1.0] - 2022-10-07

### Fixed
  * Fixed issues that prevented Spring Batch applications from running out-of-the-box

### Maintenance
  * Bump the dependency versions consumed by the generated code

## [5.0.0] - 2022-09-17

### Added
  * Added code generator for Spring Batch applications

### Fixed
  * Fixed Liquibase changelog templates for Spring WebMVC code

### Changed
  * The package structure for Spring WebMVC and Spring Webflux applications
    was changed.  Classes concerning persistence have been moved into a
    package named ```database```.  This change simplifies the code found within
    the ```endpoint``` packages and makes the classes concerned with persistence
    more obvious.

  * The generated entity bean classes now contain
    ```equals``` and ```hashCode``` methods
  * For Spring Boot projects, a place-holder unit test is now generated

### Maintenance
  * Bump the dependency versions consumed by the generated code


## [4.0.0] - 2022-08-24

### Added
  * Generate an integration test for the Service class under each endpoint
  * Generate an integration test for the Repository class under each endpoint
  * Improve AbstractIntegrationTest class

### Maintenance
  * Bumped versions of SpringBoot Gradle plugin, Reactor-test, and Lombok Gradle plugin.
    These are consumed by the generated code.

### Fixed
  * Generate a unit test folder for spring-boot projects

## [3.0.0] - 2022-07-15

### Changed
  * When generating a project using the `spring-boot` template,
    * the SecureRandomSeries class is no longer there
    * the LocalDateConverter class is no longer there
    * an application.properties file is now present
    * the build.gradle dependencies stanza is cleaner
  * In Spring WebMvc and Spring WebFlux projects, the RestfulResource
    interface was renamed to ResourceIdTrait, and the `stereotype`
    package was renamed to `trait`. This is a breaking change _if_
    you run `create endpoint` within a project created with a previous
    version of Metacode. Specifically, compile errors will occur that
    have to be fixed manually.
  * Renaming of generated classes:
    * The EntityBean class to simply, Entity. For example,
      the generated code will now have PetEntity.java instead of PetEntityBean.java
    * The BeanToResourceConverter is now EntityToPojoConverter
    * The ResourceToBeanConverter is now PojoToEntityConverter
  * In Spring WebFlux and Spring WebMvc projects, the package structure
    is improved.

### Maintenance
  * Bump the dependency versions consumed by the generated code

### Fixed
  * Disallow creating endpoints within a spring-boot project template
    (endpoints only make sense with spring-webmvc and spring-webflux projects)

## [2.0.0] - 2022-07-02

### Added
  * Add property to hold CORS `allowed origins` pattern.
    The property can be set with an environment variable
    or in the application.yml/properties file.
### Changed
  * The generated class, `Constants.java` is renamed to `SpringProfiles.java`.
    Within that class, the constants `PROFILE_IT` and `PROFILE_TEST` are renamed
    to `INTEGRATION_TEST` and `TEST`, respectively.
### Maintenance
  * Bump the dependency versions consumed by the generated code

## [1.2.1] - 2022-06-27
### Fixed
  * Added 'User' to the list of disallowed words. Hibernate generates invalid SQL
    when the table name is 'User'; the SQL error prevents the corresponding database
    table from being created, which subsequently causes integration tests to fail

## [1.2.0] - 2022-06-25
### Maintenance
  * Bump the dependency versions consumed by the generated code
  * Transition a deprecated Spring class to its replacement

## [1.1.0] - 2022-06-01
### Added
* In build.gradle, only one tag is now assigned to the docker image.
### Fixed
  * Added property for Coditory integration plugin
### Maintenance:
  * Bump the dependency versions consumed by the generated code

## [1.0.0] - 2022-05-17

A minimum, lovable product has been achieved.

### Added
  * Improved the README
  * Added a LICENSE file

### Changed
  * Renamed the `--support` command-line option to `--add`
  * Added explicit version for PostgreSQL library instead of letting the Spring Gradle plugin decide the version
  * Fixed spelling error in a ValidationMessages.properties file

## [0.2.0] - 2022-04-23

### Added
* Add database populators to webmvc projects (webflux projects already have them)
### Changed
* Improvements to generated code motivated by code analyzer reports

## [0.1.10] - 2022-04-19
### Fixed
* Version was not showing when using '--version' option

## [0.1.9] - 2022-04-18
### Fixed
* Forgot to generate ValidationMessages.properties in webflux projects

## [0.1.8] - 2022-04-15
### Added
* Generated code uses latest dependency versions
* Generate a LocalDateConverter to demonstrate handling LocalDate query parameters
### Changed
* Resource IDs to have 160-bit entropy

## [0.1.7] - 2022-04-01
### Fixed
* Spring WebMvc projects were producing Spring WebFlux code

## [0.1.6] - 2022-03-27
### Added
* Generated code uses latest dependency versions
* Generate artifacts for Postgres and TestContainers when creating a Spring WebFlux project
### Fixed
* Resolved compile errors in generated code

## [0.1.5] - 2022-03-23
### Added
* Generate basic Spring Webflux project
### Changed
* Improve generated test classes for better code coverage

## [0.1.4] - 2022-03-22
### Added
* Generate Postgres, TestContainers, and Liquibase artifacts for Spring WebMvc projects
### Changed
* Improve generated test classes for better code coverage
### Fixed
* Resolve code smells in generated code

## [0.1.3] - 2022-03-15
### Fixed
* Internal bug fixes and improvements
* Bug fix: bad URL requests in Spring WebFlux applications returned stack trace in the response

## [0.1.2] - 2022-03-15
### Added
* Generate a basic Spring WebMvc project
### Fixed
* The endpoint URLs were incorrect

## [0.1.1] - 2022-03-15
### Fixed
* Internal bug fixes
* Bug fix: ensure endpoint routes begin with front-slash
* Various bug fixes in generated code
### Changed
* Changed the content of the copyright header that appears in generated source code

## [0.1.0] - 2022-01-19
### Added
* Generate a basic Spring WebMvc project

## [0.0.0] - 2022-01-18
### Added
* Initial code commit
