package com.adivid.workmanagersimplifiedcoding;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    private static final String TAG = "MyWorker";
    public static final String KEY_TASK_OUTPUT = "key_task_output";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        sharedPreferences = context.getSharedPreferences("my_shared_pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    @NonNull
    @Override
    public Result doWork() {
        int i= sharedPreferences.getInt("value", 999);
        i++;
        editor.putInt("value", i);
        editor.commit();
        Log.d(TAG, "value i : " + i);


        Data data = getInputData();
        String desc = data.getString(MainActivity.KEY_TASK_DEC);
        displayNotification("This is title", desc);

        Data data1 = new Data.Builder()
                .putString(KEY_TASK_OUTPUT, "Task Finished Successfully")
                .build();

        return Result.success(data1);
    }

    private void displayNotification(String task, String desc) {

        NotificationManager manager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher);

        manager.notify(1, builder.build());

    }


}
