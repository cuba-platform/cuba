/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.ui;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Base test case for UI tests
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@SuppressWarnings("unused")
public abstract class UITestCase extends TestCase {

    /**
     * Key in system environment variables <br/>
     * Define custom executor script
     */
    private static final String ACCEPTANCE_EXECUTOR_KEY = "ACCEPTANCE_EXECUTOR";

    /**
     * Key in system environment variables <br/>
     * Define custom client program
     */
    private static final String ACCEPTANCE_CLIENT_KEY = "ACCEPTANCE_CLIENT";

    private static final String ACCEPTANCE_DIR = "tests/ui/";

    private Process clientRunner;

    public UITestCase() {
        super();
    }

    public UITestCase(String name) {
        super(name);
    }

    /**
     * Run UI test by name in test directory <br/>
     * If test fails then makes screenshot and writes log to output directory
     *
     * @param testFileName Test name
     * @return List of test steps with log messages
     * @throws Exception On IOException or while run client program
     */
    protected List<UITestStep> runUiTest(String testFileName) throws Exception {
        String executorScript = getExecutorScript();

        StringBuilder errors = new StringBuilder();
        StringBuilder info = new StringBuilder();
        executeTestScript(executorScript, testFileName, errors, info);

        List<UITestStep> testSteps = analyzeTestSteps(info.toString(), errors.toString());
        for (UITestStep testStep : testSteps) {
            if (!testStep.isSuccess()) {
                String outDirPath = ACCEPTANCE_DIR + "out/";
                File outDir = new File(outDirPath);
                if (!outDir.exists()) {
                    boolean result = outDir.mkdirs();
                    if (!result)
                        throw new IOException("Couldn't create out directory");
                }
                captureScreen(outDirPath + getName() + ".png");
                saveLog(testSteps, outDirPath + getName() + ".log");
                break;
            }
        }

        return testSteps;
    }

    private void saveLog(List<UITestStep> testSteps, String logFile) throws Exception {
        StringBuilder logBuilder = new StringBuilder();
        for (UITestStep testStep : testSteps) {
            if (testStep.isSuccess())
                logBuilder.append("[ STEP ] ");
            else
                logBuilder.append("[ FAILED ] ");
            logBuilder.append(testStep.getName()).append("\n");
            for (UITestLogMessage logMessage : testStep.getLogMessages()) {
                String level = "[ " + logMessage.getMessageLevel().toString() + " ] ";
                logBuilder.append(level).append(logMessage.getMessage()).append("\n");

                String content = logMessage.getContent().toString();
                if (content.length() > 0)
                    logBuilder.append(content.trim()).append("\n");
            }
        }
        FileUtils.writeStringToFile(new File(logFile), logBuilder.toString());
    }

    private void captureScreen(String destinationFile) throws Exception {
        Robot robot = new Robot();
        BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(screenShot, "PNG", new File(destinationFile));
    }

    private void pipe(final InputStream sourceStream, final OutputStream outputStream) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    IOUtils.copy(sourceStream, outputStream);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }

    private void executeTestScript(String scriptFile, String testFileName,
                                   StringBuilder errorLog, StringBuilder infoLog)
            throws FileNotFoundException {
        // get path to test script
        String testFilePath = getTestFilePath(testFileName);
        File testFile = new File(testFilePath);
        if (!testFile.exists())
            throw new FileNotFoundException("Couldn't found test file: " + testFile.getAbsolutePath());

        try {
            Process testRunner = Runtime.getRuntime().exec(scriptFile + " " + "\"" + testFilePath + "\"");

            ByteArrayOutputStream stdOutBuffer = new ByteArrayOutputStream();
            ByteArrayOutputStream stdErrBuffer = new ByteArrayOutputStream();

            pipe(testRunner.getInputStream(), stdOutBuffer);
            pipe(testRunner.getErrorStream(), stdErrBuffer);

            testRunner.waitFor();

            infoLog.append(new String(stdOutBuffer.toByteArray()));
            errorLog.append(new String(stdErrBuffer.toByteArray()));

        } catch (IOException ex) {
            throw new RuntimeException("Problem with execute external test");
        } catch (InterruptedException e) {
            throw new RuntimeException("Running test interrupted");
        }
    }

    private List<UITestStep> analyzeTestSteps(String infoLog, String errorsLog) {
        ArrayList<UITestStep> uiTestSteps = new ArrayList<UITestStep>();
        UITestStep currentStep = new UITestStep("Init");
        UITestLogMessage currentMessage = null;
        String[] logStrings = infoLog.split("\n");
        for (String logEnrty : logStrings) {
            if (!StringUtils.isWhitespace(logEnrty)) {
                UITestLogMessage logMessage = UITestLogMessage.parse(logEnrty);

                if (logMessage.getMessageLevel() == UITestLogMessage.Level.STEP) {
                    if (currentMessage != null)
                        currentStep.getLogMessages().add(currentMessage);
                    uiTestSteps.add(currentStep);
                    currentStep = new UITestStep(logMessage.getMessage());
                    currentMessage = null;
                } else if (logMessage.getMessageLevel() != UITestLogMessage.Level.CONTENT) {
                    if (currentMessage != null)
                        currentStep.getLogMessages().add(currentMessage);
                    currentMessage = logMessage;
                } else {
                    if (currentMessage == null)
                        currentMessage = new UITestLogMessage(UITestLogMessage.Level.CONTENT, "");
                    if (!StringUtils.isWhitespace(logMessage.getMessage()))
                        currentMessage.getContent().append(logMessage.getMessage().trim()).append("\n");
                }
            }
        }

        if (currentMessage != null)
            currentStep.getLogMessages().add(currentMessage);
        uiTestSteps.add(currentStep);

        // mark failed steps
        for (UITestStep step : uiTestSteps) {
            for (UITestLogMessage logMessage : step.getLogMessages()) {
                if (logMessage.getMessageLevel() == UITestLogMessage.Level.ERROR) {
                    step.setSuccess(false);
                    break;
                }
            }
        }

        if (!StringUtils.isWhitespace(errorsLog) && (errorsLog.contains("error"))) {
            UITestLogMessage logMessage = new UITestLogMessage(UITestLogMessage.Level.ERROR, "Fatal error");
            String[] strings = errorsLog.split("\n");
            for (String line : strings) {
                line = line.trim();
                if (StringUtils.isNotEmpty(line)) {
                    logMessage.getContent().append(line).append("\n");
                }
            }

            UITestStep lastStep = uiTestSteps.get(uiTestSteps.size() - 1);
            lastStep.getLogMessages().add(logMessage);
            lastStep.setSuccess(false);
        }

        return uiTestSteps;
    }

    protected String getTestExecutor() {
        return "ui-test";
    }

    protected String getClientProgram() {
        return "chrome";
    }

    private String getExecutorScript() {
        String testExecutor = System.getenv(ACCEPTANCE_EXECUTOR_KEY);

        if (StringUtils.isEmpty(testExecutor)) {
            String scriptExt;
            if (OsUtils.isWindows())
                scriptExt = "bat";
            else
                scriptExt = ".sh";
            testExecutor = getTestExecutor() + "." + scriptExt;
        }
        return testExecutor;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // start browser or another program
        startProgramInstance();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        // Exit from client program
        stopProgramInstance();
    }

    protected void startProgramInstance() throws IOException {
        String clientProgram = System.getenv(ACCEPTANCE_CLIENT_KEY);
        if (StringUtils.isEmpty(clientProgram))
            clientProgram = getClientProgram();
        clientRunner = Runtime.getRuntime().exec(clientProgram);
    }

    protected void stopProgramInstance() {
        clientRunner.destroy();
    }

    protected String getTestFilePath(String testFileName) {
        return ACCEPTANCE_DIR + "testsuite/" + testFileName;
    }

    protected boolean isSuccessful(List<UITestStep> testSteps) {
        boolean result = true;
        for (UITestStep testStep : testSteps) {
            result = result && testStep.isSuccess();
            Assert.assertTrue("Failed on step: " + testStep.getName(), testStep.isSuccess());
        }
        return result;
    }
}