/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author artamonov
 * @version $Id$
 */
public interface SearchField extends LookupField {

    String NAME = "searchField";

    String SEARCH_STRING_PARAM = "searchString";

    void setMinSearchStringLength(int searchStringLength);
    int getMinSearchStringLength();

    void setSearchNotifications(SearchNotifications searchNotifications);
    SearchNotifications getSearchNotifications();

    IFrame.NotificationType getDefaultNotificationType();
    void setDefaultNotificationType(IFrame.NotificationType defaultNotificationType);

    public interface SearchNotifications {

        void notFoundSuggestions(String filterString);

        void needMinSearchStringLength(String filterString, int minSearchStringLength);
    }
}