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
import com.haulmont.cuba.core.sys.jpql.tree.JoinVariableNode;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeVisitor;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    public static CommonTree parse(String input, boolean failOnErrors) throws RecognitionException {
        JPA2Parser parser = createParser(input);
        JPA2Parser.ql_statement_return aReturn = parser.ql_statement();
        CommonTree tree = (CommonTree) aReturn.getTree();
        if (failOnErrors) {
            checkTreeForExceptions(input, tree);
        }
        return tree;
    }

    public static CommonTree parseWhereClause(String input) throws RecognitionException {
        JPA2Parser parser = createParser(input);
        JPA2Parser.where_clause_return aReturn = parser.where_clause();
        CommonTree tree = (CommonTree) aReturn.getTree();
        checkTreeForExceptions(input, tree);
        return tree;
    }

    public static List<JoinVariableNode> parseJoinClause(String join) throws RecognitionException {
        JPA2Parser parser = createParser(join);
        JPA2Parser.join_section_return aReturn = parser.join_section();
        CommonTree tree = (CommonTree) aReturn.getTree();
        if (tree instanceof JoinVariableNode) {
            checkTreeForExceptions(join, tree);
            return Collections.singletonList((JoinVariableNode) tree);
        } else {
            List<JoinVariableNode> joins = tree.getChildren().stream()
                    .filter(node -> node instanceof JoinVariableNode)
                    .map(JoinVariableNode.class::cast)
                    .collect(Collectors.toList());
            joins.stream().forEach(node -> checkTreeForExceptions(join, tree));
            return joins;
        }
    }

    public static CommonTree parseSelectionSource(String input) throws RecognitionException {
        JPA2Parser parser = createParser(input);
        JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return aReturn =
                parser.identification_variable_declaration_or_collection_member_declaration();
        CommonTree tree = (CommonTree) aReturn.getTree();
        checkTreeForExceptions(input, tree);
        return tree;
    }

    private static JPA2Parser createParser(String input) {
        if (input.contains("~"))
            throw new IllegalArgumentException("Input string cannot contain \"~\"");

        CharStream cs = new AntlrNoCaseStringStream(input);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        return new JPA2Parser(tstream);
    }

    private static void checkTreeForExceptions(String input, CommonTree tree) {
        TreeVisitor visitor = new TreeVisitor();
        ErrorNodesFinder errorNodesFinder = new ErrorNodesFinder();
        visitor.visit(tree, errorNodesFinder);

        List<ErrorRec> errors = errorNodesFinder.getErrorNodes().stream()
                .map(node -> new ErrorRec(node, "CommonErrorNode"))
                .collect(Collectors.toList());

        if (!errors.isEmpty()) {
            throw new JpqlSyntaxException(String.format("Errors found for input jpql:[%s]", StringUtils.strip(input)), errors);
        }
    }
}
