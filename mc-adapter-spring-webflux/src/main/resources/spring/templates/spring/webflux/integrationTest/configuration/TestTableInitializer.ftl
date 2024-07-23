<#include "/common/Copyright.ftl">

package ${TestTableInitializer.packageName()};

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 * Initialize the ${endpoint.entityName} table within the testcontainer database.
 *
 * This class is named as-is to avoid a conflict with the ${endpoint.entityName}TableInitializer
 * class found under the 'src/main' source directory.
 */
@Component
public class ${TestTableInitializer.className()} {
    @Bean
    public ConnectionFactoryInitializer reportConnectionFactoryInitializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        /*
         * R2DBC does not auto-create the database tables so this script is run to do that
         */
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("database/${endpoint.lowerCaseEntityName}-schema.sql")));
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
