<#include "/common/Copyright.ftl">

package ${PojoToDocumentConverter.packageName()};

import ${Document.fqcn()};
import ${EntityResource.fqcn()};
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts POJOs into document objects
 */
@Component
public class ${PojoToDocumentConverter.className()} implements Converter<${EntityResource.className()}, ${Document.className()}> {
    /**
     * Convert the source object of type {@code ${endpoint.pojoName}} to target type {@code ${endpoint.documentName}}.
     *
     * @param resource the source object to convert, which must be an instance of {@code ${endpoint.pojoName}} (never {@code null})
     * @return the converted object, which must be an instance of {@code ${endpoint.documentName}} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public ${Document.className()} convert (@NonNull ${EntityResource.className()} resource) {
        ${Document.className()} target = new ${Document.className()}();
        target.setResourceId (resource.getResourceId());
        target.setText (resource.getText());
        return target;
    }

    /**
     * Convert a list of POJOs into EJBs
     */
    public List<${Document.className()}> convert (@NonNull List<${EntityResource.className()}> sourceList) {
        return sourceList.stream().map(this::convert).toList();
    }
}
