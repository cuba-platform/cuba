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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.RowsCount;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;

/**
 */
public class DesktopRowsCount extends DesktopAbstractComponent<DesktopRowsCount.RowsCountComponent>
        implements RowsCount {

    protected CollectionDatasource datasource;
    protected boolean refreshing;
    protected State state;
    protected State lastState;
    protected int start;
    protected int size;
    protected ListComponent owner;
    protected boolean samePage;

    public DesktopRowsCount() {
        impl = new RowsCountComponent();
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        if (datasource != null) {
            //noinspection unchecked
            this.datasource.addCollectionChangeListener(e -> {
                samePage = !CollectionDatasource.Operation.REFRESH.equals(e.getOperation());
                onCollectionChanged();
            });

            impl.getCountButton().addActionListener(e -> onLinkClick());

            impl.getPrevButton().addActionListener(e -> onPrevClick());
            impl.getNextButton().addActionListener(e -> onNextClick());
            impl.getFirstButton().addActionListener(e -> onFirstClick());
            impl.getLastButton().addActionListener(e -> onLastClick());
            if (datasource.getState() == Datasource.State.VALID) {
                onCollectionChanged();
            }
        }
    }

    @Override
    public ListComponent getOwner() {
        return owner;
    }

    @Override
    public void setOwner(ListComponent owner) {
        this.owner = owner;
    }

    protected void onCollectionChanged() {
        if (datasource == null) {
            return;
        }

        String msgKey;
        size = datasource.size();
        start = 0;

        if (datasource instanceof CollectionDatasource.SupportsPaging) {
            CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
            if (samePage) {
                state = lastState;
                start = ds.getFirstResult();
            } else if ((size == 0 || size < ds.getMaxResults()) && ds.getFirstResult() == 0) {
                state = State.FIRST_COMPLETE;
                lastState = state;
            } else if (size == ds.getMaxResults() && ds.getFirstResult() == 0) {
                state = State.FIRST_INCOMPLETE;
                lastState = state;
            } else if (size == ds.getMaxResults() && ds.getFirstResult() > 0) {
                state = State.MIDDLE;
                start = ds.getFirstResult();
                lastState = state;
            } else if (size < ds.getMaxResults() && ds.getFirstResult() > 0) {
                state = State.LAST;
                start = ds.getFirstResult();
                lastState = state;
            } else {
                state = State.FIRST_COMPLETE;
                lastState = state;
            }
        } else {
            state = State.FIRST_COMPLETE;
            lastState = state;
        }

        String countValue;
        switch (state) {
            case FIRST_COMPLETE:
                impl.getCountButton().setVisible(false);
                impl.getPrevButton().setVisible(false);
                impl.getNextButton().setVisible(false);
                impl.getFirstButton().setVisible(false);
                impl.getLastButton().setVisible(false);
                if (size % 100 > 10 && size % 100 < 20) {
                    msgKey = "table.rowsCount.msg2Plural1";
                } else {
                    switch (size % 10) {
                        case 1:
                            msgKey = "table.rowsCount.msg2Singular";
                            break;
                        case 2:
                        case 3:
                        case 4:
                            msgKey = "table.rowsCount.msg2Plural2";
                            break;
                        default:
                            msgKey = "table.rowsCount.msg2Plural1";
                    }
                }
                countValue = String.valueOf(size);
                break;
            case FIRST_INCOMPLETE:
                impl.getCountButton().setVisible(true);
                impl.getPrevButton().setVisible(false);
                impl.getNextButton().setVisible(true);
                impl.getFirstButton().setVisible(false);
                impl.getLastButton().setVisible(true);
                msgKey = "table.rowsCount.msg1";
                countValue = countValue(start, size);
                break;
            case MIDDLE:
                impl.getCountButton().setVisible(true);
                impl.getPrevButton().setVisible(true);
                impl.getNextButton().setVisible(true);
                impl.getFirstButton().setVisible(true);
                impl.getLastButton().setVisible(true);
                msgKey = "table.rowsCount.msg1";
                countValue = countValue(start, size);
                break;
            case LAST:
                impl.getCountButton().setVisible(false);
                impl.getPrevButton().setVisible(true);
                impl.getNextButton().setVisible(false);
                impl.getFirstButton().setVisible(true);
                impl.getLastButton().setVisible(false);
                msgKey = "table.rowsCount.msg2Plural2";
                countValue = countValue(start, size);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        String messagesPack = AppConfig.getMessagesPack();
        Messages messages = AppBeans.get(Messages.NAME);
        impl.getLabel().setText(messages.formatMessage(messagesPack, msgKey, countValue));

        if (impl.getCountButton().isVisible() && !refreshing) {
            impl.getCountButton().setText(messages.getMessage(messagesPack, "table.rowsCount.msg3"));
        }
        impl.repaint();
        impl.revalidate();
    }

    protected String countValue(int start, int size) {
        if (size == 0) {
            return String.valueOf(size);
        } else {
            return (start + 1) + "-" + (start + size);
        }
    }

    private void onLinkClick() {
        if (datasource == null || !(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        int count = ((CollectionDatasource.SupportsPaging) datasource).getCount();
        impl.getCountButton().setText(String.valueOf(count));
    }

    private void onNextClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

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
        if (owner instanceof DesktopAbstractTable) {
            JXTable table = (JXTable) ((DesktopAbstractTable) owner).getComponent();
            table.scrollRowToVisible(0);
        }
    }

    private void onPrevClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int newStart = ds.getFirstResult() - ds.getMaxResults();
        ds.setFirstResult(newStart < 0 ? 0 : newStart);
        refreshDatasource(ds);
        if (owner instanceof DesktopAbstractTable) {
            JXTable table = (JXTable) ((DesktopAbstractTable) owner).getComponent();
            table.scrollRowToVisible(0);
        }
    }

    protected void onFirstClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        ds.setFirstResult(0);
        refreshDatasource(ds);
        if (owner instanceof DesktopAbstractTable) {
            JXTable table = (JXTable) ((DesktopAbstractTable) owner).getComponent();
            table.scrollRowToVisible(0);
        }
    }

    protected void onLastClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int count = ((CollectionDatasource.SupportsPaging) datasource).getCount();
        int itemsToDisplay = count % ds.getMaxResults();
        if (itemsToDisplay == 0) itemsToDisplay = ds.getMaxResults();

        ds.setFirstResult(count - itemsToDisplay);
        refreshDatasource(ds);

        if (owner instanceof DesktopAbstractTable) {
            JXTable table = (JXTable) ((DesktopAbstractTable) owner).getComponent();
            table.scrollRowToVisible(0);
        }
    }


    private void refreshDatasource(CollectionDatasource.SupportsPaging ds) {
        refreshing = true;
        try {
            ds.refresh();
        } finally {
            refreshing = false;
        }
    }

    public static class RowsCountComponent extends JPanel {

        private JButton prevButton;
        private JButton nextButton;
        private JButton firstButton;
        private JButton lastButton;
        private JLabel label;
        private JButton countButton;
        private MigLayout layout;

        private final Dimension size = new Dimension(38, 25);

        public RowsCountComponent() {
            LC lc = new LC();
            lc.insetsAll("2");

            layout = new MigLayout(lc);
            if (LayoutAdapter.isDebug()) {
                lc.debug(1000);
            }
            setLayout(layout);

            firstButton = new JButton("<<");
            add(firstButton);
            firstButton.setPreferredSize(size);
            firstButton.setMinimumSize(size);

            prevButton = new JButton("<");
            add(prevButton);
            prevButton.setPreferredSize(size);
            prevButton.setMinimumSize(size);

            label = new JLabel();
            add(label);

            countButton = new JXHyperlink();
            countButton.setText("[?]");
            add(countButton);

            nextButton = new JButton(">");
            add(nextButton);
            nextButton.setPreferredSize(size);
            nextButton.setMinimumSize(size);

            lastButton = new JButton(">>");
            add(lastButton);
            lastButton.setPreferredSize(size);
            lastButton.setMinimumSize(size);
        }

        public JLabel getLabel() {
            return label;
        }

        public JButton getCountButton() {
            return countButton;
        }

        public JButton getPrevButton() {
            return prevButton;
        }

        public JButton getNextButton() {
            return nextButton;
        }

        public JButton getFirstButton() {
            return firstButton;
        }

        public JButton getLastButton() {
            return lastButton;
        }
    }
}