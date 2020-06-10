package com.example.browser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

public class withdraw extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int Rs,pnt;
    private TextView pointview,rs;
    private EditText mob;
    private Button prequest;
    private PublisherAdView wpad;
    private RewardedVideoAd mRewardedVideoAd;
    private LottieAnimationView no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.withdraw);
        setTitle("payment request");
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        pointview = findViewById(R.id.pointview);
        mob = findViewById(R.id.mob);
        prequest =findViewById(R.id.prequest);
        rs =findViewById(R.id.rs);


           wpad = findViewById(R.id.wpad);

           PublisherAdRequest publisherAdRequest = new PublisherAdRequest.Builder().build();
           wpad.loadAd(publisherAdRequest);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadRewardedVideoAd();
            }
        },5000);



        //

        no = findViewById(R.id.no);






        prequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connected())
                {
                if(pnt >= 200)
                {
                if(mob.getText().toString().equals("") || mob.getText().toString().length() != 10 )
                {
                    FancyToast.makeText(withdraw.this,"enter a valid number",FancyToast.ERROR,
                            FancyToast.LENGTH_SHORT,false).show();
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("payment").child(mAuth.getCurrentUser()
                            .getUid()).child("username").setValue(mAuth.getCurrentUser().getDisplayName());
                    FirebaseDatabase.getInstance().getReference().child("payment").child(mAuth.getCurrentUser()
                            .getUid()).child("mob").setValue(mob.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("payment").child(mAuth.getCurrentUser()
                            .getUid()).child("points").setValue(pnt);
                    FirebaseDatabase.getInstance().getReference().child("my_users").child(mAuth.getCurrentUser()
                            .getUid()).child("points").setValue(0);
                    info user =new info(withdraw.this);
                    user.setPoints("0");

                    FancyToast.makeText(withdraw.this,"payment request sent",FancyToast.SUCCESS,
                            FancyToast.LENGTH_LONG,false).show();

                }
            }

                else {
                         FancyToast.makeText(withdraw.this,"minimum 200 points required for withdrawal",FancyToast.INFO,
                                 FancyToast.LENGTH_LONG,false).show();
                }
            }

                else
                {
                    FancyToast.makeText(withdraw.this,"check your internet connection",FancyToast.WARNING,
                            FancyToast.LENGTH_LONG,false).show();
                }
            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();



        if(connected()) {
            info user =new info(withdraw.this);
            pnt =Integer.parseInt(user.getPoints());
            Rs=pnt/5;
            pointview.setText(user.getPoints());
            rs.setText(Rs + "");
        }
        else
        {
            FancyToast.makeText(withdraw.this,"check your internet connection",
                    FancyToast.WARNING,FancyToast.LENGTH_SHORT,false).show();

            no.setVisibility(View.VISIBLE);
            prequest.setVisibility(View.GONE);

        }



    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-4499095708288541/4985490720",
                new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    private boolean connected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null && activeNetworkInfo.isConnected();
    }
}
