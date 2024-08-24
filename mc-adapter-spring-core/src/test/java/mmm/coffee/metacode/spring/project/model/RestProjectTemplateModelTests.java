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

import mmm.coffee.metacode.common.dependency.Dependency;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests
 */
class RestProjectTemplateModelTests {

    // Completely hypothetical versions of libraries
    private static final String APACHE_KAFKA_VERSION = "3.0.0";
    private static final String ASSERTJ_VERSION = "1.2.3";
    private static final String SPRINGBOOT_VERSION = "2.6.3";
    private static final String SPRING_CLOUD_VERSION = "2.5.5";
    private static final String SPRING_DM_VERSION = "1.0.1";
    private static final String PROBLEM_VERSION = "2.6";
    private static final String BEN_MANES_VERSION = "1.4.2";
    private static final String JUNIT_RULES_VERSION = "1.0.1b";
    private static final String JUNIT_VERSION = "5.8.1";
    private static final String LIQUIBASE_VERSION = "4.5.6";
    private static final String LOMBOK_VERSION = "3.4.5";
    private static final String LOG4J_VERSION = "2.22.4";
    private static final String TESTCONTAINER_VERSION = "3.33.4";
    RestProjectTemplateModel modelUnderTest;
    DependencyCatalog mockDependencyCatalog;

    @BeforeEach
    public void setUp() {
        modelUnderTest = RestProjectTemplateModel.builder().build();

        // Mock the DependencyCatalog to return sample data
        mockDependencyCatalog = Mockito.mock(DependencyCatalog.class);
        List<Dependency> fakeDependencies = buildFakeDependencies();
        when(mockDependencyCatalog.collect()).thenReturn(fakeDependencies);
    }

    
    @Test
    void whenIsWebMvc_shouldReturnWebMvcFramework() {
        RestProjectTemplateModel model = RestProjectTemplateModel.builder()
                .applicationName("petstore")
                .basePackage("acme.petstore")
                .basePath("/petstore")
                .isWebMvc(true)
                .schema("petstore")
                .build();

        assertThat(model.isWebMvc()).isTrue();
    }

    @Test
    void whenIsWebFlux_shouldReturnWebFluxFramework() {
        RestProjectTemplateModel model = RestProjectTemplateModel.builder()
                .applicationName("petstore")
                .basePackage("acme.petstore")
                .basePath("/petstore")
                .isWebFlux(true)
                .schema("petstore")
                .build();

        assertThat(model.isWebFlux()).isTrue();
    }


    /**
     * The dependencyCatalog.entries() method returns a long list of Dependency entries.
     * Each of those entries causes a field w/in the RestProjectTemplateModel to get updated.
     */
    @Test
    void shouldApplyDependencyData() {
        // when: our sample dependencies are applied to the TemplateModel
        modelUnderTest.configureLibraryVersions(mockDependencyCatalog);

        // expect: a 1:1 map of each Dependency to a setter method,
        // with the getter method returning the expected value
        assertThat(modelUnderTest.getVersions().get("apacheKafka")).isEqualTo(APACHE_KAFKA_VERSION);
        assertThat(modelUnderTest.getVersions().get("assertJ")).isEqualTo(ASSERTJ_VERSION);
    }

    // -------------------------------------------------------------------------------------------
    // Helper Methods
    // -------------------------------------------------------------------------------------------

    List<Dependency> buildFakeDependencies() {
        List<Dependency> resultSet = new ArrayList<>();
        resultSet.add(new Dependency("apacheKafka", APACHE_KAFKA_VERSION));
        resultSet.add(new Dependency("springBoot", SPRINGBOOT_VERSION));
        resultSet.add(new Dependency("springCloud", SPRING_CLOUD_VERSION));
        resultSet.add(new Dependency("springDependencyManagement", SPRING_DM_VERSION));
        resultSet.add(new Dependency("problemSpringWeb", PROBLEM_VERSION));
        resultSet.add(new Dependency("assertJ", ASSERTJ_VERSION));
        resultSet.add(new Dependency("benManesPlugin", BEN_MANES_VERSION));
        resultSet.add(new Dependency("junitSystemRules", JUNIT_RULES_VERSION));
        resultSet.add(new Dependency("junit", JUNIT_VERSION));
        resultSet.add(new Dependency("liquibase", LIQUIBASE_VERSION));
        resultSet.add(new Dependency("lombok", LOMBOK_VERSION));
        resultSet.add(new Dependency("log4j", LOG4J_VERSION));
        resultSet.add(new Dependency("testContainers", TESTCONTAINER_VERSION));
        return resultSet;
    }
}
