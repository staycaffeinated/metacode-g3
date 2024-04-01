package mmm.coffee.metacode.common.toml;

import java.util.List;

public interface PackageDataDictionary {

    /**
     * Given a classKey, return its namespace
     * @param classKey a well-known class name, such {@code Service} or {@code Controller}
     *                 The classKey must be one of the known constants used by the
     *                 code generator; see @org.example.spi.ClassKeys
     */
    String packageName(String classKey);
    String packageName(ClassKey classKey);
    List<ClassKey> classKeysOfPackage(String packageKey);
    String basePackage();
    String dictionaryVersion();
    String canonicalClassNameOf(String classKey);

    /**
     * The number of entries in the dictionary
     */
    int size();

}
