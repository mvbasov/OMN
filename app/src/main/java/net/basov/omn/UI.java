package net.basov.omn;

import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

import java.util.Stack;

import net.basov.util.FileIO;
import net.basov.util.MyLog;
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
//        String str = pages.peek().getPageName();
//        if (null != str && str.length() > 0 ) {
//            int endIndex = str.lastIndexOf("#");
//            if (endIndex != -1){
//                return str.substring(0, endIndex);
//            }
//            return str;
//        }
//        return str;
    }

    public String getInPageReference(){
        return pages.peek().getmInPageReference();
//        String str = pages.peek().getPageName();
//        if (null != str && str.length() > 0 ) {
//            int endIndex = str.lastIndexOf("#");
//            if (endIndex != -1){
//                return str.substring(endIndex);
//            }
//        }
//        return "";
    }

    public String getPageTitle() {
        return pages.peek().getMetaTitle();
    }
    
    public static void displayStartPage(UI ui, WebView wv) {
        String goPage = "/" + Constants.WELCOME_PAGE;
        if (FileIO.isFileExists(wv.getContext(), "/md/" + Constants.START_PAGE + ".md"))
            goPage = "/" + Constants.START_PAGE;
        ui.setPageName(goPage);
        MyLog.LogD("Call DSP from displayStartPage "+ ui.getPageName());
        ui.displayPage(wv);
    }

    public void displayPage(final WebView wv) {

        Context c = wv.getContext();
        //Toast.makeText(c, "Get page name: " + getPageName(), Toast.LENGTH_SHORT).show();
        wv.setWebViewClient(new MyWebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                String setPageJS = view.getContext()
                        .getString(
                                R.string.set_html_page_js,
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
        setmHTMLReady(true);
        if(!FileIO.isPageActual(c, getPageName())) {
            //Toast.makeText(c, "Page not actual!", Toast.LENGTH_SHORT).show();
            setmHTMLReady(false);
            createHTML(wv);
        }
        String delim = "";
        if(getPageName().charAt(0) != '/') delim = "/";
        if(ismHTMLReady() && FileIO.isFileExists(c, "md/" + getPageName() + ".md")) {
            String htmlFileName = "file://" + FileIO.getFilesDir(c) + "/html" + delim +getPageName() + ".html" + getInPageReference();
            //Log.d(MainActivity.TAG,"DP file name: " + htmlFileName + ", PN: " + getPageName());
            //Toast.makeText(c, htmlFileName, Toast.LENGTH_SHORT).show();
            wv.clearCache(true);
            wv.loadUrl(htmlFileName);
        }

    }

    public boolean createHTML(final WebView wv) {
        Context c = wv.getContext();
        setMdContent("");
        String delim = "";
        if(getPageName().charAt(0) != '/') delim = "/";
        setMdContent( FileIO.getStringFromFile(
                FileIO.getFilesDir(wv.getContext())+ "/md" + delim +getPageName() + ".md")
        );
        wv.setWebViewClient(new MyWebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(wv, url);
                //Toast.makeText(wv.getContext(),mdContent,Toast.LENGTH_SHORT).show();
                String setPageJS = "";
                if (getMdContent().length() == 0) {
                    setPageJS = view.getContext()
                            .getString(
                                    R.string.set_md_page_js_create,
                                    getPageName(),
                                    TextTools.escapeCharsForJSON(getMdContent()),
                                    1 //Enable highlight
                            );
                } else {
                    setPageJS = view.getContext()
                            .getString(
                                    R.string.set_md_page_js,
                                    getPageName(),
                                    getPageTitle(),
                                    TextTools.escapeCharsForJSON(getMdContent()),
                                    1 //Enable highlight
                            );

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    view.evaluateJavascript(setPageJS, null);

                } else {
                    view.loadUrl(setPageJS);
                }
                wv.clearCache(true);
                wv.clearHistory();
                //Toast.makeText(wv.getContext(),"URL is: "+url,Toast.LENGTH_SHORT).show();
            }
        });
        wv.clearCache(true);
        wv.loadUrl("file:///android_asset/html/" + c.getString(R.string.anvil_file));

        return true;

    }


}
