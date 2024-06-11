package mmm.coffee.metacode.common.model;

public interface ArchetypeDescriptor {

    Archetype archetype();
    default String archetypeName() {
        if (archetype() == null) {
            return "Null Archetype";
        }
        return archetype().toString();
    }
}
