/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Embedded;
import org.apache.commons.lang.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author gorodnov
 * @version $Id$
 */
public class EmbeddedLoader extends AbstractComponentLoader<Embedded> {

    protected static final String URL_PREFIX = "url://";

    protected static final String FILE_PREFIX = "file://";

    protected static final String THEME_PREFIX = "theme://";

    @Override
    public void createComponent() {
        resultComponent = (Embedded) factory.createComponent(Embedded.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        String typeAttribute = element.attributeValue("type");
        if (StringUtils.isNotEmpty(typeAttribute)) {
            Embedded.Type type = Embedded.Type.valueOf(typeAttribute);
            if (type != null) {
                resultComponent.setType(type);
            }
        }

        String srcAttr = element.attributeValue("src");
        if (srcAttr != null) {
            if (srcAttr.startsWith(URL_PREFIX)) {
                try {
                    String src = srcAttr.substring(URL_PREFIX.length());
                    resultComponent.setType(Embedded.Type.BROWSER);
                    resultComponent.setSource(new URL(src));
                } catch (MalformedURLException e) {
                    throw new GuiDevelopmentException("Unable to instantiate component", context.getFullFrameId(),
                            "src", srcAttr);
                }
            } if (srcAttr.startsWith(THEME_PREFIX)) {
                resultComponent.setSource(srcAttr);
            } else if (srcAttr.startsWith(FILE_PREFIX)) {
                String src = srcAttr.substring(FILE_PREFIX.length());
                resultComponent.setType(Embedded.Type.OBJECT);
                resultComponent.setSource(src);
            } else {
                throw new GuiDevelopmentException("Illegal src attribute value. 'url://' or 'file://' or theme:// prefix expected",
                        context.getFullFrameId(), "src", srcAttr);
            }
        }

        loadVisible(resultComponent, element);
        loadStyleName(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
        loadAlign(resultComponent, element);
    }
}