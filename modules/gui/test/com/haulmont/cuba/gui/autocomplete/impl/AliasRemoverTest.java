/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.autocomplete.impl;

import com.haulmont.cuba.core.sys.jpql.InferredType;
import com.haulmont.cuba.gui.components.autocomplete.impl.AliasRemover;
import com.haulmont.cuba.gui.components.autocomplete.impl.HintRequest;
import junit.framework.Assert;
import org.junit.Test;

import java.util.EnumSet;

/**
 * @author degtyarjov
 * @version $Id$
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
