/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.desktop.sys.DisabledGlassPane;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.IFrame;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents Top - level application frame
 *
 * @author devyatkin
 * @version $Id$
 */
public class TopLevelFrame extends JFrame {

    private DisabledGlassPane glassPane;

    private DesktopWindowManager windowManager;

    public TopLevelFrame(String applicationTitle) {
        super(applicationTitle);
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        glassPane = new DisabledGlassPane();
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        rootPane.setGlassPane(glassPane);
    }

    public void deactivate(@Nullable String message) {
        glassPane.activate(message);
    }

    public void activate() {
        glassPane.deactivate();
    }

    public DesktopWindowManager getWindowManager() {
        if (windowManager == null)
            windowManager = new DesktopWindowManager(this);

        return windowManager;
    }

    protected void showNotificationPopup(String title, String caption, IFrame.NotificationType type) {
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

        FontMetrics fontMetrics = getGraphics().getFontMetrics();

        if (StringUtils.isNotBlank(title)) {
            caption = String.format("<b>%s</b><br>%s", title, caption);
        }
        int height = (int) fontMetrics.getStringBounds(caption, getGraphics()).getHeight();
        int width = 0;
        StringBuilder sb = new StringBuilder("<html>");
        String[] strings = caption.split("(<br>)|(<br/>)");
        for (String string : strings) {
            int w = (int) fontMetrics.getStringBounds(string, getGraphics()).getWidth();
            width = Math.max(width, w);
            sb.append(string).append("<br/>");
        }
        sb.append("</html>");

        JLabel label = new JLabel(sb.toString());
        panel.add(label);

        int x = getX() + getWidth() - (50 + width);
        int y = getY() + getHeight() - (50 + ((height + 5) * strings.length));

        PopupFactory factory = PopupFactory.getSharedInstance();
        final Popup popup = factory.getPopup(this, panel, x, y);
        popup.show();
        final Point location = MouseInfo.getPointerInfo().getLocation();
        final Timer timer = new Timer(3000, null);
        timer.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (!MouseInfo.getPointerInfo().getLocation().equals(location)) {
                            popup.hide();
                            timer.stop();
                        }
                    }
                }
        );
        timer.start();
    }

    public void showNotification(String caption, String description, IFrame.NotificationType type) {
        DesktopConfig config = AppBeans.get(Configuration.class).getConfig(DesktopConfig.class);

        caption = Strings.nullToEmpty(ComponentsHelper.preprocessHtmlMessage(
                IFrame.NotificationType.isHTML(type) ? caption : StringEscapeUtils.escapeHtml(caption)));
        description = Strings.nullToEmpty(ComponentsHelper.preprocessHtmlMessage(
                IFrame.NotificationType.isHTML(type) ? description : StringEscapeUtils.escapeHtml(description)));

        if (config.isDialogNotificationsEnabled()
                && type != IFrame.NotificationType.TRAY && type != IFrame.NotificationType.TRAY_HTML) {
            showNotificationDialog(caption, description, type);
        } else {
            showNotificationPopup(caption, description, type);
        }
    }

    public void showNotification(String caption, IFrame.NotificationType type) {
        showNotification(null, caption, type);
    }

    protected void showNotificationDialog(String caption, String description, IFrame.NotificationType type) {
        String title = AppBeans.get(Messages.class).getMessage(AppConfig.getMessagesPack(), "notification.title." + type);
        String text;
        if (StringUtils.isNotBlank(caption)) {
            text = String.format("<html><b>%s</b><br>%s", caption, description);
        } else {
            text = "<html>" + description;
        }

        int messageType = DesktopComponentsHelper.convertNotificationType(type);

        String closeText = AppBeans.get(Messages.class).getMainMessage("actions.Close");
        JButton option = new JButton(closeText);
        option.setPreferredSize(new Dimension(80, DesktopComponentsHelper.BUTTON_HEIGHT));

        JOptionPane pane = new JOptionPane(text, messageType,
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