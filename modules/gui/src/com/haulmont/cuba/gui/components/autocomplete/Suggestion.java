/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
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