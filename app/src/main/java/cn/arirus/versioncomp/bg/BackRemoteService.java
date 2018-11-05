package cn.arirus.versioncomp.bg;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;

import static cn.arirus.versioncomp.MainActivity.ARIRUS;
import static cn.arirus.versioncomp.bg.ServiceActivity.CLIENT_HANDLER_WHAT;

public class BackRemoteService extends Service {
  public BackRemoteService() {
  }

  public static final int REMOTE_SERVICE_HANDLER_WHAT = 307;

  //class RemoteServiceHandler extends Handler {
  //  @Override
  //  public void handleMessage(Message msg) {
  //    switch (msg.what) {
  //      case REMOTE_SERVICE_HANDLER_WHAT:
  //
  //        Log.i(ARIRUS, "handleMessage: "+msg.replyTo);
  //        Log.i(ARIRUS, "handleMessage: "+msg.what);
  //        Log.i(ARIRUS, "handleMessage: "+msg.arg1);
  //        Log.i(ARIRUS, "handleMessage: "+msg.arg2);
  //        Observable.intervalRange(1, 10, 0, 1, TimeUnit.SECONDS).doOnComplete(() -> {
  //          Messenger clientMessenger = msg.replyTo;
  //
  //          clientMessenger.send(Message.obtain(null,CLIENT_HANDLER_WHAT,0,0));
  //        }).subscribe(aLong -> {
  //          Log.i(ARIRUS, "handleMessage 1: "+msg.replyTo);
  //          Log.i(ARIRUS, "handleMessage 2: "+msg.what);
  //          Log.i(ARIRUS, "handleMessage 3: "+msg.arg1);
  //          Log.i(ARIRUS, "handleMessage 4: "+msg.arg2);
  //          Log.i(ARIRUS, "onCreate: "
  //              + " "
  //              + Thread.currentThread().getName()
  //              + " "
  //              + aLong
  //              + " "
  //              + isMyServiceRunning(BackRemoteService.class));
  //        }); break;
  //      default:
  //        super.handleMessage(msg);
  //    }
  //  }
  //}

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    //return new Messenger(new RemoteServiceHandler()).getBinder();
    return mBinder;
  }

  private boolean isMyServiceRunning(Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
        Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

  private IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
    @Override
    public int getPid() throws RemoteException {
      return getPid();
    }

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble,
        String aString) throws RemoteException {
      Log.i(ARIRUS, "basicTypes: "+anInt+" "+aLong+" "+aBoolean+" "+aFloat+" "+aDouble+" "+aString);
    }
  };

}
