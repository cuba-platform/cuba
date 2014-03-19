/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class SessionMessageWindow extends JDialog {

    private JTextArea messageArea;

    public SessionMessageWindow(Frame owner) {
        super(owner);

        Messages messages = AppBeans.get(Messages.NAME);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(messages.getMainMessage("sessionMessageDialog.caption", Locale.getDefault()));
        setContentPane(createContentPane());
        setPreferredSize(new Dimension(520, 140));
        pack();

        setWindowLocation(owner);
    }

    protected void setWindowLocation(Frame owner) {
        Point ownerLocation = owner.getLocationOnScreen();
        int mainX = ownerLocation.x;
        int mainY = ownerLocation.y;

        Dimension onwerSize = owner.getSize();
        int mainWidth = onwerSize.width;
        int mainHeight = onwerSize.height;

        Dimension size = getSize();
        int width = size.width;
        int height = size.height;

        int x = mainX + mainWidth - 5 - width;
        int y = mainY + mainHeight - 10 - height;

        setLocation(x, y);
    }

    protected Container createContentPane() {
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setMargin(new Insets(3, 3, 3, 3));
        messageArea.setBorder(new LineBorder((Color) UIManager.get("cubaSessionMessageDialogBackground"), 4));
        messageArea.setBackground((Color) UIManager.get("cubaSessionMessageDialogBackground"));
        messageArea.setForeground((Color) UIManager.get("cubaSessionMessageDialogFontColor"));
        messageArea.setFont((Font) UIManager.get("cubaSessionMessageDialogFont"));

        panel.setBorder(new LineBorder((Color) UIManager.get("cubaSessionMessageDialogBorderColor"), 5));

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.add(scrollPane);

        return panel;
    }

    public void setMessage(String message) {
        messageArea.setText(message);
    }
}