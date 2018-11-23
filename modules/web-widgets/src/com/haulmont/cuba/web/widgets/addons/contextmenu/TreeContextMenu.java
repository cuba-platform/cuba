/*
 * Copyright 2000-2018 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.haulmont.cuba.web.widgets.addons.contextmenu;

import com.haulmont.cuba.web.widgets.addons.contextmenu.ContextMenu.ContextMenuOpenListener.ContextMenuOpenEvent;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeContextClickEvent;

@SuppressWarnings("serial")
public class TreeContextMenu<T> extends ContextMenu {

    public TreeContextMenu(Tree<T> parentComponent) {
        super(parentComponent, true);
    }

    public void addTreeContextMenuListener(final TreeContextMenuOpenListener<T> listener) {
        addContextMenuOpenListener((final ContextMenuOpenEvent event) -> {
            if (event
                    .getContextClickEvent() instanceof Tree.TreeContextClickEvent) {
                @SuppressWarnings("unchecked")
                TreeContextClickEvent<T> treeEvent = (TreeContextClickEvent<T>) event
                        .getContextClickEvent();
                listener.onContextMenuOpen(new TreeContextMenuOpenListener.TreeContextMenuOpenEvent<>(
                        TreeContextMenu.this, treeEvent ));
            }
        });
    }

    public interface TreeContextMenuOpenListener<T>
            extends java.util.EventListener, java.io.Serializable {

        public void onContextMenuOpen(TreeContextMenuOpenEvent<T> event);

        public static class TreeContextMenuOpenEvent<T>
                extends ContextMenuOpenEvent {

            private final T item;
            private final Tree<T> component;

            public TreeContextMenuOpenEvent(ContextMenu contextMenu,
                    TreeContextClickEvent<T> contextClickEvent) {
                super(contextMenu, contextClickEvent);
                item = contextClickEvent.getItem();
                component = contextClickEvent.getComponent();
            }

            public T getItem() {
                return item;
            }

            public Tree<T> getComponent() {
                return component;
            }
        }
    }
}
