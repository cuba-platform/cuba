package com.haulmont.cuba.web;

import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.web.gui.GenericEditorWindow;
import com.haulmont.cuba.web.gui.GenericBrowserWindow;
import com.haulmont.cuba.web.gui.GenericLookupWindow;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;

public class WindowConfig extends com.haulmont.cuba.gui.config.WindowConfig {
    @Override
    public WindowInfo getWindowInfo(String id) {
        WindowInfo windowInfo = screens.get(id);
        if (windowInfo == null) {
            WebConfig config = ConfigProvider.getConfig(WebConfig.class);
            if (!config.getEnableGenericScreens())
                throw new NoSuchScreenException(id);

            if (id.endsWith(".edit")) {
                return getGenericWindowInfo(id, ".edit", GenericEditorWindow.class.getName());
            } else if (id.endsWith(".browse")) {
                return getGenericWindowInfo(id, ".browse", GenericBrowserWindow.class.getName());
            } else if (id.endsWith(".lookup")) {
                return getGenericWindowInfo(id, ".lookup", GenericLookupWindow.class.getName());
            } else {
                throw new NoSuchScreenException(id);
            }
        }
        return windowInfo;
    }

    private WindowInfo getGenericWindowInfo(String id, String actionName, String windowClass) {
        final String metaClass = id.substring(0, id.length() - actionName.length());

        if (MetadataProvider.getSession().getClass(metaClass) != null) {
            final Element element = new DOMElement("screen");
            element.addAttribute("id", id);
            element.addAttribute("class", windowClass);

            final Element paramElement = element.addElement("params").addElement("param");
            paramElement.addAttribute("name", "metaClass");
            paramElement.addAttribute("value", metaClass);

            return new WindowInfo(id, element);
        } else {
            throw new NoSuchScreenException(id);
        }
    }
}
