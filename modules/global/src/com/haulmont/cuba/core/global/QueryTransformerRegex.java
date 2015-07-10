/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link QueryTransformer} based on regular expressions.
 *
 * @author krivopustov
 * @version $Id$
 */
public class QueryTransformerRegex extends QueryParserRegex implements QueryTransformer {

    private StringBuffer buffer;
    private Set<String> addedParams;

    QueryTransformerRegex(String source) {
        super(source);
        buffer = new StringBuffer(source);
        addedParams = new HashSet<>();
    }

    @Override
    public void addWhere(String where) {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(buffer);
        String alias = findAlias(entityMatcher);

        int insertPos = buffer.length();
        Matcher lastClauseMatcher = LAST_CLAUSE_PATTERN.matcher(buffer);
        if (lastClauseMatcher.find(entityMatcher.end()))
            insertPos = lastClauseMatcher.start() - 1;

        StringBuilder sb = new StringBuilder();
        Matcher whereMatcher = WHERE_PATTERN.matcher(buffer);
        int whereEnd = -1;
        boolean needOpenBracket = false;
        if (whereMatcher.find(entityMatcher.end())) {
            whereEnd = whereMatcher.end();

            Matcher orMatcher = OR_PATTERN.matcher(buffer);
            orMatcher.region(whereEnd + 1, insertPos);
            if (orMatcher.find()) { // surround with brackets if there is OR inside WHERE
                sb.append(")");
                needOpenBracket = true;
            }
            sb.append(" and ");
        } else {
            sb.append(" where ");
        }

        sb.append("(").append(where);
        int idx;
        while ((idx = sb.indexOf(ALIAS_PLACEHOLDER)) >= 0) {
            sb.replace(idx, idx + ALIAS_PLACEHOLDER.length(), alias);
        }
        sb.append(")");

        if (needOpenBracket) {
            buffer.insert(whereEnd + 1, "(");
            insertPos++;
        }

        buffer.insert(insertPos, sb);

        Matcher paramMatcher = PARAM_PATTERN.matcher(where);
        while (paramMatcher.find()) {
            addedParams.add(paramMatcher.group(1));
        }
    }

    @Override
    public void addWhereAsIs(String where) {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(buffer);
        findAlias(entityMatcher);

        int insertPos = buffer.length();
        Matcher lastClauseMatcher = LAST_CLAUSE_PATTERN.matcher(buffer);
        if (lastClauseMatcher.find(entityMatcher.end()))
            insertPos = lastClauseMatcher.start() - 1;

        StringBuilder sb = new StringBuilder();
        Matcher whereMatcher = WHERE_PATTERN.matcher(buffer);
        if (whereMatcher.find(entityMatcher.end()))
            sb.append(" and ");
        else
            sb.append(" where ");

        sb.append("(").append(where).append(")");

        buffer.insert(insertPos, sb);

        Matcher paramMatcher = PARAM_PATTERN.matcher(where);
        while (paramMatcher.find()) {
            addedParams.add(paramMatcher.group(1));
        }
    }

    @Override
    public void addJoinAsIs(String join) {
        Matcher matcher = findReturnedEntityDeclaration();
        int insertPos = matcher.end();

        buffer.insert(insertPos, " ");
        insertPos++;

        buffer.insert(insertPos, join);

        Matcher paramMatcher = PARAM_PATTERN.matcher(join);
        while (paramMatcher.find()) {
            addedParams.add(paramMatcher.group(1));
        }
    }

    @Override
    public void addJoinAndWhere(String join, String where) {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(buffer);
        String alias = findAlias(entityMatcher);

        int insertPos = buffer.length();

        Matcher whereMatcher = WHERE_PATTERN.matcher(buffer);
        if (whereMatcher.find(entityMatcher.end())) {
            insertPos = whereMatcher.start() - 1;
        } else {
            Matcher lastClauseMatcher = LAST_CLAUSE_PATTERN.matcher(buffer);
            if (lastClauseMatcher.find(entityMatcher.end()))
                insertPos = lastClauseMatcher.start() - 1;
        }

        if (!StringUtils.isBlank(join)) {
            buffer.insert(insertPos, " ");
            insertPos++;
            buffer.insert(insertPos, join);

            Matcher paramMatcher = PARAM_PATTERN.matcher(join);
            while (paramMatcher.find()) {
                addedParams.add(paramMatcher.group(1));
            }
        }
        if (!StringUtils.isBlank(where)) {
            insertPos = buffer.length();
            Matcher lastClauseMatcher = LAST_CLAUSE_PATTERN.matcher(buffer);
            if (lastClauseMatcher.find(entityMatcher.end()))
                insertPos = lastClauseMatcher.start() - 1;

            StringBuilder sb = new StringBuilder();
            whereMatcher = WHERE_PATTERN.matcher(buffer);
            int whereEnd = -1;
            boolean needOpenBracket = false;
            if (whereMatcher.find(entityMatcher.end())) {
                whereEnd = whereMatcher.end();

                Matcher orMatcher = OR_PATTERN.matcher(buffer);
                orMatcher.region(whereEnd + 1, insertPos);
                if (orMatcher.find()) { // surround with brackets if there is OR inside WHERE
                    sb.append(")");
                    needOpenBracket = true;
                }
                sb.append(" and ");
            } else {
                sb.append(" where ");
            }

            sb.append("(").append(where).append(")");

            if (needOpenBracket) {
                buffer.insert(whereEnd + 1, "(");
                insertPos++;
            }

            buffer.insert(insertPos, sb);

            Matcher paramMatcher = PARAM_PATTERN.matcher(where);
            while (paramMatcher.find()) {
                addedParams.add(paramMatcher.group(1));
            }
        }

        // replace ALIAS_PLACEHOLDER
        int idx;
        while ((idx = buffer.indexOf(ALIAS_PLACEHOLDER)) >= 0) {
            buffer.replace(idx, idx + ALIAS_PLACEHOLDER.length(), alias);
        }
    }

    @Override
    public void mergeWhere(String query) {
        int startPos = 0;
        Matcher whereMatcher = WHERE_PATTERN.matcher(query);
        if (whereMatcher.find())
            startPos = whereMatcher.end() + 1;

        int endPos = query.length();
        Matcher lastClauseMatcher = LAST_CLAUSE_PATTERN.matcher(query);
        if (lastClauseMatcher.find())
            endPos = lastClauseMatcher.start();

        addWhere(query.substring(startPos, endPos));
    }

    @Override
    public void replaceWithCount() {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(buffer);
        String alias = findAlias(entityMatcher);

        Matcher distinctMatcher = DISTINCT_PATTERN.matcher(buffer);

        buffer.replace(0, entityMatcher.start(),
                "select count("+ (distinctMatcher.find() ? "distinct " : "") + alias + ") ");

        Matcher orderMatcher = ORDER_BY_PATTERN.matcher(buffer);
        if (orderMatcher.find()) {
            buffer.delete(orderMatcher.start(), buffer.length());
        }
    }

    @Override
    public void replaceWithSelectId() {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(buffer);
        String alias = findAlias(entityMatcher);

        Matcher distinctMatcher = DISTINCT_PATTERN.matcher(buffer);

        buffer.replace(0, entityMatcher.start(),
                "select " + (distinctMatcher.find() ? "distinct " : "") + alias + ".id ");

        Matcher orderMatcher = ORDER_BY_PATTERN.matcher(buffer);
        if (orderMatcher.find()) {
            buffer.delete(orderMatcher.start(), buffer.length());
        }
    }

    @Override
    public boolean removeDistinct() {
        Matcher matcher = SELECT_DISTINCT_PATTERN.matcher(buffer);
        if (matcher.find()) {
            buffer.replace(matcher.start(), matcher.end(), "select");
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public void replaceOrderBy(String property, boolean desc) {
        replaceOrderBy(desc, property);
    }

    @Override
    public void replaceOrderBy(boolean desc, String... properties) {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(buffer);
        String alias = findAlias(entityMatcher);

        Matcher orderByMatcher = ORDER_BY_PATTERN.matcher(buffer);
        if (orderByMatcher.find()) {
            buffer.replace(orderByMatcher.end(), buffer.length(), "");
        } else {
            buffer.append(" order by");
        }

        String separator = " ";
        for (String property : properties) {
            int dotPos = property.lastIndexOf(".");
            if (dotPos > -1) {
                String path = property.substring(0, dotPos);
                String joinedAlias = alias + "_" + path.replace(".", "_");
                if (buffer.indexOf(" " + joinedAlias) == -1) {
                    String join = "left join " + alias + "." + path + " " + joinedAlias;
                    addJoinAsIs(join);
                }

                String orderBy = joinedAlias + "." + property.substring(dotPos + 1) + (desc ? " desc" : "");
                buffer.append(separator).append(orderBy);
            } else {
                String orderBy = alias + "." + property + (desc ? " desc" : "");
                buffer.append(separator).append(orderBy);
            }
            separator = ", ";
        }
    }

    @Override
    public void removeOrderBy() {
        Matcher matcher = ORDER_BY_PATTERN.matcher(buffer);
        if (matcher.find()) {
            buffer.delete(matcher.start(), buffer.length());
        }
    }

    @Override
    public void replaceEntityName(String newName) {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(buffer);
        if (entityMatcher.find()) {
            buffer.replace(entityMatcher.start(FEP_ENTITY), entityMatcher.end(FEP_ENTITY), newName);
            return;
        }
        error("Unable to find entity name");
    }

    @Override
    public void reset() {
        buffer = new StringBuffer(source);
        addedParams.clear();
    }

    @Override
    public String getResult() {
        return buffer.toString().trim();
    }

    @Override
    public Set<String> getAddedParams() {
        return Collections.unmodifiableSet(addedParams);
    }

    @Override
    public void handleCaseInsensitiveParam(String paramName) {
        Pattern pattern = Pattern.compile(COND_PATTERN_REGEX + ":" + paramName, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(buffer);
        if (matcher.find()) {
            String field = matcher.group(1);
            buffer.replace(matcher.start(1), matcher.end(1), "lower(" + field + ")");
        }
    }

    private String findAlias(Matcher entityMatcher) {
        String alias = null;
        if (entityMatcher.find()) {
            alias = entityMatcher.group(FEP_ALIAS);
        }
        if (StringUtils.isBlank(alias))
            error("Unable to find entity alias");
        return alias;
    }

    private Matcher findReturnedEntityDeclaration() {
        Matcher firstAliasMatcher = QUERY_START_PATTERN.matcher(buffer);
        String firstAlias = null;
        if (firstAliasMatcher.find()) {
            firstAlias = firstAliasMatcher.group(QS_ALIAS);
        }

        Matcher entityMatcher = ENTITY_PATTERN.matcher(buffer);
        String alias = null;
        while (entityMatcher.find()) {
            String matchedAlias = entityMatcher.group(EP_ALIAS);
            if (matchedAlias.equalsIgnoreCase(firstAlias)) {
                alias = matchedAlias;
                break;
            }
        }

        if (StringUtils.isBlank(alias)) {
            error("Unable to find entity alias");
        }

        return entityMatcher;
    }

    private void error(String message) {
        throw new RuntimeException(message + " [" + buffer.toString() + "]");
    }
}