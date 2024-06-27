<#-- @ftlroot "../../../.." -->
<#include "/common/Copyright.ftl">
package ${RootController.packageName()};

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* The default implementation of this controller merely returns Http:200 responses to GET requests.
*/
@RestController
@RequestMapping("/")
public class ${RootController.className()} {

    /*
     * Constructor
     */
    public ${RootController.className()}() {
    }

    /*
     * The root path
     */
    @GetMapping (value= "", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> getHome() {
        return ResponseEntity.ok().build();
    }
}