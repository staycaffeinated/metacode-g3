<#include "/common/Copyright.ftl">
package ${DocumentToPojoConverter.packageName()};

import ${EntityResource.fqcn()};
import ${Document.fqcn()};
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ${DocumentToPojoConverter.className()} implements Converter<${Document.className()}, ${EntityResource.className()}> {

    /**
     * Convert the source object of type {@code ${endpoint.documentName}} to target type {@code ${endpoint.pojoName}}.
     *
     * @param source the source object to convert, which must be an instance of {@code ${endpoint.documentName}} (never {@code null})
     * @return the converted object, which must be an instance of {@code ${endpoint.pojoName}} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public ${EntityResource.className()} convert(@NonNull ${Document.className()} source) {
        return ${EntityResource.className()}.builder()
            .resourceId( source.getResourceId() )
            .text( source.getText() )
            .build();
    }

    /**
     * Convert a list of EJBs into POJOs
     */
    public List<${EntityResource.className()}> convert (@NonNull List<${Document.className()}> sourceList) {
        return sourceList.stream().map(this::convert).toList();
    }
}