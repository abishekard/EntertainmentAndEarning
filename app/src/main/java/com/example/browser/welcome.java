package com.example.browser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import fragment.gam;
import fragment.hom;
import fragment.m;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class welcome extends AppCompatActivity {
    private ActionBar toolbar;
    private PublisherAdView adView;
    private FirebaseAuth mAuth;
    public final String NOTIFICATION_CHANNEL_ID = "channel_id";
    public final String CHANNEL_NAME = "Notification Channel";
    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    private int nVersion,oVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MobileAds.initialize(this,"ca-app-pub-4499095708288541~5287715383");

        //b ad
        adView = new PublisherAdView(this);
       // adView.setAdSizes(AdSize.BANNER);
       // adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        adView = findViewById(R.id.adView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        adView.loadAd(adRequest);
        //b ad

        //


        //

        oVersion=1;

        toolbar =getSupportActionBar();
        mAuth=FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);



        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

      //  toolbar.setTitle("home");

        loadFragment(new hom());



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkVersionAndNotify();

            }
        },5000);




    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home:
                  //  toolbar.setTitle("home");
                    fragment = new hom();
                    loadFragment(fragment);
                    return true;
                case R.id.games:
                  //  toolbar.setTitle("games");
                    fragment = new gam();
                    loadFragment(fragment);
                    return true;
                case R.id.me:
                  //  toolbar.setTitle("me");
                    fragment = new m();
                    loadFragment(fragment);
                    return true;

            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.logout:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
                FirebaseDatabase.getInstance().getReference().child("my_users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("token").setValue("");
                FirebaseAuth.getInstance().signOut();
                info user=new info(welcome.this);
                user.remove();

                finish();



                break;
            case R.id.about:
                final PrettyDialog dialog = new PrettyDialog(this);
                dialog.setTitle("About").setMessage("An app that can convert your time spent on youtube,tik-tok etc to money. More details are give in 'me' section. ")
                        .addButton("Ok", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        }).setIcon(R.drawable.logoewy).show();
                break;
            case R.id.feedback:
                Intent intent = new Intent(welcome.this,feedback.class);
                startActivity(intent);
                break;
            case R.id.withdraw:
                Intent intentt = new Intent(welcome.this,withdraw.class);
                startActivity(intentt);

                break;
            case R.id.update: Intent intent1 =new Intent(welcome.this,update.class);
                      startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();


        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (currentFragment instanceof m) {
            FragmentTransaction fragTransaction =   getSupportFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();}

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("update").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                info.nVersion=dataSnapshot.child("version").getValue().toString();
                nVersion=Integer.parseInt(info.nVersion);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void checkVersionAndNotify()
    {
        if((nVersion) > (oVersion))
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.setVibrationPattern(new long[] {
                        500,
                        500,
                        500,
                        500,
                        500
                });
                //Sets whether notifications from these Channel should be visible on Lockscreen or not
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);
            }





            Intent Nintent = new Intent(this,update.class);
            Nintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, Nintent, 0);


            NotificationCompat.Builder builder =new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("New update available").setContentText("Please update your app to use our new features")
                    .setSmallIcon(R.drawable.nicon).setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());


        }


    }

}

