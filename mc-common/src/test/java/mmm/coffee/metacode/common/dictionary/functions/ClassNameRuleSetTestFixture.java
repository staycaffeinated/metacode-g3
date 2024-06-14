package mmm.coffee.metacode.common.dictionary.functions;

import mmm.coffee.metacode.common.model.Archetype;

import java.util.HashMap;
import java.util.Map;

public class ClassNameRuleSetTestFixture {

    public static ClassNameRuleSet basicRuleSet() {
        Map<String, String> rules = new HashMap<>();
        rules.put(Archetype.Application.toString(), "Application");
        rules.put(Archetype.Controller.toString(), "{{restResource}}Controller");
        rules.put(Archetype.GlobalExceptionHandler.toString(), "GlobalExceptionHandler");
        rules.put(Archetype.SecureRandomSeries.toString(), "ResourceIdGenerator");  // an example of the classname not matching archetype name
        rules.put(Archetype.ServiceApi.toString(), "{{restResource}}Service");
        rules.put(Archetype.ServiceImpl.toString(), "{{restResource}}ServiceImpl");

        return new ClassNameRuleSet(rules);
    }
}