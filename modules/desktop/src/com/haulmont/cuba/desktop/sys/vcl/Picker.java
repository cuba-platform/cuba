/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class Picker extends JComponent {

    protected JPanel contentPanel;
    protected JComponent editor;
    protected JPanel actionsPanel;

    protected List<JButton> buttons = new ArrayList<JButton>();

    public Picker() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(150, DesktopComponentsHelper.FIELD_HEIGHT));

        initContentPanel();
        add(contentPanel, BorderLayout.CENTER);

        initEditor();
        contentPanel.add(editor, BorderLayout.CENTER);

        initActionsPanel();
        contentPanel.add(actionsPanel, BorderLayout.EAST);
    }

    protected void initContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
    }

    protected void initEditor() {
        JTextField textField = new JTextField();
        textField.setEditable(false);
        editor = textField;
    }

    protected void initActionsPanel() {
        actionsPanel = new JPanel(new GridLayout(1, 0, 0, 0));
        for (JButton button : buttons) {
            actionsPanel.add(button);
        }
    }

    public List<JButton> getButtons() {
        return Collections.unmodifiableList(buttons);
    }

    public void addButton(JButton button) {
        if (buttons.contains(button))
            return;

        button.setPreferredSize(new Dimension(20, DesktopComponentsHelper.FIELD_HEIGHT));
        buttons.add(button);
        actionsPanel.add(button);
    }

    public void removeButton(JButton button) {
        buttons.remove(button);
        actionsPanel.remove(button);
    }

    public Object getValue() {
        return ((JTextField) editor).getText();
    }

    public void setValue(Object value) {
        ((JTextField) editor).setText((String) value);
    }
}
