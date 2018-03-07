/*
 * Copyright (c) 2008-2017 Haulmont.
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

import com.vaadin.v7.contextmenu.GridContextMenu;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.shared.ui.grid.GridConstants;
import com.vaadin.v7.ui.Grid;

import static com.vaadin.event.ContextClickEvent.ContextClickNotifier;
import static com.vaadin.ui.Grid.*;

public class CubaGridContextMenu extends GridContextMenu {

    protected ContextClickListener contextClickListener;

    protected boolean enabled = true;

    public CubaGridContextMenu(Grid parentComponent) {
        super(parentComponent);
    }

    @Override
    public void setAsContextMenuOf(ContextClickNotifier component) {
        if (contextClickListener == null) {
            initContextClickListener();
        }
        component.addContextClickListener(contextClickListener);
    }

    protected void initContextClickListener() {
        contextClickListener = (ContextClickListener) event -> {
            if (!isEnabled()) {
                return;
            }

            fireEvent(new ContextMenuOpenListener.ContextMenuOpenEvent(CubaGridContextMenu.this, event));

            // prevent opening context menu in non BODY sections
            if (event instanceof GridContextClickEvent) {
                GridContextClickEvent gridEvent = (GridContextClickEvent) event;
                if (!gridEvent.getSection().equals(GridConstants.Section.BODY)) {
                    return;
                }
            }

            open(event.getClientX(), event.getClientY());
        };
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
