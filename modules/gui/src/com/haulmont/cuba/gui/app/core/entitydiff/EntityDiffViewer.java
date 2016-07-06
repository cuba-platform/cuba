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

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.entity.diff.EntityDiff;
import com.haulmont.cuba.core.entity.diff.EntityPropertyDiff;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

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

    public void loadVersions(Entity entity) {
        snapshotsDs.setEntity(entity);
        snapshotsDs.refresh();

        snapshotsTable.repaint();
    }
}