package com.github.hilo.data.net;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.net.MalformedURLException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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
        OkHttpClient okHttpClient = this.setupOkHttp3();
        return setupRetrofit(okHttpClient);
    }

    private RestApi setupRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(this.gson))
                .build();
        RestApi restApi = retrofit.create(RestApi.class);
        return restApi;
    }

    private OkHttpClient setupOkHttp3() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
        okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);

        // Interceptor是拦截器, 在发送之前, 添加一些参数, 或者获取一些信息. loggingInterceptor是打印参数.
        okHttpClient.interceptors().add(chain -> {
            Response res = chain.proceed(chain.request());
            this.response = res.body().string();
            Log.i(TAG, chain.request().urlString() + "_" + res.body().string());
            return res;
        });

        return okHttpClient;
    }

}
