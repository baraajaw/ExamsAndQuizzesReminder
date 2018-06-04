package com.aliosm.examsandquizzesreminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

public class NotifyExamsAndQuizzes extends IntentService {
    public NotifyExamsAndQuizzes() {
        super("NotifyExamsAndQuizzes");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");

        DBConnections db = new DBConnections(this);
        ArrayList examQuiz = db.getExamQuiz(intent.getStringExtra("id"));
        String type = "Exam";
        if(examQuiz.get(0).equals("1"))
            type = "Quiz";

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), Integer.valueOf(intent.getStringExtra("id")), i, 0);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("You have " + examQuiz.get(1) + " " + type + "!");

        String text = "After 3 days!!!\nAt: " + examQuiz.get(2);
        if(examQuiz.get(0).equals("1"))
            text = "After 1 day!!!\nAt: " + examQuiz.get(2);

        builder.setContentText(text);
        builder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(Integer.valueOf(intent.getStringExtra("id")), builder.build());
    }
}
