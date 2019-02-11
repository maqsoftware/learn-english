package com.maqautocognita.handWritingRecognition;

import android.util.Log;

import com.maqautocognita.utils.Zip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.ZipFile;

public class LipiTKJNIInterface {

    private static final String PROJECT = "SHAPEREC_ALPHANUM";

    private static final String LIPITK_FOLDER_NAME = "lipitk";

    private static boolean libraryLoaded;

    static {
        try {
            System.loadLibrary(LIPITK_FOLDER_NAME);
            libraryLoaded = true;
        } catch (UnsatisfiedLinkError ex) {
            libraryLoaded = false;
        }
    }

    private final String lipiStorePath;

    public LipiTKJNIInterface(String lipiStorePath) {
        this.lipiStorePath = lipiStorePath;
        String[] fileNames = new File(lipiStorePath).list();

        if (fileNames != null) {
            for (String fileName : fileNames) {
                if (fileName.indexOf(".zip") > 0) {
                    try {
                        File file = new File(lipiStorePath + File.separator + fileName);
                        ZipFile zipFile = new ZipFile(file);
                        Zip _zip = new Zip(zipFile);
                        _zip.unzip(lipiStorePath + File.separator);
                        _zip.close();
                        file.delete();
                    } catch (IOException ie) {
                        Log.e(LipiTKJNIInterface.class.getName(), "failed extraction", ie);
                    }
                }
            }
        }
    }

    public String getSymbolName(int id, String project_config_dir) {
        String line;
        int temp;
        String[] splited_line = null;
        try {
            File map_file = new File(project_config_dir + "unicodeMapfile_alphanumeric.ini");
            BufferedReader readIni = new BufferedReader(new FileReader(map_file));
            readIni.readLine();
            readIni.readLine();
            readIni.readLine();
            readIni.readLine();
            while ((line = readIni.readLine()) != null) {
                splited_line = line.split(" ");
                Log.d("JNI_LOG", "split 0=" + splited_line[0]);
                Log.d("JNI_LOG", "split 1=" + splited_line[1]);
                splited_line[0] = splited_line[0].substring(0, splited_line[0].length() - 1); //trim out = sign
                if (splited_line[0].equals((new Integer(id)).toString())) {
                    splited_line[1] = splited_line[1].substring(2);
                    temp = Integer.parseInt(splited_line[1], 16);
                    return String.valueOf((char) temp);
                }
            }
        } catch (Exception ex) {
            Log.d("JNI_LOG", "Exception in getSymbolName Function" + ex.toString());
            return "-1";
        }
        return "0";
    }

    public void initialize() {
        if (libraryLoaded) {
            try {
                initializeNative(lipiStorePath, PROJECT);
            } catch (Exception e) {
            }
        }
    }

    // Initializes the LipiTKEngine in native code
    private native void initializeNative(String lipiDirectory, String project);

    public LipitkResult[] recognize(Stroke[] strokes) {
        LipitkResult[] results = recognizeNative(strokes, strokes.length);

        for (LipitkResult result : results) {
            Log.d("jni", "ShapeID = " + result.Id + " Confidence = " + result.Confidence);
        }

        return results;
    }

    // Returns a list of results when recognizing the given list of strokes
    private native LipitkResult[] recognizeNative(Stroke[] strokes, int numJStrokes);

    public boolean isLibraryLoaded() {
        return libraryLoaded;
    }

    public String getLipiDirectory() {
        return lipiStorePath;
    }

}
