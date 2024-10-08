/*
 * Copyright 2022 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.metacode.spring.catalog;

import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.catalog.ICatalogReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test
 */
class SpringWebFluxTemplateCatalogTest {

    SpringWebFluxTemplateCatalog catalogUnderTest;

    @BeforeEach
    public void setUp() {
        CatalogFileReader catalogFileReader = new CatalogFileReader();
        catalogUnderTest = new SpringWebFluxTemplateCatalog(catalogFileReader);
    }

    @Test
    void shouldReadTemplates() {
        List<CatalogEntry> resultSet = catalogUnderTest.collect();
        assertThat(resultSet).isNotNull();
        assertThat(resultSet.size()).isGreaterThan(0);
    }

    /*
     * This is a white box test of the {@code SpringTemplateCatalog}
     * constructor.  Our fake subclass's constructor invokes
     * the super's constructor with a null argument to verify
     * an NPE is thrown.
     */
    @SuppressWarnings({
            "java:S125" // false positive; there's no commented-out code here
    })
    @Test
    void shouldThrowExceptionWhenCatalogReaderArgIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new FakeTemplateCatalog(null);
        });
    }

    
    @Nested
    class ConstructorTests {
        @Test
        void shouldThrowExceptionWhenCatalogReaderIsNull() {
            assertThrows(NullPointerException.class, () -> new SpringWebFluxTemplateCatalog(null));
        }

        @Test
        void shouldBuildObjectSuccessfully() {
            var catalogReader = Mockito.mock(ICatalogReader.class);
            var catalog = new SpringWebFluxTemplateCatalog(catalogReader);
            assertThat(catalog).isNotNull();
        }
    }

    @Test
    void shouldHaveAnActiveCatalog() {
        assertThat(catalogUnderTest.getActiveCatalog()).isNotEmpty();
    }

    @Test
    void shouldReturnDefaultCatalogWhenNothingActive() {
        ICatalogReader reader = Mockito.mock(ICatalogReader.class);
        SpringWebFluxTemplateCatalog catalog = new SpringWebFluxTemplateCatalog(reader);
        assertThat(catalog.getActiveCatalog()).isNotEmpty();
        assertThat(catalog.getActiveCatalog()).isEqualTo(SpringTemplateCatalog.WEBFLUX_CATALOG);
    }

    /* --------------------------------------------------------------------------------------------
     * HELPER METHODS
     * -------------------------------------------------------------------------------------------- */
    private static class FakeTemplateCatalog extends SpringTemplateCatalog {

        public FakeTemplateCatalog(ICatalogReader reader) {
            super(reader);
        }

        /**
         * Collects items, honoring the conditions set with {@code setConditions}
         *
         * @return the items meeting the conditions
         */
        @Override
        public List<CatalogEntry> collect() {
            return null;
        }

    }
}
