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

package com.haulmont.cuba.gui.autocomplete.impl;

import com.haulmont.cuba.core.sys.jpql.InferredType;
import com.haulmont.cuba.gui.components.autocomplete.impl.AliasRemover;
import com.haulmont.cuba.gui.components.autocomplete.impl.HintRequest;
import junit.framework.Assert;
import org.junit.Test;

import java.util.EnumSet;

/**
 */
public class AliasRemoverTest {

    @Test
    public void testAliasReplacement() throws Exception {
        AliasRemover aliasRemover = new AliasRemover();

        HintRequest input = new HintRequest();
        input.setQuery("select queryEntity.!vin as \"vin\" from ref$Car queryEntity");
        input.setPosition(input.getQuery().indexOf("!"));
        input.setExpectedTypes(EnumSet.of(InferredType.Any));
        HintRequest result = aliasRemover.replaceAliases(input);
        Assert.assertEquals('!', result.getQuery().charAt(result.getPosition()));
        Assert.assertTrue(!result.getQuery().contains("as \""));
        System.out.println(result.getQuery());

        input.setQuery("select queryEntity.vin as \"vin\", queryEntity.!version as \"version\" from ref$Car queryEntity");
        input.setPosition(input.getQuery().indexOf("!"));
        input.setExpectedTypes(EnumSet.of(InferredType.Any));
        result = aliasRemover.replaceAliases(input);
        Assert.assertEquals('!', result.getQuery().charAt(result.getPosition()));
        Assert.assertTrue(!result.getQuery().contains("as \""));
        System.out.println(result.getQuery());

        input.setQuery("select queryEntity.!vin as \"vin\", queryEntity.version as \"version\" from ref$Car queryEntity");
        input.setPosition(input.getQuery().indexOf("!"));
        input.setExpectedTypes(EnumSet.of(InferredType.Any));
        result = aliasRemover.replaceAliases(input);
        Assert.assertEquals('!', result.getQuery().charAt(result.getPosition()));
        Assert.assertTrue(!result.getQuery().contains("as \""));
        System.out.println(result.getQuery());

        input.setQuery("select queryEntity.vin as \"vin\", queryEntity.version as \"version\" from ref$Car queryEntity");
        input.setPosition(input.getQuery().indexOf("\"ver"));
        input.setExpectedTypes(EnumSet.of(InferredType.Any));
        result = aliasRemover.replaceAliases(input);
        Assert.assertEquals(' ', result.getQuery().charAt(result.getPosition()));
        Assert.assertTrue(!result.getQuery().contains("as \""));
        System.out.println(result.getQuery());
    }
}
