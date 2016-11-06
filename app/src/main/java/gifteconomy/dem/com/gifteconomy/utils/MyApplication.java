package gifteconomy.dem.com.gifteconomy.utils;

import android.app.Application;

import gifteconomy.dem.com.gifteconomy.constant.Cons;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Workstation01 on 7/20/2016.
 */
public class MyApplication extends Application {

    public static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = new Retrofit.Builder()
                .baseUrl(Cons.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
