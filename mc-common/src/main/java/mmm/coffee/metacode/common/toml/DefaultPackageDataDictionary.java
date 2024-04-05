package mmm.coffee.metacode.common.toml;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import edu.umd.cs.findbugs.annotations.NonNull;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultPackageDataDictionary implements PackageDataDictionary {

    // Key: the proto-class identifier
    // Value: the relative package name (i.e, does not contain basePackage)
    private final Map<ClassKey, String> classToPackageMap = new EnumMap<>(ClassKey.class);

    // key: the package name
    // values: list of proto-classes within the package
    private final ListMultimap<String, ClassKey> packageToClassMap = ArrayListMultimap.create();

    // This value isn't known at boot time, it's provided at runtime as a CLI argument
    private String projectBasePackage;

    /**
     * Constructor
     */
    public DefaultPackageDataDictionary() {

    }

    public String getBasePackage() {
        return projectBasePackage;
    }
    public void setBasePackage(String basePackage) {
        this.projectBasePackage = basePackage;
    }

    public void add(@NonNull ClassKey key, @NonNull String packageName) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(packageName);
        classToPackageMap.put(key, packageName);
        packageToClassMap.put(packageName, key);
    }

    public void addAll(@Nonnull Map<ClassKey, String> items) {
        Objects.requireNonNull(items);
        classToPackageMap.putAll(items);
        ListMultimap<String, ClassKey> reverse = reverseListMultiMapOf(items);
        packageToClassMap.putAll(reverse);
    }

    /**
     * Returns the package name of the given class identifier
     *
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
        String relativePath = classToPackageMap.get(token);
        if (isEmptyOrNull(relativePath)) {
            // If a class doesn't have a package defined, place it in the `misc` package.
            // For any class found in the `misc` package, the user can fix the package model.
            // Maybe log a debug/info/warn statement
            return basePackage() + ".misc";
        }
        return basePackage() + "." + relativePath;
    }

    /**
     * Returns a (possibly empty) list of the proto-classes that belong to {@code relativePackage}
     *
     * @param relativePackage the package name, w/o the basePackage
     * @return a (possibly empty) list of the proto-classes that belong to {@code relativePackage}
     */
    @Override
    public List<ClassKey> classKeysOfPackage(String relativePackage) {
        List<ClassKey> values = packageToClassMap.get(relativePackage);
        if (isEmptyOrNull(values)) {
            return List.of();
        }
        return values;
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
        return classToPackageMap.size();
    }


    protected boolean isEmptyOrNull(@Nullable List<?> items) {
        return items == null || items.isEmpty();
    }

    protected boolean isEmptyOrNull(@Nullable String string) {
        return string == null || string.isEmpty();
    }

    protected ListMultimap<String, ClassKey> reverseListMultiMapOf(Map<ClassKey, String> items) {
        ListMultimap<String, ClassKey> reverse = ArrayListMultimap.create();
        for (Map.Entry<ClassKey, String> entry : items.entrySet()) {
            reverse.put(entry.getValue(), entry.getKey());
        }
        return reverse;
    }
}
