/*
 * Copyright 2022 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.metacode.cli.commands.endpoint;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.cli.mixin.DryRunOption;
import mmm.coffee.metacode.cli.validation.ResourceNameValidator;
import mmm.coffee.metacode.cli.validation.ValidationTrait;
import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import mmm.coffee.metacode.common.generator.ICodeGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * Generates the boilerplate code for a REST endpoint
 */
@CommandLine.Command(
        name = "endpoint",
        mixinStandardHelpOptions = true,
        headerHeading = "%nSynopsis:%n%n",
        header = "Generate the boilerplate code for a REST endpoint",
        descriptionHeading = "%nDescription:%n%n",
        description = "Creates the basic code for a REST endpoint",
        synopsisHeading = "%nUsage:%n%n",
        optionListHeading = "%nOptions:%n%n"
)
@Slf4j
@SuppressWarnings("java:S125") // S125: allow comments that look like code
public class SubcommandCreateEndpoint implements Callable<Integer> {

    /**
     * Handle to the code generator
     */
    private final ICodeGenerator<RestEndpointDescriptor> codeGenerator;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec commandSpec;  // injected by picocli
    @CommandLine.Mixin
    DryRunOption dryRunOption;
    @CommandLine.Option(
            names = {"-e", "--resource"},
            description = "The resource (entity) available at this endpoint (e.g: --resource Coffee)",
            paramLabel = "ENTITY")
    String resourceName; // visible for testing
    @CommandLine.Option(
            names = {"-p", "--route"},
            description = "The route (path) to the resource (e.g: --route /coffee)",
            paramLabel = "ROUTE")
    String resourceRoute; // visible for testing

    @CommandLine.Option(
            names = {"-t", "--table-name"},
            description = "The table name assigned to the EJB class. Defaults to the resource name",
            paramLabel = "TABLE_NAME")
    String tableName; // visible for testing

    /**
     * Constructor
     *
     * @param codeGenerator use this to generate code
     */
    public SubcommandCreateEndpoint(@Qualifier("restEndpointGenerator") ICodeGenerator<RestEndpointDescriptor> codeGenerator) {
        this.codeGenerator = codeGenerator;
        log.info("Created SubcommandCreateEndpoint with codeGenerator {}", codeGenerator.getClass().getSimpleName());
    }

    /* This is never called directly */
    @Override
    public Integer call() {
        log.info("[call] Generating endpoint code for resource: {}", resourceName);
        log.info("[call] the cli tableName: {}", tableName);

        validateInputs();
        var spec = buildRestEndpointDescriptor();
        log.info("[call] tableName from CLI is: {}", spec.getTableName());
        return codeGenerator.doPreprocessing(spec).generateCode(spec);
    }

    private void validateInputs() {
        if (StringUtils.isNotEmpty(tableName)) {
            checkForValidTableName(tableName);
        }
        else {
            checkForValidTableName(resourceName);
        }
    }

    private void checkForValidTableName(String candidate) {
        ValidationTrait validator = ResourceNameValidator.of(candidate);
        if (validator.isInvalid()) {
            throw new CommandLine.ParameterException(commandSpec.commandLine(), validator.errorMessage());
        }
    }

    private RestEndpointDescriptor buildRestEndpointDescriptor() {
        return RestEndpointDescriptor.builder()
                .resource(this.resourceName)
                .route(this.resourceRoute)
                .tableName(this.tableName)
                .build();
    }
}
