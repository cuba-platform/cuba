/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
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
        Set defaults = UIManager.getLookAndFeelDefaults().entrySet();
        for (Iterator i = defaults.iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            System.out.print(entry.getKey() + " = ");
            System.out.println(entry.getValue());
        }

        Font font = UIManager.getLookAndFeelDefaults().getFont("Panel.font");
        System.out.println(font);

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

    public VclTestApp() throws HeadlessException {
        setLayout(new BorderLayout());
        setTitle("VCL Test");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(300, 200, 800, 300);
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

        tabbedPane.add("Popup", createPopupTab());
        tabbedPane.add("Picker", createPickersTab());
//        tabbedPane.add("Autocomplete", createAutocompleteTab());
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
