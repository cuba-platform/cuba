// $ANTLR 3.5.2 JPA2.g 2016-02-16 13:11:17

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
		"'AS'", "'BETWEEN'", "'BOTH'", "'CASE'", "'COALESCE('", "'CONCAT('", "'CURRENT_DATE'", 
		"'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'DAY'", "'DELETE'", "'ELSE'", 
		"'EMPTY'", "'END'", "'ENTRY('", "'ESCAPE'", "'EXISTS'", "'FROM'", "'FUNCTION('", 
		"'HOUR'", "'INDEX('", "'IS'", "'KEY('", "'LEADING'", "'LENGTH('", "'LIKE'", 
		"'LOCATE('", "'MEMBER'", "'MINUTE'", "'MOD('", "'MONTH'", "'NEW'", "'NOW'", 
		"'NULL'", "'NULLIF('", "'OBJECT'", "'OF'", "'ON'", "'SECOND'", "'SELECT'", 
		"'SET'", "'SIZE('", "'SOME'", "'SQRT('", "'SUBSTRING('", "'THEN'", "'TRAILING'", 
		"'TREAT('", "'TRIM('", "'TYPE('", "'UPDATE'", "'UPPER('", "'VALUE('", 
		"'WHEN'", "'WHERE'", "'YEAR'", "'false'", "'true'", "'}'"
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
			case 117:
				{
				alt1=1;
				}
				break;
			case 128:
				{
				alt1=2;
				}
				break;
			case 88:
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
		RewriteRuleTokenStream stream_117=new RewriteRuleTokenStream(adaptor,"token 117");
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
			sl=(Token)match(input,117,FOLLOW_117_in_select_statement450); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_117.add(sl);

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
			if ( (LA2_0==132) ) {
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
			// elements: where_clause, orderby_clause, select_clause, from_clause, having_clause, groupby_clause
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
		RewriteRuleTokenStream stream_128=new RewriteRuleTokenStream(adaptor,"token 128");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_update_clause=new RewriteRuleSubtreeStream(adaptor,"rule update_clause");

		try {
			// JPA2.g:85:5: (up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) )
			// JPA2.g:85:7: up= 'UPDATE' update_clause ( where_clause )?
			{
			up=(Token)match(input,128,FOLLOW_128_in_update_statement530); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_128.add(up);

			pushFollow(FOLLOW_update_clause_in_update_statement532);
			update_clause10=update_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_clause.add(update_clause10.getTree());
			// JPA2.g:85:33: ( where_clause )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==132) ) {
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
		RewriteRuleTokenStream stream_88=new RewriteRuleTokenStream(adaptor,"token 88");
		RewriteRuleTokenStream stream_95=new RewriteRuleTokenStream(adaptor,"token 95");
		RewriteRuleSubtreeStream stream_delete_clause=new RewriteRuleSubtreeStream(adaptor,"rule delete_clause");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");

		try {
			// JPA2.g:88:5: (dl= 'DELETE' 'FROM' delete_clause ( where_clause )? -> ^( T_QUERY[$dl] delete_clause ( where_clause )? ) )
			// JPA2.g:88:7: dl= 'DELETE' 'FROM' delete_clause ( where_clause )?
			{
			dl=(Token)match(input,88,FOLLOW_88_in_delete_statement571); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_88.add(dl);

			string_literal12=(Token)match(input,95,FOLLOW_95_in_delete_statement573); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_95.add(string_literal12);

			pushFollow(FOLLOW_delete_clause_in_delete_statement575);
			delete_clause13=delete_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_delete_clause.add(delete_clause13.getTree());
			// JPA2.g:88:40: ( where_clause )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==132) ) {
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
		RewriteRuleTokenStream stream_95=new RewriteRuleTokenStream(adaptor,"token 95");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
		RewriteRuleSubtreeStream stream_identification_variable_declaration_or_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration_or_collection_member_declaration");

		try {
			// JPA2.g:92:6: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) )
			// JPA2.g:92:8: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )*
			{
			fr=(Token)match(input,95,FOLLOW_95_in_from_clause616); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_95.add(fr);

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
						if ( (LA12_3==WORD||LA12_3==125) ) {
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
					if ( (LA12_3==WORD||LA12_3==125) ) {
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
					if ( (LA12_3==WORD||LA12_3==125) ) {
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
				if ( (LA12_3==WORD||LA12_3==125) ) {
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
		RewriteRuleTokenStream stream_115=new RewriteRuleTokenStream(adaptor,"token 115");
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
			if ( (LA15_0==115) ) {
				alt15=1;
			}
			switch (alt15) {
				case 1 :
					// JPA2.g:108:84: 'ON' conditional_expression
					{
					string_literal32=(Token)match(input,115,FOLLOW_115_in_join795); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_115.add(string_literal32);

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
		RewriteRuleTokenStream stream_125=new RewriteRuleTokenStream(adaptor,"token 125");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
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
				else if ( (LA22_1==EOF||(LA22_1 >= GROUP && LA22_1 <= HAVING)||LA22_1==INNER||(LA22_1 >= JOIN && LA22_1 <= LEFT)||LA22_1==ORDER||LA22_1==RPAREN||LA22_1==WORD||LA22_1==58||LA22_1==78||LA22_1==99||LA22_1==118||LA22_1==132) ) {
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
			else if ( (LA22_0==125) ) {
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
						int LA18_0 = input.LA(1);
						if ( (LA18_0==WORD) ) {
							int LA18_1 = input.LA(2);
							if ( (LA18_1==60) ) {
								alt18=1;
							}

						}
						else if ( (LA18_0==GROUP) ) {
							int LA18_3 = input.LA(2);
							if ( (LA18_3==60) ) {
								alt18=1;
							}

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
					int LA19_0 = input.LA(1);
					if ( (LA19_0==WORD) ) {
						int LA19_1 = input.LA(2);
						if ( (synpred21_JPA2()) ) {
							alt19=1;
						}
					}
					else if ( (LA19_0==GROUP) ) {
						int LA19_3 = input.LA(2);
						if ( (LA19_3==EOF||(LA19_3 >= GROUP && LA19_3 <= HAVING)||LA19_3==INNER||(LA19_3 >= JOIN && LA19_3 <= LEFT)||LA19_3==ORDER||LA19_3==RPAREN||LA19_3==WORD||LA19_3==58||LA19_3==78||LA19_3==99||LA19_3==118||LA19_3==132) ) {
							alt19=1;
						}
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
					string_literal46=(Token)match(input,125,FOLLOW_125_in_join_association_path_expression920); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_125.add(string_literal46);

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
						int LA20_0 = input.LA(1);
						if ( (LA20_0==GROUP||LA20_0==WORD) ) {
							int LA20_1 = input.LA(2);
							if ( (LA20_1==60) ) {
								alt20=1;
							}

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
					if ( (LA21_0==GROUP||LA21_0==WORD) ) {
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
			if ( (LA24_0==100||LA24_0==130) ) {
				alt24=1;
			}
			else if ( (LA24_0==92) ) {
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


					string_literal63=(Token)match(input,92,FOLLOW_92_in_qualified_identification_variable1034); if (state.failed) return retval;
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
			if ( (LA25_0==100) ) {
				alt25=1;
			}
			else if ( (LA25_0==130) ) {
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


					string_literal66=(Token)match(input,100,FOLLOW_100_in_map_field_identification_variable1043); if (state.failed) return retval;
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


					string_literal69=(Token)match(input,130,FOLLOW_130_in_map_field_identification_variable1049); if (state.failed) return retval;
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
				int LA26_0 = input.LA(1);
				if ( (LA26_0==WORD) ) {
					int LA26_1 = input.LA(2);
					if ( (LA26_1==60) ) {
						alt26=1;
					}

				}
				else if ( (LA26_0==GROUP) ) {
					int LA26_3 = input.LA(2);
					if ( (LA26_3==60) ) {
						alt26=1;
					}

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
			int LA27_0 = input.LA(1);
			if ( (LA27_0==WORD) ) {
				int LA27_1 = input.LA(2);
				if ( (synpred30_JPA2()) ) {
					alt27=1;
				}
			}
			else if ( (LA27_0==GROUP) ) {
				int LA27_3 = input.LA(2);
				if ( (LA27_3==EOF||(LA27_3 >= AND && LA27_3 <= ASC)||LA27_3==DESC||(LA27_3 >= GROUP && LA27_3 <= INNER)||(LA27_3 >= JOIN && LA27_3 <= LEFT)||(LA27_3 >= NOT && LA27_3 <= ORDER)||LA27_3==RPAREN||LA27_3==WORD||(LA27_3 >= 56 && LA27_3 <= 59)||LA27_3==61||(LA27_3 >= 63 && LA27_3 <= 68)||(LA27_3 >= 78 && LA27_3 <= 79)||LA27_3==89||LA27_3==91||LA27_3==95||LA27_3==99||LA27_3==103||LA27_3==105||LA27_3==118||LA27_3==123||(LA27_3 >= 131 && LA27_3 <= 132)) ) {
					alt27=1;
				}
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
			else if ( (LA28_0==100||LA28_0==130) ) {
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
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_118=new RewriteRuleTokenStream(adaptor,"token 118");
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
			string_literal80=(Token)match(input,118,FOLLOW_118_in_update_clause1137); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_118.add(string_literal80);

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
			case 96:
			case 98:
			case 102:
			case 104:
			case 107:
			case 112:
			case 119:
			case 121:
			case 122:
			case 126:
			case 127:
			case 129:
			case 134:
			case 135:
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
					if ( (LA30_10==136) ) {
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
			case 111:
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


					string_literal89=(Token)match(input,111,FOLLOW_111_in_new_value1203); if (state.failed) return retval;
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
			// elements: select_item, DISTINCT
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
			case 98:
			case 102:
			case 104:
			case 107:
			case 112:
			case 119:
			case 121:
			case 122:
			case 126:
			case 127:
			case 129:
			case 134:
			case 135:
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
			case 96:
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
			case 113:
				{
				alt35=5;
				}
				break;
			case 109:
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


					string_literal102=(Token)match(input,113,FOLLOW_113_in_select_expression1357); if (state.failed) return retval;
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


			string_literal107=(Token)match(input,109,FOLLOW_109_in_constructor_expression1380); if (state.failed) return retval;
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
			case 98:
			case 102:
			case 104:
			case 107:
			case 112:
			case 119:
			case 121:
			case 122:
			case 126:
			case 127:
			case 129:
			case 134:
			case 135:
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
			case 96:
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
					// elements: RPAREN, DISTINCT, LPAREN, path_expression, aggregate_expression_function_name
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
					// elements: RPAREN, LPAREN, count_argument, COUNT, DISTINCT
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
		RewriteRuleTokenStream stream_132=new RewriteRuleTokenStream(adaptor,"token 132");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");

		try {
			// JPA2.g:189:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) )
			// JPA2.g:189:7: wh= 'WHERE' conditional_expression
			{
			wh=(Token)match(input,132,FOLLOW_132_in_where_clause1583); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_132.add(wh);

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
			// elements: GROUP, BY, groupby_item
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
	// JPA2.g:200:1: orderby_item : ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) );
	public final JPA2Parser.orderby_item_return orderby_item() throws RecognitionException {
		JPA2Parser.orderby_item_return retval = new JPA2Parser.orderby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal148=null;
		Token string_literal150=null;
		ParserRuleReturnScope orderby_variable147 =null;
		ParserRuleReturnScope orderby_variable149 =null;

		Object string_literal148_tree=null;
		Object string_literal150_tree=null;
		RewriteRuleTokenStream stream_ASC=new RewriteRuleTokenStream(adaptor,"token ASC");
		RewriteRuleTokenStream stream_DESC=new RewriteRuleTokenStream(adaptor,"token DESC");
		RewriteRuleSubtreeStream stream_orderby_variable=new RewriteRuleSubtreeStream(adaptor,"rule orderby_variable");

		try {
			// JPA2.g:201:5: ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) )
			int alt46=2;
			alt46 = dfa46.predict(input);
			switch (alt46) {
				case 1 :
					// JPA2.g:201:7: orderby_variable ( 'ASC' )?
					{
					pushFollow(FOLLOW_orderby_variable_in_orderby_item1721);
					orderby_variable147=orderby_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable147.getTree());
					// JPA2.g:201:24: ( 'ASC' )?
					int alt45=2;
					int LA45_0 = input.LA(1);
					if ( (LA45_0==ASC) ) {
						alt45=1;
					}
					switch (alt45) {
						case 1 :
							// JPA2.g:201:25: 'ASC'
							{
							string_literal148=(Token)match(input,ASC,FOLLOW_ASC_in_orderby_item1724); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ASC.add(string_literal148);

							}
							break;

					}

					// AST REWRITE
					// elements: orderby_variable, ASC
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 202:6: -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? )
					{
						// JPA2.g:202:9: ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);
						adaptor.addChild(root_1, stream_orderby_variable.nextTree());
						// JPA2.g:202:65: ( 'ASC' )?
						if ( stream_ASC.hasNext() ) {
							adaptor.addChild(root_1, stream_ASC.nextNode());
						}
						stream_ASC.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// JPA2.g:203:7: orderby_variable 'DESC'
					{
					pushFollow(FOLLOW_orderby_variable_in_orderby_item1756);
					orderby_variable149=orderby_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable149.getTree());
					string_literal150=(Token)match(input,DESC,FOLLOW_DESC_in_orderby_item1759); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DESC.add(string_literal150);

					// AST REWRITE
					// elements: orderby_variable, DESC
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 204:5: -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' )
					{
						// JPA2.g:204:8: ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);
						adaptor.addChild(root_1, stream_orderby_variable.nextTree());
						adaptor.addChild(root_1, stream_DESC.nextNode());
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
	// $ANTLR end "orderby_item"


	public static class orderby_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "orderby_variable"
	// JPA2.g:205:1: orderby_variable : ( path_expression | general_identification_variable | result_variable );
	public final JPA2Parser.orderby_variable_return orderby_variable() throws RecognitionException {
		JPA2Parser.orderby_variable_return retval = new JPA2Parser.orderby_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression151 =null;
		ParserRuleReturnScope general_identification_variable152 =null;
		ParserRuleReturnScope result_variable153 =null;


		try {
			// JPA2.g:206:5: ( path_expression | general_identification_variable | result_variable )
			int alt47=3;
			int LA47_0 = input.LA(1);
			if ( (LA47_0==WORD) ) {
				int LA47_1 = input.LA(2);
				if ( (LA47_1==60) ) {
					alt47=1;
				}
				else if ( (synpred63_JPA2()) ) {
					alt47=2;
				}
				else if ( (true) ) {
					alt47=3;
				}

			}
			else if ( (LA47_0==100||LA47_0==130) ) {
				alt47=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 47, 0, input);
				throw nvae;
			}

			switch (alt47) {
				case 1 :
					// JPA2.g:206:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_orderby_variable1788);
					path_expression151=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression151.getTree());

					}
					break;
				case 2 :
					// JPA2.g:206:25: general_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_identification_variable_in_orderby_variable1792);
					general_identification_variable152=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable152.getTree());

					}
					break;
				case 3 :
					// JPA2.g:206:59: result_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_result_variable_in_orderby_variable1796);
					result_variable153=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable153.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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


	public static class subquery_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "subquery"
	// JPA2.g:208:1: subquery : lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
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
		RewriteRuleTokenStream stream_117=new RewriteRuleTokenStream(adaptor,"token 117");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
		RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");

		try {
			// JPA2.g:209:5: (lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
			// JPA2.g:209:7: lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
			{
			lp=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subquery1810); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(lp);

			string_literal154=(Token)match(input,117,FOLLOW_117_in_subquery1812); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_117.add(string_literal154);

			pushFollow(FOLLOW_simple_select_clause_in_subquery1814);
			simple_select_clause155=simple_select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause155.getTree());
			pushFollow(FOLLOW_subquery_from_clause_in_subquery1816);
			subquery_from_clause156=subquery_from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause156.getTree());
			// JPA2.g:209:65: ( where_clause )?
			int alt48=2;
			int LA48_0 = input.LA(1);
			if ( (LA48_0==132) ) {
				alt48=1;
			}
			switch (alt48) {
				case 1 :
					// JPA2.g:209:66: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_subquery1819);
					where_clause157=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause157.getTree());
					}
					break;

			}

			// JPA2.g:209:81: ( groupby_clause )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==GROUP) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// JPA2.g:209:82: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_subquery1824);
					groupby_clause158=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause158.getTree());
					}
					break;

			}

			// JPA2.g:209:99: ( having_clause )?
			int alt50=2;
			int LA50_0 = input.LA(1);
			if ( (LA50_0==HAVING) ) {
				alt50=1;
			}
			switch (alt50) {
				case 1 :
					// JPA2.g:209:100: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_subquery1829);
					having_clause159=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause159.getTree());
					}
					break;

			}

			rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1835); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(rp);

			// AST REWRITE
			// elements: where_clause, having_clause, simple_select_clause, subquery_from_clause, 117, groupby_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 210:6: -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
			{
				// JPA2.g:210:9: ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);
				adaptor.addChild(root_1, stream_117.nextNode());
				adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
				adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
				// JPA2.g:210:90: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:210:106: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:210:124: ( having_clause )?
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
	// JPA2.g:211:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
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
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_95=new RewriteRuleTokenStream(adaptor,"token 95");
		RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");

		try {
			// JPA2.g:212:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
			// JPA2.g:212:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
			{
			fr=(Token)match(input,95,FOLLOW_95_in_subquery_from_clause1885); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_95.add(fr);

			pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1887);
			subselect_identification_variable_declaration160=subselect_identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration160.getTree());
			// JPA2.g:212:63: ( ',' subselect_identification_variable_declaration )*
			loop51:
			while (true) {
				int alt51=2;
				int LA51_0 = input.LA(1);
				if ( (LA51_0==58) ) {
					alt51=1;
				}

				switch (alt51) {
				case 1 :
					// JPA2.g:212:64: ',' subselect_identification_variable_declaration
					{
					char_literal161=(Token)match(input,58,FOLLOW_58_in_subquery_from_clause1890); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal161);

					pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1892);
					subselect_identification_variable_declaration162=subselect_identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration162.getTree());
					}
					break;

				default :
					break loop51;
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
			// 213:5: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
			{
				// JPA2.g:213:8: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				// JPA2.g:213:35: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
				while ( stream_subselect_identification_variable_declaration.hasNext() ) {
					// JPA2.g:213:35: ^( T_SOURCE subselect_identification_variable_declaration )
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
	// JPA2.g:215:1: subselect_identification_variable_declaration : ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration );
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
			// JPA2.g:216:5: ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration )
			int alt53=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA53_1 = input.LA(2);
				if ( (LA53_1==WORD||LA53_1==78) ) {
					alt53=1;
				}
				else if ( (LA53_1==60) ) {
					alt53=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 53, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 125:
				{
				alt53=2;
				}
				break;
			case IN:
				{
				alt53=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 53, 0, input);
				throw nvae;
			}
			switch (alt53) {
				case 1 :
					// JPA2.g:216:7: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1930);
					identification_variable_declaration163=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration163.getTree());

					}
					break;
				case 2 :
					// JPA2.g:217:7: derived_path_expression 'AS' identification_variable ( join )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1938);
					derived_path_expression164=derived_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_path_expression164.getTree());

					string_literal165=(Token)match(input,78,FOLLOW_78_in_subselect_identification_variable_declaration1940); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal165_tree = (Object)adaptor.create(string_literal165);
					adaptor.addChild(root_0, string_literal165_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration1942);
					identification_variable166=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable166.getTree());

					// JPA2.g:217:60: ( join )*
					loop52:
					while (true) {
						int alt52=2;
						int LA52_0 = input.LA(1);
						if ( (LA52_0==INNER||(LA52_0 >= JOIN && LA52_0 <= LEFT)) ) {
							alt52=1;
						}

						switch (alt52) {
						case 1 :
							// JPA2.g:217:61: join
							{
							pushFollow(FOLLOW_join_in_subselect_identification_variable_declaration1945);
							join167=join();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, join167.getTree());

							}
							break;

						default :
							break loop52;
						}
					}

					}
					break;
				case 3 :
					// JPA2.g:218:7: derived_collection_member_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1955);
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
	// JPA2.g:219:1: derived_path_expression : ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field );
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
			// JPA2.g:220:5: ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field )
			int alt54=2;
			int LA54_0 = input.LA(1);
			if ( (LA54_0==WORD) ) {
				int LA54_1 = input.LA(2);
				if ( (synpred71_JPA2()) ) {
					alt54=1;
				}
				else if ( (true) ) {
					alt54=2;
				}

			}
			else if ( (LA54_0==125) ) {
				int LA54_2 = input.LA(2);
				if ( (synpred71_JPA2()) ) {
					alt54=1;
				}
				else if ( (true) ) {
					alt54=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 54, 0, input);
				throw nvae;
			}

			switch (alt54) {
				case 1 :
					// JPA2.g:220:7: general_derived_path '.' single_valued_object_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1966);
					general_derived_path169=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path169.getTree());

					char_literal170=(Token)match(input,60,FOLLOW_60_in_derived_path_expression1967); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal170_tree = (Object)adaptor.create(char_literal170);
					adaptor.addChild(root_0, char_literal170_tree);
					}

					pushFollow(FOLLOW_single_valued_object_field_in_derived_path_expression1968);
					single_valued_object_field171=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field171.getTree());

					}
					break;
				case 2 :
					// JPA2.g:221:7: general_derived_path '.' collection_valued_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1976);
					general_derived_path172=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path172.getTree());

					char_literal173=(Token)match(input,60,FOLLOW_60_in_derived_path_expression1977); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal173_tree = (Object)adaptor.create(char_literal173);
					adaptor.addChild(root_0, char_literal173_tree);
					}

					pushFollow(FOLLOW_collection_valued_field_in_derived_path_expression1978);
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
	// JPA2.g:222:1: general_derived_path : ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* );
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
			// JPA2.g:223:5: ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* )
			int alt56=2;
			int LA56_0 = input.LA(1);
			if ( (LA56_0==WORD) ) {
				alt56=1;
			}
			else if ( (LA56_0==125) ) {
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
					// JPA2.g:223:7: simple_derived_path
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_derived_path_in_general_derived_path1989);
					simple_derived_path175=simple_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_derived_path175.getTree());

					}
					break;
				case 2 :
					// JPA2.g:224:7: treated_derived_path ( '.' single_valued_object_field )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_treated_derived_path_in_general_derived_path1997);
					treated_derived_path176=treated_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, treated_derived_path176.getTree());

					// JPA2.g:224:27: ( '.' single_valued_object_field )*
					loop55:
					while (true) {
						int alt55=2;
						int LA55_0 = input.LA(1);
						if ( (LA55_0==60) ) {
							int LA55_1 = input.LA(2);
							if ( (LA55_1==WORD) ) {
								int LA55_3 = input.LA(3);
								if ( (LA55_3==78) ) {
									int LA55_4 = input.LA(4);
									if ( (LA55_4==WORD) ) {
										int LA55_6 = input.LA(5);
										if ( (LA55_6==RPAREN) ) {
											int LA55_7 = input.LA(6);
											if ( (LA55_7==78) ) {
												int LA55_8 = input.LA(7);
												if ( (LA55_8==WORD) ) {
													int LA55_9 = input.LA(8);
													if ( (LA55_9==RPAREN) ) {
														alt55=1;
													}

												}

											}
											else if ( (LA55_7==60) ) {
												alt55=1;
											}

										}

									}

								}
								else if ( (LA55_3==60) ) {
									alt55=1;
								}

							}

						}

						switch (alt55) {
						case 1 :
							// JPA2.g:224:28: '.' single_valued_object_field
							{
							char_literal177=(Token)match(input,60,FOLLOW_60_in_general_derived_path1999); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal177_tree = (Object)adaptor.create(char_literal177);
							adaptor.addChild(root_0, char_literal177_tree);
							}

							pushFollow(FOLLOW_single_valued_object_field_in_general_derived_path2000);
							single_valued_object_field178=single_valued_object_field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field178.getTree());

							}
							break;

						default :
							break loop55;
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
	// JPA2.g:226:1: simple_derived_path : superquery_identification_variable ;
	public final JPA2Parser.simple_derived_path_return simple_derived_path() throws RecognitionException {
		JPA2Parser.simple_derived_path_return retval = new JPA2Parser.simple_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope superquery_identification_variable179 =null;


		try {
			// JPA2.g:227:5: ( superquery_identification_variable )
			// JPA2.g:227:7: superquery_identification_variable
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_superquery_identification_variable_in_simple_derived_path2018);
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
	// JPA2.g:229:1: treated_derived_path : 'TREAT(' general_derived_path 'AS' subtype ')' ;
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
			// JPA2.g:230:5: ( 'TREAT(' general_derived_path 'AS' subtype ')' )
			// JPA2.g:230:7: 'TREAT(' general_derived_path 'AS' subtype ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal180=(Token)match(input,125,FOLLOW_125_in_treated_derived_path2035); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal180_tree = (Object)adaptor.create(string_literal180);
			adaptor.addChild(root_0, string_literal180_tree);
			}

			pushFollow(FOLLOW_general_derived_path_in_treated_derived_path2036);
			general_derived_path181=general_derived_path();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path181.getTree());

			string_literal182=(Token)match(input,78,FOLLOW_78_in_treated_derived_path2038); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal182_tree = (Object)adaptor.create(string_literal182);
			adaptor.addChild(root_0, string_literal182_tree);
			}

			pushFollow(FOLLOW_subtype_in_treated_derived_path2040);
			subtype183=subtype();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subtype183.getTree());

			char_literal184=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_treated_derived_path2042); if (state.failed) return retval;
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
	// JPA2.g:231:1: derived_collection_member_declaration : 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field ;
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
			// JPA2.g:232:5: ( 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field )
			// JPA2.g:232:7: 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field
			{
			root_0 = (Object)adaptor.nil();


			string_literal185=(Token)match(input,IN,FOLLOW_IN_in_derived_collection_member_declaration2053); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal185_tree = (Object)adaptor.create(string_literal185);
			adaptor.addChild(root_0, string_literal185_tree);
			}

			pushFollow(FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2055);
			superquery_identification_variable186=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable186.getTree());

			char_literal187=(Token)match(input,60,FOLLOW_60_in_derived_collection_member_declaration2056); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal187_tree = (Object)adaptor.create(char_literal187);
			adaptor.addChild(root_0, char_literal187_tree);
			}

			// JPA2.g:232:49: ( single_valued_object_field '.' )*
			loop57:
			while (true) {
				int alt57=2;
				int LA57_0 = input.LA(1);
				if ( (LA57_0==WORD) ) {
					int LA57_1 = input.LA(2);
					if ( (LA57_1==60) ) {
						alt57=1;
					}

				}

				switch (alt57) {
				case 1 :
					// JPA2.g:232:50: single_valued_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2058);
					single_valued_object_field188=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field188.getTree());

					char_literal189=(Token)match(input,60,FOLLOW_60_in_derived_collection_member_declaration2060); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal189_tree = (Object)adaptor.create(char_literal189);
					adaptor.addChild(root_0, char_literal189_tree);
					}

					}
					break;

				default :
					break loop57;
				}
			}

			pushFollow(FOLLOW_collection_valued_field_in_derived_collection_member_declaration2063);
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
	// JPA2.g:234:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
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
			// JPA2.g:235:5: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
			// JPA2.g:235:7: ( 'DISTINCT' )? simple_select_expression
			{
			// JPA2.g:235:7: ( 'DISTINCT' )?
			int alt58=2;
			int LA58_0 = input.LA(1);
			if ( (LA58_0==DISTINCT) ) {
				alt58=1;
			}
			switch (alt58) {
				case 1 :
					// JPA2.g:235:8: 'DISTINCT'
					{
					string_literal191=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause2076); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal191);

					}
					break;

			}

			pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause2080);
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
			// 236:5: -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
			{
				// JPA2.g:236:8: ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:236:48: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);
				// JPA2.g:236:86: ( 'DISTINCT' )?
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
	// JPA2.g:237:1: simple_select_expression : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.simple_select_expression_return simple_select_expression() throws RecognitionException {
		JPA2Parser.simple_select_expression_return retval = new JPA2Parser.simple_select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression193 =null;
		ParserRuleReturnScope scalar_expression194 =null;
		ParserRuleReturnScope aggregate_expression195 =null;
		ParserRuleReturnScope identification_variable196 =null;


		try {
			// JPA2.g:238:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt59=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA59_1 = input.LA(2);
				if ( (synpred76_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred77_JPA2()) ) {
					alt59=2;
				}
				else if ( (true) ) {
					alt59=4;
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
			case 98:
			case 102:
			case 104:
			case 107:
			case 112:
			case 119:
			case 121:
			case 122:
			case 126:
			case 127:
			case 129:
			case 134:
			case 135:
				{
				alt59=2;
				}
				break;
			case COUNT:
				{
				int LA59_16 = input.LA(2);
				if ( (synpred77_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred78_JPA2()) ) {
					alt59=3;
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
				if ( (synpred77_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred78_JPA2()) ) {
					alt59=3;
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
			case 96:
				{
				int LA59_18 = input.LA(2);
				if ( (synpred77_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred78_JPA2()) ) {
					alt59=3;
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
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 59, 0, input);
				throw nvae;
			}
			switch (alt59) {
				case 1 :
					// JPA2.g:238:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_simple_select_expression2120);
					path_expression193=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression193.getTree());

					}
					break;
				case 2 :
					// JPA2.g:239:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_simple_select_expression2128);
					scalar_expression194=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression194.getTree());

					}
					break;
				case 3 :
					// JPA2.g:240:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression2136);
					aggregate_expression195=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression195.getTree());

					}
					break;
				case 4 :
					// JPA2.g:241:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_select_expression2144);
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
	// JPA2.g:242:1: scalar_expression : ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression );
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
			// JPA2.g:243:5: ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression )
			int alt60=7;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 57:
			case 59:
			case 62:
			case 75:
			case 98:
			case 102:
			case 104:
			case 107:
			case 119:
			case 121:
				{
				alt60=1;
				}
				break;
			case WORD:
				{
				int LA60_2 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred81_JPA2()) ) {
					alt60=3;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
				}
				else if ( (synpred83_JPA2()) ) {
					alt60=5;
				}
				else if ( (true) ) {
					alt60=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA60_5 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred81_JPA2()) ) {
					alt60=3;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
				}
				else if ( (synpred83_JPA2()) ) {
					alt60=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 60, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 69:
				{
				int LA60_6 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred81_JPA2()) ) {
					alt60=3;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
				}
				else if ( (synpred83_JPA2()) ) {
					alt60=5;
				}
				else if ( (true) ) {
					alt60=7;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA60_7 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred81_JPA2()) ) {
					alt60=3;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
				}
				else if ( (synpred83_JPA2()) ) {
					alt60=5;
				}
				else if ( (true) ) {
					alt60=7;
				}

				}
				break;
			case 55:
				{
				int LA60_8 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred81_JPA2()) ) {
					alt60=3;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
				}
				else if ( (synpred83_JPA2()) ) {
					alt60=5;
				}
				else if ( (true) ) {
					alt60=7;
				}

				}
				break;
			case COUNT:
				{
				int LA60_16 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
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
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
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
			case 96:
				{
				int LA60_18 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
				}
				else if ( (synpred83_JPA2()) ) {
					alt60=5;
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
			case 81:
				{
				int LA60_19 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred81_JPA2()) ) {
					alt60=3;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
				}
				else if ( (synpred83_JPA2()) ) {
					alt60=5;
				}
				else if ( (synpred84_JPA2()) ) {
					alt60=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 60, 19, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 82:
				{
				int LA60_20 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred81_JPA2()) ) {
					alt60=3;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
				}
				else if ( (synpred83_JPA2()) ) {
					alt60=5;
				}
				else if ( (synpred84_JPA2()) ) {
					alt60=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 60, 20, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 112:
				{
				int LA60_21 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred80_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred81_JPA2()) ) {
					alt60=3;
				}
				else if ( (synpred82_JPA2()) ) {
					alt60=4;
				}
				else if ( (synpred83_JPA2()) ) {
					alt60=5;
				}
				else if ( (synpred84_JPA2()) ) {
					alt60=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 60, 21, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 83:
			case 122:
			case 126:
			case 129:
				{
				alt60=2;
				}
				break;
			case 84:
			case 85:
			case 86:
				{
				alt60=4;
				}
				break;
			case 134:
			case 135:
				{
				alt60=5;
				}
				break;
			case 127:
				{
				alt60=7;
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
					// JPA2.g:243:7: arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_scalar_expression2155);
					arithmetic_expression197=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression197.getTree());

					}
					break;
				case 2 :
					// JPA2.g:244:7: string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_scalar_expression2163);
					string_expression198=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression198.getTree());

					}
					break;
				case 3 :
					// JPA2.g:245:7: enum_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_scalar_expression2171);
					enum_expression199=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression199.getTree());

					}
					break;
				case 4 :
					// JPA2.g:246:7: datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_scalar_expression2179);
					datetime_expression200=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression200.getTree());

					}
					break;
				case 5 :
					// JPA2.g:247:7: boolean_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_scalar_expression2187);
					boolean_expression201=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression201.getTree());

					}
					break;
				case 6 :
					// JPA2.g:248:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_scalar_expression2195);
					case_expression202=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression202.getTree());

					}
					break;
				case 7 :
					// JPA2.g:249:7: entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_scalar_expression2203);
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
	// JPA2.g:250:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
	public final JPA2Parser.conditional_expression_return conditional_expression() throws RecognitionException {
		JPA2Parser.conditional_expression_return retval = new JPA2Parser.conditional_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal205=null;
		ParserRuleReturnScope conditional_term204 =null;
		ParserRuleReturnScope conditional_term206 =null;

		Object string_literal205_tree=null;

		try {
			// JPA2.g:251:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
			// JPA2.g:251:7: ( conditional_term ) ( 'OR' conditional_term )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:251:7: ( conditional_term )
			// JPA2.g:251:8: conditional_term
			{
			pushFollow(FOLLOW_conditional_term_in_conditional_expression2215);
			conditional_term204=conditional_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term204.getTree());

			}

			// JPA2.g:251:26: ( 'OR' conditional_term )*
			loop61:
			while (true) {
				int alt61=2;
				int LA61_0 = input.LA(1);
				if ( (LA61_0==OR) ) {
					alt61=1;
				}

				switch (alt61) {
				case 1 :
					// JPA2.g:251:27: 'OR' conditional_term
					{
					string_literal205=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression2219); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal205_tree = (Object)adaptor.create(string_literal205);
					adaptor.addChild(root_0, string_literal205_tree);
					}

					pushFollow(FOLLOW_conditional_term_in_conditional_expression2221);
					conditional_term206=conditional_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term206.getTree());

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
	// $ANTLR end "conditional_expression"


	public static class conditional_term_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "conditional_term"
	// JPA2.g:252:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
	public final JPA2Parser.conditional_term_return conditional_term() throws RecognitionException {
		JPA2Parser.conditional_term_return retval = new JPA2Parser.conditional_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal208=null;
		ParserRuleReturnScope conditional_factor207 =null;
		ParserRuleReturnScope conditional_factor209 =null;

		Object string_literal208_tree=null;

		try {
			// JPA2.g:253:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
			// JPA2.g:253:7: ( conditional_factor ) ( 'AND' conditional_factor )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:253:7: ( conditional_factor )
			// JPA2.g:253:8: conditional_factor
			{
			pushFollow(FOLLOW_conditional_factor_in_conditional_term2235);
			conditional_factor207=conditional_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor207.getTree());

			}

			// JPA2.g:253:28: ( 'AND' conditional_factor )*
			loop62:
			while (true) {
				int alt62=2;
				int LA62_0 = input.LA(1);
				if ( (LA62_0==AND) ) {
					alt62=1;
				}

				switch (alt62) {
				case 1 :
					// JPA2.g:253:29: 'AND' conditional_factor
					{
					string_literal208=(Token)match(input,AND,FOLLOW_AND_in_conditional_term2239); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal208_tree = (Object)adaptor.create(string_literal208);
					adaptor.addChild(root_0, string_literal208_tree);
					}

					pushFollow(FOLLOW_conditional_factor_in_conditional_term2241);
					conditional_factor209=conditional_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor209.getTree());

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
	// $ANTLR end "conditional_term"


	public static class conditional_factor_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "conditional_factor"
	// JPA2.g:254:1: conditional_factor : ( 'NOT' )? conditional_primary ;
	public final JPA2Parser.conditional_factor_return conditional_factor() throws RecognitionException {
		JPA2Parser.conditional_factor_return retval = new JPA2Parser.conditional_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal210=null;
		ParserRuleReturnScope conditional_primary211 =null;

		Object string_literal210_tree=null;

		try {
			// JPA2.g:255:5: ( ( 'NOT' )? conditional_primary )
			// JPA2.g:255:7: ( 'NOT' )? conditional_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:255:7: ( 'NOT' )?
			int alt63=2;
			int LA63_0 = input.LA(1);
			if ( (LA63_0==NOT) ) {
				int LA63_1 = input.LA(2);
				if ( (synpred87_JPA2()) ) {
					alt63=1;
				}
			}
			switch (alt63) {
				case 1 :
					// JPA2.g:255:8: 'NOT'
					{
					string_literal210=(Token)match(input,NOT,FOLLOW_NOT_in_conditional_factor2255); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal210_tree = (Object)adaptor.create(string_literal210);
					adaptor.addChild(root_0, string_literal210_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_conditional_primary_in_conditional_factor2259);
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
	// JPA2.g:256:1: conditional_primary : ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' );
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
			// JPA2.g:257:5: ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' )
			int alt64=2;
			int LA64_0 = input.LA(1);
			if ( (LA64_0==AVG||LA64_0==COUNT||LA64_0==INT_NUMERAL||LA64_0==LOWER||(LA64_0 >= MAX && LA64_0 <= NOT)||(LA64_0 >= STRING_LITERAL && LA64_0 <= SUM)||LA64_0==WORD||LA64_0==55||LA64_0==57||LA64_0==59||LA64_0==62||(LA64_0 >= 69 && LA64_0 <= 75)||(LA64_0 >= 81 && LA64_0 <= 86)||LA64_0==94||LA64_0==96||LA64_0==98||LA64_0==102||LA64_0==104||LA64_0==107||LA64_0==112||LA64_0==119||(LA64_0 >= 121 && LA64_0 <= 122)||(LA64_0 >= 125 && LA64_0 <= 127)||LA64_0==129||(LA64_0 >= 134 && LA64_0 <= 135)) ) {
				alt64=1;
			}
			else if ( (LA64_0==LPAREN) ) {
				int LA64_17 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 64, 0, input);
				throw nvae;
			}

			switch (alt64) {
				case 1 :
					// JPA2.g:257:7: simple_cond_expression
					{
					pushFollow(FOLLOW_simple_cond_expression_in_conditional_primary2270);
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
					// 258:5: -> ^( T_SIMPLE_CONDITION[] simple_cond_expression )
					{
						// JPA2.g:258:8: ^( T_SIMPLE_CONDITION[] simple_cond_expression )
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
					// JPA2.g:259:7: '(' conditional_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal213=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_primary2294); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal213_tree = (Object)adaptor.create(char_literal213);
					adaptor.addChild(root_0, char_literal213_tree);
					}

					pushFollow(FOLLOW_conditional_expression_in_conditional_primary2295);
					conditional_expression214=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression214.getTree());

					char_literal215=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_primary2296); if (state.failed) return retval;
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
	// JPA2.g:260:1: simple_cond_expression : ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
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
			// JPA2.g:261:5: ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
			int alt65=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA65_1 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt65=3;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}
				else if ( (synpred93_JPA2()) ) {
					alt65=5;
				}
				else if ( (synpred94_JPA2()) ) {
					alt65=6;
				}
				else if ( (synpred95_JPA2()) ) {
					alt65=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case STRING_LITERAL:
				{
				int LA65_2 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 69:
				{
				int LA65_3 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}
				else if ( (synpred93_JPA2()) ) {
					alt65=5;
				}
				else if ( (synpred95_JPA2()) ) {
					alt65=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA65_4 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}
				else if ( (synpred93_JPA2()) ) {
					alt65=5;
				}
				else if ( (synpred95_JPA2()) ) {
					alt65=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 55:
				{
				int LA65_5 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}
				else if ( (synpred93_JPA2()) ) {
					alt65=5;
				}
				else if ( (synpred95_JPA2()) ) {
					alt65=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 83:
				{
				int LA65_6 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 122:
				{
				int LA65_7 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 7, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 126:
				{
				int LA65_8 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LOWER:
				{
				int LA65_9 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 9, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 129:
				{
				int LA65_10 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case COUNT:
				{
				int LA65_11 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 11, input);
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
				int LA65_12 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 12, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 96:
				{
				int LA65_13 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 81:
				{
				int LA65_14 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 14, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 82:
				{
				int LA65_15 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 15, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 112:
				{
				int LA65_16 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 16, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LPAREN:
				{
				int LA65_17 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}
				else if ( (synpred92_JPA2()) ) {
					alt65=4;
				}
				else if ( (synpred95_JPA2()) ) {
					alt65=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 134:
			case 135:
				{
				alt65=1;
				}
				break;
			case 84:
			case 85:
			case 86:
				{
				int LA65_19 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 19, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 127:
				{
				int LA65_20 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred91_JPA2()) ) {
					alt65=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 20, input);
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
				int LA65_21 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 21, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 62:
				{
				int LA65_22 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 22, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA65_23 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 23, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 102:
				{
				int LA65_24 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 24, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 104:
				{
				int LA65_25 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 25, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 75:
				{
				int LA65_26 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 26, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 121:
				{
				int LA65_27 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 27, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 107:
				{
				int LA65_28 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 28, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 119:
				{
				int LA65_29 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 29, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 98:
				{
				int LA65_30 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt65=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt65=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 65, 30, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 125:
				{
				alt65=5;
				}
				break;
			case NOT:
			case 94:
				{
				alt65=8;
				}
				break;
			case 70:
			case 71:
			case 72:
			case 73:
			case 74:
				{
				alt65=9;
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
					// JPA2.g:261:7: comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression2307);
					comparison_expression216=comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression216.getTree());

					}
					break;
				case 2 :
					// JPA2.g:262:7: between_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_between_expression_in_simple_cond_expression2315);
					between_expression217=between_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression217.getTree());

					}
					break;
				case 3 :
					// JPA2.g:263:7: in_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_in_expression_in_simple_cond_expression2323);
					in_expression218=in_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression218.getTree());

					}
					break;
				case 4 :
					// JPA2.g:264:7: like_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_like_expression_in_simple_cond_expression2331);
					like_expression219=like_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression219.getTree());

					}
					break;
				case 5 :
					// JPA2.g:265:7: null_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression2339);
					null_comparison_expression220=null_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression220.getTree());

					}
					break;
				case 6 :
					// JPA2.g:266:7: empty_collection_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2347);
					empty_collection_comparison_expression221=empty_collection_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression221.getTree());

					}
					break;
				case 7 :
					// JPA2.g:267:7: collection_member_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression2355);
					collection_member_expression222=collection_member_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression222.getTree());

					}
					break;
				case 8 :
					// JPA2.g:268:7: exists_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exists_expression_in_simple_cond_expression2363);
					exists_expression223=exists_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression223.getTree());

					}
					break;
				case 9 :
					// JPA2.g:269:7: date_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression2371);
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
	// JPA2.g:272:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
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
			// JPA2.g:273:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
			int alt66=5;
			switch ( input.LA(1) ) {
			case 70:
				{
				alt66=1;
				}
				break;
			case 72:
				{
				alt66=2;
				}
				break;
			case 71:
				{
				alt66=3;
				}
				break;
			case 73:
				{
				alt66=4;
				}
				break;
			case 74:
				{
				alt66=5;
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
					// JPA2.g:273:7: date_between_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2384);
					date_between_macro_expression225=date_between_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression225.getTree());

					}
					break;
				case 2 :
					// JPA2.g:274:7: date_before_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2392);
					date_before_macro_expression226=date_before_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression226.getTree());

					}
					break;
				case 3 :
					// JPA2.g:275:7: date_after_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2400);
					date_after_macro_expression227=date_after_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression227.getTree());

					}
					break;
				case 4 :
					// JPA2.g:276:7: date_equals_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2408);
					date_equals_macro_expression228=date_equals_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression228.getTree());

					}
					break;
				case 5 :
					// JPA2.g:277:7: date_today_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2416);
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
	// JPA2.g:279:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
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
			// JPA2.g:280:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
			// JPA2.g:280:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal230=(Token)match(input,70,FOLLOW_70_in_date_between_macro_expression2428); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal230_tree = (Object)adaptor.create(string_literal230);
			adaptor.addChild(root_0, string_literal230_tree);
			}

			char_literal231=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2430); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal231_tree = (Object)adaptor.create(char_literal231);
			adaptor.addChild(root_0, char_literal231_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2432);
			path_expression232=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression232.getTree());

			char_literal233=(Token)match(input,58,FOLLOW_58_in_date_between_macro_expression2434); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal233_tree = (Object)adaptor.create(char_literal233);
			adaptor.addChild(root_0, char_literal233_tree);
			}

			string_literal234=(Token)match(input,110,FOLLOW_110_in_date_between_macro_expression2436); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal234_tree = (Object)adaptor.create(string_literal234);
			adaptor.addChild(root_0, string_literal234_tree);
			}

			// JPA2.g:280:48: ( ( '+' | '-' ) numeric_literal )?
			int alt67=2;
			int LA67_0 = input.LA(1);
			if ( (LA67_0==57||LA67_0==59) ) {
				alt67=1;
			}
			switch (alt67) {
				case 1 :
					// JPA2.g:280:49: ( '+' | '-' ) numeric_literal
					{
					set235=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2447);
					numeric_literal236=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal236.getTree());

					}
					break;

			}

			char_literal237=(Token)match(input,58,FOLLOW_58_in_date_between_macro_expression2451); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal237_tree = (Object)adaptor.create(char_literal237);
			adaptor.addChild(root_0, char_literal237_tree);
			}

			string_literal238=(Token)match(input,110,FOLLOW_110_in_date_between_macro_expression2453); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal238_tree = (Object)adaptor.create(string_literal238);
			adaptor.addChild(root_0, string_literal238_tree);
			}

			// JPA2.g:280:89: ( ( '+' | '-' ) numeric_literal )?
			int alt68=2;
			int LA68_0 = input.LA(1);
			if ( (LA68_0==57||LA68_0==59) ) {
				alt68=1;
			}
			switch (alt68) {
				case 1 :
					// JPA2.g:280:90: ( '+' | '-' ) numeric_literal
					{
					set239=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2464);
					numeric_literal240=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal240.getTree());

					}
					break;

			}

			char_literal241=(Token)match(input,58,FOLLOW_58_in_date_between_macro_expression2468); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal241_tree = (Object)adaptor.create(char_literal241);
			adaptor.addChild(root_0, char_literal241_tree);
			}

			set242=input.LT(1);
			if ( input.LA(1)==87||input.LA(1)==97||input.LA(1)==106||input.LA(1)==108||input.LA(1)==116||input.LA(1)==133 ) {
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
			char_literal243=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2493); if (state.failed) return retval;
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
	// JPA2.g:282:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
			// JPA2.g:283:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:283:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal244=(Token)match(input,72,FOLLOW_72_in_date_before_macro_expression2505); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal244_tree = (Object)adaptor.create(string_literal244);
			adaptor.addChild(root_0, string_literal244_tree);
			}

			char_literal245=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2507); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal245_tree = (Object)adaptor.create(char_literal245);
			adaptor.addChild(root_0, char_literal245_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2509);
			path_expression246=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression246.getTree());

			char_literal247=(Token)match(input,58,FOLLOW_58_in_date_before_macro_expression2511); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal247_tree = (Object)adaptor.create(char_literal247);
			adaptor.addChild(root_0, char_literal247_tree);
			}

			// JPA2.g:283:45: ( path_expression | input_parameter )
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
					// JPA2.g:283:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2514);
					path_expression248=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression248.getTree());

					}
					break;
				case 2 :
					// JPA2.g:283:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2518);
					input_parameter249=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter249.getTree());

					}
					break;

			}

			char_literal250=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2521); if (state.failed) return retval;
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
	// JPA2.g:285:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
			// JPA2.g:286:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:286:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal251=(Token)match(input,71,FOLLOW_71_in_date_after_macro_expression2533); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal251_tree = (Object)adaptor.create(string_literal251);
			adaptor.addChild(root_0, string_literal251_tree);
			}

			char_literal252=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2535); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal252_tree = (Object)adaptor.create(char_literal252);
			adaptor.addChild(root_0, char_literal252_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2537);
			path_expression253=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression253.getTree());

			char_literal254=(Token)match(input,58,FOLLOW_58_in_date_after_macro_expression2539); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal254_tree = (Object)adaptor.create(char_literal254);
			adaptor.addChild(root_0, char_literal254_tree);
			}

			// JPA2.g:286:44: ( path_expression | input_parameter )
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
					// JPA2.g:286:45: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2542);
					path_expression255=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression255.getTree());

					}
					break;
				case 2 :
					// JPA2.g:286:63: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2546);
					input_parameter256=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter256.getTree());

					}
					break;

			}

			char_literal257=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2549); if (state.failed) return retval;
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
	// JPA2.g:288:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
			// JPA2.g:289:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:289:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal258=(Token)match(input,73,FOLLOW_73_in_date_equals_macro_expression2561); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal258_tree = (Object)adaptor.create(string_literal258);
			adaptor.addChild(root_0, string_literal258_tree);
			}

			char_literal259=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2563); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal259_tree = (Object)adaptor.create(char_literal259);
			adaptor.addChild(root_0, char_literal259_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2565);
			path_expression260=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression260.getTree());

			char_literal261=(Token)match(input,58,FOLLOW_58_in_date_equals_macro_expression2567); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal261_tree = (Object)adaptor.create(char_literal261);
			adaptor.addChild(root_0, char_literal261_tree);
			}

			// JPA2.g:289:45: ( path_expression | input_parameter )
			int alt71=2;
			int LA71_0 = input.LA(1);
			if ( (LA71_0==WORD) ) {
				alt71=1;
			}
			else if ( (LA71_0==NAMED_PARAMETER||LA71_0==55||LA71_0==69) ) {
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
					pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2570);
					path_expression262=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression262.getTree());

					}
					break;
				case 2 :
					// JPA2.g:289:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2574);
					input_parameter263=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter263.getTree());

					}
					break;

			}

			char_literal264=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2577); if (state.failed) return retval;
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
	// JPA2.g:291:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
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
			// JPA2.g:292:5: ( '@TODAY' '(' path_expression ')' )
			// JPA2.g:292:7: '@TODAY' '(' path_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal265=(Token)match(input,74,FOLLOW_74_in_date_today_macro_expression2589); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal265_tree = (Object)adaptor.create(string_literal265);
			adaptor.addChild(root_0, string_literal265_tree);
			}

			char_literal266=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2591); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal266_tree = (Object)adaptor.create(char_literal266);
			adaptor.addChild(root_0, char_literal266_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2593);
			path_expression267=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression267.getTree());

			char_literal268=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2595); if (state.failed) return retval;
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
	// JPA2.g:295:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
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
			// JPA2.g:296:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
			int alt75=3;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 57:
			case 59:
			case 62:
			case 75:
			case 98:
			case 102:
			case 104:
			case 107:
			case 119:
			case 121:
				{
				alt75=1;
				}
				break;
			case WORD:
				{
				int LA75_2 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case LPAREN:
				{
				int LA75_5 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case 69:
				{
				int LA75_6 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA75_7 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case 55:
				{
				int LA75_8 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case COUNT:
				{
				int LA75_16 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA75_17 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case 96:
				{
				int LA75_18 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case 81:
				{
				int LA75_19 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case 82:
				{
				int LA75_20 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case 112:
				{
				int LA75_21 = input.LA(2);
				if ( (synpred114_JPA2()) ) {
					alt75=1;
				}
				else if ( (synpred116_JPA2()) ) {
					alt75=2;
				}
				else if ( (true) ) {
					alt75=3;
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 83:
			case 122:
			case 126:
			case 129:
				{
				alt75=2;
				}
				break;
			case 84:
			case 85:
			case 86:
				{
				alt75=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 75, 0, input);
				throw nvae;
			}
			switch (alt75) {
				case 1 :
					// JPA2.g:296:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2608);
					arithmetic_expression269=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression269.getTree());

					// JPA2.g:296:29: ( 'NOT' )?
					int alt72=2;
					int LA72_0 = input.LA(1);
					if ( (LA72_0==NOT) ) {
						alt72=1;
					}
					switch (alt72) {
						case 1 :
							// JPA2.g:296:30: 'NOT'
							{
							string_literal270=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2611); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal270_tree = (Object)adaptor.create(string_literal270);
							adaptor.addChild(root_0, string_literal270_tree);
							}

							}
							break;

					}

					string_literal271=(Token)match(input,79,FOLLOW_79_in_between_expression2615); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal271_tree = (Object)adaptor.create(string_literal271);
					adaptor.addChild(root_0, string_literal271_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2617);
					arithmetic_expression272=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression272.getTree());

					string_literal273=(Token)match(input,AND,FOLLOW_AND_in_between_expression2619); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal273_tree = (Object)adaptor.create(string_literal273);
					adaptor.addChild(root_0, string_literal273_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2621);
					arithmetic_expression274=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression274.getTree());

					}
					break;
				case 2 :
					// JPA2.g:297:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_between_expression2629);
					string_expression275=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression275.getTree());

					// JPA2.g:297:25: ( 'NOT' )?
					int alt73=2;
					int LA73_0 = input.LA(1);
					if ( (LA73_0==NOT) ) {
						alt73=1;
					}
					switch (alt73) {
						case 1 :
							// JPA2.g:297:26: 'NOT'
							{
							string_literal276=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2632); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal276_tree = (Object)adaptor.create(string_literal276);
							adaptor.addChild(root_0, string_literal276_tree);
							}

							}
							break;

					}

					string_literal277=(Token)match(input,79,FOLLOW_79_in_between_expression2636); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal277_tree = (Object)adaptor.create(string_literal277);
					adaptor.addChild(root_0, string_literal277_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2638);
					string_expression278=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression278.getTree());

					string_literal279=(Token)match(input,AND,FOLLOW_AND_in_between_expression2640); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal279_tree = (Object)adaptor.create(string_literal279);
					adaptor.addChild(root_0, string_literal279_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2642);
					string_expression280=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression280.getTree());

					}
					break;
				case 3 :
					// JPA2.g:298:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_between_expression2650);
					datetime_expression281=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression281.getTree());

					// JPA2.g:298:27: ( 'NOT' )?
					int alt74=2;
					int LA74_0 = input.LA(1);
					if ( (LA74_0==NOT) ) {
						alt74=1;
					}
					switch (alt74) {
						case 1 :
							// JPA2.g:298:28: 'NOT'
							{
							string_literal282=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2653); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal282_tree = (Object)adaptor.create(string_literal282);
							adaptor.addChild(root_0, string_literal282_tree);
							}

							}
							break;

					}

					string_literal283=(Token)match(input,79,FOLLOW_79_in_between_expression2657); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal283_tree = (Object)adaptor.create(string_literal283);
					adaptor.addChild(root_0, string_literal283_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2659);
					datetime_expression284=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression284.getTree());

					string_literal285=(Token)match(input,AND,FOLLOW_AND_in_between_expression2661); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal285_tree = (Object)adaptor.create(string_literal285);
					adaptor.addChild(root_0, string_literal285_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2663);
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
	// JPA2.g:299:1: in_expression : ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) ;
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
			// JPA2.g:300:5: ( ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) )
			// JPA2.g:300:7: ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:300:7: ( path_expression | type_discriminator | identification_variable )
			int alt76=3;
			int LA76_0 = input.LA(1);
			if ( (LA76_0==WORD) ) {
				int LA76_1 = input.LA(2);
				if ( (LA76_1==60) ) {
					alt76=1;
				}
				else if ( (LA76_1==IN||LA76_1==NOT) ) {
					alt76=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 76, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA76_0==127) ) {
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
					// JPA2.g:300:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_in_expression2675);
					path_expression287=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression287.getTree());

					}
					break;
				case 2 :
					// JPA2.g:300:26: type_discriminator
					{
					pushFollow(FOLLOW_type_discriminator_in_in_expression2679);
					type_discriminator288=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator288.getTree());

					}
					break;
				case 3 :
					// JPA2.g:300:47: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_in_expression2683);
					identification_variable289=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable289.getTree());

					}
					break;

			}

			// JPA2.g:300:72: ( NOT )?
			int alt77=2;
			int LA77_0 = input.LA(1);
			if ( (LA77_0==NOT) ) {
				alt77=1;
			}
			switch (alt77) {
				case 1 :
					// JPA2.g:300:73: NOT
					{
					NOT290=(Token)match(input,NOT,FOLLOW_NOT_in_in_expression2687); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT290_tree = (Object)adaptor.create(NOT290);
					adaptor.addChild(root_0, NOT290_tree);
					}

					}
					break;

			}

			IN291=(Token)match(input,IN,FOLLOW_IN_in_in_expression2691); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN291_tree = (Object)adaptor.create(IN291);
			adaptor.addChild(root_0, IN291_tree);
			}

			// JPA2.g:301:13: ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			int alt79=4;
			int LA79_0 = input.LA(1);
			if ( (LA79_0==LPAREN) ) {
				switch ( input.LA(2) ) {
				case 117:
					{
					alt79=2;
					}
					break;
				case WORD:
					{
					int LA79_4 = input.LA(3);
					if ( (LA79_4==RPAREN||LA79_4==58) ) {
						alt79=1;
					}
					else if ( (LA79_4==60) ) {
						alt79=4;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 79, 4, input);
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
					alt79=1;
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 79, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}
			else if ( (LA79_0==NAMED_PARAMETER||LA79_0==55||LA79_0==69) ) {
				alt79=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 79, 0, input);
				throw nvae;
			}

			switch (alt79) {
				case 1 :
					// JPA2.g:301:15: '(' in_item ( ',' in_item )* ')'
					{
					char_literal292=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2707); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal292_tree = (Object)adaptor.create(char_literal292);
					adaptor.addChild(root_0, char_literal292_tree);
					}

					pushFollow(FOLLOW_in_item_in_in_expression2709);
					in_item293=in_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item293.getTree());

					// JPA2.g:301:27: ( ',' in_item )*
					loop78:
					while (true) {
						int alt78=2;
						int LA78_0 = input.LA(1);
						if ( (LA78_0==58) ) {
							alt78=1;
						}

						switch (alt78) {
						case 1 :
							// JPA2.g:301:28: ',' in_item
							{
							char_literal294=(Token)match(input,58,FOLLOW_58_in_in_expression2712); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal294_tree = (Object)adaptor.create(char_literal294);
							adaptor.addChild(root_0, char_literal294_tree);
							}

							pushFollow(FOLLOW_in_item_in_in_expression2714);
							in_item295=in_item();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item295.getTree());

							}
							break;

						default :
							break loop78;
						}
					}

					char_literal296=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2718); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal296_tree = (Object)adaptor.create(char_literal296);
					adaptor.addChild(root_0, char_literal296_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:302:15: subquery
					{
					pushFollow(FOLLOW_subquery_in_in_expression2734);
					subquery297=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery297.getTree());

					}
					break;
				case 3 :
					// JPA2.g:303:15: collection_valued_input_parameter
					{
					pushFollow(FOLLOW_collection_valued_input_parameter_in_in_expression2750);
					collection_valued_input_parameter298=collection_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_input_parameter298.getTree());

					}
					break;
				case 4 :
					// JPA2.g:304:15: '(' path_expression ')'
					{
					char_literal299=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2766); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal299_tree = (Object)adaptor.create(char_literal299);
					adaptor.addChild(root_0, char_literal299_tree);
					}

					pushFollow(FOLLOW_path_expression_in_in_expression2768);
					path_expression300=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression300.getTree());

					char_literal301=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2770); if (state.failed) return retval;
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
	// JPA2.g:310:1: in_item : ( literal | single_valued_input_parameter );
	public final JPA2Parser.in_item_return in_item() throws RecognitionException {
		JPA2Parser.in_item_return retval = new JPA2Parser.in_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal302 =null;
		ParserRuleReturnScope single_valued_input_parameter303 =null;


		try {
			// JPA2.g:311:5: ( literal | single_valued_input_parameter )
			int alt80=2;
			int LA80_0 = input.LA(1);
			if ( (LA80_0==WORD) ) {
				alt80=1;
			}
			else if ( (LA80_0==NAMED_PARAMETER||LA80_0==55||LA80_0==69) ) {
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
					// JPA2.g:311:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_in_item2798);
					literal302=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal302.getTree());

					}
					break;
				case 2 :
					// JPA2.g:311:17: single_valued_input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_single_valued_input_parameter_in_in_item2802);
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
	// JPA2.g:312:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? ;
	public final JPA2Parser.like_expression_return like_expression() throws RecognitionException {
		JPA2Parser.like_expression_return retval = new JPA2Parser.like_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal305=null;
		Token string_literal306=null;
		Token string_literal309=null;
		ParserRuleReturnScope string_expression304 =null;
		ParserRuleReturnScope pattern_value307 =null;
		ParserRuleReturnScope input_parameter308 =null;
		ParserRuleReturnScope escape_character310 =null;

		Object string_literal305_tree=null;
		Object string_literal306_tree=null;
		Object string_literal309_tree=null;

		try {
			// JPA2.g:313:5: ( string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? )
			// JPA2.g:313:7: string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_expression_in_like_expression2813);
			string_expression304=string_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression304.getTree());

			// JPA2.g:313:25: ( 'NOT' )?
			int alt81=2;
			int LA81_0 = input.LA(1);
			if ( (LA81_0==NOT) ) {
				alt81=1;
			}
			switch (alt81) {
				case 1 :
					// JPA2.g:313:26: 'NOT'
					{
					string_literal305=(Token)match(input,NOT,FOLLOW_NOT_in_like_expression2816); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal305_tree = (Object)adaptor.create(string_literal305);
					adaptor.addChild(root_0, string_literal305_tree);
					}

					}
					break;

			}

			string_literal306=(Token)match(input,103,FOLLOW_103_in_like_expression2820); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal306_tree = (Object)adaptor.create(string_literal306);
			adaptor.addChild(root_0, string_literal306_tree);
			}

			// JPA2.g:313:41: ( pattern_value | input_parameter )
			int alt82=2;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==STRING_LITERAL) ) {
				alt82=1;
			}
			else if ( (LA82_0==NAMED_PARAMETER||LA82_0==55||LA82_0==69) ) {
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
					// JPA2.g:313:42: pattern_value
					{
					pushFollow(FOLLOW_pattern_value_in_like_expression2823);
					pattern_value307=pattern_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value307.getTree());

					}
					break;
				case 2 :
					// JPA2.g:313:58: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_like_expression2827);
					input_parameter308=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter308.getTree());

					}
					break;

			}

			// JPA2.g:313:74: ( 'ESCAPE' escape_character )?
			int alt83=2;
			int LA83_0 = input.LA(1);
			if ( (LA83_0==93) ) {
				alt83=1;
			}
			switch (alt83) {
				case 1 :
					// JPA2.g:313:75: 'ESCAPE' escape_character
					{
					string_literal309=(Token)match(input,93,FOLLOW_93_in_like_expression2830); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal309_tree = (Object)adaptor.create(string_literal309);
					adaptor.addChild(root_0, string_literal309_tree);
					}

					pushFollow(FOLLOW_escape_character_in_like_expression2832);
					escape_character310=escape_character();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, escape_character310.getTree());

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
	// JPA2.g:314:1: null_comparison_expression : ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' ;
	public final JPA2Parser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
		JPA2Parser.null_comparison_expression_return retval = new JPA2Parser.null_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal314=null;
		Token string_literal315=null;
		Token string_literal316=null;
		ParserRuleReturnScope path_expression311 =null;
		ParserRuleReturnScope input_parameter312 =null;
		ParserRuleReturnScope join_association_path_expression313 =null;

		Object string_literal314_tree=null;
		Object string_literal315_tree=null;
		Object string_literal316_tree=null;

		try {
			// JPA2.g:315:5: ( ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' )
			// JPA2.g:315:7: ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:315:7: ( path_expression | input_parameter | join_association_path_expression )
			int alt84=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA84_1 = input.LA(2);
				if ( (LA84_1==60) ) {
					int LA84_4 = input.LA(3);
					if ( (synpred129_JPA2()) ) {
						alt84=1;
					}
					else if ( (true) ) {
						alt84=3;
					}

				}
				else if ( (LA84_1==99) ) {
					alt84=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 84, 1, input);
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
				alt84=2;
				}
				break;
			case 125:
				{
				alt84=3;
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
					// JPA2.g:315:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_null_comparison_expression2846);
					path_expression311=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression311.getTree());

					}
					break;
				case 2 :
					// JPA2.g:315:26: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_null_comparison_expression2850);
					input_parameter312=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter312.getTree());

					}
					break;
				case 3 :
					// JPA2.g:315:44: join_association_path_expression
					{
					pushFollow(FOLLOW_join_association_path_expression_in_null_comparison_expression2854);
					join_association_path_expression313=join_association_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression313.getTree());

					}
					break;

			}

			string_literal314=(Token)match(input,99,FOLLOW_99_in_null_comparison_expression2857); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal314_tree = (Object)adaptor.create(string_literal314);
			adaptor.addChild(root_0, string_literal314_tree);
			}

			// JPA2.g:315:83: ( 'NOT' )?
			int alt85=2;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==NOT) ) {
				alt85=1;
			}
			switch (alt85) {
				case 1 :
					// JPA2.g:315:84: 'NOT'
					{
					string_literal315=(Token)match(input,NOT,FOLLOW_NOT_in_null_comparison_expression2860); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal315_tree = (Object)adaptor.create(string_literal315);
					adaptor.addChild(root_0, string_literal315_tree);
					}

					}
					break;

			}

			string_literal316=(Token)match(input,111,FOLLOW_111_in_null_comparison_expression2864); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal316_tree = (Object)adaptor.create(string_literal316);
			adaptor.addChild(root_0, string_literal316_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:316:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
	public final JPA2Parser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
		JPA2Parser.empty_collection_comparison_expression_return retval = new JPA2Parser.empty_collection_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal318=null;
		Token string_literal319=null;
		Token string_literal320=null;
		ParserRuleReturnScope path_expression317 =null;

		Object string_literal318_tree=null;
		Object string_literal319_tree=null;
		Object string_literal320_tree=null;

		try {
			// JPA2.g:317:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
			// JPA2.g:317:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression2875);
			path_expression317=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression317.getTree());

			string_literal318=(Token)match(input,99,FOLLOW_99_in_empty_collection_comparison_expression2877); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal318_tree = (Object)adaptor.create(string_literal318);
			adaptor.addChild(root_0, string_literal318_tree);
			}

			// JPA2.g:317:28: ( 'NOT' )?
			int alt86=2;
			int LA86_0 = input.LA(1);
			if ( (LA86_0==NOT) ) {
				alt86=1;
			}
			switch (alt86) {
				case 1 :
					// JPA2.g:317:29: 'NOT'
					{
					string_literal319=(Token)match(input,NOT,FOLLOW_NOT_in_empty_collection_comparison_expression2880); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal319_tree = (Object)adaptor.create(string_literal319);
					adaptor.addChild(root_0, string_literal319_tree);
					}

					}
					break;

			}

			string_literal320=(Token)match(input,90,FOLLOW_90_in_empty_collection_comparison_expression2884); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal320_tree = (Object)adaptor.create(string_literal320);
			adaptor.addChild(root_0, string_literal320_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:318:1: collection_member_expression : entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
	public final JPA2Parser.collection_member_expression_return collection_member_expression() throws RecognitionException {
		JPA2Parser.collection_member_expression_return retval = new JPA2Parser.collection_member_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal322=null;
		Token string_literal323=null;
		Token string_literal324=null;
		ParserRuleReturnScope entity_or_value_expression321 =null;
		ParserRuleReturnScope path_expression325 =null;

		Object string_literal322_tree=null;
		Object string_literal323_tree=null;
		Object string_literal324_tree=null;

		try {
			// JPA2.g:319:5: ( entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
			// JPA2.g:319:7: entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_or_value_expression_in_collection_member_expression2895);
			entity_or_value_expression321=entity_or_value_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_or_value_expression321.getTree());

			// JPA2.g:319:35: ( 'NOT' )?
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==NOT) ) {
				alt87=1;
			}
			switch (alt87) {
				case 1 :
					// JPA2.g:319:36: 'NOT'
					{
					string_literal322=(Token)match(input,NOT,FOLLOW_NOT_in_collection_member_expression2899); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal322_tree = (Object)adaptor.create(string_literal322);
					adaptor.addChild(root_0, string_literal322_tree);
					}

					}
					break;

			}

			string_literal323=(Token)match(input,105,FOLLOW_105_in_collection_member_expression2903); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal323_tree = (Object)adaptor.create(string_literal323);
			adaptor.addChild(root_0, string_literal323_tree);
			}

			// JPA2.g:319:53: ( 'OF' )?
			int alt88=2;
			int LA88_0 = input.LA(1);
			if ( (LA88_0==114) ) {
				alt88=1;
			}
			switch (alt88) {
				case 1 :
					// JPA2.g:319:54: 'OF'
					{
					string_literal324=(Token)match(input,114,FOLLOW_114_in_collection_member_expression2906); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal324_tree = (Object)adaptor.create(string_literal324);
					adaptor.addChild(root_0, string_literal324_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_path_expression_in_collection_member_expression2910);
			path_expression325=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression325.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:320:1: entity_or_value_expression : ( path_expression | simple_entity_or_value_expression | subquery );
	public final JPA2Parser.entity_or_value_expression_return entity_or_value_expression() throws RecognitionException {
		JPA2Parser.entity_or_value_expression_return retval = new JPA2Parser.entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression326 =null;
		ParserRuleReturnScope simple_entity_or_value_expression327 =null;
		ParserRuleReturnScope subquery328 =null;


		try {
			// JPA2.g:321:5: ( path_expression | simple_entity_or_value_expression | subquery )
			int alt89=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA89_1 = input.LA(2);
				if ( (LA89_1==60) ) {
					alt89=1;
				}
				else if ( (LA89_1==NOT||LA89_1==105) ) {
					alt89=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 89, 1, input);
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
				alt89=2;
				}
				break;
			case LPAREN:
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
					// JPA2.g:321:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_or_value_expression2921);
					path_expression326=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression326.getTree());

					}
					break;
				case 2 :
					// JPA2.g:322:7: simple_entity_or_value_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2929);
					simple_entity_or_value_expression327=simple_entity_or_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_or_value_expression327.getTree());

					}
					break;
				case 3 :
					// JPA2.g:323:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_entity_or_value_expression2937);
					subquery328=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery328.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:324:1: simple_entity_or_value_expression : ( identification_variable | input_parameter | literal );
	public final JPA2Parser.simple_entity_or_value_expression_return simple_entity_or_value_expression() throws RecognitionException {
		JPA2Parser.simple_entity_or_value_expression_return retval = new JPA2Parser.simple_entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable329 =null;
		ParserRuleReturnScope input_parameter330 =null;
		ParserRuleReturnScope literal331 =null;


		try {
			// JPA2.g:325:5: ( identification_variable | input_parameter | literal )
			int alt90=3;
			int LA90_0 = input.LA(1);
			if ( (LA90_0==WORD) ) {
				int LA90_1 = input.LA(2);
				if ( (synpred137_JPA2()) ) {
					alt90=1;
				}
				else if ( (true) ) {
					alt90=3;
				}

			}
			else if ( (LA90_0==NAMED_PARAMETER||LA90_0==55||LA90_0==69) ) {
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
					// JPA2.g:325:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_or_value_expression2948);
					identification_variable329=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable329.getTree());

					}
					break;
				case 2 :
					// JPA2.g:326:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_or_value_expression2956);
					input_parameter330=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter330.getTree());

					}
					break;
				case 3 :
					// JPA2.g:327:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_simple_entity_or_value_expression2964);
					literal331=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal331.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:328:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
	public final JPA2Parser.exists_expression_return exists_expression() throws RecognitionException {
		JPA2Parser.exists_expression_return retval = new JPA2Parser.exists_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal332=null;
		Token string_literal333=null;
		ParserRuleReturnScope subquery334 =null;

		Object string_literal332_tree=null;
		Object string_literal333_tree=null;

		try {
			// JPA2.g:329:5: ( ( 'NOT' )? 'EXISTS' subquery )
			// JPA2.g:329:7: ( 'NOT' )? 'EXISTS' subquery
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:329:7: ( 'NOT' )?
			int alt91=2;
			int LA91_0 = input.LA(1);
			if ( (LA91_0==NOT) ) {
				alt91=1;
			}
			switch (alt91) {
				case 1 :
					// JPA2.g:329:8: 'NOT'
					{
					string_literal332=(Token)match(input,NOT,FOLLOW_NOT_in_exists_expression2976); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal332_tree = (Object)adaptor.create(string_literal332);
					adaptor.addChild(root_0, string_literal332_tree);
					}

					}
					break;

			}

			string_literal333=(Token)match(input,94,FOLLOW_94_in_exists_expression2980); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal333_tree = (Object)adaptor.create(string_literal333);
			adaptor.addChild(root_0, string_literal333_tree);
			}

			pushFollow(FOLLOW_subquery_in_exists_expression2982);
			subquery334=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery334.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:330:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
	public final JPA2Parser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
		JPA2Parser.all_or_any_expression_return retval = new JPA2Parser.all_or_any_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set335=null;
		ParserRuleReturnScope subquery336 =null;

		Object set335_tree=null;

		try {
			// JPA2.g:331:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
			// JPA2.g:331:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
			{
			root_0 = (Object)adaptor.nil();


			set335=input.LT(1);
			if ( (input.LA(1) >= 76 && input.LA(1) <= 77)||input.LA(1)==120 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set335));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_subquery_in_all_or_any_expression3006);
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
	// $ANTLR end "all_or_any_expression"


	public static class comparison_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "comparison_expression"
	// JPA2.g:332:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
	public final JPA2Parser.comparison_expression_return comparison_expression() throws RecognitionException {
		JPA2Parser.comparison_expression_return retval = new JPA2Parser.comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set342=null;
		Token set346=null;
		Token set354=null;
		Token set358=null;
		ParserRuleReturnScope string_expression337 =null;
		ParserRuleReturnScope comparison_operator338 =null;
		ParserRuleReturnScope string_expression339 =null;
		ParserRuleReturnScope all_or_any_expression340 =null;
		ParserRuleReturnScope boolean_expression341 =null;
		ParserRuleReturnScope boolean_expression343 =null;
		ParserRuleReturnScope all_or_any_expression344 =null;
		ParserRuleReturnScope enum_expression345 =null;
		ParserRuleReturnScope enum_expression347 =null;
		ParserRuleReturnScope all_or_any_expression348 =null;
		ParserRuleReturnScope datetime_expression349 =null;
		ParserRuleReturnScope comparison_operator350 =null;
		ParserRuleReturnScope datetime_expression351 =null;
		ParserRuleReturnScope all_or_any_expression352 =null;
		ParserRuleReturnScope entity_expression353 =null;
		ParserRuleReturnScope entity_expression355 =null;
		ParserRuleReturnScope all_or_any_expression356 =null;
		ParserRuleReturnScope entity_type_expression357 =null;
		ParserRuleReturnScope entity_type_expression359 =null;
		ParserRuleReturnScope arithmetic_expression360 =null;
		ParserRuleReturnScope comparison_operator361 =null;
		ParserRuleReturnScope arithmetic_expression362 =null;
		ParserRuleReturnScope all_or_any_expression363 =null;

		Object set342_tree=null;
		Object set346_tree=null;
		Object set354_tree=null;
		Object set358_tree=null;

		try {
			// JPA2.g:333:5: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
			int alt98=7;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA98_1 = input.LA(2);
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred149_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred151_JPA2()) ) {
					alt98=4;
				}
				else if ( (synpred154_JPA2()) ) {
					alt98=5;
				}
				else if ( (synpred156_JPA2()) ) {
					alt98=6;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 83:
			case 122:
			case 126:
			case 129:
				{
				alt98=1;
				}
				break;
			case 69:
				{
				int LA98_3 = input.LA(2);
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred149_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred151_JPA2()) ) {
					alt98=4;
				}
				else if ( (synpred154_JPA2()) ) {
					alt98=5;
				}
				else if ( (synpred156_JPA2()) ) {
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
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred149_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred151_JPA2()) ) {
					alt98=4;
				}
				else if ( (synpred154_JPA2()) ) {
					alt98=5;
				}
				else if ( (synpred156_JPA2()) ) {
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
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred149_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred151_JPA2()) ) {
					alt98=4;
				}
				else if ( (synpred154_JPA2()) ) {
					alt98=5;
				}
				else if ( (synpred156_JPA2()) ) {
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
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred151_JPA2()) ) {
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
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred151_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 96:
				{
				int LA98_13 = input.LA(2);
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred151_JPA2()) ) {
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
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred149_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred151_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 82:
				{
				int LA98_15 = input.LA(2);
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred149_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred151_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 112:
				{
				int LA98_16 = input.LA(2);
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred149_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred151_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA98_17 = input.LA(2);
				if ( (synpred143_JPA2()) ) {
					alt98=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt98=2;
				}
				else if ( (synpred149_JPA2()) ) {
					alt98=3;
				}
				else if ( (synpred151_JPA2()) ) {
					alt98=4;
				}
				else if ( (true) ) {
					alt98=7;
				}

				}
				break;
			case 134:
			case 135:
				{
				alt98=2;
				}
				break;
			case 84:
			case 85:
			case 86:
				{
				alt98=4;
				}
				break;
			case 127:
				{
				alt98=6;
				}
				break;
			case INT_NUMERAL:
			case 57:
			case 59:
			case 62:
			case 75:
			case 98:
			case 102:
			case 104:
			case 107:
			case 119:
			case 121:
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
					// JPA2.g:333:7: string_expression comparison_operator ( string_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_comparison_expression3017);
					string_expression337=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression337.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3019);
					comparison_operator338=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator338.getTree());

					// JPA2.g:333:45: ( string_expression | all_or_any_expression )
					int alt92=2;
					int LA92_0 = input.LA(1);
					if ( (LA92_0==AVG||LA92_0==COUNT||(LA92_0 >= LOWER && LA92_0 <= NAMED_PARAMETER)||(LA92_0 >= STRING_LITERAL && LA92_0 <= SUM)||LA92_0==WORD||LA92_0==55||LA92_0==69||(LA92_0 >= 81 && LA92_0 <= 83)||LA92_0==96||LA92_0==112||LA92_0==122||LA92_0==126||LA92_0==129) ) {
						alt92=1;
					}
					else if ( ((LA92_0 >= 76 && LA92_0 <= 77)||LA92_0==120) ) {
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
							// JPA2.g:333:46: string_expression
							{
							pushFollow(FOLLOW_string_expression_in_comparison_expression3022);
							string_expression339=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression339.getTree());

							}
							break;
						case 2 :
							// JPA2.g:333:66: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3026);
							all_or_any_expression340=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression340.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:334:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_comparison_expression3035);
					boolean_expression341=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression341.getTree());

					set342=input.LT(1);
					if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
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
					// JPA2.g:334:39: ( boolean_expression | all_or_any_expression )
					int alt93=2;
					int LA93_0 = input.LA(1);
					if ( (LA93_0==LPAREN||LA93_0==NAMED_PARAMETER||LA93_0==WORD||LA93_0==55||LA93_0==69||(LA93_0 >= 81 && LA93_0 <= 82)||LA93_0==96||LA93_0==112||(LA93_0 >= 134 && LA93_0 <= 135)) ) {
						alt93=1;
					}
					else if ( ((LA93_0 >= 76 && LA93_0 <= 77)||LA93_0==120) ) {
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
							// JPA2.g:334:40: boolean_expression
							{
							pushFollow(FOLLOW_boolean_expression_in_comparison_expression3046);
							boolean_expression343=boolean_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression343.getTree());

							}
							break;
						case 2 :
							// JPA2.g:334:61: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3050);
							all_or_any_expression344=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression344.getTree());

							}
							break;

					}

					}
					break;
				case 3 :
					// JPA2.g:335:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_comparison_expression3059);
					enum_expression345=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression345.getTree());

					set346=input.LT(1);
					if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set346));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:335:34: ( enum_expression | all_or_any_expression )
					int alt94=2;
					int LA94_0 = input.LA(1);
					if ( (LA94_0==LPAREN||LA94_0==NAMED_PARAMETER||LA94_0==WORD||LA94_0==55||LA94_0==69||(LA94_0 >= 81 && LA94_0 <= 82)||LA94_0==112) ) {
						alt94=1;
					}
					else if ( ((LA94_0 >= 76 && LA94_0 <= 77)||LA94_0==120) ) {
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
							// JPA2.g:335:35: enum_expression
							{
							pushFollow(FOLLOW_enum_expression_in_comparison_expression3068);
							enum_expression347=enum_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression347.getTree());

							}
							break;
						case 2 :
							// JPA2.g:335:53: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3072);
							all_or_any_expression348=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression348.getTree());

							}
							break;

					}

					}
					break;
				case 4 :
					// JPA2.g:336:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_comparison_expression3081);
					datetime_expression349=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression349.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3083);
					comparison_operator350=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator350.getTree());

					// JPA2.g:336:47: ( datetime_expression | all_or_any_expression )
					int alt95=2;
					int LA95_0 = input.LA(1);
					if ( (LA95_0==AVG||LA95_0==COUNT||(LA95_0 >= LPAREN && LA95_0 <= NAMED_PARAMETER)||LA95_0==SUM||LA95_0==WORD||LA95_0==55||LA95_0==69||(LA95_0 >= 81 && LA95_0 <= 82)||(LA95_0 >= 84 && LA95_0 <= 86)||LA95_0==96||LA95_0==112) ) {
						alt95=1;
					}
					else if ( ((LA95_0 >= 76 && LA95_0 <= 77)||LA95_0==120) ) {
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
							// JPA2.g:336:48: datetime_expression
							{
							pushFollow(FOLLOW_datetime_expression_in_comparison_expression3086);
							datetime_expression351=datetime_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression351.getTree());

							}
							break;
						case 2 :
							// JPA2.g:336:70: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3090);
							all_or_any_expression352=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression352.getTree());

							}
							break;

					}

					}
					break;
				case 5 :
					// JPA2.g:337:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_expression_in_comparison_expression3099);
					entity_expression353=entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression353.getTree());

					set354=input.LT(1);
					if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
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
					// JPA2.g:337:38: ( entity_expression | all_or_any_expression )
					int alt96=2;
					int LA96_0 = input.LA(1);
					if ( (LA96_0==NAMED_PARAMETER||LA96_0==WORD||LA96_0==55||LA96_0==69) ) {
						alt96=1;
					}
					else if ( ((LA96_0 >= 76 && LA96_0 <= 77)||LA96_0==120) ) {
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
							// JPA2.g:337:39: entity_expression
							{
							pushFollow(FOLLOW_entity_expression_in_comparison_expression3110);
							entity_expression355=entity_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression355.getTree());

							}
							break;
						case 2 :
							// JPA2.g:337:59: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3114);
							all_or_any_expression356=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression356.getTree());

							}
							break;

					}

					}
					break;
				case 6 :
					// JPA2.g:338:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3123);
					entity_type_expression357=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression357.getTree());

					set358=input.LT(1);
					if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set358));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3133);
					entity_type_expression359=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression359.getTree());

					}
					break;
				case 7 :
					// JPA2.g:339:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3141);
					arithmetic_expression360=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression360.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3143);
					comparison_operator361=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator361.getTree());

					// JPA2.g:339:49: ( arithmetic_expression | all_or_any_expression )
					int alt97=2;
					int LA97_0 = input.LA(1);
					if ( (LA97_0==AVG||LA97_0==COUNT||LA97_0==INT_NUMERAL||(LA97_0 >= LPAREN && LA97_0 <= NAMED_PARAMETER)||LA97_0==SUM||LA97_0==WORD||LA97_0==55||LA97_0==57||LA97_0==59||LA97_0==62||LA97_0==69||LA97_0==75||(LA97_0 >= 81 && LA97_0 <= 82)||LA97_0==96||LA97_0==98||LA97_0==102||LA97_0==104||LA97_0==107||LA97_0==112||LA97_0==119||LA97_0==121) ) {
						alt97=1;
					}
					else if ( ((LA97_0 >= 76 && LA97_0 <= 77)||LA97_0==120) ) {
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
							// JPA2.g:339:50: arithmetic_expression
							{
							pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3146);
							arithmetic_expression362=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression362.getTree());

							}
							break;
						case 2 :
							// JPA2.g:339:74: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3150);
							all_or_any_expression363=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression363.getTree());

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
	// JPA2.g:341:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
	public final JPA2Parser.comparison_operator_return comparison_operator() throws RecognitionException {
		JPA2Parser.comparison_operator_return retval = new JPA2Parser.comparison_operator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set364=null;

		Object set364_tree=null;

		try {
			// JPA2.g:342:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set364=input.LT(1);
			if ( (input.LA(1) >= 63 && input.LA(1) <= 68) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set364));
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
	// JPA2.g:348:1: arithmetic_expression : ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term );
	public final JPA2Parser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
		JPA2Parser.arithmetic_expression_return retval = new JPA2Parser.arithmetic_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set367=null;
		ParserRuleReturnScope arithmetic_term365 =null;
		ParserRuleReturnScope arithmetic_term366 =null;
		ParserRuleReturnScope arithmetic_term368 =null;

		Object set367_tree=null;

		try {
			// JPA2.g:349:5: ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term )
			int alt99=2;
			switch ( input.LA(1) ) {
			case 57:
			case 59:
				{
				int LA99_1 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 102:
				{
				int LA99_9 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 104:
				{
				int LA99_10 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 121:
				{
				int LA99_12 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 107:
				{
				int LA99_13 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 119:
				{
				int LA99_14 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 98:
				{
				int LA99_15 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 96:
				{
				int LA99_18 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
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
				if ( (synpred163_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 82:
				{
				int LA99_20 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt99=1;
				}
				else if ( (true) ) {
					alt99=2;
				}

				}
				break;
			case 112:
				{
				int LA99_21 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
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
					// JPA2.g:349:7: arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3214);
					arithmetic_term365=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term365.getTree());

					}
					break;
				case 2 :
					// JPA2.g:350:7: arithmetic_term ( '+' | '-' ) arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3222);
					arithmetic_term366=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term366.getTree());

					set367=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
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
					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3232);
					arithmetic_term368=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term368.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:351:1: arithmetic_term : ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor );
	public final JPA2Parser.arithmetic_term_return arithmetic_term() throws RecognitionException {
		JPA2Parser.arithmetic_term_return retval = new JPA2Parser.arithmetic_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set371=null;
		ParserRuleReturnScope arithmetic_factor369 =null;
		ParserRuleReturnScope arithmetic_factor370 =null;
		ParserRuleReturnScope arithmetic_factor372 =null;

		Object set371_tree=null;

		try {
			// JPA2.g:352:5: ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor )
			int alt100=2;
			switch ( input.LA(1) ) {
			case 57:
			case 59:
				{
				int LA100_1 = input.LA(2);
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 102:
				{
				int LA100_9 = input.LA(2);
				if ( (synpred165_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 104:
				{
				int LA100_10 = input.LA(2);
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 121:
				{
				int LA100_12 = input.LA(2);
				if ( (synpred165_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 107:
				{
				int LA100_13 = input.LA(2);
				if ( (synpred165_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 119:
				{
				int LA100_14 = input.LA(2);
				if ( (synpred165_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 98:
				{
				int LA100_15 = input.LA(2);
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 96:
				{
				int LA100_18 = input.LA(2);
				if ( (synpred165_JPA2()) ) {
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
				if ( (synpred165_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 82:
				{
				int LA100_20 = input.LA(2);
				if ( (synpred165_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 112:
				{
				int LA100_21 = input.LA(2);
				if ( (synpred165_JPA2()) ) {
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
					// JPA2.g:352:7: arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3243);
					arithmetic_factor369=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor369.getTree());

					}
					break;
				case 2 :
					// JPA2.g:353:7: arithmetic_factor ( '*' | '/' ) arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3251);
					arithmetic_factor370=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor370.getTree());

					set371=input.LT(1);
					if ( input.LA(1)==56||input.LA(1)==61 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set371));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3262);
					arithmetic_factor372=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor372.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:354:1: arithmetic_factor : ( ( '+' | '-' ) )? arithmetic_primary ;
	public final JPA2Parser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
		JPA2Parser.arithmetic_factor_return retval = new JPA2Parser.arithmetic_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set373=null;
		ParserRuleReturnScope arithmetic_primary374 =null;

		Object set373_tree=null;

		try {
			// JPA2.g:355:5: ( ( ( '+' | '-' ) )? arithmetic_primary )
			// JPA2.g:355:7: ( ( '+' | '-' ) )? arithmetic_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:355:7: ( ( '+' | '-' ) )?
			int alt101=2;
			int LA101_0 = input.LA(1);
			if ( (LA101_0==57||LA101_0==59) ) {
				alt101=1;
			}
			switch (alt101) {
				case 1 :
					// JPA2.g:
					{
					set373=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
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
					break;

			}

			pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor3285);
			arithmetic_primary374=arithmetic_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary374.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:356:1: arithmetic_primary : ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | subquery );
	public final JPA2Parser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
		JPA2Parser.arithmetic_primary_return retval = new JPA2Parser.arithmetic_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal377=null;
		Token char_literal379=null;
		ParserRuleReturnScope path_expression375 =null;
		ParserRuleReturnScope numeric_literal376 =null;
		ParserRuleReturnScope arithmetic_expression378 =null;
		ParserRuleReturnScope input_parameter380 =null;
		ParserRuleReturnScope functions_returning_numerics381 =null;
		ParserRuleReturnScope aggregate_expression382 =null;
		ParserRuleReturnScope case_expression383 =null;
		ParserRuleReturnScope function_invocation384 =null;
		ParserRuleReturnScope subquery385 =null;

		Object char_literal377_tree=null;
		Object char_literal379_tree=null;

		try {
			// JPA2.g:357:5: ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | subquery )
			int alt102=9;
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
				if ( (synpred171_JPA2()) ) {
					alt102=3;
				}
				else if ( (true) ) {
					alt102=9;
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
			case 98:
			case 102:
			case 104:
			case 107:
			case 119:
			case 121:
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
			case 96:
				{
				int LA102_17 = input.LA(2);
				if ( (synpred174_JPA2()) ) {
					alt102=6;
				}
				else if ( (synpred176_JPA2()) ) {
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
			case 82:
			case 112:
				{
				alt102=7;
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
					// JPA2.g:357:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_arithmetic_primary3296);
					path_expression375=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression375.getTree());

					}
					break;
				case 2 :
					// JPA2.g:358:7: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary3304);
					numeric_literal376=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal376.getTree());

					}
					break;
				case 3 :
					// JPA2.g:359:7: '(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal377=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary3312); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal377_tree = (Object)adaptor.create(char_literal377);
					adaptor.addChild(root_0, char_literal377_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_arithmetic_primary3313);
					arithmetic_expression378=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression378.getTree());

					char_literal379=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary3314); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal379_tree = (Object)adaptor.create(char_literal379);
					adaptor.addChild(root_0, char_literal379_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:360:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_arithmetic_primary3322);
					input_parameter380=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter380.getTree());

					}
					break;
				case 5 :
					// JPA2.g:361:7: functions_returning_numerics
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary3330);
					functions_returning_numerics381=functions_returning_numerics();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics381.getTree());

					}
					break;
				case 6 :
					// JPA2.g:362:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary3338);
					aggregate_expression382=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression382.getTree());

					}
					break;
				case 7 :
					// JPA2.g:363:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_arithmetic_primary3346);
					case_expression383=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression383.getTree());

					}
					break;
				case 8 :
					// JPA2.g:364:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_arithmetic_primary3354);
					function_invocation384=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation384.getTree());

					}
					break;
				case 9 :
					// JPA2.g:365:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_arithmetic_primary3362);
					subquery385=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery385.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:366:1: string_expression : ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | subquery );
	public final JPA2Parser.string_expression_return string_expression() throws RecognitionException {
		JPA2Parser.string_expression_return retval = new JPA2Parser.string_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression386 =null;
		ParserRuleReturnScope string_literal387 =null;
		ParserRuleReturnScope input_parameter388 =null;
		ParserRuleReturnScope functions_returning_strings389 =null;
		ParserRuleReturnScope aggregate_expression390 =null;
		ParserRuleReturnScope case_expression391 =null;
		ParserRuleReturnScope function_invocation392 =null;
		ParserRuleReturnScope subquery393 =null;


		try {
			// JPA2.g:367:5: ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | subquery )
			int alt103=8;
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
			case 83:
			case 122:
			case 126:
			case 129:
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
			case 96:
				{
				int LA103_13 = input.LA(2);
				if ( (synpred181_JPA2()) ) {
					alt103=5;
				}
				else if ( (synpred183_JPA2()) ) {
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
			case 82:
			case 112:
				{
				alt103=6;
				}
				break;
			case LPAREN:
				{
				alt103=8;
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


					pushFollow(FOLLOW_path_expression_in_string_expression3373);
					path_expression386=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression386.getTree());

					}
					break;
				case 2 :
					// JPA2.g:368:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_string_expression3381);
					string_literal387=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal387.getTree());

					}
					break;
				case 3 :
					// JPA2.g:369:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_string_expression3389);
					input_parameter388=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter388.getTree());

					}
					break;
				case 4 :
					// JPA2.g:370:7: functions_returning_strings
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_strings_in_string_expression3397);
					functions_returning_strings389=functions_returning_strings();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings389.getTree());

					}
					break;
				case 5 :
					// JPA2.g:371:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_string_expression3405);
					aggregate_expression390=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression390.getTree());

					}
					break;
				case 6 :
					// JPA2.g:372:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_string_expression3413);
					case_expression391=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression391.getTree());

					}
					break;
				case 7 :
					// JPA2.g:373:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_string_expression3421);
					function_invocation392=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation392.getTree());

					}
					break;
				case 8 :
					// JPA2.g:374:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_string_expression3429);
					subquery393=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery393.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:375:1: datetime_expression : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | date_time_timestamp_literal | subquery );
	public final JPA2Parser.datetime_expression_return datetime_expression() throws RecognitionException {
		JPA2Parser.datetime_expression_return retval = new JPA2Parser.datetime_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression394 =null;
		ParserRuleReturnScope input_parameter395 =null;
		ParserRuleReturnScope functions_returning_datetime396 =null;
		ParserRuleReturnScope aggregate_expression397 =null;
		ParserRuleReturnScope case_expression398 =null;
		ParserRuleReturnScope function_invocation399 =null;
		ParserRuleReturnScope date_time_timestamp_literal400 =null;
		ParserRuleReturnScope subquery401 =null;


		try {
			// JPA2.g:376:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | date_time_timestamp_literal | subquery )
			int alt104=8;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA104_1 = input.LA(2);
				if ( (synpred184_JPA2()) ) {
					alt104=1;
				}
				else if ( (synpred190_JPA2()) ) {
					alt104=7;
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
			case 84:
			case 85:
			case 86:
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
			case 96:
				{
				int LA104_8 = input.LA(2);
				if ( (synpred187_JPA2()) ) {
					alt104=4;
				}
				else if ( (synpred189_JPA2()) ) {
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
			case 82:
			case 112:
				{
				alt104=5;
				}
				break;
			case LPAREN:
				{
				alt104=8;
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
					// JPA2.g:376:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_datetime_expression3440);
					path_expression394=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression394.getTree());

					}
					break;
				case 2 :
					// JPA2.g:377:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_datetime_expression3448);
					input_parameter395=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter395.getTree());

					}
					break;
				case 3 :
					// JPA2.g:378:7: functions_returning_datetime
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_datetime_in_datetime_expression3456);
					functions_returning_datetime396=functions_returning_datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime396.getTree());

					}
					break;
				case 4 :
					// JPA2.g:379:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_datetime_expression3464);
					aggregate_expression397=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression397.getTree());

					}
					break;
				case 5 :
					// JPA2.g:380:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_datetime_expression3472);
					case_expression398=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression398.getTree());

					}
					break;
				case 6 :
					// JPA2.g:381:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_datetime_expression3480);
					function_invocation399=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation399.getTree());

					}
					break;
				case 7 :
					// JPA2.g:382:7: date_time_timestamp_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_time_timestamp_literal_in_datetime_expression3488);
					date_time_timestamp_literal400=date_time_timestamp_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_time_timestamp_literal400.getTree());

					}
					break;
				case 8 :
					// JPA2.g:383:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_datetime_expression3496);
					subquery401=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery401.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:384:1: boolean_expression : ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | subquery );
	public final JPA2Parser.boolean_expression_return boolean_expression() throws RecognitionException {
		JPA2Parser.boolean_expression_return retval = new JPA2Parser.boolean_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression402 =null;
		ParserRuleReturnScope boolean_literal403 =null;
		ParserRuleReturnScope input_parameter404 =null;
		ParserRuleReturnScope case_expression405 =null;
		ParserRuleReturnScope function_invocation406 =null;
		ParserRuleReturnScope subquery407 =null;


		try {
			// JPA2.g:385:5: ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | subquery )
			int alt105=6;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt105=1;
				}
				break;
			case 134:
			case 135:
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
			case 82:
			case 112:
				{
				alt105=4;
				}
				break;
			case 96:
				{
				alt105=5;
				}
				break;
			case LPAREN:
				{
				alt105=6;
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
					// JPA2.g:385:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_boolean_expression3507);
					path_expression402=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression402.getTree());

					}
					break;
				case 2 :
					// JPA2.g:386:7: boolean_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_literal_in_boolean_expression3515);
					boolean_literal403=boolean_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal403.getTree());

					}
					break;
				case 3 :
					// JPA2.g:387:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_boolean_expression3523);
					input_parameter404=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter404.getTree());

					}
					break;
				case 4 :
					// JPA2.g:388:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_boolean_expression3531);
					case_expression405=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression405.getTree());

					}
					break;
				case 5 :
					// JPA2.g:389:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_boolean_expression3539);
					function_invocation406=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation406.getTree());

					}
					break;
				case 6 :
					// JPA2.g:390:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_boolean_expression3547);
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
	// $ANTLR end "boolean_expression"


	public static class enum_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enum_expression"
	// JPA2.g:391:1: enum_expression : ( path_expression | enum_literal | input_parameter | case_expression | subquery );
	public final JPA2Parser.enum_expression_return enum_expression() throws RecognitionException {
		JPA2Parser.enum_expression_return retval = new JPA2Parser.enum_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression408 =null;
		ParserRuleReturnScope enum_literal409 =null;
		ParserRuleReturnScope input_parameter410 =null;
		ParserRuleReturnScope case_expression411 =null;
		ParserRuleReturnScope subquery412 =null;


		try {
			// JPA2.g:392:5: ( path_expression | enum_literal | input_parameter | case_expression | subquery )
			int alt106=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA106_1 = input.LA(2);
				if ( (LA106_1==60) ) {
					alt106=1;
				}
				else if ( (LA106_1==EOF||LA106_1==AND||(LA106_1 >= GROUP && LA106_1 <= HAVING)||LA106_1==INNER||(LA106_1 >= JOIN && LA106_1 <= LEFT)||(LA106_1 >= OR && LA106_1 <= ORDER)||LA106_1==RPAREN||LA106_1==WORD||LA106_1==58||(LA106_1 >= 65 && LA106_1 <= 66)||LA106_1==78||LA106_1==89||LA106_1==91||LA106_1==95||LA106_1==118||LA106_1==123||(LA106_1 >= 131 && LA106_1 <= 132)) ) {
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
			case 82:
			case 112:
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
					// JPA2.g:392:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_enum_expression3558);
					path_expression408=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression408.getTree());

					}
					break;
				case 2 :
					// JPA2.g:393:7: enum_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_literal_in_enum_expression3566);
					enum_literal409=enum_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal409.getTree());

					}
					break;
				case 3 :
					// JPA2.g:394:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_enum_expression3574);
					input_parameter410=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter410.getTree());

					}
					break;
				case 4 :
					// JPA2.g:395:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_enum_expression3582);
					case_expression411=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression411.getTree());

					}
					break;
				case 5 :
					// JPA2.g:396:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_enum_expression3590);
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
	// $ANTLR end "enum_expression"


	public static class entity_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_expression"
	// JPA2.g:397:1: entity_expression : ( path_expression | simple_entity_expression );
	public final JPA2Parser.entity_expression_return entity_expression() throws RecognitionException {
		JPA2Parser.entity_expression_return retval = new JPA2Parser.entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression413 =null;
		ParserRuleReturnScope simple_entity_expression414 =null;


		try {
			// JPA2.g:398:5: ( path_expression | simple_entity_expression )
			int alt107=2;
			int LA107_0 = input.LA(1);
			if ( (LA107_0==WORD) ) {
				int LA107_1 = input.LA(2);
				if ( (LA107_1==60) ) {
					alt107=1;
				}
				else if ( (LA107_1==EOF||LA107_1==AND||(LA107_1 >= GROUP && LA107_1 <= HAVING)||LA107_1==INNER||(LA107_1 >= JOIN && LA107_1 <= LEFT)||(LA107_1 >= OR && LA107_1 <= ORDER)||LA107_1==RPAREN||LA107_1==58||(LA107_1 >= 65 && LA107_1 <= 66)||LA107_1==118||LA107_1==123||LA107_1==132) ) {
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
					// JPA2.g:398:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_expression3601);
					path_expression413=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression413.getTree());

					}
					break;
				case 2 :
					// JPA2.g:399:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3609);
					simple_entity_expression414=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression414.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:400:1: simple_entity_expression : ( identification_variable | input_parameter );
	public final JPA2Parser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
		JPA2Parser.simple_entity_expression_return retval = new JPA2Parser.simple_entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable415 =null;
		ParserRuleReturnScope input_parameter416 =null;


		try {
			// JPA2.g:401:5: ( identification_variable | input_parameter )
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
					// JPA2.g:401:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3620);
					identification_variable415=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable415.getTree());

					}
					break;
				case 2 :
					// JPA2.g:402:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3628);
					input_parameter416=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter416.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:403:1: entity_type_expression : ( type_discriminator | entity_type_literal | input_parameter );
	public final JPA2Parser.entity_type_expression_return entity_type_expression() throws RecognitionException {
		JPA2Parser.entity_type_expression_return retval = new JPA2Parser.entity_type_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope type_discriminator417 =null;
		ParserRuleReturnScope entity_type_literal418 =null;
		ParserRuleReturnScope input_parameter419 =null;


		try {
			// JPA2.g:404:5: ( type_discriminator | entity_type_literal | input_parameter )
			int alt109=3;
			switch ( input.LA(1) ) {
			case 127:
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
					// JPA2.g:404:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_entity_type_expression3639);
					type_discriminator417=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator417.getTree());

					}
					break;
				case 2 :
					// JPA2.g:405:7: entity_type_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_literal_in_entity_type_expression3647);
					entity_type_literal418=entity_type_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_literal418.getTree());

					}
					break;
				case 3 :
					// JPA2.g:406:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_entity_type_expression3655);
					input_parameter419=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter419.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:407:1: type_discriminator : 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' ;
	public final JPA2Parser.type_discriminator_return type_discriminator() throws RecognitionException {
		JPA2Parser.type_discriminator_return retval = new JPA2Parser.type_discriminator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal420=null;
		Token char_literal424=null;
		ParserRuleReturnScope general_identification_variable421 =null;
		ParserRuleReturnScope path_expression422 =null;
		ParserRuleReturnScope input_parameter423 =null;

		Object string_literal420_tree=null;
		Object char_literal424_tree=null;

		try {
			// JPA2.g:408:5: ( 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' )
			// JPA2.g:408:7: 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal420=(Token)match(input,127,FOLLOW_127_in_type_discriminator3666); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal420_tree = (Object)adaptor.create(string_literal420);
			adaptor.addChild(root_0, string_literal420_tree);
			}

			// JPA2.g:408:15: ( general_identification_variable | path_expression | input_parameter )
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
			case 100:
			case 130:
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
					// JPA2.g:408:16: general_identification_variable
					{
					pushFollow(FOLLOW_general_identification_variable_in_type_discriminator3669);
					general_identification_variable421=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable421.getTree());

					}
					break;
				case 2 :
					// JPA2.g:408:50: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_type_discriminator3673);
					path_expression422=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression422.getTree());

					}
					break;
				case 3 :
					// JPA2.g:408:68: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_type_discriminator3677);
					input_parameter423=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter423.getTree());

					}
					break;

			}

			char_literal424=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_type_discriminator3680); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal424_tree = (Object)adaptor.create(char_literal424);
			adaptor.addChild(root_0, char_literal424_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:409:1: functions_returning_numerics : ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' );
	public final JPA2Parser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
		JPA2Parser.functions_returning_numerics_return retval = new JPA2Parser.functions_returning_numerics_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal425=null;
		Token char_literal427=null;
		Token string_literal428=null;
		Token char_literal430=null;
		Token char_literal432=null;
		Token char_literal434=null;
		Token string_literal435=null;
		Token char_literal437=null;
		Token string_literal438=null;
		Token char_literal440=null;
		Token string_literal441=null;
		Token char_literal443=null;
		Token char_literal445=null;
		Token string_literal446=null;
		Token char_literal448=null;
		Token string_literal449=null;
		Token char_literal451=null;
		ParserRuleReturnScope string_expression426 =null;
		ParserRuleReturnScope string_expression429 =null;
		ParserRuleReturnScope string_expression431 =null;
		ParserRuleReturnScope arithmetic_expression433 =null;
		ParserRuleReturnScope arithmetic_expression436 =null;
		ParserRuleReturnScope arithmetic_expression439 =null;
		ParserRuleReturnScope arithmetic_expression442 =null;
		ParserRuleReturnScope arithmetic_expression444 =null;
		ParserRuleReturnScope path_expression447 =null;
		ParserRuleReturnScope identification_variable450 =null;

		Object string_literal425_tree=null;
		Object char_literal427_tree=null;
		Object string_literal428_tree=null;
		Object char_literal430_tree=null;
		Object char_literal432_tree=null;
		Object char_literal434_tree=null;
		Object string_literal435_tree=null;
		Object char_literal437_tree=null;
		Object string_literal438_tree=null;
		Object char_literal440_tree=null;
		Object string_literal441_tree=null;
		Object char_literal443_tree=null;
		Object char_literal445_tree=null;
		Object string_literal446_tree=null;
		Object char_literal448_tree=null;
		Object string_literal449_tree=null;
		Object char_literal451_tree=null;

		try {
			// JPA2.g:410:5: ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' )
			int alt112=7;
			switch ( input.LA(1) ) {
			case 102:
				{
				alt112=1;
				}
				break;
			case 104:
				{
				alt112=2;
				}
				break;
			case 75:
				{
				alt112=3;
				}
				break;
			case 121:
				{
				alt112=4;
				}
				break;
			case 107:
				{
				alt112=5;
				}
				break;
			case 119:
				{
				alt112=6;
				}
				break;
			case 98:
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
					// JPA2.g:410:7: 'LENGTH(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal425=(Token)match(input,102,FOLLOW_102_in_functions_returning_numerics3691); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal425_tree = (Object)adaptor.create(string_literal425);
					adaptor.addChild(root_0, string_literal425_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3692);
					string_expression426=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression426.getTree());

					char_literal427=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3693); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal427_tree = (Object)adaptor.create(char_literal427);
					adaptor.addChild(root_0, char_literal427_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:411:7: 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal428=(Token)match(input,104,FOLLOW_104_in_functions_returning_numerics3701); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal428_tree = (Object)adaptor.create(string_literal428);
					adaptor.addChild(root_0, string_literal428_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3703);
					string_expression429=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression429.getTree());

					char_literal430=(Token)match(input,58,FOLLOW_58_in_functions_returning_numerics3704); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal430_tree = (Object)adaptor.create(char_literal430);
					adaptor.addChild(root_0, char_literal430_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3706);
					string_expression431=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression431.getTree());

					// JPA2.g:411:55: ( ',' arithmetic_expression )?
					int alt111=2;
					int LA111_0 = input.LA(1);
					if ( (LA111_0==58) ) {
						alt111=1;
					}
					switch (alt111) {
						case 1 :
							// JPA2.g:411:56: ',' arithmetic_expression
							{
							char_literal432=(Token)match(input,58,FOLLOW_58_in_functions_returning_numerics3708); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal432_tree = (Object)adaptor.create(char_literal432);
							adaptor.addChild(root_0, char_literal432_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3709);
							arithmetic_expression433=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression433.getTree());

							}
							break;

					}

					char_literal434=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3712); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal434_tree = (Object)adaptor.create(char_literal434);
					adaptor.addChild(root_0, char_literal434_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:412:7: 'ABS(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal435=(Token)match(input,75,FOLLOW_75_in_functions_returning_numerics3720); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal435_tree = (Object)adaptor.create(string_literal435);
					adaptor.addChild(root_0, string_literal435_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3721);
					arithmetic_expression436=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression436.getTree());

					char_literal437=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3722); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal437_tree = (Object)adaptor.create(char_literal437);
					adaptor.addChild(root_0, char_literal437_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:413:7: 'SQRT(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal438=(Token)match(input,121,FOLLOW_121_in_functions_returning_numerics3730); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal438_tree = (Object)adaptor.create(string_literal438);
					adaptor.addChild(root_0, string_literal438_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3731);
					arithmetic_expression439=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression439.getTree());

					char_literal440=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3732); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal440_tree = (Object)adaptor.create(char_literal440);
					adaptor.addChild(root_0, char_literal440_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:414:7: 'MOD(' arithmetic_expression ',' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal441=(Token)match(input,107,FOLLOW_107_in_functions_returning_numerics3740); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal441_tree = (Object)adaptor.create(string_literal441);
					adaptor.addChild(root_0, string_literal441_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3741);
					arithmetic_expression442=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression442.getTree());

					char_literal443=(Token)match(input,58,FOLLOW_58_in_functions_returning_numerics3742); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal443_tree = (Object)adaptor.create(char_literal443);
					adaptor.addChild(root_0, char_literal443_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3744);
					arithmetic_expression444=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression444.getTree());

					char_literal445=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3745); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal445_tree = (Object)adaptor.create(char_literal445);
					adaptor.addChild(root_0, char_literal445_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:415:7: 'SIZE(' path_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal446=(Token)match(input,119,FOLLOW_119_in_functions_returning_numerics3753); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal446_tree = (Object)adaptor.create(string_literal446);
					adaptor.addChild(root_0, string_literal446_tree);
					}

					pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3754);
					path_expression447=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression447.getTree());

					char_literal448=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3755); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal448_tree = (Object)adaptor.create(char_literal448);
					adaptor.addChild(root_0, char_literal448_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:416:7: 'INDEX(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal449=(Token)match(input,98,FOLLOW_98_in_functions_returning_numerics3763); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal449_tree = (Object)adaptor.create(string_literal449);
					adaptor.addChild(root_0, string_literal449_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_functions_returning_numerics3764);
					identification_variable450=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable450.getTree());

					char_literal451=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3765); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal451_tree = (Object)adaptor.create(char_literal451);
					adaptor.addChild(root_0, char_literal451_tree);
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
	// JPA2.g:417:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
	public final JPA2Parser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
		JPA2Parser.functions_returning_datetime_return retval = new JPA2Parser.functions_returning_datetime_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set452=null;

		Object set452_tree=null;

		try {
			// JPA2.g:418:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set452=input.LT(1);
			if ( (input.LA(1) >= 84 && input.LA(1) <= 86) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set452));
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
	// JPA2.g:421:1: functions_returning_strings : ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' );
	public final JPA2Parser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
		JPA2Parser.functions_returning_strings_return retval = new JPA2Parser.functions_returning_strings_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal453=null;
		Token char_literal455=null;
		Token char_literal457=null;
		Token char_literal459=null;
		Token string_literal460=null;
		Token char_literal462=null;
		Token char_literal464=null;
		Token char_literal466=null;
		Token string_literal467=null;
		Token string_literal470=null;
		Token char_literal472=null;
		Token string_literal473=null;
		Token char_literal474=null;
		Token char_literal476=null;
		Token string_literal477=null;
		Token char_literal479=null;
		ParserRuleReturnScope string_expression454 =null;
		ParserRuleReturnScope string_expression456 =null;
		ParserRuleReturnScope string_expression458 =null;
		ParserRuleReturnScope string_expression461 =null;
		ParserRuleReturnScope arithmetic_expression463 =null;
		ParserRuleReturnScope arithmetic_expression465 =null;
		ParserRuleReturnScope trim_specification468 =null;
		ParserRuleReturnScope trim_character469 =null;
		ParserRuleReturnScope string_expression471 =null;
		ParserRuleReturnScope string_expression475 =null;
		ParserRuleReturnScope string_expression478 =null;

		Object string_literal453_tree=null;
		Object char_literal455_tree=null;
		Object char_literal457_tree=null;
		Object char_literal459_tree=null;
		Object string_literal460_tree=null;
		Object char_literal462_tree=null;
		Object char_literal464_tree=null;
		Object char_literal466_tree=null;
		Object string_literal467_tree=null;
		Object string_literal470_tree=null;
		Object char_literal472_tree=null;
		Object string_literal473_tree=null;
		Object char_literal474_tree=null;
		Object char_literal476_tree=null;
		Object string_literal477_tree=null;
		Object char_literal479_tree=null;

		try {
			// JPA2.g:422:5: ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' )
			int alt118=5;
			switch ( input.LA(1) ) {
			case 83:
				{
				alt118=1;
				}
				break;
			case 122:
				{
				alt118=2;
				}
				break;
			case 126:
				{
				alt118=3;
				}
				break;
			case LOWER:
				{
				alt118=4;
				}
				break;
			case 129:
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
					// JPA2.g:422:7: 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal453=(Token)match(input,83,FOLLOW_83_in_functions_returning_strings3803); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal453_tree = (Object)adaptor.create(string_literal453);
					adaptor.addChild(root_0, string_literal453_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3804);
					string_expression454=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression454.getTree());

					char_literal455=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3805); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal455_tree = (Object)adaptor.create(char_literal455);
					adaptor.addChild(root_0, char_literal455_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3807);
					string_expression456=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression456.getTree());

					// JPA2.g:422:55: ( ',' string_expression )*
					loop113:
					while (true) {
						int alt113=2;
						int LA113_0 = input.LA(1);
						if ( (LA113_0==58) ) {
							alt113=1;
						}

						switch (alt113) {
						case 1 :
							// JPA2.g:422:56: ',' string_expression
							{
							char_literal457=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3810); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal457_tree = (Object)adaptor.create(char_literal457);
							adaptor.addChild(root_0, char_literal457_tree);
							}

							pushFollow(FOLLOW_string_expression_in_functions_returning_strings3812);
							string_expression458=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression458.getTree());

							}
							break;

						default :
							break loop113;
						}
					}

					char_literal459=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3815); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal459_tree = (Object)adaptor.create(char_literal459);
					adaptor.addChild(root_0, char_literal459_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:423:7: 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal460=(Token)match(input,122,FOLLOW_122_in_functions_returning_strings3823); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal460_tree = (Object)adaptor.create(string_literal460);
					adaptor.addChild(root_0, string_literal460_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3825);
					string_expression461=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression461.getTree());

					char_literal462=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3826); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal462_tree = (Object)adaptor.create(char_literal462);
					adaptor.addChild(root_0, char_literal462_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3828);
					arithmetic_expression463=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression463.getTree());

					// JPA2.g:423:63: ( ',' arithmetic_expression )?
					int alt114=2;
					int LA114_0 = input.LA(1);
					if ( (LA114_0==58) ) {
						alt114=1;
					}
					switch (alt114) {
						case 1 :
							// JPA2.g:423:64: ',' arithmetic_expression
							{
							char_literal464=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3831); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal464_tree = (Object)adaptor.create(char_literal464);
							adaptor.addChild(root_0, char_literal464_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3833);
							arithmetic_expression465=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression465.getTree());

							}
							break;

					}

					char_literal466=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3836); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal466_tree = (Object)adaptor.create(char_literal466);
					adaptor.addChild(root_0, char_literal466_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:424:7: 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal467=(Token)match(input,126,FOLLOW_126_in_functions_returning_strings3844); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal467_tree = (Object)adaptor.create(string_literal467);
					adaptor.addChild(root_0, string_literal467_tree);
					}

					// JPA2.g:424:14: ( ( trim_specification )? ( trim_character )? 'FROM' )?
					int alt117=2;
					int LA117_0 = input.LA(1);
					if ( (LA117_0==TRIM_CHARACTER||LA117_0==80||LA117_0==95||LA117_0==101||LA117_0==124) ) {
						alt117=1;
					}
					switch (alt117) {
						case 1 :
							// JPA2.g:424:15: ( trim_specification )? ( trim_character )? 'FROM'
							{
							// JPA2.g:424:15: ( trim_specification )?
							int alt115=2;
							int LA115_0 = input.LA(1);
							if ( (LA115_0==80||LA115_0==101||LA115_0==124) ) {
								alt115=1;
							}
							switch (alt115) {
								case 1 :
									// JPA2.g:424:16: trim_specification
									{
									pushFollow(FOLLOW_trim_specification_in_functions_returning_strings3847);
									trim_specification468=trim_specification();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification468.getTree());

									}
									break;

							}

							// JPA2.g:424:37: ( trim_character )?
							int alt116=2;
							int LA116_0 = input.LA(1);
							if ( (LA116_0==TRIM_CHARACTER) ) {
								alt116=1;
							}
							switch (alt116) {
								case 1 :
									// JPA2.g:424:38: trim_character
									{
									pushFollow(FOLLOW_trim_character_in_functions_returning_strings3852);
									trim_character469=trim_character();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_character469.getTree());

									}
									break;

							}

							string_literal470=(Token)match(input,95,FOLLOW_95_in_functions_returning_strings3856); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal470_tree = (Object)adaptor.create(string_literal470);
							adaptor.addChild(root_0, string_literal470_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3860);
					string_expression471=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression471.getTree());

					char_literal472=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3862); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal472_tree = (Object)adaptor.create(char_literal472);
					adaptor.addChild(root_0, char_literal472_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:425:7: 'LOWER' '(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal473=(Token)match(input,LOWER,FOLLOW_LOWER_in_functions_returning_strings3870); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal473_tree = (Object)adaptor.create(string_literal473);
					adaptor.addChild(root_0, string_literal473_tree);
					}

					char_literal474=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3872); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal474_tree = (Object)adaptor.create(char_literal474);
					adaptor.addChild(root_0, char_literal474_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3873);
					string_expression475=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression475.getTree());

					char_literal476=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3874); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal476_tree = (Object)adaptor.create(char_literal476);
					adaptor.addChild(root_0, char_literal476_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:426:7: 'UPPER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal477=(Token)match(input,129,FOLLOW_129_in_functions_returning_strings3882); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal477_tree = (Object)adaptor.create(string_literal477);
					adaptor.addChild(root_0, string_literal477_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3883);
					string_expression478=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression478.getTree());

					char_literal479=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3884); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal479_tree = (Object)adaptor.create(char_literal479);
					adaptor.addChild(root_0, char_literal479_tree);
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
	// JPA2.g:427:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
	public final JPA2Parser.trim_specification_return trim_specification() throws RecognitionException {
		JPA2Parser.trim_specification_return retval = new JPA2Parser.trim_specification_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set480=null;

		Object set480_tree=null;

		try {
			// JPA2.g:428:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set480=input.LT(1);
			if ( input.LA(1)==80||input.LA(1)==101||input.LA(1)==124 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set480));
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
	// JPA2.g:429:1: function_invocation : 'FUNCTION(' function_name ( ',' function_arg )* ')' ;
	public final JPA2Parser.function_invocation_return function_invocation() throws RecognitionException {
		JPA2Parser.function_invocation_return retval = new JPA2Parser.function_invocation_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal481=null;
		Token char_literal483=null;
		Token char_literal485=null;
		ParserRuleReturnScope function_name482 =null;
		ParserRuleReturnScope function_arg484 =null;

		Object string_literal481_tree=null;
		Object char_literal483_tree=null;
		Object char_literal485_tree=null;

		try {
			// JPA2.g:430:5: ( 'FUNCTION(' function_name ( ',' function_arg )* ')' )
			// JPA2.g:430:7: 'FUNCTION(' function_name ( ',' function_arg )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal481=(Token)match(input,96,FOLLOW_96_in_function_invocation3914); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal481_tree = (Object)adaptor.create(string_literal481);
			adaptor.addChild(root_0, string_literal481_tree);
			}

			pushFollow(FOLLOW_function_name_in_function_invocation3915);
			function_name482=function_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_name482.getTree());

			// JPA2.g:430:32: ( ',' function_arg )*
			loop119:
			while (true) {
				int alt119=2;
				int LA119_0 = input.LA(1);
				if ( (LA119_0==58) ) {
					alt119=1;
				}

				switch (alt119) {
				case 1 :
					// JPA2.g:430:33: ',' function_arg
					{
					char_literal483=(Token)match(input,58,FOLLOW_58_in_function_invocation3918); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal483_tree = (Object)adaptor.create(char_literal483);
					adaptor.addChild(root_0, char_literal483_tree);
					}

					pushFollow(FOLLOW_function_arg_in_function_invocation3920);
					function_arg484=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg484.getTree());

					}
					break;

				default :
					break loop119;
				}
			}

			char_literal485=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function_invocation3924); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal485_tree = (Object)adaptor.create(char_literal485);
			adaptor.addChild(root_0, char_literal485_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:431:1: function_arg : ( literal | path_expression | input_parameter | scalar_expression );
	public final JPA2Parser.function_arg_return function_arg() throws RecognitionException {
		JPA2Parser.function_arg_return retval = new JPA2Parser.function_arg_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal486 =null;
		ParserRuleReturnScope path_expression487 =null;
		ParserRuleReturnScope input_parameter488 =null;
		ParserRuleReturnScope scalar_expression489 =null;


		try {
			// JPA2.g:432:5: ( literal | path_expression | input_parameter | scalar_expression )
			int alt120=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA120_1 = input.LA(2);
				if ( (LA120_1==60) ) {
					alt120=2;
				}
				else if ( (synpred227_JPA2()) ) {
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
						if ( (synpred229_JPA2()) ) {
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
					if ( (synpred229_JPA2()) ) {
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
				if ( (synpred229_JPA2()) ) {
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
					if ( (LA120_11==136) ) {
						int LA120_13 = input.LA(4);
						if ( (synpred229_JPA2()) ) {
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
			case 96:
			case 98:
			case 102:
			case 104:
			case 107:
			case 112:
			case 119:
			case 121:
			case 122:
			case 126:
			case 127:
			case 129:
			case 134:
			case 135:
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
					// JPA2.g:432:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_function_arg3935);
					literal486=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal486.getTree());

					}
					break;
				case 2 :
					// JPA2.g:433:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_function_arg3943);
					path_expression487=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression487.getTree());

					}
					break;
				case 3 :
					// JPA2.g:434:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_function_arg3951);
					input_parameter488=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter488.getTree());

					}
					break;
				case 4 :
					// JPA2.g:435:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_function_arg3959);
					scalar_expression489=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression489.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:436:1: case_expression : ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression );
	public final JPA2Parser.case_expression_return case_expression() throws RecognitionException {
		JPA2Parser.case_expression_return retval = new JPA2Parser.case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope general_case_expression490 =null;
		ParserRuleReturnScope simple_case_expression491 =null;
		ParserRuleReturnScope coalesce_expression492 =null;
		ParserRuleReturnScope nullif_expression493 =null;


		try {
			// JPA2.g:437:5: ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression )
			int alt121=4;
			switch ( input.LA(1) ) {
			case 81:
				{
				int LA121_1 = input.LA(2);
				if ( (LA121_1==131) ) {
					alt121=1;
				}
				else if ( (LA121_1==WORD||LA121_1==127) ) {
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
			case 82:
				{
				alt121=3;
				}
				break;
			case 112:
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
					// JPA2.g:437:7: general_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_case_expression_in_case_expression3970);
					general_case_expression490=general_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_case_expression490.getTree());

					}
					break;
				case 2 :
					// JPA2.g:438:7: simple_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_case_expression_in_case_expression3978);
					simple_case_expression491=simple_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_case_expression491.getTree());

					}
					break;
				case 3 :
					// JPA2.g:439:7: coalesce_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_coalesce_expression_in_case_expression3986);
					coalesce_expression492=coalesce_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, coalesce_expression492.getTree());

					}
					break;
				case 4 :
					// JPA2.g:440:7: nullif_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_nullif_expression_in_case_expression3994);
					nullif_expression493=nullif_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, nullif_expression493.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:441:1: general_case_expression : 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.general_case_expression_return general_case_expression() throws RecognitionException {
		JPA2Parser.general_case_expression_return retval = new JPA2Parser.general_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal494=null;
		Token string_literal497=null;
		Token string_literal499=null;
		ParserRuleReturnScope when_clause495 =null;
		ParserRuleReturnScope when_clause496 =null;
		ParserRuleReturnScope scalar_expression498 =null;

		Object string_literal494_tree=null;
		Object string_literal497_tree=null;
		Object string_literal499_tree=null;

		try {
			// JPA2.g:442:5: ( 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:442:7: 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal494=(Token)match(input,81,FOLLOW_81_in_general_case_expression4005); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal494_tree = (Object)adaptor.create(string_literal494);
			adaptor.addChild(root_0, string_literal494_tree);
			}

			pushFollow(FOLLOW_when_clause_in_general_case_expression4007);
			when_clause495=when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause495.getTree());

			// JPA2.g:442:26: ( when_clause )*
			loop122:
			while (true) {
				int alt122=2;
				int LA122_0 = input.LA(1);
				if ( (LA122_0==131) ) {
					alt122=1;
				}

				switch (alt122) {
				case 1 :
					// JPA2.g:442:27: when_clause
					{
					pushFollow(FOLLOW_when_clause_in_general_case_expression4010);
					when_clause496=when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause496.getTree());

					}
					break;

				default :
					break loop122;
				}
			}

			string_literal497=(Token)match(input,89,FOLLOW_89_in_general_case_expression4014); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal497_tree = (Object)adaptor.create(string_literal497);
			adaptor.addChild(root_0, string_literal497_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_general_case_expression4016);
			scalar_expression498=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression498.getTree());

			string_literal499=(Token)match(input,91,FOLLOW_91_in_general_case_expression4018); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal499_tree = (Object)adaptor.create(string_literal499);
			adaptor.addChild(root_0, string_literal499_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:443:1: when_clause : 'WHEN' conditional_expression 'THEN' scalar_expression ;
	public final JPA2Parser.when_clause_return when_clause() throws RecognitionException {
		JPA2Parser.when_clause_return retval = new JPA2Parser.when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal500=null;
		Token string_literal502=null;
		ParserRuleReturnScope conditional_expression501 =null;
		ParserRuleReturnScope scalar_expression503 =null;

		Object string_literal500_tree=null;
		Object string_literal502_tree=null;

		try {
			// JPA2.g:444:5: ( 'WHEN' conditional_expression 'THEN' scalar_expression )
			// JPA2.g:444:7: 'WHEN' conditional_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal500=(Token)match(input,131,FOLLOW_131_in_when_clause4029); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal500_tree = (Object)adaptor.create(string_literal500);
			adaptor.addChild(root_0, string_literal500_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_when_clause4031);
			conditional_expression501=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression501.getTree());

			string_literal502=(Token)match(input,123,FOLLOW_123_in_when_clause4033); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal502_tree = (Object)adaptor.create(string_literal502);
			adaptor.addChild(root_0, string_literal502_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_when_clause4035);
			scalar_expression503=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression503.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:445:1: simple_case_expression : 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.simple_case_expression_return simple_case_expression() throws RecognitionException {
		JPA2Parser.simple_case_expression_return retval = new JPA2Parser.simple_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal504=null;
		Token string_literal508=null;
		Token string_literal510=null;
		ParserRuleReturnScope case_operand505 =null;
		ParserRuleReturnScope simple_when_clause506 =null;
		ParserRuleReturnScope simple_when_clause507 =null;
		ParserRuleReturnScope scalar_expression509 =null;

		Object string_literal504_tree=null;
		Object string_literal508_tree=null;
		Object string_literal510_tree=null;

		try {
			// JPA2.g:446:5: ( 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:446:7: 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal504=(Token)match(input,81,FOLLOW_81_in_simple_case_expression4046); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal504_tree = (Object)adaptor.create(string_literal504);
			adaptor.addChild(root_0, string_literal504_tree);
			}

			pushFollow(FOLLOW_case_operand_in_simple_case_expression4048);
			case_operand505=case_operand();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, case_operand505.getTree());

			pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4050);
			simple_when_clause506=simple_when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause506.getTree());

			// JPA2.g:446:46: ( simple_when_clause )*
			loop123:
			while (true) {
				int alt123=2;
				int LA123_0 = input.LA(1);
				if ( (LA123_0==131) ) {
					alt123=1;
				}

				switch (alt123) {
				case 1 :
					// JPA2.g:446:47: simple_when_clause
					{
					pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4053);
					simple_when_clause507=simple_when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause507.getTree());

					}
					break;

				default :
					break loop123;
				}
			}

			string_literal508=(Token)match(input,89,FOLLOW_89_in_simple_case_expression4057); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal508_tree = (Object)adaptor.create(string_literal508);
			adaptor.addChild(root_0, string_literal508_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_case_expression4059);
			scalar_expression509=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression509.getTree());

			string_literal510=(Token)match(input,91,FOLLOW_91_in_simple_case_expression4061); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal510_tree = (Object)adaptor.create(string_literal510);
			adaptor.addChild(root_0, string_literal510_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:447:1: case_operand : ( path_expression | type_discriminator );
	public final JPA2Parser.case_operand_return case_operand() throws RecognitionException {
		JPA2Parser.case_operand_return retval = new JPA2Parser.case_operand_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression511 =null;
		ParserRuleReturnScope type_discriminator512 =null;


		try {
			// JPA2.g:448:5: ( path_expression | type_discriminator )
			int alt124=2;
			int LA124_0 = input.LA(1);
			if ( (LA124_0==WORD) ) {
				alt124=1;
			}
			else if ( (LA124_0==127) ) {
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
					// JPA2.g:448:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_case_operand4072);
					path_expression511=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression511.getTree());

					}
					break;
				case 2 :
					// JPA2.g:449:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_case_operand4080);
					type_discriminator512=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator512.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:450:1: simple_when_clause : 'WHEN' scalar_expression 'THEN' scalar_expression ;
	public final JPA2Parser.simple_when_clause_return simple_when_clause() throws RecognitionException {
		JPA2Parser.simple_when_clause_return retval = new JPA2Parser.simple_when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal513=null;
		Token string_literal515=null;
		ParserRuleReturnScope scalar_expression514 =null;
		ParserRuleReturnScope scalar_expression516 =null;

		Object string_literal513_tree=null;
		Object string_literal515_tree=null;

		try {
			// JPA2.g:451:5: ( 'WHEN' scalar_expression 'THEN' scalar_expression )
			// JPA2.g:451:7: 'WHEN' scalar_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal513=(Token)match(input,131,FOLLOW_131_in_simple_when_clause4091); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal513_tree = (Object)adaptor.create(string_literal513);
			adaptor.addChild(root_0, string_literal513_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4093);
			scalar_expression514=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression514.getTree());

			string_literal515=(Token)match(input,123,FOLLOW_123_in_simple_when_clause4095); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal515_tree = (Object)adaptor.create(string_literal515);
			adaptor.addChild(root_0, string_literal515_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4097);
			scalar_expression516=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression516.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:452:1: coalesce_expression : 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' ;
	public final JPA2Parser.coalesce_expression_return coalesce_expression() throws RecognitionException {
		JPA2Parser.coalesce_expression_return retval = new JPA2Parser.coalesce_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal517=null;
		Token char_literal519=null;
		Token char_literal521=null;
		ParserRuleReturnScope scalar_expression518 =null;
		ParserRuleReturnScope scalar_expression520 =null;

		Object string_literal517_tree=null;
		Object char_literal519_tree=null;
		Object char_literal521_tree=null;

		try {
			// JPA2.g:453:5: ( 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' )
			// JPA2.g:453:7: 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal517=(Token)match(input,82,FOLLOW_82_in_coalesce_expression4108); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal517_tree = (Object)adaptor.create(string_literal517);
			adaptor.addChild(root_0, string_literal517_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4109);
			scalar_expression518=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression518.getTree());

			// JPA2.g:453:36: ( ',' scalar_expression )+
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
					// JPA2.g:453:37: ',' scalar_expression
					{
					char_literal519=(Token)match(input,58,FOLLOW_58_in_coalesce_expression4112); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal519_tree = (Object)adaptor.create(char_literal519);
					adaptor.addChild(root_0, char_literal519_tree);
					}

					pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4114);
					scalar_expression520=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression520.getTree());

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

			char_literal521=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_coalesce_expression4117); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal521_tree = (Object)adaptor.create(char_literal521);
			adaptor.addChild(root_0, char_literal521_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:454:1: nullif_expression : 'NULLIF(' scalar_expression ',' scalar_expression ')' ;
	public final JPA2Parser.nullif_expression_return nullif_expression() throws RecognitionException {
		JPA2Parser.nullif_expression_return retval = new JPA2Parser.nullif_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal522=null;
		Token char_literal524=null;
		Token char_literal526=null;
		ParserRuleReturnScope scalar_expression523 =null;
		ParserRuleReturnScope scalar_expression525 =null;

		Object string_literal522_tree=null;
		Object char_literal524_tree=null;
		Object char_literal526_tree=null;

		try {
			// JPA2.g:455:5: ( 'NULLIF(' scalar_expression ',' scalar_expression ')' )
			// JPA2.g:455:7: 'NULLIF(' scalar_expression ',' scalar_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal522=(Token)match(input,112,FOLLOW_112_in_nullif_expression4128); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal522_tree = (Object)adaptor.create(string_literal522);
			adaptor.addChild(root_0, string_literal522_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4129);
			scalar_expression523=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression523.getTree());

			char_literal524=(Token)match(input,58,FOLLOW_58_in_nullif_expression4131); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal524_tree = (Object)adaptor.create(char_literal524);
			adaptor.addChild(root_0, char_literal524_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4133);
			scalar_expression525=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression525.getTree());

			char_literal526=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nullif_expression4134); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal526_tree = (Object)adaptor.create(char_literal526);
			adaptor.addChild(root_0, char_literal526_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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


	public static class input_parameter_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "input_parameter"
	// JPA2.g:458:1: input_parameter : ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) );
	public final JPA2Parser.input_parameter_return input_parameter() throws RecognitionException {
		JPA2Parser.input_parameter_return retval = new JPA2Parser.input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal527=null;
		Token NAMED_PARAMETER529=null;
		Token string_literal530=null;
		Token WORD531=null;
		Token char_literal532=null;
		ParserRuleReturnScope numeric_literal528 =null;

		Object char_literal527_tree=null;
		Object NAMED_PARAMETER529_tree=null;
		Object string_literal530_tree=null;
		Object WORD531_tree=null;
		Object char_literal532_tree=null;
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
		RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
		RewriteRuleTokenStream stream_136=new RewriteRuleTokenStream(adaptor,"token 136");
		RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
		RewriteRuleSubtreeStream stream_numeric_literal=new RewriteRuleSubtreeStream(adaptor,"rule numeric_literal");

		try {
			// JPA2.g:459:5: ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
			int alt126=3;
			switch ( input.LA(1) ) {
			case 69:
				{
				alt126=1;
				}
				break;
			case NAMED_PARAMETER:
				{
				alt126=2;
				}
				break;
			case 55:
				{
				alt126=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 126, 0, input);
				throw nvae;
			}
			switch (alt126) {
				case 1 :
					// JPA2.g:459:7: '?' numeric_literal
					{
					char_literal527=(Token)match(input,69,FOLLOW_69_in_input_parameter4147); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_69.add(char_literal527);

					pushFollow(FOLLOW_numeric_literal_in_input_parameter4149);
					numeric_literal528=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_literal.add(numeric_literal528.getTree());
					// AST REWRITE
					// elements: 69, numeric_literal
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 459:27: -> ^( T_PARAMETER[] '?' numeric_literal )
					{
						// JPA2.g:459:30: ^( T_PARAMETER[] '?' numeric_literal )
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
					// JPA2.g:460:7: NAMED_PARAMETER
					{
					NAMED_PARAMETER529=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter4172); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER529);

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
					// 460:23: -> ^( T_PARAMETER[] NAMED_PARAMETER )
					{
						// JPA2.g:460:26: ^( T_PARAMETER[] NAMED_PARAMETER )
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
					// JPA2.g:461:7: '${' WORD '}'
					{
					string_literal530=(Token)match(input,55,FOLLOW_55_in_input_parameter4193); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_55.add(string_literal530);

					WORD531=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter4195); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WORD.add(WORD531);

					char_literal532=(Token)match(input,136,FOLLOW_136_in_input_parameter4197); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_136.add(char_literal532);

					// AST REWRITE
					// elements: 55, 136, WORD
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 461:21: -> ^( T_PARAMETER[] '${' WORD '}' )
					{
						// JPA2.g:461:24: ^( T_PARAMETER[] '${' WORD '}' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_55.nextNode());
						adaptor.addChild(root_1, stream_WORD.nextNode());
						adaptor.addChild(root_1, stream_136.nextNode());
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
	// JPA2.g:463:1: literal : WORD ;
	public final JPA2Parser.literal_return literal() throws RecognitionException {
		JPA2Parser.literal_return retval = new JPA2Parser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD533=null;

		Object WORD533_tree=null;

		try {
			// JPA2.g:464:5: ( WORD )
			// JPA2.g:464:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD533=(Token)match(input,WORD,FOLLOW_WORD_in_literal4225); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD533_tree = (Object)adaptor.create(WORD533);
			adaptor.addChild(root_0, WORD533_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:466:1: constructor_name : WORD ;
	public final JPA2Parser.constructor_name_return constructor_name() throws RecognitionException {
		JPA2Parser.constructor_name_return retval = new JPA2Parser.constructor_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD534=null;

		Object WORD534_tree=null;

		try {
			// JPA2.g:467:5: ( WORD )
			// JPA2.g:467:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD534=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4237); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD534_tree = (Object)adaptor.create(WORD534);
			adaptor.addChild(root_0, WORD534_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:469:1: enum_literal : WORD ;
	public final JPA2Parser.enum_literal_return enum_literal() throws RecognitionException {
		JPA2Parser.enum_literal_return retval = new JPA2Parser.enum_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD535=null;

		Object WORD535_tree=null;

		try {
			// JPA2.g:470:5: ( WORD )
			// JPA2.g:470:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD535=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal4249); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD535_tree = (Object)adaptor.create(WORD535);
			adaptor.addChild(root_0, WORD535_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:472:1: boolean_literal : ( 'true' | 'false' );
	public final JPA2Parser.boolean_literal_return boolean_literal() throws RecognitionException {
		JPA2Parser.boolean_literal_return retval = new JPA2Parser.boolean_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set536=null;

		Object set536_tree=null;

		try {
			// JPA2.g:473:5: ( 'true' | 'false' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set536=input.LT(1);
			if ( (input.LA(1) >= 134 && input.LA(1) <= 135) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set536));
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
	// JPA2.g:477:1: field : ( WORD | 'GROUP' );
	public final JPA2Parser.field_return field() throws RecognitionException {
		JPA2Parser.field_return retval = new JPA2Parser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set537=null;

		Object set537_tree=null;

		try {
			// JPA2.g:478:5: ( WORD | 'GROUP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set537=input.LT(1);
			if ( input.LA(1)==GROUP||input.LA(1)==WORD ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set537));
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
	// $ANTLR end "field"


	public static class identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "identification_variable"
	// JPA2.g:480:1: identification_variable : WORD ;
	public final JPA2Parser.identification_variable_return identification_variable() throws RecognitionException {
		JPA2Parser.identification_variable_return retval = new JPA2Parser.identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD538=null;

		Object WORD538_tree=null;

		try {
			// JPA2.g:481:5: ( WORD )
			// JPA2.g:481:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD538=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable4298); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD538_tree = (Object)adaptor.create(WORD538);
			adaptor.addChild(root_0, WORD538_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:483:1: parameter_name : WORD ( '.' WORD )* ;
	public final JPA2Parser.parameter_name_return parameter_name() throws RecognitionException {
		JPA2Parser.parameter_name_return retval = new JPA2Parser.parameter_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD539=null;
		Token char_literal540=null;
		Token WORD541=null;

		Object WORD539_tree=null;
		Object char_literal540_tree=null;
		Object WORD541_tree=null;

		try {
			// JPA2.g:484:5: ( WORD ( '.' WORD )* )
			// JPA2.g:484:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD539=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4310); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD539_tree = (Object)adaptor.create(WORD539);
			adaptor.addChild(root_0, WORD539_tree);
			}

			// JPA2.g:484:12: ( '.' WORD )*
			loop127:
			while (true) {
				int alt127=2;
				int LA127_0 = input.LA(1);
				if ( (LA127_0==60) ) {
					alt127=1;
				}

				switch (alt127) {
				case 1 :
					// JPA2.g:484:13: '.' WORD
					{
					char_literal540=(Token)match(input,60,FOLLOW_60_in_parameter_name4313); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal540_tree = (Object)adaptor.create(char_literal540);
					adaptor.addChild(root_0, char_literal540_tree);
					}

					WORD541=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4316); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD541_tree = (Object)adaptor.create(WORD541);
					adaptor.addChild(root_0, WORD541_tree);
					}

					}
					break;

				default :
					break loop127;
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
	// JPA2.g:487:1: escape_character : ( '\\'.\\'' | STRING_LITERAL );
	public final JPA2Parser.escape_character_return escape_character() throws RecognitionException {
		JPA2Parser.escape_character_return retval = new JPA2Parser.escape_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set542=null;

		Object set542_tree=null;

		try {
			// JPA2.g:488:5: ( '\\'.\\'' | STRING_LITERAL )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set542=input.LT(1);
			if ( input.LA(1)==STRING_LITERAL||input.LA(1)==TRIM_CHARACTER ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set542));
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
	// JPA2.g:489:1: trim_character : TRIM_CHARACTER ;
	public final JPA2Parser.trim_character_return trim_character() throws RecognitionException {
		JPA2Parser.trim_character_return retval = new JPA2Parser.trim_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRIM_CHARACTER543=null;

		Object TRIM_CHARACTER543_tree=null;

		try {
			// JPA2.g:490:5: ( TRIM_CHARACTER )
			// JPA2.g:490:7: TRIM_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			TRIM_CHARACTER543=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_trim_character4346); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TRIM_CHARACTER543_tree = (Object)adaptor.create(TRIM_CHARACTER543);
			adaptor.addChild(root_0, TRIM_CHARACTER543_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:491:1: string_literal : STRING_LITERAL ;
	public final JPA2Parser.string_literal_return string_literal() throws RecognitionException {
		JPA2Parser.string_literal_return retval = new JPA2Parser.string_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL544=null;

		Object STRING_LITERAL544_tree=null;

		try {
			// JPA2.g:492:5: ( STRING_LITERAL )
			// JPA2.g:492:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL544=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_literal4357); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL544_tree = (Object)adaptor.create(STRING_LITERAL544);
			adaptor.addChild(root_0, STRING_LITERAL544_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:493:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
	public final JPA2Parser.numeric_literal_return numeric_literal() throws RecognitionException {
		JPA2Parser.numeric_literal_return retval = new JPA2Parser.numeric_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal545=null;
		Token INT_NUMERAL546=null;

		Object string_literal545_tree=null;
		Object INT_NUMERAL546_tree=null;

		try {
			// JPA2.g:494:5: ( ( '0x' )? INT_NUMERAL )
			// JPA2.g:494:7: ( '0x' )? INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:494:7: ( '0x' )?
			int alt128=2;
			int LA128_0 = input.LA(1);
			if ( (LA128_0==62) ) {
				alt128=1;
			}
			switch (alt128) {
				case 1 :
					// JPA2.g:494:8: '0x'
					{
					string_literal545=(Token)match(input,62,FOLLOW_62_in_numeric_literal4369); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal545_tree = (Object)adaptor.create(string_literal545);
					adaptor.addChild(root_0, string_literal545_tree);
					}

					}
					break;

			}

			INT_NUMERAL546=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal4373); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL546_tree = (Object)adaptor.create(INT_NUMERAL546);
			adaptor.addChild(root_0, INT_NUMERAL546_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:495:1: single_valued_object_field : WORD ;
	public final JPA2Parser.single_valued_object_field_return single_valued_object_field() throws RecognitionException {
		JPA2Parser.single_valued_object_field_return retval = new JPA2Parser.single_valued_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD547=null;

		Object WORD547_tree=null;

		try {
			// JPA2.g:496:5: ( WORD )
			// JPA2.g:496:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD547=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_object_field4385); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD547_tree = (Object)adaptor.create(WORD547);
			adaptor.addChild(root_0, WORD547_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:497:1: single_valued_embeddable_object_field : WORD ;
	public final JPA2Parser.single_valued_embeddable_object_field_return single_valued_embeddable_object_field() throws RecognitionException {
		JPA2Parser.single_valued_embeddable_object_field_return retval = new JPA2Parser.single_valued_embeddable_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD548=null;

		Object WORD548_tree=null;

		try {
			// JPA2.g:498:5: ( WORD )
			// JPA2.g:498:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD548=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_embeddable_object_field4396); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD548_tree = (Object)adaptor.create(WORD548);
			adaptor.addChild(root_0, WORD548_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:499:1: collection_valued_field : WORD ;
	public final JPA2Parser.collection_valued_field_return collection_valued_field() throws RecognitionException {
		JPA2Parser.collection_valued_field_return retval = new JPA2Parser.collection_valued_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD549=null;

		Object WORD549_tree=null;

		try {
			// JPA2.g:500:5: ( WORD )
			// JPA2.g:500:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD549=(Token)match(input,WORD,FOLLOW_WORD_in_collection_valued_field4407); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD549_tree = (Object)adaptor.create(WORD549);
			adaptor.addChild(root_0, WORD549_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:501:1: entity_name : WORD ;
	public final JPA2Parser.entity_name_return entity_name() throws RecognitionException {
		JPA2Parser.entity_name_return retval = new JPA2Parser.entity_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD550=null;

		Object WORD550_tree=null;

		try {
			// JPA2.g:502:5: ( WORD )
			// JPA2.g:502:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD550=(Token)match(input,WORD,FOLLOW_WORD_in_entity_name4418); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD550_tree = (Object)adaptor.create(WORD550);
			adaptor.addChild(root_0, WORD550_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:503:1: subtype : WORD ;
	public final JPA2Parser.subtype_return subtype() throws RecognitionException {
		JPA2Parser.subtype_return retval = new JPA2Parser.subtype_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD551=null;

		Object WORD551_tree=null;

		try {
			// JPA2.g:504:5: ( WORD )
			// JPA2.g:504:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD551=(Token)match(input,WORD,FOLLOW_WORD_in_subtype4429); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD551_tree = (Object)adaptor.create(WORD551);
			adaptor.addChild(root_0, WORD551_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:505:1: entity_type_literal : WORD ;
	public final JPA2Parser.entity_type_literal_return entity_type_literal() throws RecognitionException {
		JPA2Parser.entity_type_literal_return retval = new JPA2Parser.entity_type_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD552=null;

		Object WORD552_tree=null;

		try {
			// JPA2.g:506:5: ( WORD )
			// JPA2.g:506:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD552=(Token)match(input,WORD,FOLLOW_WORD_in_entity_type_literal4440); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD552_tree = (Object)adaptor.create(WORD552);
			adaptor.addChild(root_0, WORD552_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:507:1: function_name : STRING_LITERAL ;
	public final JPA2Parser.function_name_return function_name() throws RecognitionException {
		JPA2Parser.function_name_return retval = new JPA2Parser.function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL553=null;

		Object STRING_LITERAL553_tree=null;

		try {
			// JPA2.g:508:5: ( STRING_LITERAL )
			// JPA2.g:508:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL553=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_function_name4451); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL553_tree = (Object)adaptor.create(STRING_LITERAL553);
			adaptor.addChild(root_0, STRING_LITERAL553_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:509:1: state_field : WORD ;
	public final JPA2Parser.state_field_return state_field() throws RecognitionException {
		JPA2Parser.state_field_return retval = new JPA2Parser.state_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD554=null;

		Object WORD554_tree=null;

		try {
			// JPA2.g:510:5: ( WORD )
			// JPA2.g:510:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD554=(Token)match(input,WORD,FOLLOW_WORD_in_state_field4462); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD554_tree = (Object)adaptor.create(WORD554);
			adaptor.addChild(root_0, WORD554_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:511:1: result_variable : WORD ;
	public final JPA2Parser.result_variable_return result_variable() throws RecognitionException {
		JPA2Parser.result_variable_return retval = new JPA2Parser.result_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD555=null;

		Object WORD555_tree=null;

		try {
			// JPA2.g:512:5: ( WORD )
			// JPA2.g:512:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD555=(Token)match(input,WORD,FOLLOW_WORD_in_result_variable4473); if (state.failed) return retval;
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
	// $ANTLR end "result_variable"


	public static class superquery_identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "superquery_identification_variable"
	// JPA2.g:513:1: superquery_identification_variable : WORD ;
	public final JPA2Parser.superquery_identification_variable_return superquery_identification_variable() throws RecognitionException {
		JPA2Parser.superquery_identification_variable_return retval = new JPA2Parser.superquery_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD556=null;

		Object WORD556_tree=null;

		try {
			// JPA2.g:514:5: ( WORD )
			// JPA2.g:514:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD556=(Token)match(input,WORD,FOLLOW_WORD_in_superquery_identification_variable4484); if (state.failed) return retval;
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
	// $ANTLR end "superquery_identification_variable"


	public static class date_time_timestamp_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_time_timestamp_literal"
	// JPA2.g:515:1: date_time_timestamp_literal : WORD ;
	public final JPA2Parser.date_time_timestamp_literal_return date_time_timestamp_literal() throws RecognitionException {
		JPA2Parser.date_time_timestamp_literal_return retval = new JPA2Parser.date_time_timestamp_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD557=null;

		Object WORD557_tree=null;

		try {
			// JPA2.g:516:5: ( WORD )
			// JPA2.g:516:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD557=(Token)match(input,WORD,FOLLOW_WORD_in_date_time_timestamp_literal4495); if (state.failed) return retval;
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
	// $ANTLR end "date_time_timestamp_literal"


	public static class pattern_value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "pattern_value"
	// JPA2.g:517:1: pattern_value : string_literal ;
	public final JPA2Parser.pattern_value_return pattern_value() throws RecognitionException {
		JPA2Parser.pattern_value_return retval = new JPA2Parser.pattern_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal558 =null;


		try {
			// JPA2.g:518:5: ( string_literal )
			// JPA2.g:518:7: string_literal
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_literal_in_pattern_value4506);
			string_literal558=string_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal558.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:519:1: collection_valued_input_parameter : input_parameter ;
	public final JPA2Parser.collection_valued_input_parameter_return collection_valued_input_parameter() throws RecognitionException {
		JPA2Parser.collection_valued_input_parameter_return retval = new JPA2Parser.collection_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter559 =null;


		try {
			// JPA2.g:520:5: ( input_parameter )
			// JPA2.g:520:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_collection_valued_input_parameter4517);
			input_parameter559=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter559.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:521:1: single_valued_input_parameter : input_parameter ;
	public final JPA2Parser.single_valued_input_parameter_return single_valued_input_parameter() throws RecognitionException {
		JPA2Parser.single_valued_input_parameter_return retval = new JPA2Parser.single_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter560 =null;


		try {
			// JPA2.g:522:5: ( input_parameter )
			// JPA2.g:522:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_single_valued_input_parameter4528);
			input_parameter560=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter560.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
		int alt135=2;
		int LA135_0 = input.LA(1);
		if ( (LA135_0==DISTINCT) ) {
			alt135=1;
		}
		switch (alt135) {
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
		int alt136=2;
		int LA136_0 = input.LA(1);
		if ( (LA136_0==DISTINCT) ) {
			alt136=1;
		}
		switch (alt136) {
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

	// $ANTLR start synpred63_JPA2
	public final void synpred63_JPA2_fragment() throws RecognitionException {
		// JPA2.g:206:25: ( general_identification_variable )
		// JPA2.g:206:25: general_identification_variable
		{
		pushFollow(FOLLOW_general_identification_variable_in_synpred63_JPA21792);
		general_identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred63_JPA2

	// $ANTLR start synpred71_JPA2
	public final void synpred71_JPA2_fragment() throws RecognitionException {
		// JPA2.g:220:7: ( general_derived_path '.' single_valued_object_field )
		// JPA2.g:220:7: general_derived_path '.' single_valued_object_field
		{
		pushFollow(FOLLOW_general_derived_path_in_synpred71_JPA21966);
		general_derived_path();
		state._fsp--;
		if (state.failed) return;

		match(input,60,FOLLOW_60_in_synpred71_JPA21967); if (state.failed) return;

		pushFollow(FOLLOW_single_valued_object_field_in_synpred71_JPA21968);
		single_valued_object_field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred71_JPA2

	// $ANTLR start synpred76_JPA2
	public final void synpred76_JPA2_fragment() throws RecognitionException {
		// JPA2.g:238:7: ( path_expression )
		// JPA2.g:238:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred76_JPA22120);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred76_JPA2

	// $ANTLR start synpred77_JPA2
	public final void synpred77_JPA2_fragment() throws RecognitionException {
		// JPA2.g:239:7: ( scalar_expression )
		// JPA2.g:239:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred77_JPA22128);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred77_JPA2

	// $ANTLR start synpred78_JPA2
	public final void synpred78_JPA2_fragment() throws RecognitionException {
		// JPA2.g:240:7: ( aggregate_expression )
		// JPA2.g:240:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred78_JPA22136);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred78_JPA2

	// $ANTLR start synpred79_JPA2
	public final void synpred79_JPA2_fragment() throws RecognitionException {
		// JPA2.g:243:7: ( arithmetic_expression )
		// JPA2.g:243:7: arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred79_JPA22155);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred79_JPA2

	// $ANTLR start synpred80_JPA2
	public final void synpred80_JPA2_fragment() throws RecognitionException {
		// JPA2.g:244:7: ( string_expression )
		// JPA2.g:244:7: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred80_JPA22163);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred80_JPA2

	// $ANTLR start synpred81_JPA2
	public final void synpred81_JPA2_fragment() throws RecognitionException {
		// JPA2.g:245:7: ( enum_expression )
		// JPA2.g:245:7: enum_expression
		{
		pushFollow(FOLLOW_enum_expression_in_synpred81_JPA22171);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred81_JPA2

	// $ANTLR start synpred82_JPA2
	public final void synpred82_JPA2_fragment() throws RecognitionException {
		// JPA2.g:246:7: ( datetime_expression )
		// JPA2.g:246:7: datetime_expression
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred82_JPA22179);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred82_JPA2

	// $ANTLR start synpred83_JPA2
	public final void synpred83_JPA2_fragment() throws RecognitionException {
		// JPA2.g:247:7: ( boolean_expression )
		// JPA2.g:247:7: boolean_expression
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred83_JPA22187);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred83_JPA2

	// $ANTLR start synpred84_JPA2
	public final void synpred84_JPA2_fragment() throws RecognitionException {
		// JPA2.g:248:7: ( case_expression )
		// JPA2.g:248:7: case_expression
		{
		pushFollow(FOLLOW_case_expression_in_synpred84_JPA22195);
		case_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred84_JPA2

	// $ANTLR start synpred87_JPA2
	public final void synpred87_JPA2_fragment() throws RecognitionException {
		// JPA2.g:255:8: ( 'NOT' )
		// JPA2.g:255:8: 'NOT'
		{
		match(input,NOT,FOLLOW_NOT_in_synpred87_JPA22255); if (state.failed) return;

		}

	}
	// $ANTLR end synpred87_JPA2

	// $ANTLR start synpred88_JPA2
	public final void synpred88_JPA2_fragment() throws RecognitionException {
		// JPA2.g:257:7: ( simple_cond_expression )
		// JPA2.g:257:7: simple_cond_expression
		{
		pushFollow(FOLLOW_simple_cond_expression_in_synpred88_JPA22270);
		simple_cond_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred88_JPA2

	// $ANTLR start synpred89_JPA2
	public final void synpred89_JPA2_fragment() throws RecognitionException {
		// JPA2.g:261:7: ( comparison_expression )
		// JPA2.g:261:7: comparison_expression
		{
		pushFollow(FOLLOW_comparison_expression_in_synpred89_JPA22307);
		comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred89_JPA2

	// $ANTLR start synpred90_JPA2
	public final void synpred90_JPA2_fragment() throws RecognitionException {
		// JPA2.g:262:7: ( between_expression )
		// JPA2.g:262:7: between_expression
		{
		pushFollow(FOLLOW_between_expression_in_synpred90_JPA22315);
		between_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred90_JPA2

	// $ANTLR start synpred91_JPA2
	public final void synpred91_JPA2_fragment() throws RecognitionException {
		// JPA2.g:263:7: ( in_expression )
		// JPA2.g:263:7: in_expression
		{
		pushFollow(FOLLOW_in_expression_in_synpred91_JPA22323);
		in_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred91_JPA2

	// $ANTLR start synpred92_JPA2
	public final void synpred92_JPA2_fragment() throws RecognitionException {
		// JPA2.g:264:7: ( like_expression )
		// JPA2.g:264:7: like_expression
		{
		pushFollow(FOLLOW_like_expression_in_synpred92_JPA22331);
		like_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred92_JPA2

	// $ANTLR start synpred93_JPA2
	public final void synpred93_JPA2_fragment() throws RecognitionException {
		// JPA2.g:265:7: ( null_comparison_expression )
		// JPA2.g:265:7: null_comparison_expression
		{
		pushFollow(FOLLOW_null_comparison_expression_in_synpred93_JPA22339);
		null_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred93_JPA2

	// $ANTLR start synpred94_JPA2
	public final void synpred94_JPA2_fragment() throws RecognitionException {
		// JPA2.g:266:7: ( empty_collection_comparison_expression )
		// JPA2.g:266:7: empty_collection_comparison_expression
		{
		pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred94_JPA22347);
		empty_collection_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred94_JPA2

	// $ANTLR start synpred95_JPA2
	public final void synpred95_JPA2_fragment() throws RecognitionException {
		// JPA2.g:267:7: ( collection_member_expression )
		// JPA2.g:267:7: collection_member_expression
		{
		pushFollow(FOLLOW_collection_member_expression_in_synpred95_JPA22355);
		collection_member_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred95_JPA2

	// $ANTLR start synpred114_JPA2
	public final void synpred114_JPA2_fragment() throws RecognitionException {
		// JPA2.g:296:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
		// JPA2.g:296:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred114_JPA22608);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:296:29: ( 'NOT' )?
		int alt139=2;
		int LA139_0 = input.LA(1);
		if ( (LA139_0==NOT) ) {
			alt139=1;
		}
		switch (alt139) {
			case 1 :
				// JPA2.g:296:30: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred114_JPA22611); if (state.failed) return;

				}
				break;

		}

		match(input,79,FOLLOW_79_in_synpred114_JPA22615); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred114_JPA22617);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred114_JPA22619); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred114_JPA22621);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred114_JPA2

	// $ANTLR start synpred116_JPA2
	public final void synpred116_JPA2_fragment() throws RecognitionException {
		// JPA2.g:297:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
		// JPA2.g:297:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred116_JPA22629);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:297:25: ( 'NOT' )?
		int alt140=2;
		int LA140_0 = input.LA(1);
		if ( (LA140_0==NOT) ) {
			alt140=1;
		}
		switch (alt140) {
			case 1 :
				// JPA2.g:297:26: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred116_JPA22632); if (state.failed) return;

				}
				break;

		}

		match(input,79,FOLLOW_79_in_synpred116_JPA22636); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred116_JPA22638);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred116_JPA22640); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred116_JPA22642);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred116_JPA2

	// $ANTLR start synpred129_JPA2
	public final void synpred129_JPA2_fragment() throws RecognitionException {
		// JPA2.g:315:8: ( path_expression )
		// JPA2.g:315:8: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred129_JPA22846);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred129_JPA2

	// $ANTLR start synpred137_JPA2
	public final void synpred137_JPA2_fragment() throws RecognitionException {
		// JPA2.g:325:7: ( identification_variable )
		// JPA2.g:325:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred137_JPA22948);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred137_JPA2

	// $ANTLR start synpred143_JPA2
	public final void synpred143_JPA2_fragment() throws RecognitionException {
		// JPA2.g:333:7: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) )
		// JPA2.g:333:7: string_expression comparison_operator ( string_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_string_expression_in_synpred143_JPA23017);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred143_JPA23019);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:333:45: ( string_expression | all_or_any_expression )
		int alt142=2;
		int LA142_0 = input.LA(1);
		if ( (LA142_0==AVG||LA142_0==COUNT||(LA142_0 >= LOWER && LA142_0 <= NAMED_PARAMETER)||(LA142_0 >= STRING_LITERAL && LA142_0 <= SUM)||LA142_0==WORD||LA142_0==55||LA142_0==69||(LA142_0 >= 81 && LA142_0 <= 83)||LA142_0==96||LA142_0==112||LA142_0==122||LA142_0==126||LA142_0==129) ) {
			alt142=1;
		}
		else if ( ((LA142_0 >= 76 && LA142_0 <= 77)||LA142_0==120) ) {
			alt142=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 142, 0, input);
			throw nvae;
		}

		switch (alt142) {
			case 1 :
				// JPA2.g:333:46: string_expression
				{
				pushFollow(FOLLOW_string_expression_in_synpred143_JPA23022);
				string_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:333:66: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred143_JPA23026);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred143_JPA2

	// $ANTLR start synpred146_JPA2
	public final void synpred146_JPA2_fragment() throws RecognitionException {
		// JPA2.g:334:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
		// JPA2.g:334:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred146_JPA23035);
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
		// JPA2.g:334:39: ( boolean_expression | all_or_any_expression )
		int alt143=2;
		int LA143_0 = input.LA(1);
		if ( (LA143_0==LPAREN||LA143_0==NAMED_PARAMETER||LA143_0==WORD||LA143_0==55||LA143_0==69||(LA143_0 >= 81 && LA143_0 <= 82)||LA143_0==96||LA143_0==112||(LA143_0 >= 134 && LA143_0 <= 135)) ) {
			alt143=1;
		}
		else if ( ((LA143_0 >= 76 && LA143_0 <= 77)||LA143_0==120) ) {
			alt143=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 143, 0, input);
			throw nvae;
		}

		switch (alt143) {
			case 1 :
				// JPA2.g:334:40: boolean_expression
				{
				pushFollow(FOLLOW_boolean_expression_in_synpred146_JPA23046);
				boolean_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:334:61: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred146_JPA23050);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred146_JPA2

	// $ANTLR start synpred149_JPA2
	public final void synpred149_JPA2_fragment() throws RecognitionException {
		// JPA2.g:335:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
		// JPA2.g:335:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_enum_expression_in_synpred149_JPA23059);
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
		// JPA2.g:335:34: ( enum_expression | all_or_any_expression )
		int alt144=2;
		int LA144_0 = input.LA(1);
		if ( (LA144_0==LPAREN||LA144_0==NAMED_PARAMETER||LA144_0==WORD||LA144_0==55||LA144_0==69||(LA144_0 >= 81 && LA144_0 <= 82)||LA144_0==112) ) {
			alt144=1;
		}
		else if ( ((LA144_0 >= 76 && LA144_0 <= 77)||LA144_0==120) ) {
			alt144=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 144, 0, input);
			throw nvae;
		}

		switch (alt144) {
			case 1 :
				// JPA2.g:335:35: enum_expression
				{
				pushFollow(FOLLOW_enum_expression_in_synpred149_JPA23068);
				enum_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:335:53: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred149_JPA23072);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred149_JPA2

	// $ANTLR start synpred151_JPA2
	public final void synpred151_JPA2_fragment() throws RecognitionException {
		// JPA2.g:336:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
		// JPA2.g:336:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred151_JPA23081);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred151_JPA23083);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:336:47: ( datetime_expression | all_or_any_expression )
		int alt145=2;
		int LA145_0 = input.LA(1);
		if ( (LA145_0==AVG||LA145_0==COUNT||(LA145_0 >= LPAREN && LA145_0 <= NAMED_PARAMETER)||LA145_0==SUM||LA145_0==WORD||LA145_0==55||LA145_0==69||(LA145_0 >= 81 && LA145_0 <= 82)||(LA145_0 >= 84 && LA145_0 <= 86)||LA145_0==96||LA145_0==112) ) {
			alt145=1;
		}
		else if ( ((LA145_0 >= 76 && LA145_0 <= 77)||LA145_0==120) ) {
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
				// JPA2.g:336:48: datetime_expression
				{
				pushFollow(FOLLOW_datetime_expression_in_synpred151_JPA23086);
				datetime_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:336:70: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred151_JPA23090);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred151_JPA2

	// $ANTLR start synpred154_JPA2
	public final void synpred154_JPA2_fragment() throws RecognitionException {
		// JPA2.g:337:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
		// JPA2.g:337:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_entity_expression_in_synpred154_JPA23099);
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
		// JPA2.g:337:38: ( entity_expression | all_or_any_expression )
		int alt146=2;
		int LA146_0 = input.LA(1);
		if ( (LA146_0==NAMED_PARAMETER||LA146_0==WORD||LA146_0==55||LA146_0==69) ) {
			alt146=1;
		}
		else if ( ((LA146_0 >= 76 && LA146_0 <= 77)||LA146_0==120) ) {
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
				// JPA2.g:337:39: entity_expression
				{
				pushFollow(FOLLOW_entity_expression_in_synpred154_JPA23110);
				entity_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:337:59: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred154_JPA23114);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred154_JPA2

	// $ANTLR start synpred156_JPA2
	public final void synpred156_JPA2_fragment() throws RecognitionException {
		// JPA2.g:338:7: ( entity_type_expression ( '=' | '<>' ) entity_type_expression )
		// JPA2.g:338:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
		{
		pushFollow(FOLLOW_entity_type_expression_in_synpred156_JPA23123);
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
		pushFollow(FOLLOW_entity_type_expression_in_synpred156_JPA23133);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred156_JPA2

	// $ANTLR start synpred163_JPA2
	public final void synpred163_JPA2_fragment() throws RecognitionException {
		// JPA2.g:349:7: ( arithmetic_term )
		// JPA2.g:349:7: arithmetic_term
		{
		pushFollow(FOLLOW_arithmetic_term_in_synpred163_JPA23214);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred163_JPA2

	// $ANTLR start synpred165_JPA2
	public final void synpred165_JPA2_fragment() throws RecognitionException {
		// JPA2.g:352:7: ( arithmetic_factor )
		// JPA2.g:352:7: arithmetic_factor
		{
		pushFollow(FOLLOW_arithmetic_factor_in_synpred165_JPA23243);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred165_JPA2

	// $ANTLR start synpred171_JPA2
	public final void synpred171_JPA2_fragment() throws RecognitionException {
		// JPA2.g:359:7: ( '(' arithmetic_expression ')' )
		// JPA2.g:359:7: '(' arithmetic_expression ')'
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred171_JPA23312); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred171_JPA23313);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred171_JPA23314); if (state.failed) return;

		}

	}
	// $ANTLR end synpred171_JPA2

	// $ANTLR start synpred174_JPA2
	public final void synpred174_JPA2_fragment() throws RecognitionException {
		// JPA2.g:362:7: ( aggregate_expression )
		// JPA2.g:362:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred174_JPA23338);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred174_JPA2

	// $ANTLR start synpred176_JPA2
	public final void synpred176_JPA2_fragment() throws RecognitionException {
		// JPA2.g:364:7: ( function_invocation )
		// JPA2.g:364:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred176_JPA23354);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred176_JPA2

	// $ANTLR start synpred181_JPA2
	public final void synpred181_JPA2_fragment() throws RecognitionException {
		// JPA2.g:371:7: ( aggregate_expression )
		// JPA2.g:371:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred181_JPA23405);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred181_JPA2

	// $ANTLR start synpred183_JPA2
	public final void synpred183_JPA2_fragment() throws RecognitionException {
		// JPA2.g:373:7: ( function_invocation )
		// JPA2.g:373:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred183_JPA23421);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred183_JPA2

	// $ANTLR start synpred184_JPA2
	public final void synpred184_JPA2_fragment() throws RecognitionException {
		// JPA2.g:376:7: ( path_expression )
		// JPA2.g:376:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred184_JPA23440);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred184_JPA2

	// $ANTLR start synpred187_JPA2
	public final void synpred187_JPA2_fragment() throws RecognitionException {
		// JPA2.g:379:7: ( aggregate_expression )
		// JPA2.g:379:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred187_JPA23464);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred187_JPA2

	// $ANTLR start synpred189_JPA2
	public final void synpred189_JPA2_fragment() throws RecognitionException {
		// JPA2.g:381:7: ( function_invocation )
		// JPA2.g:381:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred189_JPA23480);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred189_JPA2

	// $ANTLR start synpred190_JPA2
	public final void synpred190_JPA2_fragment() throws RecognitionException {
		// JPA2.g:382:7: ( date_time_timestamp_literal )
		// JPA2.g:382:7: date_time_timestamp_literal
		{
		pushFollow(FOLLOW_date_time_timestamp_literal_in_synpred190_JPA23488);
		date_time_timestamp_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred190_JPA2

	// $ANTLR start synpred227_JPA2
	public final void synpred227_JPA2_fragment() throws RecognitionException {
		// JPA2.g:432:7: ( literal )
		// JPA2.g:432:7: literal
		{
		pushFollow(FOLLOW_literal_in_synpred227_JPA23935);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred227_JPA2

	// $ANTLR start synpred229_JPA2
	public final void synpred229_JPA2_fragment() throws RecognitionException {
		// JPA2.g:434:7: ( input_parameter )
		// JPA2.g:434:7: input_parameter
		{
		pushFollow(FOLLOW_input_parameter_in_synpred229_JPA23951);
		input_parameter();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred229_JPA2

	// Delegated rules

	public final boolean synpred165_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred165_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
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
	public final boolean synpred77_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred77_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred151_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred151_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred71_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred71_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred227_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred227_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred114_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred114_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred171_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred171_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred76_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred76_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred229_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred229_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred184_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred184_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred189_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred189_JPA2_fragment(); // can never throw exception
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
	protected DFA46 dfa46 = new DFA46(this);
	static final String DFA40_eotS =
		"\14\uffff";
	static final String DFA40_eofS =
		"\14\uffff";
	static final String DFA40_minS =
		"\1\6\1\27\2\uffff\1\13\1\65\1\37\1\uffff\1\16\1\37\1\0\1\16";
	static final String DFA40_maxS =
		"\1\140\1\27\2\uffff\2\65\1\74\1\uffff\1\65\1\74\1\0\1\65";
	static final String DFA40_acceptS =
		"\2\uffff\1\1\1\3\3\uffff\1\2\4\uffff";
	static final String DFA40_specialS =
		"\12\uffff\1\0\1\uffff}>";
	static final String[] DFA40_transitionS = {
			"\1\2\2\uffff\1\1\16\uffff\2\2\10\uffff\1\2\75\uffff\1\3",
			"\1\4",
			"",
			"",
			"\1\5\51\uffff\1\6",
			"\1\6",
			"\1\7\34\uffff\1\10",
			"",
			"\1\11\20\uffff\1\12\25\uffff\1\11",
			"\1\12\34\uffff\1\13",
			"\1\uffff",
			"\1\11\20\uffff\1\12\25\uffff\1\11"
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
						int LA40_10 = input.LA(1);
						 
						int index40_10 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred49_JPA2()) ) {s = 2;}
						else if ( (synpred51_JPA2()) ) {s = 7;}
						 
						input.seek(index40_10);
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

	static final String DFA46_eotS =
		"\15\uffff";
	static final String DFA46_eofS =
		"\1\uffff\1\5\2\uffff\1\5\4\uffff\4\5";
	static final String DFA46_minS =
		"\1\65\1\5\2\65\1\5\2\uffff\2\37\4\5";
	static final String DFA46_maxS =
		"\1\u0082\1\74\2\65\1\72\2\uffff\2\37\1\74\3\72";
	static final String DFA46_acceptS =
		"\5\uffff\1\1\1\2\6\uffff";
	static final String DFA46_specialS =
		"\15\uffff}>";
	static final String[] DFA46_transitionS = {
			"\1\1\56\uffff\1\2\35\uffff\1\3",
			"\1\5\4\uffff\1\6\57\uffff\1\5\1\uffff\1\4",
			"\1\7",
			"\1\10",
			"\1\5\4\uffff\1\6\3\uffff\1\11\46\uffff\1\11\4\uffff\1\5",
			"",
			"",
			"\1\12",
			"\1\13",
			"\1\5\4\uffff\1\6\57\uffff\1\5\1\uffff\1\14",
			"\1\5\4\uffff\1\6\57\uffff\1\5",
			"\1\5\4\uffff\1\6\57\uffff\1\5",
			"\1\5\4\uffff\1\6\3\uffff\1\11\46\uffff\1\11\4\uffff\1\5"
	};

	static final short[] DFA46_eot = DFA.unpackEncodedString(DFA46_eotS);
	static final short[] DFA46_eof = DFA.unpackEncodedString(DFA46_eofS);
	static final char[] DFA46_min = DFA.unpackEncodedStringToUnsignedChars(DFA46_minS);
	static final char[] DFA46_max = DFA.unpackEncodedStringToUnsignedChars(DFA46_maxS);
	static final short[] DFA46_accept = DFA.unpackEncodedString(DFA46_acceptS);
	static final short[] DFA46_special = DFA.unpackEncodedString(DFA46_specialS);
	static final short[][] DFA46_transition;

	static {
		int numStates = DFA46_transitionS.length;
		DFA46_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA46_transition[i] = DFA.unpackEncodedString(DFA46_transitionS[i]);
		}
	}

	protected class DFA46 extends DFA {

		public DFA46(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 46;
			this.eot = DFA46_eot;
			this.eof = DFA46_eof;
			this.min = DFA46_min;
			this.max = DFA46_max;
			this.accept = DFA46_accept;
			this.special = DFA46_special;
			this.transition = DFA46_transition;
		}
		@Override
		public String getDescription() {
			return "200:1: orderby_item : ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) );";
		}
	}

	public static final BitSet FOLLOW_select_statement_in_ql_statement427 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_update_statement_in_ql_statement431 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_delete_statement_in_ql_statement435 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_117_in_select_statement450 = new BitSet(new long[]{0x4AA0000607C40A40L,0xC6832945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_clause_in_select_statement452 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_from_clause_in_select_statement454 = new BitSet(new long[]{0x000000002000C002L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_select_statement457 = new BitSet(new long[]{0x000000002000C002L});
	public static final BitSet FOLLOW_groupby_clause_in_select_statement462 = new BitSet(new long[]{0x0000000020008002L});
	public static final BitSet FOLLOW_having_clause_in_select_statement467 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_orderby_clause_in_select_statement472 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_128_in_update_statement530 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_update_clause_in_update_statement532 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_update_statement535 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_88_in_delete_statement571 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_delete_statement573 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_delete_clause_in_delete_statement575 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_delete_statement578 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_95_in_from_clause616 = new BitSet(new long[]{0x0020000000000000L});
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
	public static final BitSet FOLLOW_join_spec_in_join783 = new BitSet(new long[]{0x0020000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_join785 = new BitSet(new long[]{0x0020000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_join788 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join792 = new BitSet(new long[]{0x0000000000000002L,0x0008000000000000L});
	public static final BitSet FOLLOW_115_in_join795 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_join797 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_fetch_join828 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_FETCH_in_fetch_join830 = new BitSet(new long[]{0x0020000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join832 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFT_in_join_spec846 = new BitSet(new long[]{0x0000000040080000L});
	public static final BitSet FOLLOW_OUTER_in_join_spec850 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_INNER_in_join_spec856 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_JOIN_in_join_spec861 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression875 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression877 = new BitSet(new long[]{0x0020000000004002L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression880 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression881 = new BitSet(new long[]{0x0020000000004002L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression885 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_125_in_join_association_path_expression920 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression922 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression924 = new BitSet(new long[]{0x0020000000004000L,0x0000000000004000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression927 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression928 = new BitSet(new long[]{0x0020000000004000L,0x0000000000004000L});
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
	public static final BitSet FOLLOW_92_in_qualified_identification_variable1034 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_qualified_identification_variable1035 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_qualified_identification_variable1036 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_100_in_map_field_identification_variable1043 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1044 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1045 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_130_in_map_field_identification_variable1049 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1050 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1051 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_path_expression1065 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_path_expression1067 = new BitSet(new long[]{0x0020000000004002L});
	public static final BitSet FOLLOW_field_in_path_expression1070 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_path_expression1071 = new BitSet(new long[]{0x0020000000004002L});
	public static final BitSet FOLLOW_field_in_path_expression1075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_general_identification_variable1114 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_general_identification_variable1122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_update_clause1135 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
	public static final BitSet FOLLOW_118_in_update_clause1137 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1139 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_update_clause1142 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1144 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_path_expression_in_update_item1172 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_update_item1174 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6818945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_new_value_in_update_item1176 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_new_value1187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_new_value1195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_111_in_new_value1203 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_delete_clause1215 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_select_clause1242 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6832945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_item_in_select_clause1246 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_select_clause1249 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6832945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_item_in_select_clause1251 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_select_expression_in_select_item1294 = new BitSet(new long[]{0x0020000000000002L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_select_item1298 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_result_variable_in_select_item1302 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_select_expression1315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1323 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1341 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_select_expression1349 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_113_in_select_expression1357 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_select_expression1359 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1360 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_select_expression1361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_constructor_expression_in_select_expression1369 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_109_in_constructor_expression1380 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_constructor_name_in_constructor_expression1382 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_constructor_expression1384 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1386 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_constructor_expression1389 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
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
	public static final BitSet FOLLOW_132_in_where_clause1583 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_where_clause1585 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_groupby_clause1607 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_groupby_clause1609 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1611 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_groupby_clause1614 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1616 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_path_expression_in_groupby_item1650 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_groupby_item1654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HAVING_in_having_clause1665 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_having_clause1667 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderby_clause1678 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_orderby_clause1680 = new BitSet(new long[]{0x0020000000000000L,0x0000001000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1682 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_orderby_clause1685 = new BitSet(new long[]{0x0020000000000000L,0x0000001000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1687 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1721 = new BitSet(new long[]{0x0000000000000022L});
	public static final BitSet FOLLOW_ASC_in_orderby_item1724 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1756 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_DESC_in_orderby_item1759 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_orderby_variable1788 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_orderby_variable1792 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_orderby_variable1796 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_subquery1810 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_117_in_subquery1812 = new BitSet(new long[]{0x4AA0000607C40A40L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_simple_select_clause_in_subquery1814 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_subquery_from_clause_in_subquery1816 = new BitSet(new long[]{0x000000008000C000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_subquery1819 = new BitSet(new long[]{0x000000008000C000L});
	public static final BitSet FOLLOW_groupby_clause_in_subquery1824 = new BitSet(new long[]{0x0000000080008000L});
	public static final BitSet FOLLOW_having_clause_in_subquery1829 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_subquery1835 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_95_in_subquery_from_clause1885 = new BitSet(new long[]{0x0020000000010000L,0x2000000000000000L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1887 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_subquery_from_clause1890 = new BitSet(new long[]{0x0020000000010000L,0x2000000000000000L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1892 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1930 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1938 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_subselect_identification_variable_declaration1940 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration1942 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_join_in_subselect_identification_variable_declaration1945 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1955 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1966 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_path_expression1967 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_path_expression1968 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1976 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_path_expression1977 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_path_expression1978 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_derived_path_in_general_derived_path1989 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_treated_derived_path_in_general_derived_path1997 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_general_derived_path1999 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_general_derived_path2000 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_simple_derived_path2018 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_125_in_treated_derived_path2035 = new BitSet(new long[]{0x0020000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_general_derived_path_in_treated_derived_path2036 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_treated_derived_path2038 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_subtype_in_treated_derived_path2040 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_treated_derived_path2042 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_derived_collection_member_declaration2053 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2055 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_collection_member_declaration2056 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2058 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_collection_member_declaration2060 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_collection_member_declaration2063 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause2076 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause2080 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_simple_select_expression2120 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_select_expression2128 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression2136 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_select_expression2144 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_scalar_expression2155 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_scalar_expression2163 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_scalar_expression2171 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_scalar_expression2179 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_scalar_expression2187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_scalar_expression2195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_scalar_expression2203 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2215 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_OR_in_conditional_expression2219 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2221 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2235 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_conditional_term2239 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2241 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_NOT_in_conditional_factor2255 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_primary_in_conditional_factor2259 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_conditional_primary2270 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_conditional_primary2294 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_conditional_primary2295 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_conditional_primary2296 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression2307 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_simple_cond_expression2315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_simple_cond_expression2323 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_simple_cond_expression2331 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression2339 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2347 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression2355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression2363 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression2371 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2384 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2392 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2408 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2416 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_70_in_date_between_macro_expression2428 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2430 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2432 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_between_macro_expression2434 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_date_between_macro_expression2436 = new BitSet(new long[]{0x0E00000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2439 = new BitSet(new long[]{0x4000000000040000L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2447 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_between_macro_expression2451 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_date_between_macro_expression2453 = new BitSet(new long[]{0x0E00000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2456 = new BitSet(new long[]{0x4000000000040000L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2464 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_between_macro_expression2468 = new BitSet(new long[]{0x0000000000000000L,0x0010140200800000L,0x0000000000000020L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2470 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2493 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_72_in_date_before_macro_expression2505 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2507 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2509 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_before_macro_expression2511 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2514 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2518 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2521 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_date_after_macro_expression2533 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2535 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2537 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_after_macro_expression2539 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2542 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2546 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2549 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_73_in_date_equals_macro_expression2561 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2563 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2565 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_equals_macro_expression2567 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2570 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2574 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2577 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_74_in_date_today_macro_expression2589 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2591 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2593 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2595 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2608 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2611 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_between_expression2615 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2617 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2619 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2621 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2629 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2632 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_between_expression2636 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2638 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2640 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2642 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2650 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2653 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_between_expression2657 = new BitSet(new long[]{0x00A0000407800240L,0x0001000100760020L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2659 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2661 = new BitSet(new long[]{0x00A0000407800240L,0x0001000100760020L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2663 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2675 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_type_discriminator_in_in_expression2679 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_identification_variable_in_in_expression2683 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_NOT_in_in_expression2687 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_IN_in_in_expression2691 = new BitSet(new long[]{0x0080000004800000L,0x0000000000000020L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2707 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_in_item_in_in_expression2709 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_in_expression2712 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_in_item_in_in_expression2714 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2718 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_in_expression2734 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_valued_input_parameter_in_in_expression2750 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2766 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2768 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2770 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_in_item2798 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_single_valued_input_parameter_in_in_item2802 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2813 = new BitSet(new long[]{0x0000000008000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_NOT_in_like_expression2816 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_103_in_like_expression2820 = new BitSet(new long[]{0x0080000204000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_pattern_value_in_like_expression2823 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
	public static final BitSet FOLLOW_input_parameter_in_like_expression2827 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
	public static final BitSet FOLLOW_93_in_like_expression2830 = new BitSet(new long[]{0x0000000A00000000L});
	public static final BitSet FOLLOW_escape_character_in_like_expression2832 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_null_comparison_expression2846 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression2850 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_null_comparison_expression2854 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_99_in_null_comparison_expression2857 = new BitSet(new long[]{0x0000000008000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_NOT_in_null_comparison_expression2860 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_111_in_null_comparison_expression2864 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression2875 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_99_in_empty_collection_comparison_expression2877 = new BitSet(new long[]{0x0000000008000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_NOT_in_empty_collection_comparison_expression2880 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_90_in_empty_collection_comparison_expression2884 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_or_value_expression_in_collection_member_expression2895 = new BitSet(new long[]{0x0000000008000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_NOT_in_collection_member_expression2899 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_collection_member_expression2903 = new BitSet(new long[]{0x0020000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_114_in_collection_member_expression2906 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_expression2910 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_or_value_expression2921 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2929 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_entity_or_value_expression2937 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_or_value_expression2948 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_or_value_expression2956 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simple_entity_or_value_expression2964 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_exists_expression2976 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_94_in_exists_expression2980 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_subquery_in_exists_expression2982 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_all_or_any_expression2993 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_subquery_in_all_or_any_expression3006 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3017 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3019 = new BitSet(new long[]{0x00A0000607C00240L,0x45010001000E3020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3022 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3026 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3035 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3037 = new BitSet(new long[]{0x00A0000004800000L,0x0101000100063020L,0x00000000000000C0L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3046 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3050 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3059 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3061 = new BitSet(new long[]{0x00A0000004800000L,0x0101000000063020L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3068 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3072 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3081 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3083 = new BitSet(new long[]{0x00A0000407800240L,0x0101000100763020L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3086 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3090 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3099 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3101 = new BitSet(new long[]{0x00A0000004000000L,0x0100000000003020L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3110 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3114 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3123 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3125 = new BitSet(new long[]{0x00A0000004000000L,0x8000000000000020L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3133 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3141 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3143 = new BitSet(new long[]{0x4AA0000407840240L,0x0381094500063820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3146 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3150 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3214 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3222 = new BitSet(new long[]{0x0A00000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_expression3224 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3251 = new BitSet(new long[]{0x2100000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_term3253 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3262 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor3285 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_arithmetic_primary3296 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary3304 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary3312 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_arithmetic_primary3313 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary3314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary3322 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary3330 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary3338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_arithmetic_primary3346 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_arithmetic_primary3354 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_arithmetic_primary3362 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_string_expression3373 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_string_expression3381 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_string_expression3389 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_strings_in_string_expression3397 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_string_expression3405 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_string_expression3413 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_string_expression3421 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_string_expression3429 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_datetime_expression3440 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_datetime_expression3448 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_expression3456 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_datetime_expression3464 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_datetime_expression3472 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_datetime_expression3480 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_datetime_expression3488 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_datetime_expression3496 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_boolean_expression3507 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_literal_in_boolean_expression3515 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_boolean_expression3523 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_boolean_expression3531 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_boolean_expression3539 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_boolean_expression3547 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_enum_expression3558 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_literal_in_enum_expression3566 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_enum_expression3574 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_enum_expression3582 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_enum_expression3590 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_expression3601 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3609 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3620 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3628 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_entity_type_expression3639 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_literal_in_entity_type_expression3647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_entity_type_expression3655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_127_in_type_discriminator3666 = new BitSet(new long[]{0x00A0000004000000L,0x0000001000000020L,0x0000000000000004L});
	public static final BitSet FOLLOW_general_identification_variable_in_type_discriminator3669 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_path_expression_in_type_discriminator3673 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_type_discriminator3677 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_type_discriminator3680 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_functions_returning_numerics3691 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3692 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3693 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_104_in_functions_returning_numerics3701 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3703 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_numerics3704 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3706 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_numerics3708 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3709 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3712 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_functions_returning_numerics3720 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3721 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3722 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_121_in_functions_returning_numerics3730 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3731 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3732 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_107_in_functions_returning_numerics3740 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3741 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_numerics3742 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3744 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3745 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_119_in_functions_returning_numerics3753 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3754 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3755 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_98_in_functions_returning_numerics3763 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_functions_returning_numerics3764 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3765 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_83_in_functions_returning_strings3803 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3804 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3805 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3807 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3810 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3812 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3815 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_122_in_functions_returning_strings3823 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3825 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3826 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3828 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3831 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3833 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3836 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_126_in_functions_returning_strings3844 = new BitSet(new long[]{0x00A0000E07C00240L,0x54010021800F0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings3847 = new BitSet(new long[]{0x0000000800000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_trim_character_in_functions_returning_strings3852 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_functions_returning_strings3856 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3860 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3862 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LOWER_in_functions_returning_strings3870 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3872 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3873 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3874 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_129_in_functions_returning_strings3882 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3883 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3884 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_96_in_function_invocation3914 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_function_name_in_function_invocation3915 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_function_invocation3918 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_function_arg_in_function_invocation3920 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_function_invocation3924 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_function_arg3935 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_function_arg3943 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_function_arg3951 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_function_arg3959 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_case_expression_in_case_expression3970 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_case_expression_in_case_expression3978 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coalesce_expression_in_case_expression3986 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullif_expression_in_case_expression3994 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_general_case_expression4005 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4007 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4010 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_89_in_general_case_expression4014 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_general_case_expression4016 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_91_in_general_case_expression4018 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_when_clause4029 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_when_clause4031 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_when_clause4033 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_when_clause4035 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_simple_case_expression4046 = new BitSet(new long[]{0x0020000000000000L,0x8000000000000000L});
	public static final BitSet FOLLOW_case_operand_in_simple_case_expression4048 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4050 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4053 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_89_in_simple_case_expression4057 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_case_expression4059 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_91_in_simple_case_expression4061 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_case_operand4072 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_case_operand4080 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_simple_when_clause4091 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4093 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_simple_when_clause4095 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4097 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_82_in_coalesce_expression4108 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4109 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_coalesce_expression4112 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4114 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_coalesce_expression4117 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_112_in_nullif_expression4128 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4129 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_nullif_expression4131 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4133 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_nullif_expression4134 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_69_in_input_parameter4147 = new BitSet(new long[]{0x4000000000040000L});
	public static final BitSet FOLLOW_numeric_literal_in_input_parameter4149 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter4172 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_55_in_input_parameter4193 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_WORD_in_input_parameter4195 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_136_in_input_parameter4197 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_literal4225 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4237 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_literal4249 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_identification_variable4298 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4310 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_parameter_name4313 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4316 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_TRIM_CHARACTER_in_trim_character4346 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string_literal4357 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_62_in_numeric_literal4369 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal4373 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_object_field4385 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_embeddable_object_field4396 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_collection_valued_field4407 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_name4418 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_subtype4429 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_type_literal4440 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_function_name4451 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_state_field4462 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_result_variable4473 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_superquery_identification_variable4484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_date_time_timestamp_literal4495 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_pattern_value4506 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_collection_valued_input_parameter4517 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_single_valued_input_parameter4528 = new BitSet(new long[]{0x0000000000000002L});
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
	public static final BitSet FOLLOW_general_identification_variable_in_synpred63_JPA21792 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_synpred71_JPA21966 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_synpred71_JPA21967 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_synpred71_JPA21968 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred76_JPA22120 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred77_JPA22128 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred78_JPA22136 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred79_JPA22155 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred80_JPA22163 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred81_JPA22171 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred82_JPA22179 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred83_JPA22187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_synpred84_JPA22195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_synpred87_JPA22255 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_synpred88_JPA22270 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_synpred89_JPA22307 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_synpred90_JPA22315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_synpred91_JPA22323 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_synpred92_JPA22331 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_synpred93_JPA22339 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred94_JPA22347 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_synpred95_JPA22355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred114_JPA22608 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_synpred114_JPA22611 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_synpred114_JPA22615 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred114_JPA22617 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred114_JPA22619 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred114_JPA22621 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred116_JPA22629 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_synpred116_JPA22632 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_synpred116_JPA22636 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred116_JPA22638 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred116_JPA22640 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred116_JPA22642 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred129_JPA22846 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred137_JPA22948 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred143_JPA23017 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred143_JPA23019 = new BitSet(new long[]{0x00A0000607C00240L,0x45010001000E3020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred143_JPA23022 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred143_JPA23026 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred146_JPA23035 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred146_JPA23037 = new BitSet(new long[]{0x00A0000004800000L,0x0101000100063020L,0x00000000000000C0L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred146_JPA23046 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred146_JPA23050 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred149_JPA23059 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred149_JPA23061 = new BitSet(new long[]{0x00A0000004800000L,0x0101000000063020L});
	public static final BitSet FOLLOW_enum_expression_in_synpred149_JPA23068 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred149_JPA23072 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred151_JPA23081 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred151_JPA23083 = new BitSet(new long[]{0x00A0000407800240L,0x0101000100763020L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred151_JPA23086 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred151_JPA23090 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred154_JPA23099 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred154_JPA23101 = new BitSet(new long[]{0x00A0000004000000L,0x0100000000003020L});
	public static final BitSet FOLLOW_entity_expression_in_synpred154_JPA23110 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred154_JPA23114 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred156_JPA23123 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred156_JPA23125 = new BitSet(new long[]{0x00A0000004000000L,0x8000000000000020L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred156_JPA23133 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred163_JPA23214 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred165_JPA23243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred171_JPA23312 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred171_JPA23313 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred171_JPA23314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred174_JPA23338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred176_JPA23354 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred181_JPA23405 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred183_JPA23421 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred184_JPA23440 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred187_JPA23464 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred189_JPA23480 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_synpred190_JPA23488 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred227_JPA23935 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_synpred229_JPA23951 = new BitSet(new long[]{0x0000000000000002L});
}
