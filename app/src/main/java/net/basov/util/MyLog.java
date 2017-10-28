/*
Open Markdown Notes (android application to take and organize everydays notes)

Copyright (C) 2017 Mikahil Basov mikhail[at]basov[dot]net

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
