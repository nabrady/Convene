package google.com.convenebackend.GoogleAPIs;

/**
 * Created by Peru on 02-01-2016.
 */
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import google.com.convenebackend.MainApp.AppMainActivity;
import google.com.convenebackend.MainApp.NotificationItem;
import google.com.convenebackend.MainApp.UserUtils;
import google.com.convenebackend.fragments.NotifyResultActivity;
import google.com.convenebackend.R;
import google.com.convenebackend.fragments.ThreeFragment;
import google.com.convenebackend.fragments.TwoFragment;


public class GcmIntentService extends IntentService {

    private UserUtils utils;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    private final static String message1 = "Convene Request";
    private final static String message2 = "Request accepted";
    private final static String message3 = "Sorry Request rejected";
    private String senderId = null;

    private Map<String, String> fMap = utils.getFreindMap();

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

                senderId = extras.getString("senderId");
                showNotification(extras.getString("message"));
            }
        }

        System.out.println("senderId received is " + senderId);

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    protected void showNotification(final String message) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        //this default vibrate setting has to be set for a heads up notification to appear
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(R.drawable.cast_ic_notification_1)
                        .setContentTitle("Convene notification")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

// Creates an Intent for the Activity
        Intent myIntent;
        switch (message) {
            case message1:
                myIntent = new Intent(this, AppMainActivity.class);
                myIntent.putExtra("UNIQUE_ID", "CONVENE_REQUEST");
                myIntent.putExtra("FRIEND_NAME", getNameFromKey(senderId));
                //TODO add friends photo from some new list or map
                break;
            case message2:
                myIntent =
                        new Intent(this, TwoFragment.class);
                break;
            case message3:
                myIntent =
                        new Intent(this, TwoFragment.class);
                break;
            default:
                myIntent =
                        new Intent(Intent.makeMainActivity(new ComponentName(this, NotifyResultActivity.class)));

        }
/* Sets the Activity to start in a new, empty task */
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);

        myIntent.putExtra("senderId", senderId);
// Creates the PendingIntent
        PendingIntent notifyIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        myIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

// Puts the PendingIntent into the notification builder
        builder.setContentIntent(notifyIntent);
// Notifications are issued by sending them to the
// NotificationManager system service.
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// Builds an anonymous NotificationItem object from the builder, and
// passes it to the NotificationManager
        // Sets an ID for the notification
        int mNotificationId = 001;
        mNotificationManager.notify(mNotificationId, builder.build());
    }


    public String getNameFromKey(String value) {
        for (Map.Entry<String, String> entry : fMap.entrySet()) {
            String name = entry.getKey();
            String id = entry.getValue();
            if (id.equals(value)) {
                return name;
            }
        }
        return null;
    }
}
