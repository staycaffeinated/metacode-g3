<#include "/common/Copyright.ftl">
package ${SearchTextValidator.packageName()};

import lombok.NonNull;
import org.springframework.util.ObjectUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Verifies that a search-by-text parameter only contains valid characters.
 * The OWASP Validation Regex Repository has useful expressions:
 * <ul>
 * <li><a href="https://owasp.org/www-community/OWASP_Validation_Regex_Repository">OWASP Regex Repository</a></li>
 * </ul>
 * Other references:
 * <ul>
 * <li><a href="https://regexlib.com/DisplayPatterns.aspx">Display Patterns</a></li>
 * <li><a href="https://cheatography.com/davechild/cheat-sheets/regular-expressions/">Regex Cheat Sheet</a></li>
 * </ul>
 */
public class SearchTextValidator implements ConstraintValidator<SearchText, String> {

    // These value constraints are arbitrary, since we have to start somewhere.
    // These should be adjusted to something that makes sense to your use cases.
    // Don't forget to account for URL-encoded values.
    private static final String REGEX = "(?s).*";   // allow anything.
    private static final int MAXLENGTH = 24;
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public boolean isValid(@NonNull String value, ConstraintValidatorContext context) {
        // when empty, then the content of the text is irrelevant to the search filter
        if (ObjectUtils.isEmpty(value)) return true;

        // don't allow unlimited length; pick a limit to the length
        if (value.length() > MAXLENGTH) return false;

        // check against the allowed characters
        return PATTERN.matcher(value).find();
    }
}
