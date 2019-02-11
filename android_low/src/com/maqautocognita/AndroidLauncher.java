package com.maqautocognita;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PixelFormat;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.maqautocognita.adapter.IDeviceService;
import com.maqautocognita.alxp.DownloadExpansionFile;
import com.maqautocognita.handWritingRecognition.LipiTKJNIInterface;
import com.maqautocognita.prototype.databases.DBHelperManager;
import com.maqautocognita.service.AnalyticSpotService;
import com.maqautocognita.service.AndroidDeviceCameraService;
import com.maqautocognita.service.AudioService;
import com.maqautocognita.service.HandWritingRecognizeService;
import com.maqautocognita.service.OCRService;
import com.maqautocognita.service.ShareService;
import com.maqautocognita.service.SpeechRecognizeService;
import com.maqautocognita.utils.FileUtils;
import com.maqautocognita.utils.Zip;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.vending.expansion.downloader.Helpers;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.zip.ZipFile;


/**
 *
 */
public class AndroidLauncher extends AndroidApplication implements IDeviceService, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String EXPANSION_FILE_VERSION_KEY_NAME = "download_expansion_file_vesion";

    private static final String DICTIONARY_DB_NAME = "autoCognita.db";

    private static final String LESSON_DB_NAME = "autoCognita_lesson.db";

    private GoogleApiClient mGoogleApiClient;
    private double[] coordinates;
    private AutoCognitaGame game;
    private AudioService audioService;
    private LipiTKJNIInterface lipitkInterface;

    private Boolean vStartSend = false;
    private AnalyticSpotService analyticSpotService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startGps();

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if (isTablet) {
            setScreenOrientationToLandscape();
        } else {
            setScreenOrientationToPortrait();
        }
        isTablet = false;
        setScreenOrientationToPortrait();

        game = new AutoCognitaGame(isTablet,20);

        AdapterAndroid adapterAndroid = new AdapterAndroid(this);

        game.setNotificationHandler(adapterAndroid);

        //init the audioService in here,because the handler inside the service must be init in the main UI thread
        audioService = new AudioService(getContext(), getAudioStorePath());

        analyticSpotService = new AnalyticSpotService(new DBHelperManager(getContext(), "autoCognita-player.db", 1).getDBHelperDatabase());
        retrieveUserId();

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGLSurfaceView20API18 = false;
        // we need to change the default pixel format - since it does not include an alpha channel
        // we need the alpha channel so the camera preview will be seen behind the GL scene
        cfg.r = 8;
        cfg.g = 8;
        cfg.b = 8;
        cfg.a = 8;

        initialize(game, cfg);

        final AndroidLauncher launcher = this;
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                game.showLoadingScreen();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        configAndStartGame();
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {

                                String lessonDatabasePath = FileUtils.copyFileFromAsset(getContext(), LESSON_DB_NAME, false);

                                game.start(
                                        "jdbc:sqldroid:" + lessonDatabasePath,
                                        "jdbc:sqldroid:" + getDatabaseStorePath(),
                                        audioService,
                                        new HandWritingRecognizeService(lipitkInterface),
                                        new SpeechRecognizeService(getSpeechRecognizerStorePath()), launcher, new AndroidDeviceCameraService(launcher),
                                        new DBHelperManager(getContext(), "autocognita_storymode.db", 1).getDBHelperDatabase(),
                                        new OCRService(getContext(), getOCRDataStorePath()), new ShareService(launcher),
                                        analyticSpotService);
                            }
                        });
                    }
                }).start();

            }
        });

        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            // force alpha channel - I'm not sure we need this as the GL surface is already using alpha channel
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
        // we don't want the screen to turn off during the long image saving process
        graphics.getView().setKeepScreenOn(true);

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void configAndStartGame() {
        try {
            Class.forName("org.sqldroid.SQLDroidDriver");
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }

        SharedPreferences prefs = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        if (isRequiredUnzipExpansionFile(prefs)) {
            unzipExpansionFile(getExpansionFilePath());
            prefs.edit().putInt(EXPANSION_FILE_VERSION_KEY_NAME, DownloadExpansionFile.xAPK.mFileVersion).apply();
        }

        Log.i(getClass().getName(), " expansion file stored in " + getExpansionFilePath());
        Log.i(getClass().getName(), "database store in " + getDatabaseStorePath());
        Log.i(getClass().getName(), "Lipi store in " + getLipiStorePath());
        Log.i(getClass().getName(), "speech recognizer store in " + getSpeechRecognizerStorePath());
        Log.i(getClass().getName(), "audio store in " + getAudioStorePath());

        lipitkInterface = new LipiTKJNIInterface(getLipiStorePath());
        lipitkInterface.initialize();

        Log.i(getClass().getName(), "lipitkInterface init complete");
    }


    private boolean isRequiredUnzipExpansionFile(SharedPreferences prefs) {
        return prefs.getInt(EXPANSION_FILE_VERSION_KEY_NAME, 0) != DownloadExpansionFile.xAPK.mFileVersion;
    }

    private void startGps() {

        // Create an instance of GoogleAPIClient.
        if (null == mGoogleApiClient) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /**
     * get the path which the expansionFile which downloaded from play store will be stored
     *
     * @return
     */
    private String getExpansionFilePath() {

        //return Environment.getExternalStorageDirectory().toString() + File.separator + "autocognita.zip";

        return Environment.getExternalStorageDirectory().toString() + APKExpansionSupport.EXP_PATH + Helpers.getPackageName(this) + File.separator +
                Helpers.getExpansionAPKFileName(this, DownloadExpansionFile.xAPK.mIsMain, DownloadExpansionFile.xAPK.mFileVersion);
    }

    /**
     * get the database path
     *
     * @return
     */
    public String getDatabaseStorePath() {
        return getUnzippedExpansionFilePath() + DICTIONARY_DB_NAME;
    }

    /**
     * get the Lipi path
     *
     * @return
     */
    public String getLipiStorePath() {
        return getUnzippedExpansionFilePath() + "lipitk";
    }

    /**
     * get the Lipi path
     *
     * @return
     */
    public String getSpeechRecognizerStorePath() {
        return getUnzippedExpansionFilePath() + "sync";
    }

    /**
     * get the audio path which will be stored, all audio will be stored in the path
     *
     * @return
     */
    public String getAudioStorePath() {
        return getUnzippedExpansionFilePath() + "audio";
    }

    @Override
    public void setScreenOrientationToLandscape() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void setScreenOrientationToPortrait() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public double[] getUserCurrentLocation() {
        return coordinates;
    }

    @Override
    public boolean isStoryModeEnable() {
        return false; // temp remove story for lower the size of the app
        //return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && System.getProperty("os.arch").indexOf("64") >= 0;
    }

    @Override
    public String getVersionName() {
        return getVersionName(getApplicationContext());
    }

    @Override
    public boolean isSpanishLocale() {
        return getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase("es");
    }

    public String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(getClass().getName(), "when getting version name", e);
        }

        return null;
    }

    /**
     * get the ocr data store path
     *
     * @return
     */
    public String getOCRDataStorePath() {
        return getUnzippedExpansionFilePath();
    }

    /**
     * get the path which will be store the unzipped expansionFile, it will be always call when the expansion is downloaded, and going to unzip
     *
     * @return
     */
    private String getUnzippedExpansionFilePath() {
        return getContext().getExternalFilesDir(null).toString() + File.separator + "maqautocognita" + File.separator;
    }

    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.maqautocognita.alxp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void post(Runnable r) {
        handler.post(r);
    }

    private void unzipExpansionFile(String zipFilePath) {
        Log.i(getClass().getName(), "going to unzip the file for " + zipFilePath);
        try {
            File file = new File(zipFilePath);
            ZipFile zipFile = new ZipFile(file);
            Zip _zip = new Zip(zipFile);
            _zip.unzip(getUnzippedExpansionFilePath());
            _zip.close();
            Log.i(getClass().getName(), "the file " + zipFilePath + " unzipped successfully");
            //file.delete();
        } catch (IOException ie) {
            Log.e(getClass().getName(), "fail to unzip the file " + zipFilePath, ie);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Create the LocationRequest object
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        handleLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Gdx.app.log(getClass().getName(), "onConnectionSuspended");
    }

    private void handleLocation(Location location) {
        if (null != location) {
            if (null == coordinates) {
                coordinates = new double[2];
            }
            coordinates[0] = location.getLatitude();
            coordinates[1] = location.getLongitude();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
            //save record to Analytic Spot when App close
            if (vStartSend) {
                if (null != analyticSpotService) {
                    analyticSpotService.onPause();
                }
                vStartSend = false;
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "onPause Exception:", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            //save record to Analytic Spot when App resume
            if (!vStartSend) {
                if (null != analyticSpotService) {
                    analyticSpotService.onResume();
                }
                vStartSend = true;
            }
            if (null != mGoogleApiClient) {
                mGoogleApiClient.connect();
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "onResume Exception:", e);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Gdx.app.log(getClass().getName(), connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
        handleLocation(location);
    }


    /**
     * Returns a persistent, unique user id. If there is no such id one is created and saved for later use.
     */
    private String getPersistentUserId() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                analyticSpotService.SHARED_PREFS_FILE_NAME, android.content.Context.MODE_PRIVATE);
        String userId = prefs.getString(analyticSpotService.USERNAME_KEY, null);
        if (userId == null) {
            userId = UUID.randomUUID().toString();
            prefs.edit().putString(analyticSpotService.USERNAME_KEY, userId).apply();
        }
        return userId;
    }

    /**
     * Returns a User ID, unique user id. If there is no such id one is created and saved for later use.
     */
    public void retrieveUserId() {
        try {
            Intent userIdRetrievalIntent = new Intent();
            userIdRetrievalIntent.setAction("com.analyticspot.xprize.USER_ID_REQUEST");
            BroadcastReceiver userIdResultReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (this.getResultCode() == Activity.RESULT_OK) {
                        // The userId UUID is returned by getResultData().
                        // It is best to save this once
                        // in persistent storage for your app.
                        // That way you can continue using the same userId even if the
                        // user uninstalls
                        // the Literacy Now app or deletes its data.
                        try {
                            String userId = this.getResultData();
                            if (null != userId) {
                                analyticSpotService.userFrom = "AnalyticSpot";
                                analyticSpotService.saveUserId(context, userId);
                                vStartSend = true;
                            } else {
                                userId = getPersistentUserId();
                                analyticSpotService.userFrom = "Other";
                                analyticSpotService.saveUserId(context, userId);
                                vStartSend = true;
                            }
                        } catch (Exception e) {
                            Log.e(this.getClass().getName(), "retrieveUserId Exception:", e);
                        }
                    }
                }
            };
            this.sendOrderedBroadcast(userIdRetrievalIntent, null,
                    userIdResultReceiver, null, Activity.RESULT_OK, null, null);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "retrieveUserId Exception:", e);
        }
    }
}
