/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.tree.Tree;

/**
 * @author chevelev
 * @version $Id$
 */
public interface EntityReference {
    String replaceEntries(String queryPart, String replaceablePart);

    void renameVariableIn(PathNode node);

    Tree createNode();

    boolean isJoinableTo(IdentificationVariableNode node);

    PathEntityReference addFieldPath(String fieldPath);
}
