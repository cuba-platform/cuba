/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.db.DbUtils;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrMatcher;
import org.apache.commons.lang.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class DbUpdaterEngine implements DbUpdater {

    private static final String SQL_EXTENSION = "sql";
    private static final String SQL_COMMENT_PREFIX = "--";
    private static final String SQL_DELIMITER = "^";

    private static final String GROOVY_EXTENSION = "groovy";

    private static final Logger log = LoggerFactory.getLogger(DbUpdaterEngine.class);

    protected DataSource dataSource;

    protected File dbDir;
    protected String dbmsType;
    protected String dbmsVersion;

    protected boolean changelogTableExists = false;

    protected final Map<String, String> requiredTables = new HashMap<>();
    {
        requiredTables.put("reports", "report_report");
        requiredTables.put("workflow", "wf_proc");
        requiredTables.put("ccpayments", "cc_credit_card");
        requiredTables.put("bpm", "bpm_proc_definition");
    }

    // register handlers for script files
    protected final Map<String, FileHandler> extensionHandlers = new HashMap<>();
    {
        extensionHandlers.put(SQL_EXTENSION, new FileHandler() {
            @Override
            public boolean run(File file) {
                return executeSqlScript(file);
            }
        });
        extensionHandlers.put(GROOVY_EXTENSION, new FileHandler() {
            @Override
            public boolean run(File file) {
                return executeGroovyScript(file);
            }
        });
    }

    protected DbUpdaterEngine() {
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    protected void createChangelogTable() {
        String timeStampType = DbmsSpecificFactory.getDbmsFeatures().getTimeStampType();
        QueryRunner runner = new QueryRunner(getDataSource());
        try {
            runner.update("create table SYS_DB_CHANGELOG(" +
                    "SCRIPT_NAME varchar(300) not null primary key, " +
                    "CREATE_TS " + timeStampType + " default current_timestamp, " +
                    "IS_INIT integer default 0)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<File> getUpdateScripts() {
        return getUpdateScripts(null);
    }

    protected List<File> getUpdateScripts(@Nullable String oneModuleDir) {
        List<File> databaseScripts = new ArrayList<>();
        List<File> additionalScripts = new ArrayList<>();

        String[] extensions = extensionHandlers.keySet().toArray(new String[extensionHandlers.size()]);

        if (dbDir.exists()) {
            String[] moduleDirs = dbDir.list();
            Arrays.sort(moduleDirs);
            for (String moduleDirName : moduleDirs) {
                if (oneModuleDir != null && !oneModuleDir.equals(moduleDirName))
                    continue;
                File moduleDir = new File(dbDir, moduleDirName);
                File initDir = new File(moduleDir, "update");
                File scriptDir = new File(initDir, dbmsType);
                if (scriptDir.exists()) {
                    //noinspection unchecked
                    List<File> list = new ArrayList(FileUtils.listFiles(scriptDir, extensions, true));
                    final Map<File, File> file2dir = new HashMap<>();
                    for (File file : list) {
                        file2dir.put(file, scriptDir);
                    }

                    if (!StringUtils.isEmpty(dbmsVersion)) {
                        File optScriptDir = new File(initDir, dbmsType + "-" + dbmsVersion);
                        if (optScriptDir.exists()) {
                            Map<String, File> filesMap = new HashMap<>();
                            for (File file : list) {
                                filesMap.put(scriptDir.toPath().relativize(file.toPath()).toString(), file);
                            }

                            //noinspection unchecked
                            Collection<File> optList = FileUtils.listFiles(optScriptDir, extensions, true);
                            for (File optFile : optList) {
                                file2dir.put(optFile, optScriptDir);
                            }
                            Map<String, File> optFilesMap = new HashMap<>();
                            for (File optFile : optList) {
                                optFilesMap.put(optScriptDir.toPath().relativize(optFile.toPath()).toString(), optFile);
                            }

                            filesMap.putAll(optFilesMap);
                            list.clear();
                            list.addAll(filesMap.values());
                        }
                    }

                    Collections.sort(list, new Comparator<File>() {
                        @Override
                        public int compare(File f1, File f2) {
                            File f1Parent = f1.getAbsoluteFile().getParentFile();
                            File f2Parent = f2.getAbsoluteFile().getParentFile();
                            if (f1Parent.equals(f2Parent)) {
                                String f1Name = FilenameUtils.getBaseName(f1.getName());
                                String f2Name = FilenameUtils.getBaseName(f2.getName());
                                return f1Name.compareTo(f2Name);
                            }
                            File dir1 = file2dir.get(f1);
                            File dir2 = file2dir.get(f2);
                            Path p1 = dir1.toPath().relativize(f1.toPath());
                            Path p2 = dir2.toPath().relativize(f2.toPath());
                            return p1.compareTo(p2);
                        }
                    });

                    databaseScripts.addAll(list);
                }
            }
        }
        databaseScripts.addAll(additionalScripts);
        return databaseScripts;
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

    @Override
    public void updateDatabase() {
        if (dbInitialized())
            doUpdate();
        else
            doInit();
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
                        list.add(name);
                    }
                }
            }
        } else {
            throw new DBNotInitializedException(
                    "Unable to determine required updates because SEC_USER table doesn't exist");
        }
        return list;
    }

    protected boolean dbInitialized() {
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();
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
        try {
            for (File file : initFiles) {
                executeScript(file);
                markScript(getScriptName(file), true);
            }
        } finally {
            prepareScripts();
        }

        log.info("Database initialized");
    }

    protected void doUpdate() {
        log.info("Updating database...");

        if (!changelogTableExists) {
            log.info("Changelog table not found, creating it and marking all scripts as executed");

            createChangelogTable();

            List<File> initFiles = getInitScripts();
            try {
                for (File file : initFiles) {
                    markScript(getScriptName(file), true);
                }
            } finally {
                prepareScripts();
            }

            return;
        }

        runRequiredInitScripts();

        List<File> files = getUpdateScripts();
        Set<String> scripts = getExecutedScripts();
        for (File file : files) {
            String name = getScriptName(file);
            if (!scripts.contains(name)) {
                if (executeScript(file)) {
                    markScript(name, false);
                }
            }
        }
        log.info("Database is up-to-date");
    }

    protected void runRequiredInitScripts() {
        for (String dirName : getModuleDirs()) {
            String moduleName = dirName.substring(3);
            String reqTable = requiredTables.get(moduleName);
            if (reqTable != null) {
                try {
                    executeSql("select * from " + reqTable + " where 0=1");
                } catch (SQLException e) {
                    String mark = dbmsType.equals("oracle") ? "ora-00942" : reqTable;
                    if (e.getMessage() != null && e.getMessage().toLowerCase().contains(mark)) {
                        // probably the required table does not exist
                        log.info("Required table for " + moduleName + " does not exist, running init scripts");
                        List<File> initScripts = getInitScripts(dirName);
                        try {
                            for (File file : initScripts) {
                                executeScript(file);
                                markScript(getScriptName(file), true);
                            }
                        } finally {
                            List<File> updateFiles = getUpdateScripts(dirName);
                            for (File file : updateFiles)
                                markScript(getScriptName(file), true);
                        }
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
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
        QueryRunner runner = new QueryRunner(getDataSource());
        try {
            //noinspection UnnecessaryLocalVariable
            Set<String> scripts = runner.query("select SCRIPT_NAME from SYS_DB_CHANGELOG",
                    new ResultSetHandler<Set<String>>() {
                        @Override
                        public Set<String> handle(ResultSet rs) throws SQLException {
                            Set<String> rows = new HashSet<>();
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

    protected void markScript(String name, boolean init) {
        QueryRunner runner = new QueryRunner(getDataSource());
        try {
            runner.update("insert into SYS_DB_CHANGELOG (SCRIPT_NAME, IS_INIT) values (?, ?)",
                    new Object[]{name, init ? 1 : 0}
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean isEmpty(String sql) {
        String[] lines = sql.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.startsWith(SQL_COMMENT_PREFIX) && !StringUtils.isBlank(line)) {
                return false;
            }
        }
        return true;
    }

    protected boolean executeSqlScript(File file) {
        String script;
        try {
            script = FileUtils.readFileToString(file);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        StrTokenizer tokenizer = new StrTokenizer(script,
                StrMatcher.charSetMatcher(SQL_DELIMITER),
                StrMatcher.singleQuoteMatcher()
        );
        while (tokenizer.hasNext()) {
            String sql = tokenizer.nextToken().trim();
            if (!isEmpty(sql)) {
                log.debug("Executing SQL:\n" + sql);
                try {
                    executeSql(sql);
                } catch (SQLException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    protected void executeSql(String sql) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getDataSource().getConnection();
            statement = connection.createStatement();
            statement.execute(sql);
        } finally {
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    protected boolean executeGroovyScript(File file) {
        try {
            String scriptRoot = file.getParentFile().getAbsolutePath();
            ClassLoader classLoader = getClass().getClassLoader();
            GroovyScriptEngine scriptEngine = new GroovyScriptEngine(scriptRoot, classLoader);

            Binding bind = new Binding();
            bind.setProperty("ds", getDataSource());
            bind.setProperty("log", LoggerFactory.getLogger(file.getName()));
            bind.setProperty("postUpdate", new PostUpdateScripts() {
                @Override
                public void add(Closure closure) {
                    super.add(closure);

                    log.warn("Added post update action will be ignored");
                }
            });

            scriptEngine.run(file.getName(), bind);
        } catch (IOException | ResourceException | ScriptException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return true;
    }

    protected boolean executeScript(File file) {
        log.info("Executing script " + getScriptName(file));
        String filename = file.getName();
        String extension = FilenameUtils.getExtension(filename);
        if (StringUtils.isNotEmpty(extension)) {
            if (extensionHandlers.containsKey(extension)) {
                FileHandler handler = extensionHandlers.get(extension);
                return handler.run(file);
            } else
                log.warn("Update script ignored, file handler for extension not found:" +
                        file.getName());
        } else
            log.warn("Update script ignored, file extension undefined:" + file.getName());
        return false;
    }

    protected List<String> getModuleDirs() {
        if (dbDir.exists()) {
            String[] moduleDirs = dbDir.list();
            Arrays.sort(moduleDirs);
            return Arrays.asList(moduleDirs);
        }
        return Collections.emptyList();
    }

    protected List<File> getInitScripts() {
        return getInitScripts(null);
    }

    protected List<File> getInitScripts(@Nullable String oneModuleDir) {
        List<File> files = new ArrayList<>();
        if (dbDir.exists()) {
            String[] moduleDirs = dbDir.list();
            Arrays.sort(moduleDirs);
            for (String moduleDirName : moduleDirs) {
                if (oneModuleDir != null && !oneModuleDir.equals(moduleDirName))
                    continue;
                File moduleDir = new File(dbDir, moduleDirName);
                File initDir = new File(moduleDir, "init");
                File scriptDir = new File(initDir, dbmsType);
                if (scriptDir.exists()) {
                    FilenameFilter filenameFilter = new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith("create-db.sql");
                        }
                    };
                    File[] scriptFiles = scriptDir.listFiles(filenameFilter);

                    List<File> list = new ArrayList<>(Arrays.asList(scriptFiles));

                    if (!StringUtils.isEmpty(dbmsVersion)) {
                        File optScriptDir = new File(initDir, dbmsType + "-" + dbmsVersion);
                        if (optScriptDir.exists()) {
                            File[] optFiles = optScriptDir.listFiles(filenameFilter);

                            Map<String, File> filesMap = new HashMap<>();
                            for (File file : scriptFiles) {
                                filesMap.put(scriptDir.toPath().relativize(file.toPath()).toString(), file);
                            }

                            Map<String, File> optFilesMap = new HashMap<>();
                            for (File optFile : optFiles) {
                                optFilesMap.put(optScriptDir.toPath().relativize(optFile.toPath()).toString(), optFile);
                            }

                            filesMap.putAll(optFilesMap);
                            list.clear();
                            list.addAll(filesMap.values());
                        }
                    }

                    Collections.sort(list, new Comparator<File>() {
                        @Override
                        public int compare(File f1, File f2) {
                            return f1.getName().compareTo(f2.getName());
                        }
                    });
                    files.addAll(list);
                }
            }
        }
        return files;
    }

    // File extension handler
    public interface FileHandler {
        /**
         * @return need mark as executed or not
         */
        boolean run(File file);
    }
}