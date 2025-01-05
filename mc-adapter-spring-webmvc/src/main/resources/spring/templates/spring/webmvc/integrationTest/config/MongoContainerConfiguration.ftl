<#include "/common/Copyright.ftl">
package ${ContainerConfiguration.packageName()};

import java.time.Duration;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
<#if project.isWithTestContainers()>
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
</#if>

@TestConfiguration(proxyBeanMethods=false)
public class ContainerConfiguration {

private static final String IMAGE = "mongo:6.0.4";

<#if project.isWithTestContainers() && project.isWithMongoDb()>
    // @formatter:off
    // Note: the container is started as a singleton instead of using the @Container
    // annotation. When @Container is applied, multiple containers may get started.
    // When multiple containers are started, tests will hang from socket timeouts.
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(IMAGE)
        .withReuse(true)
        .withStartupTimeout(Duration.ofMinutes(1))
        .waitingFor(Wait.forListeningPort());
    // @formatter:on
    static {
        mongoDBContainer.start();
    }

    @Bean
    @ServiceConnection
    @RestartScope
    public MongoDBContainer mongoDBContainer() {
        return mongoDBContainer;
    }
</#if>

    public static Object getReplicaSetUrl() {
    <#if ((project.isWithMongoDb()) && project.isWithTestContainers())>
        return mongoDBContainer.getReplicaSetUrl();
    <#else>
        return "mongodb://localhost:27017/testdb";
    </#if>
    }

}