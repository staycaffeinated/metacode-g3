package mmm.coffee.metacode.common.toml;

import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultPackageDataDictionary implements PackageDataDictionary {

    // Key: the class identifier
    // Value: the relative package name (i.e, does not contain basePackage)
    // Todo: Maybe use bi-directional map so we can find all classes that go to a package,
    // or the package of a given class.
    private Map<ClassKey,String> dictionary = new HashMap<>();
    private String projectBasePackage;

    /**
     * Constructor
     * @param projectBasePath the base path of the project, such as 'org.acme.petstore'
     */
    public DefaultPackageDataDictionary(String projectBasePath) {
        this.projectBasePackage = projectBasePath;
    }

    public void addAll(@Nonnull Map<ClassKey,String> items) {
        Objects.requireNonNull(items);
        dictionary.putAll(items);
    }

    /**
     * Returns the package name of the given class identifier
     * @param classKey a well-known class name, such {@code Service} or {@code Controller}
     *                 The classKey must be one of the known constants used by the
     *                 code generator; see @org.example.spi.ClassKeys
     * @return the package name, such as "tech.acme.petstore.api.rest"
     */
    @Override
    public String packageName(String classKey) {
        return packageName(ClassKey.valueOf(classKey));
    }

    @Override
    public String packageName(ClassKey token) {
        String relativePath = dictionary.get(token);
        if (relativePath != null) {
            return basePackage() + "." + relativePath;
        }
        // If a class doesn't have a package defined, place it in the `misc` package.
        // For any class found in the `misc` package, the user can fix the package model.
        // Maybe log a debug/info/warn statement
        return basePackage() + ".misc";
    }

    @Override
    public List<ClassKey> classKeysOfPackage(String packageKey) {
        return List.of();
    }

    @Override
    public String basePackage() {
        return projectBasePackage;
    }

    @Override
    public String dictionaryVersion() {
        return "1.0";
    }

    @Override
    public String canonicalClassNameOf(String classKey) {
        // I think we also need the resource name and the naming strategy.
        // eg. is this sending back: org.acme.petstore.pet.api.PetController?
        return "";
    }

    public int size() {
        return dictionary.size();
    }

}
