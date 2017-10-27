package com.function.ianchang.functiongroup;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by ianchang on 2017/10/27.
 */
public class FileManager {

    public static File PATH_ROOT;         //Log 日志待上传文件目录名称
    public static File PATH_LOG_ROOT;         //Log 日志待上传文件目录名称
    public static File PATH_LOG_UPLOAD;         //Log 日志待上传文件目录名称

    /*******
     * 初始化Root目录和相关文件目录
     * @param context
     */
    public static void initFile(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            PATH_ROOT = context.getExternalCacheDir();
        } else {
            PATH_ROOT = context.getCacheDir();
        }

        PATH_LOG_ROOT = new File(PATH_ROOT, "log");
        PATH_LOG_UPLOAD = new File(PATH_ROOT, "log_upload");
    }


    /*****
     * 创建目录
     * @param file
     * @return
     */
    public static boolean createOrExistsDir(File file){

        if (file != null){
            if (file.exists()){
                return true;
            }else{
                return file.mkdirs();
            }
        }

        return false;
    }


    /*******
     * 创建文件
     * @param file
     * @return
     */
    public static boolean createOrExistsFile(File file){

        try {
            if (file != null){
                if (file.exists()){
                    return file.isFile();
                }else{
                    if (createOrExistsDir(file.getParentFile())){
                        return file.createNewFile();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }


        return false;
    }
}
