package com.adivid.workmanagersimplifiedcoding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_TASK_DEC = "key_task_desc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sharedPreferences = this.getSharedPreferences("my_shared_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int i = sharedPreferences.getInt("value", 999);
        if (i == 999) {
            editor.putInt("value", 1);
            editor.commit();
        }

        Log.d("tag", "value main act: "+ i);

        final Data data = new Data.Builder()
                .putString(KEY_TASK_DEC, "Hey I am sending the work data")
                .build();

        Constraints constraints = new Constraints.Builder()
                /*.setRequiresCharging(true)*/
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance(MainActivity.this).enqueue(request);

            }
        });

        final TextView textView = findViewById(R.id.textView);

        WorkManager.getInstance(MainActivity.this).getWorkInfoByIdLiveData(request.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {

                        if (workInfo != null) {

                            if (workInfo.getState().isFinished()) {
                                Data data1 = workInfo.getOutputData();
                                String output = data1.getString(MyWorker.KEY_TASK_OUTPUT);
                                textView.append(output + "\n");
                            }
                        }

                        String status = workInfo.getState().name();
                        textView.append(status + "\n");

                    }
                });

    }
}