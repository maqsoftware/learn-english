package com.maqautocognita.adapter;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public interface IShareService {

    void sharePhotoWithTextToFacebook(String imagePath, String text);

    void shareTextToFacebook(String text);

    void shareToWhatsapp(String text);

    boolean isFacebookInstalled();

    boolean isWhatsappInstalled();
}
