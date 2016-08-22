package kr.jsg1504.demo0824.utils.networks.retrofit;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author KangSung-Woo
 * @since 2016/07/04
 */
public class DefaultParameterBuilder {
  public static final int DEFAULT_TIMEOUT_SEC       = 10;
  public static final int DEFAULT_WRITE_TIMEOUT_SEC = 10;
  public static final int DEFAULT_READ_TIMEOUT_SEC  = 10;

  public static OkHttpClient getHttpLoggingInterceptor() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    return new OkHttpClient.Builder()
        .connectTimeout(DEFAULT_READ_TIMEOUT_SEC, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_READ_TIMEOUT_SEC, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build();
  }

  public static Retrofit getRetrofit(@NonNull OkHttpClient httpClient, @NonNull String baseUrl) {
    return new Retrofit.Builder()
        .client(httpClient)
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

}
