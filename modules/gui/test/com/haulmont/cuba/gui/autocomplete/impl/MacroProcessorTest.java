/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.autocomplete.impl;

import com.haulmont.cuba.core.sys.jpql.InferredType;
import org.junit.Before;
import org.junit.Test;

import java.util.EnumSet;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Alexander Chevelev
 * Date: 24.03.2011
 * Time: 21:36:09
 */
public class MacroProcessorTest {
    private MacroProcessor processor;

    @Before
    public void initProcessor() {
        processor = new MacroProcessor();
    }

    @Test
    public void inlineFake_between() {
        HintRequest request = processor.inlineFake("select p. from Player p where @between(p.joinDate, now, now+1, day)", 8);
        assertEquals("select p. from Player p where p.joinDate = :d ", request.getQuery());
        assertEquals(8, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Any), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @between(p., now, now+1, day)", 44);
        assertEquals("select p.name from Player p where p. = :d ", request.getQuery());
        assertEquals(35, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());

        try {
            processor.inlineFake("select p.name from Player p where @between(p.joinDate, p., now + 1, day)", 44);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        request = processor.inlineFake("select p.name from Player p where @between(p.joinDate, now, now+1, day) and p.", 77);
        assertEquals("select p.name from Player p where p.joinDate = :d  and p.", request.getQuery());
        assertEquals(56, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Any), request.getExpectedTypes());
    }

    @Test
    public void inlineFake_dateBefore() {
        HintRequest request = processor.inlineFake("select p. from Player p where @dateBefore(p.joinDate, :d)", 8);
        assertEquals("select p. from Player p where p.joinDate = :d ", request.getQuery());
        assertEquals(8, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Any), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @dateBefore(p., :d)", 47);
        assertEquals("select p.name from Player p where p. = :d ", request.getQuery());
        assertEquals(35, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @dateBefore(p.joinDate, p.)", 59);
        assertEquals("select p.name from Player p where p.joinDate = p. ", request.getQuery());
        assertEquals(48, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @dateBefore(p.joinDate, :d) and p.", 67);
        assertEquals("select p.name from Player p where p.joinDate = :d  and p.", request.getQuery());
        assertEquals(56, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Any), request.getExpectedTypes());
    }

    @Test
    public void inlineFake_dateEquals() {
        HintRequest request = processor.inlineFake("select p. from Player p where @dateEquals(p.joinDate, :d)", 8);
        assertEquals("select p. from Player p where p.joinDate = :d ", request.getQuery());
        assertEquals(8, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Any), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @dateEquals(p., :d)", 47);
        assertEquals("select p.name from Player p where p. = :d ", request.getQuery());
        assertEquals(35, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @dateEquals(p.joinDate, p.)", 59);
        assertEquals("select p.name from Player p where p.joinDate = p. ", request.getQuery());
        assertEquals(48, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());
    }

    @Test
    public void inlineFake_dateAfter() {
        HintRequest request = processor.inlineFake("select p. from Player p where @dateAfter(p.joinDate, :d)", 8);
        assertEquals("select p. from Player p where p.joinDate = :d ", request.getQuery());
        assertEquals(8, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Any), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @dateAfter(p., :d)", 46);
        assertEquals("select p.name from Player p where p. = :d ", request.getQuery());
        assertEquals(35, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @dateAfter(p.joinDate, p.)", 58);
        assertEquals("select p.name from Player p where p.joinDate = p. ", request.getQuery());
        assertEquals(48, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());
    }

    @Test
    public void inlineFake_today() {
        HintRequest request = processor.inlineFake("select p. from Player p where @today(p.joinDate)", 8);
        assertEquals("select p. from Player p where p.joinDate = :d ", request.getQuery());
        assertEquals(8, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Any), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @today(p.)", 42);
        assertEquals("select p.name from Player p where p. = :d ", request.getQuery());
        assertEquals(35, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @today(p.joinDate) and p.", 58);
        assertEquals("select p.name from Player p where p.joinDate = :d  and p.", request.getQuery());
        assertEquals(56, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Any), request.getExpectedTypes());
    }

    @Test
    public void inlineFake_two_inlines_in_row() {
        HintRequest request = processor.inlineFake("select p. from Player p where @between(p.joinDate, now, now+1, day) and @today(p.leaveDate)", 8);
        assertEquals("select p. from Player p where p.joinDate = :d  and p.leaveDate = :d ", request.getQuery());
        assertEquals(8, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Any), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @between(p.joinDate, now, now+1, day) and @today(p.)", 84);
        assertEquals("select p.name from Player p where p.joinDate = :d  and p. = :d ", request.getQuery());
        assertEquals(56, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @between(p.joinDate, now, now+1, day) and @between(p., now, now+1, day)", 86);
        assertEquals("select p.name from Player p where p.joinDate = :d  and p. = :d ", request.getQuery());
        assertEquals(56, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @dateEquals(p.joinDate, :d) and @dateEquals(p. ,:d)", 79);
        assertEquals("select p.name from Player p where p.joinDate = :d  and p.  =:d ", request.getQuery());
        assertEquals(56, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());

        request = processor.inlineFake("select p.name from Player p where @today(p.joinDate) and @today(p.)", 65);
        assertEquals("select p.name from Player p where p.joinDate = :d  and p. = :d ", request.getQuery());
        assertEquals(56, request.getPosition());
        assertEquals(EnumSet.of(InferredType.Date), request.getExpectedTypes());
    }
}
