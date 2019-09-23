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

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Standard picker field action for clearing the field value.
 * <p>
 * Should be defined for {@code PickerField} or its subclass in a screen XML descriptor.
 */
@ActionType(ClearAction.ID)
public class ClearAction extends BaseAction implements PickerField.PickerFieldAction, InitializingBean {

    public static final String ID = "picker_clear";

    protected PickerField pickerField;
    protected Icons icons;
    protected Messages messages;
    protected Configuration configuration;

    protected boolean editable = true;

    public ClearAction() {
        super(ID);
    }

    public ClearAction(String id) {
        super(id);
    }

    @Inject
    protected void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Override
    public void afterPropertiesSet() {
        setShortcut(configuration.getConfig(ClientConfig.class).getPickerClearShortcut());
        setDescription(messages.getMainMessage("pickerField.action.clear.tooltip")
                + " (" + getShortcutCombination().format() + ")");
    }

    @Override
    public void setPickerField(@Nullable PickerField pickerField) {
        this.pickerField = pickerField;
    }

    @Override
    public void editableChanged(PickerField pickerField, boolean editable) {
        setEditable(editable);

        if (editable) {
            setIcon(icons.get(CubaIcon.PICKERFIELD_CLEAR));
        } else {
            setIcon(icons.get(CubaIcon.PICKERFIELD_CLEAR_READONLY));
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

        setIcon(icons.get(CubaIcon.PICKERFIELD_CLEAR));
    }

    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            execute();
        } else {
            // call action perform handlers from super, delegate execution
            super.actionPerform(component);
        }
    }

    /**
     * Executes the action.
     */
    @SuppressWarnings("unchecked")
    public void execute() {
        // remove entity if it is a composition
        Object value = pickerField.getValue();
        ValueSource valueSource = pickerField.getValueSource();
        if (value != null && !value.equals(pickerField.getEmptyValue()) && valueSource instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) pickerField.getValueSource();
            Entity entity = (Entity) pickerField.getValue();
            if (entityValueSource.getMetaPropertyPath() != null
                    && entityValueSource.getMetaPropertyPath().getMetaProperty().getType() == MetaProperty.Type.COMPOSITION) {
                FrameOwner screen = pickerField.getFrame().getFrameOwner();
                DataContext dataContext = UiControllerUtils.getScreenData(screen).getDataContext();
                dataContext.remove(entity);
            }
        }
        // Set the value as if the user had set it
        pickerField.setValueFromUser(pickerField.getEmptyValue());
    }
}