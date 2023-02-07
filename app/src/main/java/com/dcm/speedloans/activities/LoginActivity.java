package com.dcm.speedloans.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dcm.speedloans.MainActivity;
import com.dcm.speedloans.R;
import com.dcm.speedloans.api.MtechAPIClient;
import com.dcm.speedloans.api.MtechAPIInterface;
import com.dcm.speedloans.classes.CustomCallBack;
import com.dcm.speedloans.classes.MyErrorClass;
import com.dcm.speedloans.classes.ValidateEditText;
import com.dcm.speedloans.models.PostLoginModel;
import com.dcm.speedloans.models.PreLoginModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button register = this.findViewById(R.id.btn_login_activity_register);
        EditText edt_username = findViewById(R.id.edt_login_activity_username);
        EditText edt_password = findViewById(R.id.edt_login_activity_password);

//        edt_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    hideKeyboard(v);
//                }
//            }
//        });

//        edt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    hideKeyboard(v);
//                }
//            }
//        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        Button login = findViewById(R.id.btn_login_activity_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String username;
                String password;

                ValidateEditText validateEditText = new ValidateEditText(edt_username);
                if (validateEditText.isValid()){
                    username = edt_username.getText().toString();

                    Log.d(TAG, "Username: " + username);
                    ValidateEditText validatePassword = new ValidateEditText(edt_password);
                    if (validatePassword.isValid()){
                        password = edt_password.getText().toString();

                        Log.d(TAG, "Password: " + password);
                        performLoginOperation(username,password);

                    }else {
                        Toast.makeText(LoginActivity.this,"Password can not be empty",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    edt_username.setError("username expected");
                    return;
                }

            }
        });
    }

    private void performLoginOperation(String username, String password) {


        MtechAPIInterface mtechAPIInterface = MtechAPIClient.getRetrofitInstance().create(MtechAPIInterface.class);

        Log.d(TAG, "performLoginOperation 111: " + username + " Password : " + password);
        PreLoginModel preLoginModel = new PreLoginModel(username,password);
        Call<PostLoginModel> call =mtechAPIInterface.login(preLoginModel);

        Log.d(TAG, "performLoginOperation 222: " + username + " Password : " + password);

        call.enqueue(new CustomCallBack<>(LoginActivity.this, new Callback<PostLoginModel>() {
            @Override
            public void onResponse(Call<PostLoginModel> call, Response<PostLoginModel> response) {

                int responseCode = response.code();
                switch (responseCode){
                    case 201:

                        PostLoginModel postLoginModel = response.body();
                        Log.d(TAG, "onResponse: Username : " + postLoginModel.getFirstname());
                        Toast.makeText(LoginActivity.this, "login successful "+ postLoginModel.getFirstname(),Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(i);
                        break;
                    default:
                        Gson gson = new Gson();
                        Type type = new TypeToken<MyErrorClass>() {
                        }.getType();
                        MyErrorClass errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                        Toast.makeText(LoginActivity.this, "an Error occurred '" + errorResponse.getError() + "'", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<PostLoginModel> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Could not communicate with our Servers", Toast.LENGTH_SHORT).show();
            }
        }));
    }

//    public void hideKeyboard(View view) {
//        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

}