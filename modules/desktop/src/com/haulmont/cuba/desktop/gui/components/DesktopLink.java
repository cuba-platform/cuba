/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.gui.components.Link;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXHyperlink;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collections;

public class DesktopLink extends DesktopAbstractComponent<JXHyperlink> implements Link {

    protected String url;
    protected String target;
    protected String icon;

    public DesktopLink() {
        impl = new JXHyperlink();
        impl.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String targetUrl = DesktopLink.this.url;
                if (StringUtils.isNotEmpty(targetUrl)) {
                    DesktopWindowManager wm = App.getInstance().getMainFrame().getWindowManager();
                    wm.showWebPage(targetUrl, Collections.<String, Object>emptyMap());
                }
            }
        });
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
        if (icon != null)
            impl.setIcon(App.getInstance().getResources().getIcon(icon));
        else
            impl.setIcon(null);
    }

    @Override
    public String getCaption() {
        return impl.getText();
    }

    @Override
    public void setCaption(String caption) {
        impl.setText(caption);
    }

    @Override
    public String getDescription() {
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.setToolTipText(description);
    }
}