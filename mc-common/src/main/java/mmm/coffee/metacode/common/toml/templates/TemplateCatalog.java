package mmm.coffee.metacode.common.toml.templates;

import java.util.List;

public interface TemplateCatalog {

    List<TemplateEntry> entries();

    void addEntry(TemplateEntry entry);
}
