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

import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;

/**
 * Layouts which supports drag filters should implement this
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.7.2
 */
public interface VHasDragFilter {

    /**
     * Returns the drag filter used by the layout
     * 
     * @return
     */
    VDragFilter getDragFilter();

    void setDragFilter(VDragFilter filter);
}
