/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Parser;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class Jpa2GrammarTest {

    @Test
    public void testGroupBy() throws Exception {
        CharStream cs = new AntlrNoCaseStringStream(
                "select u.login " +
                        "from sec$User u " +
                        "where u.login = 'admin' " +
                        "group by u.login having u.version > 0" +
                        "order by u.login");
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.ql_statement_return aReturn = jpa2Parser.ql_statement();
        Assert.assertNotNull(aReturn);
    }

    @Test
    public void testParserParameters() throws Exception {
        String query = "select sm from sys$SendingMessage sm " +
                "where sm.status=:(?i)statusQueue or (sm.status = :statusSending and sm.updateTs<:time) " +
                "order by sm.createTs";
        CharStream cs = new AntlrNoCaseStringStream(query);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.ql_statement_return aReturn = jpa2Parser.ql_statement();
        Assert.assertNotNull(aReturn);
    }

    @Test
    public void testJoinOn() throws Exception {
        String query = "select h " +
                "from sec$Constraint u, sec$GroupHierarchy h join sec$Constraint c on c.group.id = h.parent.id " +
                "where h.userGroup = :par";
        CharStream cs = new AntlrNoCaseStringStream(query);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.ql_statement_return aReturn = jpa2Parser.ql_statement();
        Assert.assertNotNull(aReturn);
    }
}
