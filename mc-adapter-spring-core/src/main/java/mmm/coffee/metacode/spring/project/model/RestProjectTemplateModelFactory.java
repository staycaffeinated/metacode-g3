package mmm.coffee.metacode.spring.project.model;


import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import mmm.coffee.metacode.common.toml.PackageDataDictionary;
import mmm.coffee.metacode.spring.project.converter.DescriptorToTemplateModelConverter;

public class RestProjectTemplateModelFactory {

    private DependencyCatalog dependencyCatalog;
    private RestProjectDescriptor projectDescriptor;
    private PackageDataDictionary dataDictionary;

    DescriptorToTemplateModelConverter converter = new DescriptorToTemplateModelConverter();

    public static RestProjectTemplateModelFactory create() {
        return new RestProjectTemplateModelFactory();
    }

    public RestProjectTemplateModel build() {
        RestProjectTemplateModel model = converter.convert(projectDescriptor);
        model.configureLibraryVersions(dependencyCatalog);
        return model;
    }

    public RestProjectTemplateModelFactory usingDependencyCatalog(DependencyCatalog dependencyCatalog) {
        this.dependencyCatalog = dependencyCatalog;
        return this;
    }

    public RestProjectTemplateModelFactory usingProjectDescriptor(RestProjectDescriptor projectDescriptor) {
        this.projectDescriptor = projectDescriptor;
        return this;
    }

    public RestProjectTemplateModelFactory usingDataDictionary(PackageDataDictionary dataDictionary) {
        this.dataDictionary = dataDictionary;
        return this;
    }
}
