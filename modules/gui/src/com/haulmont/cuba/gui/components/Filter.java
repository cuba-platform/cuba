/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.FilterEntity;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface Filter
        extends Component.Container, Component.Margin, Component.BelongToFrame,
        Component.HasXmlDescriptor, Component.HasSettings, Component.HasCaption {

    String NAME = "filter";

    CollectionDatasource getDatasource();
    void setDatasource(CollectionDatasource datasource);

    void setFilterEntity(FilterEntity filterEntity);

    boolean apply(boolean isNewWindow);

    /**
     * Low-level method, don't invoke from application code
     */
    void loadFiltersAndApplyDefault();

    /**
     * Whether to show field for rows count restriction.
     * <p>Automatically set to false for {@code HierarchicalDatasource}.
     */
    void setUseMaxResults(boolean useMaxResults);
    boolean getUseMaxResults();

    void setApplyTo(Component component);
    Component getApplyTo();
    
    void setManualApplyRequired(Boolean manualApplyRequired);
    Boolean getManualApplyRequired();

    void setEditable(boolean editable);
    boolean isEditable();

    void setRequired(boolean required);
    boolean isRequired();

    void setFolderActionsEnabled(boolean enabled);
    boolean isFolderActionsEnabled();
}