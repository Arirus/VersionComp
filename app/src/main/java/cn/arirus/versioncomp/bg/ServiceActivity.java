package cn.arirus.versioncomp.bg;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import cn.arirus.versioncomp.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static cn.arirus.versioncomp.MainActivity.ARIRUS;

public class ServiceActivity extends AppCompatActivity {

  public static final int CLIENT_HANDLER_WHAT = 649;

  Button mButton;
  TextView mTxt;
  Messenger mServerMessenger;

  static final Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case CLIENT_HANDLER_WHAT:
          Log.i(ARIRUS, "handleMessage: " + "Timer finish");
          break;
        default:
          super.handleMessage(msg);
      }
    }
  };

  CompositeDisposable mCompositeDisposable = new CompositeDisposable();

  BackgroundService.ServiceBinder mBinder;

  static int index = 1;

  @Override
  @TargetApi(26)
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_service);
    mButton = findViewById(R.id.btn_service);
    mTxt = findViewById(R.id.ttt);
    mButton.setOnClickListener(v -> {
      //JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
      //index++;
      //jobScheduler.
      //    schedule(new JobInfo.Builder(index,
      //        new ComponentName(getApplicationContext(),CustomJobScheduler.class))
      //    .setPersisted(true)
      //        .setPeriodic(10000)
      //        //.setMinimumLatency(1000)
      //        //.setOverrideDeadline(5000)
      //    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).build());

      //startActivity(new Intent(ServiceActivity.this,ServiceActivity.class));
      //jobScheduler.enqueue(new JobInfo.Builder(index,
      //        new ComponentName(getApplicationContext(),CustomJobScheduler.class))
      //        .setPeriodic(5000).build()
      //    , new JobWorkItem(null));

      //Intent intent = new Intent(this,BackJobIntentService.class);
      //BackJobIntentService.enqueueWork(this,intent);
      //Intent intent = new Intent(this,BackIntentService.class);
      //Observable.intervalRange(0,70,0,1,TimeUnit.SECONDS)
      //    .doOnComplete(()->startForegroundService(intent))
      //    .subscribe(aLong -> Log.i(ARIRUS, "onCreate: "+aLong));
      //Intent intent = new Intent(this,BackgroundService.class);
      //startService(intent);
      //try {
      //
      //}catch (Exception e){
      //
      //}finally {
      //  return;
      //}
      //Intent intent = new Intent(this, BackgroundService.class);
      //bindService(intent, new ServiceConnection() {
      //  @Override
      //  public void onServiceConnected(ComponentName name, IBinder service) {
      //    Log.i(ARIRUS, "onServiceConnected: " + name);
      //    mBinder = (BackgroundService.ServiceBinder) service;
      //    mCompositeDisposable.add(mBinder.mService.startTimer()
      //        .observeOn(AndroidSchedulers.mainThread())
      //        .subscribe(aLong -> mTxt.setText(String.valueOf(aLong))));
      //  }
      //
      //  @Override
      //  public void onServiceDisconnected(ComponentName name) {
      //    Log.i(ARIRUS, "onServiceDisconnected: " + name);
      //    mCompositeDisposable.clear();
      //  }
      //}, BIND_AUTO_CREATE);

      Intent intent = new Intent(this, BackRemoteService.class);
      bindService(intent, new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
          IMyAidlInterface remoteService = IMyAidlInterface.Stub.asInterface(service);
          try {
            remoteService.basicTypes(1,2,true,4f,5,"hello ipc");
          } catch (RemoteException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
          mServerMessenger = null;
        }
      }, BIND_AUTO_CREATE);
      //throw  new RuntimeException("ds");
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    Intent startServiceIntent = new Intent(this, CustomJobScheduler.class);
    startService(startServiceIntent);
  }

  @Override
  @TargetApi(21)
  protected void onStop() {
    stopService(new Intent(this, CustomJobScheduler.class));
    JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
    tm.cancelAll();
    super.onStop();
  }
}
