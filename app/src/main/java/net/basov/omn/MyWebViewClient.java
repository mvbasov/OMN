/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017-2024 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.omn;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Arrays;

import net.basov.util.MyLog;
import net.basov.util.FileIO;

/**
 * Redirect external URLs to browser
 */

public class MyWebViewClient extends WebViewClient {

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        final Uri uri = Uri.parse(url);
        return processUri(uri, view);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        final Uri uri = request.getUrl();
        return processUri(uri, view);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean processUri(final Uri uri, WebView view) {
        Context c = view.getContext();
        switch(uri.getScheme()){
            case "file":
                String pageName = uri.toString()
                        .replaceFirst("file://","")
                        .replace("//","/")
                        .replace(FileIO.getFilesDir(c).getPath(),"")
                        .replaceFirst("/html/","")
                        .replaceFirst("/android_asset","") //TODO: Why it is need?
                        .replaceAll("\\.html$","")
                        .replaceAll("\\.html#","#")
                        .replaceAll("\\.html\\?","?")
                        .replaceAll("\\.md$","");
                        
                /* For relative uri add / to begin because relative already translated to absolute */        
                if(pageName.charAt(0) != '/') pageName = "/" + pageName;
                
                Intent intentDisplayPage = new Intent(view.getContext().getApplicationContext(),
                        MainActivity.class);
                intentDisplayPage.putExtra("page_name", pageName);
                intentDisplayPage.setAction(Intent.ACTION_MAIN);
                c.startActivity(intentDisplayPage);

                return false;
            case "http":
            case "https":
            case "geo":
            case "tel":
			case "mailto":
			case "sms":
			case "market":
                final Intent intentMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    c.startActivity(intentMarket);
                    return true;
                } catch (ActivityNotFoundException e) {
                    MyLog.LogE(e, "Activity to handle " + uri.getScheme() + "not found");
                }
            case "intent":
                SharedPreferences defSharedPref = PreferenceManager.getDefaultSharedPreferences(c);
                if (! defSharedPref.getBoolean(c.getString(R.string.pk_enable_intent_uri), false))
                    return false;
                try {
                    // TODO: remove double translation string - uri - string
                    final Intent intentApp = new Intent().parseUri(uri.toString(), Intent.URI_INTENT_SCHEME);
                    if (intentApp != null) {
                        view.stopLoading();
                        PackageManager packageManager = c.getPackageManager();
                        ResolveInfo info = packageManager.resolveActivity(intentApp, PackageManager.MATCH_DEFAULT_ONLY);
                        if (info != null) {             
                            StrictMode.VmPolicy old = StrictMode.getVmPolicy();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(old)
                                                       .detectFileUriExposure()
                                        // TODO: implement it as config option
                                        /* To enable using 'file///' scheme at
                                           intent processing time uncomment the following line */
                                                       //.penaltyLog()
                                                       .build());
                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                                    try{
                                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                        m.invoke(null);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    c.startActivity(intentApp);
                                } catch (ActivityNotFoundException e) {
                                    MyLog.LogE(e, "No Activity to handle " + intentApp.getAction() + " found");
                                }
                                StrictMode.setVmPolicy(old);
                            } else {
                                try {
                                    c.startActivity(intentApp);
                                } catch (ActivityNotFoundException e) {
                                    MyLog.LogE(e, "No Activity to handle " + intentApp.getAction() + " found");
                                }
                            }
                        } else {
                            String fallbackUrl = intentApp.getStringExtra("browser_fallback_url");
                            view.loadUrl(fallbackUrl);
                        }
                        // Termux RUN_COMMAND Intent integration
                        final Bundle extras = intentApp.getExtras();
                        if (extras != null && extras.containsKey("com.termux.RUN_COMMAND_PATH")){
                            if (! defSharedPref.getBoolean(c.getString(R.string.pk_enable_termux_intent_uri), false)
                                    || ! checkPermission("com.termux.permission.RUN_COMMAND", c)
                                    || ! isPackageExisted("com.termux", c)) {
                                Toast.makeText(c,
                                        "Intent URI disabled" +
                                                " or permission RUN_COMMAND not granted" +
                                                " or Termux not installed.",
                                        Toast.LENGTH_LONG
                                ).show();
                                return false;
                            }
                            MyLog.LogD( "com.termux intent has extras");
                            String cmd = extras.getString("com.termux.RUN_COMMAND_PATH");
                            MyLog.LogD("cmd is: " + cmd );
                            if (cmd != null) {
                                String[] cmdParts = cmd.split("\\?");
                                if (cmdParts.length > 1) {
                                    MyLog.LogD( "parts is: "
                                            + Arrays.toString(cmdParts)
                                    );
                                    String[] cmdArgs = cmdParts[1].split("&");
                                    intentApp.putExtra("com.termux.RUN_COMMAND_PATH", cmdParts[0].trim());
                                    MyLog.LogD("args is: "
                                            + Arrays.toString(cmdArgs)
                                    );
                                    intentApp.putExtra("com.termux.RUN_COMMAND_ARGUMENTS", cmdArgs);
                                }
                            }
                            try {
                                c.startService(intentApp);
                            } catch (SecurityException | ActivityNotFoundException e) {
                                MyLog.LogE(e, "Service to handle " + uri.getScheme() + "not found");
                            }
                        }
                        return true;
                    }
                } catch (URISyntaxException e) {
                    MyLog.LogE(e, "href processing error");
                }
            default:
                return true;
                
         }              
    }
    private boolean checkPermission(String permission_name, Context c) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int result = c.checkSelfPermission(permission_name);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private boolean isPackageExisted(String targetPackage, Context c){
        // code from https://stackoverflow.com/a/6758962
        PackageManager pm = c.getPackageManager();
        try {
            PackageInfo info=pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }
}
