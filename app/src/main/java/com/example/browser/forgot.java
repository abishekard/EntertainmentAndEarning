package com.example.browser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.shashank.sony.fancytoastlib.FancyToast;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class forgot extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button reset;
    private EditText resEmail;
    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        reset = findViewById(R.id.reset);
        resEmail = findViewById(R.id.resEmail);
        mAuth = FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resEmail.getText().toString().equals(""))
                {
                    FancyToast.makeText(forgot.this,"empty field not allowed",FancyToast.ERROR,FancyToast.LENGTH_LONG,false).show();
                }
                else {
                    checkEmail();
                    try{
                    if (check) {
                        mAuth.sendPasswordResetEmail(resEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {
                                    final PrettyDialog dialog = new PrettyDialog(forgot.this);
                                    dialog.setTitle("Done").setMessage("password reset link han been sent to your mail")
                                            .addButton("Ok", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                } else {
                                    FancyToast.makeText(forgot.this, task.getException().getMessage(), FancyToast.ERROR, FancyToast.LENGTH_LONG, false).show();
                                }
                            }
                        });
                    } else {
                        FancyToast.makeText(forgot.this, "User not exist", FancyToast.ERROR, FancyToast.LENGTH_LONG, false).show();
                    }

                }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
    public void layoutTapp(View view){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public  boolean  checkEmail()
    {

        mAuth.fetchSignInMethodsForEmail(resEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if(task.isSuccessful())
                {
                    check=true;
                }
                else
                    check=false;

            }
        }) ;

         return check;
    }


}
