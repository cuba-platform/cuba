/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.desktop.sys.DesktopAppContextLoader;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.desktop.sys.MenuBuilder;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.security.global.LoginException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class App implements ConnectionListener {

    protected static App app;

    private Log log;

    protected JFrame frame;

    protected JMenuBar menuBar;

    protected Connection connection;

    protected DesktopWindowManager windowManager;

    private JTabbedPane tabsPane;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                app = new App();
                app.show();
                app.showLoginDialog();
            }
        });
    }

    public static App getInstance() {
        return app;
    }

    public App() {
        try {
            System.setSecurityManager(null);
            initLookAndFeel();
            initHomeDir();
            initLogging();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        }

        try {
            initConnection();

            DesktopAppContextLoader contextLoader = new DesktopAppContextLoader(getDefaultAppPropertiesConfig());
            contextLoader.load();

            initUI();
        } catch (Throwable t) {
            log.error("Error initializing application", t);
            System.exit(-1);
        }
    }

    public void show() {
        if (!frame.isVisible()) {
            frame.setVisible(true);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public JTabbedPane getTabsPane() {
        return tabsPane;
    }

    protected void showLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(connection);
        loginDialog.setLocationRelativeTo(frame);
        loginDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        loginDialog.setVisible(true);
    }

    protected String getApplicationTitle() {
        return "CUBA Application";
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

    protected void initLookAndFeel() throws Exception {
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(true);

        boolean found = false;
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                found = true;
                break;
            }
        }
        if (!found)
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }

    protected void initUI() {
        frame = new JFrame(getApplicationTitle());
        frame.setName("MainFrame");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setBounds(100, 100, 800, 600);
        frame.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        exit();
                    }
                }
        );

        frame.setContentPane(createStartContentPane());
    }

    protected void initConnection() {
        connection = new Connection();
        connection.addListener(this);
    }

    protected void exit() {
        AppContext.stopContext();
        System.exit(0);
    }

    protected Container createStartContentPane() {
        JPanel pane = new JPanel(new BorderLayout());
        menuBar = new JMenuBar();
        pane.add(menuBar, BorderLayout.NORTH);

        Locale loc = Locale.getDefault();

        JMenu menu = new JMenu(MessageProvider.getMessage(AppConfig.getMessagesPack(), "mainMenu.file", loc));
        menuBar.add(menu);

        JMenuItem item;

        item = new JMenuItem(MessageProvider.getMessage(AppConfig.getMessagesPack(), "mainMenu.connect", loc));
        item.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        showLoginDialog();
                    }
                }
        );
        menu.add(item);

        item = new JMenuItem(MessageProvider.getMessage(AppConfig.getMessagesPack(), "mainMenu.exit", loc));
        item.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
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

        JMenu menu = new JMenu(MessageProvider.getMessage(AppConfig.getMessagesPack(), "mainMenu.file"));
        menuBar.add(menu);

        JMenuItem item;

        item = new JMenuItem(MessageProvider.getMessage(AppConfig.getMessagesPack(), "mainMenu.disconnect"));
        item.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        connection.logout();
                    }
                }
        );
        menu.add(item);

        item = new JMenuItem(MessageProvider.getMessage(AppConfig.getMessagesPack(), "mainMenu.exit"));
        item.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        exit();
                    }
                }
        );
        menu.add(item);

        MenuBuilder builder = new MenuBuilder(connection.getSession(), menuBar);
        builder.build();

        return menuBar;
    }

    protected JComponent createBottomPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(0, 20));
        JLabel connectionStateLab = new JLabel("Connected");
        panel.add(connectionStateLab, BorderLayout.WEST);
        return panel;
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

    protected void initExceptionHandlers(boolean isConnected) {
        // TODO
    }

    public void connectionStateChanged(Connection connection) throws LoginException {
        if (connection.isConnected()) {
            windowManager = new DesktopWindowManager();
            frame.setContentPane(createContentPane());
            frame.repaint();
            windowManager.setTabsPane(tabsPane);
            initExceptionHandlers(true);
        } else {
            windowManager = null;
            frame.setContentPane(createStartContentPane());
            frame.repaint();
            initExceptionHandlers(false);
            showLoginDialog();
        }
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public JFrame getMainFrame() {
        return frame;
    }
}
