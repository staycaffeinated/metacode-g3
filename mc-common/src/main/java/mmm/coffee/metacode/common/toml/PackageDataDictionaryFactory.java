package mmm.coffee.metacode.common.toml;

import mmm.coffee.metacode.common.toml.functions.DottedKeyToPackageName;
import mmm.coffee.metacode.common.toml.functions.IsDottedKeyForPackages;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;

public class PackageDataDictionaryFactory {
    private final Predicate<String> isDottedKeyForPackages = new IsDottedKeyForPackages();
    private final DottedKeyToPackageName dottedKeyToPackageName = new DottedKeyToPackageName();

    public PackageDataDictionary createDictionary(String tomlFile) throws IOException {
        TomlParseResult result = Toml.parse(openFile(tomlFile));

        DefaultPackageDataDictionary dictionary = new DefaultPackageDataDictionary();

        Map<ClassKey, String> map = extractClassNames(result);
        dictionary.addAll(map);

        return dictionary;
    }

    private Map<ClassKey, String> extractClassNames(TomlParseResult result) {
        EnumMap<ClassKey, String> miniDictionary = new EnumMap<>(ClassKey.class);

        List<String> dottedKeys = result.dottedKeySet().stream()
                .filter(this::isDottedKey)
                .toList();

        for (String dottedKey : dottedKeys) {
            Object obj = result.get(dottedKey);
            if (obj instanceof String className) {
                ClassKey classKey = getClassKeyOf(className);
                String packageName = convertToPackageName(dottedKey);
                miniDictionary.put(classKey, packageName);
            } else if (obj instanceof TomlArray tomlArray) {
                for (int i = 0; i < tomlArray.size(); i++) {
                    String csvListOfNames = tomlArray.getString(i);
                    List<String> classNames = convertToClassNameList(csvListOfNames);
                    for (String className : classNames) {
                        ClassKey classKey = getClassKeyOf(className);
                        String packageName = convertToPackageName(dottedKey);
                        miniDictionary.put(classKey, packageName);
                    }
                }
            }
        }
        return miniDictionary;
    }

    private ClassKey getClassKeyOf(String className) {
        try {
            return ClassKey.valueOf(className);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: No ClassKey found for %s\n", className);
            return ClassKey.UNDEFINED;
        }
    }

    /**
     * The TomlParseResult represents the right-hand-side of an entry like:
     *      classes = [ "AlphabeticField, ResourceIdField, AlphabeticValidator, ResourceIdValidator" ]
     *  as one String value:
     *      "AlphabeticField, ResourceIdField, AlphabeticValidator, ResourceIdValidator"
     *  which needs to be parsed into a List of tokens:
     *      List.of("AlphabeticField", "ResourceIdField", "AlphabeticValidator", "ResourceIdValidator")
     *
     */
    private List<String> convertToClassNameList(String string) {
        return Collections.list(new StringTokenizer(string, "," )).stream()
                .map(token -> ((String)token).trim())// remove any whitespace
                .toList();
    }

    private boolean isDottedKey(String key) {
        return this.isDottedKeyForPackages.test(key);
    }

    private String convertToPackageName(String dottedKey) {
        return dottedKeyToPackageName.toPackageName(dottedKey);
    }

    private InputStream openFile(String fileName) {
        return this.getClass().getClassLoader().getResourceAsStream(fileName);
    }
}
