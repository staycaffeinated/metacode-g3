/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.endpoint.converter;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import mmm.coffee.metacode.common.trait.ConvertTrait;
import mmm.coffee.metacode.spring.converter.NameConverter;
import mmm.coffee.metacode.spring.converter.RouteConstantsConverter;
import mmm.coffee.metacode.spring.endpoint.model.RestEndpointTemplateModel;
import mmm.coffee.metacode.spring.endpoint.model.RouteConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * RestEndpointDescriptorToTemplateModelConverter
 */
@Builder
@RequiredArgsConstructor
public class RestEndpointDescriptorToTemplateModelConverter implements ConvertTrait<RestEndpointDescriptor, RestEndpointTemplateModel> {

    final NameConverter nameConverter;
    final RouteConstantsConverter resourceConstantsConverter;

    /**
     * Converts an instance of class {@code FROM} into an instance of class {@code TO}.
     * We let the implementer decide how to handle {@code nulls}
     *
     * @param fromType some instance to convert
     * @return the transformed object
     */
    @Override
    public RestEndpointTemplateModel convert(RestEndpointDescriptor fromType) {
        final String resourceName = fromType.getResource();
        final String packageName = buildPackageName(fromType);
        final String packagePath = nameConverter.packageNameToPath(packageName);
        final String schema = fromType.getSchema();

        resourceConstantsConverter.setResourceName(resourceName);
        // See the RouteConstants class for an explanation of why this is done
        var constants = RouteConstants.builder()
                .create(resourceConstantsConverter.create())
                .delete(resourceConstantsConverter.delete())
                .basePath(resourceConstantsConverter.basePath())
                .events(resourceConstantsConverter.events())
                .findAll(resourceConstantsConverter.findAll())
                .findOne(resourceConstantsConverter.findOne())
                .idParameter(resourceConstantsConverter.idParameter())
                .findByProperty(resourceConstantsConverter.findByProperty())
                .search(resourceConstantsConverter.search())
                .stream(resourceConstantsConverter.stream())
                .update(resourceConstantsConverter.update())
                .build();

        return RestEndpointTemplateModel.builder()
                .basePackage(fromType.getBasePackage())
                .basePackagePath(nameConverter.packageNameToPath(fromType.getBasePackage()))
                .basePath(fromType.getBasePath())
                .documentName(nameConverter.toDocumentClassName(resourceName))
                .ejbName(nameConverter.toEjbClassName(resourceName))
                .entityName(nameConverter.toEntityName(resourceName))
                .entityVarName(nameConverter.toEntityVariableName(resourceName))
                .framework(fromType.getFramework())
                .lowerCaseEntityName(nameConverter.toLowerCaseEntityName(resourceName))
                .packageName(packageName)
                .packagePath(packagePath)
                .pojoName(nameConverter.toPojoClassName(resourceName))
                .resource(fromType.getResource())
                // ensure route begins with forward slash
                .route(nameConverter.toBasePathUrl(fromType.getRoute()))
                .schema(schema)
                .tableName(determineTableName(fromType))
                .isWebFlux(fromType.isWebFlux())
                .isWebMvc(fromType.isWebMvc())
                .withPostgres(fromType.isWithPostgres())
                .withTestContainers(fromType.isWithTestContainers())
                .withLiquibase(fromType.isWithLiquibase())
                .withOpenApi(fromType.isWithOpenApi())
                .routeConstants(constants)
                .build();
    }

    private String determineTableName(RestEndpointDescriptor fromType) {
        if (StringUtils.isNotEmpty(fromType.getTableName())) {
            return nameConverter.toTableName(fromType.getTableName(), !fromType.isWithPostgres());
        }
        else return nameConverter.toTableName(fromType.getResource(), !fromType.isWithPostgres());
    }

    /**
     * Returns the package name into which classes (and sub-packages) specific to this endpoint
     * are placed. For better cohesion of the classes, all classes that support and endpoint,
     * such as the Controller, Repository, Service classes, are all placed in a package named
     * something like: '{{basePackage}}.endpoint.pet' or '{{basePackage}}.endpoint.owner'.
     *
     * @param descriptor contains the command-line values entered by the end-user
     * @return the package name for this endpoint's classes and sub-packages.
     */
    private String buildPackageName(RestEndpointDescriptor descriptor) {
        final String resourceName = descriptor.getResource();
        final String lowerCaseName = nameConverter.toLowerCaseEntityName(resourceName);

        return descriptor.getBasePackage() + ".endpoint." + lowerCaseName;
    }
}
