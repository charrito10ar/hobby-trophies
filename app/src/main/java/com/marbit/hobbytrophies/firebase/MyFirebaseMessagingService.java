package com.marbit.hobbytrophies.firebase;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.marbit.hobbytrophies.MainActivity;
import com.marbit.hobbytrophies.MeetingDetailActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.chat.ChatActivity;
import com.marbit.hobbytrophies.market.ItemDetailActivity;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.Meeting;
import com.marbit.hobbytrophies.utilities.DateUtils;
import com.marbit.hobbytrophies.utilities.Preferences;

import java.text.ParseException;

import java.util.Map;

import static com.marbit.hobbytrophies.chat.ChatActivity.PARAM_CHAT_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String MEETING_MESSAGE_NOTIFICATION = "meeting-message";
    private static final String MEETING_IS_COMING_NOTIFICATION = "meeting-is-coming";

    private static final String MEETING_MESSAGE_CHAT_NOTIFICATION = "message-chat";

    private static final String WISH_MATCH_NOTIFICATION = "wish-match";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            String  meetingDescription = data.get("meeting-description");
            switch (data.get("type")){
                case MEETING_MESSAGE_NOTIFICATION:
                    String message = data.get("message");
                    String author = data.get("author");
                    if (!author.equals(Preferences.getUserId(getApplicationContext()))) {
                        sendNotification(message, author, meetingDescription);
                    }
                    break;
                case MEETING_IS_COMING_NOTIFICATION:
                    try {
                        String dateLocal = DateUtils.getInstance().convertToLocalTimeString(data.get("date"));
                        Meeting meeting = new Meeting();
                        meeting.setId(Integer.parseInt(data.get("meeting-id")));
                        meeting.setDate(DateUtils.getInstance().convertToLocalTimeDate(data.get("date")));
                        meeting.setGame(new Game(data.get("game-id")));
                        meeting.setDescription(meetingDescription);
                        sendReminderMeetingNotification(getString(R.string.notification_meeting_is_coming_txt, dateLocal.substring(11, 16)),
                                getString(R.string.notification_meeting_is_coming_title, meetingDescription), meeting);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case MEETING_MESSAGE_CHAT_NOTIFICATION:
                    String userSender = data.get("user-sender");
                    String text = data.get("text");
                    String chatId = data.get("chatId");
                    String itemId = data.get("itemId");
                    String itemTitle = data.get("itemTitle");
                    sendNotificationMarketChat(userSender, text, chatId, itemId, itemTitle);
                    break;
                case WISH_MATCH_NOTIFICATION:
                    String itemIdMatch = data.get("itemId");
                    String itemTitleMatch = data.get("itemTitle");
                    sendNotificationWishMatch(itemIdMatch, itemTitleMatch);
                    break;
            }
        }
    }

    private void sendReminderMeetingNotification(String messageBody, String title, Meeting meeting) {
        Intent intentMeeting = new Intent(this, MeetingDetailActivity.class);
        intentMeeting.putExtra("MEETING", meeting);
        intentMeeting.putExtra("FROM", "LOCAL");
        intentMeeting.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intentMeeting,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String author, String meetingDescription) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(meetingDescription)
                .setContentText(author + " : " +messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationMarketChat(String userSender, String text, String chatId, String itemId, String itemTitle) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(PARAM_CHAT_ID, chatId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(itemTitle)
                .setContentText(userSender + " : " +text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationWishMatch(String itemId, String itemTitle) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("FROM", "DEEP-LINK");
        intent.putExtra("itemId", itemId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("¡Coincidencia en el mercadillo!")
                .setContentText("Item: " + itemTitle)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}