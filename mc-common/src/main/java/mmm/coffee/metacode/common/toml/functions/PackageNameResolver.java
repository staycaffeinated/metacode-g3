package mmm.coffee.metacode.common.toml.functions;

import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.function.BiFunction;

public class PackageNameResolver implements BiFunction<String, String, String> {

    private static final PackageNameResolver INSTANCE = new PackageNameResolver();

    @Override
    public String apply(@Nonnull String resourceName, @Nonnull String packageTemplate) {
        Objects.requireNonNull(resourceName);
        Objects.requireNonNull(packageTemplate);
        return packageTemplate.replace("_RESOURCE_", resourceName.toLowerCase());
    }

    public static String resolve(String resourceName, String packageTemplate) {
        return INSTANCE.apply(resourceName, packageTemplate);
    }
}
