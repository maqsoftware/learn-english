package com.maqautocognita.service;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.bo.storyMode.Clothes;
import com.maqautocognita.bo.storyMode.Face;
import com.maqautocognita.bo.storyMode.FacialFeatures;
import com.maqautocognita.bo.storyMode.Hair;
import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.prototype.databases.DatabaseCursor;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CharacterService {

    private static CharacterService instance = null;

    private final Database storyModeDatabase;

    private List<Hair> hairList;

    private List<FacialFeatures> eyebrowList;
    private List<FacialFeatures> noseList;
    private List<FacialFeatures> eyeList;
    private List<FacialFeatures> mouthList;

    private List<Face> faceList;

    private List<Clothes> clothesList;

    private CharacterService() {
        this.storyModeDatabase = AutoCognitaGame.storyModeDatabase;
    }

    public static CharacterService getInstance() {
        if (instance == null) {
            instance = new CharacterService();
        }
        return instance;
    }

    public Face getDefaultFace() {
        for (Face face : getAllFace()) {
            if (StringUtils.isBlank(face.color)) {
                return face;
            }
        }

        return null;
    }

    public List<Face> getAllFace() {
        if (null == faceList) {
            DatabaseCursor databaseCursor = null;
            try {

                // get data from database
                databaseCursor = storyModeDatabase.rawQuery("select id,color,x_position_to_image,y_position_to_image,body_x_position_to_image,body_y_position_to_image from Face");

                while (databaseCursor.next()) {
                    Face face = new Face();
                    face.id = databaseCursor.getInt(0);
                    face.color = databaseCursor.getString(1);
                    face.xPositionToImage = databaseCursor.getInt(2);
                    face.yPositionToImage = databaseCursor.getInt(3);
                    face.bodyXPositionToImage = databaseCursor.getInt(4);
                    face.bodyYPositionToImage = databaseCursor.getInt(5);


                    if (null == faceList) {
                        faceList = new ArrayList<Face>();
                    }

                    faceList.add(face);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != databaseCursor) {
                    databaseCursor.close();
                }
            }
        }

        return faceList;
    }

    public Hair getSelectedHair() {
        int selectedHairId = UserPreferenceUtils.getInstance().getHairId();
        for (Hair hair : getAllHair()) {
            if (hair.id == selectedHairId) {
                return hair;
            }
        }

        return null;
    }

    public List<Hair> getAllHair() {
        if (null == hairList) {
            DatabaseCursor databaseCursor = null;
            try {

                // get data from database
                databaseCursor = storyModeDatabase.rawQuery("select id,is_girl,is_boy," +
                        "color,image_name,front_image_name,rear_image_name,front_hair_x_position_to_head,front_hair_y_position_to_head,rear_hair_x_position_to_head," +
                        "rear_hair_y_position_to_head,front_image_width,front_image_height,rear_image_width,rear_image_height,group_id from Hair");

                while (databaseCursor.next()) {
                    Hair hair = new Hair();
                    hair.id = databaseCursor.getInt(0);
                    hair.isGirl = databaseCursor.getInt(1) == 1;
                    hair.isBoy = databaseCursor.getInt(2) == 1;
                    hair.setColor(databaseCursor.getString(3));
                    hair.imageName = databaseCursor.getString(4);
                    hair.frontImageName = databaseCursor.getString(5);
                    hair.rearImageName = databaseCursor.getString(6);
                    hair.frontHairXPositionToHead = databaseCursor.getInt(7);
                    hair.frontHairYPositionToHead = databaseCursor.getInt(8);
                    hair.rearHairXPositionToHead = databaseCursor.getInt(9);
                    hair.rearHairYPositionToHead = databaseCursor.getInt(10);
                    hair.frontImageWidth = databaseCursor.getInt(11);
                    hair.frontImageHeight = databaseCursor.getInt(12);
                    hair.rearImageWidth = databaseCursor.getInt(13);
                    hair.rearImageHeight = databaseCursor.getInt(14);
                    hair.groupId = databaseCursor.getInt(15);

                    if (null == hairList) {
                        hairList = new ArrayList<Hair>();
                    }
                    hairList.add(hair);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != databaseCursor) {
                    databaseCursor.close();
                }
            }
        }

        return hairList;
    }

    public Face getSelectedFace() {
        int selectedFaceId = UserPreferenceUtils.getInstance().getFaceId();
        if (selectedFaceId > 0) {
            for (Face face : getAllFace()) {
                if (face.id == selectedFaceId) {
                    return face;
                }
            }
        }

        return null;
    }

    public FacialFeatures getSelectedNose() {
        return getSelectedFacialFeatures(UserPreferenceUtils.getInstance().getNoseId(), getAllNose());
    }

    private FacialFeatures getSelectedFacialFeatures(int facialFeaturesId, List<FacialFeatures> facialFeaturesList) {
        if (facialFeaturesId > 0) {
            for (FacialFeatures facialFeatures : facialFeaturesList) {
                if (facialFeatures.id == facialFeaturesId) {
                    return facialFeatures;
                }
            }
        }

        return null;
    }

    public List<FacialFeatures> getAllNose() {
        if (null == noseList) {
            noseList = getAllFacialFeaturesByTableName("Nose");
        }

        return noseList;
    }

    private List<FacialFeatures> getAllFacialFeaturesByTableName(String tableName) {
        DatabaseCursor databaseCursor = null;
        List<FacialFeatures> facialFeaturesList = null;
        try {

            // get data from database
            databaseCursor = storyModeDatabase.rawQuery("select id,x_position_to_image,y_position_to_image from " + tableName);

            while (databaseCursor.next()) {
                FacialFeatures facialFeatures = new FacialFeatures();
                facialFeatures.id = databaseCursor.getInt(0);
                facialFeatures.xPositionToImage = databaseCursor.getInt(1);
                facialFeatures.yPositionToImage = databaseCursor.getInt(2);

                if (null == facialFeaturesList) {
                    facialFeaturesList = new ArrayList<FacialFeatures>();
                }

                facialFeaturesList.add(facialFeatures);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != databaseCursor) {
                databaseCursor.close();
            }
        }

        return facialFeaturesList;
    }

    public FacialFeatures getSelectedEye() {
        return getSelectedFacialFeatures(UserPreferenceUtils.getInstance().getEyeId(), getAllEye());
    }

    public List<FacialFeatures> getAllEye() {
        if (null == eyeList) {
            eyeList = getAllFacialFeaturesByTableName("Eye");
        }

        return eyeList;
    }

    public FacialFeatures getSelectedEyebrow() {
        return getSelectedFacialFeatures(UserPreferenceUtils.getInstance().getEyebrowId(), getAllEyebrow());
    }

    public List<FacialFeatures> getAllEyebrow() {
        if (null == eyebrowList) {
            eyebrowList = getAllFacialFeaturesByTableName("Eyebrow");
        }

        return eyebrowList;
    }

    public FacialFeatures getSelectedMouth() {
        return getSelectedFacialFeatures(UserPreferenceUtils.getInstance().getMouthId(), getAllMouth());
    }

    public List<FacialFeatures> getAllMouth() {
        if (null == mouthList) {
            mouthList = getAllFacialFeaturesByTableName("Mouth");
        }

        return mouthList;
    }

    public Clothes getSelectedTopClothes() {
        return getClothesById(UserPreferenceUtils.getInstance().getTopClothesId());
    }

    public Clothes getClothesById(int clothesId) {
        if (clothesId > 0) {
            for (Clothes clothes : getAllClothes()) {
                if (clothes.id == clothesId) {
                    return clothes;
                }
            }
        }

        return null;
    }

    public List<Clothes> getAllClothes() {
        if (null == clothesList) {
            DatabaseCursor databaseCursor = null;
            try {

                // get data from database
                databaseCursor = storyModeDatabase.rawQuery("select id,image_name,word,is_girl,is_boy,selection_level,image_width,image_height,x_position_to_body,y_position_to_body,level," +
                        "front_image_name,rear_image_name,another_selection_level from Clothes");

                while (databaseCursor.next()) {
                    Clothes clothes = new Clothes();
                    clothes.id = databaseCursor.getInt(0);
                    clothes.imageName = databaseCursor.getString(1);
                    clothes.word = databaseCursor.getString(2);
                    clothes.isGirl = databaseCursor.getInt(3) == 1;
                    clothes.isBoy = databaseCursor.getInt(4) == 1;
                    clothes.selectionLevel = databaseCursor.getInt(5);
                    clothes.imageWidth = databaseCursor.getInt(6);
                    clothes.imageHeight = databaseCursor.getInt(7);
                    clothes.xPositionToBody = databaseCursor.getInt(8);
                    clothes.yPositionToBody = databaseCursor.getInt(9);
                    clothes.level = databaseCursor.getInt(10);
                    clothes.frontImageName = databaseCursor.getString(11);
                    clothes.rearImageName = databaseCursor.getString(12);
                    clothes.anotherSelectionLevel = databaseCursor.getInt(13);

                    if (null == clothesList) {
                        clothesList = new ArrayList<Clothes>();
                    }

                    clothesList.add(clothes);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != databaseCursor) {
                    databaseCursor.close();
                }
            }
        }

        return clothesList;
    }

    public Clothes getSelectedBottomClothes() {
        return getClothesById(UserPreferenceUtils.getInstance().getBottomClothesId());
    }

    public Clothes getSelectedFootwear() {
        return getClothesById(UserPreferenceUtils.getInstance().getFootwearId());
    }

    public Face getFaceByColor(String color) {
        for (Face face : getAllFace()) {
            if (face.color.equalsIgnoreCase(color)) {
                return face;
            }
        }

        return null;
    }


}
