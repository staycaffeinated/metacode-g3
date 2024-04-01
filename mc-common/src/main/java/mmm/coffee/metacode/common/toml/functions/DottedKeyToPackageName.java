package mmm.coffee.metacode.common.toml.functions;

/**
 * Converts a TOML dottedKey value to a Java package name
 */
public class DottedKeyToPackageName {

    private IsDottedKeyForPackages isDottedKeyForPackage = new IsDottedKeyForPackages();

    public String toPackageName(String dottedKey) {
        if (isDottedKeyForPackage.test(dottedKey)) {
            int endOfKey = dottedKey.indexOf(TomlKeywords.CLASSES);
            return dottedKey.substring(0, endOfKey);
        }
        return null;
    }
}
