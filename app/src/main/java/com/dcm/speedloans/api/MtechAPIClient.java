package com.dcm.speedloans.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MtechAPIClient {
    private static Retrofit retrofit;
    private static String BASE_URL = "http://52.206.231.182/nlp/";

    public static Retrofit getRetrofitInstance(){


        //OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request  request = chain.request();
                Request.Builder newRequestBuilder = request.newBuilder().addHeader("Authorization","Basic bXRlY2hjb21tOnBhNTV3MHJk");

                return chain.proceed(newRequestBuilder.build());
            }
        });

        OkHttpClient client = httpClient.build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null){
            retrofit= new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
