package mmm.coffee.metacode;

import mmm.coffee.metacode.cli.commands.MetaCodeCommand;
import mmm.coffee.metacode.cli.commands.create.CreateCommand;
import mmm.coffee.metacode.cli.commands.create.project.SubcommandCreateProject;
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
    MetaCodeCommand metaCodeCommand;

    @Autowired
    CreateCommand createCommand;

    @Test
    void shouldBeAbleToLaunchApp() throws Exception {
        Application app = new Application(metaCodeCommand, factory);
        app.run(toArgArray("create --help"));
        int exitCode = app.getExitCode();
        assertThat(exitCode).isZero();
    }

    @Test
    void testParsingCommandLineArgs() {
        // Start with the `create` command
        CommandLine.ParseResult projectCmd = new CommandLine(createCommand, factory)
                .parseArgs(toArgArray("project spring-webmvc --name petstore --package io.acme.petstore"));

        // The first subcommand encountered after `create` is 'project'
        assertThat(projectCmd).isNotNull();
        assertThat(projectCmd.hasSubcommand()).isTrue();

        // The next subcommand is `spring-webmvc`
        CommandLine.ParseResult springWebMvcCmd = projectCmd.subcommand();
        assertThat(springWebMvcCmd).isNotNull();
        assertThat(springWebMvcCmd.hasSubcommand()).isTrue();

        // As an example of how to drill down on the objects.
        SubcommandCreateProject obj = (SubcommandCreateProject) springWebMvcCmd.commandSpec().userObject();
        assertThat(obj).isNotNull();
    }

    private String[] toArgArray(String inputs) {
        return inputs.split(" ");
    }

}
