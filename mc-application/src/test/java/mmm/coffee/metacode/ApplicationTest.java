package mmm.coffee.metacode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import picocli.CommandLine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

/**
 * Unit tests of Application
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = NONE, classes = Application.class)
class ApplicationTest {

    @Autowired
    CommandLine.IFactory factory;

    @Autowired
    MyCommand myCommand;

    @Test
    void shouldBeAbleToLaunchApp() throws Exception {
        Application app = new Application(myCommand, factory);
        app.run(toArgArray("-x abc"));
        int exitcode = app.getExitCode();
        assertThat(exitcode).isZero();
    }

    @Test
    void testParsingCommandLineArgs() {
        CommandLine.ParseResult parseResult = new CommandLine(myCommand, factory)
                .parseArgs(toArgArray("-x=someX"));

        assertThat(myCommand.getX()).isEqualTo("someX");
        assertThat(myCommand.positionals).isEmpty();

//        assertTrue(parseResult.hasSubcommand());
//        CommandLine.ParseResult subParseResult = parseResult.subcommand();
//        MyCommand.Sub sub = (MyCommand.Sub) subParseResult.commandSpec().userObject();
//        assertEquals("123", sub.y);
//        // assertNull(sub.positionals);
    }

    private String[] toArgArray(String inputs) {
        return inputs.split(" ");
    }

}
