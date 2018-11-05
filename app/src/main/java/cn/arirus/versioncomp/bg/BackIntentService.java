package cn.arirus.versioncomp.bg;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import cn.arirus.versioncomp.R;

import static cn.arirus.versioncomp.MainActivity.ARIRUS;

/**
 * @author Arirus
 */
public class BackIntentService extends IntentService {

  public BackIntentService() {
    super("BackIntentService");
  }

  @SuppressLint("CheckResult")
  @Override
  @TargetApi(26)
  protected void onHandleIntent(@Nullable Intent intent) {

    //Observable.create((ObservableOnSubscribe<Long>) emitter -> {
    //  int index = 1;
    //  while (index < 90) {
    //    emitter.onNext(Long.valueOf(index));
    //    Thread.sleep(1000);
    //    index++;
    //  }
    //}).subscribe(aLong -> {
    //  Log.i(ARIRUS,
    //      "onCreate: " + " " + Thread.currentThread().getName() + aLong + " " + isMyServiceRunning(
    //          BackIntentService.class));
    //});

    Log.i(ARIRUS, "onHandleIntent: ");

    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(this, getPackageName())
            .setContentTitle("DDDD")
            .setContentText("CCCCCC")
            .setSmallIcon(R.mipmap.ic_launchers);

    NotificationManager notificationManager = getSystemService(NotificationManager.class);

    notificationManager.createNotificationChannel(
        new NotificationChannel(getPackageName(), getPackageName(),
            NotificationManager.IMPORTANCE_HIGH));

    startForeground(10029, builder.build());
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
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
}
