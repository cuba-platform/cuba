/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.autocomplete.impl;

import com.haulmont.cuba.core.sys.jpql.InferredType;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author chevelev
 * @version $Id$
 */
public class MacroProcessor {
    private final static String BETWEEN_REPLACEMENT = " = :d ";
    private final static String TWO_OPERAND_REPLACEMENT = " =";

    public HintRequest inlineFake(String query, int position) {
        HintRequest result = new HintRequest();
        result.setQuery(query);
        result.setPosition(position);
        result.setExpectedTypes(EnumSet.of(InferredType.Any));

        result = inlineFakeBetweenMacro(result);
        result = inlineFakeSingleOperandMacro("@today", result);
        result = inlineFakeTwoOperandMacro("@dateEquals", result);
        result = inlineFakeTwoOperandMacro("@dateAfter", result);
        result = inlineFakeTwoOperandMacro("@dateBefore", result);
        return result;
    }

    private HintRequest inlineFakeBetweenMacro(HintRequest request) {
        String newQuery = request.getQuery();
        int newPosition = request.getPosition();
        Set<InferredType> newExpectedTypes = request.getExpectedTypes();

        String macroName = "@between";
        String macroEntry = macroName + "(";
        while (true) {
            int macroBegin = newQuery.indexOf(macroEntry);
            if (macroBegin == -1) {
                break;
            }

            int macroEnd = macroBegin + macroEntry.length();
            int firstComma = newQuery.indexOf(',', macroBegin);
            // assuming no braces within macro
            int closingBrace = newQuery.indexOf(')', macroBegin);

            newQuery = newQuery.substring(0, macroBegin)
                    + newQuery.substring(macroEnd, firstComma)
                    + BETWEEN_REPLACEMENT
                    + newQuery.substring(closingBrace + 1);


            if (newPosition > macroEnd && newPosition < firstComma) {
                newPosition += -macroEntry.length();
                newExpectedTypes = EnumSet.of(InferredType.Date);
            } else if (newPosition > firstComma && newPosition < closingBrace) {
                throw new IllegalStateException("Cannot handle request: unsupported request position within macro");
            } else if (newPosition > closingBrace) {
                newPosition += -macroEntry.length() + BETWEEN_REPLACEMENT.length() - 1 + (firstComma - closingBrace);
            }
        }

        HintRequest result = new HintRequest();
        result.setQuery(newQuery);
        result.setPosition(newPosition);
        result.setExpectedTypes(newExpectedTypes);
        return result;
    }

    private HintRequest inlineFakeTwoOperandMacro(String macro, HintRequest request) {
        String newQuery = request.getQuery();
        int newPosition = request.getPosition();
        Set<InferredType> newExpectedTypes = request.getExpectedTypes();

        String macroEntry = macro + "(";
        while (true) {
            int macroBegin = newQuery.indexOf(macroEntry);
            if (macroBegin == -1) {
                break;
            }

            int macroEnd = macroBegin + macroEntry.length();
            int comma = newQuery.indexOf(',', macroBegin);
            // assuming no braces within macro
            int closingBrace = newQuery.indexOf(')', macroBegin);

            newQuery = newQuery.substring(0, macroBegin)
                    + newQuery.substring(macroEnd, comma)
                    + TWO_OPERAND_REPLACEMENT
                    + newQuery.substring(comma + 1, closingBrace)
                    + " "
                    + newQuery.substring(closingBrace + 1);

            if (newPosition > macroEnd && newPosition < comma) {
                newPosition += -macroEntry.length();
                newExpectedTypes = EnumSet.of(InferredType.Date);
            } else if (newPosition > comma && newPosition < closingBrace) {
                newPosition += -macroEntry.length() + TWO_OPERAND_REPLACEMENT.length() - 1;
                newExpectedTypes = EnumSet.of(InferredType.Date);
            } else if (newPosition > closingBrace) {
                newPosition += -macroEntry.length() + TWO_OPERAND_REPLACEMENT.length() - 1 + 1 - 1;
            }
        }

        HintRequest result = new HintRequest();
        result.setQuery(newQuery);
        result.setPosition(newPosition);
        result.setExpectedTypes(newExpectedTypes);
        return result;
    }

    private HintRequest inlineFakeSingleOperandMacro(String macro, HintRequest request) {
        String newQuery = request.getQuery();
        int newPosition = request.getPosition();
        Set<InferredType> newExpectedTypes = request.getExpectedTypes();

        String macroEntry = macro + "(";
        while (true) {
            int macroBegin = newQuery.indexOf(macroEntry);
            if (macroBegin == -1) {
                break;
            }

            int macroEnd = macroBegin + macroEntry.length();
            int closingBrace = newQuery.indexOf(')', macroBegin);

            newQuery = newQuery.substring(0, macroBegin)
                    + newQuery.substring(macroEnd, closingBrace)
                    + BETWEEN_REPLACEMENT
                    + newQuery.substring(closingBrace + 1);

            if (newPosition > macroEnd && newPosition < closingBrace) {
                newPosition += -macroEntry.length();
                newExpectedTypes = EnumSet.of(InferredType.Date);
            } else if (newPosition > closingBrace) {
                newPosition += -macroEntry.length() + BETWEEN_REPLACEMENT.length() - 1;
            }
        }

        HintRequest result = new HintRequest();
        result.setQuery(newQuery);
        result.setPosition(newPosition);
        result.setExpectedTypes(newExpectedTypes);
        return result;
    }
}