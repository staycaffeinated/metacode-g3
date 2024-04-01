package mmm.coffee.metacode.common.toml.functions;

/**
 * These are tokens the code looks for in the TOML file to
 * determine what's what.
 */
public class TomlKeywords {

    /**
     * This denotes the list of classes that belong to a package.
     * The TOML content will look something like:
     * <code>
     * [com.example.petstore.api] <== indicates the package name of the following classes
     * classes = Controller, Routes <== the 'classes' keyword and the identifiers of the classes
     * </code>
     * From this, the code generator can build these classes (for a Pet resource):
     * <code>
     *     com.example.petstore.api.PetController
     *     com.example.petstore.api.PetRoutes
     * </code>
     */
    public static final String CLASSES = ".classes";


    /**
     * This class is an enumeration of string constants.
     * There's no reason to create instances of this class.
     */
    private TomlKeywords() {}
}
