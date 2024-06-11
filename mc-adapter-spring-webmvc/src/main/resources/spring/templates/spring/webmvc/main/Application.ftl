<#include "/common/Copyright.ftl">
package ${project.basePackage};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

<#if Application??>
/*
 * fqcn: ${Application.fqcn()}
 * package: ${Application.packageName()}
 * className: ${Application.className()}
 */
</#if>

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
