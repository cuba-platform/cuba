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
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.Screen;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;

@ActionType(LookupAction.ID)
public class LookupAction extends BaseAction implements PickerField.PickerFieldAction, HasDefaultDescription, InitializingBean {

    public static final String ID = "picker_lookup";

    protected PickerField pickerField;

    @Inject
    protected ScreenBuilders screenBuilders;
    protected Icons icons;
    protected Messages messages;
    protected Configuration configuration;

    protected String defaultDescription;

    protected boolean editable = true;

    public LookupAction() {
        super(LookupAction.ID);
    }

    public LookupAction(String id) {
        super(id);
    }

    @Override
    public void setPickerField(PickerField pickerField) {
        this.pickerField = pickerField;
    }

    @Override
    public void editableChanged(PickerField pickerField, boolean editable) {
        setEditable(editable);
        if (editable) {
            setIcon(icons.get(CubaIcon.PICKERFIELD_LOOKUP));
        } else {
            setIcon(icons.get(CubaIcon.PICKERFIELD_LOOKUP_READONLY));
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

        setIcon(icons.get(CubaIcon.PICKERFIELD_LOOKUP));
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Inject
    protected void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        defaultDescription = messages.getMainMessage("pickerField.action.lookup.tooltip");
        setShortcut(configuration.getConfig(ClientConfig.class).getPickerLookupShortcut());
    }

    @Override
    public String getDefaultDescription() {
        return defaultDescription;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            MetaClass metaClass = pickerField.getMetaClass();
            if (metaClass == null) {
                throw new DevelopmentException("Neither metaClass nor datasource/property is specified " +
                        "for the PickerField", "action ID", getId());
            }

            Screen lookupScreen = screenBuilders.lookup(pickerField)
                    .build();
            lookupScreen.show();
        } else {
            // call action perform handlers from super, delegate execution
            super.actionPerform(component);
        }
    }
}