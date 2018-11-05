package cn.arirus.versioncomp.bg;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static cn.arirus.versioncomp.MainActivity.ARIRUS;

public class BackgroundService extends Service {
  public BackgroundService() {
  }

  OkHttpClient mOkHttpClient;
  Request mRequest;

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(ARIRUS, "SSSS onCreate: ");
    mOkHttpClient = new OkHttpClient.Builder().build();
    mRequest = new Request.Builder().url("http://httpbin.org/get").get().build();

    //player = MediaPlayer.create(this, R.raw.music);
    //player.setLooping(true); // Set looping
    //player.setVolume(100,100);
  }

  MediaPlayer player;

  class ServiceBinder extends Binder {
    final BackgroundService mService;

    ServiceBinder(BackgroundService backgroundService) {
      mService = backgroundService;
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    return new ServiceBinder(this);
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

  Observable<Long> startTimer() {
    return Observable.intervalRange(1, 90, 0, 1, TimeUnit.SECONDS);
  }

  @SuppressLint("CheckResult")
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.i(ARIRUS, "onStartCommand: ");
    Observable.intervalRange(1, 90, 0, 1, TimeUnit.SECONDS).subscribe(aLong -> {
      Log.i(ARIRUS, "onCreate: "+ " " + Thread.currentThread().getName()+" " + aLong + " " + isMyServiceRunning(BackgroundService.class));
    });

    //new Thread(() -> {
    //  while (true) {
    //    try {
    //      Thread.sleep(10000);
    //      ResponseBody responseBody = mOkHttpClient.newCall(mRequest).execute().body();
    //      Log.i(ARIRUS, "onStartCommand: " + responseBody.contentLength());
    //    } catch (IOException | InterruptedException e) {
    //      e.printStackTrace();
    //    }
    //  }
    //}).start();

    //NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
    //builder.setSmallIcon(R.mipmap.ic_launcher)
    //    .setContentText("DDDDDDDD")
    //    .setContentTitle("FFFFFFF");
    //startForeground(10029,builder.build());
    return Service.START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i(ARIRUS, "onDestroy: " + getClass().getName());
    //player.stop();
    //player.release();
  }
}
