package com.jarno.imagedownloader.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.jarno.imagedownloader.exceptions.FileCreationException;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Utils handles file creation, deletion and write. All files are in correct file structures
 * Created by Jarno on 30.4.2016.
 */
public class FileUtils {
    public static final String FOLDER_SEPARATOR = File.separator;
    public static final String NOMEDIA_FILE_NAME = ".nomedia";
    /**
     * Creates default image folder for applications use (private). With null folder name value
     * creates root image folder other vice creates sub folder as given folderName.
     *
     * for deeper folder structure separate subfolders by '/'
     * example
     * folderName = "pageImages/thumbnails"
     *
     * @param context
     * @param folderName
     * @return
     * @throws FileCreationException
     */
    public static File createPrivateImageFolder(Context context, String folderName) throws FileCreationException {
        File pictureDir = new File(context.getExternalCacheDir(), "Pictures");
//		File pictureDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//		l.e("pictureDir is "+ pictureDir);
        StringBuilder sb = new StringBuilder(pictureDir.getAbsolutePath());
        if(folderName!= null){
            sb.append(FOLDER_SEPARATOR).append(folderName);
        }
        File file = new File(sb.toString());
        file.mkdirs();
        if(!file.exists()){
            Lg.d(sb.toString()+ " doesn't exist");
            throw new FileCreationException("failed to create private image folder: "+ sb.toString());
        }

        File noMedia = new File(file, NOMEDIA_FILE_NAME);
        try {
            noMedia.createNewFile();
        } catch (IOException e) {
            throw new FileCreationException("failed to create media scanner stopper file in private image folder: "+ noMedia.getAbsolutePath());
        }

        if(!noMedia.exists()){
            Lg.d(noMedia.getAbsolutePath()+ " doesn't exist");
            throw new FileCreationException("failed to create media scanner stopper file in private image folder: "+ noMedia.getAbsolutePath());
        }

        return file;
    }
    public static File createPrivateTempFileFolder(Context context) throws FileCreationException{
        File folderDir = context.getExternalCacheDir();

        StringBuilder sb = new StringBuilder(folderDir.getAbsolutePath());
        sb.append(FOLDER_SEPARATOR).append("temp");

        File file = new File(sb.toString());
        file.mkdirs();
        if(!file.exists()){
            Lg.d(sb.toString()+ " doesn't exist");
            throw new FileCreationException("failed to create private image folder: "+ sb.toString());
        }

        File noMedia = new File(file, NOMEDIA_FILE_NAME);
        try {
            noMedia.createNewFile();
        } catch (IOException e) {
            throw new FileCreationException("failed to create media scanner stopper file in private image folder: "+ noMedia.getAbsolutePath());
        }

        if(!noMedia.exists()){
            Lg.d(noMedia.getAbsolutePath()+ " doesn't exist");
            throw new FileCreationException("failed to create media scanner stopper file in private image folder: "+ noMedia.getAbsolutePath());
        }
        return file;
    }


    public static File createAppFolder(Context context) throws FileCreationException{
        File appFile = context.getExternalCacheDir();
        appFile.mkdirs();
        if(!appFile.exists()){
            Lg.d(appFile.getAbsolutePath()+ " doesn't exist");
            throw new FileCreationException("failed to create private image folder: "+ appFile.getAbsolutePath());
        }
        return appFile;
    }

    /**
     * Compress bitmap bm to given compress value and saves created image to given asFile value. If notifyMediaScan
     * us true notifies media scanner show that given image is instantly visible to other applications.
     *
     * @param bm
     * @param compress
     * @param asFile
     * @return
     * @throws IOException
     */
    public static boolean saveAsJpeg(Bitmap bm, int compress, File asFile){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(asFile);
            bm.compress(Bitmap.CompressFormat.JPEG, compress, out);
        } catch (FileNotFoundException e) {
            Lg.d("Failed to write image", e.getMessage());
            return false;
        }
        finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                Lg.d("Failed to close image", e.getMessage());
                return false;
            }
        }
        return asFile.exists();
    }

    public static boolean saveAsPNG(Bitmap bm, File asFile){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(asFile);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            Lg.d("Failed to write image", e.getMessage());
            return false;
        }
        finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                Lg.d("Failed to close image", e.getMessage());
                return false;
            }
        }
        return asFile.exists();
    }

    public static boolean writeToFile(InputStream is, File asFile) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(asFile);
            byte buf[]=new byte[1024];
            int len;
            try {
                while((len=is.read(buf))>0)
                    out.write(buf,0,len);
            } catch (IOException e) {
                Lg.d(e.getMessage());
                return false;
            }

        } catch (FileNotFoundException e) {
            Lg.d("Failed to write image", e.getMessage());
            return false;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    Lg.d("Failed to close image", e.getMessage());
                    return false;
                }
            }
        }
        return asFile.exists();
    }

    public static boolean saveAsJpg(Bitmap bm, File asFile){
        return saveAsJpeg(bm, 90, asFile);
    }

    public static boolean saveAsJpg(byte[] jpeg, String imgPath) {
        // Save the image.
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imgPath);
            out.write(jpeg);
        } catch (Exception e) {
            Lg.d("Failed to write image", e.getMessage());
            return false;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                Lg.d("Failed to close image", e.getMessage());
                return false;
            }
        }
        return (new File(imgPath).exists());
    }

    public static boolean saveAsJpg(byte[] jpeg, File asFile) {
        return saveAsJpg(jpeg, asFile.getAbsolutePath());
    }

    public static void closeSilently(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
        }
    }

    public static String getFileName(String absolutFilePath) {
        String[] split = absolutFilePath.split(FOLDER_SEPARATOR);
        return split[(split.length-1)];
    }

    public static ArrayList<File> listFiles(File dir){
        ArrayList<File> files = new ArrayList<File>();
        Lg.d("Folder", "Path: " + dir.getAbsolutePath());

        File file[] = dir.listFiles();
        Log.d("Files", "Size: "+ file.length);
        for (int i=0; i < file.length; i++)
        {
            Log.d("Files", "FileName:" + file[i].getName());
        }

        files.addAll(Arrays.asList(file));
        return files;
    }

    public static ArrayList<File> listImageFiles(File dir){
        ArrayList<File> files = new ArrayList<File>();
        Lg.d("Folder", "Path: " + dir.getAbsolutePath());

        File file[] = dir.listFiles();
        Log.d("Files", "Size: "+ file.length);
        for (int i=0; i < file.length; i++)
        {
            if(file[i].getName().contains("jpg")) {
                Log.d("Files", "FileName:" + file[i].getName());
                files.add(file[i]);
            }
        }
        return files;
    }
}

