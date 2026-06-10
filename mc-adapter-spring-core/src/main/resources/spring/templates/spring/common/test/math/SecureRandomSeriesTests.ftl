<#include "/common/Copyright.ftl">
package ${SecureRandomSeries.packageName()};

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
* Unit tests of the ${SecureRandomSeries.className()} class
*/
@SuppressWarnings("all")
class ${SecureRandomSeries.className()}Test {

    static ${SecureRandomSeries.className()} randomSeriesUnderTest;


    @BeforeAll
    public static void setUpOneTime() {
        randomSeriesUnderTest = new  ${SecureRandomSeries.className()}();
    }

    @Test
    void shouldReturnValueWithRequiredEntropy() {
        String value = randomSeriesUnderTest.nextString();
        assertThat(value).isNotNull();
        assertThat(value.length()).isGreaterThanOrEqualTo(${SecureRandomSeries.className()}.ENTROPY_STRING_LENGTH);
    }

    /**
     * When an unknown algorithm is specified, the  ${SecureRandomSeries.className()} falls back to
     * a default algorithm to ensure the application can continue running.  There's
     * always a chance that a given platform may not implement one of the secure
     * algorithms, hence the fallback to the platforms default secure algorithm.
     */
    @Test
    void shouldRecoverFromNoSuchAlgorithmException() {
        ${SecureRandomSeries.className()} series = new ${SecureRandomSeries.className()}("XYZ");
        assertThat(series).isNotNull();
    }

    /**
     * Verify sending a null argument raises an NPE
     */
    @Test
    void shouldThrowExceptionWhenAlgorithmIsNull() {
        assertThrows(NullPointerException.class, () -> new ${SecureRandomSeries.className()}(null));
    }

    /**
     * Exercise the nextLong() method.  Under the covers, a SecureRandom
     * is used, so its presumed the SecureRandom has been verified by
     * the JRE implementors to ensure randomness. This test verifies
     * the  ${SecureRandomSeries.className()} wrapper around the SecureRandom is working.
     */
    @Test
    void shouldReturnRandomLong() {
        assertThat(randomSeriesUnderTest.nextLong()).isNotNull();
    }


    @Test
    void shouldReturnResourceIds() {
        var resourceId = randomSeriesUnderTest.nextResourceId();

        // For the alphanumeric resource Ids, the string length is constant.
        assertThat(resourceId).isNotBlank().hasSize(${SecureRandomSeries.className()}.ENTROPY_STRING_LENGTH);

        // In our implementation, resourceIds are all digits
        assertAllLettersOrDigits(resourceId);
    }

    @Test
    void whenNumericResourceId_shouldReturnNumericId() {
        var resourceId = randomSeriesUnderTest.nextNumericResourceId();

        // Our numeric resource Ids have an entropy of 160 bits, so they can reach a length of 49 digits.
        assertThat(resourceId).isNotBlank().hasSizeBetween(ResourceIdGenerator.ENTROPY_STRING_LENGTH, ResourceIdGenerator.ENTROPY_MAX_NUMERIC_LENGTH);
        assertAllLettersOrDigits(resourceId);
    }

    @Test
    void shouldNotReturnNullInstance() {
        ${SecureRandomSeries.className()} instance = ${SecureRandomSeries.className()}.instance();
        assertThat(instance).isNotNull();
    }

    /* ===================================== HELPER METHODS ====================================== */

    private static void assertAllLettersOrDigits(String value) {
        value.chars().forEach(ch ->
                assertThat(Character.isLetterOrDigit(ch))
                    .as("Expected letter or digit but found '%c'", ch)
                    .isTrue());
    }
}
