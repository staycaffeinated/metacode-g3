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
package mmm.coffee.metacode.common.dependency;

import lombok.NonNull;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import mmm.coffee.metacode.common.stereotype.DependencyCollector;

import java.io.IOException;
import java.util.List;

/**
 * Abstracts the contents of a library-versions.yaml file. Provides
 * a helper method to filter the catalog entries.
 */
public class DependencyCatalog implements DependencyCollector {

    private final String resourceLocation;
    private final DependencyFileReader reader;

    /**
     * This constructor creates a default DependencyCatalogReader.
     */
    public DependencyCatalog(@NonNull String resourceLocation) {
        this.resourceLocation = resourceLocation;
        reader = new DependencyFileReader();
    }

    /**
     * This constructor allows all fields to be defined by the caller.
     *
     * @param resourceLocation the classpath of the resource file,
     *                         such as "/spring/dependencies/dependencies.yml"
     * @param reader           a reader capable of parsing the dependencies.yml file
     */
    public DependencyCatalog(@NonNull String resourceLocation, @NonNull DependencyFileReader reader) {
        this.resourceLocation = resourceLocation;
        this.reader = reader;
    }

    /**
     * Reads the resource file that contains Dependency entries
     * and returns those Dependency entries.
     * <p>
     * Typically, the properties file is a file named 'dependencies.properties',
     * but that's just by convention within this project.
     *
     * @return the Dependency entries
     */
    @Override
    @SuppressWarnings({
            "java:S3358"    // allow ternary operations
    })
    public List<Dependency> collect() {
        try {
            Library library = reader.readDependencyFile(resourceLocation);
            return library.getDependencies();
        } catch (IOException e) {
            throw new RuntimeApplicationError(e.getMessage(), e);
        }
    }
}