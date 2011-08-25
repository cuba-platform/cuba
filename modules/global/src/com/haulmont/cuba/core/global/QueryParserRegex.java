/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.07.2009 16:14:49
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import org.apache.commons.lang.StringUtils;

import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Implementation of {@link QueryParser} based on regular expressions
 */
public class QueryParserRegex implements QueryParser {

    public static final String ENTITY_PATTERN_REGEX = "(\\b[_A-Za-z]+\\$[A-Z][_A-Za-z0-9]*)(\\s+as\\b)?\\s+([a-z]+[a-z0-9]*)*\\b";
    public static final Pattern ENTITY_PATTERN = Pattern.compile(ENTITY_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String DISTINCT_PATTERN_REGEX = "\\bDISTINCT\\b";
    public static final Pattern DISTINCT_PATTERN = Pattern.compile(DISTINCT_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String WHERE_PATTERN_REGEX = "\\bWHERE\\b";
    public static final Pattern WHERE_PATTERN = Pattern.compile(WHERE_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String LAST_CLAUSE_PATTERN_REGEX = "(\\bGROUP\\s+BY\\b)|(\\bORDER\\s+BY\\b)|(\\bHAVING\\b)";
    public static final Pattern LAST_CLAUSE_PATTERN = Pattern.compile(LAST_CLAUSE_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String ORDER_BY_PATTERN_REGEX = "\\bORDER\\s+BY\\b";
    public static final Pattern ORDER_BY_PATTERN = Pattern.compile(ORDER_BY_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String ALIAS_PATTERN_REGEX = "(^|\\s|\\()(\\w+)\\.";
    public static final Pattern ALIAS_PATTERN = Pattern.compile(ALIAS_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String PARAM_PATTERN_REGEX = ":([a-zA-Z_0-9$\\.]+)";
    public static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    protected String source;

    QueryParserRegex(String source) {
        this.source = source;
    }

    public Set<String> getParamNames() {
        Set<String> result = new HashSet<String>();

        Matcher matcher = PARAM_PATTERN.matcher(source);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }

        return result;
    }

    public String getEntityAlias(String targetEntity) {
        String alias = null;
        Matcher entityMatcher = ENTITY_PATTERN.matcher(source);
        while (entityMatcher.find()) {
            if (targetEntity.equals(entityMatcher.group(1))) {
                alias = entityMatcher.group(3);
                break;
            }
        }
        if (StringUtils.isBlank(alias))
            throw new RuntimeException("No alias for target entity " + targetEntity + " found [" + source + "]");

        return alias;
    }
}
