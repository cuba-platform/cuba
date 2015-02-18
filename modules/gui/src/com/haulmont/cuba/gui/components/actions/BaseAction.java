/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Action that supports user defined permissions and UI permissions for actions.
 * It reacts on context and will be enabled only if it is permitted and allowed for current context.
 * Descendants may override {@link #isPermitted()} and {@link #isApplicable()} methods to define constraints in which
 * action will be enabled.
 *
 * @author artamonov
 * @version $Id$
 */
public abstract class BaseAction extends AbstractAction
        implements Action.HasTarget, Action.UiPermissionAware, CollectionDatasourceListener<Entity> {

    private boolean uiPermissionEnabled = true;
    private boolean uiPermissionVisible = true;

    private boolean userDefinedEnabled = true;
    private boolean userDefinedVisible = true;

    protected ListComponent target;

    protected BaseAction(String id) {
        this(id, null);
    }

    protected BaseAction(String id, @Nullable String shortcut) {
        this(null, id, shortcut);
    }

    protected BaseAction(ListComponent owner, String id, @Nullable String shortcut) {
        super(id, shortcut);

        this.target = owner;
    }

    protected boolean isPermitted() {
        return true;
    }

    protected boolean isApplicable() {
        return true;
    }

    @Override
    public void setVisible(boolean visible) {
        if (this.userDefinedVisible != visible) {
            this.userDefinedVisible = visible;

            refreshState();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.userDefinedEnabled != enabled) {
            this.userDefinedEnabled = enabled;

            refreshState();
        }
    }

    protected void setVisibleInternal(boolean visible) {
        super.setVisible(visible);
    }

    protected void setEnabledInternal(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void refreshState() {
        super.refreshState();

        setVisibleInternal(userDefinedVisible && uiPermissionVisible);

        setEnabledInternal(userDefinedEnabled && isPermitted() && isApplicable()
                && uiPermissionEnabled && uiPermissionVisible);
    }

    protected <T extends Entity> T getTargetSingleSelected() {
        if (target != null) {
            return target.getSingleSelected();
        } else {
            return null;
        }
    }

    protected <T extends Entity> Set<T> getTargetSelection() {
        if (target != null) {
            return target.getSelected();
        } else {
            return Collections.emptySet();
        }
    }

    @Nullable
    protected CollectionDatasource getTargetDatasource() {
        if (target != null) {
            return target.getDatasource();
        } else {
            return null;
        }
    }

    @Nonnull
    protected CollectionDatasource getTargetDatasourceNN() {
        CollectionDatasource ownerDatasource = getTargetDatasource();
        if (ownerDatasource == null) {
            throw new IllegalStateException("Null owner datasource");
        }
        return ownerDatasource;
    }

    @Override
    public ListComponent getTarget() {
        return target;
    }

    @Override
    public void setTarget(ListComponent target) {
        if (this.target != target) {
            this.target = target;

            refreshState();
        }
    }

    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
        refreshState();
    }

    @Override
    public void itemChanged(Datasource<Entity> ds, @Nullable Entity prevItem, @Nullable Entity item) {
        refreshState();
    }

    @Override
    public void stateChanged(Datasource<Entity> ds, Datasource.State prevState, Datasource.State state) {
        refreshState();
    }

    @Override
    public void valueChanged(Entity source, String property, @Nullable Object prevValue, @Nullable Object value) {
        refreshState();
    }

    @Override
    public boolean isUiPermissionEnabled() {
        return uiPermissionEnabled;
    }

    @Override
    public void setUiPermissionEnabled(boolean uiPermissionEnabled) {
        if (this.uiPermissionEnabled != uiPermissionEnabled) {
            this.uiPermissionEnabled = uiPermissionEnabled;

            refreshState();
        }
    }

    @Override
    public boolean isUiPermissionVisible() {
        return uiPermissionVisible;
    }

    @Override
    public void setUiPermissionVisible(boolean uiPermissionVisible) {
        if (this.uiPermissionVisible != uiPermissionVisible) {
            this.uiPermissionVisible = uiPermissionVisible;

            refreshState();
        }
    }
}