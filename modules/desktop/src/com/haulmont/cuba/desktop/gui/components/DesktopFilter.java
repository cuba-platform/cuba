/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.dom4j.Element;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
// TODO DesktopFilter
public class DesktopFilter extends DesktopAbstractComponent<JPanel> implements Filter {

    private CollectionDatasource datasource;

    public DesktopFilter() {
        impl = new JPanel(new java.awt.FlowLayout());
        impl.setBorder(BorderFactory.createLineBorder(java.awt.Color.gray));
        impl.add(new JLabel("TODO: filter"));
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
    }

    public void setFilterEntity(FilterEntity filterEntity) {
    }

    public void apply(boolean isNewWindow) {
    }

    public void loadFiltersAndApplyDefault() {
    }

    public void setUseMaxResults(boolean useMaxResults) {
    }

    public boolean getUseMaxResults() {
        return false;
    }

    public void setApplyTo(Component component) {
    }

    public Component getApplyTo() {
        return null;
    }

    public void add(Component component) {
    }

    public void remove(Component component) {
    }

    public <T extends Component> T getOwnComponent(String id) {
        return null;
    }

    public <T extends Component> T getComponent(String id) {
        return null;
    }

    public Collection<Component> getOwnComponents() {
        return Collections.EMPTY_LIST;
    }

    public Collection<Component> getComponents() {
        return getOwnComponents();
    }

    public void applySettings(Element element) {
    }

    public boolean saveSettings(Element element) {
        return false;
    }
}
