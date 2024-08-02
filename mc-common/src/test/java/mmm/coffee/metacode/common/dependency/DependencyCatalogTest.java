/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.common.dependency;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * DependencyCatalogIT
 */
@Tag("integration")
class DependencyCatalogTest {

    private static final String DEPENDENCY_FILE = "/spring/dependencies/dependencies.yml";

    final DependencyCatalog catalogUnderTest = new DependencyCatalog(DEPENDENCY_FILE);

    /**
     * This test verifies the production dependencies.yml file can be collected
     */
    @Test
    void shouldReadCatalogSuccessfully() {
        var collection = catalogUnderTest.collect();

        assertThat(collection).isNotEmpty();
    }

    @Test
    void shouldAllowDependencyFileReaderArg() {
        DependencyFileReader reader = new DependencyFileReader();
        DependencyCatalog catalog = new DependencyCatalog(DEPENDENCY_FILE, reader);
        assertThat(catalog.collect()).isNotEmpty();

    }
}
