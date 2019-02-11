package com.maqautocognita.prototype.sentence;

/**
 * Created by kotarou on 18/5/2017.
 */
public class SentenceButton {

    public SentenceButtonEnum vButtonName; // ADD_BEFORE, ADD_AFTER_1, ADD_AFTER_2, DELETE_1, DELETE_2, DELETE_3, CHANGE_1, CHANGE_2, CHANGE_3
    public String vButtonType; // e.g. Object, Subject, Action
    public Boolean vEnabled = false;
    public String vP1 = "";
    public String vP2 = "";
    public String vNewState = "";
}
