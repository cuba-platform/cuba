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

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.DomainModelBuilder;
import com.haulmont.cuba.gui.components.autocomplete.impl.HintProvider;
import com.haulmont.cuba.gui.components.autocomplete.impl.HintRequest;
import com.haulmont.cuba.gui.components.autocomplete.impl.HintResponse;
import com.haulmont.cuba.gui.components.autocomplete.impl.Option;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class JpqlSuggestionFactory {

    protected static Suggestion produce(AutoCompleteSupport sender, String value, String description, int senderCursorPosition, int prefixLength) {
        String valueSuffix = value.substring(prefixLength);
        String displayedValue;
        if (description == null) {
            displayedValue = value;
        } else {
            displayedValue = value + " (" + description + ")";
        }
        int startPosition = senderCursorPosition - prefixLength;

        return new Suggestion(sender, displayedValue, value, valueSuffix, startPosition, senderCursorPosition);
    }

    public static List<Suggestion> requestHint(String query, int queryPosition, AutoCompleteSupport sender,
                                               int senderCursorPosition) {
        return requestHint(query, queryPosition, sender, senderCursorPosition, null);
    }

    public static List<Suggestion> requestHint(String query, int queryPosition, AutoCompleteSupport sender,
                                               int senderCursorPosition, @Nullable HintProvider provider) {
        DomainModelBuilder builder = new DomainModelBuilder();
        DomainModel domainModel = builder.produce();
        if (provider == null) {
            provider = new HintProvider(domainModel);
        }
        try {
            HintRequest request = new HintRequest();
            request.setQuery(query);
            request.setPosition(queryPosition);
            HintResponse response = provider.requestHint(request);
            String prefix = response.getLastWord();
            List<Option> options = response.getOptionObjects();

            List<Suggestion> result = new ArrayList<>();
            for (Option option : options) {
                Suggestion suggestion = JpqlSuggestionFactory.produce(sender, option.getValue(), option.getDescription(),
                        senderCursorPosition, prefix == null ? 0 : prefix.length());
                result.add(suggestion);
            }
            return result;
        } catch (org.antlr.runtime.RecognitionException e) {
            throw new RuntimeException(e);
        }
    }
}