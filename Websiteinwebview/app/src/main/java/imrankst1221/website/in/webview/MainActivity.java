package imrankst1221.website.in.webview;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
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

    // set your custom url here
    String url = "http://www.androidbangladesh.com/";

    // if you want to show progress bar on splash screen
    Boolean showProgressOnSplashScreen = true;

    WebView mWebView;
    ProgressBar prgs;
    RelativeLayout splash, main_layout;

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                Window.PROGRESS_VISIBILITY_ON);

        mWebView = (WebView) findViewById(R.id.wv);
        prgs = (ProgressBar) findViewById(R.id.progressBar);
        main_layout = (RelativeLayout) findViewById(R.id.main_layout);

        GradientDrawable rainbow = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED});
        AnimationDrawable ad = getProgressBarAnimation();
        prgs.setBackgroundDrawable(ad);

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
        },2000);

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

        mWebView.loadUrl(url);

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

    private AnimationDrawable getProgressBarAnimation(){
        GradientDrawable rainbow1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW});

        GradientDrawable rainbow2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN});

        GradientDrawable rainbow3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN });

        GradientDrawable rainbow4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE });

        GradientDrawable rainbow5 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA });

        GradientDrawable rainbow6 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED });


        GradientDrawable[]  gds = new GradientDrawable[] {rainbow1, rainbow2, rainbow3, rainbow4, rainbow5, rainbow6};

        AnimationDrawable animation = new AnimationDrawable();

        for (GradientDrawable gd : gds){
            animation.addFrame(gd, 100);
        }
        animation.setOneShot(false);
        return animation;
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

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}