/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.entity.Entity;

import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

import static com.haulmont.cuba.gui.components.filter.Op.*;

/**
 * @author gorbunkov
 * @version $Id$
 */
@Component(OpManager.NAME)
public class OpManagerImpl implements OpManager {

    @Override
    public EnumSet<Op> availableOps(Class javaClass) {
        if (String.class.equals(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, CONTAINS, DOES_NOT_CONTAIN, NOT_EMPTY, STARTS_WITH, ENDS_WITH);

        else if (Date.class.isAssignableFrom(javaClass)
                || Number.class.isAssignableFrom(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESSER, LESSER_OR_EQUAL, NOT_EMPTY);

        else if (Boolean.class.equals(javaClass))
            return EnumSet.of(EQUAL, NOT_EQUAL, NOT_EMPTY);

        else if (UUID.class.equals(javaClass)
                || Enum.class.isAssignableFrom(javaClass)
                || Entity.class.isAssignableFrom(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, NOT_EMPTY);

        else
            throw new UnsupportedOperationException("Unsupported java class: " + javaClass);
    }
}