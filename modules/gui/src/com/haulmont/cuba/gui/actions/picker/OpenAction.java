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
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenContext;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;

@ActionType(OpenAction.ID)
public class OpenAction extends BaseAction implements PickerField.PickerFieldAction, HasDefaultDescription, InitializingBean {

    public static final String ID = "picker_open";

    protected PickerField<Entity> pickerField;
    protected Icons icons;

    protected Messages messages;
    protected Configuration configuration;

    @Inject
    protected ScreenBuilders screenBuilders;

    protected String defaultDescription;

    protected boolean editable = true;

    public OpenAction() {
        super(ID);
    }

    public OpenAction(String id) {
        super(id);
    }

    @Inject
    protected void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        defaultDescription = messages.getMainMessage("pickerField.action.open.tooltip");
        setShortcut(configuration.getConfig(ClientConfig.class).getPickerOpenShortcut());
    }

    @Override
    public String getDefaultDescription() {
        return defaultDescription;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setPickerField(PickerField pickerField) {
        this.pickerField = pickerField;
    }

    @Override
    public void editableChanged(PickerField pickerField, boolean editable) {
        // open action is available in read-only picker
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

    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            if (!checkFieldValue())
                return;

            Entity entity = pickerField.getValue();

            if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
                ScreenContext screenContext = ComponentsHelper.getScreenContext(pickerField);
                Notifications notifications = screenContext.getNotifications();

                notifications.create(NotificationType.HUMANIZED)
                        .withDescription(messages.getMainMessage("OpenAction.objectIsDeleted"))
                        .show();

                return;
            }

            MetaClass metaClass = pickerField.getMetaClass();
            if (metaClass == null) {
                throw new DevelopmentException("Neither metaClass nor datasource/property is specified " +
                        "for the PickerField", "action ID", getId());
            }

            Screen editorScreen = screenBuilders.editor(pickerField)
                    .build();

            editorScreen.show();
        } else {
            // call action perform handlers from super, delegate execution
            super.actionPerform(component);
        }
    }

    protected boolean checkFieldValue() {
        Entity entity = pickerField.getValue();
        return entity != null;
    }
}