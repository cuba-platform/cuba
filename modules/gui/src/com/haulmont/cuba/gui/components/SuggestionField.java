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

public interface SuggestionField<V> extends Field<V>, Component.Focusable, HasInputPrompt, HasOptionsStyleProvider<V> {

    String NAME = "suggestionField";

    /**
     * Custom suggestions search action interface.
     *
     * @param <E> items type
     */
    interface SearchExecutor<E> {

        /**
         * Executed on background thread.
         *
         * @param searchString search string as is
         * @param searchParams additional parameters, empty if SearchExecutor is not instance of {@link ParametrizedSearchExecutor}
         * @return list with found items. {@link OptionWrapper} instances can be used as items to provide
         * different value for displaying purpose.
         */
        List<E> search(String searchString, Map<String, Object> searchParams);
    }

    /**
     * Extended version of {@link SuggestionField.SearchExecutor} that allows to pass parameters.
     *
     * @param <E> items type
     */
    interface ParametrizedSearchExecutor<E> extends SearchExecutor<E> {

        /**
         * Called by the execution environment in UI thread to prepare execution parameters for
         * {@link SearchExecutor#search(String, Map)}.
         *
         * @return map with parameters
         */
        Map<String, Object> getParams();
    }

    /**
     * ENTER key pressed listener.
     */
    @FunctionalInterface
    interface EnterActionHandler {

        /**
         * Called by component if user entered a search string and pressed ENTER key without selection of a suggestion.
         *
         * @param searchString search string as is
         */
        void onEnterKeyPressed(String searchString);
    }

    /**
     * ARROW_DOWN key pressed listener.
     */
    @FunctionalInterface
    interface ArrowDownActionHandler {

        /**
         * Called by component if user pressed ARROW_DOWN key without search action.
         *
         * @param searchString search string as is
         */
        void onArrowDownKeyPressed(String searchString);
    }

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
    // Use raw type until #391 will be fixed
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
    void showSuggestions(List<V> suggestions);

    /**
     * Sets the given {@code width} to the component popup. There are two predefined settings available:
     * {@link SuggestionField#POPUP_AUTO_WIDTH} and {@link SuggestionField#POPUP_PARENT_WIDTH}.
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

    /**
     * @return option captions mode generation
     */
    CaptionMode getCaptionMode();

    /**
     * Sets how option captions should be generated.
     *
     * @param captionMode mode
     */
    void setCaptionMode(CaptionMode captionMode);

    /**
     * @return a property that is used for caption generation
     */
    String getCaptionProperty();

    /**
     * Sets a property that will be used for option caption generation when {@link CaptionMode#PROPERTY} is used.
     *
     * @param captionProperty property
     */
    void setCaptionProperty(String captionProperty);

    /**
     * Represents a value and its string representation.
     */
    class OptionWrapper<V> {

        protected String caption;
        protected V value;

        public OptionWrapper(String caption, V value) {
            Preconditions.checkNotNullArgument(caption, "Caption should not be null");
            Preconditions.checkNotNullArgument(value, "Value should not be null");

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
        public V getValue() {
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

            return caption.equals(that.caption)
                    && value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return 17 * caption.hashCode() + 31 * value.hashCode();
        }

        @Override
        public String toString() {
            return caption;
        }
    }
}