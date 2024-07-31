package mmm.coffee.metacode.common.dictionary.io;

import lombok.NonNull;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class ClassNameRulesReader {
    private final ResourceLoader resourceLoader;
    private final String fileName;

    public ClassNameRulesReader(@NonNull ResourceLoader resourceLoader, @NonNull String fileName) {
        this.resourceLoader = resourceLoader;
        this.fileName = fileName;
    }

    public Map<String, String> read() throws IOException {
        InputStream is = resourceLoader.getResource(fileName).getInputStream();
        Properties properties = new Properties();
        properties.load(is);

        // Convert property entries into a Map that will be consumed by ClassNameRuleSet
        HashMap<String, String> map = new HashMap<>();
        properties.forEach((key, value) -> map.put(key.toString(), value.toString()));
        return map;
    }

}
