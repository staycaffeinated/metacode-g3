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
        String pkgExpression = ruleset.getOrDefault(archetype, "{{restResource}}Thing");
        if (pkgExpression.endsWith("Thing")) {
            log.info("No classname rule was found for archetype '{}', so the suffix 'Thing' was used." , archetype);
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
