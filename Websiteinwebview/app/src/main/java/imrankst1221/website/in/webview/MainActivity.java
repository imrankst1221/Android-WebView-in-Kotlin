package imrankst1221.website.in.webview;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    Context mContext;
    // set your custom url here
    String url = "http://www.androidbangladesh.com/";

    // if you want to show progress bar on splash screen
    Boolean showProgressOnSplashScreen = true;

    WebView mWebView;
    ProgressBar prgs;
    RelativeLayout splash, main_layout;
    SwipeRefreshLayout swipeLayout;

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                Window.PROGRESS_VISIBILITY_ON);

        mWebView = (WebView) findViewById(R.id.wv);
        prgs = (ProgressBar) findViewById(R.id.progressBar);
        main_layout = (RelativeLayout) findViewById(R.id.main_layout);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeLayout.setColorSchemeColors(ContextCompat.getColor(mContext,R.color.material_core_deep_orange),
                ContextCompat.getColor(mContext,R.color.material_core_blue),
                ContextCompat.getColor(mContext,R.color.material_core_light_green),
                ContextCompat.getColor(mContext,R.color.material_core_yellow));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(internetCheck(mContext)) {
                            mWebView.loadUrl(url);
                        }else{
                            showAlertDialog(mContext, "OOPS!", "Please check your internet connection.", R.drawable.ic_no_internet);
                        }
                    }
                },1000);
            }
        });


        // splash screen View
        if (!showProgressOnSplashScreen)
            ((ProgressBar) findViewById(R.id.progressBarSplash)).setVisibility(View.GONE);
        splash = (RelativeLayout) findViewById(R.id.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (splash.getVisibility() == View.VISIBLE)
                    splash.setVisibility(View.GONE);
            }
        },5000);

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//			// get status bar height to push webview below that
//			int result = 0;
//			int resourceId = getResources().getIdentifier("status_bar_height",
//					"dimen", "android");
//			if (resourceId > 0) {
//				result = getResources().getDimensionPixelSize(resourceId);
//			}
//
//			// set top padding to status bar
//			main_layout.setPadding(0, result, 0, 0);
//		}

        if(internetCheck(mContext)) {
            mWebView.loadUrl(url);
        }else{
            showAlertDialog(mContext, "OOPS!", "Please check your internet connection.", R.drawable.ic_no_internet);
        }
        // control javaScript and add html5 features
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
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
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

                if (prgs.getVisibility() == View.VISIBLE)
                    prgs.setVisibility(View.GONE);

                // check if splash is still there, get it away!
                if (splash.getVisibility() == View.VISIBLE)
                    splash.setVisibility(View.GONE);
                // slideToBottom(splash);

            }
        });

    }

    /**
     * To animate view slide out from top to bottom
     *
     * @param
     */
    // void slideToBottom(View view) {
    // TranslateAnimation animate = new TranslateAnimation(0, 0, 0,
    // view.getHeight());
    // animate.setDuration(2000);
    // animate.setFillAfter(true);
    // view.startAnimation(animate);
    // view.setVisibility(View.GONE);
    // }

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
    }
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