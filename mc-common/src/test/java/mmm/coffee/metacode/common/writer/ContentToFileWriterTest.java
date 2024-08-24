package mmm.coffee.metacode.common.writer;

import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import mmm.coffee.metacode.common.io.FileSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

class ContentToFileWriterTest {

    @ParameterizedTest
    @NullAndEmptySource
    void shouldIgnoreEmptyContent(String content) {
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
    void shouldWriteUsingFileSystemWrapper(@TempDir Path tempDir) {
        FileSystem fs = new FileSystem();
        ContentToFileWriter writer = new ContentToFileWriter(fs);
        Path path = tempDir.resolve("deleteme02.txt");
        String tmpFileName = path.toFile().getAbsolutePath();

        writer.writeOutput(tmpFileName, "it is safe to delete me");
        assertThat(Files.exists(path)).isTrue();
    }

    @Test
    void shouldThrowExceptionIfDestinationIsNull() {
        FileSystem fs = new FileSystem();
        ContentToFileWriter writer = new ContentToFileWriter(fs);
        assertThrows(NullPointerException.class, () -> writer.writeOutput(null, "it is safe to delete me"));
    }

    @Test
    void shouldWrapIOExceptionInRuntimeAppException(@TempDir Path tempDir) throws IOException {
        FileSystem fs = Mockito.mock(FileSystem.class);
        doThrow(IOException.class).when(fs).forceMkdir(any(File.class));

        // In this test, nothing is written to this path. We just need a filename that works on any OS
        String tempPath = tempDir.toFile().getAbsolutePath();

        ContentToFileWriter writer = new ContentToFileWriter(fs);
        assertThrows(RuntimeApplicationError.class,
                () -> writer.writeOutput(tempPath,"This is some non-empty template to be rendered"));
    }
}
