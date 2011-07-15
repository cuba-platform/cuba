/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 19.07.2010 18:57:44
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Map;

public interface TokenList extends Component, Component.BelongToFrame,
        Component.Expandable, Component.HasCaption, Component.Editable {

    String NAME = "tokenList";

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    CollectionDatasource getDatasource();
    void setDatasource(CollectionDatasource datasource);

    LookupField.FilterMode getFilterMode();
    void setFilterMode(LookupField.FilterMode mode);

    String getOptionsCaptionProperty();
    void setOptionsCaptionProperty(String captionProperty);

    CaptionMode getOptionsCaptionMode();
    void setOptionsCaptionMode(CaptionMode captionMode);

    CollectionDatasource getOptionsDatasource();
    void setOptionsDatasource(CollectionDatasource datasource);

    java.util.List getOptionsList();
    void setOptionsList(java.util.List optionsList);

    Map<String, Object> getOptionsMap();
    void setOptionsMap(Map<String, Object> map);

    boolean isLookup();
    void setLookup(boolean lookup);

    String getLookupScreen();
    void setLookupScreen(String lookupScreen);

    boolean isMultiSelect();
    void setMultiSelect(boolean multiselect);

    boolean isSimple();
    void setSimple(boolean simple);

    Position getPosition();
    void setPosition(Position position);

    WindowManager.OpenType getLookupOpenMode();
    void setLookupOpenMode(WindowManager.OpenType lookupOpenMode);

    boolean isInline();
    void setInline(boolean inline);

    String getAddButtonCaption();
    void setAddButtonCaption(String caption);

    String getAddButtonIcon();
    void setAddButtonIcon(String icon);

    ItemChangeHandler getItemChangeHandler();
    void setItemChangeHandler(ItemChangeHandler handler);

    void setTokenStyleGenerator(TokenStyleGenerator tokenStyleGenerator);
    TokenStyleGenerator getTokenStyleGenerator();

    public interface TokenStyleGenerator {
        String getStyle(Object itemId);
    }

    interface ItemChangeHandler {
        void addItem(Object item);
        void removeItem(Object item);
    }

    enum Position {
        TOP, BOTTOM
    }
}
