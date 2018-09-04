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
import com.haulmont.cuba.gui.screen.MapScreenOptions;
import com.haulmont.cuba.gui.screen.MessageBundle;
import com.haulmont.cuba.gui.sys.UiControllerDependencyInjector;
import com.haulmont.cuba.gui.sys.UiControllerReflectionInspector;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.HashMap;

import static com.haulmont.cuba.gui.screen.FrameOwner.NO_OPTIONS;
import static org.junit.Assert.assertSame;

@SuppressWarnings("IncorrectCreateGuiComponent")
public class ControllerDependencyInjectorTest extends CubaClientTestCase {

    @Mocked
    BeanLocator beanLocator;
    private Messages messages = new MessagesClientImpl();
    @SuppressWarnings("unused")
    @Mocked
    private MessageBundle messageBundle;

    @Before
    public void setUp() {
        setupInfrastructure();
        new Expectations() {
            {
                beanLocator.getAll(Messages.class);
                result = ImmutableMap.of(Messages.NAME, messages);
                minTimes = 0;

                beanLocator.getPrototype(MessageBundle.NAME);
                result = messageBundle;
                minTimes = 0;
            }
        };
    }

    @Test
    public void testInjectMessagesIntoAbstractFrame() throws Exception {
        TestController controller = new TestController();
        UiControllerDependencyInjector injector = new UiControllerDependencyInjector(controller, NO_OPTIONS);
        injector.setReflectionInspector(new UiControllerReflectionInspector());
        injector.setBeanLocator(beanLocator);

        injector.inject();
        assertSame(controller.messages, messages);

        Field field = AbstractWindow.class.getDeclaredField("messages");
        field.setAccessible(true);

        assertSame(field.get(controller), messages);
    }

    @Test
    public void testInjectWindowParamsIntoAbstractFrame() {
        HashMap<String, Object> testMap = new HashMap<>();
        testMap.put("someObj", new Object());
        WindowParamTestController controller = new WindowParamTestController();

        UiControllerDependencyInjector injector = new UiControllerDependencyInjector(controller, new MapScreenOptions(testMap));
        injector.setReflectionInspector(new UiControllerReflectionInspector());
        injector.setBeanLocator(beanLocator);
        injector.inject();

        assertSame(controller.someObj, testMap.get("someObj"));
    }

    private class TestController extends AbstractWindow {

        @Inject
        protected Messages messages;
    }

    private class WindowParamTestController extends AbstractWindow {

        @WindowParam(name = "someObj", required = true)
        public Object someObj;
    }
}