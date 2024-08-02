package mmm.coffee.metacode.common.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileSystemTest {

    @Test
    void shouldMakeDirectory(@TempDir Path tempDir) throws IOException {
        FileSystem fileSystem = new FileSystem();
        Path path = tempDir.resolve("some/folder/src/main/java");

        fileSystem.forceMkdir(path.toFile());
        assertThat(Files.exists(path)).isTrue();
    }

    @Test
    void shouldWriteToFile(@TempDir Path tempDir) throws IOException {
        FileSystem fileSystem = new FileSystem();
        Path path = tempDir.resolve("some/folder/src/main/java/Simple.java");

        String expectedContent = "Ipsum Dolor Factorial E Pluribus";
        fileSystem.writeStringToFile(path.toFile(), expectedContent, Charset.defaultCharset());
        assertThat(Files.exists(path)).isTrue();
        assertThat(Files.readAllLines(path).get(0)).contains(expectedContent);

    }

}
