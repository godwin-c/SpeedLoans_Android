package com.dcm.speedloans.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dcm.speedloans.R;
import com.dcm.speedloans.api.MtechAPIClient;
import com.dcm.speedloans.api.MtechAPIInterface;
import com.dcm.speedloans.classes.CustomCallBack;
import com.dcm.speedloans.classes.MyErrorClass;
import com.dcm.speedloans.classes.SoftInputAssist;
import com.dcm.speedloans.classes.ValidateEditText;
import com.dcm.speedloans.database.LocalDataBase;
import com.dcm.speedloans.models.CheckEmailModel;
import com.dcm.speedloans.models.CheckMSISDNmodel;
import com.dcm.speedloans.models.CheckMSISDNresult;
import com.dcm.speedloans.models.PostRegisterModel;
import com.dcm.speedloans.models.PreRegisterModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1111;
    private static final String TAG = "RegActivity ERROR";
    CountryCodePicker ccp;
    EditText edtPhoneNumber;
    Button btn_reg_activity_register;
    SoftInputAssist softInputAssist;

    EditText edt_reg_activity_first_name, edt_reg_activity_last_name, edt_reg_activity_email_address,
            edt_reg_activity_username, edt_reg_activity_secret_pin, edt_reg_activity_confirm_scrt_pin;

    TextView txt_reg_activity_sign_in;

//    ActivityResultLauncher<Intent> verifyPhoneNumberActivity = registerForActivityResult(
//            new StartActivityForResultContract(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult activityResult) {
//                    int result = activityResult.getResultCode();
//                    Intent data = activityResult.getData();
//
//                    String numberVerified;
//                    if (result == RESULT_OK){
//                        Toast.makeText(RegisterActivity.this, "Phone Number Verified", Toast.LENGTH_SHORT).show();
//                        numberVerified = "true";
//                    }else{
//                        Toast.makeText(RegisterActivity.this, "Phone Number not verified.", Toast.LENGTH_SHORT).show();
//                        numberVerified = "false";
//                    }
//
//                    LocalDataBase.OtherDatabase otherDatabase = new LocalDataBase.OtherDatabase(RegisterActivity.this);
//
//                    PreRegisterModel preRegisterModel = otherDatabase.retrievePreRegisterModel();
//                    preRegisterModel.setNumberverified(numberVerified);
//
//                    registerUser(preRegisterModel);
//                    //PreRegisterModel registerModel = new PreRegisterModel(Fname, Lname, phonenumber, email, "", secret_pin, numberVerified, "", "");
//                }
//            }
//    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        softInputAssist = new SoftInputAssist(this);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        edtPhoneNumber = findViewById(R.id.phone_number_edt);
        //All Edittext Views
        edt_reg_activity_first_name = findViewById(R.id.edt_reg_activity_first_name);
        edt_reg_activity_last_name = findViewById(R.id.edt_reg_activity_last_name);
        edt_reg_activity_email_address = findViewById(R.id.edt_reg_activity_email_address);
        edt_reg_activity_username = findViewById(R.id.edt_reg_activity_username);
        edt_reg_activity_secret_pin = findViewById(R.id.edt_reg_activity_secret_pin);
        edt_reg_activity_confirm_scrt_pin = findViewById(R.id.edt_reg_activity_confirm_scrt_pin);

        btn_reg_activity_register = findViewById(R.id.btn_reg_activity_register);


        ccp.registerPhoneNumberTextView(edtPhoneNumber);

        btn_reg_activity_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Fname, Lname, email, username, secret_pin, confirm_secret_pin, phonenumber = "";

                ValidateEditText validateFname = new ValidateEditText(edt_reg_activity_first_name);
                if (validateFname.isValid()) {
                    Fname = edt_reg_activity_first_name.getText().toString();
                } else {
                    edt_reg_activity_first_name.setError("First name needed");
                    return;
                }

                ValidateEditText validateLname = new ValidateEditText(edt_reg_activity_last_name);
                if (validateLname.isValid()) {
                    Lname = edt_reg_activity_last_name.getText().toString();
                } else {
                    edt_reg_activity_last_name.setError("Last Name needed");
                    return;
                }

                ValidateEditText validateEmail = new ValidateEditText(edt_reg_activity_email_address);
                if (validateEmail.isValidEmail()) {
                    email = edt_reg_activity_email_address.getText().toString();
                } else {
                    edt_reg_activity_email_address.setError("invalid email");
                    return;
                }

                ValidateEditText validateUsername = new ValidateEditText(edt_reg_activity_username);
                if (validateUsername.isValid()) {
                    username = edt_reg_activity_username.getText().toString();
                } else {
                    edt_reg_activity_username.setError("username needed");
                    return;
                }

                ValidateEditText validateSecretPin = new ValidateEditText(edt_reg_activity_secret_pin);
                if (validateSecretPin.isValid()) {
                    secret_pin = edt_reg_activity_secret_pin.getText().toString();
                } else {
                    edt_reg_activity_secret_pin.setError("Password Needed");
                    return;
                }

                if (!(edt_reg_activity_confirm_scrt_pin.getText().toString()).matches(secret_pin)) {
                    Toast.makeText(RegisterActivity.this, "your Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                String country_code = ccp.getSelectedCountryCode();
                String number = ccp.getNumber();
                int num_lenght = number.length();
                //String new_number = "";



                if (num_lenght < 10) {
                    Toast.makeText(RegisterActivity.this, "Phone Number is incomplete", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (number.startsWith("0")) {
                    number = number.substring(1, num_lenght);
                }

                phonenumber = number;

                Toast.makeText(RegisterActivity.this, "Phone NUMBER : " + phonenumber, Toast.LENGTH_SHORT).show();
                PreRegisterModel preRegisterModel = new PreRegisterModel(Fname, Lname, phonenumber, email, "", secret_pin, "false", "", "");

                LocalDataBase.OtherDatabase otherDatabase = new LocalDataBase.OtherDatabase(RegisterActivity.this);
                otherDatabase.storePreRegisterModel(preRegisterModel);

                //Check if number Exists
                checkPhoneNumber(phonenumber, email);

            }
        });


    }

    private void checkPhoneNumber(String phoneNumber, String email) {
        MtechAPIInterface mtech_api_interface = MtechAPIClient.getRetrofitInstance().create(MtechAPIInterface.class);
        Call<CheckMSISDNresult> call = mtech_api_interface.checkMSISDN(new CheckMSISDNmodel(phoneNumber));

        call.enqueue(new CustomCallBack<>(RegisterActivity.this, new Callback<CheckMSISDNresult>() {
            @Override
            public void onResponse(Call<CheckMSISDNresult> call, Response<CheckMSISDNresult> response) {

                if (response.code() == 201) {
                    Toast.makeText(RegisterActivity.this, "this phone number has already been used in another account", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 400) {
                    checkIfEmailExists(email, phoneNumber);
                }
            }

            @Override
            public void onFailure(Call<CheckMSISDNresult> call, Throwable t) {

                Toast.makeText(RegisterActivity.this, "Error while checking phone number '" + t.getMessage() + "'", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void checkIfEmailExists(String emailAddress, String phonenumber) {
        MtechAPIInterface mtech_api_interface = MtechAPIClient.getRetrofitInstance().create(MtechAPIInterface.class);
        Call<CheckMSISDNresult> call = mtech_api_interface.checkEmail(new CheckEmailModel(emailAddress));

        call.enqueue(new CustomCallBack<>(RegisterActivity.this, new Callback<CheckMSISDNresult>() {
            @Override
            public void onResponse(Call<CheckMSISDNresult> call, Response<CheckMSISDNresult> response) {

                if (response.code() == 201) {
                    Toast.makeText(RegisterActivity.this, "this email has been used to register another account", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {

                    Intent intent = new Intent(RegisterActivity.this, VerifyPhoneActivity.class);
//                verifyPhoneNumberActivity.launch(intent);
                    intent.putExtra("mobile", phonenumber);
                    startActivityForResult(intent, MY_REQUEST_CODE);
                }

            }

            @Override
            public void onFailure(Call<CheckMSISDNresult> call, Throwable t) {

                Toast.makeText(RegisterActivity.this, "an Error occurred while verifying your email. '" + t.getMessage() + "'", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void registerUser(PreRegisterModel preRegisterModel) {
        MtechAPIInterface mtechAPIInterface = MtechAPIClient.getRetrofitInstance().create(MtechAPIInterface.class);
        Call<PostRegisterModel> call = mtechAPIInterface.registerUser(preRegisterModel);

        call.enqueue(new CustomCallBack<>(RegisterActivity.this, new Callback<PostRegisterModel>() {
            @Override
            public void onResponse(Call<PostRegisterModel> call, Response<PostRegisterModel> response) {

                if (response.code() == 201) {

                    PostRegisterModel model = response.body();

                    //verification successful we will start the profile activity
                    Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    finish();

                } else {
                    Gson gson = new Gson();
                    Type type = new TypeToken<MyErrorClass>() {
                    }.getType();
                    MyErrorClass errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                    Log.d(TAG, "onResponse: " + errorResponse.getError());
                    Toast.makeText(RegisterActivity.this, "an Error occurred '" + errorResponse.getError() + "'", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostRegisterModel> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "an Error has occurred '" + t.getMessage() + "'", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        }));
    }

    @Override
    protected void onResume() {
        softInputAssist.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        softInputAssist.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        softInputAssist.onDestroy();
        super.onDestroy();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == MY_REQUEST_CODE) {

            String numberVerified = "false";

            if (resultCode == Activity.RESULT_OK) {
                numberVerified = "true";
                //Toast.makeText(RegisterActivity.this, "Phone Number Verified", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Phone Number could not be verified.", Toast.LENGTH_SHORT).show();
            }


            LocalDataBase.OtherDatabase otherDatabase = new LocalDataBase.OtherDatabase(RegisterActivity.this);

            PreRegisterModel preRegisterModel = otherDatabase.retrievePreRegisterModel();
            preRegisterModel.setNumberverified(numberVerified);

            registerUser(preRegisterModel);
            //PreRegisterModel registerModel = new PreRegisterModel(Fname, Lname, phonenumber, email, "", secret_pin, numberVerified, "", "");


        } else {
            Toast.makeText(this, "Something has gone wrong", Toast.LENGTH_SHORT).show();
        }
    }
}