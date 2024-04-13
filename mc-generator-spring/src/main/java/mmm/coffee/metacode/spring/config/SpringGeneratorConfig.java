package mmm.coffee.metacode.spring.config;

import lombok.Getter;
import lombok.Setter;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.common.freemarker.ConfigurationFactory;
import mmm.coffee.metacode.common.freemarker.FreemarkerTemplateResolver;
import mmm.coffee.metacode.common.stereotype.MetaTemplateModel;
import mmm.coffee.metacode.common.stereotype.TemplateResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class SpringGeneratorConfig {

    @Value("${app.spring.templates.docker}")
    private String dockerTemplates;

    @Value("${app.spring.templates.gradle}")
    private String gradleTemplates;

    @Value("${app.spring.templates.lombok}")
    private String lombokTemplates;

    @Value("${app.spring.templates.spring-webmvc}")
    private String springWebMvcTemplates;

    @Value("${app.spring.templates.spring-common}")
    private String springCommonTemplates;

    @Value("${app.spring.dependency-file}")
    private String dependencyFile;

    @Value("${app.spring.templates.base-directory}")
    private String templateDirectory;

    @Bean
    public DependencyCatalog dependencyCatalog() {
        return new DependencyCatalog(dependencyFile);
    }

    @Bean
    public TemplateResolver<MetaTemplateModel> templateResolver() {
        return new FreemarkerTemplateResolver(ConfigurationFactory.defaultConfiguration(templateDirectory));
    }

}
