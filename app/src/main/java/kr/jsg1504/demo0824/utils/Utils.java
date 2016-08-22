package kr.jsg1504.demo0824.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * @author KangSung-Woo
 * @since 2016/08/18
 */
public class Utils {

  public static void showToast(@NonNull Context context, @NonNull String msg) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
  }

}
