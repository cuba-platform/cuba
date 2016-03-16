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

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Parser;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeVisitor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 */
public class Jpa2GrammarTest {

    @Test
    public void testExtensionFunctions() throws Exception {
        testQuery("select cast(e.number text) from app$MyEntity e where e.path like cast(:ds$myEntityDs.id text)");
        testQuery("select cast(e.number numeric(10,2)) from app$MyEntity e where e.number = cast(:ds$myEntityDs.id numeric(10,2))");
        testQuery("select cast(e.number varchar(100)) from app$MyEntity e where e.name = cast(:ds$myEntityDs.id varchar(100))");
        testQuery("select cast(e.number strange_type(1,2,3,4)) from app$MyEntity e where e.field1 = cast(:ds$myEntityDs.id strange_type(1,2,3,4))");

        testQuery("select e from app$MyEntity e where e.name REGEXP '.*'");

        testQuery("select extract(YEAR from e.createTs) from app$MyEntity e where extract(YEAR from e.createTs) > 2012");
        testQuery("select extract(MONTH from e.createTs) from app$MyEntity e where extract(MONTH from e.createTs) > 10");
        testQuery("select extract(DAY from e.createTs) from app$MyEntity e where extract(DAY from e.createTs) > 15");
    }

    @Test
    public void testGroupBy() throws Exception {
        testQuery("select u.login " +
                        "from sec$User u " +
                        "where u.login = 'admin' " +
                        "group by u.login having u.version > 0" +
                        "order by u.login");
    }

    @Test
    public void testOrderByCount() throws Exception {
        testQuery("select instance.bookPublication.publisher.name, count(instance) " +
                "from library$BookInstance instance " +
                "group by instance.bookPublication.publisher.name order by count(instance) desc");

        testQuery("select instance.bookPublication.publisher.name, instance.bookPublication.year, count(instance) " +
                "from library$BookInstance instance " +
                        "group by instance.bookPublication.year, instance.bookPublication.publisher.name " +
                        "order by instance.bookPublication.year, instance.bookPublication.publisher.name");
    }

    @Test
    public void testFunction() throws Exception {
        testQuery("select u from sec$User u where function('DAYOFMONTH', u.createTs) = 1");
    }

    @Test
    public void testParserParameters() throws Exception {
        String query = "select sm from sys$SendingMessage sm " +
                "where sm.status=:(?i)statusQueue or (sm.status = :statusSending and sm.updateTs<:time) " +
                "order by sm.createTs";
        testQuery(query);
    }

    @Test
    public void testJoinOn() throws Exception {
        String query = "select h " +
                "from sec$Constraint u, sec$GroupHierarchy h join sec$Constraint c on c.group.id = h.parent.id " +
                "where h.userGroup = :par";
        testQuery(query);
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

    @Test
    public void testOrderBy() throws Exception {
        testQuery("select c from ref$Contract c order by c.number");
        testQuery("select c from ref$Contract c order by c.number asc");
        testQuery("select c from ref$Contract c order by c.number desc");
        testQuery("select c from ref$Contract c order by c.order desc, c.number asc");
        testQuery("select c from ref$Contract c order by c.order asc, c.number desc");
    }

    @Test
    public void testOrderByReservedWords() throws Exception {
        testQuery("select c from ref$Contract c order by c.order");
        testQuery("select c from ref$Contract c order by c.from");
        testQuery("select c from ref$Contract c order by c.max");
        testQuery("select c from ref$Contract c order by c.min");
        testQuery("select c from ref$Contract c order by c.select");
        testQuery("select c from ref$Contract c order by c.count");
        testQuery("select c from ref$Contract c order by c.group");
    }

    @Test
    public void testGroupByReservedWords() throws Exception {
        testQuery("select c from ref$Contract c group by c.order");
        testQuery("select c from ref$Contract c group by c.from");
        testQuery("select c from ref$Contract c group by c.max");
        testQuery("select c from ref$Contract c group by c.min");
        testQuery("select c from ref$Contract c group by c.select");
        testQuery("select c from ref$Contract c group by c.count");
        testQuery("select c from ref$Contract c group by c.group");
    }

    @Test
    public void testWhereReservedWords() throws Exception {
        testQuery("select dB from taxi$DriverBan dB " +
                "where dB.driver.id = :driverId " +
                "and ((dB.till >= :date and (dB.from is null or dB.from <= :date)) or dB.bannedForever = true)  " +
                "and dB.type in (:account, :pin, :individual, :login)");

        testQuery("select dB from taxi$DriverBan dB where dB.select is null");
        testQuery("select dB from taxi$DriverBan dB where dB.from is null");
        testQuery("select dB from taxi$DriverBan dB where dB.order is null");
        testQuery("select dB from taxi$DriverBan dB where dB.max is null");
        testQuery("select dB from taxi$DriverBan dB where dB.min is null");
        testQuery("select dB from taxi$DriverBan dB where dB.count is null");
        testQuery("select dB from taxi$DriverBan dB where dB.group is null");
        testQuery("select dB from taxi$DriverBan dB where dB.avg is null");
        testQuery("select dB from taxi$DriverBan dB where dB.size is null");
    }

    private void testQuery(String query) throws RecognitionException {
        CharStream cs = new AntlrNoCaseStringStream(query);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.ql_statement_return aReturn = jpa2Parser.ql_statement();
        Assert.assertTrue(isValid((CommonTree) aReturn.getTree()));
    }

    @Test
    public void testIsNull() throws Exception {
        String query = "select f from sec$Filter f left join f.user u " +
                "where f.componentId = :component and (u.id = :userId or u is null) order by f.name";
        testQuery(query);
    }

    @Test
    public void testUpdate() throws Exception {
        String query = "update sec$User u set u.group = :group where u.id = :userId";
        CharStream cs = new AntlrNoCaseStringStream(query);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.update_statement_return aReturn = jpa2Parser.update_statement();
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
