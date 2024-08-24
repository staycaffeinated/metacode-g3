/*
 * Copyright 2020 Jon Caulfield
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.NonNull;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Reads the dependencies.yml file
 */
public class DependencyFileReader {

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    public Library readDependencyFile(@NonNull String resourceLocation) throws IOException {
        Properties properties = read(resourceLocation);
        Library library = new Library();
        // library.setDependencies(new ArrayList<>());
        properties.forEach((k, v) -> {
            Dependency dependency = new Dependency((String) k, (String) v);
            library.getDependencies().add(dependency);
        });
        return library;

    }

    public Properties read(@NonNull String location) throws IOException {
        Properties properties = new Properties();
        properties.load(resourceLoader.getResource(location).getInputStream());
        return properties;
    }

    /**
     * Reads the dependencies file and returns a POJO that mirrors it content.
     *
     * @param resourcePathOfDependenciesYaml the YAML file to read, usually the dependencies.yml
     * @return a Library object that mirrors the content of the file
     * @throws IOException if the dependencyFile cannot be read
     */
    public Library __readDependencyFile(@NonNull String resourcePathOfDependenciesYaml) throws IOException {
        try (InputStream is = this.getClass().getResourceAsStream(resourcePathOfDependenciesYaml)) {
            // This null check only ever fails during testing, since the file location will be fixed in production
            Objects.requireNonNull(is, String.format("The file resource, '%s', was not found. Verify the resource exists on the classpath in the given directory", resourcePathOfDependenciesYaml));
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(is, Library.class);
        }
    }
}
