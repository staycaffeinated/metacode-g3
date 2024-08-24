/*
 * Copyright 2022 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.metacode.spring.project.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import mmm.coffee.metacode.annotations.jacoco.ExcludeFromJacocoGeneratedReport;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;

/**
 * This is the 'Model' object used by Freemarker to resolve template variables.
 * Any variable that might be used in a project-related template must have a
 * corresponding field within this POJO.
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ExcludeFromJacocoGeneratedReport // Ignore code coverage for this class
@SuppressWarnings({"java:S125", "java:S116"})
// S125: don't warn about comments that happen to look like code
// S116: need to relax this naming convention rule for the R2dbc_h2Version instance variable
public class RestProjectTemplateModel extends SpringTemplateModel {
    // When this object is passed into Freemarker,
    // it's assigned a name referred to in Freemarker lingo
    // as the "top level variable".
    private final String topLevelVariable = Key.PROJECT.value();

    // Basic properties
    private String applicationName;
    private String basePath;
    private String basePackage;
    private String javaVersion;
    private String basePackagePath;
    private String groupId;
    private String schema;

    private JavaArchetypeDescriptor archetypeDescriptor;

    // Booleans to indicate the integrations to accommodate
    // The Setter annotation instructs Lombok to include a 'setX'
    // for these. Otherwise, they're only visible within the Builder
    @Setter
    private boolean withPostgres;
    @Setter
    private boolean withTestContainers;
    @Setter
    private boolean withLiquibase;
    @Setter
    private boolean withMongoDb;
    @Setter
    private boolean withOpenApi;

    /*
     * Key: the library name, such as 'assertJ'
     * Value: the library version, such as '3.3.0'
     * The values are accessible in the FTL templates with the syntax
     * `${project.versions['key']}`, such as `${project.versions['assertJ']}`
     */
    @Builder.Default
    private HashMap<String,String> versions = new HashMap<>();


    /**
     * Apply the entries from the {@code dependencyCatalog} to the
     * state of this template model object. There's information in
     * the DependencyCatalog that needs to be available to the Template
     * engine to enable resolving some template variables.
     * <p>
     * The dependency catalog lists the 3rd-party libraries in use and their version.
     * This enables the code generator to render the `libs.versions.toml` file and
     * the `build.gradle` files.
     *
     * @param dependencyCatalog the dependency data to add to the template model
     */
    public void configureLibraryVersions(@NonNull DependencyCatalog dependencyCatalog) {
        // Update one of the above xxxVersion instance variables with the version string
        // found in the dependencyCatalog, using reflection. Without reflection, the
        // code would look something like:
        //      this.setH2Version(...)
        //      this.setPostgresqlVersion(...)
        //      this.setReactorTestVersion(...)
        // through all the dependency's found in the DependencyCatalog. 
        // dependencyCatalog.collect().forEach(it -> setField(it.getName(), it.getVersion()));
        dependencyCatalog.collect().forEach(it -> versions.put(it.getName(), it.getVersion()));
    }
}
