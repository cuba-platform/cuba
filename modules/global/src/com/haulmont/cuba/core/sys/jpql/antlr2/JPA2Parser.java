// $ANTLR 3.5.2 JPA2.g 2017-11-29 12:50:16

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
		"COMMENT", "COUNT", "DESC", "DISTINCT", "ESCAPE_CHARACTER", "FETCH", "GROUP", 
		"HAVING", "IN", "INNER", "INT_NUMERAL", "JOIN", "LEFT", "LINE_COMMENT", 
		"LOWER", "LPAREN", "MAX", "MIN", "NAMED_PARAMETER", "NOT", "OR", "ORDER", 
		"OUTER", "RPAREN", "RUSSIAN_SYMBOLS", "SET", "STRING_LITERAL", "SUM", 
		"TRIM_CHARACTER", "T_AGGREGATE_EXPR", "T_COLLECTION_MEMBER", "T_CONDITION", 
		"T_ENUM_MACROS", "T_GROUP_BY", "T_ID_VAR", "T_JOIN_VAR", "T_ORDER_BY", 
		"T_ORDER_BY_FIELD", "T_PARAMETER", "T_QUERY", "T_SELECTED_ENTITY", "T_SELECTED_FIELD", 
		"T_SELECTED_ITEM", "T_SELECTED_ITEMS", "T_SIMPLE_CONDITION", "T_SOURCE", 
		"T_SOURCES", "WORD", "WS", "'${'", "'*'", "'+'", "','", "'-'", "'.'", 
		"'/'", "'0x'", "'<'", "'<='", "'<>'", "'='", "'>'", "'>='", "'?'", "'@BETWEEN'", 
		"'@DATEAFTER'", "'@DATEBEFORE'", "'@DATEEQUALS'", "'@ENUM'", "'@TODAY'", 
		"'ABS('", "'ALL'", "'ANY'", "'BETWEEN'", "'BOTH'", "'CASE'", "'CAST('", 
		"'COALESCE('", "'CONCAT('", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", 
		"'DAY'", "'DELETE'", "'ELSE'", "'EMPTY'", "'END'", "'ENTRY('", "'EPOCH'", 
		"'ESCAPE'", "'EXISTS'", "'EXTRACT('", "'FROM'", "'FUNCTION('", "'HOUR'", 
		"'INDEX('", "'IS'", "'KEY('", "'LEADING'", "'LENGTH('", "'LIKE'", "'LOCATE('", 
		"'MEMBER'", "'MINUTE'", "'MOD('", "'MONTH'", "'NEW'", "'NOW'", "'NULL'", 
		"'NULLIF('", "'NULLS FIRST'", "'NULLS LAST'", "'OBJECT'", "'OF'", "'ON'", 
		"'QUARTER'", "'REGEXP'", "'SECOND'", "'SELECT'", "'SIZE('", "'SOME'", 
		"'SQRT('", "'SUBSTRING('", "'THEN'", "'TRAILING'", "'TREAT('", "'TRIM('", 
		"'TYPE('", "'UPDATE'", "'UPPER('", "'USER_TIMEZONE'", "'VALUE('", "'WEEK'", 
		"'WHEN'", "'WHERE'", "'YEAR'", "'false'", "'true'", "'}'"
	};
	public static final int EOF=-1;
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
	public static final int T__145=145;
	public static final int T__146=146;
	public static final int T__147=147;
	public static final int AND=4;
	public static final int AS=5;
	public static final int ASC=6;
	public static final int AVG=7;
	public static final int BY=8;
	public static final int COMMENT=9;
	public static final int COUNT=10;
	public static final int DESC=11;
	public static final int DISTINCT=12;
	public static final int ESCAPE_CHARACTER=13;
	public static final int FETCH=14;
	public static final int GROUP=15;
	public static final int HAVING=16;
	public static final int IN=17;
	public static final int INNER=18;
	public static final int INT_NUMERAL=19;
	public static final int JOIN=20;
	public static final int LEFT=21;
	public static final int LINE_COMMENT=22;
	public static final int LOWER=23;
	public static final int LPAREN=24;
	public static final int MAX=25;
	public static final int MIN=26;
	public static final int NAMED_PARAMETER=27;
	public static final int NOT=28;
	public static final int OR=29;
	public static final int ORDER=30;
	public static final int OUTER=31;
	public static final int RPAREN=32;
	public static final int RUSSIAN_SYMBOLS=33;
	public static final int SET=34;
	public static final int STRING_LITERAL=35;
	public static final int SUM=36;
	public static final int TRIM_CHARACTER=37;
	public static final int T_AGGREGATE_EXPR=38;
	public static final int T_COLLECTION_MEMBER=39;
	public static final int T_CONDITION=40;
	public static final int T_ENUM_MACROS=41;
	public static final int T_GROUP_BY=42;
	public static final int T_ID_VAR=43;
	public static final int T_JOIN_VAR=44;
	public static final int T_ORDER_BY=45;
	public static final int T_ORDER_BY_FIELD=46;
	public static final int T_PARAMETER=47;
	public static final int T_QUERY=48;
	public static final int T_SELECTED_ENTITY=49;
	public static final int T_SELECTED_FIELD=50;
	public static final int T_SELECTED_ITEM=51;
	public static final int T_SELECTED_ITEMS=52;
	public static final int T_SIMPLE_CONDITION=53;
	public static final int T_SOURCE=54;
	public static final int T_SOURCES=55;
	public static final int WORD=56;
	public static final int WS=57;

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
	// JPA2.g:82:1: ql_statement : ( select_statement | update_statement | delete_statement );
	public final JPA2Parser.ql_statement_return ql_statement() throws RecognitionException {
		JPA2Parser.ql_statement_return retval = new JPA2Parser.ql_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope select_statement1 =null;
		ParserRuleReturnScope update_statement2 =null;
		ParserRuleReturnScope delete_statement3 =null;


		try {
			// JPA2.g:83:5: ( select_statement | update_statement | delete_statement )
			int alt1=3;
			switch ( input.LA(1) ) {
			case 127:
				{
				alt1=1;
				}
				break;
			case 137:
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
					// JPA2.g:83:7: select_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_select_statement_in_ql_statement456);
					select_statement1=select_statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, select_statement1.getTree());

					}
					break;
				case 2 :
					// JPA2.g:83:26: update_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_update_statement_in_ql_statement460);
					update_statement2=update_statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, update_statement2.getTree());

					}
					break;
				case 3 :
					// JPA2.g:83:45: delete_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_delete_statement_in_ql_statement464);
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
	// JPA2.g:85:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
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
		RewriteRuleTokenStream stream_127=new RewriteRuleTokenStream(adaptor,"token 127");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
		RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
		RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");

		try {
			// JPA2.g:86:6: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
			// JPA2.g:86:8: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )?
			{
			sl=(Token)match(input,127,FOLLOW_127_in_select_statement479); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_127.add(sl);

			pushFollow(FOLLOW_select_clause_in_select_statement481);
			select_clause4=select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_clause.add(select_clause4.getTree());
			pushFollow(FOLLOW_from_clause_in_select_statement483);
			from_clause5=from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_from_clause.add(from_clause5.getTree());
			// JPA2.g:86:46: ( where_clause )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==143) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// JPA2.g:86:47: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_select_statement486);
					where_clause6=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause6.getTree());
					}
					break;

			}

			// JPA2.g:86:62: ( groupby_clause )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==GROUP) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// JPA2.g:86:63: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_select_statement491);
					groupby_clause7=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause7.getTree());
					}
					break;

			}

			// JPA2.g:86:80: ( having_clause )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==HAVING) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// JPA2.g:86:81: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_select_statement496);
					having_clause8=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause8.getTree());
					}
					break;

			}

			// JPA2.g:86:97: ( orderby_clause )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==ORDER) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// JPA2.g:86:98: orderby_clause
					{
					pushFollow(FOLLOW_orderby_clause_in_select_statement501);
					orderby_clause9=orderby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause9.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: from_clause, orderby_clause, select_clause, having_clause, where_clause, groupby_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 87:6: -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
			{
				// JPA2.g:87:9: ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, sl), root_1);
				// JPA2.g:87:35: ( select_clause )?
				if ( stream_select_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_select_clause.nextTree());
				}
				stream_select_clause.reset();

				adaptor.addChild(root_1, stream_from_clause.nextTree());
				// JPA2.g:87:64: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:87:80: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:87:98: ( having_clause )?
				if ( stream_having_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_having_clause.nextTree());
				}
				stream_having_clause.reset();

				// JPA2.g:87:115: ( orderby_clause )?
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
	// JPA2.g:89:1: update_statement : up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) ;
	public final JPA2Parser.update_statement_return update_statement() throws RecognitionException {
		JPA2Parser.update_statement_return retval = new JPA2Parser.update_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token up=null;
		ParserRuleReturnScope update_clause10 =null;
		ParserRuleReturnScope where_clause11 =null;

		Object up_tree=null;
		RewriteRuleTokenStream stream_137=new RewriteRuleTokenStream(adaptor,"token 137");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_update_clause=new RewriteRuleSubtreeStream(adaptor,"rule update_clause");

		try {
			// JPA2.g:90:5: (up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) )
			// JPA2.g:90:7: up= 'UPDATE' update_clause ( where_clause )?
			{
			up=(Token)match(input,137,FOLLOW_137_in_update_statement559); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_137.add(up);

			pushFollow(FOLLOW_update_clause_in_update_statement561);
			update_clause10=update_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_clause.add(update_clause10.getTree());
			// JPA2.g:90:33: ( where_clause )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==143) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// JPA2.g:90:34: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_update_statement564);
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
			// 91:5: -> ^( T_QUERY[$up] update_clause ( where_clause )? )
			{
				// JPA2.g:91:8: ^( T_QUERY[$up] update_clause ( where_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, up), root_1);
				adaptor.addChild(root_1, stream_update_clause.nextTree());
				// JPA2.g:91:48: ( where_clause )?
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
	// JPA2.g:92:1: delete_statement : dl= 'DELETE' delete_clause ( where_clause )? -> ^( T_QUERY[$dl] delete_clause ( where_clause )? ) ;
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
			// JPA2.g:93:5: (dl= 'DELETE' delete_clause ( where_clause )? -> ^( T_QUERY[$dl] delete_clause ( where_clause )? ) )
			// JPA2.g:93:7: dl= 'DELETE' delete_clause ( where_clause )?
			{
			dl=(Token)match(input,92,FOLLOW_92_in_delete_statement600); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_92.add(dl);

			pushFollow(FOLLOW_delete_clause_in_delete_statement602);
			delete_clause12=delete_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_delete_clause.add(delete_clause12.getTree());
			// JPA2.g:93:33: ( where_clause )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==143) ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					// JPA2.g:93:34: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_delete_statement605);
					where_clause13=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause13.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: delete_clause, where_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 94:5: -> ^( T_QUERY[$dl] delete_clause ( where_clause )? )
			{
				// JPA2.g:94:8: ^( T_QUERY[$dl] delete_clause ( where_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, dl), root_1);
				adaptor.addChild(root_1, stream_delete_clause.nextTree());
				// JPA2.g:94:48: ( where_clause )?
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
	// JPA2.g:96:1: from_clause : fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) ;
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
		RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
		RewriteRuleSubtreeStream stream_identification_variable_declaration_or_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration_or_collection_member_declaration");

		try {
			// JPA2.g:97:6: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) )
			// JPA2.g:97:8: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )*
			{
			fr=(Token)match(input,101,FOLLOW_101_in_from_clause643); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_101.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_from_clause645);
			identification_variable_declaration14=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration14.getTree());
			// JPA2.g:97:54: ( ',' identification_variable_declaration_or_collection_member_declaration )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==61) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// JPA2.g:97:55: ',' identification_variable_declaration_or_collection_member_declaration
					{
					char_literal15=(Token)match(input,61,FOLLOW_61_in_from_clause648); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_61.add(char_literal15);

					pushFollow(FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause650);
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
			// 98:6: -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
			{
				// JPA2.g:98:9: ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
				// JPA2.g:98:72: ( identification_variable_declaration_or_collection_member_declaration )*
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
	// JPA2.g:99:1: identification_variable_declaration_or_collection_member_declaration : ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) );
	public final JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return identification_variable_declaration_or_collection_member_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return retval = new JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable_declaration17 =null;
		ParserRuleReturnScope collection_member_declaration18 =null;

		RewriteRuleSubtreeStream stream_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule collection_member_declaration");

		try {
			// JPA2.g:100:6: ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) )
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
					// JPA2.g:100:8: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration684);
					identification_variable_declaration17=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration17.getTree());

					}
					break;
				case 2 :
					// JPA2.g:101:8: collection_member_declaration
					{
					pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration693);
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
					// 101:38: -> ^( T_SOURCE collection_member_declaration )
					{
						// JPA2.g:101:41: ^( T_SOURCE collection_member_declaration )
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
	// JPA2.g:103:1: identification_variable_declaration : range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) ;
	public final JPA2Parser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_return retval = new JPA2Parser.identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope range_variable_declaration19 =null;
		ParserRuleReturnScope joined_clause20 =null;

		RewriteRuleSubtreeStream stream_range_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration");
		RewriteRuleSubtreeStream stream_joined_clause=new RewriteRuleSubtreeStream(adaptor,"rule joined_clause");

		try {
			// JPA2.g:104:6: ( range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) )
			// JPA2.g:104:8: range_variable_declaration ( joined_clause )*
			{
			pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration717);
			range_variable_declaration19=range_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range_variable_declaration.add(range_variable_declaration19.getTree());
			// JPA2.g:104:35: ( joined_clause )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==INNER||(LA10_0 >= JOIN && LA10_0 <= LEFT)) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// JPA2.g:104:35: joined_clause
					{
					pushFollow(FOLLOW_joined_clause_in_identification_variable_declaration719);
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
			// elements: joined_clause, range_variable_declaration
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 105:6: -> ^( T_SOURCE range_variable_declaration ( joined_clause )* )
			{
				// JPA2.g:105:9: ^( T_SOURCE range_variable_declaration ( joined_clause )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_1);
				adaptor.addChild(root_1, stream_range_variable_declaration.nextTree());
				// JPA2.g:105:68: ( joined_clause )*
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
	// JPA2.g:106:1: join_section : ( joined_clause )* ;
	public final JPA2Parser.join_section_return join_section() throws RecognitionException {
		JPA2Parser.join_section_return retval = new JPA2Parser.join_section_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope joined_clause21 =null;


		try {
			// JPA2.g:106:14: ( ( joined_clause )* )
			// JPA2.g:107:5: ( joined_clause )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:107:5: ( joined_clause )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( (LA11_0==INNER||(LA11_0 >= JOIN && LA11_0 <= LEFT)) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// JPA2.g:107:5: joined_clause
					{
					pushFollow(FOLLOW_joined_clause_in_join_section750);
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
	// JPA2.g:108:1: joined_clause : ( join | fetch_join );
	public final JPA2Parser.joined_clause_return joined_clause() throws RecognitionException {
		JPA2Parser.joined_clause_return retval = new JPA2Parser.joined_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope join22 =null;
		ParserRuleReturnScope fetch_join23 =null;


		try {
			// JPA2.g:108:15: ( join | fetch_join )
			int alt12=2;
			switch ( input.LA(1) ) {
			case LEFT:
				{
				int LA12_1 = input.LA(2);
				if ( (LA12_1==OUTER) ) {
					int LA12_4 = input.LA(3);
					if ( (LA12_4==JOIN) ) {
						int LA12_3 = input.LA(4);
						if ( (LA12_3==GROUP||LA12_3==WORD||LA12_3==134) ) {
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
					if ( (LA12_3==GROUP||LA12_3==WORD||LA12_3==134) ) {
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
					if ( (LA12_3==GROUP||LA12_3==WORD||LA12_3==134) ) {
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
				if ( (LA12_3==GROUP||LA12_3==WORD||LA12_3==134) ) {
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
					// JPA2.g:108:17: join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_join_in_joined_clause758);
					join22=join();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join22.getTree());

					}
					break;
				case 2 :
					// JPA2.g:108:24: fetch_join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_fetch_join_in_joined_clause762);
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
	// JPA2.g:109:1: range_variable_declaration : entity_name ( AS )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) ;
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
			// JPA2.g:110:6: ( entity_name ( AS )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) )
			// JPA2.g:110:8: entity_name ( AS )? identification_variable
			{
			pushFollow(FOLLOW_entity_name_in_range_variable_declaration774);
			entity_name24=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_entity_name.add(entity_name24.getTree());
			// JPA2.g:110:20: ( AS )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==AS) ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// JPA2.g:110:21: AS
					{
					AS25=(Token)match(input,AS,FOLLOW_AS_in_range_variable_declaration777); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS25);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_range_variable_declaration781);
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
			// 111:6: -> ^( T_ID_VAR[$identification_variable.text] entity_name )
			{
				// JPA2.g:111:9: ^( T_ID_VAR[$identification_variable.text] entity_name )
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
	// JPA2.g:112:1: join : join_spec join_association_path_expression ( AS )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? ) ;
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
		RewriteRuleTokenStream stream_123=new RewriteRuleTokenStream(adaptor,"token 123");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");
		RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");

		try {
			// JPA2.g:113:6: ( join_spec join_association_path_expression ( AS )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? ) )
			// JPA2.g:113:8: join_spec join_association_path_expression ( AS )? identification_variable ( 'ON' conditional_expression )?
			{
			pushFollow(FOLLOW_join_spec_in_join810);
			join_spec27=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_spec.add(join_spec27.getTree());
			pushFollow(FOLLOW_join_association_path_expression_in_join812);
			join_association_path_expression28=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression28.getTree());
			// JPA2.g:113:51: ( AS )?
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==AS) ) {
				alt14=1;
			}
			switch (alt14) {
				case 1 :
					// JPA2.g:113:52: AS
					{
					AS29=(Token)match(input,AS,FOLLOW_AS_in_join815); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS29);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_join819);
			identification_variable30=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable30.getTree());
			// JPA2.g:113:81: ( 'ON' conditional_expression )?
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==123) ) {
				alt15=1;
			}
			switch (alt15) {
				case 1 :
					// JPA2.g:113:82: 'ON' conditional_expression
					{
					string_literal31=(Token)match(input,123,FOLLOW_123_in_join822); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_123.add(string_literal31);

					pushFollow(FOLLOW_conditional_expression_in_join824);
					conditional_expression32=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression32.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: join_association_path_expression, conditional_expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 114:6: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? )
			{
				// JPA2.g:114:9: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new JoinVariableNode(T_JOIN_VAR, (join_spec27!=null?input.toString(join_spec27.start,join_spec27.stop):null), (identification_variable30!=null?input.toString(identification_variable30.start,identification_variable30.stop):null)), root_1);
				adaptor.addChild(root_1, stream_join_association_path_expression.nextTree());
				// JPA2.g:114:121: ( conditional_expression )?
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
	// JPA2.g:115:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
	public final JPA2Parser.fetch_join_return fetch_join() throws RecognitionException {
		JPA2Parser.fetch_join_return retval = new JPA2Parser.fetch_join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal34=null;
		ParserRuleReturnScope join_spec33 =null;
		ParserRuleReturnScope join_association_path_expression35 =null;

		Object string_literal34_tree=null;

		try {
			// JPA2.g:116:6: ( join_spec 'FETCH' join_association_path_expression )
			// JPA2.g:116:8: join_spec 'FETCH' join_association_path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_join_spec_in_fetch_join858);
			join_spec33=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec33.getTree());

			string_literal34=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join860); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal34_tree = (Object)adaptor.create(string_literal34);
			adaptor.addChild(root_0, string_literal34_tree);
			}

			pushFollow(FOLLOW_join_association_path_expression_in_fetch_join862);
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
	// JPA2.g:117:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
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
			// JPA2.g:118:6: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
			// JPA2.g:118:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:118:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
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
					// JPA2.g:118:9: ( 'LEFT' ) ( 'OUTER' )?
					{
					// JPA2.g:118:9: ( 'LEFT' )
					// JPA2.g:118:10: 'LEFT'
					{
					string_literal36=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec876); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal36_tree = (Object)adaptor.create(string_literal36);
					adaptor.addChild(root_0, string_literal36_tree);
					}

					}

					// JPA2.g:118:18: ( 'OUTER' )?
					int alt16=2;
					int LA16_0 = input.LA(1);
					if ( (LA16_0==OUTER) ) {
						alt16=1;
					}
					switch (alt16) {
						case 1 :
							// JPA2.g:118:19: 'OUTER'
							{
							string_literal37=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec880); if (state.failed) return retval;
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
					// JPA2.g:118:31: 'INNER'
					{
					string_literal38=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec886); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal38_tree = (Object)adaptor.create(string_literal38);
					adaptor.addChild(root_0, string_literal38_tree);
					}

					}
					break;

			}

			string_literal39=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec891); if (state.failed) return retval;
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
	// JPA2.g:121:1: join_association_path_expression : ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? AS subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name );
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
		RewriteRuleTokenStream stream_AS=new RewriteRuleTokenStream(adaptor,"token AS");
		RewriteRuleTokenStream stream_134=new RewriteRuleTokenStream(adaptor,"token 134");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_subtype=new RewriteRuleSubtreeStream(adaptor,"rule subtype");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:122:6: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? AS subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name )
			int alt22=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA22_1 = input.LA(2);
				if ( (LA22_1==63) ) {
					alt22=1;
				}
				else if ( (LA22_1==EOF||LA22_1==AS||(LA22_1 >= GROUP && LA22_1 <= HAVING)||LA22_1==INNER||(LA22_1 >= JOIN && LA22_1 <= LEFT)||LA22_1==ORDER||LA22_1==RPAREN||LA22_1==SET||LA22_1==WORD||LA22_1==61||LA22_1==105||LA22_1==143) ) {
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
			case 134:
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
					// JPA2.g:122:8: identification_variable '.' ( field '.' )* ( field )?
					{
					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression905);
					identification_variable40=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable40.getTree());
					char_literal41=(Token)match(input,63,FOLLOW_63_in_join_association_path_expression907); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_63.add(char_literal41);

					// JPA2.g:122:36: ( field '.' )*
					loop18:
					while (true) {
						int alt18=2;
						switch ( input.LA(1) ) {
						case WORD:
							{
							int LA18_1 = input.LA(2);
							if ( (LA18_1==63) ) {
								alt18=1;
							}

							}
							break;
						case 127:
							{
							int LA18_2 = input.LA(2);
							if ( (LA18_2==63) ) {
								alt18=1;
							}

							}
							break;
						case 101:
							{
							int LA18_3 = input.LA(2);
							if ( (LA18_3==63) ) {
								alt18=1;
							}

							}
							break;
						case GROUP:
							{
							int LA18_4 = input.LA(2);
							if ( (LA18_4==63) ) {
								alt18=1;
							}

							}
							break;
						case ORDER:
							{
							int LA18_5 = input.LA(2);
							if ( (LA18_5==63) ) {
								alt18=1;
							}

							}
							break;
						case MAX:
							{
							int LA18_6 = input.LA(2);
							if ( (LA18_6==63) ) {
								alt18=1;
							}

							}
							break;
						case MIN:
							{
							int LA18_7 = input.LA(2);
							if ( (LA18_7==63) ) {
								alt18=1;
							}

							}
							break;
						case SUM:
							{
							int LA18_8 = input.LA(2);
							if ( (LA18_8==63) ) {
								alt18=1;
							}

							}
							break;
						case AVG:
							{
							int LA18_9 = input.LA(2);
							if ( (LA18_9==63) ) {
								alt18=1;
							}

							}
							break;
						case COUNT:
							{
							int LA18_10 = input.LA(2);
							if ( (LA18_10==63) ) {
								alt18=1;
							}

							}
							break;
						case AS:
							{
							int LA18_11 = input.LA(2);
							if ( (LA18_11==63) ) {
								alt18=1;
							}

							}
							break;
						case 111:
							{
							int LA18_12 = input.LA(2);
							if ( (LA18_12==63) ) {
								alt18=1;
							}

							}
							break;
						case 91:
						case 97:
						case 103:
						case 112:
						case 114:
						case 124:
						case 126:
						case 141:
						case 144:
							{
							int LA18_13 = input.LA(2);
							if ( (LA18_13==63) ) {
								alt18=1;
							}

							}
							break;
						}
						switch (alt18) {
						case 1 :
							// JPA2.g:122:37: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression910);
							field42=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field42.getTree());
							char_literal43=(Token)match(input,63,FOLLOW_63_in_join_association_path_expression911); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_63.add(char_literal43);

							}
							break;

						default :
							break loop18;
						}
					}

					// JPA2.g:122:48: ( field )?
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
						case 111:
						case 112:
						case 114:
						case 124:
						case 126:
						case 127:
						case 141:
						case 144:
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
							if ( (LA19_4==EOF||LA19_4==AS||(LA19_4 >= GROUP && LA19_4 <= HAVING)||LA19_4==INNER||(LA19_4 >= JOIN && LA19_4 <= LEFT)||LA19_4==ORDER||LA19_4==RPAREN||LA19_4==SET||LA19_4==WORD||LA19_4==61||LA19_4==105||LA19_4==143) ) {
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
							// JPA2.g:122:48: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression915);
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
					// 123:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:123:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable40!=null?input.toString(identification_variable40.start,identification_variable40.stop):null)), root_1);
						// JPA2.g:123:73: ( field )*
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
					// JPA2.g:124:9: 'TREAT(' identification_variable '.' ( field '.' )* ( field )? AS subtype ')'
					{
					string_literal45=(Token)match(input,134,FOLLOW_134_in_join_association_path_expression950); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_134.add(string_literal45);

					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression952);
					identification_variable46=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable46.getTree());
					char_literal47=(Token)match(input,63,FOLLOW_63_in_join_association_path_expression954); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_63.add(char_literal47);

					// JPA2.g:124:46: ( field '.' )*
					loop20:
					while (true) {
						int alt20=2;
						switch ( input.LA(1) ) {
						case WORD:
							{
							int LA20_1 = input.LA(2);
							if ( (LA20_1==63) ) {
								alt20=1;
							}

							}
							break;
						case 127:
							{
							int LA20_2 = input.LA(2);
							if ( (LA20_2==63) ) {
								alt20=1;
							}

							}
							break;
						case 101:
							{
							int LA20_3 = input.LA(2);
							if ( (LA20_3==63) ) {
								alt20=1;
							}

							}
							break;
						case GROUP:
							{
							int LA20_4 = input.LA(2);
							if ( (LA20_4==63) ) {
								alt20=1;
							}

							}
							break;
						case ORDER:
							{
							int LA20_5 = input.LA(2);
							if ( (LA20_5==63) ) {
								alt20=1;
							}

							}
							break;
						case MAX:
							{
							int LA20_6 = input.LA(2);
							if ( (LA20_6==63) ) {
								alt20=1;
							}

							}
							break;
						case MIN:
							{
							int LA20_7 = input.LA(2);
							if ( (LA20_7==63) ) {
								alt20=1;
							}

							}
							break;
						case SUM:
							{
							int LA20_8 = input.LA(2);
							if ( (LA20_8==63) ) {
								alt20=1;
							}

							}
							break;
						case AVG:
							{
							int LA20_9 = input.LA(2);
							if ( (LA20_9==63) ) {
								alt20=1;
							}

							}
							break;
						case COUNT:
							{
							int LA20_10 = input.LA(2);
							if ( (LA20_10==63) ) {
								alt20=1;
							}

							}
							break;
						case AS:
							{
							int LA20_11 = input.LA(2);
							if ( (LA20_11==63) ) {
								alt20=1;
							}

							}
							break;
						case 111:
							{
							int LA20_12 = input.LA(2);
							if ( (LA20_12==63) ) {
								alt20=1;
							}

							}
							break;
						case 91:
						case 97:
						case 103:
						case 112:
						case 114:
						case 124:
						case 126:
						case 141:
						case 144:
							{
							int LA20_13 = input.LA(2);
							if ( (LA20_13==63) ) {
								alt20=1;
							}

							}
							break;
						}
						switch (alt20) {
						case 1 :
							// JPA2.g:124:47: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression957);
							field48=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field48.getTree());
							char_literal49=(Token)match(input,63,FOLLOW_63_in_join_association_path_expression958); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_63.add(char_literal49);

							}
							break;

						default :
							break loop20;
						}
					}

					// JPA2.g:124:58: ( field )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( (LA21_0==AVG||LA21_0==COUNT||LA21_0==GROUP||(LA21_0 >= MAX && LA21_0 <= MIN)||LA21_0==ORDER||LA21_0==SUM||LA21_0==WORD||LA21_0==91||LA21_0==97||LA21_0==101||LA21_0==103||(LA21_0 >= 111 && LA21_0 <= 112)||LA21_0==114||LA21_0==124||(LA21_0 >= 126 && LA21_0 <= 127)||LA21_0==141||LA21_0==144) ) {
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
							// JPA2.g:124:58: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression962);
							field50=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field50.getTree());
							}
							break;

					}

					AS51=(Token)match(input,AS,FOLLOW_AS_in_join_association_path_expression965); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS51);

					pushFollow(FOLLOW_subtype_in_join_association_path_expression967);
					subtype52=subtype();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subtype.add(subtype52.getTree());
					char_literal53=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_join_association_path_expression969); if (state.failed) return retval; 
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
					// 125:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:125:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable46!=null?input.toString(identification_variable46.start,identification_variable46.stop):null)), root_1);
						// JPA2.g:125:73: ( field )*
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
					// JPA2.g:126:8: entity_name
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_name_in_join_association_path_expression1002);
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
	// JPA2.g:129:1: collection_member_declaration : 'IN' '(' path_expression ')' ( AS )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
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
			// JPA2.g:130:5: ( 'IN' '(' path_expression ')' ( AS )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
			// JPA2.g:130:7: 'IN' '(' path_expression ')' ( AS )? identification_variable
			{
			string_literal55=(Token)match(input,IN,FOLLOW_IN_in_collection_member_declaration1015); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_IN.add(string_literal55);

			char_literal56=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration1016); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal56);

			pushFollow(FOLLOW_path_expression_in_collection_member_declaration1018);
			path_expression57=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_path_expression.add(path_expression57.getTree());
			char_literal58=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration1020); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal58);

			// JPA2.g:130:35: ( AS )?
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0==AS) ) {
				alt23=1;
			}
			switch (alt23) {
				case 1 :
					// JPA2.g:130:36: AS
					{
					AS59=(Token)match(input,AS,FOLLOW_AS_in_collection_member_declaration1023); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS59);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_collection_member_declaration1027);
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
			// 131:5: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
			{
				// JPA2.g:131:8: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
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
	// JPA2.g:133:1: qualified_identification_variable : ( map_field_identification_variable | 'ENTRY(' identification_variable ')' );
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
			// JPA2.g:134:5: ( map_field_identification_variable | 'ENTRY(' identification_variable ')' )
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==106||LA24_0==140) ) {
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
					// JPA2.g:134:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_qualified_identification_variable1056);
					map_field_identification_variable61=map_field_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map_field_identification_variable61.getTree());

					}
					break;
				case 2 :
					// JPA2.g:135:7: 'ENTRY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal62=(Token)match(input,96,FOLLOW_96_in_qualified_identification_variable1064); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal62_tree = (Object)adaptor.create(string_literal62);
					adaptor.addChild(root_0, string_literal62_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_qualified_identification_variable1065);
					identification_variable63=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable63.getTree());

					char_literal64=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_qualified_identification_variable1066); if (state.failed) return retval;
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
	// JPA2.g:136:1: map_field_identification_variable : ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' );
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
			// JPA2.g:136:35: ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' )
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==106) ) {
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
					// JPA2.g:136:37: 'KEY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal65=(Token)match(input,106,FOLLOW_106_in_map_field_identification_variable1073); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal65_tree = (Object)adaptor.create(string_literal65);
					adaptor.addChild(root_0, string_literal65_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1074);
					identification_variable66=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable66.getTree());

					char_literal67=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1075); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal67_tree = (Object)adaptor.create(char_literal67);
					adaptor.addChild(root_0, char_literal67_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:136:72: 'VALUE(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal68=(Token)match(input,140,FOLLOW_140_in_map_field_identification_variable1079); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal68_tree = (Object)adaptor.create(string_literal68);
					adaptor.addChild(root_0, string_literal68_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1080);
					identification_variable69=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable69.getTree());

					char_literal70=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1081); if (state.failed) return retval;
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
	// JPA2.g:139:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
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
		RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:140:5: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
			// JPA2.g:140:8: identification_variable '.' ( field '.' )* ( field )?
			{
			pushFollow(FOLLOW_identification_variable_in_path_expression1095);
			identification_variable71=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable71.getTree());
			char_literal72=(Token)match(input,63,FOLLOW_63_in_path_expression1097); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_63.add(char_literal72);

			// JPA2.g:140:36: ( field '.' )*
			loop26:
			while (true) {
				int alt26=2;
				switch ( input.LA(1) ) {
				case WORD:
					{
					int LA26_1 = input.LA(2);
					if ( (LA26_1==63) ) {
						alt26=1;
					}

					}
					break;
				case 127:
					{
					int LA26_2 = input.LA(2);
					if ( (LA26_2==63) ) {
						alt26=1;
					}

					}
					break;
				case 101:
					{
					int LA26_3 = input.LA(2);
					if ( (LA26_3==63) ) {
						alt26=1;
					}

					}
					break;
				case GROUP:
					{
					int LA26_4 = input.LA(2);
					if ( (LA26_4==63) ) {
						alt26=1;
					}

					}
					break;
				case ORDER:
					{
					int LA26_5 = input.LA(2);
					if ( (LA26_5==63) ) {
						alt26=1;
					}

					}
					break;
				case MAX:
					{
					int LA26_6 = input.LA(2);
					if ( (LA26_6==63) ) {
						alt26=1;
					}

					}
					break;
				case MIN:
					{
					int LA26_7 = input.LA(2);
					if ( (LA26_7==63) ) {
						alt26=1;
					}

					}
					break;
				case SUM:
					{
					int LA26_8 = input.LA(2);
					if ( (LA26_8==63) ) {
						alt26=1;
					}

					}
					break;
				case AVG:
					{
					int LA26_9 = input.LA(2);
					if ( (LA26_9==63) ) {
						alt26=1;
					}

					}
					break;
				case COUNT:
					{
					int LA26_10 = input.LA(2);
					if ( (LA26_10==63) ) {
						alt26=1;
					}

					}
					break;
				case AS:
					{
					int LA26_11 = input.LA(2);
					if ( (LA26_11==63) ) {
						alt26=1;
					}

					}
					break;
				case 111:
					{
					int LA26_12 = input.LA(2);
					if ( (LA26_12==63) ) {
						alt26=1;
					}

					}
					break;
				case 91:
				case 97:
				case 103:
				case 112:
				case 114:
				case 124:
				case 126:
				case 141:
				case 144:
					{
					int LA26_13 = input.LA(2);
					if ( (LA26_13==63) ) {
						alt26=1;
					}

					}
					break;
				}
				switch (alt26) {
				case 1 :
					// JPA2.g:140:37: field '.'
					{
					pushFollow(FOLLOW_field_in_path_expression1100);
					field73=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field73.getTree());
					char_literal74=(Token)match(input,63,FOLLOW_63_in_path_expression1101); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_63.add(char_literal74);

					}
					break;

				default :
					break loop26;
				}
			}

			// JPA2.g:140:48: ( field )?
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
				case 124:
				case 126:
				case 127:
				case 141:
				case 144:
					{
					alt27=1;
					}
					break;
				case 101:
					{
					switch ( input.LA(2) ) {
						case EOF:
						case AND:
						case AS:
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
						case 59:
						case 60:
						case 61:
						case 62:
						case 64:
						case 66:
						case 67:
						case 68:
						case 69:
						case 70:
						case 71:
						case 82:
						case 93:
						case 95:
						case 98:
						case 101:
						case 105:
						case 109:
						case 111:
						case 119:
						case 120:
						case 125:
						case 132:
						case 142:
						case 143:
							{
							alt27=1;
							}
							break;
						case WORD:
							{
							int LA27_9 = input.LA(3);
							if ( (LA27_9==EOF||LA27_9==LPAREN||LA27_9==RPAREN||LA27_9==61||LA27_9==101) ) {
								alt27=1;
							}
							}
							break;
						case IN:
							{
							int LA27_10 = input.LA(3);
							if ( (LA27_10==LPAREN||LA27_10==NAMED_PARAMETER||LA27_10==58||LA27_10==72) ) {
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
					if ( (LA27_4==EOF||(LA27_4 >= AND && LA27_4 <= ASC)||LA27_4==DESC||(LA27_4 >= GROUP && LA27_4 <= INNER)||(LA27_4 >= JOIN && LA27_4 <= LEFT)||(LA27_4 >= NOT && LA27_4 <= ORDER)||LA27_4==RPAREN||LA27_4==SET||LA27_4==WORD||(LA27_4 >= 59 && LA27_4 <= 62)||LA27_4==64||(LA27_4 >= 66 && LA27_4 <= 71)||LA27_4==82||LA27_4==93||LA27_4==95||LA27_4==98||LA27_4==101||LA27_4==105||LA27_4==109||LA27_4==111||(LA27_4 >= 119 && LA27_4 <= 120)||LA27_4==125||LA27_4==132||(LA27_4 >= 142 && LA27_4 <= 143)) ) {
						alt27=1;
					}
					}
					break;
				case ORDER:
					{
					int LA27_5 = input.LA(2);
					if ( (LA27_5==EOF||(LA27_5 >= AND && LA27_5 <= ASC)||LA27_5==DESC||(LA27_5 >= GROUP && LA27_5 <= INNER)||(LA27_5 >= JOIN && LA27_5 <= LEFT)||(LA27_5 >= NOT && LA27_5 <= ORDER)||LA27_5==RPAREN||LA27_5==SET||LA27_5==WORD||(LA27_5 >= 59 && LA27_5 <= 62)||LA27_5==64||(LA27_5 >= 66 && LA27_5 <= 71)||LA27_5==82||LA27_5==93||LA27_5==95||LA27_5==98||LA27_5==101||LA27_5==105||LA27_5==109||LA27_5==111||(LA27_5 >= 119 && LA27_5 <= 120)||LA27_5==125||LA27_5==132||(LA27_5 >= 142 && LA27_5 <= 143)) ) {
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
				case 111:
					{
					switch ( input.LA(2) ) {
						case EOF:
						case AND:
						case AS:
						case ASC:
						case DESC:
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
						case 59:
						case 60:
						case 61:
						case 62:
						case 64:
						case 66:
						case 67:
						case 68:
						case 69:
						case 70:
						case 71:
						case 82:
						case 93:
						case 95:
						case 98:
						case 101:
						case 105:
						case 109:
						case 111:
						case 119:
						case 120:
						case 125:
						case 132:
						case 142:
						case 143:
							{
							alt27=1;
							}
							break;
						case WORD:
							{
							int LA27_11 = input.LA(3);
							if ( (LA27_11==EOF||LA27_11==LPAREN||LA27_11==RPAREN||LA27_11==61||LA27_11==101) ) {
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
					// JPA2.g:140:48: field
					{
					pushFollow(FOLLOW_field_in_path_expression1105);
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
			// 141:5: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
			{
				// JPA2.g:141:8: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable71!=null?input.toString(identification_variable71.start,identification_variable71.stop):null)), root_1);
				// JPA2.g:141:68: ( field )*
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
	// JPA2.g:146:1: general_identification_variable : ( identification_variable | map_field_identification_variable );
	public final JPA2Parser.general_identification_variable_return general_identification_variable() throws RecognitionException {
		JPA2Parser.general_identification_variable_return retval = new JPA2Parser.general_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable76 =null;
		ParserRuleReturnScope map_field_identification_variable77 =null;


		try {
			// JPA2.g:147:5: ( identification_variable | map_field_identification_variable )
			int alt28=2;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==GROUP||LA28_0==WORD) ) {
				alt28=1;
			}
			else if ( (LA28_0==106||LA28_0==140) ) {
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
					// JPA2.g:147:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_general_identification_variable1144);
					identification_variable76=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable76.getTree());

					}
					break;
				case 2 :
					// JPA2.g:148:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_general_identification_variable1152);
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
	// JPA2.g:151:1: update_clause : identification_variable_declaration SET update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* ) ;
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
		RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
		RewriteRuleSubtreeStream stream_update_item=new RewriteRuleSubtreeStream(adaptor,"rule update_item");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:152:5: ( identification_variable_declaration SET update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* ) )
			// JPA2.g:152:7: identification_variable_declaration SET update_item ( ',' update_item )*
			{
			pushFollow(FOLLOW_identification_variable_declaration_in_update_clause1165);
			identification_variable_declaration78=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration78.getTree());
			SET79=(Token)match(input,SET,FOLLOW_SET_in_update_clause1167); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_SET.add(SET79);

			pushFollow(FOLLOW_update_item_in_update_clause1169);
			update_item80=update_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_item.add(update_item80.getTree());
			// JPA2.g:152:59: ( ',' update_item )*
			loop29:
			while (true) {
				int alt29=2;
				int LA29_0 = input.LA(1);
				if ( (LA29_0==61) ) {
					alt29=1;
				}

				switch (alt29) {
				case 1 :
					// JPA2.g:152:60: ',' update_item
					{
					char_literal81=(Token)match(input,61,FOLLOW_61_in_update_clause1172); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_61.add(char_literal81);

					pushFollow(FOLLOW_update_item_in_update_clause1174);
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
			// elements: identification_variable_declaration, update_item, 61, update_item, SET
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 153:5: -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* )
			{
				// JPA2.g:153:8: ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCES), root_1);
				adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
				adaptor.addChild(root_1, new UpdateSetNode(stream_SET.nextToken()));
				adaptor.addChild(root_1, stream_update_item.nextTree());
				// JPA2.g:153:108: ( ',' update_item )*
				while ( stream_61.hasNext()||stream_update_item.hasNext() ) {
					adaptor.addChild(root_1, stream_61.nextNode());
					adaptor.addChild(root_1, stream_update_item.nextTree());
				}
				stream_61.reset();
				stream_update_item.reset();

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
	// JPA2.g:154:1: update_item : path_expression '=' new_value ;
	public final JPA2Parser.update_item_return update_item() throws RecognitionException {
		JPA2Parser.update_item_return retval = new JPA2Parser.update_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal84=null;
		ParserRuleReturnScope path_expression83 =null;
		ParserRuleReturnScope new_value85 =null;

		Object char_literal84_tree=null;

		try {
			// JPA2.g:155:5: ( path_expression '=' new_value )
			// JPA2.g:155:7: path_expression '=' new_value
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_update_item1216);
			path_expression83=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression83.getTree());

			char_literal84=(Token)match(input,69,FOLLOW_69_in_update_item1218); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal84_tree = (Object)adaptor.create(char_literal84);
			adaptor.addChild(root_0, char_literal84_tree);
			}

			pushFollow(FOLLOW_new_value_in_update_item1220);
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
	// JPA2.g:156:1: new_value : ( scalar_expression | simple_entity_expression | 'NULL' );
	public final JPA2Parser.new_value_return new_value() throws RecognitionException {
		JPA2Parser.new_value_return retval = new JPA2Parser.new_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal88=null;
		ParserRuleReturnScope scalar_expression86 =null;
		ParserRuleReturnScope simple_entity_expression87 =null;

		Object string_literal88_tree=null;

		try {
			// JPA2.g:157:5: ( scalar_expression | simple_entity_expression | 'NULL' )
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
			case 60:
			case 62:
			case 65:
			case 77:
			case 79:
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
			case 145:
			case 146:
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
			case 72:
				{
				int LA30_3 = input.LA(2);
				if ( (LA30_3==65) ) {
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
			case 58:
				{
				int LA30_5 = input.LA(2);
				if ( (LA30_5==WORD) ) {
					int LA30_11 = input.LA(3);
					if ( (LA30_11==147) ) {
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
				if ( (LA30_6==63) ) {
					alt30=1;
				}
				else if ( (LA30_6==EOF||LA30_6==61||LA30_6==143) ) {
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
					// JPA2.g:157:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_new_value1231);
					scalar_expression86=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression86.getTree());

					}
					break;
				case 2 :
					// JPA2.g:158:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_new_value1239);
					simple_entity_expression87=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression87.getTree());

					}
					break;
				case 3 :
					// JPA2.g:159:7: 'NULL'
					{
					root_0 = (Object)adaptor.nil();


					string_literal88=(Token)match(input,117,FOLLOW_117_in_new_value1247); if (state.failed) return retval;
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
	// JPA2.g:161:1: delete_clause : fr= 'FROM' identification_variable_declaration -> ^( T_SOURCES[$fr] identification_variable_declaration ) ;
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
			// JPA2.g:162:5: (fr= 'FROM' identification_variable_declaration -> ^( T_SOURCES[$fr] identification_variable_declaration ) )
			// JPA2.g:162:7: fr= 'FROM' identification_variable_declaration
			{
			fr=(Token)match(input,101,FOLLOW_101_in_delete_clause1261); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_101.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_delete_clause1263);
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
			// 163:5: -> ^( T_SOURCES[$fr] identification_variable_declaration )
			{
				// JPA2.g:163:8: ^( T_SOURCES[$fr] identification_variable_declaration )
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
	// JPA2.g:164:1: select_clause : ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) ;
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
		RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
		RewriteRuleSubtreeStream stream_select_item=new RewriteRuleSubtreeStream(adaptor,"rule select_item");

		try {
			// JPA2.g:165:5: ( ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) )
			// JPA2.g:165:7: ( 'DISTINCT' )? select_item ( ',' select_item )*
			{
			// JPA2.g:165:7: ( 'DISTINCT' )?
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0==DISTINCT) ) {
				alt31=1;
			}
			switch (alt31) {
				case 1 :
					// JPA2.g:165:8: 'DISTINCT'
					{
					string_literal90=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause1291); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal90);

					}
					break;

			}

			pushFollow(FOLLOW_select_item_in_select_clause1295);
			select_item91=select_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_item.add(select_item91.getTree());
			// JPA2.g:165:33: ( ',' select_item )*
			loop32:
			while (true) {
				int alt32=2;
				int LA32_0 = input.LA(1);
				if ( (LA32_0==61) ) {
					alt32=1;
				}

				switch (alt32) {
				case 1 :
					// JPA2.g:165:34: ',' select_item
					{
					char_literal92=(Token)match(input,61,FOLLOW_61_in_select_clause1298); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_61.add(char_literal92);

					pushFollow(FOLLOW_select_item_in_select_clause1300);
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
			// 166:5: -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
			{
				// JPA2.g:166:8: ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:166:48: ( 'DISTINCT' )?
				if ( stream_DISTINCT.hasNext() ) {
					adaptor.addChild(root_1, stream_DISTINCT.nextNode());
				}
				stream_DISTINCT.reset();

				// JPA2.g:166:62: ( ^( T_SELECTED_ITEM[] select_item ) )*
				while ( stream_select_item.hasNext() ) {
					// JPA2.g:166:62: ^( T_SELECTED_ITEM[] select_item )
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
	// JPA2.g:167:1: select_item : select_expression ( ( AS )? result_variable )? ;
	public final JPA2Parser.select_item_return select_item() throws RecognitionException {
		JPA2Parser.select_item_return retval = new JPA2Parser.select_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AS95=null;
		ParserRuleReturnScope select_expression94 =null;
		ParserRuleReturnScope result_variable96 =null;

		Object AS95_tree=null;

		try {
			// JPA2.g:168:5: ( select_expression ( ( AS )? result_variable )? )
			// JPA2.g:168:7: select_expression ( ( AS )? result_variable )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_select_expression_in_select_item1343);
			select_expression94=select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, select_expression94.getTree());

			// JPA2.g:168:25: ( ( AS )? result_variable )?
			int alt34=2;
			int LA34_0 = input.LA(1);
			if ( (LA34_0==AS||LA34_0==WORD) ) {
				alt34=1;
			}
			switch (alt34) {
				case 1 :
					// JPA2.g:168:26: ( AS )? result_variable
					{
					// JPA2.g:168:26: ( AS )?
					int alt33=2;
					int LA33_0 = input.LA(1);
					if ( (LA33_0==AS) ) {
						alt33=1;
					}
					switch (alt33) {
						case 1 :
							// JPA2.g:168:27: AS
							{
							AS95=(Token)match(input,AS,FOLLOW_AS_in_select_item1347); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							AS95_tree = (Object)adaptor.create(AS95);
							adaptor.addChild(root_0, AS95_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_result_variable_in_select_item1351);
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
	// JPA2.g:169:1: select_expression : ( path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )? | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression );
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
			// JPA2.g:170:5: ( path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )? | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression )
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
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 58:
			case 60:
			case 62:
			case 65:
			case 72:
			case 77:
			case 79:
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
			case 145:
			case 146:
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
			case 102:
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
			case 121:
				{
				alt36=5;
				}
				break;
			case 115:
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
					// JPA2.g:170:7: path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_select_expression1364);
					path_expression97=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression97.getTree());

					// JPA2.g:170:23: ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
					int alt35=2;
					int LA35_0 = input.LA(1);
					if ( ((LA35_0 >= 59 && LA35_0 <= 60)||LA35_0==62||LA35_0==64) ) {
						alt35=1;
					}
					switch (alt35) {
						case 1 :
							// JPA2.g:170:24: ( '+' | '-' | '*' | '/' ) scalar_expression
							{
							set98=input.LT(1);
							if ( (input.LA(1) >= 59 && input.LA(1) <= 60)||input.LA(1)==62||input.LA(1)==64 ) {
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
							pushFollow(FOLLOW_scalar_expression_in_select_expression1383);
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
					// JPA2.g:171:7: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_select_expression1393);
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
					// 171:31: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
					{
						// JPA2.g:171:34: ^( T_SELECTED_ENTITY[$identification_variable.text] )
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
					// JPA2.g:172:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_select_expression1411);
					scalar_expression101=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression101.getTree());

					}
					break;
				case 4 :
					// JPA2.g:173:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_select_expression1419);
					aggregate_expression102=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression102.getTree());

					}
					break;
				case 5 :
					// JPA2.g:174:7: 'OBJECT' '(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal103=(Token)match(input,121,FOLLOW_121_in_select_expression1427); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal103_tree = (Object)adaptor.create(string_literal103);
					adaptor.addChild(root_0, string_literal103_tree);
					}

					char_literal104=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression1429); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal104_tree = (Object)adaptor.create(char_literal104);
					adaptor.addChild(root_0, char_literal104_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_select_expression1430);
					identification_variable105=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable105.getTree());

					char_literal106=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression1431); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal106_tree = (Object)adaptor.create(char_literal106);
					adaptor.addChild(root_0, char_literal106_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:175:7: constructor_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_constructor_expression_in_select_expression1439);
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
	// JPA2.g:176:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
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
			// JPA2.g:177:5: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
			// JPA2.g:177:7: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal108=(Token)match(input,115,FOLLOW_115_in_constructor_expression1450); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal108_tree = (Object)adaptor.create(string_literal108);
			adaptor.addChild(root_0, string_literal108_tree);
			}

			pushFollow(FOLLOW_constructor_name_in_constructor_expression1452);
			constructor_name109=constructor_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name109.getTree());

			char_literal110=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression1454); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal110_tree = (Object)adaptor.create(char_literal110);
			adaptor.addChild(root_0, char_literal110_tree);
			}

			pushFollow(FOLLOW_constructor_item_in_constructor_expression1456);
			constructor_item111=constructor_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item111.getTree());

			// JPA2.g:177:51: ( ',' constructor_item )*
			loop37:
			while (true) {
				int alt37=2;
				int LA37_0 = input.LA(1);
				if ( (LA37_0==61) ) {
					alt37=1;
				}

				switch (alt37) {
				case 1 :
					// JPA2.g:177:52: ',' constructor_item
					{
					char_literal112=(Token)match(input,61,FOLLOW_61_in_constructor_expression1459); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal112_tree = (Object)adaptor.create(char_literal112);
					adaptor.addChild(root_0, char_literal112_tree);
					}

					pushFollow(FOLLOW_constructor_item_in_constructor_expression1461);
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

			char_literal114=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression1465); if (state.failed) return retval;
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
	// JPA2.g:178:1: constructor_item : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.constructor_item_return constructor_item() throws RecognitionException {
		JPA2Parser.constructor_item_return retval = new JPA2Parser.constructor_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression115 =null;
		ParserRuleReturnScope scalar_expression116 =null;
		ParserRuleReturnScope aggregate_expression117 =null;
		ParserRuleReturnScope identification_variable118 =null;


		try {
			// JPA2.g:179:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
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
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 58:
			case 60:
			case 62:
			case 65:
			case 72:
			case 77:
			case 79:
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
			case 145:
			case 146:
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
			case 102:
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
					// JPA2.g:179:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_constructor_item1476);
					path_expression115=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression115.getTree());

					}
					break;
				case 2 :
					// JPA2.g:180:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_constructor_item1484);
					scalar_expression116=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression116.getTree());

					}
					break;
				case 3 :
					// JPA2.g:181:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_constructor_item1492);
					aggregate_expression117=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression117.getTree());

					}
					break;
				case 4 :
					// JPA2.g:182:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_constructor_item1500);
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
	// JPA2.g:183:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );
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
			// JPA2.g:184:5: ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation )
			int alt41=3;
			alt41 = dfa41.predict(input);
			switch (alt41) {
				case 1 :
					// JPA2.g:184:7: aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')'
					{
					pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1511);
					aggregate_expression_function_name119=aggregate_expression_function_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name119.getTree());
					char_literal120=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1513); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal120);

					// JPA2.g:184:45: ( DISTINCT )?
					int alt39=2;
					int LA39_0 = input.LA(1);
					if ( (LA39_0==DISTINCT) ) {
						alt39=1;
					}
					switch (alt39) {
						case 1 :
							// JPA2.g:184:46: DISTINCT
							{
							DISTINCT121=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1515); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT121);

							}
							break;

					}

					pushFollow(FOLLOW_arithmetic_expression_in_aggregate_expression1519);
					arithmetic_expression122=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_arithmetic_expression.add(arithmetic_expression122.getTree());
					char_literal123=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1520); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal123);

					// AST REWRITE
					// elements: LPAREN, arithmetic_expression, RPAREN, DISTINCT, aggregate_expression_function_name
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 185:5: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' )
					{
						// JPA2.g:185:8: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:185:93: ( 'DISTINCT' )?
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
					// JPA2.g:186:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
					{
					string_literal124=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1554); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COUNT.add(string_literal124);

					char_literal125=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1556); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal125);

					// JPA2.g:186:18: ( DISTINCT )?
					int alt40=2;
					int LA40_0 = input.LA(1);
					if ( (LA40_0==DISTINCT) ) {
						alt40=1;
					}
					switch (alt40) {
						case 1 :
							// JPA2.g:186:19: DISTINCT
							{
							DISTINCT126=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1558); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT126);

							}
							break;

					}

					pushFollow(FOLLOW_count_argument_in_aggregate_expression1562);
					count_argument127=count_argument();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_count_argument.add(count_argument127.getTree());
					char_literal128=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1564); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal128);

					// AST REWRITE
					// elements: LPAREN, RPAREN, COUNT, count_argument, DISTINCT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 187:5: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
					{
						// JPA2.g:187:8: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_COUNT.nextNode());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:187:66: ( 'DISTINCT' )?
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
					// JPA2.g:188:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_aggregate_expression1599);
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
	// JPA2.g:189:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
	public final JPA2Parser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
		JPA2Parser.aggregate_expression_function_name_return retval = new JPA2Parser.aggregate_expression_function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set130=null;

		Object set130_tree=null;

		try {
			// JPA2.g:190:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
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
	// JPA2.g:191:1: count_argument : ( identification_variable | path_expression );
	public final JPA2Parser.count_argument_return count_argument() throws RecognitionException {
		JPA2Parser.count_argument_return retval = new JPA2Parser.count_argument_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable131 =null;
		ParserRuleReturnScope path_expression132 =null;


		try {
			// JPA2.g:192:5: ( identification_variable | path_expression )
			int alt42=2;
			int LA42_0 = input.LA(1);
			if ( (LA42_0==GROUP||LA42_0==WORD) ) {
				int LA42_1 = input.LA(2);
				if ( (LA42_1==RPAREN) ) {
					alt42=1;
				}
				else if ( (LA42_1==63) ) {
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
					// JPA2.g:192:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_count_argument1636);
					identification_variable131=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable131.getTree());

					}
					break;
				case 2 :
					// JPA2.g:192:33: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_count_argument1640);
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
	// JPA2.g:193:1: where_clause : wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) ;
	public final JPA2Parser.where_clause_return where_clause() throws RecognitionException {
		JPA2Parser.where_clause_return retval = new JPA2Parser.where_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token wh=null;
		ParserRuleReturnScope conditional_expression133 =null;

		Object wh_tree=null;
		RewriteRuleTokenStream stream_143=new RewriteRuleTokenStream(adaptor,"token 143");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");

		try {
			// JPA2.g:194:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) )
			// JPA2.g:194:7: wh= 'WHERE' conditional_expression
			{
			wh=(Token)match(input,143,FOLLOW_143_in_where_clause1653); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_143.add(wh);

			pushFollow(FOLLOW_conditional_expression_in_where_clause1655);
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
			// 194:40: -> ^( T_CONDITION[$wh] conditional_expression )
			{
				// JPA2.g:194:43: ^( T_CONDITION[$wh] conditional_expression )
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
	// JPA2.g:195:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
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
		RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
		RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");

		try {
			// JPA2.g:196:5: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
			// JPA2.g:196:7: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
			{
			string_literal134=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1677); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_GROUP.add(string_literal134);

			string_literal135=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1679); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal135);

			pushFollow(FOLLOW_groupby_item_in_groupby_clause1681);
			groupby_item136=groupby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item136.getTree());
			// JPA2.g:196:33: ( ',' groupby_item )*
			loop43:
			while (true) {
				int alt43=2;
				int LA43_0 = input.LA(1);
				if ( (LA43_0==61) ) {
					alt43=1;
				}

				switch (alt43) {
				case 1 :
					// JPA2.g:196:34: ',' groupby_item
					{
					char_literal137=(Token)match(input,61,FOLLOW_61_in_groupby_clause1684); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_61.add(char_literal137);

					pushFollow(FOLLOW_groupby_item_in_groupby_clause1686);
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
			// elements: BY, groupby_item, GROUP
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 197:5: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
			{
				// JPA2.g:197:8: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new GroupByNode(T_GROUP_BY), root_1);
				adaptor.addChild(root_1, stream_GROUP.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:197:49: ( groupby_item )*
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
	// JPA2.g:198:1: groupby_item : ( path_expression | identification_variable | extract_function );
	public final JPA2Parser.groupby_item_return groupby_item() throws RecognitionException {
		JPA2Parser.groupby_item_return retval = new JPA2Parser.groupby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression139 =null;
		ParserRuleReturnScope identification_variable140 =null;
		ParserRuleReturnScope extract_function141 =null;


		try {
			// JPA2.g:199:5: ( path_expression | identification_variable | extract_function )
			int alt44=3;
			int LA44_0 = input.LA(1);
			if ( (LA44_0==GROUP||LA44_0==WORD) ) {
				int LA44_1 = input.LA(2);
				if ( (LA44_1==63) ) {
					alt44=1;
				}
				else if ( (LA44_1==EOF||LA44_1==HAVING||LA44_1==ORDER||LA44_1==RPAREN||LA44_1==61) ) {
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
			else if ( (LA44_0==100) ) {
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
					// JPA2.g:199:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_groupby_item1720);
					path_expression139=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression139.getTree());

					}
					break;
				case 2 :
					// JPA2.g:199:25: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_groupby_item1724);
					identification_variable140=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable140.getTree());

					}
					break;
				case 3 :
					// JPA2.g:199:51: extract_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extract_function_in_groupby_item1728);
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
	// JPA2.g:200:1: having_clause : 'HAVING' conditional_expression ;
	public final JPA2Parser.having_clause_return having_clause() throws RecognitionException {
		JPA2Parser.having_clause_return retval = new JPA2Parser.having_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal142=null;
		ParserRuleReturnScope conditional_expression143 =null;

		Object string_literal142_tree=null;

		try {
			// JPA2.g:201:5: ( 'HAVING' conditional_expression )
			// JPA2.g:201:7: 'HAVING' conditional_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal142=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1739); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal142_tree = (Object)adaptor.create(string_literal142);
			adaptor.addChild(root_0, string_literal142_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_having_clause1741);
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
	// JPA2.g:202:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
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
		RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
		RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");

		try {
			// JPA2.g:203:5: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
			// JPA2.g:203:7: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
			{
			string_literal144=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1752); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ORDER.add(string_literal144);

			string_literal145=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1754); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal145);

			pushFollow(FOLLOW_orderby_item_in_orderby_clause1756);
			orderby_item146=orderby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item146.getTree());
			// JPA2.g:203:33: ( ',' orderby_item )*
			loop45:
			while (true) {
				int alt45=2;
				int LA45_0 = input.LA(1);
				if ( (LA45_0==61) ) {
					alt45=1;
				}

				switch (alt45) {
				case 1 :
					// JPA2.g:203:34: ',' orderby_item
					{
					char_literal147=(Token)match(input,61,FOLLOW_61_in_orderby_clause1759); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_61.add(char_literal147);

					pushFollow(FOLLOW_orderby_item_in_orderby_clause1761);
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
			// elements: BY, orderby_item, ORDER
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 204:5: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
			{
				// JPA2.g:204:8: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByNode(T_ORDER_BY), root_1);
				adaptor.addChild(root_1, stream_ORDER.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:204:49: ( orderby_item )*
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
	// JPA2.g:205:1: orderby_item : orderby_variable ( sort )? ( sortNulls )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? ) ;
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
			// JPA2.g:206:5: ( orderby_variable ( sort )? ( sortNulls )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? ) )
			// JPA2.g:206:7: orderby_variable ( sort )? ( sortNulls )?
			{
			pushFollow(FOLLOW_orderby_variable_in_orderby_item1795);
			orderby_variable149=orderby_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable149.getTree());
			// JPA2.g:206:24: ( sort )?
			int alt46=2;
			int LA46_0 = input.LA(1);
			if ( (LA46_0==ASC||LA46_0==DESC) ) {
				alt46=1;
			}
			switch (alt46) {
				case 1 :
					// JPA2.g:206:24: sort
					{
					pushFollow(FOLLOW_sort_in_orderby_item1797);
					sort150=sort();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sort.add(sort150.getTree());
					}
					break;

			}

			// JPA2.g:206:30: ( sortNulls )?
			int alt47=2;
			int LA47_0 = input.LA(1);
			if ( ((LA47_0 >= 119 && LA47_0 <= 120)) ) {
				alt47=1;
			}
			switch (alt47) {
				case 1 :
					// JPA2.g:206:30: sortNulls
					{
					pushFollow(FOLLOW_sortNulls_in_orderby_item1800);
					sortNulls151=sortNulls();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sortNulls.add(sortNulls151.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: sort, sortNulls, orderby_variable
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 207:6: -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? )
			{
				// JPA2.g:207:9: ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);
				adaptor.addChild(root_1, stream_orderby_variable.nextTree());
				// JPA2.g:207:65: ( sort )?
				if ( stream_sort.hasNext() ) {
					adaptor.addChild(root_1, stream_sort.nextTree());
				}
				stream_sort.reset();

				// JPA2.g:207:71: ( sortNulls )?
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
	// JPA2.g:208:1: orderby_variable : ( path_expression | general_identification_variable | result_variable | scalar_expression | aggregate_expression );
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
			// JPA2.g:209:5: ( path_expression | general_identification_variable | result_variable | scalar_expression | aggregate_expression )
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
			case 106:
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
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 58:
			case 60:
			case 62:
			case 65:
			case 72:
			case 77:
			case 79:
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
			case 145:
			case 146:
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
			case 102:
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
					// JPA2.g:209:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_orderby_variable1835);
					path_expression152=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression152.getTree());

					}
					break;
				case 2 :
					// JPA2.g:209:25: general_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_identification_variable_in_orderby_variable1839);
					general_identification_variable153=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable153.getTree());

					}
					break;
				case 3 :
					// JPA2.g:209:59: result_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_result_variable_in_orderby_variable1843);
					result_variable154=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable154.getTree());

					}
					break;
				case 4 :
					// JPA2.g:209:77: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_orderby_variable1847);
					scalar_expression155=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression155.getTree());

					}
					break;
				case 5 :
					// JPA2.g:209:97: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_orderby_variable1851);
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
	// JPA2.g:210:1: sort : ( 'ASC' | 'DESC' ) ;
	public final JPA2Parser.sort_return sort() throws RecognitionException {
		JPA2Parser.sort_return retval = new JPA2Parser.sort_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set157=null;

		Object set157_tree=null;

		try {
			// JPA2.g:211:5: ( ( 'ASC' | 'DESC' ) )
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
	// JPA2.g:212:1: sortNulls : ( 'NULLS FIRST' | 'NULLS LAST' ) ;
	public final JPA2Parser.sortNulls_return sortNulls() throws RecognitionException {
		JPA2Parser.sortNulls_return retval = new JPA2Parser.sortNulls_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set158=null;

		Object set158_tree=null;

		try {
			// JPA2.g:213:5: ( ( 'NULLS FIRST' | 'NULLS LAST' ) )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set158=input.LT(1);
			if ( (input.LA(1) >= 119 && input.LA(1) <= 120) ) {
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
	// JPA2.g:214:1: subquery : lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
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
		RewriteRuleTokenStream stream_127=new RewriteRuleTokenStream(adaptor,"token 127");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
		RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");

		try {
			// JPA2.g:215:5: (lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
			// JPA2.g:215:7: lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
			{
			lp=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subquery1898); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(lp);

			string_literal159=(Token)match(input,127,FOLLOW_127_in_subquery1900); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_127.add(string_literal159);

			pushFollow(FOLLOW_simple_select_clause_in_subquery1902);
			simple_select_clause160=simple_select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause160.getTree());
			pushFollow(FOLLOW_subquery_from_clause_in_subquery1904);
			subquery_from_clause161=subquery_from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause161.getTree());
			// JPA2.g:215:65: ( where_clause )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==143) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// JPA2.g:215:66: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_subquery1907);
					where_clause162=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause162.getTree());
					}
					break;

			}

			// JPA2.g:215:81: ( groupby_clause )?
			int alt50=2;
			int LA50_0 = input.LA(1);
			if ( (LA50_0==GROUP) ) {
				alt50=1;
			}
			switch (alt50) {
				case 1 :
					// JPA2.g:215:82: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_subquery1912);
					groupby_clause163=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause163.getTree());
					}
					break;

			}

			// JPA2.g:215:99: ( having_clause )?
			int alt51=2;
			int LA51_0 = input.LA(1);
			if ( (LA51_0==HAVING) ) {
				alt51=1;
			}
			switch (alt51) {
				case 1 :
					// JPA2.g:215:100: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_subquery1917);
					having_clause164=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause164.getTree());
					}
					break;

			}

			rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1923); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(rp);

			// AST REWRITE
			// elements: simple_select_clause, groupby_clause, having_clause, subquery_from_clause, where_clause, 127
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 216:6: -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
			{
				// JPA2.g:216:9: ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);
				adaptor.addChild(root_1, stream_127.nextNode());
				adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
				adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
				// JPA2.g:216:90: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:216:106: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:216:124: ( having_clause )?
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
	// JPA2.g:217:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
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
		RewriteRuleTokenStream stream_101=new RewriteRuleTokenStream(adaptor,"token 101");
		RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
		RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");

		try {
			// JPA2.g:218:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
			// JPA2.g:218:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
			{
			fr=(Token)match(input,101,FOLLOW_101_in_subquery_from_clause1973); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_101.add(fr);

			pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1975);
			subselect_identification_variable_declaration165=subselect_identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration165.getTree());
			// JPA2.g:218:63: ( ',' subselect_identification_variable_declaration )*
			loop52:
			while (true) {
				int alt52=2;
				int LA52_0 = input.LA(1);
				if ( (LA52_0==61) ) {
					alt52=1;
				}

				switch (alt52) {
				case 1 :
					// JPA2.g:218:64: ',' subselect_identification_variable_declaration
					{
					char_literal166=(Token)match(input,61,FOLLOW_61_in_subquery_from_clause1978); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_61.add(char_literal166);

					pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1980);
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
			// 219:5: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
			{
				// JPA2.g:219:8: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				// JPA2.g:219:35: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
				while ( stream_subselect_identification_variable_declaration.hasNext() ) {
					// JPA2.g:219:35: ^( T_SOURCE subselect_identification_variable_declaration )
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
	// JPA2.g:221:1: subselect_identification_variable_declaration : ( identification_variable_declaration | derived_path_expression AS identification_variable ( join )* | derived_collection_member_declaration );
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
			// JPA2.g:222:5: ( identification_variable_declaration | derived_path_expression AS identification_variable ( join )* | derived_collection_member_declaration )
			int alt54=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA54_1 = input.LA(2);
				if ( (LA54_1==AS||LA54_1==GROUP||LA54_1==WORD) ) {
					alt54=1;
				}
				else if ( (LA54_1==63) ) {
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
			case 134:
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
					// JPA2.g:222:7: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration2018);
					identification_variable_declaration168=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration168.getTree());

					}
					break;
				case 2 :
					// JPA2.g:223:7: derived_path_expression AS identification_variable ( join )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration2026);
					derived_path_expression169=derived_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_path_expression169.getTree());

					AS170=(Token)match(input,AS,FOLLOW_AS_in_subselect_identification_variable_declaration2028); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AS170_tree = (Object)adaptor.create(AS170);
					adaptor.addChild(root_0, AS170_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration2030);
					identification_variable171=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable171.getTree());

					// JPA2.g:223:58: ( join )*
					loop53:
					while (true) {
						int alt53=2;
						int LA53_0 = input.LA(1);
						if ( (LA53_0==INNER||(LA53_0 >= JOIN && LA53_0 <= LEFT)) ) {
							alt53=1;
						}

						switch (alt53) {
						case 1 :
							// JPA2.g:223:59: join
							{
							pushFollow(FOLLOW_join_in_subselect_identification_variable_declaration2033);
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
					// JPA2.g:224:7: derived_collection_member_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration2043);
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
	// JPA2.g:225:1: derived_path_expression : ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field );
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
			// JPA2.g:226:5: ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field )
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
			else if ( (LA55_0==134) ) {
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
					// JPA2.g:226:7: general_derived_path '.' single_valued_object_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression2054);
					general_derived_path174=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path174.getTree());

					char_literal175=(Token)match(input,63,FOLLOW_63_in_derived_path_expression2055); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal175_tree = (Object)adaptor.create(char_literal175);
					adaptor.addChild(root_0, char_literal175_tree);
					}

					pushFollow(FOLLOW_single_valued_object_field_in_derived_path_expression2056);
					single_valued_object_field176=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field176.getTree());

					}
					break;
				case 2 :
					// JPA2.g:227:7: general_derived_path '.' collection_valued_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression2064);
					general_derived_path177=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path177.getTree());

					char_literal178=(Token)match(input,63,FOLLOW_63_in_derived_path_expression2065); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal178_tree = (Object)adaptor.create(char_literal178);
					adaptor.addChild(root_0, char_literal178_tree);
					}

					pushFollow(FOLLOW_collection_valued_field_in_derived_path_expression2066);
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
	// JPA2.g:228:1: general_derived_path : ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* );
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
			// JPA2.g:229:5: ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* )
			int alt57=2;
			int LA57_0 = input.LA(1);
			if ( (LA57_0==WORD) ) {
				alt57=1;
			}
			else if ( (LA57_0==134) ) {
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
					// JPA2.g:229:7: simple_derived_path
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_derived_path_in_general_derived_path2077);
					simple_derived_path180=simple_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_derived_path180.getTree());

					}
					break;
				case 2 :
					// JPA2.g:230:7: treated_derived_path ( '.' single_valued_object_field )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_treated_derived_path_in_general_derived_path2085);
					treated_derived_path181=treated_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, treated_derived_path181.getTree());

					// JPA2.g:230:27: ( '.' single_valued_object_field )*
					loop56:
					while (true) {
						int alt56=2;
						int LA56_0 = input.LA(1);
						if ( (LA56_0==63) ) {
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
											else if ( (LA56_7==63) ) {
												alt56=1;
											}

										}

									}

								}
								else if ( (LA56_3==63) ) {
									alt56=1;
								}

							}

						}

						switch (alt56) {
						case 1 :
							// JPA2.g:230:28: '.' single_valued_object_field
							{
							char_literal182=(Token)match(input,63,FOLLOW_63_in_general_derived_path2087); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal182_tree = (Object)adaptor.create(char_literal182);
							adaptor.addChild(root_0, char_literal182_tree);
							}

							pushFollow(FOLLOW_single_valued_object_field_in_general_derived_path2088);
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
	// JPA2.g:232:1: simple_derived_path : superquery_identification_variable ;
	public final JPA2Parser.simple_derived_path_return simple_derived_path() throws RecognitionException {
		JPA2Parser.simple_derived_path_return retval = new JPA2Parser.simple_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope superquery_identification_variable184 =null;


		try {
			// JPA2.g:233:5: ( superquery_identification_variable )
			// JPA2.g:233:7: superquery_identification_variable
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_superquery_identification_variable_in_simple_derived_path2106);
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
	// JPA2.g:235:1: treated_derived_path : 'TREAT(' general_derived_path AS subtype ')' ;
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
			// JPA2.g:236:5: ( 'TREAT(' general_derived_path AS subtype ')' )
			// JPA2.g:236:7: 'TREAT(' general_derived_path AS subtype ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal185=(Token)match(input,134,FOLLOW_134_in_treated_derived_path2123); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal185_tree = (Object)adaptor.create(string_literal185);
			adaptor.addChild(root_0, string_literal185_tree);
			}

			pushFollow(FOLLOW_general_derived_path_in_treated_derived_path2124);
			general_derived_path186=general_derived_path();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path186.getTree());

			AS187=(Token)match(input,AS,FOLLOW_AS_in_treated_derived_path2126); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			AS187_tree = (Object)adaptor.create(AS187);
			adaptor.addChild(root_0, AS187_tree);
			}

			pushFollow(FOLLOW_subtype_in_treated_derived_path2128);
			subtype188=subtype();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subtype188.getTree());

			char_literal189=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_treated_derived_path2130); if (state.failed) return retval;
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
	// JPA2.g:237:1: derived_collection_member_declaration : 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field ;
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
			// JPA2.g:238:5: ( 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field )
			// JPA2.g:238:7: 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field
			{
			root_0 = (Object)adaptor.nil();


			string_literal190=(Token)match(input,IN,FOLLOW_IN_in_derived_collection_member_declaration2141); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal190_tree = (Object)adaptor.create(string_literal190);
			adaptor.addChild(root_0, string_literal190_tree);
			}

			pushFollow(FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2143);
			superquery_identification_variable191=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable191.getTree());

			char_literal192=(Token)match(input,63,FOLLOW_63_in_derived_collection_member_declaration2144); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal192_tree = (Object)adaptor.create(char_literal192);
			adaptor.addChild(root_0, char_literal192_tree);
			}

			// JPA2.g:238:49: ( single_valued_object_field '.' )*
			loop58:
			while (true) {
				int alt58=2;
				int LA58_0 = input.LA(1);
				if ( (LA58_0==WORD) ) {
					int LA58_1 = input.LA(2);
					if ( (LA58_1==63) ) {
						alt58=1;
					}

				}

				switch (alt58) {
				case 1 :
					// JPA2.g:238:50: single_valued_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2146);
					single_valued_object_field193=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field193.getTree());

					char_literal194=(Token)match(input,63,FOLLOW_63_in_derived_collection_member_declaration2148); if (state.failed) return retval;
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

			pushFollow(FOLLOW_collection_valued_field_in_derived_collection_member_declaration2151);
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
	// JPA2.g:240:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
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
			// JPA2.g:241:5: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
			// JPA2.g:241:7: ( 'DISTINCT' )? simple_select_expression
			{
			// JPA2.g:241:7: ( 'DISTINCT' )?
			int alt59=2;
			int LA59_0 = input.LA(1);
			if ( (LA59_0==DISTINCT) ) {
				alt59=1;
			}
			switch (alt59) {
				case 1 :
					// JPA2.g:241:8: 'DISTINCT'
					{
					string_literal196=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause2164); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal196);

					}
					break;

			}

			pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause2168);
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
			// 242:5: -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
			{
				// JPA2.g:242:8: ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:242:48: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);
				// JPA2.g:242:86: ( 'DISTINCT' )?
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
	// JPA2.g:243:1: simple_select_expression : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.simple_select_expression_return simple_select_expression() throws RecognitionException {
		JPA2Parser.simple_select_expression_return retval = new JPA2Parser.simple_select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression198 =null;
		ParserRuleReturnScope scalar_expression199 =null;
		ParserRuleReturnScope aggregate_expression200 =null;
		ParserRuleReturnScope identification_variable201 =null;


		try {
			// JPA2.g:244:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
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
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 58:
			case 60:
			case 62:
			case 65:
			case 72:
			case 77:
			case 79:
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
			case 145:
			case 146:
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
			case 102:
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
					// JPA2.g:244:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_simple_select_expression2208);
					path_expression198=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression198.getTree());

					}
					break;
				case 2 :
					// JPA2.g:245:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_simple_select_expression2216);
					scalar_expression199=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression199.getTree());

					}
					break;
				case 3 :
					// JPA2.g:246:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression2224);
					aggregate_expression200=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression200.getTree());

					}
					break;
				case 4 :
					// JPA2.g:247:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_select_expression2232);
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
	// JPA2.g:248:1: scalar_expression : ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression );
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
			// JPA2.g:249:5: ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression )
			int alt61=7;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 60:
			case 62:
			case 65:
			case 79:
			case 104:
			case 108:
			case 110:
			case 113:
			case 128:
			case 130:
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
			case 72:
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
			case 58:
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
			case 102:
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
			case 84:
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
			case 86:
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
			case 118:
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
			case 85:
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
			case 100:
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
			case 77:
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
			case 87:
			case 131:
			case 135:
			case 138:
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
			case 88:
			case 89:
			case 90:
				{
				alt61=4;
				}
				break;
			case 145:
			case 146:
				{
				alt61=5;
				}
				break;
			case 136:
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
					// JPA2.g:249:7: arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_scalar_expression2243);
					arithmetic_expression202=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression202.getTree());

					}
					break;
				case 2 :
					// JPA2.g:250:7: string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_scalar_expression2251);
					string_expression203=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression203.getTree());

					}
					break;
				case 3 :
					// JPA2.g:251:7: enum_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_scalar_expression2259);
					enum_expression204=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression204.getTree());

					}
					break;
				case 4 :
					// JPA2.g:252:7: datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_scalar_expression2267);
					datetime_expression205=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression205.getTree());

					}
					break;
				case 5 :
					// JPA2.g:253:7: boolean_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_scalar_expression2275);
					boolean_expression206=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression206.getTree());

					}
					break;
				case 6 :
					// JPA2.g:254:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_scalar_expression2283);
					case_expression207=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression207.getTree());

					}
					break;
				case 7 :
					// JPA2.g:255:7: entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_scalar_expression2291);
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
	// JPA2.g:256:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
	public final JPA2Parser.conditional_expression_return conditional_expression() throws RecognitionException {
		JPA2Parser.conditional_expression_return retval = new JPA2Parser.conditional_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal210=null;
		ParserRuleReturnScope conditional_term209 =null;
		ParserRuleReturnScope conditional_term211 =null;

		Object string_literal210_tree=null;

		try {
			// JPA2.g:257:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
			// JPA2.g:257:7: ( conditional_term ) ( 'OR' conditional_term )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:257:7: ( conditional_term )
			// JPA2.g:257:8: conditional_term
			{
			pushFollow(FOLLOW_conditional_term_in_conditional_expression2303);
			conditional_term209=conditional_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term209.getTree());

			}

			// JPA2.g:257:26: ( 'OR' conditional_term )*
			loop62:
			while (true) {
				int alt62=2;
				int LA62_0 = input.LA(1);
				if ( (LA62_0==OR) ) {
					alt62=1;
				}

				switch (alt62) {
				case 1 :
					// JPA2.g:257:27: 'OR' conditional_term
					{
					string_literal210=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression2307); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal210_tree = (Object)adaptor.create(string_literal210);
					adaptor.addChild(root_0, string_literal210_tree);
					}

					pushFollow(FOLLOW_conditional_term_in_conditional_expression2309);
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
	// JPA2.g:258:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
	public final JPA2Parser.conditional_term_return conditional_term() throws RecognitionException {
		JPA2Parser.conditional_term_return retval = new JPA2Parser.conditional_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal213=null;
		ParserRuleReturnScope conditional_factor212 =null;
		ParserRuleReturnScope conditional_factor214 =null;

		Object string_literal213_tree=null;

		try {
			// JPA2.g:259:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
			// JPA2.g:259:7: ( conditional_factor ) ( 'AND' conditional_factor )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:259:7: ( conditional_factor )
			// JPA2.g:259:8: conditional_factor
			{
			pushFollow(FOLLOW_conditional_factor_in_conditional_term2323);
			conditional_factor212=conditional_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor212.getTree());

			}

			// JPA2.g:259:28: ( 'AND' conditional_factor )*
			loop63:
			while (true) {
				int alt63=2;
				int LA63_0 = input.LA(1);
				if ( (LA63_0==AND) ) {
					alt63=1;
				}

				switch (alt63) {
				case 1 :
					// JPA2.g:259:29: 'AND' conditional_factor
					{
					string_literal213=(Token)match(input,AND,FOLLOW_AND_in_conditional_term2327); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal213_tree = (Object)adaptor.create(string_literal213);
					adaptor.addChild(root_0, string_literal213_tree);
					}

					pushFollow(FOLLOW_conditional_factor_in_conditional_term2329);
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
	// JPA2.g:260:1: conditional_factor : ( 'NOT' )? conditional_primary ;
	public final JPA2Parser.conditional_factor_return conditional_factor() throws RecognitionException {
		JPA2Parser.conditional_factor_return retval = new JPA2Parser.conditional_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal215=null;
		ParserRuleReturnScope conditional_primary216 =null;

		Object string_literal215_tree=null;

		try {
			// JPA2.g:261:5: ( ( 'NOT' )? conditional_primary )
			// JPA2.g:261:7: ( 'NOT' )? conditional_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:261:7: ( 'NOT' )?
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
					// JPA2.g:261:8: 'NOT'
					{
					string_literal215=(Token)match(input,NOT,FOLLOW_NOT_in_conditional_factor2343); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal215_tree = (Object)adaptor.create(string_literal215);
					adaptor.addChild(root_0, string_literal215_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_conditional_primary_in_conditional_factor2347);
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
	// JPA2.g:262:1: conditional_primary : ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' );
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
			// JPA2.g:263:5: ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' )
			int alt65=2;
			int LA65_0 = input.LA(1);
			if ( (LA65_0==AVG||LA65_0==COUNT||LA65_0==GROUP||LA65_0==INT_NUMERAL||LA65_0==LOWER||(LA65_0 >= MAX && LA65_0 <= NOT)||(LA65_0 >= STRING_LITERAL && LA65_0 <= SUM)||LA65_0==WORD||LA65_0==58||LA65_0==60||LA65_0==62||LA65_0==65||(LA65_0 >= 72 && LA65_0 <= 79)||(LA65_0 >= 84 && LA65_0 <= 90)||(LA65_0 >= 99 && LA65_0 <= 100)||LA65_0==102||LA65_0==104||LA65_0==108||LA65_0==110||LA65_0==113||LA65_0==118||LA65_0==128||(LA65_0 >= 130 && LA65_0 <= 131)||(LA65_0 >= 134 && LA65_0 <= 136)||LA65_0==138||(LA65_0 >= 145 && LA65_0 <= 146)) ) {
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
					// JPA2.g:263:7: simple_cond_expression
					{
					pushFollow(FOLLOW_simple_cond_expression_in_conditional_primary2358);
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
					// 264:5: -> ^( T_SIMPLE_CONDITION[] simple_cond_expression )
					{
						// JPA2.g:264:8: ^( T_SIMPLE_CONDITION[] simple_cond_expression )
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
					// JPA2.g:265:7: '(' conditional_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal218=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_primary2382); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal218_tree = (Object)adaptor.create(char_literal218);
					adaptor.addChild(root_0, char_literal218_tree);
					}

					pushFollow(FOLLOW_conditional_expression_in_conditional_primary2383);
					conditional_expression219=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression219.getTree());

					char_literal220=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_primary2384); if (state.failed) return retval;
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
	// JPA2.g:266:1: simple_cond_expression : ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
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
			// JPA2.g:267:5: ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
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
			case 72:
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
			case 58:
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
			case 87:
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
			case 131:
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
			case 135:
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
			case 138:
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
			case 102:
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
			case 84:
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
			case 86:
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
			case 118:
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
			case 85:
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
			case 100:
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
			case 77:
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
			case 145:
			case 146:
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
			case 88:
			case 89:
			case 90:
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
			case 136:
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
			case 60:
			case 62:
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
			case 65:
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
			case 108:
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
			case 110:
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
			case 79:
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
			case 130:
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
			case 113:
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
			case 128:
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
			case 104:
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
			case 134:
				{
				alt66=5;
				}
				break;
			case NOT:
			case 99:
				{
				alt66=8;
				}
				break;
			case 73:
			case 74:
			case 75:
			case 76:
			case 78:
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
					// JPA2.g:267:7: comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression2395);
					comparison_expression221=comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression221.getTree());

					}
					break;
				case 2 :
					// JPA2.g:268:7: between_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_between_expression_in_simple_cond_expression2403);
					between_expression222=between_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression222.getTree());

					}
					break;
				case 3 :
					// JPA2.g:269:7: in_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_in_expression_in_simple_cond_expression2411);
					in_expression223=in_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression223.getTree());

					}
					break;
				case 4 :
					// JPA2.g:270:7: like_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_like_expression_in_simple_cond_expression2419);
					like_expression224=like_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression224.getTree());

					}
					break;
				case 5 :
					// JPA2.g:271:7: null_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression2427);
					null_comparison_expression225=null_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression225.getTree());

					}
					break;
				case 6 :
					// JPA2.g:272:7: empty_collection_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2435);
					empty_collection_comparison_expression226=empty_collection_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression226.getTree());

					}
					break;
				case 7 :
					// JPA2.g:273:7: collection_member_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression2443);
					collection_member_expression227=collection_member_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression227.getTree());

					}
					break;
				case 8 :
					// JPA2.g:274:7: exists_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exists_expression_in_simple_cond_expression2451);
					exists_expression228=exists_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression228.getTree());

					}
					break;
				case 9 :
					// JPA2.g:275:7: date_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression2459);
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
	// JPA2.g:278:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
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
			// JPA2.g:279:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
			int alt67=5;
			switch ( input.LA(1) ) {
			case 73:
				{
				alt67=1;
				}
				break;
			case 75:
				{
				alt67=2;
				}
				break;
			case 74:
				{
				alt67=3;
				}
				break;
			case 76:
				{
				alt67=4;
				}
				break;
			case 78:
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
					// JPA2.g:279:7: date_between_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2472);
					date_between_macro_expression230=date_between_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression230.getTree());

					}
					break;
				case 2 :
					// JPA2.g:280:7: date_before_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2480);
					date_before_macro_expression231=date_before_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression231.getTree());

					}
					break;
				case 3 :
					// JPA2.g:281:7: date_after_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2488);
					date_after_macro_expression232=date_after_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression232.getTree());

					}
					break;
				case 4 :
					// JPA2.g:282:7: date_equals_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2496);
					date_equals_macro_expression233=date_equals_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression233.getTree());

					}
					break;
				case 5 :
					// JPA2.g:283:7: date_today_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2504);
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
	// JPA2.g:285:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ( ',' 'USER_TIMEZONE' )? ')' ;
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
		Token string_literal249=null;
		Token char_literal250=null;
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
		Object string_literal249_tree=null;
		Object char_literal250_tree=null;

		try {
			// JPA2.g:286:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ( ',' 'USER_TIMEZONE' )? ')' )
			// JPA2.g:286:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ( ',' 'USER_TIMEZONE' )? ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal235=(Token)match(input,73,FOLLOW_73_in_date_between_macro_expression2516); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal235_tree = (Object)adaptor.create(string_literal235);
			adaptor.addChild(root_0, string_literal235_tree);
			}

			char_literal236=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2518); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal236_tree = (Object)adaptor.create(char_literal236);
			adaptor.addChild(root_0, char_literal236_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2520);
			path_expression237=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression237.getTree());

			char_literal238=(Token)match(input,61,FOLLOW_61_in_date_between_macro_expression2522); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal238_tree = (Object)adaptor.create(char_literal238);
			adaptor.addChild(root_0, char_literal238_tree);
			}

			string_literal239=(Token)match(input,116,FOLLOW_116_in_date_between_macro_expression2524); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal239_tree = (Object)adaptor.create(string_literal239);
			adaptor.addChild(root_0, string_literal239_tree);
			}

			// JPA2.g:286:48: ( ( '+' | '-' ) numeric_literal )?
			int alt68=2;
			int LA68_0 = input.LA(1);
			if ( (LA68_0==60||LA68_0==62) ) {
				alt68=1;
			}
			switch (alt68) {
				case 1 :
					// JPA2.g:286:49: ( '+' | '-' ) numeric_literal
					{
					set240=input.LT(1);
					if ( input.LA(1)==60||input.LA(1)==62 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2535);
					numeric_literal241=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal241.getTree());

					}
					break;

			}

			char_literal242=(Token)match(input,61,FOLLOW_61_in_date_between_macro_expression2539); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal242_tree = (Object)adaptor.create(char_literal242);
			adaptor.addChild(root_0, char_literal242_tree);
			}

			string_literal243=(Token)match(input,116,FOLLOW_116_in_date_between_macro_expression2541); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal243_tree = (Object)adaptor.create(string_literal243);
			adaptor.addChild(root_0, string_literal243_tree);
			}

			// JPA2.g:286:89: ( ( '+' | '-' ) numeric_literal )?
			int alt69=2;
			int LA69_0 = input.LA(1);
			if ( (LA69_0==60||LA69_0==62) ) {
				alt69=1;
			}
			switch (alt69) {
				case 1 :
					// JPA2.g:286:90: ( '+' | '-' ) numeric_literal
					{
					set244=input.LT(1);
					if ( input.LA(1)==60||input.LA(1)==62 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2552);
					numeric_literal245=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal245.getTree());

					}
					break;

			}

			char_literal246=(Token)match(input,61,FOLLOW_61_in_date_between_macro_expression2556); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal246_tree = (Object)adaptor.create(char_literal246);
			adaptor.addChild(root_0, char_literal246_tree);
			}

			set247=input.LT(1);
			if ( input.LA(1)==91||input.LA(1)==103||input.LA(1)==112||input.LA(1)==114||input.LA(1)==126||input.LA(1)==144 ) {
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
			// JPA2.g:286:181: ( ',' 'USER_TIMEZONE' )?
			int alt70=2;
			int LA70_0 = input.LA(1);
			if ( (LA70_0==61) ) {
				alt70=1;
			}
			switch (alt70) {
				case 1 :
					// JPA2.g:286:182: ',' 'USER_TIMEZONE'
					{
					char_literal248=(Token)match(input,61,FOLLOW_61_in_date_between_macro_expression2582); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal248_tree = (Object)adaptor.create(char_literal248);
					adaptor.addChild(root_0, char_literal248_tree);
					}

					string_literal249=(Token)match(input,139,FOLLOW_139_in_date_between_macro_expression2584); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal249_tree = (Object)adaptor.create(string_literal249);
					adaptor.addChild(root_0, string_literal249_tree);
					}

					}
					break;

			}

			char_literal250=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2588); if (state.failed) return retval;
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
	// $ANTLR end "date_between_macro_expression"


	public static class date_before_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_before_macro_expression"
	// JPA2.g:288:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' ;
	public final JPA2Parser.date_before_macro_expression_return date_before_macro_expression() throws RecognitionException {
		JPA2Parser.date_before_macro_expression_return retval = new JPA2Parser.date_before_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal251=null;
		Token char_literal252=null;
		Token char_literal254=null;
		Token char_literal257=null;
		Token string_literal258=null;
		Token char_literal259=null;
		ParserRuleReturnScope path_expression253 =null;
		ParserRuleReturnScope path_expression255 =null;
		ParserRuleReturnScope input_parameter256 =null;

		Object string_literal251_tree=null;
		Object char_literal252_tree=null;
		Object char_literal254_tree=null;
		Object char_literal257_tree=null;
		Object string_literal258_tree=null;
		Object char_literal259_tree=null;

		try {
			// JPA2.g:289:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' )
			// JPA2.g:289:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal251=(Token)match(input,75,FOLLOW_75_in_date_before_macro_expression2600); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal251_tree = (Object)adaptor.create(string_literal251);
			adaptor.addChild(root_0, string_literal251_tree);
			}

			char_literal252=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2602); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal252_tree = (Object)adaptor.create(char_literal252);
			adaptor.addChild(root_0, char_literal252_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2604);
			path_expression253=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression253.getTree());

			char_literal254=(Token)match(input,61,FOLLOW_61_in_date_before_macro_expression2606); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal254_tree = (Object)adaptor.create(char_literal254);
			adaptor.addChild(root_0, char_literal254_tree);
			}

			// JPA2.g:289:45: ( path_expression | input_parameter )
			int alt71=2;
			int LA71_0 = input.LA(1);
			if ( (LA71_0==GROUP||LA71_0==WORD) ) {
				alt71=1;
			}
			else if ( (LA71_0==NAMED_PARAMETER||LA71_0==58||LA71_0==72) ) {
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
					// JPA2.g:289:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2609);
					path_expression255=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression255.getTree());

					}
					break;
				case 2 :
					// JPA2.g:289:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2613);
					input_parameter256=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter256.getTree());

					}
					break;

			}

			// JPA2.g:289:81: ( ',' 'USER_TIMEZONE' )?
			int alt72=2;
			int LA72_0 = input.LA(1);
			if ( (LA72_0==61) ) {
				alt72=1;
			}
			switch (alt72) {
				case 1 :
					// JPA2.g:289:82: ',' 'USER_TIMEZONE'
					{
					char_literal257=(Token)match(input,61,FOLLOW_61_in_date_before_macro_expression2617); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal257_tree = (Object)adaptor.create(char_literal257);
					adaptor.addChild(root_0, char_literal257_tree);
					}

					string_literal258=(Token)match(input,139,FOLLOW_139_in_date_before_macro_expression2619); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal258_tree = (Object)adaptor.create(string_literal258);
					adaptor.addChild(root_0, string_literal258_tree);
					}

					}
					break;

			}

			char_literal259=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2623); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal259_tree = (Object)adaptor.create(char_literal259);
			adaptor.addChild(root_0, char_literal259_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:291:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' ;
	public final JPA2Parser.date_after_macro_expression_return date_after_macro_expression() throws RecognitionException {
		JPA2Parser.date_after_macro_expression_return retval = new JPA2Parser.date_after_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal260=null;
		Token char_literal261=null;
		Token char_literal263=null;
		Token char_literal266=null;
		Token string_literal267=null;
		Token char_literal268=null;
		ParserRuleReturnScope path_expression262 =null;
		ParserRuleReturnScope path_expression264 =null;
		ParserRuleReturnScope input_parameter265 =null;

		Object string_literal260_tree=null;
		Object char_literal261_tree=null;
		Object char_literal263_tree=null;
		Object char_literal266_tree=null;
		Object string_literal267_tree=null;
		Object char_literal268_tree=null;

		try {
			// JPA2.g:292:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' )
			// JPA2.g:292:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal260=(Token)match(input,74,FOLLOW_74_in_date_after_macro_expression2635); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal260_tree = (Object)adaptor.create(string_literal260);
			adaptor.addChild(root_0, string_literal260_tree);
			}

			char_literal261=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2637); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal261_tree = (Object)adaptor.create(char_literal261);
			adaptor.addChild(root_0, char_literal261_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2639);
			path_expression262=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression262.getTree());

			char_literal263=(Token)match(input,61,FOLLOW_61_in_date_after_macro_expression2641); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal263_tree = (Object)adaptor.create(char_literal263);
			adaptor.addChild(root_0, char_literal263_tree);
			}

			// JPA2.g:292:44: ( path_expression | input_parameter )
			int alt73=2;
			int LA73_0 = input.LA(1);
			if ( (LA73_0==GROUP||LA73_0==WORD) ) {
				alt73=1;
			}
			else if ( (LA73_0==NAMED_PARAMETER||LA73_0==58||LA73_0==72) ) {
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
					// JPA2.g:292:45: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2644);
					path_expression264=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression264.getTree());

					}
					break;
				case 2 :
					// JPA2.g:292:63: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2648);
					input_parameter265=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter265.getTree());

					}
					break;

			}

			// JPA2.g:292:80: ( ',' 'USER_TIMEZONE' )?
			int alt74=2;
			int LA74_0 = input.LA(1);
			if ( (LA74_0==61) ) {
				alt74=1;
			}
			switch (alt74) {
				case 1 :
					// JPA2.g:292:81: ',' 'USER_TIMEZONE'
					{
					char_literal266=(Token)match(input,61,FOLLOW_61_in_date_after_macro_expression2652); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal266_tree = (Object)adaptor.create(char_literal266);
					adaptor.addChild(root_0, char_literal266_tree);
					}

					string_literal267=(Token)match(input,139,FOLLOW_139_in_date_after_macro_expression2654); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal267_tree = (Object)adaptor.create(string_literal267);
					adaptor.addChild(root_0, string_literal267_tree);
					}

					}
					break;

			}

			char_literal268=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2658); if (state.failed) return retval;
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
	// $ANTLR end "date_after_macro_expression"


	public static class date_equals_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_equals_macro_expression"
	// JPA2.g:294:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' ;
	public final JPA2Parser.date_equals_macro_expression_return date_equals_macro_expression() throws RecognitionException {
		JPA2Parser.date_equals_macro_expression_return retval = new JPA2Parser.date_equals_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal269=null;
		Token char_literal270=null;
		Token char_literal272=null;
		Token char_literal275=null;
		Token string_literal276=null;
		Token char_literal277=null;
		ParserRuleReturnScope path_expression271 =null;
		ParserRuleReturnScope path_expression273 =null;
		ParserRuleReturnScope input_parameter274 =null;

		Object string_literal269_tree=null;
		Object char_literal270_tree=null;
		Object char_literal272_tree=null;
		Object char_literal275_tree=null;
		Object string_literal276_tree=null;
		Object char_literal277_tree=null;

		try {
			// JPA2.g:295:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' )
			// JPA2.g:295:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal269=(Token)match(input,76,FOLLOW_76_in_date_equals_macro_expression2670); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal269_tree = (Object)adaptor.create(string_literal269);
			adaptor.addChild(root_0, string_literal269_tree);
			}

			char_literal270=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2672); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal270_tree = (Object)adaptor.create(char_literal270);
			adaptor.addChild(root_0, char_literal270_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2674);
			path_expression271=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression271.getTree());

			char_literal272=(Token)match(input,61,FOLLOW_61_in_date_equals_macro_expression2676); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal272_tree = (Object)adaptor.create(char_literal272);
			adaptor.addChild(root_0, char_literal272_tree);
			}

			// JPA2.g:295:45: ( path_expression | input_parameter )
			int alt75=2;
			int LA75_0 = input.LA(1);
			if ( (LA75_0==GROUP||LA75_0==WORD) ) {
				alt75=1;
			}
			else if ( (LA75_0==NAMED_PARAMETER||LA75_0==58||LA75_0==72) ) {
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
					// JPA2.g:295:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2679);
					path_expression273=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression273.getTree());

					}
					break;
				case 2 :
					// JPA2.g:295:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2683);
					input_parameter274=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter274.getTree());

					}
					break;

			}

			// JPA2.g:295:81: ( ',' 'USER_TIMEZONE' )?
			int alt76=2;
			int LA76_0 = input.LA(1);
			if ( (LA76_0==61) ) {
				alt76=1;
			}
			switch (alt76) {
				case 1 :
					// JPA2.g:295:82: ',' 'USER_TIMEZONE'
					{
					char_literal275=(Token)match(input,61,FOLLOW_61_in_date_equals_macro_expression2687); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal275_tree = (Object)adaptor.create(char_literal275);
					adaptor.addChild(root_0, char_literal275_tree);
					}

					string_literal276=(Token)match(input,139,FOLLOW_139_in_date_equals_macro_expression2689); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal276_tree = (Object)adaptor.create(string_literal276);
					adaptor.addChild(root_0, string_literal276_tree);
					}

					}
					break;

			}

			char_literal277=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2693); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal277_tree = (Object)adaptor.create(char_literal277);
			adaptor.addChild(root_0, char_literal277_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:297:1: date_today_macro_expression : '@TODAY' '(' path_expression ( ',' 'USER_TIMEZONE' )? ')' ;
	public final JPA2Parser.date_today_macro_expression_return date_today_macro_expression() throws RecognitionException {
		JPA2Parser.date_today_macro_expression_return retval = new JPA2Parser.date_today_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal278=null;
		Token char_literal279=null;
		Token char_literal281=null;
		Token string_literal282=null;
		Token char_literal283=null;
		ParserRuleReturnScope path_expression280 =null;

		Object string_literal278_tree=null;
		Object char_literal279_tree=null;
		Object char_literal281_tree=null;
		Object string_literal282_tree=null;
		Object char_literal283_tree=null;

		try {
			// JPA2.g:298:5: ( '@TODAY' '(' path_expression ( ',' 'USER_TIMEZONE' )? ')' )
			// JPA2.g:298:7: '@TODAY' '(' path_expression ( ',' 'USER_TIMEZONE' )? ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal278=(Token)match(input,78,FOLLOW_78_in_date_today_macro_expression2705); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal278_tree = (Object)adaptor.create(string_literal278);
			adaptor.addChild(root_0, string_literal278_tree);
			}

			char_literal279=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2707); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal279_tree = (Object)adaptor.create(char_literal279);
			adaptor.addChild(root_0, char_literal279_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2709);
			path_expression280=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression280.getTree());

			// JPA2.g:298:36: ( ',' 'USER_TIMEZONE' )?
			int alt77=2;
			int LA77_0 = input.LA(1);
			if ( (LA77_0==61) ) {
				alt77=1;
			}
			switch (alt77) {
				case 1 :
					// JPA2.g:298:37: ',' 'USER_TIMEZONE'
					{
					char_literal281=(Token)match(input,61,FOLLOW_61_in_date_today_macro_expression2712); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal281_tree = (Object)adaptor.create(char_literal281);
					adaptor.addChild(root_0, char_literal281_tree);
					}

					string_literal282=(Token)match(input,139,FOLLOW_139_in_date_today_macro_expression2714); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal282_tree = (Object)adaptor.create(string_literal282);
					adaptor.addChild(root_0, string_literal282_tree);
					}

					}
					break;

			}

			char_literal283=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2718); if (state.failed) return retval;
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
	// JPA2.g:301:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
	public final JPA2Parser.between_expression_return between_expression() throws RecognitionException {
		JPA2Parser.between_expression_return retval = new JPA2Parser.between_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal285=null;
		Token string_literal286=null;
		Token string_literal288=null;
		Token string_literal291=null;
		Token string_literal292=null;
		Token string_literal294=null;
		Token string_literal297=null;
		Token string_literal298=null;
		Token string_literal300=null;
		ParserRuleReturnScope arithmetic_expression284 =null;
		ParserRuleReturnScope arithmetic_expression287 =null;
		ParserRuleReturnScope arithmetic_expression289 =null;
		ParserRuleReturnScope string_expression290 =null;
		ParserRuleReturnScope string_expression293 =null;
		ParserRuleReturnScope string_expression295 =null;
		ParserRuleReturnScope datetime_expression296 =null;
		ParserRuleReturnScope datetime_expression299 =null;
		ParserRuleReturnScope datetime_expression301 =null;

		Object string_literal285_tree=null;
		Object string_literal286_tree=null;
		Object string_literal288_tree=null;
		Object string_literal291_tree=null;
		Object string_literal292_tree=null;
		Object string_literal294_tree=null;
		Object string_literal297_tree=null;
		Object string_literal298_tree=null;
		Object string_literal300_tree=null;

		try {
			// JPA2.g:302:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
			int alt81=3;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 60:
			case 62:
			case 65:
			case 79:
			case 104:
			case 108:
			case 110:
			case 113:
			case 128:
			case 130:
				{
				alt81=1;
				}
				break;
			case WORD:
				{
				int LA81_2 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case LPAREN:
				{
				int LA81_5 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case 72:
				{
				int LA81_6 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA81_7 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case 58:
				{
				int LA81_8 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case COUNT:
				{
				int LA81_16 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA81_17 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case 102:
				{
				int LA81_18 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case 84:
				{
				int LA81_19 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case 86:
				{
				int LA81_20 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case 118:
				{
				int LA81_21 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case 85:
				{
				int LA81_22 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case 100:
				{
				int LA81_23 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case 77:
				{
				int LA81_24 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 87:
			case 131:
			case 135:
			case 138:
				{
				alt81=2;
				}
				break;
			case 88:
			case 89:
			case 90:
				{
				alt81=3;
				}
				break;
			case GROUP:
				{
				int LA81_32 = input.LA(2);
				if ( (synpred128_JPA2()) ) {
					alt81=1;
				}
				else if ( (synpred130_JPA2()) ) {
					alt81=2;
				}
				else if ( (true) ) {
					alt81=3;
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
					// JPA2.g:302:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2731);
					arithmetic_expression284=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression284.getTree());

					// JPA2.g:302:29: ( 'NOT' )?
					int alt78=2;
					int LA78_0 = input.LA(1);
					if ( (LA78_0==NOT) ) {
						alt78=1;
					}
					switch (alt78) {
						case 1 :
							// JPA2.g:302:30: 'NOT'
							{
							string_literal285=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2734); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal285_tree = (Object)adaptor.create(string_literal285);
							adaptor.addChild(root_0, string_literal285_tree);
							}

							}
							break;

					}

					string_literal286=(Token)match(input,82,FOLLOW_82_in_between_expression2738); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal286_tree = (Object)adaptor.create(string_literal286);
					adaptor.addChild(root_0, string_literal286_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2740);
					arithmetic_expression287=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression287.getTree());

					string_literal288=(Token)match(input,AND,FOLLOW_AND_in_between_expression2742); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal288_tree = (Object)adaptor.create(string_literal288);
					adaptor.addChild(root_0, string_literal288_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2744);
					arithmetic_expression289=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression289.getTree());

					}
					break;
				case 2 :
					// JPA2.g:303:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_between_expression2752);
					string_expression290=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression290.getTree());

					// JPA2.g:303:25: ( 'NOT' )?
					int alt79=2;
					int LA79_0 = input.LA(1);
					if ( (LA79_0==NOT) ) {
						alt79=1;
					}
					switch (alt79) {
						case 1 :
							// JPA2.g:303:26: 'NOT'
							{
							string_literal291=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2755); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal291_tree = (Object)adaptor.create(string_literal291);
							adaptor.addChild(root_0, string_literal291_tree);
							}

							}
							break;

					}

					string_literal292=(Token)match(input,82,FOLLOW_82_in_between_expression2759); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal292_tree = (Object)adaptor.create(string_literal292);
					adaptor.addChild(root_0, string_literal292_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2761);
					string_expression293=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression293.getTree());

					string_literal294=(Token)match(input,AND,FOLLOW_AND_in_between_expression2763); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal294_tree = (Object)adaptor.create(string_literal294);
					adaptor.addChild(root_0, string_literal294_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2765);
					string_expression295=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression295.getTree());

					}
					break;
				case 3 :
					// JPA2.g:304:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_between_expression2773);
					datetime_expression296=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression296.getTree());

					// JPA2.g:304:27: ( 'NOT' )?
					int alt80=2;
					int LA80_0 = input.LA(1);
					if ( (LA80_0==NOT) ) {
						alt80=1;
					}
					switch (alt80) {
						case 1 :
							// JPA2.g:304:28: 'NOT'
							{
							string_literal297=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2776); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal297_tree = (Object)adaptor.create(string_literal297);
							adaptor.addChild(root_0, string_literal297_tree);
							}

							}
							break;

					}

					string_literal298=(Token)match(input,82,FOLLOW_82_in_between_expression2780); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal298_tree = (Object)adaptor.create(string_literal298);
					adaptor.addChild(root_0, string_literal298_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2782);
					datetime_expression299=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression299.getTree());

					string_literal300=(Token)match(input,AND,FOLLOW_AND_in_between_expression2784); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal300_tree = (Object)adaptor.create(string_literal300);
					adaptor.addChild(root_0, string_literal300_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2786);
					datetime_expression301=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression301.getTree());

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
	// JPA2.g:305:1: in_expression : ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) ;
	public final JPA2Parser.in_expression_return in_expression() throws RecognitionException {
		JPA2Parser.in_expression_return retval = new JPA2Parser.in_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NOT305=null;
		Token IN306=null;
		Token char_literal307=null;
		Token char_literal309=null;
		Token char_literal311=null;
		Token char_literal314=null;
		Token char_literal316=null;
		ParserRuleReturnScope path_expression302 =null;
		ParserRuleReturnScope type_discriminator303 =null;
		ParserRuleReturnScope identification_variable304 =null;
		ParserRuleReturnScope in_item308 =null;
		ParserRuleReturnScope in_item310 =null;
		ParserRuleReturnScope subquery312 =null;
		ParserRuleReturnScope collection_valued_input_parameter313 =null;
		ParserRuleReturnScope path_expression315 =null;

		Object NOT305_tree=null;
		Object IN306_tree=null;
		Object char_literal307_tree=null;
		Object char_literal309_tree=null;
		Object char_literal311_tree=null;
		Object char_literal314_tree=null;
		Object char_literal316_tree=null;

		try {
			// JPA2.g:306:5: ( ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) )
			// JPA2.g:306:7: ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:306:7: ( path_expression | type_discriminator | identification_variable )
			int alt82=3;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==GROUP||LA82_0==WORD) ) {
				int LA82_1 = input.LA(2);
				if ( (LA82_1==63) ) {
					alt82=1;
				}
				else if ( (LA82_1==IN||LA82_1==NOT) ) {
					alt82=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 82, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA82_0==136) ) {
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
					// JPA2.g:306:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_in_expression2798);
					path_expression302=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression302.getTree());

					}
					break;
				case 2 :
					// JPA2.g:306:26: type_discriminator
					{
					pushFollow(FOLLOW_type_discriminator_in_in_expression2802);
					type_discriminator303=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator303.getTree());

					}
					break;
				case 3 :
					// JPA2.g:306:47: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_in_expression2806);
					identification_variable304=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable304.getTree());

					}
					break;

			}

			// JPA2.g:306:72: ( NOT )?
			int alt83=2;
			int LA83_0 = input.LA(1);
			if ( (LA83_0==NOT) ) {
				alt83=1;
			}
			switch (alt83) {
				case 1 :
					// JPA2.g:306:73: NOT
					{
					NOT305=(Token)match(input,NOT,FOLLOW_NOT_in_in_expression2810); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT305_tree = (Object)adaptor.create(NOT305);
					adaptor.addChild(root_0, NOT305_tree);
					}

					}
					break;

			}

			IN306=(Token)match(input,IN,FOLLOW_IN_in_in_expression2814); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN306_tree = (Object)adaptor.create(IN306);
			adaptor.addChild(root_0, IN306_tree);
			}

			// JPA2.g:307:13: ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			int alt85=4;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==LPAREN) ) {
				switch ( input.LA(2) ) {
				case 127:
					{
					alt85=2;
					}
					break;
				case INT_NUMERAL:
				case NAMED_PARAMETER:
				case STRING_LITERAL:
				case 58:
				case 65:
				case 72:
				case 77:
					{
					alt85=1;
					}
					break;
				case GROUP:
				case WORD:
					{
					alt85=4;
					}
					break;
				default:
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
			else if ( (LA85_0==NAMED_PARAMETER||LA85_0==58||LA85_0==72) ) {
				alt85=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 85, 0, input);
				throw nvae;
			}

			switch (alt85) {
				case 1 :
					// JPA2.g:307:15: '(' in_item ( ',' in_item )* ')'
					{
					char_literal307=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2830); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal307_tree = (Object)adaptor.create(char_literal307);
					adaptor.addChild(root_0, char_literal307_tree);
					}

					pushFollow(FOLLOW_in_item_in_in_expression2832);
					in_item308=in_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item308.getTree());

					// JPA2.g:307:27: ( ',' in_item )*
					loop84:
					while (true) {
						int alt84=2;
						int LA84_0 = input.LA(1);
						if ( (LA84_0==61) ) {
							alt84=1;
						}

						switch (alt84) {
						case 1 :
							// JPA2.g:307:28: ',' in_item
							{
							char_literal309=(Token)match(input,61,FOLLOW_61_in_in_expression2835); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal309_tree = (Object)adaptor.create(char_literal309);
							adaptor.addChild(root_0, char_literal309_tree);
							}

							pushFollow(FOLLOW_in_item_in_in_expression2837);
							in_item310=in_item();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item310.getTree());

							}
							break;

						default :
							break loop84;
						}
					}

					char_literal311=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2841); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal311_tree = (Object)adaptor.create(char_literal311);
					adaptor.addChild(root_0, char_literal311_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:308:15: subquery
					{
					pushFollow(FOLLOW_subquery_in_in_expression2857);
					subquery312=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery312.getTree());

					}
					break;
				case 3 :
					// JPA2.g:309:15: collection_valued_input_parameter
					{
					pushFollow(FOLLOW_collection_valued_input_parameter_in_in_expression2873);
					collection_valued_input_parameter313=collection_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_input_parameter313.getTree());

					}
					break;
				case 4 :
					// JPA2.g:310:15: '(' path_expression ')'
					{
					char_literal314=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2889); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal314_tree = (Object)adaptor.create(char_literal314);
					adaptor.addChild(root_0, char_literal314_tree);
					}

					pushFollow(FOLLOW_path_expression_in_in_expression2891);
					path_expression315=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression315.getTree());

					char_literal316=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2893); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal316_tree = (Object)adaptor.create(char_literal316);
					adaptor.addChild(root_0, char_literal316_tree);
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
	// JPA2.g:316:1: in_item : ( string_literal | numeric_literal | single_valued_input_parameter | enum_function );
	public final JPA2Parser.in_item_return in_item() throws RecognitionException {
		JPA2Parser.in_item_return retval = new JPA2Parser.in_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal317 =null;
		ParserRuleReturnScope numeric_literal318 =null;
		ParserRuleReturnScope single_valued_input_parameter319 =null;
		ParserRuleReturnScope enum_function320 =null;


		try {
			// JPA2.g:317:5: ( string_literal | numeric_literal | single_valued_input_parameter | enum_function )
			int alt86=4;
			switch ( input.LA(1) ) {
			case STRING_LITERAL:
				{
				alt86=1;
				}
				break;
			case INT_NUMERAL:
			case 65:
				{
				alt86=2;
				}
				break;
			case NAMED_PARAMETER:
			case 58:
			case 72:
				{
				alt86=3;
				}
				break;
			case 77:
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
					// JPA2.g:317:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_in_item2921);
					string_literal317=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal317.getTree());

					}
					break;
				case 2 :
					// JPA2.g:317:24: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_in_item2925);
					numeric_literal318=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal318.getTree());

					}
					break;
				case 3 :
					// JPA2.g:317:42: single_valued_input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_single_valued_input_parameter_in_in_item2929);
					single_valued_input_parameter319=single_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_input_parameter319.getTree());

					}
					break;
				case 4 :
					// JPA2.g:317:74: enum_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_function_in_in_item2933);
					enum_function320=enum_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_function320.getTree());

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
	// JPA2.g:318:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? ;
	public final JPA2Parser.like_expression_return like_expression() throws RecognitionException {
		JPA2Parser.like_expression_return retval = new JPA2Parser.like_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal322=null;
		Token string_literal323=null;
		Token string_literal327=null;
		ParserRuleReturnScope string_expression321 =null;
		ParserRuleReturnScope string_expression324 =null;
		ParserRuleReturnScope pattern_value325 =null;
		ParserRuleReturnScope input_parameter326 =null;
		ParserRuleReturnScope escape_character328 =null;

		Object string_literal322_tree=null;
		Object string_literal323_tree=null;
		Object string_literal327_tree=null;

		try {
			// JPA2.g:319:5: ( string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? )
			// JPA2.g:319:7: string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_expression_in_like_expression2944);
			string_expression321=string_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression321.getTree());

			// JPA2.g:319:25: ( 'NOT' )?
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==NOT) ) {
				alt87=1;
			}
			switch (alt87) {
				case 1 :
					// JPA2.g:319:26: 'NOT'
					{
					string_literal322=(Token)match(input,NOT,FOLLOW_NOT_in_like_expression2947); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal322_tree = (Object)adaptor.create(string_literal322);
					adaptor.addChild(root_0, string_literal322_tree);
					}

					}
					break;

			}

			string_literal323=(Token)match(input,109,FOLLOW_109_in_like_expression2951); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal323_tree = (Object)adaptor.create(string_literal323);
			adaptor.addChild(root_0, string_literal323_tree);
			}

			// JPA2.g:319:41: ( string_expression | pattern_value | input_parameter )
			int alt88=3;
			switch ( input.LA(1) ) {
			case AVG:
			case COUNT:
			case GROUP:
			case LOWER:
			case LPAREN:
			case MAX:
			case MIN:
			case SUM:
			case WORD:
			case 77:
			case 84:
			case 85:
			case 86:
			case 87:
			case 100:
			case 102:
			case 118:
			case 131:
			case 135:
			case 138:
				{
				alt88=1;
				}
				break;
			case STRING_LITERAL:
				{
				int LA88_2 = input.LA(2);
				if ( (synpred143_JPA2()) ) {
					alt88=1;
				}
				else if ( (synpred144_JPA2()) ) {
					alt88=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 88, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 72:
				{
				int LA88_3 = input.LA(2);
				if ( (LA88_3==65) ) {
					int LA88_7 = input.LA(3);
					if ( (LA88_7==INT_NUMERAL) ) {
						int LA88_11 = input.LA(4);
						if ( (synpred143_JPA2()) ) {
							alt88=1;
						}
						else if ( (true) ) {
							alt88=3;
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
								new NoViableAltException("", 88, 7, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA88_3==INT_NUMERAL) ) {
					int LA88_8 = input.LA(3);
					if ( (synpred143_JPA2()) ) {
						alt88=1;
					}
					else if ( (true) ) {
						alt88=3;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 88, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA88_4 = input.LA(2);
				if ( (synpred143_JPA2()) ) {
					alt88=1;
				}
				else if ( (true) ) {
					alt88=3;
				}

				}
				break;
			case 58:
				{
				int LA88_5 = input.LA(2);
				if ( (LA88_5==WORD) ) {
					int LA88_10 = input.LA(3);
					if ( (LA88_10==147) ) {
						int LA88_12 = input.LA(4);
						if ( (synpred143_JPA2()) ) {
							alt88=1;
						}
						else if ( (true) ) {
							alt88=3;
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
								new NoViableAltException("", 88, 10, input);
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
							new NoViableAltException("", 88, 5, input);
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
					new NoViableAltException("", 88, 0, input);
				throw nvae;
			}
			switch (alt88) {
				case 1 :
					// JPA2.g:319:42: string_expression
					{
					pushFollow(FOLLOW_string_expression_in_like_expression2954);
					string_expression324=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression324.getTree());

					}
					break;
				case 2 :
					// JPA2.g:319:62: pattern_value
					{
					pushFollow(FOLLOW_pattern_value_in_like_expression2958);
					pattern_value325=pattern_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value325.getTree());

					}
					break;
				case 3 :
					// JPA2.g:319:78: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_like_expression2962);
					input_parameter326=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter326.getTree());

					}
					break;

			}

			// JPA2.g:319:94: ( 'ESCAPE' escape_character )?
			int alt89=2;
			int LA89_0 = input.LA(1);
			if ( (LA89_0==98) ) {
				alt89=1;
			}
			switch (alt89) {
				case 1 :
					// JPA2.g:319:95: 'ESCAPE' escape_character
					{
					string_literal327=(Token)match(input,98,FOLLOW_98_in_like_expression2965); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal327_tree = (Object)adaptor.create(string_literal327);
					adaptor.addChild(root_0, string_literal327_tree);
					}

					pushFollow(FOLLOW_escape_character_in_like_expression2967);
					escape_character328=escape_character();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, escape_character328.getTree());

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
	// JPA2.g:320:1: null_comparison_expression : ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' ;
	public final JPA2Parser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
		JPA2Parser.null_comparison_expression_return retval = new JPA2Parser.null_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal332=null;
		Token string_literal333=null;
		Token string_literal334=null;
		ParserRuleReturnScope path_expression329 =null;
		ParserRuleReturnScope input_parameter330 =null;
		ParserRuleReturnScope join_association_path_expression331 =null;

		Object string_literal332_tree=null;
		Object string_literal333_tree=null;
		Object string_literal334_tree=null;

		try {
			// JPA2.g:321:5: ( ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' )
			// JPA2.g:321:7: ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:321:7: ( path_expression | input_parameter | join_association_path_expression )
			int alt90=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA90_1 = input.LA(2);
				if ( (LA90_1==63) ) {
					int LA90_5 = input.LA(3);
					if ( (synpred146_JPA2()) ) {
						alt90=1;
					}
					else if ( (true) ) {
						alt90=3;
					}

				}
				else if ( (LA90_1==105) ) {
					alt90=3;
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
			case 58:
			case 72:
				{
				alt90=2;
				}
				break;
			case 134:
				{
				alt90=3;
				}
				break;
			case GROUP:
				{
				int LA90_4 = input.LA(2);
				if ( (LA90_4==63) ) {
					int LA90_6 = input.LA(3);
					if ( (synpred146_JPA2()) ) {
						alt90=1;
					}
					else if ( (true) ) {
						alt90=3;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 90, 4, input);
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
					new NoViableAltException("", 90, 0, input);
				throw nvae;
			}
			switch (alt90) {
				case 1 :
					// JPA2.g:321:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_null_comparison_expression2981);
					path_expression329=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression329.getTree());

					}
					break;
				case 2 :
					// JPA2.g:321:26: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_null_comparison_expression2985);
					input_parameter330=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter330.getTree());

					}
					break;
				case 3 :
					// JPA2.g:321:44: join_association_path_expression
					{
					pushFollow(FOLLOW_join_association_path_expression_in_null_comparison_expression2989);
					join_association_path_expression331=join_association_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression331.getTree());

					}
					break;

			}

			string_literal332=(Token)match(input,105,FOLLOW_105_in_null_comparison_expression2992); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal332_tree = (Object)adaptor.create(string_literal332);
			adaptor.addChild(root_0, string_literal332_tree);
			}

			// JPA2.g:321:83: ( 'NOT' )?
			int alt91=2;
			int LA91_0 = input.LA(1);
			if ( (LA91_0==NOT) ) {
				alt91=1;
			}
			switch (alt91) {
				case 1 :
					// JPA2.g:321:84: 'NOT'
					{
					string_literal333=(Token)match(input,NOT,FOLLOW_NOT_in_null_comparison_expression2995); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal333_tree = (Object)adaptor.create(string_literal333);
					adaptor.addChild(root_0, string_literal333_tree);
					}

					}
					break;

			}

			string_literal334=(Token)match(input,117,FOLLOW_117_in_null_comparison_expression2999); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal334_tree = (Object)adaptor.create(string_literal334);
			adaptor.addChild(root_0, string_literal334_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:322:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
	public final JPA2Parser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
		JPA2Parser.empty_collection_comparison_expression_return retval = new JPA2Parser.empty_collection_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal336=null;
		Token string_literal337=null;
		Token string_literal338=null;
		ParserRuleReturnScope path_expression335 =null;

		Object string_literal336_tree=null;
		Object string_literal337_tree=null;
		Object string_literal338_tree=null;

		try {
			// JPA2.g:323:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
			// JPA2.g:323:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression3010);
			path_expression335=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression335.getTree());

			string_literal336=(Token)match(input,105,FOLLOW_105_in_empty_collection_comparison_expression3012); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal336_tree = (Object)adaptor.create(string_literal336);
			adaptor.addChild(root_0, string_literal336_tree);
			}

			// JPA2.g:323:28: ( 'NOT' )?
			int alt92=2;
			int LA92_0 = input.LA(1);
			if ( (LA92_0==NOT) ) {
				alt92=1;
			}
			switch (alt92) {
				case 1 :
					// JPA2.g:323:29: 'NOT'
					{
					string_literal337=(Token)match(input,NOT,FOLLOW_NOT_in_empty_collection_comparison_expression3015); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal337_tree = (Object)adaptor.create(string_literal337);
					adaptor.addChild(root_0, string_literal337_tree);
					}

					}
					break;

			}

			string_literal338=(Token)match(input,94,FOLLOW_94_in_empty_collection_comparison_expression3019); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal338_tree = (Object)adaptor.create(string_literal338);
			adaptor.addChild(root_0, string_literal338_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:324:1: collection_member_expression : entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
	public final JPA2Parser.collection_member_expression_return collection_member_expression() throws RecognitionException {
		JPA2Parser.collection_member_expression_return retval = new JPA2Parser.collection_member_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal340=null;
		Token string_literal341=null;
		Token string_literal342=null;
		ParserRuleReturnScope entity_or_value_expression339 =null;
		ParserRuleReturnScope path_expression343 =null;

		Object string_literal340_tree=null;
		Object string_literal341_tree=null;
		Object string_literal342_tree=null;

		try {
			// JPA2.g:325:5: ( entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
			// JPA2.g:325:7: entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_or_value_expression_in_collection_member_expression3030);
			entity_or_value_expression339=entity_or_value_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_or_value_expression339.getTree());

			// JPA2.g:325:35: ( 'NOT' )?
			int alt93=2;
			int LA93_0 = input.LA(1);
			if ( (LA93_0==NOT) ) {
				alt93=1;
			}
			switch (alt93) {
				case 1 :
					// JPA2.g:325:36: 'NOT'
					{
					string_literal340=(Token)match(input,NOT,FOLLOW_NOT_in_collection_member_expression3034); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal340_tree = (Object)adaptor.create(string_literal340);
					adaptor.addChild(root_0, string_literal340_tree);
					}

					}
					break;

			}

			string_literal341=(Token)match(input,111,FOLLOW_111_in_collection_member_expression3038); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal341_tree = (Object)adaptor.create(string_literal341);
			adaptor.addChild(root_0, string_literal341_tree);
			}

			// JPA2.g:325:53: ( 'OF' )?
			int alt94=2;
			int LA94_0 = input.LA(1);
			if ( (LA94_0==122) ) {
				alt94=1;
			}
			switch (alt94) {
				case 1 :
					// JPA2.g:325:54: 'OF'
					{
					string_literal342=(Token)match(input,122,FOLLOW_122_in_collection_member_expression3041); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal342_tree = (Object)adaptor.create(string_literal342);
					adaptor.addChild(root_0, string_literal342_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_path_expression_in_collection_member_expression3045);
			path_expression343=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression343.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:326:1: entity_or_value_expression : ( path_expression | simple_entity_or_value_expression | subquery );
	public final JPA2Parser.entity_or_value_expression_return entity_or_value_expression() throws RecognitionException {
		JPA2Parser.entity_or_value_expression_return retval = new JPA2Parser.entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression344 =null;
		ParserRuleReturnScope simple_entity_or_value_expression345 =null;
		ParserRuleReturnScope subquery346 =null;


		try {
			// JPA2.g:327:5: ( path_expression | simple_entity_or_value_expression | subquery )
			int alt95=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA95_1 = input.LA(2);
				if ( (LA95_1==63) ) {
					alt95=1;
				}
				else if ( (LA95_1==NOT||LA95_1==111) ) {
					alt95=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 95, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
			case 58:
			case 72:
				{
				alt95=2;
				}
				break;
			case GROUP:
				{
				int LA95_3 = input.LA(2);
				if ( (LA95_3==63) ) {
					alt95=1;
				}
				else if ( (LA95_3==NOT||LA95_3==111) ) {
					alt95=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 95, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LPAREN:
				{
				alt95=3;
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
					// JPA2.g:327:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_or_value_expression3056);
					path_expression344=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression344.getTree());

					}
					break;
				case 2 :
					// JPA2.g:328:7: simple_entity_or_value_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression3064);
					simple_entity_or_value_expression345=simple_entity_or_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_or_value_expression345.getTree());

					}
					break;
				case 3 :
					// JPA2.g:329:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_entity_or_value_expression3072);
					subquery346=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery346.getTree());

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
	// JPA2.g:330:1: simple_entity_or_value_expression : ( identification_variable | input_parameter | literal );
	public final JPA2Parser.simple_entity_or_value_expression_return simple_entity_or_value_expression() throws RecognitionException {
		JPA2Parser.simple_entity_or_value_expression_return retval = new JPA2Parser.simple_entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable347 =null;
		ParserRuleReturnScope input_parameter348 =null;
		ParserRuleReturnScope literal349 =null;


		try {
			// JPA2.g:331:5: ( identification_variable | input_parameter | literal )
			int alt96=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA96_1 = input.LA(2);
				if ( (synpred154_JPA2()) ) {
					alt96=1;
				}
				else if ( (true) ) {
					alt96=3;
				}

				}
				break;
			case NAMED_PARAMETER:
			case 58:
			case 72:
				{
				alt96=2;
				}
				break;
			case GROUP:
				{
				alt96=1;
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
					// JPA2.g:331:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_or_value_expression3083);
					identification_variable347=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable347.getTree());

					}
					break;
				case 2 :
					// JPA2.g:332:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_or_value_expression3091);
					input_parameter348=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter348.getTree());

					}
					break;
				case 3 :
					// JPA2.g:333:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_simple_entity_or_value_expression3099);
					literal349=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal349.getTree());

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
	// JPA2.g:334:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
	public final JPA2Parser.exists_expression_return exists_expression() throws RecognitionException {
		JPA2Parser.exists_expression_return retval = new JPA2Parser.exists_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal350=null;
		Token string_literal351=null;
		ParserRuleReturnScope subquery352 =null;

		Object string_literal350_tree=null;
		Object string_literal351_tree=null;

		try {
			// JPA2.g:335:5: ( ( 'NOT' )? 'EXISTS' subquery )
			// JPA2.g:335:7: ( 'NOT' )? 'EXISTS' subquery
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:335:7: ( 'NOT' )?
			int alt97=2;
			int LA97_0 = input.LA(1);
			if ( (LA97_0==NOT) ) {
				alt97=1;
			}
			switch (alt97) {
				case 1 :
					// JPA2.g:335:8: 'NOT'
					{
					string_literal350=(Token)match(input,NOT,FOLLOW_NOT_in_exists_expression3111); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal350_tree = (Object)adaptor.create(string_literal350);
					adaptor.addChild(root_0, string_literal350_tree);
					}

					}
					break;

			}

			string_literal351=(Token)match(input,99,FOLLOW_99_in_exists_expression3115); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal351_tree = (Object)adaptor.create(string_literal351);
			adaptor.addChild(root_0, string_literal351_tree);
			}

			pushFollow(FOLLOW_subquery_in_exists_expression3117);
			subquery352=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery352.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:336:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
	public final JPA2Parser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
		JPA2Parser.all_or_any_expression_return retval = new JPA2Parser.all_or_any_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set353=null;
		ParserRuleReturnScope subquery354 =null;

		Object set353_tree=null;

		try {
			// JPA2.g:337:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
			// JPA2.g:337:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
			{
			root_0 = (Object)adaptor.nil();


			set353=input.LT(1);
			if ( (input.LA(1) >= 80 && input.LA(1) <= 81)||input.LA(1)==129 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set353));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_subquery_in_all_or_any_expression3141);
			subquery354=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery354.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:338:1: comparison_expression : ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
	public final JPA2Parser.comparison_expression_return comparison_expression() throws RecognitionException {
		JPA2Parser.comparison_expression_return retval = new JPA2Parser.comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal357=null;
		Token set361=null;
		Token set365=null;
		Token set373=null;
		Token set377=null;
		ParserRuleReturnScope string_expression355 =null;
		ParserRuleReturnScope comparison_operator356 =null;
		ParserRuleReturnScope string_expression358 =null;
		ParserRuleReturnScope all_or_any_expression359 =null;
		ParserRuleReturnScope boolean_expression360 =null;
		ParserRuleReturnScope boolean_expression362 =null;
		ParserRuleReturnScope all_or_any_expression363 =null;
		ParserRuleReturnScope enum_expression364 =null;
		ParserRuleReturnScope enum_expression366 =null;
		ParserRuleReturnScope all_or_any_expression367 =null;
		ParserRuleReturnScope datetime_expression368 =null;
		ParserRuleReturnScope comparison_operator369 =null;
		ParserRuleReturnScope datetime_expression370 =null;
		ParserRuleReturnScope all_or_any_expression371 =null;
		ParserRuleReturnScope entity_expression372 =null;
		ParserRuleReturnScope entity_expression374 =null;
		ParserRuleReturnScope all_or_any_expression375 =null;
		ParserRuleReturnScope entity_type_expression376 =null;
		ParserRuleReturnScope entity_type_expression378 =null;
		ParserRuleReturnScope arithmetic_expression379 =null;
		ParserRuleReturnScope comparison_operator380 =null;
		ParserRuleReturnScope arithmetic_expression381 =null;
		ParserRuleReturnScope all_or_any_expression382 =null;

		Object string_literal357_tree=null;
		Object set361_tree=null;
		Object set365_tree=null;
		Object set373_tree=null;
		Object set377_tree=null;

		try {
			// JPA2.g:339:5: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
			int alt105=7;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA105_1 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred167_JPA2()) ) {
					alt105=3;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (synpred172_JPA2()) ) {
					alt105=5;
				}
				else if ( (synpred174_JPA2()) ) {
					alt105=6;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 87:
			case 131:
			case 135:
			case 138:
				{
				alt105=1;
				}
				break;
			case 72:
				{
				int LA105_3 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred167_JPA2()) ) {
					alt105=3;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (synpred172_JPA2()) ) {
					alt105=5;
				}
				else if ( (synpred174_JPA2()) ) {
					alt105=6;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA105_4 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred167_JPA2()) ) {
					alt105=3;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (synpred172_JPA2()) ) {
					alt105=5;
				}
				else if ( (synpred174_JPA2()) ) {
					alt105=6;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case 58:
				{
				int LA105_5 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred167_JPA2()) ) {
					alt105=3;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (synpred172_JPA2()) ) {
					alt105=5;
				}
				else if ( (synpred174_JPA2()) ) {
					alt105=6;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case COUNT:
				{
				int LA105_11 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA105_12 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case 102:
				{
				int LA105_13 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case 84:
				{
				int LA105_14 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred167_JPA2()) ) {
					alt105=3;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case 86:
				{
				int LA105_15 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred167_JPA2()) ) {
					alt105=3;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case 118:
				{
				int LA105_16 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred167_JPA2()) ) {
					alt105=3;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case 85:
				{
				int LA105_17 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case 100:
				{
				int LA105_18 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case 77:
				{
				int LA105_19 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA105_20 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred167_JPA2()) ) {
					alt105=3;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case 145:
			case 146:
				{
				alt105=2;
				}
				break;
			case GROUP:
				{
				int LA105_22 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred164_JPA2()) ) {
					alt105=2;
				}
				else if ( (synpred167_JPA2()) ) {
					alt105=3;
				}
				else if ( (synpred169_JPA2()) ) {
					alt105=4;
				}
				else if ( (synpred172_JPA2()) ) {
					alt105=5;
				}
				else if ( (true) ) {
					alt105=7;
				}

				}
				break;
			case 88:
			case 89:
			case 90:
				{
				alt105=4;
				}
				break;
			case 136:
				{
				alt105=6;
				}
				break;
			case INT_NUMERAL:
			case 60:
			case 62:
			case 65:
			case 79:
			case 104:
			case 108:
			case 110:
			case 113:
			case 128:
			case 130:
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
					// JPA2.g:339:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_comparison_expression3152);
					string_expression355=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression355.getTree());

					// JPA2.g:339:25: ( comparison_operator | 'REGEXP' )
					int alt98=2;
					int LA98_0 = input.LA(1);
					if ( ((LA98_0 >= 66 && LA98_0 <= 71)) ) {
						alt98=1;
					}
					else if ( (LA98_0==125) ) {
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
							// JPA2.g:339:26: comparison_operator
							{
							pushFollow(FOLLOW_comparison_operator_in_comparison_expression3155);
							comparison_operator356=comparison_operator();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator356.getTree());

							}
							break;
						case 2 :
							// JPA2.g:339:48: 'REGEXP'
							{
							string_literal357=(Token)match(input,125,FOLLOW_125_in_comparison_expression3159); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal357_tree = (Object)adaptor.create(string_literal357);
							adaptor.addChild(root_0, string_literal357_tree);
							}

							}
							break;

					}

					// JPA2.g:339:58: ( string_expression | all_or_any_expression )
					int alt99=2;
					int LA99_0 = input.LA(1);
					if ( (LA99_0==AVG||LA99_0==COUNT||LA99_0==GROUP||(LA99_0 >= LOWER && LA99_0 <= NAMED_PARAMETER)||(LA99_0 >= STRING_LITERAL && LA99_0 <= SUM)||LA99_0==WORD||LA99_0==58||LA99_0==72||LA99_0==77||(LA99_0 >= 84 && LA99_0 <= 87)||LA99_0==100||LA99_0==102||LA99_0==118||LA99_0==131||LA99_0==135||LA99_0==138) ) {
						alt99=1;
					}
					else if ( ((LA99_0 >= 80 && LA99_0 <= 81)||LA99_0==129) ) {
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
							// JPA2.g:339:59: string_expression
							{
							pushFollow(FOLLOW_string_expression_in_comparison_expression3163);
							string_expression358=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression358.getTree());

							}
							break;
						case 2 :
							// JPA2.g:339:79: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3167);
							all_or_any_expression359=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression359.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:340:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_comparison_expression3176);
					boolean_expression360=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression360.getTree());

					set361=input.LT(1);
					if ( (input.LA(1) >= 68 && input.LA(1) <= 69) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set361));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:340:39: ( boolean_expression | all_or_any_expression )
					int alt100=2;
					int LA100_0 = input.LA(1);
					if ( (LA100_0==GROUP||LA100_0==LPAREN||LA100_0==NAMED_PARAMETER||LA100_0==WORD||LA100_0==58||LA100_0==72||LA100_0==77||(LA100_0 >= 84 && LA100_0 <= 86)||LA100_0==100||LA100_0==102||LA100_0==118||(LA100_0 >= 145 && LA100_0 <= 146)) ) {
						alt100=1;
					}
					else if ( ((LA100_0 >= 80 && LA100_0 <= 81)||LA100_0==129) ) {
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
							// JPA2.g:340:40: boolean_expression
							{
							pushFollow(FOLLOW_boolean_expression_in_comparison_expression3187);
							boolean_expression362=boolean_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression362.getTree());

							}
							break;
						case 2 :
							// JPA2.g:340:61: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3191);
							all_or_any_expression363=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression363.getTree());

							}
							break;

					}

					}
					break;
				case 3 :
					// JPA2.g:341:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_comparison_expression3200);
					enum_expression364=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression364.getTree());

					set365=input.LT(1);
					if ( (input.LA(1) >= 68 && input.LA(1) <= 69) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set365));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:341:34: ( enum_expression | all_or_any_expression )
					int alt101=2;
					int LA101_0 = input.LA(1);
					if ( (LA101_0==GROUP||LA101_0==LPAREN||LA101_0==NAMED_PARAMETER||LA101_0==WORD||LA101_0==58||LA101_0==72||LA101_0==84||LA101_0==86||LA101_0==118) ) {
						alt101=1;
					}
					else if ( ((LA101_0 >= 80 && LA101_0 <= 81)||LA101_0==129) ) {
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
							// JPA2.g:341:35: enum_expression
							{
							pushFollow(FOLLOW_enum_expression_in_comparison_expression3209);
							enum_expression366=enum_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression366.getTree());

							}
							break;
						case 2 :
							// JPA2.g:341:53: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3213);
							all_or_any_expression367=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression367.getTree());

							}
							break;

					}

					}
					break;
				case 4 :
					// JPA2.g:342:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_comparison_expression3222);
					datetime_expression368=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression368.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3224);
					comparison_operator369=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator369.getTree());

					// JPA2.g:342:47: ( datetime_expression | all_or_any_expression )
					int alt102=2;
					int LA102_0 = input.LA(1);
					if ( (LA102_0==AVG||LA102_0==COUNT||LA102_0==GROUP||(LA102_0 >= LPAREN && LA102_0 <= NAMED_PARAMETER)||LA102_0==SUM||LA102_0==WORD||LA102_0==58||LA102_0==72||LA102_0==77||(LA102_0 >= 84 && LA102_0 <= 86)||(LA102_0 >= 88 && LA102_0 <= 90)||LA102_0==100||LA102_0==102||LA102_0==118) ) {
						alt102=1;
					}
					else if ( ((LA102_0 >= 80 && LA102_0 <= 81)||LA102_0==129) ) {
						alt102=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 102, 0, input);
						throw nvae;
					}

					switch (alt102) {
						case 1 :
							// JPA2.g:342:48: datetime_expression
							{
							pushFollow(FOLLOW_datetime_expression_in_comparison_expression3227);
							datetime_expression370=datetime_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression370.getTree());

							}
							break;
						case 2 :
							// JPA2.g:342:70: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3231);
							all_or_any_expression371=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression371.getTree());

							}
							break;

					}

					}
					break;
				case 5 :
					// JPA2.g:343:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_expression_in_comparison_expression3240);
					entity_expression372=entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression372.getTree());

					set373=input.LT(1);
					if ( (input.LA(1) >= 68 && input.LA(1) <= 69) ) {
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
					// JPA2.g:343:38: ( entity_expression | all_or_any_expression )
					int alt103=2;
					int LA103_0 = input.LA(1);
					if ( (LA103_0==GROUP||LA103_0==NAMED_PARAMETER||LA103_0==WORD||LA103_0==58||LA103_0==72) ) {
						alt103=1;
					}
					else if ( ((LA103_0 >= 80 && LA103_0 <= 81)||LA103_0==129) ) {
						alt103=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 103, 0, input);
						throw nvae;
					}

					switch (alt103) {
						case 1 :
							// JPA2.g:343:39: entity_expression
							{
							pushFollow(FOLLOW_entity_expression_in_comparison_expression3251);
							entity_expression374=entity_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression374.getTree());

							}
							break;
						case 2 :
							// JPA2.g:343:59: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3255);
							all_or_any_expression375=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression375.getTree());

							}
							break;

					}

					}
					break;
				case 6 :
					// JPA2.g:344:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3264);
					entity_type_expression376=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression376.getTree());

					set377=input.LT(1);
					if ( (input.LA(1) >= 68 && input.LA(1) <= 69) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set377));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3274);
					entity_type_expression378=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression378.getTree());

					}
					break;
				case 7 :
					// JPA2.g:345:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3282);
					arithmetic_expression379=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression379.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3284);
					comparison_operator380=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator380.getTree());

					// JPA2.g:345:49: ( arithmetic_expression | all_or_any_expression )
					int alt104=2;
					int LA104_0 = input.LA(1);
					if ( (LA104_0==AVG||LA104_0==COUNT||LA104_0==GROUP||LA104_0==INT_NUMERAL||(LA104_0 >= LPAREN && LA104_0 <= NAMED_PARAMETER)||LA104_0==SUM||LA104_0==WORD||LA104_0==58||LA104_0==60||LA104_0==62||LA104_0==65||LA104_0==72||LA104_0==77||LA104_0==79||(LA104_0 >= 84 && LA104_0 <= 86)||LA104_0==100||LA104_0==102||LA104_0==104||LA104_0==108||LA104_0==110||LA104_0==113||LA104_0==118||LA104_0==128||LA104_0==130) ) {
						alt104=1;
					}
					else if ( ((LA104_0 >= 80 && LA104_0 <= 81)||LA104_0==129) ) {
						alt104=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 104, 0, input);
						throw nvae;
					}

					switch (alt104) {
						case 1 :
							// JPA2.g:345:50: arithmetic_expression
							{
							pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3287);
							arithmetic_expression381=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression381.getTree());

							}
							break;
						case 2 :
							// JPA2.g:345:74: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3291);
							all_or_any_expression382=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression382.getTree());

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
	// JPA2.g:347:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
	public final JPA2Parser.comparison_operator_return comparison_operator() throws RecognitionException {
		JPA2Parser.comparison_operator_return retval = new JPA2Parser.comparison_operator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set383=null;

		Object set383_tree=null;

		try {
			// JPA2.g:348:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set383=input.LT(1);
			if ( (input.LA(1) >= 66 && input.LA(1) <= 71) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set383));
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
	// JPA2.g:354:1: arithmetic_expression : ( arithmetic_term ( ( '+' | '-' ) arithmetic_term )+ | arithmetic_term );
	public final JPA2Parser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
		JPA2Parser.arithmetic_expression_return retval = new JPA2Parser.arithmetic_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set385=null;
		ParserRuleReturnScope arithmetic_term384 =null;
		ParserRuleReturnScope arithmetic_term386 =null;
		ParserRuleReturnScope arithmetic_term387 =null;

		Object set385_tree=null;

		try {
			// JPA2.g:355:5: ( arithmetic_term ( ( '+' | '-' ) arithmetic_term )+ | arithmetic_term )
			int alt107=2;
			switch ( input.LA(1) ) {
			case 60:
			case 62:
				{
				int LA107_1 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case GROUP:
			case WORD:
				{
				int LA107_2 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA107_3 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 65:
				{
				int LA107_4 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case LPAREN:
				{
				int LA107_5 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 72:
				{
				int LA107_6 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA107_7 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 58:
				{
				int LA107_8 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 108:
				{
				int LA107_9 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 110:
				{
				int LA107_10 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 79:
				{
				int LA107_11 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 130:
				{
				int LA107_12 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 113:
				{
				int LA107_13 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 128:
				{
				int LA107_14 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 104:
				{
				int LA107_15 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case COUNT:
				{
				int LA107_16 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA107_17 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 102:
				{
				int LA107_18 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 84:
				{
				int LA107_19 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 86:
				{
				int LA107_20 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 118:
				{
				int LA107_21 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 85:
				{
				int LA107_22 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 100:
				{
				int LA107_23 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

				}
				break;
			case 77:
				{
				int LA107_24 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt107=1;
				}
				else if ( (true) ) {
					alt107=2;
				}

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
					// JPA2.g:355:7: arithmetic_term ( ( '+' | '-' ) arithmetic_term )+
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3355);
					arithmetic_term384=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term384.getTree());

					// JPA2.g:355:23: ( ( '+' | '-' ) arithmetic_term )+
					int cnt106=0;
					loop106:
					while (true) {
						int alt106=2;
						int LA106_0 = input.LA(1);
						if ( (LA106_0==60||LA106_0==62) ) {
							alt106=1;
						}

						switch (alt106) {
						case 1 :
							// JPA2.g:355:24: ( '+' | '-' ) arithmetic_term
							{
							set385=input.LT(1);
							if ( input.LA(1)==60||input.LA(1)==62 ) {
								input.consume();
								if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set385));
								state.errorRecovery=false;
								state.failed=false;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3366);
							arithmetic_term386=arithmetic_term();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term386.getTree());

							}
							break;

						default :
							if ( cnt106 >= 1 ) break loop106;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(106, input);
							throw eee;
						}
						cnt106++;
					}

					}
					break;
				case 2 :
					// JPA2.g:356:7: arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3376);
					arithmetic_term387=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term387.getTree());

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
	// JPA2.g:357:1: arithmetic_term : ( arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+ | arithmetic_factor );
	public final JPA2Parser.arithmetic_term_return arithmetic_term() throws RecognitionException {
		JPA2Parser.arithmetic_term_return retval = new JPA2Parser.arithmetic_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set389=null;
		ParserRuleReturnScope arithmetic_factor388 =null;
		ParserRuleReturnScope arithmetic_factor390 =null;
		ParserRuleReturnScope arithmetic_factor391 =null;

		Object set389_tree=null;

		try {
			// JPA2.g:358:5: ( arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+ | arithmetic_factor )
			int alt109=2;
			switch ( input.LA(1) ) {
			case 60:
			case 62:
				{
				int LA109_1 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case GROUP:
			case WORD:
				{
				int LA109_2 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA109_3 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 65:
				{
				int LA109_4 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case LPAREN:
				{
				int LA109_5 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 72:
				{
				int LA109_6 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA109_7 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 58:
				{
				int LA109_8 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 108:
				{
				int LA109_9 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 110:
				{
				int LA109_10 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 79:
				{
				int LA109_11 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 130:
				{
				int LA109_12 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 113:
				{
				int LA109_13 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 128:
				{
				int LA109_14 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 104:
				{
				int LA109_15 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case COUNT:
				{
				int LA109_16 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA109_17 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 102:
				{
				int LA109_18 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 84:
				{
				int LA109_19 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 86:
				{
				int LA109_20 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 118:
				{
				int LA109_21 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 85:
				{
				int LA109_22 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 100:
				{
				int LA109_23 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

				}
				break;
			case 77:
				{
				int LA109_24 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt109=1;
				}
				else if ( (true) ) {
					alt109=2;
				}

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
					// JPA2.g:358:7: arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3387);
					arithmetic_factor388=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor388.getTree());

					// JPA2.g:358:25: ( ( '*' | '/' ) arithmetic_factor )+
					int cnt108=0;
					loop108:
					while (true) {
						int alt108=2;
						int LA108_0 = input.LA(1);
						if ( (LA108_0==59||LA108_0==64) ) {
							alt108=1;
						}

						switch (alt108) {
						case 1 :
							// JPA2.g:358:26: ( '*' | '/' ) arithmetic_factor
							{
							set389=input.LT(1);
							if ( input.LA(1)==59||input.LA(1)==64 ) {
								input.consume();
								if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set389));
								state.errorRecovery=false;
								state.failed=false;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3399);
							arithmetic_factor390=arithmetic_factor();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor390.getTree());

							}
							break;

						default :
							if ( cnt108 >= 1 ) break loop108;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(108, input);
							throw eee;
						}
						cnt108++;
					}

					}
					break;
				case 2 :
					// JPA2.g:359:7: arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3409);
					arithmetic_factor391=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor391.getTree());

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
	// JPA2.g:360:1: arithmetic_factor : ( ( '+' | '-' ) )? arithmetic_primary ;
	public final JPA2Parser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
		JPA2Parser.arithmetic_factor_return retval = new JPA2Parser.arithmetic_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set392=null;
		ParserRuleReturnScope arithmetic_primary393 =null;

		Object set392_tree=null;

		try {
			// JPA2.g:361:5: ( ( ( '+' | '-' ) )? arithmetic_primary )
			// JPA2.g:361:7: ( ( '+' | '-' ) )? arithmetic_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:361:7: ( ( '+' | '-' ) )?
			int alt110=2;
			int LA110_0 = input.LA(1);
			if ( (LA110_0==60||LA110_0==62) ) {
				alt110=1;
			}
			switch (alt110) {
				case 1 :
					// JPA2.g:
					{
					set392=input.LT(1);
					if ( input.LA(1)==60||input.LA(1)==62 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set392));
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

			pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor3432);
			arithmetic_primary393=arithmetic_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary393.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:362:1: arithmetic_primary : ( path_expression | decimal_literal | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
		JPA2Parser.arithmetic_primary_return retval = new JPA2Parser.arithmetic_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal397=null;
		Token char_literal399=null;
		ParserRuleReturnScope path_expression394 =null;
		ParserRuleReturnScope decimal_literal395 =null;
		ParserRuleReturnScope numeric_literal396 =null;
		ParserRuleReturnScope arithmetic_expression398 =null;
		ParserRuleReturnScope input_parameter400 =null;
		ParserRuleReturnScope functions_returning_numerics401 =null;
		ParserRuleReturnScope aggregate_expression402 =null;
		ParserRuleReturnScope case_expression403 =null;
		ParserRuleReturnScope function_invocation404 =null;
		ParserRuleReturnScope extension_functions405 =null;
		ParserRuleReturnScope subquery406 =null;

		Object char_literal397_tree=null;
		Object char_literal399_tree=null;

		try {
			// JPA2.g:363:5: ( path_expression | decimal_literal | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
			int alt111=11;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				alt111=1;
				}
				break;
			case INT_NUMERAL:
				{
				int LA111_2 = input.LA(2);
				if ( (synpred190_JPA2()) ) {
					alt111=2;
				}
				else if ( (synpred191_JPA2()) ) {
					alt111=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 111, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 65:
				{
				alt111=3;
				}
				break;
			case LPAREN:
				{
				int LA111_4 = input.LA(2);
				if ( (synpred192_JPA2()) ) {
					alt111=4;
				}
				else if ( (true) ) {
					alt111=11;
				}

				}
				break;
			case NAMED_PARAMETER:
			case 58:
			case 72:
				{
				alt111=5;
				}
				break;
			case 79:
			case 104:
			case 108:
			case 110:
			case 113:
			case 128:
			case 130:
				{
				alt111=6;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt111=7;
				}
				break;
			case 102:
				{
				int LA111_17 = input.LA(2);
				if ( (synpred195_JPA2()) ) {
					alt111=7;
				}
				else if ( (synpred197_JPA2()) ) {
					alt111=9;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 111, 17, input);
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
				alt111=8;
				}
				break;
			case 77:
			case 85:
			case 100:
				{
				alt111=10;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 111, 0, input);
				throw nvae;
			}
			switch (alt111) {
				case 1 :
					// JPA2.g:363:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_arithmetic_primary3443);
					path_expression394=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression394.getTree());

					}
					break;
				case 2 :
					// JPA2.g:364:7: decimal_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_decimal_literal_in_arithmetic_primary3451);
					decimal_literal395=decimal_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, decimal_literal395.getTree());

					}
					break;
				case 3 :
					// JPA2.g:365:7: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary3459);
					numeric_literal396=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal396.getTree());

					}
					break;
				case 4 :
					// JPA2.g:366:7: '(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal397=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary3467); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal397_tree = (Object)adaptor.create(char_literal397);
					adaptor.addChild(root_0, char_literal397_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_arithmetic_primary3468);
					arithmetic_expression398=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression398.getTree());

					char_literal399=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary3469); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal399_tree = (Object)adaptor.create(char_literal399);
					adaptor.addChild(root_0, char_literal399_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:367:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_arithmetic_primary3477);
					input_parameter400=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter400.getTree());

					}
					break;
				case 6 :
					// JPA2.g:368:7: functions_returning_numerics
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary3485);
					functions_returning_numerics401=functions_returning_numerics();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics401.getTree());

					}
					break;
				case 7 :
					// JPA2.g:369:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary3493);
					aggregate_expression402=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression402.getTree());

					}
					break;
				case 8 :
					// JPA2.g:370:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_arithmetic_primary3501);
					case_expression403=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression403.getTree());

					}
					break;
				case 9 :
					// JPA2.g:371:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_arithmetic_primary3509);
					function_invocation404=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation404.getTree());

					}
					break;
				case 10 :
					// JPA2.g:372:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_arithmetic_primary3517);
					extension_functions405=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions405.getTree());

					}
					break;
				case 11 :
					// JPA2.g:373:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_arithmetic_primary3525);
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
	// $ANTLR end "arithmetic_primary"


	public static class string_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "string_expression"
	// JPA2.g:374:1: string_expression : ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.string_expression_return string_expression() throws RecognitionException {
		JPA2Parser.string_expression_return retval = new JPA2Parser.string_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression407 =null;
		ParserRuleReturnScope string_literal408 =null;
		ParserRuleReturnScope input_parameter409 =null;
		ParserRuleReturnScope functions_returning_strings410 =null;
		ParserRuleReturnScope aggregate_expression411 =null;
		ParserRuleReturnScope case_expression412 =null;
		ParserRuleReturnScope function_invocation413 =null;
		ParserRuleReturnScope extension_functions414 =null;
		ParserRuleReturnScope subquery415 =null;


		try {
			// JPA2.g:375:5: ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
			int alt112=9;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				alt112=1;
				}
				break;
			case STRING_LITERAL:
				{
				alt112=2;
				}
				break;
			case NAMED_PARAMETER:
			case 58:
			case 72:
				{
				alt112=3;
				}
				break;
			case LOWER:
			case 87:
			case 131:
			case 135:
			case 138:
				{
				alt112=4;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt112=5;
				}
				break;
			case 102:
				{
				int LA112_13 = input.LA(2);
				if ( (synpred203_JPA2()) ) {
					alt112=5;
				}
				else if ( (synpred205_JPA2()) ) {
					alt112=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 112, 13, input);
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
				alt112=6;
				}
				break;
			case 77:
			case 85:
			case 100:
				{
				alt112=8;
				}
				break;
			case LPAREN:
				{
				alt112=9;
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
					// JPA2.g:375:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_string_expression3536);
					path_expression407=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression407.getTree());

					}
					break;
				case 2 :
					// JPA2.g:376:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_string_expression3544);
					string_literal408=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal408.getTree());

					}
					break;
				case 3 :
					// JPA2.g:377:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_string_expression3552);
					input_parameter409=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter409.getTree());

					}
					break;
				case 4 :
					// JPA2.g:378:7: functions_returning_strings
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_strings_in_string_expression3560);
					functions_returning_strings410=functions_returning_strings();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings410.getTree());

					}
					break;
				case 5 :
					// JPA2.g:379:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_string_expression3568);
					aggregate_expression411=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression411.getTree());

					}
					break;
				case 6 :
					// JPA2.g:380:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_string_expression3576);
					case_expression412=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression412.getTree());

					}
					break;
				case 7 :
					// JPA2.g:381:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_string_expression3584);
					function_invocation413=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation413.getTree());

					}
					break;
				case 8 :
					// JPA2.g:382:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_string_expression3592);
					extension_functions414=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions414.getTree());

					}
					break;
				case 9 :
					// JPA2.g:383:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_string_expression3600);
					subquery415=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery415.getTree());

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
	// JPA2.g:384:1: datetime_expression : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery );
	public final JPA2Parser.datetime_expression_return datetime_expression() throws RecognitionException {
		JPA2Parser.datetime_expression_return retval = new JPA2Parser.datetime_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression416 =null;
		ParserRuleReturnScope input_parameter417 =null;
		ParserRuleReturnScope functions_returning_datetime418 =null;
		ParserRuleReturnScope aggregate_expression419 =null;
		ParserRuleReturnScope case_expression420 =null;
		ParserRuleReturnScope function_invocation421 =null;
		ParserRuleReturnScope extension_functions422 =null;
		ParserRuleReturnScope date_time_timestamp_literal423 =null;
		ParserRuleReturnScope subquery424 =null;


		try {
			// JPA2.g:385:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery )
			int alt113=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA113_1 = input.LA(2);
				if ( (synpred207_JPA2()) ) {
					alt113=1;
				}
				else if ( (synpred214_JPA2()) ) {
					alt113=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 113, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
			case 58:
			case 72:
				{
				alt113=2;
				}
				break;
			case 88:
			case 89:
			case 90:
				{
				alt113=3;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt113=4;
				}
				break;
			case 102:
				{
				int LA113_8 = input.LA(2);
				if ( (synpred210_JPA2()) ) {
					alt113=4;
				}
				else if ( (synpred212_JPA2()) ) {
					alt113=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 113, 8, input);
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
				alt113=5;
				}
				break;
			case 77:
			case 85:
			case 100:
				{
				alt113=7;
				}
				break;
			case GROUP:
				{
				alt113=1;
				}
				break;
			case LPAREN:
				{
				alt113=9;
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
					// JPA2.g:385:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_datetime_expression3611);
					path_expression416=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression416.getTree());

					}
					break;
				case 2 :
					// JPA2.g:386:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_datetime_expression3619);
					input_parameter417=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter417.getTree());

					}
					break;
				case 3 :
					// JPA2.g:387:7: functions_returning_datetime
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_datetime_in_datetime_expression3627);
					functions_returning_datetime418=functions_returning_datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime418.getTree());

					}
					break;
				case 4 :
					// JPA2.g:388:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_datetime_expression3635);
					aggregate_expression419=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression419.getTree());

					}
					break;
				case 5 :
					// JPA2.g:389:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_datetime_expression3643);
					case_expression420=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression420.getTree());

					}
					break;
				case 6 :
					// JPA2.g:390:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_datetime_expression3651);
					function_invocation421=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation421.getTree());

					}
					break;
				case 7 :
					// JPA2.g:391:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_datetime_expression3659);
					extension_functions422=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions422.getTree());

					}
					break;
				case 8 :
					// JPA2.g:392:7: date_time_timestamp_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_time_timestamp_literal_in_datetime_expression3667);
					date_time_timestamp_literal423=date_time_timestamp_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_time_timestamp_literal423.getTree());

					}
					break;
				case 9 :
					// JPA2.g:393:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_datetime_expression3675);
					subquery424=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery424.getTree());

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
	// JPA2.g:394:1: boolean_expression : ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.boolean_expression_return boolean_expression() throws RecognitionException {
		JPA2Parser.boolean_expression_return retval = new JPA2Parser.boolean_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression425 =null;
		ParserRuleReturnScope boolean_literal426 =null;
		ParserRuleReturnScope input_parameter427 =null;
		ParserRuleReturnScope case_expression428 =null;
		ParserRuleReturnScope function_invocation429 =null;
		ParserRuleReturnScope extension_functions430 =null;
		ParserRuleReturnScope subquery431 =null;


		try {
			// JPA2.g:395:5: ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery )
			int alt114=7;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				alt114=1;
				}
				break;
			case 145:
			case 146:
				{
				alt114=2;
				}
				break;
			case NAMED_PARAMETER:
			case 58:
			case 72:
				{
				alt114=3;
				}
				break;
			case 84:
			case 86:
			case 118:
				{
				alt114=4;
				}
				break;
			case 102:
				{
				alt114=5;
				}
				break;
			case 77:
			case 85:
			case 100:
				{
				alt114=6;
				}
				break;
			case LPAREN:
				{
				alt114=7;
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
					// JPA2.g:395:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_boolean_expression3686);
					path_expression425=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression425.getTree());

					}
					break;
				case 2 :
					// JPA2.g:396:7: boolean_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_literal_in_boolean_expression3694);
					boolean_literal426=boolean_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal426.getTree());

					}
					break;
				case 3 :
					// JPA2.g:397:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_boolean_expression3702);
					input_parameter427=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter427.getTree());

					}
					break;
				case 4 :
					// JPA2.g:398:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_boolean_expression3710);
					case_expression428=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression428.getTree());

					}
					break;
				case 5 :
					// JPA2.g:399:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_boolean_expression3718);
					function_invocation429=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation429.getTree());

					}
					break;
				case 6 :
					// JPA2.g:400:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_boolean_expression3726);
					extension_functions430=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions430.getTree());

					}
					break;
				case 7 :
					// JPA2.g:401:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_boolean_expression3734);
					subquery431=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery431.getTree());

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
	// JPA2.g:402:1: enum_expression : ( path_expression | enum_literal | input_parameter | case_expression | subquery );
	public final JPA2Parser.enum_expression_return enum_expression() throws RecognitionException {
		JPA2Parser.enum_expression_return retval = new JPA2Parser.enum_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression432 =null;
		ParserRuleReturnScope enum_literal433 =null;
		ParserRuleReturnScope input_parameter434 =null;
		ParserRuleReturnScope case_expression435 =null;
		ParserRuleReturnScope subquery436 =null;


		try {
			// JPA2.g:403:5: ( path_expression | enum_literal | input_parameter | case_expression | subquery )
			int alt115=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA115_1 = input.LA(2);
				if ( (LA115_1==63) ) {
					alt115=1;
				}
				else if ( (LA115_1==EOF||(LA115_1 >= AND && LA115_1 <= ASC)||LA115_1==DESC||(LA115_1 >= GROUP && LA115_1 <= HAVING)||LA115_1==INNER||(LA115_1 >= JOIN && LA115_1 <= LEFT)||(LA115_1 >= OR && LA115_1 <= ORDER)||LA115_1==RPAREN||LA115_1==SET||LA115_1==WORD||LA115_1==61||(LA115_1 >= 68 && LA115_1 <= 69)||LA115_1==93||LA115_1==95||LA115_1==101||(LA115_1 >= 119 && LA115_1 <= 120)||LA115_1==132||(LA115_1 >= 142 && LA115_1 <= 143)) ) {
					alt115=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 115, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case GROUP:
				{
				alt115=1;
				}
				break;
			case NAMED_PARAMETER:
			case 58:
			case 72:
				{
				alt115=3;
				}
				break;
			case 84:
			case 86:
			case 118:
				{
				alt115=4;
				}
				break;
			case LPAREN:
				{
				alt115=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 115, 0, input);
				throw nvae;
			}
			switch (alt115) {
				case 1 :
					// JPA2.g:403:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_enum_expression3745);
					path_expression432=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression432.getTree());

					}
					break;
				case 2 :
					// JPA2.g:404:7: enum_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_literal_in_enum_expression3753);
					enum_literal433=enum_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal433.getTree());

					}
					break;
				case 3 :
					// JPA2.g:405:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_enum_expression3761);
					input_parameter434=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter434.getTree());

					}
					break;
				case 4 :
					// JPA2.g:406:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_enum_expression3769);
					case_expression435=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression435.getTree());

					}
					break;
				case 5 :
					// JPA2.g:407:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_enum_expression3777);
					subquery436=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery436.getTree());

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
	// JPA2.g:408:1: entity_expression : ( path_expression | simple_entity_expression );
	public final JPA2Parser.entity_expression_return entity_expression() throws RecognitionException {
		JPA2Parser.entity_expression_return retval = new JPA2Parser.entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression437 =null;
		ParserRuleReturnScope simple_entity_expression438 =null;


		try {
			// JPA2.g:409:5: ( path_expression | simple_entity_expression )
			int alt116=2;
			int LA116_0 = input.LA(1);
			if ( (LA116_0==GROUP||LA116_0==WORD) ) {
				int LA116_1 = input.LA(2);
				if ( (LA116_1==63) ) {
					alt116=1;
				}
				else if ( (LA116_1==EOF||LA116_1==AND||(LA116_1 >= GROUP && LA116_1 <= HAVING)||LA116_1==INNER||(LA116_1 >= JOIN && LA116_1 <= LEFT)||(LA116_1 >= OR && LA116_1 <= ORDER)||LA116_1==RPAREN||LA116_1==SET||LA116_1==61||(LA116_1 >= 68 && LA116_1 <= 69)||LA116_1==132||LA116_1==143) ) {
					alt116=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 116, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA116_0==NAMED_PARAMETER||LA116_0==58||LA116_0==72) ) {
				alt116=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 116, 0, input);
				throw nvae;
			}

			switch (alt116) {
				case 1 :
					// JPA2.g:409:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_expression3788);
					path_expression437=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression437.getTree());

					}
					break;
				case 2 :
					// JPA2.g:410:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3796);
					simple_entity_expression438=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression438.getTree());

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
	// JPA2.g:411:1: simple_entity_expression : ( identification_variable | input_parameter );
	public final JPA2Parser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
		JPA2Parser.simple_entity_expression_return retval = new JPA2Parser.simple_entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable439 =null;
		ParserRuleReturnScope input_parameter440 =null;


		try {
			// JPA2.g:412:5: ( identification_variable | input_parameter )
			int alt117=2;
			int LA117_0 = input.LA(1);
			if ( (LA117_0==GROUP||LA117_0==WORD) ) {
				alt117=1;
			}
			else if ( (LA117_0==NAMED_PARAMETER||LA117_0==58||LA117_0==72) ) {
				alt117=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 117, 0, input);
				throw nvae;
			}

			switch (alt117) {
				case 1 :
					// JPA2.g:412:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3807);
					identification_variable439=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable439.getTree());

					}
					break;
				case 2 :
					// JPA2.g:413:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3815);
					input_parameter440=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter440.getTree());

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
	// JPA2.g:414:1: entity_type_expression : ( type_discriminator | entity_type_literal | input_parameter );
	public final JPA2Parser.entity_type_expression_return entity_type_expression() throws RecognitionException {
		JPA2Parser.entity_type_expression_return retval = new JPA2Parser.entity_type_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope type_discriminator441 =null;
		ParserRuleReturnScope entity_type_literal442 =null;
		ParserRuleReturnScope input_parameter443 =null;


		try {
			// JPA2.g:415:5: ( type_discriminator | entity_type_literal | input_parameter )
			int alt118=3;
			switch ( input.LA(1) ) {
			case 136:
				{
				alt118=1;
				}
				break;
			case WORD:
				{
				alt118=2;
				}
				break;
			case NAMED_PARAMETER:
			case 58:
			case 72:
				{
				alt118=3;
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
					// JPA2.g:415:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_entity_type_expression3826);
					type_discriminator441=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator441.getTree());

					}
					break;
				case 2 :
					// JPA2.g:416:7: entity_type_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_literal_in_entity_type_expression3834);
					entity_type_literal442=entity_type_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_literal442.getTree());

					}
					break;
				case 3 :
					// JPA2.g:417:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_entity_type_expression3842);
					input_parameter443=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter443.getTree());

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
	// JPA2.g:418:1: type_discriminator : 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' ;
	public final JPA2Parser.type_discriminator_return type_discriminator() throws RecognitionException {
		JPA2Parser.type_discriminator_return retval = new JPA2Parser.type_discriminator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal444=null;
		Token char_literal448=null;
		ParserRuleReturnScope general_identification_variable445 =null;
		ParserRuleReturnScope path_expression446 =null;
		ParserRuleReturnScope input_parameter447 =null;

		Object string_literal444_tree=null;
		Object char_literal448_tree=null;

		try {
			// JPA2.g:419:5: ( 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' )
			// JPA2.g:419:7: 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal444=(Token)match(input,136,FOLLOW_136_in_type_discriminator3853); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal444_tree = (Object)adaptor.create(string_literal444);
			adaptor.addChild(root_0, string_literal444_tree);
			}

			// JPA2.g:419:15: ( general_identification_variable | path_expression | input_parameter )
			int alt119=3;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				int LA119_1 = input.LA(2);
				if ( (LA119_1==RPAREN) ) {
					alt119=1;
				}
				else if ( (LA119_1==63) ) {
					alt119=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 119, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 106:
			case 140:
				{
				alt119=1;
				}
				break;
			case NAMED_PARAMETER:
			case 58:
			case 72:
				{
				alt119=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 119, 0, input);
				throw nvae;
			}
			switch (alt119) {
				case 1 :
					// JPA2.g:419:16: general_identification_variable
					{
					pushFollow(FOLLOW_general_identification_variable_in_type_discriminator3856);
					general_identification_variable445=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable445.getTree());

					}
					break;
				case 2 :
					// JPA2.g:419:50: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_type_discriminator3860);
					path_expression446=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression446.getTree());

					}
					break;
				case 3 :
					// JPA2.g:419:68: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_type_discriminator3864);
					input_parameter447=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter447.getTree());

					}
					break;

			}

			char_literal448=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_type_discriminator3867); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal448_tree = (Object)adaptor.create(char_literal448);
			adaptor.addChild(root_0, char_literal448_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:420:1: functions_returning_numerics : ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' );
	public final JPA2Parser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
		JPA2Parser.functions_returning_numerics_return retval = new JPA2Parser.functions_returning_numerics_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal449=null;
		Token char_literal451=null;
		Token string_literal452=null;
		Token char_literal454=null;
		Token char_literal456=null;
		Token char_literal458=null;
		Token string_literal459=null;
		Token char_literal461=null;
		Token string_literal462=null;
		Token char_literal464=null;
		Token string_literal465=null;
		Token char_literal467=null;
		Token char_literal469=null;
		Token string_literal470=null;
		Token char_literal472=null;
		Token string_literal473=null;
		Token char_literal475=null;
		ParserRuleReturnScope string_expression450 =null;
		ParserRuleReturnScope string_expression453 =null;
		ParserRuleReturnScope string_expression455 =null;
		ParserRuleReturnScope arithmetic_expression457 =null;
		ParserRuleReturnScope arithmetic_expression460 =null;
		ParserRuleReturnScope arithmetic_expression463 =null;
		ParserRuleReturnScope arithmetic_expression466 =null;
		ParserRuleReturnScope arithmetic_expression468 =null;
		ParserRuleReturnScope path_expression471 =null;
		ParserRuleReturnScope identification_variable474 =null;

		Object string_literal449_tree=null;
		Object char_literal451_tree=null;
		Object string_literal452_tree=null;
		Object char_literal454_tree=null;
		Object char_literal456_tree=null;
		Object char_literal458_tree=null;
		Object string_literal459_tree=null;
		Object char_literal461_tree=null;
		Object string_literal462_tree=null;
		Object char_literal464_tree=null;
		Object string_literal465_tree=null;
		Object char_literal467_tree=null;
		Object char_literal469_tree=null;
		Object string_literal470_tree=null;
		Object char_literal472_tree=null;
		Object string_literal473_tree=null;
		Object char_literal475_tree=null;

		try {
			// JPA2.g:421:5: ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' )
			int alt121=7;
			switch ( input.LA(1) ) {
			case 108:
				{
				alt121=1;
				}
				break;
			case 110:
				{
				alt121=2;
				}
				break;
			case 79:
				{
				alt121=3;
				}
				break;
			case 130:
				{
				alt121=4;
				}
				break;
			case 113:
				{
				alt121=5;
				}
				break;
			case 128:
				{
				alt121=6;
				}
				break;
			case 104:
				{
				alt121=7;
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
					// JPA2.g:421:7: 'LENGTH(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal449=(Token)match(input,108,FOLLOW_108_in_functions_returning_numerics3878); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal449_tree = (Object)adaptor.create(string_literal449);
					adaptor.addChild(root_0, string_literal449_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3879);
					string_expression450=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression450.getTree());

					char_literal451=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3880); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal451_tree = (Object)adaptor.create(char_literal451);
					adaptor.addChild(root_0, char_literal451_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:422:7: 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal452=(Token)match(input,110,FOLLOW_110_in_functions_returning_numerics3888); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal452_tree = (Object)adaptor.create(string_literal452);
					adaptor.addChild(root_0, string_literal452_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3890);
					string_expression453=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression453.getTree());

					char_literal454=(Token)match(input,61,FOLLOW_61_in_functions_returning_numerics3891); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal454_tree = (Object)adaptor.create(char_literal454);
					adaptor.addChild(root_0, char_literal454_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3893);
					string_expression455=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression455.getTree());

					// JPA2.g:422:55: ( ',' arithmetic_expression )?
					int alt120=2;
					int LA120_0 = input.LA(1);
					if ( (LA120_0==61) ) {
						alt120=1;
					}
					switch (alt120) {
						case 1 :
							// JPA2.g:422:56: ',' arithmetic_expression
							{
							char_literal456=(Token)match(input,61,FOLLOW_61_in_functions_returning_numerics3895); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal456_tree = (Object)adaptor.create(char_literal456);
							adaptor.addChild(root_0, char_literal456_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3896);
							arithmetic_expression457=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression457.getTree());

							}
							break;

					}

					char_literal458=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3899); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal458_tree = (Object)adaptor.create(char_literal458);
					adaptor.addChild(root_0, char_literal458_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:423:7: 'ABS(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal459=(Token)match(input,79,FOLLOW_79_in_functions_returning_numerics3907); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal459_tree = (Object)adaptor.create(string_literal459);
					adaptor.addChild(root_0, string_literal459_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3908);
					arithmetic_expression460=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression460.getTree());

					char_literal461=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3909); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal461_tree = (Object)adaptor.create(char_literal461);
					adaptor.addChild(root_0, char_literal461_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:424:7: 'SQRT(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal462=(Token)match(input,130,FOLLOW_130_in_functions_returning_numerics3917); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal462_tree = (Object)adaptor.create(string_literal462);
					adaptor.addChild(root_0, string_literal462_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3918);
					arithmetic_expression463=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression463.getTree());

					char_literal464=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3919); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal464_tree = (Object)adaptor.create(char_literal464);
					adaptor.addChild(root_0, char_literal464_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:425:7: 'MOD(' arithmetic_expression ',' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal465=(Token)match(input,113,FOLLOW_113_in_functions_returning_numerics3927); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal465_tree = (Object)adaptor.create(string_literal465);
					adaptor.addChild(root_0, string_literal465_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3928);
					arithmetic_expression466=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression466.getTree());

					char_literal467=(Token)match(input,61,FOLLOW_61_in_functions_returning_numerics3929); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal467_tree = (Object)adaptor.create(char_literal467);
					adaptor.addChild(root_0, char_literal467_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3931);
					arithmetic_expression468=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression468.getTree());

					char_literal469=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3932); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal469_tree = (Object)adaptor.create(char_literal469);
					adaptor.addChild(root_0, char_literal469_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:426:7: 'SIZE(' path_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal470=(Token)match(input,128,FOLLOW_128_in_functions_returning_numerics3940); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal470_tree = (Object)adaptor.create(string_literal470);
					adaptor.addChild(root_0, string_literal470_tree);
					}

					pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3941);
					path_expression471=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression471.getTree());

					char_literal472=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3942); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal472_tree = (Object)adaptor.create(char_literal472);
					adaptor.addChild(root_0, char_literal472_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:427:7: 'INDEX(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal473=(Token)match(input,104,FOLLOW_104_in_functions_returning_numerics3950); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal473_tree = (Object)adaptor.create(string_literal473);
					adaptor.addChild(root_0, string_literal473_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_functions_returning_numerics3951);
					identification_variable474=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable474.getTree());

					char_literal475=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3952); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal475_tree = (Object)adaptor.create(char_literal475);
					adaptor.addChild(root_0, char_literal475_tree);
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
	// JPA2.g:428:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
	public final JPA2Parser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
		JPA2Parser.functions_returning_datetime_return retval = new JPA2Parser.functions_returning_datetime_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set476=null;

		Object set476_tree=null;

		try {
			// JPA2.g:429:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set476=input.LT(1);
			if ( (input.LA(1) >= 88 && input.LA(1) <= 90) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set476));
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
	// JPA2.g:432:1: functions_returning_strings : ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' );
	public final JPA2Parser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
		JPA2Parser.functions_returning_strings_return retval = new JPA2Parser.functions_returning_strings_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal477=null;
		Token char_literal479=null;
		Token char_literal481=null;
		Token char_literal483=null;
		Token string_literal484=null;
		Token char_literal486=null;
		Token char_literal488=null;
		Token char_literal490=null;
		Token string_literal491=null;
		Token string_literal494=null;
		Token char_literal496=null;
		Token string_literal497=null;
		Token char_literal498=null;
		Token char_literal500=null;
		Token string_literal501=null;
		Token char_literal503=null;
		ParserRuleReturnScope string_expression478 =null;
		ParserRuleReturnScope string_expression480 =null;
		ParserRuleReturnScope string_expression482 =null;
		ParserRuleReturnScope string_expression485 =null;
		ParserRuleReturnScope arithmetic_expression487 =null;
		ParserRuleReturnScope arithmetic_expression489 =null;
		ParserRuleReturnScope trim_specification492 =null;
		ParserRuleReturnScope trim_character493 =null;
		ParserRuleReturnScope string_expression495 =null;
		ParserRuleReturnScope string_expression499 =null;
		ParserRuleReturnScope string_expression502 =null;

		Object string_literal477_tree=null;
		Object char_literal479_tree=null;
		Object char_literal481_tree=null;
		Object char_literal483_tree=null;
		Object string_literal484_tree=null;
		Object char_literal486_tree=null;
		Object char_literal488_tree=null;
		Object char_literal490_tree=null;
		Object string_literal491_tree=null;
		Object string_literal494_tree=null;
		Object char_literal496_tree=null;
		Object string_literal497_tree=null;
		Object char_literal498_tree=null;
		Object char_literal500_tree=null;
		Object string_literal501_tree=null;
		Object char_literal503_tree=null;

		try {
			// JPA2.g:433:5: ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' )
			int alt127=5;
			switch ( input.LA(1) ) {
			case 87:
				{
				alt127=1;
				}
				break;
			case 131:
				{
				alt127=2;
				}
				break;
			case 135:
				{
				alt127=3;
				}
				break;
			case LOWER:
				{
				alt127=4;
				}
				break;
			case 138:
				{
				alt127=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 127, 0, input);
				throw nvae;
			}
			switch (alt127) {
				case 1 :
					// JPA2.g:433:7: 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal477=(Token)match(input,87,FOLLOW_87_in_functions_returning_strings3990); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal477_tree = (Object)adaptor.create(string_literal477);
					adaptor.addChild(root_0, string_literal477_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3991);
					string_expression478=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression478.getTree());

					char_literal479=(Token)match(input,61,FOLLOW_61_in_functions_returning_strings3992); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal479_tree = (Object)adaptor.create(char_literal479);
					adaptor.addChild(root_0, char_literal479_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3994);
					string_expression480=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression480.getTree());

					// JPA2.g:433:55: ( ',' string_expression )*
					loop122:
					while (true) {
						int alt122=2;
						int LA122_0 = input.LA(1);
						if ( (LA122_0==61) ) {
							alt122=1;
						}

						switch (alt122) {
						case 1 :
							// JPA2.g:433:56: ',' string_expression
							{
							char_literal481=(Token)match(input,61,FOLLOW_61_in_functions_returning_strings3997); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal481_tree = (Object)adaptor.create(char_literal481);
							adaptor.addChild(root_0, char_literal481_tree);
							}

							pushFollow(FOLLOW_string_expression_in_functions_returning_strings3999);
							string_expression482=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression482.getTree());

							}
							break;

						default :
							break loop122;
						}
					}

					char_literal483=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4002); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal483_tree = (Object)adaptor.create(char_literal483);
					adaptor.addChild(root_0, char_literal483_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:434:7: 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal484=(Token)match(input,131,FOLLOW_131_in_functions_returning_strings4010); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal484_tree = (Object)adaptor.create(string_literal484);
					adaptor.addChild(root_0, string_literal484_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4012);
					string_expression485=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression485.getTree());

					char_literal486=(Token)match(input,61,FOLLOW_61_in_functions_returning_strings4013); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal486_tree = (Object)adaptor.create(char_literal486);
					adaptor.addChild(root_0, char_literal486_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings4015);
					arithmetic_expression487=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression487.getTree());

					// JPA2.g:434:63: ( ',' arithmetic_expression )?
					int alt123=2;
					int LA123_0 = input.LA(1);
					if ( (LA123_0==61) ) {
						alt123=1;
					}
					switch (alt123) {
						case 1 :
							// JPA2.g:434:64: ',' arithmetic_expression
							{
							char_literal488=(Token)match(input,61,FOLLOW_61_in_functions_returning_strings4018); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal488_tree = (Object)adaptor.create(char_literal488);
							adaptor.addChild(root_0, char_literal488_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings4020);
							arithmetic_expression489=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression489.getTree());

							}
							break;

					}

					char_literal490=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4023); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal490_tree = (Object)adaptor.create(char_literal490);
					adaptor.addChild(root_0, char_literal490_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:435:7: 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal491=(Token)match(input,135,FOLLOW_135_in_functions_returning_strings4031); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal491_tree = (Object)adaptor.create(string_literal491);
					adaptor.addChild(root_0, string_literal491_tree);
					}

					// JPA2.g:435:14: ( ( trim_specification )? ( trim_character )? 'FROM' )?
					int alt126=2;
					int LA126_0 = input.LA(1);
					if ( (LA126_0==TRIM_CHARACTER||LA126_0==83||LA126_0==101||LA126_0==107||LA126_0==133) ) {
						alt126=1;
					}
					switch (alt126) {
						case 1 :
							// JPA2.g:435:15: ( trim_specification )? ( trim_character )? 'FROM'
							{
							// JPA2.g:435:15: ( trim_specification )?
							int alt124=2;
							int LA124_0 = input.LA(1);
							if ( (LA124_0==83||LA124_0==107||LA124_0==133) ) {
								alt124=1;
							}
							switch (alt124) {
								case 1 :
									// JPA2.g:435:16: trim_specification
									{
									pushFollow(FOLLOW_trim_specification_in_functions_returning_strings4034);
									trim_specification492=trim_specification();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification492.getTree());

									}
									break;

							}

							// JPA2.g:435:37: ( trim_character )?
							int alt125=2;
							int LA125_0 = input.LA(1);
							if ( (LA125_0==TRIM_CHARACTER) ) {
								alt125=1;
							}
							switch (alt125) {
								case 1 :
									// JPA2.g:435:38: trim_character
									{
									pushFollow(FOLLOW_trim_character_in_functions_returning_strings4039);
									trim_character493=trim_character();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_character493.getTree());

									}
									break;

							}

							string_literal494=(Token)match(input,101,FOLLOW_101_in_functions_returning_strings4043); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal494_tree = (Object)adaptor.create(string_literal494);
							adaptor.addChild(root_0, string_literal494_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4047);
					string_expression495=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression495.getTree());

					char_literal496=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4049); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal496_tree = (Object)adaptor.create(char_literal496);
					adaptor.addChild(root_0, char_literal496_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:436:7: 'LOWER' '(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal497=(Token)match(input,LOWER,FOLLOW_LOWER_in_functions_returning_strings4057); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal497_tree = (Object)adaptor.create(string_literal497);
					adaptor.addChild(root_0, string_literal497_tree);
					}

					char_literal498=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings4059); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal498_tree = (Object)adaptor.create(char_literal498);
					adaptor.addChild(root_0, char_literal498_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4060);
					string_expression499=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression499.getTree());

					char_literal500=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4061); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal500_tree = (Object)adaptor.create(char_literal500);
					adaptor.addChild(root_0, char_literal500_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:437:7: 'UPPER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal501=(Token)match(input,138,FOLLOW_138_in_functions_returning_strings4069); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal501_tree = (Object)adaptor.create(string_literal501);
					adaptor.addChild(root_0, string_literal501_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4070);
					string_expression502=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression502.getTree());

					char_literal503=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4071); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal503_tree = (Object)adaptor.create(char_literal503);
					adaptor.addChild(root_0, char_literal503_tree);
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
	// JPA2.g:438:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
	public final JPA2Parser.trim_specification_return trim_specification() throws RecognitionException {
		JPA2Parser.trim_specification_return retval = new JPA2Parser.trim_specification_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set504=null;

		Object set504_tree=null;

		try {
			// JPA2.g:439:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set504=input.LT(1);
			if ( input.LA(1)==83||input.LA(1)==107||input.LA(1)==133 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set504));
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
	// JPA2.g:440:1: function_invocation : 'FUNCTION(' function_name ( ',' function_arg )* ')' ;
	public final JPA2Parser.function_invocation_return function_invocation() throws RecognitionException {
		JPA2Parser.function_invocation_return retval = new JPA2Parser.function_invocation_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal505=null;
		Token char_literal507=null;
		Token char_literal509=null;
		ParserRuleReturnScope function_name506 =null;
		ParserRuleReturnScope function_arg508 =null;

		Object string_literal505_tree=null;
		Object char_literal507_tree=null;
		Object char_literal509_tree=null;

		try {
			// JPA2.g:441:5: ( 'FUNCTION(' function_name ( ',' function_arg )* ')' )
			// JPA2.g:441:7: 'FUNCTION(' function_name ( ',' function_arg )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal505=(Token)match(input,102,FOLLOW_102_in_function_invocation4101); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal505_tree = (Object)adaptor.create(string_literal505);
			adaptor.addChild(root_0, string_literal505_tree);
			}

			pushFollow(FOLLOW_function_name_in_function_invocation4102);
			function_name506=function_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_name506.getTree());

			// JPA2.g:441:32: ( ',' function_arg )*
			loop128:
			while (true) {
				int alt128=2;
				int LA128_0 = input.LA(1);
				if ( (LA128_0==61) ) {
					alt128=1;
				}

				switch (alt128) {
				case 1 :
					// JPA2.g:441:33: ',' function_arg
					{
					char_literal507=(Token)match(input,61,FOLLOW_61_in_function_invocation4105); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal507_tree = (Object)adaptor.create(char_literal507);
					adaptor.addChild(root_0, char_literal507_tree);
					}

					pushFollow(FOLLOW_function_arg_in_function_invocation4107);
					function_arg508=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg508.getTree());

					}
					break;

				default :
					break loop128;
				}
			}

			char_literal509=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function_invocation4111); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal509_tree = (Object)adaptor.create(char_literal509);
			adaptor.addChild(root_0, char_literal509_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:442:1: function_arg : ( literal | path_expression | input_parameter | scalar_expression );
	public final JPA2Parser.function_arg_return function_arg() throws RecognitionException {
		JPA2Parser.function_arg_return retval = new JPA2Parser.function_arg_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal510 =null;
		ParserRuleReturnScope path_expression511 =null;
		ParserRuleReturnScope input_parameter512 =null;
		ParserRuleReturnScope scalar_expression513 =null;


		try {
			// JPA2.g:443:5: ( literal | path_expression | input_parameter | scalar_expression )
			int alt129=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA129_1 = input.LA(2);
				if ( (LA129_1==63) ) {
					alt129=2;
				}
				else if ( (synpred252_JPA2()) ) {
					alt129=1;
				}
				else if ( (true) ) {
					alt129=4;
				}

				}
				break;
			case GROUP:
				{
				int LA129_2 = input.LA(2);
				if ( (LA129_2==63) ) {
					int LA129_9 = input.LA(3);
					if ( (synpred253_JPA2()) ) {
						alt129=2;
					}
					else if ( (true) ) {
						alt129=4;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 129, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 72:
				{
				int LA129_3 = input.LA(2);
				if ( (LA129_3==65) ) {
					int LA129_10 = input.LA(3);
					if ( (LA129_10==INT_NUMERAL) ) {
						int LA129_14 = input.LA(4);
						if ( (synpred254_JPA2()) ) {
							alt129=3;
						}
						else if ( (true) ) {
							alt129=4;
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
								new NoViableAltException("", 129, 10, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA129_3==INT_NUMERAL) ) {
					int LA129_11 = input.LA(3);
					if ( (synpred254_JPA2()) ) {
						alt129=3;
					}
					else if ( (true) ) {
						alt129=4;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 129, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA129_4 = input.LA(2);
				if ( (synpred254_JPA2()) ) {
					alt129=3;
				}
				else if ( (true) ) {
					alt129=4;
				}

				}
				break;
			case 58:
				{
				int LA129_5 = input.LA(2);
				if ( (LA129_5==WORD) ) {
					int LA129_13 = input.LA(3);
					if ( (LA129_13==147) ) {
						int LA129_15 = input.LA(4);
						if ( (synpred254_JPA2()) ) {
							alt129=3;
						}
						else if ( (true) ) {
							alt129=4;
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
								new NoViableAltException("", 129, 13, input);
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
							new NoViableAltException("", 129, 5, input);
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
			case 60:
			case 62:
			case 65:
			case 77:
			case 79:
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
			case 145:
			case 146:
				{
				alt129=4;
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
					// JPA2.g:443:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_function_arg4122);
					literal510=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal510.getTree());

					}
					break;
				case 2 :
					// JPA2.g:444:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_function_arg4130);
					path_expression511=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression511.getTree());

					}
					break;
				case 3 :
					// JPA2.g:445:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_function_arg4138);
					input_parameter512=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter512.getTree());

					}
					break;
				case 4 :
					// JPA2.g:446:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_function_arg4146);
					scalar_expression513=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression513.getTree());

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
	// JPA2.g:447:1: case_expression : ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression );
	public final JPA2Parser.case_expression_return case_expression() throws RecognitionException {
		JPA2Parser.case_expression_return retval = new JPA2Parser.case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope general_case_expression514 =null;
		ParserRuleReturnScope simple_case_expression515 =null;
		ParserRuleReturnScope coalesce_expression516 =null;
		ParserRuleReturnScope nullif_expression517 =null;


		try {
			// JPA2.g:448:5: ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression )
			int alt130=4;
			switch ( input.LA(1) ) {
			case 84:
				{
				int LA130_1 = input.LA(2);
				if ( (LA130_1==142) ) {
					alt130=1;
				}
				else if ( (LA130_1==GROUP||LA130_1==WORD||LA130_1==136) ) {
					alt130=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 130, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 86:
				{
				alt130=3;
				}
				break;
			case 118:
				{
				alt130=4;
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
					// JPA2.g:448:7: general_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_case_expression_in_case_expression4157);
					general_case_expression514=general_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_case_expression514.getTree());

					}
					break;
				case 2 :
					// JPA2.g:449:7: simple_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_case_expression_in_case_expression4165);
					simple_case_expression515=simple_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_case_expression515.getTree());

					}
					break;
				case 3 :
					// JPA2.g:450:7: coalesce_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_coalesce_expression_in_case_expression4173);
					coalesce_expression516=coalesce_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, coalesce_expression516.getTree());

					}
					break;
				case 4 :
					// JPA2.g:451:7: nullif_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_nullif_expression_in_case_expression4181);
					nullif_expression517=nullif_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, nullif_expression517.getTree());

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
	// JPA2.g:452:1: general_case_expression : 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.general_case_expression_return general_case_expression() throws RecognitionException {
		JPA2Parser.general_case_expression_return retval = new JPA2Parser.general_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal518=null;
		Token string_literal521=null;
		Token string_literal523=null;
		ParserRuleReturnScope when_clause519 =null;
		ParserRuleReturnScope when_clause520 =null;
		ParserRuleReturnScope scalar_expression522 =null;

		Object string_literal518_tree=null;
		Object string_literal521_tree=null;
		Object string_literal523_tree=null;

		try {
			// JPA2.g:453:5: ( 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:453:7: 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal518=(Token)match(input,84,FOLLOW_84_in_general_case_expression4192); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal518_tree = (Object)adaptor.create(string_literal518);
			adaptor.addChild(root_0, string_literal518_tree);
			}

			pushFollow(FOLLOW_when_clause_in_general_case_expression4194);
			when_clause519=when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause519.getTree());

			// JPA2.g:453:26: ( when_clause )*
			loop131:
			while (true) {
				int alt131=2;
				int LA131_0 = input.LA(1);
				if ( (LA131_0==142) ) {
					alt131=1;
				}

				switch (alt131) {
				case 1 :
					// JPA2.g:453:27: when_clause
					{
					pushFollow(FOLLOW_when_clause_in_general_case_expression4197);
					when_clause520=when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause520.getTree());

					}
					break;

				default :
					break loop131;
				}
			}

			string_literal521=(Token)match(input,93,FOLLOW_93_in_general_case_expression4201); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal521_tree = (Object)adaptor.create(string_literal521);
			adaptor.addChild(root_0, string_literal521_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_general_case_expression4203);
			scalar_expression522=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression522.getTree());

			string_literal523=(Token)match(input,95,FOLLOW_95_in_general_case_expression4205); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal523_tree = (Object)adaptor.create(string_literal523);
			adaptor.addChild(root_0, string_literal523_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:454:1: when_clause : 'WHEN' conditional_expression 'THEN' scalar_expression ;
	public final JPA2Parser.when_clause_return when_clause() throws RecognitionException {
		JPA2Parser.when_clause_return retval = new JPA2Parser.when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal524=null;
		Token string_literal526=null;
		ParserRuleReturnScope conditional_expression525 =null;
		ParserRuleReturnScope scalar_expression527 =null;

		Object string_literal524_tree=null;
		Object string_literal526_tree=null;

		try {
			// JPA2.g:455:5: ( 'WHEN' conditional_expression 'THEN' scalar_expression )
			// JPA2.g:455:7: 'WHEN' conditional_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal524=(Token)match(input,142,FOLLOW_142_in_when_clause4216); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal524_tree = (Object)adaptor.create(string_literal524);
			adaptor.addChild(root_0, string_literal524_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_when_clause4218);
			conditional_expression525=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression525.getTree());

			string_literal526=(Token)match(input,132,FOLLOW_132_in_when_clause4220); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal526_tree = (Object)adaptor.create(string_literal526);
			adaptor.addChild(root_0, string_literal526_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_when_clause4222);
			scalar_expression527=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression527.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:456:1: simple_case_expression : 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.simple_case_expression_return simple_case_expression() throws RecognitionException {
		JPA2Parser.simple_case_expression_return retval = new JPA2Parser.simple_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal528=null;
		Token string_literal532=null;
		Token string_literal534=null;
		ParserRuleReturnScope case_operand529 =null;
		ParserRuleReturnScope simple_when_clause530 =null;
		ParserRuleReturnScope simple_when_clause531 =null;
		ParserRuleReturnScope scalar_expression533 =null;

		Object string_literal528_tree=null;
		Object string_literal532_tree=null;
		Object string_literal534_tree=null;

		try {
			// JPA2.g:457:5: ( 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:457:7: 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal528=(Token)match(input,84,FOLLOW_84_in_simple_case_expression4233); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal528_tree = (Object)adaptor.create(string_literal528);
			adaptor.addChild(root_0, string_literal528_tree);
			}

			pushFollow(FOLLOW_case_operand_in_simple_case_expression4235);
			case_operand529=case_operand();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, case_operand529.getTree());

			pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4237);
			simple_when_clause530=simple_when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause530.getTree());

			// JPA2.g:457:46: ( simple_when_clause )*
			loop132:
			while (true) {
				int alt132=2;
				int LA132_0 = input.LA(1);
				if ( (LA132_0==142) ) {
					alt132=1;
				}

				switch (alt132) {
				case 1 :
					// JPA2.g:457:47: simple_when_clause
					{
					pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4240);
					simple_when_clause531=simple_when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause531.getTree());

					}
					break;

				default :
					break loop132;
				}
			}

			string_literal532=(Token)match(input,93,FOLLOW_93_in_simple_case_expression4244); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal532_tree = (Object)adaptor.create(string_literal532);
			adaptor.addChild(root_0, string_literal532_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_case_expression4246);
			scalar_expression533=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression533.getTree());

			string_literal534=(Token)match(input,95,FOLLOW_95_in_simple_case_expression4248); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal534_tree = (Object)adaptor.create(string_literal534);
			adaptor.addChild(root_0, string_literal534_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:458:1: case_operand : ( path_expression | type_discriminator );
	public final JPA2Parser.case_operand_return case_operand() throws RecognitionException {
		JPA2Parser.case_operand_return retval = new JPA2Parser.case_operand_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression535 =null;
		ParserRuleReturnScope type_discriminator536 =null;


		try {
			// JPA2.g:459:5: ( path_expression | type_discriminator )
			int alt133=2;
			int LA133_0 = input.LA(1);
			if ( (LA133_0==GROUP||LA133_0==WORD) ) {
				alt133=1;
			}
			else if ( (LA133_0==136) ) {
				alt133=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 133, 0, input);
				throw nvae;
			}

			switch (alt133) {
				case 1 :
					// JPA2.g:459:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_case_operand4259);
					path_expression535=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression535.getTree());

					}
					break;
				case 2 :
					// JPA2.g:460:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_case_operand4267);
					type_discriminator536=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator536.getTree());

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
	// JPA2.g:461:1: simple_when_clause : 'WHEN' scalar_expression 'THEN' scalar_expression ;
	public final JPA2Parser.simple_when_clause_return simple_when_clause() throws RecognitionException {
		JPA2Parser.simple_when_clause_return retval = new JPA2Parser.simple_when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal537=null;
		Token string_literal539=null;
		ParserRuleReturnScope scalar_expression538 =null;
		ParserRuleReturnScope scalar_expression540 =null;

		Object string_literal537_tree=null;
		Object string_literal539_tree=null;

		try {
			// JPA2.g:462:5: ( 'WHEN' scalar_expression 'THEN' scalar_expression )
			// JPA2.g:462:7: 'WHEN' scalar_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal537=(Token)match(input,142,FOLLOW_142_in_simple_when_clause4278); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal537_tree = (Object)adaptor.create(string_literal537);
			adaptor.addChild(root_0, string_literal537_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4280);
			scalar_expression538=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression538.getTree());

			string_literal539=(Token)match(input,132,FOLLOW_132_in_simple_when_clause4282); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal539_tree = (Object)adaptor.create(string_literal539);
			adaptor.addChild(root_0, string_literal539_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4284);
			scalar_expression540=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression540.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:463:1: coalesce_expression : 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' ;
	public final JPA2Parser.coalesce_expression_return coalesce_expression() throws RecognitionException {
		JPA2Parser.coalesce_expression_return retval = new JPA2Parser.coalesce_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal541=null;
		Token char_literal543=null;
		Token char_literal545=null;
		ParserRuleReturnScope scalar_expression542 =null;
		ParserRuleReturnScope scalar_expression544 =null;

		Object string_literal541_tree=null;
		Object char_literal543_tree=null;
		Object char_literal545_tree=null;

		try {
			// JPA2.g:464:5: ( 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' )
			// JPA2.g:464:7: 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal541=(Token)match(input,86,FOLLOW_86_in_coalesce_expression4295); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal541_tree = (Object)adaptor.create(string_literal541);
			adaptor.addChild(root_0, string_literal541_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4296);
			scalar_expression542=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression542.getTree());

			// JPA2.g:464:36: ( ',' scalar_expression )+
			int cnt134=0;
			loop134:
			while (true) {
				int alt134=2;
				int LA134_0 = input.LA(1);
				if ( (LA134_0==61) ) {
					alt134=1;
				}

				switch (alt134) {
				case 1 :
					// JPA2.g:464:37: ',' scalar_expression
					{
					char_literal543=(Token)match(input,61,FOLLOW_61_in_coalesce_expression4299); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal543_tree = (Object)adaptor.create(char_literal543);
					adaptor.addChild(root_0, char_literal543_tree);
					}

					pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4301);
					scalar_expression544=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression544.getTree());

					}
					break;

				default :
					if ( cnt134 >= 1 ) break loop134;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(134, input);
					throw eee;
				}
				cnt134++;
			}

			char_literal545=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_coalesce_expression4304); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal545_tree = (Object)adaptor.create(char_literal545);
			adaptor.addChild(root_0, char_literal545_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:465:1: nullif_expression : 'NULLIF(' scalar_expression ',' scalar_expression ')' ;
	public final JPA2Parser.nullif_expression_return nullif_expression() throws RecognitionException {
		JPA2Parser.nullif_expression_return retval = new JPA2Parser.nullif_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal546=null;
		Token char_literal548=null;
		Token char_literal550=null;
		ParserRuleReturnScope scalar_expression547 =null;
		ParserRuleReturnScope scalar_expression549 =null;

		Object string_literal546_tree=null;
		Object char_literal548_tree=null;
		Object char_literal550_tree=null;

		try {
			// JPA2.g:466:5: ( 'NULLIF(' scalar_expression ',' scalar_expression ')' )
			// JPA2.g:466:7: 'NULLIF(' scalar_expression ',' scalar_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal546=(Token)match(input,118,FOLLOW_118_in_nullif_expression4315); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal546_tree = (Object)adaptor.create(string_literal546);
			adaptor.addChild(root_0, string_literal546_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4316);
			scalar_expression547=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression547.getTree());

			char_literal548=(Token)match(input,61,FOLLOW_61_in_nullif_expression4318); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal548_tree = (Object)adaptor.create(char_literal548);
			adaptor.addChild(root_0, char_literal548_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4320);
			scalar_expression549=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression549.getTree());

			char_literal550=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nullif_expression4321); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal550_tree = (Object)adaptor.create(char_literal550);
			adaptor.addChild(root_0, char_literal550_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:468:1: extension_functions : ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | extract_function | enum_function );
	public final JPA2Parser.extension_functions_return extension_functions() throws RecognitionException {
		JPA2Parser.extension_functions_return retval = new JPA2Parser.extension_functions_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal551=null;
		Token WORD553=null;
		Token char_literal554=null;
		Token INT_NUMERAL555=null;
		Token char_literal556=null;
		Token INT_NUMERAL557=null;
		Token char_literal558=null;
		Token char_literal559=null;
		ParserRuleReturnScope function_arg552 =null;
		ParserRuleReturnScope extract_function560 =null;
		ParserRuleReturnScope enum_function561 =null;

		Object string_literal551_tree=null;
		Object WORD553_tree=null;
		Object char_literal554_tree=null;
		Object INT_NUMERAL555_tree=null;
		Object char_literal556_tree=null;
		Object INT_NUMERAL557_tree=null;
		Object char_literal558_tree=null;
		Object char_literal559_tree=null;

		try {
			// JPA2.g:469:5: ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | extract_function | enum_function )
			int alt137=3;
			switch ( input.LA(1) ) {
			case 85:
				{
				alt137=1;
				}
				break;
			case 100:
				{
				alt137=2;
				}
				break;
			case 77:
				{
				alt137=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 137, 0, input);
				throw nvae;
			}
			switch (alt137) {
				case 1 :
					// JPA2.g:469:7: 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal551=(Token)match(input,85,FOLLOW_85_in_extension_functions4333); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal551_tree = (Object)adaptor.create(string_literal551);
					adaptor.addChild(root_0, string_literal551_tree);
					}

					pushFollow(FOLLOW_function_arg_in_extension_functions4335);
					function_arg552=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg552.getTree());

					WORD553=(Token)match(input,WORD,FOLLOW_WORD_in_extension_functions4337); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD553_tree = (Object)adaptor.create(WORD553);
					adaptor.addChild(root_0, WORD553_tree);
					}

					// JPA2.g:469:33: ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )*
					loop136:
					while (true) {
						int alt136=2;
						int LA136_0 = input.LA(1);
						if ( (LA136_0==LPAREN) ) {
							alt136=1;
						}

						switch (alt136) {
						case 1 :
							// JPA2.g:469:34: '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')'
							{
							char_literal554=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_extension_functions4340); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal554_tree = (Object)adaptor.create(char_literal554);
							adaptor.addChild(root_0, char_literal554_tree);
							}

							INT_NUMERAL555=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4341); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							INT_NUMERAL555_tree = (Object)adaptor.create(INT_NUMERAL555);
							adaptor.addChild(root_0, INT_NUMERAL555_tree);
							}

							// JPA2.g:469:49: ( ',' INT_NUMERAL )*
							loop135:
							while (true) {
								int alt135=2;
								int LA135_0 = input.LA(1);
								if ( (LA135_0==61) ) {
									alt135=1;
								}

								switch (alt135) {
								case 1 :
									// JPA2.g:469:50: ',' INT_NUMERAL
									{
									char_literal556=(Token)match(input,61,FOLLOW_61_in_extension_functions4344); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									char_literal556_tree = (Object)adaptor.create(char_literal556);
									adaptor.addChild(root_0, char_literal556_tree);
									}

									INT_NUMERAL557=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4346); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									INT_NUMERAL557_tree = (Object)adaptor.create(INT_NUMERAL557);
									adaptor.addChild(root_0, INT_NUMERAL557_tree);
									}

									}
									break;

								default :
									break loop135;
								}
							}

							char_literal558=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4351); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal558_tree = (Object)adaptor.create(char_literal558);
							adaptor.addChild(root_0, char_literal558_tree);
							}

							}
							break;

						default :
							break loop136;
						}
					}

					char_literal559=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4355); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal559_tree = (Object)adaptor.create(char_literal559);
					adaptor.addChild(root_0, char_literal559_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:470:7: extract_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extract_function_in_extension_functions4363);
					extract_function560=extract_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extract_function560.getTree());

					}
					break;
				case 3 :
					// JPA2.g:471:7: enum_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_function_in_extension_functions4371);
					enum_function561=enum_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_function561.getTree());

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
	// JPA2.g:473:1: extract_function : 'EXTRACT(' date_part 'FROM' function_arg ')' ;
	public final JPA2Parser.extract_function_return extract_function() throws RecognitionException {
		JPA2Parser.extract_function_return retval = new JPA2Parser.extract_function_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal562=null;
		Token string_literal564=null;
		Token char_literal566=null;
		ParserRuleReturnScope date_part563 =null;
		ParserRuleReturnScope function_arg565 =null;

		Object string_literal562_tree=null;
		Object string_literal564_tree=null;
		Object char_literal566_tree=null;

		try {
			// JPA2.g:474:5: ( 'EXTRACT(' date_part 'FROM' function_arg ')' )
			// JPA2.g:474:7: 'EXTRACT(' date_part 'FROM' function_arg ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal562=(Token)match(input,100,FOLLOW_100_in_extract_function4383); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal562_tree = (Object)adaptor.create(string_literal562);
			adaptor.addChild(root_0, string_literal562_tree);
			}

			pushFollow(FOLLOW_date_part_in_extract_function4385);
			date_part563=date_part();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part563.getTree());

			string_literal564=(Token)match(input,101,FOLLOW_101_in_extract_function4387); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal564_tree = (Object)adaptor.create(string_literal564);
			adaptor.addChild(root_0, string_literal564_tree);
			}

			pushFollow(FOLLOW_function_arg_in_extract_function4389);
			function_arg565=function_arg();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg565.getTree());

			char_literal566=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extract_function4391); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal566_tree = (Object)adaptor.create(char_literal566);
			adaptor.addChild(root_0, char_literal566_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:476:1: enum_function : '@ENUM' '(' enum_value_literal ')' -> ^( T_ENUM_MACROS[$enum_value_literal.text] ) ;
	public final JPA2Parser.enum_function_return enum_function() throws RecognitionException {
		JPA2Parser.enum_function_return retval = new JPA2Parser.enum_function_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal567=null;
		Token char_literal568=null;
		Token char_literal570=null;
		ParserRuleReturnScope enum_value_literal569 =null;

		Object string_literal567_tree=null;
		Object char_literal568_tree=null;
		Object char_literal570_tree=null;
		RewriteRuleTokenStream stream_77=new RewriteRuleTokenStream(adaptor,"token 77");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_enum_value_literal=new RewriteRuleSubtreeStream(adaptor,"rule enum_value_literal");

		try {
			// JPA2.g:477:5: ( '@ENUM' '(' enum_value_literal ')' -> ^( T_ENUM_MACROS[$enum_value_literal.text] ) )
			// JPA2.g:477:7: '@ENUM' '(' enum_value_literal ')'
			{
			string_literal567=(Token)match(input,77,FOLLOW_77_in_enum_function4403); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_77.add(string_literal567);

			char_literal568=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_enum_function4405); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal568);

			pushFollow(FOLLOW_enum_value_literal_in_enum_function4407);
			enum_value_literal569=enum_value_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_enum_value_literal.add(enum_value_literal569.getTree());
			char_literal570=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_enum_function4409); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal570);

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
			// 477:42: -> ^( T_ENUM_MACROS[$enum_value_literal.text] )
			{
				// JPA2.g:477:45: ^( T_ENUM_MACROS[$enum_value_literal.text] )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new EnumConditionNode(T_ENUM_MACROS, (enum_value_literal569!=null?input.toString(enum_value_literal569.start,enum_value_literal569.stop):null)), root_1);
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
	// JPA2.g:479:10: fragment date_part : ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' );
	public final JPA2Parser.date_part_return date_part() throws RecognitionException {
		JPA2Parser.date_part_return retval = new JPA2Parser.date_part_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set571=null;

		Object set571_tree=null;

		try {
			// JPA2.g:480:5: ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set571=input.LT(1);
			if ( input.LA(1)==91||input.LA(1)==97||input.LA(1)==103||input.LA(1)==112||input.LA(1)==114||input.LA(1)==124||input.LA(1)==126||input.LA(1)==141||input.LA(1)==144 ) {
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
	// $ANTLR end "date_part"


	public static class input_parameter_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "input_parameter"
	// JPA2.g:483:1: input_parameter : ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) );
	public final JPA2Parser.input_parameter_return input_parameter() throws RecognitionException {
		JPA2Parser.input_parameter_return retval = new JPA2Parser.input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal572=null;
		Token NAMED_PARAMETER574=null;
		Token string_literal575=null;
		Token WORD576=null;
		Token char_literal577=null;
		ParserRuleReturnScope numeric_literal573 =null;

		Object char_literal572_tree=null;
		Object NAMED_PARAMETER574_tree=null;
		Object string_literal575_tree=null;
		Object WORD576_tree=null;
		Object char_literal577_tree=null;
		RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_147=new RewriteRuleTokenStream(adaptor,"token 147");
		RewriteRuleTokenStream stream_72=new RewriteRuleTokenStream(adaptor,"token 72");
		RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
		RewriteRuleSubtreeStream stream_numeric_literal=new RewriteRuleSubtreeStream(adaptor,"rule numeric_literal");

		try {
			// JPA2.g:484:5: ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
			int alt138=3;
			switch ( input.LA(1) ) {
			case 72:
				{
				alt138=1;
				}
				break;
			case NAMED_PARAMETER:
				{
				alt138=2;
				}
				break;
			case 58:
				{
				alt138=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 138, 0, input);
				throw nvae;
			}
			switch (alt138) {
				case 1 :
					// JPA2.g:484:7: '?' numeric_literal
					{
					char_literal572=(Token)match(input,72,FOLLOW_72_in_input_parameter4476); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_72.add(char_literal572);

					pushFollow(FOLLOW_numeric_literal_in_input_parameter4478);
					numeric_literal573=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_literal.add(numeric_literal573.getTree());
					// AST REWRITE
					// elements: 72, numeric_literal
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 484:27: -> ^( T_PARAMETER[] '?' numeric_literal )
					{
						// JPA2.g:484:30: ^( T_PARAMETER[] '?' numeric_literal )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_72.nextNode());
						adaptor.addChild(root_1, stream_numeric_literal.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// JPA2.g:485:7: NAMED_PARAMETER
					{
					NAMED_PARAMETER574=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter4501); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER574);

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
					// 485:23: -> ^( T_PARAMETER[] NAMED_PARAMETER )
					{
						// JPA2.g:485:26: ^( T_PARAMETER[] NAMED_PARAMETER )
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
					// JPA2.g:486:7: '${' WORD '}'
					{
					string_literal575=(Token)match(input,58,FOLLOW_58_in_input_parameter4522); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(string_literal575);

					WORD576=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter4524); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WORD.add(WORD576);

					char_literal577=(Token)match(input,147,FOLLOW_147_in_input_parameter4526); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_147.add(char_literal577);

					// AST REWRITE
					// elements: WORD, 147, 58
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 486:21: -> ^( T_PARAMETER[] '${' WORD '}' )
					{
						// JPA2.g:486:24: ^( T_PARAMETER[] '${' WORD '}' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_58.nextNode());
						adaptor.addChild(root_1, stream_WORD.nextNode());
						adaptor.addChild(root_1, stream_147.nextNode());
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
	// JPA2.g:488:1: literal : WORD ;
	public final JPA2Parser.literal_return literal() throws RecognitionException {
		JPA2Parser.literal_return retval = new JPA2Parser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD578=null;

		Object WORD578_tree=null;

		try {
			// JPA2.g:489:5: ( WORD )
			// JPA2.g:489:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD578=(Token)match(input,WORD,FOLLOW_WORD_in_literal4554); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD578_tree = (Object)adaptor.create(WORD578);
			adaptor.addChild(root_0, WORD578_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:491:1: constructor_name : WORD ;
	public final JPA2Parser.constructor_name_return constructor_name() throws RecognitionException {
		JPA2Parser.constructor_name_return retval = new JPA2Parser.constructor_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD579=null;

		Object WORD579_tree=null;

		try {
			// JPA2.g:492:5: ( WORD )
			// JPA2.g:492:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD579=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4566); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD579_tree = (Object)adaptor.create(WORD579);
			adaptor.addChild(root_0, WORD579_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:494:1: enum_literal : WORD ;
	public final JPA2Parser.enum_literal_return enum_literal() throws RecognitionException {
		JPA2Parser.enum_literal_return retval = new JPA2Parser.enum_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD580=null;

		Object WORD580_tree=null;

		try {
			// JPA2.g:495:5: ( WORD )
			// JPA2.g:495:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD580=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal4578); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD580_tree = (Object)adaptor.create(WORD580);
			adaptor.addChild(root_0, WORD580_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:497:1: boolean_literal : ( 'true' | 'false' );
	public final JPA2Parser.boolean_literal_return boolean_literal() throws RecognitionException {
		JPA2Parser.boolean_literal_return retval = new JPA2Parser.boolean_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set581=null;

		Object set581_tree=null;

		try {
			// JPA2.g:498:5: ( 'true' | 'false' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set581=input.LT(1);
			if ( (input.LA(1) >= 145 && input.LA(1) <= 146) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set581));
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
	// JPA2.g:502:1: field : ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | 'AS' | 'MEMBER' | date_part );
	public final JPA2Parser.field_return field() throws RecognitionException {
		JPA2Parser.field_return retval = new JPA2Parser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD582=null;
		Token string_literal583=null;
		Token string_literal584=null;
		Token string_literal585=null;
		Token string_literal586=null;
		Token string_literal587=null;
		Token string_literal588=null;
		Token string_literal589=null;
		Token string_literal590=null;
		Token string_literal591=null;
		Token string_literal592=null;
		Token string_literal593=null;
		ParserRuleReturnScope date_part594 =null;

		Object WORD582_tree=null;
		Object string_literal583_tree=null;
		Object string_literal584_tree=null;
		Object string_literal585_tree=null;
		Object string_literal586_tree=null;
		Object string_literal587_tree=null;
		Object string_literal588_tree=null;
		Object string_literal589_tree=null;
		Object string_literal590_tree=null;
		Object string_literal591_tree=null;
		Object string_literal592_tree=null;
		Object string_literal593_tree=null;

		try {
			// JPA2.g:503:5: ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | 'AS' | 'MEMBER' | date_part )
			int alt139=13;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt139=1;
				}
				break;
			case 127:
				{
				alt139=2;
				}
				break;
			case 101:
				{
				alt139=3;
				}
				break;
			case GROUP:
				{
				alt139=4;
				}
				break;
			case ORDER:
				{
				alt139=5;
				}
				break;
			case MAX:
				{
				alt139=6;
				}
				break;
			case MIN:
				{
				alt139=7;
				}
				break;
			case SUM:
				{
				alt139=8;
				}
				break;
			case AVG:
				{
				alt139=9;
				}
				break;
			case COUNT:
				{
				alt139=10;
				}
				break;
			case AS:
				{
				alt139=11;
				}
				break;
			case 111:
				{
				alt139=12;
				}
				break;
			case 91:
			case 97:
			case 103:
			case 112:
			case 114:
			case 124:
			case 126:
			case 141:
			case 144:
				{
				alt139=13;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 139, 0, input);
				throw nvae;
			}
			switch (alt139) {
				case 1 :
					// JPA2.g:503:7: WORD
					{
					root_0 = (Object)adaptor.nil();


					WORD582=(Token)match(input,WORD,FOLLOW_WORD_in_field4611); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD582_tree = (Object)adaptor.create(WORD582);
					adaptor.addChild(root_0, WORD582_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:503:14: 'SELECT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal583=(Token)match(input,127,FOLLOW_127_in_field4615); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal583_tree = (Object)adaptor.create(string_literal583);
					adaptor.addChild(root_0, string_literal583_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:503:25: 'FROM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal584=(Token)match(input,101,FOLLOW_101_in_field4619); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal584_tree = (Object)adaptor.create(string_literal584);
					adaptor.addChild(root_0, string_literal584_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:503:34: 'GROUP'
					{
					root_0 = (Object)adaptor.nil();


					string_literal585=(Token)match(input,GROUP,FOLLOW_GROUP_in_field4623); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal585_tree = (Object)adaptor.create(string_literal585);
					adaptor.addChild(root_0, string_literal585_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:503:44: 'ORDER'
					{
					root_0 = (Object)adaptor.nil();


					string_literal586=(Token)match(input,ORDER,FOLLOW_ORDER_in_field4627); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal586_tree = (Object)adaptor.create(string_literal586);
					adaptor.addChild(root_0, string_literal586_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:503:54: 'MAX'
					{
					root_0 = (Object)adaptor.nil();


					string_literal587=(Token)match(input,MAX,FOLLOW_MAX_in_field4631); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal587_tree = (Object)adaptor.create(string_literal587);
					adaptor.addChild(root_0, string_literal587_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:503:62: 'MIN'
					{
					root_0 = (Object)adaptor.nil();


					string_literal588=(Token)match(input,MIN,FOLLOW_MIN_in_field4635); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal588_tree = (Object)adaptor.create(string_literal588);
					adaptor.addChild(root_0, string_literal588_tree);
					}

					}
					break;
				case 8 :
					// JPA2.g:503:70: 'SUM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal589=(Token)match(input,SUM,FOLLOW_SUM_in_field4639); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal589_tree = (Object)adaptor.create(string_literal589);
					adaptor.addChild(root_0, string_literal589_tree);
					}

					}
					break;
				case 9 :
					// JPA2.g:503:78: 'AVG'
					{
					root_0 = (Object)adaptor.nil();


					string_literal590=(Token)match(input,AVG,FOLLOW_AVG_in_field4643); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal590_tree = (Object)adaptor.create(string_literal590);
					adaptor.addChild(root_0, string_literal590_tree);
					}

					}
					break;
				case 10 :
					// JPA2.g:503:86: 'COUNT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal591=(Token)match(input,COUNT,FOLLOW_COUNT_in_field4647); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal591_tree = (Object)adaptor.create(string_literal591);
					adaptor.addChild(root_0, string_literal591_tree);
					}

					}
					break;
				case 11 :
					// JPA2.g:503:96: 'AS'
					{
					root_0 = (Object)adaptor.nil();


					string_literal592=(Token)match(input,AS,FOLLOW_AS_in_field4651); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal592_tree = (Object)adaptor.create(string_literal592);
					adaptor.addChild(root_0, string_literal592_tree);
					}

					}
					break;
				case 12 :
					// JPA2.g:503:103: 'MEMBER'
					{
					root_0 = (Object)adaptor.nil();


					string_literal593=(Token)match(input,111,FOLLOW_111_in_field4655); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal593_tree = (Object)adaptor.create(string_literal593);
					adaptor.addChild(root_0, string_literal593_tree);
					}

					}
					break;
				case 13 :
					// JPA2.g:503:114: date_part
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_part_in_field4659);
					date_part594=date_part();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part594.getTree());

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
	// JPA2.g:505:1: identification_variable : ( WORD | 'GROUP' );
	public final JPA2Parser.identification_variable_return identification_variable() throws RecognitionException {
		JPA2Parser.identification_variable_return retval = new JPA2Parser.identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set595=null;

		Object set595_tree=null;

		try {
			// JPA2.g:506:5: ( WORD | 'GROUP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set595=input.LT(1);
			if ( input.LA(1)==GROUP||input.LA(1)==WORD ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set595));
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
	// JPA2.g:508:1: parameter_name : WORD ( '.' WORD )* ;
	public final JPA2Parser.parameter_name_return parameter_name() throws RecognitionException {
		JPA2Parser.parameter_name_return retval = new JPA2Parser.parameter_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD596=null;
		Token char_literal597=null;
		Token WORD598=null;

		Object WORD596_tree=null;
		Object char_literal597_tree=null;
		Object WORD598_tree=null;

		try {
			// JPA2.g:509:5: ( WORD ( '.' WORD )* )
			// JPA2.g:509:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD596=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4687); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD596_tree = (Object)adaptor.create(WORD596);
			adaptor.addChild(root_0, WORD596_tree);
			}

			// JPA2.g:509:12: ( '.' WORD )*
			loop140:
			while (true) {
				int alt140=2;
				int LA140_0 = input.LA(1);
				if ( (LA140_0==63) ) {
					alt140=1;
				}

				switch (alt140) {
				case 1 :
					// JPA2.g:509:13: '.' WORD
					{
					char_literal597=(Token)match(input,63,FOLLOW_63_in_parameter_name4690); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal597_tree = (Object)adaptor.create(char_literal597);
					adaptor.addChild(root_0, char_literal597_tree);
					}

					WORD598=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4693); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD598_tree = (Object)adaptor.create(WORD598);
					adaptor.addChild(root_0, WORD598_tree);
					}

					}
					break;

				default :
					break loop140;
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
	// JPA2.g:512:1: escape_character : ( '\\'.\\'' | STRING_LITERAL );
	public final JPA2Parser.escape_character_return escape_character() throws RecognitionException {
		JPA2Parser.escape_character_return retval = new JPA2Parser.escape_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set599=null;

		Object set599_tree=null;

		try {
			// JPA2.g:513:5: ( '\\'.\\'' | STRING_LITERAL )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set599=input.LT(1);
			if ( input.LA(1)==STRING_LITERAL||input.LA(1)==TRIM_CHARACTER ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set599));
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
	// JPA2.g:514:1: trim_character : TRIM_CHARACTER ;
	public final JPA2Parser.trim_character_return trim_character() throws RecognitionException {
		JPA2Parser.trim_character_return retval = new JPA2Parser.trim_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRIM_CHARACTER600=null;

		Object TRIM_CHARACTER600_tree=null;

		try {
			// JPA2.g:515:5: ( TRIM_CHARACTER )
			// JPA2.g:515:7: TRIM_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			TRIM_CHARACTER600=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_trim_character4723); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TRIM_CHARACTER600_tree = (Object)adaptor.create(TRIM_CHARACTER600);
			adaptor.addChild(root_0, TRIM_CHARACTER600_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:516:1: string_literal : STRING_LITERAL ;
	public final JPA2Parser.string_literal_return string_literal() throws RecognitionException {
		JPA2Parser.string_literal_return retval = new JPA2Parser.string_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL601=null;

		Object STRING_LITERAL601_tree=null;

		try {
			// JPA2.g:517:5: ( STRING_LITERAL )
			// JPA2.g:517:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL601=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_literal4734); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL601_tree = (Object)adaptor.create(STRING_LITERAL601);
			adaptor.addChild(root_0, STRING_LITERAL601_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:518:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
	public final JPA2Parser.numeric_literal_return numeric_literal() throws RecognitionException {
		JPA2Parser.numeric_literal_return retval = new JPA2Parser.numeric_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal602=null;
		Token INT_NUMERAL603=null;

		Object string_literal602_tree=null;
		Object INT_NUMERAL603_tree=null;

		try {
			// JPA2.g:519:5: ( ( '0x' )? INT_NUMERAL )
			// JPA2.g:519:7: ( '0x' )? INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:519:7: ( '0x' )?
			int alt141=2;
			int LA141_0 = input.LA(1);
			if ( (LA141_0==65) ) {
				alt141=1;
			}
			switch (alt141) {
				case 1 :
					// JPA2.g:519:8: '0x'
					{
					string_literal602=(Token)match(input,65,FOLLOW_65_in_numeric_literal4746); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal602_tree = (Object)adaptor.create(string_literal602);
					adaptor.addChild(root_0, string_literal602_tree);
					}

					}
					break;

			}

			INT_NUMERAL603=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal4750); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL603_tree = (Object)adaptor.create(INT_NUMERAL603);
			adaptor.addChild(root_0, INT_NUMERAL603_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:520:1: decimal_literal : INT_NUMERAL '.' INT_NUMERAL ;
	public final JPA2Parser.decimal_literal_return decimal_literal() throws RecognitionException {
		JPA2Parser.decimal_literal_return retval = new JPA2Parser.decimal_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token INT_NUMERAL604=null;
		Token char_literal605=null;
		Token INT_NUMERAL606=null;

		Object INT_NUMERAL604_tree=null;
		Object char_literal605_tree=null;
		Object INT_NUMERAL606_tree=null;

		try {
			// JPA2.g:521:5: ( INT_NUMERAL '.' INT_NUMERAL )
			// JPA2.g:521:7: INT_NUMERAL '.' INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			INT_NUMERAL604=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_decimal_literal4762); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL604_tree = (Object)adaptor.create(INT_NUMERAL604);
			adaptor.addChild(root_0, INT_NUMERAL604_tree);
			}

			char_literal605=(Token)match(input,63,FOLLOW_63_in_decimal_literal4764); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal605_tree = (Object)adaptor.create(char_literal605);
			adaptor.addChild(root_0, char_literal605_tree);
			}

			INT_NUMERAL606=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_decimal_literal4766); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL606_tree = (Object)adaptor.create(INT_NUMERAL606);
			adaptor.addChild(root_0, INT_NUMERAL606_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:522:1: single_valued_object_field : WORD ;
	public final JPA2Parser.single_valued_object_field_return single_valued_object_field() throws RecognitionException {
		JPA2Parser.single_valued_object_field_return retval = new JPA2Parser.single_valued_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD607=null;

		Object WORD607_tree=null;

		try {
			// JPA2.g:523:5: ( WORD )
			// JPA2.g:523:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD607=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_object_field4777); if (state.failed) return retval;
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
	// $ANTLR end "single_valued_object_field"


	public static class single_valued_embeddable_object_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "single_valued_embeddable_object_field"
	// JPA2.g:524:1: single_valued_embeddable_object_field : WORD ;
	public final JPA2Parser.single_valued_embeddable_object_field_return single_valued_embeddable_object_field() throws RecognitionException {
		JPA2Parser.single_valued_embeddable_object_field_return retval = new JPA2Parser.single_valued_embeddable_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD608=null;

		Object WORD608_tree=null;

		try {
			// JPA2.g:525:5: ( WORD )
			// JPA2.g:525:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD608=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_embeddable_object_field4788); if (state.failed) return retval;
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
	// $ANTLR end "single_valued_embeddable_object_field"


	public static class collection_valued_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "collection_valued_field"
	// JPA2.g:526:1: collection_valued_field : WORD ;
	public final JPA2Parser.collection_valued_field_return collection_valued_field() throws RecognitionException {
		JPA2Parser.collection_valued_field_return retval = new JPA2Parser.collection_valued_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD609=null;

		Object WORD609_tree=null;

		try {
			// JPA2.g:527:5: ( WORD )
			// JPA2.g:527:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD609=(Token)match(input,WORD,FOLLOW_WORD_in_collection_valued_field4799); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD609_tree = (Object)adaptor.create(WORD609);
			adaptor.addChild(root_0, WORD609_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:528:1: entity_name : WORD ;
	public final JPA2Parser.entity_name_return entity_name() throws RecognitionException {
		JPA2Parser.entity_name_return retval = new JPA2Parser.entity_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD610=null;

		Object WORD610_tree=null;

		try {
			// JPA2.g:529:5: ( WORD )
			// JPA2.g:529:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD610=(Token)match(input,WORD,FOLLOW_WORD_in_entity_name4810); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD610_tree = (Object)adaptor.create(WORD610);
			adaptor.addChild(root_0, WORD610_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:530:1: subtype : WORD ;
	public final JPA2Parser.subtype_return subtype() throws RecognitionException {
		JPA2Parser.subtype_return retval = new JPA2Parser.subtype_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD611=null;

		Object WORD611_tree=null;

		try {
			// JPA2.g:531:5: ( WORD )
			// JPA2.g:531:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD611=(Token)match(input,WORD,FOLLOW_WORD_in_subtype4821); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD611_tree = (Object)adaptor.create(WORD611);
			adaptor.addChild(root_0, WORD611_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:532:1: entity_type_literal : WORD ;
	public final JPA2Parser.entity_type_literal_return entity_type_literal() throws RecognitionException {
		JPA2Parser.entity_type_literal_return retval = new JPA2Parser.entity_type_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD612=null;

		Object WORD612_tree=null;

		try {
			// JPA2.g:533:5: ( WORD )
			// JPA2.g:533:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD612=(Token)match(input,WORD,FOLLOW_WORD_in_entity_type_literal4832); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD612_tree = (Object)adaptor.create(WORD612);
			adaptor.addChild(root_0, WORD612_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:534:1: function_name : STRING_LITERAL ;
	public final JPA2Parser.function_name_return function_name() throws RecognitionException {
		JPA2Parser.function_name_return retval = new JPA2Parser.function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL613=null;

		Object STRING_LITERAL613_tree=null;

		try {
			// JPA2.g:535:5: ( STRING_LITERAL )
			// JPA2.g:535:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL613=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_function_name4843); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL613_tree = (Object)adaptor.create(STRING_LITERAL613);
			adaptor.addChild(root_0, STRING_LITERAL613_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:536:1: state_field : WORD ;
	public final JPA2Parser.state_field_return state_field() throws RecognitionException {
		JPA2Parser.state_field_return retval = new JPA2Parser.state_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD614=null;

		Object WORD614_tree=null;

		try {
			// JPA2.g:537:5: ( WORD )
			// JPA2.g:537:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD614=(Token)match(input,WORD,FOLLOW_WORD_in_state_field4854); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD614_tree = (Object)adaptor.create(WORD614);
			adaptor.addChild(root_0, WORD614_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:538:1: result_variable : WORD ;
	public final JPA2Parser.result_variable_return result_variable() throws RecognitionException {
		JPA2Parser.result_variable_return retval = new JPA2Parser.result_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD615=null;

		Object WORD615_tree=null;

		try {
			// JPA2.g:539:5: ( WORD )
			// JPA2.g:539:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD615=(Token)match(input,WORD,FOLLOW_WORD_in_result_variable4865); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD615_tree = (Object)adaptor.create(WORD615);
			adaptor.addChild(root_0, WORD615_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:540:1: superquery_identification_variable : WORD ;
	public final JPA2Parser.superquery_identification_variable_return superquery_identification_variable() throws RecognitionException {
		JPA2Parser.superquery_identification_variable_return retval = new JPA2Parser.superquery_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD616=null;

		Object WORD616_tree=null;

		try {
			// JPA2.g:541:5: ( WORD )
			// JPA2.g:541:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD616=(Token)match(input,WORD,FOLLOW_WORD_in_superquery_identification_variable4876); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD616_tree = (Object)adaptor.create(WORD616);
			adaptor.addChild(root_0, WORD616_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:542:1: date_time_timestamp_literal : WORD ;
	public final JPA2Parser.date_time_timestamp_literal_return date_time_timestamp_literal() throws RecognitionException {
		JPA2Parser.date_time_timestamp_literal_return retval = new JPA2Parser.date_time_timestamp_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD617=null;

		Object WORD617_tree=null;

		try {
			// JPA2.g:543:5: ( WORD )
			// JPA2.g:543:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD617=(Token)match(input,WORD,FOLLOW_WORD_in_date_time_timestamp_literal4887); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD617_tree = (Object)adaptor.create(WORD617);
			adaptor.addChild(root_0, WORD617_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:544:1: pattern_value : string_literal ;
	public final JPA2Parser.pattern_value_return pattern_value() throws RecognitionException {
		JPA2Parser.pattern_value_return retval = new JPA2Parser.pattern_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal618 =null;


		try {
			// JPA2.g:545:5: ( string_literal )
			// JPA2.g:545:7: string_literal
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_literal_in_pattern_value4898);
			string_literal618=string_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal618.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:546:1: collection_valued_input_parameter : input_parameter ;
	public final JPA2Parser.collection_valued_input_parameter_return collection_valued_input_parameter() throws RecognitionException {
		JPA2Parser.collection_valued_input_parameter_return retval = new JPA2Parser.collection_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter619 =null;


		try {
			// JPA2.g:547:5: ( input_parameter )
			// JPA2.g:547:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_collection_valued_input_parameter4909);
			input_parameter619=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter619.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:548:1: single_valued_input_parameter : input_parameter ;
	public final JPA2Parser.single_valued_input_parameter_return single_valued_input_parameter() throws RecognitionException {
		JPA2Parser.single_valued_input_parameter_return retval = new JPA2Parser.single_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter620 =null;


		try {
			// JPA2.g:549:5: ( input_parameter )
			// JPA2.g:549:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_single_valued_input_parameter4920);
			input_parameter620=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter620.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:550:1: enum_value_literal : WORD ( '.' WORD )* ;
	public final JPA2Parser.enum_value_literal_return enum_value_literal() throws RecognitionException {
		JPA2Parser.enum_value_literal_return retval = new JPA2Parser.enum_value_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD621=null;
		Token char_literal622=null;
		Token WORD623=null;

		Object WORD621_tree=null;
		Object char_literal622_tree=null;
		Object WORD623_tree=null;

		try {
			// JPA2.g:551:5: ( WORD ( '.' WORD )* )
			// JPA2.g:551:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD621=(Token)match(input,WORD,FOLLOW_WORD_in_enum_value_literal4931); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD621_tree = (Object)adaptor.create(WORD621);
			adaptor.addChild(root_0, WORD621_tree);
			}

			// JPA2.g:551:12: ( '.' WORD )*
			loop142:
			while (true) {
				int alt142=2;
				int LA142_0 = input.LA(1);
				if ( (LA142_0==63) ) {
					alt142=1;
				}

				switch (alt142) {
				case 1 :
					// JPA2.g:551:13: '.' WORD
					{
					char_literal622=(Token)match(input,63,FOLLOW_63_in_enum_value_literal4934); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal622_tree = (Object)adaptor.create(char_literal622);
					adaptor.addChild(root_0, char_literal622_tree);
					}

					WORD623=(Token)match(input,WORD,FOLLOW_WORD_in_enum_value_literal4937); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD623_tree = (Object)adaptor.create(WORD623);
					adaptor.addChild(root_0, WORD623_tree);
					}

					}
					break;

				default :
					break loop142;
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
		// JPA2.g:122:48: ( field )
		// JPA2.g:122:48: field
		{
		pushFollow(FOLLOW_field_in_synpred21_JPA2915);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred21_JPA2

	// $ANTLR start synpred30_JPA2
	public final void synpred30_JPA2_fragment() throws RecognitionException {
		// JPA2.g:140:48: ( field )
		// JPA2.g:140:48: field
		{
		pushFollow(FOLLOW_field_in_synpred30_JPA21105);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred30_JPA2

	// $ANTLR start synpred33_JPA2
	public final void synpred33_JPA2_fragment() throws RecognitionException {
		// JPA2.g:157:7: ( scalar_expression )
		// JPA2.g:157:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred33_JPA21231);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred33_JPA2

	// $ANTLR start synpred34_JPA2
	public final void synpred34_JPA2_fragment() throws RecognitionException {
		// JPA2.g:158:7: ( simple_entity_expression )
		// JPA2.g:158:7: simple_entity_expression
		{
		pushFollow(FOLLOW_simple_entity_expression_in_synpred34_JPA21239);
		simple_entity_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred34_JPA2

	// $ANTLR start synpred43_JPA2
	public final void synpred43_JPA2_fragment() throws RecognitionException {
		// JPA2.g:170:7: ( path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )? )
		// JPA2.g:170:7: path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
		{
		pushFollow(FOLLOW_path_expression_in_synpred43_JPA21364);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:170:23: ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
		int alt149=2;
		int LA149_0 = input.LA(1);
		if ( ((LA149_0 >= 59 && LA149_0 <= 60)||LA149_0==62||LA149_0==64) ) {
			alt149=1;
		}
		switch (alt149) {
			case 1 :
				// JPA2.g:170:24: ( '+' | '-' | '*' | '/' ) scalar_expression
				{
				if ( (input.LA(1) >= 59 && input.LA(1) <= 60)||input.LA(1)==62||input.LA(1)==64 ) {
					input.consume();
					state.errorRecovery=false;
					state.failed=false;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return;}
					MismatchedSetException mse = new MismatchedSetException(null,input);
					throw mse;
				}
				pushFollow(FOLLOW_scalar_expression_in_synpred43_JPA21383);
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
		// JPA2.g:171:7: ( identification_variable )
		// JPA2.g:171:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred44_JPA21393);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred44_JPA2

	// $ANTLR start synpred45_JPA2
	public final void synpred45_JPA2_fragment() throws RecognitionException {
		// JPA2.g:172:7: ( scalar_expression )
		// JPA2.g:172:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred45_JPA21411);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred45_JPA2

	// $ANTLR start synpred46_JPA2
	public final void synpred46_JPA2_fragment() throws RecognitionException {
		// JPA2.g:173:7: ( aggregate_expression )
		// JPA2.g:173:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred46_JPA21419);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred46_JPA2

	// $ANTLR start synpred49_JPA2
	public final void synpred49_JPA2_fragment() throws RecognitionException {
		// JPA2.g:179:7: ( path_expression )
		// JPA2.g:179:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred49_JPA21476);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred49_JPA2

	// $ANTLR start synpred50_JPA2
	public final void synpred50_JPA2_fragment() throws RecognitionException {
		// JPA2.g:180:7: ( scalar_expression )
		// JPA2.g:180:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred50_JPA21484);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred50_JPA2

	// $ANTLR start synpred51_JPA2
	public final void synpred51_JPA2_fragment() throws RecognitionException {
		// JPA2.g:181:7: ( aggregate_expression )
		// JPA2.g:181:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred51_JPA21492);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred51_JPA2

	// $ANTLR start synpred53_JPA2
	public final void synpred53_JPA2_fragment() throws RecognitionException {
		// JPA2.g:184:7: ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' )
		// JPA2.g:184:7: aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')'
		{
		pushFollow(FOLLOW_aggregate_expression_function_name_in_synpred53_JPA21511);
		aggregate_expression_function_name();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred53_JPA21513); if (state.failed) return;

		// JPA2.g:184:45: ( DISTINCT )?
		int alt150=2;
		int LA150_0 = input.LA(1);
		if ( (LA150_0==DISTINCT) ) {
			alt150=1;
		}
		switch (alt150) {
			case 1 :
				// JPA2.g:184:46: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred53_JPA21515); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_arithmetic_expression_in_synpred53_JPA21519);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred53_JPA21520); if (state.failed) return;

		}

	}
	// $ANTLR end synpred53_JPA2

	// $ANTLR start synpred55_JPA2
	public final void synpred55_JPA2_fragment() throws RecognitionException {
		// JPA2.g:186:7: ( 'COUNT' '(' ( DISTINCT )? count_argument ')' )
		// JPA2.g:186:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
		{
		match(input,COUNT,FOLLOW_COUNT_in_synpred55_JPA21554); if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred55_JPA21556); if (state.failed) return;

		// JPA2.g:186:18: ( DISTINCT )?
		int alt151=2;
		int LA151_0 = input.LA(1);
		if ( (LA151_0==DISTINCT) ) {
			alt151=1;
		}
		switch (alt151) {
			case 1 :
				// JPA2.g:186:19: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred55_JPA21558); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_count_argument_in_synpred55_JPA21562);
		count_argument();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred55_JPA21564); if (state.failed) return;

		}

	}
	// $ANTLR end synpred55_JPA2

	// $ANTLR start synpred67_JPA2
	public final void synpred67_JPA2_fragment() throws RecognitionException {
		// JPA2.g:209:7: ( path_expression )
		// JPA2.g:209:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred67_JPA21835);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred67_JPA2

	// $ANTLR start synpred68_JPA2
	public final void synpred68_JPA2_fragment() throws RecognitionException {
		// JPA2.g:209:25: ( general_identification_variable )
		// JPA2.g:209:25: general_identification_variable
		{
		pushFollow(FOLLOW_general_identification_variable_in_synpred68_JPA21839);
		general_identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred68_JPA2

	// $ANTLR start synpred69_JPA2
	public final void synpred69_JPA2_fragment() throws RecognitionException {
		// JPA2.g:209:59: ( result_variable )
		// JPA2.g:209:59: result_variable
		{
		pushFollow(FOLLOW_result_variable_in_synpred69_JPA21843);
		result_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred69_JPA2

	// $ANTLR start synpred70_JPA2
	public final void synpred70_JPA2_fragment() throws RecognitionException {
		// JPA2.g:209:77: ( scalar_expression )
		// JPA2.g:209:77: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred70_JPA21847);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred70_JPA2

	// $ANTLR start synpred80_JPA2
	public final void synpred80_JPA2_fragment() throws RecognitionException {
		// JPA2.g:226:7: ( general_derived_path '.' single_valued_object_field )
		// JPA2.g:226:7: general_derived_path '.' single_valued_object_field
		{
		pushFollow(FOLLOW_general_derived_path_in_synpred80_JPA22054);
		general_derived_path();
		state._fsp--;
		if (state.failed) return;

		match(input,63,FOLLOW_63_in_synpred80_JPA22055); if (state.failed) return;

		pushFollow(FOLLOW_single_valued_object_field_in_synpred80_JPA22056);
		single_valued_object_field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred80_JPA2

	// $ANTLR start synpred85_JPA2
	public final void synpred85_JPA2_fragment() throws RecognitionException {
		// JPA2.g:244:7: ( path_expression )
		// JPA2.g:244:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred85_JPA22208);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred85_JPA2

	// $ANTLR start synpred86_JPA2
	public final void synpred86_JPA2_fragment() throws RecognitionException {
		// JPA2.g:245:7: ( scalar_expression )
		// JPA2.g:245:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred86_JPA22216);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred86_JPA2

	// $ANTLR start synpred87_JPA2
	public final void synpred87_JPA2_fragment() throws RecognitionException {
		// JPA2.g:246:7: ( aggregate_expression )
		// JPA2.g:246:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred87_JPA22224);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred87_JPA2

	// $ANTLR start synpred88_JPA2
	public final void synpred88_JPA2_fragment() throws RecognitionException {
		// JPA2.g:249:7: ( arithmetic_expression )
		// JPA2.g:249:7: arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred88_JPA22243);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred88_JPA2

	// $ANTLR start synpred89_JPA2
	public final void synpred89_JPA2_fragment() throws RecognitionException {
		// JPA2.g:250:7: ( string_expression )
		// JPA2.g:250:7: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred89_JPA22251);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred89_JPA2

	// $ANTLR start synpred90_JPA2
	public final void synpred90_JPA2_fragment() throws RecognitionException {
		// JPA2.g:251:7: ( enum_expression )
		// JPA2.g:251:7: enum_expression
		{
		pushFollow(FOLLOW_enum_expression_in_synpred90_JPA22259);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred90_JPA2

	// $ANTLR start synpred91_JPA2
	public final void synpred91_JPA2_fragment() throws RecognitionException {
		// JPA2.g:252:7: ( datetime_expression )
		// JPA2.g:252:7: datetime_expression
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred91_JPA22267);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred91_JPA2

	// $ANTLR start synpred92_JPA2
	public final void synpred92_JPA2_fragment() throws RecognitionException {
		// JPA2.g:253:7: ( boolean_expression )
		// JPA2.g:253:7: boolean_expression
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred92_JPA22275);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred92_JPA2

	// $ANTLR start synpred93_JPA2
	public final void synpred93_JPA2_fragment() throws RecognitionException {
		// JPA2.g:254:7: ( case_expression )
		// JPA2.g:254:7: case_expression
		{
		pushFollow(FOLLOW_case_expression_in_synpred93_JPA22283);
		case_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred93_JPA2

	// $ANTLR start synpred96_JPA2
	public final void synpred96_JPA2_fragment() throws RecognitionException {
		// JPA2.g:261:8: ( 'NOT' )
		// JPA2.g:261:8: 'NOT'
		{
		match(input,NOT,FOLLOW_NOT_in_synpred96_JPA22343); if (state.failed) return;

		}

	}
	// $ANTLR end synpred96_JPA2

	// $ANTLR start synpred97_JPA2
	public final void synpred97_JPA2_fragment() throws RecognitionException {
		// JPA2.g:263:7: ( simple_cond_expression )
		// JPA2.g:263:7: simple_cond_expression
		{
		pushFollow(FOLLOW_simple_cond_expression_in_synpred97_JPA22358);
		simple_cond_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred97_JPA2

	// $ANTLR start synpred98_JPA2
	public final void synpred98_JPA2_fragment() throws RecognitionException {
		// JPA2.g:267:7: ( comparison_expression )
		// JPA2.g:267:7: comparison_expression
		{
		pushFollow(FOLLOW_comparison_expression_in_synpred98_JPA22395);
		comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred98_JPA2

	// $ANTLR start synpred99_JPA2
	public final void synpred99_JPA2_fragment() throws RecognitionException {
		// JPA2.g:268:7: ( between_expression )
		// JPA2.g:268:7: between_expression
		{
		pushFollow(FOLLOW_between_expression_in_synpred99_JPA22403);
		between_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred99_JPA2

	// $ANTLR start synpred100_JPA2
	public final void synpred100_JPA2_fragment() throws RecognitionException {
		// JPA2.g:269:7: ( in_expression )
		// JPA2.g:269:7: in_expression
		{
		pushFollow(FOLLOW_in_expression_in_synpred100_JPA22411);
		in_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred100_JPA2

	// $ANTLR start synpred101_JPA2
	public final void synpred101_JPA2_fragment() throws RecognitionException {
		// JPA2.g:270:7: ( like_expression )
		// JPA2.g:270:7: like_expression
		{
		pushFollow(FOLLOW_like_expression_in_synpred101_JPA22419);
		like_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred101_JPA2

	// $ANTLR start synpred102_JPA2
	public final void synpred102_JPA2_fragment() throws RecognitionException {
		// JPA2.g:271:7: ( null_comparison_expression )
		// JPA2.g:271:7: null_comparison_expression
		{
		pushFollow(FOLLOW_null_comparison_expression_in_synpred102_JPA22427);
		null_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred102_JPA2

	// $ANTLR start synpred103_JPA2
	public final void synpred103_JPA2_fragment() throws RecognitionException {
		// JPA2.g:272:7: ( empty_collection_comparison_expression )
		// JPA2.g:272:7: empty_collection_comparison_expression
		{
		pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred103_JPA22435);
		empty_collection_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred103_JPA2

	// $ANTLR start synpred104_JPA2
	public final void synpred104_JPA2_fragment() throws RecognitionException {
		// JPA2.g:273:7: ( collection_member_expression )
		// JPA2.g:273:7: collection_member_expression
		{
		pushFollow(FOLLOW_collection_member_expression_in_synpred104_JPA22443);
		collection_member_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred104_JPA2

	// $ANTLR start synpred128_JPA2
	public final void synpred128_JPA2_fragment() throws RecognitionException {
		// JPA2.g:302:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
		// JPA2.g:302:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred128_JPA22731);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:302:29: ( 'NOT' )?
		int alt153=2;
		int LA153_0 = input.LA(1);
		if ( (LA153_0==NOT) ) {
			alt153=1;
		}
		switch (alt153) {
			case 1 :
				// JPA2.g:302:30: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred128_JPA22734); if (state.failed) return;

				}
				break;

		}

		match(input,82,FOLLOW_82_in_synpred128_JPA22738); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred128_JPA22740);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred128_JPA22742); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred128_JPA22744);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred128_JPA2

	// $ANTLR start synpred130_JPA2
	public final void synpred130_JPA2_fragment() throws RecognitionException {
		// JPA2.g:303:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
		// JPA2.g:303:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred130_JPA22752);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:303:25: ( 'NOT' )?
		int alt154=2;
		int LA154_0 = input.LA(1);
		if ( (LA154_0==NOT) ) {
			alt154=1;
		}
		switch (alt154) {
			case 1 :
				// JPA2.g:303:26: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred130_JPA22755); if (state.failed) return;

				}
				break;

		}

		match(input,82,FOLLOW_82_in_synpred130_JPA22759); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred130_JPA22761);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred130_JPA22763); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred130_JPA22765);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred130_JPA2

	// $ANTLR start synpred143_JPA2
	public final void synpred143_JPA2_fragment() throws RecognitionException {
		// JPA2.g:319:42: ( string_expression )
		// JPA2.g:319:42: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred143_JPA22954);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred143_JPA2

	// $ANTLR start synpred144_JPA2
	public final void synpred144_JPA2_fragment() throws RecognitionException {
		// JPA2.g:319:62: ( pattern_value )
		// JPA2.g:319:62: pattern_value
		{
		pushFollow(FOLLOW_pattern_value_in_synpred144_JPA22958);
		pattern_value();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred144_JPA2

	// $ANTLR start synpred146_JPA2
	public final void synpred146_JPA2_fragment() throws RecognitionException {
		// JPA2.g:321:8: ( path_expression )
		// JPA2.g:321:8: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred146_JPA22981);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred146_JPA2

	// $ANTLR start synpred154_JPA2
	public final void synpred154_JPA2_fragment() throws RecognitionException {
		// JPA2.g:331:7: ( identification_variable )
		// JPA2.g:331:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred154_JPA23083);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred154_JPA2

	// $ANTLR start synpred161_JPA2
	public final void synpred161_JPA2_fragment() throws RecognitionException {
		// JPA2.g:339:7: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) )
		// JPA2.g:339:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_string_expression_in_synpred161_JPA23152);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:339:25: ( comparison_operator | 'REGEXP' )
		int alt156=2;
		int LA156_0 = input.LA(1);
		if ( ((LA156_0 >= 66 && LA156_0 <= 71)) ) {
			alt156=1;
		}
		else if ( (LA156_0==125) ) {
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
				// JPA2.g:339:26: comparison_operator
				{
				pushFollow(FOLLOW_comparison_operator_in_synpred161_JPA23155);
				comparison_operator();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:339:48: 'REGEXP'
				{
				match(input,125,FOLLOW_125_in_synpred161_JPA23159); if (state.failed) return;

				}
				break;

		}

		// JPA2.g:339:58: ( string_expression | all_or_any_expression )
		int alt157=2;
		int LA157_0 = input.LA(1);
		if ( (LA157_0==AVG||LA157_0==COUNT||LA157_0==GROUP||(LA157_0 >= LOWER && LA157_0 <= NAMED_PARAMETER)||(LA157_0 >= STRING_LITERAL && LA157_0 <= SUM)||LA157_0==WORD||LA157_0==58||LA157_0==72||LA157_0==77||(LA157_0 >= 84 && LA157_0 <= 87)||LA157_0==100||LA157_0==102||LA157_0==118||LA157_0==131||LA157_0==135||LA157_0==138) ) {
			alt157=1;
		}
		else if ( ((LA157_0 >= 80 && LA157_0 <= 81)||LA157_0==129) ) {
			alt157=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 157, 0, input);
			throw nvae;
		}

		switch (alt157) {
			case 1 :
				// JPA2.g:339:59: string_expression
				{
				pushFollow(FOLLOW_string_expression_in_synpred161_JPA23163);
				string_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:339:79: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred161_JPA23167);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred161_JPA2

	// $ANTLR start synpred164_JPA2
	public final void synpred164_JPA2_fragment() throws RecognitionException {
		// JPA2.g:340:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
		// JPA2.g:340:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred164_JPA23176);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 68 && input.LA(1) <= 69) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:340:39: ( boolean_expression | all_or_any_expression )
		int alt158=2;
		int LA158_0 = input.LA(1);
		if ( (LA158_0==GROUP||LA158_0==LPAREN||LA158_0==NAMED_PARAMETER||LA158_0==WORD||LA158_0==58||LA158_0==72||LA158_0==77||(LA158_0 >= 84 && LA158_0 <= 86)||LA158_0==100||LA158_0==102||LA158_0==118||(LA158_0 >= 145 && LA158_0 <= 146)) ) {
			alt158=1;
		}
		else if ( ((LA158_0 >= 80 && LA158_0 <= 81)||LA158_0==129) ) {
			alt158=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 158, 0, input);
			throw nvae;
		}

		switch (alt158) {
			case 1 :
				// JPA2.g:340:40: boolean_expression
				{
				pushFollow(FOLLOW_boolean_expression_in_synpred164_JPA23187);
				boolean_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:340:61: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred164_JPA23191);
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
		// JPA2.g:341:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
		// JPA2.g:341:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_enum_expression_in_synpred167_JPA23200);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 68 && input.LA(1) <= 69) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:341:34: ( enum_expression | all_or_any_expression )
		int alt159=2;
		int LA159_0 = input.LA(1);
		if ( (LA159_0==GROUP||LA159_0==LPAREN||LA159_0==NAMED_PARAMETER||LA159_0==WORD||LA159_0==58||LA159_0==72||LA159_0==84||LA159_0==86||LA159_0==118) ) {
			alt159=1;
		}
		else if ( ((LA159_0 >= 80 && LA159_0 <= 81)||LA159_0==129) ) {
			alt159=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 159, 0, input);
			throw nvae;
		}

		switch (alt159) {
			case 1 :
				// JPA2.g:341:35: enum_expression
				{
				pushFollow(FOLLOW_enum_expression_in_synpred167_JPA23209);
				enum_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:341:53: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred167_JPA23213);
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
		// JPA2.g:342:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
		// JPA2.g:342:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred169_JPA23222);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred169_JPA23224);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:342:47: ( datetime_expression | all_or_any_expression )
		int alt160=2;
		int LA160_0 = input.LA(1);
		if ( (LA160_0==AVG||LA160_0==COUNT||LA160_0==GROUP||(LA160_0 >= LPAREN && LA160_0 <= NAMED_PARAMETER)||LA160_0==SUM||LA160_0==WORD||LA160_0==58||LA160_0==72||LA160_0==77||(LA160_0 >= 84 && LA160_0 <= 86)||(LA160_0 >= 88 && LA160_0 <= 90)||LA160_0==100||LA160_0==102||LA160_0==118) ) {
			alt160=1;
		}
		else if ( ((LA160_0 >= 80 && LA160_0 <= 81)||LA160_0==129) ) {
			alt160=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 160, 0, input);
			throw nvae;
		}

		switch (alt160) {
			case 1 :
				// JPA2.g:342:48: datetime_expression
				{
				pushFollow(FOLLOW_datetime_expression_in_synpred169_JPA23227);
				datetime_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:342:70: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred169_JPA23231);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred169_JPA2

	// $ANTLR start synpred172_JPA2
	public final void synpred172_JPA2_fragment() throws RecognitionException {
		// JPA2.g:343:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
		// JPA2.g:343:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_entity_expression_in_synpred172_JPA23240);
		entity_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 68 && input.LA(1) <= 69) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:343:38: ( entity_expression | all_or_any_expression )
		int alt161=2;
		int LA161_0 = input.LA(1);
		if ( (LA161_0==GROUP||LA161_0==NAMED_PARAMETER||LA161_0==WORD||LA161_0==58||LA161_0==72) ) {
			alt161=1;
		}
		else if ( ((LA161_0 >= 80 && LA161_0 <= 81)||LA161_0==129) ) {
			alt161=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 161, 0, input);
			throw nvae;
		}

		switch (alt161) {
			case 1 :
				// JPA2.g:343:39: entity_expression
				{
				pushFollow(FOLLOW_entity_expression_in_synpred172_JPA23251);
				entity_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:343:59: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred172_JPA23255);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred172_JPA2

	// $ANTLR start synpred174_JPA2
	public final void synpred174_JPA2_fragment() throws RecognitionException {
		// JPA2.g:344:7: ( entity_type_expression ( '=' | '<>' ) entity_type_expression )
		// JPA2.g:344:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
		{
		pushFollow(FOLLOW_entity_type_expression_in_synpred174_JPA23264);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 68 && input.LA(1) <= 69) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		pushFollow(FOLLOW_entity_type_expression_in_synpred174_JPA23274);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred174_JPA2

	// $ANTLR start synpred183_JPA2
	public final void synpred183_JPA2_fragment() throws RecognitionException {
		// JPA2.g:355:7: ( arithmetic_term ( ( '+' | '-' ) arithmetic_term )+ )
		// JPA2.g:355:7: arithmetic_term ( ( '+' | '-' ) arithmetic_term )+
		{
		pushFollow(FOLLOW_arithmetic_term_in_synpred183_JPA23355);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:355:23: ( ( '+' | '-' ) arithmetic_term )+
		int cnt162=0;
		loop162:
		while (true) {
			int alt162=2;
			int LA162_0 = input.LA(1);
			if ( (LA162_0==60||LA162_0==62) ) {
				alt162=1;
			}

			switch (alt162) {
			case 1 :
				// JPA2.g:355:24: ( '+' | '-' ) arithmetic_term
				{
				if ( input.LA(1)==60||input.LA(1)==62 ) {
					input.consume();
					state.errorRecovery=false;
					state.failed=false;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return;}
					MismatchedSetException mse = new MismatchedSetException(null,input);
					throw mse;
				}
				pushFollow(FOLLOW_arithmetic_term_in_synpred183_JPA23366);
				arithmetic_term();
				state._fsp--;
				if (state.failed) return;

				}
				break;

			default :
				if ( cnt162 >= 1 ) break loop162;
				if (state.backtracking>0) {state.failed=true; return;}
				EarlyExitException eee = new EarlyExitException(162, input);
				throw eee;
			}
			cnt162++;
		}

		}

	}
	// $ANTLR end synpred183_JPA2

	// $ANTLR start synpred186_JPA2
	public final void synpred186_JPA2_fragment() throws RecognitionException {
		// JPA2.g:358:7: ( arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+ )
		// JPA2.g:358:7: arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+
		{
		pushFollow(FOLLOW_arithmetic_factor_in_synpred186_JPA23387);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:358:25: ( ( '*' | '/' ) arithmetic_factor )+
		int cnt163=0;
		loop163:
		while (true) {
			int alt163=2;
			int LA163_0 = input.LA(1);
			if ( (LA163_0==59||LA163_0==64) ) {
				alt163=1;
			}

			switch (alt163) {
			case 1 :
				// JPA2.g:358:26: ( '*' | '/' ) arithmetic_factor
				{
				if ( input.LA(1)==59||input.LA(1)==64 ) {
					input.consume();
					state.errorRecovery=false;
					state.failed=false;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return;}
					MismatchedSetException mse = new MismatchedSetException(null,input);
					throw mse;
				}
				pushFollow(FOLLOW_arithmetic_factor_in_synpred186_JPA23399);
				arithmetic_factor();
				state._fsp--;
				if (state.failed) return;

				}
				break;

			default :
				if ( cnt163 >= 1 ) break loop163;
				if (state.backtracking>0) {state.failed=true; return;}
				EarlyExitException eee = new EarlyExitException(163, input);
				throw eee;
			}
			cnt163++;
		}

		}

	}
	// $ANTLR end synpred186_JPA2

	// $ANTLR start synpred190_JPA2
	public final void synpred190_JPA2_fragment() throws RecognitionException {
		// JPA2.g:364:7: ( decimal_literal )
		// JPA2.g:364:7: decimal_literal
		{
		pushFollow(FOLLOW_decimal_literal_in_synpred190_JPA23451);
		decimal_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred190_JPA2

	// $ANTLR start synpred191_JPA2
	public final void synpred191_JPA2_fragment() throws RecognitionException {
		// JPA2.g:365:7: ( numeric_literal )
		// JPA2.g:365:7: numeric_literal
		{
		pushFollow(FOLLOW_numeric_literal_in_synpred191_JPA23459);
		numeric_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred191_JPA2

	// $ANTLR start synpred192_JPA2
	public final void synpred192_JPA2_fragment() throws RecognitionException {
		// JPA2.g:366:7: ( '(' arithmetic_expression ')' )
		// JPA2.g:366:7: '(' arithmetic_expression ')'
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred192_JPA23467); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred192_JPA23468);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred192_JPA23469); if (state.failed) return;

		}

	}
	// $ANTLR end synpred192_JPA2

	// $ANTLR start synpred195_JPA2
	public final void synpred195_JPA2_fragment() throws RecognitionException {
		// JPA2.g:369:7: ( aggregate_expression )
		// JPA2.g:369:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred195_JPA23493);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred195_JPA2

	// $ANTLR start synpred197_JPA2
	public final void synpred197_JPA2_fragment() throws RecognitionException {
		// JPA2.g:371:7: ( function_invocation )
		// JPA2.g:371:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred197_JPA23509);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred197_JPA2

	// $ANTLR start synpred203_JPA2
	public final void synpred203_JPA2_fragment() throws RecognitionException {
		// JPA2.g:379:7: ( aggregate_expression )
		// JPA2.g:379:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred203_JPA23568);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred203_JPA2

	// $ANTLR start synpred205_JPA2
	public final void synpred205_JPA2_fragment() throws RecognitionException {
		// JPA2.g:381:7: ( function_invocation )
		// JPA2.g:381:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred205_JPA23584);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred205_JPA2

	// $ANTLR start synpred207_JPA2
	public final void synpred207_JPA2_fragment() throws RecognitionException {
		// JPA2.g:385:7: ( path_expression )
		// JPA2.g:385:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred207_JPA23611);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred207_JPA2

	// $ANTLR start synpred210_JPA2
	public final void synpred210_JPA2_fragment() throws RecognitionException {
		// JPA2.g:388:7: ( aggregate_expression )
		// JPA2.g:388:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred210_JPA23635);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred210_JPA2

	// $ANTLR start synpred212_JPA2
	public final void synpred212_JPA2_fragment() throws RecognitionException {
		// JPA2.g:390:7: ( function_invocation )
		// JPA2.g:390:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred212_JPA23651);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred212_JPA2

	// $ANTLR start synpred214_JPA2
	public final void synpred214_JPA2_fragment() throws RecognitionException {
		// JPA2.g:392:7: ( date_time_timestamp_literal )
		// JPA2.g:392:7: date_time_timestamp_literal
		{
		pushFollow(FOLLOW_date_time_timestamp_literal_in_synpred214_JPA23667);
		date_time_timestamp_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred214_JPA2

	// $ANTLR start synpred252_JPA2
	public final void synpred252_JPA2_fragment() throws RecognitionException {
		// JPA2.g:443:7: ( literal )
		// JPA2.g:443:7: literal
		{
		pushFollow(FOLLOW_literal_in_synpred252_JPA24122);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred252_JPA2

	// $ANTLR start synpred253_JPA2
	public final void synpred253_JPA2_fragment() throws RecognitionException {
		// JPA2.g:444:7: ( path_expression )
		// JPA2.g:444:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred253_JPA24130);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred253_JPA2

	// $ANTLR start synpred254_JPA2
	public final void synpred254_JPA2_fragment() throws RecognitionException {
		// JPA2.g:445:7: ( input_parameter )
		// JPA2.g:445:7: input_parameter
		{
		pushFollow(FOLLOW_input_parameter_in_synpred254_JPA24138);
		input_parameter();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred254_JPA2

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
	public final boolean synpred203_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred203_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred143_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred143_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred212_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred212_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred144_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred144_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred210_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred210_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred174_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred174_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred128_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred128_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred253_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred253_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred146_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred146_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred154_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred154_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred254_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred254_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred183_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred183_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred172_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred172_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred161_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred161_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred214_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred214_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred191_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred191_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred252_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred252_JPA2_fragment(); // can never throw exception
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
		"\30\uffff";
	static final String DFA41_eofS =
		"\30\uffff";
	static final String DFA41_minS =
		"\1\7\1\30\2\uffff\2\7\1\40\1\uffff\1\5\15\40\1\0\1\5";
	static final String DFA41_maxS =
		"\1\146\1\30\2\uffff\2\u0082\1\77\1\uffff\1\u0090\15\100\1\0\1\u0090";
	static final String DFA41_acceptS =
		"\2\uffff\1\1\1\3\3\uffff\1\2\20\uffff";
	static final String DFA41_specialS =
		"\26\uffff\1\0\1\uffff}>";
	static final String[] DFA41_transitionS = {
			"\1\2\2\uffff\1\1\16\uffff\2\2\11\uffff\1\2\101\uffff\1\3",
			"\1\4",
			"",
			"",
			"\1\2\2\uffff\1\2\1\uffff\1\5\2\uffff\1\6\3\uffff\1\2\4\uffff\4\2\10"+
			"\uffff\1\2\23\uffff\1\6\1\uffff\1\2\1\uffff\1\2\1\uffff\1\2\2\uffff\1"+
			"\2\6\uffff\1\2\4\uffff\1\2\1\uffff\1\2\4\uffff\3\2\15\uffff\1\2\1\uffff"+
			"\1\2\1\uffff\1\2\3\uffff\1\2\1\uffff\1\2\2\uffff\1\2\4\uffff\1\2\11\uffff"+
			"\1\2\1\uffff\1\2",
			"\1\2\2\uffff\1\2\4\uffff\1\6\3\uffff\1\2\4\uffff\4\2\10\uffff\1\2\23"+
			"\uffff\1\6\1\uffff\1\2\1\uffff\1\2\1\uffff\1\2\2\uffff\1\2\6\uffff\1"+
			"\2\4\uffff\1\2\1\uffff\1\2\4\uffff\3\2\15\uffff\1\2\1\uffff\1\2\1\uffff"+
			"\1\2\3\uffff\1\2\1\uffff\1\2\2\uffff\1\2\4\uffff\1\2\11\uffff\1\2\1\uffff"+
			"\1\2",
			"\1\7\36\uffff\1\10",
			"",
			"\1\23\1\uffff\1\21\2\uffff\1\22\4\uffff\1\14\11\uffff\1\16\1\17\3\uffff"+
			"\1\15\1\uffff\1\26\3\uffff\1\20\23\uffff\1\11\2\uffff\2\2\1\uffff\1\2"+
			"\1\uffff\1\2\32\uffff\1\25\5\uffff\1\25\3\uffff\1\13\1\uffff\1\25\7\uffff"+
			"\1\24\1\25\1\uffff\1\25\11\uffff\1\25\1\uffff\1\25\1\12\15\uffff\1\25"+
			"\2\uffff\1\25",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\26\32\uffff\2\2\1\uffff\1\2\1\27\1\2",
			"\1\uffff",
			"\1\23\1\uffff\1\21\2\uffff\1\22\4\uffff\1\14\11\uffff\1\16\1\17\3\uffff"+
			"\1\15\1\uffff\1\26\3\uffff\1\20\23\uffff\1\11\2\uffff\2\2\1\uffff\1\2"+
			"\1\uffff\1\2\32\uffff\1\25\5\uffff\1\25\3\uffff\1\13\1\uffff\1\25\7\uffff"+
			"\1\24\1\25\1\uffff\1\25\11\uffff\1\25\1\uffff\1\25\1\12\15\uffff\1\25"+
			"\2\uffff\1\25"
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
			return "183:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			TokenStream input = (TokenStream)_input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA41_22 = input.LA(1);
						 
						int index41_22 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred53_JPA2()) ) {s = 2;}
						else if ( (synpred55_JPA2()) ) {s = 7;}
						 
						input.seek(index41_22);
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

	public static final BitSet FOLLOW_select_statement_in_ql_statement456 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_update_statement_in_ql_statement460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_delete_statement_in_ql_statement464 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_127_in_select_statement479 = new BitSet(new long[]{0x550000180F889480L,0x024A515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_select_clause_in_select_statement481 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_from_clause_in_select_statement483 = new BitSet(new long[]{0x0000000040018002L,0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_where_clause_in_select_statement486 = new BitSet(new long[]{0x0000000040018002L});
	public static final BitSet FOLLOW_groupby_clause_in_select_statement491 = new BitSet(new long[]{0x0000000040010002L});
	public static final BitSet FOLLOW_having_clause_in_select_statement496 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_orderby_clause_in_select_statement501 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_137_in_update_statement559 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_update_clause_in_update_statement561 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_where_clause_in_update_statement564 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_92_in_delete_statement600 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_delete_clause_in_delete_statement602 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_where_clause_in_delete_statement605 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_from_clause643 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause645 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_61_in_from_clause648 = new BitSet(new long[]{0x0100000000020000L});
	public static final BitSet FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause650 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration684 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration693 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration717 = new BitSet(new long[]{0x0000000000340002L});
	public static final BitSet FOLLOW_joined_clause_in_identification_variable_declaration719 = new BitSet(new long[]{0x0000000000340002L});
	public static final BitSet FOLLOW_joined_clause_in_join_section750 = new BitSet(new long[]{0x0000000000340002L});
	public static final BitSet FOLLOW_join_in_joined_clause758 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fetch_join_in_joined_clause762 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_range_variable_declaration774 = new BitSet(new long[]{0x0100000000008020L});
	public static final BitSet FOLLOW_AS_in_range_variable_declaration777 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration781 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_join810 = new BitSet(new long[]{0x0100000000008000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_join_association_path_expression_in_join812 = new BitSet(new long[]{0x0100000000008020L});
	public static final BitSet FOLLOW_AS_in_join815 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_identification_variable_in_join819 = new BitSet(new long[]{0x0000000000000002L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_join822 = new BitSet(new long[]{0x550000181F888480L,0x0042515807F0FF02L,0x00000000000605CDL});
	public static final BitSet FOLLOW_conditional_expression_in_join824 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_fetch_join858 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_FETCH_in_fetch_join860 = new BitSet(new long[]{0x0100000000008000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join862 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFT_in_join_spec876 = new BitSet(new long[]{0x0000000080100000L});
	public static final BitSet FOLLOW_OUTER_in_join_spec880 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_INNER_in_join_spec886 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_JOIN_in_join_spec891 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression905 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_join_association_path_expression907 = new BitSet(new long[]{0x01000010460084A2L,0xD00580A208000000L,0x0000000000012000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression910 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_join_association_path_expression911 = new BitSet(new long[]{0x01000010460084A2L,0xD00580A208000000L,0x0000000000012000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression915 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_134_in_join_association_path_expression950 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression952 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_join_association_path_expression954 = new BitSet(new long[]{0x01000010460084A0L,0xD00580A208000000L,0x0000000000012000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression957 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_join_association_path_expression958 = new BitSet(new long[]{0x01000010460084A0L,0xD00580A208000000L,0x0000000000012000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression962 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_AS_in_join_association_path_expression965 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_subtype_in_join_association_path_expression967 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_join_association_path_expression969 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_join_association_path_expression1002 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_collection_member_declaration1015 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration1016 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_declaration1018 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration1020 = new BitSet(new long[]{0x0100000000008020L});
	public static final BitSet FOLLOW_AS_in_collection_member_declaration1023 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration1027 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_qualified_identification_variable1056 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_96_in_qualified_identification_variable1064 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_identification_variable_in_qualified_identification_variable1065 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_qualified_identification_variable1066 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_106_in_map_field_identification_variable1073 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1074 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_140_in_map_field_identification_variable1079 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1080 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1081 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_path_expression1095 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_path_expression1097 = new BitSet(new long[]{0x01000010460084A2L,0xD00580A208000000L,0x0000000000012000L});
	public static final BitSet FOLLOW_field_in_path_expression1100 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_path_expression1101 = new BitSet(new long[]{0x01000010460084A2L,0xD00580A208000000L,0x0000000000012000L});
	public static final BitSet FOLLOW_field_in_path_expression1105 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_general_identification_variable1144 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_general_identification_variable1152 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_update_clause1165 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_SET_in_update_clause1167 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1169 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_61_in_update_clause1172 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1174 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_update_item1216 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_69_in_update_item1218 = new BitSet(new long[]{0x550000180F888480L,0x0062515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_new_value_in_update_item1220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_new_value1231 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_new_value1239 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_117_in_new_value1247 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_delete_clause1261 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_delete_clause1263 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_select_clause1291 = new BitSet(new long[]{0x550000180F888480L,0x024A515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_select_item_in_select_clause1295 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_61_in_select_clause1298 = new BitSet(new long[]{0x550000180F888480L,0x024A515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_select_item_in_select_clause1300 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_select_expression_in_select_item1343 = new BitSet(new long[]{0x0100000000000022L});
	public static final BitSet FOLLOW_AS_in_select_item1347 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_result_variable_in_select_item1351 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_select_expression1364 = new BitSet(new long[]{0x5800000000000002L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_select_expression1367 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1383 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1411 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_select_expression1419 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_121_in_select_expression1427 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_select_expression1429 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1430 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_select_expression1431 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_constructor_expression_in_select_expression1439 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_115_in_constructor_expression1450 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_constructor_name_in_constructor_expression1452 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_constructor_expression1454 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1456 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_constructor_expression1459 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1461 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_constructor_expression1465 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_constructor_item1476 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_constructor_item1484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_constructor_item1492 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_constructor_item1500 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1511 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1513 = new BitSet(new long[]{0x550000100F089480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1515 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_aggregate_expression1519 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1520 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_aggregate_expression1554 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1556 = new BitSet(new long[]{0x0100000000009000L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1558 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_count_argument_in_aggregate_expression1562 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1564 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_aggregate_expression1599 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_count_argument1636 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_count_argument1640 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_143_in_where_clause1653 = new BitSet(new long[]{0x550000181F888480L,0x0042515807F0FF02L,0x00000000000605CDL});
	public static final BitSet FOLLOW_conditional_expression_in_where_clause1655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_groupby_clause1677 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_BY_in_groupby_clause1679 = new BitSet(new long[]{0x0100000000008000L,0x0000001000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1681 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_61_in_groupby_clause1684 = new BitSet(new long[]{0x0100000000008000L,0x0000001000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1686 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_groupby_item1720 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_groupby_item1724 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extract_function_in_groupby_item1728 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HAVING_in_having_clause1739 = new BitSet(new long[]{0x550000181F888480L,0x0042515807F0FF02L,0x00000000000605CDL});
	public static final BitSet FOLLOW_conditional_expression_in_having_clause1741 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderby_clause1752 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_BY_in_orderby_clause1754 = new BitSet(new long[]{0x550000180F888480L,0x0042555007F0A102L,0x000000000006158DL});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1756 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_61_in_orderby_clause1759 = new BitSet(new long[]{0x550000180F888480L,0x0042555007F0A102L,0x000000000006158DL});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1761 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1795 = new BitSet(new long[]{0x0000000000000842L,0x0180000000000000L});
	public static final BitSet FOLLOW_sort_in_orderby_item1797 = new BitSet(new long[]{0x0000000000000002L,0x0180000000000000L});
	public static final BitSet FOLLOW_sortNulls_in_orderby_item1800 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_orderby_variable1835 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_orderby_variable1839 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_orderby_variable1843 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_orderby_variable1847 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_orderby_variable1851 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_subquery1898 = new BitSet(new long[]{0x0000000000000000L,0x8000000000000000L});
	public static final BitSet FOLLOW_127_in_subquery1900 = new BitSet(new long[]{0x550000180F889480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_simple_select_clause_in_subquery1902 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_subquery_from_clause_in_subquery1904 = new BitSet(new long[]{0x0000000100018000L,0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_where_clause_in_subquery1907 = new BitSet(new long[]{0x0000000100018000L});
	public static final BitSet FOLLOW_groupby_clause_in_subquery1912 = new BitSet(new long[]{0x0000000100010000L});
	public static final BitSet FOLLOW_having_clause_in_subquery1917 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_subquery1923 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_subquery_from_clause1973 = new BitSet(new long[]{0x0100000000020000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1975 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_61_in_subquery_from_clause1978 = new BitSet(new long[]{0x0100000000020000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1980 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration2018 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration2026 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_AS_in_subselect_identification_variable_declaration2028 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration2030 = new BitSet(new long[]{0x0000000000340002L});
	public static final BitSet FOLLOW_join_in_subselect_identification_variable_declaration2033 = new BitSet(new long[]{0x0000000000340002L});
	public static final BitSet FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration2043 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression2054 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_derived_path_expression2055 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_path_expression2056 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression2064 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_derived_path_expression2065 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_path_expression2066 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_derived_path_in_general_derived_path2077 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_treated_derived_path_in_general_derived_path2085 = new BitSet(new long[]{0x8000000000000002L});
	public static final BitSet FOLLOW_63_in_general_derived_path2087 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_general_derived_path2088 = new BitSet(new long[]{0x8000000000000002L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_simple_derived_path2106 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_134_in_treated_derived_path2123 = new BitSet(new long[]{0x0100000000000000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_general_derived_path_in_treated_derived_path2124 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_AS_in_treated_derived_path2126 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_subtype_in_treated_derived_path2128 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_treated_derived_path2130 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_derived_collection_member_declaration2141 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2143 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_derived_collection_member_declaration2144 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2146 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_derived_collection_member_declaration2148 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_collection_member_declaration2151 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause2164 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause2168 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_simple_select_expression2208 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_select_expression2216 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression2224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_select_expression2232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_scalar_expression2243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_scalar_expression2251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_scalar_expression2259 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_scalar_expression2267 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_scalar_expression2275 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_scalar_expression2283 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_scalar_expression2291 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2303 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_OR_in_conditional_expression2307 = new BitSet(new long[]{0x550000181F888480L,0x0042515807F0FF02L,0x00000000000605CDL});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2309 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2323 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_conditional_term2327 = new BitSet(new long[]{0x550000181F888480L,0x0042515807F0FF02L,0x00000000000605CDL});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2329 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_NOT_in_conditional_factor2343 = new BitSet(new long[]{0x550000181F888480L,0x0042515807F0FF02L,0x00000000000605CDL});
	public static final BitSet FOLLOW_conditional_primary_in_conditional_factor2347 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_conditional_primary2358 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_conditional_primary2382 = new BitSet(new long[]{0x550000181F888480L,0x0042515807F0FF02L,0x00000000000605CDL});
	public static final BitSet FOLLOW_conditional_expression_in_conditional_primary2383 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_conditional_primary2384 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression2395 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_simple_cond_expression2403 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_simple_cond_expression2411 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_simple_cond_expression2419 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression2427 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2435 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression2443 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression2451 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression2459 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2472 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2480 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2488 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2496 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2504 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_73_in_date_between_macro_expression2516 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2518 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2520 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_date_between_macro_expression2522 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
	public static final BitSet FOLLOW_116_in_date_between_macro_expression2524 = new BitSet(new long[]{0x7000000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2527 = new BitSet(new long[]{0x0000000000080000L,0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2535 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_date_between_macro_expression2539 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
	public static final BitSet FOLLOW_116_in_date_between_macro_expression2541 = new BitSet(new long[]{0x7000000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2544 = new BitSet(new long[]{0x0000000000080000L,0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2552 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_date_between_macro_expression2556 = new BitSet(new long[]{0x0000000000000000L,0x4005008008000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2558 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_date_between_macro_expression2582 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_139_in_date_between_macro_expression2584 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2588 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_date_before_macro_expression2600 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2602 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2604 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_date_before_macro_expression2606 = new BitSet(new long[]{0x0500000008008000L,0x0000000000000100L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2609 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2613 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_date_before_macro_expression2617 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_139_in_date_before_macro_expression2619 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2623 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_74_in_date_after_macro_expression2635 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2637 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2639 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_date_after_macro_expression2641 = new BitSet(new long[]{0x0500000008008000L,0x0000000000000100L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2644 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2648 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_date_after_macro_expression2652 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_139_in_date_after_macro_expression2654 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2658 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_76_in_date_equals_macro_expression2670 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2672 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2674 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_date_equals_macro_expression2676 = new BitSet(new long[]{0x0500000008008000L,0x0000000000000100L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2679 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2683 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_date_equals_macro_expression2687 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_139_in_date_equals_macro_expression2689 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2693 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_78_in_date_today_macro_expression2705 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2707 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2709 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_date_today_macro_expression2712 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_139_in_date_today_macro_expression2714 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2718 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2731 = new BitSet(new long[]{0x0000000010000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2734 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_between_expression2738 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2740 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2742 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2744 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2752 = new BitSet(new long[]{0x0000000010000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2755 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_between_expression2759 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2761 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2763 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2765 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2773 = new BitSet(new long[]{0x0000000010000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2776 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_between_expression2780 = new BitSet(new long[]{0x050000100F008480L,0x0040005007702100L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2782 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2784 = new BitSet(new long[]{0x050000100F008480L,0x0040005007702100L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2786 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2798 = new BitSet(new long[]{0x0000000010020000L});
	public static final BitSet FOLLOW_type_discriminator_in_in_expression2802 = new BitSet(new long[]{0x0000000010020000L});
	public static final BitSet FOLLOW_identification_variable_in_in_expression2806 = new BitSet(new long[]{0x0000000010020000L});
	public static final BitSet FOLLOW_NOT_in_in_expression2810 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_IN_in_in_expression2814 = new BitSet(new long[]{0x0400000009000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2830 = new BitSet(new long[]{0x0400000808080000L,0x0000000000002102L});
	public static final BitSet FOLLOW_in_item_in_in_expression2832 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_in_expression2835 = new BitSet(new long[]{0x0400000808080000L,0x0000000000002102L});
	public static final BitSet FOLLOW_in_item_in_in_expression2837 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2841 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_in_expression2857 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_valued_input_parameter_in_in_expression2873 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2889 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2891 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2893 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_in_item2921 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_in_item2925 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_single_valued_input_parameter_in_in_item2929 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_function_in_in_item2933 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2944 = new BitSet(new long[]{0x0000000010000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_NOT_in_like_expression2947 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_like_expression2951 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2954 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
	public static final BitSet FOLLOW_pattern_value_in_like_expression2958 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
	public static final BitSet FOLLOW_input_parameter_in_like_expression2962 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
	public static final BitSet FOLLOW_98_in_like_expression2965 = new BitSet(new long[]{0x0000002800000000L});
	public static final BitSet FOLLOW_escape_character_in_like_expression2967 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_null_comparison_expression2981 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression2985 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_null_comparison_expression2989 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_null_comparison_expression2992 = new BitSet(new long[]{0x0000000010000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_NOT_in_null_comparison_expression2995 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_117_in_null_comparison_expression2999 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression3010 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_empty_collection_comparison_expression3012 = new BitSet(new long[]{0x0000000010000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_NOT_in_empty_collection_comparison_expression3015 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_94_in_empty_collection_comparison_expression3019 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_or_value_expression_in_collection_member_expression3030 = new BitSet(new long[]{0x0000000010000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_NOT_in_collection_member_expression3034 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_111_in_collection_member_expression3038 = new BitSet(new long[]{0x0100000000008000L,0x0400000000000000L});
	public static final BitSet FOLLOW_122_in_collection_member_expression3041 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_expression3045 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_or_value_expression3056 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression3064 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_entity_or_value_expression3072 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_or_value_expression3083 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_or_value_expression3091 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simple_entity_or_value_expression3099 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_exists_expression3111 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_99_in_exists_expression3115 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_subquery_in_exists_expression3117 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_all_or_any_expression3128 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_subquery_in_all_or_any_expression3141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3152 = new BitSet(new long[]{0x0000000000000000L,0x20000000000000FCL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3155 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F32100L,0x000000000000048AL});
	public static final BitSet FOLLOW_125_in_comparison_expression3159 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F32100L,0x000000000000048AL});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3163 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3167 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3176 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_set_in_comparison_expression3178 = new BitSet(new long[]{0x0500000009008000L,0x0040005000732100L,0x0000000000060002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3191 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3200 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_set_in_comparison_expression3202 = new BitSet(new long[]{0x0500000009008000L,0x0040000000530100L,0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3213 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3222 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000FCL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3224 = new BitSet(new long[]{0x050000100F008480L,0x0040005007732100L,0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3227 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3231 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3240 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_set_in_comparison_expression3242 = new BitSet(new long[]{0x0500000008008000L,0x0000000000030100L,0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3255 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3264 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_set_in_comparison_expression3266 = new BitSet(new long[]{0x0500000008000000L,0x0000000000000100L,0x0000000000000100L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3274 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3282 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000FCL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3284 = new BitSet(new long[]{0x550000100F088480L,0x004251500073A102L,0x0000000000000007L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3287 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3291 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3355 = new BitSet(new long[]{0x5000000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_expression3358 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3366 = new BitSet(new long[]{0x5000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3376 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3387 = new BitSet(new long[]{0x0800000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_arithmetic_term3390 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3399 = new BitSet(new long[]{0x0800000000000002L,0x0000000000000001L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3409 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor3432 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_arithmetic_primary3443 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_decimal_literal_in_arithmetic_primary3451 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary3459 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary3467 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_arithmetic_primary3468 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary3469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary3477 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary3485 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary3493 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_arithmetic_primary3501 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_arithmetic_primary3509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_arithmetic_primary3517 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_arithmetic_primary3525 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_string_expression3536 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_string_expression3544 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_string_expression3552 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_strings_in_string_expression3560 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_string_expression3568 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_string_expression3576 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_string_expression3584 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_string_expression3592 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_string_expression3600 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_datetime_expression3611 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_datetime_expression3619 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_expression3627 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_datetime_expression3635 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_datetime_expression3643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_datetime_expression3651 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_datetime_expression3659 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_datetime_expression3667 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_datetime_expression3675 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_boolean_expression3686 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_literal_in_boolean_expression3694 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_boolean_expression3702 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_boolean_expression3710 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_boolean_expression3718 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_boolean_expression3726 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_boolean_expression3734 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_enum_expression3745 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_literal_in_enum_expression3753 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_enum_expression3761 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_enum_expression3769 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_enum_expression3777 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_expression3788 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3796 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3807 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3815 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_entity_type_expression3826 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_literal_in_entity_type_expression3834 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_entity_type_expression3842 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_136_in_type_discriminator3853 = new BitSet(new long[]{0x0500000008008000L,0x0000040000000100L,0x0000000000001000L});
	public static final BitSet FOLLOW_general_identification_variable_in_type_discriminator3856 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_path_expression_in_type_discriminator3860 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_input_parameter_in_type_discriminator3864 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_type_discriminator3867 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_108_in_functions_returning_numerics3878 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3879 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3880 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_110_in_functions_returning_numerics3888 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3890 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_functions_returning_numerics3891 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3893 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_functions_returning_numerics3895 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3896 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3899 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_79_in_functions_returning_numerics3907 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3908 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3909 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_130_in_functions_returning_numerics3917 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3918 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3919 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_113_in_functions_returning_numerics3927 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3928 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_functions_returning_numerics3929 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3931 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3932 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_128_in_functions_returning_numerics3940 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3941 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3942 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_104_in_functions_returning_numerics3950 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_identification_variable_in_functions_returning_numerics3951 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3952 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_87_in_functions_returning_strings3990 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3991 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_functions_returning_strings3992 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3994 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_functions_returning_strings3997 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3999 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4002 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_functions_returning_strings4010 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4012 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_functions_returning_strings4013 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings4015 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_functions_returning_strings4018 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings4020 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4023 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_135_in_functions_returning_strings4031 = new BitSet(new long[]{0x050000380F808480L,0x0040087000F82100L,0x00000000000004A8L});
	public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings4034 = new BitSet(new long[]{0x0000002000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_trim_character_in_functions_returning_strings4039 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_101_in_functions_returning_strings4043 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4047 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4049 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LOWER_in_functions_returning_strings4057 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings4059 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4060 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4061 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_138_in_functions_returning_strings4069 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4070 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4071 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_function_invocation4101 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_function_name_in_function_invocation4102 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_function_invocation4105 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_function_arg_in_function_invocation4107 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_function_invocation4111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_function_arg4122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_function_arg4130 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_function_arg4138 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_function_arg4146 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_case_expression_in_case_expression4157 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_case_expression_in_case_expression4165 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coalesce_expression_in_case_expression4173 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullif_expression_in_case_expression4181 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_84_in_general_case_expression4192 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4194 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4197 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_93_in_general_case_expression4201 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_general_case_expression4203 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_general_case_expression4205 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_142_in_when_clause4216 = new BitSet(new long[]{0x550000181F888480L,0x0042515807F0FF02L,0x00000000000605CDL});
	public static final BitSet FOLLOW_conditional_expression_in_when_clause4218 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_132_in_when_clause4220 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_when_clause4222 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_84_in_simple_case_expression4233 = new BitSet(new long[]{0x0100000000008000L,0x0000000000000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_case_operand_in_simple_case_expression4235 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4237 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4240 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_93_in_simple_case_expression4244 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_simple_case_expression4246 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_simple_case_expression4248 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_case_operand4259 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_case_operand4267 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_142_in_simple_when_clause4278 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4280 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_132_in_simple_when_clause4282 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_86_in_coalesce_expression4295 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4296 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_coalesce_expression4299 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4301 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_coalesce_expression4304 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_118_in_nullif_expression4315 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4316 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_nullif_expression4318 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4320 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_nullif_expression4321 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_85_in_extension_functions4333 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_function_arg_in_extension_functions4335 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_WORD_in_extension_functions4337 = new BitSet(new long[]{0x0000000101000000L});
	public static final BitSet FOLLOW_LPAREN_in_extension_functions4340 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4341 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_61_in_extension_functions4344 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4346 = new BitSet(new long[]{0x2000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4351 = new BitSet(new long[]{0x0000000101000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extract_function_in_extension_functions4363 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_function_in_extension_functions4371 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_100_in_extract_function4383 = new BitSet(new long[]{0x0000000000000000L,0x5005008208000000L,0x0000000000012000L});
	public static final BitSet FOLLOW_date_part_in_extract_function4385 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_101_in_extract_function4387 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_function_arg_in_extract_function4389 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_extract_function4391 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_77_in_enum_function4403 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_enum_function4405 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_enum_value_literal_in_enum_function4407 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_enum_function4409 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_72_in_input_parameter4476 = new BitSet(new long[]{0x0000000000080000L,0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_input_parameter4478 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter4501 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_58_in_input_parameter4522 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_WORD_in_input_parameter4524 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
	public static final BitSet FOLLOW_147_in_input_parameter4526 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_literal4554 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4566 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_literal4578 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_field4611 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_127_in_field4615 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_field4619 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_field4623 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_field4627 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MAX_in_field4631 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MIN_in_field4635 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SUM_in_field4639 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AVG_in_field4643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_field4647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AS_in_field4651 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_111_in_field4655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_part_in_field4659 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4687 = new BitSet(new long[]{0x8000000000000002L});
	public static final BitSet FOLLOW_63_in_parameter_name4690 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4693 = new BitSet(new long[]{0x8000000000000002L});
	public static final BitSet FOLLOW_TRIM_CHARACTER_in_trim_character4723 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string_literal4734 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_65_in_numeric_literal4746 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal4750 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_decimal_literal4762 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_decimal_literal4764 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_decimal_literal4766 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_object_field4777 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_embeddable_object_field4788 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_collection_valued_field4799 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_name4810 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_subtype4821 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_type_literal4832 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_function_name4843 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_state_field4854 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_result_variable4865 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_superquery_identification_variable4876 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_date_time_timestamp_literal4887 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_pattern_value4898 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_collection_valued_input_parameter4909 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_single_valued_input_parameter4920 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_value_literal4931 = new BitSet(new long[]{0x8000000000000002L});
	public static final BitSet FOLLOW_63_in_enum_value_literal4934 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_WORD_in_enum_value_literal4937 = new BitSet(new long[]{0x8000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred21_JPA2915 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred30_JPA21105 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred33_JPA21231 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_synpred34_JPA21239 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred43_JPA21364 = new BitSet(new long[]{0x5800000000000002L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_synpred43_JPA21367 = new BitSet(new long[]{0x550000180F888480L,0x0042515007F0A102L,0x000000000006058DL});
	public static final BitSet FOLLOW_scalar_expression_in_synpred43_JPA21383 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred44_JPA21393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred45_JPA21411 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred46_JPA21419 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred49_JPA21476 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred50_JPA21484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred51_JPA21492 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_synpred53_JPA21511 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred53_JPA21513 = new BitSet(new long[]{0x550000100F089480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred53_JPA21515 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred53_JPA21519 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred53_JPA21520 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_synpred55_JPA21554 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred55_JPA21556 = new BitSet(new long[]{0x0100000000009000L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred55_JPA21558 = new BitSet(new long[]{0x0100000000008000L});
	public static final BitSet FOLLOW_count_argument_in_synpred55_JPA21562 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred55_JPA21564 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred67_JPA21835 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_synpred68_JPA21839 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_synpred69_JPA21843 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred70_JPA21847 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_synpred80_JPA22054 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_synpred80_JPA22055 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_synpred80_JPA22056 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred85_JPA22208 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred86_JPA22216 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred87_JPA22224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred88_JPA22243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred89_JPA22251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred90_JPA22259 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred91_JPA22267 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred92_JPA22275 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_synpred93_JPA22283 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_synpred96_JPA22343 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_synpred97_JPA22358 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_synpred98_JPA22395 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_synpred99_JPA22403 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_synpred100_JPA22411 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_synpred101_JPA22419 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_synpred102_JPA22427 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred103_JPA22435 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_synpred104_JPA22443 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred128_JPA22731 = new BitSet(new long[]{0x0000000010000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_synpred128_JPA22734 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_synpred128_JPA22738 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred128_JPA22740 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred128_JPA22742 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred128_JPA22744 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred130_JPA22752 = new BitSet(new long[]{0x0000000010000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_synpred130_JPA22755 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_synpred130_JPA22759 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_synpred130_JPA22761 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred130_JPA22763 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F02100L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_synpred130_JPA22765 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred143_JPA22954 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_pattern_value_in_synpred144_JPA22958 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred146_JPA22981 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred154_JPA23083 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred161_JPA23152 = new BitSet(new long[]{0x0000000000000000L,0x20000000000000FCL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred161_JPA23155 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F32100L,0x000000000000048AL});
	public static final BitSet FOLLOW_125_in_synpred161_JPA23159 = new BitSet(new long[]{0x050000180F808480L,0x0040005000F32100L,0x000000000000048AL});
	public static final BitSet FOLLOW_string_expression_in_synpred161_JPA23163 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred161_JPA23167 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred164_JPA23176 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_set_in_synpred164_JPA23178 = new BitSet(new long[]{0x0500000009008000L,0x0040005000732100L,0x0000000000060002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred164_JPA23187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred164_JPA23191 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred167_JPA23200 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_set_in_synpred167_JPA23202 = new BitSet(new long[]{0x0500000009008000L,0x0040000000530100L,0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred167_JPA23209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred167_JPA23213 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred169_JPA23222 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000FCL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred169_JPA23224 = new BitSet(new long[]{0x050000100F008480L,0x0040005007732100L,0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred169_JPA23227 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred169_JPA23231 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred172_JPA23240 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_set_in_synpred172_JPA23242 = new BitSet(new long[]{0x0500000008008000L,0x0000000000030100L,0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred172_JPA23251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred172_JPA23255 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred174_JPA23264 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_set_in_synpred174_JPA23266 = new BitSet(new long[]{0x0500000008000000L,0x0000000000000100L,0x0000000000000100L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred174_JPA23274 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred183_JPA23355 = new BitSet(new long[]{0x5000000000000000L});
	public static final BitSet FOLLOW_set_in_synpred183_JPA23358 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred183_JPA23366 = new BitSet(new long[]{0x5000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred186_JPA23387 = new BitSet(new long[]{0x0800000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_synpred186_JPA23390 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred186_JPA23399 = new BitSet(new long[]{0x0800000000000002L,0x0000000000000001L});
	public static final BitSet FOLLOW_decimal_literal_in_synpred190_JPA23451 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_synpred191_JPA23459 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred192_JPA23467 = new BitSet(new long[]{0x550000100F088480L,0x004251500070A102L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred192_JPA23468 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred192_JPA23469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred195_JPA23493 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred197_JPA23509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred203_JPA23568 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred205_JPA23584 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred207_JPA23611 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred210_JPA23635 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred212_JPA23651 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_synpred214_JPA23667 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred252_JPA24122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred253_JPA24130 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_synpred254_JPA24138 = new BitSet(new long[]{0x0000000000000002L});

	@Override
	public void emitErrorMessage(String msg) {
		//do nothing
	}

	protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
		throw new MismatchedTokenException(ttype, input);
	}
}
