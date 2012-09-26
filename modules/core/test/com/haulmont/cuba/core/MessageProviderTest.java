/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 06.03.2009 12:30:48
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.mp_test.MpTestObj;
import com.haulmont.cuba.core.mp_test.nested.MpTestNestedObj;
import com.haulmont.cuba.core.mp_test.nested.MpTestNestedEnum;

public class MessageProviderTest extends CubaTestCase
{
    public void test() {
        String msg = MessageProvider.getMessage(MpTestNestedObj.class, "key0");
        assertEquals("Message0", msg);

        msg = MessageProvider.getMessage(MpTestObj.class, "key1");
        assertEquals("Message1", msg);

        msg = MessageProvider.getMessage(MpTestNestedObj.class, "key2");
        assertEquals("Message2", msg);

        // test cache
        msg = MessageProvider.getMessage(MpTestNestedObj.class, "key0");
        assertEquals("Message0", msg);

        msg = MessageProvider.getMessage("com.haulmont.cuba.core.mp_test.nested", "key1");
        assertEquals("Message1", msg);

        msg = MessageProvider.getMessage("test", "key1");
        assertEquals("key1", msg);

        msg = MessageProvider.getMessage(MpTestNestedEnum.ONE);
        assertEquals("One", msg);

        msg = MessageProvider.getMessage(MpTestNestedObj.InternalEnum.FIRST);
        assertEquals("First", msg);

    }

    public void testInclude() {
        String msg = MessageProvider.getMessage("com.haulmont.cuba.core.mp_test", "includedMsg");
        assertEquals("Included Message", msg);

        msg = MessageProvider.getMessage("com.haulmont.cuba.core.mp_test", "includedMsgToOverride");
        assertEquals("Overridden Included Message", msg);
    }

    public void testMultiInclude() {
        String msg1 = MessageProvider.getMessage("com.haulmont.cuba.core.mp_test.includes", "oneKey");
        assertEquals(msg1, "OK");

        String msg2 = MessageProvider.getMessage("com.haulmont.cuba.core.mp_test.includes", "twoKey");
        assertEquals(msg2, "OK");
    }
}
