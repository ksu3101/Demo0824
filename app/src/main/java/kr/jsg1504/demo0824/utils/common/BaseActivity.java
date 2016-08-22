package kr.jsg1504.demo0824.utils.common;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import kr.jsg1504.demo0824.utils.mvp.BasePresenter;
import kr.jsg1504.demo0824.utils.mvp.BaseView;

/**
 * @author KangSung-Woo
 * @since 2016/08/22
 */
public abstract class BaseActivity
    extends AppCompatActivity
    implements BaseView {
  private BasePresenter basePresenter;

  // - - Abstract methods  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  public abstract BasePresenter attachPresenter();

  // - - Life cycle methods  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  @CallSuper
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.basePresenter = attachPresenter();
  }

  @CallSuper
  @Override
  protected void onDestroy() {
    if (basePresenter != null) {
      // unscribe registered Subscriptions
      basePresenter.destroy();
    }
    super.onDestroy();
  }

  // - - Implements methods - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  @Override
  public void onError(String tag, String message) {
    Log.e(tag != null ? tag : "BaseActivity", message != null ? message : "Message is null.");
  }
}
