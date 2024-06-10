
package ${Controller.packageName};

import ${Pojo.fqcn}; // domain.{endpoint.entityName}
import ${UnprocessableEntityException.fqcn};    // import UnprocessableEntityException
import ${OnCreate.fqcn}
import ${OnUpdate.fqcn}
import ${ResourceIdAnnotation.fqcn}
import ${SearchText.fqcn}

<#if endpoint.isWithOpenApi()>
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
</#if>
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.net.URI;

@RestController
@RequestMapping
@Slf4j
@Validated
public class ${Controller.className} {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    private final ${ServiceApi.className} ${ServiceApi.varName};

    /*
     * Constructor
     */
    public ${Controller.className} (${ServiceApi.className} ${ServiceApi.varName}) {
        this.${ServiceApi.varName} = ${ServiceApi.varName};
    }

    /*
     * Get all
     */
<#if endpoint.isWithOpenApi()>
    @Operation(summary = "Retrieve all ${Controller.dtoClassName}")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found all ${Controller.dtoClassName}")})
</#if>
    @GetMapping (value=${Routes.paths.findAll}, produces = MediaType.APPLICATION_JSON_VALUE )
// Controller:
//   methodNames:
//     getAll: getAll{{DtoName}}s
//   returnTypes:
//     getAll: List<{{DtoName}}>
    public ${Controller.returnTypes.getAll} ${Controller.methodNames.getAll}() {
        return ${ServiceApi.varName}.findAll${Controller.dtoClassName}s();
    }

    /*
     * Get one by resourceId
     *
     */
<#if endpoint.isWithOpenApi()>
    @Operation(summary = "Retrieve a single pet based on its public identifier")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found the pet", content = {
    @Content(mediaType = "application/json",
    schema = @Schema(implementation = ${Controller.dtoClassName}.class))}),
    @ApiResponse(responseCode = "400", description = "An invalid ID was supplied")})
</#if>
    @GetMapping(value=${Routes.paths.findOne}, produces = MediaType.APPLICATION_JSON_VALUE )
// Controller:
//   methodNames:
//     getOneById: get{{dtoName}}ById
//   returnTypes:
//     getOneById: ResponseEntity'<'{{PojoName>}}'>'
// ServiceApi:
//   methodNames:
//     findOneByResourceId: find{{PojoName}}ByResourceId
// Controller.methodNames.getOneById
// Controller.returnTypeOf.getOneById
    public ${Controller.returnTypes.getOneById} ${Controller.methodNames.getOneById}(@PathVariable @ResourceId String id) {
        return ${ServiceApi.varName}.${ServiceApi.methodNames.findOneByResourceId}(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
     * Create one
     */
<#if endpoint.isWithOpenApi()>
    @Operation(summary = "Create a new ${endpoint.entityName} and persist it")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Add a ${endpoint.entityName}",
    content = {
    @Content(mediaType = "application/json",
    schema = @Schema(implementation = ${endpoint.pojoName}.class))}),
    @ApiResponse(responseCode = "400", description = "An invalid ID was supplied")})
</#if>
    @PostMapping (value=${Routes.paths.createOne}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ${Controller.returnTypes.createOne} ${Controller.methodNames.createOne}(@RequestBody @Validated(OnCreate.class) ${Controller.pojoName} resource ) {
        try {
            ${ServiceApi.returnTypes.createOne} savedResource = ${ServiceApi.methodNames.createOne} ( resource );
            URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedResource.getResourceId())
                .toUri();
            return ResponseEntity.created(uri).body(savedResource);
        }
        // if, for example, a database constraint prevents writing the data...
        catch (org.springframework.transaction.TransactionSystemException e) {
            log.error(e.getMessage());
            throw new UnprocessableEntityException();
        }
    }

    /*
     * Update one
     */
<#if endpoint.isWithOpenApi()>
    @Operation(summary = "Update an existing ${endpoint.entityName}")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Updated the ${endpoint.entityName}"),
    @ApiResponse(responseCode = "400", description = "Incorrect data was submitted")})
</#if>
    @PutMapping(value=${endpoint.entityName}Routes.${endpoint.routeConstants.findOne}, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<${endpoint.pojoName}> update${endpoint.entityName}(@PathVariable @ResourceId String id, @RequestBody @Validated(OnUpdate.class) ${endpoint.pojoName} ${endpoint.entityVarName}) {
        if (!id.equals(${endpoint.entityVarName}.getResourceId())) {
            throw new UnprocessableEntityException("The identifier in the query string and request body do not match");
        }
        ${ServiceApi.returnTypes.updateOne} optional = ${ServiceApi.methodNames.updateOne}( ${endpoint.entityVarName} );
        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
     * Delete one
     */
<#if endpoint.isWithOpenApi()>
    @Operation(summary = "Delete an existing ${endpoint.entityName}")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Removed the ${endpoint.entityName}"),
    @ApiResponse(responseCode = "400", description = "An incorrect identifier was submitted")})
</#if>
    @DeleteMapping(value=${Routes.paths.findOne})
    public ${Controller.returnTypes.deleteOne} ${Controller.methodNames.deleteOne}(@PathVariable @ResourceId String id) {
        return ${ServiceApi.returnTypes.findOne}.${ServiceApi.methodNames.findOneByResourceId}(id)
            .map(${endpoint.entityVarName} -> {
                ${ServiceApi.varName}.${ServiceApi.methodNames.deleteOneByResourceId}(id);
                return ResponseEntity.ok(${endpoint.entityVarName});
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
     * Find by text
     */
<#if endpoint.isWithOpenApi()>
    @Operation(summary = "Search for ${endpoint.entityName}")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found matching entries")})
</#if>
    @GetMapping(value=${Routes.path.search}, produces = MediaType.APPLICATION_JSON_VALUE)
// Contoller:
//   returnTypes:
//     searchByText: PagedModel<EntityModel<{{endpoint.pojoName}}>>
//   methodNames:
//     searchByText: searchByText
    public ${Controller.returnTypes.searchByText} searchByText (
                @RequestParam(name="text", required = true) @SearchText Optional<String> text,
                @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE)
                @SortDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
                PagedResourcesAssembler<${endpoint.pojoName}> resourceAssembler)
    {
        return resourceAssembler.toModel( ${ServiceApi.varName}.${ServiceApi.methodNames.findByText}(text, pageable) );
    }
}