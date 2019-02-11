package com.maqautocognita.service;

import com.maqautocognita.bo.Dictionary;
import com.maqautocognita.utils.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kotarou on 28/2/2017.
 */
public class DictionaryService {

    private static DictionaryService instance = null;

    public static DictionaryService getInstance() {
        if (instance == null) {
            instance = new DictionaryService();
        }
        return instance;
    }

    public List<Dictionary> getDictionaryByWord(String word) {
        List<Dictionary> dictionaryList = null;
        String searchWord = word;
        Boolean found = false;
        try {
            Connection conn = SingletonConnectionPool.getInstance().getDictionaryConnection();
            PreparedStatement stmt = conn.prepareStatement("select  word, ss_type, gloss, Example " +
                    " from WordnetDictionary where lower(word) = lower(?)");

            stmt.setString(1, searchWord.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                found = true;
            }else{
                if(word.toLowerCase().endsWith("s")) {
                    searchWord= word.substring(0, word.length() - 1);
                }
                stmt.setString(1, searchWord.toLowerCase());
                rs = stmt.executeQuery();
                if (rs.next()) {
                    found = true;
                }else {
                    if (word.toLowerCase().endsWith("es")) {
                        searchWord = word.substring(0, word.length() - 1);
                    }
                    stmt.setString(1, searchWord.toLowerCase());
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        found = true;
                    } else {
                        if (word.toLowerCase().endsWith("d")) {
                            searchWord = word.substring(0, word.length() - 1);
                        }
                        stmt.setString(1, searchWord.toLowerCase());
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            found = true;
                        } else {
                            if (word.toLowerCase().endsWith("ed")) {
                                searchWord = word.substring(0, word.length() - 1);
                            }
                            stmt.setString(1, searchWord.toLowerCase());
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                found = true;
                            }else{
                                found = false;
                            }
                        }
                    }
                }
            }

            if (found){
                do {
                    Dictionary dictionary = new Dictionary();
                    dictionary.wordType = rs.getString(2);

                    if (StringUtils.isNotBlank(rs.getString(3))) {
                        for (String definition : rs.getString(3).split(";")) {
                            dictionary.addDefinition(definition);
                        }
                    }

                    dictionary.addExample(rs.getString(4));

                    if (null == dictionaryList) {
                        dictionaryList = new ArrayList<Dictionary>();
                    }

                    dictionaryList.add(dictionary);
                }while (rs.next());
                rs.close();
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dictionaryList;

    }

}
