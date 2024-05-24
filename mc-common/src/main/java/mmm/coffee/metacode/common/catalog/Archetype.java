/*
 * Copyright 2022-2024 Jon Caulfield
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
package mmm.coffee.metacode.common.catalog;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({
        "java:S115" // enum values do not need to be in all-cap's
})
public enum Archetype {
    // endpoint prototypes
    Controller("Controller"),
    ServiceApi("ServiceApi"),
    ServiceImpl("ServiceImpl"),
    Repository("Repository"),
    PersistenceAdapter("PersistenceAdapter"),

    // project-scope prototypes
    GlobalExceptionHandler("GlobalExceptionHandler"),
    ResourceNotFoundException("ResourceNotFoundException"),
    BadRequestException("BadRequestException"),
    UnprocessableEntityException("UnprocessableEntityException"),

    // for plain text files, like the README.adoc, lombok.config, settings.gradle, etc.
    Text("Text"),

    // Anything that's not mapped
    Unknown("Unknown");

    private static final Map<String, Archetype> PROTOTYPE_MAP =
            Stream.of(Archetype.values()).collect(Collectors.toMap(s -> s.stringValue, Function.identity()));

    private final String stringValue;

    Archetype(String value) {
        this.stringValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    /**
     * Enables Jackson library to handle undefined values gracefully.
     * Unknown values will be mapped to the `Unknown` value.
     * @param value the text being mapped to a `PrototypeClass` value
     * @return the `PrototypeClass` equivalent of `value`
     */
    @JsonCreator
    public static Archetype fromString(String value) {
        return Optional.ofNullable(PROTOTYPE_MAP.get(value)).orElse(Archetype.Unknown);
    }
}