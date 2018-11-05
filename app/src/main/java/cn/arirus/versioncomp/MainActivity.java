package cn.arirus.versioncomp;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import io.reactivex.Observable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static android.os.Environment.DIRECTORY_MUSIC;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

  public static final String ARIRUS = "MainActivity";

  private NotificationManager mManager;

  @SuppressLint("CheckResult")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Log.i(ARIRUS, "onCreate: "+getFilesDir().getAbsolutePath());
    Log.i(ARIRUS, "onCreate: "+getCacheDir().getAbsolutePath());




    //Intent intent = new Intent(MainActivity.this,BackgroundService.class);
    //
    //NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
    //notificationManager.notify();
    //startService(intent);
  }
}
