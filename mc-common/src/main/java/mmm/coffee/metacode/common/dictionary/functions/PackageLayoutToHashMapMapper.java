package mmm.coffee.metacode.common.dictionary.functions;

import mmm.coffee.metacode.common.dictionary.PackageLayout;
import mmm.coffee.metacode.common.dictionary.PackageLayoutEntry;

import java.util.HashMap;
import java.util.Map;

public class PackageLayoutToHashMapMapper {

    public static Map<String,String> convertToHashMap(PackageLayout layout) {
        HashMap<String,String> ruleSet = new HashMap<>();

        for (PackageLayoutEntry entry : layout.getEntries()) {
            entry.getArchetypes().forEach(archetype -> {
                ruleSet.put(archetype, entry.getPackageName());
            });
        }
        return ruleSet;
    }
}
