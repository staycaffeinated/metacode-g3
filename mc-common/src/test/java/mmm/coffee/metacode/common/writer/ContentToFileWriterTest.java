package mmm.coffee.metacode.common.writer;

import mmm.coffee.metacode.common.io.FileSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.truth.Truth.assertThat;

class ContentToFileWriterTest {

    @ParameterizedTest
    @NullAndEmptySource
    void shouldIgnoreEmptyContent(String content) throws IOException {
        ContentToFileWriter writer = new ContentToFileWriter();
        writer.writeOutput("never-used-in-this-use-case", content);
        assertThat(writer).isNotNull();
    }

    @Test
    void shouldWriteContent(@TempDir Path tempDir) {
        Path path = tempDir.resolve("deleteme.txt");
        String tmpFileName = path.toFile().getAbsolutePath();
        ContentToFileWriter writer = new ContentToFileWriter();
        writer.writeOutput(tmpFileName, "it is safe to delete me");

        assertThat(Files.exists(path)).isTrue();
    }

    @Test
    void shouldWriteUsingFileSystemWrapper(@TempDir Path tempDir)  {
        FileSystem fs = new FileSystem();
        ContentToFileWriter writer = new ContentToFileWriter(fs);
        Path path = tempDir.resolve("deleteme02.txt");
        String tmpFileName = path.toFile().getAbsolutePath();

        writer.writeOutput(tmpFileName, "it is safe to delete me");
        assertThat(Files.exists(path)).isTrue();
    }
}
