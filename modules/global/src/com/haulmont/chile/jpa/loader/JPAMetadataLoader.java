/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.jpa.loader;

import com.haulmont.chile.core.loader.ChileMetadataLoader;
import com.haulmont.chile.core.loader.ClassMetadataLoader;
import com.haulmont.chile.core.model.Session;

public class JPAMetadataLoader extends ChileMetadataLoader {

    public JPAMetadataLoader(Session session) {
        super(session);
    }

    @Override
    protected ClassMetadataLoader createAnnotationsLoader(Session session) {
        return new JPAAnnotationsLoader(session);
    }
}
