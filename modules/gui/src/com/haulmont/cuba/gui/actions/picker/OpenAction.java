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

package com.haulmont.cuba.gui.actions.picker;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.EditorScreens;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenContext;

import javax.inject.Inject;

@ActionType(OpenAction.ID)
public class OpenAction extends BaseAction implements PickerField.PickerFieldAction {

    public static final String ID = "picker_open";

    protected PickerField<Entity> pickerField;
    protected Icons icons;

    protected Messages messages;
    protected EditorScreens editorScreens;

    protected boolean editable = true;

    public OpenAction() {
        super(ID);
    }

    public OpenAction(String id) {
        super(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setPickerField(PickerField pickerField) {
        this.pickerField = pickerField;
    }

    @Override
    public void editableChanged(PickerField pickerField, boolean editable) {
        setEditable(editable);

        if (editable) {
            setIcon(icons.get(CubaIcon.PICKERFIELD_OPEN));
        } else {
            setIcon(icons.get(CubaIcon.PICKERFIELD_OPEN_READONLY));
        }
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    protected void setEditable(boolean editable) {
        boolean oldValue = this.editable;
        if (oldValue != editable) {
            this.editable = editable;
            firePropertyChange(PROP_EDITABLE, oldValue, editable);
        }
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icons = icons;

        setIcon(icons.get(CubaIcon.PICKERFIELD_OPEN));
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Inject
    protected void setEditorScreens(EditorScreens editorScreens) {
        this.editorScreens = editorScreens;
    }

    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            Entity entity = pickerField.getValue();
            if (entity == null) {
                return;
            }

            if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
                ScreenContext screenContext = ComponentsHelper.getScreenContext(pickerField);
                Notifications notifications = screenContext.getNotifications();

                notifications.create()
                        .setDescription(messages.getMainMessage("OpenAction.objectIsDeleted"))
                        .setType(NotificationType.HUMANIZED)
                        .show();

                return;
            }

            MetaClass metaClass = pickerField.getMetaClass();
            if (metaClass == null) {
                throw new DevelopmentException("Neither metaClass nor datasource/property is specified " +
                        "for the PickerField", "action ID", getId());
            }

            Class<Entity> entityClass = metaClass.getJavaClass();
            Window window = ComponentsHelper.getWindowNN(pickerField);

            // todo composition
            // todo inverseProperty support

            Screen editorScreen = editorScreens.builder(entityClass, window.getFrameOwner())
                    .editEntity(entity)
                    .withField(pickerField)
                    .build();

            editorScreen.show();
        } else {
            // call action perform handlers from super, delegate execution
            super.actionPerform(component);
        }
    }
}