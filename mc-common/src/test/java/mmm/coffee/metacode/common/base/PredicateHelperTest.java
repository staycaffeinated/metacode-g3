package mmm.coffee.metacode.common.base;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.util.List;
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
        void shouldOnlyReturnTrueIfAllPredicatesAreTrue_whenPassedAsIterable() {
            List<Predicate<Object>> components = List.of(
                    PredicateHelper.alwaysTrue(),
                    PredicateHelper.alwaysTrue(),
                    PredicateHelper.alwaysTrue()
            );
            assertThat(PredicateHelper.and(components).test(1)).isTrue();
        }

        @Test
        void shouldReturnFalseIfAnyPredicateIsFalse_whenPassedAsIterable() {
            List<Predicate<Object>> components = List.of(
                    PredicateHelper.alwaysTrue(),
                    PredicateHelper.alwaysFalse()
            );
            assertThat(PredicateHelper.and(components).test(1)).isFalse();
        }

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
            assertThat(p).hasSameHashCodeAs(anAndPredicateThatEvaluatesToTrue())
                    .doesNotHaveSameHashCodeAs(anAndPredicateThatEvaluatesToFalse());

            assertThat(p.equals(anAndPredicateThatEvaluatesToTrue())).isTrue();
            assertThat(p.equals(anAndPredicateThatEvaluatesToFalse())).isFalse();
        }

        @Test
        void equalsReturnsFalseForNonAndPredicateArgument() {
            Predicate<Object> andPredicate = anAndPredicateThatEvaluatesToTrue();
            Predicate<Object> orPredicate = anOrPredicateThatEvaluatesToTrue();

            assertThat(andPredicate.equals(null)).isFalse();
            assertThat(andPredicate.equals("not a predicate")).isFalse();
            assertThat(andPredicate.equals(orPredicate)).isFalse();
        }

        @Test
        void applyDelegatesToTest() throws Exception {
            Predicate<Object> predicate = anAndPredicateThatEvaluatesToTrue();
            Method apply = predicate.getClass().getDeclaredMethod("apply", Object.class);
            apply.setAccessible(true);

            assertThat((Boolean) apply.invoke(predicate, "input")).isTrue();

            Predicate<Object> falsy = anAndPredicateThatEvaluatesToFalse();
            apply = falsy.getClass().getDeclaredMethod("apply", Object.class);
            apply.setAccessible(true);
            assertThat((Boolean) apply.invoke(falsy, "input")).isFalse();
        }
    }


    @Nested
    class OrPredicateUseCases {
        @Test
        void shouldReturnTrueIfAnyPredicateIsTrue_whenPassedAsIterable() {
            List<Predicate<Object>> components = List.of(
                    PredicateHelper.alwaysFalse(),
                    PredicateHelper.alwaysTrue(),
                    PredicateHelper.alwaysFalse()
            );
            assertThat(PredicateHelper.or(components).test(1)).isTrue();
        }

        @Test
        void shouldReturnFalseIfAllPredicatesAreFalse_whenPassedAsIterable() {
            List<Predicate<Object>> components = List.of(
                    PredicateHelper.alwaysFalse(),
                    PredicateHelper.alwaysFalse()
            );
            assertThat(PredicateHelper.or(components).test(1)).isFalse();
        }

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
            assertThat(p).hasSameHashCodeAs(anOrPredicateThatEvaluatesToTrue())
                    .doesNotHaveSameHashCodeAs(anOrPredicateThatEvaluatesToFalse());

            assertThat(p.equals(anOrPredicateThatEvaluatesToTrue())).isTrue();
            assertThat(p.equals(anOrPredicateThatEvaluatesToFalse())).isFalse();
        }

        @Test
        void equalsReturnsFalseForNonOrPredicateArgument() {
            Predicate<Object> orPredicate = anOrPredicateThatEvaluatesToTrue();
            Predicate<Object> andPredicate = anAndPredicateThatEvaluatesToTrue();

            assertThat(orPredicate.equals(null)).isFalse();
            assertThat(orPredicate.equals("not a predicate")).isFalse();
            assertThat(orPredicate.equals(andPredicate)).isFalse();
        }

        @Test
        void applyDelegatesToTest() throws Exception {
            Predicate<Object> predicate = anOrPredicateThatEvaluatesToTrue();
            Method apply = predicate.getClass().getDeclaredMethod("apply", Object.class);
            apply.setAccessible(true);

            assertThat((Boolean) apply.invoke(predicate, "input")).isTrue();

            Predicate<Object> falsy = anOrPredicateThatEvaluatesToFalse();
            apply = falsy.getClass().getDeclaredMethod("apply", Object.class);
            apply.setAccessible(true);
            assertThat((Boolean) apply.invoke(falsy, "input")).isFalse();
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

        @Test
        void equalsReturnsTrueWhenBothNotPredicatesWrapSamePredicate() {
            Predicate<Object> base = anOrPredicateThatEvaluatesToTrue();
            Predicate<Object> notA = PredicateHelper.not(base);
            Predicate<Object> notB = PredicateHelper.not(base);

            assertThat(notA.equals(notB)).isTrue();
        }

        @Test
        void equalsReturnsFalseWhenNotPredicatesWrapDifferentPredicates() {
            Predicate<Object> notTrue = PredicateHelper.not(PredicateHelper.alwaysTrue());
            Predicate<Object> notFalse = PredicateHelper.not(PredicateHelper.alwaysFalse());

            assertThat(notTrue.equals(notFalse)).isFalse();
        }

        @Test
        void equalsReturnsFalseForNonNotPredicateArgument() {
            Predicate<Object> notPredicate = PredicateHelper.not(PredicateHelper.alwaysTrue());
            Predicate<Object> andPredicate = anAndPredicateThatEvaluatesToTrue();

            assertThat(notPredicate.equals(null)).isFalse();
            assertThat(notPredicate.equals("not a predicate")).isFalse();
            assertThat(notPredicate.equals(andPredicate)).isFalse();
        }

        @Test
        void applyDelegatesToTest() throws Exception {
            Predicate<Object> predicate = PredicateHelper.not(PredicateHelper.alwaysFalse());
            Method apply = predicate.getClass().getDeclaredMethod("apply", Object.class);
            apply.setAccessible(true);

            assertThat((Boolean) apply.invoke(predicate, "input")).isTrue();

            Predicate<Object> falsy = PredicateHelper.not(PredicateHelper.alwaysTrue());
            apply = falsy.getClass().getDeclaredMethod("apply", Object.class);
            apply.setAccessible(true);
            assertThat((Boolean) apply.invoke(falsy, "input")).isFalse();
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
