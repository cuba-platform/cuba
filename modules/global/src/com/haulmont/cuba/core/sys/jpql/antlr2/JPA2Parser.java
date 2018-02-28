// $ANTLR 3.5.2 JPA2.g 2018-02-28 12:40:44

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
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND", "AS", "ASC", "AVG", "BY", 
		"CASE", "COMMENT", "COUNT", "DESC", "DISTINCT", "ELSE", "END", "ESCAPE_CHARACTER", 
		"FETCH", "GROUP", "HAVING", "IN", "INNER", "INT_NUMERAL", "JOIN", "LEFT", 
		"LINE_COMMENT", "LOWER", "LPAREN", "MAX", "MIN", "NAMED_PARAMETER", "NOT", 
		"OR", "ORDER", "OUTER", "RPAREN", "RUSSIAN_SYMBOLS", "SET", "STRING_LITERAL", 
		"SUM", "THEN", "TRIM_CHARACTER", "T_AGGREGATE_EXPR", "T_COLLECTION_MEMBER", 
		"T_CONDITION", "T_ENUM_MACROS", "T_GROUP_BY", "T_ID_VAR", "T_JOIN_VAR", 
		"T_ORDER_BY", "T_ORDER_BY_FIELD", "T_PARAMETER", "T_QUERY", "T_SELECTED_ENTITY", 
		"T_SELECTED_FIELD", "T_SELECTED_ITEM", "T_SELECTED_ITEMS", "T_SIMPLE_CONDITION", 
		"T_SOURCE", "T_SOURCES", "WHEN", "WORD", "WS", "'${'", "'*'", "'+'", "','", 
		"'-'", "'.'", "'/'", "'0x'", "'<'", "'<='", "'<>'", "'='", "'>'", "'>='", 
		"'?'", "'@BETWEEN'", "'@DATEAFTER'", "'@DATEBEFORE'", "'@DATEEQUALS'", 
		"'@ENUM'", "'@TODAY'", "'ABS('", "'ALL'", "'ANY'", "'BETWEEN'", "'BOTH'", 
		"'CAST('", "'COALESCE('", "'CONCAT('", "'CURRENT_DATE'", "'CURRENT_TIME'", 
		"'CURRENT_TIMESTAMP'", "'DAY'", "'DELETE'", "'EMPTY'", "'ENTRY('", "'EPOCH'", 
		"'ESCAPE'", "'EXISTS'", "'EXTRACT('", "'FROM'", "'FUNCTION('", "'HOUR'", 
		"'INDEX('", "'IS'", "'KEY('", "'LEADING'", "'LENGTH('", "'LIKE'", "'LOCATE('", 
		"'MEMBER'", "'MINUTE'", "'MOD('", "'MONTH'", "'NEW'", "'NOW'", "'NULL'", 
		"'NULLIF('", "'NULLS FIRST'", "'NULLS LAST'", "'OBJECT'", "'OF'", "'ON'", 
		"'QUARTER'", "'REGEXP'", "'SECOND'", "'SELECT'", "'SIZE('", "'SOME'", 
		"'SQRT('", "'SUBSTRING('", "'TRAILING'", "'TREAT('", "'TRIM('", "'TYPE('", 
		"'UPDATE'", "'UPPER('", "'VALUE('", "'WEEK'", "'WHERE'", "'YEAR'", "'false'", 
		"'true'", "'}'"
	};
	public static final int EOF=-1;
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
	public static final int T__145=145;
	public static final int T__146=146;
	public static final int AND=4;
	public static final int AS=5;
	public static final int ASC=6;
	public static final int AVG=7;
	public static final int BY=8;
	public static final int CASE=9;
	public static final int COMMENT=10;
	public static final int COUNT=11;
	public static final int DESC=12;
	public static final int DISTINCT=13;
	public static final int ELSE=14;
	public static final int END=15;
	public static final int ESCAPE_CHARACTER=16;
	public static final int FETCH=17;
	public static final int GROUP=18;
	public static final int HAVING=19;
	public static final int IN=20;
	public static final int INNER=21;
	public static final int INT_NUMERAL=22;
	public static final int JOIN=23;
	public static final int LEFT=24;
	public static final int LINE_COMMENT=25;
	public static final int LOWER=26;
	public static final int LPAREN=27;
	public static final int MAX=28;
	public static final int MIN=29;
	public static final int NAMED_PARAMETER=30;
	public static final int NOT=31;
	public static final int OR=32;
	public static final int ORDER=33;
	public static final int OUTER=34;
	public static final int RPAREN=35;
	public static final int RUSSIAN_SYMBOLS=36;
	public static final int SET=37;
	public static final int STRING_LITERAL=38;
	public static final int SUM=39;
	public static final int THEN=40;
	public static final int TRIM_CHARACTER=41;
	public static final int T_AGGREGATE_EXPR=42;
	public static final int T_COLLECTION_MEMBER=43;
	public static final int T_CONDITION=44;
	public static final int T_ENUM_MACROS=45;
	public static final int T_GROUP_BY=46;
	public static final int T_ID_VAR=47;
	public static final int T_JOIN_VAR=48;
	public static final int T_ORDER_BY=49;
	public static final int T_ORDER_BY_FIELD=50;
	public static final int T_PARAMETER=51;
	public static final int T_QUERY=52;
	public static final int T_SELECTED_ENTITY=53;
	public static final int T_SELECTED_FIELD=54;
	public static final int T_SELECTED_ITEM=55;
	public static final int T_SELECTED_ITEMS=56;
	public static final int T_SIMPLE_CONDITION=57;
	public static final int T_SOURCE=58;
	public static final int T_SOURCES=59;
	public static final int WHEN=60;
	public static final int WORD=61;
	public static final int WS=62;

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
	// JPA2.g:87:1: ql_statement : ( select_statement | update_statement | delete_statement );
	public final JPA2Parser.ql_statement_return ql_statement() throws RecognitionException {
		JPA2Parser.ql_statement_return retval = new JPA2Parser.ql_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope select_statement1 =null;
		ParserRuleReturnScope update_statement2 =null;
		ParserRuleReturnScope delete_statement3 =null;


		try {
			// JPA2.g:88:5: ( select_statement | update_statement | delete_statement )
			int alt1=3;
			switch ( input.LA(1) ) {
			case 129:
				{
				alt1=1;
				}
				break;
			case 138:
				{
				alt1=2;
				}
				break;
			case 96:
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
					// JPA2.g:88:7: select_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_select_statement_in_ql_statement511);
					select_statement1=select_statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, select_statement1.getTree());

					}
					break;
				case 2 :
					// JPA2.g:88:26: update_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_update_statement_in_ql_statement515);
					update_statement2=update_statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, update_statement2.getTree());

					}
					break;
				case 3 :
					// JPA2.g:88:45: delete_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_delete_statement_in_ql_statement519);
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
	// JPA2.g:90:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
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
		RewriteRuleTokenStream stream_129=new RewriteRuleTokenStream(adaptor,"token 129");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
		RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
		RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");

		try {
			// JPA2.g:91:6: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
			// JPA2.g:91:8: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )?
			{
			sl=(Token)match(input,129,FOLLOW_129_in_select_statement534); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_129.add(sl);

			pushFollow(FOLLOW_select_clause_in_select_statement536);
			select_clause4=select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_clause.add(select_clause4.getTree());
			pushFollow(FOLLOW_from_clause_in_select_statement538);
			from_clause5=from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_from_clause.add(from_clause5.getTree());
			// JPA2.g:91:46: ( where_clause )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==142) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// JPA2.g:91:47: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_select_statement541);
					where_clause6=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause6.getTree());
					}
					break;

			}

			// JPA2.g:91:62: ( groupby_clause )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==GROUP) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// JPA2.g:91:63: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_select_statement546);
					groupby_clause7=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause7.getTree());
					}
					break;

			}

			// JPA2.g:91:80: ( having_clause )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==HAVING) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// JPA2.g:91:81: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_select_statement551);
					having_clause8=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause8.getTree());
					}
					break;

			}

			// JPA2.g:91:97: ( orderby_clause )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==ORDER) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// JPA2.g:91:98: orderby_clause
					{
					pushFollow(FOLLOW_orderby_clause_in_select_statement556);
					orderby_clause9=orderby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause9.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: having_clause, select_clause, from_clause, where_clause, orderby_clause, groupby_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 92:6: -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
			{
				// JPA2.g:92:9: ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, sl), root_1);
				// JPA2.g:92:35: ( select_clause )?
				if ( stream_select_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_select_clause.nextTree());
				}
				stream_select_clause.reset();

				adaptor.addChild(root_1, stream_from_clause.nextTree());
				// JPA2.g:92:64: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:92:80: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:92:98: ( having_clause )?
				if ( stream_having_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_having_clause.nextTree());
				}
				stream_having_clause.reset();

				// JPA2.g:92:115: ( orderby_clause )?
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
	// JPA2.g:94:1: update_statement : up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) ;
	public final JPA2Parser.update_statement_return update_statement() throws RecognitionException {
		JPA2Parser.update_statement_return retval = new JPA2Parser.update_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token up=null;
		ParserRuleReturnScope update_clause10 =null;
		ParserRuleReturnScope where_clause11 =null;

		Object up_tree=null;
		RewriteRuleTokenStream stream_138=new RewriteRuleTokenStream(adaptor,"token 138");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_update_clause=new RewriteRuleSubtreeStream(adaptor,"rule update_clause");

		try {
			// JPA2.g:95:5: (up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) )
			// JPA2.g:95:7: up= 'UPDATE' update_clause ( where_clause )?
			{
			up=(Token)match(input,138,FOLLOW_138_in_update_statement614); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_138.add(up);

			pushFollow(FOLLOW_update_clause_in_update_statement616);
			update_clause10=update_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_clause.add(update_clause10.getTree());
			// JPA2.g:95:33: ( where_clause )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==142) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// JPA2.g:95:34: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_update_statement619);
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
			// 96:5: -> ^( T_QUERY[$up] update_clause ( where_clause )? )
			{
				// JPA2.g:96:8: ^( T_QUERY[$up] update_clause ( where_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, up), root_1);
				adaptor.addChild(root_1, stream_update_clause.nextTree());
				// JPA2.g:96:48: ( where_clause )?
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
	// JPA2.g:97:1: delete_statement : dl= 'DELETE' delete_clause ( where_clause )? -> ^( T_QUERY[$dl] delete_clause ( where_clause )? ) ;
	public final JPA2Parser.delete_statement_return delete_statement() throws RecognitionException {
		JPA2Parser.delete_statement_return retval = new JPA2Parser.delete_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token dl=null;
		ParserRuleReturnScope delete_clause12 =null;
		ParserRuleReturnScope where_clause13 =null;

		Object dl_tree=null;
		RewriteRuleTokenStream stream_96=new RewriteRuleTokenStream(adaptor,"token 96");
		RewriteRuleSubtreeStream stream_delete_clause=new RewriteRuleSubtreeStream(adaptor,"rule delete_clause");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");

		try {
			// JPA2.g:98:5: (dl= 'DELETE' delete_clause ( where_clause )? -> ^( T_QUERY[$dl] delete_clause ( where_clause )? ) )
			// JPA2.g:98:7: dl= 'DELETE' delete_clause ( where_clause )?
			{
			dl=(Token)match(input,96,FOLLOW_96_in_delete_statement655); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_96.add(dl);

			pushFollow(FOLLOW_delete_clause_in_delete_statement657);
			delete_clause12=delete_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_delete_clause.add(delete_clause12.getTree());
			// JPA2.g:98:33: ( where_clause )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==142) ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					// JPA2.g:98:34: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_delete_statement660);
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
			// 99:5: -> ^( T_QUERY[$dl] delete_clause ( where_clause )? )
			{
				// JPA2.g:99:8: ^( T_QUERY[$dl] delete_clause ( where_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, dl), root_1);
				adaptor.addChild(root_1, stream_delete_clause.nextTree());
				// JPA2.g:99:48: ( where_clause )?
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
	// JPA2.g:101:1: from_clause : fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) ;
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
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_103=new RewriteRuleTokenStream(adaptor,"token 103");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
		RewriteRuleSubtreeStream stream_identification_variable_declaration_or_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration_or_collection_member_declaration");

		try {
			// JPA2.g:102:6: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) )
			// JPA2.g:102:8: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )*
			{
			fr=(Token)match(input,103,FOLLOW_103_in_from_clause698); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_103.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_from_clause700);
			identification_variable_declaration14=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration14.getTree());
			// JPA2.g:102:54: ( ',' identification_variable_declaration_or_collection_member_declaration )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==66) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// JPA2.g:102:55: ',' identification_variable_declaration_or_collection_member_declaration
					{
					char_literal15=(Token)match(input,66,FOLLOW_66_in_from_clause703); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal15);

					pushFollow(FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause705);
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
			// elements: identification_variable_declaration_or_collection_member_declaration, identification_variable_declaration
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 103:6: -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
			{
				// JPA2.g:103:9: ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
				// JPA2.g:103:72: ( identification_variable_declaration_or_collection_member_declaration )*
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
	// JPA2.g:104:1: identification_variable_declaration_or_collection_member_declaration : ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) );
	public final JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return identification_variable_declaration_or_collection_member_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return retval = new JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable_declaration17 =null;
		ParserRuleReturnScope collection_member_declaration18 =null;

		RewriteRuleSubtreeStream stream_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule collection_member_declaration");

		try {
			// JPA2.g:105:6: ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) )
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
					// JPA2.g:105:8: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration739);
					identification_variable_declaration17=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration17.getTree());

					}
					break;
				case 2 :
					// JPA2.g:106:8: collection_member_declaration
					{
					pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration748);
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
					// 106:38: -> ^( T_SOURCE collection_member_declaration )
					{
						// JPA2.g:106:41: ^( T_SOURCE collection_member_declaration )
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
	// JPA2.g:108:1: identification_variable_declaration : range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) ;
	public final JPA2Parser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_return retval = new JPA2Parser.identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope range_variable_declaration19 =null;
		ParserRuleReturnScope joined_clause20 =null;

		RewriteRuleSubtreeStream stream_range_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration");
		RewriteRuleSubtreeStream stream_joined_clause=new RewriteRuleSubtreeStream(adaptor,"rule joined_clause");

		try {
			// JPA2.g:109:6: ( range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) )
			// JPA2.g:109:8: range_variable_declaration ( joined_clause )*
			{
			pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration772);
			range_variable_declaration19=range_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range_variable_declaration.add(range_variable_declaration19.getTree());
			// JPA2.g:109:35: ( joined_clause )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==INNER||(LA10_0 >= JOIN && LA10_0 <= LEFT)) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// JPA2.g:109:35: joined_clause
					{
					pushFollow(FOLLOW_joined_clause_in_identification_variable_declaration774);
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
			// 110:6: -> ^( T_SOURCE range_variable_declaration ( joined_clause )* )
			{
				// JPA2.g:110:9: ^( T_SOURCE range_variable_declaration ( joined_clause )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_1);
				adaptor.addChild(root_1, stream_range_variable_declaration.nextTree());
				// JPA2.g:110:68: ( joined_clause )*
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
	// JPA2.g:111:1: join_section : ( joined_clause )* ;
	public final JPA2Parser.join_section_return join_section() throws RecognitionException {
		JPA2Parser.join_section_return retval = new JPA2Parser.join_section_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope joined_clause21 =null;


		try {
			// JPA2.g:111:14: ( ( joined_clause )* )
			// JPA2.g:112:5: ( joined_clause )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:112:5: ( joined_clause )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( (LA11_0==INNER||(LA11_0 >= JOIN && LA11_0 <= LEFT)) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// JPA2.g:112:5: joined_clause
					{
					pushFollow(FOLLOW_joined_clause_in_join_section805);
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
	// JPA2.g:113:1: joined_clause : ( join | fetch_join );
	public final JPA2Parser.joined_clause_return joined_clause() throws RecognitionException {
		JPA2Parser.joined_clause_return retval = new JPA2Parser.joined_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope join22 =null;
		ParserRuleReturnScope fetch_join23 =null;


		try {
			// JPA2.g:113:15: ( join | fetch_join )
			int alt12=2;
			switch ( input.LA(1) ) {
			case LEFT:
				{
				int LA12_1 = input.LA(2);
				if ( (LA12_1==OUTER) ) {
					int LA12_4 = input.LA(3);
					if ( (LA12_4==JOIN) ) {
						int LA12_3 = input.LA(4);
						if ( (LA12_3==GROUP||LA12_3==WORD||LA12_3==135) ) {
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
					if ( (LA12_3==GROUP||LA12_3==WORD||LA12_3==135) ) {
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
					if ( (LA12_3==GROUP||LA12_3==WORD||LA12_3==135) ) {
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
				if ( (LA12_3==GROUP||LA12_3==WORD||LA12_3==135) ) {
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
					// JPA2.g:113:17: join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_join_in_joined_clause813);
					join22=join();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join22.getTree());

					}
					break;
				case 2 :
					// JPA2.g:113:24: fetch_join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_fetch_join_in_joined_clause817);
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
	// JPA2.g:114:1: range_variable_declaration : entity_name ( AS )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) ;
	public final JPA2Parser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
		JPA2Parser.range_variable_declaration_return retval = new JPA2Parser.range_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AS25=null;
		ParserRuleReturnScope entity_name24 =null;
		ParserRuleReturnScope identification_variable26 =null;

		Object AS25_tree=null;
		RewriteRuleTokenStream stream_AS=new RewriteRuleTokenStream(adaptor,"token AS");
		RewriteRuleSubtreeStream stream_entity_name=new RewriteRuleSubtreeStream(adaptor,"rule entity_name");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:115:6: ( entity_name ( AS )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) )
			// JPA2.g:115:8: entity_name ( AS )? identification_variable
			{
			pushFollow(FOLLOW_entity_name_in_range_variable_declaration829);
			entity_name24=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_entity_name.add(entity_name24.getTree());
			// JPA2.g:115:20: ( AS )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==AS) ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// JPA2.g:115:21: AS
					{
					AS25=(Token)match(input,AS,FOLLOW_AS_in_range_variable_declaration832); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS25);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_range_variable_declaration836);
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
			// 116:6: -> ^( T_ID_VAR[$identification_variable.text] entity_name )
			{
				// JPA2.g:116:9: ^( T_ID_VAR[$identification_variable.text] entity_name )
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
	// JPA2.g:117:1: join : join_spec join_association_path_expression ( AS )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? ) ;
	public final JPA2Parser.join_return join() throws RecognitionException {
		JPA2Parser.join_return retval = new JPA2Parser.join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AS29=null;
		Token string_literal31=null;
		ParserRuleReturnScope join_spec27 =null;
		ParserRuleReturnScope join_association_path_expression28 =null;
		ParserRuleReturnScope identification_variable30 =null;
		ParserRuleReturnScope conditional_expression32 =null;

		Object AS29_tree=null;
		Object string_literal31_tree=null;
		RewriteRuleTokenStream stream_AS=new RewriteRuleTokenStream(adaptor,"token AS");
		RewriteRuleTokenStream stream_125=new RewriteRuleTokenStream(adaptor,"token 125");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");
		RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");

		try {
			// JPA2.g:118:6: ( join_spec join_association_path_expression ( AS )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? ) )
			// JPA2.g:118:8: join_spec join_association_path_expression ( AS )? identification_variable ( 'ON' conditional_expression )?
			{
			pushFollow(FOLLOW_join_spec_in_join865);
			join_spec27=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_spec.add(join_spec27.getTree());
			pushFollow(FOLLOW_join_association_path_expression_in_join867);
			join_association_path_expression28=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression28.getTree());
			// JPA2.g:118:51: ( AS )?
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==AS) ) {
				alt14=1;
			}
			switch (alt14) {
				case 1 :
					// JPA2.g:118:52: AS
					{
					AS29=(Token)match(input,AS,FOLLOW_AS_in_join870); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS29);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_join874);
			identification_variable30=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable30.getTree());
			// JPA2.g:118:81: ( 'ON' conditional_expression )?
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==125) ) {
				alt15=1;
			}
			switch (alt15) {
				case 1 :
					// JPA2.g:118:82: 'ON' conditional_expression
					{
					string_literal31=(Token)match(input,125,FOLLOW_125_in_join877); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_125.add(string_literal31);

					pushFollow(FOLLOW_conditional_expression_in_join879);
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
			// 119:6: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? )
			{
				// JPA2.g:119:9: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new JoinVariableNode(T_JOIN_VAR, (join_spec27!=null?input.toString(join_spec27.start,join_spec27.stop):null), (identification_variable30!=null?input.toString(identification_variable30.start,identification_variable30.stop):null)), root_1);
				adaptor.addChild(root_1, stream_join_association_path_expression.nextTree());
				// JPA2.g:119:121: ( conditional_expression )?
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
	// JPA2.g:120:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
	public final JPA2Parser.fetch_join_return fetch_join() throws RecognitionException {
		JPA2Parser.fetch_join_return retval = new JPA2Parser.fetch_join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal34=null;
		ParserRuleReturnScope join_spec33 =null;
		ParserRuleReturnScope join_association_path_expression35 =null;

		Object string_literal34_tree=null;

		try {
			// JPA2.g:121:6: ( join_spec 'FETCH' join_association_path_expression )
			// JPA2.g:121:8: join_spec 'FETCH' join_association_path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_join_spec_in_fetch_join913);
			join_spec33=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec33.getTree());

			string_literal34=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join915); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal34_tree = (Object)adaptor.create(string_literal34);
			adaptor.addChild(root_0, string_literal34_tree);
			}

			pushFollow(FOLLOW_join_association_path_expression_in_fetch_join917);
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
	// JPA2.g:122:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
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
			// JPA2.g:123:6: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
			// JPA2.g:123:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:123:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
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
					// JPA2.g:123:9: ( 'LEFT' ) ( 'OUTER' )?
					{
					// JPA2.g:123:9: ( 'LEFT' )
					// JPA2.g:123:10: 'LEFT'
					{
					string_literal36=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec931); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal36_tree = (Object)adaptor.create(string_literal36);
					adaptor.addChild(root_0, string_literal36_tree);
					}

					}

					// JPA2.g:123:18: ( 'OUTER' )?
					int alt16=2;
					int LA16_0 = input.LA(1);
					if ( (LA16_0==OUTER) ) {
						alt16=1;
					}
					switch (alt16) {
						case 1 :
							// JPA2.g:123:19: 'OUTER'
							{
							string_literal37=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec935); if (state.failed) return retval;
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
					// JPA2.g:123:31: 'INNER'
					{
					string_literal38=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec941); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal38_tree = (Object)adaptor.create(string_literal38);
					adaptor.addChild(root_0, string_literal38_tree);
					}

					}
					break;

			}

			string_literal39=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec946); if (state.failed) return retval;
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
	// JPA2.g:126:1: join_association_path_expression : ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? AS subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name );
	public final JPA2Parser.join_association_path_expression_return join_association_path_expression() throws RecognitionException {
		JPA2Parser.join_association_path_expression_return retval = new JPA2Parser.join_association_path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal41=null;
		Token char_literal43=null;
		Token string_literal45=null;
		Token char_literal47=null;
		Token char_literal49=null;
		Token AS51=null;
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
		Object AS51_tree=null;
		Object char_literal53_tree=null;
		RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
		RewriteRuleTokenStream stream_AS=new RewriteRuleTokenStream(adaptor,"token AS");
		RewriteRuleTokenStream stream_135=new RewriteRuleTokenStream(adaptor,"token 135");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_subtype=new RewriteRuleSubtreeStream(adaptor,"rule subtype");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:127:6: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? AS subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name )
			int alt22=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA22_1 = input.LA(2);
				if ( (LA22_1==68) ) {
					alt22=1;
				}
				else if ( (LA22_1==EOF||LA22_1==AS||(LA22_1 >= GROUP && LA22_1 <= HAVING)||LA22_1==INNER||(LA22_1 >= JOIN && LA22_1 <= LEFT)||LA22_1==ORDER||LA22_1==RPAREN||LA22_1==SET||LA22_1==WORD||LA22_1==66||LA22_1==107||LA22_1==142) ) {
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
				break;
			case 135:
				{
				alt22=2;
				}
				break;
			case GROUP:
				{
				alt22=1;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 22, 0, input);
				throw nvae;
			}
			switch (alt22) {
				case 1 :
					// JPA2.g:127:8: identification_variable '.' ( field '.' )* ( field )?
					{
					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression960);
					identification_variable40=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable40.getTree());
					char_literal41=(Token)match(input,68,FOLLOW_68_in_join_association_path_expression962); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_68.add(char_literal41);

					// JPA2.g:127:36: ( field '.' )*
					loop18:
					while (true) {
						int alt18=2;
						switch ( input.LA(1) ) {
						case WORD:
							{
							int LA18_1 = input.LA(2);
							if ( (LA18_1==68) ) {
								alt18=1;
							}

							}
							break;
						case 129:
							{
							int LA18_2 = input.LA(2);
							if ( (LA18_2==68) ) {
								alt18=1;
							}

							}
							break;
						case 103:
							{
							int LA18_3 = input.LA(2);
							if ( (LA18_3==68) ) {
								alt18=1;
							}

							}
							break;
						case GROUP:
							{
							int LA18_4 = input.LA(2);
							if ( (LA18_4==68) ) {
								alt18=1;
							}

							}
							break;
						case ORDER:
							{
							int LA18_5 = input.LA(2);
							if ( (LA18_5==68) ) {
								alt18=1;
							}

							}
							break;
						case MAX:
							{
							int LA18_6 = input.LA(2);
							if ( (LA18_6==68) ) {
								alt18=1;
							}

							}
							break;
						case MIN:
							{
							int LA18_7 = input.LA(2);
							if ( (LA18_7==68) ) {
								alt18=1;
							}

							}
							break;
						case SUM:
							{
							int LA18_8 = input.LA(2);
							if ( (LA18_8==68) ) {
								alt18=1;
							}

							}
							break;
						case AVG:
							{
							int LA18_9 = input.LA(2);
							if ( (LA18_9==68) ) {
								alt18=1;
							}

							}
							break;
						case COUNT:
							{
							int LA18_10 = input.LA(2);
							if ( (LA18_10==68) ) {
								alt18=1;
							}

							}
							break;
						case AS:
							{
							int LA18_11 = input.LA(2);
							if ( (LA18_11==68) ) {
								alt18=1;
							}

							}
							break;
						case 113:
							{
							int LA18_12 = input.LA(2);
							if ( (LA18_12==68) ) {
								alt18=1;
							}

							}
							break;
						case CASE:
							{
							int LA18_13 = input.LA(2);
							if ( (LA18_13==68) ) {
								alt18=1;
							}

							}
							break;
						case 95:
						case 99:
						case 105:
						case 114:
						case 116:
						case 126:
						case 128:
						case 141:
						case 143:
							{
							int LA18_14 = input.LA(2);
							if ( (LA18_14==68) ) {
								alt18=1;
							}

							}
							break;
						}
						switch (alt18) {
						case 1 :
							// JPA2.g:127:37: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression965);
							field42=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field42.getTree());
							char_literal43=(Token)match(input,68,FOLLOW_68_in_join_association_path_expression966); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_68.add(char_literal43);

							}
							break;

						default :
							break loop18;
						}
					}

					// JPA2.g:127:48: ( field )?
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
						case CASE:
						case COUNT:
						case MAX:
						case MIN:
						case SUM:
						case 95:
						case 99:
						case 103:
						case 105:
						case 113:
						case 114:
						case 116:
						case 126:
						case 128:
						case 129:
						case 141:
						case 143:
							{
							alt19=1;
							}
							break;
						case GROUP:
							{
							int LA19_3 = input.LA(2);
							if ( (synpred21_JPA2()) ) {
								alt19=1;
							}
							}
							break;
						case ORDER:
							{
							int LA19_4 = input.LA(2);
							if ( (LA19_4==EOF||LA19_4==AS||(LA19_4 >= GROUP && LA19_4 <= HAVING)||LA19_4==INNER||(LA19_4 >= JOIN && LA19_4 <= LEFT)||LA19_4==ORDER||LA19_4==RPAREN||LA19_4==SET||LA19_4==WORD||LA19_4==66||LA19_4==107||LA19_4==142) ) {
								alt19=1;
							}
							}
							break;
						case AS:
							{
							int LA19_5 = input.LA(2);
							if ( (synpred21_JPA2()) ) {
								alt19=1;
							}
							}
							break;
					}
					switch (alt19) {
						case 1 :
							// JPA2.g:127:48: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression970);
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
					// 128:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:128:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable40!=null?input.toString(identification_variable40.start,identification_variable40.stop):null)), root_1);
						// JPA2.g:128:73: ( field )*
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
					// JPA2.g:129:9: 'TREAT(' identification_variable '.' ( field '.' )* ( field )? AS subtype ')'
					{
					string_literal45=(Token)match(input,135,FOLLOW_135_in_join_association_path_expression1005); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_135.add(string_literal45);

					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression1007);
					identification_variable46=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable46.getTree());
					char_literal47=(Token)match(input,68,FOLLOW_68_in_join_association_path_expression1009); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_68.add(char_literal47);

					// JPA2.g:129:46: ( field '.' )*
					loop20:
					while (true) {
						int alt20=2;
						switch ( input.LA(1) ) {
						case WORD:
							{
							int LA20_1 = input.LA(2);
							if ( (LA20_1==68) ) {
								alt20=1;
							}

							}
							break;
						case 129:
							{
							int LA20_2 = input.LA(2);
							if ( (LA20_2==68) ) {
								alt20=1;
							}

							}
							break;
						case 103:
							{
							int LA20_3 = input.LA(2);
							if ( (LA20_3==68) ) {
								alt20=1;
							}

							}
							break;
						case GROUP:
							{
							int LA20_4 = input.LA(2);
							if ( (LA20_4==68) ) {
								alt20=1;
							}

							}
							break;
						case ORDER:
							{
							int LA20_5 = input.LA(2);
							if ( (LA20_5==68) ) {
								alt20=1;
							}

							}
							break;
						case MAX:
							{
							int LA20_6 = input.LA(2);
							if ( (LA20_6==68) ) {
								alt20=1;
							}

							}
							break;
						case MIN:
							{
							int LA20_7 = input.LA(2);
							if ( (LA20_7==68) ) {
								alt20=1;
							}

							}
							break;
						case SUM:
							{
							int LA20_8 = input.LA(2);
							if ( (LA20_8==68) ) {
								alt20=1;
							}

							}
							break;
						case AVG:
							{
							int LA20_9 = input.LA(2);
							if ( (LA20_9==68) ) {
								alt20=1;
							}

							}
							break;
						case COUNT:
							{
							int LA20_10 = input.LA(2);
							if ( (LA20_10==68) ) {
								alt20=1;
							}

							}
							break;
						case AS:
							{
							int LA20_11 = input.LA(2);
							if ( (LA20_11==68) ) {
								alt20=1;
							}

							}
							break;
						case 113:
							{
							int LA20_12 = input.LA(2);
							if ( (LA20_12==68) ) {
								alt20=1;
							}

							}
							break;
						case CASE:
							{
							int LA20_13 = input.LA(2);
							if ( (LA20_13==68) ) {
								alt20=1;
							}

							}
							break;
						case 95:
						case 99:
						case 105:
						case 114:
						case 116:
						case 126:
						case 128:
						case 141:
						case 143:
							{
							int LA20_14 = input.LA(2);
							if ( (LA20_14==68) ) {
								alt20=1;
							}

							}
							break;
						}
						switch (alt20) {
						case 1 :
							// JPA2.g:129:47: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression1012);
							field48=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field48.getTree());
							char_literal49=(Token)match(input,68,FOLLOW_68_in_join_association_path_expression1013); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_68.add(char_literal49);

							}
							break;

						default :
							break loop20;
						}
					}

					// JPA2.g:129:58: ( field )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( (LA21_0==AVG||LA21_0==CASE||LA21_0==COUNT||LA21_0==GROUP||(LA21_0 >= MAX && LA21_0 <= MIN)||LA21_0==ORDER||LA21_0==SUM||LA21_0==WORD||LA21_0==95||LA21_0==99||LA21_0==103||LA21_0==105||(LA21_0 >= 113 && LA21_0 <= 114)||LA21_0==116||LA21_0==126||(LA21_0 >= 128 && LA21_0 <= 129)||LA21_0==141||LA21_0==143) ) {
						alt21=1;
					}
					else if ( (LA21_0==AS) ) {
						int LA21_2 = input.LA(2);
						if ( (LA21_2==AS) ) {
							alt21=1;
						}
					}
					switch (alt21) {
						case 1 :
							// JPA2.g:129:58: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression1017);
							field50=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field50.getTree());
							}
							break;

					}

					AS51=(Token)match(input,AS,FOLLOW_AS_in_join_association_path_expression1020); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS51);

					pushFollow(FOLLOW_subtype_in_join_association_path_expression1022);
					subtype52=subtype();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subtype.add(subtype52.getTree());
					char_literal53=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_join_association_path_expression1024); if (state.failed) return retval; 
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
					// 130:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:130:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable46!=null?input.toString(identification_variable46.start,identification_variable46.stop):null)), root_1);
						// JPA2.g:130:73: ( field )*
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
					// JPA2.g:131:8: entity_name
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_name_in_join_association_path_expression1057);
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
	// JPA2.g:134:1: collection_member_declaration : 'IN' '(' path_expression ')' ( AS )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
	public final JPA2Parser.collection_member_declaration_return collection_member_declaration() throws RecognitionException {
		JPA2Parser.collection_member_declaration_return retval = new JPA2Parser.collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal55=null;
		Token char_literal56=null;
		Token char_literal58=null;
		Token AS59=null;
		ParserRuleReturnScope path_expression57 =null;
		ParserRuleReturnScope identification_variable60 =null;

		Object string_literal55_tree=null;
		Object char_literal56_tree=null;
		Object char_literal58_tree=null;
		Object AS59_tree=null;
		RewriteRuleTokenStream stream_AS=new RewriteRuleTokenStream(adaptor,"token AS");
		RewriteRuleTokenStream stream_IN=new RewriteRuleTokenStream(adaptor,"token IN");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");

		try {
			// JPA2.g:135:5: ( 'IN' '(' path_expression ')' ( AS )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
			// JPA2.g:135:7: 'IN' '(' path_expression ')' ( AS )? identification_variable
			{
			string_literal55=(Token)match(input,IN,FOLLOW_IN_in_collection_member_declaration1070); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_IN.add(string_literal55);

			char_literal56=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration1071); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal56);

			pushFollow(FOLLOW_path_expression_in_collection_member_declaration1073);
			path_expression57=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_path_expression.add(path_expression57.getTree());
			char_literal58=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration1075); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal58);

			// JPA2.g:135:35: ( AS )?
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0==AS) ) {
				alt23=1;
			}
			switch (alt23) {
				case 1 :
					// JPA2.g:135:36: AS
					{
					AS59=(Token)match(input,AS,FOLLOW_AS_in_collection_member_declaration1078); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS59);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_collection_member_declaration1082);
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
			// 136:5: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
			{
				// JPA2.g:136:8: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
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
	// JPA2.g:138:1: qualified_identification_variable : ( map_field_identification_variable | 'ENTRY(' identification_variable ')' );
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
			// JPA2.g:139:5: ( map_field_identification_variable | 'ENTRY(' identification_variable ')' )
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==108||LA24_0==140) ) {
				alt24=1;
			}
			else if ( (LA24_0==98) ) {
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
					// JPA2.g:139:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_qualified_identification_variable1111);
					map_field_identification_variable61=map_field_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map_field_identification_variable61.getTree());

					}
					break;
				case 2 :
					// JPA2.g:140:7: 'ENTRY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal62=(Token)match(input,98,FOLLOW_98_in_qualified_identification_variable1119); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal62_tree = (Object)adaptor.create(string_literal62);
					adaptor.addChild(root_0, string_literal62_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_qualified_identification_variable1120);
					identification_variable63=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable63.getTree());

					char_literal64=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_qualified_identification_variable1121); if (state.failed) return retval;
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
	// JPA2.g:141:1: map_field_identification_variable : ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' );
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
			// JPA2.g:141:35: ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' )
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==108) ) {
				alt25=1;
			}
			else if ( (LA25_0==140) ) {
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
					// JPA2.g:141:37: 'KEY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal65=(Token)match(input,108,FOLLOW_108_in_map_field_identification_variable1128); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal65_tree = (Object)adaptor.create(string_literal65);
					adaptor.addChild(root_0, string_literal65_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1129);
					identification_variable66=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable66.getTree());

					char_literal67=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1130); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal67_tree = (Object)adaptor.create(char_literal67);
					adaptor.addChild(root_0, char_literal67_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:141:72: 'VALUE(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal68=(Token)match(input,140,FOLLOW_140_in_map_field_identification_variable1134); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal68_tree = (Object)adaptor.create(string_literal68);
					adaptor.addChild(root_0, string_literal68_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1135);
					identification_variable69=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable69.getTree());

					char_literal70=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1136); if (state.failed) return retval;
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
	// JPA2.g:144:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
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
		RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:145:5: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
			// JPA2.g:145:8: identification_variable '.' ( field '.' )* ( field )?
			{
			pushFollow(FOLLOW_identification_variable_in_path_expression1150);
			identification_variable71=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable71.getTree());
			char_literal72=(Token)match(input,68,FOLLOW_68_in_path_expression1152); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_68.add(char_literal72);

			// JPA2.g:145:36: ( field '.' )*
			loop26:
			while (true) {
				int alt26=2;
				switch ( input.LA(1) ) {
				case WORD:
					{
					int LA26_1 = input.LA(2);
					if ( (LA26_1==68) ) {
						alt26=1;
					}

					}
					break;
				case 129:
					{
					int LA26_2 = input.LA(2);
					if ( (LA26_2==68) ) {
						alt26=1;
					}

					}
					break;
				case 103:
					{
					int LA26_3 = input.LA(2);
					if ( (LA26_3==68) ) {
						alt26=1;
					}

					}
					break;
				case GROUP:
					{
					int LA26_4 = input.LA(2);
					if ( (LA26_4==68) ) {
						alt26=1;
					}

					}
					break;
				case ORDER:
					{
					int LA26_5 = input.LA(2);
					if ( (LA26_5==68) ) {
						alt26=1;
					}

					}
					break;
				case MAX:
					{
					int LA26_6 = input.LA(2);
					if ( (LA26_6==68) ) {
						alt26=1;
					}

					}
					break;
				case MIN:
					{
					int LA26_7 = input.LA(2);
					if ( (LA26_7==68) ) {
						alt26=1;
					}

					}
					break;
				case SUM:
					{
					int LA26_8 = input.LA(2);
					if ( (LA26_8==68) ) {
						alt26=1;
					}

					}
					break;
				case AVG:
					{
					int LA26_9 = input.LA(2);
					if ( (LA26_9==68) ) {
						alt26=1;
					}

					}
					break;
				case COUNT:
					{
					int LA26_10 = input.LA(2);
					if ( (LA26_10==68) ) {
						alt26=1;
					}

					}
					break;
				case AS:
					{
					int LA26_11 = input.LA(2);
					if ( (LA26_11==68) ) {
						alt26=1;
					}

					}
					break;
				case 113:
					{
					int LA26_12 = input.LA(2);
					if ( (LA26_12==68) ) {
						alt26=1;
					}

					}
					break;
				case CASE:
					{
					int LA26_13 = input.LA(2);
					if ( (LA26_13==68) ) {
						alt26=1;
					}

					}
					break;
				case 95:
				case 99:
				case 105:
				case 114:
				case 116:
				case 126:
				case 128:
				case 141:
				case 143:
					{
					int LA26_14 = input.LA(2);
					if ( (LA26_14==68) ) {
						alt26=1;
					}

					}
					break;
				}
				switch (alt26) {
				case 1 :
					// JPA2.g:145:37: field '.'
					{
					pushFollow(FOLLOW_field_in_path_expression1155);
					field73=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field73.getTree());
					char_literal74=(Token)match(input,68,FOLLOW_68_in_path_expression1156); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_68.add(char_literal74);

					}
					break;

				default :
					break loop26;
				}
			}

			// JPA2.g:145:48: ( field )?
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
				case CASE:
				case COUNT:
				case MAX:
				case MIN:
				case SUM:
				case 95:
				case 99:
				case 105:
				case 114:
				case 116:
				case 126:
				case 128:
				case 129:
				case 141:
				case 143:
					{
					alt27=1;
					}
					break;
				case 103:
					{
					switch ( input.LA(2) ) {
						case EOF:
						case AND:
						case AS:
						case ASC:
						case DESC:
						case ELSE:
						case END:
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
						case THEN:
						case WHEN:
						case 64:
						case 65:
						case 66:
						case 67:
						case 69:
						case 71:
						case 72:
						case 73:
						case 74:
						case 75:
						case 76:
						case 87:
						case 100:
						case 103:
						case 107:
						case 111:
						case 113:
						case 121:
						case 122:
						case 127:
						case 142:
							{
							alt27=1;
							}
							break;
						case WORD:
							{
							int LA27_9 = input.LA(3);
							if ( (LA27_9==EOF||LA27_9==LPAREN||LA27_9==RPAREN||LA27_9==66||LA27_9==103) ) {
								alt27=1;
							}
							}
							break;
						case IN:
							{
							int LA27_10 = input.LA(3);
							if ( (LA27_10==LPAREN||LA27_10==NAMED_PARAMETER||LA27_10==63||LA27_10==77) ) {
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
					if ( (LA27_4==EOF||(LA27_4 >= AND && LA27_4 <= ASC)||LA27_4==DESC||(LA27_4 >= ELSE && LA27_4 <= END)||(LA27_4 >= GROUP && LA27_4 <= INNER)||(LA27_4 >= JOIN && LA27_4 <= LEFT)||(LA27_4 >= NOT && LA27_4 <= ORDER)||LA27_4==RPAREN||LA27_4==SET||LA27_4==THEN||(LA27_4 >= WHEN && LA27_4 <= WORD)||(LA27_4 >= 64 && LA27_4 <= 67)||LA27_4==69||(LA27_4 >= 71 && LA27_4 <= 76)||LA27_4==87||LA27_4==100||LA27_4==103||LA27_4==107||LA27_4==111||LA27_4==113||(LA27_4 >= 121 && LA27_4 <= 122)||LA27_4==127||LA27_4==142) ) {
						alt27=1;
					}
					}
					break;
				case ORDER:
					{
					int LA27_5 = input.LA(2);
					if ( (LA27_5==EOF||(LA27_5 >= AND && LA27_5 <= ASC)||LA27_5==DESC||(LA27_5 >= ELSE && LA27_5 <= END)||(LA27_5 >= GROUP && LA27_5 <= INNER)||(LA27_5 >= JOIN && LA27_5 <= LEFT)||(LA27_5 >= NOT && LA27_5 <= ORDER)||LA27_5==RPAREN||LA27_5==SET||LA27_5==THEN||(LA27_5 >= WHEN && LA27_5 <= WORD)||(LA27_5 >= 64 && LA27_5 <= 67)||LA27_5==69||(LA27_5 >= 71 && LA27_5 <= 76)||LA27_5==87||LA27_5==100||LA27_5==103||LA27_5==107||LA27_5==111||LA27_5==113||(LA27_5 >= 121 && LA27_5 <= 122)||LA27_5==127||LA27_5==142) ) {
						alt27=1;
					}
					}
					break;
				case AS:
					{
					int LA27_6 = input.LA(2);
					if ( (synpred30_JPA2()) ) {
						alt27=1;
					}
					}
					break;
				case 113:
					{
					switch ( input.LA(2) ) {
						case EOF:
						case AND:
						case AS:
						case ASC:
						case DESC:
						case ELSE:
						case END:
						case HAVING:
						case IN:
						case INNER:
						case JOIN:
						case LEFT:
						case NOT:
						case OR:
						case ORDER:
						case RPAREN:
						case SET:
						case THEN:
						case WHEN:
						case 64:
						case 65:
						case 66:
						case 67:
						case 69:
						case 71:
						case 72:
						case 73:
						case 74:
						case 75:
						case 76:
						case 87:
						case 100:
						case 103:
						case 107:
						case 111:
						case 113:
						case 121:
						case 122:
						case 127:
						case 142:
							{
							alt27=1;
							}
							break;
						case WORD:
							{
							int LA27_11 = input.LA(3);
							if ( (LA27_11==EOF||LA27_11==LPAREN||LA27_11==RPAREN||LA27_11==66||LA27_11==103) ) {
								alt27=1;
							}
							}
							break;
						case GROUP:
							{
							int LA27_12 = input.LA(3);
							if ( (LA27_12==BY) ) {
								alt27=1;
							}
							}
							break;
					}
					}
					break;
			}
			switch (alt27) {
				case 1 :
					// JPA2.g:145:48: field
					{
					pushFollow(FOLLOW_field_in_path_expression1160);
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
			// 146:5: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
			{
				// JPA2.g:146:8: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable71!=null?input.toString(identification_variable71.start,identification_variable71.stop):null)), root_1);
				// JPA2.g:146:68: ( field )*
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
	// JPA2.g:151:1: general_identification_variable : ( identification_variable | map_field_identification_variable );
	public final JPA2Parser.general_identification_variable_return general_identification_variable() throws RecognitionException {
		JPA2Parser.general_identification_variable_return retval = new JPA2Parser.general_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable76 =null;
		ParserRuleReturnScope map_field_identification_variable77 =null;


		try {
			// JPA2.g:152:5: ( identification_variable | map_field_identification_variable )
			int alt28=2;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==GROUP||LA28_0==WORD) ) {
				alt28=1;
			}
			else if ( (LA28_0==108||LA28_0==140) ) {
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
					// JPA2.g:152:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_general_identification_variable1199);
					identification_variable76=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable76.getTree());

					}
					break;
				case 2 :
					// JPA2.g:153:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_general_identification_variable1207);
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
	// JPA2.g:156:1: update_clause : identification_variable_declaration SET update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* ) ;
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
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_SET=new RewriteRuleTokenStream(adaptor,"token SET");
		RewriteRuleSubtreeStream stream_update_item=new RewriteRuleSubtreeStream(adaptor,"rule update_item");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:157:5: ( identification_variable_declaration SET update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* ) )
			// JPA2.g:157:7: identification_variable_declaration SET update_item ( ',' update_item )*
			{
			pushFollow(FOLLOW_identification_variable_declaration_in_update_clause1220);
			identification_variable_declaration78=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration78.getTree());
			SET79=(Token)match(input,SET,FOLLOW_SET_in_update_clause1222); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_SET.add(SET79);

			pushFollow(FOLLOW_update_item_in_update_clause1224);
			update_item80=update_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_item.add(update_item80.getTree());
			// JPA2.g:157:59: ( ',' update_item )*
			loop29:
			while (true) {
				int alt29=2;
				int LA29_0 = input.LA(1);
				if ( (LA29_0==66) ) {
					alt29=1;
				}

				switch (alt29) {
				case 1 :
					// JPA2.g:157:60: ',' update_item
					{
					char_literal81=(Token)match(input,66,FOLLOW_66_in_update_clause1227); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal81);

					pushFollow(FOLLOW_update_item_in_update_clause1229);
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
			// elements: identification_variable_declaration, SET, update_item, 66, update_item
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 158:5: -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* )
			{
				// JPA2.g:158:8: ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCES), root_1);
				adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
				adaptor.addChild(root_1, new UpdateSetNode(stream_SET.nextToken()));
				adaptor.addChild(root_1, stream_update_item.nextTree());
				// JPA2.g:158:108: ( ',' update_item )*
				while ( stream_update_item.hasNext()||stream_66.hasNext() ) {
					adaptor.addChild(root_1, stream_66.nextNode());
					adaptor.addChild(root_1, stream_update_item.nextTree());
				}
				stream_update_item.reset();
				stream_66.reset();

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
	// JPA2.g:159:1: update_item : path_expression '=' new_value ;
	public final JPA2Parser.update_item_return update_item() throws RecognitionException {
		JPA2Parser.update_item_return retval = new JPA2Parser.update_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal84=null;
		ParserRuleReturnScope path_expression83 =null;
		ParserRuleReturnScope new_value85 =null;

		Object char_literal84_tree=null;

		try {
			// JPA2.g:160:5: ( path_expression '=' new_value )
			// JPA2.g:160:7: path_expression '=' new_value
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_update_item1271);
			path_expression83=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression83.getTree());

			char_literal84=(Token)match(input,74,FOLLOW_74_in_update_item1273); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal84_tree = (Object)adaptor.create(char_literal84);
			adaptor.addChild(root_0, char_literal84_tree);
			}

			pushFollow(FOLLOW_new_value_in_update_item1275);
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
	// JPA2.g:161:1: new_value : ( scalar_expression | simple_entity_expression | 'NULL' );
	public final JPA2Parser.new_value_return new_value() throws RecognitionException {
		JPA2Parser.new_value_return retval = new JPA2Parser.new_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal88=null;
		ParserRuleReturnScope scalar_expression86 =null;
		ParserRuleReturnScope simple_entity_expression87 =null;

		Object string_literal88_tree=null;

		try {
			// JPA2.g:162:5: ( scalar_expression | simple_entity_expression | 'NULL' )
			int alt30=3;
			switch ( input.LA(1) ) {
			case AVG:
			case CASE:
			case COUNT:
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case MAX:
			case MIN:
			case STRING_LITERAL:
			case SUM:
			case 65:
			case 67:
			case 70:
			case 82:
			case 84:
			case 89:
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 102:
			case 104:
			case 106:
			case 110:
			case 112:
			case 115:
			case 120:
			case 130:
			case 132:
			case 133:
			case 136:
			case 137:
			case 139:
			case 144:
			case 145:
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
			case 77:
				{
				int LA30_3 = input.LA(2);
				if ( (LA30_3==70) ) {
					int LA30_9 = input.LA(3);
					if ( (LA30_9==INT_NUMERAL) ) {
						int LA30_10 = input.LA(4);
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
				else if ( (LA30_3==INT_NUMERAL) ) {
					int LA30_10 = input.LA(3);
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
			case 63:
				{
				int LA30_5 = input.LA(2);
				if ( (LA30_5==WORD) ) {
					int LA30_11 = input.LA(3);
					if ( (LA30_11==146) ) {
						int LA30_12 = input.LA(4);
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
									new NoViableAltException("", 30, 12, input);
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
			case GROUP:
				{
				int LA30_6 = input.LA(2);
				if ( (LA30_6==68) ) {
					alt30=1;
				}
				else if ( (LA30_6==EOF||LA30_6==66||LA30_6==142) ) {
					alt30=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 30, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 119:
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
					// JPA2.g:162:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_new_value1286);
					scalar_expression86=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression86.getTree());

					}
					break;
				case 2 :
					// JPA2.g:163:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_new_value1294);
					simple_entity_expression87=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression87.getTree());

					}
					break;
				case 3 :
					// JPA2.g:164:7: 'NULL'
					{
					root_0 = (Object)adaptor.nil();


					string_literal88=(Token)match(input,119,FOLLOW_119_in_new_value1302); if (state.failed) return retval;
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
	// JPA2.g:166:1: delete_clause : fr= 'FROM' identification_variable_declaration -> ^( T_SOURCES[$fr] identification_variable_declaration ) ;
	public final JPA2Parser.delete_clause_return delete_clause() throws RecognitionException {
		JPA2Parser.delete_clause_return retval = new JPA2Parser.delete_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token fr=null;
		ParserRuleReturnScope identification_variable_declaration89 =null;

		Object fr_tree=null;
		RewriteRuleTokenStream stream_103=new RewriteRuleTokenStream(adaptor,"token 103");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:167:5: (fr= 'FROM' identification_variable_declaration -> ^( T_SOURCES[$fr] identification_variable_declaration ) )
			// JPA2.g:167:7: fr= 'FROM' identification_variable_declaration
			{
			fr=(Token)match(input,103,FOLLOW_103_in_delete_clause1316); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_103.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_delete_clause1318);
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
			// 168:5: -> ^( T_SOURCES[$fr] identification_variable_declaration )
			{
				// JPA2.g:168:8: ^( T_SOURCES[$fr] identification_variable_declaration )
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
	// JPA2.g:169:1: select_clause : ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) ;
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
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleSubtreeStream stream_select_item=new RewriteRuleSubtreeStream(adaptor,"rule select_item");

		try {
			// JPA2.g:170:5: ( ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) )
			// JPA2.g:170:7: ( 'DISTINCT' )? select_item ( ',' select_item )*
			{
			// JPA2.g:170:7: ( 'DISTINCT' )?
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0==DISTINCT) ) {
				alt31=1;
			}
			switch (alt31) {
				case 1 :
					// JPA2.g:170:8: 'DISTINCT'
					{
					string_literal90=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause1346); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal90);

					}
					break;

			}

			pushFollow(FOLLOW_select_item_in_select_clause1350);
			select_item91=select_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_item.add(select_item91.getTree());
			// JPA2.g:170:33: ( ',' select_item )*
			loop32:
			while (true) {
				int alt32=2;
				int LA32_0 = input.LA(1);
				if ( (LA32_0==66) ) {
					alt32=1;
				}

				switch (alt32) {
				case 1 :
					// JPA2.g:170:34: ',' select_item
					{
					char_literal92=(Token)match(input,66,FOLLOW_66_in_select_clause1353); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal92);

					pushFollow(FOLLOW_select_item_in_select_clause1355);
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
			// 171:5: -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
			{
				// JPA2.g:171:8: ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:171:48: ( 'DISTINCT' )?
				if ( stream_DISTINCT.hasNext() ) {
					adaptor.addChild(root_1, stream_DISTINCT.nextNode());
				}
				stream_DISTINCT.reset();

				// JPA2.g:171:62: ( ^( T_SELECTED_ITEM[] select_item ) )*
				while ( stream_select_item.hasNext() ) {
					// JPA2.g:171:62: ^( T_SELECTED_ITEM[] select_item )
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
	// JPA2.g:172:1: select_item : select_expression ( ( AS )? result_variable )? ;
	public final JPA2Parser.select_item_return select_item() throws RecognitionException {
		JPA2Parser.select_item_return retval = new JPA2Parser.select_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AS95=null;
		ParserRuleReturnScope select_expression94 =null;
		ParserRuleReturnScope result_variable96 =null;

		Object AS95_tree=null;

		try {
			// JPA2.g:173:5: ( select_expression ( ( AS )? result_variable )? )
			// JPA2.g:173:7: select_expression ( ( AS )? result_variable )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_select_expression_in_select_item1398);
			select_expression94=select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, select_expression94.getTree());

			// JPA2.g:173:25: ( ( AS )? result_variable )?
			int alt34=2;
			int LA34_0 = input.LA(1);
			if ( (LA34_0==AS||LA34_0==WORD) ) {
				alt34=1;
			}
			switch (alt34) {
				case 1 :
					// JPA2.g:173:26: ( AS )? result_variable
					{
					// JPA2.g:173:26: ( AS )?
					int alt33=2;
					int LA33_0 = input.LA(1);
					if ( (LA33_0==AS) ) {
						alt33=1;
					}
					switch (alt33) {
						case 1 :
							// JPA2.g:173:27: AS
							{
							AS95=(Token)match(input,AS,FOLLOW_AS_in_select_item1402); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							AS95_tree = (Object)adaptor.create(AS95);
							adaptor.addChild(root_0, AS95_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_result_variable_in_select_item1406);
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
	// JPA2.g:174:1: select_expression : ( path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )? | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression );
	public final JPA2Parser.select_expression_return select_expression() throws RecognitionException {
		JPA2Parser.select_expression_return retval = new JPA2Parser.select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set98=null;
		Token string_literal103=null;
		Token char_literal104=null;
		Token char_literal106=null;
		ParserRuleReturnScope path_expression97 =null;
		ParserRuleReturnScope scalar_expression99 =null;
		ParserRuleReturnScope identification_variable100 =null;
		ParserRuleReturnScope scalar_expression101 =null;
		ParserRuleReturnScope aggregate_expression102 =null;
		ParserRuleReturnScope identification_variable105 =null;
		ParserRuleReturnScope constructor_expression107 =null;

		Object set98_tree=null;
		Object string_literal103_tree=null;
		Object char_literal104_tree=null;
		Object char_literal106_tree=null;
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:175:5: ( path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )? | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression )
			int alt36=6;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA36_1 = input.LA(2);
				if ( (synpred43_JPA2()) ) {
					alt36=1;
				}
				else if ( (synpred44_JPA2()) ) {
					alt36=2;
				}
				else if ( (synpred45_JPA2()) ) {
					alt36=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case CASE:
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 63:
			case 65:
			case 67:
			case 70:
			case 77:
			case 82:
			case 84:
			case 89:
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 102:
			case 106:
			case 110:
			case 112:
			case 115:
			case 120:
			case 130:
			case 132:
			case 133:
			case 136:
			case 137:
			case 139:
			case 144:
			case 145:
				{
				alt36=3;
				}
				break;
			case COUNT:
				{
				int LA36_16 = input.LA(2);
				if ( (synpred45_JPA2()) ) {
					alt36=3;
				}
				else if ( (synpred46_JPA2()) ) {
					alt36=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 16, input);
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
				int LA36_17 = input.LA(2);
				if ( (synpred45_JPA2()) ) {
					alt36=3;
				}
				else if ( (synpred46_JPA2()) ) {
					alt36=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 104:
				{
				int LA36_18 = input.LA(2);
				if ( (synpred45_JPA2()) ) {
					alt36=3;
				}
				else if ( (synpred46_JPA2()) ) {
					alt36=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case GROUP:
				{
				int LA36_31 = input.LA(2);
				if ( (synpred43_JPA2()) ) {
					alt36=1;
				}
				else if ( (synpred44_JPA2()) ) {
					alt36=2;
				}
				else if ( (synpred45_JPA2()) ) {
					alt36=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 31, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 123:
				{
				alt36=5;
				}
				break;
			case 117:
				{
				alt36=6;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 36, 0, input);
				throw nvae;
			}
			switch (alt36) {
				case 1 :
					// JPA2.g:175:7: path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_select_expression1419);
					path_expression97=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression97.getTree());

					// JPA2.g:175:23: ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
					int alt35=2;
					int LA35_0 = input.LA(1);
					if ( ((LA35_0 >= 64 && LA35_0 <= 65)||LA35_0==67||LA35_0==69) ) {
						alt35=1;
					}
					switch (alt35) {
						case 1 :
							// JPA2.g:175:24: ( '+' | '-' | '*' | '/' ) scalar_expression
							{
							set98=input.LT(1);
							if ( (input.LA(1) >= 64 && input.LA(1) <= 65)||input.LA(1)==67||input.LA(1)==69 ) {
								input.consume();
								if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set98));
								state.errorRecovery=false;
								state.failed=false;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							pushFollow(FOLLOW_scalar_expression_in_select_expression1438);
							scalar_expression99=scalar_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression99.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:176:7: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_select_expression1448);
					identification_variable100=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable100.getTree());
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
					// 176:31: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
					{
						// JPA2.g:176:34: ^( T_SELECTED_ENTITY[$identification_variable.text] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_ENTITY, (identification_variable100!=null?input.toString(identification_variable100.start,identification_variable100.stop):null)), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// JPA2.g:177:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_select_expression1466);
					scalar_expression101=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression101.getTree());

					}
					break;
				case 4 :
					// JPA2.g:178:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_select_expression1474);
					aggregate_expression102=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression102.getTree());

					}
					break;
				case 5 :
					// JPA2.g:179:7: 'OBJECT' '(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal103=(Token)match(input,123,FOLLOW_123_in_select_expression1482); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal103_tree = (Object)adaptor.create(string_literal103);
					adaptor.addChild(root_0, string_literal103_tree);
					}

					char_literal104=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression1484); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal104_tree = (Object)adaptor.create(char_literal104);
					adaptor.addChild(root_0, char_literal104_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_select_expression1485);
					identification_variable105=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable105.getTree());

					char_literal106=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression1486); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal106_tree = (Object)adaptor.create(char_literal106);
					adaptor.addChild(root_0, char_literal106_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:180:7: constructor_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_constructor_expression_in_select_expression1494);
					constructor_expression107=constructor_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_expression107.getTree());

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
	// JPA2.g:181:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
	public final JPA2Parser.constructor_expression_return constructor_expression() throws RecognitionException {
		JPA2Parser.constructor_expression_return retval = new JPA2Parser.constructor_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal108=null;
		Token char_literal110=null;
		Token char_literal112=null;
		Token char_literal114=null;
		ParserRuleReturnScope constructor_name109 =null;
		ParserRuleReturnScope constructor_item111 =null;
		ParserRuleReturnScope constructor_item113 =null;

		Object string_literal108_tree=null;
		Object char_literal110_tree=null;
		Object char_literal112_tree=null;
		Object char_literal114_tree=null;

		try {
			// JPA2.g:182:5: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
			// JPA2.g:182:7: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal108=(Token)match(input,117,FOLLOW_117_in_constructor_expression1505); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal108_tree = (Object)adaptor.create(string_literal108);
			adaptor.addChild(root_0, string_literal108_tree);
			}

			pushFollow(FOLLOW_constructor_name_in_constructor_expression1507);
			constructor_name109=constructor_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name109.getTree());

			char_literal110=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression1509); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal110_tree = (Object)adaptor.create(char_literal110);
			adaptor.addChild(root_0, char_literal110_tree);
			}

			pushFollow(FOLLOW_constructor_item_in_constructor_expression1511);
			constructor_item111=constructor_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item111.getTree());

			// JPA2.g:182:51: ( ',' constructor_item )*
			loop37:
			while (true) {
				int alt37=2;
				int LA37_0 = input.LA(1);
				if ( (LA37_0==66) ) {
					alt37=1;
				}

				switch (alt37) {
				case 1 :
					// JPA2.g:182:52: ',' constructor_item
					{
					char_literal112=(Token)match(input,66,FOLLOW_66_in_constructor_expression1514); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal112_tree = (Object)adaptor.create(char_literal112);
					adaptor.addChild(root_0, char_literal112_tree);
					}

					pushFollow(FOLLOW_constructor_item_in_constructor_expression1516);
					constructor_item113=constructor_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item113.getTree());

					}
					break;

				default :
					break loop37;
				}
			}

			char_literal114=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression1520); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal114_tree = (Object)adaptor.create(char_literal114);
			adaptor.addChild(root_0, char_literal114_tree);
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
	// JPA2.g:183:1: constructor_item : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.constructor_item_return constructor_item() throws RecognitionException {
		JPA2Parser.constructor_item_return retval = new JPA2Parser.constructor_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression115 =null;
		ParserRuleReturnScope scalar_expression116 =null;
		ParserRuleReturnScope aggregate_expression117 =null;
		ParserRuleReturnScope identification_variable118 =null;


		try {
			// JPA2.g:184:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt38=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA38_1 = input.LA(2);
				if ( (synpred49_JPA2()) ) {
					alt38=1;
				}
				else if ( (synpred50_JPA2()) ) {
					alt38=2;
				}
				else if ( (true) ) {
					alt38=4;
				}

				}
				break;
			case CASE:
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 63:
			case 65:
			case 67:
			case 70:
			case 77:
			case 82:
			case 84:
			case 89:
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 102:
			case 106:
			case 110:
			case 112:
			case 115:
			case 120:
			case 130:
			case 132:
			case 133:
			case 136:
			case 137:
			case 139:
			case 144:
			case 145:
				{
				alt38=2;
				}
				break;
			case COUNT:
				{
				int LA38_16 = input.LA(2);
				if ( (synpred50_JPA2()) ) {
					alt38=2;
				}
				else if ( (synpred51_JPA2()) ) {
					alt38=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 38, 16, input);
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
				int LA38_17 = input.LA(2);
				if ( (synpred50_JPA2()) ) {
					alt38=2;
				}
				else if ( (synpred51_JPA2()) ) {
					alt38=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 38, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 104:
				{
				int LA38_18 = input.LA(2);
				if ( (synpred50_JPA2()) ) {
					alt38=2;
				}
				else if ( (synpred51_JPA2()) ) {
					alt38=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 38, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case GROUP:
				{
				int LA38_31 = input.LA(2);
				if ( (synpred49_JPA2()) ) {
					alt38=1;
				}
				else if ( (synpred50_JPA2()) ) {
					alt38=2;
				}
				else if ( (true) ) {
					alt38=4;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 38, 0, input);
				throw nvae;
			}
			switch (alt38) {
				case 1 :
					// JPA2.g:184:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_constructor_item1531);
					path_expression115=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression115.getTree());

					}
					break;
				case 2 :
					// JPA2.g:185:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_constructor_item1539);
					scalar_expression116=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression116.getTree());

					}
					break;
				case 3 :
					// JPA2.g:186:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_constructor_item1547);
					aggregate_expression117=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression117.getTree());

					}
					break;
				case 4 :
					// JPA2.g:187:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_constructor_item1555);
					identification_variable118=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable118.getTree());

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
	// JPA2.g:188:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );
	public final JPA2Parser.aggregate_expression_return aggregate_expression() throws RecognitionException {
		JPA2Parser.aggregate_expression_return retval = new JPA2Parser.aggregate_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal120=null;
		Token DISTINCT121=null;
		Token char_literal123=null;
		Token string_literal124=null;
		Token char_literal125=null;
		Token DISTINCT126=null;
		Token char_literal128=null;
		ParserRuleReturnScope aggregate_expression_function_name119 =null;
		ParserRuleReturnScope arithmetic_expression122 =null;
		ParserRuleReturnScope count_argument127 =null;
		ParserRuleReturnScope function_invocation129 =null;

		Object char_literal120_tree=null;
		Object DISTINCT121_tree=null;
		Object char_literal123_tree=null;
		Object string_literal124_tree=null;
		Object char_literal125_tree=null;
		Object DISTINCT126_tree=null;
		Object char_literal128_tree=null;
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_COUNT=new RewriteRuleTokenStream(adaptor,"token COUNT");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_arithmetic_expression=new RewriteRuleSubtreeStream(adaptor,"rule arithmetic_expression");
		RewriteRuleSubtreeStream stream_count_argument=new RewriteRuleSubtreeStream(adaptor,"rule count_argument");
		RewriteRuleSubtreeStream stream_aggregate_expression_function_name=new RewriteRuleSubtreeStream(adaptor,"rule aggregate_expression_function_name");

		try {
			// JPA2.g:189:5: ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation )
			int alt41=3;
			alt41 = dfa41.predict(input);
			switch (alt41) {
				case 1 :
					// JPA2.g:189:7: aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')'
					{
					pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1566);
					aggregate_expression_function_name119=aggregate_expression_function_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name119.getTree());
					char_literal120=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1568); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal120);

					// JPA2.g:189:45: ( DISTINCT )?
					int alt39=2;
					int LA39_0 = input.LA(1);
					if ( (LA39_0==DISTINCT) ) {
						alt39=1;
					}
					switch (alt39) {
						case 1 :
							// JPA2.g:189:46: DISTINCT
							{
							DISTINCT121=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1570); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT121);

							}
							break;

					}

					pushFollow(FOLLOW_arithmetic_expression_in_aggregate_expression1574);
					arithmetic_expression122=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_arithmetic_expression.add(arithmetic_expression122.getTree());
					char_literal123=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1575); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal123);

					// AST REWRITE
					// elements: LPAREN, RPAREN, arithmetic_expression, aggregate_expression_function_name, DISTINCT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 190:5: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' )
					{
						// JPA2.g:190:8: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:190:93: ( 'DISTINCT' )?
						if ( stream_DISTINCT.hasNext() ) {
							adaptor.addChild(root_1, (Object)adaptor.create(DISTINCT, "DISTINCT"));
						}
						stream_DISTINCT.reset();

						adaptor.addChild(root_1, stream_arithmetic_expression.nextTree());
						adaptor.addChild(root_1, stream_RPAREN.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// JPA2.g:191:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
					{
					string_literal124=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1609); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COUNT.add(string_literal124);

					char_literal125=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1611); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal125);

					// JPA2.g:191:18: ( DISTINCT )?
					int alt40=2;
					int LA40_0 = input.LA(1);
					if ( (LA40_0==DISTINCT) ) {
						alt40=1;
					}
					switch (alt40) {
						case 1 :
							// JPA2.g:191:19: DISTINCT
							{
							DISTINCT126=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1613); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT126);

							}
							break;

					}

					pushFollow(FOLLOW_count_argument_in_aggregate_expression1617);
					count_argument127=count_argument();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_count_argument.add(count_argument127.getTree());
					char_literal128=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1619); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal128);

					// AST REWRITE
					// elements: LPAREN, RPAREN, DISTINCT, count_argument, COUNT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 192:5: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
					{
						// JPA2.g:192:8: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_COUNT.nextNode());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:192:66: ( 'DISTINCT' )?
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
					// JPA2.g:193:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_aggregate_expression1654);
					function_invocation129=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation129.getTree());

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
	// JPA2.g:194:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
	public final JPA2Parser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
		JPA2Parser.aggregate_expression_function_name_return retval = new JPA2Parser.aggregate_expression_function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set130=null;

		Object set130_tree=null;

		try {
			// JPA2.g:195:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set130=input.LT(1);
			if ( input.LA(1)==AVG||input.LA(1)==COUNT||(input.LA(1) >= MAX && input.LA(1) <= MIN)||input.LA(1)==SUM ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set130));
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
	// JPA2.g:196:1: count_argument : ( identification_variable | path_expression );
	public final JPA2Parser.count_argument_return count_argument() throws RecognitionException {
		JPA2Parser.count_argument_return retval = new JPA2Parser.count_argument_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable131 =null;
		ParserRuleReturnScope path_expression132 =null;


		try {
			// JPA2.g:197:5: ( identification_variable | path_expression )
			int alt42=2;
			int LA42_0 = input.LA(1);
			if ( (LA42_0==GROUP||LA42_0==WORD) ) {
				int LA42_1 = input.LA(2);
				if ( (LA42_1==RPAREN) ) {
					alt42=1;
				}
				else if ( (LA42_1==68) ) {
					alt42=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 42, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 42, 0, input);
				throw nvae;
			}

			switch (alt42) {
				case 1 :
					// JPA2.g:197:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_count_argument1691);
					identification_variable131=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable131.getTree());

					}
					break;
				case 2 :
					// JPA2.g:197:33: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_count_argument1695);
					path_expression132=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression132.getTree());

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
	// JPA2.g:198:1: where_clause : wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) ;
	public final JPA2Parser.where_clause_return where_clause() throws RecognitionException {
		JPA2Parser.where_clause_return retval = new JPA2Parser.where_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token wh=null;
		ParserRuleReturnScope conditional_expression133 =null;

		Object wh_tree=null;
		RewriteRuleTokenStream stream_142=new RewriteRuleTokenStream(adaptor,"token 142");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");

		try {
			// JPA2.g:199:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) )
			// JPA2.g:199:7: wh= 'WHERE' conditional_expression
			{
			wh=(Token)match(input,142,FOLLOW_142_in_where_clause1708); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_142.add(wh);

			pushFollow(FOLLOW_conditional_expression_in_where_clause1710);
			conditional_expression133=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression133.getTree());
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
			// 199:40: -> ^( T_CONDITION[$wh] conditional_expression )
			{
				// JPA2.g:199:43: ^( T_CONDITION[$wh] conditional_expression )
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
	// JPA2.g:200:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
	public final JPA2Parser.groupby_clause_return groupby_clause() throws RecognitionException {
		JPA2Parser.groupby_clause_return retval = new JPA2Parser.groupby_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal134=null;
		Token string_literal135=null;
		Token char_literal137=null;
		ParserRuleReturnScope groupby_item136 =null;
		ParserRuleReturnScope groupby_item138 =null;

		Object string_literal134_tree=null;
		Object string_literal135_tree=null;
		Object char_literal137_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");

		try {
			// JPA2.g:201:5: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
			// JPA2.g:201:7: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
			{
			string_literal134=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1732); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_GROUP.add(string_literal134);

			string_literal135=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1734); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal135);

			pushFollow(FOLLOW_groupby_item_in_groupby_clause1736);
			groupby_item136=groupby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item136.getTree());
			// JPA2.g:201:33: ( ',' groupby_item )*
			loop43:
			while (true) {
				int alt43=2;
				int LA43_0 = input.LA(1);
				if ( (LA43_0==66) ) {
					alt43=1;
				}

				switch (alt43) {
				case 1 :
					// JPA2.g:201:34: ',' groupby_item
					{
					char_literal137=(Token)match(input,66,FOLLOW_66_in_groupby_clause1739); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal137);

					pushFollow(FOLLOW_groupby_item_in_groupby_clause1741);
					groupby_item138=groupby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item138.getTree());
					}
					break;

				default :
					break loop43;
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
			// 202:5: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
			{
				// JPA2.g:202:8: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new GroupByNode(T_GROUP_BY), root_1);
				adaptor.addChild(root_1, stream_GROUP.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:202:49: ( groupby_item )*
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
	// JPA2.g:203:1: groupby_item : ( path_expression | identification_variable | extract_function );
	public final JPA2Parser.groupby_item_return groupby_item() throws RecognitionException {
		JPA2Parser.groupby_item_return retval = new JPA2Parser.groupby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression139 =null;
		ParserRuleReturnScope identification_variable140 =null;
		ParserRuleReturnScope extract_function141 =null;


		try {
			// JPA2.g:204:5: ( path_expression | identification_variable | extract_function )
			int alt44=3;
			int LA44_0 = input.LA(1);
			if ( (LA44_0==GROUP||LA44_0==WORD) ) {
				int LA44_1 = input.LA(2);
				if ( (LA44_1==68) ) {
					alt44=1;
				}
				else if ( (LA44_1==EOF||LA44_1==HAVING||LA44_1==ORDER||LA44_1==RPAREN||LA44_1==66) ) {
					alt44=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 44, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA44_0==102) ) {
				alt44=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 44, 0, input);
				throw nvae;
			}

			switch (alt44) {
				case 1 :
					// JPA2.g:204:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_groupby_item1775);
					path_expression139=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression139.getTree());

					}
					break;
				case 2 :
					// JPA2.g:204:25: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_groupby_item1779);
					identification_variable140=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable140.getTree());

					}
					break;
				case 3 :
					// JPA2.g:204:51: extract_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extract_function_in_groupby_item1783);
					extract_function141=extract_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extract_function141.getTree());

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
	// JPA2.g:205:1: having_clause : 'HAVING' conditional_expression ;
	public final JPA2Parser.having_clause_return having_clause() throws RecognitionException {
		JPA2Parser.having_clause_return retval = new JPA2Parser.having_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal142=null;
		ParserRuleReturnScope conditional_expression143 =null;

		Object string_literal142_tree=null;

		try {
			// JPA2.g:206:5: ( 'HAVING' conditional_expression )
			// JPA2.g:206:7: 'HAVING' conditional_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal142=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1794); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal142_tree = (Object)adaptor.create(string_literal142);
			adaptor.addChild(root_0, string_literal142_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_having_clause1796);
			conditional_expression143=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression143.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:207:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
	public final JPA2Parser.orderby_clause_return orderby_clause() throws RecognitionException {
		JPA2Parser.orderby_clause_return retval = new JPA2Parser.orderby_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal144=null;
		Token string_literal145=null;
		Token char_literal147=null;
		ParserRuleReturnScope orderby_item146 =null;
		ParserRuleReturnScope orderby_item148 =null;

		Object string_literal144_tree=null;
		Object string_literal145_tree=null;
		Object char_literal147_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");

		try {
			// JPA2.g:208:5: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
			// JPA2.g:208:7: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
			{
			string_literal144=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1807); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ORDER.add(string_literal144);

			string_literal145=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1809); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal145);

			pushFollow(FOLLOW_orderby_item_in_orderby_clause1811);
			orderby_item146=orderby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item146.getTree());
			// JPA2.g:208:33: ( ',' orderby_item )*
			loop45:
			while (true) {
				int alt45=2;
				int LA45_0 = input.LA(1);
				if ( (LA45_0==66) ) {
					alt45=1;
				}

				switch (alt45) {
				case 1 :
					// JPA2.g:208:34: ',' orderby_item
					{
					char_literal147=(Token)match(input,66,FOLLOW_66_in_orderby_clause1814); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal147);

					pushFollow(FOLLOW_orderby_item_in_orderby_clause1816);
					orderby_item148=orderby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item148.getTree());
					}
					break;

				default :
					break loop45;
				}
			}

			// AST REWRITE
			// elements: ORDER, orderby_item, BY
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 209:5: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
			{
				// JPA2.g:209:8: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByNode(T_ORDER_BY), root_1);
				adaptor.addChild(root_1, stream_ORDER.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:209:49: ( orderby_item )*
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
	// JPA2.g:210:1: orderby_item : orderby_variable ( sort )? ( sortNulls )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? ) ;
	public final JPA2Parser.orderby_item_return orderby_item() throws RecognitionException {
		JPA2Parser.orderby_item_return retval = new JPA2Parser.orderby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope orderby_variable149 =null;
		ParserRuleReturnScope sort150 =null;
		ParserRuleReturnScope sortNulls151 =null;

		RewriteRuleSubtreeStream stream_sortNulls=new RewriteRuleSubtreeStream(adaptor,"rule sortNulls");
		RewriteRuleSubtreeStream stream_orderby_variable=new RewriteRuleSubtreeStream(adaptor,"rule orderby_variable");
		RewriteRuleSubtreeStream stream_sort=new RewriteRuleSubtreeStream(adaptor,"rule sort");

		try {
			// JPA2.g:211:5: ( orderby_variable ( sort )? ( sortNulls )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? ) )
			// JPA2.g:211:7: orderby_variable ( sort )? ( sortNulls )?
			{
			pushFollow(FOLLOW_orderby_variable_in_orderby_item1850);
			orderby_variable149=orderby_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable149.getTree());
			// JPA2.g:211:24: ( sort )?
			int alt46=2;
			int LA46_0 = input.LA(1);
			if ( (LA46_0==ASC||LA46_0==DESC) ) {
				alt46=1;
			}
			switch (alt46) {
				case 1 :
					// JPA2.g:211:24: sort
					{
					pushFollow(FOLLOW_sort_in_orderby_item1852);
					sort150=sort();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sort.add(sort150.getTree());
					}
					break;

			}

			// JPA2.g:211:30: ( sortNulls )?
			int alt47=2;
			int LA47_0 = input.LA(1);
			if ( ((LA47_0 >= 121 && LA47_0 <= 122)) ) {
				alt47=1;
			}
			switch (alt47) {
				case 1 :
					// JPA2.g:211:30: sortNulls
					{
					pushFollow(FOLLOW_sortNulls_in_orderby_item1855);
					sortNulls151=sortNulls();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sortNulls.add(sortNulls151.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: orderby_variable, sort, sortNulls
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 212:6: -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? )
			{
				// JPA2.g:212:9: ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);
				adaptor.addChild(root_1, stream_orderby_variable.nextTree());
				// JPA2.g:212:65: ( sort )?
				if ( stream_sort.hasNext() ) {
					adaptor.addChild(root_1, stream_sort.nextTree());
				}
				stream_sort.reset();

				// JPA2.g:212:71: ( sortNulls )?
				if ( stream_sortNulls.hasNext() ) {
					adaptor.addChild(root_1, stream_sortNulls.nextTree());
				}
				stream_sortNulls.reset();

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
	// JPA2.g:213:1: orderby_variable : ( path_expression | general_identification_variable | result_variable | scalar_expression | aggregate_expression );
	public final JPA2Parser.orderby_variable_return orderby_variable() throws RecognitionException {
		JPA2Parser.orderby_variable_return retval = new JPA2Parser.orderby_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression152 =null;
		ParserRuleReturnScope general_identification_variable153 =null;
		ParserRuleReturnScope result_variable154 =null;
		ParserRuleReturnScope scalar_expression155 =null;
		ParserRuleReturnScope aggregate_expression156 =null;


		try {
			// JPA2.g:214:5: ( path_expression | general_identification_variable | result_variable | scalar_expression | aggregate_expression )
			int alt48=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA48_1 = input.LA(2);
				if ( (synpred67_JPA2()) ) {
					alt48=1;
				}
				else if ( (synpred68_JPA2()) ) {
					alt48=2;
				}
				else if ( (synpred69_JPA2()) ) {
					alt48=3;
				}
				else if ( (synpred70_JPA2()) ) {
					alt48=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 48, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 108:
			case 140:
				{
				alt48=2;
				}
				break;
			case GROUP:
				{
				int LA48_4 = input.LA(2);
				if ( (synpred67_JPA2()) ) {
					alt48=1;
				}
				else if ( (synpred68_JPA2()) ) {
					alt48=2;
				}
				else if ( (synpred70_JPA2()) ) {
					alt48=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 48, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case CASE:
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 63:
			case 65:
			case 67:
			case 70:
			case 77:
			case 82:
			case 84:
			case 89:
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 102:
			case 106:
			case 110:
			case 112:
			case 115:
			case 120:
			case 130:
			case 132:
			case 133:
			case 136:
			case 137:
			case 139:
			case 144:
			case 145:
				{
				alt48=4;
				}
				break;
			case COUNT:
				{
				int LA48_19 = input.LA(2);
				if ( (synpred70_JPA2()) ) {
					alt48=4;
				}
				else if ( (true) ) {
					alt48=5;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA48_20 = input.LA(2);
				if ( (synpred70_JPA2()) ) {
					alt48=4;
				}
				else if ( (true) ) {
					alt48=5;
				}

				}
				break;
			case 104:
				{
				int LA48_21 = input.LA(2);
				if ( (synpred70_JPA2()) ) {
					alt48=4;
				}
				else if ( (true) ) {
					alt48=5;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 48, 0, input);
				throw nvae;
			}
			switch (alt48) {
				case 1 :
					// JPA2.g:214:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_orderby_variable1890);
					path_expression152=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression152.getTree());

					}
					break;
				case 2 :
					// JPA2.g:214:25: general_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_identification_variable_in_orderby_variable1894);
					general_identification_variable153=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable153.getTree());

					}
					break;
				case 3 :
					// JPA2.g:214:59: result_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_result_variable_in_orderby_variable1898);
					result_variable154=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable154.getTree());

					}
					break;
				case 4 :
					// JPA2.g:214:77: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_orderby_variable1902);
					scalar_expression155=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression155.getTree());

					}
					break;
				case 5 :
					// JPA2.g:214:97: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_orderby_variable1906);
					aggregate_expression156=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression156.getTree());

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
	// JPA2.g:215:1: sort : ( 'ASC' | 'DESC' ) ;
	public final JPA2Parser.sort_return sort() throws RecognitionException {
		JPA2Parser.sort_return retval = new JPA2Parser.sort_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set157=null;

		Object set157_tree=null;

		try {
			// JPA2.g:216:5: ( ( 'ASC' | 'DESC' ) )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set157=input.LT(1);
			if ( input.LA(1)==ASC||input.LA(1)==DESC ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set157));
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


	public static class sortNulls_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "sortNulls"
	// JPA2.g:217:1: sortNulls : ( 'NULLS FIRST' | 'NULLS LAST' ) ;
	public final JPA2Parser.sortNulls_return sortNulls() throws RecognitionException {
		JPA2Parser.sortNulls_return retval = new JPA2Parser.sortNulls_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set158=null;

		Object set158_tree=null;

		try {
			// JPA2.g:218:5: ( ( 'NULLS FIRST' | 'NULLS LAST' ) )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set158=input.LT(1);
			if ( (input.LA(1) >= 121 && input.LA(1) <= 122) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set158));
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
	// $ANTLR end "sortNulls"


	public static class subquery_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "subquery"
	// JPA2.g:219:1: subquery : lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
	public final JPA2Parser.subquery_return subquery() throws RecognitionException {
		JPA2Parser.subquery_return retval = new JPA2Parser.subquery_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token lp=null;
		Token rp=null;
		Token string_literal159=null;
		ParserRuleReturnScope simple_select_clause160 =null;
		ParserRuleReturnScope subquery_from_clause161 =null;
		ParserRuleReturnScope where_clause162 =null;
		ParserRuleReturnScope groupby_clause163 =null;
		ParserRuleReturnScope having_clause164 =null;

		Object lp_tree=null;
		Object rp_tree=null;
		Object string_literal159_tree=null;
		RewriteRuleTokenStream stream_129=new RewriteRuleTokenStream(adaptor,"token 129");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
		RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");

		try {
			// JPA2.g:220:5: (lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
			// JPA2.g:220:7: lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
			{
			lp=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subquery1953); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(lp);

			string_literal159=(Token)match(input,129,FOLLOW_129_in_subquery1955); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_129.add(string_literal159);

			pushFollow(FOLLOW_simple_select_clause_in_subquery1957);
			simple_select_clause160=simple_select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause160.getTree());
			pushFollow(FOLLOW_subquery_from_clause_in_subquery1959);
			subquery_from_clause161=subquery_from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause161.getTree());
			// JPA2.g:220:65: ( where_clause )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==142) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// JPA2.g:220:66: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_subquery1962);
					where_clause162=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause162.getTree());
					}
					break;

			}

			// JPA2.g:220:81: ( groupby_clause )?
			int alt50=2;
			int LA50_0 = input.LA(1);
			if ( (LA50_0==GROUP) ) {
				alt50=1;
			}
			switch (alt50) {
				case 1 :
					// JPA2.g:220:82: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_subquery1967);
					groupby_clause163=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause163.getTree());
					}
					break;

			}

			// JPA2.g:220:99: ( having_clause )?
			int alt51=2;
			int LA51_0 = input.LA(1);
			if ( (LA51_0==HAVING) ) {
				alt51=1;
			}
			switch (alt51) {
				case 1 :
					// JPA2.g:220:100: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_subquery1972);
					having_clause164=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause164.getTree());
					}
					break;

			}

			rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1978); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(rp);

			// AST REWRITE
			// elements: subquery_from_clause, groupby_clause, simple_select_clause, 129, where_clause, having_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 221:6: -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
			{
				// JPA2.g:221:9: ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);
				adaptor.addChild(root_1, stream_129.nextNode());
				adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
				adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
				// JPA2.g:221:90: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:221:106: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:221:124: ( having_clause )?
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
	// JPA2.g:222:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
	public final JPA2Parser.subquery_from_clause_return subquery_from_clause() throws RecognitionException {
		JPA2Parser.subquery_from_clause_return retval = new JPA2Parser.subquery_from_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token fr=null;
		Token char_literal166=null;
		ParserRuleReturnScope subselect_identification_variable_declaration165 =null;
		ParserRuleReturnScope subselect_identification_variable_declaration167 =null;

		Object fr_tree=null;
		Object char_literal166_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_103=new RewriteRuleTokenStream(adaptor,"token 103");
		RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");

		try {
			// JPA2.g:223:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
			// JPA2.g:223:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
			{
			fr=(Token)match(input,103,FOLLOW_103_in_subquery_from_clause2028); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_103.add(fr);

			pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause2030);
			subselect_identification_variable_declaration165=subselect_identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration165.getTree());
			// JPA2.g:223:63: ( ',' subselect_identification_variable_declaration )*
			loop52:
			while (true) {
				int alt52=2;
				int LA52_0 = input.LA(1);
				if ( (LA52_0==66) ) {
					alt52=1;
				}

				switch (alt52) {
				case 1 :
					// JPA2.g:223:64: ',' subselect_identification_variable_declaration
					{
					char_literal166=(Token)match(input,66,FOLLOW_66_in_subquery_from_clause2033); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal166);

					pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause2035);
					subselect_identification_variable_declaration167=subselect_identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration167.getTree());
					}
					break;

				default :
					break loop52;
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
			// 224:5: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
			{
				// JPA2.g:224:8: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				// JPA2.g:224:35: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
				while ( stream_subselect_identification_variable_declaration.hasNext() ) {
					// JPA2.g:224:35: ^( T_SOURCE subselect_identification_variable_declaration )
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
	// JPA2.g:226:1: subselect_identification_variable_declaration : ( identification_variable_declaration | derived_path_expression AS identification_variable ( join )* | derived_collection_member_declaration );
	public final JPA2Parser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration() throws RecognitionException {
		JPA2Parser.subselect_identification_variable_declaration_return retval = new JPA2Parser.subselect_identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AS170=null;
		ParserRuleReturnScope identification_variable_declaration168 =null;
		ParserRuleReturnScope derived_path_expression169 =null;
		ParserRuleReturnScope identification_variable171 =null;
		ParserRuleReturnScope join172 =null;
		ParserRuleReturnScope derived_collection_member_declaration173 =null;

		Object AS170_tree=null;

		try {
			// JPA2.g:227:5: ( identification_variable_declaration | derived_path_expression AS identification_variable ( join )* | derived_collection_member_declaration )
			int alt54=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA54_1 = input.LA(2);
				if ( (LA54_1==AS||LA54_1==GROUP||LA54_1==WORD) ) {
					alt54=1;
				}
				else if ( (LA54_1==68) ) {
					alt54=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 54, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 135:
				{
				alt54=2;
				}
				break;
			case IN:
				{
				alt54=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 54, 0, input);
				throw nvae;
			}
			switch (alt54) {
				case 1 :
					// JPA2.g:227:7: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration2073);
					identification_variable_declaration168=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration168.getTree());

					}
					break;
				case 2 :
					// JPA2.g:228:7: derived_path_expression AS identification_variable ( join )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration2081);
					derived_path_expression169=derived_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_path_expression169.getTree());

					AS170=(Token)match(input,AS,FOLLOW_AS_in_subselect_identification_variable_declaration2083); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AS170_tree = (Object)adaptor.create(AS170);
					adaptor.addChild(root_0, AS170_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration2085);
					identification_variable171=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable171.getTree());

					// JPA2.g:228:58: ( join )*
					loop53:
					while (true) {
						int alt53=2;
						int LA53_0 = input.LA(1);
						if ( (LA53_0==INNER||(LA53_0 >= JOIN && LA53_0 <= LEFT)) ) {
							alt53=1;
						}

						switch (alt53) {
						case 1 :
							// JPA2.g:228:59: join
							{
							pushFollow(FOLLOW_join_in_subselect_identification_variable_declaration2088);
							join172=join();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, join172.getTree());

							}
							break;

						default :
							break loop53;
						}
					}

					}
					break;
				case 3 :
					// JPA2.g:229:7: derived_collection_member_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration2098);
					derived_collection_member_declaration173=derived_collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_collection_member_declaration173.getTree());

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
	// JPA2.g:230:1: derived_path_expression : ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field );
	public final JPA2Parser.derived_path_expression_return derived_path_expression() throws RecognitionException {
		JPA2Parser.derived_path_expression_return retval = new JPA2Parser.derived_path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal175=null;
		Token char_literal178=null;
		ParserRuleReturnScope general_derived_path174 =null;
		ParserRuleReturnScope single_valued_object_field176 =null;
		ParserRuleReturnScope general_derived_path177 =null;
		ParserRuleReturnScope collection_valued_field179 =null;

		Object char_literal175_tree=null;
		Object char_literal178_tree=null;

		try {
			// JPA2.g:231:5: ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field )
			int alt55=2;
			int LA55_0 = input.LA(1);
			if ( (LA55_0==WORD) ) {
				int LA55_1 = input.LA(2);
				if ( (synpred80_JPA2()) ) {
					alt55=1;
				}
				else if ( (true) ) {
					alt55=2;
				}

			}
			else if ( (LA55_0==135) ) {
				int LA55_2 = input.LA(2);
				if ( (synpred80_JPA2()) ) {
					alt55=1;
				}
				else if ( (true) ) {
					alt55=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 55, 0, input);
				throw nvae;
			}

			switch (alt55) {
				case 1 :
					// JPA2.g:231:7: general_derived_path '.' single_valued_object_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression2109);
					general_derived_path174=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path174.getTree());

					char_literal175=(Token)match(input,68,FOLLOW_68_in_derived_path_expression2110); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal175_tree = (Object)adaptor.create(char_literal175);
					adaptor.addChild(root_0, char_literal175_tree);
					}

					pushFollow(FOLLOW_single_valued_object_field_in_derived_path_expression2111);
					single_valued_object_field176=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field176.getTree());

					}
					break;
				case 2 :
					// JPA2.g:232:7: general_derived_path '.' collection_valued_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression2119);
					general_derived_path177=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path177.getTree());

					char_literal178=(Token)match(input,68,FOLLOW_68_in_derived_path_expression2120); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal178_tree = (Object)adaptor.create(char_literal178);
					adaptor.addChild(root_0, char_literal178_tree);
					}

					pushFollow(FOLLOW_collection_valued_field_in_derived_path_expression2121);
					collection_valued_field179=collection_valued_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field179.getTree());

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
	// JPA2.g:233:1: general_derived_path : ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* );
	public final JPA2Parser.general_derived_path_return general_derived_path() throws RecognitionException {
		JPA2Parser.general_derived_path_return retval = new JPA2Parser.general_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal182=null;
		ParserRuleReturnScope simple_derived_path180 =null;
		ParserRuleReturnScope treated_derived_path181 =null;
		ParserRuleReturnScope single_valued_object_field183 =null;

		Object char_literal182_tree=null;

		try {
			// JPA2.g:234:5: ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* )
			int alt57=2;
			int LA57_0 = input.LA(1);
			if ( (LA57_0==WORD) ) {
				alt57=1;
			}
			else if ( (LA57_0==135) ) {
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
					// JPA2.g:234:7: simple_derived_path
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_derived_path_in_general_derived_path2132);
					simple_derived_path180=simple_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_derived_path180.getTree());

					}
					break;
				case 2 :
					// JPA2.g:235:7: treated_derived_path ( '.' single_valued_object_field )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_treated_derived_path_in_general_derived_path2140);
					treated_derived_path181=treated_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, treated_derived_path181.getTree());

					// JPA2.g:235:27: ( '.' single_valued_object_field )*
					loop56:
					while (true) {
						int alt56=2;
						int LA56_0 = input.LA(1);
						if ( (LA56_0==68) ) {
							int LA56_1 = input.LA(2);
							if ( (LA56_1==WORD) ) {
								int LA56_3 = input.LA(3);
								if ( (LA56_3==AS) ) {
									int LA56_4 = input.LA(4);
									if ( (LA56_4==WORD) ) {
										int LA56_6 = input.LA(5);
										if ( (LA56_6==RPAREN) ) {
											int LA56_7 = input.LA(6);
											if ( (LA56_7==AS) ) {
												int LA56_8 = input.LA(7);
												if ( (LA56_8==WORD) ) {
													int LA56_9 = input.LA(8);
													if ( (LA56_9==RPAREN) ) {
														alt56=1;
													}

												}

											}
											else if ( (LA56_7==68) ) {
												alt56=1;
											}

										}

									}

								}
								else if ( (LA56_3==68) ) {
									alt56=1;
								}

							}

						}

						switch (alt56) {
						case 1 :
							// JPA2.g:235:28: '.' single_valued_object_field
							{
							char_literal182=(Token)match(input,68,FOLLOW_68_in_general_derived_path2142); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal182_tree = (Object)adaptor.create(char_literal182);
							adaptor.addChild(root_0, char_literal182_tree);
							}

							pushFollow(FOLLOW_single_valued_object_field_in_general_derived_path2143);
							single_valued_object_field183=single_valued_object_field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field183.getTree());

							}
							break;

						default :
							break loop56;
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
	// JPA2.g:237:1: simple_derived_path : superquery_identification_variable ;
	public final JPA2Parser.simple_derived_path_return simple_derived_path() throws RecognitionException {
		JPA2Parser.simple_derived_path_return retval = new JPA2Parser.simple_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope superquery_identification_variable184 =null;


		try {
			// JPA2.g:238:5: ( superquery_identification_variable )
			// JPA2.g:238:7: superquery_identification_variable
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_superquery_identification_variable_in_simple_derived_path2161);
			superquery_identification_variable184=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable184.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:240:1: treated_derived_path : 'TREAT(' general_derived_path AS subtype ')' ;
	public final JPA2Parser.treated_derived_path_return treated_derived_path() throws RecognitionException {
		JPA2Parser.treated_derived_path_return retval = new JPA2Parser.treated_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal185=null;
		Token AS187=null;
		Token char_literal189=null;
		ParserRuleReturnScope general_derived_path186 =null;
		ParserRuleReturnScope subtype188 =null;

		Object string_literal185_tree=null;
		Object AS187_tree=null;
		Object char_literal189_tree=null;

		try {
			// JPA2.g:241:5: ( 'TREAT(' general_derived_path AS subtype ')' )
			// JPA2.g:241:7: 'TREAT(' general_derived_path AS subtype ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal185=(Token)match(input,135,FOLLOW_135_in_treated_derived_path2178); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal185_tree = (Object)adaptor.create(string_literal185);
			adaptor.addChild(root_0, string_literal185_tree);
			}

			pushFollow(FOLLOW_general_derived_path_in_treated_derived_path2179);
			general_derived_path186=general_derived_path();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path186.getTree());

			AS187=(Token)match(input,AS,FOLLOW_AS_in_treated_derived_path2181); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			AS187_tree = (Object)adaptor.create(AS187);
			adaptor.addChild(root_0, AS187_tree);
			}

			pushFollow(FOLLOW_subtype_in_treated_derived_path2183);
			subtype188=subtype();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subtype188.getTree());

			char_literal189=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_treated_derived_path2185); if (state.failed) return retval;
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
	// JPA2.g:242:1: derived_collection_member_declaration : 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field ;
	public final JPA2Parser.derived_collection_member_declaration_return derived_collection_member_declaration() throws RecognitionException {
		JPA2Parser.derived_collection_member_declaration_return retval = new JPA2Parser.derived_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal190=null;
		Token char_literal192=null;
		Token char_literal194=null;
		ParserRuleReturnScope superquery_identification_variable191 =null;
		ParserRuleReturnScope single_valued_object_field193 =null;
		ParserRuleReturnScope collection_valued_field195 =null;

		Object string_literal190_tree=null;
		Object char_literal192_tree=null;
		Object char_literal194_tree=null;

		try {
			// JPA2.g:243:5: ( 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field )
			// JPA2.g:243:7: 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field
			{
			root_0 = (Object)adaptor.nil();


			string_literal190=(Token)match(input,IN,FOLLOW_IN_in_derived_collection_member_declaration2196); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal190_tree = (Object)adaptor.create(string_literal190);
			adaptor.addChild(root_0, string_literal190_tree);
			}

			pushFollow(FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2198);
			superquery_identification_variable191=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable191.getTree());

			char_literal192=(Token)match(input,68,FOLLOW_68_in_derived_collection_member_declaration2199); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal192_tree = (Object)adaptor.create(char_literal192);
			adaptor.addChild(root_0, char_literal192_tree);
			}

			// JPA2.g:243:49: ( single_valued_object_field '.' )*
			loop58:
			while (true) {
				int alt58=2;
				int LA58_0 = input.LA(1);
				if ( (LA58_0==WORD) ) {
					int LA58_1 = input.LA(2);
					if ( (LA58_1==68) ) {
						alt58=1;
					}

				}

				switch (alt58) {
				case 1 :
					// JPA2.g:243:50: single_valued_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2201);
					single_valued_object_field193=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field193.getTree());

					char_literal194=(Token)match(input,68,FOLLOW_68_in_derived_collection_member_declaration2203); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal194_tree = (Object)adaptor.create(char_literal194);
					adaptor.addChild(root_0, char_literal194_tree);
					}

					}
					break;

				default :
					break loop58;
				}
			}

			pushFollow(FOLLOW_collection_valued_field_in_derived_collection_member_declaration2206);
			collection_valued_field195=collection_valued_field();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field195.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:245:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
	public final JPA2Parser.simple_select_clause_return simple_select_clause() throws RecognitionException {
		JPA2Parser.simple_select_clause_return retval = new JPA2Parser.simple_select_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal196=null;
		ParserRuleReturnScope simple_select_expression197 =null;

		Object string_literal196_tree=null;
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleSubtreeStream stream_simple_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_expression");

		try {
			// JPA2.g:246:5: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
			// JPA2.g:246:7: ( 'DISTINCT' )? simple_select_expression
			{
			// JPA2.g:246:7: ( 'DISTINCT' )?
			int alt59=2;
			int LA59_0 = input.LA(1);
			if ( (LA59_0==DISTINCT) ) {
				alt59=1;
			}
			switch (alt59) {
				case 1 :
					// JPA2.g:246:8: 'DISTINCT'
					{
					string_literal196=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause2219); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal196);

					}
					break;

			}

			pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause2223);
			simple_select_expression197=simple_select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_expression.add(simple_select_expression197.getTree());
			// AST REWRITE
			// elements: DISTINCT, simple_select_expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 247:5: -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
			{
				// JPA2.g:247:8: ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:247:48: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);
				// JPA2.g:247:86: ( 'DISTINCT' )?
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
	// JPA2.g:248:1: simple_select_expression : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.simple_select_expression_return simple_select_expression() throws RecognitionException {
		JPA2Parser.simple_select_expression_return retval = new JPA2Parser.simple_select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression198 =null;
		ParserRuleReturnScope scalar_expression199 =null;
		ParserRuleReturnScope aggregate_expression200 =null;
		ParserRuleReturnScope identification_variable201 =null;


		try {
			// JPA2.g:249:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt60=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA60_1 = input.LA(2);
				if ( (synpred85_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred86_JPA2()) ) {
					alt60=2;
				}
				else if ( (true) ) {
					alt60=4;
				}

				}
				break;
			case CASE:
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 63:
			case 65:
			case 67:
			case 70:
			case 77:
			case 82:
			case 84:
			case 89:
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 102:
			case 106:
			case 110:
			case 112:
			case 115:
			case 120:
			case 130:
			case 132:
			case 133:
			case 136:
			case 137:
			case 139:
			case 144:
			case 145:
				{
				alt60=2;
				}
				break;
			case COUNT:
				{
				int LA60_16 = input.LA(2);
				if ( (synpred86_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred87_JPA2()) ) {
					alt60=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 60, 16, input);
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
				int LA60_17 = input.LA(2);
				if ( (synpred86_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred87_JPA2()) ) {
					alt60=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 60, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 104:
				{
				int LA60_18 = input.LA(2);
				if ( (synpred86_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred87_JPA2()) ) {
					alt60=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 60, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case GROUP:
				{
				int LA60_31 = input.LA(2);
				if ( (synpred85_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred86_JPA2()) ) {
					alt60=2;
				}
				else if ( (true) ) {
					alt60=4;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 60, 0, input);
				throw nvae;
			}
			switch (alt60) {
				case 1 :
					// JPA2.g:249:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_simple_select_expression2263);
					path_expression198=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression198.getTree());

					}
					break;
				case 2 :
					// JPA2.g:250:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_simple_select_expression2271);
					scalar_expression199=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression199.getTree());

					}
					break;
				case 3 :
					// JPA2.g:251:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression2279);
					aggregate_expression200=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression200.getTree());

					}
					break;
				case 4 :
					// JPA2.g:252:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_select_expression2287);
					identification_variable201=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable201.getTree());

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
	// JPA2.g:253:1: scalar_expression : ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression );
	public final JPA2Parser.scalar_expression_return scalar_expression() throws RecognitionException {
		JPA2Parser.scalar_expression_return retval = new JPA2Parser.scalar_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope arithmetic_expression202 =null;
		ParserRuleReturnScope string_expression203 =null;
		ParserRuleReturnScope enum_expression204 =null;
		ParserRuleReturnScope datetime_expression205 =null;
		ParserRuleReturnScope boolean_expression206 =null;
		ParserRuleReturnScope case_expression207 =null;
		ParserRuleReturnScope entity_type_expression208 =null;


		try {
			// JPA2.g:254:5: ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression )
			int alt61=7;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 65:
			case 67:
			case 70:
			case 84:
			case 106:
			case 110:
			case 112:
			case 115:
			case 130:
			case 132:
				{
				alt61=1;
				}
				break;
			case WORD:
				{
				int LA61_2 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}
				else if ( (true) ) {
					alt61=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA61_5 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 77:
				{
				int LA61_6 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}
				else if ( (true) ) {
					alt61=7;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA61_7 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}
				else if ( (true) ) {
					alt61=7;
				}

				}
				break;
			case 63:
				{
				int LA61_8 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}
				else if ( (true) ) {
					alt61=7;
				}

				}
				break;
			case COUNT:
				{
				int LA61_16 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 16, input);
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
				int LA61_17 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 104:
				{
				int LA61_18 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case CASE:
				{
				int LA61_19 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}
				else if ( (synpred93_JPA2()) ) {
					alt61=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 19, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 90:
				{
				int LA61_20 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}
				else if ( (synpred93_JPA2()) ) {
					alt61=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 20, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 120:
				{
				int LA61_21 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}
				else if ( (synpred93_JPA2()) ) {
					alt61=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 21, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 89:
				{
				int LA61_22 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 22, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 102:
				{
				int LA61_23 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 23, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 82:
				{
				int LA61_24 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 24, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 91:
			case 133:
			case 136:
			case 139:
				{
				alt61=2;
				}
				break;
			case GROUP:
				{
				int LA61_31 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt61=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 61, 31, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 92:
			case 93:
			case 94:
				{
				alt61=4;
				}
				break;
			case 144:
			case 145:
				{
				alt61=5;
				}
				break;
			case 137:
				{
				alt61=7;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 61, 0, input);
				throw nvae;
			}
			switch (alt61) {
				case 1 :
					// JPA2.g:254:7: arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_scalar_expression2298);
					arithmetic_expression202=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression202.getTree());

					}
					break;
				case 2 :
					// JPA2.g:255:7: string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_scalar_expression2306);
					string_expression203=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression203.getTree());

					}
					break;
				case 3 :
					// JPA2.g:256:7: enum_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_scalar_expression2314);
					enum_expression204=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression204.getTree());

					}
					break;
				case 4 :
					// JPA2.g:257:7: datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_scalar_expression2322);
					datetime_expression205=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression205.getTree());

					}
					break;
				case 5 :
					// JPA2.g:258:7: boolean_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_scalar_expression2330);
					boolean_expression206=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression206.getTree());

					}
					break;
				case 6 :
					// JPA2.g:259:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_scalar_expression2338);
					case_expression207=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression207.getTree());

					}
					break;
				case 7 :
					// JPA2.g:260:7: entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_scalar_expression2346);
					entity_type_expression208=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression208.getTree());

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
	// JPA2.g:261:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
	public final JPA2Parser.conditional_expression_return conditional_expression() throws RecognitionException {
		JPA2Parser.conditional_expression_return retval = new JPA2Parser.conditional_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal210=null;
		ParserRuleReturnScope conditional_term209 =null;
		ParserRuleReturnScope conditional_term211 =null;

		Object string_literal210_tree=null;

		try {
			// JPA2.g:262:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
			// JPA2.g:262:7: ( conditional_term ) ( 'OR' conditional_term )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:262:7: ( conditional_term )
			// JPA2.g:262:8: conditional_term
			{
			pushFollow(FOLLOW_conditional_term_in_conditional_expression2358);
			conditional_term209=conditional_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term209.getTree());

			}

			// JPA2.g:262:26: ( 'OR' conditional_term )*
			loop62:
			while (true) {
				int alt62=2;
				int LA62_0 = input.LA(1);
				if ( (LA62_0==OR) ) {
					alt62=1;
				}

				switch (alt62) {
				case 1 :
					// JPA2.g:262:27: 'OR' conditional_term
					{
					string_literal210=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression2362); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal210_tree = (Object)adaptor.create(string_literal210);
					adaptor.addChild(root_0, string_literal210_tree);
					}

					pushFollow(FOLLOW_conditional_term_in_conditional_expression2364);
					conditional_term211=conditional_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term211.getTree());

					}
					break;

				default :
					break loop62;
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
	// JPA2.g:263:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
	public final JPA2Parser.conditional_term_return conditional_term() throws RecognitionException {
		JPA2Parser.conditional_term_return retval = new JPA2Parser.conditional_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal213=null;
		ParserRuleReturnScope conditional_factor212 =null;
		ParserRuleReturnScope conditional_factor214 =null;

		Object string_literal213_tree=null;

		try {
			// JPA2.g:264:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
			// JPA2.g:264:7: ( conditional_factor ) ( 'AND' conditional_factor )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:264:7: ( conditional_factor )
			// JPA2.g:264:8: conditional_factor
			{
			pushFollow(FOLLOW_conditional_factor_in_conditional_term2378);
			conditional_factor212=conditional_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor212.getTree());

			}

			// JPA2.g:264:28: ( 'AND' conditional_factor )*
			loop63:
			while (true) {
				int alt63=2;
				int LA63_0 = input.LA(1);
				if ( (LA63_0==AND) ) {
					alt63=1;
				}

				switch (alt63) {
				case 1 :
					// JPA2.g:264:29: 'AND' conditional_factor
					{
					string_literal213=(Token)match(input,AND,FOLLOW_AND_in_conditional_term2382); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal213_tree = (Object)adaptor.create(string_literal213);
					adaptor.addChild(root_0, string_literal213_tree);
					}

					pushFollow(FOLLOW_conditional_factor_in_conditional_term2384);
					conditional_factor214=conditional_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor214.getTree());

					}
					break;

				default :
					break loop63;
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
	// JPA2.g:265:1: conditional_factor : ( 'NOT' )? conditional_primary ;
	public final JPA2Parser.conditional_factor_return conditional_factor() throws RecognitionException {
		JPA2Parser.conditional_factor_return retval = new JPA2Parser.conditional_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal215=null;
		ParserRuleReturnScope conditional_primary216 =null;

		Object string_literal215_tree=null;

		try {
			// JPA2.g:266:5: ( ( 'NOT' )? conditional_primary )
			// JPA2.g:266:7: ( 'NOT' )? conditional_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:266:7: ( 'NOT' )?
			int alt64=2;
			int LA64_0 = input.LA(1);
			if ( (LA64_0==NOT) ) {
				int LA64_1 = input.LA(2);
				if ( (synpred96_JPA2()) ) {
					alt64=1;
				}
			}
			switch (alt64) {
				case 1 :
					// JPA2.g:266:8: 'NOT'
					{
					string_literal215=(Token)match(input,NOT,FOLLOW_NOT_in_conditional_factor2398); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal215_tree = (Object)adaptor.create(string_literal215);
					adaptor.addChild(root_0, string_literal215_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_conditional_primary_in_conditional_factor2402);
			conditional_primary216=conditional_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_primary216.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:267:1: conditional_primary : ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' );
	public final JPA2Parser.conditional_primary_return conditional_primary() throws RecognitionException {
		JPA2Parser.conditional_primary_return retval = new JPA2Parser.conditional_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal218=null;
		Token char_literal220=null;
		ParserRuleReturnScope simple_cond_expression217 =null;
		ParserRuleReturnScope conditional_expression219 =null;

		Object char_literal218_tree=null;
		Object char_literal220_tree=null;
		RewriteRuleSubtreeStream stream_simple_cond_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_cond_expression");

		try {
			// JPA2.g:268:5: ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' )
			int alt65=2;
			int LA65_0 = input.LA(1);
			if ( (LA65_0==AVG||LA65_0==CASE||LA65_0==COUNT||LA65_0==GROUP||LA65_0==INT_NUMERAL||LA65_0==LOWER||(LA65_0 >= MAX && LA65_0 <= NOT)||(LA65_0 >= STRING_LITERAL && LA65_0 <= SUM)||LA65_0==WORD||LA65_0==63||LA65_0==65||LA65_0==67||LA65_0==70||(LA65_0 >= 77 && LA65_0 <= 84)||(LA65_0 >= 89 && LA65_0 <= 94)||(LA65_0 >= 101 && LA65_0 <= 102)||LA65_0==104||LA65_0==106||LA65_0==110||LA65_0==112||LA65_0==115||LA65_0==120||LA65_0==130||(LA65_0 >= 132 && LA65_0 <= 133)||(LA65_0 >= 135 && LA65_0 <= 137)||LA65_0==139||(LA65_0 >= 144 && LA65_0 <= 145)) ) {
				alt65=1;
			}
			else if ( (LA65_0==LPAREN) ) {
				int LA65_20 = input.LA(2);
				if ( (synpred97_JPA2()) ) {
					alt65=1;
				}
				else if ( (true) ) {
					alt65=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 65, 0, input);
				throw nvae;
			}

			switch (alt65) {
				case 1 :
					// JPA2.g:268:7: simple_cond_expression
					{
					pushFollow(FOLLOW_simple_cond_expression_in_conditional_primary2413);
					simple_cond_expression217=simple_cond_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_simple_cond_expression.add(simple_cond_expression217.getTree());
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
					// 269:5: -> ^( T_SIMPLE_CONDITION[] simple_cond_expression )
					{
						// JPA2.g:269:8: ^( T_SIMPLE_CONDITION[] simple_cond_expression )
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
					// JPA2.g:270:7: '(' conditional_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal218=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_primary2437); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal218_tree = (Object)adaptor.create(char_literal218);
					adaptor.addChild(root_0, char_literal218_tree);
					}

					pushFollow(FOLLOW_conditional_expression_in_conditional_primary2438);
					conditional_expression219=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression219.getTree());

					char_literal220=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_primary2439); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal220_tree = (Object)adaptor.create(char_literal220);
					adaptor.addChild(root_0, char_literal220_tree);
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
	// JPA2.g:271:1: simple_cond_expression : ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
	public final JPA2Parser.simple_cond_expression_return simple_cond_expression() throws RecognitionException {
		JPA2Parser.simple_cond_expression_return retval = new JPA2Parser.simple_cond_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope comparison_expression221 =null;
		ParserRuleReturnScope between_expression222 =null;
		ParserRuleReturnScope in_expression223 =null;
		ParserRuleReturnScope like_expression224 =null;
		ParserRuleReturnScope null_comparison_expression225 =null;
		ParserRuleReturnScope empty_collection_comparison_expression226 =null;
		ParserRuleReturnScope collection_member_expression227 =null;
		ParserRuleReturnScope exists_expression228 =null;
		ParserRuleReturnScope date_macro_expression229 =null;


		try {
			// JPA2.g:272:5: ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
			int alt66=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA66_1 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred100_JPA2()) ) {
					alt66=3;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}
				else if ( (synpred102_JPA2()) ) {
					alt66=5;
				}
				else if ( (synpred103_JPA2()) ) {
					alt66=6;
				}
				else if ( (synpred104_JPA2()) ) {
					alt66=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case STRING_LITERAL:
				{
				int LA66_2 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 77:
				{
				int LA66_3 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}
				else if ( (synpred102_JPA2()) ) {
					alt66=5;
				}
				else if ( (synpred104_JPA2()) ) {
					alt66=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA66_4 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}
				else if ( (synpred102_JPA2()) ) {
					alt66=5;
				}
				else if ( (synpred104_JPA2()) ) {
					alt66=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 63:
				{
				int LA66_5 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}
				else if ( (synpred102_JPA2()) ) {
					alt66=5;
				}
				else if ( (synpred104_JPA2()) ) {
					alt66=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 91:
				{
				int LA66_6 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 133:
				{
				int LA66_7 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 7, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 136:
				{
				int LA66_8 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LOWER:
				{
				int LA66_9 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 9, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 139:
				{
				int LA66_10 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case COUNT:
				{
				int LA66_11 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 11, input);
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
				int LA66_12 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 12, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 104:
				{
				int LA66_13 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case CASE:
				{
				int LA66_14 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 14, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 90:
				{
				int LA66_15 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 15, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 120:
				{
				int LA66_16 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 16, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 89:
				{
				int LA66_17 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 102:
				{
				int LA66_18 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 82:
				{
				int LA66_19 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 19, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LPAREN:
				{
				int LA66_20 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}
				else if ( (synpred104_JPA2()) ) {
					alt66=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 20, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 144:
			case 145:
				{
				alt66=1;
				}
				break;
			case GROUP:
				{
				int LA66_22 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred100_JPA2()) ) {
					alt66=3;
				}
				else if ( (synpred101_JPA2()) ) {
					alt66=4;
				}
				else if ( (synpred102_JPA2()) ) {
					alt66=5;
				}
				else if ( (synpred103_JPA2()) ) {
					alt66=6;
				}
				else if ( (synpred104_JPA2()) ) {
					alt66=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 22, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 92:
			case 93:
			case 94:
				{
				int LA66_23 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 23, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 137:
				{
				int LA66_24 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred100_JPA2()) ) {
					alt66=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 24, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 65:
			case 67:
				{
				int LA66_25 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 25, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA66_26 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 26, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 70:
				{
				int LA66_27 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 27, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 110:
				{
				int LA66_28 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 28, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 112:
				{
				int LA66_29 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 29, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 84:
				{
				int LA66_30 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 30, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 132:
				{
				int LA66_31 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 31, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 115:
				{
				int LA66_32 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 32, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 130:
				{
				int LA66_33 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 33, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 106:
				{
				int LA66_34 = input.LA(2);
				if ( (synpred98_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred99_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 34, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 135:
				{
				alt66=5;
				}
				break;
			case NOT:
			case 101:
				{
				alt66=8;
				}
				break;
			case 78:
			case 79:
			case 80:
			case 81:
			case 83:
				{
				alt66=9;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 66, 0, input);
				throw nvae;
			}
			switch (alt66) {
				case 1 :
					// JPA2.g:272:7: comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression2450);
					comparison_expression221=comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression221.getTree());

					}
					break;
				case 2 :
					// JPA2.g:273:7: between_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_between_expression_in_simple_cond_expression2458);
					between_expression222=between_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression222.getTree());

					}
					break;
				case 3 :
					// JPA2.g:274:7: in_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_in_expression_in_simple_cond_expression2466);
					in_expression223=in_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression223.getTree());

					}
					break;
				case 4 :
					// JPA2.g:275:7: like_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_like_expression_in_simple_cond_expression2474);
					like_expression224=like_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression224.getTree());

					}
					break;
				case 5 :
					// JPA2.g:276:7: null_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression2482);
					null_comparison_expression225=null_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression225.getTree());

					}
					break;
				case 6 :
					// JPA2.g:277:7: empty_collection_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2490);
					empty_collection_comparison_expression226=empty_collection_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression226.getTree());

					}
					break;
				case 7 :
					// JPA2.g:278:7: collection_member_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression2498);
					collection_member_expression227=collection_member_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression227.getTree());

					}
					break;
				case 8 :
					// JPA2.g:279:7: exists_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exists_expression_in_simple_cond_expression2506);
					exists_expression228=exists_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression228.getTree());

					}
					break;
				case 9 :
					// JPA2.g:280:7: date_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression2514);
					date_macro_expression229=date_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_macro_expression229.getTree());

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
	// JPA2.g:283:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
	public final JPA2Parser.date_macro_expression_return date_macro_expression() throws RecognitionException {
		JPA2Parser.date_macro_expression_return retval = new JPA2Parser.date_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope date_between_macro_expression230 =null;
		ParserRuleReturnScope date_before_macro_expression231 =null;
		ParserRuleReturnScope date_after_macro_expression232 =null;
		ParserRuleReturnScope date_equals_macro_expression233 =null;
		ParserRuleReturnScope date_today_macro_expression234 =null;


		try {
			// JPA2.g:284:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
			int alt67=5;
			switch ( input.LA(1) ) {
			case 78:
				{
				alt67=1;
				}
				break;
			case 80:
				{
				alt67=2;
				}
				break;
			case 79:
				{
				alt67=3;
				}
				break;
			case 81:
				{
				alt67=4;
				}
				break;
			case 83:
				{
				alt67=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 67, 0, input);
				throw nvae;
			}
			switch (alt67) {
				case 1 :
					// JPA2.g:284:7: date_between_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2527);
					date_between_macro_expression230=date_between_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression230.getTree());

					}
					break;
				case 2 :
					// JPA2.g:285:7: date_before_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2535);
					date_before_macro_expression231=date_before_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression231.getTree());

					}
					break;
				case 3 :
					// JPA2.g:286:7: date_after_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2543);
					date_after_macro_expression232=date_after_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression232.getTree());

					}
					break;
				case 4 :
					// JPA2.g:287:7: date_equals_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2551);
					date_equals_macro_expression233=date_equals_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression233.getTree());

					}
					break;
				case 5 :
					// JPA2.g:288:7: date_today_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2559);
					date_today_macro_expression234=date_today_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_today_macro_expression234.getTree());

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
	// JPA2.g:290:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
	public final JPA2Parser.date_between_macro_expression_return date_between_macro_expression() throws RecognitionException {
		JPA2Parser.date_between_macro_expression_return retval = new JPA2Parser.date_between_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal235=null;
		Token char_literal236=null;
		Token char_literal238=null;
		Token string_literal239=null;
		Token set240=null;
		Token char_literal242=null;
		Token string_literal243=null;
		Token set244=null;
		Token char_literal246=null;
		Token set247=null;
		Token char_literal248=null;
		ParserRuleReturnScope path_expression237 =null;
		ParserRuleReturnScope numeric_literal241 =null;
		ParserRuleReturnScope numeric_literal245 =null;

		Object string_literal235_tree=null;
		Object char_literal236_tree=null;
		Object char_literal238_tree=null;
		Object string_literal239_tree=null;
		Object set240_tree=null;
		Object char_literal242_tree=null;
		Object string_literal243_tree=null;
		Object set244_tree=null;
		Object char_literal246_tree=null;
		Object set247_tree=null;
		Object char_literal248_tree=null;

		try {
			// JPA2.g:291:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
			// JPA2.g:291:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal235=(Token)match(input,78,FOLLOW_78_in_date_between_macro_expression2571); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal235_tree = (Object)adaptor.create(string_literal235);
			adaptor.addChild(root_0, string_literal235_tree);
			}

			char_literal236=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2573); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal236_tree = (Object)adaptor.create(char_literal236);
			adaptor.addChild(root_0, char_literal236_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2575);
			path_expression237=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression237.getTree());

			char_literal238=(Token)match(input,66,FOLLOW_66_in_date_between_macro_expression2577); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal238_tree = (Object)adaptor.create(char_literal238);
			adaptor.addChild(root_0, char_literal238_tree);
			}

			string_literal239=(Token)match(input,118,FOLLOW_118_in_date_between_macro_expression2579); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal239_tree = (Object)adaptor.create(string_literal239);
			adaptor.addChild(root_0, string_literal239_tree);
			}

			// JPA2.g:291:48: ( ( '+' | '-' ) numeric_literal )?
			int alt68=2;
			int LA68_0 = input.LA(1);
			if ( (LA68_0==65||LA68_0==67) ) {
				alt68=1;
			}
			switch (alt68) {
				case 1 :
					// JPA2.g:291:49: ( '+' | '-' ) numeric_literal
					{
					set240=input.LT(1);
					if ( input.LA(1)==65||input.LA(1)==67 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set240));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2590);
					numeric_literal241=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal241.getTree());

					}
					break;

			}

			char_literal242=(Token)match(input,66,FOLLOW_66_in_date_between_macro_expression2594); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal242_tree = (Object)adaptor.create(char_literal242);
			adaptor.addChild(root_0, char_literal242_tree);
			}

			string_literal243=(Token)match(input,118,FOLLOW_118_in_date_between_macro_expression2596); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal243_tree = (Object)adaptor.create(string_literal243);
			adaptor.addChild(root_0, string_literal243_tree);
			}

			// JPA2.g:291:89: ( ( '+' | '-' ) numeric_literal )?
			int alt69=2;
			int LA69_0 = input.LA(1);
			if ( (LA69_0==65||LA69_0==67) ) {
				alt69=1;
			}
			switch (alt69) {
				case 1 :
					// JPA2.g:291:90: ( '+' | '-' ) numeric_literal
					{
					set244=input.LT(1);
					if ( input.LA(1)==65||input.LA(1)==67 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set244));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2607);
					numeric_literal245=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal245.getTree());

					}
					break;

			}

			char_literal246=(Token)match(input,66,FOLLOW_66_in_date_between_macro_expression2611); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal246_tree = (Object)adaptor.create(char_literal246);
			adaptor.addChild(root_0, char_literal246_tree);
			}

			set247=input.LT(1);
			if ( input.LA(1)==95||input.LA(1)==105||input.LA(1)==114||input.LA(1)==116||input.LA(1)==128||input.LA(1)==143 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set247));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			char_literal248=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2636); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal248_tree = (Object)adaptor.create(char_literal248);
			adaptor.addChild(root_0, char_literal248_tree);
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
	// JPA2.g:293:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_before_macro_expression_return date_before_macro_expression() throws RecognitionException {
		JPA2Parser.date_before_macro_expression_return retval = new JPA2Parser.date_before_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal249=null;
		Token char_literal250=null;
		Token char_literal252=null;
		Token char_literal255=null;
		ParserRuleReturnScope path_expression251 =null;
		ParserRuleReturnScope path_expression253 =null;
		ParserRuleReturnScope input_parameter254 =null;

		Object string_literal249_tree=null;
		Object char_literal250_tree=null;
		Object char_literal252_tree=null;
		Object char_literal255_tree=null;

		try {
			// JPA2.g:294:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:294:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal249=(Token)match(input,80,FOLLOW_80_in_date_before_macro_expression2648); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal249_tree = (Object)adaptor.create(string_literal249);
			adaptor.addChild(root_0, string_literal249_tree);
			}

			char_literal250=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2650); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal250_tree = (Object)adaptor.create(char_literal250);
			adaptor.addChild(root_0, char_literal250_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2652);
			path_expression251=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression251.getTree());

			char_literal252=(Token)match(input,66,FOLLOW_66_in_date_before_macro_expression2654); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal252_tree = (Object)adaptor.create(char_literal252);
			adaptor.addChild(root_0, char_literal252_tree);
			}

			// JPA2.g:294:45: ( path_expression | input_parameter )
			int alt70=2;
			int LA70_0 = input.LA(1);
			if ( (LA70_0==GROUP||LA70_0==WORD) ) {
				alt70=1;
			}
			else if ( (LA70_0==NAMED_PARAMETER||LA70_0==63||LA70_0==77) ) {
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
					// JPA2.g:294:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2657);
					path_expression253=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression253.getTree());

					}
					break;
				case 2 :
					// JPA2.g:294:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2661);
					input_parameter254=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter254.getTree());

					}
					break;

			}

			char_literal255=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2664); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal255_tree = (Object)adaptor.create(char_literal255);
			adaptor.addChild(root_0, char_literal255_tree);
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
	// JPA2.g:296:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_after_macro_expression_return date_after_macro_expression() throws RecognitionException {
		JPA2Parser.date_after_macro_expression_return retval = new JPA2Parser.date_after_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal256=null;
		Token char_literal257=null;
		Token char_literal259=null;
		Token char_literal262=null;
		ParserRuleReturnScope path_expression258 =null;
		ParserRuleReturnScope path_expression260 =null;
		ParserRuleReturnScope input_parameter261 =null;

		Object string_literal256_tree=null;
		Object char_literal257_tree=null;
		Object char_literal259_tree=null;
		Object char_literal262_tree=null;

		try {
			// JPA2.g:297:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:297:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal256=(Token)match(input,79,FOLLOW_79_in_date_after_macro_expression2676); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal256_tree = (Object)adaptor.create(string_literal256);
			adaptor.addChild(root_0, string_literal256_tree);
			}

			char_literal257=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2678); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal257_tree = (Object)adaptor.create(char_literal257);
			adaptor.addChild(root_0, char_literal257_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2680);
			path_expression258=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression258.getTree());

			char_literal259=(Token)match(input,66,FOLLOW_66_in_date_after_macro_expression2682); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal259_tree = (Object)adaptor.create(char_literal259);
			adaptor.addChild(root_0, char_literal259_tree);
			}

			// JPA2.g:297:44: ( path_expression | input_parameter )
			int alt71=2;
			int LA71_0 = input.LA(1);
			if ( (LA71_0==GROUP||LA71_0==WORD) ) {
				alt71=1;
			}
			else if ( (LA71_0==NAMED_PARAMETER||LA71_0==63||LA71_0==77) ) {
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
					// JPA2.g:297:45: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2685);
					path_expression260=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression260.getTree());

					}
					break;
				case 2 :
					// JPA2.g:297:63: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2689);
					input_parameter261=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter261.getTree());

					}
					break;

			}

			char_literal262=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2692); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal262_tree = (Object)adaptor.create(char_literal262);
			adaptor.addChild(root_0, char_literal262_tree);
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
	// JPA2.g:299:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_equals_macro_expression_return date_equals_macro_expression() throws RecognitionException {
		JPA2Parser.date_equals_macro_expression_return retval = new JPA2Parser.date_equals_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal263=null;
		Token char_literal264=null;
		Token char_literal266=null;
		Token char_literal269=null;
		ParserRuleReturnScope path_expression265 =null;
		ParserRuleReturnScope path_expression267 =null;
		ParserRuleReturnScope input_parameter268 =null;

		Object string_literal263_tree=null;
		Object char_literal264_tree=null;
		Object char_literal266_tree=null;
		Object char_literal269_tree=null;

		try {
			// JPA2.g:300:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:300:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal263=(Token)match(input,81,FOLLOW_81_in_date_equals_macro_expression2704); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal263_tree = (Object)adaptor.create(string_literal263);
			adaptor.addChild(root_0, string_literal263_tree);
			}

			char_literal264=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2706); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal264_tree = (Object)adaptor.create(char_literal264);
			adaptor.addChild(root_0, char_literal264_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2708);
			path_expression265=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression265.getTree());

			char_literal266=(Token)match(input,66,FOLLOW_66_in_date_equals_macro_expression2710); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal266_tree = (Object)adaptor.create(char_literal266);
			adaptor.addChild(root_0, char_literal266_tree);
			}

			// JPA2.g:300:45: ( path_expression | input_parameter )
			int alt72=2;
			int LA72_0 = input.LA(1);
			if ( (LA72_0==GROUP||LA72_0==WORD) ) {
				alt72=1;
			}
			else if ( (LA72_0==NAMED_PARAMETER||LA72_0==63||LA72_0==77) ) {
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
					// JPA2.g:300:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2713);
					path_expression267=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression267.getTree());

					}
					break;
				case 2 :
					// JPA2.g:300:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2717);
					input_parameter268=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter268.getTree());

					}
					break;

			}

			char_literal269=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2720); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal269_tree = (Object)adaptor.create(char_literal269);
			adaptor.addChild(root_0, char_literal269_tree);
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
	// JPA2.g:302:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
	public final JPA2Parser.date_today_macro_expression_return date_today_macro_expression() throws RecognitionException {
		JPA2Parser.date_today_macro_expression_return retval = new JPA2Parser.date_today_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal270=null;
		Token char_literal271=null;
		Token char_literal273=null;
		ParserRuleReturnScope path_expression272 =null;

		Object string_literal270_tree=null;
		Object char_literal271_tree=null;
		Object char_literal273_tree=null;

		try {
			// JPA2.g:303:5: ( '@TODAY' '(' path_expression ')' )
			// JPA2.g:303:7: '@TODAY' '(' path_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal270=(Token)match(input,83,FOLLOW_83_in_date_today_macro_expression2732); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal270_tree = (Object)adaptor.create(string_literal270);
			adaptor.addChild(root_0, string_literal270_tree);
			}

			char_literal271=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2734); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal271_tree = (Object)adaptor.create(char_literal271);
			adaptor.addChild(root_0, char_literal271_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2736);
			path_expression272=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression272.getTree());

			char_literal273=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2738); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal273_tree = (Object)adaptor.create(char_literal273);
			adaptor.addChild(root_0, char_literal273_tree);
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
	// JPA2.g:306:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
	public final JPA2Parser.between_expression_return between_expression() throws RecognitionException {
		JPA2Parser.between_expression_return retval = new JPA2Parser.between_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal275=null;
		Token string_literal276=null;
		Token string_literal278=null;
		Token string_literal281=null;
		Token string_literal282=null;
		Token string_literal284=null;
		Token string_literal287=null;
		Token string_literal288=null;
		Token string_literal290=null;
		ParserRuleReturnScope arithmetic_expression274 =null;
		ParserRuleReturnScope arithmetic_expression277 =null;
		ParserRuleReturnScope arithmetic_expression279 =null;
		ParserRuleReturnScope string_expression280 =null;
		ParserRuleReturnScope string_expression283 =null;
		ParserRuleReturnScope string_expression285 =null;
		ParserRuleReturnScope datetime_expression286 =null;
		ParserRuleReturnScope datetime_expression289 =null;
		ParserRuleReturnScope datetime_expression291 =null;

		Object string_literal275_tree=null;
		Object string_literal276_tree=null;
		Object string_literal278_tree=null;
		Object string_literal281_tree=null;
		Object string_literal282_tree=null;
		Object string_literal284_tree=null;
		Object string_literal287_tree=null;
		Object string_literal288_tree=null;
		Object string_literal290_tree=null;

		try {
			// JPA2.g:307:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
			int alt76=3;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 65:
			case 67:
			case 70:
			case 84:
			case 106:
			case 110:
			case 112:
			case 115:
			case 130:
			case 132:
				{
				alt76=1;
				}
				break;
			case WORD:
				{
				int LA76_2 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case LPAREN:
				{
				int LA76_5 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 77:
				{
				int LA76_6 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA76_7 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 63:
				{
				int LA76_8 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case COUNT:
				{
				int LA76_16 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA76_17 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 104:
				{
				int LA76_18 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case CASE:
				{
				int LA76_19 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 90:
				{
				int LA76_20 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 120:
				{
				int LA76_21 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 89:
				{
				int LA76_22 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 102:
				{
				int LA76_23 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 82:
				{
				int LA76_24 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 91:
			case 133:
			case 136:
			case 139:
				{
				alt76=2;
				}
				break;
			case 92:
			case 93:
			case 94:
				{
				alt76=3;
				}
				break;
			case GROUP:
				{
				int LA76_32 = input.LA(2);
				if ( (synpred123_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred125_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

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
					// JPA2.g:307:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2751);
					arithmetic_expression274=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression274.getTree());

					// JPA2.g:307:29: ( 'NOT' )?
					int alt73=2;
					int LA73_0 = input.LA(1);
					if ( (LA73_0==NOT) ) {
						alt73=1;
					}
					switch (alt73) {
						case 1 :
							// JPA2.g:307:30: 'NOT'
							{
							string_literal275=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2754); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal275_tree = (Object)adaptor.create(string_literal275);
							adaptor.addChild(root_0, string_literal275_tree);
							}

							}
							break;

					}

					string_literal276=(Token)match(input,87,FOLLOW_87_in_between_expression2758); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal276_tree = (Object)adaptor.create(string_literal276);
					adaptor.addChild(root_0, string_literal276_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2760);
					arithmetic_expression277=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression277.getTree());

					string_literal278=(Token)match(input,AND,FOLLOW_AND_in_between_expression2762); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal278_tree = (Object)adaptor.create(string_literal278);
					adaptor.addChild(root_0, string_literal278_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2764);
					arithmetic_expression279=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression279.getTree());

					}
					break;
				case 2 :
					// JPA2.g:308:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_between_expression2772);
					string_expression280=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression280.getTree());

					// JPA2.g:308:25: ( 'NOT' )?
					int alt74=2;
					int LA74_0 = input.LA(1);
					if ( (LA74_0==NOT) ) {
						alt74=1;
					}
					switch (alt74) {
						case 1 :
							// JPA2.g:308:26: 'NOT'
							{
							string_literal281=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2775); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal281_tree = (Object)adaptor.create(string_literal281);
							adaptor.addChild(root_0, string_literal281_tree);
							}

							}
							break;

					}

					string_literal282=(Token)match(input,87,FOLLOW_87_in_between_expression2779); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal282_tree = (Object)adaptor.create(string_literal282);
					adaptor.addChild(root_0, string_literal282_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2781);
					string_expression283=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression283.getTree());

					string_literal284=(Token)match(input,AND,FOLLOW_AND_in_between_expression2783); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal284_tree = (Object)adaptor.create(string_literal284);
					adaptor.addChild(root_0, string_literal284_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2785);
					string_expression285=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression285.getTree());

					}
					break;
				case 3 :
					// JPA2.g:309:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_between_expression2793);
					datetime_expression286=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression286.getTree());

					// JPA2.g:309:27: ( 'NOT' )?
					int alt75=2;
					int LA75_0 = input.LA(1);
					if ( (LA75_0==NOT) ) {
						alt75=1;
					}
					switch (alt75) {
						case 1 :
							// JPA2.g:309:28: 'NOT'
							{
							string_literal287=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2796); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal287_tree = (Object)adaptor.create(string_literal287);
							adaptor.addChild(root_0, string_literal287_tree);
							}

							}
							break;

					}

					string_literal288=(Token)match(input,87,FOLLOW_87_in_between_expression2800); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal288_tree = (Object)adaptor.create(string_literal288);
					adaptor.addChild(root_0, string_literal288_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2802);
					datetime_expression289=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression289.getTree());

					string_literal290=(Token)match(input,AND,FOLLOW_AND_in_between_expression2804); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal290_tree = (Object)adaptor.create(string_literal290);
					adaptor.addChild(root_0, string_literal290_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2806);
					datetime_expression291=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression291.getTree());

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
	// JPA2.g:310:1: in_expression : ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) ;
	public final JPA2Parser.in_expression_return in_expression() throws RecognitionException {
		JPA2Parser.in_expression_return retval = new JPA2Parser.in_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NOT295=null;
		Token IN296=null;
		Token char_literal297=null;
		Token char_literal299=null;
		Token char_literal301=null;
		Token char_literal304=null;
		Token char_literal306=null;
		ParserRuleReturnScope path_expression292 =null;
		ParserRuleReturnScope type_discriminator293 =null;
		ParserRuleReturnScope identification_variable294 =null;
		ParserRuleReturnScope in_item298 =null;
		ParserRuleReturnScope in_item300 =null;
		ParserRuleReturnScope subquery302 =null;
		ParserRuleReturnScope collection_valued_input_parameter303 =null;
		ParserRuleReturnScope path_expression305 =null;

		Object NOT295_tree=null;
		Object IN296_tree=null;
		Object char_literal297_tree=null;
		Object char_literal299_tree=null;
		Object char_literal301_tree=null;
		Object char_literal304_tree=null;
		Object char_literal306_tree=null;

		try {
			// JPA2.g:311:5: ( ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) )
			// JPA2.g:311:7: ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:311:7: ( path_expression | type_discriminator | identification_variable )
			int alt77=3;
			int LA77_0 = input.LA(1);
			if ( (LA77_0==GROUP||LA77_0==WORD) ) {
				int LA77_1 = input.LA(2);
				if ( (LA77_1==68) ) {
					alt77=1;
				}
				else if ( (LA77_1==IN||LA77_1==NOT) ) {
					alt77=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 77, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA77_0==137) ) {
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
					// JPA2.g:311:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_in_expression2818);
					path_expression292=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression292.getTree());

					}
					break;
				case 2 :
					// JPA2.g:311:26: type_discriminator
					{
					pushFollow(FOLLOW_type_discriminator_in_in_expression2822);
					type_discriminator293=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator293.getTree());

					}
					break;
				case 3 :
					// JPA2.g:311:47: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_in_expression2826);
					identification_variable294=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable294.getTree());

					}
					break;

			}

			// JPA2.g:311:72: ( NOT )?
			int alt78=2;
			int LA78_0 = input.LA(1);
			if ( (LA78_0==NOT) ) {
				alt78=1;
			}
			switch (alt78) {
				case 1 :
					// JPA2.g:311:73: NOT
					{
					NOT295=(Token)match(input,NOT,FOLLOW_NOT_in_in_expression2830); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT295_tree = (Object)adaptor.create(NOT295);
					adaptor.addChild(root_0, NOT295_tree);
					}

					}
					break;

			}

			IN296=(Token)match(input,IN,FOLLOW_IN_in_in_expression2834); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN296_tree = (Object)adaptor.create(IN296);
			adaptor.addChild(root_0, IN296_tree);
			}

			// JPA2.g:312:13: ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			int alt80=4;
			int LA80_0 = input.LA(1);
			if ( (LA80_0==LPAREN) ) {
				switch ( input.LA(2) ) {
				case 129:
					{
					alt80=2;
					}
					break;
				case INT_NUMERAL:
				case NAMED_PARAMETER:
				case STRING_LITERAL:
				case 63:
				case 70:
				case 77:
				case 82:
					{
					alt80=1;
					}
					break;
				case GROUP:
				case WORD:
					{
					alt80=4;
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 80, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else if ( (LA80_0==NAMED_PARAMETER||LA80_0==63||LA80_0==77) ) {
				alt80=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 80, 0, input);
				throw nvae;
			}

			switch (alt80) {
				case 1 :
					// JPA2.g:312:15: '(' in_item ( ',' in_item )* ')'
					{
					char_literal297=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2850); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal297_tree = (Object)adaptor.create(char_literal297);
					adaptor.addChild(root_0, char_literal297_tree);
					}

					pushFollow(FOLLOW_in_item_in_in_expression2852);
					in_item298=in_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item298.getTree());

					// JPA2.g:312:27: ( ',' in_item )*
					loop79:
					while (true) {
						int alt79=2;
						int LA79_0 = input.LA(1);
						if ( (LA79_0==66) ) {
							alt79=1;
						}

						switch (alt79) {
						case 1 :
							// JPA2.g:312:28: ',' in_item
							{
							char_literal299=(Token)match(input,66,FOLLOW_66_in_in_expression2855); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal299_tree = (Object)adaptor.create(char_literal299);
							adaptor.addChild(root_0, char_literal299_tree);
							}

							pushFollow(FOLLOW_in_item_in_in_expression2857);
							in_item300=in_item();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item300.getTree());

							}
							break;

						default :
							break loop79;
						}
					}

					char_literal301=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2861); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal301_tree = (Object)adaptor.create(char_literal301);
					adaptor.addChild(root_0, char_literal301_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:313:15: subquery
					{
					pushFollow(FOLLOW_subquery_in_in_expression2877);
					subquery302=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery302.getTree());

					}
					break;
				case 3 :
					// JPA2.g:314:15: collection_valued_input_parameter
					{
					pushFollow(FOLLOW_collection_valued_input_parameter_in_in_expression2893);
					collection_valued_input_parameter303=collection_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_input_parameter303.getTree());

					}
					break;
				case 4 :
					// JPA2.g:315:15: '(' path_expression ')'
					{
					char_literal304=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2909); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal304_tree = (Object)adaptor.create(char_literal304);
					adaptor.addChild(root_0, char_literal304_tree);
					}

					pushFollow(FOLLOW_path_expression_in_in_expression2911);
					path_expression305=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression305.getTree());

					char_literal306=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2913); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal306_tree = (Object)adaptor.create(char_literal306);
					adaptor.addChild(root_0, char_literal306_tree);
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
	// JPA2.g:321:1: in_item : ( string_literal | numeric_literal | single_valued_input_parameter | enum_function );
	public final JPA2Parser.in_item_return in_item() throws RecognitionException {
		JPA2Parser.in_item_return retval = new JPA2Parser.in_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal307 =null;
		ParserRuleReturnScope numeric_literal308 =null;
		ParserRuleReturnScope single_valued_input_parameter309 =null;
		ParserRuleReturnScope enum_function310 =null;


		try {
			// JPA2.g:322:5: ( string_literal | numeric_literal | single_valued_input_parameter | enum_function )
			int alt81=4;
			switch ( input.LA(1) ) {
			case STRING_LITERAL:
				{
				alt81=1;
				}
				break;
			case INT_NUMERAL:
			case 70:
				{
				alt81=2;
				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt81=3;
				}
				break;
			case 82:
				{
				alt81=4;
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
					// JPA2.g:322:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_in_item2941);
					string_literal307=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal307.getTree());

					}
					break;
				case 2 :
					// JPA2.g:322:24: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_in_item2945);
					numeric_literal308=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal308.getTree());

					}
					break;
				case 3 :
					// JPA2.g:322:42: single_valued_input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_single_valued_input_parameter_in_in_item2949);
					single_valued_input_parameter309=single_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_input_parameter309.getTree());

					}
					break;
				case 4 :
					// JPA2.g:322:74: enum_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_function_in_in_item2953);
					enum_function310=enum_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_function310.getTree());

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
	// JPA2.g:323:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? ;
	public final JPA2Parser.like_expression_return like_expression() throws RecognitionException {
		JPA2Parser.like_expression_return retval = new JPA2Parser.like_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal312=null;
		Token string_literal313=null;
		Token string_literal317=null;
		ParserRuleReturnScope string_expression311 =null;
		ParserRuleReturnScope string_expression314 =null;
		ParserRuleReturnScope pattern_value315 =null;
		ParserRuleReturnScope input_parameter316 =null;
		ParserRuleReturnScope escape_character318 =null;

		Object string_literal312_tree=null;
		Object string_literal313_tree=null;
		Object string_literal317_tree=null;

		try {
			// JPA2.g:324:5: ( string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? )
			// JPA2.g:324:7: string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_expression_in_like_expression2964);
			string_expression311=string_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression311.getTree());

			// JPA2.g:324:25: ( 'NOT' )?
			int alt82=2;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==NOT) ) {
				alt82=1;
			}
			switch (alt82) {
				case 1 :
					// JPA2.g:324:26: 'NOT'
					{
					string_literal312=(Token)match(input,NOT,FOLLOW_NOT_in_like_expression2967); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal312_tree = (Object)adaptor.create(string_literal312);
					adaptor.addChild(root_0, string_literal312_tree);
					}

					}
					break;

			}

			string_literal313=(Token)match(input,111,FOLLOW_111_in_like_expression2971); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal313_tree = (Object)adaptor.create(string_literal313);
			adaptor.addChild(root_0, string_literal313_tree);
			}

			// JPA2.g:324:41: ( string_expression | pattern_value | input_parameter )
			int alt83=3;
			switch ( input.LA(1) ) {
			case AVG:
			case CASE:
			case COUNT:
			case GROUP:
			case LOWER:
			case LPAREN:
			case MAX:
			case MIN:
			case SUM:
			case WORD:
			case 82:
			case 89:
			case 90:
			case 91:
			case 102:
			case 104:
			case 120:
			case 133:
			case 136:
			case 139:
				{
				alt83=1;
				}
				break;
			case STRING_LITERAL:
				{
				int LA83_2 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt83=1;
				}
				else if ( (synpred139_JPA2()) ) {
					alt83=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 83, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 77:
				{
				int LA83_3 = input.LA(2);
				if ( (LA83_3==70) ) {
					int LA83_7 = input.LA(3);
					if ( (LA83_7==INT_NUMERAL) ) {
						int LA83_11 = input.LA(4);
						if ( (synpred138_JPA2()) ) {
							alt83=1;
						}
						else if ( (true) ) {
							alt83=3;
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
								new NoViableAltException("", 83, 7, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA83_3==INT_NUMERAL) ) {
					int LA83_8 = input.LA(3);
					if ( (synpred138_JPA2()) ) {
						alt83=1;
					}
					else if ( (true) ) {
						alt83=3;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 83, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA83_4 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt83=1;
				}
				else if ( (true) ) {
					alt83=3;
				}

				}
				break;
			case 63:
				{
				int LA83_5 = input.LA(2);
				if ( (LA83_5==WORD) ) {
					int LA83_10 = input.LA(3);
					if ( (LA83_10==146) ) {
						int LA83_12 = input.LA(4);
						if ( (synpred138_JPA2()) ) {
							alt83=1;
						}
						else if ( (true) ) {
							alt83=3;
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
								new NoViableAltException("", 83, 10, input);
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
							new NoViableAltException("", 83, 5, input);
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
					new NoViableAltException("", 83, 0, input);
				throw nvae;
			}
			switch (alt83) {
				case 1 :
					// JPA2.g:324:42: string_expression
					{
					pushFollow(FOLLOW_string_expression_in_like_expression2974);
					string_expression314=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression314.getTree());

					}
					break;
				case 2 :
					// JPA2.g:324:62: pattern_value
					{
					pushFollow(FOLLOW_pattern_value_in_like_expression2978);
					pattern_value315=pattern_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value315.getTree());

					}
					break;
				case 3 :
					// JPA2.g:324:78: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_like_expression2982);
					input_parameter316=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter316.getTree());

					}
					break;

			}

			// JPA2.g:324:94: ( 'ESCAPE' escape_character )?
			int alt84=2;
			int LA84_0 = input.LA(1);
			if ( (LA84_0==100) ) {
				alt84=1;
			}
			switch (alt84) {
				case 1 :
					// JPA2.g:324:95: 'ESCAPE' escape_character
					{
					string_literal317=(Token)match(input,100,FOLLOW_100_in_like_expression2985); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal317_tree = (Object)adaptor.create(string_literal317);
					adaptor.addChild(root_0, string_literal317_tree);
					}

					pushFollow(FOLLOW_escape_character_in_like_expression2987);
					escape_character318=escape_character();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, escape_character318.getTree());

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
	// JPA2.g:325:1: null_comparison_expression : ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' ;
	public final JPA2Parser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
		JPA2Parser.null_comparison_expression_return retval = new JPA2Parser.null_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal322=null;
		Token string_literal323=null;
		Token string_literal324=null;
		ParserRuleReturnScope path_expression319 =null;
		ParserRuleReturnScope input_parameter320 =null;
		ParserRuleReturnScope join_association_path_expression321 =null;

		Object string_literal322_tree=null;
		Object string_literal323_tree=null;
		Object string_literal324_tree=null;

		try {
			// JPA2.g:326:5: ( ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' )
			// JPA2.g:326:7: ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:326:7: ( path_expression | input_parameter | join_association_path_expression )
			int alt85=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA85_1 = input.LA(2);
				if ( (LA85_1==68) ) {
					int LA85_5 = input.LA(3);
					if ( (synpred141_JPA2()) ) {
						alt85=1;
					}
					else if ( (true) ) {
						alt85=3;
					}

				}
				else if ( (LA85_1==107) ) {
					alt85=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 85, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt85=2;
				}
				break;
			case 135:
				{
				alt85=3;
				}
				break;
			case GROUP:
				{
				int LA85_4 = input.LA(2);
				if ( (LA85_4==68) ) {
					int LA85_6 = input.LA(3);
					if ( (synpred141_JPA2()) ) {
						alt85=1;
					}
					else if ( (true) ) {
						alt85=3;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 85, 4, input);
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
					new NoViableAltException("", 85, 0, input);
				throw nvae;
			}
			switch (alt85) {
				case 1 :
					// JPA2.g:326:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_null_comparison_expression3001);
					path_expression319=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression319.getTree());

					}
					break;
				case 2 :
					// JPA2.g:326:26: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_null_comparison_expression3005);
					input_parameter320=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter320.getTree());

					}
					break;
				case 3 :
					// JPA2.g:326:44: join_association_path_expression
					{
					pushFollow(FOLLOW_join_association_path_expression_in_null_comparison_expression3009);
					join_association_path_expression321=join_association_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression321.getTree());

					}
					break;

			}

			string_literal322=(Token)match(input,107,FOLLOW_107_in_null_comparison_expression3012); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal322_tree = (Object)adaptor.create(string_literal322);
			adaptor.addChild(root_0, string_literal322_tree);
			}

			// JPA2.g:326:83: ( 'NOT' )?
			int alt86=2;
			int LA86_0 = input.LA(1);
			if ( (LA86_0==NOT) ) {
				alt86=1;
			}
			switch (alt86) {
				case 1 :
					// JPA2.g:326:84: 'NOT'
					{
					string_literal323=(Token)match(input,NOT,FOLLOW_NOT_in_null_comparison_expression3015); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal323_tree = (Object)adaptor.create(string_literal323);
					adaptor.addChild(root_0, string_literal323_tree);
					}

					}
					break;

			}

			string_literal324=(Token)match(input,119,FOLLOW_119_in_null_comparison_expression3019); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal324_tree = (Object)adaptor.create(string_literal324);
			adaptor.addChild(root_0, string_literal324_tree);
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
	// JPA2.g:327:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
	public final JPA2Parser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
		JPA2Parser.empty_collection_comparison_expression_return retval = new JPA2Parser.empty_collection_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal326=null;
		Token string_literal327=null;
		Token string_literal328=null;
		ParserRuleReturnScope path_expression325 =null;

		Object string_literal326_tree=null;
		Object string_literal327_tree=null;
		Object string_literal328_tree=null;

		try {
			// JPA2.g:328:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
			// JPA2.g:328:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression3030);
			path_expression325=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression325.getTree());

			string_literal326=(Token)match(input,107,FOLLOW_107_in_empty_collection_comparison_expression3032); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal326_tree = (Object)adaptor.create(string_literal326);
			adaptor.addChild(root_0, string_literal326_tree);
			}

			// JPA2.g:328:28: ( 'NOT' )?
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==NOT) ) {
				alt87=1;
			}
			switch (alt87) {
				case 1 :
					// JPA2.g:328:29: 'NOT'
					{
					string_literal327=(Token)match(input,NOT,FOLLOW_NOT_in_empty_collection_comparison_expression3035); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal327_tree = (Object)adaptor.create(string_literal327);
					adaptor.addChild(root_0, string_literal327_tree);
					}

					}
					break;

			}

			string_literal328=(Token)match(input,97,FOLLOW_97_in_empty_collection_comparison_expression3039); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal328_tree = (Object)adaptor.create(string_literal328);
			adaptor.addChild(root_0, string_literal328_tree);
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
	// JPA2.g:329:1: collection_member_expression : entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
	public final JPA2Parser.collection_member_expression_return collection_member_expression() throws RecognitionException {
		JPA2Parser.collection_member_expression_return retval = new JPA2Parser.collection_member_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal330=null;
		Token string_literal331=null;
		Token string_literal332=null;
		ParserRuleReturnScope entity_or_value_expression329 =null;
		ParserRuleReturnScope path_expression333 =null;

		Object string_literal330_tree=null;
		Object string_literal331_tree=null;
		Object string_literal332_tree=null;

		try {
			// JPA2.g:330:5: ( entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
			// JPA2.g:330:7: entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_or_value_expression_in_collection_member_expression3050);
			entity_or_value_expression329=entity_or_value_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_or_value_expression329.getTree());

			// JPA2.g:330:35: ( 'NOT' )?
			int alt88=2;
			int LA88_0 = input.LA(1);
			if ( (LA88_0==NOT) ) {
				alt88=1;
			}
			switch (alt88) {
				case 1 :
					// JPA2.g:330:36: 'NOT'
					{
					string_literal330=(Token)match(input,NOT,FOLLOW_NOT_in_collection_member_expression3054); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal330_tree = (Object)adaptor.create(string_literal330);
					adaptor.addChild(root_0, string_literal330_tree);
					}

					}
					break;

			}

			string_literal331=(Token)match(input,113,FOLLOW_113_in_collection_member_expression3058); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal331_tree = (Object)adaptor.create(string_literal331);
			adaptor.addChild(root_0, string_literal331_tree);
			}

			// JPA2.g:330:53: ( 'OF' )?
			int alt89=2;
			int LA89_0 = input.LA(1);
			if ( (LA89_0==124) ) {
				alt89=1;
			}
			switch (alt89) {
				case 1 :
					// JPA2.g:330:54: 'OF'
					{
					string_literal332=(Token)match(input,124,FOLLOW_124_in_collection_member_expression3061); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal332_tree = (Object)adaptor.create(string_literal332);
					adaptor.addChild(root_0, string_literal332_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_path_expression_in_collection_member_expression3065);
			path_expression333=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression333.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:331:1: entity_or_value_expression : ( path_expression | simple_entity_or_value_expression | subquery );
	public final JPA2Parser.entity_or_value_expression_return entity_or_value_expression() throws RecognitionException {
		JPA2Parser.entity_or_value_expression_return retval = new JPA2Parser.entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression334 =null;
		ParserRuleReturnScope simple_entity_or_value_expression335 =null;
		ParserRuleReturnScope subquery336 =null;


		try {
			// JPA2.g:332:5: ( path_expression | simple_entity_or_value_expression | subquery )
			int alt90=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA90_1 = input.LA(2);
				if ( (LA90_1==68) ) {
					alt90=1;
				}
				else if ( (LA90_1==NOT||LA90_1==113) ) {
					alt90=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 90, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt90=2;
				}
				break;
			case GROUP:
				{
				int LA90_3 = input.LA(2);
				if ( (LA90_3==68) ) {
					alt90=1;
				}
				else if ( (LA90_3==NOT||LA90_3==113) ) {
					alt90=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 90, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LPAREN:
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
					// JPA2.g:332:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_or_value_expression3076);
					path_expression334=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression334.getTree());

					}
					break;
				case 2 :
					// JPA2.g:333:7: simple_entity_or_value_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression3084);
					simple_entity_or_value_expression335=simple_entity_or_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_or_value_expression335.getTree());

					}
					break;
				case 3 :
					// JPA2.g:334:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_entity_or_value_expression3092);
					subquery336=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery336.getTree());

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
	// JPA2.g:335:1: simple_entity_or_value_expression : ( identification_variable | input_parameter | literal );
	public final JPA2Parser.simple_entity_or_value_expression_return simple_entity_or_value_expression() throws RecognitionException {
		JPA2Parser.simple_entity_or_value_expression_return retval = new JPA2Parser.simple_entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable337 =null;
		ParserRuleReturnScope input_parameter338 =null;
		ParserRuleReturnScope literal339 =null;


		try {
			// JPA2.g:336:5: ( identification_variable | input_parameter | literal )
			int alt91=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA91_1 = input.LA(2);
				if ( (synpred149_JPA2()) ) {
					alt91=1;
				}
				else if ( (true) ) {
					alt91=3;
				}

				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt91=2;
				}
				break;
			case GROUP:
				{
				alt91=1;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 91, 0, input);
				throw nvae;
			}
			switch (alt91) {
				case 1 :
					// JPA2.g:336:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_or_value_expression3103);
					identification_variable337=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable337.getTree());

					}
					break;
				case 2 :
					// JPA2.g:337:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_or_value_expression3111);
					input_parameter338=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter338.getTree());

					}
					break;
				case 3 :
					// JPA2.g:338:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_simple_entity_or_value_expression3119);
					literal339=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal339.getTree());

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
	// JPA2.g:339:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
	public final JPA2Parser.exists_expression_return exists_expression() throws RecognitionException {
		JPA2Parser.exists_expression_return retval = new JPA2Parser.exists_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal340=null;
		Token string_literal341=null;
		ParserRuleReturnScope subquery342 =null;

		Object string_literal340_tree=null;
		Object string_literal341_tree=null;

		try {
			// JPA2.g:340:5: ( ( 'NOT' )? 'EXISTS' subquery )
			// JPA2.g:340:7: ( 'NOT' )? 'EXISTS' subquery
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:340:7: ( 'NOT' )?
			int alt92=2;
			int LA92_0 = input.LA(1);
			if ( (LA92_0==NOT) ) {
				alt92=1;
			}
			switch (alt92) {
				case 1 :
					// JPA2.g:340:8: 'NOT'
					{
					string_literal340=(Token)match(input,NOT,FOLLOW_NOT_in_exists_expression3131); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal340_tree = (Object)adaptor.create(string_literal340);
					adaptor.addChild(root_0, string_literal340_tree);
					}

					}
					break;

			}

			string_literal341=(Token)match(input,101,FOLLOW_101_in_exists_expression3135); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal341_tree = (Object)adaptor.create(string_literal341);
			adaptor.addChild(root_0, string_literal341_tree);
			}

			pushFollow(FOLLOW_subquery_in_exists_expression3137);
			subquery342=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery342.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:341:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
	public final JPA2Parser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
		JPA2Parser.all_or_any_expression_return retval = new JPA2Parser.all_or_any_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set343=null;
		ParserRuleReturnScope subquery344 =null;

		Object set343_tree=null;

		try {
			// JPA2.g:342:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
			// JPA2.g:342:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
			{
			root_0 = (Object)adaptor.nil();


			set343=input.LT(1);
			if ( (input.LA(1) >= 85 && input.LA(1) <= 86)||input.LA(1)==131 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set343));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_subquery_in_all_or_any_expression3161);
			subquery344=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery344.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:343:1: comparison_expression : ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
	public final JPA2Parser.comparison_expression_return comparison_expression() throws RecognitionException {
		JPA2Parser.comparison_expression_return retval = new JPA2Parser.comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal347=null;
		Token set351=null;
		Token set355=null;
		Token set363=null;
		Token set367=null;
		ParserRuleReturnScope string_expression345 =null;
		ParserRuleReturnScope comparison_operator346 =null;
		ParserRuleReturnScope string_expression348 =null;
		ParserRuleReturnScope all_or_any_expression349 =null;
		ParserRuleReturnScope boolean_expression350 =null;
		ParserRuleReturnScope boolean_expression352 =null;
		ParserRuleReturnScope all_or_any_expression353 =null;
		ParserRuleReturnScope enum_expression354 =null;
		ParserRuleReturnScope enum_expression356 =null;
		ParserRuleReturnScope all_or_any_expression357 =null;
		ParserRuleReturnScope datetime_expression358 =null;
		ParserRuleReturnScope comparison_operator359 =null;
		ParserRuleReturnScope datetime_expression360 =null;
		ParserRuleReturnScope all_or_any_expression361 =null;
		ParserRuleReturnScope entity_expression362 =null;
		ParserRuleReturnScope entity_expression364 =null;
		ParserRuleReturnScope all_or_any_expression365 =null;
		ParserRuleReturnScope entity_type_expression366 =null;
		ParserRuleReturnScope entity_type_expression368 =null;
		ParserRuleReturnScope arithmetic_expression369 =null;
		ParserRuleReturnScope comparison_operator370 =null;
		ParserRuleReturnScope arithmetic_expression371 =null;
		ParserRuleReturnScope all_or_any_expression372 =null;

		Object string_literal347_tree=null;
		Object set351_tree=null;
		Object set355_tree=null;
		Object set363_tree=null;
		Object set367_tree=null;

		try {
			// JPA2.g:344:5: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
			int alt100=7;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA100_1 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred162_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (synpred167_JPA2()) ) {
					alt100=5;
				}
				else if ( (synpred169_JPA2()) ) {
					alt100=6;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 91:
			case 133:
			case 136:
			case 139:
				{
				alt100=1;
				}
				break;
			case 77:
				{
				int LA100_3 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred162_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (synpred167_JPA2()) ) {
					alt100=5;
				}
				else if ( (synpred169_JPA2()) ) {
					alt100=6;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA100_4 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred162_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (synpred167_JPA2()) ) {
					alt100=5;
				}
				else if ( (synpred169_JPA2()) ) {
					alt100=6;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 63:
				{
				int LA100_5 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred162_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (synpred167_JPA2()) ) {
					alt100=5;
				}
				else if ( (synpred169_JPA2()) ) {
					alt100=6;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case COUNT:
				{
				int LA100_11 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA100_12 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 104:
				{
				int LA100_13 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case CASE:
				{
				int LA100_14 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred162_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 90:
				{
				int LA100_15 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred162_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 120:
				{
				int LA100_16 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred162_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 89:
				{
				int LA100_17 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 102:
				{
				int LA100_18 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 82:
				{
				int LA100_19 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA100_20 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred162_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 144:
			case 145:
				{
				alt100=2;
				}
				break;
			case GROUP:
				{
				int LA100_22 = input.LA(2);
				if ( (synpred156_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred159_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred162_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred164_JPA2()) ) {
					alt100=4;
				}
				else if ( (synpred167_JPA2()) ) {
					alt100=5;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 92:
			case 93:
			case 94:
				{
				alt100=4;
				}
				break;
			case 137:
				{
				alt100=6;
				}
				break;
			case INT_NUMERAL:
			case 65:
			case 67:
			case 70:
			case 84:
			case 106:
			case 110:
			case 112:
			case 115:
			case 130:
			case 132:
				{
				alt100=7;
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
					// JPA2.g:344:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_comparison_expression3172);
					string_expression345=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression345.getTree());

					// JPA2.g:344:25: ( comparison_operator | 'REGEXP' )
					int alt93=2;
					int LA93_0 = input.LA(1);
					if ( ((LA93_0 >= 71 && LA93_0 <= 76)) ) {
						alt93=1;
					}
					else if ( (LA93_0==127) ) {
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
							// JPA2.g:344:26: comparison_operator
							{
							pushFollow(FOLLOW_comparison_operator_in_comparison_expression3175);
							comparison_operator346=comparison_operator();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator346.getTree());

							}
							break;
						case 2 :
							// JPA2.g:344:48: 'REGEXP'
							{
							string_literal347=(Token)match(input,127,FOLLOW_127_in_comparison_expression3179); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal347_tree = (Object)adaptor.create(string_literal347);
							adaptor.addChild(root_0, string_literal347_tree);
							}

							}
							break;

					}

					// JPA2.g:344:58: ( string_expression | all_or_any_expression )
					int alt94=2;
					int LA94_0 = input.LA(1);
					if ( (LA94_0==AVG||LA94_0==CASE||LA94_0==COUNT||LA94_0==GROUP||(LA94_0 >= LOWER && LA94_0 <= NAMED_PARAMETER)||(LA94_0 >= STRING_LITERAL && LA94_0 <= SUM)||LA94_0==WORD||LA94_0==63||LA94_0==77||LA94_0==82||(LA94_0 >= 89 && LA94_0 <= 91)||LA94_0==102||LA94_0==104||LA94_0==120||LA94_0==133||LA94_0==136||LA94_0==139) ) {
						alt94=1;
					}
					else if ( ((LA94_0 >= 85 && LA94_0 <= 86)||LA94_0==131) ) {
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
							// JPA2.g:344:59: string_expression
							{
							pushFollow(FOLLOW_string_expression_in_comparison_expression3183);
							string_expression348=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression348.getTree());

							}
							break;
						case 2 :
							// JPA2.g:344:79: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3187);
							all_or_any_expression349=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression349.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:345:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_comparison_expression3196);
					boolean_expression350=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression350.getTree());

					set351=input.LT(1);
					if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set351));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:345:39: ( boolean_expression | all_or_any_expression )
					int alt95=2;
					int LA95_0 = input.LA(1);
					if ( (LA95_0==CASE||LA95_0==GROUP||LA95_0==LPAREN||LA95_0==NAMED_PARAMETER||LA95_0==WORD||LA95_0==63||LA95_0==77||LA95_0==82||(LA95_0 >= 89 && LA95_0 <= 90)||LA95_0==102||LA95_0==104||LA95_0==120||(LA95_0 >= 144 && LA95_0 <= 145)) ) {
						alt95=1;
					}
					else if ( ((LA95_0 >= 85 && LA95_0 <= 86)||LA95_0==131) ) {
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
							// JPA2.g:345:40: boolean_expression
							{
							pushFollow(FOLLOW_boolean_expression_in_comparison_expression3207);
							boolean_expression352=boolean_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression352.getTree());

							}
							break;
						case 2 :
							// JPA2.g:345:61: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3211);
							all_or_any_expression353=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression353.getTree());

							}
							break;

					}

					}
					break;
				case 3 :
					// JPA2.g:346:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_comparison_expression3220);
					enum_expression354=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression354.getTree());

					set355=input.LT(1);
					if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set355));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:346:34: ( enum_expression | all_or_any_expression )
					int alt96=2;
					int LA96_0 = input.LA(1);
					if ( (LA96_0==CASE||LA96_0==GROUP||LA96_0==LPAREN||LA96_0==NAMED_PARAMETER||LA96_0==WORD||LA96_0==63||LA96_0==77||LA96_0==90||LA96_0==120) ) {
						alt96=1;
					}
					else if ( ((LA96_0 >= 85 && LA96_0 <= 86)||LA96_0==131) ) {
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
							// JPA2.g:346:35: enum_expression
							{
							pushFollow(FOLLOW_enum_expression_in_comparison_expression3229);
							enum_expression356=enum_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression356.getTree());

							}
							break;
						case 2 :
							// JPA2.g:346:53: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3233);
							all_or_any_expression357=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression357.getTree());

							}
							break;

					}

					}
					break;
				case 4 :
					// JPA2.g:347:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_comparison_expression3242);
					datetime_expression358=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression358.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3244);
					comparison_operator359=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator359.getTree());

					// JPA2.g:347:47: ( datetime_expression | all_or_any_expression )
					int alt97=2;
					int LA97_0 = input.LA(1);
					if ( (LA97_0==AVG||LA97_0==CASE||LA97_0==COUNT||LA97_0==GROUP||(LA97_0 >= LPAREN && LA97_0 <= NAMED_PARAMETER)||LA97_0==SUM||LA97_0==WORD||LA97_0==63||LA97_0==77||LA97_0==82||(LA97_0 >= 89 && LA97_0 <= 90)||(LA97_0 >= 92 && LA97_0 <= 94)||LA97_0==102||LA97_0==104||LA97_0==120) ) {
						alt97=1;
					}
					else if ( ((LA97_0 >= 85 && LA97_0 <= 86)||LA97_0==131) ) {
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
							// JPA2.g:347:48: datetime_expression
							{
							pushFollow(FOLLOW_datetime_expression_in_comparison_expression3247);
							datetime_expression360=datetime_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression360.getTree());

							}
							break;
						case 2 :
							// JPA2.g:347:70: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3251);
							all_or_any_expression361=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression361.getTree());

							}
							break;

					}

					}
					break;
				case 5 :
					// JPA2.g:348:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_expression_in_comparison_expression3260);
					entity_expression362=entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression362.getTree());

					set363=input.LT(1);
					if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set363));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:348:38: ( entity_expression | all_or_any_expression )
					int alt98=2;
					int LA98_0 = input.LA(1);
					if ( (LA98_0==GROUP||LA98_0==NAMED_PARAMETER||LA98_0==WORD||LA98_0==63||LA98_0==77) ) {
						alt98=1;
					}
					else if ( ((LA98_0 >= 85 && LA98_0 <= 86)||LA98_0==131) ) {
						alt98=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 98, 0, input);
						throw nvae;
					}

					switch (alt98) {
						case 1 :
							// JPA2.g:348:39: entity_expression
							{
							pushFollow(FOLLOW_entity_expression_in_comparison_expression3271);
							entity_expression364=entity_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression364.getTree());

							}
							break;
						case 2 :
							// JPA2.g:348:59: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3275);
							all_or_any_expression365=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression365.getTree());

							}
							break;

					}

					}
					break;
				case 6 :
					// JPA2.g:349:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3284);
					entity_type_expression366=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression366.getTree());

					set367=input.LT(1);
					if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set367));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3294);
					entity_type_expression368=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression368.getTree());

					}
					break;
				case 7 :
					// JPA2.g:350:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3302);
					arithmetic_expression369=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression369.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3304);
					comparison_operator370=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator370.getTree());

					// JPA2.g:350:49: ( arithmetic_expression | all_or_any_expression )
					int alt99=2;
					int LA99_0 = input.LA(1);
					if ( (LA99_0==AVG||LA99_0==CASE||LA99_0==COUNT||LA99_0==GROUP||LA99_0==INT_NUMERAL||(LA99_0 >= LPAREN && LA99_0 <= NAMED_PARAMETER)||LA99_0==SUM||LA99_0==WORD||LA99_0==63||LA99_0==65||LA99_0==67||LA99_0==70||LA99_0==77||LA99_0==82||LA99_0==84||(LA99_0 >= 89 && LA99_0 <= 90)||LA99_0==102||LA99_0==104||LA99_0==106||LA99_0==110||LA99_0==112||LA99_0==115||LA99_0==120||LA99_0==130||LA99_0==132) ) {
						alt99=1;
					}
					else if ( ((LA99_0 >= 85 && LA99_0 <= 86)||LA99_0==131) ) {
						alt99=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 99, 0, input);
						throw nvae;
					}

					switch (alt99) {
						case 1 :
							// JPA2.g:350:50: arithmetic_expression
							{
							pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3307);
							arithmetic_expression371=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression371.getTree());

							}
							break;
						case 2 :
							// JPA2.g:350:74: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3311);
							all_or_any_expression372=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression372.getTree());

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
	// JPA2.g:352:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
	public final JPA2Parser.comparison_operator_return comparison_operator() throws RecognitionException {
		JPA2Parser.comparison_operator_return retval = new JPA2Parser.comparison_operator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set373=null;

		Object set373_tree=null;

		try {
			// JPA2.g:353:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set373=input.LT(1);
			if ( (input.LA(1) >= 71 && input.LA(1) <= 76) ) {
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
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:359:1: arithmetic_expression : ( arithmetic_term ( ( '+' | '-' ) arithmetic_term )+ | arithmetic_term );
	public final JPA2Parser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
		JPA2Parser.arithmetic_expression_return retval = new JPA2Parser.arithmetic_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set375=null;
		ParserRuleReturnScope arithmetic_term374 =null;
		ParserRuleReturnScope arithmetic_term376 =null;
		ParserRuleReturnScope arithmetic_term377 =null;

		Object set375_tree=null;

		try {
			// JPA2.g:360:5: ( arithmetic_term ( ( '+' | '-' ) arithmetic_term )+ | arithmetic_term )
			int alt102=2;
			switch ( input.LA(1) ) {
			case 65:
			case 67:
				{
				int LA102_1 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case GROUP:
			case WORD:
				{
				int LA102_2 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA102_3 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 70:
				{
				int LA102_4 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case LPAREN:
				{
				int LA102_5 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 77:
				{
				int LA102_6 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA102_7 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 63:
				{
				int LA102_8 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 110:
				{
				int LA102_9 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 112:
				{
				int LA102_10 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 84:
				{
				int LA102_11 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 132:
				{
				int LA102_12 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 115:
				{
				int LA102_13 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 130:
				{
				int LA102_14 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 106:
				{
				int LA102_15 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case COUNT:
				{
				int LA102_16 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA102_17 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 104:
				{
				int LA102_18 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case CASE:
				{
				int LA102_19 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 90:
				{
				int LA102_20 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 120:
				{
				int LA102_21 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 89:
				{
				int LA102_22 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 102:
				{
				int LA102_23 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 82:
				{
				int LA102_24 = input.LA(2);
				if ( (synpred178_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

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
					// JPA2.g:360:7: arithmetic_term ( ( '+' | '-' ) arithmetic_term )+
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3375);
					arithmetic_term374=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term374.getTree());

					// JPA2.g:360:23: ( ( '+' | '-' ) arithmetic_term )+
					int cnt101=0;
					loop101:
					while (true) {
						int alt101=2;
						int LA101_0 = input.LA(1);
						if ( (LA101_0==65||LA101_0==67) ) {
							alt101=1;
						}

						switch (alt101) {
						case 1 :
							// JPA2.g:360:24: ( '+' | '-' ) arithmetic_term
							{
							set375=input.LT(1);
							if ( input.LA(1)==65||input.LA(1)==67 ) {
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
							pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3386);
							arithmetic_term376=arithmetic_term();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term376.getTree());

							}
							break;

						default :
							if ( cnt101 >= 1 ) break loop101;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(101, input);
							throw eee;
						}
						cnt101++;
					}

					}
					break;
				case 2 :
					// JPA2.g:361:7: arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3396);
					arithmetic_term377=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term377.getTree());

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
	// JPA2.g:362:1: arithmetic_term : ( arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+ | arithmetic_factor );
	public final JPA2Parser.arithmetic_term_return arithmetic_term() throws RecognitionException {
		JPA2Parser.arithmetic_term_return retval = new JPA2Parser.arithmetic_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set379=null;
		ParserRuleReturnScope arithmetic_factor378 =null;
		ParserRuleReturnScope arithmetic_factor380 =null;
		ParserRuleReturnScope arithmetic_factor381 =null;

		Object set379_tree=null;

		try {
			// JPA2.g:363:5: ( arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+ | arithmetic_factor )
			int alt104=2;
			switch ( input.LA(1) ) {
			case 65:
			case 67:
				{
				int LA104_1 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case GROUP:
			case WORD:
				{
				int LA104_2 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA104_3 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 70:
				{
				int LA104_4 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case LPAREN:
				{
				int LA104_5 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 77:
				{
				int LA104_6 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA104_7 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 63:
				{
				int LA104_8 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 110:
				{
				int LA104_9 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 112:
				{
				int LA104_10 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 84:
				{
				int LA104_11 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 132:
				{
				int LA104_12 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 115:
				{
				int LA104_13 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 130:
				{
				int LA104_14 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 106:
				{
				int LA104_15 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case COUNT:
				{
				int LA104_16 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA104_17 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 104:
				{
				int LA104_18 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case CASE:
				{
				int LA104_19 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 90:
				{
				int LA104_20 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 120:
				{
				int LA104_21 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 89:
				{
				int LA104_22 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 102:
				{
				int LA104_23 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

				}
				break;
			case 82:
				{
				int LA104_24 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt104=1;
				}
				else if ( (true) ) {
					alt104=2;
				}

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
					// JPA2.g:363:7: arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3407);
					arithmetic_factor378=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor378.getTree());

					// JPA2.g:363:25: ( ( '*' | '/' ) arithmetic_factor )+
					int cnt103=0;
					loop103:
					while (true) {
						int alt103=2;
						int LA103_0 = input.LA(1);
						if ( (LA103_0==64||LA103_0==69) ) {
							alt103=1;
						}

						switch (alt103) {
						case 1 :
							// JPA2.g:363:26: ( '*' | '/' ) arithmetic_factor
							{
							set379=input.LT(1);
							if ( input.LA(1)==64||input.LA(1)==69 ) {
								input.consume();
								if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set379));
								state.errorRecovery=false;
								state.failed=false;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3419);
							arithmetic_factor380=arithmetic_factor();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor380.getTree());

							}
							break;

						default :
							if ( cnt103 >= 1 ) break loop103;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(103, input);
							throw eee;
						}
						cnt103++;
					}

					}
					break;
				case 2 :
					// JPA2.g:364:7: arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3429);
					arithmetic_factor381=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor381.getTree());

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
	// JPA2.g:365:1: arithmetic_factor : ( ( '+' | '-' ) )? arithmetic_primary ;
	public final JPA2Parser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
		JPA2Parser.arithmetic_factor_return retval = new JPA2Parser.arithmetic_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set382=null;
		ParserRuleReturnScope arithmetic_primary383 =null;

		Object set382_tree=null;

		try {
			// JPA2.g:366:5: ( ( ( '+' | '-' ) )? arithmetic_primary )
			// JPA2.g:366:7: ( ( '+' | '-' ) )? arithmetic_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:366:7: ( ( '+' | '-' ) )?
			int alt105=2;
			int LA105_0 = input.LA(1);
			if ( (LA105_0==65||LA105_0==67) ) {
				alt105=1;
			}
			switch (alt105) {
				case 1 :
					// JPA2.g:
					{
					set382=input.LT(1);
					if ( input.LA(1)==65||input.LA(1)==67 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set382));
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

			pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor3452);
			arithmetic_primary383=arithmetic_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary383.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:367:1: arithmetic_primary : ( path_expression | decimal_literal | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
		JPA2Parser.arithmetic_primary_return retval = new JPA2Parser.arithmetic_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal387=null;
		Token char_literal389=null;
		ParserRuleReturnScope path_expression384 =null;
		ParserRuleReturnScope decimal_literal385 =null;
		ParserRuleReturnScope numeric_literal386 =null;
		ParserRuleReturnScope arithmetic_expression388 =null;
		ParserRuleReturnScope input_parameter390 =null;
		ParserRuleReturnScope functions_returning_numerics391 =null;
		ParserRuleReturnScope aggregate_expression392 =null;
		ParserRuleReturnScope case_expression393 =null;
		ParserRuleReturnScope function_invocation394 =null;
		ParserRuleReturnScope extension_functions395 =null;
		ParserRuleReturnScope subquery396 =null;

		Object char_literal387_tree=null;
		Object char_literal389_tree=null;

		try {
			// JPA2.g:368:5: ( path_expression | decimal_literal | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
			int alt106=11;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				alt106=1;
				}
				break;
			case INT_NUMERAL:
				{
				int LA106_2 = input.LA(2);
				if ( (synpred185_JPA2()) ) {
					alt106=2;
				}
				else if ( (synpred186_JPA2()) ) {
					alt106=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 106, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 70:
				{
				alt106=3;
				}
				break;
			case LPAREN:
				{
				int LA106_4 = input.LA(2);
				if ( (synpred187_JPA2()) ) {
					alt106=4;
				}
				else if ( (true) ) {
					alt106=11;
				}

				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt106=5;
				}
				break;
			case 84:
			case 106:
			case 110:
			case 112:
			case 115:
			case 130:
			case 132:
				{
				alt106=6;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt106=7;
				}
				break;
			case 104:
				{
				int LA106_17 = input.LA(2);
				if ( (synpred190_JPA2()) ) {
					alt106=7;
				}
				else if ( (synpred192_JPA2()) ) {
					alt106=9;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 106, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case CASE:
			case 90:
			case 120:
				{
				alt106=8;
				}
				break;
			case 82:
			case 89:
			case 102:
				{
				alt106=10;
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
					// JPA2.g:368:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_arithmetic_primary3463);
					path_expression384=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression384.getTree());

					}
					break;
				case 2 :
					// JPA2.g:369:7: decimal_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_decimal_literal_in_arithmetic_primary3471);
					decimal_literal385=decimal_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, decimal_literal385.getTree());

					}
					break;
				case 3 :
					// JPA2.g:370:7: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary3479);
					numeric_literal386=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal386.getTree());

					}
					break;
				case 4 :
					// JPA2.g:371:7: '(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal387=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary3487); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal387_tree = (Object)adaptor.create(char_literal387);
					adaptor.addChild(root_0, char_literal387_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_arithmetic_primary3488);
					arithmetic_expression388=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression388.getTree());

					char_literal389=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary3489); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal389_tree = (Object)adaptor.create(char_literal389);
					adaptor.addChild(root_0, char_literal389_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:372:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_arithmetic_primary3497);
					input_parameter390=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter390.getTree());

					}
					break;
				case 6 :
					// JPA2.g:373:7: functions_returning_numerics
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary3505);
					functions_returning_numerics391=functions_returning_numerics();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics391.getTree());

					}
					break;
				case 7 :
					// JPA2.g:374:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary3513);
					aggregate_expression392=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression392.getTree());

					}
					break;
				case 8 :
					// JPA2.g:375:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_arithmetic_primary3521);
					case_expression393=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression393.getTree());

					}
					break;
				case 9 :
					// JPA2.g:376:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_arithmetic_primary3529);
					function_invocation394=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation394.getTree());

					}
					break;
				case 10 :
					// JPA2.g:377:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_arithmetic_primary3537);
					extension_functions395=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions395.getTree());

					}
					break;
				case 11 :
					// JPA2.g:378:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_arithmetic_primary3545);
					subquery396=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery396.getTree());

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
	// JPA2.g:379:1: string_expression : ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.string_expression_return string_expression() throws RecognitionException {
		JPA2Parser.string_expression_return retval = new JPA2Parser.string_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression397 =null;
		ParserRuleReturnScope string_literal398 =null;
		ParserRuleReturnScope input_parameter399 =null;
		ParserRuleReturnScope functions_returning_strings400 =null;
		ParserRuleReturnScope aggregate_expression401 =null;
		ParserRuleReturnScope case_expression402 =null;
		ParserRuleReturnScope function_invocation403 =null;
		ParserRuleReturnScope extension_functions404 =null;
		ParserRuleReturnScope subquery405 =null;


		try {
			// JPA2.g:380:5: ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
			int alt107=9;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				alt107=1;
				}
				break;
			case STRING_LITERAL:
				{
				alt107=2;
				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt107=3;
				}
				break;
			case LOWER:
			case 91:
			case 133:
			case 136:
			case 139:
				{
				alt107=4;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt107=5;
				}
				break;
			case 104:
				{
				int LA107_13 = input.LA(2);
				if ( (synpred198_JPA2()) ) {
					alt107=5;
				}
				else if ( (synpred200_JPA2()) ) {
					alt107=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 107, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case CASE:
			case 90:
			case 120:
				{
				alt107=6;
				}
				break;
			case 82:
			case 89:
			case 102:
				{
				alt107=8;
				}
				break;
			case LPAREN:
				{
				alt107=9;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 107, 0, input);
				throw nvae;
			}
			switch (alt107) {
				case 1 :
					// JPA2.g:380:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_string_expression3556);
					path_expression397=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression397.getTree());

					}
					break;
				case 2 :
					// JPA2.g:381:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_string_expression3564);
					string_literal398=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal398.getTree());

					}
					break;
				case 3 :
					// JPA2.g:382:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_string_expression3572);
					input_parameter399=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter399.getTree());

					}
					break;
				case 4 :
					// JPA2.g:383:7: functions_returning_strings
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_strings_in_string_expression3580);
					functions_returning_strings400=functions_returning_strings();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings400.getTree());

					}
					break;
				case 5 :
					// JPA2.g:384:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_string_expression3588);
					aggregate_expression401=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression401.getTree());

					}
					break;
				case 6 :
					// JPA2.g:385:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_string_expression3596);
					case_expression402=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression402.getTree());

					}
					break;
				case 7 :
					// JPA2.g:386:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_string_expression3604);
					function_invocation403=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation403.getTree());

					}
					break;
				case 8 :
					// JPA2.g:387:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_string_expression3612);
					extension_functions404=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions404.getTree());

					}
					break;
				case 9 :
					// JPA2.g:388:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_string_expression3620);
					subquery405=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery405.getTree());

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
	// JPA2.g:389:1: datetime_expression : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery );
	public final JPA2Parser.datetime_expression_return datetime_expression() throws RecognitionException {
		JPA2Parser.datetime_expression_return retval = new JPA2Parser.datetime_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression406 =null;
		ParserRuleReturnScope input_parameter407 =null;
		ParserRuleReturnScope functions_returning_datetime408 =null;
		ParserRuleReturnScope aggregate_expression409 =null;
		ParserRuleReturnScope case_expression410 =null;
		ParserRuleReturnScope function_invocation411 =null;
		ParserRuleReturnScope extension_functions412 =null;
		ParserRuleReturnScope date_time_timestamp_literal413 =null;
		ParserRuleReturnScope subquery414 =null;


		try {
			// JPA2.g:390:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery )
			int alt108=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA108_1 = input.LA(2);
				if ( (synpred202_JPA2()) ) {
					alt108=1;
				}
				else if ( (synpred209_JPA2()) ) {
					alt108=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 108, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt108=2;
				}
				break;
			case 92:
			case 93:
			case 94:
				{
				alt108=3;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt108=4;
				}
				break;
			case 104:
				{
				int LA108_8 = input.LA(2);
				if ( (synpred205_JPA2()) ) {
					alt108=4;
				}
				else if ( (synpred207_JPA2()) ) {
					alt108=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 108, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case CASE:
			case 90:
			case 120:
				{
				alt108=5;
				}
				break;
			case 82:
			case 89:
			case 102:
				{
				alt108=7;
				}
				break;
			case GROUP:
				{
				alt108=1;
				}
				break;
			case LPAREN:
				{
				alt108=9;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 108, 0, input);
				throw nvae;
			}
			switch (alt108) {
				case 1 :
					// JPA2.g:390:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_datetime_expression3631);
					path_expression406=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression406.getTree());

					}
					break;
				case 2 :
					// JPA2.g:391:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_datetime_expression3639);
					input_parameter407=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter407.getTree());

					}
					break;
				case 3 :
					// JPA2.g:392:7: functions_returning_datetime
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_datetime_in_datetime_expression3647);
					functions_returning_datetime408=functions_returning_datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime408.getTree());

					}
					break;
				case 4 :
					// JPA2.g:393:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_datetime_expression3655);
					aggregate_expression409=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression409.getTree());

					}
					break;
				case 5 :
					// JPA2.g:394:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_datetime_expression3663);
					case_expression410=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression410.getTree());

					}
					break;
				case 6 :
					// JPA2.g:395:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_datetime_expression3671);
					function_invocation411=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation411.getTree());

					}
					break;
				case 7 :
					// JPA2.g:396:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_datetime_expression3679);
					extension_functions412=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions412.getTree());

					}
					break;
				case 8 :
					// JPA2.g:397:7: date_time_timestamp_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_time_timestamp_literal_in_datetime_expression3687);
					date_time_timestamp_literal413=date_time_timestamp_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_time_timestamp_literal413.getTree());

					}
					break;
				case 9 :
					// JPA2.g:398:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_datetime_expression3695);
					subquery414=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery414.getTree());

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
	// JPA2.g:399:1: boolean_expression : ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.boolean_expression_return boolean_expression() throws RecognitionException {
		JPA2Parser.boolean_expression_return retval = new JPA2Parser.boolean_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression415 =null;
		ParserRuleReturnScope boolean_literal416 =null;
		ParserRuleReturnScope input_parameter417 =null;
		ParserRuleReturnScope case_expression418 =null;
		ParserRuleReturnScope function_invocation419 =null;
		ParserRuleReturnScope extension_functions420 =null;
		ParserRuleReturnScope subquery421 =null;


		try {
			// JPA2.g:400:5: ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery )
			int alt109=7;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				alt109=1;
				}
				break;
			case 144:
			case 145:
				{
				alt109=2;
				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt109=3;
				}
				break;
			case CASE:
			case 90:
			case 120:
				{
				alt109=4;
				}
				break;
			case 104:
				{
				alt109=5;
				}
				break;
			case 82:
			case 89:
			case 102:
				{
				alt109=6;
				}
				break;
			case LPAREN:
				{
				alt109=7;
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
					// JPA2.g:400:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_boolean_expression3706);
					path_expression415=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression415.getTree());

					}
					break;
				case 2 :
					// JPA2.g:401:7: boolean_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_literal_in_boolean_expression3714);
					boolean_literal416=boolean_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal416.getTree());

					}
					break;
				case 3 :
					// JPA2.g:402:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_boolean_expression3722);
					input_parameter417=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter417.getTree());

					}
					break;
				case 4 :
					// JPA2.g:403:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_boolean_expression3730);
					case_expression418=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression418.getTree());

					}
					break;
				case 5 :
					// JPA2.g:404:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_boolean_expression3738);
					function_invocation419=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation419.getTree());

					}
					break;
				case 6 :
					// JPA2.g:405:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_boolean_expression3746);
					extension_functions420=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions420.getTree());

					}
					break;
				case 7 :
					// JPA2.g:406:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_boolean_expression3754);
					subquery421=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery421.getTree());

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
	// JPA2.g:407:1: enum_expression : ( path_expression | enum_literal | input_parameter | case_expression | subquery );
	public final JPA2Parser.enum_expression_return enum_expression() throws RecognitionException {
		JPA2Parser.enum_expression_return retval = new JPA2Parser.enum_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression422 =null;
		ParserRuleReturnScope enum_literal423 =null;
		ParserRuleReturnScope input_parameter424 =null;
		ParserRuleReturnScope case_expression425 =null;
		ParserRuleReturnScope subquery426 =null;


		try {
			// JPA2.g:408:5: ( path_expression | enum_literal | input_parameter | case_expression | subquery )
			int alt110=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA110_1 = input.LA(2);
				if ( (LA110_1==68) ) {
					alt110=1;
				}
				else if ( (LA110_1==EOF||(LA110_1 >= AND && LA110_1 <= ASC)||LA110_1==DESC||(LA110_1 >= ELSE && LA110_1 <= END)||(LA110_1 >= GROUP && LA110_1 <= HAVING)||LA110_1==INNER||(LA110_1 >= JOIN && LA110_1 <= LEFT)||(LA110_1 >= OR && LA110_1 <= ORDER)||LA110_1==RPAREN||LA110_1==SET||LA110_1==THEN||(LA110_1 >= WHEN && LA110_1 <= WORD)||LA110_1==66||(LA110_1 >= 73 && LA110_1 <= 74)||LA110_1==103||(LA110_1 >= 121 && LA110_1 <= 122)||LA110_1==142) ) {
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
			case GROUP:
				{
				alt110=1;
				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt110=3;
				}
				break;
			case CASE:
			case 90:
			case 120:
				{
				alt110=4;
				}
				break;
			case LPAREN:
				{
				alt110=5;
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
					// JPA2.g:408:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_enum_expression3765);
					path_expression422=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression422.getTree());

					}
					break;
				case 2 :
					// JPA2.g:409:7: enum_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_literal_in_enum_expression3773);
					enum_literal423=enum_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal423.getTree());

					}
					break;
				case 3 :
					// JPA2.g:410:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_enum_expression3781);
					input_parameter424=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter424.getTree());

					}
					break;
				case 4 :
					// JPA2.g:411:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_enum_expression3789);
					case_expression425=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression425.getTree());

					}
					break;
				case 5 :
					// JPA2.g:412:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_enum_expression3797);
					subquery426=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery426.getTree());

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
	// JPA2.g:413:1: entity_expression : ( path_expression | simple_entity_expression );
	public final JPA2Parser.entity_expression_return entity_expression() throws RecognitionException {
		JPA2Parser.entity_expression_return retval = new JPA2Parser.entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression427 =null;
		ParserRuleReturnScope simple_entity_expression428 =null;


		try {
			// JPA2.g:414:5: ( path_expression | simple_entity_expression )
			int alt111=2;
			int LA111_0 = input.LA(1);
			if ( (LA111_0==GROUP||LA111_0==WORD) ) {
				int LA111_1 = input.LA(2);
				if ( (LA111_1==68) ) {
					alt111=1;
				}
				else if ( (LA111_1==EOF||LA111_1==AND||(LA111_1 >= GROUP && LA111_1 <= HAVING)||LA111_1==INNER||(LA111_1 >= JOIN && LA111_1 <= LEFT)||(LA111_1 >= OR && LA111_1 <= ORDER)||LA111_1==RPAREN||LA111_1==SET||LA111_1==THEN||LA111_1==66||(LA111_1 >= 73 && LA111_1 <= 74)||LA111_1==142) ) {
					alt111=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 111, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA111_0==NAMED_PARAMETER||LA111_0==63||LA111_0==77) ) {
				alt111=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 111, 0, input);
				throw nvae;
			}

			switch (alt111) {
				case 1 :
					// JPA2.g:414:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_expression3808);
					path_expression427=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression427.getTree());

					}
					break;
				case 2 :
					// JPA2.g:415:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3816);
					simple_entity_expression428=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression428.getTree());

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
	// JPA2.g:416:1: simple_entity_expression : ( identification_variable | input_parameter );
	public final JPA2Parser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
		JPA2Parser.simple_entity_expression_return retval = new JPA2Parser.simple_entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable429 =null;
		ParserRuleReturnScope input_parameter430 =null;


		try {
			// JPA2.g:417:5: ( identification_variable | input_parameter )
			int alt112=2;
			int LA112_0 = input.LA(1);
			if ( (LA112_0==GROUP||LA112_0==WORD) ) {
				alt112=1;
			}
			else if ( (LA112_0==NAMED_PARAMETER||LA112_0==63||LA112_0==77) ) {
				alt112=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 112, 0, input);
				throw nvae;
			}

			switch (alt112) {
				case 1 :
					// JPA2.g:417:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3827);
					identification_variable429=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable429.getTree());

					}
					break;
				case 2 :
					// JPA2.g:418:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3835);
					input_parameter430=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter430.getTree());

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
	// JPA2.g:419:1: entity_type_expression : ( type_discriminator | entity_type_literal | input_parameter );
	public final JPA2Parser.entity_type_expression_return entity_type_expression() throws RecognitionException {
		JPA2Parser.entity_type_expression_return retval = new JPA2Parser.entity_type_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope type_discriminator431 =null;
		ParserRuleReturnScope entity_type_literal432 =null;
		ParserRuleReturnScope input_parameter433 =null;


		try {
			// JPA2.g:420:5: ( type_discriminator | entity_type_literal | input_parameter )
			int alt113=3;
			switch ( input.LA(1) ) {
			case 137:
				{
				alt113=1;
				}
				break;
			case WORD:
				{
				alt113=2;
				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt113=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 113, 0, input);
				throw nvae;
			}
			switch (alt113) {
				case 1 :
					// JPA2.g:420:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_entity_type_expression3846);
					type_discriminator431=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator431.getTree());

					}
					break;
				case 2 :
					// JPA2.g:421:7: entity_type_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_literal_in_entity_type_expression3854);
					entity_type_literal432=entity_type_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_literal432.getTree());

					}
					break;
				case 3 :
					// JPA2.g:422:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_entity_type_expression3862);
					input_parameter433=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter433.getTree());

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
	// JPA2.g:423:1: type_discriminator : 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' ;
	public final JPA2Parser.type_discriminator_return type_discriminator() throws RecognitionException {
		JPA2Parser.type_discriminator_return retval = new JPA2Parser.type_discriminator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal434=null;
		Token char_literal438=null;
		ParserRuleReturnScope general_identification_variable435 =null;
		ParserRuleReturnScope path_expression436 =null;
		ParserRuleReturnScope input_parameter437 =null;

		Object string_literal434_tree=null;
		Object char_literal438_tree=null;

		try {
			// JPA2.g:424:5: ( 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' )
			// JPA2.g:424:7: 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal434=(Token)match(input,137,FOLLOW_137_in_type_discriminator3873); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal434_tree = (Object)adaptor.create(string_literal434);
			adaptor.addChild(root_0, string_literal434_tree);
			}

			// JPA2.g:424:15: ( general_identification_variable | path_expression | input_parameter )
			int alt114=3;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				int LA114_1 = input.LA(2);
				if ( (LA114_1==RPAREN) ) {
					alt114=1;
				}
				else if ( (LA114_1==68) ) {
					alt114=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 114, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 108:
			case 140:
				{
				alt114=1;
				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt114=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 114, 0, input);
				throw nvae;
			}
			switch (alt114) {
				case 1 :
					// JPA2.g:424:16: general_identification_variable
					{
					pushFollow(FOLLOW_general_identification_variable_in_type_discriminator3876);
					general_identification_variable435=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable435.getTree());

					}
					break;
				case 2 :
					// JPA2.g:424:50: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_type_discriminator3880);
					path_expression436=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression436.getTree());

					}
					break;
				case 3 :
					// JPA2.g:424:68: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_type_discriminator3884);
					input_parameter437=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter437.getTree());

					}
					break;

			}

			char_literal438=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_type_discriminator3887); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal438_tree = (Object)adaptor.create(char_literal438);
			adaptor.addChild(root_0, char_literal438_tree);
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
	// JPA2.g:425:1: functions_returning_numerics : ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' );
	public final JPA2Parser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
		JPA2Parser.functions_returning_numerics_return retval = new JPA2Parser.functions_returning_numerics_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal439=null;
		Token char_literal441=null;
		Token string_literal442=null;
		Token char_literal444=null;
		Token char_literal446=null;
		Token char_literal448=null;
		Token string_literal449=null;
		Token char_literal451=null;
		Token string_literal452=null;
		Token char_literal454=null;
		Token string_literal455=null;
		Token char_literal457=null;
		Token char_literal459=null;
		Token string_literal460=null;
		Token char_literal462=null;
		Token string_literal463=null;
		Token char_literal465=null;
		ParserRuleReturnScope string_expression440 =null;
		ParserRuleReturnScope string_expression443 =null;
		ParserRuleReturnScope string_expression445 =null;
		ParserRuleReturnScope arithmetic_expression447 =null;
		ParserRuleReturnScope arithmetic_expression450 =null;
		ParserRuleReturnScope arithmetic_expression453 =null;
		ParserRuleReturnScope arithmetic_expression456 =null;
		ParserRuleReturnScope arithmetic_expression458 =null;
		ParserRuleReturnScope path_expression461 =null;
		ParserRuleReturnScope identification_variable464 =null;

		Object string_literal439_tree=null;
		Object char_literal441_tree=null;
		Object string_literal442_tree=null;
		Object char_literal444_tree=null;
		Object char_literal446_tree=null;
		Object char_literal448_tree=null;
		Object string_literal449_tree=null;
		Object char_literal451_tree=null;
		Object string_literal452_tree=null;
		Object char_literal454_tree=null;
		Object string_literal455_tree=null;
		Object char_literal457_tree=null;
		Object char_literal459_tree=null;
		Object string_literal460_tree=null;
		Object char_literal462_tree=null;
		Object string_literal463_tree=null;
		Object char_literal465_tree=null;

		try {
			// JPA2.g:426:5: ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' )
			int alt116=7;
			switch ( input.LA(1) ) {
			case 110:
				{
				alt116=1;
				}
				break;
			case 112:
				{
				alt116=2;
				}
				break;
			case 84:
				{
				alt116=3;
				}
				break;
			case 132:
				{
				alt116=4;
				}
				break;
			case 115:
				{
				alt116=5;
				}
				break;
			case 130:
				{
				alt116=6;
				}
				break;
			case 106:
				{
				alt116=7;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 116, 0, input);
				throw nvae;
			}
			switch (alt116) {
				case 1 :
					// JPA2.g:426:7: 'LENGTH(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal439=(Token)match(input,110,FOLLOW_110_in_functions_returning_numerics3898); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal439_tree = (Object)adaptor.create(string_literal439);
					adaptor.addChild(root_0, string_literal439_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3899);
					string_expression440=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression440.getTree());

					char_literal441=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3900); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal441_tree = (Object)adaptor.create(char_literal441);
					adaptor.addChild(root_0, char_literal441_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:427:7: 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal442=(Token)match(input,112,FOLLOW_112_in_functions_returning_numerics3908); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal442_tree = (Object)adaptor.create(string_literal442);
					adaptor.addChild(root_0, string_literal442_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3910);
					string_expression443=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression443.getTree());

					char_literal444=(Token)match(input,66,FOLLOW_66_in_functions_returning_numerics3911); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal444_tree = (Object)adaptor.create(char_literal444);
					adaptor.addChild(root_0, char_literal444_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3913);
					string_expression445=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression445.getTree());

					// JPA2.g:427:55: ( ',' arithmetic_expression )?
					int alt115=2;
					int LA115_0 = input.LA(1);
					if ( (LA115_0==66) ) {
						alt115=1;
					}
					switch (alt115) {
						case 1 :
							// JPA2.g:427:56: ',' arithmetic_expression
							{
							char_literal446=(Token)match(input,66,FOLLOW_66_in_functions_returning_numerics3915); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal446_tree = (Object)adaptor.create(char_literal446);
							adaptor.addChild(root_0, char_literal446_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3916);
							arithmetic_expression447=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression447.getTree());

							}
							break;

					}

					char_literal448=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3919); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal448_tree = (Object)adaptor.create(char_literal448);
					adaptor.addChild(root_0, char_literal448_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:428:7: 'ABS(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal449=(Token)match(input,84,FOLLOW_84_in_functions_returning_numerics3927); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal449_tree = (Object)adaptor.create(string_literal449);
					adaptor.addChild(root_0, string_literal449_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3928);
					arithmetic_expression450=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression450.getTree());

					char_literal451=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3929); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal451_tree = (Object)adaptor.create(char_literal451);
					adaptor.addChild(root_0, char_literal451_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:429:7: 'SQRT(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal452=(Token)match(input,132,FOLLOW_132_in_functions_returning_numerics3937); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal452_tree = (Object)adaptor.create(string_literal452);
					adaptor.addChild(root_0, string_literal452_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3938);
					arithmetic_expression453=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression453.getTree());

					char_literal454=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3939); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal454_tree = (Object)adaptor.create(char_literal454);
					adaptor.addChild(root_0, char_literal454_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:430:7: 'MOD(' arithmetic_expression ',' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal455=(Token)match(input,115,FOLLOW_115_in_functions_returning_numerics3947); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal455_tree = (Object)adaptor.create(string_literal455);
					adaptor.addChild(root_0, string_literal455_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3948);
					arithmetic_expression456=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression456.getTree());

					char_literal457=(Token)match(input,66,FOLLOW_66_in_functions_returning_numerics3949); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal457_tree = (Object)adaptor.create(char_literal457);
					adaptor.addChild(root_0, char_literal457_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3951);
					arithmetic_expression458=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression458.getTree());

					char_literal459=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3952); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal459_tree = (Object)adaptor.create(char_literal459);
					adaptor.addChild(root_0, char_literal459_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:431:7: 'SIZE(' path_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal460=(Token)match(input,130,FOLLOW_130_in_functions_returning_numerics3960); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal460_tree = (Object)adaptor.create(string_literal460);
					adaptor.addChild(root_0, string_literal460_tree);
					}

					pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3961);
					path_expression461=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression461.getTree());

					char_literal462=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3962); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal462_tree = (Object)adaptor.create(char_literal462);
					adaptor.addChild(root_0, char_literal462_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:432:7: 'INDEX(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal463=(Token)match(input,106,FOLLOW_106_in_functions_returning_numerics3970); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal463_tree = (Object)adaptor.create(string_literal463);
					adaptor.addChild(root_0, string_literal463_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_functions_returning_numerics3971);
					identification_variable464=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable464.getTree());

					char_literal465=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3972); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal465_tree = (Object)adaptor.create(char_literal465);
					adaptor.addChild(root_0, char_literal465_tree);
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
	// JPA2.g:433:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
	public final JPA2Parser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
		JPA2Parser.functions_returning_datetime_return retval = new JPA2Parser.functions_returning_datetime_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set466=null;

		Object set466_tree=null;

		try {
			// JPA2.g:434:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set466=input.LT(1);
			if ( (input.LA(1) >= 92 && input.LA(1) <= 94) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set466));
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
	// JPA2.g:437:1: functions_returning_strings : ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' );
	public final JPA2Parser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
		JPA2Parser.functions_returning_strings_return retval = new JPA2Parser.functions_returning_strings_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal467=null;
		Token char_literal469=null;
		Token char_literal471=null;
		Token char_literal473=null;
		Token string_literal474=null;
		Token char_literal476=null;
		Token char_literal478=null;
		Token char_literal480=null;
		Token string_literal481=null;
		Token string_literal484=null;
		Token char_literal486=null;
		Token string_literal487=null;
		Token char_literal488=null;
		Token char_literal490=null;
		Token string_literal491=null;
		Token char_literal493=null;
		ParserRuleReturnScope string_expression468 =null;
		ParserRuleReturnScope string_expression470 =null;
		ParserRuleReturnScope string_expression472 =null;
		ParserRuleReturnScope string_expression475 =null;
		ParserRuleReturnScope arithmetic_expression477 =null;
		ParserRuleReturnScope arithmetic_expression479 =null;
		ParserRuleReturnScope trim_specification482 =null;
		ParserRuleReturnScope trim_character483 =null;
		ParserRuleReturnScope string_expression485 =null;
		ParserRuleReturnScope string_expression489 =null;
		ParserRuleReturnScope string_expression492 =null;

		Object string_literal467_tree=null;
		Object char_literal469_tree=null;
		Object char_literal471_tree=null;
		Object char_literal473_tree=null;
		Object string_literal474_tree=null;
		Object char_literal476_tree=null;
		Object char_literal478_tree=null;
		Object char_literal480_tree=null;
		Object string_literal481_tree=null;
		Object string_literal484_tree=null;
		Object char_literal486_tree=null;
		Object string_literal487_tree=null;
		Object char_literal488_tree=null;
		Object char_literal490_tree=null;
		Object string_literal491_tree=null;
		Object char_literal493_tree=null;

		try {
			// JPA2.g:438:5: ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' )
			int alt122=5;
			switch ( input.LA(1) ) {
			case 91:
				{
				alt122=1;
				}
				break;
			case 133:
				{
				alt122=2;
				}
				break;
			case 136:
				{
				alt122=3;
				}
				break;
			case LOWER:
				{
				alt122=4;
				}
				break;
			case 139:
				{
				alt122=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 122, 0, input);
				throw nvae;
			}
			switch (alt122) {
				case 1 :
					// JPA2.g:438:7: 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal467=(Token)match(input,91,FOLLOW_91_in_functions_returning_strings4010); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal467_tree = (Object)adaptor.create(string_literal467);
					adaptor.addChild(root_0, string_literal467_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4011);
					string_expression468=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression468.getTree());

					char_literal469=(Token)match(input,66,FOLLOW_66_in_functions_returning_strings4012); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal469_tree = (Object)adaptor.create(char_literal469);
					adaptor.addChild(root_0, char_literal469_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4014);
					string_expression470=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression470.getTree());

					// JPA2.g:438:55: ( ',' string_expression )*
					loop117:
					while (true) {
						int alt117=2;
						int LA117_0 = input.LA(1);
						if ( (LA117_0==66) ) {
							alt117=1;
						}

						switch (alt117) {
						case 1 :
							// JPA2.g:438:56: ',' string_expression
							{
							char_literal471=(Token)match(input,66,FOLLOW_66_in_functions_returning_strings4017); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal471_tree = (Object)adaptor.create(char_literal471);
							adaptor.addChild(root_0, char_literal471_tree);
							}

							pushFollow(FOLLOW_string_expression_in_functions_returning_strings4019);
							string_expression472=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression472.getTree());

							}
							break;

						default :
							break loop117;
						}
					}

					char_literal473=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4022); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal473_tree = (Object)adaptor.create(char_literal473);
					adaptor.addChild(root_0, char_literal473_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:439:7: 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal474=(Token)match(input,133,FOLLOW_133_in_functions_returning_strings4030); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal474_tree = (Object)adaptor.create(string_literal474);
					adaptor.addChild(root_0, string_literal474_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4032);
					string_expression475=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression475.getTree());

					char_literal476=(Token)match(input,66,FOLLOW_66_in_functions_returning_strings4033); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal476_tree = (Object)adaptor.create(char_literal476);
					adaptor.addChild(root_0, char_literal476_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings4035);
					arithmetic_expression477=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression477.getTree());

					// JPA2.g:439:63: ( ',' arithmetic_expression )?
					int alt118=2;
					int LA118_0 = input.LA(1);
					if ( (LA118_0==66) ) {
						alt118=1;
					}
					switch (alt118) {
						case 1 :
							// JPA2.g:439:64: ',' arithmetic_expression
							{
							char_literal478=(Token)match(input,66,FOLLOW_66_in_functions_returning_strings4038); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal478_tree = (Object)adaptor.create(char_literal478);
							adaptor.addChild(root_0, char_literal478_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings4040);
							arithmetic_expression479=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression479.getTree());

							}
							break;

					}

					char_literal480=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4043); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal480_tree = (Object)adaptor.create(char_literal480);
					adaptor.addChild(root_0, char_literal480_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:440:7: 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal481=(Token)match(input,136,FOLLOW_136_in_functions_returning_strings4051); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal481_tree = (Object)adaptor.create(string_literal481);
					adaptor.addChild(root_0, string_literal481_tree);
					}

					// JPA2.g:440:14: ( ( trim_specification )? ( trim_character )? 'FROM' )?
					int alt121=2;
					int LA121_0 = input.LA(1);
					if ( (LA121_0==TRIM_CHARACTER||LA121_0==88||LA121_0==103||LA121_0==109||LA121_0==134) ) {
						alt121=1;
					}
					switch (alt121) {
						case 1 :
							// JPA2.g:440:15: ( trim_specification )? ( trim_character )? 'FROM'
							{
							// JPA2.g:440:15: ( trim_specification )?
							int alt119=2;
							int LA119_0 = input.LA(1);
							if ( (LA119_0==88||LA119_0==109||LA119_0==134) ) {
								alt119=1;
							}
							switch (alt119) {
								case 1 :
									// JPA2.g:440:16: trim_specification
									{
									pushFollow(FOLLOW_trim_specification_in_functions_returning_strings4054);
									trim_specification482=trim_specification();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification482.getTree());

									}
									break;

							}

							// JPA2.g:440:37: ( trim_character )?
							int alt120=2;
							int LA120_0 = input.LA(1);
							if ( (LA120_0==TRIM_CHARACTER) ) {
								alt120=1;
							}
							switch (alt120) {
								case 1 :
									// JPA2.g:440:38: trim_character
									{
									pushFollow(FOLLOW_trim_character_in_functions_returning_strings4059);
									trim_character483=trim_character();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_character483.getTree());

									}
									break;

							}

							string_literal484=(Token)match(input,103,FOLLOW_103_in_functions_returning_strings4063); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal484_tree = (Object)adaptor.create(string_literal484);
							adaptor.addChild(root_0, string_literal484_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4067);
					string_expression485=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression485.getTree());

					char_literal486=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4069); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal486_tree = (Object)adaptor.create(char_literal486);
					adaptor.addChild(root_0, char_literal486_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:441:7: 'LOWER' '(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal487=(Token)match(input,LOWER,FOLLOW_LOWER_in_functions_returning_strings4077); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal487_tree = (Object)adaptor.create(string_literal487);
					adaptor.addChild(root_0, string_literal487_tree);
					}

					char_literal488=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings4079); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal488_tree = (Object)adaptor.create(char_literal488);
					adaptor.addChild(root_0, char_literal488_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4080);
					string_expression489=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression489.getTree());

					char_literal490=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4081); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal490_tree = (Object)adaptor.create(char_literal490);
					adaptor.addChild(root_0, char_literal490_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:442:7: 'UPPER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal491=(Token)match(input,139,FOLLOW_139_in_functions_returning_strings4089); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal491_tree = (Object)adaptor.create(string_literal491);
					adaptor.addChild(root_0, string_literal491_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4090);
					string_expression492=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression492.getTree());

					char_literal493=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4091); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal493_tree = (Object)adaptor.create(char_literal493);
					adaptor.addChild(root_0, char_literal493_tree);
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
	// JPA2.g:443:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
	public final JPA2Parser.trim_specification_return trim_specification() throws RecognitionException {
		JPA2Parser.trim_specification_return retval = new JPA2Parser.trim_specification_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set494=null;

		Object set494_tree=null;

		try {
			// JPA2.g:444:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set494=input.LT(1);
			if ( input.LA(1)==88||input.LA(1)==109||input.LA(1)==134 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set494));
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
	// JPA2.g:445:1: function_invocation : 'FUNCTION(' function_name ( ',' function_arg )* ')' ;
	public final JPA2Parser.function_invocation_return function_invocation() throws RecognitionException {
		JPA2Parser.function_invocation_return retval = new JPA2Parser.function_invocation_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal495=null;
		Token char_literal497=null;
		Token char_literal499=null;
		ParserRuleReturnScope function_name496 =null;
		ParserRuleReturnScope function_arg498 =null;

		Object string_literal495_tree=null;
		Object char_literal497_tree=null;
		Object char_literal499_tree=null;

		try {
			// JPA2.g:446:5: ( 'FUNCTION(' function_name ( ',' function_arg )* ')' )
			// JPA2.g:446:7: 'FUNCTION(' function_name ( ',' function_arg )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal495=(Token)match(input,104,FOLLOW_104_in_function_invocation4121); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal495_tree = (Object)adaptor.create(string_literal495);
			adaptor.addChild(root_0, string_literal495_tree);
			}

			pushFollow(FOLLOW_function_name_in_function_invocation4122);
			function_name496=function_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_name496.getTree());

			// JPA2.g:446:32: ( ',' function_arg )*
			loop123:
			while (true) {
				int alt123=2;
				int LA123_0 = input.LA(1);
				if ( (LA123_0==66) ) {
					alt123=1;
				}

				switch (alt123) {
				case 1 :
					// JPA2.g:446:33: ',' function_arg
					{
					char_literal497=(Token)match(input,66,FOLLOW_66_in_function_invocation4125); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal497_tree = (Object)adaptor.create(char_literal497);
					adaptor.addChild(root_0, char_literal497_tree);
					}

					pushFollow(FOLLOW_function_arg_in_function_invocation4127);
					function_arg498=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg498.getTree());

					}
					break;

				default :
					break loop123;
				}
			}

			char_literal499=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function_invocation4131); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal499_tree = (Object)adaptor.create(char_literal499);
			adaptor.addChild(root_0, char_literal499_tree);
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
	// JPA2.g:447:1: function_arg : ( literal | path_expression | input_parameter | scalar_expression );
	public final JPA2Parser.function_arg_return function_arg() throws RecognitionException {
		JPA2Parser.function_arg_return retval = new JPA2Parser.function_arg_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal500 =null;
		ParserRuleReturnScope path_expression501 =null;
		ParserRuleReturnScope input_parameter502 =null;
		ParserRuleReturnScope scalar_expression503 =null;


		try {
			// JPA2.g:448:5: ( literal | path_expression | input_parameter | scalar_expression )
			int alt124=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA124_1 = input.LA(2);
				if ( (LA124_1==68) ) {
					alt124=2;
				}
				else if ( (synpred247_JPA2()) ) {
					alt124=1;
				}
				else if ( (true) ) {
					alt124=4;
				}

				}
				break;
			case GROUP:
				{
				int LA124_2 = input.LA(2);
				if ( (LA124_2==68) ) {
					int LA124_9 = input.LA(3);
					if ( (synpred248_JPA2()) ) {
						alt124=2;
					}
					else if ( (true) ) {
						alt124=4;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 124, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 77:
				{
				int LA124_3 = input.LA(2);
				if ( (LA124_3==70) ) {
					int LA124_10 = input.LA(3);
					if ( (LA124_10==INT_NUMERAL) ) {
						int LA124_14 = input.LA(4);
						if ( (synpred249_JPA2()) ) {
							alt124=3;
						}
						else if ( (true) ) {
							alt124=4;
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
								new NoViableAltException("", 124, 10, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA124_3==INT_NUMERAL) ) {
					int LA124_11 = input.LA(3);
					if ( (synpred249_JPA2()) ) {
						alt124=3;
					}
					else if ( (true) ) {
						alt124=4;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 124, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA124_4 = input.LA(2);
				if ( (synpred249_JPA2()) ) {
					alt124=3;
				}
				else if ( (true) ) {
					alt124=4;
				}

				}
				break;
			case 63:
				{
				int LA124_5 = input.LA(2);
				if ( (LA124_5==WORD) ) {
					int LA124_13 = input.LA(3);
					if ( (LA124_13==146) ) {
						int LA124_15 = input.LA(4);
						if ( (synpred249_JPA2()) ) {
							alt124=3;
						}
						else if ( (true) ) {
							alt124=4;
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
								new NoViableAltException("", 124, 13, input);
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
							new NoViableAltException("", 124, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case AVG:
			case CASE:
			case COUNT:
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case MAX:
			case MIN:
			case STRING_LITERAL:
			case SUM:
			case 65:
			case 67:
			case 70:
			case 82:
			case 84:
			case 89:
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 102:
			case 104:
			case 106:
			case 110:
			case 112:
			case 115:
			case 120:
			case 130:
			case 132:
			case 133:
			case 136:
			case 137:
			case 139:
			case 144:
			case 145:
				{
				alt124=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 124, 0, input);
				throw nvae;
			}
			switch (alt124) {
				case 1 :
					// JPA2.g:448:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_function_arg4142);
					literal500=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal500.getTree());

					}
					break;
				case 2 :
					// JPA2.g:449:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_function_arg4150);
					path_expression501=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression501.getTree());

					}
					break;
				case 3 :
					// JPA2.g:450:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_function_arg4158);
					input_parameter502=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter502.getTree());

					}
					break;
				case 4 :
					// JPA2.g:451:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_function_arg4166);
					scalar_expression503=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression503.getTree());

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
	// JPA2.g:452:1: case_expression : ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression );
	public final JPA2Parser.case_expression_return case_expression() throws RecognitionException {
		JPA2Parser.case_expression_return retval = new JPA2Parser.case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope general_case_expression504 =null;
		ParserRuleReturnScope simple_case_expression505 =null;
		ParserRuleReturnScope coalesce_expression506 =null;
		ParserRuleReturnScope nullif_expression507 =null;


		try {
			// JPA2.g:453:5: ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression )
			int alt125=4;
			switch ( input.LA(1) ) {
			case CASE:
				{
				int LA125_1 = input.LA(2);
				if ( (LA125_1==WHEN) ) {
					alt125=1;
				}
				else if ( (LA125_1==GROUP||LA125_1==WORD||LA125_1==137) ) {
					alt125=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 125, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 90:
				{
				alt125=3;
				}
				break;
			case 120:
				{
				alt125=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 125, 0, input);
				throw nvae;
			}
			switch (alt125) {
				case 1 :
					// JPA2.g:453:7: general_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_case_expression_in_case_expression4177);
					general_case_expression504=general_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_case_expression504.getTree());

					}
					break;
				case 2 :
					// JPA2.g:454:7: simple_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_case_expression_in_case_expression4185);
					simple_case_expression505=simple_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_case_expression505.getTree());

					}
					break;
				case 3 :
					// JPA2.g:455:7: coalesce_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_coalesce_expression_in_case_expression4193);
					coalesce_expression506=coalesce_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, coalesce_expression506.getTree());

					}
					break;
				case 4 :
					// JPA2.g:456:7: nullif_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_nullif_expression_in_case_expression4201);
					nullif_expression507=nullif_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, nullif_expression507.getTree());

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
	// JPA2.g:457:1: general_case_expression : CASE when_clause ( when_clause )* ELSE scalar_expression END ;
	public final JPA2Parser.general_case_expression_return general_case_expression() throws RecognitionException {
		JPA2Parser.general_case_expression_return retval = new JPA2Parser.general_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token CASE508=null;
		Token ELSE511=null;
		Token END513=null;
		ParserRuleReturnScope when_clause509 =null;
		ParserRuleReturnScope when_clause510 =null;
		ParserRuleReturnScope scalar_expression512 =null;

		Object CASE508_tree=null;
		Object ELSE511_tree=null;
		Object END513_tree=null;

		try {
			// JPA2.g:458:5: ( CASE when_clause ( when_clause )* ELSE scalar_expression END )
			// JPA2.g:458:7: CASE when_clause ( when_clause )* ELSE scalar_expression END
			{
			root_0 = (Object)adaptor.nil();


			CASE508=(Token)match(input,CASE,FOLLOW_CASE_in_general_case_expression4212); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			CASE508_tree = (Object)adaptor.create(CASE508);
			adaptor.addChild(root_0, CASE508_tree);
			}

			pushFollow(FOLLOW_when_clause_in_general_case_expression4214);
			when_clause509=when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause509.getTree());

			// JPA2.g:458:24: ( when_clause )*
			loop126:
			while (true) {
				int alt126=2;
				int LA126_0 = input.LA(1);
				if ( (LA126_0==WHEN) ) {
					alt126=1;
				}

				switch (alt126) {
				case 1 :
					// JPA2.g:458:25: when_clause
					{
					pushFollow(FOLLOW_when_clause_in_general_case_expression4217);
					when_clause510=when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause510.getTree());

					}
					break;

				default :
					break loop126;
				}
			}

			ELSE511=(Token)match(input,ELSE,FOLLOW_ELSE_in_general_case_expression4221); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ELSE511_tree = (Object)adaptor.create(ELSE511);
			adaptor.addChild(root_0, ELSE511_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_general_case_expression4223);
			scalar_expression512=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression512.getTree());

			END513=(Token)match(input,END,FOLLOW_END_in_general_case_expression4225); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			END513_tree = (Object)adaptor.create(END513);
			adaptor.addChild(root_0, END513_tree);
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
	// JPA2.g:459:1: when_clause : WHEN conditional_expression THEN scalar_expression ;
	public final JPA2Parser.when_clause_return when_clause() throws RecognitionException {
		JPA2Parser.when_clause_return retval = new JPA2Parser.when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHEN514=null;
		Token THEN516=null;
		ParserRuleReturnScope conditional_expression515 =null;
		ParserRuleReturnScope scalar_expression517 =null;

		Object WHEN514_tree=null;
		Object THEN516_tree=null;

		try {
			// JPA2.g:460:5: ( WHEN conditional_expression THEN scalar_expression )
			// JPA2.g:460:7: WHEN conditional_expression THEN scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			WHEN514=(Token)match(input,WHEN,FOLLOW_WHEN_in_when_clause4236); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WHEN514_tree = (Object)adaptor.create(WHEN514);
			adaptor.addChild(root_0, WHEN514_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_when_clause4238);
			conditional_expression515=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression515.getTree());

			THEN516=(Token)match(input,THEN,FOLLOW_THEN_in_when_clause4240); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			THEN516_tree = (Object)adaptor.create(THEN516);
			adaptor.addChild(root_0, THEN516_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_when_clause4242);
			scalar_expression517=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression517.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:461:1: simple_case_expression : CASE case_operand simple_when_clause ( simple_when_clause )* ELSE scalar_expression END ;
	public final JPA2Parser.simple_case_expression_return simple_case_expression() throws RecognitionException {
		JPA2Parser.simple_case_expression_return retval = new JPA2Parser.simple_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token CASE518=null;
		Token ELSE522=null;
		Token END524=null;
		ParserRuleReturnScope case_operand519 =null;
		ParserRuleReturnScope simple_when_clause520 =null;
		ParserRuleReturnScope simple_when_clause521 =null;
		ParserRuleReturnScope scalar_expression523 =null;

		Object CASE518_tree=null;
		Object ELSE522_tree=null;
		Object END524_tree=null;

		try {
			// JPA2.g:462:5: ( CASE case_operand simple_when_clause ( simple_when_clause )* ELSE scalar_expression END )
			// JPA2.g:462:7: CASE case_operand simple_when_clause ( simple_when_clause )* ELSE scalar_expression END
			{
			root_0 = (Object)adaptor.nil();


			CASE518=(Token)match(input,CASE,FOLLOW_CASE_in_simple_case_expression4253); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			CASE518_tree = (Object)adaptor.create(CASE518);
			adaptor.addChild(root_0, CASE518_tree);
			}

			pushFollow(FOLLOW_case_operand_in_simple_case_expression4255);
			case_operand519=case_operand();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, case_operand519.getTree());

			pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4257);
			simple_when_clause520=simple_when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause520.getTree());

			// JPA2.g:462:44: ( simple_when_clause )*
			loop127:
			while (true) {
				int alt127=2;
				int LA127_0 = input.LA(1);
				if ( (LA127_0==WHEN) ) {
					alt127=1;
				}

				switch (alt127) {
				case 1 :
					// JPA2.g:462:45: simple_when_clause
					{
					pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4260);
					simple_when_clause521=simple_when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause521.getTree());

					}
					break;

				default :
					break loop127;
				}
			}

			ELSE522=(Token)match(input,ELSE,FOLLOW_ELSE_in_simple_case_expression4264); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ELSE522_tree = (Object)adaptor.create(ELSE522);
			adaptor.addChild(root_0, ELSE522_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_case_expression4266);
			scalar_expression523=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression523.getTree());

			END524=(Token)match(input,END,FOLLOW_END_in_simple_case_expression4268); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			END524_tree = (Object)adaptor.create(END524);
			adaptor.addChild(root_0, END524_tree);
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
	// JPA2.g:463:1: case_operand : ( path_expression | type_discriminator );
	public final JPA2Parser.case_operand_return case_operand() throws RecognitionException {
		JPA2Parser.case_operand_return retval = new JPA2Parser.case_operand_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression525 =null;
		ParserRuleReturnScope type_discriminator526 =null;


		try {
			// JPA2.g:464:5: ( path_expression | type_discriminator )
			int alt128=2;
			int LA128_0 = input.LA(1);
			if ( (LA128_0==GROUP||LA128_0==WORD) ) {
				alt128=1;
			}
			else if ( (LA128_0==137) ) {
				alt128=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 128, 0, input);
				throw nvae;
			}

			switch (alt128) {
				case 1 :
					// JPA2.g:464:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_case_operand4279);
					path_expression525=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression525.getTree());

					}
					break;
				case 2 :
					// JPA2.g:465:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_case_operand4287);
					type_discriminator526=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator526.getTree());

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
	// JPA2.g:466:1: simple_when_clause : WHEN scalar_expression THEN scalar_expression ;
	public final JPA2Parser.simple_when_clause_return simple_when_clause() throws RecognitionException {
		JPA2Parser.simple_when_clause_return retval = new JPA2Parser.simple_when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHEN527=null;
		Token THEN529=null;
		ParserRuleReturnScope scalar_expression528 =null;
		ParserRuleReturnScope scalar_expression530 =null;

		Object WHEN527_tree=null;
		Object THEN529_tree=null;

		try {
			// JPA2.g:467:5: ( WHEN scalar_expression THEN scalar_expression )
			// JPA2.g:467:7: WHEN scalar_expression THEN scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			WHEN527=(Token)match(input,WHEN,FOLLOW_WHEN_in_simple_when_clause4298); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WHEN527_tree = (Object)adaptor.create(WHEN527);
			adaptor.addChild(root_0, WHEN527_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4300);
			scalar_expression528=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression528.getTree());

			THEN529=(Token)match(input,THEN,FOLLOW_THEN_in_simple_when_clause4302); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			THEN529_tree = (Object)adaptor.create(THEN529);
			adaptor.addChild(root_0, THEN529_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4304);
			scalar_expression530=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression530.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:468:1: coalesce_expression : 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' ;
	public final JPA2Parser.coalesce_expression_return coalesce_expression() throws RecognitionException {
		JPA2Parser.coalesce_expression_return retval = new JPA2Parser.coalesce_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal531=null;
		Token char_literal533=null;
		Token char_literal535=null;
		ParserRuleReturnScope scalar_expression532 =null;
		ParserRuleReturnScope scalar_expression534 =null;

		Object string_literal531_tree=null;
		Object char_literal533_tree=null;
		Object char_literal535_tree=null;

		try {
			// JPA2.g:469:5: ( 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' )
			// JPA2.g:469:7: 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal531=(Token)match(input,90,FOLLOW_90_in_coalesce_expression4315); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal531_tree = (Object)adaptor.create(string_literal531);
			adaptor.addChild(root_0, string_literal531_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4316);
			scalar_expression532=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression532.getTree());

			// JPA2.g:469:36: ( ',' scalar_expression )+
			int cnt129=0;
			loop129:
			while (true) {
				int alt129=2;
				int LA129_0 = input.LA(1);
				if ( (LA129_0==66) ) {
					alt129=1;
				}

				switch (alt129) {
				case 1 :
					// JPA2.g:469:37: ',' scalar_expression
					{
					char_literal533=(Token)match(input,66,FOLLOW_66_in_coalesce_expression4319); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal533_tree = (Object)adaptor.create(char_literal533);
					adaptor.addChild(root_0, char_literal533_tree);
					}

					pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4321);
					scalar_expression534=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression534.getTree());

					}
					break;

				default :
					if ( cnt129 >= 1 ) break loop129;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(129, input);
					throw eee;
				}
				cnt129++;
			}

			char_literal535=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_coalesce_expression4324); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal535_tree = (Object)adaptor.create(char_literal535);
			adaptor.addChild(root_0, char_literal535_tree);
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
	// JPA2.g:470:1: nullif_expression : 'NULLIF(' scalar_expression ',' scalar_expression ')' ;
	public final JPA2Parser.nullif_expression_return nullif_expression() throws RecognitionException {
		JPA2Parser.nullif_expression_return retval = new JPA2Parser.nullif_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal536=null;
		Token char_literal538=null;
		Token char_literal540=null;
		ParserRuleReturnScope scalar_expression537 =null;
		ParserRuleReturnScope scalar_expression539 =null;

		Object string_literal536_tree=null;
		Object char_literal538_tree=null;
		Object char_literal540_tree=null;

		try {
			// JPA2.g:471:5: ( 'NULLIF(' scalar_expression ',' scalar_expression ')' )
			// JPA2.g:471:7: 'NULLIF(' scalar_expression ',' scalar_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal536=(Token)match(input,120,FOLLOW_120_in_nullif_expression4335); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal536_tree = (Object)adaptor.create(string_literal536);
			adaptor.addChild(root_0, string_literal536_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4336);
			scalar_expression537=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression537.getTree());

			char_literal538=(Token)match(input,66,FOLLOW_66_in_nullif_expression4338); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal538_tree = (Object)adaptor.create(char_literal538);
			adaptor.addChild(root_0, char_literal538_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4340);
			scalar_expression539=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression539.getTree());

			char_literal540=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nullif_expression4341); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal540_tree = (Object)adaptor.create(char_literal540);
			adaptor.addChild(root_0, char_literal540_tree);
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
	// JPA2.g:473:1: extension_functions : ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | extract_function | enum_function );
	public final JPA2Parser.extension_functions_return extension_functions() throws RecognitionException {
		JPA2Parser.extension_functions_return retval = new JPA2Parser.extension_functions_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal541=null;
		Token WORD543=null;
		Token char_literal544=null;
		Token INT_NUMERAL545=null;
		Token char_literal546=null;
		Token INT_NUMERAL547=null;
		Token char_literal548=null;
		Token char_literal549=null;
		ParserRuleReturnScope function_arg542 =null;
		ParserRuleReturnScope extract_function550 =null;
		ParserRuleReturnScope enum_function551 =null;

		Object string_literal541_tree=null;
		Object WORD543_tree=null;
		Object char_literal544_tree=null;
		Object INT_NUMERAL545_tree=null;
		Object char_literal546_tree=null;
		Object INT_NUMERAL547_tree=null;
		Object char_literal548_tree=null;
		Object char_literal549_tree=null;

		try {
			// JPA2.g:474:5: ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | extract_function | enum_function )
			int alt132=3;
			switch ( input.LA(1) ) {
			case 89:
				{
				alt132=1;
				}
				break;
			case 102:
				{
				alt132=2;
				}
				break;
			case 82:
				{
				alt132=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 132, 0, input);
				throw nvae;
			}
			switch (alt132) {
				case 1 :
					// JPA2.g:474:7: 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal541=(Token)match(input,89,FOLLOW_89_in_extension_functions4353); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal541_tree = (Object)adaptor.create(string_literal541);
					adaptor.addChild(root_0, string_literal541_tree);
					}

					pushFollow(FOLLOW_function_arg_in_extension_functions4355);
					function_arg542=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg542.getTree());

					WORD543=(Token)match(input,WORD,FOLLOW_WORD_in_extension_functions4357); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD543_tree = (Object)adaptor.create(WORD543);
					adaptor.addChild(root_0, WORD543_tree);
					}

					// JPA2.g:474:33: ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )*
					loop131:
					while (true) {
						int alt131=2;
						int LA131_0 = input.LA(1);
						if ( (LA131_0==LPAREN) ) {
							alt131=1;
						}

						switch (alt131) {
						case 1 :
							// JPA2.g:474:34: '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')'
							{
							char_literal544=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_extension_functions4360); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal544_tree = (Object)adaptor.create(char_literal544);
							adaptor.addChild(root_0, char_literal544_tree);
							}

							INT_NUMERAL545=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4361); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							INT_NUMERAL545_tree = (Object)adaptor.create(INT_NUMERAL545);
							adaptor.addChild(root_0, INT_NUMERAL545_tree);
							}

							// JPA2.g:474:49: ( ',' INT_NUMERAL )*
							loop130:
							while (true) {
								int alt130=2;
								int LA130_0 = input.LA(1);
								if ( (LA130_0==66) ) {
									alt130=1;
								}

								switch (alt130) {
								case 1 :
									// JPA2.g:474:50: ',' INT_NUMERAL
									{
									char_literal546=(Token)match(input,66,FOLLOW_66_in_extension_functions4364); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									char_literal546_tree = (Object)adaptor.create(char_literal546);
									adaptor.addChild(root_0, char_literal546_tree);
									}

									INT_NUMERAL547=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4366); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									INT_NUMERAL547_tree = (Object)adaptor.create(INT_NUMERAL547);
									adaptor.addChild(root_0, INT_NUMERAL547_tree);
									}

									}
									break;

								default :
									break loop130;
								}
							}

							char_literal548=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4371); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal548_tree = (Object)adaptor.create(char_literal548);
							adaptor.addChild(root_0, char_literal548_tree);
							}

							}
							break;

						default :
							break loop131;
						}
					}

					char_literal549=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4375); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal549_tree = (Object)adaptor.create(char_literal549);
					adaptor.addChild(root_0, char_literal549_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:475:7: extract_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extract_function_in_extension_functions4383);
					extract_function550=extract_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extract_function550.getTree());

					}
					break;
				case 3 :
					// JPA2.g:476:7: enum_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_function_in_extension_functions4391);
					enum_function551=enum_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_function551.getTree());

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


	public static class extract_function_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "extract_function"
	// JPA2.g:478:1: extract_function : 'EXTRACT(' date_part 'FROM' function_arg ')' ;
	public final JPA2Parser.extract_function_return extract_function() throws RecognitionException {
		JPA2Parser.extract_function_return retval = new JPA2Parser.extract_function_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal552=null;
		Token string_literal554=null;
		Token char_literal556=null;
		ParserRuleReturnScope date_part553 =null;
		ParserRuleReturnScope function_arg555 =null;

		Object string_literal552_tree=null;
		Object string_literal554_tree=null;
		Object char_literal556_tree=null;

		try {
			// JPA2.g:479:5: ( 'EXTRACT(' date_part 'FROM' function_arg ')' )
			// JPA2.g:479:7: 'EXTRACT(' date_part 'FROM' function_arg ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal552=(Token)match(input,102,FOLLOW_102_in_extract_function4403); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal552_tree = (Object)adaptor.create(string_literal552);
			adaptor.addChild(root_0, string_literal552_tree);
			}

			pushFollow(FOLLOW_date_part_in_extract_function4405);
			date_part553=date_part();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part553.getTree());

			string_literal554=(Token)match(input,103,FOLLOW_103_in_extract_function4407); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal554_tree = (Object)adaptor.create(string_literal554);
			adaptor.addChild(root_0, string_literal554_tree);
			}

			pushFollow(FOLLOW_function_arg_in_extract_function4409);
			function_arg555=function_arg();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg555.getTree());

			char_literal556=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extract_function4411); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal556_tree = (Object)adaptor.create(char_literal556);
			adaptor.addChild(root_0, char_literal556_tree);
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
	// $ANTLR end "extract_function"


	public static class enum_function_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enum_function"
	// JPA2.g:481:1: enum_function : '@ENUM' '(' enum_value_literal ')' -> ^( T_ENUM_MACROS[$enum_value_literal.text] ) ;
	public final JPA2Parser.enum_function_return enum_function() throws RecognitionException {
		JPA2Parser.enum_function_return retval = new JPA2Parser.enum_function_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal557=null;
		Token char_literal558=null;
		Token char_literal560=null;
		ParserRuleReturnScope enum_value_literal559 =null;

		Object string_literal557_tree=null;
		Object char_literal558_tree=null;
		Object char_literal560_tree=null;
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_82=new RewriteRuleTokenStream(adaptor,"token 82");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_enum_value_literal=new RewriteRuleSubtreeStream(adaptor,"rule enum_value_literal");

		try {
			// JPA2.g:482:5: ( '@ENUM' '(' enum_value_literal ')' -> ^( T_ENUM_MACROS[$enum_value_literal.text] ) )
			// JPA2.g:482:7: '@ENUM' '(' enum_value_literal ')'
			{
			string_literal557=(Token)match(input,82,FOLLOW_82_in_enum_function4423); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_82.add(string_literal557);

			char_literal558=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_enum_function4425); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal558);

			pushFollow(FOLLOW_enum_value_literal_in_enum_function4427);
			enum_value_literal559=enum_value_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_enum_value_literal.add(enum_value_literal559.getTree());
			char_literal560=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_enum_function4429); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal560);

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
			// 482:42: -> ^( T_ENUM_MACROS[$enum_value_literal.text] )
			{
				// JPA2.g:482:45: ^( T_ENUM_MACROS[$enum_value_literal.text] )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new EnumConditionNode(T_ENUM_MACROS, (enum_value_literal559!=null?input.toString(enum_value_literal559.start,enum_value_literal559.stop):null)), root_1);
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
	// $ANTLR end "enum_function"


	public static class date_part_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_part"
	// JPA2.g:484:10: fragment date_part : ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' );
	public final JPA2Parser.date_part_return date_part() throws RecognitionException {
		JPA2Parser.date_part_return retval = new JPA2Parser.date_part_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set561=null;

		Object set561_tree=null;

		try {
			// JPA2.g:485:5: ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set561=input.LT(1);
			if ( input.LA(1)==95||input.LA(1)==99||input.LA(1)==105||input.LA(1)==114||input.LA(1)==116||input.LA(1)==126||input.LA(1)==128||input.LA(1)==141||input.LA(1)==143 ) {
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
	// $ANTLR end "date_part"


	public static class input_parameter_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "input_parameter"
	// JPA2.g:488:1: input_parameter : ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) );
	public final JPA2Parser.input_parameter_return input_parameter() throws RecognitionException {
		JPA2Parser.input_parameter_return retval = new JPA2Parser.input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal562=null;
		Token NAMED_PARAMETER564=null;
		Token string_literal565=null;
		Token WORD566=null;
		Token char_literal567=null;
		ParserRuleReturnScope numeric_literal563 =null;

		Object char_literal562_tree=null;
		Object NAMED_PARAMETER564_tree=null;
		Object string_literal565_tree=null;
		Object WORD566_tree=null;
		Object char_literal567_tree=null;
		RewriteRuleTokenStream stream_77=new RewriteRuleTokenStream(adaptor,"token 77");
		RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
		RewriteRuleTokenStream stream_146=new RewriteRuleTokenStream(adaptor,"token 146");
		RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
		RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
		RewriteRuleSubtreeStream stream_numeric_literal=new RewriteRuleSubtreeStream(adaptor,"rule numeric_literal");

		try {
			// JPA2.g:489:5: ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
			int alt133=3;
			switch ( input.LA(1) ) {
			case 77:
				{
				alt133=1;
				}
				break;
			case NAMED_PARAMETER:
				{
				alt133=2;
				}
				break;
			case 63:
				{
				alt133=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 133, 0, input);
				throw nvae;
			}
			switch (alt133) {
				case 1 :
					// JPA2.g:489:7: '?' numeric_literal
					{
					char_literal562=(Token)match(input,77,FOLLOW_77_in_input_parameter4496); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_77.add(char_literal562);

					pushFollow(FOLLOW_numeric_literal_in_input_parameter4498);
					numeric_literal563=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_literal.add(numeric_literal563.getTree());
					// AST REWRITE
					// elements: numeric_literal, 77
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 489:27: -> ^( T_PARAMETER[] '?' numeric_literal )
					{
						// JPA2.g:489:30: ^( T_PARAMETER[] '?' numeric_literal )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_77.nextNode());
						adaptor.addChild(root_1, stream_numeric_literal.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// JPA2.g:490:7: NAMED_PARAMETER
					{
					NAMED_PARAMETER564=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter4521); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER564);

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
					// 490:23: -> ^( T_PARAMETER[] NAMED_PARAMETER )
					{
						// JPA2.g:490:26: ^( T_PARAMETER[] NAMED_PARAMETER )
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
					// JPA2.g:491:7: '${' WORD '}'
					{
					string_literal565=(Token)match(input,63,FOLLOW_63_in_input_parameter4542); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_63.add(string_literal565);

					WORD566=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter4544); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WORD.add(WORD566);

					char_literal567=(Token)match(input,146,FOLLOW_146_in_input_parameter4546); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_146.add(char_literal567);

					// AST REWRITE
					// elements: WORD, 63, 146
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 491:21: -> ^( T_PARAMETER[] '${' WORD '}' )
					{
						// JPA2.g:491:24: ^( T_PARAMETER[] '${' WORD '}' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_63.nextNode());
						adaptor.addChild(root_1, stream_WORD.nextNode());
						adaptor.addChild(root_1, stream_146.nextNode());
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
	// JPA2.g:493:1: literal : WORD ;
	public final JPA2Parser.literal_return literal() throws RecognitionException {
		JPA2Parser.literal_return retval = new JPA2Parser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD568=null;

		Object WORD568_tree=null;

		try {
			// JPA2.g:494:5: ( WORD )
			// JPA2.g:494:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD568=(Token)match(input,WORD,FOLLOW_WORD_in_literal4574); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD568_tree = (Object)adaptor.create(WORD568);
			adaptor.addChild(root_0, WORD568_tree);
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
	// JPA2.g:496:1: constructor_name : WORD ;
	public final JPA2Parser.constructor_name_return constructor_name() throws RecognitionException {
		JPA2Parser.constructor_name_return retval = new JPA2Parser.constructor_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD569=null;

		Object WORD569_tree=null;

		try {
			// JPA2.g:497:5: ( WORD )
			// JPA2.g:497:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD569=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4586); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD569_tree = (Object)adaptor.create(WORD569);
			adaptor.addChild(root_0, WORD569_tree);
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
	// JPA2.g:499:1: enum_literal : WORD ;
	public final JPA2Parser.enum_literal_return enum_literal() throws RecognitionException {
		JPA2Parser.enum_literal_return retval = new JPA2Parser.enum_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD570=null;

		Object WORD570_tree=null;

		try {
			// JPA2.g:500:5: ( WORD )
			// JPA2.g:500:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD570=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal4598); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD570_tree = (Object)adaptor.create(WORD570);
			adaptor.addChild(root_0, WORD570_tree);
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
	// JPA2.g:502:1: boolean_literal : ( 'true' | 'false' );
	public final JPA2Parser.boolean_literal_return boolean_literal() throws RecognitionException {
		JPA2Parser.boolean_literal_return retval = new JPA2Parser.boolean_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set571=null;

		Object set571_tree=null;

		try {
			// JPA2.g:503:5: ( 'true' | 'false' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set571=input.LT(1);
			if ( (input.LA(1) >= 144 && input.LA(1) <= 145) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set571));
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
	// JPA2.g:507:1: field : ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | 'AS' | 'MEMBER' | 'CASE' | date_part );
	public final JPA2Parser.field_return field() throws RecognitionException {
		JPA2Parser.field_return retval = new JPA2Parser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD572=null;
		Token string_literal573=null;
		Token string_literal574=null;
		Token string_literal575=null;
		Token string_literal576=null;
		Token string_literal577=null;
		Token string_literal578=null;
		Token string_literal579=null;
		Token string_literal580=null;
		Token string_literal581=null;
		Token string_literal582=null;
		Token string_literal583=null;
		Token string_literal584=null;
		ParserRuleReturnScope date_part585 =null;

		Object WORD572_tree=null;
		Object string_literal573_tree=null;
		Object string_literal574_tree=null;
		Object string_literal575_tree=null;
		Object string_literal576_tree=null;
		Object string_literal577_tree=null;
		Object string_literal578_tree=null;
		Object string_literal579_tree=null;
		Object string_literal580_tree=null;
		Object string_literal581_tree=null;
		Object string_literal582_tree=null;
		Object string_literal583_tree=null;
		Object string_literal584_tree=null;

		try {
			// JPA2.g:508:5: ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | 'AS' | 'MEMBER' | 'CASE' | date_part )
			int alt134=14;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt134=1;
				}
				break;
			case 129:
				{
				alt134=2;
				}
				break;
			case 103:
				{
				alt134=3;
				}
				break;
			case GROUP:
				{
				alt134=4;
				}
				break;
			case ORDER:
				{
				alt134=5;
				}
				break;
			case MAX:
				{
				alt134=6;
				}
				break;
			case MIN:
				{
				alt134=7;
				}
				break;
			case SUM:
				{
				alt134=8;
				}
				break;
			case AVG:
				{
				alt134=9;
				}
				break;
			case COUNT:
				{
				alt134=10;
				}
				break;
			case AS:
				{
				alt134=11;
				}
				break;
			case 113:
				{
				alt134=12;
				}
				break;
			case CASE:
				{
				alt134=13;
				}
				break;
			case 95:
			case 99:
			case 105:
			case 114:
			case 116:
			case 126:
			case 128:
			case 141:
			case 143:
				{
				alt134=14;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 134, 0, input);
				throw nvae;
			}
			switch (alt134) {
				case 1 :
					// JPA2.g:508:7: WORD
					{
					root_0 = (Object)adaptor.nil();


					WORD572=(Token)match(input,WORD,FOLLOW_WORD_in_field4631); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD572_tree = (Object)adaptor.create(WORD572);
					adaptor.addChild(root_0, WORD572_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:508:14: 'SELECT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal573=(Token)match(input,129,FOLLOW_129_in_field4635); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal573_tree = (Object)adaptor.create(string_literal573);
					adaptor.addChild(root_0, string_literal573_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:508:25: 'FROM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal574=(Token)match(input,103,FOLLOW_103_in_field4639); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal574_tree = (Object)adaptor.create(string_literal574);
					adaptor.addChild(root_0, string_literal574_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:508:34: 'GROUP'
					{
					root_0 = (Object)adaptor.nil();


					string_literal575=(Token)match(input,GROUP,FOLLOW_GROUP_in_field4643); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal575_tree = (Object)adaptor.create(string_literal575);
					adaptor.addChild(root_0, string_literal575_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:508:44: 'ORDER'
					{
					root_0 = (Object)adaptor.nil();


					string_literal576=(Token)match(input,ORDER,FOLLOW_ORDER_in_field4647); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal576_tree = (Object)adaptor.create(string_literal576);
					adaptor.addChild(root_0, string_literal576_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:508:54: 'MAX'
					{
					root_0 = (Object)adaptor.nil();


					string_literal577=(Token)match(input,MAX,FOLLOW_MAX_in_field4651); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal577_tree = (Object)adaptor.create(string_literal577);
					adaptor.addChild(root_0, string_literal577_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:508:62: 'MIN'
					{
					root_0 = (Object)adaptor.nil();


					string_literal578=(Token)match(input,MIN,FOLLOW_MIN_in_field4655); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal578_tree = (Object)adaptor.create(string_literal578);
					adaptor.addChild(root_0, string_literal578_tree);
					}

					}
					break;
				case 8 :
					// JPA2.g:508:70: 'SUM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal579=(Token)match(input,SUM,FOLLOW_SUM_in_field4659); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal579_tree = (Object)adaptor.create(string_literal579);
					adaptor.addChild(root_0, string_literal579_tree);
					}

					}
					break;
				case 9 :
					// JPA2.g:508:78: 'AVG'
					{
					root_0 = (Object)adaptor.nil();


					string_literal580=(Token)match(input,AVG,FOLLOW_AVG_in_field4663); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal580_tree = (Object)adaptor.create(string_literal580);
					adaptor.addChild(root_0, string_literal580_tree);
					}

					}
					break;
				case 10 :
					// JPA2.g:508:86: 'COUNT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal581=(Token)match(input,COUNT,FOLLOW_COUNT_in_field4667); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal581_tree = (Object)adaptor.create(string_literal581);
					adaptor.addChild(root_0, string_literal581_tree);
					}

					}
					break;
				case 11 :
					// JPA2.g:508:96: 'AS'
					{
					root_0 = (Object)adaptor.nil();


					string_literal582=(Token)match(input,AS,FOLLOW_AS_in_field4671); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal582_tree = (Object)adaptor.create(string_literal582);
					adaptor.addChild(root_0, string_literal582_tree);
					}

					}
					break;
				case 12 :
					// JPA2.g:508:103: 'MEMBER'
					{
					root_0 = (Object)adaptor.nil();


					string_literal583=(Token)match(input,113,FOLLOW_113_in_field4675); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal583_tree = (Object)adaptor.create(string_literal583);
					adaptor.addChild(root_0, string_literal583_tree);
					}

					}
					break;
				case 13 :
					// JPA2.g:508:114: 'CASE'
					{
					root_0 = (Object)adaptor.nil();


					string_literal584=(Token)match(input,CASE,FOLLOW_CASE_in_field4679); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal584_tree = (Object)adaptor.create(string_literal584);
					adaptor.addChild(root_0, string_literal584_tree);
					}

					}
					break;
				case 14 :
					// JPA2.g:508:123: date_part
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_part_in_field4683);
					date_part585=date_part();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part585.getTree());

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
	// JPA2.g:510:1: identification_variable : ( WORD | 'GROUP' );
	public final JPA2Parser.identification_variable_return identification_variable() throws RecognitionException {
		JPA2Parser.identification_variable_return retval = new JPA2Parser.identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set586=null;

		Object set586_tree=null;

		try {
			// JPA2.g:511:5: ( WORD | 'GROUP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set586=input.LT(1);
			if ( input.LA(1)==GROUP||input.LA(1)==WORD ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set586));
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
	// $ANTLR end "identification_variable"


	public static class parameter_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "parameter_name"
	// JPA2.g:513:1: parameter_name : WORD ( '.' WORD )* ;
	public final JPA2Parser.parameter_name_return parameter_name() throws RecognitionException {
		JPA2Parser.parameter_name_return retval = new JPA2Parser.parameter_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD587=null;
		Token char_literal588=null;
		Token WORD589=null;

		Object WORD587_tree=null;
		Object char_literal588_tree=null;
		Object WORD589_tree=null;

		try {
			// JPA2.g:514:5: ( WORD ( '.' WORD )* )
			// JPA2.g:514:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD587=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4711); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD587_tree = (Object)adaptor.create(WORD587);
			adaptor.addChild(root_0, WORD587_tree);
			}

			// JPA2.g:514:12: ( '.' WORD )*
			loop135:
			while (true) {
				int alt135=2;
				int LA135_0 = input.LA(1);
				if ( (LA135_0==68) ) {
					alt135=1;
				}

				switch (alt135) {
				case 1 :
					// JPA2.g:514:13: '.' WORD
					{
					char_literal588=(Token)match(input,68,FOLLOW_68_in_parameter_name4714); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal588_tree = (Object)adaptor.create(char_literal588);
					adaptor.addChild(root_0, char_literal588_tree);
					}

					WORD589=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4717); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD589_tree = (Object)adaptor.create(WORD589);
					adaptor.addChild(root_0, WORD589_tree);
					}

					}
					break;

				default :
					break loop135;
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
	// JPA2.g:517:1: escape_character : ( '\\'.\\'' | STRING_LITERAL );
	public final JPA2Parser.escape_character_return escape_character() throws RecognitionException {
		JPA2Parser.escape_character_return retval = new JPA2Parser.escape_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set590=null;

		Object set590_tree=null;

		try {
			// JPA2.g:518:5: ( '\\'.\\'' | STRING_LITERAL )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set590=input.LT(1);
			if ( input.LA(1)==STRING_LITERAL||input.LA(1)==TRIM_CHARACTER ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set590));
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
	// JPA2.g:519:1: trim_character : TRIM_CHARACTER ;
	public final JPA2Parser.trim_character_return trim_character() throws RecognitionException {
		JPA2Parser.trim_character_return retval = new JPA2Parser.trim_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRIM_CHARACTER591=null;

		Object TRIM_CHARACTER591_tree=null;

		try {
			// JPA2.g:520:5: ( TRIM_CHARACTER )
			// JPA2.g:520:7: TRIM_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			TRIM_CHARACTER591=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_trim_character4747); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TRIM_CHARACTER591_tree = (Object)adaptor.create(TRIM_CHARACTER591);
			adaptor.addChild(root_0, TRIM_CHARACTER591_tree);
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
	// JPA2.g:521:1: string_literal : STRING_LITERAL ;
	public final JPA2Parser.string_literal_return string_literal() throws RecognitionException {
		JPA2Parser.string_literal_return retval = new JPA2Parser.string_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL592=null;

		Object STRING_LITERAL592_tree=null;

		try {
			// JPA2.g:522:5: ( STRING_LITERAL )
			// JPA2.g:522:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL592=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_literal4758); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL592_tree = (Object)adaptor.create(STRING_LITERAL592);
			adaptor.addChild(root_0, STRING_LITERAL592_tree);
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
	// JPA2.g:523:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
	public final JPA2Parser.numeric_literal_return numeric_literal() throws RecognitionException {
		JPA2Parser.numeric_literal_return retval = new JPA2Parser.numeric_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal593=null;
		Token INT_NUMERAL594=null;

		Object string_literal593_tree=null;
		Object INT_NUMERAL594_tree=null;

		try {
			// JPA2.g:524:5: ( ( '0x' )? INT_NUMERAL )
			// JPA2.g:524:7: ( '0x' )? INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:524:7: ( '0x' )?
			int alt136=2;
			int LA136_0 = input.LA(1);
			if ( (LA136_0==70) ) {
				alt136=1;
			}
			switch (alt136) {
				case 1 :
					// JPA2.g:524:8: '0x'
					{
					string_literal593=(Token)match(input,70,FOLLOW_70_in_numeric_literal4770); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal593_tree = (Object)adaptor.create(string_literal593);
					adaptor.addChild(root_0, string_literal593_tree);
					}

					}
					break;

			}

			INT_NUMERAL594=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal4774); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL594_tree = (Object)adaptor.create(INT_NUMERAL594);
			adaptor.addChild(root_0, INT_NUMERAL594_tree);
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


	public static class decimal_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "decimal_literal"
	// JPA2.g:525:1: decimal_literal : INT_NUMERAL '.' INT_NUMERAL ;
	public final JPA2Parser.decimal_literal_return decimal_literal() throws RecognitionException {
		JPA2Parser.decimal_literal_return retval = new JPA2Parser.decimal_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token INT_NUMERAL595=null;
		Token char_literal596=null;
		Token INT_NUMERAL597=null;

		Object INT_NUMERAL595_tree=null;
		Object char_literal596_tree=null;
		Object INT_NUMERAL597_tree=null;

		try {
			// JPA2.g:526:5: ( INT_NUMERAL '.' INT_NUMERAL )
			// JPA2.g:526:7: INT_NUMERAL '.' INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			INT_NUMERAL595=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_decimal_literal4786); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL595_tree = (Object)adaptor.create(INT_NUMERAL595);
			adaptor.addChild(root_0, INT_NUMERAL595_tree);
			}

			char_literal596=(Token)match(input,68,FOLLOW_68_in_decimal_literal4788); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal596_tree = (Object)adaptor.create(char_literal596);
			adaptor.addChild(root_0, char_literal596_tree);
			}

			INT_NUMERAL597=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_decimal_literal4790); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL597_tree = (Object)adaptor.create(INT_NUMERAL597);
			adaptor.addChild(root_0, INT_NUMERAL597_tree);
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
	// $ANTLR end "decimal_literal"


	public static class single_valued_object_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "single_valued_object_field"
	// JPA2.g:527:1: single_valued_object_field : WORD ;
	public final JPA2Parser.single_valued_object_field_return single_valued_object_field() throws RecognitionException {
		JPA2Parser.single_valued_object_field_return retval = new JPA2Parser.single_valued_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD598=null;

		Object WORD598_tree=null;

		try {
			// JPA2.g:528:5: ( WORD )
			// JPA2.g:528:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD598=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_object_field4801); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD598_tree = (Object)adaptor.create(WORD598);
			adaptor.addChild(root_0, WORD598_tree);
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
	// JPA2.g:529:1: single_valued_embeddable_object_field : WORD ;
	public final JPA2Parser.single_valued_embeddable_object_field_return single_valued_embeddable_object_field() throws RecognitionException {
		JPA2Parser.single_valued_embeddable_object_field_return retval = new JPA2Parser.single_valued_embeddable_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD599=null;

		Object WORD599_tree=null;

		try {
			// JPA2.g:530:5: ( WORD )
			// JPA2.g:530:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD599=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_embeddable_object_field4812); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD599_tree = (Object)adaptor.create(WORD599);
			adaptor.addChild(root_0, WORD599_tree);
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
	// JPA2.g:531:1: collection_valued_field : WORD ;
	public final JPA2Parser.collection_valued_field_return collection_valued_field() throws RecognitionException {
		JPA2Parser.collection_valued_field_return retval = new JPA2Parser.collection_valued_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD600=null;

		Object WORD600_tree=null;

		try {
			// JPA2.g:532:5: ( WORD )
			// JPA2.g:532:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD600=(Token)match(input,WORD,FOLLOW_WORD_in_collection_valued_field4823); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD600_tree = (Object)adaptor.create(WORD600);
			adaptor.addChild(root_0, WORD600_tree);
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
	// JPA2.g:533:1: entity_name : WORD ;
	public final JPA2Parser.entity_name_return entity_name() throws RecognitionException {
		JPA2Parser.entity_name_return retval = new JPA2Parser.entity_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD601=null;

		Object WORD601_tree=null;

		try {
			// JPA2.g:534:5: ( WORD )
			// JPA2.g:534:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD601=(Token)match(input,WORD,FOLLOW_WORD_in_entity_name4834); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD601_tree = (Object)adaptor.create(WORD601);
			adaptor.addChild(root_0, WORD601_tree);
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
	// JPA2.g:535:1: subtype : WORD ;
	public final JPA2Parser.subtype_return subtype() throws RecognitionException {
		JPA2Parser.subtype_return retval = new JPA2Parser.subtype_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD602=null;

		Object WORD602_tree=null;

		try {
			// JPA2.g:536:5: ( WORD )
			// JPA2.g:536:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD602=(Token)match(input,WORD,FOLLOW_WORD_in_subtype4845); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD602_tree = (Object)adaptor.create(WORD602);
			adaptor.addChild(root_0, WORD602_tree);
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
	// JPA2.g:537:1: entity_type_literal : WORD ;
	public final JPA2Parser.entity_type_literal_return entity_type_literal() throws RecognitionException {
		JPA2Parser.entity_type_literal_return retval = new JPA2Parser.entity_type_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD603=null;

		Object WORD603_tree=null;

		try {
			// JPA2.g:538:5: ( WORD )
			// JPA2.g:538:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD603=(Token)match(input,WORD,FOLLOW_WORD_in_entity_type_literal4856); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD603_tree = (Object)adaptor.create(WORD603);
			adaptor.addChild(root_0, WORD603_tree);
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
	// JPA2.g:539:1: function_name : STRING_LITERAL ;
	public final JPA2Parser.function_name_return function_name() throws RecognitionException {
		JPA2Parser.function_name_return retval = new JPA2Parser.function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL604=null;

		Object STRING_LITERAL604_tree=null;

		try {
			// JPA2.g:540:5: ( STRING_LITERAL )
			// JPA2.g:540:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL604=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_function_name4867); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL604_tree = (Object)adaptor.create(STRING_LITERAL604);
			adaptor.addChild(root_0, STRING_LITERAL604_tree);
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
	// JPA2.g:541:1: state_field : WORD ;
	public final JPA2Parser.state_field_return state_field() throws RecognitionException {
		JPA2Parser.state_field_return retval = new JPA2Parser.state_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD605=null;

		Object WORD605_tree=null;

		try {
			// JPA2.g:542:5: ( WORD )
			// JPA2.g:542:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD605=(Token)match(input,WORD,FOLLOW_WORD_in_state_field4878); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD605_tree = (Object)adaptor.create(WORD605);
			adaptor.addChild(root_0, WORD605_tree);
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
	// JPA2.g:543:1: result_variable : WORD ;
	public final JPA2Parser.result_variable_return result_variable() throws RecognitionException {
		JPA2Parser.result_variable_return retval = new JPA2Parser.result_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD606=null;

		Object WORD606_tree=null;

		try {
			// JPA2.g:544:5: ( WORD )
			// JPA2.g:544:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD606=(Token)match(input,WORD,FOLLOW_WORD_in_result_variable4889); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD606_tree = (Object)adaptor.create(WORD606);
			adaptor.addChild(root_0, WORD606_tree);
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
	// JPA2.g:545:1: superquery_identification_variable : WORD ;
	public final JPA2Parser.superquery_identification_variable_return superquery_identification_variable() throws RecognitionException {
		JPA2Parser.superquery_identification_variable_return retval = new JPA2Parser.superquery_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD607=null;

		Object WORD607_tree=null;

		try {
			// JPA2.g:546:5: ( WORD )
			// JPA2.g:546:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD607=(Token)match(input,WORD,FOLLOW_WORD_in_superquery_identification_variable4900); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD607_tree = (Object)adaptor.create(WORD607);
			adaptor.addChild(root_0, WORD607_tree);
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
	// JPA2.g:547:1: date_time_timestamp_literal : WORD ;
	public final JPA2Parser.date_time_timestamp_literal_return date_time_timestamp_literal() throws RecognitionException {
		JPA2Parser.date_time_timestamp_literal_return retval = new JPA2Parser.date_time_timestamp_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD608=null;

		Object WORD608_tree=null;

		try {
			// JPA2.g:548:5: ( WORD )
			// JPA2.g:548:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD608=(Token)match(input,WORD,FOLLOW_WORD_in_date_time_timestamp_literal4911); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD608_tree = (Object)adaptor.create(WORD608);
			adaptor.addChild(root_0, WORD608_tree);
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
	// JPA2.g:549:1: pattern_value : string_literal ;
	public final JPA2Parser.pattern_value_return pattern_value() throws RecognitionException {
		JPA2Parser.pattern_value_return retval = new JPA2Parser.pattern_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal609 =null;


		try {
			// JPA2.g:550:5: ( string_literal )
			// JPA2.g:550:7: string_literal
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_literal_in_pattern_value4922);
			string_literal609=string_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal609.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:551:1: collection_valued_input_parameter : input_parameter ;
	public final JPA2Parser.collection_valued_input_parameter_return collection_valued_input_parameter() throws RecognitionException {
		JPA2Parser.collection_valued_input_parameter_return retval = new JPA2Parser.collection_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter610 =null;


		try {
			// JPA2.g:552:5: ( input_parameter )
			// JPA2.g:552:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_collection_valued_input_parameter4933);
			input_parameter610=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter610.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:553:1: single_valued_input_parameter : input_parameter ;
	public final JPA2Parser.single_valued_input_parameter_return single_valued_input_parameter() throws RecognitionException {
		JPA2Parser.single_valued_input_parameter_return retval = new JPA2Parser.single_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter611 =null;


		try {
			// JPA2.g:554:5: ( input_parameter )
			// JPA2.g:554:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_single_valued_input_parameter4944);
			input_parameter611=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter611.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
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
	// JPA2.g:555:1: enum_value_literal : WORD ( '.' WORD )* ;
	public final JPA2Parser.enum_value_literal_return enum_value_literal() throws RecognitionException {
		JPA2Parser.enum_value_literal_return retval = new JPA2Parser.enum_value_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD612=null;
		Token char_literal613=null;
		Token WORD614=null;

		Object WORD612_tree=null;
		Object char_literal613_tree=null;
		Object WORD614_tree=null;

		try {
			// JPA2.g:556:5: ( WORD ( '.' WORD )* )
			// JPA2.g:556:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD612=(Token)match(input,WORD,FOLLOW_WORD_in_enum_value_literal4955); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD612_tree = (Object)adaptor.create(WORD612);
			adaptor.addChild(root_0, WORD612_tree);
			}

			// JPA2.g:556:12: ( '.' WORD )*
			loop137:
			while (true) {
				int alt137=2;
				int LA137_0 = input.LA(1);
				if ( (LA137_0==68) ) {
					alt137=1;
				}

				switch (alt137) {
				case 1 :
					// JPA2.g:556:13: '.' WORD
					{
					char_literal613=(Token)match(input,68,FOLLOW_68_in_enum_value_literal4958); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal613_tree = (Object)adaptor.create(char_literal613);
					adaptor.addChild(root_0, char_literal613_tree);
					}

					WORD614=(Token)match(input,WORD,FOLLOW_WORD_in_enum_value_literal4961); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD614_tree = (Object)adaptor.create(WORD614);
					adaptor.addChild(root_0, WORD614_tree);
					}

					}
					break;

				default :
					break loop137;
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
		// JPA2.g:127:48: ( field )
		// JPA2.g:127:48: field
		{
		pushFollow(FOLLOW_field_in_synpred21_JPA2970);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred21_JPA2

	// $ANTLR start synpred30_JPA2
	public final void synpred30_JPA2_fragment() throws RecognitionException {
		// JPA2.g:145:48: ( field )
		// JPA2.g:145:48: field
		{
		pushFollow(FOLLOW_field_in_synpred30_JPA21160);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred30_JPA2

	// $ANTLR start synpred33_JPA2
	public final void synpred33_JPA2_fragment() throws RecognitionException {
		// JPA2.g:162:7: ( scalar_expression )
		// JPA2.g:162:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred33_JPA21286);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred33_JPA2

	// $ANTLR start synpred34_JPA2
	public final void synpred34_JPA2_fragment() throws RecognitionException {
		// JPA2.g:163:7: ( simple_entity_expression )
		// JPA2.g:163:7: simple_entity_expression
		{
		pushFollow(FOLLOW_simple_entity_expression_in_synpred34_JPA21294);
		simple_entity_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred34_JPA2

	// $ANTLR start synpred43_JPA2
	public final void synpred43_JPA2_fragment() throws RecognitionException {
		// JPA2.g:175:7: ( path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )? )
		// JPA2.g:175:7: path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
		{
		pushFollow(FOLLOW_path_expression_in_synpred43_JPA21419);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:175:23: ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
		int alt144=2;
		int LA144_0 = input.LA(1);
		if ( ((LA144_0 >= 64 && LA144_0 <= 65)||LA144_0==67||LA144_0==69) ) {
			alt144=1;
		}
		switch (alt144) {
			case 1 :
				// JPA2.g:175:24: ( '+' | '-' | '*' | '/' ) scalar_expression
				{
				if ( (input.LA(1) >= 64 && input.LA(1) <= 65)||input.LA(1)==67||input.LA(1)==69 ) {
					input.consume();
					state.errorRecovery=false;
					state.failed=false;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return;}
					MismatchedSetException mse = new MismatchedSetException(null,input);
					throw mse;
				}
				pushFollow(FOLLOW_scalar_expression_in_synpred43_JPA21438);
				scalar_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred43_JPA2

	// $ANTLR start synpred44_JPA2
	public final void synpred44_JPA2_fragment() throws RecognitionException {
		// JPA2.g:176:7: ( identification_variable )
		// JPA2.g:176:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred44_JPA21448);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred44_JPA2

	// $ANTLR start synpred45_JPA2
	public final void synpred45_JPA2_fragment() throws RecognitionException {
		// JPA2.g:177:7: ( scalar_expression )
		// JPA2.g:177:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred45_JPA21466);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred45_JPA2

	// $ANTLR start synpred46_JPA2
	public final void synpred46_JPA2_fragment() throws RecognitionException {
		// JPA2.g:178:7: ( aggregate_expression )
		// JPA2.g:178:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred46_JPA21474);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred46_JPA2

	// $ANTLR start synpred49_JPA2
	public final void synpred49_JPA2_fragment() throws RecognitionException {
		// JPA2.g:184:7: ( path_expression )
		// JPA2.g:184:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred49_JPA21531);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred49_JPA2

	// $ANTLR start synpred50_JPA2
	public final void synpred50_JPA2_fragment() throws RecognitionException {
		// JPA2.g:185:7: ( scalar_expression )
		// JPA2.g:185:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred50_JPA21539);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred50_JPA2

	// $ANTLR start synpred51_JPA2
	public final void synpred51_JPA2_fragment() throws RecognitionException {
		// JPA2.g:186:7: ( aggregate_expression )
		// JPA2.g:186:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred51_JPA21547);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred51_JPA2

	// $ANTLR start synpred53_JPA2
	public final void synpred53_JPA2_fragment() throws RecognitionException {
		// JPA2.g:189:7: ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' )
		// JPA2.g:189:7: aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')'
		{
		pushFollow(FOLLOW_aggregate_expression_function_name_in_synpred53_JPA21566);
		aggregate_expression_function_name();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred53_JPA21568); if (state.failed) return;

		// JPA2.g:189:45: ( DISTINCT )?
		int alt145=2;
		int LA145_0 = input.LA(1);
		if ( (LA145_0==DISTINCT) ) {
			alt145=1;
		}
		switch (alt145) {
			case 1 :
				// JPA2.g:189:46: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred53_JPA21570); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_arithmetic_expression_in_synpred53_JPA21574);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred53_JPA21575); if (state.failed) return;

		}

	}
	// $ANTLR end synpred53_JPA2

	// $ANTLR start synpred55_JPA2
	public final void synpred55_JPA2_fragment() throws RecognitionException {
		// JPA2.g:191:7: ( 'COUNT' '(' ( DISTINCT )? count_argument ')' )
		// JPA2.g:191:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
		{
		match(input,COUNT,FOLLOW_COUNT_in_synpred55_JPA21609); if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred55_JPA21611); if (state.failed) return;

		// JPA2.g:191:18: ( DISTINCT )?
		int alt146=2;
		int LA146_0 = input.LA(1);
		if ( (LA146_0==DISTINCT) ) {
			alt146=1;
		}
		switch (alt146) {
			case 1 :
				// JPA2.g:191:19: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred55_JPA21613); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_count_argument_in_synpred55_JPA21617);
		count_argument();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred55_JPA21619); if (state.failed) return;

		}

	}
	// $ANTLR end synpred55_JPA2

	// $ANTLR start synpred67_JPA2
	public final void synpred67_JPA2_fragment() throws RecognitionException {
		// JPA2.g:214:7: ( path_expression )
		// JPA2.g:214:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred67_JPA21890);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred67_JPA2

	// $ANTLR start synpred68_JPA2
	public final void synpred68_JPA2_fragment() throws RecognitionException {
		// JPA2.g:214:25: ( general_identification_variable )
		// JPA2.g:214:25: general_identification_variable
		{
		pushFollow(FOLLOW_general_identification_variable_in_synpred68_JPA21894);
		general_identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred68_JPA2

	// $ANTLR start synpred69_JPA2
	public final void synpred69_JPA2_fragment() throws RecognitionException {
		// JPA2.g:214:59: ( result_variable )
		// JPA2.g:214:59: result_variable
		{
		pushFollow(FOLLOW_result_variable_in_synpred69_JPA21898);
		result_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred69_JPA2

	// $ANTLR start synpred70_JPA2
	public final void synpred70_JPA2_fragment() throws RecognitionException {
		// JPA2.g:214:77: ( scalar_expression )
		// JPA2.g:214:77: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred70_JPA21902);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred70_JPA2

	// $ANTLR start synpred80_JPA2
	public final void synpred80_JPA2_fragment() throws RecognitionException {
		// JPA2.g:231:7: ( general_derived_path '.' single_valued_object_field )
		// JPA2.g:231:7: general_derived_path '.' single_valued_object_field
		{
		pushFollow(FOLLOW_general_derived_path_in_synpred80_JPA22109);
		general_derived_path();
		state._fsp--;
		if (state.failed) return;

		match(input,68,FOLLOW_68_in_synpred80_JPA22110); if (state.failed) return;

		pushFollow(FOLLOW_single_valued_object_field_in_synpred80_JPA22111);
		single_valued_object_field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred80_JPA2

	// $ANTLR start synpred85_JPA2
	public final void synpred85_JPA2_fragment() throws RecognitionException {
		// JPA2.g:249:7: ( path_expression )
		// JPA2.g:249:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred85_JPA22263);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred85_JPA2

	// $ANTLR start synpred86_JPA2
	public final void synpred86_JPA2_fragment() throws RecognitionException {
		// JPA2.g:250:7: ( scalar_expression )
		// JPA2.g:250:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred86_JPA22271);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred86_JPA2

	// $ANTLR start synpred87_JPA2
	public final void synpred87_JPA2_fragment() throws RecognitionException {
		// JPA2.g:251:7: ( aggregate_expression )
		// JPA2.g:251:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred87_JPA22279);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred87_JPA2

	// $ANTLR start synpred88_JPA2
	public final void synpred88_JPA2_fragment() throws RecognitionException {
		// JPA2.g:254:7: ( arithmetic_expression )
		// JPA2.g:254:7: arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred88_JPA22298);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred88_JPA2

	// $ANTLR start synpred89_JPA2
	public final void synpred89_JPA2_fragment() throws RecognitionException {
		// JPA2.g:255:7: ( string_expression )
		// JPA2.g:255:7: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred89_JPA22306);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred89_JPA2

	// $ANTLR start synpred90_JPA2
	public final void synpred90_JPA2_fragment() throws RecognitionException {
		// JPA2.g:256:7: ( enum_expression )
		// JPA2.g:256:7: enum_expression
		{
		pushFollow(FOLLOW_enum_expression_in_synpred90_JPA22314);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred90_JPA2

	// $ANTLR start synpred91_JPA2
	public final void synpred91_JPA2_fragment() throws RecognitionException {
		// JPA2.g:257:7: ( datetime_expression )
		// JPA2.g:257:7: datetime_expression
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred91_JPA22322);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred91_JPA2

	// $ANTLR start synpred92_JPA2
	public final void synpred92_JPA2_fragment() throws RecognitionException {
		// JPA2.g:258:7: ( boolean_expression )
		// JPA2.g:258:7: boolean_expression
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred92_JPA22330);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred92_JPA2

	// $ANTLR start synpred93_JPA2
	public final void synpred93_JPA2_fragment() throws RecognitionException {
		// JPA2.g:259:7: ( case_expression )
		// JPA2.g:259:7: case_expression
		{
		pushFollow(FOLLOW_case_expression_in_synpred93_JPA22338);
		case_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred93_JPA2

	// $ANTLR start synpred96_JPA2
	public final void synpred96_JPA2_fragment() throws RecognitionException {
		// JPA2.g:266:8: ( 'NOT' )
		// JPA2.g:266:8: 'NOT'
		{
		match(input,NOT,FOLLOW_NOT_in_synpred96_JPA22398); if (state.failed) return;

		}

	}
	// $ANTLR end synpred96_JPA2

	// $ANTLR start synpred97_JPA2
	public final void synpred97_JPA2_fragment() throws RecognitionException {
		// JPA2.g:268:7: ( simple_cond_expression )
		// JPA2.g:268:7: simple_cond_expression
		{
		pushFollow(FOLLOW_simple_cond_expression_in_synpred97_JPA22413);
		simple_cond_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred97_JPA2

	// $ANTLR start synpred98_JPA2
	public final void synpred98_JPA2_fragment() throws RecognitionException {
		// JPA2.g:272:7: ( comparison_expression )
		// JPA2.g:272:7: comparison_expression
		{
		pushFollow(FOLLOW_comparison_expression_in_synpred98_JPA22450);
		comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred98_JPA2

	// $ANTLR start synpred99_JPA2
	public final void synpred99_JPA2_fragment() throws RecognitionException {
		// JPA2.g:273:7: ( between_expression )
		// JPA2.g:273:7: between_expression
		{
		pushFollow(FOLLOW_between_expression_in_synpred99_JPA22458);
		between_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred99_JPA2

	// $ANTLR start synpred100_JPA2
	public final void synpred100_JPA2_fragment() throws RecognitionException {
		// JPA2.g:274:7: ( in_expression )
		// JPA2.g:274:7: in_expression
		{
		pushFollow(FOLLOW_in_expression_in_synpred100_JPA22466);
		in_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred100_JPA2

	// $ANTLR start synpred101_JPA2
	public final void synpred101_JPA2_fragment() throws RecognitionException {
		// JPA2.g:275:7: ( like_expression )
		// JPA2.g:275:7: like_expression
		{
		pushFollow(FOLLOW_like_expression_in_synpred101_JPA22474);
		like_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred101_JPA2

	// $ANTLR start synpred102_JPA2
	public final void synpred102_JPA2_fragment() throws RecognitionException {
		// JPA2.g:276:7: ( null_comparison_expression )
		// JPA2.g:276:7: null_comparison_expression
		{
		pushFollow(FOLLOW_null_comparison_expression_in_synpred102_JPA22482);
		null_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred102_JPA2

	// $ANTLR start synpred103_JPA2
	public final void synpred103_JPA2_fragment() throws RecognitionException {
		// JPA2.g:277:7: ( empty_collection_comparison_expression )
		// JPA2.g:277:7: empty_collection_comparison_expression
		{
		pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred103_JPA22490);
		empty_collection_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred103_JPA2

	// $ANTLR start synpred104_JPA2
	public final void synpred104_JPA2_fragment() throws RecognitionException {
		// JPA2.g:278:7: ( collection_member_expression )
		// JPA2.g:278:7: collection_member_expression
		{
		pushFollow(FOLLOW_collection_member_expression_in_synpred104_JPA22498);
		collection_member_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred104_JPA2

	// $ANTLR start synpred123_JPA2
	public final void synpred123_JPA2_fragment() throws RecognitionException {
		// JPA2.g:307:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
		// JPA2.g:307:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred123_JPA22751);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:307:29: ( 'NOT' )?
		int alt148=2;
		int LA148_0 = input.LA(1);
		if ( (LA148_0==NOT) ) {
			alt148=1;
		}
		switch (alt148) {
			case 1 :
				// JPA2.g:307:30: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred123_JPA22754); if (state.failed) return;

				}
				break;

		}

		match(input,87,FOLLOW_87_in_synpred123_JPA22758); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred123_JPA22760);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred123_JPA22762); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred123_JPA22764);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred123_JPA2

	// $ANTLR start synpred125_JPA2
	public final void synpred125_JPA2_fragment() throws RecognitionException {
		// JPA2.g:308:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
		// JPA2.g:308:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred125_JPA22772);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:308:25: ( 'NOT' )?
		int alt149=2;
		int LA149_0 = input.LA(1);
		if ( (LA149_0==NOT) ) {
			alt149=1;
		}
		switch (alt149) {
			case 1 :
				// JPA2.g:308:26: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred125_JPA22775); if (state.failed) return;

				}
				break;

		}

		match(input,87,FOLLOW_87_in_synpred125_JPA22779); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred125_JPA22781);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred125_JPA22783); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred125_JPA22785);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred125_JPA2

	// $ANTLR start synpred138_JPA2
	public final void synpred138_JPA2_fragment() throws RecognitionException {
		// JPA2.g:324:42: ( string_expression )
		// JPA2.g:324:42: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred138_JPA22974);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred138_JPA2

	// $ANTLR start synpred139_JPA2
	public final void synpred139_JPA2_fragment() throws RecognitionException {
		// JPA2.g:324:62: ( pattern_value )
		// JPA2.g:324:62: pattern_value
		{
		pushFollow(FOLLOW_pattern_value_in_synpred139_JPA22978);
		pattern_value();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred139_JPA2

	// $ANTLR start synpred141_JPA2
	public final void synpred141_JPA2_fragment() throws RecognitionException {
		// JPA2.g:326:8: ( path_expression )
		// JPA2.g:326:8: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred141_JPA23001);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred141_JPA2

	// $ANTLR start synpred149_JPA2
	public final void synpred149_JPA2_fragment() throws RecognitionException {
		// JPA2.g:336:7: ( identification_variable )
		// JPA2.g:336:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred149_JPA23103);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred149_JPA2

	// $ANTLR start synpred156_JPA2
	public final void synpred156_JPA2_fragment() throws RecognitionException {
		// JPA2.g:344:7: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) )
		// JPA2.g:344:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_string_expression_in_synpred156_JPA23172);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:344:25: ( comparison_operator | 'REGEXP' )
		int alt151=2;
		int LA151_0 = input.LA(1);
		if ( ((LA151_0 >= 71 && LA151_0 <= 76)) ) {
			alt151=1;
		}
		else if ( (LA151_0==127) ) {
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
				// JPA2.g:344:26: comparison_operator
				{
				pushFollow(FOLLOW_comparison_operator_in_synpred156_JPA23175);
				comparison_operator();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:344:48: 'REGEXP'
				{
				match(input,127,FOLLOW_127_in_synpred156_JPA23179); if (state.failed) return;

				}
				break;

		}

		// JPA2.g:344:58: ( string_expression | all_or_any_expression )
		int alt152=2;
		int LA152_0 = input.LA(1);
		if ( (LA152_0==AVG||LA152_0==CASE||LA152_0==COUNT||LA152_0==GROUP||(LA152_0 >= LOWER && LA152_0 <= NAMED_PARAMETER)||(LA152_0 >= STRING_LITERAL && LA152_0 <= SUM)||LA152_0==WORD||LA152_0==63||LA152_0==77||LA152_0==82||(LA152_0 >= 89 && LA152_0 <= 91)||LA152_0==102||LA152_0==104||LA152_0==120||LA152_0==133||LA152_0==136||LA152_0==139) ) {
			alt152=1;
		}
		else if ( ((LA152_0 >= 85 && LA152_0 <= 86)||LA152_0==131) ) {
			alt152=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 152, 0, input);
			throw nvae;
		}

		switch (alt152) {
			case 1 :
				// JPA2.g:344:59: string_expression
				{
				pushFollow(FOLLOW_string_expression_in_synpred156_JPA23183);
				string_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:344:79: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred156_JPA23187);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred156_JPA2

	// $ANTLR start synpred159_JPA2
	public final void synpred159_JPA2_fragment() throws RecognitionException {
		// JPA2.g:345:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
		// JPA2.g:345:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred159_JPA23196);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:345:39: ( boolean_expression | all_or_any_expression )
		int alt153=2;
		int LA153_0 = input.LA(1);
		if ( (LA153_0==CASE||LA153_0==GROUP||LA153_0==LPAREN||LA153_0==NAMED_PARAMETER||LA153_0==WORD||LA153_0==63||LA153_0==77||LA153_0==82||(LA153_0 >= 89 && LA153_0 <= 90)||LA153_0==102||LA153_0==104||LA153_0==120||(LA153_0 >= 144 && LA153_0 <= 145)) ) {
			alt153=1;
		}
		else if ( ((LA153_0 >= 85 && LA153_0 <= 86)||LA153_0==131) ) {
			alt153=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 153, 0, input);
			throw nvae;
		}

		switch (alt153) {
			case 1 :
				// JPA2.g:345:40: boolean_expression
				{
				pushFollow(FOLLOW_boolean_expression_in_synpred159_JPA23207);
				boolean_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:345:61: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred159_JPA23211);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred159_JPA2

	// $ANTLR start synpred162_JPA2
	public final void synpred162_JPA2_fragment() throws RecognitionException {
		// JPA2.g:346:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
		// JPA2.g:346:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_enum_expression_in_synpred162_JPA23220);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:346:34: ( enum_expression | all_or_any_expression )
		int alt154=2;
		int LA154_0 = input.LA(1);
		if ( (LA154_0==CASE||LA154_0==GROUP||LA154_0==LPAREN||LA154_0==NAMED_PARAMETER||LA154_0==WORD||LA154_0==63||LA154_0==77||LA154_0==90||LA154_0==120) ) {
			alt154=1;
		}
		else if ( ((LA154_0 >= 85 && LA154_0 <= 86)||LA154_0==131) ) {
			alt154=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 154, 0, input);
			throw nvae;
		}

		switch (alt154) {
			case 1 :
				// JPA2.g:346:35: enum_expression
				{
				pushFollow(FOLLOW_enum_expression_in_synpred162_JPA23229);
				enum_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:346:53: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred162_JPA23233);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred162_JPA2

	// $ANTLR start synpred164_JPA2
	public final void synpred164_JPA2_fragment() throws RecognitionException {
		// JPA2.g:347:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
		// JPA2.g:347:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred164_JPA23242);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred164_JPA23244);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:347:47: ( datetime_expression | all_or_any_expression )
		int alt155=2;
		int LA155_0 = input.LA(1);
		if ( (LA155_0==AVG||LA155_0==CASE||LA155_0==COUNT||LA155_0==GROUP||(LA155_0 >= LPAREN && LA155_0 <= NAMED_PARAMETER)||LA155_0==SUM||LA155_0==WORD||LA155_0==63||LA155_0==77||LA155_0==82||(LA155_0 >= 89 && LA155_0 <= 90)||(LA155_0 >= 92 && LA155_0 <= 94)||LA155_0==102||LA155_0==104||LA155_0==120) ) {
			alt155=1;
		}
		else if ( ((LA155_0 >= 85 && LA155_0 <= 86)||LA155_0==131) ) {
			alt155=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 155, 0, input);
			throw nvae;
		}

		switch (alt155) {
			case 1 :
				// JPA2.g:347:48: datetime_expression
				{
				pushFollow(FOLLOW_datetime_expression_in_synpred164_JPA23247);
				datetime_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:347:70: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred164_JPA23251);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred164_JPA2

	// $ANTLR start synpred167_JPA2
	public final void synpred167_JPA2_fragment() throws RecognitionException {
		// JPA2.g:348:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
		// JPA2.g:348:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_entity_expression_in_synpred167_JPA23260);
		entity_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:348:38: ( entity_expression | all_or_any_expression )
		int alt156=2;
		int LA156_0 = input.LA(1);
		if ( (LA156_0==GROUP||LA156_0==NAMED_PARAMETER||LA156_0==WORD||LA156_0==63||LA156_0==77) ) {
			alt156=1;
		}
		else if ( ((LA156_0 >= 85 && LA156_0 <= 86)||LA156_0==131) ) {
			alt156=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 156, 0, input);
			throw nvae;
		}

		switch (alt156) {
			case 1 :
				// JPA2.g:348:39: entity_expression
				{
				pushFollow(FOLLOW_entity_expression_in_synpred167_JPA23271);
				entity_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:348:59: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred167_JPA23275);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred167_JPA2

	// $ANTLR start synpred169_JPA2
	public final void synpred169_JPA2_fragment() throws RecognitionException {
		// JPA2.g:349:7: ( entity_type_expression ( '=' | '<>' ) entity_type_expression )
		// JPA2.g:349:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
		{
		pushFollow(FOLLOW_entity_type_expression_in_synpred169_JPA23284);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		pushFollow(FOLLOW_entity_type_expression_in_synpred169_JPA23294);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred169_JPA2

	// $ANTLR start synpred178_JPA2
	public final void synpred178_JPA2_fragment() throws RecognitionException {
		// JPA2.g:360:7: ( arithmetic_term ( ( '+' | '-' ) arithmetic_term )+ )
		// JPA2.g:360:7: arithmetic_term ( ( '+' | '-' ) arithmetic_term )+
		{
		pushFollow(FOLLOW_arithmetic_term_in_synpred178_JPA23375);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:360:23: ( ( '+' | '-' ) arithmetic_term )+
		int cnt157=0;
		loop157:
		while (true) {
			int alt157=2;
			int LA157_0 = input.LA(1);
			if ( (LA157_0==65||LA157_0==67) ) {
				alt157=1;
			}

			switch (alt157) {
			case 1 :
				// JPA2.g:360:24: ( '+' | '-' ) arithmetic_term
				{
				if ( input.LA(1)==65||input.LA(1)==67 ) {
					input.consume();
					state.errorRecovery=false;
					state.failed=false;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return;}
					MismatchedSetException mse = new MismatchedSetException(null,input);
					throw mse;
				}
				pushFollow(FOLLOW_arithmetic_term_in_synpred178_JPA23386);
				arithmetic_term();
				state._fsp--;
				if (state.failed) return;

				}
				break;

			default :
				if ( cnt157 >= 1 ) break loop157;
				if (state.backtracking>0) {state.failed=true; return;}
				EarlyExitException eee = new EarlyExitException(157, input);
				throw eee;
			}
			cnt157++;
		}

		}

	}
	// $ANTLR end synpred178_JPA2

	// $ANTLR start synpred181_JPA2
	public final void synpred181_JPA2_fragment() throws RecognitionException {
		// JPA2.g:363:7: ( arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+ )
		// JPA2.g:363:7: arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+
		{
		pushFollow(FOLLOW_arithmetic_factor_in_synpred181_JPA23407);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:363:25: ( ( '*' | '/' ) arithmetic_factor )+
		int cnt158=0;
		loop158:
		while (true) {
			int alt158=2;
			int LA158_0 = input.LA(1);
			if ( (LA158_0==64||LA158_0==69) ) {
				alt158=1;
			}

			switch (alt158) {
			case 1 :
				// JPA2.g:363:26: ( '*' | '/' ) arithmetic_factor
				{
				if ( input.LA(1)==64||input.LA(1)==69 ) {
					input.consume();
					state.errorRecovery=false;
					state.failed=false;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return;}
					MismatchedSetException mse = new MismatchedSetException(null,input);
					throw mse;
				}
				pushFollow(FOLLOW_arithmetic_factor_in_synpred181_JPA23419);
				arithmetic_factor();
				state._fsp--;
				if (state.failed) return;

				}
				break;

			default :
				if ( cnt158 >= 1 ) break loop158;
				if (state.backtracking>0) {state.failed=true; return;}
				EarlyExitException eee = new EarlyExitException(158, input);
				throw eee;
			}
			cnt158++;
		}

		}

	}
	// $ANTLR end synpred181_JPA2

	// $ANTLR start synpred185_JPA2
	public final void synpred185_JPA2_fragment() throws RecognitionException {
		// JPA2.g:369:7: ( decimal_literal )
		// JPA2.g:369:7: decimal_literal
		{
		pushFollow(FOLLOW_decimal_literal_in_synpred185_JPA23471);
		decimal_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred185_JPA2

	// $ANTLR start synpred186_JPA2
	public final void synpred186_JPA2_fragment() throws RecognitionException {
		// JPA2.g:370:7: ( numeric_literal )
		// JPA2.g:370:7: numeric_literal
		{
		pushFollow(FOLLOW_numeric_literal_in_synpred186_JPA23479);
		numeric_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred186_JPA2

	// $ANTLR start synpred187_JPA2
	public final void synpred187_JPA2_fragment() throws RecognitionException {
		// JPA2.g:371:7: ( '(' arithmetic_expression ')' )
		// JPA2.g:371:7: '(' arithmetic_expression ')'
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred187_JPA23487); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred187_JPA23488);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred187_JPA23489); if (state.failed) return;

		}

	}
	// $ANTLR end synpred187_JPA2

	// $ANTLR start synpred190_JPA2
	public final void synpred190_JPA2_fragment() throws RecognitionException {
		// JPA2.g:374:7: ( aggregate_expression )
		// JPA2.g:374:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred190_JPA23513);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred190_JPA2

	// $ANTLR start synpred192_JPA2
	public final void synpred192_JPA2_fragment() throws RecognitionException {
		// JPA2.g:376:7: ( function_invocation )
		// JPA2.g:376:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred192_JPA23529);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred192_JPA2

	// $ANTLR start synpred198_JPA2
	public final void synpred198_JPA2_fragment() throws RecognitionException {
		// JPA2.g:384:7: ( aggregate_expression )
		// JPA2.g:384:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred198_JPA23588);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred198_JPA2

	// $ANTLR start synpred200_JPA2
	public final void synpred200_JPA2_fragment() throws RecognitionException {
		// JPA2.g:386:7: ( function_invocation )
		// JPA2.g:386:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred200_JPA23604);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred200_JPA2

	// $ANTLR start synpred202_JPA2
	public final void synpred202_JPA2_fragment() throws RecognitionException {
		// JPA2.g:390:7: ( path_expression )
		// JPA2.g:390:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred202_JPA23631);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred202_JPA2

	// $ANTLR start synpred205_JPA2
	public final void synpred205_JPA2_fragment() throws RecognitionException {
		// JPA2.g:393:7: ( aggregate_expression )
		// JPA2.g:393:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred205_JPA23655);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred205_JPA2

	// $ANTLR start synpred207_JPA2
	public final void synpred207_JPA2_fragment() throws RecognitionException {
		// JPA2.g:395:7: ( function_invocation )
		// JPA2.g:395:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred207_JPA23671);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred207_JPA2

	// $ANTLR start synpred209_JPA2
	public final void synpred209_JPA2_fragment() throws RecognitionException {
		// JPA2.g:397:7: ( date_time_timestamp_literal )
		// JPA2.g:397:7: date_time_timestamp_literal
		{
		pushFollow(FOLLOW_date_time_timestamp_literal_in_synpred209_JPA23687);
		date_time_timestamp_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred209_JPA2

	// $ANTLR start synpred247_JPA2
	public final void synpred247_JPA2_fragment() throws RecognitionException {
		// JPA2.g:448:7: ( literal )
		// JPA2.g:448:7: literal
		{
		pushFollow(FOLLOW_literal_in_synpred247_JPA24142);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred247_JPA2

	// $ANTLR start synpred248_JPA2
	public final void synpred248_JPA2_fragment() throws RecognitionException {
		// JPA2.g:449:7: ( path_expression )
		// JPA2.g:449:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred248_JPA24150);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred248_JPA2

	// $ANTLR start synpred249_JPA2
	public final void synpred249_JPA2_fragment() throws RecognitionException {
		// JPA2.g:450:7: ( input_parameter )
		// JPA2.g:450:7: input_parameter
		{
		pushFollow(FOLLOW_input_parameter_in_synpred249_JPA24158);
		input_parameter();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred249_JPA2

	// Delegated rules

	public final boolean synpred69_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred69_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred187_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred187_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred103_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred103_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred248_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred248_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred99_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred99_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred55_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred55_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred181_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred181_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred104_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred104_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred149_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred149_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred249_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred249_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred98_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred98_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred205_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred205_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred139_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred139_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred202_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred202_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred70_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred70_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred68_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred68_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred125_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred125_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred141_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred141_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred87_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred87_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred123_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred123_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred162_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred162_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred185_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred185_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred43_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred43_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred88_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred88_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred100_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred100_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred198_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred198_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred200_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred200_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred138_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred138_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred207_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred207_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred192_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred192_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred44_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred44_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred101_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred101_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred53_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred53_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred102_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred102_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred247_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred247_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred67_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred67_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred164_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred164_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred156_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred156_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred159_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred159_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred209_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred209_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred50_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred50_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}


	protected DFA41 dfa41 = new DFA41(this);
	static final String DFA41_eotS =
		"\31\uffff";
	static final String DFA41_eofS =
		"\31\uffff";
	static final String DFA41_minS =
		"\1\7\1\33\2\uffff\2\7\1\43\1\uffff\1\5\16\43\1\0\1\5";
	static final String DFA41_maxS =
		"\1\150\1\33\2\uffff\2\u0084\1\104\1\uffff\1\u008f\16\105\1\0\1\u008f";
	static final String DFA41_acceptS =
		"\2\uffff\1\1\1\3\3\uffff\1\2\21\uffff";
	static final String DFA41_specialS =
		"\27\uffff\1\0\1\uffff}>";
	static final String[] DFA41_transitionS = {
			"\1\2\3\uffff\1\1\20\uffff\2\2\11\uffff\1\2\100\uffff\1\3",
			"\1\4",
			"",
			"",
			"\1\2\1\uffff\1\2\1\uffff\1\2\1\uffff\1\5\4\uffff\1\6\3\uffff\1\2\4\uffff"+
			"\4\2\10\uffff\1\2\25\uffff\1\6\1\uffff\1\2\1\uffff\1\2\1\uffff\1\2\2"+
			"\uffff\1\2\6\uffff\1\2\4\uffff\1\2\1\uffff\1\2\4\uffff\2\2\13\uffff\1"+
			"\2\1\uffff\1\2\1\uffff\1\2\3\uffff\1\2\1\uffff\1\2\2\uffff\1\2\4\uffff"+
			"\1\2\11\uffff\1\2\1\uffff\1\2",
			"\1\2\1\uffff\1\2\1\uffff\1\2\6\uffff\1\6\3\uffff\1\2\4\uffff\4\2\10"+
			"\uffff\1\2\25\uffff\1\6\1\uffff\1\2\1\uffff\1\2\1\uffff\1\2\2\uffff\1"+
			"\2\6\uffff\1\2\4\uffff\1\2\1\uffff\1\2\4\uffff\2\2\13\uffff\1\2\1\uffff"+
			"\1\2\1\uffff\1\2\3\uffff\1\2\1\uffff\1\2\2\uffff\1\2\4\uffff\1\2\11\uffff"+
			"\1\2\1\uffff\1\2",
			"\1\7\40\uffff\1\10",
			"",
			"\1\23\1\uffff\1\21\1\uffff\1\25\1\uffff\1\22\6\uffff\1\14\11\uffff\1"+
			"\16\1\17\3\uffff\1\15\1\uffff\1\27\3\uffff\1\20\25\uffff\1\11\2\uffff"+
			"\2\2\1\uffff\1\2\1\uffff\1\2\31\uffff\1\26\3\uffff\1\26\3\uffff\1\13"+
			"\1\uffff\1\26\7\uffff\1\24\1\26\1\uffff\1\26\11\uffff\1\26\1\uffff\1"+
			"\26\1\12\13\uffff\1\26\1\uffff\1\26",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\27\34\uffff\2\2\1\uffff\1\2\1\30\1\2",
			"\1\uffff",
			"\1\23\1\uffff\1\21\1\uffff\1\25\1\uffff\1\22\6\uffff\1\14\11\uffff\1"+
			"\16\1\17\3\uffff\1\15\1\uffff\1\27\3\uffff\1\20\25\uffff\1\11\2\uffff"+
			"\2\2\1\uffff\1\2\1\uffff\1\2\31\uffff\1\26\3\uffff\1\26\3\uffff\1\13"+
			"\1\uffff\1\26\7\uffff\1\24\1\26\1\uffff\1\26\11\uffff\1\26\1\uffff\1"+
			"\26\1\12\13\uffff\1\26\1\uffff\1\26"
	};

	static final short[] DFA41_eot = DFA.unpackEncodedString(DFA41_eotS);
	static final short[] DFA41_eof = DFA.unpackEncodedString(DFA41_eofS);
	static final char[] DFA41_min = DFA.unpackEncodedStringToUnsignedChars(DFA41_minS);
	static final char[] DFA41_max = DFA.unpackEncodedStringToUnsignedChars(DFA41_maxS);
	static final short[] DFA41_accept = DFA.unpackEncodedString(DFA41_acceptS);
	static final short[] DFA41_special = DFA.unpackEncodedString(DFA41_specialS);
	static final short[][] DFA41_transition;

	static {
		int numStates = DFA41_transitionS.length;
		DFA41_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA41_transition[i] = DFA.unpackEncodedString(DFA41_transitionS[i]);
		}
	}

	protected class DFA41 extends DFA {

		public DFA41(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 41;
			this.eot = DFA41_eot;
			this.eof = DFA41_eof;
			this.min = DFA41_min;
			this.max = DFA41_max;
			this.accept = DFA41_accept;
			this.special = DFA41_special;
			this.transition = DFA41_transition;
		}
		@Override
		public String getDescription() {
			return "188:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			TokenStream input = (TokenStream)_input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA41_23 = input.LA(1);
						 
						int index41_23 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred53_JPA2()) ) {s = 2;}
						else if ( (synpred55_JPA2()) ) {s = 7;}
						 
						input.seek(index41_23);
						if ( s>=0 ) return s;
						break;
			}
			if (state.backtracking>0) {state.failed=true; return -1;}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 41, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	public static final BitSet FOLLOW_select_statement_in_ql_statement511 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_update_statement_in_ql_statement515 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_delete_statement_in_ql_statement519 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_129_in_select_statement534 = new BitSet(new long[]{0xA00000C07C442A80L,0x092945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_select_clause_in_select_statement536 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_from_clause_in_select_statement538 = new BitSet(new long[]{0x00000002000C0002L,0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_where_clause_in_select_statement541 = new BitSet(new long[]{0x00000002000C0002L});
	public static final BitSet FOLLOW_groupby_clause_in_select_statement546 = new BitSet(new long[]{0x0000000200080002L});
	public static final BitSet FOLLOW_having_clause_in_select_statement551 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_orderby_clause_in_select_statement556 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_138_in_update_statement614 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_update_clause_in_update_statement616 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_where_clause_in_update_statement619 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_96_in_delete_statement655 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_delete_clause_in_delete_statement657 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_where_clause_in_delete_statement660 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_from_clause698 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause700 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_from_clause703 = new BitSet(new long[]{0x2000000000100000L});
	public static final BitSet FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause705 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration739 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration748 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration772 = new BitSet(new long[]{0x0000000001A00002L});
	public static final BitSet FOLLOW_joined_clause_in_identification_variable_declaration774 = new BitSet(new long[]{0x0000000001A00002L});
	public static final BitSet FOLLOW_joined_clause_in_join_section805 = new BitSet(new long[]{0x0000000001A00002L});
	public static final BitSet FOLLOW_join_in_joined_clause813 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fetch_join_in_joined_clause817 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_range_variable_declaration829 = new BitSet(new long[]{0x2000000000040020L});
	public static final BitSet FOLLOW_AS_in_range_variable_declaration832 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration836 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_join865 = new BitSet(new long[]{0x2000000000040000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_join_association_path_expression_in_join867 = new BitSet(new long[]{0x2000000000040020L});
	public static final BitSet FOLLOW_AS_in_join870 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_join874 = new BitSet(new long[]{0x0000000000000002L,0x2000000000000000L});
	public static final BitSet FOLLOW_125_in_join877 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000030BB4L});
	public static final BitSet FOLLOW_conditional_expression_in_join879 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_fetch_join913 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_FETCH_in_fetch_join915 = new BitSet(new long[]{0x2000000000040000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join917 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFT_in_join_spec931 = new BitSet(new long[]{0x0000000400800000L});
	public static final BitSet FOLLOW_OUTER_in_join_spec935 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_INNER_in_join_spec941 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_JOIN_in_join_spec946 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression960 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_join_association_path_expression962 = new BitSet(new long[]{0x2000008230040AA2L,0x4016028880000000L,0x000000000000A003L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression965 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_join_association_path_expression966 = new BitSet(new long[]{0x2000008230040AA2L,0x4016028880000000L,0x000000000000A003L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression970 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_135_in_join_association_path_expression1005 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression1007 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_join_association_path_expression1009 = new BitSet(new long[]{0x2000008230040AA0L,0x4016028880000000L,0x000000000000A003L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression1012 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_join_association_path_expression1013 = new BitSet(new long[]{0x2000008230040AA0L,0x4016028880000000L,0x000000000000A003L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression1017 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_AS_in_join_association_path_expression1020 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_subtype_in_join_association_path_expression1022 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_join_association_path_expression1024 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_join_association_path_expression1057 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_collection_member_declaration1070 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration1071 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_declaration1073 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration1075 = new BitSet(new long[]{0x2000000000040020L});
	public static final BitSet FOLLOW_AS_in_collection_member_declaration1078 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration1082 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_qualified_identification_variable1111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_98_in_qualified_identification_variable1119 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_qualified_identification_variable1120 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_qualified_identification_variable1121 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_108_in_map_field_identification_variable1128 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1129 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1130 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_140_in_map_field_identification_variable1134 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1135 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1136 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_path_expression1150 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_path_expression1152 = new BitSet(new long[]{0x2000008230040AA2L,0x4016028880000000L,0x000000000000A003L});
	public static final BitSet FOLLOW_field_in_path_expression1155 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_path_expression1156 = new BitSet(new long[]{0x2000008230040AA2L,0x4016028880000000L,0x000000000000A003L});
	public static final BitSet FOLLOW_field_in_path_expression1160 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_general_identification_variable1199 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_general_identification_variable1207 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_update_clause1220 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_SET_in_update_clause1222 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1224 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_update_clause1227 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1229 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_path_expression_in_update_item1271 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_74_in_update_item1273 = new BitSet(new long[]{0xA00000C07C440A80L,0x018945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_new_value_in_update_item1275 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_new_value1286 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_new_value1294 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_119_in_new_value1302 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_delete_clause1316 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_delete_clause1318 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_select_clause1346 = new BitSet(new long[]{0xA00000C07C440A80L,0x092945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_select_item_in_select_clause1350 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_select_clause1353 = new BitSet(new long[]{0xA00000C07C440A80L,0x092945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_select_item_in_select_clause1355 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_select_expression_in_select_item1398 = new BitSet(new long[]{0x2000000000000022L});
	public static final BitSet FOLLOW_AS_in_select_item1402 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_result_variable_in_select_item1406 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_select_expression1419 = new BitSet(new long[]{0x0000000000000002L,0x000000000000002BL});
	public static final BitSet FOLLOW_set_in_select_expression1422 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1438 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1448 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1466 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_select_expression1474 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_123_in_select_expression1482 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_select_expression1484 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1485 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_select_expression1486 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_constructor_expression_in_select_expression1494 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_117_in_constructor_expression1505 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_constructor_name_in_constructor_expression1507 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_constructor_expression1509 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1511 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_constructor_expression1514 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1516 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_constructor_expression1520 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_constructor_item1531 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_constructor_item1539 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_constructor_item1547 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_constructor_item1555 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1566 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1568 = new BitSet(new long[]{0xA000008078442A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1570 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_aggregate_expression1574 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1575 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_aggregate_expression1609 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1611 = new BitSet(new long[]{0x2000000000042000L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1613 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_count_argument_in_aggregate_expression1617 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1619 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_aggregate_expression1654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_count_argument1691 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_count_argument1695 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_142_in_where_clause1708 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000030BB4L});
	public static final BitSet FOLLOW_conditional_expression_in_where_clause1710 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_groupby_clause1732 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_BY_in_groupby_clause1734 = new BitSet(new long[]{0x2000000000040000L,0x0000004000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1736 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_groupby_clause1739 = new BitSet(new long[]{0x2000000000040000L,0x0000004000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1741 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_path_expression_in_groupby_item1775 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_groupby_item1779 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extract_function_in_groupby_item1783 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HAVING_in_having_clause1794 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000030BB4L});
	public static final BitSet FOLLOW_conditional_expression_in_having_clause1796 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderby_clause1807 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_BY_in_orderby_clause1809 = new BitSet(new long[]{0xA00000C07C440A80L,0x010955407E14204AL,0x0000000000031B34L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1811 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_orderby_clause1814 = new BitSet(new long[]{0xA00000C07C440A80L,0x010955407E14204AL,0x0000000000031B34L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1816 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1850 = new BitSet(new long[]{0x0000000000001042L,0x0600000000000000L});
	public static final BitSet FOLLOW_sort_in_orderby_item1852 = new BitSet(new long[]{0x0000000000000002L,0x0600000000000000L});
	public static final BitSet FOLLOW_sortNulls_in_orderby_item1855 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_orderby_variable1890 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_orderby_variable1894 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_orderby_variable1898 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_orderby_variable1902 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_orderby_variable1906 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_subquery1953 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_129_in_subquery1955 = new BitSet(new long[]{0xA00000C07C442A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_simple_select_clause_in_subquery1957 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_subquery_from_clause_in_subquery1959 = new BitSet(new long[]{0x00000008000C0000L,0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_where_clause_in_subquery1962 = new BitSet(new long[]{0x00000008000C0000L});
	public static final BitSet FOLLOW_groupby_clause_in_subquery1967 = new BitSet(new long[]{0x0000000800080000L});
	public static final BitSet FOLLOW_having_clause_in_subquery1972 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_subquery1978 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_subquery_from_clause2028 = new BitSet(new long[]{0x2000000000100000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause2030 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_subquery_from_clause2033 = new BitSet(new long[]{0x2000000000100000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause2035 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration2073 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration2081 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_AS_in_subselect_identification_variable_declaration2083 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration2085 = new BitSet(new long[]{0x0000000001A00002L});
	public static final BitSet FOLLOW_join_in_subselect_identification_variable_declaration2088 = new BitSet(new long[]{0x0000000001A00002L});
	public static final BitSet FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration2098 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression2109 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_derived_path_expression2110 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_path_expression2111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression2119 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_derived_path_expression2120 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_path_expression2121 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_derived_path_in_general_derived_path2132 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_treated_derived_path_in_general_derived_path2140 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_general_derived_path2142 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_general_derived_path2143 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_simple_derived_path2161 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_135_in_treated_derived_path2178 = new BitSet(new long[]{0x2000000000000000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_general_derived_path_in_treated_derived_path2179 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_AS_in_treated_derived_path2181 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_subtype_in_treated_derived_path2183 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_treated_derived_path2185 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_derived_collection_member_declaration2196 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2198 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_derived_collection_member_declaration2199 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2201 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_derived_collection_member_declaration2203 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_collection_member_declaration2206 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause2219 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause2223 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_simple_select_expression2263 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_select_expression2271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression2279 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_select_expression2287 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_scalar_expression2298 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_scalar_expression2306 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_scalar_expression2314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_scalar_expression2322 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_scalar_expression2330 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_scalar_expression2338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_scalar_expression2346 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2358 = new BitSet(new long[]{0x0000000100000002L});
	public static final BitSet FOLLOW_OR_in_conditional_expression2362 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000030BB4L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2364 = new BitSet(new long[]{0x0000000100000002L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2378 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_conditional_term2382 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000030BB4L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2384 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_NOT_in_conditional_factor2398 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000030BB4L});
	public static final BitSet FOLLOW_conditional_primary_in_conditional_factor2402 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_conditional_primary2413 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_conditional_primary2437 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000030BB4L});
	public static final BitSet FOLLOW_conditional_expression_in_conditional_primary2438 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_conditional_primary2439 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression2450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_simple_cond_expression2458 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_simple_cond_expression2466 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_simple_cond_expression2474 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression2482 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2490 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression2498 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression2506 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression2514 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2527 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2535 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2543 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2559 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_78_in_date_between_macro_expression2571 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2573 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2575 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_between_macro_expression2577 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
	public static final BitSet FOLLOW_118_in_date_between_macro_expression2579 = new BitSet(new long[]{0x0000000000000000L,0x000000000000000EL});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2582 = new BitSet(new long[]{0x0000000000400000L,0x0000000000000040L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2590 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_between_macro_expression2594 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
	public static final BitSet FOLLOW_118_in_date_between_macro_expression2596 = new BitSet(new long[]{0x0000000000000000L,0x000000000000000EL});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2599 = new BitSet(new long[]{0x0000000000400000L,0x0000000000000040L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2607 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_between_macro_expression2611 = new BitSet(new long[]{0x0000000000000000L,0x0014020080000000L,0x0000000000008001L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2613 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2636 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_80_in_date_before_macro_expression2648 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2650 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2652 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_before_macro_expression2654 = new BitSet(new long[]{0xA000000040040000L,0x0000000000002000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2657 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2661 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2664 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_79_in_date_after_macro_expression2676 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2678 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2680 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_after_macro_expression2682 = new BitSet(new long[]{0xA000000040040000L,0x0000000000002000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2685 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2689 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2692 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_date_equals_macro_expression2704 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2706 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2708 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_equals_macro_expression2710 = new BitSet(new long[]{0xA000000040040000L,0x0000000000002000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2713 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2717 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2720 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_83_in_date_today_macro_expression2732 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2734 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2736 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2738 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2751 = new BitSet(new long[]{0x0000000080000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2754 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_between_expression2758 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2760 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2762 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2764 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2772 = new BitSet(new long[]{0x0000000080000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2775 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_between_expression2779 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2781 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2783 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2785 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2793 = new BitSet(new long[]{0x0000000080000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2796 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_between_expression2800 = new BitSet(new long[]{0xA000008078040A80L,0x0100014076042000L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2802 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2804 = new BitSet(new long[]{0xA000008078040A80L,0x0100014076042000L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2806 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2818 = new BitSet(new long[]{0x0000000080100000L});
	public static final BitSet FOLLOW_type_discriminator_in_in_expression2822 = new BitSet(new long[]{0x0000000080100000L});
	public static final BitSet FOLLOW_identification_variable_in_in_expression2826 = new BitSet(new long[]{0x0000000080100000L});
	public static final BitSet FOLLOW_NOT_in_in_expression2830 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_IN_in_in_expression2834 = new BitSet(new long[]{0x8000000048000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2850 = new BitSet(new long[]{0x8000004040400000L,0x0000000000042040L});
	public static final BitSet FOLLOW_in_item_in_in_expression2852 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_in_expression2855 = new BitSet(new long[]{0x8000004040400000L,0x0000000000042040L});
	public static final BitSet FOLLOW_in_item_in_in_expression2857 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2861 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_in_expression2877 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_valued_input_parameter_in_in_expression2893 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2909 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2911 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2913 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_in_item2941 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_in_item2945 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_single_valued_input_parameter_in_in_item2949 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_function_in_in_item2953 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2964 = new BitSet(new long[]{0x0000000080000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_NOT_in_like_expression2967 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_111_in_like_expression2971 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2974 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
	public static final BitSet FOLLOW_pattern_value_in_like_expression2978 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
	public static final BitSet FOLLOW_input_parameter_in_like_expression2982 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
	public static final BitSet FOLLOW_100_in_like_expression2985 = new BitSet(new long[]{0x0000024000000000L});
	public static final BitSet FOLLOW_escape_character_in_like_expression2987 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_null_comparison_expression3001 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression3005 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_null_comparison_expression3009 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_107_in_null_comparison_expression3012 = new BitSet(new long[]{0x0000000080000000L,0x0080000000000000L});
	public static final BitSet FOLLOW_NOT_in_null_comparison_expression3015 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
	public static final BitSet FOLLOW_119_in_null_comparison_expression3019 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression3030 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_107_in_empty_collection_comparison_expression3032 = new BitSet(new long[]{0x0000000080000000L,0x0000000200000000L});
	public static final BitSet FOLLOW_NOT_in_empty_collection_comparison_expression3035 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
	public static final BitSet FOLLOW_97_in_empty_collection_comparison_expression3039 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_or_value_expression_in_collection_member_expression3050 = new BitSet(new long[]{0x0000000080000000L,0x0002000000000000L});
	public static final BitSet FOLLOW_NOT_in_collection_member_expression3054 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
	public static final BitSet FOLLOW_113_in_collection_member_expression3058 = new BitSet(new long[]{0x2000000000040000L,0x1000000000000000L});
	public static final BitSet FOLLOW_124_in_collection_member_expression3061 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_expression3065 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_or_value_expression3076 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression3084 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_entity_or_value_expression3092 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_or_value_expression3103 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_or_value_expression3111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simple_entity_or_value_expression3119 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_exists_expression3131 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_101_in_exists_expression3135 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_subquery_in_exists_expression3137 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_all_or_any_expression3148 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_subquery_in_all_or_any_expression3161 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3172 = new BitSet(new long[]{0x0000000000000000L,0x8000000000001F80L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3175 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E642000L,0x0000000000000928L});
	public static final BitSet FOLLOW_127_in_comparison_expression3179 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E642000L,0x0000000000000928L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3196 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_comparison_expression3198 = new BitSet(new long[]{0xA000000048040200L,0x0100014006642000L,0x0000000000030008L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3207 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3211 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3220 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_comparison_expression3222 = new BitSet(new long[]{0xA000000048040200L,0x0100000004602000L,0x0000000000000008L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3229 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3233 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3242 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001F80L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3244 = new BitSet(new long[]{0xA000008078040A80L,0x0100014076642000L,0x0000000000000008L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3247 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3260 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_comparison_expression3262 = new BitSet(new long[]{0xA000000040040000L,0x0000000000602000L,0x0000000000000008L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3275 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3284 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_comparison_expression3286 = new BitSet(new long[]{0xA000000040000000L,0x0000000000002000L,0x0000000000000200L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3294 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3302 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001F80L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3304 = new BitSet(new long[]{0xA000008078440A80L,0x010945400674204AL,0x000000000000001CL});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3307 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3311 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3375 = new BitSet(new long[]{0x0000000000000000L,0x000000000000000AL});
	public static final BitSet FOLLOW_set_in_arithmetic_expression3378 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3386 = new BitSet(new long[]{0x0000000000000002L,0x000000000000000AL});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3396 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3407 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000021L});
	public static final BitSet FOLLOW_set_in_arithmetic_term3410 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3419 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000021L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3429 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor3452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_arithmetic_primary3463 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_decimal_literal_in_arithmetic_primary3471 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary3479 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary3487 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_arithmetic_primary3488 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary3489 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary3497 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary3505 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary3513 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_arithmetic_primary3521 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_arithmetic_primary3529 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_arithmetic_primary3537 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_arithmetic_primary3545 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_string_expression3556 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_string_expression3564 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_string_expression3572 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_strings_in_string_expression3580 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_string_expression3588 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_string_expression3596 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_string_expression3604 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_string_expression3612 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_string_expression3620 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_datetime_expression3631 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_datetime_expression3639 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_expression3647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_datetime_expression3655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_datetime_expression3663 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_datetime_expression3671 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_datetime_expression3679 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_datetime_expression3687 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_datetime_expression3695 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_boolean_expression3706 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_literal_in_boolean_expression3714 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_boolean_expression3722 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_boolean_expression3730 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_boolean_expression3738 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_boolean_expression3746 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_boolean_expression3754 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_enum_expression3765 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_literal_in_enum_expression3773 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_enum_expression3781 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_enum_expression3789 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_enum_expression3797 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_expression3808 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3816 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3835 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_entity_type_expression3846 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_literal_in_entity_type_expression3854 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_entity_type_expression3862 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_137_in_type_discriminator3873 = new BitSet(new long[]{0xA000000040040000L,0x0000100000002000L,0x0000000000001000L});
	public static final BitSet FOLLOW_general_identification_variable_in_type_discriminator3876 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_path_expression_in_type_discriminator3880 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_input_parameter_in_type_discriminator3884 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_type_discriminator3887 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_110_in_functions_returning_numerics3898 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3899 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3900 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_112_in_functions_returning_numerics3908 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3910 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_numerics3911 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3913 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_numerics3915 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3916 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3919 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_84_in_functions_returning_numerics3927 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3928 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3929 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_132_in_functions_returning_numerics3937 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3938 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3939 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_115_in_functions_returning_numerics3947 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3948 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_numerics3949 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3951 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3952 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_130_in_functions_returning_numerics3960 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3961 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3962 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_106_in_functions_returning_numerics3970 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_functions_returning_numerics3971 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3972 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_91_in_functions_returning_strings4010 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4011 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_strings4012 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4014 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_strings4017 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4019 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4022 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_133_in_functions_returning_strings4030 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4032 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_strings4033 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings4035 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_strings4038 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings4040 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4043 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_136_in_functions_returning_strings4051 = new BitSet(new long[]{0xA00002C07C040A80L,0x010021C00F042000L,0x0000000000000960L});
	public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings4054 = new BitSet(new long[]{0x0000020000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_trim_character_in_functions_returning_strings4059 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_103_in_functions_returning_strings4063 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4067 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4069 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LOWER_in_functions_returning_strings4077 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings4079 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4080 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4081 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_139_in_functions_returning_strings4089 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4090 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4091 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_104_in_function_invocation4121 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_function_name_in_function_invocation4122 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_function_invocation4125 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_function_arg_in_function_invocation4127 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_function_invocation4131 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_function_arg4142 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_function_arg4150 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_function_arg4158 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_function_arg4166 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_case_expression_in_case_expression4177 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_case_expression_in_case_expression4185 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coalesce_expression_in_case_expression4193 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullif_expression_in_case_expression4201 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CASE_in_general_case_expression4212 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4214 = new BitSet(new long[]{0x1000000000004000L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4217 = new BitSet(new long[]{0x1000000000004000L});
	public static final BitSet FOLLOW_ELSE_in_general_case_expression4221 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_general_case_expression4223 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_END_in_general_case_expression4225 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WHEN_in_when_clause4236 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000030BB4L});
	public static final BitSet FOLLOW_conditional_expression_in_when_clause4238 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_THEN_in_when_clause4240 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_when_clause4242 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CASE_in_simple_case_expression4253 = new BitSet(new long[]{0x2000000000040000L,0x0000000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_case_operand_in_simple_case_expression4255 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4257 = new BitSet(new long[]{0x1000000000004000L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4260 = new BitSet(new long[]{0x1000000000004000L});
	public static final BitSet FOLLOW_ELSE_in_simple_case_expression4264 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_case_expression4266 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_END_in_simple_case_expression4268 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_case_operand4279 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_case_operand4287 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WHEN_in_simple_when_clause4298 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4300 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_THEN_in_simple_when_clause4302 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4304 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_90_in_coalesce_expression4315 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4316 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_coalesce_expression4319 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4321 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_coalesce_expression4324 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_120_in_nullif_expression4335 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4336 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_nullif_expression4338 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4340 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_nullif_expression4341 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_89_in_extension_functions4353 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_function_arg_in_extension_functions4355 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_WORD_in_extension_functions4357 = new BitSet(new long[]{0x0000000808000000L});
	public static final BitSet FOLLOW_LPAREN_in_extension_functions4360 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4361 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_extension_functions4364 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4366 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4371 = new BitSet(new long[]{0x0000000808000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4375 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extract_function_in_extension_functions4383 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_function_in_extension_functions4391 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_extract_function4403 = new BitSet(new long[]{0x0000000000000000L,0x4014020880000000L,0x000000000000A001L});
	public static final BitSet FOLLOW_date_part_in_extract_function4405 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_103_in_extract_function4407 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_function_arg_in_extract_function4409 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_extract_function4411 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_82_in_enum_function4423 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_enum_function4425 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_enum_value_literal_in_enum_function4427 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_enum_function4429 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_77_in_input_parameter4496 = new BitSet(new long[]{0x0000000000400000L,0x0000000000000040L});
	public static final BitSet FOLLOW_numeric_literal_in_input_parameter4498 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter4521 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_63_in_input_parameter4542 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_WORD_in_input_parameter4544 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_146_in_input_parameter4546 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_literal4574 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4586 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_literal4598 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_field4631 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_129_in_field4635 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_field4639 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_field4643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_field4647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MAX_in_field4651 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MIN_in_field4655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SUM_in_field4659 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AVG_in_field4663 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_field4667 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AS_in_field4671 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_113_in_field4675 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CASE_in_field4679 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_part_in_field4683 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4711 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_parameter_name4714 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4717 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_TRIM_CHARACTER_in_trim_character4747 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string_literal4758 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_70_in_numeric_literal4770 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal4774 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_decimal_literal4786 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_decimal_literal4788 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_decimal_literal4790 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_object_field4801 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_embeddable_object_field4812 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_collection_valued_field4823 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_name4834 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_subtype4845 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_type_literal4856 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_function_name4867 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_state_field4878 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_result_variable4889 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_superquery_identification_variable4900 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_date_time_timestamp_literal4911 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_pattern_value4922 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_collection_valued_input_parameter4933 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_single_valued_input_parameter4944 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_value_literal4955 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_enum_value_literal4958 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_WORD_in_enum_value_literal4961 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_field_in_synpred21_JPA2970 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred30_JPA21160 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred33_JPA21286 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_synpred34_JPA21294 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred43_JPA21419 = new BitSet(new long[]{0x0000000000000002L,0x000000000000002BL});
	public static final BitSet FOLLOW_set_in_synpred43_JPA21422 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000030B34L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred43_JPA21438 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred44_JPA21448 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred45_JPA21466 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred46_JPA21474 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred49_JPA21531 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred50_JPA21539 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred51_JPA21547 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_synpred53_JPA21566 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred53_JPA21568 = new BitSet(new long[]{0xA000008078442A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred53_JPA21570 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred53_JPA21574 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred53_JPA21575 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_synpred55_JPA21609 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred55_JPA21611 = new BitSet(new long[]{0x2000000000042000L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred55_JPA21613 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_count_argument_in_synpred55_JPA21617 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred55_JPA21619 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred67_JPA21890 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_synpred68_JPA21894 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_synpred69_JPA21898 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred70_JPA21902 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_synpred80_JPA22109 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_synpred80_JPA22110 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_synpred80_JPA22111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred85_JPA22263 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred86_JPA22271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred87_JPA22279 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred88_JPA22298 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred89_JPA22306 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred90_JPA22314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred91_JPA22322 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred92_JPA22330 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_synpred93_JPA22338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_synpred96_JPA22398 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_synpred97_JPA22413 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_synpred98_JPA22450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_synpred99_JPA22458 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_synpred100_JPA22466 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_synpred101_JPA22474 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_synpred102_JPA22482 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred103_JPA22490 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_synpred104_JPA22498 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred123_JPA22751 = new BitSet(new long[]{0x0000000080000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_NOT_in_synpred123_JPA22754 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_synpred123_JPA22758 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred123_JPA22760 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred123_JPA22762 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred123_JPA22764 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred125_JPA22772 = new BitSet(new long[]{0x0000000080000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_NOT_in_synpred125_JPA22775 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_synpred125_JPA22779 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_synpred125_JPA22781 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred125_JPA22783 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_synpred125_JPA22785 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred138_JPA22974 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_pattern_value_in_synpred139_JPA22978 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred141_JPA23001 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred149_JPA23103 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred156_JPA23172 = new BitSet(new long[]{0x0000000000000000L,0x8000000000001F80L});
	public static final BitSet FOLLOW_comparison_operator_in_synpred156_JPA23175 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E642000L,0x0000000000000928L});
	public static final BitSet FOLLOW_127_in_synpred156_JPA23179 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E642000L,0x0000000000000928L});
	public static final BitSet FOLLOW_string_expression_in_synpred156_JPA23183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred156_JPA23187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred159_JPA23196 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_synpred159_JPA23198 = new BitSet(new long[]{0xA000000048040200L,0x0100014006642000L,0x0000000000030008L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred159_JPA23207 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred159_JPA23211 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred162_JPA23220 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_synpred162_JPA23222 = new BitSet(new long[]{0xA000000048040200L,0x0100000004602000L,0x0000000000000008L});
	public static final BitSet FOLLOW_enum_expression_in_synpred162_JPA23229 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred162_JPA23233 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred164_JPA23242 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001F80L});
	public static final BitSet FOLLOW_comparison_operator_in_synpred164_JPA23244 = new BitSet(new long[]{0xA000008078040A80L,0x0100014076642000L,0x0000000000000008L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred164_JPA23247 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred164_JPA23251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred167_JPA23260 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_synpred167_JPA23262 = new BitSet(new long[]{0xA000000040040000L,0x0000000000602000L,0x0000000000000008L});
	public static final BitSet FOLLOW_entity_expression_in_synpred167_JPA23271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred167_JPA23275 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred169_JPA23284 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_synpred169_JPA23286 = new BitSet(new long[]{0xA000000040000000L,0x0000000000002000L,0x0000000000000200L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred169_JPA23294 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred178_JPA23375 = new BitSet(new long[]{0x0000000000000000L,0x000000000000000AL});
	public static final BitSet FOLLOW_set_in_synpred178_JPA23378 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred178_JPA23386 = new BitSet(new long[]{0x0000000000000002L,0x000000000000000AL});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred181_JPA23407 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000021L});
	public static final BitSet FOLLOW_set_in_synpred181_JPA23410 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred181_JPA23419 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000021L});
	public static final BitSet FOLLOW_decimal_literal_in_synpred185_JPA23471 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_synpred186_JPA23479 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred187_JPA23487 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred187_JPA23488 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred187_JPA23489 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred190_JPA23513 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred192_JPA23529 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred198_JPA23588 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred200_JPA23604 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred202_JPA23631 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred205_JPA23655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred207_JPA23671 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_synpred209_JPA23687 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred247_JPA24142 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred248_JPA24150 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_synpred249_JPA24158 = new BitSet(new long[]{0x0000000000000002L});

	@Override
	public void emitErrorMessage(String msg) {
		//do nothing
	}

	protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
		throw new MismatchedTokenException(ttype, input);
	}
}
