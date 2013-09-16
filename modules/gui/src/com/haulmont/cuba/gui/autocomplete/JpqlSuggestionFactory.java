/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.autocomplete;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.DomainModelBuilder;
import com.haulmont.cuba.gui.autocomplete.impl.HintProvider;
import com.haulmont.cuba.gui.autocomplete.impl.HintRequest;
import com.haulmont.cuba.gui.autocomplete.impl.HintResponse;
import com.haulmont.cuba.gui.autocomplete.impl.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Chevelev
 * @version $Id$
 */
public class JpqlSuggestionFactory {

    private int prefixLength;
    private int startPosition;
    private int endPosition;

    public JpqlSuggestionFactory(int senderCursorPosition, int prefixLength) {
        this.prefixLength = prefixLength;

        startPosition = senderCursorPosition - prefixLength;
        endPosition = senderCursorPosition;
    }

    public Suggestion produce(AutoCompleteSupport sender, String value, String description) {
        String valueSuffix = value.substring(prefixLength);
        String displayedValue;
        if (description == null) {
            displayedValue = value;
        } else {
            displayedValue = value + " (" + description + ")";
        }
        return new Suggestion(sender, displayedValue, value, valueSuffix, startPosition, endPosition);
    }

    public static List<Suggestion> requestHint(String query, int queryPosition, AutoCompleteSupport sender,
                                               int senderCursorPosition) {
        DomainModelBuilder builder = new DomainModelBuilder(
                AppBeans.get(MetadataTools.class), AppBeans.get(MessageTools.class));
        DomainModel domainModel = builder.produce();

        HintProvider provider = new HintProvider(domainModel);
        try {
            HintRequest request = new HintRequest();
            request.setQuery(query);
            request.setPosition(queryPosition);
            HintResponse response = provider.requestHint(request);
            String prefix = response.getLastWord();
            List<Option> options = response.getOptionObjects();

            List<Suggestion> result = new ArrayList<Suggestion>();
            JpqlSuggestionFactory suggestionFactory = new JpqlSuggestionFactory(senderCursorPosition, prefix == null ? 0 : prefix.length());

            for (Option option : options) {
                Suggestion suggestion = suggestionFactory.produce(sender, option.getValue(), option.getDescription());
                result.add(suggestion);
            }
            return result;
        } catch (org.antlr.runtime.RecognitionException e) {
            throw new RuntimeException(e);
        }
    }
}
