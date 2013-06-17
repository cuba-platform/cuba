/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.client.sys.MessagesClientImpl;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractWindow;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.Collections;

import static junit.framework.Assert.assertTrue;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ControllerDependencyInjectorTest extends CubaClientTestCase {

    private Messages messages = new MessagesClientImpl();

    @Before
    public void setUp() throws Exception {
        setupInfrastructure();
        new NonStrictExpectations() {
            @Mocked ApplicationContext applicationContext;
            {
                AppContext.getApplicationContext();
                result = applicationContext;

                applicationContext.getBeansOfType(Messages.class, true, true);
                result = Collections.singletonMap(Messages.NAME, messages);
            }
        };
    }

    @Test
    public void testInjectMessagesIntoAbstractFrame() throws Exception {
        TestController controller = new TestController();
        ControllerDependencyInjector injector = new ControllerDependencyInjector(controller);
        injector.inject();
        assertTrue(controller.messages == messages);

        Field field = AbstractFrame.class.getDeclaredField("messages");
        field.setAccessible(true);
        assertTrue(field.get(controller) == messages);
    }

    private class TestController extends AbstractWindow {

        @Inject
        protected Messages messages;
    }
}
