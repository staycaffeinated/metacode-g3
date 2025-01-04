package mmm.coffee.metacode.common.model;

import org.apache.commons.text.WordUtils;

public interface JavaArchetypeDescriptor extends ArchetypeDescriptor {

    static final String TEST_SUFFIX = "Test";
    static final String IT_SUFFIX = "IntegrationTest";
    static final String FIXTURE_SUFFIX = ""; // blank because the archetype is already named TestFixtures

    /**
     * The fully-qualified class name, such as `com.acme.petstore.api.PetController`
     * The `fqcn` is the equivalent of `packageName()` + '.' + `shortName()`
     * @return the fully-qualified class name
     */
    String fqcn();

    /**
     * The fully-qualified class name of the unit test class, such as
     * `com.acme.petstore.api.PetControllerTest`
     * @return the class name for the unit test class
     */
    default String fqcnUnitTest() {
        return fqcn() + TEST_SUFFIX;
    }

    /**
     * The fully-qualified class name of the integration test class, such as
     * `com.acme.petstore.api.PetControllerIntegrationTest`
     * @return the class name for the integration test class
     */
    default String fqcnIntegrationTest() {
        return fqcn() + IT_SUFFIX;
    }

    /**
     * The fully-qualified class name of the test-fixture class, such as
     * `com.acme.petstore.api.PetTestFixture`
     * @return the class name for the test-fixture class
     */
    default String fqcnTestFixture() {
        return fqcn() + FIXTURE_SUFFIX;
    }


    /**
     * Returns the package name of the class, such as `com.example.petstore.api`
     * @return the package name part of the fully-qualified class name
     */
    String packageName();

    /**
     * Returns the simple class name, such as `PetController` or `PetService`
     * @return the simple name of the class.
     */
    String className();

    /**
     * Returns the test class name (the name given to the unit test class of this object)
     */
    default  String testClass() {
        return className() + TEST_SUFFIX;
    }

    /**
     * Returns the integration test class name (the name given to the integration test of this object)
     */
    default String integrationTestClass() {
        return className() + IT_SUFFIX;
    }

    default String testFixture() {
        return className() + FIXTURE_SUFFIX;
    }

    /**
     * Returns a variable name, such as `petService` or `petPersistenceAdapter`
     * that templates can use when a variable name needs to be rendered. For example,
     * when, for example, a PetController needs to invoke a PetService method,
     * the template can do something like: `ServiceApi.varName().savePet(...)`.
     * Older template versions did something like:
     *      "this.${endpoint.entityVarName}Service = ${endpoint.entityVarName}Service;"
     * With this ArchetypeDescriptor, templates can do this:
     *      "this.${ServiceApi.varName} = ${ServiceApi.varName};"
     * As another example, this template expression:
     *      "return ${endpoint.entityVarName}Service.findAll${endpoint.entityName}s();"
     * becomes
     *      "return ${ServiceApi.varName()}.findAll${Pojo.className()}s();"
     *
     * Hopefully, this will make templates a little easier follow.
     */
    default String varName() {
        if (className() != null) {
            return WordUtils.uncapitalize(className());
        }
        return "obj";
    }
}
