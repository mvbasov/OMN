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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
//import android.widget.Toast;

import net.basov.omn.Constants;
import net.basov.omn.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mvb on 9/29/17.
 */

public class FileIO {

    public static boolean isFileExists(Context c, String fileName) {
        File file = new File(getFilesDir(c), fileName);
        return file.exists();
    }

    public static void deletePage(Context c, String pageName) {
        File mdFile = new File(getFilesDir(c), "/md" + pageName + ".md");
        File htmlFile = new File(getFilesDir(c), "/html" + pageName + ".html");
        // TODO: remove debug
        //Toast.makeText(c, "Delete page "+ pageName, Toast.LENGTH_SHORT).show();
        if(mdFile.exists())
            mdFile.delete();
        if(htmlFile.exists())
            htmlFile.delete();
    }

    public static void writePageToFile(Context c, String pageName, String content) {
        String fileName = "/md" + pageName + ".md";
        File file = new File(getFilesDir(c), fileName);
        if(!file.exists()) creteParentDir(file);
        try {
            file.createNewFile();
            Writer writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            MyLog.LogE(e, "Write page to file failed.");
        }
    }

    public static boolean createPageIfNotExists(Context c, String pageName, String wvUserAgent) {
        final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = "/md" + pageName + ".md";
        File file = new File(getFilesDir(c), fileName);
        SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        if(!file.exists()) {
            creteParentDir(file);
            try {
                file.createNewFile();

                String ts = DF.format(new Date());
                // TODO: remove debug
                //MyLog.LogD("* Current time: "+ts);
                String title = pageName;
                if(pageName.equals("/default/Build")) title = "Build information";
                if(pageName.equals("/Start")) title = "My start page";
                if(pageName.equals("/QuickNotes")) title = "My quick notes";

                Writer writer = new BufferedWriter(new FileWriter(file));
                writer.write(c.getString(
                        R.string.pelican_header,
                        //Title
                        title,
                        //Creation date
                        ts,
                        //Modification date
                        ts,
                        //Authors
                        defSharedPref.getString(
                                c.getString(R.string.pk_notes_author),
                                ""
                        )
                ));

                if(pageName.equals("/default/Build")) {

                    String cmVersionString = "";
                    String cmVersion = AppDetails.getSystemProperty("ro.cm.version");
                    if (cmVersion.length() != 0)
                        cmVersionString = "* CyanogenMod version: "
                                + cmVersion
                                + "\n";

                    // Get App. name and build
                    String appInfo = c.getResources().getString(R.string.app_name);
                    try {
                        appInfo += " " + AppDetails.getAppName(c);
                    } catch (PackageManager.NameNotFoundException e) {
                        MyLog.LogE(e, "Get application name problem.");
                    }

                    writer.write(c.getString(
                                R.string.template_page_build,
                            // %1$s    android.os.Build.MODEL
                            android.os.Build.MODEL,
                            // %2$s    Build.MANUFACTURER
                            Build.MANUFACTURER,
                            // %3$s    android.os.Build.PRODUCT
                            android.os.Build.PRODUCT,
                            // %4$s    android.os.Build.DEVICE
                            android.os.Build.DEVICE,
                            // %5$d    android.os.Build.VERSION.SDK_INT
                            android.os.Build.VERSION.SDK_INT,
                            // %6$s    android.os.Build.VERSION.RELEASE
                            android.os.Build.VERSION.RELEASE,
                            // %7$s    android.os.Build.DISPLAY
                            android.os.Build.DISPLAY,
                            // %8$s    ro.cm.version (with "* CyanogenMod version: %8$s\n" or "")
                            cmVersionString,
                            // %9$s    Screen resolution
                            AppDetails.getScreenInfo(),
                            // %10s    WebView UserAgent
                            wvUserAgent,
                            // %10$s    App name and build
                            appInfo,
                            // %11$s   Data directory
                            FileIO.getFilesDir(c)
                    ));

                } if(pageName.equals("/Start")) {
                    writer.write(c.getString(
                            R.string.template_page_start
                    ));
                }
                writer.flush();
                writer.close();

            } catch (IOException e) {
                MyLog.LogE(e, "Can't create new file " + pageName);
                return false;
            }
        }
        return true;
    }

    public static boolean isPageActual(Context c, String pageName) {
        File mdFile = new File(FileIO.getFilesDir(c) + "/md/" + pageName + ".md");
        if (!mdFile.exists()) return false;
        File htmlFile = new File(FileIO.getFilesDir(c) + "/html/" + pageName + ".html");
        if (!htmlFile.exists()) return false;
        if(htmlFile.lastModified() < mdFile.lastModified()) return false;
        return true;
    }
    
    public static boolean creteHomePage(Context c) {
        return creteHomePage(c, false);
    }

    public static boolean creteHomePage(Context c, Boolean force) {
        String[] files = {
                "md/" + Constants.WELCOME_PAGE + ".md",
                "md/" + Constants.HELP_PAGE + ".md",
                "md/" + Constants.SYNTAX_PAGE + ".md",
                "md/" + Constants.SYNTAX_EXT_PAGE + ".md",
                "md/" + Constants.CHANGELOG + ".md",
                Constants.COMON_CSS,
                Constants.HIGHLIGHT_CSS,
                Constants.ICONS_FONT,
                Constants.ICONS_CSS
        };

        copyFilesFromAssets(c, files, force);
        return true;

    }

    private static void copyFilesFromAssets(Context c, String[] files, Boolean force) {
        for(int i=0; i<files.length; i++) {
            URI uriPage = null;
            try {
                uriPage = new URI("file://" + getFilesDir(c).getPath() + "/" + files[i]);
            } catch (URISyntaxException e) {
                MyLog.LogE(e, "Copy files from asset. Problem with target file " + files[i]);
            }
            File filePage = null;
            if (uriPage != null) {
                filePage = new File(uriPage);
            }
            if (null != filePage && (!filePage.exists() || force)) {
                creteParentDir(filePage);
                copyFileFromAssets(c, files[i]);
            }
        }
    }

    private static void copyFileFromAssets(Context c, String aFilename) {
        AssetManager assetManager = c.getAssets();

        InputStream in;
        OutputStream out;
        String newFileName = "";
        try {
            in = assetManager.open(aFilename);
            newFileName = getFilesDir(c).getPath() + "/" + aFilename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            MyLog.LogE(e, "Copy file from assets problem. newFileName = " + newFileName);
            Log.e(Constants.TAG, e.getMessage());
            e.printStackTrace();
        }

    }

    public static void creteParentDir(File file) {
        File parent = file.getParentFile();
        if(parent != null)
            if(!parent.exists())
                parent.mkdirs();
    }

    public static String convertStreamToString(InputStream is) {
        // http://www.java2s.com/Code/Java/File-Input-Output/ConvertInputStreamtoString.htm
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        Boolean firstLine = true;
        try {
            while ((line = reader.readLine()) != null) {
                if(firstLine){
                    sb.append(line);
                    firstLine = false;
                } else {
                    sb.append("\n").append(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            MyLog.LogE(e, "Reding file problem.");
        }
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) {
        // https://stackoverflow.com/a/36701219
        String ret = "";
        File fl = new File(filePath);
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(fl);
        } catch (FileNotFoundException e) {
            MyLog.LogI(e, "Page doesn't exists.");
            return ret;
        }
        ret = convertStreamToString(fin);
        //Make sure you close all streams.
        try {
            fin.close();
        } catch (IOException e) {
            Log.e(Constants.TAG, e.getMessage());
            e.printStackTrace();
        }
        return ret;
    }


    public static File getFilesDir(Context c) {
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            filesDir = c.getExternalFilesDir(null);
        } else {
            filesDir = c.getFilesDir();
        }
        return filesDir;
    }
    
    public static boolean saveHTML(Context c, String aFile, String aHTML) {
        File htmlFile = new File(getFilesDir(c), "/html/" + aFile + ".html");
        creteParentDir(htmlFile);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(htmlFile);
        } catch (FileNotFoundException e) {}
        try {
            stream.write(aHTML.getBytes());
        } catch (IOException e) {
            MyLog.LogE(e, "Unable write HTML to file " + aFile);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(Constants.TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        return true;
    }

}
