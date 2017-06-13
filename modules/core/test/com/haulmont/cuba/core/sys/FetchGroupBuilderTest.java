package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.View;
import org.eclipse.persistence.queries.FetchGroup;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * Created by ikuchmin on 05.12.16.
 */
public class FetchGroupBuilderTest {

    @Test
    public void shouldReturnListOfKeysForSimpleView() throws Exception {
        View view = new View(A.class);
        view.addProperty("f1");
        view.addProperty("f2");
        view.addProperty("f3");

        FetchGroup expected = new FetchGroup();
        expected.addAttributes(asList("f1","f2","f3"));
        assertEquals(expected, new FetchGroupBuilder(asList(view)).build());
    }

    @Test
    public void shouldReturnListOfKeysForSimpleHierarchy() throws Exception {
        View viewA = new View(A.class);
        viewA.addProperty("af1");
        viewA.addProperty("af2");
        viewA.addProperty("af3");

        View viewB = new View(B.class);
        viewB.addProperty("bf1");
        viewB.addProperty("bf2");
        viewB.addProperty("bf3");

        viewA.addProperty("bf", viewB);

        FetchGroup expected = new FetchGroup();
        expected.addAttributes(asList("af1","af2","af3", "bf.bf1", "bf.bf2", "bf.bf3"));
        assertEquals(expected, new FetchGroupBuilder(asList(viewA)).build());
    }

    @Test
    public void shouldCorrectResolveCircleTypeHierarchy() throws Exception {
        View viewA = new View(A.class).addProperty("af1")
                                      .addProperty("af2")
                                      .addProperty("af3");

        View innerViewA = new View(A.class).addProperty("af1")
                                           .addProperty("af2")
                                           .addProperty("af3");

        View twoDeepInnerViewA = new View(A.class).addProperty("af1")
                                                  .addProperty("af2")
                                                  .addProperty("af3");

        innerViewA.addProperty("af", twoDeepInnerViewA);
        viewA.addProperty("af", innerViewA);

        FetchGroup expected = new FetchGroup();
        expected.addAttributes(asList("af1","af2","af3", "af.af1", "af.af2", "af.af3", "af.af.af1", "af.af.af2", "af.af.af3"));
        assertEquals(expected, new FetchGroupBuilder(asList(viewA)).build());
    }

    @Test(expected = CycleException.class) // TODO: We should check view graph on it is acyclic when it saves in db
    public void shouldCorrectResolveWhenDeveloperMadeCircle() throws Exception {
        View viewA = new View(A.class, "viewA").addProperty("af1")
                                               .addProperty("af2")
                                               .addProperty("af3");

        viewA.addProperty("af", viewA);

        new FetchGroupBuilder(asList(viewA)).build();
    }

    @Test
    public void shouldUnionFieldsInViewWhichHasSameFields() throws Exception {
        View viewA = new View(A.class, "viewA").addProperty("af1")
                                               .addProperty("af2")
                                               .addProperty("af3");

        FetchGroup expected = new FetchGroup();
        expected.addAttributes(asList("af1","af2","af3"));
        assertEquals(expected, new FetchGroupBuilder(asList(viewA, viewA)).build());
    }

    static class A extends StandardEntity {

    }

    static class B extends StandardEntity {

    }
}