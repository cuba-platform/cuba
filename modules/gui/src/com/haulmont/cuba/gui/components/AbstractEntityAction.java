/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.*;

/**
 * Generic action for entities. One Action for table, datasource and entity.
 * Selected entity and multiselect will be checked automatically.
 * <p>
 * Create one action class both for table and editor screen, entity will be reloaded automatically after the action is
 * performed.
 *
 * @author Zaharchenko
 * @version $Id$
 */
public abstract class AbstractEntityAction<T extends Entity> extends AbstractAction {

    private static final long serialVersionUID = 4263878244286411498L;
    private T entity;
    protected IFrame frame;
    protected Table table;
    protected Datasource<T> datasource;


    /**
     * Constructor for entity.
     *
     * @param id     action ID
     * @param entity selected entity
     * @param frame  frame containing this action
     */
    public AbstractEntityAction(String id, T entity, IFrame frame) {
        super(id);
        this.entity = entity;
        initAction(frame);
    }

    /**
     * Constructor for table.
     *
     * @param id    action ID
     * @param table table contains action
     */
    public AbstractEntityAction(String id, Table table) {
        super(id);
        this.table = table;
        initAction(table.getFrame());
    }

    /**
     * Constructor for table.
     *
     * @param id         action ID
     * @param datasource datasource with entity
     * @param frame      frame containing this action
     */
    public AbstractEntityAction(String id, Datasource<T> datasource, IFrame frame) {
        super(id);
        this.datasource = datasource;
        initAction(frame);
    }

    protected void initAction(IFrame frame) {
        this.frame = frame;
    }

    /**
     * Whether the action supports multiselect. Override to provide specific behaviour.
     * If multiselect is supoported use {@link #getEntities()}.
     *
     * @return true if multiselect is supported
     */
    protected Boolean isSupportMultiselect() {
        return false;
    }

    /**
     * Whether the action asks for confirmation before action perform.
     * In message pack with action there should be 'confirmation.{@link #getId()}' messages property.
     * Override to provide specific behaviour.
     *
     * @return true if action asks for confirmation
     */
    protected Boolean isConfirmation() {
        return false;
    }


    /**
     * Update selected entities after action. Override to provide specific behaviour.
     *
     * @return true if action will update entities after action perform
     */
    protected Boolean isUpdateSelectedEntities() {
        return true;
    }

    /**
     * Show after action notification action.
     * In message pack with action there should be 'notification.{@link #getId()}' messages property.
     * Override to provide specific behaviour.
     *
     * @return true if notification will show after action perform.
     */
    protected Boolean isShowAfterActionNotification() {
        return true;
    }

    /**
     * Set entity to action or datasource.
     *
     * @param newEntity
     */
    protected void setEntity(T newEntity) {
        if (entity != null) entity = newEntity;
        if (datasource != null) datasource.setItem(newEntity);
        if (table != null) table.getDatasource().updateItem(newEntity);
    }

    /**
     * @return entity from datasource or single selected entity from table
     */
    protected T getEntity() {
        if (entity != null) return entity;
        if (datasource != null) return datasource.getItem();
        if (table != null) return table.getSingleSelected();
        return null;
    }

    /**
     * @return List of selected entities from table or List with one entity from datasource
     */
    protected List<T> getEntities() {
        List<T> entities = new ArrayList<>();
        if (table != null) {
            for (T entity : (Set<T>) table.getSelected()) {
                entities.add(entity);
            }
        } else if (getEntity() != null) entities.add(getEntity());
        return entities;
    }

    /**
     * @return selected entities ids
     */
    protected List<UUID> getEntitiesIds() {
        List<UUID> ids = new ArrayList<>();
        for (Entity<UUID> entity : getEntities()) {
            ids.add(entity.getId());
        }
        return ids;
    }

    protected String getMessage(String id) {
        return messages.getMessage(getClass(), id);
    }

    protected String getActionMessage(String id) {
        return messages.getMessage(AbstractEntityAction.class, id);
    }

    /**
     * Reload and update in datasources selected entities
     */
    protected void updateSelectedEntities() {
        for (T entity : getEntities()) {
            setEntity(reloadEntity(entity));
        }
    }

    protected String getConfirmationCaption() {
        return getActionMessage("confirmation");
    }

    protected String getConfirmationText() {
        return getMessage("confirmation." + getId());
    }

    protected boolean entityIsNotSelected() {
        if (getEntities() == null || getEntities().isEmpty()) {
            frame.showOptionDialog(getActionMessage("notification.warning"), getNoEntityMessage(), IFrame.MessageType.WARNING, Arrays.<Action>asList(new DialogAction(DialogAction.Type.OK)));
            return true;
        }
        return false;
    }

    protected String getNoEntityMessage() {
        return getActionMessage("notification.noEntity");
    }

    protected void showAfterActionNotification() {
        frame.showNotification(getNotificationMessage(), IFrame.NotificationType.HUMANIZED);
    }

    protected String getNotificationMessage() {
        return getMessage("notification." + getId());
    }

    protected boolean supportMultiselect() {
        if (!isSupportMultiselect() && getEntities().size() != 1) {
            frame.showOptionDialog(getActionMessage("notification.warning"), getNotSupportMultiselect(), IFrame.MessageType.WARNING, Arrays.<Action>asList(new DialogAction(DialogAction.Type.OK)));
            return true;
        }
        return false;
    }

    protected String getNotSupportMultiselect() {
        return getActionMessage("notification.notSupportMultiselect");
    }


    @Override
    public final void actionPerform(final Component buttonComponent) {
        if (entityIsNotSelected()) return;
        if (supportMultiselect()) return;
        if (isConfirmation()) {
            frame.showOptionDialog(getConfirmationCaption(), getConfirmationText(),
                    IFrame.MessageType.CONFIRMATION, Arrays.<Action>asList(new DialogAction(DialogAction.Type.OK) {
                @Override
                public void actionPerform(Component component) {
                    execute(buttonComponent);
                }
            }, new DialogAction(DialogAction.Type.CANCEL)));
        } else {
            execute(buttonComponent);
        }
    }

    private void execute(Component buttonComponent) {
        doActionPerform(buttonComponent);
        if (isUpdateSelectedEntities()) updateSelectedEntities();
        if (isShowAfterActionNotification()) showAfterActionNotification();
    }

    protected T reloadEntity(T entity) {
        View view = null;
        if (table != null) view = table.getDatasource().getView();
        else if (datasource != null) view = datasource.getView();
        else view = AppBeans.get(Metadata.class).getViewRepository().getView(entity.getClass(), View.LOCAL);
        return frame.getDsContext().getDataSupplier().reload(entity, view);
    }

    /**
     * Override this to perform action
     *
     * @param component
     */
    public abstract void doActionPerform(Component component);

}
