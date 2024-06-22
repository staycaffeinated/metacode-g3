package mmm.coffee.metacode.common.dictionary.functions;

import mmm.coffee.metacode.common.dictionary.PackageLayout;
import mmm.coffee.metacode.common.dictionary.io.PackageLayoutReader;

import java.io.IOException;
import java.util.Map;

public class PackageLayoutRuleSetTestFixture {

    public static PackageLayoutRuleSet packageLayoutRuleSet() throws IOException {
        PackageLayoutReader reader = new PackageLayoutReader();
        PackageLayout layout = reader.read("classpath:/test-package-layout.json");
        Map<String, String> rules = PackageLayoutToHashMapMapper.convertToHashMap(layout);
        return new PackageLayoutRuleSet(rules);
    }


}
