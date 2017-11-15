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

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.DisplayMetrics;
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
         * TODO: Old version form palgorithm. Remove and describe new.
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
        // TODO: Rewrite. git_describe string resource used as AIDE compilation flag. It is not elegant solution
        if (git_describe_id == 0)
            appName += pInfo.versionName + "-AIDE";
        else {
//            String git_describe = c.getResources().getString(git_describe_id);
//            if (!git_describe.isEmpty())
//                appName += git_describe;
//            else
//                appName += pInfo.versionName;
            appName += pInfo.versionName;
        }

        return appName;

    }

    /* From cmupdaterapp code. Original source deleted from code.google.com */
    public static String getSystemProperty(String propName){
        String line = "";
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine().trim();
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

    public static String getScreenInfo() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        double wi=(double)width/(double)dm.xdpi;
        double hi=(double)height/(double)dm.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);
        return String.format(
                "%d \\* %d [%.2f \\* %.2f] (%.2f \")",
                width,
                height,
                dm.xdpi,
                dm.ydpi,
                screenInches);

    }
}
