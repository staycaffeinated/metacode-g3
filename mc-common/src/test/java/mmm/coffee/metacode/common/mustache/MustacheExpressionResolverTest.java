package mmm.coffee.metacode.common.mustache;

import com.samskivert.mustache.MustacheException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class MustacheExpressionResolverTest {

    final String keyOne = "foo";
    final String keyTwo = "bar";
    final String valueOne = "bar";
    final String valueTwo = "buzz";

    @Test
    void shouldResolveExpression() {
        String expression = "{{" + keyOne + "}}|"+"{{"+ keyTwo + "}}";
        String actual = MustacheExpressionResolver.resolve(expression, anExampleMap());
        assertThat(actual).contains(valueOne);
        assertThat(actual).contains(valueTwo);
    }

    @Test
    void shouldThrowExceptionIfExpressionIsNull() {
        Map<String,String> map = anExampleMap();
        assertThrows(NullPointerException.class,
                () -> MustacheExpressionResolver.resolve(null, map));
    }

    /*
     * The Mustache library itself throws an error if all the variables
     * in the expression being parsed are not resolved. Thus, if there's
     * a variable in the expression that's not found in the map, the
     * Mustache execute() method throws an exception.
     * IMHO, a better idea is to merely resolve what can be resolved,
     * leaving the unresolved parameters in-place in the returned value.
     * If I find a library that can do that, I'll replace this one.
     */
    @Test
    void shouldThrowExceptionIfAllVariablesDoNotResolve() {
        String expression = "{{This}}{{value}}{{is}}{{not}}{{Defined}}";
        Map<String,String> map = anExampleMap();
        assertThrows(MustacheException.class,
                () -> MustacheExpressionResolver.resolve(expression, map));
    }

    /* ------------------------------------------------------------------------------------------
     * HELPER METHODS
     * ------------------------------------------------------------------------------------------ */

    Map<String, String> anExampleMap() {
        Map<String, String> map = new HashMap<>();
        map.put(keyOne, valueOne);
        map.put(keyTwo, valueTwo);
        return map;
    }
}
