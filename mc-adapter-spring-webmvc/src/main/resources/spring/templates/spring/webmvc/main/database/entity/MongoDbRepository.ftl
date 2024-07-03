<#include "/common/Copyright.ftl">
package ${Repository.packageName()};

import ${Document.fqcn()};
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
public interface ${Repository.className()} extends MongoRepository<${Document.className()}, String>,
                                                   PagingAndSortingRepository<${Document.className()}, String> {

    @Query("{resourceId:?}")
    Optional<${Document.className()}> findByResourceId(String resourceId);

    @Query("{text:?}")
    List<${Document.className()}> findByText(String text);

    Page<${Document.className()}> findByTextContainingIgnoreCase(String title, Pageable pageable);
}
