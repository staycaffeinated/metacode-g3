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

    private ClassNameRuleSet classNameRuleSet;
    private PackageLayoutRuleSet packageLayoutRuleSet;

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

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ArchetypeDescriptor [");
        builder.append("packageLayoutRules.size: ").append(packageLayoutRuleSet.size()).append(", ");
        builder.append("classNameRules.size: ").append(classNameRuleSet.size()).append("]");
        return builder.toString();
    }


    @Builder
    private record DefaultJavaArchetypeDescriptor(Archetype archetype, String fqcn, String packageName,
                                                  String className) implements JavaArchetypeDescriptor {
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("DefaultJavaArchetype[className: ").append(className()).append(", ");
            sb.append("fqcn: ").append(fqcn()).append(", ");
            sb.append("unitTestClass: ").append(fqcnUnitTest()).append(", ");
            sb.append("integrationTestClass: ").append(fqcnIntegrationTest()).append(", ");
            sb.append("packageName: ").append(packageName()).append("]");
            return sb.toString();
        }
    }

    @Builder
    private record RegisterDatabasePropertiesDescriptor(Archetype archetype, String fqcn, String packageName,
                                                        String className) implements JavaArchetypeDescriptor {
        public String fqcnIntegrationTest() {
            return className();
        }

        public String fqcnUnitTest() {
            return className();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("RegisterDatabasePropertiesDescriptor[className: ").append(className()).append(", ");
            sb.append("fqcn: ").append(fqcn()).append(", ");
            sb.append("unitTestClass: ").append(fqcnUnitTest()).append(", ");
            sb.append("integrationTestClass: ").append(fqcnIntegrationTest()).append(", ");
            sb.append("packageName: ").append(packageName()).append("]");
            return sb.toString();
        }
    }

    @Builder
    private record ContainerConfigurationDescriptor(Archetype archetype, String fqcn, String packageName,
                                                    String className) implements JavaArchetypeDescriptor {
        public String fqcnIntegrationTest() {
            return className();
        }

        public String fqcnUnitTest() {
            return className();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ContainerConfigurationDescriptor[className: ").append(className()).append(", ");
            sb.append("fqcn: ").append(fqcn()).append(", ");
            sb.append("unitTestClass: ").append(fqcnUnitTest()).append(", ");
            sb.append("integrationTestClass: ").append(fqcnIntegrationTest()).append(", ");
            sb.append("packageName: ").append(packageName()).append("]");
            return sb.toString();
        }
    }

}


