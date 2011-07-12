/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopRowsCount extends DesktopAbstractComponent<DesktopRowsCount.RowsCountComponent> implements RowsCount {

    private CollectionDatasource datasource;
    private boolean refreshing;
    private State state;
    private int start;
    private int size;

    public DesktopRowsCount() {
        impl = new RowsCountComponent();
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        if (datasource != null) {
            this.datasource.addListener(
                    new CollectionDsListenerAdapter() {
                        @Override
                        public void collectionChanged(CollectionDatasource ds, Operation operation) {
                            onCollectionChanged();
                        }
                    }
            );
            impl.getCountLabel().addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onLinkClick();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            impl.getPrevButton().addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            onPrevClick();
                        }
                    }
            );
            impl.getNextButton().addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            onNextClick();
                        }
                    }
            );
        }
    }

    private void onCollectionChanged() {
        if (datasource == null)
            return;

        String msgKey;
        size = datasource.size();
        start = 0;

        if (datasource instanceof CollectionDatasource.SupportsPaging) {
            CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
            if ((size == 0 || size < ds.getMaxResults()) && ds.getFirstResult() == 0) {
                state = State.FIRST_COMPLETE;
            } else if (size == ds.getMaxResults() && ds.getFirstResult() == 0) {
                state = State.FIRST_INCOMPLETE;
            } else if (size == ds.getMaxResults() && ds.getFirstResult() > 0) {
                state = State.MIDDLE;
                start = ds.getFirstResult();
            } else if (size < ds.getMaxResults() && ds.getFirstResult() > 0) {
                state = State.LAST;
                start = ds.getFirstResult();
            } else
                state = State.FIRST_COMPLETE;
        } else {
            state = State.FIRST_COMPLETE;
        }

        String countValue;
        switch (state) {
            case FIRST_COMPLETE:
                impl.getCountLabel().setVisible(false);
                impl.getPrevButton().setVisible(false);
                impl.getNextButton().setVisible(false);
                msgKey = "table.rowsCount.msg2";
                countValue = String.valueOf(size);
                break;
            case FIRST_INCOMPLETE:
                impl.getCountLabel().setVisible(true);
                impl.getPrevButton().setVisible(false);
                impl.getNextButton().setVisible(true);
                msgKey = "table.rowsCount.msg1";
                countValue = "1-" + size;
                break;
            case MIDDLE:
                impl.getCountLabel().setVisible(true);
                impl.getPrevButton().setVisible(true);
                impl.getNextButton().setVisible(true);
                msgKey = "table.rowsCount.msg1";
                countValue = (start + 1) + "-" + (start + size);
                break;
            case LAST:
                impl.getCountLabel().setVisible(false);
                impl.getPrevButton().setVisible(true);
                impl.getNextButton().setVisible(false);
                msgKey = "table.rowsCount.msg2";
                countValue = (start + 1) + "-" + (start + size);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        String messagesPack = AppConfig.getInstance().getMessagesPack();
        impl.getLabel().setText(MessageProvider.formatMessage(messagesPack, msgKey, countValue));

        if (impl.getCountLabel().isVisible() && !refreshing) {
            impl.getCountLabel().setText(MessageProvider.getMessage(messagesPack, "table.rowsCount.msg3"));
        }
        impl.repaint();
        impl.revalidate();
    }

    private void onLinkClick() {
        if (datasource == null || !(datasource instanceof CollectionDatasource.SupportsPaging))
            return;

        int count = ((CollectionDatasource.SupportsPaging) datasource).getCount();
        impl.getCountLabel().setText(String.valueOf(count));
    }

    private void onNextClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging))
            return;

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int firstResult = ds.getFirstResult();
        ds.setFirstResult(ds.getFirstResult() + ds.getMaxResults());
        refreshDatasource(ds);

        if (state.equals(State.LAST) && size == 0) {
            ds.setFirstResult(firstResult);
            int maxResults = ds.getMaxResults();
            ds.setMaxResults(maxResults + 1);
            refreshDatasource(ds);
            ds.setMaxResults(maxResults);
        }
    }

    private void onPrevClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging))
            return;

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int newStart = ds.getFirstResult() - ds.getMaxResults();
        ds.setFirstResult(newStart < 0 ? 0 : newStart);
        refreshDatasource(ds);
    }

    private void refreshDatasource(CollectionDatasource.SupportsPaging ds) {
        refreshing = true;
        try {
            ds.refresh();
        } finally {
            refreshing = false;
        }
    }

    public class RowsCountComponent extends JPanel {

        private JButton prevButton;
        private JButton nextButton;
        private JLabel label;
        private JLabel countLabel;
        private MigLayout layout;

        private final Dimension size = new Dimension(35, 25);

        public RowsCountComponent() {
            LC lc = new LC();
            lc.insetsAll("2");

            layout = new MigLayout(lc);
            if (LayoutAdapter.isDebug()) {
                lc.debug(1000);
            }
            setLayout(layout);

            prevButton = new JButton("<");
            add(prevButton);
            prevButton.setPreferredSize(size);
            prevButton.setMinimumSize(size);

            label = new JLabel();
            add(label);

            countLabel = new JLabel("[?]");
            countLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            countLabel.setForeground(Color.blue);
            add(countLabel);

            nextButton = new JButton(">");
            add(nextButton);
            nextButton.setPreferredSize(size);
            nextButton.setMinimumSize(size);
        }

        public JLabel getLabel() {
            return label;
        }

        public JLabel getCountLabel() {
            return countLabel;
        }

        public JButton getPrevButton() {
            return prevButton;
        }

        public JButton getNextButton() {
            return nextButton;
        }
    }
}
