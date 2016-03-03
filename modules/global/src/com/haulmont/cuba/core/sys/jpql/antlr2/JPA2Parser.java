// $ANTLR 3.5.2 JPA2.g 2016-03-03 12:20:54

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
		"RPAREN", "RUSSIAN_SYMBOLS", "STRING_LITERAL", "SUM", "TRIM_CHARACTER", 
		"T_AGGREGATE_EXPR", "T_COLLECTION_MEMBER", "T_CONDITION", "T_GROUP_BY", 
		"T_ID_VAR", "T_JOIN_VAR", "T_ORDER_BY", "T_ORDER_BY_FIELD", "T_PARAMETER", 
		"T_QUERY", "T_SELECTED_ENTITY", "T_SELECTED_FIELD", "T_SELECTED_ITEM", 
		"T_SELECTED_ITEMS", "T_SIMPLE_CONDITION", "T_SOURCE", "T_SOURCES", "WORD", 
		"WS", "'${'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", "'0x'", "'<'", 
		"'<='", "'<>'", "'='", "'>'", "'>='", "'?'", "'@BETWEEN'", "'@DATEAFTER'", 
		"'@DATEBEFORE'", "'@DATEEQUALS'", "'@TODAY'", "'ABS('", "'ALL'", "'ANY'", 
		"'AS'", "'BETWEEN'", "'BOTH'", "'CASE'", "'CAST('", "'COALESCE('", "'CONCAT('", 
		"'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'DAY'", "'DELETE'", 
		"'ELSE'", "'EMPTY'", "'END'", "'ENTRY('", "'EPOCH'", "'ESCAPE'", "'EXISTS'", 
		"'EXTRACT('", "'FROM'", "'FUNCTION('", "'HOUR'", "'INDEX('", "'IS'", "'KEY('", 
		"'LEADING'", "'LENGTH('", "'LIKE'", "'LOCATE('", "'MEMBER'", "'MINUTE'", 
		"'MOD('", "'MONTH'", "'NEW'", "'NOW'", "'NULL'", "'NULLIF('", "'OBJECT'", 
		"'OF'", "'ON'", "'QUARTER'", "'REGEXP'", "'SECOND'", "'SELECT'", "'SET'", 
		"'SIZE('", "'SOME'", "'SQRT('", "'SUBSTRING('", "'THEN'", "'TRAILING'", 
		"'TREAT('", "'TRIM('", "'TYPE('", "'UPDATE'", "'UPPER('", "'VALUE('", 
		"'WEEK'", "'WHEN'", "'WHERE'", "'YEAR'", "'false'", "'true'", "'}'"
	};
	public static final int EOF=-1;
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
	public static final int STRING_LITERAL=33;
	public static final int SUM=34;
	public static final int TRIM_CHARACTER=35;
	public static final int T_AGGREGATE_EXPR=36;
	public static final int T_COLLECTION_MEMBER=37;
	public static final int T_CONDITION=38;
	public static final int T_GROUP_BY=39;
	public static final int T_ID_VAR=40;
	public static final int T_JOIN_VAR=41;
	public static final int T_ORDER_BY=42;
	public static final int T_ORDER_BY_FIELD=43;
	public static final int T_PARAMETER=44;
	public static final int T_QUERY=45;
	public static final int T_SELECTED_ENTITY=46;
	public static final int T_SELECTED_FIELD=47;
	public static final int T_SELECTED_ITEM=48;
	public static final int T_SELECTED_ITEMS=49;
	public static final int T_SIMPLE_CONDITION=50;
	public static final int T_SOURCE=51;
	public static final int T_SOURCES=52;
	public static final int WORD=53;
	public static final int WS=54;

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
	// JPA2.g:77:1: ql_statement : ( select_statement | update_statement | delete_statement );
	public final JPA2Parser.ql_statement_return ql_statement() throws RecognitionException {
		JPA2Parser.ql_statement_return retval = new JPA2Parser.ql_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope select_statement1 =null;
		ParserRuleReturnScope update_statement2 =null;
		ParserRuleReturnScope delete_statement3 =null;


		try {
			// JPA2.g:78:5: ( select_statement | update_statement | delete_statement )
			int alt1=3;
			switch ( input.LA(1) ) {
			case 122:
				{
				alt1=1;
				}
				break;
			case 133:
				{
				alt1=2;
				}
				break;
			case 89:
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
					// JPA2.g:78:7: select_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_select_statement_in_ql_statement427);
					select_statement1=select_statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, select_statement1.getTree());

					}
					break;
				case 2 :
					// JPA2.g:78:26: update_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_update_statement_in_ql_statement431);
					update_statement2=update_statement();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, update_statement2.getTree());

					}
					break;
				case 3 :
					// JPA2.g:78:45: delete_statement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_delete_statement_in_ql_statement435);
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
	// JPA2.g:80:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
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
		RewriteRuleTokenStream stream_122=new RewriteRuleTokenStream(adaptor,"token 122");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
		RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
		RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");

		try {
			// JPA2.g:81:6: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
			// JPA2.g:81:8: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )?
			{
			sl=(Token)match(input,122,FOLLOW_122_in_select_statement450); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_122.add(sl);

			pushFollow(FOLLOW_select_clause_in_select_statement452);
			select_clause4=select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_clause.add(select_clause4.getTree());
			pushFollow(FOLLOW_from_clause_in_select_statement454);
			from_clause5=from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_from_clause.add(from_clause5.getTree());
			// JPA2.g:81:46: ( where_clause )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==138) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// JPA2.g:81:47: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_select_statement457);
					where_clause6=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause6.getTree());
					}
					break;

			}

			// JPA2.g:81:62: ( groupby_clause )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==GROUP) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// JPA2.g:81:63: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_select_statement462);
					groupby_clause7=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause7.getTree());
					}
					break;

			}

			// JPA2.g:81:80: ( having_clause )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==HAVING) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// JPA2.g:81:81: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_select_statement467);
					having_clause8=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause8.getTree());
					}
					break;

			}

			// JPA2.g:81:97: ( orderby_clause )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==ORDER) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// JPA2.g:81:98: orderby_clause
					{
					pushFollow(FOLLOW_orderby_clause_in_select_statement472);
					orderby_clause9=orderby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause9.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: where_clause, groupby_clause, orderby_clause, select_clause, from_clause, having_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 82:6: -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
			{
				// JPA2.g:82:9: ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, sl), root_1);
				// JPA2.g:82:35: ( select_clause )?
				if ( stream_select_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_select_clause.nextTree());
				}
				stream_select_clause.reset();

				adaptor.addChild(root_1, stream_from_clause.nextTree());
				// JPA2.g:82:64: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:82:80: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:82:98: ( having_clause )?
				if ( stream_having_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_having_clause.nextTree());
				}
				stream_having_clause.reset();

				// JPA2.g:82:115: ( orderby_clause )?
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
	// JPA2.g:84:1: update_statement : up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) ;
	public final JPA2Parser.update_statement_return update_statement() throws RecognitionException {
		JPA2Parser.update_statement_return retval = new JPA2Parser.update_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token up=null;
		ParserRuleReturnScope update_clause10 =null;
		ParserRuleReturnScope where_clause11 =null;

		Object up_tree=null;
		RewriteRuleTokenStream stream_133=new RewriteRuleTokenStream(adaptor,"token 133");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_update_clause=new RewriteRuleSubtreeStream(adaptor,"rule update_clause");

		try {
			// JPA2.g:85:5: (up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) )
			// JPA2.g:85:7: up= 'UPDATE' update_clause ( where_clause )?
			{
			up=(Token)match(input,133,FOLLOW_133_in_update_statement530); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_133.add(up);

			pushFollow(FOLLOW_update_clause_in_update_statement532);
			update_clause10=update_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_clause.add(update_clause10.getTree());
			// JPA2.g:85:33: ( where_clause )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==138) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// JPA2.g:85:34: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_update_statement535);
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
			// 86:5: -> ^( T_QUERY[$up] update_clause ( where_clause )? )
			{
				// JPA2.g:86:8: ^( T_QUERY[$up] update_clause ( where_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, up), root_1);
				adaptor.addChild(root_1, stream_update_clause.nextTree());
				// JPA2.g:86:48: ( where_clause )?
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
	// JPA2.g:87:1: delete_statement : dl= 'DELETE' 'FROM' delete_clause ( where_clause )? -> ^( T_QUERY[$dl] delete_clause ( where_clause )? ) ;
	public final JPA2Parser.delete_statement_return delete_statement() throws RecognitionException {
		JPA2Parser.delete_statement_return retval = new JPA2Parser.delete_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token dl=null;
		Token string_literal12=null;
		ParserRuleReturnScope delete_clause13 =null;
		ParserRuleReturnScope where_clause14 =null;

		Object dl_tree=null;
		Object string_literal12_tree=null;
		RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");
		RewriteRuleTokenStream stream_98=new RewriteRuleTokenStream(adaptor,"token 98");
		RewriteRuleSubtreeStream stream_delete_clause=new RewriteRuleSubtreeStream(adaptor,"rule delete_clause");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");

		try {
			// JPA2.g:88:5: (dl= 'DELETE' 'FROM' delete_clause ( where_clause )? -> ^( T_QUERY[$dl] delete_clause ( where_clause )? ) )
			// JPA2.g:88:7: dl= 'DELETE' 'FROM' delete_clause ( where_clause )?
			{
			dl=(Token)match(input,89,FOLLOW_89_in_delete_statement571); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_89.add(dl);

			string_literal12=(Token)match(input,98,FOLLOW_98_in_delete_statement573); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_98.add(string_literal12);

			pushFollow(FOLLOW_delete_clause_in_delete_statement575);
			delete_clause13=delete_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_delete_clause.add(delete_clause13.getTree());
			// JPA2.g:88:40: ( where_clause )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==138) ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					// JPA2.g:88:41: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_delete_statement578);
					where_clause14=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause14.getTree());
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
			// 89:5: -> ^( T_QUERY[$dl] delete_clause ( where_clause )? )
			{
				// JPA2.g:89:8: ^( T_QUERY[$dl] delete_clause ( where_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, dl), root_1);
				adaptor.addChild(root_1, stream_delete_clause.nextTree());
				// JPA2.g:89:48: ( where_clause )?
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
	// JPA2.g:91:1: from_clause : fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) ;
	public final JPA2Parser.from_clause_return from_clause() throws RecognitionException {
		JPA2Parser.from_clause_return retval = new JPA2Parser.from_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token fr=null;
		Token char_literal16=null;
		ParserRuleReturnScope identification_variable_declaration15 =null;
		ParserRuleReturnScope identification_variable_declaration_or_collection_member_declaration17 =null;

		Object fr_tree=null;
		Object char_literal16_tree=null;
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_98=new RewriteRuleTokenStream(adaptor,"token 98");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
		RewriteRuleSubtreeStream stream_identification_variable_declaration_or_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration_or_collection_member_declaration");

		try {
			// JPA2.g:92:6: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) )
			// JPA2.g:92:8: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )*
			{
			fr=(Token)match(input,98,FOLLOW_98_in_from_clause616); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_98.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_from_clause618);
			identification_variable_declaration15=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration15.getTree());
			// JPA2.g:92:54: ( ',' identification_variable_declaration_or_collection_member_declaration )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==58) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// JPA2.g:92:55: ',' identification_variable_declaration_or_collection_member_declaration
					{
					char_literal16=(Token)match(input,58,FOLLOW_58_in_from_clause621); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal16);

					pushFollow(FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause623);
					identification_variable_declaration_or_collection_member_declaration17=identification_variable_declaration_or_collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable_declaration_or_collection_member_declaration.add(identification_variable_declaration_or_collection_member_declaration17.getTree());
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
			// 93:6: -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
			{
				// JPA2.g:93:9: ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
				// JPA2.g:93:72: ( identification_variable_declaration_or_collection_member_declaration )*
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
	// JPA2.g:94:1: identification_variable_declaration_or_collection_member_declaration : ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) );
	public final JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return identification_variable_declaration_or_collection_member_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return retval = new JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable_declaration18 =null;
		ParserRuleReturnScope collection_member_declaration19 =null;

		RewriteRuleSubtreeStream stream_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule collection_member_declaration");

		try {
			// JPA2.g:95:6: ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) )
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
					// JPA2.g:95:8: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration657);
					identification_variable_declaration18=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration18.getTree());

					}
					break;
				case 2 :
					// JPA2.g:96:8: collection_member_declaration
					{
					pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration666);
					collection_member_declaration19=collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_collection_member_declaration.add(collection_member_declaration19.getTree());
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
					// 96:38: -> ^( T_SOURCE collection_member_declaration )
					{
						// JPA2.g:96:41: ^( T_SOURCE collection_member_declaration )
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
	// JPA2.g:98:1: identification_variable_declaration : range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) ;
	public final JPA2Parser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_return retval = new JPA2Parser.identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope range_variable_declaration20 =null;
		ParserRuleReturnScope joined_clause21 =null;

		RewriteRuleSubtreeStream stream_range_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration");
		RewriteRuleSubtreeStream stream_joined_clause=new RewriteRuleSubtreeStream(adaptor,"rule joined_clause");

		try {
			// JPA2.g:99:6: ( range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) )
			// JPA2.g:99:8: range_variable_declaration ( joined_clause )*
			{
			pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration690);
			range_variable_declaration20=range_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range_variable_declaration.add(range_variable_declaration20.getTree());
			// JPA2.g:99:35: ( joined_clause )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==INNER||(LA10_0 >= JOIN && LA10_0 <= LEFT)) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// JPA2.g:99:35: joined_clause
					{
					pushFollow(FOLLOW_joined_clause_in_identification_variable_declaration692);
					joined_clause21=joined_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_joined_clause.add(joined_clause21.getTree());
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
			// 100:6: -> ^( T_SOURCE range_variable_declaration ( joined_clause )* )
			{
				// JPA2.g:100:9: ^( T_SOURCE range_variable_declaration ( joined_clause )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_1);
				adaptor.addChild(root_1, stream_range_variable_declaration.nextTree());
				// JPA2.g:100:68: ( joined_clause )*
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
	// JPA2.g:101:1: join_section : ( joined_clause )* ;
	public final JPA2Parser.join_section_return join_section() throws RecognitionException {
		JPA2Parser.join_section_return retval = new JPA2Parser.join_section_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope joined_clause22 =null;


		try {
			// JPA2.g:101:14: ( ( joined_clause )* )
			// JPA2.g:102:5: ( joined_clause )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:102:5: ( joined_clause )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( (LA11_0==INNER||(LA11_0 >= JOIN && LA11_0 <= LEFT)) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// JPA2.g:102:5: joined_clause
					{
					pushFollow(FOLLOW_joined_clause_in_join_section723);
					joined_clause22=joined_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, joined_clause22.getTree());

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
	// JPA2.g:103:1: joined_clause : ( join | fetch_join );
	public final JPA2Parser.joined_clause_return joined_clause() throws RecognitionException {
		JPA2Parser.joined_clause_return retval = new JPA2Parser.joined_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope join23 =null;
		ParserRuleReturnScope fetch_join24 =null;


		try {
			// JPA2.g:103:15: ( join | fetch_join )
			int alt12=2;
			switch ( input.LA(1) ) {
			case LEFT:
				{
				int LA12_1 = input.LA(2);
				if ( (LA12_1==OUTER) ) {
					int LA12_4 = input.LA(3);
					if ( (LA12_4==JOIN) ) {
						int LA12_3 = input.LA(4);
						if ( (LA12_3==WORD||LA12_3==130) ) {
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
					if ( (LA12_3==WORD||LA12_3==130) ) {
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
					if ( (LA12_3==WORD||LA12_3==130) ) {
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
				if ( (LA12_3==WORD||LA12_3==130) ) {
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
					// JPA2.g:103:17: join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_join_in_joined_clause731);
					join23=join();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join23.getTree());

					}
					break;
				case 2 :
					// JPA2.g:103:24: fetch_join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_fetch_join_in_joined_clause735);
					fetch_join24=fetch_join();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, fetch_join24.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:104:1: range_variable_declaration : entity_name ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) ;
	public final JPA2Parser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
		JPA2Parser.range_variable_declaration_return retval = new JPA2Parser.range_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal26=null;
		ParserRuleReturnScope entity_name25 =null;
		ParserRuleReturnScope identification_variable27 =null;

		Object string_literal26_tree=null;
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleSubtreeStream stream_entity_name=new RewriteRuleSubtreeStream(adaptor,"rule entity_name");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:105:6: ( entity_name ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) )
			// JPA2.g:105:8: entity_name ( 'AS' )? identification_variable
			{
			pushFollow(FOLLOW_entity_name_in_range_variable_declaration747);
			entity_name25=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_entity_name.add(entity_name25.getTree());
			// JPA2.g:105:20: ( 'AS' )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==78) ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// JPA2.g:105:21: 'AS'
					{
					string_literal26=(Token)match(input,78,FOLLOW_78_in_range_variable_declaration750); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_78.add(string_literal26);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_range_variable_declaration754);
			identification_variable27=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable27.getTree());
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
			// 106:6: -> ^( T_ID_VAR[$identification_variable.text] entity_name )
			{
				// JPA2.g:106:9: ^( T_ID_VAR[$identification_variable.text] entity_name )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new IdentificationVariableNode(T_ID_VAR, (identification_variable27!=null?input.toString(identification_variable27.start,identification_variable27.stop):null)), root_1);
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
	// JPA2.g:107:1: join : join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression ) ;
	public final JPA2Parser.join_return join() throws RecognitionException {
		JPA2Parser.join_return retval = new JPA2Parser.join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal30=null;
		Token string_literal32=null;
		ParserRuleReturnScope join_spec28 =null;
		ParserRuleReturnScope join_association_path_expression29 =null;
		ParserRuleReturnScope identification_variable31 =null;
		ParserRuleReturnScope conditional_expression33 =null;

		Object string_literal30_tree=null;
		Object string_literal32_tree=null;
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleTokenStream stream_118=new RewriteRuleTokenStream(adaptor,"token 118");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");
		RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");

		try {
			// JPA2.g:108:6: ( join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression ) )
			// JPA2.g:108:8: join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )?
			{
			pushFollow(FOLLOW_join_spec_in_join783);
			join_spec28=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_spec.add(join_spec28.getTree());
			pushFollow(FOLLOW_join_association_path_expression_in_join785);
			join_association_path_expression29=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression29.getTree());
			// JPA2.g:108:51: ( 'AS' )?
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==78) ) {
				alt14=1;
			}
			switch (alt14) {
				case 1 :
					// JPA2.g:108:52: 'AS'
					{
					string_literal30=(Token)match(input,78,FOLLOW_78_in_join788); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_78.add(string_literal30);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_join792);
			identification_variable31=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable31.getTree());
			// JPA2.g:108:83: ( 'ON' conditional_expression )?
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==118) ) {
				alt15=1;
			}
			switch (alt15) {
				case 1 :
					// JPA2.g:108:84: 'ON' conditional_expression
					{
					string_literal32=(Token)match(input,118,FOLLOW_118_in_join795); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_118.add(string_literal32);

					pushFollow(FOLLOW_conditional_expression_in_join797);
					conditional_expression33=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression33.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: join_association_path_expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 109:6: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression )
			{
				// JPA2.g:109:9: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new JoinVariableNode(T_JOIN_VAR, (join_spec28!=null?input.toString(join_spec28.start,join_spec28.stop):null), (identification_variable31!=null?input.toString(identification_variable31.start,identification_variable31.stop):null), (conditional_expression33!=null?input.toString(conditional_expression33.start,conditional_expression33.stop):null)), root_1);
				adaptor.addChild(root_1, stream_join_association_path_expression.nextTree());
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
	// JPA2.g:110:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
	public final JPA2Parser.fetch_join_return fetch_join() throws RecognitionException {
		JPA2Parser.fetch_join_return retval = new JPA2Parser.fetch_join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal35=null;
		ParserRuleReturnScope join_spec34 =null;
		ParserRuleReturnScope join_association_path_expression36 =null;

		Object string_literal35_tree=null;

		try {
			// JPA2.g:111:6: ( join_spec 'FETCH' join_association_path_expression )
			// JPA2.g:111:8: join_spec 'FETCH' join_association_path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_join_spec_in_fetch_join828);
			join_spec34=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec34.getTree());

			string_literal35=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join830); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal35_tree = (Object)adaptor.create(string_literal35);
			adaptor.addChild(root_0, string_literal35_tree);
			}

			pushFollow(FOLLOW_join_association_path_expression_in_fetch_join832);
			join_association_path_expression36=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression36.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:112:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
	public final JPA2Parser.join_spec_return join_spec() throws RecognitionException {
		JPA2Parser.join_spec_return retval = new JPA2Parser.join_spec_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal37=null;
		Token string_literal38=null;
		Token string_literal39=null;
		Token string_literal40=null;

		Object string_literal37_tree=null;
		Object string_literal38_tree=null;
		Object string_literal39_tree=null;
		Object string_literal40_tree=null;

		try {
			// JPA2.g:113:6: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
			// JPA2.g:113:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:113:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
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
					// JPA2.g:113:9: ( 'LEFT' ) ( 'OUTER' )?
					{
					// JPA2.g:113:9: ( 'LEFT' )
					// JPA2.g:113:10: 'LEFT'
					{
					string_literal37=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec846); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal37_tree = (Object)adaptor.create(string_literal37);
					adaptor.addChild(root_0, string_literal37_tree);
					}

					}

					// JPA2.g:113:18: ( 'OUTER' )?
					int alt16=2;
					int LA16_0 = input.LA(1);
					if ( (LA16_0==OUTER) ) {
						alt16=1;
					}
					switch (alt16) {
						case 1 :
							// JPA2.g:113:19: 'OUTER'
							{
							string_literal38=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec850); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal38_tree = (Object)adaptor.create(string_literal38);
							adaptor.addChild(root_0, string_literal38_tree);
							}

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:113:31: 'INNER'
					{
					string_literal39=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec856); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal39_tree = (Object)adaptor.create(string_literal39);
					adaptor.addChild(root_0, string_literal39_tree);
					}

					}
					break;

			}

			string_literal40=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec861); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal40_tree = (Object)adaptor.create(string_literal40);
			adaptor.addChild(root_0, string_literal40_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:116:1: join_association_path_expression : ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name );
	public final JPA2Parser.join_association_path_expression_return join_association_path_expression() throws RecognitionException {
		JPA2Parser.join_association_path_expression_return retval = new JPA2Parser.join_association_path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal42=null;
		Token char_literal44=null;
		Token string_literal46=null;
		Token char_literal48=null;
		Token char_literal50=null;
		Token string_literal52=null;
		Token char_literal54=null;
		ParserRuleReturnScope identification_variable41 =null;
		ParserRuleReturnScope field43 =null;
		ParserRuleReturnScope field45 =null;
		ParserRuleReturnScope identification_variable47 =null;
		ParserRuleReturnScope field49 =null;
		ParserRuleReturnScope field51 =null;
		ParserRuleReturnScope subtype53 =null;
		ParserRuleReturnScope entity_name55 =null;

		Object char_literal42_tree=null;
		Object char_literal44_tree=null;
		Object string_literal46_tree=null;
		Object char_literal48_tree=null;
		Object char_literal50_tree=null;
		Object string_literal52_tree=null;
		Object char_literal54_tree=null;
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_130=new RewriteRuleTokenStream(adaptor,"token 130");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_subtype=new RewriteRuleSubtreeStream(adaptor,"rule subtype");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:117:6: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name )
			int alt22=3;
			int LA22_0 = input.LA(1);
			if ( (LA22_0==WORD) ) {
				int LA22_1 = input.LA(2);
				if ( (LA22_1==60) ) {
					alt22=1;
				}
				else if ( (LA22_1==EOF||(LA22_1 >= GROUP && LA22_1 <= HAVING)||LA22_1==INNER||(LA22_1 >= JOIN && LA22_1 <= LEFT)||LA22_1==ORDER||LA22_1==RPAREN||LA22_1==WORD||LA22_1==58||LA22_1==78||LA22_1==102||LA22_1==123||LA22_1==138) ) {
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
			else if ( (LA22_0==130) ) {
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
					// JPA2.g:117:8: identification_variable '.' ( field '.' )* ( field )?
					{
					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression875);
					identification_variable41=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable41.getTree());
					char_literal42=(Token)match(input,60,FOLLOW_60_in_join_association_path_expression877); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal42);

					// JPA2.g:117:36: ( field '.' )*
					loop18:
					while (true) {
						int alt18=2;
						switch ( input.LA(1) ) {
						case WORD:
							{
							int LA18_1 = input.LA(2);
							if ( (LA18_1==60) ) {
								alt18=1;
							}

							}
							break;
						case 122:
							{
							int LA18_2 = input.LA(2);
							if ( (LA18_2==60) ) {
								alt18=1;
							}

							}
							break;
						case 98:
							{
							int LA18_3 = input.LA(2);
							if ( (LA18_3==60) ) {
								alt18=1;
							}

							}
							break;
						case GROUP:
							{
							int LA18_4 = input.LA(2);
							if ( (LA18_4==60) ) {
								alt18=1;
							}

							}
							break;
						case ORDER:
							{
							int LA18_5 = input.LA(2);
							if ( (LA18_5==60) ) {
								alt18=1;
							}

							}
							break;
						case MAX:
							{
							int LA18_6 = input.LA(2);
							if ( (LA18_6==60) ) {
								alt18=1;
							}

							}
							break;
						case MIN:
							{
							int LA18_7 = input.LA(2);
							if ( (LA18_7==60) ) {
								alt18=1;
							}

							}
							break;
						case SUM:
							{
							int LA18_8 = input.LA(2);
							if ( (LA18_8==60) ) {
								alt18=1;
							}

							}
							break;
						case AVG:
							{
							int LA18_9 = input.LA(2);
							if ( (LA18_9==60) ) {
								alt18=1;
							}

							}
							break;
						case COUNT:
							{
							int LA18_10 = input.LA(2);
							if ( (LA18_10==60) ) {
								alt18=1;
							}

							}
							break;
						case 88:
						case 94:
						case 100:
						case 109:
						case 111:
						case 119:
						case 121:
						case 136:
						case 139:
							{
							int LA18_11 = input.LA(2);
							if ( (LA18_11==60) ) {
								alt18=1;
							}

							}
							break;
						}
						switch (alt18) {
						case 1 :
							// JPA2.g:117:37: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression880);
							field43=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field43.getTree());
							char_literal44=(Token)match(input,60,FOLLOW_60_in_join_association_path_expression881); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_60.add(char_literal44);

							}
							break;

						default :
							break loop18;
						}
					}

					// JPA2.g:117:48: ( field )?
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
						case 88:
						case 94:
						case 98:
						case 100:
						case 109:
						case 111:
						case 119:
						case 121:
						case 122:
						case 136:
						case 139:
							{
							alt19=1;
							}
							break;
						case GROUP:
							{
							int LA19_3 = input.LA(2);
							if ( (LA19_3==EOF||(LA19_3 >= GROUP && LA19_3 <= HAVING)||LA19_3==INNER||(LA19_3 >= JOIN && LA19_3 <= LEFT)||LA19_3==ORDER||LA19_3==RPAREN||LA19_3==WORD||LA19_3==58||LA19_3==78||LA19_3==102||LA19_3==123||LA19_3==138) ) {
								alt19=1;
							}
							}
							break;
						case ORDER:
							{
							int LA19_4 = input.LA(2);
							if ( (LA19_4==EOF||(LA19_4 >= GROUP && LA19_4 <= HAVING)||LA19_4==INNER||(LA19_4 >= JOIN && LA19_4 <= LEFT)||LA19_4==ORDER||LA19_4==RPAREN||LA19_4==WORD||LA19_4==58||LA19_4==78||LA19_4==102||LA19_4==123||LA19_4==138) ) {
								alt19=1;
							}
							}
							break;
					}
					switch (alt19) {
						case 1 :
							// JPA2.g:117:48: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression885);
							field45=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field45.getTree());
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
					// 118:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:118:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable41!=null?input.toString(identification_variable41.start,identification_variable41.stop):null)), root_1);
						// JPA2.g:118:73: ( field )*
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
					// JPA2.g:119:9: 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')'
					{
					string_literal46=(Token)match(input,130,FOLLOW_130_in_join_association_path_expression920); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_130.add(string_literal46);

					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression922);
					identification_variable47=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable47.getTree());
					char_literal48=(Token)match(input,60,FOLLOW_60_in_join_association_path_expression924); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal48);

					// JPA2.g:119:46: ( field '.' )*
					loop20:
					while (true) {
						int alt20=2;
						switch ( input.LA(1) ) {
						case WORD:
							{
							int LA20_1 = input.LA(2);
							if ( (LA20_1==60) ) {
								alt20=1;
							}

							}
							break;
						case 122:
							{
							int LA20_2 = input.LA(2);
							if ( (LA20_2==60) ) {
								alt20=1;
							}

							}
							break;
						case 98:
							{
							int LA20_3 = input.LA(2);
							if ( (LA20_3==60) ) {
								alt20=1;
							}

							}
							break;
						case GROUP:
							{
							int LA20_4 = input.LA(2);
							if ( (LA20_4==60) ) {
								alt20=1;
							}

							}
							break;
						case ORDER:
							{
							int LA20_5 = input.LA(2);
							if ( (LA20_5==60) ) {
								alt20=1;
							}

							}
							break;
						case MAX:
							{
							int LA20_6 = input.LA(2);
							if ( (LA20_6==60) ) {
								alt20=1;
							}

							}
							break;
						case MIN:
							{
							int LA20_7 = input.LA(2);
							if ( (LA20_7==60) ) {
								alt20=1;
							}

							}
							break;
						case SUM:
							{
							int LA20_8 = input.LA(2);
							if ( (LA20_8==60) ) {
								alt20=1;
							}

							}
							break;
						case AVG:
							{
							int LA20_9 = input.LA(2);
							if ( (LA20_9==60) ) {
								alt20=1;
							}

							}
							break;
						case COUNT:
							{
							int LA20_10 = input.LA(2);
							if ( (LA20_10==60) ) {
								alt20=1;
							}

							}
							break;
						case 88:
						case 94:
						case 100:
						case 109:
						case 111:
						case 119:
						case 121:
						case 136:
						case 139:
							{
							int LA20_11 = input.LA(2);
							if ( (LA20_11==60) ) {
								alt20=1;
							}

							}
							break;
						}
						switch (alt20) {
						case 1 :
							// JPA2.g:119:47: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression927);
							field49=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field49.getTree());
							char_literal50=(Token)match(input,60,FOLLOW_60_in_join_association_path_expression928); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_60.add(char_literal50);

							}
							break;

						default :
							break loop20;
						}
					}

					// JPA2.g:119:58: ( field )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( (LA21_0==AVG||LA21_0==COUNT||LA21_0==GROUP||(LA21_0 >= MAX && LA21_0 <= MIN)||LA21_0==ORDER||LA21_0==SUM||LA21_0==WORD||LA21_0==88||LA21_0==94||LA21_0==98||LA21_0==100||LA21_0==109||LA21_0==111||LA21_0==119||(LA21_0 >= 121 && LA21_0 <= 122)||LA21_0==136||LA21_0==139) ) {
						alt21=1;
					}
					switch (alt21) {
						case 1 :
							// JPA2.g:119:58: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression932);
							field51=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field51.getTree());
							}
							break;

					}

					string_literal52=(Token)match(input,78,FOLLOW_78_in_join_association_path_expression935); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_78.add(string_literal52);

					pushFollow(FOLLOW_subtype_in_join_association_path_expression937);
					subtype53=subtype();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subtype.add(subtype53.getTree());
					char_literal54=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_join_association_path_expression939); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal54);

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
					// 120:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:120:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable47!=null?input.toString(identification_variable47.start,identification_variable47.stop):null)), root_1);
						// JPA2.g:120:73: ( field )*
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
					// JPA2.g:121:8: entity_name
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_name_in_join_association_path_expression972);
					entity_name55=entity_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_name55.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:124:1: collection_member_declaration : 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
	public final JPA2Parser.collection_member_declaration_return collection_member_declaration() throws RecognitionException {
		JPA2Parser.collection_member_declaration_return retval = new JPA2Parser.collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal56=null;
		Token char_literal57=null;
		Token char_literal59=null;
		Token string_literal60=null;
		ParserRuleReturnScope path_expression58 =null;
		ParserRuleReturnScope identification_variable61 =null;

		Object string_literal56_tree=null;
		Object char_literal57_tree=null;
		Object char_literal59_tree=null;
		Object string_literal60_tree=null;
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleTokenStream stream_IN=new RewriteRuleTokenStream(adaptor,"token IN");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");

		try {
			// JPA2.g:125:5: ( 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
			// JPA2.g:125:7: 'IN' '(' path_expression ')' ( 'AS' )? identification_variable
			{
			string_literal56=(Token)match(input,IN,FOLLOW_IN_in_collection_member_declaration985); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_IN.add(string_literal56);

			char_literal57=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration986); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal57);

			pushFollow(FOLLOW_path_expression_in_collection_member_declaration988);
			path_expression58=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_path_expression.add(path_expression58.getTree());
			char_literal59=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration990); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal59);

			// JPA2.g:125:35: ( 'AS' )?
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0==78) ) {
				alt23=1;
			}
			switch (alt23) {
				case 1 :
					// JPA2.g:125:36: 'AS'
					{
					string_literal60=(Token)match(input,78,FOLLOW_78_in_collection_member_declaration993); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_78.add(string_literal60);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_collection_member_declaration997);
			identification_variable61=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable61.getTree());
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
			// 126:5: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
			{
				// JPA2.g:126:8: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new CollectionMemberNode(T_COLLECTION_MEMBER, (identification_variable61!=null?input.toString(identification_variable61.start,identification_variable61.stop):null)), root_1);
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
	// JPA2.g:128:1: qualified_identification_variable : ( map_field_identification_variable | 'ENTRY(' identification_variable ')' );
	public final JPA2Parser.qualified_identification_variable_return qualified_identification_variable() throws RecognitionException {
		JPA2Parser.qualified_identification_variable_return retval = new JPA2Parser.qualified_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal63=null;
		Token char_literal65=null;
		ParserRuleReturnScope map_field_identification_variable62 =null;
		ParserRuleReturnScope identification_variable64 =null;

		Object string_literal63_tree=null;
		Object char_literal65_tree=null;

		try {
			// JPA2.g:129:5: ( map_field_identification_variable | 'ENTRY(' identification_variable ')' )
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==103||LA24_0==135) ) {
				alt24=1;
			}
			else if ( (LA24_0==93) ) {
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
					// JPA2.g:129:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_qualified_identification_variable1026);
					map_field_identification_variable62=map_field_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map_field_identification_variable62.getTree());

					}
					break;
				case 2 :
					// JPA2.g:130:7: 'ENTRY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal63=(Token)match(input,93,FOLLOW_93_in_qualified_identification_variable1034); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal63_tree = (Object)adaptor.create(string_literal63);
					adaptor.addChild(root_0, string_literal63_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_qualified_identification_variable1035);
					identification_variable64=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable64.getTree());

					char_literal65=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_qualified_identification_variable1036); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal65_tree = (Object)adaptor.create(char_literal65);
					adaptor.addChild(root_0, char_literal65_tree);
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
	// JPA2.g:131:1: map_field_identification_variable : ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' );
	public final JPA2Parser.map_field_identification_variable_return map_field_identification_variable() throws RecognitionException {
		JPA2Parser.map_field_identification_variable_return retval = new JPA2Parser.map_field_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal66=null;
		Token char_literal68=null;
		Token string_literal69=null;
		Token char_literal71=null;
		ParserRuleReturnScope identification_variable67 =null;
		ParserRuleReturnScope identification_variable70 =null;

		Object string_literal66_tree=null;
		Object char_literal68_tree=null;
		Object string_literal69_tree=null;
		Object char_literal71_tree=null;

		try {
			// JPA2.g:131:35: ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' )
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==103) ) {
				alt25=1;
			}
			else if ( (LA25_0==135) ) {
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
					// JPA2.g:131:37: 'KEY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal66=(Token)match(input,103,FOLLOW_103_in_map_field_identification_variable1043); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal66_tree = (Object)adaptor.create(string_literal66);
					adaptor.addChild(root_0, string_literal66_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1044);
					identification_variable67=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable67.getTree());

					char_literal68=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1045); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal68_tree = (Object)adaptor.create(char_literal68);
					adaptor.addChild(root_0, char_literal68_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:131:72: 'VALUE(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal69=(Token)match(input,135,FOLLOW_135_in_map_field_identification_variable1049); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal69_tree = (Object)adaptor.create(string_literal69);
					adaptor.addChild(root_0, string_literal69_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1050);
					identification_variable70=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable70.getTree());

					char_literal71=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1051); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal71_tree = (Object)adaptor.create(char_literal71);
					adaptor.addChild(root_0, char_literal71_tree);
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
	// JPA2.g:134:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
	public final JPA2Parser.path_expression_return path_expression() throws RecognitionException {
		JPA2Parser.path_expression_return retval = new JPA2Parser.path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal73=null;
		Token char_literal75=null;
		ParserRuleReturnScope identification_variable72 =null;
		ParserRuleReturnScope field74 =null;
		ParserRuleReturnScope field76 =null;

		Object char_literal73_tree=null;
		Object char_literal75_tree=null;
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:135:5: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
			// JPA2.g:135:8: identification_variable '.' ( field '.' )* ( field )?
			{
			pushFollow(FOLLOW_identification_variable_in_path_expression1065);
			identification_variable72=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable72.getTree());
			char_literal73=(Token)match(input,60,FOLLOW_60_in_path_expression1067); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_60.add(char_literal73);

			// JPA2.g:135:36: ( field '.' )*
			loop26:
			while (true) {
				int alt26=2;
				switch ( input.LA(1) ) {
				case WORD:
					{
					int LA26_1 = input.LA(2);
					if ( (LA26_1==60) ) {
						alt26=1;
					}

					}
					break;
				case 122:
					{
					int LA26_2 = input.LA(2);
					if ( (LA26_2==60) ) {
						alt26=1;
					}

					}
					break;
				case 98:
					{
					int LA26_3 = input.LA(2);
					if ( (LA26_3==60) ) {
						alt26=1;
					}

					}
					break;
				case GROUP:
					{
					int LA26_4 = input.LA(2);
					if ( (LA26_4==60) ) {
						alt26=1;
					}

					}
					break;
				case ORDER:
					{
					int LA26_5 = input.LA(2);
					if ( (LA26_5==60) ) {
						alt26=1;
					}

					}
					break;
				case MAX:
					{
					int LA26_6 = input.LA(2);
					if ( (LA26_6==60) ) {
						alt26=1;
					}

					}
					break;
				case MIN:
					{
					int LA26_7 = input.LA(2);
					if ( (LA26_7==60) ) {
						alt26=1;
					}

					}
					break;
				case SUM:
					{
					int LA26_8 = input.LA(2);
					if ( (LA26_8==60) ) {
						alt26=1;
					}

					}
					break;
				case AVG:
					{
					int LA26_9 = input.LA(2);
					if ( (LA26_9==60) ) {
						alt26=1;
					}

					}
					break;
				case COUNT:
					{
					int LA26_10 = input.LA(2);
					if ( (LA26_10==60) ) {
						alt26=1;
					}

					}
					break;
				case 88:
				case 94:
				case 100:
				case 109:
				case 111:
				case 119:
				case 121:
				case 136:
				case 139:
					{
					int LA26_11 = input.LA(2);
					if ( (LA26_11==60) ) {
						alt26=1;
					}

					}
					break;
				}
				switch (alt26) {
				case 1 :
					// JPA2.g:135:37: field '.'
					{
					pushFollow(FOLLOW_field_in_path_expression1070);
					field74=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field74.getTree());
					char_literal75=(Token)match(input,60,FOLLOW_60_in_path_expression1071); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal75);

					}
					break;

				default :
					break loop26;
				}
			}

			// JPA2.g:135:48: ( field )?
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
				case 88:
				case 94:
				case 100:
				case 109:
				case 111:
				case 119:
				case 121:
				case 122:
				case 136:
				case 139:
					{
					alt27=1;
					}
					break;
				case 98:
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
						case 56:
						case 57:
						case 58:
						case 59:
						case 61:
						case 63:
						case 64:
						case 65:
						case 66:
						case 67:
						case 68:
						case 78:
						case 79:
						case 90:
						case 92:
						case 95:
						case 98:
						case 102:
						case 106:
						case 108:
						case 120:
						case 123:
						case 128:
						case 137:
						case 138:
							{
							alt27=1;
							}
							break;
						case WORD:
							{
							int LA27_7 = input.LA(3);
							if ( (LA27_7==EOF||LA27_7==LPAREN||LA27_7==RPAREN||LA27_7==58||LA27_7==98) ) {
								alt27=1;
							}
							}
							break;
						case IN:
							{
							int LA27_8 = input.LA(3);
							if ( (LA27_8==LPAREN||LA27_8==NAMED_PARAMETER||LA27_8==55||LA27_8==69) ) {
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
					if ( (LA27_4==EOF||(LA27_4 >= AND && LA27_4 <= ASC)||LA27_4==DESC||(LA27_4 >= GROUP && LA27_4 <= INNER)||(LA27_4 >= JOIN && LA27_4 <= LEFT)||(LA27_4 >= NOT && LA27_4 <= ORDER)||LA27_4==RPAREN||LA27_4==WORD||(LA27_4 >= 56 && LA27_4 <= 59)||LA27_4==61||(LA27_4 >= 63 && LA27_4 <= 68)||(LA27_4 >= 78 && LA27_4 <= 79)||LA27_4==90||LA27_4==92||LA27_4==95||LA27_4==98||LA27_4==102||LA27_4==106||LA27_4==108||LA27_4==120||LA27_4==123||LA27_4==128||(LA27_4 >= 137 && LA27_4 <= 138)) ) {
						alt27=1;
					}
					}
					break;
				case ORDER:
					{
					int LA27_5 = input.LA(2);
					if ( (LA27_5==EOF||(LA27_5 >= AND && LA27_5 <= ASC)||LA27_5==DESC||(LA27_5 >= GROUP && LA27_5 <= INNER)||(LA27_5 >= JOIN && LA27_5 <= LEFT)||(LA27_5 >= NOT && LA27_5 <= ORDER)||LA27_5==RPAREN||LA27_5==WORD||(LA27_5 >= 56 && LA27_5 <= 59)||LA27_5==61||(LA27_5 >= 63 && LA27_5 <= 68)||(LA27_5 >= 78 && LA27_5 <= 79)||LA27_5==90||LA27_5==92||LA27_5==95||LA27_5==98||LA27_5==102||LA27_5==106||LA27_5==108||LA27_5==120||LA27_5==123||LA27_5==128||(LA27_5 >= 137 && LA27_5 <= 138)) ) {
						alt27=1;
					}
					}
					break;
			}
			switch (alt27) {
				case 1 :
					// JPA2.g:135:48: field
					{
					pushFollow(FOLLOW_field_in_path_expression1075);
					field76=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field76.getTree());
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
			// 136:5: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
			{
				// JPA2.g:136:8: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable72!=null?input.toString(identification_variable72.start,identification_variable72.stop):null)), root_1);
				// JPA2.g:136:68: ( field )*
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
	// JPA2.g:141:1: general_identification_variable : ( identification_variable | map_field_identification_variable );
	public final JPA2Parser.general_identification_variable_return general_identification_variable() throws RecognitionException {
		JPA2Parser.general_identification_variable_return retval = new JPA2Parser.general_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable77 =null;
		ParserRuleReturnScope map_field_identification_variable78 =null;


		try {
			// JPA2.g:142:5: ( identification_variable | map_field_identification_variable )
			int alt28=2;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==WORD) ) {
				alt28=1;
			}
			else if ( (LA28_0==103||LA28_0==135) ) {
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
					// JPA2.g:142:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_general_identification_variable1114);
					identification_variable77=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable77.getTree());

					}
					break;
				case 2 :
					// JPA2.g:143:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_general_identification_variable1122);
					map_field_identification_variable78=map_field_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map_field_identification_variable78.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:146:1: update_clause : identification_variable_declaration 'SET' update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration ) ;
	public final JPA2Parser.update_clause_return update_clause() throws RecognitionException {
		JPA2Parser.update_clause_return retval = new JPA2Parser.update_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal80=null;
		Token char_literal82=null;
		ParserRuleReturnScope identification_variable_declaration79 =null;
		ParserRuleReturnScope update_item81 =null;
		ParserRuleReturnScope update_item83 =null;

		Object string_literal80_tree=null;
		Object char_literal82_tree=null;
		RewriteRuleTokenStream stream_123=new RewriteRuleTokenStream(adaptor,"token 123");
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleSubtreeStream stream_update_item=new RewriteRuleSubtreeStream(adaptor,"rule update_item");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:147:5: ( identification_variable_declaration 'SET' update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration ) )
			// JPA2.g:147:7: identification_variable_declaration 'SET' update_item ( ',' update_item )*
			{
			pushFollow(FOLLOW_identification_variable_declaration_in_update_clause1135);
			identification_variable_declaration79=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration79.getTree());
			string_literal80=(Token)match(input,123,FOLLOW_123_in_update_clause1137); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_123.add(string_literal80);

			pushFollow(FOLLOW_update_item_in_update_clause1139);
			update_item81=update_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_item.add(update_item81.getTree());
			// JPA2.g:147:61: ( ',' update_item )*
			loop29:
			while (true) {
				int alt29=2;
				int LA29_0 = input.LA(1);
				if ( (LA29_0==58) ) {
					alt29=1;
				}

				switch (alt29) {
				case 1 :
					// JPA2.g:147:62: ',' update_item
					{
					char_literal82=(Token)match(input,58,FOLLOW_58_in_update_clause1142); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal82);

					pushFollow(FOLLOW_update_item_in_update_clause1144);
					update_item83=update_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_update_item.add(update_item83.getTree());
					}
					break;

				default :
					break loop29;
				}
			}

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
			// 148:5: -> ^( T_SOURCES identification_variable_declaration )
			{
				// JPA2.g:148:8: ^( T_SOURCES identification_variable_declaration )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCES), root_1);
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
	// $ANTLR end "update_clause"


	public static class update_item_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "update_item"
	// JPA2.g:149:1: update_item : path_expression '=' new_value ;
	public final JPA2Parser.update_item_return update_item() throws RecognitionException {
		JPA2Parser.update_item_return retval = new JPA2Parser.update_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal85=null;
		ParserRuleReturnScope path_expression84 =null;
		ParserRuleReturnScope new_value86 =null;

		Object char_literal85_tree=null;

		try {
			// JPA2.g:150:5: ( path_expression '=' new_value )
			// JPA2.g:150:7: path_expression '=' new_value
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_update_item1172);
			path_expression84=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression84.getTree());

			char_literal85=(Token)match(input,66,FOLLOW_66_in_update_item1174); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal85_tree = (Object)adaptor.create(char_literal85);
			adaptor.addChild(root_0, char_literal85_tree);
			}

			pushFollow(FOLLOW_new_value_in_update_item1176);
			new_value86=new_value();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, new_value86.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:151:1: new_value : ( scalar_expression | simple_entity_expression | 'NULL' );
	public final JPA2Parser.new_value_return new_value() throws RecognitionException {
		JPA2Parser.new_value_return retval = new JPA2Parser.new_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal89=null;
		ParserRuleReturnScope scalar_expression87 =null;
		ParserRuleReturnScope simple_entity_expression88 =null;

		Object string_literal89_tree=null;

		try {
			// JPA2.g:152:5: ( scalar_expression | simple_entity_expression | 'NULL' )
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
			case 57:
			case 59:
			case 62:
			case 75:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 97:
			case 99:
			case 101:
			case 105:
			case 107:
			case 110:
			case 115:
			case 124:
			case 126:
			case 127:
			case 131:
			case 132:
			case 134:
			case 140:
			case 141:
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
			case 69:
				{
				int LA30_3 = input.LA(2);
				if ( (LA30_3==62) ) {
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
			case 55:
				{
				int LA30_5 = input.LA(2);
				if ( (LA30_5==WORD) ) {
					int LA30_10 = input.LA(3);
					if ( (LA30_10==142) ) {
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
			case 114:
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
					// JPA2.g:152:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_new_value1187);
					scalar_expression87=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression87.getTree());

					}
					break;
				case 2 :
					// JPA2.g:153:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_new_value1195);
					simple_entity_expression88=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression88.getTree());

					}
					break;
				case 3 :
					// JPA2.g:154:7: 'NULL'
					{
					root_0 = (Object)adaptor.nil();


					string_literal89=(Token)match(input,114,FOLLOW_114_in_new_value1203); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal89_tree = (Object)adaptor.create(string_literal89);
					adaptor.addChild(root_0, string_literal89_tree);
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
	// JPA2.g:156:1: delete_clause : identification_variable_declaration -> ^( T_SOURCES identification_variable_declaration ) ;
	public final JPA2Parser.delete_clause_return delete_clause() throws RecognitionException {
		JPA2Parser.delete_clause_return retval = new JPA2Parser.delete_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable_declaration90 =null;

		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:157:5: ( identification_variable_declaration -> ^( T_SOURCES identification_variable_declaration ) )
			// JPA2.g:157:7: identification_variable_declaration
			{
			pushFollow(FOLLOW_identification_variable_declaration_in_delete_clause1215);
			identification_variable_declaration90=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration90.getTree());
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
			// 158:5: -> ^( T_SOURCES identification_variable_declaration )
			{
				// JPA2.g:158:8: ^( T_SOURCES identification_variable_declaration )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCES), root_1);
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
	// JPA2.g:159:1: select_clause : ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) ;
	public final JPA2Parser.select_clause_return select_clause() throws RecognitionException {
		JPA2Parser.select_clause_return retval = new JPA2Parser.select_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal91=null;
		Token char_literal93=null;
		ParserRuleReturnScope select_item92 =null;
		ParserRuleReturnScope select_item94 =null;

		Object string_literal91_tree=null;
		Object char_literal93_tree=null;
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleSubtreeStream stream_select_item=new RewriteRuleSubtreeStream(adaptor,"rule select_item");

		try {
			// JPA2.g:160:5: ( ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) )
			// JPA2.g:160:7: ( 'DISTINCT' )? select_item ( ',' select_item )*
			{
			// JPA2.g:160:7: ( 'DISTINCT' )?
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0==DISTINCT) ) {
				alt31=1;
			}
			switch (alt31) {
				case 1 :
					// JPA2.g:160:8: 'DISTINCT'
					{
					string_literal91=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause1242); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal91);

					}
					break;

			}

			pushFollow(FOLLOW_select_item_in_select_clause1246);
			select_item92=select_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_item.add(select_item92.getTree());
			// JPA2.g:160:33: ( ',' select_item )*
			loop32:
			while (true) {
				int alt32=2;
				int LA32_0 = input.LA(1);
				if ( (LA32_0==58) ) {
					alt32=1;
				}

				switch (alt32) {
				case 1 :
					// JPA2.g:160:34: ',' select_item
					{
					char_literal93=(Token)match(input,58,FOLLOW_58_in_select_clause1249); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal93);

					pushFollow(FOLLOW_select_item_in_select_clause1251);
					select_item94=select_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_select_item.add(select_item94.getTree());
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
			// 161:5: -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
			{
				// JPA2.g:161:8: ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:161:48: ( 'DISTINCT' )?
				if ( stream_DISTINCT.hasNext() ) {
					adaptor.addChild(root_1, stream_DISTINCT.nextNode());
				}
				stream_DISTINCT.reset();

				// JPA2.g:161:62: ( ^( T_SELECTED_ITEM[] select_item ) )*
				while ( stream_select_item.hasNext() ) {
					// JPA2.g:161:62: ^( T_SELECTED_ITEM[] select_item )
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
	// JPA2.g:162:1: select_item : select_expression ( ( 'AS' )? result_variable )? ;
	public final JPA2Parser.select_item_return select_item() throws RecognitionException {
		JPA2Parser.select_item_return retval = new JPA2Parser.select_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal96=null;
		ParserRuleReturnScope select_expression95 =null;
		ParserRuleReturnScope result_variable97 =null;

		Object string_literal96_tree=null;

		try {
			// JPA2.g:163:5: ( select_expression ( ( 'AS' )? result_variable )? )
			// JPA2.g:163:7: select_expression ( ( 'AS' )? result_variable )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_select_expression_in_select_item1294);
			select_expression95=select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, select_expression95.getTree());

			// JPA2.g:163:25: ( ( 'AS' )? result_variable )?
			int alt34=2;
			int LA34_0 = input.LA(1);
			if ( (LA34_0==WORD||LA34_0==78) ) {
				alt34=1;
			}
			switch (alt34) {
				case 1 :
					// JPA2.g:163:26: ( 'AS' )? result_variable
					{
					// JPA2.g:163:26: ( 'AS' )?
					int alt33=2;
					int LA33_0 = input.LA(1);
					if ( (LA33_0==78) ) {
						alt33=1;
					}
					switch (alt33) {
						case 1 :
							// JPA2.g:163:27: 'AS'
							{
							string_literal96=(Token)match(input,78,FOLLOW_78_in_select_item1298); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal96_tree = (Object)adaptor.create(string_literal96);
							adaptor.addChild(root_0, string_literal96_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_result_variable_in_select_item1302);
					result_variable97=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable97.getTree());

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
	// JPA2.g:164:1: select_expression : ( path_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression );
	public final JPA2Parser.select_expression_return select_expression() throws RecognitionException {
		JPA2Parser.select_expression_return retval = new JPA2Parser.select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal102=null;
		Token char_literal103=null;
		Token char_literal105=null;
		ParserRuleReturnScope path_expression98 =null;
		ParserRuleReturnScope identification_variable99 =null;
		ParserRuleReturnScope scalar_expression100 =null;
		ParserRuleReturnScope aggregate_expression101 =null;
		ParserRuleReturnScope identification_variable104 =null;
		ParserRuleReturnScope constructor_expression106 =null;

		Object string_literal102_tree=null;
		Object char_literal103_tree=null;
		Object char_literal105_tree=null;
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:165:5: ( path_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression )
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
			case 55:
			case 57:
			case 59:
			case 62:
			case 69:
			case 75:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 97:
			case 101:
			case 105:
			case 107:
			case 110:
			case 115:
			case 124:
			case 126:
			case 127:
			case 131:
			case 132:
			case 134:
			case 140:
			case 141:
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
			case 99:
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
			case 116:
				{
				alt35=5;
				}
				break;
			case 112:
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
					// JPA2.g:165:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_select_expression1315);
					path_expression98=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression98.getTree());

					}
					break;
				case 2 :
					// JPA2.g:166:7: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_select_expression1323);
					identification_variable99=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable99.getTree());
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
					// 166:31: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
					{
						// JPA2.g:166:34: ^( T_SELECTED_ENTITY[$identification_variable.text] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_ENTITY, (identification_variable99!=null?input.toString(identification_variable99.start,identification_variable99.stop):null)), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// JPA2.g:167:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_select_expression1341);
					scalar_expression100=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression100.getTree());

					}
					break;
				case 4 :
					// JPA2.g:168:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_select_expression1349);
					aggregate_expression101=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression101.getTree());

					}
					break;
				case 5 :
					// JPA2.g:169:7: 'OBJECT' '(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal102=(Token)match(input,116,FOLLOW_116_in_select_expression1357); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal102_tree = (Object)adaptor.create(string_literal102);
					adaptor.addChild(root_0, string_literal102_tree);
					}

					char_literal103=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression1359); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal103_tree = (Object)adaptor.create(char_literal103);
					adaptor.addChild(root_0, char_literal103_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_select_expression1360);
					identification_variable104=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable104.getTree());

					char_literal105=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression1361); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal105_tree = (Object)adaptor.create(char_literal105);
					adaptor.addChild(root_0, char_literal105_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:170:7: constructor_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_constructor_expression_in_select_expression1369);
					constructor_expression106=constructor_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_expression106.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:171:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
	public final JPA2Parser.constructor_expression_return constructor_expression() throws RecognitionException {
		JPA2Parser.constructor_expression_return retval = new JPA2Parser.constructor_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal107=null;
		Token char_literal109=null;
		Token char_literal111=null;
		Token char_literal113=null;
		ParserRuleReturnScope constructor_name108 =null;
		ParserRuleReturnScope constructor_item110 =null;
		ParserRuleReturnScope constructor_item112 =null;

		Object string_literal107_tree=null;
		Object char_literal109_tree=null;
		Object char_literal111_tree=null;
		Object char_literal113_tree=null;

		try {
			// JPA2.g:172:5: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
			// JPA2.g:172:7: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal107=(Token)match(input,112,FOLLOW_112_in_constructor_expression1380); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal107_tree = (Object)adaptor.create(string_literal107);
			adaptor.addChild(root_0, string_literal107_tree);
			}

			pushFollow(FOLLOW_constructor_name_in_constructor_expression1382);
			constructor_name108=constructor_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name108.getTree());

			char_literal109=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression1384); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal109_tree = (Object)adaptor.create(char_literal109);
			adaptor.addChild(root_0, char_literal109_tree);
			}

			pushFollow(FOLLOW_constructor_item_in_constructor_expression1386);
			constructor_item110=constructor_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item110.getTree());

			// JPA2.g:172:51: ( ',' constructor_item )*
			loop36:
			while (true) {
				int alt36=2;
				int LA36_0 = input.LA(1);
				if ( (LA36_0==58) ) {
					alt36=1;
				}

				switch (alt36) {
				case 1 :
					// JPA2.g:172:52: ',' constructor_item
					{
					char_literal111=(Token)match(input,58,FOLLOW_58_in_constructor_expression1389); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal111_tree = (Object)adaptor.create(char_literal111);
					adaptor.addChild(root_0, char_literal111_tree);
					}

					pushFollow(FOLLOW_constructor_item_in_constructor_expression1391);
					constructor_item112=constructor_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item112.getTree());

					}
					break;

				default :
					break loop36;
				}
			}

			char_literal113=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression1395); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal113_tree = (Object)adaptor.create(char_literal113);
			adaptor.addChild(root_0, char_literal113_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:173:1: constructor_item : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.constructor_item_return constructor_item() throws RecognitionException {
		JPA2Parser.constructor_item_return retval = new JPA2Parser.constructor_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression114 =null;
		ParserRuleReturnScope scalar_expression115 =null;
		ParserRuleReturnScope aggregate_expression116 =null;
		ParserRuleReturnScope identification_variable117 =null;


		try {
			// JPA2.g:174:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
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
			case 55:
			case 57:
			case 59:
			case 62:
			case 69:
			case 75:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 97:
			case 101:
			case 105:
			case 107:
			case 110:
			case 115:
			case 124:
			case 126:
			case 127:
			case 131:
			case 132:
			case 134:
			case 140:
			case 141:
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
			case 99:
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
					// JPA2.g:174:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_constructor_item1406);
					path_expression114=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression114.getTree());

					}
					break;
				case 2 :
					// JPA2.g:175:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_constructor_item1414);
					scalar_expression115=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression115.getTree());

					}
					break;
				case 3 :
					// JPA2.g:176:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_constructor_item1422);
					aggregate_expression116=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression116.getTree());

					}
					break;
				case 4 :
					// JPA2.g:177:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_constructor_item1430);
					identification_variable117=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable117.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:178:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );
	public final JPA2Parser.aggregate_expression_return aggregate_expression() throws RecognitionException {
		JPA2Parser.aggregate_expression_return retval = new JPA2Parser.aggregate_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal119=null;
		Token DISTINCT120=null;
		Token char_literal122=null;
		Token string_literal123=null;
		Token char_literal124=null;
		Token DISTINCT125=null;
		Token char_literal127=null;
		ParserRuleReturnScope aggregate_expression_function_name118 =null;
		ParserRuleReturnScope path_expression121 =null;
		ParserRuleReturnScope count_argument126 =null;
		ParserRuleReturnScope function_invocation128 =null;

		Object char_literal119_tree=null;
		Object DISTINCT120_tree=null;
		Object char_literal122_tree=null;
		Object string_literal123_tree=null;
		Object char_literal124_tree=null;
		Object DISTINCT125_tree=null;
		Object char_literal127_tree=null;
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_COUNT=new RewriteRuleTokenStream(adaptor,"token COUNT");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_count_argument=new RewriteRuleSubtreeStream(adaptor,"rule count_argument");
		RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
		RewriteRuleSubtreeStream stream_aggregate_expression_function_name=new RewriteRuleSubtreeStream(adaptor,"rule aggregate_expression_function_name");

		try {
			// JPA2.g:179:5: ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation )
			int alt40=3;
			alt40 = dfa40.predict(input);
			switch (alt40) {
				case 1 :
					// JPA2.g:179:7: aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')'
					{
					pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1441);
					aggregate_expression_function_name118=aggregate_expression_function_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name118.getTree());
					char_literal119=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1443); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal119);

					// JPA2.g:179:45: ( DISTINCT )?
					int alt38=2;
					int LA38_0 = input.LA(1);
					if ( (LA38_0==DISTINCT) ) {
						alt38=1;
					}
					switch (alt38) {
						case 1 :
							// JPA2.g:179:46: DISTINCT
							{
							DISTINCT120=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1445); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT120);

							}
							break;

					}

					pushFollow(FOLLOW_path_expression_in_aggregate_expression1449);
					path_expression121=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_path_expression.add(path_expression121.getTree());
					char_literal122=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1450); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal122);

					// AST REWRITE
					// elements: path_expression, DISTINCT, aggregate_expression_function_name, RPAREN, LPAREN
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 180:5: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
					{
						// JPA2.g:180:8: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:180:93: ( 'DISTINCT' )?
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
					// JPA2.g:181:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
					{
					string_literal123=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1484); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COUNT.add(string_literal123);

					char_literal124=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1486); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal124);

					// JPA2.g:181:18: ( DISTINCT )?
					int alt39=2;
					int LA39_0 = input.LA(1);
					if ( (LA39_0==DISTINCT) ) {
						alt39=1;
					}
					switch (alt39) {
						case 1 :
							// JPA2.g:181:19: DISTINCT
							{
							DISTINCT125=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1488); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT125);

							}
							break;

					}

					pushFollow(FOLLOW_count_argument_in_aggregate_expression1492);
					count_argument126=count_argument();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_count_argument.add(count_argument126.getTree());
					char_literal127=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1494); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal127);

					// AST REWRITE
					// elements: RPAREN, COUNT, count_argument, DISTINCT, LPAREN
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 182:5: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
					{
						// JPA2.g:182:8: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_COUNT.nextNode());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:182:66: ( 'DISTINCT' )?
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
					// JPA2.g:183:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_aggregate_expression1529);
					function_invocation128=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation128.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:184:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
	public final JPA2Parser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
		JPA2Parser.aggregate_expression_function_name_return retval = new JPA2Parser.aggregate_expression_function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set129=null;

		Object set129_tree=null;

		try {
			// JPA2.g:185:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set129=input.LT(1);
			if ( input.LA(1)==AVG||input.LA(1)==COUNT||(input.LA(1) >= MAX && input.LA(1) <= MIN)||input.LA(1)==SUM ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set129));
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
	// JPA2.g:186:1: count_argument : ( identification_variable | path_expression );
	public final JPA2Parser.count_argument_return count_argument() throws RecognitionException {
		JPA2Parser.count_argument_return retval = new JPA2Parser.count_argument_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable130 =null;
		ParserRuleReturnScope path_expression131 =null;


		try {
			// JPA2.g:187:5: ( identification_variable | path_expression )
			int alt41=2;
			int LA41_0 = input.LA(1);
			if ( (LA41_0==WORD) ) {
				int LA41_1 = input.LA(2);
				if ( (LA41_1==RPAREN) ) {
					alt41=1;
				}
				else if ( (LA41_1==60) ) {
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
					// JPA2.g:187:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_count_argument1566);
					identification_variable130=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable130.getTree());

					}
					break;
				case 2 :
					// JPA2.g:187:33: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_count_argument1570);
					path_expression131=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression131.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:188:1: where_clause : wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) ;
	public final JPA2Parser.where_clause_return where_clause() throws RecognitionException {
		JPA2Parser.where_clause_return retval = new JPA2Parser.where_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token wh=null;
		ParserRuleReturnScope conditional_expression132 =null;

		Object wh_tree=null;
		RewriteRuleTokenStream stream_138=new RewriteRuleTokenStream(adaptor,"token 138");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");

		try {
			// JPA2.g:189:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) )
			// JPA2.g:189:7: wh= 'WHERE' conditional_expression
			{
			wh=(Token)match(input,138,FOLLOW_138_in_where_clause1583); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_138.add(wh);

			pushFollow(FOLLOW_conditional_expression_in_where_clause1585);
			conditional_expression132=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression132.getTree());
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
			// 189:40: -> ^( T_CONDITION[$wh] conditional_expression )
			{
				// JPA2.g:189:43: ^( T_CONDITION[$wh] conditional_expression )
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
	// JPA2.g:190:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
	public final JPA2Parser.groupby_clause_return groupby_clause() throws RecognitionException {
		JPA2Parser.groupby_clause_return retval = new JPA2Parser.groupby_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal133=null;
		Token string_literal134=null;
		Token char_literal136=null;
		ParserRuleReturnScope groupby_item135 =null;
		ParserRuleReturnScope groupby_item137 =null;

		Object string_literal133_tree=null;
		Object string_literal134_tree=null;
		Object char_literal136_tree=null;
		RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");

		try {
			// JPA2.g:191:5: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
			// JPA2.g:191:7: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
			{
			string_literal133=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1607); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_GROUP.add(string_literal133);

			string_literal134=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1609); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal134);

			pushFollow(FOLLOW_groupby_item_in_groupby_clause1611);
			groupby_item135=groupby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item135.getTree());
			// JPA2.g:191:33: ( ',' groupby_item )*
			loop42:
			while (true) {
				int alt42=2;
				int LA42_0 = input.LA(1);
				if ( (LA42_0==58) ) {
					alt42=1;
				}

				switch (alt42) {
				case 1 :
					// JPA2.g:191:34: ',' groupby_item
					{
					char_literal136=(Token)match(input,58,FOLLOW_58_in_groupby_clause1614); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal136);

					pushFollow(FOLLOW_groupby_item_in_groupby_clause1616);
					groupby_item137=groupby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item137.getTree());
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
			// 192:5: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
			{
				// JPA2.g:192:8: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new GroupByNode(T_GROUP_BY), root_1);
				adaptor.addChild(root_1, stream_GROUP.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:192:49: ( groupby_item )*
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
	// JPA2.g:193:1: groupby_item : ( path_expression | identification_variable );
	public final JPA2Parser.groupby_item_return groupby_item() throws RecognitionException {
		JPA2Parser.groupby_item_return retval = new JPA2Parser.groupby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression138 =null;
		ParserRuleReturnScope identification_variable139 =null;


		try {
			// JPA2.g:194:5: ( path_expression | identification_variable )
			int alt43=2;
			int LA43_0 = input.LA(1);
			if ( (LA43_0==WORD) ) {
				int LA43_1 = input.LA(2);
				if ( (LA43_1==60) ) {
					alt43=1;
				}
				else if ( (LA43_1==EOF||LA43_1==HAVING||LA43_1==ORDER||LA43_1==RPAREN||LA43_1==58) ) {
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
					// JPA2.g:194:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_groupby_item1650);
					path_expression138=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression138.getTree());

					}
					break;
				case 2 :
					// JPA2.g:194:25: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_groupby_item1654);
					identification_variable139=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable139.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:195:1: having_clause : 'HAVING' conditional_expression ;
	public final JPA2Parser.having_clause_return having_clause() throws RecognitionException {
		JPA2Parser.having_clause_return retval = new JPA2Parser.having_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal140=null;
		ParserRuleReturnScope conditional_expression141 =null;

		Object string_literal140_tree=null;

		try {
			// JPA2.g:196:5: ( 'HAVING' conditional_expression )
			// JPA2.g:196:7: 'HAVING' conditional_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal140=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1665); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal140_tree = (Object)adaptor.create(string_literal140);
			adaptor.addChild(root_0, string_literal140_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_having_clause1667);
			conditional_expression141=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression141.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:197:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
	public final JPA2Parser.orderby_clause_return orderby_clause() throws RecognitionException {
		JPA2Parser.orderby_clause_return retval = new JPA2Parser.orderby_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal142=null;
		Token string_literal143=null;
		Token char_literal145=null;
		ParserRuleReturnScope orderby_item144 =null;
		ParserRuleReturnScope orderby_item146 =null;

		Object string_literal142_tree=null;
		Object string_literal143_tree=null;
		Object char_literal145_tree=null;
		RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");

		try {
			// JPA2.g:198:5: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
			// JPA2.g:198:7: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
			{
			string_literal142=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1678); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ORDER.add(string_literal142);

			string_literal143=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1680); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal143);

			pushFollow(FOLLOW_orderby_item_in_orderby_clause1682);
			orderby_item144=orderby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item144.getTree());
			// JPA2.g:198:33: ( ',' orderby_item )*
			loop44:
			while (true) {
				int alt44=2;
				int LA44_0 = input.LA(1);
				if ( (LA44_0==58) ) {
					alt44=1;
				}

				switch (alt44) {
				case 1 :
					// JPA2.g:198:34: ',' orderby_item
					{
					char_literal145=(Token)match(input,58,FOLLOW_58_in_orderby_clause1685); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal145);

					pushFollow(FOLLOW_orderby_item_in_orderby_clause1687);
					orderby_item146=orderby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item146.getTree());
					}
					break;

				default :
					break loop44;
				}
			}

			// AST REWRITE
			// elements: ORDER, BY, orderby_item
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 199:5: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
			{
				// JPA2.g:199:8: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByNode(T_ORDER_BY), root_1);
				adaptor.addChild(root_1, stream_ORDER.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:199:49: ( orderby_item )*
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
	// JPA2.g:200:1: orderby_item : orderby_variable ( sort )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ) ;
	public final JPA2Parser.orderby_item_return orderby_item() throws RecognitionException {
		JPA2Parser.orderby_item_return retval = new JPA2Parser.orderby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope orderby_variable147 =null;
		ParserRuleReturnScope sort148 =null;

		RewriteRuleSubtreeStream stream_orderby_variable=new RewriteRuleSubtreeStream(adaptor,"rule orderby_variable");
		RewriteRuleSubtreeStream stream_sort=new RewriteRuleSubtreeStream(adaptor,"rule sort");

		try {
			// JPA2.g:201:5: ( orderby_variable ( sort )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ) )
			// JPA2.g:201:7: orderby_variable ( sort )?
			{
			pushFollow(FOLLOW_orderby_variable_in_orderby_item1721);
			orderby_variable147=orderby_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable147.getTree());
			// JPA2.g:201:24: ( sort )?
			int alt45=2;
			int LA45_0 = input.LA(1);
			if ( (LA45_0==ASC||LA45_0==DESC) ) {
				alt45=1;
			}
			switch (alt45) {
				case 1 :
					// JPA2.g:201:24: sort
					{
					pushFollow(FOLLOW_sort_in_orderby_item1723);
					sort148=sort();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sort.add(sort148.getTree());
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
			// 202:6: -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? )
			{
				// JPA2.g:202:9: ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);
				adaptor.addChild(root_1, stream_orderby_variable.nextTree());
				// JPA2.g:202:65: ( sort )?
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
	// JPA2.g:203:1: orderby_variable : ( path_expression | general_identification_variable | result_variable | scalar_expression | aggregate_expression );
	public final JPA2Parser.orderby_variable_return orderby_variable() throws RecognitionException {
		JPA2Parser.orderby_variable_return retval = new JPA2Parser.orderby_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression149 =null;
		ParserRuleReturnScope general_identification_variable150 =null;
		ParserRuleReturnScope result_variable151 =null;
		ParserRuleReturnScope scalar_expression152 =null;
		ParserRuleReturnScope aggregate_expression153 =null;


		try {
			// JPA2.g:204:5: ( path_expression | general_identification_variable | result_variable | scalar_expression | aggregate_expression )
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
			case 103:
			case 135:
				{
				alt46=2;
				}
				break;
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 55:
			case 57:
			case 59:
			case 62:
			case 69:
			case 75:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 97:
			case 101:
			case 105:
			case 107:
			case 110:
			case 115:
			case 124:
			case 126:
			case 127:
			case 131:
			case 132:
			case 134:
			case 140:
			case 141:
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
			case 99:
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
					// JPA2.g:204:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_orderby_variable1755);
					path_expression149=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression149.getTree());

					}
					break;
				case 2 :
					// JPA2.g:204:25: general_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_identification_variable_in_orderby_variable1759);
					general_identification_variable150=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable150.getTree());

					}
					break;
				case 3 :
					// JPA2.g:204:59: result_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_result_variable_in_orderby_variable1763);
					result_variable151=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable151.getTree());

					}
					break;
				case 4 :
					// JPA2.g:204:77: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_orderby_variable1767);
					scalar_expression152=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression152.getTree());

					}
					break;
				case 5 :
					// JPA2.g:204:97: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_orderby_variable1771);
					aggregate_expression153=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression153.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:205:1: sort : ( 'ASC' | 'DESC' ) ;
	public final JPA2Parser.sort_return sort() throws RecognitionException {
		JPA2Parser.sort_return retval = new JPA2Parser.sort_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set154=null;

		Object set154_tree=null;

		try {
			// JPA2.g:206:5: ( ( 'ASC' | 'DESC' ) )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set154=input.LT(1);
			if ( input.LA(1)==ASC||input.LA(1)==DESC ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set154));
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
	// JPA2.g:207:1: subquery : lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
	public final JPA2Parser.subquery_return subquery() throws RecognitionException {
		JPA2Parser.subquery_return retval = new JPA2Parser.subquery_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token lp=null;
		Token rp=null;
		Token string_literal155=null;
		ParserRuleReturnScope simple_select_clause156 =null;
		ParserRuleReturnScope subquery_from_clause157 =null;
		ParserRuleReturnScope where_clause158 =null;
		ParserRuleReturnScope groupby_clause159 =null;
		ParserRuleReturnScope having_clause160 =null;

		Object lp_tree=null;
		Object rp_tree=null;
		Object string_literal155_tree=null;
		RewriteRuleTokenStream stream_122=new RewriteRuleTokenStream(adaptor,"token 122");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
		RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");

		try {
			// JPA2.g:208:5: (lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
			// JPA2.g:208:7: lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
			{
			lp=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subquery1801); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(lp);

			string_literal155=(Token)match(input,122,FOLLOW_122_in_subquery1803); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_122.add(string_literal155);

			pushFollow(FOLLOW_simple_select_clause_in_subquery1805);
			simple_select_clause156=simple_select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause156.getTree());
			pushFollow(FOLLOW_subquery_from_clause_in_subquery1807);
			subquery_from_clause157=subquery_from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause157.getTree());
			// JPA2.g:208:65: ( where_clause )?
			int alt47=2;
			int LA47_0 = input.LA(1);
			if ( (LA47_0==138) ) {
				alt47=1;
			}
			switch (alt47) {
				case 1 :
					// JPA2.g:208:66: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_subquery1810);
					where_clause158=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause158.getTree());
					}
					break;

			}

			// JPA2.g:208:81: ( groupby_clause )?
			int alt48=2;
			int LA48_0 = input.LA(1);
			if ( (LA48_0==GROUP) ) {
				alt48=1;
			}
			switch (alt48) {
				case 1 :
					// JPA2.g:208:82: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_subquery1815);
					groupby_clause159=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause159.getTree());
					}
					break;

			}

			// JPA2.g:208:99: ( having_clause )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==HAVING) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// JPA2.g:208:100: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_subquery1820);
					having_clause160=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause160.getTree());
					}
					break;

			}

			rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1826); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(rp);

			// AST REWRITE
			// elements: where_clause, having_clause, simple_select_clause, 122, groupby_clause, subquery_from_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 209:6: -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
			{
				// JPA2.g:209:9: ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);
				adaptor.addChild(root_1, stream_122.nextNode());
				adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
				adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
				// JPA2.g:209:90: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:209:106: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:209:124: ( having_clause )?
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
	// JPA2.g:210:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
	public final JPA2Parser.subquery_from_clause_return subquery_from_clause() throws RecognitionException {
		JPA2Parser.subquery_from_clause_return retval = new JPA2Parser.subquery_from_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token fr=null;
		Token char_literal162=null;
		ParserRuleReturnScope subselect_identification_variable_declaration161 =null;
		ParserRuleReturnScope subselect_identification_variable_declaration163 =null;

		Object fr_tree=null;
		Object char_literal162_tree=null;
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_98=new RewriteRuleTokenStream(adaptor,"token 98");
		RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");

		try {
			// JPA2.g:211:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
			// JPA2.g:211:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
			{
			fr=(Token)match(input,98,FOLLOW_98_in_subquery_from_clause1876); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_98.add(fr);

			pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1878);
			subselect_identification_variable_declaration161=subselect_identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration161.getTree());
			// JPA2.g:211:63: ( ',' subselect_identification_variable_declaration )*
			loop50:
			while (true) {
				int alt50=2;
				int LA50_0 = input.LA(1);
				if ( (LA50_0==58) ) {
					alt50=1;
				}

				switch (alt50) {
				case 1 :
					// JPA2.g:211:64: ',' subselect_identification_variable_declaration
					{
					char_literal162=(Token)match(input,58,FOLLOW_58_in_subquery_from_clause1881); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal162);

					pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1883);
					subselect_identification_variable_declaration163=subselect_identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration163.getTree());
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
			// 212:5: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
			{
				// JPA2.g:212:8: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				// JPA2.g:212:35: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
				while ( stream_subselect_identification_variable_declaration.hasNext() ) {
					// JPA2.g:212:35: ^( T_SOURCE subselect_identification_variable_declaration )
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
	// JPA2.g:214:1: subselect_identification_variable_declaration : ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration );
	public final JPA2Parser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration() throws RecognitionException {
		JPA2Parser.subselect_identification_variable_declaration_return retval = new JPA2Parser.subselect_identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal166=null;
		ParserRuleReturnScope identification_variable_declaration164 =null;
		ParserRuleReturnScope derived_path_expression165 =null;
		ParserRuleReturnScope identification_variable167 =null;
		ParserRuleReturnScope join168 =null;
		ParserRuleReturnScope derived_collection_member_declaration169 =null;

		Object string_literal166_tree=null;

		try {
			// JPA2.g:215:5: ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration )
			int alt52=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA52_1 = input.LA(2);
				if ( (LA52_1==WORD||LA52_1==78) ) {
					alt52=1;
				}
				else if ( (LA52_1==60) ) {
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
			case 130:
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
					// JPA2.g:215:7: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1921);
					identification_variable_declaration164=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration164.getTree());

					}
					break;
				case 2 :
					// JPA2.g:216:7: derived_path_expression 'AS' identification_variable ( join )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1929);
					derived_path_expression165=derived_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_path_expression165.getTree());

					string_literal166=(Token)match(input,78,FOLLOW_78_in_subselect_identification_variable_declaration1931); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal166_tree = (Object)adaptor.create(string_literal166);
					adaptor.addChild(root_0, string_literal166_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration1933);
					identification_variable167=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable167.getTree());

					// JPA2.g:216:60: ( join )*
					loop51:
					while (true) {
						int alt51=2;
						int LA51_0 = input.LA(1);
						if ( (LA51_0==INNER||(LA51_0 >= JOIN && LA51_0 <= LEFT)) ) {
							alt51=1;
						}

						switch (alt51) {
						case 1 :
							// JPA2.g:216:61: join
							{
							pushFollow(FOLLOW_join_in_subselect_identification_variable_declaration1936);
							join168=join();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, join168.getTree());

							}
							break;

						default :
							break loop51;
						}
					}

					}
					break;
				case 3 :
					// JPA2.g:217:7: derived_collection_member_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1946);
					derived_collection_member_declaration169=derived_collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_collection_member_declaration169.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:218:1: derived_path_expression : ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field );
	public final JPA2Parser.derived_path_expression_return derived_path_expression() throws RecognitionException {
		JPA2Parser.derived_path_expression_return retval = new JPA2Parser.derived_path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal171=null;
		Token char_literal174=null;
		ParserRuleReturnScope general_derived_path170 =null;
		ParserRuleReturnScope single_valued_object_field172 =null;
		ParserRuleReturnScope general_derived_path173 =null;
		ParserRuleReturnScope collection_valued_field175 =null;

		Object char_literal171_tree=null;
		Object char_literal174_tree=null;

		try {
			// JPA2.g:219:5: ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field )
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
			else if ( (LA53_0==130) ) {
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
					// JPA2.g:219:7: general_derived_path '.' single_valued_object_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1957);
					general_derived_path170=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path170.getTree());

					char_literal171=(Token)match(input,60,FOLLOW_60_in_derived_path_expression1958); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal171_tree = (Object)adaptor.create(char_literal171);
					adaptor.addChild(root_0, char_literal171_tree);
					}

					pushFollow(FOLLOW_single_valued_object_field_in_derived_path_expression1959);
					single_valued_object_field172=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field172.getTree());

					}
					break;
				case 2 :
					// JPA2.g:220:7: general_derived_path '.' collection_valued_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1967);
					general_derived_path173=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path173.getTree());

					char_literal174=(Token)match(input,60,FOLLOW_60_in_derived_path_expression1968); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal174_tree = (Object)adaptor.create(char_literal174);
					adaptor.addChild(root_0, char_literal174_tree);
					}

					pushFollow(FOLLOW_collection_valued_field_in_derived_path_expression1969);
					collection_valued_field175=collection_valued_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field175.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:221:1: general_derived_path : ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* );
	public final JPA2Parser.general_derived_path_return general_derived_path() throws RecognitionException {
		JPA2Parser.general_derived_path_return retval = new JPA2Parser.general_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal178=null;
		ParserRuleReturnScope simple_derived_path176 =null;
		ParserRuleReturnScope treated_derived_path177 =null;
		ParserRuleReturnScope single_valued_object_field179 =null;

		Object char_literal178_tree=null;

		try {
			// JPA2.g:222:5: ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* )
			int alt55=2;
			int LA55_0 = input.LA(1);
			if ( (LA55_0==WORD) ) {
				alt55=1;
			}
			else if ( (LA55_0==130) ) {
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
					// JPA2.g:222:7: simple_derived_path
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_derived_path_in_general_derived_path1980);
					simple_derived_path176=simple_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_derived_path176.getTree());

					}
					break;
				case 2 :
					// JPA2.g:223:7: treated_derived_path ( '.' single_valued_object_field )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_treated_derived_path_in_general_derived_path1988);
					treated_derived_path177=treated_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, treated_derived_path177.getTree());

					// JPA2.g:223:27: ( '.' single_valued_object_field )*
					loop54:
					while (true) {
						int alt54=2;
						int LA54_0 = input.LA(1);
						if ( (LA54_0==60) ) {
							int LA54_1 = input.LA(2);
							if ( (LA54_1==WORD) ) {
								int LA54_3 = input.LA(3);
								if ( (LA54_3==78) ) {
									int LA54_4 = input.LA(4);
									if ( (LA54_4==WORD) ) {
										int LA54_6 = input.LA(5);
										if ( (LA54_6==RPAREN) ) {
											int LA54_7 = input.LA(6);
											if ( (LA54_7==78) ) {
												int LA54_8 = input.LA(7);
												if ( (LA54_8==WORD) ) {
													int LA54_9 = input.LA(8);
													if ( (LA54_9==RPAREN) ) {
														alt54=1;
													}

												}

											}
											else if ( (LA54_7==60) ) {
												alt54=1;
											}

										}

									}

								}
								else if ( (LA54_3==60) ) {
									alt54=1;
								}

							}

						}

						switch (alt54) {
						case 1 :
							// JPA2.g:223:28: '.' single_valued_object_field
							{
							char_literal178=(Token)match(input,60,FOLLOW_60_in_general_derived_path1990); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal178_tree = (Object)adaptor.create(char_literal178);
							adaptor.addChild(root_0, char_literal178_tree);
							}

							pushFollow(FOLLOW_single_valued_object_field_in_general_derived_path1991);
							single_valued_object_field179=single_valued_object_field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field179.getTree());

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
	// JPA2.g:225:1: simple_derived_path : superquery_identification_variable ;
	public final JPA2Parser.simple_derived_path_return simple_derived_path() throws RecognitionException {
		JPA2Parser.simple_derived_path_return retval = new JPA2Parser.simple_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope superquery_identification_variable180 =null;


		try {
			// JPA2.g:226:5: ( superquery_identification_variable )
			// JPA2.g:226:7: superquery_identification_variable
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_superquery_identification_variable_in_simple_derived_path2009);
			superquery_identification_variable180=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable180.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:228:1: treated_derived_path : 'TREAT(' general_derived_path 'AS' subtype ')' ;
	public final JPA2Parser.treated_derived_path_return treated_derived_path() throws RecognitionException {
		JPA2Parser.treated_derived_path_return retval = new JPA2Parser.treated_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal181=null;
		Token string_literal183=null;
		Token char_literal185=null;
		ParserRuleReturnScope general_derived_path182 =null;
		ParserRuleReturnScope subtype184 =null;

		Object string_literal181_tree=null;
		Object string_literal183_tree=null;
		Object char_literal185_tree=null;

		try {
			// JPA2.g:229:5: ( 'TREAT(' general_derived_path 'AS' subtype ')' )
			// JPA2.g:229:7: 'TREAT(' general_derived_path 'AS' subtype ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal181=(Token)match(input,130,FOLLOW_130_in_treated_derived_path2026); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal181_tree = (Object)adaptor.create(string_literal181);
			adaptor.addChild(root_0, string_literal181_tree);
			}

			pushFollow(FOLLOW_general_derived_path_in_treated_derived_path2027);
			general_derived_path182=general_derived_path();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path182.getTree());

			string_literal183=(Token)match(input,78,FOLLOW_78_in_treated_derived_path2029); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal183_tree = (Object)adaptor.create(string_literal183);
			adaptor.addChild(root_0, string_literal183_tree);
			}

			pushFollow(FOLLOW_subtype_in_treated_derived_path2031);
			subtype184=subtype();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subtype184.getTree());

			char_literal185=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_treated_derived_path2033); if (state.failed) return retval;
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
	// JPA2.g:230:1: derived_collection_member_declaration : 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field ;
	public final JPA2Parser.derived_collection_member_declaration_return derived_collection_member_declaration() throws RecognitionException {
		JPA2Parser.derived_collection_member_declaration_return retval = new JPA2Parser.derived_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal186=null;
		Token char_literal188=null;
		Token char_literal190=null;
		ParserRuleReturnScope superquery_identification_variable187 =null;
		ParserRuleReturnScope single_valued_object_field189 =null;
		ParserRuleReturnScope collection_valued_field191 =null;

		Object string_literal186_tree=null;
		Object char_literal188_tree=null;
		Object char_literal190_tree=null;

		try {
			// JPA2.g:231:5: ( 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field )
			// JPA2.g:231:7: 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field
			{
			root_0 = (Object)adaptor.nil();


			string_literal186=(Token)match(input,IN,FOLLOW_IN_in_derived_collection_member_declaration2044); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal186_tree = (Object)adaptor.create(string_literal186);
			adaptor.addChild(root_0, string_literal186_tree);
			}

			pushFollow(FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2046);
			superquery_identification_variable187=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable187.getTree());

			char_literal188=(Token)match(input,60,FOLLOW_60_in_derived_collection_member_declaration2047); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal188_tree = (Object)adaptor.create(char_literal188);
			adaptor.addChild(root_0, char_literal188_tree);
			}

			// JPA2.g:231:49: ( single_valued_object_field '.' )*
			loop56:
			while (true) {
				int alt56=2;
				int LA56_0 = input.LA(1);
				if ( (LA56_0==WORD) ) {
					int LA56_1 = input.LA(2);
					if ( (LA56_1==60) ) {
						alt56=1;
					}

				}

				switch (alt56) {
				case 1 :
					// JPA2.g:231:50: single_valued_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2049);
					single_valued_object_field189=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field189.getTree());

					char_literal190=(Token)match(input,60,FOLLOW_60_in_derived_collection_member_declaration2051); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal190_tree = (Object)adaptor.create(char_literal190);
					adaptor.addChild(root_0, char_literal190_tree);
					}

					}
					break;

				default :
					break loop56;
				}
			}

			pushFollow(FOLLOW_collection_valued_field_in_derived_collection_member_declaration2054);
			collection_valued_field191=collection_valued_field();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field191.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:233:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
	public final JPA2Parser.simple_select_clause_return simple_select_clause() throws RecognitionException {
		JPA2Parser.simple_select_clause_return retval = new JPA2Parser.simple_select_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal192=null;
		ParserRuleReturnScope simple_select_expression193 =null;

		Object string_literal192_tree=null;
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleSubtreeStream stream_simple_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_expression");

		try {
			// JPA2.g:234:5: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
			// JPA2.g:234:7: ( 'DISTINCT' )? simple_select_expression
			{
			// JPA2.g:234:7: ( 'DISTINCT' )?
			int alt57=2;
			int LA57_0 = input.LA(1);
			if ( (LA57_0==DISTINCT) ) {
				alt57=1;
			}
			switch (alt57) {
				case 1 :
					// JPA2.g:234:8: 'DISTINCT'
					{
					string_literal192=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause2067); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal192);

					}
					break;

			}

			pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause2071);
			simple_select_expression193=simple_select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_expression.add(simple_select_expression193.getTree());
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
			// 235:5: -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
			{
				// JPA2.g:235:8: ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:235:48: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);
				// JPA2.g:235:86: ( 'DISTINCT' )?
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
	// JPA2.g:236:1: simple_select_expression : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.simple_select_expression_return simple_select_expression() throws RecognitionException {
		JPA2Parser.simple_select_expression_return retval = new JPA2Parser.simple_select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression194 =null;
		ParserRuleReturnScope scalar_expression195 =null;
		ParserRuleReturnScope aggregate_expression196 =null;
		ParserRuleReturnScope identification_variable197 =null;


		try {
			// JPA2.g:237:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
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
			case 55:
			case 57:
			case 59:
			case 62:
			case 69:
			case 75:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 97:
			case 101:
			case 105:
			case 107:
			case 110:
			case 115:
			case 124:
			case 126:
			case 127:
			case 131:
			case 132:
			case 134:
			case 140:
			case 141:
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
			case 99:
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
					// JPA2.g:237:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_simple_select_expression2111);
					path_expression194=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression194.getTree());

					}
					break;
				case 2 :
					// JPA2.g:238:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_simple_select_expression2119);
					scalar_expression195=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression195.getTree());

					}
					break;
				case 3 :
					// JPA2.g:239:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression2127);
					aggregate_expression196=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression196.getTree());

					}
					break;
				case 4 :
					// JPA2.g:240:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_select_expression2135);
					identification_variable197=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable197.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:241:1: scalar_expression : ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression );
	public final JPA2Parser.scalar_expression_return scalar_expression() throws RecognitionException {
		JPA2Parser.scalar_expression_return retval = new JPA2Parser.scalar_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope arithmetic_expression198 =null;
		ParserRuleReturnScope string_expression199 =null;
		ParserRuleReturnScope enum_expression200 =null;
		ParserRuleReturnScope datetime_expression201 =null;
		ParserRuleReturnScope boolean_expression202 =null;
		ParserRuleReturnScope case_expression203 =null;
		ParserRuleReturnScope entity_type_expression204 =null;


		try {
			// JPA2.g:242:5: ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression )
			int alt59=7;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 57:
			case 59:
			case 62:
			case 75:
			case 101:
			case 105:
			case 107:
			case 110:
			case 124:
			case 126:
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
			case 69:
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
			case 55:
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
			case 99:
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
			case 81:
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
			case 83:
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
			case 115:
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
			case 82:
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
			case 97:
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
			case LOWER:
			case STRING_LITERAL:
			case 84:
			case 127:
			case 131:
			case 134:
				{
				alt59=2;
				}
				break;
			case 85:
			case 86:
			case 87:
				{
				alt59=4;
				}
				break;
			case 140:
			case 141:
				{
				alt59=5;
				}
				break;
			case 132:
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
					// JPA2.g:242:7: arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_scalar_expression2146);
					arithmetic_expression198=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression198.getTree());

					}
					break;
				case 2 :
					// JPA2.g:243:7: string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_scalar_expression2154);
					string_expression199=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression199.getTree());

					}
					break;
				case 3 :
					// JPA2.g:244:7: enum_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_scalar_expression2162);
					enum_expression200=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression200.getTree());

					}
					break;
				case 4 :
					// JPA2.g:245:7: datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_scalar_expression2170);
					datetime_expression201=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression201.getTree());

					}
					break;
				case 5 :
					// JPA2.g:246:7: boolean_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_scalar_expression2178);
					boolean_expression202=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression202.getTree());

					}
					break;
				case 6 :
					// JPA2.g:247:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_scalar_expression2186);
					case_expression203=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression203.getTree());

					}
					break;
				case 7 :
					// JPA2.g:248:7: entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_scalar_expression2194);
					entity_type_expression204=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression204.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:249:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
	public final JPA2Parser.conditional_expression_return conditional_expression() throws RecognitionException {
		JPA2Parser.conditional_expression_return retval = new JPA2Parser.conditional_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal206=null;
		ParserRuleReturnScope conditional_term205 =null;
		ParserRuleReturnScope conditional_term207 =null;

		Object string_literal206_tree=null;

		try {
			// JPA2.g:250:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
			// JPA2.g:250:7: ( conditional_term ) ( 'OR' conditional_term )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:250:7: ( conditional_term )
			// JPA2.g:250:8: conditional_term
			{
			pushFollow(FOLLOW_conditional_term_in_conditional_expression2206);
			conditional_term205=conditional_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term205.getTree());

			}

			// JPA2.g:250:26: ( 'OR' conditional_term )*
			loop60:
			while (true) {
				int alt60=2;
				int LA60_0 = input.LA(1);
				if ( (LA60_0==OR) ) {
					alt60=1;
				}

				switch (alt60) {
				case 1 :
					// JPA2.g:250:27: 'OR' conditional_term
					{
					string_literal206=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression2210); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal206_tree = (Object)adaptor.create(string_literal206);
					adaptor.addChild(root_0, string_literal206_tree);
					}

					pushFollow(FOLLOW_conditional_term_in_conditional_expression2212);
					conditional_term207=conditional_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term207.getTree());

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
	// JPA2.g:251:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
	public final JPA2Parser.conditional_term_return conditional_term() throws RecognitionException {
		JPA2Parser.conditional_term_return retval = new JPA2Parser.conditional_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal209=null;
		ParserRuleReturnScope conditional_factor208 =null;
		ParserRuleReturnScope conditional_factor210 =null;

		Object string_literal209_tree=null;

		try {
			// JPA2.g:252:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
			// JPA2.g:252:7: ( conditional_factor ) ( 'AND' conditional_factor )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:252:7: ( conditional_factor )
			// JPA2.g:252:8: conditional_factor
			{
			pushFollow(FOLLOW_conditional_factor_in_conditional_term2226);
			conditional_factor208=conditional_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor208.getTree());

			}

			// JPA2.g:252:28: ( 'AND' conditional_factor )*
			loop61:
			while (true) {
				int alt61=2;
				int LA61_0 = input.LA(1);
				if ( (LA61_0==AND) ) {
					alt61=1;
				}

				switch (alt61) {
				case 1 :
					// JPA2.g:252:29: 'AND' conditional_factor
					{
					string_literal209=(Token)match(input,AND,FOLLOW_AND_in_conditional_term2230); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal209_tree = (Object)adaptor.create(string_literal209);
					adaptor.addChild(root_0, string_literal209_tree);
					}

					pushFollow(FOLLOW_conditional_factor_in_conditional_term2232);
					conditional_factor210=conditional_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor210.getTree());

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
	// JPA2.g:253:1: conditional_factor : ( 'NOT' )? conditional_primary ;
	public final JPA2Parser.conditional_factor_return conditional_factor() throws RecognitionException {
		JPA2Parser.conditional_factor_return retval = new JPA2Parser.conditional_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal211=null;
		ParserRuleReturnScope conditional_primary212 =null;

		Object string_literal211_tree=null;

		try {
			// JPA2.g:254:5: ( ( 'NOT' )? conditional_primary )
			// JPA2.g:254:7: ( 'NOT' )? conditional_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:254:7: ( 'NOT' )?
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
					// JPA2.g:254:8: 'NOT'
					{
					string_literal211=(Token)match(input,NOT,FOLLOW_NOT_in_conditional_factor2246); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal211_tree = (Object)adaptor.create(string_literal211);
					adaptor.addChild(root_0, string_literal211_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_conditional_primary_in_conditional_factor2250);
			conditional_primary212=conditional_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_primary212.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:255:1: conditional_primary : ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' );
	public final JPA2Parser.conditional_primary_return conditional_primary() throws RecognitionException {
		JPA2Parser.conditional_primary_return retval = new JPA2Parser.conditional_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal214=null;
		Token char_literal216=null;
		ParserRuleReturnScope simple_cond_expression213 =null;
		ParserRuleReturnScope conditional_expression215 =null;

		Object char_literal214_tree=null;
		Object char_literal216_tree=null;
		RewriteRuleSubtreeStream stream_simple_cond_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_cond_expression");

		try {
			// JPA2.g:256:5: ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' )
			int alt63=2;
			int LA63_0 = input.LA(1);
			if ( (LA63_0==AVG||LA63_0==COUNT||LA63_0==INT_NUMERAL||LA63_0==LOWER||(LA63_0 >= MAX && LA63_0 <= NOT)||(LA63_0 >= STRING_LITERAL && LA63_0 <= SUM)||LA63_0==WORD||LA63_0==55||LA63_0==57||LA63_0==59||LA63_0==62||(LA63_0 >= 69 && LA63_0 <= 75)||(LA63_0 >= 81 && LA63_0 <= 87)||(LA63_0 >= 96 && LA63_0 <= 97)||LA63_0==99||LA63_0==101||LA63_0==105||LA63_0==107||LA63_0==110||LA63_0==115||LA63_0==124||(LA63_0 >= 126 && LA63_0 <= 127)||(LA63_0 >= 130 && LA63_0 <= 132)||LA63_0==134||(LA63_0 >= 140 && LA63_0 <= 141)) ) {
				alt63=1;
			}
			else if ( (LA63_0==LPAREN) ) {
				int LA63_19 = input.LA(2);
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
					// JPA2.g:256:7: simple_cond_expression
					{
					pushFollow(FOLLOW_simple_cond_expression_in_conditional_primary2261);
					simple_cond_expression213=simple_cond_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_simple_cond_expression.add(simple_cond_expression213.getTree());
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
					// 257:5: -> ^( T_SIMPLE_CONDITION[] simple_cond_expression )
					{
						// JPA2.g:257:8: ^( T_SIMPLE_CONDITION[] simple_cond_expression )
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
					// JPA2.g:258:7: '(' conditional_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal214=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_primary2285); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal214_tree = (Object)adaptor.create(char_literal214);
					adaptor.addChild(root_0, char_literal214_tree);
					}

					pushFollow(FOLLOW_conditional_expression_in_conditional_primary2286);
					conditional_expression215=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression215.getTree());

					char_literal216=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_primary2287); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal216_tree = (Object)adaptor.create(char_literal216);
					adaptor.addChild(root_0, char_literal216_tree);
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
	// JPA2.g:259:1: simple_cond_expression : ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
	public final JPA2Parser.simple_cond_expression_return simple_cond_expression() throws RecognitionException {
		JPA2Parser.simple_cond_expression_return retval = new JPA2Parser.simple_cond_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope comparison_expression217 =null;
		ParserRuleReturnScope between_expression218 =null;
		ParserRuleReturnScope in_expression219 =null;
		ParserRuleReturnScope like_expression220 =null;
		ParserRuleReturnScope null_comparison_expression221 =null;
		ParserRuleReturnScope empty_collection_comparison_expression222 =null;
		ParserRuleReturnScope collection_member_expression223 =null;
		ParserRuleReturnScope exists_expression224 =null;
		ParserRuleReturnScope date_macro_expression225 =null;


		try {
			// JPA2.g:260:5: ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
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
			case 69:
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
			case 55:
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
			case 84:
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
			case 127:
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
			case 131:
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
			case 134:
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
			case 99:
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
			case 81:
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
			case 83:
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
			case 115:
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
			case 82:
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
			case 97:
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
			case LPAREN:
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
				else if ( (synpred97_JPA2()) ) {
					alt64=7;
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
			case 140:
			case 141:
				{
				alt64=1;
				}
				break;
			case 85:
			case 86:
			case 87:
				{
				int LA64_21 = input.LA(2);
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
							new NoViableAltException("", 64, 21, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 132:
				{
				int LA64_22 = input.LA(2);
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
							new NoViableAltException("", 64, 22, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 57:
			case 59:
				{
				int LA64_23 = input.LA(2);
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
							new NoViableAltException("", 64, 23, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 62:
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
			case INT_NUMERAL:
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
			case 105:
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
			case 107:
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
			case 75:
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
			case 126:
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
			case 110:
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
			case 124:
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
			case 101:
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
			case 130:
				{
				alt64=5;
				}
				break;
			case NOT:
			case 96:
				{
				alt64=8;
				}
				break;
			case 70:
			case 71:
			case 72:
			case 73:
			case 74:
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
					// JPA2.g:260:7: comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression2298);
					comparison_expression217=comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression217.getTree());

					}
					break;
				case 2 :
					// JPA2.g:261:7: between_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_between_expression_in_simple_cond_expression2306);
					between_expression218=between_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression218.getTree());

					}
					break;
				case 3 :
					// JPA2.g:262:7: in_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_in_expression_in_simple_cond_expression2314);
					in_expression219=in_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression219.getTree());

					}
					break;
				case 4 :
					// JPA2.g:263:7: like_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_like_expression_in_simple_cond_expression2322);
					like_expression220=like_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression220.getTree());

					}
					break;
				case 5 :
					// JPA2.g:264:7: null_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression2330);
					null_comparison_expression221=null_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression221.getTree());

					}
					break;
				case 6 :
					// JPA2.g:265:7: empty_collection_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2338);
					empty_collection_comparison_expression222=empty_collection_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression222.getTree());

					}
					break;
				case 7 :
					// JPA2.g:266:7: collection_member_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression2346);
					collection_member_expression223=collection_member_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression223.getTree());

					}
					break;
				case 8 :
					// JPA2.g:267:7: exists_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exists_expression_in_simple_cond_expression2354);
					exists_expression224=exists_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression224.getTree());

					}
					break;
				case 9 :
					// JPA2.g:268:7: date_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression2362);
					date_macro_expression225=date_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_macro_expression225.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:271:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
	public final JPA2Parser.date_macro_expression_return date_macro_expression() throws RecognitionException {
		JPA2Parser.date_macro_expression_return retval = new JPA2Parser.date_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope date_between_macro_expression226 =null;
		ParserRuleReturnScope date_before_macro_expression227 =null;
		ParserRuleReturnScope date_after_macro_expression228 =null;
		ParserRuleReturnScope date_equals_macro_expression229 =null;
		ParserRuleReturnScope date_today_macro_expression230 =null;


		try {
			// JPA2.g:272:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
			int alt65=5;
			switch ( input.LA(1) ) {
			case 70:
				{
				alt65=1;
				}
				break;
			case 72:
				{
				alt65=2;
				}
				break;
			case 71:
				{
				alt65=3;
				}
				break;
			case 73:
				{
				alt65=4;
				}
				break;
			case 74:
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
					// JPA2.g:272:7: date_between_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2375);
					date_between_macro_expression226=date_between_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression226.getTree());

					}
					break;
				case 2 :
					// JPA2.g:273:7: date_before_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2383);
					date_before_macro_expression227=date_before_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression227.getTree());

					}
					break;
				case 3 :
					// JPA2.g:274:7: date_after_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2391);
					date_after_macro_expression228=date_after_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression228.getTree());

					}
					break;
				case 4 :
					// JPA2.g:275:7: date_equals_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2399);
					date_equals_macro_expression229=date_equals_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression229.getTree());

					}
					break;
				case 5 :
					// JPA2.g:276:7: date_today_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2407);
					date_today_macro_expression230=date_today_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_today_macro_expression230.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:278:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
	public final JPA2Parser.date_between_macro_expression_return date_between_macro_expression() throws RecognitionException {
		JPA2Parser.date_between_macro_expression_return retval = new JPA2Parser.date_between_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal231=null;
		Token char_literal232=null;
		Token char_literal234=null;
		Token string_literal235=null;
		Token set236=null;
		Token char_literal238=null;
		Token string_literal239=null;
		Token set240=null;
		Token char_literal242=null;
		Token set243=null;
		Token char_literal244=null;
		ParserRuleReturnScope path_expression233 =null;
		ParserRuleReturnScope numeric_literal237 =null;
		ParserRuleReturnScope numeric_literal241 =null;

		Object string_literal231_tree=null;
		Object char_literal232_tree=null;
		Object char_literal234_tree=null;
		Object string_literal235_tree=null;
		Object set236_tree=null;
		Object char_literal238_tree=null;
		Object string_literal239_tree=null;
		Object set240_tree=null;
		Object char_literal242_tree=null;
		Object set243_tree=null;
		Object char_literal244_tree=null;

		try {
			// JPA2.g:279:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
			// JPA2.g:279:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal231=(Token)match(input,70,FOLLOW_70_in_date_between_macro_expression2419); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal231_tree = (Object)adaptor.create(string_literal231);
			adaptor.addChild(root_0, string_literal231_tree);
			}

			char_literal232=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2421); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal232_tree = (Object)adaptor.create(char_literal232);
			adaptor.addChild(root_0, char_literal232_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2423);
			path_expression233=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression233.getTree());

			char_literal234=(Token)match(input,58,FOLLOW_58_in_date_between_macro_expression2425); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal234_tree = (Object)adaptor.create(char_literal234);
			adaptor.addChild(root_0, char_literal234_tree);
			}

			string_literal235=(Token)match(input,113,FOLLOW_113_in_date_between_macro_expression2427); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal235_tree = (Object)adaptor.create(string_literal235);
			adaptor.addChild(root_0, string_literal235_tree);
			}

			// JPA2.g:279:48: ( ( '+' | '-' ) numeric_literal )?
			int alt66=2;
			int LA66_0 = input.LA(1);
			if ( (LA66_0==57||LA66_0==59) ) {
				alt66=1;
			}
			switch (alt66) {
				case 1 :
					// JPA2.g:279:49: ( '+' | '-' ) numeric_literal
					{
					set236=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set236));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2438);
					numeric_literal237=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal237.getTree());

					}
					break;

			}

			char_literal238=(Token)match(input,58,FOLLOW_58_in_date_between_macro_expression2442); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal238_tree = (Object)adaptor.create(char_literal238);
			adaptor.addChild(root_0, char_literal238_tree);
			}

			string_literal239=(Token)match(input,113,FOLLOW_113_in_date_between_macro_expression2444); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal239_tree = (Object)adaptor.create(string_literal239);
			adaptor.addChild(root_0, string_literal239_tree);
			}

			// JPA2.g:279:89: ( ( '+' | '-' ) numeric_literal )?
			int alt67=2;
			int LA67_0 = input.LA(1);
			if ( (LA67_0==57||LA67_0==59) ) {
				alt67=1;
			}
			switch (alt67) {
				case 1 :
					// JPA2.g:279:90: ( '+' | '-' ) numeric_literal
					{
					set240=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2455);
					numeric_literal241=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal241.getTree());

					}
					break;

			}

			char_literal242=(Token)match(input,58,FOLLOW_58_in_date_between_macro_expression2459); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal242_tree = (Object)adaptor.create(char_literal242);
			adaptor.addChild(root_0, char_literal242_tree);
			}

			set243=input.LT(1);
			if ( input.LA(1)==88||input.LA(1)==100||input.LA(1)==109||input.LA(1)==111||input.LA(1)==121||input.LA(1)==139 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set243));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			char_literal244=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2484); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal244_tree = (Object)adaptor.create(char_literal244);
			adaptor.addChild(root_0, char_literal244_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:281:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_before_macro_expression_return date_before_macro_expression() throws RecognitionException {
		JPA2Parser.date_before_macro_expression_return retval = new JPA2Parser.date_before_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal245=null;
		Token char_literal246=null;
		Token char_literal248=null;
		Token char_literal251=null;
		ParserRuleReturnScope path_expression247 =null;
		ParserRuleReturnScope path_expression249 =null;
		ParserRuleReturnScope input_parameter250 =null;

		Object string_literal245_tree=null;
		Object char_literal246_tree=null;
		Object char_literal248_tree=null;
		Object char_literal251_tree=null;

		try {
			// JPA2.g:282:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:282:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal245=(Token)match(input,72,FOLLOW_72_in_date_before_macro_expression2496); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal245_tree = (Object)adaptor.create(string_literal245);
			adaptor.addChild(root_0, string_literal245_tree);
			}

			char_literal246=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2498); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal246_tree = (Object)adaptor.create(char_literal246);
			adaptor.addChild(root_0, char_literal246_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2500);
			path_expression247=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression247.getTree());

			char_literal248=(Token)match(input,58,FOLLOW_58_in_date_before_macro_expression2502); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal248_tree = (Object)adaptor.create(char_literal248);
			adaptor.addChild(root_0, char_literal248_tree);
			}

			// JPA2.g:282:45: ( path_expression | input_parameter )
			int alt68=2;
			int LA68_0 = input.LA(1);
			if ( (LA68_0==WORD) ) {
				alt68=1;
			}
			else if ( (LA68_0==NAMED_PARAMETER||LA68_0==55||LA68_0==69) ) {
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
					// JPA2.g:282:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2505);
					path_expression249=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression249.getTree());

					}
					break;
				case 2 :
					// JPA2.g:282:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2509);
					input_parameter250=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter250.getTree());

					}
					break;

			}

			char_literal251=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2512); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal251_tree = (Object)adaptor.create(char_literal251);
			adaptor.addChild(root_0, char_literal251_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:284:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_after_macro_expression_return date_after_macro_expression() throws RecognitionException {
		JPA2Parser.date_after_macro_expression_return retval = new JPA2Parser.date_after_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal252=null;
		Token char_literal253=null;
		Token char_literal255=null;
		Token char_literal258=null;
		ParserRuleReturnScope path_expression254 =null;
		ParserRuleReturnScope path_expression256 =null;
		ParserRuleReturnScope input_parameter257 =null;

		Object string_literal252_tree=null;
		Object char_literal253_tree=null;
		Object char_literal255_tree=null;
		Object char_literal258_tree=null;

		try {
			// JPA2.g:285:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:285:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal252=(Token)match(input,71,FOLLOW_71_in_date_after_macro_expression2524); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal252_tree = (Object)adaptor.create(string_literal252);
			adaptor.addChild(root_0, string_literal252_tree);
			}

			char_literal253=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2526); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal253_tree = (Object)adaptor.create(char_literal253);
			adaptor.addChild(root_0, char_literal253_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2528);
			path_expression254=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression254.getTree());

			char_literal255=(Token)match(input,58,FOLLOW_58_in_date_after_macro_expression2530); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal255_tree = (Object)adaptor.create(char_literal255);
			adaptor.addChild(root_0, char_literal255_tree);
			}

			// JPA2.g:285:44: ( path_expression | input_parameter )
			int alt69=2;
			int LA69_0 = input.LA(1);
			if ( (LA69_0==WORD) ) {
				alt69=1;
			}
			else if ( (LA69_0==NAMED_PARAMETER||LA69_0==55||LA69_0==69) ) {
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
					// JPA2.g:285:45: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2533);
					path_expression256=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression256.getTree());

					}
					break;
				case 2 :
					// JPA2.g:285:63: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2537);
					input_parameter257=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter257.getTree());

					}
					break;

			}

			char_literal258=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2540); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal258_tree = (Object)adaptor.create(char_literal258);
			adaptor.addChild(root_0, char_literal258_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:287:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_equals_macro_expression_return date_equals_macro_expression() throws RecognitionException {
		JPA2Parser.date_equals_macro_expression_return retval = new JPA2Parser.date_equals_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal259=null;
		Token char_literal260=null;
		Token char_literal262=null;
		Token char_literal265=null;
		ParserRuleReturnScope path_expression261 =null;
		ParserRuleReturnScope path_expression263 =null;
		ParserRuleReturnScope input_parameter264 =null;

		Object string_literal259_tree=null;
		Object char_literal260_tree=null;
		Object char_literal262_tree=null;
		Object char_literal265_tree=null;

		try {
			// JPA2.g:288:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:288:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal259=(Token)match(input,73,FOLLOW_73_in_date_equals_macro_expression2552); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal259_tree = (Object)adaptor.create(string_literal259);
			adaptor.addChild(root_0, string_literal259_tree);
			}

			char_literal260=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2554); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal260_tree = (Object)adaptor.create(char_literal260);
			adaptor.addChild(root_0, char_literal260_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2556);
			path_expression261=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression261.getTree());

			char_literal262=(Token)match(input,58,FOLLOW_58_in_date_equals_macro_expression2558); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal262_tree = (Object)adaptor.create(char_literal262);
			adaptor.addChild(root_0, char_literal262_tree);
			}

			// JPA2.g:288:45: ( path_expression | input_parameter )
			int alt70=2;
			int LA70_0 = input.LA(1);
			if ( (LA70_0==WORD) ) {
				alt70=1;
			}
			else if ( (LA70_0==NAMED_PARAMETER||LA70_0==55||LA70_0==69) ) {
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
					pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2561);
					path_expression263=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression263.getTree());

					}
					break;
				case 2 :
					// JPA2.g:288:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2565);
					input_parameter264=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter264.getTree());

					}
					break;

			}

			char_literal265=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2568); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal265_tree = (Object)adaptor.create(char_literal265);
			adaptor.addChild(root_0, char_literal265_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:290:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
	public final JPA2Parser.date_today_macro_expression_return date_today_macro_expression() throws RecognitionException {
		JPA2Parser.date_today_macro_expression_return retval = new JPA2Parser.date_today_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal266=null;
		Token char_literal267=null;
		Token char_literal269=null;
		ParserRuleReturnScope path_expression268 =null;

		Object string_literal266_tree=null;
		Object char_literal267_tree=null;
		Object char_literal269_tree=null;

		try {
			// JPA2.g:291:5: ( '@TODAY' '(' path_expression ')' )
			// JPA2.g:291:7: '@TODAY' '(' path_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal266=(Token)match(input,74,FOLLOW_74_in_date_today_macro_expression2580); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal266_tree = (Object)adaptor.create(string_literal266);
			adaptor.addChild(root_0, string_literal266_tree);
			}

			char_literal267=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2582); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal267_tree = (Object)adaptor.create(char_literal267);
			adaptor.addChild(root_0, char_literal267_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2584);
			path_expression268=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression268.getTree());

			char_literal269=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2586); if (state.failed) return retval;
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
	// $ANTLR end "date_today_macro_expression"


	public static class between_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "between_expression"
	// JPA2.g:294:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
	public final JPA2Parser.between_expression_return between_expression() throws RecognitionException {
		JPA2Parser.between_expression_return retval = new JPA2Parser.between_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal271=null;
		Token string_literal272=null;
		Token string_literal274=null;
		Token string_literal277=null;
		Token string_literal278=null;
		Token string_literal280=null;
		Token string_literal283=null;
		Token string_literal284=null;
		Token string_literal286=null;
		ParserRuleReturnScope arithmetic_expression270 =null;
		ParserRuleReturnScope arithmetic_expression273 =null;
		ParserRuleReturnScope arithmetic_expression275 =null;
		ParserRuleReturnScope string_expression276 =null;
		ParserRuleReturnScope string_expression279 =null;
		ParserRuleReturnScope string_expression281 =null;
		ParserRuleReturnScope datetime_expression282 =null;
		ParserRuleReturnScope datetime_expression285 =null;
		ParserRuleReturnScope datetime_expression287 =null;

		Object string_literal271_tree=null;
		Object string_literal272_tree=null;
		Object string_literal274_tree=null;
		Object string_literal277_tree=null;
		Object string_literal278_tree=null;
		Object string_literal280_tree=null;
		Object string_literal283_tree=null;
		Object string_literal284_tree=null;
		Object string_literal286_tree=null;

		try {
			// JPA2.g:295:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
			int alt74=3;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 57:
			case 59:
			case 62:
			case 75:
			case 101:
			case 105:
			case 107:
			case 110:
			case 124:
			case 126:
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
			case 69:
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
			case 55:
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
			case 99:
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
			case 81:
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
			case 83:
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
			case 115:
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
			case 82:
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
			case 97:
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
			case LOWER:
			case STRING_LITERAL:
			case 84:
			case 127:
			case 131:
			case 134:
				{
				alt74=2;
				}
				break;
			case 85:
			case 86:
			case 87:
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
					// JPA2.g:295:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2599);
					arithmetic_expression270=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression270.getTree());

					// JPA2.g:295:29: ( 'NOT' )?
					int alt71=2;
					int LA71_0 = input.LA(1);
					if ( (LA71_0==NOT) ) {
						alt71=1;
					}
					switch (alt71) {
						case 1 :
							// JPA2.g:295:30: 'NOT'
							{
							string_literal271=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2602); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal271_tree = (Object)adaptor.create(string_literal271);
							adaptor.addChild(root_0, string_literal271_tree);
							}

							}
							break;

					}

					string_literal272=(Token)match(input,79,FOLLOW_79_in_between_expression2606); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal272_tree = (Object)adaptor.create(string_literal272);
					adaptor.addChild(root_0, string_literal272_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2608);
					arithmetic_expression273=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression273.getTree());

					string_literal274=(Token)match(input,AND,FOLLOW_AND_in_between_expression2610); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal274_tree = (Object)adaptor.create(string_literal274);
					adaptor.addChild(root_0, string_literal274_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2612);
					arithmetic_expression275=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression275.getTree());

					}
					break;
				case 2 :
					// JPA2.g:296:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_between_expression2620);
					string_expression276=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression276.getTree());

					// JPA2.g:296:25: ( 'NOT' )?
					int alt72=2;
					int LA72_0 = input.LA(1);
					if ( (LA72_0==NOT) ) {
						alt72=1;
					}
					switch (alt72) {
						case 1 :
							// JPA2.g:296:26: 'NOT'
							{
							string_literal277=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2623); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal277_tree = (Object)adaptor.create(string_literal277);
							adaptor.addChild(root_0, string_literal277_tree);
							}

							}
							break;

					}

					string_literal278=(Token)match(input,79,FOLLOW_79_in_between_expression2627); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal278_tree = (Object)adaptor.create(string_literal278);
					adaptor.addChild(root_0, string_literal278_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2629);
					string_expression279=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression279.getTree());

					string_literal280=(Token)match(input,AND,FOLLOW_AND_in_between_expression2631); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal280_tree = (Object)adaptor.create(string_literal280);
					adaptor.addChild(root_0, string_literal280_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2633);
					string_expression281=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression281.getTree());

					}
					break;
				case 3 :
					// JPA2.g:297:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_between_expression2641);
					datetime_expression282=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression282.getTree());

					// JPA2.g:297:27: ( 'NOT' )?
					int alt73=2;
					int LA73_0 = input.LA(1);
					if ( (LA73_0==NOT) ) {
						alt73=1;
					}
					switch (alt73) {
						case 1 :
							// JPA2.g:297:28: 'NOT'
							{
							string_literal283=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2644); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal283_tree = (Object)adaptor.create(string_literal283);
							adaptor.addChild(root_0, string_literal283_tree);
							}

							}
							break;

					}

					string_literal284=(Token)match(input,79,FOLLOW_79_in_between_expression2648); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal284_tree = (Object)adaptor.create(string_literal284);
					adaptor.addChild(root_0, string_literal284_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2650);
					datetime_expression285=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression285.getTree());

					string_literal286=(Token)match(input,AND,FOLLOW_AND_in_between_expression2652); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal286_tree = (Object)adaptor.create(string_literal286);
					adaptor.addChild(root_0, string_literal286_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2654);
					datetime_expression287=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression287.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:298:1: in_expression : ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) ;
	public final JPA2Parser.in_expression_return in_expression() throws RecognitionException {
		JPA2Parser.in_expression_return retval = new JPA2Parser.in_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NOT291=null;
		Token IN292=null;
		Token char_literal293=null;
		Token char_literal295=null;
		Token char_literal297=null;
		Token char_literal300=null;
		Token char_literal302=null;
		ParserRuleReturnScope path_expression288 =null;
		ParserRuleReturnScope type_discriminator289 =null;
		ParserRuleReturnScope identification_variable290 =null;
		ParserRuleReturnScope in_item294 =null;
		ParserRuleReturnScope in_item296 =null;
		ParserRuleReturnScope subquery298 =null;
		ParserRuleReturnScope collection_valued_input_parameter299 =null;
		ParserRuleReturnScope path_expression301 =null;

		Object NOT291_tree=null;
		Object IN292_tree=null;
		Object char_literal293_tree=null;
		Object char_literal295_tree=null;
		Object char_literal297_tree=null;
		Object char_literal300_tree=null;
		Object char_literal302_tree=null;

		try {
			// JPA2.g:299:5: ( ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) )
			// JPA2.g:299:7: ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:299:7: ( path_expression | type_discriminator | identification_variable )
			int alt75=3;
			int LA75_0 = input.LA(1);
			if ( (LA75_0==WORD) ) {
				int LA75_1 = input.LA(2);
				if ( (LA75_1==60) ) {
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
			else if ( (LA75_0==132) ) {
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
					// JPA2.g:299:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_in_expression2666);
					path_expression288=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression288.getTree());

					}
					break;
				case 2 :
					// JPA2.g:299:26: type_discriminator
					{
					pushFollow(FOLLOW_type_discriminator_in_in_expression2670);
					type_discriminator289=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator289.getTree());

					}
					break;
				case 3 :
					// JPA2.g:299:47: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_in_expression2674);
					identification_variable290=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable290.getTree());

					}
					break;

			}

			// JPA2.g:299:72: ( NOT )?
			int alt76=2;
			int LA76_0 = input.LA(1);
			if ( (LA76_0==NOT) ) {
				alt76=1;
			}
			switch (alt76) {
				case 1 :
					// JPA2.g:299:73: NOT
					{
					NOT291=(Token)match(input,NOT,FOLLOW_NOT_in_in_expression2678); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT291_tree = (Object)adaptor.create(NOT291);
					adaptor.addChild(root_0, NOT291_tree);
					}

					}
					break;

			}

			IN292=(Token)match(input,IN,FOLLOW_IN_in_in_expression2682); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN292_tree = (Object)adaptor.create(IN292);
			adaptor.addChild(root_0, IN292_tree);
			}

			// JPA2.g:300:13: ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			int alt78=4;
			int LA78_0 = input.LA(1);
			if ( (LA78_0==LPAREN) ) {
				switch ( input.LA(2) ) {
				case 122:
					{
					alt78=2;
					}
					break;
				case WORD:
					{
					int LA78_4 = input.LA(3);
					if ( (LA78_4==RPAREN||LA78_4==58) ) {
						alt78=1;
					}
					else if ( (LA78_4==60) ) {
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
				case 55:
				case 69:
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
			else if ( (LA78_0==NAMED_PARAMETER||LA78_0==55||LA78_0==69) ) {
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
					// JPA2.g:300:15: '(' in_item ( ',' in_item )* ')'
					{
					char_literal293=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2698); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal293_tree = (Object)adaptor.create(char_literal293);
					adaptor.addChild(root_0, char_literal293_tree);
					}

					pushFollow(FOLLOW_in_item_in_in_expression2700);
					in_item294=in_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item294.getTree());

					// JPA2.g:300:27: ( ',' in_item )*
					loop77:
					while (true) {
						int alt77=2;
						int LA77_0 = input.LA(1);
						if ( (LA77_0==58) ) {
							alt77=1;
						}

						switch (alt77) {
						case 1 :
							// JPA2.g:300:28: ',' in_item
							{
							char_literal295=(Token)match(input,58,FOLLOW_58_in_in_expression2703); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal295_tree = (Object)adaptor.create(char_literal295);
							adaptor.addChild(root_0, char_literal295_tree);
							}

							pushFollow(FOLLOW_in_item_in_in_expression2705);
							in_item296=in_item();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item296.getTree());

							}
							break;

						default :
							break loop77;
						}
					}

					char_literal297=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2709); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal297_tree = (Object)adaptor.create(char_literal297);
					adaptor.addChild(root_0, char_literal297_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:301:15: subquery
					{
					pushFollow(FOLLOW_subquery_in_in_expression2725);
					subquery298=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery298.getTree());

					}
					break;
				case 3 :
					// JPA2.g:302:15: collection_valued_input_parameter
					{
					pushFollow(FOLLOW_collection_valued_input_parameter_in_in_expression2741);
					collection_valued_input_parameter299=collection_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_input_parameter299.getTree());

					}
					break;
				case 4 :
					// JPA2.g:303:15: '(' path_expression ')'
					{
					char_literal300=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2757); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal300_tree = (Object)adaptor.create(char_literal300);
					adaptor.addChild(root_0, char_literal300_tree);
					}

					pushFollow(FOLLOW_path_expression_in_in_expression2759);
					path_expression301=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression301.getTree());

					char_literal302=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2761); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal302_tree = (Object)adaptor.create(char_literal302);
					adaptor.addChild(root_0, char_literal302_tree);
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
	// JPA2.g:309:1: in_item : ( literal | single_valued_input_parameter );
	public final JPA2Parser.in_item_return in_item() throws RecognitionException {
		JPA2Parser.in_item_return retval = new JPA2Parser.in_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal303 =null;
		ParserRuleReturnScope single_valued_input_parameter304 =null;


		try {
			// JPA2.g:310:5: ( literal | single_valued_input_parameter )
			int alt79=2;
			int LA79_0 = input.LA(1);
			if ( (LA79_0==WORD) ) {
				alt79=1;
			}
			else if ( (LA79_0==NAMED_PARAMETER||LA79_0==55||LA79_0==69) ) {
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
					// JPA2.g:310:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_in_item2789);
					literal303=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal303.getTree());

					}
					break;
				case 2 :
					// JPA2.g:310:17: single_valued_input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_single_valued_input_parameter_in_in_item2793);
					single_valued_input_parameter304=single_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_input_parameter304.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:311:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? ;
	public final JPA2Parser.like_expression_return like_expression() throws RecognitionException {
		JPA2Parser.like_expression_return retval = new JPA2Parser.like_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal306=null;
		Token string_literal307=null;
		Token string_literal311=null;
		ParserRuleReturnScope string_expression305 =null;
		ParserRuleReturnScope string_expression308 =null;
		ParserRuleReturnScope pattern_value309 =null;
		ParserRuleReturnScope input_parameter310 =null;
		ParserRuleReturnScope escape_character312 =null;

		Object string_literal306_tree=null;
		Object string_literal307_tree=null;
		Object string_literal311_tree=null;

		try {
			// JPA2.g:312:5: ( string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? )
			// JPA2.g:312:7: string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_expression_in_like_expression2804);
			string_expression305=string_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression305.getTree());

			// JPA2.g:312:25: ( 'NOT' )?
			int alt80=2;
			int LA80_0 = input.LA(1);
			if ( (LA80_0==NOT) ) {
				alt80=1;
			}
			switch (alt80) {
				case 1 :
					// JPA2.g:312:26: 'NOT'
					{
					string_literal306=(Token)match(input,NOT,FOLLOW_NOT_in_like_expression2807); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal306_tree = (Object)adaptor.create(string_literal306);
					adaptor.addChild(root_0, string_literal306_tree);
					}

					}
					break;

			}

			string_literal307=(Token)match(input,106,FOLLOW_106_in_like_expression2811); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal307_tree = (Object)adaptor.create(string_literal307);
			adaptor.addChild(root_0, string_literal307_tree);
			}

			// JPA2.g:312:41: ( string_expression | pattern_value | input_parameter )
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
			case 81:
			case 82:
			case 83:
			case 84:
			case 97:
			case 99:
			case 115:
			case 127:
			case 131:
			case 134:
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
			case 69:
				{
				int LA81_3 = input.LA(2);
				if ( (LA81_3==62) ) {
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
			case 55:
				{
				int LA81_5 = input.LA(2);
				if ( (LA81_5==WORD) ) {
					int LA81_10 = input.LA(3);
					if ( (LA81_10==142) ) {
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
					// JPA2.g:312:42: string_expression
					{
					pushFollow(FOLLOW_string_expression_in_like_expression2814);
					string_expression308=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression308.getTree());

					}
					break;
				case 2 :
					// JPA2.g:312:62: pattern_value
					{
					pushFollow(FOLLOW_pattern_value_in_like_expression2818);
					pattern_value309=pattern_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value309.getTree());

					}
					break;
				case 3 :
					// JPA2.g:312:78: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_like_expression2822);
					input_parameter310=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter310.getTree());

					}
					break;

			}

			// JPA2.g:312:94: ( 'ESCAPE' escape_character )?
			int alt82=2;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==95) ) {
				alt82=1;
			}
			switch (alt82) {
				case 1 :
					// JPA2.g:312:95: 'ESCAPE' escape_character
					{
					string_literal311=(Token)match(input,95,FOLLOW_95_in_like_expression2825); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal311_tree = (Object)adaptor.create(string_literal311);
					adaptor.addChild(root_0, string_literal311_tree);
					}

					pushFollow(FOLLOW_escape_character_in_like_expression2827);
					escape_character312=escape_character();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, escape_character312.getTree());

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
	// JPA2.g:313:1: null_comparison_expression : ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' ;
	public final JPA2Parser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
		JPA2Parser.null_comparison_expression_return retval = new JPA2Parser.null_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal316=null;
		Token string_literal317=null;
		Token string_literal318=null;
		ParserRuleReturnScope path_expression313 =null;
		ParserRuleReturnScope input_parameter314 =null;
		ParserRuleReturnScope join_association_path_expression315 =null;

		Object string_literal316_tree=null;
		Object string_literal317_tree=null;
		Object string_literal318_tree=null;

		try {
			// JPA2.g:314:5: ( ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' )
			// JPA2.g:314:7: ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:314:7: ( path_expression | input_parameter | join_association_path_expression )
			int alt83=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA83_1 = input.LA(2);
				if ( (LA83_1==60) ) {
					int LA83_4 = input.LA(3);
					if ( (synpred132_JPA2()) ) {
						alt83=1;
					}
					else if ( (true) ) {
						alt83=3;
					}

				}
				else if ( (LA83_1==102) ) {
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
			case 55:
			case 69:
				{
				alt83=2;
				}
				break;
			case 130:
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
					// JPA2.g:314:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_null_comparison_expression2841);
					path_expression313=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression313.getTree());

					}
					break;
				case 2 :
					// JPA2.g:314:26: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_null_comparison_expression2845);
					input_parameter314=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter314.getTree());

					}
					break;
				case 3 :
					// JPA2.g:314:44: join_association_path_expression
					{
					pushFollow(FOLLOW_join_association_path_expression_in_null_comparison_expression2849);
					join_association_path_expression315=join_association_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression315.getTree());

					}
					break;

			}

			string_literal316=(Token)match(input,102,FOLLOW_102_in_null_comparison_expression2852); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal316_tree = (Object)adaptor.create(string_literal316);
			adaptor.addChild(root_0, string_literal316_tree);
			}

			// JPA2.g:314:83: ( 'NOT' )?
			int alt84=2;
			int LA84_0 = input.LA(1);
			if ( (LA84_0==NOT) ) {
				alt84=1;
			}
			switch (alt84) {
				case 1 :
					// JPA2.g:314:84: 'NOT'
					{
					string_literal317=(Token)match(input,NOT,FOLLOW_NOT_in_null_comparison_expression2855); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal317_tree = (Object)adaptor.create(string_literal317);
					adaptor.addChild(root_0, string_literal317_tree);
					}

					}
					break;

			}

			string_literal318=(Token)match(input,114,FOLLOW_114_in_null_comparison_expression2859); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal318_tree = (Object)adaptor.create(string_literal318);
			adaptor.addChild(root_0, string_literal318_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:315:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
	public final JPA2Parser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
		JPA2Parser.empty_collection_comparison_expression_return retval = new JPA2Parser.empty_collection_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal320=null;
		Token string_literal321=null;
		Token string_literal322=null;
		ParserRuleReturnScope path_expression319 =null;

		Object string_literal320_tree=null;
		Object string_literal321_tree=null;
		Object string_literal322_tree=null;

		try {
			// JPA2.g:316:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
			// JPA2.g:316:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression2870);
			path_expression319=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression319.getTree());

			string_literal320=(Token)match(input,102,FOLLOW_102_in_empty_collection_comparison_expression2872); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal320_tree = (Object)adaptor.create(string_literal320);
			adaptor.addChild(root_0, string_literal320_tree);
			}

			// JPA2.g:316:28: ( 'NOT' )?
			int alt85=2;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==NOT) ) {
				alt85=1;
			}
			switch (alt85) {
				case 1 :
					// JPA2.g:316:29: 'NOT'
					{
					string_literal321=(Token)match(input,NOT,FOLLOW_NOT_in_empty_collection_comparison_expression2875); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal321_tree = (Object)adaptor.create(string_literal321);
					adaptor.addChild(root_0, string_literal321_tree);
					}

					}
					break;

			}

			string_literal322=(Token)match(input,91,FOLLOW_91_in_empty_collection_comparison_expression2879); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal322_tree = (Object)adaptor.create(string_literal322);
			adaptor.addChild(root_0, string_literal322_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:317:1: collection_member_expression : entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
	public final JPA2Parser.collection_member_expression_return collection_member_expression() throws RecognitionException {
		JPA2Parser.collection_member_expression_return retval = new JPA2Parser.collection_member_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal324=null;
		Token string_literal325=null;
		Token string_literal326=null;
		ParserRuleReturnScope entity_or_value_expression323 =null;
		ParserRuleReturnScope path_expression327 =null;

		Object string_literal324_tree=null;
		Object string_literal325_tree=null;
		Object string_literal326_tree=null;

		try {
			// JPA2.g:318:5: ( entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
			// JPA2.g:318:7: entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_or_value_expression_in_collection_member_expression2890);
			entity_or_value_expression323=entity_or_value_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_or_value_expression323.getTree());

			// JPA2.g:318:35: ( 'NOT' )?
			int alt86=2;
			int LA86_0 = input.LA(1);
			if ( (LA86_0==NOT) ) {
				alt86=1;
			}
			switch (alt86) {
				case 1 :
					// JPA2.g:318:36: 'NOT'
					{
					string_literal324=(Token)match(input,NOT,FOLLOW_NOT_in_collection_member_expression2894); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal324_tree = (Object)adaptor.create(string_literal324);
					adaptor.addChild(root_0, string_literal324_tree);
					}

					}
					break;

			}

			string_literal325=(Token)match(input,108,FOLLOW_108_in_collection_member_expression2898); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal325_tree = (Object)adaptor.create(string_literal325);
			adaptor.addChild(root_0, string_literal325_tree);
			}

			// JPA2.g:318:53: ( 'OF' )?
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==117) ) {
				alt87=1;
			}
			switch (alt87) {
				case 1 :
					// JPA2.g:318:54: 'OF'
					{
					string_literal326=(Token)match(input,117,FOLLOW_117_in_collection_member_expression2901); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal326_tree = (Object)adaptor.create(string_literal326);
					adaptor.addChild(root_0, string_literal326_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_path_expression_in_collection_member_expression2905);
			path_expression327=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression327.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:319:1: entity_or_value_expression : ( path_expression | simple_entity_or_value_expression | subquery );
	public final JPA2Parser.entity_or_value_expression_return entity_or_value_expression() throws RecognitionException {
		JPA2Parser.entity_or_value_expression_return retval = new JPA2Parser.entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression328 =null;
		ParserRuleReturnScope simple_entity_or_value_expression329 =null;
		ParserRuleReturnScope subquery330 =null;


		try {
			// JPA2.g:320:5: ( path_expression | simple_entity_or_value_expression | subquery )
			int alt88=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA88_1 = input.LA(2);
				if ( (LA88_1==60) ) {
					alt88=1;
				}
				else if ( (LA88_1==NOT||LA88_1==108) ) {
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
			case 55:
			case 69:
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
					// JPA2.g:320:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_or_value_expression2916);
					path_expression328=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression328.getTree());

					}
					break;
				case 2 :
					// JPA2.g:321:7: simple_entity_or_value_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2924);
					simple_entity_or_value_expression329=simple_entity_or_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_or_value_expression329.getTree());

					}
					break;
				case 3 :
					// JPA2.g:322:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_entity_or_value_expression2932);
					subquery330=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery330.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:323:1: simple_entity_or_value_expression : ( identification_variable | input_parameter | literal );
	public final JPA2Parser.simple_entity_or_value_expression_return simple_entity_or_value_expression() throws RecognitionException {
		JPA2Parser.simple_entity_or_value_expression_return retval = new JPA2Parser.simple_entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable331 =null;
		ParserRuleReturnScope input_parameter332 =null;
		ParserRuleReturnScope literal333 =null;


		try {
			// JPA2.g:324:5: ( identification_variable | input_parameter | literal )
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
			else if ( (LA89_0==NAMED_PARAMETER||LA89_0==55||LA89_0==69) ) {
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
					// JPA2.g:324:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_or_value_expression2943);
					identification_variable331=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable331.getTree());

					}
					break;
				case 2 :
					// JPA2.g:325:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_or_value_expression2951);
					input_parameter332=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter332.getTree());

					}
					break;
				case 3 :
					// JPA2.g:326:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_simple_entity_or_value_expression2959);
					literal333=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal333.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:327:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
	public final JPA2Parser.exists_expression_return exists_expression() throws RecognitionException {
		JPA2Parser.exists_expression_return retval = new JPA2Parser.exists_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal334=null;
		Token string_literal335=null;
		ParserRuleReturnScope subquery336 =null;

		Object string_literal334_tree=null;
		Object string_literal335_tree=null;

		try {
			// JPA2.g:328:5: ( ( 'NOT' )? 'EXISTS' subquery )
			// JPA2.g:328:7: ( 'NOT' )? 'EXISTS' subquery
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:328:7: ( 'NOT' )?
			int alt90=2;
			int LA90_0 = input.LA(1);
			if ( (LA90_0==NOT) ) {
				alt90=1;
			}
			switch (alt90) {
				case 1 :
					// JPA2.g:328:8: 'NOT'
					{
					string_literal334=(Token)match(input,NOT,FOLLOW_NOT_in_exists_expression2971); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal334_tree = (Object)adaptor.create(string_literal334);
					adaptor.addChild(root_0, string_literal334_tree);
					}

					}
					break;

			}

			string_literal335=(Token)match(input,96,FOLLOW_96_in_exists_expression2975); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal335_tree = (Object)adaptor.create(string_literal335);
			adaptor.addChild(root_0, string_literal335_tree);
			}

			pushFollow(FOLLOW_subquery_in_exists_expression2977);
			subquery336=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery336.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:329:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
	public final JPA2Parser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
		JPA2Parser.all_or_any_expression_return retval = new JPA2Parser.all_or_any_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set337=null;
		ParserRuleReturnScope subquery338 =null;

		Object set337_tree=null;

		try {
			// JPA2.g:330:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
			// JPA2.g:330:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
			{
			root_0 = (Object)adaptor.nil();


			set337=input.LT(1);
			if ( (input.LA(1) >= 76 && input.LA(1) <= 77)||input.LA(1)==125 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set337));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_subquery_in_all_or_any_expression3001);
			subquery338=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery338.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:331:1: comparison_expression : ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
	public final JPA2Parser.comparison_expression_return comparison_expression() throws RecognitionException {
		JPA2Parser.comparison_expression_return retval = new JPA2Parser.comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal341=null;
		Token set345=null;
		Token set349=null;
		Token set357=null;
		Token set361=null;
		ParserRuleReturnScope string_expression339 =null;
		ParserRuleReturnScope comparison_operator340 =null;
		ParserRuleReturnScope string_expression342 =null;
		ParserRuleReturnScope all_or_any_expression343 =null;
		ParserRuleReturnScope boolean_expression344 =null;
		ParserRuleReturnScope boolean_expression346 =null;
		ParserRuleReturnScope all_or_any_expression347 =null;
		ParserRuleReturnScope enum_expression348 =null;
		ParserRuleReturnScope enum_expression350 =null;
		ParserRuleReturnScope all_or_any_expression351 =null;
		ParserRuleReturnScope datetime_expression352 =null;
		ParserRuleReturnScope comparison_operator353 =null;
		ParserRuleReturnScope datetime_expression354 =null;
		ParserRuleReturnScope all_or_any_expression355 =null;
		ParserRuleReturnScope entity_expression356 =null;
		ParserRuleReturnScope entity_expression358 =null;
		ParserRuleReturnScope all_or_any_expression359 =null;
		ParserRuleReturnScope entity_type_expression360 =null;
		ParserRuleReturnScope entity_type_expression362 =null;
		ParserRuleReturnScope arithmetic_expression363 =null;
		ParserRuleReturnScope comparison_operator364 =null;
		ParserRuleReturnScope arithmetic_expression365 =null;
		ParserRuleReturnScope all_or_any_expression366 =null;

		Object string_literal341_tree=null;
		Object set345_tree=null;
		Object set349_tree=null;
		Object set357_tree=null;
		Object set361_tree=null;

		try {
			// JPA2.g:332:5: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
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
			case 84:
			case 127:
			case 131:
			case 134:
				{
				alt98=1;
				}
				break;
			case 69:
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
			case 55:
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
			case 99:
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
			case 81:
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
			case 83:
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
			case 115:
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
			case 82:
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
			case 97:
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
			case LPAREN:
				{
				int LA98_19 = input.LA(2);
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
			case 140:
			case 141:
				{
				alt98=2;
				}
				break;
			case 85:
			case 86:
			case 87:
				{
				alt98=4;
				}
				break;
			case 132:
				{
				alt98=6;
				}
				break;
			case INT_NUMERAL:
			case 57:
			case 59:
			case 62:
			case 75:
			case 101:
			case 105:
			case 107:
			case 110:
			case 124:
			case 126:
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
					// JPA2.g:332:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_comparison_expression3012);
					string_expression339=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression339.getTree());

					// JPA2.g:332:25: ( comparison_operator | 'REGEXP' )
					int alt91=2;
					int LA91_0 = input.LA(1);
					if ( ((LA91_0 >= 63 && LA91_0 <= 68)) ) {
						alt91=1;
					}
					else if ( (LA91_0==120) ) {
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
							// JPA2.g:332:26: comparison_operator
							{
							pushFollow(FOLLOW_comparison_operator_in_comparison_expression3015);
							comparison_operator340=comparison_operator();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator340.getTree());

							}
							break;
						case 2 :
							// JPA2.g:332:48: 'REGEXP'
							{
							string_literal341=(Token)match(input,120,FOLLOW_120_in_comparison_expression3019); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal341_tree = (Object)adaptor.create(string_literal341);
							adaptor.addChild(root_0, string_literal341_tree);
							}

							}
							break;

					}

					// JPA2.g:332:58: ( string_expression | all_or_any_expression )
					int alt92=2;
					int LA92_0 = input.LA(1);
					if ( (LA92_0==AVG||LA92_0==COUNT||(LA92_0 >= LOWER && LA92_0 <= NAMED_PARAMETER)||(LA92_0 >= STRING_LITERAL && LA92_0 <= SUM)||LA92_0==WORD||LA92_0==55||LA92_0==69||(LA92_0 >= 81 && LA92_0 <= 84)||LA92_0==97||LA92_0==99||LA92_0==115||LA92_0==127||LA92_0==131||LA92_0==134) ) {
						alt92=1;
					}
					else if ( ((LA92_0 >= 76 && LA92_0 <= 77)||LA92_0==125) ) {
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
							// JPA2.g:332:59: string_expression
							{
							pushFollow(FOLLOW_string_expression_in_comparison_expression3023);
							string_expression342=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression342.getTree());

							}
							break;
						case 2 :
							// JPA2.g:332:79: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3027);
							all_or_any_expression343=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression343.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:333:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_comparison_expression3036);
					boolean_expression344=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression344.getTree());

					set345=input.LT(1);
					if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set345));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:333:39: ( boolean_expression | all_or_any_expression )
					int alt93=2;
					int LA93_0 = input.LA(1);
					if ( (LA93_0==LPAREN||LA93_0==NAMED_PARAMETER||LA93_0==WORD||LA93_0==55||LA93_0==69||(LA93_0 >= 81 && LA93_0 <= 83)||LA93_0==97||LA93_0==99||LA93_0==115||(LA93_0 >= 140 && LA93_0 <= 141)) ) {
						alt93=1;
					}
					else if ( ((LA93_0 >= 76 && LA93_0 <= 77)||LA93_0==125) ) {
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
							// JPA2.g:333:40: boolean_expression
							{
							pushFollow(FOLLOW_boolean_expression_in_comparison_expression3047);
							boolean_expression346=boolean_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression346.getTree());

							}
							break;
						case 2 :
							// JPA2.g:333:61: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3051);
							all_or_any_expression347=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression347.getTree());

							}
							break;

					}

					}
					break;
				case 3 :
					// JPA2.g:334:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_comparison_expression3060);
					enum_expression348=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression348.getTree());

					set349=input.LT(1);
					if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set349));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:334:34: ( enum_expression | all_or_any_expression )
					int alt94=2;
					int LA94_0 = input.LA(1);
					if ( (LA94_0==LPAREN||LA94_0==NAMED_PARAMETER||LA94_0==WORD||LA94_0==55||LA94_0==69||LA94_0==81||LA94_0==83||LA94_0==115) ) {
						alt94=1;
					}
					else if ( ((LA94_0 >= 76 && LA94_0 <= 77)||LA94_0==125) ) {
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
							// JPA2.g:334:35: enum_expression
							{
							pushFollow(FOLLOW_enum_expression_in_comparison_expression3069);
							enum_expression350=enum_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression350.getTree());

							}
							break;
						case 2 :
							// JPA2.g:334:53: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3073);
							all_or_any_expression351=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression351.getTree());

							}
							break;

					}

					}
					break;
				case 4 :
					// JPA2.g:335:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_comparison_expression3082);
					datetime_expression352=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression352.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3084);
					comparison_operator353=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator353.getTree());

					// JPA2.g:335:47: ( datetime_expression | all_or_any_expression )
					int alt95=2;
					int LA95_0 = input.LA(1);
					if ( (LA95_0==AVG||LA95_0==COUNT||(LA95_0 >= LPAREN && LA95_0 <= NAMED_PARAMETER)||LA95_0==SUM||LA95_0==WORD||LA95_0==55||LA95_0==69||(LA95_0 >= 81 && LA95_0 <= 83)||(LA95_0 >= 85 && LA95_0 <= 87)||LA95_0==97||LA95_0==99||LA95_0==115) ) {
						alt95=1;
					}
					else if ( ((LA95_0 >= 76 && LA95_0 <= 77)||LA95_0==125) ) {
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
							// JPA2.g:335:48: datetime_expression
							{
							pushFollow(FOLLOW_datetime_expression_in_comparison_expression3087);
							datetime_expression354=datetime_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression354.getTree());

							}
							break;
						case 2 :
							// JPA2.g:335:70: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3091);
							all_or_any_expression355=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression355.getTree());

							}
							break;

					}

					}
					break;
				case 5 :
					// JPA2.g:336:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_expression_in_comparison_expression3100);
					entity_expression356=entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression356.getTree());

					set357=input.LT(1);
					if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set357));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:336:38: ( entity_expression | all_or_any_expression )
					int alt96=2;
					int LA96_0 = input.LA(1);
					if ( (LA96_0==NAMED_PARAMETER||LA96_0==WORD||LA96_0==55||LA96_0==69) ) {
						alt96=1;
					}
					else if ( ((LA96_0 >= 76 && LA96_0 <= 77)||LA96_0==125) ) {
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
							// JPA2.g:336:39: entity_expression
							{
							pushFollow(FOLLOW_entity_expression_in_comparison_expression3111);
							entity_expression358=entity_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression358.getTree());

							}
							break;
						case 2 :
							// JPA2.g:336:59: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3115);
							all_or_any_expression359=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression359.getTree());

							}
							break;

					}

					}
					break;
				case 6 :
					// JPA2.g:337:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3124);
					entity_type_expression360=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression360.getTree());

					set361=input.LT(1);
					if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
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
					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3134);
					entity_type_expression362=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression362.getTree());

					}
					break;
				case 7 :
					// JPA2.g:338:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3142);
					arithmetic_expression363=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression363.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3144);
					comparison_operator364=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator364.getTree());

					// JPA2.g:338:49: ( arithmetic_expression | all_or_any_expression )
					int alt97=2;
					int LA97_0 = input.LA(1);
					if ( (LA97_0==AVG||LA97_0==COUNT||LA97_0==INT_NUMERAL||(LA97_0 >= LPAREN && LA97_0 <= NAMED_PARAMETER)||LA97_0==SUM||LA97_0==WORD||LA97_0==55||LA97_0==57||LA97_0==59||LA97_0==62||LA97_0==69||LA97_0==75||(LA97_0 >= 81 && LA97_0 <= 83)||LA97_0==97||LA97_0==99||LA97_0==101||LA97_0==105||LA97_0==107||LA97_0==110||LA97_0==115||LA97_0==124||LA97_0==126) ) {
						alt97=1;
					}
					else if ( ((LA97_0 >= 76 && LA97_0 <= 77)||LA97_0==125) ) {
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
							// JPA2.g:338:50: arithmetic_expression
							{
							pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3147);
							arithmetic_expression365=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression365.getTree());

							}
							break;
						case 2 :
							// JPA2.g:338:74: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3151);
							all_or_any_expression366=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression366.getTree());

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
	// JPA2.g:340:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
	public final JPA2Parser.comparison_operator_return comparison_operator() throws RecognitionException {
		JPA2Parser.comparison_operator_return retval = new JPA2Parser.comparison_operator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set367=null;

		Object set367_tree=null;

		try {
			// JPA2.g:341:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set367=input.LT(1);
			if ( (input.LA(1) >= 63 && input.LA(1) <= 68) ) {
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
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:347:1: arithmetic_expression : ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term );
	public final JPA2Parser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
		JPA2Parser.arithmetic_expression_return retval = new JPA2Parser.arithmetic_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set370=null;
		ParserRuleReturnScope arithmetic_term368 =null;
		ParserRuleReturnScope arithmetic_term369 =null;
		ParserRuleReturnScope arithmetic_term371 =null;

		Object set370_tree=null;

		try {
			// JPA2.g:348:5: ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term )
			int alt99=2;
			switch ( input.LA(1) ) {
			case 57:
			case 59:
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
			case 62:
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
			case 69:
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
			case 55:
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
			case 105:
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
			case 107:
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
			case 75:
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
			case 126:
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
			case 110:
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
			case 124:
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
			case 101:
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
			case 99:
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
			case 81:
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
			case 83:
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
			case 115:
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
			case 82:
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
			case 97:
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
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 99, 0, input);
				throw nvae;
			}
			switch (alt99) {
				case 1 :
					// JPA2.g:348:7: arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3215);
					arithmetic_term368=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term368.getTree());

					}
					break;
				case 2 :
					// JPA2.g:349:7: arithmetic_term ( '+' | '-' ) arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3223);
					arithmetic_term369=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term369.getTree());

					set370=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set370));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3233);
					arithmetic_term371=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term371.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:350:1: arithmetic_term : ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor );
	public final JPA2Parser.arithmetic_term_return arithmetic_term() throws RecognitionException {
		JPA2Parser.arithmetic_term_return retval = new JPA2Parser.arithmetic_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set374=null;
		ParserRuleReturnScope arithmetic_factor372 =null;
		ParserRuleReturnScope arithmetic_factor373 =null;
		ParserRuleReturnScope arithmetic_factor375 =null;

		Object set374_tree=null;

		try {
			// JPA2.g:351:5: ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor )
			int alt100=2;
			switch ( input.LA(1) ) {
			case 57:
			case 59:
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
			case 62:
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
			case 69:
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
			case 55:
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
			case 105:
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
			case 107:
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
			case 75:
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
			case 126:
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
			case 110:
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
			case 124:
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
			case 101:
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
			case 99:
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
			case 81:
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
			case 83:
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
			case 115:
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
			case 82:
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
			case 97:
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
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 100, 0, input);
				throw nvae;
			}
			switch (alt100) {
				case 1 :
					// JPA2.g:351:7: arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3244);
					arithmetic_factor372=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor372.getTree());

					}
					break;
				case 2 :
					// JPA2.g:352:7: arithmetic_factor ( '*' | '/' ) arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3252);
					arithmetic_factor373=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor373.getTree());

					set374=input.LT(1);
					if ( input.LA(1)==56||input.LA(1)==61 ) {
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
					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3263);
					arithmetic_factor375=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor375.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:353:1: arithmetic_factor : ( ( '+' | '-' ) )? arithmetic_primary ;
	public final JPA2Parser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
		JPA2Parser.arithmetic_factor_return retval = new JPA2Parser.arithmetic_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set376=null;
		ParserRuleReturnScope arithmetic_primary377 =null;

		Object set376_tree=null;

		try {
			// JPA2.g:354:5: ( ( ( '+' | '-' ) )? arithmetic_primary )
			// JPA2.g:354:7: ( ( '+' | '-' ) )? arithmetic_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:354:7: ( ( '+' | '-' ) )?
			int alt101=2;
			int LA101_0 = input.LA(1);
			if ( (LA101_0==57||LA101_0==59) ) {
				alt101=1;
			}
			switch (alt101) {
				case 1 :
					// JPA2.g:
					{
					set376=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set376));
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

			pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor3286);
			arithmetic_primary377=arithmetic_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary377.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:355:1: arithmetic_primary : ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
		JPA2Parser.arithmetic_primary_return retval = new JPA2Parser.arithmetic_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal380=null;
		Token char_literal382=null;
		ParserRuleReturnScope path_expression378 =null;
		ParserRuleReturnScope numeric_literal379 =null;
		ParserRuleReturnScope arithmetic_expression381 =null;
		ParserRuleReturnScope input_parameter383 =null;
		ParserRuleReturnScope functions_returning_numerics384 =null;
		ParserRuleReturnScope aggregate_expression385 =null;
		ParserRuleReturnScope case_expression386 =null;
		ParserRuleReturnScope function_invocation387 =null;
		ParserRuleReturnScope extension_functions388 =null;
		ParserRuleReturnScope subquery389 =null;

		Object char_literal380_tree=null;
		Object char_literal382_tree=null;

		try {
			// JPA2.g:356:5: ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
			int alt102=10;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt102=1;
				}
				break;
			case INT_NUMERAL:
			case 62:
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
			case 55:
			case 69:
				{
				alt102=4;
				}
				break;
			case 75:
			case 101:
			case 105:
			case 107:
			case 110:
			case 124:
			case 126:
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
			case 99:
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
			case 81:
			case 83:
			case 115:
				{
				alt102=7;
				}
				break;
			case 82:
			case 97:
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
					// JPA2.g:356:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_arithmetic_primary3297);
					path_expression378=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression378.getTree());

					}
					break;
				case 2 :
					// JPA2.g:357:7: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary3305);
					numeric_literal379=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal379.getTree());

					}
					break;
				case 3 :
					// JPA2.g:358:7: '(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal380=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary3313); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal380_tree = (Object)adaptor.create(char_literal380);
					adaptor.addChild(root_0, char_literal380_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_arithmetic_primary3314);
					arithmetic_expression381=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression381.getTree());

					char_literal382=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary3315); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal382_tree = (Object)adaptor.create(char_literal382);
					adaptor.addChild(root_0, char_literal382_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:359:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_arithmetic_primary3323);
					input_parameter383=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter383.getTree());

					}
					break;
				case 5 :
					// JPA2.g:360:7: functions_returning_numerics
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary3331);
					functions_returning_numerics384=functions_returning_numerics();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics384.getTree());

					}
					break;
				case 6 :
					// JPA2.g:361:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary3339);
					aggregate_expression385=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression385.getTree());

					}
					break;
				case 7 :
					// JPA2.g:362:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_arithmetic_primary3347);
					case_expression386=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression386.getTree());

					}
					break;
				case 8 :
					// JPA2.g:363:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_arithmetic_primary3355);
					function_invocation387=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation387.getTree());

					}
					break;
				case 9 :
					// JPA2.g:364:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_arithmetic_primary3363);
					extension_functions388=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions388.getTree());

					}
					break;
				case 10 :
					// JPA2.g:365:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_arithmetic_primary3371);
					subquery389=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery389.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:366:1: string_expression : ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.string_expression_return string_expression() throws RecognitionException {
		JPA2Parser.string_expression_return retval = new JPA2Parser.string_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression390 =null;
		ParserRuleReturnScope string_literal391 =null;
		ParserRuleReturnScope input_parameter392 =null;
		ParserRuleReturnScope functions_returning_strings393 =null;
		ParserRuleReturnScope aggregate_expression394 =null;
		ParserRuleReturnScope case_expression395 =null;
		ParserRuleReturnScope function_invocation396 =null;
		ParserRuleReturnScope extension_functions397 =null;
		ParserRuleReturnScope subquery398 =null;


		try {
			// JPA2.g:367:5: ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
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
			case 55:
			case 69:
				{
				alt103=3;
				}
				break;
			case LOWER:
			case 84:
			case 127:
			case 131:
			case 134:
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
			case 99:
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
			case 81:
			case 83:
			case 115:
				{
				alt103=6;
				}
				break;
			case 82:
			case 97:
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
					// JPA2.g:367:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_string_expression3382);
					path_expression390=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression390.getTree());

					}
					break;
				case 2 :
					// JPA2.g:368:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_string_expression3390);
					string_literal391=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal391.getTree());

					}
					break;
				case 3 :
					// JPA2.g:369:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_string_expression3398);
					input_parameter392=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter392.getTree());

					}
					break;
				case 4 :
					// JPA2.g:370:7: functions_returning_strings
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_strings_in_string_expression3406);
					functions_returning_strings393=functions_returning_strings();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings393.getTree());

					}
					break;
				case 5 :
					// JPA2.g:371:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_string_expression3414);
					aggregate_expression394=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression394.getTree());

					}
					break;
				case 6 :
					// JPA2.g:372:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_string_expression3422);
					case_expression395=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression395.getTree());

					}
					break;
				case 7 :
					// JPA2.g:373:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_string_expression3430);
					function_invocation396=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation396.getTree());

					}
					break;
				case 8 :
					// JPA2.g:374:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_string_expression3438);
					extension_functions397=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions397.getTree());

					}
					break;
				case 9 :
					// JPA2.g:375:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_string_expression3446);
					subquery398=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery398.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:376:1: datetime_expression : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery );
	public final JPA2Parser.datetime_expression_return datetime_expression() throws RecognitionException {
		JPA2Parser.datetime_expression_return retval = new JPA2Parser.datetime_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression399 =null;
		ParserRuleReturnScope input_parameter400 =null;
		ParserRuleReturnScope functions_returning_datetime401 =null;
		ParserRuleReturnScope aggregate_expression402 =null;
		ParserRuleReturnScope case_expression403 =null;
		ParserRuleReturnScope function_invocation404 =null;
		ParserRuleReturnScope extension_functions405 =null;
		ParserRuleReturnScope date_time_timestamp_literal406 =null;
		ParserRuleReturnScope subquery407 =null;


		try {
			// JPA2.g:377:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery )
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
			case 55:
			case 69:
				{
				alt104=2;
				}
				break;
			case 85:
			case 86:
			case 87:
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
			case 99:
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
			case 81:
			case 83:
			case 115:
				{
				alt104=5;
				}
				break;
			case 82:
			case 97:
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
					// JPA2.g:377:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_datetime_expression3457);
					path_expression399=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression399.getTree());

					}
					break;
				case 2 :
					// JPA2.g:378:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_datetime_expression3465);
					input_parameter400=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter400.getTree());

					}
					break;
				case 3 :
					// JPA2.g:379:7: functions_returning_datetime
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_datetime_in_datetime_expression3473);
					functions_returning_datetime401=functions_returning_datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime401.getTree());

					}
					break;
				case 4 :
					// JPA2.g:380:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_datetime_expression3481);
					aggregate_expression402=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression402.getTree());

					}
					break;
				case 5 :
					// JPA2.g:381:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_datetime_expression3489);
					case_expression403=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression403.getTree());

					}
					break;
				case 6 :
					// JPA2.g:382:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_datetime_expression3497);
					function_invocation404=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation404.getTree());

					}
					break;
				case 7 :
					// JPA2.g:383:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_datetime_expression3505);
					extension_functions405=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions405.getTree());

					}
					break;
				case 8 :
					// JPA2.g:384:7: date_time_timestamp_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_time_timestamp_literal_in_datetime_expression3513);
					date_time_timestamp_literal406=date_time_timestamp_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_time_timestamp_literal406.getTree());

					}
					break;
				case 9 :
					// JPA2.g:385:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_datetime_expression3521);
					subquery407=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery407.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:386:1: boolean_expression : ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery );
	public final JPA2Parser.boolean_expression_return boolean_expression() throws RecognitionException {
		JPA2Parser.boolean_expression_return retval = new JPA2Parser.boolean_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression408 =null;
		ParserRuleReturnScope boolean_literal409 =null;
		ParserRuleReturnScope input_parameter410 =null;
		ParserRuleReturnScope case_expression411 =null;
		ParserRuleReturnScope function_invocation412 =null;
		ParserRuleReturnScope extension_functions413 =null;
		ParserRuleReturnScope subquery414 =null;


		try {
			// JPA2.g:387:5: ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery )
			int alt105=7;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt105=1;
				}
				break;
			case 140:
			case 141:
				{
				alt105=2;
				}
				break;
			case NAMED_PARAMETER:
			case 55:
			case 69:
				{
				alt105=3;
				}
				break;
			case 81:
			case 83:
			case 115:
				{
				alt105=4;
				}
				break;
			case 99:
				{
				alt105=5;
				}
				break;
			case 82:
			case 97:
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
					// JPA2.g:387:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_boolean_expression3532);
					path_expression408=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression408.getTree());

					}
					break;
				case 2 :
					// JPA2.g:388:7: boolean_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_literal_in_boolean_expression3540);
					boolean_literal409=boolean_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal409.getTree());

					}
					break;
				case 3 :
					// JPA2.g:389:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_boolean_expression3548);
					input_parameter410=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter410.getTree());

					}
					break;
				case 4 :
					// JPA2.g:390:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_boolean_expression3556);
					case_expression411=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression411.getTree());

					}
					break;
				case 5 :
					// JPA2.g:391:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_boolean_expression3564);
					function_invocation412=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation412.getTree());

					}
					break;
				case 6 :
					// JPA2.g:392:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_boolean_expression3572);
					extension_functions413=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions413.getTree());

					}
					break;
				case 7 :
					// JPA2.g:393:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_boolean_expression3580);
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
	// $ANTLR end "boolean_expression"


	public static class enum_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enum_expression"
	// JPA2.g:394:1: enum_expression : ( path_expression | enum_literal | input_parameter | case_expression | subquery );
	public final JPA2Parser.enum_expression_return enum_expression() throws RecognitionException {
		JPA2Parser.enum_expression_return retval = new JPA2Parser.enum_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression415 =null;
		ParserRuleReturnScope enum_literal416 =null;
		ParserRuleReturnScope input_parameter417 =null;
		ParserRuleReturnScope case_expression418 =null;
		ParserRuleReturnScope subquery419 =null;


		try {
			// JPA2.g:395:5: ( path_expression | enum_literal | input_parameter | case_expression | subquery )
			int alt106=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA106_1 = input.LA(2);
				if ( (LA106_1==60) ) {
					alt106=1;
				}
				else if ( (LA106_1==EOF||(LA106_1 >= AND && LA106_1 <= ASC)||LA106_1==DESC||(LA106_1 >= GROUP && LA106_1 <= HAVING)||LA106_1==INNER||(LA106_1 >= JOIN && LA106_1 <= LEFT)||(LA106_1 >= OR && LA106_1 <= ORDER)||LA106_1==RPAREN||LA106_1==WORD||LA106_1==58||(LA106_1 >= 65 && LA106_1 <= 66)||LA106_1==78||LA106_1==90||LA106_1==92||LA106_1==98||LA106_1==123||LA106_1==128||(LA106_1 >= 137 && LA106_1 <= 138)) ) {
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
			case 55:
			case 69:
				{
				alt106=3;
				}
				break;
			case 81:
			case 83:
			case 115:
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
					// JPA2.g:395:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_enum_expression3591);
					path_expression415=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression415.getTree());

					}
					break;
				case 2 :
					// JPA2.g:396:7: enum_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_literal_in_enum_expression3599);
					enum_literal416=enum_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal416.getTree());

					}
					break;
				case 3 :
					// JPA2.g:397:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_enum_expression3607);
					input_parameter417=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter417.getTree());

					}
					break;
				case 4 :
					// JPA2.g:398:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_enum_expression3615);
					case_expression418=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression418.getTree());

					}
					break;
				case 5 :
					// JPA2.g:399:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_enum_expression3623);
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
	// $ANTLR end "enum_expression"


	public static class entity_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_expression"
	// JPA2.g:400:1: entity_expression : ( path_expression | simple_entity_expression );
	public final JPA2Parser.entity_expression_return entity_expression() throws RecognitionException {
		JPA2Parser.entity_expression_return retval = new JPA2Parser.entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression420 =null;
		ParserRuleReturnScope simple_entity_expression421 =null;


		try {
			// JPA2.g:401:5: ( path_expression | simple_entity_expression )
			int alt107=2;
			int LA107_0 = input.LA(1);
			if ( (LA107_0==WORD) ) {
				int LA107_1 = input.LA(2);
				if ( (LA107_1==60) ) {
					alt107=1;
				}
				else if ( (LA107_1==EOF||LA107_1==AND||(LA107_1 >= GROUP && LA107_1 <= HAVING)||LA107_1==INNER||(LA107_1 >= JOIN && LA107_1 <= LEFT)||(LA107_1 >= OR && LA107_1 <= ORDER)||LA107_1==RPAREN||LA107_1==58||(LA107_1 >= 65 && LA107_1 <= 66)||LA107_1==123||LA107_1==128||LA107_1==138) ) {
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
			else if ( (LA107_0==NAMED_PARAMETER||LA107_0==55||LA107_0==69) ) {
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
					// JPA2.g:401:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_expression3634);
					path_expression420=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression420.getTree());

					}
					break;
				case 2 :
					// JPA2.g:402:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3642);
					simple_entity_expression421=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression421.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:403:1: simple_entity_expression : ( identification_variable | input_parameter );
	public final JPA2Parser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
		JPA2Parser.simple_entity_expression_return retval = new JPA2Parser.simple_entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable422 =null;
		ParserRuleReturnScope input_parameter423 =null;


		try {
			// JPA2.g:404:5: ( identification_variable | input_parameter )
			int alt108=2;
			int LA108_0 = input.LA(1);
			if ( (LA108_0==WORD) ) {
				alt108=1;
			}
			else if ( (LA108_0==NAMED_PARAMETER||LA108_0==55||LA108_0==69) ) {
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
					// JPA2.g:404:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3653);
					identification_variable422=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable422.getTree());

					}
					break;
				case 2 :
					// JPA2.g:405:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3661);
					input_parameter423=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter423.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:406:1: entity_type_expression : ( type_discriminator | entity_type_literal | input_parameter );
	public final JPA2Parser.entity_type_expression_return entity_type_expression() throws RecognitionException {
		JPA2Parser.entity_type_expression_return retval = new JPA2Parser.entity_type_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope type_discriminator424 =null;
		ParserRuleReturnScope entity_type_literal425 =null;
		ParserRuleReturnScope input_parameter426 =null;


		try {
			// JPA2.g:407:5: ( type_discriminator | entity_type_literal | input_parameter )
			int alt109=3;
			switch ( input.LA(1) ) {
			case 132:
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
			case 55:
			case 69:
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
					// JPA2.g:407:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_entity_type_expression3672);
					type_discriminator424=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator424.getTree());

					}
					break;
				case 2 :
					// JPA2.g:408:7: entity_type_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_literal_in_entity_type_expression3680);
					entity_type_literal425=entity_type_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_literal425.getTree());

					}
					break;
				case 3 :
					// JPA2.g:409:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_entity_type_expression3688);
					input_parameter426=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter426.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:410:1: type_discriminator : 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' ;
	public final JPA2Parser.type_discriminator_return type_discriminator() throws RecognitionException {
		JPA2Parser.type_discriminator_return retval = new JPA2Parser.type_discriminator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal427=null;
		Token char_literal431=null;
		ParserRuleReturnScope general_identification_variable428 =null;
		ParserRuleReturnScope path_expression429 =null;
		ParserRuleReturnScope input_parameter430 =null;

		Object string_literal427_tree=null;
		Object char_literal431_tree=null;

		try {
			// JPA2.g:411:5: ( 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' )
			// JPA2.g:411:7: 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal427=(Token)match(input,132,FOLLOW_132_in_type_discriminator3699); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal427_tree = (Object)adaptor.create(string_literal427);
			adaptor.addChild(root_0, string_literal427_tree);
			}

			// JPA2.g:411:15: ( general_identification_variable | path_expression | input_parameter )
			int alt110=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA110_1 = input.LA(2);
				if ( (LA110_1==RPAREN) ) {
					alt110=1;
				}
				else if ( (LA110_1==60) ) {
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
			case 103:
			case 135:
				{
				alt110=1;
				}
				break;
			case NAMED_PARAMETER:
			case 55:
			case 69:
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
					// JPA2.g:411:16: general_identification_variable
					{
					pushFollow(FOLLOW_general_identification_variable_in_type_discriminator3702);
					general_identification_variable428=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable428.getTree());

					}
					break;
				case 2 :
					// JPA2.g:411:50: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_type_discriminator3706);
					path_expression429=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression429.getTree());

					}
					break;
				case 3 :
					// JPA2.g:411:68: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_type_discriminator3710);
					input_parameter430=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter430.getTree());

					}
					break;

			}

			char_literal431=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_type_discriminator3713); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal431_tree = (Object)adaptor.create(char_literal431);
			adaptor.addChild(root_0, char_literal431_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:412:1: functions_returning_numerics : ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' );
	public final JPA2Parser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
		JPA2Parser.functions_returning_numerics_return retval = new JPA2Parser.functions_returning_numerics_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal432=null;
		Token char_literal434=null;
		Token string_literal435=null;
		Token char_literal437=null;
		Token char_literal439=null;
		Token char_literal441=null;
		Token string_literal442=null;
		Token char_literal444=null;
		Token string_literal445=null;
		Token char_literal447=null;
		Token string_literal448=null;
		Token char_literal450=null;
		Token char_literal452=null;
		Token string_literal453=null;
		Token char_literal455=null;
		Token string_literal456=null;
		Token char_literal458=null;
		ParserRuleReturnScope string_expression433 =null;
		ParserRuleReturnScope string_expression436 =null;
		ParserRuleReturnScope string_expression438 =null;
		ParserRuleReturnScope arithmetic_expression440 =null;
		ParserRuleReturnScope arithmetic_expression443 =null;
		ParserRuleReturnScope arithmetic_expression446 =null;
		ParserRuleReturnScope arithmetic_expression449 =null;
		ParserRuleReturnScope arithmetic_expression451 =null;
		ParserRuleReturnScope path_expression454 =null;
		ParserRuleReturnScope identification_variable457 =null;

		Object string_literal432_tree=null;
		Object char_literal434_tree=null;
		Object string_literal435_tree=null;
		Object char_literal437_tree=null;
		Object char_literal439_tree=null;
		Object char_literal441_tree=null;
		Object string_literal442_tree=null;
		Object char_literal444_tree=null;
		Object string_literal445_tree=null;
		Object char_literal447_tree=null;
		Object string_literal448_tree=null;
		Object char_literal450_tree=null;
		Object char_literal452_tree=null;
		Object string_literal453_tree=null;
		Object char_literal455_tree=null;
		Object string_literal456_tree=null;
		Object char_literal458_tree=null;

		try {
			// JPA2.g:413:5: ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' )
			int alt112=7;
			switch ( input.LA(1) ) {
			case 105:
				{
				alt112=1;
				}
				break;
			case 107:
				{
				alt112=2;
				}
				break;
			case 75:
				{
				alt112=3;
				}
				break;
			case 126:
				{
				alt112=4;
				}
				break;
			case 110:
				{
				alt112=5;
				}
				break;
			case 124:
				{
				alt112=6;
				}
				break;
			case 101:
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
					// JPA2.g:413:7: 'LENGTH(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal432=(Token)match(input,105,FOLLOW_105_in_functions_returning_numerics3724); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal432_tree = (Object)adaptor.create(string_literal432);
					adaptor.addChild(root_0, string_literal432_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3725);
					string_expression433=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression433.getTree());

					char_literal434=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3726); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal434_tree = (Object)adaptor.create(char_literal434);
					adaptor.addChild(root_0, char_literal434_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:414:7: 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal435=(Token)match(input,107,FOLLOW_107_in_functions_returning_numerics3734); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal435_tree = (Object)adaptor.create(string_literal435);
					adaptor.addChild(root_0, string_literal435_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3736);
					string_expression436=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression436.getTree());

					char_literal437=(Token)match(input,58,FOLLOW_58_in_functions_returning_numerics3737); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal437_tree = (Object)adaptor.create(char_literal437);
					adaptor.addChild(root_0, char_literal437_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3739);
					string_expression438=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression438.getTree());

					// JPA2.g:414:55: ( ',' arithmetic_expression )?
					int alt111=2;
					int LA111_0 = input.LA(1);
					if ( (LA111_0==58) ) {
						alt111=1;
					}
					switch (alt111) {
						case 1 :
							// JPA2.g:414:56: ',' arithmetic_expression
							{
							char_literal439=(Token)match(input,58,FOLLOW_58_in_functions_returning_numerics3741); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal439_tree = (Object)adaptor.create(char_literal439);
							adaptor.addChild(root_0, char_literal439_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3742);
							arithmetic_expression440=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression440.getTree());

							}
							break;

					}

					char_literal441=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3745); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal441_tree = (Object)adaptor.create(char_literal441);
					adaptor.addChild(root_0, char_literal441_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:415:7: 'ABS(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal442=(Token)match(input,75,FOLLOW_75_in_functions_returning_numerics3753); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal442_tree = (Object)adaptor.create(string_literal442);
					adaptor.addChild(root_0, string_literal442_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3754);
					arithmetic_expression443=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression443.getTree());

					char_literal444=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3755); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal444_tree = (Object)adaptor.create(char_literal444);
					adaptor.addChild(root_0, char_literal444_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:416:7: 'SQRT(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal445=(Token)match(input,126,FOLLOW_126_in_functions_returning_numerics3763); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal445_tree = (Object)adaptor.create(string_literal445);
					adaptor.addChild(root_0, string_literal445_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3764);
					arithmetic_expression446=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression446.getTree());

					char_literal447=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3765); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal447_tree = (Object)adaptor.create(char_literal447);
					adaptor.addChild(root_0, char_literal447_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:417:7: 'MOD(' arithmetic_expression ',' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal448=(Token)match(input,110,FOLLOW_110_in_functions_returning_numerics3773); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal448_tree = (Object)adaptor.create(string_literal448);
					adaptor.addChild(root_0, string_literal448_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3774);
					arithmetic_expression449=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression449.getTree());

					char_literal450=(Token)match(input,58,FOLLOW_58_in_functions_returning_numerics3775); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal450_tree = (Object)adaptor.create(char_literal450);
					adaptor.addChild(root_0, char_literal450_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3777);
					arithmetic_expression451=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression451.getTree());

					char_literal452=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3778); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal452_tree = (Object)adaptor.create(char_literal452);
					adaptor.addChild(root_0, char_literal452_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:418:7: 'SIZE(' path_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal453=(Token)match(input,124,FOLLOW_124_in_functions_returning_numerics3786); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal453_tree = (Object)adaptor.create(string_literal453);
					adaptor.addChild(root_0, string_literal453_tree);
					}

					pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3787);
					path_expression454=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression454.getTree());

					char_literal455=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3788); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal455_tree = (Object)adaptor.create(char_literal455);
					adaptor.addChild(root_0, char_literal455_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:419:7: 'INDEX(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal456=(Token)match(input,101,FOLLOW_101_in_functions_returning_numerics3796); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal456_tree = (Object)adaptor.create(string_literal456);
					adaptor.addChild(root_0, string_literal456_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_functions_returning_numerics3797);
					identification_variable457=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable457.getTree());

					char_literal458=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3798); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal458_tree = (Object)adaptor.create(char_literal458);
					adaptor.addChild(root_0, char_literal458_tree);
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
	// JPA2.g:420:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
	public final JPA2Parser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
		JPA2Parser.functions_returning_datetime_return retval = new JPA2Parser.functions_returning_datetime_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set459=null;

		Object set459_tree=null;

		try {
			// JPA2.g:421:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set459=input.LT(1);
			if ( (input.LA(1) >= 85 && input.LA(1) <= 87) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set459));
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
	// JPA2.g:424:1: functions_returning_strings : ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' );
	public final JPA2Parser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
		JPA2Parser.functions_returning_strings_return retval = new JPA2Parser.functions_returning_strings_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal460=null;
		Token char_literal462=null;
		Token char_literal464=null;
		Token char_literal466=null;
		Token string_literal467=null;
		Token char_literal469=null;
		Token char_literal471=null;
		Token char_literal473=null;
		Token string_literal474=null;
		Token string_literal477=null;
		Token char_literal479=null;
		Token string_literal480=null;
		Token char_literal481=null;
		Token char_literal483=null;
		Token string_literal484=null;
		Token char_literal486=null;
		ParserRuleReturnScope string_expression461 =null;
		ParserRuleReturnScope string_expression463 =null;
		ParserRuleReturnScope string_expression465 =null;
		ParserRuleReturnScope string_expression468 =null;
		ParserRuleReturnScope arithmetic_expression470 =null;
		ParserRuleReturnScope arithmetic_expression472 =null;
		ParserRuleReturnScope trim_specification475 =null;
		ParserRuleReturnScope trim_character476 =null;
		ParserRuleReturnScope string_expression478 =null;
		ParserRuleReturnScope string_expression482 =null;
		ParserRuleReturnScope string_expression485 =null;

		Object string_literal460_tree=null;
		Object char_literal462_tree=null;
		Object char_literal464_tree=null;
		Object char_literal466_tree=null;
		Object string_literal467_tree=null;
		Object char_literal469_tree=null;
		Object char_literal471_tree=null;
		Object char_literal473_tree=null;
		Object string_literal474_tree=null;
		Object string_literal477_tree=null;
		Object char_literal479_tree=null;
		Object string_literal480_tree=null;
		Object char_literal481_tree=null;
		Object char_literal483_tree=null;
		Object string_literal484_tree=null;
		Object char_literal486_tree=null;

		try {
			// JPA2.g:425:5: ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' )
			int alt118=5;
			switch ( input.LA(1) ) {
			case 84:
				{
				alt118=1;
				}
				break;
			case 127:
				{
				alt118=2;
				}
				break;
			case 131:
				{
				alt118=3;
				}
				break;
			case LOWER:
				{
				alt118=4;
				}
				break;
			case 134:
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
					// JPA2.g:425:7: 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal460=(Token)match(input,84,FOLLOW_84_in_functions_returning_strings3836); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal460_tree = (Object)adaptor.create(string_literal460);
					adaptor.addChild(root_0, string_literal460_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3837);
					string_expression461=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression461.getTree());

					char_literal462=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3838); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal462_tree = (Object)adaptor.create(char_literal462);
					adaptor.addChild(root_0, char_literal462_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3840);
					string_expression463=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression463.getTree());

					// JPA2.g:425:55: ( ',' string_expression )*
					loop113:
					while (true) {
						int alt113=2;
						int LA113_0 = input.LA(1);
						if ( (LA113_0==58) ) {
							alt113=1;
						}

						switch (alt113) {
						case 1 :
							// JPA2.g:425:56: ',' string_expression
							{
							char_literal464=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3843); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal464_tree = (Object)adaptor.create(char_literal464);
							adaptor.addChild(root_0, char_literal464_tree);
							}

							pushFollow(FOLLOW_string_expression_in_functions_returning_strings3845);
							string_expression465=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression465.getTree());

							}
							break;

						default :
							break loop113;
						}
					}

					char_literal466=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3848); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal466_tree = (Object)adaptor.create(char_literal466);
					adaptor.addChild(root_0, char_literal466_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:426:7: 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal467=(Token)match(input,127,FOLLOW_127_in_functions_returning_strings3856); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal467_tree = (Object)adaptor.create(string_literal467);
					adaptor.addChild(root_0, string_literal467_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3858);
					string_expression468=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression468.getTree());

					char_literal469=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3859); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal469_tree = (Object)adaptor.create(char_literal469);
					adaptor.addChild(root_0, char_literal469_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3861);
					arithmetic_expression470=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression470.getTree());

					// JPA2.g:426:63: ( ',' arithmetic_expression )?
					int alt114=2;
					int LA114_0 = input.LA(1);
					if ( (LA114_0==58) ) {
						alt114=1;
					}
					switch (alt114) {
						case 1 :
							// JPA2.g:426:64: ',' arithmetic_expression
							{
							char_literal471=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3864); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal471_tree = (Object)adaptor.create(char_literal471);
							adaptor.addChild(root_0, char_literal471_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3866);
							arithmetic_expression472=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression472.getTree());

							}
							break;

					}

					char_literal473=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3869); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal473_tree = (Object)adaptor.create(char_literal473);
					adaptor.addChild(root_0, char_literal473_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:427:7: 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal474=(Token)match(input,131,FOLLOW_131_in_functions_returning_strings3877); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal474_tree = (Object)adaptor.create(string_literal474);
					adaptor.addChild(root_0, string_literal474_tree);
					}

					// JPA2.g:427:14: ( ( trim_specification )? ( trim_character )? 'FROM' )?
					int alt117=2;
					int LA117_0 = input.LA(1);
					if ( (LA117_0==TRIM_CHARACTER||LA117_0==80||LA117_0==98||LA117_0==104||LA117_0==129) ) {
						alt117=1;
					}
					switch (alt117) {
						case 1 :
							// JPA2.g:427:15: ( trim_specification )? ( trim_character )? 'FROM'
							{
							// JPA2.g:427:15: ( trim_specification )?
							int alt115=2;
							int LA115_0 = input.LA(1);
							if ( (LA115_0==80||LA115_0==104||LA115_0==129) ) {
								alt115=1;
							}
							switch (alt115) {
								case 1 :
									// JPA2.g:427:16: trim_specification
									{
									pushFollow(FOLLOW_trim_specification_in_functions_returning_strings3880);
									trim_specification475=trim_specification();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification475.getTree());

									}
									break;

							}

							// JPA2.g:427:37: ( trim_character )?
							int alt116=2;
							int LA116_0 = input.LA(1);
							if ( (LA116_0==TRIM_CHARACTER) ) {
								alt116=1;
							}
							switch (alt116) {
								case 1 :
									// JPA2.g:427:38: trim_character
									{
									pushFollow(FOLLOW_trim_character_in_functions_returning_strings3885);
									trim_character476=trim_character();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_character476.getTree());

									}
									break;

							}

							string_literal477=(Token)match(input,98,FOLLOW_98_in_functions_returning_strings3889); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal477_tree = (Object)adaptor.create(string_literal477);
							adaptor.addChild(root_0, string_literal477_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3893);
					string_expression478=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression478.getTree());

					char_literal479=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3895); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal479_tree = (Object)adaptor.create(char_literal479);
					adaptor.addChild(root_0, char_literal479_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:428:7: 'LOWER' '(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal480=(Token)match(input,LOWER,FOLLOW_LOWER_in_functions_returning_strings3903); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal480_tree = (Object)adaptor.create(string_literal480);
					adaptor.addChild(root_0, string_literal480_tree);
					}

					char_literal481=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3905); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal481_tree = (Object)adaptor.create(char_literal481);
					adaptor.addChild(root_0, char_literal481_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3906);
					string_expression482=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression482.getTree());

					char_literal483=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3907); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal483_tree = (Object)adaptor.create(char_literal483);
					adaptor.addChild(root_0, char_literal483_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:429:7: 'UPPER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal484=(Token)match(input,134,FOLLOW_134_in_functions_returning_strings3915); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal484_tree = (Object)adaptor.create(string_literal484);
					adaptor.addChild(root_0, string_literal484_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3916);
					string_expression485=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression485.getTree());

					char_literal486=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3917); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal486_tree = (Object)adaptor.create(char_literal486);
					adaptor.addChild(root_0, char_literal486_tree);
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
	// JPA2.g:430:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
	public final JPA2Parser.trim_specification_return trim_specification() throws RecognitionException {
		JPA2Parser.trim_specification_return retval = new JPA2Parser.trim_specification_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set487=null;

		Object set487_tree=null;

		try {
			// JPA2.g:431:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set487=input.LT(1);
			if ( input.LA(1)==80||input.LA(1)==104||input.LA(1)==129 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set487));
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
	// JPA2.g:432:1: function_invocation : 'FUNCTION(' function_name ( ',' function_arg )* ')' ;
	public final JPA2Parser.function_invocation_return function_invocation() throws RecognitionException {
		JPA2Parser.function_invocation_return retval = new JPA2Parser.function_invocation_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal488=null;
		Token char_literal490=null;
		Token char_literal492=null;
		ParserRuleReturnScope function_name489 =null;
		ParserRuleReturnScope function_arg491 =null;

		Object string_literal488_tree=null;
		Object char_literal490_tree=null;
		Object char_literal492_tree=null;

		try {
			// JPA2.g:433:5: ( 'FUNCTION(' function_name ( ',' function_arg )* ')' )
			// JPA2.g:433:7: 'FUNCTION(' function_name ( ',' function_arg )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal488=(Token)match(input,99,FOLLOW_99_in_function_invocation3947); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal488_tree = (Object)adaptor.create(string_literal488);
			adaptor.addChild(root_0, string_literal488_tree);
			}

			pushFollow(FOLLOW_function_name_in_function_invocation3948);
			function_name489=function_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_name489.getTree());

			// JPA2.g:433:32: ( ',' function_arg )*
			loop119:
			while (true) {
				int alt119=2;
				int LA119_0 = input.LA(1);
				if ( (LA119_0==58) ) {
					alt119=1;
				}

				switch (alt119) {
				case 1 :
					// JPA2.g:433:33: ',' function_arg
					{
					char_literal490=(Token)match(input,58,FOLLOW_58_in_function_invocation3951); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal490_tree = (Object)adaptor.create(char_literal490);
					adaptor.addChild(root_0, char_literal490_tree);
					}

					pushFollow(FOLLOW_function_arg_in_function_invocation3953);
					function_arg491=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg491.getTree());

					}
					break;

				default :
					break loop119;
				}
			}

			char_literal492=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function_invocation3957); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal492_tree = (Object)adaptor.create(char_literal492);
			adaptor.addChild(root_0, char_literal492_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:434:1: function_arg : ( literal | path_expression | input_parameter | scalar_expression );
	public final JPA2Parser.function_arg_return function_arg() throws RecognitionException {
		JPA2Parser.function_arg_return retval = new JPA2Parser.function_arg_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal493 =null;
		ParserRuleReturnScope path_expression494 =null;
		ParserRuleReturnScope input_parameter495 =null;
		ParserRuleReturnScope scalar_expression496 =null;


		try {
			// JPA2.g:435:5: ( literal | path_expression | input_parameter | scalar_expression )
			int alt120=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA120_1 = input.LA(2);
				if ( (LA120_1==60) ) {
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
			case 69:
				{
				int LA120_2 = input.LA(2);
				if ( (LA120_2==62) ) {
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
			case 55:
				{
				int LA120_4 = input.LA(2);
				if ( (LA120_4==WORD) ) {
					int LA120_11 = input.LA(3);
					if ( (LA120_11==142) ) {
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
			case 57:
			case 59:
			case 62:
			case 75:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 97:
			case 99:
			case 101:
			case 105:
			case 107:
			case 110:
			case 115:
			case 124:
			case 126:
			case 127:
			case 131:
			case 132:
			case 134:
			case 140:
			case 141:
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
					// JPA2.g:435:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_function_arg3968);
					literal493=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal493.getTree());

					}
					break;
				case 2 :
					// JPA2.g:436:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_function_arg3976);
					path_expression494=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression494.getTree());

					}
					break;
				case 3 :
					// JPA2.g:437:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_function_arg3984);
					input_parameter495=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter495.getTree());

					}
					break;
				case 4 :
					// JPA2.g:438:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_function_arg3992);
					scalar_expression496=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression496.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:439:1: case_expression : ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression );
	public final JPA2Parser.case_expression_return case_expression() throws RecognitionException {
		JPA2Parser.case_expression_return retval = new JPA2Parser.case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope general_case_expression497 =null;
		ParserRuleReturnScope simple_case_expression498 =null;
		ParserRuleReturnScope coalesce_expression499 =null;
		ParserRuleReturnScope nullif_expression500 =null;


		try {
			// JPA2.g:440:5: ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression )
			int alt121=4;
			switch ( input.LA(1) ) {
			case 81:
				{
				int LA121_1 = input.LA(2);
				if ( (LA121_1==137) ) {
					alt121=1;
				}
				else if ( (LA121_1==WORD||LA121_1==132) ) {
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
			case 83:
				{
				alt121=3;
				}
				break;
			case 115:
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
					// JPA2.g:440:7: general_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_case_expression_in_case_expression4003);
					general_case_expression497=general_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_case_expression497.getTree());

					}
					break;
				case 2 :
					// JPA2.g:441:7: simple_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_case_expression_in_case_expression4011);
					simple_case_expression498=simple_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_case_expression498.getTree());

					}
					break;
				case 3 :
					// JPA2.g:442:7: coalesce_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_coalesce_expression_in_case_expression4019);
					coalesce_expression499=coalesce_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, coalesce_expression499.getTree());

					}
					break;
				case 4 :
					// JPA2.g:443:7: nullif_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_nullif_expression_in_case_expression4027);
					nullif_expression500=nullif_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, nullif_expression500.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:444:1: general_case_expression : 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.general_case_expression_return general_case_expression() throws RecognitionException {
		JPA2Parser.general_case_expression_return retval = new JPA2Parser.general_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal501=null;
		Token string_literal504=null;
		Token string_literal506=null;
		ParserRuleReturnScope when_clause502 =null;
		ParserRuleReturnScope when_clause503 =null;
		ParserRuleReturnScope scalar_expression505 =null;

		Object string_literal501_tree=null;
		Object string_literal504_tree=null;
		Object string_literal506_tree=null;

		try {
			// JPA2.g:445:5: ( 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:445:7: 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal501=(Token)match(input,81,FOLLOW_81_in_general_case_expression4038); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal501_tree = (Object)adaptor.create(string_literal501);
			adaptor.addChild(root_0, string_literal501_tree);
			}

			pushFollow(FOLLOW_when_clause_in_general_case_expression4040);
			when_clause502=when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause502.getTree());

			// JPA2.g:445:26: ( when_clause )*
			loop122:
			while (true) {
				int alt122=2;
				int LA122_0 = input.LA(1);
				if ( (LA122_0==137) ) {
					alt122=1;
				}

				switch (alt122) {
				case 1 :
					// JPA2.g:445:27: when_clause
					{
					pushFollow(FOLLOW_when_clause_in_general_case_expression4043);
					when_clause503=when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause503.getTree());

					}
					break;

				default :
					break loop122;
				}
			}

			string_literal504=(Token)match(input,90,FOLLOW_90_in_general_case_expression4047); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal504_tree = (Object)adaptor.create(string_literal504);
			adaptor.addChild(root_0, string_literal504_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_general_case_expression4049);
			scalar_expression505=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression505.getTree());

			string_literal506=(Token)match(input,92,FOLLOW_92_in_general_case_expression4051); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal506_tree = (Object)adaptor.create(string_literal506);
			adaptor.addChild(root_0, string_literal506_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:446:1: when_clause : 'WHEN' conditional_expression 'THEN' scalar_expression ;
	public final JPA2Parser.when_clause_return when_clause() throws RecognitionException {
		JPA2Parser.when_clause_return retval = new JPA2Parser.when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal507=null;
		Token string_literal509=null;
		ParserRuleReturnScope conditional_expression508 =null;
		ParserRuleReturnScope scalar_expression510 =null;

		Object string_literal507_tree=null;
		Object string_literal509_tree=null;

		try {
			// JPA2.g:447:5: ( 'WHEN' conditional_expression 'THEN' scalar_expression )
			// JPA2.g:447:7: 'WHEN' conditional_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal507=(Token)match(input,137,FOLLOW_137_in_when_clause4062); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal507_tree = (Object)adaptor.create(string_literal507);
			adaptor.addChild(root_0, string_literal507_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_when_clause4064);
			conditional_expression508=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression508.getTree());

			string_literal509=(Token)match(input,128,FOLLOW_128_in_when_clause4066); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal509_tree = (Object)adaptor.create(string_literal509);
			adaptor.addChild(root_0, string_literal509_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_when_clause4068);
			scalar_expression510=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression510.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:448:1: simple_case_expression : 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.simple_case_expression_return simple_case_expression() throws RecognitionException {
		JPA2Parser.simple_case_expression_return retval = new JPA2Parser.simple_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal511=null;
		Token string_literal515=null;
		Token string_literal517=null;
		ParserRuleReturnScope case_operand512 =null;
		ParserRuleReturnScope simple_when_clause513 =null;
		ParserRuleReturnScope simple_when_clause514 =null;
		ParserRuleReturnScope scalar_expression516 =null;

		Object string_literal511_tree=null;
		Object string_literal515_tree=null;
		Object string_literal517_tree=null;

		try {
			// JPA2.g:449:5: ( 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:449:7: 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal511=(Token)match(input,81,FOLLOW_81_in_simple_case_expression4079); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal511_tree = (Object)adaptor.create(string_literal511);
			adaptor.addChild(root_0, string_literal511_tree);
			}

			pushFollow(FOLLOW_case_operand_in_simple_case_expression4081);
			case_operand512=case_operand();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, case_operand512.getTree());

			pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4083);
			simple_when_clause513=simple_when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause513.getTree());

			// JPA2.g:449:46: ( simple_when_clause )*
			loop123:
			while (true) {
				int alt123=2;
				int LA123_0 = input.LA(1);
				if ( (LA123_0==137) ) {
					alt123=1;
				}

				switch (alt123) {
				case 1 :
					// JPA2.g:449:47: simple_when_clause
					{
					pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4086);
					simple_when_clause514=simple_when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause514.getTree());

					}
					break;

				default :
					break loop123;
				}
			}

			string_literal515=(Token)match(input,90,FOLLOW_90_in_simple_case_expression4090); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal515_tree = (Object)adaptor.create(string_literal515);
			adaptor.addChild(root_0, string_literal515_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_case_expression4092);
			scalar_expression516=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression516.getTree());

			string_literal517=(Token)match(input,92,FOLLOW_92_in_simple_case_expression4094); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal517_tree = (Object)adaptor.create(string_literal517);
			adaptor.addChild(root_0, string_literal517_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:450:1: case_operand : ( path_expression | type_discriminator );
	public final JPA2Parser.case_operand_return case_operand() throws RecognitionException {
		JPA2Parser.case_operand_return retval = new JPA2Parser.case_operand_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression518 =null;
		ParserRuleReturnScope type_discriminator519 =null;


		try {
			// JPA2.g:451:5: ( path_expression | type_discriminator )
			int alt124=2;
			int LA124_0 = input.LA(1);
			if ( (LA124_0==WORD) ) {
				alt124=1;
			}
			else if ( (LA124_0==132) ) {
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
					// JPA2.g:451:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_case_operand4105);
					path_expression518=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression518.getTree());

					}
					break;
				case 2 :
					// JPA2.g:452:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_case_operand4113);
					type_discriminator519=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator519.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:453:1: simple_when_clause : 'WHEN' scalar_expression 'THEN' scalar_expression ;
	public final JPA2Parser.simple_when_clause_return simple_when_clause() throws RecognitionException {
		JPA2Parser.simple_when_clause_return retval = new JPA2Parser.simple_when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal520=null;
		Token string_literal522=null;
		ParserRuleReturnScope scalar_expression521 =null;
		ParserRuleReturnScope scalar_expression523 =null;

		Object string_literal520_tree=null;
		Object string_literal522_tree=null;

		try {
			// JPA2.g:454:5: ( 'WHEN' scalar_expression 'THEN' scalar_expression )
			// JPA2.g:454:7: 'WHEN' scalar_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal520=(Token)match(input,137,FOLLOW_137_in_simple_when_clause4124); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal520_tree = (Object)adaptor.create(string_literal520);
			adaptor.addChild(root_0, string_literal520_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4126);
			scalar_expression521=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression521.getTree());

			string_literal522=(Token)match(input,128,FOLLOW_128_in_simple_when_clause4128); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal522_tree = (Object)adaptor.create(string_literal522);
			adaptor.addChild(root_0, string_literal522_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4130);
			scalar_expression523=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression523.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:455:1: coalesce_expression : 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' ;
	public final JPA2Parser.coalesce_expression_return coalesce_expression() throws RecognitionException {
		JPA2Parser.coalesce_expression_return retval = new JPA2Parser.coalesce_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal524=null;
		Token char_literal526=null;
		Token char_literal528=null;
		ParserRuleReturnScope scalar_expression525 =null;
		ParserRuleReturnScope scalar_expression527 =null;

		Object string_literal524_tree=null;
		Object char_literal526_tree=null;
		Object char_literal528_tree=null;

		try {
			// JPA2.g:456:5: ( 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' )
			// JPA2.g:456:7: 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal524=(Token)match(input,83,FOLLOW_83_in_coalesce_expression4141); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal524_tree = (Object)adaptor.create(string_literal524);
			adaptor.addChild(root_0, string_literal524_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4142);
			scalar_expression525=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression525.getTree());

			// JPA2.g:456:36: ( ',' scalar_expression )+
			int cnt125=0;
			loop125:
			while (true) {
				int alt125=2;
				int LA125_0 = input.LA(1);
				if ( (LA125_0==58) ) {
					alt125=1;
				}

				switch (alt125) {
				case 1 :
					// JPA2.g:456:37: ',' scalar_expression
					{
					char_literal526=(Token)match(input,58,FOLLOW_58_in_coalesce_expression4145); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal526_tree = (Object)adaptor.create(char_literal526);
					adaptor.addChild(root_0, char_literal526_tree);
					}

					pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4147);
					scalar_expression527=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression527.getTree());

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

			char_literal528=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_coalesce_expression4150); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal528_tree = (Object)adaptor.create(char_literal528);
			adaptor.addChild(root_0, char_literal528_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:457:1: nullif_expression : 'NULLIF(' scalar_expression ',' scalar_expression ')' ;
	public final JPA2Parser.nullif_expression_return nullif_expression() throws RecognitionException {
		JPA2Parser.nullif_expression_return retval = new JPA2Parser.nullif_expression_return();
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
			// JPA2.g:458:5: ( 'NULLIF(' scalar_expression ',' scalar_expression ')' )
			// JPA2.g:458:7: 'NULLIF(' scalar_expression ',' scalar_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal529=(Token)match(input,115,FOLLOW_115_in_nullif_expression4161); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal529_tree = (Object)adaptor.create(string_literal529);
			adaptor.addChild(root_0, string_literal529_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4162);
			scalar_expression530=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression530.getTree());

			char_literal531=(Token)match(input,58,FOLLOW_58_in_nullif_expression4164); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal531_tree = (Object)adaptor.create(char_literal531);
			adaptor.addChild(root_0, char_literal531_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4166);
			scalar_expression532=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression532.getTree());

			char_literal533=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nullif_expression4167); if (state.failed) return retval;
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
	// $ANTLR end "nullif_expression"


	public static class extension_functions_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "extension_functions"
	// JPA2.g:460:1: extension_functions : ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | 'EXTRACT(' date_part 'FROM' function_arg ')' );
	public final JPA2Parser.extension_functions_return extension_functions() throws RecognitionException {
		JPA2Parser.extension_functions_return retval = new JPA2Parser.extension_functions_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal534=null;
		Token WORD536=null;
		Token char_literal537=null;
		Token INT_NUMERAL538=null;
		Token char_literal539=null;
		Token INT_NUMERAL540=null;
		Token char_literal541=null;
		Token char_literal542=null;
		Token string_literal543=null;
		Token string_literal545=null;
		Token char_literal547=null;
		ParserRuleReturnScope function_arg535 =null;
		ParserRuleReturnScope date_part544 =null;
		ParserRuleReturnScope function_arg546 =null;

		Object string_literal534_tree=null;
		Object WORD536_tree=null;
		Object char_literal537_tree=null;
		Object INT_NUMERAL538_tree=null;
		Object char_literal539_tree=null;
		Object INT_NUMERAL540_tree=null;
		Object char_literal541_tree=null;
		Object char_literal542_tree=null;
		Object string_literal543_tree=null;
		Object string_literal545_tree=null;
		Object char_literal547_tree=null;

		try {
			// JPA2.g:461:5: ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | 'EXTRACT(' date_part 'FROM' function_arg ')' )
			int alt128=2;
			int LA128_0 = input.LA(1);
			if ( (LA128_0==82) ) {
				alt128=1;
			}
			else if ( (LA128_0==97) ) {
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
					// JPA2.g:461:7: 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal534=(Token)match(input,82,FOLLOW_82_in_extension_functions4179); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal534_tree = (Object)adaptor.create(string_literal534);
					adaptor.addChild(root_0, string_literal534_tree);
					}

					pushFollow(FOLLOW_function_arg_in_extension_functions4181);
					function_arg535=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg535.getTree());

					WORD536=(Token)match(input,WORD,FOLLOW_WORD_in_extension_functions4183); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD536_tree = (Object)adaptor.create(WORD536);
					adaptor.addChild(root_0, WORD536_tree);
					}

					// JPA2.g:461:33: ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )*
					loop127:
					while (true) {
						int alt127=2;
						int LA127_0 = input.LA(1);
						if ( (LA127_0==LPAREN) ) {
							alt127=1;
						}

						switch (alt127) {
						case 1 :
							// JPA2.g:461:34: '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')'
							{
							char_literal537=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_extension_functions4186); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal537_tree = (Object)adaptor.create(char_literal537);
							adaptor.addChild(root_0, char_literal537_tree);
							}

							INT_NUMERAL538=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4187); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							INT_NUMERAL538_tree = (Object)adaptor.create(INT_NUMERAL538);
							adaptor.addChild(root_0, INT_NUMERAL538_tree);
							}

							// JPA2.g:461:49: ( ',' INT_NUMERAL )*
							loop126:
							while (true) {
								int alt126=2;
								int LA126_0 = input.LA(1);
								if ( (LA126_0==58) ) {
									alt126=1;
								}

								switch (alt126) {
								case 1 :
									// JPA2.g:461:50: ',' INT_NUMERAL
									{
									char_literal539=(Token)match(input,58,FOLLOW_58_in_extension_functions4190); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									char_literal539_tree = (Object)adaptor.create(char_literal539);
									adaptor.addChild(root_0, char_literal539_tree);
									}

									INT_NUMERAL540=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4192); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									INT_NUMERAL540_tree = (Object)adaptor.create(INT_NUMERAL540);
									adaptor.addChild(root_0, INT_NUMERAL540_tree);
									}

									}
									break;

								default :
									break loop126;
								}
							}

							char_literal541=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4197); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal541_tree = (Object)adaptor.create(char_literal541);
							adaptor.addChild(root_0, char_literal541_tree);
							}

							}
							break;

						default :
							break loop127;
						}
					}

					char_literal542=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4201); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal542_tree = (Object)adaptor.create(char_literal542);
					adaptor.addChild(root_0, char_literal542_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:462:7: 'EXTRACT(' date_part 'FROM' function_arg ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal543=(Token)match(input,97,FOLLOW_97_in_extension_functions4209); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal543_tree = (Object)adaptor.create(string_literal543);
					adaptor.addChild(root_0, string_literal543_tree);
					}

					pushFollow(FOLLOW_date_part_in_extension_functions4211);
					date_part544=date_part();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part544.getTree());

					string_literal545=(Token)match(input,98,FOLLOW_98_in_extension_functions4213); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal545_tree = (Object)adaptor.create(string_literal545);
					adaptor.addChild(root_0, string_literal545_tree);
					}

					pushFollow(FOLLOW_function_arg_in_extension_functions4215);
					function_arg546=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg546.getTree());

					char_literal547=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4217); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal547_tree = (Object)adaptor.create(char_literal547);
					adaptor.addChild(root_0, char_literal547_tree);
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
	// JPA2.g:464:10: fragment date_part : ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' );
	public final JPA2Parser.date_part_return date_part() throws RecognitionException {
		JPA2Parser.date_part_return retval = new JPA2Parser.date_part_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set548=null;

		Object set548_tree=null;

		try {
			// JPA2.g:465:5: ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set548=input.LT(1);
			if ( input.LA(1)==88||input.LA(1)==94||input.LA(1)==100||input.LA(1)==109||input.LA(1)==111||input.LA(1)==119||input.LA(1)==121||input.LA(1)==136||input.LA(1)==139 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set548));
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
	// JPA2.g:468:1: input_parameter : ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) );
	public final JPA2Parser.input_parameter_return input_parameter() throws RecognitionException {
		JPA2Parser.input_parameter_return retval = new JPA2Parser.input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal549=null;
		Token NAMED_PARAMETER551=null;
		Token string_literal552=null;
		Token WORD553=null;
		Token char_literal554=null;
		ParserRuleReturnScope numeric_literal550 =null;

		Object char_literal549_tree=null;
		Object NAMED_PARAMETER551_tree=null;
		Object string_literal552_tree=null;
		Object WORD553_tree=null;
		Object char_literal554_tree=null;
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
		RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
		RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
		RewriteRuleTokenStream stream_142=new RewriteRuleTokenStream(adaptor,"token 142");
		RewriteRuleSubtreeStream stream_numeric_literal=new RewriteRuleSubtreeStream(adaptor,"rule numeric_literal");

		try {
			// JPA2.g:469:5: ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
			int alt129=3;
			switch ( input.LA(1) ) {
			case 69:
				{
				alt129=1;
				}
				break;
			case NAMED_PARAMETER:
				{
				alt129=2;
				}
				break;
			case 55:
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
					// JPA2.g:469:7: '?' numeric_literal
					{
					char_literal549=(Token)match(input,69,FOLLOW_69_in_input_parameter4274); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_69.add(char_literal549);

					pushFollow(FOLLOW_numeric_literal_in_input_parameter4276);
					numeric_literal550=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_literal.add(numeric_literal550.getTree());
					// AST REWRITE
					// elements: numeric_literal, 69
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 469:27: -> ^( T_PARAMETER[] '?' numeric_literal )
					{
						// JPA2.g:469:30: ^( T_PARAMETER[] '?' numeric_literal )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_69.nextNode());
						adaptor.addChild(root_1, stream_numeric_literal.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// JPA2.g:470:7: NAMED_PARAMETER
					{
					NAMED_PARAMETER551=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter4299); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER551);

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
					// 470:23: -> ^( T_PARAMETER[] NAMED_PARAMETER )
					{
						// JPA2.g:470:26: ^( T_PARAMETER[] NAMED_PARAMETER )
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
					// JPA2.g:471:7: '${' WORD '}'
					{
					string_literal552=(Token)match(input,55,FOLLOW_55_in_input_parameter4320); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_55.add(string_literal552);

					WORD553=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter4322); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WORD.add(WORD553);

					char_literal554=(Token)match(input,142,FOLLOW_142_in_input_parameter4324); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_142.add(char_literal554);

					// AST REWRITE
					// elements: WORD, 142, 55
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 471:21: -> ^( T_PARAMETER[] '${' WORD '}' )
					{
						// JPA2.g:471:24: ^( T_PARAMETER[] '${' WORD '}' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_55.nextNode());
						adaptor.addChild(root_1, stream_WORD.nextNode());
						adaptor.addChild(root_1, stream_142.nextNode());
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
	// JPA2.g:473:1: literal : WORD ;
	public final JPA2Parser.literal_return literal() throws RecognitionException {
		JPA2Parser.literal_return retval = new JPA2Parser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD555=null;

		Object WORD555_tree=null;

		try {
			// JPA2.g:474:5: ( WORD )
			// JPA2.g:474:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD555=(Token)match(input,WORD,FOLLOW_WORD_in_literal4352); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD555_tree = (Object)adaptor.create(WORD555);
			adaptor.addChild(root_0, WORD555_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:476:1: constructor_name : WORD ;
	public final JPA2Parser.constructor_name_return constructor_name() throws RecognitionException {
		JPA2Parser.constructor_name_return retval = new JPA2Parser.constructor_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD556=null;

		Object WORD556_tree=null;

		try {
			// JPA2.g:477:5: ( WORD )
			// JPA2.g:477:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD556=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4364); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD556_tree = (Object)adaptor.create(WORD556);
			adaptor.addChild(root_0, WORD556_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:479:1: enum_literal : WORD ;
	public final JPA2Parser.enum_literal_return enum_literal() throws RecognitionException {
		JPA2Parser.enum_literal_return retval = new JPA2Parser.enum_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD557=null;

		Object WORD557_tree=null;

		try {
			// JPA2.g:480:5: ( WORD )
			// JPA2.g:480:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD557=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal4376); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD557_tree = (Object)adaptor.create(WORD557);
			adaptor.addChild(root_0, WORD557_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:482:1: boolean_literal : ( 'true' | 'false' );
	public final JPA2Parser.boolean_literal_return boolean_literal() throws RecognitionException {
		JPA2Parser.boolean_literal_return retval = new JPA2Parser.boolean_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set558=null;

		Object set558_tree=null;

		try {
			// JPA2.g:483:5: ( 'true' | 'false' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set558=input.LT(1);
			if ( (input.LA(1) >= 140 && input.LA(1) <= 141) ) {
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
	// $ANTLR end "boolean_literal"


	public static class field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "field"
	// JPA2.g:487:1: field : ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | date_part );
	public final JPA2Parser.field_return field() throws RecognitionException {
		JPA2Parser.field_return retval = new JPA2Parser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD559=null;
		Token string_literal560=null;
		Token string_literal561=null;
		Token string_literal562=null;
		Token string_literal563=null;
		Token string_literal564=null;
		Token string_literal565=null;
		Token string_literal566=null;
		Token string_literal567=null;
		Token string_literal568=null;
		ParserRuleReturnScope date_part569 =null;

		Object WORD559_tree=null;
		Object string_literal560_tree=null;
		Object string_literal561_tree=null;
		Object string_literal562_tree=null;
		Object string_literal563_tree=null;
		Object string_literal564_tree=null;
		Object string_literal565_tree=null;
		Object string_literal566_tree=null;
		Object string_literal567_tree=null;
		Object string_literal568_tree=null;

		try {
			// JPA2.g:488:5: ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | date_part )
			int alt130=11;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt130=1;
				}
				break;
			case 122:
				{
				alt130=2;
				}
				break;
			case 98:
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
			case 88:
			case 94:
			case 100:
			case 109:
			case 111:
			case 119:
			case 121:
			case 136:
			case 139:
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
					// JPA2.g:488:7: WORD
					{
					root_0 = (Object)adaptor.nil();


					WORD559=(Token)match(input,WORD,FOLLOW_WORD_in_field4409); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD559_tree = (Object)adaptor.create(WORD559);
					adaptor.addChild(root_0, WORD559_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:488:14: 'SELECT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal560=(Token)match(input,122,FOLLOW_122_in_field4413); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal560_tree = (Object)adaptor.create(string_literal560);
					adaptor.addChild(root_0, string_literal560_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:488:25: 'FROM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal561=(Token)match(input,98,FOLLOW_98_in_field4417); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal561_tree = (Object)adaptor.create(string_literal561);
					adaptor.addChild(root_0, string_literal561_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:488:34: 'GROUP'
					{
					root_0 = (Object)adaptor.nil();


					string_literal562=(Token)match(input,GROUP,FOLLOW_GROUP_in_field4421); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal562_tree = (Object)adaptor.create(string_literal562);
					adaptor.addChild(root_0, string_literal562_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:488:44: 'ORDER'
					{
					root_0 = (Object)adaptor.nil();


					string_literal563=(Token)match(input,ORDER,FOLLOW_ORDER_in_field4425); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal563_tree = (Object)adaptor.create(string_literal563);
					adaptor.addChild(root_0, string_literal563_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:488:54: 'MAX'
					{
					root_0 = (Object)adaptor.nil();


					string_literal564=(Token)match(input,MAX,FOLLOW_MAX_in_field4429); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal564_tree = (Object)adaptor.create(string_literal564);
					adaptor.addChild(root_0, string_literal564_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:488:62: 'MIN'
					{
					root_0 = (Object)adaptor.nil();


					string_literal565=(Token)match(input,MIN,FOLLOW_MIN_in_field4433); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal565_tree = (Object)adaptor.create(string_literal565);
					adaptor.addChild(root_0, string_literal565_tree);
					}

					}
					break;
				case 8 :
					// JPA2.g:488:70: 'SUM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal566=(Token)match(input,SUM,FOLLOW_SUM_in_field4437); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal566_tree = (Object)adaptor.create(string_literal566);
					adaptor.addChild(root_0, string_literal566_tree);
					}

					}
					break;
				case 9 :
					// JPA2.g:488:78: 'AVG'
					{
					root_0 = (Object)adaptor.nil();


					string_literal567=(Token)match(input,AVG,FOLLOW_AVG_in_field4441); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal567_tree = (Object)adaptor.create(string_literal567);
					adaptor.addChild(root_0, string_literal567_tree);
					}

					}
					break;
				case 10 :
					// JPA2.g:488:86: 'COUNT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal568=(Token)match(input,COUNT,FOLLOW_COUNT_in_field4445); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal568_tree = (Object)adaptor.create(string_literal568);
					adaptor.addChild(root_0, string_literal568_tree);
					}

					}
					break;
				case 11 :
					// JPA2.g:488:96: date_part
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_part_in_field4449);
					date_part569=date_part();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part569.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:490:1: identification_variable : WORD ;
	public final JPA2Parser.identification_variable_return identification_variable() throws RecognitionException {
		JPA2Parser.identification_variable_return retval = new JPA2Parser.identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD570=null;

		Object WORD570_tree=null;

		try {
			// JPA2.g:491:5: ( WORD )
			// JPA2.g:491:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD570=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable4461); if (state.failed) return retval;
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
	// $ANTLR end "identification_variable"


	public static class parameter_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "parameter_name"
	// JPA2.g:493:1: parameter_name : WORD ( '.' WORD )* ;
	public final JPA2Parser.parameter_name_return parameter_name() throws RecognitionException {
		JPA2Parser.parameter_name_return retval = new JPA2Parser.parameter_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD571=null;
		Token char_literal572=null;
		Token WORD573=null;

		Object WORD571_tree=null;
		Object char_literal572_tree=null;
		Object WORD573_tree=null;

		try {
			// JPA2.g:494:5: ( WORD ( '.' WORD )* )
			// JPA2.g:494:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD571=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4473); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD571_tree = (Object)adaptor.create(WORD571);
			adaptor.addChild(root_0, WORD571_tree);
			}

			// JPA2.g:494:12: ( '.' WORD )*
			loop131:
			while (true) {
				int alt131=2;
				int LA131_0 = input.LA(1);
				if ( (LA131_0==60) ) {
					alt131=1;
				}

				switch (alt131) {
				case 1 :
					// JPA2.g:494:13: '.' WORD
					{
					char_literal572=(Token)match(input,60,FOLLOW_60_in_parameter_name4476); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal572_tree = (Object)adaptor.create(char_literal572);
					adaptor.addChild(root_0, char_literal572_tree);
					}

					WORD573=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4479); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD573_tree = (Object)adaptor.create(WORD573);
					adaptor.addChild(root_0, WORD573_tree);
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
	// JPA2.g:497:1: escape_character : ( '\\'.\\'' | STRING_LITERAL );
	public final JPA2Parser.escape_character_return escape_character() throws RecognitionException {
		JPA2Parser.escape_character_return retval = new JPA2Parser.escape_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set574=null;

		Object set574_tree=null;

		try {
			// JPA2.g:498:5: ( '\\'.\\'' | STRING_LITERAL )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set574=input.LT(1);
			if ( input.LA(1)==STRING_LITERAL||input.LA(1)==TRIM_CHARACTER ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set574));
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
	// JPA2.g:499:1: trim_character : TRIM_CHARACTER ;
	public final JPA2Parser.trim_character_return trim_character() throws RecognitionException {
		JPA2Parser.trim_character_return retval = new JPA2Parser.trim_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRIM_CHARACTER575=null;

		Object TRIM_CHARACTER575_tree=null;

		try {
			// JPA2.g:500:5: ( TRIM_CHARACTER )
			// JPA2.g:500:7: TRIM_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			TRIM_CHARACTER575=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_trim_character4509); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TRIM_CHARACTER575_tree = (Object)adaptor.create(TRIM_CHARACTER575);
			adaptor.addChild(root_0, TRIM_CHARACTER575_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:501:1: string_literal : STRING_LITERAL ;
	public final JPA2Parser.string_literal_return string_literal() throws RecognitionException {
		JPA2Parser.string_literal_return retval = new JPA2Parser.string_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL576=null;

		Object STRING_LITERAL576_tree=null;

		try {
			// JPA2.g:502:5: ( STRING_LITERAL )
			// JPA2.g:502:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL576=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_literal4520); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL576_tree = (Object)adaptor.create(STRING_LITERAL576);
			adaptor.addChild(root_0, STRING_LITERAL576_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:503:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
	public final JPA2Parser.numeric_literal_return numeric_literal() throws RecognitionException {
		JPA2Parser.numeric_literal_return retval = new JPA2Parser.numeric_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal577=null;
		Token INT_NUMERAL578=null;

		Object string_literal577_tree=null;
		Object INT_NUMERAL578_tree=null;

		try {
			// JPA2.g:504:5: ( ( '0x' )? INT_NUMERAL )
			// JPA2.g:504:7: ( '0x' )? INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:504:7: ( '0x' )?
			int alt132=2;
			int LA132_0 = input.LA(1);
			if ( (LA132_0==62) ) {
				alt132=1;
			}
			switch (alt132) {
				case 1 :
					// JPA2.g:504:8: '0x'
					{
					string_literal577=(Token)match(input,62,FOLLOW_62_in_numeric_literal4532); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal577_tree = (Object)adaptor.create(string_literal577);
					adaptor.addChild(root_0, string_literal577_tree);
					}

					}
					break;

			}

			INT_NUMERAL578=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal4536); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL578_tree = (Object)adaptor.create(INT_NUMERAL578);
			adaptor.addChild(root_0, INT_NUMERAL578_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:505:1: single_valued_object_field : WORD ;
	public final JPA2Parser.single_valued_object_field_return single_valued_object_field() throws RecognitionException {
		JPA2Parser.single_valued_object_field_return retval = new JPA2Parser.single_valued_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD579=null;

		Object WORD579_tree=null;

		try {
			// JPA2.g:506:5: ( WORD )
			// JPA2.g:506:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD579=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_object_field4548); if (state.failed) return retval;
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
	// $ANTLR end "single_valued_object_field"


	public static class single_valued_embeddable_object_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "single_valued_embeddable_object_field"
	// JPA2.g:507:1: single_valued_embeddable_object_field : WORD ;
	public final JPA2Parser.single_valued_embeddable_object_field_return single_valued_embeddable_object_field() throws RecognitionException {
		JPA2Parser.single_valued_embeddable_object_field_return retval = new JPA2Parser.single_valued_embeddable_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD580=null;

		Object WORD580_tree=null;

		try {
			// JPA2.g:508:5: ( WORD )
			// JPA2.g:508:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD580=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_embeddable_object_field4559); if (state.failed) return retval;
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
	// $ANTLR end "single_valued_embeddable_object_field"


	public static class collection_valued_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "collection_valued_field"
	// JPA2.g:509:1: collection_valued_field : WORD ;
	public final JPA2Parser.collection_valued_field_return collection_valued_field() throws RecognitionException {
		JPA2Parser.collection_valued_field_return retval = new JPA2Parser.collection_valued_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD581=null;

		Object WORD581_tree=null;

		try {
			// JPA2.g:510:5: ( WORD )
			// JPA2.g:510:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD581=(Token)match(input,WORD,FOLLOW_WORD_in_collection_valued_field4570); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD581_tree = (Object)adaptor.create(WORD581);
			adaptor.addChild(root_0, WORD581_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:511:1: entity_name : WORD ;
	public final JPA2Parser.entity_name_return entity_name() throws RecognitionException {
		JPA2Parser.entity_name_return retval = new JPA2Parser.entity_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD582=null;

		Object WORD582_tree=null;

		try {
			// JPA2.g:512:5: ( WORD )
			// JPA2.g:512:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD582=(Token)match(input,WORD,FOLLOW_WORD_in_entity_name4581); if (state.failed) return retval;
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
	// $ANTLR end "entity_name"


	public static class subtype_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "subtype"
	// JPA2.g:513:1: subtype : WORD ;
	public final JPA2Parser.subtype_return subtype() throws RecognitionException {
		JPA2Parser.subtype_return retval = new JPA2Parser.subtype_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD583=null;

		Object WORD583_tree=null;

		try {
			// JPA2.g:514:5: ( WORD )
			// JPA2.g:514:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD583=(Token)match(input,WORD,FOLLOW_WORD_in_subtype4592); if (state.failed) return retval;
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
	// $ANTLR end "subtype"


	public static class entity_type_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_type_literal"
	// JPA2.g:515:1: entity_type_literal : WORD ;
	public final JPA2Parser.entity_type_literal_return entity_type_literal() throws RecognitionException {
		JPA2Parser.entity_type_literal_return retval = new JPA2Parser.entity_type_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD584=null;

		Object WORD584_tree=null;

		try {
			// JPA2.g:516:5: ( WORD )
			// JPA2.g:516:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD584=(Token)match(input,WORD,FOLLOW_WORD_in_entity_type_literal4603); if (state.failed) return retval;
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
	// $ANTLR end "entity_type_literal"


	public static class function_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "function_name"
	// JPA2.g:517:1: function_name : STRING_LITERAL ;
	public final JPA2Parser.function_name_return function_name() throws RecognitionException {
		JPA2Parser.function_name_return retval = new JPA2Parser.function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL585=null;

		Object STRING_LITERAL585_tree=null;

		try {
			// JPA2.g:518:5: ( STRING_LITERAL )
			// JPA2.g:518:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL585=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_function_name4614); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL585_tree = (Object)adaptor.create(STRING_LITERAL585);
			adaptor.addChild(root_0, STRING_LITERAL585_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:519:1: state_field : WORD ;
	public final JPA2Parser.state_field_return state_field() throws RecognitionException {
		JPA2Parser.state_field_return retval = new JPA2Parser.state_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD586=null;

		Object WORD586_tree=null;

		try {
			// JPA2.g:520:5: ( WORD )
			// JPA2.g:520:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD586=(Token)match(input,WORD,FOLLOW_WORD_in_state_field4625); if (state.failed) return retval;
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
	// $ANTLR end "state_field"


	public static class result_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "result_variable"
	// JPA2.g:521:1: result_variable : WORD ;
	public final JPA2Parser.result_variable_return result_variable() throws RecognitionException {
		JPA2Parser.result_variable_return retval = new JPA2Parser.result_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD587=null;

		Object WORD587_tree=null;

		try {
			// JPA2.g:522:5: ( WORD )
			// JPA2.g:522:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD587=(Token)match(input,WORD,FOLLOW_WORD_in_result_variable4636); if (state.failed) return retval;
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
	// $ANTLR end "result_variable"


	public static class superquery_identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "superquery_identification_variable"
	// JPA2.g:523:1: superquery_identification_variable : WORD ;
	public final JPA2Parser.superquery_identification_variable_return superquery_identification_variable() throws RecognitionException {
		JPA2Parser.superquery_identification_variable_return retval = new JPA2Parser.superquery_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD588=null;

		Object WORD588_tree=null;

		try {
			// JPA2.g:524:5: ( WORD )
			// JPA2.g:524:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD588=(Token)match(input,WORD,FOLLOW_WORD_in_superquery_identification_variable4647); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD588_tree = (Object)adaptor.create(WORD588);
			adaptor.addChild(root_0, WORD588_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:525:1: date_time_timestamp_literal : WORD ;
	public final JPA2Parser.date_time_timestamp_literal_return date_time_timestamp_literal() throws RecognitionException {
		JPA2Parser.date_time_timestamp_literal_return retval = new JPA2Parser.date_time_timestamp_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD589=null;

		Object WORD589_tree=null;

		try {
			// JPA2.g:526:5: ( WORD )
			// JPA2.g:526:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD589=(Token)match(input,WORD,FOLLOW_WORD_in_date_time_timestamp_literal4658); if (state.failed) return retval;
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
	// $ANTLR end "date_time_timestamp_literal"


	public static class pattern_value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "pattern_value"
	// JPA2.g:527:1: pattern_value : string_literal ;
	public final JPA2Parser.pattern_value_return pattern_value() throws RecognitionException {
		JPA2Parser.pattern_value_return retval = new JPA2Parser.pattern_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal590 =null;


		try {
			// JPA2.g:528:5: ( string_literal )
			// JPA2.g:528:7: string_literal
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_literal_in_pattern_value4669);
			string_literal590=string_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal590.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:529:1: collection_valued_input_parameter : input_parameter ;
	public final JPA2Parser.collection_valued_input_parameter_return collection_valued_input_parameter() throws RecognitionException {
		JPA2Parser.collection_valued_input_parameter_return retval = new JPA2Parser.collection_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter591 =null;


		try {
			// JPA2.g:530:5: ( input_parameter )
			// JPA2.g:530:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_collection_valued_input_parameter4680);
			input_parameter591=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter591.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:531:1: single_valued_input_parameter : input_parameter ;
	public final JPA2Parser.single_valued_input_parameter_return single_valued_input_parameter() throws RecognitionException {
		JPA2Parser.single_valued_input_parameter_return retval = new JPA2Parser.single_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter592 =null;


		try {
			// JPA2.g:532:5: ( input_parameter )
			// JPA2.g:532:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_single_valued_input_parameter4691);
			input_parameter592=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter592.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

	// $ANTLR start synpred21_JPA2
	public final void synpred21_JPA2_fragment() throws RecognitionException {
		// JPA2.g:117:48: ( field )
		// JPA2.g:117:48: field
		{
		pushFollow(FOLLOW_field_in_synpred21_JPA2885);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred21_JPA2

	// $ANTLR start synpred30_JPA2
	public final void synpred30_JPA2_fragment() throws RecognitionException {
		// JPA2.g:135:48: ( field )
		// JPA2.g:135:48: field
		{
		pushFollow(FOLLOW_field_in_synpred30_JPA21075);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred30_JPA2

	// $ANTLR start synpred33_JPA2
	public final void synpred33_JPA2_fragment() throws RecognitionException {
		// JPA2.g:152:7: ( scalar_expression )
		// JPA2.g:152:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred33_JPA21187);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred33_JPA2

	// $ANTLR start synpred34_JPA2
	public final void synpred34_JPA2_fragment() throws RecognitionException {
		// JPA2.g:153:7: ( simple_entity_expression )
		// JPA2.g:153:7: simple_entity_expression
		{
		pushFollow(FOLLOW_simple_entity_expression_in_synpred34_JPA21195);
		simple_entity_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred34_JPA2

	// $ANTLR start synpred39_JPA2
	public final void synpred39_JPA2_fragment() throws RecognitionException {
		// JPA2.g:165:7: ( path_expression )
		// JPA2.g:165:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred39_JPA21315);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred39_JPA2

	// $ANTLR start synpred40_JPA2
	public final void synpred40_JPA2_fragment() throws RecognitionException {
		// JPA2.g:166:7: ( identification_variable )
		// JPA2.g:166:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred40_JPA21323);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred40_JPA2

	// $ANTLR start synpred41_JPA2
	public final void synpred41_JPA2_fragment() throws RecognitionException {
		// JPA2.g:167:7: ( scalar_expression )
		// JPA2.g:167:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred41_JPA21341);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred41_JPA2

	// $ANTLR start synpred42_JPA2
	public final void synpred42_JPA2_fragment() throws RecognitionException {
		// JPA2.g:168:7: ( aggregate_expression )
		// JPA2.g:168:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred42_JPA21349);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred42_JPA2

	// $ANTLR start synpred45_JPA2
	public final void synpred45_JPA2_fragment() throws RecognitionException {
		// JPA2.g:174:7: ( path_expression )
		// JPA2.g:174:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred45_JPA21406);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred45_JPA2

	// $ANTLR start synpred46_JPA2
	public final void synpred46_JPA2_fragment() throws RecognitionException {
		// JPA2.g:175:7: ( scalar_expression )
		// JPA2.g:175:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred46_JPA21414);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred46_JPA2

	// $ANTLR start synpred47_JPA2
	public final void synpred47_JPA2_fragment() throws RecognitionException {
		// JPA2.g:176:7: ( aggregate_expression )
		// JPA2.g:176:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred47_JPA21422);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred47_JPA2

	// $ANTLR start synpred49_JPA2
	public final void synpred49_JPA2_fragment() throws RecognitionException {
		// JPA2.g:179:7: ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' )
		// JPA2.g:179:7: aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')'
		{
		pushFollow(FOLLOW_aggregate_expression_function_name_in_synpred49_JPA21441);
		aggregate_expression_function_name();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred49_JPA21443); if (state.failed) return;

		// JPA2.g:179:45: ( DISTINCT )?
		int alt139=2;
		int LA139_0 = input.LA(1);
		if ( (LA139_0==DISTINCT) ) {
			alt139=1;
		}
		switch (alt139) {
			case 1 :
				// JPA2.g:179:46: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred49_JPA21445); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_path_expression_in_synpred49_JPA21449);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred49_JPA21450); if (state.failed) return;

		}

	}
	// $ANTLR end synpred49_JPA2

	// $ANTLR start synpred51_JPA2
	public final void synpred51_JPA2_fragment() throws RecognitionException {
		// JPA2.g:181:7: ( 'COUNT' '(' ( DISTINCT )? count_argument ')' )
		// JPA2.g:181:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
		{
		match(input,COUNT,FOLLOW_COUNT_in_synpred51_JPA21484); if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred51_JPA21486); if (state.failed) return;

		// JPA2.g:181:18: ( DISTINCT )?
		int alt140=2;
		int LA140_0 = input.LA(1);
		if ( (LA140_0==DISTINCT) ) {
			alt140=1;
		}
		switch (alt140) {
			case 1 :
				// JPA2.g:181:19: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred51_JPA21488); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_count_argument_in_synpred51_JPA21492);
		count_argument();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred51_JPA21494); if (state.failed) return;

		}

	}
	// $ANTLR end synpred51_JPA2

	// $ANTLR start synpred61_JPA2
	public final void synpred61_JPA2_fragment() throws RecognitionException {
		// JPA2.g:204:7: ( path_expression )
		// JPA2.g:204:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred61_JPA21755);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred61_JPA2

	// $ANTLR start synpred62_JPA2
	public final void synpred62_JPA2_fragment() throws RecognitionException {
		// JPA2.g:204:25: ( general_identification_variable )
		// JPA2.g:204:25: general_identification_variable
		{
		pushFollow(FOLLOW_general_identification_variable_in_synpred62_JPA21759);
		general_identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred62_JPA2

	// $ANTLR start synpred63_JPA2
	public final void synpred63_JPA2_fragment() throws RecognitionException {
		// JPA2.g:204:59: ( result_variable )
		// JPA2.g:204:59: result_variable
		{
		pushFollow(FOLLOW_result_variable_in_synpred63_JPA21763);
		result_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred63_JPA2

	// $ANTLR start synpred64_JPA2
	public final void synpred64_JPA2_fragment() throws RecognitionException {
		// JPA2.g:204:77: ( scalar_expression )
		// JPA2.g:204:77: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred64_JPA21767);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred64_JPA2

	// $ANTLR start synpred73_JPA2
	public final void synpred73_JPA2_fragment() throws RecognitionException {
		// JPA2.g:219:7: ( general_derived_path '.' single_valued_object_field )
		// JPA2.g:219:7: general_derived_path '.' single_valued_object_field
		{
		pushFollow(FOLLOW_general_derived_path_in_synpred73_JPA21957);
		general_derived_path();
		state._fsp--;
		if (state.failed) return;

		match(input,60,FOLLOW_60_in_synpred73_JPA21958); if (state.failed) return;

		pushFollow(FOLLOW_single_valued_object_field_in_synpred73_JPA21959);
		single_valued_object_field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred73_JPA2

	// $ANTLR start synpred78_JPA2
	public final void synpred78_JPA2_fragment() throws RecognitionException {
		// JPA2.g:237:7: ( path_expression )
		// JPA2.g:237:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred78_JPA22111);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred78_JPA2

	// $ANTLR start synpred79_JPA2
	public final void synpred79_JPA2_fragment() throws RecognitionException {
		// JPA2.g:238:7: ( scalar_expression )
		// JPA2.g:238:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred79_JPA22119);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred79_JPA2

	// $ANTLR start synpred80_JPA2
	public final void synpred80_JPA2_fragment() throws RecognitionException {
		// JPA2.g:239:7: ( aggregate_expression )
		// JPA2.g:239:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred80_JPA22127);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred80_JPA2

	// $ANTLR start synpred81_JPA2
	public final void synpred81_JPA2_fragment() throws RecognitionException {
		// JPA2.g:242:7: ( arithmetic_expression )
		// JPA2.g:242:7: arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred81_JPA22146);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred81_JPA2

	// $ANTLR start synpred82_JPA2
	public final void synpred82_JPA2_fragment() throws RecognitionException {
		// JPA2.g:243:7: ( string_expression )
		// JPA2.g:243:7: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred82_JPA22154);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred82_JPA2

	// $ANTLR start synpred83_JPA2
	public final void synpred83_JPA2_fragment() throws RecognitionException {
		// JPA2.g:244:7: ( enum_expression )
		// JPA2.g:244:7: enum_expression
		{
		pushFollow(FOLLOW_enum_expression_in_synpred83_JPA22162);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred83_JPA2

	// $ANTLR start synpred84_JPA2
	public final void synpred84_JPA2_fragment() throws RecognitionException {
		// JPA2.g:245:7: ( datetime_expression )
		// JPA2.g:245:7: datetime_expression
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred84_JPA22170);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred84_JPA2

	// $ANTLR start synpred85_JPA2
	public final void synpred85_JPA2_fragment() throws RecognitionException {
		// JPA2.g:246:7: ( boolean_expression )
		// JPA2.g:246:7: boolean_expression
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred85_JPA22178);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred85_JPA2

	// $ANTLR start synpred86_JPA2
	public final void synpred86_JPA2_fragment() throws RecognitionException {
		// JPA2.g:247:7: ( case_expression )
		// JPA2.g:247:7: case_expression
		{
		pushFollow(FOLLOW_case_expression_in_synpred86_JPA22186);
		case_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred86_JPA2

	// $ANTLR start synpred89_JPA2
	public final void synpred89_JPA2_fragment() throws RecognitionException {
		// JPA2.g:254:8: ( 'NOT' )
		// JPA2.g:254:8: 'NOT'
		{
		match(input,NOT,FOLLOW_NOT_in_synpred89_JPA22246); if (state.failed) return;

		}

	}
	// $ANTLR end synpred89_JPA2

	// $ANTLR start synpred90_JPA2
	public final void synpred90_JPA2_fragment() throws RecognitionException {
		// JPA2.g:256:7: ( simple_cond_expression )
		// JPA2.g:256:7: simple_cond_expression
		{
		pushFollow(FOLLOW_simple_cond_expression_in_synpred90_JPA22261);
		simple_cond_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred90_JPA2

	// $ANTLR start synpred91_JPA2
	public final void synpred91_JPA2_fragment() throws RecognitionException {
		// JPA2.g:260:7: ( comparison_expression )
		// JPA2.g:260:7: comparison_expression
		{
		pushFollow(FOLLOW_comparison_expression_in_synpred91_JPA22298);
		comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred91_JPA2

	// $ANTLR start synpred92_JPA2
	public final void synpred92_JPA2_fragment() throws RecognitionException {
		// JPA2.g:261:7: ( between_expression )
		// JPA2.g:261:7: between_expression
		{
		pushFollow(FOLLOW_between_expression_in_synpred92_JPA22306);
		between_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred92_JPA2

	// $ANTLR start synpred93_JPA2
	public final void synpred93_JPA2_fragment() throws RecognitionException {
		// JPA2.g:262:7: ( in_expression )
		// JPA2.g:262:7: in_expression
		{
		pushFollow(FOLLOW_in_expression_in_synpred93_JPA22314);
		in_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred93_JPA2

	// $ANTLR start synpred94_JPA2
	public final void synpred94_JPA2_fragment() throws RecognitionException {
		// JPA2.g:263:7: ( like_expression )
		// JPA2.g:263:7: like_expression
		{
		pushFollow(FOLLOW_like_expression_in_synpred94_JPA22322);
		like_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred94_JPA2

	// $ANTLR start synpred95_JPA2
	public final void synpred95_JPA2_fragment() throws RecognitionException {
		// JPA2.g:264:7: ( null_comparison_expression )
		// JPA2.g:264:7: null_comparison_expression
		{
		pushFollow(FOLLOW_null_comparison_expression_in_synpred95_JPA22330);
		null_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred95_JPA2

	// $ANTLR start synpred96_JPA2
	public final void synpred96_JPA2_fragment() throws RecognitionException {
		// JPA2.g:265:7: ( empty_collection_comparison_expression )
		// JPA2.g:265:7: empty_collection_comparison_expression
		{
		pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred96_JPA22338);
		empty_collection_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred96_JPA2

	// $ANTLR start synpred97_JPA2
	public final void synpred97_JPA2_fragment() throws RecognitionException {
		// JPA2.g:266:7: ( collection_member_expression )
		// JPA2.g:266:7: collection_member_expression
		{
		pushFollow(FOLLOW_collection_member_expression_in_synpred97_JPA22346);
		collection_member_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred97_JPA2

	// $ANTLR start synpred116_JPA2
	public final void synpred116_JPA2_fragment() throws RecognitionException {
		// JPA2.g:295:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
		// JPA2.g:295:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred116_JPA22599);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:295:29: ( 'NOT' )?
		int alt142=2;
		int LA142_0 = input.LA(1);
		if ( (LA142_0==NOT) ) {
			alt142=1;
		}
		switch (alt142) {
			case 1 :
				// JPA2.g:295:30: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred116_JPA22602); if (state.failed) return;

				}
				break;

		}

		match(input,79,FOLLOW_79_in_synpred116_JPA22606); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred116_JPA22608);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred116_JPA22610); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred116_JPA22612);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred116_JPA2

	// $ANTLR start synpred118_JPA2
	public final void synpred118_JPA2_fragment() throws RecognitionException {
		// JPA2.g:296:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
		// JPA2.g:296:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred118_JPA22620);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:296:25: ( 'NOT' )?
		int alt143=2;
		int LA143_0 = input.LA(1);
		if ( (LA143_0==NOT) ) {
			alt143=1;
		}
		switch (alt143) {
			case 1 :
				// JPA2.g:296:26: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred118_JPA22623); if (state.failed) return;

				}
				break;

		}

		match(input,79,FOLLOW_79_in_synpred118_JPA22627); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred118_JPA22629);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred118_JPA22631); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred118_JPA22633);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred118_JPA2

	// $ANTLR start synpred129_JPA2
	public final void synpred129_JPA2_fragment() throws RecognitionException {
		// JPA2.g:312:42: ( string_expression )
		// JPA2.g:312:42: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred129_JPA22814);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred129_JPA2

	// $ANTLR start synpred130_JPA2
	public final void synpred130_JPA2_fragment() throws RecognitionException {
		// JPA2.g:312:62: ( pattern_value )
		// JPA2.g:312:62: pattern_value
		{
		pushFollow(FOLLOW_pattern_value_in_synpred130_JPA22818);
		pattern_value();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred130_JPA2

	// $ANTLR start synpred132_JPA2
	public final void synpred132_JPA2_fragment() throws RecognitionException {
		// JPA2.g:314:8: ( path_expression )
		// JPA2.g:314:8: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred132_JPA22841);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred132_JPA2

	// $ANTLR start synpred140_JPA2
	public final void synpred140_JPA2_fragment() throws RecognitionException {
		// JPA2.g:324:7: ( identification_variable )
		// JPA2.g:324:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred140_JPA22943);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred140_JPA2

	// $ANTLR start synpred147_JPA2
	public final void synpred147_JPA2_fragment() throws RecognitionException {
		// JPA2.g:332:7: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) )
		// JPA2.g:332:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_string_expression_in_synpred147_JPA23012);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:332:25: ( comparison_operator | 'REGEXP' )
		int alt145=2;
		int LA145_0 = input.LA(1);
		if ( ((LA145_0 >= 63 && LA145_0 <= 68)) ) {
			alt145=1;
		}
		else if ( (LA145_0==120) ) {
			alt145=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 145, 0, input);
			throw nvae;
		}

		switch (alt145) {
			case 1 :
				// JPA2.g:332:26: comparison_operator
				{
				pushFollow(FOLLOW_comparison_operator_in_synpred147_JPA23015);
				comparison_operator();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:332:48: 'REGEXP'
				{
				match(input,120,FOLLOW_120_in_synpred147_JPA23019); if (state.failed) return;

				}
				break;

		}

		// JPA2.g:332:58: ( string_expression | all_or_any_expression )
		int alt146=2;
		int LA146_0 = input.LA(1);
		if ( (LA146_0==AVG||LA146_0==COUNT||(LA146_0 >= LOWER && LA146_0 <= NAMED_PARAMETER)||(LA146_0 >= STRING_LITERAL && LA146_0 <= SUM)||LA146_0==WORD||LA146_0==55||LA146_0==69||(LA146_0 >= 81 && LA146_0 <= 84)||LA146_0==97||LA146_0==99||LA146_0==115||LA146_0==127||LA146_0==131||LA146_0==134) ) {
			alt146=1;
		}
		else if ( ((LA146_0 >= 76 && LA146_0 <= 77)||LA146_0==125) ) {
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
				// JPA2.g:332:59: string_expression
				{
				pushFollow(FOLLOW_string_expression_in_synpred147_JPA23023);
				string_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:332:79: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred147_JPA23027);
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
		// JPA2.g:333:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
		// JPA2.g:333:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred150_JPA23036);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:333:39: ( boolean_expression | all_or_any_expression )
		int alt147=2;
		int LA147_0 = input.LA(1);
		if ( (LA147_0==LPAREN||LA147_0==NAMED_PARAMETER||LA147_0==WORD||LA147_0==55||LA147_0==69||(LA147_0 >= 81 && LA147_0 <= 83)||LA147_0==97||LA147_0==99||LA147_0==115||(LA147_0 >= 140 && LA147_0 <= 141)) ) {
			alt147=1;
		}
		else if ( ((LA147_0 >= 76 && LA147_0 <= 77)||LA147_0==125) ) {
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
				// JPA2.g:333:40: boolean_expression
				{
				pushFollow(FOLLOW_boolean_expression_in_synpred150_JPA23047);
				boolean_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:333:61: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred150_JPA23051);
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
		// JPA2.g:334:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
		// JPA2.g:334:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_enum_expression_in_synpred153_JPA23060);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:334:34: ( enum_expression | all_or_any_expression )
		int alt148=2;
		int LA148_0 = input.LA(1);
		if ( (LA148_0==LPAREN||LA148_0==NAMED_PARAMETER||LA148_0==WORD||LA148_0==55||LA148_0==69||LA148_0==81||LA148_0==83||LA148_0==115) ) {
			alt148=1;
		}
		else if ( ((LA148_0 >= 76 && LA148_0 <= 77)||LA148_0==125) ) {
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
				// JPA2.g:334:35: enum_expression
				{
				pushFollow(FOLLOW_enum_expression_in_synpred153_JPA23069);
				enum_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:334:53: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred153_JPA23073);
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
		// JPA2.g:335:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
		// JPA2.g:335:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred155_JPA23082);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred155_JPA23084);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:335:47: ( datetime_expression | all_or_any_expression )
		int alt149=2;
		int LA149_0 = input.LA(1);
		if ( (LA149_0==AVG||LA149_0==COUNT||(LA149_0 >= LPAREN && LA149_0 <= NAMED_PARAMETER)||LA149_0==SUM||LA149_0==WORD||LA149_0==55||LA149_0==69||(LA149_0 >= 81 && LA149_0 <= 83)||(LA149_0 >= 85 && LA149_0 <= 87)||LA149_0==97||LA149_0==99||LA149_0==115) ) {
			alt149=1;
		}
		else if ( ((LA149_0 >= 76 && LA149_0 <= 77)||LA149_0==125) ) {
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
				// JPA2.g:335:48: datetime_expression
				{
				pushFollow(FOLLOW_datetime_expression_in_synpred155_JPA23087);
				datetime_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:335:70: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred155_JPA23091);
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
		// JPA2.g:336:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
		// JPA2.g:336:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_entity_expression_in_synpred158_JPA23100);
		entity_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:336:38: ( entity_expression | all_or_any_expression )
		int alt150=2;
		int LA150_0 = input.LA(1);
		if ( (LA150_0==NAMED_PARAMETER||LA150_0==WORD||LA150_0==55||LA150_0==69) ) {
			alt150=1;
		}
		else if ( ((LA150_0 >= 76 && LA150_0 <= 77)||LA150_0==125) ) {
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
				// JPA2.g:336:39: entity_expression
				{
				pushFollow(FOLLOW_entity_expression_in_synpred158_JPA23111);
				entity_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:336:59: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred158_JPA23115);
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
		// JPA2.g:337:7: ( entity_type_expression ( '=' | '<>' ) entity_type_expression )
		// JPA2.g:337:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
		{
		pushFollow(FOLLOW_entity_type_expression_in_synpred160_JPA23124);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		pushFollow(FOLLOW_entity_type_expression_in_synpred160_JPA23134);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred160_JPA2

	// $ANTLR start synpred167_JPA2
	public final void synpred167_JPA2_fragment() throws RecognitionException {
		// JPA2.g:348:7: ( arithmetic_term )
		// JPA2.g:348:7: arithmetic_term
		{
		pushFollow(FOLLOW_arithmetic_term_in_synpred167_JPA23215);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred167_JPA2

	// $ANTLR start synpred169_JPA2
	public final void synpred169_JPA2_fragment() throws RecognitionException {
		// JPA2.g:351:7: ( arithmetic_factor )
		// JPA2.g:351:7: arithmetic_factor
		{
		pushFollow(FOLLOW_arithmetic_factor_in_synpred169_JPA23244);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred169_JPA2

	// $ANTLR start synpred175_JPA2
	public final void synpred175_JPA2_fragment() throws RecognitionException {
		// JPA2.g:358:7: ( '(' arithmetic_expression ')' )
		// JPA2.g:358:7: '(' arithmetic_expression ')'
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred175_JPA23313); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred175_JPA23314);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred175_JPA23315); if (state.failed) return;

		}

	}
	// $ANTLR end synpred175_JPA2

	// $ANTLR start synpred178_JPA2
	public final void synpred178_JPA2_fragment() throws RecognitionException {
		// JPA2.g:361:7: ( aggregate_expression )
		// JPA2.g:361:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred178_JPA23339);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred178_JPA2

	// $ANTLR start synpred180_JPA2
	public final void synpred180_JPA2_fragment() throws RecognitionException {
		// JPA2.g:363:7: ( function_invocation )
		// JPA2.g:363:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred180_JPA23355);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred180_JPA2

	// $ANTLR start synpred186_JPA2
	public final void synpred186_JPA2_fragment() throws RecognitionException {
		// JPA2.g:371:7: ( aggregate_expression )
		// JPA2.g:371:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred186_JPA23414);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred186_JPA2

	// $ANTLR start synpred188_JPA2
	public final void synpred188_JPA2_fragment() throws RecognitionException {
		// JPA2.g:373:7: ( function_invocation )
		// JPA2.g:373:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred188_JPA23430);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred188_JPA2

	// $ANTLR start synpred190_JPA2
	public final void synpred190_JPA2_fragment() throws RecognitionException {
		// JPA2.g:377:7: ( path_expression )
		// JPA2.g:377:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred190_JPA23457);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred190_JPA2

	// $ANTLR start synpred193_JPA2
	public final void synpred193_JPA2_fragment() throws RecognitionException {
		// JPA2.g:380:7: ( aggregate_expression )
		// JPA2.g:380:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred193_JPA23481);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred193_JPA2

	// $ANTLR start synpred195_JPA2
	public final void synpred195_JPA2_fragment() throws RecognitionException {
		// JPA2.g:382:7: ( function_invocation )
		// JPA2.g:382:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred195_JPA23497);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred195_JPA2

	// $ANTLR start synpred197_JPA2
	public final void synpred197_JPA2_fragment() throws RecognitionException {
		// JPA2.g:384:7: ( date_time_timestamp_literal )
		// JPA2.g:384:7: date_time_timestamp_literal
		{
		pushFollow(FOLLOW_date_time_timestamp_literal_in_synpred197_JPA23513);
		date_time_timestamp_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred197_JPA2

	// $ANTLR start synpred235_JPA2
	public final void synpred235_JPA2_fragment() throws RecognitionException {
		// JPA2.g:435:7: ( literal )
		// JPA2.g:435:7: literal
		{
		pushFollow(FOLLOW_literal_in_synpred235_JPA23968);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred235_JPA2

	// $ANTLR start synpred237_JPA2
	public final void synpred237_JPA2_fragment() throws RecognitionException {
		// JPA2.g:437:7: ( input_parameter )
		// JPA2.g:437:7: input_parameter
		{
		pushFollow(FOLLOW_input_parameter_in_synpred237_JPA23984);
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
		"\1\6\1\27\2\uffff\1\13\1\65\1\37\1\uffff\1\6\13\37\1\0\1\6";
	static final String DFA40_maxS =
		"\1\143\1\27\2\uffff\2\65\1\74\1\uffff\1\u008b\13\74\1\0\1\u008b";
	static final String DFA40_acceptS =
		"\2\uffff\1\1\1\3\3\uffff\1\2\16\uffff";
	static final String DFA40_specialS =
		"\24\uffff\1\0\1\uffff}>";
	static final String[] DFA40_transitionS = {
			"\1\2\2\uffff\1\1\16\uffff\2\2\10\uffff\1\2\100\uffff\1\3",
			"\1\4",
			"",
			"",
			"\1\5\51\uffff\1\6",
			"\1\6",
			"\1\7\34\uffff\1\10",
			"",
			"\1\21\2\uffff\1\22\4\uffff\1\14\11\uffff\1\16\1\17\3\uffff\1\15\1\uffff"+
			"\1\24\2\uffff\1\20\22\uffff\1\11\42\uffff\1\23\5\uffff\1\23\3\uffff\1"+
			"\13\1\uffff\1\23\10\uffff\1\23\1\uffff\1\23\7\uffff\1\23\1\uffff\1\23"+
			"\1\12\15\uffff\1\23\2\uffff\1\23",
			"\1\24\34\uffff\1\25",
			"\1\24\34\uffff\1\25",
			"\1\24\34\uffff\1\25",
			"\1\24\34\uffff\1\25",
			"\1\24\34\uffff\1\25",
			"\1\24\34\uffff\1\25",
			"\1\24\34\uffff\1\25",
			"\1\24\34\uffff\1\25",
			"\1\24\34\uffff\1\25",
			"\1\24\34\uffff\1\25",
			"\1\24\34\uffff\1\25",
			"\1\uffff",
			"\1\21\2\uffff\1\22\4\uffff\1\14\11\uffff\1\16\1\17\3\uffff\1\15\1\uffff"+
			"\1\24\2\uffff\1\20\22\uffff\1\11\42\uffff\1\23\5\uffff\1\23\3\uffff\1"+
			"\13\1\uffff\1\23\10\uffff\1\23\1\uffff\1\23\7\uffff\1\23\1\uffff\1\23"+
			"\1\12\15\uffff\1\23\2\uffff\1\23"
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
			return "178:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );";
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

	public static final BitSet FOLLOW_select_statement_in_ql_statement427 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_update_statement_in_ql_statement431 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_delete_statement_in_ql_statement435 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_122_in_select_statement450 = new BitSet(new long[]{0x4AA0000607C40A40L,0xD0194A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_select_clause_in_select_statement452 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_from_clause_in_select_statement454 = new BitSet(new long[]{0x000000002000C002L,0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_where_clause_in_select_statement457 = new BitSet(new long[]{0x000000002000C002L});
	public static final BitSet FOLLOW_groupby_clause_in_select_statement462 = new BitSet(new long[]{0x0000000020008002L});
	public static final BitSet FOLLOW_having_clause_in_select_statement467 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_orderby_clause_in_select_statement472 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_133_in_update_statement530 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_update_clause_in_update_statement532 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_where_clause_in_update_statement535 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_89_in_delete_statement571 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_98_in_delete_statement573 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_delete_clause_in_delete_statement575 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_where_clause_in_delete_statement578 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_98_in_from_clause616 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause618 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_from_clause621 = new BitSet(new long[]{0x0020000000010000L});
	public static final BitSet FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause623 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration657 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration666 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration690 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_joined_clause_in_identification_variable_declaration692 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_joined_clause_in_join_section723 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_join_in_joined_clause731 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fetch_join_in_joined_clause735 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_range_variable_declaration747 = new BitSet(new long[]{0x0020000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_range_variable_declaration750 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration754 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_join783 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_join_association_path_expression_in_join785 = new BitSet(new long[]{0x0020000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_join788 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join792 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
	public static final BitSet FOLLOW_118_in_join795 = new BitSet(new long[]{0x4AA000060FC40240L,0xD0084A2B00FE0FE0L,0x000000000000305CL});
	public static final BitSet FOLLOW_conditional_expression_in_join797 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_fetch_join828 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_FETCH_in_fetch_join830 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join832 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFT_in_join_spec846 = new BitSet(new long[]{0x0000000040080000L});
	public static final BitSet FOLLOW_OUTER_in_join_spec850 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_INNER_in_join_spec856 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_JOIN_in_join_spec861 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression875 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression877 = new BitSet(new long[]{0x0020000423004242L,0x0680A01441000000L,0x0000000000000900L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression880 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression881 = new BitSet(new long[]{0x0020000423004242L,0x0680A01441000000L,0x0000000000000900L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression885 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_130_in_join_association_path_expression920 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression922 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression924 = new BitSet(new long[]{0x0020000423004240L,0x0680A01441004000L,0x0000000000000900L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression927 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression928 = new BitSet(new long[]{0x0020000423004240L,0x0680A01441004000L,0x0000000000000900L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression932 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_join_association_path_expression935 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_subtype_in_join_association_path_expression937 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_join_association_path_expression939 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_join_association_path_expression972 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_collection_member_declaration985 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration986 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_declaration988 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration990 = new BitSet(new long[]{0x0020000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_collection_member_declaration993 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration997 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_qualified_identification_variable1026 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_93_in_qualified_identification_variable1034 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_qualified_identification_variable1035 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_qualified_identification_variable1036 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_map_field_identification_variable1043 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1044 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1045 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_135_in_map_field_identification_variable1049 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1050 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1051 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_path_expression1065 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_path_expression1067 = new BitSet(new long[]{0x0020000423004242L,0x0680A01441000000L,0x0000000000000900L});
	public static final BitSet FOLLOW_field_in_path_expression1070 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_path_expression1071 = new BitSet(new long[]{0x0020000423004242L,0x0680A01441000000L,0x0000000000000900L});
	public static final BitSet FOLLOW_field_in_path_expression1075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_general_identification_variable1114 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_general_identification_variable1122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_update_clause1135 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_update_clause1137 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1139 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_update_clause1142 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1144 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_path_expression_in_update_item1172 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_update_item1174 = new BitSet(new long[]{0x4AA0000607C40240L,0xD00C4A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_new_value_in_update_item1176 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_new_value1187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_new_value1195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_114_in_new_value1203 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_delete_clause1215 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_select_clause1242 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0194A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_select_item_in_select_clause1246 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_select_clause1249 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0194A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_select_item_in_select_clause1251 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_select_expression_in_select_item1294 = new BitSet(new long[]{0x0020000000000002L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_select_item1298 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_result_variable_in_select_item1302 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_select_expression1315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1323 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1341 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_select_expression1349 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_116_in_select_expression1357 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_select_expression1359 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1360 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_select_expression1361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_constructor_expression_in_select_expression1369 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_112_in_constructor_expression1380 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_constructor_name_in_constructor_expression1382 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_constructor_expression1384 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1386 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_constructor_expression1389 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1391 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_constructor_expression1395 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_constructor_item1406 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_constructor_item1414 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_constructor_item1422 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_constructor_item1430 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1441 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1443 = new BitSet(new long[]{0x0020000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1445 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_aggregate_expression1449 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_aggregate_expression1484 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1486 = new BitSet(new long[]{0x0020000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1488 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_count_argument_in_aggregate_expression1492 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1494 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_aggregate_expression1529 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_count_argument1566 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_count_argument1570 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_138_in_where_clause1583 = new BitSet(new long[]{0x4AA000060FC40240L,0xD0084A2B00FE0FE0L,0x000000000000305CL});
	public static final BitSet FOLLOW_conditional_expression_in_where_clause1585 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_groupby_clause1607 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_groupby_clause1609 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1611 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_groupby_clause1614 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1616 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_path_expression_in_groupby_item1650 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_groupby_item1654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HAVING_in_having_clause1665 = new BitSet(new long[]{0x4AA000060FC40240L,0xD0084A2B00FE0FE0L,0x000000000000305CL});
	public static final BitSet FOLLOW_conditional_expression_in_having_clause1667 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderby_clause1678 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_orderby_clause1680 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084AAA00FE0820L,0x00000000000030D8L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1682 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_orderby_clause1685 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084AAA00FE0820L,0x00000000000030D8L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1687 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1721 = new BitSet(new long[]{0x0000000000000422L});
	public static final BitSet FOLLOW_sort_in_orderby_item1723 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_orderby_variable1755 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_orderby_variable1759 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_orderby_variable1763 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_orderby_variable1767 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_orderby_variable1771 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_subquery1801 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L});
	public static final BitSet FOLLOW_122_in_subquery1803 = new BitSet(new long[]{0x4AA0000607C40A40L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_simple_select_clause_in_subquery1805 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_subquery_from_clause_in_subquery1807 = new BitSet(new long[]{0x000000008000C000L,0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_where_clause_in_subquery1810 = new BitSet(new long[]{0x000000008000C000L});
	public static final BitSet FOLLOW_groupby_clause_in_subquery1815 = new BitSet(new long[]{0x0000000080008000L});
	public static final BitSet FOLLOW_having_clause_in_subquery1820 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_subquery1826 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_98_in_subquery_from_clause1876 = new BitSet(new long[]{0x0020000000010000L,0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1878 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_subquery_from_clause1881 = new BitSet(new long[]{0x0020000000010000L,0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1883 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1921 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1929 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_subselect_identification_variable_declaration1931 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration1933 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_join_in_subselect_identification_variable_declaration1936 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1946 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1957 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_path_expression1958 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_path_expression1959 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1967 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_path_expression1968 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_path_expression1969 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_derived_path_in_general_derived_path1980 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_treated_derived_path_in_general_derived_path1988 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_general_derived_path1990 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_general_derived_path1991 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_simple_derived_path2009 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_130_in_treated_derived_path2026 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_general_derived_path_in_treated_derived_path2027 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_treated_derived_path2029 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_subtype_in_treated_derived_path2031 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_treated_derived_path2033 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_derived_collection_member_declaration2044 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2046 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_collection_member_declaration2047 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2049 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_collection_member_declaration2051 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_collection_member_declaration2054 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause2067 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause2071 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_simple_select_expression2111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_select_expression2119 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression2127 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_select_expression2135 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_scalar_expression2146 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_scalar_expression2154 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_scalar_expression2162 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_scalar_expression2170 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_scalar_expression2178 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_scalar_expression2186 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_scalar_expression2194 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2206 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_OR_in_conditional_expression2210 = new BitSet(new long[]{0x4AA000060FC40240L,0xD0084A2B00FE0FE0L,0x000000000000305CL});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2212 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2226 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_conditional_term2230 = new BitSet(new long[]{0x4AA000060FC40240L,0xD0084A2B00FE0FE0L,0x000000000000305CL});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2232 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_NOT_in_conditional_factor2246 = new BitSet(new long[]{0x4AA000060FC40240L,0xD0084A2B00FE0FE0L,0x000000000000305CL});
	public static final BitSet FOLLOW_conditional_primary_in_conditional_factor2250 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_conditional_primary2261 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_conditional_primary2285 = new BitSet(new long[]{0x4AA000060FC40240L,0xD0084A2B00FE0FE0L,0x000000000000305CL});
	public static final BitSet FOLLOW_conditional_expression_in_conditional_primary2286 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_conditional_primary2287 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression2298 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_simple_cond_expression2306 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_simple_cond_expression2314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_simple_cond_expression2322 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression2330 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression2346 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression2354 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression2362 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2375 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2383 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2391 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2399 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2407 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_70_in_date_between_macro_expression2419 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2421 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2423 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_between_macro_expression2425 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
	public static final BitSet FOLLOW_113_in_date_between_macro_expression2427 = new BitSet(new long[]{0x0E00000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2430 = new BitSet(new long[]{0x4000000000040000L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2438 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_between_macro_expression2442 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
	public static final BitSet FOLLOW_113_in_date_between_macro_expression2444 = new BitSet(new long[]{0x0E00000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2447 = new BitSet(new long[]{0x4000000000040000L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2455 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_between_macro_expression2459 = new BitSet(new long[]{0x0000000000000000L,0x0200A01001000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2461 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_72_in_date_before_macro_expression2496 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2498 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2500 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_before_macro_expression2502 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2505 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2509 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2512 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_date_after_macro_expression2524 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2526 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2528 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_after_macro_expression2530 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2533 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2537 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2540 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_73_in_date_equals_macro_expression2552 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2554 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2556 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_equals_macro_expression2558 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2561 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2565 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2568 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_74_in_date_today_macro_expression2580 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2582 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2584 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2586 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2599 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2602 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_between_expression2606 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2608 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2610 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2612 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2620 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2623 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_between_expression2627 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2629 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2631 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2633 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2641 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2644 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_between_expression2648 = new BitSet(new long[]{0x00A0000407800240L,0x0008000A00EE0020L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2650 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2652 = new BitSet(new long[]{0x00A0000407800240L,0x0008000A00EE0020L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2666 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_type_discriminator_in_in_expression2670 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_identification_variable_in_in_expression2674 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_NOT_in_in_expression2678 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_IN_in_in_expression2682 = new BitSet(new long[]{0x0080000004800000L,0x0000000000000020L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2698 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_in_item_in_in_expression2700 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_in_expression2703 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_in_item_in_in_expression2705 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2709 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_in_expression2725 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_valued_input_parameter_in_in_expression2741 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2757 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2759 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2761 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_in_item2789 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_single_valued_input_parameter_in_in_item2793 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2804 = new BitSet(new long[]{0x0000000008000000L,0x0000040000000000L});
	public static final BitSet FOLLOW_NOT_in_like_expression2807 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
	public static final BitSet FOLLOW_106_in_like_expression2811 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2814 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
	public static final BitSet FOLLOW_pattern_value_in_like_expression2818 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_like_expression2822 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_like_expression2825 = new BitSet(new long[]{0x0000000A00000000L});
	public static final BitSet FOLLOW_escape_character_in_like_expression2827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_null_comparison_expression2841 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
	public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression2845 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_null_comparison_expression2849 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
	public static final BitSet FOLLOW_102_in_null_comparison_expression2852 = new BitSet(new long[]{0x0000000008000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_NOT_in_null_comparison_expression2855 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_114_in_null_comparison_expression2859 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression2870 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
	public static final BitSet FOLLOW_102_in_empty_collection_comparison_expression2872 = new BitSet(new long[]{0x0000000008000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_NOT_in_empty_collection_comparison_expression2875 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_91_in_empty_collection_comparison_expression2879 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_or_value_expression_in_collection_member_expression2890 = new BitSet(new long[]{0x0000000008000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_NOT_in_collection_member_expression2894 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_108_in_collection_member_expression2898 = new BitSet(new long[]{0x0020000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_117_in_collection_member_expression2901 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_expression2905 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_or_value_expression2916 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2924 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_entity_or_value_expression2932 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_or_value_expression2943 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_or_value_expression2951 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simple_entity_or_value_expression2959 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_exists_expression2971 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
	public static final BitSet FOLLOW_96_in_exists_expression2975 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_subquery_in_exists_expression2977 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_all_or_any_expression2988 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_subquery_in_all_or_any_expression3001 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3012 = new BitSet(new long[]{0x8000000000000000L,0x010000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3015 = new BitSet(new long[]{0x00A0000607C00240L,0xA008000A001E3020L,0x0000000000000048L});
	public static final BitSet FOLLOW_120_in_comparison_expression3019 = new BitSet(new long[]{0x00A0000607C00240L,0xA008000A001E3020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3023 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3027 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3036 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3038 = new BitSet(new long[]{0x00A0000004800000L,0x2008000A000E3020L,0x0000000000003000L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3047 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3051 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3060 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3062 = new BitSet(new long[]{0x00A0000004800000L,0x20080000000A3020L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3069 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3073 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3082 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3084 = new BitSet(new long[]{0x00A0000407800240L,0x2008000A00EE3020L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3087 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3091 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3100 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3102 = new BitSet(new long[]{0x00A0000004000000L,0x2000000000003020L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3115 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3124 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3126 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L,0x0000000000000010L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3134 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3142 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3144 = new BitSet(new long[]{0x4AA0000407840240L,0x70084A2A000E3820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3147 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3151 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3215 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3223 = new BitSet(new long[]{0x0A00000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_expression3225 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3233 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3244 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3252 = new BitSet(new long[]{0x2100000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_term3254 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3263 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor3286 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_arithmetic_primary3297 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary3305 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary3313 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_arithmetic_primary3314 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary3315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary3323 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary3331 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary3339 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_arithmetic_primary3347 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_arithmetic_primary3355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_arithmetic_primary3363 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_arithmetic_primary3371 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_string_expression3382 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_string_expression3390 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_string_expression3398 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_strings_in_string_expression3406 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_string_expression3414 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_string_expression3422 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_string_expression3430 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_string_expression3438 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_string_expression3446 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_datetime_expression3457 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_datetime_expression3465 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_expression3473 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_datetime_expression3481 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_datetime_expression3489 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_datetime_expression3497 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_datetime_expression3505 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_datetime_expression3513 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_datetime_expression3521 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_boolean_expression3532 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_literal_in_boolean_expression3540 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_boolean_expression3548 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_boolean_expression3556 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_boolean_expression3564 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_boolean_expression3572 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_boolean_expression3580 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_enum_expression3591 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_literal_in_enum_expression3599 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_enum_expression3607 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_enum_expression3615 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_enum_expression3623 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_expression3634 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3642 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3653 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3661 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_entity_type_expression3672 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_literal_in_entity_type_expression3680 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_entity_type_expression3688 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_132_in_type_discriminator3699 = new BitSet(new long[]{0x00A0000004000000L,0x0000008000000020L,0x0000000000000080L});
	public static final BitSet FOLLOW_general_identification_variable_in_type_discriminator3702 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_path_expression_in_type_discriminator3706 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_type_discriminator3710 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_type_discriminator3713 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_105_in_functions_returning_numerics3724 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3725 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3726 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_107_in_functions_returning_numerics3734 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3736 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_numerics3737 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3739 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_numerics3741 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3742 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3745 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_functions_returning_numerics3753 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3754 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3755 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_126_in_functions_returning_numerics3763 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3764 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3765 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_110_in_functions_returning_numerics3773 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3774 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_numerics3775 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3777 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3778 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_124_in_functions_returning_numerics3786 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3787 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3788 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_functions_returning_numerics3796 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_functions_returning_numerics3797 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3798 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_84_in_functions_returning_strings3836 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3837 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3838 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3840 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3843 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3845 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3848 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_127_in_functions_returning_strings3856 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3858 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3859 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3861 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3864 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3866 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3869 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_functions_returning_strings3877 = new BitSet(new long[]{0x00A0000E07C00240L,0x8008010E001F0020L,0x000000000000004AL});
	public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings3880 = new BitSet(new long[]{0x0000000800000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_trim_character_in_functions_returning_strings3885 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_98_in_functions_returning_strings3889 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3893 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3895 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LOWER_in_functions_returning_strings3903 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3905 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3906 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3907 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_134_in_functions_returning_strings3915 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3916 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3917 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_99_in_function_invocation3947 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_function_name_in_function_invocation3948 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_function_invocation3951 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_function_arg_in_function_invocation3953 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_function_invocation3957 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_function_arg3968 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_function_arg3976 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_function_arg3984 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_function_arg3992 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_case_expression_in_case_expression4003 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_case_expression_in_case_expression4011 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coalesce_expression_in_case_expression4019 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullif_expression_in_case_expression4027 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_general_case_expression4038 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4040 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4043 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_90_in_general_case_expression4047 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_scalar_expression_in_general_case_expression4049 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
	public static final BitSet FOLLOW_92_in_general_case_expression4051 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_137_in_when_clause4062 = new BitSet(new long[]{0x4AA000060FC40240L,0xD0084A2B00FE0FE0L,0x000000000000305CL});
	public static final BitSet FOLLOW_conditional_expression_in_when_clause4064 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_128_in_when_clause4066 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_scalar_expression_in_when_clause4068 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_simple_case_expression4079 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_case_operand_in_simple_case_expression4081 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4083 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4086 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_90_in_simple_case_expression4090 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_case_expression4092 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
	public static final BitSet FOLLOW_92_in_simple_case_expression4094 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_case_operand4105 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_case_operand4113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_137_in_simple_when_clause4124 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4126 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_128_in_simple_when_clause4128 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4130 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_83_in_coalesce_expression4141 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4142 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_coalesce_expression4145 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4147 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_coalesce_expression4150 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_115_in_nullif_expression4161 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4162 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_nullif_expression4164 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4166 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_nullif_expression4167 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_82_in_extension_functions4179 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_function_arg_in_extension_functions4181 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_WORD_in_extension_functions4183 = new BitSet(new long[]{0x0000000080800000L});
	public static final BitSet FOLLOW_LPAREN_in_extension_functions4186 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4187 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_extension_functions4190 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4192 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4197 = new BitSet(new long[]{0x0000000080800000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4201 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_97_in_extension_functions4209 = new BitSet(new long[]{0x0000000000000000L,0x0280A01041000000L,0x0000000000000900L});
	public static final BitSet FOLLOW_date_part_in_extension_functions4211 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_98_in_extension_functions4213 = new BitSet(new long[]{0x4AA0000607C40240L,0xD0084A2A00FE0820L,0x0000000000003058L});
	public static final BitSet FOLLOW_function_arg_in_extension_functions4215 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4217 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_69_in_input_parameter4274 = new BitSet(new long[]{0x4000000000040000L});
	public static final BitSet FOLLOW_numeric_literal_in_input_parameter4276 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter4299 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_55_in_input_parameter4320 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_WORD_in_input_parameter4322 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_142_in_input_parameter4324 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_literal4352 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4364 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_literal4376 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_field4409 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_122_in_field4413 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_98_in_field4417 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_field4421 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_field4425 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MAX_in_field4429 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MIN_in_field4433 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SUM_in_field4437 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AVG_in_field4441 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_field4445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_part_in_field4449 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_identification_variable4461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4473 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_parameter_name4476 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4479 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_TRIM_CHARACTER_in_trim_character4509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string_literal4520 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_62_in_numeric_literal4532 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal4536 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_object_field4548 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_embeddable_object_field4559 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_collection_valued_field4570 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_name4581 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_subtype4592 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_type_literal4603 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_function_name4614 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_state_field4625 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_result_variable4636 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_superquery_identification_variable4647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_date_time_timestamp_literal4658 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_pattern_value4669 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_collection_valued_input_parameter4680 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_single_valued_input_parameter4691 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred21_JPA2885 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred30_JPA21075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred33_JPA21187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_synpred34_JPA21195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred39_JPA21315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred40_JPA21323 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred41_JPA21341 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred42_JPA21349 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred45_JPA21406 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred46_JPA21414 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred47_JPA21422 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_synpred49_JPA21441 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred49_JPA21443 = new BitSet(new long[]{0x0020000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred49_JPA21445 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_synpred49_JPA21449 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred49_JPA21450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_synpred51_JPA21484 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred51_JPA21486 = new BitSet(new long[]{0x0020000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred51_JPA21488 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_count_argument_in_synpred51_JPA21492 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred51_JPA21494 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred61_JPA21755 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_synpred62_JPA21759 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_synpred63_JPA21763 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred64_JPA21767 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_synpred73_JPA21957 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_synpred73_JPA21958 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_synpred73_JPA21959 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred78_JPA22111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred79_JPA22119 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred80_JPA22127 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred81_JPA22146 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred82_JPA22154 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred83_JPA22162 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred84_JPA22170 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred85_JPA22178 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_synpred86_JPA22186 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_synpred89_JPA22246 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_synpred90_JPA22261 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_synpred91_JPA22298 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_synpred92_JPA22306 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_synpred93_JPA22314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_synpred94_JPA22322 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_synpred95_JPA22330 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred96_JPA22338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_synpred97_JPA22346 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred116_JPA22599 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_synpred116_JPA22602 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_synpred116_JPA22606 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred116_JPA22608 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred116_JPA22610 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred116_JPA22612 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred118_JPA22620 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_synpred118_JPA22623 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_synpred118_JPA22627 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_synpred118_JPA22629 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred118_JPA22631 = new BitSet(new long[]{0x00A0000607C00240L,0x8008000A001E0020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_synpred118_JPA22633 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred129_JPA22814 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_pattern_value_in_synpred130_JPA22818 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred132_JPA22841 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred140_JPA22943 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred147_JPA23012 = new BitSet(new long[]{0x8000000000000000L,0x010000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred147_JPA23015 = new BitSet(new long[]{0x00A0000607C00240L,0xA008000A001E3020L,0x0000000000000048L});
	public static final BitSet FOLLOW_120_in_synpred147_JPA23019 = new BitSet(new long[]{0x00A0000607C00240L,0xA008000A001E3020L,0x0000000000000048L});
	public static final BitSet FOLLOW_string_expression_in_synpred147_JPA23023 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred147_JPA23027 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred150_JPA23036 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred150_JPA23038 = new BitSet(new long[]{0x00A0000004800000L,0x2008000A000E3020L,0x0000000000003000L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred150_JPA23047 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred150_JPA23051 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred153_JPA23060 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred153_JPA23062 = new BitSet(new long[]{0x00A0000004800000L,0x20080000000A3020L});
	public static final BitSet FOLLOW_enum_expression_in_synpred153_JPA23069 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred153_JPA23073 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred155_JPA23082 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred155_JPA23084 = new BitSet(new long[]{0x00A0000407800240L,0x2008000A00EE3020L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred155_JPA23087 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred155_JPA23091 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred158_JPA23100 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred158_JPA23102 = new BitSet(new long[]{0x00A0000004000000L,0x2000000000003020L});
	public static final BitSet FOLLOW_entity_expression_in_synpred158_JPA23111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred158_JPA23115 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred160_JPA23124 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred160_JPA23126 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L,0x0000000000000010L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred160_JPA23134 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred167_JPA23215 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred169_JPA23244 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred175_JPA23313 = new BitSet(new long[]{0x4AA0000407840240L,0x50084A2A000E0820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred175_JPA23314 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred175_JPA23315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred178_JPA23339 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred180_JPA23355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred186_JPA23414 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred188_JPA23430 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred190_JPA23457 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred193_JPA23481 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred195_JPA23497 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_synpred197_JPA23513 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred235_JPA23968 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_synpred237_JPA23984 = new BitSet(new long[]{0x0000000000000002L});
}
