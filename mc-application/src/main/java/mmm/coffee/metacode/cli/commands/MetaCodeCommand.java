package mmm.coffee.metacode.cli.commands;

import mmm.coffee.metacode.cli.ManifestVersionProvider;
import mmm.coffee.metacode.cli.commands.create.CreateCommand;
import mmm.coffee.metacode.cli.traits.CallTrait;
import org.springframework.stereotype.Component;
import picocli.AutoComplete;
import picocli.CommandLine;

@CommandLine.Command(
        name = "metacode",
        descriptionHeading = "%nDescription:%n",
        description = "Metacode is a code generator for Spring applications",
        mixinStandardHelpOptions = true,
        versionProvider = ManifestVersionProvider.class,
        commandListHeading = "%nCommands:%n",
        optionListHeading = "%nOptions:%n",
        subcommands = {
                AutoComplete.GenerateCompletion.class,
                CreateCommand.class
        }
)
@Component
public class MetaCodeCommand implements CallTrait {
}
