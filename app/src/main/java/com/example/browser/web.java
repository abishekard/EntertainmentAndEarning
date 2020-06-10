package com.example.browser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.concurrent.TimeUnit;

public class web extends AppCompatActivity {

    private WebView web;
    private Toolbar toolbar;
    private String name,j;
    private int pnt,randomAd;
    private FirebaseAuth mAuth;
    private SwipeRefreshLayout refresh;
    private PublisherAdView webad;
    private RewardedVideoAd mRewardedVideoAd;
    private CountDownTimer countDown,countDown2;
    private long timerLeft;
    private MenuItem counter2;
    private Menu menu2;
    private boolean isCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        checknet();
        randomAd=info.getRandomNumber(1,11);
        //ad

        webad = findViewById(R.id.webad);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        webad.loadAd(adRequest);

        MobileAds.initialize(this,"ca-app-pub-4499095708288541~5287715383");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        isCancel=false;

        //ad

        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                checknet();
                web.reload();
                refresh.setRefreshing(false);
            }


        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
       // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       // name = user.getDisplayName();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.rgb(255,255,255));
        toolbar.setTitle("browser");
        toolbar.setBackgroundColor(Color.rgb(0,0,255));
        setTitle("");
        web = findViewById(R.id.web);
        web.setWebViewClient(new browser());
        web.setWebChromeClient(new MyWebClient());

        j="https://www.youtube.com";

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            j =(String) b.get("url");


        }

        web.loadUrl(j);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //*******************
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setDomStorageEnabled(true);
        web.setInitialScale(1);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web.setScrollbarFadingEnabled(false);
        web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 19) {
            web.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }




        //*******************




    }
    @Override
    public void onBackPressed() {
        if(web.canGoBack())
        {
            web.goBack();
        }
        else {
           // android.os.Process.killProcess(android.os.Process.myPid());
            countDown.cancel();
            web.clearCache(true);
            web.clearHistory();
            WebStorage.getInstance().deleteAllData();
            finish();
            super.onBackPressed();

            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            }

        }
    }
     public class browser extends WebViewClient
    {



        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
        {
            paramWebView.loadUrl(paramString);
            return true;
        }

    }
    public class MyWebClient extends WebChromeClient
    {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        private int mOriginalSystemUiVisibility;


        public Bitmap getDefaultVideoPoster()
        {
            if (web.this == null) {
                return null;
            }
            return BitmapFactory.decodeResource(web.this.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)web.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            web.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            web.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mOriginalOrientation= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = web.this.getWindow().getDecorView().getSystemUiVisibility();
            web.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mOriginalOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)web.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            web.this.getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }


    long timer = 20000;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu,menu);
        final MenuItem counter = menu.findItem(R.id.timerr);

        countDown = new CountDownTimer(timer, 1000) {


            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = /*(TimeUnit.MILLISECONDS.toHours(millis))+":"+*/(TimeUnit.MILLISECONDS.toMinutes(millis)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))) + ":" + (TimeUnit.MILLISECONDS.toSeconds(millis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                counter.setTitle(hms);
                timer = millis;

                if(timer<19000 && timer>18000)
                {
                    loadRewardedVideoAd();
                }


            }

            public void onFinish() {
                counter.setTitle("done!");
                FirebaseDatabase.getInstance().getReference().child("my_users").child(mAuth.getCurrentUser()
                        .getUid()).child("points").setValue(pnt + 2);

                info user =new info(web.this);
                user.setPoints((pnt+2)+"");

                FancyToast.makeText(web.this,"earned 2 points",FancyToast.SUCCESS,
                        FancyToast.LENGTH_LONG,false).show();
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FancyToast.makeText(web.this,"please tap on timer to restart timer",
                                FancyToast.SUCCESS,FancyToast.LENGTH_LONG,false).show();

                    }
                },2000);





                Handler handler1=new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (mRewardedVideoAd.isLoaded()) {
                            mRewardedVideoAd.show();
                        }

                    }
                },3000);


            }
        }.start();

        return  true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final MenuItem counter =item;
        countDown.cancel();
        if(timer<=3000) {
            timer = 900000;
        }
        else{
            if(isCancel) {
            timer=timerLeft;
            isCancel=false;
            }

        }


                countDown = new CountDownTimer(timer, 1000) {

                    public void onTick(long millisUntilFinished) {
                        long millis = millisUntilFinished;
                        String hms = /*(TimeUnit.MILLISECONDS.toHours(millis))+":"+*/(TimeUnit.MILLISECONDS.toMinutes(millis)
                                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))) + ":" + (TimeUnit.MILLISECONDS.toSeconds(millis)
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                        counter.setTitle(hms);
                        timer = millis;
                        if(timer<19000 && timer>18000)
                        {
                            loadRewardedVideoAd();

                        }


                    }

                    public void onFinish() {
                        counter.setTitle("done!");
                        FirebaseDatabase.getInstance().getReference().child("my_users").child(mAuth.getCurrentUser()
                                .getUid()).child("points").setValue(pnt + 2);

                        info user =new info(web.this);
                        user.setPoints((pnt+2)+"");

                        FancyToast.makeText(web.this,"earned 2 points",FancyToast.SUCCESS,
                                FancyToast.LENGTH_LONG,false).show();
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                FancyToast.makeText(web.this,"please tap on timer to restart timer",
                                        FancyToast.SUCCESS,FancyToast.LENGTH_LONG,false).show();

                            }
                        },2000);
                        Handler handler1=new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (mRewardedVideoAd.isLoaded()) {
                                    mRewardedVideoAd.show();
                                }

                            }
                        },3000);


                    }
                }.start();






        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("my_users").child(mAuth.getCurrentUser().getUid()).
                child("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pnt =Integer.valueOf( dataSnapshot.getValue().toString());
                Toast.makeText(web.this,pnt+"",Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        refresh.getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (web.getScrollY() == 0)
                            refresh.setEnabled(true);
                        else
                            refresh.setEnabled(false);

                    }
                });



    }


    private boolean connected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null && activeNetworkInfo.isConnected();
    }

    private void checknet()
    {
        if(!connected()){
            Toast.makeText(web.this,"check internet connection",Toast.LENGTH_SHORT).show();
        }
    }



    public class swipe extends SwipeRefreshLayout
    {


        public swipe(@NonNull Context context) {
            super(context);


        }

        @Override
        public void setOnRefreshListener(@Nullable OnRefreshListener listener) {
            super.setOnRefreshListener(listener);
            if(!canChildScrollUp())
                onRefresh();


        }
        public void onRefresh() {

            checknet();
            web.reload();
            refresh.setRefreshing(false);
        }


    }

    private void loadRewardedVideoAd() {

        if(randomAd <=4 ){
            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                    new AdRequest.Builder().build());


        }
        else if(randomAd>4 && randomAd<9) {

            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                    new AdRequest.Builder().build());

        }
        else{
            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                    new AdRequest.Builder().build());

        }

    }

    @Override
    public void onPause() {
        web.onPause();
        web.pauseTimers();
        timerLeft=timer;
        isCancel=true;
        countDown.cancel();
        try{
            web.clearCache(true);}
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        WebStorage.getInstance().deleteAllData();
        super.onPause();

    }

    @Override
    public void onResume() {

        web.resumeTimers();
        web.onResume();
        if(timer<885000)
        FancyToast.makeText(web.this,"please tap on timer to continue timer",
                FancyToast.SUCCESS,FancyToast.LENGTH_LONG,false).show();

        super.onResume();

    }


    @Override
    protected void onDestroy() {
        web.destroy();
        web = null;
        try{
        web.clearCache(true);}
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        WebStorage.getInstance().deleteAllData();
        super.onDestroy();


    }



}
