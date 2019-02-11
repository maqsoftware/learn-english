package com.maqautocognita.africanstorybookslibrary.utils;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    /**
     * Copy the file from assets folder to the external storage , a folder will be created with the given fileName
     *
     * @param context
     * @param fileName the name of the file in the asset folder,which will be copied from the assets folder
     */
    public static String copyFileFromAsset(Context context, String fileName, boolean ignoreCopyIfFileExists) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File outFile = null;
        try {

            outFile = new File(getCopiedFileStorePath(context));

            if (!outFile.exists()) {
                outFile.mkdirs();
            }

            outFile = new File(outFile, FilenameUtils.getName(fileName));

            if (outFile.exists() && ignoreCopyIfFileExists) {
                return outFile.getAbsolutePath();
            }

            inputStream = context.getAssets().open(fileName);

            outputStream = new FileOutputStream(outFile);
            copyFile(inputStream, outputStream);
            outputStream.flush();


        } catch (IOException e) {
            Log.e(TAG, "Failed to copy the asset file: " + fileName, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }

        if (null == outFile) {
            return null;
        }
        return outFile.getAbsolutePath();
    }

    public static String getCopiedFileStorePath(Context context) {
        return context.getApplicationInfo().dataDir + "/databases/";
    }

    /**
     * Copy the given inputStream to the given outputStream
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    private static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
    }
}
