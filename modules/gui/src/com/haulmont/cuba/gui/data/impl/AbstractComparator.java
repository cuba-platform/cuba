/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 01.03.2010 19:43:25
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import java.util.Comparator;

public abstract class AbstractComparator<T> implements Comparator<T> {
    protected boolean asc;

    protected AbstractComparator(boolean asc) {
        this.asc = asc;
    }

    protected int __compare(Object o1, Object o2) {
        int c;
        if ((o1 instanceof String) && (o2 instanceof String)) {
            c = ((String) o1).compareToIgnoreCase((String) o2);
        } else {
            Comparable c1 = (Comparable) o1;
            Comparable c2 = (Comparable) o2;

            if (c1 != null && c2 != null) {
                c = c1.compareTo(c2);
            } else if (c1 == null) {
                if (c2 != null) {
                    c = 1;
                } else {
                    c = 0;
                }
            } else {
                c = -1;
            }
        }
        return asc ? c : -c;
    }
}
