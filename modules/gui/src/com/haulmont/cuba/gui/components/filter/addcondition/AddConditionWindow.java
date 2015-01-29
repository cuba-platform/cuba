/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * Window for adding new filter condition
 *
 * @author gorbunkov
 * @version $Id$
 */
public class AddConditionWindow extends AbstractWindow {

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected ConditionDescriptorsDs conditionDescriptorsDs;

    @Inject
    protected TextField treeFilter;

    @Inject
    protected Tree tree;

    @Inject
    protected Button cancelBtn;

    @Inject
    protected Button selectBtn;

    @Inject
    protected ThemeConstantsManager themeConstantsManager;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        ThemeConstants theme = themeConstantsManager.getConstants();
        getDialogParams()
                .setHeight(Integer.valueOf(theme.get("cuba.gui.addFilterCondition.dialog.height")))
                .setWidth(Integer.valueOf(theme.get("cuba.gui.addFilterCondition.dialog.width")))
                .setResizable(true);
        conditionDescriptorsDs.refresh(params);
        expandTreeRoots();
        tree.setItemClickAction(new AbstractAction("select") {
            @Override
            public void actionPerform(Component component) {
                select();
            }
        });
    }

    public void expandTreeRoots() {
        Collection<UUID> rootItemIds = conditionDescriptorsDs.getRootItemIds();
        for (UUID rootItemId : rootItemIds) {
            tree.expand(rootItemId);
        }
    }

    public void search() {
        String filterValue = treeFilter.getValue();
        conditionDescriptorsDs.setFilter(filterValue);
        tree.expandTree();
    }

    public void select() {
        AbstractConditionDescriptor item = conditionDescriptorsDs.getItem();
        if (item == null) {
            showNotification(getMessage("AddCondition.selectCondition"), NotificationType.WARNING);
        } else {
            close(COMMIT_ACTION_ID);
        }
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }

    public AbstractConditionDescriptor getDescriptor() {
        AbstractConditionDescriptor item = conditionDescriptorsDs.getItem();
        return item;
    }
}
