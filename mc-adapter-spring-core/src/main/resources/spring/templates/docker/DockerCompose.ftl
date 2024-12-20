version: '3.8'

services:
    application:
        container_name: "${project.applicationName}_api"
        image: "${project.applicationName}:latest"
        ports:
            - "8080:8080"
<#if (project.isWithPostgres())>
        depends_on:
            - postgres
        environment:
            # The "hostname", postgres, must match the service name.
            # Thus, if you change the "postgres" service name to something else,
            # remember to update the hostname portion of this URL.
            # Postgres's default database name is also "postgres". If your application
            # has its own database, remember to change this URL to reflect that.
<#if (project.schema)??>
            - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/postgres?currentSchema=${project.schema}
<#else>
            - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/postgres
</#if>
            - SPRING_DATASOURCE_USERNAME=postgres
            - SPRING_DATASOURCE_PASSWORD=postgres


    postgres:
        image: 'postgres:latest'
        container_name: "${project.applicationName}_dbms"
        environment:
            - POSTGRES_USER="postgres"
            - POSTGRES_PASSWORD="postgres"
        healthcheck:
            test: [ "CMD", "pg_isready" ]
            interval: 5s
        timeout: 3s
        retries: 30
</#if>
