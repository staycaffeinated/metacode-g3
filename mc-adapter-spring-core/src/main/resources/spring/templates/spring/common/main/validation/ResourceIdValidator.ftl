<#include "/common/Copyright.ftl">
package ${ResourceIdValidator.packageName()};

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ${SecureRandomSeries.fqcn()};

/**
 * This constraint verifies a String value is
 * consistent with the format of a resource identifier.
 * This constraint can be applied in POJOs or URL path variables
 * to help verify a field or parameter "looks like" a resource ID.
 */
@SuppressWarnings({"java:S125"})
// S125: We don't care if a comment happens to look like code
public class ResourceIdValidator implements ConstraintValidator<ResourceId, String> {
    /**
     * Determines whether {@code value} is a well-formed resource identifier.
     * A well-formed resource identifier is an alphanumeric string produced by
     * {@code ResourceIdGenerator::nextResourceId()}; the value:
     * <ul>
     * <li>Cannot be null</li>
     * <li>Must be exactly {@code ENTROPY_STRING_LENGTH} (27) characters long</li>
     * <li>Must consist only of alphanumeric characters (letters and digits)</li>
     * </ul>
     * <p>
     * This method can be accessed concurrently
     *
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValid(value);
    }

    public boolean isValid(String value) {
        return value != null
            && value.length() == ResourceIdGenerator.ENTROPY_STRING_LENGTH
            && value.chars().allMatch(Character::isLetterOrDigit);
    }
}
