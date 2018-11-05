package cn.arirus.versioncomp.bg;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import cn.arirus.versioncomp.R;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.arirus.versioncomp.MainActivity.ARIRUS;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CustomJobScheduler extends JobService {
  OkHttpClient mOkHttpClient;
  Request mRequest;
  Call mCall;
  public CustomJobScheduler() {
    mOkHttpClient = new OkHttpClient.Builder().build();
    mRequest = new Request.Builder().url("http://httpbin.org/get").get().build();
    mCall = mOkHttpClient.newCall(mRequest);
  }

  //     * Override this method with the callback logic for your job. Any such logic needs to be
  //     * performed on a separate thread, as this function is executed on your application's main
  //     * thread.
  // 重写这个方法，用来实现这个service被调用时执行的具体工作。注意这个方法也是默认工作在主线程，如果后台逻辑较为耗时要
  // 新起线程来进行操作。
  // 返回值很重要，如果返回true相当于告诉系统，我还没处理完，等我处理完告你；返回false就是告诉系统说，我已经处理完了，不需要额外的
  // 操作了。如果你选择返回 true，系统会认为你会一直在执行后台任务，直到调用了 jobFinished()
  @Override
  public boolean onStartJob(JobParameters params) {
    Log.i(ARIRUS, "onStartJob: " + params.getJobId());
    long start = System.currentTimeMillis();
    mCall.clone().enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        Log.i(ARIRUS, "onResponse: "+response.body().string());
        jobFinished(params,false);

        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification= new NotificationCompat.Builder(getApplicationContext())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(String.valueOf(start - System.currentTimeMillis()))
            .setContentText("dddd")
            .build();

        notificationManager.notify(10029,notification);
      }
    });

    return true;
  }


  // 如果job 在finish之前，如果 job 被取消了，则这个方法会被调用到。
  // 当执行某个任务时，他的条件不再被满足的时候，就会调用到这个方法。例如正在下载时，突然断网了，就会调用这个方法。
  // 就可以针对这种做了一半的任务执行清洁工作等等。
  // 如果你还想让系统在重新满足条件时，重新收到通知返回 true，否则就返回 false
  @Override
  public boolean onStopJob(JobParameters params) {
    Log.i(ARIRUS, "onStopJob: ");
    if (mCall.isExecuted()) mCall.cancel();
    return true;
  }
}
