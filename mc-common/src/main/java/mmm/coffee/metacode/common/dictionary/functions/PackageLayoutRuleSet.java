package mmm.coffee.metacode.common.dictionary.functions;

import lombok.NonNull;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.mustache.MustacheExpressionResolver;

import java.util.HashMap;
import java.util.Map;

public class PackageLayoutRuleSet {

    private final HashMap<String,String> ruleset = new HashMap<>();

    public PackageLayoutRuleSet(@NonNull Map<String,String> rules) {
        ruleset.putAll(rules);
    }

    public String resolvePackageName(String archetype) {
        return ruleset.getOrDefault(archetype, "org.misc");
    }

    public String resolvePackageName(Archetype archetype) {
        return ruleset.getOrDefault(archetype.toString(), "org.misc");
    }

    public String resolvePackageName(String archetype, String restObj) {
        String partialPkg = ruleset.getOrDefault(archetype, "org.misc");
        Map<String, String> mustacheModel = new HashMap<>();
        mustacheModel.put("restResource", restObj.toLowerCase());
        return MustacheExpressionResolver.resolve(partialPkg, mustacheModel);
    }

    public int size() {
        return ruleset.size();
    }

}
