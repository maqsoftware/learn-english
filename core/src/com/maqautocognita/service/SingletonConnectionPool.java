/*
 * Copyright (C) 2014 RoboVM AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.maqautocognita.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * It is used to returns the same jdbc connection until it is closed.
 *
 */
public class SingletonConnectionPool  {

    private static SingletonConnectionPool instance = null;

    private final String lessonJdbcUrl;
    private final String dictionaryJdbcUrl;
    private Connection connection;

    private Connection dictionaryConnection;

    private SingletonConnectionPool(String lessonJdbcUrl, String dictionaryJdbcUrl) {
        this.lessonJdbcUrl = lessonJdbcUrl;
        this.dictionaryJdbcUrl = dictionaryJdbcUrl;
    }

    public static SingletonConnectionPool getInstance(String lessonJdbcUrl, String dictionaryJdbcUrl) {
        if(instance == null) {
            instance = new SingletonConnectionPool(lessonJdbcUrl, dictionaryJdbcUrl);
        }
        return instance;
    }

    /**
     * This should be call after call the init the {@link SingletonConnectionPool} by calling {@link SingletonConnectionPool#getInstance(String)}
     *
     * @return
     */
    public static SingletonConnectionPool getInstance() {
        if(instance == null) {
            throw new Error("You must init the SingletonConnectionPool first");
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(lessonJdbcUrl);
        }
        return connection;
    }

    public Connection getDictionaryConnection() throws SQLException {
        if (dictionaryConnection == null || dictionaryConnection.isClosed()) {
            dictionaryConnection = DriverManager.getConnection(dictionaryJdbcUrl);
        }
        return dictionaryConnection;
    }

}
