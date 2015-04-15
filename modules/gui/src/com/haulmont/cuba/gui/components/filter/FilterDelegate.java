/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Encapsulates common generic filter behaviour. All filter components delegates their method invocations
 * to implementation of this interface.
 *
 * @author gorbunkov
 * @version $Id$
 */
public interface FilterDelegate {
    String NAME = "cuba_FilterDelegate";

    void setFilter(Filter filter);

    void loadFiltersAndApplyDefault();

    void setFilterEntity(FilterEntity filterEntity);

    Component.Container getLayout();

    void setDatasource(CollectionDatasource datasource);

    CollectionDatasource getDatasource();

    boolean apply(boolean isNewWindow);

    String getCaption();

    void setCaption(String caption);

    void setUseMaxResults(boolean useMaxResults);

    boolean getUseMaxResults();

    void setManualApplyRequired(Boolean manualApplyRequired);

    Boolean getManualApplyRequired();

    <T extends Component> T getOwnComponent(String id);

    @Nullable
    <T extends Component> T getComponent(String id);

    boolean saveSettings(Element element);

    Component getApplyTo();

    void setApplyTo(Component applyTo);

    void setFolderActionsEnabled(boolean folderActionsEnabled);

    boolean isFolderActionsEnabled();

    void setEditable(boolean editable);

    boolean isEditable();

    Object getParamValue(String paramName);

    void setParamValue(String paramName, Object value);

    void addFilterEntityChangeListener(Filter.FilterEntityChangeListener listener);

    List<Filter.FilterEntityChangeListener> getFilterEntityChangeListeners();

    Integer getColumnsCount();

    void setColumnsCount(int columnsCount);

    void applySettings(Element element);
}
