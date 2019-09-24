package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.global.LoadContext.Query;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LoadContextQueryTest {

    @Test
    public void twoQueryShouldBeEqualsIfOneOfCopyFirst() throws Exception {
        Query expected = LoadContext.createQuery("select obj from Object obj where obj.param = :param")
                                    .setParameter("param", "value")
                                    .setFirstResult(100)
                                    .setMaxResults(100);

        assertEquals(expected, expected.copy());
    }

    @Test
    public void twoQueriesShouldBeEqualsIfTheyHaveDifferentValueCacheableField() throws Exception {
        Query expected = LoadContext.createQuery("select obj from Object obj where obj.param = :param")
                                    .setParameter("param", "value")
                                    .setFirstResult(100)
                                    .setMaxResults(100)
                                    .setCacheable(true);

        Query actual = expected.copy().setCacheable(false);

        assertEquals(expected, actual);
    }

    @Test
    public void inOtherCasesQueriesAreNotEquals() throws Exception {
        Query expected = LoadContext.createQuery("select obj from Object obj where obj.param = :param")
                                    .setParameter("param", "value")
                                    .setFirstResult(100)
                                    .setMaxResults(100)
                                    .setCacheable(true);

        assertNotEquals(expected, expected.copy().setQueryString("other query"));

        assertNotEquals(expected, expected.copy().setParameter("param", "newValue"));

        assertNotEquals(expected, expected.copy().setFirstResult(0));

        assertNotEquals(expected, expected.copy().setMaxResults(10));

    }
}