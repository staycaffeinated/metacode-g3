package mmm.coffee.metacode.cli.commands;

import mmm.coffee.metacode.cli.commands.create.CreateCommand;
import mmm.coffee.metacode.cli.traits.CallTrait;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@CommandLine.Command(
        name="metacode",
        descriptionHeading = "%nDescription:%n",
        description="A code generator for Spring projects",
        mixinStandardHelpOptions = true,
        commandListHeading = "%nCommands:%n",
        optionListHeading = "%nOptions:%n",
        subcommands = {CreateCommand.class}
)
@Component
public class MetaCodeCommand implements CallTrait {
}
