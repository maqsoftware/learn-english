package com.maqautocognita.screens;

import com.maqautocognita.Config;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class
LoadingScreen implements Screen {

    private Stage stage;
    private OrthographicCamera camera;
    private Image loadingImage;

    private ShapeRenderer shapeRenderer;

    private Texture loadingPage;

    @Override
    public void show() {
        if (null == camera) {
            camera = new CustomCamera();
            camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        if (null == stage) {
            stage = new Stage(new ScreenViewport(camera));
        }

        if (null == loadingImage) {
            if (null == loadingPage) {
                loadingPage = new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME +
                        (ScreenUtils.isTablet ? "loading_screen_smartphone.png" : "loading_screen_tablet.png")));
            }
            loadingImage = new Image(loadingPage);
            loadingImage.setScaling(Scaling.fit);
            loadingImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        stage.addActor(loadingImage);
    }


    @Override
    public void render(float delta) {
        if (null != stage) {
            if (null == shapeRenderer) {
                shapeRenderer = new ShapeRenderer();
            }
            shapeRenderer.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, stage.getWidth(), stage.getHeight());
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.end();

            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        StageUtils.dispose(stage);
        stage = null;
        camera = null;
        loadingImage = null;
        loadingPage.dispose();
        loadingPage = null;
    }
}
