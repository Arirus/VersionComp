package cn.arirus.versioncomp.noti;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import cn.arirus.versioncomp.MainActivity;
import cn.arirus.versioncomp.R;
import java.util.Objects;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;
import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;

public class NotiActivity extends AppCompatActivity implements View.OnClickListener {

  Button mButton;
  NotificationCompat.Builder mBuilder;
  NotificationManagerCompat mManagerCompat;

  private String CHANNEL_ID = getClass().getSimpleName();
  private int NOTI_ID = 0x582;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_noti);
    mButton = findViewById(R.id.button);

    mButton.setOnClickListener(this);

    mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
    mManagerCompat = NotificationManagerCompat.from(this);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel =
          new NotificationChannel(CHANNEL_ID, getResources().getString(R.string.app_name),
              NotificationManager.IMPORTANCE_HIGH);
      channel.setDescription("This is NotificationChannel Description");
      Objects.requireNonNull(getSystemService(NotificationManager.class))
          .createNotificationChannel(channel);
    }
  }

  @Override
  public void onClick(View v) {

    Intent intent = new Intent(this, MainActivity.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    stackBuilder.addNextIntentWithParentStack(intent);
    PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, FLAG_UPDATE_CURRENT);

    mBuilder.setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("This is Title")
        .setContentText("This is Content")
        .setDefaults(DEFAULT_ALL)
        .setPriority(PRIORITY_HIGH)
        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("This is Big Title")
            .bigText("This is Big Content"))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true);

    mManagerCompat.notify(NOTI_ID, mBuilder.build());
  }
}
