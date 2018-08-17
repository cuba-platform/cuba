/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.client.sys.MessagesClientImpl;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.sys.ControllerDependencyInjector;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.Assert.assertSame;

@SuppressWarnings("IncorrectCreateGuiComponent")
public class ControllerDependencyInjectorTest extends CubaClientTestCase {

    private Messages messages = new MessagesClientImpl();

    @Mocked
    BeanLocator beanLocator;

    @Before
    public void setUp() {
        setupInfrastructure();
        new Expectations() {
            {
                beanLocator.getAll(Messages.class);
                result = ImmutableMap.of(Messages.NAME, messages); minTimes = 0;
            }
        };
    }

    @Test
    public void testInjectMessagesIntoAbstractFrame() throws Exception {
        TestController controller = new TestController();
        ControllerDependencyInjector injector = new ControllerDependencyInjector(controller, new HashMap<>());
        injector.setBeanLocator(beanLocator);

        injector.inject();
        assertSame(controller.messages, messages);

        Field field = AbstractWindow.class.getDeclaredField("messages");
        field.setAccessible(true);

        assertSame(field.get(controller), messages);
    }

    @Test
    public void testInjectWindowParamsIntoAbstractFrame() {
        HashMap<String,Object> testMap = new HashMap<>();
        testMap.put("someObj",new Object());
        WindowParamTestController controller = new WindowParamTestController();
        ControllerDependencyInjector injector = new ControllerDependencyInjector(controller, testMap);
        injector.setBeanLocator(beanLocator);
        injector.inject();

        assertSame(controller.someObj, testMap.get("someObj"));
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