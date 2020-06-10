package com.example.browser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

public class feedback extends AppCompatActivity {
    private EditText feed;
    private Button send;
    private FirebaseAuth mAuth;
    private PublisherAdView fdad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        fdad = findViewById(R.id.fdad);
        //   wpad.setAdSizes(AdSize.BANNER);
        //  wpad.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        PublisherAdRequest publisherAdRequest = new PublisherAdRequest.Builder().build();
        fdad.loadAd(publisherAdRequest);


        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        feed = findViewById(R.id.feed);
        send = findViewById(R.id.prequest);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connected())
                {
                    if(feed.getText().toString().equals(""))
                    {
                        FancyToast.makeText(feedback.this,"feedback can't be empty",
                                FancyToast.INFO,FancyToast.LENGTH_SHORT,false).show();
                    }
                    else {
                        FirebaseDatabase.getInstance().getReference().child("feedback").child(mAuth.getCurrentUser()
                                .getUid()).child("username").setValue(mAuth.getCurrentUser().getDisplayName());
                        FirebaseDatabase.getInstance().getReference().child("feedback").child(mAuth.getCurrentUser()
                                .getUid()).child("message").setValue(feed.getText().toString());
                        FancyToast.makeText(feedback.this, "feedback sent", FancyToast.
                                SUCCESS, FancyToast.LENGTH_SHORT, false).show();
                        feed.setText("");
                    }
            }

                else
                {
                    FancyToast.makeText(feedback.this,"check your internet connection",
                            FancyToast.WARNING,FancyToast.LENGTH_SHORT,false).show();
                }
            }
        });
    }

    private boolean connected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null && activeNetworkInfo.isConnected();
    }

}
