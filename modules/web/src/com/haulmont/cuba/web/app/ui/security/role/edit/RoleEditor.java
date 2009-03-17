package com.haulmont.cuba.web.app.ui.security.role.edit;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Tree;

import java.util.Map;

public class RoleEditor extends AbstractEditor {
    public RoleEditor(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        Tree entityPermissionsTree = getComponent("entity-permissions-tree");
        entityPermissionsTree.getDatasource().refresh();

        Tree screenPermissionsTree = getComponent("screen-permissions-tree");
        screenPermissionsTree.getDatasource().refresh();

        Tree specificPermissionsTree = getComponent("specific-permissions-tree");
        specificPermissionsTree.getDatasource().refresh();
    }
}
