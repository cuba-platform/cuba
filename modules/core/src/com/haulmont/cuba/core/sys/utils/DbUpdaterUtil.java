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

package com.haulmont.cuba.core.sys.utils;

import com.haulmont.cuba.core.sys.DBNotInitializedException;
import com.haulmont.cuba.core.sys.dbupdate.DbUpdaterEngine;
import com.haulmont.cuba.core.sys.dbupdate.ScriptResource;
import org.apache.commons.cli.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 */
public class DbUpdaterUtil extends DbUpdaterEngine {

    private static final Logger log = LoggerFactory.getLogger(DbUpdaterUtil.class);

    private boolean executeGroovy = true;

    public static void main(String[] args) {
        String property = System.getProperty("logback.configurationFile");
        if (StringUtils.isBlank(property)) {
            System.setProperty("logback.configurationFile", "com/haulmont/cuba/core/sys/utils/dbutil-logback.xml");
        }

        DbUpdaterUtil runner = new DbUpdaterUtil();
        runner.execute(args);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public void execute(String[] args) {
        Options cliOptions = new Options();

        Option dbConnectionOption = OptionBuilder.withArgName("connectionString")
                .hasArgs()
                .withDescription("JDBC Database URL")
                .isRequired()
                .create("dbUrl");

        Option dbUserOption = OptionBuilder.withArgName("userName")
                .hasArgs()
                .withDescription("Database user")
                .isRequired()
                .create("dbUser");

        Option dbPasswordOption = OptionBuilder.withArgName("password")
                .hasArgs()
                .withDescription("Database password")
                .isRequired()
                .create("dbPassword");

        Option dbDriverClassOption = OptionBuilder.withArgName("driverClassName")
                .hasArgs()
                .withDescription("JDBC driver class name")
                .create("dbDriver");

        Option dbDirOption = OptionBuilder.withArgName("filePath")
                .hasArgs()
                .withDescription("Database scripts directory")
                .isRequired()
                .create("scriptsDir");

        Option dbTypeOption = OptionBuilder.withArgName("dbType")
                .hasArgs()
                .withDescription("DBMS type: postgres|mssql|oracle|etc")
                .isRequired()
                .create("dbType");

        Option dbVersionOption = OptionBuilder.withArgName("dbVersion")
                .hasArgs()
                .withDescription("DBMS version: 2012|etc")
                .create("dbVersion");

        Option dbExecuteGroovyOption = OptionBuilder.withArgName("executeGroovy").
                hasArgs().
                withDescription("Ignoring Groovy scripts").
                create("executeGroovy");

        Option showUpdatesOption = OptionBuilder
                .withDescription("Print update scripts")
                .create("check");

        Option applyUpdatesOption = OptionBuilder
                .withDescription("Update database")
                .create("update");

        Option createDbOption = OptionBuilder
                .withDescription("Create database")
                .create("create");

        cliOptions.addOption("help", false, "Print help");
        cliOptions.addOption(dbConnectionOption);
        cliOptions.addOption(dbUserOption);
        cliOptions.addOption(dbPasswordOption);
        cliOptions.addOption(dbDirOption);
        cliOptions.addOption(dbTypeOption);
        cliOptions.addOption(dbVersionOption);
        cliOptions.addOption(dbExecuteGroovyOption);
        cliOptions.addOption(showUpdatesOption);
        cliOptions.addOption(applyUpdatesOption);
        cliOptions.addOption(createDbOption);

        CommandLineParser parser = new PosixParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException exp) {
            formatter.printHelp("dbupdate", cliOptions);
            return;
        }

        if (cmd.hasOption("help") ||
                (!cmd.hasOption(showUpdatesOption.getOpt())) &&
                        !cmd.hasOption(applyUpdatesOption.getOpt()) &&
                        !cmd.hasOption(createDbOption.getOpt()))
            formatter.printHelp("dbupdate", cliOptions);
        else {
            this.dbScriptsDirectory = cmd.getOptionValue(dbDirOption.getOpt());
            File directory = new File(dbScriptsDirectory);
            if (!directory.exists()) {
                log.error("Not found db update directory");
                return;
            }

            dbmsType = cmd.getOptionValue(dbTypeOption.getOpt());
            dbmsVersion = StringUtils.trimToEmpty(cmd.getOptionValue(dbVersionOption.getOpt()));

            String dbDriver;
            if (!cmd.hasOption(dbDriverClassOption.getOpt())) {
                switch (dbmsType) {
                    case "postgres":
                        dbDriver = "org.postgresql.Driver";
                        break;
                    case "mssql":
                        dbDriver = "net.sourceforge.jtds.jdbc.Driver";
                        break;
                    case "oracle":
                        dbDriver = "oracle.jdbc.OracleDriver";
                        break;
                    default:
                        log.error("Unable to determine driver class name by DBMS type. Please provide driverClassName option");
                        return;
                }
            } else {
                dbDriver = cmd.getOptionValue(dbDriverClassOption.getOpt());
            }

            try {
                Class.forName(dbDriver);
            } catch (ClassNotFoundException e) {
                log.error("Unable to load driver class " + dbDriver);
                return;
            }

            String connectionStringParam = cmd.getOptionValue(dbConnectionOption.getOpt());
            try {
                this.dataSource = new SingleConnectionDataSource(
                        connectionStringParam,
                        cmd.getOptionValue(dbUserOption.getOpt()),
                        cmd.getOptionValue(dbPasswordOption.getOpt()));
            } catch (SQLException e) {
                log.error("Unable to connect to db: " + connectionStringParam);
                return;
            }

            if (cmd.hasOption(createDbOption.getOpt())) {
                // create database from init scripts
                StringBuilder availableScripts = new StringBuilder();
                for (ScriptResource initScript : getInitScripts()) {
                    availableScripts.append("\t").append(getScriptName(initScript)).append("\n");
                }
                log.info("Available create scripts: \n" + availableScripts);
                log.info(String.format("Do you want to create database %s ? [y/n]", connectionStringParam));
                Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
                if ("y".equals(scanner.next())) {
                    doInit();
                }
            } else {
                boolean updatesAvailable = false;
                try {
                    List<String> scripts;

                    executeGroovy = !(cmd.hasOption(dbExecuteGroovyOption.getOpt())
                            && cmd.getOptionValue(dbExecuteGroovyOption.getOpt()).equals("false"));

                    scripts = findUpdateDatabaseScripts();

                    if (!scripts.isEmpty()) {
                        StringBuilder availableScripts = new StringBuilder();
                        for (String script : scripts) {
                            availableScripts.append("\t").append(script).append("\n");
                        }
                        log.info("Available updates:\n" + availableScripts);
                        updatesAvailable = true;
                    } else
                        log.info("No available updates found for database");
                } catch (DBNotInitializedException e) {
                    log.warn("Database not initialized");
                    return;
                }

                if (updatesAvailable && cmd.hasOption(applyUpdatesOption.getOpt())) {
                    log.info(String.format("Do you want to apply updates to %s ? [y/n]", connectionStringParam));
                    Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
                    if ("y".equals(scanner.next())) {
                        doUpdate();
                    }
                }
            }
        }
    }

    @Override
    protected List<ScriptResource> getUpdateScripts() {
        if (executeGroovy) {
            return super.getUpdateScripts();
        } else {
            final List<ScriptResource> files = new ArrayList<>(super.getUpdateScripts());

            CollectionUtils.filter(files, object -> {
                File file = ((File) object);
                return !(file.getName().endsWith("groovy"));
            });

            return files;
        }
    }

    private static class SingleConnectionDataSource implements DataSource {

        private String url;
        private String user;
        private String password;

        private SingleConnectionDataSource(String url, String user, String password) throws SQLException {
            this.url = url;
            this.user = user;
            this.password = password;
        }

        @Override
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(url, user, password);
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return DriverManager.getConnection(url, username, password);
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return new PrintWriter(System.out);
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {
        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {
        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
            throw new SQLFeatureNotSupportedException();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }
    }
}