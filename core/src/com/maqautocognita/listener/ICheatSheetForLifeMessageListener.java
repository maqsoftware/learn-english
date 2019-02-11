package com.maqautocognita.listener;


import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public interface ICheatSheetForLifeMessageListener {

    void onHomeClick();

    void onPlayAudioPath(String audioFilePath, AbstractSoundPlayListener soundPlayListListener);

    void onTranslateLanguageSelected(String text, Language fromLanguage, Language toLanguage, IAdvanceActionListener translationListener);

    void onTextToSpeech(String text, ISoundPlayListener soundPlayListener);

    void onCameraClick();

    void onLifeSkillBuiltInContentClick();

    void onVisualRecognitionSelected();

    void onTextRecognitionSelected();

    void onTakePictureClick();

    void onLoading();

    void afterLoading();

    void onReturnToPreviousScreen();
}
