package fr.republicraft.common.api.helper;

public class StringHelper {
    /**
     * Helper method for boolean conversion
     *
     * @param s string to convert
     * @return true or false
     */
    public static boolean stringToBool(String s) {
        if (s.equals("1"))
            return true;
        if (s.equals("0"))
            return false;
        throw new IllegalArgumentException(s + " is not a bool. Only 1 and 0 are.");
    }

    /**
     * String template
     * Exemple: __("There's an incorrect value \"{0}\" in column # {1}", "a", "b")
     *
     * @param template
     * @param vars
     * @return
     */
    public static String __(String template, Object... vars) {
        for (int i = 0; i < vars.length; i++) {
            Object var = vars[i];
            if (var != null) {
                template = template.replace("{" + i + "}", var.toString());
            }
        }
        return template;
    }
}
