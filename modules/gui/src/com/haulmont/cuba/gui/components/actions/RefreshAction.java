/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;

import java.util.Map;

/**
 * Standard list action to refresh a list of entities.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor or setting properties.
 *
 */
public class RefreshAction extends BaseAction implements Action.HasBeforeAfterHandlers {

    public static final String ACTION_ID = ListActionType.REFRESH.getId();

    protected ListComponent owner;

    protected Map<String, Object> refreshParams;

    protected Runnable beforeActionPerformedHandler;
    protected Runnable afterActionPerformedHandler;

    /**
     * The simplest constructor. The action has default name.
     * @param target    component containing this action
     */
    public RefreshAction(ListComponent target) {
        this(target, ACTION_ID);
    }

    /**
     * Constructor that allows to specify action's name.
     * @param target        component containing this action
     * @param id            action's identifier
     */
    public RefreshAction(ListComponent target, String id) {
        super(id);
        this.owner = target;
        this.caption = messages.getMainMessage("actions.Refresh");

        ThemeConstantsManager thCM = AppBeans.get(ThemeConstantsManager.NAME);
        this.icon = thCM.getThemeValue("actions.Refresh.icon");
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     * @param component component invoking action
     */
    @Override
    public void actionPerform(Component component) {
        if (beforeActionPerformedHandler != null) {
            beforeActionPerformedHandler.run();
        }

        CollectionDatasource datasource = owner.getDatasource();

        Map<String, Object> params = getRefreshParams();
        if (params != null) {
            datasource.refresh(params);
        } else {
            datasource.refresh();
        }

        if (afterActionPerformedHandler != null) {
            afterActionPerformedHandler.run();
        }
    }

    /**
     * @return  parameters for {@link CollectionDatasource#refresh(java.util.Map)} method
     */
    public Map<String, Object> getRefreshParams() {
        return refreshParams;
    }

    /**
     * @param refreshParams parameters for {@link CollectionDatasource#refresh(java.util.Map)} method
     */
    public void setRefreshParams(Map<String, Object> refreshParams) {
        this.refreshParams = refreshParams;
    }

    @Override
    public Runnable getBeforeActionPerformedHandler() {
        return beforeActionPerformedHandler;
    }

    @Override
    public void setBeforeActionPerformedHandler(Runnable handler) {
        this.beforeActionPerformedHandler = handler;
    }

    @Override
    public Runnable getAfterActionPerformedHandler() {
        return afterActionPerformedHandler;
    }

    @Override
    public void setAfterActionPerformedHandler(Runnable handler) {
        this.afterActionPerformedHandler = handler;
    }
}
