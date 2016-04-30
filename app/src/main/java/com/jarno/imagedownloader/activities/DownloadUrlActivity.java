package com.jarno.imagedownloader.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.jarno.imagedownloader.ImageDownloaderApplication;
import com.jarno.imagedownloader.R;
import com.jarno.imagedownloader.service.DownloadService;
import com.jarno.imagedownloader.utils.FileUtils;
import com.jarno.imagedownloader.utils.Lg;
import com.jarno.imagedownloader.views.adapters.ImageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class DownloadUrlActivity extends AppCompatActivity {
    private EditText inputFieldUrl;
    private Button btnRandomPick, btnDownload;
    private GridView gridview;

    private ArrayList<File> thumbs;
    private ArrayList<File> images;

    private final String url1 = "http://cdn-s3.si.com/s3fs-public/styles/si_gallery_slide/public/images/X159762_TK5_00993.JPG?itok=NB2G1vp_";
    private final String url2 = "http://1.imimg.com/data/C/Q/MY-1052652/Ladies-Lingerie_250x250.jpg";
    private final String url3 = "http://www.online-image-editor.com//styles/2014/images/example_image.png";
    private final String[] urls = {url1, url2, url3};

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String imageFilePath = bundle.getString(DownloadService.KEY_FILE_PATH);
                int resultCode = bundle.getInt(DownloadService.RESULT);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(DownloadUrlActivity.this,
                            getString(R.string.download_success) + imageFilePath,
                            Toast.LENGTH_LONG).show();
                    FileUtils.listFiles(new File(ImageDownloaderApplication.sourceImgFolderPath));
                    images.add(getImageFile(imageFilePath));
                    thumbs.add(getThumbImageFile(imageFilePath));
                    ((BaseAdapter) gridview.getAdapter()).notifyDataSetChanged();

                } else {
                    Toast.makeText(DownloadUrlActivity.this, getString(R.string.download_failed),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_url);

        this.thumbs = FileUtils.listImageFiles(new File(ImageDownloaderApplication.thumbNailForlderPath));
        this.images = FileUtils.listImageFiles(new File(ImageDownloaderApplication.sourceImgFolderPath));

        initViewRefts();
        setUpListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                DownloadService.NOTIFICATION));
        ((BaseAdapter) gridview.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    private void initViewRefts(){
        inputFieldUrl = (EditText) findViewById(R.id.url_input_field);
        btnRandomPick = (Button) findViewById(R.id.btn_random_pick);
        btnDownload = (Button) findViewById(R.id.btn_download);
        gridview = (GridView) findViewById(R.id.grid_view);
    }

    private void setUpListeners(){
        btnRandomPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputFieldUrl.setText(pickRandomUrl());
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lg.d();
                if(inputFieldUrl.getText() != null && inputFieldUrl.getText().length() > 0){
                    Intent intent = new Intent(DownloadUrlActivity.this, DownloadService.class);
                    intent.putExtra(DownloadService.KEY_URL, inputFieldUrl.getText().toString());
                    startService(intent);
                }
            }
        });

        gridview.setAdapter(new ImageAdapter(this, thumbs));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DownloadUrlActivity.this, ViewImages.class);
                intent.putExtra(ViewImages.KEY_FILE_PATH, images.get(position).getAbsolutePath());
                DownloadUrlActivity.this.startActivity(intent);

                Toast.makeText(DownloadUrlActivity.this, "" + position +" clicked",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File getImageFile(String path){
        String fileName = new File(path).getName();
        return new File(ImageDownloaderApplication.sourceImgFolderPath + File.separator + fileName);

    }

    private File getThumbImageFile(String path){
        String fileName = new File(path).getName();
        return new File(ImageDownloaderApplication.thumbNailForlderPath + File.separator + fileName);
    }

    private String pickRandomUrl(){
        Random r = new Random();
        int i = Math.abs(r.nextInt() % urls.length);
        int randomNumber =  i;
        Lg.d("randomNumber: "+ randomNumber);
        return urls[randomNumber];
    }
}
