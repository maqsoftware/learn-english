package com.maqautocognita.service;

import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.badlogic.gdx.Gdx;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

import java.io.File;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class IBMVisualRecognizeService {

    private static IBMVisualRecognizeService instance = null;

    private VisualRecognition service;

    public static IBMVisualRecognizeService getInstance() {
        if (instance == null) {
            instance = new IBMVisualRecognizeService();

        }
        return instance;
    }

    public void doRecognize(String imagePath, final IAdvanceActionListener<String> advanceActionListener) {
        VisualRecognition service = getVisualRecognitionService();
        ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
                .images(new File(imagePath))
                .build();

        VisualClassification result = service.classify(options).execute();
        System.out.println(result);
        service.classify(options).enqueue(new ServiceCallback<VisualClassification>() {

            @Override
            public void onResponse(VisualClassification response) {
                if (null != advanceActionListener) {
                    advanceActionListener.onComplete(response.getImages().get(0).getClassifiers().get(0).getClasses().get(0).getName());
                }
            }

            @Override
            public void onFailure(Exception e) {
                Gdx.app.log(getClass().getName(), "VisualRecognition fails", e);
                if (null != advanceActionListener) {
                    advanceActionListener.onComplete(null);
                }
            }
        });
    }

    private VisualRecognition getVisualRecognitionService() {
        if (null == service) {
            service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20, "648b6d47b0fa8f0b37e8cf40b7a7b95156b3e203");
        }
        return service;
    }
}
