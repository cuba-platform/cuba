/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Parser;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;

public class Parser {
    public static CommonTree parse(String input) throws RecognitionException {
        JPA2Parser parser = createParser(input);
        JPA2Parser.ql_statement_return aReturn = parser.ql_statement();
        return (CommonTree) aReturn.getTree();
    }

    public static CommonTree parseWhereClause(String input) throws RecognitionException {
        JPA2Parser parser = createParser(input);
        JPA2Parser.where_clause_return aReturn = parser.where_clause();
        return (CommonTree) aReturn.getTree();
    }

    public static CommonTree parseJoinClause(String join) throws RecognitionException {
        JPA2Parser parser = createParser(join);
        JPA2Parser.join_return aReturn = parser.join();
        return (CommonTree) aReturn.getTree();
    }

    private static JPA2Parser createParser(String input) {
        if (input.contains("~"))
            throw new IllegalArgumentException("Input string cannot contain \"~\"");

        CharStream cs = new AntlrNoCaseStringStream(input);
        JPA2Lexer lexer = new JPA2Lexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        return new JPA2Parser(tstream);
    }

    public static CommonTree parseSelectionSource(String input) throws RecognitionException {
        JPA2Parser parser = createParser(input);
        JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return aReturn =
                parser.identification_variable_declaration_or_collection_member_declaration();
        return (CommonTree) aReturn.getTree();
    }
}
