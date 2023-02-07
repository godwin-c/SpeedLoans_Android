package com.dcm.speedloans.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dcm.speedloans.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;

    private static final String TAG = VerifyPhoneActivity.class.getSimpleName();
    private static final int MY_REQUEST_CODE = 1201;

    Button btn_sms_code_verify_code, btn_sms_code_req_new_code;

    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile"); //if it's a string you stored.
        //source = intent.getStringExtra("source");

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);

        btn_sms_code_verify_code = findViewById(R.id.btn_sms_code_verify_code);
        btn_sms_code_req_new_code = findViewById(R.id.btn_sms_code_req_new_code);

        btn_sms_code_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

        btn_sms_code_req_new_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

        sendVerificationCode();
    }

    private void sendVerificationCode() {

        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(this)
                        .setPhoneNumber(mobile)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mCallbacks)
                        .build());

    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            //signing the user
            signInWithPhoneAuthCredential(credential);

        }catch (Exception e){
//            DesignerToast.Error(this,"Verification Code is Wrong", Gravity.CENTER,Toast.LENGTH_SHORT);
            Toast.makeText(VerifyPhoneActivity.this, "Verification Code is wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(VerifyPhoneActivity.this, "Phone number has been verified", Toast.LENGTH_SHORT).show();
                            

//                            if (source.equals("register")){

//                                phoneVerified(true);

                                Intent intent = new Intent();
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                                

//                            }else if(source.equals("login")){
//                                //Toast.makeText(VerifyPhoneActivity.this,appUser.getSurname() + ", you are welcome",Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(VerifyPhoneActivity.this, DashboardActivity.class);
//                                startActivity(intent);
//                                finish();
//
//                            }


                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Something went wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Exit", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   // if (source.equals("register")){

//                                        phoneVerified(false);

                                        Intent intent = new Intent();
                                        //intent.putExtra("fragmentName", "RegDetailsFragment");
                                        setResult(Activity.RESULT_CANCELED, intent);
                                        finish();

//                                    }else {
//                                        finish();
//                                    }
                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
}