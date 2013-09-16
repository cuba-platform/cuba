/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link QueryParser} based on regular expressions.
 *
 * @author krivopustov
 * @version $Id$
 */
public class QueryParserRegex implements QueryParser {

    public static final String ENTITY_PATTERN_REGEX = "(\\b[_A-Za-z]+\\$[A-Z][_A-Za-z0-9]*)(\\s+as\\b)?\\s+([a-z]+[a-z0-9]*)*\\b";

    public static final Pattern FROM_ENTITY_PATTERN = Pattern.compile("\\b(from|update)\\s+" + ENTITY_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);
    public static final int FEP_ENTITY = 2;
    public static final int FEP_ALIAS = 4;

    public static final String DISTINCT_PATTERN_REGEX = "\\bDISTINCT\\b";
    public static final Pattern DISTINCT_PATTERN = Pattern.compile(DISTINCT_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String SELECT_DISTINCT_PATTERN_REGEX = "\\bSELECT\\s+DISTINCT\\b";
    public static final Pattern SELECT_DISTINCT_PATTERN = Pattern.compile(SELECT_DISTINCT_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String WHERE_PATTERN_REGEX = "\\bWHERE\\b";
    public static final Pattern WHERE_PATTERN = Pattern.compile(WHERE_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String LAST_CLAUSE_PATTERN_REGEX = "(\\bGROUP\\s+BY\\b)|(\\bORDER\\s+BY\\b)|(\\bHAVING\\b)";
    public static final Pattern LAST_CLAUSE_PATTERN = Pattern.compile(LAST_CLAUSE_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String ORDER_BY_PATTERN_REGEX = "\\bORDER\\s+BY\\b";
    public static final Pattern ORDER_BY_PATTERN = Pattern.compile(ORDER_BY_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

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

    @Override
    public String getEntityName() {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(source);
        if (entityMatcher.find()) {
            return entityMatcher.group(2);
        }
        throw new IllegalStateException("Unable to find entity name [" + source + "]");
    }

    public String getEntityAlias(String targetEntity) {
        String alias = null;
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(source);
        if (entityMatcher.find()) {
            alias = entityMatcher.group(FEP_ALIAS);
        }
        if (StringUtils.isBlank(alias))
            throw new RuntimeException("Unable to find entity alias [" + source + "]");

        return alias;
    }

    @Override
    public boolean isEntitySelect(String targetEntity) {
        String alias = getEntityAlias(targetEntity);
        Pattern pattern = Pattern.compile("\\bselect\\s+(distinct\\s+)*" + alias + "\\s+");
        Matcher matcher = pattern.matcher(source);
        return matcher.find();
    }
}
