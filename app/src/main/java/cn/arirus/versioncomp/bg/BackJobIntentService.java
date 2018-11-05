package cn.arirus.versioncomp.bg;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static cn.arirus.versioncomp.MainActivity.ARIRUS;

public class BackJobIntentService extends JobIntentService {

  static final int JOB_ID = 1000;

  static void enqueueWork(Context context, Intent work) {
    enqueueWork(context, BackJobIntentService.class, JOB_ID, work);
  }

  @SuppressLint("CheckResult")
  @Override
  protected void onHandleWork(@NonNull Intent intent) {
    Log.i(ARIRUS, "onHandleWork: " + 999);
    Observable.create((ObservableOnSubscribe<Long>) emitter -> {
      int index = 1;
      while (index < 90) {
        emitter.onNext(Long.valueOf(index));
        Thread.sleep(1000);
        index++;
      }
    }).subscribe(aLong -> {
      Log.i(ARIRUS,
          "onCreate: " + aLong + " " + Thread.currentThread().getName() + " " + isMyServiceRunning(
              BackJobIntentService.class));
    });
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
