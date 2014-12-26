/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.desktop.sys.DisabledGlassPane;
import com.haulmont.cuba.gui.AppConfig;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper.convertNotificationType;
import static com.haulmont.cuba.gui.ComponentsHelper.preprocessHtmlMessage;
import static com.haulmont.cuba.gui.components.IFrame.NotificationType;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

/**
 * Represents Top level application frame
 *
 * @author devyatkin
 * @version $Id$
 */
public class TopLevelFrame extends JFrame {

    protected DisabledGlassPane glassPane;

    protected DesktopWindowManager windowManager;

    public TopLevelFrame(String applicationTitle) {
        super(applicationTitle);
        initUI();
    }

    protected void initUI() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        glassPane = new DisabledGlassPane();
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        rootPane.setGlassPane(glassPane);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        DesktopConfig config = configuration.getConfig(DesktopConfig.class);

        DesktopResources resources = App.getInstance().getResources();
        if (StringUtils.isNotEmpty(config.getWindowIcon())) {
            setIconImage(resources.getImage(config.getWindowIcon()));
        }
    }

    public void deactivate(@Nullable String message) {
        glassPane.activate(message);
    }

    public void activate() {
        glassPane.deactivate();
    }

    public DesktopWindowManager getWindowManager() {
        if (windowManager == null)
            initWindowManager();

        return windowManager;
    }

    protected void initWindowManager() {
        windowManager = new DesktopWindowManager(this);
    }

    protected void showNotificationPopup(String title, String caption, NotificationType type) {
        JPanel panel = new JPanel(new MigLayout("flowy"));
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));

        switch (type) {
            case WARNING:
            case WARNING_HTML:
                panel.setBackground(Color.yellow);
                break;
            case ERROR:
            case ERROR_HTML:
                panel.setBackground(Color.orange);
                break;
            default:
                panel.setBackground(Color.cyan);
        }

        String popupText = preparePopupText(title, caption);

        JLabel label = new JLabel(popupText);
        panel.add(label);

        Dimension labelSize = DesktopComponentsHelper.measureHtmlText(popupText);

        int x = getX() + getWidth() - (50 + labelSize.getSize().width);
        int y = getY() + getHeight() - (50 + labelSize.getSize().height);

        PopupFactory factory = PopupFactory.getSharedInstance();
        final Popup popup = factory.getPopup(this, panel, x, y);
        popup.show();

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popup.hide();
            }
        });

        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        if (pointerInfo != null) {
            final Point location = pointerInfo.getLocation();
            final Timer timer = new Timer(3000, null);
            timer.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            PointerInfo currentPointer = MouseInfo.getPointerInfo();
                            if (currentPointer == null) {
                                timer.stop();
                            } else if (!currentPointer.getLocation().equals(location)) {
                                popup.hide();
                                timer.stop();
                            }
                        }
                    });
            timer.start();
        }
    }

    protected String preparePopupText(String caption, String description) {
        if (StringUtils.isNotBlank(caption)) {
            description = String.format("<b>%s</b><br>%s", caption, description);
        }
        StringBuilder sb = new StringBuilder("<html>");
        String[] strings = description.split("(<br>)|(<br/>)");
        for (String string : strings) {
            sb.append(string).append("<br/>");
        }
        sb.append("</html>");
        return sb.toString();
    }

    public void showNotification(String caption, String description, NotificationType type) {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        DesktopConfig config = configuration.getConfig(DesktopConfig.class);

        if (!NotificationType.isHTML(type)) {
            caption = preprocessHtmlMessage(escapeHtml(Strings.nullToEmpty(caption)));
            description = preprocessHtmlMessage(escapeHtml(Strings.nullToEmpty(description)));
        }

        if (config.isDialogNotificationsEnabled()
                && type != NotificationType.TRAY
                && type != NotificationType.TRAY_HTML) {
            showNotificationDialog(caption, description, type);
        } else {
            showNotificationPopup(caption, description, type);
        }
    }

    public void showNotification(String caption, NotificationType type) {
        showNotification(null, caption, type);
    }

    protected void showNotificationDialog(String caption, String description, NotificationType type) {
        Messages messages = AppBeans.get(Messages.NAME);
        String title = messages.getMessage(AppConfig.getMessagesPack(), "notification.title." + type);
        String text = preparePopupText(caption, description);

        String closeText = messages.getMainMessage("actions.Close");
        JButton option = new JButton(closeText);
        option.setPreferredSize(new Dimension(80, DesktopComponentsHelper.BUTTON_HEIGHT));

        @SuppressWarnings("MagicConstant")
        JOptionPane pane = new JOptionPane(text, convertNotificationType(type),
                JOptionPane.DEFAULT_OPTION, null,
                new Object[]{option}, option);

        final JDialog dialog = pane.createDialog(this, title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        option.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog.setVisible(true);
            }
        });
    }
}