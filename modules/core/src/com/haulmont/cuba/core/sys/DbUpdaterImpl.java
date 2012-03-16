/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import groovy.lang.Binding;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrMatcher;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(DbUpdater.NAME)
public class DbUpdaterImpl implements DbUpdater {

    // File extension handler
    private interface FileHandler {
        void run(File file);
    }

    @Inject
    private Scripting scripting;

    private boolean changelogTableExists;

    protected File dbDir;

    private static final String SQL_EXTENSION = "sql";
    private static final String GROOVY_EXTENSION = "groovy";

    // register handlers for script files
    private HashMap<String, FileHandler> extensionHandlers = new HashMap<String, FileHandler>();

    {
        extensionHandlers.put(SQL_EXTENSION, new FileHandler() {
            @Override
            public void run(File file) {
                executeSqlScript(file);
            }
        });
        extensionHandlers.put(GROOVY_EXTENSION, new FileHandler() {
            @Override
            public void run(File file) {
                executeGroovyScript(file);
            }
        });
    }

    private ClusterManagerAPI clusterManager;

    private Log log = LogFactory.getLog(DbUpdaterImpl.class);

    @Inject
    public void setConfigProvider(Configuration configuration) {
        String dbDirName = configuration.getConfig(ServerConfig.class).getDbDir();
        if (dbDirName != null)
            dbDir = new File(dbDirName);
    }

    @Inject
    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;
    }

    @Override
    public void updateDatabase() {
        if (!clusterManager.isMaster()) {
            log.info("Not a master node, exiting");
            return;
        }

        if (dbInitialized()) {
            doUpdate();
        } else {
            doInit();
        }
    }

    @Override
    public List<String> findUpdateDatabaseScripts() {
        List<String> list = new ArrayList<String>();
        if (dbInitialized()) {
            if (!changelogTableExists) {
                list.add("Unable to determine required updates because SYS_DB_CHANGELOG table doesn't exist");
            } else {
                List<File> files = getUpdateScripts();
                Set<String> scripts = getExecutedScripts();
                for (File file : files) {
                    String name = getScriptName(file);
                    if (!scripts.contains(name)) {
                        list.add(file.getPath());
                    }
                }
            }
        } else {
            for (File file : getInitScripts()) {
                list.add(file.getPath());
            }
        }
        return list;
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
                } catch (SQLException ignored) {
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

        prepareScripts();

        log.info("Database initialized");
    }

    private List<File> getUpdateScripts() {
        List<File> files = new ArrayList<File>();

        if (dbDir.exists()) {
            String[] moduleDirs = dbDir.list();
            Arrays.sort(moduleDirs);
            for (String moduleDirName : moduleDirs) {
                File moduleDir = new File(dbDir, moduleDirName);
                File initDir = new File(moduleDir, "update");
                File scriptDir = new File(initDir, PersistenceProvider.getDbDialect().getName());
                if (scriptDir.exists()) {
                    List<File> list = new ArrayList<File>(FileUtils.listFiles(scriptDir, null, true));
                    final URI scriptDirUri = scriptDir.toURI();
                    Collections.sort(list, new Comparator<File>() {
                        @Override
                        public int compare(File f1, File f2) {
                            URI f1Uri = scriptDirUri.relativize(f1.toURI());
                            URI f2Uri = scriptDirUri.relativize(f2.toURI());

                            return f1Uri.getPath().compareTo(f2Uri.getPath());
                        }
                    });
                    files.addAll(list);
                }
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
        log.info("Updating database...");

        if (!changelogTableExists) {
            log.info("Changelog table not found, creating it and mark all scripts as executed");

            createChangelogTable();

            List<File> initFiles = getInitScripts();
            for (File file : initFiles) {
                markScript(getScriptName(file), true);
            }

            prepareScripts();

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


    /**
     * Mark all SQL updates scripts as evaluated
     * Try to execute Groovy scripts
     */
    private void prepareScripts() {
        List<File> updateFiles = getUpdateScripts();
        for (File file : updateFiles)
            markScript(getScriptName(file), true);
    }

    private Set<String> getExecutedScripts() {
        QueryRunner runner = new QueryRunner(Locator.getDataSource());
        try {
            Set<String> scripts = runner.query("select SCRIPT_NAME from SYS_DB_CHANGELOG",
                    new ResultSetHandler<Set<String>>() {
                        @Override
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
                    "CREATE_TS " + (DbmsType.getCurrent().equals(DbmsType.MSSQL) ? "datetime" : "timestamp") + " default current_timestamp, " +
                    "IS_INIT integer default 0)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeSqlScript(File file) {
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
                if (sql.trim().toLowerCase().startsWith("select")) {
                    runner.query(sql, new ResultSetHandler<Object>() {
                        @Override
                        public Object handle(ResultSet rs) throws SQLException {
                            return null;
                        }
                    });
                } else {
                    runner.update(sql);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void executeGroovyScript(File file) {
        Binding bind = new Binding();
        scripting.runGroovyScript(getScriptName(file), bind);
    }

    private void executeScript(File file) {
        log.info("Executing script " + file.getPath());
        String filename = file.getName();
        String extension = getFileExtension(filename);
        if (StringUtils.isNotEmpty(extension)) {
            if (extensionHandlers.containsKey(extension)) {
                FileHandler handler = extensionHandlers.get(extension);
                handler.run(file);
            } else
                log.warn("Update script ignored, file handler for extension not found:" +
                        file.getName());
        } else
            log.warn("Update script ignored, file extension undefined:" + file.getName());
    }

    private String getFileExtension(String filename) {
        int dotPos = filename.lastIndexOf(".");
        return filename.substring(dotPos + 1);
    }

    private void markScript(String name, boolean init) {
        QueryRunner runner = new QueryRunner(Locator.getDataSource());
        try {
            runner.update("insert into SYS_DB_CHANGELOG (SCRIPT_NAME, IS_INIT) values (?, ?)",
                    new Object[]{name, init ? 1 : 0}
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<File> getInitScripts() {
        List<File> files = new ArrayList<File>();
        if (dbDir.exists()) {
            String[] moduleDirs = dbDir.list();
            Arrays.sort(moduleDirs);
            for (String moduleDirName : moduleDirs) {
                File moduleDir = new File(dbDir, moduleDirName);
                File initDir = new File(moduleDir, "init");
                File scriptDir = new File(initDir, PersistenceProvider.getDbDialect().getName());
                if (scriptDir.exists()) {
                    File[] scriptFiles = scriptDir.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith("create-db.sql");
                        }
                    });
                    Arrays.sort(scriptFiles);
                    files.addAll(Arrays.asList(scriptFiles));
                }
            }
        }
        return files;
    }
}
