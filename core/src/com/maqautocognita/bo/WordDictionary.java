package com.maqautocognita.bo;

import com.maqautocognita.service.SingletonConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kotarou on 28/2/2017.
 */
public class WordDictionary {
    public String dictWord;
    public String relatedWord;
    public String typeOfWord;
    public String meaning;
    public String searchingResult;
    public String searchingResultMessage;

    public WordDictionary(String pWord) {
        dictWord = pWord;
        searchingResult = "initial";
        searchingResultMessage = "";
        getDictWord(pWord);
    }

    public void getDictWord(String word) {
        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select  d.word, d.OtherForm, d.WordType, d.WordMeaning " +
                    " from Dictionary d, DictionaryWordMapping dwm where d.id = dwm.id and dwm.word = ?");

            stmt.setString(1, word.toLowerCase());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.dictWord = rs.getString(1);
                this.relatedWord = rs.getString(2);
                this.typeOfWord = rs.getString(3);
                this.meaning = rs.getString(4);
                this.searchingResult = "success";

            } else {
                this.searchingResult = "error";
                this.searchingResultMessage = "Word not found";
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            this.searchingResult = "error";
            this.searchingResultMessage = e.getMessage();
            throw new RuntimeException(e);
        }

    }
}
