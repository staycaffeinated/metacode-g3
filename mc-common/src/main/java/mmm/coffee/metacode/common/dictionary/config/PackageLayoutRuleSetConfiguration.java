package mmm.coffee.metacode.common.dictionary.config;

import mmm.coffee.metacode.common.dictionary.PackageLayout;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutToHashMapMapper;
import mmm.coffee.metacode.common.dictionary.io.PackageLayoutReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

@Configuration
public class PackageLayoutRuleSetConfiguration {

    @Value("classpath:package-layout.json")
    private String packageLayoutConfiguration;

    @Bean
    PackageLayoutRuleSet packageLayoutRuleSet(PackageLayoutReader reader) throws IOException {
        PackageLayout layout = reader.read(packageLayoutConfiguration);
        Map<String, String> rules = PackageLayoutToHashMapMapper.convertToHashMap(layout);
        return new PackageLayoutRuleSet(rules);
    }

    @Bean
    PackageLayoutReader packageLayoutReader() {
        return new PackageLayoutReader();
    }
}
