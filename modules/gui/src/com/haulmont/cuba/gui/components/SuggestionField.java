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

import com.haulmont.bali.util.Preconditions;

import java.util.List;
import java.util.Map;

public interface SuggestionField<V> extends Field<V>, Component.Focusable, Component.HasInputPrompt,
        Component.HasOptionsStyleProvider {

    String NAME = "suggestionField";

    interface SearchExecutor<E> {

        /**
         * Executed on background thread.
         *
         * @param searchString search string as is
         * @param searchParams additional parameters, empty if SearchExecutor is not instance of {@link ParametrizedSearchExecutor}
         * @return list with found items. Item can be any type. {@link OptionWrapper} instances can be used as
         * items to provide different value for displaying purpose.
         */
        List<E> search(String searchString, Map<String, Object> searchParams);
    }

    interface ParametrizedSearchExecutor<E> extends SearchExecutor<E> {
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

    /**
     * @return delay between the last key press action and async search
     * @deprecated Use {@link SuggestionField#getAsyncSearchDelayMs()} instead.
     */
    @Deprecated
    int getAsyncSearchTimeoutMs();

    /**
     * Sets delay between the last key press action and async search.
     *
     * @param asyncSearchTimeoutMs delay in ms
     * @deprecated Use {@link SuggestionField#setAsyncSearchDelayMs(int)} instead.
     */
    @Deprecated
    void setAsyncSearchTimeoutMs(int asyncSearchTimeoutMs);

    /**
     * @return delay between the last key press action and async search
     */
    int getAsyncSearchDelayMs();

    /**
     * Sets delay between the last key press action and async search.
     *
     * @param asyncSearchDelayMs delay in ms
     */
    void setAsyncSearchDelayMs(int asyncSearchDelayMs);

    /**
     * @return {@link SearchExecutor} which performs search
     */
    SearchExecutor getSearchExecutor();

    /**
     * Sets {@link SearchExecutor} which performs search.
     *
     * @param searchExecutor SearchExecutor instance
     */
    void setSearchExecutor(SearchExecutor searchExecutor);

    /**
     * @return {@link EnterActionHandler} which handles ENTER key pressing
     */
    EnterActionHandler getEnterActionHandler();

    /**
     * Sets {@link EnterActionHandler} which handles ENTER key pressing.
     *
     * @param enterActionHandler EnterActionHandler instance
     */
    void setEnterActionHandler(EnterActionHandler enterActionHandler);

    /**
     * @return {@link ArrowDownActionHandler} which handles ARROW_DOWN key pressing
     */
    ArrowDownActionHandler getArrowDownActionHandler();

    /**
     * Sets {@link ArrowDownActionHandler} which handles ARROW_DOWN key pressing.
     *
     * @param arrowDownActionHandler ArrowDownActionHandler instance
     */
    void setArrowDownActionHandler(ArrowDownActionHandler arrowDownActionHandler);

    /**
     * @return min string length to perform suggestions search
     */
    int getMinSearchStringLength();

    /**
     * Sets min string length which is required to perform suggestions search.
     *
     * @param minSearchStringLength required string length to perform search
     */
    void setMinSearchStringLength(int minSearchStringLength);

    /**
     * @return limit of suggestions which will be shown
     */
    int getSuggestionsLimit();


    /**
     * Sets limit of suggestions which will be shown.
     *
     * @param suggestionsLimit integer limit value
     */
    void setSuggestionsLimit(int suggestionsLimit);

    /**
     * Show passed suggestions in popup.
     *
     * @param suggestions suggestions to show
     */
    void showSuggestions(List<?> suggestions);

    /**
     * Sets the given {@code width} to the component popup.
     *
     * @param width width of the component popup
     */
    void setPopupWidth(String width);

    String POPUP_AUTO_WIDTH = "auto";
    String POPUP_PARENT_WIDTH = "parent";

    /**
     * @return component popup width
     */
    String getPopupWidth();

    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    /**
     * Represent value and its string representation.
     */
    class OptionWrapper {
        protected String caption;
        protected Object value;

        public OptionWrapper(String caption, Object value) {
            Preconditions.checkNotNullArgument(caption);
            Preconditions.checkNotNullArgument(value);

            this.caption = caption;
            this.value = value;
        }

        /**
         * @return string representation
         */
        public String getCaption() {
            return caption;
        }

        /**
         * @return value
         */
        public Object getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            OptionWrapper that = (OptionWrapper) o;

            return caption.equals(that.caption) && value.equals(that.value);
        }

        @Override
        public int hashCode() {
            int result = caption.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return caption;
        }
    }
}