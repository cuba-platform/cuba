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

package com.haulmont.cuba.core.sys.persistence;

import org.eclipse.persistence.cuba.CubaUtil;
import org.eclipse.persistence.expressions.ExpressionOperator;
import org.eclipse.persistence.internal.expressions.ExpressionSQLPrinter;
import org.eclipse.persistence.internal.expressions.QueryKeyExpression;
import org.eclipse.persistence.internal.helper.ClassConstants;

import java.io.IOException;
import java.util.Vector;

/**
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
                if (CubaUtil.isSoftDeleteDisabled(printer.getSession())) {
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
