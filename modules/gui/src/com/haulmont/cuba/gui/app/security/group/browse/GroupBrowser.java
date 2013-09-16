/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.security.group.browse;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class GroupBrowser extends AbstractWindow {

    @Inject
    protected UserManagementService userManagementService;

    @Named("groupsTree.create")
    protected CreateAction groupCreateAction;

    @Named("groupsTree.copy")
    protected Action groupCopyAction;

    @Named("groupsTree.edit")
    protected EditAction groupEditAction;

    @Inject
    protected Button removeButton;

    @Inject
    protected PopupButton groupCreateButton;

    @Inject
    protected HierarchicalDatasource<Group, UUID> groupsDs;

    @Inject
    protected Tree groupsTree;

    @Inject
    protected Table usersTable;

    @Inject
    protected TabSheet tabsheet;

    @Inject
    protected UserSession userSession;

    @Inject
    protected Metadata metadata;

    @Inject
    protected ComponentsFactory componentsFactory;

    protected boolean constraintsTabInitialized, attributesTabInitialized;

    protected GroupPropertyCreateAction attributeCreateAction;
    protected GroupPropertyCreateAction constraintCreateAction;
    protected GroupPropertyCreateAction userCreateAction;

    public void init(final Map<String, Object> params) {
        groupCreateAction.setCaption(getMessage("action.create"));

        groupCreateAction.setOpenType(WindowManager.OpenType.DIALOG);
        groupEditAction.setOpenType(WindowManager.OpenType.DIALOG);

        groupCreateButton.addAction(groupCreateAction);
        groupCreateButton.addAction(groupCopyAction);

        userCreateAction = new GroupPropertyCreateAction(usersTable) {
            @Override
            protected void afterCommit(Entity entity) {
                usersTable.getDatasource().refresh();
            }
        };
        groupsTree.addAction(new RemoveAction(groupsTree) {

            protected boolean enabledFlag = false;

            @Override
            public void setEnabled(boolean enabled) {
                this.enabledFlag = enabled;
                super.setEnabled(enabled);
            }

            @Override
            public boolean isEnabled() {
                return this.enabledFlag && super.isEnabled();
            }

            @Override
            public void actionPerform(Component component) {
                setEnabled(true);
                super.actionPerform(component);
            }
        });
        usersTable.addAction(userCreateAction);
        usersTable.addAction(new ItemTrackingAction("moveToGroup") {

            @Override
            public String getIcon() {
                return "icons/move.png";
            }

            @Override
            public void actionPerform(Component component) {
                final Set<User> selected = usersTable.getSelected();
                if (!selected.isEmpty()) {
                    getDialogParams().setResizable(false);
                    getDialogParams().setHeight(400);
                    openLookup("sec$Group.lookup", new Lookup.Handler() {
                        @Override
                        public void handleLookup(Collection items) {
                            if (items.size() == 1) {
                                Group group = (Group) items.iterator().next();
                                List<UUID> usersForModify = new ArrayList<>();
                                for (User user : selected) {
                                    usersForModify.add(user.getId());
                                }
                                userManagementService.moveUsersToGroup(usersForModify, group.getId());

                                usersTable.getDatasource().refresh();
                            }
                        }
                    }, WindowManager.OpenType.DIALOG);
                }
            }

            @Override
            public boolean isApplicableTo(Datasource.State state, Entity item) {
                return super.isApplicableTo(state, item) && userSession.isEntityOpPermitted(metadata.getSession().getClass(User.class),
                        EntityOp.UPDATE);
            }
        });

        tabsheet.addListener(
                new TabSheet.TabChangeListener() {
                    @Override
                    public void tabChanged(TabSheet.Tab newTab) {
                        if ("constraintsTab".equals(newTab.getName()))
                            initConstraintsTab();
                        else if ("attributesTab".equals(newTab.getName()))
                            initAttributesTab();
                    }
                }
        );

        // enable actions if group is selected
        groupsDs.addListener(new DsListenerAdapter<Group>() {
            @Override
            public void itemChanged(Datasource<Group> ds, Group prevItem, Group item) {
                if (userCreateAction != null)
                    userCreateAction.setEnabled(item != null);
                if (attributeCreateAction != null)
                    attributeCreateAction.setEnabled(item != null);
                if (constraintCreateAction != null)
                    constraintCreateAction.setEnabled(item != null);
                groupCopyAction.setEnabled(item != null);
                removeButton.setEnabled(item != null && groupsDs.getChildren(item.getId()).isEmpty());
            }
        });

        groupsDs.refresh();
        groupsTree.expandTree();

        final Collection<UUID> itemIds = groupsDs.getItemIds();
        if (!itemIds.isEmpty()) {
            groupsTree.setSelected(groupsDs.getItem(itemIds.iterator().next()));
        }

        boolean hasPermissionsToCreateGroup =
                userSession.isEntityOpPermitted(metadata.getSession().getClass(Group.class),
                        EntityOp.CREATE);

        if (groupCreateButton != null) {
            groupCreateButton.setEnabled(hasPermissionsToCreateGroup);
        }
    }

    public void copyGroup() {
        Group group = groupsDs.getItem();
        if (group != null) {
            userManagementService.copyAccessGroup(group.getId());
            groupsDs.refresh();
        }
    }

    protected void initConstraintsTab() {
        if (constraintsTabInitialized)
            return;

        final Table constraintsTable = getComponent("constraintsTable");
        constraintCreateAction = new GroupPropertyCreateAction(constraintsTable);
        constraintsTable.addAction(constraintCreateAction);

        constraintsTable.addGeneratedColumn(
                "entityName",
                new Table.ColumnGenerator<Constraint>() {
                    @Override
                    public Component generateCell(Constraint constraint) {
                        MetaClass effectiveMetaClass = metadata.getExtendedEntities().getEffectiveMetaClass(
                                metadata.getClassNN(constraint.getEntityName()));
                        Label label = componentsFactory.createComponent(Label.NAME);
                        label.setValue(effectiveMetaClass.getName());
                        return label;
                    }
                }
        );

        constraintsTabInitialized = true;
        constraintsTable.refresh();
    }

    protected void initAttributesTab() {
        if (attributesTabInitialized)
            return;

        final Table attributesTable = getComponent("attributesTable");
        attributeCreateAction = new GroupPropertyCreateAction(attributesTable);
        attributesTable.addAction(attributeCreateAction);

        attributesTabInitialized = true;
        attributesTable.refresh();
    }

    /**
     * Create action for the objects associated with the group
     */
    protected class GroupPropertyCreateAction extends CreateAction {

        public GroupPropertyCreateAction(ListComponent owner) {
            super(owner);
            Set<Group> selected = groupsTree.getSelected();
            setEnabled(selected != null && selected.size() == 1);
        }

        @Override
        public Map<String, Object> getInitialValues() {
            return Collections.<String, Object>singletonMap("group", groupsTree.getSelected());
        }

        @Override
        public void actionPerform(Component component) {
            Set<Group> selected = groupsTree.getSelected();
            if (selected == null || selected.size() != 1) {
                return;
            }

            super.actionPerform(component);
        }
    }
}