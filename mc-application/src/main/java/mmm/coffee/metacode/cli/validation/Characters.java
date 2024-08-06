package mmm.coffee.metacode.cli.validation;

/**
 * A mixture of character tests to check for upper-case, lower-case, digits, and such.
 */
public class Characters {

    private Characters() {
        // private by design. 
    }

    public static boolean isUpperCase(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    public static boolean isLowerCase(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    public static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isLetterOrDigitOrUnderscore(char ch) {
        return isDigit(ch) || isLowerCase(ch) || isUpperCase(ch) || ch == '_';
    }


    /**
     * Not exactly the rules of a Java identifier, but sufficient for package names
     *
     * @param token the token to check
     * @return true if the token is a legal part of a package name
     */
    public static boolean isLegalIdentifier(String token) {
        // first character must be a-z or underscore
        if (!(isLowerCase(token.charAt(0)) || token.charAt(0) == '_')) {
            return false;
        }

        // a valid subsequent letters can be: a-z, A-Z, 0-9, or underscore
        for (var i = 1; i < token.length(); i++) {
            char ch = token.charAt(i);
            if (!isLetterOrDigitOrUnderscore(ch))
                return false;
        }
        return true;
    }

}
