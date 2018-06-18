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

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.SharedState;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.DragCaptionInfo;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.LayoutDragMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DDLayoutState extends SharedState {

    // The current drag mode, default is dragging is not supported
    public LayoutDragMode dragMode = LayoutDragMode.NONE;

    // Are the iframes shimmed
    public boolean iframeShims = true;

    // Which connectors are draggable
    public List<Connector> draggable = new ArrayList<>();

    // Which connectors cannot be used as anchor
    public List<Connector> nonGrabbable = new ArrayList<>();

    // Reference drag images
    public Map<Connector, Connector> referenceImageComponents = new HashMap<>();

    // Custom DragCaption's with icon and caption
    public Map<Connector, DragCaptionInfo> dragCaptions = new HashMap<>();

    public Map<Connector, String> dragIcons = new HashMap<>();
}