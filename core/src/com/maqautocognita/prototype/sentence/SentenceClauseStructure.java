package com.maqautocognita.prototype.sentence;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kotarou on 15/5/2017.
 */
public class SentenceClauseStructure {
    public String vState="";
    public ArrayList<String> vPoSPattern;
    public String vClauseType="";
    public String vAddBefore="";
    public String vAddBeforeFrom="";
    public String vAddBeforeTo="";
    public String vAddBeforeNewState="";
    public String vAddAfter1="";
    public String vAddAfter1From="";
    public String vAddAfter1To="";
    public String vAddAfter1NewState="";
    public String vAddAfter2="";
    public String vAddAfter2From="";
    public String vAddAfter2To="";
    public String vAddAfter2NewState="";
    public String vDelete1="";
    public String vDelete1What="";
    public String vDelete1NewState="";
    public String vDelete2="";
    public String vDelete2What="";
    public String vDelete2NewState="";
    public String vChange1="";
    public String vChange1From="";
    public String vChange1To="";
    public String vChange1NewState="";
    public String vChange2="";
    public String vChange2From="";
    public String vChange2To="";
    public String vChange2NewState="";
    public String vChange3="";
    public String vChange3From="";
    public String vChange3To="";
    public String vChange3NewState="";

    public void setvPoSPattern(String pString){
        vPoSPattern = new ArrayList(Arrays.asList(pString.split(",")));
        String tempString;
        for (int i = 0; i < vPoSPattern.size();i++){
            tempString = vPoSPattern.get(i).trim();
            vPoSPattern.set(i, tempString);
        }

    }

    public void setvAddBefore(String pString){
        if (!pString.equals("")) {
            ArrayList<String> vSplit = new ArrayList(Arrays.asList(pString.split(",")));
            this.vAddBefore = pString;
            this.vAddBeforeFrom = vSplit.get(0).trim();
            this.vAddBeforeTo = vSplit.get(1).trim();
            this.vAddBeforeNewState = vSplit.get(2).trim();
        }
    }
    public void setvAddAfter1(String pString){
        if (!pString.equals("")) {
            ArrayList<String> vSplit = new ArrayList(Arrays.asList(pString.split(",")));
            this.vAddAfter1 = pString;
            this.vAddAfter1From = vSplit.get(0).trim();
            this.vAddAfter1To = vSplit.get(1).trim();
            this.vAddAfter1NewState = vSplit.get(2).trim();
        }
    }
    public void setvAddAfter2(String pString){
        if (!pString.equals("")) {
            ArrayList<String> vSplit = new ArrayList(Arrays.asList(pString.split(",")));
            this.vAddAfter2 = pString;
            this.vAddAfter2From = vSplit.get(0).trim();
            this.vAddAfter2To = vSplit.get(1).trim();
            this.vAddAfter2NewState = vSplit.get(2).trim();
        }
    }
    public void setvDelete1(String pString){
        if (!pString.equals("")) {
            ArrayList<String> vSplit = new ArrayList(Arrays.asList(pString.split(",")));
            this.vDelete1 = pString;
            this.vDelete1What = vSplit.get(0).trim();
            this.vDelete1NewState = vSplit.get(1).trim();
        }
    }
    public void setvDelete2(String pString){
        if (!pString.equals("")) {
            ArrayList<String> vSplit = new ArrayList(Arrays.asList(pString.split(",")));
            this.vDelete2 = pString;
            this.vDelete2What = vSplit.get(0).trim();
            this.vDelete2NewState = vSplit.get(1).trim();
        }
    }
    public void setvChange1(String pString){
        if (!pString.equals("")) {
            ArrayList<String> vSplit = new ArrayList(Arrays.asList(pString.split(",")));
            this.vChange1 = pString;
            this.vChange1From = vSplit.get(0).trim();;
            this.vChange1To = vSplit.get(1).trim();
            this.vChange1NewState = vSplit.get(2).trim();
        }
    }
    public void setvChange2(String pString){
        if (!pString.equals("")) {
            ArrayList<String> vSplit = new ArrayList(Arrays.asList(pString.split(",")));
            this.vChange2 = pString;
            this.vChange2From = vSplit.get(0).trim();
            this.vChange2To = vSplit.get(1).trim();
            this.vChange2NewState = vSplit.get(2).trim();
        }
    }
    public void setvChange3(String pString){
        if (!pString.equals("")) {
            ArrayList<String> vSplit = new ArrayList(Arrays.asList(pString.split(",")));
            this.vChange3 = pString;
            this.vChange3From = vSplit.get(0).trim();
            this.vChange3To = vSplit.get(1).trim();
            this.vChange3NewState = vSplit.get(2).trim();
        }
    }
}
