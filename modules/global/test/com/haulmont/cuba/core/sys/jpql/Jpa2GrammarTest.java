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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        testQuery("select extract(DAY FROM e.createTs), count(e.id) from app$MyEntity e group by extract(DAY FROM e.createTs)");
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
    public void testGroupAlias() throws Exception {
        testQuery("select group.name from sec$User u left join e.group group where u.login = 'admin'");
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
    @Disabled
    public void testFunction() throws Exception {
        testQuery("select u from sec$User u where function('DAYOFMONTH', u.createTs) = 1");
        testQuery("select u from sec$User u where function('hasRoles', u.createdBy, u.login)");
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
        Assertions.assertNotNull(aReturn);

        query = "c.name like :pattern escape '.'";
        cs = new AntlrNoCaseStringStream(query);
        lexer = new JPA2Lexer(cs);
        tstream = new CommonTokenStream(lexer);
        jpa2Parser = new JPA2Parser(tstream);
        aReturn = jpa2Parser.like_expression();
        Assertions.assertTrue(isValid((CommonTree) aReturn.getTree()));
    }

    @Test
    public void testTypeField() throws Exception {
        String query = "where e.model.type = :component$filter.model_type89015";
        CharStream cs = new AntlrNoCaseStringStream(query);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.where_clause_return aReturn = jpa2Parser.where_clause();
        Assertions.assertTrue(isValid((CommonTree) aReturn.getTree()));
    }

    @Test
    @Disabled
    public void testMemberOf() throws Exception {
        String query = "where p.owner.id = :userParam or (select u from tamsy$User u where u.id = :userParam) member of p.developers";
        CharStream cs = new AntlrNoCaseStringStream(query);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.where_clause_return aReturn = jpa2Parser.where_clause();
        Assertions.assertTrue(isValid((CommonTree) aReturn.getTree()));

        testQuery("SELECT d FROM app$Department d WHERE (select e from app$Employee e where e.id = :eParam) MEMBER OF e.employees");
        testQuery("SELECT e FROM app$Employee e WHERE 'write code' MEMBER OF e.codes");
        testQuery("SELECT e FROM app$Employee e WHERE 'write code' NOT MEMBER OF e.codes");
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
        testQuery("select c from ref$Contract c order by c.desc");
        testQuery("select c from ref$Contract c order by c.asc");
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
        testQuery("select c from ref$Contract c group by c.desc");
        testQuery("select c from ref$Contract c group by c.asc");
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
        testQuery("select dB from taxi$DriverBan dB where dB.desc is null");
        testQuery("select dB from taxi$DriverBan dB where dB.asc is null");
    }

    private void testQuery(String query) throws RecognitionException {
        CharStream cs = new AntlrNoCaseStringStream(query);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        JPA2Parser jpa2Parser = new JPA2Parser(tstream);
        JPA2Parser.ql_statement_return aReturn = jpa2Parser.ql_statement();
        Assertions.assertTrue(isValid((CommonTree) aReturn.getTree()));
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
        Assertions.assertTrue(isValid((CommonTree) aReturn.getTree()));
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

    @Test
    public void testInClause() throws Exception {
        testQuery("select u from sec$User u where u.login in ('a', 'b', 'c')");
        testQuery("select u from sec$User u where u.login in (1, 2, 3)");
    }

    @Test
    @Disabled
    public void testTreat() throws Exception {
        testQuery("SELECT e FROM app$Employee e JOIN TREAT(e.projects AS app$LargeProject) p WHERE p.budget > 1000000");
        testQuery("SELECT e FROM app$Employee e JOIN e.projects p WHERE TREAT(p as app$LargeProject).budget > 1000000");
    }

    @Test
    public void testInCollectionMember() throws Exception {
        testQuery("SELECT e FROM app$Employee e, IN(e.projects) p WHERE p.budget > 1000000");
    }

    @Test
    public void testIsNotEmpty() throws Exception {
        testQuery("SELECT e FROM app$Employee e WHERE e.projects IS EMPTY");
        testQuery("SELECT e FROM app$Employee e WHERE e.projects IS NOT EMPTY");
    }

    @Test
    @Disabled
    public void testEntityTypeExpression() throws Exception {
        testQuery("SELECT e FROM app$Employee e WHERE TYPE(e) IN :empTypes");
        testQuery("SELECT e FROM app$Employee e WHERE TYPE(e) IN (:empType1, :empType2)");
        testQuery("SELECT e FROM app$Employee e WHERE TYPE(e) <> app$Exempt");
        testQuery("SELECT e FROM app$Employee e WHERE TYPE(e) IN (app$Exempt, app$Contractor)");
    }

    @Test
    @Disabled
    public void testCaseExpression() throws Exception {
        testQuery("UPDATE app$Employee e SET e.salary = CASE e.rating WHEN 1 THEN e.salary * 1.1 WHEN 2 THEN e.salary * 1.05 ELSE e.salary * 1.01 END");
        testQuery("UPDATE app$Employee e SET e.salary = CASE WHEN e.rating = 1 THEN e.salary * 1.1 WHEN e.rating = 2 THEN e.salary * 1.05 ELSE e.salary * 1.01 END");
        testQuery("SELECT e.name, CASE TYPE(e) WHEN app$Exempt THEN 'Exempt' WHEN app$Contractor THEN 'Contractor' WHEN app$Intern THEN 'Intern' ELSE 'NonExempt' END FROM app$Employee e " +
                "WHERE e.dept.name = 'Engineering'");
        testQuery("SELECT e.name, f.name, CONCAT(CASE WHEN f.annualMiles > 50000 THEN 'Platinum ' WHEN f.annualMiles > 25000 THEN 'Gold ' ELSE '' END, 'Frequent Flyer') " +
                "FROM app$Employee e JOIN e.frequentFlierPlan f");
    }

    @Test
    public void testArithmeticFunctions() throws Exception {
        testQuery("SELECT w.name FROM app$Course c JOIN c.studentWaitlist w WHERE c.name = 'Calculus' AND INDEX(w) = 0");
        testQuery("SELECT w.name FROM app$Course c WHERE c.name = 'Calculus' AND SIZE(c.studentWaitlist) = 1");
        testQuery("SELECT w.name FROM app$Course c WHERE c.name = 'Calculus' AND ABS(c.time) = 10");
        testQuery("SELECT w.name FROM app$Course c WHERE c.name = 'Calculus' AND SQRT(c.time) = 10.5");
        testQuery("SELECT w.name FROM app$Course c WHERE c.name = 'Calculus' AND MOD(c.time, c.time1) = 2");
    }


    @Test
    @Disabled
    public void testStringFunctions() throws Exception {
        testQuery("SELECT x FROM app$Magazine x WHERE CONCAT(x.title, 's') = 'JDJs'");

        testQuery("SELECT x FROM app$Magazine x WHERE SUBSTRING(x.title, 1, 1) = 'J'");
        testQuery("SELECT x FROM app$Magazine x WHERE SUBSTRING(x.title, 1) = 'J'");

        testQuery("SELECT x FROM app$Magazine x WHERE TRIM(x.title) = 'D'");
        testQuery("SELECT x FROM app$Magazine x WHERE TRIM(TRAILING FROM x.title) = 'D'");
        testQuery("SELECT x FROM app$Magazine x WHERE TRIM(LEADING FROM x.title) = 'D'");
        testQuery("SELECT x FROM app$Magazine x WHERE TRIM(BOTH FROM x.title) = 'D'");
        testQuery("SELECT x FROM app$Magazine x WHERE TRIM(FROM x.title) = 'D'");

        testQuery("SELECT x FROM app$Magazine x WHERE TRIM(TRAILING 'J' FROM x.title) = 'D'");
        testQuery("SELECT x FROM app$Magazine x WHERE TRIM(LEADING 'J' FROM x.title) = 'D'");
        testQuery("SELECT x FROM app$Magazine x WHERE TRIM(BOTH 'J' FROM x.title) = 'D'");

        testQuery("SELECT x FROM app$Magazine x WHERE LOWER(x.title) = 'd'");
        testQuery("SELECT x FROM app$Magazine x WHERE UPPER(x.title) = 'D'");
        testQuery("SELECT x FROM app$Magazine x WHERE LENGTH(x.title) = 10");

        testQuery("SELECT x FROM app$Magazine x WHERE LOCATE('A', x.title, 4) = 6");
        testQuery("SELECT x FROM app$Magazine x WHERE LOCATE('A', x.title) = 2");
    }

    @Test
    public void testHaving() throws RecognitionException {
        testQuery("SELECT c, COUNT(o) FROM app$Customer c JOIN c.orders o GROUP BY c HAVING COUNT(o) > 5");
        testQuery("SELECT c.status, AVG(c.filledOrderCount), COUNT(c) FROM app$Customer c GROUP BY c.status HAVING c.status IN (1, 2)");
    }

    @Test
    public void testNullIfCoalesce() throws RecognitionException {
        testQuery("SELECT NULLIF(emp.salary, 10) FROM app$Employee emp");
        testQuery("SELECT COALESCE(emp.salary, emp.salaryOld, 10) FROM app$Employee emp");
        testQuery("SELECT COALESCE(emp.salary, emp.salaryOld, 10) * 5.7 FROM app$Employee emp");
        testQuery("SELECT COALESCE(emp.salary, 10) * 1.18 FROM app$Employee emp");
        testQuery("SELECT emp FROM app$Employee emp where COALESCE(emp.salary60,0)+COALESCE(emp.salary90,0)+COALESCE(emp.salary90,0) >= :param$Param1");
        testQuery("SELECT emp FROM app$Employee emp where COALESCE(emp.salary60,0)*COALESCE(emp.salary90,0)*COALESCE(emp.salary90,0) >= :param$Param1");
    }

    @Test
    public void testAllAnySome() throws RecognitionException {
        testQuery("SELECT emp FROM app$Employee emp WHERE emp.salary > ALL (SELECT m.salary FROM app$Manager m WHERE m.department = emp.department)");
        testQuery("SELECT emp FROM app$Employee emp WHERE emp.salary > ANY (SELECT m.salary FROM app$Manager m WHERE m.department = emp.department)");
        testQuery("SELECT emp FROM app$Employee emp WHERE emp.salary > SOME (SELECT m.salary FROM app$Manager m WHERE m.department = emp.department)");
    }

    @Test
    public void testNewObjectInSelect() throws RecognitionException {
        testQuery("SELECT NEW com.acme.example.CustomerDetails(c.id, c.status, o.count) FROM app$Customer c JOIN c.orders o WHERE o.count > 100");
    }

    @Test
    @Disabled
    public void testKeyValueColection() throws RecognitionException {
        testQuery("SELECT v.location.street, KEY(i).title, VALUE(i) FROM app$VideoStore v JOIN v.videoInventory i WHERE v.location.zipcode = '94301' AND VALUE(i) > 0");
    }

    @Test
    @Disabled
    public void testLiterals() throws RecognitionException {
        testQuery("SELECT e FROM app$Employee e WHERE e.name = 'Bob'");

        testQuery("SELECT e FROM app$Employee e WHERE e.id = 1234");
        testQuery("SELECT e FROM app$Employee e WHERE e.id = -1234");

        testQuery("SELECT e FROM app$Employee e WHERE e.id = 1234L");
        testQuery("SELECT e FROM app$Employee e WHERE e.id = -1234L");

        testQuery("SELECT s FROM app$Stat s WHERE s.ratio > 3.14F");
        testQuery("SELECT s FROM app$Stat s WHERE s.ratio > -3.14F");

        testQuery("SELECT s FROM app$Stat s WHERE s.ratio > 3.14e32D");
        testQuery("SELECT s FROM app$Stat s WHERE s.ratio > -3.14e32D");

        testQuery("SELECT e FROM app$Employee e WHERE e.active = TRUE");

        testQuery("SELECT e FROM app$Employee e WHERE e.startDate = {d'2012-01-03'}");
        testQuery("SELECT e FROM app$Employee e WHERE e.startTime = {t'09:00:00'}");
        testQuery("SELECT e FROM app$Employee e WHERE e.version = {ts'2012-01-03 09:00:00.000000001'}");

        testQuery("UPDATE app$Employee e SET e.manager = NULL WHERE e.manager = :manager");
    }

    @Test
    public void testAggregateFunctions() throws RecognitionException {
        testQuery("SELECT AVG(o.quantity)/2 FROM app$Order o");
        testQuery("SELECT AVG(o.quantity)/2.567 FROM app$Order o");
        testQuery("SELECT AVG(o.price) FROM app$Order o");
        testQuery("SELECT AVG(o.quantity * o.price) FROM app$Order o");
        testQuery("SELECT AVG(CASE WHEN o.orderType = 1 THEN o.price ELSE 0 END), AVG(CASE WHEN o.orderType = 2 THEN o.price ELSE 0 END) FROM app$Order o");
    }

    @Test
    public void testReservedWords() throws RecognitionException {
        testQuery("SELECT e FROM app$Customer goodCustomer WHERE e.order < 4");
        testQuery("SELECT e FROM app$Customer goodCustomer WHERE e.object < 4");
    }

    @Test
    @Disabled
    public void testSubQueries() throws RecognitionException {
        testQuery("SELECT goodCustomer FROM app$Customer goodCustomer WHERE goodCustomer.balanceOwed < (SELECT AVG(c.balanceOwed)/2.0 FROM app$Customer c)");
        testQuery("SELECT c FROM app$Customer c WHERE (SELECT AVG(o.price) FROM c.orders o) > 100");
    }
}