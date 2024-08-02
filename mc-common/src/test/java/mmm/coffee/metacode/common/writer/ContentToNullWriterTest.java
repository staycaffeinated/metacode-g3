package mmm.coffee.metacode.common.writer;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

class ContentToNullWriterTest {

    @Test
    void shouldBehaveAsNoOpMethod() {
        ContentToNullWriter writer = new ContentToNullWriter();
        writer.writeOutput("does-not-matter", "also-does-not-matter");
        assertThat(writer).isNotNull();
    }

}
