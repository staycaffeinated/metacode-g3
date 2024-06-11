/*
 * Copyright 2022 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.metacode.common.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import mmm.coffee.metacode.common.stereotype.MetaTemplateModel;
import mmm.coffee.metacode.common.stereotype.TemplateResolver;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles rendering templates
 */
@SuppressWarnings("java:S125")
// S125: allow comments that look like code
@Slf4j
public class FreemarkerTemplateResolver implements TemplateResolver<MetaTemplateModel> {

    private final Configuration configuration;

    /**
     * Constructor
     *
     * @param configuration the freemarker configuration
     */
    public FreemarkerTemplateResolver(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Render the template
     *
     * @param templateClassPath the path to the template file (a freemarker FTL file, for example)
     * @param dataModel         the data model used to resolve template variables
     * @return the rendered content of the template, as a String
     */
    @Override
    public String render(String templateClassPath, MetaTemplateModel dataModel) {
        try {
            // materialize the freemarker Template instance
            var template = configuration.getTemplate(templateClassPath, StandardCharsets.UTF_8.name());

            // The template model is passed into the process() method
            // via a Map. The map's key will be either 'project' or 'endpoint',
            // since those are the base names we used in the template variables.
            // For example, in the templates, we use expressions like '${project.applicationName}'
            // or '${endpoint.resourceName}', therefore, Freemarker will query
            // the Map for an entry with key 'project' or 'endpoint', and then use
            // _that_ object as a POJO to resolve the rest of the expression.
            // The POJO simply needs matching getter methods, like 'getApplicationName()'
            // or 'getResourceName()'.
            Map<String, Object> map = new HashMap<>();
            map.put(dataModel.getTopLevelVariable(), dataModel);

            // Add a top-level variable, YEAR, that has the current year.
            // This is used is the Copyright header, which gets used by
            // both the project and endpoint code generators
            map.put("YEAR", currentYear());

            // future self: do we need the lone archetypeDesc if we also have the map of all of them?
            ArchetypeDescriptor archetypeDescriptor = dataModel.getArchetypeDescriptor();
            if (archetypeDescriptor != null) {
                map.put(archetypeDescriptor.archetype().name(), archetypeDescriptor);
            }
            if (dataModel.getCustomProperties() != null) {
                for (Map.Entry<String,Object> entry : dataModel.getCustomProperties().entrySet()) {
                    map.put(entry.getKey(), entry.getValue());
                    log.trace("Added custom property for {} : {}", entry.getKey(), entry.getValue());
                }
            }


            // Parse and render the template
            var writer = new StringWriter();
            template.process(map, writer);
            return writer.toString();
        } catch (TemplateException | IOException e) {
            throw new RuntimeApplicationError(e.getMessage());
        }
    }

    private String currentYear() {
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }
}
