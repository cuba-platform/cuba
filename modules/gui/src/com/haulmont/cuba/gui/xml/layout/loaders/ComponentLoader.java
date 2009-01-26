/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 10:16:44
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class ComponentLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {
    protected Locale locale;
    protected ResourceBundle resourceBundle;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    protected void loadId(Component component, Element element) {
        final String id = element.attributeValue("id");
        component.setId(id);
    }

    protected void loadCaption(Component.HasCaption component, Element element) {
        final String caption = element.attributeValue("caption");
        component.setCaption(caption);
    }

    protected void loadAlign(Component component, Element element) {
        final String align = element.attributeValue("align");
        if (!StringUtils.isBlank(align)) {
            component.setVerticalAlIlignment(loadAlignment(align, true));
        }
    }

    protected void loadHeight(Component component, Element element) {
        final String height = element.attributeValue("height");
        if (!StringUtils.isBlank(height)) {
            component.setHeight(height);
        }
    }

    protected void loadWidth(Component component, Element element) {
        final String width = element.attributeValue("width");
        if (!StringUtils.isBlank(width)) {
            component.setWidth(width);
        }
    }

    private int loadAlignment(String align, boolean horizontal) {
        if (!horizontal) {
            if ("start".equals(align)) {
                return Component.AlignInfo.ALIGNMENT_TOP;
            } else if ("center".equals(align)) {
                return Component.AlignInfo.ALIGNMENT_VERTICAL_CENTER;
            } else if ("end".equals(align)) {
                return Component.AlignInfo.ALIGNMENT_BOTTOM;
            } else {
                return Component.AlignInfo.ALIGNMENT_TOP;
            }
        } else {
            if ("start".equals(align)) {
                return Component.AlignInfo.ALIGNMENT_LEFT;
            } else if ("center".equals(align)) {
                return Component.AlignInfo.ALIGNMENT_HORIZONTAL_CENTER;
            } else if ("end".equals(align)) {
                return Component.AlignInfo.ALIGNMENT_RIGHT;
            } else {
                return Component.AlignInfo.ALIGNMENT_LEFT;
            }
        }
    }

    protected void loadPack(Component component, Element element) {
        final String align = element.attributeValue("pack");
        if (!StringUtils.isBlank(align)) {
            component.setVerticalAlIlignment(loadAlignment(align, false));
        }
    }
}
