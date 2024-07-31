package mmm.coffee.metacode.common.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PackageLayoutEntry {
    @JsonProperty("package")
    private String packageName;

    @JsonProperty("archetypes")
    private List<String> archetypes = new ArrayList<>();

    @Override
    public String toString() {
        return "{" + "packageName: " + packageName + "," +
                "archetypes: " + archetypes + "}";
    }
}
