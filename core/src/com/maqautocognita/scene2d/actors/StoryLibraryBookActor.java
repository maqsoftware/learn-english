package com.maqautocognita.scene2d.actors;

import com.maqautocognita.AutoCognitaStoryLibraryGame;
import com.maqautocognita.Config;
import com.maqautocognita.bo.StoryLibraryBook;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class StoryLibraryBookActor extends Table {

    private final StoryLibraryBook storyLibraryBook;
    private final AbstractAutoCognitaScreen screen;
    private final AutoCognitaStoryLibraryGame game;
    private boolean isDraw;

    public StoryLibraryBookActor(StoryLibraryBook storyLibraryBook, AbstractAutoCognitaScreen screen, AutoCognitaStoryLibraryGame game) {
        this.storyLibraryBook = storyLibraryBook;
        this.screen = screen;
        this.game = game;
    }

    @Override
    public void layout() {
        super.layout();

        if (!isDraw) {

            ImageWithBorder cover = new ImageWithBorder(new Texture(Config.IMAGE_FOLDER_NAME + storyLibraryBook.imageFileName));
            cover.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    game.showStoryLibraryBookScreen(storyLibraryBook.number);
                }
            });

            cover.setSize(getWidth(), getWidth());
            add(cover).size(getWidth());
            row();

            final Label label = new Label(storyLibraryBook.title, getTipsLabelStyle());
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
            label.setAlignment(Align.center);
            label.setWrap(true);

            add(label).width(getWidth()).bottom();

            isDraw = true;
        }
    }

    @Override
    protected void drawDebugBounds(ShapeRenderer shapes) {
        super.drawDebugBounds(shapes);
    }

    private Label.LabelStyle getTipsLabelStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(TextFontSizeEnum.FONT_72);
        labelStyle.fontColor = ColorProperties.TEXT;

        return labelStyle;
    }


}
