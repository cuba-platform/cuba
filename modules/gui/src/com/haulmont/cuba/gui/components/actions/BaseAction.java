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

import javax.annotation.Nullable;
import java.util.List;

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

    private boolean enabledByUiPermissions = true;
    private boolean visibleByUiPermissions = true;

    private boolean enabledExplicitly = true;
    private boolean visibleExplicitly = true;

    protected ListComponent target;

    protected BaseAction(String id) {
        this(id, null);
    }

    protected BaseAction(String id, @Nullable String shortcut) {
        super(id, shortcut);
    }

    protected boolean isPermitted() {
        return true;
    }

    protected boolean isApplicable() {
        return true;
    }

    @Override
    public void setVisible(boolean visible) {
        if (this.visibleExplicitly != visible) {
            this.visibleExplicitly = visible;

            refreshState();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabledExplicitly != enabled) {
            this.enabledExplicitly = enabled;

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

        setVisibleInternal(visibleExplicitly && visibleByUiPermissions);

        setEnabledInternal(enabledExplicitly && isPermitted() && isApplicable()
                && enabledByUiPermissions && visibleByUiPermissions);
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
    public boolean isEnabledByUiPermissions() {
        return enabledByUiPermissions;
    }

    @Override
    public void setEnabledByUiPermissions(boolean enabledByUiPermissions) {
        if (this.enabledByUiPermissions != enabledByUiPermissions) {
            this.enabledByUiPermissions = enabledByUiPermissions;

            refreshState();
        }
    }

    @Override
    public boolean isVisibleByUiPermissions() {
        return visibleByUiPermissions;
    }

    @Override
    public void setVisibleByUiPermissions(boolean visibleByUiPermissions) {
        if (this.visibleByUiPermissions != visibleByUiPermissions) {
            this.visibleByUiPermissions = visibleByUiPermissions;

            refreshState();
        }
    }
}