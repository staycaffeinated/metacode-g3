package mmm.coffee.metacode.common.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateFacet {
    private String facet;
    private String sourceTemplate;
    private String destination;
}
