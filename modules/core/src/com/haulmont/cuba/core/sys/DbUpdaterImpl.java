/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.04.2010 15:12:52
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.sys.DbUpdater;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.text.StrMatcher;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@ManagedBean(DbUpdater.NAME)
public class DbUpdaterImpl implements DbUpdater {

    private boolean changelogTableExists;

    protected File dbDir;

    private Log log = LogFactory.getLog(DbUpdaterImpl.class);

    @Inject
    public void setConfigProvider(ConfigProvider cp) {
        String dbDirName = cp.doGetConfig(ServerConfig.class).getServerDbDir();
        dbDir = new File(dbDirName);
    }

    public void updateDatabase() {
        if (dbInitialized()) {
            doUpdate();
        } else {
            doInit();
        }
    }

    private boolean dbInitialized() {
        Connection connection = null;
        try {
            connection = Locator.getDataSource().getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet tables = dbMetaData.getTables(null, null, null, null);
            boolean found = false;
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if ("SYS_DB_CHANGELOG".equalsIgnoreCase(tableName)) {
                    changelogTableExists = true;
                }
                if ("SEC_USER".equalsIgnoreCase(tableName)) {
                    found = true;
                }
            }
            return found;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    //
                }
        }
    }

    private void doInit() {
        log.info("Initializing database");

        createChangelogTable();

        List<File> initFiles = getInitScripts();
        for (File file : initFiles) {
            executeScript(file);
            markScript(getScriptName(file), true);
        }

        List<File> updateFiles = getUpdateScripts();
        for (File file : updateFiles) {
            markScript(getScriptName(file), true);
        }
        log.info("Database initialized");
    }

    private List<File> getUpdateScripts() {
        List<File> files = new ArrayList<File>();

        String[] moduleDirs = dbDir.list();
        Arrays.sort(moduleDirs);
        for (String moduleDirName : moduleDirs) {
            File moduleDir = new File(dbDir, moduleDirName);
            File initDir = new File(moduleDir, "update");
            File scriptDir = new File(initDir, PersistenceProvider.getDbDialect().getName());
            if (scriptDir.exists()) {
                List<File> list = new ArrayList(FileUtils.listFiles(scriptDir, null, true));
                Collections.sort(list, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        return f1.getName().compareTo(f2.getName());
                    }
                });
                files.addAll(list);
            }
        }
        return files;
    }

    private String getScriptName(File file) {
        try {
            String path = file.getCanonicalPath();
            String dir = dbDir.getCanonicalPath();
            return path.substring(dir.length() + 1).replace("\\", "/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doUpdate() {
        log.info("Updating database");

        if (!changelogTableExists) {
            log.info("Changelog table not found, creating it and mark all scripts as executed");

            createChangelogTable();

            // just mark all scripts as executed before
            List<File> initFiles = getInitScripts();
            for (File file : initFiles) {
                markScript(getScriptName(file), true);
            }

            List<File> updateFiles = getUpdateScripts();
            for (File file : updateFiles) {
                markScript(getScriptName(file), true);
            }

            return;
        }

        List<File> files = getUpdateScripts();
        Set<String> scripts = getExecutedScripts();
        for (File file : files) {
            String name = getScriptName(file);
            if (!scripts.contains(name)) {
                executeScript(file);
                markScript(name, false);
            }
        }
        log.info("Database is up-to-date");
    }

    private Set<String> getExecutedScripts() {
        QueryRunner runner = new QueryRunner(Locator.getDataSource());
        try {
            Set<String> scripts = runner.query("select SCRIPT_NAME from SYS_DB_CHANGELOG",
                    new ResultSetHandler<Set<String>>() {
                        public Set<String> handle(ResultSet rs) throws SQLException {
                            Set<String> rows = new HashSet<String>();
                            while (rs.next()) {
                                rows.add(rs.getString(1));
                            }
                            return rows;
                        }
                    });
            return scripts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createChangelogTable() {
        QueryRunner runner = new QueryRunner(Locator.getDataSource());
        try {
            runner.update("create table SYS_DB_CHANGELOG(" +
                    "SCRIPT_NAME varchar(300) not null primary key, " +
                    "CREATE_TS timestamp default current_timestamp, " +
                    "IS_INIT integer default 0)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeScript(File file) {
        log.info("Executing script " + file.getPath());
        String script;
        try {
            script = FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StrTokenizer tokenizer = new StrTokenizer(script,
                StrMatcher.charSetMatcher(PersistenceProvider.getDbDialect().getScriptSeparator()),
                StrMatcher.singleQuoteMatcher()
        );
        QueryRunner runner = new QueryRunner(Locator.getDataSource());
        while (tokenizer.hasNext()) {
            String sql = tokenizer.nextToken();
            try {
                runner.update(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void markScript(String name, boolean init) {
        QueryRunner runner = new QueryRunner(Locator.getDataSource());
        try {
            runner.update("insert into SYS_DB_CHANGELOG (SCRIPT_NAME, IS_INIT) values (?, ?)",
                    new Object[] { name, init ? 1 : 0 }
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<File> getInitScripts() {
        List<File> files = new ArrayList<File>();

        String[] moduleDirs = dbDir.list();
        Arrays.sort(moduleDirs);
        for (String moduleDirName : moduleDirs) {
            File moduleDir = new File(dbDir, moduleDirName);
            File initDir = new File(moduleDir, "init");
            File scriptDir = new File(initDir, PersistenceProvider.getDbDialect().getName());
            File file = new File(scriptDir, "create-db.sql");
            if (file.exists()) {
                files.add(file);
            }
        }
        return files;
    }

}
