/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/*
 * Copied this code from: https://github.com/google/guava/blob/master/guava/src/com/google/common/base/Predicates.java.
 * The syntax was modernized to Java 17.
 */

package mmm.coffee.metacode.common.base;


import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateHelper {

    /**
     * Returns a predicate that always evaluates to {@code true}.
     *
     * <p><b>Discouraged:</b> Prefer using {@code x -> true}, but note that lambdas do not have
     * human-readable {@link #toString()} representations and are not serializable.
     */
    public static <T> Predicate<T> alwaysTrue() {
        return t -> true;
    }

    /**
     * Returns a predicate that always evaluates to {@code false}.
     *
     * <p><b>Discouraged:</b> Prefer using {@code x -> false}, but note that lambdas do not have
     * human-readable {@link #toString()} representations and are not serializable.
     */
    public static <T> Predicate<T> alwaysFalse() {
        return t -> false;
    }

    /**
     * Returns a predicate that evaluates to {@code true} if the given predicate evaluates to {@code
     * false}.
     *
     * <p><b>Discouraged:</b> Prefer using {@code predicate.negate()}.
     */
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return new NotPredicate<>(predicate);
    }

    /**
     * Returns a predicate that evaluates to {@code true} if each of its components evaluates to
     * {@code true}. The components are evaluated in order, and evaluation will be "short-circuited"
     * as soon as a false predicate is found. It defensively copies the iterable passed in, so future
     * changes to it won't alter the behavior of this predicate. If {@code components} is empty, the
     * returned predicate will always evaluate to {@code true}.
     *
     * <p><b>Discouraged:</b> Prefer using {@code first.and(second).and(third).and(...)}.
     */
    public static <T> Predicate<T> and(
            Iterable<? extends Predicate<? super T>> components) {
        return new AndPredicate<>(defensiveCopy(components));
    }

    /**
     * Returns a predicate that evaluates to {@code true} if each of its components evaluates to
     * {@code true}. The components are evaluated in order, and evaluation will be "short-circuited"
     * as soon as a false predicate is found. It defensively copies the array passed in, so future
     * changes to it won't alter the behavior of this predicate. If {@code components} is empty, the
     * returned predicate will always evaluate to {@code true}.
     *
     * <p><b>Discouraged:</b> Prefer using {@code first.and(second).and(third).and(...)}.
     */
    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<? super T>... components) {
        return new AndPredicate<T>(defensiveCopy(components));
    }

    /**
     * Returns a predicate that evaluates to {@code true} if both of its components evaluate to {@code
     * true}. The components are evaluated in order, and evaluation will be "short-circuited" as soon
     * as a false predicate is found.
     *
     * <p><b>Discouraged:</b> Prefer using {@code first.and(second)}.
     */
    public static <T> Predicate<T> and(
            Predicate<? super T> first, Predicate<? super T> second) {
        return new AndPredicate<>(PredicateHelper.<T>asList(checkNotNull(first), checkNotNull(second)));
    }

    /**
     * Returns a predicate that evaluates to {@code true} if any one of its components evaluates to
     * {@code true}. The components are evaluated in order, and evaluation will be "short-circuited"
     * as soon as a true predicate is found. It defensively copies the iterable passed in, so future
     * changes to it won't alter the behavior of this predicate. If {@code components} is empty, the
     * returned predicate will always evaluate to {@code false}.
     *
     * <p><b>Discouraged:</b> Prefer using {@code first.or(second).or(third).or(...)}.
     */
    public static <T> Predicate<T> or(
            Iterable<? extends Predicate<? super T>> components) {
        return new OrPredicate<>(defensiveCopy(components));
    }

    /**
     * Returns a predicate that evaluates to {@code true} if any one of its components evaluates to
     * {@code true}. The components are evaluated in order, and evaluation will be "short-circuited"
     * as soon as a true predicate is found. It defensively copies the array passed in, so future
     * changes to it won't alter the behavior of this predicate. If {@code components} is empty, the
     * returned predicate will always evaluate to {@code false}.
     *
     * <p><b>Discouraged:</b> Prefer using {@code first.or(second).or(third).or(...)}.
     */
    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<? super T>... components) {
        return new OrPredicate<T>(defensiveCopy(components));
    }

    /**
     * Returns a predicate that evaluates to {@code true} if either of its components evaluates to
     * {@code true}. The components are evaluated in order, and evaluation will be "short-circuited"
     * as soon as a true predicate is found.
     *
     * <p><b>Discouraged:</b> Prefer using {@code first.or(second)}.
     */
    public static <T> Predicate<T> or(
            Predicate<? super T> first, Predicate<? super T> second) {
        return new OrPredicate<>(PredicateHelper.<T>asList(checkNotNull(first), checkNotNull(second)));
    }

    /* ==================================================================================================
     * HELPER CLASSES
     * ================================================================================================== */

    private static final class AndPredicate<T>
            implements Predicate<T>, Serializable {
        private final List<? extends Predicate<? super T>> components;

        private AndPredicate(List<? extends Predicate<? super T>> components) {
            this.components = components;
        }

        public boolean apply(@Nullable T input) {
            return test(input);
        }

        @Override
        public boolean test(T t) {
            for (Predicate<? super T> component : components) {
                if (!component.test(t)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            // add a random number to avoid collisions with OrPredicate
            return components.hashCode() + 0x12472c2c;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof AndPredicate<?> that) {
                return components.equals(that.components);
            }
            return false;
        }
    }

    private static final class OrPredicate<T>
            implements Predicate<T>, Serializable {
        private final List<? extends Predicate<? super T>> components;

        private OrPredicate(List<? extends Predicate<? super T>> components) {
            this.components = components;
        }

        public boolean apply(@Nullable T input) {
            return test(input);
        }

        @Override
        public boolean test(T t) {
            for (Predicate<? super T> component : components) {
                if (component.test(t)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            // add a random number to avoid collisions with AndPredicate
            return components.hashCode() + 0x053c91cf;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof OrPredicate<?> that) {
                return components.equals(that.components);
            }
            return false;
        }
    }

    private static final class NotPredicate<T>
            implements Predicate<T>, Serializable {
        final Predicate<T> predicate;

        NotPredicate(Predicate<T> predicate) {
            this.predicate = checkNotNull(predicate);
        }

        public boolean apply(@Nullable T input) {
            return test(input);
        }

        @Override
        public boolean test(T t) {
            return !predicate.test(t);
        }

        @Override
        public int hashCode() {
            return ~predicate.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof NotPredicate<?> that) {
                return predicate.equals(that.predicate);
            }
            return false;
        }
    }

    /* ==================================================================================================
     * HELPER METHODS
     * ================================================================================================== */
    private static <T> List<Predicate<? super T>> asList(
            Predicate<? super T> first, Predicate<? super T> second) {
        return Arrays.<Predicate<? super T>>asList(first, second);
    }

    @SafeVarargs
    private static <T> List<T> defensiveCopy(T... array) {
        return defensiveCopy(Arrays.asList(array));
    }

    static <T> List<T> defensiveCopy(Iterable<T> iterable) {
        ArrayList<T> list = new ArrayList<>();
        for (T element : iterable) {
            list.add(checkNotNull(element));
        }
        return list;
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
