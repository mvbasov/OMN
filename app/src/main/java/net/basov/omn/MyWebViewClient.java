/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.omn;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;

import net.basov.util.FileIO;

/**
 * Redirect external URLs to browser
 */

public class MyWebViewClient extends WebViewClient {

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // from https://stackoverflow.com/questions/33151246/how-to-handle-intent-on-a-webview-url/34022490#34022490
        if (url.startsWith("intent://")) {
            try {
                Context context = view.getContext();
                Intent intent = new Intent().parseUri(url, Intent.URI_INTENT_SCHEME);

                if (intent != null) {
                    view.stopLoading();

                    PackageManager packageManager = context.getPackageManager();
                    ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (info != null) {
                        context.startActivity(intent);
                    } else {
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        view.loadUrl(fallbackUrl);

                        // or call external broswer
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
//                    context.startActivity(browserIntent);
                    }

                    return true;
                }
            } catch (URISyntaxException e) {
//                if (GeneralData.DEBUG) {
//                    Log.e(TAG, "Can't resolve intent://", e);
//                }
            }
        }
        
        final Uri uri = Uri.parse(url);
        return processUri(uri, view);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        final Uri uri = request.getUrl();
        return processUri(uri, view);
    }

    private boolean processUri(final Uri uri, WebView view) {
        Context c = view.getContext();
        switch(uri.getScheme()){
            case "file":
                String pageName = uri.toString()
                        .replace("file://","")
                        .replace("//","/")
                        .replace(FileIO.getFilesDir(c).getPath(),"")
                        .replace("/html/","")
                        .replace("/android_asset","") //TODO: Why it is need?
                        .replace(".html","")
						.replace(".md","");
                        
                /* For relative uri add / to begin because relative already translated to absolute */        
                if(pageName.charAt(0) != '/') pageName = "/" + pageName;
                
                Intent intentDisplayPage = new Intent(view.getContext().getApplicationContext(),
                        MainActivity.class);
                intentDisplayPage.putExtra("page_name", pageName);
                intentDisplayPage.setAction(Intent.ACTION_MAIN);
                view.getContext().startActivity(intentDisplayPage);

                return false;
            case "http":
            case "https":
            case "geo":
            case "tel":
			case "mailto":
			case "sms":
			case "market":
                final Intent intent = new Intent(Intent.ACTION_VIEW, uri);	
                c.startActivity(intent);
                return true;
            
            default:
                return true;
                
         }              
    }
}
