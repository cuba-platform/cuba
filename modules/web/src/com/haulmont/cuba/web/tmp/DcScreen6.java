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
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.events.BeforeShowEvent;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;

import javax.inject.Inject;

@UiController("dcScreen6")
@UiDescriptor("dc-screen-6.xml")
public class DcScreen6 extends StandardEditor<User> {

    @Inject
    protected Screens screens;
    @Inject
    protected CollectionContainer<UserRole> userRolesCont;

    @Override
    protected InstanceContainer<Entity> getEditedEntityContainer() {
        return getScreenData().getContainer("userCont");
    }

    @Subscribe
    protected void beforeShow(BeforeShowEvent event) {
        getScreenData().loadAll();
    }

    @Subscribe("editBtn")
    private void onEditClick(Button.ClickEvent event) {
        UserRole selectedUserRole = userRolesCont.getItemOrNull();
        if (selectedUserRole != null) {
            TmpUserRoleEdit userRoleEdit = screens.create(TmpUserRoleEdit.class, OpenMode.THIS_TAB);
            userRoleEdit.setEntityToEdit(selectedUserRole);

            ScreenUtils.getScreenData(userRoleEdit).getDataContext().setParent(getScreenData().getDataContext());

            userRoleEdit.addAfterCloseListener(afterCloseEvent -> {
                CloseAction closeAction = afterCloseEvent.getCloseAction();
                if ((closeAction instanceof StandardCloseAction) && ((StandardCloseAction) closeAction).getActionId().equals(Window.COMMIT_ACTION_ID)) {
                    userRolesCont.replaceItem(userRoleEdit.getEditedEntity());
                }
            });
            screens.show(userRoleEdit);
        }
    }

    @Subscribe("okBtn")
    protected void onOkClick(Button.ClickEvent event) {
        closeWithCommit();
    }

    @Subscribe("cancelBtn")
    protected void onCancelClick(Button.ClickEvent event) {
        close(WINDOW_CLOSE_ACTION);
    }
}