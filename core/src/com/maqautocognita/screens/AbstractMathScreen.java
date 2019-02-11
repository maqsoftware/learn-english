package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.MathLesson;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.AbstractAutoCognitaSection;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.section.Math.IMathSection;
import com.maqautocognita.section.MenuSection;
import com.maqautocognita.section.NavigationSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.AlphabetLessonService;
import com.maqautocognita.service.MathLessonService;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sc.chi on 14/5/16.
 */
public abstract class AbstractMathScreen extends AbstractProgressMapPopupScreen implements AbstractAutoCognitaSection.IOnHelpListener {


    protected List<IAutoCognitaSection> autoCognitaSectionList;
    private List<IMathSection> mathSectionList;
    private List<MathLesson> mathLessonList;
    private NavigationSection<MathLesson, NavigationSection.ILessonChangeListener> navigationSection;

    private MenuSection menuSection;

    private MathLesson selectedLesson;

    private String selectedMathElementCode;

    public AbstractMathScreen(AbstractGame game, IMenuScreenListener menuScreenListener, String... additionalImages) {
        super(game, menuScreenListener, additionalImages);


        mathLessonList = getLessonList();
        List<MathLesson> removeMathLessonList = new ArrayList<MathLesson>();
        if (null == mathSectionList) {
            mathSectionList = new ArrayList<IMathSection>(mathLessonList.size());
            for (MathLesson mathLesson : mathLessonList) {
                if (null == mathLesson.getAutoCognitaSection()) {
                    removeMathLessonList.add(mathLesson);
                } else {
                    mathSectionList.add((IMathSection) mathLesson.getAutoCognitaSection());
                }
            }
            mathLessonList.removeAll(removeMathLessonList);
        }

        navigationSection = new NavigationSection<MathLesson, NavigationSection.ILessonChangeListener>(this,
                menuScreenListener, mathLessonList);
        navigationSection.setLessonSelectListener(new NavigationSection.ILessonChangeListener<MathLesson>() {
            @Override
            public void onLessonChanged(MathLesson selectedLesson) {
                showProgressMap(String.valueOf(selectedLesson.getLessonCode()));
                AbstractMathScreen.this.selectedLesson = selectedLesson;
                clearDebugMessage();
            }
        });

        menuSection = new MenuSection(this);
        menuSection.setMenuSelectListener(new MenuSection.IMenuSelectListener() {

            @Override
            public void onHelpSelected(MenuSection.MenuItemEnum menuItemEnum) {
                if (MenuSection.MenuItemEnum.HELP.equals(menuItemEnum)) {
                    if (menuSection.helpMenuItem.isHighLighted()) {
                        menuSection.helpMenuItem.setHighLighted(false);

                        if (null != autoCognitaSectionList) {
                            for (IAutoCognitaSection autoCognitaSection : autoCognitaSectionList) {
                                if (isNotMenuAndNavigation(autoCognitaSection)) {
                                    autoCognitaSection.closeHelp();
                                }
                            }
                        }

                    } else {
                        menuSection.helpMenuItem.setHighLighted(true);
                        if (null != autoCognitaSectionList) {
                            for (IAutoCognitaSection autoCognitaSection : autoCognitaSectionList) {
                                if (isNotMenuAndNavigation(autoCognitaSection)) {
                                    autoCognitaSection.onHelp();
                                }
                            }
                        }


                    }

                }

            }
        });
    }

    protected abstract List<MathLesson> getLessonList();

    private boolean isNotMenuAndNavigation(IAutoCognitaSection autoCognitaSection) {
        return !(autoCognitaSection instanceof MenuSection) && !(autoCognitaSection instanceof NavigationSection);
    }

    @Override
    public AbstractLessonService getLessonService() {
        return MathLessonService.getInstance();
    }

    @Override
    public void restart() {
        selectedLesson = null;
    }

    @Override
    public void showNextSection(int numberOfFails) {
        AlphabetLessonService.getInstance().updateMathLessonProgress(numberOfFails, selectedMathElementCode);
        navigationSection.onLessonComplete();
    }

    @Override
    protected List<IAutoCognitaSection> getAutoCognitaSectionList() {
        if (null == autoCognitaSectionList) {
            autoCognitaSectionList = new ArrayList<IAutoCognitaSection>();
            autoCognitaSectionList.addAll(mathSectionList);
            autoCognitaSectionList.add(navigationSection);
            autoCognitaSectionList.add(menuSection);
        }
        return autoCognitaSectionList;
    }

    @Override
    protected String getAudioPath() {
        return (UserPreferenceUtils.getInstance().isEnglish() ? "english" : "swahili") + File.separator + "math";
    }

    @Override
    public void show() {
        super.show();

        if (CollectionUtils.isNotEmpty(mathSectionList)) {
            for (IMathSection mathSection : mathSectionList) {
                mathSection.reloadVoScript();
            }
        }

        navigationSection.onResize();
        menuSection.onResize();
    }

    @Override
    public void doRender() {
        if (null != selectedLesson && CollectionUtils.isNotEmpty(getAutoCognitaSectionList())) {
            IMathSection selectedMathLesson = null;
            for (IMathSection mathSection : mathSectionList) {
                if (selectedLesson.getAutoCognitaSection().equals(mathSection)) {
                    selectedMathLesson = mathSection;
                } else {
                    mathSection.render(false);
                }
            }
            //make sure the previous lesson is notified about no need render first
            if (null != selectedMathLesson) {
                selectedMathElementCode = selectedLesson.getElementCode();
                selectedMathLesson.render(true);
            }
        }

        navigationSection.render(true);
        menuSection.render(true);
    }

    protected void setScreenOrientation() {
        ScreenUtils.setLandscapeMode();
    }

    @Override
    public void onHelpComplete() {
        if (null != menuSection && null != menuSection.helpMenuItem) {
            menuSection.helpMenuItem.setHighLighted(false);
        }
    }

    @Override
    protected void onProgressMapPopupElementSelected(String selectedElementCode) {
        //when the topic is selected in the progress map
        clearTimer();

        for (MathLesson mathLesson : mathLessonList) {
            if (mathLesson.getElementCode().equals(selectedElementCode)) {
                navigationSection.setSelectedLesson(mathLesson);
                hideProgressMap();
                break;
            }
        }
    }

}
