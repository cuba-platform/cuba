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

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.desktop.DesktopResources;
import com.haulmont.cuba.desktop.TopLevelFrame;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.vcl.CollapsiblePanel;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class FontDialog extends JDialog {

    private Font result;
    private Font editFont;

    private JLabel previewLabel;
    private JToggleButton boldToggle;
    private JToggleButton italicToggle;
    private JToggleButton underlineToggle;
    private JComboBox fontSizeBox;
    private JComboBox fontFamilyBox;

    private Messages messages;

    public FontDialog(Frame parent, Font editFont) {
        super(parent);
        initDialog(parent, editFont);
    }

    public FontDialog(Dialog parent, Font editFont) {
        super(parent);
        initDialog(parent, editFont);
    }

    public FontDialog(Window parent, Font editFont) {
        super(parent);
        initDialog(parent, editFont);
    }

    public FontDialog(Component parent, Font editFont) {
        initDialog(parent, editFont);
    }

    private void initDialog(Component parent, Font editFont) {
        this.editFont = editFont;
        this.messages = AppBeans.get(Messages.NAME);

        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(messages.getMessage(getClass(), "FontDialog.title"));

        addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        TopLevelFrame topLevelFrame = DesktopComponentsHelper.getTopLevelFrame(FontDialog.this);
                        DesktopWindowManager wm = topLevelFrame.getWindowManager();

                        DialogWindow lastDialogWindow = wm.getLastDialogWindow();
                        if (lastDialogWindow == null) {
                            topLevelFrame.activate();
                        } else {
                            lastDialogWindow.enableWindow();
                        }
                    }
                }
        );

        initUI();
    }

    private void initUI() {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        DesktopConfig desktopConfig = configuration.getConfig(DesktopConfig.class);

        setIconImage(null);
        setIconImages(null);
        setPreferredSize(new Dimension(400, 220));
        setSize(new Dimension(400, 220));
        setMinimumSize(new Dimension(380, 200));

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 5));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        // font properties panel

        JPanel fontPrefsPanel = new JPanel();
        fontPrefsPanel.setLayout(new BoxLayout(fontPrefsPanel, BoxLayout.X_AXIS));

        fontFamilyBox = new JComboBox();
        fontFamilyBox.setPreferredSize(new Dimension(160, -1));
        String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontFamilyBox.setModel(new DefaultComboBoxModel<>(availableFonts));

        fontSizeBox = new JComboBox();
        fontSizeBox.setPreferredSize(new Dimension(60, -1));
        fontSizeBox.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
        fontSizeBox.setMinimumSize(new Dimension(60, 0));
        fontSizeBox.setModel(new ListComboBoxModel<>(desktopConfig.getAvailableFontSizes()));

        DesktopResources resources = App.getInstance().getResources();
        boldToggle = new JToggleButton(resources.getIcon("font/bold.png"));
        italicToggle = new JToggleButton(resources.getIcon("font/italic.png"));
        underlineToggle = new JToggleButton(resources.getIcon("font/underline.png"));

        fontPrefsPanel.add(fontFamilyBox);
        fontPrefsPanel.add(fontSizeBox);
        fontPrefsPanel.add(boldToggle);
        fontPrefsPanel.add(italicToggle);
        fontPrefsPanel.add(underlineToggle);

        if (editFont != null) {
            fontFamilyBox.setSelectedItem(editFont.getFamily());
            fontSizeBox.setSelectedItem(editFont.getSize());
            // toggle buttons
            Map<TextAttribute, ?> attributes = editFont.getAttributes();

            boldToggle.setSelected((editFont.getStyle() & Font.BOLD) == Font.BOLD);
            italicToggle.setSelected((editFont.getStyle() & Font.ITALIC) == Font.ITALIC);
            underlineToggle.setSelected(attributes.get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON);
        } else {
            fontFamilyBox.setSelectedIndex(0);
            fontSizeBox.setSelectedIndex(0);
        }

        initListeners();

        contentPane.add(fontPrefsPanel, BorderLayout.NORTH);

        // preview panel

        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new GridBagLayout());
        previewPanel.setPreferredSize(new Dimension(-1, 120));
        previewPanel.setMinimumSize(new Dimension(0, 120));
        previewPanel.setSize(-1, 120);

        previewLabel = new JLabel("ABCDEFG abcdefg");
        previewPanel.add(previewLabel);
        previewLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        if (editFont != null)
            previewLabel.setFont(editFont);

        CollapsiblePanel groupBox = new CollapsiblePanel(previewPanel);
        groupBox.setCollapsible(false);
        groupBox.setCaption(messages.getMessage(getClass(), "FontDialog.preview"));

        contentPane.add(groupBox, BorderLayout.CENTER);

        // buttons panel

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        JButton okBtn = new JButton(new AbstractAction(
                messages.getMessage(getClass(), "actions.Ok"),
                resources.getIcon("icons/ok.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = compileFont();
                closeDialog();
            }
        });
        okBtn.setPreferredSize(new Dimension(0, DesktopComponentsHelper.BUTTON_HEIGHT));

        JButton cancelBtn = new JButton(new AbstractAction(
                messages.getMessage(getClass(), "actions.Cancel"),
                resources.getIcon("icons/cancel.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });
        cancelBtn.setPreferredSize(new Dimension(0, DesktopComponentsHelper.BUTTON_HEIGHT));

        buttonsPanel.add(okBtn);
        buttonsPanel.add(cancelBtn);

        contentPane.add(buttonsPanel, BorderLayout.SOUTH);

        initToolTips();

        setContentPane(contentPane);
        pack();

        applyLocation();
    }

    private void applyLocation() {
        Point ownerLocation = getParent().getLocationOnScreen();
        int mainX = ownerLocation.x;
        int mainY = ownerLocation.y;

        Dimension ownerSize = getParent().getSize();
        int mainWidth = ownerSize.width;
        int mainHeight = ownerSize.height;

        Dimension size = getSize();
        int width = size.width;
        int height = size.height;

        setLocation(mainX + mainWidth / 2 - width / 2, mainY + mainHeight / 2 - height / 2);
    }

    private void initToolTips() {
        fontFamilyBox.setToolTipText(messages.getMessage(getClass(), "FontDialog.font"));
        fontSizeBox.setToolTipText(messages.getMessage(getClass(), "FontDialog.size"));

        boldToggle.setToolTipText(messages.getMessage(getClass(), "FontDialog.bold"));
        italicToggle.setToolTipText(messages.getMessage(getClass(), "FontDialog.italic"));
        underlineToggle.setToolTipText(messages.getMessage(getClass(), "FontDialog.underline"));

        DesktopToolTipManager.getInstance().registerTooltip(fontFamilyBox);
        DesktopToolTipManager.getInstance().registerTooltip(fontSizeBox);

        DesktopToolTipManager.getInstance().registerTooltip(boldToggle);
        DesktopToolTipManager.getInstance().registerTooltip(italicToggle);
        DesktopToolTipManager.getInstance().registerTooltip(underlineToggle);
    }

    private void closeDialog() {
        WindowEvent windowClosing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        this.dispatchEvent(windowClosing);
    }

    private void initListeners() {
        ItemListener propertyBoxChangeListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                previewLabel.setFont(compileFont());
            }
        };

        fontFamilyBox.addItemListener(propertyBoxChangeListener);
        fontSizeBox.addItemListener(propertyBoxChangeListener);

        ChangeListener toggleListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                previewLabel.setFont(compileFont());
            }
        };

        boldToggle.addChangeListener(toggleListener);
        italicToggle.addChangeListener(toggleListener);
        underlineToggle.addChangeListener(toggleListener);
    }

    public Font compileFont() {
        int style = 0;
        style |= boldToggle.isSelected() ? Font.BOLD : 0;
        style |= italicToggle.isSelected() ? Font.ITALIC : 0;
        Font font = new Font((String) fontFamilyBox.getSelectedItem(), style, (Integer) fontSizeBox.getSelectedItem());
        if (underlineToggle.isSelected()) {
            Map<TextAttribute, Integer> attributes = new HashMap<>();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            font = font.deriveFont(attributes);
        }
        return font;
    }

    public void open() {
        TopLevelFrame topLevelFrame = DesktopComponentsHelper.getTopLevelFrame(this);
        DialogWindow lastDialogWindow = topLevelFrame.getWindowManager().getLastDialogWindow();
        if (lastDialogWindow == null) {
            topLevelFrame.deactivate(null);
        } else {
            lastDialogWindow.disableWindow(null);
        }
        setVisible(true);
    }

    public Font getResult() {
        return result;
    }

    public static FontDialog show(Component parent, Font editFont) {
        FontDialog dialog;
        if (parent instanceof JFrame)
            dialog = new FontDialog((JFrame) parent, editFont);
        else if (parent instanceof JDialog)
            dialog = new FontDialog((JDialog) parent, editFont);
        else if (parent instanceof JWindow)
            dialog = new FontDialog((JWindow) parent, editFont);
        else
            dialog = new FontDialog(parent, editFont);

        return dialog;
    }
}