package com.maqautocognita.utils;

import com.maqautocognita.bo.storyMode.Clothes;
import com.maqautocognita.constant.Language;
import com.maqautocognita.constant.UserPreferencesKeys;
import com.maqautocognita.listener.IUserPreferenceValueChangeListener;
import com.maqautocognita.service.CharacterService;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * store the charter information, such as sex, selected facial features and clothes
 * <p/>
 * They will be store in the preference finally, you must call the method {@link #save()} after information updated
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class UserPreferenceUtils {

    private static final String PREFERENCES_PROFILE_NAME = "UserPreferenceUtils";
    private static UserPreferenceUtils instance = null;
    private Preferences preferences;

    private List<IUserPreferenceValueChangeListener> valueChangeListenerList;

    private Boolean isGirl;
    private int faceId;
    private int eyeId;
    private int hairId;
    private int eyebrowId;
    private int mouthId;
    private int noseId;
    private int topClothesId;
    private int bottomClothesId;
    private int footwearId;
    private String language;
    private String cheatSheetSelectedlanguage;

    private UserPreferenceUtils() {
        preferences = Gdx.app.getPreferences(PREFERENCES_PROFILE_NAME);
        this.isGirl = isGirl();
        this.faceId = getFaceId();
        this.eyeId = getEyeId();
        this.eyebrowId = getEyebrowId();
        this.hairId = getHairId();
        this.mouthId = getMouthId();
        this.noseId = getNoseId();
        this.topClothesId = getTopClothesId();
        this.bottomClothesId = getBottomClothesId();
        this.footwearId = getFootwearId();
        this.language = getLanguage();
        this.cheatSheetSelectedlanguage = getCheatSheetSelectedLanguage();
    }

    public Boolean isGirl() {
        if (preferences.contains(UserPreferencesKeys.IS_GIRL)) {
            return preferences.getBoolean(UserPreferencesKeys.IS_GIRL);
        }
        return null;
    }

    public int getFaceId() {
        return getInteger(UserPreferencesKeys.FACE_ID);
    }

    public void setFaceId(int faceId) {
        putInteger(UserPreferencesKeys.FACE_ID, faceId, this.faceId);
        this.faceId = faceId;
    }

    private void putInteger(String key, int newValue, int oldValue) {
        preferences.putInteger(key, newValue);
        save();
        if (newValue != oldValue) {
            onValueChange(key, newValue);
        }
    }

    public void save() {
        preferences.flush();
    }

    private void onValueChange(final String key, final Object value) {
        if (CollectionUtils.isNotEmpty(valueChangeListenerList)) {
            for (final IUserPreferenceValueChangeListener valueChangeListener : valueChangeListenerList) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        valueChangeListener.onValueChange(key, value);
                    }
                });
            }
        }
    }

    public int getEyeId() {
        return getInteger(UserPreferencesKeys.EYE_ID);
    }

    public void setEyeId(int eyeId) {
        putInteger(UserPreferencesKeys.EYE_ID, eyeId, this.eyeId);
        this.eyeId = eyeId;
    }

    public int getEyebrowId() {
        return getInteger(UserPreferencesKeys.EYEBROW_ID);
    }

    public int getHairId() {
        return getInteger(UserPreferencesKeys.HAIR_ID);
    }

    public void setHairId(int hairId) {
        putInteger(UserPreferencesKeys.HAIR_ID, hairId, this.hairId);
        this.hairId = hairId;
    }

    public int getMouthId() {
        return getInteger(UserPreferencesKeys.MOUTH_ID);
    }

    public int getNoseId() {
        return getInteger(UserPreferencesKeys.NOSE_ID);
    }

    public void setNoseId(int noseId) {
        putInteger(UserPreferencesKeys.NOSE_ID, noseId, this.noseId);
        this.noseId = noseId;
    }

    public int getTopClothesId() {
        return getInteger(UserPreferencesKeys.TOP_CLOTHES_ID);
    }

    public void setTopClothesId(int topClothesId) {
        putInteger(UserPreferencesKeys.TOP_CLOTHES_ID, topClothesId, this.topClothesId);
        this.topClothesId = topClothesId;
    }

    public int getBottomClothesId() {
        return getInteger(UserPreferencesKeys.BOTTOM_CLOTHES_ID);
    }

    public void setBottomClothesId(int bottomClothesId) {
        putInteger(UserPreferencesKeys.BOTTOM_CLOTHES_ID, bottomClothesId, this.bottomClothesId);
        this.bottomClothesId = bottomClothesId;
    }

    public int getFootwearId() {
        return getInteger(UserPreferencesKeys.FOOTWEAR_ID);
    }

    public void setFootwearId(int footwearId) {
        putInteger(UserPreferencesKeys.FOOTWEAR_ID, footwearId, this.footwearId);
        this.footwearId = footwearId;
    }

    public String getLanguage() {
        return getString(UserPreferencesKeys.LANGUAGE_ID);
    }

    public void setLanguage(String language) {
        putString(UserPreferencesKeys.LANGUAGE_ID, language, this.language);
    }

    private void putString(String key, String newValue, String oldValue) {
        preferences.putString(key, newValue);
        save();
        if (newValue != oldValue) {
            onValueChange(key, newValue);
        }
    }

    public String getCheatSheetSelectedLanguage() {
        return getString(UserPreferencesKeys.CHEAT_SHEET_SELECTED_LANGUAGE_ID);
    }

    public void setCheatSheetSelectedLanguage(String language) {
        putString(UserPreferencesKeys.CHEAT_SHEET_SELECTED_LANGUAGE_ID, language, this.language);
    }

    private int getInteger(String key) {
        return preferences.getInteger(key);
    }

    private String getString(String key) {
        return preferences.getString(key);
    }

    public void setMouthId(int mouthId) {
        putInteger(UserPreferencesKeys.MOUTH_ID, mouthId, this.mouthId);
        this.mouthId = mouthId;
    }

    public void setEyebrowId(int eyebrowId) {
        putInteger(UserPreferencesKeys.EYEBROW_ID, eyebrowId, this.eyebrowId);
        this.eyebrowId = eyebrowId;
    }

    public static UserPreferenceUtils getInstance() {
        if (instance == null) {
            instance = new UserPreferenceUtils();

        }
        return instance;
    }

    public boolean isStoryMissionCompleted() {
        if (preferences.contains(UserPreferencesKeys.IS_STORY_MISSION_COMPLETED_ID)) {
            return preferences.getBoolean(UserPreferencesKeys.IS_STORY_MISSION_COMPLETED_ID);
        }
        return false;
    }

    public void setStoryMissionCompleted() {
        putBoolean(UserPreferencesKeys.IS_STORY_MISSION_COMPLETED_ID, true, false);
    }

    private void putBoolean(String key, boolean newValue, boolean oldValue) {
        preferences.putBoolean(key, newValue);
        save();
        if (newValue != oldValue) {
            onValueChange(key, newValue);
        }
    }

    public void addValueChangeListener(IUserPreferenceValueChangeListener valueChangeListener) {
        if (null == valueChangeListenerList) {
            valueChangeListenerList = new ArrayList<IUserPreferenceValueChangeListener>();
        }
        valueChangeListenerList.add(valueChangeListener);
    }

    public void setIsGirl(boolean isGirl) {
        preferences.putBoolean(UserPreferencesKeys.IS_GIRL, isGirl);
        save();

        if (null == this.isGirl || !this.isGirl.equals(isGirl)) {
            onValueChange(UserPreferencesKeys.IS_GIRL, isGirl);
        }

        this.isGirl = isGirl;
    }

    public boolean isSexSelected() {
        return null != isGirl();
    }

    public boolean isFacialFeaturesConfigReady() {
        return isFaceSelected() && getNoseId() > 0 && getEyebrowId() > 0 && getEyeId() > 0 && getMouthId() > 0;
    }

    public boolean isFaceSelected() {
        return getFaceId() > 0;
    }

    public boolean isClothingConfigReady() {

        if (getTopClothesId() > 0) {
            Clothes clothes = CharacterService.getInstance().getClothesById(getTopClothesId());
            if (clothes.anotherSelectionLevel == 2) {
                return getFootwearId() > 0;
            } else {
                return getBottomClothesId() > 0 && getFootwearId() > 0;
            }

        }

        return false;
    }

    public boolean isEnglish() {
        return Language.ENGLISH.equals(getLanguage());
    }

    public boolean isSwahili() {
        return Language.SWAHILI.equals(getLanguage());
    }

    public boolean isCheatSheetSpanishSelected() {
        return com.ibm.watson.developer_cloud.language_translator.v2.model.Language.SPANISH.toString().equals(getCheatSheetSelectedLanguage());
    }

    public boolean isCheatSheetEnglishSelected() {
        return com.ibm.watson.developer_cloud.language_translator.v2.model.Language.ENGLISH.toString().equals(getCheatSheetSelectedLanguage());
    }

    public void removeByKey(String key) {
        preferences.remove(key);

        if (UserPreferencesKeys.FACE_ID.equals(key)) {
            this.faceId = 0;
        } else if (UserPreferencesKeys.HAIR_ID.equals(key)) {
            this.hairId = 0;
        } else if (UserPreferencesKeys.IS_GIRL.equals(key)) {
            this.isGirl = null;
        } else if (UserPreferencesKeys.NOSE_ID.equals(key)) {
            this.noseId = 0;
        } else if (UserPreferencesKeys.EYE_ID.equals(key)) {
            this.eyeId = 0;
        } else if (UserPreferencesKeys.MOUTH_ID.equals(key)) {
            this.mouthId = 0;
        } else if (UserPreferencesKeys.EYEBROW_ID.equals(key)) {
            this.eyebrowId = 0;
        } else if (UserPreferencesKeys.TOP_CLOTHES_ID.equals(key)) {
            this.topClothesId = 0;
        } else if (UserPreferencesKeys.BOTTOM_CLOTHES_ID.equals(key)) {
            this.bottomClothesId = 0;
        } else if (UserPreferencesKeys.FOOTWEAR_ID.equals(key)) {
            this.footwearId = 0;
        }

        if (CollectionUtils.isNotEmpty(valueChangeListenerList)) {
            for (IUserPreferenceValueChangeListener valueChangeListener : valueChangeListenerList) {
                valueChangeListener.onRemove(key);
            }
        }
    }
}
