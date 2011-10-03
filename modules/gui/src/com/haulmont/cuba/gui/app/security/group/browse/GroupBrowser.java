/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.group.browse;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.SessionAttribute;
import com.haulmont.cuba.security.entity.User;

import java.util.*;

public class GroupBrowser extends AbstractWindow {

    protected Tree tree;

    private boolean constraintsTabInitialized, attributesTabInitialized;

    public GroupBrowser(Window frame) {
        super(frame);
    }

    public void init(final Map<String, Object> params) {
        tree = getComponent("groups");

        final CollectionDatasource treeDS = tree.getDatasource();
        treeDS.refresh();
        tree.expandTree();

        final Collection itemIds = treeDS.getItemIds();
        if (!itemIds.isEmpty()) {
            tree.setSelected(treeDS.getItem(itemIds.iterator().next()));
        }

        tree.addAction(new CreateAction(tree, WindowManager.OpenType.DIALOG));
        tree.addAction(new EditAction(tree, WindowManager.OpenType.DIALOG));
        tree.addAction(new RemoveAction(tree));
        ComponentsHelper.createActions(tree, EnumSet.of(ListActionType.REFRESH));

        PopupButton createButton = getComponent("createButton");
        createButton.addAction(new CreateAction(tree, WindowManager.OpenType.DIALOG) {
            @Override
            public String getCaption() {
                return getMessage("action.create");
            }
        });
        createButton.addAction(new AbstractAction("copy") {
            @Override
            public void actionPerform(final Component component) {
                Group group = (Group) treeDS.getItem();
                if (group != null) {
                    group = treeDS.getDsContext().getDataService().reload(group, "group.edit");
                    List<Entity> toCommit = cloneGroup(group, group.getParent(), new ArrayList<Entity>());
                    CommitContext ctx = new CommitContext(toCommit);
                    ServiceLocator.getDataService().commit(ctx);
                    treeDS.refresh();
                }
            }

            @Override
            public String getCaption() {
                return getMessage("action.copy");
            }
        });

        tree.addAction(createButton.getAction("copy"));

        final Table users = getComponent("users");

        users.addAction(
                new CreateAction(users) {
                    @Override
                    protected Map<String, Object> getInitialValues() {
                        final Map<String, Object> map = new HashMap<String, Object>();
                        map.put("group", tree.getSelected());
                        return map;
                    }
                }
        );

        users.addAction(
                new EditAction(users) {
                    @Override
                    protected void afterCommit(Entity entity) {
                        final CollectionDatasource ds = users.getDatasource();
                        ds.refresh();
                        users.setSelected((Entity) null);
                    }
                }
        );

        users.addAction(new AbstractAction("moveToGroup") {
            public String getCaption() {
                return getMessage("users.moveToGroup");
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                final Set<User> selected = users.getSelected();
                if (!selected.isEmpty()) {
                    getDialogParams().setResizable(false);
                    getDialogParams().setHeight(400);
                    openLookup("sec$Group.lookup", new Lookup.Handler() {
                                public void handleLookup(Collection items) {
                                    if (items.size() == 1) {
                                        Group group = (Group) items.iterator().next();
                                        for (User user : selected) {
                                            user.setGroup(group);
                                        }
                                        final CollectionDatasource ds = users.getDatasource();
                                        ds.commit();
                                        ds.refresh();
                                        users.setSelected((Entity) null);
                                    }
                                }
                            }, WindowManager.OpenType.DIALOG);
                }
            }
        });

        users.addAction(new RefreshAction(users));

        Tabsheet tabsheet = getComponent("tabsheet");
        tabsheet.addListener(
                new Tabsheet.TabChangeListener() {
                    public void tabChanged(Tabsheet.Tab newTab) {
                        if ("constraintsTab".equals(newTab.getName()))
                            initConstraintsTab();
                        else if ("attributesTab".equals(newTab.getName()))
                            initAttributesTab();
                    }
                }
        );
    }

    private List<Entity> cloneGroup(Group group, Group parent, List<Entity> list) {
        Group resultGroup = new Group();
        if (group != null) {
            resultGroup.setName(group.getName());
            resultGroup.setParent(parent);
            if (group.getConstraints() != null)
                for (Constraint constraint : group.getConstraints())
                    list.add(cloneConstraint(constraint, resultGroup));
            if (group.getSessionAttributes() != null)
                for (SessionAttribute attribute : group.getSessionAttributes())
                    list.add(cloneSessionAttribute(attribute, resultGroup));
            CommitContext ctx = new CommitContext(Arrays.asList(resultGroup));
            ServiceLocator.getDataService().commit(ctx);
            LoadContext ltx = new LoadContext(Group.class).setView("group.edit");
            LoadContext.Query query = ltx.setQueryString("select g from sec$Group g where g.parent.id = :group and g.deleteTs is null");
            query.addParameter("group", group);
            List<Group> groups = getDsContext().getDataService().loadList(ltx);
            for (Group g : groups)
                list = cloneGroup(g, resultGroup, list);
        }
        return list;
    }

    private SessionAttribute cloneSessionAttribute(SessionAttribute attribute, Group group) {
        SessionAttribute resultAttribute = new SessionAttribute();
        resultAttribute.setName(attribute.getName());
        resultAttribute.setDatatype(attribute.getDatatype());
        resultAttribute.setStringValue(attribute.getStringValue());
        resultAttribute.setGroup(group);
        return resultAttribute;
    }

    private Constraint cloneConstraint(Constraint constraint, Group group) {
        Constraint resultConstraint = new Constraint();
        resultConstraint.setEntityName(constraint.getEntityName());
        resultConstraint.setJoinClause(constraint.getJoinClause());
        resultConstraint.setWhereClause(constraint.getWhereClause());
        resultConstraint.setGroup(group);
        return resultConstraint;
    }

    private void initConstraintsTab() {
        if (constraintsTabInitialized)
            return;

        final Table constraints = getComponent("constraints");
        constraints.addAction(
                new AbstractAction("create") {

                    @Override
                    public String getCaption() {
                        String mp = AppConfig.getMessagesPack();
                        return MessageProvider.getMessage(mp, "actions.Create");
                    }

                    public void actionPerform(Component component) {
                        Set<Group> selected = tree.getSelected();
                        if (selected.size() != 1)
                            return;

                        Constraint constraint = new Constraint();
                        constraint.setGroup(selected.iterator().next());
                        final Window window = openEditor(
                                constraints.getDatasource().getMetaClass().getName() + ".edit",
                                constraint,
                                WindowManager.OpenType.THIS_TAB
                        );
                        window.addListener(
                                new CloseListener() {
                                    public void windowClosed(String actionId) {
                                        if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                                            constraints.getDatasource().refresh();
                                        }
                                    }
                                }
                        );
                    }
                }
        );

        ComponentsHelper.createActions(constraints, EnumSet.of(ListActionType.EDIT, ListActionType.REMOVE, ListActionType.REFRESH));

        constraintsTabInitialized = true;
    }

    private void initAttributesTab() {
        if (attributesTabInitialized)
            return;

        final Table attributes = getComponent("attributes");
        attributes.addAction(
                new AbstractAction("create") {

                    @Override
                    public String getCaption() {
                        String mp = AppConfig.getMessagesPack();
                        return MessageProvider.getMessage(mp, "actions.Create");
                    }

                    public void actionPerform(Component component) {
                        Set<Group> selected = tree.getSelected();
                        if (selected.size() != 1)
                            return;

                        Constraint constraint = new Constraint();
                        constraint.setGroup(selected.iterator().next());
                        final Window window = openEditor(
                                attributes.getDatasource().getMetaClass().getName() + ".edit",
                                constraint,
                                WindowManager.OpenType.THIS_TAB
                        );
                        window.addListener(
                                new CloseListener() {
                                    public void windowClosed(String actionId) {
                                        if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                                            attributes.getDatasource().refresh();
                                        }
                                    }
                                }
                        );
                    }
                }
        );

        ComponentsHelper.createActions(attributes, EnumSet.of(ListActionType.EDIT, ListActionType.REMOVE, ListActionType.REFRESH));

        attributesTabInitialized = true;
    }
}
