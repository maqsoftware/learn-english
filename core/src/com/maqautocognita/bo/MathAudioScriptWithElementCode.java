package com.maqautocognita.bo;

import com.maqautocognita.service.VOScriptService;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * It is mainly store the Math audio script name which are required to use in the math lesson
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class MathAudioScriptWithElementCode extends AbstractAudioFile {

    private static final String NUMBER_PATTERN = "<number>";
    private final String introductionScriptName;
    private final String instructionScriptName1;
    private final String instructionScriptName2;
    public List<String> instructionScriptAudioFileNameList;
    public List<String> instructionScript2AudioFileNameList;

    public MathAudioScriptWithElementCode(String introductionScriptName) {
        this(introductionScriptName, null, null);
    }

    public MathAudioScriptWithElementCode(String introductionScriptName, String instructionScriptName1,
                                          String instructionScriptName2) {
        this.introductionScriptName = introductionScriptName;
        this.instructionScriptName1 = instructionScriptName1;
        this.instructionScriptName2 = instructionScriptName2;

        loadVoScript();
    }

    public void loadVoScript() {
        if (StringUtils.isNotBlank(introductionScriptName)) {
            List<String> introductionAudioFileNameList = VOScriptService.getInstance().getListOfFileNameByVoScriptName(introductionScriptName);
            if (CollectionUtils.isEmpty(introductionAudioFileNameList)) {
                introductionAudioFileNameList = new ArrayList<String>();
                introductionAudioFileNameList.add(introductionScriptName);
            }
            setIntroductionAudioFilenameList(introductionAudioFileNameList);
            setHelpAudioFilenameList(getIntroductionAudioFilenameList());
        }

        instructionScriptAudioFileNameList = VOScriptService.getInstance().getListOfFileNameByVoScriptName(instructionScriptName1);
        instructionScript2AudioFileNameList = VOScriptService.getInstance().getListOfFileNameByVoScriptName(instructionScriptName2);
    }

    public MathAudioScriptWithElementCode(String introductionScriptName, String instructionScriptName1) {
        this(introductionScriptName, instructionScriptName1, null);
    }

    public List<String> getInstructionScriptAudioFileNameList(int... replaceNumber) {
        return getInstructionScriptAudioFileNameList(instructionScriptAudioFileNameList, replaceNumber);
    }

    private List<String> getInstructionScriptAudioFileNameList(List<String> audioFileNameList, int... replaceNumber) {

        if (CollectionUtils.isNotEmpty(audioFileNameList)) {
            List<String> newAudioFileNameList = new ArrayList<String>();

            int numberIndex = 0;

            for (int i = 0; i < audioFileNameList.size(); i++) {
                String audioFileName = audioFileNameList.get(i);

                if (NUMBER_PATTERN.equals(audioFileName) && numberIndex < replaceNumber.length) {

                    int number = replaceNumber[numberIndex];

                    newAudioFileNameList.addAll(getNumberAudioFileList(number));

                    numberIndex++;
                } else {
                    newAudioFileNameList.add(audioFileName);
                }
            }

            return newAudioFileNameList;
        }

        return null;
    }

    public List<String> getNumberAudioFileList(final int number) {
        if (number < 100) {
            return new ArrayList<String>() {{
                add("number_" + number);
            }};
        } else {
            return new ArrayList<String>() {{
                int hundredDigit = number / 100 % 100;
                add("number_" + (hundredDigit * 100) + "_");
                add("number_" + number % 100);
            }};
        }
    }

    public List<String> getInstruction2ScriptAudioFileNameList(int... replaceNumber) {
        return getInstructionScriptAudioFileNameList(instructionScript2AudioFileNameList, replaceNumber);
    }

    public String replaceNumberPattern(String audioFileName, int number) {
        if (NUMBER_PATTERN.equals(audioFileName)) {

            if (number >= 100) {
                //if the given number is 3 digits
                int result = (number / 100 % 100 * 100);

                audioFileName = getNumberAudioFileName(result);

                if (number == result) {
                    audioFileName = audioFileName + "_";
                }
            }
            if (number >= 10) {
                //if the given number is more or equals than 2 digits
                audioFileName = getNumberAudioFileName(number / 10 % 10 * 10);
            }
            if (number % 10 > 0) {
                audioFileName = getNumberAudioFileName(number % 10);
            }
        }

        return audioFileName;

    }

    private String getNumberAudioFileName(int number) {
        if (number > 0) {
            return "number_" + number;
        }

        return null;
    }

    private void addAudioFileName(List<String> audioFileNameList, int number) {
        String numberAudioFileName = getNumberAudioFileName(number);
        if (StringUtils.isNotBlank(numberAudioFileName)) {
            audioFileNameList.add(numberAudioFileName);
        }
    }
}
