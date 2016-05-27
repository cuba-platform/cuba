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

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.ResizeListener;

@Deprecated
public class ResizeListenerWrapper implements ResizableTextArea.ResizeListener {

    private final ResizeListener listener;

    public ResizeListenerWrapper(ResizeListener listener) {
        this.listener = listener;
    }

    @Override
    public void sizeChanged(ResizableTextArea.ResizeEvent e) {
        listener.onResize(e.getComponent(), e.getPrevWidth(), e.getPrevHeight(), e.getWidth(), e.getHeight());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        ResizeListenerWrapper that = (ResizeListenerWrapper) obj;

        return this.listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }
}