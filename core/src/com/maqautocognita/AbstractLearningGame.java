package com.maqautocognita;

import com.maqautocognita.adapter.IAnalyticSpotService;
import com.maqautocognita.adapter.IAudioService;
import com.maqautocognita.adapter.IDeviceCameraService;
import com.maqautocognita.adapter.IDeviceService;
import com.maqautocognita.adapter.IHandWritingRecognizeService;
import com.maqautocognita.adapter.IOCR;
import com.maqautocognita.adapter.IShareService;
import com.maqautocognita.adapter.ISpeechRecognizeService;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractLearningGame extends AbstractGame {

    public static IHandWritingRecognizeService handWritingRecognizeService;
    public static ISpeechRecognizeService speechRecognizeService;
    public static IDeviceService deviceService;
    public static IDeviceCameraService deviceCameraService;
    public static IOCR ocrService;
    public static IShareService shareService;
    public static IAnalyticSpotService analyticSpotService;

    public AbstractLearningGame() {

    }

    protected void start(String lessonJdbcUrl,
                         String dictionaryJdbcUrl,
                         IAudioService audioService, IHandWritingRecognizeService handWritingRecognizeService,
                         ISpeechRecognizeService speechRecognizeService, IDeviceService deviceService,
                         IDeviceCameraService deviceCameraService, IOCR ocrService, IShareService shareService, IAnalyticSpotService analyticSpotService) {
        super.start(lessonJdbcUrl, dictionaryJdbcUrl, audioService);

        this.handWritingRecognizeService = handWritingRecognizeService;
        this.speechRecognizeService = speechRecognizeService;
        this.deviceService = deviceService;
        this.deviceCameraService = deviceCameraService;
        this.ocrService = ocrService;
        this.shareService = shareService;
        this.analyticSpotService = analyticSpotService;

        //it the user not yet to select the language for cheat sheet module
        if (StringUtils.isBlank(UserPreferenceUtils.getInstance().getCheatSheetSelectedLanguage())) {
            if (deviceService.isSpanishLocale()) {
                UserPreferenceUtils.getInstance().setCheatSheetSelectedLanguage(com.ibm.watson.developer_cloud.language_translator.v2.model.Language.SPANISH.toString());
            } else {
                UserPreferenceUtils.getInstance().setCheatSheetSelectedLanguage(com.ibm.watson.developer_cloud.language_translator.v2.model.Language.ENGLISH.toString());
            }
        }

        addStartTime();
    }

    private void addStartTime() {

    }


}
