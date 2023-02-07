package com.dcm.speedloans.api;

import com.dcm.speedloans.models.CheckEmailModel;
import com.dcm.speedloans.models.CheckMSISDNmodel;
import com.dcm.speedloans.models.CheckMSISDNresult;
import com.dcm.speedloans.models.PostLoginModel;
import com.dcm.speedloans.models.PostRegisterModel;
import com.dcm.speedloans.models.PreLoginModel;
import com.dcm.speedloans.models.PreRegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MtechAPIInterface {

    @POST("login")
    Call<PostLoginModel> login(@Body PreLoginModel preLoginModel);

    @POST("register")
    Call<PostRegisterModel> registerUser(@Body PreRegisterModel registerDetailsModel);

    @POST("checkmsisdn")
    Call<CheckMSISDNresult> checkMSISDN(@Body CheckMSISDNmodel checkMSISDNmodel);

    @POST("checkemail")
    Call<CheckMSISDNresult> checkEmail(@Body CheckEmailModel checkEmailModel);
}
