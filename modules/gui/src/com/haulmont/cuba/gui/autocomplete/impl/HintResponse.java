/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.autocomplete.impl;

import java.util.*;

/**
 * User: Alex Chevelev
 * Date: 13.10.2010
 * Time: 20:46:51
 */
public class HintResponse {
    private final List<Option> options;
    private final String errorMessage;
    private final List<String> causeErrorMessage;
    private String lastWord;

    public HintResponse(String errorMessage, List<String> causeErrorMessage) {
        this.errorMessage = errorMessage;
        this.causeErrorMessage = new ArrayList<String>(causeErrorMessage);
        options = Collections.emptyList();
    }

    HintResponse(List<Option> options, String lastWord) {
        this.lastWord = lastWord;
        this.options = (options == null) ? Collections.<Option>emptyList() : options;
        errorMessage = null;
        this.causeErrorMessage = null;
    }

    public List<String> getOptions() {
        List<String> result = new ArrayList<String>();
        for (Option option : options) {
            result.add(option.getValue());
        }
        Collections.sort(result);
        return Collections.unmodifiableList(result);
    }

    public List<Option> getOptionObjects() {
        Collections.sort(options, new Comparator<Option>() {
            public int compare(Option o1, Option o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        return Collections.unmodifiableList(options);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<String> getCauseErrorMessages() {
        return Collections.unmodifiableList(causeErrorMessage);
    }

    public String getLastWord() {
        return lastWord;
    }
}