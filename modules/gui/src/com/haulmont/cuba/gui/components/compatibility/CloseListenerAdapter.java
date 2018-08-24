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

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.Window;

@Deprecated
public class CloseListenerAdapter implements Window.CloseListener {

    protected Window.CloseWithCommitListener closeWithCommitListener;

    public CloseListenerAdapter(Window.CloseWithCommitListener closeWithCommitListener) {
        this.closeWithCommitListener = closeWithCommitListener;
    }

    @Override
    public int hashCode() {
        return closeWithCommitListener.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CloseListenerAdapter wrapper = (CloseListenerAdapter) obj;

        return this.closeWithCommitListener.equals(wrapper.closeWithCommitListener);
    }

    @Override
    public void windowClosed(String actionId) {
        if (Window.COMMIT_ACTION_ID.equals(actionId)) {
            closeWithCommitListener.windowClosedWithCommitAction();
        }
    }
}