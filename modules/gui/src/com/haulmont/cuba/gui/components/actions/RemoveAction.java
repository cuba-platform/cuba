/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.03.11 18:55
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.Set;

/**
 * Standard list action to remove an entity instance.
 * <p>
 *      Action's behaviour can be customized by providing arguments to constructor, as well as overriding the following
 *      methods:
 *      <ul>
 *          <li>{@link #getCaption()}</li>
 *          <li>{@link #isEnabled()}</li>
 *          <li>{@link #getConfirmationMessage(String)}</li>
 *          <li>{@link #getConfirmationTitle(String)}</li>
 *          <li>{@link #afterRemove(java.util.Set)} )}</li>
 *      </ul>
 * </p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RemoveAction extends AbstractAction implements CollectionDatasourceListener {

    private static final long serialVersionUID = -8700360141431140203L;

    public static final String ACTION_ID = ListActionType.REMOVE.getId();

    protected final ListComponent owner;
    protected final boolean autocommit;
    protected final CollectionDatasource datasource;
    protected MetaProperty metaProperty;

    /**
     * The simplest constructor. The action has default name and autocommit=true.
     * @param owner    component containing this action
     */
    public RemoveAction(ListComponent owner) {
        this(owner, true, ACTION_ID);
    }

    /**
     * Constructor that allows to specify autocommit value. The action has default name.
     * @param owner        component containing this action
     * @param autocommit    whether to commit datasource immediately
     */
    public RemoveAction(ListComponent owner, boolean autocommit) {
        this(owner, autocommit, ACTION_ID);
    }

    /**
     * Constructor that allows to specify action's identifier and autocommit value.
     * @param owner        component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param id            action's identifier
     */
    public RemoveAction(ListComponent owner, boolean autocommit, String id) {
        super(id);
        this.owner = owner;
        this.autocommit = autocommit;
        this.datasource = owner.getDatasource();
        if (datasource instanceof PropertyDatasource) {
            metaProperty = ((PropertyDatasource) datasource).getProperty();
        }
    }

    /**
     * Returns the action's caption. Override to provide a specific caption.
     * @return  localized caption
     */
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Remove");
    }

    /**
     * @return  true if this list is connected to PropertyDatasource and this datasource contains ManyToMany property
     */
    public boolean isManyToMany() {
        return metaProperty != null && metaProperty.getRange() != null
                && metaProperty.getRange().getCardinality() != null
                && metaProperty.getRange().getCardinality() == Range.Cardinality.MANY_TO_MANY;
    }

    /**
     * Whether the action is currently enabled. Override to provide specific behaviour.
     * @return  true if enabled
     */
    public boolean isEnabled() {
        return super.isEnabled() &&
                (isManyToMany() || UserSessionProvider.getUserSession().isEntityOpPermitted(datasource.getMetaClass(), EntityOp.DELETE));
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     * @param component component invoking action
     */
    public void actionPerform(Component component) {
        if (!isEnabled())
            return;
        Set selected = owner.getSelected();
        if (!selected.isEmpty()) {
            confirmAndRemove(selected);
        }
    }

    protected void confirmAndRemove(final Set selected) {
        final String messagesPackage = AppConfig.getMessagesPack();
        owner.getFrame().showOptionDialog(
                getConfirmationTitle(messagesPackage),
                getConfirmationMessage(messagesPackage),
                IFrame.MessageType.CONFIRMATION,
                new Action[]{
                        new DialogAction(DialogAction.Type.OK) {

                            public void actionPerform(Component component) {
                                doRemove(selected, autocommit);
                                afterRemove(selected);
                            }
                        }, new DialogAction(DialogAction.Type.CANCEL) {

                            public void actionPerform(Component component) {
                            }
                        }
                }
        );
    }

    /**
     * Provides confirmation dialog message.
     * @param   messagesPackage   message pack containing the message
     * @return  localized message
     */
    protected String getConfirmationMessage(String messagesPackage) {
        return MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation.Remove");
    }

    /**
     * Provides confirmation dialog title.
     * @param   messagesPackage   message pack containing the title
     * @return  localized title
     */
    protected String getConfirmationTitle(String messagesPackage) {
        return MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation");
    }

    protected void doRemove(Set selected, boolean autocommit) {
        for (Object item : selected) {
            datasource.removeItem((Entity) item);
        }

        if (this.autocommit) {
            try {
                datasource.commit();
            } catch (RuntimeException e) {
                datasource.refresh();
                throw e;
            }
        }
    }

    /**
     * Hook invoked after remove.
     * @param selected  set of removed instances
     */
    protected void afterRemove(Set selected) {
    }

    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation) {
    }

    @Override
    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
        setEnabled(item != null);
    }

    @Override
    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
        setEnabled(Datasource.State.VALID.equals(state) && ds.getItem() != null);
    }

    @Override
    public void valueChanged(Object source, String property, Object prevValue, Object value) {
    }
}
