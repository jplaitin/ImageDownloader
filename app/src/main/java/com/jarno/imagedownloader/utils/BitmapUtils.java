package com.jarno.imagedownloader.utils;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

import com.jarno.imagedownloader.exceptions.ImageCreationException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Some helpful functions to optimize and make bitmap handling easyer
 * Created by Jarno on 30.4.2016.
 */
public class BitmapUtils {
//	private static DebugLog l = new DebugLog(BitmapUtils.class.getSimpleName());


    public static Bitmap loadBitmap(byte[] jpegData, int newWidth, int newHeight, boolean goodQuality) throws IOException, ImageCreationException {
        final BitmapRegionDecoder bmRDC = BitmapRegionDecoder.newInstance(new ByteArrayInputStream(jpegData), false);
        int oWidth = bmRDC.getWidth();
        int oHeight = bmRDC.getHeight();
        if( newWidth > oWidth) {
            newWidth = oWidth;
        }
        if( newHeight > oHeight) {
            newHeight = oHeight;
        }

        BitmapFactory.Options opts = createDecodeOptions(oWidth, newHeight, goodQuality);
        Bitmap smallerOrigBm = BitmapFactory.decodeStream(new ByteArrayInputStream(jpegData), null, opts);
        Bitmap scaledBM = Bitmap.createScaledBitmap(smallerOrigBm, newWidth, newHeight, false);
        smallerOrigBm.recycle();
        return scaledBM;
    }

    public static Bitmap loadBitmap(File file, int newWidth, int newHeight, boolean goodQuality) throws IOException {
        return loadBitmap(file.getAbsolutePath());
    }

    public static Bitmap loadBitmap(String imgPath, int newWidth, int newHeight, boolean goodQuality) throws IOException, ImageCreationException {

        final BitmapRegionDecoder bmRDC = BitmapRegionDecoder.newInstance(imgPath, false);
        int oWidth = bmRDC.getWidth();
        int oHeight = bmRDC.getHeight();
        if( newWidth > oWidth) {
            newWidth = oWidth;
        }
        if( newHeight > oHeight) {
            newHeight = oHeight;
        }

        BitmapFactory.Options opts = createDecodeOptions(oWidth, newHeight, goodQuality);
        Bitmap smallerOrigBm = BitmapFactory.decodeFile(imgPath, opts);
        Bitmap scaledBM = Bitmap.createScaledBitmap(smallerOrigBm, newWidth, newHeight, false);
        smallerOrigBm.recycle();
        return scaledBM;
    }


    private static BitmapFactory.Options createDecodeOptions(int oWidth, int newWidth, boolean goodQuality){
        int ratio = (int) Math.ceil((double) oWidth / newWidth);
        int inSampleSize = Integer.highestOneBit(ratio);
        final byte[] tempStorage = new byte[1024*16];
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTempStorage = tempStorage;
        opts.inSampleSize = inSampleSize;
        opts.inPreferQualityOverSpeed = goodQuality;
        if(goodQuality) {
            opts.inPreferredConfig = Config.ARGB_8888;
        }
        else {
            opts.inPreferredConfig = Config.RGB_565;
        }
        return opts;
    }

    /**
     * Crops bm with given parameters. right parameter must be less than bm's width and bottom must be less than bm's height.
     * bm is recycled once it's "cropped"
     * @param bm
     * @param top
     * @param left
     * @param right
     * @param bottom
     * @return
     */
    public static Bitmap cropBitmap(Bitmap bm, int top, int left, int right, int bottom) {
        Bitmap croppedBM = Bitmap.createBitmap(bm, left, top, (right-left), (bottom-top), null, false);
        bm.recycle();
        return croppedBM;
    }

    public static Bitmap createThumbnail(final Bitmap bm, int thumbW) {
        final int height = bm.getHeight();
        final int width = bm.getWidth();
        if (width == thumbW)
            return bm;

        final float coefWidth = (float)thumbW/width;
        Matrix m = new Matrix();
        m.postScale(coefWidth, coefWidth);

        return Bitmap.createBitmap(bm, 0, 0, width, height, m, true);
    }

    public static Bitmap rotate(final Bitmap bm, int rotation){
        if(rotation != 0) {
            Matrix m = new Matrix();
            m.setRotate(rotation, (bm.getWidth()/2.0f), (bm.getHeight()/2.0f));
            Bitmap rotBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true).copy(bm.getConfig(), true);
            bm.recycle();
            return rotBm;
        }
        return bm;
    }

    public static Bitmap createThumbnail(final Bitmap bm, int thumbW, int rotation) {
        Bitmap portraitBM = BitmapUtils.createThumbnail(bm, thumbW).copy(bm.getConfig(), true);
//		l.i("portraitBM: "+ portraitBM);
//		l.i("portraitBM: w: "+ portraitBM.getWidth() +" rotation: "+ rotation);
        if(rotation != 0) {
            Matrix m = new Matrix();
            m.setRotate(rotation, (portraitBM.getWidth()/2.0f), (portraitBM.getHeight()/2.0f));
            Bitmap rotPortraitBM = Bitmap.createBitmap(portraitBM, 0, 0, portraitBM.getWidth(), portraitBM.getHeight(), m, true).copy(portraitBM.getConfig(), true);
            portraitBM.recycle();
            return rotPortraitBM;
        }
        return portraitBM;
    }

    public static Bitmap createThumbnail(final byte[] jpeg, int width, int newWidth) {
        int ratio = (int) Math.ceil((double) width / newWidth);
        int inSampleSize = Integer.highestOneBit(ratio);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        options.inPreferQualityOverSpeed = true;
        options.inPreferredConfig = Config.RGB_565;
        return BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, options);
    }

    public static Bitmap createThumbnail(String imagePath, int width, int newWidth) {
        int ratio = (int) Math.ceil((double) width / newWidth);
        int inSampleSize = Integer.highestOneBit(ratio);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        options.inPreferQualityOverSpeed = true;
        options.inPreferredConfig = Config.RGB_565;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * Loads bitmap from filepath and returns it. If loading failed returns null
     * @param path
     * @return
     */
    public static Bitmap loadBitmap(String path) {
        Bitmap bitmap = null;
        FileInputStream f = null;
        BufferedInputStream b = null;
        DataInputStream d = null;
        int BUFSIZE = 4096;
        try {
            f = new FileInputStream(new File(path));
            b = new BufferedInputStream(f, BUFSIZE);
            d = new DataInputStream(b);
            bitmap = BitmapFactory.decodeStream(d);
            d.close();
        } catch (IOException e) {
//	        l.i("Fail to load bitmap. " + e);
            return null;
        } finally {
            FileUtils.closeSilently(f);
            FileUtils.closeSilently(b);
            FileUtils.closeSilently(d);
        }
        return bitmap;
    }


    public static Bitmap optimizedLoad(String imagePath, Point image, Point container, boolean goodQuality){
        float fitRatio = (float)image.x/image.y;
        float contRatio = (float)container.x/container.y;
//		l.i("fitRatio: "+ fitRatio +" contRatio: "+ contRatio);
        float ratio;
        if(fitRatio > contRatio) {
            ratio = (float)container.x/image.x;
        }
        else {
            ratio = (float)container.y/image.y;
        }
        int newWidth =   (int)(image.x * ratio);
//		l.i("newWidth: "+ newWidth);

        int scaleRatio = (int) Math.ceil((double) image.x / newWidth);
        int inSampleSize = Integer.highestOneBit(scaleRatio);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        options.inPreferQualityOverSpeed = goodQuality;
        options.inPreferredConfig = Config.RGB_565;
        options.inScaled = false;
        options.inTempStorage=new byte[32 * 1024];

        return BitmapFactory.decodeFile(imagePath, options);
    }

    public static Bitmap optimizedLoad(String imagePath, Point container, boolean goodQuality){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap b = BitmapFactory.decodeFile(imagePath, opts);
        if(b != null && !b.isRecycled()) {
            b.recycle();
        }
        Point image  	= new Point(opts.outWidth, opts.outHeight);
        float fitRatio = (float)image.x/image.y;
        float contRatio = (float)container.x/container.y;
//		l.i("fitRatio: "+ fitRatio +" contRatio: "+ contRatio);
        float ratio;
        if(fitRatio > contRatio) {
            ratio = (float)container.x/image.x;
        }
        else {
            ratio = (float)container.y/image.y;
        }
        int newWidth =   (int)(image.x * ratio);
//		l.i("newWidth: "+ newWidth);

        int scaleRatio = (int) Math.ceil((double) image.x / newWidth);
        int inSampleSize = Integer.highestOneBit(scaleRatio);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        options.inPreferQualityOverSpeed = goodQuality;
        options.inPreferredConfig = Config.RGB_565;
        options.inScaled = false;
        options.inTempStorage=new byte[32 * 1024];

        return BitmapFactory.decodeFile(imagePath, options);
    }

    public static Bitmap createCenterCroppedThumb(InputStream input, int cW, int cH) throws IOException{
        BitmapRegionDecoder regDec = BitmapRegionDecoder.newInstance(input, false);
        int imgW = regDec.getWidth();
        int imgH = regDec.getHeight();
        int sampleSize = BitmapUtils.calculateInSampleSize(imgW, imgH, cW, cH);
        int sW = imgW / sampleSize;
        int sH = imgH / sampleSize;
        // l.i("sW: "+ sW +" sH: "+ sH);
        // l.i("sampleSize: "+ sampleSize);
        float aspectW = (float) cW / sW;
        float aspectH = (float) cH / sH;
        float scale = Math.max(aspectW, aspectH);
        // l.i("aspectW: "+ aspectW +" aspectH: "+ aspectH);
        int tempTW = (int) (cW / scale);
        int tempTH = (int) (cH / scale);
        // l.i("tempTW: "+ tempTW +" tempTH: "+ tempTH);

        int dx = (int) ((float) (sW - tempTW) / 2.0f);
        int dy = (int) ((float) (sH - tempTH) / 2.0f);
//		l.i("dx: "+ dx +" dy: "+ dy);

        int DX = (int) (((float) dx / sW) * imgW);
        int DY = (int) (((float) dy / sH) * imgH);
//		l.i("DX: "+ DX +" DY: "+ DY);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inPreferQualityOverSpeed = true;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inTempStorage = new byte[16 * 1024];
        opts.inSampleSize = sampleSize;
        Bitmap thumb = null;

        Rect r = new Rect(DX, DY, imgW - DX, imgH - DY);
//		l.i("r: "+ r.toString());

        thumb = regDec.decodeRegion(r, opts);
//		l.i("thumb w: "+ thumb.getWidth() +" h: "+ thumb.getHeight());
        Bitmap temp = Bitmap.createScaledBitmap(thumb, cW, cH, false);
//		l.i("temp w: "+ temp.getWidth() +" h: "+ temp.getHeight());
        if (temp != thumb) {
            thumb.recycle();
        }
        return temp;
    }

    /**
     * Gets sample size that if used loades bigger or same size bitmap as container edge. Loaded
     * bitmap will be smaller or same size as original.
     * @param containerEdge
     * @param edge
     * @return
     */
    public static int getClosestFitSampleSize( int containerEdge, int edge){
        int sampleSize = 1;
        int multiply   = 1;
        while(containerEdge*multiply < edge) {
            sampleSize++;
            multiply *= 2;
        }
        return sampleSize;
    }

    public static int calculateInSampleSize(int height, int width, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return Integer.highestOneBit(inSampleSize);
    }


}