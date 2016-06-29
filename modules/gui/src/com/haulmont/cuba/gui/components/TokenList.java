/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public interface TokenList extends Field, Component.BelongToFrame, Component.HasCaption, Component.Editable {

    String NAME = "tokenList";

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    @Override
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

    /**
     * @deprecated Use {@link #setLookupOpenMode(WindowManager.OpenType)}
     */
    @Deprecated
    void setLookupScreenDialogParams(DialogParams dialogparams);
    @Deprecated
    @Nullable
    DialogParams getLookupScreenDialogParams();

    boolean isClearEnabled();
    void setClearEnabled(boolean clearEnabled);

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

    String getClearButtonCaption();
    void setClearButtonCaption(String caption);

    String getClearButtonIcon();
    void setClearButtonIcon(String icon);

    ItemChangeHandler getItemChangeHandler();
    void setItemChangeHandler(ItemChangeHandler handler);

    ItemClickListener getItemClickListener();

    void setItemClickListener(ItemClickListener itemClickListener);

    AfterLookupCloseHandler getAfterLookupCloseHandler();
    void setAfterLookupCloseHandler(AfterLookupCloseHandler handler);

    AfterLookupSelectionHandler getAfterLookupSelectionHandler();
    void setAfterLookupSelectionHandler(AfterLookupSelectionHandler handler);

    void setTokenStyleGenerator(TokenStyleGenerator tokenStyleGenerator);
    TokenStyleGenerator getTokenStyleGenerator();

    interface TokenStyleGenerator {
        String getStyle(Object itemId);
    }

    interface ItemChangeHandler {
        void addItem(Object item);
        void removeItem(Object item);
    }

    interface ItemClickListener {
        void onClick(Object item);
    }

    interface AfterLookupCloseHandler {
        void onClose(Window window, String actionId);
    }

    interface AfterLookupSelectionHandler {
        void onSelect(Collection items);
    }

    enum Position {
        TOP, BOTTOM
    }
}