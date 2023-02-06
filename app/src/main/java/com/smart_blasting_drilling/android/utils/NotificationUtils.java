package com.smart_blasting_drilling.android.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.ui.activity.AuthActivity;
import com.smart_blasting_drilling.android.ui.activity.HomeActivity;
import com.smart_blasting_drilling.android.app.BaseApplication;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class NotificationUtils {

    private static final String TAG = "NotificationUtils";
    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String ACTION = "type";
    public static final String NOTIFICATION = "notification";
    public static final String ALARM_TYPE = "alarm_type";
    public static final String NOTIFICATION_ID = "NotificationId";
    public static final String NOTIFICATION_MODEL = "NotificationModel";

    public static String APP_UPDATES_CHANNEL = "FRF";

    public static final int LOCATION_DISABLE_NOTIFICATION_ID = 1;

    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title,
                                        String message,
                                        Intent intent,
                                        int notificationID,
                                        int icon,
                                        String channelId) {
        showNotificationMessage(title, message, intent, null, notificationID, icon, channelId);
    }

    public void showNotificationMessage(final String title,
                                        final String message,
                                        Intent intent,
                                        String imageUrl,
                                        int notificationID,
                                        int icon,
                                        String channelId) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext, channelId);
        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (!TextUtils.isEmpty(imageUrl)) {
            Logger.DebugLog(TAG, "Image URL Not empty");

            Bitmap bitmap = getBitmapFromURL(imageUrl);

            if (bitmap != null) {
                Logger.DebugLog(TAG, "Bitmap is not null");

                showBigNotification(bitmap, mBuilder, icon, title, message, resultPendingIntent, alarmSound, notificationID);
            } else {
                Logger.DebugLog(TAG, "Bitmap is null");

                showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound, notificationID);
            }

        } else {
            showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound, notificationID);
        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder,
                                       int icon,
                                       String title,
                                       String message,
                                       PendingIntent resultPendingIntent,
                                       Uri alarmSound,
                                       int notificationID) {

        //Multiline notification style
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(message);

        Notification notification = mBuilder
                .setSmallIcon(icon)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(message)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setColor(mContext.getResources().getColor(R.color.black))
                .setWhen(System.currentTimeMillis())
                .setStyle(bigTextStyle)
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);
    }


    private void showBigNotification(Bitmap bitmap,
                                     NotificationCompat.Builder mBuilder,
                                     int icon,
                                     String title,
                                     String message,
                                     PendingIntent resultPendingIntent,
                                     Uri alarmSound,
                                     int notificationID) {

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder
                .setTicker(title)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setColor(mContext.getResources().getColor(R.color.black))
                .setStyle(bigPictureStyle)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            Logger.ErrorLog(TAG, "Bitmap string" + strURL);

            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           // connection.setSSLSocketFactory(SslUtils.INSTANCE.getSslContextForCertificateFile(mContext).getSocketFactory());
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method checks if the com.firebasenotification.app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotification(Context context, int notificationID) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationID);
    }

    // Clears notification tray messages
    public static void clearSingleNotification(int notificationId) {
        NotificationManager notificationManager =
                (NotificationManager) BaseApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null)
            notificationManager.cancel(notificationId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Notification createForegroundNotification(Context context, Intent intent, String title) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, APP_UPDATES_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentTitle(title)
                .setColor(context.getResources().getColor(R.color.black))
                .setCategory(Notification.CATEGORY_SERVICE);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
        return builder.build();
    }

    private Notification createNotification(Context context) {
        Intent contentIntent = new Intent(context, HomeActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0);

        Intent fullScreenIntent = new Intent(context, AuthActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, 0);

        return new NotificationCompat.Builder(context, "FRF")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(context.getString(R.string.app_name))
                .setAutoCancel(true)
                .setContentIntent(contentPendingIntent)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();
    }

    private void createNotificationChannel(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Alarm", "FRF APP", NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
