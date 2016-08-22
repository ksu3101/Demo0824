package kr.jsg1504.demo0824.utils.networks;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author KangSung-Woo
 * @since 2016/08/22
 */
public interface NJobInterface {
  String NET_CONTENT_TYPE = "Content-Type: application/x-www-form-urlencoded; charset=utf-8";

  @Headers(NET_CONTENT_TYPE)
  @GET("/test/state/server/{id}")
  Observable<ResponseBody> retrieveServerState(@Path("id") String id);

  @Headers(NET_CONTENT_TYPE)
  @GET("/test/start/server/{id}")
  Observable<ResponseBody> sendStartSignal(@Path("id") String id);

  @Headers(NET_CONTENT_TYPE)
  @GET("/test/stop/server/{id}")
  Observable<ResponseBody> sendStopSignal(@Path("id") String id);

}
