package com.maqautocognita.prototype.sentence;

/**
 * Created by kotarou on 20/5/2017.
 */
public class SentenceDisplayItem {
    private String vStem;
    private String vDisplay;

    public SentenceDisplayItem(String pStem, String pDislpay){
        vStem = pStem;
        vDisplay = pDislpay;
    }

    public void setValue(String pStem, String pDislpay){
        vStem = pStem;
        vDisplay = pDislpay;
    }

    public SentenceDisplayItem getDisplayItem(){
        SentenceDisplayItem retValue = new SentenceDisplayItem(vStem,vDisplay);
        return retValue;
    }
    public String getStem (){
        return vStem;
    }
    public String getDisplay(){
        return vDisplay;
    }
}
