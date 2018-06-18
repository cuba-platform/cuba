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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui;

/**
 * LayoutDragMode specifies how dragging is visualized.
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 6.5
 */
public enum LayoutDragMode {

    /**
     * Disables dragging components from a layout. This is the default
     * behaviour.
     */
    NONE,

    /**
     * Makes a copy of the component which is shown when dragging.
     */
    CLONE,

    /**
     * The same as {@link LayoutDragMode#CLONE} except that components can only
     * be dragged from their captions.
     */
    CAPTION,

    /**
     * The same as {@link LayoutDragMode#CLONE} except that the dragged
     * component is not shown, but instead another component defined by
     */
    CLONE_OTHER

    ;
}
