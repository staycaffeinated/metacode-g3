package mmm.coffee.metacode.spring.project.generator;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.dictionary.EndpointArchetypeToMap;
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
    public static Map<String, Object> assembleCustomProperties(IArchetypeDescriptorFactory archetypeDescriptorFactory,
                                                               String basePackage,
                                                               String restResource) {
        Map<String,Object> projectScopeProperties = assembleCustomProperties(archetypeDescriptorFactory, basePackage);

        Map<String, ArchetypeDescriptor> customProperties = EndpointArchetypeToMap.map(archetypeDescriptorFactory, restResource);
        Map<String, Object> endpointScopeProperties = new TreeMap<>();
        // The templates of endpoint-scope classes (such as Controller) will need the coordinates
        // of project-scope classes (such as Exceptions and ResourceIdSuppliers). 
        endpointScopeProperties.putAll(projectScopeProperties);
        customProperties.forEach((key, value) -> {
            ArchetypeDescriptor descriptor1 = resolveBasePackageOf(value, basePackage, restResource);
            endpointScopeProperties.put(key, descriptor1);
        });
        return endpointScopeProperties;
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

            switch (descriptor.archetype()) {

                case AbstractIntegrationTest, ContainerConfiguration, RegisterDatabaseProperties: {
                    log.info("[resolveBasePackageOf: archetype: {}", descriptor.archetypeName());
                    return EdgeCaseResolvedArchetypeDescriptor.builder()
                            .archetype(descriptor.archetype())
                            .fqcn(resolvedFQCN)
                            .className(resolvedClassName)
                            .packageName(resolvedPkgName)
                            .build();
                }
                default:
                    return ResolvedJavaArchetypeDescriptor.builder()
                            .archetype(descriptor.archetype())
                            .fqcn(resolvedFQCN)
                            .className(resolvedClassName)
                            .packageName(resolvedPkgName)
                            .build();

            }
        } else {
            return descriptor;
        }
    }

    private static ArchetypeDescriptor resolveBasePackageOf(ArchetypeDescriptor descriptor, String basePackage, String restObj) {
        log.info("[resolveBasePackageOf] restObj: {}", restObj);
        if (descriptor instanceof JavaArchetypeDescriptor) {
            JavaArchetypeDescriptor that = (JavaArchetypeDescriptor) descriptor;
            Map<String, String> map = new HashMap<>();   // the map for the mustache resolver
            map.put("basePackage", basePackage);
            map.put("restObj", restObj);
            map.put("endpoint", restObj.toLowerCase());
            String resolvedClassName = MustacheExpressionResolver.resolve(that.className(), map);
            String resolvedFQCN = MustacheExpressionResolver.resolve(that.fqcn(), map);
            String resolvedPkgName = MustacheExpressionResolver.resolve(that.packageName(), map);

            switch (descriptor.archetype()) {

                case AbstractIntegrationTest, ContainerConfiguration, RegisterDatabaseProperties: {
                    log.info("[resolveBasePackageOf: archetype: {}", descriptor.archetypeName());
                    return EdgeCaseResolvedArchetypeDescriptor.builder()
                            .archetype(descriptor.archetype())
                            .fqcn(resolvedFQCN)
                            .className(resolvedClassName)
                            .packageName(resolvedPkgName)
                            .build();
                }
                default:
                    var foo = ResolvedJavaArchetypeDescriptor.builder()
                            .archetype(descriptor.archetype())
                            .fqcn(resolvedFQCN)
                            .className(resolvedClassName)
                            .packageName(resolvedPkgName)
                            .build();
                    log.info("Resolved descriptor of endpoint archetype: {}", foo);
                    return foo;
            }
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

    @Builder
    private record EdgeCaseResolvedArchetypeDescriptor(Archetype archetype, String fqcn, String packageName,
                                                       String className) implements JavaArchetypeDescriptor {
        public String fqcnIntegrationTest() {
            return fqcn();
        }
        public String fqcnUnitTest() {
            return fqcn();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("NoOpTestDescriptor[className: ").append(className()).append(", ");
            sb.append("fqcn: ").append(fqcn()).append(", ");
            sb.append("unitTestClass: ").append(fqcnUnitTest()).append(", ");
            sb.append("integrationTestClass: ").append(fqcnIntegrationTest()).append(", ");
            sb.append("packageName: ").append(packageName()).append("]");
            return sb.toString();
        }
    }

}
