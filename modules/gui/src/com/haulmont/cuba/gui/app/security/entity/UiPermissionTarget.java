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

package com.haulmont.cuba.gui.app.security.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

@com.haulmont.chile.core.annotations.MetaClass(name = "sec$UiTarget")
@SystemLevel
public class UiPermissionTarget extends AbstractPermissionTarget
        implements AssignableTarget {

    @MetaProperty(mandatory = true)
    private UiPermissionVariant permissionVariant = UiPermissionVariant.NOTSET;

    @MetaProperty(mandatory = true)
    private String screen;

    @MetaProperty(mandatory = true)
    private String component;

    public UiPermissionTarget(String id, String caption, String permissionValue) {
        super(id, caption);
        this.permissionValue = permissionValue;
    }

    public UiPermissionTarget(String id, String caption, String permissionValue, UiPermissionVariant permissionVariant) {
        super(id, caption);
        this.permissionValue = permissionValue;
        this.permissionVariant = permissionVariant;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public boolean isAssigned() {
        return permissionVariant != UiPermissionVariant.NOTSET;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public UiPermissionVariant getPermissionVariant() {
        return permissionVariant;
    }

    public void setPermissionVariant(UiPermissionVariant permissionVariant) {
        this.permissionVariant = permissionVariant;
    }
}