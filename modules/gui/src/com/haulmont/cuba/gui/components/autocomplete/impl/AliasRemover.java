/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.autocomplete.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class AliasRemover {
    protected Pattern aliasPattern = Pattern.compile("as\\s+\"?([\\w|\\d|_|\\.]+)\"?\\s*");

    public HintRequest replaceAliases(HintRequest input) {
        HintRequest result = new HintRequest();
        result.setQuery(input.getQuery());
        result.setPosition(input.getPosition());
        result.setExpectedTypes(input.getExpectedTypes());

        int indexOfFrom = input.getQuery().indexOf("from");
        Matcher matcher = aliasPattern.matcher(input.getQuery());
        String resultQuery = result.getQuery();
        while (matcher.find()) {
            String alias = matcher.group();
            int regionStart = matcher.start();
            int regionEnd = matcher.end();

            if (regionEnd <= indexOfFrom || indexOfFrom == -1) {
                if (result.getPosition() > regionEnd) {
                    result.setPosition(result.getPosition() - alias.length());
                    resultQuery = matcher.replaceFirst("");
                    matcher.reset(resultQuery);
                } else if (result.getPosition() > regionStart) {
                    result.setPosition(regionStart - 1);
                    resultQuery = matcher.replaceFirst("");
                    matcher.reset(resultQuery);
                } else {
                    resultQuery = matcher.replaceFirst("");
                    matcher.reset(resultQuery);
                }
                indexOfFrom = resultQuery.indexOf("from");
            }
        }

        result.setQuery(resultQuery);

        return result;
    }
}