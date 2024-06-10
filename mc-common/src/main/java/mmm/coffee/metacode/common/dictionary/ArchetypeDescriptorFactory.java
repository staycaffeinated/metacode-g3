package mmm.coffee.metacode.common.dictionary;

import lombok.Builder;
import lombok.NonNull;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import org.springframework.stereotype.Component;

@Component
public class ArchetypeDescriptorFactory implements IArchetypeDescriptorFactory {

    private ClassNameRuleSet classNameRuleSet;
    private PackageLayoutRuleSet packageLayoutRuleSet;

    /**
     * Constructor
     * @param packageLayoutRuleSet indicates packages and their classes
     * @param classNameRuleSet indicates archetypes and their class names
     */
    public ArchetypeDescriptorFactory(@NonNull PackageLayoutRuleSet packageLayoutRuleSet, @NonNull ClassNameRuleSet classNameRuleSet) {
        this.packageLayoutRuleSet = packageLayoutRuleSet;
        this.classNameRuleSet = classNameRuleSet;
    }

    public JavaArchetypeDescriptor createArchetypeDescriptor(Archetype archetype) {
        String pkgName = packageLayoutRuleSet.resolvePackageName(archetype.toString());
        String klassName = classNameRuleSet.resolveClassName(archetype.toString());
        String fqcn = pkgName + "." + klassName;

        return DefaultJavaArchetypeDescriptor.builder()
                .archetype(archetype)
                .fqcn(fqcn)
                .packageName(pkgName)
                .className(klassName)
                .build();
    }


    public JavaArchetypeDescriptor createArchetypeDescriptor(Archetype archetype, String restObj) {
        String pkgName = packageLayoutRuleSet.resolvePackageName(archetype.toString(), restObj);
        String klassName = classNameRuleSet.resolveClassName(archetype.toString(), restObj);
        String fqcn = pkgName + "." + klassName;

        return DefaultJavaArchetypeDescriptor.builder()
                .archetype(archetype)
                .fqcn(fqcn)
                .packageName(pkgName)
                .className(klassName)
                .build();
    }

    @Builder
    private record DefaultJavaArchetypeDescriptor(Archetype archetype, String fqcn, String packageName,
                                                      String className) implements JavaArchetypeDescriptor {
    }

}


