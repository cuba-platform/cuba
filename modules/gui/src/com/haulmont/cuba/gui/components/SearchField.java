/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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