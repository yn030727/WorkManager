package com.example.workmanager;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.function.LongFunction;

public class MyWork extends Worker {


    public MyWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        //获取传递进来的参数
        String inputData=getInputData().getString("input_data");
        Log.d("ning","InputData:"+inputData);

        //SystemClock.sleep(2000);
        Log.d("ning","MyWork doWork");

        //任务执行完后，返回数据
        Data outputData=new Data.Builder()
                .putString("output_data","执行成功")
                .build();
        return Result.success(outputData);
    }
}
