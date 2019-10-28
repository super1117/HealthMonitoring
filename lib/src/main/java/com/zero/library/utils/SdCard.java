package com.zero.library.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.zero.library.Library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * create by szl on 2017/8/18
 */

public class SdCard {
    /**
     * 判断SD卡是否存在
     * @return
     */
    public static boolean getSDState() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 删除文件夹下的所有文件
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if(files == null || files.length == 0){
                    file.delete();
                    return;
                }
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {

        }
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static File createSDFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        return file;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean deleteSDFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 创建文件夹
     *
     * @param path
     * @return
     */
    public static File createSDDir(String path) {
        File dir = new File(path);
        boolean result = dir.mkdirs();
//        if(!result){
//            if(Build.VERSION.SDK_INT >= 26){
//                try{
//                    Path p = Paths.get(path);
//                    Files.createDirectory(p);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }
        return dir;
    }

    /**
     * 判断SD卡上的文件夹(文件)是否存在
     *
     * @param path
     * @return
     */
    public static boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * sdcard
     *
     * @return
     */
    public static String getSDPath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator;
    }

    /**
     * sdcard/package/childFolder
     * @param childFolder
     * @return
     */
    public static String getExternalAppPath(String childFolder) {
        String externalPath = getSDPath() + Library.CONTEXT.getPackageName() + File.separator + (TextUtils.isEmpty(childFolder) ? "" : childFolder ) + File.separator;
        if (!isFileExists(externalPath)){
            createSDDir(externalPath);
        }
        showPathTag(externalPath);
        return externalPath;
    }

    private static void showPathTag(String path) {
        Log.i("aiya", path);
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile (String oldPath, String newPath) {
        try{
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 复制整个文件夹内容
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) throws Exception  {
        (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
        File a=new File(oldPath);
        String[] file=a.list();
        File temp=null;
        for (int i = 0; i < file.length; i++) {
            if(oldPath.endsWith(File.separator)){
                temp=new File(oldPath+file[i]);
            }
            else{
                temp=new File(oldPath+File.separator+file[i]);
            }

            if(temp.isFile()){
                FileInputStream input = new FileInputStream(temp);
                FileOutputStream output = new FileOutputStream(newPath + "/" +
                        (temp.getName()).toString());
                byte[] b = new byte[1024 * 5];
                int len;
                while ( (len = input.read(b)) != -1) {
                    output.write(b, 0, len);
                }
                output.flush();
                output.close();
                input.close();
            }
            if(temp.isDirectory()){//如果是子文件夹
                copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
            }
        }
    }
}
