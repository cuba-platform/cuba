/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import org.eclipse.persistence.expressions.ExpressionOperator;
import org.eclipse.persistence.internal.expressions.ExpressionSQLPrinter;
import org.eclipse.persistence.internal.expressions.QueryKeyExpression;
import org.eclipse.persistence.internal.helper.ClassConstants;

import java.io.IOException;
import java.util.Vector;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaIsNullExpressionOperator extends ExpressionOperator {

    public CubaIsNullExpressionOperator() {
        setType(ExpressionOperator.ComparisonOperator);
        setSelector(ExpressionOperator.IsNull);
        Vector v = org.eclipse.persistence.internal.helper.NonSynchronizedVector.newInstance();
        v.add("(");
        v.add(" IS NULL)");
        printsAs(v);
        bePrefix();
        printsJavaAs(".isNull()");
        setNodeClass(ClassConstants.FunctionExpression_Class);
    }

    @Override
    public void printCollection(Vector items, ExpressionSQLPrinter printer) {
        if (items.size() == 1
                && items.get(0) instanceof QueryKeyExpression
                && "deleteTs".equals(((QueryKeyExpression) items.get(0)).getName())) {
            if (printer.getSession() != null) {
                if (Boolean.TRUE.equals(printer.getSession().getProperties().get("cuba.disableSoftDelete"))) {
                    try {
                        printer.getWriter().write("(0=0)");
                        return;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        super.printCollection(items, printer);
    }
}
