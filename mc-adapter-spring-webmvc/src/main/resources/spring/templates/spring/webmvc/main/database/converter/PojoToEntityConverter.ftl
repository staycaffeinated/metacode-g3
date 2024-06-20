<#include "/common/Copyright.ftl">

package ${PojoToEntityConverter.packageName()};

import ${EntityResource.fqcn()};
import ${Entity.fqcn()};
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* Converts Drink entity beans into DrinkResource objects
*/
@Component
public class ${PojoToEntityConverter.className()} implements Converter<${EntityResource.className()}, ${Entity.className()}> {
    /**
     * Convert the source object of type {@code ${endpoint.pojoName}} to target type {@code ${endpoint.ejbName}}.
     *
     * @param resource the source object to convert, which must be an instance of {@code ${endpoint.pojoName}} (never {@code null})
     * @return the converted object, which must be an instance of {@code ${endpoint.ejbName}} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public ${Entity.className()} convert (@NonNull ${EntityResource.className()} resource) {
        ${Entity.className()} target = new ${EntityResource.className()}();
        target.setResourceId (resource.getResourceId());
        target.setText (resource.getText());
        return target;
    }

    /**
     * Convert a list of POJOs into EJBs
     */
    public List<${Entity.className()}> convert (@NonNull List<${EntityResource.className()}> sourceList) {
        return sourceList.stream().map(this::convert).toList();
    }
}
