/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class Picker extends JComponent implements FocusableComponent {

    protected JPanel contentPanel;
    protected JComponent editor;
    protected JPanel actionsPanel;
    protected boolean enabled = true;

    protected List<JButton> buttons = new ArrayList<>();

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

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);

        contentPanel.setBackground(bg);
        actionsPanel.setBackground(bg);
    }

    protected void initContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
    }

    protected void initEditor() {
        JTextField textField = new JTextField();
        textField.setEditable(false);
        editor = textField;
    }

    public JComponent getEditor() {
        return editor;
    }

    public JComponent getInputField(){
        return editor;
    }

    protected void initActionsPanel() {
        actionsPanel = new JPanel(new MigLayout("hidemode 2, ins 0 0 0 0, gap 0"));
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

        button.setPreferredSize(new Dimension(22, DesktopComponentsHelper.FIELD_HEIGHT));
        button.setMaximumSize(new Dimension(22, Integer.MAX_VALUE));
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

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        editor.setEnabled(enabled);
        for (JButton btn : getButtons()) {
            btn.setEnabled(enabled);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void requestFocus(){
        editor.requestFocus();
    }

    @Override
    public void focus() {
        requestFocus();
    }
}