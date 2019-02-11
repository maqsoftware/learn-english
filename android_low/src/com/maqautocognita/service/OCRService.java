package com.maqautocognita.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.maqautocognita.adapter.IOCR;
import com.maqautocognita.bo.OCRResult;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kotarou on 19/8/15.
 * change the file DB_PATH and DB_NAME before compile
 */


public class OCRService implements IOCR {

    final String ocrDataStorePath;
    Context context;
    //OCR interface

    public OCRService(Context pContext, String ocrDataStorePath) {
        context = pContext;
        //initialize Tesseract API
        String language = "eng";
        this.ocrDataStorePath = ocrDataStorePath;

    }


/*    @Override
    public String processImage(String pImageFilePath){

        //File imgFile = new  File("/sdcard/Images/test_image.jpg");
        //pImageFilePath = "/storage/emulated/0/DCIM/Camera/IMG_20170221_101742.jpg";
        //pImageFilePath = "/storage/emulated/0/Android/data/com.maqautocognita.prototype.android/files/data/IMG_20170221_101742.jpg";
        File imgFile = new  File(pImageFilePath);
        Bitmap pImage;
        String OCRResult = "";

        if(imgFile.exists()){

            pImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mTess.setImage(pImage);
            OCRResult = mTess.getUTF8Text();

        }

        return OCRResult;
    }*/

    @Override
    public ArrayList<int[]> getOCRRect(String pImageFilePath) {
        ArrayList<int[]> returnList = new ArrayList<int[]>();

        return returnList;
    }

    @Override
    public void processImage(final String imageFilePath, final IAdvanceActionListener<OCRResult> actionListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                File imageFile = new File(imageFilePath);
                try {

                } catch (Exception e) {
                    Log.e(getClass().getName(), "", e);
                }
            }
        }).start();
    }

    /*
    Result Code
    Err Message
    Full Result
    individual Word
    individual Word Corrodinate
    */
    @Override
    public ArrayList<String> processImage(String pImageFilePath, int left, int top, int width, int height) {

        //File imgFile = new  File("/sdcard/Images/test_image.jpg");
        //pImageFilePath = "/storage/emulated/0/DCIM/Camera/IMG_20170221_101742.jpg";
        //pImageFilePath = "/storage/emulated/0/Android/data/com.maqautocognita.prototype.android/files/data/IMG_20170221_101742.jpg";
        File imgFile = new File(pImageFilePath);
        Bitmap pImage;
        ArrayList<String> OCRResult = new ArrayList<String>();
        String OCRresult = "";
        int[] OCRCor;
        String OCRCorString = "";
        OCRResult.add("Success");
        OCRResult.add("");
        return OCRResult;
    }
}
