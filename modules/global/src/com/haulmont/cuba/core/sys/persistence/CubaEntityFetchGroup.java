/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.global.IllegalEntityStateException;
import org.eclipse.persistence.internal.localization.ExceptionLocalization;
import org.eclipse.persistence.internal.queries.EntityFetchGroup;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaEntityFetchGroup extends EntityFetchGroup {

    public CubaEntityFetchGroup(FetchGroup fetchGroup) {
        super(fetchGroup);
    }

    @Override
    public String onUnfetchedAttribute(FetchGroupTracker entity, String attributeName) {
        if (attributeName == null && entity._persistence_getSession() != null) { // occurs on merge
            return super.onUnfetchedAttribute(entity, null);
        }
        throw new IllegalEntityStateException(ExceptionLocalization.buildMessage("cannot_get_unfetched_attribute", new Object[]{entity, attributeName}));
    }
}