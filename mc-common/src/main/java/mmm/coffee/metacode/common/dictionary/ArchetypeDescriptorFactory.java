package mmm.coffee.metacode.common.dictionary;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArchetypeDescriptorFactory implements IArchetypeDescriptorFactory {

    private final ClassNameRuleSet classNameRuleSet;
    private final PackageLayoutRuleSet packageLayoutRuleSet;

    /**
     * Constructor
     *
     * @param packageLayoutRuleSet indicates packages and their classes
     * @param classNameRuleSet     indicates archetypes and their class names
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
        String pkgName = packageLayoutRuleSet.resolvePackageName(archetype.toString());
        log.info("[createArchetypeDescriptor] archetype: {}, pkgName={}", archetype.toString(), pkgName);
        String klassName = classNameRuleSet.resolveClassName(archetype.toString(), restObj);
        String fqcn = pkgName + "." + klassName;

        return DefaultJavaArchetypeDescriptor.builder()
                .archetype(archetype)
                .fqcn(fqcn)
                .packageName(pkgName)
                .className(klassName)
                .build();
    }

    public String toString() {
        return "ArchetypeDescriptor [" +
                "packageLayoutRules.size: " + packageLayoutRuleSet.size() + ", " +
                "classNameRules.size: " + classNameRuleSet.size() + "]";
    }


    @Builder
    private record DefaultJavaArchetypeDescriptor(Archetype archetype, String fqcn, String packageName,
                                                  String className) implements JavaArchetypeDescriptor {
        public String toString() {
            return "DefaultJavaArchetype[className: " + className() + ", " +
                    "fqcn: " + fqcn() + ", " +
                    "unitTestClass: " + fqcnUnitTest() + ", " +
                    "integrationTestClass: " + fqcnIntegrationTest() + ", " +
                    "packageName: " + packageName() + "]";
        }
    }
}


