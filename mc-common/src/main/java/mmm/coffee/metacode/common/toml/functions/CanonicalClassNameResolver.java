package mmm.coffee.metacode.common.toml.functions;

import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Computes the `canonical` classname, such as "org.example.petstore.domain.Pet"
 */
public class CanonicalClassNameResolver implements BiFunction<String, String, String> {

    private static final CanonicalClassNameResolver INSTANCE = new CanonicalClassNameResolver();

    /**
     * A static version of the `apply` method, for convenience
     */
    public static String resolve(String resourceName, String packageTemplate) {
        return INSTANCE.apply(resourceName, packageTemplate);
    }

    @Override
    public String apply(@Nonnull String resourceName, @Nonnull String packageTemplate) {
        Objects.requireNonNull(resourceName);
        Objects.requireNonNull(packageTemplate);

        // TODO: Will we support naming patterns? Can the user define a regex-style template
        // that we'll use to conjure the classname?
        // If the resource name is part of the package namespace, resolve that part
        String canonicalName = PackageNameResolver.resolve(resourceName, packageTemplate);

        // Append the class name to the package name
        return canonicalName + "." + toCamelCase(resourceName);
    }

    private String toCamelCase(String string) {
        if (string.isEmpty()) {
            return string;
        }
        char ch = string.charAt(0);
        char upperChar = Character.toUpperCase(ch);
        return upperChar + string.substring(1, string.length());
    }
}
