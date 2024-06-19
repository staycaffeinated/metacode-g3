package mmm.coffee.metacode.common.dictionary.functions;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.mustache.MustacheExpressionResolver;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PackageLayoutRuleSet {

    // archetypes missing from package-layout will be assigned to this package
    public static final String DEFAULT_BASE_PKG = "org.example.misc";

    private final HashMap<String,String> ruleset = new HashMap<>();

    public PackageLayoutRuleSet(@NonNull Map<String,String> rules) {
        ruleset.putAll(rules);
    }

    public String resolvePackageName(String archetypeName) {
        return ruleset.getOrDefault(archetypeName, DEFAULT_BASE_PKG);
    }

    public String resolvePackageName(Archetype archetype) {
        return ruleset.getOrDefault(archetype.toString(), DEFAULT_BASE_PKG);
    }

    public String resolvePackageName(String archetype, String restObj) {
        log.info("[resolvePackageName] archetype: {} restObj: {}", archetype, restObj);
        String partialPkg = ruleset.getOrDefault(archetype, DEFAULT_BASE_PKG);
        log.info("[resolvePackageName] partialPkg: {}", partialPkg);
        Map<String, String> mustacheModel = new HashMap<>();

        mustacheModel.put("restResource", restObj.toLowerCase());
        mustacheModel.put("basePackage", "{{basePackage}}");
        return MustacheExpressionResolver.resolve(partialPkg, mustacheModel);
    }

    public int size() {
        return ruleset.size();
    }

}
