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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.webkit.WebView;
//import android.widget.Toast;

import net.basov.util.FileIO;
//import net.basov.util.MyLog;
import net.basov.util.TextTools;

/**
 * Created by mvb on 8/1/17.
 */

public class UI {

    public static void setMdContentFromFile(Context c, Page page) {
        page.setMdContent(FileIO.getStringFromFile(
                FileIO.getFilesDir(c)
                        + "/md"
                        + page.getPageName()
                        + ".md"
        ));
    }
    
    public static String displayStartPage(WebView wv) {
        String goPage = "/" + Constants.WELCOME_PAGE;
        if (FileIO.isFileExists(wv.getContext(), "/md/" + Constants.START_PAGE + ".md"))
            goPage = "/" + Constants.START_PAGE;
        Page page = new Page(goPage);
        //MyLog.LogD("Call DSP from displayStartPage "+ ui.getPageName());
        displayPage(wv, page);
        return page.getPageName();
    }

    public static void displayPage(final WebView wv, final Page page) {

        Context c = wv.getContext();
        String actionButtons = TextTools.escapeJavaScriptFunctionParameter(c.getString(
                R.string.html_action_button_header,
                page.getPageName()
        ));
        SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        final String setPageJS =
                c.getString(
                        R.string.set_html_page_js,
                        page.getPageName(),
                        actionButtons,
                        //Enable Home button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_home),
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
                        //Enable Create shortcut button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_shortcut),
                                true
                        ),
                        //Enable QuickNotes button
                        defSharedPref.getBoolean(
                                c.getString(R.string.pk_btn_enable_quicknotes),
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

        if(!FileIO.isPageActual(c, page.getPageName())) {
            createHTML(wv, page.getPageName());
        } else if(FileIO.isFileExists(c, "md/" + page.getPageName() + ".md")) {
            String htmlFileName = "file://" + FileIO.getFilesDir(c) + "/html" + page.getPageName() + ".html" + page.getInPageReference();
            wv.clearCache(true);
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


}
