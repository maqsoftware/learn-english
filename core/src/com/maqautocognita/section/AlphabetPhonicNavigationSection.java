package com.maqautocognita.section;

import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.LessonWithReview;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.NinePatchScreenObject;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.screens.AbstractLetterScreen;
import com.maqautocognita.section.Alphabet.IAlphabetLessonChangeListener;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public final class AlphabetPhonicNavigationSection extends NavigationSection<LessonWithReview, IAlphabetLessonChangeListener> {

    /**
     * the width between each menu letter in the menu border
     */
    private static final int GAP_BETWEEN_MENU_LETTER = 10;
    private static final int MENU_BORDER_INDICATOR_LINE_HEIGHT = 20;
    private static final float PADDING_TOP_BOTTOM_IN_MENU_BORDER = 20;
    //private static final float LETTER_IN_MENU_POPUP_Y_POSITION = DOT_Y_POSITION - MENU_BORDER_INDICATOR_LINE_HEIGHT - PADDING_TOP_BOTTOM_IN_MENU_BORDER;
    private final boolean isRequiredToShowBothCase;
    private ILetterSelectListener letterSelectListener;
    private Activity selectedLetter;
    private LessonWithReview selectedLessonWithReview;
    private Activity selectedReview;
    private Activity selectedMasteryTest;
    private int selectedLessonIndex;
    private List<ScreenObject<String, ScreenObjectType>> menuLetterScreenObjectList;
    private TextureRegion menuBorderTextureRegion;
    private TextureScreenObject selectedDot;
    private ShapeRenderer menuBorderIndicatorLine;

    private List<Activity> masteryTestList;

    public AlphabetPhonicNavigationSection(AbstractLetterScreen abstractLetterScreen, IMenuScreenListener menuScreenListener,
                                           List<LessonWithReview> lessonList, boolean isRequiredToShowBothCase) {
        super(abstractLetterScreen, menuScreenListener, lessonList);

        this.isRequiredToShowBothCase = isRequiredToShowBothCase;

        //it is always show
        isShowing = true;
    }

    private void clearLetterSelection() {
        selectedLetter = null;
    }

    public void setLetterSelectListener(ILetterSelectListener letterSelectListener) {
        this.letterSelectListener = letterSelectListener;
    }

    public void setSelectedLetter(Activity letter) {
        if (null != letter && !letter.equals(selectedLetter)) {
            onMenuLetterChange(letter);
            initMenu();
        }
    }

    @Override
    public void setSelectedLesson(LessonWithReview lessonWithReview) {
        if (!lessonWithReview.equals(selectedLessonWithReview)) {

            deselectLesson();

            deselectMasteryTest();

            deselectReview();

            onLessonChange(lessonWithReview);

            initMenu();
        }
    }

    @Override
    public void render() {

        if (null != lessonList && lessonList.size() > 0) {
            batch.begin();

            if (null == navigationObjectList) {
                initMenu();
            }

            ScreenObjectUtils.draw(batch, navigationObjectList);

            ScreenObjectUtils.draw(batch, menuLetterScreenObjectList);

            AutoCognitaTextureRegion arrow = null;

            if (isLeftArrowRequireDisable()) {
                arrow = disabledLeftArrow;
                isEnableLeftArrow = false;

            } else {
                arrow = enabledLeftArrow;
                isEnableLeftArrow = true;

            }
            batch.draw(arrow, ScreenUtils.getNavigationBarStartXPosition(), ScreenUtils.getNavigationBarStartYPosition());

            if (isRightArrowRequireDisable()) {
                arrow = disabledRightArrow;
                isEnableRightArrow = false;
            } else {
                arrow = enabledRightArrow;
                isEnableRightArrow = true;
            }
            batch.draw(arrow, ScreenUtils.getNavigationRightArrowStartXPosition(), ScreenUtils.getNavigationBarStartYPosition());

            batch.end();

            if (IS_MENU_DOT_REQUIRED && null != selectedDot) {
                if (null == menuBorderIndicatorLine) {
                    menuBorderIndicatorLine = new ShapeRenderer();
                }
                menuBorderIndicatorLine.setProjectionMatrix(batch.getProjectionMatrix());
                menuBorderIndicatorLine.begin(ShapeRenderer.ShapeType.Filled);
                menuBorderIndicatorLine.setColor(ColorProperties.HIGHLIGHT);

                final float startXPosition = selectedDot.xPositionInScreen + selectedDot.width / 2;
                //the menuBorderIndicatorLine show across the math menu icons
                menuBorderIndicatorLine.rectLine(startXPosition, selectedDot.yPositionInScreen, startXPosition, selectedDot.yPositionInScreen - MENU_BORDER_INDICATOR_LINE_HEIGHT
                        , 5);

                menuBorderIndicatorLine.end();
            }
        }
    }

    private void initMenu() {

        initTexture();

        if (null == navigationObjectList) {
            navigationObjectList = new ArrayList<ScreenObject<Object, ScreenObjectType>>();
        } else {
            navigationObjectList.clear();
        }

        if (null == menuLetterScreenObjectList) {
            menuLetterScreenObjectList = new ArrayList<ScreenObject<String, ScreenObjectType>>();
        } else {
            menuLetterScreenObjectList.clear();
        }

        final float originalStartDotXPosition = ScreenUtils.getNavigationBarStartXPosition() + LEFT_ARROW_POSITION.width +
                (ScreenUtils.isLandscapeMode ? 20 : 5);
        float startDotXPosition = originalStartDotXPosition;

        if (null == masteryTestList) {
            masteryTestList = new ArrayList<Activity>();
        } else {
            masteryTestList.clear();
        }

        for (LessonWithReview lessonWithReview : lessonList) {

            if (CollectionUtils.isNotEmpty(lessonWithReview.getActivityList())) {

                float startDotYPosition = getDotYPosition();

                TextureScreenObject lessonDot = null;

                if (null != enabledDotTexture) {
                    lessonDot =
                            new TextureScreenObject(lessonWithReview, ScreenObjectType.MENU_LESSON_DOT,
                                    startDotXPosition, startDotYPosition, enabledDotTexture, selectedDotTexture, disabledDotTexture);
                    //drawing lesson dot
                    navigationObjectList.add(lessonDot);
                }

                if (lessonWithReview.isSelected()) {
                    selectedLessonIndex = lessonList.indexOf(lessonWithReview);
                    if (null == selectedLessonWithReview) {
                        //for the first init, in order to trigger the lesson change listeners
                        onLessonChange(lessonWithReview);
                    } else {
                        selectedLessonWithReview = lessonWithReview;
                    }
                    //get all letter which will bek teach in this lesson
                    String[] letters = new String[lessonWithReview.getLetterList().size()];
                    for (int i = 0; i < lessonWithReview.getLetterList().size(); i++) {
                        letters[i] = lessonWithReview.getLetterList().get(i);
                    }

                    //if no letter selected for this lessonWithReview
                    if (null == selectedLetter) {

                        Activity selectedActivity = null;
                        for (Activity activity : lessonWithReview.getActivityList()) {
                            if (activity.isSelected()) {
                                selectedActivity = activity;
                                break;
                            }
                        }

                        if (null == selectedActivity) {
                            selectedActivity = lessonWithReview.getActivityList().get(0);
                        }

                        //set the first letter as the default letter
                        onMenuLetterChange(selectedActivity);
                    }

                    float startMenuContentXPosition = startDotXPosition + 5;

                    float size[] = drawLessonMenuLettersWithBothCase(startMenuContentXPosition, isRequiredToShowBothCase, letters);

                    if (null != size) {
                        drawPopupMenuBorder(startDotXPosition, size[0], size[1]);
                    }

                    if (null != lessonDot) {
                        lessonDot.isHighlighted = true;

                        selectedDot = lessonDot;
                    }

                } else if (lessonWithReview.isPassed()) {
                    if (null != lessonDot) {
                        lessonDot.isHighlighted = false;
                    }
                } else {
                    if (null != lessonDot) {
                        lessonDot.isDisabled = true;
                        lessonDot.isTouchAllow = false;
                    }
                }
                if (null != lessonDot) {
                    startDotXPosition += (lessonDot.width + GAP_BETWEEN_DOT);
                }
            }
            //draw review dot and the popup menu if selected
            startDotXPosition = addActivityInMenu(lessonWithReview.getReviewList(), startDotXPosition, lessonWithReview, ScreenObjectType.MENU_REVIEW_DOT,
                    (UserPreferenceUtils.getInstance().isEnglish() ? "Review " : "Mazoezi ") + lessonWithReview.getSequence()
            );

            if (CollectionUtils.isNotEmpty(lessonWithReview.getMasterTestList())) {
                masteryTestList.addAll(lessonWithReview.getMasterTestList());
                startDotXPosition = addActivityInMenu(lessonWithReview.getMasterTestList(), startDotXPosition, lessonWithReview, ScreenObjectType.MENU_MASTERY_TEST_DOT,
                        UserPreferenceUtils.getInstance().isEnglish() ? "Mastery Tests " : "Mtihani "
                );
            }

        }

//        if (null != masteryTestList && masteryTestList.size() > 0) {
//
//            TextureScreenObject masteryTestDot = new TextureScreenObject(null, ScreenObjectType.MENU_MASTERY_TEST_DOT, startDotXPosition, getDotYPosition(),
//                    enabledDotTexture, selectedDotTexture, disabledDotTexture);
//            //drawing mastery test dot
//            navigationObjectList.add(masteryTestDot);
//
//            if (null != selectedMasteryTest) {
//                masteryTestDot.isHighlighted = true;
//                float size[] = drawPopupMenuContent(UserPreferenceUtils.getInstance().isEnglish() ? "Mastery Tests" : "Mtihani", startDotXPosition);
//                drawPopupMenuBorder(startDotXPosition, size[0], size[1]);
//                selectedDot = masteryTestDot;
//            } else if (isAllowAccessMasteryTest()) {
//                masteryTestDot.isHighlighted = false;
//            } else {
//                masteryTestDot.isDisabled = true;
//                masteryTestDot.isTouchAllow = false;
//            }
//        }

    }

    private boolean isLeftArrowRequireDisable() {

        return null != selectedLessonWithReview &&
                //and this is the first lesson && it is the first letter
                0 == selectedLessonIndex && 0 == getSelectedLetterIndex();
    }

    private boolean isRightArrowRequireDisable() {

        if (null != selectedLessonWithReview) {
            //lesson is selected
            return
                    //if the lesson is not passed yet
                    !selectedLessonWithReview.isPassed() ||
                            ((CollectionUtils.isEmpty(selectedLessonWithReview.getActivityList()) ||
                                    (getSelectedLetterIndex() ==
                                            selectedLessonWithReview.getActivityList().size() - 1)) &&
                                    //no review
                                    null == selectedLessonWithReview.getReviewList() &&
                                    //no next lesson
                                    selectedLessonIndex + 1 == lessonList.size() &&
                                    //no mastery test
                                    null == selectedLessonWithReview.getMasterTestList());

        } else if (null != selectedReview) {
            //review is selected
            return
                    !selectedReview.isPassed() || (
                            //no next lesson
                            selectedLessonIndex + 1 == lessonList.size()
                                    && //no mastery test
                                    null == masteryTestList
                    );
        } else {
            //mastery test is selected
            return
                    //no next mastery test
                    getIndexOfSelectedMasteryTest() + 1 == getNumberOfMasterTest() &&
                            //no next lesson
                            selectedLessonIndex + 1 == lessonList.size();
        }
    }

    private void onLessonChange(LessonWithReview lessonWithReview) {
        selectedLessonWithReview = lessonWithReview;
        selectedLessonWithReview.setSelected(true);
        if (null != lessonSelectListener) {
            lessonSelectListener.onLessonChanged(selectedLessonWithReview);
        }
    }

    private void onMenuLetterChange(Activity letter) {
        selectedLetter = letter;
        if (null != letterSelectListener) {
            letterSelectListener.onLetterSelected(selectedLetter);
        }
    }

    private float[] drawLessonMenuLettersWithBothCase(float startXPosition, boolean isBothCaseRequired, String... letters) {
        if (IS_MENU_DOT_REQUIRED) {
            final float beginingXPosition = startXPosition;

            float maxHeight = 0;

            for (int i = 0; i < letters.length; i++) {
                String letter = letters[i];

                String showLetter = letter;

                if (isBothCaseRequired) {
                    showLetter = letter.toUpperCase() + letter.toLowerCase();
                }

                if (null == menuLetterScreenObjectList) {
                    menuLetterScreenObjectList = new ArrayList<ScreenObject<String, ScreenObjectType>>();
                }


                String displayText = showLetter;

                TextScreenObject underlineTextScreenObject = null;
                if ("_th".equals(showLetter)) {
                    underlineTextScreenObject = new TextScreenObject(letter, ScreenObjectType.MENU_LETTER,
                            "_",
                            startXPosition, getLetterInMenuPopupYPosition(), TextFontSizeEnum.FONT_48);
                    menuLetterScreenObjectList.add(underlineTextScreenObject);
                    displayText = "th";
                }

                TextScreenObject letterTextScreenObject = new TextScreenObject(letter, ScreenObjectType.MENU_LETTER,
                        displayText,
                        startXPosition, getLetterInMenuPopupYPosition(), TextFontSizeEnum.FONT_48);


                if (letter.equalsIgnoreCase(selectedLetter.getLetter())) {
                    letterTextScreenObject.isHighlighted = true;
                    if (null != underlineTextScreenObject) {
                        underlineTextScreenObject.isHighlighted = true;
                    }
                }

                if (null != underlineTextScreenObject) {
                    underlineTextScreenObject.setTargetWidth(letterTextScreenObject.width);
                    letterTextScreenObject.sameGroupObject = underlineTextScreenObject;
                    underlineTextScreenObject.sameGroupObject = letterTextScreenObject;
                }


                if (letterTextScreenObject.height > maxHeight) {
                    maxHeight = letterTextScreenObject.height;
                }
                menuLetterScreenObjectList.add(letterTextScreenObject);


                startXPosition += letterTextScreenObject.width + GAP_BETWEEN_MENU_LETTER;
            }

            float menuLettersWidth = startXPosition - beginingXPosition;

            if (beginingXPosition + menuLettersWidth > ScreenUtils.getScreenWidth()) {
                startXPosition = beginingXPosition - menuLettersWidth + GAP_BETWEEN_MENU_LETTER;
                for (ScreenObject<String, ScreenObjectType> menuLetter : menuLetterScreenObjectList) {
                    startXPosition += GAP_BETWEEN_MENU_LETTER;
                    menuLetter.xPositionInScreen = startXPosition;
                    startXPosition += menuLetter.width;
                }
            }


            return new float[]{menuLettersWidth, maxHeight};
        }

        return null;

    }

    private void drawPopupMenuBorder(float startXPosition, float width, float height) {

        if (null == menuBorderTextureRegion) {
            generalIconTexture = AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_ICONS_FOR_NINE_PATCH);
            if (null != generalIconTexture) {
                menuBorderTextureRegion = new TextureRegion(generalIconTexture, 936, 190, 300, 80);
            }
        }

        if (null != menuBorderTextureRegion) {

            if (startXPosition + width > ScreenUtils.getScreenWidth()) {
                startXPosition = startXPosition - width + GAP_BETWEEN_MENU_LETTER;
            } else {
                startXPosition -= GAP_BETWEEN_MENU_LETTER;
            }


            //make sure the menuBorderTextureRegion is initialized
            navigationObjectList.add(
                    new NinePatchScreenObject(menuBorderTextureRegion, width + 20, height
                            //the margin top bottom between menu letter and the border
                            + PADDING_TOP_BOTTOM_IN_MENU_BORDER * 2, startXPosition, getLetterInMenuPopupYPosition() - height - PADDING_TOP_BOTTOM_IN_MENU_BORDER));
        }

    }

    private float addActivityInMenu(List<Activity> activityList, float startDotXPosition, LessonWithReview lessonWithReview,
                                    ScreenObjectType screenObjectType,
                                    String menuText) {
        if (CollectionUtils.isNotEmpty(activityList)) {

            Activity review = activityList.get(0);

            TextureScreenObject reviewDot = null;

            if (null != enabledDotTexture) {
                reviewDot = new TextureScreenObject(review,
                        screenObjectType, startDotXPosition,
                        getDotYPosition(), enabledDotTexture, selectedDotTexture, disabledDotTexture);
                //drawing review dot
                navigationObjectList.add(reviewDot);
            }

            //draw review dot
            if (isActivitySelected(activityList)) {

//                if (null == selectedReview) {
//                    //for the first init, in order to trigger the review change listeners
//                    onReviewChange(review);
//                }

                selectedLessonIndex = lessonList.indexOf(lessonWithReview);

                if (null != reviewDot) {
                    float size[] = drawPopupMenuContent(menuText, startDotXPosition);
                    drawPopupMenuBorder(startDotXPosition, size[0], size[1]);

                    reviewDot.isHighlighted = true;
                    selectedDot = reviewDot;
                }

            } else if (isActivityPassed(activityList)) {
                if (null != reviewDot) {
                    reviewDot.isHighlighted = false;
                }
            } else {
                if (null != reviewDot) {
                    reviewDot.isDisabled = true;
                    reviewDot.isTouchAllow = false;
                }
            }
            if (null != reviewDot) {
                startDotXPosition += (reviewDot.width + GAP_BETWEEN_DOT);
            }
        }

        return startDotXPosition;
    }

    private int getSelectedLetterIndex() {
        if (null != selectedLessonWithReview && CollectionUtils.isNotEmpty(selectedLessonWithReview.getActivityList())) {
            return selectedLessonWithReview.getActivityList().indexOf(selectedLetter);
        }
        return -1;
    }

    private int getNumberOfMasterTest() {
        if (null != selectedMasteryTest) {
            return selectedMasteryTest.getParent().getMasterTestList().size();
        }

        return 0;
    }

    private int getIndexOfSelectedMasteryTest() {
        if (null != selectedMasteryTest) {
            return selectedMasteryTest.getParent().getMasterTestList().indexOf(selectedMasteryTest);
        }
        return -1;
    }

    private float getLetterInMenuPopupYPosition() {
        return getDotYPosition() - MENU_BORDER_INDICATOR_LINE_HEIGHT - PADDING_TOP_BOTTOM_IN_MENU_BORDER;
    }

    private boolean isActivitySelected(List<Activity> activityList) {
        if (null != activityList) {
            for (Activity activity : activityList) {
                if (activity.isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private float[] drawPopupMenuContent(String displayText, float startXPosition) {


        TextScreenObject textScreenObject = new TextScreenObject(displayText, startXPosition, getLetterInMenuPopupYPosition(), TextFontSizeEnum.FONT_48);

        textScreenObject.isHighlighted = true;

        navigationObjectList.add(textScreenObject);

        if (startXPosition + textScreenObject.width > ScreenUtils.getScreenWidth()) {
            textScreenObject.xPositionInScreen = startXPosition - textScreenObject.width + GAP_BETWEEN_MENU_LETTER * 2;
        }

        return new float[]{textScreenObject.width, textScreenObject.height};
    }

    private boolean isActivityPassed(List<Activity> activityList) {
        if (null != activityList) {
            for (Activity review : activityList) {
                if (!review.isPassed()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void dispose() {
        super.dispose();
        menuBorderTextureRegion = null;
        if (null != menuBorderIndicatorLine) {
            menuBorderIndicatorLine.dispose();
            menuBorderIndicatorLine = null;
        }

        navigationObjectList = null;

        selectedLetter = null;

        selectedDot = null;
    }

    @Override
    public void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        if (!isTouchingMenuNavigator()) {

            ScreenObject<?, ScreenObjectType> touchedScreenObject = isMenuTouched(navigationObjectList, GAP_BETWEEN_DOT, screenX, screenY);

            if (null != touchedScreenObject) {

                if (ScreenObjectType.MENU_LESSON_DOT.equals(touchedScreenObject.objectType)) {
                    //select the lesson
                    setSelectedLesson((LessonWithReview) touchedScreenObject.id);
                } else if (ScreenObjectType.MENU_REVIEW_DOT.equals(touchedScreenObject.objectType)) {
                    //select the first review of the lesson
                    setSelectedReview((Activity) touchedScreenObject.id);
                } else if (ScreenObjectType.MENU_MASTERY_TEST_DOT.equals(touchedScreenObject.objectType)) {

                    //check if the last review is passed
                    if (isAllowAccessMasteryTest()) {

                        if (null != selectedReview) {
                            selectedReview.setSelected(false);
                            selectedReview = null;
                        }

                        if (null != selectedLessonWithReview) {
                            selectedLessonWithReview.setSelected(false);
                            selectedLessonWithReview = null;
                            clearLetterSelection();
                        }

                        setSelectedMasteryTest(masteryTestList.get(0));
                    }


                }
            } else {
                touchedScreenObject = isMenuTouched(menuLetterScreenObjectList, GAP_BETWEEN_MENU_LETTER, screenX, screenY);

                if (null != touchedScreenObject) {
                    //if the user is touching the menu letter
                    touchedScreenObject.isHighlighted = true;
                    if (null != touchedScreenObject.sameGroupObject) {
                        touchedScreenObject.sameGroupObject.isHighlighted = true;
                    }
                    setSelectedLetter(getTheFirstActivityByLetter((String) touchedScreenObject.id));
                }
            }
        }
    }

    @Override
    public void onSelectPreviousSection() {


        if (null != selectedReview) {
            //which mean it is review selected


            int selectedReviewIndex = selectedReview.getParent().getReviewList().indexOf(selectedReview);
            if (0 == selectedReviewIndex) {
                selectPreviousSection(selectedLessonIndex);
            } else {
                //select the previous review
                setSelectedReview(lessonList.get(selectedLessonIndex).getReviewList().get(selectedReviewIndex - 1));
            }
        } else if (null != selectedLessonWithReview) {
            //which mean it is lesson selected
            //select the previous review
            if (!selectPreviousLetter()) {
                selectPreviousSection();
            }
        } else if (null != selectedMasteryTest) {
            //which mean mastery test is selected
            int selectedMasteryIndex = getIndexOfSelectedMasteryTest();
            if (0 == selectedMasteryIndex) {
                selectPreviousSection();
            } else {
                //select the previous mastery test
                setSelectedMasteryTest(
                        getMasteryTestList().get(selectedMasteryIndex - 1));
            }

        }

    }

    @Override
    public void onSelectNextSection() {
        //if the current section is writing, jump to next lesson or letter
        if (null != selectedReview) {
            //which mean it is review selected

            LessonWithReview lesson = selectedReview.getParent();

            int selectedReviewIndex = lesson.getReviewList().indexOf(selectedReview);
            //check if there is next review
            if (selectedReviewIndex + 1 < lesson.getReviewList().size()) {
                //select next review
                setSelectedReview(lesson.getReviewList().get(selectedReviewIndex + 1));
            } else {
                selectNextLesson();
            }


        } else if (null != selectedLessonWithReview && CollectionUtils.isNotEmpty(selectedLessonWithReview.getActivityList())) {
            //which mean it is lesson selected
            //select the belongs review
            if (!selectNextLetter()) {

                List<Activity> nextReviewList = lessonList.get(selectedLessonIndex).getReviewList();

                if (null == nextReviewList) {
                    //select next lesson
                    selectNextLesson();
                } else {
                    //if the last letter is already selected in the selected lesson, jump to next review, select the first review
                    setSelectedReview(nextReviewList.get(0));
                }
            }
        } else {
            //which mean mastery test is selected
            int selectedMasteryIndex = getIndexOfSelectedMasteryTest();
            if (selectedMasteryIndex + 1 < getNumberOfMasterTest()) {
                //select next mastery test
                setSelectedMasteryTest(getMasteryTestList().get(selectedMasteryIndex + 1));
            } else if (selectedLessonIndex + 1 < lessonList.size()) {
                selectNextLesson();
            } else {
                //if no more mastery test, jump to home
                //make sure it is calling the render thread
                restartLessonAndGoToHomeScreen();
            }
        }

    }

    @Override
    public void onLessonComplete() {
        //if the current section is writing, jump to next lesson or letter
        if (null != selectedReview) {
            //which mean it is review selected

            LessonWithReview lesson = selectedReview.getParent();

            int selectedReviewIndex = lesson.getReviewList().indexOf(selectedReview);
            //check if there is next review
            if (selectedReviewIndex + 1 < lesson.getReviewList().size()) {
                //select next review
                setSelectedReview(lesson.getReviewList().get(selectedReviewIndex + 1));
            } else {
                selectNextLesson();
            }


        } else if (null != selectedLessonWithReview && CollectionUtils.isNotEmpty(selectedLessonWithReview.getActivityList())) {
            //which mean it is lesson selected
            //select the belongs review
            if (!selectNextLetter()) {

                List<Activity> nextReviewList = lessonList.get(selectedLessonIndex).getReviewList();

                if (null == nextReviewList) {
                    //select next lesson
                    selectNextLesson();
                } else {
                    //if the last letter is already selected in the selected lesson, jump to next review, select the first review
                    setSelectedReview(nextReviewList.get(0));
                }
            }
        } else {
            //which mean mastery test is selected
            int selectedMasteryIndex = getIndexOfSelectedMasteryTest();
            if (selectedMasteryIndex + 1 < getNumberOfMasterTest()) {
                //select next mastery test
                setSelectedMasteryTest(getMasteryTestList().get(selectedMasteryIndex + 1));
            } else {

                restartLessonAndGoToHomeScreen();

            }
        }

    }

    protected void restart() {
        //in order to reset everything after all test are done, so next time the user enter the screen again, it will be start from begining
        super.restart();
        deselectMasteryTest();
    }

    private void deselectMasteryTest() {
        if (null != selectedMasteryTest) {
            selectedMasteryTest.setSelected(false);
            selectedMasteryTest = null;
        }
    }

    private List<Activity> getMasteryTestList() {
        if (null != selectedMasteryTest) {
            return selectedMasteryTest.getParent().getMasterTestList();
        }
        return null;
    }

    private void selectPreviousSection() {
        LessonWithReview lesson = lessonList.get(selectedLessonIndex - 1);
        if (CollectionUtils.isNotEmpty(lesson.getReviewList())) {
            //get the last review
            setSelectedReview(lesson.getReviewList().get(lesson.getReviewList().size() - 1));
        } else if (CollectionUtils.isNotEmpty(lesson.getActivityList())) {
            selectPreviousSection(selectedLessonIndex - 1);
        } else if (CollectionUtils.isNotEmpty(lesson.getMasterTestList())) {
            //get the last master
            setSelectedMasteryTest(lesson.getMasterTestList().get(lesson.getMasterTestList().size() - 1));
        }
    }

    private void onReviewChange(Activity review) {
        selectedReview = review;
        selectedReview.setSelected(true);
        selectedLessonIndex = lessonList.indexOf(selectedReview.getParent());
        if (null != lessonSelectListener) {
            lessonSelectListener.onReviewChanged(selectedReview);
        }
    }

    private boolean isAllowAccessMasteryTest() {
        return masteryTestList != null && masteryTestList.size() > 0 && lessonList.get(lessonList.size() - 1).isReviewPassed();
    }

    private void setSelectedReview(Activity review) {
        if (!review.equals(selectedReview)) {

            deselectReview();

            deselectLesson();

            deselectMasteryTest();

            onReviewChange(review);

            initMenu();

        }
    }

    private void setSelectedMasteryTest(Activity masteryTest) {
        if (!masteryTest.equals(selectedMasteryTest)) {

            deselectReview();

            deselectLesson();

            deselectMasteryTest();

            onMasteryTestChange(masteryTest);

            initMenu();

        }
    }

    private void onMasteryTestChange(Activity masteryTest) {
        selectedMasteryTest = masteryTest;
        selectedMasteryTest.setSelected(true);
        //indicate to last lesson
        selectedLessonIndex = lessonList.size() - 1;
        if (null != lessonSelectListener) {
            lessonSelectListener.onMasteryTestSelected(selectedMasteryTest);
        }

    }

    private void deselectLesson() {
        if (null != selectedLessonWithReview) {
            selectedLessonWithReview.setSelected(false);
            selectedLessonWithReview = null;
            clearLetterSelection();
        }
    }

    private void deselectReview() {
        if (null != selectedReview) {
            selectedReview.setSelected(false);
            selectedReview = null;
        }
    }

    private Activity getTheFirstActivityByLetter(String letter) {
        for (Activity activity : selectedLessonWithReview.getActivityList()) {
            if (activity.getLetter().equalsIgnoreCase(letter)) {
                return activity;
            }
        }

        return null;
    }

    private boolean selectPreviousLetter() {
        if (null != selectedLessonWithReview) {
            int selectedLetterIndex = getSelectedLetterIndex();
            if (selectedLetterIndex - 1 >= 0) {
                selectedLetterIndex -= 1;
                //select the next letter
                setSelectedLetter(selectedLessonWithReview.getActivityList().get(selectedLetterIndex));
                return true;
            }
        }
        return false;
    }

    private boolean selectNextLetter() {
        if (null != selectedLessonWithReview && CollectionUtils.isNotEmpty(selectedLessonWithReview.getActivityList())) {
            int selectedLetterIndex = getSelectedLetterIndex();
            if (selectedLetterIndex + 1 < selectedLessonWithReview.getActivityList().size()) {
                selectedLetterIndex += 1;
                Activity selectedActivity = selectedLessonWithReview.getActivityList().get(selectedLetterIndex);
                //select the next letter
                setSelectedLetter(selectedActivity);

                return true;
            }
        }
        return false;
    }

    private void selectPreviousSection(int previousLessonIndex) {
        //select the previous lesson
        setSelectedLesson(lessonList.get(previousLessonIndex));
        int theLastLetterLessonIndex = selectedLessonWithReview.getActivityList().size() - 1;
        setSelectedLetter(selectedLessonWithReview.getActivityList().get(theLastLetterLessonIndex));
    }

    private void selectNextLesson() {
        //select the next lesson
        if (selectedLessonIndex + 1 < lessonList.size()) {

            LessonWithReview nextLesson = lessonList.get(selectedLessonIndex + 1);
            if (CollectionUtils.isNotEmpty(nextLesson.getMasterTestList())) {
                //which mean it is the last review , go to mastery test
                setSelectedMasteryTest(nextLesson.getMasterTestList().get(0));
            } else {
                //make sure there is still has next lesson
                setSelectedLesson(nextLesson);
            }
        }
    }

    public interface ILetterSelectListener {
        void onLetterSelected(Activity selectedLetter);
    }
}
