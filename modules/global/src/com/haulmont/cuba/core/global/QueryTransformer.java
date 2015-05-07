/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import java.util.Set;

/**
 * Transforms JPQL query
 *
 * @author krivopustov
 * @version $Id$
 */
public interface QueryTransformer {

    /** Main entity alias placeholder  */
    String ALIAS_PLACEHOLDER = "{E}";

    /** Adds 'where' clause replacing entity alias */
    void addWhere(String where);

    /** Adds 'where' clause */
    void addWhereAsIs(String where);

    /** Adds 'join' clause */
    void addJoinAsIs(String join);

    /** Adds 'join' and 'where' clauses. Replaces alias in 'join' but doesn't replace it in 'where'*/
    void addJoinAndWhere(String join, String where);

    /** Adds 'where' clause from the query provided. Replaces entity alias */
    void mergeWhere(String query);

    /** Replaces <code>select e.f1, e.f2, ...</code> clause with <code>select count(e) ...</code> */
    void replaceWithCount();

    /** Replaces <code>select e from ...</code> clause with <code>select e.id from ...</code> */
    void replaceWithSelectId();

    /**
     * Replaces 'select distinct' with 'select'.
     * @return  true if 'distinct' was really removed, false if there was no 'distinct' in the query
     */
    boolean removeDistinct();

    /** DEPRECATED: use {@link #replaceOrderBy(boolean, String...)} */
    @Deprecated
    void replaceOrderBy(String property, boolean desc);

    /** Adds or replaces 'order by' clause */
    void replaceOrderBy(boolean desc, String... properties);

    /** Removes 'order by' clause */
    void removeOrderBy();

    /** Replace main entity name, e.g. "select d ref$Driver d" with "select d ref$ExtDriver d" */
    void replaceEntityName(String newName);

    /** Reset internal buffer */
    void reset();

    /** Get buffer */
    String getResult();

    /** Get parameter names found during transformation */
    Set<String> getAddedParams();
}
