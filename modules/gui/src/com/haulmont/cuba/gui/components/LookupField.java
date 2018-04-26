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

public interface LookupField<V> extends OptionsField<V>, HasInputPrompt, Buffered, LookupComponent,
        Component.Focusable, HasOptionsStyleProvider {

    String NAME = "lookupField";

    V getNullOption();
    void setNullOption(V nullOption);

    FilterMode getFilterMode();
    void setFilterMode(FilterMode mode);

    /**
     * vaadin8 deprecate
     *
     * @return true if the component handles new options entered by user.
     * @see LookupField.NewOptionHandler
     */
    boolean isNewOptionAllowed();
    /**
     * vaadin8 deprecate
     *
     * Makes the component handle new options entered by user.
     * @see LookupField.NewOptionHandler
     */
    void setNewOptionAllowed(boolean newOptionAllowed);

    /**
     * @return true if text input allowed
     */
    boolean isTextInputAllowed();
    /**
     * Sets whether it is possible to input text into the field or whether the field area of the component is just used
     * to show what is selected.
     */
    void setTextInputAllowed(boolean textInputAllowed);

    /**
     * @return current handler
     */
    NewOptionHandler getNewOptionHandler();
    /**
     * Set handler.
     * @param newOptionHandler handler instance
     */
    void setNewOptionHandler(NewOptionHandler newOptionHandler);

    /**
     * @return the page length of the suggestion popup.
     */
    int getPageLength();
    /**
     * Sets the page length for the suggestion popup. Setting the page length to
     * 0 will disable suggestion popup paging (all items visible).
     *
     * @param pageLength the pageLength to set
     */
    void setPageLength(int pageLength);

    /**
     * Sets visibility for first null element in suggestion popup.
     */
    void setNullOptionVisible(boolean nullOptionVisible);
    /**
     * @return true if first null element is visible.
     */
    boolean isNullOptionVisible();

    /**
     * Set the icon provider for the LookupField.
     *
     * @param optionIconProvider provider which provides icons for options
     */
    void setOptionIconProvider(OptionIconProvider<? super V> optionIconProvider);

    /**
     * Set the icon provider for LookupField.
     * vaadin8 deprecate
     *
     * @param optionClass        class of the option
     * @param optionIconProvider provider which provides icons for options
     */
    void setOptionIconProvider(Class<V> optionClass, OptionIconProvider<V> optionIconProvider);

    /**
     * @return icon provider of the LookupField.
     */
    OptionIconProvider<? super V> getOptionIconProvider();

    /**
     * Enables to setup how items should be filtered.
     *
     * @param filterPredicate items filter predicate
     */
    void setFilterPredicate(FilterPredicate filterPredicate);

    /**
     * @return items filter predicate
     */
    FilterPredicate getFilterPredicate();

    /**
     * A predicate that tests whether an item with the given caption matches to the given search string.
     */
    @FunctionalInterface
    interface FilterPredicate {

        /**
         * @param itemCaption  a caption of item
         * @param searchString search string as is
         * @return true if item with the given caption matches to the given search string or false otherwise
         */
        boolean test(String itemCaption, String searchString);
    }

    enum FilterMode {
        NO,
        STARTS_WITH,
        CONTAINS
    }

    /**
     * Interface to be implemented if {@link #setNewOptionAllowed(boolean)} is set to true.
     *
     * vaadin8 replace with Consumer
     */
    interface NewOptionHandler {
        /**
         * Called when user enters a value which is not in the options list, and presses Enter.
         * @param caption value entered by user
         */
        void addNewOption(String caption);
    }

    /**
     * Allows to set icons for particular elements in the options list.
     *
     * vaadin8 replace with Supplier
     */
    interface OptionIconProvider<T> {

        /**
         * Called when component paints its content.
         *
         * @param item item from options list, options map or enum options
         * @return icon name or null to show no icon
         */
        String getItemIcon(T item);
    }
}