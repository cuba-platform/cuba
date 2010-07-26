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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.data.CollectionDatasource;

public interface TokenList extends Component, Component.BelongToFrame,
        Component.Expandable, Component.HasCaption {

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    String getOptionsCaptionProperty();
    void setOptionsCaptionProperty(String captionProperty);

    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    CollectionDatasource getDatasource();
    void setDatasource(CollectionDatasource datasource);

    CollectionDatasource getOptionsDatasource();
    void setOptionsDatasource(CollectionDatasource optionsDatasource);

    MetaClass getMetaClass();
    void setMetaClass(MetaClass metaClass);

    String getLookupScreen();
    void setLookupScreen(String lookupScreen);

    Position getPosition();
    void setPosition(Position position);

    Type getType();
    void setType(Type type);

    boolean isInline();
    void setInline(boolean inline);

    String getAddButtonCaption();
    void setAddButtonCaption(String caption);

    String getAddButtonIcon();
    void setAddButtonIcon(String icon);

    ItemChangeHandler getItemChangeHandler();
    void setItemChangeHandler(ItemChangeHandler handler);

    interface ItemChangeHandler {
        void addItem(Object item);
        void removeItem(Object item);
    }

    enum Position {
        TOP, BOTTOM
    }

    enum Type {
        PICKER, LOOKUP
    }
}
