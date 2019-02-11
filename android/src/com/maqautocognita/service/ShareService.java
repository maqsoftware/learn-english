package com.maqautocognita.service;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.maqautocognita.adapter.IShareService;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ShareService implements IShareService {

    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    private final Activity activity;

    public ShareService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void sharePhotoWithTextToFacebook(String imagePath, String text) {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath()))
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareDialog.show(activity, content);
    }

    @Override
    public void shareTextToFacebook(String text) {
//        ShareDialog.show(activity, new ShareLinkContent.Builder()
//                .setContentTitle(activity.getString(R.string.app_name))
//                .setContentDescription(text)
//                .build());

        shareToApplication(FACEBOOK_PACKAGE_NAME, text);
    }

    @Override
    public void shareToWhatsapp(String text) {
        shareToApplication(WHATSAPP_PACKAGE_NAME, text);
    }

    @Override
    public boolean isFacebookInstalled() {
        return isApplicationInstalled(FACEBOOK_PACKAGE_NAME);
    }

    @Override
    public boolean isWhatsappInstalled() {
        return isApplicationInstalled(WHATSAPP_PACKAGE_NAME);
    }

    private boolean isApplicationInstalled(String packageName) {
        PackageManager packageManager = activity.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

        return true;
    }

    private void shareToApplication(String packageName, String text) {
        PackageManager packageManager = activity.getPackageManager();
        try {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code in catch block will be called
            shareIntent.setPackage(packageName);

            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            activity.startActivity(Intent.createChooser(shareIntent, "Share"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(activity, "", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
