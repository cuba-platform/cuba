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

public class RemoveAction extends AbstractAction implements CollectionDatasourceListener {

    private static final long serialVersionUID = -8700360141431140203L;

    public static final String ACTION_ID = "remove";

    protected final ListComponent owner;
    protected final boolean autocommit;
    protected final CollectionDatasource datasource;
    protected MetaProperty metaProperty;

    public RemoveAction(ListComponent owner) {
        this(owner, true, ACTION_ID);
    }

    public RemoveAction(ListComponent owner, boolean autocommit) {
        this(owner, autocommit, ACTION_ID);
    }

    public RemoveAction(ListComponent owner, boolean autocommit, String id) {
        super(id);
        this.owner = owner;
        this.autocommit = autocommit;
        this.datasource = owner.getDatasource();
        if (datasource instanceof PropertyDatasource) {
            metaProperty = ((PropertyDatasource) datasource).getProperty();
        }
    }

    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Remove");
    }

    public boolean isManyToMany() {
        return metaProperty != null && metaProperty.getRange() != null && metaProperty.getRange().getCardinality() != null && metaProperty.getRange().getCardinality() == Range.Cardinality.MANY_TO_MANY;
    }

    public boolean isEnabled() {
        return super.isEnabled() &&
                (isManyToMany() || UserSessionProvider.getUserSession().isEntityOpPermitted(datasource.getMetaClass(), EntityOp.DELETE));
    }

    public void actionPerform(Component component) {
        if(!isEnabled()) return;
        final Set selected = owner.getSelected();
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
                        new AbstractAction("ok") {
                            public String getCaption() {
                                return MessageProvider.getMessage(messagesPackage, "actions.Ok");
                            }

                            public boolean isEnabled() {
                                return true;
                            }

                            @Override
                            public String getIcon() {
                                return "icons/ok.png";
                            }

                            public void actionPerform(Component component) {
                                doRemove(selected, autocommit);
                                afterRemove(selected);
                            }
                        }, new AbstractAction("cancel") {
                            public String getCaption() {
                                return MessageProvider.getMessage(messagesPackage, "actions.Cancel");
                            }

                            public boolean isEnabled() {
                                return true;
                            }

                            @Override
                            public String getIcon() {
                                return "icons/cancel.png";
                            }

                            public void actionPerform(Component component) {
                            }
                        }
                }
        );
    }

    protected String getConfirmationMessage(String messagesPackage) {
        return MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation.Remove");
    }

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
