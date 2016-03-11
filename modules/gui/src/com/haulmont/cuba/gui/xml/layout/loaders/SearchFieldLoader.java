/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.SearchField;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author artamonov
 * @version $Id$
 */
public class SearchFieldLoader extends LookupFieldLoader {
    @Override
    public void createComponent() {
        resultComponent = (SearchField) factory.createComponent(SearchField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        SearchField searchField = (SearchField) resultComponent;

        String minSearchStringLength = element.attributeValue("minSearchStringLength");
        if (StringUtils.isNotEmpty(minSearchStringLength)) {
            searchField.setMinSearchStringLength(Integer.parseInt(minSearchStringLength));
        }

        String modeString = element.attributeValue("mode");
        if (StringUtils.isNotEmpty(modeString)) {
            SearchField.Mode mode;
            try {
                mode = SearchField.Mode.valueOf(StringUtils.upperCase(modeString));
            } catch (IllegalArgumentException e) {
                throw new GuiDevelopmentException("Unable to parse mode for search",
                        context.getFullFrameId(), "mode", modeString);
            }
            searchField.setMode(mode);
        }

        String escapeValueForLike = element.attributeValue("escapeValueForLike");
        if (StringUtils.isNotEmpty(escapeValueForLike)) {
            searchField.setEscapeValueForLike(Boolean.parseBoolean(escapeValueForLike));
        }
    }

    @Override
    protected void loadTextInputAllowed() {
        // do nothing
    }
}