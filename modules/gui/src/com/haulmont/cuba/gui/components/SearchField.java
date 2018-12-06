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

import com.google.common.reflect.TypeToken;

public interface SearchField<V> extends LookupField<V> {

    String NAME = "searchField";

    String SEARCH_STRING_PARAM = "searchString";

    static <T> TypeToken<SearchField<T>> of(Class<T> valueClass) {
        return new TypeToken<SearchField<T>>() {};
    }

    /**
     * Sets minimal required search string length.
     *
     * @param searchStringLength minimal string length
     */
    void setMinSearchStringLength(int searchStringLength);

    /**
     * @return minimal required search string length
     */
    int getMinSearchStringLength();

    /**
     * Sets notifications configuration object that determines what messages will be shown in case of too small
     * search string or absence of matches.
     * <p>
     * {@link Frame.NotificationType#TRAY} is the default value.
     *
     * @param searchNotifications {@link SearchNotifications} instance
     */
    void setSearchNotifications(SearchNotifications searchNotifications);

    /**
     * @return notifications configuration object
     */
    SearchNotifications getSearchNotifications();

    /**
     * Sets a type of notifications ({@code TRAY}, {@code WARNING}, etc) that will be shown in case of too small
     * search string or absence of matches.
     *
     * @param defaultNotificationType notification type
     */
    void setDefaultNotificationType(Frame.NotificationType defaultNotificationType);

    /**
     * @return notification type
     */
    Frame.NotificationType getDefaultNotificationType();

    /**
     * Sets whether search string case should be considered or it should be converted to upper or lower case.
     * <p>
     * {@link Mode#CASE_SENSITIVE} is the default mode.
     *
     * @param mode search mode
     */
    void setMode(Mode mode);

    /**
     * @return search string case mode
     */
    Mode getMode();

    /**
     * Sets whether special symbols (like %, \, _) should be escaped in a search string.
     *
     * @param escapeValueForLike escape special symbols if true, don't otherwise
     */
    void setEscapeValueForLike(boolean escapeValueForLike);

    /**
     * @return whether special symbols (like %, \, _) should be escaped in a search string.
     */
    boolean isEscapeValueForLike();

    /**
     * Notifications configuration object. Enables to determine what messages will be shown in case of too small
     * search string or absence of matches.
     */
    interface SearchNotifications {

        /**
         * Sets a message that will be shown in case of absence of matches.
         *
         * @param filterString search string
         */
        void notFoundSuggestions(String filterString);

        /**
         * Sets a message that will be shown in case of too small search string.
         *
         * @param filterString          search string
         * @param minSearchStringLength minimal search string length
         */
        void needMinSearchStringLength(String filterString, int minSearchStringLength);
    }

    /**
     * Determines how search string case should be considered.
     */
    enum Mode {

        CASE_SENSITIVE,
        LOWER_CASE,
        UPPER_CASE
    }
}