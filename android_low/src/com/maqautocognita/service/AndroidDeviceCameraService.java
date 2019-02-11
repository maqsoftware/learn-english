package com.maqautocognita.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;

import com.maqautocognita.AndroidLauncher;
import com.maqautocognita.CameraSurface;
import com.maqautocognita.adapter.IDeviceCameraService;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AndroidDeviceCameraService implements IDeviceCameraService {

    private final AndroidLauncher activity;
    private CameraSurface cameraSurface;
    private boolean safeToTakePicture;

    public AndroidDeviceCameraService(AndroidLauncher activity) {
        this.activity = activity;
    }

    @Override
    public synchronized void stopPreview() {
        // stop previewing.
        if (cameraSurface != null) {
//            ViewParent parentView = cameraSurface.getParent();
//            if (parentView instanceof ViewGroup) {
//                ViewGroup viewGroup = (ViewGroup) parentView;
//                viewGroup.removeView(cameraSurface);
//            }

            if (null != cameraSurface.getCamera()) {
                cameraSurface.getCamera().stopPreview();
            }

            safeToTakePicture = false;

            //cameraSurface.surfaceDestroyed(null);

            //cameraSurface = null;
        }
        //activity.restoreFixedSize();
    }

    @Override
    public synchronized void takePicture(final float heightRatioFromPictureTop, final String savePictureFullPath,
                                         final IAdvanceActionListener<Boolean> actionListener, final int compressQuality) {
        if (safeToTakePicture) {
            // the user request to take a picture - start the process by requesting focus
            setCameraParametersForPicture(cameraSurface.getCamera());
            cameraSurface.getCamera().takePicture(null, null, null, new Camera.PictureCallback() {
                @Override
                public synchronized void onPictureTaken(byte[] pictureData, Camera camera) {
                    camera.stopPreview();
                    safeToTakePicture = false;

                    // We got the picture data - keep it
                    FileOutputStream stream = null;
                    try {
                        stream = new FileOutputStream(savePictureFullPath);
                        Bitmap bitmap = rotate(BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length), 90);
                        bitmap = Bitmap.createBitmap(bitmap, 0,
                                0,
                                bitmap.getWidth(), (int) (bitmap.getHeight() * heightRatioFromPictureTop));

                        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, stream);
                        bitmap.recycle();

                    } catch (FileNotFoundException e) {
                        Log.e(getClass().getName(), "", e);
                    } catch (IOException e) {
                        Log.e(getClass().getName(), "", e);
                    } finally {
                        if (null != stream) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                                //nothing can do
                            }
                        }

                        if (null != actionListener) {
                            actionListener.onComplete(true);
                        }
                    }
                }
            });
        } else {
            if (null != actionListener) {
                actionListener.onComplete(false);
            }
        }
    }

    @Override
    public synchronized void takePictureFromTablet(final float widthRatio, final String savePictureFullPath,
                                                   final IAdvanceActionListener<Boolean> actionListener, final int compressQuality) {
        if (safeToTakePicture) {
            // the user request to take a picture - start the process by requesting focus
            setCameraParametersForPicture(cameraSurface.getCamera());
            cameraSurface.getCamera().takePicture(null, null, null, new Camera.PictureCallback() {
                @Override
                public synchronized void onPictureTaken(byte[] pictureData, Camera camera) {
                    camera.stopPreview();
                    safeToTakePicture = false;
                    // We got the picture data - keep it
                    FileOutputStream stream = null;
                    try {

                        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);

                        stream = new FileOutputStream(savePictureFullPath);

                        bitmap = Bitmap.createBitmap(bitmap, 0,
                                0,
                                (int) (bitmap.getWidth() * widthRatio), bitmap.getHeight());

                        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, stream);
                        bitmap.recycle();

                    } catch (FileNotFoundException e) {
                        Log.e(getClass().getName(), "", e);
                    } catch (IOException e) {
                        Log.e(getClass().getName(), "", e);
                    } finally {
                        if (null != stream) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                                //nothing can do
                            }
                        }

                        if (null != actionListener) {
                            actionListener.onComplete(true);
                        }
                    }
                }
            });
        } else {
            if (null != actionListener) {
                actionListener.onComplete(false);
            }
        }
    }

    public void setCameraParametersForPicture(Camera camera) {
        // Before we take the picture - we make sure all camera parameters are as we like them
        // Use max resolution and auto focus
        Camera.Parameters p = camera.getParameters();
        List<Camera.Size> supportedSizes = p.getSupportedPreviewSizes();

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        int displayWidth = displaySize.x;

        int maxSupportedWidth = Integer.MAX_VALUE;
        int maxSupportedHeight = -1;
        for (Camera.Size size : supportedSizes) {
            if (Math.abs(size.width - displayWidth) < Math.abs(maxSupportedWidth - displayWidth)) {
                maxSupportedWidth = size.width;
                maxSupportedHeight = size.height;
            }
        }
        p.setPictureSize(maxSupportedWidth, maxSupportedHeight);
        p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        try {
            camera.setParameters(p);
        }catch (Exception e){
            p.setPictureSize(maxSupportedHeight, maxSupportedWidth);
            try {
                camera.setParameters(p);
            }catch (Exception e2){
                p.setPictureSize(maxSupportedWidth, maxSupportedWidth);
                try {
                    camera.setParameters(p);
                }catch (Exception e3){
                    p.setPictureSize(maxSupportedHeight, maxSupportedHeight);
                    try {
                        camera.setParameters(p);
                    } catch (Exception e4){
                        Log.e(getClass().getName(), "Cannot set camera");
                    }
                }
            }
        }
    }

    @Override
    public synchronized void startPreviewAsync() {

        Runnable r = new Runnable() {
            public void run() {
                prepareCamera();
                startPreview();
            }
        };
        activity.post(r);
    }

    private synchronized void prepareCamera() {
        //activity.setFixedSize(960,640);
        if (cameraSurface == null) {
            cameraSurface = new CameraSurface(activity);
            activity.addContentView(cameraSurface, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    private synchronized void startPreview() {
        // ...and start previewing. From now on, the camera keeps pushing preview
        // images to the surface.
        if (cameraSurface != null && cameraSurface.getCamera() != null && !safeToTakePicture) {
            cameraSurface.getCamera().startPreview();
            safeToTakePicture = true;
        }
    }

    @Override
    public boolean isReady() {
        if (cameraSurface != null && cameraSurface.getCamera() != null) {
            return true;
        }
        return false;
    }

    public Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
}
