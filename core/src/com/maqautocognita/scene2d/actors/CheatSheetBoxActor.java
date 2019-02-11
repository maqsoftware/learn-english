package com.maqautocognita.scene2d.actors;

import com.maqautocognita.bo.CheatSheetBox;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.service.IBMWatonTranslateAndSpeechService;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CheatSheetBoxActor extends Actor {

    private static int BORDER_WIDTH = 3;
    private final CheatSheetBox cheatSheetBox;
    private final CheatSheetForLifeMessageActor cheatSheetForLifeMessageActor;
    private final List<CheatSheetBoxActor> cheatSheetBoxActorList;
    private ShapeRenderer shapeRenderer;
    private boolean highlighted;

    public CheatSheetBoxActor(CheatSheetBox cheatSheetBox, CheatSheetForLifeMessageActor cheatSheetForLifeMessageActor, List<CheatSheetBoxActor> cheatSheetBoxActorList) {

        this.cheatSheetBox = cheatSheetBox;
        this.cheatSheetForLifeMessageActor = cheatSheetForLifeMessageActor;
        this.cheatSheetBoxActorList = cheatSheetBoxActorList;

        addListener(new ActorGestureListener() {

            public void tap(InputEvent event, float x, float y, int count, int button) {
                setHighlighted(true);
            }
        });
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean isHighlighted) {
        if (isHighlighted && CollectionUtils.isNotEmpty(cheatSheetBoxActorList)) {
            for (CheatSheetBoxActor cheatSheetBoxActor : cheatSheetBoxActorList) {
                if (cheatSheetBoxActor.isHighlighted()) {
                    cheatSheetBoxActor.setHighlighted(false);
                    break;
                }
            }
            //make sure the previous speaking is stopped
            IBMWatonTranslateAndSpeechService.getInstance().stopPlayTextToSpeech();

            if (StringUtils.isBlank(cheatSheetBox.spanishTranslation)) {
                cheatSheetForLifeMessageActor.setTextAndRemoveTranslation(cheatSheetBox.text);
            } else {
                cheatSheetForLifeMessageActor.setTextWithTranslation(cheatSheetBox.text, cheatSheetBox.spanishTranslation,
                        cheatSheetBox.voiceFilename);
            }
        }
        this.highlighted = isHighlighted;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        Gdx.gl.glLineWidth(BORDER_WIDTH);

        if (null == shapeRenderer) {
            shapeRenderer = new ShapeRenderer();
        }
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(getX(), getParent().getY() + getY(), getWidth(), getHeight());
        shapeRenderer.setColor(highlighted ? ColorProperties.HIGHLIGHT : ColorProperties.BORDER);
        shapeRenderer.end();

        batch.begin();

    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
    }


}
