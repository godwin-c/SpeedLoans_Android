package com.dcm.speedloans.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.dcm.speedloans.models.PreRegisterModel;

public class LocalDataBase {

    public static class UserDatabase {
        public static final String DB_NAME = "userDetails";

        SharedPreferences userLocalDB;

        public UserDatabase(Context context) {
            userLocalDB = context.getSharedPreferences(DB_NAME, 0);
        }
    }

    public static class OtherDatabase {
        public static final String OTHER_NAME = "otherDetails";

        SharedPreferences otherLocalDB;

        public OtherDatabase(Context context) {
            otherLocalDB = context.getSharedPreferences(OTHER_NAME, 0);
        }

        public void storePreRegisterModel(PreRegisterModel preRegisterModel) {
            SharedPreferences.Editor editor = otherLocalDB.edit();

            editor.putString("firstname", preRegisterModel.getFirstname());
            editor.putString("lastname", preRegisterModel.getLastname());
            editor.putString("phonenumber", preRegisterModel.getPhonenumber());
            editor.putString("email", preRegisterModel.getEmail());
            editor.putString("dob",preRegisterModel.getDob());
            editor.putString("secret_pin", preRegisterModel.getPassword());
            editor.putString("number_verified", preRegisterModel.getNumberverified());
            editor.putString("accountname", preRegisterModel.getAccountname());
            editor.putString("accountnumber", preRegisterModel.getAccountno());

            editor.commit();
        }

        public PreRegisterModel retrievePreRegisterModel(){
            PreRegisterModel preRegisterModel = new PreRegisterModel(
                    otherLocalDB.getString("firstname",""),
                    otherLocalDB.getString("lastname",""),
                    otherLocalDB.getString("phonenumber",""),
                    otherLocalDB.getString("email",""),
                    otherLocalDB.getString("dob",""),
                    otherLocalDB.getString("secret_pin",""),
                    otherLocalDB.getString("number_verified","false"),
                    otherLocalDB.getString("accountname",""),
                    otherLocalDB.getString("accountnumber","")

            );
                    return preRegisterModel;
        }
    }
}
