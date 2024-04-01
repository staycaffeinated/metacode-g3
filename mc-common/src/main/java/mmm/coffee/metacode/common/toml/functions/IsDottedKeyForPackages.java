package mmm.coffee.metacode.common.toml.functions;

import java.util.Objects;
import java.util.function.Predicate;

public class IsDottedKeyForPackages implements Predicate<String> {

    @Override
    public boolean test(String key) {
        if (Objects.isNull(key)) return false;
        if (key.endsWith(TomlKeywords.CLASSES)) return true;
        return false;
    }

}
