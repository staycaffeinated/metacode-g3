/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.project.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import mmm.coffee.metacode.annotations.jacoco.ExcludeFromJacocoGeneratedReport;
import mmm.coffee.metacode.common.stereotype.MetaTemplateModel;

import java.util.Map;


/**
 * SpringTemplateModel
 */
@SuppressWarnings({
        "java:S1170"    // We do not want to make 'application' a static field.
        // At some future point, we may allow users customize this, eg, to "app"
})
@SuperBuilder
@ExcludeFromJacocoGeneratedReport // exclude this class from code coverage reports
public abstract class SpringTemplateModel implements MetaTemplateModel {

    /*
     * This is the folder where the application module's code is dropped.
     * This style follows Gradle's idiomatic project structure and enables
     * a multi-module project. The project will look like, for example:
     * + petstore
     *   - build.gradle
     *   - settings.gradle
     *   + application
     *     - build.gradle
     *     - src/main/java
     *     - src/test/java
     */
    @Getter
    private final String appModule = "application";

    @Setter(AccessLevel.PUBLIC)
    private boolean isWebFlux;

    @Setter(AccessLevel.PUBLIC)
    private boolean isWebMvc;

    @Setter(AccessLevel.PUBLIC)
    private boolean isSpringBatch;

    @Setter(AccessLevel.PUBLIC)
    private boolean isSpringBoot;

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.NONE)       // we provide a custom getter
    private String framework;       // This is a String because the templates expect a string
    // Future task: refactor to use the Enum here, and have
    // templates use 'framework.isWebFlux', 'framework.isWebMvc'

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    Map<String, Object> customProperties;

    /**
     * Returns {@code true} if {@code framework} is spring-webflux
     */
    public final boolean isWebFlux() {
        return isWebFlux;
    }

    /**
     * Returns {@code true} if {@code framework} is spring-webmvc
     */
    public final boolean isWebMvc() {
        return isWebMvc;
    }

}
