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
