package kr.jsg1504.demo0824.utils;

import android.support.annotation.NonNull;

import kr.jsg1504.demo0824.utils.mvp.BasePresenter;
import rx.Observable;
import rx.Subscriber;

/**
 * @author KangSung-Woo
 * @since 2016/08/02
 */
public class SwObservable {
  private BasePresenter presenter;
  private Observable    observable;

  public SwObservable(@NonNull BasePresenter p, @NonNull Observable o) {
    this.presenter = p;
    this.observable = o;
  }

  @SuppressWarnings("unchecked")
  public final void subscribe(@NonNull Subscriber subscriber) {
    if (presenter == null) {
      throw new NullPointerException("BasePresenter object is null.");
    }
    presenter.addSubscriber(subscriber);

    if (observable == null) {
      throw new NullPointerException("Observable object is null.");
    }
    observable.subscribe(subscriber);
  }

}
