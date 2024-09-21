<#include "/common/Copyright.ftl">
package ${Controller.packageName()};

import ${EntityResource.fqcn()};
import ${UnprocessableEntityException.fqcn()};
import ${OnCreateAnnotation.fqcn()};
import ${OnUpdateAnnotation.fqcn()};
import ${ResourceIdTrait.fqcn()};
import ${SearchTextAnnotation.fqcn()};
import ${ResourceIdAnnotation.fqcn()};
import ${Routes.fqcn()};

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
public class ${Controller.className()} {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    private final ${ServiceApi.className()} ${ServiceApi.varName()};

    /*
     * Constructor
     */
    public ${Controller.className()}(${ServiceApi.className()} ${ServiceApi.varName()}) {
        this.${ServiceApi.varName()} = ${ServiceApi.varName()};
    }

    /*
     * Get all
     */
    <#if endpoint.isWithOpenApi()>
    @Operation(summary = "Retrieve all ${endpoint.entityName}")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found all ${endpoint.entityName}")})
    </#if>
    @GetMapping (value=${Routes.className()}.${endpoint.routeConstants.findAll}, produces = MediaType.APPLICATION_JSON_VALUE )
    public List<${EntityResource.className()}> getAll${endpoint.entityName}s() {
        return ${ServiceApi.varName()}.findAll${endpoint.entityName}s();
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
    schema = @Schema(implementation = ${endpoint.pojoName}.class))}),
    @ApiResponse(responseCode = "400", description = "An invalid ID was supplied")})
    </#if>
    @GetMapping(value=${Routes.className()}.${endpoint.routeConstants.findOne}, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<${EntityResource.className()}> get${endpoint.entityName}ById(@PathVariable @ResourceId String id) {
        return ${ServiceApi.varName()}.find${endpoint.entityName}ByResourceId(id)
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
    @PostMapping (value=${Routes.className()}.${endpoint.routeConstants.create}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<${EntityResource.className()}> create${endpoint.entityName}(@RequestBody @Validated(OnCreate.class) ${endpoint.pojoName} resource ) {
        try {
            ${EntityResource.className()} savedResource = ${ServiceApi.varName()}.create${endpoint.entityName} ( resource );
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
    @PutMapping(value=${Routes.className()}.${endpoint.routeConstants.update}, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<${EntityResource.className()}> update${endpoint.entityName}(@PathVariable @ResourceId String id, @RequestBody @Validated(OnUpdate.class) ${EntityResource.className()} ${EntityResource.varName()}) {
        if (!id.equals(${EntityResource.varName()}.getResourceId())) {
            throw new UnprocessableEntityException("The identifier in the query string and request body do not match");
        }
        Optional<${EntityResource.className()}> optional = ${ServiceApi.varName()}.update${endpoint.entityName}( ${EntityResource.varName()} );
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
    @DeleteMapping(value=${Routes.className()}.${endpoint.routeConstants.delete})
    public ResponseEntity<${EntityResource.className()}> delete${endpoint.entityName}(@PathVariable @ResourceId String id) {
        return ${ServiceApi.varName()}.find${endpoint.entityName}ByResourceId(id)
            .map(${endpoint.entityVarName} -> {
                ${ServiceApi.varName()}.delete${endpoint.entityName}ByResourceId(id);
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
    @GetMapping(value=${Routes.className()}.${endpoint.routeConstants.search}, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<EntityModel<${endpoint.pojoName}>> searchByText (
            @RequestParam(name="text", required = true) @SearchText Optional<String> text,
            @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE)
            @SortDefault(sort = ${EntityResource.className()}.Fields.TEXT, direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<${endpoint.pojoName}> resourceAssembler)
    {
        return resourceAssembler.toModel( ${ServiceApi.varName()}.findByText(text.orElse(""), pageable) );
    }

    /*
     * Search using an RSQL query
     */
    <#if endpoint.isWithOpenApi()>
    @Operation(summary = "Search for ${endpoint.entityName} using an RSQL query")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found matching entries")})
    </#if>
    @GetMapping(value=${Routes.className()}.${endpoint.routeConstants.query}, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<EntityModel<${endpoint.pojoName}>> searchByQuery (
    @RequestParam(name="q", required = true) @SearchText Optional<String> rsqlQuery,
        @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE)
        @SortDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
        PagedResourcesAssembler<${endpoint.pojoName}> resourceAssembler)
    {
        return resourceAssembler.toModel( ${ServiceApi.varName()}.search(rsqlQuery.orElse(""), pageable) );
    }

}