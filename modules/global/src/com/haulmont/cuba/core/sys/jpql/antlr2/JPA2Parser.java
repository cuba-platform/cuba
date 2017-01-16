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

package com.haulmont.cuba.core.sys.jpql.antlr2;

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
import com.haulmont.cuba.core.sys.jpql.tree.SelectedItemsNode;
import com.haulmont.cuba.core.sys.jpql.tree.UpdateSetNode;
import com.haulmont.cuba.core.sys.jpql.tree.EnumConditionNode;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class JPA2Parser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND", "ASC", "AVG", "BY", "COMMENT", 
		"COUNT", "DESC", "DISTINCT", "ESCAPE_CHARACTER", "FETCH", "GROUP", "HAVING", 
		"IN", "INNER", "INT_NUMERAL", "JOIN", "LEFT", "LINE_COMMENT", "LOWER", 
		"LPAREN", "MAX", "MIN", "NAMED_PARAMETER", "NOT", "OR", "ORDER", "OUTER", 
		"RPAREN", "RUSSIAN_SYMBOLS", "SET", "STRING_LITERAL", "SUM", "TRIM_CHARACTER", 
		"T_AGGREGATE_EXPR", "T_COLLECTION_MEMBER", "T_CONDITION", "T_ENUM_MACROS", 
		"T_GROUP_BY", "T_ID_VAR", "T_JOIN_VAR", "T_ORDER_BY", "T_ORDER_BY_FIELD", 
		"T_PARAMETER", "T_QUERY", "T_SELECTED_ENTITY", "T_SELECTED_FIELD", "T_SELECTED_ITEM", 
		"T_SELECTED_ITEMS", "T_SIMPLE_CONDITION", "T_SOURCE", "T_SOURCES", "WORD", 
		"WS", "'${'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", "'0x'", "'<'", 
		"'<='", "'<>'", "'='", "'>'", "'>='", "'?'", "'@BETWEEN'", "'@DATEAFTER'", 
		"'@DATEBEFORE'", "'@DATEEQUALS'", "'@ENUM'", "'@TODAY'", "'ABS('", "'ALL'", 
		"'ANY'", "'AS'", "'BETWEEN'", "'BOTH'", "'CASE'", "'CAST('", "'COALESCE('", 
		"'CONCAT('", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", 
		"'DAY'", "'DELETE'", "'ELSE'", "'EMPTY'", "'END'", "'ENTRY('", "'EPOCH'", 
		"'ESCAPE'", "'EXISTS'", "'EXTRACT('", "'FROM'", "'FUNCTION('", "'HOUR'", 
		"'INDEX('", "'IS'", "'KEY('", "'LEADING'", "'LENGTH('", "'LIKE'", "'LOCATE('", 
		"'MEMBER'", "'MINUTE'", "'MOD('", "'MONTH'", "'NEW'", "'NOW'", "'NULL'", 
		"'NULLIF('", "'OBJECT'", "'OF'", "'ON'", "'QUARTER'", "'REGEXP'", "'SECOND'", 
		"'SELECT'", "'SIZE('", "'SOME'", "'SQRT('", "'SUBSTRING('", "'THEN'", 
		"'TRAILING'", "'TREAT('", "'TRIM('", "'TYPE('", "'UPDATE'", "'UPPER('", 
		"'VALUE('", "'WEEK'", "'WHEN'", "'WHERE'", "'YEAR'", "'false'", "'true'", 
		"'}'"
	};
	public static final int EOF=-1;
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
	public static final int T__117=117;
	public static final int T__118=118;
	public static final int T__119=119;
	public static final int T__120=120;
	public static final int T__121=121;
	public static final int T__122=122;
	public static final int T__123=123;
	public static final int T__124=124;
	public static final int T__125=125;
	public static final int T__126=126;
	public static final int T__127=127;
	public static final int T__128=128;
	public static final int T__129=129;
	public static final int T__130=130;
	public static final int T__131=131;
	public static final int T__132=132;
	public static final int T__133=133;
	public static final int T__134=134;
	public static final int T__135=135;
	public static final int T__136=136;
	public static final int T__137=137;
	public static final int T__138=138;
	public static final int T__139=139;
	public static final int T__140=140;
	public static final int T__141=141;
	public static final int T__142=142;
	public static final int T__143=143;
	public static final int T__144=144;
	public static final int AND=4;
	public static final int ASC=5;
	public static final int AVG=6;
	public static final int BY=7;
	public static final int COMMENT=8;
	public static final int COUNT=9;
	public static final int DESC=10;
	public static final int DISTINCT=11;
	public static final int ESCAPE_CHARACTER=12;
	public static final int FETCH=13;
	public static final int GROUP=14;
	public static final int HAVING=15;
	public static final int IN=16;
	public static final int INNER=17;
	public static final int INT_NUMERAL=18;
	public static final int JOIN=19;
	public static final int LEFT=20;
	public static final int LINE_COMMENT=21;
	public static final int LOWER=22;
	public static final int LPAREN=23;
	public static final int MAX=24;
	public static final int MIN=25;
	public static final int NAMED_PARAMETER=26;
	public static final int NOT=27;
	public static final int OR=28;
	public static final int ORDER=29;
	public static final int OUTER=30;
	public static final int RPAREN=31;
	public static final int RUSSIAN_SYMBOLS=32;
	public static final int SET=33;
	public static final int STRING_LITERAL=34;
	public static final int SUM=35;
	public static final int TRIM_CHARACTER=36;
	public static final int T_AGGREGATE_EXPR=37;
	public static final int T_COLLECTION_MEMBER=38;
	public static final int T_CONDITION=39;
	public static final int T_ENUM_MACROS=40;
	public static final int T_GROUP_BY=41;
	public static final int T_ID_VAR=42;
	public static final int T_JOIN_VAR=43;
	public static final int T_ORDER_BY=44;
	public static final int T_ORDER_BY_FIELD=45;
	public static final int T_PARAMETER=46;
	public static final int T_QUERY=47;
	public static final int T_SELECTED_ENTITY=48;
	public static final int T_SELECTED_FIELD=49;
	public static final int T_SELECTED_ITEM=50;
	public static final int T_SELECTED_ITEMS=51;
	public static final int T_SIMPLE_CONDITION=52;
	public static final int T_SOURCE=53;
	public static final int T_SOURCES=54;
	public static final int WORD=55;
	public static final int WS=56;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public JPA2Parser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public JPA2Parser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return JPA2Parser.tokenNames; }
	@Override public String getGrammarFileName() { return "JPA2.g"; }


	public static class ql_statement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "ql_statement"
	// JPA2.g:81:1: ql_statement : ( select_statement | update_statement | delete_statement );
	public final JPA2Parser.ql_statement_return ql_statement() throws RecognitionException {
		JPA2Parser.ql_statement_return retval = new JPA2Parser.ql_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope select_statement1 =null;
		ParserRuleReturnScope update_statement2 =null;
		ParserRuleReturnScope delete_statement3 =null;


		try {
			// JPA2.g:82:5: ( select_statement | update_statement | delete_statement )
			int alt1=3;
			switch ( input.LA(1) ) {
			case 125:
				{
				alt1=1;
				}
				break;
			case 135:
				{
				alt1=2;
				}
				break;
			case 92:
				{
				alt1=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 1, 0, input);
				throw nvae;
			}
			switch (alt1) {
				case 1 :
					// JPA2.g:82:7: select_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_select_statement_in_ql_statement445);
					select_statement1=select_statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, select_statement1.getTree());

					}
					break;
				case 2 :
					// JPA2.g:82:26: update_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_update_statement_in_ql_statement449);
					update_statement2=update_statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, update_statement2.getTree());

					}
					break;
				case 3 :
					// JPA2.g:82:45: delete_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_delete_statement_in_ql_statement453);
					delete_statement3=delete_statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, delete_statement3.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "ql_statement"


	public static class select_statement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "select_statement"
	// JPA2.g:84:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
	public final JPA2Parser.select_statement_return select_statement() throws RecognitionException {
		JPA2Parser.select_statement_return retval = new JPA2Parser.select_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token sl=null;
		ParserRuleReturnScope select_clause4 =null;
		ParserRuleReturnScope from_clause5 =null;
		ParserRuleReturnScope where_clause6 =null;
		ParserRuleReturnScope groupby_clause7 =null;
		ParserRuleReturnScope having_clause8 =null;
		ParserRuleReturnScope orderby_clause9 =null;

		Object sl_tree=null;
		RewriteRuleTokenStream stream_125=new RewriteRuleTokenStream(adaptor,"token 125");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
		RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
		RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");

		try {
			// JPA2.g:85:6: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
			// JPA2.g:85:8: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )?
			{
			sl=(Token)match(input,125,FOLLOW_125_in_select_statement468); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_125.add(sl);

			pushFollow(FOLLOW_select_clause_in_select_statement470);
			select_clause4=select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_clause.add(select_clause4.getTree());
			pushFollow(FOLLOW_from_clause_in_select_statement472);
			from_clause5=from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_from_clause.add(from_clause5.getTree());
			// JPA2.g:85:46: ( where_clause )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==140) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// JPA2.g:85:47: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_select_statement475);
					where_clause6=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause6.getTree());
					}
					break;

			}

			// JPA2.g:85:62: ( groupby_clause )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==GROUP) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// JPA2.g:85:63: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_select_statement480);
					groupby_clause7=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause7.getTree());
					}
					break;

			}

			// JPA2.g:85:80: ( having_clause )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==HAVING) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// JPA2.g:85:81: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_select_statement485);
					having_clause8=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause8.getTree());
					}
					break;

			}

			// JPA2.g:85:97: ( orderby_clause )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==ORDER) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// JPA2.g:85:98: orderby_clause
					{
					pushFollow(FOLLOW_orderby_clause_in_select_statement490);
					orderby_clause9=orderby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause9.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: select_clause, where_clause, from_clause, orderby_clause, groupby_clause, having_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 86:6: -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
			{
				// JPA2.g:86:9: ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, sl), root_1);
				// JPA2.g:86:35: ( select_clause )?
				if ( stream_select_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_select_clause.nextTree());
				}
				stream_select_clause.reset();

				adaptor.addChild(root_1, stream_from_clause.nextTree());
				// JPA2.g:86:64: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:86:80: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:86:98: ( having_clause )?
				if ( stream_having_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_having_clause.nextTree());
				}
				stream_having_clause.reset();

				// JPA2.g:86:115: ( orderby_clause )?
				if ( stream_orderby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_orderby_clause.nextTree());
				}
				stream_orderby_clause.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "select_statement"


	public static class update_statement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "update_statement"
	// JPA2.g:88:1: update_statement : up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) ;
	public final JPA2Parser.update_statement_return update_statement() throws RecognitionException {
		JPA2Parser.update_statement_return retval = new JPA2Parser.update_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token up=null;
		ParserRuleReturnScope update_clause10 =null;
		ParserRuleReturnScope where_clause11 =null;

		Object up_tree=null;
		RewriteRuleTokenStream stream_135=new RewriteRuleTokenStream(adaptor,"token 135");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_update_clause=new RewriteRuleSubtreeStream(adaptor,"rule update_clause");

		try {
			// JPA2.g:89:5: (up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) )
			// JPA2.g:89:7: up= 'UPDATE' update_clause ( where_clause )?
			{
			up=(Token)match(input,135,FOLLOW_135_in_update_statement548); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_135.add(up);

			pushFollow(FOLLOW_update_clause_in_update_statement550);
			update_clause10=update_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_clause.add(update_clause10.getTree());
			// JPA2.g:89:33: ( where_clause )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==140) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// JPA2.g:89:34: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_update_statement553);
					where_clause11=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause11.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: where_clause, update_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 90:5: -> ^( T_QUERY[$up] update_clause ( where_clause )? )
			{
				// JPA2.g:90:8: ^( T_QUERY[$up] update_clause ( where_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, up), root_1);
				adaptor.addChild(root_1, stream_update_clause.nextTree());
				// JPA2.g:90:48: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "update_statement"


	public static class delete_statement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "delete_statement"
	// JPA2.g:91:1: delete_statement : dl= 'DELETE' delete_clause ( where_clause )? -> ^( T_QUERY[$dl] delete_clause ( where_clause )? ) ;
	public final JPA2Parser.delete_statement_return delete_statement() throws RecognitionException {
		JPA2Parser.delete_statement_return retval = new JPA2Parser.delete_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token dl=null;
		ParserRuleReturnScope delete_clause12 =null;
		ParserRuleReturnScope where_clause13 =null;

		Object dl_tree=null;
		RewriteRuleTokenStream stream_92=new RewriteRuleTokenStream(adaptor,"token 92");
		RewriteRuleSubtreeStream stream_delete_clause=new RewriteRuleSubtreeStream(adaptor,"rule delete_clause");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");

		try {
			// JPA2.g:92:5: (dl= 'DELETE' delete_clause ( where_clause )? -> ^( T_QUERY[$dl] delete_clause ( where_clause )? ) )
			// JPA2.g:92:7: dl= 'DELETE' delete_clause ( where_clause )?
			{
			dl=(Token)match(input,92,FOLLOW_92_in_delete_statement589); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_92.add(dl);

			pushFollow(FOLLOW_delete_clause_in_delete_statement591);
			delete_clause12=delete_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_delete_clause.add(delete_clause12.getTree());
			// JPA2.g:92:33: ( where_clause )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==140) ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					// JPA2.g:92:34: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_delete_statement594);
					where_clause13=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause13.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: where_clause, delete_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 93:5: -> ^( T_QUERY[$dl] delete_clause ( where_clause )? )
			{
				// JPA2.g:93:8: ^( T_QUERY[$dl] delete_clause ( where_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, dl), root_1);
				adaptor.addChild(root_1, stream_delete_clause.nextTree());
				// JPA2.g:93:48: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "delete_statement"


	public static class from_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "from_clause"
	// JPA2.g:95:1: from_clause : fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) ;
	public final JPA2Parser.from_clause_return from_clause() throws RecognitionException {
		JPA2Parser.from_clause_return retval = new JPA2Parser.from_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token fr=null;
		Token char_literal15=null;
		ParserRuleReturnScope identification_variable_declaration14 =null;
		ParserRuleReturnScope identification_variable_declaration_or_collection_member_declaration16 =null;

		Object fr_tree=null;
		Object char_literal15_tree=null;
		RewriteRuleTokenStream stream_101=new RewriteRuleTokenStream(adaptor,"token 101");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
		RewriteRuleSubtreeStream stream_identification_variable_declaration_or_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration_or_collection_member_declaration");

		try {
			// JPA2.g:96:6: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) )
			// JPA2.g:96:8: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )*
			{
			fr=(Token)match(input,101,FOLLOW_101_in_from_clause632); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_101.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_from_clause634);
			identification_variable_declaration14=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration14.getTree());
			// JPA2.g:96:54: ( ',' identification_variable_declaration_or_collection_member_declaration )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==60) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// JPA2.g:96:55: ',' identification_variable_declaration_or_collection_member_declaration
					{
					char_literal15=(Token)match(input,60,FOLLOW_60_in_from_clause637); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal15);

					pushFollow(FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause639);
					identification_variable_declaration_or_collection_member_declaration16=identification_variable_declaration_or_collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable_declaration_or_collection_member_declaration.add(identification_variable_declaration_or_collection_member_declaration16.getTree());
					}
					break;

				default :
					break loop8;
				}
			}

			// AST REWRITE
			// elements: identification_variable_declaration, identification_variable_declaration_or_collection_member_declaration
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 97:6: -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
			{
				// JPA2.g:97:9: ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
				// JPA2.g:97:72: ( identification_variable_declaration_or_collection_member_declaration )*
				while ( stream_identification_variable_declaration_or_collection_member_declaration.hasNext() ) {
					adaptor.addChild(root_1, stream_identification_variable_declaration_or_collection_member_declaration.nextTree());
				}
				stream_identification_variable_declaration_or_collection_member_declaration.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "from_clause"


	public static class identification_variable_declaration_or_collection_member_declaration_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "identification_variable_declaration_or_collection_member_declaration"
	// JPA2.g:98:1: identification_variable_declaration_or_collection_member_declaration : ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) );
	public final JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return identification_variable_declaration_or_collection_member_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return retval = new JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable_declaration17 =null;
		ParserRuleReturnScope collection_member_declaration18 =null;

		RewriteRuleSubtreeStream stream_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule collection_member_declaration");

		try {
			// JPA2.g:99:6: ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) )
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0==WORD) ) {
				alt9=1;
			}
			else if ( (LA9_0==IN) ) {
				alt9=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 9, 0, input);
				throw nvae;
			}

			switch (alt9) {
				case 1 :
					// JPA2.g:99:8: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration673);
					identification_variable_declaration17=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration17.getTree());

					}
					break;
				case 2 :
					// JPA2.g:100:8: collection_member_declaration
					{
					pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration682);
					collection_member_declaration18=collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_collection_member_declaration.add(collection_member_declaration18.getTree());
					// AST REWRITE
					// elements: collection_member_declaration
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 100:38: -> ^( T_SOURCE collection_member_declaration )
					{
						// JPA2.g:100:41: ^( T_SOURCE collection_member_declaration )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_1);
						adaptor.addChild(root_1, stream_collection_member_declaration.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "identification_variable_declaration_or_collection_member_declaration"


	public static class identification_variable_declaration_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "identification_variable_declaration"
	// JPA2.g:102:1: identification_variable_declaration : range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) ;
	public final JPA2Parser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_return retval = new JPA2Parser.identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope range_variable_declaration19 =null;
		ParserRuleReturnScope joined_clause20 =null;

		RewriteRuleSubtreeStream stream_range_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration");
		RewriteRuleSubtreeStream stream_joined_clause=new RewriteRuleSubtreeStream(adaptor,"rule joined_clause");

		try {
			// JPA2.g:103:6: ( range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) )
			// JPA2.g:103:8: range_variable_declaration ( joined_clause )*
			{
			pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration706);
			range_variable_declaration19=range_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range_variable_declaration.add(range_variable_declaration19.getTree());
			// JPA2.g:103:35: ( joined_clause )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==INNER||(LA10_0 >= JOIN && LA10_0 <= LEFT)) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// JPA2.g:103:35: joined_clause
					{
					pushFollow(FOLLOW_joined_clause_in_identification_variable_declaration708);
					joined_clause20=joined_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_joined_clause.add(joined_clause20.getTree());
					}
					break;

				default :
					break loop10;
				}
			}

			// AST REWRITE
			// elements: range_variable_declaration, joined_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 104:6: -> ^( T_SOURCE range_variable_declaration ( joined_clause )* )
			{
				// JPA2.g:104:9: ^( T_SOURCE range_variable_declaration ( joined_clause )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_1);
				adaptor.addChild(root_1, stream_range_variable_declaration.nextTree());
				// JPA2.g:104:68: ( joined_clause )*
				while ( stream_joined_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_joined_clause.nextTree());
				}
				stream_joined_clause.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "identification_variable_declaration"


	public static class join_section_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "join_section"
	// JPA2.g:105:1: join_section : ( joined_clause )* ;
	public final JPA2Parser.join_section_return join_section() throws RecognitionException {
		JPA2Parser.join_section_return retval = new JPA2Parser.join_section_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope joined_clause21 =null;


		try {
			// JPA2.g:105:14: ( ( joined_clause )* )
			// JPA2.g:106:5: ( joined_clause )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:106:5: ( joined_clause )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( (LA11_0==INNER||(LA11_0 >= JOIN && LA11_0 <= LEFT)) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// JPA2.g:106:5: joined_clause
					{
					pushFollow(FOLLOW_joined_clause_in_join_section739);
					joined_clause21=joined_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, joined_clause21.getTree());

					}
					break;

				default :
					break loop11;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "join_section"


	public static class joined_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "joined_clause"
	// JPA2.g:107:1: joined_clause : ( join | fetch_join );
	public final JPA2Parser.joined_clause_return joined_clause() throws RecognitionException {
		JPA2Parser.joined_clause_return retval = new JPA2Parser.joined_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope join22 =null;
		ParserRuleReturnScope fetch_join23 =null;


		try {
			// JPA2.g:107:15: ( join | fetch_join )
			int alt12=2;
			switch ( input.LA(1) ) {
			case LEFT:
				{
				int LA12_1 = input.LA(2);
				if ( (LA12_1==OUTER) ) {
					int LA12_4 = input.LA(3);
					if ( (LA12_4==JOIN) ) {
						int LA12_3 = input.LA(4);
						if ( (LA12_3==WORD||LA12_3==132) ) {
							alt12=1;
						}
						else if ( (LA12_3==FETCH) ) {
							alt12=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 12, 3, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 12, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA12_1==JOIN) ) {
					int LA12_3 = input.LA(3);
					if ( (LA12_3==WORD||LA12_3==132) ) {
						alt12=1;
					}
					else if ( (LA12_3==FETCH) ) {
						alt12=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 12, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 12, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INNER:
				{
				int LA12_2 = input.LA(2);
				if ( (LA12_2==JOIN) ) {
					int LA12_3 = input.LA(3);
					if ( (LA12_3==WORD||LA12_3==132) ) {
						alt12=1;
					}
					else if ( (LA12_3==FETCH) ) {
						alt12=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 12, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 12, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case JOIN:
				{
				int LA12_3 = input.LA(2);
				if ( (LA12_3==WORD||LA12_3==132) ) {
					alt12=1;
				}
				else if ( (LA12_3==FETCH) ) {
					alt12=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 12, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 12, 0, input);
				throw nvae;
			}
			switch (alt12) {
				case 1 :
					// JPA2.g:107:17: join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_join_in_joined_clause747);
					join22=join();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join22.getTree());

					}
					break;
				case 2 :
					// JPA2.g:107:24: fetch_join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_fetch_join_in_joined_clause751);
					fetch_join23=fetch_join();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, fetch_join23.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "joined_clause"


	public static class range_variable_declaration_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "range_variable_declaration"
	// JPA2.g:108:1: range_variable_declaration : entity_name ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) ;
	public final JPA2Parser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
		JPA2Parser.range_variable_declaration_return retval = new JPA2Parser.range_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal25=null;
		ParserRuleReturnScope entity_name24 =null;
		ParserRuleReturnScope identification_variable26 =null;

		Object string_literal25_tree=null;
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleSubtreeStream stream_entity_name=new RewriteRuleSubtreeStream(adaptor,"rule entity_name");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:109:6: ( entity_name ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) )
			// JPA2.g:109:8: entity_name ( 'AS' )? identification_variable
			{
			pushFollow(FOLLOW_entity_name_in_range_variable_declaration763);
			entity_name24=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_entity_name.add(entity_name24.getTree());
			// JPA2.g:109:20: ( 'AS' )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==81) ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// JPA2.g:109:21: 'AS'
					{
					string_literal25=(Token)match(input,81,FOLLOW_81_in_range_variable_declaration766); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_81.add(string_literal25);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_range_variable_declaration770);
			identification_variable26=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable26.getTree());
			// AST REWRITE
			// elements: entity_name
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 110:6: -> ^( T_ID_VAR[$identification_variable.text] entity_name )
			{
				// JPA2.g:110:9: ^( T_ID_VAR[$identification_variable.text] entity_name )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new IdentificationVariableNode(T_ID_VAR, (identification_variable26!=null?input.toString(identification_variable26.start,identification_variable26.stop):null)), root_1);
				adaptor.addChild(root_1, stream_entity_name.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "range_variable_declaration"


	public static class join_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "join"
	// JPA2.g:111:1: join : join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? ) ;
	public final JPA2Parser.join_return join() throws RecognitionException {
		JPA2Parser.join_return retval = new JPA2Parser.join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal29=null;
		Token string_literal31=null;
		ParserRuleReturnScope join_spec27 =null;
		ParserRuleReturnScope join_association_path_expression28 =null;
		ParserRuleReturnScope identification_variable30 =null;
		ParserRuleReturnScope conditional_expression32 =null;

		Object string_literal29_tree=null;
		Object string_literal31_tree=null;
		RewriteRuleTokenStream stream_121=new RewriteRuleTokenStream(adaptor,"token 121");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");
		RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");

		try {
			// JPA2.g:112:6: ( join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? ) )
			// JPA2.g:112:8: join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )?
			{
			pushFollow(FOLLOW_join_spec_in_join799);
			join_spec27=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_spec.add(join_spec27.getTree());
			pushFollow(FOLLOW_join_association_path_expression_in_join801);
			join_association_path_expression28=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression28.getTree());
			// JPA2.g:112:51: ( 'AS' )?
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==81) ) {
				alt14=1;
			}
			switch (alt14) {
				case 1 :
					// JPA2.g:112:52: 'AS'
					{
					string_literal29=(Token)match(input,81,FOLLOW_81_in_join804); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_81.add(string_literal29);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_join808);
			identification_variable30=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable30.getTree());
			// JPA2.g:112:83: ( 'ON' conditional_expression )?
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==121) ) {
				alt15=1;
			}
			switch (alt15) {
				case 1 :
					// JPA2.g:112:84: 'ON' conditional_expression
					{
					string_literal31=(Token)match(input,121,FOLLOW_121_in_join811); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_121.add(string_literal31);

					pushFollow(FOLLOW_conditional_expression_in_join813);
					conditional_expression32=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression32.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: conditional_expression, join_association_path_expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 113:6: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? )
			{
				// JPA2.g:113:9: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new JoinVariableNode(T_JOIN_VAR, (join_spec27!=null?input.toString(join_spec27.start,join_spec27.stop):null), (identification_variable30!=null?input.toString(identification_variable30.start,identification_variable30.stop):null)), root_1);
				adaptor.addChild(root_1, stream_join_association_path_expression.nextTree());
				// JPA2.g:113:121: ( conditional_expression )?
				if ( stream_conditional_expression.hasNext() ) {
					adaptor.addChild(root_1, stream_conditional_expression.nextTree());
				}
				stream_conditional_expression.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "join"


	public static class fetch_join_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "fetch_join"
	// JPA2.g:114:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
	public final JPA2Parser.fetch_join_return fetch_join() throws RecognitionException {
		JPA2Parser.fetch_join_return retval = new JPA2Parser.fetch_join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal34=null;
		ParserRuleReturnScope join_spec33 =null;
		ParserRuleReturnScope join_association_path_expression35 =null;

		Object string_literal34_tree=null;

		try {
			// JPA2.g:115:6: ( join_spec 'FETCH' join_association_path_expression )
			// JPA2.g:115:8: join_spec 'FETCH' join_association_path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_join_spec_in_fetch_join847);
			join_spec33=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec33.getTree());

			string_literal34=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join849); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal34_tree = (Object)adaptor.create(string_literal34);
			adaptor.addChild(root_0, string_literal34_tree);
			}

			pushFollow(FOLLOW_join_association_path_expression_in_fetch_join851);
			join_association_path_expression35=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression35.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "fetch_join"


	public static class join_spec_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "join_spec"
	// JPA2.g:116:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
	public final JPA2Parser.join_spec_return join_spec() throws RecognitionException {
		JPA2Parser.join_spec_return retval = new JPA2Parser.join_spec_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal36=null;
		Token string_literal37=null;
		Token string_literal38=null;
		Token string_literal39=null;

		Object string_literal36_tree=null;
		Object string_literal37_tree=null;
		Object string_literal38_tree=null;
		Object string_literal39_tree=null;

		try {
			// JPA2.g:117:6: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
			// JPA2.g:117:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:117:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
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
					// JPA2.g:117:9: ( 'LEFT' ) ( 'OUTER' )?
					{
					// JPA2.g:117:9: ( 'LEFT' )
					// JPA2.g:117:10: 'LEFT'
					{
					string_literal36=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec865); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal36_tree = (Object)adaptor.create(string_literal36);
					adaptor.addChild(root_0, string_literal36_tree);
					}

					}

					// JPA2.g:117:18: ( 'OUTER' )?
					int alt16=2;
					int LA16_0 = input.LA(1);
					if ( (LA16_0==OUTER) ) {
						alt16=1;
					}
					switch (alt16) {
						case 1 :
							// JPA2.g:117:19: 'OUTER'
							{
							string_literal37=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec869); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal37_tree = (Object)adaptor.create(string_literal37);
							adaptor.addChild(root_0, string_literal37_tree);
							}

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:117:31: 'INNER'
					{
					string_literal38=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec875); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal38_tree = (Object)adaptor.create(string_literal38);
					adaptor.addChild(root_0, string_literal38_tree);
					}

					}
					break;

			}

			string_literal39=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec880); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal39_tree = (Object)adaptor.create(string_literal39);
			adaptor.addChild(root_0, string_literal39_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "join_spec"


	public static class join_association_path_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "join_association_path_expression"
	// JPA2.g:120:1: join_association_path_expression : ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name );
	public final JPA2Parser.join_association_path_expression_return join_association_path_expression() throws RecognitionException {
		JPA2Parser.join_association_path_expression_return retval = new JPA2Parser.join_association_path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal41=null;
		Token char_literal43=null;
		Token string_literal45=null;
		Token char_literal47=null;
		Token char_literal49=null;
		Token string_literal51=null;
		Token char_literal53=null;
		ParserRuleReturnScope identification_variable40 =null;
		ParserRuleReturnScope field42 =null;
		ParserRuleReturnScope field44 =null;
		ParserRuleReturnScope identification_variable46 =null;
		ParserRuleReturnScope field48 =null;
		ParserRuleReturnScope field50 =null;
		ParserRuleReturnScope subtype52 =null;
		ParserRuleReturnScope entity_name54 =null;

		Object char_literal41_tree=null;
		Object char_literal43_tree=null;
		Object string_literal45_tree=null;
		Object char_literal47_tree=null;
		Object char_literal49_tree=null;
		Object string_literal51_tree=null;
		Object char_literal53_tree=null;
		RewriteRuleTokenStream stream_132=new RewriteRuleTokenStream(adaptor,"token 132");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_subtype=new RewriteRuleSubtreeStream(adaptor,"rule subtype");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:121:6: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name )
			int alt22=3;
			int LA22_0 = input.LA(1);
			if ( (LA22_0==WORD) ) {
				int LA22_1 = input.LA(2);
				if ( (LA22_1==62) ) {
					alt22=1;
				}
				else if ( (LA22_1==EOF||(LA22_1 >= GROUP && LA22_1 <= HAVING)||LA22_1==INNER||(LA22_1 >= JOIN && LA22_1 <= LEFT)||LA22_1==ORDER||LA22_1==RPAREN||LA22_1==SET||LA22_1==WORD||LA22_1==60||LA22_1==81||LA22_1==105||LA22_1==140) ) {
					alt22=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA22_0==132) ) {
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
					// JPA2.g:121:8: identification_variable '.' ( field '.' )* ( field )?
					{
					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression894);
					identification_variable40=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable40.getTree());
					char_literal41=(Token)match(input,62,FOLLOW_62_in_join_association_path_expression896); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_62.add(char_literal41);

					// JPA2.g:121:36: ( field '.' )*
					loop18:
					while (true) {
						int alt18=2;
						switch ( input.LA(1) ) {
						case WORD:
							{
							int LA18_1 = input.LA(2);
							if ( (LA18_1==62) ) {
								alt18=1;
							}

							}
							break;
						case 125:
							{
							int LA18_2 = input.LA(2);
							if ( (LA18_2==62) ) {
								alt18=1;
							}

							}
							break;
						case 101:
							{
							int LA18_3 = input.LA(2);
							if ( (LA18_3==62) ) {
								alt18=1;
							}

							}
							break;
						case GROUP:
							{
							int LA18_4 = input.LA(2);
							if ( (LA18_4==62) ) {
								alt18=1;
							}

							}
							break;
						case ORDER:
							{
							int LA18_5 = input.LA(2);
							if ( (LA18_5==62) ) {
								alt18=1;
							}

							}
							break;
						case MAX:
							{
							int LA18_6 = input.LA(2);
							if ( (LA18_6==62) ) {
								alt18=1;
							}

							}
							break;
						case MIN:
							{
							int LA18_7 = input.LA(2);
							if ( (LA18_7==62) ) {
								alt18=1;
							}

							}
							break;
						case SUM:
							{
							int LA18_8 = input.LA(2);
							if ( (LA18_8==62) ) {
								alt18=1;
							}

							}
							break;
						case AVG:
							{
							int LA18_9 = input.LA(2);
							if ( (LA18_9==62) ) {
								alt18=1;
							}

							}
							break;
						case COUNT:
							{
							int LA18_10 = input.LA(2);
							if ( (LA18_10==62) ) {
								alt18=1;
							}

							}
							break;
						case 91:
						case 97:
						case 103:
						case 112:
						case 114:
						case 122:
						case 124:
						case 138:
						case 141:
							{
							int LA18_11 = input.LA(2);
							if ( (LA18_11==62) ) {
								alt18=1;
							}

							}
							break;
						}
						switch (alt18) {
						case 1 :
							// JPA2.g:121:37: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression899);
							field42=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field42.getTree());
							char_literal43=(Token)match(input,62,FOLLOW_62_in_join_association_path_expression900); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_62.add(char_literal43);

							}
							break;

						default :
							break loop18;
						}
					}

					// JPA2.g:121:48: ( field )?
					int alt19=2;
					switch ( input.LA(1) ) {
						case WORD:
							{
							int LA19_1 = input.LA(2);
							if ( (synpred21_JPA2()) ) {
								alt19=1;
							}
							}
							break;
						case AVG:
						case COUNT:
						case MAX:
						case MIN:
						case SUM:
						case 91:
						case 97:
						case 101:
						case 103:
						case 112:
						case 114:
						case 122:
						case 124:
						case 125:
						case 138:
						case 141:
							{
							alt19=1;
							}
							break;
						case GROUP:
							{
							int LA19_3 = input.LA(2);
							if ( (LA19_3==EOF||(LA19_3 >= GROUP && LA19_3 <= HAVING)||LA19_3==INNER||(LA19_3 >= JOIN && LA19_3 <= LEFT)||LA19_3==ORDER||LA19_3==RPAREN||LA19_3==SET||LA19_3==WORD||LA19_3==60||LA19_3==81||LA19_3==105||LA19_3==140) ) {
								alt19=1;
							}
							}
							break;
						case ORDER:
							{
							int LA19_4 = input.LA(2);
							if ( (LA19_4==EOF||(LA19_4 >= GROUP && LA19_4 <= HAVING)||LA19_4==INNER||(LA19_4 >= JOIN && LA19_4 <= LEFT)||LA19_4==ORDER||LA19_4==RPAREN||LA19_4==SET||LA19_4==WORD||LA19_4==60||LA19_4==81||LA19_4==105||LA19_4==140) ) {
								alt19=1;
							}
							}
							break;
					}
					switch (alt19) {
						case 1 :
							// JPA2.g:121:48: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression904);
							field44=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field44.getTree());
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 122:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:122:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable40!=null?input.toString(identification_variable40.start,identification_variable40.stop):null)), root_1);
						// JPA2.g:122:73: ( field )*
						while ( stream_field.hasNext() ) {
							adaptor.addChild(root_1, stream_field.nextTree());
						}
						stream_field.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// JPA2.g:123:9: 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')'
					{
					string_literal45=(Token)match(input,132,FOLLOW_132_in_join_association_path_expression939); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_132.add(string_literal45);

					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression941);
					identification_variable46=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable46.getTree());
					char_literal47=(Token)match(input,62,FOLLOW_62_in_join_association_path_expression943); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_62.add(char_literal47);

					// JPA2.g:123:46: ( field '.' )*
					loop20:
					while (true) {
						int alt20=2;
						switch ( input.LA(1) ) {
						case WORD:
							{
							int LA20_1 = input.LA(2);
							if ( (LA20_1==62) ) {
								alt20=1;
							}

							}
							break;
						case 125:
							{
							int LA20_2 = input.LA(2);
							if ( (LA20_2==62) ) {
								alt20=1;
							}

							}
							break;
						case 101:
							{
							int LA20_3 = input.LA(2);
							if ( (LA20_3==62) ) {
								alt20=1;
							}

							}
							break;
						case GROUP:
							{
							int LA20_4 = input.LA(2);
							if ( (LA20_4==62) ) {
								alt20=1;
							}

							}
							break;
						case ORDER:
							{
							int LA20_5 = input.LA(2);
							if ( (LA20_5==62) ) {
								alt20=1;
							}

							}
							break;
						case MAX:
							{
							int LA20_6 = input.LA(2);
							if ( (LA20_6==62) ) {
								alt20=1;
							}

							}
							break;
						case MIN:
							{
							int LA20_7 = input.LA(2);
							if ( (LA20_7==62) ) {
								alt20=1;
							}

							}
							break;
						case SUM:
							{
							int LA20_8 = input.LA(2);
							if ( (LA20_8==62) ) {
								alt20=1;
							}

							}
							break;
						case AVG:
							{
							int LA20_9 = input.LA(2);
							if ( (LA20_9==62) ) {
								alt20=1;
							}

							}
							break;
						case COUNT:
							{
							int LA20_10 = input.LA(2);
							if ( (LA20_10==62) ) {
								alt20=1;
							}

							}
							break;
						case 91:
						case 97:
						case 103:
						case 112:
						case 114:
						case 122:
						case 124:
						case 138:
						case 141:
							{
							int LA20_11 = input.LA(2);
							if ( (LA20_11==62) ) {
								alt20=1;
							}

							}
							break;
						}
						switch (alt20) {
						case 1 :
							// JPA2.g:123:47: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression946);
							field48=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field48.getTree());
							char_literal49=(Token)match(input,62,FOLLOW_62_in_join_association_path_expression947); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_62.add(char_literal49);

							}
							break;

						default :
							break loop20;
						}
					}

					// JPA2.g:123:58: ( field )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( (LA21_0==AVG||LA21_0==COUNT||LA21_0==GROUP||(LA21_0 >= MAX && LA21_0 <= MIN)||LA21_0==ORDER||LA21_0==SUM||LA21_0==WORD||LA21_0==91||LA21_0==97||LA21_0==101||LA21_0==103||LA21_0==112||LA21_0==114||LA21_0==122||(LA21_0 >= 124 && LA21_0 <= 125)||LA21_0==138||LA21_0==141) ) {
						alt21=1;
					}
					switch (alt21) {
						case 1 :
							// JPA2.g:123:58: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression951);
							field50=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field50.getTree());
							}
							break;

					}

					string_literal51=(Token)match(input,81,FOLLOW_81_in_join_association_path_expression954); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_81.add(string_literal51);

					pushFollow(FOLLOW_subtype_in_join_association_path_expression956);
					subtype52=subtype();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subtype.add(subtype52.getTree());
					char_literal53=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_join_association_path_expression958); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal53);

					// AST REWRITE
					// elements: field
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 124:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:124:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable46!=null?input.toString(identification_variable46.start,identification_variable46.stop):null)), root_1);
						// JPA2.g:124:73: ( field )*
						while ( stream_field.hasNext() ) {
							adaptor.addChild(root_1, stream_field.nextTree());
						}
						stream_field.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// JPA2.g:125:8: entity_name
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_name_in_join_association_path_expression991);
					entity_name54=entity_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_name54.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "join_association_path_expression"


	public static class collection_member_declaration_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "collection_member_declaration"
	// JPA2.g:128:1: collection_member_declaration : 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
	public final JPA2Parser.collection_member_declaration_return collection_member_declaration() throws RecognitionException {
		JPA2Parser.collection_member_declaration_return retval = new JPA2Parser.collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal55=null;
		Token char_literal56=null;
		Token char_literal58=null;
		Token string_literal59=null;
		ParserRuleReturnScope path_expression57 =null;
		ParserRuleReturnScope identification_variable60 =null;

		Object string_literal55_tree=null;
		Object char_literal56_tree=null;
		Object char_literal58_tree=null;
		Object string_literal59_tree=null;
		RewriteRuleTokenStream stream_IN=new RewriteRuleTokenStream(adaptor,"token IN");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");

		try {
			// JPA2.g:129:5: ( 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
			// JPA2.g:129:7: 'IN' '(' path_expression ')' ( 'AS' )? identification_variable
			{
			string_literal55=(Token)match(input,IN,FOLLOW_IN_in_collection_member_declaration1004); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_IN.add(string_literal55);

			char_literal56=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration1005); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal56);

			pushFollow(FOLLOW_path_expression_in_collection_member_declaration1007);
			path_expression57=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_path_expression.add(path_expression57.getTree());
			char_literal58=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration1009); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal58);

			// JPA2.g:129:35: ( 'AS' )?
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0==81) ) {
				alt23=1;
			}
			switch (alt23) {
				case 1 :
					// JPA2.g:129:36: 'AS'
					{
					string_literal59=(Token)match(input,81,FOLLOW_81_in_collection_member_declaration1012); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_81.add(string_literal59);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_collection_member_declaration1016);
			identification_variable60=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable60.getTree());
			// AST REWRITE
			// elements: path_expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 130:5: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
			{
				// JPA2.g:130:8: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new CollectionMemberNode(T_COLLECTION_MEMBER, (identification_variable60!=null?input.toString(identification_variable60.start,identification_variable60.stop):null)), root_1);
				adaptor.addChild(root_1, stream_path_expression.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "collection_member_declaration"


	public static class qualified_identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "qualified_identification_variable"
	// JPA2.g:132:1: qualified_identification_variable : ( map_field_identification_variable | 'ENTRY(' identification_variable ')' );
	public final JPA2Parser.qualified_identification_variable_return qualified_identification_variable() throws RecognitionException {
		JPA2Parser.qualified_identification_variable_return retval = new JPA2Parser.qualified_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal62=null;
		Token char_literal64=null;
		ParserRuleReturnScope map_field_identification_variable61 =null;
		ParserRuleReturnScope identification_variable63 =null;

		Object string_literal62_tree=null;
		Object char_literal64_tree=null;

		try {
			// JPA2.g:133:5: ( map_field_identification_variable | 'ENTRY(' identification_variable ')' )
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==106||LA24_0==137) ) {
				alt24=1;
			}
			else if ( (LA24_0==96) ) {
				alt24=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 24, 0, input);
				throw nvae;
			}

			switch (alt24) {
				case 1 :
					// JPA2.g:133:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_qualified_identification_variable1045);
					map_field_identification_variable61=map_field_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map_field_identification_variable61.getTree());

					}
					break;
				case 2 :
					// JPA2.g:134:7: 'ENTRY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal62=(Token)match(input,96,FOLLOW_96_in_qualified_identification_variable1053); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal62_tree = (Object)adaptor.create(string_literal62);
					adaptor.addChild(root_0, string_literal62_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_qualified_identification_variable1054);
					identification_variable63=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable63.getTree());

					char_literal64=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_qualified_identification_variable1055); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal64_tree = (Object)adaptor.create(char_literal64);
					adaptor.addChild(root_0, char_literal64_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "qualified_identification_variable"


	public static class map_field_identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "map_field_identification_variable"
	// JPA2.g:135:1: map_field_identification_variable : ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' );
	public final JPA2Parser.map_field_identification_variable_return map_field_identification_variable() throws RecognitionException {
		JPA2Parser.map_field_identification_variable_return retval = new JPA2Parser.map_field_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal65=null;
		Token char_literal67=null;
		Token string_literal68=null;
		Token char_literal70=null;
		ParserRuleReturnScope identification_variable66 =null;
		ParserRuleReturnScope identification_variable69 =null;

		Object string_literal65_tree=null;
		Object char_literal67_tree=null;
		Object string_literal68_tree=null;
		Object char_literal70_tree=null;

		try {
			// JPA2.g:135:35: ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' )
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==106) ) {
				alt25=1;
			}
			else if ( (LA25_0==137) ) {
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
					// JPA2.g:135:37: 'KEY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal65=(Token)match(input,106,FOLLOW_106_in_map_field_identification_variable1062); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal65_tree = (Object)adaptor.create(string_literal65);
					adaptor.addChild(root_0, string_literal65_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1063);
					identification_variable66=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable66.getTree());

					char_literal67=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1064); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal67_tree = (Object)adaptor.create(char_literal67);
					adaptor.addChild(root_0, char_literal67_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:135:72: 'VALUE(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal68=(Token)match(input,137,FOLLOW_137_in_map_field_identification_variable1068); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal68_tree = (Object)adaptor.create(string_literal68);
					adaptor.addChild(root_0, string_literal68_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1069);
					identification_variable69=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable69.getTree());

					char_literal70=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1070); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal70_tree = (Object)adaptor.create(char_literal70);
					adaptor.addChild(root_0, char_literal70_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "map_field_identification_variable"


	public static class path_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "path_expression"
	// JPA2.g:138:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
	public final JPA2Parser.path_expression_return path_expression() throws RecognitionException {
		JPA2Parser.path_expression_return retval = new JPA2Parser.path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal72=null;
		Token char_literal74=null;
		ParserRuleReturnScope identification_variable71 =null;
		ParserRuleReturnScope field73 =null;
		ParserRuleReturnScope field75 =null;

		Object char_literal72_tree=null;
		Object char_literal74_tree=null;
		RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:139:5: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
			// JPA2.g:139:8: identification_variable '.' ( field '.' )* ( field )?
			{
			pushFollow(FOLLOW_identification_variable_in_path_expression1084);
			identification_variable71=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable71.getTree());
			char_literal72=(Token)match(input,62,FOLLOW_62_in_path_expression1086); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_62.add(char_literal72);

			// JPA2.g:139:36: ( field '.' )*
			loop26:
			while (true) {
				int alt26=2;
				switch ( input.LA(1) ) {
				case WORD:
					{
					int LA26_1 = input.LA(2);
					if ( (LA26_1==62) ) {
						alt26=1;
					}

					}
					break;
				case 125:
					{
					int LA26_2 = input.LA(2);
					if ( (LA26_2==62) ) {
						alt26=1;
					}

					}
					break;
				case 101:
					{
					int LA26_3 = input.LA(2);
					if ( (LA26_3==62) ) {
						alt26=1;
					}

					}
					break;
				case GROUP:
					{
					int LA26_4 = input.LA(2);
					if ( (LA26_4==62) ) {
						alt26=1;
					}

					}
					break;
				case ORDER:
					{
					int LA26_5 = input.LA(2);
					if ( (LA26_5==62) ) {
						alt26=1;
					}

					}
					break;
				case MAX:
					{
					int LA26_6 = input.LA(2);
					if ( (LA26_6==62) ) {
						alt26=1;
					}

					}
					break;
				case MIN:
					{
					int LA26_7 = input.LA(2);
					if ( (LA26_7==62) ) {
						alt26=1;
					}

					}
					break;
				case SUM:
					{
					int LA26_8 = input.LA(2);
					if ( (LA26_8==62) ) {
						alt26=1;
					}

					}
					break;
				case AVG:
					{
					int LA26_9 = input.LA(2);
					if ( (LA26_9==62) ) {
						alt26=1;
					}

					}
					break;
				case COUNT:
					{
					int LA26_10 = input.LA(2);
					if ( (LA26_10==62) ) {
						alt26=1;
					}

					}
					break;
				case 91:
				case 97:
				case 103:
				case 112:
				case 114:
				case 122:
				case 124:
				case 138:
				case 141:
					{
					int LA26_11 = input.LA(2);
					if ( (LA26_11==62) ) {
						alt26=1;
					}

					}
					break;
				}
				switch (alt26) {
				case 1 :
					// JPA2.g:139:37: field '.'
					{
					pushFollow(FOLLOW_field_in_path_expression1089);
					field73=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field73.getTree());
					char_literal74=(Token)match(input,62,FOLLOW_62_in_path_expression1090); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_62.add(char_literal74);

					}
					break;

				default :
					break loop26;
				}
			}

			// JPA2.g:139:48: ( field )?
			int alt27=2;
			switch ( input.LA(1) ) {
				case WORD:
					{
					int LA27_1 = input.LA(2);
					if ( (synpred30_JPA2()) ) {
						alt27=1;
					}
					}
					break;
				case AVG:
				case COUNT:
				case MAX:
				case MIN:
				case SUM:
				case 91:
				case 97:
				case 103:
				case 112:
				case 114:
				case 122:
				case 124:
				case 125:
				case 138:
				case 141:
					{
					alt27=1;
					}
					break;
				case 101:
					{
					switch ( input.LA(2) ) {
						case EOF:
						case AND:
						case ASC:
						case DESC:
						case GROUP:
						case HAVING:
						case INNER:
						case JOIN:
						case LEFT:
						case NOT:
						case OR:
						case ORDER:
						case RPAREN:
						case SET:
						case 58:
						case 59:
						case 60:
						case 61:
						case 63:
						case 65:
						case 66:
						case 67:
						case 68:
						case 69:
						case 70:
						case 81:
						case 82:
						case 93:
						case 95:
						case 98:
						case 101:
						case 105:
						case 109:
						case 111:
						case 123:
						case 130:
						case 139:
						case 140:
							{
							alt27=1;
							}
							break;
						case WORD:
							{
							int LA27_7 = input.LA(3);
							if ( (LA27_7==EOF||LA27_7==LPAREN||LA27_7==RPAREN||LA27_7==60||LA27_7==101) ) {
								alt27=1;
							}
							}
							break;
						case IN:
							{
							int LA27_8 = input.LA(3);
							if ( (LA27_8==LPAREN||LA27_8==NAMED_PARAMETER||LA27_8==57||LA27_8==71) ) {
								alt27=1;
							}
							}
							break;
					}
					}
					break;
				case GROUP:
					{
					int LA27_4 = input.LA(2);
					if ( (LA27_4==EOF||(LA27_4 >= AND && LA27_4 <= ASC)||LA27_4==DESC||(LA27_4 >= GROUP && LA27_4 <= INNER)||(LA27_4 >= JOIN && LA27_4 <= LEFT)||(LA27_4 >= NOT && LA27_4 <= ORDER)||LA27_4==RPAREN||LA27_4==SET||LA27_4==WORD||(LA27_4 >= 58 && LA27_4 <= 61)||LA27_4==63||(LA27_4 >= 65 && LA27_4 <= 70)||(LA27_4 >= 81 && LA27_4 <= 82)||LA27_4==93||LA27_4==95||LA27_4==98||LA27_4==101||LA27_4==105||LA27_4==109||LA27_4==111||LA27_4==123||LA27_4==130||(LA27_4 >= 139 && LA27_4 <= 140)) ) {
						alt27=1;
					}
					}
					break;
				case ORDER:
					{
					int LA27_5 = input.LA(2);
					if ( (LA27_5==EOF||(LA27_5 >= AND && LA27_5 <= ASC)||LA27_5==DESC||(LA27_5 >= GROUP && LA27_5 <= INNER)||(LA27_5 >= JOIN && LA27_5 <= LEFT)||(LA27_5 >= NOT && LA27_5 <= ORDER)||LA27_5==RPAREN||LA27_5==SET||LA27_5==WORD||(LA27_5 >= 58 && LA27_5 <= 61)||LA27_5==63||(LA27_5 >= 65 && LA27_5 <= 70)||(LA27_5 >= 81 && LA27_5 <= 82)||LA27_5==93||LA27_5==95||LA27_5==98||LA27_5==101||LA27_5==105||LA27_5==109||LA27_5==111||LA27_5==123||LA27_5==130||(LA27_5 >= 139 && LA27_5 <= 140)) ) {
						alt27=1;
					}
					}
					break;
			}
			switch (alt27) {
				case 1 :
					// JPA2.g:139:48: field
					{
					pushFollow(FOLLOW_field_in_path_expression1094);
					field75=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field75.getTree());
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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 140:5: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
			{
				// JPA2.g:140:8: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable71!=null?input.toString(identification_variable71.start,identification_variable71.stop):null)), root_1);
				// JPA2.g:140:68: ( field )*
				while ( stream_field.hasNext() ) {
					adaptor.addChild(root_1, stream_field.nextTree());
				}
				stream_field.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "path_expression"


	public static class general_identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "general_identification_variable"
	// JPA2.g:145:1: general_identification_variable : ( identification_variable | map_field_identification_variable );
	public final JPA2Parser.general_identification_variable_return general_identification_variable() throws RecognitionException {
		JPA2Parser.general_identification_variable_return retval = new JPA2Parser.general_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable76 =null;
		ParserRuleReturnScope map_field_identification_variable77 =null;


		try {
			// JPA2.g:146:5: ( identification_variable | map_field_identification_variable )
			int alt28=2;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==WORD) ) {
				alt28=1;
			}
			else if ( (LA28_0==106||LA28_0==137) ) {
				alt28=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 28, 0, input);
				throw nvae;
			}

			switch (alt28) {
				case 1 :
					// JPA2.g:146:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_general_identification_variable1133);
					identification_variable76=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable76.getTree());

					}
					break;
				case 2 :
					// JPA2.g:147:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_general_identification_variable1141);
					map_field_identification_variable77=map_field_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map_field_identification_variable77.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "general_identification_variable"


	public static class update_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "update_clause"
	// JPA2.g:150:1: update_clause : identification_variable_declaration SET update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* ) ;
	public final JPA2Parser.update_clause_return update_clause() throws RecognitionException {
		JPA2Parser.update_clause_return retval = new JPA2Parser.update_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token SET79=null;
		Token char_literal81=null;
		ParserRuleReturnScope identification_variable_declaration78 =null;
		ParserRuleReturnScope update_item80 =null;
		ParserRuleReturnScope update_item82 =null;

		Object SET79_tree=null;
		Object char_literal81_tree=null;
		RewriteRuleTokenStream stream_SET=new RewriteRuleTokenStream(adaptor,"token SET");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_update_item=new RewriteRuleSubtreeStream(adaptor,"rule update_item");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:151:5: ( identification_variable_declaration SET update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* ) )
			// JPA2.g:151:7: identification_variable_declaration SET update_item ( ',' update_item )*
			{
			pushFollow(FOLLOW_identification_variable_declaration_in_update_clause1154);
			identification_variable_declaration78=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration78.getTree());
			SET79=(Token)match(input,SET,FOLLOW_SET_in_update_clause1156); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_SET.add(SET79);

			pushFollow(FOLLOW_update_item_in_update_clause1158);
			update_item80=update_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_item.add(update_item80.getTree());
			// JPA2.g:151:59: ( ',' update_item )*
			loop29:
			while (true) {
				int alt29=2;
				int LA29_0 = input.LA(1);
				if ( (LA29_0==60) ) {
					alt29=1;
				}

				switch (alt29) {
				case 1 :
					// JPA2.g:151:60: ',' update_item
					{
					char_literal81=(Token)match(input,60,FOLLOW_60_in_update_clause1161); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal81);

					pushFollow(FOLLOW_update_item_in_update_clause1163);
					update_item82=update_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_update_item.add(update_item82.getTree());
					}
					break;

				default :
					break loop29;
				}
			}

			// AST REWRITE
			// elements: identification_variable_declaration, SET, update_item, 60, update_item
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 152:5: -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* )
			{
				// JPA2.g:152:8: ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCES), root_1);
				adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
				adaptor.addChild(root_1, new UpdateSetNode(stream_SET.nextToken()));
				adaptor.addChild(root_1, stream_update_item.nextTree());
				// JPA2.g:152:108: ( ',' update_item )*
				while ( stream_update_item.hasNext()||stream_60.hasNext() ) {
					adaptor.addChild(root_1, stream_60.nextNode());
					adaptor.addChild(root_1, stream_update_item.nextTree());
				}
				stream_update_item.reset();
				stream_60.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "update_clause"


	public static class update_item_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "update_item"
	// JPA2.g:153:1: update_item : path_expression '=' new_value ;
	public final JPA2Parser.update_item_return update_item() throws RecognitionException {
		JPA2Parser.update_item_return retval = new JPA2Parser.update_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal84=null;
		ParserRuleReturnScope path_expression83 =null;
		ParserRuleReturnScope new_value85 =null;

		Object char_literal84_tree=null;

		try {
			// JPA2.g:154:5: ( path_expression '=' new_value )
			// JPA2.g:154:7: path_expression '=' new_value
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_update_item1205);
			path_expression83=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression83.getTree());

			char_literal84=(Token)match(input,68,FOLLOW_68_in_update_item1207); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal84_tree = (Object)adaptor.create(char_literal84);
			adaptor.addChild(root_0, char_literal84_tree);
			}

			pushFollow(FOLLOW_new_value_in_update_item1209);
			new_value85=new_value();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, new_value85.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "update_item"


	public static class new_value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "new_value"
	// JPA2.g:155:1: new_value : ( scalar_expression | simple_entity_expression | 'NULL' );
	public final JPA2Parser.new_value_return new_value() throws RecognitionException {
		JPA2Parser.new_value_return retval = new JPA2Parser.new_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal88=null;
		ParserRuleReturnScope scalar_expression86 =null;
		ParserRuleReturnScope simple_entity_expression87 =null;

		Object string_literal88_tree=null;

		try {
			// JPA2.g:156:5: ( scalar_expression | simple_entity_expression | 'NULL' )
			int alt30=3;
			switch ( input.LA(1) ) {
			case AVG:
			case COUNT:
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case MAX:
			case MIN:
			case STRING_LITERAL:
			case SUM:
			case 59:
			case 61:
			case 64:
			case 76:
			case 78:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 100:
			case 102:
			case 104:
			case 108:
			case 110:
			case 113:
			case 118:
			case 126:
			case 128:
			case 129:
			case 133:
			case 134:
			case 136:
			case 142:
			case 143:
				{
				alt30=1;
				}
				break;
			case WORD:
				{
				int LA30_2 = input.LA(2);
				if ( (synpred33_JPA2()) ) {
					alt30=1;
				}
				else if ( (synpred34_JPA2()) ) {
					alt30=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 30, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 71:
				{
				int LA30_3 = input.LA(2);
				if ( (LA30_3==64) ) {
					int LA30_8 = input.LA(3);
					if ( (LA30_8==INT_NUMERAL) ) {
						int LA30_9 = input.LA(4);
						if ( (synpred33_JPA2()) ) {
							alt30=1;
						}
						else if ( (synpred34_JPA2()) ) {
							alt30=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 30, 9, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 30, 8, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA30_3==INT_NUMERAL) ) {
					int LA30_9 = input.LA(3);
					if ( (synpred33_JPA2()) ) {
						alt30=1;
					}
					else if ( (synpred34_JPA2()) ) {
						alt30=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 30, 9, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 30, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA30_4 = input.LA(2);
				if ( (synpred33_JPA2()) ) {
					alt30=1;
				}
				else if ( (synpred34_JPA2()) ) {
					alt30=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 30, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 57:
				{
				int LA30_5 = input.LA(2);
				if ( (LA30_5==WORD) ) {
					int LA30_10 = input.LA(3);
					if ( (LA30_10==144) ) {
						int LA30_11 = input.LA(4);
						if ( (synpred33_JPA2()) ) {
							alt30=1;
						}
						else if ( (synpred34_JPA2()) ) {
							alt30=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 30, 11, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 30, 10, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 30, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 117:
				{
				alt30=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 30, 0, input);
				throw nvae;
			}
			switch (alt30) {
				case 1 :
					// JPA2.g:156:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_new_value1220);
					scalar_expression86=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression86.getTree());

					}
					break;
				case 2 :
					// JPA2.g:157:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_new_value1228);
					simple_entity_expression87=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression87.getTree());

					}
					break;
				case 3 :
					// JPA2.g:158:7: 'NULL'
					{
					root_0 = (Object)adaptor.nil();


					string_literal88=(Token)match(input,117,FOLLOW_117_in_new_value1236); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal88_tree = (Object)adaptor.create(string_literal88);
					adaptor.addChild(root_0, string_literal88_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "new_value"


	public static class delete_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "delete_clause"
	// JPA2.g:160:1: delete_clause : fr= 'FROM' identification_variable_declaration -> ^( T_SOURCES[$fr] identification_variable_declaration ) ;
	public final JPA2Parser.delete_clause_return delete_clause() throws RecognitionException {
		JPA2Parser.delete_clause_return retval = new JPA2Parser.delete_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token fr=null;
		ParserRuleReturnScope identification_variable_declaration89 =null;

		Object fr_tree=null;
		RewriteRuleTokenStream stream_101=new RewriteRuleTokenStream(adaptor,"token 101");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:161:5: (fr= 'FROM' identification_variable_declaration -> ^( T_SOURCES[$fr] identification_variable_declaration ) )
			// JPA2.g:161:7: fr= 'FROM' identification_variable_declaration
			{
			fr=(Token)match(input,101,FOLLOW_101_in_delete_clause1250); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_101.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_delete_clause1252);
			identification_variable_declaration89=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration89.getTree());
			// AST REWRITE
			// elements: identification_variable_declaration
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 162:5: -> ^( T_SOURCES[$fr] identification_variable_declaration )
			{
				// JPA2.g:162:8: ^( T_SOURCES[$fr] identification_variable_declaration )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "delete_clause"


	public static class select_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "select_clause"
	// JPA2.g:163:1: select_clause : ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) ;
	public final JPA2Parser.select_clause_return select_clause() throws RecognitionException {
		JPA2Parser.select_clause_return retval = new JPA2Parser.select_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal90=null;
		Token char_literal92=null;
		ParserRuleReturnScope select_item91 =null;
		ParserRuleReturnScope select_item93 =null;

		Object string_literal90_tree=null;
		Object char_literal92_tree=null;
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_select_item=new RewriteRuleSubtreeStream(adaptor,"rule select_item");

		try {
			// JPA2.g:164:5: ( ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) )
			// JPA2.g:164:7: ( 'DISTINCT' )? select_item ( ',' select_item )*
			{
			// JPA2.g:164:7: ( 'DISTINCT' )?
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0==DISTINCT) ) {
				alt31=1;
			}
			switch (alt31) {
				case 1 :
					// JPA2.g:164:8: 'DISTINCT'
					{
					string_literal90=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause1280); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal90);

					}
					break;

			}

			pushFollow(FOLLOW_select_item_in_select_clause1284);
			select_item91=select_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_item.add(select_item91.getTree());
			// JPA2.g:164:33: ( ',' select_item )*
			loop32:
			while (true) {
				int alt32=2;
				int LA32_0 = input.LA(1);
				if ( (LA32_0==60) ) {
					alt32=1;
				}

				switch (alt32) {
				case 1 :
					// JPA2.g:164:34: ',' select_item
					{
					char_literal92=(Token)match(input,60,FOLLOW_60_in_select_clause1287); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal92);

					pushFollow(FOLLOW_select_item_in_select_clause1289);
					select_item93=select_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_select_item.add(select_item93.getTree());
					}
					break;

				default :
					break loop32;
				}
			}

			// AST REWRITE
			// elements: DISTINCT, select_item
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 165:5: -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
			{
				// JPA2.g:165:8: ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:165:48: ( 'DISTINCT' )?
				if ( stream_DISTINCT.hasNext() ) {
					adaptor.addChild(root_1, stream_DISTINCT.nextNode());
				}
				stream_DISTINCT.reset();

				// JPA2.g:165:62: ( ^( T_SELECTED_ITEM[] select_item ) )*
				while ( stream_select_item.hasNext() ) {
					// JPA2.g:165:62: ^( T_SELECTED_ITEM[] select_item )
					{
					Object root_2 = (Object)adaptor.nil();
					root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);
					adaptor.addChild(root_2, stream_select_item.nextTree());
					adaptor.addChild(root_1, root_2);
					}

				}
				stream_select_item.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "select_clause"


	public static class select_item_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "select_item"
	// JPA2.g:166:1: select_item : select_expression ( ( 'AS' )? result_variable )? ;
	public final JPA2Parser.select_item_return select_item() throws RecognitionException {
		JPA2Parser.select_item_return retval = new JPA2Parser.select_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal95=null;
		ParserRuleReturnScope select_expression94 =null;
		ParserRuleReturnScope result_variable96 =null;

		Object string_literal95_tree=null;

		try {
			// JPA2.g:167:5: ( select_expression ( ( 'AS' )? result_variable )? )
			// JPA2.g:167:7: select_expression ( ( 'AS' )? result_variable )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_select_expression_in_select_item1332);
			select_expression94=select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, select_expression94.getTree());

			// JPA2.g:167:25: ( ( 'AS' )? result_variable )?
			int alt34=2;
			int LA34_0 = input.LA(1);
			if ( (LA34_0==WORD||LA34_0==81) ) {
				alt34=1;
			}
			switch (alt34) {
				case 1 :
					// JPA2.g:167:26: ( 'AS' )? result_variable
					{
					// JPA2.g:167:26: ( 'AS' )?
					int alt33=2;
					int LA33_0 = input.LA(1);
					if ( (LA33_0==81) ) {
						alt33=1;
					}
					switch (alt33) {
						case 1 :
							// JPA2.g:167:27: 'AS'
							{
							string_literal95=(Token)match(input,81,FOLLOW_81_in_select_item1336); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal95_tree = (Object)adaptor.create(string_literal95);
							adaptor.addChild(root_0, string_literal95_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_result_variable_in_select_item1340);
					result_variable96=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable96.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "select_item"


	public static class select_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "select_expression"
	// JPA2.g:168:1: select_expression : ( path_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression );
	public final JPA2Parser.select_expression_return select_expression() throws RecognitionException {
		JPA2Parser.select_expression_return retval = new JPA2Parser.select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal101=null;
		Token char_literal102=null;
		Token char_literal104=null;
		ParserRuleReturnScope path_expression97 =null;
		ParserRuleReturnScope identification_variable98 =null;
		ParserRuleReturnScope scalar_expression99 =null;
		ParserRuleReturnScope aggregate_expression100 =null;
		ParserRuleReturnScope identification_variable103 =null;
		ParserRuleReturnScope constructor_expression105 =null;

		Object string_literal101_tree=null;
		Object char_literal102_tree=null;
		Object char_literal104_tree=null;
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:169:5: ( path_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression )
			int alt35=6;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA35_1 = input.LA(2);
				if ( (synpred39_JPA2()) ) {
					alt35=1;
				}
				else if ( (synpred40_JPA2()) ) {
					alt35=2;
				}
				else if ( (synpred41_JPA2()) ) {
					alt35=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 35, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 57:
			case 59:
			case 61:
			case 64:
			case 71:
			case 76:
			case 78:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 100:
			case 104:
			case 108:
			case 110:
			case 113:
			case 118:
			case 126:
			case 128:
			case 129:
			case 133:
			case 134:
			case 136:
			case 142:
			case 143:
				{
				alt35=3;
				}
				break;
			case COUNT:
				{
				int LA35_16 = input.LA(2);
				if ( (synpred41_JPA2()) ) {
					alt35=3;
				}
				else if ( (synpred42_JPA2()) ) {
					alt35=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 35, 16, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA35_17 = input.LA(2);
				if ( (synpred41_JPA2()) ) {
					alt35=3;
				}
				else if ( (synpred42_JPA2()) ) {
					alt35=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 35, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 102:
				{
				int LA35_18 = input.LA(2);
				if ( (synpred41_JPA2()) ) {
					alt35=3;
				}
				else if ( (synpred42_JPA2()) ) {
					alt35=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 35, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 119:
				{
				alt35=5;
				}
				break;
			case 115:
				{
				alt35=6;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 35, 0, input);
				throw nvae;
			}
			switch (alt35) {
				case 1 :
					// JPA2.g:169:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_select_expression1353);
					path_expression97=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression97.getTree());

					}
					break;
				case 2 :
					// JPA2.g:170:7: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_select_expression1361);
					identification_variable98=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable98.getTree());
					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 170:31: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
					{
						// JPA2.g:170:34: ^( T_SELECTED_ENTITY[$identification_variable.text] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_ENTITY, (identification_variable98!=null?input.toString(identification_variable98.start,identification_variable98.stop):null)), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// JPA2.g:171:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_select_expression1379);
					scalar_expression99=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression99.getTree());

					}
					break;
				case 4 :
					// JPA2.g:172:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_select_expression1387);
					aggregate_expression100=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression100.getTree());

					}
					break;
				case 5 :
					// JPA2.g:173:7: 'OBJECT' '(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal101=(Token)match(input,119,FOLLOW_119_in_select_expression1395); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal101_tree = (Object)adaptor.create(string_literal101);
					adaptor.addChild(root_0, string_literal101_tree);
					}

					char_literal102=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression1397); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal102_tree = (Object)adaptor.create(char_literal102);
					adaptor.addChild(root_0, char_literal102_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_select_expression1398);
					identification_variable103=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable103.getTree());

					char_literal104=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression1399); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal104_tree = (Object)adaptor.create(char_literal104);
					adaptor.addChild(root_0, char_literal104_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:174:7: constructor_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_constructor_expression_in_select_expression1407);
					constructor_expression105=constructor_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_expression105.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "select_expression"


	public static class constructor_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "constructor_expression"
	// JPA2.g:175:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
	public final JPA2Parser.constructor_expression_return constructor_expression() throws RecognitionException {
		JPA2Parser.constructor_expression_return retval = new JPA2Parser.constructor_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal106=null;
		Token char_literal108=null;
		Token char_literal110=null;
		Token char_literal112=null;
		ParserRuleReturnScope constructor_name107 =null;
		ParserRuleReturnScope constructor_item109 =null;
		ParserRuleReturnScope constructor_item111 =null;

		Object string_literal106_tree=null;
		Object char_literal108_tree=null;
		Object char_literal110_tree=null;
		Object char_literal112_tree=null;

		try {
			// JPA2.g:176:5: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
			// JPA2.g:176:7: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal106=(Token)match(input,115,FOLLOW_115_in_constructor_expression1418); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal106_tree = (Object)adaptor.create(string_literal106);
			adaptor.addChild(root_0, string_literal106_tree);
			}

			pushFollow(FOLLOW_constructor_name_in_constructor_expression1420);
			constructor_name107=constructor_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name107.getTree());

			char_literal108=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression1422); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal108_tree = (Object)adaptor.create(char_literal108);
			adaptor.addChild(root_0, char_literal108_tree);
			}

			pushFollow(FOLLOW_constructor_item_in_constructor_expression1424);
			constructor_item109=constructor_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item109.getTree());

			// JPA2.g:176:51: ( ',' constructor_item )*
			loop36:
			while (true) {
				int alt36=2;
				int LA36_0 = input.LA(1);
				if ( (LA36_0==60) ) {
					alt36=1;
				}

				switch (alt36) {
				case 1 :
					// JPA2.g:176:52: ',' constructor_item
					{
					char_literal110=(Token)match(input,60,FOLLOW_60_in_constructor_expression1427); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal110_tree = (Object)adaptor.create(char_literal110);
					adaptor.addChild(root_0, char_literal110_tree);
					}

					pushFollow(FOLLOW_constructor_item_in_constructor_expression1429);
					constructor_item111=constructor_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item111.getTree());

					}
					break;

				default :
					break loop36;
				}
			}

			char_literal112=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression1433); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal112_tree = (Object)adaptor.create(char_literal112);
			adaptor.addChild(root_0, char_literal112_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "constructor_expression"


	public static class constructor_item_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "constructor_item"
	// JPA2.g:177:1: constructor_item : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.constructor_item_return constructor_item() throws RecognitionException {
		JPA2Parser.constructor_item_return retval = new JPA2Parser.constructor_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression113 =null;
		ParserRuleReturnScope scalar_expression114 =null;
		ParserRuleReturnScope aggregate_expression115 =null;
		ParserRuleReturnScope identification_variable116 =null;


		try {
			// JPA2.g:178:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt37=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA37_1 = input.LA(2);
				if ( (synpred45_JPA2()) ) {
					alt37=1;
				}
				else if ( (synpred46_JPA2()) ) {
					alt37=2;
				}
				else if ( (true) ) {
					alt37=4;
				}

				}
				break;
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 57:
			case 59:
			case 61:
			case 64:
			case 71:
			case 76:
			case 78:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 100:
			case 104:
			case 108:
			case 110:
			case 113:
			case 118:
			case 126:
			case 128:
			case 129:
			case 133:
			case 134:
			case 136:
			case 142:
			case 143:
				{
				alt37=2;
				}
				break;
			case COUNT:
				{
				int LA37_16 = input.LA(2);
				if ( (synpred46_JPA2()) ) {
					alt37=2;
				}
				else if ( (synpred47_JPA2()) ) {
					alt37=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 37, 16, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA37_17 = input.LA(2);
				if ( (synpred46_JPA2()) ) {
					alt37=2;
				}
				else if ( (synpred47_JPA2()) ) {
					alt37=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 37, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 102:
				{
				int LA37_18 = input.LA(2);
				if ( (synpred46_JPA2()) ) {
					alt37=2;
				}
				else if ( (synpred47_JPA2()) ) {
					alt37=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 37, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

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
					// JPA2.g:178:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_constructor_item1444);
					path_expression113=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression113.getTree());

					}
					break;
				case 2 :
					// JPA2.g:179:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_constructor_item1452);
					scalar_expression114=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression114.getTree());

					}
					break;
				case 3 :
					// JPA2.g:180:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_constructor_item1460);
					aggregate_expression115=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression115.getTree());

					}
					break;
				case 4 :
					// JPA2.g:181:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_constructor_item1468);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "constructor_item"


	public static class aggregate_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "aggregate_expression"
	// JPA2.g:182:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );
	public final JPA2Parser.aggregate_expression_return aggregate_expression() throws RecognitionException {
		JPA2Parser.aggregate_expression_return retval = new JPA2Parser.aggregate_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal118=null;
		Token DISTINCT119=null;
		Token char_literal121=null;
		Token string_literal122=null;
		Token char_literal123=null;
		Token DISTINCT124=null;
		Token char_literal126=null;
		ParserRuleReturnScope aggregate_expression_function_name117 =null;
		ParserRuleReturnScope path_expression120 =null;
		ParserRuleReturnScope count_argument125 =null;
		ParserRuleReturnScope function_invocation127 =null;

		Object char_literal118_tree=null;
		Object DISTINCT119_tree=null;
		Object char_literal121_tree=null;
		Object string_literal122_tree=null;
		Object char_literal123_tree=null;
		Object DISTINCT124_tree=null;
		Object char_literal126_tree=null;
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_COUNT=new RewriteRuleTokenStream(adaptor,"token COUNT");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_count_argument=new RewriteRuleSubtreeStream(adaptor,"rule count_argument");
		RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
		RewriteRuleSubtreeStream stream_aggregate_expression_function_name=new RewriteRuleSubtreeStream(adaptor,"rule aggregate_expression_function_name");

		try {
			// JPA2.g:183:5: ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation )
			int alt40=3;
			alt40 = dfa40.predict(input);
			switch (alt40) {
				case 1 :
					// JPA2.g:183:7: aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')'
					{
					pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1479);
					aggregate_expression_function_name117=aggregate_expression_function_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name117.getTree());
					char_literal118=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1481); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal118);

					// JPA2.g:183:45: ( DISTINCT )?
					int alt38=2;
					int LA38_0 = input.LA(1);
					if ( (LA38_0==DISTINCT) ) {
						alt38=1;
					}
					switch (alt38) {
						case 1 :
							// JPA2.g:183:46: DISTINCT
							{
							DISTINCT119=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1483); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT119);

							}
							break;

					}

					pushFollow(FOLLOW_path_expression_in_aggregate_expression1487);
					path_expression120=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_path_expression.add(path_expression120.getTree());
					char_literal121=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1488); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal121);

					// AST REWRITE
					// elements: path_expression, LPAREN, DISTINCT, aggregate_expression_function_name, RPAREN
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 184:5: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
					{
						// JPA2.g:184:8: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:184:93: ( 'DISTINCT' )?
						if ( stream_DISTINCT.hasNext() ) {
							adaptor.addChild(root_1, (Object)adaptor.create(DISTINCT, "DISTINCT"));
						}
						stream_DISTINCT.reset();

						adaptor.addChild(root_1, stream_path_expression.nextTree());
						adaptor.addChild(root_1, stream_RPAREN.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// JPA2.g:185:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
					{
					string_literal122=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1522); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COUNT.add(string_literal122);

					char_literal123=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1524); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal123);

					// JPA2.g:185:18: ( DISTINCT )?
					int alt39=2;
					int LA39_0 = input.LA(1);
					if ( (LA39_0==DISTINCT) ) {
						alt39=1;
					}
					switch (alt39) {
						case 1 :
							// JPA2.g:185:19: DISTINCT
							{
							DISTINCT124=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1526); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT124);

							}
							break;

					}

					pushFollow(FOLLOW_count_argument_in_aggregate_expression1530);
					count_argument125=count_argument();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_count_argument.add(count_argument125.getTree());
					char_literal126=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1532); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal126);

					// AST REWRITE
					// elements: LPAREN, count_argument, RPAREN, DISTINCT, COUNT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 186:5: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
					{
						// JPA2.g:186:8: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_COUNT.nextNode());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:186:66: ( 'DISTINCT' )?
						if ( stream_DISTINCT.hasNext() ) {
							adaptor.addChild(root_1, (Object)adaptor.create(DISTINCT, "DISTINCT"));
						}
						stream_DISTINCT.reset();

						adaptor.addChild(root_1, stream_count_argument.nextTree());
						adaptor.addChild(root_1, stream_RPAREN.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// JPA2.g:187:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_aggregate_expression1567);
					function_invocation127=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation127.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "aggregate_expression"


	public static class aggregate_expression_function_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "aggregate_expression_function_name"
	// JPA2.g:188:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
	public final JPA2Parser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
		JPA2Parser.aggregate_expression_function_name_return retval = new JPA2Parser.aggregate_expression_function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set128=null;

		Object set128_tree=null;

		try {
			// JPA2.g:189:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set128=input.LT(1);
			if ( input.LA(1)==AVG||input.LA(1)==COUNT||(input.LA(1) >= MAX && input.LA(1) <= MIN)||input.LA(1)==SUM ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set128));
				state.errorRecovery=false;
				state.failed=false;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "aggregate_expression_function_name"


	public static class count_argument_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "count_argument"
	// JPA2.g:190:1: count_argument : ( identification_variable | path_expression );
	public final JPA2Parser.count_argument_return count_argument() throws RecognitionException {
		JPA2Parser.count_argument_return retval = new JPA2Parser.count_argument_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable129 =null;
		ParserRuleReturnScope path_expression130 =null;


		try {
			// JPA2.g:191:5: ( identification_variable | path_expression )
			int alt41=2;
			int LA41_0 = input.LA(1);
			if ( (LA41_0==WORD) ) {
				int LA41_1 = input.LA(2);
				if ( (LA41_1==RPAREN) ) {
					alt41=1;
				}
				else if ( (LA41_1==62) ) {
					alt41=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 41, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 41, 0, input);
				throw nvae;
			}

			switch (alt41) {
				case 1 :
					// JPA2.g:191:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_count_argument1604);
					identification_variable129=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable129.getTree());

					}
					break;
				case 2 :
					// JPA2.g:191:33: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_count_argument1608);
					path_expression130=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression130.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "count_argument"


	public static class where_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "where_clause"
	// JPA2.g:192:1: where_clause : wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) ;
	public final JPA2Parser.where_clause_return where_clause() throws RecognitionException {
		JPA2Parser.where_clause_return retval = new JPA2Parser.where_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token wh=null;
		ParserRuleReturnScope conditional_expression131 =null;

		Object wh_tree=null;
		RewriteRuleTokenStream stream_140=new RewriteRuleTokenStream(adaptor,"token 140");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");

		try {
			// JPA2.g:193:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) )
			// JPA2.g:193:7: wh= 'WHERE' conditional_expression
			{
			wh=(Token)match(input,140,FOLLOW_140_in_where_clause1621); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_140.add(wh);

			pushFollow(FOLLOW_conditional_expression_in_where_clause1623);
			conditional_expression131=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression131.getTree());
			// AST REWRITE
			// elements: conditional_expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 193:40: -> ^( T_CONDITION[$wh] conditional_expression )
			{
				// JPA2.g:193:43: ^( T_CONDITION[$wh] conditional_expression )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new WhereNode(T_CONDITION, wh), root_1);
				adaptor.addChild(root_1, stream_conditional_expression.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "where_clause"


	public static class groupby_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "groupby_clause"
	// JPA2.g:194:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
	public final JPA2Parser.groupby_clause_return groupby_clause() throws RecognitionException {
		JPA2Parser.groupby_clause_return retval = new JPA2Parser.groupby_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal132=null;
		Token string_literal133=null;
		Token char_literal135=null;
		ParserRuleReturnScope groupby_item134 =null;
		ParserRuleReturnScope groupby_item136 =null;

		Object string_literal132_tree=null;
		Object string_literal133_tree=null;
		Object char_literal135_tree=null;
		RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");

		try {
			// JPA2.g:195:5: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
			// JPA2.g:195:7: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
			{
			string_literal132=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1645); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_GROUP.add(string_literal132);

			string_literal133=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1647); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal133);

			pushFollow(FOLLOW_groupby_item_in_groupby_clause1649);
			groupby_item134=groupby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item134.getTree());
			// JPA2.g:195:33: ( ',' groupby_item )*
			loop42:
			while (true) {
				int alt42=2;
				int LA42_0 = input.LA(1);
				if ( (LA42_0==60) ) {
					alt42=1;
				}

				switch (alt42) {
				case 1 :
					// JPA2.g:195:34: ',' groupby_item
					{
					char_literal135=(Token)match(input,60,FOLLOW_60_in_groupby_clause1652); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal135);

					pushFollow(FOLLOW_groupby_item_in_groupby_clause1654);
					groupby_item136=groupby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item136.getTree());
					}
					break;

				default :
					break loop42;
				}
			}

			// AST REWRITE
			// elements: GROUP, groupby_item, BY
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 196:5: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
			{
				// JPA2.g:196:8: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new GroupByNode(T_GROUP_BY), root_1);
				adaptor.addChild(root_1, stream_GROUP.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:196:49: ( groupby_item )*
				while ( stream_groupby_item.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_item.nextTree());
				}
				stream_groupby_item.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "groupby_clause"


	public static class groupby_item_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "groupby_item"
	// JPA2.g:197:1: groupby_item : ( path_expression | identification_variable );
	public final JPA2Parser.groupby_item_return groupby_item() throws RecognitionException {
		JPA2Parser.groupby_item_return retval = new JPA2Parser.groupby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression137 =null;
		ParserRuleReturnScope identification_variable138 =null;


		try {
			// JPA2.g:198:5: ( path_expression | identification_variable )
			int alt43=2;
			int LA43_0 = input.LA(1);
			if ( (LA43_0==WORD) ) {
				int LA43_1 = input.LA(2);
				if ( (LA43_1==62) ) {
					alt43=1;
				}
				else if ( (LA43_1==EOF||LA43_1==HAVING||LA43_1==ORDER||LA43_1==RPAREN||LA43_1==60) ) {
					alt43=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 43, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 43, 0, input);
				throw nvae;
			}

			switch (alt43) {
				case 1 :
					// JPA2.g:198:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_groupby_item1688);
					path_expression137=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression137.getTree());

					}
					break;
				case 2 :
					// JPA2.g:198:25: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_groupby_item1692);
					identification_variable138=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable138.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "groupby_item"


	public static class having_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "having_clause"
	// JPA2.g:199:1: having_clause : 'HAVING' conditional_expression ;
	public final JPA2Parser.having_clause_return having_clause() throws RecognitionException {
		JPA2Parser.having_clause_return retval = new JPA2Parser.having_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal139=null;
		ParserRuleReturnScope conditional_expression140 =null;

		Object string_literal139_tree=null;

		try {
			// JPA2.g:200:5: ( 'HAVING' conditional_expression )
			// JPA2.g:200:7: 'HAVING' conditional_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal139=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1703); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal139_tree = (Object)adaptor.create(string_literal139);
			adaptor.addChild(root_0, string_literal139_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_having_clause1705);
			conditional_expression140=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression140.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "having_clause"


	public static class orderby_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "orderby_clause"
	// JPA2.g:201:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
	public final JPA2Parser.orderby_clause_return orderby_clause() throws RecognitionException {
		JPA2Parser.orderby_clause_return retval = new JPA2Parser.orderby_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal141=null;
		Token string_literal142=null;
		Token char_literal144=null;
		ParserRuleReturnScope orderby_item143 =null;
		ParserRuleReturnScope orderby_item145 =null;

		Object string_literal141_tree=null;
		Object string_literal142_tree=null;
		Object char_literal144_tree=null;
		RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");

		try {
			// JPA2.g:202:5: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
			// JPA2.g:202:7: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
			{
			string_literal141=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1716); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ORDER.add(string_literal141);

			string_literal142=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1718); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal142);

			pushFollow(FOLLOW_orderby_item_in_orderby_clause1720);
			orderby_item143=orderby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item143.getTree());
			// JPA2.g:202:33: ( ',' orderby_item )*
			loop44:
			while (true) {
				int alt44=2;
				int LA44_0 = input.LA(1);
				if ( (LA44_0==60) ) {
					alt44=1;
				}

				switch (alt44) {
				case 1 :
					// JPA2.g:202:34: ',' orderby_item
					{
					char_literal144=(Token)match(input,60,FOLLOW_60_in_orderby_clause1723); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal144);

					pushFollow(FOLLOW_orderby_item_in_orderby_clause1725);
					orderby_item145=orderby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item145.getTree());
					}
					break;

				default :
					break loop44;
				}
			}

			// AST REWRITE
			// elements: orderby_item, ORDER, BY
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 203:5: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
			{
				// JPA2.g:203:8: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByNode(T_ORDER_BY), root_1);
				adaptor.addChild(root_1, stream_ORDER.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:203:49: ( orderby_item )*
				while ( stream_orderby_item.hasNext() ) {
					adaptor.addChild(root_1, stream_orderby_item.nextTree());
				}
				stream_orderby_item.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "orderby_clause"


	public static class orderby_item_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "orderby_item"
	// JPA2.g:204:1: orderby_item : orderby_variable ( sort )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ) ;
	public final JPA2Parser.orderby_item_return orderby_item() throws RecognitionException {
		JPA2Parser.orderby_item_return retval = new JPA2Parser.orderby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope orderby_variable146 =null;
		ParserRuleReturnScope sort147 =null;

		RewriteRuleSubtreeStream stream_orderby_variable=new RewriteRuleSubtreeStream(adaptor,"rule orderby_variable");
		RewriteRuleSubtreeStream stream_sort=new RewriteRuleSubtreeStream(adaptor,"rule sort");

		try {
			// JPA2.g:205:5: ( orderby_variable ( sort )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ) )
			// JPA2.g:205:7: orderby_variable ( sort )?
			{
			pushFollow(FOLLOW_orderby_variable_in_orderby_item1759);
			orderby_variable146=orderby_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable146.getTree());
			// JPA2.g:205:24: ( sort )?
			int alt45=2;
			int LA45_0 = input.LA(1);
			if ( (LA45_0==ASC||LA45_0==DESC) ) {
				alt45=1;
			}
			switch (alt45) {
				case 1 :
					// JPA2.g:205:24: sort
					{
					pushFollow(FOLLOW_sort_in_orderby_item1761);
					sort147=sort();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sort.add(sort147.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: orderby_variable, sort
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 206:6: -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? )
			{
				// JPA2.g:206:9: ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);
				adaptor.addChild(root_1, stream_orderby_variable.nextTree());
				// JPA2.g:206:65: ( sort )?
				if ( stream_sort.hasNext() ) {
					adaptor.addChild(root_1, stream_sort.nextTree());
				}
				stream_sort.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "orderby_item"


	public static class orderby_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "orderby_variable"
	// JPA2.g:207:1: orderby_variable : ( path_expression | general_identification_variable | result_variable | scalar_expression | aggregate_expression );
	public final JPA2Parser.orderby_variable_return orderby_variable() throws RecognitionException {
		JPA2Parser.orderby_variable_return retval = new JPA2Parser.orderby_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression148 =null;
		ParserRuleReturnScope general_identification_variable149 =null;
		ParserRuleReturnScope result_variable150 =null;
		ParserRuleReturnScope scalar_expression151 =null;
		ParserRuleReturnScope aggregate_expression152 =null;


		try {
			// JPA2.g:208:5: ( path_expression | general_identification_variable | result_variable | scalar_expression | aggregate_expression )
			int alt46=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA46_1 = input.LA(2);
				if ( (synpred61_JPA2()) ) {
					alt46=1;
				}
				else if ( (synpred62_JPA2()) ) {
					alt46=2;
				}
				else if ( (synpred63_JPA2()) ) {
					alt46=3;
				}
				else if ( (synpred64_JPA2()) ) {
					alt46=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 46, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 106:
			case 137:
				{
				alt46=2;
				}
				break;
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 57:
			case 59:
			case 61:
			case 64:
			case 71:
			case 76:
			case 78:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 100:
			case 104:
			case 108:
			case 110:
			case 113:
			case 118:
			case 126:
			case 128:
			case 129:
			case 133:
			case 134:
			case 136:
			case 142:
			case 143:
				{
				alt46=4;
				}
				break;
			case COUNT:
				{
				int LA46_18 = input.LA(2);
				if ( (synpred64_JPA2()) ) {
					alt46=4;
				}
				else if ( (true) ) {
					alt46=5;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA46_19 = input.LA(2);
				if ( (synpred64_JPA2()) ) {
					alt46=4;
				}
				else if ( (true) ) {
					alt46=5;
				}

				}
				break;
			case 102:
				{
				int LA46_20 = input.LA(2);
				if ( (synpred64_JPA2()) ) {
					alt46=4;
				}
				else if ( (true) ) {
					alt46=5;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 46, 0, input);
				throw nvae;
			}
			switch (alt46) {
				case 1 :
					// JPA2.g:208:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_orderby_variable1793);
					path_expression148=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression148.getTree());

					}
					break;
				case 2 :
					// JPA2.g:208:25: general_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_identification_variable_in_orderby_variable1797);
					general_identification_variable149=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable149.getTree());

					}
					break;
				case 3 :
					// JPA2.g:208:59: result_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_result_variable_in_orderby_variable1801);
					result_variable150=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable150.getTree());

					}
					break;
				case 4 :
					// JPA2.g:208:77: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_orderby_variable1805);
					scalar_expression151=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression151.getTree());

					}
					break;
				case 5 :
					// JPA2.g:208:97: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_orderby_variable1809);
					aggregate_expression152=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression152.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "orderby_variable"


	public static class sort_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "sort"
	// JPA2.g:209:1: sort : ( 'ASC' | 'DESC' ) ;
	public final JPA2Parser.sort_return sort() throws RecognitionException {
		JPA2Parser.sort_return retval = new JPA2Parser.sort_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set153=null;

		Object set153_tree=null;

		try {
			// JPA2.g:210:5: ( ( 'ASC' | 'DESC' ) )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set153=input.LT(1);
			if ( input.LA(1)==ASC||input.LA(1)==DESC ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set153));
				state.errorRecovery=false;
				state.failed=false;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "sort"


	public static class subquery_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "subquery"
	// JPA2.g:211:1: subquery : lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
	public final JPA2Parser.subquery_return subquery() throws RecognitionException {
		JPA2Parser.subquery_return retval = new JPA2Parser.subquery_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token lp=null;
		Token rp=null;
		Token string_literal154=null;
		ParserRuleReturnScope simple_select_clause155 =null;
		ParserRuleReturnScope subquery_from_clause156 =null;
		ParserRuleReturnScope where_clause157 =null;
		ParserRuleReturnScope groupby_clause158 =null;
		ParserRuleReturnScope having_clause159 =null;

		Object lp_tree=null;
		Object rp_tree=null;
		Object string_literal154_tree=null;
		RewriteRuleTokenStream stream_125=new RewriteRuleTokenStream(adaptor,"token 125");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
		RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");

		try {
			// JPA2.g:212:5: (lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
			// JPA2.g:212:7: lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
			{
			lp=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subquery1839); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(lp);

			string_literal154=(Token)match(input,125,FOLLOW_125_in_subquery1841); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_125.add(string_literal154);

			pushFollow(FOLLOW_simple_select_clause_in_subquery1843);
			simple_select_clause155=simple_select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause155.getTree());
			pushFollow(FOLLOW_subquery_from_clause_in_subquery1845);
			subquery_from_clause156=subquery_from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause156.getTree());
			// JPA2.g:212:65: ( where_clause )?
			int alt47=2;
			int LA47_0 = input.LA(1);
			if ( (LA47_0==140) ) {
				alt47=1;
			}
			switch (alt47) {
				case 1 :
					// JPA2.g:212:66: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_subquery1848);
					where_clause157=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause157.getTree());
					}
					break;

			}

			// JPA2.g:212:81: ( groupby_clause )?
			int alt48=2;
			int LA48_0 = input.LA(1);
			if ( (LA48_0==GROUP) ) {
				alt48=1;
			}
			switch (alt48) {
				case 1 :
					// JPA2.g:212:82: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_subquery1853);
					groupby_clause158=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause158.getTree());
					}
					break;

			}

			// JPA2.g:212:99: ( having_clause )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==HAVING) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// JPA2.g:212:100: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_subquery1858);
					having_clause159=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause159.getTree());
					}
					break;

			}

			rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1864); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(rp);

			// AST REWRITE
			// elements: simple_select_clause, 125, having_clause, groupby_clause, subquery_from_clause, where_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 213:6: -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
			{
				// JPA2.g:213:9: ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);
				adaptor.addChild(root_1, stream_125.nextNode());
				adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
				adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
				// JPA2.g:213:90: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:213:106: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:213:124: ( having_clause )?
				if ( stream_having_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_having_clause.nextTree());
				}
				stream_having_clause.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "subquery"


	public static class subquery_from_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "subquery_from_clause"
	// JPA2.g:214:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
	public final JPA2Parser.subquery_from_clause_return subquery_from_clause() throws RecognitionException {
		JPA2Parser.subquery_from_clause_return retval = new JPA2Parser.subquery_from_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token fr=null;
		Token char_literal161=null;
		ParserRuleReturnScope subselect_identification_variable_declaration160 =null;
		ParserRuleReturnScope subselect_identification_variable_declaration162 =null;

		Object fr_tree=null;
		Object char_literal161_tree=null;
		RewriteRuleTokenStream stream_101=new RewriteRuleTokenStream(adaptor,"token 101");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");

		try {
			// JPA2.g:215:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
			// JPA2.g:215:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
			{
			fr=(Token)match(input,101,FOLLOW_101_in_subquery_from_clause1914); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_101.add(fr);

			pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1916);
			subselect_identification_variable_declaration160=subselect_identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration160.getTree());
			// JPA2.g:215:63: ( ',' subselect_identification_variable_declaration )*
			loop50:
			while (true) {
				int alt50=2;
				int LA50_0 = input.LA(1);
				if ( (LA50_0==60) ) {
					alt50=1;
				}

				switch (alt50) {
				case 1 :
					// JPA2.g:215:64: ',' subselect_identification_variable_declaration
					{
					char_literal161=(Token)match(input,60,FOLLOW_60_in_subquery_from_clause1919); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal161);

					pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1921);
					subselect_identification_variable_declaration162=subselect_identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration162.getTree());
					}
					break;

				default :
					break loop50;
				}
			}

			// AST REWRITE
			// elements: subselect_identification_variable_declaration
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 216:5: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
			{
				// JPA2.g:216:8: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				// JPA2.g:216:35: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
				while ( stream_subselect_identification_variable_declaration.hasNext() ) {
					// JPA2.g:216:35: ^( T_SOURCE subselect_identification_variable_declaration )
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


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "subquery_from_clause"


	public static class subselect_identification_variable_declaration_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "subselect_identification_variable_declaration"
	// JPA2.g:218:1: subselect_identification_variable_declaration : ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration );
	public final JPA2Parser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration() throws RecognitionException {
		JPA2Parser.subselect_identification_variable_declaration_return retval = new JPA2Parser.subselect_identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal165=null;
		ParserRuleReturnScope identification_variable_declaration163 =null;
		ParserRuleReturnScope derived_path_expression164 =null;
		ParserRuleReturnScope identification_variable166 =null;
		ParserRuleReturnScope join167 =null;
		ParserRuleReturnScope derived_collection_member_declaration168 =null;

		Object string_literal165_tree=null;

		try {
			// JPA2.g:219:5: ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration )
			int alt52=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA52_1 = input.LA(2);
				if ( (LA52_1==WORD||LA52_1==81) ) {
					alt52=1;
				}
				else if ( (LA52_1==62) ) {
					alt52=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 52, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 132:
				{
				alt52=2;
				}
				break;
			case IN:
				{
				alt52=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 52, 0, input);
				throw nvae;
			}
			switch (alt52) {
				case 1 :
					// JPA2.g:219:7: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1959);
					identification_variable_declaration163=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration163.getTree());

					}
					break;
				case 2 :
					// JPA2.g:220:7: derived_path_expression 'AS' identification_variable ( join )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1967);
					derived_path_expression164=derived_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_path_expression164.getTree());

					string_literal165=(Token)match(input,81,FOLLOW_81_in_subselect_identification_variable_declaration1969); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal165_tree = (Object)adaptor.create(string_literal165);
					adaptor.addChild(root_0, string_literal165_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration1971);
					identification_variable166=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable166.getTree());

					// JPA2.g:220:60: ( join )*
					loop51:
					while (true) {
						int alt51=2;
						int LA51_0 = input.LA(1);
						if ( (LA51_0==INNER||(LA51_0 >= JOIN && LA51_0 <= LEFT)) ) {
							alt51=1;
						}

						switch (alt51) {
						case 1 :
							// JPA2.g:220:61: join
							{
							pushFollow(FOLLOW_join_in_subselect_identification_variable_declaration1974);
							join167=join();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, join167.getTree());

							}
							break;

						default :
							break loop51;
						}
					}

					}
					break;
				case 3 :
					// JPA2.g:221:7: derived_collection_member_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1984);
					derived_collection_member_declaration168=derived_collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_collection_member_declaration168.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "subselect_identification_variable_declaration"


	public static class derived_path_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "derived_path_expression"
	// JPA2.g:222:1: derived_path_expression : ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field );
	public final JPA2Parser.derived_path_expression_return derived_path_expression() throws RecognitionException {
		JPA2Parser.derived_path_expression_return retval = new JPA2Parser.derived_path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal170=null;
		Token char_literal173=null;
		ParserRuleReturnScope general_derived_path169 =null;
		ParserRuleReturnScope single_valued_object_field171 =null;
		ParserRuleReturnScope general_derived_path172 =null;
		ParserRuleReturnScope collection_valued_field174 =null;

		Object char_literal170_tree=null;
		Object char_literal173_tree=null;

		try {
			// JPA2.g:223:5: ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field )
			int alt53=2;
			int LA53_0 = input.LA(1);
			if ( (LA53_0==WORD) ) {
				int LA53_1 = input.LA(2);
				if ( (synpred73_JPA2()) ) {
					alt53=1;
				}
				else if ( (true) ) {
					alt53=2;
				}

			}
			else if ( (LA53_0==132) ) {
				int LA53_2 = input.LA(2);
				if ( (synpred73_JPA2()) ) {
					alt53=1;
				}
				else if ( (true) ) {
					alt53=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 53, 0, input);
				throw nvae;
			}

			switch (alt53) {
				case 1 :
					// JPA2.g:223:7: general_derived_path '.' single_valued_object_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1995);
					general_derived_path169=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path169.getTree());

					char_literal170=(Token)match(input,62,FOLLOW_62_in_derived_path_expression1996); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal170_tree = (Object)adaptor.create(char_literal170);
					adaptor.addChild(root_0, char_literal170_tree);
					}

					pushFollow(FOLLOW_single_valued_object_field_in_derived_path_expression1997);
					single_valued_object_field171=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field171.getTree());

					}
					break;
				case 2 :
					// JPA2.g:224:7: general_derived_path '.' collection_valued_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression2005);
					general_derived_path172=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path172.getTree());

					char_literal173=(Token)match(input,62,FOLLOW_62_in_derived_path_expression2006); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal173_tree = (Object)adaptor.create(char_literal173);
					adaptor.addChild(root_0, char_literal173_tree);
					}

					pushFollow(FOLLOW_collection_valued_field_in_derived_path_expression2007);
					collection_valued_field174=collection_valued_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field174.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "derived_path_expression"


	public static class general_derived_path_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "general_derived_path"
	// JPA2.g:225:1: general_derived_path : ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* );
	public final JPA2Parser.general_derived_path_return general_derived_path() throws RecognitionException {
		JPA2Parser.general_derived_path_return retval = new JPA2Parser.general_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal177=null;
		ParserRuleReturnScope simple_derived_path175 =null;
		ParserRuleReturnScope treated_derived_path176 =null;
		ParserRuleReturnScope single_valued_object_field178 =null;

		Object char_literal177_tree=null;

		try {
			// JPA2.g:226:5: ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* )
			int alt55=2;
			int LA55_0 = input.LA(1);
			if ( (LA55_0==WORD) ) {
				alt55=1;
			}
			else if ( (LA55_0==132) ) {
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
					// JPA2.g:226:7: simple_derived_path
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_derived_path_in_general_derived_path2018);
					simple_derived_path175=simple_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_derived_path175.getTree());

					}
					break;
				case 2 :
					// JPA2.g:227:7: treated_derived_path ( '.' single_valued_object_field )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_treated_derived_path_in_general_derived_path2026);
					treated_derived_path176=treated_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, treated_derived_path176.getTree());

					// JPA2.g:227:27: ( '.' single_valued_object_field )*
					loop54:
					while (true) {
						int alt54=2;
						int LA54_0 = input.LA(1);
						if ( (LA54_0==62) ) {
							int LA54_1 = input.LA(2);
							if ( (LA54_1==WORD) ) {
								int LA54_3 = input.LA(3);
								if ( (LA54_3==81) ) {
									int LA54_4 = input.LA(4);
									if ( (LA54_4==WORD) ) {
										int LA54_6 = input.LA(5);
										if ( (LA54_6==RPAREN) ) {
											int LA54_7 = input.LA(6);
											if ( (LA54_7==81) ) {
												int LA54_8 = input.LA(7);
												if ( (LA54_8==WORD) ) {
													int LA54_9 = input.LA(8);
													if ( (LA54_9==RPAREN) ) {
														alt54=1;
													}

												}

											}
											else if ( (LA54_7==62) ) {
												alt54=1;
											}

										}

									}

								}
								else if ( (LA54_3==62) ) {
									alt54=1;
								}

							}

						}

						switch (alt54) {
						case 1 :
							// JPA2.g:227:28: '.' single_valued_object_field
							{
							char_literal177=(Token)match(input,62,FOLLOW_62_in_general_derived_path2028); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal177_tree = (Object)adaptor.create(char_literal177);
							adaptor.addChild(root_0, char_literal177_tree);
							}

							pushFollow(FOLLOW_single_valued_object_field_in_general_derived_path2029);
							single_valued_object_field178=single_valued_object_field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field178.getTree());

							}
							break;

						default :
							break loop54;
						}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "general_derived_path"


	public static class simple_derived_path_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_derived_path"
	// JPA2.g:229:1: simple_derived_path : superquery_identification_variable ;
	public final JPA2Parser.simple_derived_path_return simple_derived_path() throws RecognitionException {
		JPA2Parser.simple_derived_path_return retval = new JPA2Parser.simple_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope superquery_identification_variable179 =null;


		try {
			// JPA2.g:230:5: ( superquery_identification_variable )
			// JPA2.g:230:7: superquery_identification_variable
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_superquery_identification_variable_in_simple_derived_path2047);
			superquery_identification_variable179=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable179.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "simple_derived_path"


	public static class treated_derived_path_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "treated_derived_path"
	// JPA2.g:232:1: treated_derived_path : 'TREAT(' general_derived_path 'AS' subtype ')' ;
	public final JPA2Parser.treated_derived_path_return treated_derived_path() throws RecognitionException {
		JPA2Parser.treated_derived_path_return retval = new JPA2Parser.treated_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal180=null;
		Token string_literal182=null;
		Token char_literal184=null;
		ParserRuleReturnScope general_derived_path181 =null;
		ParserRuleReturnScope subtype183 =null;

		Object string_literal180_tree=null;
		Object string_literal182_tree=null;
		Object char_literal184_tree=null;

		try {
			// JPA2.g:233:5: ( 'TREAT(' general_derived_path 'AS' subtype ')' )
			// JPA2.g:233:7: 'TREAT(' general_derived_path 'AS' subtype ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal180=(Token)match(input,132,FOLLOW_132_in_treated_derived_path2064); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal180_tree = (Object)adaptor.create(string_literal180);
			adaptor.addChild(root_0, string_literal180_tree);
			}

			pushFollow(FOLLOW_general_derived_path_in_treated_derived_path2065);
			general_derived_path181=general_derived_path();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path181.getTree());

			string_literal182=(Token)match(input,81,FOLLOW_81_in_treated_derived_path2067); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal182_tree = (Object)adaptor.create(string_literal182);
			adaptor.addChild(root_0, string_literal182_tree);
			}

			pushFollow(FOLLOW_subtype_in_treated_derived_path2069);
			subtype183=subtype();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subtype183.getTree());

			char_literal184=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_treated_derived_path2071); if (state.failed) return retval;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "treated_derived_path"


	public static class derived_collection_member_declaration_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "derived_collection_member_declaration"
	// JPA2.g:234:1: derived_collection_member_declaration : 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field ;
	public final JPA2Parser.derived_collection_member_declaration_return derived_collection_member_declaration() throws RecognitionException {
		JPA2Parser.derived_collection_member_declaration_return retval = new JPA2Parser.derived_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal185=null;
		Token char_literal187=null;
		Token char_literal189=null;
		ParserRuleReturnScope superquery_identification_variable186 =null;
		ParserRuleReturnScope single_valued_object_field188 =null;
		ParserRuleReturnScope collection_valued_field190 =null;

		Object string_literal185_tree=null;
		Object char_literal187_tree=null;
		Object char_literal189_tree=null;

		try {
			// JPA2.g:235:5: ( 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field )
			// JPA2.g:235:7: 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field
			{
			root_0 = (Object)adaptor.nil();


			string_literal185=(Token)match(input,IN,FOLLOW_IN_in_derived_collection_member_declaration2082); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal185_tree = (Object)adaptor.create(string_literal185);
			adaptor.addChild(root_0, string_literal185_tree);
			}

			pushFollow(FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2084);
			superquery_identification_variable186=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable186.getTree());

			char_literal187=(Token)match(input,62,FOLLOW_62_in_derived_collection_member_declaration2085); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal187_tree = (Object)adaptor.create(char_literal187);
			adaptor.addChild(root_0, char_literal187_tree);
			}

			// JPA2.g:235:49: ( single_valued_object_field '.' )*
			loop56:
			while (true) {
				int alt56=2;
				int LA56_0 = input.LA(1);
				if ( (LA56_0==WORD) ) {
					int LA56_1 = input.LA(2);
					if ( (LA56_1==62) ) {
						alt56=1;
					}

				}

				switch (alt56) {
				case 1 :
					// JPA2.g:235:50: single_valued_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2087);
					single_valued_object_field188=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field188.getTree());

					char_literal189=(Token)match(input,62,FOLLOW_62_in_derived_collection_member_declaration2089); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal189_tree = (Object)adaptor.create(char_literal189);
					adaptor.addChild(root_0, char_literal189_tree);
					}

					}
					break;

				default :
					break loop56;
				}
			}

			pushFollow(FOLLOW_collection_valued_field_in_derived_collection_member_declaration2092);
			collection_valued_field190=collection_valued_field();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field190.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "derived_collection_member_declaration"


	public static class simple_select_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_select_clause"
	// JPA2.g:237:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
	public final JPA2Parser.simple_select_clause_return simple_select_clause() throws RecognitionException {
		JPA2Parser.simple_select_clause_return retval = new JPA2Parser.simple_select_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal191=null;
		ParserRuleReturnScope simple_select_expression192 =null;

		Object string_literal191_tree=null;
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleSubtreeStream stream_simple_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_expression");

		try {
			// JPA2.g:238:5: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
			// JPA2.g:238:7: ( 'DISTINCT' )? simple_select_expression
			{
			// JPA2.g:238:7: ( 'DISTINCT' )?
			int alt57=2;
			int LA57_0 = input.LA(1);
			if ( (LA57_0==DISTINCT) ) {
				alt57=1;
			}
			switch (alt57) {
				case 1 :
					// JPA2.g:238:8: 'DISTINCT'
					{
					string_literal191=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause2105); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal191);

					}
					break;

			}

			pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause2109);
			simple_select_expression192=simple_select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_expression.add(simple_select_expression192.getTree());
			// AST REWRITE
			// elements: simple_select_expression, DISTINCT
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 239:5: -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
			{
				// JPA2.g:239:8: ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:239:48: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);
				// JPA2.g:239:86: ( 'DISTINCT' )?
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


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "simple_select_clause"


	public static class simple_select_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_select_expression"
	// JPA2.g:240:1: simple_select_expression : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.simple_select_expression_return simple_select_expression() throws RecognitionException {
		JPA2Parser.simple_select_expression_return retval = new JPA2Parser.simple_select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression193 =null;
		ParserRuleReturnScope scalar_expression194 =null;
		ParserRuleReturnScope aggregate_expression195 =null;
		ParserRuleReturnScope identification_variable196 =null;


		try {
			// JPA2.g:241:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt58=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA58_1 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt58=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt58=2;
				}
				else if ( (true) ) {
					alt58=4;
				}

				}
				break;
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 57:
			case 59:
			case 61:
			case 64:
			case 71:
			case 76:
			case 78:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 100:
			case 104:
			case 108:
			case 110:
			case 113:
			case 118:
			case 126:
			case 128:
			case 129:
			case 133:
			case 134:
			case 136:
			case 142:
			case 143:
				{
				alt58=2;
				}
				break;
			case COUNT:
				{
				int LA58_16 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt58=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt58=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 58, 16, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA58_17 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt58=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt58=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 58, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 102:
				{
				int LA58_18 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt58=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt58=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 58, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 58, 0, input);
				throw nvae;
			}
			switch (alt58) {
				case 1 :
					// JPA2.g:241:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_simple_select_expression2149);
					path_expression193=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression193.getTree());

					}
					break;
				case 2 :
					// JPA2.g:242:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_simple_select_expression2157);
					scalar_expression194=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression194.getTree());

					}
					break;
				case 3 :
					// JPA2.g:243:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression2165);
					aggregate_expression195=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression195.getTree());

					}
					break;
				case 4 :
					// JPA2.g:244:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_select_expression2173);
					identification_variable196=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable196.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "simple_select_expression"


	public static class scalar_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "scalar_expression"
	// JPA2.g:245:1: scalar_expression : ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression );
	public final JPA2Parser.scalar_expression_return scalar_expression() throws RecognitionException {
		JPA2Parser.scalar_expression_return retval = new JPA2Parser.scalar_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope arithmetic_expression197 =null;
		ParserRuleReturnScope string_expression198 =null;
		ParserRuleReturnScope enum_expression199 =null;
		ParserRuleReturnScope datetime_expression200 =null;
		ParserRuleReturnScope boolean_expression201 =null;
		ParserRuleReturnScope case_expression202 =null;
		ParserRuleReturnScope entity_type_expression203 =null;


		try {
			// JPA2.g:246:5: ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression )
			int alt59=7;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 59:
			case 61:
			case 64:
			case 78:
			case 104:
			case 108:
			case 110:
			case 113:
			case 126:
			case 128:
				{
				alt59=1;
				}
				break;
			case WORD:
				{
				int LA59_2 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}
				else if ( (true) ) {
					alt59=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA59_5 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 71:
				{
				int LA59_6 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}
				else if ( (true) ) {
					alt59=7;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA59_7 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}
				else if ( (true) ) {
					alt59=7;
				}

				}
				break;
			case 57:
				{
				int LA59_8 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}
				else if ( (true) ) {
					alt59=7;
				}

				}
				break;
			case COUNT:
				{
				int LA59_16 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 16, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA59_17 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 102:
				{
				int LA59_18 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 84:
				{
				int LA59_19 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}
				else if ( (synpred86_JPA2()) ) {
					alt59=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 19, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 86:
				{
				int LA59_20 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}
				else if ( (synpred86_JPA2()) ) {
					alt59=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 20, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 118:
				{
				int LA59_21 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}
				else if ( (synpred86_JPA2()) ) {
					alt59=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 21, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 85:
				{
				int LA59_22 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 22, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 100:
				{
				int LA59_23 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 23, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 76:
				{
				int LA59_24 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred84_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt59=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 59, 24, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 87:
			case 129:
			case 133:
			case 136:
				{
				alt59=2;
				}
				break;
			case 88:
			case 89:
			case 90:
				{
				alt59=4;
				}
				break;
			case 142:
			case 143:
				{
				alt59=5;
				}
				break;
			case 134:
				{
				alt59=7;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 59, 0, input);
				throw nvae;
			}
			switch (alt59) {
				case 1 :
					// JPA2.g:246:7: arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_scalar_expression2184);
					arithmetic_expression197=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression197.getTree());

					}
					break;
				case 2 :
					// JPA2.g:247:7: string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_scalar_expression2192);
					string_expression198=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression198.getTree());

					}
					break;
				case 3 :
					// JPA2.g:248:7: enum_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_scalar_expression2200);
					enum_expression199=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression199.getTree());

					}
					break;
				case 4 :
					// JPA2.g:249:7: datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_scalar_expression2208);
					datetime_expression200=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression200.getTree());

					}
					break;
				case 5 :
					// JPA2.g:250:7: boolean_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_scalar_expression2216);
					boolean_expression201=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression201.getTree());

					}
					break;
				case 6 :
					// JPA2.g:251:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_scalar_expression2224);
					case_expression202=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression202.getTree());

					}
					break;
				case 7 :
					// JPA2.g:252:7: entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_scalar_expression2232);
					entity_type_expression203=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression203.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "scalar_expression"


	public static class conditional_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "conditional_expression"
	// JPA2.g:253:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
	public final JPA2Parser.conditional_expression_return conditional_expression() throws RecognitionException {
		JPA2Parser.conditional_expression_return retval = new JPA2Parser.conditional_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal205=null;
		ParserRuleReturnScope conditional_term204 =null;
		ParserRuleReturnScope conditional_term206 =null;

		Object string_literal205_tree=null;

		try {
			// JPA2.g:254:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
			// JPA2.g:254:7: ( conditional_term ) ( 'OR' conditional_term )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:254:7: ( conditional_term )
			// JPA2.g:254:8: conditional_term
			{
			pushFollow(FOLLOW_conditional_term_in_conditional_expression2244);
			conditional_term204=conditional_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term204.getTree());

			}

			// JPA2.g:254:26: ( 'OR' conditional_term )*
			loop60:
			while (true) {
				int alt60=2;
				int LA60_0 = input.LA(1);
				if ( (LA60_0==OR) ) {
					alt60=1;
				}

				switch (alt60) {
				case 1 :
					// JPA2.g:254:27: 'OR' conditional_term
					{
					string_literal205=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression2248); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal205_tree = (Object)adaptor.create(string_literal205);
					adaptor.addChild(root_0, string_literal205_tree);
					}

					pushFollow(FOLLOW_conditional_term_in_conditional_expression2250);
					conditional_term206=conditional_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term206.getTree());

					}
					break;

				default :
					break loop60;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "conditional_expression"


	public static class conditional_term_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "conditional_term"
	// JPA2.g:255:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
	public final JPA2Parser.conditional_term_return conditional_term() throws RecognitionException {
		JPA2Parser.conditional_term_return retval = new JPA2Parser.conditional_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal208=null;
		ParserRuleReturnScope conditional_factor207 =null;
		ParserRuleReturnScope conditional_factor209 =null;

		Object string_literal208_tree=null;

		try {
			// JPA2.g:256:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
			// JPA2.g:256:7: ( conditional_factor ) ( 'AND' conditional_factor )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:256:7: ( conditional_factor )
			// JPA2.g:256:8: conditional_factor
			{
			pushFollow(FOLLOW_conditional_factor_in_conditional_term2264);
			conditional_factor207=conditional_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor207.getTree());

			}

			// JPA2.g:256:28: ( 'AND' conditional_factor )*
			loop61:
			while (true) {
				int alt61=2;
				int LA61_0 = input.LA(1);
				if ( (LA61_0==AND) ) {
					alt61=1;
				}

				switch (alt61) {
				case 1 :
					// JPA2.g:256:29: 'AND' conditional_factor
					{
					string_literal208=(Token)match(input,AND,FOLLOW_AND_in_conditional_term2268); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal208_tree = (Object)adaptor.create(string_literal208);
					adaptor.addChild(root_0, string_literal208_tree);
					}

					pushFollow(FOLLOW_conditional_factor_in_conditional_term2270);
					conditional_factor209=conditional_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor209.getTree());

					}
					break;

				default :
					break loop61;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "conditional_term"


	public static class conditional_factor_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "conditional_factor"
	// JPA2.g:257:1: conditional_factor : ( 'NOT' )? conditional_primary ;
	public final JPA2Parser.conditional_factor_return conditional_factor() throws RecognitionException {
		JPA2Parser.conditional_factor_return retval = new JPA2Parser.conditional_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal210=null;
		ParserRuleReturnScope conditional_primary211 =null;

		Object string_literal210_tree=null;

		try {
			// JPA2.g:258:5: ( ( 'NOT' )? conditional_primary )
			// JPA2.g:258:7: ( 'NOT' )? conditional_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:258:7: ( 'NOT' )?
			int alt62=2;
			int LA62_0 = input.LA(1);
			if ( (LA62_0==NOT) ) {
				int LA62_1 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt62=1;
				}
			}
			switch (alt62) {
				case 1 :
					// JPA2.g:258:8: 'NOT'
					{
					string_literal210=(Token)match(input,NOT,FOLLOW_NOT_in_conditional_factor2284); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal210_tree = (Object)adaptor.create(string_literal210);
					adaptor.addChild(root_0, string_literal210_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_conditional_primary_in_conditional_factor2288);
			conditional_primary211=conditional_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_primary211.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "conditional_factor"


	public static class conditional_primary_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "conditional_primary"
	// JPA2.g:259:1: conditional_primary : ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' );
	public final JPA2Parser.conditional_primary_return conditional_primary() throws RecognitionException {
		JPA2Parser.conditional_primary_return retval = new JPA2Parser.conditional_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal213=null;
		Token char_literal215=null;
		ParserRuleReturnScope simple_cond_expression212 =null;
		ParserRuleReturnScope conditional_expression214 =null;

		Object char_literal213_tree=null;
		Object char_literal215_tree=null;
		RewriteRuleSubtreeStream stream_simple_cond_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_cond_expression");

		try {
			// JPA2.g:260:5: ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' )
			int alt63=2;
			int LA63_0 = input.LA(1);
			if ( (LA63_0==AVG||LA63_0==COUNT||LA63_0==INT_NUMERAL||LA63_0==LOWER||(LA63_0 >= MAX && LA63_0 <= NOT)||(LA63_0 >= STRING_LITERAL && LA63_0 <= SUM)||LA63_0==WORD||LA63_0==57||LA63_0==59||LA63_0==61||LA63_0==64||(LA63_0 >= 71 && LA63_0 <= 78)||(LA63_0 >= 84 && LA63_0 <= 90)||(LA63_0 >= 99 && LA63_0 <= 100)||LA63_0==102||LA63_0==104||LA63_0==108||LA63_0==110||LA63_0==113||LA63_0==118||LA63_0==126||(LA63_0 >= 128 && LA63_0 <= 129)||(LA63_0 >= 132 && LA63_0 <= 134)||LA63_0==136||(LA63_0 >= 142 && LA63_0 <= 143)) ) {
				alt63=1;
			}
			else if ( (LA63_0==LPAREN) ) {
				int LA63_20 = input.LA(2);
				if ( (synpred90_JPA2()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 63, 0, input);
				throw nvae;
			}

			switch (alt63) {
				case 1 :
					// JPA2.g:260:7: simple_cond_expression
					{
					pushFollow(FOLLOW_simple_cond_expression_in_conditional_primary2299);
					simple_cond_expression212=simple_cond_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_simple_cond_expression.add(simple_cond_expression212.getTree());
					// AST REWRITE
					// elements: simple_cond_expression
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 261:5: -> ^( T_SIMPLE_CONDITION[] simple_cond_expression )
					{
						// JPA2.g:261:8: ^( T_SIMPLE_CONDITION[] simple_cond_expression )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new SimpleConditionNode(T_SIMPLE_CONDITION), root_1);
						adaptor.addChild(root_1, stream_simple_cond_expression.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// JPA2.g:262:7: '(' conditional_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal213=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_primary2323); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal213_tree = (Object)adaptor.create(char_literal213);
					adaptor.addChild(root_0, char_literal213_tree);
					}

					pushFollow(FOLLOW_conditional_expression_in_conditional_primary2324);
					conditional_expression214=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression214.getTree());

					char_literal215=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_primary2325); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal215_tree = (Object)adaptor.create(char_literal215);
					adaptor.addChild(root_0, char_literal215_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "conditional_primary"


	public static class simple_cond_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_cond_expression"
	// JPA2.g:263:1: simple_cond_expression : ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
	public final JPA2Parser.simple_cond_expression_return simple_cond_expression() throws RecognitionException {
		JPA2Parser.simple_cond_expression_return retval = new JPA2Parser.simple_cond_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope comparison_expression216 =null;
		ParserRuleReturnScope between_expression217 =null;
		ParserRuleReturnScope in_expression218 =null;
		ParserRuleReturnScope like_expression219 =null;
		ParserRuleReturnScope null_comparison_expression220 =null;
		ParserRuleReturnScope empty_collection_comparison_expression221 =null;
		ParserRuleReturnScope collection_member_expression222 =null;
		ParserRuleReturnScope exists_expression223 =null;
		ParserRuleReturnScope date_macro_expression224 =null;


		try {
			// JPA2.g:264:5: ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
			int alt64=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA64_1 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred93_JPA2()) ) {
					alt64=3;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}
				else if ( (synpred95_JPA2()) ) {
					alt64=5;
				}
				else if ( (synpred96_JPA2()) ) {
					alt64=6;
				}
				else if ( (synpred97_JPA2()) ) {
					alt64=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case STRING_LITERAL:
				{
				int LA64_2 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 71:
				{
				int LA64_3 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}
				else if ( (synpred95_JPA2()) ) {
					alt64=5;
				}
				else if ( (synpred97_JPA2()) ) {
					alt64=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA64_4 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}
				else if ( (synpred95_JPA2()) ) {
					alt64=5;
				}
				else if ( (synpred97_JPA2()) ) {
					alt64=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 57:
				{
				int LA64_5 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}
				else if ( (synpred95_JPA2()) ) {
					alt64=5;
				}
				else if ( (synpred97_JPA2()) ) {
					alt64=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 87:
				{
				int LA64_6 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 129:
				{
				int LA64_7 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 7, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 133:
				{
				int LA64_8 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LOWER:
				{
				int LA64_9 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 9, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 136:
				{
				int LA64_10 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case COUNT:
				{
				int LA64_11 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 11, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA64_12 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 12, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 102:
				{
				int LA64_13 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 84:
				{
				int LA64_14 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 14, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 86:
				{
				int LA64_15 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 15, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 118:
				{
				int LA64_16 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 16, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 85:
				{
				int LA64_17 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 100:
				{
				int LA64_18 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 76:
				{
				int LA64_19 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 19, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LPAREN:
				{
				int LA64_20 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=4;
				}
				else if ( (synpred97_JPA2()) ) {
					alt64=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 20, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 142:
			case 143:
				{
				alt64=1;
				}
				break;
			case 88:
			case 89:
			case 90:
				{
				int LA64_22 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 22, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 134:
				{
				int LA64_23 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred93_JPA2()) ) {
					alt64=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 23, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 59:
			case 61:
				{
				int LA64_24 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 24, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 64:
				{
				int LA64_25 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 25, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA64_26 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 26, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 108:
				{
				int LA64_27 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 27, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 110:
				{
				int LA64_28 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 28, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 78:
				{
				int LA64_29 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 29, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 128:
				{
				int LA64_30 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 30, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 113:
				{
				int LA64_31 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 31, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 126:
				{
				int LA64_32 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 32, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 104:
				{
				int LA64_33 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 33, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 132:
				{
				alt64=5;
				}
				break;
			case NOT:
			case 99:
				{
				alt64=8;
				}
				break;
			case 72:
			case 73:
			case 74:
			case 75:
			case 77:
				{
				alt64=9;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 64, 0, input);
				throw nvae;
			}
			switch (alt64) {
				case 1 :
					// JPA2.g:264:7: comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression2336);
					comparison_expression216=comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression216.getTree());

					}
					break;
				case 2 :
					// JPA2.g:265:7: between_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_between_expression_in_simple_cond_expression2344);
					between_expression217=between_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression217.getTree());

					}
					break;
				case 3 :
					// JPA2.g:266:7: in_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_in_expression_in_simple_cond_expression2352);
					in_expression218=in_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression218.getTree());

					}
					break;
				case 4 :
					// JPA2.g:267:7: like_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_like_expression_in_simple_cond_expression2360);
					like_expression219=like_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression219.getTree());

					}
					break;
				case 5 :
					// JPA2.g:268:7: null_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression2368);
					null_comparison_expression220=null_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression220.getTree());

					}
					break;
				case 6 :
					// JPA2.g:269:7: empty_collection_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2376);
					empty_collection_comparison_expression221=empty_collection_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression221.getTree());

					}
					break;
				case 7 :
					// JPA2.g:270:7: collection_member_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression2384);
					collection_member_expression222=collection_member_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression222.getTree());

					}
					break;
				case 8 :
					// JPA2.g:271:7: exists_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exists_expression_in_simple_cond_expression2392);
					exists_expression223=exists_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression223.getTree());

					}
					break;
				case 9 :
					// JPA2.g:272:7: date_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression2400);
					date_macro_expression224=date_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_macro_expression224.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "simple_cond_expression"


	public static class date_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_macro_expression"
	// JPA2.g:275:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
	public final JPA2Parser.date_macro_expression_return date_macro_expression() throws RecognitionException {
		JPA2Parser.date_macro_expression_return retval = new JPA2Parser.date_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope date_between_macro_expression225 =null;
		ParserRuleReturnScope date_before_macro_expression226 =null;
		ParserRuleReturnScope date_after_macro_expression227 =null;
		ParserRuleReturnScope date_equals_macro_expression228 =null;
		ParserRuleReturnScope date_today_macro_expression229 =null;


		try {
			// JPA2.g:276:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
			int alt65=5;
			switch ( input.LA(1) ) {
			case 72:
				{
				alt65=1;
				}
				break;
			case 74:
				{
				alt65=2;
				}
				break;
			case 73:
				{
				alt65=3;
				}
				break;
			case 75:
				{
				alt65=4;
				}
				break;
			case 77:
				{
				alt65=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 65, 0, input);
				throw nvae;
			}
			switch (alt65) {
				case 1 :
					// JPA2.g:276:7: date_between_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2413);
					date_between_macro_expression225=date_between_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression225.getTree());

					}
					break;
				case 2 :
					// JPA2.g:277:7: date_before_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2421);
					date_before_macro_expression226=date_before_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression226.getTree());

					}
					break;
				case 3 :
					// JPA2.g:278:7: date_after_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2429);
					date_after_macro_expression227=date_after_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression227.getTree());

					}
					break;
				case 4 :
					// JPA2.g:279:7: date_equals_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2437);
					date_equals_macro_expression228=date_equals_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression228.getTree());

					}
					break;
				case 5 :
					// JPA2.g:280:7: date_today_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2445);
					date_today_macro_expression229=date_today_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_today_macro_expression229.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "date_macro_expression"


	public static class date_between_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_between_macro_expression"
	// JPA2.g:282:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
	public final JPA2Parser.date_between_macro_expression_return date_between_macro_expression() throws RecognitionException {
		JPA2Parser.date_between_macro_expression_return retval = new JPA2Parser.date_between_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal230=null;
		Token char_literal231=null;
		Token char_literal233=null;
		Token string_literal234=null;
		Token set235=null;
		Token char_literal237=null;
		Token string_literal238=null;
		Token set239=null;
		Token char_literal241=null;
		Token set242=null;
		Token char_literal243=null;
		ParserRuleReturnScope path_expression232 =null;
		ParserRuleReturnScope numeric_literal236 =null;
		ParserRuleReturnScope numeric_literal240 =null;

		Object string_literal230_tree=null;
		Object char_literal231_tree=null;
		Object char_literal233_tree=null;
		Object string_literal234_tree=null;
		Object set235_tree=null;
		Object char_literal237_tree=null;
		Object string_literal238_tree=null;
		Object set239_tree=null;
		Object char_literal241_tree=null;
		Object set242_tree=null;
		Object char_literal243_tree=null;

		try {
			// JPA2.g:283:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
			// JPA2.g:283:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal230=(Token)match(input,72,FOLLOW_72_in_date_between_macro_expression2457); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal230_tree = (Object)adaptor.create(string_literal230);
			adaptor.addChild(root_0, string_literal230_tree);
			}

			char_literal231=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2459); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal231_tree = (Object)adaptor.create(char_literal231);
			adaptor.addChild(root_0, char_literal231_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2461);
			path_expression232=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression232.getTree());

			char_literal233=(Token)match(input,60,FOLLOW_60_in_date_between_macro_expression2463); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal233_tree = (Object)adaptor.create(char_literal233);
			adaptor.addChild(root_0, char_literal233_tree);
			}

			string_literal234=(Token)match(input,116,FOLLOW_116_in_date_between_macro_expression2465); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal234_tree = (Object)adaptor.create(string_literal234);
			adaptor.addChild(root_0, string_literal234_tree);
			}

			// JPA2.g:283:48: ( ( '+' | '-' ) numeric_literal )?
			int alt66=2;
			int LA66_0 = input.LA(1);
			if ( (LA66_0==59||LA66_0==61) ) {
				alt66=1;
			}
			switch (alt66) {
				case 1 :
					// JPA2.g:283:49: ( '+' | '-' ) numeric_literal
					{
					set235=input.LT(1);
					if ( input.LA(1)==59||input.LA(1)==61 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set235));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2476);
					numeric_literal236=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal236.getTree());

					}
					break;

			}

			char_literal237=(Token)match(input,60,FOLLOW_60_in_date_between_macro_expression2480); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal237_tree = (Object)adaptor.create(char_literal237);
			adaptor.addChild(root_0, char_literal237_tree);
			}

			string_literal238=(Token)match(input,116,FOLLOW_116_in_date_between_macro_expression2482); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal238_tree = (Object)adaptor.create(string_literal238);
			adaptor.addChild(root_0, string_literal238_tree);
			}

			// JPA2.g:283:89: ( ( '+' | '-' ) numeric_literal )?
			int alt67=2;
			int LA67_0 = input.LA(1);
			if ( (LA67_0==59||LA67_0==61) ) {
				alt67=1;
			}
			switch (alt67) {
				case 1 :
					// JPA2.g:283:90: ( '+' | '-' ) numeric_literal
					{
					set239=input.LT(1);
					if ( input.LA(1)==59||input.LA(1)==61 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set239));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2493);
					numeric_literal240=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal240.getTree());

					}
					break;

			}

			char_literal241=(Token)match(input,60,FOLLOW_60_in_date_between_macro_expression2497); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal241_tree = (Object)adaptor.create(char_literal241);
			adaptor.addChild(root_0, char_literal241_tree);
			}

			set242=input.LT(1);
			if ( input.LA(1)==91||input.LA(1)==103||input.LA(1)==112||input.LA(1)==114||input.LA(1)==124||input.LA(1)==141 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set242));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			char_literal243=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2522); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal243_tree = (Object)adaptor.create(char_literal243);
			adaptor.addChild(root_0, char_literal243_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "date_between_macro_expression"


	public static class date_before_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_before_macro_expression"
	// JPA2.g:285:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_before_macro_expression_return date_before_macro_expression() throws RecognitionException {
		JPA2Parser.date_before_macro_expression_return retval = new JPA2Parser.date_before_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal244=null;
		Token char_literal245=null;
		Token char_literal247=null;
		Token char_literal250=null;
		ParserRuleReturnScope path_expression246 =null;
		ParserRuleReturnScope path_expression248 =null;
		ParserRuleReturnScope input_parameter249 =null;

		Object string_literal244_tree=null;
		Object char_literal245_tree=null;
		Object char_literal247_tree=null;
		Object char_literal250_tree=null;

		try {
			// JPA2.g:286:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:286:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal244=(Token)match(input,74,FOLLOW_74_in_date_before_macro_expression2534); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal244_tree = (Object)adaptor.create(string_literal244);
			adaptor.addChild(root_0, string_literal244_tree);
			}

			char_literal245=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2536); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal245_tree = (Object)adaptor.create(char_literal245);
			adaptor.addChild(root_0, char_literal245_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2538);
			path_expression246=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression246.getTree());

			char_literal247=(Token)match(input,60,FOLLOW_60_in_date_before_macro_expression2540); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal247_tree = (Object)adaptor.create(char_literal247);
			adaptor.addChild(root_0, char_literal247_tree);
			}

			// JPA2.g:286:45: ( path_expression | input_parameter )
			int alt68=2;
			int LA68_0 = input.LA(1);
			if ( (LA68_0==WORD) ) {
				alt68=1;
			}
			else if ( (LA68_0==NAMED_PARAMETER||LA68_0==57||LA68_0==71) ) {
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
					// JPA2.g:286:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2543);
					path_expression248=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression248.getTree());

					}
					break;
				case 2 :
					// JPA2.g:286:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2547);
					input_parameter249=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter249.getTree());

					}
					break;

			}

			char_literal250=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2550); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal250_tree = (Object)adaptor.create(char_literal250);
			adaptor.addChild(root_0, char_literal250_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "date_before_macro_expression"


	public static class date_after_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_after_macro_expression"
	// JPA2.g:288:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_after_macro_expression_return date_after_macro_expression() throws RecognitionException {
		JPA2Parser.date_after_macro_expression_return retval = new JPA2Parser.date_after_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal251=null;
		Token char_literal252=null;
		Token char_literal254=null;
		Token char_literal257=null;
		ParserRuleReturnScope path_expression253 =null;
		ParserRuleReturnScope path_expression255 =null;
		ParserRuleReturnScope input_parameter256 =null;

		Object string_literal251_tree=null;
		Object char_literal252_tree=null;
		Object char_literal254_tree=null;
		Object char_literal257_tree=null;

		try {
			// JPA2.g:289:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:289:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal251=(Token)match(input,73,FOLLOW_73_in_date_after_macro_expression2562); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal251_tree = (Object)adaptor.create(string_literal251);
			adaptor.addChild(root_0, string_literal251_tree);
			}

			char_literal252=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2564); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal252_tree = (Object)adaptor.create(char_literal252);
			adaptor.addChild(root_0, char_literal252_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2566);
			path_expression253=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression253.getTree());

			char_literal254=(Token)match(input,60,FOLLOW_60_in_date_after_macro_expression2568); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal254_tree = (Object)adaptor.create(char_literal254);
			adaptor.addChild(root_0, char_literal254_tree);
			}

			// JPA2.g:289:44: ( path_expression | input_parameter )
			int alt69=2;
			int LA69_0 = input.LA(1);
			if ( (LA69_0==WORD) ) {
				alt69=1;
			}
			else if ( (LA69_0==NAMED_PARAMETER||LA69_0==57||LA69_0==71) ) {
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
					// JPA2.g:289:45: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2571);
					path_expression255=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression255.getTree());

					}
					break;
				case 2 :
					// JPA2.g:289:63: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2575);
					input_parameter256=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter256.getTree());

					}
					break;

			}

			char_literal257=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2578); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal257_tree = (Object)adaptor.create(char_literal257);
			adaptor.addChild(root_0, char_literal257_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "date_after_macro_expression"


	public static class date_equals_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_equals_macro_expression"
	// JPA2.g:291:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_equals_macro_expression_return date_equals_macro_expression() throws RecognitionException {
		JPA2Parser.date_equals_macro_expression_return retval = new JPA2Parser.date_equals_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal258=null;
		Token char_literal259=null;
		Token char_literal261=null;
		Token char_literal264=null;
		ParserRuleReturnScope path_expression260 =null;
		ParserRuleReturnScope path_expression262 =null;
		ParserRuleReturnScope input_parameter263 =null;

		Object string_literal258_tree=null;
		Object char_literal259_tree=null;
		Object char_literal261_tree=null;
		Object char_literal264_tree=null;

		try {
			// JPA2.g:292:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:292:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal258=(Token)match(input,75,FOLLOW_75_in_date_equals_macro_expression2590); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal258_tree = (Object)adaptor.create(string_literal258);
			adaptor.addChild(root_0, string_literal258_tree);
			}

			char_literal259=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2592); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal259_tree = (Object)adaptor.create(char_literal259);
			adaptor.addChild(root_0, char_literal259_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2594);
			path_expression260=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression260.getTree());

			char_literal261=(Token)match(input,60,FOLLOW_60_in_date_equals_macro_expression2596); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal261_tree = (Object)adaptor.create(char_literal261);
			adaptor.addChild(root_0, char_literal261_tree);
			}

			// JPA2.g:292:45: ( path_expression | input_parameter )
			int alt70=2;
			int LA70_0 = input.LA(1);
			if ( (LA70_0==WORD) ) {
				alt70=1;
			}
			else if ( (LA70_0==NAMED_PARAMETER||LA70_0==57||LA70_0==71) ) {
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
					// JPA2.g:292:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2599);
					path_expression262=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression262.getTree());

					}
					break;
				case 2 :
					// JPA2.g:292:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2603);
					input_parameter263=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter263.getTree());

					}
					break;

			}

			char_literal264=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2606); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal264_tree = (Object)adaptor.create(char_literal264);
			adaptor.addChild(root_0, char_literal264_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "date_equals_macro_expression"


	public static class date_today_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_today_macro_expression"
	// JPA2.g:294:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
	public final JPA2Parser.date_today_macro_expression_return date_today_macro_expression() throws RecognitionException {
		JPA2Parser.date_today_macro_expression_return retval = new JPA2Parser.date_today_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal265=null;
		Token char_literal266=null;
		Token char_literal268=null;
		ParserRuleReturnScope path_expression267 =null;

		Object string_literal265_tree=null;
		Object char_literal266_tree=null;
		Object char_literal268_tree=null;

		try {
			// JPA2.g:295:5: ( '@TODAY' '(' path_expression ')' )
			// JPA2.g:295:7: '@TODAY' '(' path_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal265=(Token)match(input,77,FOLLOW_77_in_date_today_macro_expression2618); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal265_tree = (Object)adaptor.create(string_literal265);
			adaptor.addChild(root_0, string_literal265_tree);
			}

			char_literal266=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2620); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal266_tree = (Object)adaptor.create(char_literal266);
			adaptor.addChild(root_0, char_literal266_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2622);
			path_expression267=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression267.getTree());

			char_literal268=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2624); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal268_tree = (Object)adaptor.create(char_literal268);
			adaptor.addChild(root_0, char_literal268_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "date_today_macro_expression"


	public static class between_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "between_expression"
	// JPA2.g:298:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
	public final JPA2Parser.between_expression_return between_expression() throws RecognitionException {
		JPA2Parser.between_expression_return retval = new JPA2Parser.between_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal270=null;
		Token string_literal271=null;
		Token string_literal273=null;
		Token string_literal276=null;
		Token string_literal277=null;
		Token string_literal279=null;
		Token string_literal282=null;
		Token string_literal283=null;
		Token string_literal285=null;
		ParserRuleReturnScope arithmetic_expression269 =null;
		ParserRuleReturnScope arithmetic_expression272 =null;
		ParserRuleReturnScope arithmetic_expression274 =null;
		ParserRuleReturnScope string_expression275 =null;
		ParserRuleReturnScope string_expression278 =null;
		ParserRuleReturnScope string_expression280 =null;
		ParserRuleReturnScope datetime_expression281 =null;
		ParserRuleReturnScope datetime_expression284 =null;
		ParserRuleReturnScope datetime_expression286 =null;

		Object string_literal270_tree=null;
		Object string_literal271_tree=null;
		Object string_literal273_tree=null;
		Object string_literal276_tree=null;
		Object string_literal277_tree=null;
		Object string_literal279_tree=null;
		Object string_literal282_tree=null;
		Object string_literal283_tree=null;
		Object string_literal285_tree=null;

		try {
			// JPA2.g:299:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
			int alt74=3;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 59:
			case 61:
			case 64:
			case 78:
			case 104:
			case 108:
			case 110:
			case 113:
			case 126:
			case 128:
				{
				alt74=1;
				}
				break;
			case WORD:
				{
				int LA74_2 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case LPAREN:
				{
				int LA74_5 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 71:
				{
				int LA74_6 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA74_7 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 57:
				{
				int LA74_8 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case COUNT:
				{
				int LA74_16 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA74_17 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 102:
				{
				int LA74_18 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 84:
				{
				int LA74_19 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 86:
				{
				int LA74_20 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 118:
				{
				int LA74_21 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 85:
				{
				int LA74_22 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 100:
				{
				int LA74_23 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 76:
				{
				int LA74_24 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 87:
			case 129:
			case 133:
			case 136:
				{
				alt74=2;
				}
				break;
			case 88:
			case 89:
			case 90:
				{
				alt74=3;
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
					// JPA2.g:299:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2637);
					arithmetic_expression269=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression269.getTree());

					// JPA2.g:299:29: ( 'NOT' )?
					int alt71=2;
					int LA71_0 = input.LA(1);
					if ( (LA71_0==NOT) ) {
						alt71=1;
					}
					switch (alt71) {
						case 1 :
							// JPA2.g:299:30: 'NOT'
							{
							string_literal270=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2640); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal270_tree = (Object)adaptor.create(string_literal270);
							adaptor.addChild(root_0, string_literal270_tree);
							}

							}
							break;

					}

					string_literal271=(Token)match(input,82,FOLLOW_82_in_between_expression2644); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal271_tree = (Object)adaptor.create(string_literal271);
					adaptor.addChild(root_0, string_literal271_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2646);
					arithmetic_expression272=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression272.getTree());

					string_literal273=(Token)match(input,AND,FOLLOW_AND_in_between_expression2648); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal273_tree = (Object)adaptor.create(string_literal273);
					adaptor.addChild(root_0, string_literal273_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2650);
					arithmetic_expression274=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression274.getTree());

					}
					break;
				case 2 :
					// JPA2.g:300:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_between_expression2658);
					string_expression275=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression275.getTree());

					// JPA2.g:300:25: ( 'NOT' )?
					int alt72=2;
					int LA72_0 = input.LA(1);
					if ( (LA72_0==NOT) ) {
						alt72=1;
					}
					switch (alt72) {
						case 1 :
							// JPA2.g:300:26: 'NOT'
							{
							string_literal276=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2661); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal276_tree = (Object)adaptor.create(string_literal276);
							adaptor.addChild(root_0, string_literal276_tree);
							}

							}
							break;

					}

					string_literal277=(Token)match(input,82,FOLLOW_82_in_between_expression2665); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal277_tree = (Object)adaptor.create(string_literal277);
					adaptor.addChild(root_0, string_literal277_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2667);
					string_expression278=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression278.getTree());

					string_literal279=(Token)match(input,AND,FOLLOW_AND_in_between_expression2669); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal279_tree = (Object)adaptor.create(string_literal279);
					adaptor.addChild(root_0, string_literal279_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2671);
					string_expression280=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression280.getTree());

					}
					break;
				case 3 :
					// JPA2.g:301:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_between_expression2679);
					datetime_expression281=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression281.getTree());

					// JPA2.g:301:27: ( 'NOT' )?
					int alt73=2;
					int LA73_0 = input.LA(1);
					if ( (LA73_0==NOT) ) {
						alt73=1;
					}
					switch (alt73) {
						case 1 :
							// JPA2.g:301:28: 'NOT'
							{
							string_literal282=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2682); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal282_tree = (Object)adaptor.create(string_literal282);
							adaptor.addChild(root_0, string_literal282_tree);
							}

							}
							break;

					}

					string_literal283=(Token)match(input,82,FOLLOW_82_in_between_expression2686); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal283_tree = (Object)adaptor.create(string_literal283);
					adaptor.addChild(root_0, string_literal283_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2688);
					datetime_expression284=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression284.getTree());

					string_literal285=(Token)match(input,AND,FOLLOW_AND_in_between_expression2690); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal285_tree = (Object)adaptor.create(string_literal285);
					adaptor.addChild(root_0, string_literal285_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2692);
					datetime_expression286=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression286.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "between_expression"


	public static class in_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "in_expression"
	// JPA2.g:302:1: in_expression : ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) ;
	public final JPA2Parser.in_expression_return in_expression() throws RecognitionException {
		JPA2Parser.in_expression_return retval = new JPA2Parser.in_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NOT290=null;
		Token IN291=null;
		Token char_literal292=null;
		Token char_literal294=null;
		Token char_literal296=null;
		Token char_literal299=null;
		Token char_literal301=null;
		ParserRuleReturnScope path_expression287 =null;
		ParserRuleReturnScope type_discriminator288 =null;
		ParserRuleReturnScope identification_variable289 =null;
		ParserRuleReturnScope in_item293 =null;
		ParserRuleReturnScope in_item295 =null;
		ParserRuleReturnScope subquery297 =null;
		ParserRuleReturnScope collection_valued_input_parameter298 =null;
		ParserRuleReturnScope path_expression300 =null;

		Object NOT290_tree=null;
		Object IN291_tree=null;
		Object char_literal292_tree=null;
		Object char_literal294_tree=null;
		Object char_literal296_tree=null;
		Object char_literal299_tree=null;
		Object char_literal301_tree=null;

		try {
			// JPA2.g:303:5: ( ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) )
			// JPA2.g:303:7: ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:303:7: ( path_expression | type_discriminator | identification_variable )
			int alt75=3;
			int LA75_0 = input.LA(1);
			if ( (LA75_0==WORD) ) {
				int LA75_1 = input.LA(2);
				if ( (LA75_1==62) ) {
					alt75=1;
				}
				else if ( (LA75_1==IN||LA75_1==NOT) ) {
					alt75=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 75, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA75_0==134) ) {
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
					// JPA2.g:303:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_in_expression2704);
					path_expression287=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression287.getTree());

					}
					break;
				case 2 :
					// JPA2.g:303:26: type_discriminator
					{
					pushFollow(FOLLOW_type_discriminator_in_in_expression2708);
					type_discriminator288=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator288.getTree());

					}
					break;
				case 3 :
					// JPA2.g:303:47: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_in_expression2712);
					identification_variable289=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable289.getTree());

					}
					break;

			}

			// JPA2.g:303:72: ( NOT )?
			int alt76=2;
			int LA76_0 = input.LA(1);
			if ( (LA76_0==NOT) ) {
				alt76=1;
			}
			switch (alt76) {
				case 1 :
					// JPA2.g:303:73: NOT
					{
					NOT290=(Token)match(input,NOT,FOLLOW_NOT_in_in_expression2716); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT290_tree = (Object)adaptor.create(NOT290);
					adaptor.addChild(root_0, NOT290_tree);
					}

					}
					break;

			}

			IN291=(Token)match(input,IN,FOLLOW_IN_in_in_expression2720); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN291_tree = (Object)adaptor.create(IN291);
			adaptor.addChild(root_0, IN291_tree);
			}

			// JPA2.g:304:13: ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			int alt78=4;
			int LA78_0 = input.LA(1);
			if ( (LA78_0==LPAREN) ) {
				switch ( input.LA(2) ) {
				case 125:
					{
					alt78=2;
					}
					break;
				case WORD:
					{
					int LA78_4 = input.LA(3);
					if ( (LA78_4==RPAREN||LA78_4==60) ) {
						alt78=1;
					}
					else if ( (LA78_4==62) ) {
						alt78=4;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 78, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case NAMED_PARAMETER:
				case 57:
				case 71:
					{
					alt78=1;
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 78, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else if ( (LA78_0==NAMED_PARAMETER||LA78_0==57||LA78_0==71) ) {
				alt78=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 78, 0, input);
				throw nvae;
			}

			switch (alt78) {
				case 1 :
					// JPA2.g:304:15: '(' in_item ( ',' in_item )* ')'
					{
					char_literal292=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2736); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal292_tree = (Object)adaptor.create(char_literal292);
					adaptor.addChild(root_0, char_literal292_tree);
					}

					pushFollow(FOLLOW_in_item_in_in_expression2738);
					in_item293=in_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item293.getTree());

					// JPA2.g:304:27: ( ',' in_item )*
					loop77:
					while (true) {
						int alt77=2;
						int LA77_0 = input.LA(1);
						if ( (LA77_0==60) ) {
							alt77=1;
						}

						switch (alt77) {
						case 1 :
							// JPA2.g:304:28: ',' in_item
							{
							char_literal294=(Token)match(input,60,FOLLOW_60_in_in_expression2741); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal294_tree = (Object)adaptor.create(char_literal294);
							adaptor.addChild(root_0, char_literal294_tree);
							}

							pushFollow(FOLLOW_in_item_in_in_expression2743);
							in_item295=in_item();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item295.getTree());

							}
							break;

						default :
							break loop77;
						}
					}

					char_literal296=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2747); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal296_tree = (Object)adaptor.create(char_literal296);
					adaptor.addChild(root_0, char_literal296_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:305:15: subquery
					{
					pushFollow(FOLLOW_subquery_in_in_expression2763);
					subquery297=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery297.getTree());

					}
					break;
				case 3 :
					// JPA2.g:306:15: collection_valued_input_parameter
					{
					pushFollow(FOLLOW_collection_valued_input_parameter_in_in_expression2779);
					collection_valued_input_parameter298=collection_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_input_parameter298.getTree());

					}
					break;
				case 4 :
					// JPA2.g:307:15: '(' path_expression ')'
					{
					char_literal299=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2795); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal299_tree = (Object)adaptor.create(char_literal299);
					adaptor.addChild(root_0, char_literal299_tree);
					}

					pushFollow(FOLLOW_path_expression_in_in_expression2797);
					path_expression300=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression300.getTree());

					char_literal301=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2799); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal301_tree = (Object)adaptor.create(char_literal301);
					adaptor.addChild(root_0, char_literal301_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "in_expression"


	public static class in_item_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "in_item"
	// JPA2.g:313:1: in_item : ( literal | single_valued_input_parameter );
	public final JPA2Parser.in_item_return in_item() throws RecognitionException {
		JPA2Parser.in_item_return retval = new JPA2Parser.in_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal302 =null;
		ParserRuleReturnScope single_valued_input_parameter303 =null;


		try {
			// JPA2.g:314:5: ( literal | single_valued_input_parameter )
			int alt79=2;
			int LA79_0 = input.LA(1);
			if ( (LA79_0==WORD) ) {
				alt79=1;
			}
			else if ( (LA79_0==NAMED_PARAMETER||LA79_0==57||LA79_0==71) ) {
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
					// JPA2.g:314:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_in_item2827);
					literal302=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal302.getTree());

					}
					break;
				case 2 :
					// JPA2.g:314:17: single_valued_input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_single_valued_input_parameter_in_in_item2831);
					single_valued_input_parameter303=single_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_input_parameter303.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "in_item"


	public static class like_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "like_expression"
	// JPA2.g:315:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? ;
	public final JPA2Parser.like_expression_return like_expression() throws RecognitionException {
		JPA2Parser.like_expression_return retval = new JPA2Parser.like_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal305=null;
		Token string_literal306=null;
		Token string_literal310=null;
		ParserRuleReturnScope string_expression304 =null;
		ParserRuleReturnScope string_expression307 =null;
		ParserRuleReturnScope pattern_value308 =null;
		ParserRuleReturnScope input_parameter309 =null;
		ParserRuleReturnScope escape_character311 =null;

		Object string_literal305_tree=null;
		Object string_literal306_tree=null;
		Object string_literal310_tree=null;

		try {
			// JPA2.g:316:5: ( string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? )
			// JPA2.g:316:7: string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_expression_in_like_expression2842);
			string_expression304=string_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression304.getTree());

			// JPA2.g:316:25: ( 'NOT' )?
			int alt80=2;
			int LA80_0 = input.LA(1);
			if ( (LA80_0==NOT) ) {
				alt80=1;
			}
			switch (alt80) {
				case 1 :
					// JPA2.g:316:26: 'NOT'
					{
					string_literal305=(Token)match(input,NOT,FOLLOW_NOT_in_like_expression2845); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal305_tree = (Object)adaptor.create(string_literal305);
					adaptor.addChild(root_0, string_literal305_tree);
					}

					}
					break;

			}

			string_literal306=(Token)match(input,109,FOLLOW_109_in_like_expression2849); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal306_tree = (Object)adaptor.create(string_literal306);
			adaptor.addChild(root_0, string_literal306_tree);
			}

			// JPA2.g:316:41: ( string_expression | pattern_value | input_parameter )
			int alt81=3;
			switch ( input.LA(1) ) {
			case AVG:
			case COUNT:
			case LOWER:
			case LPAREN:
			case MAX:
			case MIN:
			case SUM:
			case WORD:
			case 76:
			case 84:
			case 85:
			case 86:
			case 87:
			case 100:
			case 102:
			case 118:
			case 129:
			case 133:
			case 136:
				{
				alt81=1;
				}
				break;
			case STRING_LITERAL:
				{
				int LA81_2 = input.LA(2);
				if ( (synpred129_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 81, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 71:
				{
				int LA81_3 = input.LA(2);
				if ( (LA81_3==64) ) {
					int LA81_7 = input.LA(3);
					if ( (LA81_7==INT_NUMERAL) ) {
						int LA81_11 = input.LA(4);
						if ( (synpred129_JPA2()) ) {
							alt81=1;
						}
						else if ( (true) ) {
							alt81=3;
						}

					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 81, 7, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA81_3==INT_NUMERAL) ) {
					int LA81_8 = input.LA(3);
					if ( (synpred129_JPA2()) ) {
						alt81=1;
					}
					else if ( (true) ) {
						alt81=3;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 81, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA81_4 = input.LA(2);
				if ( (synpred129_JPA2()) ) {
					alt81=1;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case 57:
				{
				int LA81_5 = input.LA(2);
				if ( (LA81_5==WORD) ) {
					int LA81_10 = input.LA(3);
					if ( (LA81_10==144) ) {
						int LA81_12 = input.LA(4);
						if ( (synpred129_JPA2()) ) {
							alt81=1;
						}
						else if ( (true) ) {
							alt81=3;
						}

					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 81, 10, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 81, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 81, 0, input);
				throw nvae;
			}
			switch (alt81) {
				case 1 :
					// JPA2.g:316:42: string_expression
					{
					pushFollow(FOLLOW_string_expression_in_like_expression2852);
					string_expression307=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression307.getTree());

					}
					break;
				case 2 :
					// JPA2.g:316:62: pattern_value
					{
					pushFollow(FOLLOW_pattern_value_in_like_expression2856);
					pattern_value308=pattern_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value308.getTree());

					}
					break;
				case 3 :
					// JPA2.g:316:78: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_like_expression2860);
					input_parameter309=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter309.getTree());

					}
					break;

			}

			// JPA2.g:316:94: ( 'ESCAPE' escape_character )?
			int alt82=2;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==98) ) {
				alt82=1;
			}
			switch (alt82) {
				case 1 :
					// JPA2.g:316:95: 'ESCAPE' escape_character
					{
					string_literal310=(Token)match(input,98,FOLLOW_98_in_like_expression2863); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal310_tree = (Object)adaptor.create(string_literal310);
					adaptor.addChild(root_0, string_literal310_tree);
					}

					pushFollow(FOLLOW_escape_character_in_like_expression2865);
					escape_character311=escape_character();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, escape_character311.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "like_expression"


	public static class null_comparison_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "null_comparison_expression"
	// JPA2.g:317:1: null_comparison_expression : ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' ;
	public final JPA2Parser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
		JPA2Parser.null_comparison_expression_return retval = new JPA2Parser.null_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal315=null;
		Token string_literal316=null;
		Token string_literal317=null;
		ParserRuleReturnScope path_expression312 =null;
		ParserRuleReturnScope input_parameter313 =null;
		ParserRuleReturnScope join_association_path_expression314 =null;

		Object string_literal315_tree=null;
		Object string_literal316_tree=null;
		Object string_literal317_tree=null;

		try {
			// JPA2.g:318:5: ( ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' )
			// JPA2.g:318:7: ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:318:7: ( path_expression | input_parameter | join_association_path_expression )
			int alt83=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA83_1 = input.LA(2);
				if ( (LA83_1==62) ) {
					int LA83_4 = input.LA(3);
					if ( (synpred132_JPA2()) ) {
						alt83=1;
					}
					else if ( (true) ) {
						alt83=3;
					}

				}
				else if ( (LA83_1==105) ) {
					alt83=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 83, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt83=2;
				}
				break;
			case 132:
				{
				alt83=3;
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
					// JPA2.g:318:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_null_comparison_expression2879);
					path_expression312=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression312.getTree());

					}
					break;
				case 2 :
					// JPA2.g:318:26: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_null_comparison_expression2883);
					input_parameter313=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter313.getTree());

					}
					break;
				case 3 :
					// JPA2.g:318:44: join_association_path_expression
					{
					pushFollow(FOLLOW_join_association_path_expression_in_null_comparison_expression2887);
					join_association_path_expression314=join_association_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression314.getTree());

					}
					break;

			}

			string_literal315=(Token)match(input,105,FOLLOW_105_in_null_comparison_expression2890); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal315_tree = (Object)adaptor.create(string_literal315);
			adaptor.addChild(root_0, string_literal315_tree);
			}

			// JPA2.g:318:83: ( 'NOT' )?
			int alt84=2;
			int LA84_0 = input.LA(1);
			if ( (LA84_0==NOT) ) {
				alt84=1;
			}
			switch (alt84) {
				case 1 :
					// JPA2.g:318:84: 'NOT'
					{
					string_literal316=(Token)match(input,NOT,FOLLOW_NOT_in_null_comparison_expression2893); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal316_tree = (Object)adaptor.create(string_literal316);
					adaptor.addChild(root_0, string_literal316_tree);
					}

					}
					break;

			}

			string_literal317=(Token)match(input,117,FOLLOW_117_in_null_comparison_expression2897); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal317_tree = (Object)adaptor.create(string_literal317);
			adaptor.addChild(root_0, string_literal317_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "null_comparison_expression"


	public static class empty_collection_comparison_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "empty_collection_comparison_expression"
	// JPA2.g:319:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
	public final JPA2Parser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
		JPA2Parser.empty_collection_comparison_expression_return retval = new JPA2Parser.empty_collection_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal319=null;
		Token string_literal320=null;
		Token string_literal321=null;
		ParserRuleReturnScope path_expression318 =null;

		Object string_literal319_tree=null;
		Object string_literal320_tree=null;
		Object string_literal321_tree=null;

		try {
			// JPA2.g:320:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
			// JPA2.g:320:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression2908);
			path_expression318=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression318.getTree());

			string_literal319=(Token)match(input,105,FOLLOW_105_in_empty_collection_comparison_expression2910); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal319_tree = (Object)adaptor.create(string_literal319);
			adaptor.addChild(root_0, string_literal319_tree);
			}

			// JPA2.g:320:28: ( 'NOT' )?
			int alt85=2;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==NOT) ) {
				alt85=1;
			}
			switch (alt85) {
				case 1 :
					// JPA2.g:320:29: 'NOT'
					{
					string_literal320=(Token)match(input,NOT,FOLLOW_NOT_in_empty_collection_comparison_expression2913); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal320_tree = (Object)adaptor.create(string_literal320);
					adaptor.addChild(root_0, string_literal320_tree);
					}

					}
					break;

			}

			string_literal321=(Token)match(input,94,FOLLOW_94_in_empty_collection_comparison_expression2917); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal321_tree = (Object)adaptor.create(string_literal321);
			adaptor.addChild(root_0, string_literal321_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "empty_collection_comparison_expression"


	public static class collection_member_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "collection_member_expression"
	// JPA2.g:321:1: collection_member_expression : entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
	public final JPA2Parser.collection_member_expression_return collection_member_expression() throws RecognitionException {
		JPA2Parser.collection_member_expression_return retval = new JPA2Parser.collection_member_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal323=null;
		Token string_literal324=null;
		Token string_literal325=null;
		ParserRuleReturnScope entity_or_value_expression322 =null;
		ParserRuleReturnScope path_expression326 =null;

		Object string_literal323_tree=null;
		Object string_literal324_tree=null;
		Object string_literal325_tree=null;

		try {
			// JPA2.g:322:5: ( entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
			// JPA2.g:322:7: entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_or_value_expression_in_collection_member_expression2928);
			entity_or_value_expression322=entity_or_value_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_or_value_expression322.getTree());

			// JPA2.g:322:35: ( 'NOT' )?
			int alt86=2;
			int LA86_0 = input.LA(1);
			if ( (LA86_0==NOT) ) {
				alt86=1;
			}
			switch (alt86) {
				case 1 :
					// JPA2.g:322:36: 'NOT'
					{
					string_literal323=(Token)match(input,NOT,FOLLOW_NOT_in_collection_member_expression2932); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal323_tree = (Object)adaptor.create(string_literal323);
					adaptor.addChild(root_0, string_literal323_tree);
					}

					}
					break;

			}

			string_literal324=(Token)match(input,111,FOLLOW_111_in_collection_member_expression2936); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal324_tree = (Object)adaptor.create(string_literal324);
			adaptor.addChild(root_0, string_literal324_tree);
			}

			// JPA2.g:322:53: ( 'OF' )?
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==120) ) {
				alt87=1;
			}
			switch (alt87) {
				case 1 :
					// JPA2.g:322:54: 'OF'
					{
					string_literal325=(Token)match(input,120,FOLLOW_120_in_collection_member_expression2939); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal325_tree = (Object)adaptor.create(string_literal325);
					adaptor.addChild(root_0, string_literal325_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_path_expression_in_collection_member_expression2943);
			path_expression326=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression326.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "collection_member_expression"


	public static class entity_or_value_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_or_value_expression"
	// JPA2.g:323:1: entity_or_value_expression : ( path_expression | simple_entity_or_value_expression | subquery );
	public final JPA2Parser.entity_or_value_expression_return entity_or_value_expression() throws RecognitionException {
		JPA2Parser.entity_or_value_expression_return retval = new JPA2Parser.entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression327 =null;
		ParserRuleReturnScope simple_entity_or_value_expression328 =null;
		ParserRuleReturnScope subquery329 =null;


		try {
			// JPA2.g:324:5: ( path_expression | simple_entity_or_value_expression | subquery )
			int alt88=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA88_1 = input.LA(2);
				if ( (LA88_1==62) ) {
					alt88=1;
				}
				else if ( (LA88_1==NOT||LA88_1==111) ) {
					alt88=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 88, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt88=2;
				}
				break;
			case LPAREN:
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
					// JPA2.g:324:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_or_value_expression2954);
					path_expression327=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression327.getTree());

					}
					break;
				case 2 :
					// JPA2.g:325:7: simple_entity_or_value_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2962);
					simple_entity_or_value_expression328=simple_entity_or_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_or_value_expression328.getTree());

					}
					break;
				case 3 :
					// JPA2.g:326:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_entity_or_value_expression2970);
					subquery329=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery329.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "entity_or_value_expression"


	public static class simple_entity_or_value_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_entity_or_value_expression"
	// JPA2.g:327:1: simple_entity_or_value_expression : ( identification_variable | input_parameter | literal );
	public final JPA2Parser.simple_entity_or_value_expression_return simple_entity_or_value_expression() throws RecognitionException {
		JPA2Parser.simple_entity_or_value_expression_return retval = new JPA2Parser.simple_entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable330 =null;
		ParserRuleReturnScope input_parameter331 =null;
		ParserRuleReturnScope literal332 =null;


		try {
			// JPA2.g:328:5: ( identification_variable | input_parameter | literal )
			int alt89=3;
			int LA89_0 = input.LA(1);
			if ( (LA89_0==WORD) ) {
				int LA89_1 = input.LA(2);
				if ( (synpred140_JPA2()) ) {
					alt89=1;
				}
				else if ( (true) ) {
					alt89=3;
				}

			}
			else if ( (LA89_0==NAMED_PARAMETER||LA89_0==57||LA89_0==71) ) {
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
					// JPA2.g:328:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_or_value_expression2981);
					identification_variable330=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable330.getTree());

					}
					break;
				case 2 :
					// JPA2.g:329:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_or_value_expression2989);
					input_parameter331=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter331.getTree());

					}
					break;
				case 3 :
					// JPA2.g:330:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_simple_entity_or_value_expression2997);
					literal332=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal332.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "simple_entity_or_value_expression"


	public static class exists_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "exists_expression"
	// JPA2.g:331:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
	public final JPA2Parser.exists_expression_return exists_expression() throws RecognitionException {
		JPA2Parser.exists_expression_return retval = new JPA2Parser.exists_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal333=null;
		Token string_literal334=null;
		ParserRuleReturnScope subquery335 =null;

		Object string_literal333_tree=null;
		Object string_literal334_tree=null;

		try {
			// JPA2.g:332:5: ( ( 'NOT' )? 'EXISTS' subquery )
			// JPA2.g:332:7: ( 'NOT' )? 'EXISTS' subquery
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:332:7: ( 'NOT' )?
			int alt90=2;
			int LA90_0 = input.LA(1);
			if ( (LA90_0==NOT) ) {
				alt90=1;
			}
			switch (alt90) {
				case 1 :
					// JPA2.g:332:8: 'NOT'
					{
					string_literal333=(Token)match(input,NOT,FOLLOW_NOT_in_exists_expression3009); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal333_tree = (Object)adaptor.create(string_literal333);
					adaptor.addChild(root_0, string_literal333_tree);
					}

					}
					break;

			}

			string_literal334=(Token)match(input,99,FOLLOW_99_in_exists_expression3013); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal334_tree = (Object)adaptor.create(string_literal334);
			adaptor.addChild(root_0, string_literal334_tree);
			}

			pushFollow(FOLLOW_subquery_in_exists_expression3015);
			subquery335=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery335.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "exists_expression"


	public static class all_or_any_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "all_or_any_expression"
	// JPA2.g:333:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
	public final JPA2Parser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
		JPA2Parser.all_or_any_expression_return retval = new JPA2Parser.all_or_any_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set336=null;
		ParserRuleReturnScope subquery337 =null;

		Object set336_tree=null;

		try {
			// JPA2.g:334:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
			// JPA2.g:334:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
			{
			root_0 = (Object)adaptor.nil();


			set336=input.LT(1);
			if ( (input.LA(1) >= 79 && input.LA(1) <= 80)||input.LA(1)==127 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set336));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_subquery_in_all_or_any_expression3039);
			subquery337=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery337.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "all_or_any_expression"


	public static class comparison_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "comparison_expression"
	// JPA2.g:335:1: comparison_expression : ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
	public final JPA2Parser.comparison_expression_return comparison_expression() throws RecognitionException {
		JPA2Parser.comparison_expression_return retval = new JPA2Parser.comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal340=null;
		Token set344=null;
		Token set348=null;
		Token set356=null;
		Token set360=null;
		ParserRuleReturnScope string_expression338 =null;
		ParserRuleReturnScope comparison_operator339 =null;
		ParserRuleReturnScope string_expression341 =null;
		ParserRuleReturnScope all_or_any_expression342 =null;
		ParserRuleReturnScope boolean_expression343 =null;
		ParserRuleReturnScope boolean_expression345 =null;
		ParserRuleReturnScope all_or_any_expression346 =null;
		ParserRuleReturnScope enum_expression347 =null;
		ParserRuleReturnScope enum_expression349 =null;
		ParserRuleReturnScope all_or_any_expression350 =null;
		ParserRuleReturnScope datetime_expression351 =null;
		ParserRuleReturnScope comparison_operator352 =null;
		ParserRuleReturnScope datetime_expression353 =null;
		ParserRuleReturnScope all_or_any_expression354 =null;
		ParserRuleReturnScope entity_expression355 =null;
		ParserRuleReturnScope entity_expression357 =null;
		ParserRuleReturnScope all_or_any_expression358 =null;
		ParserRuleReturnScope entity_type_expression359 =null;
		ParserRuleReturnScope entity_type_expression361 =null;
		ParserRuleReturnScope arithmetic_expression362 =null;
		ParserRuleReturnScope comparison_operator363 =null;
		ParserRuleReturnScope arithmetic_expression364 =null;
		ParserRuleReturnScope all_or_any_expression365 =null;

		Object string_literal340_tree=null;
		Object set344_tree=null;
		Object set348_tree=null;
		Object set356_tree=null;
		Object set360_tree=null;

		try {
			// JPA2.g:336:5: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
			int alt98=7;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA98_1 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred153_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (synpred158_JPA2()) ) {
					alt98=5;
				}
				else if ( (synpred160_JPA2()) ) {
					alt98=6;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 87:
			case 129:
			case 133:
			case 136:
				{
				alt98=1;
				}
				break;
			case 71:
				{
				int LA98_3 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred153_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (synpred158_JPA2()) ) {
					alt98=5;
				}
				else if ( (synpred160_JPA2()) ) {
					alt98=6;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA98_4 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred153_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (synpred158_JPA2()) ) {
					alt98=5;
				}
				else if ( (synpred160_JPA2()) ) {
					alt98=6;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 57:
				{
				int LA98_5 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred153_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (synpred158_JPA2()) ) {
					alt98=5;
				}
				else if ( (synpred160_JPA2()) ) {
					alt98=6;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case COUNT:
				{
				int LA98_11 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA98_12 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 102:
				{
				int LA98_13 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 84:
				{
				int LA98_14 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred153_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 86:
				{
				int LA98_15 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred153_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 118:
				{
				int LA98_16 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred153_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 85:
				{
				int LA98_17 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 100:
				{
				int LA98_18 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 76:
				{
				int LA98_19 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA98_20 = input.LA(2);
				if ( (synpred147_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred153_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred155_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 142:
			case 143:
				{
				alt98=2;
				}
				break;
			case 88:
			case 89:
			case 90:
				{
				alt98=4;
				}
				break;
			case 134:
				{
				alt98=6;
				}
				break;
			case INT_NUMERAL:
			case 59:
			case 61:
			case 64:
			case 78:
			case 104:
			case 108:
			case 110:
			case 113:
			case 126:
			case 128:
				{
				alt98=7;
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
					// JPA2.g:336:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_comparison_expression3050);
					string_expression338=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression338.getTree());

					// JPA2.g:336:25: ( comparison_operator | 'REGEXP' )
					int alt91=2;
					int LA91_0 = input.LA(1);
					if ( ((LA91_0 >= 65 && LA91_0 <= 70)) ) {
						alt91=1;
					}
					else if ( (LA91_0==123) ) {
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
							// JPA2.g:336:26: comparison_operator
							{
							pushFollow(FOLLOW_comparison_operator_in_comparison_expression3053);
							comparison_operator339=comparison_operator();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator339.getTree());

							}
							break;
						case 2 :
							// JPA2.g:336:48: 'REGEXP'
							{
							string_literal340=(Token)match(input,123,FOLLOW_123_in_comparison_expression3057); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal340_tree = (Object)adaptor.create(string_literal340);
							adaptor.addChild(root_0, string_literal340_tree);
							}

							}
							break;

					}

					// JPA2.g:336:58: ( string_expression | all_or_any_expression )
					int alt92=2;
					int LA92_0 = input.LA(1);
					if ( (LA92_0==AVG||LA92_0==COUNT||(LA92_0 >= LOWER && LA92_0 <= NAMED_PARAMETER)||(LA92_0 >= STRING_LITERAL && LA92_0 <= SUM)||LA92_0==WORD||LA92_0==57||LA92_0==71||LA92_0==76||(LA92_0 >= 84 && LA92_0 <= 87)||LA92_0==100||LA92_0==102||LA92_0==118||LA92_0==129||LA92_0==133||LA92_0==136) ) {
						alt92=1;
					}
					else if ( ((LA92_0 >= 79 && LA92_0 <= 80)||LA92_0==127) ) {
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
							// JPA2.g:336:59: string_expression
							{
							pushFollow(FOLLOW_string_expression_in_comparison_expression3061);
							string_expression341=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression341.getTree());

							}
							break;
						case 2 :
							// JPA2.g:336:79: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3065);
							all_or_any_expression342=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression342.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:337:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_comparison_expression3074);
					boolean_expression343=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression343.getTree());

					set344=input.LT(1);
					if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set344));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:337:39: ( boolean_expression | all_or_any_expression )
					int alt93=2;
					int LA93_0 = input.LA(1);
					if ( (LA93_0==LPAREN||LA93_0==NAMED_PARAMETER||LA93_0==WORD||LA93_0==57||LA93_0==71||LA93_0==76||(LA93_0 >= 84 && LA93_0 <= 86)||LA93_0==100||LA93_0==102||LA93_0==118||(LA93_0 >= 142 && LA93_0 <= 143)) ) {
						alt93=1;
					}
					else if ( ((LA93_0 >= 79 && LA93_0 <= 80)||LA93_0==127) ) {
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
							// JPA2.g:337:40: boolean_expression
							{
							pushFollow(FOLLOW_boolean_expression_in_comparison_expression3085);
							boolean_expression345=boolean_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression345.getTree());

							}
							break;
						case 2 :
							// JPA2.g:337:61: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3089);
							all_or_any_expression346=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression346.getTree());

							}
							break;

					}

					}
					break;
				case 3 :
					// JPA2.g:338:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_comparison_expression3098);
					enum_expression347=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression347.getTree());

					set348=input.LT(1);
					if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set348));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:338:34: ( enum_expression | all_or_any_expression )
					int alt94=2;
					int LA94_0 = input.LA(1);
					if ( (LA94_0==LPAREN||LA94_0==NAMED_PARAMETER||LA94_0==WORD||LA94_0==57||LA94_0==71||LA94_0==84||LA94_0==86||LA94_0==118) ) {
						alt94=1;
					}
					else if ( ((LA94_0 >= 79 && LA94_0 <= 80)||LA94_0==127) ) {
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
							// JPA2.g:338:35: enum_expression
							{
							pushFollow(FOLLOW_enum_expression_in_comparison_expression3107);
							enum_expression349=enum_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression349.getTree());

							}
							break;
						case 2 :
							// JPA2.g:338:53: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3111);
							all_or_any_expression350=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression350.getTree());

							}
							break;

					}

					}
					break;
				case 4 :
					// JPA2.g:339:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_comparison_expression3120);
					datetime_expression351=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression351.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3122);
					comparison_operator352=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator352.getTree());

					// JPA2.g:339:47: ( datetime_expression | all_or_any_expression )
					int alt95=2;
					int LA95_0 = input.LA(1);
					if ( (LA95_0==AVG||LA95_0==COUNT||(LA95_0 >= LPAREN && LA95_0 <= NAMED_PARAMETER)||LA95_0==SUM||LA95_0==WORD||LA95_0==57||LA95_0==71||LA95_0==76||(LA95_0 >= 84 && LA95_0 <= 86)||(LA95_0 >= 88 && LA95_0 <= 90)||LA95_0==100||LA95_0==102||LA95_0==118) ) {
						alt95=1;
					}
					else if ( ((LA95_0 >= 79 && LA95_0 <= 80)||LA95_0==127) ) {
						alt95=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 95, 0, input);
						throw nvae;
					}

					switch (alt95) {
						case 1 :
							// JPA2.g:339:48: datetime_expression
							{
							pushFollow(FOLLOW_datetime_expression_in_comparison_expression3125);
							datetime_expression353=datetime_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression353.getTree());

							}
							break;
						case 2 :
							// JPA2.g:339:70: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3129);
							all_or_any_expression354=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression354.getTree());

							}
							break;

					}

					}
					break;
				case 5 :
					// JPA2.g:340:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_expression_in_comparison_expression3138);
					entity_expression355=entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression355.getTree());

					set356=input.LT(1);
					if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set356));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:340:38: ( entity_expression | all_or_any_expression )
					int alt96=2;
					int LA96_0 = input.LA(1);
					if ( (LA96_0==NAMED_PARAMETER||LA96_0==WORD||LA96_0==57||LA96_0==71) ) {
						alt96=1;
					}
					else if ( ((LA96_0 >= 79 && LA96_0 <= 80)||LA96_0==127) ) {
						alt96=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 96, 0, input);
						throw nvae;
					}

					switch (alt96) {
						case 1 :
							// JPA2.g:340:39: entity_expression
							{
							pushFollow(FOLLOW_entity_expression_in_comparison_expression3149);
							entity_expression357=entity_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression357.getTree());

							}
							break;
						case 2 :
							// JPA2.g:340:59: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3153);
							all_or_any_expression358=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression358.getTree());

							}
							break;

					}

					}
					break;
				case 6 :
					// JPA2.g:341:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3162);
					entity_type_expression359=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression359.getTree());

					set360=input.LT(1);
					if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set360));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3172);
					entity_type_expression361=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression361.getTree());

					}
					break;
				case 7 :
					// JPA2.g:342:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3180);
					arithmetic_expression362=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression362.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3182);
					comparison_operator363=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator363.getTree());

					// JPA2.g:342:49: ( arithmetic_expression | all_or_any_expression )
					int alt97=2;
					int LA97_0 = input.LA(1);
					if ( (LA97_0==AVG||LA97_0==COUNT||LA97_0==INT_NUMERAL||(LA97_0 >= LPAREN && LA97_0 <= NAMED_PARAMETER)||LA97_0==SUM||LA97_0==WORD||LA97_0==57||LA97_0==59||LA97_0==61||LA97_0==64||LA97_0==71||LA97_0==76||LA97_0==78||(LA97_0 >= 84 && LA97_0 <= 86)||LA97_0==100||LA97_0==102||LA97_0==104||LA97_0==108||LA97_0==110||LA97_0==113||LA97_0==118||LA97_0==126||LA97_0==128) ) {
						alt97=1;
					}
					else if ( ((LA97_0 >= 79 && LA97_0 <= 80)||LA97_0==127) ) {
						alt97=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 97, 0, input);
						throw nvae;
					}

					switch (alt97) {
						case 1 :
							// JPA2.g:342:50: arithmetic_expression
							{
							pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3185);
							arithmetic_expression364=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression364.getTree());

							}
							break;
						case 2 :
							// JPA2.g:342:74: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3189);
							all_or_any_expression365=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression365.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "comparison_expression"


	public static class comparison_operator_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "comparison_operator"
	// JPA2.g:344:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
	public final JPA2Parser.comparison_operator_return comparison_operator() throws RecognitionException {
		JPA2Parser.comparison_operator_return retval = new JPA2Parser.comparison_operator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set366=null;

		Object set366_tree=null;

		try {
			// JPA2.g:345:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set366=input.LT(1);
			if ( (input.LA(1) >= 65 && input.LA(1) <= 70) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set366));
				state.errorRecovery=false;
				state.failed=false;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "comparison_operator"


	public static class arithmetic_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "arithmetic_expression"
	// JPA2.g:351:1: arithmetic_expression : ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term );
	public final JPA2Parser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
		JPA2Parser.arithmetic_expression_return retval = new JPA2Parser.arithmetic_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set369=null;
		ParserRuleReturnScope arithmetic_term367 =null;
		ParserRuleReturnScope arithmetic_term368 =null;
		ParserRuleReturnScope arithmetic_term370 =null;

		Object set369_tree=null;

		try {
			// JPA2.g:352:5: ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term )
			int alt99=2;
			switch ( input.LA(1) ) {
			case 59:
			case 61:
				{
				int LA99_1 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case WORD:
				{
				int LA99_2 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 64:
				{
				int LA99_3 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA99_4 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case LPAREN:
				{
				int LA99_5 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 71:
				{
				int LA99_6 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA99_7 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 57:
				{
				int LA99_8 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 108:
				{
				int LA99_9 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 110:
				{
				int LA99_10 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 78:
				{
				int LA99_11 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 128:
				{
				int LA99_12 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 113:
				{
				int LA99_13 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 126:
				{
				int LA99_14 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 104:
				{
				int LA99_15 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case COUNT:
				{
				int LA99_16 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA99_17 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 102:
				{
				int LA99_18 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 84:
				{
				int LA99_19 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 86:
				{
				int LA99_20 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 118:
				{
				int LA99_21 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 85:
				{
				int LA99_22 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 100:
				{
				int LA99_23 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 76:
				{
				int LA99_24 = input.LA(2);
				if ( (synpred167_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

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
					// JPA2.g:352:7: arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3253);
					arithmetic_term367=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term367.getTree());

					}
					break;
				case 2 :
					// JPA2.g:353:7: arithmetic_term ( '+' | '-' ) arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3261);
					arithmetic_term368=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term368.getTree());

					set369=input.LT(1);
					if ( input.LA(1)==59||input.LA(1)==61 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set369));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3271);
					arithmetic_term370=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term370.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "arithmetic_expression"


	public static class arithmetic_term_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "arithmetic_term"
	// JPA2.g:354:1: arithmetic_term : ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor );
	public final JPA2Parser.arithmetic_term_return arithmetic_term() throws RecognitionException {
		JPA2Parser.arithmetic_term_return retval = new JPA2Parser.arithmetic_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set373=null;
		ParserRuleReturnScope arithmetic_factor371 =null;
		ParserRuleReturnScope arithmetic_factor372 =null;
		ParserRuleReturnScope arithmetic_factor374 =null;

		Object set373_tree=null;

		try {
			// JPA2.g:355:5: ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor )
			int alt100=2;
			switch ( input.LA(1) ) {
			case 59:
			case 61:
				{
				int LA100_1 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case WORD:
				{
				int LA100_2 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 64:
				{
				int LA100_3 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA100_4 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case LPAREN:
				{
				int LA100_5 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 71:
				{
				int LA100_6 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA100_7 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 57:
				{
				int LA100_8 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 108:
				{
				int LA100_9 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 110:
				{
				int LA100_10 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 78:
				{
				int LA100_11 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 128:
				{
				int LA100_12 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 113:
				{
				int LA100_13 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 126:
				{
				int LA100_14 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 104:
				{
				int LA100_15 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case COUNT:
				{
				int LA100_16 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA100_17 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 102:
				{
				int LA100_18 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 84:
				{
				int LA100_19 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 86:
				{
				int LA100_20 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 118:
				{
				int LA100_21 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 85:
				{
				int LA100_22 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 100:
				{
				int LA100_23 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 76:
				{
				int LA100_24 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

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
					// JPA2.g:355:7: arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3282);
					arithmetic_factor371=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor371.getTree());

					}
					break;
				case 2 :
					// JPA2.g:356:7: arithmetic_factor ( '*' | '/' ) arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3290);
					arithmetic_factor372=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor372.getTree());

					set373=input.LT(1);
					if ( input.LA(1)==58||input.LA(1)==63 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set373));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3301);
					arithmetic_factor374=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor374.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "arithmetic_term"


	public static class arithmetic_factor_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "arithmetic_factor"
	// JPA2.g:357:1: arithmetic_factor : ( ( '+' | '-' ) )? arithmetic_primary ;
	public final JPA2Parser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
		JPA2Parser.arithmetic_factor_return retval = new JPA2Parser.arithmetic_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set375=null;
		ParserRuleReturnScope arithmetic_primary376 =null;

		Object set375_tree=null;

		try {
			// JPA2.g:358:5: ( ( ( '+' | '-' ) )? arithmetic_primary )
			// JPA2.g:358:7: ( ( '+' | '-' ) )? arithmetic_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:358:7: ( ( '+' | '-' ) )?
			int alt101=2;
			int LA101_0 = input.LA(1);
			if ( (LA101_0==59||LA101_0==61) ) {
				alt101=1;
			}
			switch (alt101) {
				case 1 :
					// JPA2.g:
					{
					set375=input.LT(1);
					if ( input.LA(1)==59||input.LA(1)==61 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set375));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					}
					break;

			}

			pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor3324);
			arithmetic_primary376=arithmetic_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary376.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "arithmetic_factor"


	public static class arithmetic_primary_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "arithmetic_primary"
	// JPA2.g:359:1: arithmetic_primary : ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
		JPA2Parser.arithmetic_primary_return retval = new JPA2Parser.arithmetic_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal379=null;
		Token char_literal381=null;
		ParserRuleReturnScope path_expression377 =null;
		ParserRuleReturnScope numeric_literal378 =null;
		ParserRuleReturnScope arithmetic_expression380 =null;
		ParserRuleReturnScope input_parameter382 =null;
		ParserRuleReturnScope functions_returning_numerics383 =null;
		ParserRuleReturnScope aggregate_expression384 =null;
		ParserRuleReturnScope case_expression385 =null;
		ParserRuleReturnScope function_invocation386 =null;
		ParserRuleReturnScope extension_functions387 =null;
		ParserRuleReturnScope subquery388 =null;

		Object char_literal379_tree=null;
		Object char_literal381_tree=null;

		try {
			// JPA2.g:360:5: ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
			int alt102=10;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt102=1;
				}
				break;
			case INT_NUMERAL:
			case 64:
				{
				alt102=2;
				}
				break;
			case LPAREN:
				{
				int LA102_4 = input.LA(2);
				if ( (synpred175_JPA2()) ) {
					alt102=3;
				}
				else if ( (true) ) {
					alt102=10;
				}

				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt102=4;
				}
				break;
			case 78:
			case 104:
			case 108:
			case 110:
			case 113:
			case 126:
			case 128:
				{
				alt102=5;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt102=6;
				}
				break;
			case 102:
				{
				int LA102_17 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=6;
				}
				else if ( (synpred180_JPA2()) ) {
					alt102=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 102, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 84:
			case 86:
			case 118:
				{
				alt102=7;
				}
				break;
			case 76:
			case 85:
			case 100:
				{
				alt102=9;
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
					// JPA2.g:360:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_arithmetic_primary3335);
					path_expression377=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression377.getTree());

					}
					break;
				case 2 :
					// JPA2.g:361:7: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary3343);
					numeric_literal378=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal378.getTree());

					}
					break;
				case 3 :
					// JPA2.g:362:7: '(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal379=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary3351); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal379_tree = (Object)adaptor.create(char_literal379);
					adaptor.addChild(root_0, char_literal379_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_arithmetic_primary3352);
					arithmetic_expression380=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression380.getTree());

					char_literal381=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary3353); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal381_tree = (Object)adaptor.create(char_literal381);
					adaptor.addChild(root_0, char_literal381_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:363:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_arithmetic_primary3361);
					input_parameter382=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter382.getTree());

					}
					break;
				case 5 :
					// JPA2.g:364:7: functions_returning_numerics
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary3369);
					functions_returning_numerics383=functions_returning_numerics();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics383.getTree());

					}
					break;
				case 6 :
					// JPA2.g:365:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary3377);
					aggregate_expression384=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression384.getTree());

					}
					break;
				case 7 :
					// JPA2.g:366:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_arithmetic_primary3385);
					case_expression385=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression385.getTree());

					}
					break;
				case 8 :
					// JPA2.g:367:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_arithmetic_primary3393);
					function_invocation386=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation386.getTree());

					}
					break;
				case 9 :
					// JPA2.g:368:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_arithmetic_primary3401);
					extension_functions387=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions387.getTree());

					}
					break;
				case 10 :
					// JPA2.g:369:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_arithmetic_primary3409);
					subquery388=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery388.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "arithmetic_primary"


	public static class string_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "string_expression"
	// JPA2.g:370:1: string_expression : ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.string_expression_return string_expression() throws RecognitionException {
		JPA2Parser.string_expression_return retval = new JPA2Parser.string_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression389 =null;
		ParserRuleReturnScope string_literal390 =null;
		ParserRuleReturnScope input_parameter391 =null;
		ParserRuleReturnScope functions_returning_strings392 =null;
		ParserRuleReturnScope aggregate_expression393 =null;
		ParserRuleReturnScope case_expression394 =null;
		ParserRuleReturnScope function_invocation395 =null;
		ParserRuleReturnScope extension_functions396 =null;
		ParserRuleReturnScope subquery397 =null;


		try {
			// JPA2.g:371:5: ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
			int alt103=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt103=1;
				}
				break;
			case STRING_LITERAL:
				{
				alt103=2;
				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt103=3;
				}
				break;
			case LOWER:
			case 87:
			case 129:
			case 133:
			case 136:
				{
				alt103=4;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt103=5;
				}
				break;
			case 102:
				{
				int LA103_13 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt103=5;
				}
				else if ( (synpred188_JPA2()) ) {
					alt103=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 103, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 84:
			case 86:
			case 118:
				{
				alt103=6;
				}
				break;
			case 76:
			case 85:
			case 100:
				{
				alt103=8;
				}
				break;
			case LPAREN:
				{
				alt103=9;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 103, 0, input);
				throw nvae;
			}
			switch (alt103) {
				case 1 :
					// JPA2.g:371:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_string_expression3420);
					path_expression389=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression389.getTree());

					}
					break;
				case 2 :
					// JPA2.g:372:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_string_expression3428);
					string_literal390=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal390.getTree());

					}
					break;
				case 3 :
					// JPA2.g:373:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_string_expression3436);
					input_parameter391=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter391.getTree());

					}
					break;
				case 4 :
					// JPA2.g:374:7: functions_returning_strings
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_strings_in_string_expression3444);
					functions_returning_strings392=functions_returning_strings();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings392.getTree());

					}
					break;
				case 5 :
					// JPA2.g:375:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_string_expression3452);
					aggregate_expression393=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression393.getTree());

					}
					break;
				case 6 :
					// JPA2.g:376:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_string_expression3460);
					case_expression394=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression394.getTree());

					}
					break;
				case 7 :
					// JPA2.g:377:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_string_expression3468);
					function_invocation395=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation395.getTree());

					}
					break;
				case 8 :
					// JPA2.g:378:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_string_expression3476);
					extension_functions396=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions396.getTree());

					}
					break;
				case 9 :
					// JPA2.g:379:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_string_expression3484);
					subquery397=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery397.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "string_expression"


	public static class datetime_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "datetime_expression"
	// JPA2.g:380:1: datetime_expression : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery );
	public final JPA2Parser.datetime_expression_return datetime_expression() throws RecognitionException {
		JPA2Parser.datetime_expression_return retval = new JPA2Parser.datetime_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression398 =null;
		ParserRuleReturnScope input_parameter399 =null;
		ParserRuleReturnScope functions_returning_datetime400 =null;
		ParserRuleReturnScope aggregate_expression401 =null;
		ParserRuleReturnScope case_expression402 =null;
		ParserRuleReturnScope function_invocation403 =null;
		ParserRuleReturnScope extension_functions404 =null;
		ParserRuleReturnScope date_time_timestamp_literal405 =null;
		ParserRuleReturnScope subquery406 =null;


		try {
			// JPA2.g:381:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery )
			int alt104=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA104_1 = input.LA(2);
				if ( (synpred190_JPA2()) ) {
					alt104=1;
				}
				else if ( (synpred197_JPA2()) ) {
					alt104=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 104, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt104=2;
				}
				break;
			case 88:
			case 89:
			case 90:
				{
				alt104=3;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt104=4;
				}
				break;
			case 102:
				{
				int LA104_8 = input.LA(2);
				if ( (synpred193_JPA2()) ) {
					alt104=4;
				}
				else if ( (synpred195_JPA2()) ) {
					alt104=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 104, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 84:
			case 86:
			case 118:
				{
				alt104=5;
				}
				break;
			case 76:
			case 85:
			case 100:
				{
				alt104=7;
				}
				break;
			case LPAREN:
				{
				alt104=9;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 104, 0, input);
				throw nvae;
			}
			switch (alt104) {
				case 1 :
					// JPA2.g:381:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_datetime_expression3495);
					path_expression398=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression398.getTree());

					}
					break;
				case 2 :
					// JPA2.g:382:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_datetime_expression3503);
					input_parameter399=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter399.getTree());

					}
					break;
				case 3 :
					// JPA2.g:383:7: functions_returning_datetime
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_datetime_in_datetime_expression3511);
					functions_returning_datetime400=functions_returning_datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime400.getTree());

					}
					break;
				case 4 :
					// JPA2.g:384:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_datetime_expression3519);
					aggregate_expression401=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression401.getTree());

					}
					break;
				case 5 :
					// JPA2.g:385:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_datetime_expression3527);
					case_expression402=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression402.getTree());

					}
					break;
				case 6 :
					// JPA2.g:386:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_datetime_expression3535);
					function_invocation403=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation403.getTree());

					}
					break;
				case 7 :
					// JPA2.g:387:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_datetime_expression3543);
					extension_functions404=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions404.getTree());

					}
					break;
				case 8 :
					// JPA2.g:388:7: date_time_timestamp_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_time_timestamp_literal_in_datetime_expression3551);
					date_time_timestamp_literal405=date_time_timestamp_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_time_timestamp_literal405.getTree());

					}
					break;
				case 9 :
					// JPA2.g:389:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_datetime_expression3559);
					subquery406=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery406.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "datetime_expression"


	public static class boolean_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "boolean_expression"
	// JPA2.g:390:1: boolean_expression : ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.boolean_expression_return boolean_expression() throws RecognitionException {
		JPA2Parser.boolean_expression_return retval = new JPA2Parser.boolean_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression407 =null;
		ParserRuleReturnScope boolean_literal408 =null;
		ParserRuleReturnScope input_parameter409 =null;
		ParserRuleReturnScope case_expression410 =null;
		ParserRuleReturnScope function_invocation411 =null;
		ParserRuleReturnScope extension_functions412 =null;
		ParserRuleReturnScope subquery413 =null;


		try {
			// JPA2.g:391:5: ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery )
			int alt105=7;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt105=1;
				}
				break;
			case 142:
			case 143:
				{
				alt105=2;
				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt105=3;
				}
				break;
			case 84:
			case 86:
			case 118:
				{
				alt105=4;
				}
				break;
			case 102:
				{
				alt105=5;
				}
				break;
			case 76:
			case 85:
			case 100:
				{
				alt105=6;
				}
				break;
			case LPAREN:
				{
				alt105=7;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 105, 0, input);
				throw nvae;
			}
			switch (alt105) {
				case 1 :
					// JPA2.g:391:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_boolean_expression3570);
					path_expression407=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression407.getTree());

					}
					break;
				case 2 :
					// JPA2.g:392:7: boolean_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_literal_in_boolean_expression3578);
					boolean_literal408=boolean_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal408.getTree());

					}
					break;
				case 3 :
					// JPA2.g:393:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_boolean_expression3586);
					input_parameter409=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter409.getTree());

					}
					break;
				case 4 :
					// JPA2.g:394:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_boolean_expression3594);
					case_expression410=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression410.getTree());

					}
					break;
				case 5 :
					// JPA2.g:395:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_boolean_expression3602);
					function_invocation411=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation411.getTree());

					}
					break;
				case 6 :
					// JPA2.g:396:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_boolean_expression3610);
					extension_functions412=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions412.getTree());

					}
					break;
				case 7 :
					// JPA2.g:397:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_boolean_expression3618);
					subquery413=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery413.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "boolean_expression"


	public static class enum_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enum_expression"
	// JPA2.g:398:1: enum_expression : ( path_expression | enum_literal | input_parameter | case_expression | subquery );
	public final JPA2Parser.enum_expression_return enum_expression() throws RecognitionException {
		JPA2Parser.enum_expression_return retval = new JPA2Parser.enum_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression414 =null;
		ParserRuleReturnScope enum_literal415 =null;
		ParserRuleReturnScope input_parameter416 =null;
		ParserRuleReturnScope case_expression417 =null;
		ParserRuleReturnScope subquery418 =null;


		try {
			// JPA2.g:399:5: ( path_expression | enum_literal | input_parameter | case_expression | subquery )
			int alt106=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA106_1 = input.LA(2);
				if ( (LA106_1==62) ) {
					alt106=1;
				}
				else if ( (LA106_1==EOF||(LA106_1 >= AND && LA106_1 <= ASC)||LA106_1==DESC||(LA106_1 >= GROUP && LA106_1 <= HAVING)||LA106_1==INNER||(LA106_1 >= JOIN && LA106_1 <= LEFT)||(LA106_1 >= OR && LA106_1 <= ORDER)||LA106_1==RPAREN||LA106_1==SET||LA106_1==WORD||LA106_1==60||(LA106_1 >= 67 && LA106_1 <= 68)||LA106_1==81||LA106_1==93||LA106_1==95||LA106_1==101||LA106_1==130||(LA106_1 >= 139 && LA106_1 <= 140)) ) {
					alt106=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 106, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt106=3;
				}
				break;
			case 84:
			case 86:
			case 118:
				{
				alt106=4;
				}
				break;
			case LPAREN:
				{
				alt106=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 106, 0, input);
				throw nvae;
			}
			switch (alt106) {
				case 1 :
					// JPA2.g:399:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_enum_expression3629);
					path_expression414=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression414.getTree());

					}
					break;
				case 2 :
					// JPA2.g:400:7: enum_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_literal_in_enum_expression3637);
					enum_literal415=enum_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal415.getTree());

					}
					break;
				case 3 :
					// JPA2.g:401:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_enum_expression3645);
					input_parameter416=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter416.getTree());

					}
					break;
				case 4 :
					// JPA2.g:402:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_enum_expression3653);
					case_expression417=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression417.getTree());

					}
					break;
				case 5 :
					// JPA2.g:403:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_enum_expression3661);
					subquery418=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery418.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "enum_expression"


	public static class entity_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_expression"
	// JPA2.g:404:1: entity_expression : ( path_expression | simple_entity_expression );
	public final JPA2Parser.entity_expression_return entity_expression() throws RecognitionException {
		JPA2Parser.entity_expression_return retval = new JPA2Parser.entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression419 =null;
		ParserRuleReturnScope simple_entity_expression420 =null;


		try {
			// JPA2.g:405:5: ( path_expression | simple_entity_expression )
			int alt107=2;
			int LA107_0 = input.LA(1);
			if ( (LA107_0==WORD) ) {
				int LA107_1 = input.LA(2);
				if ( (LA107_1==62) ) {
					alt107=1;
				}
				else if ( (LA107_1==EOF||LA107_1==AND||(LA107_1 >= GROUP && LA107_1 <= HAVING)||LA107_1==INNER||(LA107_1 >= JOIN && LA107_1 <= LEFT)||(LA107_1 >= OR && LA107_1 <= ORDER)||LA107_1==RPAREN||LA107_1==SET||LA107_1==60||(LA107_1 >= 67 && LA107_1 <= 68)||LA107_1==130||LA107_1==140) ) {
					alt107=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 107, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA107_0==NAMED_PARAMETER||LA107_0==57||LA107_0==71) ) {
				alt107=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 107, 0, input);
				throw nvae;
			}

			switch (alt107) {
				case 1 :
					// JPA2.g:405:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_expression3672);
					path_expression419=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression419.getTree());

					}
					break;
				case 2 :
					// JPA2.g:406:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3680);
					simple_entity_expression420=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression420.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "entity_expression"


	public static class simple_entity_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_entity_expression"
	// JPA2.g:407:1: simple_entity_expression : ( identification_variable | input_parameter );
	public final JPA2Parser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
		JPA2Parser.simple_entity_expression_return retval = new JPA2Parser.simple_entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable421 =null;
		ParserRuleReturnScope input_parameter422 =null;


		try {
			// JPA2.g:408:5: ( identification_variable | input_parameter )
			int alt108=2;
			int LA108_0 = input.LA(1);
			if ( (LA108_0==WORD) ) {
				alt108=1;
			}
			else if ( (LA108_0==NAMED_PARAMETER||LA108_0==57||LA108_0==71) ) {
				alt108=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 108, 0, input);
				throw nvae;
			}

			switch (alt108) {
				case 1 :
					// JPA2.g:408:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3691);
					identification_variable421=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable421.getTree());

					}
					break;
				case 2 :
					// JPA2.g:409:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3699);
					input_parameter422=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter422.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "simple_entity_expression"


	public static class entity_type_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_type_expression"
	// JPA2.g:410:1: entity_type_expression : ( type_discriminator | entity_type_literal | input_parameter );
	public final JPA2Parser.entity_type_expression_return entity_type_expression() throws RecognitionException {
		JPA2Parser.entity_type_expression_return retval = new JPA2Parser.entity_type_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope type_discriminator423 =null;
		ParserRuleReturnScope entity_type_literal424 =null;
		ParserRuleReturnScope input_parameter425 =null;


		try {
			// JPA2.g:411:5: ( type_discriminator | entity_type_literal | input_parameter )
			int alt109=3;
			switch ( input.LA(1) ) {
			case 134:
				{
				alt109=1;
				}
				break;
			case WORD:
				{
				alt109=2;
				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt109=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 109, 0, input);
				throw nvae;
			}
			switch (alt109) {
				case 1 :
					// JPA2.g:411:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_entity_type_expression3710);
					type_discriminator423=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator423.getTree());

					}
					break;
				case 2 :
					// JPA2.g:412:7: entity_type_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_literal_in_entity_type_expression3718);
					entity_type_literal424=entity_type_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_literal424.getTree());

					}
					break;
				case 3 :
					// JPA2.g:413:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_entity_type_expression3726);
					input_parameter425=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter425.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "entity_type_expression"


	public static class type_discriminator_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "type_discriminator"
	// JPA2.g:414:1: type_discriminator : 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' ;
	public final JPA2Parser.type_discriminator_return type_discriminator() throws RecognitionException {
		JPA2Parser.type_discriminator_return retval = new JPA2Parser.type_discriminator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal426=null;
		Token char_literal430=null;
		ParserRuleReturnScope general_identification_variable427 =null;
		ParserRuleReturnScope path_expression428 =null;
		ParserRuleReturnScope input_parameter429 =null;

		Object string_literal426_tree=null;
		Object char_literal430_tree=null;

		try {
			// JPA2.g:415:5: ( 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' )
			// JPA2.g:415:7: 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal426=(Token)match(input,134,FOLLOW_134_in_type_discriminator3737); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal426_tree = (Object)adaptor.create(string_literal426);
			adaptor.addChild(root_0, string_literal426_tree);
			}

			// JPA2.g:415:15: ( general_identification_variable | path_expression | input_parameter )
			int alt110=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA110_1 = input.LA(2);
				if ( (LA110_1==RPAREN) ) {
					alt110=1;
				}
				else if ( (LA110_1==62) ) {
					alt110=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 110, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 106:
			case 137:
				{
				alt110=1;
				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt110=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 110, 0, input);
				throw nvae;
			}
			switch (alt110) {
				case 1 :
					// JPA2.g:415:16: general_identification_variable
					{
					pushFollow(FOLLOW_general_identification_variable_in_type_discriminator3740);
					general_identification_variable427=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable427.getTree());

					}
					break;
				case 2 :
					// JPA2.g:415:50: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_type_discriminator3744);
					path_expression428=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression428.getTree());

					}
					break;
				case 3 :
					// JPA2.g:415:68: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_type_discriminator3748);
					input_parameter429=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter429.getTree());

					}
					break;

			}

			char_literal430=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_type_discriminator3751); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal430_tree = (Object)adaptor.create(char_literal430);
			adaptor.addChild(root_0, char_literal430_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "type_discriminator"


	public static class functions_returning_numerics_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "functions_returning_numerics"
	// JPA2.g:416:1: functions_returning_numerics : ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' );
	public final JPA2Parser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
		JPA2Parser.functions_returning_numerics_return retval = new JPA2Parser.functions_returning_numerics_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal431=null;
		Token char_literal433=null;
		Token string_literal434=null;
		Token char_literal436=null;
		Token char_literal438=null;
		Token char_literal440=null;
		Token string_literal441=null;
		Token char_literal443=null;
		Token string_literal444=null;
		Token char_literal446=null;
		Token string_literal447=null;
		Token char_literal449=null;
		Token char_literal451=null;
		Token string_literal452=null;
		Token char_literal454=null;
		Token string_literal455=null;
		Token char_literal457=null;
		ParserRuleReturnScope string_expression432 =null;
		ParserRuleReturnScope string_expression435 =null;
		ParserRuleReturnScope string_expression437 =null;
		ParserRuleReturnScope arithmetic_expression439 =null;
		ParserRuleReturnScope arithmetic_expression442 =null;
		ParserRuleReturnScope arithmetic_expression445 =null;
		ParserRuleReturnScope arithmetic_expression448 =null;
		ParserRuleReturnScope arithmetic_expression450 =null;
		ParserRuleReturnScope path_expression453 =null;
		ParserRuleReturnScope identification_variable456 =null;

		Object string_literal431_tree=null;
		Object char_literal433_tree=null;
		Object string_literal434_tree=null;
		Object char_literal436_tree=null;
		Object char_literal438_tree=null;
		Object char_literal440_tree=null;
		Object string_literal441_tree=null;
		Object char_literal443_tree=null;
		Object string_literal444_tree=null;
		Object char_literal446_tree=null;
		Object string_literal447_tree=null;
		Object char_literal449_tree=null;
		Object char_literal451_tree=null;
		Object string_literal452_tree=null;
		Object char_literal454_tree=null;
		Object string_literal455_tree=null;
		Object char_literal457_tree=null;

		try {
			// JPA2.g:417:5: ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' )
			int alt112=7;
			switch ( input.LA(1) ) {
			case 108:
				{
				alt112=1;
				}
				break;
			case 110:
				{
				alt112=2;
				}
				break;
			case 78:
				{
				alt112=3;
				}
				break;
			case 128:
				{
				alt112=4;
				}
				break;
			case 113:
				{
				alt112=5;
				}
				break;
			case 126:
				{
				alt112=6;
				}
				break;
			case 104:
				{
				alt112=7;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 112, 0, input);
				throw nvae;
			}
			switch (alt112) {
				case 1 :
					// JPA2.g:417:7: 'LENGTH(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal431=(Token)match(input,108,FOLLOW_108_in_functions_returning_numerics3762); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal431_tree = (Object)adaptor.create(string_literal431);
					adaptor.addChild(root_0, string_literal431_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3763);
					string_expression432=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression432.getTree());

					char_literal433=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3764); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal433_tree = (Object)adaptor.create(char_literal433);
					adaptor.addChild(root_0, char_literal433_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:418:7: 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal434=(Token)match(input,110,FOLLOW_110_in_functions_returning_numerics3772); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal434_tree = (Object)adaptor.create(string_literal434);
					adaptor.addChild(root_0, string_literal434_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3774);
					string_expression435=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression435.getTree());

					char_literal436=(Token)match(input,60,FOLLOW_60_in_functions_returning_numerics3775); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal436_tree = (Object)adaptor.create(char_literal436);
					adaptor.addChild(root_0, char_literal436_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3777);
					string_expression437=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression437.getTree());

					// JPA2.g:418:55: ( ',' arithmetic_expression )?
					int alt111=2;
					int LA111_0 = input.LA(1);
					if ( (LA111_0==60) ) {
						alt111=1;
					}
					switch (alt111) {
						case 1 :
							// JPA2.g:418:56: ',' arithmetic_expression
							{
							char_literal438=(Token)match(input,60,FOLLOW_60_in_functions_returning_numerics3779); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal438_tree = (Object)adaptor.create(char_literal438);
							adaptor.addChild(root_0, char_literal438_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3780);
							arithmetic_expression439=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression439.getTree());

							}
							break;

					}

					char_literal440=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3783); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal440_tree = (Object)adaptor.create(char_literal440);
					adaptor.addChild(root_0, char_literal440_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:419:7: 'ABS(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal441=(Token)match(input,78,FOLLOW_78_in_functions_returning_numerics3791); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal441_tree = (Object)adaptor.create(string_literal441);
					adaptor.addChild(root_0, string_literal441_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3792);
					arithmetic_expression442=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression442.getTree());

					char_literal443=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3793); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal443_tree = (Object)adaptor.create(char_literal443);
					adaptor.addChild(root_0, char_literal443_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:420:7: 'SQRT(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal444=(Token)match(input,128,FOLLOW_128_in_functions_returning_numerics3801); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal444_tree = (Object)adaptor.create(string_literal444);
					adaptor.addChild(root_0, string_literal444_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3802);
					arithmetic_expression445=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression445.getTree());

					char_literal446=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3803); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal446_tree = (Object)adaptor.create(char_literal446);
					adaptor.addChild(root_0, char_literal446_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:421:7: 'MOD(' arithmetic_expression ',' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal447=(Token)match(input,113,FOLLOW_113_in_functions_returning_numerics3811); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal447_tree = (Object)adaptor.create(string_literal447);
					adaptor.addChild(root_0, string_literal447_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3812);
					arithmetic_expression448=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression448.getTree());

					char_literal449=(Token)match(input,60,FOLLOW_60_in_functions_returning_numerics3813); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal449_tree = (Object)adaptor.create(char_literal449);
					adaptor.addChild(root_0, char_literal449_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3815);
					arithmetic_expression450=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression450.getTree());

					char_literal451=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3816); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal451_tree = (Object)adaptor.create(char_literal451);
					adaptor.addChild(root_0, char_literal451_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:422:7: 'SIZE(' path_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal452=(Token)match(input,126,FOLLOW_126_in_functions_returning_numerics3824); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal452_tree = (Object)adaptor.create(string_literal452);
					adaptor.addChild(root_0, string_literal452_tree);
					}

					pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3825);
					path_expression453=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression453.getTree());

					char_literal454=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3826); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal454_tree = (Object)adaptor.create(char_literal454);
					adaptor.addChild(root_0, char_literal454_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:423:7: 'INDEX(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal455=(Token)match(input,104,FOLLOW_104_in_functions_returning_numerics3834); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal455_tree = (Object)adaptor.create(string_literal455);
					adaptor.addChild(root_0, string_literal455_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_functions_returning_numerics3835);
					identification_variable456=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable456.getTree());

					char_literal457=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3836); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal457_tree = (Object)adaptor.create(char_literal457);
					adaptor.addChild(root_0, char_literal457_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "functions_returning_numerics"


	public static class functions_returning_datetime_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "functions_returning_datetime"
	// JPA2.g:424:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
	public final JPA2Parser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
		JPA2Parser.functions_returning_datetime_return retval = new JPA2Parser.functions_returning_datetime_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set458=null;

		Object set458_tree=null;

		try {
			// JPA2.g:425:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set458=input.LT(1);
			if ( (input.LA(1) >= 88 && input.LA(1) <= 90) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set458));
				state.errorRecovery=false;
				state.failed=false;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "functions_returning_datetime"


	public static class functions_returning_strings_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "functions_returning_strings"
	// JPA2.g:428:1: functions_returning_strings : ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' );
	public final JPA2Parser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
		JPA2Parser.functions_returning_strings_return retval = new JPA2Parser.functions_returning_strings_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal459=null;
		Token char_literal461=null;
		Token char_literal463=null;
		Token char_literal465=null;
		Token string_literal466=null;
		Token char_literal468=null;
		Token char_literal470=null;
		Token char_literal472=null;
		Token string_literal473=null;
		Token string_literal476=null;
		Token char_literal478=null;
		Token string_literal479=null;
		Token char_literal480=null;
		Token char_literal482=null;
		Token string_literal483=null;
		Token char_literal485=null;
		ParserRuleReturnScope string_expression460 =null;
		ParserRuleReturnScope string_expression462 =null;
		ParserRuleReturnScope string_expression464 =null;
		ParserRuleReturnScope string_expression467 =null;
		ParserRuleReturnScope arithmetic_expression469 =null;
		ParserRuleReturnScope arithmetic_expression471 =null;
		ParserRuleReturnScope trim_specification474 =null;
		ParserRuleReturnScope trim_character475 =null;
		ParserRuleReturnScope string_expression477 =null;
		ParserRuleReturnScope string_expression481 =null;
		ParserRuleReturnScope string_expression484 =null;

		Object string_literal459_tree=null;
		Object char_literal461_tree=null;
		Object char_literal463_tree=null;
		Object char_literal465_tree=null;
		Object string_literal466_tree=null;
		Object char_literal468_tree=null;
		Object char_literal470_tree=null;
		Object char_literal472_tree=null;
		Object string_literal473_tree=null;
		Object string_literal476_tree=null;
		Object char_literal478_tree=null;
		Object string_literal479_tree=null;
		Object char_literal480_tree=null;
		Object char_literal482_tree=null;
		Object string_literal483_tree=null;
		Object char_literal485_tree=null;

		try {
			// JPA2.g:429:5: ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' )
			int alt118=5;
			switch ( input.LA(1) ) {
			case 87:
				{
				alt118=1;
				}
				break;
			case 129:
				{
				alt118=2;
				}
				break;
			case 133:
				{
				alt118=3;
				}
				break;
			case LOWER:
				{
				alt118=4;
				}
				break;
			case 136:
				{
				alt118=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 118, 0, input);
				throw nvae;
			}
			switch (alt118) {
				case 1 :
					// JPA2.g:429:7: 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal459=(Token)match(input,87,FOLLOW_87_in_functions_returning_strings3874); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal459_tree = (Object)adaptor.create(string_literal459);
					adaptor.addChild(root_0, string_literal459_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3875);
					string_expression460=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression460.getTree());

					char_literal461=(Token)match(input,60,FOLLOW_60_in_functions_returning_strings3876); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal461_tree = (Object)adaptor.create(char_literal461);
					adaptor.addChild(root_0, char_literal461_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3878);
					string_expression462=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression462.getTree());

					// JPA2.g:429:55: ( ',' string_expression )*
					loop113:
					while (true) {
						int alt113=2;
						int LA113_0 = input.LA(1);
						if ( (LA113_0==60) ) {
							alt113=1;
						}

						switch (alt113) {
						case 1 :
							// JPA2.g:429:56: ',' string_expression
							{
							char_literal463=(Token)match(input,60,FOLLOW_60_in_functions_returning_strings3881); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal463_tree = (Object)adaptor.create(char_literal463);
							adaptor.addChild(root_0, char_literal463_tree);
							}

							pushFollow(FOLLOW_string_expression_in_functions_returning_strings3883);
							string_expression464=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression464.getTree());

							}
							break;

						default :
							break loop113;
						}
					}

					char_literal465=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3886); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal465_tree = (Object)adaptor.create(char_literal465);
					adaptor.addChild(root_0, char_literal465_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:430:7: 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal466=(Token)match(input,129,FOLLOW_129_in_functions_returning_strings3894); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal466_tree = (Object)adaptor.create(string_literal466);
					adaptor.addChild(root_0, string_literal466_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3896);
					string_expression467=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression467.getTree());

					char_literal468=(Token)match(input,60,FOLLOW_60_in_functions_returning_strings3897); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal468_tree = (Object)adaptor.create(char_literal468);
					adaptor.addChild(root_0, char_literal468_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3899);
					arithmetic_expression469=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression469.getTree());

					// JPA2.g:430:63: ( ',' arithmetic_expression )?
					int alt114=2;
					int LA114_0 = input.LA(1);
					if ( (LA114_0==60) ) {
						alt114=1;
					}
					switch (alt114) {
						case 1 :
							// JPA2.g:430:64: ',' arithmetic_expression
							{
							char_literal470=(Token)match(input,60,FOLLOW_60_in_functions_returning_strings3902); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal470_tree = (Object)adaptor.create(char_literal470);
							adaptor.addChild(root_0, char_literal470_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3904);
							arithmetic_expression471=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression471.getTree());

							}
							break;

					}

					char_literal472=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3907); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal472_tree = (Object)adaptor.create(char_literal472);
					adaptor.addChild(root_0, char_literal472_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:431:7: 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal473=(Token)match(input,133,FOLLOW_133_in_functions_returning_strings3915); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal473_tree = (Object)adaptor.create(string_literal473);
					adaptor.addChild(root_0, string_literal473_tree);
					}

					// JPA2.g:431:14: ( ( trim_specification )? ( trim_character )? 'FROM' )?
					int alt117=2;
					int LA117_0 = input.LA(1);
					if ( (LA117_0==TRIM_CHARACTER||LA117_0==83||LA117_0==101||LA117_0==107||LA117_0==131) ) {
						alt117=1;
					}
					switch (alt117) {
						case 1 :
							// JPA2.g:431:15: ( trim_specification )? ( trim_character )? 'FROM'
							{
							// JPA2.g:431:15: ( trim_specification )?
							int alt115=2;
							int LA115_0 = input.LA(1);
							if ( (LA115_0==83||LA115_0==107||LA115_0==131) ) {
								alt115=1;
							}
							switch (alt115) {
								case 1 :
									// JPA2.g:431:16: trim_specification
									{
									pushFollow(FOLLOW_trim_specification_in_functions_returning_strings3918);
									trim_specification474=trim_specification();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification474.getTree());

									}
									break;

							}

							// JPA2.g:431:37: ( trim_character )?
							int alt116=2;
							int LA116_0 = input.LA(1);
							if ( (LA116_0==TRIM_CHARACTER) ) {
								alt116=1;
							}
							switch (alt116) {
								case 1 :
									// JPA2.g:431:38: trim_character
									{
									pushFollow(FOLLOW_trim_character_in_functions_returning_strings3923);
									trim_character475=trim_character();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_character475.getTree());

									}
									break;

							}

							string_literal476=(Token)match(input,101,FOLLOW_101_in_functions_returning_strings3927); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal476_tree = (Object)adaptor.create(string_literal476);
							adaptor.addChild(root_0, string_literal476_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3931);
					string_expression477=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression477.getTree());

					char_literal478=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3933); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal478_tree = (Object)adaptor.create(char_literal478);
					adaptor.addChild(root_0, char_literal478_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:432:7: 'LOWER' '(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal479=(Token)match(input,LOWER,FOLLOW_LOWER_in_functions_returning_strings3941); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal479_tree = (Object)adaptor.create(string_literal479);
					adaptor.addChild(root_0, string_literal479_tree);
					}

					char_literal480=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3943); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal480_tree = (Object)adaptor.create(char_literal480);
					adaptor.addChild(root_0, char_literal480_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3944);
					string_expression481=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression481.getTree());

					char_literal482=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3945); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal482_tree = (Object)adaptor.create(char_literal482);
					adaptor.addChild(root_0, char_literal482_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:433:7: 'UPPER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal483=(Token)match(input,136,FOLLOW_136_in_functions_returning_strings3953); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal483_tree = (Object)adaptor.create(string_literal483);
					adaptor.addChild(root_0, string_literal483_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3954);
					string_expression484=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression484.getTree());

					char_literal485=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3955); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal485_tree = (Object)adaptor.create(char_literal485);
					adaptor.addChild(root_0, char_literal485_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "functions_returning_strings"


	public static class trim_specification_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "trim_specification"
	// JPA2.g:434:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
	public final JPA2Parser.trim_specification_return trim_specification() throws RecognitionException {
		JPA2Parser.trim_specification_return retval = new JPA2Parser.trim_specification_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set486=null;

		Object set486_tree=null;

		try {
			// JPA2.g:435:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set486=input.LT(1);
			if ( input.LA(1)==83||input.LA(1)==107||input.LA(1)==131 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set486));
				state.errorRecovery=false;
				state.failed=false;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "trim_specification"


	public static class function_invocation_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "function_invocation"
	// JPA2.g:436:1: function_invocation : 'FUNCTION(' function_name ( ',' function_arg )* ')' ;
	public final JPA2Parser.function_invocation_return function_invocation() throws RecognitionException {
		JPA2Parser.function_invocation_return retval = new JPA2Parser.function_invocation_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal487=null;
		Token char_literal489=null;
		Token char_literal491=null;
		ParserRuleReturnScope function_name488 =null;
		ParserRuleReturnScope function_arg490 =null;

		Object string_literal487_tree=null;
		Object char_literal489_tree=null;
		Object char_literal491_tree=null;

		try {
			// JPA2.g:437:5: ( 'FUNCTION(' function_name ( ',' function_arg )* ')' )
			// JPA2.g:437:7: 'FUNCTION(' function_name ( ',' function_arg )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal487=(Token)match(input,102,FOLLOW_102_in_function_invocation3985); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal487_tree = (Object)adaptor.create(string_literal487);
			adaptor.addChild(root_0, string_literal487_tree);
			}

			pushFollow(FOLLOW_function_name_in_function_invocation3986);
			function_name488=function_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_name488.getTree());

			// JPA2.g:437:32: ( ',' function_arg )*
			loop119:
			while (true) {
				int alt119=2;
				int LA119_0 = input.LA(1);
				if ( (LA119_0==60) ) {
					alt119=1;
				}

				switch (alt119) {
				case 1 :
					// JPA2.g:437:33: ',' function_arg
					{
					char_literal489=(Token)match(input,60,FOLLOW_60_in_function_invocation3989); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal489_tree = (Object)adaptor.create(char_literal489);
					adaptor.addChild(root_0, char_literal489_tree);
					}

					pushFollow(FOLLOW_function_arg_in_function_invocation3991);
					function_arg490=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg490.getTree());

					}
					break;

				default :
					break loop119;
				}
			}

			char_literal491=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function_invocation3995); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal491_tree = (Object)adaptor.create(char_literal491);
			adaptor.addChild(root_0, char_literal491_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "function_invocation"


	public static class function_arg_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "function_arg"
	// JPA2.g:438:1: function_arg : ( literal | path_expression | input_parameter | scalar_expression );
	public final JPA2Parser.function_arg_return function_arg() throws RecognitionException {
		JPA2Parser.function_arg_return retval = new JPA2Parser.function_arg_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal492 =null;
		ParserRuleReturnScope path_expression493 =null;
		ParserRuleReturnScope input_parameter494 =null;
		ParserRuleReturnScope scalar_expression495 =null;


		try {
			// JPA2.g:439:5: ( literal | path_expression | input_parameter | scalar_expression )
			int alt120=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA120_1 = input.LA(2);
				if ( (LA120_1==62) ) {
					alt120=2;
				}
				else if ( (synpred235_JPA2()) ) {
					alt120=1;
				}
				else if ( (true) ) {
					alt120=4;
				}

				}
				break;
			case 71:
				{
				int LA120_2 = input.LA(2);
				if ( (LA120_2==64) ) {
					int LA120_8 = input.LA(3);
					if ( (LA120_8==INT_NUMERAL) ) {
						int LA120_12 = input.LA(4);
						if ( (synpred237_JPA2()) ) {
							alt120=3;
						}
						else if ( (true) ) {
							alt120=4;
						}

					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 120, 8, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA120_2==INT_NUMERAL) ) {
					int LA120_9 = input.LA(3);
					if ( (synpred237_JPA2()) ) {
						alt120=3;
					}
					else if ( (true) ) {
						alt120=4;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 120, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA120_3 = input.LA(2);
				if ( (synpred237_JPA2()) ) {
					alt120=3;
				}
				else if ( (true) ) {
					alt120=4;
				}

				}
				break;
			case 57:
				{
				int LA120_4 = input.LA(2);
				if ( (LA120_4==WORD) ) {
					int LA120_11 = input.LA(3);
					if ( (LA120_11==144) ) {
						int LA120_13 = input.LA(4);
						if ( (synpred237_JPA2()) ) {
							alt120=3;
						}
						else if ( (true) ) {
							alt120=4;
						}

					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 120, 11, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 120, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case AVG:
			case COUNT:
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case MAX:
			case MIN:
			case STRING_LITERAL:
			case SUM:
			case 59:
			case 61:
			case 64:
			case 76:
			case 78:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 100:
			case 102:
			case 104:
			case 108:
			case 110:
			case 113:
			case 118:
			case 126:
			case 128:
			case 129:
			case 133:
			case 134:
			case 136:
			case 142:
			case 143:
				{
				alt120=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 120, 0, input);
				throw nvae;
			}
			switch (alt120) {
				case 1 :
					// JPA2.g:439:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_function_arg4006);
					literal492=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal492.getTree());

					}
					break;
				case 2 :
					// JPA2.g:440:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_function_arg4014);
					path_expression493=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression493.getTree());

					}
					break;
				case 3 :
					// JPA2.g:441:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_function_arg4022);
					input_parameter494=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter494.getTree());

					}
					break;
				case 4 :
					// JPA2.g:442:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_function_arg4030);
					scalar_expression495=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression495.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "function_arg"


	public static class case_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "case_expression"
	// JPA2.g:443:1: case_expression : ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression );
	public final JPA2Parser.case_expression_return case_expression() throws RecognitionException {
		JPA2Parser.case_expression_return retval = new JPA2Parser.case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope general_case_expression496 =null;
		ParserRuleReturnScope simple_case_expression497 =null;
		ParserRuleReturnScope coalesce_expression498 =null;
		ParserRuleReturnScope nullif_expression499 =null;


		try {
			// JPA2.g:444:5: ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression )
			int alt121=4;
			switch ( input.LA(1) ) {
			case 84:
				{
				int LA121_1 = input.LA(2);
				if ( (LA121_1==139) ) {
					alt121=1;
				}
				else if ( (LA121_1==WORD||LA121_1==134) ) {
					alt121=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 121, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 86:
				{
				alt121=3;
				}
				break;
			case 118:
				{
				alt121=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 121, 0, input);
				throw nvae;
			}
			switch (alt121) {
				case 1 :
					// JPA2.g:444:7: general_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_case_expression_in_case_expression4041);
					general_case_expression496=general_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_case_expression496.getTree());

					}
					break;
				case 2 :
					// JPA2.g:445:7: simple_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_case_expression_in_case_expression4049);
					simple_case_expression497=simple_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_case_expression497.getTree());

					}
					break;
				case 3 :
					// JPA2.g:446:7: coalesce_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_coalesce_expression_in_case_expression4057);
					coalesce_expression498=coalesce_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, coalesce_expression498.getTree());

					}
					break;
				case 4 :
					// JPA2.g:447:7: nullif_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_nullif_expression_in_case_expression4065);
					nullif_expression499=nullif_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, nullif_expression499.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "case_expression"


	public static class general_case_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "general_case_expression"
	// JPA2.g:448:1: general_case_expression : 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.general_case_expression_return general_case_expression() throws RecognitionException {
		JPA2Parser.general_case_expression_return retval = new JPA2Parser.general_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal500=null;
		Token string_literal503=null;
		Token string_literal505=null;
		ParserRuleReturnScope when_clause501 =null;
		ParserRuleReturnScope when_clause502 =null;
		ParserRuleReturnScope scalar_expression504 =null;

		Object string_literal500_tree=null;
		Object string_literal503_tree=null;
		Object string_literal505_tree=null;

		try {
			// JPA2.g:449:5: ( 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:449:7: 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal500=(Token)match(input,84,FOLLOW_84_in_general_case_expression4076); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal500_tree = (Object)adaptor.create(string_literal500);
			adaptor.addChild(root_0, string_literal500_tree);
			}

			pushFollow(FOLLOW_when_clause_in_general_case_expression4078);
			when_clause501=when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause501.getTree());

			// JPA2.g:449:26: ( when_clause )*
			loop122:
			while (true) {
				int alt122=2;
				int LA122_0 = input.LA(1);
				if ( (LA122_0==139) ) {
					alt122=1;
				}

				switch (alt122) {
				case 1 :
					// JPA2.g:449:27: when_clause
					{
					pushFollow(FOLLOW_when_clause_in_general_case_expression4081);
					when_clause502=when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause502.getTree());

					}
					break;

				default :
					break loop122;
				}
			}

			string_literal503=(Token)match(input,93,FOLLOW_93_in_general_case_expression4085); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal503_tree = (Object)adaptor.create(string_literal503);
			adaptor.addChild(root_0, string_literal503_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_general_case_expression4087);
			scalar_expression504=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression504.getTree());

			string_literal505=(Token)match(input,95,FOLLOW_95_in_general_case_expression4089); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal505_tree = (Object)adaptor.create(string_literal505);
			adaptor.addChild(root_0, string_literal505_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "general_case_expression"


	public static class when_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "when_clause"
	// JPA2.g:450:1: when_clause : 'WHEN' conditional_expression 'THEN' scalar_expression ;
	public final JPA2Parser.when_clause_return when_clause() throws RecognitionException {
		JPA2Parser.when_clause_return retval = new JPA2Parser.when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal506=null;
		Token string_literal508=null;
		ParserRuleReturnScope conditional_expression507 =null;
		ParserRuleReturnScope scalar_expression509 =null;

		Object string_literal506_tree=null;
		Object string_literal508_tree=null;

		try {
			// JPA2.g:451:5: ( 'WHEN' conditional_expression 'THEN' scalar_expression )
			// JPA2.g:451:7: 'WHEN' conditional_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal506=(Token)match(input,139,FOLLOW_139_in_when_clause4100); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal506_tree = (Object)adaptor.create(string_literal506);
			adaptor.addChild(root_0, string_literal506_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_when_clause4102);
			conditional_expression507=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression507.getTree());

			string_literal508=(Token)match(input,130,FOLLOW_130_in_when_clause4104); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal508_tree = (Object)adaptor.create(string_literal508);
			adaptor.addChild(root_0, string_literal508_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_when_clause4106);
			scalar_expression509=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression509.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "when_clause"


	public static class simple_case_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_case_expression"
	// JPA2.g:452:1: simple_case_expression : 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.simple_case_expression_return simple_case_expression() throws RecognitionException {
		JPA2Parser.simple_case_expression_return retval = new JPA2Parser.simple_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal510=null;
		Token string_literal514=null;
		Token string_literal516=null;
		ParserRuleReturnScope case_operand511 =null;
		ParserRuleReturnScope simple_when_clause512 =null;
		ParserRuleReturnScope simple_when_clause513 =null;
		ParserRuleReturnScope scalar_expression515 =null;

		Object string_literal510_tree=null;
		Object string_literal514_tree=null;
		Object string_literal516_tree=null;

		try {
			// JPA2.g:453:5: ( 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:453:7: 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal510=(Token)match(input,84,FOLLOW_84_in_simple_case_expression4117); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal510_tree = (Object)adaptor.create(string_literal510);
			adaptor.addChild(root_0, string_literal510_tree);
			}

			pushFollow(FOLLOW_case_operand_in_simple_case_expression4119);
			case_operand511=case_operand();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, case_operand511.getTree());

			pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4121);
			simple_when_clause512=simple_when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause512.getTree());

			// JPA2.g:453:46: ( simple_when_clause )*
			loop123:
			while (true) {
				int alt123=2;
				int LA123_0 = input.LA(1);
				if ( (LA123_0==139) ) {
					alt123=1;
				}

				switch (alt123) {
				case 1 :
					// JPA2.g:453:47: simple_when_clause
					{
					pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4124);
					simple_when_clause513=simple_when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause513.getTree());

					}
					break;

				default :
					break loop123;
				}
			}

			string_literal514=(Token)match(input,93,FOLLOW_93_in_simple_case_expression4128); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal514_tree = (Object)adaptor.create(string_literal514);
			adaptor.addChild(root_0, string_literal514_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_case_expression4130);
			scalar_expression515=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression515.getTree());

			string_literal516=(Token)match(input,95,FOLLOW_95_in_simple_case_expression4132); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal516_tree = (Object)adaptor.create(string_literal516);
			adaptor.addChild(root_0, string_literal516_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "simple_case_expression"


	public static class case_operand_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "case_operand"
	// JPA2.g:454:1: case_operand : ( path_expression | type_discriminator );
	public final JPA2Parser.case_operand_return case_operand() throws RecognitionException {
		JPA2Parser.case_operand_return retval = new JPA2Parser.case_operand_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression517 =null;
		ParserRuleReturnScope type_discriminator518 =null;


		try {
			// JPA2.g:455:5: ( path_expression | type_discriminator )
			int alt124=2;
			int LA124_0 = input.LA(1);
			if ( (LA124_0==WORD) ) {
				alt124=1;
			}
			else if ( (LA124_0==134) ) {
				alt124=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 124, 0, input);
				throw nvae;
			}

			switch (alt124) {
				case 1 :
					// JPA2.g:455:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_case_operand4143);
					path_expression517=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression517.getTree());

					}
					break;
				case 2 :
					// JPA2.g:456:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_case_operand4151);
					type_discriminator518=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator518.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "case_operand"


	public static class simple_when_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "simple_when_clause"
	// JPA2.g:457:1: simple_when_clause : 'WHEN' scalar_expression 'THEN' scalar_expression ;
	public final JPA2Parser.simple_when_clause_return simple_when_clause() throws RecognitionException {
		JPA2Parser.simple_when_clause_return retval = new JPA2Parser.simple_when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal519=null;
		Token string_literal521=null;
		ParserRuleReturnScope scalar_expression520 =null;
		ParserRuleReturnScope scalar_expression522 =null;

		Object string_literal519_tree=null;
		Object string_literal521_tree=null;

		try {
			// JPA2.g:458:5: ( 'WHEN' scalar_expression 'THEN' scalar_expression )
			// JPA2.g:458:7: 'WHEN' scalar_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal519=(Token)match(input,139,FOLLOW_139_in_simple_when_clause4162); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal519_tree = (Object)adaptor.create(string_literal519);
			adaptor.addChild(root_0, string_literal519_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4164);
			scalar_expression520=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression520.getTree());

			string_literal521=(Token)match(input,130,FOLLOW_130_in_simple_when_clause4166); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal521_tree = (Object)adaptor.create(string_literal521);
			adaptor.addChild(root_0, string_literal521_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4168);
			scalar_expression522=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression522.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "simple_when_clause"


	public static class coalesce_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "coalesce_expression"
	// JPA2.g:459:1: coalesce_expression : 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' ;
	public final JPA2Parser.coalesce_expression_return coalesce_expression() throws RecognitionException {
		JPA2Parser.coalesce_expression_return retval = new JPA2Parser.coalesce_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal523=null;
		Token char_literal525=null;
		Token char_literal527=null;
		ParserRuleReturnScope scalar_expression524 =null;
		ParserRuleReturnScope scalar_expression526 =null;

		Object string_literal523_tree=null;
		Object char_literal525_tree=null;
		Object char_literal527_tree=null;

		try {
			// JPA2.g:460:5: ( 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' )
			// JPA2.g:460:7: 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal523=(Token)match(input,86,FOLLOW_86_in_coalesce_expression4179); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal523_tree = (Object)adaptor.create(string_literal523);
			adaptor.addChild(root_0, string_literal523_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4180);
			scalar_expression524=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression524.getTree());

			// JPA2.g:460:36: ( ',' scalar_expression )+
			int cnt125=0;
			loop125:
			while (true) {
				int alt125=2;
				int LA125_0 = input.LA(1);
				if ( (LA125_0==60) ) {
					alt125=1;
				}

				switch (alt125) {
				case 1 :
					// JPA2.g:460:37: ',' scalar_expression
					{
					char_literal525=(Token)match(input,60,FOLLOW_60_in_coalesce_expression4183); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal525_tree = (Object)adaptor.create(char_literal525);
					adaptor.addChild(root_0, char_literal525_tree);
					}

					pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4185);
					scalar_expression526=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression526.getTree());

					}
					break;

				default :
					if ( cnt125 >= 1 ) break loop125;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(125, input);
					throw eee;
				}
				cnt125++;
			}

			char_literal527=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_coalesce_expression4188); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal527_tree = (Object)adaptor.create(char_literal527);
			adaptor.addChild(root_0, char_literal527_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "coalesce_expression"


	public static class nullif_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "nullif_expression"
	// JPA2.g:461:1: nullif_expression : 'NULLIF(' scalar_expression ',' scalar_expression ')' ;
	public final JPA2Parser.nullif_expression_return nullif_expression() throws RecognitionException {
		JPA2Parser.nullif_expression_return retval = new JPA2Parser.nullif_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal528=null;
		Token char_literal530=null;
		Token char_literal532=null;
		ParserRuleReturnScope scalar_expression529 =null;
		ParserRuleReturnScope scalar_expression531 =null;

		Object string_literal528_tree=null;
		Object char_literal530_tree=null;
		Object char_literal532_tree=null;

		try {
			// JPA2.g:462:5: ( 'NULLIF(' scalar_expression ',' scalar_expression ')' )
			// JPA2.g:462:7: 'NULLIF(' scalar_expression ',' scalar_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal528=(Token)match(input,118,FOLLOW_118_in_nullif_expression4199); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal528_tree = (Object)adaptor.create(string_literal528);
			adaptor.addChild(root_0, string_literal528_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4200);
			scalar_expression529=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression529.getTree());

			char_literal530=(Token)match(input,60,FOLLOW_60_in_nullif_expression4202); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal530_tree = (Object)adaptor.create(char_literal530);
			adaptor.addChild(root_0, char_literal530_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4204);
			scalar_expression531=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression531.getTree());

			char_literal532=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nullif_expression4205); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal532_tree = (Object)adaptor.create(char_literal532);
			adaptor.addChild(root_0, char_literal532_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "nullif_expression"


	public static class extension_functions_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "extension_functions"
	// JPA2.g:464:1: extension_functions : ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | 'EXTRACT(' date_part 'FROM' function_arg ')' | '@ENUM' '(' enum_value_literal ')' -> ^( T_ENUM_MACROS[$enum_value_literal.text] ) );
	public final JPA2Parser.extension_functions_return extension_functions() throws RecognitionException {
		JPA2Parser.extension_functions_return retval = new JPA2Parser.extension_functions_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal533=null;
		Token WORD535=null;
		Token char_literal536=null;
		Token INT_NUMERAL537=null;
		Token char_literal538=null;
		Token INT_NUMERAL539=null;
		Token char_literal540=null;
		Token char_literal541=null;
		Token string_literal542=null;
		Token string_literal544=null;
		Token char_literal546=null;
		Token string_literal547=null;
		Token char_literal548=null;
		Token char_literal550=null;
		ParserRuleReturnScope function_arg534 =null;
		ParserRuleReturnScope date_part543 =null;
		ParserRuleReturnScope function_arg545 =null;
		ParserRuleReturnScope enum_value_literal549 =null;

		Object string_literal533_tree=null;
		Object WORD535_tree=null;
		Object char_literal536_tree=null;
		Object INT_NUMERAL537_tree=null;
		Object char_literal538_tree=null;
		Object INT_NUMERAL539_tree=null;
		Object char_literal540_tree=null;
		Object char_literal541_tree=null;
		Object string_literal542_tree=null;
		Object string_literal544_tree=null;
		Object char_literal546_tree=null;
		Object string_literal547_tree=null;
		Object char_literal548_tree=null;
		Object char_literal550_tree=null;
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");
		RewriteRuleSubtreeStream stream_enum_value_literal=new RewriteRuleSubtreeStream(adaptor,"rule enum_value_literal");

		try {
			// JPA2.g:465:5: ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | 'EXTRACT(' date_part 'FROM' function_arg ')' | '@ENUM' '(' enum_value_literal ')' -> ^( T_ENUM_MACROS[$enum_value_literal.text] ) )
			int alt128=3;
			switch ( input.LA(1) ) {
			case 85:
				{
				alt128=1;
				}
				break;
			case 100:
				{
				alt128=2;
				}
				break;
			case 76:
				{
				alt128=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 128, 0, input);
				throw nvae;
			}
			switch (alt128) {
				case 1 :
					// JPA2.g:465:7: 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal533=(Token)match(input,85,FOLLOW_85_in_extension_functions4217); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal533_tree = (Object)adaptor.create(string_literal533);
					adaptor.addChild(root_0, string_literal533_tree);
					}

					pushFollow(FOLLOW_function_arg_in_extension_functions4219);
					function_arg534=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg534.getTree());

					WORD535=(Token)match(input,WORD,FOLLOW_WORD_in_extension_functions4221); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD535_tree = (Object)adaptor.create(WORD535);
					adaptor.addChild(root_0, WORD535_tree);
					}

					// JPA2.g:465:33: ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )*
					loop127:
					while (true) {
						int alt127=2;
						int LA127_0 = input.LA(1);
						if ( (LA127_0==LPAREN) ) {
							alt127=1;
						}

						switch (alt127) {
						case 1 :
							// JPA2.g:465:34: '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')'
							{
							char_literal536=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_extension_functions4224); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal536_tree = (Object)adaptor.create(char_literal536);
							adaptor.addChild(root_0, char_literal536_tree);
							}

							INT_NUMERAL537=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4225); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							INT_NUMERAL537_tree = (Object)adaptor.create(INT_NUMERAL537);
							adaptor.addChild(root_0, INT_NUMERAL537_tree);
							}

							// JPA2.g:465:49: ( ',' INT_NUMERAL )*
							loop126:
							while (true) {
								int alt126=2;
								int LA126_0 = input.LA(1);
								if ( (LA126_0==60) ) {
									alt126=1;
								}

								switch (alt126) {
								case 1 :
									// JPA2.g:465:50: ',' INT_NUMERAL
									{
									char_literal538=(Token)match(input,60,FOLLOW_60_in_extension_functions4228); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									char_literal538_tree = (Object)adaptor.create(char_literal538);
									adaptor.addChild(root_0, char_literal538_tree);
									}

									INT_NUMERAL539=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4230); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									INT_NUMERAL539_tree = (Object)adaptor.create(INT_NUMERAL539);
									adaptor.addChild(root_0, INT_NUMERAL539_tree);
									}

									}
									break;

								default :
									break loop126;
								}
							}

							char_literal540=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4235); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal540_tree = (Object)adaptor.create(char_literal540);
							adaptor.addChild(root_0, char_literal540_tree);
							}

							}
							break;

						default :
							break loop127;
						}
					}

					char_literal541=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4239); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal541_tree = (Object)adaptor.create(char_literal541);
					adaptor.addChild(root_0, char_literal541_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:466:7: 'EXTRACT(' date_part 'FROM' function_arg ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal542=(Token)match(input,100,FOLLOW_100_in_extension_functions4247); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal542_tree = (Object)adaptor.create(string_literal542);
					adaptor.addChild(root_0, string_literal542_tree);
					}

					pushFollow(FOLLOW_date_part_in_extension_functions4249);
					date_part543=date_part();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part543.getTree());

					string_literal544=(Token)match(input,101,FOLLOW_101_in_extension_functions4251); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal544_tree = (Object)adaptor.create(string_literal544);
					adaptor.addChild(root_0, string_literal544_tree);
					}

					pushFollow(FOLLOW_function_arg_in_extension_functions4253);
					function_arg545=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg545.getTree());

					char_literal546=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4255); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal546_tree = (Object)adaptor.create(char_literal546);
					adaptor.addChild(root_0, char_literal546_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:467:7: '@ENUM' '(' enum_value_literal ')'
					{
					string_literal547=(Token)match(input,76,FOLLOW_76_in_extension_functions4263); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_76.add(string_literal547);

					char_literal548=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_extension_functions4265); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal548);

					pushFollow(FOLLOW_enum_value_literal_in_extension_functions4267);
					enum_value_literal549=enum_value_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_enum_value_literal.add(enum_value_literal549.getTree());
					char_literal550=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4269); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal550);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 467:42: -> ^( T_ENUM_MACROS[$enum_value_literal.text] )
					{
						// JPA2.g:467:45: ^( T_ENUM_MACROS[$enum_value_literal.text] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new EnumConditionNode(T_ENUM_MACROS, (enum_value_literal549!=null?input.toString(enum_value_literal549.start,enum_value_literal549.stop):null)), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "extension_functions"


	public static class date_part_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_part"
	// JPA2.g:469:10: fragment date_part : ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' );
	public final JPA2Parser.date_part_return date_part() throws RecognitionException {
		JPA2Parser.date_part_return retval = new JPA2Parser.date_part_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set551=null;

		Object set551_tree=null;

		try {
			// JPA2.g:470:5: ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set551=input.LT(1);
			if ( input.LA(1)==91||input.LA(1)==97||input.LA(1)==103||input.LA(1)==112||input.LA(1)==114||input.LA(1)==122||input.LA(1)==124||input.LA(1)==138||input.LA(1)==141 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set551));
				state.errorRecovery=false;
				state.failed=false;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "date_part"


	public static class input_parameter_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "input_parameter"
	// JPA2.g:473:1: input_parameter : ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) );
	public final JPA2Parser.input_parameter_return input_parameter() throws RecognitionException {
		JPA2Parser.input_parameter_return retval = new JPA2Parser.input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal552=null;
		Token NAMED_PARAMETER554=null;
		Token string_literal555=null;
		Token WORD556=null;
		Token char_literal557=null;
		ParserRuleReturnScope numeric_literal553 =null;

		Object char_literal552_tree=null;
		Object NAMED_PARAMETER554_tree=null;
		Object string_literal555_tree=null;
		Object WORD556_tree=null;
		Object char_literal557_tree=null;
		RewriteRuleTokenStream stream_144=new RewriteRuleTokenStream(adaptor,"token 144");
		RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
		RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
		RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");
		RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
		RewriteRuleSubtreeStream stream_numeric_literal=new RewriteRuleSubtreeStream(adaptor,"rule numeric_literal");

		try {
			// JPA2.g:474:5: ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
			int alt129=3;
			switch ( input.LA(1) ) {
			case 71:
				{
				alt129=1;
				}
				break;
			case NAMED_PARAMETER:
				{
				alt129=2;
				}
				break;
			case 57:
				{
				alt129=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 129, 0, input);
				throw nvae;
			}
			switch (alt129) {
				case 1 :
					// JPA2.g:474:7: '?' numeric_literal
					{
					char_literal552=(Token)match(input,71,FOLLOW_71_in_input_parameter4336); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_71.add(char_literal552);

					pushFollow(FOLLOW_numeric_literal_in_input_parameter4338);
					numeric_literal553=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_literal.add(numeric_literal553.getTree());
					// AST REWRITE
					// elements: numeric_literal, 71
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 474:27: -> ^( T_PARAMETER[] '?' numeric_literal )
					{
						// JPA2.g:474:30: ^( T_PARAMETER[] '?' numeric_literal )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_71.nextNode());
						adaptor.addChild(root_1, stream_numeric_literal.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// JPA2.g:475:7: NAMED_PARAMETER
					{
					NAMED_PARAMETER554=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter4361); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER554);

					// AST REWRITE
					// elements: NAMED_PARAMETER
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 475:23: -> ^( T_PARAMETER[] NAMED_PARAMETER )
					{
						// JPA2.g:475:26: ^( T_PARAMETER[] NAMED_PARAMETER )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_NAMED_PARAMETER.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// JPA2.g:476:7: '${' WORD '}'
					{
					string_literal555=(Token)match(input,57,FOLLOW_57_in_input_parameter4382); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_57.add(string_literal555);

					WORD556=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter4384); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WORD.add(WORD556);

					char_literal557=(Token)match(input,144,FOLLOW_144_in_input_parameter4386); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_144.add(char_literal557);

					// AST REWRITE
					// elements: 57, WORD, 144
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 476:21: -> ^( T_PARAMETER[] '${' WORD '}' )
					{
						// JPA2.g:476:24: ^( T_PARAMETER[] '${' WORD '}' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_57.nextNode());
						adaptor.addChild(root_1, stream_WORD.nextNode());
						adaptor.addChild(root_1, stream_144.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "input_parameter"


	public static class literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "literal"
	// JPA2.g:478:1: literal : WORD ;
	public final JPA2Parser.literal_return literal() throws RecognitionException {
		JPA2Parser.literal_return retval = new JPA2Parser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD558=null;

		Object WORD558_tree=null;

		try {
			// JPA2.g:479:5: ( WORD )
			// JPA2.g:479:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD558=(Token)match(input,WORD,FOLLOW_WORD_in_literal4414); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD558_tree = (Object)adaptor.create(WORD558);
			adaptor.addChild(root_0, WORD558_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "literal"


	public static class constructor_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "constructor_name"
	// JPA2.g:481:1: constructor_name : WORD ;
	public final JPA2Parser.constructor_name_return constructor_name() throws RecognitionException {
		JPA2Parser.constructor_name_return retval = new JPA2Parser.constructor_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD559=null;

		Object WORD559_tree=null;

		try {
			// JPA2.g:482:5: ( WORD )
			// JPA2.g:482:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD559=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4426); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD559_tree = (Object)adaptor.create(WORD559);
			adaptor.addChild(root_0, WORD559_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "constructor_name"


	public static class enum_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enum_literal"
	// JPA2.g:484:1: enum_literal : WORD ;
	public final JPA2Parser.enum_literal_return enum_literal() throws RecognitionException {
		JPA2Parser.enum_literal_return retval = new JPA2Parser.enum_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD560=null;

		Object WORD560_tree=null;

		try {
			// JPA2.g:485:5: ( WORD )
			// JPA2.g:485:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD560=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal4438); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD560_tree = (Object)adaptor.create(WORD560);
			adaptor.addChild(root_0, WORD560_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "enum_literal"


	public static class boolean_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "boolean_literal"
	// JPA2.g:487:1: boolean_literal : ( 'true' | 'false' );
	public final JPA2Parser.boolean_literal_return boolean_literal() throws RecognitionException {
		JPA2Parser.boolean_literal_return retval = new JPA2Parser.boolean_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set561=null;

		Object set561_tree=null;

		try {
			// JPA2.g:488:5: ( 'true' | 'false' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set561=input.LT(1);
			if ( (input.LA(1) >= 142 && input.LA(1) <= 143) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set561));
				state.errorRecovery=false;
				state.failed=false;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "boolean_literal"


	public static class field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "field"
	// JPA2.g:492:1: field : ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | date_part );
	public final JPA2Parser.field_return field() throws RecognitionException {
		JPA2Parser.field_return retval = new JPA2Parser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD562=null;
		Token string_literal563=null;
		Token string_literal564=null;
		Token string_literal565=null;
		Token string_literal566=null;
		Token string_literal567=null;
		Token string_literal568=null;
		Token string_literal569=null;
		Token string_literal570=null;
		Token string_literal571=null;
		ParserRuleReturnScope date_part572 =null;

		Object WORD562_tree=null;
		Object string_literal563_tree=null;
		Object string_literal564_tree=null;
		Object string_literal565_tree=null;
		Object string_literal566_tree=null;
		Object string_literal567_tree=null;
		Object string_literal568_tree=null;
		Object string_literal569_tree=null;
		Object string_literal570_tree=null;
		Object string_literal571_tree=null;

		try {
			// JPA2.g:493:5: ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | date_part )
			int alt130=11;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt130=1;
				}
				break;
			case 125:
				{
				alt130=2;
				}
				break;
			case 101:
				{
				alt130=3;
				}
				break;
			case GROUP:
				{
				alt130=4;
				}
				break;
			case ORDER:
				{
				alt130=5;
				}
				break;
			case MAX:
				{
				alt130=6;
				}
				break;
			case MIN:
				{
				alt130=7;
				}
				break;
			case SUM:
				{
				alt130=8;
				}
				break;
			case AVG:
				{
				alt130=9;
				}
				break;
			case COUNT:
				{
				alt130=10;
				}
				break;
			case 91:
			case 97:
			case 103:
			case 112:
			case 114:
			case 122:
			case 124:
			case 138:
			case 141:
				{
				alt130=11;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 130, 0, input);
				throw nvae;
			}
			switch (alt130) {
				case 1 :
					// JPA2.g:493:7: WORD
					{
					root_0 = (Object)adaptor.nil();


					WORD562=(Token)match(input,WORD,FOLLOW_WORD_in_field4471); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD562_tree = (Object)adaptor.create(WORD562);
					adaptor.addChild(root_0, WORD562_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:493:14: 'SELECT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal563=(Token)match(input,125,FOLLOW_125_in_field4475); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal563_tree = (Object)adaptor.create(string_literal563);
					adaptor.addChild(root_0, string_literal563_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:493:25: 'FROM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal564=(Token)match(input,101,FOLLOW_101_in_field4479); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal564_tree = (Object)adaptor.create(string_literal564);
					adaptor.addChild(root_0, string_literal564_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:493:34: 'GROUP'
					{
					root_0 = (Object)adaptor.nil();


					string_literal565=(Token)match(input,GROUP,FOLLOW_GROUP_in_field4483); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal565_tree = (Object)adaptor.create(string_literal565);
					adaptor.addChild(root_0, string_literal565_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:493:44: 'ORDER'
					{
					root_0 = (Object)adaptor.nil();


					string_literal566=(Token)match(input,ORDER,FOLLOW_ORDER_in_field4487); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal566_tree = (Object)adaptor.create(string_literal566);
					adaptor.addChild(root_0, string_literal566_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:493:54: 'MAX'
					{
					root_0 = (Object)adaptor.nil();


					string_literal567=(Token)match(input,MAX,FOLLOW_MAX_in_field4491); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal567_tree = (Object)adaptor.create(string_literal567);
					adaptor.addChild(root_0, string_literal567_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:493:62: 'MIN'
					{
					root_0 = (Object)adaptor.nil();


					string_literal568=(Token)match(input,MIN,FOLLOW_MIN_in_field4495); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal568_tree = (Object)adaptor.create(string_literal568);
					adaptor.addChild(root_0, string_literal568_tree);
					}

					}
					break;
				case 8 :
					// JPA2.g:493:70: 'SUM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal569=(Token)match(input,SUM,FOLLOW_SUM_in_field4499); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal569_tree = (Object)adaptor.create(string_literal569);
					adaptor.addChild(root_0, string_literal569_tree);
					}

					}
					break;
				case 9 :
					// JPA2.g:493:78: 'AVG'
					{
					root_0 = (Object)adaptor.nil();


					string_literal570=(Token)match(input,AVG,FOLLOW_AVG_in_field4503); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal570_tree = (Object)adaptor.create(string_literal570);
					adaptor.addChild(root_0, string_literal570_tree);
					}

					}
					break;
				case 10 :
					// JPA2.g:493:86: 'COUNT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal571=(Token)match(input,COUNT,FOLLOW_COUNT_in_field4507); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal571_tree = (Object)adaptor.create(string_literal571);
					adaptor.addChild(root_0, string_literal571_tree);
					}

					}
					break;
				case 11 :
					// JPA2.g:493:96: date_part
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_part_in_field4511);
					date_part572=date_part();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part572.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "field"


	public static class identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "identification_variable"
	// JPA2.g:495:1: identification_variable : WORD ;
	public final JPA2Parser.identification_variable_return identification_variable() throws RecognitionException {
		JPA2Parser.identification_variable_return retval = new JPA2Parser.identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD573=null;

		Object WORD573_tree=null;

		try {
			// JPA2.g:496:5: ( WORD )
			// JPA2.g:496:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD573=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable4523); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD573_tree = (Object)adaptor.create(WORD573);
			adaptor.addChild(root_0, WORD573_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "identification_variable"


	public static class parameter_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "parameter_name"
	// JPA2.g:498:1: parameter_name : WORD ( '.' WORD )* ;
	public final JPA2Parser.parameter_name_return parameter_name() throws RecognitionException {
		JPA2Parser.parameter_name_return retval = new JPA2Parser.parameter_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD574=null;
		Token char_literal575=null;
		Token WORD576=null;

		Object WORD574_tree=null;
		Object char_literal575_tree=null;
		Object WORD576_tree=null;

		try {
			// JPA2.g:499:5: ( WORD ( '.' WORD )* )
			// JPA2.g:499:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD574=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4535); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD574_tree = (Object)adaptor.create(WORD574);
			adaptor.addChild(root_0, WORD574_tree);
			}

			// JPA2.g:499:12: ( '.' WORD )*
			loop131:
			while (true) {
				int alt131=2;
				int LA131_0 = input.LA(1);
				if ( (LA131_0==62) ) {
					alt131=1;
				}

				switch (alt131) {
				case 1 :
					// JPA2.g:499:13: '.' WORD
					{
					char_literal575=(Token)match(input,62,FOLLOW_62_in_parameter_name4538); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal575_tree = (Object)adaptor.create(char_literal575);
					adaptor.addChild(root_0, char_literal575_tree);
					}

					WORD576=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4541); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD576_tree = (Object)adaptor.create(WORD576);
					adaptor.addChild(root_0, WORD576_tree);
					}

					}
					break;

				default :
					break loop131;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "parameter_name"


	public static class escape_character_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "escape_character"
	// JPA2.g:502:1: escape_character : ( '\\'.\\'' | STRING_LITERAL );
	public final JPA2Parser.escape_character_return escape_character() throws RecognitionException {
		JPA2Parser.escape_character_return retval = new JPA2Parser.escape_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set577=null;

		Object set577_tree=null;

		try {
			// JPA2.g:503:5: ( '\\'.\\'' | STRING_LITERAL )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set577=input.LT(1);
			if ( input.LA(1)==STRING_LITERAL||input.LA(1)==TRIM_CHARACTER ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set577));
				state.errorRecovery=false;
				state.failed=false;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "escape_character"


	public static class trim_character_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "trim_character"
	// JPA2.g:504:1: trim_character : TRIM_CHARACTER ;
	public final JPA2Parser.trim_character_return trim_character() throws RecognitionException {
		JPA2Parser.trim_character_return retval = new JPA2Parser.trim_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRIM_CHARACTER578=null;

		Object TRIM_CHARACTER578_tree=null;

		try {
			// JPA2.g:505:5: ( TRIM_CHARACTER )
			// JPA2.g:505:7: TRIM_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			TRIM_CHARACTER578=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_trim_character4571); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TRIM_CHARACTER578_tree = (Object)adaptor.create(TRIM_CHARACTER578);
			adaptor.addChild(root_0, TRIM_CHARACTER578_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "trim_character"


	public static class string_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "string_literal"
	// JPA2.g:506:1: string_literal : STRING_LITERAL ;
	public final JPA2Parser.string_literal_return string_literal() throws RecognitionException {
		JPA2Parser.string_literal_return retval = new JPA2Parser.string_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL579=null;

		Object STRING_LITERAL579_tree=null;

		try {
			// JPA2.g:507:5: ( STRING_LITERAL )
			// JPA2.g:507:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL579=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_literal4582); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL579_tree = (Object)adaptor.create(STRING_LITERAL579);
			adaptor.addChild(root_0, STRING_LITERAL579_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "string_literal"


	public static class numeric_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "numeric_literal"
	// JPA2.g:508:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
	public final JPA2Parser.numeric_literal_return numeric_literal() throws RecognitionException {
		JPA2Parser.numeric_literal_return retval = new JPA2Parser.numeric_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal580=null;
		Token INT_NUMERAL581=null;

		Object string_literal580_tree=null;
		Object INT_NUMERAL581_tree=null;

		try {
			// JPA2.g:509:5: ( ( '0x' )? INT_NUMERAL )
			// JPA2.g:509:7: ( '0x' )? INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:509:7: ( '0x' )?
			int alt132=2;
			int LA132_0 = input.LA(1);
			if ( (LA132_0==64) ) {
				alt132=1;
			}
			switch (alt132) {
				case 1 :
					// JPA2.g:509:8: '0x'
					{
					string_literal580=(Token)match(input,64,FOLLOW_64_in_numeric_literal4594); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal580_tree = (Object)adaptor.create(string_literal580);
					adaptor.addChild(root_0, string_literal580_tree);
					}

					}
					break;

			}

			INT_NUMERAL581=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal4598); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL581_tree = (Object)adaptor.create(INT_NUMERAL581);
			adaptor.addChild(root_0, INT_NUMERAL581_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "numeric_literal"


	public static class single_valued_object_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "single_valued_object_field"
	// JPA2.g:510:1: single_valued_object_field : WORD ;
	public final JPA2Parser.single_valued_object_field_return single_valued_object_field() throws RecognitionException {
		JPA2Parser.single_valued_object_field_return retval = new JPA2Parser.single_valued_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD582=null;

		Object WORD582_tree=null;

		try {
			// JPA2.g:511:5: ( WORD )
			// JPA2.g:511:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD582=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_object_field4610); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD582_tree = (Object)adaptor.create(WORD582);
			adaptor.addChild(root_0, WORD582_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "single_valued_object_field"


	public static class single_valued_embeddable_object_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "single_valued_embeddable_object_field"
	// JPA2.g:512:1: single_valued_embeddable_object_field : WORD ;
	public final JPA2Parser.single_valued_embeddable_object_field_return single_valued_embeddable_object_field() throws RecognitionException {
		JPA2Parser.single_valued_embeddable_object_field_return retval = new JPA2Parser.single_valued_embeddable_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD583=null;

		Object WORD583_tree=null;

		try {
			// JPA2.g:513:5: ( WORD )
			// JPA2.g:513:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD583=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_embeddable_object_field4621); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD583_tree = (Object)adaptor.create(WORD583);
			adaptor.addChild(root_0, WORD583_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "single_valued_embeddable_object_field"


	public static class collection_valued_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "collection_valued_field"
	// JPA2.g:514:1: collection_valued_field : WORD ;
	public final JPA2Parser.collection_valued_field_return collection_valued_field() throws RecognitionException {
		JPA2Parser.collection_valued_field_return retval = new JPA2Parser.collection_valued_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD584=null;

		Object WORD584_tree=null;

		try {
			// JPA2.g:515:5: ( WORD )
			// JPA2.g:515:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD584=(Token)match(input,WORD,FOLLOW_WORD_in_collection_valued_field4632); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD584_tree = (Object)adaptor.create(WORD584);
			adaptor.addChild(root_0, WORD584_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "collection_valued_field"


	public static class entity_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_name"
	// JPA2.g:516:1: entity_name : WORD ;
	public final JPA2Parser.entity_name_return entity_name() throws RecognitionException {
		JPA2Parser.entity_name_return retval = new JPA2Parser.entity_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD585=null;

		Object WORD585_tree=null;

		try {
			// JPA2.g:517:5: ( WORD )
			// JPA2.g:517:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD585=(Token)match(input,WORD,FOLLOW_WORD_in_entity_name4643); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD585_tree = (Object)adaptor.create(WORD585);
			adaptor.addChild(root_0, WORD585_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "entity_name"


	public static class subtype_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "subtype"
	// JPA2.g:518:1: subtype : WORD ;
	public final JPA2Parser.subtype_return subtype() throws RecognitionException {
		JPA2Parser.subtype_return retval = new JPA2Parser.subtype_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD586=null;

		Object WORD586_tree=null;

		try {
			// JPA2.g:519:5: ( WORD )
			// JPA2.g:519:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD586=(Token)match(input,WORD,FOLLOW_WORD_in_subtype4654); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD586_tree = (Object)adaptor.create(WORD586);
			adaptor.addChild(root_0, WORD586_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "subtype"


	public static class entity_type_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_type_literal"
	// JPA2.g:520:1: entity_type_literal : WORD ;
	public final JPA2Parser.entity_type_literal_return entity_type_literal() throws RecognitionException {
		JPA2Parser.entity_type_literal_return retval = new JPA2Parser.entity_type_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD587=null;

		Object WORD587_tree=null;

		try {
			// JPA2.g:521:5: ( WORD )
			// JPA2.g:521:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD587=(Token)match(input,WORD,FOLLOW_WORD_in_entity_type_literal4665); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD587_tree = (Object)adaptor.create(WORD587);
			adaptor.addChild(root_0, WORD587_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "entity_type_literal"


	public static class function_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "function_name"
	// JPA2.g:522:1: function_name : STRING_LITERAL ;
	public final JPA2Parser.function_name_return function_name() throws RecognitionException {
		JPA2Parser.function_name_return retval = new JPA2Parser.function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL588=null;

		Object STRING_LITERAL588_tree=null;

		try {
			// JPA2.g:523:5: ( STRING_LITERAL )
			// JPA2.g:523:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL588=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_function_name4676); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL588_tree = (Object)adaptor.create(STRING_LITERAL588);
			adaptor.addChild(root_0, STRING_LITERAL588_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "function_name"


	public static class state_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "state_field"
	// JPA2.g:524:1: state_field : WORD ;
	public final JPA2Parser.state_field_return state_field() throws RecognitionException {
		JPA2Parser.state_field_return retval = new JPA2Parser.state_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD589=null;

		Object WORD589_tree=null;

		try {
			// JPA2.g:525:5: ( WORD )
			// JPA2.g:525:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD589=(Token)match(input,WORD,FOLLOW_WORD_in_state_field4687); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD589_tree = (Object)adaptor.create(WORD589);
			adaptor.addChild(root_0, WORD589_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "state_field"


	public static class result_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "result_variable"
	// JPA2.g:526:1: result_variable : WORD ;
	public final JPA2Parser.result_variable_return result_variable() throws RecognitionException {
		JPA2Parser.result_variable_return retval = new JPA2Parser.result_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD590=null;

		Object WORD590_tree=null;

		try {
			// JPA2.g:527:5: ( WORD )
			// JPA2.g:527:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD590=(Token)match(input,WORD,FOLLOW_WORD_in_result_variable4698); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD590_tree = (Object)adaptor.create(WORD590);
			adaptor.addChild(root_0, WORD590_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "result_variable"


	public static class superquery_identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "superquery_identification_variable"
	// JPA2.g:528:1: superquery_identification_variable : WORD ;
	public final JPA2Parser.superquery_identification_variable_return superquery_identification_variable() throws RecognitionException {
		JPA2Parser.superquery_identification_variable_return retval = new JPA2Parser.superquery_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD591=null;

		Object WORD591_tree=null;

		try {
			// JPA2.g:529:5: ( WORD )
			// JPA2.g:529:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD591=(Token)match(input,WORD,FOLLOW_WORD_in_superquery_identification_variable4709); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD591_tree = (Object)adaptor.create(WORD591);
			adaptor.addChild(root_0, WORD591_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "superquery_identification_variable"


	public static class date_time_timestamp_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_time_timestamp_literal"
	// JPA2.g:530:1: date_time_timestamp_literal : WORD ;
	public final JPA2Parser.date_time_timestamp_literal_return date_time_timestamp_literal() throws RecognitionException {
		JPA2Parser.date_time_timestamp_literal_return retval = new JPA2Parser.date_time_timestamp_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD592=null;

		Object WORD592_tree=null;

		try {
			// JPA2.g:531:5: ( WORD )
			// JPA2.g:531:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD592=(Token)match(input,WORD,FOLLOW_WORD_in_date_time_timestamp_literal4720); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD592_tree = (Object)adaptor.create(WORD592);
			adaptor.addChild(root_0, WORD592_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "date_time_timestamp_literal"


	public static class pattern_value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "pattern_value"
	// JPA2.g:532:1: pattern_value : string_literal ;
	public final JPA2Parser.pattern_value_return pattern_value() throws RecognitionException {
		JPA2Parser.pattern_value_return retval = new JPA2Parser.pattern_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal593 =null;


		try {
			// JPA2.g:533:5: ( string_literal )
			// JPA2.g:533:7: string_literal
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_literal_in_pattern_value4731);
			string_literal593=string_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal593.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "pattern_value"


	public static class collection_valued_input_parameter_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "collection_valued_input_parameter"
	// JPA2.g:534:1: collection_valued_input_parameter : input_parameter ;
	public final JPA2Parser.collection_valued_input_parameter_return collection_valued_input_parameter() throws RecognitionException {
		JPA2Parser.collection_valued_input_parameter_return retval = new JPA2Parser.collection_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter594 =null;


		try {
			// JPA2.g:535:5: ( input_parameter )
			// JPA2.g:535:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_collection_valued_input_parameter4742);
			input_parameter594=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter594.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "collection_valued_input_parameter"


	public static class single_valued_input_parameter_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "single_valued_input_parameter"
	// JPA2.g:536:1: single_valued_input_parameter : input_parameter ;
	public final JPA2Parser.single_valued_input_parameter_return single_valued_input_parameter() throws RecognitionException {
		JPA2Parser.single_valued_input_parameter_return retval = new JPA2Parser.single_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter595 =null;


		try {
			// JPA2.g:537:5: ( input_parameter )
			// JPA2.g:537:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_single_valued_input_parameter4753);
			input_parameter595=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter595.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "single_valued_input_parameter"


	public static class enum_value_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enum_value_literal"
	// JPA2.g:538:1: enum_value_literal : WORD ( '.' WORD )* ;
	public final JPA2Parser.enum_value_literal_return enum_value_literal() throws RecognitionException {
		JPA2Parser.enum_value_literal_return retval = new JPA2Parser.enum_value_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD596=null;
		Token char_literal597=null;
		Token WORD598=null;

		Object WORD596_tree=null;
		Object char_literal597_tree=null;
		Object WORD598_tree=null;

		try {
			// JPA2.g:539:5: ( WORD ( '.' WORD )* )
			// JPA2.g:539:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD596=(Token)match(input,WORD,FOLLOW_WORD_in_enum_value_literal4764); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD596_tree = (Object)adaptor.create(WORD596);
			adaptor.addChild(root_0, WORD596_tree);
			}

			// JPA2.g:539:12: ( '.' WORD )*
			loop133:
			while (true) {
				int alt133=2;
				int LA133_0 = input.LA(1);
				if ( (LA133_0==62) ) {
					alt133=1;
				}

				switch (alt133) {
				case 1 :
					// JPA2.g:539:13: '.' WORD
					{
					char_literal597=(Token)match(input,62,FOLLOW_62_in_enum_value_literal4767); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal597_tree = (Object)adaptor.create(char_literal597);
					adaptor.addChild(root_0, char_literal597_tree);
					}

					WORD598=(Token)match(input,WORD,FOLLOW_WORD_in_enum_value_literal4770); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD598_tree = (Object)adaptor.create(WORD598);
					adaptor.addChild(root_0, WORD598_tree);
					}

					}
					break;

				default :
					break loop133;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "enum_value_literal"

	// $ANTLR start synpred21_JPA2
	public final void synpred21_JPA2_fragment() throws RecognitionException {
		// JPA2.g:121:48: ( field )
		// JPA2.g:121:48: field
		{
		pushFollow(FOLLOW_field_in_synpred21_JPA2904);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred21_JPA2

	// $ANTLR start synpred30_JPA2
	public final void synpred30_JPA2_fragment() throws RecognitionException {
		// JPA2.g:139:48: ( field )
		// JPA2.g:139:48: field
		{
		pushFollow(FOLLOW_field_in_synpred30_JPA21094);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred30_JPA2

	// $ANTLR start synpred33_JPA2
	public final void synpred33_JPA2_fragment() throws RecognitionException {
		// JPA2.g:156:7: ( scalar_expression )
		// JPA2.g:156:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred33_JPA21220);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred33_JPA2

	// $ANTLR start synpred34_JPA2
	public final void synpred34_JPA2_fragment() throws RecognitionException {
		// JPA2.g:157:7: ( simple_entity_expression )
		// JPA2.g:157:7: simple_entity_expression
		{
		pushFollow(FOLLOW_simple_entity_expression_in_synpred34_JPA21228);
		simple_entity_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred34_JPA2

	// $ANTLR start synpred39_JPA2
	public final void synpred39_JPA2_fragment() throws RecognitionException {
		// JPA2.g:169:7: ( path_expression )
		// JPA2.g:169:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred39_JPA21353);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred39_JPA2

	// $ANTLR start synpred40_JPA2
	public final void synpred40_JPA2_fragment() throws RecognitionException {
		// JPA2.g:170:7: ( identification_variable )
		// JPA2.g:170:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred40_JPA21361);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred40_JPA2

	// $ANTLR start synpred41_JPA2
	public final void synpred41_JPA2_fragment() throws RecognitionException {
		// JPA2.g:171:7: ( scalar_expression )
		// JPA2.g:171:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred41_JPA21379);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred41_JPA2

	// $ANTLR start synpred42_JPA2
	public final void synpred42_JPA2_fragment() throws RecognitionException {
		// JPA2.g:172:7: ( aggregate_expression )
		// JPA2.g:172:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred42_JPA21387);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred42_JPA2

	// $ANTLR start synpred45_JPA2
	public final void synpred45_JPA2_fragment() throws RecognitionException {
		// JPA2.g:178:7: ( path_expression )
		// JPA2.g:178:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred45_JPA21444);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred45_JPA2

	// $ANTLR start synpred46_JPA2
	public final void synpred46_JPA2_fragment() throws RecognitionException {
		// JPA2.g:179:7: ( scalar_expression )
		// JPA2.g:179:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred46_JPA21452);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred46_JPA2

	// $ANTLR start synpred47_JPA2
	public final void synpred47_JPA2_fragment() throws RecognitionException {
		// JPA2.g:180:7: ( aggregate_expression )
		// JPA2.g:180:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred47_JPA21460);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred47_JPA2

	// $ANTLR start synpred49_JPA2
	public final void synpred49_JPA2_fragment() throws RecognitionException {
		// JPA2.g:183:7: ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' )
		// JPA2.g:183:7: aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')'
		{
		pushFollow(FOLLOW_aggregate_expression_function_name_in_synpred49_JPA21479);
		aggregate_expression_function_name();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred49_JPA21481); if (state.failed) return;

		// JPA2.g:183:45: ( DISTINCT )?
		int alt140=2;
		int LA140_0 = input.LA(1);
		if ( (LA140_0==DISTINCT) ) {
			alt140=1;
		}
		switch (alt140) {
			case 1 :
				// JPA2.g:183:46: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred49_JPA21483); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_path_expression_in_synpred49_JPA21487);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred49_JPA21488); if (state.failed) return;

		}

	}
	// $ANTLR end synpred49_JPA2

	// $ANTLR start synpred51_JPA2
	public final void synpred51_JPA2_fragment() throws RecognitionException {
		// JPA2.g:185:7: ( 'COUNT' '(' ( DISTINCT )? count_argument ')' )
		// JPA2.g:185:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
		{
		match(input,COUNT,FOLLOW_COUNT_in_synpred51_JPA21522); if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred51_JPA21524); if (state.failed) return;

		// JPA2.g:185:18: ( DISTINCT )?
		int alt141=2;
		int LA141_0 = input.LA(1);
		if ( (LA141_0==DISTINCT) ) {
			alt141=1;
		}
		switch (alt141) {
			case 1 :
				// JPA2.g:185:19: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred51_JPA21526); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_count_argument_in_synpred51_JPA21530);
		count_argument();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred51_JPA21532); if (state.failed) return;

		}

	}
	// $ANTLR end synpred51_JPA2

	// $ANTLR start synpred61_JPA2
	public final void synpred61_JPA2_fragment() throws RecognitionException {
		// JPA2.g:208:7: ( path_expression )
		// JPA2.g:208:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred61_JPA21793);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred61_JPA2

	// $ANTLR start synpred62_JPA2
	public final void synpred62_JPA2_fragment() throws RecognitionException {
		// JPA2.g:208:25: ( general_identification_variable )
		// JPA2.g:208:25: general_identification_variable
		{
		pushFollow(FOLLOW_general_identification_variable_in_synpred62_JPA21797);
		general_identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred62_JPA2

	// $ANTLR start synpred63_JPA2
	public final void synpred63_JPA2_fragment() throws RecognitionException {
		// JPA2.g:208:59: ( result_variable )
		// JPA2.g:208:59: result_variable
		{
		pushFollow(FOLLOW_result_variable_in_synpred63_JPA21801);
		result_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred63_JPA2

	// $ANTLR start synpred64_JPA2
	public final void synpred64_JPA2_fragment() throws RecognitionException {
		// JPA2.g:208:77: ( scalar_expression )
		// JPA2.g:208:77: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred64_JPA21805);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred64_JPA2

	// $ANTLR start synpred73_JPA2
	public final void synpred73_JPA2_fragment() throws RecognitionException {
		// JPA2.g:223:7: ( general_derived_path '.' single_valued_object_field )
		// JPA2.g:223:7: general_derived_path '.' single_valued_object_field
		{
		pushFollow(FOLLOW_general_derived_path_in_synpred73_JPA21995);
		general_derived_path();
		state._fsp--;
		if (state.failed) return;

		match(input,62,FOLLOW_62_in_synpred73_JPA21996); if (state.failed) return;

		pushFollow(FOLLOW_single_valued_object_field_in_synpred73_JPA21997);
		single_valued_object_field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred73_JPA2

	// $ANTLR start synpred78_JPA2
	public final void synpred78_JPA2_fragment() throws RecognitionException {
		// JPA2.g:241:7: ( path_expression )
		// JPA2.g:241:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred78_JPA22149);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred78_JPA2

	// $ANTLR start synpred79_JPA2
	public final void synpred79_JPA2_fragment() throws RecognitionException {
		// JPA2.g:242:7: ( scalar_expression )
		// JPA2.g:242:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred79_JPA22157);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred79_JPA2

	// $ANTLR start synpred80_JPA2
	public final void synpred80_JPA2_fragment() throws RecognitionException {
		// JPA2.g:243:7: ( aggregate_expression )
		// JPA2.g:243:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred80_JPA22165);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred80_JPA2

	// $ANTLR start synpred81_JPA2
	public final void synpred81_JPA2_fragment() throws RecognitionException {
		// JPA2.g:246:7: ( arithmetic_expression )
		// JPA2.g:246:7: arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred81_JPA22184);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred81_JPA2

	// $ANTLR start synpred82_JPA2
	public final void synpred82_JPA2_fragment() throws RecognitionException {
		// JPA2.g:247:7: ( string_expression )
		// JPA2.g:247:7: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred82_JPA22192);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred82_JPA2

	// $ANTLR start synpred83_JPA2
	public final void synpred83_JPA2_fragment() throws RecognitionException {
		// JPA2.g:248:7: ( enum_expression )
		// JPA2.g:248:7: enum_expression
		{
		pushFollow(FOLLOW_enum_expression_in_synpred83_JPA22200);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred83_JPA2

	// $ANTLR start synpred84_JPA2
	public final void synpred84_JPA2_fragment() throws RecognitionException {
		// JPA2.g:249:7: ( datetime_expression )
		// JPA2.g:249:7: datetime_expression
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred84_JPA22208);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred84_JPA2

	// $ANTLR start synpred85_JPA2
	public final void synpred85_JPA2_fragment() throws RecognitionException {
		// JPA2.g:250:7: ( boolean_expression )
		// JPA2.g:250:7: boolean_expression
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred85_JPA22216);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred85_JPA2

	// $ANTLR start synpred86_JPA2
	public final void synpred86_JPA2_fragment() throws RecognitionException {
		// JPA2.g:251:7: ( case_expression )
		// JPA2.g:251:7: case_expression
		{
		pushFollow(FOLLOW_case_expression_in_synpred86_JPA22224);
		case_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred86_JPA2

	// $ANTLR start synpred89_JPA2
	public final void synpred89_JPA2_fragment() throws RecognitionException {
		// JPA2.g:258:8: ( 'NOT' )
		// JPA2.g:258:8: 'NOT'
		{
		match(input,NOT,FOLLOW_NOT_in_synpred89_JPA22284); if (state.failed) return;

		}

	}
	// $ANTLR end synpred89_JPA2

	// $ANTLR start synpred90_JPA2
	public final void synpred90_JPA2_fragment() throws RecognitionException {
		// JPA2.g:260:7: ( simple_cond_expression )
		// JPA2.g:260:7: simple_cond_expression
		{
		pushFollow(FOLLOW_simple_cond_expression_in_synpred90_JPA22299);
		simple_cond_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred90_JPA2

	// $ANTLR start synpred91_JPA2
	public final void synpred91_JPA2_fragment() throws RecognitionException {
		// JPA2.g:264:7: ( comparison_expression )
		// JPA2.g:264:7: comparison_expression
		{
		pushFollow(FOLLOW_comparison_expression_in_synpred91_JPA22336);
		comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred91_JPA2

	// $ANTLR start synpred92_JPA2
	public final void synpred92_JPA2_fragment() throws RecognitionException {
		// JPA2.g:265:7: ( between_expression )
		// JPA2.g:265:7: between_expression
		{
		pushFollow(FOLLOW_between_expression_in_synpred92_JPA22344);
		between_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred92_JPA2

	// $ANTLR start synpred93_JPA2
	public final void synpred93_JPA2_fragment() throws RecognitionException {
		// JPA2.g:266:7: ( in_expression )
		// JPA2.g:266:7: in_expression
		{
		pushFollow(FOLLOW_in_expression_in_synpred93_JPA22352);
		in_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred93_JPA2

	// $ANTLR start synpred94_JPA2
	public final void synpred94_JPA2_fragment() throws RecognitionException {
		// JPA2.g:267:7: ( like_expression )
		// JPA2.g:267:7: like_expression
		{
		pushFollow(FOLLOW_like_expression_in_synpred94_JPA22360);
		like_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred94_JPA2

	// $ANTLR start synpred95_JPA2
	public final void synpred95_JPA2_fragment() throws RecognitionException {
		// JPA2.g:268:7: ( null_comparison_expression )
		// JPA2.g:268:7: null_comparison_expression
		{
		pushFollow(FOLLOW_null_comparison_expression_in_synpred95_JPA22368);
		null_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred95_JPA2

	// $ANTLR start synpred96_JPA2
	public final void synpred96_JPA2_fragment() throws RecognitionException {
		// JPA2.g:269:7: ( empty_collection_comparison_expression )
		// JPA2.g:269:7: empty_collection_comparison_expression
		{
		pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred96_JPA22376);
		empty_collection_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred96_JPA2

	// $ANTLR start synpred97_JPA2
	public final void synpred97_JPA2_fragment() throws RecognitionException {
		// JPA2.g:270:7: ( collection_member_expression )
		// JPA2.g:270:7: collection_member_expression
		{
		pushFollow(FOLLOW_collection_member_expression_in_synpred97_JPA22384);
		collection_member_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred97_JPA2

	// $ANTLR start synpred116_JPA2
	public final void synpred116_JPA2_fragment() throws RecognitionException {
		// JPA2.g:299:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
		// JPA2.g:299:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred116_JPA22637);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:299:29: ( 'NOT' )?
		int alt143=2;
		int LA143_0 = input.LA(1);
		if ( (LA143_0==NOT) ) {
			alt143=1;
		}
		switch (alt143) {
			case 1 :
				// JPA2.g:299:30: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred116_JPA22640); if (state.failed) return;

				}
				break;

		}

		match(input,82,FOLLOW_82_in_synpred116_JPA22644); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred116_JPA22646);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred116_JPA22648); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred116_JPA22650);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred116_JPA2

	// $ANTLR start synpred118_JPA2
	public final void synpred118_JPA2_fragment() throws RecognitionException {
		// JPA2.g:300:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
		// JPA2.g:300:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred118_JPA22658);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:300:25: ( 'NOT' )?
		int alt144=2;
		int LA144_0 = input.LA(1);
		if ( (LA144_0==NOT) ) {
			alt144=1;
		}
		switch (alt144) {
			case 1 :
				// JPA2.g:300:26: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred118_JPA22661); if (state.failed) return;

				}
				break;

		}

		match(input,82,FOLLOW_82_in_synpred118_JPA22665); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred118_JPA22667);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred118_JPA22669); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred118_JPA22671);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred118_JPA2

	// $ANTLR start synpred129_JPA2
	public final void synpred129_JPA2_fragment() throws RecognitionException {
		// JPA2.g:316:42: ( string_expression )
		// JPA2.g:316:42: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred129_JPA22852);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred129_JPA2

	// $ANTLR start synpred130_JPA2
	public final void synpred130_JPA2_fragment() throws RecognitionException {
		// JPA2.g:316:62: ( pattern_value )
		// JPA2.g:316:62: pattern_value
		{
		pushFollow(FOLLOW_pattern_value_in_synpred130_JPA22856);
		pattern_value();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred130_JPA2

	// $ANTLR start synpred132_JPA2
	public final void synpred132_JPA2_fragment() throws RecognitionException {
		// JPA2.g:318:8: ( path_expression )
		// JPA2.g:318:8: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred132_JPA22879);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred132_JPA2

	// $ANTLR start synpred140_JPA2
	public final void synpred140_JPA2_fragment() throws RecognitionException {
		// JPA2.g:328:7: ( identification_variable )
		// JPA2.g:328:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred140_JPA22981);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred140_JPA2

	// $ANTLR start synpred147_JPA2
	public final void synpred147_JPA2_fragment() throws RecognitionException {
		// JPA2.g:336:7: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) )
		// JPA2.g:336:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_string_expression_in_synpred147_JPA23050);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:336:25: ( comparison_operator | 'REGEXP' )
		int alt146=2;
		int LA146_0 = input.LA(1);
		if ( ((LA146_0 >= 65 && LA146_0 <= 70)) ) {
			alt146=1;
		}
		else if ( (LA146_0==123) ) {
			alt146=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 146, 0, input);
			throw nvae;
		}

		switch (alt146) {
			case 1 :
				// JPA2.g:336:26: comparison_operator
				{
				pushFollow(FOLLOW_comparison_operator_in_synpred147_JPA23053);
				comparison_operator();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:336:48: 'REGEXP'
				{
				match(input,123,FOLLOW_123_in_synpred147_JPA23057); if (state.failed) return;

				}
				break;

		}

		// JPA2.g:336:58: ( string_expression | all_or_any_expression )
		int alt147=2;
		int LA147_0 = input.LA(1);
		if ( (LA147_0==AVG||LA147_0==COUNT||(LA147_0 >= LOWER && LA147_0 <= NAMED_PARAMETER)||(LA147_0 >= STRING_LITERAL && LA147_0 <= SUM)||LA147_0==WORD||LA147_0==57||LA147_0==71||LA147_0==76||(LA147_0 >= 84 && LA147_0 <= 87)||LA147_0==100||LA147_0==102||LA147_0==118||LA147_0==129||LA147_0==133||LA147_0==136) ) {
			alt147=1;
		}
		else if ( ((LA147_0 >= 79 && LA147_0 <= 80)||LA147_0==127) ) {
			alt147=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 147, 0, input);
			throw nvae;
		}

		switch (alt147) {
			case 1 :
				// JPA2.g:336:59: string_expression
				{
				pushFollow(FOLLOW_string_expression_in_synpred147_JPA23061);
				string_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:336:79: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred147_JPA23065);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred147_JPA2

	// $ANTLR start synpred150_JPA2
	public final void synpred150_JPA2_fragment() throws RecognitionException {
		// JPA2.g:337:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
		// JPA2.g:337:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred150_JPA23074);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:337:39: ( boolean_expression | all_or_any_expression )
		int alt148=2;
		int LA148_0 = input.LA(1);
		if ( (LA148_0==LPAREN||LA148_0==NAMED_PARAMETER||LA148_0==WORD||LA148_0==57||LA148_0==71||LA148_0==76||(LA148_0 >= 84 && LA148_0 <= 86)||LA148_0==100||LA148_0==102||LA148_0==118||(LA148_0 >= 142 && LA148_0 <= 143)) ) {
			alt148=1;
		}
		else if ( ((LA148_0 >= 79 && LA148_0 <= 80)||LA148_0==127) ) {
			alt148=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 148, 0, input);
			throw nvae;
		}

		switch (alt148) {
			case 1 :
				// JPA2.g:337:40: boolean_expression
				{
				pushFollow(FOLLOW_boolean_expression_in_synpred150_JPA23085);
				boolean_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:337:61: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred150_JPA23089);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred150_JPA2

	// $ANTLR start synpred153_JPA2
	public final void synpred153_JPA2_fragment() throws RecognitionException {
		// JPA2.g:338:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
		// JPA2.g:338:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_enum_expression_in_synpred153_JPA23098);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:338:34: ( enum_expression | all_or_any_expression )
		int alt149=2;
		int LA149_0 = input.LA(1);
		if ( (LA149_0==LPAREN||LA149_0==NAMED_PARAMETER||LA149_0==WORD||LA149_0==57||LA149_0==71||LA149_0==84||LA149_0==86||LA149_0==118) ) {
			alt149=1;
		}
		else if ( ((LA149_0 >= 79 && LA149_0 <= 80)||LA149_0==127) ) {
			alt149=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 149, 0, input);
			throw nvae;
		}

		switch (alt149) {
			case 1 :
				// JPA2.g:338:35: enum_expression
				{
				pushFollow(FOLLOW_enum_expression_in_synpred153_JPA23107);
				enum_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:338:53: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred153_JPA23111);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred153_JPA2

	// $ANTLR start synpred155_JPA2
	public final void synpred155_JPA2_fragment() throws RecognitionException {
		// JPA2.g:339:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
		// JPA2.g:339:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred155_JPA23120);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred155_JPA23122);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:339:47: ( datetime_expression | all_or_any_expression )
		int alt150=2;
		int LA150_0 = input.LA(1);
		if ( (LA150_0==AVG||LA150_0==COUNT||(LA150_0 >= LPAREN && LA150_0 <= NAMED_PARAMETER)||LA150_0==SUM||LA150_0==WORD||LA150_0==57||LA150_0==71||LA150_0==76||(LA150_0 >= 84 && LA150_0 <= 86)||(LA150_0 >= 88 && LA150_0 <= 90)||LA150_0==100||LA150_0==102||LA150_0==118) ) {
			alt150=1;
		}
		else if ( ((LA150_0 >= 79 && LA150_0 <= 80)||LA150_0==127) ) {
			alt150=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 150, 0, input);
			throw nvae;
		}

		switch (alt150) {
			case 1 :
				// JPA2.g:339:48: datetime_expression
				{
				pushFollow(FOLLOW_datetime_expression_in_synpred155_JPA23125);
				datetime_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:339:70: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred155_JPA23129);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred155_JPA2

	// $ANTLR start synpred158_JPA2
	public final void synpred158_JPA2_fragment() throws RecognitionException {
		// JPA2.g:340:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
		// JPA2.g:340:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_entity_expression_in_synpred158_JPA23138);
		entity_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:340:38: ( entity_expression | all_or_any_expression )
		int alt151=2;
		int LA151_0 = input.LA(1);
		if ( (LA151_0==NAMED_PARAMETER||LA151_0==WORD||LA151_0==57||LA151_0==71) ) {
			alt151=1;
		}
		else if ( ((LA151_0 >= 79 && LA151_0 <= 80)||LA151_0==127) ) {
			alt151=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 151, 0, input);
			throw nvae;
		}

		switch (alt151) {
			case 1 :
				// JPA2.g:340:39: entity_expression
				{
				pushFollow(FOLLOW_entity_expression_in_synpred158_JPA23149);
				entity_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:340:59: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred158_JPA23153);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred158_JPA2

	// $ANTLR start synpred160_JPA2
	public final void synpred160_JPA2_fragment() throws RecognitionException {
		// JPA2.g:341:7: ( entity_type_expression ( '=' | '<>' ) entity_type_expression )
		// JPA2.g:341:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
		{
		pushFollow(FOLLOW_entity_type_expression_in_synpred160_JPA23162);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		pushFollow(FOLLOW_entity_type_expression_in_synpred160_JPA23172);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred160_JPA2

	// $ANTLR start synpred167_JPA2
	public final void synpred167_JPA2_fragment() throws RecognitionException {
		// JPA2.g:352:7: ( arithmetic_term )
		// JPA2.g:352:7: arithmetic_term
		{
		pushFollow(FOLLOW_arithmetic_term_in_synpred167_JPA23253);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred167_JPA2

	// $ANTLR start synpred169_JPA2
	public final void synpred169_JPA2_fragment() throws RecognitionException {
		// JPA2.g:355:7: ( arithmetic_factor )
		// JPA2.g:355:7: arithmetic_factor
		{
		pushFollow(FOLLOW_arithmetic_factor_in_synpred169_JPA23282);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred169_JPA2

	// $ANTLR start synpred175_JPA2
	public final void synpred175_JPA2_fragment() throws RecognitionException {
		// JPA2.g:362:7: ( '(' arithmetic_expression ')' )
		// JPA2.g:362:7: '(' arithmetic_expression ')'
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred175_JPA23351); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred175_JPA23352);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred175_JPA23353); if (state.failed) return;

		}

	}
	// $ANTLR end synpred175_JPA2

	// $ANTLR start synpred178_JPA2
	public final void synpred178_JPA2_fragment() throws RecognitionException {
		// JPA2.g:365:7: ( aggregate_expression )
		// JPA2.g:365:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred178_JPA23377);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred178_JPA2

	// $ANTLR start synpred180_JPA2
	public final void synpred180_JPA2_fragment() throws RecognitionException {
		// JPA2.g:367:7: ( function_invocation )
		// JPA2.g:367:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred180_JPA23393);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred180_JPA2

	// $ANTLR start synpred186_JPA2
	public final void synpred186_JPA2_fragment() throws RecognitionException {
		// JPA2.g:375:7: ( aggregate_expression )
		// JPA2.g:375:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred186_JPA23452);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred186_JPA2

	// $ANTLR start synpred188_JPA2
	public final void synpred188_JPA2_fragment() throws RecognitionException {
		// JPA2.g:377:7: ( function_invocation )
		// JPA2.g:377:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred188_JPA23468);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred188_JPA2

	// $ANTLR start synpred190_JPA2
	public final void synpred190_JPA2_fragment() throws RecognitionException {
		// JPA2.g:381:7: ( path_expression )
		// JPA2.g:381:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred190_JPA23495);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred190_JPA2

	// $ANTLR start synpred193_JPA2
	public final void synpred193_JPA2_fragment() throws RecognitionException {
		// JPA2.g:384:7: ( aggregate_expression )
		// JPA2.g:384:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred193_JPA23519);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred193_JPA2

	// $ANTLR start synpred195_JPA2
	public final void synpred195_JPA2_fragment() throws RecognitionException {
		// JPA2.g:386:7: ( function_invocation )
		// JPA2.g:386:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred195_JPA23535);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred195_JPA2

	// $ANTLR start synpred197_JPA2
	public final void synpred197_JPA2_fragment() throws RecognitionException {
		// JPA2.g:388:7: ( date_time_timestamp_literal )
		// JPA2.g:388:7: date_time_timestamp_literal
		{
		pushFollow(FOLLOW_date_time_timestamp_literal_in_synpred197_JPA23551);
		date_time_timestamp_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred197_JPA2

	// $ANTLR start synpred235_JPA2
	public final void synpred235_JPA2_fragment() throws RecognitionException {
		// JPA2.g:439:7: ( literal )
		// JPA2.g:439:7: literal
		{
		pushFollow(FOLLOW_literal_in_synpred235_JPA24006);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred235_JPA2

	// $ANTLR start synpred237_JPA2
	public final void synpred237_JPA2_fragment() throws RecognitionException {
		// JPA2.g:441:7: ( input_parameter )
		// JPA2.g:441:7: input_parameter
		{
		pushFollow(FOLLOW_input_parameter_in_synpred237_JPA24022);
		input_parameter();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred237_JPA2

	// Delegated rules

	public final boolean synpred63_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred63_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred46_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred46_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred118_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred118_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred80_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred80_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred41_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred41_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred94_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred94_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred190_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred190_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred33_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred33_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred47_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred47_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred86_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred86_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred150_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred150_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred195_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred195_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred188_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred188_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred235_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred235_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred40_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred40_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred85_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred85_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred93_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred93_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred39_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred39_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred81_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred81_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred84_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred84_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred130_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred130_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred147_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred147_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred155_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred155_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred73_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred73_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred169_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred169_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred158_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred158_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred62_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred62_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred30_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred30_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred160_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred160_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred92_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred92_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred79_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred79_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred95_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred95_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred21_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred21_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred140_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred140_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred49_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred49_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred83_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred83_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred193_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred193_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred129_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred129_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred237_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred237_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred91_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred91_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred51_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred51_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred132_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred132_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred90_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred90_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred96_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred96_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred89_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred89_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred97_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred97_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred82_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred82_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred186_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred186_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred45_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred45_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred175_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred175_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred180_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred180_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred64_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred64_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred78_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred78_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred116_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred116_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred153_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred153_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred167_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred167_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred178_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred178_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred61_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred61_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred197_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred197_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred34_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred34_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred42_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred42_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}


	protected DFA40 dfa40 = new DFA40(this);
	static final String DFA40_eotS =
		"\26\uffff";
	static final String DFA40_eofS =
		"\26\uffff";
	static final String DFA40_minS =
		"\1\6\1\27\2\uffff\1\13\1\67\1\37\1\uffff\1\6\13\37\1\0\1\6";
	static final String DFA40_maxS =
		"\1\146\1\27\2\uffff\2\67\1\76\1\uffff\1\u008d\13\76\1\0\1\u008d";
	static final String DFA40_acceptS =
		"\2\uffff\1\1\1\3\3\uffff\1\2\16\uffff";
	static final String DFA40_specialS =
		"\24\uffff\1\0\1\uffff}>";
	static final String[] DFA40_transitionS = {
			"\1\2\2\uffff\1\1\16\uffff\2\2\11\uffff\1\2\102\uffff\1\3",
			"\1\4",
			"",
			"",
			"\1\5\53\uffff\1\6",
			"\1\6",
			"\1\7\36\uffff\1\10",
			"",
			"\1\21\2\uffff\1\22\4\uffff\1\14\11\uffff\1\16\1\17\3\uffff\1\15\1\uffff"+
			"\1\24\3\uffff\1\20\23\uffff\1\11\43\uffff\1\23\5\uffff\1\23\3\uffff\1"+
			"\13\1\uffff\1\23\10\uffff\1\23\1\uffff\1\23\7\uffff\1\23\1\uffff\1\23"+
			"\1\12\14\uffff\1\23\2\uffff\1\23",
			"\1\24\36\uffff\1\25",
			"\1\24\36\uffff\1\25",
			"\1\24\36\uffff\1\25",
			"\1\24\36\uffff\1\25",
			"\1\24\36\uffff\1\25",
			"\1\24\36\uffff\1\25",
			"\1\24\36\uffff\1\25",
			"\1\24\36\uffff\1\25",
			"\1\24\36\uffff\1\25",
			"\1\24\36\uffff\1\25",
			"\1\24\36\uffff\1\25",
			"\1\uffff",
			"\1\21\2\uffff\1\22\4\uffff\1\14\11\uffff\1\16\1\17\3\uffff\1\15\1\uffff"+
			"\1\24\3\uffff\1\20\23\uffff\1\11\43\uffff\1\23\5\uffff\1\23\3\uffff\1"+
			"\13\1\uffff\1\23\10\uffff\1\23\1\uffff\1\23\7\uffff\1\23\1\uffff\1\23"+
			"\1\12\14\uffff\1\23\2\uffff\1\23"
	};

	static final short[] DFA40_eot = DFA.unpackEncodedString(DFA40_eotS);
	static final short[] DFA40_eof = DFA.unpackEncodedString(DFA40_eofS);
	static final char[] DFA40_min = DFA.unpackEncodedStringToUnsignedChars(DFA40_minS);
	static final char[] DFA40_max = DFA.unpackEncodedStringToUnsignedChars(DFA40_maxS);
	static final short[] DFA40_accept = DFA.unpackEncodedString(DFA40_acceptS);
	static final short[] DFA40_special = DFA.unpackEncodedString(DFA40_specialS);
	static final short[][] DFA40_transition;

	static {
		int numStates = DFA40_transitionS.length;
		DFA40_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA40_transition[i] = DFA.unpackEncodedString(DFA40_transitionS[i]);
		}
	}

	protected class DFA40 extends DFA {

		public DFA40(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 40;
			this.eot = DFA40_eot;
			this.eof = DFA40_eof;
			this.min = DFA40_min;
			this.max = DFA40_max;
			this.accept = DFA40_accept;
			this.special = DFA40_special;
			this.transition = DFA40_transition;
		}
		@Override
		public String getDescription() {
			return "182:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			TokenStream input = (TokenStream)_input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA40_20 = input.LA(1);
						 
						int index40_20 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred49_JPA2()) ) {s = 2;}
						else if ( (synpred51_JPA2()) ) {s = 7;}
						 
						input.seek(index40_20);
						if ( s>=0 ) return s;
						break;
			}
			if (state.backtracking>0) {state.failed=true; return -1;}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 40, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	public static final BitSet FOLLOW_select_statement_in_ql_statement445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_update_statement_in_ql_statement449 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_delete_statement_in_ql_statement453 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_125_in_select_statement468 = new BitSet(new long[]{0x2A80000C07C40A40L,0x40CA515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_select_clause_in_select_statement470 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_from_clause_in_select_statement472 = new BitSet(new long[]{0x000000002000C002L,0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_where_clause_in_select_statement475 = new BitSet(new long[]{0x000000002000C002L});
	public static final BitSet FOLLOW_groupby_clause_in_select_statement480 = new BitSet(new long[]{0x0000000020008002L});
	public static final BitSet FOLLOW_having_clause_in_select_statement485 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_orderby_clause_in_select_statement490 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_135_in_update_statement548 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_update_clause_in_update_statement550 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_where_clause_in_update_statement553 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_92_in_delete_statement589 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_delete_clause_in_delete_statement591 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_where_clause_in_delete_statement594 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_from_clause632 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause634 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_from_clause637 = new BitSet(new long[]{0x0080000000010000L});
	public static final BitSet FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause639 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration673 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration682 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration706 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_joined_clause_in_identification_variable_declaration708 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_joined_clause_in_join_section739 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_join_in_joined_clause747 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fetch_join_in_joined_clause751 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_range_variable_declaration763 = new BitSet(new long[]{0x0080000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_range_variable_declaration766 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration770 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_join799 = new BitSet(new long[]{0x0080000000000000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_join_association_path_expression_in_join801 = new BitSet(new long[]{0x0080000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_join804 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join808 = new BitSet(new long[]{0x0000000000000002L,0x0200000000000000L});
	public static final BitSet FOLLOW_121_in_join811 = new BitSet(new long[]{0x2A80000C0FC40240L,0x4042515807F07F81L,0x000000000000C173L});
	public static final BitSet FOLLOW_conditional_expression_in_join813 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_fetch_join847 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_FETCH_in_fetch_join849 = new BitSet(new long[]{0x0080000000000000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join851 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFT_in_join_spec865 = new BitSet(new long[]{0x0000000040080000L});
	public static final BitSet FOLLOW_OUTER_in_join_spec869 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_INNER_in_join_spec875 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_JOIN_in_join_spec880 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression894 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_join_association_path_expression896 = new BitSet(new long[]{0x0080000823004242L,0x340500A208000000L,0x0000000000002400L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression899 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_join_association_path_expression900 = new BitSet(new long[]{0x0080000823004242L,0x340500A208000000L,0x0000000000002400L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression904 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_132_in_join_association_path_expression939 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression941 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_join_association_path_expression943 = new BitSet(new long[]{0x0080000823004240L,0x340500A208020000L,0x0000000000002400L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression946 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_join_association_path_expression947 = new BitSet(new long[]{0x0080000823004240L,0x340500A208020000L,0x0000000000002400L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression951 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_join_association_path_expression954 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_subtype_in_join_association_path_expression956 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_join_association_path_expression958 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_join_association_path_expression991 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_collection_member_declaration1004 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration1005 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_declaration1007 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration1009 = new BitSet(new long[]{0x0080000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_collection_member_declaration1012 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration1016 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_qualified_identification_variable1045 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_96_in_qualified_identification_variable1053 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_qualified_identification_variable1054 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_qualified_identification_variable1055 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_106_in_map_field_identification_variable1062 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1063 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1064 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_137_in_map_field_identification_variable1068 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1069 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1070 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_path_expression1084 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_path_expression1086 = new BitSet(new long[]{0x0080000823004242L,0x340500A208000000L,0x0000000000002400L});
	public static final BitSet FOLLOW_field_in_path_expression1089 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_path_expression1090 = new BitSet(new long[]{0x0080000823004242L,0x340500A208000000L,0x0000000000002400L});
	public static final BitSet FOLLOW_field_in_path_expression1094 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_general_identification_variable1133 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_general_identification_variable1141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_update_clause1154 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SET_in_update_clause1156 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1158 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_update_clause1161 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1163 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_update_item1205 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_update_item1207 = new BitSet(new long[]{0x2A80000C07C40240L,0x4062515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_new_value_in_update_item1209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_new_value1220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_new_value1228 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_117_in_new_value1236 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_delete_clause1250 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_delete_clause1252 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_select_clause1280 = new BitSet(new long[]{0x2A80000C07C40240L,0x40CA515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_select_item_in_select_clause1284 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_select_clause1287 = new BitSet(new long[]{0x2A80000C07C40240L,0x40CA515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_select_item_in_select_clause1289 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_select_expression_in_select_item1332 = new BitSet(new long[]{0x0080000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_select_item1336 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_result_variable_in_select_item1340 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_select_expression1353 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1379 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_select_expression1387 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_119_in_select_expression1395 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_select_expression1397 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1398 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_select_expression1399 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_constructor_expression_in_select_expression1407 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_115_in_constructor_expression1418 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_constructor_name_in_constructor_expression1420 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_constructor_expression1422 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1424 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_constructor_expression1427 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1429 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_constructor_expression1433 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_constructor_item1444 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_constructor_item1452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_constructor_item1460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_constructor_item1468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1479 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1481 = new BitSet(new long[]{0x0080000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1483 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_aggregate_expression1487 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1488 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_aggregate_expression1522 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1524 = new BitSet(new long[]{0x0080000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1526 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_count_argument_in_aggregate_expression1530 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1532 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_aggregate_expression1567 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_count_argument1604 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_count_argument1608 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_140_in_where_clause1621 = new BitSet(new long[]{0x2A80000C0FC40240L,0x4042515807F07F81L,0x000000000000C173L});
	public static final BitSet FOLLOW_conditional_expression_in_where_clause1623 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_groupby_clause1645 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_groupby_clause1647 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1649 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_groupby_clause1652 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1654 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_groupby_item1688 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_groupby_item1692 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HAVING_in_having_clause1703 = new BitSet(new long[]{0x2A80000C0FC40240L,0x4042515807F07F81L,0x000000000000C173L});
	public static final BitSet FOLLOW_conditional_expression_in_having_clause1705 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderby_clause1716 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_orderby_clause1718 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042555007F05081L,0x000000000000C363L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1720 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_orderby_clause1723 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042555007F05081L,0x000000000000C363L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1725 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1759 = new BitSet(new long[]{0x0000000000000422L});
	public static final BitSet FOLLOW_sort_in_orderby_item1761 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_orderby_variable1793 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_orderby_variable1797 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_orderby_variable1801 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_orderby_variable1805 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_orderby_variable1809 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_subquery1839 = new BitSet(new long[]{0x0000000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_125_in_subquery1841 = new BitSet(new long[]{0x2A80000C07C40A40L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_simple_select_clause_in_subquery1843 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_subquery_from_clause_in_subquery1845 = new BitSet(new long[]{0x000000008000C000L,0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_where_clause_in_subquery1848 = new BitSet(new long[]{0x000000008000C000L});
	public static final BitSet FOLLOW_groupby_clause_in_subquery1853 = new BitSet(new long[]{0x0000000080008000L});
	public static final BitSet FOLLOW_having_clause_in_subquery1858 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_subquery1864 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_subquery_from_clause1914 = new BitSet(new long[]{0x0080000000010000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1916 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_subquery_from_clause1919 = new BitSet(new long[]{0x0080000000010000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1921 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1959 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1967 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_subselect_identification_variable_declaration1969 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration1971 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_join_in_subselect_identification_variable_declaration1974 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1984 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1995 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_derived_path_expression1996 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_path_expression1997 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression2005 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_derived_path_expression2006 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_path_expression2007 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_derived_path_in_general_derived_path2018 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_treated_derived_path_in_general_derived_path2026 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_62_in_general_derived_path2028 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_general_derived_path2029 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_simple_derived_path2047 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_132_in_treated_derived_path2064 = new BitSet(new long[]{0x0080000000000000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_general_derived_path_in_treated_derived_path2065 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_treated_derived_path2067 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_subtype_in_treated_derived_path2069 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_treated_derived_path2071 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_derived_collection_member_declaration2082 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2084 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_derived_collection_member_declaration2085 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2087 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_derived_collection_member_declaration2089 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_collection_member_declaration2092 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause2105 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause2109 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_simple_select_expression2149 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_select_expression2157 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression2165 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_select_expression2173 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_scalar_expression2184 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_scalar_expression2192 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_scalar_expression2200 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_scalar_expression2208 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_scalar_expression2216 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_scalar_expression2224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_scalar_expression2232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2244 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_OR_in_conditional_expression2248 = new BitSet(new long[]{0x2A80000C0FC40240L,0x4042515807F07F81L,0x000000000000C173L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2250 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2264 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_conditional_term2268 = new BitSet(new long[]{0x2A80000C0FC40240L,0x4042515807F07F81L,0x000000000000C173L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2270 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_NOT_in_conditional_factor2284 = new BitSet(new long[]{0x2A80000C0FC40240L,0x4042515807F07F81L,0x000000000000C173L});
	public static final BitSet FOLLOW_conditional_primary_in_conditional_factor2288 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_conditional_primary2299 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_conditional_primary2323 = new BitSet(new long[]{0x2A80000C0FC40240L,0x4042515807F07F81L,0x000000000000C173L});
	public static final BitSet FOLLOW_conditional_expression_in_conditional_primary2324 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_conditional_primary2325 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression2336 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_simple_cond_expression2344 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_simple_cond_expression2352 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_simple_cond_expression2360 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression2368 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2376 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression2384 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression2392 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression2400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2413 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2421 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2429 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2437 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_72_in_date_between_macro_expression2457 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2459 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2461 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_between_macro_expression2463 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
	public static final BitSet FOLLOW_116_in_date_between_macro_expression2465 = new BitSet(new long[]{0x3800000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2468 = new BitSet(new long[]{0x0000000000040000L,0x0000000000000001L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2476 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_between_macro_expression2480 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
	public static final BitSet FOLLOW_116_in_date_between_macro_expression2482 = new BitSet(new long[]{0x3800000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2485 = new BitSet(new long[]{0x0000000000040000L,0x0000000000000001L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2493 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_between_macro_expression2497 = new BitSet(new long[]{0x0000000000000000L,0x1005008008000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2499 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2522 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_74_in_date_before_macro_expression2534 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2536 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2538 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_before_macro_expression2540 = new BitSet(new long[]{0x0280000004000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2543 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2547 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2550 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_73_in_date_after_macro_expression2562 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2564 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2566 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_after_macro_expression2568 = new BitSet(new long[]{0x0280000004000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2571 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2575 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2578 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_date_equals_macro_expression2590 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2592 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2594 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_equals_macro_expression2596 = new BitSet(new long[]{0x0280000004000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2599 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2603 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2606 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_77_in_date_today_macro_expression2618 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2620 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2622 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2624 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2637 = new BitSet(new long[]{0x0000000008000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2640 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_between_expression2644 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2646 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2648 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2650 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2658 = new BitSet(new long[]{0x0000000008000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2661 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_between_expression2665 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2667 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2669 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2671 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2679 = new BitSet(new long[]{0x0000000008000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2682 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_between_expression2686 = new BitSet(new long[]{0x0280000807800240L,0x0040005007701080L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2688 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2690 = new BitSet(new long[]{0x0280000807800240L,0x0040005007701080L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2692 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2704 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_type_discriminator_in_in_expression2708 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_identification_variable_in_in_expression2712 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_NOT_in_in_expression2716 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_IN_in_in_expression2720 = new BitSet(new long[]{0x0200000004800000L,0x0000000000000080L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2736 = new BitSet(new long[]{0x0280000004000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_in_item_in_in_expression2738 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_in_expression2741 = new BitSet(new long[]{0x0280000004000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_in_item_in_in_expression2743 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2747 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_in_expression2763 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_valued_input_parameter_in_in_expression2779 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2795 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2797 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2799 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_in_item2827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_single_valued_input_parameter_in_in_item2831 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2842 = new BitSet(new long[]{0x0000000008000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_NOT_in_like_expression2845 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_like_expression2849 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2852 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
	public static final BitSet FOLLOW_pattern_value_in_like_expression2856 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
	public static final BitSet FOLLOW_input_parameter_in_like_expression2860 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
	public static final BitSet FOLLOW_98_in_like_expression2863 = new BitSet(new long[]{0x0000001400000000L});
	public static final BitSet FOLLOW_escape_character_in_like_expression2865 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_null_comparison_expression2879 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression2883 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_null_comparison_expression2887 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_null_comparison_expression2890 = new BitSet(new long[]{0x0000000008000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_NOT_in_null_comparison_expression2893 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_117_in_null_comparison_expression2897 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression2908 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_empty_collection_comparison_expression2910 = new BitSet(new long[]{0x0000000008000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_NOT_in_empty_collection_comparison_expression2913 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_94_in_empty_collection_comparison_expression2917 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_or_value_expression_in_collection_member_expression2928 = new BitSet(new long[]{0x0000000008000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_NOT_in_collection_member_expression2932 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_111_in_collection_member_expression2936 = new BitSet(new long[]{0x0080000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_120_in_collection_member_expression2939 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_expression2943 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_or_value_expression2954 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2962 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_entity_or_value_expression2970 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_or_value_expression2981 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_or_value_expression2989 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simple_entity_or_value_expression2997 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_exists_expression3009 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_99_in_exists_expression3013 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_subquery_in_exists_expression3015 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_all_or_any_expression3026 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_subquery_in_all_or_any_expression3039 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3050 = new BitSet(new long[]{0x0000000000000000L,0x080000000000007EL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3053 = new BitSet(new long[]{0x0280000C07C00240L,0x8040005000F19080L,0x0000000000000122L});
	public static final BitSet FOLLOW_123_in_comparison_expression3057 = new BitSet(new long[]{0x0280000C07C00240L,0x8040005000F19080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3061 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3065 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3074 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_comparison_expression3076 = new BitSet(new long[]{0x0280000004800000L,0x8040005000719080L,0x000000000000C000L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3085 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3089 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3098 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_comparison_expression3100 = new BitSet(new long[]{0x0280000004800000L,0x8040000000518080L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3107 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3120 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3122 = new BitSet(new long[]{0x0280000807800240L,0x8040005007719080L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3125 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3129 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3138 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_comparison_expression3140 = new BitSet(new long[]{0x0280000004000000L,0x8000000000018080L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3149 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3153 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3162 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_comparison_expression3164 = new BitSet(new long[]{0x0280000004000000L,0x0000000000000080L,0x0000000000000040L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3172 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3180 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3182 = new BitSet(new long[]{0x2A80000807840240L,0xC04251500071D081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3185 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3189 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3253 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3261 = new BitSet(new long[]{0x2800000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_expression3263 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3282 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3290 = new BitSet(new long[]{0x8400000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_term3292 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3301 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor3324 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_arithmetic_primary3335 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary3343 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary3351 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_arithmetic_primary3352 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary3353 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary3361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary3369 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary3377 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_arithmetic_primary3385 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_arithmetic_primary3393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_arithmetic_primary3401 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_arithmetic_primary3409 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_string_expression3420 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_string_expression3428 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_string_expression3436 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_strings_in_string_expression3444 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_string_expression3452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_string_expression3460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_string_expression3468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_string_expression3476 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_string_expression3484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_datetime_expression3495 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_datetime_expression3503 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_expression3511 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_datetime_expression3519 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_datetime_expression3527 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_datetime_expression3535 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_datetime_expression3543 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_datetime_expression3551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_datetime_expression3559 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_boolean_expression3570 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_literal_in_boolean_expression3578 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_boolean_expression3586 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_boolean_expression3594 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_boolean_expression3602 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_boolean_expression3610 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_boolean_expression3618 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_enum_expression3629 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_literal_in_enum_expression3637 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_enum_expression3645 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_enum_expression3653 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_enum_expression3661 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_expression3672 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3680 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3691 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3699 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_entity_type_expression3710 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_literal_in_entity_type_expression3718 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_entity_type_expression3726 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_134_in_type_discriminator3737 = new BitSet(new long[]{0x0280000004000000L,0x0000040000000080L,0x0000000000000200L});
	public static final BitSet FOLLOW_general_identification_variable_in_type_discriminator3740 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_path_expression_in_type_discriminator3744 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_type_discriminator3748 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_type_discriminator3751 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_108_in_functions_returning_numerics3762 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3763 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3764 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_110_in_functions_returning_numerics3772 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3774 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_numerics3775 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3777 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_numerics3779 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3780 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3783 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_78_in_functions_returning_numerics3791 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3792 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3793 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_128_in_functions_returning_numerics3801 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3802 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3803 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_113_in_functions_returning_numerics3811 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3812 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_numerics3813 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3815 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3816 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_126_in_functions_returning_numerics3824 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3825 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3826 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_104_in_functions_returning_numerics3834 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_functions_returning_numerics3835 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3836 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_87_in_functions_returning_strings3874 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3875 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_strings3876 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3878 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_strings3881 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3883 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3886 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_129_in_functions_returning_strings3894 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3896 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_strings3897 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3899 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_strings3902 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3904 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3907 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_133_in_functions_returning_strings3915 = new BitSet(new long[]{0x0280001C07C00240L,0x0040087000F81080L,0x000000000000012AL});
	public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings3918 = new BitSet(new long[]{0x0000001000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_trim_character_in_functions_returning_strings3923 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_101_in_functions_returning_strings3927 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3931 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3933 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LOWER_in_functions_returning_strings3941 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3943 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3944 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3945 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_136_in_functions_returning_strings3953 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3954 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3955 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_function_invocation3985 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_function_name_in_function_invocation3986 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_function_invocation3989 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_function_arg_in_function_invocation3991 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_function_invocation3995 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_function_arg4006 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_function_arg4014 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_function_arg4022 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_function_arg4030 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_case_expression_in_case_expression4041 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_case_expression_in_case_expression4049 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coalesce_expression_in_case_expression4057 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullif_expression_in_case_expression4065 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_84_in_general_case_expression4076 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4078 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4081 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_93_in_general_case_expression4085 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_scalar_expression_in_general_case_expression4087 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_general_case_expression4089 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_139_in_when_clause4100 = new BitSet(new long[]{0x2A80000C0FC40240L,0x4042515807F07F81L,0x000000000000C173L});
	public static final BitSet FOLLOW_conditional_expression_in_when_clause4102 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_130_in_when_clause4104 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_scalar_expression_in_when_clause4106 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_84_in_simple_case_expression4117 = new BitSet(new long[]{0x0080000000000000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_case_operand_in_simple_case_expression4119 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4121 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4124 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_93_in_simple_case_expression4128 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_case_expression4130 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_simple_case_expression4132 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_case_operand4143 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_case_operand4151 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_139_in_simple_when_clause4162 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4164 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_130_in_simple_when_clause4166 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4168 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_86_in_coalesce_expression4179 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4180 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_coalesce_expression4183 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4185 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_coalesce_expression4188 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_118_in_nullif_expression4199 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4200 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_nullif_expression4202 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4204 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_nullif_expression4205 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_85_in_extension_functions4217 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_function_arg_in_extension_functions4219 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_WORD_in_extension_functions4221 = new BitSet(new long[]{0x0000000080800000L});
	public static final BitSet FOLLOW_LPAREN_in_extension_functions4224 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4225 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_extension_functions4228 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4230 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4235 = new BitSet(new long[]{0x0000000080800000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4239 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_100_in_extension_functions4247 = new BitSet(new long[]{0x0000000000000000L,0x1405008208000000L,0x0000000000002400L});
	public static final BitSet FOLLOW_date_part_in_extension_functions4249 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_101_in_extension_functions4251 = new BitSet(new long[]{0x2A80000C07C40240L,0x4042515007F05081L,0x000000000000C163L});
	public static final BitSet FOLLOW_function_arg_in_extension_functions4253 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4255 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_76_in_extension_functions4263 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_extension_functions4265 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_enum_value_literal_in_extension_functions4267 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4269 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_input_parameter4336 = new BitSet(new long[]{0x0000000000040000L,0x0000000000000001L});
	public static final BitSet FOLLOW_numeric_literal_in_input_parameter4338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter4361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_57_in_input_parameter4382 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_WORD_in_input_parameter4384 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_144_in_input_parameter4386 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_literal4414 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4426 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_literal4438 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_field4471 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_125_in_field4475 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_field4479 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_field4483 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_field4487 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MAX_in_field4491 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MIN_in_field4495 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SUM_in_field4499 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AVG_in_field4503 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_field4507 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_part_in_field4511 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_identification_variable4523 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4535 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_62_in_parameter_name4538 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4541 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_TRIM_CHARACTER_in_trim_character4571 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string_literal4582 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_64_in_numeric_literal4594 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal4598 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_object_field4610 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_embeddable_object_field4621 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_collection_valued_field4632 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_name4643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_subtype4654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_type_literal4665 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_function_name4676 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_state_field4687 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_result_variable4698 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_superquery_identification_variable4709 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_date_time_timestamp_literal4720 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_pattern_value4731 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_collection_valued_input_parameter4742 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_single_valued_input_parameter4753 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_value_literal4764 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_62_in_enum_value_literal4767 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_WORD_in_enum_value_literal4770 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred21_JPA2904 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred30_JPA21094 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred33_JPA21220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_synpred34_JPA21228 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred39_JPA21353 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred40_JPA21361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred41_JPA21379 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred42_JPA21387 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred45_JPA21444 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred46_JPA21452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred47_JPA21460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_synpred49_JPA21479 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred49_JPA21481 = new BitSet(new long[]{0x0080000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred49_JPA21483 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_path_expression_in_synpred49_JPA21487 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred49_JPA21488 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_synpred51_JPA21522 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred51_JPA21524 = new BitSet(new long[]{0x0080000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred51_JPA21526 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_count_argument_in_synpred51_JPA21530 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred51_JPA21532 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred61_JPA21793 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_synpred62_JPA21797 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_synpred63_JPA21801 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred64_JPA21805 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_synpred73_JPA21995 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_synpred73_JPA21996 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_synpred73_JPA21997 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred78_JPA22149 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred79_JPA22157 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred80_JPA22165 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred81_JPA22184 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred82_JPA22192 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred83_JPA22200 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred84_JPA22208 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred85_JPA22216 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_synpred86_JPA22224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_synpred89_JPA22284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_synpred90_JPA22299 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_synpred91_JPA22336 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_synpred92_JPA22344 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_synpred93_JPA22352 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_synpred94_JPA22360 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_synpred95_JPA22368 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred96_JPA22376 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_synpred97_JPA22384 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred116_JPA22637 = new BitSet(new long[]{0x0000000008000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_synpred116_JPA22640 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_synpred116_JPA22644 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred116_JPA22646 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred116_JPA22648 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred116_JPA22650 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred118_JPA22658 = new BitSet(new long[]{0x0000000008000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_synpred118_JPA22661 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_synpred118_JPA22665 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_synpred118_JPA22667 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred118_JPA22669 = new BitSet(new long[]{0x0280000C07C00240L,0x0040005000F01080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_synpred118_JPA22671 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred129_JPA22852 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_pattern_value_in_synpred130_JPA22856 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred132_JPA22879 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred140_JPA22981 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred147_JPA23050 = new BitSet(new long[]{0x0000000000000000L,0x080000000000007EL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred147_JPA23053 = new BitSet(new long[]{0x0280000C07C00240L,0x8040005000F19080L,0x0000000000000122L});
	public static final BitSet FOLLOW_123_in_synpred147_JPA23057 = new BitSet(new long[]{0x0280000C07C00240L,0x8040005000F19080L,0x0000000000000122L});
	public static final BitSet FOLLOW_string_expression_in_synpred147_JPA23061 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred147_JPA23065 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred150_JPA23074 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_synpred150_JPA23076 = new BitSet(new long[]{0x0280000004800000L,0x8040005000719080L,0x000000000000C000L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred150_JPA23085 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred150_JPA23089 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred153_JPA23098 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_synpred153_JPA23100 = new BitSet(new long[]{0x0280000004800000L,0x8040000000518080L});
	public static final BitSet FOLLOW_enum_expression_in_synpred153_JPA23107 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred153_JPA23111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred155_JPA23120 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred155_JPA23122 = new BitSet(new long[]{0x0280000807800240L,0x8040005007719080L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred155_JPA23125 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred155_JPA23129 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred158_JPA23138 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_synpred158_JPA23140 = new BitSet(new long[]{0x0280000004000000L,0x8000000000018080L});
	public static final BitSet FOLLOW_entity_expression_in_synpred158_JPA23149 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred158_JPA23153 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred160_JPA23162 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_synpred160_JPA23164 = new BitSet(new long[]{0x0280000004000000L,0x0000000000000080L,0x0000000000000040L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred160_JPA23172 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred167_JPA23253 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred169_JPA23282 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred175_JPA23351 = new BitSet(new long[]{0x2A80000807840240L,0x4042515000705081L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred175_JPA23352 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred175_JPA23353 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred178_JPA23377 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred180_JPA23393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred186_JPA23452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred188_JPA23468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred190_JPA23495 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred193_JPA23519 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred195_JPA23535 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_synpred197_JPA23551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred235_JPA24006 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_synpred237_JPA24022 = new BitSet(new long[]{0x0000000000000002L});

	@Override
	public void emitErrorMessage(String msg) {
		//do nothing
	}

	protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
		throw new MismatchedTokenException(ttype, input);
	}
}
