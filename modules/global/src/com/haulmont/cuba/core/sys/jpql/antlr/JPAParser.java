// $ANTLR 3.2 Sep 23, 2009 12:02:23 JPA.g 2011-04-13 18:38:35

package com.haulmont.cuba.core.sys.jpql.antlr;

import com.haulmont.cuba.core.sys.jpql.tree.QueryNode;
import com.haulmont.cuba.core.sys.jpql.tree.SelectedItemNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import com.haulmont.cuba.core.sys.jpql.tree.FromNode;
import com.haulmont.cuba.core.sys.jpql.tree.SelectionSourceNode;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.JoinVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.CollectionMemberNode;
import com.haulmont.cuba.core.sys.jpql.tree.WhereNode;
import com.haulmont.cuba.core.sys.jpql.tree.SimpleConditionNode;
import com.haulmont.cuba.core.sys.jpql.tree.ParameterNode;
import com.haulmont.cuba.core.sys.jpql.tree.GroupByNode;
import com.haulmont.cuba.core.sys.jpql.tree.OrderByNode;
import com.haulmont.cuba.core.sys.jpql.tree.OrderByFieldNode;
import com.haulmont.cuba.core.sys.jpql.tree.AggregateExpressionNode;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class JPAParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "T_SELECTED_ITEMS", "T_SELECTED_ITEM", "T_SOURCES", "T_SOURCE", "T_SELECTED_FIELD", "T_SELECTED_ENTITY", "T_ID_VAR", "T_JOIN_VAR", "T_COLLECTION_MEMBER", "T_QUERY", "T_CONDITION", "T_SIMPLE_CONDITION", "T_PARAMETER", "T_GROUP_BY", "T_ORDER_BY", "T_ORDER_BY_FIELD", "T_AGGREGATE_EXPR", "HAVING", "ASC", "DESC", "AVG", "MAX", "MIN", "SUM", "COUNT", "OR", "AND", "LPAREN", "RPAREN", "DISTINCT", "LEFT", "OUTER", "INNER", "JOIN", "FETCH", "INT_NUMERAL", "ESCAPE_CHARACTER", "STRINGLITERAL", "TRIM_CHARACTER", "WORD", "NAMED_PARAMETER", "WS", "COMMENT", "LINE_COMMENT", "'SELECT'", "'FROM'", "','", "'AS'", "'(SELECT'", "'.'", "'IN'", "'OBJECT'", "'NEW'", "'WHERE'", "'GROUP'", "'BY'", "'ORDER'", "'NOT'", "'@BETWEEN'", "'NOW'", "'+'", "'-'", "'YEAR'", "'MONTH'", "'DAY'", "'HOUR'", "'MINUTE'", "'SECOND'", "'@DATEBEFORE'", "'@DATEAFTER'", "'@DATEEQUALS'", "'@TODAY'", "'BETWEEN'", "'LIKE'", "'ESCAPE'", "'IS'", "'NULL'", "'EMPTY'", "'MEMBER'", "'OF'", "'EXISTS'", "'ALL'", "'ANY'", "'SOME'", "'='", "'<>'", "'>'", "'>='", "'<'", "'<='", "'*'", "'/'", "'LENGTH'", "'LOCATE'", "'ABS'", "'SQRT'", "'MOD'", "'SIZE'", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'CONCAT'", "'SUBSTRING'", "'TRIM'", "'LOWER'", "'UPPER'", "'LEADING'", "'TRAILING'", "'BOTH'", "'0x'", "'?'", "'true'", "'false'"
    };
    public static final int EOF=-1;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__50=50;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__59=59;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__73=73;
    public static final int T__74=74;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int T__77=77;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__84=84;
    public static final int T__85=85;
    public static final int T__86=86;
    public static final int T__87=87;
    public static final int T__88=88;
    public static final int T__89=89;
    public static final int T__90=90;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int T__95=95;
    public static final int T__96=96;
    public static final int T__97=97;
    public static final int T__98=98;
    public static final int T__99=99;
    public static final int T__100=100;
    public static final int T__101=101;
    public static final int T__102=102;
    public static final int T__103=103;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int T__110=110;
    public static final int T__111=111;
    public static final int T__112=112;
    public static final int T__113=113;
    public static final int T__114=114;
    public static final int T__115=115;
    public static final int T__116=116;
    public static final int T_SELECTED_ITEMS=4;
    public static final int T_SELECTED_ITEM=5;
    public static final int T_SOURCES=6;
    public static final int T_SOURCE=7;
    public static final int T_SELECTED_FIELD=8;
    public static final int T_SELECTED_ENTITY=9;
    public static final int T_ID_VAR=10;
    public static final int T_JOIN_VAR=11;
    public static final int T_COLLECTION_MEMBER=12;
    public static final int T_QUERY=13;
    public static final int T_CONDITION=14;
    public static final int T_SIMPLE_CONDITION=15;
    public static final int T_PARAMETER=16;
    public static final int T_GROUP_BY=17;
    public static final int T_ORDER_BY=18;
    public static final int T_ORDER_BY_FIELD=19;
    public static final int T_AGGREGATE_EXPR=20;
    public static final int HAVING=21;
    public static final int ASC=22;
    public static final int DESC=23;
    public static final int AVG=24;
    public static final int MAX=25;
    public static final int MIN=26;
    public static final int SUM=27;
    public static final int COUNT=28;
    public static final int OR=29;
    public static final int AND=30;
    public static final int LPAREN=31;
    public static final int RPAREN=32;
    public static final int DISTINCT=33;
    public static final int LEFT=34;
    public static final int OUTER=35;
    public static final int INNER=36;
    public static final int JOIN=37;
    public static final int FETCH=38;
    public static final int INT_NUMERAL=39;
    public static final int ESCAPE_CHARACTER=40;
    public static final int STRINGLITERAL=41;
    public static final int TRIM_CHARACTER=42;
    public static final int WORD=43;
    public static final int NAMED_PARAMETER=44;
    public static final int WS=45;
    public static final int COMMENT=46;
    public static final int LINE_COMMENT=47;

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
    // JPA.g:81:1: ql_statement : select_statement ;
    public final JPAParser.ql_statement_return ql_statement() throws RecognitionException {
        JPAParser.ql_statement_return retval = new JPAParser.ql_statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.select_statement_return select_statement1 = null;



        try {
            // JPA.g:82:2: ( select_statement )
            // JPA.g:82:4: select_statement
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_select_statement_in_ql_statement383);
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
    // JPA.g:84:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
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
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");
        try {
            // JPA.g:85:2: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
            // JPA.g:85:5: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )?
            {
            sl=(Token)match(input,48,FOLLOW_48_in_select_statement395); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_48.add(sl);

            pushFollow(FOLLOW_select_clause_in_select_statement397);
            select_clause2=select_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_select_clause.add(select_clause2.getTree());
            pushFollow(FOLLOW_from_clause_in_select_statement399);
            from_clause3=from_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_from_clause.add(from_clause3.getTree());
            // JPA.g:85:43: ( where_clause )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==57) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // JPA.g:85:44: where_clause
                    {
                    pushFollow(FOLLOW_where_clause_in_select_statement402);
                    where_clause4=where_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_where_clause.add(where_clause4.getTree());

                    }
                    break;

            }

            // JPA.g:85:59: ( groupby_clause )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==58) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // JPA.g:85:60: groupby_clause
                    {
                    pushFollow(FOLLOW_groupby_clause_in_select_statement407);
                    groupby_clause5=groupby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause5.getTree());

                    }
                    break;

            }

            // JPA.g:85:77: ( having_clause )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==HAVING) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // JPA.g:85:78: having_clause
                    {
                    pushFollow(FOLLOW_having_clause_in_select_statement412);
                    having_clause6=having_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_having_clause.add(having_clause6.getTree());

                    }
                    break;

            }

            // JPA.g:85:93: ( orderby_clause )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==60) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // JPA.g:85:94: orderby_clause
                    {
                    pushFollow(FOLLOW_orderby_clause_in_select_statement416);
                    orderby_clause7=orderby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause7.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: where_clause, select_clause, groupby_clause, from_clause, having_clause, orderby_clause
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 86:3: -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
            {
                // JPA.g:86:6: ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, sl), root_1);

                // JPA.g:86:32: ( select_clause )?
                if ( stream_select_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_select_clause.nextTree());

                }
                stream_select_clause.reset();
                adaptor.addChild(root_1, stream_from_clause.nextTree());
                // JPA.g:86:61: ( where_clause )?
                if ( stream_where_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_where_clause.nextTree());

                }
                stream_where_clause.reset();
                // JPA.g:86:77: ( groupby_clause )?
                if ( stream_groupby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                }
                stream_groupby_clause.reset();
                // JPA.g:86:95: ( having_clause )?
                if ( stream_having_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_having_clause.nextTree());

                }
                stream_having_clause.reset();
                // JPA.g:86:112: ( orderby_clause )?
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
    // JPA.g:88:1: from_clause : fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )* -> ^( T_SOURCES[$fr] ^( T_SOURCE identification_variable_declaration ) ( ^( T_SOURCE identification_variable_or_collection_declaration ) )* ) ;
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
        RewriteRuleTokenStream stream_49=new RewriteRuleTokenStream(adaptor,"token 49");
        RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");
        RewriteRuleSubtreeStream stream_identification_variable_or_collection_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_or_collection_declaration");
        RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
        try {
            // JPA.g:89:2: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )* -> ^( T_SOURCES[$fr] ^( T_SOURCE identification_variable_declaration ) ( ^( T_SOURCE identification_variable_or_collection_declaration ) )* ) )
            // JPA.g:89:4: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )*
            {
            fr=(Token)match(input,49,FOLLOW_49_in_from_clause471); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_49.add(fr);

            pushFollow(FOLLOW_identification_variable_declaration_in_from_clause473);
            identification_variable_declaration8=identification_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration8.getTree());
            // JPA.g:89:50: ( ',' identification_variable_or_collection_declaration )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==50) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // JPA.g:89:51: ',' identification_variable_or_collection_declaration
            	    {
            	    char_literal9=(Token)match(input,50,FOLLOW_50_in_from_clause476); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_50.add(char_literal9);

            	    pushFollow(FOLLOW_identification_variable_or_collection_declaration_in_from_clause478);
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
            // 90:2: -> ^( T_SOURCES[$fr] ^( T_SOURCE identification_variable_declaration ) ( ^( T_SOURCE identification_variable_or_collection_declaration ) )* )
            {
                // JPA.g:90:5: ^( T_SOURCES[$fr] ^( T_SOURCE identification_variable_declaration ) ( ^( T_SOURCE identification_variable_or_collection_declaration ) )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);

                // JPA.g:90:32: ^( T_SOURCE identification_variable_declaration )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_2);

                adaptor.addChild(root_2, stream_identification_variable_declaration.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                // JPA.g:90:101: ( ^( T_SOURCE identification_variable_or_collection_declaration ) )*
                while ( stream_identification_variable_or_collection_declaration.hasNext() ) {
                    // JPA.g:90:101: ^( T_SOURCE identification_variable_or_collection_declaration )
                    {
                    Object root_2 = (Object)adaptor.nil();
                    root_2 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_2);

                    adaptor.addChild(root_2, stream_identification_variable_or_collection_declaration.nextTree());

                    adaptor.addChild(root_1, root_2);
                    }

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
    // JPA.g:92:1: identification_variable_or_collection_declaration : ( identification_variable_declaration | collection_member_declaration );
    public final JPAParser.identification_variable_or_collection_declaration_return identification_variable_or_collection_declaration() throws RecognitionException {
        JPAParser.identification_variable_or_collection_declaration_return retval = new JPAParser.identification_variable_or_collection_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.identification_variable_declaration_return identification_variable_declaration11 = null;

        JPAParser.collection_member_declaration_return collection_member_declaration12 = null;



        try {
            // JPA.g:93:5: ( identification_variable_declaration | collection_member_declaration )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WORD||LA6_0==52) ) {
                alt6=1;
            }
            else if ( (LA6_0==54) ) {
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
                    // JPA.g:93:7: identification_variable_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_or_collection_declaration522);
                    identification_variable_declaration11=identification_variable_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration11.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:93:45: collection_member_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_or_collection_declaration526);
                    collection_member_declaration12=collection_member_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_declaration12.getTree());

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
    // JPA.g:95:1: identification_variable_declaration : range_variable_declaration ( join | fetch_join )* ;
    public final JPAParser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
        JPAParser.identification_variable_declaration_return retval = new JPAParser.identification_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.range_variable_declaration_return range_variable_declaration13 = null;

        JPAParser.join_return join14 = null;

        JPAParser.fetch_join_return fetch_join15 = null;



        try {
            // JPA.g:96:2: ( range_variable_declaration ( join | fetch_join )* )
            // JPA.g:96:4: range_variable_declaration ( join | fetch_join )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration535);
            range_variable_declaration13=range_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, range_variable_declaration13.getTree());
            // JPA.g:96:31: ( join | fetch_join )*
            loop7:
            do {
                int alt7=3;
                switch ( input.LA(1) ) {
                case LEFT:
                    {
                    int LA7_2 = input.LA(2);

                    if ( (LA7_2==OUTER) ) {
                        int LA7_5 = input.LA(3);

                        if ( (LA7_5==JOIN) ) {
                            int LA7_4 = input.LA(4);

                            if ( (LA7_4==WORD) ) {
                                alt7=1;
                            }
                            else if ( (LA7_4==FETCH) ) {
                                alt7=2;
                            }


                        }


                    }
                    else if ( (LA7_2==JOIN) ) {
                        int LA7_4 = input.LA(3);

                        if ( (LA7_4==WORD) ) {
                            alt7=1;
                        }
                        else if ( (LA7_4==FETCH) ) {
                            alt7=2;
                        }


                    }


                    }
                    break;
                case INNER:
                    {
                    int LA7_3 = input.LA(2);

                    if ( (LA7_3==JOIN) ) {
                        int LA7_4 = input.LA(3);

                        if ( (LA7_4==WORD) ) {
                            alt7=1;
                        }
                        else if ( (LA7_4==FETCH) ) {
                            alt7=2;
                        }


                    }


                    }
                    break;
                case JOIN:
                    {
                    int LA7_4 = input.LA(2);

                    if ( (LA7_4==WORD) ) {
                        alt7=1;
                    }
                    else if ( (LA7_4==FETCH) ) {
                        alt7=2;
                    }


                    }
                    break;

                }

                switch (alt7) {
            	case 1 :
            	    // JPA.g:96:33: join
            	    {
            	    pushFollow(FOLLOW_join_in_identification_variable_declaration539);
            	    join14=join();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, join14.getTree());

            	    }
            	    break;
            	case 2 :
            	    // JPA.g:96:40: fetch_join
            	    {
            	    pushFollow(FOLLOW_fetch_join_in_identification_variable_declaration543);
            	    fetch_join15=fetch_join();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, fetch_join15.getTree());

            	    }
            	    break;

            	default :
            	    break loop7;
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
    // $ANTLR end "identification_variable_declaration"

    public static class range_variable_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "range_variable_declaration"
    // JPA.g:98:1: range_variable_declaration : range_variable_declaration_source ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source ) ;
    public final JPAParser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
        JPAParser.range_variable_declaration_return retval = new JPAParser.range_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal17=null;
        JPAParser.range_variable_declaration_source_return range_variable_declaration_source16 = null;

        JPAParser.identification_variable_return identification_variable18 = null;


        Object string_literal17_tree=null;
        RewriteRuleTokenStream stream_51=new RewriteRuleTokenStream(adaptor,"token 51");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_range_variable_declaration_source=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration_source");
        try {
            // JPA.g:99:2: ( range_variable_declaration_source ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source ) )
            // JPA.g:99:4: range_variable_declaration_source ( 'AS' )? identification_variable
            {
            pushFollow(FOLLOW_range_variable_declaration_source_in_range_variable_declaration555);
            range_variable_declaration_source16=range_variable_declaration_source();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_range_variable_declaration_source.add(range_variable_declaration_source16.getTree());
            // JPA.g:99:38: ( 'AS' )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==51) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // JPA.g:99:39: 'AS'
                    {
                    string_literal17=(Token)match(input,51,FOLLOW_51_in_range_variable_declaration558); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_51.add(string_literal17);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_range_variable_declaration562);
            identification_variable18=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable18.getTree());


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
            // 100:4: -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source )
            {
                // JPA.g:100:7: ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new IdentificationVariableNode(T_ID_VAR, (identification_variable18!=null?input.toString(identification_variable18.start,identification_variable18.stop):null)), root_1);

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
    // JPA.g:103:1: range_variable_declaration_source : ( abstract_schema_name | lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')' -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) );
    public final JPAParser.range_variable_declaration_source_return range_variable_declaration_source() throws RecognitionException {
        JPAParser.range_variable_declaration_source_return retval = new JPAParser.range_variable_declaration_source_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token lp=null;
        Token rp=null;
        JPAParser.abstract_schema_name_return abstract_schema_name19 = null;

        JPAParser.select_clause_return select_clause20 = null;

        JPAParser.from_clause_return from_clause21 = null;

        JPAParser.where_clause_return where_clause22 = null;

        JPAParser.groupby_clause_return groupby_clause23 = null;

        JPAParser.having_clause_return having_clause24 = null;

        JPAParser.orderby_clause_return orderby_clause25 = null;


        Object lp_tree=null;
        Object rp_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
        RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");
        try {
            // JPA.g:104:2: ( abstract_schema_name | lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')' -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==WORD) ) {
                alt13=1;
            }
            else if ( (LA13_0==52) ) {
                alt13=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // JPA.g:104:4: abstract_schema_name
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_abstract_schema_name_in_range_variable_declaration_source589);
                    abstract_schema_name19=abstract_schema_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, abstract_schema_name19.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:105:4: lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')'
                    {
                    lp=(Token)match(input,52,FOLLOW_52_in_range_variable_declaration_source596); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_52.add(lp);

                    pushFollow(FOLLOW_select_clause_in_range_variable_declaration_source598);
                    select_clause20=select_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_select_clause.add(select_clause20.getTree());
                    pushFollow(FOLLOW_from_clause_in_range_variable_declaration_source600);
                    from_clause21=from_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_from_clause.add(from_clause21.getTree());
                    // JPA.g:105:43: ( where_clause )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==57) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // JPA.g:105:44: where_clause
                            {
                            pushFollow(FOLLOW_where_clause_in_range_variable_declaration_source603);
                            where_clause22=where_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_where_clause.add(where_clause22.getTree());

                            }
                            break;

                    }

                    // JPA.g:105:59: ( groupby_clause )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==58) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // JPA.g:105:60: groupby_clause
                            {
                            pushFollow(FOLLOW_groupby_clause_in_range_variable_declaration_source608);
                            groupby_clause23=groupby_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause23.getTree());

                            }
                            break;

                    }

                    // JPA.g:105:77: ( having_clause )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==HAVING) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // JPA.g:105:78: having_clause
                            {
                            pushFollow(FOLLOW_having_clause_in_range_variable_declaration_source613);
                            having_clause24=having_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_having_clause.add(having_clause24.getTree());

                            }
                            break;

                    }

                    // JPA.g:105:93: ( orderby_clause )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==60) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // JPA.g:105:94: orderby_clause
                            {
                            pushFollow(FOLLOW_orderby_clause_in_range_variable_declaration_source617);
                            orderby_clause25=orderby_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause25.getTree());

                            }
                            break;

                    }

                    rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_range_variable_declaration_source623); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_RPAREN.add(rp);



                    // AST REWRITE
                    // elements: having_clause, orderby_clause, groupby_clause, where_clause, from_clause, select_clause
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 106:3: -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                    {
                        // JPA.g:106:6: ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);

                        // JPA.g:106:37: ( select_clause )?
                        if ( stream_select_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_select_clause.nextTree());

                        }
                        stream_select_clause.reset();
                        adaptor.addChild(root_1, stream_from_clause.nextTree());
                        // JPA.g:106:66: ( where_clause )?
                        if ( stream_where_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_where_clause.nextTree());

                        }
                        stream_where_clause.reset();
                        // JPA.g:106:82: ( groupby_clause )?
                        if ( stream_groupby_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                        }
                        stream_groupby_clause.reset();
                        // JPA.g:106:100: ( having_clause )?
                        if ( stream_having_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_having_clause.nextTree());

                        }
                        stream_having_clause.reset();
                        // JPA.g:106:117: ( orderby_clause )?
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
    // JPA.g:109:1: join : join_spec join_association_path_expression ( 'AS' )? identification_variable -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ) ;
    public final JPAParser.join_return join() throws RecognitionException {
        JPAParser.join_return retval = new JPAParser.join_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal28=null;
        JPAParser.join_spec_return join_spec26 = null;

        JPAParser.join_association_path_expression_return join_association_path_expression27 = null;

        JPAParser.identification_variable_return identification_variable29 = null;


        Object string_literal28_tree=null;
        RewriteRuleTokenStream stream_51=new RewriteRuleTokenStream(adaptor,"token 51");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
        RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");
        try {
            // JPA.g:110:2: ( join_spec join_association_path_expression ( 'AS' )? identification_variable -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ) )
            // JPA.g:110:4: join_spec join_association_path_expression ( 'AS' )? identification_variable
            {
            pushFollow(FOLLOW_join_spec_in_join677);
            join_spec26=join_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_spec.add(join_spec26.getTree());
            pushFollow(FOLLOW_join_association_path_expression_in_join679);
            join_association_path_expression27=join_association_path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression27.getTree());
            // JPA.g:110:47: ( 'AS' )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==51) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // JPA.g:110:48: 'AS'
                    {
                    string_literal28=(Token)match(input,51,FOLLOW_51_in_join682); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_51.add(string_literal28);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_join686);
            identification_variable29=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable29.getTree());


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
            // 111:4: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression )
            {
                // JPA.g:111:7: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new JoinVariableNode(T_JOIN_VAR, (join_spec26!=null?input.toString(join_spec26.start,join_spec26.stop):null), (identification_variable29!=null?input.toString(identification_variable29.start,identification_variable29.stop):null)), root_1);

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
    // JPA.g:114:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
    public final JPAParser.fetch_join_return fetch_join() throws RecognitionException {
        JPAParser.fetch_join_return retval = new JPAParser.fetch_join_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal31=null;
        JPAParser.join_spec_return join_spec30 = null;

        JPAParser.join_association_path_expression_return join_association_path_expression32 = null;


        Object string_literal31_tree=null;

        try {
            // JPA.g:115:2: ( join_spec 'FETCH' join_association_path_expression )
            // JPA.g:115:4: join_spec 'FETCH' join_association_path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_join_spec_in_fetch_join715);
            join_spec30=join_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec30.getTree());
            string_literal31=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join717); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal31_tree = (Object)adaptor.create(string_literal31);
            adaptor.addChild(root_0, string_literal31_tree);
            }
            pushFollow(FOLLOW_join_association_path_expression_in_fetch_join719);
            join_association_path_expression32=join_association_path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression32.getTree());

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
    // JPA.g:117:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
    public final JPAParser.join_spec_return join_spec() throws RecognitionException {
        JPAParser.join_spec_return retval = new JPAParser.join_spec_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal33=null;
        Token string_literal34=null;
        Token string_literal35=null;
        Token string_literal36=null;

        Object string_literal33_tree=null;
        Object string_literal34_tree=null;
        Object string_literal35_tree=null;
        Object string_literal36_tree=null;

        try {
            // JPA.g:118:2: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
            // JPA.g:118:3: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:118:3: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
            int alt16=3;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==LEFT) ) {
                alt16=1;
            }
            else if ( (LA16_0==INNER) ) {
                alt16=2;
            }
            switch (alt16) {
                case 1 :
                    // JPA.g:118:4: ( 'LEFT' ) ( 'OUTER' )?
                    {
                    // JPA.g:118:4: ( 'LEFT' )
                    // JPA.g:118:5: 'LEFT'
                    {
                    string_literal33=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec729); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal33_tree = (Object)adaptor.create(string_literal33);
                    adaptor.addChild(root_0, string_literal33_tree);
                    }

                    }

                    // JPA.g:118:13: ( 'OUTER' )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==OUTER) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // JPA.g:118:14: 'OUTER'
                            {
                            string_literal34=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec733); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal34_tree = (Object)adaptor.create(string_literal34);
                            adaptor.addChild(root_0, string_literal34_tree);
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // JPA.g:118:26: 'INNER'
                    {
                    string_literal35=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec739); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal35_tree = (Object)adaptor.create(string_literal35);
                    adaptor.addChild(root_0, string_literal35_tree);
                    }

                    }
                    break;

            }

            string_literal36=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec744); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal36_tree = (Object)adaptor.create(string_literal36);
            adaptor.addChild(root_0, string_literal36_tree);
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
    // JPA.g:120:1: join_association_path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
    public final JPAParser.join_association_path_expression_return join_association_path_expression() throws RecognitionException {
        JPAParser.join_association_path_expression_return retval = new JPAParser.join_association_path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal38=null;
        Token char_literal40=null;
        JPAParser.identification_variable_return identification_variable37 = null;

        JPAParser.field_return field39 = null;

        JPAParser.field_return field41 = null;


        Object char_literal38_tree=null;
        Object char_literal40_tree=null;
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // JPA.g:121:2: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
            // JPA.g:121:4: identification_variable '.' ( field '.' )* ( field )?
            {
            pushFollow(FOLLOW_identification_variable_in_join_association_path_expression753);
            identification_variable37=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable37.getTree());
            char_literal38=(Token)match(input,53,FOLLOW_53_in_join_association_path_expression755); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_53.add(char_literal38);

            // JPA.g:121:32: ( field '.' )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==WORD) ) {
                    int LA17_1 = input.LA(2);

                    if ( (LA17_1==53) ) {
                        alt17=1;
                    }


                }
                else if ( (LA17_0==58) ) {
                    int LA17_3 = input.LA(2);

                    if ( (LA17_3==53) ) {
                        alt17=1;
                    }


                }


                switch (alt17) {
            	case 1 :
            	    // JPA.g:121:33: field '.'
            	    {
            	    pushFollow(FOLLOW_field_in_join_association_path_expression758);
            	    field39=field();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_field.add(field39.getTree());
            	    char_literal40=(Token)match(input,53,FOLLOW_53_in_join_association_path_expression759); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_53.add(char_literal40);


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            // JPA.g:121:44: ( field )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==WORD) ) {
                int LA18_1 = input.LA(2);

                if ( (synpred20_JPA()) ) {
                    alt18=1;
                }
            }
            else if ( (LA18_0==58) ) {
                int LA18_3 = input.LA(2);

                if ( (LA18_3==EOF||LA18_3==HAVING||LA18_3==RPAREN||LA18_3==LEFT||(LA18_3>=INNER && LA18_3<=JOIN)||LA18_3==WORD||(LA18_3>=50 && LA18_3<=51)||(LA18_3>=57 && LA18_3<=58)||LA18_3==60) ) {
                    alt18=1;
                }
            }
            switch (alt18) {
                case 1 :
                    // JPA.g:0:0: field
                    {
                    pushFollow(FOLLOW_field_in_join_association_path_expression763);
                    field41=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field41.getTree());

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
            // 122:2: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
            {
                // JPA.g:122:5: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable37!=null?input.toString(identification_variable37.start,identification_variable37.stop):null)), root_1);

                // JPA.g:122:65: ( field )*
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
    // JPA.g:125:1: collection_member_declaration : 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
    public final JPAParser.collection_member_declaration_return collection_member_declaration() throws RecognitionException {
        JPAParser.collection_member_declaration_return retval = new JPAParser.collection_member_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal42=null;
        Token char_literal43=null;
        Token char_literal45=null;
        Token string_literal46=null;
        JPAParser.path_expression_return path_expression44 = null;

        JPAParser.identification_variable_return identification_variable47 = null;


        Object string_literal42_tree=null;
        Object char_literal43_tree=null;
        Object char_literal45_tree=null;
        Object string_literal46_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_51=new RewriteRuleTokenStream(adaptor,"token 51");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        try {
            // JPA.g:126:2: ( 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
            // JPA.g:126:4: 'IN' '(' path_expression ')' ( 'AS' )? identification_variable
            {
            string_literal42=(Token)match(input,54,FOLLOW_54_in_collection_member_declaration791); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_54.add(string_literal42);

            char_literal43=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration792); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_LPAREN.add(char_literal43);

            pushFollow(FOLLOW_path_expression_in_collection_member_declaration794);
            path_expression44=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_path_expression.add(path_expression44.getTree());
            char_literal45=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration796); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_RPAREN.add(char_literal45);

            // JPA.g:126:32: ( 'AS' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==51) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // JPA.g:126:33: 'AS'
                    {
                    string_literal46=(Token)match(input,51,FOLLOW_51_in_collection_member_declaration799); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_51.add(string_literal46);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_collection_member_declaration803);
            identification_variable47=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable47.getTree());


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
            // 127:2: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
            {
                // JPA.g:127:5: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new CollectionMemberNode(T_COLLECTION_MEMBER, (identification_variable47!=null?input.toString(identification_variable47.start,identification_variable47.stop):null)), root_1);

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
    // JPA.g:130:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
    public final JPAParser.path_expression_return path_expression() throws RecognitionException {
        JPAParser.path_expression_return retval = new JPAParser.path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal49=null;
        Token char_literal51=null;
        JPAParser.identification_variable_return identification_variable48 = null;

        JPAParser.field_return field50 = null;

        JPAParser.field_return field52 = null;


        Object char_literal49_tree=null;
        Object char_literal51_tree=null;
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // JPA.g:131:2: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
            // JPA.g:131:5: identification_variable '.' ( field '.' )* ( field )?
            {
            pushFollow(FOLLOW_identification_variable_in_path_expression829);
            identification_variable48=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable48.getTree());
            char_literal49=(Token)match(input,53,FOLLOW_53_in_path_expression831); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_53.add(char_literal49);

            // JPA.g:131:33: ( field '.' )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==58) ) {
                    int LA20_1 = input.LA(2);

                    if ( (LA20_1==53) ) {
                        alt20=1;
                    }


                }
                else if ( (LA20_0==WORD) ) {
                    int LA20_3 = input.LA(2);

                    if ( (LA20_3==53) ) {
                        alt20=1;
                    }


                }


                switch (alt20) {
            	case 1 :
            	    // JPA.g:131:34: field '.'
            	    {
            	    pushFollow(FOLLOW_field_in_path_expression834);
            	    field50=field();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_field.add(field50.getTree());
            	    char_literal51=(Token)match(input,53,FOLLOW_53_in_path_expression835); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_53.add(char_literal51);


            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

            // JPA.g:131:45: ( field )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==58) ) {
                int LA21_1 = input.LA(2);

                if ( (LA21_1==EOF||(LA21_1>=HAVING && LA21_1<=DESC)||(LA21_1>=OR && LA21_1<=AND)||LA21_1==RPAREN||LA21_1==WORD||(LA21_1>=49 && LA21_1<=51)||LA21_1==54||LA21_1==58||(LA21_1>=60 && LA21_1<=61)||(LA21_1>=64 && LA21_1<=65)||(LA21_1>=76 && LA21_1<=77)||LA21_1==79||LA21_1==82||(LA21_1>=88 && LA21_1<=95)) ) {
                    alt21=1;
                }
            }
            else if ( (LA21_0==WORD) ) {
                int LA21_3 = input.LA(2);

                if ( (synpred23_JPA()) ) {
                    alt21=1;
                }
            }
            switch (alt21) {
                case 1 :
                    // JPA.g:131:46: field
                    {
                    pushFollow(FOLLOW_field_in_path_expression840);
                    field52=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field52.getTree());

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
            // 132:2: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
            {
                // JPA.g:132:5: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable48!=null?input.toString(identification_variable48.start,identification_variable48.stop):null)), root_1);

                // JPA.g:132:65: ( field )*
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
    // JPA.g:135:1: select_clause : ( 'DISTINCT' )? select_expression ( ',' select_expression )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* ) ;
    public final JPAParser.select_clause_return select_clause() throws RecognitionException {
        JPAParser.select_clause_return retval = new JPAParser.select_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal53=null;
        Token char_literal55=null;
        JPAParser.select_expression_return select_expression54 = null;

        JPAParser.select_expression_return select_expression56 = null;


        Object string_literal53_tree=null;
        Object char_literal55_tree=null;
        RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
        RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");
        RewriteRuleSubtreeStream stream_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule select_expression");
        try {
            // JPA.g:136:2: ( ( 'DISTINCT' )? select_expression ( ',' select_expression )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* ) )
            // JPA.g:136:4: ( 'DISTINCT' )? select_expression ( ',' select_expression )*
            {
            // JPA.g:136:4: ( 'DISTINCT' )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==DISTINCT) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // JPA.g:136:5: 'DISTINCT'
                    {
                    string_literal53=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause870); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal53);


                    }
                    break;

            }

            pushFollow(FOLLOW_select_expression_in_select_clause874);
            select_expression54=select_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_select_expression.add(select_expression54.getTree());
            // JPA.g:136:36: ( ',' select_expression )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==50) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // JPA.g:136:37: ',' select_expression
            	    {
            	    char_literal55=(Token)match(input,50,FOLLOW_50_in_select_clause877); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_50.add(char_literal55);

            	    pushFollow(FOLLOW_select_expression_in_select_clause879);
            	    select_expression56=select_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_select_expression.add(select_expression56.getTree());

            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);



            // AST REWRITE
            // elements: select_expression, DISTINCT
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 137:2: -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* )
            {
                // JPA.g:137:5: ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);

                // JPA.g:137:24: ( 'DISTINCT' )?
                if ( stream_DISTINCT.hasNext() ) {
                    adaptor.addChild(root_1, stream_DISTINCT.nextNode());

                }
                stream_DISTINCT.reset();
                // JPA.g:137:38: ( ^( T_SELECTED_ITEM[] select_expression ) )*
                while ( stream_select_expression.hasNext() ) {
                    // JPA.g:137:38: ^( T_SELECTED_ITEM[] select_expression )
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
    // JPA.g:140:1: select_expression : ( path_expression | aggregate_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | 'OBJECT' '(' identification_variable ')' | constructor_expression );
    public final JPAParser.select_expression_return select_expression() throws RecognitionException {
        JPAParser.select_expression_return retval = new JPAParser.select_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal60=null;
        Token char_literal61=null;
        Token char_literal63=null;
        JPAParser.path_expression_return path_expression57 = null;

        JPAParser.aggregate_expression_return aggregate_expression58 = null;

        JPAParser.identification_variable_return identification_variable59 = null;

        JPAParser.identification_variable_return identification_variable62 = null;

        JPAParser.constructor_expression_return constructor_expression64 = null;


        Object string_literal60_tree=null;
        Object char_literal61_tree=null;
        Object char_literal63_tree=null;
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        try {
            // JPA.g:141:2: ( path_expression | aggregate_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | 'OBJECT' '(' identification_variable ')' | constructor_expression )
            int alt24=5;
            switch ( input.LA(1) ) {
            case WORD:
                {
                int LA24_1 = input.LA(2);

                if ( (LA24_1==53) ) {
                    alt24=1;
                }
                else if ( (LA24_1==EOF||(LA24_1>=49 && LA24_1<=50)) ) {
                    alt24=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 24, 1, input);

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
                alt24=2;
                }
                break;
            case 55:
                {
                alt24=4;
                }
                break;
            case 56:
                {
                alt24=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }

            switch (alt24) {
                case 1 :
                    // JPA.g:141:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_select_expression915);
                    path_expression57=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression57.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:142:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_select_expression920);
                    aggregate_expression58=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression58.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:143:4: identification_variable
                    {
                    pushFollow(FOLLOW_identification_variable_in_select_expression925);
                    identification_variable59=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable59.getTree());


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
                    // 143:28: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
                    {
                        // JPA.g:143:31: ^( T_SELECTED_ENTITY[$identification_variable.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_ENTITY, (identification_variable59!=null?input.toString(identification_variable59.start,identification_variable59.stop):null)), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // JPA.g:144:4: 'OBJECT' '(' identification_variable ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal60=(Token)match(input,55,FOLLOW_55_in_select_expression940); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal60_tree = (Object)adaptor.create(string_literal60);
                    adaptor.addChild(root_0, string_literal60_tree);
                    }
                    char_literal61=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression942); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal61_tree = (Object)adaptor.create(char_literal61);
                    adaptor.addChild(root_0, char_literal61_tree);
                    }
                    pushFollow(FOLLOW_identification_variable_in_select_expression943);
                    identification_variable62=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable62.getTree());
                    char_literal63=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression944); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal63_tree = (Object)adaptor.create(char_literal63);
                    adaptor.addChild(root_0, char_literal63_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:145:4: constructor_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_constructor_expression_in_select_expression949);
                    constructor_expression64=constructor_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_expression64.getTree());

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
    // JPA.g:147:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
    public final JPAParser.constructor_expression_return constructor_expression() throws RecognitionException {
        JPAParser.constructor_expression_return retval = new JPAParser.constructor_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal65=null;
        Token char_literal67=null;
        Token char_literal69=null;
        Token char_literal71=null;
        JPAParser.constructor_name_return constructor_name66 = null;

        JPAParser.constructor_item_return constructor_item68 = null;

        JPAParser.constructor_item_return constructor_item70 = null;


        Object string_literal65_tree=null;
        Object char_literal67_tree=null;
        Object char_literal69_tree=null;
        Object char_literal71_tree=null;

        try {
            // JPA.g:148:2: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
            // JPA.g:148:4: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal65=(Token)match(input,56,FOLLOW_56_in_constructor_expression958); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal65_tree = (Object)adaptor.create(string_literal65);
            adaptor.addChild(root_0, string_literal65_tree);
            }
            pushFollow(FOLLOW_constructor_name_in_constructor_expression960);
            constructor_name66=constructor_name();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name66.getTree());
            char_literal67=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression962); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal67_tree = (Object)adaptor.create(char_literal67);
            adaptor.addChild(root_0, char_literal67_tree);
            }
            pushFollow(FOLLOW_constructor_item_in_constructor_expression964);
            constructor_item68=constructor_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item68.getTree());
            // JPA.g:148:48: ( ',' constructor_item )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==50) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // JPA.g:148:49: ',' constructor_item
            	    {
            	    char_literal69=(Token)match(input,50,FOLLOW_50_in_constructor_expression967); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal69_tree = (Object)adaptor.create(char_literal69);
            	    adaptor.addChild(root_0, char_literal69_tree);
            	    }
            	    pushFollow(FOLLOW_constructor_item_in_constructor_expression969);
            	    constructor_item70=constructor_item();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item70.getTree());

            	    }
            	    break;

            	default :
            	    break loop25;
                }
            } while (true);

            char_literal71=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression973); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal71_tree = (Object)adaptor.create(char_literal71);
            adaptor.addChild(root_0, char_literal71_tree);
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
    // JPA.g:150:1: constructor_item : ( path_expression | aggregate_expression );
    public final JPAParser.constructor_item_return constructor_item() throws RecognitionException {
        JPAParser.constructor_item_return retval = new JPAParser.constructor_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression72 = null;

        JPAParser.aggregate_expression_return aggregate_expression73 = null;



        try {
            // JPA.g:151:2: ( path_expression | aggregate_expression )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==WORD) ) {
                alt26=1;
            }
            else if ( ((LA26_0>=AVG && LA26_0<=COUNT)) ) {
                alt26=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // JPA.g:151:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_constructor_item982);
                    path_expression72=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression72.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:151:22: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_constructor_item986);
                    aggregate_expression73=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression73.getTree());

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
    // JPA.g:153:1: aggregate_expression : ( aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' ) );
    public final JPAParser.aggregate_expression_return aggregate_expression() throws RecognitionException {
        JPAParser.aggregate_expression_return retval = new JPAParser.aggregate_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal75=null;
        Token string_literal76=null;
        Token char_literal78=null;
        Token string_literal79=null;
        Token char_literal80=null;
        Token string_literal81=null;
        Token char_literal83=null;
        JPAParser.aggregate_expression_function_name_return aggregate_expression_function_name74 = null;

        JPAParser.path_expression_return path_expression77 = null;

        JPAParser.identification_variable_return identification_variable82 = null;


        Object char_literal75_tree=null;
        Object string_literal76_tree=null;
        Object char_literal78_tree=null;
        Object string_literal79_tree=null;
        Object char_literal80_tree=null;
        Object string_literal81_tree=null;
        Object char_literal83_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COUNT=new RewriteRuleTokenStream(adaptor,"token COUNT");
        RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        RewriteRuleSubtreeStream stream_aggregate_expression_function_name=new RewriteRuleSubtreeStream(adaptor,"rule aggregate_expression_function_name");
        try {
            // JPA.g:154:2: ( aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' ) )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==COUNT) ) {
                int LA29_1 = input.LA(2);

                if ( (LA29_1==LPAREN) ) {
                    int LA29_3 = input.LA(3);

                    if ( (LA29_3==DISTINCT) ) {
                        int LA29_4 = input.LA(4);

                        if ( (LA29_4==WORD) ) {
                            int LA29_5 = input.LA(5);

                            if ( (LA29_5==RPAREN) ) {
                                alt29=2;
                            }
                            else if ( (LA29_5==53) ) {
                                alt29=1;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 29, 5, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 29, 4, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA29_3==WORD) ) {
                        int LA29_5 = input.LA(4);

                        if ( (LA29_5==RPAREN) ) {
                            alt29=2;
                        }
                        else if ( (LA29_5==53) ) {
                            alt29=1;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 29, 5, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 29, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 29, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA29_0>=AVG && LA29_0<=SUM)) ) {
                alt29=1;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }
            switch (alt29) {
                case 1 :
                    // JPA.g:154:4: aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')'
                    {
                    pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression995);
                    aggregate_expression_function_name74=aggregate_expression_function_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name74.getTree());
                    char_literal75=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression997); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal75);

                    // JPA.g:154:43: ( 'DISTINCT' )?
                    int alt27=2;
                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==DISTINCT) ) {
                        alt27=1;
                    }
                    switch (alt27) {
                        case 1 :
                            // JPA.g:154:44: 'DISTINCT'
                            {
                            string_literal76=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1000); if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal76);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_path_expression_in_aggregate_expression1004);
                    path_expression77=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression77.getTree());
                    char_literal78=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1005); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_RPAREN.add(char_literal78);



                    // AST REWRITE
                    // elements: LPAREN, aggregate_expression_function_name, DISTINCT, RPAREN, path_expression
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 155:2: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
                    {
                        // JPA.g:155:5: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);

                        adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
                        adaptor.addChild(root_1, stream_LPAREN.nextNode());
                        // JPA.g:155:90: ( 'DISTINCT' )?
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
                    // JPA.g:156:4: 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')'
                    {
                    string_literal79=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1033); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_COUNT.add(string_literal79);

                    char_literal80=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1035); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal80);

                    // JPA.g:156:16: ( 'DISTINCT' )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==DISTINCT) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // JPA.g:156:17: 'DISTINCT'
                            {
                            string_literal81=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1038); if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal81);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_identification_variable_in_aggregate_expression1042);
                    identification_variable82=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable82.getTree());
                    char_literal83=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1044); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_RPAREN.add(char_literal83);



                    // AST REWRITE
                    // elements: DISTINCT, LPAREN, identification_variable, COUNT, RPAREN
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 157:2: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' )
                    {
                        // JPA.g:157:5: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);

                        adaptor.addChild(root_1, stream_COUNT.nextNode());
                        adaptor.addChild(root_1, stream_LPAREN.nextNode());
                        // JPA.g:157:63: ( 'DISTINCT' )?
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
    // JPA.g:159:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
    public final JPAParser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
        JPAParser.aggregate_expression_function_name_return retval = new JPAParser.aggregate_expression_function_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set84=null;

        Object set84_tree=null;

        try {
            // JPA.g:160:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set84=(Token)input.LT(1);
            if ( (input.LA(1)>=AVG && input.LA(1)<=COUNT) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set84));
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
    // JPA.g:162:1: where_clause : (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) );
    public final JPAParser.where_clause_return where_clause() throws RecognitionException {
        JPAParser.where_clause_return retval = new JPAParser.where_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token wh=null;
        Token string_literal86=null;
        JPAParser.conditional_expression_return conditional_expression85 = null;

        JPAParser.path_expression_return path_expression87 = null;


        Object wh_tree=null;
        Object string_literal86_tree=null;
        RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
        RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        try {
            // JPA.g:163:2: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) )
            int alt30=2;
            alt30 = dfa30.predict(input);
            switch (alt30) {
                case 1 :
                    // JPA.g:163:4: wh= 'WHERE' conditional_expression
                    {
                    wh=(Token)match(input,57,FOLLOW_57_in_where_clause1106); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_57.add(wh);

                    pushFollow(FOLLOW_conditional_expression_in_where_clause1108);
                    conditional_expression85=conditional_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression85.getTree());


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
                    // 163:37: -> ^( T_CONDITION[$wh] conditional_expression )
                    {
                        // JPA.g:163:40: ^( T_CONDITION[$wh] conditional_expression )
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
                    // JPA.g:164:4: 'WHERE' path_expression
                    {
                    string_literal86=(Token)match(input,57,FOLLOW_57_in_where_clause1124); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_57.add(string_literal86);

                    pushFollow(FOLLOW_path_expression_in_where_clause1126);
                    path_expression87=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression87.getTree());


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
                    // 164:28: -> ^( T_CONDITION[$wh] path_expression )
                    {
                        // JPA.g:164:31: ^( T_CONDITION[$wh] path_expression )
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
    // JPA.g:166:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
    public final JPAParser.groupby_clause_return groupby_clause() throws RecognitionException {
        JPAParser.groupby_clause_return retval = new JPAParser.groupby_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal88=null;
        Token string_literal89=null;
        Token char_literal91=null;
        JPAParser.groupby_item_return groupby_item90 = null;

        JPAParser.groupby_item_return groupby_item92 = null;


        Object string_literal88_tree=null;
        Object string_literal89_tree=null;
        Object char_literal91_tree=null;
        RewriteRuleTokenStream stream_59=new RewriteRuleTokenStream(adaptor,"token 59");
        RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
        RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");
        RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");
        try {
            // JPA.g:167:2: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
            // JPA.g:167:4: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
            {
            string_literal88=(Token)match(input,58,FOLLOW_58_in_groupby_clause1147); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_58.add(string_literal88);

            string_literal89=(Token)match(input,59,FOLLOW_59_in_groupby_clause1149); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_59.add(string_literal89);

            pushFollow(FOLLOW_groupby_item_in_groupby_clause1151);
            groupby_item90=groupby_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item90.getTree());
            // JPA.g:167:30: ( ',' groupby_item )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==50) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // JPA.g:167:31: ',' groupby_item
            	    {
            	    char_literal91=(Token)match(input,50,FOLLOW_50_in_groupby_clause1154); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_50.add(char_literal91);

            	    pushFollow(FOLLOW_groupby_item_in_groupby_clause1156);
            	    groupby_item92=groupby_item();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item92.getTree());

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);



            // AST REWRITE
            // elements: 58, 59, groupby_item
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 168:2: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
            {
                // JPA.g:168:5: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new GroupByNode(T_GROUP_BY), root_1);

                adaptor.addChild(root_1, stream_58.nextNode());
                adaptor.addChild(root_1, stream_59.nextNode());
                // JPA.g:168:46: ( groupby_item )*
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
    // JPA.g:171:1: groupby_item : ( path_expression | identification_variable );
    public final JPAParser.groupby_item_return groupby_item() throws RecognitionException {
        JPAParser.groupby_item_return retval = new JPAParser.groupby_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression93 = null;

        JPAParser.identification_variable_return identification_variable94 = null;



        try {
            // JPA.g:172:2: ( path_expression | identification_variable )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==WORD) ) {
                int LA32_1 = input.LA(2);

                if ( (LA32_1==53) ) {
                    alt32=1;
                }
                else if ( (LA32_1==EOF||LA32_1==HAVING||LA32_1==RPAREN||LA32_1==50||LA32_1==60) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // JPA.g:172:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_groupby_item1187);
                    path_expression93=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression93.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:172:22: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_groupby_item1191);
                    identification_variable94=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable94.getTree());

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
    // JPA.g:174:1: having_clause : 'HAVING' conditional_expression ;
    public final JPAParser.having_clause_return having_clause() throws RecognitionException {
        JPAParser.having_clause_return retval = new JPAParser.having_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal95=null;
        JPAParser.conditional_expression_return conditional_expression96 = null;


        Object string_literal95_tree=null;

        try {
            // JPA.g:175:2: ( 'HAVING' conditional_expression )
            // JPA.g:175:4: 'HAVING' conditional_expression
            {
            root_0 = (Object)adaptor.nil();

            string_literal95=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1200); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal95_tree = (Object)adaptor.create(string_literal95);
            adaptor.addChild(root_0, string_literal95_tree);
            }
            pushFollow(FOLLOW_conditional_expression_in_having_clause1202);
            conditional_expression96=conditional_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression96.getTree());

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
    // JPA.g:177:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
    public final JPAParser.orderby_clause_return orderby_clause() throws RecognitionException {
        JPAParser.orderby_clause_return retval = new JPAParser.orderby_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal97=null;
        Token string_literal98=null;
        Token char_literal100=null;
        JPAParser.orderby_item_return orderby_item99 = null;

        JPAParser.orderby_item_return orderby_item101 = null;


        Object string_literal97_tree=null;
        Object string_literal98_tree=null;
        Object char_literal100_tree=null;
        RewriteRuleTokenStream stream_59=new RewriteRuleTokenStream(adaptor,"token 59");
        RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
        RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");
        RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");
        try {
            // JPA.g:178:2: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
            // JPA.g:178:4: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
            {
            string_literal97=(Token)match(input,60,FOLLOW_60_in_orderby_clause1211); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_60.add(string_literal97);

            string_literal98=(Token)match(input,59,FOLLOW_59_in_orderby_clause1213); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_59.add(string_literal98);

            pushFollow(FOLLOW_orderby_item_in_orderby_clause1215);
            orderby_item99=orderby_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item99.getTree());
            // JPA.g:178:30: ( ',' orderby_item )*
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0==50) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // JPA.g:178:31: ',' orderby_item
            	    {
            	    char_literal100=(Token)match(input,50,FOLLOW_50_in_orderby_clause1218); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_50.add(char_literal100);

            	    pushFollow(FOLLOW_orderby_item_in_orderby_clause1220);
            	    orderby_item101=orderby_item();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item101.getTree());

            	    }
            	    break;

            	default :
            	    break loop33;
                }
            } while (true);



            // AST REWRITE
            // elements: orderby_item, 59, 60
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 179:2: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
            {
                // JPA.g:179:5: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new OrderByNode(T_ORDER_BY), root_1);

                adaptor.addChild(root_1, stream_60.nextNode());
                adaptor.addChild(root_1, stream_59.nextNode());
                // JPA.g:179:46: ( orderby_item )*
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
    // JPA.g:181:1: orderby_item : ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) );
    public final JPAParser.orderby_item_return orderby_item() throws RecognitionException {
        JPAParser.orderby_item_return retval = new JPAParser.orderby_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal103=null;
        Token string_literal105=null;
        JPAParser.path_expression_return path_expression102 = null;

        JPAParser.path_expression_return path_expression104 = null;


        Object string_literal103_tree=null;
        Object string_literal105_tree=null;
        RewriteRuleTokenStream stream_DESC=new RewriteRuleTokenStream(adaptor,"token DESC");
        RewriteRuleTokenStream stream_ASC=new RewriteRuleTokenStream(adaptor,"token ASC");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        try {
            // JPA.g:182:2: ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) )
            int alt35=2;
            alt35 = dfa35.predict(input);
            switch (alt35) {
                case 1 :
                    // JPA.g:182:4: path_expression ( 'ASC' )?
                    {
                    pushFollow(FOLLOW_path_expression_in_orderby_item1249);
                    path_expression102=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression102.getTree());
                    // JPA.g:182:20: ( 'ASC' )?
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==ASC) ) {
                        alt34=1;
                    }
                    switch (alt34) {
                        case 1 :
                            // JPA.g:182:21: 'ASC'
                            {
                            string_literal103=(Token)match(input,ASC,FOLLOW_ASC_in_orderby_item1252); if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_ASC.add(string_literal103);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: path_expression, ASC
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 183:3: -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? )
                    {
                        // JPA.g:183:6: ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);

                        adaptor.addChild(root_1, stream_path_expression.nextTree());
                        // JPA.g:183:61: ( 'ASC' )?
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
                    // JPA.g:184:4: path_expression 'DESC'
                    {
                    pushFollow(FOLLOW_path_expression_in_orderby_item1278);
                    path_expression104=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression104.getTree());
                    string_literal105=(Token)match(input,DESC,FOLLOW_DESC_in_orderby_item1280); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_DESC.add(string_literal105);



                    // AST REWRITE
                    // elements: DESC, path_expression
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 185:2: -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' )
                    {
                        // JPA.g:185:5: ^( T_ORDER_BY_FIELD[] path_expression 'DESC' )
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
    // JPA.g:187:1: subquery : lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
    public final JPAParser.subquery_return subquery() throws RecognitionException {
        JPAParser.subquery_return retval = new JPAParser.subquery_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token lp=null;
        Token rp=null;
        JPAParser.simple_select_clause_return simple_select_clause106 = null;

        JPAParser.subquery_from_clause_return subquery_from_clause107 = null;

        JPAParser.where_clause_return where_clause108 = null;

        JPAParser.groupby_clause_return groupby_clause109 = null;

        JPAParser.having_clause_return having_clause110 = null;


        Object lp_tree=null;
        Object rp_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
        RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
        try {
            // JPA.g:188:2: (lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
            // JPA.g:188:4: lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
            {
            lp=(Token)match(input,52,FOLLOW_52_in_subquery1306); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_52.add(lp);

            pushFollow(FOLLOW_simple_select_clause_in_subquery1308);
            simple_select_clause106=simple_select_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause106.getTree());
            pushFollow(FOLLOW_subquery_from_clause_in_subquery1310);
            subquery_from_clause107=subquery_from_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause107.getTree());
            // JPA.g:188:59: ( where_clause )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==57) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // JPA.g:188:60: where_clause
                    {
                    pushFollow(FOLLOW_where_clause_in_subquery1313);
                    where_clause108=where_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_where_clause.add(where_clause108.getTree());

                    }
                    break;

            }

            // JPA.g:188:75: ( groupby_clause )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==58) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // JPA.g:188:76: groupby_clause
                    {
                    pushFollow(FOLLOW_groupby_clause_in_subquery1318);
                    groupby_clause109=groupby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause109.getTree());

                    }
                    break;

            }

            // JPA.g:188:93: ( having_clause )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==HAVING) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // JPA.g:188:94: having_clause
                    {
                    pushFollow(FOLLOW_having_clause_in_subquery1323);
                    having_clause110=having_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_having_clause.add(having_clause110.getTree());

                    }
                    break;

            }

            rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1329); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_RPAREN.add(rp);



            // AST REWRITE
            // elements: simple_select_clause, having_clause, where_clause, groupby_clause, subquery_from_clause
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 189:3: -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
            {
                // JPA.g:189:6: ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);

                adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
                adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
                // JPA.g:189:78: ( where_clause )?
                if ( stream_where_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_where_clause.nextTree());

                }
                stream_where_clause.reset();
                // JPA.g:189:94: ( groupby_clause )?
                if ( stream_groupby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                }
                stream_groupby_clause.reset();
                // JPA.g:189:112: ( having_clause )?
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
    // JPA.g:191:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
    public final JPAParser.subquery_from_clause_return subquery_from_clause() throws RecognitionException {
        JPAParser.subquery_from_clause_return retval = new JPAParser.subquery_from_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token fr=null;
        Token char_literal112=null;
        JPAParser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration111 = null;

        JPAParser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration113 = null;


        Object fr_tree=null;
        Object char_literal112_tree=null;
        RewriteRuleTokenStream stream_49=new RewriteRuleTokenStream(adaptor,"token 49");
        RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");
        RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");
        try {
            // JPA.g:192:2: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
            // JPA.g:192:4: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
            {
            fr=(Token)match(input,49,FOLLOW_49_in_subquery_from_clause1372); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_49.add(fr);

            pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1374);
            subselect_identification_variable_declaration111=subselect_identification_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration111.getTree());
            // JPA.g:192:60: ( ',' subselect_identification_variable_declaration )*
            loop39:
            do {
                int alt39=2;
                int LA39_0 = input.LA(1);

                if ( (LA39_0==50) ) {
                    alt39=1;
                }


                switch (alt39) {
            	case 1 :
            	    // JPA.g:192:61: ',' subselect_identification_variable_declaration
            	    {
            	    char_literal112=(Token)match(input,50,FOLLOW_50_in_subquery_from_clause1377); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_50.add(char_literal112);

            	    pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1379);
            	    subselect_identification_variable_declaration113=subselect_identification_variable_declaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration113.getTree());

            	    }
            	    break;

            	default :
            	    break loop39;
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
            // 193:2: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
            {
                // JPA.g:193:5: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);

                // JPA.g:193:32: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
                while ( stream_subselect_identification_variable_declaration.hasNext() ) {
                    // JPA.g:193:32: ^( T_SOURCE subselect_identification_variable_declaration )
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
    // JPA.g:195:1: subselect_identification_variable_declaration : ( identification_variable_declaration | association_path_expression ( 'AS' )? identification_variable | collection_member_declaration );
    public final JPAParser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration() throws RecognitionException {
        JPAParser.subselect_identification_variable_declaration_return retval = new JPAParser.subselect_identification_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal116=null;
        JPAParser.identification_variable_declaration_return identification_variable_declaration114 = null;

        JPAParser.association_path_expression_return association_path_expression115 = null;

        JPAParser.identification_variable_return identification_variable117 = null;

        JPAParser.collection_member_declaration_return collection_member_declaration118 = null;


        Object string_literal116_tree=null;

        try {
            // JPA.g:196:2: ( identification_variable_declaration | association_path_expression ( 'AS' )? identification_variable | collection_member_declaration )
            int alt41=3;
            switch ( input.LA(1) ) {
            case WORD:
                {
                int LA41_1 = input.LA(2);

                if ( (LA41_1==WORD||LA41_1==51) ) {
                    alt41=1;
                }
                else if ( (LA41_1==53) ) {
                    alt41=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 41, 1, input);

                    throw nvae;
                }
                }
                break;
            case 52:
                {
                alt41=1;
                }
                break;
            case 54:
                {
                alt41=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 41, 0, input);

                throw nvae;
            }

            switch (alt41) {
                case 1 :
                    // JPA.g:196:4: identification_variable_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1411);
                    identification_variable_declaration114=identification_variable_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration114.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:197:4: association_path_expression ( 'AS' )? identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_association_path_expression_in_subselect_identification_variable_declaration1416);
                    association_path_expression115=association_path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, association_path_expression115.getTree());
                    // JPA.g:197:32: ( 'AS' )?
                    int alt40=2;
                    int LA40_0 = input.LA(1);

                    if ( (LA40_0==51) ) {
                        alt40=1;
                    }
                    switch (alt40) {
                        case 1 :
                            // JPA.g:197:33: 'AS'
                            {
                            string_literal116=(Token)match(input,51,FOLLOW_51_in_subselect_identification_variable_declaration1419); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal116_tree = (Object)adaptor.create(string_literal116);
                            adaptor.addChild(root_0, string_literal116_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration1423);
                    identification_variable117=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable117.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:198:4: collection_member_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_declaration_in_subselect_identification_variable_declaration1428);
                    collection_member_declaration118=collection_member_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_declaration118.getTree());

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
    // JPA.g:200:1: association_path_expression : path_expression ;
    public final JPAParser.association_path_expression_return association_path_expression() throws RecognitionException {
        JPAParser.association_path_expression_return retval = new JPAParser.association_path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression119 = null;



        try {
            // JPA.g:201:2: ( path_expression )
            // JPA.g:201:4: path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_association_path_expression1437);
            path_expression119=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression119.getTree());

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
    // JPA.g:203:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
    public final JPAParser.simple_select_clause_return simple_select_clause() throws RecognitionException {
        JPAParser.simple_select_clause_return retval = new JPAParser.simple_select_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal120=null;
        JPAParser.simple_select_expression_return simple_select_expression121 = null;


        Object string_literal120_tree=null;
        RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
        RewriteRuleSubtreeStream stream_simple_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_expression");
        try {
            // JPA.g:204:2: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
            // JPA.g:204:4: ( 'DISTINCT' )? simple_select_expression
            {
            // JPA.g:204:4: ( 'DISTINCT' )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==DISTINCT) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // JPA.g:204:5: 'DISTINCT'
                    {
                    string_literal120=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause1447); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal120);


                    }
                    break;

            }

            pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause1451);
            simple_select_expression121=simple_select_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_simple_select_expression.add(simple_select_expression121.getTree());


            // AST REWRITE
            // elements: DISTINCT, simple_select_expression
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 205:2: -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
            {
                // JPA.g:205:5: ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);

                // JPA.g:205:24: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);

                // JPA.g:205:62: ( 'DISTINCT' )?
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
    // JPA.g:207:1: simple_select_expression : ( path_expression | aggregate_expression | identification_variable );
    public final JPAParser.simple_select_expression_return simple_select_expression() throws RecognitionException {
        JPAParser.simple_select_expression_return retval = new JPAParser.simple_select_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression122 = null;

        JPAParser.aggregate_expression_return aggregate_expression123 = null;

        JPAParser.identification_variable_return identification_variable124 = null;



        try {
            // JPA.g:208:2: ( path_expression | aggregate_expression | identification_variable )
            int alt43=3;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==WORD) ) {
                int LA43_1 = input.LA(2);

                if ( (LA43_1==53) ) {
                    alt43=1;
                }
                else if ( (LA43_1==49) ) {
                    alt43=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 43, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA43_0>=AVG && LA43_0<=COUNT)) ) {
                alt43=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 43, 0, input);

                throw nvae;
            }
            switch (alt43) {
                case 1 :
                    // JPA.g:208:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_simple_select_expression1482);
                    path_expression122=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression122.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:209:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression1487);
                    aggregate_expression123=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression123.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:210:4: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_simple_select_expression1492);
                    identification_variable124=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable124.getTree());

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
    // JPA.g:212:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
    public final JPAParser.conditional_expression_return conditional_expression() throws RecognitionException {
        JPAParser.conditional_expression_return retval = new JPAParser.conditional_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal126=null;
        JPAParser.conditional_term_return conditional_term125 = null;

        JPAParser.conditional_term_return conditional_term127 = null;


        Object string_literal126_tree=null;

        try {
            // JPA.g:213:2: ( ( conditional_term ) ( 'OR' conditional_term )* )
            // JPA.g:213:4: ( conditional_term ) ( 'OR' conditional_term )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:213:4: ( conditional_term )
            // JPA.g:213:5: conditional_term
            {
            pushFollow(FOLLOW_conditional_term_in_conditional_expression1502);
            conditional_term125=conditional_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term125.getTree());

            }

            // JPA.g:213:23: ( 'OR' conditional_term )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==OR) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // JPA.g:213:24: 'OR' conditional_term
            	    {
            	    string_literal126=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression1506); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal126_tree = (Object)adaptor.create(string_literal126);
            	    adaptor.addChild(root_0, string_literal126_tree);
            	    }
            	    pushFollow(FOLLOW_conditional_term_in_conditional_expression1508);
            	    conditional_term127=conditional_term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term127.getTree());

            	    }
            	    break;

            	default :
            	    break loop44;
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
    // JPA.g:215:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
    public final JPAParser.conditional_term_return conditional_term() throws RecognitionException {
        JPAParser.conditional_term_return retval = new JPAParser.conditional_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal129=null;
        JPAParser.conditional_factor_return conditional_factor128 = null;

        JPAParser.conditional_factor_return conditional_factor130 = null;


        Object string_literal129_tree=null;

        try {
            // JPA.g:216:2: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
            // JPA.g:216:4: ( conditional_factor ) ( 'AND' conditional_factor )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:216:4: ( conditional_factor )
            // JPA.g:216:5: conditional_factor
            {
            pushFollow(FOLLOW_conditional_factor_in_conditional_term1520);
            conditional_factor128=conditional_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor128.getTree());

            }

            // JPA.g:216:25: ( 'AND' conditional_factor )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==AND) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // JPA.g:216:26: 'AND' conditional_factor
            	    {
            	    string_literal129=(Token)match(input,AND,FOLLOW_AND_in_conditional_term1524); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal129_tree = (Object)adaptor.create(string_literal129);
            	    adaptor.addChild(root_0, string_literal129_tree);
            	    }
            	    pushFollow(FOLLOW_conditional_factor_in_conditional_term1526);
            	    conditional_factor130=conditional_factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor130.getTree());

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
    // $ANTLR end "conditional_term"

    public static class conditional_factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "conditional_factor"
    // JPA.g:218:1: conditional_factor : ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' );
    public final JPAParser.conditional_factor_return conditional_factor() throws RecognitionException {
        JPAParser.conditional_factor_return retval = new JPAParser.conditional_factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal131=null;
        Token char_literal133=null;
        Token char_literal135=null;
        JPAParser.simple_cond_expression_return simple_cond_expression132 = null;

        JPAParser.conditional_expression_return conditional_expression134 = null;


        Object string_literal131_tree=null;
        Object char_literal133_tree=null;
        Object char_literal135_tree=null;
        RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
        RewriteRuleSubtreeStream stream_simple_cond_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_cond_expression");
        try {
            // JPA.g:219:2: ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' )
            int alt47=2;
            alt47 = dfa47.predict(input);
            switch (alt47) {
                case 1 :
                    // JPA.g:219:4: ( 'NOT' )? simple_cond_expression
                    {
                    // JPA.g:219:4: ( 'NOT' )?
                    int alt46=2;
                    int LA46_0 = input.LA(1);

                    if ( (LA46_0==61) ) {
                        int LA46_1 = input.LA(2);

                        if ( (synpred57_JPA()) ) {
                            alt46=1;
                        }
                    }
                    switch (alt46) {
                        case 1 :
                            // JPA.g:219:5: 'NOT'
                            {
                            string_literal131=(Token)match(input,61,FOLLOW_61_in_conditional_factor1539); if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_61.add(string_literal131);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_simple_cond_expression_in_conditional_factor1543);
                    simple_cond_expression132=simple_cond_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_simple_cond_expression.add(simple_cond_expression132.getTree());


                    // AST REWRITE
                    // elements: simple_cond_expression, 61
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 219:36: -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression )
                    {
                        // JPA.g:219:39: ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new SimpleConditionNode(T_SIMPLE_CONDITION), root_1);

                        // JPA.g:219:83: ( 'NOT' )?
                        if ( stream_61.hasNext() ) {
                            adaptor.addChild(root_1, stream_61.nextNode());

                        }
                        stream_61.reset();
                        adaptor.addChild(root_1, stream_simple_cond_expression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // JPA.g:220:4: '(' conditional_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal133=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_factor1565); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal133_tree = (Object)adaptor.create(char_literal133);
                    adaptor.addChild(root_0, char_literal133_tree);
                    }
                    pushFollow(FOLLOW_conditional_expression_in_conditional_factor1566);
                    conditional_expression134=conditional_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression134.getTree());
                    char_literal135=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_factor1567); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal135_tree = (Object)adaptor.create(char_literal135);
                    adaptor.addChild(root_0, char_literal135_tree);
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
    // JPA.g:222:1: simple_cond_expression : ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
    public final JPAParser.simple_cond_expression_return simple_cond_expression() throws RecognitionException {
        JPAParser.simple_cond_expression_return retval = new JPAParser.simple_cond_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.comparison_expression_return comparison_expression136 = null;

        JPAParser.between_expression_return between_expression137 = null;

        JPAParser.like_expression_return like_expression138 = null;

        JPAParser.in_expression_return in_expression139 = null;

        JPAParser.null_comparison_expression_return null_comparison_expression140 = null;

        JPAParser.empty_collection_comparison_expression_return empty_collection_comparison_expression141 = null;

        JPAParser.collection_member_expression_return collection_member_expression142 = null;

        JPAParser.exists_expression_return exists_expression143 = null;

        JPAParser.date_macro_expression_return date_macro_expression144 = null;



        try {
            // JPA.g:223:2: ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
            int alt48=9;
            alt48 = dfa48.predict(input);
            switch (alt48) {
                case 1 :
                    // JPA.g:223:4: comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression1576);
                    comparison_expression136=comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression136.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:224:4: between_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_between_expression_in_simple_cond_expression1581);
                    between_expression137=between_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression137.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:225:4: like_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_like_expression_in_simple_cond_expression1586);
                    like_expression138=like_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression138.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:226:4: in_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_in_expression_in_simple_cond_expression1591);
                    in_expression139=in_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression139.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:227:4: null_comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression1596);
                    null_comparison_expression140=null_comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression140.getTree());

                    }
                    break;
                case 6 :
                    // JPA.g:228:4: empty_collection_comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression1601);
                    empty_collection_comparison_expression141=empty_collection_comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression141.getTree());

                    }
                    break;
                case 7 :
                    // JPA.g:229:4: collection_member_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression1606);
                    collection_member_expression142=collection_member_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression142.getTree());

                    }
                    break;
                case 8 :
                    // JPA.g:230:4: exists_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_exists_expression_in_simple_cond_expression1611);
                    exists_expression143=exists_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression143.getTree());

                    }
                    break;
                case 9 :
                    // JPA.g:231:4: date_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression1616);
                    date_macro_expression144=date_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_macro_expression144.getTree());

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
    // JPA.g:233:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
    public final JPAParser.date_macro_expression_return date_macro_expression() throws RecognitionException {
        JPAParser.date_macro_expression_return retval = new JPAParser.date_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.date_between_macro_expression_return date_between_macro_expression145 = null;

        JPAParser.date_before_macro_expression_return date_before_macro_expression146 = null;

        JPAParser.date_after_macro_expression_return date_after_macro_expression147 = null;

        JPAParser.date_equals_macro_expression_return date_equals_macro_expression148 = null;

        JPAParser.date_today_macro_expression_return date_today_macro_expression149 = null;



        try {
            // JPA.g:234:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
            int alt49=5;
            switch ( input.LA(1) ) {
            case 62:
                {
                alt49=1;
                }
                break;
            case 72:
                {
                alt49=2;
                }
                break;
            case 73:
                {
                alt49=3;
                }
                break;
            case 74:
                {
                alt49=4;
                }
                break;
            case 75:
                {
                alt49=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;
            }

            switch (alt49) {
                case 1 :
                    // JPA.g:234:7: date_between_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression1628);
                    date_between_macro_expression145=date_between_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression145.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:235:7: date_before_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression1636);
                    date_before_macro_expression146=date_before_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression146.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:236:7: date_after_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression1644);
                    date_after_macro_expression147=date_after_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression147.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:237:7: date_equals_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression1652);
                    date_equals_macro_expression148=date_equals_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression148.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:238:7: date_today_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression1660);
                    date_today_macro_expression149=date_today_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_today_macro_expression149.getTree());

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
    // JPA.g:240:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
    public final JPAParser.date_between_macro_expression_return date_between_macro_expression() throws RecognitionException {
        JPAParser.date_between_macro_expression_return retval = new JPAParser.date_between_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal150=null;
        Token char_literal151=null;
        Token char_literal153=null;
        Token string_literal154=null;
        Token set155=null;
        Token INT_NUMERAL156=null;
        Token char_literal157=null;
        Token string_literal158=null;
        Token set159=null;
        Token INT_NUMERAL160=null;
        Token char_literal161=null;
        Token set162=null;
        Token char_literal163=null;
        JPAParser.path_expression_return path_expression152 = null;


        Object string_literal150_tree=null;
        Object char_literal151_tree=null;
        Object char_literal153_tree=null;
        Object string_literal154_tree=null;
        Object set155_tree=null;
        Object INT_NUMERAL156_tree=null;
        Object char_literal157_tree=null;
        Object string_literal158_tree=null;
        Object set159_tree=null;
        Object INT_NUMERAL160_tree=null;
        Object char_literal161_tree=null;
        Object set162_tree=null;
        Object char_literal163_tree=null;

        try {
            // JPA.g:241:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
            // JPA.g:241:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal150=(Token)match(input,62,FOLLOW_62_in_date_between_macro_expression1672); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal150_tree = (Object)adaptor.create(string_literal150);
            adaptor.addChild(root_0, string_literal150_tree);
            }
            char_literal151=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression1674); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal151_tree = (Object)adaptor.create(char_literal151);
            adaptor.addChild(root_0, char_literal151_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_between_macro_expression1676);
            path_expression152=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression152.getTree());
            char_literal153=(Token)match(input,50,FOLLOW_50_in_date_between_macro_expression1678); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal153_tree = (Object)adaptor.create(char_literal153);
            adaptor.addChild(root_0, char_literal153_tree);
            }
            string_literal154=(Token)match(input,63,FOLLOW_63_in_date_between_macro_expression1680); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal154_tree = (Object)adaptor.create(string_literal154);
            adaptor.addChild(root_0, string_literal154_tree);
            }
            // JPA.g:241:48: ( ( '+' | '-' ) INT_NUMERAL )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( ((LA50_0>=64 && LA50_0<=65)) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // JPA.g:241:49: ( '+' | '-' ) INT_NUMERAL
                    {
                    set155=(Token)input.LT(1);
                    if ( (input.LA(1)>=64 && input.LA(1)<=65) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set155));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    INT_NUMERAL156=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_date_between_macro_expression1691); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_NUMERAL156_tree = (Object)adaptor.create(INT_NUMERAL156);
                    adaptor.addChild(root_0, INT_NUMERAL156_tree);
                    }

                    }
                    break;

            }

            char_literal157=(Token)match(input,50,FOLLOW_50_in_date_between_macro_expression1695); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal157_tree = (Object)adaptor.create(char_literal157);
            adaptor.addChild(root_0, char_literal157_tree);
            }
            string_literal158=(Token)match(input,63,FOLLOW_63_in_date_between_macro_expression1697); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal158_tree = (Object)adaptor.create(string_literal158);
            adaptor.addChild(root_0, string_literal158_tree);
            }
            // JPA.g:241:85: ( ( '+' | '-' ) INT_NUMERAL )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( ((LA51_0>=64 && LA51_0<=65)) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // JPA.g:241:86: ( '+' | '-' ) INT_NUMERAL
                    {
                    set159=(Token)input.LT(1);
                    if ( (input.LA(1)>=64 && input.LA(1)<=65) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set159));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    INT_NUMERAL160=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_date_between_macro_expression1708); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_NUMERAL160_tree = (Object)adaptor.create(INT_NUMERAL160);
                    adaptor.addChild(root_0, INT_NUMERAL160_tree);
                    }

                    }
                    break;

            }

            char_literal161=(Token)match(input,50,FOLLOW_50_in_date_between_macro_expression1712); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal161_tree = (Object)adaptor.create(char_literal161);
            adaptor.addChild(root_0, char_literal161_tree);
            }
            set162=(Token)input.LT(1);
            if ( (input.LA(1)>=66 && input.LA(1)<=71) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set162));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            char_literal163=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression1737); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal163_tree = (Object)adaptor.create(char_literal163);
            adaptor.addChild(root_0, char_literal163_tree);
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
    // JPA.g:243:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
    public final JPAParser.date_before_macro_expression_return date_before_macro_expression() throws RecognitionException {
        JPAParser.date_before_macro_expression_return retval = new JPAParser.date_before_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal164=null;
        Token char_literal165=null;
        Token char_literal167=null;
        Token char_literal170=null;
        JPAParser.path_expression_return path_expression166 = null;

        JPAParser.path_expression_return path_expression168 = null;

        JPAParser.input_parameter_return input_parameter169 = null;


        Object string_literal164_tree=null;
        Object char_literal165_tree=null;
        Object char_literal167_tree=null;
        Object char_literal170_tree=null;

        try {
            // JPA.g:244:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:244:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal164=(Token)match(input,72,FOLLOW_72_in_date_before_macro_expression1749); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal164_tree = (Object)adaptor.create(string_literal164);
            adaptor.addChild(root_0, string_literal164_tree);
            }
            char_literal165=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression1751); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal165_tree = (Object)adaptor.create(char_literal165);
            adaptor.addChild(root_0, char_literal165_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_before_macro_expression1753);
            path_expression166=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression166.getTree());
            char_literal167=(Token)match(input,50,FOLLOW_50_in_date_before_macro_expression1755); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal167_tree = (Object)adaptor.create(char_literal167);
            adaptor.addChild(root_0, char_literal167_tree);
            }
            // JPA.g:244:45: ( path_expression | input_parameter )
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==WORD) ) {
                alt52=1;
            }
            else if ( (LA52_0==NAMED_PARAMETER||LA52_0==114) ) {
                alt52=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 52, 0, input);

                throw nvae;
            }
            switch (alt52) {
                case 1 :
                    // JPA.g:244:46: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_before_macro_expression1758);
                    path_expression168=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression168.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:244:64: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression1762);
                    input_parameter169=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter169.getTree());

                    }
                    break;

            }

            char_literal170=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression1765); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal170_tree = (Object)adaptor.create(char_literal170);
            adaptor.addChild(root_0, char_literal170_tree);
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
    // JPA.g:246:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
    public final JPAParser.date_after_macro_expression_return date_after_macro_expression() throws RecognitionException {
        JPAParser.date_after_macro_expression_return retval = new JPAParser.date_after_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal171=null;
        Token char_literal172=null;
        Token char_literal174=null;
        Token char_literal177=null;
        JPAParser.path_expression_return path_expression173 = null;

        JPAParser.path_expression_return path_expression175 = null;

        JPAParser.input_parameter_return input_parameter176 = null;


        Object string_literal171_tree=null;
        Object char_literal172_tree=null;
        Object char_literal174_tree=null;
        Object char_literal177_tree=null;

        try {
            // JPA.g:247:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:247:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal171=(Token)match(input,73,FOLLOW_73_in_date_after_macro_expression1777); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal171_tree = (Object)adaptor.create(string_literal171);
            adaptor.addChild(root_0, string_literal171_tree);
            }
            char_literal172=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression1779); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal172_tree = (Object)adaptor.create(char_literal172);
            adaptor.addChild(root_0, char_literal172_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_after_macro_expression1781);
            path_expression173=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression173.getTree());
            char_literal174=(Token)match(input,50,FOLLOW_50_in_date_after_macro_expression1783); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal174_tree = (Object)adaptor.create(char_literal174);
            adaptor.addChild(root_0, char_literal174_tree);
            }
            // JPA.g:247:44: ( path_expression | input_parameter )
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==WORD) ) {
                alt53=1;
            }
            else if ( (LA53_0==NAMED_PARAMETER||LA53_0==114) ) {
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
                    // JPA.g:247:45: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_after_macro_expression1786);
                    path_expression175=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression175.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:247:63: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression1790);
                    input_parameter176=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter176.getTree());

                    }
                    break;

            }

            char_literal177=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression1793); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal177_tree = (Object)adaptor.create(char_literal177);
            adaptor.addChild(root_0, char_literal177_tree);
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
    // JPA.g:249:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
    public final JPAParser.date_equals_macro_expression_return date_equals_macro_expression() throws RecognitionException {
        JPAParser.date_equals_macro_expression_return retval = new JPAParser.date_equals_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal178=null;
        Token char_literal179=null;
        Token char_literal181=null;
        Token char_literal184=null;
        JPAParser.path_expression_return path_expression180 = null;

        JPAParser.path_expression_return path_expression182 = null;

        JPAParser.input_parameter_return input_parameter183 = null;


        Object string_literal178_tree=null;
        Object char_literal179_tree=null;
        Object char_literal181_tree=null;
        Object char_literal184_tree=null;

        try {
            // JPA.g:250:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:250:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal178=(Token)match(input,74,FOLLOW_74_in_date_equals_macro_expression1805); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal178_tree = (Object)adaptor.create(string_literal178);
            adaptor.addChild(root_0, string_literal178_tree);
            }
            char_literal179=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression1807); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal179_tree = (Object)adaptor.create(char_literal179);
            adaptor.addChild(root_0, char_literal179_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression1809);
            path_expression180=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression180.getTree());
            char_literal181=(Token)match(input,50,FOLLOW_50_in_date_equals_macro_expression1811); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal181_tree = (Object)adaptor.create(char_literal181);
            adaptor.addChild(root_0, char_literal181_tree);
            }
            // JPA.g:250:45: ( path_expression | input_parameter )
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==WORD) ) {
                alt54=1;
            }
            else if ( (LA54_0==NAMED_PARAMETER||LA54_0==114) ) {
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
                    // JPA.g:250:46: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression1814);
                    path_expression182=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression182.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:250:64: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression1818);
                    input_parameter183=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter183.getTree());

                    }
                    break;

            }

            char_literal184=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression1821); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal184_tree = (Object)adaptor.create(char_literal184);
            adaptor.addChild(root_0, char_literal184_tree);
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
    // JPA.g:252:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
    public final JPAParser.date_today_macro_expression_return date_today_macro_expression() throws RecognitionException {
        JPAParser.date_today_macro_expression_return retval = new JPAParser.date_today_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal185=null;
        Token char_literal186=null;
        Token char_literal188=null;
        JPAParser.path_expression_return path_expression187 = null;


        Object string_literal185_tree=null;
        Object char_literal186_tree=null;
        Object char_literal188_tree=null;

        try {
            // JPA.g:253:5: ( '@TODAY' '(' path_expression ')' )
            // JPA.g:253:7: '@TODAY' '(' path_expression ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal185=(Token)match(input,75,FOLLOW_75_in_date_today_macro_expression1833); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal185_tree = (Object)adaptor.create(string_literal185);
            adaptor.addChild(root_0, string_literal185_tree);
            }
            char_literal186=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression1835); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal186_tree = (Object)adaptor.create(char_literal186);
            adaptor.addChild(root_0, char_literal186_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_today_macro_expression1837);
            path_expression187=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression187.getTree());
            char_literal188=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression1839); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal188_tree = (Object)adaptor.create(char_literal188);
            adaptor.addChild(root_0, char_literal188_tree);
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
    // JPA.g:255:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
    public final JPAParser.between_expression_return between_expression() throws RecognitionException {
        JPAParser.between_expression_return retval = new JPAParser.between_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal190=null;
        Token string_literal191=null;
        Token string_literal193=null;
        Token string_literal196=null;
        Token string_literal197=null;
        Token string_literal199=null;
        Token string_literal202=null;
        Token string_literal203=null;
        Token string_literal205=null;
        JPAParser.arithmetic_expression_return arithmetic_expression189 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression192 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression194 = null;

        JPAParser.string_expression_return string_expression195 = null;

        JPAParser.string_expression_return string_expression198 = null;

        JPAParser.string_expression_return string_expression200 = null;

        JPAParser.datetime_expression_return datetime_expression201 = null;

        JPAParser.datetime_expression_return datetime_expression204 = null;

        JPAParser.datetime_expression_return datetime_expression206 = null;


        Object string_literal190_tree=null;
        Object string_literal191_tree=null;
        Object string_literal193_tree=null;
        Object string_literal196_tree=null;
        Object string_literal197_tree=null;
        Object string_literal199_tree=null;
        Object string_literal202_tree=null;
        Object string_literal203_tree=null;
        Object string_literal205_tree=null;

        try {
            // JPA.g:256:2: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
            int alt58=3;
            alt58 = dfa58.predict(input);
            switch (alt58) {
                case 1 :
                    // JPA.g:256:4: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1848);
                    arithmetic_expression189=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression189.getTree());
                    // JPA.g:256:26: ( 'NOT' )?
                    int alt55=2;
                    int LA55_0 = input.LA(1);

                    if ( (LA55_0==61) ) {
                        alt55=1;
                    }
                    switch (alt55) {
                        case 1 :
                            // JPA.g:256:27: 'NOT'
                            {
                            string_literal190=(Token)match(input,61,FOLLOW_61_in_between_expression1851); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal190_tree = (Object)adaptor.create(string_literal190);
                            adaptor.addChild(root_0, string_literal190_tree);
                            }

                            }
                            break;

                    }

                    string_literal191=(Token)match(input,76,FOLLOW_76_in_between_expression1855); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal191_tree = (Object)adaptor.create(string_literal191);
                    adaptor.addChild(root_0, string_literal191_tree);
                    }
                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1857);
                    arithmetic_expression192=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression192.getTree());
                    string_literal193=(Token)match(input,AND,FOLLOW_AND_in_between_expression1859); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal193_tree = (Object)adaptor.create(string_literal193);
                    adaptor.addChild(root_0, string_literal193_tree);
                    }
                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1861);
                    arithmetic_expression194=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression194.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:257:4: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_expression_in_between_expression1866);
                    string_expression195=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression195.getTree());
                    // JPA.g:257:22: ( 'NOT' )?
                    int alt56=2;
                    int LA56_0 = input.LA(1);

                    if ( (LA56_0==61) ) {
                        alt56=1;
                    }
                    switch (alt56) {
                        case 1 :
                            // JPA.g:257:23: 'NOT'
                            {
                            string_literal196=(Token)match(input,61,FOLLOW_61_in_between_expression1869); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal196_tree = (Object)adaptor.create(string_literal196);
                            adaptor.addChild(root_0, string_literal196_tree);
                            }

                            }
                            break;

                    }

                    string_literal197=(Token)match(input,76,FOLLOW_76_in_between_expression1873); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal197_tree = (Object)adaptor.create(string_literal197);
                    adaptor.addChild(root_0, string_literal197_tree);
                    }
                    pushFollow(FOLLOW_string_expression_in_between_expression1875);
                    string_expression198=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression198.getTree());
                    string_literal199=(Token)match(input,AND,FOLLOW_AND_in_between_expression1877); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal199_tree = (Object)adaptor.create(string_literal199);
                    adaptor.addChild(root_0, string_literal199_tree);
                    }
                    pushFollow(FOLLOW_string_expression_in_between_expression1879);
                    string_expression200=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression200.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:258:4: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_expression_in_between_expression1884);
                    datetime_expression201=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression201.getTree());
                    // JPA.g:258:24: ( 'NOT' )?
                    int alt57=2;
                    int LA57_0 = input.LA(1);

                    if ( (LA57_0==61) ) {
                        alt57=1;
                    }
                    switch (alt57) {
                        case 1 :
                            // JPA.g:258:25: 'NOT'
                            {
                            string_literal202=(Token)match(input,61,FOLLOW_61_in_between_expression1887); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal202_tree = (Object)adaptor.create(string_literal202);
                            adaptor.addChild(root_0, string_literal202_tree);
                            }

                            }
                            break;

                    }

                    string_literal203=(Token)match(input,76,FOLLOW_76_in_between_expression1891); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal203_tree = (Object)adaptor.create(string_literal203);
                    adaptor.addChild(root_0, string_literal203_tree);
                    }
                    pushFollow(FOLLOW_datetime_expression_in_between_expression1893);
                    datetime_expression204=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression204.getTree());
                    string_literal205=(Token)match(input,AND,FOLLOW_AND_in_between_expression1895); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal205_tree = (Object)adaptor.create(string_literal205);
                    adaptor.addChild(root_0, string_literal205_tree);
                    }
                    pushFollow(FOLLOW_datetime_expression_in_between_expression1897);
                    datetime_expression206=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression206.getTree());

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
    // JPA.g:260:1: in_expression : path_expression ( 'NOT' )? 'IN' in_expression_right_part ;
    public final JPAParser.in_expression_return in_expression() throws RecognitionException {
        JPAParser.in_expression_return retval = new JPAParser.in_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal208=null;
        Token string_literal209=null;
        JPAParser.path_expression_return path_expression207 = null;

        JPAParser.in_expression_right_part_return in_expression_right_part210 = null;


        Object string_literal208_tree=null;
        Object string_literal209_tree=null;

        try {
            // JPA.g:261:2: ( path_expression ( 'NOT' )? 'IN' in_expression_right_part )
            // JPA.g:261:4: path_expression ( 'NOT' )? 'IN' in_expression_right_part
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_in_expression1906);
            path_expression207=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression207.getTree());
            // JPA.g:261:20: ( 'NOT' )?
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==61) ) {
                alt59=1;
            }
            switch (alt59) {
                case 1 :
                    // JPA.g:261:21: 'NOT'
                    {
                    string_literal208=(Token)match(input,61,FOLLOW_61_in_in_expression1909); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal208_tree = (Object)adaptor.create(string_literal208);
                    adaptor.addChild(root_0, string_literal208_tree);
                    }

                    }
                    break;

            }

            string_literal209=(Token)match(input,54,FOLLOW_54_in_in_expression1913); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal209_tree = (Object)adaptor.create(string_literal209);
            adaptor.addChild(root_0, string_literal209_tree);
            }
            pushFollow(FOLLOW_in_expression_right_part_in_in_expression1915);
            in_expression_right_part210=in_expression_right_part();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression_right_part210.getTree());

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
    // JPA.g:263:1: in_expression_right_part : ( '(' in_item ( ',' in_item )* ')' | subquery );
    public final JPAParser.in_expression_right_part_return in_expression_right_part() throws RecognitionException {
        JPAParser.in_expression_right_part_return retval = new JPAParser.in_expression_right_part_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal211=null;
        Token char_literal213=null;
        Token char_literal215=null;
        JPAParser.in_item_return in_item212 = null;

        JPAParser.in_item_return in_item214 = null;

        JPAParser.subquery_return subquery216 = null;


        Object char_literal211_tree=null;
        Object char_literal213_tree=null;
        Object char_literal215_tree=null;

        try {
            // JPA.g:264:2: ( '(' in_item ( ',' in_item )* ')' | subquery )
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==LPAREN) ) {
                alt61=1;
            }
            else if ( (LA61_0==52) ) {
                alt61=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 61, 0, input);

                throw nvae;
            }
            switch (alt61) {
                case 1 :
                    // JPA.g:264:4: '(' in_item ( ',' in_item )* ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal211=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression_right_part1924); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal211_tree = (Object)adaptor.create(char_literal211);
                    adaptor.addChild(root_0, char_literal211_tree);
                    }
                    pushFollow(FOLLOW_in_item_in_in_expression_right_part1926);
                    in_item212=in_item();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item212.getTree());
                    // JPA.g:264:16: ( ',' in_item )*
                    loop60:
                    do {
                        int alt60=2;
                        int LA60_0 = input.LA(1);

                        if ( (LA60_0==50) ) {
                            alt60=1;
                        }


                        switch (alt60) {
                    	case 1 :
                    	    // JPA.g:264:17: ',' in_item
                    	    {
                    	    char_literal213=(Token)match(input,50,FOLLOW_50_in_in_expression_right_part1929); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal213_tree = (Object)adaptor.create(char_literal213);
                    	    adaptor.addChild(root_0, char_literal213_tree);
                    	    }
                    	    pushFollow(FOLLOW_in_item_in_in_expression_right_part1931);
                    	    in_item214=in_item();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item214.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop60;
                        }
                    } while (true);

                    char_literal215=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression_right_part1935); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal215_tree = (Object)adaptor.create(char_literal215);
                    adaptor.addChild(root_0, char_literal215_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:265:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_in_expression_right_part1940);
                    subquery216=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery216.getTree());

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
    // JPA.g:267:1: in_item : ( literal | input_parameter );
    public final JPAParser.in_item_return in_item() throws RecognitionException {
        JPAParser.in_item_return retval = new JPAParser.in_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.literal_return literal217 = null;

        JPAParser.input_parameter_return input_parameter218 = null;



        try {
            // JPA.g:268:2: ( literal | input_parameter )
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==WORD) ) {
                alt62=1;
            }
            else if ( (LA62_0==NAMED_PARAMETER||LA62_0==114) ) {
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
                    // JPA.g:268:4: literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_literal_in_in_item1949);
                    literal217=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal217.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:269:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_in_item1954);
                    input_parameter218=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter218.getTree());

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
    // JPA.g:271:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' ESCAPE_CHARACTER )? ;
    public final JPAParser.like_expression_return like_expression() throws RecognitionException {
        JPAParser.like_expression_return retval = new JPAParser.like_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal220=null;
        Token string_literal221=null;
        Token string_literal224=null;
        Token ESCAPE_CHARACTER225=null;
        JPAParser.string_expression_return string_expression219 = null;

        JPAParser.pattern_value_return pattern_value222 = null;

        JPAParser.input_parameter_return input_parameter223 = null;


        Object string_literal220_tree=null;
        Object string_literal221_tree=null;
        Object string_literal224_tree=null;
        Object ESCAPE_CHARACTER225_tree=null;

        try {
            // JPA.g:272:2: ( string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' ESCAPE_CHARACTER )? )
            // JPA.g:272:4: string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' ESCAPE_CHARACTER )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_string_expression_in_like_expression1963);
            string_expression219=string_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression219.getTree());
            // JPA.g:272:22: ( 'NOT' )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==61) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // JPA.g:272:23: 'NOT'
                    {
                    string_literal220=(Token)match(input,61,FOLLOW_61_in_like_expression1966); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal220_tree = (Object)adaptor.create(string_literal220);
                    adaptor.addChild(root_0, string_literal220_tree);
                    }

                    }
                    break;

            }

            string_literal221=(Token)match(input,77,FOLLOW_77_in_like_expression1970); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal221_tree = (Object)adaptor.create(string_literal221);
            adaptor.addChild(root_0, string_literal221_tree);
            }
            // JPA.g:272:38: ( pattern_value | input_parameter )
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==WORD) ) {
                alt64=1;
            }
            else if ( (LA64_0==NAMED_PARAMETER||LA64_0==114) ) {
                alt64=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 64, 0, input);

                throw nvae;
            }
            switch (alt64) {
                case 1 :
                    // JPA.g:272:39: pattern_value
                    {
                    pushFollow(FOLLOW_pattern_value_in_like_expression1973);
                    pattern_value222=pattern_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value222.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:272:55: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_like_expression1977);
                    input_parameter223=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter223.getTree());

                    }
                    break;

            }

            // JPA.g:272:71: ( 'ESCAPE' ESCAPE_CHARACTER )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==78) ) {
                alt65=1;
            }
            switch (alt65) {
                case 1 :
                    // JPA.g:272:72: 'ESCAPE' ESCAPE_CHARACTER
                    {
                    string_literal224=(Token)match(input,78,FOLLOW_78_in_like_expression1980); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal224_tree = (Object)adaptor.create(string_literal224);
                    adaptor.addChild(root_0, string_literal224_tree);
                    }
                    ESCAPE_CHARACTER225=(Token)match(input,ESCAPE_CHARACTER,FOLLOW_ESCAPE_CHARACTER_in_like_expression1982); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ESCAPE_CHARACTER225_tree = (Object)adaptor.create(ESCAPE_CHARACTER225);
                    adaptor.addChild(root_0, ESCAPE_CHARACTER225_tree);
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
    // JPA.g:274:1: null_comparison_expression : ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' ;
    public final JPAParser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
        JPAParser.null_comparison_expression_return retval = new JPAParser.null_comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal228=null;
        Token string_literal229=null;
        Token string_literal230=null;
        JPAParser.path_expression_return path_expression226 = null;

        JPAParser.input_parameter_return input_parameter227 = null;


        Object string_literal228_tree=null;
        Object string_literal229_tree=null;
        Object string_literal230_tree=null;

        try {
            // JPA.g:275:2: ( ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' )
            // JPA.g:275:4: ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL'
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:275:4: ( path_expression | input_parameter )
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==WORD) ) {
                alt66=1;
            }
            else if ( (LA66_0==NAMED_PARAMETER||LA66_0==114) ) {
                alt66=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 66, 0, input);

                throw nvae;
            }
            switch (alt66) {
                case 1 :
                    // JPA.g:275:5: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_null_comparison_expression1994);
                    path_expression226=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression226.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:275:23: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_null_comparison_expression1998);
                    input_parameter227=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter227.getTree());

                    }
                    break;

            }

            string_literal228=(Token)match(input,79,FOLLOW_79_in_null_comparison_expression2001); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal228_tree = (Object)adaptor.create(string_literal228);
            adaptor.addChild(root_0, string_literal228_tree);
            }
            // JPA.g:275:45: ( 'NOT' )?
            int alt67=2;
            int LA67_0 = input.LA(1);

            if ( (LA67_0==61) ) {
                alt67=1;
            }
            switch (alt67) {
                case 1 :
                    // JPA.g:275:46: 'NOT'
                    {
                    string_literal229=(Token)match(input,61,FOLLOW_61_in_null_comparison_expression2004); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal229_tree = (Object)adaptor.create(string_literal229);
                    adaptor.addChild(root_0, string_literal229_tree);
                    }

                    }
                    break;

            }

            string_literal230=(Token)match(input,80,FOLLOW_80_in_null_comparison_expression2008); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal230_tree = (Object)adaptor.create(string_literal230);
            adaptor.addChild(root_0, string_literal230_tree);
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
    // JPA.g:277:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
    public final JPAParser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
        JPAParser.empty_collection_comparison_expression_return retval = new JPAParser.empty_collection_comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal232=null;
        Token string_literal233=null;
        Token string_literal234=null;
        JPAParser.path_expression_return path_expression231 = null;


        Object string_literal232_tree=null;
        Object string_literal233_tree=null;
        Object string_literal234_tree=null;

        try {
            // JPA.g:278:2: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
            // JPA.g:278:4: path_expression 'IS' ( 'NOT' )? 'EMPTY'
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression2017);
            path_expression231=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression231.getTree());
            string_literal232=(Token)match(input,79,FOLLOW_79_in_empty_collection_comparison_expression2019); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal232_tree = (Object)adaptor.create(string_literal232);
            adaptor.addChild(root_0, string_literal232_tree);
            }
            // JPA.g:278:25: ( 'NOT' )?
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==61) ) {
                alt68=1;
            }
            switch (alt68) {
                case 1 :
                    // JPA.g:278:26: 'NOT'
                    {
                    string_literal233=(Token)match(input,61,FOLLOW_61_in_empty_collection_comparison_expression2022); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal233_tree = (Object)adaptor.create(string_literal233);
                    adaptor.addChild(root_0, string_literal233_tree);
                    }

                    }
                    break;

            }

            string_literal234=(Token)match(input,81,FOLLOW_81_in_empty_collection_comparison_expression2026); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal234_tree = (Object)adaptor.create(string_literal234);
            adaptor.addChild(root_0, string_literal234_tree);
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
    // JPA.g:280:1: collection_member_expression : entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
    public final JPAParser.collection_member_expression_return collection_member_expression() throws RecognitionException {
        JPAParser.collection_member_expression_return retval = new JPAParser.collection_member_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal236=null;
        Token string_literal237=null;
        Token string_literal238=null;
        JPAParser.entity_expression_return entity_expression235 = null;

        JPAParser.path_expression_return path_expression239 = null;


        Object string_literal236_tree=null;
        Object string_literal237_tree=null;
        Object string_literal238_tree=null;

        try {
            // JPA.g:281:2: ( entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
            // JPA.g:281:4: entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_entity_expression_in_collection_member_expression2035);
            entity_expression235=entity_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression235.getTree());
            // JPA.g:281:22: ( 'NOT' )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==61) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // JPA.g:281:23: 'NOT'
                    {
                    string_literal236=(Token)match(input,61,FOLLOW_61_in_collection_member_expression2038); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal236_tree = (Object)adaptor.create(string_literal236);
                    adaptor.addChild(root_0, string_literal236_tree);
                    }

                    }
                    break;

            }

            string_literal237=(Token)match(input,82,FOLLOW_82_in_collection_member_expression2042); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal237_tree = (Object)adaptor.create(string_literal237);
            adaptor.addChild(root_0, string_literal237_tree);
            }
            // JPA.g:281:40: ( 'OF' )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==83) ) {
                alt70=1;
            }
            switch (alt70) {
                case 1 :
                    // JPA.g:281:41: 'OF'
                    {
                    string_literal238=(Token)match(input,83,FOLLOW_83_in_collection_member_expression2045); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal238_tree = (Object)adaptor.create(string_literal238);
                    adaptor.addChild(root_0, string_literal238_tree);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_path_expression_in_collection_member_expression2049);
            path_expression239=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression239.getTree());

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
    // JPA.g:283:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
    public final JPAParser.exists_expression_return exists_expression() throws RecognitionException {
        JPAParser.exists_expression_return retval = new JPAParser.exists_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal240=null;
        Token string_literal241=null;
        JPAParser.subquery_return subquery242 = null;


        Object string_literal240_tree=null;
        Object string_literal241_tree=null;

        try {
            // JPA.g:284:2: ( ( 'NOT' )? 'EXISTS' subquery )
            // JPA.g:284:4: ( 'NOT' )? 'EXISTS' subquery
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:284:4: ( 'NOT' )?
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==61) ) {
                alt71=1;
            }
            switch (alt71) {
                case 1 :
                    // JPA.g:284:5: 'NOT'
                    {
                    string_literal240=(Token)match(input,61,FOLLOW_61_in_exists_expression2059); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal240_tree = (Object)adaptor.create(string_literal240);
                    adaptor.addChild(root_0, string_literal240_tree);
                    }

                    }
                    break;

            }

            string_literal241=(Token)match(input,84,FOLLOW_84_in_exists_expression2063); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal241_tree = (Object)adaptor.create(string_literal241);
            adaptor.addChild(root_0, string_literal241_tree);
            }
            pushFollow(FOLLOW_subquery_in_exists_expression2065);
            subquery242=subquery();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery242.getTree());

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
    // JPA.g:286:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
    public final JPAParser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
        JPAParser.all_or_any_expression_return retval = new JPAParser.all_or_any_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set243=null;
        JPAParser.subquery_return subquery244 = null;


        Object set243_tree=null;

        try {
            // JPA.g:287:2: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
            // JPA.g:287:4: ( 'ALL' | 'ANY' | 'SOME' ) subquery
            {
            root_0 = (Object)adaptor.nil();

            set243=(Token)input.LT(1);
            if ( (input.LA(1)>=85 && input.LA(1)<=87) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set243));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            pushFollow(FOLLOW_subquery_in_all_or_any_expression2087);
            subquery244=subquery();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery244.getTree());

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
    // JPA.g:289:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
    public final JPAParser.comparison_expression_return comparison_expression() throws RecognitionException {
        JPAParser.comparison_expression_return retval = new JPAParser.comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set250=null;
        Token set254=null;
        Token set262=null;
        JPAParser.string_expression_return string_expression245 = null;

        JPAParser.comparison_operator_return comparison_operator246 = null;

        JPAParser.string_expression_return string_expression247 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression248 = null;

        JPAParser.boolean_expression_return boolean_expression249 = null;

        JPAParser.boolean_expression_return boolean_expression251 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression252 = null;

        JPAParser.enum_expression_return enum_expression253 = null;

        JPAParser.enum_expression_return enum_expression255 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression256 = null;

        JPAParser.datetime_expression_return datetime_expression257 = null;

        JPAParser.comparison_operator_return comparison_operator258 = null;

        JPAParser.datetime_expression_return datetime_expression259 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression260 = null;

        JPAParser.entity_expression_return entity_expression261 = null;

        JPAParser.entity_expression_return entity_expression263 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression264 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression265 = null;

        JPAParser.comparison_operator_return comparison_operator266 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression267 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression268 = null;


        Object set250_tree=null;
        Object set254_tree=null;
        Object set262_tree=null;

        try {
            // JPA.g:290:2: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
            int alt78=6;
            alt78 = dfa78.predict(input);
            switch (alt78) {
                case 1 :
                    // JPA.g:290:4: string_expression comparison_operator ( string_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_expression_in_comparison_expression2096);
                    string_expression245=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression245.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2098);
                    comparison_operator246=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator246.getTree());
                    // JPA.g:290:42: ( string_expression | all_or_any_expression )
                    int alt72=2;
                    int LA72_0 = input.LA(1);

                    if ( ((LA72_0>=AVG && LA72_0<=COUNT)||LA72_0==STRINGLITERAL||(LA72_0>=WORD && LA72_0<=NAMED_PARAMETER)||LA72_0==52||(LA72_0>=105 && LA72_0<=109)||LA72_0==114) ) {
                        alt72=1;
                    }
                    else if ( ((LA72_0>=85 && LA72_0<=87)) ) {
                        alt72=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 72, 0, input);

                        throw nvae;
                    }
                    switch (alt72) {
                        case 1 :
                            // JPA.g:290:43: string_expression
                            {
                            pushFollow(FOLLOW_string_expression_in_comparison_expression2101);
                            string_expression247=string_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression247.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:290:63: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2105);
                            all_or_any_expression248=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression248.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // JPA.g:291:4: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_expression_in_comparison_expression2111);
                    boolean_expression249=boolean_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression249.getTree());
                    set250=(Token)input.LT(1);
                    if ( (input.LA(1)>=88 && input.LA(1)<=89) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set250));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:291:36: ( boolean_expression | all_or_any_expression )
                    int alt73=2;
                    int LA73_0 = input.LA(1);

                    if ( ((LA73_0>=WORD && LA73_0<=NAMED_PARAMETER)||LA73_0==52||(LA73_0>=114 && LA73_0<=116)) ) {
                        alt73=1;
                    }
                    else if ( ((LA73_0>=85 && LA73_0<=87)) ) {
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
                            // JPA.g:291:37: boolean_expression
                            {
                            pushFollow(FOLLOW_boolean_expression_in_comparison_expression2122);
                            boolean_expression251=boolean_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression251.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:291:58: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2126);
                            all_or_any_expression252=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression252.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // JPA.g:292:4: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_expression_in_comparison_expression2132);
                    enum_expression253=enum_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression253.getTree());
                    set254=(Token)input.LT(1);
                    if ( (input.LA(1)>=88 && input.LA(1)<=89) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set254));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:292:31: ( enum_expression | all_or_any_expression )
                    int alt74=2;
                    int LA74_0 = input.LA(1);

                    if ( ((LA74_0>=WORD && LA74_0<=NAMED_PARAMETER)||LA74_0==52||LA74_0==114) ) {
                        alt74=1;
                    }
                    else if ( ((LA74_0>=85 && LA74_0<=87)) ) {
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
                            // JPA.g:292:32: enum_expression
                            {
                            pushFollow(FOLLOW_enum_expression_in_comparison_expression2141);
                            enum_expression255=enum_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression255.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:292:50: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2145);
                            all_or_any_expression256=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression256.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // JPA.g:293:4: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_expression_in_comparison_expression2151);
                    datetime_expression257=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression257.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2153);
                    comparison_operator258=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator258.getTree());
                    // JPA.g:293:44: ( datetime_expression | all_or_any_expression )
                    int alt75=2;
                    int LA75_0 = input.LA(1);

                    if ( ((LA75_0>=AVG && LA75_0<=COUNT)||(LA75_0>=WORD && LA75_0<=NAMED_PARAMETER)||LA75_0==52||(LA75_0>=102 && LA75_0<=104)||LA75_0==114) ) {
                        alt75=1;
                    }
                    else if ( ((LA75_0>=85 && LA75_0<=87)) ) {
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
                            // JPA.g:293:45: datetime_expression
                            {
                            pushFollow(FOLLOW_datetime_expression_in_comparison_expression2156);
                            datetime_expression259=datetime_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression259.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:293:67: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2160);
                            all_or_any_expression260=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression260.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // JPA.g:294:4: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_entity_expression_in_comparison_expression2166);
                    entity_expression261=entity_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression261.getTree());
                    set262=(Token)input.LT(1);
                    if ( (input.LA(1)>=88 && input.LA(1)<=89) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set262));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:294:35: ( entity_expression | all_or_any_expression )
                    int alt76=2;
                    int LA76_0 = input.LA(1);

                    if ( ((LA76_0>=WORD && LA76_0<=NAMED_PARAMETER)||LA76_0==114) ) {
                        alt76=1;
                    }
                    else if ( ((LA76_0>=85 && LA76_0<=87)) ) {
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
                            // JPA.g:294:36: entity_expression
                            {
                            pushFollow(FOLLOW_entity_expression_in_comparison_expression2177);
                            entity_expression263=entity_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression263.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:294:56: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2181);
                            all_or_any_expression264=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression264.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 6 :
                    // JPA.g:295:4: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression2187);
                    arithmetic_expression265=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression265.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2189);
                    comparison_operator266=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator266.getTree());
                    // JPA.g:295:46: ( arithmetic_expression | all_or_any_expression )
                    int alt77=2;
                    int LA77_0 = input.LA(1);

                    if ( ((LA77_0>=AVG && LA77_0<=COUNT)||LA77_0==LPAREN||LA77_0==INT_NUMERAL||(LA77_0>=WORD && LA77_0<=NAMED_PARAMETER)||LA77_0==52||(LA77_0>=64 && LA77_0<=65)||(LA77_0>=96 && LA77_0<=101)||(LA77_0>=113 && LA77_0<=114)) ) {
                        alt77=1;
                    }
                    else if ( ((LA77_0>=85 && LA77_0<=87)) ) {
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
                            // JPA.g:295:47: arithmetic_expression
                            {
                            pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression2192);
                            arithmetic_expression267=arithmetic_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression267.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:295:71: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2196);
                            all_or_any_expression268=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression268.getTree());

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
    // JPA.g:297:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
    public final JPAParser.comparison_operator_return comparison_operator() throws RecognitionException {
        JPAParser.comparison_operator_return retval = new JPAParser.comparison_operator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set269=null;

        Object set269_tree=null;

        try {
            // JPA.g:298:2: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set269=(Token)input.LT(1);
            if ( (input.LA(1)>=88 && input.LA(1)<=93) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set269));
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
    // JPA.g:305:1: arithmetic_expression : ( simple_arithmetic_expression | subquery );
    public final JPAParser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
        JPAParser.arithmetic_expression_return retval = new JPAParser.arithmetic_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression270 = null;

        JPAParser.subquery_return subquery271 = null;



        try {
            // JPA.g:306:2: ( simple_arithmetic_expression | subquery )
            int alt79=2;
            int LA79_0 = input.LA(1);

            if ( ((LA79_0>=AVG && LA79_0<=COUNT)||LA79_0==LPAREN||LA79_0==INT_NUMERAL||(LA79_0>=WORD && LA79_0<=NAMED_PARAMETER)||(LA79_0>=64 && LA79_0<=65)||(LA79_0>=96 && LA79_0<=101)||(LA79_0>=113 && LA79_0<=114)) ) {
                alt79=1;
            }
            else if ( (LA79_0==52) ) {
                alt79=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 79, 0, input);

                throw nvae;
            }
            switch (alt79) {
                case 1 :
                    // JPA.g:306:4: simple_arithmetic_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simple_arithmetic_expression_in_arithmetic_expression2240);
                    simple_arithmetic_expression270=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression270.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:307:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_arithmetic_expression2245);
                    subquery271=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery271.getTree());

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
    // JPA.g:309:1: simple_arithmetic_expression : ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )* ;
    public final JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression() throws RecognitionException {
        JPAParser.simple_arithmetic_expression_return retval = new JPAParser.simple_arithmetic_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set273=null;
        JPAParser.arithmetic_term_return arithmetic_term272 = null;

        JPAParser.arithmetic_term_return arithmetic_term274 = null;


        Object set273_tree=null;

        try {
            // JPA.g:310:2: ( ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )* )
            // JPA.g:310:4: ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:310:4: ( arithmetic_term )
            // JPA.g:310:5: arithmetic_term
            {
            pushFollow(FOLLOW_arithmetic_term_in_simple_arithmetic_expression2255);
            arithmetic_term272=arithmetic_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term272.getTree());

            }

            // JPA.g:310:22: ( ( '+' | '-' ) arithmetic_term )*
            loop80:
            do {
                int alt80=2;
                int LA80_0 = input.LA(1);

                if ( ((LA80_0>=64 && LA80_0<=65)) ) {
                    alt80=1;
                }


                switch (alt80) {
            	case 1 :
            	    // JPA.g:310:23: ( '+' | '-' ) arithmetic_term
            	    {
            	    set273=(Token)input.LT(1);
            	    if ( (input.LA(1)>=64 && input.LA(1)<=65) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set273));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmetic_term_in_simple_arithmetic_expression2269);
            	    arithmetic_term274=arithmetic_term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term274.getTree());

            	    }
            	    break;

            	default :
            	    break loop80;
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
    // JPA.g:312:1: arithmetic_term : ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )* ;
    public final JPAParser.arithmetic_term_return arithmetic_term() throws RecognitionException {
        JPAParser.arithmetic_term_return retval = new JPAParser.arithmetic_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set276=null;
        JPAParser.arithmetic_factor_return arithmetic_factor275 = null;

        JPAParser.arithmetic_factor_return arithmetic_factor277 = null;


        Object set276_tree=null;

        try {
            // JPA.g:313:2: ( ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )* )
            // JPA.g:313:4: ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:313:4: ( arithmetic_factor )
            // JPA.g:313:5: arithmetic_factor
            {
            pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term2281);
            arithmetic_factor275=arithmetic_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor275.getTree());

            }

            // JPA.g:313:24: ( ( '*' | '/' ) arithmetic_factor )*
            loop81:
            do {
                int alt81=2;
                int LA81_0 = input.LA(1);

                if ( ((LA81_0>=94 && LA81_0<=95)) ) {
                    alt81=1;
                }


                switch (alt81) {
            	case 1 :
            	    // JPA.g:313:25: ( '*' | '/' ) arithmetic_factor
            	    {
            	    set276=(Token)input.LT(1);
            	    if ( (input.LA(1)>=94 && input.LA(1)<=95) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set276));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term2295);
            	    arithmetic_factor277=arithmetic_factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor277.getTree());

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
    // $ANTLR end "arithmetic_term"

    public static class arithmetic_factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arithmetic_factor"
    // JPA.g:315:1: arithmetic_factor : ( '+' | '-' )? arithmetic_primary ;
    public final JPAParser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
        JPAParser.arithmetic_factor_return retval = new JPAParser.arithmetic_factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set278=null;
        JPAParser.arithmetic_primary_return arithmetic_primary279 = null;


        Object set278_tree=null;

        try {
            // JPA.g:316:2: ( ( '+' | '-' )? arithmetic_primary )
            // JPA.g:316:4: ( '+' | '-' )? arithmetic_primary
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:316:4: ( '+' | '-' )?
            int alt82=2;
            int LA82_0 = input.LA(1);

            if ( ((LA82_0>=64 && LA82_0<=65)) ) {
                alt82=1;
            }
            switch (alt82) {
                case 1 :
                    // JPA.g:
                    {
                    set278=(Token)input.LT(1);
                    if ( (input.LA(1)>=64 && input.LA(1)<=65) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set278));
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

            pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor2317);
            arithmetic_primary279=arithmetic_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary279.getTree());

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
    // JPA.g:318:1: arithmetic_primary : ( path_expression | numeric_literal | '(' simple_arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression );
    public final JPAParser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
        JPAParser.arithmetic_primary_return retval = new JPAParser.arithmetic_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal282=null;
        Token char_literal284=null;
        JPAParser.path_expression_return path_expression280 = null;

        JPAParser.numeric_literal_return numeric_literal281 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression283 = null;

        JPAParser.input_parameter_return input_parameter285 = null;

        JPAParser.functions_returning_numerics_return functions_returning_numerics286 = null;

        JPAParser.aggregate_expression_return aggregate_expression287 = null;


        Object char_literal282_tree=null;
        Object char_literal284_tree=null;

        try {
            // JPA.g:319:2: ( path_expression | numeric_literal | '(' simple_arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression )
            int alt83=6;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt83=1;
                }
                break;
            case INT_NUMERAL:
            case 113:
                {
                alt83=2;
                }
                break;
            case LPAREN:
                {
                alt83=3;
                }
                break;
            case NAMED_PARAMETER:
            case 114:
                {
                alt83=4;
                }
                break;
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
                {
                alt83=5;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt83=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 83, 0, input);

                throw nvae;
            }

            switch (alt83) {
                case 1 :
                    // JPA.g:319:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_arithmetic_primary2326);
                    path_expression280=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression280.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:320:4: numeric_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary2331);
                    numeric_literal281=numeric_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal281.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:321:4: '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal282=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary2336); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal282_tree = (Object)adaptor.create(char_literal282);
                    adaptor.addChild(root_0, char_literal282_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_arithmetic_primary2337);
                    simple_arithmetic_expression283=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression283.getTree());
                    char_literal284=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary2338); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal284_tree = (Object)adaptor.create(char_literal284);
                    adaptor.addChild(root_0, char_literal284_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:322:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_arithmetic_primary2343);
                    input_parameter285=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter285.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:323:4: functions_returning_numerics
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary2348);
                    functions_returning_numerics286=functions_returning_numerics();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics286.getTree());

                    }
                    break;
                case 6 :
                    // JPA.g:324:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary2353);
                    aggregate_expression287=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression287.getTree());

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
    // JPA.g:326:1: string_expression : ( string_primary | subquery );
    public final JPAParser.string_expression_return string_expression() throws RecognitionException {
        JPAParser.string_expression_return retval = new JPAParser.string_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.string_primary_return string_primary288 = null;

        JPAParser.subquery_return subquery289 = null;



        try {
            // JPA.g:327:2: ( string_primary | subquery )
            int alt84=2;
            int LA84_0 = input.LA(1);

            if ( ((LA84_0>=AVG && LA84_0<=COUNT)||LA84_0==STRINGLITERAL||(LA84_0>=WORD && LA84_0<=NAMED_PARAMETER)||(LA84_0>=105 && LA84_0<=109)||LA84_0==114) ) {
                alt84=1;
            }
            else if ( (LA84_0==52) ) {
                alt84=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 84, 0, input);

                throw nvae;
            }
            switch (alt84) {
                case 1 :
                    // JPA.g:327:4: string_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_primary_in_string_expression2362);
                    string_primary288=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary288.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:327:21: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_string_expression2366);
                    subquery289=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery289.getTree());

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
    // JPA.g:329:1: string_primary : ( path_expression | STRINGLITERAL | input_parameter | functions_returning_strings | aggregate_expression );
    public final JPAParser.string_primary_return string_primary() throws RecognitionException {
        JPAParser.string_primary_return retval = new JPAParser.string_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRINGLITERAL291=null;
        JPAParser.path_expression_return path_expression290 = null;

        JPAParser.input_parameter_return input_parameter292 = null;

        JPAParser.functions_returning_strings_return functions_returning_strings293 = null;

        JPAParser.aggregate_expression_return aggregate_expression294 = null;


        Object STRINGLITERAL291_tree=null;

        try {
            // JPA.g:330:2: ( path_expression | STRINGLITERAL | input_parameter | functions_returning_strings | aggregate_expression )
            int alt85=5;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt85=1;
                }
                break;
            case STRINGLITERAL:
                {
                alt85=2;
                }
                break;
            case NAMED_PARAMETER:
            case 114:
                {
                alt85=3;
                }
                break;
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
                {
                alt85=4;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt85=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 85, 0, input);

                throw nvae;
            }

            switch (alt85) {
                case 1 :
                    // JPA.g:330:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_string_primary2375);
                    path_expression290=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression290.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:331:4: STRINGLITERAL
                    {
                    root_0 = (Object)adaptor.nil();

                    STRINGLITERAL291=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_string_primary2380); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRINGLITERAL291_tree = (Object)adaptor.create(STRINGLITERAL291);
                    adaptor.addChild(root_0, STRINGLITERAL291_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:332:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_string_primary2385);
                    input_parameter292=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter292.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:333:4: functions_returning_strings
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_strings_in_string_primary2390);
                    functions_returning_strings293=functions_returning_strings();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings293.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:334:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_string_primary2395);
                    aggregate_expression294=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression294.getTree());

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
    // JPA.g:336:1: datetime_expression : ( datetime_primary | subquery );
    public final JPAParser.datetime_expression_return datetime_expression() throws RecognitionException {
        JPAParser.datetime_expression_return retval = new JPAParser.datetime_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.datetime_primary_return datetime_primary295 = null;

        JPAParser.subquery_return subquery296 = null;



        try {
            // JPA.g:337:2: ( datetime_primary | subquery )
            int alt86=2;
            int LA86_0 = input.LA(1);

            if ( ((LA86_0>=AVG && LA86_0<=COUNT)||(LA86_0>=WORD && LA86_0<=NAMED_PARAMETER)||(LA86_0>=102 && LA86_0<=104)||LA86_0==114) ) {
                alt86=1;
            }
            else if ( (LA86_0==52) ) {
                alt86=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 86, 0, input);

                throw nvae;
            }
            switch (alt86) {
                case 1 :
                    // JPA.g:337:4: datetime_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_primary_in_datetime_expression2404);
                    datetime_primary295=datetime_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_primary295.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:338:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_datetime_expression2409);
                    subquery296=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery296.getTree());

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
    // JPA.g:340:1: datetime_primary : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression );
    public final JPAParser.datetime_primary_return datetime_primary() throws RecognitionException {
        JPAParser.datetime_primary_return retval = new JPAParser.datetime_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression297 = null;

        JPAParser.input_parameter_return input_parameter298 = null;

        JPAParser.functions_returning_datetime_return functions_returning_datetime299 = null;

        JPAParser.aggregate_expression_return aggregate_expression300 = null;



        try {
            // JPA.g:341:2: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression )
            int alt87=4;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt87=1;
                }
                break;
            case NAMED_PARAMETER:
            case 114:
                {
                alt87=2;
                }
                break;
            case 102:
            case 103:
            case 104:
                {
                alt87=3;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt87=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 87, 0, input);

                throw nvae;
            }

            switch (alt87) {
                case 1 :
                    // JPA.g:341:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_datetime_primary2418);
                    path_expression297=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression297.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:342:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_datetime_primary2423);
                    input_parameter298=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter298.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:343:4: functions_returning_datetime
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_datetime_in_datetime_primary2428);
                    functions_returning_datetime299=functions_returning_datetime();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime299.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:344:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_datetime_primary2433);
                    aggregate_expression300=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression300.getTree());

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
    // JPA.g:346:1: boolean_expression : ( boolean_primary | subquery );
    public final JPAParser.boolean_expression_return boolean_expression() throws RecognitionException {
        JPAParser.boolean_expression_return retval = new JPAParser.boolean_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.boolean_primary_return boolean_primary301 = null;

        JPAParser.subquery_return subquery302 = null;



        try {
            // JPA.g:347:2: ( boolean_primary | subquery )
            int alt88=2;
            int LA88_0 = input.LA(1);

            if ( ((LA88_0>=WORD && LA88_0<=NAMED_PARAMETER)||(LA88_0>=114 && LA88_0<=116)) ) {
                alt88=1;
            }
            else if ( (LA88_0==52) ) {
                alt88=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 88, 0, input);

                throw nvae;
            }
            switch (alt88) {
                case 1 :
                    // JPA.g:347:4: boolean_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_primary_in_boolean_expression2442);
                    boolean_primary301=boolean_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_primary301.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:348:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_boolean_expression2447);
                    subquery302=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery302.getTree());

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
    // JPA.g:350:1: boolean_primary : ( path_expression | boolean_literal | input_parameter );
    public final JPAParser.boolean_primary_return boolean_primary() throws RecognitionException {
        JPAParser.boolean_primary_return retval = new JPAParser.boolean_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression303 = null;

        JPAParser.boolean_literal_return boolean_literal304 = null;

        JPAParser.input_parameter_return input_parameter305 = null;



        try {
            // JPA.g:351:2: ( path_expression | boolean_literal | input_parameter )
            int alt89=3;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt89=1;
                }
                break;
            case 115:
            case 116:
                {
                alt89=2;
                }
                break;
            case NAMED_PARAMETER:
            case 114:
                {
                alt89=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 89, 0, input);

                throw nvae;
            }

            switch (alt89) {
                case 1 :
                    // JPA.g:351:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_boolean_primary2456);
                    path_expression303=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression303.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:352:4: boolean_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_literal_in_boolean_primary2461);
                    boolean_literal304=boolean_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal304.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:353:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_boolean_primary2466);
                    input_parameter305=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter305.getTree());

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
    // JPA.g:355:1: enum_expression : ( enum_primary | subquery );
    public final JPAParser.enum_expression_return enum_expression() throws RecognitionException {
        JPAParser.enum_expression_return retval = new JPAParser.enum_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.enum_primary_return enum_primary306 = null;

        JPAParser.subquery_return subquery307 = null;



        try {
            // JPA.g:356:2: ( enum_primary | subquery )
            int alt90=2;
            int LA90_0 = input.LA(1);

            if ( ((LA90_0>=WORD && LA90_0<=NAMED_PARAMETER)||LA90_0==114) ) {
                alt90=1;
            }
            else if ( (LA90_0==52) ) {
                alt90=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 90, 0, input);

                throw nvae;
            }
            switch (alt90) {
                case 1 :
                    // JPA.g:356:4: enum_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_primary_in_enum_expression2475);
                    enum_primary306=enum_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_primary306.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:357:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_enum_expression2480);
                    subquery307=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery307.getTree());

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
    // JPA.g:359:1: enum_primary : ( path_expression | enum_literal | input_parameter );
    public final JPAParser.enum_primary_return enum_primary() throws RecognitionException {
        JPAParser.enum_primary_return retval = new JPAParser.enum_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression308 = null;

        JPAParser.enum_literal_return enum_literal309 = null;

        JPAParser.input_parameter_return input_parameter310 = null;



        try {
            // JPA.g:360:2: ( path_expression | enum_literal | input_parameter )
            int alt91=3;
            int LA91_0 = input.LA(1);

            if ( (LA91_0==WORD) ) {
                int LA91_1 = input.LA(2);

                if ( (LA91_1==53) ) {
                    alt91=1;
                }
                else if ( (LA91_1==EOF||LA91_1==HAVING||(LA91_1>=OR && LA91_1<=AND)||LA91_1==RPAREN||LA91_1==58||LA91_1==60||(LA91_1>=88 && LA91_1<=89)) ) {
                    alt91=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 91, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA91_0==NAMED_PARAMETER||LA91_0==114) ) {
                alt91=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 91, 0, input);

                throw nvae;
            }
            switch (alt91) {
                case 1 :
                    // JPA.g:360:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_enum_primary2489);
                    path_expression308=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression308.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:361:4: enum_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_literal_in_enum_primary2494);
                    enum_literal309=enum_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal309.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:362:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_enum_primary2499);
                    input_parameter310=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter310.getTree());

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
    // JPA.g:364:1: entity_expression : ( path_expression | simple_entity_expression );
    public final JPAParser.entity_expression_return entity_expression() throws RecognitionException {
        JPAParser.entity_expression_return retval = new JPAParser.entity_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression311 = null;

        JPAParser.simple_entity_expression_return simple_entity_expression312 = null;



        try {
            // JPA.g:365:2: ( path_expression | simple_entity_expression )
            int alt92=2;
            int LA92_0 = input.LA(1);

            if ( (LA92_0==WORD) ) {
                int LA92_1 = input.LA(2);

                if ( (LA92_1==53) ) {
                    alt92=1;
                }
                else if ( (LA92_1==EOF||LA92_1==HAVING||(LA92_1>=OR && LA92_1<=AND)||LA92_1==RPAREN||LA92_1==58||(LA92_1>=60 && LA92_1<=61)||LA92_1==82||(LA92_1>=88 && LA92_1<=89)) ) {
                    alt92=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 92, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA92_0==NAMED_PARAMETER||LA92_0==114) ) {
                alt92=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 92, 0, input);

                throw nvae;
            }
            switch (alt92) {
                case 1 :
                    // JPA.g:365:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_entity_expression2508);
                    path_expression311=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression311.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:366:4: simple_entity_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simple_entity_expression_in_entity_expression2513);
                    simple_entity_expression312=simple_entity_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression312.getTree());

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
    // JPA.g:368:1: simple_entity_expression : ( identification_variable | input_parameter );
    public final JPAParser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
        JPAParser.simple_entity_expression_return retval = new JPAParser.simple_entity_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.identification_variable_return identification_variable313 = null;

        JPAParser.input_parameter_return input_parameter314 = null;



        try {
            // JPA.g:369:2: ( identification_variable | input_parameter )
            int alt93=2;
            int LA93_0 = input.LA(1);

            if ( (LA93_0==WORD) ) {
                alt93=1;
            }
            else if ( (LA93_0==NAMED_PARAMETER||LA93_0==114) ) {
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
                    // JPA.g:369:4: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_simple_entity_expression2522);
                    identification_variable313=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable313.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:370:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_simple_entity_expression2527);
                    input_parameter314=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter314.getTree());

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
    // JPA.g:372:1: functions_returning_numerics : ( 'LENGTH' '(' string_primary ')' | 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')' | 'ABS' '(' simple_arithmetic_expression ')' | 'SQRT' '(' simple_arithmetic_expression ')' | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'SIZE' '(' path_expression ')' );
    public final JPAParser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
        JPAParser.functions_returning_numerics_return retval = new JPAParser.functions_returning_numerics_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal315=null;
        Token char_literal316=null;
        Token char_literal318=null;
        Token string_literal319=null;
        Token char_literal320=null;
        Token char_literal322=null;
        Token char_literal324=null;
        Token char_literal326=null;
        Token string_literal327=null;
        Token char_literal328=null;
        Token char_literal330=null;
        Token string_literal331=null;
        Token char_literal332=null;
        Token char_literal334=null;
        Token string_literal335=null;
        Token char_literal336=null;
        Token char_literal338=null;
        Token char_literal340=null;
        Token string_literal341=null;
        Token char_literal342=null;
        Token char_literal344=null;
        JPAParser.string_primary_return string_primary317 = null;

        JPAParser.string_primary_return string_primary321 = null;

        JPAParser.string_primary_return string_primary323 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression325 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression329 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression333 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression337 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression339 = null;

        JPAParser.path_expression_return path_expression343 = null;


        Object string_literal315_tree=null;
        Object char_literal316_tree=null;
        Object char_literal318_tree=null;
        Object string_literal319_tree=null;
        Object char_literal320_tree=null;
        Object char_literal322_tree=null;
        Object char_literal324_tree=null;
        Object char_literal326_tree=null;
        Object string_literal327_tree=null;
        Object char_literal328_tree=null;
        Object char_literal330_tree=null;
        Object string_literal331_tree=null;
        Object char_literal332_tree=null;
        Object char_literal334_tree=null;
        Object string_literal335_tree=null;
        Object char_literal336_tree=null;
        Object char_literal338_tree=null;
        Object char_literal340_tree=null;
        Object string_literal341_tree=null;
        Object char_literal342_tree=null;
        Object char_literal344_tree=null;

        try {
            // JPA.g:373:2: ( 'LENGTH' '(' string_primary ')' | 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')' | 'ABS' '(' simple_arithmetic_expression ')' | 'SQRT' '(' simple_arithmetic_expression ')' | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'SIZE' '(' path_expression ')' )
            int alt95=6;
            switch ( input.LA(1) ) {
            case 96:
                {
                alt95=1;
                }
                break;
            case 97:
                {
                alt95=2;
                }
                break;
            case 98:
                {
                alt95=3;
                }
                break;
            case 99:
                {
                alt95=4;
                }
                break;
            case 100:
                {
                alt95=5;
                }
                break;
            case 101:
                {
                alt95=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 95, 0, input);

                throw nvae;
            }

            switch (alt95) {
                case 1 :
                    // JPA.g:373:4: 'LENGTH' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal315=(Token)match(input,96,FOLLOW_96_in_functions_returning_numerics2536); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal315_tree = (Object)adaptor.create(string_literal315);
                    adaptor.addChild(root_0, string_literal315_tree);
                    }
                    char_literal316=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2538); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal316_tree = (Object)adaptor.create(char_literal316);
                    adaptor.addChild(root_0, char_literal316_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics2539);
                    string_primary317=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary317.getTree());
                    char_literal318=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2540); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal318_tree = (Object)adaptor.create(char_literal318);
                    adaptor.addChild(root_0, char_literal318_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:374:4: 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal319=(Token)match(input,97,FOLLOW_97_in_functions_returning_numerics2545); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal319_tree = (Object)adaptor.create(string_literal319);
                    adaptor.addChild(root_0, string_literal319_tree);
                    }
                    char_literal320=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2547); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal320_tree = (Object)adaptor.create(char_literal320);
                    adaptor.addChild(root_0, char_literal320_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics2548);
                    string_primary321=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary321.getTree());
                    char_literal322=(Token)match(input,50,FOLLOW_50_in_functions_returning_numerics2549); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal322_tree = (Object)adaptor.create(char_literal322);
                    adaptor.addChild(root_0, char_literal322_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics2551);
                    string_primary323=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary323.getTree());
                    // JPA.g:374:48: ( ',' simple_arithmetic_expression )?
                    int alt94=2;
                    int LA94_0 = input.LA(1);

                    if ( (LA94_0==50) ) {
                        alt94=1;
                    }
                    switch (alt94) {
                        case 1 :
                            // JPA.g:374:49: ',' simple_arithmetic_expression
                            {
                            char_literal324=(Token)match(input,50,FOLLOW_50_in_functions_returning_numerics2553); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal324_tree = (Object)adaptor.create(char_literal324);
                            adaptor.addChild(root_0, char_literal324_tree);
                            }
                            pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2555);
                            simple_arithmetic_expression325=simple_arithmetic_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression325.getTree());

                            }
                            break;

                    }

                    char_literal326=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2558); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal326_tree = (Object)adaptor.create(char_literal326);
                    adaptor.addChild(root_0, char_literal326_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:375:4: 'ABS' '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal327=(Token)match(input,98,FOLLOW_98_in_functions_returning_numerics2563); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal327_tree = (Object)adaptor.create(string_literal327);
                    adaptor.addChild(root_0, string_literal327_tree);
                    }
                    char_literal328=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2565); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal328_tree = (Object)adaptor.create(char_literal328);
                    adaptor.addChild(root_0, char_literal328_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2566);
                    simple_arithmetic_expression329=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression329.getTree());
                    char_literal330=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2567); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal330_tree = (Object)adaptor.create(char_literal330);
                    adaptor.addChild(root_0, char_literal330_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:376:4: 'SQRT' '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal331=(Token)match(input,99,FOLLOW_99_in_functions_returning_numerics2572); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal331_tree = (Object)adaptor.create(string_literal331);
                    adaptor.addChild(root_0, string_literal331_tree);
                    }
                    char_literal332=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2574); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal332_tree = (Object)adaptor.create(char_literal332);
                    adaptor.addChild(root_0, char_literal332_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2575);
                    simple_arithmetic_expression333=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression333.getTree());
                    char_literal334=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2576); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal334_tree = (Object)adaptor.create(char_literal334);
                    adaptor.addChild(root_0, char_literal334_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:377:4: 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal335=(Token)match(input,100,FOLLOW_100_in_functions_returning_numerics2581); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal335_tree = (Object)adaptor.create(string_literal335);
                    adaptor.addChild(root_0, string_literal335_tree);
                    }
                    char_literal336=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2583); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal336_tree = (Object)adaptor.create(char_literal336);
                    adaptor.addChild(root_0, char_literal336_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2584);
                    simple_arithmetic_expression337=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression337.getTree());
                    char_literal338=(Token)match(input,50,FOLLOW_50_in_functions_returning_numerics2585); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal338_tree = (Object)adaptor.create(char_literal338);
                    adaptor.addChild(root_0, char_literal338_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2587);
                    simple_arithmetic_expression339=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression339.getTree());
                    char_literal340=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2588); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal340_tree = (Object)adaptor.create(char_literal340);
                    adaptor.addChild(root_0, char_literal340_tree);
                    }

                    }
                    break;
                case 6 :
                    // JPA.g:378:4: 'SIZE' '(' path_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal341=(Token)match(input,101,FOLLOW_101_in_functions_returning_numerics2593); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal341_tree = (Object)adaptor.create(string_literal341);
                    adaptor.addChild(root_0, string_literal341_tree);
                    }
                    char_literal342=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2595); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal342_tree = (Object)adaptor.create(char_literal342);
                    adaptor.addChild(root_0, char_literal342_tree);
                    }
                    pushFollow(FOLLOW_path_expression_in_functions_returning_numerics2596);
                    path_expression343=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression343.getTree());
                    char_literal344=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2597); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal344_tree = (Object)adaptor.create(char_literal344);
                    adaptor.addChild(root_0, char_literal344_tree);
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
    // JPA.g:380:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
    public final JPAParser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
        JPAParser.functions_returning_datetime_return retval = new JPAParser.functions_returning_datetime_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set345=null;

        Object set345_tree=null;

        try {
            // JPA.g:381:2: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set345=(Token)input.LT(1);
            if ( (input.LA(1)>=102 && input.LA(1)<=104) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set345));
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
    // JPA.g:385:1: functions_returning_strings : ( 'CONCAT' '(' string_primary ',' string_primary ')' | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')' | 'LOWER' '(' string_primary ')' | 'UPPER' '(' string_primary ')' );
    public final JPAParser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
        JPAParser.functions_returning_strings_return retval = new JPAParser.functions_returning_strings_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal346=null;
        Token char_literal347=null;
        Token char_literal349=null;
        Token char_literal351=null;
        Token string_literal352=null;
        Token char_literal353=null;
        Token char_literal355=null;
        Token char_literal357=null;
        Token char_literal359=null;
        Token string_literal360=null;
        Token char_literal361=null;
        Token TRIM_CHARACTER363=null;
        Token string_literal364=null;
        Token char_literal366=null;
        Token string_literal367=null;
        Token char_literal368=null;
        Token char_literal370=null;
        Token string_literal371=null;
        Token char_literal372=null;
        Token char_literal374=null;
        JPAParser.string_primary_return string_primary348 = null;

        JPAParser.string_primary_return string_primary350 = null;

        JPAParser.string_primary_return string_primary354 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression356 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression358 = null;

        JPAParser.trim_specification_return trim_specification362 = null;

        JPAParser.string_primary_return string_primary365 = null;

        JPAParser.string_primary_return string_primary369 = null;

        JPAParser.string_primary_return string_primary373 = null;


        Object string_literal346_tree=null;
        Object char_literal347_tree=null;
        Object char_literal349_tree=null;
        Object char_literal351_tree=null;
        Object string_literal352_tree=null;
        Object char_literal353_tree=null;
        Object char_literal355_tree=null;
        Object char_literal357_tree=null;
        Object char_literal359_tree=null;
        Object string_literal360_tree=null;
        Object char_literal361_tree=null;
        Object TRIM_CHARACTER363_tree=null;
        Object string_literal364_tree=null;
        Object char_literal366_tree=null;
        Object string_literal367_tree=null;
        Object char_literal368_tree=null;
        Object char_literal370_tree=null;
        Object string_literal371_tree=null;
        Object char_literal372_tree=null;
        Object char_literal374_tree=null;

        try {
            // JPA.g:386:2: ( 'CONCAT' '(' string_primary ',' string_primary ')' | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')' | 'LOWER' '(' string_primary ')' | 'UPPER' '(' string_primary ')' )
            int alt99=5;
            switch ( input.LA(1) ) {
            case 105:
                {
                alt99=1;
                }
                break;
            case 106:
                {
                alt99=2;
                }
                break;
            case 107:
                {
                alt99=3;
                }
                break;
            case 108:
                {
                alt99=4;
                }
                break;
            case 109:
                {
                alt99=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 99, 0, input);

                throw nvae;
            }

            switch (alt99) {
                case 1 :
                    // JPA.g:386:4: 'CONCAT' '(' string_primary ',' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal346=(Token)match(input,105,FOLLOW_105_in_functions_returning_strings2625); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal346_tree = (Object)adaptor.create(string_literal346);
                    adaptor.addChild(root_0, string_literal346_tree);
                    }
                    char_literal347=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2627); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal347_tree = (Object)adaptor.create(char_literal347);
                    adaptor.addChild(root_0, char_literal347_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2628);
                    string_primary348=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary348.getTree());
                    char_literal349=(Token)match(input,50,FOLLOW_50_in_functions_returning_strings2629); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal349_tree = (Object)adaptor.create(char_literal349);
                    adaptor.addChild(root_0, char_literal349_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2631);
                    string_primary350=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary350.getTree());
                    char_literal351=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2632); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal351_tree = (Object)adaptor.create(char_literal351);
                    adaptor.addChild(root_0, char_literal351_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:387:4: 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal352=(Token)match(input,106,FOLLOW_106_in_functions_returning_strings2637); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal352_tree = (Object)adaptor.create(string_literal352);
                    adaptor.addChild(root_0, string_literal352_tree);
                    }
                    char_literal353=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2639); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal353_tree = (Object)adaptor.create(char_literal353);
                    adaptor.addChild(root_0, char_literal353_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2640);
                    string_primary354=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary354.getTree());
                    char_literal355=(Token)match(input,50,FOLLOW_50_in_functions_returning_strings2641); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal355_tree = (Object)adaptor.create(char_literal355);
                    adaptor.addChild(root_0, char_literal355_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2642);
                    simple_arithmetic_expression356=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression356.getTree());
                    char_literal357=(Token)match(input,50,FOLLOW_50_in_functions_returning_strings2643); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal357_tree = (Object)adaptor.create(char_literal357);
                    adaptor.addChild(root_0, char_literal357_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2645);
                    simple_arithmetic_expression358=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression358.getTree());
                    char_literal359=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2646); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal359_tree = (Object)adaptor.create(char_literal359);
                    adaptor.addChild(root_0, char_literal359_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:388:4: 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal360=(Token)match(input,107,FOLLOW_107_in_functions_returning_strings2651); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal360_tree = (Object)adaptor.create(string_literal360);
                    adaptor.addChild(root_0, string_literal360_tree);
                    }
                    char_literal361=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2653); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal361_tree = (Object)adaptor.create(char_literal361);
                    adaptor.addChild(root_0, char_literal361_tree);
                    }
                    // JPA.g:388:14: ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )?
                    int alt98=2;
                    int LA98_0 = input.LA(1);

                    if ( (LA98_0==TRIM_CHARACTER||LA98_0==49||(LA98_0>=110 && LA98_0<=112)) ) {
                        alt98=1;
                    }
                    switch (alt98) {
                        case 1 :
                            // JPA.g:388:15: ( trim_specification )? ( TRIM_CHARACTER )? 'FROM'
                            {
                            // JPA.g:388:15: ( trim_specification )?
                            int alt96=2;
                            int LA96_0 = input.LA(1);

                            if ( ((LA96_0>=110 && LA96_0<=112)) ) {
                                alt96=1;
                            }
                            switch (alt96) {
                                case 1 :
                                    // JPA.g:388:16: trim_specification
                                    {
                                    pushFollow(FOLLOW_trim_specification_in_functions_returning_strings2656);
                                    trim_specification362=trim_specification();

                                    state._fsp--;
                                    if (state.failed) return retval;
                                    if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification362.getTree());

                                    }
                                    break;

                            }

                            // JPA.g:388:37: ( TRIM_CHARACTER )?
                            int alt97=2;
                            int LA97_0 = input.LA(1);

                            if ( (LA97_0==TRIM_CHARACTER) ) {
                                alt97=1;
                            }
                            switch (alt97) {
                                case 1 :
                                    // JPA.g:388:38: TRIM_CHARACTER
                                    {
                                    TRIM_CHARACTER363=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_functions_returning_strings2661); if (state.failed) return retval;
                                    if ( state.backtracking==0 ) {
                                    TRIM_CHARACTER363_tree = (Object)adaptor.create(TRIM_CHARACTER363);
                                    adaptor.addChild(root_0, TRIM_CHARACTER363_tree);
                                    }

                                    }
                                    break;

                            }

                            string_literal364=(Token)match(input,49,FOLLOW_49_in_functions_returning_strings2665); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal364_tree = (Object)adaptor.create(string_literal364);
                            adaptor.addChild(root_0, string_literal364_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2669);
                    string_primary365=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary365.getTree());
                    char_literal366=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2670); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal366_tree = (Object)adaptor.create(char_literal366);
                    adaptor.addChild(root_0, char_literal366_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:389:4: 'LOWER' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal367=(Token)match(input,108,FOLLOW_108_in_functions_returning_strings2675); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal367_tree = (Object)adaptor.create(string_literal367);
                    adaptor.addChild(root_0, string_literal367_tree);
                    }
                    char_literal368=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2677); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal368_tree = (Object)adaptor.create(char_literal368);
                    adaptor.addChild(root_0, char_literal368_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2678);
                    string_primary369=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary369.getTree());
                    char_literal370=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2679); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal370_tree = (Object)adaptor.create(char_literal370);
                    adaptor.addChild(root_0, char_literal370_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:390:4: 'UPPER' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal371=(Token)match(input,109,FOLLOW_109_in_functions_returning_strings2684); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal371_tree = (Object)adaptor.create(string_literal371);
                    adaptor.addChild(root_0, string_literal371_tree);
                    }
                    char_literal372=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2686); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal372_tree = (Object)adaptor.create(char_literal372);
                    adaptor.addChild(root_0, char_literal372_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2687);
                    string_primary373=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary373.getTree());
                    char_literal374=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2688); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal374_tree = (Object)adaptor.create(char_literal374);
                    adaptor.addChild(root_0, char_literal374_tree);
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
    // JPA.g:392:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
    public final JPAParser.trim_specification_return trim_specification() throws RecognitionException {
        JPAParser.trim_specification_return retval = new JPAParser.trim_specification_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set375=null;

        Object set375_tree=null;

        try {
            // JPA.g:393:2: ( 'LEADING' | 'TRAILING' | 'BOTH' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set375=(Token)input.LT(1);
            if ( (input.LA(1)>=110 && input.LA(1)<=112) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set375));
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
    // JPA.g:398:1: abstract_schema_name : WORD ;
    public final JPAParser.abstract_schema_name_return abstract_schema_name() throws RecognitionException {
        JPAParser.abstract_schema_name_return retval = new JPAParser.abstract_schema_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD376=null;

        Object WORD376_tree=null;

        try {
            // JPA.g:399:4: ( WORD )
            // JPA.g:399:6: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD376=(Token)match(input,WORD,FOLLOW_WORD_in_abstract_schema_name2719); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD376_tree = (Object)adaptor.create(WORD376);
            adaptor.addChild(root_0, WORD376_tree);
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
    // JPA.g:402:1: pattern_value : WORD ;
    public final JPAParser.pattern_value_return pattern_value() throws RecognitionException {
        JPAParser.pattern_value_return retval = new JPAParser.pattern_value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD377=null;

        Object WORD377_tree=null;

        try {
            // JPA.g:403:2: ( WORD )
            // JPA.g:403:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD377=(Token)match(input,WORD,FOLLOW_WORD_in_pattern_value2729); if (state.failed) return retval;
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
    // $ANTLR end "pattern_value"

    public static class numeric_literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numeric_literal"
    // JPA.g:406:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
    public final JPAParser.numeric_literal_return numeric_literal() throws RecognitionException {
        JPAParser.numeric_literal_return retval = new JPAParser.numeric_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal378=null;
        Token INT_NUMERAL379=null;

        Object string_literal378_tree=null;
        Object INT_NUMERAL379_tree=null;

        try {
            // JPA.g:407:2: ( ( '0x' )? INT_NUMERAL )
            // JPA.g:407:4: ( '0x' )? INT_NUMERAL
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:407:4: ( '0x' )?
            int alt100=2;
            int LA100_0 = input.LA(1);

            if ( (LA100_0==113) ) {
                alt100=1;
            }
            switch (alt100) {
                case 1 :
                    // JPA.g:407:5: '0x'
                    {
                    string_literal378=(Token)match(input,113,FOLLOW_113_in_numeric_literal2740); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal378_tree = (Object)adaptor.create(string_literal378);
                    adaptor.addChild(root_0, string_literal378_tree);
                    }

                    }
                    break;

            }

            INT_NUMERAL379=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal2744); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            INT_NUMERAL379_tree = (Object)adaptor.create(INT_NUMERAL379);
            adaptor.addChild(root_0, INT_NUMERAL379_tree);
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
    // JPA.g:409:1: input_parameter : ( '?' INT_NUMERAL -> ^( T_PARAMETER[] '?' INT_NUMERAL ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) );
    public final JPAParser.input_parameter_return input_parameter() throws RecognitionException {
        JPAParser.input_parameter_return retval = new JPAParser.input_parameter_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal380=null;
        Token INT_NUMERAL381=null;
        Token NAMED_PARAMETER382=null;

        Object char_literal380_tree=null;
        Object INT_NUMERAL381_tree=null;
        Object NAMED_PARAMETER382_tree=null;
        RewriteRuleTokenStream stream_114=new RewriteRuleTokenStream(adaptor,"token 114");
        RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
        RewriteRuleTokenStream stream_INT_NUMERAL=new RewriteRuleTokenStream(adaptor,"token INT_NUMERAL");

        try {
            // JPA.g:410:2: ( '?' INT_NUMERAL -> ^( T_PARAMETER[] '?' INT_NUMERAL ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) )
            int alt101=2;
            int LA101_0 = input.LA(1);

            if ( (LA101_0==114) ) {
                alt101=1;
            }
            else if ( (LA101_0==NAMED_PARAMETER) ) {
                alt101=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 101, 0, input);

                throw nvae;
            }
            switch (alt101) {
                case 1 :
                    // JPA.g:410:4: '?' INT_NUMERAL
                    {
                    char_literal380=(Token)match(input,114,FOLLOW_114_in_input_parameter2754); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_114.add(char_literal380);

                    INT_NUMERAL381=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_input_parameter2756); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_INT_NUMERAL.add(INT_NUMERAL381);



                    // AST REWRITE
                    // elements: 114, INT_NUMERAL
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 410:20: -> ^( T_PARAMETER[] '?' INT_NUMERAL )
                    {
                        // JPA.g:410:23: ^( T_PARAMETER[] '?' INT_NUMERAL )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);

                        adaptor.addChild(root_1, stream_114.nextNode());
                        adaptor.addChild(root_1, stream_INT_NUMERAL.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // JPA.g:411:4: NAMED_PARAMETER
                    {
                    NAMED_PARAMETER382=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter2776); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER382);



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
                    // 411:20: -> ^( T_PARAMETER[] NAMED_PARAMETER )
                    {
                        // JPA.g:411:23: ^( T_PARAMETER[] NAMED_PARAMETER )
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
    // JPA.g:413:1: literal : WORD ;
    public final JPAParser.literal_return literal() throws RecognitionException {
        JPAParser.literal_return retval = new JPAParser.literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD383=null;

        Object WORD383_tree=null;

        try {
            // JPA.g:414:2: ( WORD )
            // JPA.g:414:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD383=(Token)match(input,WORD,FOLLOW_WORD_in_literal2798); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD383_tree = (Object)adaptor.create(WORD383);
            adaptor.addChild(root_0, WORD383_tree);
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
    // JPA.g:416:1: constructor_name : WORD ;
    public final JPAParser.constructor_name_return constructor_name() throws RecognitionException {
        JPAParser.constructor_name_return retval = new JPAParser.constructor_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD384=null;

        Object WORD384_tree=null;

        try {
            // JPA.g:417:2: ( WORD )
            // JPA.g:417:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD384=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name2807); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD384_tree = (Object)adaptor.create(WORD384);
            adaptor.addChild(root_0, WORD384_tree);
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
    // JPA.g:419:1: enum_literal : WORD ;
    public final JPAParser.enum_literal_return enum_literal() throws RecognitionException {
        JPAParser.enum_literal_return retval = new JPAParser.enum_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD385=null;

        Object WORD385_tree=null;

        try {
            // JPA.g:420:2: ( WORD )
            // JPA.g:420:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD385=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal2848); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD385_tree = (Object)adaptor.create(WORD385);
            adaptor.addChild(root_0, WORD385_tree);
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
    // JPA.g:422:1: boolean_literal : ( 'true' | 'false' );
    public final JPAParser.boolean_literal_return boolean_literal() throws RecognitionException {
        JPAParser.boolean_literal_return retval = new JPAParser.boolean_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set386=null;

        Object set386_tree=null;

        try {
            // JPA.g:423:2: ( 'true' | 'false' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set386=(Token)input.LT(1);
            if ( (input.LA(1)>=115 && input.LA(1)<=116) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set386));
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
    // JPA.g:427:1: field : ( WORD | 'GROUP' );
    public final JPAParser.field_return field() throws RecognitionException {
        JPAParser.field_return retval = new JPAParser.field_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set387=null;

        Object set387_tree=null;

        try {
            // JPA.g:428:4: ( WORD | 'GROUP' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set387=(Token)input.LT(1);
            if ( input.LA(1)==WORD||input.LA(1)==58 ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set387));
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
    // JPA.g:430:1: identification_variable : WORD ;
    public final JPAParser.identification_variable_return identification_variable() throws RecognitionException {
        JPAParser.identification_variable_return retval = new JPAParser.identification_variable_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD388=null;

        Object WORD388_tree=null;

        try {
            // JPA.g:431:4: ( WORD )
            // JPA.g:431:6: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD388=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable2889); if (state.failed) return retval;
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
    // $ANTLR end "identification_variable"

    public static class parameter_name_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameter_name"
    // JPA.g:433:1: parameter_name : WORD ( '.' WORD )* ;
    public final JPAParser.parameter_name_return parameter_name() throws RecognitionException {
        JPAParser.parameter_name_return retval = new JPAParser.parameter_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD389=null;
        Token char_literal390=null;
        Token WORD391=null;

        Object WORD389_tree=null;
        Object char_literal390_tree=null;
        Object WORD391_tree=null;

        try {
            // JPA.g:434:4: ( WORD ( '.' WORD )* )
            // JPA.g:434:6: WORD ( '.' WORD )*
            {
            root_0 = (Object)adaptor.nil();

            WORD389=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name2900); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD389_tree = (Object)adaptor.create(WORD389);
            adaptor.addChild(root_0, WORD389_tree);
            }
            // JPA.g:434:11: ( '.' WORD )*
            loop102:
            do {
                int alt102=2;
                int LA102_0 = input.LA(1);

                if ( (LA102_0==53) ) {
                    alt102=1;
                }


                switch (alt102) {
            	case 1 :
            	    // JPA.g:434:12: '.' WORD
            	    {
            	    char_literal390=(Token)match(input,53,FOLLOW_53_in_parameter_name2903); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal390_tree = (Object)adaptor.create(char_literal390);
            	    adaptor.addChild(root_0, char_literal390_tree);
            	    }
            	    WORD391=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name2906); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    WORD391_tree = (Object)adaptor.create(WORD391);
            	    adaptor.addChild(root_0, WORD391_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop102;
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
        // JPA.g:121:44: ( field )
        // JPA.g:121:44: field
        {
        pushFollow(FOLLOW_field_in_synpred20_JPA763);
        field();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred20_JPA

    // $ANTLR start synpred23_JPA
    public final void synpred23_JPA_fragment() throws RecognitionException {
        // JPA.g:131:46: ( field )
        // JPA.g:131:46: field
        {
        pushFollow(FOLLOW_field_in_synpred23_JPA840);
        field();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred23_JPA

    // $ANTLR start synpred57_JPA
    public final void synpred57_JPA_fragment() throws RecognitionException {
        // JPA.g:219:5: ( 'NOT' )
        // JPA.g:219:5: 'NOT'
        {
        match(input,61,FOLLOW_61_in_synpred57_JPA1539); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred57_JPA

    // $ANTLR start synpred58_JPA
    public final void synpred58_JPA_fragment() throws RecognitionException {
        // JPA.g:219:4: ( ( 'NOT' )? simple_cond_expression )
        // JPA.g:219:4: ( 'NOT' )? simple_cond_expression
        {
        // JPA.g:219:4: ( 'NOT' )?
        int alt107=2;
        int LA107_0 = input.LA(1);

        if ( (LA107_0==61) ) {
            int LA107_1 = input.LA(2);

            if ( (synpred57_JPA()) ) {
                alt107=1;
            }
        }
        switch (alt107) {
            case 1 :
                // JPA.g:219:5: 'NOT'
                {
                match(input,61,FOLLOW_61_in_synpred58_JPA1539); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_simple_cond_expression_in_synpred58_JPA1543);
        simple_cond_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred58_JPA

    // $ANTLR start synpred59_JPA
    public final void synpred59_JPA_fragment() throws RecognitionException {
        // JPA.g:223:4: ( comparison_expression )
        // JPA.g:223:4: comparison_expression
        {
        pushFollow(FOLLOW_comparison_expression_in_synpred59_JPA1576);
        comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred59_JPA

    // $ANTLR start synpred60_JPA
    public final void synpred60_JPA_fragment() throws RecognitionException {
        // JPA.g:224:4: ( between_expression )
        // JPA.g:224:4: between_expression
        {
        pushFollow(FOLLOW_between_expression_in_synpred60_JPA1581);
        between_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred60_JPA

    // $ANTLR start synpred61_JPA
    public final void synpred61_JPA_fragment() throws RecognitionException {
        // JPA.g:225:4: ( like_expression )
        // JPA.g:225:4: like_expression
        {
        pushFollow(FOLLOW_like_expression_in_synpred61_JPA1586);
        like_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred61_JPA

    // $ANTLR start synpred62_JPA
    public final void synpred62_JPA_fragment() throws RecognitionException {
        // JPA.g:226:4: ( in_expression )
        // JPA.g:226:4: in_expression
        {
        pushFollow(FOLLOW_in_expression_in_synpred62_JPA1591);
        in_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred62_JPA

    // $ANTLR start synpred63_JPA
    public final void synpred63_JPA_fragment() throws RecognitionException {
        // JPA.g:227:4: ( null_comparison_expression )
        // JPA.g:227:4: null_comparison_expression
        {
        pushFollow(FOLLOW_null_comparison_expression_in_synpred63_JPA1596);
        null_comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred63_JPA

    // $ANTLR start synpred64_JPA
    public final void synpred64_JPA_fragment() throws RecognitionException {
        // JPA.g:228:4: ( empty_collection_comparison_expression )
        // JPA.g:228:4: empty_collection_comparison_expression
        {
        pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred64_JPA1601);
        empty_collection_comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred64_JPA

    // $ANTLR start synpred65_JPA
    public final void synpred65_JPA_fragment() throws RecognitionException {
        // JPA.g:229:4: ( collection_member_expression )
        // JPA.g:229:4: collection_member_expression
        {
        pushFollow(FOLLOW_collection_member_expression_in_synpred65_JPA1606);
        collection_member_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred65_JPA

    // $ANTLR start synpred84_JPA
    public final void synpred84_JPA_fragment() throws RecognitionException {
        // JPA.g:256:4: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
        // JPA.g:256:4: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
        {
        pushFollow(FOLLOW_arithmetic_expression_in_synpred84_JPA1848);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:256:26: ( 'NOT' )?
        int alt108=2;
        int LA108_0 = input.LA(1);

        if ( (LA108_0==61) ) {
            alt108=1;
        }
        switch (alt108) {
            case 1 :
                // JPA.g:256:27: 'NOT'
                {
                match(input,61,FOLLOW_61_in_synpred84_JPA1851); if (state.failed) return ;

                }
                break;

        }

        match(input,76,FOLLOW_76_in_synpred84_JPA1855); if (state.failed) return ;
        pushFollow(FOLLOW_arithmetic_expression_in_synpred84_JPA1857);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,AND,FOLLOW_AND_in_synpred84_JPA1859); if (state.failed) return ;
        pushFollow(FOLLOW_arithmetic_expression_in_synpred84_JPA1861);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred84_JPA

    // $ANTLR start synpred86_JPA
    public final void synpred86_JPA_fragment() throws RecognitionException {
        // JPA.g:257:4: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
        // JPA.g:257:4: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
        {
        pushFollow(FOLLOW_string_expression_in_synpred86_JPA1866);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:257:22: ( 'NOT' )?
        int alt109=2;
        int LA109_0 = input.LA(1);

        if ( (LA109_0==61) ) {
            alt109=1;
        }
        switch (alt109) {
            case 1 :
                // JPA.g:257:23: 'NOT'
                {
                match(input,61,FOLLOW_61_in_synpred86_JPA1869); if (state.failed) return ;

                }
                break;

        }

        match(input,76,FOLLOW_76_in_synpred86_JPA1873); if (state.failed) return ;
        pushFollow(FOLLOW_string_expression_in_synpred86_JPA1875);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,AND,FOLLOW_AND_in_synpred86_JPA1877); if (state.failed) return ;
        pushFollow(FOLLOW_string_expression_in_synpred86_JPA1879);
        string_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred86_JPA

    // $ANTLR start synpred104_JPA
    public final void synpred104_JPA_fragment() throws RecognitionException {
        // JPA.g:290:4: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) )
        // JPA.g:290:4: string_expression comparison_operator ( string_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_string_expression_in_synpred104_JPA2096);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_comparison_operator_in_synpred104_JPA2098);
        comparison_operator();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:290:42: ( string_expression | all_or_any_expression )
        int alt111=2;
        int LA111_0 = input.LA(1);

        if ( ((LA111_0>=AVG && LA111_0<=COUNT)||LA111_0==STRINGLITERAL||(LA111_0>=WORD && LA111_0<=NAMED_PARAMETER)||LA111_0==52||(LA111_0>=105 && LA111_0<=109)||LA111_0==114) ) {
            alt111=1;
        }
        else if ( ((LA111_0>=85 && LA111_0<=87)) ) {
            alt111=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 111, 0, input);

            throw nvae;
        }
        switch (alt111) {
            case 1 :
                // JPA.g:290:43: string_expression
                {
                pushFollow(FOLLOW_string_expression_in_synpred104_JPA2101);
                string_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:290:63: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred104_JPA2105);
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
        // JPA.g:291:4: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
        // JPA.g:291:4: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_boolean_expression_in_synpred107_JPA2111);
        boolean_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=88 && input.LA(1)<=89) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:291:36: ( boolean_expression | all_or_any_expression )
        int alt112=2;
        int LA112_0 = input.LA(1);

        if ( ((LA112_0>=WORD && LA112_0<=NAMED_PARAMETER)||LA112_0==52||(LA112_0>=114 && LA112_0<=116)) ) {
            alt112=1;
        }
        else if ( ((LA112_0>=85 && LA112_0<=87)) ) {
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
                // JPA.g:291:37: boolean_expression
                {
                pushFollow(FOLLOW_boolean_expression_in_synpred107_JPA2122);
                boolean_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:291:58: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred107_JPA2126);
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
        // JPA.g:292:4: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
        // JPA.g:292:4: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_enum_expression_in_synpred110_JPA2132);
        enum_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=88 && input.LA(1)<=89) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:292:31: ( enum_expression | all_or_any_expression )
        int alt113=2;
        int LA113_0 = input.LA(1);

        if ( ((LA113_0>=WORD && LA113_0<=NAMED_PARAMETER)||LA113_0==52||LA113_0==114) ) {
            alt113=1;
        }
        else if ( ((LA113_0>=85 && LA113_0<=87)) ) {
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
                // JPA.g:292:32: enum_expression
                {
                pushFollow(FOLLOW_enum_expression_in_synpred110_JPA2141);
                enum_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:292:50: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred110_JPA2145);
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
        // JPA.g:293:4: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
        // JPA.g:293:4: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_datetime_expression_in_synpred112_JPA2151);
        datetime_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_comparison_operator_in_synpred112_JPA2153);
        comparison_operator();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:293:44: ( datetime_expression | all_or_any_expression )
        int alt114=2;
        int LA114_0 = input.LA(1);

        if ( ((LA114_0>=AVG && LA114_0<=COUNT)||(LA114_0>=WORD && LA114_0<=NAMED_PARAMETER)||LA114_0==52||(LA114_0>=102 && LA114_0<=104)||LA114_0==114) ) {
            alt114=1;
        }
        else if ( ((LA114_0>=85 && LA114_0<=87)) ) {
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
                // JPA.g:293:45: datetime_expression
                {
                pushFollow(FOLLOW_datetime_expression_in_synpred112_JPA2156);
                datetime_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:293:67: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred112_JPA2160);
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
        // JPA.g:294:4: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
        // JPA.g:294:4: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_entity_expression_in_synpred115_JPA2166);
        entity_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=88 && input.LA(1)<=89) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:294:35: ( entity_expression | all_or_any_expression )
        int alt115=2;
        int LA115_0 = input.LA(1);

        if ( ((LA115_0>=WORD && LA115_0<=NAMED_PARAMETER)||LA115_0==114) ) {
            alt115=1;
        }
        else if ( ((LA115_0>=85 && LA115_0<=87)) ) {
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
                // JPA.g:294:36: entity_expression
                {
                pushFollow(FOLLOW_entity_expression_in_synpred115_JPA2177);
                entity_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:294:56: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred115_JPA2181);
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


    protected DFA30 dfa30 = new DFA30(this);
    protected DFA35 dfa35 = new DFA35(this);
    protected DFA47 dfa47 = new DFA47(this);
    protected DFA48 dfa48 = new DFA48(this);
    protected DFA58 dfa58 = new DFA58(this);
    protected DFA78 dfa78 = new DFA78(this);
    static final String DFA30_eotS =
        "\11\uffff";
    static final String DFA30_eofS =
        "\4\uffff\3\7\1\uffff\1\7";
    static final String DFA30_minS =
        "\1\71\1\30\1\uffff\1\65\3\25\1\uffff\1\25";
    static final String DFA30_maxS =
        "\1\71\1\164\1\uffff\1\131\3\137\1\uffff\1\137";
    static final String DFA30_acceptS =
        "\2\uffff\1\1\4\uffff\1\2\1\uffff";
    static final String DFA30_specialS =
        "\11\uffff}>";
    static final String[] DFA30_transitionS = {
            "\1\1",
            "\5\2\2\uffff\1\2\7\uffff\1\2\1\uffff\1\2\1\uffff\1\3\1\2\7"+
            "\uffff\1\2\10\uffff\2\2\1\uffff\2\2\6\uffff\4\2\10\uffff\1\2"+
            "\13\uffff\16\2\3\uffff\4\2",
            "",
            "\1\4\7\uffff\1\2\24\uffff\1\2\5\uffff\2\2",
            "\1\7\12\uffff\1\7\12\uffff\1\6\12\uffff\1\2\3\uffff\1\5\1"+
            "\uffff\1\7\1\2\2\uffff\2\2\12\uffff\2\2\1\uffff\1\2\2\uffff"+
            "\1\2\5\uffff\10\2",
            "\1\7\12\uffff\1\7\24\uffff\1\10\1\2\3\uffff\3\7\1\2\2\uffff"+
            "\2\2\12\uffff\2\2\1\uffff\1\2\2\uffff\1\2\5\uffff\10\2",
            "\1\7\12\uffff\1\7\24\uffff\1\10\1\2\3\uffff\1\7\1\uffff\1"+
            "\7\1\2\2\uffff\2\2\12\uffff\2\2\1\uffff\1\2\2\uffff\1\2\5\uffff"+
            "\10\2",
            "",
            "\1\7\12\uffff\1\7\12\uffff\1\6\12\uffff\1\2\3\uffff\1\5\1"+
            "\uffff\1\7\1\2\2\uffff\2\2\12\uffff\2\2\1\uffff\1\2\2\uffff"+
            "\1\2\5\uffff\10\2"
    };

    static final short[] DFA30_eot = DFA.unpackEncodedString(DFA30_eotS);
    static final short[] DFA30_eof = DFA.unpackEncodedString(DFA30_eofS);
    static final char[] DFA30_min = DFA.unpackEncodedStringToUnsignedChars(DFA30_minS);
    static final char[] DFA30_max = DFA.unpackEncodedStringToUnsignedChars(DFA30_maxS);
    static final short[] DFA30_accept = DFA.unpackEncodedString(DFA30_acceptS);
    static final short[] DFA30_special = DFA.unpackEncodedString(DFA30_specialS);
    static final short[][] DFA30_transition;

    static {
        int numStates = DFA30_transitionS.length;
        DFA30_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA30_transition[i] = DFA.unpackEncodedString(DFA30_transitionS[i]);
        }
    }

    class DFA30 extends DFA {

        public DFA30(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 30;
            this.eot = DFA30_eot;
            this.eof = DFA30_eof;
            this.min = DFA30_min;
            this.max = DFA30_max;
            this.accept = DFA30_accept;
            this.special = DFA30_special;
            this.transition = DFA30_transition;
        }
        public String getDescription() {
            return "162:1: where_clause : (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) );";
        }
    }
    static final String DFA35_eotS =
        "\7\uffff";
    static final String DFA35_eofS =
        "\2\uffff\2\4\2\uffff\1\4";
    static final String DFA35_minS =
        "\1\53\1\65\2\26\2\uffff\1\26";
    static final String DFA35_maxS =
        "\1\53\1\65\1\72\1\65\2\uffff\1\72";
    static final String DFA35_acceptS =
        "\4\uffff\1\1\1\2\1\uffff";
    static final String DFA35_specialS =
        "\7\uffff}>";
    static final String[] DFA35_transitionS = {
            "\1\1",
            "\1\2",
            "\1\4\1\5\10\uffff\1\4\12\uffff\1\3\6\uffff\1\4\7\uffff\1\3",
            "\1\4\1\5\10\uffff\1\4\21\uffff\1\4\2\uffff\1\6",
            "",
            "",
            "\1\4\1\5\10\uffff\1\4\12\uffff\1\3\6\uffff\1\4\7\uffff\1\3"
    };

    static final short[] DFA35_eot = DFA.unpackEncodedString(DFA35_eotS);
    static final short[] DFA35_eof = DFA.unpackEncodedString(DFA35_eofS);
    static final char[] DFA35_min = DFA.unpackEncodedStringToUnsignedChars(DFA35_minS);
    static final char[] DFA35_max = DFA.unpackEncodedStringToUnsignedChars(DFA35_maxS);
    static final short[] DFA35_accept = DFA.unpackEncodedString(DFA35_acceptS);
    static final short[] DFA35_special = DFA.unpackEncodedString(DFA35_specialS);
    static final short[][] DFA35_transition;

    static {
        int numStates = DFA35_transitionS.length;
        DFA35_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA35_transition[i] = DFA.unpackEncodedString(DFA35_transitionS[i]);
        }
    }

    class DFA35 extends DFA {

        public DFA35(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 35;
            this.eot = DFA35_eot;
            this.eof = DFA35_eof;
            this.min = DFA35_min;
            this.max = DFA35_max;
            this.accept = DFA35_accept;
            this.special = DFA35_special;
            this.transition = DFA35_transition;
        }
        public String getDescription() {
            return "181:1: orderby_item : ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) );";
        }
    }
    static final String DFA47_eotS =
        "\41\uffff";
    static final String DFA47_eofS =
        "\41\uffff";
    static final String DFA47_minS =
        "\1\30\22\uffff\1\0\15\uffff";
    static final String DFA47_maxS =
        "\1\164\22\uffff\1\0\15\uffff";
    static final String DFA47_acceptS =
        "\1\uffff\1\1\36\uffff\1\2";
    static final String DFA47_specialS =
        "\23\uffff\1\0\15\uffff}>";
    static final String[] DFA47_transitionS = {
            "\5\1\2\uffff\1\23\7\uffff\1\1\1\uffff\1\1\1\uffff\2\1\7\uffff"+
            "\1\1\10\uffff\2\1\1\uffff\2\1\6\uffff\4\1\10\uffff\1\1\13\uffff"+
            "\16\1\3\uffff\4\1",
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

    static final short[] DFA47_eot = DFA.unpackEncodedString(DFA47_eotS);
    static final short[] DFA47_eof = DFA.unpackEncodedString(DFA47_eofS);
    static final char[] DFA47_min = DFA.unpackEncodedStringToUnsignedChars(DFA47_minS);
    static final char[] DFA47_max = DFA.unpackEncodedStringToUnsignedChars(DFA47_maxS);
    static final short[] DFA47_accept = DFA.unpackEncodedString(DFA47_acceptS);
    static final short[] DFA47_special = DFA.unpackEncodedString(DFA47_specialS);
    static final short[][] DFA47_transition;

    static {
        int numStates = DFA47_transitionS.length;
        DFA47_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA47_transition[i] = DFA.unpackEncodedString(DFA47_transitionS[i]);
        }
    }

    class DFA47 extends DFA {

        public DFA47(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 47;
            this.eot = DFA47_eot;
            this.eof = DFA47_eof;
            this.min = DFA47_min;
            this.max = DFA47_max;
            this.accept = DFA47_accept;
            this.special = DFA47_special;
            this.transition = DFA47_transition;
        }
        public String getDescription() {
            return "218:1: conditional_factor : ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 :
                        int LA47_19 = input.LA(1);


                        int index47_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred58_JPA()) ) {s = 1;}

                        else if ( (true) ) {s = 32;}


                        input.seek(index47_19);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 47, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA48_eotS =
        "\46\uffff";
    static final String DFA48_eofS =
        "\46\uffff";
    static final String DFA48_minS =
        "\1\30\14\0\1\uffff\13\0\15\uffff";
    static final String DFA48_maxS =
        "\1\164\14\0\1\uffff\13\0\15\uffff";
    static final String DFA48_acceptS =
        "\15\uffff\1\1\13\uffff\1\10\1\uffff\1\11\4\uffff\1\2\1\3\1\4\1"+
        "\5\1\6\1\7";
    static final String DFA48_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\uffff\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\15"+
        "\uffff}>";
    static final String[] DFA48_transitionS = {
            "\4\13\1\12\2\uffff\1\22\7\uffff\1\21\1\uffff\1\2\1\uffff\1"+
            "\1\1\4\7\uffff\1\14\10\uffff\1\31\1\33\1\uffff\2\17\6\uffff"+
            "\4\33\10\uffff\1\31\13\uffff\1\23\1\24\1\25\1\26\1\27\1\30\3"+
            "\16\1\5\1\6\1\7\1\10\1\11\3\uffff\1\20\1\3\2\15",
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
            return "222:1: simple_cond_expression : ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 :
                        int LA48_1 = input.LA(1);


                        int index48_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}

                        else if ( (synpred62_JPA()) ) {s = 34;}

                        else if ( (synpred63_JPA()) ) {s = 35;}

                        else if ( (synpred64_JPA()) ) {s = 36;}

                        else if ( (synpred65_JPA()) ) {s = 37;}


                        input.seek(index48_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 :
                        int LA48_2 = input.LA(1);


                        int index48_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}


                        input.seek(index48_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 :
                        int LA48_3 = input.LA(1);


                        int index48_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}

                        else if ( (synpred63_JPA()) ) {s = 35;}

                        else if ( (synpred65_JPA()) ) {s = 37;}


                        input.seek(index48_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 :
                        int LA48_4 = input.LA(1);


                        int index48_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}

                        else if ( (synpred63_JPA()) ) {s = 35;}

                        else if ( (synpred65_JPA()) ) {s = 37;}


                        input.seek(index48_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 :
                        int LA48_5 = input.LA(1);


                        int index48_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}


                        input.seek(index48_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 :
                        int LA48_6 = input.LA(1);


                        int index48_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}


                        input.seek(index48_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 :
                        int LA48_7 = input.LA(1);


                        int index48_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}


                        input.seek(index48_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 :
                        int LA48_8 = input.LA(1);


                        int index48_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}


                        input.seek(index48_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 :
                        int LA48_9 = input.LA(1);


                        int index48_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}


                        input.seek(index48_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 :
                        int LA48_10 = input.LA(1);


                        int index48_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}


                        input.seek(index48_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 :
                        int LA48_11 = input.LA(1);


                        int index48_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}


                        input.seek(index48_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 :
                        int LA48_12 = input.LA(1);


                        int index48_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}


                        input.seek(index48_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 :
                        int LA48_14 = input.LA(1);


                        int index48_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_14);
                        if ( s>=0 ) return s;
                        break;
                    case 13 :
                        int LA48_15 = input.LA(1);


                        int index48_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_15);
                        if ( s>=0 ) return s;
                        break;
                    case 14 :
                        int LA48_16 = input.LA(1);


                        int index48_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_16);
                        if ( s>=0 ) return s;
                        break;
                    case 15 :
                        int LA48_17 = input.LA(1);


                        int index48_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_17);
                        if ( s>=0 ) return s;
                        break;
                    case 16 :
                        int LA48_18 = input.LA(1);


                        int index48_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_18);
                        if ( s>=0 ) return s;
                        break;
                    case 17 :
                        int LA48_19 = input.LA(1);


                        int index48_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_19);
                        if ( s>=0 ) return s;
                        break;
                    case 18 :
                        int LA48_20 = input.LA(1);


                        int index48_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_20);
                        if ( s>=0 ) return s;
                        break;
                    case 19 :
                        int LA48_21 = input.LA(1);


                        int index48_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_21);
                        if ( s>=0 ) return s;
                        break;
                    case 20 :
                        int LA48_22 = input.LA(1);


                        int index48_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_22);
                        if ( s>=0 ) return s;
                        break;
                    case 21 :
                        int LA48_23 = input.LA(1);


                        int index48_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_23);
                        if ( s>=0 ) return s;
                        break;
                    case 22 :
                        int LA48_24 = input.LA(1);


                        int index48_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_JPA()) ) {s = 13;}

                        else if ( (synpred60_JPA()) ) {s = 32;}


                        input.seek(index48_24);
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
    static final String DFA58_eotS =
        "\30\uffff";
    static final String DFA58_eofS =
        "\30\uffff";
    static final String DFA58_minS =
        "\1\30\1\uffff\1\0\3\uffff\2\0\6\uffff\3\0\7\uffff";
    static final String DFA58_maxS =
        "\1\162\1\uffff\1\0\3\uffff\2\0\6\uffff\3\0\7\uffff";
    static final String DFA58_acceptS =
        "\1\uffff\1\1\17\uffff\1\2\5\uffff\1\3";
    static final String DFA58_specialS =
        "\2\uffff\1\0\3\uffff\1\1\1\2\6\uffff\1\3\1\4\1\5\7\uffff}>";
    static final String[] DFA58_transitionS = {
            "\4\17\1\16\2\uffff\1\1\7\uffff\1\1\1\uffff\1\21\1\uffff\1\2"+
            "\1\7\7\uffff\1\20\13\uffff\2\1\36\uffff\6\1\3\27\5\21\3\uffff"+
            "\1\1\1\6",
            "",
            "\1\uffff",
            "",
            "",
            "",
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

    static final short[] DFA58_eot = DFA.unpackEncodedString(DFA58_eotS);
    static final short[] DFA58_eof = DFA.unpackEncodedString(DFA58_eofS);
    static final char[] DFA58_min = DFA.unpackEncodedStringToUnsignedChars(DFA58_minS);
    static final char[] DFA58_max = DFA.unpackEncodedStringToUnsignedChars(DFA58_maxS);
    static final short[] DFA58_accept = DFA.unpackEncodedString(DFA58_acceptS);
    static final short[] DFA58_special = DFA.unpackEncodedString(DFA58_specialS);
    static final short[][] DFA58_transition;

    static {
        int numStates = DFA58_transitionS.length;
        DFA58_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA58_transition[i] = DFA.unpackEncodedString(DFA58_transitionS[i]);
        }
    }

    class DFA58 extends DFA {

        public DFA58(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 58;
            this.eot = DFA58_eot;
            this.eof = DFA58_eof;
            this.min = DFA58_min;
            this.max = DFA58_max;
            this.accept = DFA58_accept;
            this.special = DFA58_special;
            this.transition = DFA58_transition;
        }
        public String getDescription() {
            return "255:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 :
                        int LA58_2 = input.LA(1);


                        int index58_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}


                        input.seek(index58_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 :
                        int LA58_6 = input.LA(1);


                        int index58_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}


                        input.seek(index58_6);
                        if ( s>=0 ) return s;
                        break;
                    case 2 :
                        int LA58_7 = input.LA(1);


                        int index58_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}


                        input.seek(index58_7);
                        if ( s>=0 ) return s;
                        break;
                    case 3 :
                        int LA58_14 = input.LA(1);


                        int index58_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}


                        input.seek(index58_14);
                        if ( s>=0 ) return s;
                        break;
                    case 4 :
                        int LA58_15 = input.LA(1);


                        int index58_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}


                        input.seek(index58_15);
                        if ( s>=0 ) return s;
                        break;
                    case 5 :
                        int LA58_16 = input.LA(1);


                        int index58_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred84_JPA()) ) {s = 1;}

                        else if ( (synpred86_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}


                        input.seek(index58_16);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 58, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA78_eotS =
        "\33\uffff";
    static final String DFA78_eofS =
        "\33\uffff";
    static final String DFA78_minS =
        "\1\30\1\0\1\uffff\2\0\5\uffff\3\0\16\uffff";
    static final String DFA78_maxS =
        "\1\164\1\0\1\uffff\2\0\5\uffff\3\0\16\uffff";
    static final String DFA78_acceptS =
        "\2\uffff\1\1\12\uffff\1\2\1\4\1\6\11\uffff\1\3\1\5";
    static final String DFA78_specialS =
        "\1\uffff\1\0\1\uffff\1\1\1\2\5\uffff\1\3\1\4\1\5\16\uffff}>";
    static final String[] DFA78_transitionS = {
            "\4\13\1\12\2\uffff\1\17\7\uffff\1\17\1\uffff\1\2\1\uffff\1"+
            "\1\1\4\7\uffff\1\14\13\uffff\2\17\36\uffff\6\17\3\16\5\2\3\uffff"+
            "\1\17\1\3\2\15",
            "\1\uffff",
            "",
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

    static final short[] DFA78_eot = DFA.unpackEncodedString(DFA78_eotS);
    static final short[] DFA78_eof = DFA.unpackEncodedString(DFA78_eofS);
    static final char[] DFA78_min = DFA.unpackEncodedStringToUnsignedChars(DFA78_minS);
    static final char[] DFA78_max = DFA.unpackEncodedStringToUnsignedChars(DFA78_maxS);
    static final short[] DFA78_accept = DFA.unpackEncodedString(DFA78_acceptS);
    static final short[] DFA78_special = DFA.unpackEncodedString(DFA78_specialS);
    static final short[][] DFA78_transition;

    static {
        int numStates = DFA78_transitionS.length;
        DFA78_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA78_transition[i] = DFA.unpackEncodedString(DFA78_transitionS[i]);
        }
    }

    class DFA78 extends DFA {

        public DFA78(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 78;
            this.eot = DFA78_eot;
            this.eof = DFA78_eof;
            this.min = DFA78_min;
            this.max = DFA78_max;
            this.accept = DFA78_accept;
            this.special = DFA78_special;
            this.transition = DFA78_transition;
        }
        public String getDescription() {
            return "289:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 :
                        int LA78_1 = input.LA(1);


                        int index78_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred107_JPA()) ) {s = 13;}

                        else if ( (synpred110_JPA()) ) {s = 25;}

                        else if ( (synpred112_JPA()) ) {s = 14;}

                        else if ( (synpred115_JPA()) ) {s = 26;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index78_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 :
                        int LA78_3 = input.LA(1);


                        int index78_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred107_JPA()) ) {s = 13;}

                        else if ( (synpred110_JPA()) ) {s = 25;}

                        else if ( (synpred112_JPA()) ) {s = 14;}

                        else if ( (synpred115_JPA()) ) {s = 26;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index78_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 :
                        int LA78_4 = input.LA(1);


                        int index78_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred107_JPA()) ) {s = 13;}

                        else if ( (synpred110_JPA()) ) {s = 25;}

                        else if ( (synpred112_JPA()) ) {s = 14;}

                        else if ( (synpred115_JPA()) ) {s = 26;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index78_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 :
                        int LA78_10 = input.LA(1);


                        int index78_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred112_JPA()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index78_10);
                        if ( s>=0 ) return s;
                        break;
                    case 4 :
                        int LA78_11 = input.LA(1);


                        int index78_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred112_JPA()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index78_11);
                        if ( s>=0 ) return s;
                        break;
                    case 5 :
                        int LA78_12 = input.LA(1);


                        int index78_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_JPA()) ) {s = 2;}

                        else if ( (synpred107_JPA()) ) {s = 13;}

                        else if ( (synpred110_JPA()) ) {s = 25;}

                        else if ( (synpred112_JPA()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index78_12);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 78, _s, input);
            error(nvae);
            throw nvae;
        }
    }


    public static final BitSet FOLLOW_select_statement_in_ql_statement383 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_select_statement395 = new BitSet(new long[]{0x018008021F000000L});
    public static final BitSet FOLLOW_select_clause_in_select_statement397 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_from_clause_in_select_statement399 = new BitSet(new long[]{0x1600000000200002L});
    public static final BitSet FOLLOW_where_clause_in_select_statement402 = new BitSet(new long[]{0x1400000000200002L});
    public static final BitSet FOLLOW_groupby_clause_in_select_statement407 = new BitSet(new long[]{0x1000000000200002L});
    public static final BitSet FOLLOW_having_clause_in_select_statement412 = new BitSet(new long[]{0x1000000000000002L});
    public static final BitSet FOLLOW_orderby_clause_in_select_statement416 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_from_clause471 = new BitSet(new long[]{0x0010080000000000L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause473 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_50_in_from_clause476 = new BitSet(new long[]{0x0050080000000000L});
    public static final BitSet FOLLOW_identification_variable_or_collection_declaration_in_from_clause478 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_or_collection_declaration522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_or_collection_declaration526 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration535 = new BitSet(new long[]{0x0000003400000002L});
    public static final BitSet FOLLOW_join_in_identification_variable_declaration539 = new BitSet(new long[]{0x0000003400000002L});
    public static final BitSet FOLLOW_fetch_join_in_identification_variable_declaration543 = new BitSet(new long[]{0x0000003400000002L});
    public static final BitSet FOLLOW_range_variable_declaration_source_in_range_variable_declaration555 = new BitSet(new long[]{0x0008080000000000L});
    public static final BitSet FOLLOW_51_in_range_variable_declaration558 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_abstract_schema_name_in_range_variable_declaration_source589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_range_variable_declaration_source596 = new BitSet(new long[]{0x018008021F000000L});
    public static final BitSet FOLLOW_select_clause_in_range_variable_declaration_source598 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_from_clause_in_range_variable_declaration_source600 = new BitSet(new long[]{0x1600000100200000L});
    public static final BitSet FOLLOW_where_clause_in_range_variable_declaration_source603 = new BitSet(new long[]{0x1400000100200000L});
    public static final BitSet FOLLOW_groupby_clause_in_range_variable_declaration_source608 = new BitSet(new long[]{0x1000000100200000L});
    public static final BitSet FOLLOW_having_clause_in_range_variable_declaration_source613 = new BitSet(new long[]{0x1000000100000000L});
    public static final BitSet FOLLOW_orderby_clause_in_range_variable_declaration_source617 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_range_variable_declaration_source623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_spec_in_join677 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_join_association_path_expression_in_join679 = new BitSet(new long[]{0x0008080000000000L});
    public static final BitSet FOLLOW_51_in_join682 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_identification_variable_in_join686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_spec_in_fetch_join715 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_FETCH_in_fetch_join717 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_in_join_spec729 = new BitSet(new long[]{0x0000002800000000L});
    public static final BitSet FOLLOW_OUTER_in_join_spec733 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_INNER_in_join_spec739 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_JOIN_in_join_spec744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression753 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_join_association_path_expression755 = new BitSet(new long[]{0x0400080000000002L});
    public static final BitSet FOLLOW_field_in_join_association_path_expression758 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_join_association_path_expression759 = new BitSet(new long[]{0x0400080000000002L});
    public static final BitSet FOLLOW_field_in_join_association_path_expression763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_collection_member_declaration791 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration792 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_path_expression_in_collection_member_declaration794 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration796 = new BitSet(new long[]{0x0008080000000000L});
    public static final BitSet FOLLOW_51_in_collection_member_declaration799 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_path_expression829 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_path_expression831 = new BitSet(new long[]{0x0400080000000002L});
    public static final BitSet FOLLOW_field_in_path_expression834 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_path_expression835 = new BitSet(new long[]{0x0400080000000002L});
    public static final BitSet FOLLOW_field_in_path_expression840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_select_clause870 = new BitSet(new long[]{0x018008021F000000L});
    public static final BitSet FOLLOW_select_expression_in_select_clause874 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_50_in_select_clause877 = new BitSet(new long[]{0x018008021F000000L});
    public static final BitSet FOLLOW_select_expression_in_select_clause879 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_path_expression_in_select_expression915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_select_expression920 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_select_expression925 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_select_expression940 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_select_expression942 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_identification_variable_in_select_expression943 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_select_expression944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_expression_in_select_expression949 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_56_in_constructor_expression958 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_constructor_name_in_constructor_expression960 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_constructor_expression962 = new BitSet(new long[]{0x000008001F000000L});
    public static final BitSet FOLLOW_constructor_item_in_constructor_expression964 = new BitSet(new long[]{0x0004000100000000L});
    public static final BitSet FOLLOW_50_in_constructor_expression967 = new BitSet(new long[]{0x000008001F000000L});
    public static final BitSet FOLLOW_constructor_item_in_constructor_expression969 = new BitSet(new long[]{0x0004000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_constructor_expression973 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_constructor_item982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_constructor_item986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression995 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_aggregate_expression997 = new BitSet(new long[]{0x0000080200000000L});
    public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1000 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_path_expression_in_aggregate_expression1004 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COUNT_in_aggregate_expression1033 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1035 = new BitSet(new long[]{0x0000080200000000L});
    public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1038 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_identification_variable_in_aggregate_expression1042 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1044 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_aggregate_expression_function_name0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_57_in_where_clause1106 = new BitSet(new long[]{0x60101A809F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_conditional_expression_in_where_clause1108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_57_in_where_clause1124 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_path_expression_in_where_clause1126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_groupby_clause1147 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_59_in_groupby_clause1149 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_groupby_item_in_groupby_clause1151 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_50_in_groupby_clause1154 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_groupby_item_in_groupby_clause1156 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_path_expression_in_groupby_item1187 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_groupby_item1191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HAVING_in_having_clause1200 = new BitSet(new long[]{0x60101A809F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_conditional_expression_in_having_clause1202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_orderby_clause1211 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_59_in_orderby_clause1213 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_orderby_item_in_orderby_clause1215 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_50_in_orderby_clause1218 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_orderby_item_in_orderby_clause1220 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_path_expression_in_orderby_item1249 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_ASC_in_orderby_item1252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_orderby_item1278 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_DESC_in_orderby_item1280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_subquery1306 = new BitSet(new long[]{0x000008021F000000L});
    public static final BitSet FOLLOW_simple_select_clause_in_subquery1308 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_subquery_from_clause_in_subquery1310 = new BitSet(new long[]{0x0600000100200000L});
    public static final BitSet FOLLOW_where_clause_in_subquery1313 = new BitSet(new long[]{0x0400000100200000L});
    public static final BitSet FOLLOW_groupby_clause_in_subquery1318 = new BitSet(new long[]{0x0000000100200000L});
    public static final BitSet FOLLOW_having_clause_in_subquery1323 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_subquery1329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_subquery_from_clause1372 = new BitSet(new long[]{0x0050080000000000L});
    public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1374 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_50_in_subquery_from_clause1377 = new BitSet(new long[]{0x0050080000000000L});
    public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1379 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_association_path_expression_in_subselect_identification_variable_declaration1416 = new BitSet(new long[]{0x0008080000000000L});
    public static final BitSet FOLLOW_51_in_subselect_identification_variable_declaration1419 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration1423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_declaration_in_subselect_identification_variable_declaration1428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_association_path_expression1437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause1447 = new BitSet(new long[]{0x000008021F000000L});
    public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause1451 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_simple_select_expression1482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression1487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_simple_select_expression1492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditional_term_in_conditional_expression1502 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_OR_in_conditional_expression1506 = new BitSet(new long[]{0x60101A809F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_conditional_term_in_conditional_expression1508 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_conditional_factor_in_conditional_term1520 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_AND_in_conditional_term1524 = new BitSet(new long[]{0x60101A809F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_conditional_factor_in_conditional_term1526 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_61_in_conditional_factor1539 = new BitSet(new long[]{0x60101A809F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_simple_cond_expression_in_conditional_factor1543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_conditional_factor1565 = new BitSet(new long[]{0x60101A809F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_conditional_expression_in_conditional_factor1566 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_conditional_factor1567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression1576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_expression_in_simple_cond_expression1581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_expression_in_simple_cond_expression1586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_expression_in_simple_cond_expression1591 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression1596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression1601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression1606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression1611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression1616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression1628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression1636 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression1644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression1652 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression1660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_date_between_macro_expression1672 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression1674 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression1676 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_date_between_macro_expression1678 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_date_between_macro_expression1680 = new BitSet(new long[]{0x0004000000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression1683 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_date_between_macro_expression1691 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_date_between_macro_expression1695 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_date_between_macro_expression1697 = new BitSet(new long[]{0x0004000000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression1700 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_date_between_macro_expression1708 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_date_between_macro_expression1712 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000FCL});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression1714 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression1737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_date_before_macro_expression1749 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression1751 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression1753 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_date_before_macro_expression1755 = new BitSet(new long[]{0x0000180000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression1758 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression1762 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression1765 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_date_after_macro_expression1777 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression1779 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression1781 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_date_after_macro_expression1783 = new BitSet(new long[]{0x0000180000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression1786 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression1790 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression1793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_date_equals_macro_expression1805 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression1807 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression1809 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_date_equals_macro_expression1811 = new BitSet(new long[]{0x0000180000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression1814 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression1818 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression1821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_75_in_date_today_macro_expression1833 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression1835 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression1837 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression1839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1848 = new BitSet(new long[]{0x2000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_61_in_between_expression1851 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_between_expression1855 = new BitSet(new long[]{0x00101A809F000000L,0x001E3FFF00000003L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1857 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression1859 = new BitSet(new long[]{0x00101A809F000000L,0x001E3FFF00000003L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1861 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1866 = new BitSet(new long[]{0x2000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_61_in_between_expression1869 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_between_expression1873 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1875 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression1877 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1884 = new BitSet(new long[]{0x2000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_61_in_between_expression1887 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_between_expression1891 = new BitSet(new long[]{0x00101A001F000000L,0x00043FC000000000L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1893 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression1895 = new BitSet(new long[]{0x00101A001F000000L,0x00043FC000000000L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1897 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_in_expression1906 = new BitSet(new long[]{0x2040000000000000L});
    public static final BitSet FOLLOW_61_in_in_expression1909 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_in_expression1913 = new BitSet(new long[]{0x00101A009F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_in_expression_right_part_in_in_expression1915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_in_expression_right_part1924 = new BitSet(new long[]{0x0000180000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_in_item_in_in_expression_right_part1926 = new BitSet(new long[]{0x0004000100000000L});
    public static final BitSet FOLLOW_50_in_in_expression_right_part1929 = new BitSet(new long[]{0x0000180000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_in_item_in_in_expression_right_part1931 = new BitSet(new long[]{0x0004000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_in_expression_right_part1935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_in_expression_right_part1940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_in_item1949 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_in_item1954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_like_expression1963 = new BitSet(new long[]{0x2000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_61_in_like_expression1966 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_like_expression1970 = new BitSet(new long[]{0x0000180000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_pattern_value_in_like_expression1973 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_input_parameter_in_like_expression1977 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_78_in_like_expression1980 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_ESCAPE_CHARACTER_in_like_expression1982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_null_comparison_expression1994 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression1998 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_79_in_null_comparison_expression2001 = new BitSet(new long[]{0x2000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_61_in_null_comparison_expression2004 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_80_in_null_comparison_expression2008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression2017 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_79_in_empty_collection_comparison_expression2019 = new BitSet(new long[]{0x2000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_61_in_empty_collection_comparison_expression2022 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_81_in_empty_collection_comparison_expression2026 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_collection_member_expression2035 = new BitSet(new long[]{0x2000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_61_in_collection_member_expression2038 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_collection_member_expression2042 = new BitSet(new long[]{0x0000080000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_collection_member_expression2045 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_path_expression_in_collection_member_expression2049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_exists_expression2059 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_84_in_exists_expression2063 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_subquery_in_exists_expression2065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_all_or_any_expression2074 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_subquery_in_all_or_any_expression2087 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_comparison_expression2096 = new BitSet(new long[]{0x0000000000000000L,0x000000003F000000L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2098 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_string_expression_in_comparison_expression2101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2105 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2111 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_comparison_expression2113 = new BitSet(new long[]{0x00101A001F000000L,0x001C3E0000E00000L});
    public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_expression_in_comparison_expression2132 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_comparison_expression2134 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_enum_expression_in_comparison_expression2141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2151 = new BitSet(new long[]{0x0000000000000000L,0x000000003F000000L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2153 = new BitSet(new long[]{0x00101A001F000000L,0x00043FC000E00000L});
    public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_comparison_expression2166 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_comparison_expression2168 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_entity_expression_in_comparison_expression2177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression2187 = new BitSet(new long[]{0x0000000000000000L,0x000000003F000000L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2189 = new BitSet(new long[]{0x00101A809F000000L,0x001E3FFF00E00003L});
    public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression2192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_comparison_operator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_arithmetic_expression2240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_arithmetic_expression2245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_term_in_simple_arithmetic_expression2255 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_set_in_simple_arithmetic_expression2259 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_arithmetic_term_in_simple_arithmetic_expression2269 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term2281 = new BitSet(new long[]{0x0000000000000002L,0x00000000C0000000L});
    public static final BitSet FOLLOW_set_in_arithmetic_term2285 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term2295 = new BitSet(new long[]{0x0000000000000002L,0x00000000C0000000L});
    public static final BitSet FOLLOW_set_in_arithmetic_factor2306 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor2317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_arithmetic_primary2326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary2331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary2336 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_arithmetic_primary2337 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary2338 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary2343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary2348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary2353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_primary_in_string_expression2362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_string_expression2366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_string_primary2375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_string_primary2380 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_string_primary2385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_strings_in_string_primary2390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_string_primary2395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_primary_in_datetime_expression2404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_datetime_expression2409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_datetime_primary2418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_datetime_primary2423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_primary2428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_datetime_primary2433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_primary_in_boolean_expression2442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_boolean_expression2447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_boolean_primary2456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_literal_in_boolean_primary2461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_boolean_primary2466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_primary_in_enum_expression2475 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_enum_expression2480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_enum_primary2489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_literal_in_enum_primary2494 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_enum_primary2499 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_entity_expression2508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression2513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression2522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression2527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_96_in_functions_returning_numerics2536 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2538 = new BitSet(new long[]{0x00001A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics2539 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_functions_returning_numerics2545 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2547 = new BitSet(new long[]{0x00001A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics2548 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_functions_returning_numerics2549 = new BitSet(new long[]{0x00001A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics2551 = new BitSet(new long[]{0x0004000100000000L});
    public static final BitSet FOLLOW_50_in_functions_returning_numerics2553 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2555 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_functions_returning_numerics2563 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2565 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2566 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_functions_returning_numerics2572 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2574 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2575 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_functions_returning_numerics2581 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2583 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2584 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_functions_returning_numerics2585 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2587 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_101_in_functions_returning_numerics2593 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2595 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics2596 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_functions_returning_datetime0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_functions_returning_strings2625 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2627 = new BitSet(new long[]{0x00001A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2628 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_functions_returning_strings2629 = new BitSet(new long[]{0x00001A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2631 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_106_in_functions_returning_strings2637 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2639 = new BitSet(new long[]{0x00001A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2640 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_functions_returning_strings2641 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2642 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_functions_returning_strings2643 = new BitSet(new long[]{0x000018809F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2645 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_107_in_functions_returning_strings2651 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2653 = new BitSet(new long[]{0x00021E001F000000L,0x0005FE0000000000L});
    public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings2656 = new BitSet(new long[]{0x0002040000000000L});
    public static final BitSet FOLLOW_TRIM_CHARACTER_in_functions_returning_strings2661 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_functions_returning_strings2665 = new BitSet(new long[]{0x00001A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2669 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_functions_returning_strings2675 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2677 = new BitSet(new long[]{0x00001A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2678 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2679 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_109_in_functions_returning_strings2684 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2686 = new BitSet(new long[]{0x00001A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2687 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_trim_specification0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_abstract_schema_name2719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_pattern_value2729 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_113_in_numeric_literal2740 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal2744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_114_in_input_parameter2754 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_input_parameter2756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter2776 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_literal2798 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_constructor_name2807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_enum_literal2848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_boolean_literal0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_field0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_identification_variable2889 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_parameter_name2900 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_53_in_parameter_name2903 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_WORD_in_parameter_name2906 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_field_in_synpred20_JPA763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_field_in_synpred23_JPA840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_synpred57_JPA1539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_synpred58_JPA1539 = new BitSet(new long[]{0x60101A809F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_simple_cond_expression_in_synpred58_JPA1543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_expression_in_synpred59_JPA1576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_expression_in_synpred60_JPA1581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_expression_in_synpred61_JPA1586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_expression_in_synpred62_JPA1591 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_comparison_expression_in_synpred63_JPA1596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred64_JPA1601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_expression_in_synpred65_JPA1606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred84_JPA1848 = new BitSet(new long[]{0x2000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_61_in_synpred84_JPA1851 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_synpred84_JPA1855 = new BitSet(new long[]{0x00101A809F000000L,0x001E3FFF00000003L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred84_JPA1857 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_synpred84_JPA1859 = new BitSet(new long[]{0x00101A809F000000L,0x001E3FFF00000003L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred84_JPA1861 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_synpred86_JPA1866 = new BitSet(new long[]{0x2000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_61_in_synpred86_JPA1869 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_synpred86_JPA1873 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_expression_in_synpred86_JPA1875 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_synpred86_JPA1877 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_expression_in_synpred86_JPA1879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_synpred104_JPA2096 = new BitSet(new long[]{0x0000000000000000L,0x000000003F000000L});
    public static final BitSet FOLLOW_comparison_operator_in_synpred104_JPA2098 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_string_expression_in_synpred104_JPA2101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred104_JPA2105 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_expression_in_synpred107_JPA2111 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_synpred107_JPA2113 = new BitSet(new long[]{0x00101A001F000000L,0x001C3E0000E00000L});
    public static final BitSet FOLLOW_boolean_expression_in_synpred107_JPA2122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred107_JPA2126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_expression_in_synpred110_JPA2132 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_synpred110_JPA2134 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_enum_expression_in_synpred110_JPA2141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred110_JPA2145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_synpred112_JPA2151 = new BitSet(new long[]{0x0000000000000000L,0x000000003F000000L});
    public static final BitSet FOLLOW_comparison_operator_in_synpred112_JPA2153 = new BitSet(new long[]{0x00101A001F000000L,0x00043FC000E00000L});
    public static final BitSet FOLLOW_datetime_expression_in_synpred112_JPA2156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred112_JPA2160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_synpred115_JPA2166 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_synpred115_JPA2168 = new BitSet(new long[]{0x00101A001F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_entity_expression_in_synpred115_JPA2177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred115_JPA2181 = new BitSet(new long[]{0x0000000000000002L});

}