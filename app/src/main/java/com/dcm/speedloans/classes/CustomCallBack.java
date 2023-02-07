package com.dcm.speedloans.classes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import com.devhoony.lottieproegressdialog.LottieProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomCallBack<T> implements Callback<T> {
    @SuppressWarnings("unused")
    private static final String TAG = "RetrofitCustomCallback";
    Context context;
    private final Callback<T> mCallback;

    LottieProgressDialog dialog0;


    public CustomCallBack(Context context, Callback<T> callback) {
        this.context = context;
        this.mCallback = callback;


        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


       dialog0 = new LottieProgressDialog(context,true,null,null,null,null,LottieProgressDialog.SAMPLE_1,null,null);
       dialog0.show();

    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (dialog0.isShowing()) {
            dialog0.dismiss();
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        mCallback.onResponse(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

        if (dialog0.isShowing()) {
            dialog0.dismiss();
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        mCallback.onFailure(call, t);
    }
}
