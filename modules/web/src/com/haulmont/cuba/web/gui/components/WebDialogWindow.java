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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.gui.components.DialogWindow;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaWindow;
import com.vaadin.ui.Component;

import javax.inject.Inject;

public class WebDialogWindow extends WebWindow implements DialogWindow {
    protected CubaWindow dialogWindow;

    protected BeanLocator beanLocator;

    public WebDialogWindow() {
        this.dialogWindow = new CubaWindow();
        this.dialogWindow.setStyleName("c-app-dialog-window");

        this.dialogWindow.setContent(component);
    }

    @Inject
    protected void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Override
    public void setIcon(String icon) {
        super.setIcon(icon);

        if (icon == null) {
            dialogWindow.setIcon(null);
        } else {
            IconResolver iconResolver = beanLocator.get(IconResolver.NAME);
            dialogWindow.setIcon(iconResolver.getIconResource(icon));
        }
    }

    @Override
    public void setCaption(String caption) {
        super.setCaption(caption);

        this.dialogWindow.setCaption(caption);
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);

        this.dialogWindow.setDescription(description);
    }

    @Override
    public Component getComposition() {
        return dialogWindow;
    }

    @Override
    public void setResizable(boolean resizable) {
        dialogWindow.setResizable(resizable);
    }

    @Override
    public boolean isResizable() {
        return dialogWindow.isResizable();
    }

    @Override
    public void setDraggable(boolean draggable) {
        dialogWindow.setDraggable(draggable);
    }

    @Override
    public boolean isDraggable() {
        return dialogWindow.isDraggable();
    }

    @Override
    public void setCloseable(boolean closeable) {
        super.setCloseable(closeable);

        dialogWindow.setClosable(closeable);
    }

    @Override
    public void setModal(boolean modal) {
        dialogWindow.setModal(modal);
    }

    @Override
    public boolean isModal() {
        return dialogWindow.isModal();
    }

    @Override
    public void setCloseOnClickOutside(boolean closeOnClickOutside) {
        dialogWindow.setCloseOnClickOutside(closeOnClickOutside);
    }

    @Override
    public boolean isCloseOnClickOutside() {
        return dialogWindow.getCloseOnClickOutside();
    }

    @Override
    public void setWindowMode(WindowMode mode) {
        dialogWindow.setWindowMode(com.vaadin.shared.ui.window.WindowMode.valueOf(mode.name()));
    }

    @Override
    public WindowMode getWindowMode() {
        return WindowMode.valueOf(dialogWindow.getWindowMode().name());
    }

    @Override
    public void center() {
        dialogWindow.center();
    }

    @Override
    public void setPositionX(int positionX) {
        dialogWindow.setPositionX(positionX);
    }

    @Override
    public int getPositionX() {
        return dialogWindow.getPositionX();
    }

    @Override
    public void setPositionY(int positionY) {
        dialogWindow.setPositionY(positionY);
    }

    @Override
    public int getPositionY() {
        return dialogWindow.getPositionY();
    }
}