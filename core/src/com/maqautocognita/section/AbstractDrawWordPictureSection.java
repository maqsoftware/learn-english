package com.maqautocognita.section;

import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ImageProperties;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.SoundService;
import com.maqautocognita.utils.ApplicationUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.SizeUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractDrawWordPictureSection extends AbstractAutoCognitaSection {

    //private static final int BORDER_START_Y_POSITION = 180;

    /**
     * The max scale for the picture to increase when the picture is highlighted
     */
    private static final float MAX_SCALE = 1.2f;
    /**
     * The degree for the picture during rotation, the picture will be rotate after the picure size is reach the scale {@link #MAX_SCALE}
     */
    private static final float ROTATE_DEGREE = 10f;

    private static final float GAP_BETWEEN_WORD_PICTURE_ROW = 40;

    private final int maximumNumberOfImagePerRow;

    public AbstractDrawWordPictureSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(abstractAutoCognitaScreen, onHelpListener);
        maximumNumberOfImagePerRow = ScreenUtils.isLandscapeMode ? 5 : 3;
    }

    protected float getTotalHeightOfWordPictures(int numberOfPicture) {
        int numberOfRow = numberOfPicture / maximumNumberOfImagePerRow + 1;
        return numberOfRow * ImageProperties.BORDER_IMAGE_SIZE + (numberOfRow - 1) * GAP_BETWEEN_WORD_PICTURE_ROW;
    }

    protected List<ScreenObject<String, ScreenObjectType>> getWordPictureScreenObjectList(String[] picturePaths, String[] words, String unitCode) {

        List<ScreenObject<String, ScreenObjectType>> screenObjectList = new ArrayList<ScreenObject<String, ScreenObjectType>>();

        int totalWordPictureWidth = getMaximumPictureWidth(words.length);

        int numberOfRow = 1;

        if (totalWordPictureWidth > ScreenUtils.getScreenWidth()) {
            numberOfRow += words.length / maximumNumberOfImagePerRow;
            totalWordPictureWidth = getMaximumPictureWidth(maximumNumberOfImagePerRow);
        }

        final float originalXPosition = ScreenUtils.getXPositionForCenterObject(totalWordPictureWidth);

        //the start x position of the border
        float startXPosition = originalXPosition;

        float startYPosition = ScreenUtils.getBottomYPositionForCenterObject(ImageProperties.BORDER_IMAGE_SIZE * numberOfRow + (numberOfRow - 1) *
                GAP_BETWEEN_WORD_PICTURE_ROW, getHeightForPictureSection());

        startYPosition += (numberOfRow - 1) * ImageProperties.BORDER_IMAGE_SIZE + (numberOfRow - 1) * GAP_BETWEEN_WORD_PICTURE_ROW;

        int rowNumber = 0;

        //loop picture, suppose the number of picture is same as the number of word
        for (int i = 0; i < picturePaths.length; i++) {

            if (i >= maximumNumberOfImagePerRow && i / maximumNumberOfImagePerRow > rowNumber) {
                rowNumber++;
                int remainNumberOfImage = picturePaths.length - i;
                if (remainNumberOfImage <= maximumNumberOfImagePerRow) {
                    startXPosition = ScreenUtils.getXPositionForCenterObject(getMaximumPictureWidth(remainNumberOfImage));
                }

                startYPosition -= rowNumber * ImageProperties.BORDER_IMAGE_SIZE + rowNumber * GAP_BETWEEN_WORD_PICTURE_ROW;
            }

            String word = words[i];

            String audioFileName = getAudioFileName(word, i);

            totalWordPictureWidth += ImageProperties.BORDER_IMAGE_SIZE;

            TextureScreenObject picture = null;
            if (ApplicationUtils.isImageRequired(unitCode)) {
                float pictureSize[] = SizeUtils.getExpectSize(picturePaths[i], ImageProperties.WORD_IMAGE_WIDTH, ImageProperties.WORD_IMAGE_WIDTH);
                picture = new TextureScreenObject<String, ScreenObjectType>(word, ScreenObjectType.PICTURE,
                        startXPosition + (ImageProperties.BORDER_IMAGE_SIZE - pictureSize[0]) / 2,
                        //make sure the image is center vertically
                        startYPosition +
                                //make sure the image is at the top of the border
                                (ImageProperties.BORDER_IMAGE_SIZE - pictureSize[1]) / 2,
                        pictureSize[0],
                        pictureSize[1], picturePaths[i]);
                picture.audioFileName = audioFileName;

                afterPictureInitialized(picture, i);

                //add the word picture inside the border
                screenObjectList.add(picture);
            }

            //the word start x position within the border
            float startXPositionWithinBorder = (ImageProperties.BORDER_IMAGE_SIZE - LetterUtils.getTotalWidthOfWord(word, TextFontSizeEnum.FONT_72)) / 2;

            List<ScreenObject<String, ScreenObjectType>> wordScreenObjectList =
                    LetterUtils.getTextScreenObjectListSeparately(word, ScreenObjectType.WORD, startXPosition + startXPositionWithinBorder,
                            startYPosition,
                            word, TextFontSizeEnum.FONT_72);

            for (ScreenObject screenObject : wordScreenObjectList) {
                screenObject.audioFileName = audioFileName;
                if (null != picture) {
                    screenObject.sameGroupObject = picture;
                }
            }

            afterWordCreated(wordScreenObjectList);

            //add the word under border
            screenObjectList.addAll(wordScreenObjectList);

            startXPosition += ImageProperties.BORDER_IMAGE_SIZE + ImageProperties.BORDER_GAP_WIDTH;
        }

        return screenObjectList;
    }

    private int getMaximumPictureWidth(int numberOfPicture) {
        //caculate the total border width below the screen
        int totalWordPictureWidth = numberOfPicture * ImageProperties.BORDER_IMAGE_SIZE;

        //add the gap width between borders
        totalWordPictureWidth += (numberOfPicture - 1) * ImageProperties.BORDER_GAP_WIDTH;

        return totalWordPictureWidth;
    }

    protected abstract float getHeightForPictureSection();

    protected String getAudioFileName(String word, int index) {
        return SoundService.getInstance().getWordAudioFilename(word);
    }

    /**
     * It will be call when the picture screen object is initialized
     *
     * @param picture
     * @param index   the index of the border , for example if this is the second border , the index will be given 1
     */
    protected void afterPictureInitialized(TextureScreenObject picture, int index) {

    }

    /**
     * It will be call when the word screen object is initialized which is display under the picture
     *
     * @param wordList
     */
    protected void afterWordCreated(List<ScreenObject<String, ScreenObjectType>> wordList) {

    }

    protected <T> void render(List<? extends ScreenObject<T, ScreenObjectType>> screenObjectList) {
        if (CollectionUtils.isNotEmpty(screenObjectList)) {
            batch.begin();
            for (ScreenObject<?, ScreenObjectType> screenObject : screenObjectList) {
                if (ScreenObjectType.PICTURE.equals(screenObject.objectType) && !screenObject.isDragging()) {
                    Sprite sprite = ((TextureScreenObject) screenObject).getSprite();

                    if (null != sprite) {
                        //in order to make the animation to complete in 0.5 second (increase/decrease the image size)
                        float deltaTime = Gdx.graphics.getDeltaTime() / 0.5f;

                        if (screenObject.isHighlighted) {
                            if (sprite.getScaleX() < MAX_SCALE) {
                                sprite.setScale(sprite.getScaleX() + (MAX_SCALE - 1) * deltaTime);
                                sprite.setOriginCenter();
                            } else {
                                if (screenObject.isRotatingToRight) {
                                    screenObject.isRotatingToRight = !rotateToRight(sprite, deltaTime);
                                } else {
                                    screenObject.isRotatingToRight = rotateToLeft(sprite, deltaTime);
                                }
                            }
                        } else {
                            if (0 != sprite.getRotation()) {
                                //make sure the sprite is rollback to the original degree after animation
                                sprite.setRotation(0);
                            }
                            if (sprite.getScaleX() > 1) {
                                //rollback the image size to ratio 1 as the original size
                                sprite.setScale(sprite.getScaleX() - (MAX_SCALE - 1) * deltaTime);
                            }
                        }

                        sprite.draw(batch);
                    }
                } else {
                    ScreenObjectUtils.draw(batch, screenObject);
                }
            }
            batch.end();
        }

    }

    private boolean rotateToRight(Sprite sprite, float deltaTime) {
        if (sprite.getRotation() < ROTATE_DEGREE) {
            //rotate in anti-clockwise
            rotateSprite(sprite, deltaTime * ROTATE_DEGREE);
            return false;
        }
        return true;
    }

    private boolean rotateToLeft(Sprite sprite, float deltaTime) {
        if (sprite.getRotation() > -ROTATE_DEGREE) {
            //rotate in anti-clockwise
            rotateSprite(sprite, -deltaTime * ROTATE_DEGREE);
            return false;
        }
        return true;
    }

    private void rotateSprite(Sprite sprite, float degrees) {
        sprite.rotate(5 * degrees);
    }
}
