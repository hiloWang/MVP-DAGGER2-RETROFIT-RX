package com.github.hilo.data.net;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.util.concurrent.Callable;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class ApiConnection implements Callable<RestApi> {

    private static final String TAG = "API";
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";

    private String url;
    private Gson gson;
    private String response;

    private ApiConnection(String url, Gson gson) throws MalformedURLException {
        this.url = url;
        this.gson = gson;
    }

    public static ApiConnection createGET(String url, Gson gson) throws MalformedURLException {
        return new ApiConnection(url, gson);
    }

    /**
     * Do a request to an api synchronously.
     * It should not be executed in the main thread of the application.
     *
     * @return A string response
     */
    @Nullable
    public RestApi requestSyncCall() {
        return connectToApi();
    }

    @Override
    public RestApi call() throws Exception {
        return requestSyncCall();
    }

    private RestApi connectToApi() {
        return setupRetrofit();
    }

    private RestApi setupRetrofit() {
        // setup Retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(this.gson))
                .build();

        // setup square okHttpClient
//        OkHttpClient okHttpClient = retrofit.client();
//        okHttpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
//        okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
//
//        // Interceptor是拦截器, 在发送之前, 添加一些参数, 或者获取一些信息. loggingInterceptor是打印参数.
//        okHttpClient.interceptors().add(chain -> {
//            Response res = chain.proceed(chain.request());
//            this.response = res.body().string();
//            Log.i(TAG, chain.request().urlString() + "_" + res.body().string());
//            return res;
//        });

        // REST Api
        RestApi restApi = retrofit.create(RestApi.class);

        return restApi;
    }

}
