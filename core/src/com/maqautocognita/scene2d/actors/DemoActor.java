package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.listener.IDemoListener;
import com.maqautocognita.service.DemoService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Scaling;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class DemoActor extends Actor {

    private final int iconSize;
    private final IDemoListener demoListener;
    private Image homeIcon,
            imageDemoScreenAddSubtractBackground,
            imageDemoScreenAlphabetBackground,imageDemoScreenComprehension1Background,imageDemoScreenComprehension2Background,
            imageDemoScreenComprehension3Background, imageDemoScreenCountBackground,imageDemoScreenMultiplyBackground,
            imageDemoScreenPhonics1Background,imageDemoScreenPhonics2Background,imageDemoScreenPhonics3Background,imageDemoScreenPhonics4Background,
            imageDemoScreenWord1Background,imageDemoScreenWord2Background,imageDemoScreenWord3Background,imageDemoScreenWord4Background;


    private String currentScreen = "DemoScreenWord1";

    private BitmapFont recognizedTextBitmapFont;

    public DemoActor() {
        this.demoListener = DemoService.getInstance(null);
        iconSize = 200;
        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                boolean isChildrenTouch = false;
                if (isTouch(event.getStageX(), event.getStageY(),
                        homeIcon, imageDemoScreenAddSubtractBackground,
                        imageDemoScreenAlphabetBackground,imageDemoScreenComprehension1Background,imageDemoScreenComprehension2Background,
                        imageDemoScreenComprehension3Background, imageDemoScreenCountBackground,imageDemoScreenMultiplyBackground,
                        imageDemoScreenPhonics1Background,imageDemoScreenPhonics2Background,imageDemoScreenPhonics3Background,imageDemoScreenPhonics4Background,
                        imageDemoScreenWord1Background,imageDemoScreenWord2Background,imageDemoScreenWord3Background,imageDemoScreenWord4Background
                )) {
                    isChildrenTouch = true;
                }

            }

            private boolean isTouch(float touchX, float touchY, Actor... actors) {
                for (Actor actor : actors) {
                    return TouchUtils.isTouched(actor, (int) touchX, (int) touchY);
                }
                return false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible()) {
            super.draw(batch, parentAlpha);
            batch.end();

            batch.begin();
            addIcons();


        }
    }

    @Override
    public boolean remove() {
        return super.remove();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (null != homeIcon) {
            homeIcon.setVisible(visible);

        }

    }

    private void addIcons() {

        homeIcon = getIcon("home.png", 0, 0);

        imageDemoScreenAddSubtractBackground = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenAddSubtract.png" : "DemoScreenAddSubtract.png"))));
        imageDemoScreenAlphabetBackground = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenAlphabet.png" : "DemoScreenAlphabet.png"))));
        imageDemoScreenComprehension1Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenComprehension1.png" : "DemoScreenComprehension1.png"))));
        imageDemoScreenComprehension2Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenComprehension2.png" : "DemoScreenComprehension2.png"))));
        imageDemoScreenComprehension3Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenComprehension3.png" : "DemoScreenComprehension3.png"))));
        imageDemoScreenCountBackground = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenCount.png" : "DemoScreenCount.png"))));
        imageDemoScreenMultiplyBackground = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenMultiply.png" : "DemoScreenMultiply.png"))));
        imageDemoScreenPhonics1Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenPhonics1.png" : "DemoScreenPhonics1.png"))));
        imageDemoScreenPhonics2Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenPhonics2.png" : "DemoScreenPhonics2.png"))));
        imageDemoScreenPhonics3Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenPhonics3.png" : "DemoScreenPhonics3.png"))));
        imageDemoScreenPhonics4Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenPhonics4.png" : "DemoScreenPhonics4.png"))));
        imageDemoScreenWord1Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenWord1.png" : "DemoScreenWord1.png"))));
        imageDemoScreenWord2Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenWord2.png" : "DemoScreenWord2.png"))));
        imageDemoScreenWord3Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenWord3.png" : "DemoScreenWord3.png"))));
        imageDemoScreenWord4Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenWord4.png" : "DemoScreenWord4.png"))));


//        imageDemoScreenAddSubtractBackground.setSize(50,50);
//        imageDemoScreenAlphabetBackground.setSize(50,50);
//        imageDemoScreenComprehension1Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenComprehension2Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenComprehension3Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenCountBackground.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenMultiplyBackground.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenPhonics1Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenPhonics2Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenPhonics3Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenPhonics4Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenWord1Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenWord2Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenWord3Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
//        imageDemoScreenWord4Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());

        getStage().addActor(imageDemoScreenAddSubtractBackground);
        getStage().addActor(imageDemoScreenAlphabetBackground);
        getStage().addActor(imageDemoScreenComprehension1Background);
        getStage().addActor(imageDemoScreenComprehension2Background);
        getStage().addActor(imageDemoScreenComprehension3Background);
        getStage().addActor(imageDemoScreenCountBackground);
        getStage().addActor(imageDemoScreenMultiplyBackground);
        getStage().addActor(imageDemoScreenPhonics1Background);
        getStage().addActor(imageDemoScreenPhonics2Background);
        getStage().addActor(imageDemoScreenPhonics3Background);
        getStage().addActor(imageDemoScreenPhonics4Background);
        getStage().addActor(imageDemoScreenWord1Background);
        getStage().addActor(imageDemoScreenWord2Background);
        getStage().addActor(imageDemoScreenWord3Background);
        getStage().addActor(imageDemoScreenWord4Background);

        setBackgroundVisible(currentScreen);
        Table bottomControlsTable = new Table();

        float width = getWidth();

        if (ScreenUtils.isTablet) {
            width = width / 2;
        }

        bottomControlsTable.setSize(width, iconSize);

        Stack stack = new Stack();


        stack.add(homeIcon);
        bottomControlsTable.add(stack).expand().left();
        stack = new Stack();

        bottomControlsTable.add(stack).expand();

        if (ScreenUtils.isTablet) {
            bottomControlsTable.setX(getStage().getWidth() / 2);
        }

        getStage().addActor(bottomControlsTable);

        addIconListener();
    }


    public void setBackgroundVisible(String pCurrentScreen){
        currentScreen = pCurrentScreen;
        imageDemoScreenAddSubtractBackground.setVisible(false);
        imageDemoScreenAlphabetBackground.setVisible(false);
        imageDemoScreenComprehension1Background.setVisible(false);
        imageDemoScreenComprehension2Background.setVisible(false);
        imageDemoScreenComprehension3Background.setVisible(false);
        imageDemoScreenCountBackground.setVisible(false);
        imageDemoScreenMultiplyBackground.setVisible(false);
        imageDemoScreenPhonics1Background.setVisible(false);
        imageDemoScreenPhonics2Background.setVisible(false);
        imageDemoScreenPhonics3Background.setVisible(false);
        imageDemoScreenPhonics4Background.setVisible(false);
        imageDemoScreenWord1Background.setVisible(false);
        imageDemoScreenWord2Background.setVisible(false);
        imageDemoScreenWord3Background.setVisible(false);
        imageDemoScreenWord4Background.setVisible(false);

        if (currentScreen.equals("DemoScreenAddSubtract")) {
            imageDemoScreenAddSubtractBackground.setVisible(true);
        }else if (currentScreen.equals("DemoScreenAlphabet")) {
            imageDemoScreenAlphabetBackground.setVisible(true);
        }else if (currentScreen.equals("DemoScreenComprehension1")) {
            imageDemoScreenComprehension1Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenComprehension2")) {
            imageDemoScreenComprehension2Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenComprehension3")) {
            imageDemoScreenComprehension3Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenCount")) {
            imageDemoScreenCountBackground.setVisible(true);
        }else if (currentScreen.equals("DemoScreenMultiply")) {
            imageDemoScreenMultiplyBackground.setVisible(true);
        }else if (currentScreen.equals("DemoScreenPhonics1")) {
            imageDemoScreenPhonics1Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenPhonics2")) {
            imageDemoScreenPhonics2Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenPhonics3")) {
            imageDemoScreenPhonics3Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenPhonics4")) {
            imageDemoScreenPhonics4Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenWord1")) {
            imageDemoScreenWord1Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenWord2")) {
            imageDemoScreenWord2Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenWord3")) {
            imageDemoScreenWord3Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenWord4")) {
            imageDemoScreenWord4Background.setVisible(true);
        }


    }

    private void addIconListener() {
        homeIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onHomeClick();
            }
        });

        imageDemoScreenAddSubtractBackground.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });


        imageDemoScreenAlphabetBackground.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenComprehension1Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenComprehension2Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenComprehension3Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenCountBackground.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenMultiplyBackground.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenPhonics1Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenPhonics2Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenPhonics3Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenPhonics4Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenWord1Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenWord2Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenWord3Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenWord4Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
    }



    private Image getIcon(String iconImageName, float xPosition, float yPosition) {
        Image image = new Image(AssetManagerUtils.getTextureWithWait(Config.COMMON_IMAGE_XDPI_PATH + iconImageName));
        image.setSize(iconSize, iconSize);
        image.setScaling(Scaling.fill);
        image.setPosition(xPosition, yPosition);
        return image;
    }

}
