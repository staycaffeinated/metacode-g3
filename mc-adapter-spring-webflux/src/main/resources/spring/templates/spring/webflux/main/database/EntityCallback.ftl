<#include "/common/Copyright.ftl">

package ${EntityCallback.packageName()};

import ${Entity.fqcn()};
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ${EntityCallback.className()} implements BeforeSaveCallback<${Entity.className()}> {
    @Override
    public Publisher<${Entity.className()}> onBeforeSave(${Entity.className()} entity, OutboundRow row, SqlIdentifier table) {
        if (entity.getResourceId() == null || entity.getResourceId().isEmpty()) {
            entity.beforeInsert();
        }
        return Mono.just(entity);
    }
}