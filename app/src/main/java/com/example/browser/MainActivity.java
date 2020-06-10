package com.example.browser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText edtName,edtEmail,edtPass;
    private Button btnSignUp,btnLogIn;
    private TextView fpass;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fpass =findViewById(R.id.fpass);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.resEmail);
        edtPass = findViewById(R.id.edtPass);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!= null && mAuth.getCurrentUser().isEmailVerified()){
            transition();
            finish();
        }

        edtPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnSignUp);
                }
                return false;
            }
        });


    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnLogIn:
                Intent intent = new Intent(this,logIn.class);
                startActivity(intent);
                break;
            case R.id.btnSignUp:
                if(edtName.getText().toString().equals("") || edtEmail.getText().toString().equals("")||edtPass.getText().toString().equals(""))
                {
                    FancyToast.makeText(this,"no fields can be left empty",FancyToast.INFO,FancyToast.LENGTH_SHORT,false).show();
                }
                else
                {
                   try{
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("Loading");
                    dialog.show();
                    mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPass.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){


                                        FirebaseDatabase.getInstance().getReference().child("my_users").child(task.getResult().getUser()
                                                .getUid()).child("username").setValue(edtName.getText().toString());
                                        FirebaseDatabase.getInstance().getReference().child("my_users").child(task.getResult().getUser()
                                                .getUid()).child("points").setValue(0);
                                        FirebaseDatabase.getInstance().getReference().child("my_users").child(task.getResult().getUser()
                                                .getUid()).child("email").setValue(edtEmail.getText().toString());
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(edtName.getText().toString()).build();

                                        user.updateProfile(profileUpdates);

                                        dialog.dismiss();
                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    final PrettyDialog dialog = new PrettyDialog(MainActivity.this);
                                                    dialog.setTitle("Done").setMessage("registration link han been sent to your mail(Promotion section). Please click on that link to activate your account")
                                                            .setIcon(R.drawable.logoewy)
                                                            .addButton("Ok", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                                                                @Override
                                                                public void onClick() {
                                                                    dialog.dismiss();
                                                                }
                                                            }).show();

                                                }
                                                else
                                                {
                                                    FancyToast.makeText(MainActivity.this,task.getException().getMessage(),FancyToast.ERROR,FancyToast.LENGTH_SHORT,false).show();
                                                }
                                            }
                                        });

                                    }
                                    else{
                                        //FancyToast.makeText(MainActivity.this,"error",FancyToast.ERROR,FancyToast.LENGTH_SHORT,false).show();


                                        FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                        Toast.makeText(MainActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();


                                    }

                                }
                            }); }
                   catch (Exception e)
                   {
                       e.printStackTrace();
                   }

                }
                break;

        }
    }
    private void transition(){
        Intent intent = new Intent(MainActivity.this,profile.class);
        startActivity(intent);

    }
    public void layoutTap(View view){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void forgotPassword(View view)
    {
        Intent intent = new Intent(MainActivity.this,forgot.class);
        startActivity(intent);
    }
}
