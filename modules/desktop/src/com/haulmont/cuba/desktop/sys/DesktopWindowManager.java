/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.desktop.TopLevelFrame;
import com.haulmont.cuba.desktop.gui.components.DesktopAbstractComponent;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.gui.components.DesktopWindow;
import com.haulmont.cuba.desktop.sys.validation.ValidationAlertHolder;
import com.haulmont.cuba.desktop.sys.validation.ValidationAwareAction;
import com.haulmont.cuba.desktop.sys.validation.ValidationAwareWindowClosingListener;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.dev.LayoutAnalyzer;
import com.haulmont.cuba.gui.dev.LayoutTip;
import com.haulmont.cuba.gui.executors.*;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

import static com.haulmont.cuba.gui.components.Component.AUTO_SIZE;
import static com.haulmont.cuba.gui.components.IFrame.MessageType;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopWindowManager extends WindowManager {

    private static final Log log = LogFactory.getLog(DesktopWindowManager.class);

    protected static final float NEW_WINDOW_SCALE = 0.7f;

    protected JTabbedPane tabsPane;

    protected final Map<JComponent, WindowBreadCrumbs> tabs = new HashMap<>();
    protected final Map<Window, WindowOpenMode> windowOpenMode = new LinkedHashMap<>();
    protected final Map<WindowBreadCrumbs, Stack<Map.Entry<Window, Integer>>> stacks = new HashMap<>();
    protected final Map<Window, Integer> windows = new HashMap<>();
    protected final TopLevelFrame frame;
    protected final boolean isMainWindowManager;

    protected boolean disableSavingScreenHistory;
    protected ScreenHistorySupport screenHistorySupport = new ScreenHistorySupport();

    protected boolean recursiveFramesClose = false;

    public DesktopWindowManager(TopLevelFrame frame) {
        this.frame = frame;
        isMainWindowManager = frame == App.getInstance().getMainFrame();
    }

    public TopLevelFrame getFrame() {
        return frame;
    }

    public void setTabsPane(final JTabbedPane tabsPane) {
        this.tabsPane = tabsPane;

        // todo move to config
        tabsPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke("control W"),
                "closeTab"
        );
        tabsPane.getActionMap().put("closeTab", new ValidationAwareAction() {
            @Override
            public void actionPerformedAfterValidation(ActionEvent e) {
                closeTab((JComponent) tabsPane.getSelectedComponent());
            }
        });
    }

    protected void closeTab(JComponent tabContent) {
        if (tabContent == null)
            return;
        WindowBreadCrumbs breadCrumbs = tabs.get(tabContent);
        // may be window already closed
        if (breadCrumbs != null) {
            Runnable closeTask = new TabCloseTask(breadCrumbs);
            closeTask.run();
        }
    }

    @Override
    public Collection<Window> getOpenWindows() {
        return new ArrayList<>(windowOpenMode.keySet());
    }

    @Override
    public void selectWindowTab(Window window) {
        if (isMainWindowManager) {
            WindowOpenMode openMode = windowOpenMode.get(window);
            if (openMode != null) {
                OpenType openType = openMode.getOpenType();
                if (openType == OpenType.NEW_TAB || openType == OpenType.THIS_TAB) {
                    // show in tabsheet
                    JComponent layout = (JComponent) openMode.getData();
                    tabsPane.setSelectedComponent(layout);
                }
            }
        }
    }

    @Override
    public void setWindowCaption(Window window, String caption, String description) {
        Window desktopWindow = window;
        if (window instanceof Window.Wrapper) {
            desktopWindow = ((Window.Wrapper) window).getWrappedWindow();
        }
        window.setCaption(caption);

        String formattedCaption = formatTabDescription(caption, description);
        WindowOpenMode openMode = windowOpenMode.get(desktopWindow);

        if (openMode != null) {
            OpenType openType = openMode.getOpenType();

            if (openType != OpenType.DIALOG) {
                if (tabsPane != null) {
                    int selectedIndex = tabsPane.getSelectedIndex();
                    if (selectedIndex != -1) {
                        setActiveWindowCaption(caption, description, selectedIndex);
                    }
                } else if (!isMainWindowManager) {
                    setTopLevelWindowCaption(formattedCaption);
                }
            } else {
                JDialog jDialog = (JDialog) openMode.getData();
                if (jDialog != null) {
                    jDialog.setTitle(formattedCaption);
                }
            }
        }
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

    protected Integer getWindowHashCode(Window window) {
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
    protected void showWindow(final Window window, final String caption, final String description, OpenType openType, boolean multipleOpen) {
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
                        WindowBreadCrumbs oldBreadCrumbs = tabs.get(tab);

                        final Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                        selectWindowTab(((Window.Wrapper) oldBreadCrumbs.getCurrentWindow()).getWrappedWindow());

                        final int finalTabPosition = getTabPosition(tab);
                        oldWindow.closeAndRun(MAIN_MENU_ACTION_ID, new Runnable() {
                            @Override
                            public void run() {
                                showWindow(window, caption, description, OpenType.NEW_TAB, false);

                                Window wrappedWindow = window;
                                if (window instanceof Window.Wrapper) {
                                    wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
                                }

                                if (finalTabPosition >= 0 && finalTabPosition < tabsPane.getComponentCount() - 1) {
                                    moveWindowTab(wrappedWindow, finalTabPosition);
                                }
                            }
                        });
                        return;
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
        afterShowWindow(window);
    }

    protected int getTabPosition(JComponent tab) {
        int position = -1;
        for (int i = 0; i < tabsPane.getTabCount(); i++) {
            if (tab.equals(tabsPane.getComponentAt(i))) {
                position = i;
            }
        }
        return position;
    }

    /**
     * @param window   Window implementation (DesktopWindow)
     * @param position new tab position
     */
    protected void moveWindowTab(Window window, int position) {
        if (isMainWindowManager && position >= 0 && position < tabsPane.getComponentCount()) {
            WindowOpenMode openMode = windowOpenMode.get(window);
            if (openMode != null) {
                OpenType openType = openMode.getOpenType();
                if (openType == OpenType.NEW_TAB || openType == OpenType.THIS_TAB) {
                    // show in tabsheet
                    JComponent layout = (JComponent) openMode.getData();

                    int currentPosition = getTabPosition(layout);

                    String label = tabsPane.getTitleAt(currentPosition);
                    Icon icon = tabsPane.getIconAt(currentPosition);
                    Icon iconDis = tabsPane.getDisabledIconAt(currentPosition);
                    String tooltip = tabsPane.getToolTipTextAt(currentPosition);
                    boolean enabled = tabsPane.isEnabledAt(currentPosition);
                    int keycode = tabsPane.getMnemonicAt(currentPosition);
                    int mnemonicLoc = tabsPane.getDisplayedMnemonicIndexAt(currentPosition);
                    Color fg = tabsPane.getForegroundAt(currentPosition);
                    Color bg = tabsPane.getBackgroundAt(currentPosition);
                    java.awt.Component tabHeaderComponent = tabsPane.getTabComponentAt(currentPosition);

                    tabsPane.remove(layout);

                    tabsPane.insertTab(label, icon, layout, tooltip, position);

                    tabsPane.setDisabledIconAt(position, iconDis);
                    tabsPane.setEnabledAt(position, enabled);
                    tabsPane.setMnemonicAt(position, keycode);
                    tabsPane.setDisplayedMnemonicIndexAt(position, mnemonicLoc);
                    tabsPane.setForegroundAt(position, fg);
                    tabsPane.setBackgroundAt(position, bg);
                    tabsPane.setTabComponentAt(position, tabHeaderComponent);

                    tabsPane.setSelectedComponent(layout);
                }
            }
        }
    }

    protected void showInMainWindowManager(Window window, String caption, String description, OpenType openType, boolean multipleOpen) {
        DesktopWindowManager mainMgr = App.getInstance().getMainFrame().getWindowManager();
        windows.remove(window);
        window.setWindowManager(mainMgr);
        mainMgr.showWindow(window, caption, openType, multipleOpen);
    }

    protected TopLevelFrame createTopLevelFrame(String caption) {
        final TopLevelFrame windowFrame = new TopLevelFrame(caption);
        Dimension size = frame.getSize();
        int width = Math.round(size.width * NEW_WINDOW_SCALE);
        int height = Math.round(size.height * NEW_WINDOW_SCALE);

        windowFrame.setSize(width, height);
        windowFrame.setLocationRelativeTo(frame);

        return windowFrame;
    }

    protected void closeFrame(TopLevelFrame frame) {
        frame.setVisible(false);
        frame.dispose();
        frame.getWindowManager().dispose();
        App.getInstance().unregisterFrame(getFrame());
    }

    protected JComponent showNewWindow(Window window, String caption) {
        window.setHeight("100%");
        window.setWidth("100%");

        TopLevelFrame windowFrame = createTopLevelFrame(caption);

        DialogParams dialogParams = getDialogParams();
        Dimension dimension = new Dimension();

        dimension.width = 800;
        if (dialogParams.getWidth() != null) {
            dimension.width = dialogParams.getWidth();
        }

        dimension.height = 500;
        if (dialogParams.getHeight() != null) {
            dimension.height = dialogParams.getHeight();
        }

        boolean resizable = true;
        if (dialogParams.getResizable() != null) {
            resizable = dialogParams.getResizable();
        }
        windowFrame.setResizable(resizable);
        windowFrame.setMinimumSize(dimension);
        windowFrame.pack();
        getDialogParams().reset();

        WindowBreadCrumbs breadCrumbs = createBreadCrumbs();
        breadCrumbs.addWindow(window);

        JComponent tabContent = createTabPanel(window, breadCrumbs);

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
        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

        String closeShortcut = clientConfig.getCloseShortcut();
        window.addAction(new com.haulmont.cuba.gui.components.AbstractAction("closeWindowShortcutAction", closeShortcut) {
            @Override
            public void actionPerform(Component component) {
                window.close("close");
            }
        });

        String previousTabShortcut = clientConfig.getPreviousTabShortcut();
        window.addAction(new com.haulmont.cuba.gui.components.AbstractAction("onPreviousTab", previousTabShortcut) {
            @Override
            public void actionPerform(Component component) {
                if (window.getWindowManager() != DesktopWindowManager.this) {
                    // detached tab
                    return;
                }

                if (isMainWindowManager && getLastDialogWindow() == null && tabsPane.getTabCount() > 1) {
                    int selectedIndex = getSelectedTabIndex();

                    int newIndex = (selectedIndex + tabsPane.getTabCount() - 1) % tabsPane.getTabCount();
                    java.awt.Component newTab = tabsPane.getComponentAt(newIndex);
                    tabsPane.setSelectedComponent(newTab);

                    moveFocus(newTab);
                }
            }
        });

        String nextTabShortcut = clientConfig.getNextTabShortcut();
        window.addAction(new com.haulmont.cuba.gui.components.AbstractAction("onNextTab", nextTabShortcut) {
            @Override
            public void actionPerform(Component component) {
                if (window.getWindowManager() != DesktopWindowManager.this) {
                    // detached tab
                    return;
                }

                if (isMainWindowManager && getLastDialogWindow() == null && tabsPane.getTabCount() > 1) {
                    int selectedIndex = getSelectedTabIndex();

                    int newIndex = (selectedIndex + 1) % tabsPane.getTabCount();
                    java.awt.Component newTab = tabsPane.getComponentAt(newIndex);
                    tabsPane.setSelectedComponent(newTab);

                    moveFocus(newTab);
                }
            }
        });
    }

    protected void moveFocus(java.awt.Component tab) {
        Window window = tabs.get(tab).getCurrentWindow();

        if (window != null) {
            String focusComponentId = window.getFocusComponent();

            boolean focused = false;
            if (focusComponentId != null) {
                com.haulmont.cuba.gui.components.Component focusComponent = window.getComponent(focusComponentId);
                if (focusComponent != null) {
                    if (focusComponent.isEnabled() && focusComponent.isVisible()) {
                        focusComponent.requestFocus();
                        focused = true;
                    }
                }
            }

            if (!focused && window instanceof Window.Wrapper) {
                Window.Wrapper wrapper = (Window.Wrapper) window;
                focused = ((DesktopWindow) wrapper.getWrappedWindow()).findAndFocusChildComponent();
                if (!focused) {
                    tabsPane.requestFocus();
                }
            }
        }
    }

    protected int getSelectedTabIndex() {
        java.awt.Component selectedComponent = tabsPane.getSelectedComponent();
        for (int i = 0; i < tabsPane.getTabCount(); i++) {
            if (selectedComponent == tabsPane.getComponentAt(i)) {
                return i;
            }
        }
        return -1;
    }

    protected JDialog showWindowDialog(final Window window, String caption, String description, boolean forciblyDialog) {
        final DialogWindow dialog = new DialogWindow(frame, caption);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        final DialogParams dialogParams = getDialogParams();
        JComponent jComponent = DesktopComponentsHelper.getComposition(window);
        dialog.add(jComponent);

        if (dialogParams.getCloseable() == null ||
                dialogParams.getCloseable()) {
            dialog.addWindowListener(new ValidationAwareWindowClosingListener() {
                @Override
                public void windowClosingAfterValidation(WindowEvent e) {
                    if (window.close("close", false)) {
                        dialog.dispose();
                    }
                }
            });
        }

        Dimension dim = new Dimension();
        if (forciblyDialog) {
            window.setHeight("100%");
            // todo move it to desktop application preferences
            dim.width = 800;
            dim.height = 500;

            dialog.setResizable(BooleanUtils.isNotFalse(dialogParams.getResizable()));
            if (!dialog.isResizable()) {
                dialog.setFixedHeight(dim.height);
                dialog.setFixedWidth(dim.width);
            }

            window.setHeight("100%");
        } else {
            dialog.setResizable(BooleanUtils.isTrue(dialogParams.getResizable()));
            if (dialogParams.getWidth() == null) {
                dim.width = 600;
                if (!dialog.isResizable()) {
                    dialog.setFixedWidth(dim.width);
                }
            } else if (dialogParams.getWidth() == DialogParams.AUTO_SIZE_PX) {
                window.setWidth(AUTO_SIZE);
            } else {
                dim.width = dialogParams.getWidth();
                if (!dialog.isResizable()) {
                    dialog.setFixedWidth(dim.width);
                }
            }

            if (dialogParams.getHeight() != null && dialogParams.getHeight() != DialogParams.AUTO_SIZE_PX) {
                dim.height = dialogParams.getHeight();

                if (!dialog.isResizable()) {
                    dialog.setFixedHeight(dim.height);
                }
                window.setHeight("100%");
            } else {
                window.setHeight(AUTO_SIZE);
            }
        }

        if (dialogParams.getCloseable() != null) {
            if (!dialogParams.getCloseable())
                dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        }

        dialogParams.reset();

        dialog.setMinimumSize(dim);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);

        boolean modal = true;
        if (!hasModalWindow() && dialogParams.getModal() != null) {
            modal = dialogParams.getModal();
        }

        if (modal) {
            DialogWindow lastDialogWindow = getLastDialogWindow();
            if (lastDialogWindow == null)
                frame.deactivate(null);
            else
                lastDialogWindow.disableWindow(null);
        }

        dialog.setVisible(true);

        JPopupMenu popupMenu = createWindowPopupMenu(window);
        if (popupMenu.getComponentCount() > 0) {
            jComponent.setComponentPopupMenu(popupMenu);
        }

        return dialog;
    }

    protected JComponent showWindowThisTab(Window window, String caption, String description) {
        getDialogParams().reset();

        window.setWidth("100%");
        window.setHeight("100%");

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
        Window currentWindowFrame = currentWindow.getFrame();
        windowOpenMode.get(currentWindowFrame).setFocusOwner(frame.getFocusOwner());

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
            setActiveWindowCaption(caption, description, tabsPane.getSelectedIndex());
        } else {
            setTopLevelWindowCaption(caption);
            component.revalidate();
            component.repaint();
        }

        return layout;
    }

    protected void setActiveWindowCaption(String caption, String description, int tabIndex) {
        ButtonTabComponent tabComponent = (ButtonTabComponent) tabsPane.getTabComponentAt(tabIndex);
        String formattedCaption = formatTabCaption(caption, description);
        tabComponent.setCaption(formattedCaption);
    }

    protected void setTopLevelWindowCaption(String caption) {
        frame.setTitle(caption);
    }

    protected WindowBreadCrumbs createBreadCrumbs() {
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
        getDialogParams().reset();

        window.setWidth("100%");
        window.setHeight("100%");

        final WindowBreadCrumbs breadCrumbs = createBreadCrumbs();
        stacks.put(breadCrumbs, new Stack<Map.Entry<Window, Integer>>());

        breadCrumbs.addWindow(window);
        JComponent tabContent = createNewTab(window, caption, description, breadCrumbs, tabPosition);
        tabs.put(tabContent, breadCrumbs);
        return tabContent;
    }

    protected JPanel createTabPanel(Window window, WindowBreadCrumbs breadCrumbs) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(breadCrumbs, BorderLayout.NORTH);
        JComponent composition = DesktopComponentsHelper.getComposition(window);
        panel.add(composition, BorderLayout.CENTER);
        return panel;
    }

    protected JComponent createNewTab(Window window, String caption, String description, WindowBreadCrumbs breadCrumbs, Integer tabPosition) {
        JPanel panel = createTabPanel(window, breadCrumbs);
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
                    public void onTabClose(final int tabIndex) {
                        ValidationAlertHolder.runIfValid(new Runnable() {
                            @Override
                            public void run() {
                                JComponent tabContent = (JComponent) tabsPane.getComponentAt(tabIndex);
                                closeTab(tabContent);
                            }
                        });
                    }
                },
                new ButtonTabComponent.DetachListener() {
                    @Override
                    public void onDetach(final int tabIndex) {
                        ValidationAlertHolder.runIfValid(new Runnable() {
                            @Override
                            public void run() {
                                detachTab(tabIndex);
                            }
                        });
                    }
                }
        );
        tabsPane.setTabComponentAt(idx, tabComponent);
        tabsPane.setSelectedIndex(idx);

        initTabContextMenu(tabComponent);

        return panel;
    }

    protected void initTabContextMenu(JComponent tabComponent) {
        tabComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dispatchToParent(e);
                if (e.isPopupTrigger()) {
                    showTabPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dispatchToParent(e);
                if (e.isPopupTrigger()) {
                    showTabPopup(e);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                dispatchToParent(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                dispatchToParent(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                dispatchToParent(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                dispatchToParent(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dispatchToParent(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                dispatchToParent(e);
            }

            public void dispatchToParent(MouseEvent e) {
                tabsPane.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, tabsPane));
            }
        });
    }

    public void showTabPopup(MouseEvent e) {
        JComponent tabHeaderComponent = (JComponent) e.getComponent();
        int tabIndex = getTabIndex(tabHeaderComponent);
        JComponent tabContent = (JComponent) tabsPane.getComponentAt(tabIndex);
        WindowBreadCrumbs windowBreadCrumbs = tabs.get(tabContent);

        Window window = windowBreadCrumbs.getCurrentWindow();

        JPopupMenu popupMenu = createWindowPopupMenu(window);
        if (popupMenu.getComponentCount() > 0) {
            popupMenu.show(tabHeaderComponent, e.getX(), e.getY());
        }
    }

    protected int getTabIndex(java.awt.Component tabHeaderComponent) {
        for (int i = 0; i < tabsPane.getTabCount(); i++) {
            if (tabsPane.getTabComponentAt(i) == tabHeaderComponent)
                return i;
        }
        return -1;
    }

    protected JPopupMenu createWindowPopupMenu(final Window window) {
        JPopupMenu popupMenu = new JPopupMenu();

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

        if (clientConfig.getLayoutAnalyzerEnabled()) {
            JMenuItem analyzeLayoutItem = new JMenuItem(messages.getMainMessage("actions.analyzeLayout"));
            analyzeLayoutItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LayoutAnalyzer analyzer = new LayoutAnalyzer();
                    List<LayoutTip> tipsList = analyzer.analyze(window);

                    if (tipsList.isEmpty()) {
                        showNotification("No layout problems found", IFrame.NotificationType.HUMANIZED);
                    } else {
                        window.openWindow("layoutAnalyzer", OpenType.DIALOG, ParamsMap.of("tipsList", tipsList));
                    }
                }
            });
            popupMenu.add(analyzeLayoutItem);
        }
        return popupMenu;
    }

    protected void detachTab(int tabIndex) {
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
        frame.addWindowListener(new ValidationAwareWindowClosingListener() {
            @Override
            public void windowClosingAfterValidation(WindowEvent e) {
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

        JPopupMenu popupMenu = createWindowPopupMenu(window);
        if (popupMenu.getComponentCount() > 0) {
            frame.getRootPane().setComponentPopupMenu(popupMenu);
        }
    }

    protected String formatTabCaption(String caption, String description) {
        String s = formatTabDescription(caption, description);
        Configuration configuration = AppBeans.get(Configuration.NAME);
        int maxLength = configuration.getConfig(DesktopConfig.class).getMainTabCaptionLength();
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
                Window currentWindowFrame = currentWindow.getFrame();
                final java.awt.Component focusedCmp = windowOpenMode.get(currentWindowFrame).getFocusOwner();
                if (focusedCmp != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            focusedCmp.requestFocus();
                        }
                    });
                }
                layout.remove(DesktopComponentsHelper.getComposition(window));

                if (App.getInstance().getConnection().isConnected()) {
                    layout.add(component);
                    if (isMainWindowManager) {
                        // If user clicked on close button maybe selectedIndex != tabsPane.getSelectedIndex()
                        // Refs #1117
                        int selectedIndex = 0;
                        while ((selectedIndex < tabs.size()) &&
                                (tabsPane.getComponentAt(selectedIndex) != layout)) {
                            selectedIndex++;
                        }
                        if (selectedIndex == tabs.size()) {
                            selectedIndex = tabsPane.getSelectedIndex();
                        }

                        setActiveWindowCaption(currentWindow.getCaption(), currentWindow.getDescription(), selectedIndex);
                    } else {
                        setTopLevelWindowCaption(currentWindow.getCaption());
                        component.revalidate();
                        component.repaint();
                    }
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
    public void showMessageDialog(final String title, final String message, MessageType messageType) {
        showOptionDialog(title, message, messageType, false, new Action[]{
                new DialogAction(DialogAction.Type.OK)
        }, "messageDialog");
    }

    protected JPanel createButtonsPanel(Action[] actions, final JDialog dialog) {
        JPanel buttonsPanel = new JPanel();
        for (final Action action : actions) {
            JButton button = new JButton(action.getCaption());
            String icon = action.getIcon();

            if (icon != null) {
                button.setIcon(App.getInstance().getResources().getIcon(icon));
            }

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    action.actionPerform(null);
                    dialog.setVisible(false);
                    cleanupAfterModalDialogClosed(null);
                }
            });

            button.setPreferredSize(new Dimension(button.getPreferredSize().width, DesktopComponentsHelper.BUTTON_HEIGHT));
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, DesktopComponentsHelper.BUTTON_HEIGHT));

            buttonsPanel.add(button);
        }
        return buttonsPanel;
    }

    protected void initShortcut(final JDialog dialog, JPanel panel, final Action[] actions) {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

        KeyCombination okCombination = KeyCombination.create(clientConfig.getCommitShortcut());
        KeyStroke okKeyStroke = DesktopComponentsHelper.convertKeyCombination(okCombination);

        InputMap inputMap = panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = panel.getActionMap();

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
    }

    protected void showOptionDialog(final String title, final String message, MessageType messageType,
                                    boolean alwaysModal, final Action[] actions, String debugName) {
        final DialogWindow dialog = new DialogWindow(frame, title);

        if (App.getInstance().isTestMode()) {
            dialog.setName(debugName);
        }
        dialog.setModal(false);

        if (actions.length == 1) {
            final Action action = actions[0];
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    action.actionPerform(null);
                    dialog.setVisible(false);
                    cleanupAfterModalDialogClosed(null);
                }
            });
        } else if (actions.length > 1) {
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        }

        int width = 500;
        DialogParams dialogParams = getDialogParams();
        if (dialogParams.getWidth() != null) {
            width = dialogParams.getWidth();
        }

        LC lc = new LC();
        lc.insets("10");

        MigLayout layout = new MigLayout(lc);
        final JPanel panel = new JPanel(layout);
        Icon icon = convertMessageType(messageType);
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            panel.add(iconLabel, "aligny top");
        }

        String msg = message;
        if (!MessageType.isHTML(messageType)) {
            msg = StringEscapeUtils.escapeHtml(msg);
            msg = ComponentsHelper.preprocessHtmlMessage("<html>" + msg + "</html>");
        } else {
            msg = "<html>" + msg + "</html>";
        }
        JLabel msgLabel = new JLabel(msg);

        panel.add(msgLabel, "width 100%, wrap, growy 0");

        if (icon != null) {
            panel.add(new JLabel(" "));
        }
        panel.add(createButtonsPanel(actions, dialog), "alignx right");

        dialog.setLayout(new MigLayout(new LC().insets("0").width(width + "px")));
        dialog.setFixedWidth(width);
        dialog.add(panel, "width 100%, growy 0");

        initShortcut(dialog, panel, actions);

        dialog.pack();
        dialog.setResizable(false);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.revalidate();
                panel.repaint();

                java.awt.Container container = panel.getTopLevelAncestor();
                if (container instanceof JDialog) {
                    JDialog dialog = (JDialog) container;
                    dialog.pack();
                }

                dialog.requestFocus();
            }
        });
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(frame);

        boolean modal = true;
        if (!alwaysModal) {
            if (!hasModalWindow() && dialogParams.getModal() != null) {
                modal = dialogParams.getModal();
            }
        }

        if (modal) {
            DialogWindow lastDialogWindow = getLastDialogWindow();
            if (lastDialogWindow == null) {
                frame.deactivate(null);
            } else {
                lastDialogWindow.disableWindow(null);
            }
        }
    }

    protected Icon convertMessageType(MessageType messageType) {
        switch (messageType) {
            case CONFIRMATION:
            case CONFIRMATION_HTML:
                return UIManager.getIcon("OptionPane.informationIcon");
            case WARNING:
            case WARNING_HTML:
                return UIManager.getIcon("OptionPane.warningIcon");
            default:
                return null;
        }
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, final Action[] actions) {
        showOptionDialog(title, message, messageType, true, actions, "optionDialog");
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Unable to show web page " + url, e);
        }
    }

    /**
     * @deprecated Use {@link WindowManager#setWindowCaption(com.haulmont.cuba.gui.components.Window, String, String)}
     */
    @Deprecated
    public void setCurrentWindowCaption(Window window, String caption, String description) {
        setWindowCaption(window, caption, description);
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

        if (isMainWindowManager) {
            // Stop background tasks
            WatchDog watchDog = AppBeans.get(WatchDog.NAME);
            watchDog.stopTasks();
        }

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
        protected java.awt.Component focusOwner;

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
        protected final WindowBreadCrumbs breadCrumbs;

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
    }

    public void checkModificationsAndCloseAll(final Runnable runIfOk, final @Nullable Runnable runIfCancel) {
        boolean modified = false;
        for (Window window : getOpenWindows()) {
            if (!disableSavingScreenHistory) {
                screenHistorySupport.saveScreenHistory(window, windowOpenMode.get(window).getOpenType());
            }

            recursiveFramesClose = true;
            try {
                if (window instanceof WrappedWindow && ((WrappedWindow) window).getWrapper() != null) {
                    ((WrappedWindow) window).getWrapper().saveSettings();
                } else {
                    window.saveSettings();
                }
            } finally {
                recursiveFramesClose = false;
            }

            if (window.getDsContext() != null && window.getDsContext().isModified()) {
                modified = true;
            }
        }
        disableSavingScreenHistory = true;
        if (modified) {
            showOptionDialog(
                    messages.getMainMessage("closeUnsaved.caption"),
                    messages.getMainMessage("discardChangesOnClose"),
                    MessageType.WARNING,
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

    @Override
    protected SettingsImpl getSettingsImpl(String id) {
        return new AsyncSettingsImpl(id);
    }

    protected class AsyncSettingsImpl extends SettingsImpl {
        public AsyncSettingsImpl(String id) {
            super(id);
        }

        @Override
        public void commit() {
            if (modified && root != null) {
                final String xml = Dom4j.writeDocument(root.getDocument(), true);
                if (!recursiveFramesClose) {
                    saveSettingsAsync(xml);
                } else {
                    getSettingsClient().setSetting(name, xml);
                }

                modified = false;
            }
        }

        protected void saveSettingsAsync(final String xml) {
            BackgroundWorker worker = AppConfig.getBackgroundWorker();
            BackgroundTaskHandler<Object> handle = worker.handle(new BackgroundTask<Object, Object>(10) {
                @Override
                public Object run(TaskLifeCycle<Object> taskLifeCycle) throws Exception {
                    getSettingsClient().setSetting(AsyncSettingsImpl.this.name, xml);

                    return null;
                }

                @Override
                public boolean handleException(Exception ex) {
                    log.warn("Unable to save user settings " + AsyncSettingsImpl.this.name, ex);

                    return true;
                }

                @Override
                public boolean handleTimeoutException() {
                    log.warn("Time out while saving user settings " + AsyncSettingsImpl.this.name);

                    return true;
                }
            });
            handle.execute();
        }
    }

    @Override
    protected void initDebugIds(IFrame frame) {
        if (App.getInstance().isTestMode()) {
            ComponentsHelper.walkComponents(frame, new ComponentVisitor() {
                @Override
                public void visit(com.haulmont.cuba.gui.components.Component component, String name) {
                    if (component.getDebugId() == null) {
                        IFrame componentFrame = null;
                        if (component instanceof com.haulmont.cuba.gui.components.Component.BelongToFrame) {
                            componentFrame = ((com.haulmont.cuba.gui.components.Component.BelongToFrame) component).getFrame();
                        }
                        if (componentFrame == null) {
                            log.warn("Frame for component " + component.getClass() + " is not assigned");
                        } else {
                            if (component instanceof DesktopAbstractComponent) {
                                DesktopAbstractComponent desktopComponent = (DesktopAbstractComponent) component;
                                desktopComponent.assignAutoDebugId();
                            }
                        }
                    }
                }
            });
        }
    }
}