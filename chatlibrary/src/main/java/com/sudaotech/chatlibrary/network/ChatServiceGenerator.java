package com.sudaotech.chatlibrary.network;

import com.sudao.basemodule.base.BundleConst;
import com.sudao.basemodule.http.AddCookiesInterceptor;
import com.sudao.basemodule.http.LoggingInterceptor;
import com.sudao.basemodule.http.ReceivedCookiesInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Samuel on 5/23/16 19:03
 * Email:xuzhou40@gmail.com
 * desc: service生成类
 */
public class ChatServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BundleConst.getChatUrl())
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        httpClient.addInterceptor(new AddCookiesInterceptor());
        httpClient.addInterceptor(new ReceivedCookiesInterceptor());
        httpClient.addInterceptor(new LoggingInterceptor());

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
