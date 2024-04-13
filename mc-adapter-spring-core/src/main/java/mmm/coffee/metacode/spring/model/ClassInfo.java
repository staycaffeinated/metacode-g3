package mmm.coffee.metacode.spring.model;

/**
 * @param className   The simple class name, such as `Pet`
 * @param packageName The package name, such as `org.example.petstore.domain`
 * @param fqName      The fully-qualified class name, such as `org.example.petstore.domain.Pet` The FQ className must match packageName + className.
 * @param varName     A variable name, such as `pet`. This has to be suitable for use as a variable name in the generated code. The goal is, instead of generating code like `Pet x = ... ` generate something better, like `Pet pet = ...`.
 */
public record ClassInfo(String className, String packageName, String fqName, String varName) {
}
