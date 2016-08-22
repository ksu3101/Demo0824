package kr.jsg1504.demo0824;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import kr.jsg1504.demo0824.model.ResultCode;
import kr.jsg1504.demo0824.utils.SwObservable;
import kr.jsg1504.demo0824.utils.mvp.BasePresenter;
import kr.jsg1504.demo0824.utils.mvp.BaseView;
import kr.jsg1504.demo0824.utils.networks.retrofit.RetrofitAdapter;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author KangSung-Woo
 * @since 2016/08/22
 */
public class MainActiviyPresenter
    extends BasePresenter {
  private static final String TAG = MainActiviyPresenter.class.getSimpleName();

  private View view;

  public MainActiviyPresenter(@NonNull View viewImplInstance) {
    this.view = viewImplInstance;
  }

  public void retrieveServerState(@NonNull String id) {
    SwObservable observable = new SwObservable(
        this,
        RetrofitAdapter.getInstance()
                       .retrieveServerState(id)
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
    );
    observable.subscribe(
        new Subscriber<ResponseBody>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            if (view != null) {
              view.onError(TAG, e.getMessage());
            }
          }

          @Override
          public void onNext(ResponseBody responseBody) {
            if (view != null) {
              view.onResultRetrieveServerState(parseResultCode(responseBody));
            }
            onCompleted();
          }
        }
    );
  }

  public void sendStartSignalToServer(@NonNull String id) {
    SwObservable observable = new SwObservable(
        this,
        RetrofitAdapter.getInstance()
                       .sendStartSignal(id)
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
    );
    observable.subscribe(
        new Subscriber<ResponseBody>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            if (view != null) {
              view.onError(TAG, e.getMessage());
            }
          }

          @Override
          public void onNext(ResponseBody responseBody) {
            if (view != null) {
              view.sendStartSignalCompleted(parseResultCode(responseBody));
            }
            onCompleted();
          }
        }
    );
  }

  public void sendStopSignalToServer(@NonNull String id) {
    SwObservable observable = new SwObservable(
        this,
        RetrofitAdapter.getInstance()
                       .sendStopSignal(id)
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
    );
    observable.subscribe(
        new Subscriber<ResponseBody>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            if (view != null) {
              view.onError(TAG, e.getMessage());
            }
          }

          @Override
          public void onNext(ResponseBody responseBody) {
            if (view != null) {
              view.sendStopSignalCompleted(parseResultCode(responseBody));
            }
            onCompleted();
          }
        }
    );
  }

  private ResultCode parseResultCode(ResponseBody responseBody) {
    if (responseBody != null) {
      try {
        final String bodyStr = responseBody.string();

        JSONObject rootObj = new JSONObject(bodyStr);
        if (rootObj.has("code")) {
          return ResultCode.parseFromValue(rootObj.getString("code"));
        }

      } catch (Exception ex) {
        return null;
      }
    }
    return null;
  }

  public interface View
      extends BaseView {
    void onResultRetrieveServerState(@NonNull ResultCode resultCode);

    void sendStartSignalCompleted(@NonNull ResultCode resultCode);

    void sendStopSignalCompleted(@NonNull ResultCode resultCode);
  }

}
