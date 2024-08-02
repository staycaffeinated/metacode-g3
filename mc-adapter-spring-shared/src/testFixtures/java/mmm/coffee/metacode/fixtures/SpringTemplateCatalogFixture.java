package mmm.coffee.metacode.fixtures;

import lombok.NonNull;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.catalog.ICatalogReader;
import mmm.coffee.metacode.spring.catalog.SpringTemplateCatalog;

import java.util.List;

public class SpringTemplateCatalogFixture {

    public static SpringTemplateCatalog springTemplateCatalog() {
        return new FakeSpringTemplateCatalog(new CatalogFileReader());
    }

    public static class FakeSpringTemplateCatalog extends SpringTemplateCatalog {

        public FakeSpringTemplateCatalog(@NonNull ICatalogReader reader) {
            super(reader);
        }

        public List<CatalogEntry> collect() {
            return super.collectGeneralCatalogsAndThisOne(WEBMVC_CATALOG);
        }
    }

}
