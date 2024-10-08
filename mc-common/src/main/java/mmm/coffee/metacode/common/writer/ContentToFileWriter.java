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
package mmm.coffee.metacode.common.writer;

import lombok.NonNull;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import mmm.coffee.metacode.common.io.FileSystem;
import mmm.coffee.metacode.common.trait.WriteOutputTrait;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Handles writing String content to a destination.
 * Typically, the destination is a File but, for testing,
 * writing can be a no-op.
 */
public class ContentToFileWriter implements WriteOutputTrait {

    private final FileSystem fileSystem;

    /**
     * Default constructor
     */
    public ContentToFileWriter() {
        fileSystem = new FileSystem();
    }

    /**
     * Constructor
     */
    public ContentToFileWriter(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    /**
     * Writes {@code content} to the {@code destination}.
     * A File object of {@code destination} is created.
     * <p>
     * If {@code content} is empty, nothing happens here.
     * If {@code content} is an empty string, the destination file is created,
     * with the empty content written to it.
     * </p>
     *
     * @param destination the FQP to the output file
     * @param content     the content written to the output file
     */
    @Override
    public void writeOutput(@NonNull String destination, String content) {
        if (isEmpty(content)) return;
        try {
            File fOutput = new File(destination);
            fileSystem.forceMkdir(fOutput.getParentFile());
            fileSystem.writeStringToFile(fOutput, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeApplicationError(e.getMessage(), e);
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}