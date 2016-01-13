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
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeVisitor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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
        Assert.assertTrue(isValid((CommonTree) aReturn.getTree()));
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
        Assert.assertTrue(isValid((CommonTree) aReturn.getTree()));
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
        Assert.assertTrue(isValid((CommonTree) aReturn.getTree()));
    }

    @Test
    public void testEscape() throws Exception {
        String query = "c.name like :pattern escape '/'";
        CharStream cs = new AntlrNoCaseStringStream(query);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.like_expression_return aReturn = jpa2Parser.like_expression();
        Assert.assertNotNull(aReturn);

        query = "c.name like :pattern escape '.'";
        cs = new AntlrNoCaseStringStream(query);
        lexer = new JPA2Lexer(cs);
        tstream = new CommonTokenStream(lexer);
        jpa2Parser = new JPA2Parser(tstream);
        aReturn = jpa2Parser.like_expression();
        Assert.assertTrue(isValid((CommonTree) aReturn.getTree()));
    }

    @Test
    public void testTypeField() throws Exception {
        String query = "where e.model.type = :component$filter.model_type89015";
        CharStream cs = new AntlrNoCaseStringStream(query);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.where_clause_return aReturn = jpa2Parser.where_clause();
        Assert.assertTrue(isValid((CommonTree) aReturn.getTree()));
    }

    @Test
    public void testMemberOf() throws Exception {
        String query = "where p.owner.id = :userParam or (select u from tamsy$User u where u.id = :userParam) member of p.developers";
        CharStream cs = new AntlrNoCaseStringStream(query);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.where_clause_return aReturn = jpa2Parser.where_clause();
        Assert.assertTrue(isValid((CommonTree) aReturn.getTree()));
    }

    protected boolean isValid(CommonTree tree) {
        TreeVisitor visitor = new TreeVisitor();
        ErrorNodesFinder errorNodesFinder = new ErrorNodesFinder();
        visitor.visit(tree, errorNodesFinder);

        List<CommonErrorNode> errorNodes = errorNodesFinder.getErrorNodes();
        if (!errorNodes.isEmpty()) {
            System.err.println(errorNodes);
        }

        return errorNodes.isEmpty();
    }
}
