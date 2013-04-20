/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.util

import junit.framework.TestCase

/**
 * @author artamonov
 * @version $Id$
 */
class ViewNodeBuilderTest extends TestCase {

    public void testSimpleView() {
        def builder = new ViewNodeBuilder()

        def viewDefinition = builder.view(entity: 'sec$UserSubstitution', extends: 'user.edit') {
            property(name: 'substitutedUser') {
                property(name: 'name')
                property(name: 'login')
            }
            property(name: 'startDate')
            property(name: 'endDate')
        }

        assertNotNull(viewDefinition)
    }
}