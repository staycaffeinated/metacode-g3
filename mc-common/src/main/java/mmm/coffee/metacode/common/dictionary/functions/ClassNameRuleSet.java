package mmm.coffee.metacode.common.dictionary.functions;

import lombok.NonNull;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.mustache.MustacheExpressionResolver;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ClassNameRuleSet {

    private final HashMap<String,String> ruleset = new HashMap<>();

    /**
     * Constructor
     * @param initialRules rules
     */
    public ClassNameRuleSet(@NonNull Map<String,String> initialRules) {
        ruleset.putAll(initialRules);
    }

    public String resolveClassName(String archetype) {
        String pkgExpression = ruleset.getOrDefault(archetype, "{{archetype}}");
        Map<String,String> model = new HashMap<>();
        model.put("archetype", StringUtils.capitalize(archetype));
        return MustacheExpressionResolver.resolve(pkgExpression, model);
    }

    public String resolveClassName(Archetype archetype) {
        return resolveClassName(archetype.toString());
    }

    public String resolveClassName(String archetype, String resource) {
        String pkgExpression = ruleset.getOrDefault(archetype, "{{restResource}}Thing");
        Map<String,String> model = new HashMap<>();
        model.put("restResource", StringUtils.capitalize(resource));
        model.put("archetype", StringUtils.capitalize(archetype));
        return MustacheExpressionResolver.resolve(pkgExpression, model);
    }

    public String resolveClassName(Archetype archetype, String resource) {
        return resolveClassName(archetype.toString(), resource);
    }

}
