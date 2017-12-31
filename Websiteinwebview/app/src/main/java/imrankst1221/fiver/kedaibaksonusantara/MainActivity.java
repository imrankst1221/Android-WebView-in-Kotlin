package imrankst1221.fiver.kedaibaksonusantara;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    Context mContext;
    boolean mLoaded = false;
    // set your custom url here
    String url = "https://jobboardpro.site/";

    //AdView adView;
    Button btnTryAgain;
    WebView mWebView;
    ProgressBar prgs;
    View viewSplash;
    RelativeLayout layoutSplash, layoutWebview, layoutNoInternet;
    LinearLayout layoutFooter;
    SwipeRefreshLayout swipeLayout;

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                Window.PROGRESS_VISIBILITY_ON);

        mContext = this;
        mWebView = (WebView) findViewById(R.id.webview);
        prgs = (ProgressBar) findViewById(R.id.progressBar);
        btnTryAgain = (Button) findViewById(R.id.btn_try_again);
        viewSplash = (View) findViewById(R.id.view_splash);
        layoutWebview = (RelativeLayout) findViewById(R.id.layout_webview);
        layoutNoInternet = (RelativeLayout) findViewById(R.id.layout_no_internet);
        /** Layout of Splash screen View **/
        layoutSplash = (RelativeLayout) findViewById(R.id.layout_splash);

        //request for show website
        requestForWebview();

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.setVisibility(View.GONE);
                prgs.setVisibility(View.VISIBLE);
                layoutSplash.setVisibility(View.VISIBLE);
                layoutNoInternet.setVisibility(View.GONE);
                requestForWebview();
            }
        });
        //showAdMob();
    }

    private void requestForWebview() {

        if(!mLoaded) {
            requestWebView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    prgs.setVisibility(View.VISIBLE);
                    //viewSplash.getBackground().setAlpha(145);
                    mWebView.setVisibility(View.VISIBLE);
                }
            }, 3000);

        }else{
            mWebView.setVisibility(View.VISIBLE);
            prgs.setVisibility(View.GONE);
            layoutSplash.setVisibility(View.GONE);
            layoutNoInternet.setVisibility(View.GONE);
        }

    }

    private void requestWebView() {
        /** Layout of webview screen View **/
        if(internetCheck(mContext)) {
            mWebView.setVisibility(View.VISIBLE);
            layoutNoInternet.setVisibility(View.GONE);
            mWebView.loadUrl(url);
        }else{
            prgs.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
            layoutSplash.setVisibility(View.GONE);
            layoutNoInternet.setVisibility(View.VISIBLE);

            return;
        }
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDatabasePath(
                this.getFilesDir().getPath() + this.getPackageName()
                        + "/databases/");

        // this force use chromeWebClient
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(internetCheck(mContext)) {
                    mWebView.setVisibility(View.VISIBLE);
                    layoutNoInternet.setVisibility(View.GONE);
                    view.loadUrl(url);
                }else{
                    prgs.setVisibility(View.GONE);
                    mWebView.setVisibility(View.GONE);
                    layoutSplash.setVisibility(View.GONE);
                    layoutNoInternet.setVisibility(View.VISIBLE);
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (prgs.getVisibility() == View.GONE) {
                    prgs.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoaded = true;
                if (prgs.getVisibility() == View.VISIBLE)
                    prgs.setVisibility(View.GONE);

                // check if layoutSplash is still there, get it away!
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutSplash.setVisibility(View.GONE);
                        //viewSplash.getBackground().setAlpha(255);
                    }
                }, 2000);
            }
        });
    }

    private void showAdMob() {
        /** Layout of AdMob screen View **/
        //layoutFooter = (LinearLayout) findViewById(R.id.layout_footer);
        //adView = (AdView) findViewById(R.id.adMob);
            /*try {
            if(internetCheck(mContext)){
                //initializeAdMob();
            }else{
                Log.d("---------","--no internet-");
            }
        }catch (Exception ex){
            Log.d("-----------", ""+ex);
        }*/
    }
    /**** Initial AdMob ****/
    /**
    private void initializeAdMob() {
        Log.d("----","Initial Call");
        adView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                //.addTestDevice("F901B815E265F8281206A2CC49D4E432")
                .build();
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adView.setVisibility(View.VISIBLE);
                        Log.d("----","Visible");
                    }
                });
            }
        });
        adView.loadAd(adRequest);
    }
    **/
    /**
    public static void showAlertDialog(Context mContext, String mTitle, String mBody, int mImage){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setIcon(mImage);
        if(mTitle.length()>0)
            builder.setTitle(mTitle);
        if(mBody.length()>0)
            builder.setTitle(mBody);

        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }**/

    public static boolean internetCheck(Context context) {
        boolean available=false;
        ConnectivityManager connectivity= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivity!=null)
        {
            NetworkInfo[] networkInfo= connectivity.getAllNetworkInfo();
            if(networkInfo!=null)
            {
                for(int i=0; i<networkInfo.length;i++)
                {
                    if(networkInfo[i].getState()==NetworkInfo.State.CONNECTED)
                    {
                        available=true;
                        break;
                    }
                }
            }
        }
        return available;
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}