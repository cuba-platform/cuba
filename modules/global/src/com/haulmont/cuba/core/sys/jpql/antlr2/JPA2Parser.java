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
		"'NULLIF('", "'NULLS FIRST'", "'NULLS LAST'", "'OBJECT'", "'OF'", "'ON'", 
		"'QUARTER'", "'REGEXP'", "'SECOND'", "'SELECT'", "'SIZE('", "'SOME'", 
		"'SQRT('", "'SUBSTRING('", "'THEN'", "'TRAILING'", "'TREAT('", "'TRIM('", 
		"'TYPE('", "'UPDATE'", "'UPPER('", "'VALUE('", "'WEEK'", "'WHEN'", "'WHERE'", 
		"'YEAR'", "'false'", "'true'", "'}'"
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
	public static final int T__145=145;
	public static final int T__146=146;
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
		RewriteRuleTokenStream stream_127=new RewriteRuleTokenStream(adaptor,"token 127");
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
			sl=(Token)match(input,127,FOLLOW_127_in_select_statement468); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_127.add(sl);

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
			if ( (LA2_0==142) ) {
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
			// elements: where_clause, groupby_clause, having_clause, select_clause, orderby_clause, from_clause
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
		RewriteRuleTokenStream stream_137=new RewriteRuleTokenStream(adaptor,"token 137");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_update_clause=new RewriteRuleSubtreeStream(adaptor,"rule update_clause");

		try {
			// JPA2.g:89:5: (up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) )
			// JPA2.g:89:7: up= 'UPDATE' update_clause ( where_clause )?
			{
			up=(Token)match(input,137,FOLLOW_137_in_update_statement548); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_137.add(up);

			pushFollow(FOLLOW_update_clause_in_update_statement550);
			update_clause10=update_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_clause.add(update_clause10.getTree());
			// JPA2.g:89:33: ( where_clause )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==142) ) {
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
			// elements: update_clause, where_clause
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
			if ( (LA7_0==142) ) {
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
		RewriteRuleTokenStream stream_123=new RewriteRuleTokenStream(adaptor,"token 123");
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
			if ( (LA15_0==123) ) {
				alt15=1;
			}
			switch (alt15) {
				case 1 :
					// JPA2.g:112:84: 'ON' conditional_expression
					{
					string_literal31=(Token)match(input,123,FOLLOW_123_in_join811); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_123.add(string_literal31);

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
		RewriteRuleTokenStream stream_134=new RewriteRuleTokenStream(adaptor,"token 134");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_subtype=new RewriteRuleSubtreeStream(adaptor,"rule subtype");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:121:6: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name )
			int alt22=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA22_1 = input.LA(2);
				if ( (LA22_1==62) ) {
					alt22=1;
				}
				else if ( (LA22_1==EOF||(LA22_1 >= GROUP && LA22_1 <= HAVING)||LA22_1==INNER||(LA22_1 >= JOIN && LA22_1 <= LEFT)||LA22_1==ORDER||LA22_1==RPAREN||LA22_1==SET||LA22_1==WORD||LA22_1==60||LA22_1==81||LA22_1==105||LA22_1==142) ) {
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
						case 127:
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
						case 81:
							{
							int LA18_11 = input.LA(2);
							if ( (LA18_11==62) ) {
								alt18=1;
							}

							}
							break;
						case 111:
							{
							int LA18_12 = input.LA(2);
							if ( (LA18_12==62) ) {
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
						case 140:
						case 143:
							{
							int LA18_13 = input.LA(2);
							if ( (LA18_13==62) ) {
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
						case 111:
						case 112:
						case 114:
						case 124:
						case 126:
						case 127:
						case 140:
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
							if ( (LA19_4==EOF||(LA19_4 >= GROUP && LA19_4 <= HAVING)||LA19_4==INNER||(LA19_4 >= JOIN && LA19_4 <= LEFT)||LA19_4==ORDER||LA19_4==RPAREN||LA19_4==SET||LA19_4==WORD||LA19_4==60||LA19_4==81||LA19_4==105||LA19_4==142) ) {
								alt19=1;
							}
							}
							break;
						case 81:
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
					string_literal45=(Token)match(input,134,FOLLOW_134_in_join_association_path_expression939); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_134.add(string_literal45);

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
						case 127:
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
						case 81:
							{
							int LA20_11 = input.LA(2);
							if ( (LA20_11==62) ) {
								alt20=1;
							}

							}
							break;
						case 111:
							{
							int LA20_12 = input.LA(2);
							if ( (LA20_12==62) ) {
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
						case 140:
						case 143:
							{
							int LA20_13 = input.LA(2);
							if ( (LA20_13==62) ) {
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
					if ( (LA21_0==AVG||LA21_0==COUNT||LA21_0==GROUP||(LA21_0 >= MAX && LA21_0 <= MIN)||LA21_0==ORDER||LA21_0==SUM||LA21_0==WORD||LA21_0==91||LA21_0==97||LA21_0==101||LA21_0==103||(LA21_0 >= 111 && LA21_0 <= 112)||LA21_0==114||LA21_0==124||(LA21_0 >= 126 && LA21_0 <= 127)||LA21_0==140||LA21_0==143) ) {
						alt21=1;
					}
					else if ( (LA21_0==81) ) {
						int LA21_2 = input.LA(2);
						if ( (LA21_2==81) ) {
							alt21=1;
						}
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
			if ( (LA24_0==106||LA24_0==139) ) {
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
			else if ( (LA25_0==139) ) {
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


					string_literal68=(Token)match(input,139,FOLLOW_139_in_map_field_identification_variable1068); if (state.failed) return retval;
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
				case 127:
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
				case 81:
					{
					int LA26_11 = input.LA(2);
					if ( (LA26_11==62) ) {
						alt26=1;
					}

					}
					break;
				case 111:
					{
					int LA26_12 = input.LA(2);
					if ( (LA26_12==62) ) {
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
				case 140:
				case 143:
					{
					int LA26_13 = input.LA(2);
					if ( (LA26_13==62) ) {
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
				case 124:
				case 126:
				case 127:
				case 140:
				case 143:
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
						case 119:
						case 120:
						case 125:
						case 132:
						case 141:
						case 142:
							{
							alt27=1;
							}
							break;
						case WORD:
							{
							int LA27_9 = input.LA(3);
							if ( (LA27_9==EOF||LA27_9==LPAREN||LA27_9==RPAREN||LA27_9==60||LA27_9==101) ) {
								alt27=1;
							}
							}
							break;
						case IN:
							{
							int LA27_10 = input.LA(3);
							if ( (LA27_10==LPAREN||LA27_10==NAMED_PARAMETER||LA27_10==57||LA27_10==71) ) {
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
					if ( (LA27_4==EOF||(LA27_4 >= AND && LA27_4 <= ASC)||LA27_4==DESC||(LA27_4 >= GROUP && LA27_4 <= INNER)||(LA27_4 >= JOIN && LA27_4 <= LEFT)||(LA27_4 >= NOT && LA27_4 <= ORDER)||LA27_4==RPAREN||LA27_4==SET||LA27_4==WORD||(LA27_4 >= 58 && LA27_4 <= 61)||LA27_4==63||(LA27_4 >= 65 && LA27_4 <= 70)||(LA27_4 >= 81 && LA27_4 <= 82)||LA27_4==93||LA27_4==95||LA27_4==98||LA27_4==101||LA27_4==105||LA27_4==109||LA27_4==111||(LA27_4 >= 119 && LA27_4 <= 120)||LA27_4==125||LA27_4==132||(LA27_4 >= 141 && LA27_4 <= 142)) ) {
						alt27=1;
					}
					}
					break;
				case ORDER:
					{
					int LA27_5 = input.LA(2);
					if ( (LA27_5==EOF||(LA27_5 >= AND && LA27_5 <= ASC)||LA27_5==DESC||(LA27_5 >= GROUP && LA27_5 <= INNER)||(LA27_5 >= JOIN && LA27_5 <= LEFT)||(LA27_5 >= NOT && LA27_5 <= ORDER)||LA27_5==RPAREN||LA27_5==SET||LA27_5==WORD||(LA27_5 >= 58 && LA27_5 <= 61)||LA27_5==63||(LA27_5 >= 65 && LA27_5 <= 70)||(LA27_5 >= 81 && LA27_5 <= 82)||LA27_5==93||LA27_5==95||LA27_5==98||LA27_5==101||LA27_5==105||LA27_5==109||LA27_5==111||(LA27_5 >= 119 && LA27_5 <= 120)||LA27_5==125||LA27_5==132||(LA27_5 >= 141 && LA27_5 <= 142)) ) {
						alt27=1;
					}
					}
					break;
				case 81:
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
						case 119:
						case 120:
						case 125:
						case 132:
						case 141:
						case 142:
							{
							alt27=1;
							}
							break;
						case WORD:
							{
							int LA27_11 = input.LA(3);
							if ( (LA27_11==EOF||LA27_11==LPAREN||LA27_11==RPAREN||LA27_11==60||LA27_11==101) ) {
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
			if ( (LA28_0==GROUP||LA28_0==WORD) ) {
				alt28=1;
			}
			else if ( (LA28_0==106||LA28_0==139) ) {
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
			// elements: 60, update_item, identification_variable_declaration, update_item, SET
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
				while ( stream_60.hasNext()||stream_update_item.hasNext() ) {
					adaptor.addChild(root_1, stream_60.nextNode());
					adaptor.addChild(root_1, stream_update_item.nextTree());
				}
				stream_60.reset();
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
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
			case 71:
				{
				int LA30_3 = input.LA(2);
				if ( (LA30_3==64) ) {
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
			case 57:
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
				if ( (LA30_6==62) ) {
					alt30=1;
				}
				else if ( (LA30_6==EOF||LA30_6==60||LA30_6==142) ) {
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
	// JPA2.g:168:1: select_expression : ( path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )? | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression );
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
			// JPA2.g:169:5: ( path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )? | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression )
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
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
					// JPA2.g:169:7: path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_select_expression1353);
					path_expression97=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression97.getTree());

					// JPA2.g:169:23: ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
					int alt35=2;
					int LA35_0 = input.LA(1);
					if ( ((LA35_0 >= 58 && LA35_0 <= 59)||LA35_0==61||LA35_0==63) ) {
						alt35=1;
					}
					switch (alt35) {
						case 1 :
							// JPA2.g:169:24: ( '+' | '-' | '*' | '/' ) scalar_expression
							{
							set98=input.LT(1);
							if ( (input.LA(1) >= 58 && input.LA(1) <= 59)||input.LA(1)==61||input.LA(1)==63 ) {
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
							pushFollow(FOLLOW_scalar_expression_in_select_expression1372);
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
					// JPA2.g:170:7: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_select_expression1382);
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
					// 170:31: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
					{
						// JPA2.g:170:34: ^( T_SELECTED_ENTITY[$identification_variable.text] )
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
					// JPA2.g:171:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_select_expression1400);
					scalar_expression101=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression101.getTree());

					}
					break;
				case 4 :
					// JPA2.g:172:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_select_expression1408);
					aggregate_expression102=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression102.getTree());

					}
					break;
				case 5 :
					// JPA2.g:173:7: 'OBJECT' '(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal103=(Token)match(input,121,FOLLOW_121_in_select_expression1416); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal103_tree = (Object)adaptor.create(string_literal103);
					adaptor.addChild(root_0, string_literal103_tree);
					}

					char_literal104=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression1418); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal104_tree = (Object)adaptor.create(char_literal104);
					adaptor.addChild(root_0, char_literal104_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_select_expression1419);
					identification_variable105=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable105.getTree());

					char_literal106=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression1420); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal106_tree = (Object)adaptor.create(char_literal106);
					adaptor.addChild(root_0, char_literal106_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:174:7: constructor_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_constructor_expression_in_select_expression1428);
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
	// JPA2.g:175:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
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
			// JPA2.g:176:5: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
			// JPA2.g:176:7: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal108=(Token)match(input,115,FOLLOW_115_in_constructor_expression1439); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal108_tree = (Object)adaptor.create(string_literal108);
			adaptor.addChild(root_0, string_literal108_tree);
			}

			pushFollow(FOLLOW_constructor_name_in_constructor_expression1441);
			constructor_name109=constructor_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name109.getTree());

			char_literal110=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression1443); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal110_tree = (Object)adaptor.create(char_literal110);
			adaptor.addChild(root_0, char_literal110_tree);
			}

			pushFollow(FOLLOW_constructor_item_in_constructor_expression1445);
			constructor_item111=constructor_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item111.getTree());

			// JPA2.g:176:51: ( ',' constructor_item )*
			loop37:
			while (true) {
				int alt37=2;
				int LA37_0 = input.LA(1);
				if ( (LA37_0==60) ) {
					alt37=1;
				}

				switch (alt37) {
				case 1 :
					// JPA2.g:176:52: ',' constructor_item
					{
					char_literal112=(Token)match(input,60,FOLLOW_60_in_constructor_expression1448); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal112_tree = (Object)adaptor.create(char_literal112);
					adaptor.addChild(root_0, char_literal112_tree);
					}

					pushFollow(FOLLOW_constructor_item_in_constructor_expression1450);
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

			char_literal114=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression1454); if (state.failed) return retval;
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
	// JPA2.g:177:1: constructor_item : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.constructor_item_return constructor_item() throws RecognitionException {
		JPA2Parser.constructor_item_return retval = new JPA2Parser.constructor_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression115 =null;
		ParserRuleReturnScope scalar_expression116 =null;
		ParserRuleReturnScope aggregate_expression117 =null;
		ParserRuleReturnScope identification_variable118 =null;


		try {
			// JPA2.g:178:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
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
					// JPA2.g:178:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_constructor_item1465);
					path_expression115=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression115.getTree());

					}
					break;
				case 2 :
					// JPA2.g:179:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_constructor_item1473);
					scalar_expression116=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression116.getTree());

					}
					break;
				case 3 :
					// JPA2.g:180:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_constructor_item1481);
					aggregate_expression117=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression117.getTree());

					}
					break;
				case 4 :
					// JPA2.g:181:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_constructor_item1489);
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
	// JPA2.g:182:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );
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
			// JPA2.g:183:5: ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation )
			int alt41=3;
			alt41 = dfa41.predict(input);
			switch (alt41) {
				case 1 :
					// JPA2.g:183:7: aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')'
					{
					pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1500);
					aggregate_expression_function_name119=aggregate_expression_function_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name119.getTree());
					char_literal120=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1502); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal120);

					// JPA2.g:183:45: ( DISTINCT )?
					int alt39=2;
					int LA39_0 = input.LA(1);
					if ( (LA39_0==DISTINCT) ) {
						alt39=1;
					}
					switch (alt39) {
						case 1 :
							// JPA2.g:183:46: DISTINCT
							{
							DISTINCT121=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1504); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT121);

							}
							break;

					}

					pushFollow(FOLLOW_arithmetic_expression_in_aggregate_expression1508);
					arithmetic_expression122=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_arithmetic_expression.add(arithmetic_expression122.getTree());
					char_literal123=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1509); if (state.failed) return retval; 
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
					// 184:5: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' )
					{
						// JPA2.g:184:8: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' )
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
					// JPA2.g:185:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
					{
					string_literal124=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1543); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COUNT.add(string_literal124);

					char_literal125=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1545); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal125);

					// JPA2.g:185:18: ( DISTINCT )?
					int alt40=2;
					int LA40_0 = input.LA(1);
					if ( (LA40_0==DISTINCT) ) {
						alt40=1;
					}
					switch (alt40) {
						case 1 :
							// JPA2.g:185:19: DISTINCT
							{
							DISTINCT126=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1547); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT126);

							}
							break;

					}

					pushFollow(FOLLOW_count_argument_in_aggregate_expression1551);
					count_argument127=count_argument();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_count_argument.add(count_argument127.getTree());
					char_literal128=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1553); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal128);

					// AST REWRITE
					// elements: LPAREN, RPAREN, count_argument, COUNT, DISTINCT
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


					pushFollow(FOLLOW_function_invocation_in_aggregate_expression1588);
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
	// JPA2.g:188:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
	public final JPA2Parser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
		JPA2Parser.aggregate_expression_function_name_return retval = new JPA2Parser.aggregate_expression_function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set130=null;

		Object set130_tree=null;

		try {
			// JPA2.g:189:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
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
	// JPA2.g:190:1: count_argument : ( identification_variable | path_expression );
	public final JPA2Parser.count_argument_return count_argument() throws RecognitionException {
		JPA2Parser.count_argument_return retval = new JPA2Parser.count_argument_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable131 =null;
		ParserRuleReturnScope path_expression132 =null;


		try {
			// JPA2.g:191:5: ( identification_variable | path_expression )
			int alt42=2;
			int LA42_0 = input.LA(1);
			if ( (LA42_0==GROUP||LA42_0==WORD) ) {
				int LA42_1 = input.LA(2);
				if ( (LA42_1==RPAREN) ) {
					alt42=1;
				}
				else if ( (LA42_1==62) ) {
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
					// JPA2.g:191:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_count_argument1625);
					identification_variable131=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable131.getTree());

					}
					break;
				case 2 :
					// JPA2.g:191:33: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_count_argument1629);
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
	// JPA2.g:192:1: where_clause : wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) ;
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
			// JPA2.g:193:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) )
			// JPA2.g:193:7: wh= 'WHERE' conditional_expression
			{
			wh=(Token)match(input,142,FOLLOW_142_in_where_clause1642); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_142.add(wh);

			pushFollow(FOLLOW_conditional_expression_in_where_clause1644);
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
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");

		try {
			// JPA2.g:195:5: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
			// JPA2.g:195:7: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
			{
			string_literal134=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1666); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_GROUP.add(string_literal134);

			string_literal135=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1668); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal135);

			pushFollow(FOLLOW_groupby_item_in_groupby_clause1670);
			groupby_item136=groupby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item136.getTree());
			// JPA2.g:195:33: ( ',' groupby_item )*
			loop43:
			while (true) {
				int alt43=2;
				int LA43_0 = input.LA(1);
				if ( (LA43_0==60) ) {
					alt43=1;
				}

				switch (alt43) {
				case 1 :
					// JPA2.g:195:34: ',' groupby_item
					{
					char_literal137=(Token)match(input,60,FOLLOW_60_in_groupby_clause1673); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal137);

					pushFollow(FOLLOW_groupby_item_in_groupby_clause1675);
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
			// elements: BY, GROUP, groupby_item
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
	// JPA2.g:197:1: groupby_item : ( path_expression | identification_variable | extract_function );
	public final JPA2Parser.groupby_item_return groupby_item() throws RecognitionException {
		JPA2Parser.groupby_item_return retval = new JPA2Parser.groupby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression139 =null;
		ParserRuleReturnScope identification_variable140 =null;
		ParserRuleReturnScope extract_function141 =null;


		try {
			// JPA2.g:198:5: ( path_expression | identification_variable | extract_function )
			int alt44=3;
			int LA44_0 = input.LA(1);
			if ( (LA44_0==GROUP||LA44_0==WORD) ) {
				int LA44_1 = input.LA(2);
				if ( (LA44_1==62) ) {
					alt44=1;
				}
				else if ( (LA44_1==EOF||LA44_1==HAVING||LA44_1==ORDER||LA44_1==RPAREN||LA44_1==60) ) {
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
					// JPA2.g:198:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_groupby_item1709);
					path_expression139=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression139.getTree());

					}
					break;
				case 2 :
					// JPA2.g:198:25: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_groupby_item1713);
					identification_variable140=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable140.getTree());

					}
					break;
				case 3 :
					// JPA2.g:198:51: extract_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extract_function_in_groupby_item1717);
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
	// JPA2.g:199:1: having_clause : 'HAVING' conditional_expression ;
	public final JPA2Parser.having_clause_return having_clause() throws RecognitionException {
		JPA2Parser.having_clause_return retval = new JPA2Parser.having_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal142=null;
		ParserRuleReturnScope conditional_expression143 =null;

		Object string_literal142_tree=null;

		try {
			// JPA2.g:200:5: ( 'HAVING' conditional_expression )
			// JPA2.g:200:7: 'HAVING' conditional_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal142=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1728); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal142_tree = (Object)adaptor.create(string_literal142);
			adaptor.addChild(root_0, string_literal142_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_having_clause1730);
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
	// JPA2.g:201:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
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
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");

		try {
			// JPA2.g:202:5: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
			// JPA2.g:202:7: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
			{
			string_literal144=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1741); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ORDER.add(string_literal144);

			string_literal145=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1743); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal145);

			pushFollow(FOLLOW_orderby_item_in_orderby_clause1745);
			orderby_item146=orderby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item146.getTree());
			// JPA2.g:202:33: ( ',' orderby_item )*
			loop45:
			while (true) {
				int alt45=2;
				int LA45_0 = input.LA(1);
				if ( (LA45_0==60) ) {
					alt45=1;
				}

				switch (alt45) {
				case 1 :
					// JPA2.g:202:34: ',' orderby_item
					{
					char_literal147=(Token)match(input,60,FOLLOW_60_in_orderby_clause1748); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal147);

					pushFollow(FOLLOW_orderby_item_in_orderby_clause1750);
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
	// JPA2.g:204:1: orderby_item : orderby_variable ( sort )? ( sortNulls )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? ) ;
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
			// JPA2.g:205:5: ( orderby_variable ( sort )? ( sortNulls )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? ) )
			// JPA2.g:205:7: orderby_variable ( sort )? ( sortNulls )?
			{
			pushFollow(FOLLOW_orderby_variable_in_orderby_item1784);
			orderby_variable149=orderby_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable149.getTree());
			// JPA2.g:205:24: ( sort )?
			int alt46=2;
			int LA46_0 = input.LA(1);
			if ( (LA46_0==ASC||LA46_0==DESC) ) {
				alt46=1;
			}
			switch (alt46) {
				case 1 :
					// JPA2.g:205:24: sort
					{
					pushFollow(FOLLOW_sort_in_orderby_item1786);
					sort150=sort();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sort.add(sort150.getTree());
					}
					break;

			}

			// JPA2.g:205:30: ( sortNulls )?
			int alt47=2;
			int LA47_0 = input.LA(1);
			if ( ((LA47_0 >= 119 && LA47_0 <= 120)) ) {
				alt47=1;
			}
			switch (alt47) {
				case 1 :
					// JPA2.g:205:30: sortNulls
					{
					pushFollow(FOLLOW_sortNulls_in_orderby_item1789);
					sortNulls151=sortNulls();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sortNulls.add(sortNulls151.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: sort, orderby_variable, sortNulls
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 206:6: -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? )
			{
				// JPA2.g:206:9: ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);
				adaptor.addChild(root_1, stream_orderby_variable.nextTree());
				// JPA2.g:206:65: ( sort )?
				if ( stream_sort.hasNext() ) {
					adaptor.addChild(root_1, stream_sort.nextTree());
				}
				stream_sort.reset();

				// JPA2.g:206:71: ( sortNulls )?
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
	// JPA2.g:207:1: orderby_variable : ( path_expression | general_identification_variable | result_variable | scalar_expression | aggregate_expression );
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
			// JPA2.g:208:5: ( path_expression | general_identification_variable | result_variable | scalar_expression | aggregate_expression )
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
			case 139:
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
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
					// JPA2.g:208:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_orderby_variable1824);
					path_expression152=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression152.getTree());

					}
					break;
				case 2 :
					// JPA2.g:208:25: general_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_identification_variable_in_orderby_variable1828);
					general_identification_variable153=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable153.getTree());

					}
					break;
				case 3 :
					// JPA2.g:208:59: result_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_result_variable_in_orderby_variable1832);
					result_variable154=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable154.getTree());

					}
					break;
				case 4 :
					// JPA2.g:208:77: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_orderby_variable1836);
					scalar_expression155=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression155.getTree());

					}
					break;
				case 5 :
					// JPA2.g:208:97: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_orderby_variable1840);
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
	// JPA2.g:209:1: sort : ( 'ASC' | 'DESC' ) ;
	public final JPA2Parser.sort_return sort() throws RecognitionException {
		JPA2Parser.sort_return retval = new JPA2Parser.sort_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set157=null;

		Object set157_tree=null;

		try {
			// JPA2.g:210:5: ( ( 'ASC' | 'DESC' ) )
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
	// JPA2.g:211:1: sortNulls : ( 'NULLS FIRST' | 'NULLS LAST' ) ;
	public final JPA2Parser.sortNulls_return sortNulls() throws RecognitionException {
		JPA2Parser.sortNulls_return retval = new JPA2Parser.sortNulls_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set158=null;

		Object set158_tree=null;

		try {
			// JPA2.g:212:5: ( ( 'NULLS FIRST' | 'NULLS LAST' ) )
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
	// JPA2.g:213:1: subquery : lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
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
			// JPA2.g:214:5: (lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
			// JPA2.g:214:7: lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
			{
			lp=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subquery1887); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(lp);

			string_literal159=(Token)match(input,127,FOLLOW_127_in_subquery1889); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_127.add(string_literal159);

			pushFollow(FOLLOW_simple_select_clause_in_subquery1891);
			simple_select_clause160=simple_select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause160.getTree());
			pushFollow(FOLLOW_subquery_from_clause_in_subquery1893);
			subquery_from_clause161=subquery_from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause161.getTree());
			// JPA2.g:214:65: ( where_clause )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==142) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// JPA2.g:214:66: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_subquery1896);
					where_clause162=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause162.getTree());
					}
					break;

			}

			// JPA2.g:214:81: ( groupby_clause )?
			int alt50=2;
			int LA50_0 = input.LA(1);
			if ( (LA50_0==GROUP) ) {
				alt50=1;
			}
			switch (alt50) {
				case 1 :
					// JPA2.g:214:82: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_subquery1901);
					groupby_clause163=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause163.getTree());
					}
					break;

			}

			// JPA2.g:214:99: ( having_clause )?
			int alt51=2;
			int LA51_0 = input.LA(1);
			if ( (LA51_0==HAVING) ) {
				alt51=1;
			}
			switch (alt51) {
				case 1 :
					// JPA2.g:214:100: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_subquery1906);
					having_clause164=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause164.getTree());
					}
					break;

			}

			rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1912); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(rp);

			// AST REWRITE
			// elements: 127, where_clause, subquery_from_clause, simple_select_clause, groupby_clause, having_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 215:6: -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
			{
				// JPA2.g:215:9: ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);
				adaptor.addChild(root_1, stream_127.nextNode());
				adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
				adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
				// JPA2.g:215:90: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:215:106: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:215:124: ( having_clause )?
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
	// JPA2.g:216:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
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
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");

		try {
			// JPA2.g:217:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
			// JPA2.g:217:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
			{
			fr=(Token)match(input,101,FOLLOW_101_in_subquery_from_clause1962); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_101.add(fr);

			pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1964);
			subselect_identification_variable_declaration165=subselect_identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration165.getTree());
			// JPA2.g:217:63: ( ',' subselect_identification_variable_declaration )*
			loop52:
			while (true) {
				int alt52=2;
				int LA52_0 = input.LA(1);
				if ( (LA52_0==60) ) {
					alt52=1;
				}

				switch (alt52) {
				case 1 :
					// JPA2.g:217:64: ',' subselect_identification_variable_declaration
					{
					char_literal166=(Token)match(input,60,FOLLOW_60_in_subquery_from_clause1967); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal166);

					pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1969);
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
			// 218:5: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
			{
				// JPA2.g:218:8: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				// JPA2.g:218:35: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
				while ( stream_subselect_identification_variable_declaration.hasNext() ) {
					// JPA2.g:218:35: ^( T_SOURCE subselect_identification_variable_declaration )
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
	// JPA2.g:220:1: subselect_identification_variable_declaration : ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration );
	public final JPA2Parser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration() throws RecognitionException {
		JPA2Parser.subselect_identification_variable_declaration_return retval = new JPA2Parser.subselect_identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal170=null;
		ParserRuleReturnScope identification_variable_declaration168 =null;
		ParserRuleReturnScope derived_path_expression169 =null;
		ParserRuleReturnScope identification_variable171 =null;
		ParserRuleReturnScope join172 =null;
		ParserRuleReturnScope derived_collection_member_declaration173 =null;

		Object string_literal170_tree=null;

		try {
			// JPA2.g:221:5: ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration )
			int alt54=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA54_1 = input.LA(2);
				if ( (LA54_1==GROUP||LA54_1==WORD||LA54_1==81) ) {
					alt54=1;
				}
				else if ( (LA54_1==62) ) {
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
					// JPA2.g:221:7: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration2007);
					identification_variable_declaration168=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration168.getTree());

					}
					break;
				case 2 :
					// JPA2.g:222:7: derived_path_expression 'AS' identification_variable ( join )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration2015);
					derived_path_expression169=derived_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_path_expression169.getTree());

					string_literal170=(Token)match(input,81,FOLLOW_81_in_subselect_identification_variable_declaration2017); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal170_tree = (Object)adaptor.create(string_literal170);
					adaptor.addChild(root_0, string_literal170_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration2019);
					identification_variable171=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable171.getTree());

					// JPA2.g:222:60: ( join )*
					loop53:
					while (true) {
						int alt53=2;
						int LA53_0 = input.LA(1);
						if ( (LA53_0==INNER||(LA53_0 >= JOIN && LA53_0 <= LEFT)) ) {
							alt53=1;
						}

						switch (alt53) {
						case 1 :
							// JPA2.g:222:61: join
							{
							pushFollow(FOLLOW_join_in_subselect_identification_variable_declaration2022);
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
					// JPA2.g:223:7: derived_collection_member_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration2032);
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
	// JPA2.g:224:1: derived_path_expression : ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field );
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
			// JPA2.g:225:5: ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field )
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
					// JPA2.g:225:7: general_derived_path '.' single_valued_object_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression2043);
					general_derived_path174=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path174.getTree());

					char_literal175=(Token)match(input,62,FOLLOW_62_in_derived_path_expression2044); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal175_tree = (Object)adaptor.create(char_literal175);
					adaptor.addChild(root_0, char_literal175_tree);
					}

					pushFollow(FOLLOW_single_valued_object_field_in_derived_path_expression2045);
					single_valued_object_field176=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field176.getTree());

					}
					break;
				case 2 :
					// JPA2.g:226:7: general_derived_path '.' collection_valued_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression2053);
					general_derived_path177=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path177.getTree());

					char_literal178=(Token)match(input,62,FOLLOW_62_in_derived_path_expression2054); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal178_tree = (Object)adaptor.create(char_literal178);
					adaptor.addChild(root_0, char_literal178_tree);
					}

					pushFollow(FOLLOW_collection_valued_field_in_derived_path_expression2055);
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
	// JPA2.g:227:1: general_derived_path : ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* );
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
			// JPA2.g:228:5: ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* )
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
					// JPA2.g:228:7: simple_derived_path
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_derived_path_in_general_derived_path2066);
					simple_derived_path180=simple_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_derived_path180.getTree());

					}
					break;
				case 2 :
					// JPA2.g:229:7: treated_derived_path ( '.' single_valued_object_field )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_treated_derived_path_in_general_derived_path2074);
					treated_derived_path181=treated_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, treated_derived_path181.getTree());

					// JPA2.g:229:27: ( '.' single_valued_object_field )*
					loop56:
					while (true) {
						int alt56=2;
						int LA56_0 = input.LA(1);
						if ( (LA56_0==62) ) {
							int LA56_1 = input.LA(2);
							if ( (LA56_1==WORD) ) {
								int LA56_3 = input.LA(3);
								if ( (LA56_3==81) ) {
									int LA56_4 = input.LA(4);
									if ( (LA56_4==WORD) ) {
										int LA56_6 = input.LA(5);
										if ( (LA56_6==RPAREN) ) {
											int LA56_7 = input.LA(6);
											if ( (LA56_7==81) ) {
												int LA56_8 = input.LA(7);
												if ( (LA56_8==WORD) ) {
													int LA56_9 = input.LA(8);
													if ( (LA56_9==RPAREN) ) {
														alt56=1;
													}

												}

											}
											else if ( (LA56_7==62) ) {
												alt56=1;
											}

										}

									}

								}
								else if ( (LA56_3==62) ) {
									alt56=1;
								}

							}

						}

						switch (alt56) {
						case 1 :
							// JPA2.g:229:28: '.' single_valued_object_field
							{
							char_literal182=(Token)match(input,62,FOLLOW_62_in_general_derived_path2076); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal182_tree = (Object)adaptor.create(char_literal182);
							adaptor.addChild(root_0, char_literal182_tree);
							}

							pushFollow(FOLLOW_single_valued_object_field_in_general_derived_path2077);
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
	// JPA2.g:231:1: simple_derived_path : superquery_identification_variable ;
	public final JPA2Parser.simple_derived_path_return simple_derived_path() throws RecognitionException {
		JPA2Parser.simple_derived_path_return retval = new JPA2Parser.simple_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope superquery_identification_variable184 =null;


		try {
			// JPA2.g:232:5: ( superquery_identification_variable )
			// JPA2.g:232:7: superquery_identification_variable
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_superquery_identification_variable_in_simple_derived_path2095);
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
	// JPA2.g:234:1: treated_derived_path : 'TREAT(' general_derived_path 'AS' subtype ')' ;
	public final JPA2Parser.treated_derived_path_return treated_derived_path() throws RecognitionException {
		JPA2Parser.treated_derived_path_return retval = new JPA2Parser.treated_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal185=null;
		Token string_literal187=null;
		Token char_literal189=null;
		ParserRuleReturnScope general_derived_path186 =null;
		ParserRuleReturnScope subtype188 =null;

		Object string_literal185_tree=null;
		Object string_literal187_tree=null;
		Object char_literal189_tree=null;

		try {
			// JPA2.g:235:5: ( 'TREAT(' general_derived_path 'AS' subtype ')' )
			// JPA2.g:235:7: 'TREAT(' general_derived_path 'AS' subtype ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal185=(Token)match(input,134,FOLLOW_134_in_treated_derived_path2112); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal185_tree = (Object)adaptor.create(string_literal185);
			adaptor.addChild(root_0, string_literal185_tree);
			}

			pushFollow(FOLLOW_general_derived_path_in_treated_derived_path2113);
			general_derived_path186=general_derived_path();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path186.getTree());

			string_literal187=(Token)match(input,81,FOLLOW_81_in_treated_derived_path2115); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal187_tree = (Object)adaptor.create(string_literal187);
			adaptor.addChild(root_0, string_literal187_tree);
			}

			pushFollow(FOLLOW_subtype_in_treated_derived_path2117);
			subtype188=subtype();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subtype188.getTree());

			char_literal189=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_treated_derived_path2119); if (state.failed) return retval;
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
	// JPA2.g:236:1: derived_collection_member_declaration : 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field ;
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
			// JPA2.g:237:5: ( 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field )
			// JPA2.g:237:7: 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field
			{
			root_0 = (Object)adaptor.nil();


			string_literal190=(Token)match(input,IN,FOLLOW_IN_in_derived_collection_member_declaration2130); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal190_tree = (Object)adaptor.create(string_literal190);
			adaptor.addChild(root_0, string_literal190_tree);
			}

			pushFollow(FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2132);
			superquery_identification_variable191=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable191.getTree());

			char_literal192=(Token)match(input,62,FOLLOW_62_in_derived_collection_member_declaration2133); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal192_tree = (Object)adaptor.create(char_literal192);
			adaptor.addChild(root_0, char_literal192_tree);
			}

			// JPA2.g:237:49: ( single_valued_object_field '.' )*
			loop58:
			while (true) {
				int alt58=2;
				int LA58_0 = input.LA(1);
				if ( (LA58_0==WORD) ) {
					int LA58_1 = input.LA(2);
					if ( (LA58_1==62) ) {
						alt58=1;
					}

				}

				switch (alt58) {
				case 1 :
					// JPA2.g:237:50: single_valued_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2135);
					single_valued_object_field193=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field193.getTree());

					char_literal194=(Token)match(input,62,FOLLOW_62_in_derived_collection_member_declaration2137); if (state.failed) return retval;
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

			pushFollow(FOLLOW_collection_valued_field_in_derived_collection_member_declaration2140);
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
	// JPA2.g:239:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
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
			// JPA2.g:240:5: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
			// JPA2.g:240:7: ( 'DISTINCT' )? simple_select_expression
			{
			// JPA2.g:240:7: ( 'DISTINCT' )?
			int alt59=2;
			int LA59_0 = input.LA(1);
			if ( (LA59_0==DISTINCT) ) {
				alt59=1;
			}
			switch (alt59) {
				case 1 :
					// JPA2.g:240:8: 'DISTINCT'
					{
					string_literal196=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause2153); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal196);

					}
					break;

			}

			pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause2157);
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
			// 241:5: -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
			{
				// JPA2.g:241:8: ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:241:48: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);
				// JPA2.g:241:86: ( 'DISTINCT' )?
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
	// JPA2.g:242:1: simple_select_expression : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.simple_select_expression_return simple_select_expression() throws RecognitionException {
		JPA2Parser.simple_select_expression_return retval = new JPA2Parser.simple_select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression198 =null;
		ParserRuleReturnScope scalar_expression199 =null;
		ParserRuleReturnScope aggregate_expression200 =null;
		ParserRuleReturnScope identification_variable201 =null;


		try {
			// JPA2.g:243:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
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
					// JPA2.g:243:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_simple_select_expression2197);
					path_expression198=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression198.getTree());

					}
					break;
				case 2 :
					// JPA2.g:244:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_simple_select_expression2205);
					scalar_expression199=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression199.getTree());

					}
					break;
				case 3 :
					// JPA2.g:245:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression2213);
					aggregate_expression200=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression200.getTree());

					}
					break;
				case 4 :
					// JPA2.g:246:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_select_expression2221);
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
	// JPA2.g:247:1: scalar_expression : ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression );
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
			// JPA2.g:248:5: ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression )
			int alt61=7;
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
			case 71:
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
			case 57:
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
			case 76:
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
			case 144:
			case 145:
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
					// JPA2.g:248:7: arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_scalar_expression2232);
					arithmetic_expression202=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression202.getTree());

					}
					break;
				case 2 :
					// JPA2.g:249:7: string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_scalar_expression2240);
					string_expression203=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression203.getTree());

					}
					break;
				case 3 :
					// JPA2.g:250:7: enum_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_scalar_expression2248);
					enum_expression204=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression204.getTree());

					}
					break;
				case 4 :
					// JPA2.g:251:7: datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_scalar_expression2256);
					datetime_expression205=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression205.getTree());

					}
					break;
				case 5 :
					// JPA2.g:252:7: boolean_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_scalar_expression2264);
					boolean_expression206=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression206.getTree());

					}
					break;
				case 6 :
					// JPA2.g:253:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_scalar_expression2272);
					case_expression207=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression207.getTree());

					}
					break;
				case 7 :
					// JPA2.g:254:7: entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_scalar_expression2280);
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
	// JPA2.g:255:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
	public final JPA2Parser.conditional_expression_return conditional_expression() throws RecognitionException {
		JPA2Parser.conditional_expression_return retval = new JPA2Parser.conditional_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal210=null;
		ParserRuleReturnScope conditional_term209 =null;
		ParserRuleReturnScope conditional_term211 =null;

		Object string_literal210_tree=null;

		try {
			// JPA2.g:256:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
			// JPA2.g:256:7: ( conditional_term ) ( 'OR' conditional_term )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:256:7: ( conditional_term )
			// JPA2.g:256:8: conditional_term
			{
			pushFollow(FOLLOW_conditional_term_in_conditional_expression2292);
			conditional_term209=conditional_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term209.getTree());

			}

			// JPA2.g:256:26: ( 'OR' conditional_term )*
			loop62:
			while (true) {
				int alt62=2;
				int LA62_0 = input.LA(1);
				if ( (LA62_0==OR) ) {
					alt62=1;
				}

				switch (alt62) {
				case 1 :
					// JPA2.g:256:27: 'OR' conditional_term
					{
					string_literal210=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression2296); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal210_tree = (Object)adaptor.create(string_literal210);
					adaptor.addChild(root_0, string_literal210_tree);
					}

					pushFollow(FOLLOW_conditional_term_in_conditional_expression2298);
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
	// JPA2.g:257:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
	public final JPA2Parser.conditional_term_return conditional_term() throws RecognitionException {
		JPA2Parser.conditional_term_return retval = new JPA2Parser.conditional_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal213=null;
		ParserRuleReturnScope conditional_factor212 =null;
		ParserRuleReturnScope conditional_factor214 =null;

		Object string_literal213_tree=null;

		try {
			// JPA2.g:258:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
			// JPA2.g:258:7: ( conditional_factor ) ( 'AND' conditional_factor )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:258:7: ( conditional_factor )
			// JPA2.g:258:8: conditional_factor
			{
			pushFollow(FOLLOW_conditional_factor_in_conditional_term2312);
			conditional_factor212=conditional_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor212.getTree());

			}

			// JPA2.g:258:28: ( 'AND' conditional_factor )*
			loop63:
			while (true) {
				int alt63=2;
				int LA63_0 = input.LA(1);
				if ( (LA63_0==AND) ) {
					alt63=1;
				}

				switch (alt63) {
				case 1 :
					// JPA2.g:258:29: 'AND' conditional_factor
					{
					string_literal213=(Token)match(input,AND,FOLLOW_AND_in_conditional_term2316); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal213_tree = (Object)adaptor.create(string_literal213);
					adaptor.addChild(root_0, string_literal213_tree);
					}

					pushFollow(FOLLOW_conditional_factor_in_conditional_term2318);
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
	// JPA2.g:259:1: conditional_factor : ( 'NOT' )? conditional_primary ;
	public final JPA2Parser.conditional_factor_return conditional_factor() throws RecognitionException {
		JPA2Parser.conditional_factor_return retval = new JPA2Parser.conditional_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal215=null;
		ParserRuleReturnScope conditional_primary216 =null;

		Object string_literal215_tree=null;

		try {
			// JPA2.g:260:5: ( ( 'NOT' )? conditional_primary )
			// JPA2.g:260:7: ( 'NOT' )? conditional_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:260:7: ( 'NOT' )?
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
					// JPA2.g:260:8: 'NOT'
					{
					string_literal215=(Token)match(input,NOT,FOLLOW_NOT_in_conditional_factor2332); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal215_tree = (Object)adaptor.create(string_literal215);
					adaptor.addChild(root_0, string_literal215_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_conditional_primary_in_conditional_factor2336);
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
	// JPA2.g:261:1: conditional_primary : ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' );
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
			// JPA2.g:262:5: ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' )
			int alt65=2;
			int LA65_0 = input.LA(1);
			if ( (LA65_0==AVG||LA65_0==COUNT||LA65_0==GROUP||LA65_0==INT_NUMERAL||LA65_0==LOWER||(LA65_0 >= MAX && LA65_0 <= NOT)||(LA65_0 >= STRING_LITERAL && LA65_0 <= SUM)||LA65_0==WORD||LA65_0==57||LA65_0==59||LA65_0==61||LA65_0==64||(LA65_0 >= 71 && LA65_0 <= 78)||(LA65_0 >= 84 && LA65_0 <= 90)||(LA65_0 >= 99 && LA65_0 <= 100)||LA65_0==102||LA65_0==104||LA65_0==108||LA65_0==110||LA65_0==113||LA65_0==118||LA65_0==128||(LA65_0 >= 130 && LA65_0 <= 131)||(LA65_0 >= 134 && LA65_0 <= 136)||LA65_0==138||(LA65_0 >= 144 && LA65_0 <= 145)) ) {
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
					// JPA2.g:262:7: simple_cond_expression
					{
					pushFollow(FOLLOW_simple_cond_expression_in_conditional_primary2347);
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
					// 263:5: -> ^( T_SIMPLE_CONDITION[] simple_cond_expression )
					{
						// JPA2.g:263:8: ^( T_SIMPLE_CONDITION[] simple_cond_expression )
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
					// JPA2.g:264:7: '(' conditional_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal218=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_primary2371); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal218_tree = (Object)adaptor.create(char_literal218);
					adaptor.addChild(root_0, char_literal218_tree);
					}

					pushFollow(FOLLOW_conditional_expression_in_conditional_primary2372);
					conditional_expression219=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression219.getTree());

					char_literal220=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_primary2373); if (state.failed) return retval;
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
	// JPA2.g:265:1: simple_cond_expression : ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
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
			// JPA2.g:266:5: ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
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
			case 71:
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
			case 57:
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
			case 76:
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
			case 59:
			case 61:
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
			case 64:
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
			case INT_NUMERAL:
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
			case 78:
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
			case 72:
			case 73:
			case 74:
			case 75:
			case 77:
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
					// JPA2.g:266:7: comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression2384);
					comparison_expression221=comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression221.getTree());

					}
					break;
				case 2 :
					// JPA2.g:267:7: between_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_between_expression_in_simple_cond_expression2392);
					between_expression222=between_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression222.getTree());

					}
					break;
				case 3 :
					// JPA2.g:268:7: in_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_in_expression_in_simple_cond_expression2400);
					in_expression223=in_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression223.getTree());

					}
					break;
				case 4 :
					// JPA2.g:269:7: like_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_like_expression_in_simple_cond_expression2408);
					like_expression224=like_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression224.getTree());

					}
					break;
				case 5 :
					// JPA2.g:270:7: null_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression2416);
					null_comparison_expression225=null_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression225.getTree());

					}
					break;
				case 6 :
					// JPA2.g:271:7: empty_collection_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2424);
					empty_collection_comparison_expression226=empty_collection_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression226.getTree());

					}
					break;
				case 7 :
					// JPA2.g:272:7: collection_member_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression2432);
					collection_member_expression227=collection_member_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression227.getTree());

					}
					break;
				case 8 :
					// JPA2.g:273:7: exists_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exists_expression_in_simple_cond_expression2440);
					exists_expression228=exists_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression228.getTree());

					}
					break;
				case 9 :
					// JPA2.g:274:7: date_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression2448);
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
	// JPA2.g:277:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
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
			// JPA2.g:278:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
			int alt67=5;
			switch ( input.LA(1) ) {
			case 72:
				{
				alt67=1;
				}
				break;
			case 74:
				{
				alt67=2;
				}
				break;
			case 73:
				{
				alt67=3;
				}
				break;
			case 75:
				{
				alt67=4;
				}
				break;
			case 77:
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
					// JPA2.g:278:7: date_between_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2461);
					date_between_macro_expression230=date_between_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression230.getTree());

					}
					break;
				case 2 :
					// JPA2.g:279:7: date_before_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2469);
					date_before_macro_expression231=date_before_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression231.getTree());

					}
					break;
				case 3 :
					// JPA2.g:280:7: date_after_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2477);
					date_after_macro_expression232=date_after_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression232.getTree());

					}
					break;
				case 4 :
					// JPA2.g:281:7: date_equals_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2485);
					date_equals_macro_expression233=date_equals_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression233.getTree());

					}
					break;
				case 5 :
					// JPA2.g:282:7: date_today_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2493);
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
	// JPA2.g:284:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
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
			// JPA2.g:285:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
			// JPA2.g:285:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal235=(Token)match(input,72,FOLLOW_72_in_date_between_macro_expression2505); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal235_tree = (Object)adaptor.create(string_literal235);
			adaptor.addChild(root_0, string_literal235_tree);
			}

			char_literal236=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2507); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal236_tree = (Object)adaptor.create(char_literal236);
			adaptor.addChild(root_0, char_literal236_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2509);
			path_expression237=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression237.getTree());

			char_literal238=(Token)match(input,60,FOLLOW_60_in_date_between_macro_expression2511); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal238_tree = (Object)adaptor.create(char_literal238);
			adaptor.addChild(root_0, char_literal238_tree);
			}

			string_literal239=(Token)match(input,116,FOLLOW_116_in_date_between_macro_expression2513); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal239_tree = (Object)adaptor.create(string_literal239);
			adaptor.addChild(root_0, string_literal239_tree);
			}

			// JPA2.g:285:48: ( ( '+' | '-' ) numeric_literal )?
			int alt68=2;
			int LA68_0 = input.LA(1);
			if ( (LA68_0==59||LA68_0==61) ) {
				alt68=1;
			}
			switch (alt68) {
				case 1 :
					// JPA2.g:285:49: ( '+' | '-' ) numeric_literal
					{
					set240=input.LT(1);
					if ( input.LA(1)==59||input.LA(1)==61 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2524);
					numeric_literal241=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal241.getTree());

					}
					break;

			}

			char_literal242=(Token)match(input,60,FOLLOW_60_in_date_between_macro_expression2528); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal242_tree = (Object)adaptor.create(char_literal242);
			adaptor.addChild(root_0, char_literal242_tree);
			}

			string_literal243=(Token)match(input,116,FOLLOW_116_in_date_between_macro_expression2530); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal243_tree = (Object)adaptor.create(string_literal243);
			adaptor.addChild(root_0, string_literal243_tree);
			}

			// JPA2.g:285:89: ( ( '+' | '-' ) numeric_literal )?
			int alt69=2;
			int LA69_0 = input.LA(1);
			if ( (LA69_0==59||LA69_0==61) ) {
				alt69=1;
			}
			switch (alt69) {
				case 1 :
					// JPA2.g:285:90: ( '+' | '-' ) numeric_literal
					{
					set244=input.LT(1);
					if ( input.LA(1)==59||input.LA(1)==61 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2541);
					numeric_literal245=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal245.getTree());

					}
					break;

			}

			char_literal246=(Token)match(input,60,FOLLOW_60_in_date_between_macro_expression2545); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal246_tree = (Object)adaptor.create(char_literal246);
			adaptor.addChild(root_0, char_literal246_tree);
			}

			set247=input.LT(1);
			if ( input.LA(1)==91||input.LA(1)==103||input.LA(1)==112||input.LA(1)==114||input.LA(1)==126||input.LA(1)==143 ) {
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
			char_literal248=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2570); if (state.failed) return retval;
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
	// JPA2.g:287:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
			// JPA2.g:288:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:288:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal249=(Token)match(input,74,FOLLOW_74_in_date_before_macro_expression2582); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal249_tree = (Object)adaptor.create(string_literal249);
			adaptor.addChild(root_0, string_literal249_tree);
			}

			char_literal250=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2584); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal250_tree = (Object)adaptor.create(char_literal250);
			adaptor.addChild(root_0, char_literal250_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2586);
			path_expression251=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression251.getTree());

			char_literal252=(Token)match(input,60,FOLLOW_60_in_date_before_macro_expression2588); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal252_tree = (Object)adaptor.create(char_literal252);
			adaptor.addChild(root_0, char_literal252_tree);
			}

			// JPA2.g:288:45: ( path_expression | input_parameter )
			int alt70=2;
			int LA70_0 = input.LA(1);
			if ( (LA70_0==GROUP||LA70_0==WORD) ) {
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
					// JPA2.g:288:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2591);
					path_expression253=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression253.getTree());

					}
					break;
				case 2 :
					// JPA2.g:288:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2595);
					input_parameter254=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter254.getTree());

					}
					break;

			}

			char_literal255=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2598); if (state.failed) return retval;
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
	// JPA2.g:290:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
			// JPA2.g:291:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:291:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal256=(Token)match(input,73,FOLLOW_73_in_date_after_macro_expression2610); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal256_tree = (Object)adaptor.create(string_literal256);
			adaptor.addChild(root_0, string_literal256_tree);
			}

			char_literal257=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2612); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal257_tree = (Object)adaptor.create(char_literal257);
			adaptor.addChild(root_0, char_literal257_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2614);
			path_expression258=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression258.getTree());

			char_literal259=(Token)match(input,60,FOLLOW_60_in_date_after_macro_expression2616); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal259_tree = (Object)adaptor.create(char_literal259);
			adaptor.addChild(root_0, char_literal259_tree);
			}

			// JPA2.g:291:44: ( path_expression | input_parameter )
			int alt71=2;
			int LA71_0 = input.LA(1);
			if ( (LA71_0==GROUP||LA71_0==WORD) ) {
				alt71=1;
			}
			else if ( (LA71_0==NAMED_PARAMETER||LA71_0==57||LA71_0==71) ) {
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
					// JPA2.g:291:45: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2619);
					path_expression260=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression260.getTree());

					}
					break;
				case 2 :
					// JPA2.g:291:63: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2623);
					input_parameter261=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter261.getTree());

					}
					break;

			}

			char_literal262=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2626); if (state.failed) return retval;
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
	// JPA2.g:293:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
			// JPA2.g:294:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:294:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal263=(Token)match(input,75,FOLLOW_75_in_date_equals_macro_expression2638); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal263_tree = (Object)adaptor.create(string_literal263);
			adaptor.addChild(root_0, string_literal263_tree);
			}

			char_literal264=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2640); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal264_tree = (Object)adaptor.create(char_literal264);
			adaptor.addChild(root_0, char_literal264_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2642);
			path_expression265=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression265.getTree());

			char_literal266=(Token)match(input,60,FOLLOW_60_in_date_equals_macro_expression2644); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal266_tree = (Object)adaptor.create(char_literal266);
			adaptor.addChild(root_0, char_literal266_tree);
			}

			// JPA2.g:294:45: ( path_expression | input_parameter )
			int alt72=2;
			int LA72_0 = input.LA(1);
			if ( (LA72_0==GROUP||LA72_0==WORD) ) {
				alt72=1;
			}
			else if ( (LA72_0==NAMED_PARAMETER||LA72_0==57||LA72_0==71) ) {
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
					// JPA2.g:294:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2647);
					path_expression267=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression267.getTree());

					}
					break;
				case 2 :
					// JPA2.g:294:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2651);
					input_parameter268=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter268.getTree());

					}
					break;

			}

			char_literal269=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2654); if (state.failed) return retval;
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
	// JPA2.g:296:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
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
			// JPA2.g:297:5: ( '@TODAY' '(' path_expression ')' )
			// JPA2.g:297:7: '@TODAY' '(' path_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal270=(Token)match(input,77,FOLLOW_77_in_date_today_macro_expression2666); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal270_tree = (Object)adaptor.create(string_literal270);
			adaptor.addChild(root_0, string_literal270_tree);
			}

			char_literal271=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2668); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal271_tree = (Object)adaptor.create(char_literal271);
			adaptor.addChild(root_0, char_literal271_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2670);
			path_expression272=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression272.getTree());

			char_literal273=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2672); if (state.failed) return retval;
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
	// JPA2.g:300:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
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
			// JPA2.g:301:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
			int alt76=3;
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
			case 128:
			case 130:
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
			case 71:
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
			case 57:
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
			case 102:
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
			case 84:
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
			case 86:
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
			case 118:
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
			case 85:
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
			case 100:
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
			case 76:
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
			case 87:
			case 131:
			case 135:
			case 138:
				{
				alt76=2;
				}
				break;
			case 88:
			case 89:
			case 90:
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
					// JPA2.g:301:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2685);
					arithmetic_expression274=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression274.getTree());

					// JPA2.g:301:29: ( 'NOT' )?
					int alt73=2;
					int LA73_0 = input.LA(1);
					if ( (LA73_0==NOT) ) {
						alt73=1;
					}
					switch (alt73) {
						case 1 :
							// JPA2.g:301:30: 'NOT'
							{
							string_literal275=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2688); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal275_tree = (Object)adaptor.create(string_literal275);
							adaptor.addChild(root_0, string_literal275_tree);
							}

							}
							break;

					}

					string_literal276=(Token)match(input,82,FOLLOW_82_in_between_expression2692); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal276_tree = (Object)adaptor.create(string_literal276);
					adaptor.addChild(root_0, string_literal276_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2694);
					arithmetic_expression277=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression277.getTree());

					string_literal278=(Token)match(input,AND,FOLLOW_AND_in_between_expression2696); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal278_tree = (Object)adaptor.create(string_literal278);
					adaptor.addChild(root_0, string_literal278_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2698);
					arithmetic_expression279=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression279.getTree());

					}
					break;
				case 2 :
					// JPA2.g:302:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_between_expression2706);
					string_expression280=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression280.getTree());

					// JPA2.g:302:25: ( 'NOT' )?
					int alt74=2;
					int LA74_0 = input.LA(1);
					if ( (LA74_0==NOT) ) {
						alt74=1;
					}
					switch (alt74) {
						case 1 :
							// JPA2.g:302:26: 'NOT'
							{
							string_literal281=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2709); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal281_tree = (Object)adaptor.create(string_literal281);
							adaptor.addChild(root_0, string_literal281_tree);
							}

							}
							break;

					}

					string_literal282=(Token)match(input,82,FOLLOW_82_in_between_expression2713); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal282_tree = (Object)adaptor.create(string_literal282);
					adaptor.addChild(root_0, string_literal282_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2715);
					string_expression283=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression283.getTree());

					string_literal284=(Token)match(input,AND,FOLLOW_AND_in_between_expression2717); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal284_tree = (Object)adaptor.create(string_literal284);
					adaptor.addChild(root_0, string_literal284_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2719);
					string_expression285=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression285.getTree());

					}
					break;
				case 3 :
					// JPA2.g:303:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_between_expression2727);
					datetime_expression286=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression286.getTree());

					// JPA2.g:303:27: ( 'NOT' )?
					int alt75=2;
					int LA75_0 = input.LA(1);
					if ( (LA75_0==NOT) ) {
						alt75=1;
					}
					switch (alt75) {
						case 1 :
							// JPA2.g:303:28: 'NOT'
							{
							string_literal287=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2730); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal287_tree = (Object)adaptor.create(string_literal287);
							adaptor.addChild(root_0, string_literal287_tree);
							}

							}
							break;

					}

					string_literal288=(Token)match(input,82,FOLLOW_82_in_between_expression2734); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal288_tree = (Object)adaptor.create(string_literal288);
					adaptor.addChild(root_0, string_literal288_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2736);
					datetime_expression289=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression289.getTree());

					string_literal290=(Token)match(input,AND,FOLLOW_AND_in_between_expression2738); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal290_tree = (Object)adaptor.create(string_literal290);
					adaptor.addChild(root_0, string_literal290_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2740);
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
	// JPA2.g:304:1: in_expression : ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) ;
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
			// JPA2.g:305:5: ( ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) )
			// JPA2.g:305:7: ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:305:7: ( path_expression | type_discriminator | identification_variable )
			int alt77=3;
			int LA77_0 = input.LA(1);
			if ( (LA77_0==GROUP||LA77_0==WORD) ) {
				int LA77_1 = input.LA(2);
				if ( (LA77_1==62) ) {
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
			else if ( (LA77_0==136) ) {
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
					// JPA2.g:305:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_in_expression2752);
					path_expression292=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression292.getTree());

					}
					break;
				case 2 :
					// JPA2.g:305:26: type_discriminator
					{
					pushFollow(FOLLOW_type_discriminator_in_in_expression2756);
					type_discriminator293=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator293.getTree());

					}
					break;
				case 3 :
					// JPA2.g:305:47: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_in_expression2760);
					identification_variable294=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable294.getTree());

					}
					break;

			}

			// JPA2.g:305:72: ( NOT )?
			int alt78=2;
			int LA78_0 = input.LA(1);
			if ( (LA78_0==NOT) ) {
				alt78=1;
			}
			switch (alt78) {
				case 1 :
					// JPA2.g:305:73: NOT
					{
					NOT295=(Token)match(input,NOT,FOLLOW_NOT_in_in_expression2764); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT295_tree = (Object)adaptor.create(NOT295);
					adaptor.addChild(root_0, NOT295_tree);
					}

					}
					break;

			}

			IN296=(Token)match(input,IN,FOLLOW_IN_in_in_expression2768); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN296_tree = (Object)adaptor.create(IN296);
			adaptor.addChild(root_0, IN296_tree);
			}

			// JPA2.g:306:13: ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			int alt80=4;
			int LA80_0 = input.LA(1);
			if ( (LA80_0==LPAREN) ) {
				switch ( input.LA(2) ) {
				case 127:
					{
					alt80=2;
					}
					break;
				case INT_NUMERAL:
				case NAMED_PARAMETER:
				case STRING_LITERAL:
				case 57:
				case 64:
				case 71:
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
			else if ( (LA80_0==NAMED_PARAMETER||LA80_0==57||LA80_0==71) ) {
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
					// JPA2.g:306:15: '(' in_item ( ',' in_item )* ')'
					{
					char_literal297=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2784); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal297_tree = (Object)adaptor.create(char_literal297);
					adaptor.addChild(root_0, char_literal297_tree);
					}

					pushFollow(FOLLOW_in_item_in_in_expression2786);
					in_item298=in_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item298.getTree());

					// JPA2.g:306:27: ( ',' in_item )*
					loop79:
					while (true) {
						int alt79=2;
						int LA79_0 = input.LA(1);
						if ( (LA79_0==60) ) {
							alt79=1;
						}

						switch (alt79) {
						case 1 :
							// JPA2.g:306:28: ',' in_item
							{
							char_literal299=(Token)match(input,60,FOLLOW_60_in_in_expression2789); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal299_tree = (Object)adaptor.create(char_literal299);
							adaptor.addChild(root_0, char_literal299_tree);
							}

							pushFollow(FOLLOW_in_item_in_in_expression2791);
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

					char_literal301=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2795); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal301_tree = (Object)adaptor.create(char_literal301);
					adaptor.addChild(root_0, char_literal301_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:307:15: subquery
					{
					pushFollow(FOLLOW_subquery_in_in_expression2811);
					subquery302=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery302.getTree());

					}
					break;
				case 3 :
					// JPA2.g:308:15: collection_valued_input_parameter
					{
					pushFollow(FOLLOW_collection_valued_input_parameter_in_in_expression2827);
					collection_valued_input_parameter303=collection_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_input_parameter303.getTree());

					}
					break;
				case 4 :
					// JPA2.g:309:15: '(' path_expression ')'
					{
					char_literal304=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2843); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal304_tree = (Object)adaptor.create(char_literal304);
					adaptor.addChild(root_0, char_literal304_tree);
					}

					pushFollow(FOLLOW_path_expression_in_in_expression2845);
					path_expression305=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression305.getTree());

					char_literal306=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2847); if (state.failed) return retval;
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
	// JPA2.g:315:1: in_item : ( string_literal | numeric_literal | single_valued_input_parameter );
	public final JPA2Parser.in_item_return in_item() throws RecognitionException {
		JPA2Parser.in_item_return retval = new JPA2Parser.in_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal307 =null;
		ParserRuleReturnScope numeric_literal308 =null;
		ParserRuleReturnScope single_valued_input_parameter309 =null;


		try {
			// JPA2.g:316:5: ( string_literal | numeric_literal | single_valued_input_parameter )
			int alt81=3;
			switch ( input.LA(1) ) {
			case STRING_LITERAL:
				{
				alt81=1;
				}
				break;
			case INT_NUMERAL:
			case 64:
				{
				alt81=2;
				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt81=3;
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
					// JPA2.g:316:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_in_item2875);
					string_literal307=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal307.getTree());

					}
					break;
				case 2 :
					// JPA2.g:316:24: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_in_item2879);
					numeric_literal308=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal308.getTree());

					}
					break;
				case 3 :
					// JPA2.g:316:42: single_valued_input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_single_valued_input_parameter_in_in_item2883);
					single_valued_input_parameter309=single_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_input_parameter309.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:317:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? ;
	public final JPA2Parser.like_expression_return like_expression() throws RecognitionException {
		JPA2Parser.like_expression_return retval = new JPA2Parser.like_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal311=null;
		Token string_literal312=null;
		Token string_literal316=null;
		ParserRuleReturnScope string_expression310 =null;
		ParserRuleReturnScope string_expression313 =null;
		ParserRuleReturnScope pattern_value314 =null;
		ParserRuleReturnScope input_parameter315 =null;
		ParserRuleReturnScope escape_character317 =null;

		Object string_literal311_tree=null;
		Object string_literal312_tree=null;
		Object string_literal316_tree=null;

		try {
			// JPA2.g:318:5: ( string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? )
			// JPA2.g:318:7: string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_expression_in_like_expression2894);
			string_expression310=string_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression310.getTree());

			// JPA2.g:318:25: ( 'NOT' )?
			int alt82=2;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==NOT) ) {
				alt82=1;
			}
			switch (alt82) {
				case 1 :
					// JPA2.g:318:26: 'NOT'
					{
					string_literal311=(Token)match(input,NOT,FOLLOW_NOT_in_like_expression2897); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal311_tree = (Object)adaptor.create(string_literal311);
					adaptor.addChild(root_0, string_literal311_tree);
					}

					}
					break;

			}

			string_literal312=(Token)match(input,109,FOLLOW_109_in_like_expression2901); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal312_tree = (Object)adaptor.create(string_literal312);
			adaptor.addChild(root_0, string_literal312_tree);
			}

			// JPA2.g:318:41: ( string_expression | pattern_value | input_parameter )
			int alt83=3;
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
			case 76:
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
				alt83=1;
				}
				break;
			case STRING_LITERAL:
				{
				int LA83_2 = input.LA(2);
				if ( (synpred137_JPA2()) ) {
					alt83=1;
				}
				else if ( (synpred138_JPA2()) ) {
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
			case 71:
				{
				int LA83_3 = input.LA(2);
				if ( (LA83_3==64) ) {
					int LA83_7 = input.LA(3);
					if ( (LA83_7==INT_NUMERAL) ) {
						int LA83_11 = input.LA(4);
						if ( (synpred137_JPA2()) ) {
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
					if ( (synpred137_JPA2()) ) {
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
				if ( (synpred137_JPA2()) ) {
					alt83=1;
				}
				else if ( (true) ) {
					alt83=3;
				}

				}
				break;
			case 57:
				{
				int LA83_5 = input.LA(2);
				if ( (LA83_5==WORD) ) {
					int LA83_10 = input.LA(3);
					if ( (LA83_10==146) ) {
						int LA83_12 = input.LA(4);
						if ( (synpred137_JPA2()) ) {
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
					// JPA2.g:318:42: string_expression
					{
					pushFollow(FOLLOW_string_expression_in_like_expression2904);
					string_expression313=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression313.getTree());

					}
					break;
				case 2 :
					// JPA2.g:318:62: pattern_value
					{
					pushFollow(FOLLOW_pattern_value_in_like_expression2908);
					pattern_value314=pattern_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value314.getTree());

					}
					break;
				case 3 :
					// JPA2.g:318:78: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_like_expression2912);
					input_parameter315=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter315.getTree());

					}
					break;

			}

			// JPA2.g:318:94: ( 'ESCAPE' escape_character )?
			int alt84=2;
			int LA84_0 = input.LA(1);
			if ( (LA84_0==98) ) {
				alt84=1;
			}
			switch (alt84) {
				case 1 :
					// JPA2.g:318:95: 'ESCAPE' escape_character
					{
					string_literal316=(Token)match(input,98,FOLLOW_98_in_like_expression2915); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal316_tree = (Object)adaptor.create(string_literal316);
					adaptor.addChild(root_0, string_literal316_tree);
					}

					pushFollow(FOLLOW_escape_character_in_like_expression2917);
					escape_character317=escape_character();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, escape_character317.getTree());

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
	// JPA2.g:319:1: null_comparison_expression : ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' ;
	public final JPA2Parser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
		JPA2Parser.null_comparison_expression_return retval = new JPA2Parser.null_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal321=null;
		Token string_literal322=null;
		Token string_literal323=null;
		ParserRuleReturnScope path_expression318 =null;
		ParserRuleReturnScope input_parameter319 =null;
		ParserRuleReturnScope join_association_path_expression320 =null;

		Object string_literal321_tree=null;
		Object string_literal322_tree=null;
		Object string_literal323_tree=null;

		try {
			// JPA2.g:320:5: ( ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' )
			// JPA2.g:320:7: ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:320:7: ( path_expression | input_parameter | join_association_path_expression )
			int alt85=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA85_1 = input.LA(2);
				if ( (LA85_1==62) ) {
					int LA85_5 = input.LA(3);
					if ( (synpred140_JPA2()) ) {
						alt85=1;
					}
					else if ( (true) ) {
						alt85=3;
					}

				}
				else if ( (LA85_1==105) ) {
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
			case 57:
			case 71:
				{
				alt85=2;
				}
				break;
			case 134:
				{
				alt85=3;
				}
				break;
			case GROUP:
				{
				int LA85_4 = input.LA(2);
				if ( (LA85_4==62) ) {
					int LA85_6 = input.LA(3);
					if ( (synpred140_JPA2()) ) {
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
					// JPA2.g:320:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_null_comparison_expression2931);
					path_expression318=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression318.getTree());

					}
					break;
				case 2 :
					// JPA2.g:320:26: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_null_comparison_expression2935);
					input_parameter319=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter319.getTree());

					}
					break;
				case 3 :
					// JPA2.g:320:44: join_association_path_expression
					{
					pushFollow(FOLLOW_join_association_path_expression_in_null_comparison_expression2939);
					join_association_path_expression320=join_association_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression320.getTree());

					}
					break;

			}

			string_literal321=(Token)match(input,105,FOLLOW_105_in_null_comparison_expression2942); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal321_tree = (Object)adaptor.create(string_literal321);
			adaptor.addChild(root_0, string_literal321_tree);
			}

			// JPA2.g:320:83: ( 'NOT' )?
			int alt86=2;
			int LA86_0 = input.LA(1);
			if ( (LA86_0==NOT) ) {
				alt86=1;
			}
			switch (alt86) {
				case 1 :
					// JPA2.g:320:84: 'NOT'
					{
					string_literal322=(Token)match(input,NOT,FOLLOW_NOT_in_null_comparison_expression2945); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal322_tree = (Object)adaptor.create(string_literal322);
					adaptor.addChild(root_0, string_literal322_tree);
					}

					}
					break;

			}

			string_literal323=(Token)match(input,117,FOLLOW_117_in_null_comparison_expression2949); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal323_tree = (Object)adaptor.create(string_literal323);
			adaptor.addChild(root_0, string_literal323_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:321:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
	public final JPA2Parser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
		JPA2Parser.empty_collection_comparison_expression_return retval = new JPA2Parser.empty_collection_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal325=null;
		Token string_literal326=null;
		Token string_literal327=null;
		ParserRuleReturnScope path_expression324 =null;

		Object string_literal325_tree=null;
		Object string_literal326_tree=null;
		Object string_literal327_tree=null;

		try {
			// JPA2.g:322:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
			// JPA2.g:322:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression2960);
			path_expression324=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression324.getTree());

			string_literal325=(Token)match(input,105,FOLLOW_105_in_empty_collection_comparison_expression2962); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal325_tree = (Object)adaptor.create(string_literal325);
			adaptor.addChild(root_0, string_literal325_tree);
			}

			// JPA2.g:322:28: ( 'NOT' )?
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==NOT) ) {
				alt87=1;
			}
			switch (alt87) {
				case 1 :
					// JPA2.g:322:29: 'NOT'
					{
					string_literal326=(Token)match(input,NOT,FOLLOW_NOT_in_empty_collection_comparison_expression2965); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal326_tree = (Object)adaptor.create(string_literal326);
					adaptor.addChild(root_0, string_literal326_tree);
					}

					}
					break;

			}

			string_literal327=(Token)match(input,94,FOLLOW_94_in_empty_collection_comparison_expression2969); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal327_tree = (Object)adaptor.create(string_literal327);
			adaptor.addChild(root_0, string_literal327_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:323:1: collection_member_expression : entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
	public final JPA2Parser.collection_member_expression_return collection_member_expression() throws RecognitionException {
		JPA2Parser.collection_member_expression_return retval = new JPA2Parser.collection_member_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal329=null;
		Token string_literal330=null;
		Token string_literal331=null;
		ParserRuleReturnScope entity_or_value_expression328 =null;
		ParserRuleReturnScope path_expression332 =null;

		Object string_literal329_tree=null;
		Object string_literal330_tree=null;
		Object string_literal331_tree=null;

		try {
			// JPA2.g:324:5: ( entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
			// JPA2.g:324:7: entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_or_value_expression_in_collection_member_expression2980);
			entity_or_value_expression328=entity_or_value_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_or_value_expression328.getTree());

			// JPA2.g:324:35: ( 'NOT' )?
			int alt88=2;
			int LA88_0 = input.LA(1);
			if ( (LA88_0==NOT) ) {
				alt88=1;
			}
			switch (alt88) {
				case 1 :
					// JPA2.g:324:36: 'NOT'
					{
					string_literal329=(Token)match(input,NOT,FOLLOW_NOT_in_collection_member_expression2984); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal329_tree = (Object)adaptor.create(string_literal329);
					adaptor.addChild(root_0, string_literal329_tree);
					}

					}
					break;

			}

			string_literal330=(Token)match(input,111,FOLLOW_111_in_collection_member_expression2988); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal330_tree = (Object)adaptor.create(string_literal330);
			adaptor.addChild(root_0, string_literal330_tree);
			}

			// JPA2.g:324:53: ( 'OF' )?
			int alt89=2;
			int LA89_0 = input.LA(1);
			if ( (LA89_0==122) ) {
				alt89=1;
			}
			switch (alt89) {
				case 1 :
					// JPA2.g:324:54: 'OF'
					{
					string_literal331=(Token)match(input,122,FOLLOW_122_in_collection_member_expression2991); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal331_tree = (Object)adaptor.create(string_literal331);
					adaptor.addChild(root_0, string_literal331_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_path_expression_in_collection_member_expression2995);
			path_expression332=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression332.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:325:1: entity_or_value_expression : ( path_expression | simple_entity_or_value_expression | subquery );
	public final JPA2Parser.entity_or_value_expression_return entity_or_value_expression() throws RecognitionException {
		JPA2Parser.entity_or_value_expression_return retval = new JPA2Parser.entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression333 =null;
		ParserRuleReturnScope simple_entity_or_value_expression334 =null;
		ParserRuleReturnScope subquery335 =null;


		try {
			// JPA2.g:326:5: ( path_expression | simple_entity_or_value_expression | subquery )
			int alt90=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA90_1 = input.LA(2);
				if ( (LA90_1==62) ) {
					alt90=1;
				}
				else if ( (LA90_1==NOT||LA90_1==111) ) {
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
			case 57:
			case 71:
				{
				alt90=2;
				}
				break;
			case GROUP:
				{
				int LA90_3 = input.LA(2);
				if ( (LA90_3==62) ) {
					alt90=1;
				}
				else if ( (LA90_3==NOT||LA90_3==111) ) {
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
					// JPA2.g:326:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_or_value_expression3006);
					path_expression333=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression333.getTree());

					}
					break;
				case 2 :
					// JPA2.g:327:7: simple_entity_or_value_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression3014);
					simple_entity_or_value_expression334=simple_entity_or_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_or_value_expression334.getTree());

					}
					break;
				case 3 :
					// JPA2.g:328:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_entity_or_value_expression3022);
					subquery335=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery335.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:329:1: simple_entity_or_value_expression : ( identification_variable | input_parameter | literal );
	public final JPA2Parser.simple_entity_or_value_expression_return simple_entity_or_value_expression() throws RecognitionException {
		JPA2Parser.simple_entity_or_value_expression_return retval = new JPA2Parser.simple_entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable336 =null;
		ParserRuleReturnScope input_parameter337 =null;
		ParserRuleReturnScope literal338 =null;


		try {
			// JPA2.g:330:5: ( identification_variable | input_parameter | literal )
			int alt91=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA91_1 = input.LA(2);
				if ( (synpred148_JPA2()) ) {
					alt91=1;
				}
				else if ( (true) ) {
					alt91=3;
				}

				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
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
					// JPA2.g:330:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_or_value_expression3033);
					identification_variable336=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable336.getTree());

					}
					break;
				case 2 :
					// JPA2.g:331:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_or_value_expression3041);
					input_parameter337=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter337.getTree());

					}
					break;
				case 3 :
					// JPA2.g:332:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_simple_entity_or_value_expression3049);
					literal338=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal338.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:333:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
	public final JPA2Parser.exists_expression_return exists_expression() throws RecognitionException {
		JPA2Parser.exists_expression_return retval = new JPA2Parser.exists_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal339=null;
		Token string_literal340=null;
		ParserRuleReturnScope subquery341 =null;

		Object string_literal339_tree=null;
		Object string_literal340_tree=null;

		try {
			// JPA2.g:334:5: ( ( 'NOT' )? 'EXISTS' subquery )
			// JPA2.g:334:7: ( 'NOT' )? 'EXISTS' subquery
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:334:7: ( 'NOT' )?
			int alt92=2;
			int LA92_0 = input.LA(1);
			if ( (LA92_0==NOT) ) {
				alt92=1;
			}
			switch (alt92) {
				case 1 :
					// JPA2.g:334:8: 'NOT'
					{
					string_literal339=(Token)match(input,NOT,FOLLOW_NOT_in_exists_expression3061); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal339_tree = (Object)adaptor.create(string_literal339);
					adaptor.addChild(root_0, string_literal339_tree);
					}

					}
					break;

			}

			string_literal340=(Token)match(input,99,FOLLOW_99_in_exists_expression3065); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal340_tree = (Object)adaptor.create(string_literal340);
			adaptor.addChild(root_0, string_literal340_tree);
			}

			pushFollow(FOLLOW_subquery_in_exists_expression3067);
			subquery341=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery341.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:335:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
	public final JPA2Parser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
		JPA2Parser.all_or_any_expression_return retval = new JPA2Parser.all_or_any_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set342=null;
		ParserRuleReturnScope subquery343 =null;

		Object set342_tree=null;

		try {
			// JPA2.g:336:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
			// JPA2.g:336:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
			{
			root_0 = (Object)adaptor.nil();


			set342=input.LT(1);
			if ( (input.LA(1) >= 79 && input.LA(1) <= 80)||input.LA(1)==129 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set342));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_subquery_in_all_or_any_expression3091);
			subquery343=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery343.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:337:1: comparison_expression : ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
	public final JPA2Parser.comparison_expression_return comparison_expression() throws RecognitionException {
		JPA2Parser.comparison_expression_return retval = new JPA2Parser.comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal346=null;
		Token set350=null;
		Token set354=null;
		Token set362=null;
		Token set366=null;
		ParserRuleReturnScope string_expression344 =null;
		ParserRuleReturnScope comparison_operator345 =null;
		ParserRuleReturnScope string_expression347 =null;
		ParserRuleReturnScope all_or_any_expression348 =null;
		ParserRuleReturnScope boolean_expression349 =null;
		ParserRuleReturnScope boolean_expression351 =null;
		ParserRuleReturnScope all_or_any_expression352 =null;
		ParserRuleReturnScope enum_expression353 =null;
		ParserRuleReturnScope enum_expression355 =null;
		ParserRuleReturnScope all_or_any_expression356 =null;
		ParserRuleReturnScope datetime_expression357 =null;
		ParserRuleReturnScope comparison_operator358 =null;
		ParserRuleReturnScope datetime_expression359 =null;
		ParserRuleReturnScope all_or_any_expression360 =null;
		ParserRuleReturnScope entity_expression361 =null;
		ParserRuleReturnScope entity_expression363 =null;
		ParserRuleReturnScope all_or_any_expression364 =null;
		ParserRuleReturnScope entity_type_expression365 =null;
		ParserRuleReturnScope entity_type_expression367 =null;
		ParserRuleReturnScope arithmetic_expression368 =null;
		ParserRuleReturnScope comparison_operator369 =null;
		ParserRuleReturnScope arithmetic_expression370 =null;
		ParserRuleReturnScope all_or_any_expression371 =null;

		Object string_literal346_tree=null;
		Object set350_tree=null;
		Object set354_tree=null;
		Object set362_tree=null;
		Object set366_tree=null;

		try {
			// JPA2.g:338:5: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
			int alt100=7;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA100_1 = input.LA(2);
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred161_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (synpred166_JPA2()) ) {
					alt100=5;
				}
				else if ( (synpred168_JPA2()) ) {
					alt100=6;
				}
				else if ( (true) ) {
					alt100=7;
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
				alt100=1;
				}
				break;
			case 71:
				{
				int LA100_3 = input.LA(2);
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred161_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (synpred166_JPA2()) ) {
					alt100=5;
				}
				else if ( (synpred168_JPA2()) ) {
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
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred161_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (synpred166_JPA2()) ) {
					alt100=5;
				}
				else if ( (synpred168_JPA2()) ) {
					alt100=6;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 57:
				{
				int LA100_5 = input.LA(2);
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred161_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (synpred166_JPA2()) ) {
					alt100=5;
				}
				else if ( (synpred168_JPA2()) ) {
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
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred163_JPA2()) ) {
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
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 102:
				{
				int LA100_13 = input.LA(2);
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 84:
				{
				int LA100_14 = input.LA(2);
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred161_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 86:
				{
				int LA100_15 = input.LA(2);
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred161_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 118:
				{
				int LA100_16 = input.LA(2);
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred161_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 85:
				{
				int LA100_17 = input.LA(2);
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 100:
				{
				int LA100_18 = input.LA(2);
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 76:
				{
				int LA100_19 = input.LA(2);
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred163_JPA2()) ) {
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
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred161_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred163_JPA2()) ) {
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
				if ( (synpred155_JPA2()) ) {
					alt100=1;
				}
				else if ( (synpred158_JPA2()) ) {
					alt100=2;
				}
				else if ( (synpred161_JPA2()) ) {
					alt100=3;
				}
				else if ( (synpred163_JPA2()) ) {
					alt100=4;
				}
				else if ( (synpred166_JPA2()) ) {
					alt100=5;
				}
				else if ( (true) ) {
					alt100=7;
				}

				}
				break;
			case 88:
			case 89:
			case 90:
				{
				alt100=4;
				}
				break;
			case 136:
				{
				alt100=6;
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
			case 128:
			case 130:
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
					// JPA2.g:338:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_comparison_expression3102);
					string_expression344=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression344.getTree());

					// JPA2.g:338:25: ( comparison_operator | 'REGEXP' )
					int alt93=2;
					int LA93_0 = input.LA(1);
					if ( ((LA93_0 >= 65 && LA93_0 <= 70)) ) {
						alt93=1;
					}
					else if ( (LA93_0==125) ) {
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
							// JPA2.g:338:26: comparison_operator
							{
							pushFollow(FOLLOW_comparison_operator_in_comparison_expression3105);
							comparison_operator345=comparison_operator();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator345.getTree());

							}
							break;
						case 2 :
							// JPA2.g:338:48: 'REGEXP'
							{
							string_literal346=(Token)match(input,125,FOLLOW_125_in_comparison_expression3109); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal346_tree = (Object)adaptor.create(string_literal346);
							adaptor.addChild(root_0, string_literal346_tree);
							}

							}
							break;

					}

					// JPA2.g:338:58: ( string_expression | all_or_any_expression )
					int alt94=2;
					int LA94_0 = input.LA(1);
					if ( (LA94_0==AVG||LA94_0==COUNT||LA94_0==GROUP||(LA94_0 >= LOWER && LA94_0 <= NAMED_PARAMETER)||(LA94_0 >= STRING_LITERAL && LA94_0 <= SUM)||LA94_0==WORD||LA94_0==57||LA94_0==71||LA94_0==76||(LA94_0 >= 84 && LA94_0 <= 87)||LA94_0==100||LA94_0==102||LA94_0==118||LA94_0==131||LA94_0==135||LA94_0==138) ) {
						alt94=1;
					}
					else if ( ((LA94_0 >= 79 && LA94_0 <= 80)||LA94_0==129) ) {
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
							// JPA2.g:338:59: string_expression
							{
							pushFollow(FOLLOW_string_expression_in_comparison_expression3113);
							string_expression347=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression347.getTree());

							}
							break;
						case 2 :
							// JPA2.g:338:79: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3117);
							all_or_any_expression348=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression348.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:339:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_comparison_expression3126);
					boolean_expression349=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression349.getTree());

					set350=input.LT(1);
					if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set350));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:339:39: ( boolean_expression | all_or_any_expression )
					int alt95=2;
					int LA95_0 = input.LA(1);
					if ( (LA95_0==GROUP||LA95_0==LPAREN||LA95_0==NAMED_PARAMETER||LA95_0==WORD||LA95_0==57||LA95_0==71||LA95_0==76||(LA95_0 >= 84 && LA95_0 <= 86)||LA95_0==100||LA95_0==102||LA95_0==118||(LA95_0 >= 144 && LA95_0 <= 145)) ) {
						alt95=1;
					}
					else if ( ((LA95_0 >= 79 && LA95_0 <= 80)||LA95_0==129) ) {
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
							// JPA2.g:339:40: boolean_expression
							{
							pushFollow(FOLLOW_boolean_expression_in_comparison_expression3137);
							boolean_expression351=boolean_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression351.getTree());

							}
							break;
						case 2 :
							// JPA2.g:339:61: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3141);
							all_or_any_expression352=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression352.getTree());

							}
							break;

					}

					}
					break;
				case 3 :
					// JPA2.g:340:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_comparison_expression3150);
					enum_expression353=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression353.getTree());

					set354=input.LT(1);
					if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set354));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:340:34: ( enum_expression | all_or_any_expression )
					int alt96=2;
					int LA96_0 = input.LA(1);
					if ( (LA96_0==GROUP||LA96_0==LPAREN||LA96_0==NAMED_PARAMETER||LA96_0==WORD||LA96_0==57||LA96_0==71||LA96_0==84||LA96_0==86||LA96_0==118) ) {
						alt96=1;
					}
					else if ( ((LA96_0 >= 79 && LA96_0 <= 80)||LA96_0==129) ) {
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
							// JPA2.g:340:35: enum_expression
							{
							pushFollow(FOLLOW_enum_expression_in_comparison_expression3159);
							enum_expression355=enum_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression355.getTree());

							}
							break;
						case 2 :
							// JPA2.g:340:53: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3163);
							all_or_any_expression356=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression356.getTree());

							}
							break;

					}

					}
					break;
				case 4 :
					// JPA2.g:341:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_comparison_expression3172);
					datetime_expression357=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression357.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3174);
					comparison_operator358=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator358.getTree());

					// JPA2.g:341:47: ( datetime_expression | all_or_any_expression )
					int alt97=2;
					int LA97_0 = input.LA(1);
					if ( (LA97_0==AVG||LA97_0==COUNT||LA97_0==GROUP||(LA97_0 >= LPAREN && LA97_0 <= NAMED_PARAMETER)||LA97_0==SUM||LA97_0==WORD||LA97_0==57||LA97_0==71||LA97_0==76||(LA97_0 >= 84 && LA97_0 <= 86)||(LA97_0 >= 88 && LA97_0 <= 90)||LA97_0==100||LA97_0==102||LA97_0==118) ) {
						alt97=1;
					}
					else if ( ((LA97_0 >= 79 && LA97_0 <= 80)||LA97_0==129) ) {
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
							// JPA2.g:341:48: datetime_expression
							{
							pushFollow(FOLLOW_datetime_expression_in_comparison_expression3177);
							datetime_expression359=datetime_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression359.getTree());

							}
							break;
						case 2 :
							// JPA2.g:341:70: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3181);
							all_or_any_expression360=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression360.getTree());

							}
							break;

					}

					}
					break;
				case 5 :
					// JPA2.g:342:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_expression_in_comparison_expression3190);
					entity_expression361=entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression361.getTree());

					set362=input.LT(1);
					if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set362));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:342:38: ( entity_expression | all_or_any_expression )
					int alt98=2;
					int LA98_0 = input.LA(1);
					if ( (LA98_0==GROUP||LA98_0==NAMED_PARAMETER||LA98_0==WORD||LA98_0==57||LA98_0==71) ) {
						alt98=1;
					}
					else if ( ((LA98_0 >= 79 && LA98_0 <= 80)||LA98_0==129) ) {
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
							// JPA2.g:342:39: entity_expression
							{
							pushFollow(FOLLOW_entity_expression_in_comparison_expression3201);
							entity_expression363=entity_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression363.getTree());

							}
							break;
						case 2 :
							// JPA2.g:342:59: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3205);
							all_or_any_expression364=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression364.getTree());

							}
							break;

					}

					}
					break;
				case 6 :
					// JPA2.g:343:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3214);
					entity_type_expression365=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression365.getTree());

					set366=input.LT(1);
					if ( (input.LA(1) >= 67 && input.LA(1) <= 68) ) {
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
					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3224);
					entity_type_expression367=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression367.getTree());

					}
					break;
				case 7 :
					// JPA2.g:344:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3232);
					arithmetic_expression368=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression368.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3234);
					comparison_operator369=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator369.getTree());

					// JPA2.g:344:49: ( arithmetic_expression | all_or_any_expression )
					int alt99=2;
					int LA99_0 = input.LA(1);
					if ( (LA99_0==AVG||LA99_0==COUNT||LA99_0==GROUP||LA99_0==INT_NUMERAL||(LA99_0 >= LPAREN && LA99_0 <= NAMED_PARAMETER)||LA99_0==SUM||LA99_0==WORD||LA99_0==57||LA99_0==59||LA99_0==61||LA99_0==64||LA99_0==71||LA99_0==76||LA99_0==78||(LA99_0 >= 84 && LA99_0 <= 86)||LA99_0==100||LA99_0==102||LA99_0==104||LA99_0==108||LA99_0==110||LA99_0==113||LA99_0==118||LA99_0==128||LA99_0==130) ) {
						alt99=1;
					}
					else if ( ((LA99_0 >= 79 && LA99_0 <= 80)||LA99_0==129) ) {
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
							// JPA2.g:344:50: arithmetic_expression
							{
							pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3237);
							arithmetic_expression370=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression370.getTree());

							}
							break;
						case 2 :
							// JPA2.g:344:74: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3241);
							all_or_any_expression371=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression371.getTree());

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
	// JPA2.g:346:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
	public final JPA2Parser.comparison_operator_return comparison_operator() throws RecognitionException {
		JPA2Parser.comparison_operator_return retval = new JPA2Parser.comparison_operator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set372=null;

		Object set372_tree=null;

		try {
			// JPA2.g:347:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set372=input.LT(1);
			if ( (input.LA(1) >= 65 && input.LA(1) <= 70) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set372));
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
	// JPA2.g:353:1: arithmetic_expression : ( arithmetic_term ( '+' | '-' ) arithmetic_term | arithmetic_term );
	public final JPA2Parser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
		JPA2Parser.arithmetic_expression_return retval = new JPA2Parser.arithmetic_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set374=null;
		ParserRuleReturnScope arithmetic_term373 =null;
		ParserRuleReturnScope arithmetic_term375 =null;
		ParserRuleReturnScope arithmetic_term376 =null;

		Object set374_tree=null;

		try {
			// JPA2.g:354:5: ( arithmetic_term ( '+' | '-' ) arithmetic_term | arithmetic_term )
			int alt101=2;
			switch ( input.LA(1) ) {
			case 59:
			case 61:
				{
				int LA101_1 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case GROUP:
			case WORD:
				{
				int LA101_2 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 64:
				{
				int LA101_3 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA101_4 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case LPAREN:
				{
				int LA101_5 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 71:
				{
				int LA101_6 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA101_7 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 57:
				{
				int LA101_8 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 108:
				{
				int LA101_9 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 110:
				{
				int LA101_10 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 78:
				{
				int LA101_11 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 130:
				{
				int LA101_12 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 113:
				{
				int LA101_13 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 128:
				{
				int LA101_14 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 104:
				{
				int LA101_15 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case COUNT:
				{
				int LA101_16 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA101_17 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 102:
				{
				int LA101_18 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 84:
				{
				int LA101_19 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 86:
				{
				int LA101_20 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 118:
				{
				int LA101_21 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 85:
				{
				int LA101_22 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 100:
				{
				int LA101_23 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 76:
				{
				int LA101_24 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 101, 0, input);
				throw nvae;
			}
			switch (alt101) {
				case 1 :
					// JPA2.g:354:7: arithmetic_term ( '+' | '-' ) arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3305);
					arithmetic_term373=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term373.getTree());

					set374=input.LT(1);
					if ( input.LA(1)==59||input.LA(1)==61 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set374));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3315);
					arithmetic_term375=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term375.getTree());

					}
					break;
				case 2 :
					// JPA2.g:355:7: arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3323);
					arithmetic_term376=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term376.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:356:1: arithmetic_term : ( arithmetic_factor ( '*' | '/' ) arithmetic_factor | arithmetic_factor );
	public final JPA2Parser.arithmetic_term_return arithmetic_term() throws RecognitionException {
		JPA2Parser.arithmetic_term_return retval = new JPA2Parser.arithmetic_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set378=null;
		ParserRuleReturnScope arithmetic_factor377 =null;
		ParserRuleReturnScope arithmetic_factor379 =null;
		ParserRuleReturnScope arithmetic_factor380 =null;

		Object set378_tree=null;

		try {
			// JPA2.g:357:5: ( arithmetic_factor ( '*' | '/' ) arithmetic_factor | arithmetic_factor )
			int alt102=2;
			switch ( input.LA(1) ) {
			case 59:
			case 61:
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
			case 64:
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
			case INT_NUMERAL:
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
			case 71:
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
			case 57:
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
			case 108:
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
			case 110:
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
			case 78:
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
			case 130:
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
			case 113:
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
			case 128:
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
			case 104:
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
			case 102:
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
			case 84:
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
			case 86:
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
			case 118:
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
			case 85:
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
			case 100:
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
			case 76:
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
					// JPA2.g:357:7: arithmetic_factor ( '*' | '/' ) arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3334);
					arithmetic_factor377=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor377.getTree());

					set378=input.LT(1);
					if ( input.LA(1)==58||input.LA(1)==63 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set378));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3345);
					arithmetic_factor379=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor379.getTree());

					}
					break;
				case 2 :
					// JPA2.g:358:7: arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3353);
					arithmetic_factor380=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor380.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:359:1: arithmetic_factor : ( ( '+' | '-' ) )? arithmetic_primary ;
	public final JPA2Parser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
		JPA2Parser.arithmetic_factor_return retval = new JPA2Parser.arithmetic_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set381=null;
		ParserRuleReturnScope arithmetic_primary382 =null;

		Object set381_tree=null;

		try {
			// JPA2.g:360:5: ( ( ( '+' | '-' ) )? arithmetic_primary )
			// JPA2.g:360:7: ( ( '+' | '-' ) )? arithmetic_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:360:7: ( ( '+' | '-' ) )?
			int alt103=2;
			int LA103_0 = input.LA(1);
			if ( (LA103_0==59||LA103_0==61) ) {
				alt103=1;
			}
			switch (alt103) {
				case 1 :
					// JPA2.g:
					{
					set381=input.LT(1);
					if ( input.LA(1)==59||input.LA(1)==61 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set381));
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

			pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor3376);
			arithmetic_primary382=arithmetic_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary382.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:361:1: arithmetic_primary : ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
		JPA2Parser.arithmetic_primary_return retval = new JPA2Parser.arithmetic_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal385=null;
		Token char_literal387=null;
		ParserRuleReturnScope path_expression383 =null;
		ParserRuleReturnScope numeric_literal384 =null;
		ParserRuleReturnScope arithmetic_expression386 =null;
		ParserRuleReturnScope input_parameter388 =null;
		ParserRuleReturnScope functions_returning_numerics389 =null;
		ParserRuleReturnScope aggregate_expression390 =null;
		ParserRuleReturnScope case_expression391 =null;
		ParserRuleReturnScope function_invocation392 =null;
		ParserRuleReturnScope extension_functions393 =null;
		ParserRuleReturnScope subquery394 =null;

		Object char_literal385_tree=null;
		Object char_literal387_tree=null;

		try {
			// JPA2.g:362:5: ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
			int alt104=10;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				alt104=1;
				}
				break;
			case INT_NUMERAL:
			case 64:
				{
				alt104=2;
				}
				break;
			case LPAREN:
				{
				int LA104_4 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt104=3;
				}
				else if ( (true) ) {
					alt104=10;
				}

				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt104=4;
				}
				break;
			case 78:
			case 104:
			case 108:
			case 110:
			case 113:
			case 128:
			case 130:
				{
				alt104=5;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt104=6;
				}
				break;
			case 102:
				{
				int LA104_17 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt104=6;
				}
				else if ( (synpred188_JPA2()) ) {
					alt104=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 104, 17, input);
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
				alt104=7;
				}
				break;
			case 76:
			case 85:
			case 100:
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
					// JPA2.g:362:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_arithmetic_primary3387);
					path_expression383=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression383.getTree());

					}
					break;
				case 2 :
					// JPA2.g:363:7: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary3395);
					numeric_literal384=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal384.getTree());

					}
					break;
				case 3 :
					// JPA2.g:364:7: '(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal385=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary3403); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal385_tree = (Object)adaptor.create(char_literal385);
					adaptor.addChild(root_0, char_literal385_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_arithmetic_primary3404);
					arithmetic_expression386=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression386.getTree());

					char_literal387=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary3405); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal387_tree = (Object)adaptor.create(char_literal387);
					adaptor.addChild(root_0, char_literal387_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:365:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_arithmetic_primary3413);
					input_parameter388=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter388.getTree());

					}
					break;
				case 5 :
					// JPA2.g:366:7: functions_returning_numerics
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary3421);
					functions_returning_numerics389=functions_returning_numerics();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics389.getTree());

					}
					break;
				case 6 :
					// JPA2.g:367:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary3429);
					aggregate_expression390=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression390.getTree());

					}
					break;
				case 7 :
					// JPA2.g:368:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_arithmetic_primary3437);
					case_expression391=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression391.getTree());

					}
					break;
				case 8 :
					// JPA2.g:369:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_arithmetic_primary3445);
					function_invocation392=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation392.getTree());

					}
					break;
				case 9 :
					// JPA2.g:370:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_arithmetic_primary3453);
					extension_functions393=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions393.getTree());

					}
					break;
				case 10 :
					// JPA2.g:371:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_arithmetic_primary3461);
					subquery394=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery394.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:372:1: string_expression : ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.string_expression_return string_expression() throws RecognitionException {
		JPA2Parser.string_expression_return retval = new JPA2Parser.string_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression395 =null;
		ParserRuleReturnScope string_literal396 =null;
		ParserRuleReturnScope input_parameter397 =null;
		ParserRuleReturnScope functions_returning_strings398 =null;
		ParserRuleReturnScope aggregate_expression399 =null;
		ParserRuleReturnScope case_expression400 =null;
		ParserRuleReturnScope function_invocation401 =null;
		ParserRuleReturnScope extension_functions402 =null;
		ParserRuleReturnScope subquery403 =null;


		try {
			// JPA2.g:373:5: ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
			int alt105=9;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				alt105=1;
				}
				break;
			case STRING_LITERAL:
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
			case LOWER:
			case 87:
			case 131:
			case 135:
			case 138:
				{
				alt105=4;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt105=5;
				}
				break;
			case 102:
				{
				int LA105_13 = input.LA(2);
				if ( (synpred194_JPA2()) ) {
					alt105=5;
				}
				else if ( (synpred196_JPA2()) ) {
					alt105=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 105, 13, input);
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
				alt105=6;
				}
				break;
			case 76:
			case 85:
			case 100:
				{
				alt105=8;
				}
				break;
			case LPAREN:
				{
				alt105=9;
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
					// JPA2.g:373:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_string_expression3472);
					path_expression395=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression395.getTree());

					}
					break;
				case 2 :
					// JPA2.g:374:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_string_expression3480);
					string_literal396=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal396.getTree());

					}
					break;
				case 3 :
					// JPA2.g:375:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_string_expression3488);
					input_parameter397=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter397.getTree());

					}
					break;
				case 4 :
					// JPA2.g:376:7: functions_returning_strings
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_strings_in_string_expression3496);
					functions_returning_strings398=functions_returning_strings();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings398.getTree());

					}
					break;
				case 5 :
					// JPA2.g:377:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_string_expression3504);
					aggregate_expression399=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression399.getTree());

					}
					break;
				case 6 :
					// JPA2.g:378:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_string_expression3512);
					case_expression400=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression400.getTree());

					}
					break;
				case 7 :
					// JPA2.g:379:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_string_expression3520);
					function_invocation401=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation401.getTree());

					}
					break;
				case 8 :
					// JPA2.g:380:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_string_expression3528);
					extension_functions402=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions402.getTree());

					}
					break;
				case 9 :
					// JPA2.g:381:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_string_expression3536);
					subquery403=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery403.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:382:1: datetime_expression : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery );
	public final JPA2Parser.datetime_expression_return datetime_expression() throws RecognitionException {
		JPA2Parser.datetime_expression_return retval = new JPA2Parser.datetime_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression404 =null;
		ParserRuleReturnScope input_parameter405 =null;
		ParserRuleReturnScope functions_returning_datetime406 =null;
		ParserRuleReturnScope aggregate_expression407 =null;
		ParserRuleReturnScope case_expression408 =null;
		ParserRuleReturnScope function_invocation409 =null;
		ParserRuleReturnScope extension_functions410 =null;
		ParserRuleReturnScope date_time_timestamp_literal411 =null;
		ParserRuleReturnScope subquery412 =null;


		try {
			// JPA2.g:383:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery )
			int alt106=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA106_1 = input.LA(2);
				if ( (synpred198_JPA2()) ) {
					alt106=1;
				}
				else if ( (synpred205_JPA2()) ) {
					alt106=8;
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
				alt106=2;
				}
				break;
			case 88:
			case 89:
			case 90:
				{
				alt106=3;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt106=4;
				}
				break;
			case 102:
				{
				int LA106_8 = input.LA(2);
				if ( (synpred201_JPA2()) ) {
					alt106=4;
				}
				else if ( (synpred203_JPA2()) ) {
					alt106=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 106, 8, input);
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
				alt106=5;
				}
				break;
			case 76:
			case 85:
			case 100:
				{
				alt106=7;
				}
				break;
			case GROUP:
				{
				alt106=1;
				}
				break;
			case LPAREN:
				{
				alt106=9;
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
					// JPA2.g:383:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_datetime_expression3547);
					path_expression404=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression404.getTree());

					}
					break;
				case 2 :
					// JPA2.g:384:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_datetime_expression3555);
					input_parameter405=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter405.getTree());

					}
					break;
				case 3 :
					// JPA2.g:385:7: functions_returning_datetime
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_datetime_in_datetime_expression3563);
					functions_returning_datetime406=functions_returning_datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime406.getTree());

					}
					break;
				case 4 :
					// JPA2.g:386:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_datetime_expression3571);
					aggregate_expression407=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression407.getTree());

					}
					break;
				case 5 :
					// JPA2.g:387:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_datetime_expression3579);
					case_expression408=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression408.getTree());

					}
					break;
				case 6 :
					// JPA2.g:388:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_datetime_expression3587);
					function_invocation409=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation409.getTree());

					}
					break;
				case 7 :
					// JPA2.g:389:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_datetime_expression3595);
					extension_functions410=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions410.getTree());

					}
					break;
				case 8 :
					// JPA2.g:390:7: date_time_timestamp_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_time_timestamp_literal_in_datetime_expression3603);
					date_time_timestamp_literal411=date_time_timestamp_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_time_timestamp_literal411.getTree());

					}
					break;
				case 9 :
					// JPA2.g:391:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_datetime_expression3611);
					subquery412=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery412.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:392:1: boolean_expression : ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.boolean_expression_return boolean_expression() throws RecognitionException {
		JPA2Parser.boolean_expression_return retval = new JPA2Parser.boolean_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression413 =null;
		ParserRuleReturnScope boolean_literal414 =null;
		ParserRuleReturnScope input_parameter415 =null;
		ParserRuleReturnScope case_expression416 =null;
		ParserRuleReturnScope function_invocation417 =null;
		ParserRuleReturnScope extension_functions418 =null;
		ParserRuleReturnScope subquery419 =null;


		try {
			// JPA2.g:393:5: ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery )
			int alt107=7;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				alt107=1;
				}
				break;
			case 144:
			case 145:
				{
				alt107=2;
				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt107=3;
				}
				break;
			case 84:
			case 86:
			case 118:
				{
				alt107=4;
				}
				break;
			case 102:
				{
				alt107=5;
				}
				break;
			case 76:
			case 85:
			case 100:
				{
				alt107=6;
				}
				break;
			case LPAREN:
				{
				alt107=7;
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
					// JPA2.g:393:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_boolean_expression3622);
					path_expression413=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression413.getTree());

					}
					break;
				case 2 :
					// JPA2.g:394:7: boolean_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_literal_in_boolean_expression3630);
					boolean_literal414=boolean_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal414.getTree());

					}
					break;
				case 3 :
					// JPA2.g:395:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_boolean_expression3638);
					input_parameter415=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter415.getTree());

					}
					break;
				case 4 :
					// JPA2.g:396:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_boolean_expression3646);
					case_expression416=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression416.getTree());

					}
					break;
				case 5 :
					// JPA2.g:397:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_boolean_expression3654);
					function_invocation417=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation417.getTree());

					}
					break;
				case 6 :
					// JPA2.g:398:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_boolean_expression3662);
					extension_functions418=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions418.getTree());

					}
					break;
				case 7 :
					// JPA2.g:399:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_boolean_expression3670);
					subquery419=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery419.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:400:1: enum_expression : ( path_expression | enum_literal | input_parameter | case_expression | subquery );
	public final JPA2Parser.enum_expression_return enum_expression() throws RecognitionException {
		JPA2Parser.enum_expression_return retval = new JPA2Parser.enum_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression420 =null;
		ParserRuleReturnScope enum_literal421 =null;
		ParserRuleReturnScope input_parameter422 =null;
		ParserRuleReturnScope case_expression423 =null;
		ParserRuleReturnScope subquery424 =null;


		try {
			// JPA2.g:401:5: ( path_expression | enum_literal | input_parameter | case_expression | subquery )
			int alt108=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA108_1 = input.LA(2);
				if ( (LA108_1==62) ) {
					alt108=1;
				}
				else if ( (LA108_1==EOF||(LA108_1 >= AND && LA108_1 <= ASC)||LA108_1==DESC||(LA108_1 >= GROUP && LA108_1 <= HAVING)||LA108_1==INNER||(LA108_1 >= JOIN && LA108_1 <= LEFT)||(LA108_1 >= OR && LA108_1 <= ORDER)||LA108_1==RPAREN||LA108_1==SET||LA108_1==WORD||LA108_1==60||(LA108_1 >= 67 && LA108_1 <= 68)||LA108_1==81||LA108_1==93||LA108_1==95||LA108_1==101||(LA108_1 >= 119 && LA108_1 <= 120)||LA108_1==132||(LA108_1 >= 141 && LA108_1 <= 142)) ) {
					alt108=2;
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
			case GROUP:
				{
				alt108=1;
				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt108=3;
				}
				break;
			case 84:
			case 86:
			case 118:
				{
				alt108=4;
				}
				break;
			case LPAREN:
				{
				alt108=5;
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
					// JPA2.g:401:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_enum_expression3681);
					path_expression420=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression420.getTree());

					}
					break;
				case 2 :
					// JPA2.g:402:7: enum_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_literal_in_enum_expression3689);
					enum_literal421=enum_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal421.getTree());

					}
					break;
				case 3 :
					// JPA2.g:403:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_enum_expression3697);
					input_parameter422=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter422.getTree());

					}
					break;
				case 4 :
					// JPA2.g:404:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_enum_expression3705);
					case_expression423=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression423.getTree());

					}
					break;
				case 5 :
					// JPA2.g:405:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_enum_expression3713);
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
	// $ANTLR end "enum_expression"


	public static class entity_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_expression"
	// JPA2.g:406:1: entity_expression : ( path_expression | simple_entity_expression );
	public final JPA2Parser.entity_expression_return entity_expression() throws RecognitionException {
		JPA2Parser.entity_expression_return retval = new JPA2Parser.entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression425 =null;
		ParserRuleReturnScope simple_entity_expression426 =null;


		try {
			// JPA2.g:407:5: ( path_expression | simple_entity_expression )
			int alt109=2;
			int LA109_0 = input.LA(1);
			if ( (LA109_0==GROUP||LA109_0==WORD) ) {
				int LA109_1 = input.LA(2);
				if ( (LA109_1==62) ) {
					alt109=1;
				}
				else if ( (LA109_1==EOF||LA109_1==AND||(LA109_1 >= GROUP && LA109_1 <= HAVING)||LA109_1==INNER||(LA109_1 >= JOIN && LA109_1 <= LEFT)||(LA109_1 >= OR && LA109_1 <= ORDER)||LA109_1==RPAREN||LA109_1==SET||LA109_1==60||(LA109_1 >= 67 && LA109_1 <= 68)||LA109_1==132||LA109_1==142) ) {
					alt109=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 109, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA109_0==NAMED_PARAMETER||LA109_0==57||LA109_0==71) ) {
				alt109=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 109, 0, input);
				throw nvae;
			}

			switch (alt109) {
				case 1 :
					// JPA2.g:407:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_expression3724);
					path_expression425=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression425.getTree());

					}
					break;
				case 2 :
					// JPA2.g:408:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3732);
					simple_entity_expression426=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression426.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:409:1: simple_entity_expression : ( identification_variable | input_parameter );
	public final JPA2Parser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
		JPA2Parser.simple_entity_expression_return retval = new JPA2Parser.simple_entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable427 =null;
		ParserRuleReturnScope input_parameter428 =null;


		try {
			// JPA2.g:410:5: ( identification_variable | input_parameter )
			int alt110=2;
			int LA110_0 = input.LA(1);
			if ( (LA110_0==GROUP||LA110_0==WORD) ) {
				alt110=1;
			}
			else if ( (LA110_0==NAMED_PARAMETER||LA110_0==57||LA110_0==71) ) {
				alt110=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 110, 0, input);
				throw nvae;
			}

			switch (alt110) {
				case 1 :
					// JPA2.g:410:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3743);
					identification_variable427=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable427.getTree());

					}
					break;
				case 2 :
					// JPA2.g:411:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3751);
					input_parameter428=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter428.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:412:1: entity_type_expression : ( type_discriminator | entity_type_literal | input_parameter );
	public final JPA2Parser.entity_type_expression_return entity_type_expression() throws RecognitionException {
		JPA2Parser.entity_type_expression_return retval = new JPA2Parser.entity_type_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope type_discriminator429 =null;
		ParserRuleReturnScope entity_type_literal430 =null;
		ParserRuleReturnScope input_parameter431 =null;


		try {
			// JPA2.g:413:5: ( type_discriminator | entity_type_literal | input_parameter )
			int alt111=3;
			switch ( input.LA(1) ) {
			case 136:
				{
				alt111=1;
				}
				break;
			case WORD:
				{
				alt111=2;
				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt111=3;
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
					// JPA2.g:413:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_entity_type_expression3762);
					type_discriminator429=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator429.getTree());

					}
					break;
				case 2 :
					// JPA2.g:414:7: entity_type_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_literal_in_entity_type_expression3770);
					entity_type_literal430=entity_type_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_literal430.getTree());

					}
					break;
				case 3 :
					// JPA2.g:415:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_entity_type_expression3778);
					input_parameter431=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter431.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:416:1: type_discriminator : 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' ;
	public final JPA2Parser.type_discriminator_return type_discriminator() throws RecognitionException {
		JPA2Parser.type_discriminator_return retval = new JPA2Parser.type_discriminator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal432=null;
		Token char_literal436=null;
		ParserRuleReturnScope general_identification_variable433 =null;
		ParserRuleReturnScope path_expression434 =null;
		ParserRuleReturnScope input_parameter435 =null;

		Object string_literal432_tree=null;
		Object char_literal436_tree=null;

		try {
			// JPA2.g:417:5: ( 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' )
			// JPA2.g:417:7: 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal432=(Token)match(input,136,FOLLOW_136_in_type_discriminator3789); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal432_tree = (Object)adaptor.create(string_literal432);
			adaptor.addChild(root_0, string_literal432_tree);
			}

			// JPA2.g:417:15: ( general_identification_variable | path_expression | input_parameter )
			int alt112=3;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				int LA112_1 = input.LA(2);
				if ( (LA112_1==RPAREN) ) {
					alt112=1;
				}
				else if ( (LA112_1==62) ) {
					alt112=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 112, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 106:
			case 139:
				{
				alt112=1;
				}
				break;
			case NAMED_PARAMETER:
			case 57:
			case 71:
				{
				alt112=3;
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
					// JPA2.g:417:16: general_identification_variable
					{
					pushFollow(FOLLOW_general_identification_variable_in_type_discriminator3792);
					general_identification_variable433=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable433.getTree());

					}
					break;
				case 2 :
					// JPA2.g:417:50: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_type_discriminator3796);
					path_expression434=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression434.getTree());

					}
					break;
				case 3 :
					// JPA2.g:417:68: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_type_discriminator3800);
					input_parameter435=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter435.getTree());

					}
					break;

			}

			char_literal436=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_type_discriminator3803); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal436_tree = (Object)adaptor.create(char_literal436);
			adaptor.addChild(root_0, char_literal436_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:418:1: functions_returning_numerics : ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' );
	public final JPA2Parser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
		JPA2Parser.functions_returning_numerics_return retval = new JPA2Parser.functions_returning_numerics_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal437=null;
		Token char_literal439=null;
		Token string_literal440=null;
		Token char_literal442=null;
		Token char_literal444=null;
		Token char_literal446=null;
		Token string_literal447=null;
		Token char_literal449=null;
		Token string_literal450=null;
		Token char_literal452=null;
		Token string_literal453=null;
		Token char_literal455=null;
		Token char_literal457=null;
		Token string_literal458=null;
		Token char_literal460=null;
		Token string_literal461=null;
		Token char_literal463=null;
		ParserRuleReturnScope string_expression438 =null;
		ParserRuleReturnScope string_expression441 =null;
		ParserRuleReturnScope string_expression443 =null;
		ParserRuleReturnScope arithmetic_expression445 =null;
		ParserRuleReturnScope arithmetic_expression448 =null;
		ParserRuleReturnScope arithmetic_expression451 =null;
		ParserRuleReturnScope arithmetic_expression454 =null;
		ParserRuleReturnScope arithmetic_expression456 =null;
		ParserRuleReturnScope path_expression459 =null;
		ParserRuleReturnScope identification_variable462 =null;

		Object string_literal437_tree=null;
		Object char_literal439_tree=null;
		Object string_literal440_tree=null;
		Object char_literal442_tree=null;
		Object char_literal444_tree=null;
		Object char_literal446_tree=null;
		Object string_literal447_tree=null;
		Object char_literal449_tree=null;
		Object string_literal450_tree=null;
		Object char_literal452_tree=null;
		Object string_literal453_tree=null;
		Object char_literal455_tree=null;
		Object char_literal457_tree=null;
		Object string_literal458_tree=null;
		Object char_literal460_tree=null;
		Object string_literal461_tree=null;
		Object char_literal463_tree=null;

		try {
			// JPA2.g:419:5: ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' )
			int alt114=7;
			switch ( input.LA(1) ) {
			case 108:
				{
				alt114=1;
				}
				break;
			case 110:
				{
				alt114=2;
				}
				break;
			case 78:
				{
				alt114=3;
				}
				break;
			case 130:
				{
				alt114=4;
				}
				break;
			case 113:
				{
				alt114=5;
				}
				break;
			case 128:
				{
				alt114=6;
				}
				break;
			case 104:
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
					// JPA2.g:419:7: 'LENGTH(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal437=(Token)match(input,108,FOLLOW_108_in_functions_returning_numerics3814); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal437_tree = (Object)adaptor.create(string_literal437);
					adaptor.addChild(root_0, string_literal437_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3815);
					string_expression438=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression438.getTree());

					char_literal439=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3816); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal439_tree = (Object)adaptor.create(char_literal439);
					adaptor.addChild(root_0, char_literal439_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:420:7: 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal440=(Token)match(input,110,FOLLOW_110_in_functions_returning_numerics3824); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal440_tree = (Object)adaptor.create(string_literal440);
					adaptor.addChild(root_0, string_literal440_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3826);
					string_expression441=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression441.getTree());

					char_literal442=(Token)match(input,60,FOLLOW_60_in_functions_returning_numerics3827); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal442_tree = (Object)adaptor.create(char_literal442);
					adaptor.addChild(root_0, char_literal442_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3829);
					string_expression443=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression443.getTree());

					// JPA2.g:420:55: ( ',' arithmetic_expression )?
					int alt113=2;
					int LA113_0 = input.LA(1);
					if ( (LA113_0==60) ) {
						alt113=1;
					}
					switch (alt113) {
						case 1 :
							// JPA2.g:420:56: ',' arithmetic_expression
							{
							char_literal444=(Token)match(input,60,FOLLOW_60_in_functions_returning_numerics3831); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal444_tree = (Object)adaptor.create(char_literal444);
							adaptor.addChild(root_0, char_literal444_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3832);
							arithmetic_expression445=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression445.getTree());

							}
							break;

					}

					char_literal446=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3835); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal446_tree = (Object)adaptor.create(char_literal446);
					adaptor.addChild(root_0, char_literal446_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:421:7: 'ABS(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal447=(Token)match(input,78,FOLLOW_78_in_functions_returning_numerics3843); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal447_tree = (Object)adaptor.create(string_literal447);
					adaptor.addChild(root_0, string_literal447_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3844);
					arithmetic_expression448=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression448.getTree());

					char_literal449=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3845); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal449_tree = (Object)adaptor.create(char_literal449);
					adaptor.addChild(root_0, char_literal449_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:422:7: 'SQRT(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal450=(Token)match(input,130,FOLLOW_130_in_functions_returning_numerics3853); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal450_tree = (Object)adaptor.create(string_literal450);
					adaptor.addChild(root_0, string_literal450_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3854);
					arithmetic_expression451=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression451.getTree());

					char_literal452=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3855); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal452_tree = (Object)adaptor.create(char_literal452);
					adaptor.addChild(root_0, char_literal452_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:423:7: 'MOD(' arithmetic_expression ',' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal453=(Token)match(input,113,FOLLOW_113_in_functions_returning_numerics3863); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal453_tree = (Object)adaptor.create(string_literal453);
					adaptor.addChild(root_0, string_literal453_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3864);
					arithmetic_expression454=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression454.getTree());

					char_literal455=(Token)match(input,60,FOLLOW_60_in_functions_returning_numerics3865); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal455_tree = (Object)adaptor.create(char_literal455);
					adaptor.addChild(root_0, char_literal455_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3867);
					arithmetic_expression456=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression456.getTree());

					char_literal457=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3868); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal457_tree = (Object)adaptor.create(char_literal457);
					adaptor.addChild(root_0, char_literal457_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:424:7: 'SIZE(' path_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal458=(Token)match(input,128,FOLLOW_128_in_functions_returning_numerics3876); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal458_tree = (Object)adaptor.create(string_literal458);
					adaptor.addChild(root_0, string_literal458_tree);
					}

					pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3877);
					path_expression459=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression459.getTree());

					char_literal460=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3878); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal460_tree = (Object)adaptor.create(char_literal460);
					adaptor.addChild(root_0, char_literal460_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:425:7: 'INDEX(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal461=(Token)match(input,104,FOLLOW_104_in_functions_returning_numerics3886); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal461_tree = (Object)adaptor.create(string_literal461);
					adaptor.addChild(root_0, string_literal461_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_functions_returning_numerics3887);
					identification_variable462=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable462.getTree());

					char_literal463=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3888); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal463_tree = (Object)adaptor.create(char_literal463);
					adaptor.addChild(root_0, char_literal463_tree);
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
	// JPA2.g:426:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
	public final JPA2Parser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
		JPA2Parser.functions_returning_datetime_return retval = new JPA2Parser.functions_returning_datetime_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set464=null;

		Object set464_tree=null;

		try {
			// JPA2.g:427:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set464=input.LT(1);
			if ( (input.LA(1) >= 88 && input.LA(1) <= 90) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set464));
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
	// JPA2.g:430:1: functions_returning_strings : ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' );
	public final JPA2Parser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
		JPA2Parser.functions_returning_strings_return retval = new JPA2Parser.functions_returning_strings_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal465=null;
		Token char_literal467=null;
		Token char_literal469=null;
		Token char_literal471=null;
		Token string_literal472=null;
		Token char_literal474=null;
		Token char_literal476=null;
		Token char_literal478=null;
		Token string_literal479=null;
		Token string_literal482=null;
		Token char_literal484=null;
		Token string_literal485=null;
		Token char_literal486=null;
		Token char_literal488=null;
		Token string_literal489=null;
		Token char_literal491=null;
		ParserRuleReturnScope string_expression466 =null;
		ParserRuleReturnScope string_expression468 =null;
		ParserRuleReturnScope string_expression470 =null;
		ParserRuleReturnScope string_expression473 =null;
		ParserRuleReturnScope arithmetic_expression475 =null;
		ParserRuleReturnScope arithmetic_expression477 =null;
		ParserRuleReturnScope trim_specification480 =null;
		ParserRuleReturnScope trim_character481 =null;
		ParserRuleReturnScope string_expression483 =null;
		ParserRuleReturnScope string_expression487 =null;
		ParserRuleReturnScope string_expression490 =null;

		Object string_literal465_tree=null;
		Object char_literal467_tree=null;
		Object char_literal469_tree=null;
		Object char_literal471_tree=null;
		Object string_literal472_tree=null;
		Object char_literal474_tree=null;
		Object char_literal476_tree=null;
		Object char_literal478_tree=null;
		Object string_literal479_tree=null;
		Object string_literal482_tree=null;
		Object char_literal484_tree=null;
		Object string_literal485_tree=null;
		Object char_literal486_tree=null;
		Object char_literal488_tree=null;
		Object string_literal489_tree=null;
		Object char_literal491_tree=null;

		try {
			// JPA2.g:431:5: ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' )
			int alt120=5;
			switch ( input.LA(1) ) {
			case 87:
				{
				alt120=1;
				}
				break;
			case 131:
				{
				alt120=2;
				}
				break;
			case 135:
				{
				alt120=3;
				}
				break;
			case LOWER:
				{
				alt120=4;
				}
				break;
			case 138:
				{
				alt120=5;
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
					// JPA2.g:431:7: 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal465=(Token)match(input,87,FOLLOW_87_in_functions_returning_strings3926); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal465_tree = (Object)adaptor.create(string_literal465);
					adaptor.addChild(root_0, string_literal465_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3927);
					string_expression466=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression466.getTree());

					char_literal467=(Token)match(input,60,FOLLOW_60_in_functions_returning_strings3928); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal467_tree = (Object)adaptor.create(char_literal467);
					adaptor.addChild(root_0, char_literal467_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3930);
					string_expression468=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression468.getTree());

					// JPA2.g:431:55: ( ',' string_expression )*
					loop115:
					while (true) {
						int alt115=2;
						int LA115_0 = input.LA(1);
						if ( (LA115_0==60) ) {
							alt115=1;
						}

						switch (alt115) {
						case 1 :
							// JPA2.g:431:56: ',' string_expression
							{
							char_literal469=(Token)match(input,60,FOLLOW_60_in_functions_returning_strings3933); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal469_tree = (Object)adaptor.create(char_literal469);
							adaptor.addChild(root_0, char_literal469_tree);
							}

							pushFollow(FOLLOW_string_expression_in_functions_returning_strings3935);
							string_expression470=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression470.getTree());

							}
							break;

						default :
							break loop115;
						}
					}

					char_literal471=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3938); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal471_tree = (Object)adaptor.create(char_literal471);
					adaptor.addChild(root_0, char_literal471_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:432:7: 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal472=(Token)match(input,131,FOLLOW_131_in_functions_returning_strings3946); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal472_tree = (Object)adaptor.create(string_literal472);
					adaptor.addChild(root_0, string_literal472_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3948);
					string_expression473=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression473.getTree());

					char_literal474=(Token)match(input,60,FOLLOW_60_in_functions_returning_strings3949); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal474_tree = (Object)adaptor.create(char_literal474);
					adaptor.addChild(root_0, char_literal474_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3951);
					arithmetic_expression475=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression475.getTree());

					// JPA2.g:432:63: ( ',' arithmetic_expression )?
					int alt116=2;
					int LA116_0 = input.LA(1);
					if ( (LA116_0==60) ) {
						alt116=1;
					}
					switch (alt116) {
						case 1 :
							// JPA2.g:432:64: ',' arithmetic_expression
							{
							char_literal476=(Token)match(input,60,FOLLOW_60_in_functions_returning_strings3954); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal476_tree = (Object)adaptor.create(char_literal476);
							adaptor.addChild(root_0, char_literal476_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3956);
							arithmetic_expression477=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression477.getTree());

							}
							break;

					}

					char_literal478=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3959); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal478_tree = (Object)adaptor.create(char_literal478);
					adaptor.addChild(root_0, char_literal478_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:433:7: 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal479=(Token)match(input,135,FOLLOW_135_in_functions_returning_strings3967); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal479_tree = (Object)adaptor.create(string_literal479);
					adaptor.addChild(root_0, string_literal479_tree);
					}

					// JPA2.g:433:14: ( ( trim_specification )? ( trim_character )? 'FROM' )?
					int alt119=2;
					int LA119_0 = input.LA(1);
					if ( (LA119_0==TRIM_CHARACTER||LA119_0==83||LA119_0==101||LA119_0==107||LA119_0==133) ) {
						alt119=1;
					}
					switch (alt119) {
						case 1 :
							// JPA2.g:433:15: ( trim_specification )? ( trim_character )? 'FROM'
							{
							// JPA2.g:433:15: ( trim_specification )?
							int alt117=2;
							int LA117_0 = input.LA(1);
							if ( (LA117_0==83||LA117_0==107||LA117_0==133) ) {
								alt117=1;
							}
							switch (alt117) {
								case 1 :
									// JPA2.g:433:16: trim_specification
									{
									pushFollow(FOLLOW_trim_specification_in_functions_returning_strings3970);
									trim_specification480=trim_specification();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification480.getTree());

									}
									break;

							}

							// JPA2.g:433:37: ( trim_character )?
							int alt118=2;
							int LA118_0 = input.LA(1);
							if ( (LA118_0==TRIM_CHARACTER) ) {
								alt118=1;
							}
							switch (alt118) {
								case 1 :
									// JPA2.g:433:38: trim_character
									{
									pushFollow(FOLLOW_trim_character_in_functions_returning_strings3975);
									trim_character481=trim_character();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_character481.getTree());

									}
									break;

							}

							string_literal482=(Token)match(input,101,FOLLOW_101_in_functions_returning_strings3979); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal482_tree = (Object)adaptor.create(string_literal482);
							adaptor.addChild(root_0, string_literal482_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3983);
					string_expression483=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression483.getTree());

					char_literal484=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3985); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal484_tree = (Object)adaptor.create(char_literal484);
					adaptor.addChild(root_0, char_literal484_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:434:7: 'LOWER' '(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal485=(Token)match(input,LOWER,FOLLOW_LOWER_in_functions_returning_strings3993); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal485_tree = (Object)adaptor.create(string_literal485);
					adaptor.addChild(root_0, string_literal485_tree);
					}

					char_literal486=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3995); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal486_tree = (Object)adaptor.create(char_literal486);
					adaptor.addChild(root_0, char_literal486_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3996);
					string_expression487=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression487.getTree());

					char_literal488=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3997); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal488_tree = (Object)adaptor.create(char_literal488);
					adaptor.addChild(root_0, char_literal488_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:435:7: 'UPPER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal489=(Token)match(input,138,FOLLOW_138_in_functions_returning_strings4005); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal489_tree = (Object)adaptor.create(string_literal489);
					adaptor.addChild(root_0, string_literal489_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4006);
					string_expression490=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression490.getTree());

					char_literal491=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4007); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal491_tree = (Object)adaptor.create(char_literal491);
					adaptor.addChild(root_0, char_literal491_tree);
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
	// JPA2.g:436:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
	public final JPA2Parser.trim_specification_return trim_specification() throws RecognitionException {
		JPA2Parser.trim_specification_return retval = new JPA2Parser.trim_specification_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set492=null;

		Object set492_tree=null;

		try {
			// JPA2.g:437:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set492=input.LT(1);
			if ( input.LA(1)==83||input.LA(1)==107||input.LA(1)==133 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set492));
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
	// JPA2.g:438:1: function_invocation : 'FUNCTION(' function_name ( ',' function_arg )* ')' ;
	public final JPA2Parser.function_invocation_return function_invocation() throws RecognitionException {
		JPA2Parser.function_invocation_return retval = new JPA2Parser.function_invocation_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal493=null;
		Token char_literal495=null;
		Token char_literal497=null;
		ParserRuleReturnScope function_name494 =null;
		ParserRuleReturnScope function_arg496 =null;

		Object string_literal493_tree=null;
		Object char_literal495_tree=null;
		Object char_literal497_tree=null;

		try {
			// JPA2.g:439:5: ( 'FUNCTION(' function_name ( ',' function_arg )* ')' )
			// JPA2.g:439:7: 'FUNCTION(' function_name ( ',' function_arg )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal493=(Token)match(input,102,FOLLOW_102_in_function_invocation4037); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal493_tree = (Object)adaptor.create(string_literal493);
			adaptor.addChild(root_0, string_literal493_tree);
			}

			pushFollow(FOLLOW_function_name_in_function_invocation4038);
			function_name494=function_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_name494.getTree());

			// JPA2.g:439:32: ( ',' function_arg )*
			loop121:
			while (true) {
				int alt121=2;
				int LA121_0 = input.LA(1);
				if ( (LA121_0==60) ) {
					alt121=1;
				}

				switch (alt121) {
				case 1 :
					// JPA2.g:439:33: ',' function_arg
					{
					char_literal495=(Token)match(input,60,FOLLOW_60_in_function_invocation4041); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal495_tree = (Object)adaptor.create(char_literal495);
					adaptor.addChild(root_0, char_literal495_tree);
					}

					pushFollow(FOLLOW_function_arg_in_function_invocation4043);
					function_arg496=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg496.getTree());

					}
					break;

				default :
					break loop121;
				}
			}

			char_literal497=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function_invocation4047); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal497_tree = (Object)adaptor.create(char_literal497);
			adaptor.addChild(root_0, char_literal497_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:440:1: function_arg : ( literal | path_expression | input_parameter | scalar_expression );
	public final JPA2Parser.function_arg_return function_arg() throws RecognitionException {
		JPA2Parser.function_arg_return retval = new JPA2Parser.function_arg_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal498 =null;
		ParserRuleReturnScope path_expression499 =null;
		ParserRuleReturnScope input_parameter500 =null;
		ParserRuleReturnScope scalar_expression501 =null;


		try {
			// JPA2.g:441:5: ( literal | path_expression | input_parameter | scalar_expression )
			int alt122=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA122_1 = input.LA(2);
				if ( (LA122_1==62) ) {
					alt122=2;
				}
				else if ( (synpred243_JPA2()) ) {
					alt122=1;
				}
				else if ( (true) ) {
					alt122=4;
				}

				}
				break;
			case GROUP:
				{
				int LA122_2 = input.LA(2);
				if ( (LA122_2==62) ) {
					int LA122_9 = input.LA(3);
					if ( (synpred244_JPA2()) ) {
						alt122=2;
					}
					else if ( (true) ) {
						alt122=4;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 122, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 71:
				{
				int LA122_3 = input.LA(2);
				if ( (LA122_3==64) ) {
					int LA122_10 = input.LA(3);
					if ( (LA122_10==INT_NUMERAL) ) {
						int LA122_14 = input.LA(4);
						if ( (synpred245_JPA2()) ) {
							alt122=3;
						}
						else if ( (true) ) {
							alt122=4;
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
								new NoViableAltException("", 122, 10, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA122_3==INT_NUMERAL) ) {
					int LA122_11 = input.LA(3);
					if ( (synpred245_JPA2()) ) {
						alt122=3;
					}
					else if ( (true) ) {
						alt122=4;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 122, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA122_4 = input.LA(2);
				if ( (synpred245_JPA2()) ) {
					alt122=3;
				}
				else if ( (true) ) {
					alt122=4;
				}

				}
				break;
			case 57:
				{
				int LA122_5 = input.LA(2);
				if ( (LA122_5==WORD) ) {
					int LA122_13 = input.LA(3);
					if ( (LA122_13==146) ) {
						int LA122_15 = input.LA(4);
						if ( (synpred245_JPA2()) ) {
							alt122=3;
						}
						else if ( (true) ) {
							alt122=4;
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
								new NoViableAltException("", 122, 13, input);
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
							new NoViableAltException("", 122, 5, input);
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
			case 128:
			case 130:
			case 131:
			case 135:
			case 136:
			case 138:
			case 144:
			case 145:
				{
				alt122=4;
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
					// JPA2.g:441:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_function_arg4058);
					literal498=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal498.getTree());

					}
					break;
				case 2 :
					// JPA2.g:442:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_function_arg4066);
					path_expression499=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression499.getTree());

					}
					break;
				case 3 :
					// JPA2.g:443:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_function_arg4074);
					input_parameter500=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter500.getTree());

					}
					break;
				case 4 :
					// JPA2.g:444:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_function_arg4082);
					scalar_expression501=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression501.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:445:1: case_expression : ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression );
	public final JPA2Parser.case_expression_return case_expression() throws RecognitionException {
		JPA2Parser.case_expression_return retval = new JPA2Parser.case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope general_case_expression502 =null;
		ParserRuleReturnScope simple_case_expression503 =null;
		ParserRuleReturnScope coalesce_expression504 =null;
		ParserRuleReturnScope nullif_expression505 =null;


		try {
			// JPA2.g:446:5: ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression )
			int alt123=4;
			switch ( input.LA(1) ) {
			case 84:
				{
				int LA123_1 = input.LA(2);
				if ( (LA123_1==141) ) {
					alt123=1;
				}
				else if ( (LA123_1==GROUP||LA123_1==WORD||LA123_1==136) ) {
					alt123=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 123, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 86:
				{
				alt123=3;
				}
				break;
			case 118:
				{
				alt123=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 123, 0, input);
				throw nvae;
			}
			switch (alt123) {
				case 1 :
					// JPA2.g:446:7: general_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_case_expression_in_case_expression4093);
					general_case_expression502=general_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_case_expression502.getTree());

					}
					break;
				case 2 :
					// JPA2.g:447:7: simple_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_case_expression_in_case_expression4101);
					simple_case_expression503=simple_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_case_expression503.getTree());

					}
					break;
				case 3 :
					// JPA2.g:448:7: coalesce_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_coalesce_expression_in_case_expression4109);
					coalesce_expression504=coalesce_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, coalesce_expression504.getTree());

					}
					break;
				case 4 :
					// JPA2.g:449:7: nullif_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_nullif_expression_in_case_expression4117);
					nullif_expression505=nullif_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, nullif_expression505.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:450:1: general_case_expression : 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.general_case_expression_return general_case_expression() throws RecognitionException {
		JPA2Parser.general_case_expression_return retval = new JPA2Parser.general_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal506=null;
		Token string_literal509=null;
		Token string_literal511=null;
		ParserRuleReturnScope when_clause507 =null;
		ParserRuleReturnScope when_clause508 =null;
		ParserRuleReturnScope scalar_expression510 =null;

		Object string_literal506_tree=null;
		Object string_literal509_tree=null;
		Object string_literal511_tree=null;

		try {
			// JPA2.g:451:5: ( 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:451:7: 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal506=(Token)match(input,84,FOLLOW_84_in_general_case_expression4128); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal506_tree = (Object)adaptor.create(string_literal506);
			adaptor.addChild(root_0, string_literal506_tree);
			}

			pushFollow(FOLLOW_when_clause_in_general_case_expression4130);
			when_clause507=when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause507.getTree());

			// JPA2.g:451:26: ( when_clause )*
			loop124:
			while (true) {
				int alt124=2;
				int LA124_0 = input.LA(1);
				if ( (LA124_0==141) ) {
					alt124=1;
				}

				switch (alt124) {
				case 1 :
					// JPA2.g:451:27: when_clause
					{
					pushFollow(FOLLOW_when_clause_in_general_case_expression4133);
					when_clause508=when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause508.getTree());

					}
					break;

				default :
					break loop124;
				}
			}

			string_literal509=(Token)match(input,93,FOLLOW_93_in_general_case_expression4137); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal509_tree = (Object)adaptor.create(string_literal509);
			adaptor.addChild(root_0, string_literal509_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_general_case_expression4139);
			scalar_expression510=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression510.getTree());

			string_literal511=(Token)match(input,95,FOLLOW_95_in_general_case_expression4141); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal511_tree = (Object)adaptor.create(string_literal511);
			adaptor.addChild(root_0, string_literal511_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:452:1: when_clause : 'WHEN' conditional_expression 'THEN' scalar_expression ;
	public final JPA2Parser.when_clause_return when_clause() throws RecognitionException {
		JPA2Parser.when_clause_return retval = new JPA2Parser.when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal512=null;
		Token string_literal514=null;
		ParserRuleReturnScope conditional_expression513 =null;
		ParserRuleReturnScope scalar_expression515 =null;

		Object string_literal512_tree=null;
		Object string_literal514_tree=null;

		try {
			// JPA2.g:453:5: ( 'WHEN' conditional_expression 'THEN' scalar_expression )
			// JPA2.g:453:7: 'WHEN' conditional_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal512=(Token)match(input,141,FOLLOW_141_in_when_clause4152); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal512_tree = (Object)adaptor.create(string_literal512);
			adaptor.addChild(root_0, string_literal512_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_when_clause4154);
			conditional_expression513=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression513.getTree());

			string_literal514=(Token)match(input,132,FOLLOW_132_in_when_clause4156); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal514_tree = (Object)adaptor.create(string_literal514);
			adaptor.addChild(root_0, string_literal514_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_when_clause4158);
			scalar_expression515=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression515.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:454:1: simple_case_expression : 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.simple_case_expression_return simple_case_expression() throws RecognitionException {
		JPA2Parser.simple_case_expression_return retval = new JPA2Parser.simple_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal516=null;
		Token string_literal520=null;
		Token string_literal522=null;
		ParserRuleReturnScope case_operand517 =null;
		ParserRuleReturnScope simple_when_clause518 =null;
		ParserRuleReturnScope simple_when_clause519 =null;
		ParserRuleReturnScope scalar_expression521 =null;

		Object string_literal516_tree=null;
		Object string_literal520_tree=null;
		Object string_literal522_tree=null;

		try {
			// JPA2.g:455:5: ( 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:455:7: 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal516=(Token)match(input,84,FOLLOW_84_in_simple_case_expression4169); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal516_tree = (Object)adaptor.create(string_literal516);
			adaptor.addChild(root_0, string_literal516_tree);
			}

			pushFollow(FOLLOW_case_operand_in_simple_case_expression4171);
			case_operand517=case_operand();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, case_operand517.getTree());

			pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4173);
			simple_when_clause518=simple_when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause518.getTree());

			// JPA2.g:455:46: ( simple_when_clause )*
			loop125:
			while (true) {
				int alt125=2;
				int LA125_0 = input.LA(1);
				if ( (LA125_0==141) ) {
					alt125=1;
				}

				switch (alt125) {
				case 1 :
					// JPA2.g:455:47: simple_when_clause
					{
					pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4176);
					simple_when_clause519=simple_when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause519.getTree());

					}
					break;

				default :
					break loop125;
				}
			}

			string_literal520=(Token)match(input,93,FOLLOW_93_in_simple_case_expression4180); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal520_tree = (Object)adaptor.create(string_literal520);
			adaptor.addChild(root_0, string_literal520_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_case_expression4182);
			scalar_expression521=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression521.getTree());

			string_literal522=(Token)match(input,95,FOLLOW_95_in_simple_case_expression4184); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal522_tree = (Object)adaptor.create(string_literal522);
			adaptor.addChild(root_0, string_literal522_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:456:1: case_operand : ( path_expression | type_discriminator );
	public final JPA2Parser.case_operand_return case_operand() throws RecognitionException {
		JPA2Parser.case_operand_return retval = new JPA2Parser.case_operand_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression523 =null;
		ParserRuleReturnScope type_discriminator524 =null;


		try {
			// JPA2.g:457:5: ( path_expression | type_discriminator )
			int alt126=2;
			int LA126_0 = input.LA(1);
			if ( (LA126_0==GROUP||LA126_0==WORD) ) {
				alt126=1;
			}
			else if ( (LA126_0==136) ) {
				alt126=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 126, 0, input);
				throw nvae;
			}

			switch (alt126) {
				case 1 :
					// JPA2.g:457:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_case_operand4195);
					path_expression523=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression523.getTree());

					}
					break;
				case 2 :
					// JPA2.g:458:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_case_operand4203);
					type_discriminator524=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator524.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:459:1: simple_when_clause : 'WHEN' scalar_expression 'THEN' scalar_expression ;
	public final JPA2Parser.simple_when_clause_return simple_when_clause() throws RecognitionException {
		JPA2Parser.simple_when_clause_return retval = new JPA2Parser.simple_when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal525=null;
		Token string_literal527=null;
		ParserRuleReturnScope scalar_expression526 =null;
		ParserRuleReturnScope scalar_expression528 =null;

		Object string_literal525_tree=null;
		Object string_literal527_tree=null;

		try {
			// JPA2.g:460:5: ( 'WHEN' scalar_expression 'THEN' scalar_expression )
			// JPA2.g:460:7: 'WHEN' scalar_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal525=(Token)match(input,141,FOLLOW_141_in_simple_when_clause4214); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal525_tree = (Object)adaptor.create(string_literal525);
			adaptor.addChild(root_0, string_literal525_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4216);
			scalar_expression526=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression526.getTree());

			string_literal527=(Token)match(input,132,FOLLOW_132_in_simple_when_clause4218); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal527_tree = (Object)adaptor.create(string_literal527);
			adaptor.addChild(root_0, string_literal527_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4220);
			scalar_expression528=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression528.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:461:1: coalesce_expression : 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' ;
	public final JPA2Parser.coalesce_expression_return coalesce_expression() throws RecognitionException {
		JPA2Parser.coalesce_expression_return retval = new JPA2Parser.coalesce_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal529=null;
		Token char_literal531=null;
		Token char_literal533=null;
		ParserRuleReturnScope scalar_expression530 =null;
		ParserRuleReturnScope scalar_expression532 =null;

		Object string_literal529_tree=null;
		Object char_literal531_tree=null;
		Object char_literal533_tree=null;

		try {
			// JPA2.g:462:5: ( 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' )
			// JPA2.g:462:7: 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal529=(Token)match(input,86,FOLLOW_86_in_coalesce_expression4231); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal529_tree = (Object)adaptor.create(string_literal529);
			adaptor.addChild(root_0, string_literal529_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4232);
			scalar_expression530=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression530.getTree());

			// JPA2.g:462:36: ( ',' scalar_expression )+
			int cnt127=0;
			loop127:
			while (true) {
				int alt127=2;
				int LA127_0 = input.LA(1);
				if ( (LA127_0==60) ) {
					alt127=1;
				}

				switch (alt127) {
				case 1 :
					// JPA2.g:462:37: ',' scalar_expression
					{
					char_literal531=(Token)match(input,60,FOLLOW_60_in_coalesce_expression4235); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal531_tree = (Object)adaptor.create(char_literal531);
					adaptor.addChild(root_0, char_literal531_tree);
					}

					pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4237);
					scalar_expression532=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression532.getTree());

					}
					break;

				default :
					if ( cnt127 >= 1 ) break loop127;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(127, input);
					throw eee;
				}
				cnt127++;
			}

			char_literal533=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_coalesce_expression4240); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal533_tree = (Object)adaptor.create(char_literal533);
			adaptor.addChild(root_0, char_literal533_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:463:1: nullif_expression : 'NULLIF(' scalar_expression ',' scalar_expression ')' ;
	public final JPA2Parser.nullif_expression_return nullif_expression() throws RecognitionException {
		JPA2Parser.nullif_expression_return retval = new JPA2Parser.nullif_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal534=null;
		Token char_literal536=null;
		Token char_literal538=null;
		ParserRuleReturnScope scalar_expression535 =null;
		ParserRuleReturnScope scalar_expression537 =null;

		Object string_literal534_tree=null;
		Object char_literal536_tree=null;
		Object char_literal538_tree=null;

		try {
			// JPA2.g:464:5: ( 'NULLIF(' scalar_expression ',' scalar_expression ')' )
			// JPA2.g:464:7: 'NULLIF(' scalar_expression ',' scalar_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal534=(Token)match(input,118,FOLLOW_118_in_nullif_expression4251); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal534_tree = (Object)adaptor.create(string_literal534);
			adaptor.addChild(root_0, string_literal534_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4252);
			scalar_expression535=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression535.getTree());

			char_literal536=(Token)match(input,60,FOLLOW_60_in_nullif_expression4254); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal536_tree = (Object)adaptor.create(char_literal536);
			adaptor.addChild(root_0, char_literal536_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4256);
			scalar_expression537=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression537.getTree());

			char_literal538=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nullif_expression4257); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal538_tree = (Object)adaptor.create(char_literal538);
			adaptor.addChild(root_0, char_literal538_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:466:1: extension_functions : ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | extract_function | '@ENUM' '(' enum_value_literal ')' -> ^( T_ENUM_MACROS[$enum_value_literal.text] ) );
	public final JPA2Parser.extension_functions_return extension_functions() throws RecognitionException {
		JPA2Parser.extension_functions_return retval = new JPA2Parser.extension_functions_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal539=null;
		Token WORD541=null;
		Token char_literal542=null;
		Token INT_NUMERAL543=null;
		Token char_literal544=null;
		Token INT_NUMERAL545=null;
		Token char_literal546=null;
		Token char_literal547=null;
		Token string_literal549=null;
		Token char_literal550=null;
		Token char_literal552=null;
		ParserRuleReturnScope function_arg540 =null;
		ParserRuleReturnScope extract_function548 =null;
		ParserRuleReturnScope enum_value_literal551 =null;

		Object string_literal539_tree=null;
		Object WORD541_tree=null;
		Object char_literal542_tree=null;
		Object INT_NUMERAL543_tree=null;
		Object char_literal544_tree=null;
		Object INT_NUMERAL545_tree=null;
		Object char_literal546_tree=null;
		Object char_literal547_tree=null;
		Object string_literal549_tree=null;
		Object char_literal550_tree=null;
		Object char_literal552_tree=null;
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");
		RewriteRuleSubtreeStream stream_enum_value_literal=new RewriteRuleSubtreeStream(adaptor,"rule enum_value_literal");

		try {
			// JPA2.g:467:5: ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | extract_function | '@ENUM' '(' enum_value_literal ')' -> ^( T_ENUM_MACROS[$enum_value_literal.text] ) )
			int alt130=3;
			switch ( input.LA(1) ) {
			case 85:
				{
				alt130=1;
				}
				break;
			case 100:
				{
				alt130=2;
				}
				break;
			case 76:
				{
				alt130=3;
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
					// JPA2.g:467:7: 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal539=(Token)match(input,85,FOLLOW_85_in_extension_functions4269); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal539_tree = (Object)adaptor.create(string_literal539);
					adaptor.addChild(root_0, string_literal539_tree);
					}

					pushFollow(FOLLOW_function_arg_in_extension_functions4271);
					function_arg540=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg540.getTree());

					WORD541=(Token)match(input,WORD,FOLLOW_WORD_in_extension_functions4273); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD541_tree = (Object)adaptor.create(WORD541);
					adaptor.addChild(root_0, WORD541_tree);
					}

					// JPA2.g:467:33: ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )*
					loop129:
					while (true) {
						int alt129=2;
						int LA129_0 = input.LA(1);
						if ( (LA129_0==LPAREN) ) {
							alt129=1;
						}

						switch (alt129) {
						case 1 :
							// JPA2.g:467:34: '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')'
							{
							char_literal542=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_extension_functions4276); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal542_tree = (Object)adaptor.create(char_literal542);
							adaptor.addChild(root_0, char_literal542_tree);
							}

							INT_NUMERAL543=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4277); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							INT_NUMERAL543_tree = (Object)adaptor.create(INT_NUMERAL543);
							adaptor.addChild(root_0, INT_NUMERAL543_tree);
							}

							// JPA2.g:467:49: ( ',' INT_NUMERAL )*
							loop128:
							while (true) {
								int alt128=2;
								int LA128_0 = input.LA(1);
								if ( (LA128_0==60) ) {
									alt128=1;
								}

								switch (alt128) {
								case 1 :
									// JPA2.g:467:50: ',' INT_NUMERAL
									{
									char_literal544=(Token)match(input,60,FOLLOW_60_in_extension_functions4280); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									char_literal544_tree = (Object)adaptor.create(char_literal544);
									adaptor.addChild(root_0, char_literal544_tree);
									}

									INT_NUMERAL545=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4282); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									INT_NUMERAL545_tree = (Object)adaptor.create(INT_NUMERAL545);
									adaptor.addChild(root_0, INT_NUMERAL545_tree);
									}

									}
									break;

								default :
									break loop128;
								}
							}

							char_literal546=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4287); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal546_tree = (Object)adaptor.create(char_literal546);
							adaptor.addChild(root_0, char_literal546_tree);
							}

							}
							break;

						default :
							break loop129;
						}
					}

					char_literal547=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4291); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal547_tree = (Object)adaptor.create(char_literal547);
					adaptor.addChild(root_0, char_literal547_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:468:7: extract_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extract_function_in_extension_functions4299);
					extract_function548=extract_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extract_function548.getTree());

					}
					break;
				case 3 :
					// JPA2.g:469:7: '@ENUM' '(' enum_value_literal ')'
					{
					string_literal549=(Token)match(input,76,FOLLOW_76_in_extension_functions4307); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_76.add(string_literal549);

					char_literal550=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_extension_functions4309); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal550);

					pushFollow(FOLLOW_enum_value_literal_in_extension_functions4311);
					enum_value_literal551=enum_value_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_enum_value_literal.add(enum_value_literal551.getTree());
					char_literal552=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4313); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal552);

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
					// 469:42: -> ^( T_ENUM_MACROS[$enum_value_literal.text] )
					{
						// JPA2.g:469:45: ^( T_ENUM_MACROS[$enum_value_literal.text] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new EnumConditionNode(T_ENUM_MACROS, (enum_value_literal551!=null?input.toString(enum_value_literal551.start,enum_value_literal551.stop):null)), root_1);
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


	public static class extract_function_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "extract_function"
	// JPA2.g:471:1: extract_function : 'EXTRACT(' date_part 'FROM' function_arg ')' ;
	public final JPA2Parser.extract_function_return extract_function() throws RecognitionException {
		JPA2Parser.extract_function_return retval = new JPA2Parser.extract_function_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal553=null;
		Token string_literal555=null;
		Token char_literal557=null;
		ParserRuleReturnScope date_part554 =null;
		ParserRuleReturnScope function_arg556 =null;

		Object string_literal553_tree=null;
		Object string_literal555_tree=null;
		Object char_literal557_tree=null;

		try {
			// JPA2.g:472:5: ( 'EXTRACT(' date_part 'FROM' function_arg ')' )
			// JPA2.g:472:7: 'EXTRACT(' date_part 'FROM' function_arg ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal553=(Token)match(input,100,FOLLOW_100_in_extract_function4335); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal553_tree = (Object)adaptor.create(string_literal553);
			adaptor.addChild(root_0, string_literal553_tree);
			}

			pushFollow(FOLLOW_date_part_in_extract_function4337);
			date_part554=date_part();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part554.getTree());

			string_literal555=(Token)match(input,101,FOLLOW_101_in_extract_function4339); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal555_tree = (Object)adaptor.create(string_literal555);
			adaptor.addChild(root_0, string_literal555_tree);
			}

			pushFollow(FOLLOW_function_arg_in_extract_function4341);
			function_arg556=function_arg();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg556.getTree());

			char_literal557=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extract_function4343); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal557_tree = (Object)adaptor.create(char_literal557);
			adaptor.addChild(root_0, char_literal557_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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


	public static class date_part_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_part"
	// JPA2.g:474:10: fragment date_part : ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' );
	public final JPA2Parser.date_part_return date_part() throws RecognitionException {
		JPA2Parser.date_part_return retval = new JPA2Parser.date_part_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set558=null;

		Object set558_tree=null;

		try {
			// JPA2.g:475:5: ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set558=input.LT(1);
			if ( input.LA(1)==91||input.LA(1)==97||input.LA(1)==103||input.LA(1)==112||input.LA(1)==114||input.LA(1)==124||input.LA(1)==126||input.LA(1)==140||input.LA(1)==143 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set558));
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
	// JPA2.g:478:1: input_parameter : ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) );
	public final JPA2Parser.input_parameter_return input_parameter() throws RecognitionException {
		JPA2Parser.input_parameter_return retval = new JPA2Parser.input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal559=null;
		Token NAMED_PARAMETER561=null;
		Token string_literal562=null;
		Token WORD563=null;
		Token char_literal564=null;
		ParserRuleReturnScope numeric_literal560 =null;

		Object char_literal559_tree=null;
		Object NAMED_PARAMETER561_tree=null;
		Object string_literal562_tree=null;
		Object WORD563_tree=null;
		Object char_literal564_tree=null;
		RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
		RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
		RewriteRuleTokenStream stream_146=new RewriteRuleTokenStream(adaptor,"token 146");
		RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");
		RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
		RewriteRuleSubtreeStream stream_numeric_literal=new RewriteRuleSubtreeStream(adaptor,"rule numeric_literal");

		try {
			// JPA2.g:479:5: ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
			int alt131=3;
			switch ( input.LA(1) ) {
			case 71:
				{
				alt131=1;
				}
				break;
			case NAMED_PARAMETER:
				{
				alt131=2;
				}
				break;
			case 57:
				{
				alt131=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 131, 0, input);
				throw nvae;
			}
			switch (alt131) {
				case 1 :
					// JPA2.g:479:7: '?' numeric_literal
					{
					char_literal559=(Token)match(input,71,FOLLOW_71_in_input_parameter4400); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_71.add(char_literal559);

					pushFollow(FOLLOW_numeric_literal_in_input_parameter4402);
					numeric_literal560=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_literal.add(numeric_literal560.getTree());
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
					// 479:27: -> ^( T_PARAMETER[] '?' numeric_literal )
					{
						// JPA2.g:479:30: ^( T_PARAMETER[] '?' numeric_literal )
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
					// JPA2.g:480:7: NAMED_PARAMETER
					{
					NAMED_PARAMETER561=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter4425); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER561);

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
					// 480:23: -> ^( T_PARAMETER[] NAMED_PARAMETER )
					{
						// JPA2.g:480:26: ^( T_PARAMETER[] NAMED_PARAMETER )
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
					// JPA2.g:481:7: '${' WORD '}'
					{
					string_literal562=(Token)match(input,57,FOLLOW_57_in_input_parameter4446); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_57.add(string_literal562);

					WORD563=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter4448); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WORD.add(WORD563);

					char_literal564=(Token)match(input,146,FOLLOW_146_in_input_parameter4450); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_146.add(char_literal564);

					// AST REWRITE
					// elements: 57, WORD, 146
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 481:21: -> ^( T_PARAMETER[] '${' WORD '}' )
					{
						// JPA2.g:481:24: ^( T_PARAMETER[] '${' WORD '}' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_57.nextNode());
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
	// JPA2.g:483:1: literal : WORD ;
	public final JPA2Parser.literal_return literal() throws RecognitionException {
		JPA2Parser.literal_return retval = new JPA2Parser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD565=null;

		Object WORD565_tree=null;

		try {
			// JPA2.g:484:5: ( WORD )
			// JPA2.g:484:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD565=(Token)match(input,WORD,FOLLOW_WORD_in_literal4478); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD565_tree = (Object)adaptor.create(WORD565);
			adaptor.addChild(root_0, WORD565_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:486:1: constructor_name : WORD ;
	public final JPA2Parser.constructor_name_return constructor_name() throws RecognitionException {
		JPA2Parser.constructor_name_return retval = new JPA2Parser.constructor_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD566=null;

		Object WORD566_tree=null;

		try {
			// JPA2.g:487:5: ( WORD )
			// JPA2.g:487:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD566=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4490); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD566_tree = (Object)adaptor.create(WORD566);
			adaptor.addChild(root_0, WORD566_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:489:1: enum_literal : WORD ;
	public final JPA2Parser.enum_literal_return enum_literal() throws RecognitionException {
		JPA2Parser.enum_literal_return retval = new JPA2Parser.enum_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD567=null;

		Object WORD567_tree=null;

		try {
			// JPA2.g:490:5: ( WORD )
			// JPA2.g:490:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD567=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal4502); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD567_tree = (Object)adaptor.create(WORD567);
			adaptor.addChild(root_0, WORD567_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:492:1: boolean_literal : ( 'true' | 'false' );
	public final JPA2Parser.boolean_literal_return boolean_literal() throws RecognitionException {
		JPA2Parser.boolean_literal_return retval = new JPA2Parser.boolean_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set568=null;

		Object set568_tree=null;

		try {
			// JPA2.g:493:5: ( 'true' | 'false' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set568=input.LT(1);
			if ( (input.LA(1) >= 144 && input.LA(1) <= 145) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set568));
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
	// JPA2.g:497:1: field : ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | 'AS' | 'MEMBER' | date_part );
	public final JPA2Parser.field_return field() throws RecognitionException {
		JPA2Parser.field_return retval = new JPA2Parser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD569=null;
		Token string_literal570=null;
		Token string_literal571=null;
		Token string_literal572=null;
		Token string_literal573=null;
		Token string_literal574=null;
		Token string_literal575=null;
		Token string_literal576=null;
		Token string_literal577=null;
		Token string_literal578=null;
		Token string_literal579=null;
		Token string_literal580=null;
		ParserRuleReturnScope date_part581 =null;

		Object WORD569_tree=null;
		Object string_literal570_tree=null;
		Object string_literal571_tree=null;
		Object string_literal572_tree=null;
		Object string_literal573_tree=null;
		Object string_literal574_tree=null;
		Object string_literal575_tree=null;
		Object string_literal576_tree=null;
		Object string_literal577_tree=null;
		Object string_literal578_tree=null;
		Object string_literal579_tree=null;
		Object string_literal580_tree=null;

		try {
			// JPA2.g:498:5: ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | 'AS' | 'MEMBER' | date_part )
			int alt132=13;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt132=1;
				}
				break;
			case 127:
				{
				alt132=2;
				}
				break;
			case 101:
				{
				alt132=3;
				}
				break;
			case GROUP:
				{
				alt132=4;
				}
				break;
			case ORDER:
				{
				alt132=5;
				}
				break;
			case MAX:
				{
				alt132=6;
				}
				break;
			case MIN:
				{
				alt132=7;
				}
				break;
			case SUM:
				{
				alt132=8;
				}
				break;
			case AVG:
				{
				alt132=9;
				}
				break;
			case COUNT:
				{
				alt132=10;
				}
				break;
			case 81:
				{
				alt132=11;
				}
				break;
			case 111:
				{
				alt132=12;
				}
				break;
			case 91:
			case 97:
			case 103:
			case 112:
			case 114:
			case 124:
			case 126:
			case 140:
			case 143:
				{
				alt132=13;
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
					// JPA2.g:498:7: WORD
					{
					root_0 = (Object)adaptor.nil();


					WORD569=(Token)match(input,WORD,FOLLOW_WORD_in_field4535); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD569_tree = (Object)adaptor.create(WORD569);
					adaptor.addChild(root_0, WORD569_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:498:14: 'SELECT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal570=(Token)match(input,127,FOLLOW_127_in_field4539); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal570_tree = (Object)adaptor.create(string_literal570);
					adaptor.addChild(root_0, string_literal570_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:498:25: 'FROM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal571=(Token)match(input,101,FOLLOW_101_in_field4543); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal571_tree = (Object)adaptor.create(string_literal571);
					adaptor.addChild(root_0, string_literal571_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:498:34: 'GROUP'
					{
					root_0 = (Object)adaptor.nil();


					string_literal572=(Token)match(input,GROUP,FOLLOW_GROUP_in_field4547); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal572_tree = (Object)adaptor.create(string_literal572);
					adaptor.addChild(root_0, string_literal572_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:498:44: 'ORDER'
					{
					root_0 = (Object)adaptor.nil();


					string_literal573=(Token)match(input,ORDER,FOLLOW_ORDER_in_field4551); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal573_tree = (Object)adaptor.create(string_literal573);
					adaptor.addChild(root_0, string_literal573_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:498:54: 'MAX'
					{
					root_0 = (Object)adaptor.nil();


					string_literal574=(Token)match(input,MAX,FOLLOW_MAX_in_field4555); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal574_tree = (Object)adaptor.create(string_literal574);
					adaptor.addChild(root_0, string_literal574_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:498:62: 'MIN'
					{
					root_0 = (Object)adaptor.nil();


					string_literal575=(Token)match(input,MIN,FOLLOW_MIN_in_field4559); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal575_tree = (Object)adaptor.create(string_literal575);
					adaptor.addChild(root_0, string_literal575_tree);
					}

					}
					break;
				case 8 :
					// JPA2.g:498:70: 'SUM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal576=(Token)match(input,SUM,FOLLOW_SUM_in_field4563); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal576_tree = (Object)adaptor.create(string_literal576);
					adaptor.addChild(root_0, string_literal576_tree);
					}

					}
					break;
				case 9 :
					// JPA2.g:498:78: 'AVG'
					{
					root_0 = (Object)adaptor.nil();


					string_literal577=(Token)match(input,AVG,FOLLOW_AVG_in_field4567); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal577_tree = (Object)adaptor.create(string_literal577);
					adaptor.addChild(root_0, string_literal577_tree);
					}

					}
					break;
				case 10 :
					// JPA2.g:498:86: 'COUNT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal578=(Token)match(input,COUNT,FOLLOW_COUNT_in_field4571); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal578_tree = (Object)adaptor.create(string_literal578);
					adaptor.addChild(root_0, string_literal578_tree);
					}

					}
					break;
				case 11 :
					// JPA2.g:498:96: 'AS'
					{
					root_0 = (Object)adaptor.nil();


					string_literal579=(Token)match(input,81,FOLLOW_81_in_field4575); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal579_tree = (Object)adaptor.create(string_literal579);
					adaptor.addChild(root_0, string_literal579_tree);
					}

					}
					break;
				case 12 :
					// JPA2.g:498:103: 'MEMBER'
					{
					root_0 = (Object)adaptor.nil();


					string_literal580=(Token)match(input,111,FOLLOW_111_in_field4579); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal580_tree = (Object)adaptor.create(string_literal580);
					adaptor.addChild(root_0, string_literal580_tree);
					}

					}
					break;
				case 13 :
					// JPA2.g:498:114: date_part
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_part_in_field4583);
					date_part581=date_part();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part581.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:500:1: identification_variable : ( WORD | 'GROUP' );
	public final JPA2Parser.identification_variable_return identification_variable() throws RecognitionException {
		JPA2Parser.identification_variable_return retval = new JPA2Parser.identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set582=null;

		Object set582_tree=null;

		try {
			// JPA2.g:501:5: ( WORD | 'GROUP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set582=input.LT(1);
			if ( input.LA(1)==GROUP||input.LA(1)==WORD ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set582));
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
	// JPA2.g:503:1: parameter_name : WORD ( '.' WORD )* ;
	public final JPA2Parser.parameter_name_return parameter_name() throws RecognitionException {
		JPA2Parser.parameter_name_return retval = new JPA2Parser.parameter_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD583=null;
		Token char_literal584=null;
		Token WORD585=null;

		Object WORD583_tree=null;
		Object char_literal584_tree=null;
		Object WORD585_tree=null;

		try {
			// JPA2.g:504:5: ( WORD ( '.' WORD )* )
			// JPA2.g:504:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD583=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4611); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD583_tree = (Object)adaptor.create(WORD583);
			adaptor.addChild(root_0, WORD583_tree);
			}

			// JPA2.g:504:12: ( '.' WORD )*
			loop133:
			while (true) {
				int alt133=2;
				int LA133_0 = input.LA(1);
				if ( (LA133_0==62) ) {
					alt133=1;
				}

				switch (alt133) {
				case 1 :
					// JPA2.g:504:13: '.' WORD
					{
					char_literal584=(Token)match(input,62,FOLLOW_62_in_parameter_name4614); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal584_tree = (Object)adaptor.create(char_literal584);
					adaptor.addChild(root_0, char_literal584_tree);
					}

					WORD585=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4617); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD585_tree = (Object)adaptor.create(WORD585);
					adaptor.addChild(root_0, WORD585_tree);
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
	// $ANTLR end "parameter_name"


	public static class escape_character_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "escape_character"
	// JPA2.g:507:1: escape_character : ( '\\'.\\'' | STRING_LITERAL );
	public final JPA2Parser.escape_character_return escape_character() throws RecognitionException {
		JPA2Parser.escape_character_return retval = new JPA2Parser.escape_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set586=null;

		Object set586_tree=null;

		try {
			// JPA2.g:508:5: ( '\\'.\\'' | STRING_LITERAL )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set586=input.LT(1);
			if ( input.LA(1)==STRING_LITERAL||input.LA(1)==TRIM_CHARACTER ) {
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
	// $ANTLR end "escape_character"


	public static class trim_character_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "trim_character"
	// JPA2.g:509:1: trim_character : TRIM_CHARACTER ;
	public final JPA2Parser.trim_character_return trim_character() throws RecognitionException {
		JPA2Parser.trim_character_return retval = new JPA2Parser.trim_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRIM_CHARACTER587=null;

		Object TRIM_CHARACTER587_tree=null;

		try {
			// JPA2.g:510:5: ( TRIM_CHARACTER )
			// JPA2.g:510:7: TRIM_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			TRIM_CHARACTER587=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_trim_character4647); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TRIM_CHARACTER587_tree = (Object)adaptor.create(TRIM_CHARACTER587);
			adaptor.addChild(root_0, TRIM_CHARACTER587_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:511:1: string_literal : STRING_LITERAL ;
	public final JPA2Parser.string_literal_return string_literal() throws RecognitionException {
		JPA2Parser.string_literal_return retval = new JPA2Parser.string_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL588=null;

		Object STRING_LITERAL588_tree=null;

		try {
			// JPA2.g:512:5: ( STRING_LITERAL )
			// JPA2.g:512:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL588=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_literal4658); if (state.failed) return retval;
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
	// $ANTLR end "string_literal"


	public static class numeric_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "numeric_literal"
	// JPA2.g:513:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
	public final JPA2Parser.numeric_literal_return numeric_literal() throws RecognitionException {
		JPA2Parser.numeric_literal_return retval = new JPA2Parser.numeric_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal589=null;
		Token INT_NUMERAL590=null;

		Object string_literal589_tree=null;
		Object INT_NUMERAL590_tree=null;

		try {
			// JPA2.g:514:5: ( ( '0x' )? INT_NUMERAL )
			// JPA2.g:514:7: ( '0x' )? INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:514:7: ( '0x' )?
			int alt134=2;
			int LA134_0 = input.LA(1);
			if ( (LA134_0==64) ) {
				alt134=1;
			}
			switch (alt134) {
				case 1 :
					// JPA2.g:514:8: '0x'
					{
					string_literal589=(Token)match(input,64,FOLLOW_64_in_numeric_literal4670); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal589_tree = (Object)adaptor.create(string_literal589);
					adaptor.addChild(root_0, string_literal589_tree);
					}

					}
					break;

			}

			INT_NUMERAL590=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal4674); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL590_tree = (Object)adaptor.create(INT_NUMERAL590);
			adaptor.addChild(root_0, INT_NUMERAL590_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:515:1: single_valued_object_field : WORD ;
	public final JPA2Parser.single_valued_object_field_return single_valued_object_field() throws RecognitionException {
		JPA2Parser.single_valued_object_field_return retval = new JPA2Parser.single_valued_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD591=null;

		Object WORD591_tree=null;

		try {
			// JPA2.g:516:5: ( WORD )
			// JPA2.g:516:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD591=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_object_field4686); if (state.failed) return retval;
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
	// $ANTLR end "single_valued_object_field"


	public static class single_valued_embeddable_object_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "single_valued_embeddable_object_field"
	// JPA2.g:517:1: single_valued_embeddable_object_field : WORD ;
	public final JPA2Parser.single_valued_embeddable_object_field_return single_valued_embeddable_object_field() throws RecognitionException {
		JPA2Parser.single_valued_embeddable_object_field_return retval = new JPA2Parser.single_valued_embeddable_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD592=null;

		Object WORD592_tree=null;

		try {
			// JPA2.g:518:5: ( WORD )
			// JPA2.g:518:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD592=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_embeddable_object_field4697); if (state.failed) return retval;
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
	// $ANTLR end "single_valued_embeddable_object_field"


	public static class collection_valued_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "collection_valued_field"
	// JPA2.g:519:1: collection_valued_field : WORD ;
	public final JPA2Parser.collection_valued_field_return collection_valued_field() throws RecognitionException {
		JPA2Parser.collection_valued_field_return retval = new JPA2Parser.collection_valued_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD593=null;

		Object WORD593_tree=null;

		try {
			// JPA2.g:520:5: ( WORD )
			// JPA2.g:520:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD593=(Token)match(input,WORD,FOLLOW_WORD_in_collection_valued_field4708); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD593_tree = (Object)adaptor.create(WORD593);
			adaptor.addChild(root_0, WORD593_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:521:1: entity_name : WORD ;
	public final JPA2Parser.entity_name_return entity_name() throws RecognitionException {
		JPA2Parser.entity_name_return retval = new JPA2Parser.entity_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD594=null;

		Object WORD594_tree=null;

		try {
			// JPA2.g:522:5: ( WORD )
			// JPA2.g:522:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD594=(Token)match(input,WORD,FOLLOW_WORD_in_entity_name4719); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD594_tree = (Object)adaptor.create(WORD594);
			adaptor.addChild(root_0, WORD594_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:523:1: subtype : WORD ;
	public final JPA2Parser.subtype_return subtype() throws RecognitionException {
		JPA2Parser.subtype_return retval = new JPA2Parser.subtype_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD595=null;

		Object WORD595_tree=null;

		try {
			// JPA2.g:524:5: ( WORD )
			// JPA2.g:524:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD595=(Token)match(input,WORD,FOLLOW_WORD_in_subtype4730); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD595_tree = (Object)adaptor.create(WORD595);
			adaptor.addChild(root_0, WORD595_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:525:1: entity_type_literal : WORD ;
	public final JPA2Parser.entity_type_literal_return entity_type_literal() throws RecognitionException {
		JPA2Parser.entity_type_literal_return retval = new JPA2Parser.entity_type_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD596=null;

		Object WORD596_tree=null;

		try {
			// JPA2.g:526:5: ( WORD )
			// JPA2.g:526:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD596=(Token)match(input,WORD,FOLLOW_WORD_in_entity_type_literal4741); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD596_tree = (Object)adaptor.create(WORD596);
			adaptor.addChild(root_0, WORD596_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:527:1: function_name : STRING_LITERAL ;
	public final JPA2Parser.function_name_return function_name() throws RecognitionException {
		JPA2Parser.function_name_return retval = new JPA2Parser.function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL597=null;

		Object STRING_LITERAL597_tree=null;

		try {
			// JPA2.g:528:5: ( STRING_LITERAL )
			// JPA2.g:528:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL597=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_function_name4752); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL597_tree = (Object)adaptor.create(STRING_LITERAL597);
			adaptor.addChild(root_0, STRING_LITERAL597_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:529:1: state_field : WORD ;
	public final JPA2Parser.state_field_return state_field() throws RecognitionException {
		JPA2Parser.state_field_return retval = new JPA2Parser.state_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD598=null;

		Object WORD598_tree=null;

		try {
			// JPA2.g:530:5: ( WORD )
			// JPA2.g:530:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD598=(Token)match(input,WORD,FOLLOW_WORD_in_state_field4763); if (state.failed) return retval;
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
	// $ANTLR end "state_field"


	public static class result_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "result_variable"
	// JPA2.g:531:1: result_variable : WORD ;
	public final JPA2Parser.result_variable_return result_variable() throws RecognitionException {
		JPA2Parser.result_variable_return retval = new JPA2Parser.result_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD599=null;

		Object WORD599_tree=null;

		try {
			// JPA2.g:532:5: ( WORD )
			// JPA2.g:532:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD599=(Token)match(input,WORD,FOLLOW_WORD_in_result_variable4774); if (state.failed) return retval;
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
	// $ANTLR end "result_variable"


	public static class superquery_identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "superquery_identification_variable"
	// JPA2.g:533:1: superquery_identification_variable : WORD ;
	public final JPA2Parser.superquery_identification_variable_return superquery_identification_variable() throws RecognitionException {
		JPA2Parser.superquery_identification_variable_return retval = new JPA2Parser.superquery_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD600=null;

		Object WORD600_tree=null;

		try {
			// JPA2.g:534:5: ( WORD )
			// JPA2.g:534:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD600=(Token)match(input,WORD,FOLLOW_WORD_in_superquery_identification_variable4785); if (state.failed) return retval;
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
	// $ANTLR end "superquery_identification_variable"


	public static class date_time_timestamp_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_time_timestamp_literal"
	// JPA2.g:535:1: date_time_timestamp_literal : WORD ;
	public final JPA2Parser.date_time_timestamp_literal_return date_time_timestamp_literal() throws RecognitionException {
		JPA2Parser.date_time_timestamp_literal_return retval = new JPA2Parser.date_time_timestamp_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD601=null;

		Object WORD601_tree=null;

		try {
			// JPA2.g:536:5: ( WORD )
			// JPA2.g:536:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD601=(Token)match(input,WORD,FOLLOW_WORD_in_date_time_timestamp_literal4796); if (state.failed) return retval;
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
	// $ANTLR end "date_time_timestamp_literal"


	public static class pattern_value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "pattern_value"
	// JPA2.g:537:1: pattern_value : string_literal ;
	public final JPA2Parser.pattern_value_return pattern_value() throws RecognitionException {
		JPA2Parser.pattern_value_return retval = new JPA2Parser.pattern_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal602 =null;


		try {
			// JPA2.g:538:5: ( string_literal )
			// JPA2.g:538:7: string_literal
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_literal_in_pattern_value4807);
			string_literal602=string_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal602.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:539:1: collection_valued_input_parameter : input_parameter ;
	public final JPA2Parser.collection_valued_input_parameter_return collection_valued_input_parameter() throws RecognitionException {
		JPA2Parser.collection_valued_input_parameter_return retval = new JPA2Parser.collection_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter603 =null;


		try {
			// JPA2.g:540:5: ( input_parameter )
			// JPA2.g:540:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_collection_valued_input_parameter4818);
			input_parameter603=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter603.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:541:1: single_valued_input_parameter : input_parameter ;
	public final JPA2Parser.single_valued_input_parameter_return single_valued_input_parameter() throws RecognitionException {
		JPA2Parser.single_valued_input_parameter_return retval = new JPA2Parser.single_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter604 =null;


		try {
			// JPA2.g:542:5: ( input_parameter )
			// JPA2.g:542:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_single_valued_input_parameter4829);
			input_parameter604=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter604.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:543:1: enum_value_literal : WORD ( '.' WORD )* ;
	public final JPA2Parser.enum_value_literal_return enum_value_literal() throws RecognitionException {
		JPA2Parser.enum_value_literal_return retval = new JPA2Parser.enum_value_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD605=null;
		Token char_literal606=null;
		Token WORD607=null;

		Object WORD605_tree=null;
		Object char_literal606_tree=null;
		Object WORD607_tree=null;

		try {
			// JPA2.g:544:5: ( WORD ( '.' WORD )* )
			// JPA2.g:544:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD605=(Token)match(input,WORD,FOLLOW_WORD_in_enum_value_literal4840); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD605_tree = (Object)adaptor.create(WORD605);
			adaptor.addChild(root_0, WORD605_tree);
			}

			// JPA2.g:544:12: ( '.' WORD )*
			loop135:
			while (true) {
				int alt135=2;
				int LA135_0 = input.LA(1);
				if ( (LA135_0==62) ) {
					alt135=1;
				}

				switch (alt135) {
				case 1 :
					// JPA2.g:544:13: '.' WORD
					{
					char_literal606=(Token)match(input,62,FOLLOW_62_in_enum_value_literal4843); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal606_tree = (Object)adaptor.create(char_literal606);
					adaptor.addChild(root_0, char_literal606_tree);
					}

					WORD607=(Token)match(input,WORD,FOLLOW_WORD_in_enum_value_literal4846); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD607_tree = (Object)adaptor.create(WORD607);
					adaptor.addChild(root_0, WORD607_tree);
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

	// $ANTLR start synpred43_JPA2
	public final void synpred43_JPA2_fragment() throws RecognitionException {
		// JPA2.g:169:7: ( path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )? )
		// JPA2.g:169:7: path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
		{
		pushFollow(FOLLOW_path_expression_in_synpred43_JPA21353);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:169:23: ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
		int alt142=2;
		int LA142_0 = input.LA(1);
		if ( ((LA142_0 >= 58 && LA142_0 <= 59)||LA142_0==61||LA142_0==63) ) {
			alt142=1;
		}
		switch (alt142) {
			case 1 :
				// JPA2.g:169:24: ( '+' | '-' | '*' | '/' ) scalar_expression
				{
				if ( (input.LA(1) >= 58 && input.LA(1) <= 59)||input.LA(1)==61||input.LA(1)==63 ) {
					input.consume();
					state.errorRecovery=false;
					state.failed=false;
				}
				else {
					if (state.backtracking>0) {state.failed=true; return;}
					MismatchedSetException mse = new MismatchedSetException(null,input);
					throw mse;
				}
				pushFollow(FOLLOW_scalar_expression_in_synpred43_JPA21372);
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
		// JPA2.g:170:7: ( identification_variable )
		// JPA2.g:170:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred44_JPA21382);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred44_JPA2

	// $ANTLR start synpred45_JPA2
	public final void synpred45_JPA2_fragment() throws RecognitionException {
		// JPA2.g:171:7: ( scalar_expression )
		// JPA2.g:171:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred45_JPA21400);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred45_JPA2

	// $ANTLR start synpred46_JPA2
	public final void synpred46_JPA2_fragment() throws RecognitionException {
		// JPA2.g:172:7: ( aggregate_expression )
		// JPA2.g:172:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred46_JPA21408);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred46_JPA2

	// $ANTLR start synpred49_JPA2
	public final void synpred49_JPA2_fragment() throws RecognitionException {
		// JPA2.g:178:7: ( path_expression )
		// JPA2.g:178:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred49_JPA21465);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred49_JPA2

	// $ANTLR start synpred50_JPA2
	public final void synpred50_JPA2_fragment() throws RecognitionException {
		// JPA2.g:179:7: ( scalar_expression )
		// JPA2.g:179:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred50_JPA21473);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred50_JPA2

	// $ANTLR start synpred51_JPA2
	public final void synpred51_JPA2_fragment() throws RecognitionException {
		// JPA2.g:180:7: ( aggregate_expression )
		// JPA2.g:180:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred51_JPA21481);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred51_JPA2

	// $ANTLR start synpred53_JPA2
	public final void synpred53_JPA2_fragment() throws RecognitionException {
		// JPA2.g:183:7: ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' )
		// JPA2.g:183:7: aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')'
		{
		pushFollow(FOLLOW_aggregate_expression_function_name_in_synpred53_JPA21500);
		aggregate_expression_function_name();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred53_JPA21502); if (state.failed) return;

		// JPA2.g:183:45: ( DISTINCT )?
		int alt143=2;
		int LA143_0 = input.LA(1);
		if ( (LA143_0==DISTINCT) ) {
			alt143=1;
		}
		switch (alt143) {
			case 1 :
				// JPA2.g:183:46: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred53_JPA21504); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_arithmetic_expression_in_synpred53_JPA21508);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred53_JPA21509); if (state.failed) return;

		}

	}
	// $ANTLR end synpred53_JPA2

	// $ANTLR start synpred55_JPA2
	public final void synpred55_JPA2_fragment() throws RecognitionException {
		// JPA2.g:185:7: ( 'COUNT' '(' ( DISTINCT )? count_argument ')' )
		// JPA2.g:185:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
		{
		match(input,COUNT,FOLLOW_COUNT_in_synpred55_JPA21543); if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred55_JPA21545); if (state.failed) return;

		// JPA2.g:185:18: ( DISTINCT )?
		int alt144=2;
		int LA144_0 = input.LA(1);
		if ( (LA144_0==DISTINCT) ) {
			alt144=1;
		}
		switch (alt144) {
			case 1 :
				// JPA2.g:185:19: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred55_JPA21547); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_count_argument_in_synpred55_JPA21551);
		count_argument();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred55_JPA21553); if (state.failed) return;

		}

	}
	// $ANTLR end synpred55_JPA2

	// $ANTLR start synpred67_JPA2
	public final void synpred67_JPA2_fragment() throws RecognitionException {
		// JPA2.g:208:7: ( path_expression )
		// JPA2.g:208:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred67_JPA21824);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred67_JPA2

	// $ANTLR start synpred68_JPA2
	public final void synpred68_JPA2_fragment() throws RecognitionException {
		// JPA2.g:208:25: ( general_identification_variable )
		// JPA2.g:208:25: general_identification_variable
		{
		pushFollow(FOLLOW_general_identification_variable_in_synpred68_JPA21828);
		general_identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred68_JPA2

	// $ANTLR start synpred69_JPA2
	public final void synpred69_JPA2_fragment() throws RecognitionException {
		// JPA2.g:208:59: ( result_variable )
		// JPA2.g:208:59: result_variable
		{
		pushFollow(FOLLOW_result_variable_in_synpred69_JPA21832);
		result_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred69_JPA2

	// $ANTLR start synpred70_JPA2
	public final void synpred70_JPA2_fragment() throws RecognitionException {
		// JPA2.g:208:77: ( scalar_expression )
		// JPA2.g:208:77: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred70_JPA21836);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred70_JPA2

	// $ANTLR start synpred80_JPA2
	public final void synpred80_JPA2_fragment() throws RecognitionException {
		// JPA2.g:225:7: ( general_derived_path '.' single_valued_object_field )
		// JPA2.g:225:7: general_derived_path '.' single_valued_object_field
		{
		pushFollow(FOLLOW_general_derived_path_in_synpred80_JPA22043);
		general_derived_path();
		state._fsp--;
		if (state.failed) return;

		match(input,62,FOLLOW_62_in_synpred80_JPA22044); if (state.failed) return;

		pushFollow(FOLLOW_single_valued_object_field_in_synpred80_JPA22045);
		single_valued_object_field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred80_JPA2

	// $ANTLR start synpred85_JPA2
	public final void synpred85_JPA2_fragment() throws RecognitionException {
		// JPA2.g:243:7: ( path_expression )
		// JPA2.g:243:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred85_JPA22197);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred85_JPA2

	// $ANTLR start synpred86_JPA2
	public final void synpred86_JPA2_fragment() throws RecognitionException {
		// JPA2.g:244:7: ( scalar_expression )
		// JPA2.g:244:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred86_JPA22205);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred86_JPA2

	// $ANTLR start synpred87_JPA2
	public final void synpred87_JPA2_fragment() throws RecognitionException {
		// JPA2.g:245:7: ( aggregate_expression )
		// JPA2.g:245:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred87_JPA22213);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred87_JPA2

	// $ANTLR start synpred88_JPA2
	public final void synpred88_JPA2_fragment() throws RecognitionException {
		// JPA2.g:248:7: ( arithmetic_expression )
		// JPA2.g:248:7: arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred88_JPA22232);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred88_JPA2

	// $ANTLR start synpred89_JPA2
	public final void synpred89_JPA2_fragment() throws RecognitionException {
		// JPA2.g:249:7: ( string_expression )
		// JPA2.g:249:7: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred89_JPA22240);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred89_JPA2

	// $ANTLR start synpred90_JPA2
	public final void synpred90_JPA2_fragment() throws RecognitionException {
		// JPA2.g:250:7: ( enum_expression )
		// JPA2.g:250:7: enum_expression
		{
		pushFollow(FOLLOW_enum_expression_in_synpred90_JPA22248);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred90_JPA2

	// $ANTLR start synpred91_JPA2
	public final void synpred91_JPA2_fragment() throws RecognitionException {
		// JPA2.g:251:7: ( datetime_expression )
		// JPA2.g:251:7: datetime_expression
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred91_JPA22256);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred91_JPA2

	// $ANTLR start synpred92_JPA2
	public final void synpred92_JPA2_fragment() throws RecognitionException {
		// JPA2.g:252:7: ( boolean_expression )
		// JPA2.g:252:7: boolean_expression
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred92_JPA22264);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred92_JPA2

	// $ANTLR start synpred93_JPA2
	public final void synpred93_JPA2_fragment() throws RecognitionException {
		// JPA2.g:253:7: ( case_expression )
		// JPA2.g:253:7: case_expression
		{
		pushFollow(FOLLOW_case_expression_in_synpred93_JPA22272);
		case_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred93_JPA2

	// $ANTLR start synpred96_JPA2
	public final void synpred96_JPA2_fragment() throws RecognitionException {
		// JPA2.g:260:8: ( 'NOT' )
		// JPA2.g:260:8: 'NOT'
		{
		match(input,NOT,FOLLOW_NOT_in_synpred96_JPA22332); if (state.failed) return;

		}

	}
	// $ANTLR end synpred96_JPA2

	// $ANTLR start synpred97_JPA2
	public final void synpred97_JPA2_fragment() throws RecognitionException {
		// JPA2.g:262:7: ( simple_cond_expression )
		// JPA2.g:262:7: simple_cond_expression
		{
		pushFollow(FOLLOW_simple_cond_expression_in_synpred97_JPA22347);
		simple_cond_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred97_JPA2

	// $ANTLR start synpred98_JPA2
	public final void synpred98_JPA2_fragment() throws RecognitionException {
		// JPA2.g:266:7: ( comparison_expression )
		// JPA2.g:266:7: comparison_expression
		{
		pushFollow(FOLLOW_comparison_expression_in_synpred98_JPA22384);
		comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred98_JPA2

	// $ANTLR start synpred99_JPA2
	public final void synpred99_JPA2_fragment() throws RecognitionException {
		// JPA2.g:267:7: ( between_expression )
		// JPA2.g:267:7: between_expression
		{
		pushFollow(FOLLOW_between_expression_in_synpred99_JPA22392);
		between_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred99_JPA2

	// $ANTLR start synpred100_JPA2
	public final void synpred100_JPA2_fragment() throws RecognitionException {
		// JPA2.g:268:7: ( in_expression )
		// JPA2.g:268:7: in_expression
		{
		pushFollow(FOLLOW_in_expression_in_synpred100_JPA22400);
		in_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred100_JPA2

	// $ANTLR start synpred101_JPA2
	public final void synpred101_JPA2_fragment() throws RecognitionException {
		// JPA2.g:269:7: ( like_expression )
		// JPA2.g:269:7: like_expression
		{
		pushFollow(FOLLOW_like_expression_in_synpred101_JPA22408);
		like_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred101_JPA2

	// $ANTLR start synpred102_JPA2
	public final void synpred102_JPA2_fragment() throws RecognitionException {
		// JPA2.g:270:7: ( null_comparison_expression )
		// JPA2.g:270:7: null_comparison_expression
		{
		pushFollow(FOLLOW_null_comparison_expression_in_synpred102_JPA22416);
		null_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred102_JPA2

	// $ANTLR start synpred103_JPA2
	public final void synpred103_JPA2_fragment() throws RecognitionException {
		// JPA2.g:271:7: ( empty_collection_comparison_expression )
		// JPA2.g:271:7: empty_collection_comparison_expression
		{
		pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred103_JPA22424);
		empty_collection_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred103_JPA2

	// $ANTLR start synpred104_JPA2
	public final void synpred104_JPA2_fragment() throws RecognitionException {
		// JPA2.g:272:7: ( collection_member_expression )
		// JPA2.g:272:7: collection_member_expression
		{
		pushFollow(FOLLOW_collection_member_expression_in_synpred104_JPA22432);
		collection_member_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred104_JPA2

	// $ANTLR start synpred123_JPA2
	public final void synpred123_JPA2_fragment() throws RecognitionException {
		// JPA2.g:301:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
		// JPA2.g:301:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred123_JPA22685);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:301:29: ( 'NOT' )?
		int alt146=2;
		int LA146_0 = input.LA(1);
		if ( (LA146_0==NOT) ) {
			alt146=1;
		}
		switch (alt146) {
			case 1 :
				// JPA2.g:301:30: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred123_JPA22688); if (state.failed) return;

				}
				break;

		}

		match(input,82,FOLLOW_82_in_synpred123_JPA22692); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred123_JPA22694);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred123_JPA22696); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred123_JPA22698);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred123_JPA2

	// $ANTLR start synpred125_JPA2
	public final void synpred125_JPA2_fragment() throws RecognitionException {
		// JPA2.g:302:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
		// JPA2.g:302:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred125_JPA22706);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:302:25: ( 'NOT' )?
		int alt147=2;
		int LA147_0 = input.LA(1);
		if ( (LA147_0==NOT) ) {
			alt147=1;
		}
		switch (alt147) {
			case 1 :
				// JPA2.g:302:26: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred125_JPA22709); if (state.failed) return;

				}
				break;

		}

		match(input,82,FOLLOW_82_in_synpred125_JPA22713); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred125_JPA22715);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred125_JPA22717); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred125_JPA22719);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred125_JPA2

	// $ANTLR start synpred137_JPA2
	public final void synpred137_JPA2_fragment() throws RecognitionException {
		// JPA2.g:318:42: ( string_expression )
		// JPA2.g:318:42: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred137_JPA22904);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred137_JPA2

	// $ANTLR start synpred138_JPA2
	public final void synpred138_JPA2_fragment() throws RecognitionException {
		// JPA2.g:318:62: ( pattern_value )
		// JPA2.g:318:62: pattern_value
		{
		pushFollow(FOLLOW_pattern_value_in_synpred138_JPA22908);
		pattern_value();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred138_JPA2

	// $ANTLR start synpred140_JPA2
	public final void synpred140_JPA2_fragment() throws RecognitionException {
		// JPA2.g:320:8: ( path_expression )
		// JPA2.g:320:8: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred140_JPA22931);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred140_JPA2

	// $ANTLR start synpred148_JPA2
	public final void synpred148_JPA2_fragment() throws RecognitionException {
		// JPA2.g:330:7: ( identification_variable )
		// JPA2.g:330:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred148_JPA23033);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred148_JPA2

	// $ANTLR start synpred155_JPA2
	public final void synpred155_JPA2_fragment() throws RecognitionException {
		// JPA2.g:338:7: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) )
		// JPA2.g:338:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_string_expression_in_synpred155_JPA23102);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:338:25: ( comparison_operator | 'REGEXP' )
		int alt149=2;
		int LA149_0 = input.LA(1);
		if ( ((LA149_0 >= 65 && LA149_0 <= 70)) ) {
			alt149=1;
		}
		else if ( (LA149_0==125) ) {
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
				// JPA2.g:338:26: comparison_operator
				{
				pushFollow(FOLLOW_comparison_operator_in_synpred155_JPA23105);
				comparison_operator();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:338:48: 'REGEXP'
				{
				match(input,125,FOLLOW_125_in_synpred155_JPA23109); if (state.failed) return;

				}
				break;

		}

		// JPA2.g:338:58: ( string_expression | all_or_any_expression )
		int alt150=2;
		int LA150_0 = input.LA(1);
		if ( (LA150_0==AVG||LA150_0==COUNT||LA150_0==GROUP||(LA150_0 >= LOWER && LA150_0 <= NAMED_PARAMETER)||(LA150_0 >= STRING_LITERAL && LA150_0 <= SUM)||LA150_0==WORD||LA150_0==57||LA150_0==71||LA150_0==76||(LA150_0 >= 84 && LA150_0 <= 87)||LA150_0==100||LA150_0==102||LA150_0==118||LA150_0==131||LA150_0==135||LA150_0==138) ) {
			alt150=1;
		}
		else if ( ((LA150_0 >= 79 && LA150_0 <= 80)||LA150_0==129) ) {
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
				// JPA2.g:338:59: string_expression
				{
				pushFollow(FOLLOW_string_expression_in_synpred155_JPA23113);
				string_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:338:79: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred155_JPA23117);
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
		// JPA2.g:339:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
		// JPA2.g:339:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred158_JPA23126);
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
		// JPA2.g:339:39: ( boolean_expression | all_or_any_expression )
		int alt151=2;
		int LA151_0 = input.LA(1);
		if ( (LA151_0==GROUP||LA151_0==LPAREN||LA151_0==NAMED_PARAMETER||LA151_0==WORD||LA151_0==57||LA151_0==71||LA151_0==76||(LA151_0 >= 84 && LA151_0 <= 86)||LA151_0==100||LA151_0==102||LA151_0==118||(LA151_0 >= 144 && LA151_0 <= 145)) ) {
			alt151=1;
		}
		else if ( ((LA151_0 >= 79 && LA151_0 <= 80)||LA151_0==129) ) {
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
				// JPA2.g:339:40: boolean_expression
				{
				pushFollow(FOLLOW_boolean_expression_in_synpred158_JPA23137);
				boolean_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:339:61: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred158_JPA23141);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred158_JPA2

	// $ANTLR start synpred161_JPA2
	public final void synpred161_JPA2_fragment() throws RecognitionException {
		// JPA2.g:340:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
		// JPA2.g:340:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_enum_expression_in_synpred161_JPA23150);
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
		// JPA2.g:340:34: ( enum_expression | all_or_any_expression )
		int alt152=2;
		int LA152_0 = input.LA(1);
		if ( (LA152_0==GROUP||LA152_0==LPAREN||LA152_0==NAMED_PARAMETER||LA152_0==WORD||LA152_0==57||LA152_0==71||LA152_0==84||LA152_0==86||LA152_0==118) ) {
			alt152=1;
		}
		else if ( ((LA152_0 >= 79 && LA152_0 <= 80)||LA152_0==129) ) {
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
				// JPA2.g:340:35: enum_expression
				{
				pushFollow(FOLLOW_enum_expression_in_synpred161_JPA23159);
				enum_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:340:53: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred161_JPA23163);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred161_JPA2

	// $ANTLR start synpred163_JPA2
	public final void synpred163_JPA2_fragment() throws RecognitionException {
		// JPA2.g:341:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
		// JPA2.g:341:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred163_JPA23172);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred163_JPA23174);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:341:47: ( datetime_expression | all_or_any_expression )
		int alt153=2;
		int LA153_0 = input.LA(1);
		if ( (LA153_0==AVG||LA153_0==COUNT||LA153_0==GROUP||(LA153_0 >= LPAREN && LA153_0 <= NAMED_PARAMETER)||LA153_0==SUM||LA153_0==WORD||LA153_0==57||LA153_0==71||LA153_0==76||(LA153_0 >= 84 && LA153_0 <= 86)||(LA153_0 >= 88 && LA153_0 <= 90)||LA153_0==100||LA153_0==102||LA153_0==118) ) {
			alt153=1;
		}
		else if ( ((LA153_0 >= 79 && LA153_0 <= 80)||LA153_0==129) ) {
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
				// JPA2.g:341:48: datetime_expression
				{
				pushFollow(FOLLOW_datetime_expression_in_synpred163_JPA23177);
				datetime_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:341:70: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred163_JPA23181);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred163_JPA2

	// $ANTLR start synpred166_JPA2
	public final void synpred166_JPA2_fragment() throws RecognitionException {
		// JPA2.g:342:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
		// JPA2.g:342:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_entity_expression_in_synpred166_JPA23190);
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
		// JPA2.g:342:38: ( entity_expression | all_or_any_expression )
		int alt154=2;
		int LA154_0 = input.LA(1);
		if ( (LA154_0==GROUP||LA154_0==NAMED_PARAMETER||LA154_0==WORD||LA154_0==57||LA154_0==71) ) {
			alt154=1;
		}
		else if ( ((LA154_0 >= 79 && LA154_0 <= 80)||LA154_0==129) ) {
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
				// JPA2.g:342:39: entity_expression
				{
				pushFollow(FOLLOW_entity_expression_in_synpred166_JPA23201);
				entity_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:342:59: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred166_JPA23205);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred166_JPA2

	// $ANTLR start synpred168_JPA2
	public final void synpred168_JPA2_fragment() throws RecognitionException {
		// JPA2.g:343:7: ( entity_type_expression ( '=' | '<>' ) entity_type_expression )
		// JPA2.g:343:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
		{
		pushFollow(FOLLOW_entity_type_expression_in_synpred168_JPA23214);
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
		pushFollow(FOLLOW_entity_type_expression_in_synpred168_JPA23224);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred168_JPA2

	// $ANTLR start synpred176_JPA2
	public final void synpred176_JPA2_fragment() throws RecognitionException {
		// JPA2.g:354:7: ( arithmetic_term ( '+' | '-' ) arithmetic_term )
		// JPA2.g:354:7: arithmetic_term ( '+' | '-' ) arithmetic_term
		{
		pushFollow(FOLLOW_arithmetic_term_in_synpred176_JPA23305);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		if ( input.LA(1)==59||input.LA(1)==61 ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		pushFollow(FOLLOW_arithmetic_term_in_synpred176_JPA23315);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred176_JPA2

	// $ANTLR start synpred178_JPA2
	public final void synpred178_JPA2_fragment() throws RecognitionException {
		// JPA2.g:357:7: ( arithmetic_factor ( '*' | '/' ) arithmetic_factor )
		// JPA2.g:357:7: arithmetic_factor ( '*' | '/' ) arithmetic_factor
		{
		pushFollow(FOLLOW_arithmetic_factor_in_synpred178_JPA23334);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		if ( input.LA(1)==58||input.LA(1)==63 ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		pushFollow(FOLLOW_arithmetic_factor_in_synpred178_JPA23345);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred178_JPA2

	// $ANTLR start synpred183_JPA2
	public final void synpred183_JPA2_fragment() throws RecognitionException {
		// JPA2.g:364:7: ( '(' arithmetic_expression ')' )
		// JPA2.g:364:7: '(' arithmetic_expression ')'
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred183_JPA23403); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred183_JPA23404);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred183_JPA23405); if (state.failed) return;

		}

	}
	// $ANTLR end synpred183_JPA2

	// $ANTLR start synpred186_JPA2
	public final void synpred186_JPA2_fragment() throws RecognitionException {
		// JPA2.g:367:7: ( aggregate_expression )
		// JPA2.g:367:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred186_JPA23429);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred186_JPA2

	// $ANTLR start synpred188_JPA2
	public final void synpred188_JPA2_fragment() throws RecognitionException {
		// JPA2.g:369:7: ( function_invocation )
		// JPA2.g:369:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred188_JPA23445);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred188_JPA2

	// $ANTLR start synpred194_JPA2
	public final void synpred194_JPA2_fragment() throws RecognitionException {
		// JPA2.g:377:7: ( aggregate_expression )
		// JPA2.g:377:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred194_JPA23504);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred194_JPA2

	// $ANTLR start synpred196_JPA2
	public final void synpred196_JPA2_fragment() throws RecognitionException {
		// JPA2.g:379:7: ( function_invocation )
		// JPA2.g:379:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred196_JPA23520);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred196_JPA2

	// $ANTLR start synpred198_JPA2
	public final void synpred198_JPA2_fragment() throws RecognitionException {
		// JPA2.g:383:7: ( path_expression )
		// JPA2.g:383:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred198_JPA23547);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred198_JPA2

	// $ANTLR start synpred201_JPA2
	public final void synpred201_JPA2_fragment() throws RecognitionException {
		// JPA2.g:386:7: ( aggregate_expression )
		// JPA2.g:386:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred201_JPA23571);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred201_JPA2

	// $ANTLR start synpred203_JPA2
	public final void synpred203_JPA2_fragment() throws RecognitionException {
		// JPA2.g:388:7: ( function_invocation )
		// JPA2.g:388:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred203_JPA23587);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred203_JPA2

	// $ANTLR start synpred205_JPA2
	public final void synpred205_JPA2_fragment() throws RecognitionException {
		// JPA2.g:390:7: ( date_time_timestamp_literal )
		// JPA2.g:390:7: date_time_timestamp_literal
		{
		pushFollow(FOLLOW_date_time_timestamp_literal_in_synpred205_JPA23603);
		date_time_timestamp_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred205_JPA2

	// $ANTLR start synpred243_JPA2
	public final void synpred243_JPA2_fragment() throws RecognitionException {
		// JPA2.g:441:7: ( literal )
		// JPA2.g:441:7: literal
		{
		pushFollow(FOLLOW_literal_in_synpred243_JPA24058);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred243_JPA2

	// $ANTLR start synpred244_JPA2
	public final void synpred244_JPA2_fragment() throws RecognitionException {
		// JPA2.g:442:7: ( path_expression )
		// JPA2.g:442:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred244_JPA24066);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred244_JPA2

	// $ANTLR start synpred245_JPA2
	public final void synpred245_JPA2_fragment() throws RecognitionException {
		// JPA2.g:443:7: ( input_parameter )
		// JPA2.g:443:7: input_parameter
		{
		pushFollow(FOLLOW_input_parameter_in_synpred245_JPA24074);
		input_parameter();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred245_JPA2

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
	public final boolean synpred196_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred196_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred148_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred148_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred243_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred243_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred166_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred166_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred163_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred163_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred168_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred168_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred176_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred176_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred137_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred137_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred245_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred245_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred201_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred201_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred244_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred244_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred194_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred194_JPA2_fragment(); // can never throw exception
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
		"\1\6\1\27\2\uffff\2\6\1\37\1\uffff\1\6\15\37\1\0\1\6";
	static final String DFA41_maxS =
		"\1\146\1\27\2\uffff\2\u0082\1\76\1\uffff\1\u008f\15\77\1\0\1\u008f";
	static final String DFA41_acceptS =
		"\2\uffff\1\1\1\3\3\uffff\1\2\20\uffff";
	static final String DFA41_specialS =
		"\26\uffff\1\0\1\uffff}>";
	static final String[] DFA41_transitionS = {
			"\1\2\2\uffff\1\1\16\uffff\2\2\11\uffff\1\2\102\uffff\1\3",
			"\1\4",
			"",
			"",
			"\1\2\2\uffff\1\2\1\uffff\1\5\2\uffff\1\6\3\uffff\1\2\4\uffff\4\2\10"+
			"\uffff\1\2\23\uffff\1\6\1\uffff\1\2\1\uffff\1\2\1\uffff\1\2\2\uffff\1"+
			"\2\6\uffff\1\2\4\uffff\1\2\1\uffff\1\2\5\uffff\3\2\15\uffff\1\2\1\uffff"+
			"\1\2\1\uffff\1\2\3\uffff\1\2\1\uffff\1\2\2\uffff\1\2\4\uffff\1\2\11\uffff"+
			"\1\2\1\uffff\1\2",
			"\1\2\2\uffff\1\2\4\uffff\1\6\3\uffff\1\2\4\uffff\4\2\10\uffff\1\2\23"+
			"\uffff\1\6\1\uffff\1\2\1\uffff\1\2\1\uffff\1\2\2\uffff\1\2\6\uffff\1"+
			"\2\4\uffff\1\2\1\uffff\1\2\5\uffff\3\2\15\uffff\1\2\1\uffff\1\2\1\uffff"+
			"\1\2\3\uffff\1\2\1\uffff\1\2\2\uffff\1\2\4\uffff\1\2\11\uffff\1\2\1\uffff"+
			"\1\2",
			"\1\7\36\uffff\1\10",
			"",
			"\1\21\2\uffff\1\22\4\uffff\1\14\11\uffff\1\16\1\17\3\uffff\1\15\1\uffff"+
			"\1\26\3\uffff\1\20\23\uffff\1\11\2\uffff\2\2\1\uffff\1\2\1\uffff\1\2"+
			"\21\uffff\1\23\11\uffff\1\25\5\uffff\1\25\3\uffff\1\13\1\uffff\1\25\7"+
			"\uffff\1\24\1\25\1\uffff\1\25\11\uffff\1\25\1\uffff\1\25\1\12\14\uffff"+
			"\1\25\2\uffff\1\25",
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
			"\1\21\2\uffff\1\22\4\uffff\1\14\11\uffff\1\16\1\17\3\uffff\1\15\1\uffff"+
			"\1\26\3\uffff\1\20\23\uffff\1\11\2\uffff\2\2\1\uffff\1\2\1\uffff\1\2"+
			"\21\uffff\1\23\11\uffff\1\25\5\uffff\1\25\3\uffff\1\13\1\uffff\1\25\7"+
			"\uffff\1\24\1\25\1\uffff\1\25\11\uffff\1\25\1\uffff\1\25\1\12\14\uffff"+
			"\1\25\2\uffff\1\25"
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
			return "182:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? arithmetic_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? arithmetic_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );";
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

	public static final BitSet FOLLOW_select_statement_in_ql_statement445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_update_statement_in_ql_statement449 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_delete_statement_in_ql_statement453 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_127_in_select_statement468 = new BitSet(new long[]{0x2A80000C07C44A40L,0x024A515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_select_clause_in_select_statement470 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_from_clause_in_select_statement472 = new BitSet(new long[]{0x000000002000C002L,0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_where_clause_in_select_statement475 = new BitSet(new long[]{0x000000002000C002L});
	public static final BitSet FOLLOW_groupby_clause_in_select_statement480 = new BitSet(new long[]{0x0000000020008002L});
	public static final BitSet FOLLOW_having_clause_in_select_statement485 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_orderby_clause_in_select_statement490 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_137_in_update_statement548 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_update_clause_in_update_statement550 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_where_clause_in_update_statement553 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_92_in_delete_statement589 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_delete_clause_in_delete_statement591 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000004000L});
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
	public static final BitSet FOLLOW_entity_name_in_range_variable_declaration763 = new BitSet(new long[]{0x0080000000004000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_range_variable_declaration766 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration770 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_join799 = new BitSet(new long[]{0x0080000000004000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_join_association_path_expression_in_join801 = new BitSet(new long[]{0x0080000000004000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_join804 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_identification_variable_in_join808 = new BitSet(new long[]{0x0000000000000002L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_join811 = new BitSet(new long[]{0x2A80000C0FC44240L,0x0042515807F07F81L,0x00000000000305CDL});
	public static final BitSet FOLLOW_conditional_expression_in_join813 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_fetch_join847 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_FETCH_in_fetch_join849 = new BitSet(new long[]{0x0080000000004000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join851 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFT_in_join_spec865 = new BitSet(new long[]{0x0000000040080000L});
	public static final BitSet FOLLOW_OUTER_in_join_spec869 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_INNER_in_join_spec875 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_JOIN_in_join_spec880 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression894 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_join_association_path_expression896 = new BitSet(new long[]{0x0080000823004242L,0xD00580A208020000L,0x0000000000009000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression899 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_join_association_path_expression900 = new BitSet(new long[]{0x0080000823004242L,0xD00580A208020000L,0x0000000000009000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression904 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_134_in_join_association_path_expression939 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression941 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_join_association_path_expression943 = new BitSet(new long[]{0x0080000823004240L,0xD00580A208020000L,0x0000000000009000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression946 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_join_association_path_expression947 = new BitSet(new long[]{0x0080000823004240L,0xD00580A208020000L,0x0000000000009000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression951 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_join_association_path_expression954 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_subtype_in_join_association_path_expression956 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_join_association_path_expression958 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_join_association_path_expression991 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_collection_member_declaration1004 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration1005 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_declaration1007 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration1009 = new BitSet(new long[]{0x0080000000004000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_collection_member_declaration1012 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration1016 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_qualified_identification_variable1045 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_96_in_qualified_identification_variable1053 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_identification_variable_in_qualified_identification_variable1054 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_qualified_identification_variable1055 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_106_in_map_field_identification_variable1062 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1063 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1064 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_139_in_map_field_identification_variable1068 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1069 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1070 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_path_expression1084 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_path_expression1086 = new BitSet(new long[]{0x0080000823004242L,0xD00580A208020000L,0x0000000000009000L});
	public static final BitSet FOLLOW_field_in_path_expression1089 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_path_expression1090 = new BitSet(new long[]{0x0080000823004242L,0xD00580A208020000L,0x0000000000009000L});
	public static final BitSet FOLLOW_field_in_path_expression1094 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_general_identification_variable1133 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_general_identification_variable1141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_update_clause1154 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_SET_in_update_clause1156 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1158 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_update_clause1161 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1163 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_update_item1205 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_update_item1207 = new BitSet(new long[]{0x2A80000C07C44240L,0x0062515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_new_value_in_update_item1209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_new_value1220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_new_value1228 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_117_in_new_value1236 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_delete_clause1250 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_delete_clause1252 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_select_clause1280 = new BitSet(new long[]{0x2A80000C07C44240L,0x024A515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_select_item_in_select_clause1284 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_select_clause1287 = new BitSet(new long[]{0x2A80000C07C44240L,0x024A515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_select_item_in_select_clause1289 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_select_expression_in_select_item1332 = new BitSet(new long[]{0x0080000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_select_item1336 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_result_variable_in_select_item1340 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_select_expression1353 = new BitSet(new long[]{0xAC00000000000002L});
	public static final BitSet FOLLOW_set_in_select_expression1356 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1372 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1382 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_select_expression1408 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_121_in_select_expression1416 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_select_expression1418 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1419 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_select_expression1420 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_constructor_expression_in_select_expression1428 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_115_in_constructor_expression1439 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_constructor_name_in_constructor_expression1441 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_constructor_expression1443 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1445 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_constructor_expression1448 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1450 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_constructor_expression1454 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_constructor_item1465 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_constructor_item1473 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_constructor_item1481 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_constructor_item1489 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1500 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1502 = new BitSet(new long[]{0x2A80000807844A40L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1504 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_aggregate_expression1508 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_aggregate_expression1543 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1545 = new BitSet(new long[]{0x0080000000004800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1547 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_count_argument_in_aggregate_expression1551 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1553 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_aggregate_expression1588 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_count_argument1625 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_count_argument1629 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_142_in_where_clause1642 = new BitSet(new long[]{0x2A80000C0FC44240L,0x0042515807F07F81L,0x00000000000305CDL});
	public static final BitSet FOLLOW_conditional_expression_in_where_clause1644 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_groupby_clause1666 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_groupby_clause1668 = new BitSet(new long[]{0x0080000000004000L,0x0000001000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1670 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_groupby_clause1673 = new BitSet(new long[]{0x0080000000004000L,0x0000001000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1675 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_groupby_item1709 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_groupby_item1713 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extract_function_in_groupby_item1717 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HAVING_in_having_clause1728 = new BitSet(new long[]{0x2A80000C0FC44240L,0x0042515807F07F81L,0x00000000000305CDL});
	public static final BitSet FOLLOW_conditional_expression_in_having_clause1730 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderby_clause1741 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_orderby_clause1743 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042555007F05081L,0x0000000000030D8DL});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1745 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_orderby_clause1748 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042555007F05081L,0x0000000000030D8DL});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1750 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1784 = new BitSet(new long[]{0x0000000000000422L,0x0180000000000000L});
	public static final BitSet FOLLOW_sort_in_orderby_item1786 = new BitSet(new long[]{0x0000000000000002L,0x0180000000000000L});
	public static final BitSet FOLLOW_sortNulls_in_orderby_item1789 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_orderby_variable1824 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_orderby_variable1828 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_orderby_variable1832 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_orderby_variable1836 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_orderby_variable1840 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_subquery1887 = new BitSet(new long[]{0x0000000000000000L,0x8000000000000000L});
	public static final BitSet FOLLOW_127_in_subquery1889 = new BitSet(new long[]{0x2A80000C07C44A40L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_simple_select_clause_in_subquery1891 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_subquery_from_clause_in_subquery1893 = new BitSet(new long[]{0x000000008000C000L,0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_where_clause_in_subquery1896 = new BitSet(new long[]{0x000000008000C000L});
	public static final BitSet FOLLOW_groupby_clause_in_subquery1901 = new BitSet(new long[]{0x0000000080008000L});
	public static final BitSet FOLLOW_having_clause_in_subquery1906 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_subquery1912 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_subquery_from_clause1962 = new BitSet(new long[]{0x0080000000010000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1964 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_subquery_from_clause1967 = new BitSet(new long[]{0x0080000000010000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1969 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration2007 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration2015 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_subselect_identification_variable_declaration2017 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration2019 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_join_in_subselect_identification_variable_declaration2022 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration2032 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression2043 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_derived_path_expression2044 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_path_expression2045 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression2053 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_derived_path_expression2054 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_path_expression2055 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_derived_path_in_general_derived_path2066 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_treated_derived_path_in_general_derived_path2074 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_62_in_general_derived_path2076 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_general_derived_path2077 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_simple_derived_path2095 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_134_in_treated_derived_path2112 = new BitSet(new long[]{0x0080000000000000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_general_derived_path_in_treated_derived_path2113 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_81_in_treated_derived_path2115 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_subtype_in_treated_derived_path2117 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_treated_derived_path2119 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_derived_collection_member_declaration2130 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2132 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_derived_collection_member_declaration2133 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2135 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_derived_collection_member_declaration2137 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_collection_member_declaration2140 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause2153 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause2157 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_simple_select_expression2197 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_select_expression2205 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression2213 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_select_expression2221 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_scalar_expression2232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_scalar_expression2240 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_scalar_expression2248 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_scalar_expression2256 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_scalar_expression2264 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_scalar_expression2272 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_scalar_expression2280 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2292 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_OR_in_conditional_expression2296 = new BitSet(new long[]{0x2A80000C0FC44240L,0x0042515807F07F81L,0x00000000000305CDL});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2298 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2312 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_conditional_term2316 = new BitSet(new long[]{0x2A80000C0FC44240L,0x0042515807F07F81L,0x00000000000305CDL});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2318 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_NOT_in_conditional_factor2332 = new BitSet(new long[]{0x2A80000C0FC44240L,0x0042515807F07F81L,0x00000000000305CDL});
	public static final BitSet FOLLOW_conditional_primary_in_conditional_factor2336 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_conditional_primary2347 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_conditional_primary2371 = new BitSet(new long[]{0x2A80000C0FC44240L,0x0042515807F07F81L,0x00000000000305CDL});
	public static final BitSet FOLLOW_conditional_expression_in_conditional_primary2372 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_conditional_primary2373 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression2384 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_simple_cond_expression2392 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_simple_cond_expression2400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_simple_cond_expression2408 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression2416 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2424 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression2432 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression2440 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression2448 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2477 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2485 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2493 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_72_in_date_between_macro_expression2505 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2507 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2509 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_between_macro_expression2511 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
	public static final BitSet FOLLOW_116_in_date_between_macro_expression2513 = new BitSet(new long[]{0x3800000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2516 = new BitSet(new long[]{0x0000000000040000L,0x0000000000000001L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2524 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_between_macro_expression2528 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
	public static final BitSet FOLLOW_116_in_date_between_macro_expression2530 = new BitSet(new long[]{0x3800000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2533 = new BitSet(new long[]{0x0000000000040000L,0x0000000000000001L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2541 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_between_macro_expression2545 = new BitSet(new long[]{0x0000000000000000L,0x4005008008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2547 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2570 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_74_in_date_before_macro_expression2582 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2584 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2586 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_before_macro_expression2588 = new BitSet(new long[]{0x0280000004004000L,0x0000000000000080L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2591 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2595 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2598 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_73_in_date_after_macro_expression2610 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2612 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2614 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_after_macro_expression2616 = new BitSet(new long[]{0x0280000004004000L,0x0000000000000080L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2619 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2623 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2626 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_date_equals_macro_expression2638 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2640 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2642 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_date_equals_macro_expression2644 = new BitSet(new long[]{0x0280000004004000L,0x0000000000000080L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2647 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2651 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_77_in_date_today_macro_expression2666 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2668 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2670 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2672 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2685 = new BitSet(new long[]{0x0000000008000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2688 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_between_expression2692 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2694 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2696 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2698 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2706 = new BitSet(new long[]{0x0000000008000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2709 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_between_expression2713 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2715 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2717 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2719 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2727 = new BitSet(new long[]{0x0000000008000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2730 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_between_expression2734 = new BitSet(new long[]{0x0280000807804240L,0x0040005007701080L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2736 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2738 = new BitSet(new long[]{0x0280000807804240L,0x0040005007701080L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2740 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2752 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_type_discriminator_in_in_expression2756 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_identification_variable_in_in_expression2760 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_NOT_in_in_expression2764 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_IN_in_in_expression2768 = new BitSet(new long[]{0x0200000004800000L,0x0000000000000080L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2784 = new BitSet(new long[]{0x0200000404040000L,0x0000000000000081L});
	public static final BitSet FOLLOW_in_item_in_in_expression2786 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_in_expression2789 = new BitSet(new long[]{0x0200000404040000L,0x0000000000000081L});
	public static final BitSet FOLLOW_in_item_in_in_expression2791 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2795 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_in_expression2811 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_valued_input_parameter_in_in_expression2827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2843 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2845 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2847 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_in_item2875 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_in_item2879 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_single_valued_input_parameter_in_in_item2883 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2894 = new BitSet(new long[]{0x0000000008000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_NOT_in_like_expression2897 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_109_in_like_expression2901 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2904 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
	public static final BitSet FOLLOW_pattern_value_in_like_expression2908 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
	public static final BitSet FOLLOW_input_parameter_in_like_expression2912 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
	public static final BitSet FOLLOW_98_in_like_expression2915 = new BitSet(new long[]{0x0000001400000000L});
	public static final BitSet FOLLOW_escape_character_in_like_expression2917 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_null_comparison_expression2931 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression2935 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_null_comparison_expression2939 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_null_comparison_expression2942 = new BitSet(new long[]{0x0000000008000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_NOT_in_null_comparison_expression2945 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_117_in_null_comparison_expression2949 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression2960 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_empty_collection_comparison_expression2962 = new BitSet(new long[]{0x0000000008000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_NOT_in_empty_collection_comparison_expression2965 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_94_in_empty_collection_comparison_expression2969 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_or_value_expression_in_collection_member_expression2980 = new BitSet(new long[]{0x0000000008000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_NOT_in_collection_member_expression2984 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_111_in_collection_member_expression2988 = new BitSet(new long[]{0x0080000000004000L,0x0400000000000000L});
	public static final BitSet FOLLOW_122_in_collection_member_expression2991 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_expression2995 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_or_value_expression3006 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression3014 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_entity_or_value_expression3022 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_or_value_expression3033 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_or_value_expression3041 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simple_entity_or_value_expression3049 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_exists_expression3061 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_99_in_exists_expression3065 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_subquery_in_exists_expression3067 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_all_or_any_expression3078 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_subquery_in_all_or_any_expression3091 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3102 = new BitSet(new long[]{0x0000000000000000L,0x200000000000007EL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3105 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F19080L,0x000000000000048AL});
	public static final BitSet FOLLOW_125_in_comparison_expression3109 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F19080L,0x000000000000048AL});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3117 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3126 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_comparison_expression3128 = new BitSet(new long[]{0x0280000004804000L,0x0040005000719080L,0x0000000000030002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3137 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3150 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_comparison_expression3152 = new BitSet(new long[]{0x0280000004804000L,0x0040000000518080L,0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3159 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3163 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3172 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3174 = new BitSet(new long[]{0x0280000807804240L,0x0040005007719080L,0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3177 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3181 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3190 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_comparison_expression3192 = new BitSet(new long[]{0x0280000004004000L,0x0000000000018080L,0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3201 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3205 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3214 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_comparison_expression3216 = new BitSet(new long[]{0x0280000004000000L,0x0000000000000080L,0x0000000000000100L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3232 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3234 = new BitSet(new long[]{0x2A80000807844240L,0x004251500071D081L,0x0000000000000007L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3237 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3241 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3305 = new BitSet(new long[]{0x2800000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_expression3307 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3323 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3334 = new BitSet(new long[]{0x8400000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_term3336 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3345 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3353 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor3376 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_arithmetic_primary3387 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary3395 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary3403 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_arithmetic_primary3404 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary3405 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary3413 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary3421 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary3429 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_arithmetic_primary3437 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_arithmetic_primary3445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_arithmetic_primary3453 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_arithmetic_primary3461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_string_expression3472 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_string_expression3480 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_string_expression3488 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_strings_in_string_expression3496 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_string_expression3504 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_string_expression3512 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_string_expression3520 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_string_expression3528 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_string_expression3536 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_datetime_expression3547 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_datetime_expression3555 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_expression3563 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_datetime_expression3571 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_datetime_expression3579 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_datetime_expression3587 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_datetime_expression3595 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_datetime_expression3603 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_datetime_expression3611 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_boolean_expression3622 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_literal_in_boolean_expression3630 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_boolean_expression3638 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_boolean_expression3646 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_boolean_expression3654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_boolean_expression3662 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_boolean_expression3670 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_enum_expression3681 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_literal_in_enum_expression3689 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_enum_expression3697 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_enum_expression3705 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_enum_expression3713 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_expression3724 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3732 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3743 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3751 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_entity_type_expression3762 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_literal_in_entity_type_expression3770 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_entity_type_expression3778 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_136_in_type_discriminator3789 = new BitSet(new long[]{0x0280000004004000L,0x0000040000000080L,0x0000000000000800L});
	public static final BitSet FOLLOW_general_identification_variable_in_type_discriminator3792 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_path_expression_in_type_discriminator3796 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_type_discriminator3800 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_type_discriminator3803 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_108_in_functions_returning_numerics3814 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3815 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3816 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_110_in_functions_returning_numerics3824 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3826 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_numerics3827 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3829 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_numerics3831 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3832 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3835 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_78_in_functions_returning_numerics3843 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3844 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3845 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_130_in_functions_returning_numerics3853 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3854 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3855 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_113_in_functions_returning_numerics3863 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3864 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_numerics3865 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3867 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3868 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_128_in_functions_returning_numerics3876 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3877 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3878 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_104_in_functions_returning_numerics3886 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_identification_variable_in_functions_returning_numerics3887 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3888 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_87_in_functions_returning_strings3926 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3927 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_strings3928 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3930 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_strings3933 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3935 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3938 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_functions_returning_strings3946 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3948 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_strings3949 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3951 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_functions_returning_strings3954 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3956 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3959 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_135_in_functions_returning_strings3967 = new BitSet(new long[]{0x0280001C07C04240L,0x0040087000F81080L,0x00000000000004A8L});
	public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings3970 = new BitSet(new long[]{0x0000001000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_trim_character_in_functions_returning_strings3975 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_101_in_functions_returning_strings3979 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3983 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3985 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LOWER_in_functions_returning_strings3993 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3995 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3996 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3997 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_138_in_functions_returning_strings4005 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4006 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4007 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_function_invocation4037 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_function_name_in_function_invocation4038 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_function_invocation4041 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_function_arg_in_function_invocation4043 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_function_invocation4047 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_function_arg4058 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_function_arg4066 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_function_arg4074 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_function_arg4082 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_case_expression_in_case_expression4093 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_case_expression_in_case_expression4101 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coalesce_expression_in_case_expression4109 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullif_expression_in_case_expression4117 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_84_in_general_case_expression4128 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4130 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4133 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_93_in_general_case_expression4137 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_general_case_expression4139 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_general_case_expression4141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_141_in_when_clause4152 = new BitSet(new long[]{0x2A80000C0FC44240L,0x0042515807F07F81L,0x00000000000305CDL});
	public static final BitSet FOLLOW_conditional_expression_in_when_clause4154 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_132_in_when_clause4156 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_when_clause4158 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_84_in_simple_case_expression4169 = new BitSet(new long[]{0x0080000000004000L,0x0000000000000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_case_operand_in_simple_case_expression4171 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4173 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4176 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_93_in_simple_case_expression4180 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_simple_case_expression4182 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_simple_case_expression4184 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_case_operand4195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_case_operand4203 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_141_in_simple_when_clause4214 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4216 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_132_in_simple_when_clause4218 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_86_in_coalesce_expression4231 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4232 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_coalesce_expression4235 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4237 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_coalesce_expression4240 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_118_in_nullif_expression4251 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4252 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_nullif_expression4254 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4256 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_nullif_expression4257 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_85_in_extension_functions4269 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_function_arg_in_extension_functions4271 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_WORD_in_extension_functions4273 = new BitSet(new long[]{0x0000000080800000L});
	public static final BitSet FOLLOW_LPAREN_in_extension_functions4276 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4277 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_60_in_extension_functions4280 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4282 = new BitSet(new long[]{0x1000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4287 = new BitSet(new long[]{0x0000000080800000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4291 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extract_function_in_extension_functions4299 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_76_in_extension_functions4307 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_extension_functions4309 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_enum_value_literal_in_extension_functions4311 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4313 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_100_in_extract_function4335 = new BitSet(new long[]{0x0000000000000000L,0x5005008208000000L,0x0000000000009000L});
	public static final BitSet FOLLOW_date_part_in_extract_function4337 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_101_in_extract_function4339 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_function_arg_in_extract_function4341 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_extract_function4343 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_input_parameter4400 = new BitSet(new long[]{0x0000000000040000L,0x0000000000000001L});
	public static final BitSet FOLLOW_numeric_literal_in_input_parameter4402 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter4425 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_57_in_input_parameter4446 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_WORD_in_input_parameter4448 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_146_in_input_parameter4450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_literal4478 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4490 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_literal4502 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_field4535 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_127_in_field4539 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_field4543 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_field4547 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_field4551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MAX_in_field4555 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MIN_in_field4559 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SUM_in_field4563 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AVG_in_field4567 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_field4571 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_field4575 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_111_in_field4579 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_part_in_field4583 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4611 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_62_in_parameter_name4614 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4617 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_TRIM_CHARACTER_in_trim_character4647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string_literal4658 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_64_in_numeric_literal4670 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal4674 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_object_field4686 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_embeddable_object_field4697 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_collection_valued_field4708 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_name4719 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_subtype4730 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_type_literal4741 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_function_name4752 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_state_field4763 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_result_variable4774 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_superquery_identification_variable4785 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_date_time_timestamp_literal4796 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_pattern_value4807 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_collection_valued_input_parameter4818 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_single_valued_input_parameter4829 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_value_literal4840 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_62_in_enum_value_literal4843 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_WORD_in_enum_value_literal4846 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred21_JPA2904 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred30_JPA21094 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred33_JPA21220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_synpred34_JPA21228 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred43_JPA21353 = new BitSet(new long[]{0xAC00000000000002L});
	public static final BitSet FOLLOW_set_in_synpred43_JPA21356 = new BitSet(new long[]{0x2A80000C07C44240L,0x0042515007F05081L,0x000000000003058DL});
	public static final BitSet FOLLOW_scalar_expression_in_synpred43_JPA21372 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred44_JPA21382 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred45_JPA21400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred46_JPA21408 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred49_JPA21465 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred50_JPA21473 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred51_JPA21481 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_synpred53_JPA21500 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred53_JPA21502 = new BitSet(new long[]{0x2A80000807844A40L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred53_JPA21504 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred53_JPA21508 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred53_JPA21509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_synpred55_JPA21543 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred55_JPA21545 = new BitSet(new long[]{0x0080000000004800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred55_JPA21547 = new BitSet(new long[]{0x0080000000004000L});
	public static final BitSet FOLLOW_count_argument_in_synpred55_JPA21551 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred55_JPA21553 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred67_JPA21824 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_synpred68_JPA21828 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_synpred69_JPA21832 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred70_JPA21836 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_synpred80_JPA22043 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_62_in_synpred80_JPA22044 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_synpred80_JPA22045 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred85_JPA22197 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred86_JPA22205 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred87_JPA22213 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred88_JPA22232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred89_JPA22240 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred90_JPA22248 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred91_JPA22256 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred92_JPA22264 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_synpred93_JPA22272 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_synpred96_JPA22332 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_synpred97_JPA22347 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_synpred98_JPA22384 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_synpred99_JPA22392 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_synpred100_JPA22400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_synpred101_JPA22408 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_synpred102_JPA22416 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred103_JPA22424 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_synpred104_JPA22432 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred123_JPA22685 = new BitSet(new long[]{0x0000000008000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_synpred123_JPA22688 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_synpred123_JPA22692 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred123_JPA22694 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred123_JPA22696 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred123_JPA22698 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred125_JPA22706 = new BitSet(new long[]{0x0000000008000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_NOT_in_synpred125_JPA22709 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_82_in_synpred125_JPA22713 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_synpred125_JPA22715 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred125_JPA22717 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F01080L,0x0000000000000488L});
	public static final BitSet FOLLOW_string_expression_in_synpred125_JPA22719 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred137_JPA22904 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_pattern_value_in_synpred138_JPA22908 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred140_JPA22931 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred148_JPA23033 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred155_JPA23102 = new BitSet(new long[]{0x0000000000000000L,0x200000000000007EL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred155_JPA23105 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F19080L,0x000000000000048AL});
	public static final BitSet FOLLOW_125_in_synpred155_JPA23109 = new BitSet(new long[]{0x0280000C07C04240L,0x0040005000F19080L,0x000000000000048AL});
	public static final BitSet FOLLOW_string_expression_in_synpred155_JPA23113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred155_JPA23117 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred158_JPA23126 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_synpred158_JPA23128 = new BitSet(new long[]{0x0280000004804000L,0x0040005000719080L,0x0000000000030002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred158_JPA23137 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred158_JPA23141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred161_JPA23150 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_synpred161_JPA23152 = new BitSet(new long[]{0x0280000004804000L,0x0040000000518080L,0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred161_JPA23159 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred161_JPA23163 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred163_JPA23172 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007EL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred163_JPA23174 = new BitSet(new long[]{0x0280000807804240L,0x0040005007719080L,0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred163_JPA23177 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred163_JPA23181 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred166_JPA23190 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_synpred166_JPA23192 = new BitSet(new long[]{0x0280000004004000L,0x0000000000018080L,0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred166_JPA23201 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred166_JPA23205 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred168_JPA23214 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000018L});
	public static final BitSet FOLLOW_set_in_synpred168_JPA23216 = new BitSet(new long[]{0x0280000004000000L,0x0000000000000080L,0x0000000000000100L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred168_JPA23224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred176_JPA23305 = new BitSet(new long[]{0x2800000000000000L});
	public static final BitSet FOLLOW_set_in_synpred176_JPA23307 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred176_JPA23315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred178_JPA23334 = new BitSet(new long[]{0x8400000000000000L});
	public static final BitSet FOLLOW_set_in_synpred178_JPA23336 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred178_JPA23345 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred183_JPA23403 = new BitSet(new long[]{0x2A80000807844240L,0x0042515000705081L,0x0000000000000005L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred183_JPA23404 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred183_JPA23405 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred186_JPA23429 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred188_JPA23445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred194_JPA23504 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred196_JPA23520 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred198_JPA23547 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred201_JPA23571 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred203_JPA23587 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_synpred205_JPA23603 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred243_JPA24058 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred244_JPA24066 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_synpred245_JPA24074 = new BitSet(new long[]{0x0000000000000002L});

	@Override
	public void emitErrorMessage(String msg) {
		//do nothing
	}

	protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
		throw new MismatchedTokenException(ttype, input);
	}
}
