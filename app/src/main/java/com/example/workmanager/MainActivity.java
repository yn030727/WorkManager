package com.example.workmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //在这个里面添加任务
    public void mAddWork(View view) {
        //(1)设置限制条件
        Constraints constraints =new Constraints.Builder()
                //设置成无要求
                //改为要求网络，不会立马执行，交给系统决定
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                //在电量足够时执行
                //.setRequiresBatteryNotLow(true)
                //在充电时执行
                //.setRequiresCharging(true)
                //在存储容量足够时执行
                //.setRequiresStorageNotLow(true)
                //在待机状态下执行，调用需要API级别最低为23
                //.setRequiresDeviceIdle(true)
                .build();
        //定义参数
        Data inputdata = new Data.Builder()
                .putString("input_data","jack")
                .build();

        //(2)配置任务
        //一次性执行的任务
        OneTimeWorkRequest workRequest1= new OneTimeWorkRequest.Builder(MyWork.class)
                //任务可以设置触发条件
                .setConstraints(constraints)//约束
                //任务可以设置延迟执行
                .setInitialDelay(5, TimeUnit.SECONDS)//延迟5秒钟
                //任务可以设置指数退避策略(需要API等级)
                //请求失败后，隔几秒再次请求
                //.setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofSeconds(2))
                //设置tag标签
                .addTag("workRequest1")
                //参数传递
                .setInputData(inputdata)
                .build();
        //周期性任务
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            PeriodicWorkRequest workRequest2=new PeriodicWorkRequest.Builder(MyWork.class,Duration.ofMinutes(15))
                    .build();
        }


        //(3)创建WorkManager
        WorkManager workManager=WorkManager.getInstance(this);
        //任务提交给WorkManager
        //WorkManager是一个队列，所以说任务不是马上去执行，而是看前面排了多少人
        workManager.enqueue(workRequest1);

        //观察任务状态
        workManager.getWorkInfoByIdLiveData(workRequest1.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                Log.d("ning",workInfo.toString());
                if(workInfo!=null&&workInfo.getState()==WorkInfo.State.SUCCEEDED){
                    String OutputData = workInfo.getOutputData().getString("output_data");
                    Log.d("ning","outputDta"+OutputData);
                }
            }
        });
//        //取消任务
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                workManager.cancelWorkById(workRequest1.getId());
//            }
//        },2000);

    }
}