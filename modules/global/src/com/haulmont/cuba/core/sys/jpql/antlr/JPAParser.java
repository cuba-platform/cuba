// $ANTLR 3.2 Sep 23, 2009 12:02:23 JPA.g 2012-08-02 17:19:15

package com.haulmont.cuba.core.sys.jpql.antlr;

import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.JoinVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import com.haulmont.cuba.core.sys.jpql.tree.QueryNode;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.RewriteRuleTokenStream;
import org.antlr.runtime.tree.TreeAdaptor;

public class JPAParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "T_SELECTED_ITEMS", "T_SOURCES", "T_SOURCE", "T_COLLECTION_MEMBER", "T_SELECTED_ITEM", "T_AGGREGATE_EXPR", "T_ORDER_BY", "T_GROUP_BY", "T_ORDER_BY_FIELD", "T_SIMPLE_CONDITION", "T_PARAMETER", "T_SELECTED_FIELD", "T_SELECTED_ENTITY", "T_ID_VAR", "T_JOIN_VAR", "T_QUERY", "T_CONDITION", "HAVING", "ASC", "DESC", "AVG", "MAX", "MIN", "SUM", "COUNT", "OR", "AND", "LPAREN", "RPAREN", "DISTINCT", "LEFT", "OUTER", "INNER", "JOIN", "FETCH", "ORDER", "GROUP", "BY", "SELECT", "ESCAPE_CHARACTER", "STRINGLITERAL", "INT_NUMERAL", "TRIM_CHARACTER", "WORD", "NAMED_PARAMETER", "RUSSIAN_SYMBOLS", "WS", "COMMENT", "LINE_COMMENT", "'FROM'", "','", "'AS'", "'(SELECT'", "'.'", "'IN'", "'OBJECT'", "'NEW'", "'WHERE'", "'NOT'", "'BETWEEN'", "'LIKE'", "'ESCAPE'", "'IS'", "'NULL'", "'EMPTY'", "'MEMBER'", "'OF'", "'EXISTS'", "'ALL'", "'ANY'", "'SOME'", "'='", "'<>'", "'>'", "'>='", "'<'", "'<='", "'+'", "'-'", "'*'", "'/'", "'@BETWEEN'", "'NOW'", "'YEAR'", "'MONTH'", "'DAY'", "'HOUR'", "'MINUTE'", "'SECOND'", "'@DATEBEFORE'", "'@DATEAFTER'", "'@DATEEQUALS'", "'@TODAY'", "'LENGTH'", "'LOCATE'", "'ABS'", "'SQRT'", "'MOD'", "'SIZE'", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'CONCAT'", "'SUBSTRING'", "'TRIM'", "'LOWER'", "'UPPER'", "'LEADING'", "'TRAILING'", "'BOTH'", "'0x'", "'?'", "'${'", "'}'", "'true'", "'false'"
    };
    public static final int T_JOIN_VAR=18;
    public static final int T_AGGREGATE_EXPR=9;
    public static final int COUNT=28;
    public static final int T_ORDER_BY=10;
    public static final int EOF=-1;
    public static final int WORD=47;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int RPAREN=32;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__90=90;
    public static final int TRIM_CHARACTER=46;
    public static final int T_SELECTED_ITEM=8;
    public static final int COMMENT=51;
    public static final int SELECT=42;
    public static final int T__99=99;
    public static final int T__98=98;
    public static final int T__97=97;
    public static final int T__96=96;
    public static final int T_QUERY=19;
    public static final int T__95=95;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int ASC=22;
    public static final int LINE_COMMENT=52;
    public static final int T__85=85;
    public static final int T__84=84;
    public static final int T__87=87;
    public static final int T__86=86;
    public static final int T__89=89;
    public static final int T__88=88;
    public static final int GROUP=40;
    public static final int WS=50;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int FETCH=38;
    public static final int T__70=70;
    public static final int T_SELECTED_FIELD=15;
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
    public static final int T_SOURCE=6;
    public static final int T__119=119;
    public static final int T__116=116;
    public static final int T_ID_VAR=17;
    public static final int T__117=117;
    public static final int T_SIMPLE_CONDITION=13;
    public static final int T__114=114;
    public static final int T__115=115;
    public static final int MAX=25;
    public static final int AND=30;
    public static final int SUM=27;
    public static final int T__61=61;
    public static final int T__60=60;
    public static final int RUSSIAN_SYMBOLS=49;
    public static final int LPAREN=31;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int LEFT=34;
    public static final int T_ORDER_BY_FIELD=12;
    public static final int AVG=24;
    public static final int T__59=59;
    public static final int T__103=103;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int T__111=111;
    public static final int T__110=110;
    public static final int T__113=113;
    public static final int T__112=112;
    public static final int T_GROUP_BY=11;
    public static final int OUTER=35;
    public static final int BY=41;
    public static final int T_CONDITION=20;
    public static final int T_SELECTED_ENTITY=16;
    public static final int T__102=102;
    public static final int HAVING=21;
    public static final int T__101=101;
    public static final int MIN=26;
    public static final int T__100=100;
    public static final int T_PARAMETER=14;
    public static final int JOIN=37;
    public static final int NAMED_PARAMETER=48;
    public static final int ESCAPE_CHARACTER=43;
    public static final int INT_NUMERAL=45;
    public static final int STRINGLITERAL=44;
    public static final int T_COLLECTION_MEMBER=7;
    public static final int DESC=23;
    public static final int T_SOURCES=5;

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
    // JPA.g:75:1: ql_statement : select_statement ;
    public final JPAParser.ql_statement_return ql_statement() throws RecognitionException {
        JPAParser.ql_statement_return retval = new JPAParser.ql_statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.select_statement_return select_statement1 = null;



        try {
            // JPA.g:76:2: ( select_statement )
            // JPA.g:76:4: select_statement
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_select_statement_in_ql_statement297);
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
    // JPA.g:78:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
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
        RewriteRuleTokenStream stream_SELECT=new RewriteRuleTokenStream(adaptor,"token SELECT");
        RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");
        try {
            // JPA.g:79:2: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
            // JPA.g:79:5: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )?
            {
            sl=(Token)match(input,SELECT,FOLLOW_SELECT_in_select_statement309); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SELECT.add(sl);

            pushFollow(FOLLOW_select_clause_in_select_statement311);
            select_clause2=select_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_select_clause.add(select_clause2.getTree());
            pushFollow(FOLLOW_from_clause_in_select_statement313);
            from_clause3=from_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_from_clause.add(from_clause3.getTree());
            // JPA.g:79:43: ( where_clause )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==61) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // JPA.g:79:44: where_clause
                    {
                    pushFollow(FOLLOW_where_clause_in_select_statement316);
                    where_clause4=where_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_where_clause.add(where_clause4.getTree());

                    }
                    break;

            }

            // JPA.g:79:59: ( groupby_clause )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==GROUP) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // JPA.g:79:60: groupby_clause
                    {
                    pushFollow(FOLLOW_groupby_clause_in_select_statement321);
                    groupby_clause5=groupby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause5.getTree());

                    }
                    break;

            }

            // JPA.g:79:77: ( having_clause )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==HAVING) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // JPA.g:79:78: having_clause
                    {
                    pushFollow(FOLLOW_having_clause_in_select_statement326);
                    having_clause6=having_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_having_clause.add(having_clause6.getTree());

                    }
                    break;

            }

            // JPA.g:79:93: ( orderby_clause )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==ORDER) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // JPA.g:79:94: orderby_clause
                    {
                    pushFollow(FOLLOW_orderby_clause_in_select_statement330);
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
            // 80:3: -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
            {
                // JPA.g:80:6: ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, sl), root_1);

                // JPA.g:80:21: ( select_clause )?
                if ( stream_select_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_select_clause.nextTree());

                }
                stream_select_clause.reset();
                adaptor.addChild(root_1, stream_from_clause.nextTree());
                // JPA.g:80:52: ( where_clause )?
                if ( stream_where_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_where_clause.nextTree());

                }
                stream_where_clause.reset();
                // JPA.g:80:70: ( groupby_clause )?
                if ( stream_groupby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                }
                stream_groupby_clause.reset();
                // JPA.g:80:90: ( having_clause )?
                if ( stream_having_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_having_clause.nextTree());

                }
                stream_having_clause.reset();
                // JPA.g:80:109: ( orderby_clause )?
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
    // JPA.g:83:1: from_clause : fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_or_collection_declaration )* ) ;
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
            // JPA.g:84:2: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_or_collection_declaration )* ) )
            // JPA.g:84:4: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_or_collection_declaration )*
            {
            fr=(Token)match(input,53,FOLLOW_53_in_from_clause398); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_53.add(fr);

            pushFollow(FOLLOW_identification_variable_declaration_in_from_clause400);
            identification_variable_declaration8=identification_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration8.getTree());
            // JPA.g:84:51: ( ',' identification_variable_or_collection_declaration )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==54) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // JPA.g:84:53: ',' identification_variable_or_collection_declaration
            	    {
            	    char_literal9=(Token)match(input,54,FOLLOW_54_in_from_clause404); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_54.add(char_literal9);

            	    pushFollow(FOLLOW_identification_variable_or_collection_declaration_in_from_clause406);
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
            // 85:2: -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_or_collection_declaration )* )
            {
                // JPA.g:85:5: ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_or_collection_declaration )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SOURCES, fr), root_1);

                adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
                // JPA.g:85:59: ( identification_variable_or_collection_declaration )*
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
    // JPA.g:87:1: identification_variable_or_collection_declaration : ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) ) ;
    public final JPAParser.identification_variable_or_collection_declaration_return identification_variable_or_collection_declaration() throws RecognitionException {
        JPAParser.identification_variable_or_collection_declaration_return retval = new JPAParser.identification_variable_or_collection_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.identification_variable_declaration_return identification_variable_declaration11 = null;

        JPAParser.collection_member_declaration_return collection_member_declaration12 = null;


        RewriteRuleSubtreeStream stream_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule collection_member_declaration");
        RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
        try {
            // JPA.g:88:5: ( ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) ) )
            // JPA.g:88:7: ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) )
            {
            // JPA.g:88:7: ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) )
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
                    // JPA.g:88:9: identification_variable_declaration
                    {
                    pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_or_collection_declaration445);
                    identification_variable_declaration11=identification_variable_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration11.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:88:47: collection_member_declaration
                    {
                    pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_or_collection_declaration449);
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
                    // 88:77: -> ^( T_SOURCE collection_member_declaration )
                    {
                        // JPA.g:88:80: ^( T_SOURCE collection_member_declaration )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SOURCE, "T_SOURCE"), root_1);

                        adaptor.addChild(root_1, stream_collection_member_declaration.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
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
    // $ANTLR end "identification_variable_or_collection_declaration"

    public static class identification_variable_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "identification_variable_declaration"
    // JPA.g:91:1: identification_variable_declaration : range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) ;
    public final JPAParser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
        JPAParser.identification_variable_declaration_return retval = new JPAParser.identification_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.range_variable_declaration_return range_variable_declaration13 = null;

        JPAParser.joined_clause_return joined_clause14 = null;


        RewriteRuleSubtreeStream stream_joined_clause=new RewriteRuleSubtreeStream(adaptor,"rule joined_clause");
        RewriteRuleSubtreeStream stream_range_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration");
        try {
            // JPA.g:92:2: ( range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) )
            // JPA.g:92:4: range_variable_declaration ( joined_clause )*
            {
            pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration476);
            range_variable_declaration13=range_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_range_variable_declaration.add(range_variable_declaration13.getTree());
            // JPA.g:92:31: ( joined_clause )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==LEFT||(LA7_0>=INNER && LA7_0<=JOIN)) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // JPA.g:92:33: joined_clause
            	    {
            	    pushFollow(FOLLOW_joined_clause_in_identification_variable_declaration480);
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
            // 92:50: -> ^( T_SOURCE range_variable_declaration ( joined_clause )* )
            {
                // JPA.g:92:53: ^( T_SOURCE range_variable_declaration ( joined_clause )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SOURCE, "T_SOURCE"), root_1);

                adaptor.addChild(root_1, stream_range_variable_declaration.nextTree());
                // JPA.g:92:92: ( joined_clause )*
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
    // JPA.g:95:1: joined_clause : ( join | fetch_join ) ;
    public final JPAParser.joined_clause_return joined_clause() throws RecognitionException {
        JPAParser.joined_clause_return retval = new JPAParser.joined_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.join_return join15 = null;

        JPAParser.fetch_join_return fetch_join16 = null;



        try {
            // JPA.g:96:2: ( ( join | fetch_join ) )
            // JPA.g:96:4: ( join | fetch_join )
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:96:4: ( join | fetch_join )
            int alt8=2;
            switch ( input.LA(1) ) {
            case LEFT:
                {
                int LA8_1 = input.LA(2);

                if ( (LA8_1==OUTER) ) {
                    int LA8_4 = input.LA(3);

                    if ( (LA8_4==JOIN) ) {
                        int LA8_3 = input.LA(4);

                        if ( (LA8_3==WORD) ) {
                            alt8=1;
                        }
                        else if ( (LA8_3==FETCH) ) {
                            alt8=2;
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

                    if ( (LA8_3==WORD) ) {
                        alt8=1;
                    }
                    else if ( (LA8_3==FETCH) ) {
                        alt8=2;
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

                    if ( (LA8_3==WORD) ) {
                        alt8=1;
                    }
                    else if ( (LA8_3==FETCH) ) {
                        alt8=2;
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

                if ( (LA8_3==WORD) ) {
                    alt8=1;
                }
                else if ( (LA8_3==FETCH) ) {
                    alt8=2;
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
                    // JPA.g:96:6: join
                    {
                    pushFollow(FOLLOW_join_in_joined_clause515);
                    join15=join();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, join15.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:96:13: fetch_join
                    {
                    pushFollow(FOLLOW_fetch_join_in_joined_clause519);
                    fetch_join16=fetch_join();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, fetch_join16.getTree());

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
    // $ANTLR end "joined_clause"

    public static class range_variable_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "range_variable_declaration"
    // JPA.g:99:1: range_variable_declaration : range_variable_declaration_source ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source ) ;
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
            // JPA.g:100:2: ( range_variable_declaration_source ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source ) )
            // JPA.g:100:4: range_variable_declaration_source ( 'AS' )? identification_variable
            {
            pushFollow(FOLLOW_range_variable_declaration_source_in_range_variable_declaration532);
            range_variable_declaration_source17=range_variable_declaration_source();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_range_variable_declaration_source.add(range_variable_declaration_source17.getTree());
            // JPA.g:100:38: ( 'AS' )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==55) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // JPA.g:100:39: 'AS'
                    {
                    string_literal18=(Token)match(input,55,FOLLOW_55_in_range_variable_declaration535); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_55.add(string_literal18);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_range_variable_declaration539);
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
            // 101:4: -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source )
            {
                // JPA.g:101:7: ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source )
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
    // JPA.g:104:1: range_variable_declaration_source : ( abstract_schema_name | lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')' -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) );
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
            // JPA.g:105:2: ( abstract_schema_name | lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')' -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
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
                    // JPA.g:105:4: abstract_schema_name
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_abstract_schema_name_in_range_variable_declaration_source566);
                    abstract_schema_name20=abstract_schema_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, abstract_schema_name20.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:106:4: lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')'
                    {
                    lp=(Token)match(input,56,FOLLOW_56_in_range_variable_declaration_source573); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_56.add(lp);

                    pushFollow(FOLLOW_select_clause_in_range_variable_declaration_source575);
                    select_clause21=select_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_select_clause.add(select_clause21.getTree());
                    pushFollow(FOLLOW_from_clause_in_range_variable_declaration_source577);
                    from_clause22=from_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_from_clause.add(from_clause22.getTree());
                    // JPA.g:106:43: ( where_clause )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==61) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // JPA.g:106:44: where_clause
                            {
                            pushFollow(FOLLOW_where_clause_in_range_variable_declaration_source580);
                            where_clause23=where_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_where_clause.add(where_clause23.getTree());

                            }
                            break;

                    }

                    // JPA.g:106:59: ( groupby_clause )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==GROUP) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // JPA.g:106:60: groupby_clause
                            {
                            pushFollow(FOLLOW_groupby_clause_in_range_variable_declaration_source585);
                            groupby_clause24=groupby_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause24.getTree());

                            }
                            break;

                    }

                    // JPA.g:106:77: ( having_clause )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==HAVING) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // JPA.g:106:78: having_clause
                            {
                            pushFollow(FOLLOW_having_clause_in_range_variable_declaration_source590);
                            having_clause25=having_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_having_clause.add(having_clause25.getTree());

                            }
                            break;

                    }

                    // JPA.g:106:93: ( orderby_clause )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==ORDER) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // JPA.g:106:94: orderby_clause
                            {
                            pushFollow(FOLLOW_orderby_clause_in_range_variable_declaration_source594);
                            orderby_clause26=orderby_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause26.getTree());

                            }
                            break;

                    }

                    rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_range_variable_declaration_source600); if (state.failed) return retval; 
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
                    // 107:3: -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                    {
                        // JPA.g:107:6: ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);

                        // JPA.g:107:37: ( select_clause )?
                        if ( stream_select_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_select_clause.nextTree());

                        }
                        stream_select_clause.reset();
                        adaptor.addChild(root_1, stream_from_clause.nextTree());
                        // JPA.g:107:66: ( where_clause )?
                        if ( stream_where_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_where_clause.nextTree());

                        }
                        stream_where_clause.reset();
                        // JPA.g:107:82: ( groupby_clause )?
                        if ( stream_groupby_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                        }
                        stream_groupby_clause.reset();
                        // JPA.g:107:100: ( having_clause )?
                        if ( stream_having_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_having_clause.nextTree());

                        }
                        stream_having_clause.reset();
                        // JPA.g:107:117: ( orderby_clause )?
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
    // JPA.g:110:1: join : join_spec join_association_path_expression ( 'AS' )? identification_variable -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ) ;
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
            // JPA.g:111:2: ( join_spec join_association_path_expression ( 'AS' )? identification_variable -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ) )
            // JPA.g:111:4: join_spec join_association_path_expression ( 'AS' )? identification_variable
            {
            pushFollow(FOLLOW_join_spec_in_join653);
            join_spec27=join_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_spec.add(join_spec27.getTree());
            pushFollow(FOLLOW_join_association_path_expression_in_join655);
            join_association_path_expression28=join_association_path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression28.getTree());
            // JPA.g:111:47: ( 'AS' )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==55) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // JPA.g:111:49: 'AS'
                    {
                    string_literal29=(Token)match(input,55,FOLLOW_55_in_join659); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_55.add(string_literal29);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_join664);
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
            // 112:2: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression )
            {
                // JPA.g:112:5: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression )
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
    // JPA.g:115:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
    public final JPAParser.fetch_join_return fetch_join() throws RecognitionException {
        JPAParser.fetch_join_return retval = new JPAParser.fetch_join_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal32=null;
        JPAParser.join_spec_return join_spec31 = null;

        JPAParser.join_association_path_expression_return join_association_path_expression33 = null;


        Object string_literal32_tree=null;

        try {
            // JPA.g:116:2: ( join_spec 'FETCH' join_association_path_expression )
            // JPA.g:116:4: join_spec 'FETCH' join_association_path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_join_spec_in_fetch_join692);
            join_spec31=join_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec31.getTree());
            string_literal32=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join694); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal32_tree = (Object)adaptor.create(string_literal32);
            adaptor.addChild(root_0, string_literal32_tree);
            }
            pushFollow(FOLLOW_join_association_path_expression_in_fetch_join696);
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
    // JPA.g:118:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
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
            // JPA.g:119:2: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
            // JPA.g:119:3: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:119:3: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
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
                    // JPA.g:119:4: ( 'LEFT' ) ( 'OUTER' )?
                    {
                    // JPA.g:119:4: ( 'LEFT' )
                    // JPA.g:119:5: 'LEFT'
                    {
                    string_literal34=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec706); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal34_tree = (Object)adaptor.create(string_literal34);
                    adaptor.addChild(root_0, string_literal34_tree);
                    }

                    }

                    // JPA.g:119:13: ( 'OUTER' )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==OUTER) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // JPA.g:119:14: 'OUTER'
                            {
                            string_literal35=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec710); if (state.failed) return retval;
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
                    // JPA.g:119:26: 'INNER'
                    {
                    string_literal36=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec716); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal36_tree = (Object)adaptor.create(string_literal36);
                    adaptor.addChild(root_0, string_literal36_tree);
                    }

                    }
                    break;

            }

            string_literal37=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec721); if (state.failed) return retval;
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
    // JPA.g:121:1: join_association_path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
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
            // JPA.g:122:2: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
            // JPA.g:122:4: identification_variable '.' ( field '.' )* ( field )?
            {
            pushFollow(FOLLOW_identification_variable_in_join_association_path_expression731);
            identification_variable38=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable38.getTree());
            char_literal39=(Token)match(input,57,FOLLOW_57_in_join_association_path_expression733); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_57.add(char_literal39);

            // JPA.g:122:32: ( field '.' )*
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
            	    // JPA.g:122:34: field '.'
            	    {
            	    pushFollow(FOLLOW_field_in_join_association_path_expression737);
            	    field40=field();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_field.add(field40.getTree());
            	    char_literal41=(Token)match(input,57,FOLLOW_57_in_join_association_path_expression739); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_57.add(char_literal41);


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

            // JPA.g:122:47: ( field )?
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
                    // JPA.g:122:49: field
                    {
                    pushFollow(FOLLOW_field_in_join_association_path_expression746);
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
            // 123:2: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
            {
                // JPA.g:123:5: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable38!=null?input.toString(identification_variable38.start,identification_variable38.stop):null)), root_1);

                // JPA.g:123:56: ( field )*
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
            // JPA.g:126:2: ( 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
            // JPA.g:126:4: 'IN' '(' path_expression ')' ( 'AS' )? identification_variable
            {
            string_literal43=(Token)match(input,58,FOLLOW_58_in_collection_member_declaration778); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_58.add(string_literal43);

            char_literal44=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration780); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(char_literal44);

            pushFollow(FOLLOW_path_expression_in_collection_member_declaration782);
            path_expression45=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_path_expression.add(path_expression45.getTree());
            char_literal46=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration784); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(char_literal46);

            // JPA.g:126:33: ( 'AS' )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==55) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // JPA.g:126:35: 'AS'
                    {
                    string_literal47=(Token)match(input,55,FOLLOW_55_in_collection_member_declaration788); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_55.add(string_literal47);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_collection_member_declaration793);
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
            // 127:2: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
            {
                // JPA.g:127:5: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_COLLECTION_MEMBER, (identification_variable48!=null?input.toString(identification_variable48.start,identification_variable48.stop):null)), root_1);

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
    // JPA.g:129:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
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
            // JPA.g:130:2: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
            // JPA.g:130:4: identification_variable '.' ( field '.' )* ( field )?
            {
            pushFollow(FOLLOW_identification_variable_in_path_expression817);
            identification_variable49=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable49.getTree());
            char_literal50=(Token)match(input,57,FOLLOW_57_in_path_expression819); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_57.add(char_literal50);

            // JPA.g:130:32: ( field '.' )*
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
            	    // JPA.g:130:34: field '.'
            	    {
            	    pushFollow(FOLLOW_field_in_path_expression823);
            	    field51=field();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_field.add(field51.getTree());
            	    char_literal52=(Token)match(input,57,FOLLOW_57_in_path_expression825); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_57.add(char_literal52);


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);

            // JPA.g:130:47: ( field )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==GROUP) ) {
                int LA22_1 = input.LA(2);

                if ( (LA22_1==EOF||(LA22_1>=HAVING && LA22_1<=DESC)||(LA22_1>=OR && LA22_1<=AND)||LA22_1==RPAREN||(LA22_1>=ORDER && LA22_1<=GROUP)||LA22_1==WORD||(LA22_1>=53 && LA22_1<=55)||LA22_1==58||(LA22_1>=62 && LA22_1<=64)||LA22_1==66||LA22_1==69||(LA22_1>=75 && LA22_1<=84)) ) {
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
                    // JPA.g:130:49: field
                    {
                    pushFollow(FOLLOW_field_in_path_expression832);
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
            // 131:2: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
            {
                // JPA.g:131:5: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable49!=null?input.toString(identification_variable49.start,identification_variable49.stop):null)), root_1);

                // JPA.g:131:56: ( field )*
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
    // JPA.g:134:1: select_clause : ( 'DISTINCT' )? select_expression ( ',' select_expression )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* ) ;
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
            // JPA.g:135:2: ( ( 'DISTINCT' )? select_expression ( ',' select_expression )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* ) )
            // JPA.g:135:4: ( 'DISTINCT' )? select_expression ( ',' select_expression )*
            {
            // JPA.g:135:4: ( 'DISTINCT' )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==DISTINCT) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // JPA.g:135:6: 'DISTINCT'
                    {
                    string_literal54=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause868); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal54);


                    }
                    break;

            }

            pushFollow(FOLLOW_select_expression_in_select_clause873);
            select_expression55=select_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_select_expression.add(select_expression55.getTree());
            // JPA.g:135:38: ( ',' select_expression )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==54) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // JPA.g:135:40: ',' select_expression
            	    {
            	    char_literal56=(Token)match(input,54,FOLLOW_54_in_select_clause877); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_54.add(char_literal56);

            	    pushFollow(FOLLOW_select_expression_in_select_clause879);
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
            // 136:2: -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* )
            {
                // JPA.g:136:5: ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_expression ) )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);

                // JPA.g:136:25: ( 'DISTINCT' )?
                if ( stream_DISTINCT.hasNext() ) {
                    adaptor.addChild(root_1, stream_DISTINCT.nextNode());

                }
                stream_DISTINCT.reset();
                // JPA.g:136:41: ( ^( T_SELECTED_ITEM[] select_expression ) )*
                while ( stream_select_expression.hasNext() ) {
                    // JPA.g:136:43: ^( T_SELECTED_ITEM[] select_expression )
                    {
                    Object root_2 = (Object)adaptor.nil();
                    root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEM, "T_SELECTED_ITEM"), root_2);

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
    // JPA.g:139:1: select_expression : ( path_expression | aggregate_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | 'OBJECT' '(' identification_variable ')' | constructor_expression );
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
            // JPA.g:140:2: ( path_expression | aggregate_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | 'OBJECT' '(' identification_variable ')' | constructor_expression )
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
                    // JPA.g:140:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_select_expression925);
                    path_expression58=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression58.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:141:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_select_expression930);
                    aggregate_expression59=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression59.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:142:4: identification_variable
                    {
                    pushFollow(FOLLOW_identification_variable_in_select_expression935);
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
                    // 142:28: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
                    {
                        // JPA.g:142:31: ^( T_SELECTED_ENTITY[$identification_variable.text] )
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
                    // JPA.g:143:4: 'OBJECT' '(' identification_variable ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal61=(Token)match(input,59,FOLLOW_59_in_select_expression950); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal61_tree = (Object)adaptor.create(string_literal61);
                    adaptor.addChild(root_0, string_literal61_tree);
                    }
                    char_literal62=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression952); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal62_tree = (Object)adaptor.create(char_literal62);
                    adaptor.addChild(root_0, char_literal62_tree);
                    }
                    pushFollow(FOLLOW_identification_variable_in_select_expression953);
                    identification_variable63=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable63.getTree());
                    char_literal64=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression954); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal64_tree = (Object)adaptor.create(char_literal64);
                    adaptor.addChild(root_0, char_literal64_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:144:4: constructor_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_constructor_expression_in_select_expression959);
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
    // JPA.g:146:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
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
            // JPA.g:147:2: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
            // JPA.g:147:4: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal66=(Token)match(input,60,FOLLOW_60_in_constructor_expression968); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal66_tree = (Object)adaptor.create(string_literal66);
            adaptor.addChild(root_0, string_literal66_tree);
            }
            pushFollow(FOLLOW_constructor_name_in_constructor_expression970);
            constructor_name67=constructor_name();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name67.getTree());
            char_literal68=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression972); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal68_tree = (Object)adaptor.create(char_literal68);
            adaptor.addChild(root_0, char_literal68_tree);
            }
            pushFollow(FOLLOW_constructor_item_in_constructor_expression974);
            constructor_item69=constructor_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item69.getTree());
            // JPA.g:147:48: ( ',' constructor_item )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==54) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // JPA.g:147:49: ',' constructor_item
            	    {
            	    char_literal70=(Token)match(input,54,FOLLOW_54_in_constructor_expression977); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal70_tree = (Object)adaptor.create(char_literal70);
            	    adaptor.addChild(root_0, char_literal70_tree);
            	    }
            	    pushFollow(FOLLOW_constructor_item_in_constructor_expression979);
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

            char_literal72=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression983); if (state.failed) return retval;
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
    // JPA.g:149:1: constructor_item : ( path_expression | aggregate_expression );
    public final JPAParser.constructor_item_return constructor_item() throws RecognitionException {
        JPAParser.constructor_item_return retval = new JPAParser.constructor_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression73 = null;

        JPAParser.aggregate_expression_return aggregate_expression74 = null;



        try {
            // JPA.g:150:2: ( path_expression | aggregate_expression )
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
                    // JPA.g:150:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_constructor_item992);
                    path_expression73=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression73.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:150:22: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_constructor_item996);
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
    // JPA.g:152:1: aggregate_expression : ( aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' ) ) ;
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
            // JPA.g:153:2: ( ( aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' ) ) )
            // JPA.g:153:4: ( aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' ) )
            {
            // JPA.g:153:4: ( aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' ) )
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

                            if ( (LA30_5==RPAREN) ) {
                                alt30=2;
                            }
                            else if ( (LA30_5==57) ) {
                                alt30=1;
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

                        if ( (LA30_5==RPAREN) ) {
                            alt30=2;
                        }
                        else if ( (LA30_5==57) ) {
                            alt30=1;
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
                    // JPA.g:153:6: aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')'
                    {
                    pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1008);
                    aggregate_expression_function_name75=aggregate_expression_function_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name75.getTree());
                    char_literal76=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1010); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal76);

                    // JPA.g:153:45: ( 'DISTINCT' )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==DISTINCT) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // JPA.g:153:47: 'DISTINCT'
                            {
                            string_literal77=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1014); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal77);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_path_expression_in_aggregate_expression1019);
                    path_expression78=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression78.getTree());
                    char_literal79=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1021); if (state.failed) return retval; 
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
                    // 154:2: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
                    {
                        // JPA.g:154:5: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_AGGREGATE_EXPR, "T_AGGREGATE_EXPR"), root_1);

                        adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
                        adaptor.addChild(root_1, stream_LPAREN.nextNode());
                        // JPA.g:154:66: ( 'DISTINCT' )?
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
                    // JPA.g:155:4: 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')'
                    {
                    string_literal80=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1053); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COUNT.add(string_literal80);

                    char_literal81=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1055); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal81);

                    // JPA.g:155:16: ( 'DISTINCT' )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0==DISTINCT) ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // JPA.g:155:18: 'DISTINCT'
                            {
                            string_literal82=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1059); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal82);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_identification_variable_in_aggregate_expression1064);
                    identification_variable83=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable83.getTree());
                    char_literal84=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1066); if (state.failed) return retval; 
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
                    // 156:2: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' )
                    {
                        // JPA.g:156:5: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? identification_variable ')' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_AGGREGATE_EXPR, "T_AGGREGATE_EXPR"), root_1);

                        adaptor.addChild(root_1, stream_COUNT.nextNode());
                        adaptor.addChild(root_1, stream_LPAREN.nextNode());
                        // JPA.g:156:39: ( 'DISTINCT' )?
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
    // JPA.g:159:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' ) ;
    public final JPAParser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
        JPAParser.aggregate_expression_function_name_return retval = new JPAParser.aggregate_expression_function_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set85=null;

        Object set85_tree=null;

        try {
            // JPA.g:160:2: ( ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' ) )
            // JPA.g:160:4: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
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
    // JPA.g:163:1: where_clause : (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) ) ;
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
            // JPA.g:164:2: ( (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) ) )
            // JPA.g:164:4: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) )
            {
            // JPA.g:164:4: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) )
            int alt31=2;
            alt31 = dfa31.predict(input);
            switch (alt31) {
                case 1 :
                    // JPA.g:164:5: wh= 'WHERE' conditional_expression
                    {
                    wh=(Token)match(input,61,FOLLOW_61_in_where_clause1142); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_61.add(wh);

                    pushFollow(FOLLOW_conditional_expression_in_where_clause1144);
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
                    // 164:40: -> ^( T_CONDITION[$wh] conditional_expression )
                    {
                        // JPA.g:164:43: ^( T_CONDITION[$wh] conditional_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_CONDITION, wh), root_1);

                        adaptor.addChild(root_1, stream_conditional_expression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // JPA.g:165:4: 'WHERE' path_expression
                    {
                    string_literal87=(Token)match(input,61,FOLLOW_61_in_where_clause1161); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_61.add(string_literal87);

                    pushFollow(FOLLOW_path_expression_in_where_clause1163);
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
                    // 165:28: -> ^( T_CONDITION[$wh] path_expression )
                    {
                        // JPA.g:165:31: ^( T_CONDITION[$wh] path_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_CONDITION, wh), root_1);

                        adaptor.addChild(root_1, stream_path_expression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
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
    // $ANTLR end "where_clause"

    public static class groupby_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "groupby_clause"
    // JPA.g:167:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
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
            // JPA.g:168:2: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
            // JPA.g:168:4: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
            {
            string_literal89=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1186); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_GROUP.add(string_literal89);

            string_literal90=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1188); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BY.add(string_literal90);

            pushFollow(FOLLOW_groupby_item_in_groupby_clause1190);
            groupby_item91=groupby_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item91.getTree());
            // JPA.g:168:30: ( ',' groupby_item )*
            loop32:
            do {
                int alt32=2;
                int LA32_0 = input.LA(1);

                if ( (LA32_0==54) ) {
                    alt32=1;
                }


                switch (alt32) {
            	case 1 :
            	    // JPA.g:168:32: ',' groupby_item
            	    {
            	    char_literal92=(Token)match(input,54,FOLLOW_54_in_groupby_clause1194); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_54.add(char_literal92);

            	    pushFollow(FOLLOW_groupby_item_in_groupby_clause1196);
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
            // 169:2: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
            {
                // JPA.g:169:5: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_GROUP_BY, "T_GROUP_BY"), root_1);

                adaptor.addChild(root_1, stream_GROUP.nextNode());
                adaptor.addChild(root_1, stream_BY.nextNode());
                // JPA.g:169:34: ( groupby_item )*
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
    // JPA.g:172:1: groupby_item : ( path_expression | identification_variable );
    public final JPAParser.groupby_item_return groupby_item() throws RecognitionException {
        JPAParser.groupby_item_return retval = new JPAParser.groupby_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression94 = null;

        JPAParser.identification_variable_return identification_variable95 = null;



        try {
            // JPA.g:173:2: ( path_expression | identification_variable )
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==WORD) ) {
                int LA33_1 = input.LA(2);

                if ( (LA33_1==57) ) {
                    alt33=1;
                }
                else if ( (LA33_1==EOF||LA33_1==HAVING||LA33_1==RPAREN||LA33_1==ORDER||LA33_1==54) ) {
                    alt33=2;
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
                    // JPA.g:173:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_groupby_item1233);
                    path_expression94=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression94.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:173:22: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_groupby_item1237);
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
    // JPA.g:175:1: having_clause : 'HAVING' conditional_expression ;
    public final JPAParser.having_clause_return having_clause() throws RecognitionException {
        JPAParser.having_clause_return retval = new JPAParser.having_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal96=null;
        JPAParser.conditional_expression_return conditional_expression97 = null;


        Object string_literal96_tree=null;

        try {
            // JPA.g:176:2: ( 'HAVING' conditional_expression )
            // JPA.g:176:4: 'HAVING' conditional_expression
            {
            root_0 = (Object)adaptor.nil();

            string_literal96=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1246); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal96_tree = (Object)adaptor.create(string_literal96);
            adaptor.addChild(root_0, string_literal96_tree);
            }
            pushFollow(FOLLOW_conditional_expression_in_having_clause1248);
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
    // JPA.g:178:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
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
            // JPA.g:179:2: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
            // JPA.g:179:4: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
            {
            string_literal98=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1258); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ORDER.add(string_literal98);

            string_literal99=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1260); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BY.add(string_literal99);

            pushFollow(FOLLOW_orderby_item_in_orderby_clause1262);
            orderby_item100=orderby_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item100.getTree());
            // JPA.g:179:30: ( ',' orderby_item )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==54) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // JPA.g:179:32: ',' orderby_item
            	    {
            	    char_literal101=(Token)match(input,54,FOLLOW_54_in_orderby_clause1266); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_54.add(char_literal101);

            	    pushFollow(FOLLOW_orderby_item_in_orderby_clause1268);
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
            // 180:2: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
            {
                // JPA.g:180:5: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_ORDER_BY, "T_ORDER_BY"), root_1);

                adaptor.addChild(root_1, stream_ORDER.nextNode());
                adaptor.addChild(root_1, stream_BY.nextNode());
                // JPA.g:180:34: ( orderby_item )*
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
    // JPA.g:183:1: orderby_item : ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) ) ;
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
        RewriteRuleTokenStream stream_ASC=new RewriteRuleTokenStream(adaptor,"token ASC");
        RewriteRuleTokenStream stream_DESC=new RewriteRuleTokenStream(adaptor,"token DESC");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        try {
            // JPA.g:184:2: ( ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) ) )
            // JPA.g:184:4: ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) )
            {
            // JPA.g:184:4: ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) )
            int alt36=2;
            alt36 = dfa36.predict(input);
            switch (alt36) {
                case 1 :
                    // JPA.g:184:6: path_expression ( 'ASC' )?
                    {
                    pushFollow(FOLLOW_path_expression_in_orderby_item1308);
                    path_expression103=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression103.getTree());
                    // JPA.g:184:22: ( 'ASC' )?
                    int alt35=2;
                    int LA35_0 = input.LA(1);

                    if ( (LA35_0==ASC) ) {
                        alt35=1;
                    }
                    switch (alt35) {
                        case 1 :
                            // JPA.g:184:24: 'ASC'
                            {
                            string_literal104=(Token)match(input,ASC,FOLLOW_ASC_in_orderby_item1312); if (state.failed) return retval; 
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
                    // 184:33: -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? )
                    {
                        // JPA.g:184:36: ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_ORDER_BY_FIELD, "T_ORDER_BY_FIELD"), root_1);

                        adaptor.addChild(root_1, stream_path_expression.nextTree());
                        // JPA.g:184:74: ( 'ASC' )?
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
                    // JPA.g:185:4: path_expression 'DESC'
                    {
                    pushFollow(FOLLOW_path_expression_in_orderby_item1339);
                    path_expression105=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression105.getTree());
                    string_literal106=(Token)match(input,DESC,FOLLOW_DESC_in_orderby_item1341); if (state.failed) return retval; 
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
                    // 185:27: -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' )
                    {
                        // JPA.g:185:30: ^( T_ORDER_BY_FIELD[] path_expression 'DESC' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_ORDER_BY_FIELD, "T_ORDER_BY_FIELD"), root_1);

                        adaptor.addChild(root_1, stream_path_expression.nextTree());
                        adaptor.addChild(root_1, stream_DESC.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
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
    // $ANTLR end "orderby_item"

    public static class subquery_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "subquery"
    // JPA.g:188:1: subquery : lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
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
            // JPA.g:189:2: (lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
            // JPA.g:189:4: lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
            {
            lp=(Token)match(input,56,FOLLOW_56_in_subquery1369); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_56.add(lp);

            pushFollow(FOLLOW_simple_select_clause_in_subquery1371);
            simple_select_clause107=simple_select_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause107.getTree());
            pushFollow(FOLLOW_subquery_from_clause_in_subquery1373);
            subquery_from_clause108=subquery_from_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause108.getTree());
            // JPA.g:189:59: ( where_clause )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==61) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // JPA.g:189:60: where_clause
                    {
                    pushFollow(FOLLOW_where_clause_in_subquery1376);
                    where_clause109=where_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_where_clause.add(where_clause109.getTree());

                    }
                    break;

            }

            // JPA.g:189:75: ( groupby_clause )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==GROUP) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // JPA.g:189:76: groupby_clause
                    {
                    pushFollow(FOLLOW_groupby_clause_in_subquery1381);
                    groupby_clause110=groupby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause110.getTree());

                    }
                    break;

            }

            // JPA.g:189:93: ( having_clause )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==HAVING) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // JPA.g:189:94: having_clause
                    {
                    pushFollow(FOLLOW_having_clause_in_subquery1386);
                    having_clause111=having_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_having_clause.add(having_clause111.getTree());

                    }
                    break;

            }

            rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1392); if (state.failed) return retval; 
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
            // 190:3: -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
            {
                // JPA.g:190:6: ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);

                adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
                adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
                // JPA.g:190:78: ( where_clause )?
                if ( stream_where_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_where_clause.nextTree());

                }
                stream_where_clause.reset();
                // JPA.g:190:94: ( groupby_clause )?
                if ( stream_groupby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                }
                stream_groupby_clause.reset();
                // JPA.g:190:112: ( having_clause )?
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
    // JPA.g:192:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
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
            // JPA.g:193:2: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
            // JPA.g:193:4: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
            {
            fr=(Token)match(input,53,FOLLOW_53_in_subquery_from_clause1437); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_53.add(fr);

            pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1439);
            subselect_identification_variable_declaration112=subselect_identification_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration112.getTree());
            // JPA.g:193:61: ( ',' subselect_identification_variable_declaration )*
            loop40:
            do {
                int alt40=2;
                int LA40_0 = input.LA(1);

                if ( (LA40_0==54) ) {
                    alt40=1;
                }


                switch (alt40) {
            	case 1 :
            	    // JPA.g:193:63: ',' subselect_identification_variable_declaration
            	    {
            	    char_literal113=(Token)match(input,54,FOLLOW_54_in_subquery_from_clause1443); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_54.add(char_literal113);

            	    pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1445);
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
            // 194:2: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
            {
                // JPA.g:194:5: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SOURCES, fr), root_1);

                // JPA.g:194:23: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
                while ( stream_subselect_identification_variable_declaration.hasNext() ) {
                    // JPA.g:194:25: ^( T_SOURCE subselect_identification_variable_declaration )
                    {
                    Object root_2 = (Object)adaptor.nil();
                    root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SOURCE, "T_SOURCE"), root_2);

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
    // JPA.g:197:1: subselect_identification_variable_declaration : ( identification_variable_declaration | association_path_expression ( 'AS' )? identification_variable | collection_member_declaration );
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
            // JPA.g:198:2: ( identification_variable_declaration | association_path_expression ( 'AS' )? identification_variable | collection_member_declaration )
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
                    // JPA.g:198:4: identification_variable_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1484);
                    identification_variable_declaration115=identification_variable_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration115.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:199:4: association_path_expression ( 'AS' )? identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_association_path_expression_in_subselect_identification_variable_declaration1489);
                    association_path_expression116=association_path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, association_path_expression116.getTree());
                    // JPA.g:199:32: ( 'AS' )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==55) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // JPA.g:199:33: 'AS'
                            {
                            string_literal117=(Token)match(input,55,FOLLOW_55_in_subselect_identification_variable_declaration1492); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal117_tree = (Object)adaptor.create(string_literal117);
                            adaptor.addChild(root_0, string_literal117_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration1496);
                    identification_variable118=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable118.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:200:4: collection_member_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_declaration_in_subselect_identification_variable_declaration1501);
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
    // JPA.g:202:1: association_path_expression : path_expression ;
    public final JPAParser.association_path_expression_return association_path_expression() throws RecognitionException {
        JPAParser.association_path_expression_return retval = new JPAParser.association_path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression120 = null;



        try {
            // JPA.g:203:2: ( path_expression )
            // JPA.g:203:4: path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_association_path_expression1510);
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
    // JPA.g:205:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
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
            // JPA.g:206:2: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
            // JPA.g:206:4: ( 'DISTINCT' )? simple_select_expression
            {
            // JPA.g:206:4: ( 'DISTINCT' )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==DISTINCT) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // JPA.g:206:6: 'DISTINCT'
                    {
                    string_literal121=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause1522); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal121);


                    }
                    break;

            }

            pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause1527);
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
            // 207:2: -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
            {
                // JPA.g:207:5: ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);

                // JPA.g:207:25: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEM, "T_SELECTED_ITEM"), root_2);

                // JPA.g:207:46: ( 'DISTINCT' )?
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
    // JPA.g:210:1: simple_select_expression : ( path_expression | aggregate_expression | identification_variable );
    public final JPAParser.simple_select_expression_return simple_select_expression() throws RecognitionException {
        JPAParser.simple_select_expression_return retval = new JPAParser.simple_select_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression123 = null;

        JPAParser.aggregate_expression_return aggregate_expression124 = null;

        JPAParser.identification_variable_return identification_variable125 = null;



        try {
            // JPA.g:211:2: ( path_expression | aggregate_expression | identification_variable )
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
                    // JPA.g:211:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_simple_select_expression1565);
                    path_expression123=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression123.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:212:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression1570);
                    aggregate_expression124=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression124.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:213:4: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_simple_select_expression1575);
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
    // JPA.g:215:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
    public final JPAParser.conditional_expression_return conditional_expression() throws RecognitionException {
        JPAParser.conditional_expression_return retval = new JPAParser.conditional_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal127=null;
        JPAParser.conditional_term_return conditional_term126 = null;

        JPAParser.conditional_term_return conditional_term128 = null;


        Object string_literal127_tree=null;

        try {
            // JPA.g:216:2: ( ( conditional_term ) ( 'OR' conditional_term )* )
            // JPA.g:216:4: ( conditional_term ) ( 'OR' conditional_term )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:216:4: ( conditional_term )
            // JPA.g:216:5: conditional_term
            {
            pushFollow(FOLLOW_conditional_term_in_conditional_expression1585);
            conditional_term126=conditional_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term126.getTree());

            }

            // JPA.g:216:23: ( 'OR' conditional_term )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==OR) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // JPA.g:216:24: 'OR' conditional_term
            	    {
            	    string_literal127=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression1589); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal127_tree = (Object)adaptor.create(string_literal127);
            	    adaptor.addChild(root_0, string_literal127_tree);
            	    }
            	    pushFollow(FOLLOW_conditional_term_in_conditional_expression1591);
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
    // JPA.g:218:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
    public final JPAParser.conditional_term_return conditional_term() throws RecognitionException {
        JPAParser.conditional_term_return retval = new JPAParser.conditional_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal130=null;
        JPAParser.conditional_factor_return conditional_factor129 = null;

        JPAParser.conditional_factor_return conditional_factor131 = null;


        Object string_literal130_tree=null;

        try {
            // JPA.g:219:2: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
            // JPA.g:219:4: ( conditional_factor ) ( 'AND' conditional_factor )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:219:4: ( conditional_factor )
            // JPA.g:219:5: conditional_factor
            {
            pushFollow(FOLLOW_conditional_factor_in_conditional_term1603);
            conditional_factor129=conditional_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor129.getTree());

            }

            // JPA.g:219:25: ( 'AND' conditional_factor )*
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( (LA46_0==AND) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // JPA.g:219:26: 'AND' conditional_factor
            	    {
            	    string_literal130=(Token)match(input,AND,FOLLOW_AND_in_conditional_term1607); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal130_tree = (Object)adaptor.create(string_literal130);
            	    adaptor.addChild(root_0, string_literal130_tree);
            	    }
            	    pushFollow(FOLLOW_conditional_factor_in_conditional_term1609);
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
    // JPA.g:221:1: conditional_factor : ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' ) ;
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
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_simple_cond_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_cond_expression");
        RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");
        try {
            // JPA.g:222:2: ( ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' ) )
            // JPA.g:222:4: ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' )
            {
            // JPA.g:222:4: ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' )
            int alt48=2;
            alt48 = dfa48.predict(input);
            switch (alt48) {
                case 1 :
                    // JPA.g:222:6: ( 'NOT' )? simple_cond_expression
                    {
                    // JPA.g:222:6: ( 'NOT' )?
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
                            // JPA.g:222:8: 'NOT'
                            {
                            string_literal132=(Token)match(input,62,FOLLOW_62_in_conditional_factor1625); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_62.add(string_literal132);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_simple_cond_expression_in_conditional_factor1630);
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
                    // 223:2: -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression )
                    {
                        // JPA.g:223:5: ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SIMPLE_CONDITION, "T_SIMPLE_CONDITION"), root_1);

                        // JPA.g:223:29: ( 'NOT' )?
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
                    // JPA.g:224:4: '(' conditional_expression ')'
                    {
                    char_literal134=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_factor1656); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal134);

                    pushFollow(FOLLOW_conditional_expression_in_conditional_factor1658);
                    conditional_expression135=conditional_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression135.getTree());
                    char_literal136=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_factor1660); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(char_literal136);


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
    // $ANTLR end "conditional_factor"

    public static class simple_cond_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "simple_cond_expression"
    // JPA.g:227:1: simple_cond_expression : ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression ) ;
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
            // JPA.g:228:2: ( ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression ) )
            // JPA.g:228:4: ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:228:4: ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
            int alt49=9;
            alt49 = dfa49.predict(input);
            switch (alt49) {
                case 1 :
                    // JPA.g:228:6: comparison_expression
                    {
                    pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression1676);
                    comparison_expression137=comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression137.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:229:4: between_expression
                    {
                    pushFollow(FOLLOW_between_expression_in_simple_cond_expression1682);
                    between_expression138=between_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression138.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:230:4: like_expression
                    {
                    pushFollow(FOLLOW_like_expression_in_simple_cond_expression1688);
                    like_expression139=like_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression139.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:231:4: in_expression
                    {
                    pushFollow(FOLLOW_in_expression_in_simple_cond_expression1694);
                    in_expression140=in_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression140.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:232:4: null_comparison_expression
                    {
                    pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression1700);
                    null_comparison_expression141=null_comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression141.getTree());

                    }
                    break;
                case 6 :
                    // JPA.g:233:4: empty_collection_comparison_expression
                    {
                    pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression1706);
                    empty_collection_comparison_expression142=empty_collection_comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression142.getTree());

                    }
                    break;
                case 7 :
                    // JPA.g:234:4: collection_member_expression
                    {
                    pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression1712);
                    collection_member_expression143=collection_member_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression143.getTree());

                    }
                    break;
                case 8 :
                    // JPA.g:235:4: exists_expression
                    {
                    pushFollow(FOLLOW_exists_expression_in_simple_cond_expression1718);
                    exists_expression144=exists_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression144.getTree());

                    }
                    break;
                case 9 :
                    // JPA.g:236:4: date_macro_expression
                    {
                    pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression1724);
                    date_macro_expression145=date_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_macro_expression145.getTree());

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
    // $ANTLR end "simple_cond_expression"

    public static class between_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "between_expression"
    // JPA.g:238:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
    public final JPAParser.between_expression_return between_expression() throws RecognitionException {
        JPAParser.between_expression_return retval = new JPAParser.between_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal147=null;
        Token string_literal148=null;
        Token string_literal150=null;
        Token string_literal153=null;
        Token string_literal154=null;
        Token string_literal156=null;
        Token string_literal159=null;
        Token string_literal160=null;
        Token string_literal162=null;
        JPAParser.arithmetic_expression_return arithmetic_expression146 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression149 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression151 = null;

        JPAParser.string_expression_return string_expression152 = null;

        JPAParser.string_expression_return string_expression155 = null;

        JPAParser.string_expression_return string_expression157 = null;

        JPAParser.datetime_expression_return datetime_expression158 = null;

        JPAParser.datetime_expression_return datetime_expression161 = null;

        JPAParser.datetime_expression_return datetime_expression163 = null;


        Object string_literal147_tree=null;
        Object string_literal148_tree=null;
        Object string_literal150_tree=null;
        Object string_literal153_tree=null;
        Object string_literal154_tree=null;
        Object string_literal156_tree=null;
        Object string_literal159_tree=null;
        Object string_literal160_tree=null;
        Object string_literal162_tree=null;

        try {
            // JPA.g:239:2: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
            int alt53=3;
            alt53 = dfa53.predict(input);
            switch (alt53) {
                case 1 :
                    // JPA.g:239:4: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1735);
                    arithmetic_expression146=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression146.getTree());
                    // JPA.g:239:26: ( 'NOT' )?
                    int alt50=2;
                    int LA50_0 = input.LA(1);

                    if ( (LA50_0==62) ) {
                        alt50=1;
                    }
                    switch (alt50) {
                        case 1 :
                            // JPA.g:239:27: 'NOT'
                            {
                            string_literal147=(Token)match(input,62,FOLLOW_62_in_between_expression1738); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal147_tree = (Object)adaptor.create(string_literal147);
                            adaptor.addChild(root_0, string_literal147_tree);
                            }

                            }
                            break;

                    }

                    string_literal148=(Token)match(input,63,FOLLOW_63_in_between_expression1742); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal148_tree = (Object)adaptor.create(string_literal148);
                    adaptor.addChild(root_0, string_literal148_tree);
                    }
                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1744);
                    arithmetic_expression149=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression149.getTree());
                    string_literal150=(Token)match(input,AND,FOLLOW_AND_in_between_expression1746); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal150_tree = (Object)adaptor.create(string_literal150);
                    adaptor.addChild(root_0, string_literal150_tree);
                    }
                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1748);
                    arithmetic_expression151=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression151.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:240:4: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_expression_in_between_expression1753);
                    string_expression152=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression152.getTree());
                    // JPA.g:240:22: ( 'NOT' )?
                    int alt51=2;
                    int LA51_0 = input.LA(1);

                    if ( (LA51_0==62) ) {
                        alt51=1;
                    }
                    switch (alt51) {
                        case 1 :
                            // JPA.g:240:23: 'NOT'
                            {
                            string_literal153=(Token)match(input,62,FOLLOW_62_in_between_expression1756); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal153_tree = (Object)adaptor.create(string_literal153);
                            adaptor.addChild(root_0, string_literal153_tree);
                            }

                            }
                            break;

                    }

                    string_literal154=(Token)match(input,63,FOLLOW_63_in_between_expression1760); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal154_tree = (Object)adaptor.create(string_literal154);
                    adaptor.addChild(root_0, string_literal154_tree);
                    }
                    pushFollow(FOLLOW_string_expression_in_between_expression1762);
                    string_expression155=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression155.getTree());
                    string_literal156=(Token)match(input,AND,FOLLOW_AND_in_between_expression1764); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal156_tree = (Object)adaptor.create(string_literal156);
                    adaptor.addChild(root_0, string_literal156_tree);
                    }
                    pushFollow(FOLLOW_string_expression_in_between_expression1766);
                    string_expression157=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression157.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:241:4: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_expression_in_between_expression1771);
                    datetime_expression158=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression158.getTree());
                    // JPA.g:241:24: ( 'NOT' )?
                    int alt52=2;
                    int LA52_0 = input.LA(1);

                    if ( (LA52_0==62) ) {
                        alt52=1;
                    }
                    switch (alt52) {
                        case 1 :
                            // JPA.g:241:25: 'NOT'
                            {
                            string_literal159=(Token)match(input,62,FOLLOW_62_in_between_expression1774); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal159_tree = (Object)adaptor.create(string_literal159);
                            adaptor.addChild(root_0, string_literal159_tree);
                            }

                            }
                            break;

                    }

                    string_literal160=(Token)match(input,63,FOLLOW_63_in_between_expression1778); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal160_tree = (Object)adaptor.create(string_literal160);
                    adaptor.addChild(root_0, string_literal160_tree);
                    }
                    pushFollow(FOLLOW_datetime_expression_in_between_expression1780);
                    datetime_expression161=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression161.getTree());
                    string_literal162=(Token)match(input,AND,FOLLOW_AND_in_between_expression1782); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal162_tree = (Object)adaptor.create(string_literal162);
                    adaptor.addChild(root_0, string_literal162_tree);
                    }
                    pushFollow(FOLLOW_datetime_expression_in_between_expression1784);
                    datetime_expression163=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression163.getTree());

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
    // JPA.g:243:1: in_expression : path_expression ( 'NOT' )? 'IN' in_expression_right_part ;
    public final JPAParser.in_expression_return in_expression() throws RecognitionException {
        JPAParser.in_expression_return retval = new JPAParser.in_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal165=null;
        Token string_literal166=null;
        JPAParser.path_expression_return path_expression164 = null;

        JPAParser.in_expression_right_part_return in_expression_right_part167 = null;


        Object string_literal165_tree=null;
        Object string_literal166_tree=null;

        try {
            // JPA.g:244:2: ( path_expression ( 'NOT' )? 'IN' in_expression_right_part )
            // JPA.g:244:4: path_expression ( 'NOT' )? 'IN' in_expression_right_part
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_in_expression1793);
            path_expression164=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression164.getTree());
            // JPA.g:244:20: ( 'NOT' )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==62) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // JPA.g:244:21: 'NOT'
                    {
                    string_literal165=(Token)match(input,62,FOLLOW_62_in_in_expression1796); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal165_tree = (Object)adaptor.create(string_literal165);
                    adaptor.addChild(root_0, string_literal165_tree);
                    }

                    }
                    break;

            }

            string_literal166=(Token)match(input,58,FOLLOW_58_in_in_expression1800); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal166_tree = (Object)adaptor.create(string_literal166);
            adaptor.addChild(root_0, string_literal166_tree);
            }
            pushFollow(FOLLOW_in_expression_right_part_in_in_expression1802);
            in_expression_right_part167=in_expression_right_part();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression_right_part167.getTree());

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
    // JPA.g:246:1: in_expression_right_part : ( '(' in_item ( ',' in_item )* ')' | subquery );
    public final JPAParser.in_expression_right_part_return in_expression_right_part() throws RecognitionException {
        JPAParser.in_expression_right_part_return retval = new JPAParser.in_expression_right_part_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal168=null;
        Token char_literal170=null;
        Token char_literal172=null;
        JPAParser.in_item_return in_item169 = null;

        JPAParser.in_item_return in_item171 = null;

        JPAParser.subquery_return subquery173 = null;


        Object char_literal168_tree=null;
        Object char_literal170_tree=null;
        Object char_literal172_tree=null;

        try {
            // JPA.g:247:2: ( '(' in_item ( ',' in_item )* ')' | subquery )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==LPAREN) ) {
                alt56=1;
            }
            else if ( (LA56_0==56) ) {
                alt56=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // JPA.g:247:4: '(' in_item ( ',' in_item )* ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal168=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression_right_part1811); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal168_tree = (Object)adaptor.create(char_literal168);
                    adaptor.addChild(root_0, char_literal168_tree);
                    }
                    pushFollow(FOLLOW_in_item_in_in_expression_right_part1813);
                    in_item169=in_item();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item169.getTree());
                    // JPA.g:247:16: ( ',' in_item )*
                    loop55:
                    do {
                        int alt55=2;
                        int LA55_0 = input.LA(1);

                        if ( (LA55_0==54) ) {
                            alt55=1;
                        }


                        switch (alt55) {
                    	case 1 :
                    	    // JPA.g:247:17: ',' in_item
                    	    {
                    	    char_literal170=(Token)match(input,54,FOLLOW_54_in_in_expression_right_part1816); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal170_tree = (Object)adaptor.create(char_literal170);
                    	    adaptor.addChild(root_0, char_literal170_tree);
                    	    }
                    	    pushFollow(FOLLOW_in_item_in_in_expression_right_part1818);
                    	    in_item171=in_item();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item171.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop55;
                        }
                    } while (true);

                    char_literal172=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression_right_part1822); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal172_tree = (Object)adaptor.create(char_literal172);
                    adaptor.addChild(root_0, char_literal172_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:248:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_in_expression_right_part1827);
                    subquery173=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery173.getTree());

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
    // JPA.g:250:1: in_item : ( literal | input_parameter );
    public final JPAParser.in_item_return in_item() throws RecognitionException {
        JPAParser.in_item_return retval = new JPAParser.in_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.literal_return literal174 = null;

        JPAParser.input_parameter_return input_parameter175 = null;



        try {
            // JPA.g:251:2: ( literal | input_parameter )
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==WORD) ) {
                alt57=1;
            }
            else if ( (LA57_0==NAMED_PARAMETER||(LA57_0>=115 && LA57_0<=116)) ) {
                alt57=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 57, 0, input);

                throw nvae;
            }
            switch (alt57) {
                case 1 :
                    // JPA.g:251:4: literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_literal_in_in_item1836);
                    literal174=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal174.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:252:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_in_item1841);
                    input_parameter175=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter175.getTree());

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
    // JPA.g:254:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' ESCAPE_CHARACTER )? ;
    public final JPAParser.like_expression_return like_expression() throws RecognitionException {
        JPAParser.like_expression_return retval = new JPAParser.like_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal177=null;
        Token string_literal178=null;
        Token string_literal181=null;
        Token ESCAPE_CHARACTER182=null;
        JPAParser.string_expression_return string_expression176 = null;

        JPAParser.pattern_value_return pattern_value179 = null;

        JPAParser.input_parameter_return input_parameter180 = null;


        Object string_literal177_tree=null;
        Object string_literal178_tree=null;
        Object string_literal181_tree=null;
        Object ESCAPE_CHARACTER182_tree=null;

        try {
            // JPA.g:255:2: ( string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' ESCAPE_CHARACTER )? )
            // JPA.g:255:4: string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' ESCAPE_CHARACTER )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_string_expression_in_like_expression1851);
            string_expression176=string_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression176.getTree());
            // JPA.g:255:22: ( 'NOT' )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==62) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // JPA.g:255:24: 'NOT'
                    {
                    string_literal177=(Token)match(input,62,FOLLOW_62_in_like_expression1855); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal177_tree = (Object)adaptor.create(string_literal177);
                    adaptor.addChild(root_0, string_literal177_tree);
                    }

                    }
                    break;

            }

            string_literal178=(Token)match(input,64,FOLLOW_64_in_like_expression1860); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal178_tree = (Object)adaptor.create(string_literal178);
            adaptor.addChild(root_0, string_literal178_tree);
            }
            // JPA.g:255:40: ( pattern_value | input_parameter )
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==WORD) ) {
                alt59=1;
            }
            else if ( (LA59_0==NAMED_PARAMETER||(LA59_0>=115 && LA59_0<=116)) ) {
                alt59=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 59, 0, input);

                throw nvae;
            }
            switch (alt59) {
                case 1 :
                    // JPA.g:255:42: pattern_value
                    {
                    pushFollow(FOLLOW_pattern_value_in_like_expression1864);
                    pattern_value179=pattern_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value179.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:255:58: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_like_expression1868);
                    input_parameter180=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter180.getTree());

                    }
                    break;

            }

            // JPA.g:255:76: ( 'ESCAPE' ESCAPE_CHARACTER )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==65) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // JPA.g:255:78: 'ESCAPE' ESCAPE_CHARACTER
                    {
                    string_literal181=(Token)match(input,65,FOLLOW_65_in_like_expression1874); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal181_tree = (Object)adaptor.create(string_literal181);
                    adaptor.addChild(root_0, string_literal181_tree);
                    }
                    ESCAPE_CHARACTER182=(Token)match(input,ESCAPE_CHARACTER,FOLLOW_ESCAPE_CHARACTER_in_like_expression1876); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ESCAPE_CHARACTER182_tree = (Object)adaptor.create(ESCAPE_CHARACTER182);
                    adaptor.addChild(root_0, ESCAPE_CHARACTER182_tree);
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
    // JPA.g:258:1: null_comparison_expression : ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' ;
    public final JPAParser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
        JPAParser.null_comparison_expression_return retval = new JPAParser.null_comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal185=null;
        Token string_literal186=null;
        Token string_literal187=null;
        JPAParser.path_expression_return path_expression183 = null;

        JPAParser.input_parameter_return input_parameter184 = null;


        Object string_literal185_tree=null;
        Object string_literal186_tree=null;
        Object string_literal187_tree=null;

        try {
            // JPA.g:259:2: ( ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' )
            // JPA.g:259:4: ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL'
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:259:4: ( path_expression | input_parameter )
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==WORD) ) {
                alt61=1;
            }
            else if ( (LA61_0==NAMED_PARAMETER||(LA61_0>=115 && LA61_0<=116)) ) {
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
                    // JPA.g:259:5: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_null_comparison_expression1892);
                    path_expression183=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression183.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:259:23: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_null_comparison_expression1896);
                    input_parameter184=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter184.getTree());

                    }
                    break;

            }

            string_literal185=(Token)match(input,66,FOLLOW_66_in_null_comparison_expression1899); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal185_tree = (Object)adaptor.create(string_literal185);
            adaptor.addChild(root_0, string_literal185_tree);
            }
            // JPA.g:259:45: ( 'NOT' )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==62) ) {
                alt62=1;
            }
            switch (alt62) {
                case 1 :
                    // JPA.g:259:46: 'NOT'
                    {
                    string_literal186=(Token)match(input,62,FOLLOW_62_in_null_comparison_expression1902); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal186_tree = (Object)adaptor.create(string_literal186);
                    adaptor.addChild(root_0, string_literal186_tree);
                    }

                    }
                    break;

            }

            string_literal187=(Token)match(input,67,FOLLOW_67_in_null_comparison_expression1906); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal187_tree = (Object)adaptor.create(string_literal187);
            adaptor.addChild(root_0, string_literal187_tree);
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
    // JPA.g:261:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
    public final JPAParser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
        JPAParser.empty_collection_comparison_expression_return retval = new JPAParser.empty_collection_comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal189=null;
        Token string_literal190=null;
        Token string_literal191=null;
        JPAParser.path_expression_return path_expression188 = null;


        Object string_literal189_tree=null;
        Object string_literal190_tree=null;
        Object string_literal191_tree=null;

        try {
            // JPA.g:262:2: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
            // JPA.g:262:4: path_expression 'IS' ( 'NOT' )? 'EMPTY'
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression1915);
            path_expression188=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression188.getTree());
            string_literal189=(Token)match(input,66,FOLLOW_66_in_empty_collection_comparison_expression1917); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal189_tree = (Object)adaptor.create(string_literal189);
            adaptor.addChild(root_0, string_literal189_tree);
            }
            // JPA.g:262:25: ( 'NOT' )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==62) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // JPA.g:262:26: 'NOT'
                    {
                    string_literal190=(Token)match(input,62,FOLLOW_62_in_empty_collection_comparison_expression1920); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal190_tree = (Object)adaptor.create(string_literal190);
                    adaptor.addChild(root_0, string_literal190_tree);
                    }

                    }
                    break;

            }

            string_literal191=(Token)match(input,68,FOLLOW_68_in_empty_collection_comparison_expression1924); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal191_tree = (Object)adaptor.create(string_literal191);
            adaptor.addChild(root_0, string_literal191_tree);
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
    // JPA.g:264:1: collection_member_expression : entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
    public final JPAParser.collection_member_expression_return collection_member_expression() throws RecognitionException {
        JPAParser.collection_member_expression_return retval = new JPAParser.collection_member_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal193=null;
        Token string_literal194=null;
        Token string_literal195=null;
        JPAParser.entity_expression_return entity_expression192 = null;

        JPAParser.path_expression_return path_expression196 = null;


        Object string_literal193_tree=null;
        Object string_literal194_tree=null;
        Object string_literal195_tree=null;

        try {
            // JPA.g:265:2: ( entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
            // JPA.g:265:4: entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_entity_expression_in_collection_member_expression1933);
            entity_expression192=entity_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression192.getTree());
            // JPA.g:265:22: ( 'NOT' )?
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==62) ) {
                alt64=1;
            }
            switch (alt64) {
                case 1 :
                    // JPA.g:265:23: 'NOT'
                    {
                    string_literal193=(Token)match(input,62,FOLLOW_62_in_collection_member_expression1936); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal193_tree = (Object)adaptor.create(string_literal193);
                    adaptor.addChild(root_0, string_literal193_tree);
                    }

                    }
                    break;

            }

            string_literal194=(Token)match(input,69,FOLLOW_69_in_collection_member_expression1940); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal194_tree = (Object)adaptor.create(string_literal194);
            adaptor.addChild(root_0, string_literal194_tree);
            }
            // JPA.g:265:40: ( 'OF' )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==70) ) {
                alt65=1;
            }
            switch (alt65) {
                case 1 :
                    // JPA.g:265:41: 'OF'
                    {
                    string_literal195=(Token)match(input,70,FOLLOW_70_in_collection_member_expression1943); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal195_tree = (Object)adaptor.create(string_literal195);
                    adaptor.addChild(root_0, string_literal195_tree);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_path_expression_in_collection_member_expression1947);
            path_expression196=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression196.getTree());

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
    // JPA.g:267:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
    public final JPAParser.exists_expression_return exists_expression() throws RecognitionException {
        JPAParser.exists_expression_return retval = new JPAParser.exists_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal197=null;
        Token string_literal198=null;
        JPAParser.subquery_return subquery199 = null;


        Object string_literal197_tree=null;
        Object string_literal198_tree=null;

        try {
            // JPA.g:268:2: ( ( 'NOT' )? 'EXISTS' subquery )
            // JPA.g:268:4: ( 'NOT' )? 'EXISTS' subquery
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:268:4: ( 'NOT' )?
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==62) ) {
                alt66=1;
            }
            switch (alt66) {
                case 1 :
                    // JPA.g:268:5: 'NOT'
                    {
                    string_literal197=(Token)match(input,62,FOLLOW_62_in_exists_expression1957); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal197_tree = (Object)adaptor.create(string_literal197);
                    adaptor.addChild(root_0, string_literal197_tree);
                    }

                    }
                    break;

            }

            string_literal198=(Token)match(input,71,FOLLOW_71_in_exists_expression1961); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal198_tree = (Object)adaptor.create(string_literal198);
            adaptor.addChild(root_0, string_literal198_tree);
            }
            pushFollow(FOLLOW_subquery_in_exists_expression1963);
            subquery199=subquery();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery199.getTree());

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
    // JPA.g:270:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
    public final JPAParser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
        JPAParser.all_or_any_expression_return retval = new JPAParser.all_or_any_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set200=null;
        JPAParser.subquery_return subquery201 = null;


        Object set200_tree=null;

        try {
            // JPA.g:271:2: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
            // JPA.g:271:4: ( 'ALL' | 'ANY' | 'SOME' ) subquery
            {
            root_0 = (Object)adaptor.nil();

            set200=(Token)input.LT(1);
            if ( (input.LA(1)>=72 && input.LA(1)<=74) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set200));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            pushFollow(FOLLOW_subquery_in_all_or_any_expression1985);
            subquery201=subquery();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery201.getTree());

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
    // JPA.g:273:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
    public final JPAParser.comparison_expression_return comparison_expression() throws RecognitionException {
        JPAParser.comparison_expression_return retval = new JPAParser.comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set207=null;
        Token set211=null;
        Token set219=null;
        JPAParser.string_expression_return string_expression202 = null;

        JPAParser.comparison_operator_return comparison_operator203 = null;

        JPAParser.string_expression_return string_expression204 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression205 = null;

        JPAParser.boolean_expression_return boolean_expression206 = null;

        JPAParser.boolean_expression_return boolean_expression208 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression209 = null;

        JPAParser.enum_expression_return enum_expression210 = null;

        JPAParser.enum_expression_return enum_expression212 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression213 = null;

        JPAParser.datetime_expression_return datetime_expression214 = null;

        JPAParser.comparison_operator_return comparison_operator215 = null;

        JPAParser.datetime_expression_return datetime_expression216 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression217 = null;

        JPAParser.entity_expression_return entity_expression218 = null;

        JPAParser.entity_expression_return entity_expression220 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression221 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression222 = null;

        JPAParser.comparison_operator_return comparison_operator223 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression224 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression225 = null;


        Object set207_tree=null;
        Object set211_tree=null;
        Object set219_tree=null;

        try {
            // JPA.g:274:2: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
            int alt73=6;
            alt73 = dfa73.predict(input);
            switch (alt73) {
                case 1 :
                    // JPA.g:274:4: string_expression comparison_operator ( string_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_expression_in_comparison_expression1994);
                    string_expression202=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression202.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression1996);
                    comparison_operator203=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator203.getTree());
                    // JPA.g:274:42: ( string_expression | all_or_any_expression )
                    int alt67=2;
                    int LA67_0 = input.LA(1);

                    if ( ((LA67_0>=AVG && LA67_0<=COUNT)||LA67_0==STRINGLITERAL||(LA67_0>=WORD && LA67_0<=NAMED_PARAMETER)||LA67_0==56||(LA67_0>=106 && LA67_0<=110)||(LA67_0>=115 && LA67_0<=116)) ) {
                        alt67=1;
                    }
                    else if ( ((LA67_0>=72 && LA67_0<=74)) ) {
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
                            // JPA.g:274:43: string_expression
                            {
                            pushFollow(FOLLOW_string_expression_in_comparison_expression1999);
                            string_expression204=string_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression204.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:274:63: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2003);
                            all_or_any_expression205=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression205.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // JPA.g:275:4: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_expression_in_comparison_expression2009);
                    boolean_expression206=boolean_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression206.getTree());
                    set207=(Token)input.LT(1);
                    if ( (input.LA(1)>=75 && input.LA(1)<=76) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set207));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:275:36: ( boolean_expression | all_or_any_expression )
                    int alt68=2;
                    int LA68_0 = input.LA(1);

                    if ( ((LA68_0>=WORD && LA68_0<=NAMED_PARAMETER)||LA68_0==56||(LA68_0>=115 && LA68_0<=116)||(LA68_0>=118 && LA68_0<=119)) ) {
                        alt68=1;
                    }
                    else if ( ((LA68_0>=72 && LA68_0<=74)) ) {
                        alt68=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 68, 0, input);

                        throw nvae;
                    }
                    switch (alt68) {
                        case 1 :
                            // JPA.g:275:37: boolean_expression
                            {
                            pushFollow(FOLLOW_boolean_expression_in_comparison_expression2020);
                            boolean_expression208=boolean_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression208.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:275:58: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2024);
                            all_or_any_expression209=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression209.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // JPA.g:276:4: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_expression_in_comparison_expression2030);
                    enum_expression210=enum_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression210.getTree());
                    set211=(Token)input.LT(1);
                    if ( (input.LA(1)>=75 && input.LA(1)<=76) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set211));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:276:31: ( enum_expression | all_or_any_expression )
                    int alt69=2;
                    int LA69_0 = input.LA(1);

                    if ( ((LA69_0>=WORD && LA69_0<=NAMED_PARAMETER)||LA69_0==56||(LA69_0>=115 && LA69_0<=116)) ) {
                        alt69=1;
                    }
                    else if ( ((LA69_0>=72 && LA69_0<=74)) ) {
                        alt69=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 69, 0, input);

                        throw nvae;
                    }
                    switch (alt69) {
                        case 1 :
                            // JPA.g:276:32: enum_expression
                            {
                            pushFollow(FOLLOW_enum_expression_in_comparison_expression2039);
                            enum_expression212=enum_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression212.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:276:50: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2043);
                            all_or_any_expression213=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression213.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // JPA.g:277:4: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_expression_in_comparison_expression2049);
                    datetime_expression214=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression214.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2051);
                    comparison_operator215=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator215.getTree());
                    // JPA.g:277:44: ( datetime_expression | all_or_any_expression )
                    int alt70=2;
                    int LA70_0 = input.LA(1);

                    if ( ((LA70_0>=AVG && LA70_0<=COUNT)||(LA70_0>=WORD && LA70_0<=NAMED_PARAMETER)||LA70_0==56||(LA70_0>=103 && LA70_0<=105)||(LA70_0>=115 && LA70_0<=116)) ) {
                        alt70=1;
                    }
                    else if ( ((LA70_0>=72 && LA70_0<=74)) ) {
                        alt70=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 70, 0, input);

                        throw nvae;
                    }
                    switch (alt70) {
                        case 1 :
                            // JPA.g:277:45: datetime_expression
                            {
                            pushFollow(FOLLOW_datetime_expression_in_comparison_expression2054);
                            datetime_expression216=datetime_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression216.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:277:67: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2058);
                            all_or_any_expression217=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression217.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // JPA.g:278:4: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_entity_expression_in_comparison_expression2064);
                    entity_expression218=entity_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression218.getTree());
                    set219=(Token)input.LT(1);
                    if ( (input.LA(1)>=75 && input.LA(1)<=76) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set219));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:278:35: ( entity_expression | all_or_any_expression )
                    int alt71=2;
                    int LA71_0 = input.LA(1);

                    if ( ((LA71_0>=WORD && LA71_0<=NAMED_PARAMETER)||(LA71_0>=115 && LA71_0<=116)) ) {
                        alt71=1;
                    }
                    else if ( ((LA71_0>=72 && LA71_0<=74)) ) {
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
                            // JPA.g:278:36: entity_expression
                            {
                            pushFollow(FOLLOW_entity_expression_in_comparison_expression2075);
                            entity_expression220=entity_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression220.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:278:56: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2079);
                            all_or_any_expression221=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression221.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 6 :
                    // JPA.g:279:4: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression2085);
                    arithmetic_expression222=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression222.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression2087);
                    comparison_operator223=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator223.getTree());
                    // JPA.g:279:46: ( arithmetic_expression | all_or_any_expression )
                    int alt72=2;
                    int LA72_0 = input.LA(1);

                    if ( ((LA72_0>=AVG && LA72_0<=COUNT)||LA72_0==LPAREN||LA72_0==INT_NUMERAL||(LA72_0>=WORD && LA72_0<=NAMED_PARAMETER)||LA72_0==56||(LA72_0>=81 && LA72_0<=82)||(LA72_0>=97 && LA72_0<=102)||(LA72_0>=114 && LA72_0<=116)) ) {
                        alt72=1;
                    }
                    else if ( ((LA72_0>=72 && LA72_0<=74)) ) {
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
                            // JPA.g:279:47: arithmetic_expression
                            {
                            pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression2090);
                            arithmetic_expression224=arithmetic_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression224.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:279:71: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2094);
                            all_or_any_expression225=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression225.getTree());

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
    // JPA.g:281:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
    public final JPAParser.comparison_operator_return comparison_operator() throws RecognitionException {
        JPAParser.comparison_operator_return retval = new JPAParser.comparison_operator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set226=null;

        Object set226_tree=null;

        try {
            // JPA.g:282:2: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set226=(Token)input.LT(1);
            if ( (input.LA(1)>=75 && input.LA(1)<=80) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set226));
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
    // JPA.g:289:1: arithmetic_expression : ( simple_arithmetic_expression | subquery );
    public final JPAParser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
        JPAParser.arithmetic_expression_return retval = new JPAParser.arithmetic_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression227 = null;

        JPAParser.subquery_return subquery228 = null;



        try {
            // JPA.g:290:2: ( simple_arithmetic_expression | subquery )
            int alt74=2;
            int LA74_0 = input.LA(1);

            if ( ((LA74_0>=AVG && LA74_0<=COUNT)||LA74_0==LPAREN||LA74_0==INT_NUMERAL||(LA74_0>=WORD && LA74_0<=NAMED_PARAMETER)||(LA74_0>=81 && LA74_0<=82)||(LA74_0>=97 && LA74_0<=102)||(LA74_0>=114 && LA74_0<=116)) ) {
                alt74=1;
            }
            else if ( (LA74_0==56) ) {
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
                    // JPA.g:290:4: simple_arithmetic_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simple_arithmetic_expression_in_arithmetic_expression2138);
                    simple_arithmetic_expression227=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression227.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:291:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_arithmetic_expression2143);
                    subquery228=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery228.getTree());

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
    // JPA.g:293:1: simple_arithmetic_expression : ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )* ;
    public final JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression() throws RecognitionException {
        JPAParser.simple_arithmetic_expression_return retval = new JPAParser.simple_arithmetic_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set230=null;
        JPAParser.arithmetic_term_return arithmetic_term229 = null;

        JPAParser.arithmetic_term_return arithmetic_term231 = null;


        Object set230_tree=null;

        try {
            // JPA.g:294:2: ( ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )* )
            // JPA.g:294:4: ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:294:4: ( arithmetic_term )
            // JPA.g:294:5: arithmetic_term
            {
            pushFollow(FOLLOW_arithmetic_term_in_simple_arithmetic_expression2153);
            arithmetic_term229=arithmetic_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term229.getTree());

            }

            // JPA.g:294:22: ( ( '+' | '-' ) arithmetic_term )*
            loop75:
            do {
                int alt75=2;
                int LA75_0 = input.LA(1);

                if ( ((LA75_0>=81 && LA75_0<=82)) ) {
                    alt75=1;
                }


                switch (alt75) {
            	case 1 :
            	    // JPA.g:294:23: ( '+' | '-' ) arithmetic_term
            	    {
            	    set230=(Token)input.LT(1);
            	    if ( (input.LA(1)>=81 && input.LA(1)<=82) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set230));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmetic_term_in_simple_arithmetic_expression2167);
            	    arithmetic_term231=arithmetic_term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term231.getTree());

            	    }
            	    break;

            	default :
            	    break loop75;
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
    // JPA.g:296:1: arithmetic_term : ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )* ;
    public final JPAParser.arithmetic_term_return arithmetic_term() throws RecognitionException {
        JPAParser.arithmetic_term_return retval = new JPAParser.arithmetic_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set233=null;
        JPAParser.arithmetic_factor_return arithmetic_factor232 = null;

        JPAParser.arithmetic_factor_return arithmetic_factor234 = null;


        Object set233_tree=null;

        try {
            // JPA.g:297:2: ( ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )* )
            // JPA.g:297:4: ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:297:4: ( arithmetic_factor )
            // JPA.g:297:5: arithmetic_factor
            {
            pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term2179);
            arithmetic_factor232=arithmetic_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor232.getTree());

            }

            // JPA.g:297:24: ( ( '*' | '/' ) arithmetic_factor )*
            loop76:
            do {
                int alt76=2;
                int LA76_0 = input.LA(1);

                if ( ((LA76_0>=83 && LA76_0<=84)) ) {
                    alt76=1;
                }


                switch (alt76) {
            	case 1 :
            	    // JPA.g:297:25: ( '*' | '/' ) arithmetic_factor
            	    {
            	    set233=(Token)input.LT(1);
            	    if ( (input.LA(1)>=83 && input.LA(1)<=84) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set233));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term2193);
            	    arithmetic_factor234=arithmetic_factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor234.getTree());

            	    }
            	    break;

            	default :
            	    break loop76;
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
    // JPA.g:299:1: arithmetic_factor : ( '+' | '-' )? arithmetic_primary ;
    public final JPAParser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
        JPAParser.arithmetic_factor_return retval = new JPAParser.arithmetic_factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set235=null;
        JPAParser.arithmetic_primary_return arithmetic_primary236 = null;


        Object set235_tree=null;

        try {
            // JPA.g:300:2: ( ( '+' | '-' )? arithmetic_primary )
            // JPA.g:300:4: ( '+' | '-' )? arithmetic_primary
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:300:4: ( '+' | '-' )?
            int alt77=2;
            int LA77_0 = input.LA(1);

            if ( ((LA77_0>=81 && LA77_0<=82)) ) {
                alt77=1;
            }
            switch (alt77) {
                case 1 :
                    // JPA.g:
                    {
                    set235=(Token)input.LT(1);
                    if ( (input.LA(1)>=81 && input.LA(1)<=82) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set235));
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

            pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor2215);
            arithmetic_primary236=arithmetic_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary236.getTree());

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
    // JPA.g:302:1: arithmetic_primary : ( path_expression | numeric_literal | '(' simple_arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression );
    public final JPAParser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
        JPAParser.arithmetic_primary_return retval = new JPAParser.arithmetic_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal239=null;
        Token char_literal241=null;
        JPAParser.path_expression_return path_expression237 = null;

        JPAParser.numeric_literal_return numeric_literal238 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression240 = null;

        JPAParser.input_parameter_return input_parameter242 = null;

        JPAParser.functions_returning_numerics_return functions_returning_numerics243 = null;

        JPAParser.aggregate_expression_return aggregate_expression244 = null;


        Object char_literal239_tree=null;
        Object char_literal241_tree=null;

        try {
            // JPA.g:303:2: ( path_expression | numeric_literal | '(' simple_arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression )
            int alt78=6;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt78=1;
                }
                break;
            case INT_NUMERAL:
            case 114:
                {
                alt78=2;
                }
                break;
            case LPAREN:
                {
                alt78=3;
                }
                break;
            case NAMED_PARAMETER:
            case 115:
            case 116:
                {
                alt78=4;
                }
                break;
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
                {
                alt78=5;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt78=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 78, 0, input);

                throw nvae;
            }

            switch (alt78) {
                case 1 :
                    // JPA.g:303:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_arithmetic_primary2224);
                    path_expression237=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression237.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:304:4: numeric_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary2229);
                    numeric_literal238=numeric_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal238.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:305:4: '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal239=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary2234); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal239_tree = (Object)adaptor.create(char_literal239);
                    adaptor.addChild(root_0, char_literal239_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_arithmetic_primary2235);
                    simple_arithmetic_expression240=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression240.getTree());
                    char_literal241=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary2236); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal241_tree = (Object)adaptor.create(char_literal241);
                    adaptor.addChild(root_0, char_literal241_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:306:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_arithmetic_primary2241);
                    input_parameter242=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter242.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:307:4: functions_returning_numerics
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary2246);
                    functions_returning_numerics243=functions_returning_numerics();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics243.getTree());

                    }
                    break;
                case 6 :
                    // JPA.g:308:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary2251);
                    aggregate_expression244=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression244.getTree());

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
    // JPA.g:310:1: string_expression : ( string_primary | subquery );
    public final JPAParser.string_expression_return string_expression() throws RecognitionException {
        JPAParser.string_expression_return retval = new JPAParser.string_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.string_primary_return string_primary245 = null;

        JPAParser.subquery_return subquery246 = null;



        try {
            // JPA.g:311:2: ( string_primary | subquery )
            int alt79=2;
            int LA79_0 = input.LA(1);

            if ( ((LA79_0>=AVG && LA79_0<=COUNT)||LA79_0==STRINGLITERAL||(LA79_0>=WORD && LA79_0<=NAMED_PARAMETER)||(LA79_0>=106 && LA79_0<=110)||(LA79_0>=115 && LA79_0<=116)) ) {
                alt79=1;
            }
            else if ( (LA79_0==56) ) {
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
                    // JPA.g:311:4: string_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_primary_in_string_expression2260);
                    string_primary245=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary245.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:311:21: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_string_expression2264);
                    subquery246=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery246.getTree());

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
    // JPA.g:313:1: string_primary : ( path_expression | STRINGLITERAL | input_parameter | functions_returning_strings | aggregate_expression );
    public final JPAParser.string_primary_return string_primary() throws RecognitionException {
        JPAParser.string_primary_return retval = new JPAParser.string_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRINGLITERAL248=null;
        JPAParser.path_expression_return path_expression247 = null;

        JPAParser.input_parameter_return input_parameter249 = null;

        JPAParser.functions_returning_strings_return functions_returning_strings250 = null;

        JPAParser.aggregate_expression_return aggregate_expression251 = null;


        Object STRINGLITERAL248_tree=null;

        try {
            // JPA.g:314:2: ( path_expression | STRINGLITERAL | input_parameter | functions_returning_strings | aggregate_expression )
            int alt80=5;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt80=1;
                }
                break;
            case STRINGLITERAL:
                {
                alt80=2;
                }
                break;
            case NAMED_PARAMETER:
            case 115:
            case 116:
                {
                alt80=3;
                }
                break;
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
                {
                alt80=4;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt80=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 80, 0, input);

                throw nvae;
            }

            switch (alt80) {
                case 1 :
                    // JPA.g:314:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_string_primary2273);
                    path_expression247=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression247.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:315:4: STRINGLITERAL
                    {
                    root_0 = (Object)adaptor.nil();

                    STRINGLITERAL248=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_string_primary2278); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRINGLITERAL248_tree = (Object)adaptor.create(STRINGLITERAL248);
                    adaptor.addChild(root_0, STRINGLITERAL248_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:316:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_string_primary2283);
                    input_parameter249=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter249.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:317:4: functions_returning_strings
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_strings_in_string_primary2288);
                    functions_returning_strings250=functions_returning_strings();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings250.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:318:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_string_primary2293);
                    aggregate_expression251=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression251.getTree());

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
    // JPA.g:320:1: datetime_expression : ( datetime_primary | subquery );
    public final JPAParser.datetime_expression_return datetime_expression() throws RecognitionException {
        JPAParser.datetime_expression_return retval = new JPAParser.datetime_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.datetime_primary_return datetime_primary252 = null;

        JPAParser.subquery_return subquery253 = null;



        try {
            // JPA.g:321:2: ( datetime_primary | subquery )
            int alt81=2;
            int LA81_0 = input.LA(1);

            if ( ((LA81_0>=AVG && LA81_0<=COUNT)||(LA81_0>=WORD && LA81_0<=NAMED_PARAMETER)||(LA81_0>=103 && LA81_0<=105)||(LA81_0>=115 && LA81_0<=116)) ) {
                alt81=1;
            }
            else if ( (LA81_0==56) ) {
                alt81=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 81, 0, input);

                throw nvae;
            }
            switch (alt81) {
                case 1 :
                    // JPA.g:321:4: datetime_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_primary_in_datetime_expression2302);
                    datetime_primary252=datetime_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_primary252.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:322:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_datetime_expression2307);
                    subquery253=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery253.getTree());

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
    // JPA.g:324:1: datetime_primary : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression );
    public final JPAParser.datetime_primary_return datetime_primary() throws RecognitionException {
        JPAParser.datetime_primary_return retval = new JPAParser.datetime_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression254 = null;

        JPAParser.input_parameter_return input_parameter255 = null;

        JPAParser.functions_returning_datetime_return functions_returning_datetime256 = null;

        JPAParser.aggregate_expression_return aggregate_expression257 = null;



        try {
            // JPA.g:325:2: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression )
            int alt82=4;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt82=1;
                }
                break;
            case NAMED_PARAMETER:
            case 115:
            case 116:
                {
                alt82=2;
                }
                break;
            case 103:
            case 104:
            case 105:
                {
                alt82=3;
                }
                break;
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case COUNT:
                {
                alt82=4;
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
                    // JPA.g:325:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_datetime_primary2316);
                    path_expression254=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression254.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:326:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_datetime_primary2321);
                    input_parameter255=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter255.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:327:4: functions_returning_datetime
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_datetime_in_datetime_primary2326);
                    functions_returning_datetime256=functions_returning_datetime();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime256.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:328:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_datetime_primary2331);
                    aggregate_expression257=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression257.getTree());

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

    public static class date_macro_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_macro_expression"
    // JPA.g:330:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression ) ;
    public final JPAParser.date_macro_expression_return date_macro_expression() throws RecognitionException {
        JPAParser.date_macro_expression_return retval = new JPAParser.date_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.date_between_macro_expression_return date_between_macro_expression258 = null;

        JPAParser.date_before_macro_expression_return date_before_macro_expression259 = null;

        JPAParser.date_after_macro_expression_return date_after_macro_expression260 = null;

        JPAParser.date_equals_macro_expression_return date_equals_macro_expression261 = null;

        JPAParser.date_today_macro_expression_return date_today_macro_expression262 = null;



        try {
            // JPA.g:331:2: ( ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression ) )
            // JPA.g:331:4: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:331:4: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
            int alt83=5;
            switch ( input.LA(1) ) {
            case 85:
                {
                alt83=1;
                }
                break;
            case 93:
                {
                alt83=2;
                }
                break;
            case 94:
                {
                alt83=3;
                }
                break;
            case 95:
                {
                alt83=4;
                }
                break;
            case 96:
                {
                alt83=5;
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
                    // JPA.g:331:6: date_between_macro_expression
                    {
                    pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2343);
                    date_between_macro_expression258=date_between_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression258.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:332:4: date_before_macro_expression
                    {
                    pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2349);
                    date_before_macro_expression259=date_before_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression259.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:333:4: date_after_macro_expression
                    {
                    pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2355);
                    date_after_macro_expression260=date_after_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression260.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:334:4: date_equals_macro_expression
                    {
                    pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2361);
                    date_equals_macro_expression261=date_equals_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression261.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:335:4: date_today_macro_expression
                    {
                    pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2367);
                    date_today_macro_expression262=date_today_macro_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, date_today_macro_expression262.getTree());

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
    // $ANTLR end "date_macro_expression"

    public static class date_between_macro_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_between_macro_expression"
    // JPA.g:338:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
    public final JPAParser.date_between_macro_expression_return date_between_macro_expression() throws RecognitionException {
        JPAParser.date_between_macro_expression_return retval = new JPAParser.date_between_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal263=null;
        Token char_literal264=null;
        Token char_literal266=null;
        Token string_literal267=null;
        Token set268=null;
        Token INT_NUMERAL269=null;
        Token char_literal270=null;
        Token string_literal271=null;
        Token set272=null;
        Token INT_NUMERAL273=null;
        Token char_literal274=null;
        Token set275=null;
        Token char_literal276=null;
        JPAParser.path_expression_return path_expression265 = null;


        Object string_literal263_tree=null;
        Object char_literal264_tree=null;
        Object char_literal266_tree=null;
        Object string_literal267_tree=null;
        Object set268_tree=null;
        Object INT_NUMERAL269_tree=null;
        Object char_literal270_tree=null;
        Object string_literal271_tree=null;
        Object set272_tree=null;
        Object INT_NUMERAL273_tree=null;
        Object char_literal274_tree=null;
        Object set275_tree=null;
        Object char_literal276_tree=null;

        try {
            // JPA.g:339:2: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
            // JPA.g:339:4: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' 'NOW' ( ( '+' | '-' ) INT_NUMERAL )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal263=(Token)match(input,85,FOLLOW_85_in_date_between_macro_expression2381); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal263_tree = (Object)adaptor.create(string_literal263);
            adaptor.addChild(root_0, string_literal263_tree);
            }
            char_literal264=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2383); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal264_tree = (Object)adaptor.create(char_literal264);
            adaptor.addChild(root_0, char_literal264_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2385);
            path_expression265=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression265.getTree());
            char_literal266=(Token)match(input,54,FOLLOW_54_in_date_between_macro_expression2387); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal266_tree = (Object)adaptor.create(char_literal266);
            adaptor.addChild(root_0, char_literal266_tree);
            }
            string_literal267=(Token)match(input,86,FOLLOW_86_in_date_between_macro_expression2389); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal267_tree = (Object)adaptor.create(string_literal267);
            adaptor.addChild(root_0, string_literal267_tree);
            }
            // JPA.g:339:45: ( ( '+' | '-' ) INT_NUMERAL )?
            int alt84=2;
            int LA84_0 = input.LA(1);

            if ( ((LA84_0>=81 && LA84_0<=82)) ) {
                alt84=1;
            }
            switch (alt84) {
                case 1 :
                    // JPA.g:339:47: ( '+' | '-' ) INT_NUMERAL
                    {
                    set268=(Token)input.LT(1);
                    if ( (input.LA(1)>=81 && input.LA(1)<=82) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set268));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    INT_NUMERAL269=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_date_between_macro_expression2403); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_NUMERAL269_tree = (Object)adaptor.create(INT_NUMERAL269);
                    adaptor.addChild(root_0, INT_NUMERAL269_tree);
                    }

                    }
                    break;

            }

            char_literal270=(Token)match(input,54,FOLLOW_54_in_date_between_macro_expression2408); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal270_tree = (Object)adaptor.create(char_literal270);
            adaptor.addChild(root_0, char_literal270_tree);
            }
            string_literal271=(Token)match(input,86,FOLLOW_86_in_date_between_macro_expression2410); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal271_tree = (Object)adaptor.create(string_literal271);
            adaptor.addChild(root_0, string_literal271_tree);
            }
            // JPA.g:339:86: ( ( '+' | '-' ) INT_NUMERAL )?
            int alt85=2;
            int LA85_0 = input.LA(1);

            if ( ((LA85_0>=81 && LA85_0<=82)) ) {
                alt85=1;
            }
            switch (alt85) {
                case 1 :
                    // JPA.g:339:88: ( '+' | '-' ) INT_NUMERAL
                    {
                    set272=(Token)input.LT(1);
                    if ( (input.LA(1)>=81 && input.LA(1)<=82) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set272));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    INT_NUMERAL273=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_date_between_macro_expression2424); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_NUMERAL273_tree = (Object)adaptor.create(INT_NUMERAL273);
                    adaptor.addChild(root_0, INT_NUMERAL273_tree);
                    }

                    }
                    break;

            }

            char_literal274=(Token)match(input,54,FOLLOW_54_in_date_between_macro_expression2429); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal274_tree = (Object)adaptor.create(char_literal274);
            adaptor.addChild(root_0, char_literal274_tree);
            }
            set275=(Token)input.LT(1);
            if ( (input.LA(1)>=87 && input.LA(1)<=92) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set275));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            char_literal276=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2457); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal276_tree = (Object)adaptor.create(char_literal276);
            adaptor.addChild(root_0, char_literal276_tree);
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
    // JPA.g:341:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
    public final JPAParser.date_before_macro_expression_return date_before_macro_expression() throws RecognitionException {
        JPAParser.date_before_macro_expression_return retval = new JPAParser.date_before_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal277=null;
        Token char_literal278=null;
        Token char_literal280=null;
        Token char_literal283=null;
        JPAParser.path_expression_return path_expression279 = null;

        JPAParser.path_expression_return path_expression281 = null;

        JPAParser.input_parameter_return input_parameter282 = null;


        Object string_literal277_tree=null;
        Object char_literal278_tree=null;
        Object char_literal280_tree=null;
        Object char_literal283_tree=null;

        try {
            // JPA.g:342:2: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:342:4: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal277=(Token)match(input,93,FOLLOW_93_in_date_before_macro_expression2468); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal277_tree = (Object)adaptor.create(string_literal277);
            adaptor.addChild(root_0, string_literal277_tree);
            }
            char_literal278=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2470); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal278_tree = (Object)adaptor.create(char_literal278);
            adaptor.addChild(root_0, char_literal278_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2472);
            path_expression279=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression279.getTree());
            char_literal280=(Token)match(input,54,FOLLOW_54_in_date_before_macro_expression2474); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal280_tree = (Object)adaptor.create(char_literal280);
            adaptor.addChild(root_0, char_literal280_tree);
            }
            // JPA.g:342:42: ( path_expression | input_parameter )
            int alt86=2;
            int LA86_0 = input.LA(1);

            if ( (LA86_0==WORD) ) {
                alt86=1;
            }
            else if ( (LA86_0==NAMED_PARAMETER||(LA86_0>=115 && LA86_0<=116)) ) {
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
                    // JPA.g:342:44: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2478);
                    path_expression281=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression281.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:342:62: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2482);
                    input_parameter282=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter282.getTree());

                    }
                    break;

            }

            char_literal283=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2486); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal283_tree = (Object)adaptor.create(char_literal283);
            adaptor.addChild(root_0, char_literal283_tree);
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
    // JPA.g:345:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
    public final JPAParser.date_after_macro_expression_return date_after_macro_expression() throws RecognitionException {
        JPAParser.date_after_macro_expression_return retval = new JPAParser.date_after_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal284=null;
        Token char_literal285=null;
        Token char_literal287=null;
        Token char_literal290=null;
        JPAParser.path_expression_return path_expression286 = null;

        JPAParser.path_expression_return path_expression288 = null;

        JPAParser.input_parameter_return input_parameter289 = null;


        Object string_literal284_tree=null;
        Object char_literal285_tree=null;
        Object char_literal287_tree=null;
        Object char_literal290_tree=null;

        try {
            // JPA.g:346:2: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:346:4: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal284=(Token)match(input,94,FOLLOW_94_in_date_after_macro_expression2499); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal284_tree = (Object)adaptor.create(string_literal284);
            adaptor.addChild(root_0, string_literal284_tree);
            }
            char_literal285=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2501); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal285_tree = (Object)adaptor.create(char_literal285);
            adaptor.addChild(root_0, char_literal285_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2503);
            path_expression286=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression286.getTree());
            char_literal287=(Token)match(input,54,FOLLOW_54_in_date_after_macro_expression2505); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal287_tree = (Object)adaptor.create(char_literal287);
            adaptor.addChild(root_0, char_literal287_tree);
            }
            // JPA.g:346:41: ( path_expression | input_parameter )
            int alt87=2;
            int LA87_0 = input.LA(1);

            if ( (LA87_0==WORD) ) {
                alt87=1;
            }
            else if ( (LA87_0==NAMED_PARAMETER||(LA87_0>=115 && LA87_0<=116)) ) {
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
                    // JPA.g:346:43: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2509);
                    path_expression288=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression288.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:346:61: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2513);
                    input_parameter289=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter289.getTree());

                    }
                    break;

            }

            char_literal290=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2517); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal290_tree = (Object)adaptor.create(char_literal290);
            adaptor.addChild(root_0, char_literal290_tree);
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
    // JPA.g:349:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
    public final JPAParser.date_equals_macro_expression_return date_equals_macro_expression() throws RecognitionException {
        JPAParser.date_equals_macro_expression_return retval = new JPAParser.date_equals_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal291=null;
        Token char_literal292=null;
        Token char_literal294=null;
        Token char_literal297=null;
        JPAParser.path_expression_return path_expression293 = null;

        JPAParser.path_expression_return path_expression295 = null;

        JPAParser.input_parameter_return input_parameter296 = null;


        Object string_literal291_tree=null;
        Object char_literal292_tree=null;
        Object char_literal294_tree=null;
        Object char_literal297_tree=null;

        try {
            // JPA.g:350:2: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
            // JPA.g:350:4: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal291=(Token)match(input,95,FOLLOW_95_in_date_equals_macro_expression2530); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal291_tree = (Object)adaptor.create(string_literal291);
            adaptor.addChild(root_0, string_literal291_tree);
            }
            char_literal292=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2532); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal292_tree = (Object)adaptor.create(char_literal292);
            adaptor.addChild(root_0, char_literal292_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2534);
            path_expression293=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression293.getTree());
            char_literal294=(Token)match(input,54,FOLLOW_54_in_date_equals_macro_expression2536); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal294_tree = (Object)adaptor.create(char_literal294);
            adaptor.addChild(root_0, char_literal294_tree);
            }
            // JPA.g:350:42: ( path_expression | input_parameter )
            int alt88=2;
            int LA88_0 = input.LA(1);

            if ( (LA88_0==WORD) ) {
                alt88=1;
            }
            else if ( (LA88_0==NAMED_PARAMETER||(LA88_0>=115 && LA88_0<=116)) ) {
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
                    // JPA.g:350:44: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2540);
                    path_expression295=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression295.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:350:62: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2544);
                    input_parameter296=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter296.getTree());

                    }
                    break;

            }

            char_literal297=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2548); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal297_tree = (Object)adaptor.create(char_literal297);
            adaptor.addChild(root_0, char_literal297_tree);
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
    // JPA.g:353:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
    public final JPAParser.date_today_macro_expression_return date_today_macro_expression() throws RecognitionException {
        JPAParser.date_today_macro_expression_return retval = new JPAParser.date_today_macro_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal298=null;
        Token char_literal299=null;
        Token char_literal301=null;
        JPAParser.path_expression_return path_expression300 = null;


        Object string_literal298_tree=null;
        Object char_literal299_tree=null;
        Object char_literal301_tree=null;

        try {
            // JPA.g:354:2: ( '@TODAY' '(' path_expression ')' )
            // JPA.g:354:4: '@TODAY' '(' path_expression ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal298=(Token)match(input,96,FOLLOW_96_in_date_today_macro_expression2561); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal298_tree = (Object)adaptor.create(string_literal298);
            adaptor.addChild(root_0, string_literal298_tree);
            }
            char_literal299=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2563); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal299_tree = (Object)adaptor.create(char_literal299);
            adaptor.addChild(root_0, char_literal299_tree);
            }
            pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2565);
            path_expression300=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression300.getTree());
            char_literal301=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2567); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal301_tree = (Object)adaptor.create(char_literal301);
            adaptor.addChild(root_0, char_literal301_tree);
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

    public static class boolean_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_expression"
    // JPA.g:357:1: boolean_expression : ( boolean_primary | subquery );
    public final JPAParser.boolean_expression_return boolean_expression() throws RecognitionException {
        JPAParser.boolean_expression_return retval = new JPAParser.boolean_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.boolean_primary_return boolean_primary302 = null;

        JPAParser.subquery_return subquery303 = null;



        try {
            // JPA.g:358:2: ( boolean_primary | subquery )
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
                    // JPA.g:358:4: boolean_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_primary_in_boolean_expression2579);
                    boolean_primary302=boolean_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_primary302.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:359:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_boolean_expression2584);
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
    // JPA.g:361:1: boolean_primary : ( path_expression | boolean_literal | input_parameter );
    public final JPAParser.boolean_primary_return boolean_primary() throws RecognitionException {
        JPAParser.boolean_primary_return retval = new JPAParser.boolean_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression304 = null;

        JPAParser.boolean_literal_return boolean_literal305 = null;

        JPAParser.input_parameter_return input_parameter306 = null;



        try {
            // JPA.g:362:2: ( path_expression | boolean_literal | input_parameter )
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
                    // JPA.g:362:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_boolean_primary2593);
                    path_expression304=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression304.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:363:4: boolean_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_literal_in_boolean_primary2598);
                    boolean_literal305=boolean_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal305.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:364:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_boolean_primary2603);
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
    // JPA.g:366:1: enum_expression : ( enum_primary | subquery );
    public final JPAParser.enum_expression_return enum_expression() throws RecognitionException {
        JPAParser.enum_expression_return retval = new JPAParser.enum_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.enum_primary_return enum_primary307 = null;

        JPAParser.subquery_return subquery308 = null;



        try {
            // JPA.g:367:2: ( enum_primary | subquery )
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
                    // JPA.g:367:4: enum_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_primary_in_enum_expression2612);
                    enum_primary307=enum_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_primary307.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:368:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_enum_expression2617);
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
    // JPA.g:370:1: enum_primary : ( path_expression | enum_literal | input_parameter );
    public final JPAParser.enum_primary_return enum_primary() throws RecognitionException {
        JPAParser.enum_primary_return retval = new JPAParser.enum_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression309 = null;

        JPAParser.enum_literal_return enum_literal310 = null;

        JPAParser.input_parameter_return input_parameter311 = null;



        try {
            // JPA.g:371:2: ( path_expression | enum_literal | input_parameter )
            int alt92=3;
            int LA92_0 = input.LA(1);

            if ( (LA92_0==WORD) ) {
                int LA92_1 = input.LA(2);

                if ( (LA92_1==57) ) {
                    alt92=1;
                }
                else if ( (LA92_1==EOF||LA92_1==HAVING||(LA92_1>=OR && LA92_1<=AND)||LA92_1==RPAREN||(LA92_1>=ORDER && LA92_1<=GROUP)||(LA92_1>=75 && LA92_1<=76)) ) {
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
                    // JPA.g:371:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_enum_primary2626);
                    path_expression309=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression309.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:372:4: enum_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_literal_in_enum_primary2631);
                    enum_literal310=enum_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal310.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:373:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_enum_primary2636);
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
    // JPA.g:375:1: entity_expression : ( path_expression | simple_entity_expression );
    public final JPAParser.entity_expression_return entity_expression() throws RecognitionException {
        JPAParser.entity_expression_return retval = new JPAParser.entity_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression312 = null;

        JPAParser.simple_entity_expression_return simple_entity_expression313 = null;



        try {
            // JPA.g:376:2: ( path_expression | simple_entity_expression )
            int alt93=2;
            int LA93_0 = input.LA(1);

            if ( (LA93_0==WORD) ) {
                int LA93_1 = input.LA(2);

                if ( (LA93_1==57) ) {
                    alt93=1;
                }
                else if ( (LA93_1==EOF||LA93_1==HAVING||(LA93_1>=OR && LA93_1<=AND)||LA93_1==RPAREN||(LA93_1>=ORDER && LA93_1<=GROUP)||LA93_1==62||LA93_1==69||(LA93_1>=75 && LA93_1<=76)) ) {
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
                    // JPA.g:376:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_entity_expression2645);
                    path_expression312=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression312.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:377:4: simple_entity_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simple_entity_expression_in_entity_expression2650);
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
    // JPA.g:379:1: simple_entity_expression : ( identification_variable | input_parameter );
    public final JPAParser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
        JPAParser.simple_entity_expression_return retval = new JPAParser.simple_entity_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.identification_variable_return identification_variable314 = null;

        JPAParser.input_parameter_return input_parameter315 = null;



        try {
            // JPA.g:380:2: ( identification_variable | input_parameter )
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
                    // JPA.g:380:4: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_simple_entity_expression2659);
                    identification_variable314=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable314.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:381:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_simple_entity_expression2664);
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
    // JPA.g:383:1: functions_returning_numerics : ( 'LENGTH' '(' string_primary ')' | 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')' | 'ABS' '(' simple_arithmetic_expression ')' | 'SQRT' '(' simple_arithmetic_expression ')' | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'SIZE' '(' path_expression ')' );
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
            // JPA.g:384:2: ( 'LENGTH' '(' string_primary ')' | 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')' | 'ABS' '(' simple_arithmetic_expression ')' | 'SQRT' '(' simple_arithmetic_expression ')' | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'SIZE' '(' path_expression ')' )
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
                    // JPA.g:384:4: 'LENGTH' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal316=(Token)match(input,97,FOLLOW_97_in_functions_returning_numerics2673); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal316_tree = (Object)adaptor.create(string_literal316);
                    adaptor.addChild(root_0, string_literal316_tree);
                    }
                    char_literal317=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2675); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal317_tree = (Object)adaptor.create(char_literal317);
                    adaptor.addChild(root_0, char_literal317_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics2676);
                    string_primary318=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary318.getTree());
                    char_literal319=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2677); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal319_tree = (Object)adaptor.create(char_literal319);
                    adaptor.addChild(root_0, char_literal319_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:385:4: 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal320=(Token)match(input,98,FOLLOW_98_in_functions_returning_numerics2682); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal320_tree = (Object)adaptor.create(string_literal320);
                    adaptor.addChild(root_0, string_literal320_tree);
                    }
                    char_literal321=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2684); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal321_tree = (Object)adaptor.create(char_literal321);
                    adaptor.addChild(root_0, char_literal321_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics2685);
                    string_primary322=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary322.getTree());
                    char_literal323=(Token)match(input,54,FOLLOW_54_in_functions_returning_numerics2686); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal323_tree = (Object)adaptor.create(char_literal323);
                    adaptor.addChild(root_0, char_literal323_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics2688);
                    string_primary324=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary324.getTree());
                    // JPA.g:385:48: ( ',' simple_arithmetic_expression )?
                    int alt95=2;
                    int LA95_0 = input.LA(1);

                    if ( (LA95_0==54) ) {
                        alt95=1;
                    }
                    switch (alt95) {
                        case 1 :
                            // JPA.g:385:49: ',' simple_arithmetic_expression
                            {
                            char_literal325=(Token)match(input,54,FOLLOW_54_in_functions_returning_numerics2690); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal325_tree = (Object)adaptor.create(char_literal325);
                            adaptor.addChild(root_0, char_literal325_tree);
                            }
                            pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2692);
                            simple_arithmetic_expression326=simple_arithmetic_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression326.getTree());

                            }
                            break;

                    }

                    char_literal327=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2695); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal327_tree = (Object)adaptor.create(char_literal327);
                    adaptor.addChild(root_0, char_literal327_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:386:4: 'ABS' '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal328=(Token)match(input,99,FOLLOW_99_in_functions_returning_numerics2700); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal328_tree = (Object)adaptor.create(string_literal328);
                    adaptor.addChild(root_0, string_literal328_tree);
                    }
                    char_literal329=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2702); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal329_tree = (Object)adaptor.create(char_literal329);
                    adaptor.addChild(root_0, char_literal329_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2703);
                    simple_arithmetic_expression330=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression330.getTree());
                    char_literal331=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2704); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal331_tree = (Object)adaptor.create(char_literal331);
                    adaptor.addChild(root_0, char_literal331_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:387:4: 'SQRT' '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal332=(Token)match(input,100,FOLLOW_100_in_functions_returning_numerics2709); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal332_tree = (Object)adaptor.create(string_literal332);
                    adaptor.addChild(root_0, string_literal332_tree);
                    }
                    char_literal333=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2711); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal333_tree = (Object)adaptor.create(char_literal333);
                    adaptor.addChild(root_0, char_literal333_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2712);
                    simple_arithmetic_expression334=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression334.getTree());
                    char_literal335=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2713); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal335_tree = (Object)adaptor.create(char_literal335);
                    adaptor.addChild(root_0, char_literal335_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:388:4: 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal336=(Token)match(input,101,FOLLOW_101_in_functions_returning_numerics2718); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal336_tree = (Object)adaptor.create(string_literal336);
                    adaptor.addChild(root_0, string_literal336_tree);
                    }
                    char_literal337=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2720); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal337_tree = (Object)adaptor.create(char_literal337);
                    adaptor.addChild(root_0, char_literal337_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2721);
                    simple_arithmetic_expression338=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression338.getTree());
                    char_literal339=(Token)match(input,54,FOLLOW_54_in_functions_returning_numerics2722); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal339_tree = (Object)adaptor.create(char_literal339);
                    adaptor.addChild(root_0, char_literal339_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2724);
                    simple_arithmetic_expression340=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression340.getTree());
                    char_literal341=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2725); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal341_tree = (Object)adaptor.create(char_literal341);
                    adaptor.addChild(root_0, char_literal341_tree);
                    }

                    }
                    break;
                case 6 :
                    // JPA.g:389:4: 'SIZE' '(' path_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal342=(Token)match(input,102,FOLLOW_102_in_functions_returning_numerics2730); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal342_tree = (Object)adaptor.create(string_literal342);
                    adaptor.addChild(root_0, string_literal342_tree);
                    }
                    char_literal343=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_numerics2732); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal343_tree = (Object)adaptor.create(char_literal343);
                    adaptor.addChild(root_0, char_literal343_tree);
                    }
                    pushFollow(FOLLOW_path_expression_in_functions_returning_numerics2733);
                    path_expression344=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression344.getTree());
                    char_literal345=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics2734); if (state.failed) return retval;
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
    // JPA.g:391:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
    public final JPAParser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
        JPAParser.functions_returning_datetime_return retval = new JPAParser.functions_returning_datetime_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set346=null;

        Object set346_tree=null;

        try {
            // JPA.g:392:2: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
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
    // JPA.g:396:1: functions_returning_strings : ( 'CONCAT' '(' string_primary ',' string_primary ')' | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')' | 'LOWER' '(' string_primary ')' | 'UPPER' '(' string_primary ')' );
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
            // JPA.g:397:2: ( 'CONCAT' '(' string_primary ',' string_primary ')' | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')' | 'LOWER' '(' string_primary ')' | 'UPPER' '(' string_primary ')' )
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
                    // JPA.g:397:4: 'CONCAT' '(' string_primary ',' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal347=(Token)match(input,106,FOLLOW_106_in_functions_returning_strings2762); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal347_tree = (Object)adaptor.create(string_literal347);
                    adaptor.addChild(root_0, string_literal347_tree);
                    }
                    char_literal348=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2764); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal348_tree = (Object)adaptor.create(char_literal348);
                    adaptor.addChild(root_0, char_literal348_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2765);
                    string_primary349=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary349.getTree());
                    char_literal350=(Token)match(input,54,FOLLOW_54_in_functions_returning_strings2766); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal350_tree = (Object)adaptor.create(char_literal350);
                    adaptor.addChild(root_0, char_literal350_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2768);
                    string_primary351=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary351.getTree());
                    char_literal352=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2769); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal352_tree = (Object)adaptor.create(char_literal352);
                    adaptor.addChild(root_0, char_literal352_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:398:4: 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal353=(Token)match(input,107,FOLLOW_107_in_functions_returning_strings2774); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal353_tree = (Object)adaptor.create(string_literal353);
                    adaptor.addChild(root_0, string_literal353_tree);
                    }
                    char_literal354=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2776); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal354_tree = (Object)adaptor.create(char_literal354);
                    adaptor.addChild(root_0, char_literal354_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2777);
                    string_primary355=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary355.getTree());
                    char_literal356=(Token)match(input,54,FOLLOW_54_in_functions_returning_strings2778); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal356_tree = (Object)adaptor.create(char_literal356);
                    adaptor.addChild(root_0, char_literal356_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2779);
                    simple_arithmetic_expression357=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression357.getTree());
                    char_literal358=(Token)match(input,54,FOLLOW_54_in_functions_returning_strings2780); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal358_tree = (Object)adaptor.create(char_literal358);
                    adaptor.addChild(root_0, char_literal358_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2782);
                    simple_arithmetic_expression359=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression359.getTree());
                    char_literal360=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2783); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal360_tree = (Object)adaptor.create(char_literal360);
                    adaptor.addChild(root_0, char_literal360_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:399:4: 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal361=(Token)match(input,108,FOLLOW_108_in_functions_returning_strings2788); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal361_tree = (Object)adaptor.create(string_literal361);
                    adaptor.addChild(root_0, string_literal361_tree);
                    }
                    char_literal362=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2790); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal362_tree = (Object)adaptor.create(char_literal362);
                    adaptor.addChild(root_0, char_literal362_tree);
                    }
                    // JPA.g:399:14: ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )?
                    int alt99=2;
                    int LA99_0 = input.LA(1);

                    if ( (LA99_0==TRIM_CHARACTER||LA99_0==53||(LA99_0>=111 && LA99_0<=113)) ) {
                        alt99=1;
                    }
                    switch (alt99) {
                        case 1 :
                            // JPA.g:399:15: ( trim_specification )? ( TRIM_CHARACTER )? 'FROM'
                            {
                            // JPA.g:399:15: ( trim_specification )?
                            int alt97=2;
                            int LA97_0 = input.LA(1);

                            if ( ((LA97_0>=111 && LA97_0<=113)) ) {
                                alt97=1;
                            }
                            switch (alt97) {
                                case 1 :
                                    // JPA.g:399:16: trim_specification
                                    {
                                    pushFollow(FOLLOW_trim_specification_in_functions_returning_strings2793);
                                    trim_specification363=trim_specification();

                                    state._fsp--;
                                    if (state.failed) return retval;
                                    if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification363.getTree());

                                    }
                                    break;

                            }

                            // JPA.g:399:37: ( TRIM_CHARACTER )?
                            int alt98=2;
                            int LA98_0 = input.LA(1);

                            if ( (LA98_0==TRIM_CHARACTER) ) {
                                alt98=1;
                            }
                            switch (alt98) {
                                case 1 :
                                    // JPA.g:399:38: TRIM_CHARACTER
                                    {
                                    TRIM_CHARACTER364=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_functions_returning_strings2798); if (state.failed) return retval;
                                    if ( state.backtracking==0 ) {
                                    TRIM_CHARACTER364_tree = (Object)adaptor.create(TRIM_CHARACTER364);
                                    adaptor.addChild(root_0, TRIM_CHARACTER364_tree);
                                    }

                                    }
                                    break;

                            }

                            string_literal365=(Token)match(input,53,FOLLOW_53_in_functions_returning_strings2802); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal365_tree = (Object)adaptor.create(string_literal365);
                            adaptor.addChild(root_0, string_literal365_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2806);
                    string_primary366=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary366.getTree());
                    char_literal367=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2807); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal367_tree = (Object)adaptor.create(char_literal367);
                    adaptor.addChild(root_0, char_literal367_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:400:4: 'LOWER' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal368=(Token)match(input,109,FOLLOW_109_in_functions_returning_strings2812); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal368_tree = (Object)adaptor.create(string_literal368);
                    adaptor.addChild(root_0, string_literal368_tree);
                    }
                    char_literal369=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2814); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal369_tree = (Object)adaptor.create(char_literal369);
                    adaptor.addChild(root_0, char_literal369_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2815);
                    string_primary370=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary370.getTree());
                    char_literal371=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2816); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal371_tree = (Object)adaptor.create(char_literal371);
                    adaptor.addChild(root_0, char_literal371_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:401:4: 'UPPER' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal372=(Token)match(input,110,FOLLOW_110_in_functions_returning_strings2821); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal372_tree = (Object)adaptor.create(string_literal372);
                    adaptor.addChild(root_0, string_literal372_tree);
                    }
                    char_literal373=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings2823); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal373_tree = (Object)adaptor.create(char_literal373);
                    adaptor.addChild(root_0, char_literal373_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings2824);
                    string_primary374=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary374.getTree());
                    char_literal375=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings2825); if (state.failed) return retval;
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
    // JPA.g:403:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
    public final JPAParser.trim_specification_return trim_specification() throws RecognitionException {
        JPAParser.trim_specification_return retval = new JPAParser.trim_specification_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set376=null;

        Object set376_tree=null;

        try {
            // JPA.g:404:2: ( 'LEADING' | 'TRAILING' | 'BOTH' )
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
    // JPA.g:409:1: abstract_schema_name : WORD ;
    public final JPAParser.abstract_schema_name_return abstract_schema_name() throws RecognitionException {
        JPAParser.abstract_schema_name_return retval = new JPAParser.abstract_schema_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD377=null;

        Object WORD377_tree=null;

        try {
            // JPA.g:410:4: ( WORD )
            // JPA.g:410:6: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD377=(Token)match(input,WORD,FOLLOW_WORD_in_abstract_schema_name2856); if (state.failed) return retval;
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
    // JPA.g:413:1: pattern_value : WORD ;
    public final JPAParser.pattern_value_return pattern_value() throws RecognitionException {
        JPAParser.pattern_value_return retval = new JPAParser.pattern_value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD378=null;

        Object WORD378_tree=null;

        try {
            // JPA.g:414:2: ( WORD )
            // JPA.g:414:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD378=(Token)match(input,WORD,FOLLOW_WORD_in_pattern_value2866); if (state.failed) return retval;
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
    // JPA.g:417:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
    public final JPAParser.numeric_literal_return numeric_literal() throws RecognitionException {
        JPAParser.numeric_literal_return retval = new JPAParser.numeric_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal379=null;
        Token INT_NUMERAL380=null;

        Object string_literal379_tree=null;
        Object INT_NUMERAL380_tree=null;

        try {
            // JPA.g:418:2: ( ( '0x' )? INT_NUMERAL )
            // JPA.g:418:4: ( '0x' )? INT_NUMERAL
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:418:4: ( '0x' )?
            int alt101=2;
            int LA101_0 = input.LA(1);

            if ( (LA101_0==114) ) {
                alt101=1;
            }
            switch (alt101) {
                case 1 :
                    // JPA.g:418:5: '0x'
                    {
                    string_literal379=(Token)match(input,114,FOLLOW_114_in_numeric_literal2877); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal379_tree = (Object)adaptor.create(string_literal379);
                    adaptor.addChild(root_0, string_literal379_tree);
                    }

                    }
                    break;

            }

            INT_NUMERAL380=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal2881); if (state.failed) return retval;
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
    // JPA.g:420:1: input_parameter : ( '?' INT_NUMERAL -> ^( T_PARAMETER[] '?' INT_NUMERAL ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' ) ;
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
            // JPA.g:421:2: ( ( '?' INT_NUMERAL -> ^( T_PARAMETER[] '?' INT_NUMERAL ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' ) )
            // JPA.g:421:4: ( '?' INT_NUMERAL -> ^( T_PARAMETER[] '?' INT_NUMERAL ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' )
            {
            // JPA.g:421:4: ( '?' INT_NUMERAL -> ^( T_PARAMETER[] '?' INT_NUMERAL ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' )
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
                    // JPA.g:421:6: '?' INT_NUMERAL
                    {
                    char_literal381=(Token)match(input,115,FOLLOW_115_in_input_parameter2894); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_115.add(char_literal381);

                    INT_NUMERAL382=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_input_parameter2896); if (state.failed) return retval; 
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
                    // 421:22: -> ^( T_PARAMETER[] '?' INT_NUMERAL )
                    {
                        // JPA.g:421:25: ^( T_PARAMETER[] '?' INT_NUMERAL )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_PARAMETER, "T_PARAMETER"), root_1);

                        adaptor.addChild(root_1, stream_115.nextNode());
                        adaptor.addChild(root_1, stream_INT_NUMERAL.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // JPA.g:422:4: NAMED_PARAMETER
                    {
                    NAMED_PARAMETER383=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter2915); if (state.failed) return retval; 
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
                    // 422:20: -> ^( T_PARAMETER[] NAMED_PARAMETER )
                    {
                        // JPA.g:422:23: ^( T_PARAMETER[] NAMED_PARAMETER )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_PARAMETER, "T_PARAMETER"), root_1);

                        adaptor.addChild(root_1, stream_NAMED_PARAMETER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // JPA.g:423:4: '${' WORD '}'
                    {
                    string_literal384=(Token)match(input,116,FOLLOW_116_in_input_parameter2931); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_116.add(string_literal384);

                    WORD385=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter2933); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WORD.add(WORD385);

                    char_literal386=(Token)match(input,117,FOLLOW_117_in_input_parameter2935); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_117.add(char_literal386);


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
    // $ANTLR end "input_parameter"

    public static class parameter_name_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameter_name"
    // JPA.g:426:1: parameter_name : WORD ( '.' WORD )* ;
    public final JPAParser.parameter_name_return parameter_name() throws RecognitionException {
        JPAParser.parameter_name_return retval = new JPAParser.parameter_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD387=null;
        Token char_literal388=null;
        Token WORD389=null;

        Object WORD387_tree=null;
        Object char_literal388_tree=null;
        Object WORD389_tree=null;

        try {
            // JPA.g:427:2: ( WORD ( '.' WORD )* )
            // JPA.g:427:4: WORD ( '.' WORD )*
            {
            root_0 = (Object)adaptor.nil();

            WORD387=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name2949); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD387_tree = (Object)adaptor.create(WORD387);
            adaptor.addChild(root_0, WORD387_tree);
            }
            // JPA.g:427:9: ( '.' WORD )*
            loop103:
            do {
                int alt103=2;
                int LA103_0 = input.LA(1);

                if ( (LA103_0==57) ) {
                    alt103=1;
                }


                switch (alt103) {
            	case 1 :
            	    // JPA.g:427:11: '.' WORD
            	    {
            	    char_literal388=(Token)match(input,57,FOLLOW_57_in_parameter_name2953); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal388_tree = (Object)adaptor.create(char_literal388);
            	    adaptor.addChild(root_0, char_literal388_tree);
            	    }
            	    WORD389=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name2955); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    WORD389_tree = (Object)adaptor.create(WORD389);
            	    adaptor.addChild(root_0, WORD389_tree);
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

    public static class literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literal"
    // JPA.g:430:1: literal : WORD ;
    public final JPAParser.literal_return literal() throws RecognitionException {
        JPAParser.literal_return retval = new JPAParser.literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD390=null;

        Object WORD390_tree=null;

        try {
            // JPA.g:431:2: ( WORD )
            // JPA.g:431:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD390=(Token)match(input,WORD,FOLLOW_WORD_in_literal2970); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD390_tree = (Object)adaptor.create(WORD390);
            adaptor.addChild(root_0, WORD390_tree);
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
    // JPA.g:433:1: constructor_name : WORD ;
    public final JPAParser.constructor_name_return constructor_name() throws RecognitionException {
        JPAParser.constructor_name_return retval = new JPAParser.constructor_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD391=null;

        Object WORD391_tree=null;

        try {
            // JPA.g:434:2: ( WORD )
            // JPA.g:434:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD391=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name2979); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD391_tree = (Object)adaptor.create(WORD391);
            adaptor.addChild(root_0, WORD391_tree);
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
    // JPA.g:436:1: enum_literal : WORD ;
    public final JPAParser.enum_literal_return enum_literal() throws RecognitionException {
        JPAParser.enum_literal_return retval = new JPAParser.enum_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD392=null;

        Object WORD392_tree=null;

        try {
            // JPA.g:437:2: ( WORD )
            // JPA.g:437:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD392=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal3020); if (state.failed) return retval;
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
    // $ANTLR end "enum_literal"

    public static class boolean_literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_literal"
    // JPA.g:439:1: boolean_literal : ( 'true' | 'false' );
    public final JPAParser.boolean_literal_return boolean_literal() throws RecognitionException {
        JPAParser.boolean_literal_return retval = new JPAParser.boolean_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set393=null;

        Object set393_tree=null;

        try {
            // JPA.g:440:2: ( 'true' | 'false' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set393=(Token)input.LT(1);
            if ( (input.LA(1)>=118 && input.LA(1)<=119) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set393));
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
    // JPA.g:443:1: field : ( WORD | 'GROUP' ) ;
    public final JPAParser.field_return field() throws RecognitionException {
        JPAParser.field_return retval = new JPAParser.field_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set394=null;

        Object set394_tree=null;

        try {
            // JPA.g:444:2: ( ( WORD | 'GROUP' ) )
            // JPA.g:444:4: ( WORD | 'GROUP' )
            {
            root_0 = (Object)adaptor.nil();

            set394=(Token)input.LT(1);
            if ( input.LA(1)==GROUP||input.LA(1)==WORD ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set394));
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
    // JPA.g:447:1: identification_variable : WORD ;
    public final JPAParser.identification_variable_return identification_variable() throws RecognitionException {
        JPAParser.identification_variable_return retval = new JPAParser.identification_variable_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD395=null;

        Object WORD395_tree=null;

        try {
            // JPA.g:448:2: ( WORD )
            // JPA.g:448:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD395=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable3063); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD395_tree = (Object)adaptor.create(WORD395);
            adaptor.addChild(root_0, WORD395_tree);
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

    // $ANTLR start synpred20_JPA
    public final void synpred20_JPA_fragment() throws RecognitionException {   
        // JPA.g:122:49: ( field )
        // JPA.g:122:49: field
        {
        pushFollow(FOLLOW_field_in_synpred20_JPA746);
        field();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred20_JPA

    // $ANTLR start synpred23_JPA
    public final void synpred23_JPA_fragment() throws RecognitionException {   
        // JPA.g:130:49: ( field )
        // JPA.g:130:49: field
        {
        pushFollow(FOLLOW_field_in_synpred23_JPA832);
        field();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred23_JPA

    // $ANTLR start synpred57_JPA
    public final void synpred57_JPA_fragment() throws RecognitionException {   
        // JPA.g:222:8: ( 'NOT' )
        // JPA.g:222:8: 'NOT'
        {
        match(input,62,FOLLOW_62_in_synpred57_JPA1625); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred57_JPA

    // $ANTLR start synpred58_JPA
    public final void synpred58_JPA_fragment() throws RecognitionException {   
        // JPA.g:222:6: ( ( 'NOT' )? simple_cond_expression )
        // JPA.g:222:6: ( 'NOT' )? simple_cond_expression
        {
        // JPA.g:222:6: ( 'NOT' )?
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
                // JPA.g:222:8: 'NOT'
                {
                match(input,62,FOLLOW_62_in_synpred58_JPA1625); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_simple_cond_expression_in_synpred58_JPA1630);
        simple_cond_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred58_JPA

    // $ANTLR start synpred59_JPA
    public final void synpred59_JPA_fragment() throws RecognitionException {   
        // JPA.g:228:6: ( comparison_expression )
        // JPA.g:228:6: comparison_expression
        {
        pushFollow(FOLLOW_comparison_expression_in_synpred59_JPA1676);
        comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred59_JPA

    // $ANTLR start synpred60_JPA
    public final void synpred60_JPA_fragment() throws RecognitionException {   
        // JPA.g:229:4: ( between_expression )
        // JPA.g:229:4: between_expression
        {
        pushFollow(FOLLOW_between_expression_in_synpred60_JPA1682);
        between_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred60_JPA

    // $ANTLR start synpred61_JPA
    public final void synpred61_JPA_fragment() throws RecognitionException {   
        // JPA.g:230:4: ( like_expression )
        // JPA.g:230:4: like_expression
        {
        pushFollow(FOLLOW_like_expression_in_synpred61_JPA1688);
        like_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred61_JPA

    // $ANTLR start synpred62_JPA
    public final void synpred62_JPA_fragment() throws RecognitionException {   
        // JPA.g:231:4: ( in_expression )
        // JPA.g:231:4: in_expression
        {
        pushFollow(FOLLOW_in_expression_in_synpred62_JPA1694);
        in_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred62_JPA

    // $ANTLR start synpred63_JPA
    public final void synpred63_JPA_fragment() throws RecognitionException {   
        // JPA.g:232:4: ( null_comparison_expression )
        // JPA.g:232:4: null_comparison_expression
        {
        pushFollow(FOLLOW_null_comparison_expression_in_synpred63_JPA1700);
        null_comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred63_JPA

    // $ANTLR start synpred64_JPA
    public final void synpred64_JPA_fragment() throws RecognitionException {   
        // JPA.g:233:4: ( empty_collection_comparison_expression )
        // JPA.g:233:4: empty_collection_comparison_expression
        {
        pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred64_JPA1706);
        empty_collection_comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred64_JPA

    // $ANTLR start synpred65_JPA
    public final void synpred65_JPA_fragment() throws RecognitionException {   
        // JPA.g:234:4: ( collection_member_expression )
        // JPA.g:234:4: collection_member_expression
        {
        pushFollow(FOLLOW_collection_member_expression_in_synpred65_JPA1712);
        collection_member_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred65_JPA

    // $ANTLR start synpred68_JPA
    public final void synpred68_JPA_fragment() throws RecognitionException {   
        // JPA.g:239:4: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
        // JPA.g:239:4: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
        {
        pushFollow(FOLLOW_arithmetic_expression_in_synpred68_JPA1735);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:239:26: ( 'NOT' )?
        int alt109=2;
        int LA109_0 = input.LA(1);

        if ( (LA109_0==62) ) {
            alt109=1;
        }
        switch (alt109) {
            case 1 :
                // JPA.g:239:27: 'NOT'
                {
                match(input,62,FOLLOW_62_in_synpred68_JPA1738); if (state.failed) return ;

                }
                break;

        }

        match(input,63,FOLLOW_63_in_synpred68_JPA1742); if (state.failed) return ;
        pushFollow(FOLLOW_arithmetic_expression_in_synpred68_JPA1744);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,AND,FOLLOW_AND_in_synpred68_JPA1746); if (state.failed) return ;
        pushFollow(FOLLOW_arithmetic_expression_in_synpred68_JPA1748);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred68_JPA

    // $ANTLR start synpred70_JPA
    public final void synpred70_JPA_fragment() throws RecognitionException {   
        // JPA.g:240:4: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
        // JPA.g:240:4: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
        {
        pushFollow(FOLLOW_string_expression_in_synpred70_JPA1753);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:240:22: ( 'NOT' )?
        int alt110=2;
        int LA110_0 = input.LA(1);

        if ( (LA110_0==62) ) {
            alt110=1;
        }
        switch (alt110) {
            case 1 :
                // JPA.g:240:23: 'NOT'
                {
                match(input,62,FOLLOW_62_in_synpred70_JPA1756); if (state.failed) return ;

                }
                break;

        }

        match(input,63,FOLLOW_63_in_synpred70_JPA1760); if (state.failed) return ;
        pushFollow(FOLLOW_string_expression_in_synpred70_JPA1762);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,AND,FOLLOW_AND_in_synpred70_JPA1764); if (state.failed) return ;
        pushFollow(FOLLOW_string_expression_in_synpred70_JPA1766);
        string_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred70_JPA

    // $ANTLR start synpred88_JPA
    public final void synpred88_JPA_fragment() throws RecognitionException {   
        // JPA.g:274:4: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) )
        // JPA.g:274:4: string_expression comparison_operator ( string_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_string_expression_in_synpred88_JPA1994);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_comparison_operator_in_synpred88_JPA1996);
        comparison_operator();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:274:42: ( string_expression | all_or_any_expression )
        int alt112=2;
        int LA112_0 = input.LA(1);

        if ( ((LA112_0>=AVG && LA112_0<=COUNT)||LA112_0==STRINGLITERAL||(LA112_0>=WORD && LA112_0<=NAMED_PARAMETER)||LA112_0==56||(LA112_0>=106 && LA112_0<=110)||(LA112_0>=115 && LA112_0<=116)) ) {
            alt112=1;
        }
        else if ( ((LA112_0>=72 && LA112_0<=74)) ) {
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
                // JPA.g:274:43: string_expression
                {
                pushFollow(FOLLOW_string_expression_in_synpred88_JPA1999);
                string_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:274:63: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred88_JPA2003);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred88_JPA

    // $ANTLR start synpred91_JPA
    public final void synpred91_JPA_fragment() throws RecognitionException {   
        // JPA.g:275:4: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
        // JPA.g:275:4: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_boolean_expression_in_synpred91_JPA2009);
        boolean_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=75 && input.LA(1)<=76) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:275:36: ( boolean_expression | all_or_any_expression )
        int alt113=2;
        int LA113_0 = input.LA(1);

        if ( ((LA113_0>=WORD && LA113_0<=NAMED_PARAMETER)||LA113_0==56||(LA113_0>=115 && LA113_0<=116)||(LA113_0>=118 && LA113_0<=119)) ) {
            alt113=1;
        }
        else if ( ((LA113_0>=72 && LA113_0<=74)) ) {
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
                // JPA.g:275:37: boolean_expression
                {
                pushFollow(FOLLOW_boolean_expression_in_synpred91_JPA2020);
                boolean_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:275:58: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred91_JPA2024);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred91_JPA

    // $ANTLR start synpred94_JPA
    public final void synpred94_JPA_fragment() throws RecognitionException {   
        // JPA.g:276:4: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
        // JPA.g:276:4: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_enum_expression_in_synpred94_JPA2030);
        enum_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=75 && input.LA(1)<=76) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:276:31: ( enum_expression | all_or_any_expression )
        int alt114=2;
        int LA114_0 = input.LA(1);

        if ( ((LA114_0>=WORD && LA114_0<=NAMED_PARAMETER)||LA114_0==56||(LA114_0>=115 && LA114_0<=116)) ) {
            alt114=1;
        }
        else if ( ((LA114_0>=72 && LA114_0<=74)) ) {
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
                // JPA.g:276:32: enum_expression
                {
                pushFollow(FOLLOW_enum_expression_in_synpred94_JPA2039);
                enum_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:276:50: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred94_JPA2043);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred94_JPA

    // $ANTLR start synpred96_JPA
    public final void synpred96_JPA_fragment() throws RecognitionException {   
        // JPA.g:277:4: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
        // JPA.g:277:4: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_datetime_expression_in_synpred96_JPA2049);
        datetime_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_comparison_operator_in_synpred96_JPA2051);
        comparison_operator();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:277:44: ( datetime_expression | all_or_any_expression )
        int alt115=2;
        int LA115_0 = input.LA(1);

        if ( ((LA115_0>=AVG && LA115_0<=COUNT)||(LA115_0>=WORD && LA115_0<=NAMED_PARAMETER)||LA115_0==56||(LA115_0>=103 && LA115_0<=105)||(LA115_0>=115 && LA115_0<=116)) ) {
            alt115=1;
        }
        else if ( ((LA115_0>=72 && LA115_0<=74)) ) {
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
                // JPA.g:277:45: datetime_expression
                {
                pushFollow(FOLLOW_datetime_expression_in_synpred96_JPA2054);
                datetime_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:277:67: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred96_JPA2058);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred96_JPA

    // $ANTLR start synpred99_JPA
    public final void synpred99_JPA_fragment() throws RecognitionException {   
        // JPA.g:278:4: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
        // JPA.g:278:4: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_entity_expression_in_synpred99_JPA2064);
        entity_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=75 && input.LA(1)<=76) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:278:35: ( entity_expression | all_or_any_expression )
        int alt116=2;
        int LA116_0 = input.LA(1);

        if ( ((LA116_0>=WORD && LA116_0<=NAMED_PARAMETER)||(LA116_0>=115 && LA116_0<=116)) ) {
            alt116=1;
        }
        else if ( ((LA116_0>=72 && LA116_0<=74)) ) {
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
                // JPA.g:278:36: entity_expression
                {
                pushFollow(FOLLOW_entity_expression_in_synpred99_JPA2075);
                entity_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:278:56: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred99_JPA2079);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred99_JPA

    // Delegated rules

    public final boolean synpred91_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred91_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred68_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred68_JPA_fragment(); // can never throw exception
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
    public final boolean synpred88_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred88_JPA_fragment(); // can never throw exception
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
    public final boolean synpred70_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred70_JPA_fragment(); // can never throw exception
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
    public final boolean synpred94_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred94_JPA_fragment(); // can never throw exception
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
    public final boolean synpred96_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred96_JPA_fragment(); // can never throw exception
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
    public final boolean synpred99_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred99_JPA_fragment(); // can never throw exception
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
    protected DFA53 dfa53 = new DFA53(this);
    protected DFA73 dfa73 = new DFA73(this);
    static final String DFA31_eotS =
        "\11\uffff";
    static final String DFA31_eofS =
        "\4\uffff\3\7\1\uffff\1\7";
    static final String DFA31_minS =
        "\1\75\1\30\1\uffff\1\71\3\25\1\uffff\1\25";
    static final String DFA31_maxS =
        "\1\75\1\167\1\uffff\1\114\3\124\1\uffff\1\124";
    static final String DFA31_acceptS =
        "\2\uffff\1\1\4\uffff\1\2\1\uffff";
    static final String DFA31_specialS =
        "\11\uffff}>";
    static final String[] DFA31_transitionS = {
            "\1\1",
            "\5\2\2\uffff\1\2\14\uffff\2\2\1\uffff\1\3\1\2\7\uffff\1\2"+
            "\5\uffff\1\2\10\uffff\1\2\11\uffff\2\2\2\uffff\1\2\7\uffff\22"+
            "\2\3\uffff\3\2\1\uffff\2\2",
            "",
            "\1\4\4\uffff\1\2\6\uffff\1\2\5\uffff\2\2",
            "\1\7\12\uffff\1\7\6\uffff\1\7\1\5\6\uffff\1\6\12\uffff\1\2"+
            "\3\uffff\3\2\1\uffff\1\2\2\uffff\1\2\5\uffff\12\2",
            "\1\7\12\uffff\1\7\6\uffff\3\7\17\uffff\1\10\1\2\3\uffff\3"+
            "\2\1\uffff\1\2\2\uffff\1\2\5\uffff\12\2",
            "\1\7\12\uffff\1\7\6\uffff\2\7\20\uffff\1\10\1\2\3\uffff\3"+
            "\2\1\uffff\1\2\2\uffff\1\2\5\uffff\12\2",
            "",
            "\1\7\12\uffff\1\7\6\uffff\1\7\1\5\6\uffff\1\6\12\uffff\1\2"+
            "\3\uffff\3\2\1\uffff\1\2\2\uffff\1\2\5\uffff\12\2"
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
            return "164:4: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION[$wh] path_expression ) )";
        }
    }
    static final String DFA36_eotS =
        "\7\uffff";
    static final String DFA36_eofS =
        "\2\uffff\2\4\2\uffff\1\4";
    static final String DFA36_minS =
        "\1\57\1\71\2\26\2\uffff\1\26";
    static final String DFA36_maxS =
        "\1\57\1\71\1\66\1\71\2\uffff\1\66";
    static final String DFA36_acceptS =
        "\4\uffff\1\1\1\2\1\uffff";
    static final String DFA36_specialS =
        "\7\uffff}>";
    static final String[] DFA36_transitionS = {
            "\1\1",
            "\1\2",
            "\1\4\1\5\10\uffff\1\4\7\uffff\1\3\6\uffff\1\3\6\uffff\1\4",
            "\1\4\1\5\10\uffff\1\4\25\uffff\1\4\2\uffff\1\6",
            "",
            "",
            "\1\4\1\5\10\uffff\1\4\7\uffff\1\3\6\uffff\1\3\6\uffff\1\4"
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
            return "184:4: ( path_expression ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] path_expression ( 'ASC' )? ) | path_expression 'DESC' -> ^( T_ORDER_BY_FIELD[] path_expression 'DESC' ) )";
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
            "\5\1\2\uffff\1\24\14\uffff\2\1\1\uffff\2\1\7\uffff\1\1\5\uffff"+
            "\1\1\10\uffff\1\1\11\uffff\2\1\2\uffff\1\1\7\uffff\22\1\3\uffff"+
            "\3\1\1\uffff\2\1",
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
            return "222:4: ( ( 'NOT' )? simple_cond_expression -> ^( T_SIMPLE_CONDITION[] ( 'NOT' )? simple_cond_expression ) | '(' conditional_expression ')' )";
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
            "\4\14\1\13\2\uffff\1\23\14\uffff\1\2\1\22\1\uffff\1\1\1\4\7"+
            "\uffff\1\15\5\uffff\1\32\10\uffff\1\32\11\uffff\2\20\2\uffff"+
            "\1\34\7\uffff\4\34\1\24\1\25\1\26\1\27\1\30\1\31\3\17\1\6\1"+
            "\7\1\10\1\11\1\12\3\uffff\1\21\1\3\1\5\1\uffff\2\16",
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
            return "228:4: ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )";
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
    static final String DFA53_eotS =
        "\31\uffff";
    static final String DFA53_eofS =
        "\31\uffff";
    static final String DFA53_minS =
        "\1\30\1\uffff\1\0\3\uffff\3\0\6\uffff\3\0\7\uffff";
    static final String DFA53_maxS =
        "\1\164\1\uffff\1\0\3\uffff\3\0\6\uffff\3\0\7\uffff";
    static final String DFA53_acceptS =
        "\1\uffff\1\1\20\uffff\1\2\5\uffff\1\3";
    static final String DFA53_specialS =
        "\2\uffff\1\0\3\uffff\1\1\1\2\1\3\6\uffff\1\4\1\5\1\6\7\uffff}>";
    static final String[] DFA53_transitionS = {
            "\4\20\1\17\2\uffff\1\1\14\uffff\1\22\1\1\1\uffff\1\2\1\7\7"+
            "\uffff\1\21\30\uffff\2\1\16\uffff\6\1\3\30\5\22\3\uffff\1\1"+
            "\1\6\1\10",
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

    static final short[] DFA53_eot = DFA.unpackEncodedString(DFA53_eotS);
    static final short[] DFA53_eof = DFA.unpackEncodedString(DFA53_eofS);
    static final char[] DFA53_min = DFA.unpackEncodedStringToUnsignedChars(DFA53_minS);
    static final char[] DFA53_max = DFA.unpackEncodedStringToUnsignedChars(DFA53_maxS);
    static final short[] DFA53_accept = DFA.unpackEncodedString(DFA53_acceptS);
    static final short[] DFA53_special = DFA.unpackEncodedString(DFA53_specialS);
    static final short[][] DFA53_transition;

    static {
        int numStates = DFA53_transitionS.length;
        DFA53_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA53_transition[i] = DFA.unpackEncodedString(DFA53_transitionS[i]);
        }
    }

    class DFA53 extends DFA {

        public DFA53(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 53;
            this.eot = DFA53_eot;
            this.eof = DFA53_eof;
            this.min = DFA53_min;
            this.max = DFA53_max;
            this.accept = DFA53_accept;
            this.special = DFA53_special;
            this.transition = DFA53_transition;
        }
        public String getDescription() {
            return "238:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA53_2 = input.LA(1);

                         
                        int index53_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_JPA()) ) {s = 1;}

                        else if ( (synpred70_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index53_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA53_6 = input.LA(1);

                         
                        int index53_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_JPA()) ) {s = 1;}

                        else if ( (synpred70_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index53_6);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA53_7 = input.LA(1);

                         
                        int index53_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_JPA()) ) {s = 1;}

                        else if ( (synpred70_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index53_7);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA53_8 = input.LA(1);

                         
                        int index53_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_JPA()) ) {s = 1;}

                        else if ( (synpred70_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index53_8);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA53_15 = input.LA(1);

                         
                        int index53_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_JPA()) ) {s = 1;}

                        else if ( (synpred70_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index53_15);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA53_16 = input.LA(1);

                         
                        int index53_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_JPA()) ) {s = 1;}

                        else if ( (synpred70_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index53_16);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA53_17 = input.LA(1);

                         
                        int index53_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_JPA()) ) {s = 1;}

                        else if ( (synpred70_JPA()) ) {s = 18;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index53_17);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 53, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA73_eotS =
        "\34\uffff";
    static final String DFA73_eofS =
        "\34\uffff";
    static final String DFA73_minS =
        "\1\30\1\0\1\uffff\3\0\5\uffff\3\0\16\uffff";
    static final String DFA73_maxS =
        "\1\167\1\0\1\uffff\3\0\5\uffff\3\0\16\uffff";
    static final String DFA73_acceptS =
        "\2\uffff\1\1\13\uffff\1\2\1\4\1\6\11\uffff\1\3\1\5";
    static final String DFA73_specialS =
        "\1\uffff\1\0\1\uffff\1\1\1\2\1\3\5\uffff\1\4\1\5\1\6\16\uffff}>";
    static final String[] DFA73_transitionS = {
            "\4\14\1\13\2\uffff\1\20\14\uffff\1\2\1\20\1\uffff\1\1\1\4\7"+
            "\uffff\1\15\30\uffff\2\20\16\uffff\6\20\3\17\5\2\3\uffff\1\20"+
            "\1\3\1\5\1\uffff\2\16",
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

    static final short[] DFA73_eot = DFA.unpackEncodedString(DFA73_eotS);
    static final short[] DFA73_eof = DFA.unpackEncodedString(DFA73_eofS);
    static final char[] DFA73_min = DFA.unpackEncodedStringToUnsignedChars(DFA73_minS);
    static final char[] DFA73_max = DFA.unpackEncodedStringToUnsignedChars(DFA73_maxS);
    static final short[] DFA73_accept = DFA.unpackEncodedString(DFA73_acceptS);
    static final short[] DFA73_special = DFA.unpackEncodedString(DFA73_specialS);
    static final short[][] DFA73_transition;

    static {
        int numStates = DFA73_transitionS.length;
        DFA73_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA73_transition[i] = DFA.unpackEncodedString(DFA73_transitionS[i]);
        }
    }

    class DFA73 extends DFA {

        public DFA73(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 73;
            this.eot = DFA73_eot;
            this.eof = DFA73_eof;
            this.min = DFA73_min;
            this.max = DFA73_max;
            this.accept = DFA73_accept;
            this.special = DFA73_special;
            this.transition = DFA73_transition;
        }
        public String getDescription() {
            return "273:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA73_1 = input.LA(1);

                         
                        int index73_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred88_JPA()) ) {s = 2;}

                        else if ( (synpred91_JPA()) ) {s = 14;}

                        else if ( (synpred94_JPA()) ) {s = 26;}

                        else if ( (synpred96_JPA()) ) {s = 15;}

                        else if ( (synpred99_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index73_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA73_3 = input.LA(1);

                         
                        int index73_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred88_JPA()) ) {s = 2;}

                        else if ( (synpred91_JPA()) ) {s = 14;}

                        else if ( (synpred94_JPA()) ) {s = 26;}

                        else if ( (synpred96_JPA()) ) {s = 15;}

                        else if ( (synpred99_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index73_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA73_4 = input.LA(1);

                         
                        int index73_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred88_JPA()) ) {s = 2;}

                        else if ( (synpred91_JPA()) ) {s = 14;}

                        else if ( (synpred94_JPA()) ) {s = 26;}

                        else if ( (synpred96_JPA()) ) {s = 15;}

                        else if ( (synpred99_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index73_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA73_5 = input.LA(1);

                         
                        int index73_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred88_JPA()) ) {s = 2;}

                        else if ( (synpred91_JPA()) ) {s = 14;}

                        else if ( (synpred94_JPA()) ) {s = 26;}

                        else if ( (synpred96_JPA()) ) {s = 15;}

                        else if ( (synpred99_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index73_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA73_11 = input.LA(1);

                         
                        int index73_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred88_JPA()) ) {s = 2;}

                        else if ( (synpred96_JPA()) ) {s = 15;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index73_11);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA73_12 = input.LA(1);

                         
                        int index73_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred88_JPA()) ) {s = 2;}

                        else if ( (synpred96_JPA()) ) {s = 15;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index73_12);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA73_13 = input.LA(1);

                         
                        int index73_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred88_JPA()) ) {s = 2;}

                        else if ( (synpred91_JPA()) ) {s = 14;}

                        else if ( (synpred94_JPA()) ) {s = 26;}

                        else if ( (synpred96_JPA()) ) {s = 15;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index73_13);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 73, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_select_statement_in_ql_statement297 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELECT_in_select_statement309 = new BitSet(new long[]{0x180080021F000000L});
    public static final BitSet FOLLOW_select_clause_in_select_statement311 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_from_clause_in_select_statement313 = new BitSet(new long[]{0x2000018000200002L});
    public static final BitSet FOLLOW_where_clause_in_select_statement316 = new BitSet(new long[]{0x0000018000200002L});
    public static final BitSet FOLLOW_groupby_clause_in_select_statement321 = new BitSet(new long[]{0x0000008000200002L});
    public static final BitSet FOLLOW_having_clause_in_select_statement326 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_orderby_clause_in_select_statement330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_from_clause398 = new BitSet(new long[]{0x0100800000000000L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause400 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_from_clause404 = new BitSet(new long[]{0x0500800000000000L});
    public static final BitSet FOLLOW_identification_variable_or_collection_declaration_in_from_clause406 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_or_collection_declaration445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_or_collection_declaration449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration476 = new BitSet(new long[]{0x0000003400000002L});
    public static final BitSet FOLLOW_joined_clause_in_identification_variable_declaration480 = new BitSet(new long[]{0x0000003400000002L});
    public static final BitSet FOLLOW_join_in_joined_clause515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fetch_join_in_joined_clause519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_variable_declaration_source_in_range_variable_declaration532 = new BitSet(new long[]{0x0080800000000000L});
    public static final BitSet FOLLOW_55_in_range_variable_declaration535 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_abstract_schema_name_in_range_variable_declaration_source566 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_56_in_range_variable_declaration_source573 = new BitSet(new long[]{0x180080021F000000L});
    public static final BitSet FOLLOW_select_clause_in_range_variable_declaration_source575 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_from_clause_in_range_variable_declaration_source577 = new BitSet(new long[]{0x2000018100200000L});
    public static final BitSet FOLLOW_where_clause_in_range_variable_declaration_source580 = new BitSet(new long[]{0x0000018100200000L});
    public static final BitSet FOLLOW_groupby_clause_in_range_variable_declaration_source585 = new BitSet(new long[]{0x0000008100200000L});
    public static final BitSet FOLLOW_having_clause_in_range_variable_declaration_source590 = new BitSet(new long[]{0x0000008100000000L});
    public static final BitSet FOLLOW_orderby_clause_in_range_variable_declaration_source594 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_range_variable_declaration_source600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_spec_in_join653 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_join_association_path_expression_in_join655 = new BitSet(new long[]{0x0080800000000000L});
    public static final BitSet FOLLOW_55_in_join659 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_identification_variable_in_join664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_spec_in_fetch_join692 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_FETCH_in_fetch_join694 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join696 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_in_join_spec706 = new BitSet(new long[]{0x0000002800000000L});
    public static final BitSet FOLLOW_OUTER_in_join_spec710 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_INNER_in_join_spec716 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_JOIN_in_join_spec721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression731 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_join_association_path_expression733 = new BitSet(new long[]{0x0000810000000002L});
    public static final BitSet FOLLOW_field_in_join_association_path_expression737 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_join_association_path_expression739 = new BitSet(new long[]{0x0000810000000002L});
    public static final BitSet FOLLOW_field_in_join_association_path_expression746 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_collection_member_declaration778 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration780 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_path_expression_in_collection_member_declaration782 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration784 = new BitSet(new long[]{0x0080800000000000L});
    public static final BitSet FOLLOW_55_in_collection_member_declaration788 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_path_expression817 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_path_expression819 = new BitSet(new long[]{0x0000810000000002L});
    public static final BitSet FOLLOW_field_in_path_expression823 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_path_expression825 = new BitSet(new long[]{0x0000810000000002L});
    public static final BitSet FOLLOW_field_in_path_expression832 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_select_clause868 = new BitSet(new long[]{0x180080021F000000L});
    public static final BitSet FOLLOW_select_expression_in_select_clause873 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_select_clause877 = new BitSet(new long[]{0x180080021F000000L});
    public static final BitSet FOLLOW_select_expression_in_select_clause879 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_path_expression_in_select_expression925 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_select_expression930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_select_expression935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_select_expression950 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_select_expression952 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_identification_variable_in_select_expression953 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_select_expression954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_expression_in_select_expression959 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_constructor_expression968 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_constructor_name_in_constructor_expression970 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_constructor_expression972 = new BitSet(new long[]{0x000080001F000000L});
    public static final BitSet FOLLOW_constructor_item_in_constructor_expression974 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_54_in_constructor_expression977 = new BitSet(new long[]{0x000080001F000000L});
    public static final BitSet FOLLOW_constructor_item_in_constructor_expression979 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_constructor_expression983 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_constructor_item992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_constructor_item996 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1008 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1010 = new BitSet(new long[]{0x0000800200000000L});
    public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1014 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_path_expression_in_aggregate_expression1019 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COUNT_in_aggregate_expression1053 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1055 = new BitSet(new long[]{0x0000800200000000L});
    public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1059 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_identification_variable_in_aggregate_expression1064 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1066 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_aggregate_expression_function_name1106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_where_clause1142 = new BitSet(new long[]{0x4101B0009F000000L,0x00DC7FFFE0260080L});
    public static final BitSet FOLLOW_conditional_expression_in_where_clause1144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_where_clause1161 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_path_expression_in_where_clause1163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_groupby_clause1186 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_BY_in_groupby_clause1188 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_groupby_item_in_groupby_clause1190 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_groupby_clause1194 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_groupby_item_in_groupby_clause1196 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_path_expression_in_groupby_item1233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_groupby_item1237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HAVING_in_having_clause1246 = new BitSet(new long[]{0x4101B0009F000000L,0x00DC7FFFE0260080L});
    public static final BitSet FOLLOW_conditional_expression_in_having_clause1248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ORDER_in_orderby_clause1258 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_BY_in_orderby_clause1260 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_orderby_item_in_orderby_clause1262 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_orderby_clause1266 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_orderby_item_in_orderby_clause1268 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_path_expression_in_orderby_item1308 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_ASC_in_orderby_item1312 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_orderby_item1339 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_DESC_in_orderby_item1341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_56_in_subquery1369 = new BitSet(new long[]{0x000080021F000000L});
    public static final BitSet FOLLOW_simple_select_clause_in_subquery1371 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_subquery_from_clause_in_subquery1373 = new BitSet(new long[]{0x2000010100200000L});
    public static final BitSet FOLLOW_where_clause_in_subquery1376 = new BitSet(new long[]{0x0000010100200000L});
    public static final BitSet FOLLOW_groupby_clause_in_subquery1381 = new BitSet(new long[]{0x0000000100200000L});
    public static final BitSet FOLLOW_having_clause_in_subquery1386 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_subquery1392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_subquery_from_clause1437 = new BitSet(new long[]{0x0500800000000000L});
    public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1439 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_subquery_from_clause1443 = new BitSet(new long[]{0x0500800000000000L});
    public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1445 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_association_path_expression_in_subselect_identification_variable_declaration1489 = new BitSet(new long[]{0x0080800000000000L});
    public static final BitSet FOLLOW_55_in_subselect_identification_variable_declaration1492 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration1496 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_declaration_in_subselect_identification_variable_declaration1501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_association_path_expression1510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause1522 = new BitSet(new long[]{0x000080021F000000L});
    public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause1527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_simple_select_expression1565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression1570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_simple_select_expression1575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditional_term_in_conditional_expression1585 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_OR_in_conditional_expression1589 = new BitSet(new long[]{0x4101B0009F000000L,0x00DC7FFFE0260080L});
    public static final BitSet FOLLOW_conditional_term_in_conditional_expression1591 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_conditional_factor_in_conditional_term1603 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_AND_in_conditional_term1607 = new BitSet(new long[]{0x4101B0009F000000L,0x00DC7FFFE0260080L});
    public static final BitSet FOLLOW_conditional_factor_in_conditional_term1609 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_62_in_conditional_factor1625 = new BitSet(new long[]{0x4101B0009F000000L,0x00DC7FFFE0260080L});
    public static final BitSet FOLLOW_simple_cond_expression_in_conditional_factor1630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_conditional_factor1656 = new BitSet(new long[]{0x4101B0009F000000L,0x00DC7FFFE0260080L});
    public static final BitSet FOLLOW_conditional_expression_in_conditional_factor1658 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_conditional_factor1660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression1676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_expression_in_simple_cond_expression1682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_expression_in_simple_cond_expression1688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_expression_in_simple_cond_expression1694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression1700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression1706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression1712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression1718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression1724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1735 = new BitSet(new long[]{0xC000000000000000L});
    public static final BitSet FOLLOW_62_in_between_expression1738 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_between_expression1742 = new BitSet(new long[]{0x0101B0009F000000L,0x00DC7FFE00060000L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1744 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression1746 = new BitSet(new long[]{0x0101B0009F000000L,0x00DC7FFE00060000L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1748 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1753 = new BitSet(new long[]{0xC000000000000000L});
    public static final BitSet FOLLOW_62_in_between_expression1756 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_between_expression1760 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1762 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression1764 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1771 = new BitSet(new long[]{0xC000000000000000L});
    public static final BitSet FOLLOW_62_in_between_expression1774 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_between_expression1778 = new BitSet(new long[]{0x010190001F000000L,0x00187F8000000000L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1780 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_between_expression1782 = new BitSet(new long[]{0x010190001F000000L,0x00187F8000000000L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_in_expression1793 = new BitSet(new long[]{0x4400000000000000L});
    public static final BitSet FOLLOW_62_in_in_expression1796 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_58_in_in_expression1800 = new BitSet(new long[]{0x010190009F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_in_expression_right_part_in_in_expression1802 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_in_expression_right_part1811 = new BitSet(new long[]{0x0001800000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_in_item_in_in_expression_right_part1813 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_54_in_in_expression_right_part1816 = new BitSet(new long[]{0x0001800000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_in_item_in_in_expression_right_part1818 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_in_expression_right_part1822 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_in_expression_right_part1827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_in_item1836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_in_item1841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_like_expression1851 = new BitSet(new long[]{0x4000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_62_in_like_expression1855 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_like_expression1860 = new BitSet(new long[]{0x0001800000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_pattern_value_in_like_expression1864 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_like_expression1868 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_like_expression1874 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_ESCAPE_CHARACTER_in_like_expression1876 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_null_comparison_expression1892 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression1896 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_null_comparison_expression1899 = new BitSet(new long[]{0x4000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_62_in_null_comparison_expression1902 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_null_comparison_expression1906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression1915 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_empty_collection_comparison_expression1917 = new BitSet(new long[]{0x4000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_62_in_empty_collection_comparison_expression1920 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_68_in_empty_collection_comparison_expression1924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_collection_member_expression1933 = new BitSet(new long[]{0x4000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_62_in_collection_member_expression1936 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_collection_member_expression1940 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_collection_member_expression1943 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_path_expression_in_collection_member_expression1947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_exists_expression1957 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_exists_expression1961 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_subquery_in_exists_expression1963 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_all_or_any_expression1972 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_subquery_in_all_or_any_expression1985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_comparison_expression1994 = new BitSet(new long[]{0x0000000000000000L,0x000000000001F800L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression1996 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000700L});
    public static final BitSet FOLLOW_string_expression_in_comparison_expression1999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2003 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2009 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_set_in_comparison_expression2011 = new BitSet(new long[]{0x010190001F000000L,0x00D87C0000000700L});
    public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_expression_in_comparison_expression2030 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_set_in_comparison_expression2032 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000700L});
    public static final BitSet FOLLOW_enum_expression_in_comparison_expression2039 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2049 = new BitSet(new long[]{0x0000000000000000L,0x000000000001F800L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2051 = new BitSet(new long[]{0x010190001F000000L,0x00187F8000000700L});
    public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_comparison_expression2064 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_set_in_comparison_expression2066 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000700L});
    public static final BitSet FOLLOW_entity_expression_in_comparison_expression2075 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression2085 = new BitSet(new long[]{0x0000000000000000L,0x000000000001F800L});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2087 = new BitSet(new long[]{0x0101B0009F000000L,0x00DC7FFE00060700L});
    public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression2090 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2094 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_comparison_operator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_arithmetic_expression2138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_arithmetic_expression2143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_term_in_simple_arithmetic_expression2153 = new BitSet(new long[]{0x0000000000000002L,0x0000000000060000L});
    public static final BitSet FOLLOW_set_in_simple_arithmetic_expression2157 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_arithmetic_term_in_simple_arithmetic_expression2167 = new BitSet(new long[]{0x0000000000000002L,0x0000000000060000L});
    public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term2179 = new BitSet(new long[]{0x0000000000000002L,0x0000000000180000L});
    public static final BitSet FOLLOW_set_in_arithmetic_term2183 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term2193 = new BitSet(new long[]{0x0000000000000002L,0x0000000000180000L});
    public static final BitSet FOLLOW_set_in_arithmetic_factor2204 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor2215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_arithmetic_primary2224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary2229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary2234 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_arithmetic_primary2235 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary2236 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary2241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary2246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary2251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_primary_in_string_expression2260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_string_expression2264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_string_primary2273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_string_primary2278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_string_primary2283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_strings_in_string_primary2288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_string_primary2293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_primary_in_datetime_expression2302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_datetime_expression2307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_datetime_primary2316 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_datetime_primary2321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_primary2326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_datetime_primary2331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2355 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_date_between_macro_expression2381 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2383 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2385 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_between_macro_expression2387 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_86_in_date_between_macro_expression2389 = new BitSet(new long[]{0x0040000000000000L,0x0000000000060000L});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression2393 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_date_between_macro_expression2403 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_between_macro_expression2408 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_86_in_date_between_macro_expression2410 = new BitSet(new long[]{0x0040000000000000L,0x0000000000060000L});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression2414 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_date_between_macro_expression2424 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_between_macro_expression2429 = new BitSet(new long[]{0x0000000000000000L,0x000000001F800000L});
    public static final BitSet FOLLOW_set_in_date_between_macro_expression2431 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_date_before_macro_expression2468 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2470 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2472 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_before_macro_expression2474 = new BitSet(new long[]{0x0001800000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2478 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2482 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2486 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_date_after_macro_expression2499 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2501 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2503 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_after_macro_expression2505 = new BitSet(new long[]{0x0001800000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2509 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2513 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_date_equals_macro_expression2530 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2532 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2534 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_date_equals_macro_expression2536 = new BitSet(new long[]{0x0001800000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2540 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2544 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_96_in_date_today_macro_expression2561 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2563 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2565 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_primary_in_boolean_expression2579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_boolean_expression2584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_boolean_primary2593 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_literal_in_boolean_primary2598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_boolean_primary2603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_primary_in_enum_expression2612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_enum_expression2617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_enum_primary2626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_literal_in_enum_primary2631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_enum_primary2636 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_entity_expression2645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression2650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression2659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression2664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_functions_returning_numerics2673 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2675 = new BitSet(new long[]{0x000190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics2676 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_functions_returning_numerics2682 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2684 = new BitSet(new long[]{0x000190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics2685 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_numerics2686 = new BitSet(new long[]{0x000190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics2688 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_numerics2690 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2692 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_functions_returning_numerics2700 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2702 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2703 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_functions_returning_numerics2709 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2711 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2712 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2713 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_101_in_functions_returning_numerics2718 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2720 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2721 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_numerics2722 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics2724 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_102_in_functions_returning_numerics2730 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_numerics2732 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics2733 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics2734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_functions_returning_datetime0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_106_in_functions_returning_strings2762 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2764 = new BitSet(new long[]{0x000190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2765 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_strings2766 = new BitSet(new long[]{0x000190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2768 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_107_in_functions_returning_strings2774 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2776 = new BitSet(new long[]{0x000190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2777 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_strings2778 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2779 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functions_returning_strings2780 = new BitSet(new long[]{0x0001A0009F000000L,0x001C007E00060000L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_strings2782 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_functions_returning_strings2788 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2790 = new BitSet(new long[]{0x0021D0001F000000L,0x001BFC0000000000L});
    public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings2793 = new BitSet(new long[]{0x0020400000000000L});
    public static final BitSet FOLLOW_TRIM_CHARACTER_in_functions_returning_strings2798 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_functions_returning_strings2802 = new BitSet(new long[]{0x000190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2806 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_109_in_functions_returning_strings2812 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2814 = new BitSet(new long[]{0x000190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2815 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_110_in_functions_returning_strings2821 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings2823 = new BitSet(new long[]{0x000190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings2824 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings2825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_trim_specification0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_abstract_schema_name2856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_pattern_value2866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_114_in_numeric_literal2877 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal2881 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_115_in_input_parameter2894 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_input_parameter2896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter2915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_116_in_input_parameter2931 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_WORD_in_input_parameter2933 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_117_in_input_parameter2935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_parameter_name2949 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_57_in_parameter_name2953 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_WORD_in_parameter_name2955 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_WORD_in_literal2970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_constructor_name2979 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_enum_literal3020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_boolean_literal0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_field3043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_identification_variable3063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_field_in_synpred20_JPA746 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_field_in_synpred23_JPA832 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_synpred57_JPA1625 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_synpred58_JPA1625 = new BitSet(new long[]{0x4101B0009F000000L,0x00DC7FFFE0260080L});
    public static final BitSet FOLLOW_simple_cond_expression_in_synpred58_JPA1630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_expression_in_synpred59_JPA1676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_expression_in_synpred60_JPA1682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_expression_in_synpred61_JPA1688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_expression_in_synpred62_JPA1694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_comparison_expression_in_synpred63_JPA1700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred64_JPA1706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_expression_in_synpred65_JPA1712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred68_JPA1735 = new BitSet(new long[]{0xC000000000000000L});
    public static final BitSet FOLLOW_62_in_synpred68_JPA1738 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_synpred68_JPA1742 = new BitSet(new long[]{0x0101B0009F000000L,0x00DC7FFE00060000L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred68_JPA1744 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_synpred68_JPA1746 = new BitSet(new long[]{0x0101B0009F000000L,0x00DC7FFE00060000L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred68_JPA1748 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_synpred70_JPA1753 = new BitSet(new long[]{0xC000000000000000L});
    public static final BitSet FOLLOW_62_in_synpred70_JPA1756 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_synpred70_JPA1760 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_expression_in_synpred70_JPA1762 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_synpred70_JPA1764 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000000L});
    public static final BitSet FOLLOW_string_expression_in_synpred70_JPA1766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_synpred88_JPA1994 = new BitSet(new long[]{0x0000000000000000L,0x000000000001F800L});
    public static final BitSet FOLLOW_comparison_operator_in_synpred88_JPA1996 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000700L});
    public static final BitSet FOLLOW_string_expression_in_synpred88_JPA1999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred88_JPA2003 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_expression_in_synpred91_JPA2009 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_set_in_synpred91_JPA2011 = new BitSet(new long[]{0x010190001F000000L,0x00D87C0000000700L});
    public static final BitSet FOLLOW_boolean_expression_in_synpred91_JPA2020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred91_JPA2024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_expression_in_synpred94_JPA2030 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_set_in_synpred94_JPA2032 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000700L});
    public static final BitSet FOLLOW_enum_expression_in_synpred94_JPA2039 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred94_JPA2043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_synpred96_JPA2049 = new BitSet(new long[]{0x0000000000000000L,0x000000000001F800L});
    public static final BitSet FOLLOW_comparison_operator_in_synpred96_JPA2051 = new BitSet(new long[]{0x010190001F000000L,0x00187F8000000700L});
    public static final BitSet FOLLOW_datetime_expression_in_synpred96_JPA2054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred96_JPA2058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_synpred99_JPA2064 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_set_in_synpred99_JPA2066 = new BitSet(new long[]{0x010190001F000000L,0x00187C0000000700L});
    public static final BitSet FOLLOW_entity_expression_in_synpred99_JPA2075 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred99_JPA2079 = new BitSet(new long[]{0x0000000000000002L});

}