/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.utils;

import com.haulmont.cuba.core.global.MssqlDbDialect;
import com.haulmont.cuba.core.global.OracleDbDialect;
import com.haulmont.cuba.core.global.PostgresDbDialect;
import com.haulmont.cuba.core.sys.DBNotInitializedException;
import com.haulmont.cuba.core.sys.DbUpdaterEngine;
import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author artamonov
 * @version $Id$
 */
public class DbUpdaterUtil extends DbUpdaterEngine {

    private static Log log = LogFactory.getLog(DbUpdaterEngine.class);

    public static void main(String[] args) {
        DOMConfigurator.configure(DbUpdaterUtil.class.getResource("/com/haulmont/cuba/core/sys/utils/dbutil-log4j.xml"));

        DbUpdaterUtil runner = new DbUpdaterUtil();
        runner.execute(args);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public void execute(String[] args) {
        Options cliOptions = new Options();

        Option dbConnectionOption = OptionBuilder.withArgName("connectionString")
                .hasArgs()
                .withDescription("JDBC Database Url")
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

        Option dbDialectOption = OptionBuilder.withArgName("dbDialect")
                .hasArgs()
                .withDescription("Database dialect: postgres|mssql|oracle")
                .isRequired()
                .create("dialect");

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
        cliOptions.addOption(dbDialectOption);
        cliOptions.addOption(showUpdatesOption);
        cliOptions.addOption(applyUpdatesOption);
        cliOptions.addOption(createDbOption);

        CommandLineParser parser = new PosixParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            // parse the command line arguments
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException exp) {
            // oops, something went wrong
            formatter.printHelp("dbupdate", cliOptions);
            return;
        }

        if (cmd.hasOption("help") ||
                (!cmd.hasOption(showUpdatesOption.getOpt())) &&
                        !cmd.hasOption(applyUpdatesOption.getOpt()) &&
                        !cmd.hasOption(createDbOption.getOpt()))
            formatter.printHelp("dbupdate", cliOptions);
        else {
            String dbDirParam = cmd.getOptionValue(dbDirOption.getOpt());
            this.dbDir = new File(dbDirParam);
            if (!this.dbDir.exists()) {
                log.fatal("Not found db update directory");
                return;
            }

            String dbDialectParam = cmd.getOptionValue(dbDialectOption.getOpt());

            switch (dbDialectParam) {
                case "postgres":
                    this.dbDialect = new PostgresDbDialect();
                    break;
                case "mssql":
                    this.dbDialect = new MssqlDbDialect();
                    break;
                case "oracle":
                    this.dbDialect = new OracleDbDialect();
                    break;
                default:
                    log.fatal("Unable to determine db dialect");
                    return;
            }

            String dbDriver;
            if (!cmd.hasOption(dbDriverClassOption.getOpt())) {
                switch (dbDialectParam) {
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
                        log.fatal("Unable to determine driver class name by db dialect");
                        return;
                }
            } else {
                dbDriver = cmd.getOptionValue(dbDriverClassOption.getOpt());
            }

            try {
                Class.forName(dbDriver);
            } catch (ClassNotFoundException e) {
                log.fatal("Unable to load driver class " + dbDriver);
                return;
            }

            String connectionStringParam = cmd.getOptionValue(dbConnectionOption.getOpt());
            try {
                this.dataSource = new SingleConnectionDataSource(
                        connectionStringParam,
                        cmd.getOptionValue(dbUserOption.getOpt()),
                        cmd.getOptionValue(dbPasswordOption.getOpt()));
            } catch (SQLException e) {
                log.fatal("Unable to connect to db: " + connectionStringParam);
                return;
            }

            if (cmd.hasOption(createDbOption.getOpt())) {
                // create database from init scripts
                StringBuilder availableScripts = new StringBuilder();
                for (File initScript : getInitScripts()) {
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
                    List<String> scripts = findUpdateDatabaseScripts();
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
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
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