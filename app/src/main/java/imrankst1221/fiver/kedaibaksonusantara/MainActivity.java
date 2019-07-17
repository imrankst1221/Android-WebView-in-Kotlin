package imrankst1221.fiver.kedaibaksonusantara;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends Activity {
    static String TAG = "---MainActivity";
    Context mContext;
    boolean mLoaded = false;
    // set your custom url here
    String URL = "https://www.allmistree.com/career/";

    //for attach files
    private String mCameraPhotoPath;
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
    private ValueCallback<Uri[]> mFilePathCallback;
    boolean doubleBackToExitPressedOnce = false;


    //AdView adView;
    Button btnTryAgain;
    WebView mWebView;
    ProgressBar prgs;
    View viewSplash;
    RelativeLayout layoutSplash, layoutWebview, layoutNoInternet;


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

        SecretKey secret = null;
        String toEncrypt = URL;
        try {
            secret = generateKey();
            byte[] toDecrypt = encryptMsg(toEncrypt, secret);

            Log.d(TAG, toDecrypt.toString());

            Log.d(TAG, decryptMsg(toDecrypt, secret));
        } catch (Exception e) {
            Log.e(TAG, ""+e.getMessage());
        }

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

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void requestWebView() {
        /** Layout of webview screen View **/
        if(internetCheck(mContext)) {
            mWebView.setVisibility(View.VISIBLE);
            layoutNoInternet.setVisibility(View.GONE);
            mWebView.loadUrl(URL);
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
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        //mWebView.getSettings().setDatabasePath(
        //        this.getFilesDir().getPath() + this.getPackageName() + "/databases/");

        // this force use chromeWebClient
        mWebView.getSettings().setSupportMultipleWindows(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){

                Log.d(TAG,"URL: "+url);
                if(internetCheck(mContext)) {
                    //view.loadUrl(url);
                    // if you wanna open outside of app
                     if (url != null && url.startsWith(URL)) {
                        view.loadUrl(url);
                        return false;
                    }
                    // Otherwise, give the default behavior (open in browser)
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }else{
                    prgs.setVisibility(View.GONE);
                    mWebView.setVisibility(View.GONE);
                    layoutSplash.setVisibility(View.GONE);
                    layoutNoInternet.setVisibility(View.VISIBLE);
                }

                return true;
            }

           /* @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(internetCheck(mContext)) {
                    mWebView.setVisibility(View.VISIBLE);
                    layoutNoInternet.setVisibility(View.GONE);
                    //view.loadUrl(url);
                }else{
                    prgs.setVisibility(View.GONE);
                    mWebView.setVisibility(View.GONE);
                    layoutSplash.setVisibility(View.GONE);
                    layoutNoInternet.setVisibility(View.VISIBLE);
                }
                return false;
            }*/

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

        //file attach request
        mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if(mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if(takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

                return true;
            }
        });

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }



    /**
     * Convenience method to set some generic defaults for a
     * given WebView
     */
    /*@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // We set the WebViewClient to ensure links are consumed by the WebView rather
        // than passed to a browser if it can
        mWebView.setWebViewClient(new WebViewClient());
    }*/

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;

        // Check that the response is a good one
        if(resultCode == Activity.RESULT_OK) {
            if(data == null) {
                // If there is not data, then we may have taken a photo
                if(mCameraPhotoPath != null) {
                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return;
    }


    //for security
    public static SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] key = {1,0,0,0,0,0,0,1,1,1,0,1,0,0,0,0};
        //random.nextBytes(key);
        return new SecretKeySpec(key, "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
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

        if (doubleBackToExitPressedOnce) {
            return super.onKeyDown(keyCode, event);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
        return true;
    }

}