package com.haulmont.cuba.namepattern;

import com.haulmont.cuba.testmodel.namepattern.MethodNamePatternEntity;
import com.haulmont.cuba.testmodel.namepattern.SpelNamePatternEntity;
import com.haulmont.cuba.testmodel.namepattern.SimpleNamePatternEntity;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Rushan Zagidullin
 * @since 17.07.2018
 */
public class NamePatternTest {

    @ClassRule
    public static TestContainer container = TestContainer.Common.INSTANCE;

    SimpleNamePatternEntity simpleNamePatternEntity1;
    SimpleNamePatternEntity simpleNamePatternEntity2;
    MethodNamePatternEntity methodNamePatternEntity1;
    MethodNamePatternEntity methodNamePatternEntity2;
    SpelNamePatternEntity spelNamePatternEntity1;
    SpelNamePatternEntity spelNamePatternEntity2;

    @SuppressWarnings("IncorrectCreateEntity")
    @Before
    public void setUp() {
        simpleNamePatternEntity1 = new SimpleNamePatternEntity();
        simpleNamePatternEntity1.setName("name1");

        simpleNamePatternEntity2 = new SimpleNamePatternEntity();
        simpleNamePatternEntity2.setName(null);

        methodNamePatternEntity1 = new MethodNamePatternEntity();
        methodNamePatternEntity1.setName("name1");

        methodNamePatternEntity2 = new MethodNamePatternEntity();
        methodNamePatternEntity2.setName(null);

        spelNamePatternEntity1 = new SpelNamePatternEntity();
        spelNamePatternEntity1.setName("name1");
        spelNamePatternEntity1.setNumber(5);
        spelNamePatternEntity1.setNumber2(null);

        spelNamePatternEntity2 = new SpelNamePatternEntity();
        spelNamePatternEntity2.setName("name2");
        spelNamePatternEntity2.setNumber(null);
        spelNamePatternEntity2.setNumber2(null);
    }

    @Test
    public void testSimpleNamePatternEntity1() {
        assertEquals("name1", simpleNamePatternEntity1.getInstanceName());
    }

    @Test
    public void testSimpleNamePatternEntity2() {
        assertEquals("", simpleNamePatternEntity2.getInstanceName());
    }

    @Test
    public void testMethodNamePatternEntity1() {
        assertEquals("name1", methodNamePatternEntity1.getInstanceName());
    }

    @Test
    public void testMethodNamePatternEntity2() {
        assertNull(methodNamePatternEntity2.getInstanceName());
    }

    @Test
    public void testComplexNamePatternEntity1() {
        assertEquals("Hello, my name is name1. Sum of numbers = 5", spelNamePatternEntity1.getInstanceName());
    }

    @Test
    public void testComplexNamePatternEntity2() {
        assertEquals("Hello, my name is name2. Sum of numbers = 0", spelNamePatternEntity2.getInstanceName());
    }
}
