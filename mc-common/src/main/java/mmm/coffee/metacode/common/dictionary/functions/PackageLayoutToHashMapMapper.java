package mmm.coffee.metacode.common.dictionary.functions;

import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.dictionary.PackageLayout;
import mmm.coffee.metacode.common.dictionary.PackageLayoutEntry;
import mmm.coffee.metacode.common.model.Archetype;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PackageLayoutToHashMapMapper {

    /**
     * Returns a Map where the key is an archtype name and the value is the package name.
     * The package name may contain mustache expressions, such as "{{basePackage}}.infrastructure.config"
     * or "{{basePackage}}.api.{{endpoint}}".
     * Essentially, the mapping provides a reverse-lookup of the PackageLayout. The PackageLayout
     * uses the packageName for keys and a list of the archetypes that belong to that packageName in the value slot.
     * This method reverses that: the key is the archetype and the value is the packageName.
     *
     * @param layout the content of the "package-layout.json" file, in object form
     * @return the mapping of archetypes to packages.
     */

    public static Map<String, String> convertToHashMap(PackageLayout layout) {
        HashMap<String, String> ruleSet = new HashMap<>();

        for (PackageLayoutEntry entry : layout.getEntries()) {
            entry.getArchetypes().forEach(archetypeName -> {
                log.info("[convertToHashMap] archetype: {}", archetypeName);
                Archetype at = Archetype.fromString(archetypeName);
                if (at == Archetype.Undefined && !archetypeName.equalsIgnoreCase("undefined")) {
                    log.info("An undefined archetype '{}' was encountered in the package layout", archetypeName);
                }
                ruleSet.put(archetypeName, entry.getPackageName());
            });
        }
        return ruleSet;
    }
}
