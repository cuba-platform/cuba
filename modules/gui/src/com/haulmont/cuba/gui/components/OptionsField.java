/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public interface OptionsField extends Field {
    boolean isMultiSelect();
    void setMultiSelect(boolean multiselect);

    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    String getDescriptionProperty();
    void setDescriptionProperty(String descProperty);

    CollectionDatasource getOptionsDatasource();
    void setOptionsDatasource(CollectionDatasource datasource);
    
    java.util.List getOptionsList();
    void setOptionsList(java.util.List optionsList);

    Map<String, Object> getOptionsMap();
    void setOptionsMap(Map<String, Object> map);
}
