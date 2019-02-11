package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.bo.StoryLibraryBook;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class StoryBookActor extends Table {

    private static final float IMAGE_WIDTH_SCALE = 0.4f;
    private static final float TEXT_WIDTH_SCALE = 0.6f;
    private final StoryLibraryBook storyLibraryBook;
    private final AbstractAutoCognitaScreen screen;
    private boolean isDraw;

    public StoryBookActor(StoryLibraryBook storyLibraryBook, AbstractAutoCognitaScreen screen, float padding) {
        this.storyLibraryBook = storyLibraryBook;
        this.screen = screen;
        pad(padding);
    }

    @Override
    public void layout() {
        super.layout();

        if (!isDraw) {

            float tableWidth = getWidth() - getPadLeft() - getPadRight();

            float imageSize = tableWidth * IMAGE_WIDTH_SCALE;

            ImageWithBorder cover = new ImageWithBorder(new Texture(Config.IMAGE_FOLDER_NAME + storyLibraryBook.imageFileName));
            cover.setSize(imageSize, imageSize);
            add(cover).size(imageSize);

            float textLayoutSize = tableWidth * TEXT_WIDTH_SCALE;

            String displayText = null;

            boolean isCoverPage = 0 == storyLibraryBook.pageNumber;

            if (isCoverPage) {
                displayText = storyLibraryBook.title;
            } else {
                StringBuilder textBuilder = new StringBuilder();
                if (StringUtils.isNotBlank(storyLibraryBook.sentence1)) {
                    textBuilder.append(storyLibraryBook.sentence1 + "\n");
                }
                if (StringUtils.isNotBlank(storyLibraryBook.sentence2)) {
                    textBuilder.append(storyLibraryBook.sentence2 + "\n");
                }
                if (StringUtils.isNotBlank(storyLibraryBook.sentence3)) {
                    textBuilder.append(storyLibraryBook.sentence3 + "\n");
                }
                if (StringUtils.isNotBlank(storyLibraryBook.sentence4)) {
                    textBuilder.append(storyLibraryBook.sentence4 + "\n");
                }
                displayText = textBuilder.toString();
            }

            final Label label = new Label(displayText, getTipsLabelStyle(isCoverPage ? TextFontSizeEnum.FONT_108 : TextFontSizeEnum.FONT_72));
            label.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    label.getStyle().fontColor = ColorProperties.HIGHLIGHT;
                    screen.playSound(storyLibraryBook.audioFileName, new AbstractSoundPlayListener() {
                        @Override
                        public void onComplete() {
                            super.onComplete();
                            doComplete();
                        }

                        @Override
                        public void onStop() {
                            super.onStop();
                            doComplete();
                        }

                        private void doComplete() {
                            label.getStyle().fontColor = ColorProperties.TEXT;
                        }
                    });
                }
            });

            label.setAlignment(Align.left);
            label.setWrap(true);

            Cell cell = add(label).width(textLayoutSize).padLeft(50);

            if (isCoverPage) {
                cell.center();
            } else {
                cell.top();
            }

            isDraw = true;
        }
    }

    private Label.LabelStyle getTipsLabelStyle(TextFontSizeEnum textFontSizeEnum) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(textFontSizeEnum);
        labelStyle.fontColor = ColorProperties.TEXT;

        return labelStyle;
    }


}
