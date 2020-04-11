/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.split.SplitPanelDockMode;

public interface CubaDockableSplitPanel {

    public boolean isDockable();

    public void setDockable(boolean usePinButton);

    public void setDockMode(SplitPanelDockMode dockMode);

    public SplitPanelDockMode getDockMode();

    public String getDefaultPosition();

    /**
     * Set default position for dock mode
     *
     * @param defaultPosition default position
     */
    public void setDefaultPosition(String defaultPosition);

}
