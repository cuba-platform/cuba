// $ANTLR 3.2 Sep 23, 2009 12:02:23 JPA.g 2010-12-17 02:33:32

package com.haulmont.cuba.core.sys.jpql.antlr;

import com.haulmont.cuba.core.sys.jpql.tree.QueryNode;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.JoinVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;

import org.antlr.runtime.*;

import org.antlr.runtime.tree.*;

public class JPAParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "T_SELECTED_ITEMS", "T_SOURCES", "T_SELECTED_FIELD", "T_SELECTED_ENTITY", "T_ID_VAR", "T_JOIN_VAR", "T_QUERY", "T_CONDITION", "ESCAPE_CHARACTER", "STRINGLITERAL", "TRIM_CHARACTER", "WORD", "INT_NUMERAL", "SIMPLE_FIELD_PATH", "FIELD_PATH", "WS", "COMMENT", "LINE_COMMENT", "'SELECT'", "'FROM'", "','", "'AS'", "'(SELECT'", "')'", "'FETCH'", "'LEFT'", "'OUTER'", "'INNER'", "'JOIN'", "'IN'", "'('", "'DISTINCT'", "'OBJECT'", "'NEW'", "'AVG'", "'MAX'", "'MIN'", "'SUM'", "'COUNT'", "'WHERE'", "'GROUP'", "'BY'", "'HAVING'", "'ORDER'", "'ASC'", "'DESC'", "'OR'", "'AND'", "'NOT'", "'BETWEEN'", "'LIKE'", "'ESCAPE'", "'IS'", "'NULL'", "'EMPTY'", "'MEMBER'", "'OF'", "'EXISTS'", "'ALL'", "'ANY'", "'SOME'", "'='", "'<>'", "'>'", "'>='", "'<'", "'<='", "'+'", "'-'", "'*'", "'/'", "'LENGTH'", "'LOCATE'", "'ABS'", "'SQRT'", "'MOD'", "'SIZE'", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'CONCAT'", "'SUBSTRING'", "'TRIM'", "'LOWER'", "'UPPER'", "'LEADING'", "'TRAILING'", "'BOTH'", "'0x'", "'?'", "':'", "'true'", "'false'"
    };
    public static final int T_JOIN_VAR=9;
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int EOF=-1;
    public static final int T__93=93;
    public static final int WORD=15;
    public static final int T__94=94;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__90=90;
    public static final int SIMPLE_FIELD_PATH=17;
    public static final int TRIM_CHARACTER=14;
    public static final int COMMENT=20;
    public static final int T__96=96;
    public static final int T_QUERY=10;
    public static final int T__95=95;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int LINE_COMMENT=21;
    public static final int T__85=85;
    public static final int T__84=84;
    public static final int T__87=87;
    public static final int T__86=86;
    public static final int T__89=89;
    public static final int T__88=88;
    public static final int FIELD_PATH=18;
    public static final int WS=19;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__70=70;
    public static final int T_SELECTED_FIELD=6;
    public static final int T__76=76;
    public static final int T__75=75;
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
    public static final int T_ID_VAR=8;
    public static final int T__61=61;
    public static final int T__60=60;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__59=59;
    public static final int T__50=50;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__44=44;
    public static final int T_CONDITION=11;
    public static final int T__45=45;
    public static final int T_SELECTED_ENTITY=7;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int ESCAPE_CHARACTER=12;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int INT_NUMERAL=16;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int STRINGLITERAL=13;
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
    // JPA.g:44:1: ql_statement : select_statement ;
    public final JPAParser.ql_statement_return ql_statement() throws RecognitionException {
        JPAParser.ql_statement_return retval = new JPAParser.ql_statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.select_statement_return select_statement1 = null;



        try {
            // JPA.g:45:2: ( select_statement )
            // JPA.g:45:4: select_statement
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_select_statement_in_ql_statement122);
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
    // JPA.g:47:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
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
        RewriteRuleTokenStream stream_22=new RewriteRuleTokenStream(adaptor,"token 22");
        RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");
        try {
            // JPA.g:48:2: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
            // JPA.g:48:5: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )?
            {
            sl=(Token)match(input,22,FOLLOW_22_in_select_statement134); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_22.add(sl);

            pushFollow(FOLLOW_select_clause_in_select_statement136);
            select_clause2=select_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_select_clause.add(select_clause2.getTree());
            pushFollow(FOLLOW_from_clause_in_select_statement138);
            from_clause3=from_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_from_clause.add(from_clause3.getTree());
            // JPA.g:48:43: ( where_clause )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==43) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // JPA.g:48:44: where_clause
                    {
                    pushFollow(FOLLOW_where_clause_in_select_statement141);
                    where_clause4=where_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_where_clause.add(where_clause4.getTree());

                    }
                    break;

            }

            // JPA.g:48:59: ( groupby_clause )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==44) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // JPA.g:48:60: groupby_clause
                    {
                    pushFollow(FOLLOW_groupby_clause_in_select_statement146);
                    groupby_clause5=groupby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause5.getTree());

                    }
                    break;

            }

            // JPA.g:48:77: ( having_clause )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==46) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // JPA.g:48:78: having_clause
                    {
                    pushFollow(FOLLOW_having_clause_in_select_statement151);
                    having_clause6=having_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_having_clause.add(having_clause6.getTree());

                    }
                    break;

            }

            // JPA.g:48:93: ( orderby_clause )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==47) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // JPA.g:48:94: orderby_clause
                    {
                    pushFollow(FOLLOW_orderby_clause_in_select_statement155);
                    orderby_clause7=orderby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause7.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: orderby_clause, having_clause, groupby_clause, from_clause, select_clause, where_clause
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 49:3: -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
            {
                // JPA.g:49:6: ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, sl), root_1);

                // JPA.g:49:32: ( select_clause )?
                if ( stream_select_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_select_clause.nextTree());

                }
                stream_select_clause.reset();
                adaptor.addChild(root_1, stream_from_clause.nextTree());
                // JPA.g:49:61: ( where_clause )?
                if ( stream_where_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_where_clause.nextTree());

                }
                stream_where_clause.reset();
                // JPA.g:49:77: ( groupby_clause )?
                if ( stream_groupby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                }
                stream_groupby_clause.reset();
                // JPA.g:49:95: ( having_clause )?
                if ( stream_having_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_having_clause.nextTree());

                }
                stream_having_clause.reset();
                // JPA.g:49:112: ( orderby_clause )?
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
    // JPA.g:51:1: from_clause : 'FROM' identification_variable_declaration ( ',' ( identification_variable_declaration | collection_member_declaration ) )* -> ^( T_SOURCES ( identification_variable_declaration )* ) ;
    public final JPAParser.from_clause_return from_clause() throws RecognitionException {
        JPAParser.from_clause_return retval = new JPAParser.from_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal8=null;
        Token char_literal10=null;
        JPAParser.identification_variable_declaration_return identification_variable_declaration9 = null;

        JPAParser.identification_variable_declaration_return identification_variable_declaration11 = null;

        JPAParser.collection_member_declaration_return collection_member_declaration12 = null;


        Object string_literal8_tree=null;
        Object char_literal10_tree=null;
        RewriteRuleTokenStream stream_23=new RewriteRuleTokenStream(adaptor,"token 23");
        RewriteRuleTokenStream stream_24=new RewriteRuleTokenStream(adaptor,"token 24");
        RewriteRuleSubtreeStream stream_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule collection_member_declaration");
        RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
        try {
            // JPA.g:52:2: ( 'FROM' identification_variable_declaration ( ',' ( identification_variable_declaration | collection_member_declaration ) )* -> ^( T_SOURCES ( identification_variable_declaration )* ) )
            // JPA.g:52:4: 'FROM' identification_variable_declaration ( ',' ( identification_variable_declaration | collection_member_declaration ) )*
            {
            string_literal8=(Token)match(input,23,FOLLOW_23_in_from_clause208); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_23.add(string_literal8);

            pushFollow(FOLLOW_identification_variable_declaration_in_from_clause210);
            identification_variable_declaration9=identification_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration9.getTree());
            // JPA.g:52:47: ( ',' ( identification_variable_declaration | collection_member_declaration ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==24) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // JPA.g:52:48: ',' ( identification_variable_declaration | collection_member_declaration )
            	    {
            	    char_literal10=(Token)match(input,24,FOLLOW_24_in_from_clause213); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_24.add(char_literal10);

            	    // JPA.g:52:51: ( identification_variable_declaration | collection_member_declaration )
            	    int alt5=2;
            	    int LA5_0 = input.LA(1);

            	    if ( (LA5_0==WORD||LA5_0==26) ) {
            	        alt5=1;
            	    }
            	    else if ( (LA5_0==33) ) {
            	        alt5=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 5, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt5) {
            	        case 1 :
            	            // JPA.g:52:52: identification_variable_declaration
            	            {
            	            pushFollow(FOLLOW_identification_variable_declaration_in_from_clause215);
            	            identification_variable_declaration11=identification_variable_declaration();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration11.getTree());

            	            }
            	            break;
            	        case 2 :
            	            // JPA.g:52:88: collection_member_declaration
            	            {
            	            pushFollow(FOLLOW_collection_member_declaration_in_from_clause217);
            	            collection_member_declaration12=collection_member_declaration();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) stream_collection_member_declaration.add(collection_member_declaration12.getTree());

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);



            // AST REWRITE
            // elements: identification_variable_declaration
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 53:2: -> ^( T_SOURCES ( identification_variable_declaration )* )
            {
                // JPA.g:53:5: ^( T_SOURCES ( identification_variable_declaration )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SOURCES, "T_SOURCES"), root_1);

                // JPA.g:53:17: ( identification_variable_declaration )*
                while ( stream_identification_variable_declaration.hasNext() ) {
                    adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());

                }
                stream_identification_variable_declaration.reset();

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

    public static class identification_variable_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "identification_variable_declaration"
    // JPA.g:55:1: identification_variable_declaration : range_variable_declaration ( join | fetch_join )* ;
    public final JPAParser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
        JPAParser.identification_variable_declaration_return retval = new JPAParser.identification_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.range_variable_declaration_return range_variable_declaration13 = null;

        JPAParser.join_return join14 = null;

        JPAParser.fetch_join_return fetch_join15 = null;



        try {
            // JPA.g:56:2: ( range_variable_declaration ( join | fetch_join )* )
            // JPA.g:56:4: range_variable_declaration ( join | fetch_join )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration239);
            range_variable_declaration13=range_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, range_variable_declaration13.getTree());
            // JPA.g:56:31: ( join | fetch_join )*
            loop7:
            do {
                int alt7=3;
                switch ( input.LA(1) ) {
                case 29:
                    {
                    int LA7_2 = input.LA(2);

                    if ( (LA7_2==30) ) {
                        int LA7_5 = input.LA(3);

                        if ( (LA7_5==32) ) {
                            int LA7_4 = input.LA(4);

                            if ( (LA7_4==SIMPLE_FIELD_PATH) ) {
                                alt7=1;
                            }
                            else if ( (LA7_4==28) ) {
                                alt7=2;
                            }


                        }


                    }
                    else if ( (LA7_2==32) ) {
                        int LA7_4 = input.LA(3);

                        if ( (LA7_4==SIMPLE_FIELD_PATH) ) {
                            alt7=1;
                        }
                        else if ( (LA7_4==28) ) {
                            alt7=2;
                        }


                    }


                    }
                    break;
                case 31:
                    {
                    int LA7_3 = input.LA(2);

                    if ( (LA7_3==32) ) {
                        int LA7_4 = input.LA(3);

                        if ( (LA7_4==SIMPLE_FIELD_PATH) ) {
                            alt7=1;
                        }
                        else if ( (LA7_4==28) ) {
                            alt7=2;
                        }


                    }


                    }
                    break;
                case 32:
                    {
                    int LA7_4 = input.LA(2);

                    if ( (LA7_4==SIMPLE_FIELD_PATH) ) {
                        alt7=1;
                    }
                    else if ( (LA7_4==28) ) {
                        alt7=2;
                    }


                    }
                    break;

                }

                switch (alt7) {
            	case 1 :
            	    // JPA.g:56:33: join
            	    {
            	    pushFollow(FOLLOW_join_in_identification_variable_declaration243);
            	    join14=join();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, join14.getTree());

            	    }
            	    break;
            	case 2 :
            	    // JPA.g:56:40: fetch_join
            	    {
            	    pushFollow(FOLLOW_fetch_join_in_identification_variable_declaration247);
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
    // JPA.g:58:1: range_variable_declaration : range_variable_declaration_source ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source ) ;
    public final JPAParser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
        JPAParser.range_variable_declaration_return retval = new JPAParser.range_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal17=null;
        JPAParser.range_variable_declaration_source_return range_variable_declaration_source16 = null;

        JPAParser.identification_variable_return identification_variable18 = null;


        Object string_literal17_tree=null;
        RewriteRuleTokenStream stream_25=new RewriteRuleTokenStream(adaptor,"token 25");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_range_variable_declaration_source=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration_source");
        try {
            // JPA.g:59:2: ( range_variable_declaration_source ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source ) )
            // JPA.g:59:4: range_variable_declaration_source ( 'AS' )? identification_variable
            {
            pushFollow(FOLLOW_range_variable_declaration_source_in_range_variable_declaration259);
            range_variable_declaration_source16=range_variable_declaration_source();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_range_variable_declaration_source.add(range_variable_declaration_source16.getTree());
            // JPA.g:59:38: ( 'AS' )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==25) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // JPA.g:59:39: 'AS'
                    {
                    string_literal17=(Token)match(input,25,FOLLOW_25_in_range_variable_declaration262); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_25.add(string_literal17);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_range_variable_declaration266);
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
            // 60:4: -> ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source )
            {
                // JPA.g:60:7: ^( T_ID_VAR[$identification_variable.text] range_variable_declaration_source )
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
    // JPA.g:63:1: range_variable_declaration_source : ( abstract_schema_name | lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')' -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) );
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
        RewriteRuleTokenStream stream_26=new RewriteRuleTokenStream(adaptor,"token 26");
        RewriteRuleTokenStream stream_27=new RewriteRuleTokenStream(adaptor,"token 27");
        RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");
        try {
            // JPA.g:64:2: ( abstract_schema_name | lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')' -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==WORD) ) {
                alt13=1;
            }
            else if ( (LA13_0==26) ) {
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
                    // JPA.g:64:4: abstract_schema_name
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_abstract_schema_name_in_range_variable_declaration_source293);
                    abstract_schema_name19=abstract_schema_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, abstract_schema_name19.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:65:4: lp= '(SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? rp= ')'
                    {
                    lp=(Token)match(input,26,FOLLOW_26_in_range_variable_declaration_source300); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_26.add(lp);

                    pushFollow(FOLLOW_select_clause_in_range_variable_declaration_source302);
                    select_clause20=select_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_select_clause.add(select_clause20.getTree());
                    pushFollow(FOLLOW_from_clause_in_range_variable_declaration_source304);
                    from_clause21=from_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_from_clause.add(from_clause21.getTree());
                    // JPA.g:65:43: ( where_clause )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==43) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // JPA.g:65:44: where_clause
                            {
                            pushFollow(FOLLOW_where_clause_in_range_variable_declaration_source307);
                            where_clause22=where_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_where_clause.add(where_clause22.getTree());

                            }
                            break;

                    }

                    // JPA.g:65:59: ( groupby_clause )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==44) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // JPA.g:65:60: groupby_clause
                            {
                            pushFollow(FOLLOW_groupby_clause_in_range_variable_declaration_source312);
                            groupby_clause23=groupby_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause23.getTree());

                            }
                            break;

                    }

                    // JPA.g:65:77: ( having_clause )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==46) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // JPA.g:65:78: having_clause
                            {
                            pushFollow(FOLLOW_having_clause_in_range_variable_declaration_source317);
                            having_clause24=having_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_having_clause.add(having_clause24.getTree());

                            }
                            break;

                    }

                    // JPA.g:65:93: ( orderby_clause )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==47) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // JPA.g:65:94: orderby_clause
                            {
                            pushFollow(FOLLOW_orderby_clause_in_range_variable_declaration_source321);
                            orderby_clause25=orderby_clause();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause25.getTree());

                            }
                            break;

                    }

                    rp=(Token)match(input,27,FOLLOW_27_in_range_variable_declaration_source327); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_27.add(rp);



                    // AST REWRITE
                    // elements: where_clause, having_clause, from_clause, select_clause, orderby_clause, groupby_clause
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 66:3: -> ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                    {
                        // JPA.g:66:6: ^( T_QUERY[$lp, $rp] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);

                        // JPA.g:66:37: ( select_clause )?
                        if ( stream_select_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_select_clause.nextTree());

                        }
                        stream_select_clause.reset();
                        adaptor.addChild(root_1, stream_from_clause.nextTree());
                        // JPA.g:66:66: ( where_clause )?
                        if ( stream_where_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_where_clause.nextTree());

                        }
                        stream_where_clause.reset();
                        // JPA.g:66:82: ( groupby_clause )?
                        if ( stream_groupby_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                        }
                        stream_groupby_clause.reset();
                        // JPA.g:66:100: ( having_clause )?
                        if ( stream_having_clause.hasNext() ) {
                            adaptor.addChild(root_1, stream_having_clause.nextTree());

                        }
                        stream_having_clause.reset();
                        // JPA.g:66:117: ( orderby_clause )?
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
    // JPA.g:69:1: join : join_spec join_association_path_expression ( 'AS' )? identification_variable -> ^( T_JOIN_VAR[$identification_variable.text] join_association_path_expression ) ;
    public final JPAParser.join_return join() throws RecognitionException {
        JPAParser.join_return retval = new JPAParser.join_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal28=null;
        JPAParser.join_spec_return join_spec26 = null;

        JPAParser.join_association_path_expression_return join_association_path_expression27 = null;

        JPAParser.identification_variable_return identification_variable29 = null;


        Object string_literal28_tree=null;
        RewriteRuleTokenStream stream_25=new RewriteRuleTokenStream(adaptor,"token 25");
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
        RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");
        try {
            // JPA.g:70:2: ( join_spec join_association_path_expression ( 'AS' )? identification_variable -> ^( T_JOIN_VAR[$identification_variable.text] join_association_path_expression ) )
            // JPA.g:70:4: join_spec join_association_path_expression ( 'AS' )? identification_variable
            {
            pushFollow(FOLLOW_join_spec_in_join381);
            join_spec26=join_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_spec.add(join_spec26.getTree());
            pushFollow(FOLLOW_join_association_path_expression_in_join383);
            join_association_path_expression27=join_association_path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression27.getTree());
            // JPA.g:70:47: ( 'AS' )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==25) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // JPA.g:70:48: 'AS'
                    {
                    string_literal28=(Token)match(input,25,FOLLOW_25_in_join386); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_25.add(string_literal28);


                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_join390);
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
            // 71:4: -> ^( T_JOIN_VAR[$identification_variable.text] join_association_path_expression )
            {
                // JPA.g:71:7: ^( T_JOIN_VAR[$identification_variable.text] join_association_path_expression )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new JoinVariableNode(T_JOIN_VAR, (identification_variable29!=null?input.toString(identification_variable29.start,identification_variable29.stop):null)), root_1);

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
    // JPA.g:74:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
    public final JPAParser.fetch_join_return fetch_join() throws RecognitionException {
        JPAParser.fetch_join_return retval = new JPAParser.fetch_join_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal31=null;
        JPAParser.join_spec_return join_spec30 = null;

        JPAParser.join_association_path_expression_return join_association_path_expression32 = null;


        Object string_literal31_tree=null;

        try {
            // JPA.g:75:2: ( join_spec 'FETCH' join_association_path_expression )
            // JPA.g:75:4: join_spec 'FETCH' join_association_path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_join_spec_in_fetch_join419);
            join_spec30=join_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec30.getTree());
            string_literal31=(Token)match(input,28,FOLLOW_28_in_fetch_join421); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal31_tree = (Object)adaptor.create(string_literal31);
            adaptor.addChild(root_0, string_literal31_tree);
            }
            pushFollow(FOLLOW_join_association_path_expression_in_fetch_join423);
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
    // JPA.g:77:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
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
            // JPA.g:78:2: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
            // JPA.g:78:3: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:78:3: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
            int alt16=3;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==29) ) {
                alt16=1;
            }
            else if ( (LA16_0==31) ) {
                alt16=2;
            }
            switch (alt16) {
                case 1 :
                    // JPA.g:78:4: ( 'LEFT' ) ( 'OUTER' )?
                    {
                    // JPA.g:78:4: ( 'LEFT' )
                    // JPA.g:78:5: 'LEFT'
                    {
                    string_literal33=(Token)match(input,29,FOLLOW_29_in_join_spec433); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal33_tree = (Object)adaptor.create(string_literal33);
                    adaptor.addChild(root_0, string_literal33_tree);
                    }

                    }

                    // JPA.g:78:13: ( 'OUTER' )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==30) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // JPA.g:78:14: 'OUTER'
                            {
                            string_literal34=(Token)match(input,30,FOLLOW_30_in_join_spec437); if (state.failed) return retval;
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
                    // JPA.g:78:26: 'INNER'
                    {
                    string_literal35=(Token)match(input,31,FOLLOW_31_in_join_spec443); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal35_tree = (Object)adaptor.create(string_literal35);
                    adaptor.addChild(root_0, string_literal35_tree);
                    }

                    }
                    break;

            }

            string_literal36=(Token)match(input,32,FOLLOW_32_in_join_spec448); if (state.failed) return retval;
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
    // JPA.g:80:1: join_association_path_expression : join_field_path -> ^( T_SELECTED_FIELD[$join_field_path.text] ) ;
    public final JPAParser.join_association_path_expression_return join_association_path_expression() throws RecognitionException {
        JPAParser.join_association_path_expression_return retval = new JPAParser.join_association_path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.join_field_path_return join_field_path37 = null;


        RewriteRuleSubtreeStream stream_join_field_path=new RewriteRuleSubtreeStream(adaptor,"rule join_field_path");
        try {
            // JPA.g:81:2: ( join_field_path -> ^( T_SELECTED_FIELD[$join_field_path.text] ) )
            // JPA.g:81:4: join_field_path
            {
            pushFollow(FOLLOW_join_field_path_in_join_association_path_expression457);
            join_field_path37=join_field_path();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_join_field_path.add(join_field_path37.getTree());


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
            // 82:2: -> ^( T_SELECTED_FIELD[$join_field_path.text] )
            {
                // JPA.g:82:5: ^( T_SELECTED_FIELD[$join_field_path.text] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (join_field_path37!=null?input.toString(join_field_path37.start,join_field_path37.stop):null)), root_1);

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
    // JPA.g:84:1: collection_member_declaration : 'IN' '(' path_expression ')' ( 'AS' )? identification_variable ;
    public final JPAParser.collection_member_declaration_return collection_member_declaration() throws RecognitionException {
        JPAParser.collection_member_declaration_return retval = new JPAParser.collection_member_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal38=null;
        Token char_literal39=null;
        Token char_literal41=null;
        Token string_literal42=null;
        JPAParser.path_expression_return path_expression40 = null;

        JPAParser.identification_variable_return identification_variable43 = null;


        Object string_literal38_tree=null;
        Object char_literal39_tree=null;
        Object char_literal41_tree=null;
        Object string_literal42_tree=null;

        try {
            // JPA.g:85:2: ( 'IN' '(' path_expression ')' ( 'AS' )? identification_variable )
            // JPA.g:85:4: 'IN' '(' path_expression ')' ( 'AS' )? identification_variable
            {
            root_0 = (Object)adaptor.nil();

            string_literal38=(Token)match(input,33,FOLLOW_33_in_collection_member_declaration477); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal38_tree = (Object)adaptor.create(string_literal38);
            adaptor.addChild(root_0, string_literal38_tree);
            }
            char_literal39=(Token)match(input,34,FOLLOW_34_in_collection_member_declaration478); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal39_tree = (Object)adaptor.create(char_literal39);
            adaptor.addChild(root_0, char_literal39_tree);
            }
            pushFollow(FOLLOW_path_expression_in_collection_member_declaration480);
            path_expression40=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression40.getTree());
            char_literal41=(Token)match(input,27,FOLLOW_27_in_collection_member_declaration482); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal41_tree = (Object)adaptor.create(char_literal41);
            adaptor.addChild(root_0, char_literal41_tree);
            }
            // JPA.g:85:32: ( 'AS' )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==25) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // JPA.g:85:33: 'AS'
                    {
                    string_literal42=(Token)match(input,25,FOLLOW_25_in_collection_member_declaration485); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal42_tree = (Object)adaptor.create(string_literal42);
                    adaptor.addChild(root_0, string_literal42_tree);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_identification_variable_in_collection_member_declaration489);
            identification_variable43=identification_variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable43.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
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
    // JPA.g:87:1: path_expression : field_path -> ^( T_SELECTED_FIELD[$field_path.text] ) ;
    public final JPAParser.path_expression_return path_expression() throws RecognitionException {
        JPAParser.path_expression_return retval = new JPAParser.path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.field_path_return field_path44 = null;


        RewriteRuleSubtreeStream stream_field_path=new RewriteRuleSubtreeStream(adaptor,"rule field_path");
        try {
            // JPA.g:88:2: ( field_path -> ^( T_SELECTED_FIELD[$field_path.text] ) )
            // JPA.g:88:5: field_path
            {
            pushFollow(FOLLOW_field_path_in_path_expression499);
            field_path44=field_path();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_field_path.add(field_path44.getTree());


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
            // 89:2: -> ^( T_SELECTED_FIELD[$field_path.text] )
            {
                // JPA.g:89:5: ^( T_SELECTED_FIELD[$field_path.text] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (field_path44!=null?input.toString(field_path44.start,field_path44.stop):null)), root_1);

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
    // JPA.g:92:1: select_clause : ( 'DISTINCT' )? select_expression ( ',' select_expression )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( select_expression )* ) ;
    public final JPAParser.select_clause_return select_clause() throws RecognitionException {
        JPAParser.select_clause_return retval = new JPAParser.select_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal45=null;
        Token char_literal47=null;
        JPAParser.select_expression_return select_expression46 = null;

        JPAParser.select_expression_return select_expression48 = null;


        Object string_literal45_tree=null;
        Object char_literal47_tree=null;
        RewriteRuleTokenStream stream_35=new RewriteRuleTokenStream(adaptor,"token 35");
        RewriteRuleTokenStream stream_24=new RewriteRuleTokenStream(adaptor,"token 24");
        RewriteRuleSubtreeStream stream_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule select_expression");
        try {
            // JPA.g:93:2: ( ( 'DISTINCT' )? select_expression ( ',' select_expression )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( select_expression )* ) )
            // JPA.g:93:4: ( 'DISTINCT' )? select_expression ( ',' select_expression )*
            {
            // JPA.g:93:4: ( 'DISTINCT' )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==35) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // JPA.g:93:5: 'DISTINCT'
                    {
                    string_literal45=(Token)match(input,35,FOLLOW_35_in_select_clause522); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_35.add(string_literal45);


                    }
                    break;

            }

            pushFollow(FOLLOW_select_expression_in_select_clause526);
            select_expression46=select_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_select_expression.add(select_expression46.getTree());
            // JPA.g:93:36: ( ',' select_expression )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==24) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // JPA.g:93:37: ',' select_expression
            	    {
            	    char_literal47=(Token)match(input,24,FOLLOW_24_in_select_clause529); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_24.add(char_literal47);

            	    pushFollow(FOLLOW_select_expression_in_select_clause531);
            	    select_expression48=select_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_select_expression.add(select_expression48.getTree());

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);



            // AST REWRITE
            // elements: select_expression, 35
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 93:61: -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( select_expression )* )
            {
                // JPA.g:93:64: ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( select_expression )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);

                // JPA.g:93:83: ( 'DISTINCT' )?
                if ( stream_35.hasNext() ) {
                    adaptor.addChild(root_1, stream_35.nextNode());

                }
                stream_35.reset();
                // JPA.g:93:97: ( select_expression )*
                while ( stream_select_expression.hasNext() ) {
                    adaptor.addChild(root_1, stream_select_expression.nextTree());

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
    // JPA.g:96:1: select_expression : ( path_expression | aggregate_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | 'OBJECT' '(' identification_variable ')' | constructor_expression );
    public final JPAParser.select_expression_return select_expression() throws RecognitionException {
        JPAParser.select_expression_return retval = new JPAParser.select_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal52=null;
        Token char_literal53=null;
        Token char_literal55=null;
        JPAParser.path_expression_return path_expression49 = null;

        JPAParser.aggregate_expression_return aggregate_expression50 = null;

        JPAParser.identification_variable_return identification_variable51 = null;

        JPAParser.identification_variable_return identification_variable54 = null;

        JPAParser.constructor_expression_return constructor_expression56 = null;


        Object string_literal52_tree=null;
        Object char_literal53_tree=null;
        Object char_literal55_tree=null;
        RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
        try {
            // JPA.g:97:2: ( path_expression | aggregate_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | 'OBJECT' '(' identification_variable ')' | constructor_expression )
            int alt20=5;
            switch ( input.LA(1) ) {
            case SIMPLE_FIELD_PATH:
            case FIELD_PATH:
                {
                alt20=1;
                }
                break;
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
                {
                alt20=2;
                }
                break;
            case WORD:
                {
                alt20=3;
                }
                break;
            case 36:
                {
                alt20=4;
                }
                break;
            case 37:
                {
                alt20=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // JPA.g:97:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_select_expression560);
                    path_expression49=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression49.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:98:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_select_expression565);
                    aggregate_expression50=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression50.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:99:4: identification_variable
                    {
                    pushFollow(FOLLOW_identification_variable_in_select_expression570);
                    identification_variable51=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable51.getTree());


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
                    // 99:28: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
                    {
                        // JPA.g:99:31: ^( T_SELECTED_ENTITY[$identification_variable.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_ENTITY, (identification_variable51!=null?input.toString(identification_variable51.start,identification_variable51.stop):null)), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // JPA.g:100:4: 'OBJECT' '(' identification_variable ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal52=(Token)match(input,36,FOLLOW_36_in_select_expression585); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal52_tree = (Object)adaptor.create(string_literal52);
                    adaptor.addChild(root_0, string_literal52_tree);
                    }
                    char_literal53=(Token)match(input,34,FOLLOW_34_in_select_expression587); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal53_tree = (Object)adaptor.create(char_literal53);
                    adaptor.addChild(root_0, char_literal53_tree);
                    }
                    pushFollow(FOLLOW_identification_variable_in_select_expression588);
                    identification_variable54=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable54.getTree());
                    char_literal55=(Token)match(input,27,FOLLOW_27_in_select_expression589); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal55_tree = (Object)adaptor.create(char_literal55);
                    adaptor.addChild(root_0, char_literal55_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:101:4: constructor_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_constructor_expression_in_select_expression594);
                    constructor_expression56=constructor_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_expression56.getTree());

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
    // JPA.g:103:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
    public final JPAParser.constructor_expression_return constructor_expression() throws RecognitionException {
        JPAParser.constructor_expression_return retval = new JPAParser.constructor_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal57=null;
        Token char_literal59=null;
        Token char_literal61=null;
        Token char_literal63=null;
        JPAParser.constructor_name_return constructor_name58 = null;

        JPAParser.constructor_item_return constructor_item60 = null;

        JPAParser.constructor_item_return constructor_item62 = null;


        Object string_literal57_tree=null;
        Object char_literal59_tree=null;
        Object char_literal61_tree=null;
        Object char_literal63_tree=null;

        try {
            // JPA.g:104:2: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
            // JPA.g:104:4: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
            {
            root_0 = (Object)adaptor.nil();

            string_literal57=(Token)match(input,37,FOLLOW_37_in_constructor_expression603); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal57_tree = (Object)adaptor.create(string_literal57);
            adaptor.addChild(root_0, string_literal57_tree);
            }
            pushFollow(FOLLOW_constructor_name_in_constructor_expression605);
            constructor_name58=constructor_name();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name58.getTree());
            char_literal59=(Token)match(input,34,FOLLOW_34_in_constructor_expression607); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal59_tree = (Object)adaptor.create(char_literal59);
            adaptor.addChild(root_0, char_literal59_tree);
            }
            pushFollow(FOLLOW_constructor_item_in_constructor_expression609);
            constructor_item60=constructor_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item60.getTree());
            // JPA.g:104:48: ( ',' constructor_item )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==24) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // JPA.g:104:49: ',' constructor_item
            	    {
            	    char_literal61=(Token)match(input,24,FOLLOW_24_in_constructor_expression612); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal61_tree = (Object)adaptor.create(char_literal61);
            	    adaptor.addChild(root_0, char_literal61_tree);
            	    }
            	    pushFollow(FOLLOW_constructor_item_in_constructor_expression614);
            	    constructor_item62=constructor_item();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item62.getTree());

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);

            char_literal63=(Token)match(input,27,FOLLOW_27_in_constructor_expression618); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal63_tree = (Object)adaptor.create(char_literal63);
            adaptor.addChild(root_0, char_literal63_tree);
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
    // JPA.g:106:1: constructor_item : ( path_expression | aggregate_expression );
    public final JPAParser.constructor_item_return constructor_item() throws RecognitionException {
        JPAParser.constructor_item_return retval = new JPAParser.constructor_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression64 = null;

        JPAParser.aggregate_expression_return aggregate_expression65 = null;



        try {
            // JPA.g:107:2: ( path_expression | aggregate_expression )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( ((LA22_0>=SIMPLE_FIELD_PATH && LA22_0<=FIELD_PATH)) ) {
                alt22=1;
            }
            else if ( ((LA22_0>=38 && LA22_0<=42)) ) {
                alt22=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // JPA.g:107:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_constructor_item627);
                    path_expression64=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression64.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:107:22: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_constructor_item631);
                    aggregate_expression65=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression65.getTree());

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
    // JPA.g:109:1: aggregate_expression : ( ( 'AVG' | 'MAX' | 'MIN' | 'SUM' ) '(' ( 'DISTINCT' )? path_expression ')' | 'COUNT' '(' ( 'DISTINCT' )? ( identification_variable | path_expression ) ')' );
    public final JPAParser.aggregate_expression_return aggregate_expression() throws RecognitionException {
        JPAParser.aggregate_expression_return retval = new JPAParser.aggregate_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set66=null;
        Token char_literal67=null;
        Token string_literal68=null;
        Token char_literal70=null;
        Token string_literal71=null;
        Token char_literal72=null;
        Token string_literal73=null;
        Token char_literal76=null;
        JPAParser.path_expression_return path_expression69 = null;

        JPAParser.identification_variable_return identification_variable74 = null;

        JPAParser.path_expression_return path_expression75 = null;


        Object set66_tree=null;
        Object char_literal67_tree=null;
        Object string_literal68_tree=null;
        Object char_literal70_tree=null;
        Object string_literal71_tree=null;
        Object char_literal72_tree=null;
        Object string_literal73_tree=null;
        Object char_literal76_tree=null;

        try {
            // JPA.g:110:2: ( ( 'AVG' | 'MAX' | 'MIN' | 'SUM' ) '(' ( 'DISTINCT' )? path_expression ')' | 'COUNT' '(' ( 'DISTINCT' )? ( identification_variable | path_expression ) ')' )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( ((LA26_0>=38 && LA26_0<=41)) ) {
                alt26=1;
            }
            else if ( (LA26_0==42) ) {
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
                    // JPA.g:110:4: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' ) '(' ( 'DISTINCT' )? path_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    set66=(Token)input.LT(1);
                    if ( (input.LA(1)>=38 && input.LA(1)<=41) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set66));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    char_literal67=(Token)match(input,34,FOLLOW_34_in_aggregate_expression657); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal67_tree = (Object)adaptor.create(char_literal67);
                    adaptor.addChild(root_0, char_literal67_tree);
                    }
                    // JPA.g:110:41: ( 'DISTINCT' )?
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0==35) ) {
                        alt23=1;
                    }
                    switch (alt23) {
                        case 1 :
                            // JPA.g:110:42: 'DISTINCT'
                            {
                            string_literal68=(Token)match(input,35,FOLLOW_35_in_aggregate_expression660); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal68_tree = (Object)adaptor.create(string_literal68);
                            adaptor.addChild(root_0, string_literal68_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_path_expression_in_aggregate_expression664);
                    path_expression69=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression69.getTree());
                    char_literal70=(Token)match(input,27,FOLLOW_27_in_aggregate_expression665); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal70_tree = (Object)adaptor.create(char_literal70);
                    adaptor.addChild(root_0, char_literal70_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:111:4: 'COUNT' '(' ( 'DISTINCT' )? ( identification_variable | path_expression ) ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal71=(Token)match(input,42,FOLLOW_42_in_aggregate_expression670); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal71_tree = (Object)adaptor.create(string_literal71);
                    adaptor.addChild(root_0, string_literal71_tree);
                    }
                    char_literal72=(Token)match(input,34,FOLLOW_34_in_aggregate_expression672); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal72_tree = (Object)adaptor.create(char_literal72);
                    adaptor.addChild(root_0, char_literal72_tree);
                    }
                    // JPA.g:111:16: ( 'DISTINCT' )?
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==35) ) {
                        alt24=1;
                    }
                    switch (alt24) {
                        case 1 :
                            // JPA.g:111:17: 'DISTINCT'
                            {
                            string_literal73=(Token)match(input,35,FOLLOW_35_in_aggregate_expression675); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal73_tree = (Object)adaptor.create(string_literal73);
                            adaptor.addChild(root_0, string_literal73_tree);
                            }

                            }
                            break;

                    }

                    // JPA.g:111:30: ( identification_variable | path_expression )
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( (LA25_0==WORD) ) {
                        alt25=1;
                    }
                    else if ( ((LA25_0>=SIMPLE_FIELD_PATH && LA25_0<=FIELD_PATH)) ) {
                        alt25=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 25, 0, input);

                        throw nvae;
                    }
                    switch (alt25) {
                        case 1 :
                            // JPA.g:111:31: identification_variable
                            {
                            pushFollow(FOLLOW_identification_variable_in_aggregate_expression680);
                            identification_variable74=identification_variable();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable74.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:111:57: path_expression
                            {
                            pushFollow(FOLLOW_path_expression_in_aggregate_expression684);
                            path_expression75=path_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression75.getTree());

                            }
                            break;

                    }

                    char_literal76=(Token)match(input,27,FOLLOW_27_in_aggregate_expression687); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal76_tree = (Object)adaptor.create(char_literal76);
                    adaptor.addChild(root_0, char_literal76_tree);
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
    // $ANTLR end "aggregate_expression"

    public static class where_clause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "where_clause"
    // JPA.g:113:1: where_clause : ( 'WHERE' conditional_expression -> ^( T_CONDITION conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION path_expression ) );
    public final JPAParser.where_clause_return where_clause() throws RecognitionException {
        JPAParser.where_clause_return retval = new JPAParser.where_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal77=null;
        Token string_literal79=null;
        JPAParser.conditional_expression_return conditional_expression78 = null;

        JPAParser.path_expression_return path_expression80 = null;


        Object string_literal77_tree=null;
        Object string_literal79_tree=null;
        RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");
        RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");
        RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
        try {
            // JPA.g:114:2: ( 'WHERE' conditional_expression -> ^( T_CONDITION conditional_expression ) | 'WHERE' path_expression -> ^( T_CONDITION path_expression ) )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==43) ) {
                int LA27_1 = input.LA(2);

                if ( ((LA27_1>=SIMPLE_FIELD_PATH && LA27_1<=FIELD_PATH)) ) {
                    int LA27_2 = input.LA(3);

                    if ( (LA27_2==33||(LA27_2>=52 && LA27_2<=54)||LA27_2==56||LA27_2==59||(LA27_2>=65 && LA27_2<=74)) ) {
                        alt27=1;
                    }
                    else if ( (LA27_2==EOF||LA27_2==27||LA27_2==44||(LA27_2>=46 && LA27_2<=47)) ) {
                        alt27=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 27, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA27_1==STRINGLITERAL||(LA27_1>=WORD && LA27_1<=INT_NUMERAL)||LA27_1==26||LA27_1==34||(LA27_1>=38 && LA27_1<=42)||LA27_1==52||LA27_1==61||(LA27_1>=71 && LA27_1<=72)||(LA27_1>=75 && LA27_1<=88)||(LA27_1>=92 && LA27_1<=96)) ) {
                    alt27=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 27, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // JPA.g:114:4: 'WHERE' conditional_expression
                    {
                    string_literal77=(Token)match(input,43,FOLLOW_43_in_where_clause696); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_43.add(string_literal77);

                    pushFollow(FOLLOW_conditional_expression_in_where_clause698);
                    conditional_expression78=conditional_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression78.getTree());


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
                    // 114:34: -> ^( T_CONDITION conditional_expression )
                    {
                        // JPA.g:114:37: ^( T_CONDITION conditional_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_CONDITION, "T_CONDITION"), root_1);

                        adaptor.addChild(root_1, stream_conditional_expression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // JPA.g:115:4: 'WHERE' path_expression
                    {
                    string_literal79=(Token)match(input,43,FOLLOW_43_in_where_clause710); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_43.add(string_literal79);

                    pushFollow(FOLLOW_path_expression_in_where_clause712);
                    path_expression80=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_path_expression.add(path_expression80.getTree());


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
                    // 115:28: -> ^( T_CONDITION path_expression )
                    {
                        // JPA.g:115:31: ^( T_CONDITION path_expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_CONDITION, "T_CONDITION"), root_1);

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
    // JPA.g:117:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* ;
    public final JPAParser.groupby_clause_return groupby_clause() throws RecognitionException {
        JPAParser.groupby_clause_return retval = new JPAParser.groupby_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal81=null;
        Token string_literal82=null;
        Token char_literal84=null;
        JPAParser.groupby_item_return groupby_item83 = null;

        JPAParser.groupby_item_return groupby_item85 = null;


        Object string_literal81_tree=null;
        Object string_literal82_tree=null;
        Object char_literal84_tree=null;

        try {
            // JPA.g:118:2: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* )
            // JPA.g:118:4: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
            {
            root_0 = (Object)adaptor.nil();

            string_literal81=(Token)match(input,44,FOLLOW_44_in_groupby_clause729); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal81_tree = (Object)adaptor.create(string_literal81);
            adaptor.addChild(root_0, string_literal81_tree);
            }
            string_literal82=(Token)match(input,45,FOLLOW_45_in_groupby_clause731); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal82_tree = (Object)adaptor.create(string_literal82);
            adaptor.addChild(root_0, string_literal82_tree);
            }
            pushFollow(FOLLOW_groupby_item_in_groupby_clause733);
            groupby_item83=groupby_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, groupby_item83.getTree());
            // JPA.g:118:30: ( ',' groupby_item )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0==24) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // JPA.g:118:31: ',' groupby_item
            	    {
            	    char_literal84=(Token)match(input,24,FOLLOW_24_in_groupby_clause736); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal84_tree = (Object)adaptor.create(char_literal84);
            	    adaptor.addChild(root_0, char_literal84_tree);
            	    }
            	    pushFollow(FOLLOW_groupby_item_in_groupby_clause738);
            	    groupby_item85=groupby_item();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, groupby_item85.getTree());

            	    }
            	    break;

            	default :
            	    break loop28;
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
    // $ANTLR end "groupby_clause"

    public static class groupby_item_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "groupby_item"
    // JPA.g:120:1: groupby_item : ( path_expression | identification_variable );
    public final JPAParser.groupby_item_return groupby_item() throws RecognitionException {
        JPAParser.groupby_item_return retval = new JPAParser.groupby_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression86 = null;

        JPAParser.identification_variable_return identification_variable87 = null;



        try {
            // JPA.g:121:2: ( path_expression | identification_variable )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( ((LA29_0>=SIMPLE_FIELD_PATH && LA29_0<=FIELD_PATH)) ) {
                alt29=1;
            }
            else if ( (LA29_0==WORD) ) {
                alt29=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }
            switch (alt29) {
                case 1 :
                    // JPA.g:121:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_groupby_item749);
                    path_expression86=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression86.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:121:22: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_groupby_item753);
                    identification_variable87=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable87.getTree());

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
    // JPA.g:123:1: having_clause : 'HAVING' conditional_expression ;
    public final JPAParser.having_clause_return having_clause() throws RecognitionException {
        JPAParser.having_clause_return retval = new JPAParser.having_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal88=null;
        JPAParser.conditional_expression_return conditional_expression89 = null;


        Object string_literal88_tree=null;

        try {
            // JPA.g:124:2: ( 'HAVING' conditional_expression )
            // JPA.g:124:4: 'HAVING' conditional_expression
            {
            root_0 = (Object)adaptor.nil();

            string_literal88=(Token)match(input,46,FOLLOW_46_in_having_clause762); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal88_tree = (Object)adaptor.create(string_literal88);
            adaptor.addChild(root_0, string_literal88_tree);
            }
            pushFollow(FOLLOW_conditional_expression_in_having_clause764);
            conditional_expression89=conditional_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression89.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
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
    // JPA.g:126:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* ;
    public final JPAParser.orderby_clause_return orderby_clause() throws RecognitionException {
        JPAParser.orderby_clause_return retval = new JPAParser.orderby_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal90=null;
        Token string_literal91=null;
        Token char_literal93=null;
        JPAParser.orderby_item_return orderby_item92 = null;

        JPAParser.orderby_item_return orderby_item94 = null;


        Object string_literal90_tree=null;
        Object string_literal91_tree=null;
        Object char_literal93_tree=null;

        try {
            // JPA.g:127:2: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* )
            // JPA.g:127:4: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
            {
            root_0 = (Object)adaptor.nil();

            string_literal90=(Token)match(input,47,FOLLOW_47_in_orderby_clause773); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal90_tree = (Object)adaptor.create(string_literal90);
            adaptor.addChild(root_0, string_literal90_tree);
            }
            string_literal91=(Token)match(input,45,FOLLOW_45_in_orderby_clause775); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal91_tree = (Object)adaptor.create(string_literal91);
            adaptor.addChild(root_0, string_literal91_tree);
            }
            pushFollow(FOLLOW_orderby_item_in_orderby_clause777);
            orderby_item92=orderby_item();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, orderby_item92.getTree());
            // JPA.g:127:30: ( ',' orderby_item )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( (LA30_0==24) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // JPA.g:127:31: ',' orderby_item
            	    {
            	    char_literal93=(Token)match(input,24,FOLLOW_24_in_orderby_clause780); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal93_tree = (Object)adaptor.create(char_literal93);
            	    adaptor.addChild(root_0, char_literal93_tree);
            	    }
            	    pushFollow(FOLLOW_orderby_item_in_orderby_clause782);
            	    orderby_item94=orderby_item();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, orderby_item94.getTree());

            	    }
            	    break;

            	default :
            	    break loop30;
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
    // $ANTLR end "orderby_clause"

    public static class orderby_item_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "orderby_item"
    // JPA.g:129:1: orderby_item : path_expression ( 'ASC' | 'DESC' )? ;
    public final JPAParser.orderby_item_return orderby_item() throws RecognitionException {
        JPAParser.orderby_item_return retval = new JPAParser.orderby_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set96=null;
        JPAParser.path_expression_return path_expression95 = null;


        Object set96_tree=null;

        try {
            // JPA.g:130:2: ( path_expression ( 'ASC' | 'DESC' )? )
            // JPA.g:130:4: path_expression ( 'ASC' | 'DESC' )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_orderby_item793);
            path_expression95=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression95.getTree());
            // JPA.g:130:20: ( 'ASC' | 'DESC' )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( ((LA31_0>=48 && LA31_0<=49)) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // JPA.g:
                    {
                    set96=(Token)input.LT(1);
                    if ( (input.LA(1)>=48 && input.LA(1)<=49) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set96));
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


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
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
    // JPA.g:132:1: subquery : lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
    public final JPAParser.subquery_return subquery() throws RecognitionException {
        JPAParser.subquery_return retval = new JPAParser.subquery_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token lp=null;
        Token rp=null;
        JPAParser.simple_select_clause_return simple_select_clause97 = null;

        JPAParser.subquery_from_clause_return subquery_from_clause98 = null;

        JPAParser.where_clause_return where_clause99 = null;

        JPAParser.groupby_clause_return groupby_clause100 = null;

        JPAParser.having_clause_return having_clause101 = null;


        Object lp_tree=null;
        Object rp_tree=null;
        RewriteRuleTokenStream stream_26=new RewriteRuleTokenStream(adaptor,"token 26");
        RewriteRuleTokenStream stream_27=new RewriteRuleTokenStream(adaptor,"token 27");
        RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
        RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
        RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
        RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
        RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
        try {
            // JPA.g:133:2: (lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
            // JPA.g:133:4: lp= '(SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
            {
            lp=(Token)match(input,26,FOLLOW_26_in_subquery813); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_26.add(lp);

            pushFollow(FOLLOW_simple_select_clause_in_subquery815);
            simple_select_clause97=simple_select_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause97.getTree());
            pushFollow(FOLLOW_subquery_from_clause_in_subquery817);
            subquery_from_clause98=subquery_from_clause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause98.getTree());
            // JPA.g:133:59: ( where_clause )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==43) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // JPA.g:133:60: where_clause
                    {
                    pushFollow(FOLLOW_where_clause_in_subquery820);
                    where_clause99=where_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_where_clause.add(where_clause99.getTree());

                    }
                    break;

            }

            // JPA.g:133:75: ( groupby_clause )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==44) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // JPA.g:133:76: groupby_clause
                    {
                    pushFollow(FOLLOW_groupby_clause_in_subquery825);
                    groupby_clause100=groupby_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause100.getTree());

                    }
                    break;

            }

            // JPA.g:133:93: ( having_clause )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==46) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // JPA.g:133:94: having_clause
                    {
                    pushFollow(FOLLOW_having_clause_in_subquery830);
                    having_clause101=having_clause();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_having_clause.add(having_clause101.getTree());

                    }
                    break;

            }

            rp=(Token)match(input,27,FOLLOW_27_in_subquery836); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_27.add(rp);



            // AST REWRITE
            // elements: having_clause, groupby_clause, subquery_from_clause, where_clause, simple_select_clause
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 134:3: -> ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
            {
                // JPA.g:134:6: ^( T_QUERY[$lp,$rp] simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);

                adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
                adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
                // JPA.g:134:78: ( where_clause )?
                if ( stream_where_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_where_clause.nextTree());

                }
                stream_where_clause.reset();
                // JPA.g:134:94: ( groupby_clause )?
                if ( stream_groupby_clause.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupby_clause.nextTree());

                }
                stream_groupby_clause.reset();
                // JPA.g:134:112: ( having_clause )?
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
    // JPA.g:136:1: subquery_from_clause : 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES ( subselect_identification_variable_declaration )* ) ;
    public final JPAParser.subquery_from_clause_return subquery_from_clause() throws RecognitionException {
        JPAParser.subquery_from_clause_return retval = new JPAParser.subquery_from_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal102=null;
        Token char_literal104=null;
        JPAParser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration103 = null;

        JPAParser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration105 = null;


        Object string_literal102_tree=null;
        Object char_literal104_tree=null;
        RewriteRuleTokenStream stream_23=new RewriteRuleTokenStream(adaptor,"token 23");
        RewriteRuleTokenStream stream_24=new RewriteRuleTokenStream(adaptor,"token 24");
        RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");
        try {
            // JPA.g:137:2: ( 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES ( subselect_identification_variable_declaration )* ) )
            // JPA.g:137:4: 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
            {
            string_literal102=(Token)match(input,23,FOLLOW_23_in_subquery_from_clause877); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_23.add(string_literal102);

            pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause879);
            subselect_identification_variable_declaration103=subselect_identification_variable_declaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration103.getTree());
            // JPA.g:137:57: ( ',' subselect_identification_variable_declaration )*
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==24) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // JPA.g:137:58: ',' subselect_identification_variable_declaration
            	    {
            	    char_literal104=(Token)match(input,24,FOLLOW_24_in_subquery_from_clause882); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_24.add(char_literal104);

            	    pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause884);
            	    subselect_identification_variable_declaration105=subselect_identification_variable_declaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration105.getTree());

            	    }
            	    break;

            	default :
            	    break loop35;
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
            // 138:2: -> ^( T_SOURCES ( subselect_identification_variable_declaration )* )
            {
                // JPA.g:138:5: ^( T_SOURCES ( subselect_identification_variable_declaration )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SOURCES, "T_SOURCES"), root_1);

                // JPA.g:138:17: ( subselect_identification_variable_declaration )*
                while ( stream_subselect_identification_variable_declaration.hasNext() ) {
                    adaptor.addChild(root_1, stream_subselect_identification_variable_declaration.nextTree());

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
    // JPA.g:140:1: subselect_identification_variable_declaration : ( identification_variable_declaration | association_path_expression ( 'AS' )? identification_variable | collection_member_declaration );
    public final JPAParser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration() throws RecognitionException {
        JPAParser.subselect_identification_variable_declaration_return retval = new JPAParser.subselect_identification_variable_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal108=null;
        JPAParser.identification_variable_declaration_return identification_variable_declaration106 = null;

        JPAParser.association_path_expression_return association_path_expression107 = null;

        JPAParser.identification_variable_return identification_variable109 = null;

        JPAParser.collection_member_declaration_return collection_member_declaration110 = null;


        Object string_literal108_tree=null;

        try {
            // JPA.g:141:2: ( identification_variable_declaration | association_path_expression ( 'AS' )? identification_variable | collection_member_declaration )
            int alt37=3;
            switch ( input.LA(1) ) {
            case WORD:
            case 26:
                {
                alt37=1;
                }
                break;
            case SIMPLE_FIELD_PATH:
            case FIELD_PATH:
                {
                alt37=2;
                }
                break;
            case 33:
                {
                alt37=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;
            }

            switch (alt37) {
                case 1 :
                    // JPA.g:141:4: identification_variable_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration905);
                    identification_variable_declaration106=identification_variable_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration106.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:142:4: association_path_expression ( 'AS' )? identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_association_path_expression_in_subselect_identification_variable_declaration910);
                    association_path_expression107=association_path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, association_path_expression107.getTree());
                    // JPA.g:142:32: ( 'AS' )?
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( (LA36_0==25) ) {
                        alt36=1;
                    }
                    switch (alt36) {
                        case 1 :
                            // JPA.g:142:33: 'AS'
                            {
                            string_literal108=(Token)match(input,25,FOLLOW_25_in_subselect_identification_variable_declaration913); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal108_tree = (Object)adaptor.create(string_literal108);
                            adaptor.addChild(root_0, string_literal108_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration917);
                    identification_variable109=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable109.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:143:4: collection_member_declaration
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_declaration_in_subselect_identification_variable_declaration922);
                    collection_member_declaration110=collection_member_declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_declaration110.getTree());

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
    // JPA.g:145:1: association_path_expression : path_expression ;
    public final JPAParser.association_path_expression_return association_path_expression() throws RecognitionException {
        JPAParser.association_path_expression_return retval = new JPAParser.association_path_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression111 = null;



        try {
            // JPA.g:146:2: ( path_expression )
            // JPA.g:146:4: path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_association_path_expression931);
            path_expression111=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression111.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
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
    // JPA.g:148:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? simple_select_expression ) ;
    public final JPAParser.simple_select_clause_return simple_select_clause() throws RecognitionException {
        JPAParser.simple_select_clause_return retval = new JPAParser.simple_select_clause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal112=null;
        JPAParser.simple_select_expression_return simple_select_expression113 = null;


        Object string_literal112_tree=null;
        RewriteRuleTokenStream stream_35=new RewriteRuleTokenStream(adaptor,"token 35");
        RewriteRuleSubtreeStream stream_simple_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_expression");
        try {
            // JPA.g:149:2: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? simple_select_expression ) )
            // JPA.g:149:4: ( 'DISTINCT' )? simple_select_expression
            {
            // JPA.g:149:4: ( 'DISTINCT' )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==35) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // JPA.g:149:5: 'DISTINCT'
                    {
                    string_literal112=(Token)match(input,35,FOLLOW_35_in_simple_select_clause941); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_35.add(string_literal112);


                    }
                    break;

            }

            pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause945);
            simple_select_expression113=simple_select_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_simple_select_expression.add(simple_select_expression113.getTree());


            // AST REWRITE
            // elements: simple_select_expression, 35
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 150:2: -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? simple_select_expression )
            {
                // JPA.g:150:5: ^( T_SELECTED_ITEMS ( 'DISTINCT' )? simple_select_expression )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);

                // JPA.g:150:24: ( 'DISTINCT' )?
                if ( stream_35.hasNext() ) {
                    adaptor.addChild(root_1, stream_35.nextNode());

                }
                stream_35.reset();
                adaptor.addChild(root_1, stream_simple_select_expression.nextTree());

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
    // JPA.g:152:1: simple_select_expression : ( path_expression | aggregate_expression | identification_variable );
    public final JPAParser.simple_select_expression_return simple_select_expression() throws RecognitionException {
        JPAParser.simple_select_expression_return retval = new JPAParser.simple_select_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression114 = null;

        JPAParser.aggregate_expression_return aggregate_expression115 = null;

        JPAParser.identification_variable_return identification_variable116 = null;



        try {
            // JPA.g:153:2: ( path_expression | aggregate_expression | identification_variable )
            int alt39=3;
            switch ( input.LA(1) ) {
            case SIMPLE_FIELD_PATH:
            case FIELD_PATH:
                {
                alt39=1;
                }
                break;
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
                {
                alt39=2;
                }
                break;
            case WORD:
                {
                alt39=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }

            switch (alt39) {
                case 1 :
                    // JPA.g:153:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_simple_select_expression969);
                    path_expression114=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression114.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:154:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression974);
                    aggregate_expression115=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression115.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:155:4: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_simple_select_expression979);
                    identification_variable116=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable116.getTree());

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
    // JPA.g:157:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
    public final JPAParser.conditional_expression_return conditional_expression() throws RecognitionException {
        JPAParser.conditional_expression_return retval = new JPAParser.conditional_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal118=null;
        JPAParser.conditional_term_return conditional_term117 = null;

        JPAParser.conditional_term_return conditional_term119 = null;


        Object string_literal118_tree=null;

        try {
            // JPA.g:158:2: ( ( conditional_term ) ( 'OR' conditional_term )* )
            // JPA.g:158:4: ( conditional_term ) ( 'OR' conditional_term )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:158:4: ( conditional_term )
            // JPA.g:158:5: conditional_term
            {
            pushFollow(FOLLOW_conditional_term_in_conditional_expression989);
            conditional_term117=conditional_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term117.getTree());

            }

            // JPA.g:158:23: ( 'OR' conditional_term )*
            loop40:
            do {
                int alt40=2;
                int LA40_0 = input.LA(1);

                if ( (LA40_0==50) ) {
                    alt40=1;
                }


                switch (alt40) {
            	case 1 :
            	    // JPA.g:158:24: 'OR' conditional_term
            	    {
            	    string_literal118=(Token)match(input,50,FOLLOW_50_in_conditional_expression993); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal118_tree = (Object)adaptor.create(string_literal118);
            	    adaptor.addChild(root_0, string_literal118_tree);
            	    }
            	    pushFollow(FOLLOW_conditional_term_in_conditional_expression995);
            	    conditional_term119=conditional_term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term119.getTree());

            	    }
            	    break;

            	default :
            	    break loop40;
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
    // JPA.g:160:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
    public final JPAParser.conditional_term_return conditional_term() throws RecognitionException {
        JPAParser.conditional_term_return retval = new JPAParser.conditional_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal121=null;
        JPAParser.conditional_factor_return conditional_factor120 = null;

        JPAParser.conditional_factor_return conditional_factor122 = null;


        Object string_literal121_tree=null;

        try {
            // JPA.g:161:2: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
            // JPA.g:161:4: ( conditional_factor ) ( 'AND' conditional_factor )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:161:4: ( conditional_factor )
            // JPA.g:161:5: conditional_factor
            {
            pushFollow(FOLLOW_conditional_factor_in_conditional_term1007);
            conditional_factor120=conditional_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor120.getTree());

            }

            // JPA.g:161:25: ( 'AND' conditional_factor )*
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( (LA41_0==51) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // JPA.g:161:26: 'AND' conditional_factor
            	    {
            	    string_literal121=(Token)match(input,51,FOLLOW_51_in_conditional_term1011); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal121_tree = (Object)adaptor.create(string_literal121);
            	    adaptor.addChild(root_0, string_literal121_tree);
            	    }
            	    pushFollow(FOLLOW_conditional_factor_in_conditional_term1013);
            	    conditional_factor122=conditional_factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor122.getTree());

            	    }
            	    break;

            	default :
            	    break loop41;
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
    // JPA.g:163:1: conditional_factor : ( ( 'NOT' )? simple_cond_expression | '(' conditional_expression ')' );
    public final JPAParser.conditional_factor_return conditional_factor() throws RecognitionException {
        JPAParser.conditional_factor_return retval = new JPAParser.conditional_factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal123=null;
        Token char_literal125=null;
        Token char_literal127=null;
        JPAParser.simple_cond_expression_return simple_cond_expression124 = null;

        JPAParser.conditional_expression_return conditional_expression126 = null;


        Object string_literal123_tree=null;
        Object char_literal125_tree=null;
        Object char_literal127_tree=null;

        try {
            // JPA.g:164:2: ( ( 'NOT' )? simple_cond_expression | '(' conditional_expression ')' )
            int alt43=2;
            alt43 = dfa43.predict(input);
            switch (alt43) {
                case 1 :
                    // JPA.g:164:4: ( 'NOT' )? simple_cond_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    // JPA.g:164:4: ( 'NOT' )?
                    int alt42=2;
                    int LA42_0 = input.LA(1);

                    if ( (LA42_0==52) ) {
                        int LA42_1 = input.LA(2);

                        if ( (synpred53_JPA()) ) {
                            alt42=1;
                        }
                    }
                    switch (alt42) {
                        case 1 :
                            // JPA.g:164:5: 'NOT'
                            {
                            string_literal123=(Token)match(input,52,FOLLOW_52_in_conditional_factor1026); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal123_tree = (Object)adaptor.create(string_literal123);
                            adaptor.addChild(root_0, string_literal123_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_simple_cond_expression_in_conditional_factor1030);
                    simple_cond_expression124=simple_cond_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_cond_expression124.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:164:38: '(' conditional_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal125=(Token)match(input,34,FOLLOW_34_in_conditional_factor1034); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal125_tree = (Object)adaptor.create(char_literal125);
                    adaptor.addChild(root_0, char_literal125_tree);
                    }
                    pushFollow(FOLLOW_conditional_expression_in_conditional_factor1035);
                    conditional_expression126=conditional_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression126.getTree());
                    char_literal127=(Token)match(input,27,FOLLOW_27_in_conditional_factor1036); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal127_tree = (Object)adaptor.create(char_literal127);
                    adaptor.addChild(root_0, char_literal127_tree);
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
    // JPA.g:166:1: simple_cond_expression : ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression );
    public final JPAParser.simple_cond_expression_return simple_cond_expression() throws RecognitionException {
        JPAParser.simple_cond_expression_return retval = new JPAParser.simple_cond_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.comparison_expression_return comparison_expression128 = null;

        JPAParser.between_expression_return between_expression129 = null;

        JPAParser.like_expression_return like_expression130 = null;

        JPAParser.in_expression_return in_expression131 = null;

        JPAParser.null_comparison_expression_return null_comparison_expression132 = null;

        JPAParser.empty_collection_comparison_expression_return empty_collection_comparison_expression133 = null;

        JPAParser.collection_member_expression_return collection_member_expression134 = null;

        JPAParser.exists_expression_return exists_expression135 = null;



        try {
            // JPA.g:167:2: ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression )
            int alt44=8;
            alt44 = dfa44.predict(input);
            switch (alt44) {
                case 1 :
                    // JPA.g:167:4: comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression1045);
                    comparison_expression128=comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression128.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:168:4: between_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_between_expression_in_simple_cond_expression1050);
                    between_expression129=between_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression129.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:169:4: like_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_like_expression_in_simple_cond_expression1055);
                    like_expression130=like_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression130.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:170:4: in_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_in_expression_in_simple_cond_expression1060);
                    in_expression131=in_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression131.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:171:4: null_comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression1065);
                    null_comparison_expression132=null_comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression132.getTree());

                    }
                    break;
                case 6 :
                    // JPA.g:172:4: empty_collection_comparison_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression1070);
                    empty_collection_comparison_expression133=empty_collection_comparison_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression133.getTree());

                    }
                    break;
                case 7 :
                    // JPA.g:173:4: collection_member_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression1075);
                    collection_member_expression134=collection_member_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression134.getTree());

                    }
                    break;
                case 8 :
                    // JPA.g:174:4: exists_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_exists_expression_in_simple_cond_expression1080);
                    exists_expression135=exists_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression135.getTree());

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

    public static class between_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "between_expression"
    // JPA.g:176:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
    public final JPAParser.between_expression_return between_expression() throws RecognitionException {
        JPAParser.between_expression_return retval = new JPAParser.between_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal137=null;
        Token string_literal138=null;
        Token string_literal140=null;
        Token string_literal143=null;
        Token string_literal144=null;
        Token string_literal146=null;
        Token string_literal149=null;
        Token string_literal150=null;
        Token string_literal152=null;
        JPAParser.arithmetic_expression_return arithmetic_expression136 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression139 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression141 = null;

        JPAParser.string_expression_return string_expression142 = null;

        JPAParser.string_expression_return string_expression145 = null;

        JPAParser.string_expression_return string_expression147 = null;

        JPAParser.datetime_expression_return datetime_expression148 = null;

        JPAParser.datetime_expression_return datetime_expression151 = null;

        JPAParser.datetime_expression_return datetime_expression153 = null;


        Object string_literal137_tree=null;
        Object string_literal138_tree=null;
        Object string_literal140_tree=null;
        Object string_literal143_tree=null;
        Object string_literal144_tree=null;
        Object string_literal146_tree=null;
        Object string_literal149_tree=null;
        Object string_literal150_tree=null;
        Object string_literal152_tree=null;

        try {
            // JPA.g:177:2: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
            int alt48=3;
            alt48 = dfa48.predict(input);
            switch (alt48) {
                case 1 :
                    // JPA.g:177:4: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1089);
                    arithmetic_expression136=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression136.getTree());
                    // JPA.g:177:26: ( 'NOT' )?
                    int alt45=2;
                    int LA45_0 = input.LA(1);

                    if ( (LA45_0==52) ) {
                        alt45=1;
                    }
                    switch (alt45) {
                        case 1 :
                            // JPA.g:177:27: 'NOT'
                            {
                            string_literal137=(Token)match(input,52,FOLLOW_52_in_between_expression1092); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal137_tree = (Object)adaptor.create(string_literal137);
                            adaptor.addChild(root_0, string_literal137_tree);
                            }

                            }
                            break;

                    }

                    string_literal138=(Token)match(input,53,FOLLOW_53_in_between_expression1096); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal138_tree = (Object)adaptor.create(string_literal138);
                    adaptor.addChild(root_0, string_literal138_tree);
                    }
                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1098);
                    arithmetic_expression139=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression139.getTree());
                    string_literal140=(Token)match(input,51,FOLLOW_51_in_between_expression1100); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal140_tree = (Object)adaptor.create(string_literal140);
                    adaptor.addChild(root_0, string_literal140_tree);
                    }
                    pushFollow(FOLLOW_arithmetic_expression_in_between_expression1102);
                    arithmetic_expression141=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression141.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:178:4: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_expression_in_between_expression1107);
                    string_expression142=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression142.getTree());
                    // JPA.g:178:22: ( 'NOT' )?
                    int alt46=2;
                    int LA46_0 = input.LA(1);

                    if ( (LA46_0==52) ) {
                        alt46=1;
                    }
                    switch (alt46) {
                        case 1 :
                            // JPA.g:178:23: 'NOT'
                            {
                            string_literal143=(Token)match(input,52,FOLLOW_52_in_between_expression1110); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal143_tree = (Object)adaptor.create(string_literal143);
                            adaptor.addChild(root_0, string_literal143_tree);
                            }

                            }
                            break;

                    }

                    string_literal144=(Token)match(input,53,FOLLOW_53_in_between_expression1114); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal144_tree = (Object)adaptor.create(string_literal144);
                    adaptor.addChild(root_0, string_literal144_tree);
                    }
                    pushFollow(FOLLOW_string_expression_in_between_expression1116);
                    string_expression145=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression145.getTree());
                    string_literal146=(Token)match(input,51,FOLLOW_51_in_between_expression1118); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal146_tree = (Object)adaptor.create(string_literal146);
                    adaptor.addChild(root_0, string_literal146_tree);
                    }
                    pushFollow(FOLLOW_string_expression_in_between_expression1120);
                    string_expression147=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression147.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:179:4: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_expression_in_between_expression1125);
                    datetime_expression148=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression148.getTree());
                    // JPA.g:179:24: ( 'NOT' )?
                    int alt47=2;
                    int LA47_0 = input.LA(1);

                    if ( (LA47_0==52) ) {
                        alt47=1;
                    }
                    switch (alt47) {
                        case 1 :
                            // JPA.g:179:25: 'NOT'
                            {
                            string_literal149=(Token)match(input,52,FOLLOW_52_in_between_expression1128); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal149_tree = (Object)adaptor.create(string_literal149);
                            adaptor.addChild(root_0, string_literal149_tree);
                            }

                            }
                            break;

                    }

                    string_literal150=(Token)match(input,53,FOLLOW_53_in_between_expression1132); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal150_tree = (Object)adaptor.create(string_literal150);
                    adaptor.addChild(root_0, string_literal150_tree);
                    }
                    pushFollow(FOLLOW_datetime_expression_in_between_expression1134);
                    datetime_expression151=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression151.getTree());
                    string_literal152=(Token)match(input,51,FOLLOW_51_in_between_expression1136); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal152_tree = (Object)adaptor.create(string_literal152);
                    adaptor.addChild(root_0, string_literal152_tree);
                    }
                    pushFollow(FOLLOW_datetime_expression_in_between_expression1138);
                    datetime_expression153=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression153.getTree());

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
    // JPA.g:181:1: in_expression : path_expression ( 'NOT' )? 'IN' in_expression_right_part ;
    public final JPAParser.in_expression_return in_expression() throws RecognitionException {
        JPAParser.in_expression_return retval = new JPAParser.in_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal155=null;
        Token string_literal156=null;
        JPAParser.path_expression_return path_expression154 = null;

        JPAParser.in_expression_right_part_return in_expression_right_part157 = null;


        Object string_literal155_tree=null;
        Object string_literal156_tree=null;

        try {
            // JPA.g:182:2: ( path_expression ( 'NOT' )? 'IN' in_expression_right_part )
            // JPA.g:182:4: path_expression ( 'NOT' )? 'IN' in_expression_right_part
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_in_expression1147);
            path_expression154=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression154.getTree());
            // JPA.g:182:20: ( 'NOT' )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==52) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // JPA.g:182:21: 'NOT'
                    {
                    string_literal155=(Token)match(input,52,FOLLOW_52_in_in_expression1150); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal155_tree = (Object)adaptor.create(string_literal155);
                    adaptor.addChild(root_0, string_literal155_tree);
                    }

                    }
                    break;

            }

            string_literal156=(Token)match(input,33,FOLLOW_33_in_in_expression1154); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal156_tree = (Object)adaptor.create(string_literal156);
            adaptor.addChild(root_0, string_literal156_tree);
            }
            pushFollow(FOLLOW_in_expression_right_part_in_in_expression1156);
            in_expression_right_part157=in_expression_right_part();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression_right_part157.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
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
    // JPA.g:184:1: in_expression_right_part : ( '(' in_item ( ',' in_item )* ')' | subquery );
    public final JPAParser.in_expression_right_part_return in_expression_right_part() throws RecognitionException {
        JPAParser.in_expression_right_part_return retval = new JPAParser.in_expression_right_part_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal158=null;
        Token char_literal160=null;
        Token char_literal162=null;
        JPAParser.in_item_return in_item159 = null;

        JPAParser.in_item_return in_item161 = null;

        JPAParser.subquery_return subquery163 = null;


        Object char_literal158_tree=null;
        Object char_literal160_tree=null;
        Object char_literal162_tree=null;

        try {
            // JPA.g:185:2: ( '(' in_item ( ',' in_item )* ')' | subquery )
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==34) ) {
                alt51=1;
            }
            else if ( (LA51_0==26) ) {
                alt51=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 51, 0, input);

                throw nvae;
            }
            switch (alt51) {
                case 1 :
                    // JPA.g:185:4: '(' in_item ( ',' in_item )* ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal158=(Token)match(input,34,FOLLOW_34_in_in_expression_right_part1165); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal158_tree = (Object)adaptor.create(char_literal158);
                    adaptor.addChild(root_0, char_literal158_tree);
                    }
                    pushFollow(FOLLOW_in_item_in_in_expression_right_part1167);
                    in_item159=in_item();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item159.getTree());
                    // JPA.g:185:16: ( ',' in_item )*
                    loop50:
                    do {
                        int alt50=2;
                        int LA50_0 = input.LA(1);

                        if ( (LA50_0==24) ) {
                            alt50=1;
                        }


                        switch (alt50) {
                    	case 1 :
                    	    // JPA.g:185:17: ',' in_item
                    	    {
                    	    char_literal160=(Token)match(input,24,FOLLOW_24_in_in_expression_right_part1170); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal160_tree = (Object)adaptor.create(char_literal160);
                    	    adaptor.addChild(root_0, char_literal160_tree);
                    	    }
                    	    pushFollow(FOLLOW_in_item_in_in_expression_right_part1172);
                    	    in_item161=in_item();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item161.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop50;
                        }
                    } while (true);

                    char_literal162=(Token)match(input,27,FOLLOW_27_in_in_expression_right_part1176); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal162_tree = (Object)adaptor.create(char_literal162);
                    adaptor.addChild(root_0, char_literal162_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:186:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_in_expression_right_part1181);
                    subquery163=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery163.getTree());

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
    // JPA.g:188:1: in_item : ( literal | input_parameter );
    public final JPAParser.in_item_return in_item() throws RecognitionException {
        JPAParser.in_item_return retval = new JPAParser.in_item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.literal_return literal164 = null;

        JPAParser.input_parameter_return input_parameter165 = null;



        try {
            // JPA.g:189:2: ( literal | input_parameter )
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==WORD) ) {
                alt52=1;
            }
            else if ( ((LA52_0>=93 && LA52_0<=94)) ) {
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
                    // JPA.g:189:4: literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_literal_in_in_item1190);
                    literal164=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal164.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:190:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_in_item1195);
                    input_parameter165=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter165.getTree());

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
    // JPA.g:192:1: like_expression : string_expression ( 'NOT' )? 'LIKE' pattern_value ( 'ESCAPE' ESCAPE_CHARACTER )? ;
    public final JPAParser.like_expression_return like_expression() throws RecognitionException {
        JPAParser.like_expression_return retval = new JPAParser.like_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal167=null;
        Token string_literal168=null;
        Token string_literal170=null;
        Token ESCAPE_CHARACTER171=null;
        JPAParser.string_expression_return string_expression166 = null;

        JPAParser.pattern_value_return pattern_value169 = null;


        Object string_literal167_tree=null;
        Object string_literal168_tree=null;
        Object string_literal170_tree=null;
        Object ESCAPE_CHARACTER171_tree=null;

        try {
            // JPA.g:193:2: ( string_expression ( 'NOT' )? 'LIKE' pattern_value ( 'ESCAPE' ESCAPE_CHARACTER )? )
            // JPA.g:193:4: string_expression ( 'NOT' )? 'LIKE' pattern_value ( 'ESCAPE' ESCAPE_CHARACTER )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_string_expression_in_like_expression1204);
            string_expression166=string_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression166.getTree());
            // JPA.g:193:22: ( 'NOT' )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==52) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // JPA.g:193:23: 'NOT'
                    {
                    string_literal167=(Token)match(input,52,FOLLOW_52_in_like_expression1207); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal167_tree = (Object)adaptor.create(string_literal167);
                    adaptor.addChild(root_0, string_literal167_tree);
                    }

                    }
                    break;

            }

            string_literal168=(Token)match(input,54,FOLLOW_54_in_like_expression1211); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal168_tree = (Object)adaptor.create(string_literal168);
            adaptor.addChild(root_0, string_literal168_tree);
            }
            pushFollow(FOLLOW_pattern_value_in_like_expression1213);
            pattern_value169=pattern_value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value169.getTree());
            // JPA.g:193:52: ( 'ESCAPE' ESCAPE_CHARACTER )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==55) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // JPA.g:193:53: 'ESCAPE' ESCAPE_CHARACTER
                    {
                    string_literal170=(Token)match(input,55,FOLLOW_55_in_like_expression1216); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal170_tree = (Object)adaptor.create(string_literal170);
                    adaptor.addChild(root_0, string_literal170_tree);
                    }
                    ESCAPE_CHARACTER171=(Token)match(input,ESCAPE_CHARACTER,FOLLOW_ESCAPE_CHARACTER_in_like_expression1218); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ESCAPE_CHARACTER171_tree = (Object)adaptor.create(ESCAPE_CHARACTER171);
                    adaptor.addChild(root_0, ESCAPE_CHARACTER171_tree);
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
    // JPA.g:195:1: null_comparison_expression : ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' ;
    public final JPAParser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
        JPAParser.null_comparison_expression_return retval = new JPAParser.null_comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal174=null;
        Token string_literal175=null;
        Token string_literal176=null;
        JPAParser.path_expression_return path_expression172 = null;

        JPAParser.input_parameter_return input_parameter173 = null;


        Object string_literal174_tree=null;
        Object string_literal175_tree=null;
        Object string_literal176_tree=null;

        try {
            // JPA.g:196:2: ( ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' )
            // JPA.g:196:4: ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL'
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:196:4: ( path_expression | input_parameter )
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( ((LA55_0>=SIMPLE_FIELD_PATH && LA55_0<=FIELD_PATH)) ) {
                alt55=1;
            }
            else if ( ((LA55_0>=93 && LA55_0<=94)) ) {
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
                    // JPA.g:196:5: path_expression
                    {
                    pushFollow(FOLLOW_path_expression_in_null_comparison_expression1230);
                    path_expression172=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression172.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:196:23: input_parameter
                    {
                    pushFollow(FOLLOW_input_parameter_in_null_comparison_expression1234);
                    input_parameter173=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter173.getTree());

                    }
                    break;

            }

            string_literal174=(Token)match(input,56,FOLLOW_56_in_null_comparison_expression1237); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal174_tree = (Object)adaptor.create(string_literal174);
            adaptor.addChild(root_0, string_literal174_tree);
            }
            // JPA.g:196:45: ( 'NOT' )?
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==52) ) {
                alt56=1;
            }
            switch (alt56) {
                case 1 :
                    // JPA.g:196:46: 'NOT'
                    {
                    string_literal175=(Token)match(input,52,FOLLOW_52_in_null_comparison_expression1240); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal175_tree = (Object)adaptor.create(string_literal175);
                    adaptor.addChild(root_0, string_literal175_tree);
                    }

                    }
                    break;

            }

            string_literal176=(Token)match(input,57,FOLLOW_57_in_null_comparison_expression1244); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal176_tree = (Object)adaptor.create(string_literal176);
            adaptor.addChild(root_0, string_literal176_tree);
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
    // JPA.g:198:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
    public final JPAParser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
        JPAParser.empty_collection_comparison_expression_return retval = new JPAParser.empty_collection_comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal178=null;
        Token string_literal179=null;
        Token string_literal180=null;
        JPAParser.path_expression_return path_expression177 = null;


        Object string_literal178_tree=null;
        Object string_literal179_tree=null;
        Object string_literal180_tree=null;

        try {
            // JPA.g:199:2: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
            // JPA.g:199:4: path_expression 'IS' ( 'NOT' )? 'EMPTY'
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression1253);
            path_expression177=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression177.getTree());
            string_literal178=(Token)match(input,56,FOLLOW_56_in_empty_collection_comparison_expression1255); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal178_tree = (Object)adaptor.create(string_literal178);
            adaptor.addChild(root_0, string_literal178_tree);
            }
            // JPA.g:199:25: ( 'NOT' )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==52) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // JPA.g:199:26: 'NOT'
                    {
                    string_literal179=(Token)match(input,52,FOLLOW_52_in_empty_collection_comparison_expression1258); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal179_tree = (Object)adaptor.create(string_literal179);
                    adaptor.addChild(root_0, string_literal179_tree);
                    }

                    }
                    break;

            }

            string_literal180=(Token)match(input,58,FOLLOW_58_in_empty_collection_comparison_expression1262); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal180_tree = (Object)adaptor.create(string_literal180);
            adaptor.addChild(root_0, string_literal180_tree);
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
    // JPA.g:201:1: collection_member_expression : entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
    public final JPAParser.collection_member_expression_return collection_member_expression() throws RecognitionException {
        JPAParser.collection_member_expression_return retval = new JPAParser.collection_member_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal182=null;
        Token string_literal183=null;
        Token string_literal184=null;
        JPAParser.entity_expression_return entity_expression181 = null;

        JPAParser.path_expression_return path_expression185 = null;


        Object string_literal182_tree=null;
        Object string_literal183_tree=null;
        Object string_literal184_tree=null;

        try {
            // JPA.g:202:2: ( entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
            // JPA.g:202:4: entity_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_entity_expression_in_collection_member_expression1271);
            entity_expression181=entity_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression181.getTree());
            // JPA.g:202:22: ( 'NOT' )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==52) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // JPA.g:202:23: 'NOT'
                    {
                    string_literal182=(Token)match(input,52,FOLLOW_52_in_collection_member_expression1274); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal182_tree = (Object)adaptor.create(string_literal182);
                    adaptor.addChild(root_0, string_literal182_tree);
                    }

                    }
                    break;

            }

            string_literal183=(Token)match(input,59,FOLLOW_59_in_collection_member_expression1278); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal183_tree = (Object)adaptor.create(string_literal183);
            adaptor.addChild(root_0, string_literal183_tree);
            }
            // JPA.g:202:40: ( 'OF' )?
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==60) ) {
                alt59=1;
            }
            switch (alt59) {
                case 1 :
                    // JPA.g:202:41: 'OF'
                    {
                    string_literal184=(Token)match(input,60,FOLLOW_60_in_collection_member_expression1281); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal184_tree = (Object)adaptor.create(string_literal184);
                    adaptor.addChild(root_0, string_literal184_tree);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_path_expression_in_collection_member_expression1285);
            path_expression185=path_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression185.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
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
    // JPA.g:204:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
    public final JPAParser.exists_expression_return exists_expression() throws RecognitionException {
        JPAParser.exists_expression_return retval = new JPAParser.exists_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal186=null;
        Token string_literal187=null;
        JPAParser.subquery_return subquery188 = null;


        Object string_literal186_tree=null;
        Object string_literal187_tree=null;

        try {
            // JPA.g:205:2: ( ( 'NOT' )? 'EXISTS' subquery )
            // JPA.g:205:4: ( 'NOT' )? 'EXISTS' subquery
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:205:4: ( 'NOT' )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==52) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // JPA.g:205:5: 'NOT'
                    {
                    string_literal186=(Token)match(input,52,FOLLOW_52_in_exists_expression1295); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal186_tree = (Object)adaptor.create(string_literal186);
                    adaptor.addChild(root_0, string_literal186_tree);
                    }

                    }
                    break;

            }

            string_literal187=(Token)match(input,61,FOLLOW_61_in_exists_expression1299); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal187_tree = (Object)adaptor.create(string_literal187);
            adaptor.addChild(root_0, string_literal187_tree);
            }
            pushFollow(FOLLOW_subquery_in_exists_expression1301);
            subquery188=subquery();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery188.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
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
    // JPA.g:207:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
    public final JPAParser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
        JPAParser.all_or_any_expression_return retval = new JPAParser.all_or_any_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set189=null;
        JPAParser.subquery_return subquery190 = null;


        Object set189_tree=null;

        try {
            // JPA.g:208:2: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
            // JPA.g:208:4: ( 'ALL' | 'ANY' | 'SOME' ) subquery
            {
            root_0 = (Object)adaptor.nil();

            set189=(Token)input.LT(1);
            if ( (input.LA(1)>=62 && input.LA(1)<=64) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set189));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            pushFollow(FOLLOW_subquery_in_all_or_any_expression1323);
            subquery190=subquery();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery190.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
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
    // JPA.g:210:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
    public final JPAParser.comparison_expression_return comparison_expression() throws RecognitionException {
        JPAParser.comparison_expression_return retval = new JPAParser.comparison_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set196=null;
        Token set200=null;
        Token set208=null;
        JPAParser.string_expression_return string_expression191 = null;

        JPAParser.comparison_operator_return comparison_operator192 = null;

        JPAParser.string_expression_return string_expression193 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression194 = null;

        JPAParser.boolean_expression_return boolean_expression195 = null;

        JPAParser.boolean_expression_return boolean_expression197 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression198 = null;

        JPAParser.enum_expression_return enum_expression199 = null;

        JPAParser.enum_expression_return enum_expression201 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression202 = null;

        JPAParser.datetime_expression_return datetime_expression203 = null;

        JPAParser.comparison_operator_return comparison_operator204 = null;

        JPAParser.datetime_expression_return datetime_expression205 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression206 = null;

        JPAParser.entity_expression_return entity_expression207 = null;

        JPAParser.entity_expression_return entity_expression209 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression210 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression211 = null;

        JPAParser.comparison_operator_return comparison_operator212 = null;

        JPAParser.arithmetic_expression_return arithmetic_expression213 = null;

        JPAParser.all_or_any_expression_return all_or_any_expression214 = null;


        Object set196_tree=null;
        Object set200_tree=null;
        Object set208_tree=null;

        try {
            // JPA.g:211:2: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
            int alt67=6;
            alt67 = dfa67.predict(input);
            switch (alt67) {
                case 1 :
                    // JPA.g:211:4: string_expression comparison_operator ( string_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_expression_in_comparison_expression1332);
                    string_expression191=string_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression191.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression1334);
                    comparison_operator192=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator192.getTree());
                    // JPA.g:211:42: ( string_expression | all_or_any_expression )
                    int alt61=2;
                    int LA61_0 = input.LA(1);

                    if ( (LA61_0==STRINGLITERAL||(LA61_0>=SIMPLE_FIELD_PATH && LA61_0<=FIELD_PATH)||LA61_0==26||(LA61_0>=38 && LA61_0<=42)||(LA61_0>=84 && LA61_0<=88)||(LA61_0>=93 && LA61_0<=94)) ) {
                        alt61=1;
                    }
                    else if ( ((LA61_0>=62 && LA61_0<=64)) ) {
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
                            // JPA.g:211:43: string_expression
                            {
                            pushFollow(FOLLOW_string_expression_in_comparison_expression1337);
                            string_expression193=string_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression193.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:211:63: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression1341);
                            all_or_any_expression194=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression194.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // JPA.g:212:4: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_expression_in_comparison_expression1347);
                    boolean_expression195=boolean_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression195.getTree());
                    set196=(Token)input.LT(1);
                    if ( (input.LA(1)>=65 && input.LA(1)<=66) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set196));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:212:36: ( boolean_expression | all_or_any_expression )
                    int alt62=2;
                    int LA62_0 = input.LA(1);

                    if ( ((LA62_0>=SIMPLE_FIELD_PATH && LA62_0<=FIELD_PATH)||LA62_0==26||(LA62_0>=93 && LA62_0<=96)) ) {
                        alt62=1;
                    }
                    else if ( ((LA62_0>=62 && LA62_0<=64)) ) {
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
                            // JPA.g:212:37: boolean_expression
                            {
                            pushFollow(FOLLOW_boolean_expression_in_comparison_expression1358);
                            boolean_expression197=boolean_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression197.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:212:58: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression1362);
                            all_or_any_expression198=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression198.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // JPA.g:213:4: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_expression_in_comparison_expression1368);
                    enum_expression199=enum_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression199.getTree());
                    set200=(Token)input.LT(1);
                    if ( (input.LA(1)>=65 && input.LA(1)<=66) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set200));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:213:31: ( enum_expression | all_or_any_expression )
                    int alt63=2;
                    int LA63_0 = input.LA(1);

                    if ( (LA63_0==WORD||(LA63_0>=SIMPLE_FIELD_PATH && LA63_0<=FIELD_PATH)||LA63_0==26||(LA63_0>=93 && LA63_0<=94)) ) {
                        alt63=1;
                    }
                    else if ( ((LA63_0>=62 && LA63_0<=64)) ) {
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
                            // JPA.g:213:32: enum_expression
                            {
                            pushFollow(FOLLOW_enum_expression_in_comparison_expression1377);
                            enum_expression201=enum_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression201.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:213:50: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression1381);
                            all_or_any_expression202=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression202.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // JPA.g:214:4: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_expression_in_comparison_expression1387);
                    datetime_expression203=datetime_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression203.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression1389);
                    comparison_operator204=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator204.getTree());
                    // JPA.g:214:44: ( datetime_expression | all_or_any_expression )
                    int alt64=2;
                    int LA64_0 = input.LA(1);

                    if ( ((LA64_0>=SIMPLE_FIELD_PATH && LA64_0<=FIELD_PATH)||LA64_0==26||(LA64_0>=38 && LA64_0<=42)||(LA64_0>=81 && LA64_0<=83)||(LA64_0>=93 && LA64_0<=94)) ) {
                        alt64=1;
                    }
                    else if ( ((LA64_0>=62 && LA64_0<=64)) ) {
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
                            // JPA.g:214:45: datetime_expression
                            {
                            pushFollow(FOLLOW_datetime_expression_in_comparison_expression1392);
                            datetime_expression205=datetime_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression205.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:214:67: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression1396);
                            all_or_any_expression206=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression206.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // JPA.g:215:4: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_entity_expression_in_comparison_expression1402);
                    entity_expression207=entity_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression207.getTree());
                    set208=(Token)input.LT(1);
                    if ( (input.LA(1)>=65 && input.LA(1)<=66) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set208));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // JPA.g:215:35: ( entity_expression | all_or_any_expression )
                    int alt65=2;
                    int LA65_0 = input.LA(1);

                    if ( (LA65_0==WORD||(LA65_0>=SIMPLE_FIELD_PATH && LA65_0<=FIELD_PATH)||(LA65_0>=93 && LA65_0<=94)) ) {
                        alt65=1;
                    }
                    else if ( ((LA65_0>=62 && LA65_0<=64)) ) {
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
                            // JPA.g:215:36: entity_expression
                            {
                            pushFollow(FOLLOW_entity_expression_in_comparison_expression1413);
                            entity_expression209=entity_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression209.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:215:56: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression1417);
                            all_or_any_expression210=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression210.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 6 :
                    // JPA.g:216:4: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression1423);
                    arithmetic_expression211=arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression211.getTree());
                    pushFollow(FOLLOW_comparison_operator_in_comparison_expression1425);
                    comparison_operator212=comparison_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator212.getTree());
                    // JPA.g:216:46: ( arithmetic_expression | all_or_any_expression )
                    int alt66=2;
                    int LA66_0 = input.LA(1);

                    if ( ((LA66_0>=INT_NUMERAL && LA66_0<=FIELD_PATH)||LA66_0==26||LA66_0==34||(LA66_0>=38 && LA66_0<=42)||(LA66_0>=71 && LA66_0<=72)||(LA66_0>=75 && LA66_0<=80)||(LA66_0>=92 && LA66_0<=94)) ) {
                        alt66=1;
                    }
                    else if ( ((LA66_0>=62 && LA66_0<=64)) ) {
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
                            // JPA.g:216:47: arithmetic_expression
                            {
                            pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression1428);
                            arithmetic_expression213=arithmetic_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression213.getTree());

                            }
                            break;
                        case 2 :
                            // JPA.g:216:71: all_or_any_expression
                            {
                            pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression1432);
                            all_or_any_expression214=all_or_any_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression214.getTree());

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
    // JPA.g:218:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
    public final JPAParser.comparison_operator_return comparison_operator() throws RecognitionException {
        JPAParser.comparison_operator_return retval = new JPAParser.comparison_operator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set215=null;

        Object set215_tree=null;

        try {
            // JPA.g:219:2: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set215=(Token)input.LT(1);
            if ( (input.LA(1)>=65 && input.LA(1)<=70) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set215));
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
    // JPA.g:226:1: arithmetic_expression : ( simple_arithmetic_expression | subquery );
    public final JPAParser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
        JPAParser.arithmetic_expression_return retval = new JPAParser.arithmetic_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression216 = null;

        JPAParser.subquery_return subquery217 = null;



        try {
            // JPA.g:227:2: ( simple_arithmetic_expression | subquery )
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( ((LA68_0>=INT_NUMERAL && LA68_0<=FIELD_PATH)||LA68_0==34||(LA68_0>=38 && LA68_0<=42)||(LA68_0>=71 && LA68_0<=72)||(LA68_0>=75 && LA68_0<=80)||(LA68_0>=92 && LA68_0<=94)) ) {
                alt68=1;
            }
            else if ( (LA68_0==26) ) {
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
                    // JPA.g:227:4: simple_arithmetic_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simple_arithmetic_expression_in_arithmetic_expression1476);
                    simple_arithmetic_expression216=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression216.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:228:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_arithmetic_expression1481);
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
    // $ANTLR end "arithmetic_expression"

    public static class simple_arithmetic_expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "simple_arithmetic_expression"
    // JPA.g:230:1: simple_arithmetic_expression : ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )* ;
    public final JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression() throws RecognitionException {
        JPAParser.simple_arithmetic_expression_return retval = new JPAParser.simple_arithmetic_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set219=null;
        JPAParser.arithmetic_term_return arithmetic_term218 = null;

        JPAParser.arithmetic_term_return arithmetic_term220 = null;


        Object set219_tree=null;

        try {
            // JPA.g:231:2: ( ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )* )
            // JPA.g:231:4: ( arithmetic_term ) ( ( '+' | '-' ) arithmetic_term )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:231:4: ( arithmetic_term )
            // JPA.g:231:5: arithmetic_term
            {
            pushFollow(FOLLOW_arithmetic_term_in_simple_arithmetic_expression1491);
            arithmetic_term218=arithmetic_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term218.getTree());

            }

            // JPA.g:231:22: ( ( '+' | '-' ) arithmetic_term )*
            loop69:
            do {
                int alt69=2;
                int LA69_0 = input.LA(1);

                if ( ((LA69_0>=71 && LA69_0<=72)) ) {
                    alt69=1;
                }


                switch (alt69) {
            	case 1 :
            	    // JPA.g:231:23: ( '+' | '-' ) arithmetic_term
            	    {
            	    set219=(Token)input.LT(1);
            	    if ( (input.LA(1)>=71 && input.LA(1)<=72) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set219));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmetic_term_in_simple_arithmetic_expression1505);
            	    arithmetic_term220=arithmetic_term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term220.getTree());

            	    }
            	    break;

            	default :
            	    break loop69;
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
    // JPA.g:233:1: arithmetic_term : ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )* ;
    public final JPAParser.arithmetic_term_return arithmetic_term() throws RecognitionException {
        JPAParser.arithmetic_term_return retval = new JPAParser.arithmetic_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set222=null;
        JPAParser.arithmetic_factor_return arithmetic_factor221 = null;

        JPAParser.arithmetic_factor_return arithmetic_factor223 = null;


        Object set222_tree=null;

        try {
            // JPA.g:234:2: ( ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )* )
            // JPA.g:234:4: ( arithmetic_factor ) ( ( '*' | '/' ) arithmetic_factor )*
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:234:4: ( arithmetic_factor )
            // JPA.g:234:5: arithmetic_factor
            {
            pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term1517);
            arithmetic_factor221=arithmetic_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor221.getTree());

            }

            // JPA.g:234:24: ( ( '*' | '/' ) arithmetic_factor )*
            loop70:
            do {
                int alt70=2;
                int LA70_0 = input.LA(1);

                if ( ((LA70_0>=73 && LA70_0<=74)) ) {
                    alt70=1;
                }


                switch (alt70) {
            	case 1 :
            	    // JPA.g:234:25: ( '*' | '/' ) arithmetic_factor
            	    {
            	    set222=(Token)input.LT(1);
            	    if ( (input.LA(1)>=73 && input.LA(1)<=74) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set222));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term1531);
            	    arithmetic_factor223=arithmetic_factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor223.getTree());

            	    }
            	    break;

            	default :
            	    break loop70;
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
    // JPA.g:236:1: arithmetic_factor : ( '+' | '-' )? arithmetic_primary ;
    public final JPAParser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
        JPAParser.arithmetic_factor_return retval = new JPAParser.arithmetic_factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set224=null;
        JPAParser.arithmetic_primary_return arithmetic_primary225 = null;


        Object set224_tree=null;

        try {
            // JPA.g:237:2: ( ( '+' | '-' )? arithmetic_primary )
            // JPA.g:237:4: ( '+' | '-' )? arithmetic_primary
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:237:4: ( '+' | '-' )?
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( ((LA71_0>=71 && LA71_0<=72)) ) {
                alt71=1;
            }
            switch (alt71) {
                case 1 :
                    // JPA.g:
                    {
                    set224=(Token)input.LT(1);
                    if ( (input.LA(1)>=71 && input.LA(1)<=72) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set224));
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

            pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor1553);
            arithmetic_primary225=arithmetic_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary225.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
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
    // JPA.g:239:1: arithmetic_primary : ( path_expression | numeric_literal | '(' simple_arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression );
    public final JPAParser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
        JPAParser.arithmetic_primary_return retval = new JPAParser.arithmetic_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal228=null;
        Token char_literal230=null;
        JPAParser.path_expression_return path_expression226 = null;

        JPAParser.numeric_literal_return numeric_literal227 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression229 = null;

        JPAParser.input_parameter_return input_parameter231 = null;

        JPAParser.functions_returning_numerics_return functions_returning_numerics232 = null;

        JPAParser.aggregate_expression_return aggregate_expression233 = null;


        Object char_literal228_tree=null;
        Object char_literal230_tree=null;

        try {
            // JPA.g:240:2: ( path_expression | numeric_literal | '(' simple_arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression )
            int alt72=6;
            switch ( input.LA(1) ) {
            case SIMPLE_FIELD_PATH:
            case FIELD_PATH:
                {
                alt72=1;
                }
                break;
            case INT_NUMERAL:
            case 92:
                {
                alt72=2;
                }
                break;
            case 34:
                {
                alt72=3;
                }
                break;
            case 93:
            case 94:
                {
                alt72=4;
                }
                break;
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
                {
                alt72=5;
                }
                break;
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
                {
                alt72=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 72, 0, input);

                throw nvae;
            }

            switch (alt72) {
                case 1 :
                    // JPA.g:240:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_arithmetic_primary1562);
                    path_expression226=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression226.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:241:4: numeric_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary1567);
                    numeric_literal227=numeric_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal227.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:242:4: '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal228=(Token)match(input,34,FOLLOW_34_in_arithmetic_primary1572); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal228_tree = (Object)adaptor.create(char_literal228);
                    adaptor.addChild(root_0, char_literal228_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_arithmetic_primary1573);
                    simple_arithmetic_expression229=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression229.getTree());
                    char_literal230=(Token)match(input,27,FOLLOW_27_in_arithmetic_primary1574); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal230_tree = (Object)adaptor.create(char_literal230);
                    adaptor.addChild(root_0, char_literal230_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:243:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_arithmetic_primary1579);
                    input_parameter231=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter231.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:244:4: functions_returning_numerics
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary1584);
                    functions_returning_numerics232=functions_returning_numerics();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics232.getTree());

                    }
                    break;
                case 6 :
                    // JPA.g:245:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary1589);
                    aggregate_expression233=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression233.getTree());

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
    // JPA.g:247:1: string_expression : ( string_primary | subquery );
    public final JPAParser.string_expression_return string_expression() throws RecognitionException {
        JPAParser.string_expression_return retval = new JPAParser.string_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.string_primary_return string_primary234 = null;

        JPAParser.subquery_return subquery235 = null;



        try {
            // JPA.g:248:2: ( string_primary | subquery )
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==STRINGLITERAL||(LA73_0>=SIMPLE_FIELD_PATH && LA73_0<=FIELD_PATH)||(LA73_0>=38 && LA73_0<=42)||(LA73_0>=84 && LA73_0<=88)||(LA73_0>=93 && LA73_0<=94)) ) {
                alt73=1;
            }
            else if ( (LA73_0==26) ) {
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
                    // JPA.g:248:4: string_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_string_primary_in_string_expression1598);
                    string_primary234=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary234.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:248:21: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_string_expression1602);
                    subquery235=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery235.getTree());

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
    // JPA.g:250:1: string_primary : ( path_expression | STRINGLITERAL | input_parameter | functions_returning_strings | aggregate_expression );
    public final JPAParser.string_primary_return string_primary() throws RecognitionException {
        JPAParser.string_primary_return retval = new JPAParser.string_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRINGLITERAL237=null;
        JPAParser.path_expression_return path_expression236 = null;

        JPAParser.input_parameter_return input_parameter238 = null;

        JPAParser.functions_returning_strings_return functions_returning_strings239 = null;

        JPAParser.aggregate_expression_return aggregate_expression240 = null;


        Object STRINGLITERAL237_tree=null;

        try {
            // JPA.g:251:2: ( path_expression | STRINGLITERAL | input_parameter | functions_returning_strings | aggregate_expression )
            int alt74=5;
            switch ( input.LA(1) ) {
            case SIMPLE_FIELD_PATH:
            case FIELD_PATH:
                {
                alt74=1;
                }
                break;
            case STRINGLITERAL:
                {
                alt74=2;
                }
                break;
            case 93:
            case 94:
                {
                alt74=3;
                }
                break;
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
                {
                alt74=4;
                }
                break;
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
                {
                alt74=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 74, 0, input);

                throw nvae;
            }

            switch (alt74) {
                case 1 :
                    // JPA.g:251:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_string_primary1611);
                    path_expression236=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression236.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:252:4: STRINGLITERAL
                    {
                    root_0 = (Object)adaptor.nil();

                    STRINGLITERAL237=(Token)match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_string_primary1616); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRINGLITERAL237_tree = (Object)adaptor.create(STRINGLITERAL237);
                    adaptor.addChild(root_0, STRINGLITERAL237_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:253:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_string_primary1621);
                    input_parameter238=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter238.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:254:4: functions_returning_strings
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_strings_in_string_primary1626);
                    functions_returning_strings239=functions_returning_strings();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings239.getTree());

                    }
                    break;
                case 5 :
                    // JPA.g:255:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_string_primary1631);
                    aggregate_expression240=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression240.getTree());

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
    // JPA.g:257:1: datetime_expression : ( datetime_primary | subquery );
    public final JPAParser.datetime_expression_return datetime_expression() throws RecognitionException {
        JPAParser.datetime_expression_return retval = new JPAParser.datetime_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.datetime_primary_return datetime_primary241 = null;

        JPAParser.subquery_return subquery242 = null;



        try {
            // JPA.g:258:2: ( datetime_primary | subquery )
            int alt75=2;
            int LA75_0 = input.LA(1);

            if ( ((LA75_0>=SIMPLE_FIELD_PATH && LA75_0<=FIELD_PATH)||(LA75_0>=38 && LA75_0<=42)||(LA75_0>=81 && LA75_0<=83)||(LA75_0>=93 && LA75_0<=94)) ) {
                alt75=1;
            }
            else if ( (LA75_0==26) ) {
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
                    // JPA.g:258:4: datetime_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_datetime_primary_in_datetime_expression1640);
                    datetime_primary241=datetime_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_primary241.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:259:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_datetime_expression1645);
                    subquery242=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery242.getTree());

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
    // JPA.g:261:1: datetime_primary : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression );
    public final JPAParser.datetime_primary_return datetime_primary() throws RecognitionException {
        JPAParser.datetime_primary_return retval = new JPAParser.datetime_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression243 = null;

        JPAParser.input_parameter_return input_parameter244 = null;

        JPAParser.functions_returning_datetime_return functions_returning_datetime245 = null;

        JPAParser.aggregate_expression_return aggregate_expression246 = null;



        try {
            // JPA.g:262:2: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression )
            int alt76=4;
            switch ( input.LA(1) ) {
            case SIMPLE_FIELD_PATH:
            case FIELD_PATH:
                {
                alt76=1;
                }
                break;
            case 93:
            case 94:
                {
                alt76=2;
                }
                break;
            case 81:
            case 82:
            case 83:
                {
                alt76=3;
                }
                break;
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
                {
                alt76=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 76, 0, input);

                throw nvae;
            }

            switch (alt76) {
                case 1 :
                    // JPA.g:262:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_datetime_primary1654);
                    path_expression243=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression243.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:263:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_datetime_primary1659);
                    input_parameter244=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter244.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:264:4: functions_returning_datetime
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functions_returning_datetime_in_datetime_primary1664);
                    functions_returning_datetime245=functions_returning_datetime();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime245.getTree());

                    }
                    break;
                case 4 :
                    // JPA.g:265:4: aggregate_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregate_expression_in_datetime_primary1669);
                    aggregate_expression246=aggregate_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression246.getTree());

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
    // JPA.g:267:1: boolean_expression : ( boolean_primary | subquery );
    public final JPAParser.boolean_expression_return boolean_expression() throws RecognitionException {
        JPAParser.boolean_expression_return retval = new JPAParser.boolean_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.boolean_primary_return boolean_primary247 = null;

        JPAParser.subquery_return subquery248 = null;



        try {
            // JPA.g:268:2: ( boolean_primary | subquery )
            int alt77=2;
            int LA77_0 = input.LA(1);

            if ( ((LA77_0>=SIMPLE_FIELD_PATH && LA77_0<=FIELD_PATH)||(LA77_0>=93 && LA77_0<=96)) ) {
                alt77=1;
            }
            else if ( (LA77_0==26) ) {
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
                    // JPA.g:268:4: boolean_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_primary_in_boolean_expression1678);
                    boolean_primary247=boolean_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_primary247.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:269:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_boolean_expression1683);
                    subquery248=subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery248.getTree());

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
    // JPA.g:271:1: boolean_primary : ( path_expression | boolean_literal | input_parameter );
    public final JPAParser.boolean_primary_return boolean_primary() throws RecognitionException {
        JPAParser.boolean_primary_return retval = new JPAParser.boolean_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression249 = null;

        JPAParser.boolean_literal_return boolean_literal250 = null;

        JPAParser.input_parameter_return input_parameter251 = null;



        try {
            // JPA.g:272:2: ( path_expression | boolean_literal | input_parameter )
            int alt78=3;
            switch ( input.LA(1) ) {
            case SIMPLE_FIELD_PATH:
            case FIELD_PATH:
                {
                alt78=1;
                }
                break;
            case 95:
            case 96:
                {
                alt78=2;
                }
                break;
            case 93:
            case 94:
                {
                alt78=3;
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
                    // JPA.g:272:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_boolean_primary1692);
                    path_expression249=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression249.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:273:4: boolean_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_boolean_literal_in_boolean_primary1697);
                    boolean_literal250=boolean_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal250.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:274:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_boolean_primary1702);
                    input_parameter251=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter251.getTree());

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
    // JPA.g:276:1: enum_expression : ( enum_primary | subquery );
    public final JPAParser.enum_expression_return enum_expression() throws RecognitionException {
        JPAParser.enum_expression_return retval = new JPAParser.enum_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.enum_primary_return enum_primary252 = null;

        JPAParser.subquery_return subquery253 = null;



        try {
            // JPA.g:277:2: ( enum_primary | subquery )
            int alt79=2;
            int LA79_0 = input.LA(1);

            if ( (LA79_0==WORD||(LA79_0>=SIMPLE_FIELD_PATH && LA79_0<=FIELD_PATH)||(LA79_0>=93 && LA79_0<=94)) ) {
                alt79=1;
            }
            else if ( (LA79_0==26) ) {
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
                    // JPA.g:277:4: enum_primary
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_primary_in_enum_expression1711);
                    enum_primary252=enum_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_primary252.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:278:4: subquery
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_subquery_in_enum_expression1716);
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
    // $ANTLR end "enum_expression"

    public static class enum_primary_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "enum_primary"
    // JPA.g:280:1: enum_primary : ( path_expression | enum_literal | input_parameter );
    public final JPAParser.enum_primary_return enum_primary() throws RecognitionException {
        JPAParser.enum_primary_return retval = new JPAParser.enum_primary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression254 = null;

        JPAParser.enum_literal_return enum_literal255 = null;

        JPAParser.input_parameter_return input_parameter256 = null;



        try {
            // JPA.g:281:2: ( path_expression | enum_literal | input_parameter )
            int alt80=3;
            switch ( input.LA(1) ) {
            case SIMPLE_FIELD_PATH:
            case FIELD_PATH:
                {
                alt80=1;
                }
                break;
            case WORD:
                {
                alt80=2;
                }
                break;
            case 93:
            case 94:
                {
                alt80=3;
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
                    // JPA.g:281:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_enum_primary1725);
                    path_expression254=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression254.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:282:4: enum_literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_literal_in_enum_primary1730);
                    enum_literal255=enum_literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal255.getTree());

                    }
                    break;
                case 3 :
                    // JPA.g:283:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_enum_primary1735);
                    input_parameter256=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter256.getTree());

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
    // JPA.g:285:1: entity_expression : ( path_expression | simple_entity_expression );
    public final JPAParser.entity_expression_return entity_expression() throws RecognitionException {
        JPAParser.entity_expression_return retval = new JPAParser.entity_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.path_expression_return path_expression257 = null;

        JPAParser.simple_entity_expression_return simple_entity_expression258 = null;



        try {
            // JPA.g:286:2: ( path_expression | simple_entity_expression )
            int alt81=2;
            int LA81_0 = input.LA(1);

            if ( ((LA81_0>=SIMPLE_FIELD_PATH && LA81_0<=FIELD_PATH)) ) {
                alt81=1;
            }
            else if ( (LA81_0==WORD||(LA81_0>=93 && LA81_0<=94)) ) {
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
                    // JPA.g:286:4: path_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_path_expression_in_entity_expression1744);
                    path_expression257=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression257.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:287:4: simple_entity_expression
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simple_entity_expression_in_entity_expression1749);
                    simple_entity_expression258=simple_entity_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression258.getTree());

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
    // JPA.g:289:1: simple_entity_expression : ( identification_variable | input_parameter );
    public final JPAParser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
        JPAParser.simple_entity_expression_return retval = new JPAParser.simple_entity_expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        JPAParser.identification_variable_return identification_variable259 = null;

        JPAParser.input_parameter_return input_parameter260 = null;



        try {
            // JPA.g:290:2: ( identification_variable | input_parameter )
            int alt82=2;
            int LA82_0 = input.LA(1);

            if ( (LA82_0==WORD) ) {
                alt82=1;
            }
            else if ( ((LA82_0>=93 && LA82_0<=94)) ) {
                alt82=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 82, 0, input);

                throw nvae;
            }
            switch (alt82) {
                case 1 :
                    // JPA.g:290:4: identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identification_variable_in_simple_entity_expression1758);
                    identification_variable259=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable259.getTree());

                    }
                    break;
                case 2 :
                    // JPA.g:291:4: input_parameter
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_input_parameter_in_simple_entity_expression1763);
                    input_parameter260=input_parameter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter260.getTree());

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
    // JPA.g:293:1: functions_returning_numerics : ( 'LENGTH' '(' string_primary ')' | 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')' | 'ABS' '(' simple_arithmetic_expression ')' | 'SQRT' '(' simple_arithmetic_expression ')' | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'SIZE' '(' path_expression ')' );
    public final JPAParser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
        JPAParser.functions_returning_numerics_return retval = new JPAParser.functions_returning_numerics_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal261=null;
        Token char_literal262=null;
        Token char_literal264=null;
        Token string_literal265=null;
        Token char_literal266=null;
        Token char_literal268=null;
        Token char_literal270=null;
        Token char_literal272=null;
        Token string_literal273=null;
        Token char_literal274=null;
        Token char_literal276=null;
        Token string_literal277=null;
        Token char_literal278=null;
        Token char_literal280=null;
        Token string_literal281=null;
        Token char_literal282=null;
        Token char_literal284=null;
        Token char_literal286=null;
        Token string_literal287=null;
        Token char_literal288=null;
        Token char_literal290=null;
        JPAParser.string_primary_return string_primary263 = null;

        JPAParser.string_primary_return string_primary267 = null;

        JPAParser.string_primary_return string_primary269 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression271 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression275 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression279 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression283 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression285 = null;

        JPAParser.path_expression_return path_expression289 = null;


        Object string_literal261_tree=null;
        Object char_literal262_tree=null;
        Object char_literal264_tree=null;
        Object string_literal265_tree=null;
        Object char_literal266_tree=null;
        Object char_literal268_tree=null;
        Object char_literal270_tree=null;
        Object char_literal272_tree=null;
        Object string_literal273_tree=null;
        Object char_literal274_tree=null;
        Object char_literal276_tree=null;
        Object string_literal277_tree=null;
        Object char_literal278_tree=null;
        Object char_literal280_tree=null;
        Object string_literal281_tree=null;
        Object char_literal282_tree=null;
        Object char_literal284_tree=null;
        Object char_literal286_tree=null;
        Object string_literal287_tree=null;
        Object char_literal288_tree=null;
        Object char_literal290_tree=null;

        try {
            // JPA.g:294:2: ( 'LENGTH' '(' string_primary ')' | 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')' | 'ABS' '(' simple_arithmetic_expression ')' | 'SQRT' '(' simple_arithmetic_expression ')' | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'SIZE' '(' path_expression ')' )
            int alt84=6;
            switch ( input.LA(1) ) {
            case 75:
                {
                alt84=1;
                }
                break;
            case 76:
                {
                alt84=2;
                }
                break;
            case 77:
                {
                alt84=3;
                }
                break;
            case 78:
                {
                alt84=4;
                }
                break;
            case 79:
                {
                alt84=5;
                }
                break;
            case 80:
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
                    // JPA.g:294:4: 'LENGTH' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal261=(Token)match(input,75,FOLLOW_75_in_functions_returning_numerics1772); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal261_tree = (Object)adaptor.create(string_literal261);
                    adaptor.addChild(root_0, string_literal261_tree);
                    }
                    char_literal262=(Token)match(input,34,FOLLOW_34_in_functions_returning_numerics1774); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal262_tree = (Object)adaptor.create(char_literal262);
                    adaptor.addChild(root_0, char_literal262_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics1775);
                    string_primary263=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary263.getTree());
                    char_literal264=(Token)match(input,27,FOLLOW_27_in_functions_returning_numerics1776); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal264_tree = (Object)adaptor.create(char_literal264);
                    adaptor.addChild(root_0, char_literal264_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:295:4: 'LOCATE' '(' string_primary ',' string_primary ( ',' simple_arithmetic_expression )? ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal265=(Token)match(input,76,FOLLOW_76_in_functions_returning_numerics1781); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal265_tree = (Object)adaptor.create(string_literal265);
                    adaptor.addChild(root_0, string_literal265_tree);
                    }
                    char_literal266=(Token)match(input,34,FOLLOW_34_in_functions_returning_numerics1783); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal266_tree = (Object)adaptor.create(char_literal266);
                    adaptor.addChild(root_0, char_literal266_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics1784);
                    string_primary267=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary267.getTree());
                    char_literal268=(Token)match(input,24,FOLLOW_24_in_functions_returning_numerics1785); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal268_tree = (Object)adaptor.create(char_literal268);
                    adaptor.addChild(root_0, char_literal268_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_numerics1787);
                    string_primary269=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary269.getTree());
                    // JPA.g:295:48: ( ',' simple_arithmetic_expression )?
                    int alt83=2;
                    int LA83_0 = input.LA(1);

                    if ( (LA83_0==24) ) {
                        alt83=1;
                    }
                    switch (alt83) {
                        case 1 :
                            // JPA.g:295:49: ',' simple_arithmetic_expression
                            {
                            char_literal270=(Token)match(input,24,FOLLOW_24_in_functions_returning_numerics1789); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal270_tree = (Object)adaptor.create(char_literal270);
                            adaptor.addChild(root_0, char_literal270_tree);
                            }
                            pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics1791);
                            simple_arithmetic_expression271=simple_arithmetic_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression271.getTree());

                            }
                            break;

                    }

                    char_literal272=(Token)match(input,27,FOLLOW_27_in_functions_returning_numerics1794); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal272_tree = (Object)adaptor.create(char_literal272);
                    adaptor.addChild(root_0, char_literal272_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:296:4: 'ABS' '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal273=(Token)match(input,77,FOLLOW_77_in_functions_returning_numerics1799); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal273_tree = (Object)adaptor.create(string_literal273);
                    adaptor.addChild(root_0, string_literal273_tree);
                    }
                    char_literal274=(Token)match(input,34,FOLLOW_34_in_functions_returning_numerics1801); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal274_tree = (Object)adaptor.create(char_literal274);
                    adaptor.addChild(root_0, char_literal274_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics1802);
                    simple_arithmetic_expression275=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression275.getTree());
                    char_literal276=(Token)match(input,27,FOLLOW_27_in_functions_returning_numerics1803); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal276_tree = (Object)adaptor.create(char_literal276);
                    adaptor.addChild(root_0, char_literal276_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:297:4: 'SQRT' '(' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal277=(Token)match(input,78,FOLLOW_78_in_functions_returning_numerics1808); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal277_tree = (Object)adaptor.create(string_literal277);
                    adaptor.addChild(root_0, string_literal277_tree);
                    }
                    char_literal278=(Token)match(input,34,FOLLOW_34_in_functions_returning_numerics1810); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal278_tree = (Object)adaptor.create(char_literal278);
                    adaptor.addChild(root_0, char_literal278_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics1811);
                    simple_arithmetic_expression279=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression279.getTree());
                    char_literal280=(Token)match(input,27,FOLLOW_27_in_functions_returning_numerics1812); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal280_tree = (Object)adaptor.create(char_literal280);
                    adaptor.addChild(root_0, char_literal280_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:298:4: 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal281=(Token)match(input,79,FOLLOW_79_in_functions_returning_numerics1817); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal281_tree = (Object)adaptor.create(string_literal281);
                    adaptor.addChild(root_0, string_literal281_tree);
                    }
                    char_literal282=(Token)match(input,34,FOLLOW_34_in_functions_returning_numerics1819); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal282_tree = (Object)adaptor.create(char_literal282);
                    adaptor.addChild(root_0, char_literal282_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics1820);
                    simple_arithmetic_expression283=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression283.getTree());
                    char_literal284=(Token)match(input,24,FOLLOW_24_in_functions_returning_numerics1821); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal284_tree = (Object)adaptor.create(char_literal284);
                    adaptor.addChild(root_0, char_literal284_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics1823);
                    simple_arithmetic_expression285=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression285.getTree());
                    char_literal286=(Token)match(input,27,FOLLOW_27_in_functions_returning_numerics1824); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal286_tree = (Object)adaptor.create(char_literal286);
                    adaptor.addChild(root_0, char_literal286_tree);
                    }

                    }
                    break;
                case 6 :
                    // JPA.g:299:4: 'SIZE' '(' path_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal287=(Token)match(input,80,FOLLOW_80_in_functions_returning_numerics1829); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal287_tree = (Object)adaptor.create(string_literal287);
                    adaptor.addChild(root_0, string_literal287_tree);
                    }
                    char_literal288=(Token)match(input,34,FOLLOW_34_in_functions_returning_numerics1831); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal288_tree = (Object)adaptor.create(char_literal288);
                    adaptor.addChild(root_0, char_literal288_tree);
                    }
                    pushFollow(FOLLOW_path_expression_in_functions_returning_numerics1832);
                    path_expression289=path_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression289.getTree());
                    char_literal290=(Token)match(input,27,FOLLOW_27_in_functions_returning_numerics1833); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal290_tree = (Object)adaptor.create(char_literal290);
                    adaptor.addChild(root_0, char_literal290_tree);
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
    // JPA.g:301:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
    public final JPAParser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
        JPAParser.functions_returning_datetime_return retval = new JPAParser.functions_returning_datetime_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set291=null;

        Object set291_tree=null;

        try {
            // JPA.g:302:2: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set291=(Token)input.LT(1);
            if ( (input.LA(1)>=81 && input.LA(1)<=83) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set291));
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
    // JPA.g:306:1: functions_returning_strings : ( 'CONCAT' '(' string_primary ',' string_primary ')' | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')' | 'LOWER' '(' string_primary ')' | 'UPPER' '(' string_primary ')' );
    public final JPAParser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
        JPAParser.functions_returning_strings_return retval = new JPAParser.functions_returning_strings_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal292=null;
        Token char_literal293=null;
        Token char_literal295=null;
        Token char_literal297=null;
        Token string_literal298=null;
        Token char_literal299=null;
        Token char_literal301=null;
        Token char_literal303=null;
        Token char_literal305=null;
        Token string_literal306=null;
        Token char_literal307=null;
        Token TRIM_CHARACTER309=null;
        Token string_literal310=null;
        Token char_literal312=null;
        Token string_literal313=null;
        Token char_literal314=null;
        Token char_literal316=null;
        Token string_literal317=null;
        Token char_literal318=null;
        Token char_literal320=null;
        JPAParser.string_primary_return string_primary294 = null;

        JPAParser.string_primary_return string_primary296 = null;

        JPAParser.string_primary_return string_primary300 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression302 = null;

        JPAParser.simple_arithmetic_expression_return simple_arithmetic_expression304 = null;

        JPAParser.trim_specification_return trim_specification308 = null;

        JPAParser.string_primary_return string_primary311 = null;

        JPAParser.string_primary_return string_primary315 = null;

        JPAParser.string_primary_return string_primary319 = null;


        Object string_literal292_tree=null;
        Object char_literal293_tree=null;
        Object char_literal295_tree=null;
        Object char_literal297_tree=null;
        Object string_literal298_tree=null;
        Object char_literal299_tree=null;
        Object char_literal301_tree=null;
        Object char_literal303_tree=null;
        Object char_literal305_tree=null;
        Object string_literal306_tree=null;
        Object char_literal307_tree=null;
        Object TRIM_CHARACTER309_tree=null;
        Object string_literal310_tree=null;
        Object char_literal312_tree=null;
        Object string_literal313_tree=null;
        Object char_literal314_tree=null;
        Object char_literal316_tree=null;
        Object string_literal317_tree=null;
        Object char_literal318_tree=null;
        Object char_literal320_tree=null;

        try {
            // JPA.g:307:2: ( 'CONCAT' '(' string_primary ',' string_primary ')' | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')' | 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')' | 'LOWER' '(' string_primary ')' | 'UPPER' '(' string_primary ')' )
            int alt88=5;
            switch ( input.LA(1) ) {
            case 84:
                {
                alt88=1;
                }
                break;
            case 85:
                {
                alt88=2;
                }
                break;
            case 86:
                {
                alt88=3;
                }
                break;
            case 87:
                {
                alt88=4;
                }
                break;
            case 88:
                {
                alt88=5;
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
                    // JPA.g:307:4: 'CONCAT' '(' string_primary ',' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal292=(Token)match(input,84,FOLLOW_84_in_functions_returning_strings1861); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal292_tree = (Object)adaptor.create(string_literal292);
                    adaptor.addChild(root_0, string_literal292_tree);
                    }
                    char_literal293=(Token)match(input,34,FOLLOW_34_in_functions_returning_strings1863); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal293_tree = (Object)adaptor.create(char_literal293);
                    adaptor.addChild(root_0, char_literal293_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings1864);
                    string_primary294=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary294.getTree());
                    char_literal295=(Token)match(input,24,FOLLOW_24_in_functions_returning_strings1865); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal295_tree = (Object)adaptor.create(char_literal295);
                    adaptor.addChild(root_0, char_literal295_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings1867);
                    string_primary296=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary296.getTree());
                    char_literal297=(Token)match(input,27,FOLLOW_27_in_functions_returning_strings1868); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal297_tree = (Object)adaptor.create(char_literal297);
                    adaptor.addChild(root_0, char_literal297_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:308:4: 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal298=(Token)match(input,85,FOLLOW_85_in_functions_returning_strings1873); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal298_tree = (Object)adaptor.create(string_literal298);
                    adaptor.addChild(root_0, string_literal298_tree);
                    }
                    char_literal299=(Token)match(input,34,FOLLOW_34_in_functions_returning_strings1875); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal299_tree = (Object)adaptor.create(char_literal299);
                    adaptor.addChild(root_0, char_literal299_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings1876);
                    string_primary300=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary300.getTree());
                    char_literal301=(Token)match(input,24,FOLLOW_24_in_functions_returning_strings1877); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal301_tree = (Object)adaptor.create(char_literal301);
                    adaptor.addChild(root_0, char_literal301_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_strings1878);
                    simple_arithmetic_expression302=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression302.getTree());
                    char_literal303=(Token)match(input,24,FOLLOW_24_in_functions_returning_strings1879); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal303_tree = (Object)adaptor.create(char_literal303);
                    adaptor.addChild(root_0, char_literal303_tree);
                    }
                    pushFollow(FOLLOW_simple_arithmetic_expression_in_functions_returning_strings1881);
                    simple_arithmetic_expression304=simple_arithmetic_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_arithmetic_expression304.getTree());
                    char_literal305=(Token)match(input,27,FOLLOW_27_in_functions_returning_strings1882); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal305_tree = (Object)adaptor.create(char_literal305);
                    adaptor.addChild(root_0, char_literal305_tree);
                    }

                    }
                    break;
                case 3 :
                    // JPA.g:309:4: 'TRIM' '(' ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )? string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal306=(Token)match(input,86,FOLLOW_86_in_functions_returning_strings1887); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal306_tree = (Object)adaptor.create(string_literal306);
                    adaptor.addChild(root_0, string_literal306_tree);
                    }
                    char_literal307=(Token)match(input,34,FOLLOW_34_in_functions_returning_strings1889); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal307_tree = (Object)adaptor.create(char_literal307);
                    adaptor.addChild(root_0, char_literal307_tree);
                    }
                    // JPA.g:309:14: ( ( trim_specification )? ( TRIM_CHARACTER )? 'FROM' )?
                    int alt87=2;
                    int LA87_0 = input.LA(1);

                    if ( (LA87_0==TRIM_CHARACTER||LA87_0==23||(LA87_0>=89 && LA87_0<=91)) ) {
                        alt87=1;
                    }
                    switch (alt87) {
                        case 1 :
                            // JPA.g:309:15: ( trim_specification )? ( TRIM_CHARACTER )? 'FROM'
                            {
                            // JPA.g:309:15: ( trim_specification )?
                            int alt85=2;
                            int LA85_0 = input.LA(1);

                            if ( ((LA85_0>=89 && LA85_0<=91)) ) {
                                alt85=1;
                            }
                            switch (alt85) {
                                case 1 :
                                    // JPA.g:309:16: trim_specification
                                    {
                                    pushFollow(FOLLOW_trim_specification_in_functions_returning_strings1892);
                                    trim_specification308=trim_specification();

                                    state._fsp--;
                                    if (state.failed) return retval;
                                    if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification308.getTree());

                                    }
                                    break;

                            }

                            // JPA.g:309:37: ( TRIM_CHARACTER )?
                            int alt86=2;
                            int LA86_0 = input.LA(1);

                            if ( (LA86_0==TRIM_CHARACTER) ) {
                                alt86=1;
                            }
                            switch (alt86) {
                                case 1 :
                                    // JPA.g:309:38: TRIM_CHARACTER
                                    {
                                    TRIM_CHARACTER309=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_functions_returning_strings1897); if (state.failed) return retval;
                                    if ( state.backtracking==0 ) {
                                    TRIM_CHARACTER309_tree = (Object)adaptor.create(TRIM_CHARACTER309);
                                    adaptor.addChild(root_0, TRIM_CHARACTER309_tree);
                                    }

                                    }
                                    break;

                            }

                            string_literal310=(Token)match(input,23,FOLLOW_23_in_functions_returning_strings1901); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal310_tree = (Object)adaptor.create(string_literal310);
                            adaptor.addChild(root_0, string_literal310_tree);
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings1905);
                    string_primary311=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary311.getTree());
                    char_literal312=(Token)match(input,27,FOLLOW_27_in_functions_returning_strings1906); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal312_tree = (Object)adaptor.create(char_literal312);
                    adaptor.addChild(root_0, char_literal312_tree);
                    }

                    }
                    break;
                case 4 :
                    // JPA.g:310:4: 'LOWER' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal313=(Token)match(input,87,FOLLOW_87_in_functions_returning_strings1911); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal313_tree = (Object)adaptor.create(string_literal313);
                    adaptor.addChild(root_0, string_literal313_tree);
                    }
                    char_literal314=(Token)match(input,34,FOLLOW_34_in_functions_returning_strings1913); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal314_tree = (Object)adaptor.create(char_literal314);
                    adaptor.addChild(root_0, char_literal314_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings1914);
                    string_primary315=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary315.getTree());
                    char_literal316=(Token)match(input,27,FOLLOW_27_in_functions_returning_strings1915); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal316_tree = (Object)adaptor.create(char_literal316);
                    adaptor.addChild(root_0, char_literal316_tree);
                    }

                    }
                    break;
                case 5 :
                    // JPA.g:311:4: 'UPPER' '(' string_primary ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    string_literal317=(Token)match(input,88,FOLLOW_88_in_functions_returning_strings1920); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal317_tree = (Object)adaptor.create(string_literal317);
                    adaptor.addChild(root_0, string_literal317_tree);
                    }
                    char_literal318=(Token)match(input,34,FOLLOW_34_in_functions_returning_strings1922); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal318_tree = (Object)adaptor.create(char_literal318);
                    adaptor.addChild(root_0, char_literal318_tree);
                    }
                    pushFollow(FOLLOW_string_primary_in_functions_returning_strings1923);
                    string_primary319=string_primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_primary319.getTree());
                    char_literal320=(Token)match(input,27,FOLLOW_27_in_functions_returning_strings1924); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal320_tree = (Object)adaptor.create(char_literal320);
                    adaptor.addChild(root_0, char_literal320_tree);
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
    // JPA.g:313:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
    public final JPAParser.trim_specification_return trim_specification() throws RecognitionException {
        JPAParser.trim_specification_return retval = new JPAParser.trim_specification_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set321=null;

        Object set321_tree=null;

        try {
            // JPA.g:314:2: ( 'LEADING' | 'TRAILING' | 'BOTH' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set321=(Token)input.LT(1);
            if ( (input.LA(1)>=89 && input.LA(1)<=91) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set321));
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
    // JPA.g:319:1: abstract_schema_name : WORD ;
    public final JPAParser.abstract_schema_name_return abstract_schema_name() throws RecognitionException {
        JPAParser.abstract_schema_name_return retval = new JPAParser.abstract_schema_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD322=null;

        Object WORD322_tree=null;

        try {
            // JPA.g:320:4: ( WORD )
            // JPA.g:320:6: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD322=(Token)match(input,WORD,FOLLOW_WORD_in_abstract_schema_name1955); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD322_tree = (Object)adaptor.create(WORD322);
            adaptor.addChild(root_0, WORD322_tree);
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
    // JPA.g:323:1: pattern_value : WORD ;
    public final JPAParser.pattern_value_return pattern_value() throws RecognitionException {
        JPAParser.pattern_value_return retval = new JPAParser.pattern_value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD323=null;

        Object WORD323_tree=null;

        try {
            // JPA.g:324:2: ( WORD )
            // JPA.g:324:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD323=(Token)match(input,WORD,FOLLOW_WORD_in_pattern_value1965); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD323_tree = (Object)adaptor.create(WORD323);
            adaptor.addChild(root_0, WORD323_tree);
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
    // JPA.g:327:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
    public final JPAParser.numeric_literal_return numeric_literal() throws RecognitionException {
        JPAParser.numeric_literal_return retval = new JPAParser.numeric_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal324=null;
        Token INT_NUMERAL325=null;

        Object string_literal324_tree=null;
        Object INT_NUMERAL325_tree=null;

        try {
            // JPA.g:328:2: ( ( '0x' )? INT_NUMERAL )
            // JPA.g:328:4: ( '0x' )? INT_NUMERAL
            {
            root_0 = (Object)adaptor.nil();

            // JPA.g:328:4: ( '0x' )?
            int alt89=2;
            int LA89_0 = input.LA(1);

            if ( (LA89_0==92) ) {
                alt89=1;
            }
            switch (alt89) {
                case 1 :
                    // JPA.g:328:5: '0x'
                    {
                    string_literal324=(Token)match(input,92,FOLLOW_92_in_numeric_literal1976); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal324_tree = (Object)adaptor.create(string_literal324);
                    adaptor.addChild(root_0, string_literal324_tree);
                    }

                    }
                    break;

            }

            INT_NUMERAL325=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal1980); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            INT_NUMERAL325_tree = (Object)adaptor.create(INT_NUMERAL325);
            adaptor.addChild(root_0, INT_NUMERAL325_tree);
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
    // JPA.g:330:1: input_parameter : ( '?' INT_NUMERAL | ':' identification_variable );
    public final JPAParser.input_parameter_return input_parameter() throws RecognitionException {
        JPAParser.input_parameter_return retval = new JPAParser.input_parameter_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal326=null;
        Token INT_NUMERAL327=null;
        Token char_literal328=null;
        JPAParser.identification_variable_return identification_variable329 = null;


        Object char_literal326_tree=null;
        Object INT_NUMERAL327_tree=null;
        Object char_literal328_tree=null;

        try {
            // JPA.g:331:2: ( '?' INT_NUMERAL | ':' identification_variable )
            int alt90=2;
            int LA90_0 = input.LA(1);

            if ( (LA90_0==93) ) {
                alt90=1;
            }
            else if ( (LA90_0==94) ) {
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
                    // JPA.g:331:4: '?' INT_NUMERAL
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal326=(Token)match(input,93,FOLLOW_93_in_input_parameter1990); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal326_tree = (Object)adaptor.create(char_literal326);
                    adaptor.addChild(root_0, char_literal326_tree);
                    }
                    INT_NUMERAL327=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_input_parameter1992); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT_NUMERAL327_tree = (Object)adaptor.create(INT_NUMERAL327);
                    adaptor.addChild(root_0, INT_NUMERAL327_tree);
                    }

                    }
                    break;
                case 2 :
                    // JPA.g:332:4: ':' identification_variable
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal328=(Token)match(input,94,FOLLOW_94_in_input_parameter1997); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal328_tree = (Object)adaptor.create(char_literal328);
                    adaptor.addChild(root_0, char_literal328_tree);
                    }
                    pushFollow(FOLLOW_identification_variable_in_input_parameter1999);
                    identification_variable329=identification_variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable329.getTree());

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
    // JPA.g:334:1: literal : WORD ;
    public final JPAParser.literal_return literal() throws RecognitionException {
        JPAParser.literal_return retval = new JPAParser.literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD330=null;

        Object WORD330_tree=null;

        try {
            // JPA.g:335:2: ( WORD )
            // JPA.g:335:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD330=(Token)match(input,WORD,FOLLOW_WORD_in_literal2008); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD330_tree = (Object)adaptor.create(WORD330);
            adaptor.addChild(root_0, WORD330_tree);
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
    // JPA.g:337:1: constructor_name : WORD ;
    public final JPAParser.constructor_name_return constructor_name() throws RecognitionException {
        JPAParser.constructor_name_return retval = new JPAParser.constructor_name_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD331=null;

        Object WORD331_tree=null;

        try {
            // JPA.g:338:2: ( WORD )
            // JPA.g:338:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD331=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name2017); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD331_tree = (Object)adaptor.create(WORD331);
            adaptor.addChild(root_0, WORD331_tree);
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
    // JPA.g:340:1: enum_literal : WORD ;
    public final JPAParser.enum_literal_return enum_literal() throws RecognitionException {
        JPAParser.enum_literal_return retval = new JPAParser.enum_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD332=null;

        Object WORD332_tree=null;

        try {
            // JPA.g:341:2: ( WORD )
            // JPA.g:341:4: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD332=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal2058); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD332_tree = (Object)adaptor.create(WORD332);
            adaptor.addChild(root_0, WORD332_tree);
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
    // JPA.g:343:1: boolean_literal : ( 'true' | 'false' );
    public final JPAParser.boolean_literal_return boolean_literal() throws RecognitionException {
        JPAParser.boolean_literal_return retval = new JPAParser.boolean_literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set333=null;

        Object set333_tree=null;

        try {
            // JPA.g:344:2: ( 'true' | 'false' )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set333=(Token)input.LT(1);
            if ( (input.LA(1)>=95 && input.LA(1)<=96) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set333));
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

    public static class join_field_path_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "join_field_path"
    // JPA.g:348:1: join_field_path : SIMPLE_FIELD_PATH ;
    public final JPAParser.join_field_path_return join_field_path() throws RecognitionException {
        JPAParser.join_field_path_return retval = new JPAParser.join_field_path_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SIMPLE_FIELD_PATH334=null;

        Object SIMPLE_FIELD_PATH334_tree=null;

        try {
            // JPA.g:349:5: ( SIMPLE_FIELD_PATH )
            // JPA.g:349:7: SIMPLE_FIELD_PATH
            {
            root_0 = (Object)adaptor.nil();

            SIMPLE_FIELD_PATH334=(Token)match(input,SIMPLE_FIELD_PATH,FOLLOW_SIMPLE_FIELD_PATH_in_join_field_path2085); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            SIMPLE_FIELD_PATH334_tree = (Object)adaptor.create(SIMPLE_FIELD_PATH334);
            adaptor.addChild(root_0, SIMPLE_FIELD_PATH334_tree);
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
    // $ANTLR end "join_field_path"

    public static class field_path_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "field_path"
    // JPA.g:351:1: field_path : ( SIMPLE_FIELD_PATH | FIELD_PATH );
    public final JPAParser.field_path_return field_path() throws RecognitionException {
        JPAParser.field_path_return retval = new JPAParser.field_path_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set335=null;

        Object set335_tree=null;

        try {
            // JPA.g:352:4: ( SIMPLE_FIELD_PATH | FIELD_PATH )
            // JPA.g:
            {
            root_0 = (Object)adaptor.nil();

            set335=(Token)input.LT(1);
            if ( (input.LA(1)>=SIMPLE_FIELD_PATH && input.LA(1)<=FIELD_PATH) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set335));
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
    // $ANTLR end "field_path"

    public static class identification_variable_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "identification_variable"
    // JPA.g:354:1: identification_variable : WORD ;
    public final JPAParser.identification_variable_return identification_variable() throws RecognitionException {
        JPAParser.identification_variable_return retval = new JPAParser.identification_variable_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD336=null;

        Object WORD336_tree=null;

        try {
            // JPA.g:355:4: ( WORD )
            // JPA.g:355:6: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD336=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable2111); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            WORD336_tree = (Object)adaptor.create(WORD336);
            adaptor.addChild(root_0, WORD336_tree);
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

    // $ANTLR start synpred53_JPA
    public final void synpred53_JPA_fragment() throws RecognitionException {   
        // JPA.g:164:5: ( 'NOT' )
        // JPA.g:164:5: 'NOT'
        {
        match(input,52,FOLLOW_52_in_synpred53_JPA1026); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred53_JPA

    // $ANTLR start synpred54_JPA
    public final void synpred54_JPA_fragment() throws RecognitionException {   
        // JPA.g:164:4: ( ( 'NOT' )? simple_cond_expression )
        // JPA.g:164:4: ( 'NOT' )? simple_cond_expression
        {
        // JPA.g:164:4: ( 'NOT' )?
        int alt95=2;
        int LA95_0 = input.LA(1);

        if ( (LA95_0==52) ) {
            int LA95_1 = input.LA(2);

            if ( (synpred53_JPA()) ) {
                alt95=1;
            }
        }
        switch (alt95) {
            case 1 :
                // JPA.g:164:5: 'NOT'
                {
                match(input,52,FOLLOW_52_in_synpred54_JPA1026); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_simple_cond_expression_in_synpred54_JPA1030);
        simple_cond_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred54_JPA

    // $ANTLR start synpred55_JPA
    public final void synpred55_JPA_fragment() throws RecognitionException {   
        // JPA.g:167:4: ( comparison_expression )
        // JPA.g:167:4: comparison_expression
        {
        pushFollow(FOLLOW_comparison_expression_in_synpred55_JPA1045);
        comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred55_JPA

    // $ANTLR start synpred56_JPA
    public final void synpred56_JPA_fragment() throws RecognitionException {   
        // JPA.g:168:4: ( between_expression )
        // JPA.g:168:4: between_expression
        {
        pushFollow(FOLLOW_between_expression_in_synpred56_JPA1050);
        between_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred56_JPA

    // $ANTLR start synpred57_JPA
    public final void synpred57_JPA_fragment() throws RecognitionException {   
        // JPA.g:169:4: ( like_expression )
        // JPA.g:169:4: like_expression
        {
        pushFollow(FOLLOW_like_expression_in_synpred57_JPA1055);
        like_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred57_JPA

    // $ANTLR start synpred58_JPA
    public final void synpred58_JPA_fragment() throws RecognitionException {   
        // JPA.g:170:4: ( in_expression )
        // JPA.g:170:4: in_expression
        {
        pushFollow(FOLLOW_in_expression_in_synpred58_JPA1060);
        in_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred58_JPA

    // $ANTLR start synpred59_JPA
    public final void synpred59_JPA_fragment() throws RecognitionException {   
        // JPA.g:171:4: ( null_comparison_expression )
        // JPA.g:171:4: null_comparison_expression
        {
        pushFollow(FOLLOW_null_comparison_expression_in_synpred59_JPA1065);
        null_comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred59_JPA

    // $ANTLR start synpred60_JPA
    public final void synpred60_JPA_fragment() throws RecognitionException {   
        // JPA.g:172:4: ( empty_collection_comparison_expression )
        // JPA.g:172:4: empty_collection_comparison_expression
        {
        pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred60_JPA1070);
        empty_collection_comparison_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred60_JPA

    // $ANTLR start synpred61_JPA
    public final void synpred61_JPA_fragment() throws RecognitionException {   
        // JPA.g:173:4: ( collection_member_expression )
        // JPA.g:173:4: collection_member_expression
        {
        pushFollow(FOLLOW_collection_member_expression_in_synpred61_JPA1075);
        collection_member_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred61_JPA

    // $ANTLR start synpred63_JPA
    public final void synpred63_JPA_fragment() throws RecognitionException {   
        // JPA.g:177:4: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
        // JPA.g:177:4: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
        {
        pushFollow(FOLLOW_arithmetic_expression_in_synpred63_JPA1089);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:177:26: ( 'NOT' )?
        int alt96=2;
        int LA96_0 = input.LA(1);

        if ( (LA96_0==52) ) {
            alt96=1;
        }
        switch (alt96) {
            case 1 :
                // JPA.g:177:27: 'NOT'
                {
                match(input,52,FOLLOW_52_in_synpred63_JPA1092); if (state.failed) return ;

                }
                break;

        }

        match(input,53,FOLLOW_53_in_synpred63_JPA1096); if (state.failed) return ;
        pushFollow(FOLLOW_arithmetic_expression_in_synpred63_JPA1098);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,51,FOLLOW_51_in_synpred63_JPA1100); if (state.failed) return ;
        pushFollow(FOLLOW_arithmetic_expression_in_synpred63_JPA1102);
        arithmetic_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred63_JPA

    // $ANTLR start synpred65_JPA
    public final void synpred65_JPA_fragment() throws RecognitionException {   
        // JPA.g:178:4: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
        // JPA.g:178:4: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
        {
        pushFollow(FOLLOW_string_expression_in_synpred65_JPA1107);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:178:22: ( 'NOT' )?
        int alt97=2;
        int LA97_0 = input.LA(1);

        if ( (LA97_0==52) ) {
            alt97=1;
        }
        switch (alt97) {
            case 1 :
                // JPA.g:178:23: 'NOT'
                {
                match(input,52,FOLLOW_52_in_synpred65_JPA1110); if (state.failed) return ;

                }
                break;

        }

        match(input,53,FOLLOW_53_in_synpred65_JPA1114); if (state.failed) return ;
        pushFollow(FOLLOW_string_expression_in_synpred65_JPA1116);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,51,FOLLOW_51_in_synpred65_JPA1118); if (state.failed) return ;
        pushFollow(FOLLOW_string_expression_in_synpred65_JPA1120);
        string_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred65_JPA

    // $ANTLR start synpred82_JPA
    public final void synpred82_JPA_fragment() throws RecognitionException {   
        // JPA.g:211:4: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) )
        // JPA.g:211:4: string_expression comparison_operator ( string_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_string_expression_in_synpred82_JPA1332);
        string_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_comparison_operator_in_synpred82_JPA1334);
        comparison_operator();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:211:42: ( string_expression | all_or_any_expression )
        int alt99=2;
        int LA99_0 = input.LA(1);

        if ( (LA99_0==STRINGLITERAL||(LA99_0>=SIMPLE_FIELD_PATH && LA99_0<=FIELD_PATH)||LA99_0==26||(LA99_0>=38 && LA99_0<=42)||(LA99_0>=84 && LA99_0<=88)||(LA99_0>=93 && LA99_0<=94)) ) {
            alt99=1;
        }
        else if ( ((LA99_0>=62 && LA99_0<=64)) ) {
            alt99=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 99, 0, input);

            throw nvae;
        }
        switch (alt99) {
            case 1 :
                // JPA.g:211:43: string_expression
                {
                pushFollow(FOLLOW_string_expression_in_synpred82_JPA1337);
                string_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:211:63: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred82_JPA1341);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred82_JPA

    // $ANTLR start synpred85_JPA
    public final void synpred85_JPA_fragment() throws RecognitionException {   
        // JPA.g:212:4: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
        // JPA.g:212:4: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_boolean_expression_in_synpred85_JPA1347);
        boolean_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=65 && input.LA(1)<=66) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:212:36: ( boolean_expression | all_or_any_expression )
        int alt100=2;
        int LA100_0 = input.LA(1);

        if ( ((LA100_0>=SIMPLE_FIELD_PATH && LA100_0<=FIELD_PATH)||LA100_0==26||(LA100_0>=93 && LA100_0<=96)) ) {
            alt100=1;
        }
        else if ( ((LA100_0>=62 && LA100_0<=64)) ) {
            alt100=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 100, 0, input);

            throw nvae;
        }
        switch (alt100) {
            case 1 :
                // JPA.g:212:37: boolean_expression
                {
                pushFollow(FOLLOW_boolean_expression_in_synpred85_JPA1358);
                boolean_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:212:58: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred85_JPA1362);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred85_JPA

    // $ANTLR start synpred88_JPA
    public final void synpred88_JPA_fragment() throws RecognitionException {   
        // JPA.g:213:4: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
        // JPA.g:213:4: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_enum_expression_in_synpred88_JPA1368);
        enum_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=65 && input.LA(1)<=66) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:213:31: ( enum_expression | all_or_any_expression )
        int alt101=2;
        int LA101_0 = input.LA(1);

        if ( (LA101_0==WORD||(LA101_0>=SIMPLE_FIELD_PATH && LA101_0<=FIELD_PATH)||LA101_0==26||(LA101_0>=93 && LA101_0<=94)) ) {
            alt101=1;
        }
        else if ( ((LA101_0>=62 && LA101_0<=64)) ) {
            alt101=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 101, 0, input);

            throw nvae;
        }
        switch (alt101) {
            case 1 :
                // JPA.g:213:32: enum_expression
                {
                pushFollow(FOLLOW_enum_expression_in_synpred88_JPA1377);
                enum_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:213:50: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred88_JPA1381);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred88_JPA

    // $ANTLR start synpred90_JPA
    public final void synpred90_JPA_fragment() throws RecognitionException {   
        // JPA.g:214:4: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
        // JPA.g:214:4: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_datetime_expression_in_synpred90_JPA1387);
        datetime_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_comparison_operator_in_synpred90_JPA1389);
        comparison_operator();

        state._fsp--;
        if (state.failed) return ;
        // JPA.g:214:44: ( datetime_expression | all_or_any_expression )
        int alt102=2;
        int LA102_0 = input.LA(1);

        if ( ((LA102_0>=SIMPLE_FIELD_PATH && LA102_0<=FIELD_PATH)||LA102_0==26||(LA102_0>=38 && LA102_0<=42)||(LA102_0>=81 && LA102_0<=83)||(LA102_0>=93 && LA102_0<=94)) ) {
            alt102=1;
        }
        else if ( ((LA102_0>=62 && LA102_0<=64)) ) {
            alt102=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 102, 0, input);

            throw nvae;
        }
        switch (alt102) {
            case 1 :
                // JPA.g:214:45: datetime_expression
                {
                pushFollow(FOLLOW_datetime_expression_in_synpred90_JPA1392);
                datetime_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:214:67: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred90_JPA1396);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred90_JPA

    // $ANTLR start synpred93_JPA
    public final void synpred93_JPA_fragment() throws RecognitionException {   
        // JPA.g:215:4: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
        // JPA.g:215:4: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
        {
        pushFollow(FOLLOW_entity_expression_in_synpred93_JPA1402);
        entity_expression();

        state._fsp--;
        if (state.failed) return ;
        if ( (input.LA(1)>=65 && input.LA(1)<=66) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // JPA.g:215:35: ( entity_expression | all_or_any_expression )
        int alt103=2;
        int LA103_0 = input.LA(1);

        if ( (LA103_0==WORD||(LA103_0>=SIMPLE_FIELD_PATH && LA103_0<=FIELD_PATH)||(LA103_0>=93 && LA103_0<=94)) ) {
            alt103=1;
        }
        else if ( ((LA103_0>=62 && LA103_0<=64)) ) {
            alt103=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 103, 0, input);

            throw nvae;
        }
        switch (alt103) {
            case 1 :
                // JPA.g:215:36: entity_expression
                {
                pushFollow(FOLLOW_entity_expression_in_synpred93_JPA1413);
                entity_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;
            case 2 :
                // JPA.g:215:56: all_or_any_expression
                {
                pushFollow(FOLLOW_all_or_any_expression_in_synpred93_JPA1417);
                all_or_any_expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred93_JPA

    // Delegated rules

    public final boolean synpred82_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred82_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred56_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred56_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred90_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred90_JPA_fragment(); // can never throw exception
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
    public final boolean synpred85_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred85_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred54_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred54_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred55_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred55_JPA_fragment(); // can never throw exception
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
    public final boolean synpred93_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred93_JPA_fragment(); // can never throw exception
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
    public final boolean synpred53_JPA() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred53_JPA_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA43 dfa43 = new DFA43(this);
    protected DFA44 dfa44 = new DFA44(this);
    protected DFA48 dfa48 = new DFA48(this);
    protected DFA67 dfa67 = new DFA67(this);
    static final String DFA43_eotS =
        "\35\uffff";
    static final String DFA43_eofS =
        "\35\uffff";
    static final String DFA43_minS =
        "\1\15\23\uffff\1\0\10\uffff";
    static final String DFA43_maxS =
        "\1\140\23\uffff\1\0\10\uffff";
    static final String DFA43_acceptS =
        "\1\uffff\1\1\32\uffff\1\2";
    static final String DFA43_specialS =
        "\24\uffff\1\0\10\uffff}>";
    static final String[] DFA43_transitionS = {
            "\1\1\1\uffff\4\1\7\uffff\1\1\7\uffff\1\24\3\uffff\5\1\11\uffff"+
            "\1\1\10\uffff\1\1\11\uffff\2\1\2\uffff\16\1\3\uffff\5\1",
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
            ""
    };

    static final short[] DFA43_eot = DFA.unpackEncodedString(DFA43_eotS);
    static final short[] DFA43_eof = DFA.unpackEncodedString(DFA43_eofS);
    static final char[] DFA43_min = DFA.unpackEncodedStringToUnsignedChars(DFA43_minS);
    static final char[] DFA43_max = DFA.unpackEncodedStringToUnsignedChars(DFA43_maxS);
    static final short[] DFA43_accept = DFA.unpackEncodedString(DFA43_acceptS);
    static final short[] DFA43_special = DFA.unpackEncodedString(DFA43_specialS);
    static final short[][] DFA43_transition;

    static {
        int numStates = DFA43_transitionS.length;
        DFA43_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA43_transition[i] = DFA.unpackEncodedString(DFA43_transitionS[i]);
        }
    }

    class DFA43 extends DFA {

        public DFA43(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 43;
            this.eot = DFA43_eot;
            this.eof = DFA43_eof;
            this.min = DFA43_min;
            this.max = DFA43_max;
            this.accept = DFA43_accept;
            this.special = DFA43_special;
            this.transition = DFA43_transition;
        }
        public String getDescription() {
            return "163:1: conditional_factor : ( ( 'NOT' )? simple_cond_expression | '(' conditional_expression ')' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA43_20 = input.LA(1);

                         
                        int index43_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred54_JPA()) ) {s = 1;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index43_20);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 43, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA44_eotS =
        "\42\uffff";
    static final String DFA44_eofS =
        "\42\uffff";
    static final String DFA44_minS =
        "\1\15\14\0\1\uffff\14\0\10\uffff";
    static final String DFA44_maxS =
        "\1\140\14\0\1\uffff\14\0\10\uffff";
    static final String DFA44_acceptS =
        "\15\uffff\1\1\14\uffff\1\10\1\uffff\1\2\1\3\1\4\1\5\1\6\1\7";
    static final String DFA44_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\uffff\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1"+
        "\27\10\uffff}>";
    static final String[] DFA44_transitionS = {
            "\1\2\1\uffff\1\16\1\22\2\1\7\uffff\1\14\7\uffff\1\23\3\uffff"+
            "\4\12\1\13\11\uffff\1\32\10\uffff\1\32\11\uffff\2\20\2\uffff"+
            "\1\24\1\25\1\26\1\27\1\30\1\31\3\17\1\5\1\6\1\7\1\10\1\11\3"+
            "\uffff\1\21\1\3\1\4\2\15",
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
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA44_eot = DFA.unpackEncodedString(DFA44_eotS);
    static final short[] DFA44_eof = DFA.unpackEncodedString(DFA44_eofS);
    static final char[] DFA44_min = DFA.unpackEncodedStringToUnsignedChars(DFA44_minS);
    static final char[] DFA44_max = DFA.unpackEncodedStringToUnsignedChars(DFA44_maxS);
    static final short[] DFA44_accept = DFA.unpackEncodedString(DFA44_acceptS);
    static final short[] DFA44_special = DFA.unpackEncodedString(DFA44_specialS);
    static final short[][] DFA44_transition;

    static {
        int numStates = DFA44_transitionS.length;
        DFA44_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA44_transition[i] = DFA.unpackEncodedString(DFA44_transitionS[i]);
        }
    }

    class DFA44 extends DFA {

        public DFA44(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 44;
            this.eot = DFA44_eot;
            this.eof = DFA44_eof;
            this.min = DFA44_min;
            this.max = DFA44_max;
            this.accept = DFA44_accept;
            this.special = DFA44_special;
            this.transition = DFA44_transition;
        }
        public String getDescription() {
            return "166:1: simple_cond_expression : ( comparison_expression | between_expression | like_expression | in_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA44_1 = input.LA(1);

                         
                        int index44_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                        else if ( (synpred58_JPA()) ) {s = 30;}

                        else if ( (synpred59_JPA()) ) {s = 31;}

                        else if ( (synpred60_JPA()) ) {s = 32;}

                        else if ( (synpred61_JPA()) ) {s = 33;}

                         
                        input.seek(index44_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA44_2 = input.LA(1);

                         
                        int index44_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                         
                        input.seek(index44_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA44_3 = input.LA(1);

                         
                        int index44_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                        else if ( (synpred59_JPA()) ) {s = 31;}

                        else if ( (synpred61_JPA()) ) {s = 33;}

                         
                        input.seek(index44_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA44_4 = input.LA(1);

                         
                        int index44_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                        else if ( (synpred59_JPA()) ) {s = 31;}

                        else if ( (synpred61_JPA()) ) {s = 33;}

                         
                        input.seek(index44_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA44_5 = input.LA(1);

                         
                        int index44_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                         
                        input.seek(index44_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA44_6 = input.LA(1);

                         
                        int index44_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                         
                        input.seek(index44_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA44_7 = input.LA(1);

                         
                        int index44_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                         
                        input.seek(index44_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA44_8 = input.LA(1);

                         
                        int index44_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                         
                        input.seek(index44_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA44_9 = input.LA(1);

                         
                        int index44_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                         
                        input.seek(index44_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA44_10 = input.LA(1);

                         
                        int index44_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                         
                        input.seek(index44_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA44_11 = input.LA(1);

                         
                        int index44_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                         
                        input.seek(index44_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA44_12 = input.LA(1);

                         
                        int index44_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                        else if ( (synpred57_JPA()) ) {s = 29;}

                         
                        input.seek(index44_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA44_14 = input.LA(1);

                         
                        int index44_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred61_JPA()) ) {s = 33;}

                         
                        input.seek(index44_14);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA44_15 = input.LA(1);

                         
                        int index44_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_15);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA44_16 = input.LA(1);

                         
                        int index44_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_16);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA44_17 = input.LA(1);

                         
                        int index44_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_17);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA44_18 = input.LA(1);

                         
                        int index44_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_18);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA44_19 = input.LA(1);

                         
                        int index44_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_19);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA44_20 = input.LA(1);

                         
                        int index44_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_20);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA44_21 = input.LA(1);

                         
                        int index44_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_21);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA44_22 = input.LA(1);

                         
                        int index44_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_22);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA44_23 = input.LA(1);

                         
                        int index44_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_23);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA44_24 = input.LA(1);

                         
                        int index44_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_24);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA44_25 = input.LA(1);

                         
                        int index44_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_JPA()) ) {s = 13;}

                        else if ( (synpred56_JPA()) ) {s = 28;}

                         
                        input.seek(index44_25);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 44, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA48_eotS =
        "\30\uffff";
    static final String DFA48_eofS =
        "\30\uffff";
    static final String DFA48_minS =
        "\1\15\1\uffff\1\0\3\uffff\2\0\6\uffff\3\0\7\uffff";
    static final String DFA48_maxS =
        "\1\136\1\uffff\1\0\3\uffff\2\0\6\uffff\3\0\7\uffff";
    static final String DFA48_acceptS =
        "\1\uffff\1\1\17\uffff\1\2\5\uffff\1\3";
    static final String DFA48_specialS =
        "\2\uffff\1\0\3\uffff\1\1\1\2\6\uffff\1\3\1\4\1\5\7\uffff}>";
    static final String[] DFA48_transitionS = {
            "\1\21\2\uffff\1\1\2\2\7\uffff\1\20\7\uffff\1\1\3\uffff\4\16"+
            "\1\17\34\uffff\2\1\2\uffff\6\1\3\27\5\21\3\uffff\1\1\1\6\1\7",
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
            return "176:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA48_2 = input.LA(1);

                         
                        int index48_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred63_JPA()) ) {s = 1;}

                        else if ( (synpred65_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index48_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA48_6 = input.LA(1);

                         
                        int index48_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred63_JPA()) ) {s = 1;}

                        else if ( (synpred65_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index48_6);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA48_7 = input.LA(1);

                         
                        int index48_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred63_JPA()) ) {s = 1;}

                        else if ( (synpred65_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index48_7);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA48_14 = input.LA(1);

                         
                        int index48_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred63_JPA()) ) {s = 1;}

                        else if ( (synpred65_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index48_14);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA48_15 = input.LA(1);

                         
                        int index48_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred63_JPA()) ) {s = 1;}

                        else if ( (synpred65_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index48_15);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA48_16 = input.LA(1);

                         
                        int index48_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred63_JPA()) ) {s = 1;}

                        else if ( (synpred65_JPA()) ) {s = 17;}

                        else if ( (true) ) {s = 23;}

                         
                        input.seek(index48_16);
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
    static final String DFA67_eotS =
        "\34\uffff";
    static final String DFA67_eofS =
        "\34\uffff";
    static final String DFA67_minS =
        "\1\15\1\0\1\uffff\2\0\5\uffff\3\0\1\uffff\1\0\15\uffff";
    static final String DFA67_maxS =
        "\1\140\1\0\1\uffff\2\0\5\uffff\3\0\1\uffff\1\0\15\uffff";
    static final String DFA67_acceptS =
        "\2\uffff\1\1\12\uffff\1\2\1\uffff\1\4\1\6\11\uffff\1\3\1\5";
    static final String DFA67_specialS =
        "\1\uffff\1\0\1\uffff\1\1\1\2\5\uffff\1\3\1\4\1\5\1\uffff\1\6\15"+
        "\uffff}>";
    static final String[] DFA67_transitionS = {
            "\1\2\1\uffff\1\16\1\20\2\1\7\uffff\1\14\7\uffff\1\20\3\uffff"+
            "\4\12\1\13\34\uffff\2\20\2\uffff\6\20\3\17\5\2\3\uffff\1\20"+
            "\1\3\1\4\2\15",
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

    static final short[] DFA67_eot = DFA.unpackEncodedString(DFA67_eotS);
    static final short[] DFA67_eof = DFA.unpackEncodedString(DFA67_eofS);
    static final char[] DFA67_min = DFA.unpackEncodedStringToUnsignedChars(DFA67_minS);
    static final char[] DFA67_max = DFA.unpackEncodedStringToUnsignedChars(DFA67_maxS);
    static final short[] DFA67_accept = DFA.unpackEncodedString(DFA67_acceptS);
    static final short[] DFA67_special = DFA.unpackEncodedString(DFA67_specialS);
    static final short[][] DFA67_transition;

    static {
        int numStates = DFA67_transitionS.length;
        DFA67_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA67_transition[i] = DFA.unpackEncodedString(DFA67_transitionS[i]);
        }
    }

    class DFA67 extends DFA {

        public DFA67(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 67;
            this.eot = DFA67_eot;
            this.eof = DFA67_eof;
            this.min = DFA67_min;
            this.max = DFA67_max;
            this.accept = DFA67_accept;
            this.special = DFA67_special;
            this.transition = DFA67_transition;
        }
        public String getDescription() {
            return "210:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA67_1 = input.LA(1);

                         
                        int index67_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred82_JPA()) ) {s = 2;}

                        else if ( (synpred85_JPA()) ) {s = 13;}

                        else if ( (synpred88_JPA()) ) {s = 26;}

                        else if ( (synpred90_JPA()) ) {s = 15;}

                        else if ( (synpred93_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index67_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA67_3 = input.LA(1);

                         
                        int index67_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred82_JPA()) ) {s = 2;}

                        else if ( (synpred85_JPA()) ) {s = 13;}

                        else if ( (synpred88_JPA()) ) {s = 26;}

                        else if ( (synpred90_JPA()) ) {s = 15;}

                        else if ( (synpred93_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index67_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA67_4 = input.LA(1);

                         
                        int index67_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred82_JPA()) ) {s = 2;}

                        else if ( (synpred85_JPA()) ) {s = 13;}

                        else if ( (synpred88_JPA()) ) {s = 26;}

                        else if ( (synpred90_JPA()) ) {s = 15;}

                        else if ( (synpred93_JPA()) ) {s = 27;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index67_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA67_10 = input.LA(1);

                         
                        int index67_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred82_JPA()) ) {s = 2;}

                        else if ( (synpred90_JPA()) ) {s = 15;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index67_10);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA67_11 = input.LA(1);

                         
                        int index67_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred82_JPA()) ) {s = 2;}

                        else if ( (synpred90_JPA()) ) {s = 15;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index67_11);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA67_12 = input.LA(1);

                         
                        int index67_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred82_JPA()) ) {s = 2;}

                        else if ( (synpred85_JPA()) ) {s = 13;}

                        else if ( (synpred88_JPA()) ) {s = 26;}

                        else if ( (synpred90_JPA()) ) {s = 15;}

                        else if ( (true) ) {s = 16;}

                         
                        input.seek(index67_12);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA67_14 = input.LA(1);

                         
                        int index67_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred88_JPA()) ) {s = 26;}

                        else if ( (synpred93_JPA()) ) {s = 27;}

                         
                        input.seek(index67_14);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 67, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_select_statement_in_ql_statement122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_select_statement134 = new BitSet(new long[]{0x000007F800068000L});
    public static final BitSet FOLLOW_select_clause_in_select_statement136 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_from_clause_in_select_statement138 = new BitSet(new long[]{0x0000D80000000002L});
    public static final BitSet FOLLOW_where_clause_in_select_statement141 = new BitSet(new long[]{0x0000D00000000002L});
    public static final BitSet FOLLOW_groupby_clause_in_select_statement146 = new BitSet(new long[]{0x0000C00000000002L});
    public static final BitSet FOLLOW_having_clause_in_select_statement151 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_orderby_clause_in_select_statement155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_from_clause208 = new BitSet(new long[]{0x0000000004008000L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause210 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_24_in_from_clause213 = new BitSet(new long[]{0x0000000204008000L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause215 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_collection_member_declaration_in_from_clause217 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration239 = new BitSet(new long[]{0x00000001A0000002L});
    public static final BitSet FOLLOW_join_in_identification_variable_declaration243 = new BitSet(new long[]{0x00000001A0000002L});
    public static final BitSet FOLLOW_fetch_join_in_identification_variable_declaration247 = new BitSet(new long[]{0x00000001A0000002L});
    public static final BitSet FOLLOW_range_variable_declaration_source_in_range_variable_declaration259 = new BitSet(new long[]{0x0000000002008000L});
    public static final BitSet FOLLOW_25_in_range_variable_declaration262 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_abstract_schema_name_in_range_variable_declaration_source293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_range_variable_declaration_source300 = new BitSet(new long[]{0x000007F800068000L});
    public static final BitSet FOLLOW_select_clause_in_range_variable_declaration_source302 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_from_clause_in_range_variable_declaration_source304 = new BitSet(new long[]{0x0000D80008000000L});
    public static final BitSet FOLLOW_where_clause_in_range_variable_declaration_source307 = new BitSet(new long[]{0x0000D00008000000L});
    public static final BitSet FOLLOW_groupby_clause_in_range_variable_declaration_source312 = new BitSet(new long[]{0x0000C00008000000L});
    public static final BitSet FOLLOW_having_clause_in_range_variable_declaration_source317 = new BitSet(new long[]{0x0000800008000000L});
    public static final BitSet FOLLOW_orderby_clause_in_range_variable_declaration_source321 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_range_variable_declaration_source327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_spec_in_join381 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_join_association_path_expression_in_join383 = new BitSet(new long[]{0x0000000002008000L});
    public static final BitSet FOLLOW_25_in_join386 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_identification_variable_in_join390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_spec_in_fetch_join419 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_fetch_join421 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_join_spec433 = new BitSet(new long[]{0x0000000140000000L});
    public static final BitSet FOLLOW_30_in_join_spec437 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_31_in_join_spec443 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_join_spec448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_join_field_path_in_join_association_path_expression457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_collection_member_declaration477 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_collection_member_declaration478 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_path_expression_in_collection_member_declaration480 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_collection_member_declaration482 = new BitSet(new long[]{0x0000000002008000L});
    public static final BitSet FOLLOW_25_in_collection_member_declaration485 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_field_path_in_path_expression499 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_select_clause522 = new BitSet(new long[]{0x000007F800068000L});
    public static final BitSet FOLLOW_select_expression_in_select_clause526 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_24_in_select_clause529 = new BitSet(new long[]{0x000007F800068000L});
    public static final BitSet FOLLOW_select_expression_in_select_clause531 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_path_expression_in_select_expression560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_select_expression565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_select_expression570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_select_expression585 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_select_expression587 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_identification_variable_in_select_expression588 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_select_expression589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_expression_in_select_expression594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_constructor_expression603 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_constructor_name_in_constructor_expression605 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_constructor_expression607 = new BitSet(new long[]{0x000007C000060000L});
    public static final BitSet FOLLOW_constructor_item_in_constructor_expression609 = new BitSet(new long[]{0x0000000009000000L});
    public static final BitSet FOLLOW_24_in_constructor_expression612 = new BitSet(new long[]{0x000007C000060000L});
    public static final BitSet FOLLOW_constructor_item_in_constructor_expression614 = new BitSet(new long[]{0x0000000009000000L});
    public static final BitSet FOLLOW_27_in_constructor_expression618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_constructor_item627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_constructor_item631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_aggregate_expression640 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_aggregate_expression657 = new BitSet(new long[]{0x0000000800060000L});
    public static final BitSet FOLLOW_35_in_aggregate_expression660 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_path_expression_in_aggregate_expression664 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_aggregate_expression665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_aggregate_expression670 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_aggregate_expression672 = new BitSet(new long[]{0x0000000800068000L});
    public static final BitSet FOLLOW_35_in_aggregate_expression675 = new BitSet(new long[]{0x0000000000068000L});
    public static final BitSet FOLLOW_identification_variable_in_aggregate_expression680 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_path_expression_in_aggregate_expression684 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_aggregate_expression687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_where_clause696 = new BitSet(new long[]{0x201007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_conditional_expression_in_where_clause698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_where_clause710 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_path_expression_in_where_clause712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_groupby_clause729 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_45_in_groupby_clause731 = new BitSet(new long[]{0x0000000000068000L});
    public static final BitSet FOLLOW_groupby_item_in_groupby_clause733 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_24_in_groupby_clause736 = new BitSet(new long[]{0x0000000000068000L});
    public static final BitSet FOLLOW_groupby_item_in_groupby_clause738 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_path_expression_in_groupby_item749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_groupby_item753 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_having_clause762 = new BitSet(new long[]{0x201007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_conditional_expression_in_having_clause764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_orderby_clause773 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_45_in_orderby_clause775 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_orderby_item_in_orderby_clause777 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_24_in_orderby_clause780 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_orderby_item_in_orderby_clause782 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_path_expression_in_orderby_item793 = new BitSet(new long[]{0x0003000000000002L});
    public static final BitSet FOLLOW_set_in_orderby_item795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_subquery813 = new BitSet(new long[]{0x000007C800068000L});
    public static final BitSet FOLLOW_simple_select_clause_in_subquery815 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_subquery_from_clause_in_subquery817 = new BitSet(new long[]{0x0000580008000000L});
    public static final BitSet FOLLOW_where_clause_in_subquery820 = new BitSet(new long[]{0x0000500008000000L});
    public static final BitSet FOLLOW_groupby_clause_in_subquery825 = new BitSet(new long[]{0x0000400008000000L});
    public static final BitSet FOLLOW_having_clause_in_subquery830 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_subquery836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_subquery_from_clause877 = new BitSet(new long[]{0x0000000204068000L});
    public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause879 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_24_in_subquery_from_clause882 = new BitSet(new long[]{0x0000000204068000L});
    public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause884 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration905 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_association_path_expression_in_subselect_identification_variable_declaration910 = new BitSet(new long[]{0x0000000002008000L});
    public static final BitSet FOLLOW_25_in_subselect_identification_variable_declaration913 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_declaration_in_subselect_identification_variable_declaration922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_association_path_expression931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_simple_select_clause941 = new BitSet(new long[]{0x000007C800068000L});
    public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause945 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_simple_select_expression969 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_simple_select_expression979 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditional_term_in_conditional_expression989 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_50_in_conditional_expression993 = new BitSet(new long[]{0x201007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_conditional_term_in_conditional_expression995 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_conditional_factor_in_conditional_term1007 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_51_in_conditional_term1011 = new BitSet(new long[]{0x201007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_conditional_factor_in_conditional_term1013 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_52_in_conditional_factor1026 = new BitSet(new long[]{0x201007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_simple_cond_expression_in_conditional_factor1030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_conditional_factor1034 = new BitSet(new long[]{0x201007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_conditional_expression_in_conditional_factor1035 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_conditional_factor1036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression1045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_expression_in_simple_cond_expression1050 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_expression_in_simple_cond_expression1055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_expression_in_simple_cond_expression1060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression1065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression1070 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression1075 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression1080 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1089 = new BitSet(new long[]{0x0030000000000000L});
    public static final BitSet FOLLOW_52_in_between_expression1092 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_between_expression1096 = new BitSet(new long[]{0x000007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1098 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_between_expression1100 = new BitSet(new long[]{0x000007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_arithmetic_expression_in_between_expression1102 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1107 = new BitSet(new long[]{0x0030000000000000L});
    public static final BitSet FOLLOW_52_in_between_expression1110 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_between_expression1114 = new BitSet(new long[]{0x000007C004062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1116 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_between_expression1118 = new BitSet(new long[]{0x000007C004062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_expression_in_between_expression1120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1125 = new BitSet(new long[]{0x0030000000000000L});
    public static final BitSet FOLLOW_52_in_between_expression1128 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_between_expression1132 = new BitSet(new long[]{0x000007C004062000L,0x0000000061FE0000L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1134 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_between_expression1136 = new BitSet(new long[]{0x000007C004062000L,0x0000000061FE0000L});
    public static final BitSet FOLLOW_datetime_expression_in_between_expression1138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_in_expression1147 = new BitSet(new long[]{0x0010000200000000L});
    public static final BitSet FOLLOW_52_in_in_expression1150 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_in_expression1154 = new BitSet(new long[]{0x000007C404062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_in_expression_right_part_in_in_expression1156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_in_expression_right_part1165 = new BitSet(new long[]{0x0000000000008000L,0x0000000060000000L});
    public static final BitSet FOLLOW_in_item_in_in_expression_right_part1167 = new BitSet(new long[]{0x0000000009000000L});
    public static final BitSet FOLLOW_24_in_in_expression_right_part1170 = new BitSet(new long[]{0x0000000000008000L,0x0000000060000000L});
    public static final BitSet FOLLOW_in_item_in_in_expression_right_part1172 = new BitSet(new long[]{0x0000000009000000L});
    public static final BitSet FOLLOW_27_in_in_expression_right_part1176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_in_expression_right_part1181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_in_item1190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_in_item1195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_like_expression1204 = new BitSet(new long[]{0x0050000000000000L});
    public static final BitSet FOLLOW_52_in_like_expression1207 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_like_expression1211 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_pattern_value_in_like_expression1213 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_like_expression1216 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_ESCAPE_CHARACTER_in_like_expression1218 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_null_comparison_expression1230 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression1234 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_null_comparison_expression1237 = new BitSet(new long[]{0x0210000000000000L});
    public static final BitSet FOLLOW_52_in_null_comparison_expression1240 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_null_comparison_expression1244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression1253 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_empty_collection_comparison_expression1255 = new BitSet(new long[]{0x0410000000000000L});
    public static final BitSet FOLLOW_52_in_empty_collection_comparison_expression1258 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_58_in_empty_collection_comparison_expression1262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_collection_member_expression1271 = new BitSet(new long[]{0x0810000000000000L});
    public static final BitSet FOLLOW_52_in_collection_member_expression1274 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_59_in_collection_member_expression1278 = new BitSet(new long[]{0x1000000000060000L});
    public static final BitSet FOLLOW_60_in_collection_member_expression1281 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_path_expression_in_collection_member_expression1285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_exists_expression1295 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_61_in_exists_expression1299 = new BitSet(new long[]{0x000007C004062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_subquery_in_exists_expression1301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_all_or_any_expression1310 = new BitSet(new long[]{0x000007C004062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_subquery_in_all_or_any_expression1323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_comparison_expression1332 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression1334 = new BitSet(new long[]{0xC00007C004062000L,0x0000000061F00001L});
    public static final BitSet FOLLOW_string_expression_in_comparison_expression1337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression1341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_expression_in_comparison_expression1347 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_set_in_comparison_expression1349 = new BitSet(new long[]{0xC00007C004062000L,0x00000001E1F00001L});
    public static final BitSet FOLLOW_boolean_expression_in_comparison_expression1358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression1362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_expression_in_comparison_expression1368 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_set_in_comparison_expression1370 = new BitSet(new long[]{0xC00007C00406A000L,0x0000000061F00001L});
    public static final BitSet FOLLOW_enum_expression_in_comparison_expression1377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression1381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_comparison_expression1387 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression1389 = new BitSet(new long[]{0xC00007C004062000L,0x0000000061FE0001L});
    public static final BitSet FOLLOW_datetime_expression_in_comparison_expression1392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression1396 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_comparison_expression1402 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_set_in_comparison_expression1404 = new BitSet(new long[]{0xC00007C00406A000L,0x0000000061F00001L});
    public static final BitSet FOLLOW_entity_expression_in_comparison_expression1413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression1417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression1423 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
    public static final BitSet FOLLOW_comparison_operator_in_comparison_expression1425 = new BitSet(new long[]{0xC00007C40407A000L,0x00000001F1FFF981L});
    public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression1428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression1432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_comparison_operator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_arithmetic_expression1476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_arithmetic_expression1481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_term_in_simple_arithmetic_expression1491 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000180L});
    public static final BitSet FOLLOW_set_in_simple_arithmetic_expression1495 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_arithmetic_term_in_simple_arithmetic_expression1505 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000180L});
    public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term1517 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000600L});
    public static final BitSet FOLLOW_set_in_arithmetic_term1521 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term1531 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000600L});
    public static final BitSet FOLLOW_set_in_arithmetic_factor1542 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor1553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_arithmetic_primary1562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary1567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_arithmetic_primary1572 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_arithmetic_primary1573 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_arithmetic_primary1574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary1579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary1584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary1589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_primary_in_string_expression1598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_string_expression1602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_string_primary1611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_string_primary1616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_string_primary1621 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_strings_in_string_primary1626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_string_primary1631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_primary_in_datetime_expression1640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_datetime_expression1645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_datetime_primary1654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_datetime_primary1659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_primary1664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_expression_in_datetime_primary1669 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_primary_in_boolean_expression1678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_boolean_expression1683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_boolean_primary1692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_literal_in_boolean_primary1697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_boolean_primary1702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_primary_in_enum_expression1711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subquery_in_enum_expression1716 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_enum_primary1725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_literal_in_enum_primary1730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_enum_primary1735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_path_expression_in_entity_expression1744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression1749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression1758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression1763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_75_in_functions_returning_numerics1772 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_numerics1774 = new BitSet(new long[]{0x000007C000062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics1775 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_numerics1776 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_functions_returning_numerics1781 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_numerics1783 = new BitSet(new long[]{0x000007C000062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics1784 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_functions_returning_numerics1785 = new BitSet(new long[]{0x000007C000062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_numerics1787 = new BitSet(new long[]{0x0000000009000000L});
    public static final BitSet FOLLOW_24_in_functions_returning_numerics1789 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics1791 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_numerics1794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_functions_returning_numerics1799 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_numerics1801 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics1802 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_numerics1803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_78_in_functions_returning_numerics1808 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_numerics1810 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics1811 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_numerics1812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_79_in_functions_returning_numerics1817 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_numerics1819 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics1820 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_functions_returning_numerics1821 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_numerics1823 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_numerics1824 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_80_in_functions_returning_numerics1829 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_numerics1831 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics1832 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_numerics1833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_functions_returning_datetime0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_84_in_functions_returning_strings1861 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_strings1863 = new BitSet(new long[]{0x000007C000062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings1864 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_functions_returning_strings1865 = new BitSet(new long[]{0x000007C000062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings1867 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_strings1868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_functions_returning_strings1873 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_strings1875 = new BitSet(new long[]{0x000007C000062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings1876 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_functions_returning_strings1877 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_strings1878 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_functions_returning_strings1879 = new BitSet(new long[]{0x000007C400070000L,0x000000007001F980L});
    public static final BitSet FOLLOW_simple_arithmetic_expression_in_functions_returning_strings1881 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_strings1882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_86_in_functions_returning_strings1887 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_strings1889 = new BitSet(new long[]{0x000007C000866000L,0x000000006FF00000L});
    public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings1892 = new BitSet(new long[]{0x0000000000804000L});
    public static final BitSet FOLLOW_TRIM_CHARACTER_in_functions_returning_strings1897 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_functions_returning_strings1901 = new BitSet(new long[]{0x000007C000062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings1905 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_strings1906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_87_in_functions_returning_strings1911 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_strings1913 = new BitSet(new long[]{0x000007C000062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings1914 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_strings1915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_88_in_functions_returning_strings1920 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functions_returning_strings1922 = new BitSet(new long[]{0x000007C000062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_primary_in_functions_returning_strings1923 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_functions_returning_strings1924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_trim_specification0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_abstract_schema_name1955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_pattern_value1965 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_numeric_literal1976 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal1980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_input_parameter1990 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_INT_NUMERAL_in_input_parameter1992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_input_parameter1997 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_identification_variable_in_input_parameter1999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_literal2008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_constructor_name2017 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_enum_literal2058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_boolean_literal0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIMPLE_FIELD_PATH_in_join_field_path2085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_field_path0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_identification_variable2111 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred53_JPA1026 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred54_JPA1026 = new BitSet(new long[]{0x201007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_simple_cond_expression_in_synpred54_JPA1030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_expression_in_synpred55_JPA1045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_expression_in_synpred56_JPA1050 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_expression_in_synpred57_JPA1055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_expression_in_synpred58_JPA1060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_comparison_expression_in_synpred59_JPA1065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred60_JPA1070 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collection_member_expression_in_synpred61_JPA1075 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred63_JPA1089 = new BitSet(new long[]{0x0030000000000000L});
    public static final BitSet FOLLOW_52_in_synpred63_JPA1092 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_synpred63_JPA1096 = new BitSet(new long[]{0x000007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred63_JPA1098 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_synpred63_JPA1100 = new BitSet(new long[]{0x000007C40407A000L,0x00000001F1FFF980L});
    public static final BitSet FOLLOW_arithmetic_expression_in_synpred63_JPA1102 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_synpred65_JPA1107 = new BitSet(new long[]{0x0030000000000000L});
    public static final BitSet FOLLOW_52_in_synpred65_JPA1110 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_synpred65_JPA1114 = new BitSet(new long[]{0x000007C004062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_expression_in_synpred65_JPA1116 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_synpred65_JPA1118 = new BitSet(new long[]{0x000007C004062000L,0x0000000061F00000L});
    public static final BitSet FOLLOW_string_expression_in_synpred65_JPA1120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_synpred82_JPA1332 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
    public static final BitSet FOLLOW_comparison_operator_in_synpred82_JPA1334 = new BitSet(new long[]{0xC00007C004062000L,0x0000000061F00001L});
    public static final BitSet FOLLOW_string_expression_in_synpred82_JPA1337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred82_JPA1341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_expression_in_synpred85_JPA1347 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_set_in_synpred85_JPA1349 = new BitSet(new long[]{0xC00007C004062000L,0x00000001E1F00001L});
    public static final BitSet FOLLOW_boolean_expression_in_synpred85_JPA1358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred85_JPA1362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_expression_in_synpred88_JPA1368 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_set_in_synpred88_JPA1370 = new BitSet(new long[]{0xC00007C00406A000L,0x0000000061F00001L});
    public static final BitSet FOLLOW_enum_expression_in_synpred88_JPA1377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred88_JPA1381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_expression_in_synpred90_JPA1387 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
    public static final BitSet FOLLOW_comparison_operator_in_synpred90_JPA1389 = new BitSet(new long[]{0xC00007C004062000L,0x0000000061FE0001L});
    public static final BitSet FOLLOW_datetime_expression_in_synpred90_JPA1392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred90_JPA1396 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entity_expression_in_synpred93_JPA1402 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_set_in_synpred93_JPA1404 = new BitSet(new long[]{0xC00007C00406A000L,0x0000000061F00001L});
    public static final BitSet FOLLOW_entity_expression_in_synpred93_JPA1413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_all_or_any_expression_in_synpred93_JPA1417 = new BitSet(new long[]{0x0000000000000002L});

}