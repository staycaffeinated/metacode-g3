package mmm.coffee.metacode.spring.project.mustache;

import mmm.coffee.metacode.spring.project.converter.RestTemplateModelToMapConverter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

class MustacheDecoderTest {

    @Test
    void shouldRenderContent() {

        MustacheDecoder decoder = MustacheDecoder.builder()
                .map(exampleModel())
                .converter(getTemplateModelConverter())
                .build();

        String expression = "{{foo}}{{fizz}}";
        String result = decoder.decode(expression);
        assertThat(result).contains("bar");
        assertThat(result).contains("bin");
    }

    /* --------------------------------------------------------------------------------
     * HELPER METHODS
     * -------------------------------------------------------------------------------- */

    Map<String,String> exampleModel() {
        Map<String,String> model = new HashMap<>();
        model.put("foo","bar");
        model.put("fizz","bin");
        return model;
    }

    RestTemplateModelToMapConverter getTemplateModelConverter() {
        return new RestTemplateModelToMapConverter();
    }
}
