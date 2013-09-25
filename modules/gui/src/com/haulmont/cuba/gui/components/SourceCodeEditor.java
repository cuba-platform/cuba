/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.Suggester;
import org.apache.commons.lang.StringUtils;

/**
 * @author artamonov
 * @version $Id$
 */
public interface SourceCodeEditor extends Field {

    String NAME = "sourceCodeEditor";

    enum Mode {
        Java,
        HTML,
        XML,
        Groovy,
        SQL,
        JavaScript,
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
}