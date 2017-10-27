package com.function.ianchang.devtools;

/**
 * Created by ianchang on 2017/10/27.
 */

public interface IConstant {

    String LOG_BACKUP_NAME = "upload_log_backup"; // 日志备份
    long INTERVAL_TIME = 1000 * 60 * 60 * 24 * 15; // 最大备份时间15天
    long UPLOAD_FILE_SIZE = 1024 * 1024 * 2; //  上传文件大小
    String APP_TAG = "APP_TAG_";

}
