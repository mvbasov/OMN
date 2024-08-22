/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017-2024 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.omn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.webkit.WebView;
//import android.widget.Toast;

import net.basov.util.FileIO;
//import net.basov.util.MyLog;
import net.basov.util.TextTools;
import android.widget.*;

/**
 * Created by mvb on 8/1/17.
 */

public class UI {

    /**
     * Display start page
     * If /Start exists display it else display /default/Welcome
     * return name of displayed page
     */
    public static String displayStartPage(WebView wv) {
        String goPage = "/" + Constants.WELCOME_PAGE.replace("_legacy", "");
        if (FileIO.isFileExists(wv.getContext(), "/md/" + Constants.START_PAGE + ".md"))
            goPage = "/" + Constants.START_PAGE;
        Page page = new Page(goPage);
        displayPage(wv, page);
        return page.getPageName();
    }

    public static void displayPage(final WebView wv, final Page page) {

        Context c = wv.getContext();
        //Toast.makeText(c, page.getPageName(), Toast.LENGTH_LONG).show();
        if(!FileIO.isPageActual(c, page.getPageName()) || !page.isHtmlActual()) {
            createHTML(wv, page.getPageName());
            page.setHtmlActual(true);
        } else if(FileIO.isFileExists(c, "md/" + page.getPageName() + ".md")) {
            // TODO: Only tags display dosn't shown without the following string. But add this decrease performance (may be)
            setMdContentFromFile(c, page);
            String actionButtons = TextTools.escapeJavaScriptFunctionParameter(c.getString(
                    R.string.html_action_button_header
            ));
            SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(c);
            final String setPageJS =
                c.getString(
                        R.string.set_html_page_js,
                        c.getPackageName(),
                        page.getPageName(),
                        actionButtons,
                        //Enable Home button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_home),
                                true
                        ),
                        //Enable Debug button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_enable_js_debug),
                                false
                        ),
                        //Enable Add page button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_add_page),
                                true
                        ),
                        //Enable Link button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_link),
                                true
                        ),
                        //Enable E-Mail button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_email),
                                true
                        ),
                        //Enable File browser button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_filemanager),
                                true
                        ),

                        //Enable Refresh HYML button
                        defSharedPref.getBoolean(
                            c.getString(R.string.pk_btn_enable_refresh_html),
                            true
                        ),
                        //Enable Create shortcut button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_shortcut),
                                true
                        ),
                        //Enable QuickNotes button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_quicknotes),
                                true
                        ),
                        //Enable Send button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_send),
                                true
                        )

                );
            wv.setWebViewClient(new MyWebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(wv, url);
                    wv.clearCache(true);
                    wv.clearHistory();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        view.evaluateJavascript(setPageJS, null);
                    } else {
                        view.loadUrl(setPageJS);
                    }
                }
            });

            String htmlFileName = "file://" + FileIO.getFilesDir(c) + "/html" + page.getPageName() + ".html" + page.getInPageReference();
            wv.clearCache(true);
            wv.clearHistory();
            wv.loadUrl(htmlFileName);
        }

    }

    public static boolean createHTML(final WebView wv, final String pageName) {
        Context c = wv.getContext();
        final String mdContent = FileIO.getStringFromFile(
                FileIO.getFilesDir(wv.getContext())+ "/md" + pageName + ".md"
        );
        if (mdContent.length() == 0 ) {
            final String setPageJS =
                    c.getString(
                            R.string.set_md_page_js_create,
                            c.getPackageName(),
                            pageName
                    );
            wv.setWebViewClient(new MyWebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(wv, url);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        view.evaluateJavascript(setPageJS, null);

                    } else {
                        view.loadUrl(setPageJS);
                    }
                    wv.clearCache(true);
                    wv.clearHistory();
                }
            });
            wv.clearCache(true);
            wv.loadUrl("file:///android_asset/html/" + c.getString(R.string.create_file));
        } else {

            SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(c);

            Page page = new Page(pageName);
            page.setMdContent(mdContent);
            String pageTitle = page.getPageTitleOrName();
            String mdBody = TextTools.escapeJavaScriptFunctionParameter(page.getMdContent());

            final String setPageJS =
                    c.getString(
                            R.string.set_md_page_js,
                            c.getPackageName(),
                            pageName,
                            pageTitle,
                            mdBody,
                            //Enable highlight
                            defSharedPref.getBoolean(
                                    c.getString(R.string.pk_enable_code_highlight),
                                    true
                            )
                    );
            wv.setWebViewClient(new MyWebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(wv, url);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        view.evaluateJavascript(setPageJS, null);

                    } else {
                        view.loadUrl(setPageJS);
                    }
                    wv.clearCache(true);
                    wv.clearHistory();
                }
            });
            wv.clearCache(true);
            wv.loadUrl("file:///android_asset/html/" + c.getString(R.string.anvil_file));
        }
        return true;
    }

    /**
     * In addition to main function UI class is some "isolation layer" to make Page class pure Java code.
     * "Isolation" methods are:
     *      setMdContentFromFile
     *      setTSFromFile
     * And may be:
     *      createHTML
     */

    public static void setMdContentFromFile(Context c, Page page) {
        String pageFileName = FileIO.getFilesDir(c)
                + "/md"
                + page.getPageName()
                + ".md";
        page.setMdContent(FileIO.getStringFromFile(
                pageFileName
        ));
    }

    public static void setTSFromFile(Context c, Page page) {
        page.setFileTS(FileIO.getPageFileTS(c, page.getPageName()));
    }

}
