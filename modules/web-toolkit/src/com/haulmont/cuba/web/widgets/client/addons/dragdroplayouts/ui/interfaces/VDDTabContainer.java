/*
 * Copyright 2015 John Ahlroos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces;

import com.google.gwt.user.client.ui.Widget;

/**
 * An interface for layouts which has separated tab and content from each other
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.3
 */
public interface VDDTabContainer {

    /**
     * Get the position of a tabs content
     * 
     * @param w
     *            The tabs content
     */
    int getTabContentPosition(Widget w);

    /**
     * Returns the position of a tab
     * 
     * @param tab
     *            The tab in the tabbar
     * @return
     */
    public int getTabPosition(Widget tab);

}
