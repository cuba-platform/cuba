/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.uberjar;

import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.security.CodeSource;
import java.util.Properties;

import static com.haulmont.cuba.uberjar.CubaJettyUtils.*;
import static java.lang.String.format;

public class ServerRunner {


    public static void main(String[] args) {
        ServerRunner runner = new ServerRunner();
        runner.execute(args);
    }

    protected void execute(String[] args) {
        Options cliOptions = new Options();

        Option portOption = Option.builder("port")
                .hasArg()
                .desc("server port").argName("port").build();

        Option contextPathOption = Option.builder("contextName")
                .hasArg()
                .desc("application context name").argName("contextName").build();

        Option frontContextPathOption = Option.builder("frontContextName")
                .hasArg()
                .desc("front application context name").argName("frontContextName").build();

        Option portalContextPathOption = Option.builder("portalContextName")
                .hasArg()
                .desc("portal application context name for single jar application").argName("portalContextName").build();

        Option jettyEnvPathOption = Option.builder("jettyEnvPath")
                .hasArg()
                .desc("jetty resource xml path").argName("jettyEnvPath").build();

        Option helpOption = Option.builder("help")
                .desc("print help information").build();

        cliOptions.addOption(helpOption);
        cliOptions.addOption(portOption);
        cliOptions.addOption(contextPathOption);
        cliOptions.addOption(frontContextPathOption);
        cliOptions.addOption(portalContextPathOption);
        cliOptions.addOption(jettyEnvPathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException exp) {
            printHelp(formatter, cliOptions);
            return;
        }

        if (cmd.hasOption("help"))
            printHelp(formatter, cliOptions);
        else {
            CubaJettyServer jettyServer = new CubaJettyServer();
            if (cmd.hasOption(portOption.getOpt())) {
                try {
                    jettyServer.setPort(Integer.parseInt(cmd.getOptionValue(portOption.getOpt())));
                } catch (NumberFormatException e) {
                    System.out.println("port has to be number");
                    printHelp(formatter, cliOptions);
                    return;
                }
            } else {
                jettyServer.setPort(getDefaultWebPort());
            }
            String contextPath = null;
            String frontContextPath = null;
            String portalContextPath = null;
            if (cmd.hasOption(contextPathOption.getOpt())) {
                String contextName = cmd.getOptionValue(contextPathOption.getOpt());
                if (contextName != null && !contextName.isEmpty()) {
                    if (PATH_DELIMITER.equals(contextName)) {
                        contextPath = PATH_DELIMITER;
                    } else {
                        contextPath = PATH_DELIMITER + contextName;
                    }
                }
            }
            if (cmd.hasOption(frontContextPathOption.getOpt())) {
                String contextName = cmd.getOptionValue(frontContextPathOption.getOpt());
                if (contextName != null && !contextName.isEmpty()) {
                    if (PATH_DELIMITER.equals(contextName)) {
                        frontContextPath = PATH_DELIMITER;
                    } else {
                        frontContextPath = PATH_DELIMITER + contextName;
                    }
                }
            }

            if (cmd.hasOption(portalContextPathOption.getOpt())) {
                String contextName = cmd.getOptionValue(portalContextPathOption.getOpt());
                if (contextName != null && !contextName.isEmpty()) {
                    if (PATH_DELIMITER.equals(contextName)) {
                        portalContextPath = PATH_DELIMITER;
                    } else {
                        portalContextPath = PATH_DELIMITER + contextName;
                    }
                }
            }

            if (contextPath == null) {
                String jarName = getJarName();
                if (jarName != null) {
                    jettyServer.setContextPath(PATH_DELIMITER + FilenameUtils.getBaseName(jarName));
                } else {
                    jettyServer.setContextPath(PATH_DELIMITER);
                }
            } else {
                jettyServer.setContextPath(contextPath);
            }
            if (frontContextPath == null) {
                if (PATH_DELIMITER.equals(contextPath)) {
                    jettyServer.setFrontContextPath(PATH_DELIMITER + "app-front");
                } else {
                    jettyServer.setFrontContextPath(jettyServer.getContextPath() + "-front");
                }
            } else {
                jettyServer.setFrontContextPath(frontContextPath);
            }
            if (portalContextPath == null) {
                if (PATH_DELIMITER.equals(contextPath)) {
                    jettyServer.setPortalContextPath(PATH_DELIMITER + "app-portal");
                } else {
                    jettyServer.setPortalContextPath(jettyServer.getContextPath() + "-portal");
                }
            } else {
                jettyServer.setPortalContextPath(frontContextPath);
            }

            if (cmd.hasOption(jettyEnvPathOption.getOpt())) {
                String jettyEnvPath = cmd.getOptionValue(jettyEnvPathOption.getOpt());
                if (jettyEnvPath != null && !jettyEnvPath.isEmpty()) {
                    File file = new File(jettyEnvPath);
                    if (!file.exists()) {
                        System.out.println("jettyEnvPath should point to an existing file");
                        printHelp(formatter, cliOptions);
                        return;
                    }
                    try {
                        jettyServer.setJettyEnvPathUrl(file.toURI().toURL());
                    } catch (MalformedURLException e) {
                        throw new RuntimeException("Unable to create jettyEnvPathUrl", e);
                    }
                }
            }
            System.out.println(format("Starting Jetty server on port: %s and contextPath: %s", jettyServer.getPort(), jettyServer.getContextPath()));
            jettyServer.start();
        }
    }

    protected void printHelp(HelpFormatter formatter, Options cliOptions) {
        String jarName = getJarName();
        formatter.printHelp(String.format("java -jar %s", jarName == null ? "jar-file" : jarName), cliOptions);
    }

    protected String getJarName() {
        CodeSource codeSource = ServerRunner.class.getProtectionDomain().getCodeSource();
        if (codeSource != null) {
            File file = new File(codeSource.getLocation().getPath());
            return file.getName();
        }
        return null;
    }

    protected int getDefaultWebPort() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Properties properties = new Properties();
            properties.load(classLoader.getResourceAsStream(getPropertiesForPort(classLoader)));
            String webPort = (String) properties.get("cuba.webPort");
            if (webPort != null && !webPort.isEmpty()) {
                return Integer.parseInt(webPort);
            }
        } catch (Exception e) {
            System.out.println("Error while parsing port, use default port");
        }
        return 8080;
    }

    protected String getPropertiesForPort(ClassLoader classLoader) {
        if (isSingleJar(classLoader) || hasWebApp(classLoader)) {
            return WEB_PATH_IN_JAR + PATH_DELIMITER + APP_PROPERTIES_PATH_IN_JAR;
        } else if (hasCoreApp(classLoader)) {
            return CORE_PATH_IN_JAR + PATH_DELIMITER + APP_PROPERTIES_PATH_IN_JAR;
        } else if (hasPortalApp(classLoader)) {
            return PORTAL_PATH_IN_JAR + PATH_DELIMITER + APP_PROPERTIES_PATH_IN_JAR;
        }
        return null;
    }
}
