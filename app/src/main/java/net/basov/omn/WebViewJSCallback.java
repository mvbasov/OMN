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

import net.basov.util.FileIO;

public class WebViewJSCallback {

    private Context mContext;

    WebViewJSCallback(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void prefButonCallback() {
        Intent i = new Intent(mContext, AppPreferencesActivity.class);
        mContext.startActivity(i);
    }


    @JavascriptInterface
    public void createButtonCallback(String PFN) {
        // TODO: remove debug
        //Toast.makeText(mContext, "The BUTTON CREATE pressed! "+PFN, Toast.LENGTH_LONG).show();
        FileIO.createPageIfNotExists(mContext, PFN);

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

        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(title, "["+title+"]("+pn+".html)");
        clipboard.setPrimaryClip(clip);
    }

    @JavascriptInterface
    public void saveHTML(String html, String PFN, String Title) {
        Toast.makeText(mContext, "Save HTML.", Toast.LENGTH_SHORT).show();
        UI ui = UI.getInstance();

        /* Make directory prefix for right css reference */
        int dirCount = PFN.length() - PFN.replaceAll("/","").length();
        String dirPrefix = "";
        if(PFN.charAt(0) == '/') dirCount--;
        for(int i=0; i < dirCount; i++) dirPrefix += "../";

        String htmlTop = mContext.getString(
                R.string.html_top,
                Title,      //Page title
                dirPrefix,  //Reference to top (../*[0..n])
                "",         //Tags
                "",         //Date
                "",         //Modified
                "",         //Category
                "",         //Authors
                "",         //Summary
				PFN,		//Page name in top control bock
                Title       //1-st page header as Title and top buttons block controll
        );
        String htmlBottom = mContext.getString(R.string.html_buttom);
        String htmlPage = htmlTop + html + htmlBottom;
        if(FileIO.isFileExists(mContext, "md/"+ui.getPageName()+".md"))
            FileIO.saveHTML(mContext, ui.getPageName(), htmlPage);
        ui.setHTML(htmlPage);
        ui.setmHTMLReady(true);
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
                        R.mipmap.omn_ic
				)
		);
        addIntent
            .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
        mContext.getApplicationContext().sendBroadcast(addIntent);
    }
}
