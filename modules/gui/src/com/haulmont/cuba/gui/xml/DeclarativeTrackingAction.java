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

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;

import javax.annotation.Nullable;

/**
 */
public class DeclarativeTrackingAction extends DeclarativeAction implements Action.HasTarget, Action.UiPermissionAware {

    public DeclarativeTrackingAction(String id, String caption, String description, String icon, String enable, String visible,
                                     String methodName, @Nullable String shortcut, Component.ActionsHolder holder) {
        super(id, caption, description, icon, enable, visible, methodName, shortcut, holder);
    }

    @Override
    protected boolean isApplicable() {
        return target != null && !target.getSelected().isEmpty();
    }
}