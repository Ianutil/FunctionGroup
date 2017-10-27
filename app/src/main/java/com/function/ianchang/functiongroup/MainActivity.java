package com.function.ianchang.functiongroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化项目文件系统信息
        FileManager.initFile(this);

        LogManager logManager = LogManager.create(this.getClass());
        logManager.enableSaveToFile(true);
        logManager.e("Log Message");

        for (int i = 0; i < 1000; i++){
            logManager.e("这是一个测试");
        }

        // 每隔5分钟上，查看一次是否有待上传的日志文件
        UploadFileManager uploadFileManager = new UploadFileManager(this.getApplication());
        uploadFileManager.start(10 * 60 * 1000);
    }
}
