package mmm.coffee.metacode.cli.commands.create.project;


import mmm.coffee.metacode.cli.validation.PackageNameValidator;
import mmm.coffee.metacode.cli.validation.SpringIntegrationValidator;
import mmm.coffee.metacode.common.descriptor.Framework;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import mmm.coffee.metacode.common.generator.ICodeGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

/**
 * Command-line for creating a spring-boot project.
 * The CLI syntax goes like this:
 * ```create project spring-boot --name sample --package org.acme.sample` --base-path /sample```
 */
@CommandLine.Command(
        name = "spring-batch",
        mixinStandardHelpOptions = true,
        headerHeading = "%nSynopsis:%n",
        header = "  Generate a SpringBatch project",
        descriptionHeading = "%nDescription:%n",
        description = "  Creates a simple SpringBatch project",
        synopsisHeading = "%nUsage:%n",
        optionListHeading = "%nOptions:%n"
)
@Component
@SuppressWarnings({
        "java:S1854",   // allow unused vars for now
        "java:S1841",   // also allows unused vars
        "java:S125"     // allow comments that look like code
})
public class SubcommandCreateSpringBatchProject extends AbstractCreateSpringProject {
    /**
     * Handle to the code generator
     */
    private ICodeGenerator<RestProjectDescriptor> codeGenerator;

    /**
     * Construct instance with a given generator
     *
     * @param codeGenerator the code generator used by this command
     */
    public SubcommandCreateSpringBatchProject(@Qualifier("springBatchGenerator") ICodeGenerator<RestProjectDescriptor> codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    /**
     * Lifecycle for PicoCLI commands
     *
     * @return the exit code of the code generator
     */
    @Override
    public Integer call() {
        PackageNameValidator pnv = PackageNameValidator.of(packageName);
        SpringIntegrationValidator siv = SpringIntegrationValidator.of(features);
        super.runValidations(pnv, siv);
        var descriptor = buildProjectDescriptor(Framework.SPRING_BATCH);
        return codeGenerator.doPreprocessing(descriptor).generateCode(descriptor);
    }
}
