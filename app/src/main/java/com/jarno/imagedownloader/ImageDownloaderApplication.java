package com.jarno.imagedownloader;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.jarno.imagedownloader.exceptions.FileCreationException;
import com.jarno.imagedownloader.utils.FileUtils;
import com.jarno.imagedownloader.utils.Lg;

import java.io.File;

/**
 * Created by Jarno on 30.4.2016.
 */
public class ImageDownloaderApplication extends Application {
    public static String sourceImgFolderPath;
    public static String thumbNailForlderPath;
    public static String appFolder;
    public static String tempFolder;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    public void initApplication() {
        // l.i("initApplication()");
        SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        if(prefs.getBoolean("first_run", true)) {
            try {
                firstStartApplicationInit();
                final Editor edit = prefs.edit();
                edit.putBoolean("first_run", true);
                edit.commit();
            } catch (FileCreationException e) {
                Lg.d("Folder structer creation failed");
                e.printStackTrace();
            }
        }
        else {
            setApplicationFoldersToGlobal();
        }

    }

    /**
     * is used only once when application is started for the first time. Creates folder structures etc.
     *
     * @throws FileCreationException
     */
    private void firstStartApplicationInit() throws FileCreationException {
        // l.i("firstStartApplicationInit()");
        File appFolder 			= FileUtils.createAppFolder(getApplicationContext());
        File tempFolder 		= FileUtils.createPrivateTempFileFolder(getApplicationContext());
        File sourceImgFolder 	= FileUtils.createPrivateImageFolder(getApplicationContext(), "sourceImages");
        File thumbNailForlder 	= FileUtils.createPrivateImageFolder(getApplicationContext(), "thumbNails");

        setApplicationFoldersToGlobal();
    }

    private void setApplicationFoldersToGlobal() {
        appFolder = getExternalCacheDir().getAbsolutePath();
        File pictureDir = new File(appFolder, "Pictures");
        sourceImgFolderPath = (new File(pictureDir, "sourceImages")).getAbsolutePath();
        thumbNailForlderPath = (new File(pictureDir, "thumbNails")).getAbsolutePath();
        tempFolder = (new File(appFolder, "temp")).getAbsolutePath();
        Lg.d("appFolder: "+ appFolder);
    }
}
