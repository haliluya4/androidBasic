package com.jx.framework.network;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 服务端管理器，维护各服务端的Retrofit实例，便于复用
 */
public final class XServerManager {

    /**
     * 超时时间10s
     */
    private static final int HTTP_CONNECTION_TIMEOUT = 10 * 1000;

    /**
     * 不同服务器的实体，根据baseUrl区分
     */
    private static final Map<String, Retrofit> sServers = new HashMap<>();

    /**
     * 获取服务器实例，每个不同的base url对应一个独立的服务器实例
     *
     * @param baseUrl          服务器的基础url
     * @param xHeaderGenerator 请求头生成器，如果不提供，表示不需要自定义
     * @param isDebug          是否调试模式，调试模式下会输出请求内容
     * @return 对应的Retrofit对象
     */
    public static Retrofit getServerInstance(String baseUrl, XHeaderGenerator xHeaderGenerator, boolean isDebug) {
        if (!sServers.containsKey(baseUrl)) {
            synchronized (XServerManager.class) {
                if (!sServers.containsKey(baseUrl)) {
                    sServers.put(baseUrl, createInstance(baseUrl, xHeaderGenerator, isDebug));
                }
                return sServers.get(baseUrl);
            }
        }
        return sServers.get(baseUrl);
    }

    private static Retrofit createInstance(String baseUrl, final XHeaderGenerator xHeaderGenerator, final boolean isDebug) {
        // 配置日志输出，因为Retrofit2不支持输出日志，只能用OkHttp来输出
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (isDebug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor);

        if (xHeaderGenerator != null) {
            // 有配请求头生成器才加拦截器
            Interceptor netInterceptor = new Interceptor() {
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Map<String, String> headers = xHeaderGenerator.getHeader();
                    if (headers == null || headers.isEmpty()) {
                        return chain.proceed(originalRequest);
                    }

                    Request.Builder headerBuilder = originalRequest.newBuilder();
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        headerBuilder = headerBuilder.addHeader(header.getKey(), header.getValue());
                    }
                    return chain.proceed(headerBuilder.build());
                }
            };
            builder = builder.addNetworkInterceptor(netInterceptor);
        }

        OkHttpClient client = builder.retryOnConnectionFailure(true).build();

        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static void destroy() {
        sServers.clear();
    }
}
