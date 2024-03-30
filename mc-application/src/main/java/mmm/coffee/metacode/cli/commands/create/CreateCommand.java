package mmm.coffee.metacode.cli.commands.create;

import mmm.coffee.metacode.cli.commands.create.project.SubcommandCreateProject;
import mmm.coffee.metacode.cli.traits.CallTrait;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

/**
 * The entry point of the CLI's `create` command
 */
@CommandLine.Command(
        name="create",
        descriptionHeading = "%nDescription:%n",
        description="Create a new project or endpoint artifacts by way of `create project` or `create endpoint`",
        mixinStandardHelpOptions = true,
        commandListHeading = "%nCommands:%n",
        optionListHeading = "%nOptions:%n",
        subcommands = {SubcommandCreateProject.class}
)
@Component
@SuppressWarnings("java:S1135") // ignore to-do tasks for now
public class CreateCommand implements CallTrait {
}
