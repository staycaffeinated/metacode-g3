package mmm.coffee.metacode.spring.model;

import mmm.coffee.metacode.common.toml.DefaultPackageDataDictionary;
import mmm.coffee.metacode.common.toml.PackageDataDictionary;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static mmm.coffee.metacode.common.toml.DefaultPackageDataDictionary.DEFAULT_PACKAGE;

public class ClassInventoryFactoryTests {

    PackageDataDictionary packageDataDictionary = new DefaultPackageDataDictionary();

    @Nested
    class ProjectScopeClassTests {

        ClassInventory classUnderTest = ClassInventoryFactory.create(packageDataDictionary);

        @Test
        void shouldReturnPackageNames() {
            assertThat(classUnderTest.getAlphabeticAnnotation().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getAlphabeticValidator().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getApplicationConfiguration().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getBadRequestException().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getDateTimeFormatConfiguration().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getGlobalExceptionHandler().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getLocalDateConverter().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getProblemConfiguration().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getResourceIdTrait().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getResourceIdValidator().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getResourceIdAnnotation().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getSecureRandomSeries().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getSpringProfiles().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getUnprocessableEntityException().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getWebMvcConfiguration().packageName()).startsWith(DEFAULT_PACKAGE);
        }

        @Test
        void shouldReturnSimpleClassNames() {
            assertThat(classUnderTest.getAlphabeticAnnotation().className()).isNotEmpty();
            assertThat(classUnderTest.getAlphabeticValidator().className()).isNotEmpty();
            assertThat(classUnderTest.getApplicationConfiguration().className()).isNotEmpty();
            assertThat(classUnderTest.getBadRequestException().className()).isNotEmpty();
            assertThat(classUnderTest.getDateTimeFormatConfiguration().className()).isNotEmpty();
            assertThat(classUnderTest.getGlobalExceptionHandler().className()).isNotEmpty();
            assertThat(classUnderTest.getLocalDateConverter().className()).isNotEmpty();
            assertThat(classUnderTest.getProblemConfiguration().className()).isNotEmpty();
            assertThat(classUnderTest.getResourceIdTrait().className()).isNotEmpty();
            assertThat(classUnderTest.getResourceIdValidator().className()).isNotEmpty();
            assertThat(classUnderTest.getResourceIdAnnotation().className()).isNotEmpty();
            assertThat(classUnderTest.getSecureRandomSeries().className()).isNotEmpty();
            assertThat(classUnderTest.getSpringProfiles().className()).isNotEmpty();
            assertThat(classUnderTest.getUnprocessableEntityException().className()).isNotEmpty();
            assertThat(classUnderTest.getWebMvcConfiguration().className()).isNotEmpty();
        }
    }

    @Nested
    class EndpointScopeClassTests {

        private static final String EXAMPLE = "Book";
        ClassInventory classUnderTest = ClassInventoryFactory.create(packageDataDictionary, EXAMPLE);

        @Test
        void shouldDefineControllerClassInfo() {
            assertThat(classUnderTest.getController().className()).startsWith(EXAMPLE);
            assertThat(classUnderTest.getController().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getController().varName()).isNotEmpty();
            assertThat(classUnderTest.getController().fqName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getController().fqName()).contains(EXAMPLE);
        }

        @Test
        void shouldDefineServiceImplClassInfo() {
            assertThat(classUnderTest.getServiceImpl().className()).startsWith(EXAMPLE);
            assertThat(classUnderTest.getServiceImpl().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getServiceImpl().varName()).isNotEmpty();
            assertThat(classUnderTest.getServiceImpl().fqName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getServiceImpl().fqName()).contains(EXAMPLE);
        }

        @Test
        void shouldDefineServiceApiClassInfo() {
            assertThat(classUnderTest.getServiceApi().className()).startsWith(EXAMPLE);
            assertThat(classUnderTest.getServiceApi().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getServiceApi().varName()).isNotEmpty();
            assertThat(classUnderTest.getServiceApi().fqName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getServiceApi().fqName()).contains(EXAMPLE);
        }

        @Test
        void shouldDefineRepositoryClassInfo() {
            assertThat(classUnderTest.getRepository().className()).startsWith(EXAMPLE);
            assertThat(classUnderTest.getRepository().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getRepository().varName()).isNotEmpty();
            assertThat(classUnderTest.getRepository().fqName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getRepository().fqName()).contains(EXAMPLE);
        }

        @Test
        void shouldDefineCustomRepositoryClassInfo() {
            assertThat(classUnderTest.getCustomRepository().className()).startsWith(EXAMPLE);
            assertThat(classUnderTest.getCustomRepository().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getCustomRepository().varName()).isNotEmpty();
            assertThat(classUnderTest.getCustomRepository().fqName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getCustomRepository().fqName()).contains(EXAMPLE);
        }

        @Test
        void shouldDefineEntityClassInfo() {
            assertThat(classUnderTest.getEntity().className()).startsWith(EXAMPLE);
            assertThat(classUnderTest.getEntity().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getEntity().varName()).isNotEmpty();
            assertThat(classUnderTest.getEntity().fqName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getEntity().fqName()).contains(EXAMPLE);
        }

        @Test
        void shouldDefineEntityToPojoConverterClassInfo() {
            assertThat(classUnderTest.getEntityToPojoConverter().className()).startsWith(EXAMPLE);
            assertThat(classUnderTest.getEntityToPojoConverter().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getEntityToPojoConverter().varName()).isNotEmpty();
            assertThat(classUnderTest.getEntityToPojoConverter().fqName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getEntityToPojoConverter().fqName()).contains(EXAMPLE);
        }

        @Test
        void shouldDefinePojoToEntityConverterClassInfo() {
            assertThat(classUnderTest.getPojoToEntityConverter().className()).startsWith(EXAMPLE);
            assertThat(classUnderTest.getPojoToEntityConverter().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getPojoToEntityConverter().varName()).isNotEmpty();
            assertThat(classUnderTest.getPojoToEntityConverter().fqName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getPojoToEntityConverter().fqName()).contains(EXAMPLE);
        }

        @Test
        void shouldDefineResourcePojoClassInfo() {
            assertThat(classUnderTest.getResourcePojo().className()).startsWith(EXAMPLE);
            assertThat(classUnderTest.getResourcePojo().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getResourcePojo().varName()).isNotEmpty();
            assertThat(classUnderTest.getResourcePojo().fqName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getResourcePojo().fqName()).contains(EXAMPLE);
        }

        @Test
        void shouldDefineRoutesClassInfo() {
            assertThat(classUnderTest.getRoutes().className()).startsWith(EXAMPLE);
            assertThat(classUnderTest.getRoutes().packageName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getRoutes().varName()).isNotEmpty();
            assertThat(classUnderTest.getRoutes().fqName()).startsWith(DEFAULT_PACKAGE);
            assertThat(classUnderTest.getRoutes().fqName()).contains(EXAMPLE);
        }
    }

}
