/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import groovy.lang.Binding;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(DbUpdater.NAME)
public class DbUpdaterImpl implements DbUpdater {

    // File extension handler
    protected interface FileHandler {
        void run(File file);
    }

    @Inject
    protected Scripting scripting;

    protected boolean changelogTableExists;

    protected File dbDir;

    protected static final String SQL_EXTENSION = "sql";
    protected static final String GROOVY_EXTENSION = "groovy";

    // register handlers for script files
    protected HashMap<String, FileHandler> extensionHandlers = new HashMap<>();

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

    @Inject
    protected Persistence persistence;

    private Log log = LogFactory.getLog(DbUpdaterImpl.class);

    @Inject
    public void setConfigProvider(Configuration configuration) {
        String dbDirName = configuration.getConfig(ServerConfig.class).getDbDir();
        if (dbDirName != null)
            dbDir = new File(dbDirName);
    }

    @Override
    public void updateDatabase() {
        if (dbInitialized()) {
            doUpdate();
        } else {
            doInit();
        }
    }

    @Override
    public List<String> findUpdateDatabaseScripts() throws DBNotInitializedException {
        List<String> list = new ArrayList<>();
        if (dbInitialized()) {
            if (!changelogTableExists) {
                throw new DBNotInitializedException(
                        "Unable to determine required updates because SYS_DB_CHANGELOG table doesn't exist");
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

    protected boolean dbInitialized() {
        Connection connection = null;
        try {
            connection = persistence.getDataSource().getConnection();
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

    protected void doInit() {
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

    protected List<File> getUpdateScripts() {
        List<File> databaseScripts = new ArrayList<>();
        List<File> groovyScripts = new ArrayList<>();

        if (dbDir.exists()) {
            String[] moduleDirs = dbDir.list();
            Arrays.sort(moduleDirs);
            for (String moduleDirName : moduleDirs) {
                File moduleDir = new File(dbDir, moduleDirName);
                File initDir = new File(moduleDir, "update");
                File scriptDir = new File(initDir, persistence.getDbDialect().getName());
                if (scriptDir.exists()) {
                    Collection list = FileUtils.listFiles(scriptDir, null, true);
                    URI scriptDirUri = scriptDir.toURI();

                    List<File> sqlFiles = getScriptsByExtension(list, scriptDirUri, SQL_EXTENSION);
                    List<File> groovyFiles = getScriptsByExtension(list, scriptDirUri, GROOVY_EXTENSION);

                    databaseScripts.addAll(sqlFiles);
                    groovyScripts.addAll(groovyFiles);
                }
            }
        }
        databaseScripts.addAll(groovyScripts);
        return databaseScripts;
    }

    protected List<File> getScriptsByExtension(Collection files, final URI scriptDirUri, final String extension) {
        Collection scriptsCollection = CollectionUtils.select(files, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                File file = (File) object;
                return extension.equals(getFileExtension(file.getName()));
            }
        });
        List<File> scripts = new ArrayList<File>(scriptsCollection);
        Collections.sort(scripts, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                URI f1Uri = scriptDirUri.relativize(f1.toURI());
                URI f2Uri = scriptDirUri.relativize(f2.toURI());

                return f1Uri.getPath().compareTo(f2Uri.getPath());
            }
        });
        return scripts;
    }

    protected String getScriptName(File file) {
        try {
            String path = file.getCanonicalPath();
            String dir = dbDir.getCanonicalPath();
            return path.substring(dir.length() + 1).replace("\\", "/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doUpdate() {
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
    protected void prepareScripts() {
        List<File> updateFiles = getUpdateScripts();
        for (File file : updateFiles)
            markScript(getScriptName(file), true);
    }

    protected Set<String> getExecutedScripts() {
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
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

    protected void createChangelogTable() {
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        try {
            runner.update("create table SYS_DB_CHANGELOG(" +
                    "SCRIPT_NAME varchar(300) not null primary key, " +
                    "CREATE_TS " + (DbmsType.getCurrent().equals(DbmsType.MSSQL) ? "datetime" : "timestamp") + " default current_timestamp, " +
                    "IS_INIT integer default 0)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void executeSqlScript(File file) {
        String script;
        try {
            script = FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StrTokenizer tokenizer = new StrTokenizer(script,
                StrMatcher.charSetMatcher(persistence.getDbDialect().getScriptSeparator()),
                StrMatcher.singleQuoteMatcher()
        );
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
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

    protected void executeGroovyScript(File file) {
        Binding bind = new Binding();
        scripting.runGroovyScript(getScriptName(file), bind);
    }

    protected void executeScript(File file) {
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

    protected String getFileExtension(String filename) {
        int dotPos = filename.lastIndexOf(".");
        return filename.substring(dotPos + 1);
    }

    protected void markScript(String name, boolean init) {
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        try {
            runner.update("insert into SYS_DB_CHANGELOG (SCRIPT_NAME, IS_INIT) values (?, ?)",
                    new Object[]{name, init ? 1 : 0}
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<File> getInitScripts() {
        List<File> files = new ArrayList<>();
        if (dbDir.exists()) {
            String[] moduleDirs = dbDir.list();
            Arrays.sort(moduleDirs);
            for (String moduleDirName : moduleDirs) {
                File moduleDir = new File(dbDir, moduleDirName);
                File initDir = new File(moduleDir, "init");
                File scriptDir = new File(initDir, persistence.getDbDialect().getName());
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
