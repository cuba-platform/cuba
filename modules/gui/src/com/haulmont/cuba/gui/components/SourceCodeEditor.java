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

import com.haulmont.cuba.gui.components.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.components.autocomplete.Suggester;
import org.apache.commons.lang.StringUtils;

public interface SourceCodeEditor extends Field {
    String NAME = "sourceCodeEditor";

    enum Mode {
        Java,
        HTML,
        XML,
        Groovy,
        SQL,
        JavaScript,
        Properties,
        Text;

        public static Mode parse(String name) {
            if (StringUtils.isEmpty(name)) {
                return Text;
            }

            for (Mode mode : values()) {
                if (StringUtils.equalsIgnoreCase(name, mode.name())) {
                    return mode;
                }
            }

            return Text;
        }
    }

    Mode getMode();
    void setMode(Mode mode);

    Suggester getSuggester();
    void setSuggester(Suggester suggester);

    AutoCompleteSupport getAutoCompleteSupport();

    void setShowGutter(boolean showGutter);
    boolean isShowGutter();

    void setShowPrintMargin(boolean showPrintMargin);
    boolean isShowPrintMargin();

    void setHighlightActiveLine(boolean highlightActiveLine);
    boolean isHighlightActiveLine();

    /**
     * Enables Tab key handling as tab symbol.
     * If handleTabKey is false then Tab/Shift-Tab key press will change focus to next/previous field.
     */
    void setHandleTabKey(boolean handleTabKey);

    /**
     * @return if Tab key handling is enabled
     */
    boolean isHandleTabKey();

    @SuppressWarnings("unchecked")
    @Override
    String getValue();
}