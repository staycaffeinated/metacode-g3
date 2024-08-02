package mmm.coffee.metacode.spring;

import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.io.ClassNameRulesReader;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.util.Map;

public class ClassNameRuleSetFixture {
    public static ClassNameRuleSet buildClassNameRuleSet() throws IOException {
        ClassNameRulesReader reader = new ClassNameRulesReader(new DefaultResourceLoader(), "classpath:/test-classname-rules.properties");
        Map<String, String> map = reader.read();
        return new ClassNameRuleSet(map);
    }

}
