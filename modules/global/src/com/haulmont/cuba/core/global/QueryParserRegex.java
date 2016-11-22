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
package com.haulmont.cuba.core.global;

import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link QueryParser} based on regular expressions.
 *
 */
public class QueryParserRegex implements QueryParser {
    public static final String QUERY_START_PATTERN_REGEX = "select(\\s+distinct)?\\s+([a-z]+[a-z0-9]*)*\\b";
    public static final Pattern QUERY_START_PATTERN = Pattern.compile(QUERY_START_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);
    public static final int QS_ALIAS = 2;

    public static final String ENTITY_PATH_PATTERN_REGEX = "select(\\s+distinct)?\\s+(%s[\\.]*[a-z0-9]*)?\\s+\\b";
    public static final int ENTITY_PATH_ALIAS = 2;

    public static final String ENTITY_PATTERN_REGEX = "(\\b[_A-Za-z]+\\$[A-Z][_A-Za-z0-9]*)(\\s+as\\b)?\\s+([a-z]+[a-z0-9]*)*\\b";
    public static final Pattern ENTITY_PATTERN = Pattern.compile(ENTITY_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);
    public static final int EP_ALIAS = 3;

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

    public static final String COND_PATTERN_REGEX = "([a-zA-Z_0-9$\\.]+)\\s*([=<>]+|like)\\s*";

    public static final String OR_PATTERN_REGEX = "\\bOR\\b";
    public static final Pattern OR_PATTERN = Pattern.compile(OR_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    protected String source;

    QueryParserRegex(String source) {
        this.source = source;
    }

    @Override
    public Set<String> getParamNames() {
        Set<String> result = new HashSet<>();

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
        throw new RuntimeException("Unable to find entity name [" + source + "]");
    }

    @Override
    public String getEntityAlias(String targetEntity) {
        return getEntityAlias();
    }

    @Override
    public String getEntityAlias() {
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

    @Override
    public boolean hasIsNullCondition(String attribute) {
        Matcher whereMatcher = WHERE_PATTERN.matcher(source);
        if (whereMatcher.find()) {
            String alias = getEntityAlias();
            Pattern isNullPattern = Pattern.compile("\\b" + alias + "\\." + attribute + "((?i)\\s+is\\s+null)\\b");
            Matcher isNullMatcher = isNullPattern.matcher(source);
            return isNullMatcher.find(whereMatcher.end());
        }
        return false;
    }

    @Override
    public Set<String> getAllEntityNames() {
        return null;
    }

    @Override
    public String getEntityNameIfSecondaryReturnedInsteadOfMain() {
        return null;
    }

    @Override
    public String getEntityPathIfSecondaryReturnedInsteadOfMain() {
        return null;
    }

    @Override
    public boolean isCollectionSecondaryEntitySelect() {
        return false;
    }

    @Override
    public boolean isParameterInCondition(String parameterName) {
        return false;
    }
}