<#include "/common/Copyright.ftl">
package ${Controller.packageName()};

import ${EntityResource.fqcn()};
import ${UnprocessableEntityException.fqcn()};
import ${OnCreateAnnotation.fqcn()};
import ${OnUpdateAnnotation.fqcn()};
import ${ResourceIdAnnotation.fqcn()};
import ${SearchTextAnnotation.fqcn()};
import ${ServiceApi.fqcn()};
import ${EntityResponse.fqcn()};

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    public ${endpoint.entityName}Controller(${ServiceApi.className()} service) {
        this.${ServiceApi.varName()} = service;
    }


    @GetMapping (value=${Routes.className()}.${endpoint.routeConstants.findAll}, produces = MediaType.APPLICATION_JSON_VALUE )
    public List<${EntityResponse.className()}> getAll${endpoint.entityName}s() {
        return ${ServiceApi.varName()}.findAll${endpoint.entityName}s().stream().map(${EntityResponse.className()}::fromDomain).toList();
    }


    @GetMapping(value=${Routes.className()}.${endpoint.routeConstants.findOne}, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<${EntityResponse.className()}> get${endpoint.entityName}ById(@PathVariable @ResourceId String id) {
        return ${ServiceApi.varName()}.find${endpoint.entityName}ByResourceId(id)
            .map(${EntityResponse.className()}::fromDomain)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping (value=${Routes.className()}.${endpoint.routeConstants.create}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<${EntityResponse.className()}> create${endpoint.entityName}(@RequestBody @Validated(${OnCreateAnnotation.className()}.class) ${EntityRequest.className()} request ) {
        ${endpoint.pojoName} savedResource = ${ServiceApi.varName()}.create${endpoint.entityName} ( request.toDomain() );
        URI uri = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedResource.getResourceId())
                        .toUri();
        return ResponseEntity.created(uri).body(${EntityResponse.className()}.fromDomain(savedResource));
    }


    @PutMapping(value=${Routes.className()}.${endpoint.routeConstants.update}, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<${EntityResponse.className()}>> update${endpoint.entityName}(@PathVariable @ResourceId String id, @RequestBody @Validated(${OnUpdateAnnotation.className()}.class) ${EntityRequest.className()} request) {
        if (!id.equals(request.resourceId())) {
            throw new UnprocessableEntityException("The identifier in the query string and request body do not match");
        }
        List<${EntityResource.className()}> rs = ${ServiceApi.varName()}.update${endpoint.entityName}(request.toDomain());
        if (rs.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(rs.stream().map(${EntityResponse.className()}::fromDomain).toList());
    }


    @DeleteMapping(value=${Routes.className()}.${endpoint.routeConstants.delete})
    public ResponseEntity<${EntityResponse.className()}> delete${endpoint.entityName}(@PathVariable @ResourceId String id) {
        return ${ServiceApi.varName()}.find${endpoint.entityName}ByResourceId(id)
                .map(${endpoint.entityVarName} -> {
                    ${ServiceApi.varName()}.delete${endpoint.entityName}ByResourceId(id);
                    return ResponseEntity.ok(${EntityResponse.className()}.fromDomain(${endpoint.entityVarName}));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value=${Routes.className()}.${endpoint.routeConstants.search}, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<EntityModel<${EntityResponse.className()}>> searchByText (
        @RequestParam(name="text", required = true)  Optional< @SearchText String> text,
        @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE)
        @SortDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
        PagedResourcesAssembler<${EntityResponse.className()}> resourceAssembler)
    {
        Page<${EntityResource.className()}> result = ${ServiceApi.varName()}.findByText(text.orElse(""), pageable);
        return resourceAssembler.toModel(result.map(${EntityResponse.className()}::fromDomain));
    }
}
