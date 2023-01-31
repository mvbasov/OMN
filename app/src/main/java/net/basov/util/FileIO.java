/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
//import android.widget.Toast;

import net.basov.omn.Constants;
import net.basov.omn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.io.StringReader;
import net.basov.omn.Page;

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
        saveTS(c);
    }

    public static Page importPage(Context c, InputStream stream) {
        return importPage(c, convertStreamToString(stream));
    }
    
    public static Page importPage(Context c, String content) {
        // String iteration based on https://stackoverflow.com/a/9259462
        BufferedReader reader = new BufferedReader(new StringReader(content));
        StringBuilder sb = new StringBuilder();
        Page inPage = null;
        String pn = null;
        Boolean inMarkdown = false;
        String currentLine = null;
        String sent = null;
        String from = null;
        String to = null;
        try {
            while ((currentLine = reader.readLine()) != null) {
                if (inMarkdown) {
                    if (currentLine.startsWith(Constants.EMA_MARK_STOP)) {
                        reader.close();
                        break;
                    }
                    sb.append(currentLine+"\n");
                } else {
                    if (currentLine.startsWith(Constants.EMA_H_VER)) continue;
                    if (currentLine.startsWith(Constants.EMA_H_PFN + ":")) {
                        pn = "/incoming" + currentLine.split(":")[1].trim();
                        continue;
                    }

                    if (currentLine.startsWith(Constants.EMA_H_SENT + ":")) {
                        sent = currentLine;
                        continue;
                    }
                    if (currentLine.startsWith(Constants.EMA_H_FROM + ":")) {
                        from = currentLine;
                        continue;
                    }
                    if (currentLine.startsWith(Constants.EMA_H_TO + ":")) {
                        to = currentLine;
                        continue;
                    }
                    if (currentLine.startsWith(Constants.EMA_MARK_START)) {
                        inMarkdown = true;
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            MyLog.LogE("Import page error.");
            inPage = null;
        }
        if (sb.length() == 0) return null;
        if (sent != null || from != null || to != null) {
            sb.append("\n- - -\n");
            if (sent != null) sb.append(sent + "  \n");
            if (from != null) sb.append(from + "  \n");
            if (to != null) sb.append(to + "  \n");
        }

        String fileName = "/md/"+ pn;      
        File file = new File(getFilesDir(c), fileName + ".md");
        String suffix = "";
        String str;
        for(int idx = 1; idx < 100; idx++) {
            if (!file.exists()) break;
            str = "0" + idx;
            suffix = str.length() > 2 ? str.substring(str.length() - 2) : str;
            suffix = "-" + suffix;
            file = new File(getFilesDir(c), fileName + suffix + ".md");
        }
        inPage = new Page(pn + suffix);
        inPage.setMdContent(sb.toString());
        if (suffix.length() > 0) inPage.setMetaByKey("title", inPage.getMetaByKey("title") + " (" + suffix.substring(1) + ")");
        if(!file.exists()) {
            creteParentDir(file);
            try {
                file.createNewFile();
                Writer writer = new BufferedWriter(new FileWriter(file));
                writer.write(inPage.getMetaHeaderAsString() + inPage.getMdContent());
                writer.flush();
                writer.close();

            } catch (IOException e) {
                MyLog.LogE(e, "Can't import page " + inPage.getPageName());
                return null;
            }
            return inPage;      
        } else {
           MyLog.LogE("Can't import page. Already exists" + inPage.getPageName());
        }
        return null;
    }

    public static boolean createPageIfNotExists(Context c, String pageName, String mtitle, String wvUserAgent) {
        final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = "/md" + pageName + ".md";
        File file = new File(getFilesDir(c), fileName);
        SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        if(!file.exists()) {
            creteParentDir(file);
            try {
                file.createNewFile();

                String ts = DF.format(new Date());
                String title = "";
                String tags = "";

                if (mtitle.length() == 0)
                    title = pageName;
                else
                    title = mtitle;
                if(pageName.equals("/default/Build")) title = "Build information";
                if(pageName.equals("/Start")) {
                    title = "My start page";
                    tags += "OMN default, Index";
                }
                if(pageName.equals("/QuickNotes")) {
                    title = "My quick notes";
                    tags += "OMN default, Notes";
                }
                if(pageName.equals("/incoming/Incoming")) {
                    title = "Incoming pages index";
                    tags += "OMN default, Index";
                }

                String pageHeader = "";
                if (defSharedPref.getBoolean(
                        c.getString(R.string.pk_enable_pelican_meta), true)){

                    pageHeader += c.getString(
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
                            ),
                            // Tags
                            tags
                    );

                } else {

                    pageHeader += c.getString(
                            R.string.no_pelican_header,
                            title
                    );

                }

                Writer writer = new BufferedWriter(new FileWriter(file));
                writer.write(pageHeader);

                if(pageName.equals("/default/Build")) {

                    String cmVersionString = "";
                    String cmVersion = AppDetails.getSystemProperty("ro.cm.version");
                    if (cmVersion != null && cmVersion.length() != 0) {
                        cmVersionString = "\n* CyanogenMod version: "
                                + cmVersion;
                    } else {
                        cmVersion = AppDetails.getSystemProperty("ro.lineage.version");
                        if (cmVersion != null && cmVersion.length() != 0) {
                            cmVersionString = "\n* LineageOS version: "
                                    + cmVersion;
                        } else {
                            cmVersion = AppDetails.getSystemProperty("ro.modversion");
                            if (cmVersion != null && cmVersion.length() != 0) {
                                cmVersionString = "\n* ROM mod version: "
                                        + cmVersion;
                            }
                        }
                    }
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

                } if (pageName.equals("/" + Constants.START_PAGE)) {
                    writer.write(c.getString(
                            R.string.template_page_start
                    ));
                } if (pageName.equals("/" + Constants.QUICKNOTES_PAGE)){
                    writer.write(c.getString(
                            R.string.template_page_quick_notes
                    ));
                }
                writer.flush();
                writer.close();

            } catch (IOException e) {
                MyLog.LogE(e, "Can't create new file " + pageName);
                return false;
            }
        }
        saveTS(c);
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

    public static Date getPageFileTS(Context c, String pageName) {
        File pageFile = new File(FileIO.getFilesDir(c) + "/md/" + pageName + ".md");
        if (pageFile.exists())
            return new Date(pageFile.lastModified());
        else
            return null;
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
                "md/" + Constants.URL_INCOMING_HELP_PAGE + ".md",
                Constants.COMON_CSS,
                Constants.HIGHLIGHT_CSS,
                Constants.ICONS_FONT,
                Constants.ICONS_CSS,
                Constants.FUNCTIONS_JS,
                Constants.URL_INCOMING_CSS,
                Constants.URL_INCOMING_JS,
                Constants.PSEARCH_CSS,
                Constants.PSEARCH_JS
        };
        copyFilesFromAssets(c, files, force);

        String[] optionalFiles = {
                Constants.CUSTOM_CSS,
                Constants.GITIGNORE,
                "md/" + Constants.URL_INCOMING_PAGE + ".md"
        };
        copyFilesFromAssets(c, optionalFiles, false);

        return true;

    }

    /**
     * Copy files from Assets to data files directory
     * If name contain '_legacy' this substring will be removed
     * If name start with '_' it will be replaced to '.'
     *
     * @param c     Application context
     * @param files Files list
     * @param force Force to overwrite flag
     */

    private static void copyFilesFromAssets(Context c, String[] files, Boolean force) {
        for(int i=0; i<files.length; i++) {
            URI uriPage = null;
            String toName = files[i];
            if (toName.contains("_legacy"))
                toName = toName.replace("_legacy", "");
            if (toName.indexOf("_") == 0)
                toName = toName.replaceFirst("^_", ".");
            try {
                uriPage = new URI("file://" + getFilesDir(c).getPath() + "/" + toName);
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

    /**
     * Copy file from Assets to data files directory
     * If name contain '_legacy' this substring will be removed
     * If name start with '_' it will be replaced to '.'
     *
     * @param c         Application context
     * @param aFilename File to copy
     */
    private static void copyFileFromAssets(Context c, String aFilename) {
        AssetManager assetManager = c.getAssets();

        InputStream in;
        OutputStream out;
        String newFileName = "";
        try {
            in = assetManager.open(aFilename);
            String toName = aFilename;
            if (toName.contains("_legacy"))
                toName = toName.replace("_legacy", "");
            if (toName.indexOf("_") == 0)
                toName = toName.replaceFirst("^_", ".");
            newFileName = getFilesDir(c).getPath() + "/" + toName;
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
            MyLog.LogE(e, "Reading file problem.");
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
            if (Build.VERSION.SDK_INT <= 18)
                filesDir = new File(Environment.getExternalStorageDirectory()
                        + "/Android/data/"
                        + c.getPackageName()
                        +"/files"
                );
            else
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
        } catch (FileNotFoundException e) {
            MyLog.LogE(e, "Unable write HTML to OutputStream " + aFile);
        }
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

    /**
     * Save last write operation timestamp (UNIX time) to file
     * @param c Application context
     */
    private static void saveTS(Context c) {
        String dts = String.format("%d", System.currentTimeMillis() / 1000L);
        try {
            File dtsFile = new File(getFilesDir(c) + "/.ts");
            FileOutputStream is = new FileOutputStream(dtsFile);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(dts);
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save page tags to special (like DB) file
     * @param c     Application context
     * @param pfn   page file name (without extension)
     * @param title page title
     * @param tags  list of tags
     */
    public static void savePageTags(Context c, String pfn, String title, ArrayList<String> tags) {
        String tagJSONStr = "";
        Boolean inJSON = false;
        Boolean dirty = false;
        String[] splitMd = getStringFromFile(getFilesDir(c)+ "/md/Tags.md").split("\\n");
        if (splitMd.length < 3) {
            dirty = true;
            tagJSONStr = "{}";
        } else {
            for(String str: splitMd) {
                if(str.startsWith("// Start of Tags DB")) {
                    inJSON = true;
                    continue;
                } else if (!inJSON) continue;
                if(str.startsWith("// End of Tags DB")) break;
                tagJSONStr += str;
            }
            tagJSONStr = tagJSONStr.replaceFirst("^var pageDb = ", "");
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(tagJSONStr);
            if(jsonObject.has(pfn.substring(1)) && tags == null) {
                jsonObject.remove(pfn.substring(1));
                dirty = true;
            } else if (tags != null) {
                JSONArray jsaTags = new JSONArray();
                for(String t : tags) jsaTags.put(t);

                JSONObject pageJsonObject = new JSONObject();
                pageJsonObject.put("tags",jsaTags);
                pageJsonObject.put("title", title);
                jsonObject.put(pfn.substring(1), pageJsonObject);
                dirty = true;
            }

            tagJSONStr = jsonObject.toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonObject == null) {
            tagJSONStr = "{}";
            dirty = true;
        }
        if(dirty){
            String tagFileContent = c.getString(R.string.md_tag_file_template, tagJSONStr);
            writePageToFile(c, "/Tags", tagFileContent);
        }
    }
}
