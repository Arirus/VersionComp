package cn.arirus.versioncomp.noti

import android.annotation.TargetApi
import android.app.Notification.DEFAULT_ALL
import android.app.Notification.DecoratedMediaCustomViewStyle
import android.app.Notification.PRIORITY_HIGH
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.DecoratedCustomViewStyle
import android.support.v4.app.NotificationCompat.MessagingStyle.Message
import android.support.v4.app.NotificationCompat.PRIORITY_MAX
import android.support.v4.app.RemoteInput
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.widget.RemoteViews
import cn.arirus.versioncomp.MainActivity
import cn.arirus.versioncomp.R
import kotlinx.android.synthetic.main.activity_main2.btn_noti


class Main2Activity : AppCompatActivity() {

  @TargetApi(26)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main2)

    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    btn_noti.setOnClickListener { v ->

      val CHANNEL_ID = "DDDF"// The id of the channel.

      val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      }
      val paIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

      val resultIntent = Intent(this, MainActivity::class.java)
      val stackBuilder = TaskStackBuilder.create(this)
// 添加返回栈
      stackBuilder.addParentStack(MainActivity::class.java)
// 添加Intent到栈顶
      stackBuilder.addNextIntent(resultIntent)
// 创建包含返回栈的pendingIntent
      val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)


      val KEY_TEXT_REPLY = "key_text_reply"
      var replyLabel: String = "reply_label"
      var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
        setLabel(replyLabel)
        build()
      }

      var replyPendingIntent: PendingIntent =
          PendingIntent.getBroadcast(applicationContext,
              0,
              intent,
              PendingIntent.FLAG_UPDATE_CURRENT)

      var action: NotificationCompat.Action =
          NotificationCompat.Action.Builder(R.drawable.notification_icon_background,
              ("label"), replyPendingIntent)
              .addRemoteInput(remoteInput)
              .build()

      val PROGRESS_MAX = 100
      val PROGRESS_CURRENT = 0

      val bmp = BitmapFactory.decodeResource(resources,R.mipmap.ic_launchser)

      val noti = NotificationCompat.Builder(this, CHANNEL_ID)
          .setSmallIcon(R.mipmap.ic_launcher)
          .setContentText("this is content")
          .setContentTitle("this is title")
          .addAction(R.drawable.notification_icon_background, ("snooze"),
              paIntent).addAction(action)
          .setCategory(NotificationCompat.CATEGORY_REMINDER)
          .setVisibility(VISIBILITY_PUBLIC)
          .setContentIntent(resultPendingIntent)
          .setPriority(PRIORITY_HIGH)
          .setDefaults(DEFAULT_ALL)
//          .setFullScreenIntent(resultPendingIntent,true)
          .setAutoCancel(true)
          .setLargeIcon(bmp)
//          .setStyle(DecoratedCustomViewStyle())
//          .setCustomContentView(RemoteViews(packageName,R.layout.activity_main))
//          .setCustomBigContentView(RemoteViews(packageName,R.layout.activity_main2))
//          .setStyle(NotificationCompat.MessagingStyle("老王").addMessage(Message("dsdsds",System.currentTimeMillis(),"老张")).addMessage(Message("dsdsds2",System.currentTimeMillis(),"老张2")).addMessage(Message("dsdsds1",System.currentTimeMillis(),"老张1")))
//          .setStyle(NotificationCompat.BigTextStyle().setBigContentTitle("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD").bigText("DDdsdsdsdsdsds多大点事颠三倒四颠三倒四颠三倒四颠三倒四颠三倒四颠三倒四颠三倒四颠三倒四颠三倒四第三点"))
          .setStyle(NotificationCompat.BigPictureStyle()
              .setBigContentTitle("this is big title")
              .bigPicture(bmp)
              .bigLargeIcon(null))
//          .setStyle(NotificationCompat.InboxStyle()
//              .addLine("messageSnippet2")
//              .addLine("messageSnippet1")
//              .addLine("messageSnippet1")
//              .addLine("messageSnippet1")
//              .addLine("messageSnippet1"))
//          .setStyle(NotificationCompat.MessagingStyle
//              .setShowActionsInCompactView(1 /* #1: pause button \*/)
//              .setMediaSession(mMediaSession.getSessionToken()))
          .setAutoCancel(true);


      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {

        val name = CHANNEL_ID
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {

        }

        manager.createNotificationChannel(channel)
      }
      manager.notify(10029, noti.build())

//      Observable.timer(10,SECONDS).subscribe { manager.notify(10029, noti.build()) }


//      Observable.intervalRange(1, 100, 0, 100, MILLISECONDS).doOnComplete {
//        manager.cancel(10029)
//      }.subscribe { it ->
//        noti.setProgress(PROGRESS_MAX, it.toInt(), false)
//        manager.notify(10029, noti.build())
//      }

//      val notifyID = 10029
//      val name = getString(R.string.app_name)// The user-visible name of the channel.
//      val importance = NotificationManager.IMPORTANCE_HIGH
//      val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
//// Create a notification and set the notification channel.
//      val notification = NotificationCompat.Builder(this)
//          .setContentTitle("New Message")
//          .setContentText("You've received new messages.")
//          .setSmallIcon(R.mipmap.ic_launcher)
//          .setChannelId(CHANNEL_ID)
//          .build()
//
////      val mNotificationManager = getSystemService(
////          Context.NOTIFICATION_SERVICE) as NotificationManager
////      mNotificationManager.createNotificationChannel(mChannel)
//
//// Issue the notification.
//      manager.notify(notifyID, notification)


    }
  }

  private fun createNotificationChannel(manager: NotificationManager) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val name = getString(R.string.app_name)
      val descriptionText = getString(R.string.app_name)
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel = NotificationChannel(packageName, name, importance).apply {
        description = descriptionText
      }

      manager.createNotificationChannel(channel)
    }
  }

}
