/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.omn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.basov.util.FileIO;
import net.basov.util.MyLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

public class MainActivity extends Activity {

    private WebView mainUI_WV;
    private Page page;
    private String pageName;
    private Stack<String> pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int versionCode = 0;
        int oldVersion = 0;
        
        /* Set default preferences at first run and after preferences version upgrade */
        SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defSharedPref.edit();
        final int currePrefVersion = 7;
        switch (defSharedPref.getInt(getString(R.string.pk_pref_version), 0)) {
            case 0: // initial
                editor.putBoolean(getString(R.string.pk_use_view_directory), false);
                editor.putBoolean(getString(R.string.pk_btn_enable_home), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_link), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_email), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_filemanager), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_shortcut), true);
                editor.putBoolean(getString(R.string.pk_pref_changed), false);
                editor.putBoolean(getString(R.string.pk_enable_code_highlight), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_quicknotes), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_add_page), true);
                editor.putInt(getString(R.string.pk_pref_version), currePrefVersion);
                editor.commit();
                break;
            case 1: //Upgrade to properties version 2
                editor.putBoolean(getString(R.string.pk_btn_enable_home), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_link), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_email), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_filemanager), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_shortcut), true);
                editor.putBoolean(getString(R.string.pk_pref_changed), false);
                editor.putBoolean(getString(R.string.pk_enable_code_highlight), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_quicknotes), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_add_page), true);
                editor.putInt(getString(R.string.pk_pref_version), currePrefVersion);
                editor.commit();
                break;
            case 2:
                editor.putBoolean(getString(R.string.pk_btn_enable_shortcut), true);
                editor.putBoolean(getString(R.string.pk_pref_changed), false);
                editor.putBoolean(getString(R.string.pk_enable_code_highlight), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_quicknotes), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_add_page), true);
                editor.putInt(getString(R.string.pk_pref_version), currePrefVersion);
                editor.commit();
                break;
            case 3:
                editor.putBoolean(getString(R.string.pk_pref_changed), false);
                editor.putBoolean(getString(R.string.pk_enable_code_highlight), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_quicknotes), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_add_page), true);
                editor.putInt(getString(R.string.pk_pref_version), currePrefVersion);
                editor.commit();
                break;
            case 4:
                editor.putBoolean(getString(R.string.pk_enable_code_highlight), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_quicknotes), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_add_page), true);
                editor.putInt(getString(R.string.pk_pref_version), currePrefVersion);
                editor.commit();
                break;
            case 5:
                editor.putBoolean(getString(R.string.pk_btn_enable_quicknotes), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_add_page), true);
                editor.putInt(getString(R.string.pk_pref_version), currePrefVersion);
                editor.commit();
                break;
            case 6:
                editor.putBoolean(getString(R.string.pk_btn_enable_add_page), true);
                editor.putInt(getString(R.string.pk_pref_version), currePrefVersion);
                editor.commit();
                break;

            default:
                break;
        }

        // Check app version upgrade
        try {
            PackageInfo packageInfo = getPackageManager()
                .getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            MyLog.LogE(e, "getPackageManager run problem");
        }
        oldVersion = defSharedPref.getInt(getString(R.string.pk_version_code), 0);
        if (versionCode != 0 && versionCode > oldVersion) {
            runAfterUpdate(oldVersion, versionCode);
            editor.putInt(getString(R.string.pk_version_code), versionCode);
            editor.commit();
        }

        setContentView(R.layout.webview_ui);
        mainUI_WV = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mainUI_WV.getSettings();

        /* Disable using cache */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);
        webSettings.setSaveFormData(false);
        /* Enable JavaScript */
        webSettings.setJavaScriptEnabled(true);
        mainUI_WV.addJavascriptInterface(new WebViewJSCallback(this), "Android");
        /* Show external page in browser */
        mainUI_WV.setWebViewClient(new MyWebViewClient());
        /* Handle JavaScript prompt dialog */
        mainUI_WV.setWebChromeClient(new myWebChromeClient());


        /* Enable chome remote debuging for WebView (Ctrl-Shift-I) */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE))
            { WebView.setWebContentsDebuggingEnabled(true); }
        }

        if (savedInstanceState != null) {
            pageName = savedInstanceState.getString("pageName");
        }

        FileIO.creteHomePage(MainActivity.this);

        onNewIntent(getIntent());
        
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("pageName", pageName);
        outState.putStringArray("pages", pages.toArray(new String[pages.size()]));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            pageName = savedInstanceState.getString("pageName");
            String[] pagesArray = savedInstanceState.getStringArray("pages");
            if(pagesArray != null && pagesArray.length != 0) {
                pages.clear();
                for (String str : pagesArray) {
                    pages.push(str);
                }
            }
        }
    }

    private void runAfterUpdate(int oldVersion, int newVersion) {
        FileIO.creteHomePage(MainActivity.this, true);
        FileIO.deletePage(this, "/" + Constants.BUILD_PAGE);
        Toast.makeText(this, "version upgrade from " + oldVersion + " to " + newVersion, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if(!pageBack()) finish();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        if (intent != null && intent.getAction().equals(Intent.ACTION_MAIN)) {
        /* Show page from shortcut */
            Bundle main_extras = intent.getExtras();
            if(main_extras != null) {
				
				// TODO: remove debug code
				// Debug code from https://stackoverflow.com/a/15074150
				//for (String key : main_extras.keySet()) {
				//	Object value = main_extras.get(key);
				//	MyLog.LogD(String.format("Extras(key::value (type): %s::%s (%s)", key,
				//							 value.toString(), value.getClass().getName()));
				//}
				
                String page_name = (String) main_extras.get("page_name");
                if(page_name != null && page_name.length() > 0) {
                    pageAdd(page_name);
                    UI.displayPage(mainUI_WV, page);
                } else {
					pageAdd(UI.displayStartPage(mainUI_WV));
				}
            } else {
                pageAdd(UI.displayStartPage(mainUI_WV));
            }
        } if (intent != null && intent.getAction().equals(this.getPackageName()+".EDIT_PAGE")) {
            Bundle extras = intent.getExtras();
            if(extras != null) {
                String name = (String) extras.get("name");
                Intent intentE = new Intent(Intent.ACTION_EDIT);
                Uri uri = Uri.parse("file://" + FileIO.getFilesDir(this).getPath() + "/md/" + name + ".md");
                intentE.setDataAndType(uri, "text/plain");
                try {
                    this.startActivityForResult(intentE, Constants.EDIT_PAGE_REQUEST);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(this, "No editor found. Please install one.", Toast.LENGTH_LONG).show();
                }
            }
        } if (intent != null && intent.getAction().equals(this.getPackageName()+".HOME_PAGE")) {
        /* Show home page */
            pageAdd(UI.displayStartPage(mainUI_WV));
        } if (intent != null && intent.getAction().equals(this.getPackageName()+".REDISPLAY_PAGE")) {
        /* Redisplay page after creation */
            UI.displayPage(mainUI_WV, page);
        } if (intent != null && intent.getAction().equals(this.getPackageName()+".PREFERENCES")) {
        /* Redisplay page after creation */
            Intent i = new Intent(this, AppPreferencesActivity.class);
            this.startActivityForResult(i, Constants.PREFERENCES_REQUEST);
        } if (intent != null && intent.getAction().equals(this.getPackageName()+".QUICK_NOTE")) {
        /* QuickNotes creation */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create QuickNote");
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Context c = MainActivity.this;
                    pageAdd("/" + Constants.QUICKNOTES_PAGE);
                    FileIO.createPageIfNotExists(c, "/" + Constants.QUICKNOTES_PAGE, "", "");
                    UI.setMdContentFromFile(c, page);                 
                    final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeStamp = DF.format(new Date());
                    page.setMetaModified(timeStamp);
                    String newText =
                            page.getMetaHeaderAsString()
                            + getString(
                                R.string.template_quick_note,
                                // Time stamp
                                timeStamp,
                                // Note text
                                input.getText().toString()
                            )
                            + page.getMdContent();
                    FileIO.writePageToFile(
                            c,
                            "/" + Constants.QUICKNOTES_PAGE,
                            newText
                    );
                    Intent i = new Intent();
                    i.setAction(c.getPackageName() + ".REDISPLAY_PAGE");
                    c.startActivity(i);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            UI.displayPage(mainUI_WV, page);
        } if (intent != null && intent.getAction().equals(this.getPackageName()+".NEW_PAGE")) {
        /* Create new page and put link to it on top of current page */
            final String currentPageName = page.getPageName();

            AlertDialog.Builder builderName = new AlertDialog.Builder(this);
            builderName.setTitle("New page (file) name");
            final EditText inputName = new EditText(this);
            builderName.setView(inputName);
            builderName.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String newPageNameEntered = inputName.getText().toString().trim();
                    final String newPageName = currentPageName.substring(0,currentPageName.lastIndexOf("/") + 1) + newPageNameEntered;
                    if (FileIO.isFileExists(MainActivity.this, "/md/" + newPageName + ".md")) {
                        Toast.makeText(MainActivity.this, "Page already exists", Toast.LENGTH_SHORT).show();
                        //TODO: implement add link to current page if page exists
                    } else {
                        AlertDialog.Builder builderTitle = new AlertDialog.Builder(MainActivity.this);
                        builderTitle.setTitle("New page title");
                        final EditText inputTitle = new EditText(MainActivity.this);
                        inputTitle.setText(newPageNameEntered, TextView.BufferType.EDITABLE);
                        builderTitle.setView(inputTitle);
                        builderTitle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                final String timeStamp = DF.format(new Date());
                                UI.setMdContentFromFile(MainActivity.this, page);
                                page.setMetaModified(timeStamp);
                                final String currentMeta = page.getMetaHeaderAsString();
                                final String currentContent =  page.getMdContent();

                                String newPageTitle = inputTitle.getText().toString().trim();
                                String linkToNewPage = String.format("* [%s](%s.html)\n", newPageTitle, newPageNameEntered);
                                if (currentMeta != null && currentContent != null) {
                                    String newText =
                                            currentMeta
                                                    + linkToNewPage
                                                    + currentContent;
                                    FileIO.writePageToFile(
                                            MainActivity.this,
                                            currentPageName,
                                            newText
                                    );
                                } else {
                                    String stackState;
                                    if (pages == null)
                                        stackState = "null";
                                    else if (pages.empty())
                                        stackState = "empty";
                                    else
                                        stackState = pages.peek();
                                    Toast.makeText(MainActivity.this, "Internal programm error. Link to page didn't created\n * page = " + pageName + "\n * pages.peek = " + stackState , Toast.LENGTH_SHORT).show();
                                }

                                FileIO.createPageIfNotExists(MainActivity.this, newPageName,newPageTitle, "");

                                pageAdd(newPageName);

                                Intent i = new Intent();
                                i.setAction(MainActivity.this.getPackageName()+".EDIT_PAGE");
                                i.putExtra("name", "/"+newPageName);
                                MainActivity.this.startActivity(i);

                            }

                        });
                        builderTitle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builderTitle.show();
                    }
                }
            });
            builderName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builderName.show();

            UI.displayPage(mainUI_WV, page);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.EDIT_PAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                UI.displayPage(mainUI_WV, page);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //TODO: Why this result code actual?
                //MyLog.LogD("* Call DP from ma onActivityResult(CANCELED), PN: " + ui.getPageName());
                UI.displayPage(mainUI_WV, page);
            }
        } else if (requestCode == Constants.PREFERENCES_REQUEST) {
            SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            if (defSharedPref.getBoolean(getString(R.string.pk_pref_changed), false)) {
                SharedPreferences.Editor editor = defSharedPref.edit();
                UI.displayPage(mainUI_WV, page);
                editor.putBoolean(getString(R.string.pk_pref_changed), false);
                editor.commit();
            }
        }
    }

    /**
     * Page stack controll functions
     */

    private void pageAdd(String pageName) {
        this.pageName = pageName;
        if (pages == null) pages = new Stack<String>();
        if (pages.empty()) {
            pages.push(pageName);
            page = new Page(pageName);
        } else if (!pages.peek().equals(pageName)) {
            pages.push(pageName);
            page = new Page(pageName);
        }
    }

    public boolean pageBack(){
        if(pages.empty()) return false;
        pages.pop();
        if(pages.empty()) return false;
        //Log.d(MainActivity.TAG, "Call DP from UI backPage");
        page = new Page(pages.peek());
        UI.displayPage(mainUI_WV, page);
        return true;
    }

    /**
     * Handle JavaScript console log
     */
    private class myWebChromeClient extends WebChromeClient {

        public boolean onConsoleMessage(ConsoleMessage cm) {
            String formattedMessage =
                    cm.message()
                    + " -- From line: "
                    + cm.lineNumber()
                    + " of "
                    + cm.sourceId();
            switch (cm.messageLevel()) {
                case DEBUG:
                    MyLog.LogD(formattedMessage);
                    break;
                case ERROR:
                    MyLog.LogE(formattedMessage);
                    break;
                case LOG:
                    MyLog.LogI(formattedMessage);
                    break;
                case TIP:
                    MyLog.LogI(formattedMessage);
                    break;
                case WARNING:
                    MyLog.LogW(formattedMessage);
                    break;
                default:
                    MyLog.LogW(formattedMessage);
                    break;
            }
            return true;
        }
    }
}
