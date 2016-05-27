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

package com.haulmont.cuba.gui.components.autocomplete.impl;

import java.util.*;

public class HintResponse {
    private final List<Option> options;
    private final String errorMessage;
    private final List<String> causeErrorMessage;
    private String lastWord;

    public HintResponse(String errorMessage, List<String> causeErrorMessage) {
        this.errorMessage = errorMessage;
        this.causeErrorMessage = new ArrayList<>(causeErrorMessage);
        options = Collections.emptyList();
    }

    public HintResponse(List<Option> options, String lastWord) {
        this.lastWord = lastWord;
        this.options = (options == null) ? Collections.<Option>emptyList() : options;
        errorMessage = null;
        this.causeErrorMessage = null;
    }

    public List<String> getOptions() {
        List<String> result = new ArrayList<>();
        for (Option option : options) {
            result.add(option.getValue());
        }
        Collections.sort(result);
        return Collections.unmodifiableList(result);
    }

    public List<Option> getOptionObjects() {
        Collections.sort(options, new Comparator<Option>() {
            @Override
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