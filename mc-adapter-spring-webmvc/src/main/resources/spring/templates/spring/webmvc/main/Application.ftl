<#include "/common/Copyright.ftl">
package ${project.basePackage};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

<#if Application??>
/*
 * fqcn: ${Application.fqcn()}
 * package: ${Application.packageName()}
 * className: ${Application.className()}
 * varName: ${Application.varName()}
 */
@SpringBootApplication
public class ${Application.className()} {
    public static void main(String[] args) {
        SpringApplication.run(${Application.className()}.class, args);
    }
}
<#else>
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
</#if>