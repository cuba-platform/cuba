/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.desktop.DesktopResources;
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
 * @author artamonov
 * @version $Id$
 */
public class FontDialog extends JDialog {

    private Font result;
    private Font editFont;

    private JLabel previewLabel;
    private JToggleButton boldToogle;
    private JToggleButton italicToogle;
    private JToggleButton underlineToogle;
    private JComboBox fontSizeBox;
    private JComboBox fontFamilyBox;

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

        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(MessageProvider.getMessage(getClass(), "FontDialog.title"));

        addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        DesktopComponentsHelper.getTopLevelFrame(FontDialog.this).activate();
                    }
                }
        );

        initUI();
    }

    private void initUI() {
        DesktopConfig desktopConfig = ConfigProvider.getConfig(DesktopConfig.class);

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
        fontFamilyBox.setModel(new DefaultComboBoxModel(availableFonts));

        fontSizeBox = new JComboBox();
        fontSizeBox.setPreferredSize(new Dimension(60, -1));
        fontSizeBox.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
        fontSizeBox.setMinimumSize(new Dimension(60, 0));
        fontSizeBox.setModel(new ListComboBoxModel<Integer>(desktopConfig.getAvailableFontSizes()));

        DesktopResources resources = App.getInstance().getResources();
        boldToogle = new JToggleButton(resources.getIcon("font/bold.png"));
        italicToogle = new JToggleButton(resources.getIcon("font/italic.png"));
        underlineToogle = new JToggleButton(resources.getIcon("font/underline.png"));

        fontPrefsPanel.add(fontFamilyBox);
        fontPrefsPanel.add(fontSizeBox);
        fontPrefsPanel.add(boldToogle);
        fontPrefsPanel.add(italicToogle);
        fontPrefsPanel.add(underlineToogle);

        if (editFont != null) {
            fontFamilyBox.setSelectedItem(editFont.getFamily());
            fontSizeBox.setSelectedItem(editFont.getSize());
            // toogle buttons
            Map<TextAttribute, ?> attributes = editFont.getAttributes();

            boldToogle.setSelected((editFont.getStyle() & Font.BOLD) == Font.BOLD);
            italicToogle.setSelected((editFont.getStyle() & Font.ITALIC) == Font.ITALIC);
            underlineToogle.setSelected(attributes.get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON);
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
        groupBox.setCaption(MessageProvider.getMessage(getClass(), "FontDialog.preview"));

        contentPane.add(groupBox, BorderLayout.CENTER);

        // buttons panel

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        JButton okBtn = new JButton(new AbstractAction(
                MessageProvider.getMessage(getClass(), "actions.Ok"),
                resources.getIcon("icons/ok.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = compileFont();
                closeDialog();
            }
        });
        okBtn.setPreferredSize(new Dimension(0, DesktopComponentsHelper.BUTTON_HEIGHT));

        JButton cancelBtn = new JButton(new AbstractAction(
                MessageProvider.getMessage(getClass(), "actions.Cancel"),
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
    }

    private void initToolTips() {
        fontFamilyBox.setToolTipText(MessageProvider.getMessage(getClass(), "FontDialog.font"));
        fontSizeBox.setToolTipText(MessageProvider.getMessage(getClass(), "FontDialog.size"));

        boldToogle.setToolTipText(MessageProvider.getMessage(getClass(), "FontDialog.bold"));
        italicToogle.setToolTipText(MessageProvider.getMessage(getClass(), "FontDialog.italic"));
        underlineToogle.setToolTipText(MessageProvider.getMessage(getClass(), "FontDialog.underline"));

        DesktopToolTipManager.getInstance().registerTooltip(fontFamilyBox);
        DesktopToolTipManager.getInstance().registerTooltip(fontSizeBox);

        DesktopToolTipManager.getInstance().registerTooltip(boldToogle);
        DesktopToolTipManager.getInstance().registerTooltip(italicToogle);
        DesktopToolTipManager.getInstance().registerTooltip(underlineToogle);
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

        ChangeListener toogleListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                previewLabel.setFont(compileFont());
            }
        };

        boldToogle.addChangeListener(toogleListener);
        italicToogle.addChangeListener(toogleListener);
        underlineToogle.addChangeListener(toogleListener);
    }

    public Font compileFont() {
        int style = 0;
        style |= boldToogle.isSelected() ? Font.BOLD : 0;
        style |= italicToogle.isSelected() ? Font.ITALIC : 0;
        Font font = new Font((String) fontFamilyBox.getSelectedItem(), style, (Integer) fontSizeBox.getSelectedItem());
        if (underlineToogle.isSelected()) {
            Map<TextAttribute, Integer> attributes = new HashMap<TextAttribute, Integer>();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            font = font.deriveFont(attributes);
        }
        return font;
    }

    public void open() {
        DesktopComponentsHelper.getTopLevelFrame(this).deactivate(null);
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
