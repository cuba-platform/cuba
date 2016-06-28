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
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class VclTestApp extends JFrame {

    public static void main(String[] args) {
        //printUIDefaults();

        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        VclTestApp app = new VclTestApp();
                        app.initLookAndFeel();
                        app.showUI();
                        app.setVisible(true);
                    }
                }
        );
    }

    private static void printUIDefaults() {
        Set defaults = UIManager.getLookAndFeelDefaults().entrySet();
        for (Object aDefault : defaults) {
            Map.Entry entry = (Map.Entry) aDefault;
            System.out.print(entry.getKey() + " = ");
            System.out.println(entry.getValue());
        }

        Font font = UIManager.getLookAndFeelDefaults().getFont("Panel.font");
        System.out.println(font);
    }

    public VclTestApp() throws HeadlessException {
        setLayout(new BorderLayout());
        setTitle("VCL Test");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(500, 300, 600, 300);
    }

    private void initLookAndFeel() {
        try {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JFrame.setDefaultLookAndFeelDecorated(true);

            boolean found = false;
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    found = true;
                    break;
                }
            }
            if (!found)
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.exit(-1);
        }
    }

    private void showUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.add("Table", createTableTab());
        tabbedPane.add("Align", createAlignTab());
        tabbedPane.add("TextArea", createTextAreaTab());
        tabbedPane.add("Popup", createPopupTab());
        tabbedPane.add("Picker", createPickersTab());
//        tabbedPane.add("Autocomplete", createAutocompleteTab());
    }

    public static class MyPanel extends JPanel implements Scrollable {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return visibleRect.width;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
//            final Container viewport = getParent();
//            System.out.println("viewport.width=" + viewport.getWidth() + ", minimumSize.width=" + getMinimumSize().width);
//            return viewport.getWidth() > getMinimumSize().width;
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private Component createTableTab() {
        final JPanel box = new JPanel();
        box.setFocusable(false);
        MigLayout boxLayout = new MigLayout("fill");
        box.setLayout(boxLayout);

        final JScrollPane outerScrollPane = new JScrollPane();
        outerScrollPane.setViewportView(box);

        outerScrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Dimension minimumSize = box.getMinimumSize();
                final int width = Math.max(minimumSize.width, outerScrollPane.getViewport().getWidth());
                box.setPreferredSize(new Dimension(width, minimumSize.height));
            }
        });

        JPanel tablePanel = new JPanel();
        MigLayout tablePanellayout = new MigLayout("flowy, fill, insets 0", "", "[min!][fill]");
        tablePanel.setLayout(tablePanellayout);

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setVisible(true);
        tablePanel.add(topPanel/*, "growx"*/);

        topPanel.add(new JButton("Button1"));
        topPanel.add(new JButton("Button2"));
        topPanel.add(new JButton("Button3"));
        topPanel.add(new JButton("Button4"));
        topPanel.add(new JButton("Button5"));

        JXTableExt table = new JXTableExt();

        JScrollPane tableScrollPane = new JScrollPane(table);
//        table.setFillsViewportHeight(true);

        tablePanel.add(tableScrollPane, "grow");

        table.setShowGrid(true);
        table.setGridColor(Color.lightGray);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setColumnControlVisible(true);

        table.setModel(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return 20;
            }

            @Override
            public int getColumnCount() {
                return 20;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return rowIndex + "-" + columnIndex;
            }
        });

        CC cc = new CC();
//        cc.growX(0);
//        cc.width("300!");
        cc.width("100%");
//        cc.growY(0.0f);
        cc.height("200!");
//        cc.height("100%");

        box.add(tablePanel);

        boxLayout.setComponentConstraints(tablePanel, cc);

//        cc = new CC();
//        cc.growX(0);
//        cc.width("300!");
//        cc.width("100%");
//        cc.height("200!");
//        tablePanellayout.setComponentConstraints(tableScrollPane, cc);

        return outerScrollPane;
    }

    private Component createAlignTab() {
        JPanel box = new JPanel();
        box.setFocusable(false);
        MigLayout boxLayout = new MigLayout("debug 1000, fill");
        box.setLayout(boxLayout);

        JPanel panel = new JPanel();
        MigLayout layout = new MigLayout("debug 1000, fill");
        panel.setLayout(layout);

        JLabel label = new JLabel("Label");
        CC cc = new CC();
        cc.alignX("right").alignY("50%");

        panel.add(label);

        layout.setComponentConstraints(label, cc);

        LC lc = new LC();
        lc.hideMode(2); // The size of an invisible component will be set to 0, 0 and the gaps will also be set to 0 around it.
        lc.debug(1000);

        AC rowConstr = new AC();
        AC colConstr = new AC();

        lc.fillX();
        lc.flowY();
        lc.gridGapY("0");

        layout.setLayoutConstraints(lc);
        layout.setRowConstraints(rowConstr);
        layout.setColumnConstraints(colConstr);

        box.add(panel, "height 100px, width 100px");
        layout.setComponentConstraints(label, cc);
        return box;
    }

    private Component createTextAreaTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("flowy, fill", "[]", "[min!][fill]"));
//        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setRows(3);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        int height = (int) textArea.getPreferredSize().getHeight();
        textArea.setMinimumSize(new Dimension(150, height));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setMinimumSize(new Dimension(0, height));
        scrollPane.setPreferredSize(new Dimension(150, height));

        panel.add(scrollPane);
        panel.add(new JButton("button"), "grow");

        return panel;
    }

    private JPanel createPickersTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());

        panel.add(new JLabel("Picker"));

        Picker picker = new Picker();
        panel.add(picker, "wrap");

        panel.add(new JLabel("Picker"));

        Picker picker1 = new Picker();
        picker1.addButton(new JButton("..."), 0);
        panel.add(picker1, "wrap");

        panel.add(new JLabel("LookupPicker"));

        Picker picker2 = new LookupPicker();
        picker2.addButton(new JButton("..."), 0);
        panel.add(picker2, "wrap");

        return panel;
    }

    private JPanel createPopupTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());

        JButton start = new JButton("Pick Me for Popup");
        start.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showPopup();
                    }
                }
        );
        panel.add(start);

        return panel;
    }

    private void showPopup() {
        JPanel panel = new JPanel(new MigLayout("flowy"));
        panel.setBorder(BorderFactory.createLineBorder(Color.yellow));
        panel.setBackground(Color.yellow);
        String text = "A Message " + new Date();

        JLabel label = new JLabel("<html>" + text + "<br/>" + text + "</html>");
        panel.add(label);

        Dimension labelSize = DesktopComponentsHelper.measureHtmlText(text);

        int x = getX() + getWidth() - (50 + labelSize.width);
        int y = getY() + getHeight() - (50 + labelSize.height);

        PopupFactory factory = PopupFactory.getSharedInstance();
        final Popup popup = factory.getPopup(this, panel, x, y);
        System.out.println("Show popup " + popup);
        popup.show();
        final Point location = MouseInfo.getPointerInfo().getLocation();
        final Timer timer = new Timer(3000, null);
        timer.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!MouseInfo.getPointerInfo().getLocation().equals(location)) {
                            System.out.println("Hide popup " + popup);
                            popup.hide();
                            timer.stop();
                        }
                    }
                }
        );
        timer.start();
    }
}