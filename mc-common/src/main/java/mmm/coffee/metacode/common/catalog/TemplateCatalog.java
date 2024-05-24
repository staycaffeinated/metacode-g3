package mmm.coffee.metacode.common.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateCatalog {
    private String catalogName;
    private List<CatalogEntry> entries = new ArrayList<>();
}
