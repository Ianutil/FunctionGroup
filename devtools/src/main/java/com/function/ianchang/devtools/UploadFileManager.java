package com.function.ianchang.devtools;

import android.app.Application;

import java.io.File;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by martin.du on 2017/10/23.
 *
 * 上传日志工具
 *
 * 上传日志文件相关参数或变量
 */
public class UploadFileManager implements IConstant{

    private static Executor mExecutor;
    private Timer timer;
    private Application context;

    public UploadFileManager(Application context){
        this.context = context;
        timer = new Timer();
        LogManager.create(this.getClass());
    }

    /******
     * 监听日志上传
     * @param delay
     */
    public void start(long delay){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                // 判断是否上传日志
                isUploadLog();

                // 判断是否清空备份
                // upload_log_backup目录下的所有日志文件，每半个月清除一次,根据上次记录的清除时间
                clearBackupLog();
            }
        }, delay);
    }

    // 判断是否上传日志
    public void isUploadLog(){
        // 判断日志文件目录下，是否有待上传的日志文件
        if (FileManager.PATH_LOG_UPLOAD.exists()){

            File[] uploadFiles = FileManager.PATH_LOG_UPLOAD.listFiles();

            // 上传-上传日志目录下，所有的待上传日志文件
            if (uploadFiles != null && uploadFiles.length > 0){

                log("待上传文件目录:"+ FileManager.PATH_LOG_UPLOAD.getPath());

                // 上传文件目录
                File mUploadFileDir = FileManager.PATH_LOG_UPLOAD; // 上传日志文件目录
                FileManager.createOrExistsDir(mUploadFileDir);

                File saveLogFile;
                for (File file : uploadFiles){

                    // 判断文件是否
                    if (isUploadFile(file, UPLOAD_FILE_SIZE)) {
                        saveLogFile = new File(mUploadFileDir, file.getName());

                        // 上传文件
                        File uploadFile = new File(mUploadFileDir, saveLogFile.getName());
                        saveLogFile.renameTo(uploadFile);

                        // 删除原日志文件
                        file.delete();
                    }

                }

                log("待上传文件列表:"+ Arrays.toString(uploadFiles));
                log("待上传文件个数:"+uploadFiles.length);

                // 判断网络状态，是否连接
//                if (NetworkUtils.currentNetworkType(Utils.getApp()) != -1){
                    log("网络状态正常，开始上传日志文件");

                    uploadFiles(uploadFiles);
//                }
            }
        }
    }

    /******
     * 上传到日志服务器
     * @param files 待上传的日志文件路径
     */
    public void uploadFiles(final File... files) {

        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadExecutor();
        }

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                log("开始排队等待上传");

                // 开始上传
                requestUploadFile(files);
            }
        });

    }

    /*******
     * 请求上传日志文件到服务器上
     * @param files
     */
    private boolean uploadFileToService(File... files){

        //组装partMap对象
//        Map<String, RequestBody> partMap = new HashMap<>();
//        for(File file : files ){
//            if (!file.exists()){
//                continue;
//            }
//
//            RequestBody fileBody = RequestBody.create(MediaType.parse("text/plain"), file);
//            partMap.put("file\"; filename=\""+file.getName()+"\"", fileBody);
//        }
//
//        try {
//            Response<String> response = BlApiManager.getInstance().uploadFiles(partMap).execute();
//
//            return response.isSuccessful();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return false;
    }

    // 测试代码
    public void requestUploadFile(File... files){

        // 系统业务流程日志：APP_NAME（Screen）##Log_Type##设备ID##时间#服务(关键字TAG类名)#参数#Message<Error\>
        // Log_Type:系统日志、APP(Screen)日志

        File uploadFileDir = new File(FileManager.PATH_LOG_ROOT, LOG_BACKUP_NAME);
        FileManager.createOrExistsDir(uploadFileDir);
        log("备份上传日志文件的目录："+uploadFileDir.getPath());

        File tmpUploadFile;
        for (File file: files) {

//            if(uploadFileToService()){
                log("上传成功:"+files.length);

                tmpUploadFile = new File(uploadFileDir, file.getName());
                file.renameTo(tmpUploadFile);

                file.delete();
//            }
        }

        log("日志文件上传完成:"+files.length);
        log("到目前为止总共备份上传文件列表："+uploadFileDir.list().length);
        log("到目前为止总共备份上传文件个数："+Arrays.toString(uploadFileDir.list()));
    }

    private void log(String msg){
        log(msg);
    }

     /*** 是否上传日志文件
     * @param file  文件
     * @param size 上传大小条件
     * @return
      * * */
    public static boolean isUploadFile(File file, long size){
        // 判断是否上传日志文件，文件大于2M
        if (file.length() >= size){
            // 创建一个新的文件，并开始上传
            return true;
        }

        return false;
    }

    // upload_log_backup目录下的所有日志文件，每半个月清除一次,根据上次记录的清除时间
    private void clearBackupLog(){
        File uploadFileDir = new File(FileManager.PATH_LOG_ROOT, LOG_BACKUP_NAME);

        // 判断备份目录是否存在
        if (uploadFileDir.exists()){

            // 获取备份目录下
            File[] files = uploadFileDir.listFiles();

            if (files != null && files.length > 0){
                long lastModified;
                long currentTime = System.currentTimeMillis();

                int count = 0;
                log("开始清理备份日志");

                for (File file : files){

                    // 上次文件修改时间
                    lastModified = file.lastModified();

                    if ((currentTime - lastModified) >= INTERVAL_TIME){
                        log("清理备份日志:"+file.getPath());

                        file.delete();
                        count++;
                    }
                }

                log("本次总共清理备份日志共:"+count);
            }
        }
    }

}
