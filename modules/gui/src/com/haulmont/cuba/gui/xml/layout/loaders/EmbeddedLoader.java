package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Embedded;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.dom4j.Element;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: Nikolay Gorodnov
 * Date: 22.06.2009
 */
public class EmbeddedLoader extends ComponentLoader {

    private final String URL_PREFIX = "url";

    private final String FILE_PREFIX = "file";

    public EmbeddedLoader(Context context) {
        super(context);
    }

    public Component loadComponent(
            ComponentsFactory factory,
            Element element,
            Component parent) throws InstantiationException, IllegalAccessException {
        Embedded component = factory.createComponent("embedded");

        String srcAttr = element.attributeValue("src");
        if (srcAttr != null) {
            if (srcAttr.startsWith(URL_PREFIX + "://")) {
                try {
                    String src = srcAttr.substring(srcAttr.indexOf("//") + 2);
                    component.setType(Embedded.Type.BROWSER);
                    component.setSource(new URL(src));
                } catch (MalformedURLException e) {
                    throw new InstantiationException("Unable to instantiate component");
                }
            } else if (srcAttr.startsWith(FILE_PREFIX + "://")) {
                String src = srcAttr.substring(srcAttr.indexOf("//") + 2);
                component.setType(Embedded.Type.OBJECT);
                component.setSource(src);
            } else {
                throw new InstantiationException("Illegal src attribute value. Expect 'url:' or 'file:' prefix");
            }
        }

        loadId(component, element);
        loadVisible(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        return component;
    }
}
