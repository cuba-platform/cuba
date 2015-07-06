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

        int summaryPositionShift = 0;
        Matcher matcher = aliasPattern.matcher(input.getQuery());
        while (matcher.find()) {
            String alias = matcher.group();
            int regionStart = matcher.start();
            int regionEnd = matcher.end();

            if (result.getPosition() > regionEnd) {
                summaryPositionShift += alias.length();
            } else if (result.getPosition() > regionStart) {
                summaryPositionShift += result.getPosition() - regionStart + 1;
            }
        }

        result.setQuery(result.getQuery().replaceAll(aliasPattern.pattern(), ""));
        result.setPosition(result.getPosition() - summaryPositionShift);

        return result;
    }
}
