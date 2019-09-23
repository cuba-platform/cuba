/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.components.actions.ListAction;
import com.haulmont.cuba.gui.xml.layout.loaders.ActionCustomPropertyLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ActionCustomPropertyLoaderTest {

    private ActionCustomPropertyLoader loader;
    private TestAction action;

    public enum TestEnum {
        ONE,
        TWO
    }

    public static class TestAction extends ListAction {

        private Boolean booleanProp;

        private TestEnum enumProp;

        private Class classProp;

        private List<String> listOfStrings;

        private List<Integer> listOfIntegers;

        public TestAction(String id) {
            super(id);
        }

        public List<String> getListOfStrings() {
            return listOfStrings;
        }

        public void setListOfStrings(List<String> listOfStrings) {
            this.listOfStrings = listOfStrings;
        }

        public List<Integer> getListOfIntegers() {
            return listOfIntegers;
        }

        public void setListOfIntegers(List<Integer> listOfIntegers) {
            this.listOfIntegers = listOfIntegers;
        }

        public Boolean getBooleanProp() {
            return booleanProp;
        }

        public void setBooleanProp(Boolean booleanProp) {
            this.booleanProp = booleanProp;
        }

        public TestEnum getEnumProp() {
            return enumProp;
        }

        public void setEnumProp(TestEnum enumProp) {
            this.enumProp = enumProp;
        }

        public Class getClassProp() {
            return classProp;
        }

        public void setClassProp(Class classProp) {
            this.classProp = classProp;
        }
    }

    @Before
    public void setUp() throws Exception {
        loader = new ActionCustomPropertyLoader();
        action = new TestAction("test");
    }

    @Test
    public void testBoolean() {
        loader.load(action, "booleanProp", "true");
        assertEquals(true, action.getBooleanProp());

        loader.load(action, "booleanProp", "false");
        assertEquals(false, action.getBooleanProp());

        loader.load(action, "booleanProp", "abc");
        assertEquals(false, action.getBooleanProp());
    }

    @Test
    public void testEnum() {
        loader.load(action, "enumProp", "ONE");
        assertEquals(TestEnum.ONE, action.getEnumProp());

        loader.load(action, "enumProp", "TWO");
        assertEquals(TestEnum.TWO, action.getEnumProp());

        loader.load(action, "enumProp", "NONEXISTENT");
        assertNull(action.getEnumProp());
    }

    @Test
    public void testClass() {
        loader.load(action, "classProp", "com.haulmont.cuba.gui.xml.ActionCustomPropertyLoaderTest");
        assertEquals(ActionCustomPropertyLoaderTest.class, action.getClassProp());

        action.setClassProp(null);
        try {
            loader.load(action, "classProp", "foo");
            fail();
        } catch (Exception e) {
            assertNull(action.getClassProp());
        }
    }

    @Test
    public void testList() {
        loader.load(action, "listOfStrings", "aaa,bbb,ccc");
        assertEquals(Arrays.asList("aaa", "bbb", "ccc"), action.getListOfStrings());

        loader.load(action, "listOfIntegers", "1,2,3");
        assertEquals(Arrays.asList(1, 2, 3), action.getListOfIntegers());

        action.setListOfIntegers(null);
        try {
            loader.load(action, "listOfIntegers", "a,b,c");
            fail();
        } catch (Exception e) {
            assertNull(action.getListOfIntegers());
        }
    }
}
