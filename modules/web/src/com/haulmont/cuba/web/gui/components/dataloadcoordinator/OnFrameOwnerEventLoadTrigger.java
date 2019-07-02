/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.gui.components.dataloadcoordinator;

import com.haulmont.cuba.gui.components.DataLoadCoordinator;
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.sys.UiControllerReflectionInspector;

import java.lang.invoke.MethodHandle;
import java.util.function.Consumer;

public class OnFrameOwnerEventLoadTrigger implements DataLoadCoordinator.Trigger {

    private final DataLoader loader;

    public OnFrameOwnerEventLoadTrigger(FrameOwner frameOwner, UiControllerReflectionInspector reflectionInspector,
                                        DataLoader loader, Class eventClass) {
        this.loader = loader;
        MethodHandle addListenerMethod = reflectionInspector.getAddListenerMethod(frameOwner.getClass(), eventClass);
        if (addListenerMethod == null) {
            throw new IllegalStateException("Cannot find addListener method for " + eventClass);
        }
        try {
            addListenerMethod.invoke(frameOwner, (Consumer) event -> load());
        } catch (Error e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException("Unable to add listener for " + eventClass, e);
        }
    }

    private void load() {
        loader.load();
    }

    @Override
    public DataLoader getLoader() {
        return loader;
    }
}
