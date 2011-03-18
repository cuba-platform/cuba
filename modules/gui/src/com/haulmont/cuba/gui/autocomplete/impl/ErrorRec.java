package com.haulmont.cuba.jpql.impl;

import org.antlr.runtime.tree.CommonTree;

/**
 * Author: Alexander Chevelev
* Date: 01.11.2010
* Time: 21:39:52
*/
public class ErrorRec {
    public final CommonTree node;
    public final String message;

    public ErrorRec(CommonTree node, String message) {
        this.node = node;
        this.message = message;
    }
}
