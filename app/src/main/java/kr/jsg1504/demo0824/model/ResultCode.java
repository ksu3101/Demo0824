package kr.jsg1504.demo0824.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author KangSung-Woo
 * @since 2016/08/22
 */
public enum ResultCode {
  SUCCESS("00"),
  FAIL("99"),
  RUNNING("01"),
  STOP("02");

  private String value;

  ResultCode(String v) {
    this.value = v;
  }

  public String getValue() {
    return value;
  }

  public static ResultCode parseFromValue(@Nullable String value) {
    ResultCode resultCode = FAIL;
    if (value != null) {
      if ("00".equals(value)) {
        resultCode = SUCCESS;
      }
      else if ("99".equals(value)) {
        resultCode = FAIL;
      }
      else if ("01".equals(value)) {
        resultCode = RUNNING;
      }
      else if ("02".equals(value)) {
        resultCode = STOP;
      }
    }
    return resultCode;
  }
}
