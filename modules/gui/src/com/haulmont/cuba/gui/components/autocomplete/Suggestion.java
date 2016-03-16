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
package com.haulmont.cuba.gui.components.autocomplete;

/**
 * Class for single suggestion for an {@link AutoCompleteSupport} field.
 * <p/>
 * Single suggestion has three attributes: <li>Suggestion value itself. If
 * user selects this suggestion this value will be inserted to text field.</li>
 * <li>Display value that is presented inside the suggestion box. <li>Cursor
 * positions (start and end) for the replacement.
 *
 */
public class Suggestion {

    protected String displayText;
    protected String valueText;
    protected String valueSuffix;
    protected int startPosition;
    protected int endPosition;

    /**
     * Create new suggestion.
     *
     * @param displayText   Text to display in the suggestion box
     * @param valueText     Value to be inserted into text field if this suggestion is
     *                      selected.
     * @param valueSuffix   The part of the value that is located after the current
     *                      cursor position. This is used by the client-side filtering
     *                      method.
     * @param startPosition Start position of the replacement. Must be positive and
     *                      below length of text in textfield. If negative number is
     *                      given the current cursor position is used.
     * @param endPosition   End position of the replacement. Must be positive and
     *                      below length of text in textfield. If negative number is
     *                      given the current cursor position is used.
     */
    public Suggestion(AutoCompleteSupport component, String displayText, String valueText,
                      String valueSuffix, int startPosition, int endPosition) {
        super();
        this.displayText = displayText;
        this.valueText = valueText;
        this.valueSuffix = valueSuffix;
        this.startPosition = startPosition < 0 ? component.getCursorPosition() : startPosition;
        this.endPosition = endPosition < 0 ? component.getCursorPosition() : endPosition;

        // Text length check
        Object value = component.getValue();
        if (value != null) {
            int l = value.toString().length();
            if (startPosition > l) {
                this.startPosition = l;
            }
            if (endPosition > l) {
                this.endPosition = l;
            }
        }
    }

    public String getValueSuffix() {
        return valueSuffix;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getValueText() {
        return valueText;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }
}