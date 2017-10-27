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

        UploadFileManager uploadFileManager = new UploadFileManager(this.getApplication());
        uploadFileManager.start(10 * 60 * 1000);
    }
}
