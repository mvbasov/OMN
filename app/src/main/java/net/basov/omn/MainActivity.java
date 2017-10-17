package net.basov.omn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import net.basov.util.FileIO;
import net.basov.util.MyLog;

public class MainActivity extends Activity {

    private WebView mainUI_WV;
    private UI ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int versionCode = 0;
        int oldVersion = 0;
        
        /* Set default preferences at first run and after preferences version upgrade */
        SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defSharedPref.edit();
        final int currePrefVersion = 3;
        switch (defSharedPref.getInt(getString(R.string.pk_pref_version), 0)) {
            case 0: // initial
                editor.putBoolean(getString(R.string.pk_use_view_directory), false);
                editor.putBoolean(getString(R.string.pk_btn_enable_home), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_link), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_email), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_filemanager), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_shortcut), true);
                editor.putInt(getString(R.string.pk_pref_version), currePrefVersion);
                editor.commit();
                break;
            case 1: //Upgrade to properties version 2
                editor.putBoolean(getString(R.string.pk_btn_enable_home), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_link), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_email), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_filemanager), true);
                editor.putBoolean(getString(R.string.pk_btn_enable_shortcut), true);
                editor.putInt(getString(R.string.pk_pref_version), currePrefVersion);
                editor.commit();
                break;
            case 2:
                editor.putBoolean(getString(R.string.pk_btn_enable_shortcut), true);
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

        /* Enable JavaScript */
        webSettings.setJavaScriptEnabled(true);
        mainUI_WV.addJavascriptInterface(new WebViewJSCallback(this), "Android");
        /* Show external page in browser */
        mainUI_WV.setWebViewClient(new MyWebViewClient());
        /* Handle JavaScript prompt dialog */
        mainUI_WV.setWebChromeClient(new myWebChromeClient());
        /* Disable using cache */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);


        /* Enable chome remote debuging for WebView (Ctrl-Shift-I) */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE))
            { WebView.setWebContentsDebuggingEnabled(true); }
        } 
        
        FileIO.creteHomePage(MainActivity.this);
        ui = UI.getInstance();
        onNewIntent(getIntent());
        
    }

    private void runAfterUpdate(int oldVersion, int newVersion) {
        // TODO: Implement this method
        FileIO.creteHomePage(MainActivity.this, true);
        Toast.makeText(this, "version upgrade from " + oldVersion + " to " + newVersion, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    UI ui = UI.getInstance();
                    if(!ui.backPage(mainUI_WV)) finish();                
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
                //Toast.makeText(this, page_name,Toast.LENGTH_SHORT).show();
                if(page_name != null && page_name.length() > 0) {
                    ui.setPageName(page_name);
                    ui.displayPage(mainUI_WV);
                } else {
					UI.displayStartPage(ui, mainUI_WV);
				}
            } else {
                UI.displayStartPage(ui, mainUI_WV);
            }
        } if (intent != null && intent.getAction().equals(this.getPackageName()+".HOME_PAGE")) {
        /* Show home page */
            UI.displayStartPage(ui, mainUI_WV);

        }if (intent != null && intent.getAction().equals(this.getPackageName()+".REDISPLAY_PAGE")) {
        /* Redisplay page after edit */
            ui.displayPage(mainUI_WV);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.EDIT_PAGE_REQUEST) {
            //Toast.makeText(this,"Without result"+resultCode, Toast.LENGTH_SHORT).show();
            if(resultCode == Activity.RESULT_OK){
                //Toast.makeText(this,"Result OK: ", Toast.LENGTH_SHORT).show();
                //UI ui = UI.getInstance();
                //Log.d(TAG, "Call DP from ma onActivityResult(OK)");
                ui.displayPage(mainUI_WV);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Toast.makeText(this,"Result canceled", Toast.LENGTH_SHORT).show();
                //TODO: Why this result code actual?
                //UI ui = UI.getInstance();
                //MyLog.LogD("* Call DP from ma onActivityResult(CANCELED), PN: " + ui.getPageName());
                ui.displayPage(mainUI_WV);
            }
        }
    }

    /**
     * Handle JavaScript prompt() dialogue
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
