package mmm.coffee.metacode.common.dictionary.config;

import mmm.coffee.metacode.annotations.jacoco.ExcludeFromJacocoGeneratedReport;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.io.ClassNameRulesReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.util.Map;

@Configuration
@ExcludeFromJacocoGeneratedReport
public class ClassNameRuleSetConfiguration {

    @Value("classpath:classname-rules.properties")
    private String classNameRulesProperties;

    @Bean
    public ClassNameRulesReader classNameRulesReaderFactory() {
        return new ClassNameRulesReader(new DefaultResourceLoader(), classNameRulesProperties);
    }


    @Bean
    public ClassNameRuleSet classNameRuleSet(ClassNameRulesReader rulesReader) throws IOException {
        Map<String,String> map = rulesReader.read();
        return new ClassNameRuleSet(map);
    }
}
