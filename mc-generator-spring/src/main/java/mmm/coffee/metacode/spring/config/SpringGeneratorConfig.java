package mmm.coffee.metacode.config;

import lombok.Getter;
import lombok.Setter;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class SpringGeneratorConfig {
    
    @Value("app.spring.templates.docker")
    private String dockerTemplates;

    @Value("app.spring.templates.gradle")
    private String gradleTemplates;

    @Value("app.spring.templates.lombok")
    private String lombokTemplates;

    @Value("app.spring.templates.spring-webmvc")
    private String springWebMvcTemplates;

    @Value("app.spring.templates.spring-common")
    private String springCommonTemplates;

    @Value("app.spring.dependency-file")
    private String dependencyFile;

    @Value("app.spring.templates.base-directory")
    private String templateDirectory;

    @Bean
    DependencyCatalog dependencyCatalog() {
        return new DependencyCatalog(dependencyFile);
    }

}
