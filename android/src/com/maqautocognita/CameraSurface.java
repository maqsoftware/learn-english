package com.maqautocognita;

/*
 * Copyright 2012 Johnny Lish (johnnyoneeyed@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera;

    public CameraSurface(Activity activity) {
        super(activity);
        // We're implementing the Callback interface and want to get notified
        // about certain surface events.
        getHolder().addCallback(this);
        // We're changing the surface to a PUSH surface, meaning we're receiving
        // all buffer data from another component - the camera, in this case.
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {

        // Once the surface is created, simply open a handle to the camera hardware.
        try {
            camera = Camera.open();
            if (Configuration.ORIENTATION_PORTRAIT == getContext().getResources().getConfiguration().orientation) {
                camera.setDisplayOrientation(90);
            }
        } catch (Exception ex) {
            Log.e(getClass().getName(), ex.toString(), ex);
        }

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (null != camera) {
            // We also assign the preview display to this surface...
            Camera.Parameters p = camera.getParameters();

            p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            camera.setParameters(p);
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Once the surface gets destroyed, we stop the preview mode and release
        // the whole camera since we no longer need it.
        if (null != camera) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public Camera getCamera() {
        return camera;
    }

}
