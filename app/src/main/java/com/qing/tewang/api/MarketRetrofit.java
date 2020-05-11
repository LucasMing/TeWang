package com.qing.tewang.api;


import com.qing.tewang.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wuliao on 2018/3/22.
 */

public class MarketRetrofit {

    private MarketApi mMarketService;

    private static MarketRetrofit sInstance;

    public static MarketRetrofit getsInstance() {
        if (sInstance == null) {
            sInstance = new MarketRetrofit();
        }
        return sInstance;
    }

    private MarketRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }


        OkHttpClient client = httpClient.build();

        mMarketService = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://test.vlean.xyz/api/")
                //rx与Gson混用
                .addConverterFactory(GsonConverterFactory.create())
                //rx与retrofit混用
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MarketApi.class);

    }

    public MarketApi getMarketService() {
        return mMarketService;
    }

}
