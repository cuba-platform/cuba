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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "T_SELECTED_ITEMS", "T_SELECTED_ITEM", "T_SOURCES", "T_SOURCE", "T_SELECTED_FIELD", "T_SELECTED_ENTITY", "T_ID_VAR", "T_JOIN_VAR", "T_COLLECTION_MEMBER", "T_QUERY", "T_CONDITION", "T_SIMPLE_CONDITION", "T_PARAMETER", "T_GROUP_BY", "T_ORDER_BY", "T_ORDER_BY_FIELD", "T_AGGREGATE_EXPR", "HAVING", "ASC", "DESC", "AVG", "MAX", "MIN", "SUM", "COUNT", "OR", "AND", "LPAREN", "RPAREN", "DISTINCT", "JOIN", "FETCH", "INT_NUMERAL", "ESCAPE_CHARACTER", "STRINGLITERAL", "TRIM_CHARACTER", "WORD", "NAMED_PARAMETER", "WS", "COMMENT", "LINE_COMMENT", "'SELECT'", "'FROM'", "','", "'AS'", "'(SELECT'", "'LEFT'", "'OUTER'", "'INNER'", "'.'", "'IN'", "'OBJECT'", "'NEW'", "'WHERE'", "'GROUP'", "'BY'", "'ORDER'", "'NOT'", "'@BETWEEN'", "'NOW'", "'+'", "'-'", "'YEAR'", "'MONTH'", "'DAY'", "'HOUR'", "'MINUTE'", "'SECOND'", "'@DATEBEFORE'", "'@DATEAFTER'", "'@DATEEQUALS'", "'@TODAY'", "'BETWEEN'", "'LIKE'", "'ESCAPE'", "'IS'", "'NULL'", "'EMPTY'", "'MEMBER'", "'OF'", "'EXISTS'", "'ALL'", "'ANY'", "'SOME'", "'='", "'<>'", "'>'", "'>='", "'<'", "'<='", "'*'", "'/'", "'LENGTH'", "'LOCATE'", "'ABS'", "'SQRT'", "'MOD'", "'SIZE'", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'CONCAT'", "'SUBSTRING'", "'TRIM'", "'LOWER'", "'UPPER'", "'LEADING'", "'TRAILING'", "'BOTH'", "'0x'", "'?'", "'true'", "'false'"
    };
    public static final int T_JOIN_VAR=11;
    public static final int T_AGGREGATE_EXPR=20;
    public static final int COUNT=28;
    public static final int T_ORDER_BY=18;
    public static final int EOF=-1;
    public static final int WORD=40;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int RPAREN=32;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__90=90;
    public static final int TRIM_CHARACTER=39;
    public static final int T_SELECTED_ITEM=5;
    public static final int COMMENT=43;
    public static final int T__99=99;
    public static final int T__98=98;
    public static final int T__97=97;
    public static final int T__96=96;
    public static final int T_QUERY=13;
    public static final int T__95=95;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int ASC=22;
    public static final int T__83=83;
    public static final int LINE_COMMENT=44;
    public static final int T__85=85;
    public static final int T__84=84;
    public static final int T__87=87;
    public static final int T__86=86;
    public static final int T__89=89;
    public static final int T__88=88;
    public static final int WS=42;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int FETCH=35;
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
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T_SOURCE=7;
    public static final int T__116=116;
    public static final int T_ID_VAR=10;
    public static final int T_SIMPLE_CONDITION=15;
    public static final int T__114=114;
    public static final int T__115=115;
    public static final int MAX=25;
    public static final int AND=30;
    public static final int SUM=27;
    public static final int T__61=61;
    public static final int T__60=60;
    public static final int LPAREN=31;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
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
    public static final int T__50=50;
    public static final int T_GROUP_BY=17;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T_CONDITION=14;
    public static final int T__45=45;
    public static final int T_SELECTED_ENTITY=9;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int HAVING=21;
    public static final int T__102=102;
    public static final int T__101=101;
    public static final int MIN=26;
    public static final int T__100=100;
    public static final int T_PARAMETER=16;
    public static final int JOIN=34;
    public static final int NAMED_PARAMETER=41;
    public static final int ESCAPE_CHARACTER=37;
    public static final int INT_NUMERAL=36;
    public static final int STRINGLITERAL=38;
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
    // JPA.g:78:1: ql_statement : select_statement ;
    public final JPAParser.ql_statement_return ql_statement() throws RecognitionException {
        JPAParser.ql_statement_return retval = new JPAParser.ql_statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.select_statement_return select_statement1 = null;



        try {
            // JPA.g:79:2: ( select_statement )
            // JPA.g:79:4: select_statement
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_select_statement_in_ql_statement350);
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
    // JPA.g:81:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
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
        RewriteRuleTokenStream stream_45=new RewriteRuleTokenStream(adaptor,"token 45");
        RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");
        try {
            // JPA.g:82:2: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
            // JPA.g:82:5: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )?
            {
            sl=(Token)match(input,45,FOLLOW_45_in_select_statement362); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_45.add(sl);

            pushFollow(FOLLOW_select_clause_in_select_statement364);
            select_clause2=select_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_select_clause.add(select_clause2.getTree());
            pushFollow(FOLLOW_from_clause_in_select_statement366);
            from_clause3=from_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_from_clause.add(from_clause3.getTree());
            // JPA.g:82:43: ( where_clause )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==57) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // JPA.g:82:44: where_clause
                    {
                    pushFollow(FOLLOW_where_clause_in_select_statement369);
                    where_clause4=where_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_where_clause.add(where_clause4.getTree());

                    }
                    break;

            }

            // JPA.g:82:59: ( groupby_clause )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==58) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // JPA.g:82:60: groupby_clause
                    {
                    pushFollow(FOLLOW_groupby_clause_in_select_statement374);
                    groupby_clause5=groupby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause5.getTree());

                    }
                    break;

            }

            // JPA.g:82:77: ( having_clause )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==HAVING) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // JPA.g:82:78: having_clause
                    {
                    pushFollow(FOLLOW_having_clause_in_select_statement379);
                    having_clause6=having_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_having_clause.add(having_clause6.getTree());

                    }
                    break;

            }

            // JPA.g:82:93: ( orderby_clause )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==60) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // JPA.g:82:94: orderby_clause
                    {
                    pushFollow(FOLLOW_orderby_clause_in_select_statement383);
                    orderby_clause7=orderby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause7.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: from_clause, select_clause, having_clause, groupby_clause, where_clause, orderby_clause
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 83:3: -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
            {
                // JPA.g:83:6: ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, sl), root_1);

                // JPA.g:83:32: ( select_clause )?
                if ( stream_select_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_select_clause.nextTree());

                }
                stream_select_clause.reset();
                adaptor.addChild(root_1, stream_from_clause.nextTree());
                // JPA.g:83:61: ( where_clause )?
                if ( stream_where_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_where_clause.nextTree());

                }
                stream_where_clause.reset();
                // JPA.g:83:77: ( groupby_clause )?
                if ( stream_groupby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                }
                stream_groupby_clause.reset();
                // JPA.g:83:95: ( having_clause )?
                if ( stream_having_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_having_clause.nextTree());

                }
                stream_having_clause.reset();
                // JPA.g:83:112: ( orderby_clause )?
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
    // JPA.g:85:1: from_clause : fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )* -> ^( T_SOURCES[$fr] ^( T_SOURCE identification_variable_declaration ) ( ^( T_SOURCE identification_variable_or_collection_declaration ) )* ) ;
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
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
        RewriteRuleSubtreeStream stream_identification_variable_or_collection_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_or_collection_declaration");
        RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
        try {
            // JPA.g:86:2: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )* -> ^( T_SOURCES[$fr] ^( T_SOURCE identification_variable_declaration ) ( ^( T_SOURCE identification_variable_or_collection_declaration ) )* ) )
            // JPA.g:86:4: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )*
            {
            fr=(Token)match(input,46,FOLLOW_46_in_from_clause438); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_46.add(fr);

            pushFollow(FOLLOW_identification_variable_declaration_in_from_clause440);
            identification_variable_declaration8=identification_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration8.getTree());
            // JPA.g:86:50: ( ',' identification_variable_or_collection_declaration )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==47) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // JPA.g:86:51: ',' identification_variable_or_collection_declaration
            	    {
            	    char_literal9=(Token)match(input,47,FOLLOW_47_in_from_clause443); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_47.add(char_literal9);

            	    pushFollow(FOLLOW_identification_variable_or_collection_declaration_in_from_clause445);
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
            // elements: identification_variable_declaration, identification_variable_or_collection_declaration
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 87:2: -> ^( T_SOURCES[$fr] ^( T_SOURCE identification_variable_declaration ) ( ^( T_SOURCE identification_variable_or_collection_declaration ) )* )
            {
                // JPA.g:87:5: ^( T_SOURCES[$fr] ^( T_SOURCE identification_variable_declaration ) ( ^( T_SOURCE identification_variable_or_collection_declaration ) )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);

                // JPA.g:87:32: ^( T_SOURCE identification_variable_declaration )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_2);

                adaptor.addChild(root_2, stream_identification_variable_declaration.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                // JPA.g:87:101: ( ^( T_SOURCE identification_variable_or_collection_declaration ) )*
                while ( stream_identification_variable_or_collection_declaration.hasNext() ) {
                    // JPA.g:87:101: ^( T_SOURCE identification_variable_or_collection_declaration )
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
    // JPA.g:89:1: identification_variable_or_collection_declaration : ( identification_variable_declaration | collection_member_declaration );
    public final JPAParser.identification_variable_or_collection_declaration_return identification_variable_or_collection_declaration() throws RecognitionException {
        JPAParser.identification_variable_or_collection_declaration_return retval = new JPAParser.identification_variable_or_collection_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.identification_variable_declaration_return identification_variable_declaration11 = null;

        JPAParser.collection_member_declaration_return collection_member_declaration12 = null;



        try {
            // JPA.g:90:5: ( identification_variable_declaration | collection_member_declaration )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WORD||LA6_0==49) ) {
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
                    // JPA.g:90:7: identification_variable_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_or_collection_declaration489);
                    identification_variable_declaration11=identification_variable_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration11.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:90:45: collection_member_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_or_collection_declaration493);
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
    // JPA.g:92:1: identification_variable_declaration : range_variable_declaration ( join | fetch_join )* ;
    public final JPAParser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
        JPAParser.identification_variable_declaration_return retval = new JPAParser.identification_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.range_variable_declaration_return range_variable_declaration13 = null;

        JPAParser.join_return join14 = null;

        JPAParser.fetch_join_return fetch_join15 = null;



        try {
            // JPA.g:93:2: ( range_variable_declaration ( join | fetch_join )* )
            // JPA.g:93:4: range_variable_declaration ( join | fetch_join )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration502);
            range_variable_declaration13=range_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, range_variable_declaration13.getTree());
            // JPA.g:93:31: ( join | fetch_join )*
            loop7:
            do {
                int alt7=3;
                switch ( input.LA(1) ) {
                case 50:
                    {
                    int LA7_2 = input.LA(2);

                    if ( (LA7_2==51) ) {
                        int LA7_5 = input.LA(3);

                        if ( (LA7_5==JOIN) ) {
                            int LA7_4 = input.LA(4);

                            if ( (LA7_4==FETCH) ) {
                                alt7=2;
                            }
                            else if ( (LA7_4==WORD) ) {
                                alt7=1;
                            }


                        }


                    }
                    else if ( (LA7_2==JOIN) ) {
                        int LA7_4 = input.LA(3);

                        if ( (LA7_4==FETCH) ) {
                            alt7=2;
                        }
                        else if ( (LA7_4==WORD) ) {
                            alt7=1;
                        }


                    }


                    }
                    break;
                case 52:
                    {
                    int LA7_3 = input.LA(2);

                    if ( (LA7_3==JOIN) ) {
                        int LA7_4 = input.LA(3);

                        if ( (LA7_4==FETCH) ) {
                            alt7=2;
                        }
                        else if ( (LA7_4==WORD) ) {
                            alt7=1;
                        }


                    }


                    }
                    break;
                case JOIN:
                    {
                    int LA7_4 = input.LA(2);

                    if ( (LA7_4==FETCH) ) {
                        alt7=2;
                    }
                    else if ( (LA7_4==WORD) ) {
                        alt7=1;
                    }


                    }
                    break;

                }

                switch (alt7) {
            	case 1 :
            	    // JPA.g:93:33: join
            	    {
            	    pushFollow(FOLLOW_join_in_identification_variable_declaration506);
            	    join14=join();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, join14.getTree());

            	    }
            	    break;
            	case 2 :
            	    // JPA.g:93:40: fetch_join
            	    {
            	    pushFollow(FOLLOW_fetch_join_in_identification_variable_declaration510);
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
    // JPA.g:95:1: range_variable_declaration : range_variable_declaration_source ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source ) ;
    public final JPAParser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
        JPAParser.range_variable_declaration_return retval = new JPAParser.range_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal17=null;
        JPAParser.range_variable_declaration_source_return range_variable_declaration_source16 = null;

        JPAParser.identification_variable_return identification_variable18 = null;


        Object string_literal17_tree=null;
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_range_variable_declaration_source=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration_source");
        try {
            // JPA.g:96:2: ( range_variable_declaration_source ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source ) )
            // JPA.g:96:4: range_variable_declaration_source ( 'AS' )? identification_variable
            {
            pushFollow(FOLLOW_range_variable_declaration_source_in_range_variable_declaration522);
            range_variable_declaration_source16=range_variable_declaration_source();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_range_variable_declaration_source.add(range_variable_declaration_source16.getTree());
            // JPA.g:96:38: ( 'AS' )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==48) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // JPA.g:96:39: 'AS'
                    {
                    string_literal17=(Token)match(input,48,FOLLOW_48_in_range_variable_declaration525); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_48.add(string_literal17);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_range_variable_declaration529);
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
            // 97:4: -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source )
            {
                // JPA.g:97:7: ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source )
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
    // JPA.g:100:1: range_variable_declaration_source : ( abstract_schema_name | lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')' -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) );
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
        RewriteRuleTokenStream stream_49=new RewriteRuleTokenStream(adaptor,"token 49");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");
        try {
            // JPA.g:101:2: ( abstract_schema_name | lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')' -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==WORD) ) {
                alt13=1;
            }
            else if ( (LA13_0==49) ) {
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
                    // JPA.g:101:4: abstract_schema_name
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_abstract_schema_name_in_range_variable_declaration_source556);
                    abstract_schema_name19=abstract_schema_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, abstract_schema_name19.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:102:4: lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')'
                    {
                    lp=(Token)match(input,49,FOLLOW_49_in_range_variable_declaration_source563); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_49.add(lp);

                    pushFollow(FOLLOW_select_clause_in_range_variable_declaration_source565);
                    select_clause20=select_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_select_clause.add(select_clause20.getTree());
                    pushFollow(FOLLOW_from_clause_in_range_variable_declaration_source567);
                    from_clause21=from_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_from_clause.add(from_clause21.getTree());
                    // JPA.g:102:43: ( where_clause )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==57) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // JPA.g:102:44: where_clause
                            {
                            pushFollow(FOLLOW_where_clause_in_range_variable_declaration_source570);
                            where_clause22=where_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_where_clause.add(where_clause22.getTree());

                            }
                            break;

                    }

                    // JPA.g:102:59: ( groupby_clause )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==58) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // JPA.g:102:60: groupby_clause
                            {
                            pushFollow(FOLLOW_groupby_clause_in_range_variable_declaration_source575);
                            groupby_clause23=groupby_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause23.getTree());

                            }
                            break;

                    }

                    // JPA.g:102:77: ( having_clause )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==HAVING) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // JPA.g:102:78: having_clause
                            {
                            pushFollow(FOLLOW_having_clause_in_range_variable_declaration_source580);
                            having_clause24=having_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_having_clause.add(having_clause24.getTree());

                            }
                            break;

                    }

                    // JPA.g:102:93: ( orderby_clause )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==60) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // JPA.g:102:94: orderby_clause
                            {
                            pushFollow(FOLLOW_orderby_clause_in_range_variable_declaration_source584);
                            orderby_clause25=orderby_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause25.getTree());

                            }
                            break;

                    }

                    rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_range_variable_declaration_source590); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_RPAREN.add(rp);



                    // AST REWRITE
                    // elements: select_clause, groupby_clause, where_clause, orderby_clause, having_clause, from_clause
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 103:3: -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                    {
                        // JPA.g:103:6: ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);

                        // JPA.g:103:37: ( select_clause )?
                        if ( stream_select_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_select_clause.nextTree());

                        }
                        stream_select_clause.reset();
                        adaptor.addChild(root_1, stream_from_clause.nextTree());
                        // JPA.g:103:66: ( where_clause )?
                        if ( stream_where_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_where_clause.nextTree());

                        }
                        stream_where_clause.reset();
                        // JPA.g:103:82: ( groupby_clause )?
                        if ( stream_groupby_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                        }
                        stream_groupby_clause.reset();
                        // JPA.g:103:100: ( having_clause )?
                        if ( stream_having_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_having_clause.nextTree());

                        }
                        stream_having_clause.reset();
                        // JPA.g:103:117: ( orderby_clause )?
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
    // JPA.g:106:1: join : join_spec join_association_path_expression ( 'AS' )? identification_variable -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ) ;
    public final JPAParser.join_return join() throws RecognitionException {
        JPAParser.join_return retval = new JPAParser.join_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal28=null;
        JPAParser.join_spec_return join_spec26 = null;

        JPAParser.join_association_path_expression_return join_association_path_expression27 = null;

        JPAParser.identification_variable_return identification_variable29 = null;


        Object string_literal28_tree=null;
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
        RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");
        try {
            // JPA.g:107:2: ( join_spec join_association_path_expression ( 'AS' )? identification_variable -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ) )
            // JPA.g:107:4: join_spec join_association_path_expression ( 'AS' )? identification_variable
            {
            pushFollow(FOLLOW_join_spec_in_join644);
            join_spec26=join_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_spec.add(join_spec26.getTree());
            pushFollow(FOLLOW_join_association_path_expression_in_join646);
            join_association_path_expression27=join_association_path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression27.getTree());
            // JPA.g:107:47: ( 'AS' )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==48) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // JPA.g:107:48: 'AS'
                    {
                    string_literal28=(Token)match(input,48,FOLLOW_48_in_join649); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_48.add(string_literal28);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_join653);
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
            // 108:4: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression )
            {
                // JPA.g:108:7: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression )
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
    // JPA.g:111:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
    public final JPAParser.fetch_join_return fetch_join() throws RecognitionException {
        JPAParser.fetch_join_return retval = new JPAParser.fetch_join_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal31=null;
        JPAParser.join_spec_return join_spec30 = null;

        JPAParser.join_association_path_expression_return join_association_path_expression32 = null;


        Object string_literal31_tree=null;

        try {
            // JPA.g:112:2: ( join_spec 'FETCH' join_association_path_expression )
            // JPA.g:112:4: join_spec 'FETCH' join_association_path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_join_spec_in_fetch_join682);
            join_spec30=join_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec30.getTree());
            string_literal31=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join684); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal31_tree = (Object)adaptor.create(string_literal31);
            adaptor.addChild(root_0, string_literal31_tree);
            }
            pushFollow(FOLLOW_join_association_path_expression_in_fetch_join686);
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
    // JPA.g:114:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
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
            // JPA.g:115:2: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
            // JPA.g:115:3: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:115:3: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
            int alt16=3;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==50) ) {
                alt16=1;
            }
            else if ( (LA16_0==52) ) {
                alt16=2;
            }
            switch (alt16) {
                case 1 :
                    // JPA.g:115:4: ( 'LEFT' ) ( 'OUTER' )?
                    {
                    // JPA.g:115:4: ( 'LEFT' )
                    // JPA.g:115:5: 'LEFT'
                    {
                    string_literal33=(Token)match(input,50,FOLLOW_50_in_join_spec696); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal33_tree = (Object)adaptor.create(string_literal33);
                    adaptor.addChild(root_0, string_literal33_tree);
                    }

                    }

                    // JPA.g:115:13: ( 'OUTER' )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==51) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // JPA.g:115:14: 'OUTER'
                            {
                            string_literal34=(Token)match(input,51,FOLLOW_51_in_join_spec700); if (state.failed) return retval;
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
                    // JPA.g:115:26: 'INNER'
                    {
                    string_literal35=(Token)match(input,52,FOLLOW_52_in_join_spec706); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal35_tree = (Object)adaptor.create(string_literal35);
                    adaptor.addChild(root_0, string_literal35_tree);
                    }

                    }
                    break;

            }

            string_literal36=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec711); if (state.failed) return retval;
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
    // JPA.g:117:1: join_association_path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
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
            // JPA.g:118:2: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
            // JPA.g:118:4: identification_variable '.' ( field '.' )* ( field )?
            {
            pushFollow(FOLLOW_identification_variable_in_join_association_path_expression720);
            identification_variable37=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable37.getTree());
            char_literal38=(Token)match(input,53,FOLLOW_53_in_join_association_path_expression722); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_53.add(char_literal38);

            // JPA.g:118:32: ( field '.' )*
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
            	    // JPA.g:118:33: field '.'
            	    {
            	    pushFollow(FOLLOW_field_in_join_association_path_expression725);
            	    field39=field();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_field.add(field39.getTree());
            	    char_literal40=(Token)match(input,53,FOLLOW_53_in_join_association_path_expression726); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_53.add(char_literal40);


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            // JPA.g:118:44: ( field )?
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

                if ( (LA18_3==EOF||LA18_3==HAVING||LA18_3==RPAREN||LA18_3==JOIN||LA18_3==WORD||(LA18_3>=47 && LA18_3<=48)||LA18_3==50||LA18_3==52||(LA18_3>=57 && LA18_3<=58)||LA18_3==60) ) {
                    alt18=1;
                }
            }
            switch (alt18) {
                case 1 :
                    // JPA.g:0:0: field
                    {
                    pushFollow(FOLLOW_field_in_join_association_path_expression730);
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
            // 119:2: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
            {
                // JPA.g:119:5: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable37!=null?input.toString(identification_variable37.start,identification_variable37.stop):null)), root_1);

                // JPA.g:119:65: ( field )*
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
    // JPA.g:122:1: collection_member_declaration : 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
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
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        try {
            // JPA.g:123:2: ( 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
            // JPA.g:123:4: 'IN' '(' path_expression ')' ( 'AS' )? identification_variable
            {
            string_literal42=(Token)match(input,54,FOLLOW_54_in_collection_member_declaration758); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_54.add(string_literal42);

            char_literal43=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration759); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_LPAREN.add(char_literal43);

            pushFollow(FOLLOW_path_expression_in_collection_member_declaration761);
            path_expression44=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_path_expression.add(path_expression44.getTree());
            char_literal45=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration763); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_RPAREN.add(char_literal45);

            // JPA.g:123:32: ( 'AS' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==48) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // JPA.g:123:33: 'AS'
                    {
                    string_literal46=(Token)match(input,48,FOLLOW_48_in_collection_member_declaration766); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_48.add(string_literal46);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_collection_member_declaration770);
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
            // 124:2: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
            {
                // JPA.g:124:5: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
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
    // JPA.g:127:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
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
            // JPA.g:128:2: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
            // JPA.g:128:5: identification_variable '.' ( field '.' )* ( field )?
            {
            pushFollow(FOLLOW_identification_variable_in_path_expression796);
            identification_variable48=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable48.getTree());
            char_literal49=(Token)match(input,53,FOLLOW_53_in_path_expression798); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_53.add(char_literal49);

            // JPA.g:128:33: ( field '.' )*
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
            	    // JPA.g:128:34: field '.'
            	    {
            	    pushFollow(FOLLOW_field_in_path_expression801);
            	    field50=field();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_field.add(field50.getTree());
            	    char_literal51=(Token)match(input,53,FOLLOW_53_in_path_expression802); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_53.add(char_literal51);


            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

            // JPA.g:128:45: ( field )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==58) ) {
                int LA21_1 = input.LA(2);

                if ( (LA21_1==EOF||(LA21_1>=HAVING && LA21_1<=DESC)||(LA21_1>=OR && LA21_1<=AND)||LA21_1==RPAREN||LA21_1==WORD||(LA21_1>=46 && LA21_1<=48)||LA21_1==54||LA21_1==58||(LA21_1>=60 && LA21_1<=61)||(LA21_1>=64 && LA21_1<=65)||(LA21_1>=76 && LA21_1<=77)||LA21_1==79||LA21_1==82||(LA21_1>=88 && LA21_1<=95)) ) {
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
                    // JPA.g:128:46: field
                    {
                    pushFollow(FOLLOW_field_in_path_expression807);
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
            // 129:2: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
            {
                // JPA.g:129:5: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable48!=null?input.toString(identification_variable48.start,identification_variable48.stop):null)), root_1);

                // JPA.g:129:65: ( field )*
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
    // JPA.g:132:1: select_clause : ( 'DISTINCT' )? select_expression ( ',' select_expression )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* ) ;
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
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
        RewriteRuleSubtreeStream stream_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule select_expression");
        try {
            // JPA.g:133:2: ( ( 'DISTINCT' )? select_expression ( ',' select_expression )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* ) )
            // JPA.g:133:4: ( 'DISTINCT' )? select_expression ( ',' select_expression )*
            {
            // JPA.g:133:4: ( 'DISTINCT' )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==DISTINCT) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // JPA.g:133:5: 'DISTINCT'
                    {
                    string_literal53=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause837); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal53);


                    }
                    break;

            }

            pushFollow(FOLLOW_select_expression_in_select_clause841);
            select_expression54=select_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_select_expression.add(select_expression54.getTree());
            // JPA.g:133:36: ( ',' select_expression )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==47) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // JPA.g:133:37: ',' select_expression
            	    {
            	    char_literal55=(Token)match(input,47,FOLLOW_47_in_select_clause844); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_47.add(char_literal55);

            	    pushFollow(FOLLOW_select_expression_in_select_clause846);
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
            // 134:2: -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* )
            {
                // JPA.g:134:5: ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);

                // JPA.g:134:24: ( 'DISTINCT' )?
                if ( stream_DISTINCT.hasNext() ) {
                    adaptor.addChild(root_1, stream_DISTINCT.nextNode());

                }
                stream_DISTINCT.reset();
                // JPA.g:134:38: ( ^( T_SELECTED_ITEM[] select_expression ) )*
                while ( stream_select_expression.hasNext() ) {
                    // JPA.g:134:38: ^( T_SELECTED_ITEM[] select_expression )
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
    // JPA.g:137:1: select_expression : ( path_expression | aggregate_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | 'OBJECT' '(' identification_variable ')' | constructor_expression );
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
            // JPA.g:138:2: ( path_expression | aggregate_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | 'OBJECT' '(' identification_variable ')' | constructor_expression )
            int alt24=5;
            switch ( input.LA(1) ) {
            case WORD:
                {
                int LA24_1 = input.LA(2);

                if ( (LA24_1==EOF||(LA24_1>=46 && LA24_1<=47)) ) {
                    alt24=3;
                }
                else if ( (LA24_1==53) ) {
                    alt24=1;
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
                    // JPA.g:138:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_select_expression882);
                    path_expression57=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression57.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:139:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_select_expression887);
                    aggregate_expression58=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression58.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:140:4: identification_variable
                    {
                    pushFollow(FOLLOW_identification_variable_in_select_expression892);
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
                    // 140:28: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
                    {
                        // JPA.g:140:31: ^( T_SELECTED_ENTITY[$identification_variable.text] )
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
                    // JPA.g:141:4: 'OBJECT' '(' identification_variable ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal60=(Token)match(input,55,FOLLOW_55_in_select_expression907); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal60_tree = (Object)adaptor.create(string_literal60);
                    adaptor.addChild(root_0, string_literal60_tree);
                    }
                    char_literal61=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression909); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal61_tree = (Object)adaptor.create(char_literal61);
                    adaptor.addChild(root_0, char_literal61_tree);
                    }
                    pushFollow(FOLLOW_identification_variable_in_select_expression910);
                    identification_variable62=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable62.getTree());
                    char_literal63=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression911); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal63_tree = (Object)adaptor.create(char_literal63);
                    adaptor.addChild(root_0, char_literal63_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:142:4: constructor_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_constructor_expression_in_select_expression916);
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
    // JPA.g:144:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
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
            // JPA.g:145:2: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
            // JPA.g:145:4: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal65=(Token)match(input,56,FOLLOW_56_in_constructor_expression925); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal65_tree = (Object)adaptor.create(string_literal65);
            adaptor.addChild(root_0, string_literal65_tree);
            }
            pushFollow(FOLLOW_constructor_name_in_constructor_expression927);
            constructor_name66=constructor_name();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name66.getTree());
            char_literal67=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression929); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal67_tree = (Object)adaptor.create(char_literal67);
            adaptor.addChild(root_0, char_literal67_tree);
            }
            pushFollow(FOLLOW_constructor_item_in_constructor_expression931);
            constructor_item68=constructor_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item68.getTree());
            // JPA.g:145:48: ( ',' constructor_item )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==47) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // JPA.g:145:49: ',' constructor_item
            	    {
            	    char_literal69=(Token)match(input,47,FOLLOW_47_in_constructor_expression934); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal69_tree = (Object)adaptor.create(char_literal69);
            	    adaptor.addChild(root_0, char_literal69_tree);
            	    }
            	    pushFollow(FOLLOW_constructor_item_in_constructor_expression936);
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

            char_literal71=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression940); if (state.failed) return retval;
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
    // JPA.g:147:1: constructor_item : ( path_expression | aggregate_expression );
    public final JPAParser.constructor_item_return constructor_item() throws RecognitionException {
        JPAParser.constructor_item_return retval = new JPAParser.constructor_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression72 = null;

        JPAParser.aggregate_expression_return aggregate_expression73 = null;



        try {
            // JPA.g:148:2: ( path_expression | aggregate_expression )
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
                    // JPA.g:148:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_constructor_item949);
                    path_expression72=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression72.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:148:22: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_constructor_item953);
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
    // JPA.g:150:1: aggregate_expression : ( aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' ) );
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
            // JPA.g:151:2: ( aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' ) )
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

                            if ( (LA29_5==53) ) {
                                alt29=1;
                            }
                            else if ( (LA29_5==RPAREN) ) {
                                alt29=2;
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

                        if ( (LA29_5==53) ) {
                            alt29=1;
                        }
                        else if ( (LA29_5==RPAREN) ) {
                            alt29=2;
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
                    // JPA.g:151:4: aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')'
                    {
                    pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression962);
                    aggregate_expression_function_name74=aggregate_expression_function_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name74.getTree());
                    char_literal75=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression964); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal75);

                    // JPA.g:151:43: ( 'DISTINCT' )?
                    int alt27=2;
                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==DISTINCT) ) {
                        alt27=1;
                    }
                    switch (alt27) {
                        case 1 :
                            // JPA.g:151:44: 'DISTINCT'
                            {
                            string_literal76=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression967); if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal76);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_path_expression_in_aggregate_expression971);
                    path_expression77=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression77.getTree());
                    char_literal78=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression972); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_RPAREN.add(char_literal78);



                    // AST REWRITE
                    // elements: aggregate_expression_function_name, RPAREN, path_expression, LPAREN, DISTINCT
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 152:2: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
                    {
                        // JPA.g:152:5: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);

                        adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
                        adaptor.addChild(root_1, stream_LPAREN.nextNode());
                        // JPA.g:152:90: ( 'DISTINCT' )?
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
                    // JPA.g:153:4: 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')'
                    {
                    string_literal79=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1000); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_COUNT.add(string_literal79);

                    char_literal80=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1002); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal80);

                    // JPA.g:153:16: ( 'DISTINCT' )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==DISTINCT) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // JPA.g:153:17: 'DISTINCT'
                            {
                            string_literal81=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1005); if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal81);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_identification_variable_in_aggregate_expression1009);
                    identification_variable82=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable82.getTree());
                    char_literal83=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1011); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_RPAREN.add(char_literal83);



                    // AST REWRITE
                    // elements: LPAREN, COUNT, DISTINCT, RPAREN, identification_variable
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 154:2: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' )
                    {
                        // JPA.g:154:5: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);

                        adaptor.addChild(root_1, stream_COUNT.nextNode());
                        adaptor.addChild(root_1, stream_LPAREN.nextNode());
                        // JPA.g:154:63: ( 'DISTINCT' )?
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
    // JPA.g:156:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
    public final JPAParser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
        JPAParser.aggregate_expression_function_name_return retval = new JPAParser.aggregate_expression_function_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set84=null;

        Object set84_tree=null;

        try {
            // JPA.g:157:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
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
    // JPA.g:159:1: where_clause : (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) );
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
            // JPA.g:160:2: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) )
            int alt30=2;
            alt30 = dfa30.predict(input);
            switch (alt30) {
                case 1 :
                    // JPA.g:160:4: wh= 'WHERE' conditional_expression
                    {
                    wh=(Token)match(input,57,FOLLOW_57_in_where_clause1073); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_57.add(wh);

                    pushFollow(FOLLOW_conditional_expression_in_where_clause1075);
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
                    // 160:37: -> ^( T_CONDITION[$wh] conditional_expression )
                    {
                        // JPA.g:160:40: ^( T_CONDITION[$wh] conditional_expression )
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
                    // JPA.g:161:4: 'WHERE' path_expression
                    {
                    string_literal86=(Token)match(input,57,FOLLOW_57_in_where_clause1091); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_57.add(string_literal86);

                    pushFollow(FOLLOW_path_expression_in_where_clause1093);
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
                    // 161:28: -> ^( T_CONDITION[$wh] path_expression )
                    {
                        // JPA.g:161:31: ^( T_CONDITION[$wh] path_expression )
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
    // JPA.g:163:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
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
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");
        try {
            // JPA.g:164:2: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
            // JPA.g:164:4: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
            {
            string_literal88=(Token)match(input,58,FOLLOW_58_in_groupby_clause1114); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_58.add(string_literal88);

            string_literal89=(Token)match(input,59,FOLLOW_59_in_groupby_clause1116); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_59.add(string_literal89);

            pushFollow(FOLLOW_groupby_item_in_groupby_clause1118);
            groupby_item90=groupby_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item90.getTree());
            // JPA.g:164:30: ( ',' groupby_item )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==47) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // JPA.g:164:31: ',' groupby_item
            	    {
            	    char_literal91=(Token)match(input,47,FOLLOW_47_in_groupby_clause1121); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_47.add(char_literal91);

            	    pushFollow(FOLLOW_groupby_item_in_groupby_clause1123);
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
            // elements: groupby_item, 59, 58
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 165:2: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
            {
                // JPA.g:165:5: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new GroupByNode(T_GROUP_BY), root_1);

                adaptor.addChild(root_1, stream_58.nextNode());
                adaptor.addChild(root_1, stream_59.nextNode());
                // JPA.g:165:46: ( groupby_item )*
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
    // JPA.g:168:1: groupby_item : ( path_expression | identification_variable );
    public final JPAParser.groupby_item_return groupby_item() throws RecognitionException {
        JPAParser.groupby_item_return retval = new JPAParser.groupby_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression93 = null;

        JPAParser.identification_variable_return identification_variable94 = null;



        try {
            // JPA.g:169:2: ( path_expression | identification_variable )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==WORD) ) {
                int LA32_1 = input.LA(2);

                if ( (LA32_1==53) ) {
                    alt32=1;
                }
                else if ( (LA32_1==EOF||LA32_1==HAVING||LA32_1==RPAREN||LA32_1==47||LA32_1==60) ) {
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
                    // JPA.g:169:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_groupby_item1154);
                    path_expression93=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression93.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:169:22: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_groupby_item1158);
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
    // JPA.g:171:1: having_clause : 'HAVING' conditional_expression ;
    public final JPAParser.having_clause_return having_clause() throws RecognitionException {
        JPAParser.having_clause_return retval = new JPAParser.having_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal95=null;
        JPAParser.conditional_expression_return conditional_expression96 = null;


        Object string_literal95_tree=null;

        try {
            // JPA.g:172:2: ( 'HAVING' conditional_expression )
            // JPA.g:172:4: 'HAVING' conditional_expression
            {
            root_0 = (Object)adaptor.nil();

            string_literal95=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1167); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal95_tree = (Object)adaptor.create(string_literal95);
            adaptor.addChild(root_0, string_literal95_tree);
            }
            pushFollow(FOLLOW_conditional_expression_in_having_clause1169);
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
    // JPA.g:174:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
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
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
        RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");
        try {
            // JPA.g:175:2: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
            // JPA.g:175:4: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
            {
            string_literal97=(Token)match(input,60,FOLLOW_60_in_orderby_clause1178); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_60.add(string_literal97);

            string_literal98=(Token)match(input,59,FOLLOW_59_in_orderby_clause1180); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_59.add(string_literal98);

            pushFollow(FOLLOW_orderby_item_in_orderby_clause1182);
            orderby_item99=orderby_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item99.getTree());
            // JPA.g:175:30: ( ',' orderby_item )*
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0==47) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // JPA.g:175:31: ',' orderby_item
            	    {
            	    char_literal100=(Token)match(input,47,FOLLOW_47_in_orderby_clause1185); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_47.add(char_literal100);

            	    pushFollow(FOLLOW_orderby_item_in_orderby_clause1187);
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
            // elements: 59, orderby_item, 60
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 176:2: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
            {
                // JPA.g:176:5: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new OrderByNode(T_ORDER_BY), root_1);

                adaptor.addChild(root_1, stream_60.nextNode());
                adaptor.addChild(root_1, stream_59.nextNode());
                // JPA.g:176:46: ( orderby_item )*
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
    // JPA.g:178:1: orderby_item : ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) );
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
            // JPA.g:179:2: ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) )
            int alt35=2;
            alt35 = dfa35.predict(input);
            switch (alt35) {
                case 1 :
                    // JPA.g:179:4: path_expression ( 'ASC' )?
                    {
                    pushFollow(FOLLOW_path_expression_in_orderby_item1216);
                    path_expression102=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression102.getTree());
                    // JPA.g:179:20: ( 'ASC' )?
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==ASC) ) {
                        alt34=1;
                    }
                    switch (alt34) {
                        case 1 :
                            // JPA.g:179:21: 'ASC'
                            {
                            string_literal103=(Token)match(input,ASC,FOLLOW_ASC_in_orderby_item1219); if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_ASC.add(string_literal103);


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
                    // 180:3: -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? )
                    {
                        // JPA.g:180:6: ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);

                        adaptor.addChild(root_1, stream_path_expression.nextTree());
                        // JPA.g:180:61: ( 'ASC' )?
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
                    // JPA.g:181:4: path_expression 'DESC'
                    {
                    pushFollow(FOLLOW_path_expression_in_orderby_item1245);
                    path_expression104=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression104.getTree());
                    string_literal105=(Token)match(input,DESC,FOLLOW_DESC_in_orderby_item1247); if (state.failed) return retval;
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
                    // 182:2: -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' )
                    {
                        // JPA.g:182:5: ^( T_ORDER_BY_FIELD[] path_expression 'DESC' )
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
    // JPA.g:184:1: subquery : lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
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
        RewriteRuleTokenStream stream_49=new RewriteRuleTokenStream(adaptor,"token 49");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
        RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
        try {
            // JPA.g:185:2: (lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
            // JPA.g:185:4: lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
            {
            lp=(Token)match(input,49,FOLLOW_49_in_subquery1273); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_49.add(lp);

            pushFollow(FOLLOW_simple_select_clause_in_subquery1275);
            simple_select_clause106=simple_select_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause106.getTree());
            pushFollow(FOLLOW_subquery_from_clause_in_subquery1277);
            subquery_from_clause107=subquery_from_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause107.getTree());
            // JPA.g:185:59: ( where_clause )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==57) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // JPA.g:185:60: where_clause
                    {
                    pushFollow(FOLLOW_where_clause_in_subquery1280);
                    where_clause108=where_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_where_clause.add(where_clause108.getTree());

                    }
                    break;

            }

            // JPA.g:185:75: ( groupby_clause )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==58) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // JPA.g:185:76: groupby_clause
                    {
                    pushFollow(FOLLOW_groupby_clause_in_subquery1285);
                    groupby_clause109=groupby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause109.getTree());

                    }
                    break;

            }

            // JPA.g:185:93: ( having_clause )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==HAVING) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // JPA.g:185:94: having_clause
                    {
                    pushFollow(FOLLOW_having_clause_in_subquery1290);
                    having_clause110=having_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_having_clause.add(having_clause110.getTree());

                    }
                    break;

            }

            rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1296); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_RPAREN.add(rp);



            // AST REWRITE
            // elements: subquery_from_clause, having_clause, simple_select_clause, groupby_clause, where_clause
            // token labels:
            // rule labels: retval
            // token list labels:
            // rule list labels:
            // wildcard labels:
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 186:3: -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
            {
                // JPA.g:186:6: ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);

                adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
                adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
                // JPA.g:186:78: ( where_clause )?
                if ( stream_where_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_where_clause.nextTree());

                }
                stream_where_clause.reset();
                // JPA.g:186:94: ( groupby_clause )?
                if ( stream_groupby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                }
                stream_groupby_clause.reset();
                // JPA.g:186:112: ( having_clause )?
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
    // JPA.g:188:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
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
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
        RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");
        try {
            // JPA.g:189:2: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
            // JPA.g:189:4: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
            {
            fr=(Token)match(input,46,FOLLOW_46_in_subquery_from_clause1339); if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_46.add(fr);

            pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1341);
            subselect_identification_variable_declaration111=subselect_identification_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration111.getTree());
            // JPA.g:189:60: ( ',' subselect_identification_variable_declaration )*
            loop39:
            do {
                int alt39=2;
                int LA39_0 = input.LA(1);

                if ( (LA39_0==47) ) {
                    alt39=1;
                }


                switch (alt39) {
            	case 1 :
            	    // JPA.g:189:61: ',' subselect_identification_variable_declaration
            	    {
            	    char_literal112=(Token)match(input,47,FOLLOW_47_in_subquery_from_clause1344); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_47.add(char_literal112);

            	    pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1346);
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
            // 190:2: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
            {
                // JPA.g:190:5: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);

                // JPA.g:190:32: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
                while ( stream_subselect_identification_variable_declaration.hasNext() ) {
                    // JPA.g:190:32: ^( T_SOURCE subselect_identification_variable_declaration )
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
    // JPA.g:192:1: subselect_identification_variable_declaration : ( identification_variable_declaration | association_path_expression ( 'AS' )? identification_variable | collection_member_declaration );
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
            // JPA.g:193:2: ( identification_variable_declaration | association_path_expression ( 'AS' )? identification_variable | collection_member_declaration )
            int alt41=3;
            switch ( input.LA(1) ) {
            case WORD:
                {
                int LA41_1 = input.LA(2);

                if ( (LA41_1==WORD||LA41_1==48) ) {
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
            case 49:
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
                    // JPA.g:193:4: identification_variable_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1378);
                    identification_variable_declaration114=identification_variable_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration114.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:194:4: association_path_expression ( 'AS' )? identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_association_path_expression_in_subselect_identification_variable_declaration1383);
                    association_path_expression115=association_path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, association_path_expression115.getTree());
                    // JPA.g:194:32: ( 'AS' )?
                    int alt40=2;
                    int LA40_0 = input.LA(1);

                    if ( (LA40_0==48) ) {
                        alt40=1;
                    }
                    switch (alt40) {
                        case 1 :
                            // JPA.g:194:33: 'AS'
                            {
                            string_literal116=(Token)match(input,48,FOLLOW_48_in_subselect_identification_variable_declaration1386); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal116_tree = (Object)adaptor.create(string_literal116);
                            adaptor.addChild(root_0, string_literal116_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration1390);
                    identification_variable117=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable117.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:195:4: collection_member_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_declaration_in_subselect_identification_variable_declaration1395);
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
    // JPA.g:197:1: association_path_expression : path_expression ;
    public final JPAParser.association_path_expression_return association_path_expression() throws RecognitionException {
        JPAParser.association_path_expression_return retval = new JPAParser.association_path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression119 = null;



        try {
            // JPA.g:198:2: ( path_expression )
            // JPA.g:198:4: path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_association_path_expression1404);
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
    // JPA.g:200:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
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
            // JPA.g:201:2: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
            // JPA.g:201:4: ( 'DISTINCT' )? simple_select_expression
            {
            // JPA.g:201:4: ( 'DISTINCT' )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==DISTINCT) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // JPA.g:201:5: 'DISTINCT'
                    {
                    string_literal120=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause1414); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal120);


                    }
                    break;

            }

            pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause1418);
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
            // 202:2: -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
            {
                // JPA.g:202:5: ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);

                // JPA.g:202:24: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);

                // JPA.g:202:62: ( 'DISTINCT' )?
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
    // JPA.g:204:1: simple_select_expression : ( path_expression | aggregate_expression | identification_variable );
    public final JPAParser.simple_select_expression_return simple_select_expression() throws RecognitionException {
        JPAParser.simple_select_expression_return retval = new JPAParser.simple_select_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression122 = null;

        JPAParser.aggregate_expression_return aggregate_expression123 = null;

        JPAParser.identification_variable_return identification_variable124 = null;



        try {
            // JPA.g:205:2: ( path_expression | aggregate_expression | identification_variable )
            int alt43=3;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==WORD) ) {
                int LA43_1 = input.LA(2);

                if ( (LA43_1==46) ) {
                    alt43=3;
                }
                else if ( (LA43_1==53) ) {
                    alt43=1;
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
                    // JPA.g:205:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_simple_select_expression1449);
                    path_expression122=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression122.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:206:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression1454);
                    aggregate_expression123=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression123.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:207:4: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_simple_select_expression1459);
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
    // JPA.g:209:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
    public final JPAParser.conditional_expression_return conditional_expression() throws RecognitionException {
        JPAParser.conditional_expression_return retval = new JPAParser.conditional_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal126=null;
        JPAParser.conditional_term_return conditional_term125 = null;

        JPAParser.conditional_term_return conditional_term127 = null;


        Object string_literal126_tree=null;

        try {
            // JPA.g:210:2: ( ( conditional_term ) ( 'OR' conditional_term )* )
            // JPA.g:210:4: ( conditional_term ) ( 'OR' conditional_term )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:210:4: ( conditional_term )
            // JPA.g:210:5: conditional_term
            {
            pushFollow(FOLLOW_conditional_term_in_conditional_expression1469);
            conditional_term125=conditional_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term125.getTree());

            }

            // JPA.g:210:23: ( 'OR' conditional_term )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==OR) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // JPA.g:210:24: 'OR' conditional_term
            	    {
            	    string_literal126=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression1473); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal126_tree = (Object)adaptor.create(string_literal126);
            	    adaptor.addChild(root_0, string_literal126_tree);
            	    }
            	    pushFollow(FOLLOW_conditional_term_in_conditional_expression1475);
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
    // JPA.g:212:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
    public final JPAParser.conditional_term_return conditional_term() throws RecognitionException {
        JPAParser.conditional_term_return retval = new JPAParser.conditional_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal129=null;
        JPAParser.conditional_factor_return conditional_factor128 = null;

        JPAParser.conditional_factor_return conditional_factor130 = null;


        Object string_literal129_tree=null;

        try {
            // JPA.g:213:2: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
            // JPA.g:213:4: ( conditional_factor ) ( 'AND' conditional_factor )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:213:4: ( conditional_factor )
            // JPA.g:213:5: conditional_factor
            {
            pushFollow(FOLLOW_conditional_factor_in_conditional_term1487);
            conditional_factor128=conditional_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor128.getTree());

            }

            // JPA.g:213:25: ( 'AND' conditional_factor )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==AND) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // JPA.g:213:26: 'AND' conditional_factor
            	    {
            	    string_literal129=(Token)match(input,AND,FOLLOW_AND_in_conditional_term1491); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal129_tree = (Object)adaptor.create(string_literal129);
            	    adaptor.addChild(root_0, string_literal129_tree);
            	    }
            	    pushFollow(FOLLOW_conditional_factor_in_conditional_term1493);
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
    // JPA.g:215:1: conditional_factor : ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' );
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
            // JPA.g:216:2: ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' )
            int alt47=2;
            alt47 = dfa47.predict(input);
            switch (alt47) {
                case 1 :
                    // JPA.g:216:4: ( 'NOT' )? simple_cond_expression
                    {
                    // JPA.g:216:4: ( 'NOT' )?
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
                            // JPA.g:216:5: 'NOT'
                            {
                            string_literal131=(Token)match(input,61,FOLLOW_61_in_conditional_factor1506); if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_61.add(string_literal131);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_simple_cond_expression_in_conditional_factor1510);
                    simple_cond_expression132=simple_cond_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_simple_cond_expression.add(simple_cond_expression132.getTree());


                    // AST REWRITE
                    // elements: 61, simple_cond_expression
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 216:36: -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression )
                    {
                        // JPA.g:216:39: ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new SimpleConditionNode(T_SIMPLE_CONDITION), root_1);

                        // JPA.g:216:83: ( 'NOT' )?
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
                    // JPA.g:217:4: '(' conditional_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal133=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_factor1532); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal133_tree = (Object)adaptor.create(char_literal133);
                    adaptor.addChild(root_0, char_literal133_tree);
                    }
                    pushFollow(FOLLOW_conditional_expression_in_conditional_factor1533);
                    conditional_expression134=conditional_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression134.getTree());
                    char_literal135=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_factor1534); if (state.failed) return retval;
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
    // JPA.g:219:1: simple_cond_expression : ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
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
            // JPA.g:220:2: ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
            int alt48=9;
            alt48 = dfa48.predict(input);
            switch (alt48) {
                case 1 :
                    // JPA.g:220:4: comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression1543);
                    comparison_expression136=comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression136.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:221:4: between_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_between_expression_in_simple_cond_expression1548);
                    between_expression137=between_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression137.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:222:4: like_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_like_expression_in_simple_cond_expression1553);
                    like_expression138=like_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression138.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:223:4: in_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_in_expression_in_simple_cond_expression1558);
                    in_expression139=in_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression139.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:224:4: null_comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression1563);
                    null_comparison_expression140=null_comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression140.getTree());

                    }
                    break;
                case 6 :
                    // JPA.g:225:4: empty_collection_comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression1568);
                    empty_collection_comparison_expression141=empty_collection_comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression141.getTree());

                    }
                    break;
                case 7 :
                    // JPA.g:226:4: collection_member_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression1573);
                    collection_member_expression142=collection_member_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression142.getTree());

                    }
                    break;
                case 8 :
                    // JPA.g:227:4: exists_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_exists_expression_in_simple_cond_expression1578);
                    exists_expression143=exists_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression143.getTree());

                    }
                    break;
                case 9 :
                    // JPA.g:228:4: date_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression1583);
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
    // JPA.g:230:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
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
            // JPA.g:231:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
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
                    // JPA.g:231:7: date_between_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression1595);
                    date_between_macro_expression145=date_between_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression145.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:232:7: date_before_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression1603);
                    date_before_macro_expression146=date_before_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression146.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:233:7: date_after_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression1611);
                    date_after_macro_expression147=date_after_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression147.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:234:7: date_equals_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression1619);
                    date_equals_macro_expression148=date_equals_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression148.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:235:7: date_today_macro_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression1627);
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
    // JPA.g:237:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
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
            // JPA.g:238:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
            // JPA.g:238:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal150=(Token)match(input,62,FOLLOW_62_in_date_between_macro_expression1639); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal150_tree = (Object)adaptor.create(string_literal150);
            adaptor.addChild(root_0, string_literal150_tree);
            }
            char_literal151=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression1641); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal151_tree = (Object)adaptor.create(char_literal151);
            adaptor.addChild(root_0, char_literal151_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_between_macro_expression1643);
            path_expression152=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression152.getTree());
            char_literal153=(Token)match(input,47,FOLLOW_47_in_date_between_macro_expression1645); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal153_tree = (Object)adaptor.create(char_literal153);
            adaptor.addChild(root_0, char_literal153_tree);
            }
            string_literal154=(Token)match(input,63,FOLLOW_63_in_date_between_macro_expression1647); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal154_tree = (Object)adaptor.create(string_literal154);
            adaptor.addChild(root_0, string_literal154_tree);
            }
            // JPA.g:238:48: ( ( '+' | '-' ) INT_NUMERAL )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( ((LA50_0>=64 && LA50_0<=65)) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // JPA.g:238:49: ( '+' | '-' ) INT_NUMERAL
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

                    INT_NUMERAL156=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_date_between_macro_expression1658); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_NUMERAL156_tree = (Object)adaptor.create(INT_NUMERAL156);
                    adaptor.addChild(root_0, INT_NUMERAL156_tree);
                    }

                    }
                    break;

            }

            char_literal157=(Token)match(input,47,FOLLOW_47_in_date_between_macro_expression1662); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal157_tree = (Object)adaptor.create(char_literal157);
            adaptor.addChild(root_0, char_literal157_tree);
            }
            string_literal158=(Token)match(input,63,FOLLOW_63_in_date_between_macro_expression1664); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal158_tree = (Object)adaptor.create(string_literal158);
            adaptor.addChild(root_0, string_literal158_tree);
            }
            // JPA.g:238:85: ( ( '+' | '-' ) INT_NUMERAL )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( ((LA51_0>=64 && LA51_0<=65)) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // JPA.g:238:86: ( '+' | '-' ) INT_NUMERAL
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

                    INT_NUMERAL160=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_date_between_macro_expression1675); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_NUMERAL160_tree = (Object)adaptor.create(INT_NUMERAL160);
                    adaptor.addChild(root_0, INT_NUMERAL160_tree);
                    }

                    }
                    break;

            }

            char_literal161=(Token)match(input,47,FOLLOW_47_in_date_between_macro_expression1679); if (state.failed) return retval;
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

            char_literal163=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression1704); if (state.failed) return retval;
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
    // JPA.g:240:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
            // JPA.g:241:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:241:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal164=(Token)match(input,72,FOLLOW_72_in_date_before_macro_expression1716); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal164_tree = (Object)adaptor.create(string_literal164);
            adaptor.addChild(root_0, string_literal164_tree);
            }
            char_literal165=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression1718); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal165_tree = (Object)adaptor.create(char_literal165);
            adaptor.addChild(root_0, char_literal165_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_before_macro_expression1720);
            path_expression166=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression166.getTree());
            char_literal167=(Token)match(input,47,FOLLOW_47_in_date_before_macro_expression1722); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal167_tree = (Object)adaptor.create(char_literal167);
            adaptor.addChild(root_0, char_literal167_tree);
            }
            // JPA.g:241:45: ( path_expression | input_parameter )
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
                    // JPA.g:241:46: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_before_macro_expression1725);
                    path_expression168=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression168.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:241:64: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression1729);
                    input_parameter169=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter169.getTree());

                    }
                    break;

            }

            char_literal170=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression1732); if (state.failed) return retval;
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
    // JPA.g:243:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
            // JPA.g:244:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:244:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal171=(Token)match(input,73,FOLLOW_73_in_date_after_macro_expression1744); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal171_tree = (Object)adaptor.create(string_literal171);
            adaptor.addChild(root_0, string_literal171_tree);
            }
            char_literal172=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression1746); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal172_tree = (Object)adaptor.create(char_literal172);
            adaptor.addChild(root_0, char_literal172_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_after_macro_expression1748);
            path_expression173=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression173.getTree());
            char_literal174=(Token)match(input,47,FOLLOW_47_in_date_after_macro_expression1750); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal174_tree = (Object)adaptor.create(char_literal174);
            adaptor.addChild(root_0, char_literal174_tree);
            }
            // JPA.g:244:44: ( path_expression | input_parameter )
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
                    // JPA.g:244:45: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_after_macro_expression1753);
                    path_expression175=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression175.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:244:63: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression1757);
                    input_parameter176=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter176.getTree());

                    }
                    break;

            }

            char_literal177=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression1760); if (state.failed) return retval;
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
    // JPA.g:246:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
            // JPA.g:247:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:247:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal178=(Token)match(input,74,FOLLOW_74_in_date_equals_macro_expression1772); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal178_tree = (Object)adaptor.create(string_literal178);
            adaptor.addChild(root_0, string_literal178_tree);
            }
            char_literal179=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression1774); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal179_tree = (Object)adaptor.create(char_literal179);
            adaptor.addChild(root_0, char_literal179_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression1776);
            path_expression180=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression180.getTree());
            char_literal181=(Token)match(input,47,FOLLOW_47_in_date_equals_macro_expression1778); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal181_tree = (Object)adaptor.create(char_literal181);
            adaptor.addChild(root_0, char_literal181_tree);
            }
            // JPA.g:247:45: ( path_expression | input_parameter )
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
                    // JPA.g:247:46: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression1781);
                    path_expression182=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression182.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:247:64: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression1785);
                    input_parameter183=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter183.getTree());

                    }
                    break;

            }

            char_literal184=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression1788); if (state.failed) return retval;
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
    // JPA.g:249:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
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
            // JPA.g:250:5: ( '@TODAY' '(' path_expression ')' )
            // JPA.g:250:7: '@TODAY' '(' path_expression ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal185=(Token)match(input,75,FOLLOW_75_in_date_today_macro_expression1800); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal185_tree = (Object)adaptor.create(string_literal185);
            adaptor.addChild(root_0, string_literal185_tree);
            }
            char_literal186=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression1802); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal186_tree = (Object)adaptor.create(char_literal186);
            adaptor.addChild(root_0, char_literal186_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_today_macro_expression1804);
            path_expression187=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression187.getTree());
            char_literal188=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression1806); if (state.failed) return retval;
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
    // JPA.g:252:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
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
            // JPA.g:253:2: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
            int alt58=3;
            alt58 = dfa58.predict(input);
            switch (alt58) {
                case 1 :
                    // JPA.g:253:4: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1815);
                    arithmetic_expression189=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression189.getTree());
                    // JPA.g:253:26: ( 'NOT' )?
                    int alt55=2;
                    int LA55_0 = input.LA(1);

                    if ( (LA55_0==61) ) {
                        alt55=1;
                    }
                    switch (alt55) {
                        case 1 :
                            // JPA.g:253:27: 'NOT'
                            {
                            string_literal190=(Token)match(input,61,FOLLOW_61_in_between_expression1818); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal190_tree = (Object)adaptor.create(string_literal190);
                            adaptor.addChild(root_0, string_literal190_tree);
                            }

                            }
                            break;

                    }

                    string_literal191=(Token)match(input,76,FOLLOW_76_in_between_expression1822); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal191_tree = (Object)adaptor.create(string_literal191);
                    adaptor.addChild(root_0, string_literal191_tree);
                    }
                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1824);
                    arithmetic_expression192=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression192.getTree());
                    string_literal193=(Token)match(input,AND,FOLLOW_AND_in_between_expression1826); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal193_tree = (Object)adaptor.create(string_literal193);
                    adaptor.addChild(root_0, string_literal193_tree);
                    }
                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1828);
                    arithmetic_expression194=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression194.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:254:4: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_expression_in_between_expression1833);
                    string_expression195=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression195.getTree());
                    // JPA.g:254:22: ( 'NOT' )?
                    int alt56=2;
                    int LA56_0 = input.LA(1);

                    if ( (LA56_0==61) ) {
                        alt56=1;
                    }
                    switch (alt56) {
                        case 1 :
                            // JPA.g:254:23: 'NOT'
                            {
                            string_literal196=(Token)match(input,61,FOLLOW_61_in_between_expression1836); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal196_tree = (Object)adaptor.create(string_literal196);
                            adaptor.addChild(root_0, string_literal196_tree);
                            }

                            }
                            break;

                    }

                    string_literal197=(Token)match(input,76,FOLLOW_76_in_between_expression1840); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal197_tree = (Object)adaptor.create(string_literal197);
                    adaptor.addChild(root_0, string_literal197_tree);
                    }
                    pushFollow(FOLLOW_string_expression_in_between_expression1842);
                    string_expression198=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression198.getTree());
                    string_literal199=(Token)match(input,AND,FOLLOW_AND_in_between_expression1844); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal199_tree = (Object)adaptor.create(string_literal199);
                    adaptor.addChild(root_0, string_literal199_tree);
                    }
                    pushFollow(FOLLOW_string_expression_in_between_expression1846);
                    string_expression200=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression200.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:255:4: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_expression_in_between_expression1851);
                    datetime_expression201=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression201.getTree());
                    // JPA.g:255:24: ( 'NOT' )?
                    int alt57=2;
                    int LA57_0 = input.LA(1);

                    if ( (LA57_0==61) ) {
                        alt57=1;
                    }
                    switch (alt57) {
                        case 1 :
                            // JPA.g:255:25: 'NOT'
                            {
                            string_literal202=(Token)match(input,61,FOLLOW_61_in_between_expression1854); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal202_tree = (Object)adaptor.create(string_literal202);
                            adaptor.addChild(root_0, string_literal202_tree);
                            }

                            }
                            break;

                    }

                    string_literal203=(Token)match(input,76,FOLLOW_76_in_between_expression1858); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal203_tree = (Object)adaptor.create(string_literal203);
                    adaptor.addChild(root_0, string_literal203_tree);
                    }
                    pushFollow(FOLLOW_datetime_expression_in_between_expression1860);
                    datetime_expression204=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression204.getTree());
                    string_literal205=(Token)match(input,AND,FOLLOW_AND_in_between_expression1862); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal205_tree = (Object)adaptor.create(string_literal205);
                    adaptor.addChild(root_0, string_literal205_tree);
                    }
                    pushFollow(FOLLOW_datetime_expression_in_between_expression1864);
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
    // JPA.g:257:1: in_expression : path_expression ( 'NOT' )? 'IN' in_expression_right_part ;
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
            // JPA.g:258:2: ( path_expression ( 'NOT' )? 'IN' in_expression_right_part )
            // JPA.g:258:4: path_expression ( 'NOT' )? 'IN' in_expression_right_part
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_in_expression1873);
            path_expression207=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression207.getTree());
            // JPA.g:258:20: ( 'NOT' )?
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==61) ) {
                alt59=1;
            }
            switch (alt59) {
                case 1 :
                    // JPA.g:258:21: 'NOT'
                    {
                    string_literal208=(Token)match(input,61,FOLLOW_61_in_in_expression1876); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal208_tree = (Object)adaptor.create(string_literal208);
                    adaptor.addChild(root_0, string_literal208_tree);
                    }

                    }
                    break;

            }

            string_literal209=(Token)match(input,54,FOLLOW_54_in_in_expression1880); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal209_tree = (Object)adaptor.create(string_literal209);
            adaptor.addChild(root_0, string_literal209_tree);
            }
            pushFollow(FOLLOW_in_expression_right_part_in_in_expression1882);
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
    // JPA.g:260:1: in_expression_right_part : ( '(' in_item ( ',' in_item )* ')' | subquery );
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
            // JPA.g:261:2: ( '(' in_item ( ',' in_item )* ')' | subquery )
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==LPAREN) ) {
                alt61=1;
            }
            else if ( (LA61_0==49) ) {
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
                    // JPA.g:261:4: '(' in_item ( ',' in_item )* ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal211=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression_right_part1891); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal211_tree = (Object)adaptor.create(char_literal211);
                    adaptor.addChild(root_0, char_literal211_tree);
                    }
                    pushFollow(FOLLOW_in_item_in_in_expression_right_part1893);
                    in_item212=in_item();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item212.getTree());
                    // JPA.g:261:16: ( ',' in_item )*
                    loop60:
                    do {
                        int alt60=2;
                        int LA60_0 = input.LA(1);

                        if ( (LA60_0==47) ) {
                            alt60=1;
                        }


                        switch (alt60) {
                    	case 1 :
                    	    // JPA.g:261:17: ',' in_item
                    	    {
                    	    char_literal213=(Token)match(input,47,FOLLOW_47_in_in_expression_right_part1896); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal213_tree = (Object)adaptor.create(char_literal213);
                    	    adaptor.addChild(root_0, char_literal213_tree);
                    	    }
                    	    pushFollow(FOLLOW_in_item_in_in_expression_right_part1898);
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

                    char_literal215=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression_right_part1902); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal215_tree = (Object)adaptor.create(char_literal215);
                    adaptor.addChild(root_0, char_literal215_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:262:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_in_expression_right_part1907);
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
    // JPA.g:264:1: in_item : ( literal | input_parameter );
    public final JPAParser.in_item_return in_item() throws RecognitionException {
        JPAParser.in_item_return retval = new JPAParser.in_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.literal_return literal217 = null;

        JPAParser.input_parameter_return input_parameter218 = null;



        try {
            // JPA.g:265:2: ( literal | input_parameter )
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
                    // JPA.g:265:4: literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_literal_in_in_item1916);
                    literal217=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal217.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:266:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_in_item1921);
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
    // JPA.g:268:1: like_expression : string_expression ( 'NOT' )? 'LIKE' pattern_value ( 'ESCAPE' ESCAPE_CHARACTER )? ;
    public final JPAParser.like_expression_return like_expression() throws RecognitionException {
        JPAParser.like_expression_return retval = new JPAParser.like_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal220=null;
        Token string_literal221=null;
        Token string_literal223=null;
        Token ESCAPE_CHARACTER224=null;
        JPAParser.string_expression_return string_expression219 = null;

        JPAParser.pattern_value_return pattern_value222 = null;


        Object string_literal220_tree=null;
        Object string_literal221_tree=null;
        Object string_literal223_tree=null;
        Object ESCAPE_CHARACTER224_tree=null;

        try {
            // JPA.g:269:2: ( string_expression ( 'NOT' )? 'LIKE' pattern_value ( 'ESCAPE' ESCAPE_CHARACTER )? )
            // JPA.g:269:4: string_expression ( 'NOT' )? 'LIKE' pattern_value ( 'ESCAPE' ESCAPE_CHARACTER )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_string_expression_in_like_expression1930);
            string_expression219=string_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression219.getTree());
            // JPA.g:269:22: ( 'NOT' )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==61) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // JPA.g:269:23: 'NOT'
                    {
                    string_literal220=(Token)match(input,61,FOLLOW_61_in_like_expression1933); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal220_tree = (Object)adaptor.create(string_literal220);
                    adaptor.addChild(root_0, string_literal220_tree);
                    }

                    }
                    break;

            }

            string_literal221=(Token)match(input,77,FOLLOW_77_in_like_expression1937); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal221_tree = (Object)adaptor.create(string_literal221);
            adaptor.addChild(root_0, string_literal221_tree);
            }
            pushFollow(FOLLOW_pattern_value_in_like_expression1939);
            pattern_value222=pattern_value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value222.getTree());
            // JPA.g:269:52: ( 'ESCAPE' ESCAPE_CHARACTER )?
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==78) ) {
                alt64=1;
            }
            switch (alt64) {
                case 1 :
                    // JPA.g:269:53: 'ESCAPE' ESCAPE_CHARACTER
                    {
                    string_literal223=(Token)match(input,78,FOLLOW_78_in_like_expression1942); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal223_tree = (Object)adaptor.create(string_literal223);
                    adaptor.addChild(root_0, string_literal223_tree);
                    }
                    ESCAPE_CHARACTER224=(Token)match(input,ESCAPE_CHARACTER,FOLLOW_ESCAPE_CHARACTER_in_like_expression1944); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ESCAPE_CHARACTER224_tree = (Object)adaptor.create(ESCAPE_CHARACTER224);
                    adaptor.addChild(root_0, ESCAPE_CHARACTER224_tree);
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
    // JPA.g:271:1: null_comparison_expression : ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' ;
    public final JPAParser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
        JPAParser.null_comparison_expression_return retval = new JPAParser.null_comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal227=null;
        Token string_literal228=null;
        Token string_literal229=null;
        JPAParser.path_expression_return path_expression225 = null;

        JPAParser.input_parameter_return input_parameter226 = null;


        Object string_literal227_tree=null;
        Object string_literal228_tree=null;
        Object string_literal229_tree=null;

        try {
            // JPA.g:272:2: ( ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' )
            // JPA.g:272:4: ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL'
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:272:4: ( path_expression | input_parameter )
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==WORD) ) {
                alt65=1;
            }
            else if ( (LA65_0==NAMED_PARAMETER||LA65_0==114) ) {
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
                    // JPA.g:272:5: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_null_comparison_expression1956);
                    path_expression225=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression225.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:272:23: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_null_comparison_expression1960);
                    input_parameter226=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter226.getTree());

                    }
                    break;

            }

            string_literal227=(Token)match(input,79,FOLLOW_79_in_null_comparison_expression1963); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal227_tree = (Object)adaptor.create(string_literal227);
            adaptor.addChild(root_0, string_literal227_tree);
            }
            // JPA.g:272:45: ( 'NOT' )?
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==61) ) {
                alt66=1;
            }
            switch (alt66) {
                case 1 :
                    // JPA.g:272:46: 'NOT'
                    {
                    string_literal228=(Token)match(input,61,FOLLOW_61_in_null_comparison_expression1966); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal228_tree = (Object)adaptor.create(string_literal228);
                    adaptor.addChild(root_0, string_literal228_tree);
                    }

                    }
                    break;

            }

            string_literal229=(Token)match(input,80,FOLLOW_80_in_null_comparison_expression1970); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal229_tree = (Object)adaptor.create(string_literal229);
            adaptor.addChild(root_0, string_literal229_tree);
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
    // JPA.g:274:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
    public final JPAParser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
        JPAParser.empty_collection_comparison_expression_return retval = new JPAParser.empty_collection_comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal231=null;
        Token string_literal232=null;
        Token string_literal233=null;
        JPAParser.path_expression_return path_expression230 = null;


        Object string_literal231_tree=null;
        Object string_literal232_tree=null;
        Object string_literal233_tree=null;

        try {
            // JPA.g:275:2: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
            // JPA.g:275:4: path_expression 'IS' ( 'NOT' )? 'EMPTY'
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression1979);
            path_expression230=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression230.getTree());
            string_literal231=(Token)match(input,79,FOLLOW_79_in_empty_collection_comparison_expression1981); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal231_tree = (Object)adaptor.create(string_literal231);
            adaptor.addChild(root_0, string_literal231_tree);
            }
            // JPA.g:275:25: ( 'NOT' )?
            int alt67=2;
            int LA67_0 = input.LA(1);

            if ( (LA67_0==61) ) {
                alt67=1;
            }
            switch (alt67) {
                case 1 :
                    // JPA.g:275:26: 'NOT'
                    {
                    string_literal232=(Token)match(input,61,FOLLOW_61_in_empty_collection_comparison_expression1984); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal232_tree = (Object)adaptor.create(string_literal232);
                    adaptor.addChild(root_0, string_literal232_tree);
                    }

                    }
                    break;

            }

            string_literal233=(Token)match(input,81,FOLLOW_81_in_empty_collection_comparison_expression1988); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal233_tree = (Object)adaptor.create(string_literal233);
            adaptor.addChild(root_0, string_literal233_tree);
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
    // JPA.g:277:1: collection_member_expression : entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
    public final JPAParser.collection_member_expression_return collection_member_expression() throws RecognitionException {
        JPAParser.collection_member_expression_return retval = new JPAParser.collection_member_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal235=null;
        Token string_literal236=null;
        Token string_literal237=null;
        JPAParser.entity_expression_return entity_expression234 = null;

        JPAParser.path_expression_return path_expression238 = null;


        Object string_literal235_tree=null;
        Object string_literal236_tree=null;
        Object string_literal237_tree=null;

        try {
            // JPA.g:278:2: ( entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
            // JPA.g:278:4: entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_entity_expression_in_collection_member_expression1997);
            entity_expression234=entity_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression234.getTree());
            // JPA.g:278:22: ( 'NOT' )?
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==61) ) {
                alt68=1;
            }
            switch (alt68) {
                case 1 :
                    // JPA.g:278:23: 'NOT'
                    {
                    string_literal235=(Token)match(input,61,FOLLOW_61_in_collection_member_expression2000); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal235_tree = (Object)adaptor.create(string_literal235);
                    adaptor.addChild(root_0, string_literal235_tree);
                    }

                    }
                    break;

            }

            string_literal236=(Token)match(input,82,FOLLOW_82_in_collection_member_expression2004); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal236_tree = (Object)adaptor.create(string_literal236);
            adaptor.addChild(root_0, string_literal236_tree);
            }
            // JPA.g:278:40: ( 'OF' )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==83) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // JPA.g:278:41: 'OF'
                    {
                    string_literal237=(Token)match(input,83,FOLLOW_83_in_collection_member_expression2007); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal237_tree = (Object)adaptor.create(string_literal237);
                    adaptor.addChild(root_0, string_literal237_tree);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_path_expression_in_collection_member_expression2011);
            path_expression238=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression238.getTree());

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
    // JPA.g:280:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
    public final JPAParser.exists_expression_return exists_expression() throws RecognitionException {
        JPAParser.exists_expression_return retval = new JPAParser.exists_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal239=null;
        Token string_literal240=null;
        JPAParser.subquery_return subquery241 = null;


        Object string_literal239_tree=null;
        Object string_literal240_tree=null;

        try {
            // JPA.g:281:2: ( ( 'NOT' )? 'EXISTS' subquery )
            // JPA.g:281:4: ( 'NOT' )? 'EXISTS' subquery
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:281:4: ( 'NOT' )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==61) ) {
                alt70=1;
            }
            switch (alt70) {
                case 1 :
                    // JPA.g:281:5: 'NOT'
                    {
                    string_literal239=(Token)match(input,61,FOLLOW_61_in_exists_expression2021); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal239_tree = (Object)adaptor.create(string_literal239);
                    adaptor.addChild(root_0, string_literal239_tree);
                    }

                    }
                    break;

            }

            string_literal240=(Token)match(input,84,FOLLOW_84_in_exists_expression2025); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal240_tree = (Object)adaptor.create(string_literal240);
            adaptor.addChild(root_0, string_literal240_tree);
            }
            pushFollow(FOLLOW_subquery_in_exists_expression2027);
            subquery241=subquery();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery241.getTree());

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
    // JPA.g:283:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
    public final JPAParser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
        JPAParser.all_or_any_expression_return retval = new JPAParser.all_or_any_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set242=null;
        JPAParser.subquery_return subquery243 = null;


        Object set242_tree=null;

        try {
            // JPA.g:284:2: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
            // JPA.g:284:4: ( 'ALL' | 'ANY' | 'SOME' ) subquery
            {
            root_0 = (Object)adaptor.nil();

            set242=(Token)input.LT(1);
            if ( (input.LA(1)>=85 && input.LA(1)<=87) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set242));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            pushFollow(FOLLOW_subquery_in_all_or_any_expression2049);
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
    // $ANTLR end "all_or_any_expression"

    public static class comparison_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "comparison_expression"
    // JPA.g:286:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
    public final JPAParser.comparison_expression_return comparison_expression() throws RecognitionException {
        JPAParser.comparison_expression_return retval = new JPAParser.comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set249=null;
        Token set253=null;
        Token set261=null;
        JPAParser.string_expression_return string_expression244 = null;

        JPAParser.comparison_operator_return comparison_operator245 = null;

        JPAParser.string_expression_return string_expression246 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression247 = null;

        JPAParser.boolean_expression_return boolean_expression248 = null;

        JPAParser.boolean_expression_return boolean_expression250 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression251 = null;

        JPAParser.enum_expression_return enum_expression252 = null;

        JPAParser.enum_expression_return enum_expression254 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression255 = null;

        JPAParser.datetime_expression_return datetime_expression256 = null;

        JPAParser.comparison_operator_return comparison_operator257 = null;

        JPAParser.datetime_expression_return datetime_expression258 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression259 = null;

        JPAParser.entity_expression_return entity_expression260 = null;

        JPAParser.entity_expression_return entity_expression262 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression263 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression264 = null;

        JPAParser.comparison_operator_return comparison_operator265 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression266 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression267 = null;


        Object set249_tree=null;
        Object set253_tree=null;
        Object set261_tree=null;

        try {
            // JPA.g:287:2: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
            int alt77=6;
            alt77 = dfa77.predict(input);
            switch (alt77) {
                case 1 :
                    // JPA.g:287:4: string_expression comparison_operator ( string_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_expression_in_comparison_expression2058);
                    string_expression244=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression244.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2060);
                    comparison_operator245=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator245.getTree());
                    // JPA.g:287:42: ( string_expression | all_or_any_expression )
                    int alt71=2;
                    int LA71_0 = input.LA(1);

                    if ( ((LA71_0>=AVG && LA71_0<=COUNT)||LA71_0==STRINGLITERAL||(LA71_0>=WORD && LA71_0<=NAMED_PARAMETER)||LA71_0==49||(LA71_0>=105 && LA71_0<=109)||LA71_0==114) ) {
                        alt71=1;
                    }
                    else if ( ((LA71_0>=85 && LA71_0<=87)) ) {
                        alt71=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 71, 0, input);

                        throw nvae;
                    }
                    switch (alt71) {
                        case 1 :
                            // JPA.g:287:43: string_expression
                            {
                            pushFollow(FOLLOW_string_expression_in_comparison_expression2063);
                            string_expression246=string_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression246.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:287:63: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2067);
                            all_or_any_expression247=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression247.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // JPA.g:288:4: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_expression_in_comparison_expression2073);
                    boolean_expression248=boolean_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression248.getTree());
                    set249=(Token)input.LT(1);
                    if ( (input.LA(1)>=88 && input.LA(1)<=89) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set249));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:288:36: ( boolean_expression | all_or_any_expression )
                    int alt72=2;
                    int LA72_0 = input.LA(1);

                    if ( ((LA72_0>=WORD && LA72_0<=NAMED_PARAMETER)||LA72_0==49||(LA72_0>=114 && LA72_0<=116)) ) {
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
                            // JPA.g:288:37: boolean_expression
                            {
                            pushFollow(FOLLOW_boolean_expression_in_comparison_expression2084);
                            boolean_expression250=boolean_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression250.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:288:58: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2088);
                            all_or_any_expression251=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression251.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // JPA.g:289:4: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_expression_in_comparison_expression2094);
                    enum_expression252=enum_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression252.getTree());
                    set253=(Token)input.LT(1);
                    if ( (input.LA(1)>=88 && input.LA(1)<=89) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set253));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:289:31: ( enum_expression | all_or_any_expression )
                    int alt73=2;
                    int LA73_0 = input.LA(1);

                    if ( ((LA73_0>=WORD && LA73_0<=NAMED_PARAMETER)||LA73_0==49||LA73_0==114) ) {
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
                            // JPA.g:289:32: enum_expression
                            {
                            pushFollow(FOLLOW_enum_expression_in_comparison_expression2103);
                            enum_expression254=enum_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression254.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:289:50: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2107);
                            all_or_any_expression255=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression255.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // JPA.g:290:4: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_expression_in_comparison_expression2113);
                    datetime_expression256=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression256.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2115);
                    comparison_operator257=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator257.getTree());
                    // JPA.g:290:44: ( datetime_expression | all_or_any_expression )
                    int alt74=2;
                    int LA74_0 = input.LA(1);

                    if ( ((LA74_0>=AVG && LA74_0<=COUNT)||(LA74_0>=WORD && LA74_0<=NAMED_PARAMETER)||LA74_0==49||(LA74_0>=102 && LA74_0<=104)||LA74_0==114) ) {
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
                            // JPA.g:290:45: datetime_expression
                            {
                            pushFollow(FOLLOW_datetime_expression_in_comparison_expression2118);
                            datetime_expression258=datetime_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression258.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:290:67: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2122);
                            all_or_any_expression259=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression259.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // JPA.g:291:4: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_entity_expression_in_comparison_expression2128);
                    entity_expression260=entity_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression260.getTree());
                    set261=(Token)input.LT(1);
                    if ( (input.LA(1)>=88 && input.LA(1)<=89) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set261));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:291:35: ( entity_expression | all_or_any_expression )
                    int alt75=2;
                    int LA75_0 = input.LA(1);

                    if ( ((LA75_0>=WORD && LA75_0<=NAMED_PARAMETER)||LA75_0==114) ) {
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
                            // JPA.g:291:36: entity_expression
                            {
                            pushFollow(FOLLOW_entity_expression_in_comparison_expression2139);
                            entity_expression262=entity_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression262.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:291:56: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2143);
                            all_or_any_expression263=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression263.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 6 :
                    // JPA.g:292:4: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression2149);
                    arithmetic_expression264=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression264.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2151);
                    comparison_operator265=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator265.getTree());
                    // JPA.g:292:46: ( arithmetic_expression | all_or_any_expression )
                    int alt76=2;
                    int LA76_0 = input.LA(1);

                    if ( ((LA76_0>=AVG && LA76_0<=COUNT)||LA76_0==LPAREN||LA76_0==INT_NUMERAL||(LA76_0>=WORD && LA76_0<=NAMED_PARAMETER)||LA76_0==49||(LA76_0>=64 && LA76_0<=65)||(LA76_0>=96 && LA76_0<=101)||(LA76_0>=113 && LA76_0<=114)) ) {
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
                            // JPA.g:292:47: arithmetic_expression
                            {
                            pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression2154);
                            arithmetic_expression266=arithmetic_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression266.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:292:71: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2158);
                            all_or_any_expression267=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression267.getTree());

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
    // JPA.g:294:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
    public final JPAParser.comparison_operator_return comparison_operator() throws RecognitionException {
        JPAParser.comparison_operator_return retval = new JPAParser.comparison_operator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set268=null;

        Object set268_tree=null;

        try {
            // JPA.g:295:2: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set268=(Token)input.LT(1);
            if ( (input.LA(1)>=88 && input.LA(1)<=93) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set268));
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
    // JPA.g:302:1: arithmetic_expression : ( simple_arithmetic_expression | subquery );
    public final JPAParser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
        JPAParser.arithmetic_expression_return retval = new JPAParser.arithmetic_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression269 = null;

        JPAParser.subquery_return subquery270 = null;



        try {
            // JPA.g:303:2: ( simple_arithmetic_expression | subquery )
            int alt78=2;
            int LA78_0 = input.LA(1);

            if ( ((LA78_0>=AVG && LA78_0<=COUNT)||LA78_0==LPAREN||LA78_0==INT_NUMERAL||(LA78_0>=WORD && LA78_0<=NAMED_PARAMETER)||(LA78_0>=64 && LA78_0<=65)||(LA78_0>=96 && LA78_0<=101)||(LA78_0>=113 && LA78_0<=114)) ) {
                alt78=1;
            }
            else if ( (LA78_0==49) ) {
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
                    // JPA.g:303:4: simple_arithmetic_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simple_arithmetic_expression_in_arithmetic_expression2202);
                    simple_arithmetic_expression269=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression269.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:304:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_arithmetic_expression2207);
                    subquery270=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery270.getTree());

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
    // JPA.g:306:1: simple_arithmetic_expression : ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )* ;
    public final JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression() throws RecognitionException {
        JPAParser.simple_arithmetic_expression_return retval = new JPAParser.simple_arithmetic_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set272=null;
        JPAParser.arithmetic_term_return arithmetic_term271 = null;

        JPAParser.arithmetic_term_return arithmetic_term273 = null;


        Object set272_tree=null;

        try {
            // JPA.g:307:2: ( ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )* )
            // JPA.g:307:4: ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:307:4: ( arithmetic_term )
            // JPA.g:307:5: arithmetic_term
            {
            pushFollow(FOLLOW_arithmetic_term_in_simple_arithmetic_expression2217);
            arithmetic_term271=arithmetic_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term271.getTree());

            }

            // JPA.g:307:22: ( ( '+' | '-' ) arithmetic_term )*
            loop79:
            do {
                int alt79=2;
                int LA79_0 = input.LA(1);

                if ( ((LA79_0>=64 && LA79_0<=65)) ) {
                    alt79=1;
                }


                switch (alt79) {
            	case 1 :
            	    // JPA.g:307:23: ( '+' | '-' ) arithmetic_term
            	    {
            	    set272=(Token)input.LT(1);
            	    if ( (input.LA(1)>=64 && input.LA(1)<=65) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set272));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmetic_term_in_simple_arithmetic_expression2231);
            	    arithmetic_term273=arithmetic_term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term273.getTree());

            	    }
            	    break;

            	default :
            	    break loop79;
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
    // JPA.g:309:1: arithmetic_term : ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )* ;
    public final JPAParser.arithmetic_term_return arithmetic_term() throws RecognitionException {
        JPAParser.arithmetic_term_return retval = new JPAParser.arithmetic_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set275=null;
        JPAParser.arithmetic_factor_return arithmetic_factor274 = null;

        JPAParser.arithmetic_factor_return arithmetic_factor276 = null;


        Object set275_tree=null;

        try {
            // JPA.g:310:2: ( ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )* )
            // JPA.g:310:4: ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:310:4: ( arithmetic_factor )
            // JPA.g:310:5: arithmetic_factor
            {
            pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term2243);
            arithmetic_factor274=arithmetic_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor274.getTree());

            }

            // JPA.g:310:24: ( ( '*' | '/' ) arithmetic_factor )*
            loop80:
            do {
                int alt80=2;
                int LA80_0 = input.LA(1);

                if ( ((LA80_0>=94 && LA80_0<=95)) ) {
                    alt80=1;
                }


                switch (alt80) {
            	case 1 :
            	    // JPA.g:310:25: ( '*' | '/' ) arithmetic_factor
            	    {
            	    set275=(Token)input.LT(1);
            	    if ( (input.LA(1)>=94 && input.LA(1)<=95) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set275));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term2257);
            	    arithmetic_factor276=arithmetic_factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor276.getTree());

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
    // $ANTLR end "arithmetic_term"

    public static class arithmetic_factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arithmetic_factor"
    // JPA.g:312:1: arithmetic_factor : ( '+' | '-' )? arithmetic_primary ;
    public final JPAParser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
        JPAParser.arithmetic_factor_return retval = new JPAParser.arithmetic_factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set277=null;
        JPAParser.arithmetic_primary_return arithmetic_primary278 = null;


        Object set277_tree=null;

        try {
            // JPA.g:313:2: ( ( '+' | '-' )? arithmetic_primary )
            // JPA.g:313:4: ( '+' | '-' )? arithmetic_primary
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:313:4: ( '+' | '-' )?
            int alt81=2;
            int LA81_0 = input.LA(1);

            if ( ((LA81_0>=64 && LA81_0<=65)) ) {
                alt81=1;
            }
            switch (alt81) {
                case 1 :
                    // JPA.g:
                    {
                    set277=(Token)input.LT(1);
                    if ( (input.LA(1)>=64 && input.LA(1)<=65) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set277));
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

            pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor2279);
            arithmetic_primary278=arithmetic_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary278.getTree());

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
    // JPA.g:315:1: arithmetic_primary : ( path_expression | numeric_literal | '(' simple_arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression );
    public final JPAParser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
        JPAParser.arithmetic_primary_return retval = new JPAParser.arithmetic_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal281=null;
        Token char_literal283=null;
        JPAParser.path_expression_return path_expression279 = null;

        JPAParser.numeric_literal_return numeric_literal280 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression282 = null;

        JPAParser.input_parameter_return input_parameter284 = null;

        JPAParser.functions_returning_numerics_return functions_returning_numerics285 = null;

        JPAParser.aggregate_expression_return aggregate_expression286 = null;


        Object char_literal281_tree=null;
        Object char_literal283_tree=null;

        try {
            // JPA.g:316:2: ( path_expression | numeric_literal | '(' simple_arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression )
            int alt82=6;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt82=1;
                }
                break;
            case INT_NUMERAL:
            case 113:
                {
                alt82=2;
                }
                break;
            case LPAREN:
                {
                alt82=3;
                }
                break;
            case NAMED_PARAMETER:
            case 114:
                {
                alt82=4;
                }
                break;
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
                {
                alt82=5;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt82=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 82, 0, input);

                throw nvae;
            }

            switch (alt82) {
                case 1 :
                    // JPA.g:316:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_arithmetic_primary2288);
                    path_expression279=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression279.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:317:4: numeric_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary2293);
                    numeric_literal280=numeric_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal280.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:318:4: '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal281=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary2298); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal281_tree = (Object)adaptor.create(char_literal281);
                    adaptor.addChild(root_0, char_literal281_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_arithmetic_primary2299);
                    simple_arithmetic_expression282=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression282.getTree());
                    char_literal283=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary2300); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal283_tree = (Object)adaptor.create(char_literal283);
                    adaptor.addChild(root_0, char_literal283_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:319:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_arithmetic_primary2305);
                    input_parameter284=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter284.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:320:4: functions_returning_numerics
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary2310);
                    functions_returning_numerics285=functions_returning_numerics();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics285.getTree());

                    }
                    break;
                case 6 :
                    // JPA.g:321:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary2315);
                    aggregate_expression286=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression286.getTree());

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
    // JPA.g:323:1: string_expression : ( string_primary | subquery );
    public final JPAParser.string_expression_return string_expression() throws RecognitionException {
        JPAParser.string_expression_return retval = new JPAParser.string_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.string_primary_return string_primary287 = null;

        JPAParser.subquery_return subquery288 = null;



        try {
            // JPA.g:324:2: ( string_primary | subquery )
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( ((LA83_0>=AVG && LA83_0<=COUNT)||LA83_0==STRINGLITERAL||(LA83_0>=WORD && LA83_0<=NAMED_PARAMETER)||(LA83_0>=105 && LA83_0<=109)||LA83_0==114) ) {
                alt83=1;
            }
            else if ( (LA83_0==49) ) {
                alt83=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 83, 0, input);

                throw nvae;
            }
            switch (alt83) {
                case 1 :
                    // JPA.g:324:4: string_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_primary_in_string_expression2324);
                    string_primary287=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary287.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:324:21: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_string_expression2328);
                    subquery288=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery288.getTree());

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
    // JPA.g:326:1: string_primary : ( path_expression | STRINGLITERAL | input_parameter | functions_returning_strings | aggregate_expression );
    public final JPAParser.string_primary_return string_primary() throws RecognitionException {
        JPAParser.string_primary_return retval = new JPAParser.string_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRINGLITERAL290=null;
        JPAParser.path_expression_return path_expression289 = null;

        JPAParser.input_parameter_return input_parameter291 = null;

        JPAParser.functions_returning_strings_return functions_returning_strings292 = null;

        JPAParser.aggregate_expression_return aggregate_expression293 = null;


        Object STRINGLITERAL290_tree=null;

        try {
            // JPA.g:327:2: ( path_expression | STRINGLITERAL | input_parameter | functions_returning_strings | aggregate_expression )
            int alt84=5;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt84=1;
                }
                break;
            case STRINGLITERAL:
                {
                alt84=2;
                }
                break;
            case NAMED_PARAMETER:
            case 114:
                {
                alt84=3;
                }
                break;
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
                {
                alt84=4;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt84=5;
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
                    // JPA.g:327:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_string_primary2337);
                    path_expression289=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression289.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:328:4: STRINGLITERAL
                    {
                    root_0 = (Object)adaptor.nil();

                    STRINGLITERAL290=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_string_primary2342); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRINGLITERAL290_tree = (Object)adaptor.create(STRINGLITERAL290);
                    adaptor.addChild(root_0, STRINGLITERAL290_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:329:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_string_primary2347);
                    input_parameter291=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter291.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:330:4: functions_returning_strings
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_strings_in_string_primary2352);
                    functions_returning_strings292=functions_returning_strings();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings292.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:331:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_string_primary2357);
                    aggregate_expression293=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression293.getTree());

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
    // JPA.g:333:1: datetime_expression : ( datetime_primary | subquery );
    public final JPAParser.datetime_expression_return datetime_expression() throws RecognitionException {
        JPAParser.datetime_expression_return retval = new JPAParser.datetime_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.datetime_primary_return datetime_primary294 = null;

        JPAParser.subquery_return subquery295 = null;



        try {
            // JPA.g:334:2: ( datetime_primary | subquery )
            int alt85=2;
            int LA85_0 = input.LA(1);

            if ( ((LA85_0>=AVG && LA85_0<=COUNT)||(LA85_0>=WORD && LA85_0<=NAMED_PARAMETER)||(LA85_0>=102 && LA85_0<=104)||LA85_0==114) ) {
                alt85=1;
            }
            else if ( (LA85_0==49) ) {
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
                    // JPA.g:334:4: datetime_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_primary_in_datetime_expression2366);
                    datetime_primary294=datetime_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_primary294.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:335:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_datetime_expression2371);
                    subquery295=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery295.getTree());

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
    // JPA.g:337:1: datetime_primary : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression );
    public final JPAParser.datetime_primary_return datetime_primary() throws RecognitionException {
        JPAParser.datetime_primary_return retval = new JPAParser.datetime_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression296 = null;

        JPAParser.input_parameter_return input_parameter297 = null;

        JPAParser.functions_returning_datetime_return functions_returning_datetime298 = null;

        JPAParser.aggregate_expression_return aggregate_expression299 = null;



        try {
            // JPA.g:338:2: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression )
            int alt86=4;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt86=1;
                }
                break;
            case NAMED_PARAMETER:
            case 114:
                {
                alt86=2;
                }
                break;
            case 102:
            case 103:
            case 104:
                {
                alt86=3;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt86=4;
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
                    // JPA.g:338:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_datetime_primary2380);
                    path_expression296=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression296.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:339:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_datetime_primary2385);
                    input_parameter297=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter297.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:340:4: functions_returning_datetime
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_datetime_in_datetime_primary2390);
                    functions_returning_datetime298=functions_returning_datetime();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime298.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:341:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_datetime_primary2395);
                    aggregate_expression299=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression299.getTree());

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
    // JPA.g:343:1: boolean_expression : ( boolean_primary | subquery );
    public final JPAParser.boolean_expression_return boolean_expression() throws RecognitionException {
        JPAParser.boolean_expression_return retval = new JPAParser.boolean_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.boolean_primary_return boolean_primary300 = null;

        JPAParser.subquery_return subquery301 = null;



        try {
            // JPA.g:344:2: ( boolean_primary | subquery )
            int alt87=2;
            int LA87_0 = input.LA(1);

            if ( ((LA87_0>=WORD && LA87_0<=NAMED_PARAMETER)||(LA87_0>=114 && LA87_0<=116)) ) {
                alt87=1;
            }
            else if ( (LA87_0==49) ) {
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
                    // JPA.g:344:4: boolean_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_primary_in_boolean_expression2404);
                    boolean_primary300=boolean_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_primary300.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:345:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_boolean_expression2409);
                    subquery301=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery301.getTree());

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
    // JPA.g:347:1: boolean_primary : ( path_expression | boolean_literal | input_parameter );
    public final JPAParser.boolean_primary_return boolean_primary() throws RecognitionException {
        JPAParser.boolean_primary_return retval = new JPAParser.boolean_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression302 = null;

        JPAParser.boolean_literal_return boolean_literal303 = null;

        JPAParser.input_parameter_return input_parameter304 = null;



        try {
            // JPA.g:348:2: ( path_expression | boolean_literal | input_parameter )
            int alt88=3;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt88=1;
                }
                break;
            case 115:
            case 116:
                {
                alt88=2;
                }
                break;
            case NAMED_PARAMETER:
            case 114:
                {
                alt88=3;
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
                    // JPA.g:348:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_boolean_primary2418);
                    path_expression302=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression302.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:349:4: boolean_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_literal_in_boolean_primary2423);
                    boolean_literal303=boolean_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal303.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:350:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_boolean_primary2428);
                    input_parameter304=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter304.getTree());

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
    // JPA.g:352:1: enum_expression : ( enum_primary | subquery );
    public final JPAParser.enum_expression_return enum_expression() throws RecognitionException {
        JPAParser.enum_expression_return retval = new JPAParser.enum_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.enum_primary_return enum_primary305 = null;

        JPAParser.subquery_return subquery306 = null;



        try {
            // JPA.g:353:2: ( enum_primary | subquery )
            int alt89=2;
            int LA89_0 = input.LA(1);

            if ( ((LA89_0>=WORD && LA89_0<=NAMED_PARAMETER)||LA89_0==114) ) {
                alt89=1;
            }
            else if ( (LA89_0==49) ) {
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
                    // JPA.g:353:4: enum_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_primary_in_enum_expression2437);
                    enum_primary305=enum_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_primary305.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:354:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_enum_expression2442);
                    subquery306=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery306.getTree());

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
    // JPA.g:356:1: enum_primary : ( path_expression | enum_literal | input_parameter );
    public final JPAParser.enum_primary_return enum_primary() throws RecognitionException {
        JPAParser.enum_primary_return retval = new JPAParser.enum_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression307 = null;

        JPAParser.enum_literal_return enum_literal308 = null;

        JPAParser.input_parameter_return input_parameter309 = null;



        try {
            // JPA.g:357:2: ( path_expression | enum_literal | input_parameter )
            int alt90=3;
            int LA90_0 = input.LA(1);

            if ( (LA90_0==WORD) ) {
                int LA90_1 = input.LA(2);

                if ( (LA90_1==53) ) {
                    alt90=1;
                }
                else if ( (LA90_1==EOF||LA90_1==HAVING||(LA90_1>=OR && LA90_1<=AND)||LA90_1==RPAREN||LA90_1==58||LA90_1==60||(LA90_1>=88 && LA90_1<=89)) ) {
                    alt90=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA90_0==NAMED_PARAMETER||LA90_0==114) ) {
                alt90=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 90, 0, input);

                throw nvae;
            }
            switch (alt90) {
                case 1 :
                    // JPA.g:357:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_enum_primary2451);
                    path_expression307=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression307.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:358:4: enum_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_literal_in_enum_primary2456);
                    enum_literal308=enum_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal308.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:359:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_enum_primary2461);
                    input_parameter309=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter309.getTree());

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
    // JPA.g:361:1: entity_expression : ( path_expression | simple_entity_expression );
    public final JPAParser.entity_expression_return entity_expression() throws RecognitionException {
        JPAParser.entity_expression_return retval = new JPAParser.entity_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression310 = null;

        JPAParser.simple_entity_expression_return simple_entity_expression311 = null;



        try {
            // JPA.g:362:2: ( path_expression | simple_entity_expression )
            int alt91=2;
            int LA91_0 = input.LA(1);

            if ( (LA91_0==WORD) ) {
                int LA91_1 = input.LA(2);

                if ( (LA91_1==53) ) {
                    alt91=1;
                }
                else if ( (LA91_1==EOF||LA91_1==HAVING||(LA91_1>=OR && LA91_1<=AND)||LA91_1==RPAREN||LA91_1==58||(LA91_1>=60 && LA91_1<=61)||LA91_1==82||(LA91_1>=88 && LA91_1<=89)) ) {
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
                    // JPA.g:362:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_entity_expression2470);
                    path_expression310=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression310.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:363:4: simple_entity_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simple_entity_expression_in_entity_expression2475);
                    simple_entity_expression311=simple_entity_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression311.getTree());

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
    // JPA.g:365:1: simple_entity_expression : ( identification_variable | input_parameter );
    public final JPAParser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
        JPAParser.simple_entity_expression_return retval = new JPAParser.simple_entity_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.identification_variable_return identification_variable312 = null;

        JPAParser.input_parameter_return input_parameter313 = null;



        try {
            // JPA.g:366:2: ( identification_variable | input_parameter )
            int alt92=2;
            int LA92_0 = input.LA(1);

            if ( (LA92_0==WORD) ) {
                alt92=1;
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
                    // JPA.g:366:4: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_simple_entity_expression2484);
                    identification_variable312=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable312.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:367:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_simple_entity_expression2489);
                    input_parameter313=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter313.getTree());

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
    // JPA.g:369:1: functions_returning_numerics : ( 'LENGTH' '(' string_primary ')' | 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')' | 'ABS' '(' simple_arithmetic_expression ')' | 'SQRT' '(' simple_arithmetic_expression ')' | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'SIZE' '(' path_expression ')' );
    public final JPAParser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
        JPAParser.functions_returning_numerics_return retval = new JPAParser.functions_returning_numerics_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal314=null;
        Token char_literal315=null;
        Token char_literal317=null;
        Token string_literal318=null;
        Token char_literal319=null;
        Token char_literal321=null;
        Token char_literal323=null;
        Token char_literal325=null;
        Token string_literal326=null;
        Token char_literal327=null;
        Token char_literal329=null;
        Token string_literal330=null;
        Token char_literal331=null;
        Token char_literal333=null;
        Token string_literal334=null;
        Token char_literal335=null;
        Token char_literal337=null;
        Token char_literal339=null;
        Token string_literal340=null;
        Token char_literal341=null;
        Token char_literal343=null;
        JPAParser.string_primary_return string_primary316 = null;

        JPAParser.string_primary_return string_primary320 = null;

        JPAParser.string_primary_return string_primary322 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression324 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression328 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression332 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression336 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression338 = null;

        JPAParser.path_expression_return path_expression342 = null;


        Object string_literal314_tree=null;
        Object char_literal315_tree=null;
        Object char_literal317_tree=null;
        Object string_literal318_tree=null;
        Object char_literal319_tree=null;
        Object char_literal321_tree=null;
        Object char_literal323_tree=null;
        Object char_literal325_tree=null;
        Object string_literal326_tree=null;
        Object char_literal327_tree=null;
        Object char_literal329_tree=null;
        Object string_literal330_tree=null;
        Object char_literal331_tree=null;
        Object char_literal333_tree=null;
        Object string_literal334_tree=null;
        Object char_literal335_tree=null;
        Object char_literal337_tree=null;
        Object char_literal339_tree=null;
        Object string_literal340_tree=null;
        Object char_literal341_tree=null;
        Object char_literal343_tree=null;

        try {
            // JPA.g:370:2: ( 'LENGTH' '(' string_primary ')' | 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')' | 'ABS' '(' simple_arithmetic_expression ')' | 'SQRT' '(' simple_arithmetic_expression ')' | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'SIZE' '(' path_expression ')' )
            int alt94=6;
            switch ( input.LA(1) ) {
            case 96:
                {
                alt94=1;
                }
                break;
            case 97:
                {
                alt94=2;
                }
                break;
            case 98:
                {
                alt94=3;
                }
                break;
            case 99:
                {
                alt94=4;
                }
                break;
            case 100:
                {
                alt94=5;
                }
                break;
            case 101:
                {
                alt94=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 94, 0, input);

                throw nvae;
            }

            switch (alt94) {
                case 1 :
                    // JPA.g:370:4: 'LENGTH' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal314=(Token)match(input,96,FOLLOW_96_in_functions_returning_numerics2498); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal314_tree = (Object)adaptor.create(string_literal314);
                    adaptor.addChild(root_0, string_literal314_tree);
                    }
                    char_literal315=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2500); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal315_tree = (Object)adaptor.create(char_literal315);
                    adaptor.addChild(root_0, char_literal315_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics2501);
                    string_primary316=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary316.getTree());
                    char_literal317=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2502); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal317_tree = (Object)adaptor.create(char_literal317);
                    adaptor.addChild(root_0, char_literal317_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:371:4: 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal318=(Token)match(input,97,FOLLOW_97_in_functions_returning_numerics2507); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal318_tree = (Object)adaptor.create(string_literal318);
                    adaptor.addChild(root_0, string_literal318_tree);
                    }
                    char_literal319=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2509); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal319_tree = (Object)adaptor.create(char_literal319);
                    adaptor.addChild(root_0, char_literal319_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics2510);
                    string_primary320=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary320.getTree());
                    char_literal321=(Token)match(input,47,FOLLOW_47_in_functions_returning_numerics2511); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal321_tree = (Object)adaptor.create(char_literal321);
                    adaptor.addChild(root_0, char_literal321_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics2513);
                    string_primary322=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary322.getTree());
                    // JPA.g:371:48: ( ',' simple_arithmetic_expression )?
                    int alt93=2;
                    int LA93_0 = input.LA(1);

                    if ( (LA93_0==47) ) {
                        alt93=1;
                    }
                    switch (alt93) {
                        case 1 :
                            // JPA.g:371:49: ',' simple_arithmetic_expression
                            {
                            char_literal323=(Token)match(input,47,FOLLOW_47_in_functions_returning_numerics2515); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal323_tree = (Object)adaptor.create(char_literal323);
                            adaptor.addChild(root_0, char_literal323_tree);
                            }
                            pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2517);
                            simple_arithmetic_expression324=simple_arithmetic_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression324.getTree());

                            }
                            break;

                    }

                    char_literal325=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2520); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal325_tree = (Object)adaptor.create(char_literal325);
                    adaptor.addChild(root_0, char_literal325_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:372:4: 'ABS' '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal326=(Token)match(input,98,FOLLOW_98_in_functions_returning_numerics2525); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal326_tree = (Object)adaptor.create(string_literal326);
                    adaptor.addChild(root_0, string_literal326_tree);
                    }
                    char_literal327=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2527); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal327_tree = (Object)adaptor.create(char_literal327);
                    adaptor.addChild(root_0, char_literal327_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2528);
                    simple_arithmetic_expression328=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression328.getTree());
                    char_literal329=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2529); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal329_tree = (Object)adaptor.create(char_literal329);
                    adaptor.addChild(root_0, char_literal329_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:373:4: 'SQRT' '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal330=(Token)match(input,99,FOLLOW_99_in_functions_returning_numerics2534); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal330_tree = (Object)adaptor.create(string_literal330);
                    adaptor.addChild(root_0, string_literal330_tree);
                    }
                    char_literal331=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2536); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal331_tree = (Object)adaptor.create(char_literal331);
                    adaptor.addChild(root_0, char_literal331_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2537);
                    simple_arithmetic_expression332=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression332.getTree());
                    char_literal333=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2538); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal333_tree = (Object)adaptor.create(char_literal333);
                    adaptor.addChild(root_0, char_literal333_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:374:4: 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal334=(Token)match(input,100,FOLLOW_100_in_functions_returning_numerics2543); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal334_tree = (Object)adaptor.create(string_literal334);
                    adaptor.addChild(root_0, string_literal334_tree);
                    }
                    char_literal335=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2545); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal335_tree = (Object)adaptor.create(char_literal335);
                    adaptor.addChild(root_0, char_literal335_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2546);
                    simple_arithmetic_expression336=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression336.getTree());
                    char_literal337=(Token)match(input,47,FOLLOW_47_in_functions_returning_numerics2547); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal337_tree = (Object)adaptor.create(char_literal337);
                    adaptor.addChild(root_0, char_literal337_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2549);
                    simple_arithmetic_expression338=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression338.getTree());
                    char_literal339=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2550); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal339_tree = (Object)adaptor.create(char_literal339);
                    adaptor.addChild(root_0, char_literal339_tree);
                    }

                    }
                    break;
                case 6 :
                    // JPA.g:375:4: 'SIZE' '(' path_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal340=(Token)match(input,101,FOLLOW_101_in_functions_returning_numerics2555); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal340_tree = (Object)adaptor.create(string_literal340);
                    adaptor.addChild(root_0, string_literal340_tree);
                    }
                    char_literal341=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2557); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal341_tree = (Object)adaptor.create(char_literal341);
                    adaptor.addChild(root_0, char_literal341_tree);
                    }
                    pushFollow(FOLLOW_path_expression_in_functions_returning_numerics2558);
                    path_expression342=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression342.getTree());
                    char_literal343=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2559); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal343_tree = (Object)adaptor.create(char_literal343);
                    adaptor.addChild(root_0, char_literal343_tree);
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
    // JPA.g:377:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
    public final JPAParser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
        JPAParser.functions_returning_datetime_return retval = new JPAParser.functions_returning_datetime_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set344=null;

        Object set344_tree=null;

        try {
            // JPA.g:378:2: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set344=(Token)input.LT(1);
            if ( (input.LA(1)>=102 && input.LA(1)<=104) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set344));
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
    // JPA.g:382:1: functions_returning_strings : ( 'CONCAT' '(' string_primary ',' string_primary ')' | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')' | 'LOWER' '(' string_primary ')' | 'UPPER' '(' string_primary ')' );
    public final JPAParser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
        JPAParser.functions_returning_strings_return retval = new JPAParser.functions_returning_strings_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal345=null;
        Token char_literal346=null;
        Token char_literal348=null;
        Token char_literal350=null;
        Token string_literal351=null;
        Token char_literal352=null;
        Token char_literal354=null;
        Token char_literal356=null;
        Token char_literal358=null;
        Token string_literal359=null;
        Token char_literal360=null;
        Token TRIM_CHARACTER362=null;
        Token string_literal363=null;
        Token char_literal365=null;
        Token string_literal366=null;
        Token char_literal367=null;
        Token char_literal369=null;
        Token string_literal370=null;
        Token char_literal371=null;
        Token char_literal373=null;
        JPAParser.string_primary_return string_primary347 = null;

        JPAParser.string_primary_return string_primary349 = null;

        JPAParser.string_primary_return string_primary353 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression355 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression357 = null;

        JPAParser.trim_specification_return trim_specification361 = null;

        JPAParser.string_primary_return string_primary364 = null;

        JPAParser.string_primary_return string_primary368 = null;

        JPAParser.string_primary_return string_primary372 = null;


        Object string_literal345_tree=null;
        Object char_literal346_tree=null;
        Object char_literal348_tree=null;
        Object char_literal350_tree=null;
        Object string_literal351_tree=null;
        Object char_literal352_tree=null;
        Object char_literal354_tree=null;
        Object char_literal356_tree=null;
        Object char_literal358_tree=null;
        Object string_literal359_tree=null;
        Object char_literal360_tree=null;
        Object TRIM_CHARACTER362_tree=null;
        Object string_literal363_tree=null;
        Object char_literal365_tree=null;
        Object string_literal366_tree=null;
        Object char_literal367_tree=null;
        Object char_literal369_tree=null;
        Object string_literal370_tree=null;
        Object char_literal371_tree=null;
        Object char_literal373_tree=null;

        try {
            // JPA.g:383:2: ( 'CONCAT' '(' string_primary ',' string_primary ')' | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')' | 'LOWER' '(' string_primary ')' | 'UPPER' '(' string_primary ')' )
            int alt98=5;
            switch ( input.LA(1) ) {
            case 105:
                {
                alt98=1;
                }
                break;
            case 106:
                {
                alt98=2;
                }
                break;
            case 107:
                {
                alt98=3;
                }
                break;
            case 108:
                {
                alt98=4;
                }
                break;
            case 109:
                {
                alt98=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 98, 0, input);

                throw nvae;
            }

            switch (alt98) {
                case 1 :
                    // JPA.g:383:4: 'CONCAT' '(' string_primary ',' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal345=(Token)match(input,105,FOLLOW_105_in_functions_returning_strings2587); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal345_tree = (Object)adaptor.create(string_literal345);
                    adaptor.addChild(root_0, string_literal345_tree);
                    }
                    char_literal346=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2589); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal346_tree = (Object)adaptor.create(char_literal346);
                    adaptor.addChild(root_0, char_literal346_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2590);
                    string_primary347=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary347.getTree());
                    char_literal348=(Token)match(input,47,FOLLOW_47_in_functions_returning_strings2591); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal348_tree = (Object)adaptor.create(char_literal348);
                    adaptor.addChild(root_0, char_literal348_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2593);
                    string_primary349=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary349.getTree());
                    char_literal350=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2594); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal350_tree = (Object)adaptor.create(char_literal350);
                    adaptor.addChild(root_0, char_literal350_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:384:4: 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal351=(Token)match(input,106,FOLLOW_106_in_functions_returning_strings2599); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal351_tree = (Object)adaptor.create(string_literal351);
                    adaptor.addChild(root_0, string_literal351_tree);
                    }
                    char_literal352=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2601); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal352_tree = (Object)adaptor.create(char_literal352);
                    adaptor.addChild(root_0, char_literal352_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2602);
                    string_primary353=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary353.getTree());
                    char_literal354=(Token)match(input,47,FOLLOW_47_in_functions_returning_strings2603); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal354_tree = (Object)adaptor.create(char_literal354);
                    adaptor.addChild(root_0, char_literal354_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2604);
                    simple_arithmetic_expression355=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression355.getTree());
                    char_literal356=(Token)match(input,47,FOLLOW_47_in_functions_returning_strings2605); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal356_tree = (Object)adaptor.create(char_literal356);
                    adaptor.addChild(root_0, char_literal356_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2607);
                    simple_arithmetic_expression357=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression357.getTree());
                    char_literal358=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2608); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal358_tree = (Object)adaptor.create(char_literal358);
                    adaptor.addChild(root_0, char_literal358_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:385:4: 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal359=(Token)match(input,107,FOLLOW_107_in_functions_returning_strings2613); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal359_tree = (Object)adaptor.create(string_literal359);
                    adaptor.addChild(root_0, string_literal359_tree);
                    }
                    char_literal360=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2615); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal360_tree = (Object)adaptor.create(char_literal360);
                    adaptor.addChild(root_0, char_literal360_tree);
                    }
                    // JPA.g:385:14: ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )?
                    int alt97=2;
                    int LA97_0 = input.LA(1);

                    if ( (LA97_0==TRIM_CHARACTER||LA97_0==46||(LA97_0>=110 && LA97_0<=112)) ) {
                        alt97=1;
                    }
                    switch (alt97) {
                        case 1 :
                            // JPA.g:385:15: ( trim_specification )? ( TRIM_CHARACTER )? 'FROM'
                            {
                            // JPA.g:385:15: ( trim_specification )?
                            int alt95=2;
                            int LA95_0 = input.LA(1);

                            if ( ((LA95_0>=110 && LA95_0<=112)) ) {
                                alt95=1;
                            }
                            switch (alt95) {
                                case 1 :
                                    // JPA.g:385:16: trim_specification
                                    {
                                    pushFollow(FOLLOW_trim_specification_in_functions_returning_strings2618);
                                    trim_specification361=trim_specification();

                                    state._fsp--;
                                    if (state.failed) return retval;
                                    if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification361.getTree());

                                    }
                                    break;

                            }

                            // JPA.g:385:37: ( TRIM_CHARACTER )?
                            int alt96=2;
                            int LA96_0 = input.LA(1);

                            if ( (LA96_0==TRIM_CHARACTER) ) {
                                alt96=1;
                            }
                            switch (alt96) {
                                case 1 :
                                    // JPA.g:385:38: TRIM_CHARACTER
                                    {
                                    TRIM_CHARACTER362=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_functions_returning_strings2623); if (state.failed) return retval;
                                    if ( state.backtracking==0 ) {
                                    TRIM_CHARACTER362_tree = (Object)adaptor.create(TRIM_CHARACTER362);
                                    adaptor.addChild(root_0, TRIM_CHARACTER362_tree);
                                    }

                                    }
                                    break;

                            }

                            string_literal363=(Token)match(input,46,FOLLOW_46_in_functions_returning_strings2627); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal363_tree = (Object)adaptor.create(string_literal363);
                            adaptor.addChild(root_0, string_literal363_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2631);
                    string_primary364=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary364.getTree());
                    char_literal365=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2632); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal365_tree = (Object)adaptor.create(char_literal365);
                    adaptor.addChild(root_0, char_literal365_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:386:4: 'LOWER' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal366=(Token)match(input,108,FOLLOW_108_in_functions_returning_strings2637); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal366_tree = (Object)adaptor.create(string_literal366);
                    adaptor.addChild(root_0, string_literal366_tree);
                    }
                    char_literal367=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2639); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal367_tree = (Object)adaptor.create(char_literal367);
                    adaptor.addChild(root_0, char_literal367_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2640);
                    string_primary368=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary368.getTree());
                    char_literal369=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2641); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal369_tree = (Object)adaptor.create(char_literal369);
                    adaptor.addChild(root_0, char_literal369_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:387:4: 'UPPER' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal370=(Token)match(input,109,FOLLOW_109_in_functions_returning_strings2646); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal370_tree = (Object)adaptor.create(string_literal370);
                    adaptor.addChild(root_0, string_literal370_tree);
                    }
                    char_literal371=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2648); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal371_tree = (Object)adaptor.create(char_literal371);
                    adaptor.addChild(root_0, char_literal371_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2649);
                    string_primary372=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary372.getTree());
                    char_literal373=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2650); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal373_tree = (Object)adaptor.create(char_literal373);
                    adaptor.addChild(root_0, char_literal373_tree);
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
    // JPA.g:389:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
    public final JPAParser.trim_specification_return trim_specification() throws RecognitionException {
        JPAParser.trim_specification_return retval = new JPAParser.trim_specification_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set374=null;

        Object set374_tree=null;

        try {
            // JPA.g:390:2: ( 'LEADING' | 'TRAILING' | 'BOTH' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set374=(Token)input.LT(1);
            if ( (input.LA(1)>=110 && input.LA(1)<=112) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set374));
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
    // JPA.g:395:1: abstract_schema_name : WORD ;
    public final JPAParser.abstract_schema_name_return abstract_schema_name() throws RecognitionException {
        JPAParser.abstract_schema_name_return retval = new JPAParser.abstract_schema_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD375=null;

        Object WORD375_tree=null;

        try {
            // JPA.g:396:4: ( WORD )
            // JPA.g:396:6: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD375=(Token)match(input,WORD,FOLLOW_WORD_in_abstract_schema_name2681); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD375_tree = (Object)adaptor.create(WORD375);
            adaptor.addChild(root_0, WORD375_tree);
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
    // JPA.g:399:1: pattern_value : WORD ;
    public final JPAParser.pattern_value_return pattern_value() throws RecognitionException {
        JPAParser.pattern_value_return retval = new JPAParser.pattern_value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD376=null;

        Object WORD376_tree=null;

        try {
            // JPA.g:400:2: ( WORD )
            // JPA.g:400:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD376=(Token)match(input,WORD,FOLLOW_WORD_in_pattern_value2691); if (state.failed) return retval;
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
    // $ANTLR end "pattern_value"

    public static class numeric_literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numeric_literal"
    // JPA.g:403:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
    public final JPAParser.numeric_literal_return numeric_literal() throws RecognitionException {
        JPAParser.numeric_literal_return retval = new JPAParser.numeric_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal377=null;
        Token INT_NUMERAL378=null;

        Object string_literal377_tree=null;
        Object INT_NUMERAL378_tree=null;

        try {
            // JPA.g:404:2: ( ( '0x' )? INT_NUMERAL )
            // JPA.g:404:4: ( '0x' )? INT_NUMERAL
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:404:4: ( '0x' )?
            int alt99=2;
            int LA99_0 = input.LA(1);

            if ( (LA99_0==113) ) {
                alt99=1;
            }
            switch (alt99) {
                case 1 :
                    // JPA.g:404:5: '0x'
                    {
                    string_literal377=(Token)match(input,113,FOLLOW_113_in_numeric_literal2702); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal377_tree = (Object)adaptor.create(string_literal377);
                    adaptor.addChild(root_0, string_literal377_tree);
                    }

                    }
                    break;

            }

            INT_NUMERAL378=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal2706); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            INT_NUMERAL378_tree = (Object)adaptor.create(INT_NUMERAL378);
            adaptor.addChild(root_0, INT_NUMERAL378_tree);
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
    // JPA.g:406:1: input_parameter : ( '?' INT_NUMERAL -> ^( T_PARAMETER[] '?' INT_NUMERAL ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) );
    public final JPAParser.input_parameter_return input_parameter() throws RecognitionException {
        JPAParser.input_parameter_return retval = new JPAParser.input_parameter_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal379=null;
        Token INT_NUMERAL380=null;
        Token NAMED_PARAMETER381=null;

        Object char_literal379_tree=null;
        Object INT_NUMERAL380_tree=null;
        Object NAMED_PARAMETER381_tree=null;
        RewriteRuleTokenStream stream_114=new RewriteRuleTokenStream(adaptor,"token 114");
        RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
        RewriteRuleTokenStream stream_INT_NUMERAL=new RewriteRuleTokenStream(adaptor,"token INT_NUMERAL");

        try {
            // JPA.g:407:2: ( '?' INT_NUMERAL -> ^( T_PARAMETER[] '?' INT_NUMERAL ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) )
            int alt100=2;
            int LA100_0 = input.LA(1);

            if ( (LA100_0==114) ) {
                alt100=1;
            }
            else if ( (LA100_0==NAMED_PARAMETER) ) {
                alt100=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 100, 0, input);

                throw nvae;
            }
            switch (alt100) {
                case 1 :
                    // JPA.g:407:4: '?' INT_NUMERAL
                    {
                    char_literal379=(Token)match(input,114,FOLLOW_114_in_input_parameter2716); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_114.add(char_literal379);

                    INT_NUMERAL380=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_input_parameter2718); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_INT_NUMERAL.add(INT_NUMERAL380);



                    // AST REWRITE
                    // elements: INT_NUMERAL, 114
                    // token labels:
                    // rule labels: retval
                    // token list labels:
                    // rule list labels:
                    // wildcard labels:
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 407:20: -> ^( T_PARAMETER[] '?' INT_NUMERAL )
                    {
                        // JPA.g:407:23: ^( T_PARAMETER[] '?' INT_NUMERAL )
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
                    // JPA.g:408:4: NAMED_PARAMETER
                    {
                    NAMED_PARAMETER381=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter2738); if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER381);



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
                    // 408:20: -> ^( T_PARAMETER[] NAMED_PARAMETER )
                    {
                        // JPA.g:408:23: ^( T_PARAMETER[] NAMED_PARAMETER )
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
    // JPA.g:410:1: literal : WORD ;
    public final JPAParser.literal_return literal() throws RecognitionException {
        JPAParser.literal_return retval = new JPAParser.literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD382=null;

        Object WORD382_tree=null;

        try {
            // JPA.g:411:2: ( WORD )
            // JPA.g:411:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD382=(Token)match(input,WORD,FOLLOW_WORD_in_literal2760); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD382_tree = (Object)adaptor.create(WORD382);
            adaptor.addChild(root_0, WORD382_tree);
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
    // JPA.g:413:1: constructor_name : WORD ;
    public final JPAParser.constructor_name_return constructor_name() throws RecognitionException {
        JPAParser.constructor_name_return retval = new JPAParser.constructor_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD383=null;

        Object WORD383_tree=null;

        try {
            // JPA.g:414:2: ( WORD )
            // JPA.g:414:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD383=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name2769); if (state.failed) return retval;
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
    // $ANTLR end "constructor_name"

    public static class enum_literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "enum_literal"
    // JPA.g:416:1: enum_literal : WORD ;
    public final JPAParser.enum_literal_return enum_literal() throws RecognitionException {
        JPAParser.enum_literal_return retval = new JPAParser.enum_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD384=null;

        Object WORD384_tree=null;

        try {
            // JPA.g:417:2: ( WORD )
            // JPA.g:417:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD384=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal2810); if (state.failed) return retval;
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
    // $ANTLR end "enum_literal"

    public static class boolean_literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_literal"
    // JPA.g:419:1: boolean_literal : ( 'true' | 'false' );
    public final JPAParser.boolean_literal_return boolean_literal() throws RecognitionException {
        JPAParser.boolean_literal_return retval = new JPAParser.boolean_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set385=null;

        Object set385_tree=null;

        try {
            // JPA.g:420:2: ( 'true' | 'false' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set385=(Token)input.LT(1);
            if ( (input.LA(1)>=115 && input.LA(1)<=116) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set385));
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
    // JPA.g:424:1: field : ( WORD | 'GROUP' );
    public final JPAParser.field_return field() throws RecognitionException {
        JPAParser.field_return retval = new JPAParser.field_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set386=null;

        Object set386_tree=null;

        try {
            // JPA.g:425:4: ( WORD | 'GROUP' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set386=(Token)input.LT(1);
            if ( input.LA(1)==WORD||input.LA(1)==58 ) {
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
    // $ANTLR end "field"

    public static class identification_variable_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "identification_variable"
    // JPA.g:427:1: identification_variable : WORD ;
    public final JPAParser.identification_variable_return identification_variable() throws RecognitionException {
        JPAParser.identification_variable_return retval = new JPAParser.identification_variable_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD387=null;

        Object WORD387_tree=null;

        try {
            // JPA.g:428:4: ( WORD )
            // JPA.g:428:6: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD387=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable2851); if (state.failed) return retval;
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
    // $ANTLR end "identification_variable"

    public static class parameter_name_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameter_name"
    // JPA.g:430:1: parameter_name : WORD ( '.' WORD )* ;
    public final JPAParser.parameter_name_return parameter_name() throws RecognitionException {
        JPAParser.parameter_name_return retval = new JPAParser.parameter_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD388=null;
        Token char_literal389=null;
        Token WORD390=null;

        Object WORD388_tree=null;
        Object char_literal389_tree=null;
        Object WORD390_tree=null;

        try {
            // JPA.g:431:4: ( WORD ( '.' WORD )* )
            // JPA.g:431:6: WORD ( '.' WORD )*
            {
            root_0 = (Object)adaptor.nil();

            WORD388=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name2862); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD388_tree = (Object)adaptor.create(WORD388);
            adaptor.addChild(root_0, WORD388_tree);
            }
            // JPA.g:431:11: ( '.' WORD )*
            loop101:
            do {
                int alt101=2;
                int LA101_0 = input.LA(1);

                if ( (LA101_0==53) ) {
                    alt101=1;
                }


                switch (alt101) {
            	case 1 :
            	    // JPA.g:431:12: '.' WORD
            	    {
            	    char_literal389=(Token)match(input,53,FOLLOW_53_in_parameter_name2865); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal389_tree = (Object)adaptor.create(char_literal389);
            	    adaptor.addChild(root_0, char_literal389_tree);
            	    }
            	    WORD390=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name2868); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    WORD390_tree = (Object)adaptor.create(WORD390);
            	    adaptor.addChild(root_0, WORD390_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop101;
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
        // JPA.g:118:44: ( field )
        // JPA.g:118:44: field
        {
        pushFollow(FOLLOW_field_in_synpred20_JPA730);
        field();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred20_JPA

    // $ANTLR start synpred23_JPA
    public final void synpred23_JPA_fragment() throws RecognitionException {
        // JPA.g:128:46: ( field )
        // JPA.g:128:46: field
        {
        pushFollow(FOLLOW_field_in_synpred23_JPA807);
        field();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred23_JPA

    // $ANTLR start synpred57_JPA
    public final void synpred57_JPA_fragment() throws RecognitionException {
        // JPA.g:216:5: ( 'NOT' )
        // JPA.g:216:5: 'NOT'
        {
        match(input,61,FOLLOW_61_in_synpred57_JPA1506); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred57_JPA

    // $ANTLR start synpred58_JPA
    public final void synpred58_JPA_fragment() throws RecognitionException {
        // JPA.g:216:4: ( ( 'NOT' )? simple_cond_expression )
        // JPA.g:216:4: ( 'NOT' )? simple_cond_expression
        {
        // JPA.g:216:4: ( 'NOT' )?
        int alt106=2;
        int LA106_0 = input.LA(1);

        if ( (LA106_0==61) ) {
            int LA106_1 = input.LA(2);

            if ( (synpred57_JPA()) ) {
                alt106=1;
            }
        }
        switch (alt106) {
            case 1 :
                // JPA.g:216:5: 'NOT'
                {
                match(input,61,FOLLOW_61_in_synpred58_JPA1506); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_simple_cond_expression_in_synpred58_JPA1510);
        simple_cond_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred58_JPA

    // $ANTLR start synpred59_JPA
    public final void synpred59_JPA_fragment() throws RecognitionException {
        // JPA.g:220:4: ( comparison_expression )
        // JPA.g:220:4: comparison_expression
        {
        pushFollow(FOLLOW_comparison_expression_in_synpred59_JPA1543);
        comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred59_JPA

    // $ANTLR start synpred60_JPA
    public final void synpred60_JPA_fragment() throws RecognitionException {
        // JPA.g:221:4: ( between_expression )
        // JPA.g:221:4: between_expression
        {
        pushFollow(FOLLOW_between_expression_in_synpred60_JPA1548);
        between_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred60_JPA

    // $ANTLR start synpred61_JPA
    public final void synpred61_JPA_fragment() throws RecognitionException {
        // JPA.g:222:4: ( like_expression )
        // JPA.g:222:4: like_expression
        {
        pushFollow(FOLLOW_like_expression_in_synpred61_JPA1553);
        like_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred61_JPA

    // $ANTLR start synpred62_JPA
    public final void synpred62_JPA_fragment() throws RecognitionException {
        // JPA.g:223:4: ( in_expression )
        // JPA.g:223:4: in_expression
        {
        pushFollow(FOLLOW_in_expression_in_synpred62_JPA1558);
        in_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred62_JPA

    // $ANTLR start synpred63_JPA
    public final void synpred63_JPA_fragment() throws RecognitionException {
        // JPA.g:224:4: ( null_comparison_expression )
        // JPA.g:224:4: null_comparison_expression
        {
        pushFollow(FOLLOW_null_comparison_expression_in_synpred63_JPA1563);
        null_comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred63_JPA

    // $ANTLR start synpred64_JPA
    public final void synpred64_JPA_fragment() throws RecognitionException {
        // JPA.g:225:4: ( empty_collection_comparison_expression )
        // JPA.g:225:4: empty_collection_comparison_expression
        {
        pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred64_JPA1568);
        empty_collection_comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred64_JPA

    // $ANTLR start synpred65_JPA
    public final void synpred65_JPA_fragment() throws RecognitionException {
        // JPA.g:226:4: ( collection_member_expression )
        // JPA.g:226:4: collection_member_expression
        {
        pushFollow(FOLLOW_collection_member_expression_in_synpred65_JPA1573);
        collection_member_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred65_JPA

    // $ANTLR start synpred84_JPA
    public final void synpred84_JPA_fragment() throws RecognitionException {
        // JPA.g:253:4: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
        // JPA.g:253:4: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
        {
        pushFollow(FOLLOW_arithmetic_expression_in_synpred84_JPA1815);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:253:26: ( 'NOT' )?
        int alt107=2;
        int LA107_0 = input.LA(1);

        if ( (LA107_0==61) ) {
            alt107=1;
        }
        switch (alt107) {
            case 1 :
                // JPA.g:253:27: 'NOT'
                {
                match(input,61,FOLLOW_61_in_synpred84_JPA1818); if (state.failed) return ;

                }
                break;

        }

        match(input,76,FOLLOW_76_in_synpred84_JPA1822); if (state.failed) return ;
        pushFollow(FOLLOW_arithmetic_expression_in_synpred84_JPA1824);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,AND,FOLLOW_AND_in_synpred84_JPA1826); if (state.failed) return ;
        pushFollow(FOLLOW_arithmetic_expression_in_synpred84_JPA1828);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred84_JPA

    // $ANTLR start synpred86_JPA
    public final void synpred86_JPA_fragment() throws RecognitionException {
        // JPA.g:254:4: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
        // JPA.g:254:4: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
        {
        pushFollow(FOLLOW_string_expression_in_synpred86_JPA1833);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:254:22: ( 'NOT' )?
        int alt108=2;
        int LA108_0 = input.LA(1);

        if ( (LA108_0==61) ) {
            alt108=1;
        }
        switch (alt108) {
            case 1 :
                // JPA.g:254:23: 'NOT'
                {
                match(input,61,FOLLOW_61_in_synpred86_JPA1836); if (state.failed) return ;

                }
                break;

        }

        match(input,76,FOLLOW_76_in_synpred86_JPA1840); if (state.failed) return ;
        pushFollow(FOLLOW_string_expression_in_synpred86_JPA1842);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,AND,FOLLOW_AND_in_synpred86_JPA1844); if (state.failed) return ;
        pushFollow(FOLLOW_string_expression_in_synpred86_JPA1846);
        string_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred86_JPA

    // $ANTLR start synpred103_JPA
    public final void synpred103_JPA_fragment() throws RecognitionException {
        // JPA.g:287:4: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) )
        // JPA.g:287:4: string_expression comparison_operator ( string_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_string_expression_in_synpred103_JPA2058);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_comparison_operator_in_synpred103_JPA2060);
        comparison_operator();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:287:42: ( string_expression | all_or_any_expression )
        int alt110=2;
        int LA110_0 = input.LA(1);

        if ( ((LA110_0>=AVG && LA110_0<=COUNT)||LA110_0==STRINGLITERAL||(LA110_0>=WORD && LA110_0<=NAMED_PARAMETER)||LA110_0==49||(LA110_0>=105 && LA110_0<=109)||LA110_0==114) ) {
            alt110=1;
        }
        else if ( ((LA110_0>=85 && LA110_0<=87)) ) {
            alt110=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 110, 0, input);

            throw nvae;
        }
        switch (alt110) {
            case 1 :
                // JPA.g:287:43: string_expression
                {
                pushFollow(FOLLOW_string_expression_in_synpred103_JPA2063);
                string_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:287:63: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred103_JPA2067);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred103_JPA

    // $ANTLR start synpred106_JPA
    public final void synpred106_JPA_fragment() throws RecognitionException {
        // JPA.g:288:4: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
        // JPA.g:288:4: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_boolean_expression_in_synpred106_JPA2073);
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

        // JPA.g:288:36: ( boolean_expression | all_or_any_expression )
        int alt111=2;
        int LA111_0 = input.LA(1);

        if ( ((LA111_0>=WORD && LA111_0<=NAMED_PARAMETER)||LA111_0==49||(LA111_0>=114 && LA111_0<=116)) ) {
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
                // JPA.g:288:37: boolean_expression
                {
                pushFollow(FOLLOW_boolean_expression_in_synpred106_JPA2084);
                boolean_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:288:58: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred106_JPA2088);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred106_JPA

    // $ANTLR start synpred109_JPA
    public final void synpred109_JPA_fragment() throws RecognitionException {
        // JPA.g:289:4: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
        // JPA.g:289:4: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_enum_expression_in_synpred109_JPA2094);
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

        // JPA.g:289:31: ( enum_expression | all_or_any_expression )
        int alt112=2;
        int LA112_0 = input.LA(1);

        if ( ((LA112_0>=WORD && LA112_0<=NAMED_PARAMETER)||LA112_0==49||LA112_0==114) ) {
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
                // JPA.g:289:32: enum_expression
                {
                pushFollow(FOLLOW_enum_expression_in_synpred109_JPA2103);
                enum_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:289:50: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred109_JPA2107);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred109_JPA

    // $ANTLR start synpred111_JPA
    public final void synpred111_JPA_fragment() throws RecognitionException {
        // JPA.g:290:4: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
        // JPA.g:290:4: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_datetime_expression_in_synpred111_JPA2113);
        datetime_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_comparison_operator_in_synpred111_JPA2115);
        comparison_operator();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:290:44: ( datetime_expression | all_or_any_expression )
        int alt113=2;
        int LA113_0 = input.LA(1);

        if ( ((LA113_0>=AVG && LA113_0<=COUNT)||(LA113_0>=WORD && LA113_0<=NAMED_PARAMETER)||LA113_0==49||(LA113_0>=102 && LA113_0<=104)||LA113_0==114) ) {
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
                // JPA.g:290:45: datetime_expression
                {
                pushFollow(FOLLOW_datetime_expression_in_synpred111_JPA2118);
                datetime_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:290:67: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred111_JPA2122);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred111_JPA

    // $ANTLR start synpred114_JPA
    public final void synpred114_JPA_fragment() throws RecognitionException {
        // JPA.g:291:4: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
        // JPA.g:291:4: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_entity_expression_in_synpred114_JPA2128);
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

        // JPA.g:291:35: ( entity_expression | all_or_any_expression )
        int alt114=2;
        int LA114_0 = input.LA(1);

        if ( ((LA114_0>=WORD && LA114_0<=NAMED_PARAMETER)||LA114_0==114) ) {
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
                // JPA.g:291:36: entity_expression
                {
                pushFollow(FOLLOW_entity_expression_in_synpred114_JPA2139);
                entity_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:291:56: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred114_JPA2143);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred114_JPA

    // Delegated rules

    public final boolean synpred111_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred111_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred106_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred106_JPA_fragment(); // can never throw exception
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
    public final boolean synpred103_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred103_JPA_fragment(); // can never throw exception
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
    public final boolean synpred109_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred109_JPA_fragment(); // can never throw exception
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
    public final boolean synpred114_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred114_JPA_fragment(); // can never throw exception
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
    protected DFA77 dfa77 = new DFA77(this);
    static final String DFA30_eotS =
        "\11\uffff";
    static final String DFA30_eofS =
        "\4\uffff\3\7\1\uffff\1\7";
    static final String DFA30_minS =
        "\1\71\1\30\1\65\1\uffff\3\25\1\uffff\1\25";
    static final String DFA30_maxS =
        "\1\71\1\164\1\131\1\uffff\3\137\1\uffff\1\137";
    static final String DFA30_acceptS =
        "\3\uffff\1\1\3\uffff\1\2\1\uffff";
    static final String DFA30_specialS =
        "\11\uffff}>";
    static final String[] DFA30_transitionS = {
            "\1\1",
            "\5\3\2\uffff\1\3\4\uffff\1\3\1\uffff\1\3\1\uffff\1\2\1\3\7"+
            "\uffff\1\3\13\uffff\2\3\1\uffff\2\3\6\uffff\4\3\10\uffff\1\3"+
            "\13\uffff\16\3\3\uffff\4\3",
            "\1\4\7\uffff\1\3\24\uffff\1\3\5\uffff\2\3",
            "",
            "\1\7\12\uffff\1\7\7\uffff\1\6\15\uffff\1\3\3\uffff\1\5\1\uffff"+
            "\1\7\1\3\2\uffff\2\3\12\uffff\2\3\1\uffff\1\3\2\uffff\1\3\5"+
            "\uffff\10\3",
            "\1\7\12\uffff\1\7\24\uffff\1\10\1\3\3\uffff\3\7\1\3\2\uffff"+
            "\2\3\12\uffff\2\3\1\uffff\1\3\2\uffff\1\3\5\uffff\10\3",
            "\1\7\12\uffff\1\7\24\uffff\1\10\1\3\3\uffff\1\7\1\uffff\1"+
            "\7\1\3\2\uffff\2\3\12\uffff\2\3\1\uffff\1\3\2\uffff\1\3\5\uffff"+
            "\10\3",
            "",
            "\1\7\12\uffff\1\7\7\uffff\1\6\15\uffff\1\3\3\uffff\1\5\1\uffff"+
            "\1\7\1\3\2\uffff\2\3\12\uffff\2\3\1\uffff\1\3\2\uffff\1\3\5"+
            "\uffff\10\3"
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
            return "159:1: where_clause : (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) );";
        }
    }
    static final String DFA35_eotS =
        "\7\uffff";
    static final String DFA35_eofS =
        "\2\uffff\2\4\2\uffff\1\4";
    static final String DFA35_minS =
        "\1\50\1\65\2\26\2\uffff\1\26";
    static final String DFA35_maxS =
        "\1\50\1\65\1\72\1\65\2\uffff\1\72";
    static final String DFA35_acceptS =
        "\4\uffff\1\1\1\2\1\uffff";
    static final String DFA35_specialS =
        "\7\uffff}>";
    static final String[] DFA35_transitionS = {
            "\1\1",
            "\1\2",
            "\1\4\1\5\10\uffff\1\4\7\uffff\1\3\6\uffff\1\4\12\uffff\1\3",
            "\1\4\1\5\10\uffff\1\4\16\uffff\1\4\5\uffff\1\6",
            "",
            "",
            "\1\4\1\5\10\uffff\1\4\7\uffff\1\3\6\uffff\1\4\12\uffff\1\3"
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
            return "178:1: orderby_item : ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) );";
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
            "\5\1\2\uffff\1\23\4\uffff\1\1\1\uffff\1\1\1\uffff\2\1\7\uffff"+
            "\1\1\13\uffff\2\1\1\uffff\2\1\6\uffff\4\1\10\uffff\1\1\13\uffff"+
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
            return "215:1: conditional_factor : ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' );";
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
            "\4\13\1\12\2\uffff\1\22\4\uffff\1\21\1\uffff\1\2\1\uffff\1"+
            "\1\1\4\7\uffff\1\14\13\uffff\1\31\1\33\1\uffff\2\17\6\uffff"+
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
            return "219:1: simple_cond_expression : ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );";
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
            "\4\17\1\16\2\uffff\1\1\4\uffff\1\1\1\uffff\1\21\1\uffff\1\2"+
            "\1\7\7\uffff\1\20\16\uffff\2\1\36\uffff\6\1\3\27\5\21\3\uffff"+
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
            return "252:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );";
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
    static final String DFA77_eotS =
        "\33\uffff";
    static final String DFA77_eofS =
        "\33\uffff";
    static final String DFA77_minS =
        "\1\30\1\0\1\uffff\2\0\5\uffff\3\0\16\uffff";
    static final String DFA77_maxS =
        "\1\164\1\0\1\uffff\2\0\5\uffff\3\0\16\uffff";
    static final String DFA77_acceptS =
        "\2\uffff\1\1\12\uffff\1\2\1\4\1\6\11\uffff\1\3\1\5";
    static final String DFA77_specialS =
        "\1\uffff\1\0\1\uffff\1\1\1\2\5\uffff\1\3\1\4\1\5\16\uffff}>";
    static final String[] DFA77_transitionS = {
            "\4\13\1\12\2\uffff\1\17\4\uffff\1\17\1\uffff\1\2\1\uffff\1"+
            "\1\1\4\7\uffff\1\14\16\uffff\2\17\36\uffff\6\17\3\16\5\2\3\uffff"+
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

    static final short[] DFA77_eot = DFA.unpackEncodedString(DFA77_eotS);
    static final short[] DFA77_eof = DFA.unpackEncodedString(DFA77_eofS);
    static final char[] DFA77_min = DFA.unpackEncodedStringToUnsignedChars(DFA77_minS);
    static final char[] DFA77_max = DFA.unpackEncodedStringToUnsignedChars(DFA77_maxS);
    static final short[] DFA77_accept = DFA.unpackEncodedString(DFA77_acceptS);
    static final short[] DFA77_special = DFA.unpackEncodedString(DFA77_specialS);
    static final short[][] DFA77_transition;

    static {
        int numStates = DFA77_transitionS.length;
        DFA77_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA77_transition[i] = DFA.unpackEncodedString(DFA77_transitionS[i]);
        }
    }

    class DFA77 extends DFA {

        public DFA77(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 77;
            this.eot = DFA77_eot;
            this.eof = DFA77_eof;
            this.min = DFA77_min;
            this.max = DFA77_max;
            this.accept = DFA77_accept;
            this.special = DFA77_special;
            this.transition = DFA77_transition;
        }
        public String getDescription() {
            return "286:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 :
                        int LA77_1 = input.LA(1);


                        int index77_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_JPA()) ) {s = 2;}

                        else if ( (synpred106_JPA()) ) {s = 13;}

                        else if ( (synpred109_JPA()) ) {s = 25;}

                        else if ( (synpred111_JPA()) ) {s = 14;}

                        else if ( (synpred114_JPA()) ) {s = 26;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index77_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 :
                        int LA77_3 = input.LA(1);


                        int index77_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_JPA()) ) {s = 2;}

                        else if ( (synpred106_JPA()) ) {s = 13;}

                        else if ( (synpred109_JPA()) ) {s = 25;}

                        else if ( (synpred111_JPA()) ) {s = 14;}

                        else if ( (synpred114_JPA()) ) {s = 26;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index77_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 :
                        int LA77_4 = input.LA(1);


                        int index77_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_JPA()) ) {s = 2;}

                        else if ( (synpred106_JPA()) ) {s = 13;}

                        else if ( (synpred109_JPA()) ) {s = 25;}

                        else if ( (synpred111_JPA()) ) {s = 14;}

                        else if ( (synpred114_JPA()) ) {s = 26;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index77_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 :
                        int LA77_10 = input.LA(1);


                        int index77_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_JPA()) ) {s = 2;}

                        else if ( (synpred111_JPA()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index77_10);
                        if ( s>=0 ) return s;
                        break;
                    case 4 :
                        int LA77_11 = input.LA(1);


                        int index77_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_JPA()) ) {s = 2;}

                        else if ( (synpred111_JPA()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index77_11);
                        if ( s>=0 ) return s;
                        break;
                    case 5 :
                        int LA77_12 = input.LA(1);


                        int index77_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_JPA()) ) {s = 2;}

                        else if ( (synpred106_JPA()) ) {s = 13;}

                        else if ( (synpred109_JPA()) ) {s = 25;}

                        else if ( (synpred111_JPA()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}


                        input.seek(index77_12);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 77, _s, input);
            error(nvae);
            throw nvae;
        }
    }


    public static final BitSet FOLLOW_select_statement_in_ql_statement350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_select_statement362 = new BitSet(new long[]{0x018001021F000000L});
    public static final BitSet FOLLOW_select_clause_in_select_statement364 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_from_clause_in_select_statement366 = new BitSet(new long[]{0x1600000000200002L});
    public static final BitSet FOLLOW_where_clause_in_select_statement369 = new BitSet(new long[]{0x1400000000200002L});
    public static final BitSet FOLLOW_groupby_clause_in_select_statement374 = new BitSet(new long[]{0x1000000000200002L});
    public static final BitSet FOLLOW_having_clause_in_select_statement379 = new BitSet(new long[]{0x1000000000000002L});
    public static final BitSet FOLLOW_orderby_clause_in_select_statement383 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_from_clause438 = new BitSet(new long[]{0x0002010000000000L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause440 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_47_in_from_clause443 = new BitSet(new long[]{0x0042010000000000L});
    public static final BitSet FOLLOW_identification_variable_or_collection_declaration_in_from_clause445 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_or_collection_declaration489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_or_collection_declaration493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration502 = new BitSet(new long[]{0x0014000400000002L});
    public static final BitSet FOLLOW_join_in_identification_variable_declaration506 = new BitSet(new long[]{0x0014000400000002L});
    public static final BitSet FOLLOW_fetch_join_in_identification_variable_declaration510 = new BitSet(new long[]{0x0014000400000002L});
    public static final BitSet FOLLOW_range_variable_declaration_source_in_range_variable_declaration522 = new BitSet(new long[]{0x0001010000000000L});
    public static final BitSet FOLLOW_48_in_range_variable_declaration525 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_abstract_schema_name_in_range_variable_declaration_source556 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_range_variable_declaration_source563 = new BitSet(new long[]{0x018001021F000000L});
    public static final BitSet FOLLOW_select_clause_in_range_variable_declaration_source565 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_from_clause_in_range_variable_declaration_source567 = new BitSet(new long[]{0x1600000100200000L});
    public static final BitSet FOLLOW_where_clause_in_range_variable_declaration_source570 = new BitSet(new long[]{0x1400000100200000L});
    public static final BitSet FOLLOW_groupby_clause_in_range_variable_declaration_source575 = new BitSet(new long[]{0x1000000100200000L});
    public static final BitSet FOLLOW_having_clause_in_range_variable_declaration_source580 = new BitSet(new long[]{0x1000000100000000L});
    public static final BitSet FOLLOW_orderby_clause_in_range_variable_declaration_source584 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_range_variable_declaration_source590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_spec_in_join644 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_join_association_path_expression_in_join646 = new BitSet(new long[]{0x0001010000000000L});
    public static final BitSet FOLLOW_48_in_join649 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_identification_variable_in_join653 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_spec_in_fetch_join682 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_FETCH_in_fetch_join684 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_join_spec696 = new BitSet(new long[]{0x0008000400000000L});
    public static final BitSet FOLLOW_51_in_join_spec700 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_52_in_join_spec706 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_JOIN_in_join_spec711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression720 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_join_association_path_expression722 = new BitSet(new long[]{0x0400010000000002L});
    public static final BitSet FOLLOW_field_in_join_association_path_expression725 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_join_association_path_expression726 = new BitSet(new long[]{0x0400010000000002L});
    public static final BitSet FOLLOW_field_in_join_association_path_expression730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_collection_member_declaration758 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration759 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_path_expression_in_collection_member_declaration761 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration763 = new BitSet(new long[]{0x0001010000000000L});
    public static final BitSet FOLLOW_48_in_collection_member_declaration766 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_path_expression796 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_path_expression798 = new BitSet(new long[]{0x0400010000000002L});
    public static final BitSet FOLLOW_field_in_path_expression801 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_path_expression802 = new BitSet(new long[]{0x0400010000000002L});
    public static final BitSet FOLLOW_field_in_path_expression807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_select_clause837 = new BitSet(new long[]{0x018001021F000000L});
    public static final BitSet FOLLOW_select_expression_in_select_clause841 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_47_in_select_clause844 = new BitSet(new long[]{0x018001021F000000L});
    public static final BitSet FOLLOW_select_expression_in_select_clause846 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_path_expression_in_select_expression882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_select_expression887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_select_expression892 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_select_expression907 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_select_expression909 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_identification_variable_in_select_expression910 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_select_expression911 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_expression_in_select_expression916 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_56_in_constructor_expression925 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_constructor_name_in_constructor_expression927 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_constructor_expression929 = new BitSet(new long[]{0x000001001F000000L});
    public static final BitSet FOLLOW_constructor_item_in_constructor_expression931 = new BitSet(new long[]{0x0000800100000000L});
    public static final BitSet FOLLOW_47_in_constructor_expression934 = new BitSet(new long[]{0x000001001F000000L});
    public static final BitSet FOLLOW_constructor_item_in_constructor_expression936 = new BitSet(new long[]{0x0000800100000000L});
    public static final BitSet FOLLOW_RPAREN_in_constructor_expression940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_constructor_item949 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_constructor_item953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression962 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_aggregate_expression964 = new BitSet(new long[]{0x0000010200000000L});
    public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression967 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_path_expression_in_aggregate_expression971 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_aggregate_expression972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COUNT_in_aggregate_expression1000 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1002 = new BitSet(new long[]{0x0000010200000000L});
    public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1005 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_identification_variable_in_aggregate_expression1009 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_aggregate_expression_function_name0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_57_in_where_clause1073 = new BitSet(new long[]{0x600203509F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_conditional_expression_in_where_clause1075 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_57_in_where_clause1091 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_path_expression_in_where_clause1093 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_groupby_clause1114 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_59_in_groupby_clause1116 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_groupby_item_in_groupby_clause1118 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_47_in_groupby_clause1121 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_groupby_item_in_groupby_clause1123 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_path_expression_in_groupby_item1154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_groupby_item1158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HAVING_in_having_clause1167 = new BitSet(new long[]{0x600203509F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_conditional_expression_in_having_clause1169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_orderby_clause1178 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_59_in_orderby_clause1180 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_orderby_item_in_orderby_clause1182 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_47_in_orderby_clause1185 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_orderby_item_in_orderby_clause1187 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_path_expression_in_orderby_item1216 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_ASC_in_orderby_item1219 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_orderby_item1245 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_DESC_in_orderby_item1247 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_subquery1273 = new BitSet(new long[]{0x000001021F000000L});
    public static final BitSet FOLLOW_simple_select_clause_in_subquery1275 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_subquery_from_clause_in_subquery1277 = new BitSet(new long[]{0x0600000100200000L});
    public static final BitSet FOLLOW_where_clause_in_subquery1280 = new BitSet(new long[]{0x0400000100200000L});
    public static final BitSet FOLLOW_groupby_clause_in_subquery1285 = new BitSet(new long[]{0x0000000100200000L});
    public static final BitSet FOLLOW_having_clause_in_subquery1290 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_subquery1296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_subquery_from_clause1339 = new BitSet(new long[]{0x0042010000000000L});
    public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1341 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_47_in_subquery_from_clause1344 = new BitSet(new long[]{0x0042010000000000L});
    public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1346 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_association_path_expression_in_subselect_identification_variable_declaration1383 = new BitSet(new long[]{0x0001010000000000L});
    public static final BitSet FOLLOW_48_in_subselect_identification_variable_declaration1386 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration1390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_declaration_in_subselect_identification_variable_declaration1395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_association_path_expression1404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause1414 = new BitSet(new long[]{0x000001021F000000L});
    public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause1418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_simple_select_expression1449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression1454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_simple_select_expression1459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditional_term_in_conditional_expression1469 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_OR_in_conditional_expression1473 = new BitSet(new long[]{0x600203509F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_conditional_term_in_conditional_expression1475 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_conditional_factor_in_conditional_term1487 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_AND_in_conditional_term1491 = new BitSet(new long[]{0x600203509F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_conditional_factor_in_conditional_term1493 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_61_in_conditional_factor1506 = new BitSet(new long[]{0x600203509F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_simple_cond_expression_in_conditional_factor1510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_conditional_factor1532 = new BitSet(new long[]{0x600203509F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_conditional_expression_in_conditional_factor1533 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_conditional_factor1534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression1543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_expression_in_simple_cond_expression1548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_expression_in_simple_cond_expression1553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_expression_in_simple_cond_expression1558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression1563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression1568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression1573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression1578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression1583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression1595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression1603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression1611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression1619 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression1627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_date_between_macro_expression1639 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression1641 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression1643 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_date_between_macro_expression1645 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_date_between_macro_expression1647 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression1650 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_date_between_macro_expression1658 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_date_between_macro_expression1662 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_date_between_macro_expression1664 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression1667 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_date_between_macro_expression1675 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_date_between_macro_expression1679 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000FCL});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression1681 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression1704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_date_before_macro_expression1716 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression1718 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression1720 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_date_before_macro_expression1722 = new BitSet(new long[]{0x0000030000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression1725 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression1729 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression1732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_date_after_macro_expression1744 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression1746 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression1748 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_date_after_macro_expression1750 = new BitSet(new long[]{0x0000030000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression1753 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression1757 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression1760 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_date_equals_macro_expression1772 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression1774 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression1776 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_date_equals_macro_expression1778 = new BitSet(new long[]{0x0000030000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression1781 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression1785 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression1788 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_75_in_date_today_macro_expression1800 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression1802 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression1804 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression1806 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1815 = new BitSet(new long[]{0x2000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_61_in_between_expression1818 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_between_expression1822 = new BitSet(new long[]{0x000203509F000000L,0x001E3FFF00000003L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1824 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression1826 = new BitSet(new long[]{0x000203509F000000L,0x001E3FFF00000003L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1828 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1833 = new BitSet(new long[]{0x2000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_61_in_between_expression1836 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_between_expression1840 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1842 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression1844 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1846 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1851 = new BitSet(new long[]{0x2000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_61_in_between_expression1854 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_between_expression1858 = new BitSet(new long[]{0x000203401F000000L,0x00043FC000000000L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1860 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression1862 = new BitSet(new long[]{0x000203401F000000L,0x00043FC000000000L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1864 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_in_expression1873 = new BitSet(new long[]{0x2040000000000000L});
    public static final BitSet FOLLOW_61_in_in_expression1876 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_in_expression1880 = new BitSet(new long[]{0x000203409F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_in_expression_right_part_in_in_expression1882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_in_expression_right_part1891 = new BitSet(new long[]{0x0000030000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_in_item_in_in_expression_right_part1893 = new BitSet(new long[]{0x0000800100000000L});
    public static final BitSet FOLLOW_47_in_in_expression_right_part1896 = new BitSet(new long[]{0x0000030000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_in_item_in_in_expression_right_part1898 = new BitSet(new long[]{0x0000800100000000L});
    public static final BitSet FOLLOW_RPAREN_in_in_expression_right_part1902 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_in_expression_right_part1907 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_in_item1916 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_in_item1921 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_like_expression1930 = new BitSet(new long[]{0x2000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_61_in_like_expression1933 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_like_expression1937 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_pattern_value_in_like_expression1939 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_78_in_like_expression1942 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_ESCAPE_CHARACTER_in_like_expression1944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_null_comparison_expression1956 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression1960 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_79_in_null_comparison_expression1963 = new BitSet(new long[]{0x2000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_61_in_null_comparison_expression1966 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_80_in_null_comparison_expression1970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression1979 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_79_in_empty_collection_comparison_expression1981 = new BitSet(new long[]{0x2000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_61_in_empty_collection_comparison_expression1984 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_81_in_empty_collection_comparison_expression1988 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_collection_member_expression1997 = new BitSet(new long[]{0x2000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_61_in_collection_member_expression2000 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_collection_member_expression2004 = new BitSet(new long[]{0x0000010000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_collection_member_expression2007 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_path_expression_in_collection_member_expression2011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_exists_expression2021 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_84_in_exists_expression2025 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_subquery_in_exists_expression2027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_all_or_any_expression2036 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_subquery_in_all_or_any_expression2049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_comparison_expression2058 = new BitSet(new long[]{0x0000000000000000L,0x000000003F000000L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2060 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_string_expression_in_comparison_expression2063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2073 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_comparison_expression2075 = new BitSet(new long[]{0x000203401F000000L,0x001C3E0000E00000L});
    public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_expression_in_comparison_expression2094 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_comparison_expression2096 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_enum_expression_in_comparison_expression2103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2113 = new BitSet(new long[]{0x0000000000000000L,0x000000003F000000L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2115 = new BitSet(new long[]{0x000203401F000000L,0x00043FC000E00000L});
    public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_comparison_expression2128 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_comparison_expression2130 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_entity_expression_in_comparison_expression2139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression2149 = new BitSet(new long[]{0x0000000000000000L,0x000000003F000000L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2151 = new BitSet(new long[]{0x000203509F000000L,0x001E3FFF00E00003L});
    public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression2154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_comparison_operator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_arithmetic_expression2202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_arithmetic_expression2207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_term_in_simple_arithmetic_expression2217 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_set_in_simple_arithmetic_expression2221 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_arithmetic_term_in_simple_arithmetic_expression2231 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term2243 = new BitSet(new long[]{0x0000000000000002L,0x00000000C0000000L});
    public static final BitSet FOLLOW_set_in_arithmetic_term2247 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term2257 = new BitSet(new long[]{0x0000000000000002L,0x00000000C0000000L});
    public static final BitSet FOLLOW_set_in_arithmetic_factor2268 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor2279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_arithmetic_primary2288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary2293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary2298 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_arithmetic_primary2299 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary2300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary2305 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary2310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary2315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_primary_in_string_expression2324 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_string_expression2328 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_string_primary2337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_string_primary2342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_string_primary2347 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_strings_in_string_primary2352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_string_primary2357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_primary_in_datetime_expression2366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_datetime_expression2371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_datetime_primary2380 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_datetime_primary2385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_primary2390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_datetime_primary2395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_primary_in_boolean_expression2404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_boolean_expression2409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_boolean_primary2418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_literal_in_boolean_primary2423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_boolean_primary2428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_primary_in_enum_expression2437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_enum_expression2442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_enum_primary2451 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_literal_in_enum_primary2456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_enum_primary2461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_entity_expression2470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression2475 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression2484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression2489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_96_in_functions_returning_numerics2498 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2500 = new BitSet(new long[]{0x000003401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics2501 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2502 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_functions_returning_numerics2507 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2509 = new BitSet(new long[]{0x000003401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics2510 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_functions_returning_numerics2511 = new BitSet(new long[]{0x000003401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics2513 = new BitSet(new long[]{0x0000800100000000L});
    public static final BitSet FOLLOW_47_in_functions_returning_numerics2515 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2517 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_functions_returning_numerics2525 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2527 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2528 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_functions_returning_numerics2534 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2536 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2537 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_functions_returning_numerics2543 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2545 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2546 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_functions_returning_numerics2547 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2549 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_101_in_functions_returning_numerics2555 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2557 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics2558 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_functions_returning_datetime0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_functions_returning_strings2587 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2589 = new BitSet(new long[]{0x000003401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2590 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_functions_returning_strings2591 = new BitSet(new long[]{0x000003401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2593 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_106_in_functions_returning_strings2599 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2601 = new BitSet(new long[]{0x000003401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2602 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_functions_returning_strings2603 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2604 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_functions_returning_strings2605 = new BitSet(new long[]{0x000003109F000000L,0x0006003F00000003L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2607 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_107_in_functions_returning_strings2613 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2615 = new BitSet(new long[]{0x000043C01F000000L,0x0005FE0000000000L});
    public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings2618 = new BitSet(new long[]{0x0000408000000000L});
    public static final BitSet FOLLOW_TRIM_CHARACTER_in_functions_returning_strings2623 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_functions_returning_strings2627 = new BitSet(new long[]{0x000003401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2631 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_functions_returning_strings2637 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2639 = new BitSet(new long[]{0x000003401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2640 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_109_in_functions_returning_strings2646 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2648 = new BitSet(new long[]{0x000003401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2649 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_trim_specification0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_abstract_schema_name2681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_pattern_value2691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_113_in_numeric_literal2702 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal2706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_114_in_input_parameter2716 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_input_parameter2718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter2738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_literal2760 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_constructor_name2769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_enum_literal2810 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_boolean_literal0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_field0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_identification_variable2851 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_parameter_name2862 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_53_in_parameter_name2865 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_WORD_in_parameter_name2868 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_field_in_synpred20_JPA730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_field_in_synpred23_JPA807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_synpred57_JPA1506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_synpred58_JPA1506 = new BitSet(new long[]{0x600203509F000000L,0x001E3FFF00100F03L});
    public static final BitSet FOLLOW_simple_cond_expression_in_synpred58_JPA1510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_expression_in_synpred59_JPA1543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_expression_in_synpred60_JPA1548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_expression_in_synpred61_JPA1553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_expression_in_synpred62_JPA1558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_comparison_expression_in_synpred63_JPA1563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred64_JPA1568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_expression_in_synpred65_JPA1573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred84_JPA1815 = new BitSet(new long[]{0x2000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_61_in_synpred84_JPA1818 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_synpred84_JPA1822 = new BitSet(new long[]{0x000203509F000000L,0x001E3FFF00000003L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred84_JPA1824 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_synpred84_JPA1826 = new BitSet(new long[]{0x000203509F000000L,0x001E3FFF00000003L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred84_JPA1828 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_synpred86_JPA1833 = new BitSet(new long[]{0x2000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_61_in_synpred86_JPA1836 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_synpred86_JPA1840 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_expression_in_synpred86_JPA1842 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_synpred86_JPA1844 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000000000L});
    public static final BitSet FOLLOW_string_expression_in_synpred86_JPA1846 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_synpred103_JPA2058 = new BitSet(new long[]{0x0000000000000000L,0x000000003F000000L});
    public static final BitSet FOLLOW_comparison_operator_in_synpred103_JPA2060 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_string_expression_in_synpred103_JPA2063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred103_JPA2067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_expression_in_synpred106_JPA2073 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_synpred106_JPA2075 = new BitSet(new long[]{0x000203401F000000L,0x001C3E0000E00000L});
    public static final BitSet FOLLOW_boolean_expression_in_synpred106_JPA2084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred106_JPA2088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_expression_in_synpred109_JPA2094 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_synpred109_JPA2096 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_enum_expression_in_synpred109_JPA2103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred109_JPA2107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_synpred111_JPA2113 = new BitSet(new long[]{0x0000000000000000L,0x000000003F000000L});
    public static final BitSet FOLLOW_comparison_operator_in_synpred111_JPA2115 = new BitSet(new long[]{0x000203401F000000L,0x00043FC000E00000L});
    public static final BitSet FOLLOW_datetime_expression_in_synpred111_JPA2118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred111_JPA2122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_synpred114_JPA2128 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_set_in_synpred114_JPA2130 = new BitSet(new long[]{0x000203401F000000L,0x00043E0000E00000L});
    public static final BitSet FOLLOW_entity_expression_in_synpred114_JPA2139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred114_JPA2143 = new BitSet(new long[]{0x0000000000000002L});

}