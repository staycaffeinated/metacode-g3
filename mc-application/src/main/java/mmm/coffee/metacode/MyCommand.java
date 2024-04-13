package mmm.coffee.metacode;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Starting point of the command line
 */
@Component
@CommandLine.Command(name = "mycommand", mixinStandardHelpOptions = true)
public class MyCommand implements Callable<Integer> {
    @CommandLine.Parameters(description = "positional params")
    List<String> positionals = new ArrayList<>();
    @CommandLine.Option(names = "-x", description = "optional arg")
    private String x;

    String getX() {
        return x;
    }

    @Override
    public Integer call() throws Exception {
        System.out.printf("My subcommnad was called with option -x=%s\n", x);
        return 23;
    }
}
