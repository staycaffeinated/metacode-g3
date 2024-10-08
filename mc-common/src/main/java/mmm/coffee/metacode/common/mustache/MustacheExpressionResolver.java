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
package mmm.coffee.metacode.common.mustache;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.MustacheException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Converts a mustache expressions into a resolved value.
 */
@Component
@Slf4j
public class MustacheExpressionResolver {

    private MustacheExpressionResolver() {
        // empty
    }

    /**
     * Resolves the {@code mustacheExpression} into a String.
     * For example, if
     * a) {@code mustacheExpression} is "{{basePackage}}"
     * b) {@code values} contains the k/v pair "basePackage=org.example.projectx"
     * then
     * toString returns "org.example.projectx".
     */
    public static String resolve(@NonNull String mustacheExpression, Map<String, String> values) {
        try {
            return Mustache.compiler().compile(mustacheExpression).execute(values);
        }
        catch (MustacheException e) {
            String msg = String.format("This expression could not be evaluated: %s. %s",  mustacheExpression, e.getMessage());
            log.warn(msg, e);
            throw new MustacheException(msg, e);
        }
    }
}
