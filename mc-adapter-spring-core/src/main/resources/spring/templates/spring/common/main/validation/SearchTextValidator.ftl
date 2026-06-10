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

    // This value constraint is arbitrary, but we have to start somewhere.
    // These should be adjusted to something that makes sense to your use cases.
    // Remember to account for URL-encoded values and unprintable characters.
    private static final int MAXLENGTH = 24;

    @Override
    public boolean isValid(@NonNull String value, ConstraintValidatorContext context) {
        // when empty, then the content of the text is irrelevant to the search filter
        if (ObjectUtils.isEmpty(value)) return true;

        return value.length() <= MAXLENGTH;
    }
}
