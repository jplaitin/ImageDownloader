package com.jarno.imagedownloader.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;

import com.jarno.imagedownloader.ImageDownloaderApplication;
import com.jarno.imagedownloader.utils.BitmapUtils;
import com.jarno.imagedownloader.utils.FileUtils;
import com.jarno.imagedownloader.utils.Lg;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadService  extends IntentService {
    private int result = Activity.RESULT_CANCELED;
    public static final String KEY_URL = "url";
    public static final String KEY_FILE_PATH = "file_path";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "service_receiver";

    public DownloadService() {
        super("DownloadService");
    }

    // Will be called asynchronously by OS.
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra(KEY_URL);
        File imageFile = createImageFile();

        InputStream is = null;
        try {

            URL url = new URL(urlPath);
            is = url.openConnection().getInputStream();
            FileUtils.writeToFile(is, imageFile);
            createThumb(imageFile);
            result = Activity.RESULT_OK;

        } catch (Exception e) {
            e.printStackTrace();
        }

        publishResults(imageFile.getAbsolutePath(), result);
    }

    private File createImageFile() {
        String tstamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(ImageDownloaderApplication.sourceImgFolderPath + File.separator + "IMG_" + tstamp + ".jpg");
    }

    private void createThumb(File imageFile){
        String fileName = (new File(imageFile.getPath())).getName();
        File destThumbFile = new File(ImageDownloaderApplication.thumbNailForlderPath + File.separator + fileName);
        Bitmap thumb = null;
        String errorTxt = null;
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(imageFile));
            thumb = BitmapUtils.createCenterCroppedThumb(in, 200, 200);
            Lg.i("thumb after center cropping w: " + thumb.getWidth() + " h: " + thumb.getHeight());
            in.close();
            FileUtils.saveAsJpeg(thumb, 90, destThumbFile);
            assert thumb != null;
        } catch (FileNotFoundException e) {
            errorTxt =  "Couldn't access image file";
        } catch (IOException e) {
            errorTxt =  "Releasing original image failed";
        }
    }

    private void publishResults(String imageFilePath, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(KEY_FILE_PATH, imageFilePath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}

