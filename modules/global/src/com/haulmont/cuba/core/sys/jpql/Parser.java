/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.antlr.JPALexer;
import com.haulmont.cuba.core.sys.jpql.antlr.JPAParser;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;

public class Parser {
    public static CommonTree parse(String input) throws RecognitionException {
        JPAParser parser = createParser(input);
        JPAParser.ql_statement_return aReturn = parser.ql_statement();
        return (CommonTree) aReturn.getTree();
    }

    public static CommonTree parseWhereClause(String input) throws RecognitionException {
        JPAParser parser = createParser(input);
        JPAParser.where_clause_return aReturn = parser.where_clause();
        return (CommonTree) aReturn.getTree();
    }

    public static CommonTree parseJoinClause(String join) throws RecognitionException {
        JPAParser parser = createParser(join);
        JPAParser.join_return aReturn = parser.join();
        return (CommonTree) aReturn.getTree();
    }

    private static JPAParser createParser(String input) {
        if (input.contains("~"))
            throw new IllegalArgumentException("Input string cannot contain \"~\"");

        CharStream cs = new AntlrNoCaseStringStream(input);
        JPALexer lexer = new JPALexer(cs);
        TokenStream tstream = new CommonTokenStream(lexer);
        return new JPAParser(tstream);
    }

    public static CommonTree parseSelectionSource(String input) throws RecognitionException {
        JPAParser parser = createParser(input);
        JPAParser.identification_variable_or_collection_declaration_return aReturn =
                parser.identification_variable_or_collection_declaration();
        return (CommonTree) aReturn.getTree();
    }
}
