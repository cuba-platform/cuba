/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.web.tmp;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.gui.screen.events.BeforeShowEvent;
import com.haulmont.cuba.gui.util.OperationResult;

import javax.annotation.Nullable;
import javax.inject.Inject;

@UiController("dcScreen6")
@UiDescriptor("dc-screen-6.xml")
public class DcScreen6 extends Screen {

    @Inject
    protected EntityStates entityStates;

    protected Entity entityToEdit;

    public void setEntityToEdit(Entity entity) {
        this.entityToEdit = entity;
    }

    @Nullable
    public Entity getEditedEntity() {
        return getEditedEntityContainer().getItemOrNull();
    }

    protected InstanceContainer<Entity> getEditedEntityContainer() {
        return getScreenData().getContainer("userCont");
    }

    protected InstanceLoader getEditedEntityLoader() {
        InstanceLoader loader = getScreenData().findLoaderOf(getEditedEntityContainer());
        if (loader == null)
            throw new IllegalStateException("Edited entity loader is not defined");
        return loader;
    }

    @Subscribe
    protected void beforeShow(BeforeShowEvent event) {
        if (entityStates.isNew(entityToEdit)) {
            InstanceContainer<Entity> userCont = getEditedEntityContainer();
            userCont.setItem(entityToEdit);
            getScreenData().getDataContext().merge(entityToEdit);
        } else {
            InstanceLoader loader = getEditedEntityLoader();
            loader.setEntityId(entityToEdit.getId());
        }

        getScreenData().loadAll();
    }

    @Subscribe("okBtn")
    protected void onOkClick(Button.ClickEvent event) {
        closeWithCommit();
    }

    @Subscribe("cancelBtn")
    protected void onCancelClick(Button.ClickEvent event) {
        close(WINDOW_CLOSE_ACTION);
    }

    @Override
    public boolean hasUnsavedChanges() {
        return getScreenData().getDataContext().hasChanges();
    }

    @Override
    protected OperationResult commitChanges() {
        getScreenData().getDataContext().commit();
        return OperationResult.success();
    }
}
