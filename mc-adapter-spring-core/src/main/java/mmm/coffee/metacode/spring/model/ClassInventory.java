package mmm.coffee.metacode.spring.model;

import lombok.Getter;
import lombok.Setter;

/**
 * A collection of all the prototype classes referenced
 * during code generation.
 */
@Getter
@Setter
public class ClassInventory {
    private ClassInfo serviceApi;
    private ClassInfo serviceImpl;
    private ClassInfo controller;
    private ClassInfo routes;
    private ClassInfo resourcePojo;
    private ClassInfo dataStoreApi;
    private ClassInfo dataStoreImpl;
    private ClassInfo entity;
    private ClassInfo repository;
    private ClassInfo entityToPojoConverter;
    private ClassInfo pojoToEntityConverter;
    private ClassInfo globalExceptionHandler;
    private ClassInfo applicationConfiguration;
    private ClassInfo dateTimeFormatConfiguration;
    private ClassInfo problemConfiguration;
    private ClassInfo localDateConverter;
    private ClassInfo webMvcConfiguration;
    private ClassInfo springProfiles;
    private ClassInfo resourceIdTrait;
    private ClassInfo secureRandomSeries;
    private ClassInfo badRequestException;
    private ClassInfo resourceNotFoundException;
    private ClassInfo unprocessableEntityException;
    private ClassInfo customRepository;
    private ClassInfo genericDataStore;
    private ClassInfo alphabeticAnnotation;
    private ClassInfo resourceIdAnnotation;
    private ClassInfo alphabeticValidator;
    private ClassInfo resourceIdValidator;

}
