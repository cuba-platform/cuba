/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
import java.util.HashMap;

import static junit.framework.Assert.assertTrue;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ControllerDependencyInjectorTest extends CubaClientTestCase {

    private Messages messages = new MessagesClientImpl();

    @Mocked ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        setupInfrastructure();
        new NonStrictExpectations() {
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
        ControllerDependencyInjector injector = new ControllerDependencyInjector(controller, new HashMap<String,Object>());
        injector.inject();
        assertTrue(controller.messages == messages);

        Field field = AbstractFrame.class.getDeclaredField("messages");
        field.setAccessible(true);
        assertTrue(field.get(controller) == messages);
    }

    @Test
    public void testInjectWindowParamsIntoAbstactFrame() throws Exception {
        HashMap<String,Object> testMap = new HashMap<>();
        testMap.put("someObj",new Object());
        WindowParamTestController controller = new WindowParamTestController();
        ControllerDependencyInjector injector = new ControllerDependencyInjector(controller,testMap);
        injector.inject();

        assertTrue(controller.someObj == testMap.get("someObj"));
    }

    private class TestController extends AbstractWindow {

        @Inject
        protected Messages messages;
    }

    private class WindowParamTestController extends AbstractWindow {

        @WindowParam(name = "someObj",required = true)
        public Object someObj;
    }
}
