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

package com.haulmont.cuba.gui.xml;

import com.google.common.base.Predicate;
import com.haulmont.cuba.core.global.filter.ParameterInfo;
import com.haulmont.cuba.core.global.filter.ParametersHelper;
import com.google.common.collect.Iterables;
import junit.framework.TestCase;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 *
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
