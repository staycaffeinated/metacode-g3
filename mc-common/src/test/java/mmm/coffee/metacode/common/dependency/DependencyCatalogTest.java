/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.common.dependency;

import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldThrowExceptionWhenResourceNameIsNull() {
        DependencyFileReader reader = new DependencyFileReader();
        assertThrows(NullPointerException.class, () -> new DependencyCatalog(null));
        assertThrows(NullPointerException.class, () -> new DependencyCatalog(null, reader));

    }

    @Test
    void shouldThrowExceptionWhenReaderIsNull() {
        assertThrows(NullPointerException.class, () -> new DependencyCatalog(DEPENDENCY_FILE, null));
    }

    @Test
    void shouldWrapIoExceptionsAsError() throws IOException {
        DependencyFileReader reader = Mockito.mock(DependencyFileReader.class);
        when(reader.readDependencyFile(anyString())).thenThrow(IOException.class);

        // If an IOException occurs when reading the catalog, the IOException should be recast as an Error
        DependencyCatalog catalog = new DependencyCatalog(DEPENDENCY_FILE, reader);
        assertThrows(RuntimeApplicationError.class, catalog::collect);
    }

}
