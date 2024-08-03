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

    /**
     * Creates a Map of the custom properties that need to be added to the template model
     * to enable these properties to be used in template rendering.  This method is
     * called when creating project artifacts.
     * @param archetypeDescriptorFactory a factory to create ArchetypeDescriptors
     * @param basePackage the base package of the code being generated (e.g., "org.example.petstore")
     * @return the mapping
     */
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

    /**
     * Creates a Map of the custom properties that need to be added to the template model
     * to enable these properties to be used in template rendering.  This method is called
     * when creating endpoint artifacts.
     * @param archetypeDescriptorFactory a factory to create ArchetypeDescriptors
     * @param basePackage the base package of the code being generated (e.g., "org.example.petstore")
     * @param restResource the REST endpoint resource (e.g., "Pet" of the petstore example)
     * @return the mapping
     */
    public static Map<String, Object> assembleCustomProperties(IArchetypeDescriptorFactory archetypeDescriptorFactory,
                                                               String basePackage,
                                                               String restResource) {
        Map<String, Object> projectScopeProperties = assembleCustomProperties(archetypeDescriptorFactory, basePackage);

        Map<String, ArchetypeDescriptor> customProperties = EndpointArchetypeToMap.map(archetypeDescriptorFactory, restResource);
        // The templates of endpoint-scope classes (such as Controller) will need the coordinates
        // of project-scope classes (such as Exceptions and ResourceIdSuppliers). 
        Map<String, Object> endpointScopeProperties = new TreeMap<>(projectScopeProperties);
        customProperties.forEach((key, value) -> {
            ArchetypeDescriptor descriptor1 = resolveBasePackageOf(value, basePackage, restResource);
            endpointScopeProperties.put(key, descriptor1);
        });
        return endpointScopeProperties;
    }

    /*
     *
     */
    protected static ArchetypeDescriptor resolveBasePackageOf(ArchetypeDescriptor descriptor, String basePackage) {
        if (descriptor instanceof JavaArchetypeDescriptor that) {
            Map<String, String> map = new HashMap<>();   // the map for the mustache resolver
            map.put("basePackage", basePackage);
            String resolvedClassName = MustacheExpressionResolver.resolve(that.className(), map);
            String resolvedFQCN = MustacheExpressionResolver.resolve(that.fqcn(), map);
            String resolvedPkgName = MustacheExpressionResolver.resolve(that.packageName(), map);

            return switch (descriptor.archetype()) {
                case AbstractIntegrationTest, ContainerConfiguration, RegisterDatabaseProperties -> {
                    log.info("[resolveBasePackageOf: archetype: {}", descriptor.archetypeName());
                    yield EdgeCaseResolvedArchetypeDescriptor.builder()
                            .archetype(descriptor.archetype())
                            .fqcn(resolvedFQCN)
                            .className(resolvedClassName)
                            .packageName(resolvedPkgName)
                            .build();
                }
                default -> ResolvedJavaArchetypeDescriptor.builder()
                        .archetype(descriptor.archetype())
                        .fqcn(resolvedFQCN)
                        .className(resolvedClassName)
                        .packageName(resolvedPkgName)
                        .build();
            };
        } else {
            return descriptor;
        }
    }

    protected static ArchetypeDescriptor resolveBasePackageOf(ArchetypeDescriptor descriptor, String basePackage, String restObj) {
        log.info("[resolveBasePackageOf] restObj: {}", restObj);
        if (descriptor instanceof JavaArchetypeDescriptor that) {
            Map<String, String> map = new HashMap<>();   // the map for the mustache resolver
            map.put("basePackage", basePackage);
            map.put("restObj", restObj);
            map.put("endpoint", restObj.toLowerCase());
            String resolvedClassName = MustacheExpressionResolver.resolve(that.className(), map);
            String resolvedFQCN = MustacheExpressionResolver.resolve(that.fqcn(), map);
            String resolvedPkgName = MustacheExpressionResolver.resolve(that.packageName(), map);

            return switch (descriptor.archetype()) {
                case AbstractIntegrationTest, ContainerConfiguration, RegisterDatabaseProperties,
                     TestTableInitializer: {
                    log.debug("[resolveBasePackageOf: archetype: {}", descriptor.archetypeName());
                    yield EdgeCaseResolvedArchetypeDescriptor.builder()
                            .archetype(descriptor.archetype())
                            .fqcn(resolvedFQCN)
                            .className(resolvedClassName)
                            .packageName(resolvedPkgName)
                            .build();
                }
                default: {
                    yield ResolvedJavaArchetypeDescriptor.builder()
                            .archetype(descriptor.archetype())
                            .fqcn(resolvedFQCN)
                            .className(resolvedClassName)
                            .packageName(resolvedPkgName)
                            .build();
                }
            };
        } else {
            return descriptor;
        }
    }


    @Builder
    private record ResolvedJavaArchetypeDescriptor(Archetype archetype, String fqcn, String packageName,
                                                   String className) implements JavaArchetypeDescriptor {

        @Override
        public String testClass() {
            return JavaArchetypeDescriptor.super.testClass();
        }

        public String toString() {
            return "ResolvedJavaArchetypeDescriptor[className: " + className() + ", " +
                    "fqcn: " + fqcn() + ", " +
                    "unitTest: " + fqcnUnitTest() + ", " +
                    "integrationTest: " + fqcnIntegrationTest() + ", " +
                    "packageName: " + packageName() + "]";
        }
    }

    @Builder
    private record EdgeCaseResolvedArchetypeDescriptor(Archetype archetype, String fqcn, String packageName,
                                                       String className) implements JavaArchetypeDescriptor {
        @Override
        public String fqcnIntegrationTest() {
            return fqcn();
        }

        @Override
        public String fqcnUnitTest() {
            return fqcn();
        }

        public String toString() {
            return "NoOpTestDescriptor[className: " + className() + ", " +
                    "fqcn: " + fqcn() + ", " +
                    "unitTestClass: " + fqcnUnitTest() + ", " +
                    "integrationTestClass: " + fqcnIntegrationTest() + ", " +
                    "packageName: " + packageName() + "]";
        }
    }

}
