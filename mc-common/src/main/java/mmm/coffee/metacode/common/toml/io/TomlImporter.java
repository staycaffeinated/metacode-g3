package mmm.coffee.metacode.common.toml.io;

import lombok.extern.slf4j.Slf4j;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

@Slf4j
public class TomlImporter {

    // @Value("pick some application.yml property we can use")
    // candidates: java-package-schema-file:
    private final String tomlFile = "package.toml";

    public void importToml(String tomlFile) throws IOException {
        TomlParseResult result = Toml.parse(openFile(tomlFile));
        Set<String> dottedKeySet = result.dottedKeySet();

        ArrayList<String> sortedKeys = new ArrayList<>(dottedKeySet);
        sortedKeys.sort(String.CASE_INSENSITIVE_ORDER);
        for (String dottedKey : sortedKeys) {
            log.debug("{} : {}", dottedKey, result.get(dottedKey));
            Object obj = result.get(dottedKey);
            log.debug("The value of {} is a {}", dottedKey, obj.getClass().getSimpleName());
        }

    }

    private InputStream openFile(String fileName) {
        return this.getClass().getClassLoader().getResourceAsStream(fileName);
    }

    private String extractValue(Object value) {
        if (value instanceof TomlArray) {

            TomlArray tomlArray = (TomlArray) value;

            return "[TomlArray: " + asString(tomlArray) + "]";
        } else if (value instanceof Toml) {
            Toml toml = (Toml) value;
            return "[Toml: " + toml + "]";
        } else if (value instanceof TomlParseResult) {
            TomlParseResult parseResult = (TomlParseResult) value;
            return "[TomlParseResult: " + parseResult + "]";
        } else if (value instanceof String) {
            return "[String: " + value + "]";
        }
        return "[Object: " + value + "]";
    }

    private String asString(TomlArray tomlArray) {
        /*
         * tomlArray.size()         : returns the number of items in the array
         * tomlArray.get(N)         : returns the Nth item as an Object
         * tomlArray.toList()       : returns the items as a List
         * tomlArray.getString(N)   : returns the Nth item as a String
         */
        return tomlArray.toList().toString();
    }

}
