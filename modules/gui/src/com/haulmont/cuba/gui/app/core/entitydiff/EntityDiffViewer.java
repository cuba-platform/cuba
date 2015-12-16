/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.entity.diff.EntityDiff;
import com.haulmont.cuba.core.entity.diff.EntityPropertyDiff;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 * @author artamonov
 * @version $Id$
 */
public class EntityDiffViewer extends AbstractFrame {

    @Inject
    private EntitySnapshotsDatasource snapshotsDs;

    @Inject
    private Datasource<EntityDiff> entityDiffDs;

    @Inject
    private DiffTreeDatasource diffDs;

    @Inject
    private Table snapshotsTable;

    @Inject
    private TreeTable<EntityPropertyDiff> diffTable;

    @Inject
    private Label itemStateLabel;

    @Inject
    private Label valuesHeader;

    @Inject
    private Component itemStateField;

    @Inject
    private Component diffValuesField;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        diffTable.setStyleProvider(new DiffStyleProvider());
        diffTable.setIconProvider(new DiffIconProvider());

        diffDs.addItemChangeListener(e -> {
            boolean valuesVisible = (e.getItem() != null) && (e.getItem().hasStateValues());
            boolean stateVisible = (e.getItem() != null) && (e.getItem().hasStateValues() && e.getItem().itemStateVisible());

            valuesHeader.setVisible(stateVisible || valuesVisible);
            itemStateField.setVisible(stateVisible);
            diffValuesField.setVisible(valuesVisible);

            if (e.getItem() != null) {
                EntityPropertyDiff.ItemState itemState = e.getItem().getItemState();
                if (itemState != EntityPropertyDiff.ItemState.Normal) {
                    String messageCode = "ItemState." + itemState.toString();
                    itemStateLabel.setValue(getMessage(messageCode));
                    itemStateLabel.setVisible(true);
                } else {
                    itemStateField.setVisible(false);
                }
            }
        });
    }

    @SuppressWarnings("unused")
    public void compareSnapshots() {
        entityDiffDs.setItem(null);

        EntitySnapshot firstSnap = null;
        EntitySnapshot secondSnap = null;

        Set selected = snapshotsTable.getSelected();
        Object[] selectedItems = selected.toArray();
        if ((selected.size() == 2)) {
            firstSnap = (EntitySnapshot) selectedItems[0];
            secondSnap = (EntitySnapshot) selectedItems[1];
        } else if (selected.size() == 1) {
            secondSnap = (EntitySnapshot) selectedItems[0];
            firstSnap = snapshotsDs.getLatestSnapshot();
            if (firstSnap == secondSnap)
                firstSnap = null;
        }

        if ((secondSnap != null) || (firstSnap != null)) {
            EntityDiff diff = diffDs.loadDiff(firstSnap, secondSnap);
            entityDiffDs.setItem(diff);
        }

        diffTable.refresh();
        diffTable.expandAll();
    }

    public void loadVersions(BaseEntity entity) {
        snapshotsDs.setEntity(entity);
        snapshotsDs.refresh();

        snapshotsTable.repaint();
    }
}