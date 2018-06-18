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
 * Contains all string constants used by DragDropLayouts
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.4
 */
public interface Constants {

    // Drop details
    public static final String DROP_DETAIL_TO = "to";
    public static final String DROP_DETAIL_ROW = "row";
    public static final String DROP_DETAIL_COLUMN = "column";
    public static final String DROP_DETAIL_VERTICAL_DROP_LOCATION = "vdetail";
    public static final String DROP_DETAIL_HORIZONTAL_DROP_LOCATION = "hdetail";
    public static final String DROP_DETAIL_EMPTY_CELL = "overEmpty";
    public static final String DROP_DETAIL_MOUSE_EVENT = "mouseEvent";
    public static final String DROP_DETAIL_OVER_CLASS = "overClass";
    public static final String DROP_DETAIL_COMPONENT_HEIGHT = "compHeight";
    public static final String DROP_DETAIL_COMPONENT_WIDTH = "compWidth";
    public static final String DROP_DETAIL_ABSOLUTE_LEFT = "absoluteLeft";
    public static final String DROP_DETAIL_ABSOLUTE_TOP = "absoluteTop";
    public static final String DROP_DETAIL_RELATIVE_LEFT = "relativeLeft";
    public static final String DROP_DETAIL_RELATIVE_TOP = "relativeTop";

    // Transferable details
    public static final String TRANSFERABLE_DETAIL_COMPONENT = "component";
    public static final String TRANSFERABLE_DETAIL_INDEX = "index";
    public static final String TRANSFERABLE_DETAIL_MOUSEDOWN = "mouseDown";
    public static final String TRANSFERABLE_DETAIL_CAPTION = "caption";

    // Attributes
    public static final String ATTRIBUTE_HORIZONTAL_DROP_RATIO = "hDropRatio";
    public static final String ATTRIBUTE_VERTICAL_DROP_RATIO = "vDropRatio";
    public static final String DRAGMODE_ATTRIBUTE = "dragMode";
}
