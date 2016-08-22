package kr.jsg1504.demo0824.utils.networks.retrofit;

import android.util.Log;

import kr.jsg1504.demo0824.BuildConfig;
import kr.jsg1504.demo0824.utils.networks.NJobInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author KangSung-Woo
 * @since 2016/08/22
 */
public class RetrofitAdapter {
  private static NJobInterface nJobInterface;

  public synchronized static NJobInterface getInstance() {
    if (nJobInterface == null) {
      OkHttpClient httpClient = DefaultParameterBuilder.getHttpLoggingInterceptor();

      // retrofit configures
      final String baseUrl = BuildConfig.DEMO_SERVER_BASE_URL + ":" + BuildConfig.DEMO_SERVER_BASE_PORT;
      Retrofit retrofit = DefaultParameterBuilder.getRetrofit(httpClient, baseUrl);
      nJobInterface = retrofit.create(NJobInterface.class);
    }
    return nJobInterface;
  }

}
