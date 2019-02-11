package com.maqautocognita.prototype.sentence;

import com.maqautocognita.prototype.databases.Database;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kotarou on 15/5/2017.
 */
public class SentenceModule {
    static String sSPN = "I";
    static String sSArticle = "the";
    static String sSPPN = "my";
    static String sSAdjective = "blue";
    static String sSNoun = "boy";
    static String sAVerb = "jump";
    static String sAAdverb = "always";
    static String sAPreposition ="after";
    static String sOPN = "he";
    static String sOArticle = "the";
    static String sOPPN = "mine";
    static String sOAdjective = "blue";
    static String sONoun = "boy";
    public ArrayList<SentenceAdjective> arrSentenceAdjective = new ArrayList<SentenceAdjective>();
    public ArrayList<SentenceAdverb> arrSentenceAdverb = new ArrayList<SentenceAdverb>();
    public ArrayList<SentenceArticle> arrSentenceArticle = new ArrayList<SentenceArticle>();
    public ArrayList<SentenceClauseStructure> arrSentenceClauseStructure = new ArrayList<SentenceClauseStructure>();
    public ArrayList<SentenceConjunction> arrSentenceConjunction = new ArrayList<SentenceConjunction>();
    public ArrayList<SentenceNoun> arrSentenceNoun = new ArrayList<SentenceNoun>();
    public ArrayList<SentencePreposition> arrSentencePreposition = new ArrayList<SentencePreposition>();
    public ArrayList<SentencePronoun> arrSentencePronoun = new ArrayList<SentencePronoun>();
    public ArrayList<SentenceStructure> arrSentenceStructure = new ArrayList<SentenceStructure>();
    public ArrayList<SentenceVerb> arrSentenceVerb = new ArrayList<SentenceVerb>();
    public String currSubjectState = "s1";
    public String currActionState = "a1";
    public String currObjectState = "o1";
    public ArrayList<SentenceDisplayItem> selSPN;
    public ArrayList<SentenceDisplayItem> selSArticle;
    public ArrayList<SentenceDisplayItem> selSPPN;
    public ArrayList<SentenceDisplayItem> selSAdjective;
    public ArrayList<SentenceDisplayItem> selSNoun;
    //public ArrayList<SentenceDisplayItem> selAVerbStem;
    public ArrayList<SentenceDisplayItem> selAVerb;
    public ArrayList<SentenceDisplayItem> selAAdverb;
    public ArrayList<SentenceDisplayItem> selAPreposition;
    public ArrayList<SentenceDisplayItem> selOPN;
    public ArrayList<SentenceDisplayItem> selOArticle;
    //public ArrayList<SentenceDisplayItem> selOPPN;
    public ArrayList<SentenceDisplayItem> selOAdjective;
    public ArrayList<SentenceDisplayItem> selONoun;
    public SentenceTenseEnum currTense = SentenceTenseEnum.PRESENT;
    public String currSubjectPerson="";
    public String currObjectPerson="";
    // Sentence selection enable flag
    public Boolean enableSPN = true;
    public Boolean enableSArticle = false;
    public Boolean enableSPPN = false;
    public Boolean enableSAdjective = false;
    public Boolean enableSNoun = false;
    public Boolean enableAVerb = true;
    public Boolean enableAAdverb = false;
    public Boolean enableAPreposition = false;
    public Boolean enableOPN = false;
    public Boolean enableOArticle = false;
    public Boolean enableOPPN = false;
    public Boolean enableOAdjective = false;
    public Boolean enableONoun = false;
    public Boolean enableVerbConjugation = false;
/*    // Button option enalbe flag
    public Boolean enableSAddBefore = false;
    public Boolean enableSAddAfter1 = false;
    public Boolean enableSAddAfter2 = false;
    public Boolean enableSDelete1 = false;
    public Boolean enableSDelete2 = false;
    public Boolean enableSChange1 = false;
    public Boolean enableSChange2 = false;
    public Boolean enableSChange3 = false;
    public Boolean enableAAddBefore = false;
    public Boolean enableAAddAfter1 = false;
    public Boolean enableAAddAfter2 = false;
    public Boolean enableADelete1 = false;
    public Boolean enableADelete2 = false;
    public Boolean enableAChange1 = false;
    public Boolean enableAChange2 = false;
    public Boolean enableAChange3 = false;
    public Boolean enableOAddBefore = false;
    public Boolean enableOAddAfter1 = false;
    public Boolean enableOAddAfter2 = false;
    public Boolean enableODelete1 = false;
    public Boolean enableODelete2 = false;
    public Boolean enableOChange1 = false;
    public Boolean enableOChange2 = false;
    public Boolean enableOChange3 = false;*/
    public String currSentenceStructure= "";
    public Boolean currSubjectEnable = true;
    public Boolean currActionEnable = true;
    public Boolean currObjectEnable = false;
    public Boolean enableSSingle = true;
    public Boolean enableSPlural = false;
    public Boolean enableOSingle = false;
    public Boolean enableOPlural = false;
    public ArrayList<SentenceButton> vSentenceButton = new ArrayList<SentenceButton>();
    public ArrayList<SentenceButton> vStructButton = new ArrayList<SentenceButton>();
    private SentenceLogic vSentenceLogic;
    private SentenceDisplayItem currSPN = new SentenceDisplayItem(sSPN, sSPN);
    private SentenceDisplayItem currSArticle = new SentenceDisplayItem(sSArticle,sSArticle);
    private SentenceDisplayItem currSPPN = new SentenceDisplayItem(sSPPN, sSPPN);
    private SentenceDisplayItem currSAdjective = new SentenceDisplayItem(sSAdjective, sSAdjective);
    private SentenceDisplayItem currSNoun = new SentenceDisplayItem(sSNoun, sSNoun);
    //public SentenceDisplayItem currAVerbStem;
    private SentenceDisplayItem currAVerb = new SentenceDisplayItem(sAVerb , sAVerb );
    private SentenceDisplayItem currAAdverb = new SentenceDisplayItem(sAAdverb, sAAdverb);
    private SentenceDisplayItem currAPreposition = new SentenceDisplayItem(sAPreposition, sAPreposition);
    private SentenceDisplayItem currOPN = new SentenceDisplayItem(sOPN,sOPN);
    private SentenceDisplayItem currOArticle = new SentenceDisplayItem(sOArticle, sOArticle);
    private SentenceDisplayItem currOPPN = new SentenceDisplayItem(sOPPN, sOPPN);
    private SentenceDisplayItem currOAdjective = new SentenceDisplayItem(sOAdjective, sOAdjective);
    private SentenceDisplayItem currONoun = new SentenceDisplayItem(sONoun, sONoun);

    public Boolean selectedPastTenseButton = false;
    public Boolean selectedPresentTenseButton = true;
    public Boolean selectedFutureTenseButton = false;
    public Boolean selectedPastContinuousTenseButton = false;
    public Boolean selectedPresentContinuousTenseButton = false;
    public Boolean selectedFutureContinuousTenseButton = false;

    public Boolean selectedS1SButton = false;
    public Boolean selectedS2SButton = false;
    public Boolean selectedS3SButton = false;
    public Boolean selectedS1PButton = false;
    public Boolean selectedS2PButton = false;
    public Boolean selectedS3PButton = false;

    public Boolean selectedO1SButton = false;
    public Boolean selectedO2SButton = false;
    public Boolean selectedO3SButton = false;
    public Boolean selectedO1PButton = false;
    public Boolean selectedO2PButton = false;
    public Boolean selectedO3PButton = false;

    public SentenceModule(Database pDatabase){
        vSentenceLogic = new SentenceLogic(pDatabase);
        arrSentenceAdjective = vSentenceLogic.getSentenceAdjective();
        arrSentenceAdverb = vSentenceLogic.getSentenceAdverb();
        arrSentenceArticle = vSentenceLogic.getSentenceArticle();
        arrSentenceClauseStructure = vSentenceLogic.getSentenceClauseStructure();
        arrSentenceConjunction = vSentenceLogic.getSentenceConjunction();
        arrSentenceNoun = vSentenceLogic.getSentenceNoun();
        arrSentencePreposition = vSentenceLogic.getSentencePreposition();
        arrSentencePronoun = vSentenceLogic.getSentencePronoun();
        arrSentenceStructure = vSentenceLogic.getSentenceStructure();
        arrSentenceVerb = vSentenceLogic.getSentenceVerb();
        setSelSPN();
        setSelOPN();
        setSelSPPN();
        setSelSAdjective();
        setSelSNoun();
        setSelONoun();
        setSelOAdjective();
        setSelOPPN();
        setSelAAdverb();
        setSelAPreposition();
        this.changeStructure("statement1");
        this.changePattern("subject", "s1");
        this.changePattern("action","a1");
        this.setCurrPronoun("I","subject");
        this.changeSinglePlural("subject", "1S");
        selectedPresentTenseButton = true;

    }

    public void initSetting(){
        setSelSPN();
        setSelOPN();
        setSelSPPN();
        setSelSAdjective();
        setSelSNoun();
        setSelONoun();
        setSelOAdjective();
        setSelOPPN();
        setSelAAdverb();
        setSelAPreposition();
        this.changeStructure("statement1");
        this.changePattern("subject", "s1");
        this.changePattern("action","a1");
        this.setCurrPronoun("I","subject");
        this.changeSinglePlural("subject", "1S");
        selectedPresentTenseButton = true;
    }

    private void setSelSPN() {
        ArrayList<SentenceDisplayItem> retSPN = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentencePronoun.size(); i++) {
            retSPN.add(new SentenceDisplayItem(arrSentencePronoun.get(i).vStem, arrSentencePronoun.get(i).vSubjectPN));
        }
        selSPN = retSPN;
    }

    private void setSelOPN() {
        ArrayList<SentenceDisplayItem> retOPN = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentencePronoun.size(); i++) {
            retOPN.add(new SentenceDisplayItem(arrSentencePronoun.get(i).vStem, arrSentencePronoun.get(i).vObjectPN));
        }
        selOPN = retOPN;
    }

    private void setSelSPPN() {
        ArrayList<SentenceDisplayItem> retSPPN = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentencePronoun.size(); i++) {
            retSPPN.add(new SentenceDisplayItem(arrSentencePronoun.get(i).vStem, arrSentencePronoun.get(i).vSubjectPPN));
        }
        selSPPN = retSPPN;
    }

    private void setSelSAdjective() {
        ArrayList<SentenceDisplayItem> retSAdjective = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentenceAdjective.size(); i++) {
            retSAdjective.add(new SentenceDisplayItem(arrSentenceAdjective.get(i).vStem, arrSentenceAdjective.get(i).vStem));
        }
        selSAdjective = retSAdjective;
    }

    private void setSelSNoun() {
        ArrayList<SentenceDisplayItem> retSNoun = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentenceNoun.size(); i++) {
            if (enableSSingle) {
                retSNoun.add(new SentenceDisplayItem(arrSentenceNoun.get(i).vStem, arrSentenceNoun.get(i).vNoun3S));
            } else {
                retSNoun.add(new SentenceDisplayItem(arrSentenceNoun.get(i).vStem, arrSentenceNoun.get(i).vNoun3P));
            }
        }
        selSNoun = retSNoun;
    }

    private void setSelONoun() {
        ArrayList<SentenceDisplayItem> retSNoun = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentenceNoun.size(); i++) {
            if (enableSSingle) {
                retSNoun.add(new SentenceDisplayItem(arrSentenceNoun.get(i).vStem, arrSentenceNoun.get(i).vNoun3S));
            } else {
                retSNoun.add(new SentenceDisplayItem(arrSentenceNoun.get(i).vStem, arrSentenceNoun.get(i).vNoun3P));
            }
        }
        selONoun = retSNoun;
    }

    private void setSelOPPN() {
        ArrayList<SentenceDisplayItem> retOPPN = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentencePronoun.size(); i++) {
            retOPPN.add(new SentenceDisplayItem(arrSentencePronoun.get(i).vStem, arrSentencePronoun.get(i).vSubjectPPN));
        }
        selSPPN = retOPPN;
    }

    private void setSelOAdjective() {
        ArrayList<SentenceDisplayItem> retSAdjective = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentenceAdjective.size(); i++) {
            retSAdjective.add(new SentenceDisplayItem(arrSentenceAdjective.get(i).vStem, arrSentenceAdjective.get(i).vStem));
        }
        selOAdjective = retSAdjective;
    }

    private void setSelAAdverb() {
        ArrayList<SentenceDisplayItem> retAAdverb = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentenceAdverb.size(); i++) {
            retAAdverb.add(new SentenceDisplayItem(arrSentenceAdverb.get(i).vStem, arrSentenceAdverb.get(i).vStem));
        }
        selAAdverb = retAAdverb;
    }

    private void setSelAPreposition() {
        ArrayList<SentenceDisplayItem> retAPreposition = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentencePreposition.size(); i++) {
            retAPreposition.add(new SentenceDisplayItem(arrSentencePreposition.get(i).vStem, arrSentencePreposition.get(i).vStem));
        }
        selAPreposition = retAPreposition;
    }

    public void changeStructure(String pState) {
        currSubjectEnable = false;
        currActionEnable = false;
        currObjectEnable = false;
        vStructButton = new ArrayList<SentenceButton>();
        for (int i = 0; i < arrSentenceStructure.size(); i++) {
            if (pState.equals(arrSentenceStructure.get(i).vState)) {
                for (int j = 0; j < arrSentenceStructure.get(i).vClausePattern.size(); j++) {
                    if (arrSentenceStructure.get(i).vClausePattern.get(j).equals("subject")) {
                        currSubjectEnable = true;
                    }
                    if (arrSentenceStructure.get(i).vClausePattern.get(j).equals("action")) {
                        currActionEnable = true;
                    }
                    if (arrSentenceStructure.get(i).vClausePattern.get(j).equals("object")) {
                        currObjectEnable = true;
                        enableObject();
                    }
                }
                currSentenceStructure = pState;
            }
            if (!arrSentenceStructure.get(i).vAddAfter1.equals("")) {
                SentenceButton tmpButton = new SentenceButton();
                tmpButton.vButtonName = SentenceButtonEnum.ADD_AFTER_1;
                tmpButton.vButtonType = "SentenceStructure";
                tmpButton.vEnabled = true;
                ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceStructure.get(i).vAddAfter1.split(",")));
                tmpButton.vP1 = vSplit.get(0);
                tmpButton.vP2 = vSplit.get(1);
                tmpButton.vNewState = vSplit.get(2);
                vStructButton.add(tmpButton);
            }
            if (!arrSentenceStructure.get(i).vAddAfter2.equals("")) {
                SentenceButton tmpButton = new SentenceButton();
                tmpButton.vButtonName = SentenceButtonEnum.ADD_AFTER_2;
                tmpButton.vButtonType = "SentenceStructure";
                tmpButton.vEnabled = true;
                ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceStructure.get(i).vAddAfter2.split(",")));
                tmpButton.vP1 = vSplit.get(0);
                tmpButton.vP2 = vSplit.get(1);
                tmpButton.vNewState = vSplit.get(2);
                vStructButton.add(tmpButton);
            }
            if (!arrSentenceStructure.get(i).vDelete1.equals("")) {
                SentenceButton tmpButton = new SentenceButton();
                tmpButton.vButtonName = SentenceButtonEnum.DELETE_1;
                tmpButton.vButtonType = "SentenceStructure";
                tmpButton.vEnabled = true;
                ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceStructure.get(i).vDelete1.split(",")));
                tmpButton.vP1 = vSplit.get(0);
                tmpButton.vP2 = "";
                tmpButton.vNewState = vSplit.get(1);
                vStructButton.add(tmpButton);
            }
            if (!arrSentenceStructure.get(i).vDelete2.equals("")) {
                SentenceButton tmpButton = new SentenceButton();
                tmpButton.vButtonName = SentenceButtonEnum.DELETE_2;
                tmpButton.vButtonType = "SentenceStructure";
                tmpButton.vEnabled = true;
                ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceStructure.get(i).vDelete2.split(",")));
                tmpButton.vP1 = vSplit.get(0);
                tmpButton.vP2 = "";
                tmpButton.vNewState = vSplit.get(1);
                vStructButton.add(tmpButton);
            }
        }

    }

    public void changePattern(String pClauseType, String SubjectState){
        String tVal = "";
        if (pClauseType.equals("subject")) {
            enableSPN = false;
            enableSArticle = false;
            enableSPPN = false;
            enableSAdjective = false;
            enableSNoun = false;
        }
        if (pClauseType.equals("action")) {
            enableAVerb = false;
            enableAAdverb = false;
            enableAPreposition = false;
        }
        if (pClauseType.equals("object")) {
            enableOPN = false;
            enableOArticle = false;
            enableOPPN = false;
            enableOAdjective = false;
            enableONoun = false;
        }
        //vSentenceButton = new ArrayList<SentenceButton>();
        ArrayList<SentenceButton> vNewSentenceButton = new ArrayList<SentenceButton>();

        for (int i = 0; i < vSentenceButton.size();i++){
            if (!vSentenceButton.get(i).vButtonType.equals(pClauseType)){
                vNewSentenceButton.add(vSentenceButton.get(i));
            }
        }

        for (int i = 0; i < arrSentenceClauseStructure.size(); i++){
            if(SubjectState.equals(arrSentenceClauseStructure.get(i).vState)){
                for(int j = 0; j<arrSentenceClauseStructure.get(i).vPoSPattern.size(); j++){
                    tVal = arrSentenceClauseStructure.get(i).vPoSPattern.get(j);
                    if (pClauseType.equals("subject")) {
                        if (tVal.equals("pn")) {
                            enableSPN = true;
                        }
                        if (tVal.equals("art")) {
                            enableSArticle = true;
                        }
                        if (tVal.equals("ppn")) {
                            enableSPPN = true;
                        }
                        if (tVal.equals("adj")) {
                            enableSAdjective = true;
                        }
                        if (tVal.equals("n")) {
                            enableSNoun = true;
                        }
                    }
                    if (pClauseType.equals("action")) {
                        if (tVal.equals("v")) {
                            enableAVerb = true;
                        }
                        if (tVal.equals("adv")) {
                            enableAAdverb = true;
                        }
                        if (tVal.equals("prep")) {
                            enableAPreposition = true;
                        }
                    }
                    if (pClauseType.equals("object")) {
                        if (tVal.equals("pn")) {
                            enableOPN = true;
                        }
                        if (tVal.equals("art")) {
                            enableOArticle = true;
                        }
                        if (tVal.equals("ppn")) {
                            enableOPPN = true;
                        }
                        if (tVal.equals("adj")) {
                            enableOAdjective = true;
                        }
                        if (tVal.equals("n")) {
                            enableONoun = true;
                        }
                    }
                }
                if (!arrSentenceClauseStructure.get(i).vAddBefore.equals("")){
                    SentenceButton tmpButton = new SentenceButton();
                    tmpButton.vButtonName = SentenceButtonEnum.ADD_BEFORE;
                    tmpButton.vButtonType = arrSentenceClauseStructure.get(i).vClauseType;
                    tmpButton.vEnabled = true;
                    ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceClauseStructure.get(i).vAddBefore.split(",")));
                    tmpButton.vP1 = arrSentenceClauseStructure.get(i).vAddBeforeFrom; //vSplit.get(0);
                    tmpButton.vP2 = arrSentenceClauseStructure.get(i).vAddBeforeTo; //vSplit.get(1);
                    tmpButton.vNewState = arrSentenceClauseStructure.get(i).vAddBeforeNewState; //vSplit.get(2);
                    vNewSentenceButton.add(tmpButton);
                }
                if (!arrSentenceClauseStructure.get(i).vAddAfter1.equals("")){
                    SentenceButton tmpButton = new SentenceButton();
                    tmpButton.vButtonName = SentenceButtonEnum.ADD_AFTER_1;
                    tmpButton.vButtonType = arrSentenceClauseStructure.get(i).vClauseType;
                    tmpButton.vEnabled = true;
                    ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceClauseStructure.get(i).vAddAfter1.split(",")));
                    tmpButton.vP1 = arrSentenceClauseStructure.get(i).vAddAfter1From; //vSplit.get(0);
                    tmpButton.vP2 = arrSentenceClauseStructure.get(i).vAddAfter1To; //vSplit.get(1);
                    tmpButton.vNewState = arrSentenceClauseStructure.get(i).vAddAfter1NewState;  //vSplit.get(2);
                    vNewSentenceButton.add(tmpButton);
                }
                if (!arrSentenceClauseStructure.get(i).vAddAfter2.equals("")){
                    SentenceButton tmpButton = new SentenceButton();
                    tmpButton.vButtonName = SentenceButtonEnum.ADD_AFTER_2;
                    tmpButton.vButtonType = arrSentenceClauseStructure.get(i).vClauseType;
                    tmpButton.vEnabled = true;
                    ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceClauseStructure.get(i).vAddAfter2.split(",")));
                    tmpButton.vP1 = arrSentenceClauseStructure.get(i).vAddAfter2From; //vSplit.get(0);
                    tmpButton.vP2 = arrSentenceClauseStructure.get(i).vAddAfter2To; //vSplit.get(1);
                    tmpButton.vNewState = arrSentenceClauseStructure.get(i).vAddAfter2NewState; //vSplit.get(2);
                    vNewSentenceButton.add(tmpButton);
                }
                if (!arrSentenceClauseStructure.get(i).vDelete1.equals("")){
                    SentenceButton tmpButton = new SentenceButton();
                    tmpButton.vButtonName = SentenceButtonEnum.DELETE_1;
                    tmpButton.vButtonType = arrSentenceClauseStructure.get(i).vClauseType;
                    tmpButton.vEnabled = true;
                    ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceClauseStructure.get(i).vDelete1.split(",")));
                    tmpButton.vP1 = arrSentenceClauseStructure.get(i).vDelete1What; //vSplit.get(0);
                    tmpButton.vP2 = "";
                    tmpButton.vNewState = arrSentenceClauseStructure.get(i).vDelete1NewState; //vSplit.get(1);
                    vNewSentenceButton.add(tmpButton);
                }
                if (!arrSentenceClauseStructure.get(i).vDelete2.equals("")){
                    SentenceButton tmpButton = new SentenceButton();
                    tmpButton.vButtonName = SentenceButtonEnum.DELETE_2;
                    tmpButton.vButtonType = arrSentenceClauseStructure.get(i).vClauseType;
                    tmpButton.vEnabled = true;
                    ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceClauseStructure.get(i).vDelete2.split(",")));
                    tmpButton.vP1 = arrSentenceClauseStructure.get(i).vDelete2What; //vSplit.get(0);
                    tmpButton.vP2 = "";
                    tmpButton.vNewState = arrSentenceClauseStructure.get(i).vDelete2NewState; //vSplit.get(1);
                    vNewSentenceButton.add(tmpButton);
                }
                if (!arrSentenceClauseStructure.get(i).vChange1.equals("")){
                    SentenceButton tmpButton = new SentenceButton();
                    tmpButton.vButtonName = SentenceButtonEnum.CHANGE_1;
                    tmpButton.vButtonType = arrSentenceClauseStructure.get(i).vClauseType;
                    tmpButton.vEnabled = true;
                    ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceClauseStructure.get(i).vChange1.split(",")));
                    tmpButton.vP1 = arrSentenceClauseStructure.get(i).vChange1From; // vSplit.get(0);
                    tmpButton.vP2 = arrSentenceClauseStructure.get(i).vChange1To; //vSplit.get(1);
                    tmpButton.vNewState = arrSentenceClauseStructure.get(i).vChange1NewState; //vSplit.get(2);
                    vNewSentenceButton.add(tmpButton);
                }
                if (!arrSentenceClauseStructure.get(i).vChange2.equals("")){
                    SentenceButton tmpButton = new SentenceButton();
                    tmpButton.vButtonName = SentenceButtonEnum.CHANGE_2;
                    tmpButton.vButtonType = arrSentenceClauseStructure.get(i).vClauseType;
                    tmpButton.vEnabled = true;
                    ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceClauseStructure.get(i).vChange2.split(",")));
                    tmpButton.vP1 = arrSentenceClauseStructure.get(i).vChange2From; // vSplit.get(0);
                    tmpButton.vP2 = arrSentenceClauseStructure.get(i).vChange2To; //vSplit.get(1);
                    tmpButton.vNewState = arrSentenceClauseStructure.get(i).vChange2NewState; //vSplit.get(2);
                    vNewSentenceButton.add(tmpButton);
                }
                if (!arrSentenceClauseStructure.get(i).vChange3.equals("")){
                    SentenceButton tmpButton = new SentenceButton();
                    tmpButton.vButtonName = SentenceButtonEnum.CHANGE_3;
                    tmpButton.vButtonType = arrSentenceClauseStructure.get(i).vClauseType;
                    tmpButton.vEnabled = true;
                    ArrayList<String> vSplit = new ArrayList(Arrays.asList(arrSentenceClauseStructure.get(i).vChange3.split(",")));
                    tmpButton.vP1 = arrSentenceClauseStructure.get(i).vChange1From; //vSplit.get(0);
                    tmpButton.vP2 = arrSentenceClauseStructure.get(i).vChange3To; //vSplit.get(1);
                    tmpButton.vNewState = arrSentenceClauseStructure.get(i).vChange3NewState; //vSplit.get(2);
                    vNewSentenceButton.add(tmpButton);
                }
            }
        }
        vSentenceButton = vNewSentenceButton;
    }

    public void setCurrPronoun(String pPronoun, String vClauseType) {
        for (int i = 0; i < arrSentencePronoun.size(); i++) {
            if (pPronoun.equalsIgnoreCase(arrSentencePronoun.get(i).vStem)) {
                if (vClauseType.equals("subject")) {
                    currSubjectPerson = arrSentencePronoun.get(i).vPerson;
                    currSPN.setValue(pPronoun, pPronoun);
                    if (enableSPN) {
                        setSelectVerb();
                        changeVerb();
                    }
                } else {
                    currObjectPerson = arrSentencePronoun.get(i).vPerson;
                    currOPN.setValue(pPronoun, pPronoun);
/*                    if (enableOPN) {
                        setSelectVerb(currObjectPerson);
                    }*/
                }
            }
        }
    }

    private void changeVerb(){
        String chkVerbStem = currAVerb.getStem();
        for (SentenceVerb chkVerb : arrSentenceVerb){
            if (chkVerb.vTense == currTense && chkVerb.vPerson.equalsIgnoreCase(currSubjectPerson) && chkVerb.vStem.equalsIgnoreCase(chkVerbStem)){
                currAVerb.setValue(chkVerb.vStem, chkVerb.vResult);
            }
        }
    }

    private void enableObject(){
        currOPN.setValue("me","me");
        enableOPN = true;
        enableOArticle = false;
        enableOPPN = false;
        enableOAdjective = false;
        enableONoun = false;

    }

    private void setSelectVerb() {
        ArrayList<SentenceDisplayItem> retVerb = new ArrayList<SentenceDisplayItem>();
        for (int i = 0; i < arrSentenceVerb.size(); i++) {
            if (arrSentenceVerb.get(i).vPerson.equals(currSubjectPerson) && arrSentenceVerb.get(i).vTense == currTense) {
                SentenceDisplayItem tmpItem = new SentenceDisplayItem(arrSentenceVerb.get(i).vStem, arrSentenceVerb.get(i).vResult);
                retVerb.add(tmpItem);
            }
        }
        selAVerb = retVerb;
    }

    public SentenceButton getSentenceButtonEnable(String vType, String vButtonName){
        for (int i = 0; i< vSentenceButton.size() ; i++){
            if (vSentenceButton.get(i).vButtonType.equals(vType) && vSentenceButton.get(i).vButtonName.equals(vButtonName)){
                return vSentenceButton.get(i);
            }
        }
        return null;
    }

    public SentenceButton getStructureButtonEnable(String vButtonName){
        for (int i = 0; i< vStructButton.size() ; i++){
            if (vStructButton.get(i).vButtonName.equals(vButtonName)){
                return vStructButton.get(i);
            }
        }
        return null;
    }

    public void setSelArticle(String pType, SentenceDisplayItem pNoun){
        String vCountable = "0";
        String vVowel = "0";
        SentenceDisplayItem tmpItem;
        ArrayList<SentenceDisplayItem> retString = new ArrayList<SentenceDisplayItem>();
        for (int i = 0 ; i < arrSentenceNoun.size(); i++){
            if(pNoun.getStem().equals(arrSentenceNoun.get(i).vStem)){
                vCountable = arrSentenceNoun.get(i).vCountable;
                vVowel = arrSentenceNoun.get(i).vVowel;
            }
        }
        for (int i = 0; i < arrSentenceArticle.size(); i++){
            if (arrSentenceArticle.get(i).vCountable.equals(vCountable) && arrSentenceArticle.get(i).vVowel.equals(vVowel)||arrSentenceArticle.get(i).vCountable.equals(" ")&& arrSentenceArticle.get(i).vVowel.equals(" ")){
                tmpItem = new SentenceDisplayItem(arrSentenceArticle.get(i).vStem, arrSentenceArticle.get(i).vStem);
                retString.add(tmpItem);
            }
        }
        if (vCountable.equals("0")){
            tmpItem = new SentenceDisplayItem(" ", " ");
            retString.add(tmpItem);
        }

        if (pType.equals("subject")){
            selSArticle = retString;
        }else {
            selOArticle = retString;
        }
    }

    public void setNoun(SentenceDisplayItem pNoun, String vClauseType){
        for (int i = 0 ; i< arrSentenceNoun.size();i++){
            if (pNoun.equals(arrSentenceNoun.get(i).vStem)){
                if (vClauseType.equals("subject")) {
                    if (enableSSingle) {
                        currSubjectPerson = "3S";
                    }else{
                        currSubjectPerson = "3P";
                    }
                    if (enableSSingle) {
                        currSNoun.setValue(pNoun.getStem(), pNoun.getDisplay());
                    }
                    if (enableSNoun) {
                        setSelectVerb();
                    }
                    setSelArticle(vClauseType, currSNoun);
                }else{
                    currObjectPerson = arrSentencePronoun.get(i).vPerson;
                    currONoun.setValue(pNoun.getStem(),pNoun.getDisplay());
                    setSelArticle(vClauseType, currONoun);
/*                    if (enableOPN) {
                        setSelectVerb(currObjectPerson);
                    }*/
                }
            }
        }
    }

    public SentenceDisplayItem getCurrSPN(){
        return currSPN;
    }

    public SentenceDisplayItem getCurrOPN(){
        return currOPN;
    }

    public void setcurrSArticle(String pStem, String pDislpay){
        currSArticle.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getcurrSArticle(){
        return currSArticle;
    }

    public void setcurrSSPPN(String pStem, String pDislpay) {
        currSPPN.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getcurrSPPN(){
        return currSPPN;
    }

    public void setcurrSAdjective(String pStem, String pDislpay) {
        currSAdjective.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getcurrSAdjective(){
        return currSAdjective;
    }

    public void setcurrSNoun(String pStem, String pDislpay) {
        currSNoun.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getcurrSNoun(){
        return currSNoun;
    }

    public void setcurrAVerb(String pStem, String pDislpay) {
        currAVerb.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getcurrAVerb(){
        return currAVerb;
    }

    public void setcurrAAdverb(String pStem, String pDislpay) {
        currAAdverb.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getcurrAAdverb(){
        return currAAdverb;
    }

    public void setcurrAPreposition(String pStem, String pDislpay) {
        currAPreposition.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getcurrAPreposition(){
        return currAPreposition;
    }

    public void setcurrOArticle(String pStem, String pDislpay) {
        currOArticle.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getcurrOArticle(){
        return currOArticle;
    }

    public void setcurrOPPN(String pStem, String pDislpay) {
        currOPPN.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getccurrOPPN(){
        return currOPPN;
    }

    public void setcurrOAdjective(String pStem, String pDislpay) {
        currOAdjective.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getcurrOAdjective(){
        return currOAdjective;
    }

    public void setcurrONoun(String pStem, String pDislpay) {
        currONoun.setValue(pStem, pDislpay);
    }

    public SentenceDisplayItem getcurrONoun(){
        return currONoun;
    }

    public void changeSinglePlural(String pClauseType, String pCount){
        selectedS1SButton = false;
        selectedS2SButton = false;
        selectedS3SButton = false;
        selectedS1PButton = false;
        selectedS2PButton = false;
        selectedS3PButton = false;
        selectedO1SButton = false;
        selectedO2SButton = false;
        selectedO3SButton = false;
        selectedO1PButton = false;
        selectedO2PButton = false;
        selectedO3PButton = false;
        if (pClauseType.equals("subject")) {
            currSubjectPerson = pCount;
            if (currSubjectPerson.equals("1S")){
                selectedS1SButton = true;
            }
            if (currSubjectPerson.equals("2S")){
                selectedS2SButton = true;
            }
            if (currSubjectPerson.equals("3S")){
                selectedS3SButton = true;
            }
            if (currSubjectPerson.equals("1P")){
                selectedS1PButton = true;
            }
            if (currSubjectPerson.equals("2P")){
                selectedS2PButton = true;
            }
            if (currSubjectPerson.equals("3P")){
                selectedS3PButton = true;
            }
            setSelectVerb();
        }
        if (pClauseType.equals("object")) {
            currObjectPerson = pCount;
            enableOSingle = false;
            enableOPlural = true;
            if (currObjectPerson.equals("1S")){
                selectedO1SButton = true;
            }
            if (currObjectPerson.equals("2S")){
                selectedO2SButton = true;
            }
            if (currObjectPerson.equals("3S")){
                selectedO3SButton = true;
            }
            if (currObjectPerson.equals("1P")){
                selectedO1PButton = true;
            }
            if (currObjectPerson.equals("2P")){
                selectedO2PButton = true;
            }
            if (currObjectPerson.equals("3P")){
                selectedO3PButton = true;
            }
        }


    }

    public void changeTense(SentenceTenseEnum pTense){
        currTense = pTense;

        selectedPastTenseButton = false;
        selectedPresentTenseButton = false;
        selectedFutureTenseButton = false;
        selectedPastContinuousTenseButton = false;
        selectedPresentContinuousTenseButton = false;
        selectedFutureContinuousTenseButton = false;

        if (currTense.toString().equals(SentenceTenseEnum.PAST.toString())) {
            selectedPastTenseButton = true;
        }
        if (currTense.toString().equals(SentenceTenseEnum.PRESENT.toString())) {
            selectedPresentTenseButton = true;
        }
        if (currTense.toString().equals(SentenceTenseEnum.FUTURE.toString())) {
            selectedFutureTenseButton = true;
        }
        if (currTense.toString().equals(SentenceTenseEnum.PASTCONTINUOUS.toString())) {
            selectedPastContinuousTenseButton = true;
        }
        if (currTense.toString().equals(SentenceTenseEnum.PRESENTCONTINUOUS.toString())) {
            selectedPresentContinuousTenseButton = true;
        }
        if (currTense.toString().equals(SentenceTenseEnum.FUTURECONTINUOUS.toString())) {
            selectedFutureContinuousTenseButton = true;
        }


        setSelectVerb();
    }

}
