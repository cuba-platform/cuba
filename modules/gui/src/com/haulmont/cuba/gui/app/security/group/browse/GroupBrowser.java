/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.security.group.browse;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.StringUtils;

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

    @Named("groupsTree.copy")
    protected Action groupCopyAction;

    @Named("groupsTree.edit")
    protected EditAction groupEditAction;

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
    protected Metadata metadata;

    @Inject
    protected Security security;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected ThemeConstants themeConstants;

    protected boolean constraintsTabInitialized, attributesTabInitialized;

    protected GroupPropertyCreateAction attributeCreateAction;
    protected GroupPropertyCreateAction constraintCreateAction;
    protected GroupPropertyCreateAction userCreateAction;

    @Override
    public void init(final Map<String, Object> params) {
        CreateAction createAction = new CreateAction(groupsTree) {
            @Override
            protected void afterCommit(Entity entity) {
                groupsTree.expandTree();
            }
        };
        groupsTree.addAction(createAction);
        createAction.setCaption(getMessage("action.create"));

        createAction.setOpenType(WindowManager.OpenType.DIALOG);
        groupEditAction.setOpenType(WindowManager.OpenType.DIALOG);

        groupCreateButton.addAction(createAction);
        groupCreateButton.addAction(groupCopyAction);

        userCreateAction = new GroupPropertyCreateAction(usersTable) {
            @Override
            protected void afterCommit(Entity entity) {
                usersTable.getDatasource().refresh();
            }
        };

        groupsTree.addAction(new RemoveAction(groupsTree) {
            @Override
            public boolean isApplicableTo(Datasource.State state, Entity item) {
                return super.isApplicableTo(state, item)
                        && groupsDs.getChildren((UUID) item.getId()).isEmpty();
            }
        });
        usersTable.addAction(userCreateAction);
        usersTable.addAction(new ItemTrackingAction("moveToGroup") {
            {
                refreshState();
            }

            @Override
            public String getIcon() {
                return "icons/move.png";
            }

            @Override
            public void actionPerform(Component component) {
                final Set<User> selected = usersTable.getSelected();
                if (!selected.isEmpty()) {
                    getDialogParams().setResizable(false);
                    getDialogParams().setHeight(themeConstants.getInt("cuba.gui.GroupBrowser.moveToGroupLookup.height"));

                    Window lookupWindow = openLookup("sec$Group.lookup", new Lookup.Handler() {
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

                    lookupWindow.addListener(new CloseListener() {
                        @Override
                        public void windowClosed(String actionId) {
                            usersTable.requestFocus();
                        }
                    });
                }
            }

            @Override
            public void refreshState() {
                MetaClass userMetaClass = metadata.getSession().getClass(User.class);
                setEnabled(security.isEntityOpPermitted(userMetaClass, EntityOp.UPDATE));
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

        final boolean hasPermissionsToCreateGroup =
                security.isEntityOpPermitted(metadata.getSession().getClass(Group.class),
                        EntityOp.CREATE);

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
                groupCopyAction.setEnabled(hasPermissionsToCreateGroup && item != null);
            }
        });

        groupsDs.refresh();
        groupsTree.expandTree();

        final Collection<UUID> itemIds = groupsDs.getRootItemIds();
        if (!itemIds.isEmpty()) {
            groupsTree.setSelected(groupsDs.getItem(itemIds.iterator().next()));
        }

        groupCreateButton.setEnabled(hasPermissionsToCreateGroup);
        groupCopyAction.setEnabled(hasPermissionsToCreateGroup);
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

        final Table constraintsTable = getComponentNN("constraintsTable");
        constraintCreateAction = new GroupPropertyCreateAction(constraintsTable);
        constraintsTable.addAction(constraintCreateAction);

        constraintsTable.addGeneratedColumn(
                "entityName",
                new Table.ColumnGenerator<Constraint>() {
                    @Override
                    public Component generateCell(Constraint constraint) {
                        if (StringUtils.isEmpty(constraint.getEntityName())) {
                            return componentsFactory.createComponent(Label.NAME);
                        }

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

        final Table attributesTable = getComponentNN("attributesTable");
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