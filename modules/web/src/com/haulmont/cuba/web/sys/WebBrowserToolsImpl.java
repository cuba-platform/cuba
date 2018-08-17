/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.gui.WebBrowserTools;
import com.haulmont.cuba.web.AppUI;
import com.vaadin.shared.ui.BorderStyle;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Map;

@Scope(UIScope.NAME)
@Component(WebBrowserTools.NAME)
public class WebBrowserToolsImpl implements WebBrowserTools {

    protected AppUI ui;

    public WebBrowserToolsImpl(AppUI ui) {
        this.ui = ui;
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        String target = null;
        Integer width = null;
        Integer height = null;
        String border = "DEFAULT";
        Boolean tryToOpenAsPopup = null;
        if (params != null) {
            target = (String) params.get("target");
            width = (Integer) params.get("width");
            height = (Integer) params.get("height");
            border = (String) params.get("border");
            tryToOpenAsPopup = (Boolean) params.get("tryToOpenAsPopup");
        }
        if (target == null) {
            target = "_blank";
        }
        if (width != null && height != null && border != null) {
            ui.getPage().open(url, target, width, height, BorderStyle.valueOf(border));
        } else if (tryToOpenAsPopup != null) {
            ui.getPage().open(url, target, tryToOpenAsPopup);
        } else {
            ui.getPage().open(url, target, false);
        }
    }
}