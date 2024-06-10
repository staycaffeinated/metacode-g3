package mmm.coffee.metacode.common.dictionary.functions;

import mmm.coffee.metacode.common.model.Archetype;

import java.util.HashMap;

public class PackageLayoutRuleSetTestFixture {

    public static PackageLayoutRuleSet basicRuleSet() {
        HashMap<String, String> rules = new HashMap<>();
        Archetype[] values = Archetype.values();
        for (Archetype e : values) {
            rules.put(e.toString(), "com.acme.petstore");
        }
        rules.put("Controller", "com.acme.petstore.{{restResource}}.api");
        rules.put("ServiceApi", "com.acme.petstore.{{restResource}}.api");
        rules.put("ServiceImpl", "com.acme.petstore.{{restResource}}.api");
        rules.put("Routes", "com.acme.petstore.{{restResource}}.api");
        rules.put("Application", "com.acme.petstore");
        rules.put(Archetype.SecureRandomSeries.toString(), "com.acme.petstore.utils");

        return new PackageLayoutRuleSet(rules);
    }


}
