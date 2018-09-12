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
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.function.Consumer;

@Component
public class Editors {

    @Inject
    protected BeanLocator beanLocator;

    @Inject
    protected Metadata metadata;

    public <E extends Entity> void createEntity(CollectionContainer<E> container,
                                              Class<? extends Screen> screenClass,
                                              @Nullable Consumer<E> initializer,
                                              @Nullable DataContext parentDataContext) {
        Screens screens = beanLocator.get(Screens.class);

        Class<E> entityClass = container.getEntityMetaClass().getJavaClass();
        E entity = metadata.create(entityClass);

        if (initializer != null) {
            initializer.accept(entity);
        }

        Screen screen = screens.create(screenClass, OpenMode.THIS_TAB);
        if (screen instanceof EditorScreen) {
            EditorScreen<E> editorScreen = (EditorScreen) screen;

            editorScreen.setEntityToEdit(entity);

            if (parentDataContext != null) {
                UiControllerUtils.getScreenData(screen).getDataContext().setParent(parentDataContext);
            }

            screen.addAfterCloseListener(afterCloseEvent -> {
                CloseAction closeAction = afterCloseEvent.getCloseAction();
                if ((closeAction instanceof StandardCloseAction) && ((StandardCloseAction) closeAction).getActionId().equals(Window.COMMIT_ACTION_ID)) {
                    container.getMutableItems().add(0, editorScreen.getEditedEntity());
                }
            });
            screens.show(screen);
        }
    }

    public <E extends Entity> void editEntity(CollectionContainer<E> container,
                                              Class<? extends Screen> screenClass,
                                              @Nullable DataContext parentDataContext) {
        E selectedUserRole = container.getItemOrNull();
        if (selectedUserRole != null) {
            Screens screens = beanLocator.get(Screens.class);

            Screen screen = screens.create(screenClass, OpenMode.THIS_TAB);
            if (screen instanceof EditorScreen) {
                EditorScreen<E> editorScreen = (EditorScreen) screen;

                editorScreen.setEntityToEdit(selectedUserRole);

                if (parentDataContext != null) {
                    UiControllerUtils.getScreenData(screen).getDataContext().setParent(parentDataContext);
                }

                screen.addAfterCloseListener(afterCloseEvent -> {
                    CloseAction closeAction = afterCloseEvent.getCloseAction();
                    if ((closeAction instanceof StandardCloseAction) && ((StandardCloseAction) closeAction).getActionId().equals(Window.COMMIT_ACTION_ID)) {
                        container.replaceItem(editorScreen.getEditedEntity());
                    }
                });
                screens.show(screen);
            }
        }
    }
}
