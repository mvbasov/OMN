package net.basov.util;

/**
 * Created by mvb on 6/18/17.
 */

public class TextTools {

    public static String escapeJavaScriptFunctionParameter(String param) {
        /* Concept of code from here https://stackoverflow.com/a/23224442 */
        char[] chars = param.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\'':
                    sb.append("\\'");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;

                default:
                    sb.append(chars[i]);
                    break;
            }
        }
        return sb.toString();
    }
}
