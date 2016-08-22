package kr.jsg1504.demo0824;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.jsg1504.demo0824.model.ResultCode;
import kr.jsg1504.demo0824.model.ServerState;
import kr.jsg1504.demo0824.utils.common.BaseActivity;
import kr.jsg1504.demo0824.utils.mvp.BasePresenter;

public class MainActivity
    extends BaseActivity
    implements MainActiviyPresenter.View {
  private static final String TAG       = MainActivity.class.getSimpleName();
  private static final String SERVER_ID = "1";

  private MainActiviyPresenter presenter;
  private ServerState          serverState;

  @BindView(R.id.main_btn_signal)
  Button      btnSignals;
  @BindView(R.id.main_pb)
  ProgressBar progressBar;
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

    serverState = ServerState.DISABLED;

    btnSignals.setEnabled(false);
    btnSignals.setText(getString(R.string.retrieve_server_state));
    presenter.retrieveServerState(SERVER_ID);

  }

  @OnClick(R.id.main_btn_signal)
  public void onClick(View v) {
    v.setEnabled(false);
    progressBar.setVisibility(View.VISIBLE);
    if (v instanceof Button) {
      Button tv = (Button) v;
      if (serverState == ServerState.RUNNING) {
        // send stop signal to server.
        presenter.sendStopSignalToServer(SERVER_ID);
        tv.setText(getString(R.string.send_signal));
      }
      else if (serverState == ServerState.STOP) {
        // send start signal to server.
        presenter.sendStartSignalToServer(SERVER_ID);
        tv.setText(getString(R.string.send_signal));
      }
      else {
        // re try to get ServerState.
        presenter.retrieveServerState(SERVER_ID);
        tv.setText(getString(R.string.retrieve_server_state));
      }
    }
  }

  public void showToast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onResultRetrieveServerState(@NonNull ResultCode resultCode) {
    if (resultCode == ResultCode.RUNNING) {
      btnSignals.setText(getString(R.string.running_to_stop));
      this.serverState = ServerState.RUNNING;
    }
    else if (resultCode == ResultCode.STOP) {
      btnSignals.setText(getString(R.string.stop_to_start));
      this.serverState = ServerState.STOP;
    }
    else {
      btnSignals.setText(getString(R.string.get_server_state));
      this.serverState = ServerState.DISABLED;
      showToast("failed to get server state.\nResult code is `" + resultCode.name() + "`");
    }
    btnSignals.setEnabled(true);
    progressBar.setVisibility(View.INVISIBLE);
    setText_ServerState(serverState);
    Log.d(TAG, "// onResultRetrieveServerState() // result Code = " + resultCode.name());
  }

  @Override
  public void sendStartSignalCompleted(@NonNull ResultCode resultCode) {
    if (resultCode == ResultCode.SUCCESS) {
      this.serverState = ServerState.RUNNING;
      btnSignals.setText(getString(R.string.running_to_stop));
    }
    else {
      showToast("failed to send Start signal.\nResult code is `" + resultCode.name() + "`");
    }
    btnSignals.setEnabled(true);
    progressBar.setVisibility(View.INVISIBLE);
    setText_ServerState(serverState);
  }

  @Override
  public void sendStopSignalCompleted(@NonNull ResultCode resultCode) {
    if (resultCode == ResultCode.SUCCESS) {
      this.serverState = ServerState.STOP;
      btnSignals.setText(getString(R.string.stop_to_start));
    }
    else {
      showToast("failed to send Stop signal.\nResult code is `" + resultCode.name() + "`");
    }
    btnSignals.setEnabled(true);
    progressBar.setVisibility(View.INVISIBLE);
    setText_ServerState(serverState);
  }

  @Override
  public void onError(String tag, String message) {
    super.onError(tag, message);

    showToast("an error occurred while network jobs. please re try.");
    btnSignals.setEnabled(true);
    progressBar.setVisibility(View.INVISIBLE);
    setText_ServerState(null);    // error occur
  }

  private void setText_ServerState(ServerState state) {
    final String oldText = tvState.getText().toString();
    tvState.setText(String.valueOf(("Server State : " + (state.name() != null ? state.name() : "ERROR") + "\n") + oldText));
  }

}
