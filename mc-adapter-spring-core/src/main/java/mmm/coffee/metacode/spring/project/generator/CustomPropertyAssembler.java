package mmm.coffee.metacode.spring.project.generator;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.dictionary.ArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.ProjectArchetypeToMap;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import mmm.coffee.metacode.common.mustache.MustacheExpressionResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class CustomPropertyAssembler {


    public static Map<String, Object> assembleCustomProperties(IArchetypeDescriptorFactory archetypeDescriptorFactory,
                                                               String basePackage) {
        Map<String, ArchetypeDescriptor> customProperties = ProjectArchetypeToMap.map(archetypeDescriptorFactory);
        Map<String, Object> props = new TreeMap<>();
        customProperties.forEach((key, value) -> {
            ArchetypeDescriptor descriptor1 = resolveBasePackageOf(value, basePackage);
            props.put(key, descriptor1);
        });
        return props;
    }

    /*
     *
     */
    private static ArchetypeDescriptor resolveBasePackageOf(ArchetypeDescriptor descriptor, String basePackage) {
        if (descriptor instanceof JavaArchetypeDescriptor) {
            JavaArchetypeDescriptor that = (JavaArchetypeDescriptor) descriptor;
            Map<String, String> map = new HashMap<>();   // the map for the mustache resolver
            map.put("basePackage", basePackage);
            String resolvedClassName = MustacheExpressionResolver.resolve(that.className(), map);
            String resolvedFQCN = MustacheExpressionResolver.resolve(that.fqcn(), map);
            String resolvedPkgName = MustacheExpressionResolver.resolve(that.packageName(), map);

            var foo = ResolvedJavaArchetypeDescriptor.builder()
                    .archetype(descriptor.archetype())
                    .fqcn(resolvedFQCN)
                    .className(resolvedClassName)
                    .packageName(resolvedPkgName)
                    .build();
            log.debug("Returning resolved descriptor: {}", foo);
            return foo;
        } else {
            return descriptor;
        }
    }


    @Builder
    private record ResolvedJavaArchetypeDescriptor(Archetype archetype, String fqcn, String packageName,
                                                   String className) implements JavaArchetypeDescriptor {

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ResolvedJavaArchetypeDescriptor[className: ").append(className()).append(", ");
            sb.append("fqcn: ").append(fqcn()).append(", ");
            sb.append("unitTest: ").append(fqcnUnitTest()).append(", ");
            sb.append("integrationTest: ").append(fqcnIntegrationTest()).append(", ");
            sb.append("packageName: ").append(packageName()).append("]");
            return sb.toString();
        }
    }
}