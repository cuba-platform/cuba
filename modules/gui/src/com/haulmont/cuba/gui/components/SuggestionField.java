/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;

import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public interface SuggestionField extends SearchField {

    String NAME = "suggestionField";

    interface SearchExecutor<E extends Entity> {

        /**
         * Executed on backgroud thread.
         *
         * @param searchString search string as is.
         * @param searchParams additional parameters, empty of SearchExecutor is not instance of {@link ParametrizedSearchExecutor}
         * @return list with found entities
         */
        List<E> search(String searchString, Map<String, Object> searchParams);
    }

    interface ParametrizedSearchExecutor<E extends Entity> extends SearchExecutor<E> {
        /**
         * Called by the execution environment in UI thread to prepare execution parameters for {@link SearchExecutor#search(String, Map)}.
         *
         * @return map with parameters.
         */
        Map<String, Object> getParams();
    }

    interface EnterActionHandler {
        /**
         * Called by component if user entered a search string and pressed ENTER key without selection of a suggestion.
         *
         * @param currentSearchString search string as is.
         */
        void onEnterKeyPressed(String currentSearchString);
    }

    int getAsyncSearchTimeoutMs();
    void setAsyncSearchTimeoutMs(int asyncSearchTimeoutMs);

    SearchExecutor getSearchExecutor();
    void setSearchExecutor(SearchExecutor searchExecutor);

    EnterActionHandler getEnterActionHandler();
    void setEnterActionHandler(EnterActionHandler enterActionHandler);

    void showSuggestions(List<? extends Entity> suggestions);
}