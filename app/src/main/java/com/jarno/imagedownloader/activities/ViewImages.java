package com.jarno.imagedownloader.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.jarno.imagedownloader.ImageDownloaderApplication;
import com.jarno.imagedownloader.R;
import com.jarno.imagedownloader.utils.BitmapUtils;
import com.jarno.imagedownloader.utils.FileUtils;
import com.jarno.imagedownloader.utils.Lg;

import java.io.File;

public class ViewImages extends AppCompatActivity {

    public static final String KEY_FILE_PATH = "file_path";
    private Bitmap image;
    private File imageFile;
    private File thumbFile;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String imageFilePath = getIntent().getExtras().getString(KEY_FILE_PATH);
        Lg.d(imageFilePath);
        image = BitmapUtils.loadBitmap(imageFilePath);
        imageFile = new File(imageFilePath);
        thumbFile = getThumbImageFile(imageFilePath);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.image_rotated), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                rotateImage(180);
            }
        });

        initViewRefts();
        setUpViews();
    }

    private void initViewRefts(){
        imageView = (ImageView) findViewById(R.id.image_view);
    }

    private void setUpViews(){
        if(image != null){
            imageView.setImageBitmap(image);
        }
    }

    private File getThumbImageFile(String path){
        String fileName = new File(path).getName();
        return new File(ImageDownloaderApplication.thumbNailForlderPath + File.separator + fileName);
    }

    private void rotateImage(int degrees) {
        Bitmap rotatedImg = BitmapUtils.rotate(image, degrees);
        FileUtils.saveAsJpg(rotatedImg, imageFile);

        Bitmap thumb = BitmapUtils.loadBitmap(thumbFile.getAbsolutePath());
        Bitmap rotatedThumb = BitmapUtils.rotate(thumb, degrees);
        FileUtils.saveAsJpg(rotatedThumb, thumbFile);
        rotatedThumb.recycle(); //not needed in this activity

        image = rotatedImg;
        setUpViews();
    }


}
