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
package com.haulmont.cuba.web.widgets.addons.dragdroplayouts;

import com.vaadin.event.dd.DropHandler;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.Resource;
import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.DragCaptionInfo;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.DDLayoutState;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.DragAndDropAwareState;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.interfaces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DDUtil {

    public static void onBeforeClientResponse(HasComponents layout,
            DragAndDropAwareState state) {
        DDLayoutState dragAndDropState = state.getDragAndDropState();
        Iterator<Component> componentIterator = layout.iterator();

        dragAndDropState.draggable = new ArrayList<>();
        dragAndDropState.referenceImageComponents = new HashMap<>();
        dragAndDropState.nonGrabbable = new ArrayList<>();
        dragAndDropState.dragCaptions = new HashMap<>();

        if (layout instanceof AbstractClientConnector) {
            for (DragCaptionInfo dci : dragAndDropState.dragCaptions.values()) {
                if (dci.iconKey != null) {
                    ((AbstractClientConnector) layout).setConnectorResource(dci.iconKey, null);
                }
            }
        }

        KeyMapper<Resource> keyMapper = new KeyMapper<>();

        while (componentIterator.hasNext()) {
            Component c = componentIterator.next();

            if (layout instanceof DragFilterSupport
                    && ((DragFilterSupport) layout).getDragFilter()
                            .isDraggable(c)) {
                dragAndDropState.draggable.add(c);
            }

            if (layout instanceof DragGrabFilterSupport) {
                DragGrabFilter dragGrabFilter = ((DragGrabFilterSupport) layout).getDragGrabFilter();
                if (dragGrabFilter != null) {
                    addNonGrabbedComponents(dragAndDropState.nonGrabbable, c, dragGrabFilter);
                }
            }

            if (layout instanceof HasDragCaptionProvider) {
                DragCaptionProvider dragCaptionProvider = ((HasDragCaptionProvider) layout)
                        .getDragCaptionProvider();

                if (dragCaptionProvider != null) {
                    DragCaption dragCaption = dragCaptionProvider.getDragCaption(c);

                    if (dragCaption != null) {
                        String dragIconKey = null;
                        if (dragCaption.getIcon() != null
                                && layout instanceof AbstractClientConnector) {
                            dragIconKey = keyMapper.key(dragCaption.getIcon());
                            ((AbstractClientConnector) layout).setConnectorResource(dragIconKey, dragCaption.getIcon());
                        }

                        DragCaptionInfo dci = new DragCaptionInfo();
                        dci.caption = dragCaption.getCaption();
                        dci.contentMode = dragCaption.getContentMode();
                        dci.iconKey = dragIconKey;

                        dragAndDropState.dragCaptions.put(c, dci);
                    }
                }
            }

            if (layout instanceof DragImageReferenceSupport) {
                DragImageProvider provider = ((DragImageReferenceSupport) layout)
                        .getDragImageProvider();
                if (provider != null) {
                    Component dragImage = provider.getDragImage(c);
                    if (dragImage != null) {
                        dragAndDropState.referenceImageComponents.put(c,
                                dragImage);
                    }
                }
            }
        }
    }

    private static void addNonGrabbedComponents(List<Connector> nonGrabbable, Component component,
                                                DragGrabFilter dragGrabFilter) {
        if (!dragGrabFilter.canBeGrabbed(component)) {
            nonGrabbable.add(component);
        } else if (component instanceof HasComponents
                && !(component instanceof LayoutDragSource)) {
            for (Component child : ((HasComponents) component)) {
                addNonGrabbedComponents(nonGrabbable, child, dragGrabFilter);
            }
        }
    }

    public static void verifyHandlerType(HasComponents layout,
            DropHandler handler) {
        if (handler instanceof AbstractDefaultLayoutDropHandler) {
            AbstractDefaultLayoutDropHandler dropHandler = (AbstractDefaultLayoutDropHandler) handler;
            if (!dropHandler.getTargetLayoutType()
                    .isAssignableFrom(layout.getClass())) {
                throw new IllegalArgumentException("Cannot add a handler for "
                        + dropHandler.getTargetLayoutType() + " to a "
                        + layout.getClass());
            }
        }
    }
}