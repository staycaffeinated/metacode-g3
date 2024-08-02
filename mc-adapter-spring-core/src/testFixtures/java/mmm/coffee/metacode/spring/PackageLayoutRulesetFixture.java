package mmm.coffee.metacode.spring;

import mmm.coffee.metacode.common.dictionary.PackageLayout;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutToHashMapMapper;
import mmm.coffee.metacode.common.dictionary.io.PackageLayoutReader;

import java.io.IOException;
import java.util.Map;

public class PackageLayoutRulesetFixture {
    public static PackageLayoutRuleSet buildPackageLayoutRuleSet() throws IOException {
        PackageLayoutReader reader = new PackageLayoutReader();
        PackageLayout layout = reader.read("classpath:/test-package-layout.json");
        Map<String, String> rules = PackageLayoutToHashMapMapper.convertToHashMap(layout);
        return new PackageLayoutRuleSet(rules);
    }
}
