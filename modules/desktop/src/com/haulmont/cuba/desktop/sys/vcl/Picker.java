/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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

    public void addButton(JButton button, int index) {
        if (buttons.contains(button))
            return;

        button.setPreferredSize(new Dimension(22, DesktopComponentsHelper.FIELD_HEIGHT));
        button.setMaximumSize(new Dimension(22, Integer.MAX_VALUE));

        buttons.add(index, button);
        actionsPanel.add(button, index);
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