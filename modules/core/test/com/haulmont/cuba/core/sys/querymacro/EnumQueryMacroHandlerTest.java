/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.querymacro;

import junit.framework.TestCase;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EnumQueryMacroHandlerTest extends TestCase {

    public void testExpandMacro() throws Exception {
        EnumQueryMacroHandler handler = new EnumQueryMacroHandler();

        String s = handler.expandMacro("select s from taxi$SpecialInstructionType s where s.parameterType = 10 order by s.name");
        assertEquals("select s from taxi$SpecialInstructionType s where s.parameterType = 10 order by s.name", s);

        s = handler.expandMacro("select s from taxi$SpecialInstructionType s " +
                "where s.parameterType = @enum(com.haulmont.cuba.security.entity.RoleType.SUPER) order by s.name");
        assertEquals("select s from taxi$SpecialInstructionType s where s.parameterType = 10 order by s.name", s);

        s = handler.expandMacro("select s from taxi$SpecialInstructionType s " +
                "where s.parameterType = @enum(com.haulmont.cuba.core.entity.FtsChangeType.UPDATE) order by s.name");
        assertEquals("select s from taxi$SpecialInstructionType s where s.parameterType = 'U' order by s.name", s);
    }
}