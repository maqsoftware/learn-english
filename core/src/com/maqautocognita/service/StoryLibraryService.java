package com.maqautocognita.service;

import com.maqautocognita.bo.StoryLibraryBook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public final class StoryLibraryService {

    private static StoryLibraryService instance = null;

    public static StoryLibraryService getInstance() {
        if (instance == null) {
            instance = new StoryLibraryService();
        }
        return instance;
    }

    public List<StoryLibraryBook> getAllStoryLibraryBook() {

        List<StoryLibraryBook> storyLibraryBookList = null;

        try {

            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select StoryNumber,AudioFileName,ImageFileName,StoryTitle from StoryBook where PageNumber = 0 order by StoryNumber");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                StoryLibraryBook storyLibraryBook = new StoryLibraryBook();

                storyLibraryBook.number = rs.getInt("StoryNumber");
                storyLibraryBook.audioFileName = rs.getString("AudioFileName");
                storyLibraryBook.imageFileName = rs.getString("ImageFileName");
                storyLibraryBook.title = rs.getString("StoryTitle");

                if (null == storyLibraryBookList) {
                    storyLibraryBookList = new ArrayList<StoryLibraryBook>();
                }

                storyLibraryBookList.add(storyLibraryBook);


            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return storyLibraryBookList;
    }

    public List<StoryLibraryBook> getAllStoryBookPage(int storyNumber) {

        List<StoryLibraryBook> storyLibraryBookList = null;

        try {

            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select StoryNumber,PageNumber,AudioFileName,ImageFileName,StoryTitle,Sentence1,Sentence2,Sentence3,Sentence4 " +
                    "from StoryBook where StoryNumber = ?  order by PageNumber");
            stmt.setInt(1, storyNumber);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                StoryLibraryBook storyLibraryBook = new StoryLibraryBook();

                storyLibraryBook.number = rs.getInt("StoryNumber");
                storyLibraryBook.pageNumber = rs.getInt("PageNumber");
                storyLibraryBook.audioFileName = rs.getString("AudioFileName");
                storyLibraryBook.imageFileName = rs.getString("ImageFileName");
                storyLibraryBook.title = rs.getString("StoryTitle");
                storyLibraryBook.sentence1 = rs.getString("Sentence1");
                storyLibraryBook.sentence2 = rs.getString("Sentence2");
                storyLibraryBook.sentence3 = rs.getString("Sentence3");
                storyLibraryBook.sentence4 = rs.getString("Sentence4");

                if (null == storyLibraryBookList) {
                    storyLibraryBookList = new ArrayList<StoryLibraryBook>();
                }

                storyLibraryBookList.add(storyLibraryBook);


            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return storyLibraryBookList;
    }

}
