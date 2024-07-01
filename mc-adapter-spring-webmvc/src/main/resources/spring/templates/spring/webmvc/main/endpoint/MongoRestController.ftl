<#include "/common/Copyright.ftl">
package ${Controller.packageName()};

import ${EntityResource.fqcn()};
import ${UnprocessableEntityException.fqcn()};
import ${OnCreateAnnotation.fqcn()};
import ${OnUpdateAnnotation.fqcn()};
import ${ResourceIdAnnotation.fqcn()};
import ${SearchTextAnnotation.fqcn()};

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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
    @Autowired
    public ${endpoint.entityName}Controller(${ServiceApi.className()} service) {
        this.${ServiceApi.varName()} = service;
    }

    /*
     * Get all
     */
    @GetMapping (value=${Routes.className()}.${endpoint.routeConstants.findAll}, produces = MediaType.APPLICATION_JSON_VALUE )
    public List<${EntityResource.className()}> getAll${endpoint.entityName}s() {
        return ${ServiceApi.varName()}.findAll${endpoint.entityName}s();
    }

    /*
     * Get one by resourceId
     *
     */
    @GetMapping(value=${Routes.className()}.${endpoint.routeConstants.findOne}, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<${EntityResource.className()}> get${endpoint.entityName}ById(@PathVariable @ResourceId String id) {
        return ${ServiceApi.varName()}.find${endpoint.entityName}ByResourceId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
     * Create one
     */
    @PostMapping (value=${Routes.className()}.${endpoint.routeConstants.create}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<${EntityResource.className()}> create${endpoint.entityName}(@RequestBody @Validated(OnCreate.class) ${endpoint.pojoName} resource ) {
        try {
            ${endpoint.pojoName} savedResource = ${ServiceApi.varName()}.create${endpoint.entityName} ( resource );
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
    @PutMapping(value=${Routes.className()}.${endpoint.routeConstants.findOne}, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<${EntityResource.className()}>> update${endpoint.entityName}(@PathVariable @ResourceId String id, @RequestBody @Validated(OnUpdate.class) ${endpoint.pojoName} ${endpoint.entityVarName}) {
        if (!id.equals(${endpoint.entityVarName}.getResourceId())) {
            throw new UnprocessableEntityException("The identifier in the query string and request body do not match");
        }
        List<${EntityResource.className()}> rs = ${ServiceApi.varName()}.update${endpoint.entityName}(${endpoint.entityVarName});
        if (rs.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(rs);
    }

    /*
     * Delete one
     */
    @DeleteMapping(value=${Routes.className()}.${endpoint.routeConstants.findOne})
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
    @GetMapping(value=${Routes.className()}.${endpoint.routeConstants.search}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<${EntityResource.className()}> searchByText (
        @RequestParam(name="text", required = true) @SearchText Optional<String> text,
        @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE)
        @SortDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable)
    {
        return ${ServiceApi.varName()}.findByText(text.orElse(""), pageable);
    }
}
