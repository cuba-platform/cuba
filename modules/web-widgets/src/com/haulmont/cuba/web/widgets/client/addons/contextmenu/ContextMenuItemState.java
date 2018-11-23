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

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.ContentMode;

import java.io.Serializable;
import java.util.List;

public class ContextMenuItemState implements Serializable {
    public int id;
    public boolean separator;
    public String text;
    public boolean command;
    public URLReference icon;
    public boolean enabled;
    @NoLayout
    public String description;
    @NoLayout
    public ContentMode descriptionContentMode = ContentMode.PREFORMATTED;
    public boolean checkable;
    public boolean checked;
    public List<ContextMenuItemState> childItems;
    public String styleName;

}
