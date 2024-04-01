package mmm.coffee.metacode.common.toml.templates;

import java.nio.file.Path;

/**
 * Provides information about a template, such as the
 * source file of the template
 */
public interface TemplateResolver {
    Path templateSource(String classKey);
    Path destinationPath(String classKey);
}
