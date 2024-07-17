<#include "/common/Copyright.ftl">
package ${ResourceIdentity.packageName()};

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A formalized wrapper for a resource identifier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ${ResourceIdentity.className()} {
    @NonNull
    private String resourceId;
}