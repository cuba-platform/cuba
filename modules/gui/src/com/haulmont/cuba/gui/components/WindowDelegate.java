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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.settings.Settings;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.Collection;

// todo get rid of it
public class WindowDelegate {

    public static final String LOOKUP_ITEM_CLICK_ACTION_ID = "lookupItemClickAction";
    public static final String LOOKUP_ENTER_PRESSED_ACTION_ID = "lookupEnterPressed";
    public static final String LOOKUP_SELECT_ACTION_ID = "lookupSelectAction";
    public static final String LOOKUP_CANCEL_ACTION_ID = "lookupCancelAction";

    protected Window window;
    protected Window wrapper;
    protected Settings settings;

    protected WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

    public WindowDelegate(Window window) {
        this.window = window;
    }

    public Window getWrapper() {
        return wrapper;
    }

    @Deprecated
    public Datasource getDatasource() {
        Datasource ds = null;
        Element element = ((Component.HasXmlDescriptor) window).getXmlDescriptor();
        String datasourceName = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasourceName)) {
            DsContext context = LegacyFrame.of(window).getDsContext();
            if (context != null) {
                ds = context.get(datasourceName);
            }
        }

        if (ds == null) {
            throw new GuiDevelopmentException("Can't find main datasource", window.getId());
        }

        return ds;
    }

    public Settings getSettings() {
        return settings;
    }

    public void saveSettings() {
    }

    public void deleteSettings() {
    }

    public void applySettings(Settings settings) {
    }

    public void disposeComponents() {
        ComponentsHelper.walkComponents(
                window,
                (component, name) -> {
                    if (component instanceof Component.Disposable) {
                        ((Component.Disposable) component).dispose();
                    }
                }
        );
    }

    public boolean isValid() {
        Collection<Component> components = ComponentsHelper.getComponents(window);
        for (Component component : components) {
            if (component instanceof Validatable) {
                Validatable validatable = (Validatable) component;
                if (validatable.isValidateOnCommit() && !validatable.isValid())
                    return false;
            }
        }
        return true;
    }

    public void validate() throws ValidationException {
        Collection<Component> components = ComponentsHelper.getComponents(window);
        for (Component component : components) {
            if (component instanceof Validatable) {
                Validatable validatable = (Validatable) component;
                if (validatable.isValidateOnCommit()) {
                    validatable.validate();
                }
            }
        }
    }

    public void postValidate(ValidationErrors errors) {
        if (wrapper instanceof AbstractWindow) {
            ((AbstractWindow) wrapper).postValidate(errors);
        }
    }

    public void showValidationErrors(ValidationErrors errors) {
        if (wrapper instanceof AbstractWindow) {
            ((AbstractWindow) wrapper).showValidationErrors(errors);
        }
    }

    public boolean preClose(String actionId) {
        if (wrapper instanceof AbstractWindow) {
            return ((AbstractWindow) wrapper).preClose(actionId);
        }

        return true;
    }

    public boolean isModified() {
        if (wrapper instanceof Window.Committable)
            return ((Window.Committable) wrapper).isModified();
        else
            return LegacyFrame.of(window).getDsContext() != null && LegacyFrame.of(window).getDsContext().isModified();
    }
}