package com.github.hilo.data.net;

import android.support.annotation.Nullable;
import android.util.Log;

import com.github.hilo.data.entity.mapper.UserEntityGsonMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

@Singleton
public class ApiConnectionImpl implements ApiConnection {

    private static final String TAG = "API";
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";
    //    String API_BASE_URL = "http://m.1332255.com";
    private static final String API_BASE_URL = "http://gank.io/";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private final Retrofit retrofit;


    private String response;

    @Inject
    public ApiConnectionImpl(UserEntityGsonMapper userEntityGsonMapper) {
        // setup Retrofit
        retrofit = new Retrofit.Builder().baseUrl(API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(userEntityGsonMapper.getGson()))
                .build();

        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                Log.w("Error",e);
            }
        });

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
    }

    /**
     * Do a request to an api synchronously.
     * It should not be executed in the main thread of the application.
     *
     * @return A string response
     */
    @Nullable
    @Override
    public RestApi requestSyncCall() {
        return connectToApi();
    }

    @Override
    public RestApi call() throws Exception {
        return requestSyncCall();
    }

    private RestApi connectToApi() {
        try {
            RestApi restApi = retrofit.create(RestApi.class);
            return restApi;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
