package mmm.coffee.metacode.common.toml;

import mmm.coffee.metacode.common.model.Archetype;

import java.util.List;
import java.util.Map;

public interface PackageDataDictionary {

    /**
     * Given a classKey, return its namespace
     *
     * @param classKey a well-known class name, such {@code Service} or {@code Controller}
     *                 The classKey must be one of the known constants used by the
     *                 code generator; see @org.example.spi.ClassKeys
     */
    String packageName(String classKey);

    String packageName(Archetype classKey);

    List<Archetype> classKeysOfPackage(String packageKey);

    String basePackage();

    String dictionaryVersion();

    String canonicalClassNameOf(String resourceName, Archetype classKey);

    String canonicalClassNameOf(Archetype classKey);

    void add(Archetype classKey, String packageName);

    void addAll(Map<Archetype, String> klassToPkgMap);

    /**
     * The number of entries in the dictionary
     */
    int size();

}
