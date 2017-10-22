package net.basov.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.pm.PackageManager.NameNotFoundException;
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

    public static boolean createPageIfNotExists(Context c, String pageName) {
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
                try {
                    if(pageName.equals("/default/Build")) {
                        writer.write(c.getString(
                                     R.string.template_page_build,
                                     AppDetails.getAppName(c)
                        ));
                    }
                } catch (NameNotFoundException e) {
                    MyLog.LogE(e, "Get application name problem.");
                }
                if(pageName.equals("/Start")) {
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
