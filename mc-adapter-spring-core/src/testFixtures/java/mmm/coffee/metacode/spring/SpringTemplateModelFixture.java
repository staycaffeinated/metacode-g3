package mmm.coffee.metacode.spring;

import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import mmm.coffee.metacode.spring.endpoint.model.RestEndpointTemplateModel;
import mmm.coffee.metacode.spring.project.generator.CustomPropertyAssembler;
import mmm.coffee.metacode.spring.project.model.RestProjectTemplateModel;
import mmm.coffee.metacode.spring.project.model.SpringTemplateModel;

import java.io.IOException;
import java.util.Map;

public class SpringTemplateModelFixture {

    SpringTemplateModel aSpringProjectTemplateModel() throws IOException {
        return RestProjectTemplateModel.builder()
                .applicationName("petstore")
                .basePackage(Constants.BASE_PKG)
                .basePath(Constants.BASE_PATH)
                .groupId(Constants.BASE_PKG)
                .isWebMvc(true)
                .customProperties(projectScopeCustomProperties())
                .build();
    }

    public static SpringTemplateModel aSpringEndpointTemplateModel() throws IOException {
        return RestEndpointTemplateModel.builder()
                .basePackage(Constants.BASE_PKG)
                .basePath(Constants.BASE_PATH)
                .isWebMvc(true)
                .customProperties(endpointScopeCustomProperties())
                .resource(Constants.PET_RESOURCE)
                .route(Constants.PET_ROUTE)
                .build();
    }

    static Map<String, Object> projectScopeCustomProperties() throws IOException {
        return CustomPropertyAssembler.assembleCustomProperties(fakeArchetypeDescriptorFactory(), Constants.BASE_PKG);
    }
    static Map<String, Object> endpointScopeCustomProperties() throws IOException {
        return CustomPropertyAssembler.assembleCustomProperties(fakeArchetypeDescriptorFactory(), Constants.BASE_PKG, Constants.PET_RESOURCE);
    }

    static IArchetypeDescriptorFactory fakeArchetypeDescriptorFactory() throws IOException {
        return FakeArchetypeDescriptorFactory.archetypeDescriptorFactory();
    }

}
