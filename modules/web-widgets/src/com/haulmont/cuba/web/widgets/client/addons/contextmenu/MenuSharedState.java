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
package com.haulmont.cuba.web.widgets.client.addons.contextmenu;

import java.io.Serializable;
import java.util.List;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.communication.URLReference;

// FIXME: TabIndex? MenuBar's state needs to extend a TabIndex state, ContextMenu's state doesn't need it, what's the solution?
@SuppressWarnings("serial")
public class MenuSharedState extends AbstractComponentState {
    // public class MenuBarState extends TabIndexState {
    // {
    // primaryStyleName = "v-menubar";
    // }
    // }

    public List<MenuItemState> menuItems;
    public boolean htmlContentAllowed;

    public static class MenuItemState implements Serializable {
        public int id;
        public boolean separator;
        public String text;
        public boolean command;
        public URLReference icon;
        public boolean enabled;
        public String description;
        public boolean checkable;
        public boolean checked;
        public List<MenuItemState> childItems;
        public String styleName;
    }
}
