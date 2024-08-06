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
package mmm.coffee.metacode.cli.validation;

import java.util.StringTokenizer;

/**
 * Validation of packageName to ensure it's a legal Java package name
 * <p>
 * A review of the rules for package names
 * (from  https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html )
 * <p>
 * 1. Package names are written in all lower case
 * 2. Cannot contain a hyphen or other special character (underscore is allowed)
 * 3. Cannot contain a reserved word
 */
public class PackageNameValidator implements ValidationTrait {

    private final String value;
    private String errorMessage;

    private boolean evaluated;

    // Only subclasses need to create instances
    protected PackageNameValidator(String value) {
        this.value = value;
    }

    public static PackageNameValidator of(String value) {
        return new PackageNameValidator(value);
    }

    public boolean isValid() {
        return evaluate(this.value);
    }

    public boolean isInvalid() {
        return !isValid();
    }

    @Override
    public String errorMessage() {
        if (!evaluated) evaluate(this.value);
        return errorMessage;
    }

    /**
     * Checks whether {@code value} represents a valid Java package name
     *
     * @param value the String to check
     * @return true if {@code value} is a valid Java package name
     */
    public final boolean evaluate(String value) {
        evaluated = true;
        if (value == null || value.isBlank()) {
            errorMessage = "A Java package name cannot be null nor an empty string.";
            return false;
        }
        return check(value);
    }

    /**
     * Checks whether the given candidate is a valid Java package name.
     * We don't guarantee that pathological cases will be detected.
     *
     * @param candidate the candidate value
     * @return if it can be used as a package name
     */
    protected final boolean check(String candidate) {
        // Edge case: don't want a package name starting with '.'
        if (value.charAt(0) == '.') {
            errorMessage = "A Java package name cannot begin with the '.' character";
            return false;
        }

        var tokenizer = new StringTokenizer(candidate, ".");

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (ReservedWords.isReservedWord(token)) {
                errorMessage = String.format("Your Java package name contains the reserved word, '%s', which will cause compile errors.", token);
                return false;
            }
            if (!Characters.isLegalIdentifier((token))) {
                errorMessage = String.format("Your Java package name contains the illegal identifier, '%s', which will cause compile errors", token);
                return false;
            }
        }
        return true;
    }
}
