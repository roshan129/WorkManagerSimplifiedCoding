package com.adivid.workmanagersimplifiedcoding;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public static final String KEY_TASK_OUTPUT = "key_task_output";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String desc = data.getString(MainActivity.KEY_TASK_DEC);
        displayNotification("This is title", desc);

        Data data1 = new Data.Builder()
                .putString(KEY_TASK_OUTPUT, "Task Finished Successfully")
                .build();

        return Result.success(data1);
    }

    private void displayNotification(String task, String desc){

        NotificationManager manager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        manager.notify(1, builder.build());

    }
}
