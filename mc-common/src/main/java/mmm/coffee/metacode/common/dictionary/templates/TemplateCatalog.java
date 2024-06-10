package mmm.coffee.metacode.common.dictionary.templates;

import java.util.List;

public interface TemplateCatalog {

    List<TemplateEntry> entries();

    void addEntry(TemplateEntry entry);
}
