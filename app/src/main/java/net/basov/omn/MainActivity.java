/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.omn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Stack;

import android.support.v4.content.FileProvider;
import net.basov.util.FileIO;
import net.basov.util.MyLog;
import net.basov.util.TextTools;

public class MainActivity extends Activity {

    private WebView mainUI_WV;
    private Page page;
    private String pageName;
    private Stack<String> pages;
    boolean backPressedRecently = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int versionCode = 0;
        int oldVersion = 0;
        
        /*
        Set default preferences at first run and after preferences version upgrade

        For change preferences increase currentPrefVersion

        break; line not missing in switch/case and all statements need to run
        from previous currentPrefVersion value

        Yes, I know about PreferenceManager.setDefaultValues(...
         */
        SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defSharedPref.edit();
        final int currentPrefVersion = 14;
        switch (defSharedPref.getInt(getString(R.string.pk_pref_version), 0)) {
            case 0: // initial
                editor.putBoolean(getString(R.string.pk_use_view_directory), false);
            case 1: //Upgrade to properties version 2
                editor.putBoolean(getString(R.string.pk_btn_enable_home), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_link), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_email), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_filemanager), true);
            case 2:
                editor.putBoolean(getString(R.string.pk_btn_enable_shortcut), true);
            case 3:
                editor.putBoolean(getString(R.string.pk_pref_changed), false);
            case 4:
                editor.putBoolean(getString(R.string.pk_enable_code_highlight), true);
            case 5:
                editor.putBoolean(getString(R.string.pk_btn_enable_quicknotes), true);
            case 6:
                editor.putBoolean(getString(R.string.pk_btn_enable_add_page), true);
            case 7:
                editor.putBoolean(getString(R.string.pk_enable_pelican_meta), true);
            case 8:
                editor.putBoolean(getString(R.string.pk_btn_enable_refresh_html), false);
            case 9:
                editor.putBoolean(getString(R.string.pk_enable_js_debug), false);
            case 10:
                editor.putBoolean(getString(R.string.pk_enable_js_web_db), false);
                editor.putBoolean(getString(R.string.pk_enable_js_local_storage), false);
            case 11:
                editor.putBoolean(getString(R.string.pk_enable_intent_uri),false);
            case 12:
                editor.putBoolean(getString(R.string.pk_enable_termux_intent_uri),false);
            case 13:
                editor.putBoolean(getString(R.string.pk_btn_enable_send),false);
                editor.putInt(getString(R.string.pk_pref_version), currentPrefVersion);
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
        webSettings.setAllowFileAccess(true);
        mainUI_WV.addJavascriptInterface(new WebViewJSCallback(this), "Android");
        /* Show external page in browser */
        //mainUI_WV.setWebViewClient(new MyWebViewClient());
        /* Enable WebDB */
        //webSettings.setJavaScriptCanOpenWindowsAutomatically(true); 
        if (defSharedPref.getBoolean(getString(R.string.pk_enable_js_web_db), false)) {
            webSettings.setDatabaseEnabled(true);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                webSettings.setDatabasePath(FileIO.getFilesDir(MainActivity.this).getPath() + "/db/");
            }
        }
        /* Enable localStorage in WebView */
        if (defSharedPref.getBoolean(getString(R.string.pk_enable_js_local_storage), false)) {
            webSettings.setDomStorageEnabled(true);
        }
        /* Handle JavaScript prompt dialog */
        mainUI_WV.setWebChromeClient(new myWebChromeClient());


        /* Enable chrome remote debugging for WebView (Ctrl-Shift-I) */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE))
            { WebView.setWebContentsDebuggingEnabled(true); }
        }

        if (savedInstanceState != null) {
            pageName = savedInstanceState.getString("pageName");
        }

        FileIO.creteHomePage(MainActivity.this);

        //Add pined shortcut for Android 8.0 and later
        if(Build.VERSION.SDK_INT >= 25) {
            Intent shortcutIntent = new Intent(this.getApplicationContext(),
                    MainActivity.class);
            shortcutIntent.setAction(this.getPackageName() + ".QUICK_NOTE");
            ShortcutManager shortcutManager = this.getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "QuickNote")
                    .setShortLabel("QuickNote")
                    .setLongLabel("Take QuickNote")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_baseline_comment_24px))
                    .setIntent(shortcutIntent)
                    .build();
            // Dynamic shortcut
            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
        }

        requestTermuxPermission(defSharedPref);

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
                    if (backPressedRecently) {
                        Toast.makeText(
                                this, 
                                "'Back' quickly pressed twice.\nExit application.", 
                                Toast.LENGTH_SHORT
                        ).show();
                        finish();
                    }
                    this.backPressedRecently = true;     

                    new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                backPressedRecently = false;                       
                            }
                        },
                        500 // milliseconds to treat Back pressed twice
                    );
                        
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
        if (intent != null) {
            if (Intent.ACTION_MAIN.equals(intent.getAction())) {
                /* Show page from old shortcut or start from launcher */
                Bundle main_extras = intent.getExtras();
                if (main_extras != null) {

                    // TODO: remove debug code
                    // Debug code from https://stackoverflow.com/a/15074150
                    //for (String key : main_extras.keySet()) {
                    //	Object value = main_extras.get(key);
                    //	MyLog.LogD(String.format("Extras(key::value (type): %s::%s (%s)", key,
                    //							 value.toString(), value.getClass().getName()));
                    //}

                    String page_name = (String) main_extras.get("page_name");
                    if (page_name != null && page_name.length() > 0) {
                        pageAdd(page_name);
                        UI.displayPage(mainUI_WV, page);
                    } else {
                        pageAdd(UI.displayStartPage(mainUI_WV));
                    }
                } else {
                    pageAdd(UI.displayStartPage(mainUI_WV));
                }
            }
            if ((this.getPackageName() + ".EDIT_PAGE").equals(intent.getAction())) {
                /* Call external editor to edit page */
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    Uri uri = null;
                    String name = (String) extras.get("name");
                    UI.setTSFromFile(MainActivity.this, page);
                    Intent intentE = new Intent(Intent.ACTION_EDIT);
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                        uri = Uri.parse("file://"
                                + FileIO.getFilesDir(MainActivity.this).getPath()
                                + "/md"
                                + name
                                + ".md"
                        );
                        intentE.setDataAndType(uri, "text/plain");
                    } else {
                        File pageDir = new File(FileIO.getFilesDir(MainActivity.this), "md");
                        File pageFile = new File(pageDir, name + ".md");
                        uri = FileProvider.getUriForFile(this, this.getPackageName(), pageFile);
                        intentE.setDataAndType(uri, "text/plain");
                        intentE.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
//                List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(intentE, PackageManager.MATCH_DEFAULT_ONLY);
//                for (ResolveInfo resolveInfo : resInfoList) {
//                    String packageName = resolveInfo.activityInfo.packageName;
//                    this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    MyLog.LogD(packageName);
//                }
                    try {
                        this.startActivityForResult(intentE, Constants.EDIT_PAGE_REQUEST);
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(this,
                                "No editor found. Please install one.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            }
            if ((this.getPackageName() + ".HOME_PAGE").equals(intent.getAction())) {
                /* Show home page */
                pageAdd(UI.displayStartPage(mainUI_WV));
            }
            if ((this.getPackageName() + ".DEBUG_PAGE").equals(intent.getAction())) {
                /* Show JavaScript debug page */
                pageAdd("/"+Constants.JS_DEBUG_PAGE);
                UI.displayPage(mainUI_WV, page);
            }
            if ((this.getPackageName() + ".REDISPLAY_PAGE").equals(intent.getAction())) {
                /* Redisplay page after creation */
                if(intent.getBooleanExtra("RECREATE_HTML", false)) page.setHtmlActual(false);
                UI.displayPage(mainUI_WV, page);
            }
            if ((this.getPackageName() + ".PREFERENCES").equals(intent.getAction())) {
                /* Redisplay page after creation */
                Intent i = new Intent(this, AppPreferencesActivity.class);
                this.startActivityForResult(i, Constants.PREFERENCES_REQUEST);
            }
            if ((this.getPackageName() + ".QUICK_NOTE").equals(intent.getAction())
                    || Intent.ACTION_SEND.equals(intent.getAction())
                    || Intent.ACTION_SENDTO.equals(intent.getAction())) {
                /* QuickNotes creation */
                //AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //builder.setTitle("Create QuickNote");
                //final EditText input = new EditText(this);

                // Get shared text
				String link = "";
                String urlLink = "";
                String urlTitle = "";
                String noteText = "";
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                String sharedSubject = intent.getStringExtra(Intent.EXTRA_SUBJECT);
                String sharedHTML = intent.getStringExtra(Intent.EXTRA_HTML_TEXT);
                int endOfFirstLine = 0;
                Page importedPage = null;
                //Page shared as file
                if ( intent.hasExtra(Intent.EXTRA_STREAM)){
                    //MyLog.LogD("Intent has EXTRA_STREAM");
                    Uri streamuri = (Uri)intent.getExtras().get(Intent.EXTRA_STREAM);
                    try {
                        if (streamuri != null) {
                            importedPage = FileIO.importPage(MainActivity.this,
                                    getContentResolver().openInputStream(streamuri));
                        }
                        if (importedPage == null) {
                            fallbackDisplayStartPage("Page can't be imported...");
                            return;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (importedPage != null) {
                        checkAndImportPage(importedPage);
                    } else {
                        if (streamuri.toString().startsWith("file:")) {
                            fallbackDisplayStartPage("file:// uri scheme not supported. ContentProvider required.\n\nFile can't be imported...");
                        } else {
                            fallbackDisplayStartPage("Page can't be imported...");
                        }
                    }
                    return;
                }
                if (sharedHTML != null) {
                    noteText += sharedHTML;
                } else {
                    if (sharedSubject != null) {
                        noteText += "#### " + sharedSubject + "\n";
						link += "* [" + sharedSubject + "]";
                        urlTitle = sharedSubject;
                    }
                    if (sharedText != null) {           
						if (sharedText.startsWith("http")) {
							if (sharedText.contains("\n")) {
                                endOfFirstLine = sharedText.indexOf("\n");
							    link += sharedText.substring(0, endOfFirstLine);
                                urlLink = sharedText.substring(0, endOfFirstLine);
							} else
						        link += "(" + sharedText +")\n";
                                urlLink = sharedText;
                            if (endOfFirstLine != 0)
                                noteText = sharedText.substring(sharedText.length() - endOfFirstLine);
                            else
                                noteText = "";
                            if(!urlLink.isEmpty()){
                                showUrlImportDialog(urlTitle, urlLink);
                                return;
                             }
						} else if( sharedText.startsWith(Constants.EMA_H_VER + ": ")
                                || sharedText.startsWith(Constants.EMA_H_SUBJECT)
                        ) {
                            if ((importedPage = FileIO.importPage(MainActivity.this, sharedText)) == null) {
                                fallbackDisplayStartPage("Page can't be imported...");
                            } else {
                                checkAndImportPage(importedPage);
                            }
                            return;
                        } else {
							link = "";
                            noteText = sharedText;
						}                     
						noteText = link + noteText;
                    }
                }
 
                showQuickNoteDialog(noteText);

                //Set start page before display if called from dynamic shortcut
                if (page == null)
                    pageAdd(UI.displayStartPage(mainUI_WV));
                UI.displayPage(mainUI_WV, page);

            }
            if ((this.getPackageName() + ".NEW_PAGE").equals(intent.getAction())) {
                /* Create new page and put link to it on top of current page */
                final String currentPageName = page.getPageName();
                final String currentPagePath = currentPageName.substring(0, currentPageName.lastIndexOf("/") + 1);

                AlertDialog.Builder builderName = new AlertDialog.Builder(this);
                builderName.setTitle("New page (file) name");
                final EditText inputName = new EditText(this);
                builderName.setView(inputName);
                builderName.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String newPageNameEntered = inputName.getText().toString().trim();
                        final String newPageName = currentPagePath + newPageNameEntered;
                        final String newPageLink = TextTools.pathAbsolutize(newPageName);
                        if (newPageNameEntered.startsWith("/")) {
                            Toast.makeText(MainActivity.this, "Page link must be relative", Toast.LENGTH_SHORT).show();
                            //TODO: implement stay on dialog
                        } else if (newPageLink == null) {
                            Toast.makeText(MainActivity.this, "Page link out of file tree", Toast.LENGTH_SHORT).show();
                            //TODO: implement stay on dialog
                        } else if (FileIO.isFileExists(MainActivity.this, "/md/" + newPageLink + ".md")) {
                            Toast.makeText(MainActivity.this, "Page already exists", Toast.LENGTH_SHORT).show();
                            //TODO: implement add link to current page if page exists
                        } else {
                            AlertDialog.Builder builderTitle = new AlertDialog.Builder(MainActivity.this);
                            
                            builderTitle.setTitle("New page title");
                            final EditText inputTitle = new EditText(MainActivity.this);
                            // If current page after absolute path is in the same directory
                            // as current page squash directory name before offering new
                            // page title
                            String newPageTitleOffer = newPageLink.substring(1);
                            if (newPageLink.startsWith(currentPagePath))
                                newPageTitleOffer = newPageLink.replace(currentPagePath, "");                 
                            inputTitle.setText(newPageTitleOffer, TextView.BufferType.EDITABLE);
                            builderTitle.setView(inputTitle);
                            
                            builderTitle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    final String timeStamp = DF.format(new Date());
                                    UI.setMdContentFromFile(MainActivity.this, page);
                                    page.setMetaModified(timeStamp);
                                    final String currentMeta = page.getMetaHeaderAsString();
                                    final String currentContent = page.getMdContent();

                                    String newPageTitle = inputTitle.getText().toString().trim();
                                    String linkToNewPage = String.format("* [%s](%s.html)\n", newPageTitle, newPageNameEntered);
                                    if (currentMeta != null && currentContent != null) {
                                        page.addAtTopOfPage(linkToNewPage);
                                        FileIO.writePageToFile(
                                                MainActivity.this,
                                                currentPageName,
                                                page.getMdContent()
                                        );
                                    } else {
                                        String stackState;
                                        if (pages == null)
                                            stackState = "null";
                                        else if (pages.empty())
                                            stackState = "empty";
                                        else
                                            stackState = pages.peek();
                                        Toast.makeText(MainActivity.this, "Internal program error. Link to page didn't created\n * page = " + pageName + "\n * pages.peek = " + stackState, Toast.LENGTH_SHORT).show();
                                    }

                                    FileIO.createPageIfNotExists(MainActivity.this, newPageLink, newPageTitle, "");

                                    pageAdd(newPageLink);

                                    Intent i = new Intent();
                                    i.setAction(MainActivity.this.getPackageName() + ".EDIT_PAGE");
                                    i.putExtra("name", newPageLink);
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

    }

    private void fallbackDisplayStartPage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        pageAdd(UI.displayStartPage(mainUI_WV));

    }

    private void checkAndImportPage(Page importedPage) {
        String importedPageLink = importedPage.getPageName().replaceFirst("/incoming/","");
        pageAdd(Constants.INCOMING_INDEX_PAGE);
        FileIO.createPageIfNotExists(this,
                Constants.INCOMING_INDEX_PAGE,
                "Incoming pages index",
                ""
        );
        UI.setMdContentFromFile(this, page);
        String newText = "* ["+ importedPage.getMetaByKey("title") +"]("+ importedPageLink +".html)\n";
        page.addAtTopOfPage(newText);
        FileIO.writePageToFile(
                this,
                Constants.INCOMING_INDEX_PAGE,
                page.getMdContent()
        );
        pageAdd(importedPage.getPageName());
        UI.displayPage(mainUI_WV, page);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.EDIT_PAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                UI.displayPage(mainUI_WV, page);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Context c = MainActivity.this;
                //TODO: Why this result code actual instead of RESULT_OK?
                //MyLog.LogD("* Call DP from ma onActivityResult(CANCELED), PN: " + ui.getPageName());
                Date tsBefore = page.getFileTS();
                UI.setTSFromFile(c, page);
                Date tsAfter = page.getFileTS();
                if (tsBefore != null && tsAfter != null && tsAfter.after(tsBefore)) {
                    UI.setMdContentFromFile(c, page);
                    page.addAtTopOfPage(""); //To set 'modified' meta
                    FileIO.writePageToFile(
                            c,
                            "/" + page.getPageName(),
                            page.getMdContent()
                    );
                }
                UI.displayPage(mainUI_WV, page);
            }
        } else if (requestCode == Constants.PREFERENCES_REQUEST) {
            SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            if (defSharedPref.getBoolean(getString(R.string.pk_pref_changed), false)) {
                SharedPreferences.Editor editor = defSharedPref.edit();
                UI.displayPage(mainUI_WV, page);
                editor.putBoolean(getString(R.string.pk_pref_changed), false);
                editor.commit();
                requestTermuxPermission(defSharedPref);
            }
        }
    }

    /**
     * Page stack control functions
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
        page = new Page(pages.peek());
        UI.displayPage(mainUI_WV, page);
        return true;
    }

    private void requestTermuxPermission(SharedPreferences pref){
        if (Build.VERSION.SDK_INT > 22 && pref.getBoolean(this.getString(R.string.pk_enable_termux_intent_uri), false) ) {
            String[] Termux_permission = {"com.termux.permission.RUN_COMMAND"};
            this.requestPermissions(Termux_permission, 10743);
        }
    }
    
    private void showQuickNoteDialog(String noteText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create QuickNote");
        final EditText input = new EditText(this);
        builder.setView(input);
        if (!noteText.isEmpty()) {
            input.setText(noteText);
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Context c = MainActivity.this;
                    pageAdd("/" + Constants.QUICKNOTES_PAGE);
                    FileIO.createPageIfNotExists(c,
                                                 "/" + Constants.QUICKNOTES_PAGE,
                                                 "",
                                                 ""
                                                 );
                    UI.setMdContentFromFile(c, page);

                    final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String newText = getString(
                        R.string.template_quick_note,
                        // Time stamp
                        DF.format(new Date()),
                        // Note text
                        input.getText().toString()
                    );
                    page.addAtTopOfPage(newText);

                    FileIO.writePageToFile(
                        c,
                        "/" + Constants.QUICKNOTES_PAGE,
                        page.getMdContent()
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
        
    }
    
    private void showUrlImportDialog(String title, String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Import URL");
        /*****************/
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleBox = new EditText(this);
        titleBox.setHint("Title");
        layout.addView(titleBox);
        if(!title.isEmpty())
            titleBox.setText(title);

        final EditText urlBox = new EditText(this);
        urlBox.setHint("URL");
        layout.addView(urlBox);
        if(!url.isEmpty())
            urlBox.setText(url);

        final EditText tagsBox = new EditText(this);
        tagsBox.setHint("Tags");
        layout.addView(tagsBox);
        //tagsBox.setText("* ");

        final EditText notesBox = new EditText(this);
        notesBox.setHint("Notes");
        layout.addView(notesBox);
        //notesBox.setText("* ");

        builder.setView(layout);


        /**************/
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Context c = MainActivity.this;
                    pageAdd("/" + Constants.URL_INCOMING_PAGE);
                    FileIO.createPageIfNotExists(c,
                                                 "/" + Constants.URL_INCOMING_PAGE,
                                                 "",
                                                 ""
                                                 );
                    final StringBuilder tags_sb = new StringBuilder();
                    if (tagsBox.getText().toString().length() > 0) {
                        String[] tags = tagsBox.getText().toString().split(";");
                        if (tags.length > 0) {
                            for (int i = 0; i < tags.length; i++) {
                                tags_sb.append("      \"")
                                        .append(tags[i].trim())
                                        .append("\"");
                                if (i != tags.length-1)
                                    tags_sb.append(",");
                                tags_sb.append("\n");
                            }
                        }
                    }
                    final StringBuilder notes_sb = new StringBuilder();
                    if (notesBox.getText().toString().length() > 0) {
                        String[] notes = notesBox.getText().toString().split(";");
                        if (notes.length > 0) {
                            for (int i = 0; i < notes.length; i++) {
                                notes_sb.append("      \"")
                                        .append(notes[i].trim())
                                        .append("\"");
                                if (i != notes.length-1)
                                    notes_sb.append(",");
                                notes_sb.append("\n");
                            }
                        }
                    }
                    final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String newText = getString(
                        R.string.template_url_bookmark,
                        // Time stamp
                        DF.format(new Date()),
                        urlBox.getText().toString(),
                        TextTools.escapeJavaScriptFunctionParameter(titleBox.getText().toString()),
                        tags_sb.toString(),
                        notes_sb.toString()
                    );
                    UI.setMdContentFromFile(c, page);
                    String header = page.getMetaHeaderAsString();
                    String bookmarkHeaderRegexp = "<script>bookmarks = \\[\n<!-- Don't edit body below this line -->\n";
                    page.setMdContent(page.getMdContent().replaceFirst(bookmarkHeaderRegexp, ""));
                    page.addAtTopOfPage(newText);
                    page.addAtTopOfPage(header);

                    FileIO.writePageToFile(
                        c,
                        "/" + Constants.URL_INCOMING_PAGE,
                        page.getMdContent()
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
                    fallbackDisplayStartPage("Url not imported...");
                }
            });
        builder.show();
    }

    /**
     * Handle JavaScript console log
     */
    private class myWebChromeClient extends WebChromeClient {
        // Based on https://stackoverflow.com/a/2474524
        // need for API < 19
        public void onExceededDatabaseQuota(String url, String
                databaseIdentifier, long currentQuota, long estimatedSize, long
                totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            quotaUpdater.updateQuota(256 * 1024);
        } 

        public boolean onConsoleMessage(ConsoleMessage cm) {
            Context c = getApplicationContext();
            SharedPreferences defSharedPref =
                PreferenceManager.getDefaultSharedPreferences(c);
            Boolean jsDebug = defSharedPref.getBoolean(getString(R.string.pk_enable_js_debug),false);
            if(jsDebug && cm.lineNumber() > 10) {
                writeJSDebug(c, cm);
            } else {
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
            }
            return true;
        }

        private void writeJSDebug(Context c,  ConsoleMessage cm) {
            String[] srcStrings = FileIO.getStringFromFile(cm.sourceId().replace("file://","")).split("\n");
            final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String severity = "UNDEFINED";
            switch (cm.messageLevel()) {
                case DEBUG:
                    severity = "DEBUG";
                    break;
                case ERROR:
                    severity = "ERROR";
                    break;
                case LOG:
                    severity = "LOG";
                    break;
                case TIP:
                    severity = "TIP";
                    break;
                case WARNING:
                    severity = "WARNING";
                    break;
                default:
                    severity = "UNKNOWN";
                    break;
            }
            String debugMsg = ""
                + "Title: JavaScript debug\n\n"
                + "<!--\n"
                + "  Don't edit this page!\n"
                + "  Page generated automatically.\n"
                + "  All your changes will be lost.\n"
                + "-->\n\n"
                + "##### "
                + DF.format(new Date())
                + ", severity: "
                + severity
                + "\n\n";
            if (cm.messageLevel() == ConsoleMessage.MessageLevel.ERROR)
                debugMsg += ""
                    + cm.message().replace(": ", ":\n\n");
            else
                debugMsg += ""
                        + "Console log:\n\n"
                        + cm.message()
                        + "\n\n";
            debugMsg += ""
                + "\n\non line **"
                + cm.lineNumber()
                + "** of file: ***"
                + cm.sourceId().replaceAll(".*/files/", "")
                + "***\n- - -\n"
                + "\n```\n"
                + (cm.lineNumber() - 1) + ". "
                + srcStrings[cm.lineNumber() - 2] + "\n"
                + (cm.lineNumber()) + ". "
                + srcStrings[cm.lineNumber() - 1] + "\n"
                + (cm.lineNumber() + 1) + ". "
                + srcStrings[cm.lineNumber()] + "\n"
                + "```\n"
                + "\n- - -\n";
            FileIO.writePageToFile(c, "/" + Constants.JS_DEBUG_PAGE, debugMsg);
            Toast.makeText(c, "JS debug generated",Toast.LENGTH_SHORT).show();
        }
    }
}
