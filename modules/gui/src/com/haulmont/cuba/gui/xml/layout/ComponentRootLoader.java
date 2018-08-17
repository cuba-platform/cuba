package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.components.Component;
import org.dom4j.Element;

/**
 * JavaDoc
 */
public interface ComponentRootLoader<T extends Component> extends ComponentLoader<T> {

    void createContent(Element layoutElement);
}