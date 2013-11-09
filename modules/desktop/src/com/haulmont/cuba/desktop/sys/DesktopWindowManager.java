/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.desktop.TopLevelFrame;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.ScreenHistorySupport;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.executors.WatchDog;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopWindowManager extends WindowManager {

    private static final float NEW_WINDOW_SCALE = 0.7f;

    private JTabbedPane tabsPane;

    private final Map<JComponent, WindowBreadCrumbs> tabs = new HashMap<>();
    private final Map<Window, WindowOpenMode> windowOpenMode = new LinkedHashMap<>();
    private final Map<WindowBreadCrumbs, Stack<Map.Entry<Window, Integer>>> stacks = new HashMap<>();
    private final Map<Window, Integer> windows = new HashMap<>();
    private final TopLevelFrame frame;
    private final boolean isMainWindowManager;

    private boolean disableSavingScreenHistory;
    private ScreenHistorySupport screenHistorySupport = new ScreenHistorySupport();

    private Log log = LogFactory.getLog(DesktopWindowManager.class);

    public DesktopWindowManager(TopLevelFrame frame) {
        this.frame = frame;
        isMainWindowManager = frame == App.getInstance().getMainFrame();
    }

    public TopLevelFrame getFrame() {
        return frame;
    }

    public void setTabsPane(final JTabbedPane tabsPane) {
        this.tabsPane = tabsPane;

        tabsPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke("control W"),
                "closeTab"
        );
        tabsPane.getActionMap().put(
                "closeTab",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        closeTab((JComponent) tabsPane.getSelectedComponent());
                    }
                }
        );
    }

    private void closeTab(JComponent tabContent) {
        if (tabContent == null)
            return;
        WindowBreadCrumbs breadCrumbs = tabs.get(tabContent);
        Runnable closeTask = new TabCloseTask(breadCrumbs);
        closeTask.run();
    }

    @Override
    public Collection<Window> getOpenWindows() {
        return new ArrayList<Window>(windowOpenMode.keySet());
    }

    @Nullable
    public DialogWindow getLastDialogWindow() {
        List<Window> openedWindows = new ArrayList<>(windowOpenMode.keySet());
        if (openedWindows.size() > 0) {
            Window w = openedWindows.get(openedWindows.size() - 1);
            WindowOpenMode mode = windowOpenMode.get(w);
            if (mode.getOpenType().equals(OpenType.DIALOG) && mode.getData() instanceof DialogWindow) {
                return (DialogWindow) mode.getData();
            }
        }
        return null;
    }

    @Override
    protected void putToWindowMap(Window window, Integer hashCode) {
        if (window != null) {
            windows.put(window, hashCode);
        }
    }

    private Integer getWindowHashCode(Window window) {
        return windows.get(window);
    }

    @Override
    protected Window getWindow(Integer hashCode) {
        Set<Map.Entry<Window, Integer>> set = windows.entrySet();
        for (Map.Entry<Window, Integer> entry : set) {
            if (hashCode.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    protected void checkCanOpenWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params) {
    }

    protected boolean hasModalWindow() {
        Set<Map.Entry<Window, WindowOpenMode>> openModes = windowOpenMode.entrySet();
        for (Map.Entry<Window, WindowOpenMode> openMode : openModes) {
            if (OpenType.DIALOG.equals(openMode.getValue().getOpenType()))
                return true;
        }
        return false;
    }

    @Override
    protected void showWindow(Window window, String caption, OpenType openType, boolean multipleOpen) {
        showWindow(window, caption, null, openType, multipleOpen);
    }

    @Override
    protected void showWindow(Window window, String caption, String description, OpenType openType, boolean multipleOpen) {
        boolean forciblyDialog = false;
        boolean addWindowData = true;
        if (openType != OpenType.DIALOG && hasModalWindow()) {
            openType = OpenType.DIALOG;
            forciblyDialog = true;
        }

        if (openType == OpenType.THIS_TAB && tabs.size() == 0) {
            openType = OpenType.NEW_TAB;
        }

        window.setCaption(caption);
        window.setDescription(description);

        Object windowData;

        switch (openType) {
            case NEW_TAB:
                if (!isMainWindowManager) {
                    addWindowData = false;
                    showInMainWindowManager(window, caption, description, openType, multipleOpen);
                    windowData = null;
                } else {
                    Integer hashCode = getWindowHashCode(window);
                    JComponent tab;
                    if (hashCode != null && !multipleOpen && (tab = findTab(hashCode)) != null) {
                        int oldTabPosition = -1;
                        for (int i = 0; i < tabsPane.getTabCount(); i++) {
                            if (tab.equals(tabsPane.getComponentAt(i))) {
                                oldTabPosition = i;
                            }
                        }
                        WindowBreadCrumbs oldBreadCrumbs = tabs.get(tab);
                        oldBreadCrumbs.getCurrentWindow().close("mainMenu");
                        windowData = showWindowNewTab(window, caption, description, oldTabPosition);
                    } else {
                        windowData = showWindowNewTab(window, caption, description, null);
                    }
                }
                break;
            case THIS_TAB:
                windowData = showWindowThisTab(window, caption, description);
                break;
            case DIALOG:
                windowData = showWindowDialog(window, caption, description, forciblyDialog);
                break;
            case NEW_WINDOW:
                addWindowData = false;
                windowData = showNewWindow(window, caption);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        if (addWindowData) {
            addWindowData(window, windowData, openType);
        }
    }

    private void showInMainWindowManager(Window window, String caption, String description, OpenType openType, boolean multipleOpen) {
        DesktopWindowManager mainMgr = App.getInstance().getMainFrame().getWindowManager();
        windows.remove(window);
        window.setWindowManager(mainMgr);
        mainMgr.showWindow(window, caption, openType, multipleOpen);
    }

    private TopLevelFrame createTopLevelFrame(String caption) {
        final TopLevelFrame windowFrame = new TopLevelFrame(caption);
        Dimension size = frame.getSize();
        int width = Math.round(size.width * NEW_WINDOW_SCALE);
        int height = Math.round(size.height * NEW_WINDOW_SCALE);

        windowFrame.setSize(width, height);
        windowFrame.setLocationRelativeTo(frame);

        return windowFrame;
    }

    private void closeFrame(TopLevelFrame frame) {
        frame.setVisible(false);
        frame.dispose();
        frame.getWindowManager().dispose();
        App.getInstance().unregisterFrame(getFrame());
    }

    private JComponent showNewWindow(Window window, String caption) {
        TopLevelFrame windowFrame = createTopLevelFrame(caption);

        WindowBreadCrumbs breadCrumbs = createBreadCrumbs();
        breadCrumbs.addWindow(window);
        JComponent tabContent = createTabSheetPanel(window, breadCrumbs);

        WindowOpenMode openMode = new WindowOpenMode(window, OpenType.NEW_WINDOW);
        openMode.setData(tabContent);
        Map<Window, WindowOpenMode> openModes = new HashMap<>();
        if (window instanceof Window.Wrapper) {
            Window wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
            openModes.put(wrappedWindow, openMode);
        } else {
            openModes.put(window, openMode);
        }

        windowFrame.getWindowManager().attachTab(breadCrumbs,
                new Stack<Map.Entry<Window, Integer>>(),
                window,
                getWindowHashCode(window),
                tabContent, openModes);

        App.getInstance().registerFrame(windowFrame);

        windowFrame.setVisible(true);
        return DesktopComponentsHelper.getComposition(window);
    }

    protected void addShortcuts(final Window window) {
        ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
        String keys = clientConfig.getCloseShortcut();
        window.addAction(new com.haulmont.cuba.gui.components.AbstractAction("closeWindowShortcutAction", keys) {
            @Override
            public void actionPerform(Component component) {
                window.close("close");
            }
        });
    }

    private JDialog showWindowDialog(final Window window, String caption, String description, boolean forciblyDialog) {
        final JDialog dialog = new DialogWindow(frame, caption);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        final DialogParams dialogParams = getDialogParams();
        JComponent jComponent = DesktopComponentsHelper.getComposition(window);
        dialog.add(jComponent);

        if (dialogParams.getCloseable() == null ||
                dialogParams.getCloseable()) {
            dialog.addWindowListener(
                    new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            if (window.close("close", false)) {
                                dialog.dispose();
                            }
                        }
                    }
            );
        }

        Dimension dim = new Dimension();
        if (forciblyDialog) {
            window.setHeight("100%");
            // todo move it to desktop application preferences
            dim.width = 800;
            dim.height = 500;
            dialog.setResizable(true);
        } else {
            if (dialogParams.getWidth() != null)
                dim.width = dialogParams.getWidth();
            else
                dim.width = 600;

            if (dialogParams.getHeight() != null) {
                dim.height = dialogParams.getHeight();
            }
            dialog.setResizable(BooleanUtils.isTrue(dialogParams.getResizable()));
        }

        if (dialogParams.getCloseable() != null) {
            if (!dialogParams.getCloseable())
                dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        }
        dialogParams.reset();

        dialog.setMinimumSize(dim);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);

        DialogWindow lastDialogWindow = getLastDialogWindow();
        if (lastDialogWindow == null)
            frame.deactivate(null);
        else
            lastDialogWindow.disableWindow(null);

        dialog.setVisible(true);

        return dialog;
    }

    private JComponent showWindowThisTab(Window window, String caption, String description) {
        JComponent layout;
        if (isMainWindowManager) {
            layout = (JComponent) tabsPane.getSelectedComponent();
        } else {
            layout = (JComponent) frame.getContentPane().getComponent(0);
        }
        WindowBreadCrumbs breadCrumbs = tabs.get(layout);
        if (breadCrumbs == null)
            throw new IllegalStateException("BreadCrumbs not found");

        Window currentWindow = breadCrumbs.getCurrentWindow();
        windowOpenMode.get(currentWindow.getFrame()).setFocusOwner(frame.getFocusOwner());

        Set<Map.Entry<Window, Integer>> set = windows.entrySet();
        boolean pushed = false;
        for (Map.Entry<Window, Integer> entry : set) {
            if (entry.getKey().equals(currentWindow)) {
                windows.remove(currentWindow);
                stacks.get(breadCrumbs).push(entry);
                pushed = true;
                break;
            }
        }
        if (!pushed) {
            stacks.get(breadCrumbs).push(new AbstractMap.SimpleEntry<Window, Integer>(currentWindow, null));
        }

        windows.remove(window);
        layout.remove(DesktopComponentsHelper.getComposition(currentWindow));

        JComponent component = DesktopComponentsHelper.getComposition(window);
        layout.add(component);

        breadCrumbs.addWindow(window);
        if (isMainWindowManager) {
            setWindowCaption(caption, description, tabsPane.getSelectedIndex());
        } else {
            setTopLevelWindowCaption(caption);
            component.revalidate();
            component.repaint();
        }

        return layout;
    }

    private void setWindowCaption(String caption, String description, int tabIndex) {
        ((ButtonTabComponent) tabsPane.getTabComponentAt(tabIndex)).setCaption(formatTabCaption(caption, description));
    }

    private void setTopLevelWindowCaption(String caption) {
        frame.setTitle(caption);
    }

    private WindowBreadCrumbs createBreadCrumbs() {
        final WindowBreadCrumbs breadCrumbs = new WindowBreadCrumbs();
        breadCrumbs.addListener(
                new WindowBreadCrumbs.Listener() {
                    @Override
                    public void windowClick(final Window window) {
                        Runnable op = new Runnable() {
                            @Override
                            public void run() {
                                Window currentWindow = breadCrumbs.getCurrentWindow();
                                if (currentWindow != null && window != currentWindow) {
                                    currentWindow.closeAndRun("close", this);
                                }
                            }
                        };
                        op.run();
                    }
                }
        );
        return breadCrumbs;
    }

    protected JComponent showWindowNewTab(Window window, String caption, String description, Integer tabPosition) {
        final WindowBreadCrumbs breadCrumbs = createBreadCrumbs();
        stacks.put(breadCrumbs, new Stack<Map.Entry<Window, Integer>>());
        breadCrumbs.addWindow(window);
        JComponent tabContent = createNewTabSheet(window, caption, description, breadCrumbs, tabPosition);
        tabs.put(tabContent, breadCrumbs);
        return tabContent;
    }

    protected JPanel createTabSheetPanel(Window window, WindowBreadCrumbs breadCrumbs) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(breadCrumbs, BorderLayout.NORTH);
        JComponent composition = DesktopComponentsHelper.getComposition(window);
        panel.add(composition, BorderLayout.CENTER);
        return panel;
    }

    protected JComponent createNewTabSheet(Window window, String caption, String description, WindowBreadCrumbs breadCrumbs, Integer tabPosition) {
        JPanel panel = createTabSheetPanel(window, breadCrumbs);
        int idx;
        if (tabPosition != null) {
            idx = tabPosition;
        } else {
            idx = tabsPane.getTabCount();
        }
        tabsPane.insertTab(formatTabCaption(caption, description), null, panel, null, idx);

        ButtonTabComponent tabComponent = new ButtonTabComponent(
                tabsPane, true, true,
                new ButtonTabComponent.CloseListener() {
                    @Override
                    public void onTabClose(int tabIndex) {
                        JComponent tabContent = (JComponent) tabsPane.getComponentAt(tabIndex);
                        closeTab(tabContent);
                    }
                },
                new ButtonTabComponent.DetachListener() {
                    @Override
                    public void onDetach(int tabIndex) {
                        detachTab(tabIndex);

                    }
                }
        );
        tabsPane.setTabComponentAt(idx, tabComponent);
        tabsPane.setSelectedIndex(idx);

        return panel;
    }

    private void detachTab(int tabIndex) {
        //Create new top-level frame, put this tab to it with breadcrumbs.
        // remove tab data from this window manager
        JComponent tabContent = (JComponent) tabsPane.getComponentAt(tabIndex);
        WindowBreadCrumbs breadCrumbs = tabs.get(tabContent);
        Window window = breadCrumbs.getCurrentWindow();

        if (window == null) {
            throw new IllegalArgumentException("window is null");
        }

        //WindowOpenMode map
        Map<Window, WindowOpenMode> detachOpenModes = new HashMap<>();
        detachOpenModes.put(window.<Window>getFrame(), windowOpenMode.get(window.<Window>getFrame()));
        windowOpenMode.remove(window.<Window>getFrame());
        Stack<Map.Entry<Window, Integer>> stack = stacks.get(breadCrumbs);
        for (Map.Entry<Window, Integer> entry : stack) {
            WindowOpenMode openMode = windowOpenMode.get(entry.getKey().<Window>getFrame());
            detachOpenModes.put(entry.getKey().<Window>getFrame(), openMode);
            windowOpenMode.remove(entry.getKey().<Window>getFrame());
        }

        tabs.remove(tabContent);

        Integer hashCode = windows.remove(window);
        tabsPane.remove(tabIndex);
        stacks.remove(breadCrumbs);

        final TopLevelFrame windowFrame = createTopLevelFrame(window.getCaption());
        App.getInstance().registerFrame(windowFrame);
        windowFrame.setVisible(true);
        windowFrame.getWindowManager().attachTab(breadCrumbs, stack, window, hashCode, tabContent, detachOpenModes);
    }

    public void attachTab(WindowBreadCrumbs breadCrumbs, Stack<Map.Entry<Window, Integer>> stack,
                          Window window, Integer hashCode, final JComponent tabContent,
                          Map<Window, WindowOpenMode> openModes) {
        frame.add(tabContent);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeTab(tabContent);
            }
        });
        tabs.put(tabContent, breadCrumbs);
        windowOpenMode.putAll(openModes);
        stacks.put(breadCrumbs, stack);
        for (Map.Entry<Window, Integer> entry : stack) {
            entry.getKey().setWindowManager(this);
        }
        window.setWindowManager(this);
        if (hashCode != null) {
            windows.put(window, hashCode);
        }
    }

    private String formatTabCaption(String caption, String description) {
        String s = formatTabDescription(caption, description);
        int maxLength = AppBeans.get(Configuration.class).getConfig(DesktopConfig.class).getMainTabCaptionLength();
        if (s.length() > maxLength) {
            return s.substring(0, maxLength) + "...";
        } else {
            return s;
        }
    }

    protected String formatTabDescription(final String caption, final String description) {
        if (!StringUtils.isEmpty(description)) {
            return String.format("%s: %s", caption, description);
        } else {
            return caption;
        }
    }

    @Override
    protected void showFrame(Component parent, IFrame frame) {
        // the same as web window manager does
        if (parent instanceof Component.Container) {
            Component.Container container = (Component.Container) parent;
            for (Component c : container.getComponents()) {
                if (c instanceof Component.Disposable) {
                    Component.Disposable disposable =
                            (Component.Disposable) c;
                    if (!disposable.isDisposed()) {
                        disposable.dispose();
                    }
                }
                container.remove(c);
            }
            container.add(frame);
        } else {
            throw new IllegalStateException(
                    "Parent component must be com.haulmont.cuba.gui.components.Component.Container"
            );
        }
    }

    @Override
    public void close(Window window) {
        if (window instanceof Window.Wrapper) {
            window = ((Window.Wrapper) window).getWrappedWindow();
        }

        final WindowOpenMode openMode = windowOpenMode.get(window);
        if (openMode == null) {
            log.warn("Problem closing window " + window + " : WindowOpenMode not found");
            return;
        }
        disableSavingScreenHistory = false;
        closeWindow(window, openMode);
        windowOpenMode.remove(window);
        windows.remove(openMode.getWindow());
    }

    protected void closeWindow(Window window, WindowOpenMode openMode) {
        if (!disableSavingScreenHistory) {
            screenHistorySupport.saveScreenHistory(window, openMode.getOpenType());
        }

        switch (openMode.openType) {
            case DIALOG: {
                JDialog dialog = (JDialog) openMode.getData();
                dialog.setVisible(false);
                dialog.dispose();
                cleanupAfterModalDialogClosed(window);

                fireListeners(window, tabs.size() != 0);
                break;
            }
            case NEW_TAB:
            case NEW_WINDOW: {
                JComponent layout = (JComponent) openMode.getData();
                layout.remove(DesktopComponentsHelper.getComposition(window));
                if (isMainWindowManager) {
                    tabsPane.remove(layout);
                }

                WindowBreadCrumbs windowBreadCrumbs = tabs.get(layout);
                if (windowBreadCrumbs != null) {
                    windowBreadCrumbs.clearListeners();
                    windowBreadCrumbs.removeWindow();
                }

                tabs.remove(layout);
                stacks.remove(windowBreadCrumbs);

                fireListeners(window, tabs.size() != 0);
                if (!isMainWindowManager) {
                    closeFrame(getFrame());
                }
                break;
            }
            case THIS_TAB: {
                JComponent layout = (JComponent) openMode.getData();

                final WindowBreadCrumbs breadCrumbs = tabs.get(layout);
                if (breadCrumbs == null)
                    throw new IllegalStateException("Unable to close screen: breadCrumbs not found");

                breadCrumbs.removeWindow();
                Window currentWindow = breadCrumbs.getCurrentWindow();
                if (!stacks.get(breadCrumbs).empty()) {
                    Map.Entry<Window, Integer> entry = stacks.get(breadCrumbs).pop();
                    putToWindowMap(entry.getKey(), entry.getValue());
                }
                JComponent component = DesktopComponentsHelper.getComposition(currentWindow);
                final java.awt.Component focusedCmp = windowOpenMode.get(currentWindow.getFrame()).getFocusOwner();
                if (focusedCmp != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            focusedCmp.requestFocus();
                        }
                    });
                }
                layout.remove(DesktopComponentsHelper.getComposition(window));
                layout.add(component);
                if (isMainWindowManager) {
                    // If user clicked on close button maybe selectedIndex != tabsPane.getSelectedIndex()
                    // Refs #1117
                    int selectedIndex = 0;
                    while ((selectedIndex < tabs.size()) &&
                            (tabsPane.getComponentAt(selectedIndex) != layout))
                        selectedIndex++;
                    if (selectedIndex == tabs.size())
                        selectedIndex = tabsPane.getSelectedIndex();

                    setWindowCaption(currentWindow.getCaption(), currentWindow.getDescription(), selectedIndex);
                } else {
                    setTopLevelWindowCaption(currentWindow.getCaption());
                    component.revalidate();
                    component.repaint();
                }

                fireListeners(window, tabs.size() != 0);
                break;
            }

            default:
                throw new UnsupportedOperationException();
        }
    }

    protected void cleanupAfterModalDialogClosed(@Nullable Window closingWindow) {
        WindowOpenMode previous = null;
        for (Iterator<Window> it = windowOpenMode.keySet().iterator(); it.hasNext(); ) {
            Window w = it.next();
            // Check if there is a modal window opened before the current
            WindowOpenMode mode = windowOpenMode.get(w);
            if (w != closingWindow && mode.getOpenType().equals(OpenType.DIALOG)) {
                previous = mode;
            }
            // If there are windows opened after the current, close them
            if (w == closingWindow && it.hasNext()) {
                close(it.next());
                break;
            }
        }
        if (previous == null) {
            frame.activate();
        } else if (previous.getData() instanceof DialogWindow) {
            ((DialogWindow) previous.getData()).enableWindow();
        } else if (previous.getData() instanceof JDialog) {
            ((JDialog) previous.getData()).requestFocus();
        }
    }

    @Override
    public void showNotification(String caption, IFrame.NotificationType type) {
        frame.showNotification(caption, type);
    }

    @Override
    public void showNotification(String caption, String description, IFrame.NotificationType type) {
        frame.showNotification(caption, description, type);
    }

    @Override
    public void showMessageDialog(final String title, final String message, IFrame.MessageType messageType) {
        final int swingMessageType = DesktopComponentsHelper.convertMessageType(messageType);
        final String msg = IFrame.MessageType.isHTML(messageType) ?
                "<html>" + ComponentsHelper.preprocessHtmlMessage(message) : message;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(frame, msg, title, swingMessageType);
            }
        });
    }

    @Override
    public void showOptionDialog(String title, String message, IFrame.MessageType messageType, final Action[] actions) {
        final JDialog dialog = new JDialog(frame, title, false);

        Object[] options = new Object[actions.length];
        for (int i = 0; i < actions.length; i++) {
            final Action action = actions[i];
            JButton btn = new JButton(action.getCaption());

            String icon = action.getIcon();

            if (icon != null)
                btn.setIcon(App.getInstance().getResources().getIcon(icon));

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    action.actionPerform(null);
                    dialog.setVisible(false);
                    cleanupAfterModalDialogClosed(null);
                }
            });

            btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, DesktopComponentsHelper.BUTTON_HEIGHT));
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, DesktopComponentsHelper.BUTTON_HEIGHT));
            options[i] = btn;
        }

        int optionType;

        if (options.length == 1)
            optionType = JOptionPane.DEFAULT_OPTION;
        else if (options.length == 2)
            optionType = JOptionPane.YES_NO_OPTION;
        else if (options.length == 3)
            optionType = JOptionPane.YES_NO_CANCEL_OPTION;
        else
            throw new UnsupportedOperationException("Not more than 3 actions supported");

        final String msg = IFrame.MessageType.isHTML(messageType) ?
                "<html>" + ComponentsHelper.preprocessHtmlMessage(message) : message;

        final JOptionPane optionPane = new JOptionPane(
                msg,
                DesktopComponentsHelper.convertMessageType(messageType),
                optionType,
                null,
                options,
                null
        );

        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        optionPane.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        String prop = e.getPropertyName();
                        if (dialog.isVisible()
                                && (e.getSource() == optionPane)
                                && (prop.equals(JOptionPane.VALUE_PROPERTY))
                                && new Integer(-1).equals(e.getNewValue())) {

                            dialog.setVisible(false);
                            cleanupAfterModalDialogClosed(null);
                        }
                    }
                });

        KeyStroke okKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK, true);

        InputMap inputMap = optionPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = optionPane.getActionMap();

        inputMap.put(okKeyStroke, "okAction");
        actionMap.put("okAction", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Unfortunately, JComponent.processKeyBinding method allows KEY_RELEASE to pass to this point.
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    for (Action action : actions) {
                        if (action instanceof DialogAction) {
                            switch (((DialogAction) action).getType()) {
                                case OK:
                                case YES:
                                    action.actionPerform(null);
                                    dialog.setVisible(false);
                                    cleanupAfterModalDialogClosed(null);
                                    return;
                            }
                        }
                    }
                }
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        frame.deactivate(null);
        dialog.setVisible(true);
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Unable to show web page " + url, e);
        }
    }

    public void setCurrentWindowCaption(Window window, String caption, String description) {
        WindowOpenMode openMode;
        if (window instanceof Window.Wrapper)
            openMode = windowOpenMode.get(((Window.Wrapper) window).getWrappedWindow());
        else
            openMode = windowOpenMode.get(window);

        OpenType openType = openMode.getOpenType();
        String formattedCaption = formatTabDescription(caption, description);

        if (openType != OpenType.DIALOG) {
            if (tabsPane == null)
                return;
            int selectedIndex = tabsPane.getSelectedIndex();
            if (selectedIndex != -1) {
                setWindowCaption(caption, description, selectedIndex);
            }
        } else {
            JDialog jDialog = (JDialog) windowOpenMode.get(window).getData();
            if (jDialog != null) {
                jDialog.setTitle(formattedCaption);
            }
        }

        window.setCaption(formattedCaption);
    }

    protected JComponent findTab(Integer hashCode) {
        Set<Map.Entry<JComponent, WindowBreadCrumbs>> set = tabs.entrySet();
        for (Map.Entry<JComponent, WindowBreadCrumbs> entry : set) {
            Window currentWindow = entry.getValue().getCurrentWindow();
            if (hashCode.equals(getWindowHashCode(currentWindow)))
                return entry.getKey();
        }
        return null;
    }

    /**
     * Release resources right before throwing away this WindowManager instance.
     */
    public void dispose() {
        for (WindowOpenMode openMode : windowOpenMode.values()) {
            if (openMode.getOpenType().equals(OpenType.DIALOG)) {
                JDialog dialog = (JDialog) openMode.getData();
                dialog.setVisible(false);
            }
        }
        // Stop background tasks
        AppBeans.get(WatchDog.class).stopTasks();
        // Dispose windows
        for (Window window : windowOpenMode.keySet()) {
            IFrame frame = window.getFrame();
            if (frame instanceof Component.Disposable)
                ((Component.Disposable) frame).dispose();
        }

        tabs.clear();
        windowOpenMode.clear();
        stacks.clear();
    }

    protected static class WindowOpenMode {

        protected Window window;
        protected OpenType openType;
        protected Object data;
        private java.awt.Component focusOwner;

        public WindowOpenMode(Window window, OpenType openType) {
            this.window = window;
            this.openType = openType;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Window getWindow() {
            return window;
        }

        public OpenType getOpenType() {
            return openType;
        }

        public java.awt.Component getFocusOwner() {
            return focusOwner;
        }

        public void setFocusOwner(java.awt.Component focusOwner) {
            this.focusOwner = focusOwner;
        }
    }

    public class TabCloseTask implements Runnable {
        private final WindowBreadCrumbs breadCrumbs;

        public TabCloseTask(WindowBreadCrumbs breadCrumbs) {
            this.breadCrumbs = breadCrumbs;
        }

        @Override
        public void run() {
            Window windowToClose = breadCrumbs.getCurrentWindow();
            if (windowToClose != null) {
                windowToClose.closeAndRun("close", new TabCloseTask(breadCrumbs));
            }
        }
    }

    public void addWindowData(Window window, Object windowData, OpenType openType) {
        WindowOpenMode openMode = new WindowOpenMode(window, openType);
        openMode.setData(windowData);
        if (window instanceof Window.Wrapper) {
            Window wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
            windowOpenMode.put(wrappedWindow, openMode);
        } else {
            windowOpenMode.put(window, openMode);
        }

        addShortcuts(window);
        afterShowWindow(window);
    }

    public void checkModificationsAndCloseAll(final Runnable runIfOk, final @Nullable Runnable runIfCancel) {
        boolean modified = false;
        for (Window window : getOpenWindows()) {
            if (!disableSavingScreenHistory) {
                screenHistorySupport.saveScreenHistory(window, windowOpenMode.get(window).getOpenType());
            }

            if (window instanceof WrappedWindow && ((WrappedWindow) window).getWrapper() != null)
                ((WrappedWindow) window).getWrapper().saveSettings();
            else
                window.saveSettings();

            if (window.getDsContext() != null && window.getDsContext().isModified()) {
                modified = true;
            }
        }
        disableSavingScreenHistory = true;
        if (modified) {
            showOptionDialog(
                    messages.getMainMessage("closeUnsaved.caption"),
                    messages.getMainMessage("discardChangesOnClose"),
                    IFrame.MessageType.WARNING,
                    new Action[]{
                            new com.haulmont.cuba.gui.components.AbstractAction(
                                    messages.getMainMessage("closeApplication")) {
                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    if (runIfOk != null)
                                        runIfOk.run();
                                }

                                @Override
                                public String getIcon() {
                                    return "icons/ok.png";
                                }
                            },
                            new com.haulmont.cuba.gui.components.AbstractAction(
                                    messages.getMainMessage("actions.Cancel")) {
                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    if (runIfCancel != null)
                                        runIfCancel.run();
                                }

                                @Override
                                public String getIcon() {
                                    return "icons/cancel.png";
                                }
                            }
                    }
            );
        } else {
            runIfOk.run();
        }
    }

}
