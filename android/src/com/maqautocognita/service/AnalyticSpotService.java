package com.maqautocognita.service;

import android.content.Context;
import android.util.Log;

import com.analyticspot.android.sdk.EventManager;
import com.analyticspot.android.sdk.SessionInfo;
import com.maqautocognita.adapter.IAnalyticSpotService;
import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.prototype.databases.DatabaseCursor;

import java.util.Date;
import java.util.UUID;

/**
 * lessonList
 */
public class AnalyticSpotService implements IAnalyticSpotService {
    public static final String SHARED_PREFS_FILE_NAME = "maqautocognita";
    public static final String USERNAME_KEY = "FRANK";
    private static final String ANX_API_KEY = "23NWo1fa6vFewVfI-5GJ/YzKBRK1yyoTZ";
    private EventManager eventManager;
    private EventManager eventManagerResend;
    private String vUserID="";
    public String userFrom = "";
    private Context vContext;
    private Database vPlayerDatabase;
    private String vSessionID;

    public void setEventManager(EventManager pEventManger){
        eventManager = pEventManger;
    }

    public AnalyticSpotService (Database pPlayerDatabase){
        vPlayerDatabase = pPlayerDatabase;
    }

    @Override
    public void updateScore(int ProgressCompleted, String UnitCode, String LessonCode, String ElementSequence, String ElementCode, String ProgressType, String language) {
        try {
            //save record to Analytic Spot when App resume
            if (eventManager != null) {
                if (!isAnalyticSpotAvailable()){
                    saveOfficeScore("LessonProgress", UnitCode, LessonCode, ElementSequence, ElementCode, ProgressType, language,
                            Integer.toString(ProgressCompleted));
                }else {
                    getEventManager().genericEvent()
                            .putTag("Data Name", "LessonProgress")
                            .putTag("Unit Code", UnitCode)
                            .putTag("Lesson Code", LessonCode)
                            .putTag("Element Sequence", ElementSequence)
                            .putTag("Element Code", ElementCode)
                            .putTag("Progress Type", ProgressType)
                            .putTag("Language", language)
                            .putTag("Progress Completed", Integer.toString(ProgressCompleted))
                            .putTag("timestamp", Long.toString(new Date().getTime()))
                            .putTag("mode", "online")
                            .send();
                }
            }
        }catch(Exception e){
            Log.i(this.getClass().getName(), e.getMessage());
        }
    }

    private void saveOfficeScore(String pDataName, String pUnitCode, String pLessonCode, String pElementSequence, String pElementCode, String pProgressType, String pLanguage, String pProgressCompleted){
        try {
            vPlayerDatabase.execSQL("insert into AnalyticSpotDataEnglish values ('"+ vSessionID + "','"+vUserID +
                    "','"+pDataName+"','"+pUnitCode +"','"+pLessonCode +"','"+pElementSequence + "','" + pElementCode +
                    "','"+pProgressType+"','"+pLanguage +"','"+pProgressCompleted +
                    "','"+Long.toString(new Date().getTime()) + "','0')");
        }catch (Exception e){
            String err = e.getMessage();
        }
    }

    @Override
    public void updateMathScore(int ProgressCompleted, String pElementCode, String language) {

        try {
            //save record to Analytic Spot when App resume
            if (eventManager != null) {
                if (!isAnalyticSpotAvailable()){
                    saveOfficeMathScore("MathLessonProgress", pElementCode, language,
                            Integer.toString(ProgressCompleted));
                }else {
                    getEventManager().genericEvent()
                            .putTag("Data Name", "MathLessonProgress")
                            .putTag("Element Code", pElementCode)
                            .putTag("Language", language)
                            .putTag("Progress Completed", Integer.toString(ProgressCompleted))
                            .putTag("timestamp", Long.toString(new Date().getTime()))
                            .putTag("mode", "online")
                            .send();
                }
            }
        }catch(Exception e){
            Log.i(this.getClass().getName(), e.getMessage());
        }
    }

    private void saveOfficeMathScore(String pDataName, String pElementCode, String pLanguage, String pProgressCompleted){
        try {
            vPlayerDatabase.execSQL("insert into AnalyticSpotDataMath values ('"+ vSessionID + "','"+vUserID +
                    "','"+pDataName+"','"+pElementCode + "','"+pLanguage +"','"+pProgressCompleted +
                    "','"+Long.toString(new Date().getTime()) + "','0')");
        }catch (Exception e){
            String err = e.getMessage();
        }
    }

    public EventManager getEventManager() {
        return eventManager;
    }


    public void saveUserId(Context pContext, String pUserID) {
        String tmpTag = "";
        vContext = pContext;
        if (userFrom.equals("AnalyticSpot")) {
            tmpTag = "lifecycle";
        }else {
            tmpTag = "otherlifecycle";
        }
        try {
            vSessionID = UUID.randomUUID().toString();

            vUserID = pUserID;
            SessionInfo sessionInfo = SessionInfo.builder()
                    .setSessionId(vSessionID)
                    .setAppId(SHARED_PREFS_FILE_NAME) // App ID here
                    .setAppStore(com.analyticspot.anxevent.Context.AppStore.GOOGLE_PLAY)
                    .setAppVersion("1.0")
                    .setUser("XPRIZE", vUserID)
                    .build();
            eventManager = EventManager.create(ANX_API_KEY, sessionInfo, vContext);
            if( !isAnalyticSpotAvailable()){
                unsaveStopStartData(tmpTag, "started", Long.toString(new Date().getTime()));
            }else {
                getEventManager().genericEvent()
                        .putTag(tmpTag, "started")
                        .putTag("timestamp", Long.toString(new Date().getTime()))
                        .putTag("mode","online")
                        .send();
            }
        } catch (Exception e){
            Log.e(this.getClass().getName(), "saveUserId Exception:", e);
        }

    }

    public void renewSessionID(String pSessionID, Boolean isResend){

        String tmpTag = "";
        if (userFrom.equals("AnalyticSpot")) {
            tmpTag = "lifecycle";
        }else {
            tmpTag = "otherlifecycle";
        }
        if (pSessionID.equals("")){
            vSessionID = UUID.randomUUID().toString();
        }else{
            vSessionID = pSessionID;
        }
        try {
//
//            if (isResend){
//                SessionInfo sessionInfo = SessionInfo.builder()
//                        .setSessionId(pSessionID)
//                        .setAppId(SHARED_PREFS_FILE_NAME) // App ID here
//                        .setAppStore(com.analyticspot.anxevent.Context.AppStore.GOOGLE_PLAY)
//                        .setAppVersion("1.0")
//                        .setUser("XPRIZE", vUserID)
//                        .build();
//                eventManagerResend = EventManager.create(ANX_API_KEY, sessionInfo, vContext);
//            }else {
                SessionInfo sessionInfo = SessionInfo.builder()
                        .setSessionId(vSessionID)
                        .setAppId(SHARED_PREFS_FILE_NAME) // App ID here
                        .setAppStore(com.analyticspot.anxevent.Context.AppStore.GOOGLE_PLAY)
                        .setAppVersion("1.0")
                        .setUser("XPRIZE", vUserID)
                        .build();
                eventManager = EventManager.create(ANX_API_KEY, sessionInfo, vContext);
//            }
        } catch (Exception e){
            Log.e(this.getClass().getName(), "saveUserId Exception:", e);
        }
    }

    public void onResume(){
        //save record to Analytic Spot when App resume
        String tmpTag = "";
        if (userFrom.equals("AnalyticSpot")) {
            tmpTag = "lifecycle";
        }else {
            tmpTag = "otherlifecycle";
        }
        if (eventManager != null) {
            if( !isAnalyticSpotAvailable()){
                unsaveStopStartData(tmpTag, "started", Long.toString(new Date().getTime()));
            }else {
                resentStopStartData();
                resentScoreEnglishData();
                resentScoreMathData();
                getEventManager().genericEvent()
                        .putTag(tmpTag, "started")
                        .putTag("timestamp", Long.toString(new Date().getTime()))
                        .putTag("mode","online")
                        .send();

            }
        }
    }

    public void onPause(){
        //save record to Analytic Spot when App close

        String tmpTag = "";
        if (userFrom.equals("AnalyticSpot")) {
            tmpTag = "lifecycle";
        }else {
            tmpTag = "otherlifecycle";
        }

        if (eventManager != null) {
            if( !isAnalyticSpotAvailable()){
                unsaveStopStartData(tmpTag, "stopped", Long.toString(new Date().getTime()));
            }else {
            getEventManager().genericEvent()
                    .putTag(tmpTag, "stopped")
                    .putTag("timestamp", Long.toString(new Date().getTime()))
                    .putTag("mode","online")
                    .send();

            }
        }
        renewSessionID("",false);
    }

    private void unsaveStopStartData(String pTageName, String pTagValue, String pTime){
        try {
            vPlayerDatabase.execSQL("insert into AnalyticSpotData values ('"+ vSessionID + "','"+vUserID +
                    "','"+pTageName+"','"+pTagValue + "','"+pTime + "','0')");
        }catch (Exception e){
            String err = e.getMessage();
        }
    }

    private void resentStopStartData(){
        DatabaseCursor vDatabaseCursorElement = null;
        String tmpSessionID = vSessionID;
        try {
            // get data from database
            vDatabaseCursorElement = vPlayerDatabase.rawQuery("select SessionID, UserID, TagName, TagValue, TimeStamp from AnalyticSpotData" + " where Sent = '0'");

            if (vDatabaseCursorElement.moveToFirst()) {
                do {
                    vUserID = vDatabaseCursorElement.getString(1);
                    vSessionID = vDatabaseCursorElement.getString(0);
                    renewSessionID(vSessionID, true);
                    String tmpTag = "";
                    if (userFrom.equals("AnalyticSpot")) {
                        tmpTag = "lifecycle";
                    } else {
                        tmpTag = "otherlifecycle";
                    }
                    if (eventManager != null) {
                        getEventManager().genericEvent()
                                .putTag(vDatabaseCursorElement.getString(2), vDatabaseCursorElement.getString(3))
                                .putTag("timestamp", vDatabaseCursorElement.getString(4))
                                .putTag("mode","offline")
                                .send();
                    }
                }while (vDatabaseCursorElement.next());
                vPlayerDatabase.execSQL("delete from AnalyticSpotData");
                vSessionID = tmpSessionID;
                renewSessionID(vSessionID, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursorElement) {
                vDatabaseCursorElement.close();
            }
        }

    }


    private void resentScoreEnglishData(){
        DatabaseCursor vDatabaseCursorElement = null;
        String tmpSessionID = vSessionID;
        try {
            // get data from database
            vDatabaseCursorElement = vPlayerDatabase.rawQuery("select SessionID, UserID, DataName, UnitCode, LessonCode, ElementSequence, ElementCode, ProgressType, Language, ProgressCompleted, TimeStamp from AnalyticSpotDataEnglish" + " where Sent = '0'");

            if (vDatabaseCursorElement.moveToFirst()) {
                do {
                    vUserID = vDatabaseCursorElement.getString(1);
                    vSessionID = vDatabaseCursorElement.getString(0);
                    renewSessionID(vSessionID, true);
                        getEventManager().genericEvent()
                                .putTag("Data Name", "LessonProgress")
                                .putTag("Unit Code", vDatabaseCursorElement.getString(3))
                                .putTag("Lesson Code", vDatabaseCursorElement.getString(4))
                                .putTag("Element Sequence", vDatabaseCursorElement.getString(5))
                                .putTag("Element Code", vDatabaseCursorElement.getString(6))
                                .putTag("Progress Type", vDatabaseCursorElement.getString(7))
                                .putTag("Language", vDatabaseCursorElement.getString(8))
                                .putTag("Progress Completed", vDatabaseCursorElement.getString(9))
                                .putTag("timestamp", vDatabaseCursorElement.getString(10))
                                .putTag("mode", "offline")
                                .send();

                }while (vDatabaseCursorElement.next());
                vPlayerDatabase.execSQL("delete from AnalyticSpotDataEnglish");
                vSessionID = tmpSessionID;
                renewSessionID(vSessionID, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursorElement) {
                vDatabaseCursorElement.close();
            }
        }

    }

    private void resentScoreMathData(){
        DatabaseCursor vDatabaseCursorElement = null;
        String tmpSessionID = vSessionID;
        try {
            // get data from database
            vDatabaseCursorElement = vPlayerDatabase.rawQuery("select SessionID, UserID, DataName, ElementCode, Language, ProgressCompleted, TimeStamp from AnalyticSpotDataMath" + " where Sent = '0'");

            if (vDatabaseCursorElement.moveToFirst()) {
                do {
                    vUserID = vDatabaseCursorElement.getString(1);
                    vSessionID = vDatabaseCursorElement.getString(0);
                    renewSessionID(vSessionID, true);
                    getEventManager().genericEvent()
                            .putTag("Data Name", "MathLessonProgress")
                            .putTag("Element Code",  vDatabaseCursorElement.getString(3))
                            .putTag("Language",  vDatabaseCursorElement.getString(4))
                            .putTag("Progress Completed",  vDatabaseCursorElement.getString(5))
                            .putTag("timestamp",  vDatabaseCursorElement.getString(6))
                            .putTag("mode", "offline")
                            .send();

                }while (vDatabaseCursorElement.next());
                vPlayerDatabase.execSQL("delete from AnalyticSpotDataMath");
                vSessionID = tmpSessionID;
                renewSessionID(vSessionID, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursorElement) {
                vDatabaseCursorElement.close();
            }
        }

    }

    public boolean isAnalyticSpotAvailable()
    {
        try {
            String command = "ping -c 1 analyticspot.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        }catch(Exception e){

            String err = e.getMessage();
            return false;
        }
    }

}
