package mmm.coffee.metacode;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import picocli.CommandLine;

import javax.swing.*;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application implements CommandLineRunner, ExitCodeGenerator {

    private final MyCommand myCommand;

    private final CommandLine.IFactory factory; // auto-configured to inject PicocliSpringFactory

    private int exitCode;

    /**
     * Constructor
     */
    public Application(MyCommand myCommand, CommandLine.IFactory factory) {
        this.factory = factory;
        this.myCommand = myCommand;
    }

    /**
     * Main
     */
    public static void main(String... args) {
        SpringApplication app = new SpringApplication(Application.class);

        // By default, spring-boot assumes any command-line args that start with 2 dashes ("--")
        // are environment properties.  To enable "--args" being handled by the picocli code,
        // spring-boot's behavior is turned off.
        app.setAddCommandLineProperties(false);

        // The application.yml settings have not been loaded and applied yet
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);

        System.exit(SpringApplication.exit(app.run(args)));
    }

    /**
     * run
     */
    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(myCommand, factory).execute(args);
    }

    /**
     * @return the application's exit code
     */
    @Override
    public int getExitCode() {
        return 0;
    }
}

