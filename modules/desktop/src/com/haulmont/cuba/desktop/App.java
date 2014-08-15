/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.client.sys.MessagesClientImpl;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.remoting.ClusterInvocationSupport;
import com.haulmont.cuba.desktop.exception.ExceptionHandlers;
import com.haulmont.cuba.desktop.gui.SessionMessagesNotifier;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.*;
import com.haulmont.cuba.desktop.theme.DesktopTheme;
import com.haulmont.cuba.desktop.theme.DesktopThemeLoader;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsRepository;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.RemoteAccessException;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.plaf.InputMapUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author krivopustov
 * @version $Id$
 */
public class App implements ConnectionListener {

    protected static App app;

    private Log log;

    protected TopLevelFrame mainFrame;

    protected JMenuBar menuBar;

    protected Connection connection;

    private JTabbedPane tabsPane;

    protected DesktopTheme theme;

    protected ThemeConstants themeConstants;

    protected LinkedList<TopLevelFrame> topLevelFrames = new LinkedList<>();

    protected Messages messages;

    protected Configuration configuration;

    protected boolean exiting;

    protected ApplicationSession applicationSession;

    static {
        initEnvironment();
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app = new App();
                app.init(args);
                app.show();
                app.showLoginDialog();
            }
        });
    }

    public static void initEnvironment() {
        // Due to #PL-2421
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }

    public static App getInstance() {
        return app;
    }

    public void init(String[] args) {
        try {
            System.setSecurityManager(null);
            initHomeDir();
            initLogging();
        } catch (Throwable t) {
            //noinspection CallToPrintStackTrace
            t.printStackTrace();
            System.exit(-1);
        }

        try {
            log.debug("Program arguments: " + Arrays.toString(args));

            initConnection();

            DesktopAppContextLoader contextLoader = new DesktopAppContextLoader(getDefaultAppPropertiesConfig(), args);
            contextLoader.load();

            messages = AppBeans.get(Messages.class);
            configuration = AppBeans.get(Configuration.class);

            initTheme();
            initLookAndFeelDefaults();
            initUI();
            initExceptionHandling();
        } catch (Throwable t) {
            log.error("Error initializing application", t);
            System.exit(-1);
        }
    }

    public void show() {
        if (!mainFrame.isVisible()) {
            mainFrame.setVisible(true);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public JTabbedPane getTabsPane() {
        return tabsPane;
    }

    public void showLoginDialog() {
        if (exiting)
            return;

        LoginDialog loginDialog = createLoginDialog();
        setLoginDialogLocation(loginDialog);
        loginDialog.open();
    }

    protected void setLoginDialogLocation(LoginDialog loginDialog) {
        Point ownerLocation = mainFrame.getLocationOnScreen();
        int mainX = ownerLocation.x;
        int mainY = ownerLocation.y;

        Dimension ownerSize = mainFrame.getSize();
        int mainWidth = ownerSize.width;
        int mainHeight = ownerSize.height;

        Dimension size = loginDialog.getSize();
        int width = size.width;
        int height = size.height;

        loginDialog.setLocation(mainX + mainWidth/2 - width/2, mainY + mainHeight/2 - height/2);
    }

    protected LoginDialog createLoginDialog() {
        return new LoginDialog(mainFrame, connection);
    }

    protected String getApplicationTitle() {
        return messages.getMainMessage("application.caption");
    }

    protected String getDefaultAppPropertiesConfig() {
        return "/cuba-desktop-app.properties";
    }

    protected String getDefaultHomeDir() {
        return System.getProperty("user.home") + "/.haulmont/cuba";
    }

    protected String getDefaultLog4jConfig() {
        return "cuba-log4j.xml";
    }

    protected void initHomeDir() {
        String homeDir = System.getProperty(DesktopAppContextLoader.HOME_DIR_SYS_PROP);
        if (StringUtils.isBlank(homeDir)) {
            homeDir = getDefaultHomeDir();
        }
        homeDir = StrSubstitutor.replaceSystemProperties(homeDir);
        System.setProperty(DesktopAppContextLoader.HOME_DIR_SYS_PROP, homeDir);

        File file = new File(homeDir);
        if (!file.exists()) {
            boolean success = file.mkdirs();
            if (!success) {
                System.out.println("Unable to create home dir: " + homeDir);
                System.exit(-1);
            }
        }
        if (!file.isDirectory()) {
            System.out.println("Invalid home dir: " + homeDir);
            System.exit(-1);
        }
    }

    protected void initLogging() {
        String property = System.getProperty("log4j.configuration");
        if (StringUtils.isBlank(property)) {
            System.setProperty("log4j.configuration", getDefaultLog4jConfig());
        }
        log = LogFactory.getLog(App.class);
    }

    protected void initTheme() throws Exception {
        DesktopConfig config = configuration.getConfig(DesktopConfig.class);
        String themeName = config.getTheme();
        theme = AppBeans.get(DesktopThemeLoader.class).loadTheme(themeName);
        theme.init();

        ThemeConstantsRepository themeRepository = AppBeans.get(ThemeConstantsRepository.NAME);
        ThemeConstants uiTheme = themeRepository.getConstants(themeName);

        if (uiTheme == null) {
            throw new IllegalStateException("Unable to use theme constants '" + themeName + "'");
        }

        this.themeConstants = uiTheme;
    }

    public DesktopTheme getTheme() {
        return theme;
    }

    public ThemeConstants getThemeConstants() {
        return themeConstants;
    }

    protected void initLookAndFeelDefaults() {
        InputMapUIResource inputMap =
                (InputMapUIResource) UIManager.getLookAndFeelDefaults().get("FormattedTextField.focusInputMap");
        inputMap.remove(KeyStroke.getKeyStroke("ESCAPE"));
    }

    protected void initUI() {
        ToolTipManager.sharedInstance().setEnabled(false);
        mainFrame = new TopLevelFrame(getApplicationTitle());
        mainFrame.setName("MainFrame");
        mainFrame.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        DesktopComponentsHelper.flushCurrentInputField();

                        exit();
                    }
                }
        );

        mainFrame.setContentPane(createStartContentPane());
        registerFrame(mainFrame);
        createMainWindowProperties().load();
    }

    protected MainWindowProperties createMainWindowProperties() {
        return new MainWindowProperties(mainFrame);
    }

    protected void initConnection() {
        connection = createConnection();
        connection.addListener(this);
    }

    protected Connection createConnection() {
        return new Connection();
    }

    protected void exit() {
        try {
            if (connection.isConnected()) {
                recursiveClosingFrames(topLevelFrames.iterator(), new Runnable() {
                    @Override
                    public void run() {
                        exiting = true;
                        connection.logout();
                        forceExit();
                    }
                });
            } else {
                forceExit();
            }
        } catch (Throwable e) {
            log.warn("Error closing application: " + e);
            String title = messages.getMainMessage("errorPane.title");
            String text = messages.getMainMessage("unexpectedCloseException.message") + "\n";
            if (e instanceof RemoteAccessException) {
                text = text + messages.getMainMessage("connectException.message");
            } else {
                text = text + e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            JOptionPane.showMessageDialog(mainFrame, text, title, JOptionPane.WARNING_MESSAGE);
            forceExit();
        }
    }

    protected void forceExit() {
        //noinspection finally
        try {
            createMainWindowProperties().save();
            AppContext.stopContext();
        } finally {
            System.exit(0);
        }
    }

    protected Container createStartContentPane() {
        JPanel pane = new JPanel(new BorderLayout());
        menuBar = new JMenuBar();
        pane.add(menuBar, BorderLayout.NORTH);

        Locale loc = Locale.getDefault();

        JMenu menu = new JMenu(messages.getMessage(AppConfig.getMessagesPack(), "mainMenu.file", loc));
        menuBar.add(menu);

        JMenuItem item;

        item = new JMenuItem(messages.getMessage(AppConfig.getMessagesPack(), "mainMenu.connect", loc));
        item.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showLoginDialog();
                    }
                }
        );
        menu.add(item);

        item = new JMenuItem(messages.getMessage(AppConfig.getMessagesPack(), "mainMenu.exit", loc));
        item.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DesktopComponentsHelper.flushCurrentInputField();

                        exit();
                    }
                }
        );
        menu.add(item);

        return pane;
    }

    protected Container createContentPane() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.add(createTopPane(), BorderLayout.NORTH);
        pane.add(createCenterPane(), BorderLayout.CENTER);
        pane.add(createBottomPane(), BorderLayout.SOUTH);
        return pane;
    }

    protected JComponent createTopPane() {
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.add(createMenuBar(), BorderLayout.CENTER);
        return toolBar;
    }

    protected JComponent createMenuBar() {
        menuBar = new JMenuBar();

        JMenu menu = new JMenu(messages.getMessage(AppConfig.getMessagesPack(), "mainMenu.file"));
        menuBar.add(menu);

        JMenuItem item;

        item = new JMenuItem(messages.getMessage(AppConfig.getMessagesPack(), "mainMenu.disconnect"));
        item.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DesktopComponentsHelper.flushCurrentInputField();

                        logout();
                    }
                }
        );
        menu.add(item);

        item = new JMenuItem(messages.getMessage(AppConfig.getMessagesPack(), "mainMenu.exit"));
        item.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DesktopComponentsHelper.flushCurrentInputField();

                        exit();
                    }
                }
        );
        menu.add(item);

        MenuBuilder builder = new MenuBuilder(connection.getSession(), menuBar);
        builder.build();

        return menuBar;
    }

    private void logout() {
        final Iterator<TopLevelFrame> it = topLevelFrames.iterator();
        recursiveClosingFrames(it, new Runnable() {
            @Override
            public void run() {
                connection.logout();
            }
        });
    }

    private void recursiveClosingFrames(final Iterator<TopLevelFrame> it, final Runnable onSuccess) {
        final TopLevelFrame frame = it.next();
        frame.getWindowManager().checkModificationsAndCloseAll(new Runnable() {
            @Override
            public void run() {
                if (!it.hasNext()) {
                    onSuccess.run();
                } else {
                    frame.getWindowManager().dispose();
                    frame.dispose();
                    it.remove();
                    recursiveClosingFrames(it, onSuccess);
                }
            }
        }, null
        );
    }

    protected JComponent createBottomPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(0, 20));

        ClusterInvocationSupport clusterInvocationSupport = AppBeans.get(ClusterInvocationSupport.NAME);
        String url = clusterInvocationSupport.getUrlList().isEmpty() ? "?" : clusterInvocationSupport.getUrlList().get(0);

        final JLabel connectionStateLab = new JLabel(
                messages.formatMessage(AppConfig.getMessagesPack(), "statusBar.connected", getUserFriendlyConnectionUrl(url)));

        clusterInvocationSupport.addListener(
                new ClusterInvocationSupport.Listener() {
                    @Override
                    public void urlListChanged(List<String> newUrlList) {
                        String url = newUrlList.isEmpty() ? "?" : newUrlList.get(0);
                        connectionStateLab.setText(
                                messages.formatMessage(AppConfig.getMessagesPack(),
                                        "statusBar.connected", getUserFriendlyConnectionUrl(url)));
                    }
                }
        );

        panel.add(connectionStateLab, BorderLayout.WEST);

        JLabel userInfoLabel = new JLabel();
        UserSession session = connection.getSession();
        String userInfo = messages.formatMessage(AppConfig.getMessagesPack(), "statusBar.user",
                session.getUser().getName(), session.getUser().getLogin());
        userInfoLabel.setText(userInfo);

        panel.add(userInfoLabel, BorderLayout.EAST);

        return panel;
    }

    protected String getUserFriendlyConnectionUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getHost() + (url.getPort() == -1 ? "" : ":" + url.getPort());
        } catch (MalformedURLException e) {
            return urlString;
        }
    }

    protected JComponent createCenterPane() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.add(createTabsPane(), BorderLayout.CENTER);
        return pane;
    }

    protected JComponent createTabsPane() {
        tabsPane = new JTabbedPane();
        return tabsPane;
    }

    protected void initExceptionHandling() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                handleException(thread, throwable);
            }
        });

        System.setProperty("sun.awt.exception.handler", "com.haulmont.cuba.desktop.exception.AWTExceptionHandler");
    }

    public void handleException(Thread thread, Throwable throwable) {
        if (!(throwable instanceof SilentException)) {
            Logging annotation = throwable.getClass().getAnnotation(Logging.class);
            Logging.Type loggingType = annotation == null ? Logging.Type.FULL : annotation.value();
            if (loggingType != Logging.Type.NONE) {
                if (loggingType == Logging.Type.BRIEF)
                    log.error("Uncaught exception in thread " + thread + ": " + throwable.toString());
                else
                    log.error("Uncaught exception in thread " + thread, throwable);
            }
        }

        ExceptionHandlers handlers = AppBeans.get("cuba_ExceptionHandlers", ExceptionHandlers.class);
        handlers.handle(thread, throwable);
    }

    /**
     * Initializes exception handlers immediately after login and logout.
     * Can be overridden in descendants to manipulate exception handlers programmatically.
     *
     * @param isConnected true after login, false after logout
     */
    protected void initExceptionHandlers(boolean isConnected) {
        ExceptionHandlers handlers = AppBeans.get("cuba_ExceptionHandlers", ExceptionHandlers.class);
        if (isConnected) {
            handlers.createByConfiguration();
        } else {
            handlers.createMinimalSet();
        }
    }

    @Override
    public void connectionStateChanged(Connection connection) throws LoginException {
        MessagesClientImpl messagesClient = AppBeans.get(Messages.NAME);
        SessionMessagesNotifier messagesNotifier = AppBeans.get(SessionMessagesNotifier.NAME);

        if (connection.isConnected()) {
            applicationSession = new ApplicationSession(new ConcurrentHashMap<String, Object>());

            messagesClient.setRemoteSearch(true);

            DesktopWindowManager windowManager = mainFrame.getWindowManager();
            mainFrame.setContentPane(createContentPane());
            mainFrame.repaint();
            windowManager.setTabsPane(tabsPane);

            initExceptionHandlers(true);
            initClientTime();

            messagesNotifier.activate();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    checkSessions();
                    afterLoggedIn();
                }
            });
        } else {
            messagesNotifier.deactivate();

            messagesClient.setRemoteSearch(false);
            Iterator<TopLevelFrame> it = topLevelFrames.iterator();
            while (it.hasNext()) {
                TopLevelFrame frame = it.next();
                if (frame != mainFrame) {
                    DesktopWindowManager windowManager = frame.getWindowManager();
                    if (windowManager != null)
                        windowManager.dispose();
                    frame.dispose();
                    it.remove();
                }
            }

            DesktopWindowManager windowManager = mainFrame.getWindowManager();
            if (windowManager != null)
                windowManager.dispose();

            applicationSession = null;

            mainFrame.setContentPane(createStartContentPane());
            mainFrame.repaint();

            initExceptionHandlers(false);
            showLoginDialog();
        }
    }

    @Nullable
    public ApplicationSession getApplicationSession() {
        return applicationSession;
    }

    private void checkSessions() {
        Map<String, Object> info = AppBeans.get(UserSessionService.class).getLicenseInfo();
        Integer licensed = (Integer) info.get("licensedSessions");
        if (licensed < 0) {
            mainFrame.showNotification("Invalid CUBA platform license", IFrame.NotificationType.WARNING);
        } else {
            Integer active = (Integer) info.get("activeSessions");
            if (licensed != 0 && active > licensed) {
                mainFrame.showNotification("Number of licensed sessions exceeded", "active: " + active + ", licensed: " + licensed,
                        IFrame.NotificationType.WARNING);
            }
        }
    }

    /**
     * Perform actions after success login
     */
    protected void afterLoggedIn() {
        final User user = AppBeans.get(UserSessionSource.class).getUserSession().getUser();
        // Change password on logon
        if (Boolean.TRUE.equals(user.getChangePasswordAtNextLogon())) {
            mainFrame.deactivate("");
            final DesktopWindowManager wm = mainFrame.getWindowManager();
            for (Window window : wm.getOpenWindows())
                window.setEnabled(false);

            WindowInfo changePasswordDialog = AppBeans.get(WindowConfig.class).getWindowInfo("sec$User.changePassw");
            wm.getDialogParams().setCloseable(false);
            Map<String, Object> params = Collections.singletonMap("cancelEnabled", (Object) Boolean.FALSE);
            Window changePasswordWindow = wm.openEditor(changePasswordDialog, user, WindowManager.OpenType.DIALOG, params);
            changePasswordWindow.addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    for (Window window : wm.getOpenWindows())
                        window.setEnabled(true);
                }
            });
        }
    }

    protected void initClientTime() {
        ClientTimeSynchronizer clientTimeSynchronizer = AppBeans.get(ClientTimeSynchronizer.NAME);
        clientTimeSynchronizer.syncTimeZone();
        clientTimeSynchronizer.syncTime();
    }

    public TopLevelFrame getMainFrame() {
        return mainFrame;
    }

    public void registerFrame(TopLevelFrame frame) {
        topLevelFrames.addFirst(frame);
    }

    public void unregisterFrame(TopLevelFrame frame) {
        topLevelFrames.remove(frame);
    }

    public DesktopResources getResources() {
        return theme.getResources();
    }

    public Locale getLocale() {
        if (getConnection().getSession() == null)
            return Locale.getDefault();
        else
            return getConnection().getSession().getLocale();
    }
}
