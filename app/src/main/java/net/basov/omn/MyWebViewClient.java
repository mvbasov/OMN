package net.basov.omn;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    private boolean processUri(final Uri uri, WebView view) {
        Context c = view.getContext();
        // TODO: remove debug
        //Toast.makeText(c,"URI sheme: "+uri.getScheme(),Toast.LENGTH_LONG).show();       
        switch(uri.getScheme()){
            case "file":
                // TODO: remove debug
                //Toast.makeText(c,"PROCESS URI: " + uri.toString(),Toast.LENGTH_SHORT).show();
                String pageName = uri.toString()                       
                        .replace("file://","")
                        .replace("//","/")
                        .replace(FileIO.getFilesDir(c).getPath(),"")
                        .replace("/html/","")
                        //.replace("/android_asset/","") //TODO: Why it is need?
                        .replace("/android_asset","") //TODO: Why it is need?
                        .replace(".html","")
						.replace(".md","");
                        
                /* For relative uri add / to begin because relative already translated to absolute */        
                if(pageName.charAt(0) != '/') pageName = "/" + pageName;
                
                UI ui = UI.getInstance();
                ui.setPageName(pageName);
                //Log.d(MainActivity.TAG, "Call DP from myWvCl processUri " + uri.toString() + ", \nPage name: " + pageName);
                ui.displayPage(view);

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
