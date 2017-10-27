package com.function.ianchang.functiongroup;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by martin.du on 2017/10/23.
 */

public class LogManager implements IConstant{

    private static char[] CHAR_LEVEL = new char[]{'V', 'D', 'I', 'W', 'E', 'A'};
    private static Executor mExecutor;
    private static File mGlobalFile = new File(FileManager.PATH_LOG_ROOT, "log.txt");
    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ", Locale.getDefault());
    private final String mTag;

    private boolean mToFile;
    private File mLogFile;

    private LogManager(String tag) {
        this.mTag = tag;
        this.mToFile = false;
    }

    /*****
     * 创建一个LogManager对象
     * @param object
     * @return
     */
    public static LogManager create(Object object) {

        String tag = object.getClass().getSimpleName();

        if (APP_TAG.length() + tag.length() > 21) {
            tag = tag.substring(0, 21 - APP_TAG.length());
        }

        return new LogManager(APP_TAG + tag);
    }

    public void enableSaveToFile(boolean isSave) {
        this.mToFile = isSave;
    }

    public void setFile(File saveToFile) {
        this.mLogFile = saveToFile;
    }

    public final void d(String message) {
        log(Log.DEBUG, message);
    }

    public final void d(String message, Throwable tr) {
        log(Log.DEBUG, Log.getStackTraceString(tr));
    }

    public final void e(String message) {
        log(Log.ERROR, message);
    }

    public final void e(String message, Throwable tr) {
        log(Log.DEBUG, Log.getStackTraceString(tr));
    }

    public final void i(String message) {
        log(Log.INFO, message);
    }

    public final void i(String message, Throwable tr) {
        log(Log.INFO, Log.getStackTraceString(tr));
    }

    public final void v(String message) {
        log(Log.VERBOSE, message);
    }

    public final void v(String message, Throwable tr) {
        log(Log.VERBOSE, Log.getStackTraceString(tr));
    }

    public final void w(String message) {
        log(Log.WARN, message);
    }

    public final void w(String message, Throwable tr) {
        log(Log.WARN, Log.getStackTraceString(tr));
    }

    /*******
     * log
     * @param priority
     * @param message
     */
    private void log(int priority, String message) {
        Log.println(priority, mTag, message);
        toFile(priority, message);
    }

    private void toFile(final int priority, final String message) {
        if (mToFile) {
            if (mExecutor == null) {
                mExecutor = Executors.newSingleThreadExecutor();
            }

            if (mLogFile == null) mLogFile = mGlobalFile;
            if (mLogFile == null) return;

            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    if (!mToFile) return;

                    if (!FileManager.createOrExistsFile(mLogFile)) {
                        return;
                    }

                    // 日志文件无法动态设置，只能每个文件大于2M时，再复制出来
                    // log.txt 当成临时文本路径，当满足XXM时，把临时文件放到指定的目录下 PATH_LOG_UPLOAD
                    if (UploadFileManager.isUploadFile(mLogFile, 1024 * 1024 * 2)) {
                        uploadFile(mLogFile);
                    }

                    Date currentDate = new Date(System.currentTimeMillis());
                    String time = mDateFormat.format(currentDate);

                    // 系统业务流程日志：APP_NAME（Screen）##Log_Type##设备ID##时间#服务(关键字TAG类名)#参数#Message<Error\>
                    // Log_Type:系统日志、APP(Screen)日志

                    StringBuilder sb = new StringBuilder();
                    sb.append(time)
                            .append(CHAR_LEVEL[priority - 2])
                            .append("/")
                            .append(mTag)
                            .append(message)
                            .append('\n');

                    BufferedWriter bw = null;
                    try {
                        bw = new BufferedWriter(new FileWriter(mLogFile, true));
                        bw.write(sb.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (bw != null) {
                                bw.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }


    /*********
     * 待上传日志文件复制出来
     * @param file
     * @return
     */
    private void uploadFile(File file){

        // 上传文件目录
        File mUploadFileDir = FileManager.PATH_LOG_UPLOAD; // 上传日志文件目录
        FileManager.createOrExistsDir(mUploadFileDir);

        // 上传文件
        File uploadFile = new File(mUploadFileDir, "/LOG_"+mDateFormat.format(System.currentTimeMillis())+".txt");
        file.renameTo(uploadFile);
        // 删除原日志文件
        file.delete();
        // 再创建日志文件
        FileManager.createOrExistsFile(file);
    }

}
