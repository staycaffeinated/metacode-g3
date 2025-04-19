/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.endpoint.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.annotations.jacoco.ExcludeFromJacocoGeneratedReport;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import mmm.coffee.metacode.spring.project.model.SpringTemplateModel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * RestEndpointTemplateModel
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ExcludeFromJacocoGeneratedReport // Ignore code coverage for this class
@SuppressWarnings({"java:S125", "java:S116"})
@Slf4j
public class RestEndpointTemplateModel extends SpringTemplateModel {
    // When this object is passed into Freemarker,
    // it's assigned a name referred to in Freemarker lingo
    // as the "top level variable".
    private final String topLevelVariable = Key.ENDPOINT.value();

    // The resource name as given by the end-user on the command line.
    private String resource;

    /**
     * The relative path to this endpoint (relative to the base path)
     * For example, the basePath may be "/petstore", with a route to the
     * Pet resource being "/pet", with the fully-qualified path being "/petstore/pet".
     */
    private String route;

    /**
     * The base java package of the application, such as 'org.acme.petstore'
     */
    private String basePackage;

    /**
     * The file system path equivalent of the base package; such as 'org/acme/petstore'
     */
    private String basePackagePath;

    /**
     * The base URL path of the application, such as '/petstore'
     */
    private String basePath;

    /**
     * The resource's base class name, such as 'Pet'
     */
    private String entityName;

    /**
     * The variable name used in source code for instances of the resource
     */
    private String entityVarName;

    /**
     * The entity name in lower case letters. This is used in package names.
     * For example, with a base package 'org.acme.petstore' and a 'Pet' resource,
     * one package name is 'org.acme.petstore.endpoint.pet'; this is the use case
     * where the lower-case version of the entity name gets used.
     */
    private String lowerCaseEntityName;

    /**
     * The name of the POJO class for the resource. The generated code generates
     * a class for the database model (ejbName) and the business model (pojoName).
     */
    private String pojoName;

    /**
     * The name of the database class name for the resource.
     */
    private String ejbName;

    /**
     * The name of the Document class when using NoSQL databases.
     * Documents have a role equivalent to EJBs
     */
    private String documentName;

    /**
     * The package name into which this endpoint's classes are placed.
     * For example, this might look like 'org.acme.petstore.endpoint.pet',
     * with all Pet-related classes placed there, or 'org.acme.petstore.endpoint.owner'
     * with all Owner-related classes placed there.
     */
    private String packageName;

    /**
     * The file system path equivalent of the package name, say,
     * 'org/acme/petstore/endpoint/pet/'
     */
    private String packagePath;

    /**
     * This property sets the 'schema' attribute of the @Table annotation.
     */
    private String schema;

    /**
     * JPA allows a TableName to be specified with an annotation, when the
     * default table name used by JPA isn't what's wanted. This property
     * sets the value used in the JPA annotation.
     */
    private String tableName;

    private JavaArchetypeDescriptor archetypeDescriptor;

    /**
     * Provides the constant names used in the Routes class.
     * The constant names are intentionally sensitive to the
     * entity name to avoid DuplicatedBlocks errors from Sonarqube,
     * which, by default, are Major severity.
     */
    private RouteConstants routeConstants;

    @Setter(AccessLevel.PUBLIC)
    private boolean withTestContainers;

    @Setter(AccessLevel.PUBLIC)
    private boolean withPostgres;

    @Setter(AccessLevel.PUBLIC)
    private boolean withLiquibase;

    @Setter(AccessLevel.PUBLIC)
    private boolean withMongoDB;

    @Setter(AccessLevel.PUBLIC)
    private boolean withOpenApi;

    //
    // These get methods are added because Freemarker templates
    // can get confused by Lombok naming conventions
    //
    public boolean getPostgresFlag() {
        return withPostgres;
    }

    public boolean getTestContainersFlag() {
        return withTestContainers;
    }

    public boolean getLiquibaseFlag() {
        return withLiquibase;
    }

    public boolean getMongoDbFlag() {
        return withMongoDB;
    }

    public boolean getOpenApiFlag() {
        return withOpenApi;
    }

    /*
     * Constructs the file name to be used for the [nn]-create-[table].sql script.
     * The method has to conjure 2 values: the next numeric sequence (nn), and the
     * name of the table being created.  The general idea is, if the end user creates
     * several endpoints, say, Pet, Owner, and Store, then these scripts may be created:
     * 01-create-schema.sql
     * 02-create-pet-table.sql
     * 03-create-owner-table.sql
     * 04-create-store-table.sql
     *
     * The numeric prefixes help ensure the scripts run in the correct order, especially
     * the first script that creates the schema.
     * The creation of these scripts is necessary _iff_ a schema is used _and_
     * neither Flyway nor Liquibase are used.  When tables are stored in a schema, Hibernate
     * does not auto-magically create tables _within_ the schema. For example, without a schema,
     * Hibernate generates the SQL statement "create table pet (...)" However, _with_ a schema,
     * Hibernate still generates the SQL "create table pet (...)" instead of, for instance,
     * "create table petstore.pet (...)" (in this example, `petstore` is the schema name).
     * When libraries like Flyway or Liquibase are used, the tables are correctly created w/in their
     * schemas. Its only when a database schema is used but a library that runs its own db scripts
     * is not used.
     *
     * In production, its presumed the end-user manages 
     */
    public String createTableScriptName() {
        long fileCount = fileCount(scriptFolder());
        return String.format("%02d-create-%s-table.sql", ++fileCount, getLowerCaseEntityName());
    }

    private long fileCount(File folder) {
        log.info("[fileCount] folder: {}", folder.getAbsolutePath());
        if (!folder.exists()) {
            log.debug("[fileCount] File count: 0 (folder does not exist)");
            return 0L;
        }
        String[] list = folder.list();
        if (list == null) {
            log.debug("[fileCount] File count: 0 (folder.list is null)");
            return 0L;
        }
        log.debug("[fileCount] File count: {} (from list.length)", list.length);
        return list.length;
    }

    private File scriptFolder() {
        File currentDir = FileUtils.current();   // get the base folder of the spring/gradle project
        final String scriptsFolder = "application/src/integrationTest/resources/db/scripts";
        return new File(currentDir, scriptsFolder);
    }
}
