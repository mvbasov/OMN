/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017-2023 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.util;

import android.util.Log;

import net.basov.omn.Constants;

/**
 * Created by mvb on 10/6/17.
 */

public class MyLog {

    public static void LogI (String msg) { Log.i(Constants.TAG, msg); }
    public static void LogI (Exception e) {
        LogI(e,"");
    }
    public static void LogI (Exception e, String msg) {
        String diag = getDiagString(e);
        Log.i(Constants.TAG,
              msg.length() > 0 ? msg + "\n" + diag : diag
              );
    }

    public static void LogW (String msg) { Log.w(Constants.TAG, msg); }
    public static void LogW (Exception e) {
        LogW(e,"");
    }
    public static void LogW (Exception e, String msg) {
        String diag = getDiagString(e);
        Log.w(Constants.TAG,
              msg.length() > 0 ? msg + "\n" + diag : diag
              );
    }

    public static void LogD (String msg) { Log.d(Constants.TAG, msg); }
    public static void LogD (Exception e) {
        LogD(e,"");
    }
    public static void LogD (Exception e, String msg) {
        String diag = getDiagString(e);
        Log.d(Constants.TAG,
              msg.length() > 0 ? msg + "\n" + diag : diag
              );
    }

    public static void LogE (String msg) { Log.e(Constants.TAG, msg); }
    public static void LogE (Exception e) {
        LogE(e,"");
    }
    public static void LogE (Exception e, String msg) {
        String diag = getDiagString(e);
        Log.e(Constants.TAG,
              msg.length() > 0 ? msg + "\n" + diag : diag
              );
    }

    private static String getDiagString (Exception e) {
        return "at "
                + e.getStackTrace()[2].getClassName()
                + "("
                + e.getStackTrace()[2].getFileName()
                + ":"
                + e.getStackTrace()[2].getLineNumber()
                + ")\n"
                + e.getMessage();
    }
}
