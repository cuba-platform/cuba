/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Implementation of {@link QueryTransformer} based on regular expressions.
 *
 * @author krivopustov
 * @version $Id$
 */
public class QueryTransformerRegex extends QueryParserRegex implements QueryTransformer
{
    private StringBuffer buffer;
    private Set<String> addedParams;

    QueryTransformerRegex(String source, String targetEntity) {
        super(source);
        buffer = new StringBuffer(source);
        addedParams = new HashSet<String>();
    }

    public void addWhere(String where) {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(buffer);
        String alias = findAlias(entityMatcher);

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

        if (where.contains(ALIAS_PLACEHOLDER)) {
            // replace ALIAS_PLACEHOLDER
            sb.append(where);
            int idx;
            while ((idx = sb.indexOf(ALIAS_PLACEHOLDER)) >= 0) {
                sb.replace(idx, idx + ALIAS_PLACEHOLDER.length(), alias);
            }
        } else {
            Set<String> subqueryEntityAliases = new HashSet<String>();
            Matcher subqueryEntityMatcher = ENTITY_PATTERN.matcher(where);
            while (subqueryEntityMatcher.find()) {
                subqueryEntityAliases.add(subqueryEntityMatcher.group(3));
            }
            sb.append(replaceEntityAliases(where, alias, subqueryEntityAliases));
        }

        buffer.insert(insertPos, sb);

        Matcher paramMatcher = PARAM_PATTERN.matcher(where);
        while (paramMatcher.find()) {
            addedParams.add(paramMatcher.group(1));
        }
    }

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

    public void addJoinAsIs(String join) {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(buffer);
        findAlias(entityMatcher);

        int insertPos = buffer.length();

        Matcher whereMatcher = WHERE_PATTERN.matcher(buffer);
        if (whereMatcher.find(entityMatcher.end())) {
            insertPos = whereMatcher.start() - 1;
        } else {
            Matcher lastClauseMatcher = LAST_CLAUSE_PATTERN.matcher(buffer);
            if (lastClauseMatcher.find(entityMatcher.end()))
                insertPos = lastClauseMatcher.start() - 1;
        }

        buffer.insert(insertPos, " ");
        insertPos++;

        buffer.insert(insertPos, join);

        Matcher paramMatcher = PARAM_PATTERN.matcher(join);
        while (paramMatcher.find()) {
            addedParams.add(paramMatcher.group(1));
        }
    }

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

        Set<String> joinEntityAliases = new HashSet<String>();
        if (!StringUtils.isBlank(join)) {
            buffer.insert(insertPos, " ");
            insertPos++;
            int joinLen;
            if (join.contains(ALIAS_PLACEHOLDER)) {
                joinLen = join.length();
                buffer.insert(insertPos, join);
            } else {
                joinLen = insertReplacingAlias(buffer, insertPos, join, alias);
            }
            insertPos += joinLen;

            Matcher paramMatcher = PARAM_PATTERN.matcher(join);
            while (paramMatcher.find()) {
                addedParams.add(paramMatcher.group(1));
            }

            Matcher joinEntityAliasMatcher = JOIN_ALIAS_PATTERN.matcher(join);
            while (joinEntityAliasMatcher.find()) {
                joinEntityAliases.add(joinEntityAliasMatcher.group(3));
            }

        }
        if (!StringUtils.isBlank(where)) {
            StringBuilder sb = new StringBuilder();
            whereMatcher = WHERE_PATTERN.matcher(buffer);
            if (whereMatcher.find(entityMatcher.end()))
                sb.append(" and ");
            else
                sb.append(" where ");

            if (where.contains(ALIAS_PLACEHOLDER)) {
                // replace ALIAS_PLACEHOLDER
                sb.append(where);
            } else {
                Set<String> subqueryEntityAliases = new HashSet<String>();
                Matcher subqueryEntityMatcher = ENTITY_PATTERN.matcher(where);
                while (subqueryEntityMatcher.find()) {
                    subqueryEntityAliases.add(subqueryEntityMatcher.group(3));
                }

                Set<String> excludedAliases = new HashSet<String>();
                excludedAliases.addAll(subqueryEntityAliases);
                excludedAliases.addAll(joinEntityAliases);
                sb.append(replaceEntityAliases(where, alias, excludedAliases));
            }

            insertPos = buffer.length();
            Matcher lastClauseMatcher = LAST_CLAUSE_PATTERN.matcher(buffer);
            if (lastClauseMatcher.find(entityMatcher.end()))
                insertPos = lastClauseMatcher.start() - 1;

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

    private int insertReplacingAlias(StringBuffer sb, int insertPos, String clause, String alias) {
        Matcher matcher = ALIAS_PATTERN.matcher(clause);
        int pos = 0;
        while (matcher.find()) {
            sb.insert(insertPos, clause.substring(pos, matcher.start(2)));
            insertPos += clause.substring(pos, matcher.start(2)).length();
            pos = matcher.end(2);
            sb.insert(insertPos, alias);
            insertPos += alias.length();
        }
        sb.insert(insertPos, clause.substring(pos));
        return alias.length() + clause.substring(pos).length();
    }

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
                "select "+ (distinctMatcher.find() ? "distinct " : "") + alias + ".id ");

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

    public void replaceOrderBy(String property, boolean desc) {
        Matcher entityMatcher = FROM_ENTITY_PATTERN.matcher(buffer);
        String alias = findAlias(entityMatcher);

        int dotPos = property.lastIndexOf(".");
        if (dotPos > -1) {
            String path = property.substring(0, dotPos);
            String joinedAlias = alias + "_" + path.replace(".", "_");
            if (buffer.indexOf(" " + joinedAlias) == -1) {
                String join = "left join " + alias + "." + path + " " + joinedAlias;
                addJoinAsIs(join);
            }

            String orderBy = joinedAlias + "." + property.substring(dotPos + 1) + (desc ? " desc" : "");
            Matcher matcher = ORDER_BY_PATTERN.matcher(buffer);
            if (matcher.find()) {
                buffer.replace(matcher.end(), buffer.length(), " " + orderBy);
            } else {
                buffer.append(" order by ").append(orderBy);
            }
        } else {
            String orderBy = alias + "." + property + (desc ? " desc" : "");
            Matcher matcher = ORDER_BY_PATTERN.matcher(buffer);
            if (matcher.find()) {
                buffer.replace(matcher.end(), buffer.length(), " " + orderBy);
            } else {
                buffer.append(" order by ").append(orderBy);
            }
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

    public void reset() {
        buffer = new StringBuffer(source);
        addedParams.clear();
    }

    public String getResult() {
        return buffer.toString().trim();
    }

    public Set<String> getAddedParams() {
        return Collections.unmodifiableSet(addedParams);
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

    private void error(String message) {
        throw new RuntimeException(message + " [" + buffer.toString() + "]");
    }

    private String replaceEntityAliases(String clause, String alias, Collection<String> excludedAliases) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = ALIAS_PATTERN.matcher(clause);
        int pos = 0;
        while (matcher.find()) {
            if (excludedAliases.contains(matcher.group(2))) {
                continue;
            }
            sb.append(clause.substring(pos, matcher.start(2)));
            pos = matcher.end(2);
            sb.append(alias);
        }
        sb.append(clause.substring(pos));
        return sb.toString();
    }
}
