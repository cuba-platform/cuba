/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.testsupport;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.PrintWriter;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class TestDataSource implements DataSource {

    private String connUrl;
    private String user;
    private String password;

    public TestDataSource(String connUrl, String user, String password) {
        this.connUrl = connUrl;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connUrl, user, password);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(connUrl, username, password);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    public void setLoginTimeout(int seconds) throws SQLException {
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
