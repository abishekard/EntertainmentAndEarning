package com.example.browser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class update extends AppCompatActivity {

    private int oversion;
    private FirebaseAuth mAuth;
    private Button check;
    private int nVersion;
    private String updateUrl;
    private TextView link;
    private LottieAnimationView vLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        oversion=1;
        mAuth = FirebaseAuth.getInstance();
        link =findViewById(R.id.link);
        check = findViewById(R.id.check);
        vLoad = findViewById(R.id.vLoad);
        updateUrl="";
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vLoad.setVisibility(View.VISIBLE);
                check.setVisibility(View.GONE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vLoad.setVisibility(View.GONE);
                        if(connected()) {
                            versionCheck();
                        }
                        else
                        {
                            FancyToast.makeText(update.this,"check your internet connection",
                                    FancyToast.WARNING,FancyToast.LENGTH_SHORT,false).show();
                        }
                    }
                },3000);


            }
        });

        link.setText("click to get update");
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (updateUrl != "") {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(updateUrl));
                        startActivity(browserIntent);
                    } else {
                        FancyToast.makeText(update.this, "server Problem", FancyToast.ERROR, FancyToast.LENGTH_LONG, false).show();
                    }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference().child("update").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nVersion=Integer.valueOf(dataSnapshot.child("version").getValue().toString());
                updateUrl=dataSnapshot.child("link").getValue().toString();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void versionCheck()
    {
        if(nVersion > oversion)
        {
            link.setVisibility(View.VISIBLE);
        }
        else{
            final PrettyDialog dialog = new PrettyDialog(this);
            dialog.setTitle("no update").setMessage("App is up to date ")
                    .addButton("Ok", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                            finish();
                        }
                    }).setIcon(R.drawable.logoewy).show();

        }
    }

    private boolean connected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null && activeNetworkInfo.isConnected();
    }
}
