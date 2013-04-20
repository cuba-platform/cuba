/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.util

import com.haulmont.cuba.core.CubaTestCase
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.core.util.ViewBuilder

/**
 * @author artamonov
 * @version $Id$
 */
class ViewBuilderTest extends CubaTestCase {

    ViewBuilder builder

    @Override
    protected void setUp() throws Exception {
        super.setUp()

        builder = new ViewBuilder()
    }

    public void testSimpleViewDefinition() {
        View view1 = builder.view(class: 'com.haulmont.cuba.security.entity.Group',
                name: 'group.edit.test', extends: 'group.browse') {
            property(name: 'constraints', view: '_local')
            property(name: 'sessionAttributes', view: '_local')
        }

        assertNotNull(view1)
        assertEquals(4, view1.properties.size())
    }

    public void testDeepViewDefinition() {
        View view2 = builder.view(entity: 'sec$Group', name: 'group.edit.test', extends: 'group.browse') {
            property(name: 'constraints', view: 'group.browse')
            property(name: 'sessionAttributes', view: '_local')
            property(name: 'parent', view: '_local') {
                property(name: 'parent') {
                    property(name: 'name')
                }
            }
        }

        assertNotNull(view2)
        assertEquals(4, view2.properties.size())
        assertEquals(2, view2.getProperty('parent').getView().properties.size())
        assertEquals(1, view2.getProperty('parent').getView().getProperty('parent').getView().properties.size())
    }
}