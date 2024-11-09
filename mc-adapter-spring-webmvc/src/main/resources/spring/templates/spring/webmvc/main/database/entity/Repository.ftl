<#include "/common/Copyright.ftl">
package ${Repository.packageName()};

import ${CustomRepository.fqcn()};
import ${Entity.fqcn()};
import jakarta.persistence.QueryHint;
import java.util.List;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.QueryHints;

@SuppressWarnings({
    "java:S3252"    // allow static access to HINT_FETCH_SIZE
})
public interface ${Repository.className()} extends ${CustomRepository.className()}<${Entity.className()}, Long> {

    /*
     * If the underlying table has, say, hundreds of thousands of rows (or more), then fetching all
     * the rows will probably be detrimental to performance. More often than not, a reasonable
     * limit on the number of rows returned should be applied; thus the query hints. The limit
     * chosen here is arbitrary; adjust as desired.
     * <p>
     * To read more on this topic, see:
     *  https://vladmihalcea.com/spring-data-jpa-stream/
     *  https://vladmihalcea.com/whats-new-in-jpa-2-2-stream-the-result-of-a-query-execution/
     *  https://vladmihalcea.com/jpa-hibernate-query-hints/
     */
    @QueryHints(@QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "25"))
    @Override
    List<${Entity.className()}> findAll();

}

