/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.desktop.sys.DesktopAppContextLoader;
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

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class App implements ConnectionListener {

    protected static App app;

    protected JFrame frame;

    protected JMenuBar menuBar;

    protected Connection connection;

    private Log log;

    public static void main(String[] args) {
        app = new App();
        app.show();
        app.showLoginDialog();
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
            initUI();

            DesktopAppContextLoader contextLoader = new DesktopAppContextLoader(getDefaultAppPropertiesConfig());
            contextLoader.load();
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
        return "/META-INF/cuba/app.properties";
    }

    protected String getDefaultHomeDir() {
        return System.getProperty("user.home") + "/.haulmont/cuba";
    }

    protected String getDefaultLog4jConfig() {
        return "META-INF/cuba/log4j.xml";
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

        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem item;

        item = new JMenuItem("Connect");
        item.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        showLoginDialog();
                    }
                }
        );
        menu.add(item);

        item = new JMenuItem("Exit");
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

        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem item;

        item = new JMenuItem("Disconnect");
        item.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        connection.logout();
                    }
                }
        );
        menu.add(item);

        item = new JMenuItem("Exit");
        item.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        exit();
                    }
                }
        );
        menu.add(item);

        return menuBar;
    }

    protected JComponent createBottomPane() {
        JPanel pane = new JPanel();
        JLabel connectionStateLab = new JLabel("Connected");
        pane.add(connectionStateLab);
        return pane;
    }

    protected JComponent createCenterPane() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.add(createTabsPane(), BorderLayout.CENTER);
        return pane;
    }

    private JComponent createTabsPane() {
        return new JTabbedPane();
    }

    public void connectionStateChanged(Connection connection) throws LoginException {
        if (connection.isConnected()) {
            frame.setContentPane(createContentPane());
            frame.repaint();
        } else {
            frame.setContentPane(createStartContentPane());
            frame.repaint();
            showLoginDialog();
        }
    }
}
