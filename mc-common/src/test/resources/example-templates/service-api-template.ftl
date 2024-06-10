
package ${ServiceApi.fqcn};

import ${Pojo.fqcn};
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ${ServiceApi.className} {
    /*
     * findAll
     */
    ${ServiceApi.returnTypes.findAll} ${ServiceApi.methodNames.findAll}()

    /**
     * findByResourceId
     */
    ${ServiceApi.returnTypes.findOneByResourceId} ${ServiceApi.methodNames.findOneByResourceId}(String id);

    /*
     * findByText
     */
    ${ServiceApi.returnTypes.findByText} ${ServiceApi.methodNames.findByText}(Optional<String> text, Pageable pageable);

    /**
     * Persists a new resource
     */
    ${ServiceApi.returnTypes.createOne} ${ServiceApi.methodNames.createOne}(${Pojo.className} resource);

    /**
     * Updates an existing resource
     */
    ${ServiceApi.returnTypes.updateOne} ${ServiceApi.methodNames.updateOne}(${Pojo.className} resource);

    /**
     * delete
     */
    ${ServiceApi.returnTypes.deleteOneByResourceId} ${ServiceApi.methodNames.deleteOneByResourceId}(String resourceId);
}
