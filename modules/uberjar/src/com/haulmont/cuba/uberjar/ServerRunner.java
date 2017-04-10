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

import com.google.common.reflect.ClassPath;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static java.lang.String.format;

public class ServerRunner {

    public static final String STATIC_CONTENT_PATH_IN_JAR = "ubercontent";
    public static final String FRONT_CONTENT_PATH_IN_JAR = "uberfront";
    public static final String APP_PROPERTIES_PATH_IN_JAR = "WEB-INF/local.app.properties";
    protected static final int DEFAULT_PORT = 8080;
    protected static final String CONTEXT_PATH_DELIMITER = "/";
    protected int port;
    protected String contextPath;
    protected String frontContextPath;
    protected URL jettyEnvPathUrl;

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

        Option jettyEnvPathOption = Option.builder("jettyEnvPath")
                .hasArg()
                .desc("jetty resource xml path").argName("jettyEnvPath").build();

        Option helpOption = Option.builder("help")
                .desc("print help information").build();

        cliOptions.addOption(helpOption);
        cliOptions.addOption(portOption);
        cliOptions.addOption(contextPathOption);
        cliOptions.addOption(frontContextPathOption);
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
            if (cmd.hasOption(portOption.getOpt())) {
                try {
                    port = Integer.parseInt(cmd.getOptionValue(portOption.getOpt()));
                } catch (NumberFormatException e) {
                    System.out.println("port has to be number");
                    printHelp(formatter, cliOptions);
                    return;
                }
            } else if (port == 0) {
                port = getWebPort();
            }

            if (cmd.hasOption(contextPathOption.getOpt())) {
                String contextName = cmd.getOptionValue(contextPathOption.getOpt());
                if (contextName != null && !contextName.isEmpty()) {
                    if (CONTEXT_PATH_DELIMITER.equals(contextName)) {
                        contextPath = CONTEXT_PATH_DELIMITER;
                    }
                    contextPath = CONTEXT_PATH_DELIMITER + contextName;
                }
            }
            if (cmd.hasOption(frontContextPathOption.getOpt())) {
                String contextName = cmd.getOptionValue(frontContextPathOption.getOpt());
                if (contextName != null && !contextName.isEmpty()) {
                    if (CONTEXT_PATH_DELIMITER.equals(contextName)) {
                        frontContextPath = CONTEXT_PATH_DELIMITER;
                    }
                    frontContextPath = CONTEXT_PATH_DELIMITER + contextName;
                }
            }

            if (contextPath == null) {
                String jarName = getJarName();
                if (jarName != null) {
                    contextPath = CONTEXT_PATH_DELIMITER + FilenameUtils.getBaseName(jarName);
                } else {
                    contextPath = CONTEXT_PATH_DELIMITER;
                }
            }
            if (frontContextPath == null) {
                if (CONTEXT_PATH_DELIMITER.equals(contextPath)) {
                    frontContextPath = CONTEXT_PATH_DELIMITER + "app-front";
                } else {
                    frontContextPath = contextPath + "-front";
                }
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
                        jettyEnvPathUrl = file.toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException("Unable to create jettyEnvPathUrl", e);
                    }
                }
            }
            startServer();
        }
    }

    protected void startServer() {
        System.out.println(format("Starting Jetty server on port: %s and contextPath: %s", port, contextPath));
        System.setProperty("app.home", System.getProperty("user.dir"));
        try {
            Server server = createServer();
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    protected Server createServer() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Server server = new Server(port);

        CubaJettyWebAppContext cubaContext = new CubaJettyWebAppContext();
        cubaContext.setStaticContents(getStaticContents(classLoader));
        cubaContext.setConfigurations(new Configuration[]{new CubaJettyWebXmlConfiguration(), createEnvConfiguration(classLoader)});
        cubaContext.setDescriptor("WEB-INF/web.xml");
        cubaContext.setContextPath(contextPath);
        cubaContext.setParentLoaderPriority(true);
        cubaContext.setClassLoader(classLoader);

        URL frontContentUrl = classLoader.getResource(FRONT_CONTENT_PATH_IN_JAR);
        WebAppContext frontContext = null;
        if (frontContentUrl != null) {
            frontContext = new WebAppContext();
            frontContext.setContextPath(frontContextPath);
            frontContext.setParentLoaderPriority(true);
            frontContext.setClassLoader(classLoader);
            frontContext.setResourceBase(frontContentUrl.toURI().toString());
        }
        if (frontContext != null) {
            HandlerCollection handlerCollection = new HandlerCollection();
            handlerCollection.setHandlers(new Handler[]{cubaContext, frontContext});
            server.setHandler(handlerCollection);
        } else {
            server.setHandler(cubaContext);
        }
        return server;
    }

    protected EnvConfiguration createEnvConfiguration(ClassLoader classLoader) {
        EnvConfiguration envConfiguration = new EnvConfiguration();
        if (jettyEnvPathUrl != null) {
            envConfiguration.setJettyEnvXml(jettyEnvPathUrl);
        } else {
            envConfiguration.setJettyEnvXml(classLoader.getResource("WEB-INF/jetty-env.xml"));
        }
        return envConfiguration;
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

    protected Set<String> getStaticContents(ClassLoader classLoader) {
        Set<String> contents = new HashSet<>();
        try {
            ClassPath.from(classLoader).getResources().forEach(it -> {
                if (it.getResourceName() != null && it.getResourceName().startsWith(STATIC_CONTENT_PATH_IN_JAR)) {
                    String[] paths = it.getResourceName().split("/");
                    //extract only first level dirs
                    if (paths.length > 2) {
                        contents.add(paths[1]);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return contents;
    }

    protected int getWebPort() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Properties properties = new Properties();
            properties.load(classLoader.getResourceAsStream(APP_PROPERTIES_PATH_IN_JAR));
            String webPort = (String) properties.get("cuba.webPort");
            if (webPort != null && !webPort.isEmpty()) {
                return Integer.parseInt(webPort);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error while parsing port, use default port");
        }
        return DEFAULT_PORT;
    }
}
