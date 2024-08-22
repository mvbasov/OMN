/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017-2024 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.util;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import android.text.TextUtils;

/**
 * Created by mvb on 6/18/17.
 */

public class TextTools {
    /**
    * Escape string to pass as JavaString function parameter
    *
    * @param  param  Source string
    * @return String Escaped string
    */
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
    
    /**
    * Convert path with relative components path absolute
    * 
    * @param src     path with ../ inside it
    * @return String Simplified path or null if result begin with ../
    */
    public static String pathAbsolutize(String src) {
        if (src.charAt(0) == '/') src = src.substring(1);
        List<String>  resArray = new ArrayList<String>(Arrays.asList(src.split("/")));

        Integer idx;
        while((idx = resArray.indexOf("..")) > 0){
            resArray.remove(idx-1);
            resArray.remove(idx-1);
        }

        if(resArray.indexOf("..")==0)
            return null;
        else
            return "/" + TextUtils.join("/", resArray);
    }

    /**
    * Convert string with ' ' or '_' to CamelCase string
    *
    * @param  str   original string
    * @return       string converted to CamelCase
    */
    public static String toCamelCase(String str){
        // from https://stackoverflow.com/a/1144014
        StringBuilder sb = new StringBuilder();
        for(String oneWord : str.toLowerCase().split("[_ ]"))
        {
            sb.append(oneWord.substring(0,1).toUpperCase());
            sb.append(oneWord.substring(1) );
        }
        return sb.toString();
    }

}
