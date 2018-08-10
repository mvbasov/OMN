/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.omn;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.LinkedHashMap;

import net.basov.util.AppDetails;
import net.basov.util.FileIO;
import net.basov.util.MyLog;

public class WebViewJSCallback {

    private Context mContext;

    WebViewJSCallback(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void prefButtonCallback() {
        Intent i = new Intent();
        i.setAction(mContext.getPackageName() + ".PREFERENCES");
        mContext.startActivity(i);
    }

    @JavascriptInterface
    public void quicknoteButtonCallback() {
        Intent i = new Intent();
        i.setAction(mContext.getPackageName() + ".QUICK_NOTE");
        mContext.startActivity(i);
    }

    @JavascriptInterface
    public void newPageButtonCallback() {
        Intent i = new Intent();
        i.setAction(mContext.getPackageName() + ".NEW_PAGE");
        mContext.startActivity(i);
    }

    @JavascriptInterface
    public void createButtonCallback(String PFN, String wvUA) {
        FileIO.createPageIfNotExists(mContext, PFN,"", wvUA);

        if(PFN.equals("/" + Constants.BUILD_PAGE)) {
            Intent i = new Intent();
            i.setAction(mContext.getPackageName() + ".REDISPLAY_PAGE");
            mContext.startActivity(i);
        } else {
            editButtonCallback(PFN);
        }
    }

    @JavascriptInterface
    public void editButtonCallback(String pn) {

        Intent i = new Intent();
        i.setAction(mContext.getPackageName()+".EDIT_PAGE");
        i.putExtra("name", pn);

        mContext.startActivity(i);
    }

    @JavascriptInterface
    public void homeButtonCallback() {

        Intent i = new Intent();
        i.setAction(mContext.getPackageName()+".HOME_PAGE");

        mContext.startActivity(i);
    }

    @JavascriptInterface
    public void emailButtonCallback(String pn, String title) {

        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        // TODO: send HTML E-Mail body.
        //i.setType("text/html");

        // Help user send platform statistic to correct dev. address
        if (pn.equals("/default/Build")) {
            // Simplest defence from spamer - put e-mail address in different part of code
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.EMA + "@" + Constants.EMA_DOM});
            // Get build information
            String appInfo = "";
            try {
                appInfo += " " + AppDetails.getAppName(mContext);
            } catch (PackageManager.NameNotFoundException e) {
                MyLog.LogE(e, "Get application name problem.");
            }
            String subject = "["
                    + Build.MANUFACTURER
                    + " - "
                    + Build.MODEL
                    + "] "
                    + appInfo;
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        } else {
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        }

        i.putExtra(
                Intent.EXTRA_HTML_TEXT,
                Html.fromHtml(
                        FileIO.getStringFromFile(
                                FileIO.getFilesDir(mContext) + "/html" + pn + ".html"
                        )
                )
        );
        i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + FileIO.getFilesDir(mContext) + "/html" + pn + ".html"));
        i.putExtra(
                Intent.EXTRA_TEXT,
                mContext.getString(R.string.ema_md_start)
                + FileIO.getStringFromFile(
                    FileIO.getFilesDir(mContext) + "/md" + pn + ".md"
                  )
                +mContext.getString(R.string.ema_md_end)
        );
        try {
            mContext.startActivity(i);
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(mContext, "No E-Mail client found. Please install one.", Toast.LENGTH_LONG).show();
        }
    }
    
    @JavascriptInterface
    public void folderButtonCallback (String pn) {
        Intent intent = new Intent();

        SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (defSharedPref.getBoolean(mContext.getString(R.string.pk_use_view_directory), false)) {
            /* For ES File Manager and X-plore File Manager */
            intent.setAction("org.openintents.action.VIEW_DIRECTORY");
        } else {
            /* For OI File Manager */
            intent.setAction("android.intent.action.VIEW");
        }

        Uri uri = Uri.parse("file://" + FileIO.getFilesDir(mContext).getAbsolutePath());
        intent.setData(uri);
        try {
            // Dirty hack to enable file:// URI
            StrictMode.VmPolicy old = StrictMode.getVmPolicy();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(old)
                                           .detectFileUriExposure()
                                           //.penaltyLog()
                                           .build());
                mContext.startActivity(Intent.createChooser(intent, "Open pages folder"));
                StrictMode.setVmPolicy(old);
            } else {
                mContext.startActivity(Intent.createChooser(intent, "Open pages folder"));
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No File Manager found. Please install one.", Toast.LENGTH_LONG).show();
        }
    }

    @JavascriptInterface
    public void linkButtonCallback(String pn, String title) {
        Toast.makeText(mContext, "Link to page in clipboard.", Toast.LENGTH_SHORT).show();

        ClipboardManager clipboard =
                (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                title,
                "["+title+"]("+pn.substring(pn.lastIndexOf("/") + 1)+".html)"
        );
        clipboard.setPrimaryClip(clip);      
    }

    @JavascriptInterface
    public void refreshHtmlButtonCallback() {
        Intent i = new Intent();
        i.setAction(mContext.getPackageName() + ".REDISPLAY_PAGE");
        i.putExtra("RECREATE_HTML", true);
        mContext.startActivity(i);
    }
    
    @JavascriptInterface
    public void saveHTML(String html, String PFN, String Title) {
        Toast.makeText(mContext, "Save HTML.", Toast.LENGTH_SHORT).show();

        /* Make directory prefix for right css reference */
        int dirCount = PFN.length() - PFN.replaceAll("/","").length();
        String dirPrefix = "";
        if(PFN.charAt(0) == '/') dirCount--;
        for(int i=0; i < dirCount; i++) dirPrefix += "../";

        String[] htmlMetaNames = {
                "date",
                "modified",
                "authors",
                "tags",
                "keywords",
                "summary"
        };
        Page page = new Page(PFN);
        page.setMdContent(
                FileIO.getStringFromFile(
                        FileIO.getFilesDir(mContext)
                                + "/md"
                                + PFN
                                + ".md"
                )
        );
        LinkedHashMap<String, String> pageMeta = page.getPageMeta();
        String htmlMeta = "";
        for (String metaName: htmlMetaNames) {
            if (pageMeta.containsKey(metaName)) {
                String metaRealName = metaName;
                if(metaName.equals("authors") && !pageMeta.get(metaName).contains(","))
                    metaRealName = "author";
                htmlMeta += mContext.getString(
                        R.string.html_meta_template,
                        metaRealName,
                        pageMeta.get(metaName)
                );
            }
        }
        String appNameAndVersion = mContext.getResources().getString(R.string.app_name);
        try {
            appNameAndVersion += " "
                    + AppDetails.getAppName(mContext);
        } catch (PackageManager.NameNotFoundException e) {
            MyLog.LogE("Get application version error");
        }
        htmlMeta += mContext.getString(
                R.string.html_meta_template,
                "generator",
                appNameAndVersion
        );

        String tagsMarks = "";
        if (page.hasMetaWithKey("tags")) {
            for (String tg: page.getMetaByKey("tags").split(",")) {
                if (tg.trim().length() >0)
                    tagsMarks += mContext.getString(
                            R.string.html_one_tag_template,
                            // Trim edge spaces and replace
                            // internal spaces to &nbsp;
                            // to prevent split tag when wrap to next line
                            tg.trim().replace(" ", "&nbsp;"),
                            "/Tasg.html#" + tg.trim()
                                    .replace(" ", "-")
                                    .replace("#", "")
                                    .replace("<", "")
                                    .replace(">", "")
                    );
            }
            tagsMarks = mContext.getString(
                    R.string.html_tags_template,
                    tagsMarks);
            //TODO: Write to tag page here
        }


        String htmlTop = mContext.getString(
                R.string.html_top,
                Title,      //Page title
                dirPrefix,  //Reference to top (../*[0..n])
                htmlMeta,   //Summary
				PFN,		//Page name in top control bock
                Title,      //1-st page header as Title and top buttons block controll
                tagsMarks   //Tags
        );
        String htmlBottom = mContext.getString(R.string.html_buttom);
        String htmlPage = htmlTop + html + htmlBottom;
        if(FileIO.isFileExists(mContext, "md"+PFN+".md"))
            FileIO.saveHTML(mContext, PFN, htmlPage);
        Intent i = new Intent();
        i.setAction(mContext.getPackageName()+".REDISPLAY_PAGE");

        mContext.startActivity(i);
    }

    @JavascriptInterface
    public void shortcutButtonCallback(String pn, String title) {

        Toast.makeText(mContext, "Create shortcut to \"" + title + "\"", Toast.LENGTH_SHORT).show();

        Intent shortcutIntent = new Intent(mContext.getApplicationContext(),
                MainActivity.class);
        shortcutIntent.putExtra("page_name", pn);
        shortcutIntent.setAction(Intent.ACTION_MAIN);


        if(Build.VERSION.SDK_INT >= 26) {
            ShortcutManager mShortcutManager =
                    mContext.getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(mContext, pn)
                    .setShortLabel(title)
                    .setLongLabel(title)
                    .setIcon(Icon.createWithResource(mContext, R.mipmap.omn_ic_shortcut))
                    .setIntent(shortcutIntent)
                    .build();
            if (mShortcutManager.isRequestPinShortcutSupported()) {
                Intent pinnedShortcutCallbackIntent =
                        mShortcutManager.createShortcutResultIntent(shortcut);
                PendingIntent successCallback = PendingIntent.getBroadcast(mContext, 0,
                        pinnedShortcutCallbackIntent, 0);

                mShortcutManager.requestPinShortcut(shortcut,
                        successCallback.getIntentSender());
            }

        } else {
            // Code from here: https://stackoverflow.com/a/16873257
            Intent addIntent = new Intent();
            addIntent
                    .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
            addIntent.putExtra(
                    Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(
                            mContext.getApplicationContext(),
                            R.mipmap.omn_ic_shortcut
                    )
            );
            addIntent
                    .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
            mContext.getApplicationContext().sendBroadcast(addIntent);
        }
    }
}
