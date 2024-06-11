package mmm.coffee.metacode.common.model;

public interface JavaArchetypeDescriptor extends ArchetypeDescriptor {
    /**
     * The fully-qualified class name, such as `com.acme.petstore.api.PetController`
     * The `fqcn` is the equivalent of `packageName()` + '.' + `shortName()`
     * @return the fully-qualified class name
     */
    String fqcn();

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

}
