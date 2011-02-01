/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 06.03.2009 15:33:44
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Map;

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
