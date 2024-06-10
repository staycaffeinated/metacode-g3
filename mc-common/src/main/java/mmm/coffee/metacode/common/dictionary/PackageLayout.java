package mmm.coffee.metacode.common.dictionary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties
public class PackageLayout {
    private String layoutName;

    @JsonProperty("layout")
    private List<PackageLayoutEntry> entries;
}
