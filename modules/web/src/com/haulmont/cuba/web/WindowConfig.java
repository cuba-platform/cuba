package com.haulmont.cuba.web;

import com.haulmont.cuba.gui.config.ScreenConfig;
import com.haulmont.cuba.gui.config.ScreenInfo;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.web.gui.GenericEditorWindow;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;

class WindowConfig extends ScreenConfig {
    @Override
    public ScreenInfo getScreenInfo(String id) {
        ScreenInfo screenInfo = screens.get(id);
        if (screenInfo == null) {
            if (id.endsWith(".edit")) {
                final String metaClass = id.substring(0, id.length() - ".edit".length());
                if (MetadataProvider.getSession().getClass(metaClass) != null) {
                    final Element element = new DOMElement("screen");
                    element.addAttribute("id", id);
                    element.addAttribute("class", GenericEditorWindow.class.getName());

                    final Element paramElement = element.addElement("params").addElement("param");
                    paramElement.addAttribute("name", "metaClass");
                    paramElement.addAttribute("value", metaClass);

                    return new ScreenInfo(id, element);
                }
            }
            throw new IllegalStateException("Screen '" + id + "' is not defined");
        }
        return screenInfo;
    }
}
