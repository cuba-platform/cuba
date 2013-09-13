/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.SearchField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.ui.CubaSearchSelect;
import com.vaadin.data.Property;
import com.vaadin.server.ErrorMessage;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebSearchField extends WebLookupField implements SearchField {

    protected int minSearchStringLength = 0;

    protected Messages messages;

    protected IFrame.NotificationType defaultNotificationType = IFrame.NotificationType.TRAY;

    protected SearchNotifications searchNotifications = new SearchNotifications() {
        @Override
        public void notFoundSuggestions(String filterString) {
            String message = messages.formatMessage("com.haulmont.cuba.web", "searchSelect.notFound", filterString);
            App.getInstance().getWindowManager().showNotification(message, defaultNotificationType);
        }

        @Override
        public void needMinSearchStringLength(String filterString, int minSearchStringLength) {
            String message = messages.formatMessage(
                    "com.haulmont.cuba.web", "searchSelect.minimumLengthOfFilter", minSearchStringLength);
            App.getInstance().getWindowManager().showNotification(message, defaultNotificationType);
        }
    };

    public WebSearchField() {
        messages = AppBeans.get(Messages.class);
    }

    @Override
    protected void createComponent() {
        this.component = new CubaSearchSelect() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                if (newDataSource == null)
                    super.setPropertyDataSource(null);
                else
                    super.setPropertyDataSource(new LookupPropertyAdapter(newDataSource));
            }

            @Override
            public void setComponentError(ErrorMessage componentError) {
                boolean handled = false;
                if (componentErrorHandler != null)
                    handled = componentErrorHandler.handleError(componentError);

                if (!handled)
                    super.setComponentError(componentError);
            }
        };

        getSearchComponent().setFilterHandler(new CubaSearchSelect.FilterHandler() {
            @Override
            public void onFilterChange(String newFilter) {
                if (StringUtils.length(newFilter) >= minSearchStringLength) {
                    optionsDatasource.refresh(Collections.singletonMap(SEARCH_STRING_PARAM, (Object) newFilter));
                    if (optionsDatasource.getState() == Datasource.State.VALID && optionsDatasource.size() == 1) {
                        setValue(optionsDatasource.getItems().iterator().next());
                    }

                    if (searchNotifications != null) {
                        if (optionsDatasource.getState() == Datasource.State.VALID && optionsDatasource.size() == 0)
                            searchNotifications.notFoundSuggestions(newFilter);
                    }
                } else {
                    if (optionsDatasource.getState() == Datasource.State.VALID) {
                        optionsDatasource.clear();
                        if (!isRequired())
                            setValue(null);
                    }

                    if (searchNotifications != null && StringUtils.length(newFilter) > 0)
                        searchNotifications.needMinSearchStringLength(newFilter, minSearchStringLength);
                }
            }
        });
    }

    private CubaSearchSelect getSearchComponent() {
        return (CubaSearchSelect) component;
    }

    @Override
    public void setMinSearchStringLength(int searchStringLength) {
        this.minSearchStringLength = searchStringLength;
    }

    @Override
    public int getMinSearchStringLength() {
        return minSearchStringLength;
    }

    @Override
    public void setSearchNotifications(SearchNotifications searchNotifications) {
        this.searchNotifications = searchNotifications;
    }

    @Override
    public SearchNotifications getSearchNotifications() {
        return searchNotifications;
    }

    @Override
    public IFrame.NotificationType getDefaultNotificationType() {
        return defaultNotificationType;
    }

    @Override
    public void setDefaultNotificationType(IFrame.NotificationType defaultNotificationType) {
        this.defaultNotificationType = defaultNotificationType;
    }
}