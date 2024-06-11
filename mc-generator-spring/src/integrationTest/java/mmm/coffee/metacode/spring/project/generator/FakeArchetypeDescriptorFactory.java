package mmm.coffee.metacode.spring.project.generator;

import lombok.Builder;
import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;

import java.util.HashMap;
import java.util.Map;

public class FakeArchetypeDescriptorFactory implements IArchetypeDescriptorFactory {

    private final PackageLayoutRuleSet packageLayoutRuleSet = basicPackageLayoutRuleSet();
    private final ClassNameRuleSet classNameRuleSet = basicClassNameRuleSet();


    public FakeArchetypeDescriptorFactory() {
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
    }

    /* -----------------------------------------------------------------------------------------------------------
     * Helper methods
     * ----------------------------------------------------------------------------------------------------------- */


    private static PackageLayoutRuleSet basicPackageLayoutRuleSet() {
        HashMap<String, String> rules = new HashMap<>();
        Archetype[] values = Archetype.values();
        for (Archetype e : values) {
            rules.put(e.toString(), "com.acme.petstore");
        }
        rules.put("Controller", "com.acme.petstore.{{restResource}}.api");
        rules.put("ServiceApi", "com.acme.petstore.{{restResource}}.api");
        rules.put("ServiceImpl", "com.acme.petstore.{{restResource}}.api");
        rules.put("Routes", "com.acme.petstore.{{restResource}}.api");
        rules.put("Application", "com.acme.petstore");
        rules.put(Archetype.SecureRandomSeries.toString(), "com.acme.petstore.utils");

        return new PackageLayoutRuleSet(rules);
    }

    private static ClassNameRuleSet basicClassNameRuleSet() {
        Map<String, String> rules = new HashMap<>();
        rules.put(Archetype.Application.toString(), "Application");
        rules.put(Archetype.Controller.toString(), "{{restResource}}Controller");
        rules.put(Archetype.ExceptionHandler.toString(), "GlobalExceptionHandler");
        rules.put(Archetype.SecureRandomSeries.toString(), "ResourceIdGenerator");  // an example of the classname not matching archetype name
        rules.put(Archetype.ServiceApi.toString(), "{{restResource}}Service");
        rules.put(Archetype.ServiceImpl.toString(), "{{restResource}}ServiceImpl");

        return new ClassNameRuleSet(rules);
    }
}
