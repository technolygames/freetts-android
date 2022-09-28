package com.technolygames.freetts_app3;
//android
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

public class NotificationUtils extends android.content.ContextWrapper{
    private NotificationManager nm;

    public static final String ANDROID_CHANNEL_ID="com.technolygames.freetts_app3";
    public static final String ANDROID_CHANNEL_NAME="TTS";

    public NotificationUtils(Context base){
        super(base);
        createChannels();
    }

    public void createChannels(){
        NotificationChannel nch=new NotificationChannel(ANDROID_CHANNEL_ID,ANDROID_CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        nch.enableLights(true);
        nch.enableVibration(true);
        nch.setLightColor(Color.GREEN);
        nch.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(nch);
    }

    private NotificationManager getManager(){
        if(nm==null){
            nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return nm;
    }
}
