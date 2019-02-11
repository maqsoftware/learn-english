package com.maqautocognita.prototype.sentence;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kotarou on 15/5/2017.
 */
public class SentenceStructure {
    public String vState="";
    public ArrayList<String > vClausePattern;
    public String vPunc = "";
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
    public String vDelete3="";
    public String vDelete3What="";
    public String vDelete3NewState="";

    public void setvClausePattern(String pString){
        vClausePattern = new ArrayList(Arrays.asList(pString.split(",")));
        String tempString;
        for (int i = 0; i < vClausePattern.size();i++){
            tempString = vClausePattern.get(i).trim();
            vClausePattern.set(i, tempString);
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

    public void setvDelete3(String pString){
        if (!pString.equals("")) {
            ArrayList<String> vSplit = new ArrayList(Arrays.asList(pString.split(",")));
            this.vDelete3 = pString;
            this.vDelete3What = vSplit.get(0).trim();
            this.vDelete3NewState = vSplit.get(1).trim();
        }
    }
}
