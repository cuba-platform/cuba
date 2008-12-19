/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 12:42:44
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.utils.PrintUtils;
import com.haulmont.cuba.core.global.MetadataProvider;

import java.util.Collection;

public class MetadataProviderTest extends CubaTestCase
{
    public void test() {
        Session session = MetadataProvider.getSession();
        assertNotNull(session);

        Collection<MetaModel> models = session.getModels();
        for (MetaModel model : models) {
            System.out.println("Model: " + model.getName());
            System.out.println(PrintUtils.printClassHierarchy(model));
        }
    }
}
