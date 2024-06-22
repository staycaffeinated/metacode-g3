package mmm.coffee.metacode.common.dictionary.functions;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.mustache.MustacheExpressionResolver;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ClassNameRuleSet {

    public static final String UNDEFINED_SUFFIX = "Undefined";

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
        model.put("restResource", StringUtils.capitalize(archetype)); // to handle RootController/Service
        return MustacheExpressionResolver.resolve(pkgExpression, model);
    }

    public String resolveClassName(String archetype, String resource) {
        // If an archetype isn't found in this ruleset, add a suffix to make it easy to identify that archetype
        String pkgExpression = ruleset.getOrDefault(archetype, archetype.toString() + UNDEFINED_SUFFIX);
        if (pkgExpression.endsWith("Undefined")) {
            log.warn("No classname rule was found for archetype '{}', so the suffix 'Undefined' was used." , archetype);
        }
        Map<String,String> model = new HashMap<>();
        model.put("restResource", StringUtils.capitalize(resource));
        model.put("archetype", StringUtils.capitalize(archetype));
        return MustacheExpressionResolver.resolve(pkgExpression, model);
    }

    public int size() {
        return ruleset.size();
    }

}
