package com.maqautocognita.service;

import com.maqautocognita.constant.Language;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class VOScriptService {

    private static VOScriptService instance = null;

    private VOScriptService() {

    }

    public static VOScriptService getInstance() {
        if (instance == null) {
            instance = new VOScriptService();
        }
        return instance;
    }

    public List<String> getListOfFileNameByVoScriptName(String voScriptName) {

        List<String> audioFileNameList = null;
        if (StringUtils.isNotBlank(voScriptName)) {
            try {
                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement("select * from vo_script where vo_script_name = ?  and language = ?");
                stmt.setString(1, voScriptName);
                stmt.setString(2, UserPreferenceUtils.getInstance().isEnglish() ? Language.ENGLISH : Language.SWAHILI);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    if (null == audioFileNameList) {
                        audioFileNameList = new ArrayList<String>();
                    }

                    for (int i = 1; i <= 11; i++) {
                        addAudioFileName(audioFileNameList, rs, i);
                    }
                }
                rs.close();
                stmt.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return audioFileNameList;
    }

    private void addAudioFileName(List<String> audioFileNameList, ResultSet rs, int scriptIndex) throws SQLException {
        String audioFileName = rs.getString("vo_filename" + scriptIndex);
        if (StringUtils.isNotBlank(audioFileName)) {
            audioFileNameList.add(audioFileName);
        }
    }
}
