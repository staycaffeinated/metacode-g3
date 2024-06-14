package mmm.coffee.metacode.common.dictionary;

import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;

public interface IArchetypeDescriptorFactory {

    /*
     * Creates a descriptor for, usually, a project-scope class, such as
     * the main application, the global exception handler, exception classes.
     * These classes are independent of specific endpoints or "RESTful" resources.
     */
    JavaArchetypeDescriptor createArchetypeDescriptor(Archetype archetype);

    /*
     * Creates a descriptor for endpoint-scope class, such as controllers, repositories,
     * converters. These classes are specific to an endpoint or "RESTful" resource.
     */
    JavaArchetypeDescriptor createArchetypeDescriptor(Archetype archetype, String restfulResource);

}
