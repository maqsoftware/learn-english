package com.maqautocognita.service;

import com.maqautocognita.bo.Conversation;
import com.maqautocognita.bo.ConversationLesson;
import com.badlogic.gdx.Gdx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by siu-chun.chi on 5/9/2017.
 */

public class ConversationService {

    private static ConversationService instance = null;
    private List<ConversationLesson> lessonList;

    private ConversationService() {

    }

    public static ConversationService getInstance() {
        if (instance == null) {
            instance = new ConversationService();
        }
        return instance;
    }

    public List<ConversationLesson> getAllLesson() {

        if (null == lessonList) {

            ResultSet rs = null;
            PreparedStatement stmt = null;
            try {

                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                stmt = conn.prepareStatement("select * from Conversation order by lesson_Code,element_sequence");
                rs = stmt.executeQuery();

                ConversationLesson conversationLesson = null;

                while (rs.next()) {

                    String lessonCode = rs.getString("lesson_code");
                    String unitCode = rs.getString("unit_code");
                    if (null == conversationLesson || !conversationLesson.lessonCode.equals(lessonCode)) {
                        conversationLesson = new ConversationLesson();
                        conversationLesson.unitCode = unitCode;
                        conversationLesson.lessonCode = lessonCode;
                        conversationLesson.topic = rs.getString("topic");

                        if (null == lessonList) {
                            lessonList = new ArrayList<ConversationLesson>();
                        }

                        lessonList.add(conversationLesson);
                    }
                    Conversation conversation = new Conversation();

                    conversation.elementSequence = rs.getInt("element_sequence");
                    conversation.sentences = rs.getString("sentences");
                    conversation.speaker = rs.getString("speaker");
                    conversation.audio = rs.getString("audio");
                    conversation.time = rs.getFloat("time");

                    conversationLesson.addConversation(conversation);

                }
            } catch (Exception e) {
                Gdx.app.error(getClass().getName(), "", e);
            } finally {
                if (null != stmt) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (null != rs) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        return lessonList;
    }

}
