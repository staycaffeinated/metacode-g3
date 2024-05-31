package mmm.coffee.metacode.common.toml;

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
    private List<String> archetypes = new ArrayList();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("packageName: ").append(packageName).append(",");
        sb.append("archetypes: ").append(archetypes).append("}");
        return sb.toString();
    }
}
