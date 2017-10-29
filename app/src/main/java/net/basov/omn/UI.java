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

import java.util.Stack;

import net.basov.util.FileIO;
//import net.basov.util.MyLog;
import net.basov.util.TextTools;

/**
 * Created by mvb on 8/1/17.
 */

public class UI {

    private String mHTML;

    public boolean ismHTMLReady() {
        return mHTMLReady;
    }

    public void setmHTMLReady(boolean mHTMLReady) {
        this.mHTMLReady = mHTMLReady;
    }

    private boolean mHTMLReady;

    private Stack<Page> pages = null;

    private static UI mInstance = null;

    private UI() { dataClean(); }

    public static UI getInstance(){
        if(mInstance == null)
        {
            mInstance = new UI();
        }
        return mInstance;
    }

    public void dataClean () {
        pages = new Stack<Page>();
    }
    
    public void setHTML(String html){
        mHTML = html;
        setmHTMLReady(true);
    }

    public void setMdContent (String content) {
        pages.peek().setMdContent(content);
    }

    public void setPageName(String pageName) {
        if (pages.empty())
            pages.push(new Page(pageName));
        else if (!pageName.equals(pages.peek().getPageName()))
            pages.push(new Page(pageName));
    }
    
    public boolean backPage(final WebView wv){
        if(pages.empty()) return false;
        pages.pop();
        if(pages.empty()) return false;
        //Log.d(MainActivity.TAG, "Call DP from UI backPage");
        displayPage(wv);
        return true;
    }

    public String getMdContent () {return pages.peek().getMdContent();}

    public String getPageName() {
        return pages.peek().getPageName();
    }

    public Page getPage() {
        return pages.peek();
    }

    public String getInPageReference(){
        return pages.peek().getInPageReference();
    }

    public String getPageTitle() {
        return pages.peek().getMetaTitle();
    }
    
    public static void displayStartPage(UI ui, WebView wv) {
        String goPage = "/" + Constants.WELCOME_PAGE;
        if (FileIO.isFileExists(wv.getContext(), "/md/" + Constants.START_PAGE + ".md"))
            goPage = "/" + Constants.START_PAGE;
        ui.setPageName(goPage);
        //MyLog.LogD("Call DSP from displayStartPage "+ ui.getPageName());
        ui.displayPage(wv);
    }

    public void displayPage(final WebView wv) {

        Context c = wv.getContext();
        // TODO: remove debug
        //Toast.makeText(c, "Get page name: " + getPageName(), Toast.LENGTH_SHORT).show();
        wv.setWebViewClient(new MyWebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(wv, url);
                Context c = wv.getContext();
                SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(c);
                String setPageJS = view.getContext()
                        .getString(
                                R.string.set_html_page_js,
                                getPageName(),
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    view.evaluateJavascript(setPageJS, null);
                } else {
                    view.loadUrl(setPageJS);
                }
                wv.clearCache(true);
                wv.clearHistory();
            }
        });
        setmHTMLReady(true);
        if(!FileIO.isPageActual(c, getPageName())) {
            // TODO: remove debug
            //Toast.makeText(c, "Page not actual!", Toast.LENGTH_SHORT).show();
            setmHTMLReady(false);
            createHTML(wv);
        }
        //String delim = "";
        //if(getPageName().charAt(0) != '/') delim = "/";
        if(ismHTMLReady() && FileIO.isFileExists(c, "md/" + getPageName() + ".md")) {
            String htmlFileName = "file://" + FileIO.getFilesDir(c) + "/html" + getPageName() + ".html" + getInPageReference();
            // TODO: remove debug
            //Log.d(MainActivity.TAG,"DP file name: " + htmlFileName + ", PN: " + getPageName());
            //Toast.makeText(c, htmlFileName, Toast.LENGTH_SHORT).show();
            wv.clearCache(true);
            wv.loadUrl(htmlFileName);
        }

    }

    public boolean createHTML(final WebView wv) {
        Context c = wv.getContext();
        setMdContent("");
        String mdContent = FileIO.getStringFromFile(
                FileIO.getFilesDir(wv.getContext())+ "/md" +getPageName() + ".md"
        );
        if (mdContent.length() == 0 ) {
            wv.setWebViewClient(new MyWebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(wv, url);
                    Context c = wv.getContext();
                    SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(c);
                    String setPageJS = "";
                    setPageJS = view.getContext()
                            .getString(
                                    R.string.set_md_page_js_create,

                                    getPageName()
                            );
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
            setMdContent(mdContent);

            wv.setWebViewClient(new MyWebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(wv, url);
                    Context c = wv.getContext();
                    SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(c);
                    String setPageJS = "";
                    setPageJS = view.getContext()
                            .getString(
                                    R.string.set_md_page_js,

                                    getPageName(),
                                    getPageTitle(),
                                    TextTools.escapeJavaScriptFunctionParameter(getMdContent()),
                                    //Enable highlight
                                    defSharedPref.getBoolean(
                                            c.getString(R.string.pk_enable_code_highlight),
                                            true
                                    )
                            );

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
