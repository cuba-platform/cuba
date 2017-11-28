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
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import org.springframework.context.annotation.Scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Standard list action to refresh a list of entities.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor or setting properties.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 * &lt;bean id="cuba_RefreshAction" class="com.company.sample.gui.MyRefreshAction" scope="prototype"/&gt;
 * </pre>
 * Also, use {@code create()} static methods instead of constructors when creating the action programmatically.
 */
@org.springframework.stereotype.Component("cuba_RefreshAction")
@Scope("prototype")
public class RefreshAction extends BaseAction {

    public static final String ACTION_ID = ListActionType.REFRESH.getId();

    protected ListComponent owner;

    protected Map<String, Object> refreshParams;
    protected Supplier<Map<String, Object>> refreshParamsSupplier;

    protected Runnable beforeRefreshHandler;
    protected Runnable afterRefreshHandler;

    /**
     * Creates an action with default id.
     * @param target    component containing this action
     */
    public static RefreshAction create(ListComponent target) {
        return AppBeans.getPrototype("cuba_RefreshAction", target);
    }

    /**
     * Creates an action with the given id.
     * @param target    component containing this action
     */
    public static RefreshAction create(ListComponent target, String id) {
        return AppBeans.getPrototype("cuba_RefreshAction", target, id);
    }

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

        this.icon = AppBeans.get(Icons.class).get(CubaIcon.REFRESH_ACTION);
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     *
     * @param component component invoking action
     */
    @Override
    public void actionPerform(Component component) {
        if (beforeRefreshHandler != null) {
            beforeRefreshHandler.run();
        }

        CollectionDatasource datasource = owner.getDatasource();

        Map<String, Object> refreshParams = getRefreshParams();
        Map<String, Object> supplierParams = null;
        if (refreshParamsSupplier != null) {
            supplierParams = refreshParamsSupplier.get();
        }

        Map<String, Object> params = null;
        if (supplierParams != null || refreshParams != null) {
            params = new HashMap<>();
            params.putAll(refreshParams != null ? refreshParams : Collections.emptyMap());
            params.putAll(supplierParams != null ? supplierParams : Collections.emptyMap());
        }

        if (params != null) {
            datasource.refresh(params);
        } else {
            datasource.refresh();
        }

        if (afterRefreshHandler != null) {
            afterRefreshHandler.run();
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

    /**
     * @return supplier that provides parameters for {@link CollectionDatasource#refresh(java.util.Map)} method
     */
    public Supplier<Map<String, Object>> getRefreshParamsSupplier() {
        return refreshParamsSupplier;
    }

    /**
     * @param refreshParamsSupplier supplier that provides parameters for {@link CollectionDatasource#refresh(java.util.Map)} method
     */
    public void setRefreshParamsSupplier(Supplier<Map<String, Object>> refreshParamsSupplier) {
        this.refreshParamsSupplier = refreshParamsSupplier;
    }

    public Runnable getBeforeRefreshHandler() {
        return beforeRefreshHandler;
    }

    public void setBeforeRefreshHandler(Runnable beforeRefreshHandler) {
        this.beforeRefreshHandler = beforeRefreshHandler;
    }

    public Runnable getAfterRefreshHandler() {
        return afterRefreshHandler;
    }

    public void setAfterRefreshHandler(Runnable afterRefreshHandler) {
        this.afterRefreshHandler = afterRefreshHandler;
    }
}