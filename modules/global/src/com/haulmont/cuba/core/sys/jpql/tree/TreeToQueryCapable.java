package com.haulmont.cuba.core.sys.jpql.tree;

import com.haulmont.cuba.core.sys.jpql.ErrorRec;
import com.haulmont.cuba.core.sys.jpql.QueryBuilder;
import org.antlr.runtime.tree.CommonTree;

import java.util.List;

/**
 * Author: Alexander Chevelev
 * Date: 26.03.2011
 * Time: 3:16:12
 */
public interface TreeToQueryCapable {

    CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes);

    CommonTree treeToQueryPost(QueryBuilder sb, List<ErrorRec> invalidNodes);
}
