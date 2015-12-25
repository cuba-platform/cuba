/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class ErrorNodesFinder implements TreeVisitorAction {
    protected List<CommonErrorNode> errorNodes = new ArrayList<>();

    @Override
    public Object pre(Object t) {
        if (t instanceof CommonErrorNode) {
            errorNodes.add((CommonErrorNode) t);
            return t;
        }
        return t;
    }

    @Override
    public Object post(Object t) {
        return t;
    }

    public List<CommonErrorNode> getErrorNodes() {
        return Collections.unmodifiableList(errorNodes);
    }
}
