package mmm.coffee.metacode.spring;

import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import mmm.coffee.metacode.common.io.MetaProperties;
import mmm.coffee.metacode.common.io.MetaPropertiesHandler;
import mmm.coffee.metacode.common.io.MetaPropertiesReader;
import mmm.coffee.metacode.spring.endpoint.io.SpringEndpointMetaPropertiesHandler;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.mockito.Mockito;

import static mmm.coffee.metacode.spring.constant.MustacheConstants.BASE_PACKAGE;
import static mmm.coffee.metacode.spring.constant.MustacheConstants.BASE_PATH;
import static org.mockito.Mockito.when;

public class MetaPropertiesHandlerFixture {

    /**
     * Returns a mock-up of the MetaPropertiesHandler. The mock-up does not read the file system.
     * Instead, there are predefined values for the various metacode.properties keys.
     * @param frameworkToUse indicate which framework flavor to use (webmvc, webflux, boot, batch, ...)
     * @return a mock-up of a MetaPropertiesHandler 
     */
    public static MetaPropertiesHandler<RestEndpointDescriptor> buildMockMetaPropertiesHandler(String frameworkToUse) {
        // Mock a Configuration that will return values that are
        // normally acquired by reading the metacode.properties file.
        Configuration mockConfig = Mockito.mock(Configuration.class);
        when(mockConfig.getString(MetaProperties.BASE_PATH)).thenReturn(BASE_PATH);
        when(mockConfig.getString(MetaProperties.BASE_PACKAGE)).thenReturn(BASE_PACKAGE);
        when(mockConfig.getString(MetaProperties.FRAMEWORK)).thenReturn(frameworkToUse);

        // Set up the MetaPropertiesHandler. We only have to mock reading;
        // endpoint code generation never writes to the metacode.properties file.
        MetaPropertiesHandler mockMetaPropHandler = Mockito.mock(MetaPropertiesHandler.class);
        when(mockMetaPropHandler.readMetaProperties()).thenReturn(mockConfig);
        return mockMetaPropHandler;
    }

    /**
     * Builds a MetaPropertiesHandler that looks for the (real) metacode.properties file on the file system
     * @return a MetaPropertiesHandler
     */
    public static MetaPropertiesHandler<RestEndpointDescriptor> providesEndpointMetaPropertiesHandler() {
        return SpringEndpointMetaPropertiesHandler.builder()
                .reader(MetaPropertiesReader.builder()
                        .propertyFileName(MetaProperties.DEFAULT_FILENAME)
                        .configurations(new Configurations())
                        .build())
                .build();
    }
}
