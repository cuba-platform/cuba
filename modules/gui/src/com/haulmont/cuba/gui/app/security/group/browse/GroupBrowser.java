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
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.SessionAttribute;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class GroupBrowser extends AbstractWindow {

    @Named("groups")
    protected Tree tree;

    @Named("groups.create")
    protected CreateAction groupCreateAction;

    @Named("groups.copy")
    protected Action groupCopyAction;

    @Named("groups.edit")
    protected EditAction groupEditAction;

    @Inject
    protected PopupButton groupCreateButton;

    @Named("users.create")
    protected CreateAction userCreateAction;

    @Named("groups")
    protected CollectionDatasource treeDs;

    @Inject
    protected Table users;

    @Inject
    protected Tabsheet tabsheet;

    @Inject
    protected DataService dataService;
    private boolean constraintsTabInitialized, attributesTabInitialized;

    public GroupBrowser(Window frame) {
        super(frame);
    }

    public void init(final Map<String, Object> params) {
        treeDs.refresh();
        tree.expandTree();

        final Collection itemIds = treeDs.getItemIds();
        if (!itemIds.isEmpty()) {
            tree.setSelected(treeDs.getItem(itemIds.iterator().next()));
        }
        groupCreateAction.setCaption(getMessage("action.create"));

        groupCreateAction.setOpenType(WindowManager.OpenType.DIALOG);
        groupEditAction.setOpenType(WindowManager.OpenType.DIALOG);

        groupCreateButton.addAction(groupCreateAction);
        groupCreateButton.addAction(groupCopyAction);

        users.addAction(
                new CreateAction(users) {
                    @Override
                    public Map<String, Object> getInitialValues() {
                        return Collections.<String, Object>singletonMap("group", tree.getSelected());
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

    public void copyGroup(Component component) {
        Group group = (Group) treeDs.getItem();
        if (group != null) {
            group = dataService.reload(group, "group.edit");
            List<Entity> toCommit = cloneGroup(group, group.getParent(), new ArrayList<Entity>());
            CommitContext ctx = new CommitContext(toCommit);
            ServiceLocator.getDataService().commit(ctx);
            treeDs.refresh();
        }
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

    public void moveUser(Component component) {
        final Set<User> selected = users.getSelected();
        if (!selected.isEmpty()) {
            getDialogParams().setResizable(false);
            getDialogParams().setHeight(400);
            openLookup("sec$Group.lookup", new Lookup.Handler() {
                @Override
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

    private void initConstraintsTab() {
        if (constraintsTabInitialized)
            return;

        final Table constraints = getComponent("constraints");
        constraints.addAction(
                new AbstractAction("create") {

                    @Override
                    public String getCaption() {
                        return MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Create");
                    }

                    @Override
                    public String getIcon() {
                        return "icons/create.png";
                    }

                    @Override
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
                        return MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Create");
                    }

                    @Override
                    public String getIcon() {
                        return "icons/create.png";
                    }

                    @Override
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
                                    @Override
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

        attributesTabInitialized = true;
    }
}
