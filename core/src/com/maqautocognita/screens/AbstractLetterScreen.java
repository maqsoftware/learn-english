package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.AbstractLearningGame;
import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.LessonWithReview;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.listener.IUserPreferenceValueChangeListener;
import com.maqautocognita.section.AbstractAutoCognitaSection;
import com.maqautocognita.section.Alphabet.IAlphabetLessonChangeListener;
import com.maqautocognita.section.AlphabetPhonicNavigationSection;
import com.maqautocognita.section.IActivityChangeListener;
import com.maqautocognita.section.MenuSection;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractLetterScreen extends AbstractProgressMapPopupScreen implements AbstractAutoCognitaSection.IOnHelpListener, IUserPreferenceValueChangeListener {

    protected List<AbstractAutoCognitaSection> autoCognitaSectionList;
    protected Activity selectedActivity;
    protected Activity selectedReview;
    protected Activity selectedMasteryTest;
    private MenuSection menuSection;
    private AlphabetPhonicNavigationSection alphabetNavigationSection;
    private LessonWithReview selectedLessonWithReview;


    public AbstractLetterScreen(AbstractGame game, IMenuScreenListener menuScreenListener, String... images) {
        super(game, menuScreenListener, ArrayUtils.join(new String[]{AssetManagerUtils.GENERAL_ICONS,
                AssetManagerUtils.GENERAL_ICONS_FOR_NINE_PATCH,
                AssetManagerUtils.ICONS,
                AssetManagerUtils.GENERAL_KEYS,
                AssetManagerUtils.SMALL_MICROPHONE,
                AssetManagerUtils.CORRECT_FRAME,
                AssetManagerUtils.WRONG_FRAME
        }, images));

        menuSection = new MenuSection(this);

        initAllLesson();


        alphabetNavigationSection = new AlphabetPhonicNavigationSection(this, menuScreenListener, getAllLessonWithReviewMasterList(), isRequiredToShowBothCaseLetter());

        autoCognitaSectionList = new ArrayList<AbstractAutoCognitaSection>();
        autoCognitaSectionList.add(menuSection);
        autoCognitaSectionList.add(alphabetNavigationSection);

        alphabetNavigationSection.setLetterSelectListener(new AlphabetPhonicNavigationSection.ILetterSelectListener() {
            @Override
            public void onLetterSelected(Activity activity) {

                setRequiredRender(false);

                showProgressMap(activity.getLessonCode());


                selectedActivity = activity;

                clearDebugMessage();

                //make sure stop all current playing audio
                if (null != AutoCognitaGame.audioService) {
                    AutoCognitaGame.audioService.stopMusic();
                }

                if (null != autoCognitaSectionList) {
                    setSelectedActivity(activity, false);
                }
                setRequiredRender(true);


            }
        });

        alphabetNavigationSection.setLessonSelectListener(new IAlphabetLessonChangeListener() {
            @Override
            public void onLessonChanged(LessonWithReview selectedLesson) {

                setRequiredRender(false);

                clearDebugMessage();

                if (null != AutoCognitaGame.audioService) {
                    AutoCognitaGame.audioService.stopMusic();
                }

                selectedActivity = null;
                selectedReview = null;
                selectedMasteryTest = null;
                selectedLessonWithReview = selectedLesson;

                setRequiredRender(true);

            }

            @Override
            public void onReviewChanged(Activity review) {

                setRequiredRender(false);

                if (null != AutoCognitaGame.audioService) {
                    AutoCognitaGame.audioService.stopMusic();
                }

                selectedActivity = null;
                selectedLessonWithReview = null;
                selectedMasteryTest = null;
                selectedReview = review;

                setSelectedActivity(selectedReview, false);

                setRequiredRender(true);
            }

            @Override
            public void onMasteryTestSelected(Activity masteryTest) {

                setRequiredRender(false);

                selectedActivity = null;
                selectedLessonWithReview = null;
                selectedReview = null;
                selectedMasteryTest = masteryTest;

                setSelectedActivity(selectedMasteryTest, true);

                setRequiredRender(true);
            }


        });

        menuSection.setMenuSelectListener(new MenuSection.IMenuSelectListener() {

            @Override
            public void onHelpSelected(MenuSection.MenuItemEnum menuItemEnum) {
                if (MenuSection.MenuItemEnum.HELP.equals(menuItemEnum)) {
                    if (menuSection.helpMenuItem.isHighLighted()) {
                        menuSection.helpMenuItem.setHighLighted(false);

                        if (null != autoCognitaSectionList) {
                            for (AbstractAutoCognitaSection autoCognitaSection : autoCognitaSectionList) {
                                autoCognitaSection.closeHelp();
                            }
                        }

                    } else {
                        menuSection.helpMenuItem.setHighLighted(true);
                        if (null != autoCognitaSectionList) {
                            for (AbstractAutoCognitaSection autoCognitaSection : autoCognitaSectionList) {
                                autoCognitaSection.onHelp();
                            }
                        }


                    }

                }

            }
        });

        UserPreferenceUtils.getInstance().addValueChangeListener(this);
    }

    protected abstract void initAllLesson();

    protected List<LessonWithReview> getAllLessonWithReviewMasterList() {
        List<LessonWithReview> lessonWithReviewList = getAllLessonList();
        LessonWithReview master = getAllMasteryTest();
        if (null != master) {
            lessonWithReviewList.add(master);
        }

        return lessonWithReviewList;
    }

    protected abstract boolean isRequiredToShowBothCaseLetter();


    private void setSelectedActivity(Activity review, boolean isMastery) {

        Gdx.app.log(getClass().getName(), "The user location = " + AbstractLearningGame.deviceService.getUserCurrentLocation());

        clearDebugMessage();

        for (AbstractAutoCognitaSection autoCognitaSection : autoCognitaSectionList) {
            if (autoCognitaSection instanceof IActivityChangeListener) {
                ((IActivityChangeListener) autoCognitaSection).reset();
                ((IActivityChangeListener) autoCognitaSection).setSelectedActivity(review, isMastery);
            }
        }
    }

    protected abstract List<LessonWithReview> getAllLessonList();

    protected abstract LessonWithReview getAllMasteryTest();


    @Override
    public void onHelpComplete() {
        menuSection.helpMenuItem.setHighLighted(false);
    }

    protected boolean isNormalActivitySelected() {
        return null != selectedActivity;
    }

    protected boolean isReviewSelected() {
        return null != selectedReview;
    }

    protected boolean isMasteryTestSelected() {
        return null != selectedMasteryTest;
    }

    //mainly used to listen the language change (english or swahili)
    @Override
    public void onValueChange(String key, Object value) {
        restart();
        initAllLesson();
        alphabetNavigationSection.setLessonList(getAllLessonWithReviewMasterList());
    }

    @Override
    public void restart() {
        selectedActivity = null;
        selectedLessonWithReview = null;
        selectedReview = null;
        selectedMasteryTest = null;
    }

    @Override
    public void showNextSection(int errorCount) {

        boolean isMasterTest = false;
        Activity currentActivity = selectedActivity;
        if (null == currentActivity) {
            if (null != selectedReview) {
                currentActivity = selectedReview;
            } else if (null != selectedMasteryTest) {
                currentActivity = selectedMasteryTest;
                isMasterTest = true;
            }
        }

        if (getLessonService().updateLessonProgress(errorCount, currentActivity.getUnitCode(),
                currentActivity.getLessonCode(),
                String.valueOf(currentActivity.getSequence()), currentActivity.getLetter(), currentActivity.getActivityCode().getCode(), UserPreferenceUtils.getInstance().getLanguage())) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    alphabetNavigationSection.onLessonComplete();
                }
            });
        } else {
            setSelectedActivity(currentActivity, isMasterTest);
        }

    }

    @Override
    protected void onHomeClick() {
        if (!isProgressMapShowing()) {
            super.onHomeClick();
        }
    }

    @Override
    protected List<AbstractAutoCognitaSection> getAutoCognitaSectionList() {
        return autoCognitaSectionList;
    }

    @Override
    public void doRender() {
        menuSection.render(true);
        alphabetNavigationSection.render(true);
    }

    @Override
    public void onRemove(String key) {

    }

    protected void onProgressMapPopupElementSelected(String selectedElementCode) {
        //when the topic is selected in the progress map
        clearTimer();

        for (LessonWithReview lessonWithReview : getAllLessonWithReviewMasterList()) {
            if (setSelectLetterByElementCode(lessonWithReview, selectedElementCode)) {
                hideProgressMap();
                break;
            }
        }
    }

    private boolean setSelectLetterByElementCode(LessonWithReview lessonWithReview, String elementCode) {
        if (CollectionUtils.isNotEmpty(lessonWithReview.getActivityList())) {
            for (Activity activity : lessonWithReview.getActivityList()) {
                if (activity.getLetter().equalsIgnoreCase(elementCode)) {
                    //make sure the progress map will not show
                    previousLessonCode = null;
                    alphabetNavigationSection.setSelectedLesson(lessonWithReview);
                    alphabetNavigationSection.setSelectedLetter(activity);
                    return true;
                }
            }
        }
        return false;
    }
}
