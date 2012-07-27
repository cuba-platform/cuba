/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
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
        for (Iterator i = defaults.iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
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
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void showUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.add("Align", createAlignTab());
        tabbedPane.add("Table", createTableTab());
        tabbedPane.add("TextArea", createTextAreaTab());
        tabbedPane.add("Popup", createPopupTab());
        tabbedPane.add("Picker", createPickersTab());
//        tabbedPane.add("Autocomplete", createAutocompleteTab());
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

    private Component createTableTab() {
        JPanel box = new JPanel();
        box.setFocusable(false);
        MigLayout boxLayout = new MigLayout("debug 1000");
        box.setLayout(boxLayout);

        JPanel panel = new JPanel();
        MigLayout layout = new MigLayout("debug 1000, flowy, fill, insets 0", "", "[min!][fill]");
        panel.setLayout(layout);

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setVisible(true);
        panel.add(topPanel, "growx");

        topPanel.add(new JButton("Button1"));
        topPanel.add(new JButton("Button2"));
        topPanel.add(new JButton("Button3"));
        topPanel.add(new JButton("Button4"));
        topPanel.add(new JButton("Button5"));

        JXTableExt impl = new JXTableExt();

//        JPanel tablePanel = new JPanel(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(impl);
        impl.setFillsViewportHeight(true);
//        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(scrollPane, "grow");

        impl.setShowGrid(true);
        impl.setGridColor(Color.lightGray);

        impl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        impl.setColumnControlVisible(true);

        impl.setModel(new AbstractTableModel() {
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
        cc.growX(0);
        cc.width("300!");
//        cc.width("100%");
        cc.growY(0.0f);
        cc.height("200!");
//        cc.height("100%");

        box.add(panel);

        boxLayout.setComponentConstraints(panel, cc);

        cc = new CC();
        cc.growX(0);
        cc.width("300!");
//        cc.width("100%");
        layout.setComponentConstraints(scrollPane, cc);

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
        picker1.addButton(new JButton("..."));
        panel.add(picker1, "wrap");

        panel.add(new JLabel("LookupPicker"));

        Picker picker2 = new LookupPicker();
        picker2.addButton(new JButton("..."));
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

        FontMetrics fontMetrics = getGraphics().getFontMetrics();
        double width = fontMetrics.getStringBounds(text, getGraphics()).getWidth();

        int x = getX() + getWidth() - (50 + (int)width);
        int y = getY() + getHeight() - 100;

        PopupFactory factory = PopupFactory.getSharedInstance();
        final Popup popup = factory.getPopup(this, panel, x, y);
        System.out.println("Show popup " + popup);
        popup.show();
        final Point location = MouseInfo.getPointerInfo().getLocation();
        final Timer timer = new Timer(3000, null);
        timer.addActionListener(
                new ActionListener() {
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
