package mmm.coffee.metacode;

import lombok.RequiredArgsConstructor;
import mmm.coffee.metacode.cli.CommandHelpRenderer;
import mmm.coffee.metacode.cli.ManifestVersionProvider;
import mmm.coffee.metacode.cli.ParameterExceptionHandler;
import mmm.coffee.metacode.cli.PrintExceptionMessageHandler;
import mmm.coffee.metacode.cli.commands.MetaCodeCommand;
import mmm.coffee.metacode.cli.commands.create.CreateCommand;
import mmm.coffee.metacode.common.components.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import picocli.AutoComplete;
import picocli.CommandLine;

import static picocli.CommandLine.Model.UsageMessageSpec.SECTION_KEY_COMMAND_LIST;

@Configuration
@ComponentScan(basePackages = "mmm.coffee.metacode")
@EnableAutoConfiguration
@CommandLine.Command(
        name = "metacode",
        description = "Metacode is a code generator for Spring applications",
        versionProvider = ManifestVersionProvider.class,
        mixinStandardHelpOptions = true,
        subcommands = {AutoComplete.GenerateCompletion.class, CreateCommand.class}
)
public class Application implements CommandLineRunner, ExitCodeGenerator {

    private final MetaCodeCommand metaCodeCommand;

    private final CommandLine.IFactory factory; // auto-configured to inject PicocliSpringFactory

    private int exitCode;

    /**
     * Constructor
     */
    public Application(MetaCodeCommand createCommand, CommandLine.IFactory factory) {
        this.factory = factory;
        this.metaCodeCommand = createCommand;
    }

    /**
     * Main
     */
    public static void main(String... args) {
        final SpringApplication app = getSpringApplication();
        System.exit(SpringApplication.exit(app.run(args)));
    }

    private static SpringApplication getSpringApplication() {
        SpringApplication app = new SpringApplication(Application.class);

        // By default, spring-boot assumes any command-line args that start with 2 dashes ("--")
        // are environment properties.  To enable "--args" being handled by the picocli code,
        // spring-boot's behavior is turned off.
        app.setAddCommandLineProperties(false);

        // The application.yml settings have not been loaded and applied yet
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        return app;
    }


    /**
     * run
     */
    @Override
    public void run(String... args) throws Exception {
        Assert.notNull(metaCodeCommand, "CreateCommand is not auto-wired");
        Assert.notNull(factory, "IFactory was not auto-wired");

        CommandLine cmdLine = new CommandLine(metaCodeCommand, factory)
                .setUsageHelpAutoWidth(true) // take advantage of wide terminals when available
                .setExecutionExceptionHandler(new PrintExceptionMessageHandler())
                .setParameterExceptionHandler(new ParameterExceptionHandler());
        cmdLine.getHelpSectionMap().put(SECTION_KEY_COMMAND_LIST, new CommandHelpRenderer());
        exitCode = cmdLine.execute(args);
    }

    /**
     * @return the application's exit code
     */
    @Override
    public int getExitCode() {
        return 0;
    }
}

