package mmm.coffee.metacode.common.toml.templates;

import java.nio.file.Path;

public interface TemplateEntry {
    String key();
    Path templateSource();
    Path destinationPath();
    String packageName();
    String fullClassName();
}
