package com.maqautocognita.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siu-chun.chi on 7/3/2017.
 */

public class ConversationLesson {

    public String lessonCode;

    public String unitCode;

    public String topic;

    public List<Conversation> conversationList;

    public void addConversation(Conversation conversation) {
        if (null == conversationList) {
            conversationList = new ArrayList<Conversation>();
        }

        conversationList.add(conversation);

    }

}
