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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VDDAbstractDropHandler;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.VDDHasDropHandler;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.ui.dd.VDragAndDropManager;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.client.ui.dd.VTransferable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides HTML5 drops for any connector
 * 
 * @author John Ahlroos / www.jasoft.fi
 */
public class HTML5Support {

    protected static DragOverHandler globalDragOverHandler = null;
    protected static DropHandler globalDropHandler = null;
    protected static DragEnterHandler globalDragEnterHandler = null;

    private final List<HandlerRegistration> handlers = new ArrayList<>();

    public static class HTML5DragHandler
            implements DragEnterHandler, DragOverHandler, DropHandler {

        private VDragEvent vaadinDragEvent;

        private ComponentConnector connector;

        private VDDAbstractDropHandler<? extends Widget> dropHandler;

        public HTML5DragHandler(ComponentConnector connector,
                VDDAbstractDropHandler<? extends Widget> handler) {
            this.connector = connector;
            this.dropHandler = handler;
        }

        @Override
        public void onDrop(DropEvent event) {
            NativeEvent nativeEvent = event.getNativeEvent();
            if (validate(nativeEvent) && vaadinDragEvent != null) {
                nativeEvent.preventDefault();
                nativeEvent.stopPropagation();

                // event stopped, just notify global handler
                // Haulmont API
                if (globalDropHandler != null) {
                    globalDropHandler.onDrop(event);
                }

                vaadinDragEvent.setCurrentGwtEvent(nativeEvent);
                VDragAndDropManager.get().setCurrentDropHandler(dropHandler);

                // FIXME only text currently supported
                String data;
                if (BrowserInfo.get().isIE()) {
                    // IE does not support MIME types
                    // http://www.developerfusion.com/article/144828/the-html5-drag-and-drop-api/
                    data = event.getData("text");
                } else {
                    data = event.getData("text/plain");
                }

                vaadinDragEvent.getTransferable().setData("html5Data", data);

                VDragAndDropManager.get().endDrag();
                vaadinDragEvent = null;
            }
        }

        @Override
        public void onDragOver(DragOverEvent event) {
            NativeEvent nativeEvent = event.getNativeEvent();
            if (validate(nativeEvent) && vaadinDragEvent != null) {
                nativeEvent.preventDefault();
                nativeEvent.stopPropagation();

                // event stopped, just notify global handler
                // Haulmont API
                if (globalDragOverHandler != null) {
                    globalDragOverHandler.onDragOver(event);
                }

                vaadinDragEvent.setCurrentGwtEvent(nativeEvent);
                VDragAndDropManager.get().setCurrentDropHandler(dropHandler);
                dropHandler.dragOver(vaadinDragEvent);
            }
        }

        @Override
        public void onDragEnter(DragEnterEvent event) {
            NativeEvent nativeEvent = event.getNativeEvent();

            if (validate(nativeEvent)) {
                VTransferable transferable = new VTransferable();
                transferable.setDragSource(connector);

                vaadinDragEvent = VDragAndDropManager.get()
                        .startDrag(transferable, event.getNativeEvent(), false);

                vaadinDragEvent.setCurrentGwtEvent(nativeEvent);

                VDragAndDropManager.get().setCurrentDropHandler(dropHandler);

                dropHandler.dragEnter(vaadinDragEvent);

                nativeEvent.preventDefault();
                nativeEvent.stopPropagation();

            } else if (vaadinDragEvent != null
                    && Element.is(nativeEvent.getEventTarget())) {
                vaadinDragEvent.setCurrentGwtEvent(nativeEvent);
                VDragAndDropManager.get().setCurrentDropHandler(null);
                VDragAndDropManager.get().interruptDrag();
                vaadinDragEvent = null;

                nativeEvent.preventDefault();
                nativeEvent.stopPropagation();
            }

            if (globalDragEnterHandler != null) {
                globalDragEnterHandler.onDragEnter(event);
            }
        }

        private boolean validate(NativeEvent event) {
            if (!Element.is(event.getEventTarget())) {
                return false;
            }

            Element target = Element.as(event.getEventTarget());
            Widget widget = Util.findWidget(target, null);
            if (widget == null) {
                return false;
            }

            ComponentConnector connector = Util.findConnectorFor(widget);
            while (connector == null && widget != null) {
                widget = widget.getParent();
                connector = Util.findConnectorFor(widget);
            }

            if (this.connector == connector) {
                return true;
            } else if (connector == null) {
                return false;
            } else if (connector.getWidget() instanceof VDDHasDropHandler) {
                // Child connector handles its own drops
                return false;
            }

            // Over non droppable child
            return true;
        }
    }

    public static HTML5Support enable(final ComponentConnector connector,
            final VDDAbstractDropHandler<? extends Widget> handler) {
        if (handler == null) {
            return null;
        }

        Widget w = connector.getWidget();
        final HTML5Support support = GWT.create(HTML5Support.class);
        final HTML5DragHandler dragHandler = new HTML5DragHandler(connector,
                handler);

        support.handlers
                .add(w.addDomHandler(dragHandler, DragEnterEvent.getType()));
        support.handlers
                .add(w.addDomHandler(dragHandler, DragOverEvent.getType()));
        support.handlers.add(w.addDomHandler(dragHandler, DropEvent.getType()));

        return support;
    }

    private HTML5Support() {
        // Factory
    }

    public void disable() {
        for (HandlerRegistration handlerRegistration : handlers) {
            handlerRegistration.removeHandler();
        }
        handlers.clear();
    }

    // Haulmont API
    public static DragOverHandler getGlobalDragOverHandler() {
        return globalDragOverHandler;
    }

    // Haulmont API
    public static void setGlobalDragOverHandler(DragOverHandler globalDragOverHandler) {
        HTML5Support.globalDragOverHandler = globalDragOverHandler;
    }

    // Haulmont API
    public static DropHandler getGlobalDropHandler() {
        return globalDropHandler;
    }

    // Haulmont API
    public static void setGlobalDropHandler(DropHandler globalDropHandler) {
        HTML5Support.globalDropHandler = globalDropHandler;
    }

    // Haulmont API
    public static DragEnterHandler getGlobalDragEnterHandler() {
        return globalDragEnterHandler;
    }

    // Haulmont API
    public static void setGlobalDragEnterHandler(DragEnterHandler globalDragEnterHandler) {
        HTML5Support.globalDragEnterHandler = globalDragEnterHandler;
    }
}