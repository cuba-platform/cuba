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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.SearchField;
import com.haulmont.cuba.gui.components.data.OptionsSource;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.widgets.CComboBox;
import com.haulmont.cuba.web.widgets.CubaSearchSelect;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class WebSearchField<V> extends WebLookupField<V> implements SearchField<V> {

    protected static final String SEARCHSELECT_STYLENAME = "c-searchselect";

    protected int minSearchStringLength = 0;
    protected Mode mode = Mode.CASE_SENSITIVE;
    protected boolean escapeValueForLike = false;

    protected Frame.NotificationType defaultNotificationType = Frame.NotificationType.TRAY;

    protected SearchNotifications searchNotifications = createSearchNotifications();

    @Override
    protected CComboBox<V> createComponent() {
        return new CubaSearchSelect<>();

        /* vaadin8
        this.component = new CubaSearchSelect() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                if (newDataSource == null) {
                    super.setPropertyDataSource(null);
                } else {
                    super.setPropertyDataSource(new LookupPropertyAdapter(newDataSource));
                }
            }

            @Override
            public void setComponentError(ErrorMessage componentError) {
                boolean handled = false;
                if (componentErrorHandler != null) {
                    handled = componentErrorHandler.handleError(componentError);
                }

                if (!handled) {
                    super.setComponentError(componentError);
                }
            }
        };*/
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        initComponent(getSearchComponent());
    }

    protected void initComponent(CubaSearchSelect<V> component) {
        component.setFilterHandler(this::executeSearch);
    }

    protected SearchNotifications createSearchNotifications() {
        return new SearchNotifications() {
            protected Messages messages = AppBeans.get(Messages.NAME);

            @Override
            public void notFoundSuggestions(String filterString) {
                String message = messages.formatMessage("com.haulmont.cuba.gui", "searchSelect.notFound", filterString);
                App.getInstance().getWindowManager().showNotification(message, defaultNotificationType);
            }

            @Override
            public void needMinSearchStringLength(String filterString, int minSearchStringLength) {
                String message = messages.formatMessage(
                        "com.haulmont.cuba.gui", "searchSelect.minimumLengthOfFilter", minSearchStringLength);
                App.getInstance().getWindowManager().showNotification(message, defaultNotificationType);
            }
        };
    }

    protected void executeSearch(final String newFilter) {
//        vaadin8
        /*if (optionsDatasource == null)
            return;

        String filterForDs = newFilter;
        if (mode == Mode.LOWER_CASE) {
            filterForDs = StringUtils.lowerCase(newFilter);
        } else if (mode == Mode.UPPER_CASE) {
            filterForDs = StringUtils.upperCase(newFilter);
        }

        if (escapeValueForLike && StringUtils.isNotEmpty(filterForDs)) {
            filterForDs = QueryUtils.escapeForLike(filterForDs);
        }

        if (!isRequired() && StringUtils.isEmpty(filterForDs)) {
            setValue(null);
            if (optionsDatasource.getState() == State.VALID) {
                optionsDatasource.clear();
            }
            return;
        }

        if (StringUtils.length(filterForDs) >= minSearchStringLength) {
            optionsDatasource.refresh(Collections.singletonMap(SEARCH_STRING_PARAM, filterForDs));

            if (optionsDatasource.getState() == State.VALID) {
                if (optionsDatasource.size() == 0) {
                    if (searchNotifications != null) {
                        searchNotifications.notFoundSuggestions(newFilter);
                    }
                } else if (optionsDatasource.size() == 1) {
                    setValue(optionsDatasource.getItems().iterator().next());
                }
            }
        } else {
            if (optionsDatasource.getState() == State.VALID) {
                optionsDatasource.clear();
            }

            if (searchNotifications != null && StringUtils.length(newFilter) > 0) {
                searchNotifications.needMinSearchStringLength(newFilter, minSearchStringLength);
            }
        }*/
    }

    protected CubaSearchSelect<V> getSearchComponent() {
        return (CubaSearchSelect<V>) component;
    }

    @Override
    public void setStyleName(String styleName) {
        super.setStyleName(styleName);

        component.addStyleName(SEARCHSELECT_STYLENAME);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(SEARCHSELECT_STYLENAME, ""));
    }

    @Override
    public int getMinSearchStringLength() {
        return minSearchStringLength;
    }

    @Override
    public void setMinSearchStringLength(int searchStringLength) {
        this.minSearchStringLength = searchStringLength;
    }

    @Override
    public SearchNotifications getSearchNotifications() {
        return searchNotifications;
    }

    @Override
    public void setSearchNotifications(SearchNotifications searchNotifications) {
        this.searchNotifications = searchNotifications;
    }

    @Override
    public Frame.NotificationType getDefaultNotificationType() {
        return defaultNotificationType;
    }

    @Override
    public void setDefaultNotificationType(Frame.NotificationType defaultNotificationType) {
        this.defaultNotificationType = defaultNotificationType;
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public boolean isEscapeValueForLike() {
        return escapeValueForLike;
    }

    @Override
    public void setEscapeValueForLike(boolean escapeValueForLike) {
        this.escapeValueForLike = escapeValueForLike;
    }

    @Override
    public void setTextInputAllowed(boolean textInputAllowed) {
        throw new UnsupportedOperationException("Option textInputAllowed is unsupported for Search field");
    }

    @Override
    public void setOptionsList(List optionsList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOptionsMap(Map<String, V> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOptionsEnum(Class<V> optionsEnum) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOptionsSource(OptionsSource<V> optionsSource) {
        super.setOptionsSource(optionsSource);
    }

    // VAADIN8: gg, implement
    /*@Override
    protected void setupDsAutoRefresh(OptionsDsWrapper ds) {
        ds.setAutoRefresh(false);
        ds.setExecuteAutoRefreshInvalid(false);
    }*/
}