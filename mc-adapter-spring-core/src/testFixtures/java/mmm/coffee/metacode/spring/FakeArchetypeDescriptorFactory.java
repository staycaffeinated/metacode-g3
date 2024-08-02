package mmm.coffee.metacode.spring;

import lombok.Builder;
import mmm.coffee.metacode.common.dictionary.ArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.PackageLayout;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutToHashMapMapper;
import mmm.coffee.metacode.common.dictionary.io.ClassNameRulesReader;
import mmm.coffee.metacode.common.dictionary.io.PackageLayoutReader;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.util.Map;

public class FakeArchetypeDescriptorFactory implements IArchetypeDescriptorFactory {

    private final PackageLayoutRuleSet packageLayoutRuleSet;
    private final ClassNameRuleSet classNameRuleSet;

    public static IArchetypeDescriptorFactory archetypeDescriptorFactory() throws IOException {
        return new FakeArchetypeDescriptorFactory();
    }


    public FakeArchetypeDescriptorFactory() throws IOException {
        packageLayoutRuleSet = buildPackageLayoutRuleSet();
        classNameRuleSet = buildClassNameRuleSet();
    }

    public JavaArchetypeDescriptor createArchetypeDescriptor(Archetype archetype) {
        String pkgName = packageLayoutRuleSet.resolvePackageName(archetype.toString());
        String klassName = classNameRuleSet.resolveClassName(archetype.toString());
        String fullName = pkgName + "." + klassName;

        return FakeJavaArchetypeDescriptor.builder()
                .archetype(archetype)
                .fqcn(fullName)
                .packageName(pkgName)
                .className(klassName)
                .build();
    }

    public JavaArchetypeDescriptor createArchetypeDescriptor(Archetype archetype, String restObj) {
        String pkgName = packageLayoutRuleSet.resolvePackageName(archetype.toString(), restObj);
        String klassName = classNameRuleSet.resolveClassName(archetype.toString(), restObj);
        String fqcn = pkgName + "." + klassName;

        return FakeJavaArchetypeDescriptor.builder()
                .archetype(archetype)
                .fqcn(fqcn)
                .packageName(pkgName)
                .className(klassName)
                .build();
    }

    @Builder
    private record FakeJavaArchetypeDescriptor(Archetype archetype, String fqcn, String packageName,
                                                  String className) implements JavaArchetypeDescriptor {
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("FakeJavaArchetypeDescriptor[className: ").append(className()).append(", ");
            sb.append("fqcn: {}").append(fqcn()).append(", ");
            sb.append("unitTest: {}").append(fqcnUnitTest()).append(", ");
            sb.append("integrationTest: {}").append(fqcnIntegrationTest()).append(", ");
            sb.append("packageName: {}").append(packageName()).append("]");
            return sb.toString();
        }
    }

    /* -----------------------------------------------------------------------------------------------------------
     * Helper methods
     * ----------------------------------------------------------------------------------------------------------- */

    ClassNameRuleSet buildClassNameRuleSet() throws IOException {
        ClassNameRulesReader reader = new ClassNameRulesReader(new DefaultResourceLoader(), "classpath:/classname-rules.properties");
        Map<String, String> map = reader.read();
        return new ClassNameRuleSet(map);
    }

    PackageLayoutRuleSet buildPackageLayoutRuleSet() throws IOException {
        PackageLayoutReader reader = new PackageLayoutReader();
        PackageLayout layout = reader.read("classpath:/package-layout.json");
        Map<String, String> rules = PackageLayoutToHashMapMapper.convertToHashMap(layout);
        return new PackageLayoutRuleSet(rules);
    }
}
