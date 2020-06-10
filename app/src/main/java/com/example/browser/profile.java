package com.example.browser;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import android.widget.TextView;

public class profile extends AppCompatActivity  {

    private CardView sIcon;
    private TextView sName,sfName;
    private String pkgVersion;
    private TextView versionView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        sIcon = findViewById(R.id.sIcon);
        sName = findViewById(R.id.sName);
        sfName = findViewById(R.id.sfName);
        versionView = findViewById(R.id.versionView);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            pkgVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionView.setText("Version  "+pkgVersion);

        Handler handler=new Handler();
        handler.postDelayed(splash,4000);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.red));
        }
    }


    private Runnable splash = new Runnable() {
        public void run() {
            Intent i = new Intent(profile.this,welcome.class);
            startActivity(i);
            finish();
        }
    };

}
