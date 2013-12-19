/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface TokenList extends Field, Component.BelongToFrame,
        Component.HasCaption, Component.Editable {

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

    void setLookupScreenParams(Map<String, Object> params);
    @Nullable
    Map<String, Object> getLookupScreenParams();

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

    ItemClickListener getItemClickListener();

    void setItemClickListener(ItemClickListener itemClickListener);

    void setTokenStyleGenerator(TokenStyleGenerator tokenStyleGenerator);
    TokenStyleGenerator getTokenStyleGenerator();

    public interface TokenStyleGenerator {
        String getStyle(Object itemId);
    }

    interface ItemChangeHandler {
        void addItem(Object item);
        void removeItem(Object item);
    }

    interface ItemClickListener {
        void onClick(Object item);
    }

    enum Position {
        TOP, BOTTOM
    }
}