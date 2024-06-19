package mmm.coffee.metacode.common.dictionary.config;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PackageLayoutRuleSetConfiguration {

    @Value("classpath:package-layout.json")
    private String packageLayoutConfiguration;

    @Bean
    PackageLayoutRuleSet packageLayoutRuleSet(PackageLayoutReader reader) throws IOException {
        log.debug("[packageLayoutRuleSet] Building the PackageLayoutRuleSet using the layout from {}", packageLayoutConfiguration);
        PackageLayout layout = reader.read(packageLayoutConfiguration);
        Map<String, String> rules = PackageLayoutToHashMapMapper.convertToHashMap(layout);
        log.debug("[packageLayoutRuleSet] Rules are {}", rules);
        return new PackageLayoutRuleSet(rules);
    }

    @Bean
    PackageLayoutReader packageLayoutReader() {
        return new PackageLayoutReader();
    }
}
