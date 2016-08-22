package kr.jsg1504.demo0824;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.jsg1504.demo0824.model.ResultCode;
import kr.jsg1504.demo0824.model.ServerState;
import kr.jsg1504.demo0824.utils.Utils;
import kr.jsg1504.demo0824.utils.common.BaseActivity;
import kr.jsg1504.demo0824.utils.mvp.BasePresenter;

public class MainActivity
    extends BaseActivity
    implements MainActiviyPresenter.View {
  private static final String TAG   = MainActivity.class.getSimpleName();
  private static final String SID_1 = "1";
  private static final String SID_2 = "2";

  private MainActiviyPresenter presenter;

  private ServerState s1State;
  private ServerState s2State;

  @BindView(R.id.main_btn_s1_signal)
  Button      btnS1Signal;
  @BindView(R.id.main_btn_s2_signal)
  Button      btnS2Signal;
  @BindView(R.id.main_pb_s1)
  ProgressBar pbS1;
  @BindView(R.id.main_pb_s2)
  ProgressBar pbS2;
  @BindView(R.id.main_tv_state)
  TextView    tvState;

  @Override
  public BasePresenter attachPresenter() {
    this.presenter = new MainActiviyPresenter(this);
    return presenter;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    s1State = ServerState.DISABLED;
    s2State = ServerState.DISABLED;

    presenter.retrieveServerState(SID_1);
    presenter.retrieveServerState(SID_2);

  }

  @OnClick({R.id.main_btn_s1_signal, R.id.main_btn_s2_signal})
  public void onClick(View v) {
    if (v.getId() == R.id.main_btn_s1_signal) {
      if (s1State == ServerState.RUNNING) {
        presenter.sendStopSignalToServer(SID_1);
      }
      else if (s1State == ServerState.STOP) {
        presenter.sendStartSignalToServer(SID_1);
      }
      else {
        presenter.retrieveServerState(SID_1);
      }
      setButtonsLayouts(SID_1, s1State, false, ResultCode.WAIT);
    }
    else if (v.getId() == R.id.main_btn_s2_signal) {
      if (s2State == ServerState.RUNNING) {
        presenter.sendStopSignalToServer(SID_2);
      }
      else if (s2State == ServerState.STOP) {
        presenter.sendStartSignalToServer(SID_2);
      }
      else {
        presenter.retrieveServerState(SID_2);
      }
      setButtonsLayouts(SID_2, s2State, false, ResultCode.WAIT);
    }
  }

  private void setButtonsLayouts(@NonNull String sId,
                                 @NonNull ServerState state,
                                 boolean isResponse,
                                 @NonNull ResultCode resultCode) {
    String btnTitle = "";

    ProgressBar pb = (SID_1.equals(sId) ? pbS1 : pbS2);
    Button btn = (SID_1.equals(sId) ? btnS1Signal : btnS2Signal);

    pb.setVisibility(!isResponse ? View.VISIBLE : View.GONE);
    if (resultCode == ResultCode.SUCCESS) {
      if (state == ServerState.RUNNING) {
        btnTitle = getString(R.string.stop_to_start);
        state = ServerState.STOP;
      }
      else {
        btnTitle = getString(R.string.running_to_stop);
        state = ServerState.RUNNING;
      }
    }
    else if (resultCode == ResultCode.RUNNING) {
      btnTitle = getString(R.string.running_to_stop);
      state = ServerState.RUNNING;
    }
    else if (resultCode == ResultCode.STOP) {
      btnTitle = getString(R.string.stop_to_start);
      state = ServerState.STOP;
    }
    if (resultCode == ResultCode.FAIL) {
      Utils.showToast(this, "failed to get server [" + sId + "] state.\nResult code is `" + resultCode.name() + "`");
      btnTitle = getString(R.string.get_server_state);
      state = ServerState.DISABLED;
    }
    if (resultCode == ResultCode.WAIT) {
      // wait for response.
      btnTitle = getString(R.string.send_signal);
    }

    if (SID_1.equals(sId)) {
      s1State = state;
    }
    else if (SID_2.equals(sId)) {
      s2State = state;
    }
    btn.setText(btnTitle);
    btn.setEnabled(isResponse);
    setText_ServerState(sId, state);
  }

  @Override
  public void onResultRetrieveServerState(@NonNull String sId, @NonNull ResultCode resultCode) {
    setButtonsLayouts(sId, (SID_1.equals(sId) ? s1State : s2State), true, resultCode);
  }

  @Override
  public void sendStartSignalCompleted(@NonNull String sId, @NonNull ResultCode resultCode) {
    setButtonsLayouts(sId, (SID_1.equals(sId) ? s1State : s2State), true, resultCode);
  }

  @Override
  public void sendStopSignalCompleted(@NonNull String sId, @NonNull ResultCode resultCode) {
    setButtonsLayouts(sId, (SID_1.equals(sId) ? s1State : s2State), true, resultCode);
  }

  @Override
  public void onError(String tag, String message) {
    super.onError(tag, message);

    Utils.showToast(this, "an error occurred while network jobs. please re try.");

    if (SID_1.equals(tag) || SID_2.equals(tag)) {
      setButtonsLayouts(tag, (SID_1.equals(tag) ? s1State : s2State), true, ResultCode.FAIL);
    }
  }

  private void setText_ServerState(@NonNull String sId, ServerState state) {
    final String oldText = tvState.getText().toString();
    tvState.setText(String.valueOf(("[" + sId + "] State : " + (state.name() != null ? state.name() : "ERROR") + "\n") + oldText));
  }

}
