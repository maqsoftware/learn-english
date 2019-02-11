package com.maqautocognita.bo;

import com.maqautocognita.utils.IconPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class LessonWithReview extends Lesson {


    private List<Activity> activityList;

    private List<Activity> reviewList;

    private List<Activity> masterTestList;

    private List<String> letterList;

    /**
     * It is mainly used to store the dot in the navigation menu which is represent the review
     */
    private IconPosition reviewDotPosition;

    public LessonWithReview() {
        super();
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void addActivity(Activity activity) {
        if (null == activityList) {
            activityList = new ArrayList<Activity>();
        }

        activityList.add(activity);
    }

    public void addReviewActivity(Activity review) {

        if (null == reviewList) {
            reviewList = new ArrayList<Activity>();
        }

        reviewList.add(review);
    }

    public List<Activity> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Activity> reviewList) {
        this.reviewList = reviewList;
    }

    public List<Activity> getMasterTestList() {
        return masterTestList;
    }

    public void setMasterTestList(List<Activity> masterTestList) {
        this.masterTestList = masterTestList;
    }

    public void addMasterTest(Activity masterTest) {

        if (null == masterTestList) {
            masterTestList = new ArrayList<Activity>();
        }

        masterTestList.add(masterTest);
    }

    public boolean isReviewPassed() {
        if (null != reviewList) {
            for (Activity review : reviewList) {
                if (!review.isPassed()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isReviewSelected() {
        if (null != reviewList) {
            for (Activity review : reviewList) {
                if (review.isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public IconPosition getReviewDotPosition() {
        return reviewDotPosition;
    }

    public void setReviewDotPosition(IconPosition reviewDotPosition) {
        this.reviewDotPosition = reviewDotPosition;
    }

    public List<String> getLetterList() {
        return letterList;
    }

    public void setLetterList(List<String> letterList) {
        this.letterList = letterList;
    }
}
