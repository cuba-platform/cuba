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

import java.util.EventObject;

public interface TextInputField<V> extends Field<V>, Component.Buffered, Component.Focusable, PropertyBoundComponent {

    /**
     * Defines case conversion for text input fields,
     * which implement {@link TextInputField.CaseConversionSupported} interface.
     */
    enum CaseConversion {
        NONE,
        LOWER,
        UPPER
    }

    interface TrimSupported {
        boolean isTrimming();
        void setTrimming(boolean trimming);
    }

    interface MaxLengthLimited {
        int getMaxLength();
        void setMaxLength(int value);
    }

    interface CursorPositionSupported {
        /**
         * Sets the cursor position in the field.
         *
         * @param position new cursor position
         */
        void setCursorPosition(int position);
    }

    interface CaseConversionSupported {
        /**
         * @return conversion mode or null if automatic conversion is disabled
         */
        CaseConversion getCaseConversion();
        /**
         * Disable automatic case conversion or enable with chosen mode
         */
        void setCaseConversion(CaseConversion caseConversion);
    }

    interface TextSelectionSupported {
        void selectAll();
        void setSelectionRange(int pos, int length);
    }

    interface TextChangeNotifier {
        void addTextChangeListener(TextChangeListener listener);
        void removeTextChangeListener(TextChangeListener listener);

        /**
         * Gets the timeout used to fire {@link TextChangeEvent}s when the
         * {@link #getTextChangeEventMode()} is {@link TextChangeEventMode#LAZY} or
         * {@link TextChangeEventMode#TIMEOUT}.
         *
         * @return timeout in milliseconds
         */
        int getTextChangeTimeout();
        /**
         * The text change timeout modifies how often text change events are
         * communicated to the application when {@link #getTextChangeEventMode()} is
         * {@link TextChangeEventMode#LAZY} or {@link TextChangeEventMode#TIMEOUT}.
         *
         * @param timeout timeout in milliseconds
         */
        void setTextChangeTimeout(int timeout);

        /**
         * @return the mode used to trigger {@link TextChangeEvent}s.
         */
        TextChangeEventMode getTextChangeEventMode();
        /**
         * Sets the mode how the TextField triggers {@link TextChangeEvent}s.
         *
         * @param mode the new mode
         */
        void setTextChangeEventMode(TextChangeEventMode mode);
    }

    @FunctionalInterface
    interface TextChangeListener {
        void textChange(TextChangeEvent event);
    }

    /**
     * TextChangeEvents are fired when the user is editing the text content of a field. Most commonly text change events
     * are triggered by typing text with keyboard, but e.g. pasting content from clip board to a text field also
     * triggers an event.
     * <p>
     * TextChangeEvents differ from {@link ValueChangeEvent}s so that they are triggered repeatedly while the end user
     * is filling the field. ValueChangeEvents are not fired until the user for example hits enter or focuses another
     * field. Also note the difference that TextChangeEvents are only fired if the change is triggered from the user,
     * while ValueChangeEvents are also fired if the field value is set by the application code.
     */
    class TextChangeEvent extends EventObject {
        private final String text;

        private final int cursorPosition;

        public TextChangeEvent(TextInputField source, String text, int cursorPosition) {
            super(source);
            this.text = text;
            this.cursorPosition = cursorPosition;
        }

        @Override
        public TextInputField getSource() {
            return (TextInputField) super.getSource();
        }

        public String getText() {
            return text;
        }

        public int getCursorPosition() {
            return cursorPosition;
        }
    }

    /**
     * Different modes how the TextField can trigger {@link TextChangeEvent}s.
     */
    enum TextChangeEventMode {
        /**
         * An event is triggered on each text content change, most commonly key
         * press events.
         */
        EAGER,
        /**
         * Each text change event in the UI causes the event to be communicated to the application after a timeout.
         * The length of the timeout can be controlled with {@link TextChangeNotifier#setTextChangeTimeout(int)}.
         * Only the last input event is reported to the server side if several text change events happen during the timeout.
         * <p>
         * In case of a {@link ValueChangeEvent} the schedule is not kept strictly. Before a {@link ValueChangeEvent}
         * a {@link TextChangeEvent} is triggered if the text content has changed since the previous TextChangeEvent
         * regardless of the schedule.
         */
        TIMEOUT,
        /**
         * An event is triggered when there is a pause of text modifications. The length of the pause can be modified
         * with {@link TextChangeNotifier#setTextChangeTimeout(int)}. Like with the {@link #TIMEOUT} mode, an event is
         * forced before {@link ValueChangeEvent}s, even if the user did not keep a pause while entering the text.
         * <p>
         * This is the default mode.
         */
        LAZY
    }

    interface EnterPressNotifier {
        void addEnterPressListener(EnterPressListener listener);
        void removeEnterPressListener(EnterPressListener listener);
    }

    @FunctionalInterface
    interface EnterPressListener {
        void enterPressed(EnterPressEvent e);
    }

    /**
     * EnterPressEvents are fired when the user presses Enter while editing the text content of a field.
     */
    class EnterPressEvent extends EventObject {
        public EnterPressEvent(TextInputField source) {
            super(source);
        }

        @Override
        public TextInputField getSource() {
            return (TextInputField) super.getSource();
        }
    }
}