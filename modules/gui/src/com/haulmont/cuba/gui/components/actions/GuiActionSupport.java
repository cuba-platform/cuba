/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.gui.data.Datasource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Contains utility methods used by GUI actions.
 */
@Component(GuiActionSupport.NAME)
public class GuiActionSupport {

    public static final String NAME = "cuba_GuiActionSupport";

    @Inject
    protected ClientConfig clientConfig;

    @Inject
    private ViewRepository viewRepository;

    @Inject
    private EntityStates entityStates;

    /**
     * Returns an entity reloaded with the view of the target datasource if it is wider than the set of attributes
     * that is loaded in the given entity instance.
     */
    public Entity reloadEntityIfNeeded(Entity entity, Datasource targetDatasource) {
        if (clientConfig.getReloadEntityAfterEditIfRequired()) {
            View view = targetDatasource.getView();
            if (view == null) {
                view = viewRepository.getView(entity.getClass(), View.LOCAL);
            }
            if (!entityStates.isLoadedWithView(entity, view)) {
                return targetDatasource.getDsContext().getDataSupplier().reload(entity, view);
            }
        }
        return entity;
    }
}
