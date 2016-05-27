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

package com.haulmont.cuba.desktop;

import net.miginfocom.layout.AC;
import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LayoutTest {

    private JFrame frame;
    private JPanel contentPane;

    public static void main(String[] args) {
        LayoutTest layoutTest = new LayoutTest();

        layoutTest.testTableAndButtons();

        layoutTest.start();
    }

    public LayoutTest() {
        frame = new JFrame("Layout Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(300, 300, 800, 600);

        contentPane = new JPanel(new BorderLayout());
        frame.setContentPane(contentPane);

    }

    private void start() {
        frame.setVisible(true);
    }

    private void testTable() {
        MigLayout layout = new MigLayout("debug");
        JPanel panel = new JPanel(layout);
        contentPane.add(panel, BorderLayout.CENTER);

        layout.setLayoutConstraints("fill, flowy, insets 10 0 0 0, debug");

        JTable table = new JTable();
        table.setModel(new DefaultTableModel(new String[] {"col1", "col2"}, 3));
        panel.add(table);

        layout.setComponentConstraints(table, "grow");
    }

    private void testTableAndButtons() {
        MigLayout mainLayout = new MigLayout();
        JPanel mainPanel = new JPanel(mainLayout);
        contentPane.add(mainPanel, BorderLayout.CENTER);

        mainLayout.setLayoutConstraints("flowy, fillx, insets panel, debug");

        MigLayout buttonsLayout = new MigLayout("flowx, filly, insets panel, debug");
        JPanel buttonsPanel = new JPanel(buttonsLayout);
        buttonsPanel.add(new JButton("button1"));
        buttonsPanel.add(new JButton("button2"));

        mainPanel.add(buttonsPanel);

        MigLayout tableLayout = new MigLayout("flowy, fill, debug");
        JPanel tablePanel = new JPanel(tableLayout);
        JTable table = new JTable();
        table.setModel(new DefaultTableModel(new String[] {"col1", "col2"}, 3));
        tablePanel.add(table, "grow");

        mainPanel.add(tablePanel);
        mainLayout.setComponentConstraints(tablePanel, "grow");
        mainLayout.setLayoutConstraints("flowy, fill, insets panel, debug"); // change fillx to fill
//        mainLayout.setRowConstraints("[min!][fill]");
        AC ac = new AC().size("min!", 0).fill(1);
        mainLayout.setRowConstraints(ac);
    }
}