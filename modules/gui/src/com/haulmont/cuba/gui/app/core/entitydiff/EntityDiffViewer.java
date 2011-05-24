/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.core.app.EntitySnapshotService;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.EntityDiff;
import com.haulmont.cuba.core.global.EntityPropertyDiff;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class EntityDiffViewer extends AbstractFrame {
    private static final long serialVersionUID = -6858393916181794311L;

    private List<EntitySnapshot> snapshots;

    private CollectionDatasource<EntitySnapshot, UUID> snapshotsDs;
    private Datasource entityDiffDs;
    private DiffTreeDatasource diffDs;

    private Table snapshotsTable;
    private TreeTable diffTable;
    private Label itemStateLabel;

    private Component itemStateField;
    private Component diffValuesField;

    public EntityDiffViewer(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);

        entityDiffDs = getDsContext().get("entityDiffDs");
        snapshotsDs = getDsContext().get("snapshotsDs");
        diffDs = getDsContext().get("diffDs");

        snapshotsTable = getComponent("versionsList");
        diffTable = getComponent("diffTable");
        itemStateLabel = getComponent("itemStateLabel");

        diffValuesField = getComponent("diffValuesField");
        itemStateField = getComponent("itemStateField");

        diffTable.setStyleProvider(new DiffStyleProvider());

        Button compareBtn = getComponent("compareBtn");
        compareBtn.setAction(new AbstractAction("actions.Compare") {
            @Override
            public void actionPerform(Component component) {
                entityDiffDs.setItem(null);

                EntitySnapshot firstSnap = null;
                EntitySnapshot secondSnap = null;

                Set selected = snapshotsTable.getSelected();
                Object[] selectedItems = selected.toArray();
                if ((selected.size() == 2)) {
                    firstSnap = (EntitySnapshot) selectedItems[0];
                    secondSnap = (EntitySnapshot) selectedItems[1];
                } else if (selected.size() == 1) {
                    firstSnap = null;
                    secondSnap = (EntitySnapshot) selectedItems[0];
                    int index = snapshots.indexOf(secondSnap);
                    if (index > 0)
                        firstSnap = snapshots.get(index - 1);
                }

                if ((secondSnap != null) || (firstSnap != null)) {
                    EntityDiff diff = diffDs.loadDiff(firstSnap, secondSnap);
                    entityDiffDs.setItem(diff);
                }

                diffTable.refresh();
            }
        });

        diffDs.addListener(new DatasourceListener<EntityPropertyDiff>() {
            @Override
            public void itemChanged(Datasource<EntityPropertyDiff> ds,
                                    EntityPropertyDiff prevItem, EntityPropertyDiff item) {
                boolean stateVisible = (item != null) && (item.hasStateValues());

                itemStateField.setVisible(stateVisible);
                diffValuesField.setVisible(stateVisible);

                if (item != null) {
                    EntityPropertyDiff.ItemState itemState = item.getItemState();
                    String messageCode = "ItemState." + itemState.toString();
                    itemStateLabel.setValue(getMessage(messageCode));
                    itemStateField.setVisible(item.itemStateVisible());
                }
            }

            @Override
            public void stateChanged(Datasource<EntityPropertyDiff> ds,
                                     Datasource.State prevState, Datasource.State state) {
            }

            @Override
            public void valueChanged(EntityPropertyDiff source, String property,
                                     Object prevValue, Object value) {
            }
        });
    }

    public void loadVersions(BaseEntity entity) {
        EntitySnapshotService snapshotService = ServiceLocator.lookup(EntitySnapshotService.NAME);
        snapshots = snapshotService.getSnapshots(
                MetadataProvider.getSession().getClass(entity.getClass()),
                entity.getUuid());

        for (EntitySnapshot snapshot : snapshots) {
            snapshotsDs.includeItem(snapshot);
        }
    }
}
