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

package com.haulmont.cuba.core.global;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QueryParserRegexTest {

    @Test
    public void testHasIsNullCondition() throws Exception {
        QueryParserRegex parser;

        parser = new QueryParserRegex("select c from ref$Car c");
        assertFalse(parser.hasIsNullCondition("colour"));

        parser = new QueryParserRegex("select c from ref$Car c where c.colour = ?1");
        assertFalse(parser.hasIsNullCondition("colour"));

        parser = new QueryParserRegex("select c from ref$Car c where c.colour is null");
        assertTrue(parser.hasIsNullCondition("colour"));

        parser = new QueryParserRegex("select c from ref$Car c where c.model.manufacturer is null");
        assertTrue(parser.hasIsNullCondition("model.manufacturer"));

        parser = new QueryParserRegex("select c from ref$Car c where c.model = (select a form ref$Other a where a.model is null)");
        assertFalse(parser.hasIsNullCondition("model"));
    }
}
