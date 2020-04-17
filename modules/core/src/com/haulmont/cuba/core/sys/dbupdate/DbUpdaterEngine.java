/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys.dbupdate;

import com.google.common.collect.ImmutableList;
import com.haulmont.bali.db.DbUtils;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.DbInitializationException;
import com.haulmont.cuba.core.sys.DbUpdater;
import com.haulmont.cuba.core.sys.PostUpdateScripts;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.haulmont.cuba.core.global.Stores.storeNameToString;

public class DbUpdaterEngine implements DbUpdater {
    private static final String SQL_EXTENSION = "sql";
    private static final String SQL_COMMENT_PREFIX = "--";
    private static final String SQL_DELIMITER = "^";

    private static final String GROOVY_EXTENSION = "groovy";
    protected static final String UPGRADE_GROOVY_EXTENSION = "upgrade.groovy";

    protected static final Pattern RESTAPI_REGEX = Pattern.compile("^\\d+-restapi.*$");

    protected static final List<Pattern> EXCLUDED_ADDONS = ImmutableList.of(RESTAPI_REGEX);

    protected static final String ERROR = "\n" +
            "=================================================\n" +
            "ERROR: Data store update failed. See details below.\n" +
            "=================================================\n";

    protected static final Logger log = LoggerFactory.getLogger(DbUpdaterEngine.class);

    protected DataSource dataSource;

    protected String dbScriptsDirectory;
    protected String storeName = Stores.MAIN;
    protected String dbmsType;
    protected String dbmsVersion;

    protected boolean changelogTableExists = false;
    protected boolean scriptsExists = false;

    // register handlers for script files
    protected final Map<String, FileHandler> extensionHandlers = new HashMap<>();

    {
        extensionHandlers.put(SQL_EXTENSION, this::executeSqlScript);
        extensionHandlers.put(GROOVY_EXTENSION, this::executeGroovyScript);
    }

    protected DbUpdaterEngine() {
    }

    protected DataSource getDataSource() {
        return dataSource;
    }

    public List<ScriptResource> getScripts(ScriptType scriptType, @Nullable String moduleName) {
        List<ScriptResource> scripts = scriptScanner().getScripts(scriptType, moduleName);
        log.trace("Found {} scripts for data store [{}]: {}", scriptType, storeNameToString(storeName), scripts);
        return scripts;
    }

    @Override
    public void updateDatabase() throws DbInitializationException {
        if (getDataSource() != null) {
            if (dbInitialized()) {
                if (scriptsExists) {
                    doUpdate();
                }
            } else {
                doInit();
            }
        }
    }

    @Override
    public List<String> findUpdateDatabaseScripts() throws DbInitializationException {
        if (dbInitialized()) {
            if (!scriptsExists) {
                return Collections.emptyList();
            }
            if (!changelogTableExists) {
                throw new DbInitializationException(
                        "Unable to determine required updates because SYS_DB_CHANGELOG table doesn't exist");
            } else {
                List<String> list = new ArrayList<>();
                List<ScriptResource> files = getUpdateScripts();
                Set<String> scripts = getExecutedScripts();
                for (ScriptResource file : files) {
                    String name = getScriptName(file);
                    if (!containsIgnoringPrefix(scripts, name)) {
                        list.add(name);
                    }
                }
                return list;
            }
        } else {
            throw new DbInitializationException("Unable to determine required updates");
        }
    }

    protected ScriptScanner scriptScanner() {
        return new ScriptScanner(dbScriptsDirectory, storeName, dbmsType, dbmsVersion);
    }

    protected String dbScriptDirectoryPath() {
        return scriptScanner().dbScriptDirectoryPath();
    }

    protected void createChangelogTable() {
        log.trace("Creating SYS_DB_CHANGELOG table for data store [{}]", storeNameToString(storeName));
        String timeStampType = DbmsSpecificFactory.getDbmsFeatures().getTimeStampType();
        QueryRunner runner = new QueryRunner(getDataSource());
        try {
            int pkLength = "mysql".equals(dbmsType) ? 255 : 300;
            runner.update("create table SYS_DB_CHANGELOG(" +
                    "SCRIPT_NAME varchar(" + pkLength + ") not null primary key, " +
                    "CREATE_TS " + timeStampType + " default current_timestamp, " +
                    "IS_INIT integer default 0)");
        } catch (SQLException e) {
            throw new RuntimeException(ERROR + String.format("Error creating changelog table for data store [%s]", storeNameToString(storeName)), e);
        }
    }

    protected String getScriptName(ScriptResource resource) {
        String path = resource.getPath();
        return getScriptName(path);
    }

    protected String getScriptName(String path) {
        String dir = dbScriptDirectoryPath();
        path = path.replace("\\", "/");
        int indexOfDir = path.indexOf(dir);
        return path.substring(indexOfDir + dir.length() + 1).replaceAll("^/+", "");
    }

    public boolean dbInitialized() throws DbInitializationException {
        log.trace("Checking if the data store [{}] is initialized", storeNameToString(storeName));

        DataSource dataSource = getDataSource();
        if (dataSource == null) {
            log.trace("Database pool isn't initialized for data store [{}], so data store is initialized", storeNameToString(storeName));
            return true;
        }

        List<ScriptResource> initScripts = getInitScripts();
        List<ScriptResource> updateScripts = getUpdateScripts();
        if (initScripts.isEmpty() && updateScripts.isEmpty()) {
            log.trace("Init/Update scripts folder is empty for data store [{}], so data store is initialized", storeNameToString(storeName));
            return true;
        }
        scriptsExists = true;

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            DbProperties dbProperties = new DbProperties(getConnectionUrl(connection));
            boolean isSchemaByUser = DbmsSpecificFactory.getDbmsFeatures().isSchemaByUser();
            boolean isRequiresCatalog = DbmsSpecificFactory.getDbmsFeatures().isRequiresDbCatalogName();
            String schemaName = isSchemaByUser ?
                    dbMetaData.getUserName() : dbProperties.getCurrentSchemaProperty();
            String catalogName = isRequiresCatalog ? connection.getCatalog() : null;
            ResultSet tables = dbMetaData.getTables(catalogName, schemaName, "%", null);

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if ("SYS_DB_CHANGELOG".equalsIgnoreCase(tableName)) {
                    log.trace("Found SYS_DB_CHANGELOG table");
                    changelogTableExists = true;
                }
            }

            return changelogTableExists;
        } catch (SQLException e) {
            throw new DbInitializationException(true,
                    String.format("Error connecting to data store [%s]: %s", storeNameToString(storeName), e.getMessage()), e);
        }
    }

    protected void doInit() {
        List<ScriptResource> initFiles = getInitScripts();

        log.info("Initializing data store [{}]", storeNameToString(storeName));

        if (!changelogTableExists) {
            createChangelogTable();
        }

        try {
            for (ScriptResource file : initFiles) {
                executeScript(file);
                markScript(getScriptName(file), true);
            }
        } finally {
            prepareScripts();
        }

        log.info("Data store [{}] initialized", storeNameToString(storeName));
    }

    protected void doUpdate() {

        log.info("Updating data store [{}] ...", storeNameToString(storeName));

        if (!changelogTableExists) {
            log.info("Changelog table not found for data store [{}], creating it", storeNameToString(storeName));
            createChangelogTable();
        }

        runRequiredInitScripts();

        log.trace("Checking existing and executed update scripts for data store [{}]", storeNameToString(storeName));
        List<ScriptResource> files = getUpdateScripts();
        Set<String> scripts = getExecutedScripts();
        for (ScriptResource file : files) {
            String name = getScriptName(file);
            if (!containsIgnoringPrefix(scripts, name)) {
                if (executeScript(file)) {
                    markScript(name, false);
                }
            }
        }
        log.info("Data store [{}] is up-to-date", storeNameToString(storeName));
    }

    protected void runRequiredInitScripts() {
        log.trace("Checking executed init scripts for components. Data store [{}]", storeNameToString(storeName));
        Set<String> executedScripts = getExecutedScripts();
        List<String> dirs = getModuleDirs();
        if (dirs.size() > 1) {
            // check all db folders except the last because it is the folder of the app and we need only components
            for (String dirName : dirs.subList(0, dirs.size() - 1)) {
                List<ScriptResource> initScripts = getInitScripts(dirName)
                        .stream()
                        .filter(this::filterInitScript)
                        .collect(Collectors.toList());

                if (!initScripts.isEmpty()) {
                    boolean anInitScriptHasBeenExecuted = false;
                    for (ScriptResource initScript : initScripts) {
                        String initScriptName = getScriptName(initScript);
                        log.trace("Checking script {}", initScriptName);
                        if (containsIgnoringPrefix(executedScripts, initScriptName)) {
                            anInitScriptHasBeenExecuted = true;
                            break;
                        }
                    }
                    if (!anInitScriptHasBeenExecuted && !initializedByOwnScript(executedScripts, dirName)) {
                        log.info("No init scripts from " + dirName + " have been executed, running init scripts for " + distinguishingSubstring(dirName));
                        try {
                            for (ScriptResource file : initScripts) {
                                executeScript(file);
                                markScript(getScriptName(file), true);
                            }
                        } finally {
                            List<ScriptResource> updateFiles = getUpdateScripts(dirName);
                            for (ScriptResource file : updateFiles)
                                markScript(getScriptName(file), true);
                        }
                    }
                }
            }
        }
    }

    protected boolean filterInitScript(ScriptResource scriptResource) {
        return EXCLUDED_ADDONS.stream()
                .noneMatch(pattern ->
                        pattern.matcher(getScriptName(scriptResource)).matches());
    }

    protected boolean initializedByOwnScript(Set<String> executedScripts, String dirName) {
        boolean found = executedScripts.stream()
                .anyMatch(s -> s.substring(s.lastIndexOf('/') + 1).equalsIgnoreCase("01." + dirName.substring(3) + "-create-db.sql"));
        if (found)
            log.debug("Found executed '01." + dirName.substring(3) + "-create-db.sql' script");
        return found;
    }

    protected boolean containsIgnoringPrefix(Collection<String> strings, String s) {
        return strings.stream()
                .anyMatch(it -> distinguishingSubstring(it).equals(distinguishingSubstring(s)));
    }

    protected String distinguishingSubstring(String scriptName) {
        int substringStart = scriptName.indexOf("-") + 1;
        return scriptName.length() > substringStart ? scriptName.substring(substringStart) : scriptName;
    }

    /**
     * Mark all SQL updates scripts as evaluated
     * Try to execute Groovy scripts
     */
    protected void prepareScripts() {
        List<ScriptResource> updateFiles = getUpdateScripts();
        for (ScriptResource file : updateFiles)
            markScript(getScriptName(file), true);
    }

    protected Set<String> getExecutedScripts() {
        QueryRunner runner = new QueryRunner(getDataSource());
        try {
            Set<String> scripts = runner.query("select SCRIPT_NAME from SYS_DB_CHANGELOG",
                    rs -> {
                        Set<String> rows = new HashSet<>();
                        while (rs.next()) {
                            rows.add(rs.getString(1));
                        }
                        return rows;
                    });
            log.trace("Found executed scripts: {}", scripts);
            return scripts;
        } catch (SQLException e) {
            throw new RuntimeException(ERROR + "Error loading executed scripts", e);
        }
    }

    protected void markScript(String name, boolean init) {
        log.trace("Marking script as executed: {}", name);
        QueryRunner runner = new QueryRunner(getDataSource());
        try {
            runner.update("insert into SYS_DB_CHANGELOG (SCRIPT_NAME, IS_INIT) values (?, ?)",
                    new Object[]{name, init ? 1 : 0}
            );
        } catch (SQLException e) {
            throw new RuntimeException(ERROR + "Error updating SYS_DB_CHANGELOG", e);
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

    protected boolean executeSqlScript(ScriptResource file) {
        String script;
        try {
            script = file.getContent();
        } catch (IOException e) {
            throw new RuntimeException(ERROR + "Error resolving SQL script " + file.name, e);
        }
        ScriptSplitter splitter = new ScriptSplitter(SQL_DELIMITER);
        for (String sql : splitter.split(script)) {
            if (!isEmpty(sql)) {
                log.debug("Executing SQL:\n" + sql);
                try {
                    executeSql(sql);
                } catch (SQLException e) {
                    throw new RuntimeException(ERROR + "Error executing SQL script " + file.name + "\n" + e.getMessage(), e);
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

    protected boolean executeGroovyScript(ScriptResource file) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            CompilerConfiguration cc = new CompilerConfiguration();
            cc.setRecompileGroovySource(true);

            Binding bind = new Binding();
            bind.setProperty("ds", getDataSource());
            bind.setProperty("log", LoggerFactory.getLogger(String.format("%s$%s", DbUpdaterEngine.class.getName(),
                    StringUtils.removeEndIgnoreCase(file.getName(), ".groovy"))));
            if (!StringUtils.endsWithIgnoreCase(file.getName(), "." + UPGRADE_GROOVY_EXTENSION)) {
                bind.setProperty("postUpdate", new PostUpdateScripts() {
                    @Override
                    public void add(Closure closure) {
                        super.add(closure);

                        log.warn("Added post update action will be ignored for data store [{}]", storeNameToString(storeName));
                    }
                });
            }

            GroovyShell shell = new GroovyShell(classLoader, bind, cc);
            Script script = shell.parse(file.getContent());
            script.run();
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("%sError executing Groovy script %s\n%s", ERROR, file.name, e.getMessage()), e);
        }
        return true;
    }

    protected boolean executeScript(ScriptResource file) {
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
        return scriptScanner().getModuleDirs();
    }

    protected List<ScriptResource> getInitScripts() {
        return getModuleDirs().stream()
                .flatMap(name -> getInitScripts(name).stream())
                .collect(Collectors.toList());
    }

    protected List<ScriptResource> getInitScripts(@Nullable String oneModuleDir) {
        return getScripts(ScriptType.INIT, oneModuleDir);
    }

    protected List<ScriptResource> getUpdateScripts() {
        return getModuleDirs().stream()
                .flatMap(name -> getUpdateScripts(name).stream())
                .collect(Collectors.toList());
    }

    protected List<ScriptResource> getUpdateScripts(@Nullable String oneModuleDir) {
        return getScripts(ScriptType.UPDATE, oneModuleDir);
    }


    protected String getConnectionUrl(Connection connection) {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            return databaseMetaData.getURL();
        } catch (Throwable e) {
            log.warn("Unable to get connection url for data store [{}]", storeNameToString(storeName));
            return null;
        }
    }

    // File extension handler
    public interface FileHandler {
        /**
         * @return need mark as executed or not
         */
        boolean run(ScriptResource file);
    }

    public static class ScriptSplitter {

        private String delimiter;

        public ScriptSplitter(String delimiter) {
            this.delimiter = delimiter;
        }

        public List<String> split(java.lang.String script) {
            String qd = Pattern.quote(delimiter);
            String[] commands = script.split("(?<!" + qd + ")" + qd + "(?!" + qd + ")"); // regex for ^: (?<!\^)\^(?!\^)
            return Arrays.stream(commands)
                    .map(s -> s.replace(delimiter + delimiter, delimiter))
                    .collect(Collectors.toList());
        }
    }
}