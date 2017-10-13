package net.basov.util;

/**
 * Created by mvb on 6/18/17.
 */

public class TextTools {

    public static String escapeCharsForJSON(String src) {
        return src
                .replace("\n", "\\n")
                .replace("'", "&#39;");
    }

}
