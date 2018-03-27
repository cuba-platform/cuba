/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;

/**
 * The CurrencyField component is intended for displaying currency values.
 */
public interface CurrencyField<V> extends Field<V>, Component.Buffered {
    String NAME = "currencyField";

    /**
     * Sets the given <code>currency</code> to the field. Currency label will be displayed next to the text input
     * component if the <code>showCurrency</code> option is enabled.
     *
     * Recommended max currency length - 3 character.
     *
     * @param currency currency ($, EUR, etc)
     */
    void setCurrency(String currency);

    /**
     * @return current currency
     */
    String getCurrency();

    /**
     * Enables or disables currency label displaying.
     */
    void setShowCurrencyLabel(boolean showCurrencyLabel);

    /**
     * @return true if currency label is displayed or false otherwise
     */
    boolean getShowCurrencyLabel();

    /**
     * Sets where the currency label will be located: to the left or to the right from the text input component.
     *
     * @param currencyLabelPosition not-null {@link CurrencyLabelPosition} value
     */
    void setCurrencyLabelPosition(CurrencyLabelPosition currencyLabelPosition);

    /**
     * @return where the currency label is located
     */
    CurrencyLabelPosition getCurrencyLabelPosition();

    /**
     * Sets the given <code>datatype</code> to the component. Its value will be formatted according to this
     * datatype.
     *
     * @param datatype {@link Datatype} instance
     */
    void setDatatype(Datatype datatype);

    /**
     * @return a datatype that is used by this component
     */
    Datatype getDatatype();

    /**
     * Defines where the currency label is located.
     */
    enum CurrencyLabelPosition {
        /**
         * To the left from the text input component.
         */
        LEFT,
        /**
         * To the right from the text input component.
         */
        RIGHT
    }
}
