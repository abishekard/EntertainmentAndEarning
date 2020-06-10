package com.example.browser;

import android.app.ProgressDialog;
import  android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shashank.sony.fancytoastlib.FancyToast;

public class logIn extends AppCompatActivity implements View.OnClickListener {
    private Button btnLlogin, btnLsignup;
    private EditText edtLname, edtLpass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_in);
        btnLlogin = findViewById(R.id.btnLlogin);
        btnLsignup = findViewById(R.id.btnLsignup);
        edtLpass = findViewById(R.id.edtLpass);
        edtLname = findViewById(R.id.edtLname);
        btnLsignup.setOnClickListener(this);
        btnLlogin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        if(mAuth.getCurrentUser()!= null && mAuth.getCurrentUser().isEmailVerified()){

            transition();
            finish();
        }


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLlogin:


                btnLlogin.setEnabled(false);
                if(edtLname.getText().toString().equals("") || edtLpass.getText().toString().equals(""))
                {
                    FancyToast.makeText(logIn.this,"no fields can be left empty",FancyToast.INFO,FancyToast.
                            LENGTH_SHORT,false).show();
                    Button button1 = findViewById(R.id.btnLlogin);
                    button1.setEnabled(true);
                }
                else
                {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("loading");
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(edtLname.getText().toString(),edtLpass.getText().toString())
                            .addOnCompleteListener(logIn.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        if(mAuth.getCurrentUser().isEmailVerified())
                                        {



                                            FirebaseInstanceId.getInstance().getInstanceId()
                                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                            if (!task.isSuccessful()) {

                                                                return;
                                                            }

                                                            // Get new Instance ID token
                                                            String token = task.getResult().getToken();

                                                            FirebaseDatabase.getInstance().getReference().child("my_users").child(mAuth.getCurrentUser()
                                                                    .getUid()).child("token").setValue(token);
                                                        }
                                                    });


                                            FirebaseMessaging.getInstance().subscribeToTopic("all")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            String msg = "welcome";
                                                            if (!task.isSuccessful()) {
                                                                msg = "topic error";
                                                            }

                                                            Toast.makeText(logIn.this, msg, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            //  setting detail in sharedpreference
                                              getDetails(edtLname.getText().toString());



                                            FancyToast.makeText(logIn.this,"Log In successful",FancyToast.SUCCESS,FancyToast.LENGTH_SHORT,false).show();

                                        transition();
                                        }
                                        else{
                                            FancyToast.makeText(logIn.this,"Please verify your Email",FancyToast.INFO,FancyToast
                                                    .LENGTH_LONG,false).show();
                                            btnLlogin.setEnabled(true);
                                        }
                                    }
                                    else{



                                        FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                        Toast.makeText(logIn.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        btnLlogin.setEnabled(true);


                                    }
                                }
                            });

                }
                break;
            case R.id.btnLsignup:
                btnLsignup.setEnabled(false);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);

                break;
        }

}
    private void transition(){
        Intent intent = new Intent(logIn.this,profile.class);
        startActivity(intent);

    }
    public void layoutLTap(View view){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        Button button1 = findViewById(R.id.btnLlogin);
        button1.setEnabled(true);
        Button button2 =findViewById(R.id.btnLsignup);
        button2.setEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    public void getDetails(final String email){
        FirebaseDatabase.getInstance().getReference().child("my_users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("username").getValue().toString();
                String points=dataSnapshot.child("points").getValue().toString();
                info user = new info(logIn.this);
                user.setUserName(name);
                user.setPoints(points);
                user.setEmail(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
