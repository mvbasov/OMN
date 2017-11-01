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

package net.basov.omn;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.Html;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.LinkedHashMap;

import net.basov.util.FileIO;

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
        // TODO: remove debug
        //Toast.makeText(mContext, "The BUTTON CREATE pressed! "+PFN, Toast.LENGTH_LONG).show();
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
        // TODO: remove debug
        //Toast.makeText(mContext, "The BUTTON EDIT pressed!", Toast.LENGTH_SHORT).show();

        Intent i = new Intent();
        i.setAction(mContext.getPackageName()+".HOME_PAGE");

        mContext.startActivity(i);
    }

    @JavascriptInterface
    public void emailButtonCallback(String pn, String title) {

        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
//        i.setType("text/html");

        // Help user send platform statistic to correct dev. address
        if (pn.equals("/default/Build"))
            // Simplest defence from spamer - put e-mail address in different part of code
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.EMA + "@" + Constants.EMA_DOM});

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
        mContext.startActivity(Intent.createChooser(intent, "Open pages folder"));
        //mContext.startActivity(intent);
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
    public void saveHTML(String html, String PFN, String Title) {
        Toast.makeText(mContext, "Save HTML.", Toast.LENGTH_SHORT).show();
        //UI ui = ((UI) mContext.getApplicationContext()).getInstance();
        //UI ui = UI.getInstance();

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
                        R.string.html_meta_trmplate,
                        metaRealName,
                        pageMeta.get(metaName)
                );
            }
        }

        String htmlTop = mContext.getString(
                R.string.html_top,
                Title,      //Page title
                dirPrefix,  //Reference to top (../*[0..n])
                htmlMeta,         //Summary
				PFN,		//Page name in top control bock
                Title       //1-st page header as Title and top buttons block controll
        );
        String htmlBottom = mContext.getString(R.string.html_buttom);
        String htmlPage = htmlTop + html + htmlBottom;
        if(FileIO.isFileExists(mContext, "md"+PFN+".md"))
            FileIO.saveHTML(mContext, PFN, htmlPage);
        //ui.setHTML(htmlPage);
        //ui.setmHTMLReady(true);
        Intent i = new Intent();
        i.setAction(mContext.getPackageName()+".REDISPLAY_PAGE");

        mContext.startActivity(i);
    }
    
    @JavascriptInterface
    public void shortcutButtonCallback(String pn, String title) {

        Toast.makeText(mContext, "Create shortcut to \"" + title + "\"", Toast.LENGTH_SHORT).show();

        // Code from here: https://stackoverflow.com/a/16873257
        Intent shortcutIntent = new Intent(mContext.getApplicationContext(),
                                           MainActivity.class);
        shortcutIntent.putExtra("page_name", pn);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

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
