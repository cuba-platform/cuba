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

import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.sys.FrameImplementation;
import com.haulmont.cuba.gui.screen.FrameOwner;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

// todo
public class WebFragment extends WebVBoxLayout implements Fragment, FrameImplementation {

    @Override
    public FrameOwner getFrameOwner() {
        return null; // todo
    }

    @Override
    public FrameContext getContext() {
        return null;
    }

    @Override
    public void setContext(FrameContext ctx) {
        // todo
    }

    @Override
    public void registerComponent(Component component) {
        // todo
    }

    @Override
    public void unregisterComponent(Component component) {
        // todo
    }

    @Nullable
    @Override
    public Component getRegisteredComponent(String id) {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void validate() throws ValidationException {

    }

    @Override
    public boolean validate(List<Validatable> fields) {
        return false;
    }

    @Override
    public boolean validateAll() {
        return false;
    }

    @Override
    public WindowManager getWindowManager() {
        return null;
    }

    @Override
    public void addAction(Action action) {

    }

    @Override
    public void addAction(Action action, int index) {

    }

    @Override
    public void removeAction(@Nullable Action action) {

    }

    @Override
    public void removeAction(@Nullable String id) {

    }

    @Override
    public void removeAllActions() {

    }

    @Override
    public Collection<Action> getActions() {
        return null;
    }

    @Nullable
    @Override
    public Action getAction(String id) {
        return null;
    }
}