/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import junit.framework.TestCase;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ParametersHelperTest extends TestCase {

    @Test
    public void testParseQuery() {
        ParameterInfo[] params = ParametersHelper.parseQuery(
                "select e from sys$ScheduledExecution e\n" +
                "  where e.task.id = :param$task\n" +
                "   <#if (component$finishedField?has_content && component$finishedField == true)>and e.finishTime is not null</#if>\n" +
                "order by e.startTime desc", null);

        assertNotNull(Iterables.find(Arrays.asList(params), new Predicate<ParameterInfo>() {
            @Override
            public boolean apply(@Nullable ParameterInfo param) {
                return param.getType().equals(ParameterInfo.Type.PARAM) && param.getName().equals("param$task");
            }
        }, null));

        assertNotNull(Iterables.find(Arrays.asList(params), new Predicate<ParameterInfo>() {
            @Override
            public boolean apply(@Nullable ParameterInfo param) {
                return param.getType().equals(ParameterInfo.Type.COMPONENT) && param.getName().equals("component$finishedField");
            }
        }, null));

        params = ParametersHelper.parseQuery(
                "select distinct e from df$Employee e <#if param$departmentLookup?has_content>where e.department is null</#if> order by e.name",
                null);

        assertNotNull(Iterables.find(Arrays.asList(params), new Predicate<ParameterInfo>() {
            @Override
            public boolean apply(@Nullable ParameterInfo param) {
                return param.getType().equals(ParameterInfo.Type.PARAM) && param.getName().equals("param$departmentLookup");
            }
        }, null));
    }
}
