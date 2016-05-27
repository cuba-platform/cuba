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

import com.haulmont.cuba.core.entity.Entity;

import java.util.List;
import java.util.Map;

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

    interface ArrowDownActionHandler {
        /**
         * Called by component if user pressed ARROW_DOWN key without search action.
         *
         * @param currentSearchString search string as is.
         */
        void onArrowDownKeyPressed(String currentSearchString);
    }

    int getAsyncSearchTimeoutMs();
    void setAsyncSearchTimeoutMs(int asyncSearchTimeoutMs);

    SearchExecutor getSearchExecutor();
    void setSearchExecutor(SearchExecutor searchExecutor);

    EnterActionHandler getEnterActionHandler();
    void setEnterActionHandler(EnterActionHandler enterActionHandler);

    ArrowDownActionHandler getArrowDownActionHandler();
    void setArrowDownActionHandler(ArrowDownActionHandler arrowDownActionHandler);

    void showSuggestions(List<? extends Entity> suggestions);
}