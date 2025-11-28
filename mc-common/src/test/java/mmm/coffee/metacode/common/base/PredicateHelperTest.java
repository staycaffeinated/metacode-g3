package mmm.coffee.metacode.common.base;

import org.assertj.core.condition.Not;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

class PredicateHelperTest {

    @Nested
    class BasicUseCases {
        @ParameterizedTest
        @ValueSource(strings = {"x", "y", "z"})
        void shouldAlwaysReturnTrue(String payload) {
            assertThat(PredicateHelper.alwaysTrue().test(payload)).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"x", "y", "z"})
        void shouldAlwaysReturnFalse(String payload) {
            assertThat(PredicateHelper.alwaysFalse().test(payload)).isFalse();
        }
    }

    @Nested
    class AndPredicateUseCases {
        @Test
        void shouldOnlyReturnTrueIfAllPredicatesAreTrue() {
            Predicate<Object> p1 = PredicateHelper.alwaysTrue();
            Predicate<Object> p2 = PredicateHelper.alwaysTrue();
            Predicate<Object> p3 = PredicateHelper.alwaysTrue();

            var result = PredicateHelper.and(p1, p2, p3);
            assertThat(result.test(1)).isTrue();
        }

        @Test
        void shouldReturnFalseIfAnyPredicateIsFalse() {
            Predicate<Object> p1 = PredicateHelper.alwaysTrue();
            Predicate<Object> p2 = PredicateHelper.alwaysFalse();
            Predicate<Object> p3 = PredicateHelper.alwaysTrue();

            var result = PredicateHelper.and(p1, p2, p3);
            assertThat(result.test(1)).isFalse();
        }

        @Test
        void verifyHashCodeAndEqualsMethods() {
            var p = anAndPredicateThatEvaluatesToTrue();
            assertThat(p.hashCode()).isEqualTo(anAndPredicateThatEvaluatesToTrue().hashCode());
            assertThat(p.hashCode()).isNotEqualTo(anAndPredicateThatEvaluatesToFalse().hashCode());

            assertThat(p.equals(anAndPredicateThatEvaluatesToTrue())).isTrue();
            assertThat(p.equals(anAndPredicateThatEvaluatesToFalse())).isFalse();
        }
    }


    @Nested
    class OrPredicateUseCases {
        @Test
        void shouldReturnTrueIfAnyPredicateIsTrue() {
            Predicate<Object> p1 = PredicateHelper.alwaysFalse();
            Predicate<Object> p2 = PredicateHelper.alwaysTrue();
            Predicate<Object> p3 = PredicateHelper.alwaysFalse();

            var result = PredicateHelper.or(p1, p2, p3);
            assertThat(result.test(1)).isTrue();
        }

        @Test
        void shouldReturnFalseIfAllPredicatesAreFalse() {
            Predicate<Object> p1 = PredicateHelper.alwaysFalse();
            Predicate<Object> p2 = PredicateHelper.alwaysFalse();
            Predicate<Object> p3 = PredicateHelper.alwaysFalse();

            var result = PredicateHelper.and(p1, p2, p3);
            assertThat(result.test(1)).isFalse();
        }

        @Test
        void verifyHashCodeAndEqualsMethods() {
            var p = anOrPredicateThatEvaluatesToTrue();
            assertThat(p.hashCode()).isEqualTo(anOrPredicateThatEvaluatesToTrue().hashCode());
            assertThat(p.hashCode()).isNotEqualTo(anOrPredicateThatEvaluatesToFalse().hashCode());

            assertThat(p.equals(anOrPredicateThatEvaluatesToTrue())).isTrue();
            assertThat(p.equals(anOrPredicateThatEvaluatesToFalse())).isFalse();
        }
    }

    @Nested
    class NotPredicateUseCases {
        @Test
        void shouldReturnTrueIfBasePredicateIsFalse() {
            Predicate<Object> p1 = PredicateHelper.alwaysTrue();

            var result = PredicateHelper.not(p1);
            assertThat(result.test(1)).isFalse();
        }

        @Test
        void shouldReturnFalseIfBasePredicateIsTrue() {
            Predicate<Object> p1 = PredicateHelper.alwaysFalse();

            var result = PredicateHelper.not(p1);
            assertThat(result.test(1)).isTrue();
        }

        @Test
        void whenComposedPredicateIsTrue_shouldReturnFalse() {
            var p1 = anAndPredicateThatEvaluatesToTrue();
            var result = PredicateHelper.not(p1);
            assertThat(result.test(1)).isFalse();

            var p2 = anOrPredicateThatEvaluatesToTrue();
            result = PredicateHelper.not(p2);
            assertThat(result.test(1)).isFalse();
        }

        @Test
        void whenComposedPredicateIsFalse_shouldReturnTrue() {
            var p1 = anAndPredicateThatEvaluatesToFalse();
            var result = PredicateHelper.not(p1);
            assertThat(result.test(1)).isTrue();

            var p2 = anOrPredicateThatEvaluatesToFalse();
            result = PredicateHelper.not(p2);
            assertThat(result.test(1)).isTrue();
        }

        @Test
        void verifyHashCodeAndEqualsMethods() {
            var alwaysTrue = anOrPredicateThatEvaluatesToTrue();
            var notAlwaysTrue = PredicateHelper.not(alwaysTrue);
            var alsoNotTrue = PredicateHelper.not(alwaysTrue);

            assertThat(notAlwaysTrue.hashCode()).isNotEqualTo(alwaysTrue.hashCode());
            assertThat(notAlwaysTrue.equals(alwaysTrue)).isFalse();

            // Not same object, so not equal
            assertThat(alsoNotTrue.equals(alwaysTrue)).isFalse();
        }
    }

    /* -------------------------------------------------------------------------------------------------------
     * HELPER METHODS
     * ------------------------------------------------------------------------------------------------------- */
    Predicate<Object> anAndPredicateThatEvaluatesToTrue() {
        return PredicateHelper.and(PredicateHelper.alwaysTrue(), PredicateHelper.alwaysTrue());
    }

    Predicate<Object> anAndPredicateThatEvaluatesToFalse() {
        return PredicateHelper.and(PredicateHelper.alwaysFalse(), PredicateHelper.alwaysTrue());
    }

    Predicate<Object> anOrPredicateThatEvaluatesToTrue() {
        return PredicateHelper.or(PredicateHelper.alwaysTrue(), PredicateHelper.alwaysFalse());
    }

    Predicate<Object> anOrPredicateThatEvaluatesToFalse() {
        return PredicateHelper.or(PredicateHelper.alwaysFalse(), PredicateHelper.alwaysFalse());
    }
}
