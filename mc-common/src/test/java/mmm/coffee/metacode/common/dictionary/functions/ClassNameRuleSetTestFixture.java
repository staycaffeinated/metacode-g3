package mmm.coffee.metacode.common.dictionary.functions;

import mmm.coffee.metacode.common.dictionary.io.ClassNameRulesReader;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.util.Map;

public class ClassNameRuleSetTestFixture {

    public static ClassNameRuleSet classNameRuleSet() throws IOException {
        ClassNameRulesReader reader = new ClassNameRulesReader(
                new DefaultResourceLoader(),
                "classpath:/test-classname-rules.properties");
        Map<String, String> map = reader.read();
        return new ClassNameRuleSet(map);
    }
}