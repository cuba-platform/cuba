/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.09.2009 11:30:46
 *
 * $Id$
 */
package com.haulmont.cuba.gui.filter;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public abstract class Condition implements Cloneable {

    public Condition copy() {
        try {
            return (Condition) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract List<Condition> getConditions();
    public abstract void setConditions(List<Condition> conditions);

    public abstract String getContent();

    public abstract Set<String> getParameters();

    public abstract Set<String> getJoins();
}
