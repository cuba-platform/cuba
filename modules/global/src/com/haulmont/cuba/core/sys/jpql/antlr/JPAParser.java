/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

// $ANTLR 3.2 Sep 23, 2009 12:02:23 JPA.g 2012-08-06 14:42:50

package com.haulmont.cuba.core.sys.jpql.antlr;

import com.haulmont.cuba.core.sys.jpql.tree.*;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.RewriteRuleTokenStream;
import org.antlr.runtime.tree.TreeAdaptor;

public class JPAParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "T_SELECTED_ITEMS", "T_SELECTED_ITEM", "T_SOURCES", "T_SOURCE", "T_SELECTED_FIELD", "T_SELECTED_ENTITY", "T_ID_VAR", "T_JOIN_VAR", "T_COLLECTION_MEMBER", "T_QUERY", "T_CONDITION", "T_SIMPLE_CONDITION", "T_PARAMETER", "T_GROUP_BY", "T_ORDER_BY", "T_ORDER_BY_FIELD", "T_AGGREGATE_EXPR", "HAVING", "ASC", "DESC", "AVG", "MAX", "MIN", "SUM", "COUNT", "OR", "AND", "LPAREN", "RPAREN", "DISTINCT", "LEFT", "OUTER", "INNER", "JOIN", "FETCH", "ORDER", "GROUP", "BY", "INT_NUMERAL", "ESCAPE_CHARACTER", "STRINGLITERAL", "TRIM_CHARACTER", "WORD", "NAMED_PARAMETER", "RUSSIAN_SYMBOLS", "WS", "COMMENT", "LINE_COMMENT", "'SELECT'", "'FROM'", "','", "'AS'", "'(SELECT'", "'.'", "'IN'", "'OBJECT'", "'NEW'", "'WHERE'", "'NOT'", "'@BETWEEN'", "'NOW'", "'+'", "'-'", "'YEAR'", "'MONTH'", "'DAY'", "'HOUR'", "'MINUTE'", "'SECOND'", "'@DATEBEFORE'", "'@DATEAFTER'", "'@DATEEQUALS'", "'@TODAY'", "'BETWEEN'", "'LIKE'", "'ESCAPE'", "'IS'", "'NULL'", "'EMPTY'", "'MEMBER'", "'OF'", "'EXISTS'", "'ALL'", "'ANY'", "'SOME'", "'='", "'<>'", "'>'", "'>='", "'<'", "'<='", "'*'", "'/'", "'LENGTH'", "'LOCATE'", "'ABS'", "'SQRT'", "'MOD'", "'SIZE'", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'CONCAT'", "'SUBSTRING'", "'TRIM'", "'LOWER'", "'UPPER'", "'LEADING'", "'TRAILING'", "'BOTH'", "'0x'", "'?'", "'${'", "'}'", "'true'", "'false'"
    };
    public static final int T_JOIN_VAR=11;
    public static final int T_AGGREGATE_EXPR=20;
    public static final int COUNT=28;
    public static final int T_ORDER_BY=18;
    public static final int EOF=-1;
    public static final int WORD=46;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int RPAREN=32;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__90=90;
    public static final int TRIM_CHARACTER=45;
    public static final int T_SELECTED_ITEM=5;
    public static final int COMMENT=50;
    public static final int T__99=99;
    public static final int T__98=98;
    public static final int T__97=97;
    public static final int T__96=96;
    public static final int T_QUERY=13;
    public static final int T__95=95;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int ASC=22;
    public static final int LINE_COMMENT=51;
    public static final int T__85=85;
    public static final int T__84=84;
    public static final int T__87=87;
    public static final int T__86=86;
    public static final int T__89=89;
    public static final int T__88=88;
    public static final int GROUP=40;
    public static final int WS=49;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int FETCH=38;
    public static final int T__70=70;
    public static final int T_SELECTED_FIELD=8;
    public static final int OR=29;
    public static final int T__76=76;
    public static final int T__75=75;
    public static final int DISTINCT=33;
    public static final int T__74=74;
    public static final int T__73=73;
    public static final int T__79=79;
    public static final int T__78=78;
    public static final int T__77=77;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T_SELECTED_ITEMS=4;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int INNER=36;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int ORDER=39;
    public static final int T__118=118;
    public static final int T_SOURCE=7;
    public static final int T__119=119;
    public static final int T__116=116;
    public static final int T_ID_VAR=10;
    public static final int T__117=117;
    public static final int T_SIMPLE_CONDITION=15;
    public static final int T__114=114;
    public static final int T__115=115;
    public static final int MAX=25;
    public static final int AND=30;
    public static final int SUM=27;
    public static final int T__61=61;
    public static final int T__60=60;
    public static final int RUSSIAN_SYMBOLS=48;
    public static final int LPAREN=31;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int LEFT=34;
    public static final int AVG=24;
    public static final int T_ORDER_BY_FIELD=19;
    public static final int T__59=59;
    public static final int T__103=103;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int T__111=111;
    public static final int T__110=110;
    public static final int T__113=113;
    public static final int T__112=112;
    public static final int T_GROUP_BY=17;
    public static final int OUTER=35;
    public static final int BY=41;
    public static final int T_CONDITION=14;
    public static final int T_SELECTED_ENTITY=9;
    public static final int T__102=102;
    public static final int HAVING=21;
    public static final int T__101=101;
    public static final int MIN=26;
    public static final int T__100=100;
    public static final int T_PARAMETER=16;
    public static final int JOIN=37;
    public static final int NAMED_PARAMETER=47;
    public static final int ESCAPE_CHARACTER=43;
    public static final int INT_NUMERAL=42;
    public static final int STRINGLITERAL=44;
    public static final int T_COLLECTION_MEMBER=12;
    public static final int DESC=23;
    public static final int T_SOURCES=6;

    // delegates
    // delegators


        public JPAParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public JPAParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return JPAParser.tokenNames; }
    public String getGrammarFileName() { return "JPA.g"; }


    public static class ql_statement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ql_statement"
    // JPA.g:85:1: ql_statement : select_statement ;
    public final JPAParser.ql_statement_return ql_statement() throws RecognitionException {
        JPAParser.ql_statement_return retval = new JPAParser.ql_statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.select_statement_return select_statement1 = null;



        try {
            // JPA.g:86:5: ( select_statement )
            // JPA.g:86:7: select_statement
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_select_statement_in_ql_statement426);
            select_statement1=select_statement();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, select_statement1.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ql_statement"

    public static class select_statement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "select_statement"
    // JPA.g:88:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
    public final JPAParser.select_statement_return select_statement() throws RecognitionException {
        JPAParser.select_statement_return retval = new JPAParser.select_statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token sl=null;
        JPAParser.select_clause_return select_clause2 = null;

        JPAParser.from_clause_return from_clause3 = null;

        JPAParser.where_clause_return where_clause4 = null;

        JPAParser.groupby_clause_return groupby_clause5 = null;

        JPAParser.having_clause_return having_clause6 = null;

        JPAParser.orderby_clause_return orderby_clause7 = null;


        Object sl_tree=null;
        RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
        RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");
        try {
            // JPA.g:89:5: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
            // JPA.g:89:8: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )?
            {
            sl=(Token)match(input,52,FOLLOW_52_in_select_statement441); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_52.add(sl);

            pushFollow(FOLLOW_select_clause_in_select_statement443);
            select_clause2=select_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_select_clause.add(select_clause2.getTree());
            pushFollow(FOLLOW_from_clause_in_select_statement445);
            from_clause3=from_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_from_clause.add(from_clause3.getTree());
            // JPA.g:89:46: ( where_clause )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==61) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // JPA.g:89:47: where_clause
                    {
                    pushFollow(FOLLOW_where_clause_in_select_statement448);
                    where_clause4=where_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_where_clause.add(where_clause4.getTree());

                    }
                    break;

            }

            // JPA.g:89:62: ( groupby_clause )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==GROUP) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // JPA.g:89:63: groupby_clause
                    {
                    pushFollow(FOLLOW_groupby_clause_in_select_statement453);
                    groupby_clause5=groupby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause5.getTree());

                    }
                    break;

            }

            // JPA.g:89:80: ( having_clause )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==HAVING) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // JPA.g:89:81: having_clause
                    {
                    pushFollow(FOLLOW_having_clause_in_select_statement458);
                    having_clause6=having_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_having_clause.add(having_clause6.getTree());

                    }
                    break;

            }

            // JPA.g:89:96: ( orderby_clause )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==ORDER) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // JPA.g:89:97: orderby_clause
                    {
                    pushFollow(FOLLOW_orderby_clause_in_select_statement462);
                    orderby_clause7=orderby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause7.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: orderby_clause, having_clause, groupby_clause, select_clause, from_clause, where_clause
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 90:6: -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
            {
                // JPA.g:90:9: ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, sl), root_1);

                // JPA.g:90:35: ( select_clause )?
                if ( stream_select_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_select_clause.nextTree());

                }
                stream_select_clause.reset();
                adaptor.addChild(root_1, stream_from_clause.nextTree());
                // JPA.g:90:64: ( where_clause )?
                if ( stream_where_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_where_clause.nextTree());

                }
                stream_where_clause.reset();
                // JPA.g:90:80: ( groupby_clause )?
                if ( stream_groupby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                }
                stream_groupby_clause.reset();
                // JPA.g:90:98: ( having_clause )?
                if ( stream_having_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_having_clause.nextTree());

                }
                stream_having_clause.reset();
                // JPA.g:90:115: ( orderby_clause )?
                if ( stream_orderby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_orderby_clause.nextTree());

                }
                stream_orderby_clause.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "select_statement"

    public static class from_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "from_clause"
    // JPA.g:92:1: from_clause : fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_or_collection_declaration )* ) ;
    public final JPAParser.from_clause_return from_clause() throws RecognitionException {
        JPAParser.from_clause_return retval = new JPAParser.from_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token fr=null;
        Token char_literal9=null;
        JPAParser.identification_variable_declaration_return identification_variable_declaration8 = null;

        JPAParser.identification_variable_or_collection_declaration_return identification_variable_or_collection_declaration10 = null;


        Object fr_tree=null;
        Object char_literal9_tree=null;
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleSubtreeStream stream_identification_variable_or_collection_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_or_collection_declaration");
        RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
        try {
            // JPA.g:93:5: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_or_collection_declaration )* ) )
            // JPA.g:93:7: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )*
            {
            fr=(Token)match(input,53,FOLLOW_53_in_from_clause523); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_53.add(fr);

            pushFollow(FOLLOW_identification_variable_declaration_in_from_clause525);
            identification_variable_declaration8=identification_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration8.getTree());
            // JPA.g:93:53: ( ',' identification_variable_or_collection_declaration )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==54) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // JPA.g:93:54: ',' identification_variable_or_collection_declaration
            	    {
            	    char_literal9=(Token)match(input,54,FOLLOW_54_in_from_clause528); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_54.add(char_literal9);

            	    pushFollow(FOLLOW_identification_variable_or_collection_declaration_in_from_clause530);
            	    identification_variable_or_collection_declaration10=identification_variable_or_collection_declaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_identification_variable_or_collection_declaration.add(identification_variable_or_collection_declaration10.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);



            // AST REWRITE
            // elements: identification_variable_or_collection_declaration, identification_variable_declaration
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 94:5: -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_or_collection_declaration )* )
            {
                // JPA.g:94:8: ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_or_collection_declaration )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);

                adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
                // JPA.g:94:71: ( identification_variable_or_collection_declaration )*
                while ( stream_identification_variable_or_collection_declaration.hasNext() ) {
                    adaptor.addChild(root_1, stream_identification_variable_or_collection_declaration.nextTree());

                }
                stream_identification_variable_or_collection_declaration.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "from_clause"

    public static class identification_variable_or_collection_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "identification_variable_or_collection_declaration"
    // JPA.g:96:1: identification_variable_or_collection_declaration : ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) );
    public final JPAParser.identification_variable_or_collection_declaration_return identification_variable_or_collection_declaration() throws RecognitionException {
        JPAParser.identification_variable_or_collection_declaration_return retval = new JPAParser.identification_variable_or_collection_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.identification_variable_declaration_return identification_variable_declaration11 = null;

        JPAParser.collection_member_declaration_return collection_member_declaration12 = null;


        RewriteRuleSubtreeStream stream_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule collection_member_declaration");
        try {
            // JPA.g:97:5: ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WORD||LA6_0==56) ) {
                alt6=1;
            }
            else if ( (LA6_0==58) ) {
                alt6=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // JPA.g:97:7: identification_variable_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_or_collection_declaration563);
                    identification_variable_declaration11=identification_variable_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration11.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:98:7: collection_member_declaration
                    {
                    pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_or_collection_declaration571);
                    collection_member_declaration12=collection_member_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_collection_member_declaration.add(collection_member_declaration12.getTree());


                    // AST REWRITE
                    // elements: collection_member_declaration
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 98:37: -> ^( T_SOURCE collection_member_declaration )
                    {
                        // JPA.g:98:40: ^( T_SOURCE collection_member_declaration )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_1);

                        adaptor.addChild(root_1, stream_collection_member_declaration.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "identification_variable_or_collection_declaration"

    public static class identification_variable_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "identification_variable_declaration"
    // JPA.g:100:1: identification_variable_declaration : range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) ;
    public final JPAParser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
        JPAParser.identification_variable_declaration_return retval = new JPAParser.identification_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.range_variable_declaration_return range_variable_declaration13 = null;

        JPAParser.joined_clause_return joined_clause14 = null;


        RewriteRuleSubtreeStream stream_joined_clause=new RewriteRuleSubtreeStream(adaptor,"rule joined_clause");
        RewriteRuleSubtreeStream stream_range_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration");
        try {
            // JPA.g:101:5: ( range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) )
            // JPA.g:101:7: range_variable_declaration ( joined_clause )*
            {
            pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration594);
            range_variable_declaration13=range_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_range_variable_declaration.add(range_variable_declaration13.getTree());
            // JPA.g:101:34: ( joined_clause )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==LEFT||(LA7_0>=INNER && LA7_0<=JOIN)) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // JPA.g:0:0: joined_clause
            	    {
            	    pushFollow(FOLLOW_joined_clause_in_identification_variable_declaration596);
            	    joined_clause14=joined_clause();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_joined_clause.add(joined_clause14.getTree());

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);



            // AST REWRITE
            // elements: joined_clause, range_variable_declaration
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 102:5: -> ^( T_SOURCE range_variable_declaration ( joined_clause )* )
            {
                // JPA.g:102:8: ^( T_SOURCE range_variable_declaration ( joined_clause )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_1);

                adaptor.addChild(root_1, stream_range_variable_declaration.nextTree());
                // JPA.g:102:67: ( joined_clause )*
                while ( stream_joined_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_joined_clause.nextTree());

                }
                stream_joined_clause.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "identification_variable_declaration"

    public static class joined_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "joined_clause"
    // JPA.g:104:1: joined_clause : ( join | fetch_join );
    public final JPAParser.joined_clause_return joined_clause() throws RecognitionException {
        JPAParser.joined_clause_return retval = new JPAParser.joined_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.join_return join15 = null;

        JPAParser.fetch_join_return fetch_join16 = null;



        try {
            // JPA.g:105:5: ( join | fetch_join )
            int alt8=2;
            switch ( input.LA(1) ) {
            case LEFT:
                {
                int LA8_1 = input.LA(2);

                if ( (LA8_1==OUTER) ) {
                    int LA8_4 = input.LA(3);

                    if ( (LA8_4==JOIN) ) {
                        int LA8_3 = input.LA(4);

                        if ( (LA8_3==FETCH) ) {
                            alt8=2;
                        }
                        else if ( (LA8_3==WORD) ) {
                            alt8=1;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 8, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 4, input);

                        throw nvae;
                    }
                }
                else if ( (LA8_1==JOIN) ) {
                    int LA8_3 = input.LA(3);

                    if ( (LA8_3==FETCH) ) {
                        alt8=2;
                    }
                    else if ( (LA8_3==WORD) ) {
                        alt8=1;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;
                }
                }
                break;
            case INNER:
                {
                int LA8_2 = input.LA(2);

                if ( (LA8_2==JOIN) ) {
                    int LA8_3 = input.LA(3);

                    if ( (LA8_3==FETCH) ) {
                        alt8=2;
                    }
                    else if ( (LA8_3==WORD) ) {
                        alt8=1;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 2, input);

                    throw nvae;
                }
                }
                break;
            case JOIN:
                {
                int LA8_3 = input.LA(2);

                if ( (LA8_3==FETCH) ) {
                    alt8=2;
                }
                else if ( (LA8_3==WORD) ) {
                    alt8=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // JPA.g:105:7: join
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_join_in_joined_clause627);
                    join15=join();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, join15.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:105:14: fetch_join
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_fetch_join_in_joined_clause631);
                    fetch_join16=fetch_join();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, fetch_join16.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "joined_clause"

    public static class range_variable_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "range_variable_declaration"
    // JPA.g:107:1: range_variable_declaration : range_variable_declaration_source ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source ) ;
    public final JPAParser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
        JPAParser.range_variable_declaration_return retval = new JPAParser.range_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal18=null;
        JPAParser.range_variable_declaration_source_return range_variable_declaration_source17 = null;

        JPAParser.identification_variable_return identification_variable19 = null;


        Object string_literal18_tree=null;
        RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_range_variable_declaration_source=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration_source");
        try {
            // JPA.g:108:5: ( range_variable_declaration_source ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source ) )
            // JPA.g:108:7: range_variable_declaration_source ( 'AS' )? identification_variable
            {
            pushFollow(FOLLOW_range_variable_declaration_source_in_range_variable_declaration643);
            range_variable_declaration_source17=range_variable_declaration_source();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_range_variable_declaration_source.add(range_variable_declaration_source17.getTree());
            // JPA.g:108:41: ( 'AS' )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==55) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // JPA.g:108:42: 'AS'
                    {
                    string_literal18=(Token)match(input,55,FOLLOW_55_in_range_variable_declaration646); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_55.add(string_literal18);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_range_variable_declaration650);
            identification_variable19=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable19.getTree());


            // AST REWRITE
            // elements: range_variable_declaration_source
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 109:7: -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source )
            {
                // JPA.g:109:10: ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new IdentificationVariableNode(T_ID_VAR, (identification_variable19!=null?input.toString(identification_variable19.start,identification_variable19.stop):null)), root_1);

                adaptor.addChild(root_1, stream_range_variable_declaration_source.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "range_variable_declaration"

    public static class range_variable_declaration_source_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "range_variable_declaration_source"
    // JPA.g:112:1: range_variable_declaration_source : ( abstract_schema_name | lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')' -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) );
    public final JPAParser.range_variable_declaration_source_return range_variable_declaration_source() throws RecognitionException {
        JPAParser.range_variable_declaration_source_return retval = new JPAParser.range_variable_declaration_source_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token lp=null;
        Token rp=null;
        JPAParser.abstract_schema_name_return abstract_schema_name20 = null;

        JPAParser.select_clause_return select_clause21 = null;

        JPAParser.from_clause_return from_clause22 = null;

        JPAParser.where_clause_return where_clause23 = null;

        JPAParser.groupby_clause_return groupby_clause24 = null;

        JPAParser.having_clause_return having_clause25 = null;

        JPAParser.orderby_clause_return orderby_clause26 = null;


        Object lp_tree=null;
        Object rp_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
        RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");
        try {
            // JPA.g:113:5: ( abstract_schema_name | lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')' -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==WORD) ) {
                alt14=1;
            }
            else if ( (LA14_0==56) ) {
                alt14=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // JPA.g:113:7: abstract_schema_name
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_abstract_schema_name_in_range_variable_declaration_source686);
                    abstract_schema_name20=abstract_schema_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, abstract_schema_name20.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:114:7: lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')'
                    {
                    lp=(Token)match(input,56,FOLLOW_56_in_range_variable_declaration_source696); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_56.add(lp);

                    pushFollow(FOLLOW_select_clause_in_range_variable_declaration_source698);
                    select_clause21=select_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_select_clause.add(select_clause21.getTree());
                    pushFollow(FOLLOW_from_clause_in_range_variable_declaration_source700);
                    from_clause22=from_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_from_clause.add(from_clause22.getTree());
                    // JPA.g:114:46: ( where_clause )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==61) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // JPA.g:114:47: where_clause
                            {
                            pushFollow(FOLLOW_where_clause_in_range_variable_declaration_source703);
                            where_clause23=where_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_where_clause.add(where_clause23.getTree());

                            }
                            break;

                    }

                    // JPA.g:114:62: ( groupby_clause )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==GROUP) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // JPA.g:114:63: groupby_clause
                            {
                            pushFollow(FOLLOW_groupby_clause_in_range_variable_declaration_source708);
                            groupby_clause24=groupby_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause24.getTree());

                            }
                            break;

                    }

                    // JPA.g:114:80: ( having_clause )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==HAVING) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // JPA.g:114:81: having_clause
                            {
                            pushFollow(FOLLOW_having_clause_in_range_variable_declaration_source713);
                            having_clause25=having_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_having_clause.add(having_clause25.getTree());

                            }
                            break;

                    }

                    // JPA.g:114:96: ( orderby_clause )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==ORDER) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // JPA.g:114:97: orderby_clause
                            {
                            pushFollow(FOLLOW_orderby_clause_in_range_variable_declaration_source717);
                            orderby_clause26=orderby_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause26.getTree());

                            }
                            break;

                    }

                    rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_range_variable_declaration_source723); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(rp);



                    // AST REWRITE
                    // elements: select_clause, having_clause, groupby_clause, from_clause, where_clause, orderby_clause
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 115:6: -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                    {
                        // JPA.g:115:9: ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);

                        // JPA.g:115:40: ( select_clause )?
                        if ( stream_select_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_select_clause.nextTree());

                        }
                        stream_select_clause.reset();
                        adaptor.addChild(root_1, stream_from_clause.nextTree());
                        // JPA.g:115:69: ( where_clause )?
                        if ( stream_where_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_where_clause.nextTree());

                        }
                        stream_where_clause.reset();
                        // JPA.g:115:85: ( groupby_clause )?
                        if ( stream_groupby_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                        }
                        stream_groupby_clause.reset();
                        // JPA.g:115:103: ( having_clause )?
                        if ( stream_having_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_having_clause.nextTree());

                        }
                        stream_having_clause.reset();
                        // JPA.g:115:120: ( orderby_clause )?
                        if ( stream_orderby_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_orderby_clause.nextTree());

                        }
                        stream_orderby_clause.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "range_variable_declaration_source"

    public static class join_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "join"
    // JPA.g:118:1: join : join_spec join_association_path_expression ( 'AS' )? identification_variable -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ) ;
    public final JPAParser.join_return join() throws RecognitionException {
        JPAParser.join_return retval = new JPAParser.join_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal29=null;
        JPAParser.join_spec_return join_spec27 = null;

        JPAParser.join_association_path_expression_return join_association_path_expression28 = null;

        JPAParser.identification_variable_return identification_variable30 = null;


        Object string_literal29_tree=null;
        RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
        RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");
        try {
            // JPA.g:119:5: ( join_spec join_association_path_expression ( 'AS' )? identification_variable -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ) )
            // JPA.g:119:7: join_spec join_association_path_expression ( 'AS' )? identification_variable
            {
            pushFollow(FOLLOW_join_spec_in_join786);
            join_spec27=join_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_spec.add(join_spec27.getTree());
            pushFollow(FOLLOW_join_association_path_expression_in_join788);
            join_association_path_expression28=join_association_path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression28.getTree());
            // JPA.g:119:50: ( 'AS' )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==55) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // JPA.g:119:51: 'AS'
                    {
                    string_literal29=(Token)match(input,55,FOLLOW_55_in_join791); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_55.add(string_literal29);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_join795);
            identification_variable30=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable30.getTree());


            // AST REWRITE
            // elements: join_association_path_expression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 120:7: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression )
            {
                // JPA.g:120:10: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new JoinVariableNode(T_JOIN_VAR, (join_spec27!=null?input.toString(join_spec27.start,join_spec27.stop):null), (identification_variable30!=null?input.toString(identification_variable30.start,identification_variable30.stop):null)), root_1);

                adaptor.addChild(root_1, stream_join_association_path_expression.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "join"

    public static class fetch_join_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fetch_join"
    // JPA.g:123:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
    public final JPAParser.fetch_join_return fetch_join() throws RecognitionException {
        JPAParser.fetch_join_return retval = new JPAParser.fetch_join_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal32=null;
        JPAParser.join_spec_return join_spec31 = null;

        JPAParser.join_association_path_expression_return join_association_path_expression33 = null;


        Object string_literal32_tree=null;

        try {
            // JPA.g:124:5: ( join_spec 'FETCH' join_association_path_expression )
            // JPA.g:124:7: join_spec 'FETCH' join_association_path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_join_spec_in_fetch_join833);
            join_spec31=join_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec31.getTree());
            string_literal32=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join835); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal32_tree = (Object)adaptor.create(string_literal32);
            adaptor.addChild(root_0, string_literal32_tree);
            }
            pushFollow(FOLLOW_join_association_path_expression_in_fetch_join837);
            join_association_path_expression33=join_association_path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression33.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "fetch_join"

    public static class join_spec_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "join_spec"
    // JPA.g:126:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
    public final JPAParser.join_spec_return join_spec() throws RecognitionException {
        JPAParser.join_spec_return retval = new JPAParser.join_spec_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal34=null;
        Token string_literal35=null;
        Token string_literal36=null;
        Token string_literal37=null;

        Object string_literal34_tree=null;
        Object string_literal35_tree=null;
        Object string_literal36_tree=null;
        Object string_literal37_tree=null;

        try {
            // JPA.g:127:5: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
            // JPA.g:127:6: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:127:6: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
            int alt17=3;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==LEFT) ) {
                alt17=1;
            }
            else if ( (LA17_0==INNER) ) {
                alt17=2;
            }
            switch (alt17) {
                case 1 :
                    // JPA.g:127:7: ( 'LEFT' ) ( 'OUTER' )?
                    {
                    // JPA.g:127:7: ( 'LEFT' )
                    // JPA.g:127:8: 'LEFT'
                    {
                    string_literal34=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec850); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal34_tree = (Object)adaptor.create(string_literal34);
                    adaptor.addChild(root_0, string_literal34_tree);
                    }

                    }

                    // JPA.g:127:16: ( 'OUTER' )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==OUTER) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // JPA.g:127:17: 'OUTER'
                            {
                            string_literal35=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec854); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal35_tree = (Object)adaptor.create(string_literal35);
                            adaptor.addChild(root_0, string_literal35_tree);
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // JPA.g:127:29: 'INNER'
                    {
                    string_literal36=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec860); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal36_tree = (Object)adaptor.create(string_literal36);
                    adaptor.addChild(root_0, string_literal36_tree);
                    }

                    }
                    break;

            }

            string_literal37=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec865); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal37_tree = (Object)adaptor.create(string_literal37);
            adaptor.addChild(root_0, string_literal37_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "join_spec"

    public static class join_association_path_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "join_association_path_expression"
    // JPA.g:129:1: join_association_path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
    public final JPAParser.join_association_path_expression_return join_association_path_expression() throws RecognitionException {
        JPAParser.join_association_path_expression_return retval = new JPAParser.join_association_path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal39=null;
        Token char_literal41=null;
        JPAParser.identification_variable_return identification_variable38 = null;

        JPAParser.field_return field40 = null;

        JPAParser.field_return field42 = null;


        Object char_literal39_tree=null;
        Object char_literal41_tree=null;
        RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // JPA.g:130:5: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
            // JPA.g:130:7: identification_variable '.' ( field '.' )* ( field )?
            {
            pushFollow(FOLLOW_identification_variable_in_join_association_path_expression877);
            identification_variable38=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable38.getTree());
            char_literal39=(Token)match(input,57,FOLLOW_57_in_join_association_path_expression879); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_57.add(char_literal39);

            // JPA.g:130:35: ( field '.' )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==WORD) ) {
                    int LA18_1 = input.LA(2);

                    if ( (LA18_1==57) ) {
                        alt18=1;
                    }


                }
                else if ( (LA18_0==GROUP) ) {
                    int LA18_3 = input.LA(2);

                    if ( (LA18_3==57) ) {
                        alt18=1;
                    }


                }


                switch (alt18) {
            	case 1 :
            	    // JPA.g:130:36: field '.'
            	    {
            	    pushFollow(FOLLOW_field_in_join_association_path_expression882);
            	    field40=field();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_field.add(field40.getTree());
            	    char_literal41=(Token)match(input,57,FOLLOW_57_in_join_association_path_expression883); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_57.add(char_literal41);


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

            // JPA.g:130:47: ( field )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==WORD) ) {
                int LA19_1 = input.LA(2);

                if ( (synpred20_JPA()) ) {
                    alt19=1;
                }
            }
            else if ( (LA19_0==GROUP) ) {
                int LA19_3 = input.LA(2);

                if ( (LA19_3==EOF||LA19_3==HAVING||LA19_3==RPAREN||LA19_3==LEFT||(LA19_3>=INNER && LA19_3<=JOIN)||(LA19_3>=ORDER && LA19_3<=GROUP)||LA19_3==WORD||(LA19_3>=54 && LA19_3<=55)||LA19_3==61) ) {
                    alt19=1;
                }
            }
            switch (alt19) {
                case 1 :
                    // JPA.g:0:0: field
                    {
                    pushFollow(FOLLOW_field_in_join_association_path_expression887);
                    field42=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field42.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: field
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 131:5: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
            {
                // JPA.g:131:8: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable38!=null?input.toString(identification_variable38.start,identification_variable38.stop):null)), root_1);

                // JPA.g:131:68: ( field )*
                while ( stream_field.hasNext() ) {
                    adaptor.addChild(root_1, stream_field.nextTree());

                }
                stream_field.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "join_association_path_expression"

    public static class collection_member_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "collection_member_declaration"
    // JPA.g:134:1: collection_member_declaration : 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
    public final JPAParser.collection_member_declaration_return collection_member_declaration() throws RecognitionException {
        JPAParser.collection_member_declaration_return retval = new JPAParser.collection_member_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal43=null;
        Token char_literal44=null;
        Token char_literal46=null;
        Token string_literal47=null;
        JPAParser.path_expression_return path_expression45 = null;

        JPAParser.identification_variable_return identification_variable48 = null;


        Object string_literal43_tree=null;
        Object char_literal44_tree=null;
        Object char_literal46_tree=null;
        Object string_literal47_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
        RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        try {
            // JPA.g:135:5: ( 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
            // JPA.g:135:7: 'IN' '(' path_expression ')' ( 'AS' )? identification_variable
            {
            string_literal43=(Token)match(input,58,FOLLOW_58_in_collection_member_declaration924); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_58.add(string_literal43);

            char_literal44=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration925); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(char_literal44);

            pushFollow(FOLLOW_path_expression_in_collection_member_declaration927);
            path_expression45=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_path_expression.add(path_expression45.getTree());
            char_literal46=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration929); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(char_literal46);

            // JPA.g:135:35: ( 'AS' )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==55) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // JPA.g:135:36: 'AS'
                    {
                    string_literal47=(Token)match(input,55,FOLLOW_55_in_collection_member_declaration932); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_55.add(string_literal47);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_collection_member_declaration936);
            identification_variable48=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable48.getTree());


            // AST REWRITE
            // elements: path_expression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 136:5: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
            {
                // JPA.g:136:8: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new CollectionMemberNode(T_COLLECTION_MEMBER, (identification_variable48!=null?input.toString(identification_variable48.start,identification_variable48.stop):null)), root_1);

                adaptor.addChild(root_1, stream_path_expression.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "collection_member_declaration"

    public static class path_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "path_expression"
    // JPA.g:139:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
    public final JPAParser.path_expression_return path_expression() throws RecognitionException {
        JPAParser.path_expression_return retval = new JPAParser.path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal50=null;
        Token char_literal52=null;
        JPAParser.identification_variable_return identification_variable49 = null;

        JPAParser.field_return field51 = null;

        JPAParser.field_return field53 = null;


        Object char_literal50_tree=null;
        Object char_literal52_tree=null;
        RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // JPA.g:140:5: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
            // JPA.g:140:8: identification_variable '.' ( field '.' )* ( field )?
            {
            pushFollow(FOLLOW_identification_variable_in_path_expression971);
            identification_variable49=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable49.getTree());
            char_literal50=(Token)match(input,57,FOLLOW_57_in_path_expression973); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_57.add(char_literal50);

            // JPA.g:140:36: ( field '.' )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==GROUP) ) {
                    int LA21_1 = input.LA(2);

                    if ( (LA21_1==57) ) {
                        alt21=1;
                    }


                }
                else if ( (LA21_0==WORD) ) {
                    int LA21_3 = input.LA(2);

                    if ( (LA21_3==57) ) {
                        alt21=1;
                    }


                }


                switch (alt21) {
            	case 1 :
            	    // JPA.g:140:37: field '.'
            	    {
            	    pushFollow(FOLLOW_field_in_path_expression976);
            	    field51=field();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_field.add(field51.getTree());
            	    char_literal52=(Token)match(input,57,FOLLOW_57_in_path_expression977); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_57.add(char_literal52);


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);

            // JPA.g:140:48: ( field )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==GROUP) ) {
                int LA22_1 = input.LA(2);

                if ( (LA22_1==EOF||(LA22_1>=HAVING && LA22_1<=DESC)||(LA22_1>=OR && LA22_1<=AND)||LA22_1==RPAREN||(LA22_1>=ORDER && LA22_1<=GROUP)||LA22_1==WORD||(LA22_1>=53 && LA22_1<=55)||LA22_1==58||LA22_1==62||(LA22_1>=65 && LA22_1<=66)||(LA22_1>=77 && LA22_1<=78)||LA22_1==80||LA22_1==83||(LA22_1>=89 && LA22_1<=96)) ) {
                    alt22=1;
                }
            }
            else if ( (LA22_0==WORD) ) {
                int LA22_3 = input.LA(2);

                if ( (synpred23_JPA()) ) {
                    alt22=1;
                }
            }
            switch (alt22) {
                case 1 :
                    // JPA.g:140:49: field
                    {
                    pushFollow(FOLLOW_field_in_path_expression982);
                    field53=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field53.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: field
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 141:5: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
            {
                // JPA.g:141:8: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable49!=null?input.toString(identification_variable49.start,identification_variable49.stop):null)), root_1);

                // JPA.g:141:68: ( field )*
                while ( stream_field.hasNext() ) {
                    adaptor.addChild(root_1, stream_field.nextTree());

                }
                stream_field.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "path_expression"

    public static class select_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "select_clause"
    // JPA.g:144:1: select_clause : ( 'DISTINCT' )? select_expression ( ',' select_expression )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* ) ;
    public final JPAParser.select_clause_return select_clause() throws RecognitionException {
        JPAParser.select_clause_return retval = new JPAParser.select_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal54=null;
        Token char_literal56=null;
        JPAParser.select_expression_return select_expression55 = null;

        JPAParser.select_expression_return select_expression57 = null;


        Object string_literal54_tree=null;
        Object char_literal56_tree=null;
        RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleSubtreeStream stream_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule select_expression");
        try {
            // JPA.g:145:5: ( ( 'DISTINCT' )? select_expression ( ',' select_expression )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* ) )
            // JPA.g:145:7: ( 'DISTINCT' )? select_expression ( ',' select_expression )*
            {
            // JPA.g:145:7: ( 'DISTINCT' )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==DISTINCT) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // JPA.g:145:8: 'DISTINCT'
                    {
                    string_literal54=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause1021); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal54);


                    }
                    break;

            }

            pushFollow(FOLLOW_select_expression_in_select_clause1025);
            select_expression55=select_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_select_expression.add(select_expression55.getTree());
            // JPA.g:145:39: ( ',' select_expression )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==54) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // JPA.g:145:40: ',' select_expression
            	    {
            	    char_literal56=(Token)match(input,54,FOLLOW_54_in_select_clause1028); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_54.add(char_literal56);

            	    pushFollow(FOLLOW_select_expression_in_select_clause1030);
            	    select_expression57=select_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_select_expression.add(select_expression57.getTree());

            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);



            // AST REWRITE
            // elements: DISTINCT, select_expression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 146:5: -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* )
            {
                // JPA.g:146:8: ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);

                // JPA.g:146:27: ( 'DISTINCT' )?
                if ( stream_DISTINCT.hasNext() ) {
                    adaptor.addChild(root_1, stream_DISTINCT.nextNode());

                }
                stream_DISTINCT.reset();
                // JPA.g:146:41: ( ^( T_SELECTED_ITEM[] select_expression ) )*
                while ( stream_select_expression.hasNext() ) {
                    // JPA.g:146:41: ^( T_SELECTED_ITEM[] select_expression )
                    {
                    Object root_2 = (Object)adaptor.nil();
                    root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);

                    adaptor.addChild(root_2, stream_select_expression.nextTree());

                    adaptor.addChild(root_1, root_2);
                    }

                }
                stream_select_expression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "select_clause"

    public static class select_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "select_expression"
    // JPA.g:149:1: select_expression : ( path_expression | aggregate_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | 'OBJECT' '(' identification_variable ')' | constructor_expression );
    public final JPAParser.select_expression_return select_expression() throws RecognitionException {
        JPAParser.select_expression_return retval = new JPAParser.select_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal61=null;
        Token char_literal62=null;
        Token char_literal64=null;
        JPAParser.path_expression_return path_expression58 = null;

        JPAParser.aggregate_expression_return aggregate_expression59 = null;

        JPAParser.identification_variable_return identification_variable60 = null;

        JPAParser.identification_variable_return identification_variable63 = null;

        JPAParser.constructor_expression_return constructor_expression65 = null;


        Object string_literal61_tree=null;
        Object char_literal62_tree=null;
        Object char_literal64_tree=null;
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        try {
            // JPA.g:150:5: ( path_expression | aggregate_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | 'OBJECT' '(' identification_variable ')' | constructor_expression )
            int alt25=5;
            switch ( input.LA(1) ) {
            case WORD:
                {
                int LA25_1 = input.LA(2);

                if ( (LA25_1==EOF||(LA25_1>=53 && LA25_1<=54)) ) {
                    alt25=3;
                }
                else if ( (LA25_1==57) ) {
                    alt25=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 25, 1, input);

                    throw nvae;
                }
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt25=2;
                }
                break;
            case 59:
                {
                alt25=4;
                }
                break;
            case 60:
                {
                alt25=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }

            switch (alt25) {
                case 1 :
                    // JPA.g:150:7: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_select_expression1075);
                    path_expression58=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression58.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:151:7: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_select_expression1083);
                    aggregate_expression59=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression59.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:152:7: identification_variable
                    {
                    pushFollow(FOLLOW_identification_variable_in_select_expression1091);
                    identification_variable60=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable60.getTree());


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 152:31: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
                    {
                        // JPA.g:152:34: ^( T_SELECTED_ENTITY[$identification_variable.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_ENTITY, (identification_variable60!=null?input.toString(identification_variable60.start,identification_variable60.stop):null)), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // JPA.g:153:7: 'OBJECT' '(' identification_variable ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal61=(Token)match(input,59,FOLLOW_59_in_select_expression1109); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal61_tree = (Object)adaptor.create(string_literal61);
                    adaptor.addChild(root_0, string_literal61_tree);
                    }
                    char_literal62=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression1111); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal62_tree = (Object)adaptor.create(char_literal62);
                    adaptor.addChild(root_0, char_literal62_tree);
                    }
                    pushFollow(FOLLOW_identification_variable_in_select_expression1112);
                    identification_variable63=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable63.getTree());
                    char_literal64=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression1113); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal64_tree = (Object)adaptor.create(char_literal64);
                    adaptor.addChild(root_0, char_literal64_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:154:7: constructor_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_constructor_expression_in_select_expression1121);
                    constructor_expression65=constructor_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_expression65.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "select_expression"

    public static class constructor_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "constructor_expression"
    // JPA.g:156:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
    public final JPAParser.constructor_expression_return constructor_expression() throws RecognitionException {
        JPAParser.constructor_expression_return retval = new JPAParser.constructor_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal66=null;
        Token char_literal68=null;
        Token char_literal70=null;
        Token char_literal72=null;
        JPAParser.constructor_name_return constructor_name67 = null;

        JPAParser.constructor_item_return constructor_item69 = null;

        JPAParser.constructor_item_return constructor_item71 = null;


        Object string_literal66_tree=null;
        Object char_literal68_tree=null;
        Object char_literal70_tree=null;
        Object char_literal72_tree=null;

        try {
            // JPA.g:157:5: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
            // JPA.g:157:7: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal66=(Token)match(input,60,FOLLOW_60_in_constructor_expression1133); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal66_tree = (Object)adaptor.create(string_literal66);
            adaptor.addChild(root_0, string_literal66_tree);
            }
            pushFollow(FOLLOW_constructor_name_in_constructor_expression1135);
            constructor_name67=constructor_name();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name67.getTree());
            char_literal68=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression1137); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal68_tree = (Object)adaptor.create(char_literal68);
            adaptor.addChild(root_0, char_literal68_tree);
            }
            pushFollow(FOLLOW_constructor_item_in_constructor_expression1139);
            constructor_item69=constructor_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item69.getTree());
            // JPA.g:157:51: ( ',' constructor_item )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==54) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // JPA.g:157:52: ',' constructor_item
            	    {
            	    char_literal70=(Token)match(input,54,FOLLOW_54_in_constructor_expression1142); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal70_tree = (Object)adaptor.create(char_literal70);
            	    adaptor.addChild(root_0, char_literal70_tree);
            	    }
            	    pushFollow(FOLLOW_constructor_item_in_constructor_expression1144);
            	    constructor_item71=constructor_item();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item71.getTree());

            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);

            char_literal72=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression1148); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal72_tree = (Object)adaptor.create(char_literal72);
            adaptor.addChild(root_0, char_literal72_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "constructor_expression"

    public static class constructor_item_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "constructor_item"
    // JPA.g:159:1: constructor_item : ( path_expression | aggregate_expression );
    public final JPAParser.constructor_item_return constructor_item() throws RecognitionException {
        JPAParser.constructor_item_return retval = new JPAParser.constructor_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression73 = null;

        JPAParser.aggregate_expression_return aggregate_expression74 = null;



        try {
            // JPA.g:160:5: ( path_expression | aggregate_expression )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==WORD) ) {
                alt27=1;
            }
            else if ( ((LA27_0>=AVG && LA27_0<=COUNT)) ) {
                alt27=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // JPA.g:160:7: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_constructor_item1160);
                    path_expression73=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression73.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:160:25: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_constructor_item1164);
                    aggregate_expression74=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression74.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "constructor_item"

    public static class aggregate_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "aggregate_expression"
    // JPA.g:162:1: aggregate_expression : ( aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' ) );
    public final JPAParser.aggregate_expression_return aggregate_expression() throws RecognitionException {
        JPAParser.aggregate_expression_return retval = new JPAParser.aggregate_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal76=null;
        Token string_literal77=null;
        Token char_literal79=null;
        Token string_literal80=null;
        Token char_literal81=null;
        Token string_literal82=null;
        Token char_literal84=null;
        JPAParser.aggregate_expression_function_name_return aggregate_expression_function_name75 = null;

        JPAParser.path_expression_return path_expression78 = null;

        JPAParser.identification_variable_return identification_variable83 = null;


        Object char_literal76_tree=null;
        Object string_literal77_tree=null;
        Object char_literal79_tree=null;
        Object string_literal80_tree=null;
        Object char_literal81_tree=null;
        Object string_literal82_tree=null;
        Object char_literal84_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COUNT=new RewriteRuleTokenStream(adaptor,"token COUNT");
        RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        RewriteRuleSubtreeStream stream_aggregate_expression_function_name=new RewriteRuleSubtreeStream(adaptor,"rule aggregate_expression_function_name");
        try {
            // JPA.g:163:5: ( aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' ) )
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==COUNT) ) {
                int LA30_1 = input.LA(2);

                if ( (LA30_1==LPAREN) ) {
                    int LA30_3 = input.LA(3);

                    if ( (LA30_3==DISTINCT) ) {
                        int LA30_4 = input.LA(4);

                        if ( (LA30_4==WORD) ) {
                            int LA30_5 = input.LA(5);

                            if ( (LA30_5==57) ) {
                                alt30=1;
                            }
                            else if ( (LA30_5==RPAREN) ) {
                                alt30=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 30, 5, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 30, 4, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA30_3==WORD) ) {
                        int LA30_5 = input.LA(4);

                        if ( (LA30_5==57) ) {
                            alt30=1;
                        }
                        else if ( (LA30_5==RPAREN) ) {
                            alt30=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 30, 5, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 30, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 30, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA30_0>=AVG && LA30_0<=SUM)) ) {
                alt30=1;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;
            }
            switch (alt30) {
                case 1 :
                    // JPA.g:163:7: aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')'
                    {
                    pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1176);
                    aggregate_expression_function_name75=aggregate_expression_function_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name75.getTree());
                    char_literal76=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1178); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal76);

                    // JPA.g:163:46: ( 'DISTINCT' )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==DISTINCT) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // JPA.g:163:47: 'DISTINCT'
                            {
                            string_literal77=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1181); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal77);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_path_expression_in_aggregate_expression1185);
                    path_expression78=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression78.getTree());
                    char_literal79=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1186); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(char_literal79);



                    // AST REWRITE
                    // elements: aggregate_expression_function_name, path_expression, LPAREN, RPAREN, DISTINCT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 164:5: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
                    {
                        // JPA.g:164:8: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);

                        adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
                        adaptor.addChild(root_1, stream_LPAREN.nextNode());
                        // JPA.g:164:93: ( 'DISTINCT' )?
                        if ( stream_DISTINCT.hasNext() ) {
                            adaptor.addChild(root_1, stream_DISTINCT.nextNode());

                        }
                        stream_DISTINCT.reset();
                        adaptor.addChild(root_1, stream_path_expression.nextTree());
                        adaptor.addChild(root_1, stream_RPAREN.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // JPA.g:165:7: 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')'
                    {
                    string_literal80=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1220); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COUNT.add(string_literal80);

                    char_literal81=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1222); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal81);

                    // JPA.g:165:19: ( 'DISTINCT' )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0==DISTINCT) ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // JPA.g:165:20: 'DISTINCT'
                            {
                            string_literal82=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1225); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal82);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_identification_variable_in_aggregate_expression1229);
                    identification_variable83=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable83.getTree());
                    char_literal84=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1231); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(char_literal84);



                    // AST REWRITE
                    // elements: DISTINCT, identification_variable, COUNT, LPAREN, RPAREN
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 166:5: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' )
                    {
                        // JPA.g:166:8: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);

                        adaptor.addChild(root_1, stream_COUNT.nextNode());
                        adaptor.addChild(root_1, stream_LPAREN.nextNode());
                        // JPA.g:166:66: ( 'DISTINCT' )?
                        if ( stream_DISTINCT.hasNext() ) {
                            adaptor.addChild(root_1, stream_DISTINCT.nextNode());

                        }
                        stream_DISTINCT.reset();
                        adaptor.addChild(root_1, stream_identification_variable.nextTree());
                        adaptor.addChild(root_1, stream_RPAREN.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "aggregate_expression"

    public static class aggregate_expression_function_name_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "aggregate_expression_function_name"
    // JPA.g:168:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
    public final JPAParser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
        JPAParser.aggregate_expression_function_name_return retval = new JPAParser.aggregate_expression_function_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set85=null;

        Object set85_tree=null;

        try {
            // JPA.g:169:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set85=(Token)input.LT(1);
            if ( (input.LA(1)>=AVG && input.LA(1)<=COUNT) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set85));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "aggregate_expression_function_name"

    public static class where_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "where_clause"
    // JPA.g:171:1: where_clause : (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) );
    public final JPAParser.where_clause_return where_clause() throws RecognitionException {
        JPAParser.where_clause_return retval = new JPAParser.where_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token wh=null;
        Token string_literal87=null;
        JPAParser.conditional_expression_return conditional_expression86 = null;

        JPAParser.path_expression_return path_expression88 = null;


        Object wh_tree=null;
        Object string_literal87_tree=null;
        RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
        RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        try {
            // JPA.g:172:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) )
            int alt31=2;
            alt31 = dfa31.predict(input);
            switch (alt31) {
                case 1 :
                    // JPA.g:172:7: wh= 'WHERE' conditional_expression
                    {
                    wh=(Token)match(input,61,FOLLOW_61_in_where_clause1299); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_61.add(wh);

                    pushFollow(FOLLOW_conditional_expression_in_where_clause1301);
                    conditional_expression86=conditional_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression86.getTree());


                    // AST REWRITE
                    // elements: conditional_expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 172:40: -> ^( T_CONDITION[$wh] conditional_expression )
                    {
                        // JPA.g:172:43: ^( T_CONDITION[$wh] conditional_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new WhereNode(T_CONDITION, wh), root_1);

                        adaptor.addChild(root_1, stream_conditional_expression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // JPA.g:173:7: 'WHERE' path_expression
                    {
                    string_literal87=(Token)match(input,61,FOLLOW_61_in_where_clause1320); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_61.add(string_literal87);

                    pushFollow(FOLLOW_path_expression_in_where_clause1322);
                    path_expression88=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression88.getTree());


                    // AST REWRITE
                    // elements: path_expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 173:31: -> ^( T_CONDITION[$wh] path_expression )
                    {
                        // JPA.g:173:34: ^( T_CONDITION[$wh] path_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new WhereNode(T_CONDITION, wh), root_1);

                        adaptor.addChild(root_1, stream_path_expression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "where_clause"

    public static class groupby_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "groupby_clause"
    // JPA.g:175:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
    public final JPAParser.groupby_clause_return groupby_clause() throws RecognitionException {
        JPAParser.groupby_clause_return retval = new JPAParser.groupby_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal89=null;
        Token string_literal90=null;
        Token char_literal92=null;
        JPAParser.groupby_item_return groupby_item91 = null;

        JPAParser.groupby_item_return groupby_item93 = null;


        Object string_literal89_tree=null;
        Object string_literal90_tree=null;
        Object char_literal92_tree=null;
        RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
        RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");
        try {
            // JPA.g:176:5: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
            // JPA.g:176:7: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
            {
            string_literal89=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1346); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_GROUP.add(string_literal89);

            string_literal90=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1348); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BY.add(string_literal90);

            pushFollow(FOLLOW_groupby_item_in_groupby_clause1350);
            groupby_item91=groupby_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item91.getTree());
            // JPA.g:176:33: ( ',' groupby_item )*
            loop32:
            do {
                int alt32=2;
                int LA32_0 = input.LA(1);

                if ( (LA32_0==54) ) {
                    alt32=1;
                }


                switch (alt32) {
            	case 1 :
            	    // JPA.g:176:34: ',' groupby_item
            	    {
            	    char_literal92=(Token)match(input,54,FOLLOW_54_in_groupby_clause1353); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_54.add(char_literal92);

            	    pushFollow(FOLLOW_groupby_item_in_groupby_clause1355);
            	    groupby_item93=groupby_item();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item93.getTree());

            	    }
            	    break;

            	default :
            	    break loop32;
                }
            } while (true);



            // AST REWRITE
            // elements: BY, groupby_item, GROUP
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 177:5: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
            {
                // JPA.g:177:8: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new GroupByNode(T_GROUP_BY), root_1);

                adaptor.addChild(root_1, stream_GROUP.nextNode());
                adaptor.addChild(root_1, stream_BY.nextNode());
                // JPA.g:177:49: ( groupby_item )*
                while ( stream_groupby_item.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_item.nextTree());

                }
                stream_groupby_item.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "groupby_clause"

    public static class groupby_item_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "groupby_item"
    // JPA.g:180:1: groupby_item : ( path_expression | identification_variable );
    public final JPAParser.groupby_item_return groupby_item() throws RecognitionException {
        JPAParser.groupby_item_return retval = new JPAParser.groupby_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression94 = null;

        JPAParser.identification_variable_return identification_variable95 = null;



        try {
            // JPA.g:181:5: ( path_expression | identification_variable )
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==WORD) ) {
                int LA33_1 = input.LA(2);

                if ( (LA33_1==EOF||LA33_1==HAVING||LA33_1==RPAREN||LA33_1==ORDER||LA33_1==54) ) {
                    alt33=2;
                }
                else if ( (LA33_1==57) ) {
                    alt33=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 33, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 33, 0, input);

                throw nvae;
            }
            switch (alt33) {
                case 1 :
                    // JPA.g:181:7: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_groupby_item1395);
                    path_expression94=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression94.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:181:25: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_groupby_item1399);
                    identification_variable95=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable95.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "groupby_item"

    public static class having_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "having_clause"
    // JPA.g:183:1: having_clause : 'HAVING' conditional_expression ;
    public final JPAParser.having_clause_return having_clause() throws RecognitionException {
        JPAParser.having_clause_return retval = new JPAParser.having_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal96=null;
        JPAParser.conditional_expression_return conditional_expression97 = null;


        Object string_literal96_tree=null;

        try {
            // JPA.g:184:5: ( 'HAVING' conditional_expression )
            // JPA.g:184:7: 'HAVING' conditional_expression
            {
            root_0 = (Object)adaptor.nil();

            string_literal96=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1411); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal96_tree = (Object)adaptor.create(string_literal96);
            adaptor.addChild(root_0, string_literal96_tree);
            }
            pushFollow(FOLLOW_conditional_expression_in_having_clause1413);
            conditional_expression97=conditional_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression97.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "having_clause"

    public static class orderby_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "orderby_clause"
    // JPA.g:186:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
    public final JPAParser.orderby_clause_return orderby_clause() throws RecognitionException {
        JPAParser.orderby_clause_return retval = new JPAParser.orderby_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal98=null;
        Token string_literal99=null;
        Token char_literal101=null;
        JPAParser.orderby_item_return orderby_item100 = null;

        JPAParser.orderby_item_return orderby_item102 = null;


        Object string_literal98_tree=null;
        Object string_literal99_tree=null;
        Object char_literal101_tree=null;
        RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
        RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");
        try {
            // JPA.g:187:5: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
            // JPA.g:187:7: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
            {
            string_literal98=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1425); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ORDER.add(string_literal98);

            string_literal99=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1427); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BY.add(string_literal99);

            pushFollow(FOLLOW_orderby_item_in_orderby_clause1429);
            orderby_item100=orderby_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item100.getTree());
            // JPA.g:187:33: ( ',' orderby_item )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==54) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // JPA.g:187:34: ',' orderby_item
            	    {
            	    char_literal101=(Token)match(input,54,FOLLOW_54_in_orderby_clause1432); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_54.add(char_literal101);

            	    pushFollow(FOLLOW_orderby_item_in_orderby_clause1434);
            	    orderby_item102=orderby_item();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item102.getTree());

            	    }
            	    break;

            	default :
            	    break loop34;
                }
            } while (true);



            // AST REWRITE
            // elements: BY, orderby_item, ORDER
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 188:5: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
            {
                // JPA.g:188:8: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new OrderByNode(T_ORDER_BY), root_1);

                adaptor.addChild(root_1, stream_ORDER.nextNode());
                adaptor.addChild(root_1, stream_BY.nextNode());
                // JPA.g:188:49: ( orderby_item )*
                while ( stream_orderby_item.hasNext() ) {
                    adaptor.addChild(root_1, stream_orderby_item.nextTree());

                }
                stream_orderby_item.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "orderby_clause"

    public static class orderby_item_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "orderby_item"
    // JPA.g:190:1: orderby_item : ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) );
    public final JPAParser.orderby_item_return orderby_item() throws RecognitionException {
        JPAParser.orderby_item_return retval = new JPAParser.orderby_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal104=null;
        Token string_literal106=null;
        JPAParser.path_expression_return path_expression103 = null;

        JPAParser.path_expression_return path_expression105 = null;


        Object string_literal104_tree=null;
        Object string_literal106_tree=null;
        RewriteRuleTokenStream stream_DESC=new RewriteRuleTokenStream(adaptor,"token DESC");
        RewriteRuleTokenStream stream_ASC=new RewriteRuleTokenStream(adaptor,"token ASC");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        try {
            // JPA.g:191:5: ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) )
            int alt36=2;
            alt36 = dfa36.predict(input);
            switch (alt36) {
                case 1 :
                    // JPA.g:191:7: path_expression ( 'ASC' )?
                    {
                    pushFollow(FOLLOW_path_expression_in_orderby_item1469);
                    path_expression103=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression103.getTree());
                    // JPA.g:191:25: ( 'ASC' )?
                    int alt35=2;
                    int LA35_0 = input.LA(1);

                    if ( (LA35_0==ASC) ) {
                        alt35=1;
                    }
                    switch (alt35) {
                        case 1 :
                            // JPA.g:191:26: 'ASC'
                            {
                            string_literal104=(Token)match(input,ASC,FOLLOW_ASC_in_orderby_item1474); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_ASC.add(string_literal104);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: ASC, path_expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 192:6: -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? )
                    {
                        // JPA.g:192:9: ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);

                        adaptor.addChild(root_1, stream_path_expression.nextTree());
                        // JPA.g:192:64: ( 'ASC' )?
                        if ( stream_ASC.hasNext() ) {
                            adaptor.addChild(root_1, stream_ASC.nextNode());

                        }
                        stream_ASC.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // JPA.g:193:7: path_expression 'DESC'
                    {
                    pushFollow(FOLLOW_path_expression_in_orderby_item1506);
                    path_expression105=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression105.getTree());
                    string_literal106=(Token)match(input,DESC,FOLLOW_DESC_in_orderby_item1510); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DESC.add(string_literal106);



                    // AST REWRITE
                    // elements: path_expression, DESC
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 194:5: -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' )
                    {
                        // JPA.g:194:8: ^( T_ORDER_BY_FIELD[] path_expression 'DESC' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);

                        adaptor.addChild(root_1, stream_path_expression.nextTree());
                        adaptor.addChild(root_1, stream_DESC.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "orderby_item"

    public static class subquery_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "subquery"
    // JPA.g:196:1: subquery : lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
    public final JPAParser.subquery_return subquery() throws RecognitionException {
        JPAParser.subquery_return retval = new JPAParser.subquery_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token lp=null;
        Token rp=null;
        JPAParser.simple_select_clause_return simple_select_clause107 = null;

        JPAParser.subquery_from_clause_return subquery_from_clause108 = null;

        JPAParser.where_clause_return where_clause109 = null;

        JPAParser.groupby_clause_return groupby_clause110 = null;

        JPAParser.having_clause_return having_clause111 = null;


        Object lp_tree=null;
        Object rp_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
        RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
        try {
            // JPA.g:197:5: (lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
            // JPA.g:197:7: lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
            {
            lp=(Token)match(input,56,FOLLOW_56_in_subquery1542); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_56.add(lp);

            pushFollow(FOLLOW_simple_select_clause_in_subquery1544);
            simple_select_clause107=simple_select_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause107.getTree());
            pushFollow(FOLLOW_subquery_from_clause_in_subquery1546);
            subquery_from_clause108=subquery_from_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause108.getTree());
            // JPA.g:197:62: ( where_clause )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==61) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // JPA.g:197:63: where_clause
                    {
                    pushFollow(FOLLOW_where_clause_in_subquery1549);
                    where_clause109=where_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_where_clause.add(where_clause109.getTree());

                    }
                    break;

            }

            // JPA.g:197:78: ( groupby_clause )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==GROUP) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // JPA.g:197:79: groupby_clause
                    {
                    pushFollow(FOLLOW_groupby_clause_in_subquery1554);
                    groupby_clause110=groupby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause110.getTree());

                    }
                    break;

            }

            // JPA.g:197:96: ( having_clause )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==HAVING) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // JPA.g:197:97: having_clause
                    {
                    pushFollow(FOLLOW_having_clause_in_subquery1559);
                    having_clause111=having_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_having_clause.add(having_clause111.getTree());

                    }
                    break;

            }

            rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1565); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(rp);



            // AST REWRITE
            // elements: where_clause, simple_select_clause, groupby_clause, subquery_from_clause, having_clause
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 198:6: -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
            {
                // JPA.g:198:9: ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);

                adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
                adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
                // JPA.g:198:81: ( where_clause )?
                if ( stream_where_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_where_clause.nextTree());

                }
                stream_where_clause.reset();
                // JPA.g:198:97: ( groupby_clause )?
                if ( stream_groupby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                }
                stream_groupby_clause.reset();
                // JPA.g:198:115: ( having_clause )?
                if ( stream_having_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_having_clause.nextTree());

                }
                stream_having_clause.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "subquery"

    public static class subquery_from_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "subquery_from_clause"
    // JPA.g:200:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
    public final JPAParser.subquery_from_clause_return subquery_from_clause() throws RecognitionException {
        JPAParser.subquery_from_clause_return retval = new JPAParser.subquery_from_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token fr=null;
        Token char_literal113=null;
        JPAParser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration112 = null;

        JPAParser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration114 = null;


        Object fr_tree=null;
        Object char_literal113_tree=null;
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");
        try {
            // JPA.g:201:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
            // JPA.g:201:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
            {
            fr=(Token)match(input,53,FOLLOW_53_in_subquery_from_clause1614); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_53.add(fr);

            pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1616);
            subselect_identification_variable_declaration112=subselect_identification_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration112.getTree());
            // JPA.g:201:63: ( ',' subselect_identification_variable_declaration )*
            loop40:
            do {
                int alt40=2;
                int LA40_0 = input.LA(1);

                if ( (LA40_0==54) ) {
                    alt40=1;
                }


                switch (alt40) {
            	case 1 :
            	    // JPA.g:201:64: ',' subselect_identification_variable_declaration
            	    {
            	    char_literal113=(Token)match(input,54,FOLLOW_54_in_subquery_from_clause1619); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_54.add(char_literal113);

            	    pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1621);
            	    subselect_identification_variable_declaration114=subselect_identification_variable_declaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration114.getTree());

            	    }
            	    break;

            	default :
            	    break loop40;
                }
            } while (true);



            // AST REWRITE
            // elements: subselect_identification_variable_declaration
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 202:5: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
            {
                // JPA.g:202:8: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);

                // JPA.g:202:35: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
                while ( stream_subselect_identification_variable_declaration.hasNext() ) {
                    // JPA.g:202:35: ^( T_SOURCE subselect_identification_variable_declaration )
                    {
                    Object root_2 = (Object)adaptor.nil();
                    root_2 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_2);

                    adaptor.addChild(root_2, stream_subselect_identification_variable_declaration.nextTree());

                    adaptor.addChild(root_1, root_2);
                    }

                }
                stream_subselect_identification_variable_declaration.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "subquery_from_clause"

    public static class subselect_identification_variable_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "subselect_identification_variable_declaration"
    // JPA.g:204:1: subselect_identification_variable_declaration : ( identification_variable_declaration | association_path_expression ( 'AS' )? identification_variable | collection_member_declaration );
    public final JPAParser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration() throws RecognitionException {
        JPAParser.subselect_identification_variable_declaration_return retval = new JPAParser.subselect_identification_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal117=null;
        JPAParser.identification_variable_declaration_return identification_variable_declaration115 = null;

        JPAParser.association_path_expression_return association_path_expression116 = null;

        JPAParser.identification_variable_return identification_variable118 = null;

        JPAParser.collection_member_declaration_return collection_member_declaration119 = null;


        Object string_literal117_tree=null;

        try {
            // JPA.g:205:5: ( identification_variable_declaration | association_path_expression ( 'AS' )? identification_variable | collection_member_declaration )
            int alt42=3;
            switch ( input.LA(1) ) {
            case WORD:
                {
                int LA42_1 = input.LA(2);

                if ( (LA42_1==WORD||LA42_1==55) ) {
                    alt42=1;
                }
                else if ( (LA42_1==57) ) {
                    alt42=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 42, 1, input);

                    throw nvae;
                }
                }
                break;
            case 56:
                {
                alt42=1;
                }
                break;
            case 58:
                {
                alt42=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;
            }

            switch (alt42) {
                case 1 :
                    // JPA.g:205:7: identification_variable_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1659);
                    identification_variable_declaration115=identification_variable_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration115.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:206:7: association_path_expression ( 'AS' )? identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_association_path_expression_in_subselect_identification_variable_declaration1667);
                    association_path_expression116=association_path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, association_path_expression116.getTree());
                    // JPA.g:206:35: ( 'AS' )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==55) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // JPA.g:206:36: 'AS'
                            {
                            string_literal117=(Token)match(input,55,FOLLOW_55_in_subselect_identification_variable_declaration1670); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal117_tree = (Object)adaptor.create(string_literal117);
                            adaptor.addChild(root_0, string_literal117_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration1674);
                    identification_variable118=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable118.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:207:7: collection_member_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_declaration_in_subselect_identification_variable_declaration1682);
                    collection_member_declaration119=collection_member_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_declaration119.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "subselect_identification_variable_declaration"

    public static class association_path_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "association_path_expression"
    // JPA.g:209:1: association_path_expression : path_expression ;
    public final JPAParser.association_path_expression_return association_path_expression() throws RecognitionException {
        JPAParser.association_path_expression_return retval = new JPAParser.association_path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression120 = null;



        try {
            // JPA.g:210:5: ( path_expression )
            // JPA.g:210:7: path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_association_path_expression1694);
            path_expression120=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression120.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "association_path_expression"

    public static class simple_select_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "simple_select_clause"
    // JPA.g:212:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
    public final JPAParser.simple_select_clause_return simple_select_clause() throws RecognitionException {
        JPAParser.simple_select_clause_return retval = new JPAParser.simple_select_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal121=null;
        JPAParser.simple_select_expression_return simple_select_expression122 = null;


        Object string_literal121_tree=null;
        RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
        RewriteRuleSubtreeStream stream_simple_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_expression");
        try {
            // JPA.g:213:5: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
            // JPA.g:213:7: ( 'DISTINCT' )? simple_select_expression
            {
            // JPA.g:213:7: ( 'DISTINCT' )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==DISTINCT) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // JPA.g:213:8: 'DISTINCT'
                    {
                    string_literal121=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause1707); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal121);


                    }
                    break;

            }

            pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause1711);
            simple_select_expression122=simple_select_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_simple_select_expression.add(simple_select_expression122.getTree());


            // AST REWRITE
            // elements: simple_select_expression, DISTINCT
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 214:5: -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
            {
                // JPA.g:214:8: ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);

                // JPA.g:214:27: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);

                // JPA.g:214:65: ( 'DISTINCT' )?
                if ( stream_DISTINCT.hasNext() ) {
                    adaptor.addChild(root_2, stream_DISTINCT.nextNode());

                }
                stream_DISTINCT.reset();
                adaptor.addChild(root_2, stream_simple_select_expression.nextTree());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "simple_select_clause"

    public static class simple_select_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "simple_select_expression"
    // JPA.g:216:1: simple_select_expression : ( path_expression | aggregate_expression | identification_variable );
    public final JPAParser.simple_select_expression_return simple_select_expression() throws RecognitionException {
        JPAParser.simple_select_expression_return retval = new JPAParser.simple_select_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression123 = null;

        JPAParser.aggregate_expression_return aggregate_expression124 = null;

        JPAParser.identification_variable_return identification_variable125 = null;



        try {
            // JPA.g:217:5: ( path_expression | aggregate_expression | identification_variable )
            int alt44=3;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==WORD) ) {
                int LA44_1 = input.LA(2);

                if ( (LA44_1==57) ) {
                    alt44=1;
                }
                else if ( (LA44_1==53) ) {
                    alt44=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 44, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA44_0>=AVG && LA44_0<=COUNT)) ) {
                alt44=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 44, 0, input);

                throw nvae;
            }
            switch (alt44) {
                case 1 :
                    // JPA.g:217:7: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_simple_select_expression1748);
                    path_expression123=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression123.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:218:7: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression1756);
                    aggregate_expression124=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression124.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:219:7: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_simple_select_expression1764);
                    identification_variable125=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable125.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "simple_select_expression"

    public static class conditional_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "conditional_expression"
    // JPA.g:221:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
    public final JPAParser.conditional_expression_return conditional_expression() throws RecognitionException {
        JPAParser.conditional_expression_return retval = new JPAParser.conditional_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal127=null;
        JPAParser.conditional_term_return conditional_term126 = null;

        JPAParser.conditional_term_return conditional_term128 = null;


        Object string_literal127_tree=null;

        try {
            // JPA.g:222:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
            // JPA.g:222:7: ( conditional_term ) ( 'OR' conditional_term )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:222:7: ( conditional_term )
            // JPA.g:222:8: conditional_term
            {
            pushFollow(FOLLOW_conditional_term_in_conditional_expression1777);
            conditional_term126=conditional_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term126.getTree());

            }

            // JPA.g:222:26: ( 'OR' conditional_term )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==OR) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // JPA.g:222:27: 'OR' conditional_term
            	    {
            	    string_literal127=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression1781); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal127_tree = (Object)adaptor.create(string_literal127);
            	    adaptor.addChild(root_0, string_literal127_tree);
            	    }
            	    pushFollow(FOLLOW_conditional_term_in_conditional_expression1783);
            	    conditional_term128=conditional_term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term128.getTree());

            	    }
            	    break;

            	default :
            	    break loop45;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "conditional_expression"

    public static class conditional_term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "conditional_term"
    // JPA.g:224:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
    public final JPAParser.conditional_term_return conditional_term() throws RecognitionException {
        JPAParser.conditional_term_return retval = new JPAParser.conditional_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal130=null;
        JPAParser.conditional_factor_return conditional_factor129 = null;

        JPAParser.conditional_factor_return conditional_factor131 = null;


        Object string_literal130_tree=null;

        try {
            // JPA.g:225:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
            // JPA.g:225:7: ( conditional_factor ) ( 'AND' conditional_factor )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:225:7: ( conditional_factor )
            // JPA.g:225:8: conditional_factor
            {
            pushFollow(FOLLOW_conditional_factor_in_conditional_term1798);
            conditional_factor129=conditional_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor129.getTree());

            }

            // JPA.g:225:28: ( 'AND' conditional_factor )*
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( (LA46_0==AND) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // JPA.g:225:29: 'AND' conditional_factor
            	    {
            	    string_literal130=(Token)match(input,AND,FOLLOW_AND_in_conditional_term1802); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal130_tree = (Object)adaptor.create(string_literal130);
            	    adaptor.addChild(root_0, string_literal130_tree);
            	    }
            	    pushFollow(FOLLOW_conditional_factor_in_conditional_term1804);
            	    conditional_factor131=conditional_factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor131.getTree());

            	    }
            	    break;

            	default :
            	    break loop46;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "conditional_term"

    public static class conditional_factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "conditional_factor"
    // JPA.g:227:1: conditional_factor : ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' );
    public final JPAParser.conditional_factor_return conditional_factor() throws RecognitionException {
        JPAParser.conditional_factor_return retval = new JPAParser.conditional_factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal132=null;
        Token char_literal134=null;
        Token char_literal136=null;
        JPAParser.simple_cond_expression_return simple_cond_expression133 = null;

        JPAParser.conditional_expression_return conditional_expression135 = null;


        Object string_literal132_tree=null;
        Object char_literal134_tree=null;
        Object char_literal136_tree=null;
        RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
        RewriteRuleSubtreeStream stream_simple_cond_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_cond_expression");
        try {
            // JPA.g:228:5: ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' )
            int alt48=2;
            alt48 = dfa48.predict(input);
            switch (alt48) {
                case 1 :
                    // JPA.g:228:7: ( 'NOT' )? simple_cond_expression
                    {
                    // JPA.g:228:7: ( 'NOT' )?
                    int alt47=2;
                    int LA47_0 = input.LA(1);

                    if ( (LA47_0==62) ) {
                        int LA47_1 = input.LA(2);

                        if ( (synpred57_JPA()) ) {
                            alt47=1;
                        }
                    }
                    switch (alt47) {
                        case 1 :
                            // JPA.g:228:8: 'NOT'
                            {
                            string_literal132=(Token)match(input,62,FOLLOW_62_in_conditional_factor1823); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_62.add(string_literal132);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_simple_cond_expression_in_conditional_factor1827);
                    simple_cond_expression133=simple_cond_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_simple_cond_expression.add(simple_cond_expression133.getTree());


                    // AST REWRITE
                    // elements: simple_cond_expression, 62
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 228:39: -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression )
                    {
                        // JPA.g:228:42: ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new SimpleConditionNode(T_SIMPLE_CONDITION), root_1);

                        // JPA.g:228:86: ( 'NOT' )?
                        if ( stream_62.hasNext() ) {
                            adaptor.addChild(root_1, stream_62.nextNode());

                        }
                        stream_62.reset();
                        adaptor.addChild(root_1, stream_simple_cond_expression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // JPA.g:229:7: '(' conditional_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal134=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_factor1852); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal134_tree = (Object)adaptor.create(char_literal134);
                    adaptor.addChild(root_0, char_literal134_tree);
                    }
                    pushFollow(FOLLOW_conditional_expression_in_conditional_factor1853);
                    conditional_expression135=conditional_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression135.getTree());
                    char_literal136=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_factor1854); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal136_tree = (Object)adaptor.create(char_literal136);
                    adaptor.addChild(root_0, char_literal136_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "conditional_factor"

    public static class simple_cond_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "simple_cond_expression"
    // JPA.g:231:1: simple_cond_expression : ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
    public final JPAParser.simple_cond_expression_return simple_cond_expression() throws RecognitionException {
        JPAParser.simple_cond_expression_return retval = new JPAParser.simple_cond_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.comparison_expression_return comparison_expression137 = null;

        JPAParser.between_expression_return between_expression138 = null;

        JPAParser.like_expression_return like_expression139 = null;

        JPAParser.in_expression_return in_expression140 = null;

        JPAParser.null_comparison_expression_return null_comparison_expression141 = null;

        JPAParser.empty_collection_comparison_expression_return empty_collection_comparison_expression142 = null;

        JPAParser.collection_member_expression_return collection_member_expression143 = null;

        JPAParser.exists_expression_return exists_expression144 = null;

        JPAParser.date_macro_expression_return date_macro_expression145 = null;



        try {
            // JPA.g:232:5: ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
            int alt49=9;
            alt49 = dfa49.predict(input);
            switch (alt49) {
                case 1 :
                    // JPA.g:232:7: comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression1866);
                    comparison_expression137=comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression137.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:233:7: between_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_between_expression_in_simple_cond_expression1874);
                    between_expression138=between_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression138.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:234:7: like_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_like_expression_in_simple_cond_expression1882);
                    like_expression139=like_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression139.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:235:7: in_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_in_expression_in_simple_cond_expression1890);
                    in_expression140=in_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression140.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:236:7: null_comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression1898);
                    null_comparison_expression141=null_comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression141.getTree());

                    }
                    break;
                case 6 :
                    // JPA.g:237:7: empty_collection_comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression1906);
                    empty_collection_comparison_expression142=empty_collection_comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression142.getTree());

                    }
                    break;
                case 7 :
                    // JPA.g:238:7: collection_member_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression1914);
                    collection_member_expression143=collection_member_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression143.getTree());

                    }
                    break;
                case 8 :
                    // JPA.g:239:7: exists_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_exists_expression_in_simple_cond_expression1922);
                    exists_expression144=exists_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression144.getTree());

                    }
                    break;
                case 9 :
                    // JPA.g:240:7: date_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression1930);
                    date_macro_expression145=date_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_macro_expression145.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "simple_cond_expression"

    public static class date_macro_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_macro_expression"
    // JPA.g:242:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
    public final JPAParser.date_macro_expression_return date_macro_expression() throws RecognitionException {
        JPAParser.date_macro_expression_return retval = new JPAParser.date_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.date_between_macro_expression_return date_between_macro_expression146 = null;

        JPAParser.date_before_macro_expression_return date_before_macro_expression147 = null;

        JPAParser.date_after_macro_expression_return date_after_macro_expression148 = null;

        JPAParser.date_equals_macro_expression_return date_equals_macro_expression149 = null;

        JPAParser.date_today_macro_expression_return date_today_macro_expression150 = null;



        try {
            // JPA.g:243:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
            int alt50=5;
            switch ( input.LA(1) ) {
            case 63:
                {
                alt50=1;
                }
                break;
            case 73:
                {
                alt50=2;
                }
                break;
            case 74:
                {
                alt50=3;
                }
                break;
            case 75:
                {
                alt50=4;
                }
                break;
            case 76:
                {
                alt50=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }

            switch (alt50) {
                case 1 :
                    // JPA.g:243:7: date_between_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression1942);
                    date_between_macro_expression146=date_between_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression146.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:244:7: date_before_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression1950);
                    date_before_macro_expression147=date_before_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression147.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:245:7: date_after_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression1958);
                    date_after_macro_expression148=date_after_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression148.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:246:7: date_equals_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression1966);
                    date_equals_macro_expression149=date_equals_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression149.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:247:7: date_today_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression1974);
                    date_today_macro_expression150=date_today_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_today_macro_expression150.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "date_macro_expression"

    public static class date_between_macro_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_between_macro_expression"
    // JPA.g:249:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
    public final JPAParser.date_between_macro_expression_return date_between_macro_expression() throws RecognitionException {
        JPAParser.date_between_macro_expression_return retval = new JPAParser.date_between_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal151=null;
        Token char_literal152=null;
        Token char_literal154=null;
        Token string_literal155=null;
        Token set156=null;
        Token INT_NUMERAL157=null;
        Token char_literal158=null;
        Token string_literal159=null;
        Token set160=null;
        Token INT_NUMERAL161=null;
        Token char_literal162=null;
        Token set163=null;
        Token char_literal164=null;
        JPAParser.path_expression_return path_expression153 = null;


        Object string_literal151_tree=null;
        Object char_literal152_tree=null;
        Object char_literal154_tree=null;
        Object string_literal155_tree=null;
        Object set156_tree=null;
        Object INT_NUMERAL157_tree=null;
        Object char_literal158_tree=null;
        Object string_literal159_tree=null;
        Object set160_tree=null;
        Object INT_NUMERAL161_tree=null;
        Object char_literal162_tree=null;
        Object set163_tree=null;
        Object char_literal164_tree=null;

        try {
            // JPA.g:250:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
            // JPA.g:250:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal151=(Token)match(input,63,FOLLOW_63_in_date_between_macro_expression1986); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal151_tree = (Object)adaptor.create(string_literal151);
            adaptor.addChild(root_0, string_literal151_tree);
            }
            char_literal152=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression1988); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal152_tree = (Object)adaptor.create(char_literal152);
            adaptor.addChild(root_0, char_literal152_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_between_macro_expression1990);
            path_expression153=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression153.getTree());
            char_literal154=(Token)match(input,54,FOLLOW_54_in_date_between_macro_expression1992); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal154_tree = (Object)adaptor.create(char_literal154);
            adaptor.addChild(root_0, char_literal154_tree);
            }
            string_literal155=(Token)match(input,64,FOLLOW_64_in_date_between_macro_expression1994); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal155_tree = (Object)adaptor.create(string_literal155);
            adaptor.addChild(root_0, string_literal155_tree);
            }
            // JPA.g:250:48: ( ( '+' | '-' ) INT_NUMERAL )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( ((LA51_0>=65 && LA51_0<=66)) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // JPA.g:250:49: ( '+' | '-' ) INT_NUMERAL
                    {
                    set156=(Token)input.LT(1);
                    if ( (input.LA(1)>=65 && input.LA(1)<=66) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set156));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    INT_NUMERAL157=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_date_between_macro_expression2005); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_NUMERAL157_tree = (Object)adaptor.create(INT_NUMERAL157);
                    adaptor.addChild(root_0, INT_NUMERAL157_tree);
                    }

                    }
                    break;

            }

            char_literal158=(Token)match(input,54,FOLLOW_54_in_date_between_macro_expression2009); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal158_tree = (Object)adaptor.create(char_literal158);
            adaptor.addChild(root_0, char_literal158_tree);
            }
            string_literal159=(Token)match(input,64,FOLLOW_64_in_date_between_macro_expression2011); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal159_tree = (Object)adaptor.create(string_literal159);
            adaptor.addChild(root_0, string_literal159_tree);
            }
            // JPA.g:250:85: ( ( '+' | '-' ) INT_NUMERAL )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( ((LA52_0>=65 && LA52_0<=66)) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // JPA.g:250:86: ( '+' | '-' ) INT_NUMERAL
                    {
                    set160=(Token)input.LT(1);
                    if ( (input.LA(1)>=65 && input.LA(1)<=66) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set160));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    INT_NUMERAL161=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_date_between_macro_expression2022); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_NUMERAL161_tree = (Object)adaptor.create(INT_NUMERAL161);
                    adaptor.addChild(root_0, INT_NUMERAL161_tree);
                    }

                    }
                    break;

            }

            char_literal162=(Token)match(input,54,FOLLOW_54_in_date_between_macro_expression2026); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal162_tree = (Object)adaptor.create(char_literal162);
            adaptor.addChild(root_0, char_literal162_tree);
            }
            set163=(Token)input.LT(1);
            if ( (input.LA(1)>=67 && input.LA(1)<=72) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set163));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            char_literal164=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2051); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal164_tree = (Object)adaptor.create(char_literal164);
            adaptor.addChild(root_0, char_literal164_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "date_between_macro_expression"

    public static class date_before_macro_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_before_macro_expression"
    // JPA.g:252:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
    public final JPAParser.date_before_macro_expression_return date_before_macro_expression() throws RecognitionException {
        JPAParser.date_before_macro_expression_return retval = new JPAParser.date_before_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal165=null;
        Token char_literal166=null;
        Token char_literal168=null;
        Token char_literal171=null;
        JPAParser.path_expression_return path_expression167 = null;

        JPAParser.path_expression_return path_expression169 = null;

        JPAParser.input_parameter_return input_parameter170 = null;


        Object string_literal165_tree=null;
        Object char_literal166_tree=null;
        Object char_literal168_tree=null;
        Object char_literal171_tree=null;

        try {
            // JPA.g:253:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:253:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal165=(Token)match(input,73,FOLLOW_73_in_date_before_macro_expression2063); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal165_tree = (Object)adaptor.create(string_literal165);
            adaptor.addChild(root_0, string_literal165_tree);
            }
            char_literal166=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2065); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal166_tree = (Object)adaptor.create(char_literal166);
            adaptor.addChild(root_0, char_literal166_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2067);
            path_expression167=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression167.getTree());
            char_literal168=(Token)match(input,54,FOLLOW_54_in_date_before_macro_expression2069); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal168_tree = (Object)adaptor.create(char_literal168);
            adaptor.addChild(root_0, char_literal168_tree);
            }
            // JPA.g:253:45: ( path_expression | input_parameter )
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==WORD) ) {
                alt53=1;
            }
            else if ( (LA53_0==NAMED_PARAMETER||(LA53_0>=115 && LA53_0<=116)) ) {
                alt53=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 53, 0, input);

                throw nvae;
            }
            switch (alt53) {
                case 1 :
                    // JPA.g:253:46: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2072);
                    path_expression169=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression169.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:253:64: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2076);
                    input_parameter170=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter170.getTree());

                    }
                    break;

            }

            char_literal171=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2079); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal171_tree = (Object)adaptor.create(char_literal171);
            adaptor.addChild(root_0, char_literal171_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "date_before_macro_expression"

    public static class date_after_macro_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_after_macro_expression"
    // JPA.g:255:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
    public final JPAParser.date_after_macro_expression_return date_after_macro_expression() throws RecognitionException {
        JPAParser.date_after_macro_expression_return retval = new JPAParser.date_after_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal172=null;
        Token char_literal173=null;
        Token char_literal175=null;
        Token char_literal178=null;
        JPAParser.path_expression_return path_expression174 = null;

        JPAParser.path_expression_return path_expression176 = null;

        JPAParser.input_parameter_return input_parameter177 = null;


        Object string_literal172_tree=null;
        Object char_literal173_tree=null;
        Object char_literal175_tree=null;
        Object char_literal178_tree=null;

        try {
            // JPA.g:256:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:256:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal172=(Token)match(input,74,FOLLOW_74_in_date_after_macro_expression2091); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal172_tree = (Object)adaptor.create(string_literal172);
            adaptor.addChild(root_0, string_literal172_tree);
            }
            char_literal173=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2093); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal173_tree = (Object)adaptor.create(char_literal173);
            adaptor.addChild(root_0, char_literal173_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2095);
            path_expression174=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression174.getTree());
            char_literal175=(Token)match(input,54,FOLLOW_54_in_date_after_macro_expression2097); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal175_tree = (Object)adaptor.create(char_literal175);
            adaptor.addChild(root_0, char_literal175_tree);
            }
            // JPA.g:256:44: ( path_expression | input_parameter )
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==WORD) ) {
                alt54=1;
            }
            else if ( (LA54_0==NAMED_PARAMETER||(LA54_0>=115 && LA54_0<=116)) ) {
                alt54=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }
            switch (alt54) {
                case 1 :
                    // JPA.g:256:45: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2100);
                    path_expression176=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression176.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:256:63: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2104);
                    input_parameter177=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter177.getTree());

                    }
                    break;

            }

            char_literal178=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2107); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal178_tree = (Object)adaptor.create(char_literal178);
            adaptor.addChild(root_0, char_literal178_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "date_after_macro_expression"

    public static class date_equals_macro_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_equals_macro_expression"
    // JPA.g:258:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
    public final JPAParser.date_equals_macro_expression_return date_equals_macro_expression() throws RecognitionException {
        JPAParser.date_equals_macro_expression_return retval = new JPAParser.date_equals_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal179=null;
        Token char_literal180=null;
        Token char_literal182=null;
        Token char_literal185=null;
        JPAParser.path_expression_return path_expression181 = null;

        JPAParser.path_expression_return path_expression183 = null;

        JPAParser.input_parameter_return input_parameter184 = null;


        Object string_literal179_tree=null;
        Object char_literal180_tree=null;
        Object char_literal182_tree=null;
        Object char_literal185_tree=null;

        try {
            // JPA.g:259:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:259:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal179=(Token)match(input,75,FOLLOW_75_in_date_equals_macro_expression2119); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal179_tree = (Object)adaptor.create(string_literal179);
            adaptor.addChild(root_0, string_literal179_tree);
            }
            char_literal180=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2121); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal180_tree = (Object)adaptor.create(char_literal180);
            adaptor.addChild(root_0, char_literal180_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2123);
            path_expression181=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression181.getTree());
            char_literal182=(Token)match(input,54,FOLLOW_54_in_date_equals_macro_expression2125); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal182_tree = (Object)adaptor.create(char_literal182);
            adaptor.addChild(root_0, char_literal182_tree);
            }
            // JPA.g:259:45: ( path_expression | input_parameter )
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==WORD) ) {
                alt55=1;
            }
            else if ( (LA55_0==NAMED_PARAMETER||(LA55_0>=115 && LA55_0<=116)) ) {
                alt55=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 55, 0, input);

                throw nvae;
            }
            switch (alt55) {
                case 1 :
                    // JPA.g:259:46: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2128);
                    path_expression183=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression183.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:259:64: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2132);
                    input_parameter184=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter184.getTree());

                    }
                    break;

            }

            char_literal185=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2135); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal185_tree = (Object)adaptor.create(char_literal185);
            adaptor.addChild(root_0, char_literal185_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "date_equals_macro_expression"

    public static class date_today_macro_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_today_macro_expression"
    // JPA.g:261:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
    public final JPAParser.date_today_macro_expression_return date_today_macro_expression() throws RecognitionException {
        JPAParser.date_today_macro_expression_return retval = new JPAParser.date_today_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal186=null;
        Token char_literal187=null;
        Token char_literal189=null;
        JPAParser.path_expression_return path_expression188 = null;


        Object string_literal186_tree=null;
        Object char_literal187_tree=null;
        Object char_literal189_tree=null;

        try {
            // JPA.g:262:5: ( '@TODAY' '(' path_expression ')' )
            // JPA.g:262:7: '@TODAY' '(' path_expression ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal186=(Token)match(input,76,FOLLOW_76_in_date_today_macro_expression2147); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal186_tree = (Object)adaptor.create(string_literal186);
            adaptor.addChild(root_0, string_literal186_tree);
            }
            char_literal187=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2149); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal187_tree = (Object)adaptor.create(char_literal187);
            adaptor.addChild(root_0, char_literal187_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2151);
            path_expression188=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression188.getTree());
            char_literal189=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2153); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal189_tree = (Object)adaptor.create(char_literal189);
            adaptor.addChild(root_0, char_literal189_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "date_today_macro_expression"

    public static class between_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "between_expression"
    // JPA.g:264:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
    public final JPAParser.between_expression_return between_expression() throws RecognitionException {
        JPAParser.between_expression_return retval = new JPAParser.between_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal191=null;
        Token string_literal192=null;
        Token string_literal194=null;
        Token string_literal197=null;
        Token string_literal198=null;
        Token string_literal200=null;
        Token string_literal203=null;
        Token string_literal204=null;
        Token string_literal206=null;
        JPAParser.arithmetic_expression_return arithmetic_expression190 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression193 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression195 = null;

        JPAParser.string_expression_return string_expression196 = null;

        JPAParser.string_expression_return string_expression199 = null;

        JPAParser.string_expression_return string_expression201 = null;

        JPAParser.datetime_expression_return datetime_expression202 = null;

        JPAParser.datetime_expression_return datetime_expression205 = null;

        JPAParser.datetime_expression_return datetime_expression207 = null;


        Object string_literal191_tree=null;
        Object string_literal192_tree=null;
        Object string_literal194_tree=null;
        Object string_literal197_tree=null;
        Object string_literal198_tree=null;
        Object string_literal200_tree=null;
        Object string_literal203_tree=null;
        Object string_literal204_tree=null;
        Object string_literal206_tree=null;

        try {
            // JPA.g:265:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
            int alt59=3;
            alt59 = dfa59.predict(input);
            switch (alt59) {
                case 1 :
                    // JPA.g:265:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression2165);
                    arithmetic_expression190=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression190.getTree());
                    // JPA.g:265:29: ( 'NOT' )?
                    int alt56=2;
                    int LA56_0 = input.LA(1);

                    if ( (LA56_0==62) ) {
                        alt56=1;
                    }
                    switch (alt56) {
                        case 1 :
                            // JPA.g:265:30: 'NOT'
                            {
                            string_literal191=(Token)match(input,62,FOLLOW_62_in_between_expression2168); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal191_tree = (Object)adaptor.create(string_literal191);
                            adaptor.addChild(root_0, string_literal191_tree);
                            }

                            }
                            break;

                    }

                    string_literal192=(Token)match(input,77,FOLLOW_77_in_between_expression2172); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal192_tree = (Object)adaptor.create(string_literal192);
                    adaptor.addChild(root_0, string_literal192_tree);
                    }
                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression2174);
                    arithmetic_expression193=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression193.getTree());
                    string_literal194=(Token)match(input,AND,FOLLOW_AND_in_between_expression2176); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal194_tree = (Object)adaptor.create(string_literal194);
                    adaptor.addChild(root_0, string_literal194_tree);
                    }
                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression2178);
                    arithmetic_expression195=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression195.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:266:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_expression_in_between_expression2186);
                    string_expression196=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression196.getTree());
                    // JPA.g:266:25: ( 'NOT' )?
                    int alt57=2;
                    int LA57_0 = input.LA(1);

                    if ( (LA57_0==62) ) {
                        alt57=1;
                    }
                    switch (alt57) {
                        case 1 :
                            // JPA.g:266:26: 'NOT'
                            {
                            string_literal197=(Token)match(input,62,FOLLOW_62_in_between_expression2189); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal197_tree = (Object)adaptor.create(string_literal197);
                            adaptor.addChild(root_0, string_literal197_tree);
                            }

                            }
                            break;

                    }

                    string_literal198=(Token)match(input,77,FOLLOW_77_in_between_expression2193); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal198_tree = (Object)adaptor.create(string_literal198);
                    adaptor.addChild(root_0, string_literal198_tree);
                    }
                    pushFollow(FOLLOW_string_expression_in_between_expression2195);
                    string_expression199=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression199.getTree());
                    string_literal200=(Token)match(input,AND,FOLLOW_AND_in_between_expression2197); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal200_tree = (Object)adaptor.create(string_literal200);
                    adaptor.addChild(root_0, string_literal200_tree);
                    }
                    pushFollow(FOLLOW_string_expression_in_between_expression2199);
                    string_expression201=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression201.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:267:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_expression_in_between_expression2207);
                    datetime_expression202=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression202.getTree());
                    // JPA.g:267:27: ( 'NOT' )?
                    int alt58=2;
                    int LA58_0 = input.LA(1);

                    if ( (LA58_0==62) ) {
                        alt58=1;
                    }
                    switch (alt58) {
                        case 1 :
                            // JPA.g:267:28: 'NOT'
                            {
                            string_literal203=(Token)match(input,62,FOLLOW_62_in_between_expression2210); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal203_tree = (Object)adaptor.create(string_literal203);
                            adaptor.addChild(root_0, string_literal203_tree);
                            }

                            }
                            break;

                    }

                    string_literal204=(Token)match(input,77,FOLLOW_77_in_between_expression2214); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal204_tree = (Object)adaptor.create(string_literal204);
                    adaptor.addChild(root_0, string_literal204_tree);
                    }
                    pushFollow(FOLLOW_datetime_expression_in_between_expression2216);
                    datetime_expression205=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression205.getTree());
                    string_literal206=(Token)match(input,AND,FOLLOW_AND_in_between_expression2218); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal206_tree = (Object)adaptor.create(string_literal206);
                    adaptor.addChild(root_0, string_literal206_tree);
                    }
                    pushFollow(FOLLOW_datetime_expression_in_between_expression2220);
                    datetime_expression207=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression207.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "between_expression"

    public static class in_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "in_expression"
    // JPA.g:269:1: in_expression : path_expression ( 'NOT' )? 'IN' in_expression_right_part ;
    public final JPAParser.in_expression_return in_expression() throws RecognitionException {
        JPAParser.in_expression_return retval = new JPAParser.in_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal209=null;
        Token string_literal210=null;
        JPAParser.path_expression_return path_expression208 = null;

        JPAParser.in_expression_right_part_return in_expression_right_part211 = null;


        Object string_literal209_tree=null;
        Object string_literal210_tree=null;

        try {
            // JPA.g:270:5: ( path_expression ( 'NOT' )? 'IN' in_expression_right_part )
            // JPA.g:270:7: path_expression ( 'NOT' )? 'IN' in_expression_right_part
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_in_expression2232);
            path_expression208=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression208.getTree());
            // JPA.g:270:23: ( 'NOT' )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==62) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // JPA.g:270:24: 'NOT'
                    {
                    string_literal209=(Token)match(input,62,FOLLOW_62_in_in_expression2235); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal209_tree = (Object)adaptor.create(string_literal209);
                    adaptor.addChild(root_0, string_literal209_tree);
                    }

                    }
                    break;

            }

            string_literal210=(Token)match(input,58,FOLLOW_58_in_in_expression2239); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal210_tree = (Object)adaptor.create(string_literal210);
            adaptor.addChild(root_0, string_literal210_tree);
            }
            pushFollow(FOLLOW_in_expression_right_part_in_in_expression2241);
            in_expression_right_part211=in_expression_right_part();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression_right_part211.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "in_expression"

    public static class in_expression_right_part_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "in_expression_right_part"
    // JPA.g:272:1: in_expression_right_part : ( '(' in_item ( ',' in_item )* ')' | subquery );
    public final JPAParser.in_expression_right_part_return in_expression_right_part() throws RecognitionException {
        JPAParser.in_expression_right_part_return retval = new JPAParser.in_expression_right_part_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal212=null;
        Token char_literal214=null;
        Token char_literal216=null;
        JPAParser.in_item_return in_item213 = null;

        JPAParser.in_item_return in_item215 = null;

        JPAParser.subquery_return subquery217 = null;


        Object char_literal212_tree=null;
        Object char_literal214_tree=null;
        Object char_literal216_tree=null;

        try {
            // JPA.g:273:5: ( '(' in_item ( ',' in_item )* ')' | subquery )
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==LPAREN) ) {
                alt62=1;
            }
            else if ( (LA62_0==56) ) {
                alt62=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 62, 0, input);

                throw nvae;
            }
            switch (alt62) {
                case 1 :
                    // JPA.g:273:7: '(' in_item ( ',' in_item )* ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal212=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression_right_part2253); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal212_tree = (Object)adaptor.create(char_literal212);
                    adaptor.addChild(root_0, char_literal212_tree);
                    }
                    pushFollow(FOLLOW_in_item_in_in_expression_right_part2255);
                    in_item213=in_item();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item213.getTree());
                    // JPA.g:273:19: ( ',' in_item )*
                    loop61:
                    do {
                        int alt61=2;
                        int LA61_0 = input.LA(1);

                        if ( (LA61_0==54) ) {
                            alt61=1;
                        }


                        switch (alt61) {
                    	case 1 :
                    	    // JPA.g:273:20: ',' in_item
                    	    {
                    	    char_literal214=(Token)match(input,54,FOLLOW_54_in_in_expression_right_part2258); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal214_tree = (Object)adaptor.create(char_literal214);
                    	    adaptor.addChild(root_0, char_literal214_tree);
                    	    }
                    	    pushFollow(FOLLOW_in_item_in_in_expression_right_part2260);
                    	    in_item215=in_item();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item215.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop61;
                        }
                    } while (true);

                    char_literal216=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression_right_part2264); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal216_tree = (Object)adaptor.create(char_literal216);
                    adaptor.addChild(root_0, char_literal216_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:274:7: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_in_expression_right_part2272);
                    subquery217=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery217.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "in_expression_right_part"

    public static class in_item_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "in_item"
    // JPA.g:276:1: in_item : ( literal | input_parameter );
    public final JPAParser.in_item_return in_item() throws RecognitionException {
        JPAParser.in_item_return retval = new JPAParser.in_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.literal_return literal218 = null;

        JPAParser.input_parameter_return input_parameter219 = null;



        try {
            // JPA.g:277:5: ( literal | input_parameter )
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==WORD) ) {
                alt63=1;
            }
            else if ( (LA63_0==NAMED_PARAMETER||(LA63_0>=115 && LA63_0<=116)) ) {
                alt63=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 63, 0, input);

                throw nvae;
            }
            switch (alt63) {
                case 1 :
                    // JPA.g:277:7: literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_literal_in_in_item2284);
                    literal218=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal218.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:278:7: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_in_item2292);
                    input_parameter219=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter219.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "in_item"

    public static class like_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "like_expression"
    // JPA.g:280:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' ESCAPE_CHARACTER )? ;
    public final JPAParser.like_expression_return like_expression() throws RecognitionException {
        JPAParser.like_expression_return retval = new JPAParser.like_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal221=null;
        Token string_literal222=null;
        Token string_literal225=null;
        Token ESCAPE_CHARACTER226=null;
        JPAParser.string_expression_return string_expression220 = null;

        JPAParser.pattern_value_return pattern_value223 = null;

        JPAParser.input_parameter_return input_parameter224 = null;


        Object string_literal221_tree=null;
        Object string_literal222_tree=null;
        Object string_literal225_tree=null;
        Object ESCAPE_CHARACTER226_tree=null;

        try {
            // JPA.g:281:5: ( string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' ESCAPE_CHARACTER )? )
            // JPA.g:281:7: string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' ESCAPE_CHARACTER )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_string_expression_in_like_expression2304);
            string_expression220=string_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression220.getTree());
            // JPA.g:281:25: ( 'NOT' )?
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==62) ) {
                alt64=1;
            }
            switch (alt64) {
                case 1 :
                    // JPA.g:281:26: 'NOT'
                    {
                    string_literal221=(Token)match(input,62,FOLLOW_62_in_like_expression2307); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal221_tree = (Object)adaptor.create(string_literal221);
                    adaptor.addChild(root_0, string_literal221_tree);
                    }

                    }
                    break;

            }

            string_literal222=(Token)match(input,78,FOLLOW_78_in_like_expression2311); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal222_tree = (Object)adaptor.create(string_literal222);
            adaptor.addChild(root_0, string_literal222_tree);
            }
            // JPA.g:281:41: ( pattern_value | input_parameter )
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==WORD) ) {
                alt65=1;
            }
            else if ( (LA65_0==NAMED_PARAMETER||(LA65_0>=115 && LA65_0<=116)) ) {
                alt65=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 65, 0, input);

                throw nvae;
            }
            switch (alt65) {
                case 1 :
                    // JPA.g:281:42: pattern_value
                    {
                    pushFollow(FOLLOW_pattern_value_in_like_expression2314);
                    pattern_value223=pattern_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value223.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:281:58: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_like_expression2318);
                    input_parameter224=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter224.getTree());

                    }
                    break;

            }

            // JPA.g:281:74: ( 'ESCAPE' ESCAPE_CHARACTER )?
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==79) ) {
                alt66=1;
            }
            switch (alt66) {
                case 1 :
                    // JPA.g:281:75: 'ESCAPE' ESCAPE_CHARACTER
                    {
                    string_literal225=(Token)match(input,79,FOLLOW_79_in_like_expression2321); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal225_tree = (Object)adaptor.create(string_literal225);
                    adaptor.addChild(root_0, string_literal225_tree);
                    }
                    ESCAPE_CHARACTER226=(Token)match(input,ESCAPE_CHARACTER,FOLLOW_ESCAPE_CHARACTER_in_like_expression2323); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ESCAPE_CHARACTER226_tree = (Object)adaptor.create(ESCAPE_CHARACTER226);
                    adaptor.addChild(root_0, ESCAPE_CHARACTER226_tree);
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "like_expression"

    public static class null_comparison_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "null_comparison_expression"
    // JPA.g:283:1: null_comparison_expression : ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' ;
    public final JPAParser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
        JPAParser.null_comparison_expression_return retval = new JPAParser.null_comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal229=null;
        Token string_literal230=null;
        Token string_literal231=null;
        JPAParser.path_expression_return path_expression227 = null;

        JPAParser.input_parameter_return input_parameter228 = null;


        Object string_literal229_tree=null;
        Object string_literal230_tree=null;
        Object string_literal231_tree=null;

        try {
            // JPA.g:284:5: ( ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' )
            // JPA.g:284:7: ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL'
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:284:7: ( path_expression | input_parameter )
            int alt67=2;
            int LA67_0 = input.LA(1);

            if ( (LA67_0==WORD) ) {
                alt67=1;
            }
            else if ( (LA67_0==NAMED_PARAMETER||(LA67_0>=115 && LA67_0<=116)) ) {
                alt67=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 67, 0, input);

                throw nvae;
            }
            switch (alt67) {
                case 1 :
                    // JPA.g:284:8: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_null_comparison_expression2338);
                    path_expression227=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression227.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:284:26: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_null_comparison_expression2342);
                    input_parameter228=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter228.getTree());

                    }
                    break;

            }

            string_literal229=(Token)match(input,80,FOLLOW_80_in_null_comparison_expression2345); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal229_tree = (Object)adaptor.create(string_literal229);
            adaptor.addChild(root_0, string_literal229_tree);
            }
            // JPA.g:284:48: ( 'NOT' )?
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==62) ) {
                alt68=1;
            }
            switch (alt68) {
                case 1 :
                    // JPA.g:284:49: 'NOT'
                    {
                    string_literal230=(Token)match(input,62,FOLLOW_62_in_null_comparison_expression2348); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal230_tree = (Object)adaptor.create(string_literal230);
                    adaptor.addChild(root_0, string_literal230_tree);
                    }

                    }
                    break;

            }

            string_literal231=(Token)match(input,81,FOLLOW_81_in_null_comparison_expression2352); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal231_tree = (Object)adaptor.create(string_literal231);
            adaptor.addChild(root_0, string_literal231_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "null_comparison_expression"

    public static class empty_collection_comparison_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "empty_collection_comparison_expression"
    // JPA.g:286:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
    public final JPAParser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
        JPAParser.empty_collection_comparison_expression_return retval = new JPAParser.empty_collection_comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal233=null;
        Token string_literal234=null;
        Token string_literal235=null;
        JPAParser.path_expression_return path_expression232 = null;


        Object string_literal233_tree=null;
        Object string_literal234_tree=null;
        Object string_literal235_tree=null;

        try {
            // JPA.g:287:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
            // JPA.g:287:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression2364);
            path_expression232=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression232.getTree());
            string_literal233=(Token)match(input,80,FOLLOW_80_in_empty_collection_comparison_expression2366); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal233_tree = (Object)adaptor.create(string_literal233);
            adaptor.addChild(root_0, string_literal233_tree);
            }
            // JPA.g:287:28: ( 'NOT' )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==62) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // JPA.g:287:29: 'NOT'
                    {
                    string_literal234=(Token)match(input,62,FOLLOW_62_in_empty_collection_comparison_expression2369); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal234_tree = (Object)adaptor.create(string_literal234);
                    adaptor.addChild(root_0, string_literal234_tree);
                    }

                    }
                    break;

            }

            string_literal235=(Token)match(input,82,FOLLOW_82_in_empty_collection_comparison_expression2373); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal235_tree = (Object)adaptor.create(string_literal235);
            adaptor.addChild(root_0, string_literal235_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "empty_collection_comparison_expression"

    public static class collection_member_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "collection_member_expression"
    // JPA.g:289:1: collection_member_expression : entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
    public final JPAParser.collection_member_expression_return collection_member_expression() throws RecognitionException {
        JPAParser.collection_member_expression_return retval = new JPAParser.collection_member_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal237=null;
        Token string_literal238=null;
        Token string_literal239=null;
        JPAParser.entity_expression_return entity_expression236 = null;

        JPAParser.path_expression_return path_expression240 = null;


        Object string_literal237_tree=null;
        Object string_literal238_tree=null;
        Object string_literal239_tree=null;

        try {
            // JPA.g:290:5: ( entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
            // JPA.g:290:7: entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_entity_expression_in_collection_member_expression2385);
            entity_expression236=entity_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression236.getTree());
            // JPA.g:290:25: ( 'NOT' )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==62) ) {
                alt70=1;
            }
            switch (alt70) {
                case 1 :
                    // JPA.g:290:26: 'NOT'
                    {
                    string_literal237=(Token)match(input,62,FOLLOW_62_in_collection_member_expression2388); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal237_tree = (Object)adaptor.create(string_literal237);
                    adaptor.addChild(root_0, string_literal237_tree);
                    }

                    }
                    break;

            }

            string_literal238=(Token)match(input,83,FOLLOW_83_in_collection_member_expression2392); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal238_tree = (Object)adaptor.create(string_literal238);
            adaptor.addChild(root_0, string_literal238_tree);
            }
            // JPA.g:290:43: ( 'OF' )?
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==84) ) {
                alt71=1;
            }
            switch (alt71) {
                case 1 :
                    // JPA.g:290:44: 'OF'
                    {
                    string_literal239=(Token)match(input,84,FOLLOW_84_in_collection_member_expression2395); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal239_tree = (Object)adaptor.create(string_literal239);
                    adaptor.addChild(root_0, string_literal239_tree);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_path_expression_in_collection_member_expression2399);
            path_expression240=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression240.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "collection_member_expression"

    public static class exists_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "exists_expression"
    // JPA.g:292:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
    public final JPAParser.exists_expression_return exists_expression() throws RecognitionException {
        JPAParser.exists_expression_return retval = new JPAParser.exists_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal241=null;
        Token string_literal242=null;
        JPAParser.subquery_return subquery243 = null;


        Object string_literal241_tree=null;
        Object string_literal242_tree=null;

        try {
            // JPA.g:293:5: ( ( 'NOT' )? 'EXISTS' subquery )
            // JPA.g:293:7: ( 'NOT' )? 'EXISTS' subquery
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:293:7: ( 'NOT' )?
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==62) ) {
                alt72=1;
            }
            switch (alt72) {
                case 1 :
                    // JPA.g:293:8: 'NOT'
                    {
                    string_literal241=(Token)match(input,62,FOLLOW_62_in_exists_expression2412); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal241_tree = (Object)adaptor.create(string_literal241);
                    adaptor.addChild(root_0, string_literal241_tree);
                    }

                    }
                    break;

            }

            string_literal242=(Token)match(input,85,FOLLOW_85_in_exists_expression2416); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal242_tree = (Object)adaptor.create(string_literal242);
            adaptor.addChild(root_0, string_literal242_tree);
            }
            pushFollow(FOLLOW_subquery_in_exists_expression2418);
            subquery243=subquery();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery243.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "exists_expression"

    public static class all_or_any_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "all_or_any_expression"
    // JPA.g:295:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
    public final JPAParser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
        JPAParser.all_or_any_expression_return retval = new JPAParser.all_or_any_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set244=null;
        JPAParser.subquery_return subquery245 = null;


        Object set244_tree=null;

        try {
            // JPA.g:296:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
            // JPA.g:296:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
            {
            root_0 = (Object)adaptor.nil();

            set244=(Token)input.LT(1);
            if ( (input.LA(1)>=86 && input.LA(1)<=88) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set244));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            pushFollow(FOLLOW_subquery_in_all_or_any_expression2443);
            subquery245=subquery();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery245.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "all_or_any_expression"

    public static class comparison_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "comparison_expression"
    // JPA.g:298:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
    public final JPAParser.comparison_expression_return comparison_expression() throws RecognitionException {
        JPAParser.comparison_expression_return retval = new JPAParser.comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set251=null;
        Token set255=null;
        Token set263=null;
        JPAParser.string_expression_return string_expression246 = null;

        JPAParser.comparison_operator_return comparison_operator247 = null;

        JPAParser.string_expression_return string_expression248 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression249 = null;

        JPAParser.boolean_expression_return boolean_expression250 = null;

        JPAParser.boolean_expression_return boolean_expression252 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression253 = null;

        JPAParser.enum_expression_return enum_expression254 = null;

        JPAParser.enum_expression_return enum_expression256 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression257 = null;

        JPAParser.datetime_expression_return datetime_expression258 = null;

        JPAParser.comparison_operator_return comparison_operator259 = null;

        JPAParser.datetime_expression_return datetime_expression260 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression261 = null;

        JPAParser.entity_expression_return entity_expression262 = null;

        JPAParser.entity_expression_return entity_expression264 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression265 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression266 = null;

        JPAParser.comparison_operator_return comparison_operator267 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression268 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression269 = null;


        Object set251_tree=null;
        Object set255_tree=null;
        Object set263_tree=null;

        try {
            // JPA.g:299:5: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
            int alt79=6;
            alt79 = dfa79.predict(input);
            switch (alt79) {
                case 1 :
                    // JPA.g:299:7: string_expression comparison_operator ( string_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_expression_in_comparison_expression2455);
                    string_expression246=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression246.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2457);
                    comparison_operator247=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator247.getTree());
                    // JPA.g:299:45: ( string_expression | all_or_any_expression )
                    int alt73=2;
                    int LA73_0 = input.LA(1);

                    if ( ((LA73_0>=AVG && LA73_0<=COUNT)||LA73_0==STRINGLITERAL||(LA73_0>=WORD && LA73_0<=NAMED_PARAMETER)||LA73_0==56||(LA73_0>=106 && LA73_0<=110)||(LA73_0>=115 && LA73_0<=116)) ) {
                        alt73=1;
                    }
                    else if ( ((LA73_0>=86 && LA73_0<=88)) ) {
                        alt73=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 73, 0, input);

                        throw nvae;
                    }
                    switch (alt73) {
                        case 1 :
                            // JPA.g:299:46: string_expression
                            {
                            pushFollow(FOLLOW_string_expression_in_comparison_expression2460);
                            string_expression248=string_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression248.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:299:66: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2464);
                            all_or_any_expression249=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression249.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // JPA.g:300:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_expression_in_comparison_expression2473);
                    boolean_expression250=boolean_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression250.getTree());
                    set251=(Token)input.LT(1);
                    if ( (input.LA(1)>=89 && input.LA(1)<=90) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set251));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:300:39: ( boolean_expression | all_or_any_expression )
                    int alt74=2;
                    int LA74_0 = input.LA(1);

                    if ( ((LA74_0>=WORD && LA74_0<=NAMED_PARAMETER)||LA74_0==56||(LA74_0>=115 && LA74_0<=116)||(LA74_0>=118 && LA74_0<=119)) ) {
                        alt74=1;
                    }
                    else if ( ((LA74_0>=86 && LA74_0<=88)) ) {
                        alt74=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 74, 0, input);

                        throw nvae;
                    }
                    switch (alt74) {
                        case 1 :
                            // JPA.g:300:40: boolean_expression
                            {
                            pushFollow(FOLLOW_boolean_expression_in_comparison_expression2484);
                            boolean_expression252=boolean_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression252.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:300:61: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2488);
                            all_or_any_expression253=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression253.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // JPA.g:301:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_expression_in_comparison_expression2497);
                    enum_expression254=enum_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression254.getTree());
                    set255=(Token)input.LT(1);
                    if ( (input.LA(1)>=89 && input.LA(1)<=90) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set255));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:301:34: ( enum_expression | all_or_any_expression )
                    int alt75=2;
                    int LA75_0 = input.LA(1);

                    if ( ((LA75_0>=WORD && LA75_0<=NAMED_PARAMETER)||LA75_0==56||(LA75_0>=115 && LA75_0<=116)) ) {
                        alt75=1;
                    }
                    else if ( ((LA75_0>=86 && LA75_0<=88)) ) {
                        alt75=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 75, 0, input);

                        throw nvae;
                    }
                    switch (alt75) {
                        case 1 :
                            // JPA.g:301:35: enum_expression
                            {
                            pushFollow(FOLLOW_enum_expression_in_comparison_expression2506);
                            enum_expression256=enum_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression256.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:301:53: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2510);
                            all_or_any_expression257=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression257.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // JPA.g:302:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_expression_in_comparison_expression2519);
                    datetime_expression258=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression258.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2521);
                    comparison_operator259=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator259.getTree());
                    // JPA.g:302:47: ( datetime_expression | all_or_any_expression )
                    int alt76=2;
                    int LA76_0 = input.LA(1);

                    if ( ((LA76_0>=AVG && LA76_0<=COUNT)||(LA76_0>=WORD && LA76_0<=NAMED_PARAMETER)||LA76_0==56||(LA76_0>=103 && LA76_0<=105)||(LA76_0>=115 && LA76_0<=116)) ) {
                        alt76=1;
                    }
                    else if ( ((LA76_0>=86 && LA76_0<=88)) ) {
                        alt76=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 76, 0, input);

                        throw nvae;
                    }
                    switch (alt76) {
                        case 1 :
                            // JPA.g:302:48: datetime_expression
                            {
                            pushFollow(FOLLOW_datetime_expression_in_comparison_expression2524);
                            datetime_expression260=datetime_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression260.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:302:70: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2528);
                            all_or_any_expression261=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression261.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // JPA.g:303:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_entity_expression_in_comparison_expression2537);
                    entity_expression262=entity_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression262.getTree());
                    set263=(Token)input.LT(1);
                    if ( (input.LA(1)>=89 && input.LA(1)<=90) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set263));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:303:38: ( entity_expression | all_or_any_expression )
                    int alt77=2;
                    int LA77_0 = input.LA(1);

                    if ( ((LA77_0>=WORD && LA77_0<=NAMED_PARAMETER)||(LA77_0>=115 && LA77_0<=116)) ) {
                        alt77=1;
                    }
                    else if ( ((LA77_0>=86 && LA77_0<=88)) ) {
                        alt77=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 77, 0, input);

                        throw nvae;
                    }
                    switch (alt77) {
                        case 1 :
                            // JPA.g:303:39: entity_expression
                            {
                            pushFollow(FOLLOW_entity_expression_in_comparison_expression2548);
                            entity_expression264=entity_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression264.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:303:59: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2552);
                            all_or_any_expression265=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression265.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 6 :
                    // JPA.g:304:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression2561);
                    arithmetic_expression266=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression266.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2563);
                    comparison_operator267=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator267.getTree());
                    // JPA.g:304:49: ( arithmetic_expression | all_or_any_expression )
                    int alt78=2;
                    int LA78_0 = input.LA(1);

                    if ( ((LA78_0>=AVG && LA78_0<=COUNT)||LA78_0==LPAREN||LA78_0==INT_NUMERAL||(LA78_0>=WORD && LA78_0<=NAMED_PARAMETER)||LA78_0==56||(LA78_0>=65 && LA78_0<=66)||(LA78_0>=97 && LA78_0<=102)||(LA78_0>=114 && LA78_0<=116)) ) {
                        alt78=1;
                    }
                    else if ( ((LA78_0>=86 && LA78_0<=88)) ) {
                        alt78=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 78, 0, input);

                        throw nvae;
                    }
                    switch (alt78) {
                        case 1 :
                            // JPA.g:304:50: arithmetic_expression
                            {
                            pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression2566);
                            arithmetic_expression268=arithmetic_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression268.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:304:74: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2570);
                            all_or_any_expression269=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression269.getTree());

                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "comparison_expression"

    public static class comparison_operator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "comparison_operator"
    // JPA.g:306:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
    public final JPAParser.comparison_operator_return comparison_operator() throws RecognitionException {
        JPAParser.comparison_operator_return retval = new JPAParser.comparison_operator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set270=null;

        Object set270_tree=null;

        try {
            // JPA.g:307:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set270=(Token)input.LT(1);
            if ( (input.LA(1)>=89 && input.LA(1)<=94) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set270));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "comparison_operator"

    public static class arithmetic_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arithmetic_expression"
    // JPA.g:314:1: arithmetic_expression : ( simple_arithmetic_expression | subquery );
    public final JPAParser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
        JPAParser.arithmetic_expression_return retval = new JPAParser.arithmetic_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression271 = null;

        JPAParser.subquery_return subquery272 = null;



        try {
            // JPA.g:315:5: ( simple_arithmetic_expression | subquery )
            int alt80=2;
            int LA80_0 = input.LA(1);

            if ( ((LA80_0>=AVG && LA80_0<=COUNT)||LA80_0==LPAREN||LA80_0==INT_NUMERAL||(LA80_0>=WORD && LA80_0<=NAMED_PARAMETER)||(LA80_0>=65 && LA80_0<=66)||(LA80_0>=97 && LA80_0<=102)||(LA80_0>=114 && LA80_0<=116)) ) {
                alt80=1;
            }
            else if ( (LA80_0==56) ) {
                alt80=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 80, 0, input);

                throw nvae;
            }
            switch (alt80) {
                case 1 :
                    // JPA.g:315:7: simple_arithmetic_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simple_arithmetic_expression_in_arithmetic_expression2635);
                    simple_arithmetic_expression271=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression271.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:316:7: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_arithmetic_expression2643);
                    subquery272=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery272.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arithmetic_expression"

    public static class simple_arithmetic_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "simple_arithmetic_expression"
    // JPA.g:318:1: simple_arithmetic_expression : ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )* ;
    public final JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression() throws RecognitionException {
        JPAParser.simple_arithmetic_expression_return retval = new JPAParser.simple_arithmetic_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set274=null;
        JPAParser.arithmetic_term_return arithmetic_term273 = null;

        JPAParser.arithmetic_term_return arithmetic_term275 = null;


        Object set274_tree=null;

        try {
            // JPA.g:319:5: ( ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )* )
            // JPA.g:319:7: ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:319:7: ( arithmetic_term )
            // JPA.g:319:8: arithmetic_term
            {
            pushFollow(FOLLOW_arithmetic_term_in_simple_arithmetic_expression2656);
            arithmetic_term273=arithmetic_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term273.getTree());

            }

            // JPA.g:319:25: ( ( '+' | '-' ) arithmetic_term )*
            loop81:
            do {
                int alt81=2;
                int LA81_0 = input.LA(1);

                if ( ((LA81_0>=65 && LA81_0<=66)) ) {
                    alt81=1;
                }


                switch (alt81) {
            	case 1 :
            	    // JPA.g:319:26: ( '+' | '-' ) arithmetic_term
            	    {
            	    set274=(Token)input.LT(1);
            	    if ( (input.LA(1)>=65 && input.LA(1)<=66) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set274));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmetic_term_in_simple_arithmetic_expression2670);
            	    arithmetic_term275=arithmetic_term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term275.getTree());

            	    }
            	    break;

            	default :
            	    break loop81;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "simple_arithmetic_expression"

    public static class arithmetic_term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arithmetic_term"
    // JPA.g:321:1: arithmetic_term : ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )* ;
    public final JPAParser.arithmetic_term_return arithmetic_term() throws RecognitionException {
        JPAParser.arithmetic_term_return retval = new JPAParser.arithmetic_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set277=null;
        JPAParser.arithmetic_factor_return arithmetic_factor276 = null;

        JPAParser.arithmetic_factor_return arithmetic_factor278 = null;


        Object set277_tree=null;

        try {
            // JPA.g:322:5: ( ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )* )
            // JPA.g:322:7: ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:322:7: ( arithmetic_factor )
            // JPA.g:322:8: arithmetic_factor
            {
            pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term2685);
            arithmetic_factor276=arithmetic_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor276.getTree());

            }

            // JPA.g:322:27: ( ( '*' | '/' ) arithmetic_factor )*
            loop82:
            do {
                int alt82=2;
                int LA82_0 = input.LA(1);

                if ( ((LA82_0>=95 && LA82_0<=96)) ) {
                    alt82=1;
                }


                switch (alt82) {
            	case 1 :
            	    // JPA.g:322:28: ( '*' | '/' ) arithmetic_factor
            	    {
            	    set277=(Token)input.LT(1);
            	    if ( (input.LA(1)>=95 && input.LA(1)<=96) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set277));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term2699);
            	    arithmetic_factor278=arithmetic_factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor278.getTree());

            	    }
            	    break;

            	default :
            	    break loop82;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arithmetic_term"

    public static class arithmetic_factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arithmetic_factor"
    // JPA.g:324:1: arithmetic_factor : ( '+' | '-' )? arithmetic_primary ;
    public final JPAParser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
        JPAParser.arithmetic_factor_return retval = new JPAParser.arithmetic_factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set279=null;
        JPAParser.arithmetic_primary_return arithmetic_primary280 = null;


        Object set279_tree=null;

        try {
            // JPA.g:325:5: ( ( '+' | '-' )? arithmetic_primary )
            // JPA.g:325:7: ( '+' | '-' )? arithmetic_primary
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:325:7: ( '+' | '-' )?
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( ((LA83_0>=65 && LA83_0<=66)) ) {
                alt83=1;
            }
            switch (alt83) {
                case 1 :
                    // JPA.g:
                    {
                    set279=(Token)input.LT(1);
                    if ( (input.LA(1)>=65 && input.LA(1)<=66) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set279));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }

            pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor2724);
            arithmetic_primary280=arithmetic_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary280.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arithmetic_factor"

    public static class arithmetic_primary_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arithmetic_primary"
    // JPA.g:327:1: arithmetic_primary : ( path_expression | numeric_literal | '(' simple_arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression );
    public final JPAParser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
        JPAParser.arithmetic_primary_return retval = new JPAParser.arithmetic_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal283=null;
        Token char_literal285=null;
        JPAParser.path_expression_return path_expression281 = null;

        JPAParser.numeric_literal_return numeric_literal282 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression284 = null;

        JPAParser.input_parameter_return input_parameter286 = null;

        JPAParser.functions_returning_numerics_return functions_returning_numerics287 = null;

        JPAParser.aggregate_expression_return aggregate_expression288 = null;


        Object char_literal283_tree=null;
        Object char_literal285_tree=null;

        try {
            // JPA.g:328:5: ( path_expression | numeric_literal | '(' simple_arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression )
            int alt84=6;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt84=1;
                }
                break;
            case INT_NUMERAL:
            case 114:
                {
                alt84=2;
                }
                break;
            case LPAREN:
                {
                alt84=3;
                }
                break;
            case NAMED_PARAMETER:
            case 115:
            case 116:
                {
                alt84=4;
                }
                break;
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
                {
                alt84=5;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt84=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 84, 0, input);

                throw nvae;
            }

            switch (alt84) {
                case 1 :
                    // JPA.g:328:7: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_arithmetic_primary2736);
                    path_expression281=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression281.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:329:7: numeric_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary2744);
                    numeric_literal282=numeric_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal282.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:330:7: '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal283=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary2752); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal283_tree = (Object)adaptor.create(char_literal283);
                    adaptor.addChild(root_0, char_literal283_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_arithmetic_primary2753);
                    simple_arithmetic_expression284=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression284.getTree());
                    char_literal285=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary2754); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal285_tree = (Object)adaptor.create(char_literal285);
                    adaptor.addChild(root_0, char_literal285_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:331:7: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_arithmetic_primary2762);
                    input_parameter286=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter286.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:332:7: functions_returning_numerics
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary2770);
                    functions_returning_numerics287=functions_returning_numerics();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics287.getTree());

                    }
                    break;
                case 6 :
                    // JPA.g:333:7: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary2778);
                    aggregate_expression288=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression288.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arithmetic_primary"

    public static class string_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "string_expression"
    // JPA.g:335:1: string_expression : ( string_primary | subquery );
    public final JPAParser.string_expression_return string_expression() throws RecognitionException {
        JPAParser.string_expression_return retval = new JPAParser.string_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.string_primary_return string_primary289 = null;

        JPAParser.subquery_return subquery290 = null;



        try {
            // JPA.g:336:5: ( string_primary | subquery )
            int alt85=2;
            int LA85_0 = input.LA(1);

            if ( ((LA85_0>=AVG && LA85_0<=COUNT)||LA85_0==STRINGLITERAL||(LA85_0>=WORD && LA85_0<=NAMED_PARAMETER)||(LA85_0>=106 && LA85_0<=110)||(LA85_0>=115 && LA85_0<=116)) ) {
                alt85=1;
            }
            else if ( (LA85_0==56) ) {
                alt85=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 85, 0, input);

                throw nvae;
            }
            switch (alt85) {
                case 1 :
                    // JPA.g:336:7: string_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_primary_in_string_expression2790);
                    string_primary289=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary289.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:336:24: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_string_expression2794);
                    subquery290=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery290.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "string_expression"

    public static class string_primary_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "string_primary"
    // JPA.g:338:1: string_primary : ( path_expression | STRINGLITERAL | input_parameter | functions_returning_strings | aggregate_expression );
    public final JPAParser.string_primary_return string_primary() throws RecognitionException {
        JPAParser.string_primary_return retval = new JPAParser.string_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRINGLITERAL292=null;
        JPAParser.path_expression_return path_expression291 = null;

        JPAParser.input_parameter_return input_parameter293 = null;

        JPAParser.functions_returning_strings_return functions_returning_strings294 = null;

        JPAParser.aggregate_expression_return aggregate_expression295 = null;


        Object STRINGLITERAL292_tree=null;

        try {
            // JPA.g:339:5: ( path_expression | STRINGLITERAL | input_parameter | functions_returning_strings | aggregate_expression )
            int alt86=5;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt86=1;
                }
                break;
            case STRINGLITERAL:
                {
                alt86=2;
                }
                break;
            case NAMED_PARAMETER:
            case 115:
            case 116:
                {
                alt86=3;
                }
                break;
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
                {
                alt86=4;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt86=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 86, 0, input);

                throw nvae;
            }

            switch (alt86) {
                case 1 :
                    // JPA.g:339:7: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_string_primary2806);
                    path_expression291=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression291.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:340:7: STRINGLITERAL
                    {
                    root_0 = (Object)adaptor.nil();

                    STRINGLITERAL292=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_string_primary2814); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRINGLITERAL292_tree = (Object)adaptor.create(STRINGLITERAL292);
                    adaptor.addChild(root_0, STRINGLITERAL292_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:341:7: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_string_primary2822);
                    input_parameter293=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter293.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:342:7: functions_returning_strings
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_strings_in_string_primary2830);
                    functions_returning_strings294=functions_returning_strings();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings294.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:343:7: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_string_primary2838);
                    aggregate_expression295=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression295.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "string_primary"

    public static class datetime_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "datetime_expression"
    // JPA.g:345:1: datetime_expression : ( datetime_primary | subquery );
    public final JPAParser.datetime_expression_return datetime_expression() throws RecognitionException {
        JPAParser.datetime_expression_return retval = new JPAParser.datetime_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.datetime_primary_return datetime_primary296 = null;

        JPAParser.subquery_return subquery297 = null;



        try {
            // JPA.g:346:5: ( datetime_primary | subquery )
            int alt87=2;
            int LA87_0 = input.LA(1);

            if ( ((LA87_0>=AVG && LA87_0<=COUNT)||(LA87_0>=WORD && LA87_0<=NAMED_PARAMETER)||(LA87_0>=103 && LA87_0<=105)||(LA87_0>=115 && LA87_0<=116)) ) {
                alt87=1;
            }
            else if ( (LA87_0==56) ) {
                alt87=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 87, 0, input);

                throw nvae;
            }
            switch (alt87) {
                case 1 :
                    // JPA.g:346:7: datetime_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_primary_in_datetime_expression2850);
                    datetime_primary296=datetime_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_primary296.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:347:7: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_datetime_expression2858);
                    subquery297=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery297.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "datetime_expression"

    public static class datetime_primary_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "datetime_primary"
    // JPA.g:349:1: datetime_primary : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression );
    public final JPAParser.datetime_primary_return datetime_primary() throws RecognitionException {
        JPAParser.datetime_primary_return retval = new JPAParser.datetime_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression298 = null;

        JPAParser.input_parameter_return input_parameter299 = null;

        JPAParser.functions_returning_datetime_return functions_returning_datetime300 = null;

        JPAParser.aggregate_expression_return aggregate_expression301 = null;



        try {
            // JPA.g:350:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression )
            int alt88=4;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt88=1;
                }
                break;
            case NAMED_PARAMETER:
            case 115:
            case 116:
                {
                alt88=2;
                }
                break;
            case 103:
            case 104:
            case 105:
                {
                alt88=3;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt88=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 88, 0, input);

                throw nvae;
            }

            switch (alt88) {
                case 1 :
                    // JPA.g:350:7: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_datetime_primary2870);
                    path_expression298=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression298.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:351:7: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_datetime_primary2878);
                    input_parameter299=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter299.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:352:7: functions_returning_datetime
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_datetime_in_datetime_primary2886);
                    functions_returning_datetime300=functions_returning_datetime();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime300.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:353:7: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_datetime_primary2894);
                    aggregate_expression301=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression301.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "datetime_primary"

    public static class boolean_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_expression"
    // JPA.g:355:1: boolean_expression : ( boolean_primary | subquery );
    public final JPAParser.boolean_expression_return boolean_expression() throws RecognitionException {
        JPAParser.boolean_expression_return retval = new JPAParser.boolean_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.boolean_primary_return boolean_primary302 = null;

        JPAParser.subquery_return subquery303 = null;



        try {
            // JPA.g:356:5: ( boolean_primary | subquery )
            int alt89=2;
            int LA89_0 = input.LA(1);

            if ( ((LA89_0>=WORD && LA89_0<=NAMED_PARAMETER)||(LA89_0>=115 && LA89_0<=116)||(LA89_0>=118 && LA89_0<=119)) ) {
                alt89=1;
            }
            else if ( (LA89_0==56) ) {
                alt89=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 89, 0, input);

                throw nvae;
            }
            switch (alt89) {
                case 1 :
                    // JPA.g:356:7: boolean_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_primary_in_boolean_expression2906);
                    boolean_primary302=boolean_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_primary302.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:357:7: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_boolean_expression2914);
                    subquery303=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery303.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "boolean_expression"

    public static class boolean_primary_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_primary"
    // JPA.g:359:1: boolean_primary : ( path_expression | boolean_literal | input_parameter );
    public final JPAParser.boolean_primary_return boolean_primary() throws RecognitionException {
        JPAParser.boolean_primary_return retval = new JPAParser.boolean_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression304 = null;

        JPAParser.boolean_literal_return boolean_literal305 = null;

        JPAParser.input_parameter_return input_parameter306 = null;



        try {
            // JPA.g:360:5: ( path_expression | boolean_literal | input_parameter )
            int alt90=3;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt90=1;
                }
                break;
            case 118:
            case 119:
                {
                alt90=2;
                }
                break;
            case NAMED_PARAMETER:
            case 115:
            case 116:
                {
                alt90=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 90, 0, input);

                throw nvae;
            }

            switch (alt90) {
                case 1 :
                    // JPA.g:360:7: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_boolean_primary2926);
                    path_expression304=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression304.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:361:7: boolean_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_literal_in_boolean_primary2934);
                    boolean_literal305=boolean_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal305.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:362:7: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_boolean_primary2942);
                    input_parameter306=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter306.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "boolean_primary"

    public static class enum_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "enum_expression"
    // JPA.g:364:1: enum_expression : ( enum_primary | subquery );
    public final JPAParser.enum_expression_return enum_expression() throws RecognitionException {
        JPAParser.enum_expression_return retval = new JPAParser.enum_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.enum_primary_return enum_primary307 = null;

        JPAParser.subquery_return subquery308 = null;



        try {
            // JPA.g:365:5: ( enum_primary | subquery )
            int alt91=2;
            int LA91_0 = input.LA(1);

            if ( ((LA91_0>=WORD && LA91_0<=NAMED_PARAMETER)||(LA91_0>=115 && LA91_0<=116)) ) {
                alt91=1;
            }
            else if ( (LA91_0==56) ) {
                alt91=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 91, 0, input);

                throw nvae;
            }
            switch (alt91) {
                case 1 :
                    // JPA.g:365:7: enum_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_primary_in_enum_expression2954);
                    enum_primary307=enum_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_primary307.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:366:7: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_enum_expression2962);
                    subquery308=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery308.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "enum_expression"

    public static class enum_primary_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "enum_primary"
    // JPA.g:368:1: enum_primary : ( path_expression | enum_literal | input_parameter );
    public final JPAParser.enum_primary_return enum_primary() throws RecognitionException {
        JPAParser.enum_primary_return retval = new JPAParser.enum_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression309 = null;

        JPAParser.enum_literal_return enum_literal310 = null;

        JPAParser.input_parameter_return input_parameter311 = null;



        try {
            // JPA.g:369:5: ( path_expression | enum_literal | input_parameter )
            int alt92=3;
            int LA92_0 = input.LA(1);

            if ( (LA92_0==WORD) ) {
                int LA92_1 = input.LA(2);

                if ( (LA92_1==57) ) {
                    alt92=1;
                }
                else if ( (LA92_1==EOF||LA92_1==HAVING||(LA92_1>=OR && LA92_1<=AND)||LA92_1==RPAREN||(LA92_1>=ORDER && LA92_1<=GROUP)||(LA92_1>=89 && LA92_1<=90)) ) {
                    alt92=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 92, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA92_0==NAMED_PARAMETER||(LA92_0>=115 && LA92_0<=116)) ) {
                alt92=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 92, 0, input);

                throw nvae;
            }
            switch (alt92) {
                case 1 :
                    // JPA.g:369:7: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_enum_primary2974);
                    path_expression309=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression309.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:370:7: enum_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_literal_in_enum_primary2982);
                    enum_literal310=enum_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal310.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:371:7: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_enum_primary2990);
                    input_parameter311=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter311.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "enum_primary"

    public static class entity_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "entity_expression"
    // JPA.g:373:1: entity_expression : ( path_expression | simple_entity_expression );
    public final JPAParser.entity_expression_return entity_expression() throws RecognitionException {
        JPAParser.entity_expression_return retval = new JPAParser.entity_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression312 = null;

        JPAParser.simple_entity_expression_return simple_entity_expression313 = null;



        try {
            // JPA.g:374:5: ( path_expression | simple_entity_expression )
            int alt93=2;
            int LA93_0 = input.LA(1);

            if ( (LA93_0==WORD) ) {
                int LA93_1 = input.LA(2);

                if ( (LA93_1==57) ) {
                    alt93=1;
                }
                else if ( (LA93_1==EOF||LA93_1==HAVING||(LA93_1>=OR && LA93_1<=AND)||LA93_1==RPAREN||(LA93_1>=ORDER && LA93_1<=GROUP)||LA93_1==62||LA93_1==83||(LA93_1>=89 && LA93_1<=90)) ) {
                    alt93=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 93, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA93_0==NAMED_PARAMETER||(LA93_0>=115 && LA93_0<=116)) ) {
                alt93=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 93, 0, input);

                throw nvae;
            }
            switch (alt93) {
                case 1 :
                    // JPA.g:374:7: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_entity_expression3002);
                    path_expression312=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression312.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:375:7: simple_entity_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3010);
                    simple_entity_expression313=simple_entity_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression313.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "entity_expression"

    public static class simple_entity_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "simple_entity_expression"
    // JPA.g:377:1: simple_entity_expression : ( identification_variable | input_parameter );
    public final JPAParser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
        JPAParser.simple_entity_expression_return retval = new JPAParser.simple_entity_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.identification_variable_return identification_variable314 = null;

        JPAParser.input_parameter_return input_parameter315 = null;



        try {
            // JPA.g:378:5: ( identification_variable | input_parameter )
            int alt94=2;
            int LA94_0 = input.LA(1);

            if ( (LA94_0==WORD) ) {
                alt94=1;
            }
            else if ( (LA94_0==NAMED_PARAMETER||(LA94_0>=115 && LA94_0<=116)) ) {
                alt94=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 94, 0, input);

                throw nvae;
            }
            switch (alt94) {
                case 1 :
                    // JPA.g:378:7: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3022);
                    identification_variable314=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable314.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:379:7: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3030);
                    input_parameter315=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter315.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "simple_entity_expression"

    public static class functions_returning_numerics_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functions_returning_numerics"
    // JPA.g:381:1: functions_returning_numerics : ( 'LENGTH' '(' string_primary ')' | 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')' | 'ABS' '(' simple_arithmetic_expression ')' | 'SQRT' '(' simple_arithmetic_expression ')' | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'SIZE' '(' path_expression ')' );
    public final JPAParser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
        JPAParser.functions_returning_numerics_return retval = new JPAParser.functions_returning_numerics_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal316=null;
        Token char_literal317=null;
        Token char_literal319=null;
        Token string_literal320=null;
        Token char_literal321=null;
        Token char_literal323=null;
        Token char_literal325=null;
        Token char_literal327=null;
        Token string_literal328=null;
        Token char_literal329=null;
        Token char_literal331=null;
        Token string_literal332=null;
        Token char_literal333=null;
        Token char_literal335=null;
        Token string_literal336=null;
        Token char_literal337=null;
        Token char_literal339=null;
        Token char_literal341=null;
        Token string_literal342=null;
        Token char_literal343=null;
        Token char_literal345=null;
        JPAParser.string_primary_return string_primary318 = null;

        JPAParser.string_primary_return string_primary322 = null;

        JPAParser.string_primary_return string_primary324 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression326 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression330 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression334 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression338 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression340 = null;

        JPAParser.path_expression_return path_expression344 = null;


        Object string_literal316_tree=null;
        Object char_literal317_tree=null;
        Object char_literal319_tree=null;
        Object string_literal320_tree=null;
        Object char_literal321_tree=null;
        Object char_literal323_tree=null;
        Object char_literal325_tree=null;
        Object char_literal327_tree=null;
        Object string_literal328_tree=null;
        Object char_literal329_tree=null;
        Object char_literal331_tree=null;
        Object string_literal332_tree=null;
        Object char_literal333_tree=null;
        Object char_literal335_tree=null;
        Object string_literal336_tree=null;
        Object char_literal337_tree=null;
        Object char_literal339_tree=null;
        Object char_literal341_tree=null;
        Object string_literal342_tree=null;
        Object char_literal343_tree=null;
        Object char_literal345_tree=null;

        try {
            // JPA.g:382:5: ( 'LENGTH' '(' string_primary ')' | 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')' | 'ABS' '(' simple_arithmetic_expression ')' | 'SQRT' '(' simple_arithmetic_expression ')' | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'SIZE' '(' path_expression ')' )
            int alt96=6;
            switch ( input.LA(1) ) {
            case 97:
                {
                alt96=1;
                }
                break;
            case 98:
                {
                alt96=2;
                }
                break;
            case 99:
                {
                alt96=3;
                }
                break;
            case 100:
                {
                alt96=4;
                }
                break;
            case 101:
                {
                alt96=5;
                }
                break;
            case 102:
                {
                alt96=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 96, 0, input);

                throw nvae;
            }

            switch (alt96) {
                case 1 :
                    // JPA.g:382:7: 'LENGTH' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal316=(Token)match(input,97,FOLLOW_97_in_functions_returning_numerics3042); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal316_tree = (Object)adaptor.create(string_literal316);
                    adaptor.addChild(root_0, string_literal316_tree);
                    }
                    char_literal317=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics3044); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal317_tree = (Object)adaptor.create(char_literal317);
                    adaptor.addChild(root_0, char_literal317_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics3045);
                    string_primary318=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary318.getTree());
                    char_literal319=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3046); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal319_tree = (Object)adaptor.create(char_literal319);
                    adaptor.addChild(root_0, char_literal319_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:383:7: 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal320=(Token)match(input,98,FOLLOW_98_in_functions_returning_numerics3054); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal320_tree = (Object)adaptor.create(string_literal320);
                    adaptor.addChild(root_0, string_literal320_tree);
                    }
                    char_literal321=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics3056); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal321_tree = (Object)adaptor.create(char_literal321);
                    adaptor.addChild(root_0, char_literal321_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics3057);
                    string_primary322=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary322.getTree());
                    char_literal323=(Token)match(input,54,FOLLOW_54_in_functions_returning_numerics3058); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal323_tree = (Object)adaptor.create(char_literal323);
                    adaptor.addChild(root_0, char_literal323_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics3060);
                    string_primary324=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary324.getTree());
                    // JPA.g:383:51: ( ',' simple_arithmetic_expression )?
                    int alt95=2;
                    int LA95_0 = input.LA(1);

                    if ( (LA95_0==54) ) {
                        alt95=1;
                    }
                    switch (alt95) {
                        case 1 :
                            // JPA.g:383:52: ',' simple_arithmetic_expression
                            {
                            char_literal325=(Token)match(input,54,FOLLOW_54_in_functions_returning_numerics3062); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal325_tree = (Object)adaptor.create(char_literal325);
                            adaptor.addChild(root_0, char_literal325_tree);
                            }
                            pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics3064);
                            simple_arithmetic_expression326=simple_arithmetic_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression326.getTree());

                            }
                            break;

                    }

                    char_literal327=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3067); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal327_tree = (Object)adaptor.create(char_literal327);
                    adaptor.addChild(root_0, char_literal327_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:384:7: 'ABS' '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal328=(Token)match(input,99,FOLLOW_99_in_functions_returning_numerics3075); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal328_tree = (Object)adaptor.create(string_literal328);
                    adaptor.addChild(root_0, string_literal328_tree);
                    }
                    char_literal329=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics3077); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal329_tree = (Object)adaptor.create(char_literal329);
                    adaptor.addChild(root_0, char_literal329_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics3078);
                    simple_arithmetic_expression330=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression330.getTree());
                    char_literal331=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3079); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal331_tree = (Object)adaptor.create(char_literal331);
                    adaptor.addChild(root_0, char_literal331_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:385:7: 'SQRT' '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal332=(Token)match(input,100,FOLLOW_100_in_functions_returning_numerics3087); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal332_tree = (Object)adaptor.create(string_literal332);
                    adaptor.addChild(root_0, string_literal332_tree);
                    }
                    char_literal333=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics3089); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal333_tree = (Object)adaptor.create(char_literal333);
                    adaptor.addChild(root_0, char_literal333_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics3090);
                    simple_arithmetic_expression334=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression334.getTree());
                    char_literal335=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3091); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal335_tree = (Object)adaptor.create(char_literal335);
                    adaptor.addChild(root_0, char_literal335_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:386:7: 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal336=(Token)match(input,101,FOLLOW_101_in_functions_returning_numerics3099); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal336_tree = (Object)adaptor.create(string_literal336);
                    adaptor.addChild(root_0, string_literal336_tree);
                    }
                    char_literal337=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics3101); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal337_tree = (Object)adaptor.create(char_literal337);
                    adaptor.addChild(root_0, char_literal337_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics3102);
                    simple_arithmetic_expression338=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression338.getTree());
                    char_literal339=(Token)match(input,54,FOLLOW_54_in_functions_returning_numerics3103); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal339_tree = (Object)adaptor.create(char_literal339);
                    adaptor.addChild(root_0, char_literal339_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics3105);
                    simple_arithmetic_expression340=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression340.getTree());
                    char_literal341=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3106); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal341_tree = (Object)adaptor.create(char_literal341);
                    adaptor.addChild(root_0, char_literal341_tree);
                    }

                    }
                    break;
                case 6 :
                    // JPA.g:387:7: 'SIZE' '(' path_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal342=(Token)match(input,102,FOLLOW_102_in_functions_returning_numerics3114); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal342_tree = (Object)adaptor.create(string_literal342);
                    adaptor.addChild(root_0, string_literal342_tree);
                    }
                    char_literal343=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics3116); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal343_tree = (Object)adaptor.create(char_literal343);
                    adaptor.addChild(root_0, char_literal343_tree);
                    }
                    pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3117);
                    path_expression344=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression344.getTree());
                    char_literal345=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3118); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal345_tree = (Object)adaptor.create(char_literal345);
                    adaptor.addChild(root_0, char_literal345_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "functions_returning_numerics"

    public static class functions_returning_datetime_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functions_returning_datetime"
    // JPA.g:389:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
    public final JPAParser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
        JPAParser.functions_returning_datetime_return retval = new JPAParser.functions_returning_datetime_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set346=null;

        Object set346_tree=null;

        try {
            // JPA.g:390:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set346=(Token)input.LT(1);
            if ( (input.LA(1)>=103 && input.LA(1)<=105) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set346));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "functions_returning_datetime"

    public static class functions_returning_strings_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functions_returning_strings"
    // JPA.g:394:1: functions_returning_strings : ( 'CONCAT' '(' string_primary ',' string_primary ')' | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')' | 'LOWER' '(' string_primary ')' | 'UPPER' '(' string_primary ')' );
    public final JPAParser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
        JPAParser.functions_returning_strings_return retval = new JPAParser.functions_returning_strings_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal347=null;
        Token char_literal348=null;
        Token char_literal350=null;
        Token char_literal352=null;
        Token string_literal353=null;
        Token char_literal354=null;
        Token char_literal356=null;
        Token char_literal358=null;
        Token char_literal360=null;
        Token string_literal361=null;
        Token char_literal362=null;
        Token TRIM_CHARACTER364=null;
        Token string_literal365=null;
        Token char_literal367=null;
        Token string_literal368=null;
        Token char_literal369=null;
        Token char_literal371=null;
        Token string_literal372=null;
        Token char_literal373=null;
        Token char_literal375=null;
        JPAParser.string_primary_return string_primary349 = null;

        JPAParser.string_primary_return string_primary351 = null;

        JPAParser.string_primary_return string_primary355 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression357 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression359 = null;

        JPAParser.trim_specification_return trim_specification363 = null;

        JPAParser.string_primary_return string_primary366 = null;

        JPAParser.string_primary_return string_primary370 = null;

        JPAParser.string_primary_return string_primary374 = null;


        Object string_literal347_tree=null;
        Object char_literal348_tree=null;
        Object char_literal350_tree=null;
        Object char_literal352_tree=null;
        Object string_literal353_tree=null;
        Object char_literal354_tree=null;
        Object char_literal356_tree=null;
        Object char_literal358_tree=null;
        Object char_literal360_tree=null;
        Object string_literal361_tree=null;
        Object char_literal362_tree=null;
        Object TRIM_CHARACTER364_tree=null;
        Object string_literal365_tree=null;
        Object char_literal367_tree=null;
        Object string_literal368_tree=null;
        Object char_literal369_tree=null;
        Object char_literal371_tree=null;
        Object string_literal372_tree=null;
        Object char_literal373_tree=null;
        Object char_literal375_tree=null;

        try {
            // JPA.g:395:5: ( 'CONCAT' '(' string_primary ',' string_primary ')' | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')' | 'LOWER' '(' string_primary ')' | 'UPPER' '(' string_primary ')' )
            int alt100=5;
            switch ( input.LA(1) ) {
            case 106:
                {
                alt100=1;
                }
                break;
            case 107:
                {
                alt100=2;
                }
                break;
            case 108:
                {
                alt100=3;
                }
                break;
            case 109:
                {
                alt100=4;
                }
                break;
            case 110:
                {
                alt100=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 100, 0, input);

                throw nvae;
            }

            switch (alt100) {
                case 1 :
                    // JPA.g:395:7: 'CONCAT' '(' string_primary ',' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal347=(Token)match(input,106,FOLLOW_106_in_functions_returning_strings3158); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal347_tree = (Object)adaptor.create(string_literal347);
                    adaptor.addChild(root_0, string_literal347_tree);
                    }
                    char_literal348=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3160); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal348_tree = (Object)adaptor.create(char_literal348);
                    adaptor.addChild(root_0, char_literal348_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings3161);
                    string_primary349=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary349.getTree());
                    char_literal350=(Token)match(input,54,FOLLOW_54_in_functions_returning_strings3162); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal350_tree = (Object)adaptor.create(char_literal350);
                    adaptor.addChild(root_0, char_literal350_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings3164);
                    string_primary351=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary351.getTree());
                    char_literal352=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3165); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal352_tree = (Object)adaptor.create(char_literal352);
                    adaptor.addChild(root_0, char_literal352_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:396:7: 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal353=(Token)match(input,107,FOLLOW_107_in_functions_returning_strings3173); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal353_tree = (Object)adaptor.create(string_literal353);
                    adaptor.addChild(root_0, string_literal353_tree);
                    }
                    char_literal354=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3175); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal354_tree = (Object)adaptor.create(char_literal354);
                    adaptor.addChild(root_0, char_literal354_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings3176);
                    string_primary355=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary355.getTree());
                    char_literal356=(Token)match(input,54,FOLLOW_54_in_functions_returning_strings3177); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal356_tree = (Object)adaptor.create(char_literal356);
                    adaptor.addChild(root_0, char_literal356_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_strings3178);
                    simple_arithmetic_expression357=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression357.getTree());
                    char_literal358=(Token)match(input,54,FOLLOW_54_in_functions_returning_strings3179); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal358_tree = (Object)adaptor.create(char_literal358);
                    adaptor.addChild(root_0, char_literal358_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_strings3181);
                    simple_arithmetic_expression359=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression359.getTree());
                    char_literal360=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3182); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal360_tree = (Object)adaptor.create(char_literal360);
                    adaptor.addChild(root_0, char_literal360_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:397:7: 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal361=(Token)match(input,108,FOLLOW_108_in_functions_returning_strings3190); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal361_tree = (Object)adaptor.create(string_literal361);
                    adaptor.addChild(root_0, string_literal361_tree);
                    }
                    char_literal362=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3192); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal362_tree = (Object)adaptor.create(char_literal362);
                    adaptor.addChild(root_0, char_literal362_tree);
                    }
                    // JPA.g:397:17: ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )?
                    int alt99=2;
                    int LA99_0 = input.LA(1);

                    if ( (LA99_0==TRIM_CHARACTER||LA99_0==53||(LA99_0>=111 && LA99_0<=113)) ) {
                        alt99=1;
                    }
                    switch (alt99) {
                        case 1 :
                            // JPA.g:397:18: ( trim_specification )? ( TRIM_CHARACTER )? 'FROM'
                            {
                            // JPA.g:397:18: ( trim_specification )?
                            int alt97=2;
                            int LA97_0 = input.LA(1);

                            if ( ((LA97_0>=111 && LA97_0<=113)) ) {
                                alt97=1;
                            }
                            switch (alt97) {
                                case 1 :
                                    // JPA.g:397:19: trim_specification
                                    {
                                    pushFollow(FOLLOW_trim_specification_in_functions_returning_strings3195);
                                    trim_specification363=trim_specification();

                                    state._fsp--;
                                    if (state.failed) return retval;
                                    if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification363.getTree());

                                    }
                                    break;

                            }

                            // JPA.g:397:40: ( TRIM_CHARACTER )?
                            int alt98=2;
                            int LA98_0 = input.LA(1);

                            if ( (LA98_0==TRIM_CHARACTER) ) {
                                alt98=1;
                            }
                            switch (alt98) {
                                case 1 :
                                    // JPA.g:397:41: TRIM_CHARACTER
                                    {
                                    TRIM_CHARACTER364=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_functions_returning_strings3200); if (state.failed) return retval;
                                    if ( state.backtracking==0 ) {
                                    TRIM_CHARACTER364_tree = (Object)adaptor.create(TRIM_CHARACTER364);
                                    adaptor.addChild(root_0, TRIM_CHARACTER364_tree);
                                    }

                                    }
                                    break;

                            }

                            string_literal365=(Token)match(input,53,FOLLOW_53_in_functions_returning_strings3204); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal365_tree = (Object)adaptor.create(string_literal365);
                            adaptor.addChild(root_0, string_literal365_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings3208);
                    string_primary366=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary366.getTree());
                    char_literal367=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3209); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal367_tree = (Object)adaptor.create(char_literal367);
                    adaptor.addChild(root_0, char_literal367_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:398:7: 'LOWER' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal368=(Token)match(input,109,FOLLOW_109_in_functions_returning_strings3217); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal368_tree = (Object)adaptor.create(string_literal368);
                    adaptor.addChild(root_0, string_literal368_tree);
                    }
                    char_literal369=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3219); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal369_tree = (Object)adaptor.create(char_literal369);
                    adaptor.addChild(root_0, char_literal369_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings3220);
                    string_primary370=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary370.getTree());
                    char_literal371=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3221); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal371_tree = (Object)adaptor.create(char_literal371);
                    adaptor.addChild(root_0, char_literal371_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:399:7: 'UPPER' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal372=(Token)match(input,110,FOLLOW_110_in_functions_returning_strings3229); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal372_tree = (Object)adaptor.create(string_literal372);
                    adaptor.addChild(root_0, string_literal372_tree);
                    }
                    char_literal373=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3231); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal373_tree = (Object)adaptor.create(char_literal373);
                    adaptor.addChild(root_0, char_literal373_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings3232);
                    string_primary374=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary374.getTree());
                    char_literal375=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3233); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal375_tree = (Object)adaptor.create(char_literal375);
                    adaptor.addChild(root_0, char_literal375_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "functions_returning_strings"

    public static class trim_specification_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "trim_specification"
    // JPA.g:401:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
    public final JPAParser.trim_specification_return trim_specification() throws RecognitionException {
        JPAParser.trim_specification_return retval = new JPAParser.trim_specification_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set376=null;

        Object set376_tree=null;

        try {
            // JPA.g:402:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set376=(Token)input.LT(1);
            if ( (input.LA(1)>=111 && input.LA(1)<=113) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set376));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "trim_specification"

    public static class abstract_schema_name_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "abstract_schema_name"
    // JPA.g:407:1: abstract_schema_name : WORD ;
    public final JPAParser.abstract_schema_name_return abstract_schema_name() throws RecognitionException {
        JPAParser.abstract_schema_name_return retval = new JPAParser.abstract_schema_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD377=null;

        Object WORD377_tree=null;

        try {
            // JPA.g:408:5: ( WORD )
            // JPA.g:408:7: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD377=(Token)match(input,WORD,FOLLOW_WORD_in_abstract_schema_name3274); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD377_tree = (Object)adaptor.create(WORD377);
            adaptor.addChild(root_0, WORD377_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "abstract_schema_name"

    public static class pattern_value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pattern_value"
    // JPA.g:411:1: pattern_value : WORD ;
    public final JPAParser.pattern_value_return pattern_value() throws RecognitionException {
        JPAParser.pattern_value_return retval = new JPAParser.pattern_value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD378=null;

        Object WORD378_tree=null;

        try {
            // JPA.g:412:5: ( WORD )
            // JPA.g:412:7: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD378=(Token)match(input,WORD,FOLLOW_WORD_in_pattern_value3287); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD378_tree = (Object)adaptor.create(WORD378);
            adaptor.addChild(root_0, WORD378_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "pattern_value"

    public static class numeric_literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numeric_literal"
    // JPA.g:415:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
    public final JPAParser.numeric_literal_return numeric_literal() throws RecognitionException {
        JPAParser.numeric_literal_return retval = new JPAParser.numeric_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal379=null;
        Token INT_NUMERAL380=null;

        Object string_literal379_tree=null;
        Object INT_NUMERAL380_tree=null;

        try {
            // JPA.g:416:5: ( ( '0x' )? INT_NUMERAL )
            // JPA.g:416:7: ( '0x' )? INT_NUMERAL
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:416:7: ( '0x' )?
            int alt101=2;
            int LA101_0 = input.LA(1);

            if ( (LA101_0==114) ) {
                alt101=1;
            }
            switch (alt101) {
                case 1 :
                    // JPA.g:416:8: '0x'
                    {
                    string_literal379=(Token)match(input,114,FOLLOW_114_in_numeric_literal3301); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal379_tree = (Object)adaptor.create(string_literal379);
                    adaptor.addChild(root_0, string_literal379_tree);
                    }

                    }
                    break;

            }

            INT_NUMERAL380=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal3305); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            INT_NUMERAL380_tree = (Object)adaptor.create(INT_NUMERAL380);
            adaptor.addChild(root_0, INT_NUMERAL380_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "numeric_literal"

    public static class input_parameter_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "input_parameter"
    // JPA.g:418:1: input_parameter : ( '?' INT_NUMERAL -> ^( T_PARAMETER[] '?' INT_NUMERAL ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) );
    public final JPAParser.input_parameter_return input_parameter() throws RecognitionException {
        JPAParser.input_parameter_return retval = new JPAParser.input_parameter_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal381=null;
        Token INT_NUMERAL382=null;
        Token NAMED_PARAMETER383=null;
        Token string_literal384=null;
        Token WORD385=null;
        Token char_literal386=null;

        Object char_literal381_tree=null;
        Object INT_NUMERAL382_tree=null;
        Object NAMED_PARAMETER383_tree=null;
        Object string_literal384_tree=null;
        Object WORD385_tree=null;
        Object char_literal386_tree=null;
        RewriteRuleTokenStream stream_116=new RewriteRuleTokenStream(adaptor,"token 116");
        RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
        RewriteRuleTokenStream stream_117=new RewriteRuleTokenStream(adaptor,"token 117");
        RewriteRuleTokenStream stream_115=new RewriteRuleTokenStream(adaptor,"token 115");
        RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
        RewriteRuleTokenStream stream_INT_NUMERAL=new RewriteRuleTokenStream(adaptor,"token INT_NUMERAL");

        try {
            // JPA.g:419:5: ( '?' INT_NUMERAL -> ^( T_PARAMETER[] '?' INT_NUMERAL ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
            int alt102=3;
            switch ( input.LA(1) ) {
            case 115:
                {
                alt102=1;
                }
                break;
            case NAMED_PARAMETER:
                {
                alt102=2;
                }
                break;
            case 116:
                {
                alt102=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 102, 0, input);

                throw nvae;
            }

            switch (alt102) {
                case 1 :
                    // JPA.g:419:7: '?' INT_NUMERAL
                    {
                    char_literal381=(Token)match(input,115,FOLLOW_115_in_input_parameter3318); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_115.add(char_literal381);

                    INT_NUMERAL382=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_input_parameter3320); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT_NUMERAL.add(INT_NUMERAL382);



                    // AST REWRITE
                    // elements: 115, INT_NUMERAL
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 419:23: -> ^( T_PARAMETER[] '?' INT_NUMERAL )
                    {
                        // JPA.g:419:26: ^( T_PARAMETER[] '?' INT_NUMERAL )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);

                        adaptor.addChild(root_1, stream_115.nextNode());
                        adaptor.addChild(root_1, stream_INT_NUMERAL.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // JPA.g:420:7: NAMED_PARAMETER
                    {
                    NAMED_PARAMETER383=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter3343); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER383);



                    // AST REWRITE
                    // elements: NAMED_PARAMETER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 420:23: -> ^( T_PARAMETER[] NAMED_PARAMETER )
                    {
                        // JPA.g:420:26: ^( T_PARAMETER[] NAMED_PARAMETER )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);

                        adaptor.addChild(root_1, stream_NAMED_PARAMETER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // JPA.g:421:7: '${' WORD '}'
                    {
                    string_literal384=(Token)match(input,116,FOLLOW_116_in_input_parameter3364); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_116.add(string_literal384);

                    WORD385=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter3366); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WORD.add(WORD385);

                    char_literal386=(Token)match(input,117,FOLLOW_117_in_input_parameter3368); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_117.add(char_literal386);



                    // AST REWRITE
                    // elements: 117, 116, WORD
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 421:21: -> ^( T_PARAMETER[] '${' WORD '}' )
                    {
                        // JPA.g:421:24: ^( T_PARAMETER[] '${' WORD '}' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);

                        adaptor.addChild(root_1, stream_116.nextNode());
                        adaptor.addChild(root_1, stream_WORD.nextNode());
                        adaptor.addChild(root_1, stream_117.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "input_parameter"

    public static class literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literal"
    // JPA.g:423:1: literal : WORD ;
    public final JPAParser.literal_return literal() throws RecognitionException {
        JPAParser.literal_return retval = new JPAParser.literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD387=null;

        Object WORD387_tree=null;

        try {
            // JPA.g:424:5: ( WORD )
            // JPA.g:424:7: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD387=(Token)match(input,WORD,FOLLOW_WORD_in_literal3396); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD387_tree = (Object)adaptor.create(WORD387);
            adaptor.addChild(root_0, WORD387_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "literal"

    public static class constructor_name_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "constructor_name"
    // JPA.g:426:1: constructor_name : WORD ;
    public final JPAParser.constructor_name_return constructor_name() throws RecognitionException {
        JPAParser.constructor_name_return retval = new JPAParser.constructor_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD388=null;

        Object WORD388_tree=null;

        try {
            // JPA.g:427:5: ( WORD )
            // JPA.g:427:7: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD388=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name3408); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD388_tree = (Object)adaptor.create(WORD388);
            adaptor.addChild(root_0, WORD388_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "constructor_name"

    public static class enum_literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "enum_literal"
    // JPA.g:429:1: enum_literal : WORD ;
    public final JPAParser.enum_literal_return enum_literal() throws RecognitionException {
        JPAParser.enum_literal_return retval = new JPAParser.enum_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD389=null;

        Object WORD389_tree=null;

        try {
            // JPA.g:430:5: ( WORD )
            // JPA.g:430:7: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD389=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal3452); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD389_tree = (Object)adaptor.create(WORD389);
            adaptor.addChild(root_0, WORD389_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "enum_literal"

    public static class boolean_literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_literal"
    // JPA.g:432:1: boolean_literal : ( 'true' | 'false' );
    public final JPAParser.boolean_literal_return boolean_literal() throws RecognitionException {
        JPAParser.boolean_literal_return retval = new JPAParser.boolean_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set390=null;

        Object set390_tree=null;

        try {
            // JPA.g:433:5: ( 'true' | 'false' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set390=(Token)input.LT(1);
            if ( (input.LA(1)>=118 && input.LA(1)<=119) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set390));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "boolean_literal"

    public static class field_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "field"
    // JPA.g:437:1: field : ( WORD | 'GROUP' );
    public final JPAParser.field_return field() throws RecognitionException {
        JPAParser.field_return retval = new JPAParser.field_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set391=null;

        Object set391_tree=null;

        try {
            // JPA.g:438:5: ( WORD | 'GROUP' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set391=(Token)input.LT(1);
            if ( input.LA(1)==GROUP||input.LA(1)==WORD ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set391));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "field"

    public static class identification_variable_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "identification_variable"
    // JPA.g:440:1: identification_variable : WORD ;
    public final JPAParser.identification_variable_return identification_variable() throws RecognitionException {
        JPAParser.identification_variable_return retval = new JPAParser.identification_variable_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD392=null;

        Object WORD392_tree=null;

        try {
            // JPA.g:441:5: ( WORD )
            // JPA.g:441:7: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD392=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable3501); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD392_tree = (Object)adaptor.create(WORD392);
            adaptor.addChild(root_0, WORD392_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "identification_variable"

    public static class parameter_name_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameter_name"
    // JPA.g:443:1: parameter_name : WORD ( '.' WORD )* ;
    public final JPAParser.parameter_name_return parameter_name() throws RecognitionException {
        JPAParser.parameter_name_return retval = new JPAParser.parameter_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD393=null;
        Token char_literal394=null;
        Token WORD395=null;

        Object WORD393_tree=null;
        Object char_literal394_tree=null;
        Object WORD395_tree=null;

        try {
            // JPA.g:444:5: ( WORD ( '.' WORD )* )
            // JPA.g:444:7: WORD ( '.' WORD )*
            {
            root_0 = (Object)adaptor.nil();

            WORD393=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name3513); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD393_tree = (Object)adaptor.create(WORD393);
            adaptor.addChild(root_0, WORD393_tree);
            }
            // JPA.g:444:12: ( '.' WORD )*
            loop103:
            do {
                int alt103=2;
                int LA103_0 = input.LA(1);

                if ( (LA103_0==57) ) {
                    alt103=1;
                }


                switch (alt103) {
            	case 1 :
            	    // JPA.g:444:13: '.' WORD
            	    {
            	    char_literal394=(Token)match(input,57,FOLLOW_57_in_parameter_name3516); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal394_tree = (Object)adaptor.create(char_literal394);
            	    adaptor.addChild(root_0, char_literal394_tree);
            	    }
            	    WORD395=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name3519); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    WORD395_tree = (Object)adaptor.create(WORD395);
            	    adaptor.addChild(root_0, WORD395_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop103;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "parameter_name"

    // $ANTLR start synpred20_JPA
    public final void synpred20_JPA_fragment() throws RecognitionException {   
        // JPA.g:130:47: ( field )
        // JPA.g:130:47: field
        {
        pushFollow(FOLLOW_field_in_synpred20_JPA887);
        field();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred20_JPA

    // $ANTLR start synpred23_JPA
    public final void synpred23_JPA_fragment() throws RecognitionException {   
        // JPA.g:140:49: ( field )
        // JPA.g:140:49: field
        {
        pushFollow(FOLLOW_field_in_synpred23_JPA982);
        field();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred23_JPA

    // $ANTLR start synpred57_JPA
    public final void synpred57_JPA_fragment() throws RecognitionException {   
        // JPA.g:228:8: ( 'NOT' )
        // JPA.g:228:8: 'NOT'
        {
        match(input,62,FOLLOW_62_in_synpred57_JPA1823); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred57_JPA

    // $ANTLR start synpred58_JPA
    public final void synpred58_JPA_fragment() throws RecognitionException {   
        // JPA.g:228:7: ( ( 'NOT' )? simple_cond_expression )
        // JPA.g:228:7: ( 'NOT' )? simple_cond_expression
        {
        // JPA.g:228:7: ( 'NOT' )?
        int alt108=2;
        int LA108_0 = input.LA(1);

        if ( (LA108_0==62) ) {
            int LA108_1 = input.LA(2);

            if ( (synpred57_JPA()) ) {
                alt108=1;
            }
        }
        switch (alt108) {
            case 1 :
                // JPA.g:228:8: 'NOT'
                {
                match(input,62,FOLLOW_62_in_synpred58_JPA1823); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_simple_cond_expression_in_synpred58_JPA1827);
        simple_cond_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred58_JPA

    // $ANTLR start synpred59_JPA
    public final void synpred59_JPA_fragment() throws RecognitionException {   
        // JPA.g:232:7: ( comparison_expression )
        // JPA.g:232:7: comparison_expression
        {
        pushFollow(FOLLOW_comparison_expression_in_synpred59_JPA1866);
        comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred59_JPA

    // $ANTLR start synpred60_JPA
    public final void synpred60_JPA_fragment() throws RecognitionException {   
        // JPA.g:233:7: ( between_expression )
        // JPA.g:233:7: between_expression
        {
        pushFollow(FOLLOW_between_expression_in_synpred60_JPA1874);
        between_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred60_JPA

    // $ANTLR start synpred61_JPA
    public final void synpred61_JPA_fragment() throws RecognitionException {   
        // JPA.g:234:7: ( like_expression )
        // JPA.g:234:7: like_expression
        {
        pushFollow(FOLLOW_like_expression_in_synpred61_JPA1882);
        like_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred61_JPA

    // $ANTLR start synpred62_JPA
    public final void synpred62_JPA_fragment() throws RecognitionException {   
        // JPA.g:235:7: ( in_expression )
        // JPA.g:235:7: in_expression
        {
        pushFollow(FOLLOW_in_expression_in_synpred62_JPA1890);
        in_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred62_JPA

    // $ANTLR start synpred63_JPA
    public final void synpred63_JPA_fragment() throws RecognitionException {   
        // JPA.g:236:7: ( null_comparison_expression )
        // JPA.g:236:7: null_comparison_expression
        {
        pushFollow(FOLLOW_null_comparison_expression_in_synpred63_JPA1898);
        null_comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred63_JPA

    // $ANTLR start synpred64_JPA
    public final void synpred64_JPA_fragment() throws RecognitionException {   
        // JPA.g:237:7: ( empty_collection_comparison_expression )
        // JPA.g:237:7: empty_collection_comparison_expression
        {
        pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred64_JPA1906);
        empty_collection_comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred64_JPA

    // $ANTLR start synpred65_JPA
    public final void synpred65_JPA_fragment() throws RecognitionException {   
        // JPA.g:238:7: ( collection_member_expression )
        // JPA.g:238:7: collection_member_expression
        {
        pushFollow(FOLLOW_collection_member_expression_in_synpred65_JPA1914);
        collection_member_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred65_JPA

    // $ANTLR start synpred84_JPA
    public final void synpred84_JPA_fragment() throws RecognitionException {   
        // JPA.g:265:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
        // JPA.g:265:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
        {
        pushFollow(FOLLOW_arithmetic_expression_in_synpred84_JPA2165);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:265:29: ( 'NOT' )?
        int alt109=2;
        int LA109_0 = input.LA(1);

        if ( (LA109_0==62) ) {
            alt109=1;
        }
        switch (alt109) {
            case 1 :
                // JPA.g:265:30: 'NOT'
                {
                match(input,62,FOLLOW_62_in_synpred84_JPA2168); if (state.failed) return ;

                }
                break;

        }

        match(input,77,FOLLOW_77_in_synpred84_JPA2172); if (state.failed) return ;
        pushFollow(FOLLOW_arithmetic_expression_in_synpred84_JPA2174);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,AND,FOLLOW_AND_in_synpred84_JPA2176); if (state.failed) return ;
        pushFollow(FOLLOW_arithmetic_expression_in_synpred84_JPA2178);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred84_JPA

    // $ANTLR start synpred86_JPA
    public final void synpred86_JPA_fragment() throws RecognitionException {   
        // JPA.g:266:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
        // JPA.g:266:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
        {
        pushFollow(FOLLOW_string_expression_in_synpred86_JPA2186);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:266:25: ( 'NOT' )?
        int alt110=2;
        int LA110_0 = input.LA(1);

        if ( (LA110_0==62) ) {
            alt110=1;
        }
        switch (alt110) {
            case 1 :
                // JPA.g:266:26: 'NOT'
                {
                match(input,62,FOLLOW_62_in_synpred86_JPA2189); if (state.failed) return ;

                }
                break;

        }

        match(input,77,FOLLOW_77_in_synpred86_JPA2193); if (state.failed) return ;
        pushFollow(FOLLOW_string_expression_in_synpred86_JPA2195);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,AND,FOLLOW_AND_in_synpred86_JPA2197); if (state.failed) return ;
        pushFollow(FOLLOW_string_expression_in_synpred86_JPA2199);
        string_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred86_JPA

    // $ANTLR start synpred104_JPA
    public final void synpred104_JPA_fragment() throws RecognitionException {   
        // JPA.g:299:7: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) )
        // JPA.g:299:7: string_expression comparison_operator ( string_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_string_expression_in_synpred104_JPA2455);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_comparison_operator_in_synpred104_JPA2457);
        comparison_operator();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:299:45: ( string_expression | all_or_any_expression )
        int alt112=2;
        int LA112_0 = input.LA(1);

        if ( ((LA112_0>=AVG && LA112_0<=COUNT)||LA112_0==STRINGLITERAL||(LA112_0>=WORD && LA112_0<=NAMED_PARAMETER)||LA112_0==56||(LA112_0>=106 && LA112_0<=110)||(LA112_0>=115 && LA112_0<=116)) ) {
            alt112=1;
        }
        else if ( ((LA112_0>=86 && LA112_0<=88)) ) {
            alt112=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 112, 0, input);

            throw nvae;
        }
        switch (alt112) {
            case 1 :
                // JPA.g:299:46: string_expression
                {
                pushFollow(FOLLOW_string_expression_in_synpred104_JPA2460);
                string_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:299:66: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred104_JPA2464);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred104_JPA

    // $ANTLR start synpred107_JPA
    public final void synpred107_JPA_fragment() throws RecognitionException {   
        // JPA.g:300:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
        // JPA.g:300:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_boolean_expression_in_synpred107_JPA2473);
        boolean_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=89 && input.LA(1)<=90) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:300:39: ( boolean_expression | all_or_any_expression )
        int alt113=2;
        int LA113_0 = input.LA(1);

        if ( ((LA113_0>=WORD && LA113_0<=NAMED_PARAMETER)||LA113_0==56||(LA113_0>=115 && LA113_0<=116)||(LA113_0>=118 && LA113_0<=119)) ) {
            alt113=1;
        }
        else if ( ((LA113_0>=86 && LA113_0<=88)) ) {
            alt113=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 113, 0, input);

            throw nvae;
        }
        switch (alt113) {
            case 1 :
                // JPA.g:300:40: boolean_expression
                {
                pushFollow(FOLLOW_boolean_expression_in_synpred107_JPA2484);
                boolean_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:300:61: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred107_JPA2488);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred107_JPA

    // $ANTLR start synpred110_JPA
    public final void synpred110_JPA_fragment() throws RecognitionException {   
        // JPA.g:301:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
        // JPA.g:301:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_enum_expression_in_synpred110_JPA2497);
        enum_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=89 && input.LA(1)<=90) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:301:34: ( enum_expression | all_or_any_expression )
        int alt114=2;
        int LA114_0 = input.LA(1);

        if ( ((LA114_0>=WORD && LA114_0<=NAMED_PARAMETER)||LA114_0==56||(LA114_0>=115 && LA114_0<=116)) ) {
            alt114=1;
        }
        else if ( ((LA114_0>=86 && LA114_0<=88)) ) {
            alt114=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 114, 0, input);

            throw nvae;
        }
        switch (alt114) {
            case 1 :
                // JPA.g:301:35: enum_expression
                {
                pushFollow(FOLLOW_enum_expression_in_synpred110_JPA2506);
                enum_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:301:53: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred110_JPA2510);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred110_JPA

    // $ANTLR start synpred112_JPA
    public final void synpred112_JPA_fragment() throws RecognitionException {   
        // JPA.g:302:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
        // JPA.g:302:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_datetime_expression_in_synpred112_JPA2519);
        datetime_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_comparison_operator_in_synpred112_JPA2521);
        comparison_operator();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:302:47: ( datetime_expression | all_or_any_expression )
        int alt115=2;
        int LA115_0 = input.LA(1);

        if ( ((LA115_0>=AVG && LA115_0<=COUNT)||(LA115_0>=WORD && LA115_0<=NAMED_PARAMETER)||LA115_0==56||(LA115_0>=103 && LA115_0<=105)||(LA115_0>=115 && LA115_0<=116)) ) {
            alt115=1;
        }
        else if ( ((LA115_0>=86 && LA115_0<=88)) ) {
            alt115=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 115, 0, input);

            throw nvae;
        }
        switch (alt115) {
            case 1 :
                // JPA.g:302:48: datetime_expression
                {
                pushFollow(FOLLOW_datetime_expression_in_synpred112_JPA2524);
                datetime_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:302:70: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred112_JPA2528);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred112_JPA

    // $ANTLR start synpred115_JPA
    public final void synpred115_JPA_fragment() throws RecognitionException {   
        // JPA.g:303:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
        // JPA.g:303:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_entity_expression_in_synpred115_JPA2537);
        entity_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=89 && input.LA(1)<=90) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:303:38: ( entity_expression | all_or_any_expression )
        int alt116=2;
        int LA116_0 = input.LA(1);

        if ( ((LA116_0>=WORD && LA116_0<=NAMED_PARAMETER)||(LA116_0>=115 && LA116_0<=116)) ) {
            alt116=1;
        }
        else if ( ((LA116_0>=86 && LA116_0<=88)) ) {
            alt116=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 116, 0, input);

            throw nvae;
        }
        switch (alt116) {
            case 1 :
                // JPA.g:303:39: entity_expression
                {
                pushFollow(FOLLOW_entity_expression_in_synpred115_JPA2548);
                entity_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:303:59: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred115_JPA2552);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred115_JPA

    // Delegated rules

    public final boolean synpred115_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred115_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred110_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred110_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred112_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred112_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred60_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred60_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred64_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred64_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred65_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred65_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred20_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred20_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred104_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred104_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred84_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred84_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred62_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred62_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred63_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred63_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred23_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred23_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred59_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred59_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred107_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred107_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred58_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred58_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred86_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred86_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred57_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred57_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred61_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred61_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA31 dfa31 = new DFA31(this);
    protected DFA36 dfa36 = new DFA36(this);
    protected DFA48 dfa48 = new DFA48(this);
    protected DFA49 dfa49 = new DFA49(this);
    protected DFA59 dfa59 = new DFA59(this);
    protected DFA79 dfa79 = new DFA79(this);
    static final String DFA31_eotS =
        "\11\uffff";
    static final String DFA31_eofS =
        "\4\uffff\3\7\1\uffff\1\7";
    static final String DFA31_minS =
        "\1\75\1\30\1\71\1\uffff\3\25\1\uffff\1\25";
    static final String DFA31_maxS =
        "\1\75\1\167\1\132\1\uffff\3\140\1\uffff\1\140";
    static final String DFA31_acceptS =
        "\3\uffff\1\1\3\uffff\1\2\1\uffff";
    static final String DFA31_specialS =
        "\11\uffff}>";
    static final String[] DFA31_transitionS = {
            "\1\1",
            "\5\3\2\uffff\1\3\12\uffff\1\3\1\uffff\1\3\1\uffff\1\2\1\3"+
            "\10\uffff\1\3\5\uffff\2\3\1\uffff\2\3\6\uffff\4\3\10\uffff\1"+
            "\3\13\uffff\16\3\3\uffff\3\3\1\uffff\2\3",
            "\1\4\4\uffff\1\3\24\uffff\1\3\5\uffff\2\3",
            "",
            "\1\7\12\uffff\1\7\6\uffff\1\7\1\5\5\uffff\1\6\13\uffff\1\3"+
            "\3\uffff\1\3\2\uffff\2\3\12\uffff\2\3\1\uffff\1\3\2\uffff\1"+
            "\3\5\uffff\10\3",
            "\1\7\12\uffff\1\7\6\uffff\3\7\17\uffff\1\10\1\3\3\uffff\1"+
            "\3\2\uffff\2\3\12\uffff\2\3\1\uffff\1\3\2\uffff\1\3\5\uffff"+
            "\10\3",
            "\1\7\12\uffff\1\7\6\uffff\2\7\20\uffff\1\10\1\3\3\uffff\1"+
            "\3\2\uffff\2\3\12\uffff\2\3\1\uffff\1\3\2\uffff\1\3\5\uffff"+
            "\10\3",
            "",
            "\1\7\12\uffff\1\7\6\uffff\1\7\1\5\5\uffff\1\6\13\uffff\1\3"+
            "\3\uffff\1\3\2\uffff\2\3\12\uffff\2\3\1\uffff\1\3\2\uffff\1"+
            "\3\5\uffff\10\3"
    };

    static final short[] DFA31_eot = DFA.unpackEncodedString(DFA31_eotS);
    static final short[] DFA31_eof = DFA.unpackEncodedString(DFA31_eofS);
    static final char[] DFA31_min = DFA.unpackEncodedStringToUnsignedChars(DFA31_minS);
    static final char[] DFA31_max = DFA.unpackEncodedStringToUnsignedChars(DFA31_maxS);
    static final short[] DFA31_accept = DFA.unpackEncodedString(DFA31_acceptS);
    static final short[] DFA31_special = DFA.unpackEncodedString(DFA31_specialS);
    static final short[][] DFA31_transition;

    static {
        int numStates = DFA31_transitionS.length;
        DFA31_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA31_transition[i] = DFA.unpackEncodedString(DFA31_transitionS[i]);
        }
    }

    class DFA31 extends DFA {

        public DFA31(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 31;
            this.eot = DFA31_eot;
            this.eof = DFA31_eof;
            this.min = DFA31_min;
            this.max = DFA31_max;
            this.accept = DFA31_accept;
            this.special = DFA31_special;
            this.transition = DFA31_transition;
        }
        public String getDescription() {
            return "171:1: where_clause : (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) );";
        }
    }
    static final String DFA36_eotS =
        "\7\uffff";
    static final String DFA36_eofS =
        "\2\uffff\2\5\2\uffff\1\5";
    static final String DFA36_minS =
        "\1\56\1\71\2\26\2\uffff\1\26";
    static final String DFA36_maxS =
        "\1\56\1\71\1\66\1\71\2\uffff\1\66";
    static final String DFA36_acceptS =
        "\4\uffff\1\2\1\1\1\uffff";
    static final String DFA36_specialS =
        "\7\uffff}>";
    static final String[] DFA36_transitionS = {
            "\1\1",
            "\1\2",
            "\1\5\1\4\10\uffff\1\5\7\uffff\1\3\5\uffff\1\3\7\uffff\1\5",
            "\1\5\1\4\10\uffff\1\5\25\uffff\1\5\2\uffff\1\6",
            "",
            "",
            "\1\5\1\4\10\uffff\1\5\7\uffff\1\3\5\uffff\1\3\7\uffff\1\5"
    };

    static final short[] DFA36_eot = DFA.unpackEncodedString(DFA36_eotS);
    static final short[] DFA36_eof = DFA.unpackEncodedString(DFA36_eofS);
    static final char[] DFA36_min = DFA.unpackEncodedStringToUnsignedChars(DFA36_minS);
    static final char[] DFA36_max = DFA.unpackEncodedStringToUnsignedChars(DFA36_maxS);
    static final short[] DFA36_accept = DFA.unpackEncodedString(DFA36_acceptS);
    static final short[] DFA36_special = DFA.unpackEncodedString(DFA36_specialS);
    static final short[][] DFA36_transition;

    static {
        int numStates = DFA36_transitionS.length;
        DFA36_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA36_transition[i] = DFA.unpackEncodedString(DFA36_transitionS[i]);
        }
    }

    class DFA36 extends DFA {

        public DFA36(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 36;
            this.eot = DFA36_eot;
            this.eof = DFA36_eof;
            this.min = DFA36_min;
            this.max = DFA36_max;
            this.accept = DFA36_accept;
            this.special = DFA36_special;
            this.transition = DFA36_transition;
        }
        public String getDescription() {
            return "190:1: orderby_item : ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) );";
        }
    }
    static final String DFA48_eotS =
        "\42\uffff";
    static final String DFA48_eofS =
        "\42\uffff";
    static final String DFA48_minS =
        "\1\30\23\uffff\1\0\15\uffff";
    static final String DFA48_maxS =
        "\1\167\23\uffff\1\0\15\uffff";
    static final String DFA48_acceptS =
        "\1\uffff\1\1\37\uffff\1\2";
    static final String DFA48_specialS =
        "\24\uffff\1\0\15\uffff}>";
    static final String[] DFA48_transitionS = {
            "\5\1\2\uffff\1\24\12\uffff\1\1\1\uffff\1\1\1\uffff\2\1\10\uffff"+
            "\1\1\5\uffff\2\1\1\uffff\2\1\6\uffff\4\1\10\uffff\1\1\13\uffff"+
            "\16\1\3\uffff\3\1\1\uffff\2\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA48_eot = DFA.unpackEncodedString(DFA48_eotS);
    static final short[] DFA48_eof = DFA.unpackEncodedString(DFA48_eofS);
    static final char[] DFA48_min = DFA.unpackEncodedStringToUnsignedChars(DFA48_minS);
    static final char[] DFA48_max = DFA.unpackEncodedStringToUnsignedChars(DFA48_maxS);
    static final short[] DFA48_accept = DFA.unpackEncodedString(DFA48_acceptS);
    static final short[] DFA48_special = DFA.unpackEncodedString(DFA48_specialS);
    static final short[][] DFA48_transition;

    static {
        int numStates = DFA48_transitionS.length;
        DFA48_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA48_transition[i] = DFA.unpackEncodedString(DFA48_transitionS[i]);
        }
    }

    class DFA48 extends DFA {

        public DFA48(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 48;
            this.eot = DFA48_eot;
            this.eof = DFA48_eof;
            this.min = DFA48_min;
            this.max = DFA48_max;
            this.accept = DFA48_accept;
            this.special = DFA48_special;
            this.transition = DFA48_transition;
        }
        public String getDescription() {
            return "227:1: conditional_factor : ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA48_20 = input.LA(1);

                         
                        int index48_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred58_JPA()) ) {s = 1;}

                        else if ( (true) ) {s = 33;}

                         
                        input.seek(index48_20);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 48, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA49_eotS =
        "\47\uffff";
    static final String DFA49_eofS =
        "\47\uffff";
    static final String DFA49_minS =
        "\1\30\15\0\1\uffff\13\0\15\uffff";
    static final String DFA49_maxS =
        "\1\167\15\0\1\uffff\13\0\15\uffff";
    static final String DFA49_acceptS =
        "\16\uffff\1\1\13\uffff\1\10\1\uffff\1\11\4\uffff\1\2\1\3\1\4\1"+
        "\5\1\6\1\7";
    static final String DFA49_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1"+
        "\27\15\uffff}>";
    static final String[] DFA49_transitionS = {
            "\4\14\1\13\2\uffff\1\23\12\uffff\1\22\1\uffff\1\2\1\uffff\1"+
            "\1\1\4\10\uffff\1\15\5\uffff\1\32\1\34\1\uffff\2\20\6\uffff"+
            "\4\34\10\uffff\1\32\13\uffff\1\24\1\25\1\26\1\27\1\30\1\31\3"+
            "\17\1\6\1\7\1\10\1\11\1\12\3\uffff\1\21\1\3\1\5\1\uffff\2\16",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA49_eot = DFA.unpackEncodedString(DFA49_eotS);
    static final short[] DFA49_eof = DFA.unpackEncodedString(DFA49_eofS);
    static final char[] DFA49_min = DFA.unpackEncodedStringToUnsignedChars(DFA49_minS);
    static final char[] DFA49_max = DFA.unpackEncodedStringToUnsignedChars(DFA49_maxS);
    static final short[] DFA49_accept = DFA.unpackEncodedString(DFA49_acceptS);
    static final short[] DFA49_special = DFA.unpackEncodedString(DFA49_specialS);
    static final short[][] DFA49_transition;

    static {
        int numStates = DFA49_transitionS.length;
        DFA49_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA49_transition[i] = DFA.unpackEncodedString(DFA49_transitionS[i]);
        }
    }

    class DFA49 extends DFA {

        public DFA49(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 49;
            this.eot = DFA49_eot;
            this.eof = DFA49_eof;
            this.min = DFA49_min;
            this.max = DFA49_max;
            this.accept = DFA49_accept;
            this.special = DFA49_special;
            this.transition = DFA49_transition;
        }
        public String getDescription() {
            return "231:1: simple_cond_expression : ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA49_1 = input.LA(1);

                         
                        int index49_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                        else if ( (synpred62_JPA()) ) {s = 35;}

                        else if ( (synpred63_JPA()) ) {s = 36;}

                        else if ( (synpred64_JPA()) ) {s = 37;}

                        else if ( (synpred65_JPA()) ) {s = 38;}

                         
                        input.seek(index49_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA49_2 = input.LA(1);

                         
                        int index49_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                         
                        input.seek(index49_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA49_3 = input.LA(1);

                         
                        int index49_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                        else if ( (synpred63_JPA()) ) {s = 36;}

                        else if ( (synpred65_JPA()) ) {s = 38;}

                         
                        input.seek(index49_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA49_4 = input.LA(1);

                         
                        int index49_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                        else if ( (synpred63_JPA()) ) {s = 36;}

                        else if ( (synpred65_JPA()) ) {s = 38;}

                         
                        input.seek(index49_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA49_5 = input.LA(1);

                         
                        int index49_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                        else if ( (synpred63_JPA()) ) {s = 36;}

                        else if ( (synpred65_JPA()) ) {s = 38;}

                         
                        input.seek(index49_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA49_6 = input.LA(1);

                         
                        int index49_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                         
                        input.seek(index49_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA49_7 = input.LA(1);

                         
                        int index49_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                         
                        input.seek(index49_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA49_8 = input.LA(1);

                         
                        int index49_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                         
                        input.seek(index49_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA49_9 = input.LA(1);

                         
                        int index49_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                         
                        input.seek(index49_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA49_10 = input.LA(1);

                         
                        int index49_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                         
                        input.seek(index49_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA49_11 = input.LA(1);

                         
                        int index49_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                         
                        input.seek(index49_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA49_12 = input.LA(1);

                         
                        int index49_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                         
                        input.seek(index49_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA49_13 = input.LA(1);

                         
                        int index49_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                        else if ( (synpred61_JPA()) ) {s = 34;}

                         
                        input.seek(index49_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA49_15 = input.LA(1);

                         
                        int index49_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_15);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA49_16 = input.LA(1);

                         
                        int index49_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_16);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA49_17 = input.LA(1);

                         
                        int index49_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_17);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA49_18 = input.LA(1);

                         
                        int index49_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_18);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA49_19 = input.LA(1);

                         
                        int index49_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_19);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA49_20 = input.LA(1);

                         
                        int index49_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_20);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA49_21 = input.LA(1);

                         
                        int index49_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_21);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA49_22 = input.LA(1);

                         
                        int index49_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_22);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA49_23 = input.LA(1);

                         
                        int index49_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_23);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA49_24 = input.LA(1);

                         
                        int index49_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_24);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA49_25 = input.LA(1);

                         
                        int index49_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 14;}

                        else if ( (synpred60_JPA()) ) {s = 33;}

                         
                        input.seek(index49_25);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 49, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA59_eotS =
        "\31\uffff";
    static final String DFA59_eofS =
        "\31\uffff";
    static final String DFA59_minS =
        "\1\30\1\uffff\1\0\3\uffff\3\0\6\uffff\3\0\7\uffff";
    static final String DFA59_maxS =
        "\1\164\1\uffff\1\0\3\uffff\3\0\6\uffff\3\0\7\uffff";
    static final String DFA59_acceptS =
        "\1\uffff\1\1\20\uffff\1\2\5\uffff\1\3";
    static final String DFA59_specialS =
        "\2\uffff\1\0\3\uffff\1\1\1\2\1\3\6\uffff\1\4\1\5\1\6\7\uffff}>";
    static final String[] DFA59_transitionS = {
            "\4\20\1\17\2\uffff\1\1\12\uffff\1\1\1\uffff\1\22\1\uffff\1"+
            "\2\1\7\10\uffff\1\21\10\uffff\2\1\36\uffff\6\1\3\30\5\22\3\uffff"+
            "\1\1\1\6\1\10",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA59_eot = DFA.unpackEncodedString(DFA59_eotS);
    static final short[] DFA59_eof = DFA.unpackEncodedString(DFA59_eofS);
    static final char[] DFA59_min = DFA.unpackEncodedStringToUnsignedChars(DFA59_minS);
    static final char[] DFA59_max = DFA.unpackEncodedStringToUnsignedChars(DFA59_maxS);
    static final short[] DFA59_accept = DFA.unpackEncodedString(DFA59_acceptS);
    static final short[] DFA59_special = DFA.unpackEncodedString(DFA59_specialS);
    static final short[][] DFA59_transition;

    static {
        int numStates = DFA59_transitionS.length;
        DFA59_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA59_transition[i] = DFA.unpackEncodedString(DFA59_transitionS[i]);
        }
    }

    class DFA59 extends DFA {

        public DFA59(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 59;
            this.eot = DFA59_eot;
            this.eof = DFA59_eof;
            this.min = DFA59_min;
            this.max = DFA59_max;
            this.accept = DFA59_accept;
            this.special = DFA59_special;
            this.transition = DFA59_transition;
        }
        public String getDescription() {
            return "264:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA59_2 = input.LA(1);

                         
                        int index59_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA59_6 = input.LA(1);

                         
                        int index59_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_6);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA59_7 = input.LA(1);

                         
                        int index59_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_7);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA59_8 = input.LA(1);

                         
                        int index59_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_8);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA59_15 = input.LA(1);

                         
                        int index59_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_15);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA59_16 = input.LA(1);

                         
                        int index59_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_16);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA59_17 = input.LA(1);

                         
                        int index59_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_17);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 59, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA79_eotS =
        "\34\uffff";
    static final String DFA79_eofS =
        "\34\uffff";
    static final String DFA79_minS =
        "\1\30\1\0\1\uffff\3\0\5\uffff\3\0\16\uffff";
    static final String DFA79_maxS =
        "\1\167\1\0\1\uffff\3\0\5\uffff\3\0\16\uffff";
    static final String DFA79_acceptS =
        "\2\uffff\1\1\13\uffff\1\2\1\4\1\6\11\uffff\1\3\1\5";
    static final String DFA79_specialS =
        "\1\uffff\1\0\1\uffff\1\1\1\2\1\3\5\uffff\1\4\1\5\1\6\16\uffff}>";
    static final String[] DFA79_transitionS = {
            "\4\14\1\13\2\uffff\1\20\12\uffff\1\20\1\uffff\1\2\1\uffff\1"+
            "\1\1\4\10\uffff\1\15\10\uffff\2\20\36\uffff\6\20\3\17\5\2\3"+
            "\uffff\1\20\1\3\1\5\1\uffff\2\16",
            "\1\uffff",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA79_eot = DFA.unpackEncodedString(DFA79_eotS);
    static final short[] DFA79_eof = DFA.unpackEncodedString(DFA79_eofS);
    static final char[] DFA79_min = DFA.unpackEncodedStringToUnsignedChars(DFA79_minS);
    static final char[] DFA79_max = DFA.unpackEncodedStringToUnsignedChars(DFA79_maxS);
    static final short[] DFA79_accept = DFA.unpackEncodedString(DFA79_acceptS);
    static final short[] DFA79_special = DFA.unpackEncodedString(DFA79_specialS);
    static final short[][] DFA79_transition;

    static {
        int numStates = DFA79_transitionS.length;
        DFA79_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA79_transition[i] = DFA.unpackEncodedString(DFA79_transitionS[i]);
        }
    }

    class DFA79 extends DFA {

        public DFA79(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 79;
            this.eot = DFA79_eot;
            this.eof = DFA79_eof;
            this.min = DFA79_min;
            this.max = DFA79_max;
            this.accept = DFA79_accept;
            this.special = DFA79_special;
            this.transition = DFA79_transition;
        }
        public String getDescription() {
            return "298:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA79_1 = input.LA(1);

                         
                        int index79_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred107_JPA()) ) {s = 14;}

                        else if ( (synpred110_JPA()) ) {s = 26;}

                        else if ( (synpred112_JPA()) ) {s = 15;}

                        else if ( (synpred115_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index79_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA79_3 = input.LA(1);

                         
                        int index79_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred107_JPA()) ) {s = 14;}

                        else if ( (synpred110_JPA()) ) {s = 26;}

                        else if ( (synpred112_JPA()) ) {s = 15;}

                        else if ( (synpred115_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index79_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA79_4 = input.LA(1);

                         
                        int index79_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred107_JPA()) ) {s = 14;}

                        else if ( (synpred110_JPA()) ) {s = 26;}

                        else if ( (synpred112_JPA()) ) {s = 15;}

                        else if ( (synpred115_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index79_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA79_5 = input.LA(1);

                         
                        int index79_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred107_JPA()) ) {s = 14;}

                        else if ( (synpred110_JPA()) ) {s = 26;}

                        else if ( (synpred112_JPA()) ) {s = 15;}

                        else if ( (synpred115_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index79_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA79_11 = input.LA(1);

                         
                        int index79_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred112_JPA()) ) {s = 15;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index79_11);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA79_12 = input.LA(1);

                         
                        int index79_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred112_JPA()) ) {s = 15;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index79_12);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA79_13 = input.LA(1);

                         
                        int index79_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred107_JPA()) ) {s = 14;}

                        else if ( (synpred110_JPA()) ) {s = 26;}

                        else if ( (synpred112_JPA()) ) {s = 15;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index79_13);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 79, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_select_statement_in_ql_statement426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_select_statement441 = new BitSet(new long[]{0x180040021F000000L});
    public static final BitSet FOLLOW_select_clause_in_select_statement443 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_from_clause_in_select_statement445 = new BitSet(new long[]{0x2000018000200002L});
    public static final BitSet FOLLOW_where_clause_in_select_statement448 = new BitSet(new long[]{0x0000018000200002L});
    public static final BitSet FOLLOW_groupby_clause_in_select_statement453 = new BitSet(new long[]{0x0000008000200002L});
    public static final BitSet FOLLOW_having_clause_in_select_statement458 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_orderby_clause_in_select_statement462 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_from_clause523 = new BitSet(new long[]{0x0100400000000000L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause525 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_from_clause528 = new BitSet(new long[]{0x0500400000000000L});
    public static final BitSet FOLLOW_identification_variable_or_collection_declaration_in_from_clause530 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_or_collection_declaration563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_or_collection_declaration571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration594 = new BitSet(new long[]{0x0000003400000002L});
    public static final BitSet FOLLOW_joined_clause_in_identification_variable_declaration596 = new BitSet(new long[]{0x0000003400000002L});
    public static final BitSet FOLLOW_join_in_joined_clause627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fetch_join_in_joined_clause631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_variable_declaration_source_in_range_variable_declaration643 = new BitSet(new long[]{0x0080400000000000L});
    public static final BitSet FOLLOW_55_in_range_variable_declaration646 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_abstract_schema_name_in_range_variable_declaration_source686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_56_in_range_variable_declaration_source696 = new BitSet(new long[]{0x180040021F000000L});
    public static final BitSet FOLLOW_select_clause_in_range_variable_declaration_source698 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_from_clause_in_range_variable_declaration_source700 = new BitSet(new long[]{0x2000018100200000L});
    public static final BitSet FOLLOW_where_clause_in_range_variable_declaration_source703 = new BitSet(new long[]{0x0000018100200000L});
    public static final BitSet FOLLOW_groupby_clause_in_range_variable_declaration_source708 = new BitSet(new long[]{0x0000008100200000L});
    public static final BitSet FOLLOW_having_clause_in_range_variable_declaration_source713 = new BitSet(new long[]{0x0000008100000000L});
    public static final BitSet FOLLOW_orderby_clause_in_range_variable_declaration_source717 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_range_variable_declaration_source723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_spec_in_join786 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_join_association_path_expression_in_join788 = new BitSet(new long[]{0x0080400000000000L});
    public static final BitSet FOLLOW_55_in_join791 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_identification_variable_in_join795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_spec_in_fetch_join833 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_FETCH_in_fetch_join835 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_in_join_spec850 = new BitSet(new long[]{0x0000002800000000L});
    public static final BitSet FOLLOW_OUTER_in_join_spec854 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_INNER_in_join_spec860 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_JOIN_in_join_spec865 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression877 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_join_association_path_expression879 = new BitSet(new long[]{0x0000410000000002L});
    public static final BitSet FOLLOW_field_in_join_association_path_expression882 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_join_association_path_expression883 = new BitSet(new long[]{0x0000410000000002L});
    public static final BitSet FOLLOW_field_in_join_association_path_expression887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_collection_member_declaration924 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration925 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_path_expression_in_collection_member_declaration927 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration929 = new BitSet(new long[]{0x0080400000000000L});
    public static final BitSet FOLLOW_55_in_collection_member_declaration932 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_path_expression971 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_path_expression973 = new BitSet(new long[]{0x0000410000000002L});
    public static final BitSet FOLLOW_field_in_path_expression976 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_path_expression977 = new BitSet(new long[]{0x0000410000000002L});
    public static final BitSet FOLLOW_field_in_path_expression982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_select_clause1021 = new BitSet(new long[]{0x180040021F000000L});
    public static final BitSet FOLLOW_select_expression_in_select_clause1025 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_select_clause1028 = new BitSet(new long[]{0x180040021F000000L});
    public static final BitSet FOLLOW_select_expression_in_select_clause1030 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_path_expression_in_select_expression1075 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_select_expression1083 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_select_expression1091 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_select_expression1109 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_select_expression1111 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_identification_variable_in_select_expression1112 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_select_expression1113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_expression_in_select_expression1121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_constructor_expression1133 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_constructor_name_in_constructor_expression1135 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_constructor_expression1137 = new BitSet(new long[]{0x000040001F000000L});
    public static final BitSet FOLLOW_constructor_item_in_constructor_expression1139 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_54_in_constructor_expression1142 = new BitSet(new long[]{0x000040001F000000L});
    public static final BitSet FOLLOW_constructor_item_in_constructor_expression1144 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_constructor_expression1148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_constructor_item1160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_constructor_item1164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1176 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1178 = new BitSet(new long[]{0x0000400200000000L});
    public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1181 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_path_expression_in_aggregate_expression1185 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COUNT_in_aggregate_expression1220 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1222 = new BitSet(new long[]{0x0000400200000000L});
    public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1225 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_identification_variable_in_aggregate_expression1229 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_aggregate_expression_function_name0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_where_clause1299 = new BitSet(new long[]{0xC100D4009F000000L,0x00DC7FFE00201E06L});
    public static final BitSet FOLLOW_conditional_expression_in_where_clause1301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_where_clause1320 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_path_expression_in_where_clause1322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_groupby_clause1346 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_BY_in_groupby_clause1348 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_groupby_item_in_groupby_clause1350 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_groupby_clause1353 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_groupby_item_in_groupby_clause1355 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_path_expression_in_groupby_item1395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_groupby_item1399 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HAVING_in_having_clause1411 = new BitSet(new long[]{0xC100D4009F000000L,0x00DC7FFE00201E06L});
    public static final BitSet FOLLOW_conditional_expression_in_having_clause1413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ORDER_in_orderby_clause1425 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_BY_in_orderby_clause1427 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_orderby_item_in_orderby_clause1429 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_orderby_clause1432 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_orderby_item_in_orderby_clause1434 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_path_expression_in_orderby_item1469 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_ASC_in_orderby_item1474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_orderby_item1506 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_DESC_in_orderby_item1510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_56_in_subquery1542 = new BitSet(new long[]{0x000040021F000000L});
    public static final BitSet FOLLOW_simple_select_clause_in_subquery1544 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_subquery_from_clause_in_subquery1546 = new BitSet(new long[]{0x2000010100200000L});
    public static final BitSet FOLLOW_where_clause_in_subquery1549 = new BitSet(new long[]{0x0000010100200000L});
    public static final BitSet FOLLOW_groupby_clause_in_subquery1554 = new BitSet(new long[]{0x0000000100200000L});
    public static final BitSet FOLLOW_having_clause_in_subquery1559 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_subquery1565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_subquery_from_clause1614 = new BitSet(new long[]{0x0500400000000000L});
    public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1616 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_subquery_from_clause1619 = new BitSet(new long[]{0x0500400000000000L});
    public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1621 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_association_path_expression_in_subselect_identification_variable_declaration1667 = new BitSet(new long[]{0x0080400000000000L});
    public static final BitSet FOLLOW_55_in_subselect_identification_variable_declaration1670 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration1674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_declaration_in_subselect_identification_variable_declaration1682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_association_path_expression1694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause1707 = new BitSet(new long[]{0x000040021F000000L});
    public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause1711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_simple_select_expression1748 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression1756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_simple_select_expression1764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditional_term_in_conditional_expression1777 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_OR_in_conditional_expression1781 = new BitSet(new long[]{0xC100D4009F000000L,0x00DC7FFE00201E06L});
    public static final BitSet FOLLOW_conditional_term_in_conditional_expression1783 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_conditional_factor_in_conditional_term1798 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_AND_in_conditional_term1802 = new BitSet(new long[]{0xC100D4009F000000L,0x00DC7FFE00201E06L});
    public static final BitSet FOLLOW_conditional_factor_in_conditional_term1804 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_62_in_conditional_factor1823 = new BitSet(new long[]{0xC100D4009F000000L,0x00DC7FFE00201E06L});
    public static final BitSet FOLLOW_simple_cond_expression_in_conditional_factor1827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_conditional_factor1852 = new BitSet(new long[]{0xC100D4009F000000L,0x00DC7FFE00201E06L});
    public static final BitSet FOLLOW_conditional_expression_in_conditional_factor1853 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_conditional_factor1854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression1866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_expression_in_simple_cond_expression1874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_expression_in_simple_cond_expression1882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_expression_in_simple_cond_expression1890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression1898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression1906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression1914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression1922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression1930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression1942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression1950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression1958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression1966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression1974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_date_between_macro_expression1986 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression1988 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression1990 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_between_macro_expression1992 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_date_between_macro_expression1994 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression1997 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_date_between_macro_expression2005 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_between_macro_expression2009 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_date_between_macro_expression2011 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression2014 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_date_between_macro_expression2022 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_between_macro_expression2026 = new BitSet(new long[]{0x0000000000000000L,0x00000000000001F8L});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression2028 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_date_before_macro_expression2063 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2065 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2067 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_before_macro_expression2069 = new BitSet(new long[]{0x0000C00000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2072 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2076 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_date_after_macro_expression2091 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2093 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2095 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_after_macro_expression2097 = new BitSet(new long[]{0x0000C00000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2100 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2104 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_75_in_date_equals_macro_expression2119 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2121 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2123 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_equals_macro_expression2125 = new BitSet(new long[]{0x0000C00000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2128 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2132 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_date_today_macro_expression2147 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2149 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2151 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2165 = new BitSet(new long[]{0x4000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_62_in_between_expression2168 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_between_expression2172 = new BitSet(new long[]{0x0100D4009F000000L,0x00DC7FFE00000006L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2174 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression2176 = new BitSet(new long[]{0x0100D4009F000000L,0x00DC7FFE00000006L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2178 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_between_expression2186 = new BitSet(new long[]{0x4000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_62_in_between_expression2189 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_between_expression2193 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_expression_in_between_expression2195 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression2197 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_expression_in_between_expression2199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression2207 = new BitSet(new long[]{0x4000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_62_in_between_expression2210 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_between_expression2214 = new BitSet(new long[]{0x0100D0001F000000L,0x00187F8000000000L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression2216 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression2218 = new BitSet(new long[]{0x0100D0001F000000L,0x00187F8000000000L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression2220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_in_expression2232 = new BitSet(new long[]{0x4400000000000000L});
    public static final BitSet FOLLOW_62_in_in_expression2235 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_58_in_in_expression2239 = new BitSet(new long[]{0x0100D0009F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_in_expression_right_part_in_in_expression2241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_in_expression_right_part2253 = new BitSet(new long[]{0x0000C00000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_in_item_in_in_expression_right_part2255 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_54_in_in_expression_right_part2258 = new BitSet(new long[]{0x0000C00000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_in_item_in_in_expression_right_part2260 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_in_expression_right_part2264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_in_expression_right_part2272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_in_item2284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_in_item2292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_like_expression2304 = new BitSet(new long[]{0x4000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_62_in_like_expression2307 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_78_in_like_expression2311 = new BitSet(new long[]{0x0000C00000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_pattern_value_in_like_expression2314 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_input_parameter_in_like_expression2318 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_79_in_like_expression2321 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_ESCAPE_CHARACTER_in_like_expression2323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_null_comparison_expression2338 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression2342 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_80_in_null_comparison_expression2345 = new BitSet(new long[]{0x4000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_62_in_null_comparison_expression2348 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_81_in_null_comparison_expression2352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression2364 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_80_in_empty_collection_comparison_expression2366 = new BitSet(new long[]{0x4000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_62_in_empty_collection_comparison_expression2369 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_empty_collection_comparison_expression2373 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_collection_member_expression2385 = new BitSet(new long[]{0x4000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_62_in_collection_member_expression2388 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_collection_member_expression2392 = new BitSet(new long[]{0x0000400000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_84_in_collection_member_expression2395 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_path_expression_in_collection_member_expression2399 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_exists_expression2412 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_85_in_exists_expression2416 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_subquery_in_exists_expression2418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_all_or_any_expression2430 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_subquery_in_all_or_any_expression2443 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_comparison_expression2455 = new BitSet(new long[]{0x0000000000000000L,0x000000007E000000L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2457 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0001C00000L});
    public static final BitSet FOLLOW_string_expression_in_comparison_expression2460 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2473 = new BitSet(new long[]{0x0000000000000000L,0x0000000006000000L});
    public static final BitSet FOLLOW_set_in_comparison_expression2475 = new BitSet(new long[]{0x0100D0001F000000L,0x00D87C0001C00000L});
    public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_expression_in_comparison_expression2497 = new BitSet(new long[]{0x0000000000000000L,0x0000000006000000L});
    public static final BitSet FOLLOW_set_in_comparison_expression2499 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0001C00000L});
    public static final BitSet FOLLOW_enum_expression_in_comparison_expression2506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2519 = new BitSet(new long[]{0x0000000000000000L,0x000000007E000000L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2521 = new BitSet(new long[]{0x0100D0001F000000L,0x00187F8001C00000L});
    public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2524 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_comparison_expression2537 = new BitSet(new long[]{0x0000000000000000L,0x0000000006000000L});
    public static final BitSet FOLLOW_set_in_comparison_expression2539 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0001C00000L});
    public static final BitSet FOLLOW_entity_expression_in_comparison_expression2548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression2561 = new BitSet(new long[]{0x0000000000000000L,0x000000007E000000L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2563 = new BitSet(new long[]{0x0100D4009F000000L,0x00DC7FFE01C00006L});
    public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression2566 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_comparison_operator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_arithmetic_expression2635 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_arithmetic_expression2643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_term_in_simple_arithmetic_expression2656 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000006L});
    public static final BitSet FOLLOW_set_in_simple_arithmetic_expression2660 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_arithmetic_term_in_simple_arithmetic_expression2670 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000006L});
    public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term2685 = new BitSet(new long[]{0x0000000000000002L,0x0000000180000000L});
    public static final BitSet FOLLOW_set_in_arithmetic_term2689 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term2699 = new BitSet(new long[]{0x0000000000000002L,0x0000000180000000L});
    public static final BitSet FOLLOW_set_in_arithmetic_factor2713 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor2724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_arithmetic_primary2736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary2744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary2752 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_arithmetic_primary2753 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary2754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary2762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary2770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary2778 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_primary_in_string_expression2790 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_string_expression2794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_string_primary2806 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_string_primary2814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_string_primary2822 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_strings_in_string_primary2830 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_string_primary2838 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_primary_in_datetime_expression2850 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_datetime_expression2858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_datetime_primary2870 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_datetime_primary2878 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_primary2886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_datetime_primary2894 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_primary_in_boolean_expression2906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_boolean_expression2914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_boolean_primary2926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_literal_in_boolean_primary2934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_boolean_primary2942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_primary_in_enum_expression2954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_enum_expression2962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_enum_primary2974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_literal_in_enum_primary2982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_enum_primary2990 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_entity_expression3002 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3010 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3022 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_functions_returning_numerics3042 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics3044 = new BitSet(new long[]{0x0000D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics3045 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3046 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_functions_returning_numerics3054 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics3056 = new BitSet(new long[]{0x0000D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics3057 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_numerics3058 = new BitSet(new long[]{0x0000D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics3060 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_numerics3062 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics3064 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_functions_returning_numerics3075 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics3077 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics3078 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_functions_returning_numerics3087 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics3089 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics3090 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3091 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_101_in_functions_returning_numerics3099 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics3101 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics3102 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_numerics3103 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics3105 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_102_in_functions_returning_numerics3114 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics3116 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3117 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_functions_returning_datetime0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_106_in_functions_returning_strings3158 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3160 = new BitSet(new long[]{0x0000D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings3161 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_strings3162 = new BitSet(new long[]{0x0000D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings3164 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_107_in_functions_returning_strings3173 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3175 = new BitSet(new long[]{0x0000D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings3176 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_strings3177 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_strings3178 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_strings3179 = new BitSet(new long[]{0x0000C4009F000000L,0x001C007E00000006L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_strings3181 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_functions_returning_strings3190 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3192 = new BitSet(new long[]{0x0020F0001F000000L,0x001BFC0000000000L});
    public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings3195 = new BitSet(new long[]{0x0020200000000000L});
    public static final BitSet FOLLOW_TRIM_CHARACTER_in_functions_returning_strings3200 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_functions_returning_strings3204 = new BitSet(new long[]{0x0000D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings3208 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3209 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_109_in_functions_returning_strings3217 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3219 = new BitSet(new long[]{0x0000D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings3220 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_110_in_functions_returning_strings3229 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3231 = new BitSet(new long[]{0x0000D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings3232 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_trim_specification0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_abstract_schema_name3274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_pattern_value3287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_114_in_numeric_literal3301 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal3305 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_115_in_input_parameter3318 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_input_parameter3320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter3343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_116_in_input_parameter3364 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_WORD_in_input_parameter3366 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_117_in_input_parameter3368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_literal3396 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_constructor_name3408 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_enum_literal3452 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_boolean_literal0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_field0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_identification_variable3501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_parameter_name3513 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_57_in_parameter_name3516 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_WORD_in_parameter_name3519 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_field_in_synpred20_JPA887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_field_in_synpred23_JPA982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_synpred57_JPA1823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_synpred58_JPA1823 = new BitSet(new long[]{0xC100D4009F000000L,0x00DC7FFE00201E06L});
    public static final BitSet FOLLOW_simple_cond_expression_in_synpred58_JPA1827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_expression_in_synpred59_JPA1866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_expression_in_synpred60_JPA1874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_expression_in_synpred61_JPA1882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_expression_in_synpred62_JPA1890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_comparison_expression_in_synpred63_JPA1898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred64_JPA1906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_expression_in_synpred65_JPA1914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred84_JPA2165 = new BitSet(new long[]{0x4000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_62_in_synpred84_JPA2168 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_synpred84_JPA2172 = new BitSet(new long[]{0x0100D4009F000000L,0x00DC7FFE00000006L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred84_JPA2174 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_synpred84_JPA2176 = new BitSet(new long[]{0x0100D4009F000000L,0x00DC7FFE00000006L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred84_JPA2178 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_synpred86_JPA2186 = new BitSet(new long[]{0x4000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_62_in_synpred86_JPA2189 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_synpred86_JPA2193 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_expression_in_synpred86_JPA2195 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_synpred86_JPA2197 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_expression_in_synpred86_JPA2199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_synpred104_JPA2455 = new BitSet(new long[]{0x0000000000000000L,0x000000007E000000L});
    public static final BitSet FOLLOW_comparison_operator_in_synpred104_JPA2457 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0001C00000L});
    public static final BitSet FOLLOW_string_expression_in_synpred104_JPA2460 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred104_JPA2464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_expression_in_synpred107_JPA2473 = new BitSet(new long[]{0x0000000000000000L,0x0000000006000000L});
    public static final BitSet FOLLOW_set_in_synpred107_JPA2475 = new BitSet(new long[]{0x0100D0001F000000L,0x00D87C0001C00000L});
    public static final BitSet FOLLOW_boolean_expression_in_synpred107_JPA2484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred107_JPA2488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_expression_in_synpred110_JPA2497 = new BitSet(new long[]{0x0000000000000000L,0x0000000006000000L});
    public static final BitSet FOLLOW_set_in_synpred110_JPA2499 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0001C00000L});
    public static final BitSet FOLLOW_enum_expression_in_synpred110_JPA2506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred110_JPA2510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_synpred112_JPA2519 = new BitSet(new long[]{0x0000000000000000L,0x000000007E000000L});
    public static final BitSet FOLLOW_comparison_operator_in_synpred112_JPA2521 = new BitSet(new long[]{0x0100D0001F000000L,0x00187F8001C00000L});
    public static final BitSet FOLLOW_datetime_expression_in_synpred112_JPA2524 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred112_JPA2528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_synpred115_JPA2537 = new BitSet(new long[]{0x0000000000000000L,0x0000000006000000L});
    public static final BitSet FOLLOW_set_in_synpred115_JPA2539 = new BitSet(new long[]{0x0100D0001F000000L,0x00187C0001C00000L});
    public static final BitSet FOLLOW_entity_expression_in_synpred115_JPA2548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred115_JPA2552 = new BitSet(new long[]{0x0000000000000002L});

}