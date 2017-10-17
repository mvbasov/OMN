/**
 * The MIT License (MIT)

 Copyright (c) 2017 Mikhail Basov

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

package net.basov.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mvb on 9/8/17.
 * Utils to get package name and version.
 */

public class AppDetails {

    private final static String TAG = "AppDetails";

    public static String getAppName(Context c) throws PackageManager.NameNotFoundException {
        /*
         * - This function require the following in the app/build.gradle:
         *
         *  buildTypes {
         *      ...
         *      debug {
         *          ...
         *          resValue "string", "git_describe", getGitRevParseInfo ( "describe --tags --abbrev=1" )
         *      }
         *  }
         *  def getGitRevParseInfo (what) {
         *    def cmd = "git " + what + " HEAD"
         *    def proc = cmd.execute ()
         *    proc.text.trim ()
         *  }
         *
         * - AIDE has limited support of gradle.
         *   If program compiled by AndroidStudio R.string.git_describe is set by gradle.
         *   If program compiled by AIDE this resource doesn't exist.
         */
        String appName = "";
        android.content.pm.PackageInfo pInfo =
                c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
        int git_describe_id =
                c.getResources().getIdentifier("git_describe", "string", c.getPackageName());
        if (git_describe_id == 0)
            appName += pInfo.versionName + "-AIDE";
        else {
            String git_describe = c.getResources().getString(git_describe_id);
            if (!git_describe.isEmpty())
                appName += git_describe;
            else
                appName += pInfo.versionName;
        }

        return appName;

    }

    /* From cmupdaterapp code. Original source deleted from code.google.com */
    public static String getSystemProperty(String propName){
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        }
        catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        }
        finally {
            if(input != null) {
                try {
                    input.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }
}
