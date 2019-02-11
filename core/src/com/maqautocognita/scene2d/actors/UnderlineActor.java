package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.SentenceWordTypeUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by siu-chun.chi on 5/5/2017.
 */

public class UnderlineActor extends Group {

    private final String word;

    private float lineHeight;

    private String containWord;

    public UnderlineActor(String word, String speech, float width, float height) {
        this.word = word;
        Image blockTemp = getImageBlock(speech);
        if (null != blockTemp) {
            float lineWidth = 0;
            do {
                Image block = getImageBlock(speech);
                block.setX(lineWidth);
                addActor(block);
                lineWidth += block.getWidth();
                lineHeight = block.getHeight();
            } while (lineWidth < width - blockTemp.getWidth());

            blockTemp.remove();

            setSize(lineWidth, height);
        }
    }

    private Image getImageBlock(String speech) {
        String imageName = SentenceWordTypeUtils.getImageNameByWordType(speech);
        if (StringUtils.isNotBlank(imageName)) {
            return new Image(AssetManagerUtils.getTextureWithWait(Config.SENTENCE_IMAGE_PATH + "/" + SentenceWordTypeUtils.getImageNameByWordType(speech) + "0.png"));
        }
        return null;
    }

    public boolean isCorrect() {
        if (StringUtils.isNotBlank(containWord)) {
            return word.trim().equalsIgnoreCase(containWord.trim());
        }

        return false;
    }

    public int isCorrectEnglishComprehension(){
        if(containWord == null){
            return 1;
        }

        if(word.trim().equalsIgnoreCase(containWord.trim()) == true){
            return 2;
        }

        return 0;
    }

    public boolean isContainWord() {
        return null != containWord;
    }

    public void setContainWord(String containWord) {
        this.containWord = containWord;
    }

    public float getLineHeight() {
        return lineHeight;
    }

    public String getWord() {
        return word;
    }
}
