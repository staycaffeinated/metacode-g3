package mmm.coffee.metacode.common.toml;

import lombok.NonNull;
import mmm.coffee.metacode.common.model.Archetype;
import org.apache.commons.lang3.arch.Processor;

import java.util.List;

public class PackageLayoutToPackageDataDictionaryMapper {

    PackageDataDictionary map(@NonNull PackageLayout packageLayout) {
        PackageDataDictionary dictionary = new DefaultPackageDataDictionary();

        packageLayout.getEntries().forEach(entry -> {
            List<String> archtypeNames = entry.getArchetypes();
            archtypeNames.forEach( name -> {
                Archetype archetype = Archetype.fromString(name);
                dictionary.add(archetype, entry.getPackageName());
            });
        });

        return dictionary;
    }
}
