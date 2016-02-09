// $ANTLR 3.5.2 JPA2.g 2016-02-09 18:09:42

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
			// elements: select_clause, orderby_clause, groupby_clause, where_clause, having_clause, from_clause
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


	public static class joined_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "joined_clause"
	// JPA2.g:101:1: joined_clause : ( join | fetch_join );
	public final JPA2Parser.joined_clause_return joined_clause() throws RecognitionException {
		JPA2Parser.joined_clause_return retval = new JPA2Parser.joined_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope join22 =null;
		ParserRuleReturnScope fetch_join23 =null;


		try {
			// JPA2.g:101:15: ( join | fetch_join )
			int alt11=2;
			switch ( input.LA(1) ) {
			case LEFT:
				{
				int LA11_1 = input.LA(2);
				if ( (LA11_1==OUTER) ) {
					int LA11_4 = input.LA(3);
					if ( (LA11_4==JOIN) ) {
						int LA11_3 = input.LA(4);
						if ( (LA11_3==WORD||LA11_3==125) ) {
							alt11=1;
						}
						else if ( (LA11_3==FETCH) ) {
							alt11=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 11, 3, input);
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
								new NoViableAltException("", 11, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA11_1==JOIN) ) {
					int LA11_3 = input.LA(3);
					if ( (LA11_3==WORD||LA11_3==125) ) {
						alt11=1;
					}
					else if ( (LA11_3==FETCH) ) {
						alt11=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 11, 3, input);
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
							new NoViableAltException("", 11, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INNER:
				{
				int LA11_2 = input.LA(2);
				if ( (LA11_2==JOIN) ) {
					int LA11_3 = input.LA(3);
					if ( (LA11_3==WORD||LA11_3==125) ) {
						alt11=1;
					}
					else if ( (LA11_3==FETCH) ) {
						alt11=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 11, 3, input);
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
							new NoViableAltException("", 11, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case JOIN:
				{
				int LA11_3 = input.LA(2);
				if ( (LA11_3==WORD||LA11_3==125) ) {
					alt11=1;
				}
				else if ( (LA11_3==FETCH) ) {
					alt11=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 11, 3, input);
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
					new NoViableAltException("", 11, 0, input);
				throw nvae;
			}
			switch (alt11) {
				case 1 :
					// JPA2.g:101:17: join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_join_in_joined_clause719);
					join22=join();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join22.getTree());

					}
					break;
				case 2 :
					// JPA2.g:101:24: fetch_join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_fetch_join_in_joined_clause723);
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
	// JPA2.g:102:1: range_variable_declaration : entity_name ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) ;
	public final JPA2Parser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
		JPA2Parser.range_variable_declaration_return retval = new JPA2Parser.range_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal25=null;
		ParserRuleReturnScope entity_name24 =null;
		ParserRuleReturnScope identification_variable26 =null;

		Object string_literal25_tree=null;
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleSubtreeStream stream_entity_name=new RewriteRuleSubtreeStream(adaptor,"rule entity_name");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:103:6: ( entity_name ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) )
			// JPA2.g:103:8: entity_name ( 'AS' )? identification_variable
			{
			pushFollow(FOLLOW_entity_name_in_range_variable_declaration735);
			entity_name24=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_entity_name.add(entity_name24.getTree());
			// JPA2.g:103:20: ( 'AS' )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==78) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// JPA2.g:103:21: 'AS'
					{
					string_literal25=(Token)match(input,78,FOLLOW_78_in_range_variable_declaration738); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_78.add(string_literal25);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_range_variable_declaration742);
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
			// 104:6: -> ^( T_ID_VAR[$identification_variable.text] entity_name )
			{
				// JPA2.g:104:9: ^( T_ID_VAR[$identification_variable.text] entity_name )
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
	// JPA2.g:105:1: join : join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression ) ;
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
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleTokenStream stream_115=new RewriteRuleTokenStream(adaptor,"token 115");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");
		RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");

		try {
			// JPA2.g:106:6: ( join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression ) )
			// JPA2.g:106:8: join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )?
			{
			pushFollow(FOLLOW_join_spec_in_join771);
			join_spec27=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_spec.add(join_spec27.getTree());
			pushFollow(FOLLOW_join_association_path_expression_in_join773);
			join_association_path_expression28=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression28.getTree());
			// JPA2.g:106:51: ( 'AS' )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==78) ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// JPA2.g:106:52: 'AS'
					{
					string_literal29=(Token)match(input,78,FOLLOW_78_in_join776); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_78.add(string_literal29);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_join780);
			identification_variable30=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable30.getTree());
			// JPA2.g:106:83: ( 'ON' conditional_expression )?
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==115) ) {
				alt14=1;
			}
			switch (alt14) {
				case 1 :
					// JPA2.g:106:84: 'ON' conditional_expression
					{
					string_literal31=(Token)match(input,115,FOLLOW_115_in_join783); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_115.add(string_literal31);

					pushFollow(FOLLOW_conditional_expression_in_join785);
					conditional_expression32=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression32.getTree());
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
			// 107:6: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression )
			{
				// JPA2.g:107:9: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new JoinVariableNode(T_JOIN_VAR, (join_spec27!=null?input.toString(join_spec27.start,join_spec27.stop):null), (identification_variable30!=null?input.toString(identification_variable30.start,identification_variable30.stop):null), (conditional_expression32!=null?input.toString(conditional_expression32.start,conditional_expression32.stop):null)), root_1);
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
	// JPA2.g:108:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
	public final JPA2Parser.fetch_join_return fetch_join() throws RecognitionException {
		JPA2Parser.fetch_join_return retval = new JPA2Parser.fetch_join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal34=null;
		ParserRuleReturnScope join_spec33 =null;
		ParserRuleReturnScope join_association_path_expression35 =null;

		Object string_literal34_tree=null;

		try {
			// JPA2.g:109:6: ( join_spec 'FETCH' join_association_path_expression )
			// JPA2.g:109:8: join_spec 'FETCH' join_association_path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_join_spec_in_fetch_join816);
			join_spec33=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec33.getTree());

			string_literal34=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join818); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal34_tree = (Object)adaptor.create(string_literal34);
			adaptor.addChild(root_0, string_literal34_tree);
			}

			pushFollow(FOLLOW_join_association_path_expression_in_fetch_join820);
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
	// JPA2.g:110:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
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
			// JPA2.g:111:6: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
			// JPA2.g:111:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:111:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
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
					// JPA2.g:111:9: ( 'LEFT' ) ( 'OUTER' )?
					{
					// JPA2.g:111:9: ( 'LEFT' )
					// JPA2.g:111:10: 'LEFT'
					{
					string_literal36=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec834); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal36_tree = (Object)adaptor.create(string_literal36);
					adaptor.addChild(root_0, string_literal36_tree);
					}

					}

					// JPA2.g:111:18: ( 'OUTER' )?
					int alt15=2;
					int LA15_0 = input.LA(1);
					if ( (LA15_0==OUTER) ) {
						alt15=1;
					}
					switch (alt15) {
						case 1 :
							// JPA2.g:111:19: 'OUTER'
							{
							string_literal37=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec838); if (state.failed) return retval;
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
					// JPA2.g:111:31: 'INNER'
					{
					string_literal38=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec844); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal38_tree = (Object)adaptor.create(string_literal38);
					adaptor.addChild(root_0, string_literal38_tree);
					}

					}
					break;

			}

			string_literal39=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec849); if (state.failed) return retval;
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
	// JPA2.g:114:1: join_association_path_expression : ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name );
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
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleTokenStream stream_125=new RewriteRuleTokenStream(adaptor,"token 125");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_subtype=new RewriteRuleSubtreeStream(adaptor,"rule subtype");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:115:6: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name )
			int alt21=3;
			int LA21_0 = input.LA(1);
			if ( (LA21_0==WORD) ) {
				int LA21_1 = input.LA(2);
				if ( (LA21_1==60) ) {
					alt21=1;
				}
				else if ( (LA21_1==EOF||(LA21_1 >= GROUP && LA21_1 <= HAVING)||LA21_1==INNER||(LA21_1 >= JOIN && LA21_1 <= LEFT)||LA21_1==ORDER||LA21_1==RPAREN||LA21_1==WORD||LA21_1==58||LA21_1==78||LA21_1==99||LA21_1==118||LA21_1==132) ) {
					alt21=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 21, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA21_0==125) ) {
				alt21=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 21, 0, input);
				throw nvae;
			}

			switch (alt21) {
				case 1 :
					// JPA2.g:115:8: identification_variable '.' ( field '.' )* ( field )?
					{
					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression863);
					identification_variable40=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable40.getTree());
					char_literal41=(Token)match(input,60,FOLLOW_60_in_join_association_path_expression865); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal41);

					// JPA2.g:115:36: ( field '.' )*
					loop17:
					while (true) {
						int alt17=2;
						int LA17_0 = input.LA(1);
						if ( (LA17_0==WORD) ) {
							int LA17_1 = input.LA(2);
							if ( (LA17_1==60) ) {
								alt17=1;
							}

						}
						else if ( (LA17_0==GROUP) ) {
							int LA17_3 = input.LA(2);
							if ( (LA17_3==60) ) {
								alt17=1;
							}

						}

						switch (alt17) {
						case 1 :
							// JPA2.g:115:37: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression868);
							field42=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field42.getTree());
							char_literal43=(Token)match(input,60,FOLLOW_60_in_join_association_path_expression869); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_60.add(char_literal43);

							}
							break;

						default :
							break loop17;
						}
					}

					// JPA2.g:115:48: ( field )?
					int alt18=2;
					int LA18_0 = input.LA(1);
					if ( (LA18_0==WORD) ) {
						int LA18_1 = input.LA(2);
						if ( (synpred20_JPA2()) ) {
							alt18=1;
						}
					}
					else if ( (LA18_0==GROUP) ) {
						int LA18_3 = input.LA(2);
						if ( (LA18_3==EOF||(LA18_3 >= GROUP && LA18_3 <= HAVING)||LA18_3==INNER||(LA18_3 >= JOIN && LA18_3 <= LEFT)||LA18_3==ORDER||LA18_3==RPAREN||LA18_3==WORD||LA18_3==58||LA18_3==78||LA18_3==99||LA18_3==118||LA18_3==132) ) {
							alt18=1;
						}
					}
					switch (alt18) {
						case 1 :
							// JPA2.g:115:48: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression873);
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
					// 116:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:116:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable40!=null?input.toString(identification_variable40.start,identification_variable40.stop):null)), root_1);
						// JPA2.g:116:73: ( field )*
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
					// JPA2.g:117:9: 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')'
					{
					string_literal45=(Token)match(input,125,FOLLOW_125_in_join_association_path_expression908); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_125.add(string_literal45);

					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression910);
					identification_variable46=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable46.getTree());
					char_literal47=(Token)match(input,60,FOLLOW_60_in_join_association_path_expression912); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal47);

					// JPA2.g:117:46: ( field '.' )*
					loop19:
					while (true) {
						int alt19=2;
						int LA19_0 = input.LA(1);
						if ( (LA19_0==GROUP||LA19_0==WORD) ) {
							int LA19_1 = input.LA(2);
							if ( (LA19_1==60) ) {
								alt19=1;
							}

						}

						switch (alt19) {
						case 1 :
							// JPA2.g:117:47: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression915);
							field48=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field48.getTree());
							char_literal49=(Token)match(input,60,FOLLOW_60_in_join_association_path_expression916); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_60.add(char_literal49);

							}
							break;

						default :
							break loop19;
						}
					}

					// JPA2.g:117:58: ( field )?
					int alt20=2;
					int LA20_0 = input.LA(1);
					if ( (LA20_0==GROUP||LA20_0==WORD) ) {
						alt20=1;
					}
					switch (alt20) {
						case 1 :
							// JPA2.g:117:58: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression920);
							field50=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field50.getTree());
							}
							break;

					}

					string_literal51=(Token)match(input,78,FOLLOW_78_in_join_association_path_expression923); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_78.add(string_literal51);

					pushFollow(FOLLOW_subtype_in_join_association_path_expression925);
					subtype52=subtype();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subtype.add(subtype52.getTree());
					char_literal53=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_join_association_path_expression927); if (state.failed) return retval; 
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
					// 118:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:118:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable46!=null?input.toString(identification_variable46.start,identification_variable46.stop):null)), root_1);
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
				case 3 :
					// JPA2.g:119:8: entity_name
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_name_in_join_association_path_expression960);
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
	// JPA2.g:122:1: collection_member_declaration : 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
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
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleTokenStream stream_IN=new RewriteRuleTokenStream(adaptor,"token IN");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");

		try {
			// JPA2.g:123:5: ( 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
			// JPA2.g:123:7: 'IN' '(' path_expression ')' ( 'AS' )? identification_variable
			{
			string_literal55=(Token)match(input,IN,FOLLOW_IN_in_collection_member_declaration973); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_IN.add(string_literal55);

			char_literal56=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration974); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal56);

			pushFollow(FOLLOW_path_expression_in_collection_member_declaration976);
			path_expression57=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_path_expression.add(path_expression57.getTree());
			char_literal58=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration978); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal58);

			// JPA2.g:123:35: ( 'AS' )?
			int alt22=2;
			int LA22_0 = input.LA(1);
			if ( (LA22_0==78) ) {
				alt22=1;
			}
			switch (alt22) {
				case 1 :
					// JPA2.g:123:36: 'AS'
					{
					string_literal59=(Token)match(input,78,FOLLOW_78_in_collection_member_declaration981); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_78.add(string_literal59);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_collection_member_declaration985);
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
			// 124:5: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
			{
				// JPA2.g:124:8: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
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
	// JPA2.g:126:1: qualified_identification_variable : ( map_field_identification_variable | 'ENTRY(' identification_variable ')' );
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
			// JPA2.g:127:5: ( map_field_identification_variable | 'ENTRY(' identification_variable ')' )
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0==100||LA23_0==130) ) {
				alt23=1;
			}
			else if ( (LA23_0==92) ) {
				alt23=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 23, 0, input);
				throw nvae;
			}

			switch (alt23) {
				case 1 :
					// JPA2.g:127:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_qualified_identification_variable1014);
					map_field_identification_variable61=map_field_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map_field_identification_variable61.getTree());

					}
					break;
				case 2 :
					// JPA2.g:128:7: 'ENTRY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal62=(Token)match(input,92,FOLLOW_92_in_qualified_identification_variable1022); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal62_tree = (Object)adaptor.create(string_literal62);
					adaptor.addChild(root_0, string_literal62_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_qualified_identification_variable1023);
					identification_variable63=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable63.getTree());

					char_literal64=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_qualified_identification_variable1024); if (state.failed) return retval;
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
	// JPA2.g:129:1: map_field_identification_variable : ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' );
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
			// JPA2.g:129:35: ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' )
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==100) ) {
				alt24=1;
			}
			else if ( (LA24_0==130) ) {
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
					// JPA2.g:129:37: 'KEY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal65=(Token)match(input,100,FOLLOW_100_in_map_field_identification_variable1031); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal65_tree = (Object)adaptor.create(string_literal65);
					adaptor.addChild(root_0, string_literal65_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1032);
					identification_variable66=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable66.getTree());

					char_literal67=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1033); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal67_tree = (Object)adaptor.create(char_literal67);
					adaptor.addChild(root_0, char_literal67_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:129:72: 'VALUE(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal68=(Token)match(input,130,FOLLOW_130_in_map_field_identification_variable1037); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal68_tree = (Object)adaptor.create(string_literal68);
					adaptor.addChild(root_0, string_literal68_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1038);
					identification_variable69=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable69.getTree());

					char_literal70=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1039); if (state.failed) return retval;
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
	// JPA2.g:132:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
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
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:133:5: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
			// JPA2.g:133:8: identification_variable '.' ( field '.' )* ( field )?
			{
			pushFollow(FOLLOW_identification_variable_in_path_expression1053);
			identification_variable71=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable71.getTree());
			char_literal72=(Token)match(input,60,FOLLOW_60_in_path_expression1055); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_60.add(char_literal72);

			// JPA2.g:133:36: ( field '.' )*
			loop25:
			while (true) {
				int alt25=2;
				int LA25_0 = input.LA(1);
				if ( (LA25_0==WORD) ) {
					int LA25_1 = input.LA(2);
					if ( (LA25_1==60) ) {
						alt25=1;
					}

				}
				else if ( (LA25_0==GROUP) ) {
					int LA25_3 = input.LA(2);
					if ( (LA25_3==60) ) {
						alt25=1;
					}

				}

				switch (alt25) {
				case 1 :
					// JPA2.g:133:37: field '.'
					{
					pushFollow(FOLLOW_field_in_path_expression1058);
					field73=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field73.getTree());
					char_literal74=(Token)match(input,60,FOLLOW_60_in_path_expression1059); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_60.add(char_literal74);

					}
					break;

				default :
					break loop25;
				}
			}

			// JPA2.g:133:48: ( field )?
			int alt26=2;
			int LA26_0 = input.LA(1);
			if ( (LA26_0==WORD) ) {
				int LA26_1 = input.LA(2);
				if ( (synpred29_JPA2()) ) {
					alt26=1;
				}
			}
			else if ( (LA26_0==GROUP) ) {
				int LA26_3 = input.LA(2);
				if ( (LA26_3==EOF||(LA26_3 >= AND && LA26_3 <= ASC)||LA26_3==DESC||(LA26_3 >= GROUP && LA26_3 <= INNER)||(LA26_3 >= JOIN && LA26_3 <= LEFT)||(LA26_3 >= NOT && LA26_3 <= ORDER)||LA26_3==RPAREN||LA26_3==WORD||(LA26_3 >= 56 && LA26_3 <= 59)||LA26_3==61||(LA26_3 >= 63 && LA26_3 <= 68)||(LA26_3 >= 78 && LA26_3 <= 79)||LA26_3==89||LA26_3==91||LA26_3==95||LA26_3==99||LA26_3==103||LA26_3==105||LA26_3==118||LA26_3==123||(LA26_3 >= 131 && LA26_3 <= 132)) ) {
					alt26=1;
				}
			}
			switch (alt26) {
				case 1 :
					// JPA2.g:133:48: field
					{
					pushFollow(FOLLOW_field_in_path_expression1063);
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
			// 134:5: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
			{
				// JPA2.g:134:8: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable71!=null?input.toString(identification_variable71.start,identification_variable71.stop):null)), root_1);
				// JPA2.g:134:68: ( field )*
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
	// JPA2.g:139:1: general_identification_variable : ( identification_variable | map_field_identification_variable );
	public final JPA2Parser.general_identification_variable_return general_identification_variable() throws RecognitionException {
		JPA2Parser.general_identification_variable_return retval = new JPA2Parser.general_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable76 =null;
		ParserRuleReturnScope map_field_identification_variable77 =null;


		try {
			// JPA2.g:140:5: ( identification_variable | map_field_identification_variable )
			int alt27=2;
			int LA27_0 = input.LA(1);
			if ( (LA27_0==WORD) ) {
				alt27=1;
			}
			else if ( (LA27_0==100||LA27_0==130) ) {
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
					// JPA2.g:140:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_general_identification_variable1102);
					identification_variable76=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable76.getTree());

					}
					break;
				case 2 :
					// JPA2.g:141:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_general_identification_variable1110);
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
	// JPA2.g:144:1: update_clause : identification_variable_declaration 'SET' update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration ) ;
	public final JPA2Parser.update_clause_return update_clause() throws RecognitionException {
		JPA2Parser.update_clause_return retval = new JPA2Parser.update_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal79=null;
		Token char_literal81=null;
		ParserRuleReturnScope identification_variable_declaration78 =null;
		ParserRuleReturnScope update_item80 =null;
		ParserRuleReturnScope update_item82 =null;

		Object string_literal79_tree=null;
		Object char_literal81_tree=null;
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_118=new RewriteRuleTokenStream(adaptor,"token 118");
		RewriteRuleSubtreeStream stream_update_item=new RewriteRuleSubtreeStream(adaptor,"rule update_item");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:145:5: ( identification_variable_declaration 'SET' update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration ) )
			// JPA2.g:145:7: identification_variable_declaration 'SET' update_item ( ',' update_item )*
			{
			pushFollow(FOLLOW_identification_variable_declaration_in_update_clause1123);
			identification_variable_declaration78=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration78.getTree());
			string_literal79=(Token)match(input,118,FOLLOW_118_in_update_clause1125); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_118.add(string_literal79);

			pushFollow(FOLLOW_update_item_in_update_clause1127);
			update_item80=update_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_item.add(update_item80.getTree());
			// JPA2.g:145:61: ( ',' update_item )*
			loop28:
			while (true) {
				int alt28=2;
				int LA28_0 = input.LA(1);
				if ( (LA28_0==58) ) {
					alt28=1;
				}

				switch (alt28) {
				case 1 :
					// JPA2.g:145:62: ',' update_item
					{
					char_literal81=(Token)match(input,58,FOLLOW_58_in_update_clause1130); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal81);

					pushFollow(FOLLOW_update_item_in_update_clause1132);
					update_item82=update_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_update_item.add(update_item82.getTree());
					}
					break;

				default :
					break loop28;
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
			// 146:5: -> ^( T_SOURCES identification_variable_declaration )
			{
				// JPA2.g:146:8: ^( T_SOURCES identification_variable_declaration )
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
	// JPA2.g:147:1: update_item : path_expression '=' new_value ;
	public final JPA2Parser.update_item_return update_item() throws RecognitionException {
		JPA2Parser.update_item_return retval = new JPA2Parser.update_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal84=null;
		ParserRuleReturnScope path_expression83 =null;
		ParserRuleReturnScope new_value85 =null;

		Object char_literal84_tree=null;

		try {
			// JPA2.g:148:5: ( path_expression '=' new_value )
			// JPA2.g:148:7: path_expression '=' new_value
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_update_item1160);
			path_expression83=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression83.getTree());

			char_literal84=(Token)match(input,66,FOLLOW_66_in_update_item1162); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal84_tree = (Object)adaptor.create(char_literal84);
			adaptor.addChild(root_0, char_literal84_tree);
			}

			pushFollow(FOLLOW_new_value_in_update_item1164);
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
	// JPA2.g:149:1: new_value : ( scalar_expression | simple_entity_expression | 'NULL' );
	public final JPA2Parser.new_value_return new_value() throws RecognitionException {
		JPA2Parser.new_value_return retval = new JPA2Parser.new_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal88=null;
		ParserRuleReturnScope scalar_expression86 =null;
		ParserRuleReturnScope simple_entity_expression87 =null;

		Object string_literal88_tree=null;

		try {
			// JPA2.g:150:5: ( scalar_expression | simple_entity_expression | 'NULL' )
			int alt29=3;
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
				alt29=1;
				}
				break;
			case WORD:
				{
				int LA29_2 = input.LA(2);
				if ( (synpred32_JPA2()) ) {
					alt29=1;
				}
				else if ( (synpred33_JPA2()) ) {
					alt29=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 29, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 69:
				{
				int LA29_3 = input.LA(2);
				if ( (LA29_3==62) ) {
					int LA29_8 = input.LA(3);
					if ( (LA29_8==INT_NUMERAL) ) {
						int LA29_9 = input.LA(4);
						if ( (synpred32_JPA2()) ) {
							alt29=1;
						}
						else if ( (synpred33_JPA2()) ) {
							alt29=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 29, 9, input);
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
								new NoViableAltException("", 29, 8, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA29_3==INT_NUMERAL) ) {
					int LA29_9 = input.LA(3);
					if ( (synpred32_JPA2()) ) {
						alt29=1;
					}
					else if ( (synpred33_JPA2()) ) {
						alt29=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 29, 9, input);
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
							new NoViableAltException("", 29, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA29_4 = input.LA(2);
				if ( (synpred32_JPA2()) ) {
					alt29=1;
				}
				else if ( (synpred33_JPA2()) ) {
					alt29=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 29, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 55:
				{
				int LA29_5 = input.LA(2);
				if ( (LA29_5==WORD) ) {
					int LA29_10 = input.LA(3);
					if ( (LA29_10==136) ) {
						int LA29_11 = input.LA(4);
						if ( (synpred32_JPA2()) ) {
							alt29=1;
						}
						else if ( (synpred33_JPA2()) ) {
							alt29=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 29, 11, input);
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
								new NoViableAltException("", 29, 10, input);
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
							new NoViableAltException("", 29, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 111:
				{
				alt29=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 29, 0, input);
				throw nvae;
			}
			switch (alt29) {
				case 1 :
					// JPA2.g:150:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_new_value1175);
					scalar_expression86=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression86.getTree());

					}
					break;
				case 2 :
					// JPA2.g:151:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_new_value1183);
					simple_entity_expression87=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression87.getTree());

					}
					break;
				case 3 :
					// JPA2.g:152:7: 'NULL'
					{
					root_0 = (Object)adaptor.nil();


					string_literal88=(Token)match(input,111,FOLLOW_111_in_new_value1191); if (state.failed) return retval;
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
	// JPA2.g:154:1: delete_clause : identification_variable_declaration -> ^( T_SOURCES identification_variable_declaration ) ;
	public final JPA2Parser.delete_clause_return delete_clause() throws RecognitionException {
		JPA2Parser.delete_clause_return retval = new JPA2Parser.delete_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable_declaration89 =null;

		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:155:5: ( identification_variable_declaration -> ^( T_SOURCES identification_variable_declaration ) )
			// JPA2.g:155:7: identification_variable_declaration
			{
			pushFollow(FOLLOW_identification_variable_declaration_in_delete_clause1203);
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
			// 156:5: -> ^( T_SOURCES identification_variable_declaration )
			{
				// JPA2.g:156:8: ^( T_SOURCES identification_variable_declaration )
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
	// JPA2.g:157:1: select_clause : ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) ;
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
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleSubtreeStream stream_select_item=new RewriteRuleSubtreeStream(adaptor,"rule select_item");

		try {
			// JPA2.g:158:5: ( ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) )
			// JPA2.g:158:7: ( 'DISTINCT' )? select_item ( ',' select_item )*
			{
			// JPA2.g:158:7: ( 'DISTINCT' )?
			int alt30=2;
			int LA30_0 = input.LA(1);
			if ( (LA30_0==DISTINCT) ) {
				alt30=1;
			}
			switch (alt30) {
				case 1 :
					// JPA2.g:158:8: 'DISTINCT'
					{
					string_literal90=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause1230); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal90);

					}
					break;

			}

			pushFollow(FOLLOW_select_item_in_select_clause1234);
			select_item91=select_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_item.add(select_item91.getTree());
			// JPA2.g:158:33: ( ',' select_item )*
			loop31:
			while (true) {
				int alt31=2;
				int LA31_0 = input.LA(1);
				if ( (LA31_0==58) ) {
					alt31=1;
				}

				switch (alt31) {
				case 1 :
					// JPA2.g:158:34: ',' select_item
					{
					char_literal92=(Token)match(input,58,FOLLOW_58_in_select_clause1237); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal92);

					pushFollow(FOLLOW_select_item_in_select_clause1239);
					select_item93=select_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_select_item.add(select_item93.getTree());
					}
					break;

				default :
					break loop31;
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
			// 159:5: -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
			{
				// JPA2.g:159:8: ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:159:48: ( 'DISTINCT' )?
				if ( stream_DISTINCT.hasNext() ) {
					adaptor.addChild(root_1, stream_DISTINCT.nextNode());
				}
				stream_DISTINCT.reset();

				// JPA2.g:159:62: ( ^( T_SELECTED_ITEM[] select_item ) )*
				while ( stream_select_item.hasNext() ) {
					// JPA2.g:159:62: ^( T_SELECTED_ITEM[] select_item )
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
	// JPA2.g:160:1: select_item : select_expression ( ( 'AS' )? result_variable )? ;
	public final JPA2Parser.select_item_return select_item() throws RecognitionException {
		JPA2Parser.select_item_return retval = new JPA2Parser.select_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal95=null;
		ParserRuleReturnScope select_expression94 =null;
		ParserRuleReturnScope result_variable96 =null;

		Object string_literal95_tree=null;

		try {
			// JPA2.g:161:5: ( select_expression ( ( 'AS' )? result_variable )? )
			// JPA2.g:161:7: select_expression ( ( 'AS' )? result_variable )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_select_expression_in_select_item1282);
			select_expression94=select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, select_expression94.getTree());

			// JPA2.g:161:25: ( ( 'AS' )? result_variable )?
			int alt33=2;
			int LA33_0 = input.LA(1);
			if ( (LA33_0==WORD||LA33_0==78) ) {
				alt33=1;
			}
			switch (alt33) {
				case 1 :
					// JPA2.g:161:26: ( 'AS' )? result_variable
					{
					// JPA2.g:161:26: ( 'AS' )?
					int alt32=2;
					int LA32_0 = input.LA(1);
					if ( (LA32_0==78) ) {
						alt32=1;
					}
					switch (alt32) {
						case 1 :
							// JPA2.g:161:27: 'AS'
							{
							string_literal95=(Token)match(input,78,FOLLOW_78_in_select_item1286); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal95_tree = (Object)adaptor.create(string_literal95);
							adaptor.addChild(root_0, string_literal95_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_result_variable_in_select_item1290);
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
	// JPA2.g:162:1: select_expression : ( path_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression );
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
			// JPA2.g:163:5: ( path_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression )
			int alt34=6;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA34_1 = input.LA(2);
				if ( (synpred38_JPA2()) ) {
					alt34=1;
				}
				else if ( (synpred39_JPA2()) ) {
					alt34=2;
				}
				else if ( (synpred40_JPA2()) ) {
					alt34=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 34, 1, input);
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
				alt34=3;
				}
				break;
			case COUNT:
				{
				int LA34_16 = input.LA(2);
				if ( (synpred40_JPA2()) ) {
					alt34=3;
				}
				else if ( (synpred41_JPA2()) ) {
					alt34=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 34, 16, input);
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
				int LA34_17 = input.LA(2);
				if ( (synpred40_JPA2()) ) {
					alt34=3;
				}
				else if ( (synpred41_JPA2()) ) {
					alt34=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 34, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 96:
				{
				int LA34_18 = input.LA(2);
				if ( (synpred40_JPA2()) ) {
					alt34=3;
				}
				else if ( (synpred41_JPA2()) ) {
					alt34=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 34, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 113:
				{
				alt34=5;
				}
				break;
			case 109:
				{
				alt34=6;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 34, 0, input);
				throw nvae;
			}
			switch (alt34) {
				case 1 :
					// JPA2.g:163:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_select_expression1303);
					path_expression97=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression97.getTree());

					}
					break;
				case 2 :
					// JPA2.g:164:7: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_select_expression1311);
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
					// 164:31: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
					{
						// JPA2.g:164:34: ^( T_SELECTED_ENTITY[$identification_variable.text] )
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
					// JPA2.g:165:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_select_expression1329);
					scalar_expression99=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression99.getTree());

					}
					break;
				case 4 :
					// JPA2.g:166:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_select_expression1337);
					aggregate_expression100=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression100.getTree());

					}
					break;
				case 5 :
					// JPA2.g:167:7: 'OBJECT' '(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal101=(Token)match(input,113,FOLLOW_113_in_select_expression1345); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal101_tree = (Object)adaptor.create(string_literal101);
					adaptor.addChild(root_0, string_literal101_tree);
					}

					char_literal102=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression1347); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal102_tree = (Object)adaptor.create(char_literal102);
					adaptor.addChild(root_0, char_literal102_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_select_expression1348);
					identification_variable103=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable103.getTree());

					char_literal104=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression1349); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal104_tree = (Object)adaptor.create(char_literal104);
					adaptor.addChild(root_0, char_literal104_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:168:7: constructor_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_constructor_expression_in_select_expression1357);
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
	// JPA2.g:169:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
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
			// JPA2.g:170:5: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
			// JPA2.g:170:7: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal106=(Token)match(input,109,FOLLOW_109_in_constructor_expression1368); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal106_tree = (Object)adaptor.create(string_literal106);
			adaptor.addChild(root_0, string_literal106_tree);
			}

			pushFollow(FOLLOW_constructor_name_in_constructor_expression1370);
			constructor_name107=constructor_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name107.getTree());

			char_literal108=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression1372); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal108_tree = (Object)adaptor.create(char_literal108);
			adaptor.addChild(root_0, char_literal108_tree);
			}

			pushFollow(FOLLOW_constructor_item_in_constructor_expression1374);
			constructor_item109=constructor_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item109.getTree());

			// JPA2.g:170:51: ( ',' constructor_item )*
			loop35:
			while (true) {
				int alt35=2;
				int LA35_0 = input.LA(1);
				if ( (LA35_0==58) ) {
					alt35=1;
				}

				switch (alt35) {
				case 1 :
					// JPA2.g:170:52: ',' constructor_item
					{
					char_literal110=(Token)match(input,58,FOLLOW_58_in_constructor_expression1377); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal110_tree = (Object)adaptor.create(char_literal110);
					adaptor.addChild(root_0, char_literal110_tree);
					}

					pushFollow(FOLLOW_constructor_item_in_constructor_expression1379);
					constructor_item111=constructor_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item111.getTree());

					}
					break;

				default :
					break loop35;
				}
			}

			char_literal112=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression1383); if (state.failed) return retval;
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
	// JPA2.g:171:1: constructor_item : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.constructor_item_return constructor_item() throws RecognitionException {
		JPA2Parser.constructor_item_return retval = new JPA2Parser.constructor_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression113 =null;
		ParserRuleReturnScope scalar_expression114 =null;
		ParserRuleReturnScope aggregate_expression115 =null;
		ParserRuleReturnScope identification_variable116 =null;


		try {
			// JPA2.g:172:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt36=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA36_1 = input.LA(2);
				if ( (synpred44_JPA2()) ) {
					alt36=1;
				}
				else if ( (synpred45_JPA2()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=4;
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
				alt36=2;
				}
				break;
			case COUNT:
				{
				int LA36_16 = input.LA(2);
				if ( (synpred45_JPA2()) ) {
					alt36=2;
				}
				else if ( (synpred46_JPA2()) ) {
					alt36=3;
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
					alt36=2;
				}
				else if ( (synpred46_JPA2()) ) {
					alt36=3;
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
			case 96:
				{
				int LA36_18 = input.LA(2);
				if ( (synpred45_JPA2()) ) {
					alt36=2;
				}
				else if ( (synpred46_JPA2()) ) {
					alt36=3;
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
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 36, 0, input);
				throw nvae;
			}
			switch (alt36) {
				case 1 :
					// JPA2.g:172:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_constructor_item1394);
					path_expression113=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression113.getTree());

					}
					break;
				case 2 :
					// JPA2.g:173:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_constructor_item1402);
					scalar_expression114=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression114.getTree());

					}
					break;
				case 3 :
					// JPA2.g:174:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_constructor_item1410);
					aggregate_expression115=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression115.getTree());

					}
					break;
				case 4 :
					// JPA2.g:175:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_constructor_item1418);
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
	// JPA2.g:176:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );
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
			// JPA2.g:177:5: ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation )
			int alt39=3;
			alt39 = dfa39.predict(input);
			switch (alt39) {
				case 1 :
					// JPA2.g:177:7: aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')'
					{
					pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1429);
					aggregate_expression_function_name117=aggregate_expression_function_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name117.getTree());
					char_literal118=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1431); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal118);

					// JPA2.g:177:45: ( DISTINCT )?
					int alt37=2;
					int LA37_0 = input.LA(1);
					if ( (LA37_0==DISTINCT) ) {
						alt37=1;
					}
					switch (alt37) {
						case 1 :
							// JPA2.g:177:46: DISTINCT
							{
							DISTINCT119=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1433); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT119);

							}
							break;

					}

					pushFollow(FOLLOW_path_expression_in_aggregate_expression1437);
					path_expression120=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_path_expression.add(path_expression120.getTree());
					char_literal121=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1438); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal121);

					// AST REWRITE
					// elements: path_expression, LPAREN, RPAREN, aggregate_expression_function_name, DISTINCT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 178:5: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
					{
						// JPA2.g:178:8: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:178:93: ( 'DISTINCT' )?
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
					// JPA2.g:179:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
					{
					string_literal122=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1472); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COUNT.add(string_literal122);

					char_literal123=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1474); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal123);

					// JPA2.g:179:18: ( DISTINCT )?
					int alt38=2;
					int LA38_0 = input.LA(1);
					if ( (LA38_0==DISTINCT) ) {
						alt38=1;
					}
					switch (alt38) {
						case 1 :
							// JPA2.g:179:19: DISTINCT
							{
							DISTINCT124=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1476); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT124);

							}
							break;

					}

					pushFollow(FOLLOW_count_argument_in_aggregate_expression1480);
					count_argument125=count_argument();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_count_argument.add(count_argument125.getTree());
					char_literal126=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1482); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal126);

					// AST REWRITE
					// elements: count_argument, COUNT, RPAREN, DISTINCT, LPAREN
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 180:5: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
					{
						// JPA2.g:180:8: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_COUNT.nextNode());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:180:66: ( 'DISTINCT' )?
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
					// JPA2.g:181:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_aggregate_expression1517);
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
	// JPA2.g:182:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
	public final JPA2Parser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
		JPA2Parser.aggregate_expression_function_name_return retval = new JPA2Parser.aggregate_expression_function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set128=null;

		Object set128_tree=null;

		try {
			// JPA2.g:183:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
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
	// JPA2.g:184:1: count_argument : ( identification_variable | path_expression );
	public final JPA2Parser.count_argument_return count_argument() throws RecognitionException {
		JPA2Parser.count_argument_return retval = new JPA2Parser.count_argument_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable129 =null;
		ParserRuleReturnScope path_expression130 =null;


		try {
			// JPA2.g:185:5: ( identification_variable | path_expression )
			int alt40=2;
			int LA40_0 = input.LA(1);
			if ( (LA40_0==WORD) ) {
				int LA40_1 = input.LA(2);
				if ( (LA40_1==RPAREN) ) {
					alt40=1;
				}
				else if ( (LA40_1==60) ) {
					alt40=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 40, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 40, 0, input);
				throw nvae;
			}

			switch (alt40) {
				case 1 :
					// JPA2.g:185:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_count_argument1554);
					identification_variable129=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable129.getTree());

					}
					break;
				case 2 :
					// JPA2.g:185:33: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_count_argument1558);
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
	// JPA2.g:186:1: where_clause : wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) ;
	public final JPA2Parser.where_clause_return where_clause() throws RecognitionException {
		JPA2Parser.where_clause_return retval = new JPA2Parser.where_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token wh=null;
		ParserRuleReturnScope conditional_expression131 =null;

		Object wh_tree=null;
		RewriteRuleTokenStream stream_132=new RewriteRuleTokenStream(adaptor,"token 132");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");

		try {
			// JPA2.g:187:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) )
			// JPA2.g:187:7: wh= 'WHERE' conditional_expression
			{
			wh=(Token)match(input,132,FOLLOW_132_in_where_clause1571); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_132.add(wh);

			pushFollow(FOLLOW_conditional_expression_in_where_clause1573);
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
			// 187:40: -> ^( T_CONDITION[$wh] conditional_expression )
			{
				// JPA2.g:187:43: ^( T_CONDITION[$wh] conditional_expression )
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
	// JPA2.g:188:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
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
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");

		try {
			// JPA2.g:189:5: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
			// JPA2.g:189:7: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
			{
			string_literal132=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1595); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_GROUP.add(string_literal132);

			string_literal133=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1597); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal133);

			pushFollow(FOLLOW_groupby_item_in_groupby_clause1599);
			groupby_item134=groupby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item134.getTree());
			// JPA2.g:189:33: ( ',' groupby_item )*
			loop41:
			while (true) {
				int alt41=2;
				int LA41_0 = input.LA(1);
				if ( (LA41_0==58) ) {
					alt41=1;
				}

				switch (alt41) {
				case 1 :
					// JPA2.g:189:34: ',' groupby_item
					{
					char_literal135=(Token)match(input,58,FOLLOW_58_in_groupby_clause1602); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal135);

					pushFollow(FOLLOW_groupby_item_in_groupby_clause1604);
					groupby_item136=groupby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item136.getTree());
					}
					break;

				default :
					break loop41;
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
			// 190:5: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
			{
				// JPA2.g:190:8: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new GroupByNode(T_GROUP_BY), root_1);
				adaptor.addChild(root_1, stream_GROUP.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:190:49: ( groupby_item )*
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
	// JPA2.g:191:1: groupby_item : ( path_expression | identification_variable );
	public final JPA2Parser.groupby_item_return groupby_item() throws RecognitionException {
		JPA2Parser.groupby_item_return retval = new JPA2Parser.groupby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression137 =null;
		ParserRuleReturnScope identification_variable138 =null;


		try {
			// JPA2.g:192:5: ( path_expression | identification_variable )
			int alt42=2;
			int LA42_0 = input.LA(1);
			if ( (LA42_0==WORD) ) {
				int LA42_1 = input.LA(2);
				if ( (LA42_1==60) ) {
					alt42=1;
				}
				else if ( (LA42_1==EOF||LA42_1==HAVING||LA42_1==ORDER||LA42_1==RPAREN||LA42_1==58) ) {
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
					// JPA2.g:192:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_groupby_item1638);
					path_expression137=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression137.getTree());

					}
					break;
				case 2 :
					// JPA2.g:192:25: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_groupby_item1642);
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
	// JPA2.g:193:1: having_clause : 'HAVING' conditional_expression ;
	public final JPA2Parser.having_clause_return having_clause() throws RecognitionException {
		JPA2Parser.having_clause_return retval = new JPA2Parser.having_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal139=null;
		ParserRuleReturnScope conditional_expression140 =null;

		Object string_literal139_tree=null;

		try {
			// JPA2.g:194:5: ( 'HAVING' conditional_expression )
			// JPA2.g:194:7: 'HAVING' conditional_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal139=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1653); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal139_tree = (Object)adaptor.create(string_literal139);
			adaptor.addChild(root_0, string_literal139_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_having_clause1655);
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
	// JPA2.g:195:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
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
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");

		try {
			// JPA2.g:196:5: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
			// JPA2.g:196:7: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
			{
			string_literal141=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1666); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ORDER.add(string_literal141);

			string_literal142=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1668); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal142);

			pushFollow(FOLLOW_orderby_item_in_orderby_clause1670);
			orderby_item143=orderby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item143.getTree());
			// JPA2.g:196:33: ( ',' orderby_item )*
			loop43:
			while (true) {
				int alt43=2;
				int LA43_0 = input.LA(1);
				if ( (LA43_0==58) ) {
					alt43=1;
				}

				switch (alt43) {
				case 1 :
					// JPA2.g:196:34: ',' orderby_item
					{
					char_literal144=(Token)match(input,58,FOLLOW_58_in_orderby_clause1673); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal144);

					pushFollow(FOLLOW_orderby_item_in_orderby_clause1675);
					orderby_item145=orderby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item145.getTree());
					}
					break;

				default :
					break loop43;
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
			// 197:5: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
			{
				// JPA2.g:197:8: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByNode(T_ORDER_BY), root_1);
				adaptor.addChild(root_1, stream_ORDER.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:197:49: ( orderby_item )*
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
	// JPA2.g:198:1: orderby_item : ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) );
	public final JPA2Parser.orderby_item_return orderby_item() throws RecognitionException {
		JPA2Parser.orderby_item_return retval = new JPA2Parser.orderby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal147=null;
		Token string_literal149=null;
		ParserRuleReturnScope orderby_variable146 =null;
		ParserRuleReturnScope orderby_variable148 =null;

		Object string_literal147_tree=null;
		Object string_literal149_tree=null;
		RewriteRuleTokenStream stream_ASC=new RewriteRuleTokenStream(adaptor,"token ASC");
		RewriteRuleTokenStream stream_DESC=new RewriteRuleTokenStream(adaptor,"token DESC");
		RewriteRuleSubtreeStream stream_orderby_variable=new RewriteRuleSubtreeStream(adaptor,"rule orderby_variable");

		try {
			// JPA2.g:199:5: ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) )
			int alt45=2;
			alt45 = dfa45.predict(input);
			switch (alt45) {
				case 1 :
					// JPA2.g:199:7: orderby_variable ( 'ASC' )?
					{
					pushFollow(FOLLOW_orderby_variable_in_orderby_item1709);
					orderby_variable146=orderby_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable146.getTree());
					// JPA2.g:199:24: ( 'ASC' )?
					int alt44=2;
					int LA44_0 = input.LA(1);
					if ( (LA44_0==ASC) ) {
						alt44=1;
					}
					switch (alt44) {
						case 1 :
							// JPA2.g:199:25: 'ASC'
							{
							string_literal147=(Token)match(input,ASC,FOLLOW_ASC_in_orderby_item1712); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ASC.add(string_literal147);

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
					// 200:6: -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? )
					{
						// JPA2.g:200:9: ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);
						adaptor.addChild(root_1, stream_orderby_variable.nextTree());
						// JPA2.g:200:65: ( 'ASC' )?
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
					// JPA2.g:201:7: orderby_variable 'DESC'
					{
					pushFollow(FOLLOW_orderby_variable_in_orderby_item1744);
					orderby_variable148=orderby_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable148.getTree());
					string_literal149=(Token)match(input,DESC,FOLLOW_DESC_in_orderby_item1747); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DESC.add(string_literal149);

					// AST REWRITE
					// elements: DESC, orderby_variable
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 202:5: -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' )
					{
						// JPA2.g:202:8: ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' )
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
	// JPA2.g:203:1: orderby_variable : ( path_expression | general_identification_variable | result_variable );
	public final JPA2Parser.orderby_variable_return orderby_variable() throws RecognitionException {
		JPA2Parser.orderby_variable_return retval = new JPA2Parser.orderby_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression150 =null;
		ParserRuleReturnScope general_identification_variable151 =null;
		ParserRuleReturnScope result_variable152 =null;


		try {
			// JPA2.g:204:5: ( path_expression | general_identification_variable | result_variable )
			int alt46=3;
			int LA46_0 = input.LA(1);
			if ( (LA46_0==WORD) ) {
				int LA46_1 = input.LA(2);
				if ( (LA46_1==60) ) {
					alt46=1;
				}
				else if ( (synpred62_JPA2()) ) {
					alt46=2;
				}
				else if ( (true) ) {
					alt46=3;
				}

			}
			else if ( (LA46_0==100||LA46_0==130) ) {
				alt46=2;
			}

			else {
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


					pushFollow(FOLLOW_path_expression_in_orderby_variable1776);
					path_expression150=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression150.getTree());

					}
					break;
				case 2 :
					// JPA2.g:204:25: general_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_identification_variable_in_orderby_variable1780);
					general_identification_variable151=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable151.getTree());

					}
					break;
				case 3 :
					// JPA2.g:204:59: result_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_result_variable_in_orderby_variable1784);
					result_variable152=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable152.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:206:1: subquery : lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
	public final JPA2Parser.subquery_return subquery() throws RecognitionException {
		JPA2Parser.subquery_return retval = new JPA2Parser.subquery_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token lp=null;
		Token rp=null;
		Token string_literal153=null;
		ParserRuleReturnScope simple_select_clause154 =null;
		ParserRuleReturnScope subquery_from_clause155 =null;
		ParserRuleReturnScope where_clause156 =null;
		ParserRuleReturnScope groupby_clause157 =null;
		ParserRuleReturnScope having_clause158 =null;

		Object lp_tree=null;
		Object rp_tree=null;
		Object string_literal153_tree=null;
		RewriteRuleTokenStream stream_117=new RewriteRuleTokenStream(adaptor,"token 117");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
		RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");

		try {
			// JPA2.g:207:5: (lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
			// JPA2.g:207:7: lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
			{
			lp=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subquery1798); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(lp);

			string_literal153=(Token)match(input,117,FOLLOW_117_in_subquery1800); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_117.add(string_literal153);

			pushFollow(FOLLOW_simple_select_clause_in_subquery1802);
			simple_select_clause154=simple_select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause154.getTree());
			pushFollow(FOLLOW_subquery_from_clause_in_subquery1804);
			subquery_from_clause155=subquery_from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause155.getTree());
			// JPA2.g:207:65: ( where_clause )?
			int alt47=2;
			int LA47_0 = input.LA(1);
			if ( (LA47_0==132) ) {
				alt47=1;
			}
			switch (alt47) {
				case 1 :
					// JPA2.g:207:66: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_subquery1807);
					where_clause156=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause156.getTree());
					}
					break;

			}

			// JPA2.g:207:81: ( groupby_clause )?
			int alt48=2;
			int LA48_0 = input.LA(1);
			if ( (LA48_0==GROUP) ) {
				alt48=1;
			}
			switch (alt48) {
				case 1 :
					// JPA2.g:207:82: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_subquery1812);
					groupby_clause157=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause157.getTree());
					}
					break;

			}

			// JPA2.g:207:99: ( having_clause )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==HAVING) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// JPA2.g:207:100: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_subquery1817);
					having_clause158=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause158.getTree());
					}
					break;

			}

			rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1823); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(rp);

			// AST REWRITE
			// elements: subquery_from_clause, having_clause, groupby_clause, 117, simple_select_clause, where_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 208:6: -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
			{
				// JPA2.g:208:9: ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);
				adaptor.addChild(root_1, stream_117.nextNode());
				adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
				adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
				// JPA2.g:208:90: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:208:106: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:208:124: ( having_clause )?
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
	// JPA2.g:209:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
	public final JPA2Parser.subquery_from_clause_return subquery_from_clause() throws RecognitionException {
		JPA2Parser.subquery_from_clause_return retval = new JPA2Parser.subquery_from_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token fr=null;
		Token char_literal160=null;
		ParserRuleReturnScope subselect_identification_variable_declaration159 =null;
		ParserRuleReturnScope subselect_identification_variable_declaration161 =null;

		Object fr_tree=null;
		Object char_literal160_tree=null;
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_95=new RewriteRuleTokenStream(adaptor,"token 95");
		RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");

		try {
			// JPA2.g:210:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
			// JPA2.g:210:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
			{
			fr=(Token)match(input,95,FOLLOW_95_in_subquery_from_clause1873); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_95.add(fr);

			pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1875);
			subselect_identification_variable_declaration159=subselect_identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration159.getTree());
			// JPA2.g:210:63: ( ',' subselect_identification_variable_declaration )*
			loop50:
			while (true) {
				int alt50=2;
				int LA50_0 = input.LA(1);
				if ( (LA50_0==58) ) {
					alt50=1;
				}

				switch (alt50) {
				case 1 :
					// JPA2.g:210:64: ',' subselect_identification_variable_declaration
					{
					char_literal160=(Token)match(input,58,FOLLOW_58_in_subquery_from_clause1878); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal160);

					pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1880);
					subselect_identification_variable_declaration161=subselect_identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration161.getTree());
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
			// 211:5: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
			{
				// JPA2.g:211:8: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				// JPA2.g:211:35: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
				while ( stream_subselect_identification_variable_declaration.hasNext() ) {
					// JPA2.g:211:35: ^( T_SOURCE subselect_identification_variable_declaration )
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
	// JPA2.g:213:1: subselect_identification_variable_declaration : ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration );
	public final JPA2Parser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration() throws RecognitionException {
		JPA2Parser.subselect_identification_variable_declaration_return retval = new JPA2Parser.subselect_identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal164=null;
		ParserRuleReturnScope identification_variable_declaration162 =null;
		ParserRuleReturnScope derived_path_expression163 =null;
		ParserRuleReturnScope identification_variable165 =null;
		ParserRuleReturnScope join166 =null;
		ParserRuleReturnScope derived_collection_member_declaration167 =null;

		Object string_literal164_tree=null;

		try {
			// JPA2.g:214:5: ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration )
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
			case 125:
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
					// JPA2.g:214:7: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1918);
					identification_variable_declaration162=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration162.getTree());

					}
					break;
				case 2 :
					// JPA2.g:215:7: derived_path_expression 'AS' identification_variable ( join )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1926);
					derived_path_expression163=derived_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_path_expression163.getTree());

					string_literal164=(Token)match(input,78,FOLLOW_78_in_subselect_identification_variable_declaration1928); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal164_tree = (Object)adaptor.create(string_literal164);
					adaptor.addChild(root_0, string_literal164_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration1930);
					identification_variable165=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable165.getTree());

					// JPA2.g:215:60: ( join )*
					loop51:
					while (true) {
						int alt51=2;
						int LA51_0 = input.LA(1);
						if ( (LA51_0==INNER||(LA51_0 >= JOIN && LA51_0 <= LEFT)) ) {
							alt51=1;
						}

						switch (alt51) {
						case 1 :
							// JPA2.g:215:61: join
							{
							pushFollow(FOLLOW_join_in_subselect_identification_variable_declaration1933);
							join166=join();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, join166.getTree());

							}
							break;

						default :
							break loop51;
						}
					}

					}
					break;
				case 3 :
					// JPA2.g:216:7: derived_collection_member_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1943);
					derived_collection_member_declaration167=derived_collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_collection_member_declaration167.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:217:1: derived_path_expression : ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field );
	public final JPA2Parser.derived_path_expression_return derived_path_expression() throws RecognitionException {
		JPA2Parser.derived_path_expression_return retval = new JPA2Parser.derived_path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal169=null;
		Token char_literal172=null;
		ParserRuleReturnScope general_derived_path168 =null;
		ParserRuleReturnScope single_valued_object_field170 =null;
		ParserRuleReturnScope general_derived_path171 =null;
		ParserRuleReturnScope collection_valued_field173 =null;

		Object char_literal169_tree=null;
		Object char_literal172_tree=null;

		try {
			// JPA2.g:218:5: ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field )
			int alt53=2;
			int LA53_0 = input.LA(1);
			if ( (LA53_0==WORD) ) {
				int LA53_1 = input.LA(2);
				if ( (synpred70_JPA2()) ) {
					alt53=1;
				}
				else if ( (true) ) {
					alt53=2;
				}

			}
			else if ( (LA53_0==125) ) {
				int LA53_2 = input.LA(2);
				if ( (synpred70_JPA2()) ) {
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
					// JPA2.g:218:7: general_derived_path '.' single_valued_object_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1954);
					general_derived_path168=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path168.getTree());

					char_literal169=(Token)match(input,60,FOLLOW_60_in_derived_path_expression1955); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal169_tree = (Object)adaptor.create(char_literal169);
					adaptor.addChild(root_0, char_literal169_tree);
					}

					pushFollow(FOLLOW_single_valued_object_field_in_derived_path_expression1956);
					single_valued_object_field170=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field170.getTree());

					}
					break;
				case 2 :
					// JPA2.g:219:7: general_derived_path '.' collection_valued_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1964);
					general_derived_path171=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path171.getTree());

					char_literal172=(Token)match(input,60,FOLLOW_60_in_derived_path_expression1965); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal172_tree = (Object)adaptor.create(char_literal172);
					adaptor.addChild(root_0, char_literal172_tree);
					}

					pushFollow(FOLLOW_collection_valued_field_in_derived_path_expression1966);
					collection_valued_field173=collection_valued_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field173.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:220:1: general_derived_path : ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* );
	public final JPA2Parser.general_derived_path_return general_derived_path() throws RecognitionException {
		JPA2Parser.general_derived_path_return retval = new JPA2Parser.general_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal176=null;
		ParserRuleReturnScope simple_derived_path174 =null;
		ParserRuleReturnScope treated_derived_path175 =null;
		ParserRuleReturnScope single_valued_object_field177 =null;

		Object char_literal176_tree=null;

		try {
			// JPA2.g:221:5: ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* )
			int alt55=2;
			int LA55_0 = input.LA(1);
			if ( (LA55_0==WORD) ) {
				alt55=1;
			}
			else if ( (LA55_0==125) ) {
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
					// JPA2.g:221:7: simple_derived_path
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_derived_path_in_general_derived_path1977);
					simple_derived_path174=simple_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_derived_path174.getTree());

					}
					break;
				case 2 :
					// JPA2.g:222:7: treated_derived_path ( '.' single_valued_object_field )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_treated_derived_path_in_general_derived_path1985);
					treated_derived_path175=treated_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, treated_derived_path175.getTree());

					// JPA2.g:222:27: ( '.' single_valued_object_field )*
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
							// JPA2.g:222:28: '.' single_valued_object_field
							{
							char_literal176=(Token)match(input,60,FOLLOW_60_in_general_derived_path1987); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal176_tree = (Object)adaptor.create(char_literal176);
							adaptor.addChild(root_0, char_literal176_tree);
							}

							pushFollow(FOLLOW_single_valued_object_field_in_general_derived_path1988);
							single_valued_object_field177=single_valued_object_field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field177.getTree());

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
	// JPA2.g:224:1: simple_derived_path : superquery_identification_variable ;
	public final JPA2Parser.simple_derived_path_return simple_derived_path() throws RecognitionException {
		JPA2Parser.simple_derived_path_return retval = new JPA2Parser.simple_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope superquery_identification_variable178 =null;


		try {
			// JPA2.g:225:5: ( superquery_identification_variable )
			// JPA2.g:225:7: superquery_identification_variable
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_superquery_identification_variable_in_simple_derived_path2006);
			superquery_identification_variable178=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable178.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:227:1: treated_derived_path : 'TREAT(' general_derived_path 'AS' subtype ')' ;
	public final JPA2Parser.treated_derived_path_return treated_derived_path() throws RecognitionException {
		JPA2Parser.treated_derived_path_return retval = new JPA2Parser.treated_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal179=null;
		Token string_literal181=null;
		Token char_literal183=null;
		ParserRuleReturnScope general_derived_path180 =null;
		ParserRuleReturnScope subtype182 =null;

		Object string_literal179_tree=null;
		Object string_literal181_tree=null;
		Object char_literal183_tree=null;

		try {
			// JPA2.g:228:5: ( 'TREAT(' general_derived_path 'AS' subtype ')' )
			// JPA2.g:228:7: 'TREAT(' general_derived_path 'AS' subtype ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal179=(Token)match(input,125,FOLLOW_125_in_treated_derived_path2023); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal179_tree = (Object)adaptor.create(string_literal179);
			adaptor.addChild(root_0, string_literal179_tree);
			}

			pushFollow(FOLLOW_general_derived_path_in_treated_derived_path2024);
			general_derived_path180=general_derived_path();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path180.getTree());

			string_literal181=(Token)match(input,78,FOLLOW_78_in_treated_derived_path2026); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal181_tree = (Object)adaptor.create(string_literal181);
			adaptor.addChild(root_0, string_literal181_tree);
			}

			pushFollow(FOLLOW_subtype_in_treated_derived_path2028);
			subtype182=subtype();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subtype182.getTree());

			char_literal183=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_treated_derived_path2030); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal183_tree = (Object)adaptor.create(char_literal183);
			adaptor.addChild(root_0, char_literal183_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:229:1: derived_collection_member_declaration : 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field ;
	public final JPA2Parser.derived_collection_member_declaration_return derived_collection_member_declaration() throws RecognitionException {
		JPA2Parser.derived_collection_member_declaration_return retval = new JPA2Parser.derived_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal184=null;
		Token char_literal186=null;
		Token char_literal188=null;
		ParserRuleReturnScope superquery_identification_variable185 =null;
		ParserRuleReturnScope single_valued_object_field187 =null;
		ParserRuleReturnScope collection_valued_field189 =null;

		Object string_literal184_tree=null;
		Object char_literal186_tree=null;
		Object char_literal188_tree=null;

		try {
			// JPA2.g:230:5: ( 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field )
			// JPA2.g:230:7: 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field
			{
			root_0 = (Object)adaptor.nil();


			string_literal184=(Token)match(input,IN,FOLLOW_IN_in_derived_collection_member_declaration2041); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal184_tree = (Object)adaptor.create(string_literal184);
			adaptor.addChild(root_0, string_literal184_tree);
			}

			pushFollow(FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2043);
			superquery_identification_variable185=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable185.getTree());

			char_literal186=(Token)match(input,60,FOLLOW_60_in_derived_collection_member_declaration2044); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal186_tree = (Object)adaptor.create(char_literal186);
			adaptor.addChild(root_0, char_literal186_tree);
			}

			// JPA2.g:230:49: ( single_valued_object_field '.' )*
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
					// JPA2.g:230:50: single_valued_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2046);
					single_valued_object_field187=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field187.getTree());

					char_literal188=(Token)match(input,60,FOLLOW_60_in_derived_collection_member_declaration2048); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal188_tree = (Object)adaptor.create(char_literal188);
					adaptor.addChild(root_0, char_literal188_tree);
					}

					}
					break;

				default :
					break loop56;
				}
			}

			pushFollow(FOLLOW_collection_valued_field_in_derived_collection_member_declaration2051);
			collection_valued_field189=collection_valued_field();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field189.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:232:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
	public final JPA2Parser.simple_select_clause_return simple_select_clause() throws RecognitionException {
		JPA2Parser.simple_select_clause_return retval = new JPA2Parser.simple_select_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal190=null;
		ParserRuleReturnScope simple_select_expression191 =null;

		Object string_literal190_tree=null;
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleSubtreeStream stream_simple_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_expression");

		try {
			// JPA2.g:233:5: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
			// JPA2.g:233:7: ( 'DISTINCT' )? simple_select_expression
			{
			// JPA2.g:233:7: ( 'DISTINCT' )?
			int alt57=2;
			int LA57_0 = input.LA(1);
			if ( (LA57_0==DISTINCT) ) {
				alt57=1;
			}
			switch (alt57) {
				case 1 :
					// JPA2.g:233:8: 'DISTINCT'
					{
					string_literal190=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause2064); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal190);

					}
					break;

			}

			pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause2068);
			simple_select_expression191=simple_select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_expression.add(simple_select_expression191.getTree());
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
			// 234:5: -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
			{
				// JPA2.g:234:8: ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:234:48: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);
				// JPA2.g:234:86: ( 'DISTINCT' )?
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
	// JPA2.g:235:1: simple_select_expression : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.simple_select_expression_return simple_select_expression() throws RecognitionException {
		JPA2Parser.simple_select_expression_return retval = new JPA2Parser.simple_select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression192 =null;
		ParserRuleReturnScope scalar_expression193 =null;
		ParserRuleReturnScope aggregate_expression194 =null;
		ParserRuleReturnScope identification_variable195 =null;


		try {
			// JPA2.g:236:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt58=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA58_1 = input.LA(2);
				if ( (synpred75_JPA2()) ) {
					alt58=1;
				}
				else if ( (synpred76_JPA2()) ) {
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
				alt58=2;
				}
				break;
			case COUNT:
				{
				int LA58_16 = input.LA(2);
				if ( (synpred76_JPA2()) ) {
					alt58=2;
				}
				else if ( (synpred77_JPA2()) ) {
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
				if ( (synpred76_JPA2()) ) {
					alt58=2;
				}
				else if ( (synpred77_JPA2()) ) {
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
			case 96:
				{
				int LA58_18 = input.LA(2);
				if ( (synpred76_JPA2()) ) {
					alt58=2;
				}
				else if ( (synpred77_JPA2()) ) {
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
					// JPA2.g:236:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_simple_select_expression2108);
					path_expression192=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression192.getTree());

					}
					break;
				case 2 :
					// JPA2.g:237:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_simple_select_expression2116);
					scalar_expression193=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression193.getTree());

					}
					break;
				case 3 :
					// JPA2.g:238:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression2124);
					aggregate_expression194=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression194.getTree());

					}
					break;
				case 4 :
					// JPA2.g:239:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_select_expression2132);
					identification_variable195=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable195.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:240:1: scalar_expression : ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression );
	public final JPA2Parser.scalar_expression_return scalar_expression() throws RecognitionException {
		JPA2Parser.scalar_expression_return retval = new JPA2Parser.scalar_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope arithmetic_expression196 =null;
		ParserRuleReturnScope string_expression197 =null;
		ParserRuleReturnScope enum_expression198 =null;
		ParserRuleReturnScope datetime_expression199 =null;
		ParserRuleReturnScope boolean_expression200 =null;
		ParserRuleReturnScope case_expression201 =null;
		ParserRuleReturnScope entity_type_expression202 =null;


		try {
			// JPA2.g:241:5: ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression )
			int alt59=7;
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
				alt59=1;
				}
				break;
			case WORD:
				{
				int LA59_2 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred81_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred81_JPA2()) ) {
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
			case 96:
				{
				int LA59_18 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred81_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=5;
				}
				else if ( (synpred83_JPA2()) ) {
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
			case 82:
				{
				int LA59_20 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=5;
				}
				else if ( (synpred83_JPA2()) ) {
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
			case 112:
				{
				int LA59_21 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt59=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt59=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt59=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt59=4;
				}
				else if ( (synpred82_JPA2()) ) {
					alt59=5;
				}
				else if ( (synpred83_JPA2()) ) {
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
			case LOWER:
			case STRING_LITERAL:
			case 83:
			case 122:
			case 126:
			case 129:
				{
				alt59=2;
				}
				break;
			case 84:
			case 85:
			case 86:
				{
				alt59=4;
				}
				break;
			case 134:
			case 135:
				{
				alt59=5;
				}
				break;
			case 127:
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
					// JPA2.g:241:7: arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_scalar_expression2143);
					arithmetic_expression196=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression196.getTree());

					}
					break;
				case 2 :
					// JPA2.g:242:7: string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_scalar_expression2151);
					string_expression197=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression197.getTree());

					}
					break;
				case 3 :
					// JPA2.g:243:7: enum_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_scalar_expression2159);
					enum_expression198=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression198.getTree());

					}
					break;
				case 4 :
					// JPA2.g:244:7: datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_scalar_expression2167);
					datetime_expression199=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression199.getTree());

					}
					break;
				case 5 :
					// JPA2.g:245:7: boolean_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_scalar_expression2175);
					boolean_expression200=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression200.getTree());

					}
					break;
				case 6 :
					// JPA2.g:246:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_scalar_expression2183);
					case_expression201=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression201.getTree());

					}
					break;
				case 7 :
					// JPA2.g:247:7: entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_scalar_expression2191);
					entity_type_expression202=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression202.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:248:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
	public final JPA2Parser.conditional_expression_return conditional_expression() throws RecognitionException {
		JPA2Parser.conditional_expression_return retval = new JPA2Parser.conditional_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal204=null;
		ParserRuleReturnScope conditional_term203 =null;
		ParserRuleReturnScope conditional_term205 =null;

		Object string_literal204_tree=null;

		try {
			// JPA2.g:249:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
			// JPA2.g:249:7: ( conditional_term ) ( 'OR' conditional_term )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:249:7: ( conditional_term )
			// JPA2.g:249:8: conditional_term
			{
			pushFollow(FOLLOW_conditional_term_in_conditional_expression2203);
			conditional_term203=conditional_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term203.getTree());

			}

			// JPA2.g:249:26: ( 'OR' conditional_term )*
			loop60:
			while (true) {
				int alt60=2;
				int LA60_0 = input.LA(1);
				if ( (LA60_0==OR) ) {
					alt60=1;
				}

				switch (alt60) {
				case 1 :
					// JPA2.g:249:27: 'OR' conditional_term
					{
					string_literal204=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression2207); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal204_tree = (Object)adaptor.create(string_literal204);
					adaptor.addChild(root_0, string_literal204_tree);
					}

					pushFollow(FOLLOW_conditional_term_in_conditional_expression2209);
					conditional_term205=conditional_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term205.getTree());

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
	// JPA2.g:250:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
	public final JPA2Parser.conditional_term_return conditional_term() throws RecognitionException {
		JPA2Parser.conditional_term_return retval = new JPA2Parser.conditional_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal207=null;
		ParserRuleReturnScope conditional_factor206 =null;
		ParserRuleReturnScope conditional_factor208 =null;

		Object string_literal207_tree=null;

		try {
			// JPA2.g:251:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
			// JPA2.g:251:7: ( conditional_factor ) ( 'AND' conditional_factor )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:251:7: ( conditional_factor )
			// JPA2.g:251:8: conditional_factor
			{
			pushFollow(FOLLOW_conditional_factor_in_conditional_term2223);
			conditional_factor206=conditional_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor206.getTree());

			}

			// JPA2.g:251:28: ( 'AND' conditional_factor )*
			loop61:
			while (true) {
				int alt61=2;
				int LA61_0 = input.LA(1);
				if ( (LA61_0==AND) ) {
					alt61=1;
				}

				switch (alt61) {
				case 1 :
					// JPA2.g:251:29: 'AND' conditional_factor
					{
					string_literal207=(Token)match(input,AND,FOLLOW_AND_in_conditional_term2227); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal207_tree = (Object)adaptor.create(string_literal207);
					adaptor.addChild(root_0, string_literal207_tree);
					}

					pushFollow(FOLLOW_conditional_factor_in_conditional_term2229);
					conditional_factor208=conditional_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor208.getTree());

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
	// JPA2.g:252:1: conditional_factor : ( 'NOT' )? conditional_primary ;
	public final JPA2Parser.conditional_factor_return conditional_factor() throws RecognitionException {
		JPA2Parser.conditional_factor_return retval = new JPA2Parser.conditional_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal209=null;
		ParserRuleReturnScope conditional_primary210 =null;

		Object string_literal209_tree=null;

		try {
			// JPA2.g:253:5: ( ( 'NOT' )? conditional_primary )
			// JPA2.g:253:7: ( 'NOT' )? conditional_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:253:7: ( 'NOT' )?
			int alt62=2;
			int LA62_0 = input.LA(1);
			if ( (LA62_0==NOT) ) {
				int LA62_1 = input.LA(2);
				if ( (synpred86_JPA2()) ) {
					alt62=1;
				}
			}
			switch (alt62) {
				case 1 :
					// JPA2.g:253:8: 'NOT'
					{
					string_literal209=(Token)match(input,NOT,FOLLOW_NOT_in_conditional_factor2243); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal209_tree = (Object)adaptor.create(string_literal209);
					adaptor.addChild(root_0, string_literal209_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_conditional_primary_in_conditional_factor2247);
			conditional_primary210=conditional_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_primary210.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:254:1: conditional_primary : ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' );
	public final JPA2Parser.conditional_primary_return conditional_primary() throws RecognitionException {
		JPA2Parser.conditional_primary_return retval = new JPA2Parser.conditional_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal212=null;
		Token char_literal214=null;
		ParserRuleReturnScope simple_cond_expression211 =null;
		ParserRuleReturnScope conditional_expression213 =null;

		Object char_literal212_tree=null;
		Object char_literal214_tree=null;
		RewriteRuleSubtreeStream stream_simple_cond_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_cond_expression");

		try {
			// JPA2.g:255:5: ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' )
			int alt63=2;
			int LA63_0 = input.LA(1);
			if ( (LA63_0==AVG||LA63_0==COUNT||LA63_0==INT_NUMERAL||LA63_0==LOWER||(LA63_0 >= MAX && LA63_0 <= NOT)||(LA63_0 >= STRING_LITERAL && LA63_0 <= SUM)||LA63_0==WORD||LA63_0==55||LA63_0==57||LA63_0==59||LA63_0==62||(LA63_0 >= 69 && LA63_0 <= 75)||(LA63_0 >= 81 && LA63_0 <= 86)||LA63_0==94||LA63_0==96||LA63_0==98||LA63_0==102||LA63_0==104||LA63_0==107||LA63_0==112||LA63_0==119||(LA63_0 >= 121 && LA63_0 <= 122)||(LA63_0 >= 125 && LA63_0 <= 127)||LA63_0==129||(LA63_0 >= 134 && LA63_0 <= 135)) ) {
				alt63=1;
			}
			else if ( (LA63_0==LPAREN) ) {
				int LA63_17 = input.LA(2);
				if ( (synpred87_JPA2()) ) {
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
					// JPA2.g:255:7: simple_cond_expression
					{
					pushFollow(FOLLOW_simple_cond_expression_in_conditional_primary2258);
					simple_cond_expression211=simple_cond_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_simple_cond_expression.add(simple_cond_expression211.getTree());
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
					// 256:5: -> ^( T_SIMPLE_CONDITION[] simple_cond_expression )
					{
						// JPA2.g:256:8: ^( T_SIMPLE_CONDITION[] simple_cond_expression )
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
					// JPA2.g:257:7: '(' conditional_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal212=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_primary2282); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal212_tree = (Object)adaptor.create(char_literal212);
					adaptor.addChild(root_0, char_literal212_tree);
					}

					pushFollow(FOLLOW_conditional_expression_in_conditional_primary2283);
					conditional_expression213=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression213.getTree());

					char_literal214=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_primary2284); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal214_tree = (Object)adaptor.create(char_literal214);
					adaptor.addChild(root_0, char_literal214_tree);
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
	// JPA2.g:258:1: simple_cond_expression : ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
	public final JPA2Parser.simple_cond_expression_return simple_cond_expression() throws RecognitionException {
		JPA2Parser.simple_cond_expression_return retval = new JPA2Parser.simple_cond_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope comparison_expression215 =null;
		ParserRuleReturnScope between_expression216 =null;
		ParserRuleReturnScope in_expression217 =null;
		ParserRuleReturnScope like_expression218 =null;
		ParserRuleReturnScope null_comparison_expression219 =null;
		ParserRuleReturnScope empty_collection_comparison_expression220 =null;
		ParserRuleReturnScope collection_member_expression221 =null;
		ParserRuleReturnScope exists_expression222 =null;
		ParserRuleReturnScope date_macro_expression223 =null;


		try {
			// JPA2.g:259:5: ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
			int alt64=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA64_1 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt64=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt64=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=5;
				}
				else if ( (synpred93_JPA2()) ) {
					alt64=6;
				}
				else if ( (synpred94_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt64=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=5;
				}
				else if ( (synpred94_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt64=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=5;
				}
				else if ( (synpred94_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt64=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt64=5;
				}
				else if ( (synpred94_JPA2()) ) {
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
			case 83:
				{
				int LA64_6 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 122:
				{
				int LA64_7 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 126:
				{
				int LA64_8 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 129:
				{
				int LA64_10 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 96:
				{
				int LA64_13 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 82:
				{
				int LA64_15 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 112:
				{
				int LA64_16 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case LPAREN:
				{
				int LA64_17 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt64=4;
				}
				else if ( (synpred94_JPA2()) ) {
					alt64=7;
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
			case 134:
			case 135:
				{
				alt64=1;
				}
				break;
			case 84:
			case 85:
			case 86:
				{
				int LA64_19 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt64=2;
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
			case 127:
				{
				int LA64_20 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt64=3;
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
			case 57:
			case 59:
				{
				int LA64_21 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 62:
				{
				int LA64_22 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case INT_NUMERAL:
				{
				int LA64_23 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 102:
				{
				int LA64_24 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 104:
				{
				int LA64_25 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 75:
				{
				int LA64_26 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 121:
				{
				int LA64_27 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 107:
				{
				int LA64_28 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 119:
				{
				int LA64_29 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 98:
				{
				int LA64_30 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt64=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 125:
				{
				alt64=5;
				}
				break;
			case NOT:
			case 94:
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
					// JPA2.g:259:7: comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression2295);
					comparison_expression215=comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression215.getTree());

					}
					break;
				case 2 :
					// JPA2.g:260:7: between_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_between_expression_in_simple_cond_expression2303);
					between_expression216=between_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression216.getTree());

					}
					break;
				case 3 :
					// JPA2.g:261:7: in_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_in_expression_in_simple_cond_expression2311);
					in_expression217=in_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression217.getTree());

					}
					break;
				case 4 :
					// JPA2.g:262:7: like_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_like_expression_in_simple_cond_expression2319);
					like_expression218=like_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression218.getTree());

					}
					break;
				case 5 :
					// JPA2.g:263:7: null_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression2327);
					null_comparison_expression219=null_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression219.getTree());

					}
					break;
				case 6 :
					// JPA2.g:264:7: empty_collection_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2335);
					empty_collection_comparison_expression220=empty_collection_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression220.getTree());

					}
					break;
				case 7 :
					// JPA2.g:265:7: collection_member_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression2343);
					collection_member_expression221=collection_member_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression221.getTree());

					}
					break;
				case 8 :
					// JPA2.g:266:7: exists_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exists_expression_in_simple_cond_expression2351);
					exists_expression222=exists_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression222.getTree());

					}
					break;
				case 9 :
					// JPA2.g:267:7: date_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression2359);
					date_macro_expression223=date_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_macro_expression223.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:270:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
	public final JPA2Parser.date_macro_expression_return date_macro_expression() throws RecognitionException {
		JPA2Parser.date_macro_expression_return retval = new JPA2Parser.date_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope date_between_macro_expression224 =null;
		ParserRuleReturnScope date_before_macro_expression225 =null;
		ParserRuleReturnScope date_after_macro_expression226 =null;
		ParserRuleReturnScope date_equals_macro_expression227 =null;
		ParserRuleReturnScope date_today_macro_expression228 =null;


		try {
			// JPA2.g:271:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
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
					// JPA2.g:271:7: date_between_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2372);
					date_between_macro_expression224=date_between_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression224.getTree());

					}
					break;
				case 2 :
					// JPA2.g:272:7: date_before_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2380);
					date_before_macro_expression225=date_before_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression225.getTree());

					}
					break;
				case 3 :
					// JPA2.g:273:7: date_after_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2388);
					date_after_macro_expression226=date_after_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression226.getTree());

					}
					break;
				case 4 :
					// JPA2.g:274:7: date_equals_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2396);
					date_equals_macro_expression227=date_equals_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression227.getTree());

					}
					break;
				case 5 :
					// JPA2.g:275:7: date_today_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2404);
					date_today_macro_expression228=date_today_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_today_macro_expression228.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:277:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
	public final JPA2Parser.date_between_macro_expression_return date_between_macro_expression() throws RecognitionException {
		JPA2Parser.date_between_macro_expression_return retval = new JPA2Parser.date_between_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal229=null;
		Token char_literal230=null;
		Token char_literal232=null;
		Token string_literal233=null;
		Token set234=null;
		Token char_literal236=null;
		Token string_literal237=null;
		Token set238=null;
		Token char_literal240=null;
		Token set241=null;
		Token char_literal242=null;
		ParserRuleReturnScope path_expression231 =null;
		ParserRuleReturnScope numeric_literal235 =null;
		ParserRuleReturnScope numeric_literal239 =null;

		Object string_literal229_tree=null;
		Object char_literal230_tree=null;
		Object char_literal232_tree=null;
		Object string_literal233_tree=null;
		Object set234_tree=null;
		Object char_literal236_tree=null;
		Object string_literal237_tree=null;
		Object set238_tree=null;
		Object char_literal240_tree=null;
		Object set241_tree=null;
		Object char_literal242_tree=null;

		try {
			// JPA2.g:278:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
			// JPA2.g:278:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal229=(Token)match(input,70,FOLLOW_70_in_date_between_macro_expression2416); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal229_tree = (Object)adaptor.create(string_literal229);
			adaptor.addChild(root_0, string_literal229_tree);
			}

			char_literal230=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2418); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal230_tree = (Object)adaptor.create(char_literal230);
			adaptor.addChild(root_0, char_literal230_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2420);
			path_expression231=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression231.getTree());

			char_literal232=(Token)match(input,58,FOLLOW_58_in_date_between_macro_expression2422); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal232_tree = (Object)adaptor.create(char_literal232);
			adaptor.addChild(root_0, char_literal232_tree);
			}

			string_literal233=(Token)match(input,110,FOLLOW_110_in_date_between_macro_expression2424); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal233_tree = (Object)adaptor.create(string_literal233);
			adaptor.addChild(root_0, string_literal233_tree);
			}

			// JPA2.g:278:48: ( ( '+' | '-' ) numeric_literal )?
			int alt66=2;
			int LA66_0 = input.LA(1);
			if ( (LA66_0==57||LA66_0==59) ) {
				alt66=1;
			}
			switch (alt66) {
				case 1 :
					// JPA2.g:278:49: ( '+' | '-' ) numeric_literal
					{
					set234=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set234));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2435);
					numeric_literal235=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal235.getTree());

					}
					break;

			}

			char_literal236=(Token)match(input,58,FOLLOW_58_in_date_between_macro_expression2439); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal236_tree = (Object)adaptor.create(char_literal236);
			adaptor.addChild(root_0, char_literal236_tree);
			}

			string_literal237=(Token)match(input,110,FOLLOW_110_in_date_between_macro_expression2441); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal237_tree = (Object)adaptor.create(string_literal237);
			adaptor.addChild(root_0, string_literal237_tree);
			}

			// JPA2.g:278:89: ( ( '+' | '-' ) numeric_literal )?
			int alt67=2;
			int LA67_0 = input.LA(1);
			if ( (LA67_0==57||LA67_0==59) ) {
				alt67=1;
			}
			switch (alt67) {
				case 1 :
					// JPA2.g:278:90: ( '+' | '-' ) numeric_literal
					{
					set238=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set238));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2452);
					numeric_literal239=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal239.getTree());

					}
					break;

			}

			char_literal240=(Token)match(input,58,FOLLOW_58_in_date_between_macro_expression2456); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal240_tree = (Object)adaptor.create(char_literal240);
			adaptor.addChild(root_0, char_literal240_tree);
			}

			set241=input.LT(1);
			if ( input.LA(1)==87||input.LA(1)==97||input.LA(1)==106||input.LA(1)==108||input.LA(1)==116||input.LA(1)==133 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set241));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			char_literal242=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2481); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal242_tree = (Object)adaptor.create(char_literal242);
			adaptor.addChild(root_0, char_literal242_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:280:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_before_macro_expression_return date_before_macro_expression() throws RecognitionException {
		JPA2Parser.date_before_macro_expression_return retval = new JPA2Parser.date_before_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal243=null;
		Token char_literal244=null;
		Token char_literal246=null;
		Token char_literal249=null;
		ParserRuleReturnScope path_expression245 =null;
		ParserRuleReturnScope path_expression247 =null;
		ParserRuleReturnScope input_parameter248 =null;

		Object string_literal243_tree=null;
		Object char_literal244_tree=null;
		Object char_literal246_tree=null;
		Object char_literal249_tree=null;

		try {
			// JPA2.g:281:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:281:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal243=(Token)match(input,72,FOLLOW_72_in_date_before_macro_expression2493); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal243_tree = (Object)adaptor.create(string_literal243);
			adaptor.addChild(root_0, string_literal243_tree);
			}

			char_literal244=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2495); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal244_tree = (Object)adaptor.create(char_literal244);
			adaptor.addChild(root_0, char_literal244_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2497);
			path_expression245=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression245.getTree());

			char_literal246=(Token)match(input,58,FOLLOW_58_in_date_before_macro_expression2499); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal246_tree = (Object)adaptor.create(char_literal246);
			adaptor.addChild(root_0, char_literal246_tree);
			}

			// JPA2.g:281:45: ( path_expression | input_parameter )
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
					// JPA2.g:281:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2502);
					path_expression247=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression247.getTree());

					}
					break;
				case 2 :
					// JPA2.g:281:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2506);
					input_parameter248=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter248.getTree());

					}
					break;

			}

			char_literal249=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2509); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal249_tree = (Object)adaptor.create(char_literal249);
			adaptor.addChild(root_0, char_literal249_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:283:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_after_macro_expression_return date_after_macro_expression() throws RecognitionException {
		JPA2Parser.date_after_macro_expression_return retval = new JPA2Parser.date_after_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal250=null;
		Token char_literal251=null;
		Token char_literal253=null;
		Token char_literal256=null;
		ParserRuleReturnScope path_expression252 =null;
		ParserRuleReturnScope path_expression254 =null;
		ParserRuleReturnScope input_parameter255 =null;

		Object string_literal250_tree=null;
		Object char_literal251_tree=null;
		Object char_literal253_tree=null;
		Object char_literal256_tree=null;

		try {
			// JPA2.g:284:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:284:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal250=(Token)match(input,71,FOLLOW_71_in_date_after_macro_expression2521); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal250_tree = (Object)adaptor.create(string_literal250);
			adaptor.addChild(root_0, string_literal250_tree);
			}

			char_literal251=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2523); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal251_tree = (Object)adaptor.create(char_literal251);
			adaptor.addChild(root_0, char_literal251_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2525);
			path_expression252=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression252.getTree());

			char_literal253=(Token)match(input,58,FOLLOW_58_in_date_after_macro_expression2527); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal253_tree = (Object)adaptor.create(char_literal253);
			adaptor.addChild(root_0, char_literal253_tree);
			}

			// JPA2.g:284:44: ( path_expression | input_parameter )
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
					// JPA2.g:284:45: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2530);
					path_expression254=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression254.getTree());

					}
					break;
				case 2 :
					// JPA2.g:284:63: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2534);
					input_parameter255=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter255.getTree());

					}
					break;

			}

			char_literal256=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2537); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal256_tree = (Object)adaptor.create(char_literal256);
			adaptor.addChild(root_0, char_literal256_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:286:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_equals_macro_expression_return date_equals_macro_expression() throws RecognitionException {
		JPA2Parser.date_equals_macro_expression_return retval = new JPA2Parser.date_equals_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal257=null;
		Token char_literal258=null;
		Token char_literal260=null;
		Token char_literal263=null;
		ParserRuleReturnScope path_expression259 =null;
		ParserRuleReturnScope path_expression261 =null;
		ParserRuleReturnScope input_parameter262 =null;

		Object string_literal257_tree=null;
		Object char_literal258_tree=null;
		Object char_literal260_tree=null;
		Object char_literal263_tree=null;

		try {
			// JPA2.g:287:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:287:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal257=(Token)match(input,73,FOLLOW_73_in_date_equals_macro_expression2549); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal257_tree = (Object)adaptor.create(string_literal257);
			adaptor.addChild(root_0, string_literal257_tree);
			}

			char_literal258=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2551); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal258_tree = (Object)adaptor.create(char_literal258);
			adaptor.addChild(root_0, char_literal258_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2553);
			path_expression259=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression259.getTree());

			char_literal260=(Token)match(input,58,FOLLOW_58_in_date_equals_macro_expression2555); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal260_tree = (Object)adaptor.create(char_literal260);
			adaptor.addChild(root_0, char_literal260_tree);
			}

			// JPA2.g:287:45: ( path_expression | input_parameter )
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
					// JPA2.g:287:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2558);
					path_expression261=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression261.getTree());

					}
					break;
				case 2 :
					// JPA2.g:287:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2562);
					input_parameter262=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter262.getTree());

					}
					break;

			}

			char_literal263=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2565); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal263_tree = (Object)adaptor.create(char_literal263);
			adaptor.addChild(root_0, char_literal263_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:289:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
	public final JPA2Parser.date_today_macro_expression_return date_today_macro_expression() throws RecognitionException {
		JPA2Parser.date_today_macro_expression_return retval = new JPA2Parser.date_today_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal264=null;
		Token char_literal265=null;
		Token char_literal267=null;
		ParserRuleReturnScope path_expression266 =null;

		Object string_literal264_tree=null;
		Object char_literal265_tree=null;
		Object char_literal267_tree=null;

		try {
			// JPA2.g:290:5: ( '@TODAY' '(' path_expression ')' )
			// JPA2.g:290:7: '@TODAY' '(' path_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal264=(Token)match(input,74,FOLLOW_74_in_date_today_macro_expression2577); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal264_tree = (Object)adaptor.create(string_literal264);
			adaptor.addChild(root_0, string_literal264_tree);
			}

			char_literal265=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2579); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal265_tree = (Object)adaptor.create(char_literal265);
			adaptor.addChild(root_0, char_literal265_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2581);
			path_expression266=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression266.getTree());

			char_literal267=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2583); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal267_tree = (Object)adaptor.create(char_literal267);
			adaptor.addChild(root_0, char_literal267_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:293:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
	public final JPA2Parser.between_expression_return between_expression() throws RecognitionException {
		JPA2Parser.between_expression_return retval = new JPA2Parser.between_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal269=null;
		Token string_literal270=null;
		Token string_literal272=null;
		Token string_literal275=null;
		Token string_literal276=null;
		Token string_literal278=null;
		Token string_literal281=null;
		Token string_literal282=null;
		Token string_literal284=null;
		ParserRuleReturnScope arithmetic_expression268 =null;
		ParserRuleReturnScope arithmetic_expression271 =null;
		ParserRuleReturnScope arithmetic_expression273 =null;
		ParserRuleReturnScope string_expression274 =null;
		ParserRuleReturnScope string_expression277 =null;
		ParserRuleReturnScope string_expression279 =null;
		ParserRuleReturnScope datetime_expression280 =null;
		ParserRuleReturnScope datetime_expression283 =null;
		ParserRuleReturnScope datetime_expression285 =null;

		Object string_literal269_tree=null;
		Object string_literal270_tree=null;
		Object string_literal272_tree=null;
		Object string_literal275_tree=null;
		Object string_literal276_tree=null;
		Object string_literal278_tree=null;
		Object string_literal281_tree=null;
		Object string_literal282_tree=null;
		Object string_literal284_tree=null;

		try {
			// JPA2.g:294:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
			int alt74=3;
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
				alt74=1;
				}
				break;
			case WORD:
				{
				int LA74_2 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 96:
				{
				int LA74_18 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 82:
				{
				int LA74_20 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
				}

				}
				break;
			case 112:
				{
				int LA74_21 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt74=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt74=2;
				}
				else if ( (true) ) {
					alt74=3;
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
				alt74=2;
				}
				break;
			case 84:
			case 85:
			case 86:
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
					// JPA2.g:294:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2596);
					arithmetic_expression268=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression268.getTree());

					// JPA2.g:294:29: ( 'NOT' )?
					int alt71=2;
					int LA71_0 = input.LA(1);
					if ( (LA71_0==NOT) ) {
						alt71=1;
					}
					switch (alt71) {
						case 1 :
							// JPA2.g:294:30: 'NOT'
							{
							string_literal269=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2599); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal269_tree = (Object)adaptor.create(string_literal269);
							adaptor.addChild(root_0, string_literal269_tree);
							}

							}
							break;

					}

					string_literal270=(Token)match(input,79,FOLLOW_79_in_between_expression2603); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal270_tree = (Object)adaptor.create(string_literal270);
					adaptor.addChild(root_0, string_literal270_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2605);
					arithmetic_expression271=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression271.getTree());

					string_literal272=(Token)match(input,AND,FOLLOW_AND_in_between_expression2607); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal272_tree = (Object)adaptor.create(string_literal272);
					adaptor.addChild(root_0, string_literal272_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2609);
					arithmetic_expression273=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression273.getTree());

					}
					break;
				case 2 :
					// JPA2.g:295:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_between_expression2617);
					string_expression274=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression274.getTree());

					// JPA2.g:295:25: ( 'NOT' )?
					int alt72=2;
					int LA72_0 = input.LA(1);
					if ( (LA72_0==NOT) ) {
						alt72=1;
					}
					switch (alt72) {
						case 1 :
							// JPA2.g:295:26: 'NOT'
							{
							string_literal275=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2620); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal275_tree = (Object)adaptor.create(string_literal275);
							adaptor.addChild(root_0, string_literal275_tree);
							}

							}
							break;

					}

					string_literal276=(Token)match(input,79,FOLLOW_79_in_between_expression2624); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal276_tree = (Object)adaptor.create(string_literal276);
					adaptor.addChild(root_0, string_literal276_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2626);
					string_expression277=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression277.getTree());

					string_literal278=(Token)match(input,AND,FOLLOW_AND_in_between_expression2628); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal278_tree = (Object)adaptor.create(string_literal278);
					adaptor.addChild(root_0, string_literal278_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2630);
					string_expression279=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression279.getTree());

					}
					break;
				case 3 :
					// JPA2.g:296:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_between_expression2638);
					datetime_expression280=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression280.getTree());

					// JPA2.g:296:27: ( 'NOT' )?
					int alt73=2;
					int LA73_0 = input.LA(1);
					if ( (LA73_0==NOT) ) {
						alt73=1;
					}
					switch (alt73) {
						case 1 :
							// JPA2.g:296:28: 'NOT'
							{
							string_literal281=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2641); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal281_tree = (Object)adaptor.create(string_literal281);
							adaptor.addChild(root_0, string_literal281_tree);
							}

							}
							break;

					}

					string_literal282=(Token)match(input,79,FOLLOW_79_in_between_expression2645); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal282_tree = (Object)adaptor.create(string_literal282);
					adaptor.addChild(root_0, string_literal282_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2647);
					datetime_expression283=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression283.getTree());

					string_literal284=(Token)match(input,AND,FOLLOW_AND_in_between_expression2649); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal284_tree = (Object)adaptor.create(string_literal284);
					adaptor.addChild(root_0, string_literal284_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2651);
					datetime_expression285=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression285.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:297:1: in_expression : ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) ;
	public final JPA2Parser.in_expression_return in_expression() throws RecognitionException {
		JPA2Parser.in_expression_return retval = new JPA2Parser.in_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NOT289=null;
		Token IN290=null;
		Token char_literal291=null;
		Token char_literal293=null;
		Token char_literal295=null;
		Token char_literal298=null;
		Token char_literal300=null;
		ParserRuleReturnScope path_expression286 =null;
		ParserRuleReturnScope type_discriminator287 =null;
		ParserRuleReturnScope identification_variable288 =null;
		ParserRuleReturnScope in_item292 =null;
		ParserRuleReturnScope in_item294 =null;
		ParserRuleReturnScope subquery296 =null;
		ParserRuleReturnScope collection_valued_input_parameter297 =null;
		ParserRuleReturnScope path_expression299 =null;

		Object NOT289_tree=null;
		Object IN290_tree=null;
		Object char_literal291_tree=null;
		Object char_literal293_tree=null;
		Object char_literal295_tree=null;
		Object char_literal298_tree=null;
		Object char_literal300_tree=null;

		try {
			// JPA2.g:298:5: ( ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) )
			// JPA2.g:298:7: ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:298:7: ( path_expression | type_discriminator | identification_variable )
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
			else if ( (LA75_0==127) ) {
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
					// JPA2.g:298:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_in_expression2663);
					path_expression286=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression286.getTree());

					}
					break;
				case 2 :
					// JPA2.g:298:26: type_discriminator
					{
					pushFollow(FOLLOW_type_discriminator_in_in_expression2667);
					type_discriminator287=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator287.getTree());

					}
					break;
				case 3 :
					// JPA2.g:298:47: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_in_expression2671);
					identification_variable288=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable288.getTree());

					}
					break;

			}

			// JPA2.g:298:72: ( NOT )?
			int alt76=2;
			int LA76_0 = input.LA(1);
			if ( (LA76_0==NOT) ) {
				alt76=1;
			}
			switch (alt76) {
				case 1 :
					// JPA2.g:298:73: NOT
					{
					NOT289=(Token)match(input,NOT,FOLLOW_NOT_in_in_expression2675); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT289_tree = (Object)adaptor.create(NOT289);
					adaptor.addChild(root_0, NOT289_tree);
					}

					}
					break;

			}

			IN290=(Token)match(input,IN,FOLLOW_IN_in_in_expression2679); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN290_tree = (Object)adaptor.create(IN290);
			adaptor.addChild(root_0, IN290_tree);
			}

			// JPA2.g:299:13: ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			int alt78=4;
			int LA78_0 = input.LA(1);
			if ( (LA78_0==LPAREN) ) {
				switch ( input.LA(2) ) {
				case 117:
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
					// JPA2.g:299:15: '(' in_item ( ',' in_item )* ')'
					{
					char_literal291=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2695); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal291_tree = (Object)adaptor.create(char_literal291);
					adaptor.addChild(root_0, char_literal291_tree);
					}

					pushFollow(FOLLOW_in_item_in_in_expression2697);
					in_item292=in_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item292.getTree());

					// JPA2.g:299:27: ( ',' in_item )*
					loop77:
					while (true) {
						int alt77=2;
						int LA77_0 = input.LA(1);
						if ( (LA77_0==58) ) {
							alt77=1;
						}

						switch (alt77) {
						case 1 :
							// JPA2.g:299:28: ',' in_item
							{
							char_literal293=(Token)match(input,58,FOLLOW_58_in_in_expression2700); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal293_tree = (Object)adaptor.create(char_literal293);
							adaptor.addChild(root_0, char_literal293_tree);
							}

							pushFollow(FOLLOW_in_item_in_in_expression2702);
							in_item294=in_item();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item294.getTree());

							}
							break;

						default :
							break loop77;
						}
					}

					char_literal295=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2706); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal295_tree = (Object)adaptor.create(char_literal295);
					adaptor.addChild(root_0, char_literal295_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:300:15: subquery
					{
					pushFollow(FOLLOW_subquery_in_in_expression2722);
					subquery296=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery296.getTree());

					}
					break;
				case 3 :
					// JPA2.g:301:15: collection_valued_input_parameter
					{
					pushFollow(FOLLOW_collection_valued_input_parameter_in_in_expression2738);
					collection_valued_input_parameter297=collection_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_input_parameter297.getTree());

					}
					break;
				case 4 :
					// JPA2.g:302:15: '(' path_expression ')'
					{
					char_literal298=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2754); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal298_tree = (Object)adaptor.create(char_literal298);
					adaptor.addChild(root_0, char_literal298_tree);
					}

					pushFollow(FOLLOW_path_expression_in_in_expression2756);
					path_expression299=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression299.getTree());

					char_literal300=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2758); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal300_tree = (Object)adaptor.create(char_literal300);
					adaptor.addChild(root_0, char_literal300_tree);
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
	// JPA2.g:308:1: in_item : ( literal | single_valued_input_parameter );
	public final JPA2Parser.in_item_return in_item() throws RecognitionException {
		JPA2Parser.in_item_return retval = new JPA2Parser.in_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal301 =null;
		ParserRuleReturnScope single_valued_input_parameter302 =null;


		try {
			// JPA2.g:309:5: ( literal | single_valued_input_parameter )
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
					// JPA2.g:309:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_in_item2786);
					literal301=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal301.getTree());

					}
					break;
				case 2 :
					// JPA2.g:309:17: single_valued_input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_single_valued_input_parameter_in_in_item2790);
					single_valued_input_parameter302=single_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_input_parameter302.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:310:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? ;
	public final JPA2Parser.like_expression_return like_expression() throws RecognitionException {
		JPA2Parser.like_expression_return retval = new JPA2Parser.like_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal304=null;
		Token string_literal305=null;
		Token string_literal308=null;
		ParserRuleReturnScope string_expression303 =null;
		ParserRuleReturnScope pattern_value306 =null;
		ParserRuleReturnScope input_parameter307 =null;
		ParserRuleReturnScope escape_character309 =null;

		Object string_literal304_tree=null;
		Object string_literal305_tree=null;
		Object string_literal308_tree=null;

		try {
			// JPA2.g:311:5: ( string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? )
			// JPA2.g:311:7: string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_expression_in_like_expression2801);
			string_expression303=string_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression303.getTree());

			// JPA2.g:311:25: ( 'NOT' )?
			int alt80=2;
			int LA80_0 = input.LA(1);
			if ( (LA80_0==NOT) ) {
				alt80=1;
			}
			switch (alt80) {
				case 1 :
					// JPA2.g:311:26: 'NOT'
					{
					string_literal304=(Token)match(input,NOT,FOLLOW_NOT_in_like_expression2804); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal304_tree = (Object)adaptor.create(string_literal304);
					adaptor.addChild(root_0, string_literal304_tree);
					}

					}
					break;

			}

			string_literal305=(Token)match(input,103,FOLLOW_103_in_like_expression2808); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal305_tree = (Object)adaptor.create(string_literal305);
			adaptor.addChild(root_0, string_literal305_tree);
			}

			// JPA2.g:311:41: ( pattern_value | input_parameter )
			int alt81=2;
			int LA81_0 = input.LA(1);
			if ( (LA81_0==STRING_LITERAL) ) {
				alt81=1;
			}
			else if ( (LA81_0==NAMED_PARAMETER||LA81_0==55||LA81_0==69) ) {
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
					// JPA2.g:311:42: pattern_value
					{
					pushFollow(FOLLOW_pattern_value_in_like_expression2811);
					pattern_value306=pattern_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value306.getTree());

					}
					break;
				case 2 :
					// JPA2.g:311:58: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_like_expression2815);
					input_parameter307=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter307.getTree());

					}
					break;

			}

			// JPA2.g:311:74: ( 'ESCAPE' escape_character )?
			int alt82=2;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==93) ) {
				alt82=1;
			}
			switch (alt82) {
				case 1 :
					// JPA2.g:311:75: 'ESCAPE' escape_character
					{
					string_literal308=(Token)match(input,93,FOLLOW_93_in_like_expression2818); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal308_tree = (Object)adaptor.create(string_literal308);
					adaptor.addChild(root_0, string_literal308_tree);
					}

					pushFollow(FOLLOW_escape_character_in_like_expression2820);
					escape_character309=escape_character();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, escape_character309.getTree());

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
	// JPA2.g:312:1: null_comparison_expression : ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' ;
	public final JPA2Parser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
		JPA2Parser.null_comparison_expression_return retval = new JPA2Parser.null_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal313=null;
		Token string_literal314=null;
		Token string_literal315=null;
		ParserRuleReturnScope path_expression310 =null;
		ParserRuleReturnScope input_parameter311 =null;
		ParserRuleReturnScope join_association_path_expression312 =null;

		Object string_literal313_tree=null;
		Object string_literal314_tree=null;
		Object string_literal315_tree=null;

		try {
			// JPA2.g:313:5: ( ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' )
			// JPA2.g:313:7: ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:313:7: ( path_expression | input_parameter | join_association_path_expression )
			int alt83=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA83_1 = input.LA(2);
				if ( (LA83_1==60) ) {
					int LA83_4 = input.LA(3);
					if ( (synpred128_JPA2()) ) {
						alt83=1;
					}
					else if ( (true) ) {
						alt83=3;
					}

				}
				else if ( (LA83_1==99) ) {
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
			case 125:
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
					// JPA2.g:313:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_null_comparison_expression2834);
					path_expression310=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression310.getTree());

					}
					break;
				case 2 :
					// JPA2.g:313:26: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_null_comparison_expression2838);
					input_parameter311=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter311.getTree());

					}
					break;
				case 3 :
					// JPA2.g:313:44: join_association_path_expression
					{
					pushFollow(FOLLOW_join_association_path_expression_in_null_comparison_expression2842);
					join_association_path_expression312=join_association_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression312.getTree());

					}
					break;

			}

			string_literal313=(Token)match(input,99,FOLLOW_99_in_null_comparison_expression2845); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal313_tree = (Object)adaptor.create(string_literal313);
			adaptor.addChild(root_0, string_literal313_tree);
			}

			// JPA2.g:313:83: ( 'NOT' )?
			int alt84=2;
			int LA84_0 = input.LA(1);
			if ( (LA84_0==NOT) ) {
				alt84=1;
			}
			switch (alt84) {
				case 1 :
					// JPA2.g:313:84: 'NOT'
					{
					string_literal314=(Token)match(input,NOT,FOLLOW_NOT_in_null_comparison_expression2848); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal314_tree = (Object)adaptor.create(string_literal314);
					adaptor.addChild(root_0, string_literal314_tree);
					}

					}
					break;

			}

			string_literal315=(Token)match(input,111,FOLLOW_111_in_null_comparison_expression2852); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal315_tree = (Object)adaptor.create(string_literal315);
			adaptor.addChild(root_0, string_literal315_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:314:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
	public final JPA2Parser.empty_collection_comparison_expression_return empty_collection_comparison_expression() throws RecognitionException {
		JPA2Parser.empty_collection_comparison_expression_return retval = new JPA2Parser.empty_collection_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal317=null;
		Token string_literal318=null;
		Token string_literal319=null;
		ParserRuleReturnScope path_expression316 =null;

		Object string_literal317_tree=null;
		Object string_literal318_tree=null;
		Object string_literal319_tree=null;

		try {
			// JPA2.g:315:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
			// JPA2.g:315:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression2863);
			path_expression316=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression316.getTree());

			string_literal317=(Token)match(input,99,FOLLOW_99_in_empty_collection_comparison_expression2865); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal317_tree = (Object)adaptor.create(string_literal317);
			adaptor.addChild(root_0, string_literal317_tree);
			}

			// JPA2.g:315:28: ( 'NOT' )?
			int alt85=2;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==NOT) ) {
				alt85=1;
			}
			switch (alt85) {
				case 1 :
					// JPA2.g:315:29: 'NOT'
					{
					string_literal318=(Token)match(input,NOT,FOLLOW_NOT_in_empty_collection_comparison_expression2868); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal318_tree = (Object)adaptor.create(string_literal318);
					adaptor.addChild(root_0, string_literal318_tree);
					}

					}
					break;

			}

			string_literal319=(Token)match(input,90,FOLLOW_90_in_empty_collection_comparison_expression2872); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal319_tree = (Object)adaptor.create(string_literal319);
			adaptor.addChild(root_0, string_literal319_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:316:1: collection_member_expression : entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
	public final JPA2Parser.collection_member_expression_return collection_member_expression() throws RecognitionException {
		JPA2Parser.collection_member_expression_return retval = new JPA2Parser.collection_member_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal321=null;
		Token string_literal322=null;
		Token string_literal323=null;
		ParserRuleReturnScope entity_or_value_expression320 =null;
		ParserRuleReturnScope path_expression324 =null;

		Object string_literal321_tree=null;
		Object string_literal322_tree=null;
		Object string_literal323_tree=null;

		try {
			// JPA2.g:317:5: ( entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
			// JPA2.g:317:7: entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_or_value_expression_in_collection_member_expression2883);
			entity_or_value_expression320=entity_or_value_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_or_value_expression320.getTree());

			// JPA2.g:317:35: ( 'NOT' )?
			int alt86=2;
			int LA86_0 = input.LA(1);
			if ( (LA86_0==NOT) ) {
				alt86=1;
			}
			switch (alt86) {
				case 1 :
					// JPA2.g:317:36: 'NOT'
					{
					string_literal321=(Token)match(input,NOT,FOLLOW_NOT_in_collection_member_expression2887); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal321_tree = (Object)adaptor.create(string_literal321);
					adaptor.addChild(root_0, string_literal321_tree);
					}

					}
					break;

			}

			string_literal322=(Token)match(input,105,FOLLOW_105_in_collection_member_expression2891); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal322_tree = (Object)adaptor.create(string_literal322);
			adaptor.addChild(root_0, string_literal322_tree);
			}

			// JPA2.g:317:53: ( 'OF' )?
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==114) ) {
				alt87=1;
			}
			switch (alt87) {
				case 1 :
					// JPA2.g:317:54: 'OF'
					{
					string_literal323=(Token)match(input,114,FOLLOW_114_in_collection_member_expression2894); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal323_tree = (Object)adaptor.create(string_literal323);
					adaptor.addChild(root_0, string_literal323_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_path_expression_in_collection_member_expression2898);
			path_expression324=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression324.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:318:1: entity_or_value_expression : ( path_expression | simple_entity_or_value_expression | subquery );
	public final JPA2Parser.entity_or_value_expression_return entity_or_value_expression() throws RecognitionException {
		JPA2Parser.entity_or_value_expression_return retval = new JPA2Parser.entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression325 =null;
		ParserRuleReturnScope simple_entity_or_value_expression326 =null;
		ParserRuleReturnScope subquery327 =null;


		try {
			// JPA2.g:319:5: ( path_expression | simple_entity_or_value_expression | subquery )
			int alt88=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA88_1 = input.LA(2);
				if ( (LA88_1==60) ) {
					alt88=1;
				}
				else if ( (LA88_1==NOT||LA88_1==105) ) {
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
					// JPA2.g:319:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_or_value_expression2909);
					path_expression325=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression325.getTree());

					}
					break;
				case 2 :
					// JPA2.g:320:7: simple_entity_or_value_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2917);
					simple_entity_or_value_expression326=simple_entity_or_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_or_value_expression326.getTree());

					}
					break;
				case 3 :
					// JPA2.g:321:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_entity_or_value_expression2925);
					subquery327=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery327.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:322:1: simple_entity_or_value_expression : ( identification_variable | input_parameter | literal );
	public final JPA2Parser.simple_entity_or_value_expression_return simple_entity_or_value_expression() throws RecognitionException {
		JPA2Parser.simple_entity_or_value_expression_return retval = new JPA2Parser.simple_entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable328 =null;
		ParserRuleReturnScope input_parameter329 =null;
		ParserRuleReturnScope literal330 =null;


		try {
			// JPA2.g:323:5: ( identification_variable | input_parameter | literal )
			int alt89=3;
			int LA89_0 = input.LA(1);
			if ( (LA89_0==WORD) ) {
				int LA89_1 = input.LA(2);
				if ( (synpred136_JPA2()) ) {
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
					// JPA2.g:323:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_or_value_expression2936);
					identification_variable328=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable328.getTree());

					}
					break;
				case 2 :
					// JPA2.g:324:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_or_value_expression2944);
					input_parameter329=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter329.getTree());

					}
					break;
				case 3 :
					// JPA2.g:325:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_simple_entity_or_value_expression2952);
					literal330=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal330.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:326:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
	public final JPA2Parser.exists_expression_return exists_expression() throws RecognitionException {
		JPA2Parser.exists_expression_return retval = new JPA2Parser.exists_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal331=null;
		Token string_literal332=null;
		ParserRuleReturnScope subquery333 =null;

		Object string_literal331_tree=null;
		Object string_literal332_tree=null;

		try {
			// JPA2.g:327:5: ( ( 'NOT' )? 'EXISTS' subquery )
			// JPA2.g:327:7: ( 'NOT' )? 'EXISTS' subquery
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:327:7: ( 'NOT' )?
			int alt90=2;
			int LA90_0 = input.LA(1);
			if ( (LA90_0==NOT) ) {
				alt90=1;
			}
			switch (alt90) {
				case 1 :
					// JPA2.g:327:8: 'NOT'
					{
					string_literal331=(Token)match(input,NOT,FOLLOW_NOT_in_exists_expression2964); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal331_tree = (Object)adaptor.create(string_literal331);
					adaptor.addChild(root_0, string_literal331_tree);
					}

					}
					break;

			}

			string_literal332=(Token)match(input,94,FOLLOW_94_in_exists_expression2968); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal332_tree = (Object)adaptor.create(string_literal332);
			adaptor.addChild(root_0, string_literal332_tree);
			}

			pushFollow(FOLLOW_subquery_in_exists_expression2970);
			subquery333=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery333.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:328:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
	public final JPA2Parser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
		JPA2Parser.all_or_any_expression_return retval = new JPA2Parser.all_or_any_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set334=null;
		ParserRuleReturnScope subquery335 =null;

		Object set334_tree=null;

		try {
			// JPA2.g:329:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
			// JPA2.g:329:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
			{
			root_0 = (Object)adaptor.nil();


			set334=input.LT(1);
			if ( (input.LA(1) >= 76 && input.LA(1) <= 77)||input.LA(1)==120 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set334));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_subquery_in_all_or_any_expression2994);
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
	// $ANTLR end "all_or_any_expression"


	public static class comparison_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "comparison_expression"
	// JPA2.g:330:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
	public final JPA2Parser.comparison_expression_return comparison_expression() throws RecognitionException {
		JPA2Parser.comparison_expression_return retval = new JPA2Parser.comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set341=null;
		Token set345=null;
		Token set353=null;
		Token set357=null;
		ParserRuleReturnScope string_expression336 =null;
		ParserRuleReturnScope comparison_operator337 =null;
		ParserRuleReturnScope string_expression338 =null;
		ParserRuleReturnScope all_or_any_expression339 =null;
		ParserRuleReturnScope boolean_expression340 =null;
		ParserRuleReturnScope boolean_expression342 =null;
		ParserRuleReturnScope all_or_any_expression343 =null;
		ParserRuleReturnScope enum_expression344 =null;
		ParserRuleReturnScope enum_expression346 =null;
		ParserRuleReturnScope all_or_any_expression347 =null;
		ParserRuleReturnScope datetime_expression348 =null;
		ParserRuleReturnScope comparison_operator349 =null;
		ParserRuleReturnScope datetime_expression350 =null;
		ParserRuleReturnScope all_or_any_expression351 =null;
		ParserRuleReturnScope entity_expression352 =null;
		ParserRuleReturnScope entity_expression354 =null;
		ParserRuleReturnScope all_or_any_expression355 =null;
		ParserRuleReturnScope entity_type_expression356 =null;
		ParserRuleReturnScope entity_type_expression358 =null;
		ParserRuleReturnScope arithmetic_expression359 =null;
		ParserRuleReturnScope comparison_operator360 =null;
		ParserRuleReturnScope arithmetic_expression361 =null;
		ParserRuleReturnScope all_or_any_expression362 =null;

		Object set341_tree=null;
		Object set345_tree=null;
		Object set353_tree=null;
		Object set357_tree=null;

		try {
			// JPA2.g:331:5: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
			int alt97=7;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA97_1 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred145_JPA2()) ) {
					alt97=2;
				}
				else if ( (synpred148_JPA2()) ) {
					alt97=3;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (synpred153_JPA2()) ) {
					alt97=5;
				}
				else if ( (synpred155_JPA2()) ) {
					alt97=6;
				}
				else if ( (true) ) {
					alt97=7;
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
				alt97=1;
				}
				break;
			case 69:
				{
				int LA97_3 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred145_JPA2()) ) {
					alt97=2;
				}
				else if ( (synpred148_JPA2()) ) {
					alt97=3;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (synpred153_JPA2()) ) {
					alt97=5;
				}
				else if ( (synpred155_JPA2()) ) {
					alt97=6;
				}
				else if ( (true) ) {
					alt97=7;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA97_4 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred145_JPA2()) ) {
					alt97=2;
				}
				else if ( (synpred148_JPA2()) ) {
					alt97=3;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (synpred153_JPA2()) ) {
					alt97=5;
				}
				else if ( (synpred155_JPA2()) ) {
					alt97=6;
				}
				else if ( (true) ) {
					alt97=7;
				}

				}
				break;
			case 55:
				{
				int LA97_5 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred145_JPA2()) ) {
					alt97=2;
				}
				else if ( (synpred148_JPA2()) ) {
					alt97=3;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (synpred153_JPA2()) ) {
					alt97=5;
				}
				else if ( (synpred155_JPA2()) ) {
					alt97=6;
				}
				else if ( (true) ) {
					alt97=7;
				}

				}
				break;
			case COUNT:
				{
				int LA97_11 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (true) ) {
					alt97=7;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA97_12 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (true) ) {
					alt97=7;
				}

				}
				break;
			case 96:
				{
				int LA97_13 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred145_JPA2()) ) {
					alt97=2;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (true) ) {
					alt97=7;
				}

				}
				break;
			case 81:
				{
				int LA97_14 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred145_JPA2()) ) {
					alt97=2;
				}
				else if ( (synpred148_JPA2()) ) {
					alt97=3;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (true) ) {
					alt97=7;
				}

				}
				break;
			case 82:
				{
				int LA97_15 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred145_JPA2()) ) {
					alt97=2;
				}
				else if ( (synpred148_JPA2()) ) {
					alt97=3;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (true) ) {
					alt97=7;
				}

				}
				break;
			case 112:
				{
				int LA97_16 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred145_JPA2()) ) {
					alt97=2;
				}
				else if ( (synpred148_JPA2()) ) {
					alt97=3;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (true) ) {
					alt97=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA97_17 = input.LA(2);
				if ( (synpred142_JPA2()) ) {
					alt97=1;
				}
				else if ( (synpred145_JPA2()) ) {
					alt97=2;
				}
				else if ( (synpred148_JPA2()) ) {
					alt97=3;
				}
				else if ( (synpred150_JPA2()) ) {
					alt97=4;
				}
				else if ( (true) ) {
					alt97=7;
				}

				}
				break;
			case 134:
			case 135:
				{
				alt97=2;
				}
				break;
			case 84:
			case 85:
			case 86:
				{
				alt97=4;
				}
				break;
			case 127:
				{
				alt97=6;
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
				alt97=7;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 97, 0, input);
				throw nvae;
			}
			switch (alt97) {
				case 1 :
					// JPA2.g:331:7: string_expression comparison_operator ( string_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_comparison_expression3005);
					string_expression336=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression336.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3007);
					comparison_operator337=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator337.getTree());

					// JPA2.g:331:45: ( string_expression | all_or_any_expression )
					int alt91=2;
					int LA91_0 = input.LA(1);
					if ( (LA91_0==AVG||LA91_0==COUNT||(LA91_0 >= LOWER && LA91_0 <= NAMED_PARAMETER)||(LA91_0 >= STRING_LITERAL && LA91_0 <= SUM)||LA91_0==WORD||LA91_0==55||LA91_0==69||(LA91_0 >= 81 && LA91_0 <= 83)||LA91_0==96||LA91_0==112||LA91_0==122||LA91_0==126||LA91_0==129) ) {
						alt91=1;
					}
					else if ( ((LA91_0 >= 76 && LA91_0 <= 77)||LA91_0==120) ) {
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
							// JPA2.g:331:46: string_expression
							{
							pushFollow(FOLLOW_string_expression_in_comparison_expression3010);
							string_expression338=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression338.getTree());

							}
							break;
						case 2 :
							// JPA2.g:331:66: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3014);
							all_or_any_expression339=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression339.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:332:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_comparison_expression3023);
					boolean_expression340=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression340.getTree());

					set341=input.LT(1);
					if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set341));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:332:39: ( boolean_expression | all_or_any_expression )
					int alt92=2;
					int LA92_0 = input.LA(1);
					if ( (LA92_0==LPAREN||LA92_0==NAMED_PARAMETER||LA92_0==WORD||LA92_0==55||LA92_0==69||(LA92_0 >= 81 && LA92_0 <= 82)||LA92_0==96||LA92_0==112||(LA92_0 >= 134 && LA92_0 <= 135)) ) {
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
							// JPA2.g:332:40: boolean_expression
							{
							pushFollow(FOLLOW_boolean_expression_in_comparison_expression3034);
							boolean_expression342=boolean_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression342.getTree());

							}
							break;
						case 2 :
							// JPA2.g:332:61: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3038);
							all_or_any_expression343=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression343.getTree());

							}
							break;

					}

					}
					break;
				case 3 :
					// JPA2.g:333:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_comparison_expression3047);
					enum_expression344=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression344.getTree());

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
					// JPA2.g:333:34: ( enum_expression | all_or_any_expression )
					int alt93=2;
					int LA93_0 = input.LA(1);
					if ( (LA93_0==LPAREN||LA93_0==NAMED_PARAMETER||LA93_0==WORD||LA93_0==55||LA93_0==69||(LA93_0 >= 81 && LA93_0 <= 82)||LA93_0==112) ) {
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
							// JPA2.g:333:35: enum_expression
							{
							pushFollow(FOLLOW_enum_expression_in_comparison_expression3056);
							enum_expression346=enum_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression346.getTree());

							}
							break;
						case 2 :
							// JPA2.g:333:53: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3060);
							all_or_any_expression347=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression347.getTree());

							}
							break;

					}

					}
					break;
				case 4 :
					// JPA2.g:334:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_comparison_expression3069);
					datetime_expression348=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression348.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3071);
					comparison_operator349=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator349.getTree());

					// JPA2.g:334:47: ( datetime_expression | all_or_any_expression )
					int alt94=2;
					int LA94_0 = input.LA(1);
					if ( (LA94_0==AVG||LA94_0==COUNT||(LA94_0 >= LPAREN && LA94_0 <= NAMED_PARAMETER)||LA94_0==SUM||LA94_0==WORD||LA94_0==55||LA94_0==69||(LA94_0 >= 81 && LA94_0 <= 82)||(LA94_0 >= 84 && LA94_0 <= 86)||LA94_0==96||LA94_0==112) ) {
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
							// JPA2.g:334:48: datetime_expression
							{
							pushFollow(FOLLOW_datetime_expression_in_comparison_expression3074);
							datetime_expression350=datetime_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression350.getTree());

							}
							break;
						case 2 :
							// JPA2.g:334:70: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3078);
							all_or_any_expression351=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression351.getTree());

							}
							break;

					}

					}
					break;
				case 5 :
					// JPA2.g:335:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_expression_in_comparison_expression3087);
					entity_expression352=entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression352.getTree());

					set353=input.LT(1);
					if ( (input.LA(1) >= 65 && input.LA(1) <= 66) ) {
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
					// JPA2.g:335:38: ( entity_expression | all_or_any_expression )
					int alt95=2;
					int LA95_0 = input.LA(1);
					if ( (LA95_0==NAMED_PARAMETER||LA95_0==WORD||LA95_0==55||LA95_0==69) ) {
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
							// JPA2.g:335:39: entity_expression
							{
							pushFollow(FOLLOW_entity_expression_in_comparison_expression3098);
							entity_expression354=entity_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression354.getTree());

							}
							break;
						case 2 :
							// JPA2.g:335:59: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3102);
							all_or_any_expression355=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression355.getTree());

							}
							break;

					}

					}
					break;
				case 6 :
					// JPA2.g:336:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3111);
					entity_type_expression356=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression356.getTree());

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
					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3121);
					entity_type_expression358=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression358.getTree());

					}
					break;
				case 7 :
					// JPA2.g:337:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3129);
					arithmetic_expression359=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression359.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3131);
					comparison_operator360=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator360.getTree());

					// JPA2.g:337:49: ( arithmetic_expression | all_or_any_expression )
					int alt96=2;
					int LA96_0 = input.LA(1);
					if ( (LA96_0==AVG||LA96_0==COUNT||LA96_0==INT_NUMERAL||(LA96_0 >= LPAREN && LA96_0 <= NAMED_PARAMETER)||LA96_0==SUM||LA96_0==WORD||LA96_0==55||LA96_0==57||LA96_0==59||LA96_0==62||LA96_0==69||LA96_0==75||(LA96_0 >= 81 && LA96_0 <= 82)||LA96_0==96||LA96_0==98||LA96_0==102||LA96_0==104||LA96_0==107||LA96_0==112||LA96_0==119||LA96_0==121) ) {
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
							// JPA2.g:337:50: arithmetic_expression
							{
							pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3134);
							arithmetic_expression361=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression361.getTree());

							}
							break;
						case 2 :
							// JPA2.g:337:74: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3138);
							all_or_any_expression362=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression362.getTree());

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
	// JPA2.g:339:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
	public final JPA2Parser.comparison_operator_return comparison_operator() throws RecognitionException {
		JPA2Parser.comparison_operator_return retval = new JPA2Parser.comparison_operator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set363=null;

		Object set363_tree=null;

		try {
			// JPA2.g:340:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set363=input.LT(1);
			if ( (input.LA(1) >= 63 && input.LA(1) <= 68) ) {
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
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:346:1: arithmetic_expression : ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term );
	public final JPA2Parser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
		JPA2Parser.arithmetic_expression_return retval = new JPA2Parser.arithmetic_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set366=null;
		ParserRuleReturnScope arithmetic_term364 =null;
		ParserRuleReturnScope arithmetic_term365 =null;
		ParserRuleReturnScope arithmetic_term367 =null;

		Object set366_tree=null;

		try {
			// JPA2.g:347:5: ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term )
			int alt98=2;
			switch ( input.LA(1) ) {
			case 57:
			case 59:
				{
				int LA98_1 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case WORD:
				{
				int LA98_2 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 62:
				{
				int LA98_3 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA98_4 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case LPAREN:
				{
				int LA98_5 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 69:
				{
				int LA98_6 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA98_7 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 55:
				{
				int LA98_8 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 102:
				{
				int LA98_9 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 104:
				{
				int LA98_10 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 75:
				{
				int LA98_11 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 121:
				{
				int LA98_12 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 107:
				{
				int LA98_13 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 119:
				{
				int LA98_14 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 98:
				{
				int LA98_15 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case COUNT:
				{
				int LA98_16 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA98_17 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 96:
				{
				int LA98_18 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 81:
				{
				int LA98_19 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 82:
				{
				int LA98_20 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

				}
				break;
			case 112:
				{
				int LA98_21 = input.LA(2);
				if ( (synpred162_JPA2()) ) {
					alt98=1;
				}
				else if ( (true) ) {
					alt98=2;
				}

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
					// JPA2.g:347:7: arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3202);
					arithmetic_term364=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term364.getTree());

					}
					break;
				case 2 :
					// JPA2.g:348:7: arithmetic_term ( '+' | '-' ) arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3210);
					arithmetic_term365=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term365.getTree());

					set366=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
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
					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3220);
					arithmetic_term367=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term367.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:349:1: arithmetic_term : ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor );
	public final JPA2Parser.arithmetic_term_return arithmetic_term() throws RecognitionException {
		JPA2Parser.arithmetic_term_return retval = new JPA2Parser.arithmetic_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set370=null;
		ParserRuleReturnScope arithmetic_factor368 =null;
		ParserRuleReturnScope arithmetic_factor369 =null;
		ParserRuleReturnScope arithmetic_factor371 =null;

		Object set370_tree=null;

		try {
			// JPA2.g:350:5: ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor )
			int alt99=2;
			switch ( input.LA(1) ) {
			case 57:
			case 59:
				{
				int LA99_1 = input.LA(2);
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
				if ( (synpred164_JPA2()) ) {
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
					// JPA2.g:350:7: arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3231);
					arithmetic_factor368=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor368.getTree());

					}
					break;
				case 2 :
					// JPA2.g:351:7: arithmetic_factor ( '*' | '/' ) arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3239);
					arithmetic_factor369=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor369.getTree());

					set370=input.LT(1);
					if ( input.LA(1)==56||input.LA(1)==61 ) {
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
					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3250);
					arithmetic_factor371=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor371.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:352:1: arithmetic_factor : ( ( '+' | '-' ) )? arithmetic_primary ;
	public final JPA2Parser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
		JPA2Parser.arithmetic_factor_return retval = new JPA2Parser.arithmetic_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set372=null;
		ParserRuleReturnScope arithmetic_primary373 =null;

		Object set372_tree=null;

		try {
			// JPA2.g:353:5: ( ( ( '+' | '-' ) )? arithmetic_primary )
			// JPA2.g:353:7: ( ( '+' | '-' ) )? arithmetic_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:353:7: ( ( '+' | '-' ) )?
			int alt100=2;
			int LA100_0 = input.LA(1);
			if ( (LA100_0==57||LA100_0==59) ) {
				alt100=1;
			}
			switch (alt100) {
				case 1 :
					// JPA2.g:
					{
					set372=input.LT(1);
					if ( input.LA(1)==57||input.LA(1)==59 ) {
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
					break;

			}

			pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor3273);
			arithmetic_primary373=arithmetic_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary373.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:354:1: arithmetic_primary : ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | subquery );
	public final JPA2Parser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
		JPA2Parser.arithmetic_primary_return retval = new JPA2Parser.arithmetic_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal376=null;
		Token char_literal378=null;
		ParserRuleReturnScope path_expression374 =null;
		ParserRuleReturnScope numeric_literal375 =null;
		ParserRuleReturnScope arithmetic_expression377 =null;
		ParserRuleReturnScope input_parameter379 =null;
		ParserRuleReturnScope functions_returning_numerics380 =null;
		ParserRuleReturnScope aggregate_expression381 =null;
		ParserRuleReturnScope case_expression382 =null;
		ParserRuleReturnScope function_invocation383 =null;
		ParserRuleReturnScope subquery384 =null;

		Object char_literal376_tree=null;
		Object char_literal378_tree=null;

		try {
			// JPA2.g:355:5: ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | subquery )
			int alt101=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt101=1;
				}
				break;
			case INT_NUMERAL:
			case 62:
				{
				alt101=2;
				}
				break;
			case LPAREN:
				{
				int LA101_4 = input.LA(2);
				if ( (synpred170_JPA2()) ) {
					alt101=3;
				}
				else if ( (true) ) {
					alt101=9;
				}

				}
				break;
			case NAMED_PARAMETER:
			case 55:
			case 69:
				{
				alt101=4;
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
				alt101=5;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt101=6;
				}
				break;
			case 96:
				{
				int LA101_17 = input.LA(2);
				if ( (synpred173_JPA2()) ) {
					alt101=6;
				}
				else if ( (synpred175_JPA2()) ) {
					alt101=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 101, 17, input);
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
				alt101=7;
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
					// JPA2.g:355:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_arithmetic_primary3284);
					path_expression374=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression374.getTree());

					}
					break;
				case 2 :
					// JPA2.g:356:7: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary3292);
					numeric_literal375=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal375.getTree());

					}
					break;
				case 3 :
					// JPA2.g:357:7: '(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal376=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary3300); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal376_tree = (Object)adaptor.create(char_literal376);
					adaptor.addChild(root_0, char_literal376_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_arithmetic_primary3301);
					arithmetic_expression377=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression377.getTree());

					char_literal378=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary3302); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal378_tree = (Object)adaptor.create(char_literal378);
					adaptor.addChild(root_0, char_literal378_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:358:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_arithmetic_primary3310);
					input_parameter379=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter379.getTree());

					}
					break;
				case 5 :
					// JPA2.g:359:7: functions_returning_numerics
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary3318);
					functions_returning_numerics380=functions_returning_numerics();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics380.getTree());

					}
					break;
				case 6 :
					// JPA2.g:360:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary3326);
					aggregate_expression381=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression381.getTree());

					}
					break;
				case 7 :
					// JPA2.g:361:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_arithmetic_primary3334);
					case_expression382=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression382.getTree());

					}
					break;
				case 8 :
					// JPA2.g:362:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_arithmetic_primary3342);
					function_invocation383=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation383.getTree());

					}
					break;
				case 9 :
					// JPA2.g:363:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_arithmetic_primary3350);
					subquery384=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery384.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:364:1: string_expression : ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | subquery );
	public final JPA2Parser.string_expression_return string_expression() throws RecognitionException {
		JPA2Parser.string_expression_return retval = new JPA2Parser.string_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression385 =null;
		ParserRuleReturnScope string_literal386 =null;
		ParserRuleReturnScope input_parameter387 =null;
		ParserRuleReturnScope functions_returning_strings388 =null;
		ParserRuleReturnScope aggregate_expression389 =null;
		ParserRuleReturnScope case_expression390 =null;
		ParserRuleReturnScope function_invocation391 =null;
		ParserRuleReturnScope subquery392 =null;


		try {
			// JPA2.g:365:5: ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | subquery )
			int alt102=8;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt102=1;
				}
				break;
			case STRING_LITERAL:
				{
				alt102=2;
				}
				break;
			case NAMED_PARAMETER:
			case 55:
			case 69:
				{
				alt102=3;
				}
				break;
			case LOWER:
			case 83:
			case 122:
			case 126:
			case 129:
				{
				alt102=4;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt102=5;
				}
				break;
			case 96:
				{
				int LA102_13 = input.LA(2);
				if ( (synpred180_JPA2()) ) {
					alt102=5;
				}
				else if ( (synpred182_JPA2()) ) {
					alt102=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 102, 13, input);
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
				alt102=6;
				}
				break;
			case LPAREN:
				{
				alt102=8;
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
					// JPA2.g:365:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_string_expression3361);
					path_expression385=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression385.getTree());

					}
					break;
				case 2 :
					// JPA2.g:366:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_string_expression3369);
					string_literal386=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal386.getTree());

					}
					break;
				case 3 :
					// JPA2.g:367:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_string_expression3377);
					input_parameter387=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter387.getTree());

					}
					break;
				case 4 :
					// JPA2.g:368:7: functions_returning_strings
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_strings_in_string_expression3385);
					functions_returning_strings388=functions_returning_strings();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings388.getTree());

					}
					break;
				case 5 :
					// JPA2.g:369:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_string_expression3393);
					aggregate_expression389=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression389.getTree());

					}
					break;
				case 6 :
					// JPA2.g:370:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_string_expression3401);
					case_expression390=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression390.getTree());

					}
					break;
				case 7 :
					// JPA2.g:371:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_string_expression3409);
					function_invocation391=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation391.getTree());

					}
					break;
				case 8 :
					// JPA2.g:372:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_string_expression3417);
					subquery392=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery392.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:373:1: datetime_expression : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | date_time_timestamp_literal | subquery );
	public final JPA2Parser.datetime_expression_return datetime_expression() throws RecognitionException {
		JPA2Parser.datetime_expression_return retval = new JPA2Parser.datetime_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression393 =null;
		ParserRuleReturnScope input_parameter394 =null;
		ParserRuleReturnScope functions_returning_datetime395 =null;
		ParserRuleReturnScope aggregate_expression396 =null;
		ParserRuleReturnScope case_expression397 =null;
		ParserRuleReturnScope function_invocation398 =null;
		ParserRuleReturnScope date_time_timestamp_literal399 =null;
		ParserRuleReturnScope subquery400 =null;


		try {
			// JPA2.g:374:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | date_time_timestamp_literal | subquery )
			int alt103=8;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA103_1 = input.LA(2);
				if ( (synpred183_JPA2()) ) {
					alt103=1;
				}
				else if ( (synpred189_JPA2()) ) {
					alt103=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 103, 1, input);
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
				alt103=2;
				}
				break;
			case 84:
			case 85:
			case 86:
				{
				alt103=3;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt103=4;
				}
				break;
			case 96:
				{
				int LA103_8 = input.LA(2);
				if ( (synpred186_JPA2()) ) {
					alt103=4;
				}
				else if ( (synpred188_JPA2()) ) {
					alt103=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 103, 8, input);
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
				alt103=5;
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
					// JPA2.g:374:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_datetime_expression3428);
					path_expression393=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression393.getTree());

					}
					break;
				case 2 :
					// JPA2.g:375:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_datetime_expression3436);
					input_parameter394=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter394.getTree());

					}
					break;
				case 3 :
					// JPA2.g:376:7: functions_returning_datetime
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_datetime_in_datetime_expression3444);
					functions_returning_datetime395=functions_returning_datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime395.getTree());

					}
					break;
				case 4 :
					// JPA2.g:377:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_datetime_expression3452);
					aggregate_expression396=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression396.getTree());

					}
					break;
				case 5 :
					// JPA2.g:378:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_datetime_expression3460);
					case_expression397=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression397.getTree());

					}
					break;
				case 6 :
					// JPA2.g:379:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_datetime_expression3468);
					function_invocation398=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation398.getTree());

					}
					break;
				case 7 :
					// JPA2.g:380:7: date_time_timestamp_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_time_timestamp_literal_in_datetime_expression3476);
					date_time_timestamp_literal399=date_time_timestamp_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_time_timestamp_literal399.getTree());

					}
					break;
				case 8 :
					// JPA2.g:381:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_datetime_expression3484);
					subquery400=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery400.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:382:1: boolean_expression : ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | subquery );
	public final JPA2Parser.boolean_expression_return boolean_expression() throws RecognitionException {
		JPA2Parser.boolean_expression_return retval = new JPA2Parser.boolean_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression401 =null;
		ParserRuleReturnScope boolean_literal402 =null;
		ParserRuleReturnScope input_parameter403 =null;
		ParserRuleReturnScope case_expression404 =null;
		ParserRuleReturnScope function_invocation405 =null;
		ParserRuleReturnScope subquery406 =null;


		try {
			// JPA2.g:383:5: ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | subquery )
			int alt104=6;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt104=1;
				}
				break;
			case 134:
			case 135:
				{
				alt104=2;
				}
				break;
			case NAMED_PARAMETER:
			case 55:
			case 69:
				{
				alt104=3;
				}
				break;
			case 81:
			case 82:
			case 112:
				{
				alt104=4;
				}
				break;
			case 96:
				{
				alt104=5;
				}
				break;
			case LPAREN:
				{
				alt104=6;
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
					// JPA2.g:383:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_boolean_expression3495);
					path_expression401=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression401.getTree());

					}
					break;
				case 2 :
					// JPA2.g:384:7: boolean_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_literal_in_boolean_expression3503);
					boolean_literal402=boolean_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal402.getTree());

					}
					break;
				case 3 :
					// JPA2.g:385:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_boolean_expression3511);
					input_parameter403=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter403.getTree());

					}
					break;
				case 4 :
					// JPA2.g:386:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_boolean_expression3519);
					case_expression404=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression404.getTree());

					}
					break;
				case 5 :
					// JPA2.g:387:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_boolean_expression3527);
					function_invocation405=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation405.getTree());

					}
					break;
				case 6 :
					// JPA2.g:388:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_boolean_expression3535);
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
	// $ANTLR end "boolean_expression"


	public static class enum_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enum_expression"
	// JPA2.g:389:1: enum_expression : ( path_expression | enum_literal | input_parameter | case_expression | subquery );
	public final JPA2Parser.enum_expression_return enum_expression() throws RecognitionException {
		JPA2Parser.enum_expression_return retval = new JPA2Parser.enum_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression407 =null;
		ParserRuleReturnScope enum_literal408 =null;
		ParserRuleReturnScope input_parameter409 =null;
		ParserRuleReturnScope case_expression410 =null;
		ParserRuleReturnScope subquery411 =null;


		try {
			// JPA2.g:390:5: ( path_expression | enum_literal | input_parameter | case_expression | subquery )
			int alt105=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA105_1 = input.LA(2);
				if ( (LA105_1==60) ) {
					alt105=1;
				}
				else if ( (LA105_1==EOF||LA105_1==AND||(LA105_1 >= GROUP && LA105_1 <= HAVING)||LA105_1==INNER||(LA105_1 >= JOIN && LA105_1 <= LEFT)||(LA105_1 >= OR && LA105_1 <= ORDER)||LA105_1==RPAREN||LA105_1==WORD||LA105_1==58||(LA105_1 >= 65 && LA105_1 <= 66)||LA105_1==78||LA105_1==89||LA105_1==91||LA105_1==95||LA105_1==118||LA105_1==123||(LA105_1 >= 131 && LA105_1 <= 132)) ) {
					alt105=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 105, 1, input);
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
			case LPAREN:
				{
				alt105=5;
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
					// JPA2.g:390:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_enum_expression3546);
					path_expression407=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression407.getTree());

					}
					break;
				case 2 :
					// JPA2.g:391:7: enum_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_literal_in_enum_expression3554);
					enum_literal408=enum_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal408.getTree());

					}
					break;
				case 3 :
					// JPA2.g:392:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_enum_expression3562);
					input_parameter409=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter409.getTree());

					}
					break;
				case 4 :
					// JPA2.g:393:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_enum_expression3570);
					case_expression410=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression410.getTree());

					}
					break;
				case 5 :
					// JPA2.g:394:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_enum_expression3578);
					subquery411=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery411.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:395:1: entity_expression : ( path_expression | simple_entity_expression );
	public final JPA2Parser.entity_expression_return entity_expression() throws RecognitionException {
		JPA2Parser.entity_expression_return retval = new JPA2Parser.entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression412 =null;
		ParserRuleReturnScope simple_entity_expression413 =null;


		try {
			// JPA2.g:396:5: ( path_expression | simple_entity_expression )
			int alt106=2;
			int LA106_0 = input.LA(1);
			if ( (LA106_0==WORD) ) {
				int LA106_1 = input.LA(2);
				if ( (LA106_1==60) ) {
					alt106=1;
				}
				else if ( (LA106_1==EOF||LA106_1==AND||(LA106_1 >= GROUP && LA106_1 <= HAVING)||LA106_1==INNER||(LA106_1 >= JOIN && LA106_1 <= LEFT)||(LA106_1 >= OR && LA106_1 <= ORDER)||LA106_1==RPAREN||LA106_1==58||(LA106_1 >= 65 && LA106_1 <= 66)||LA106_1==118||LA106_1==123||LA106_1==132) ) {
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
			else if ( (LA106_0==NAMED_PARAMETER||LA106_0==55||LA106_0==69) ) {
				alt106=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 106, 0, input);
				throw nvae;
			}

			switch (alt106) {
				case 1 :
					// JPA2.g:396:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_expression3589);
					path_expression412=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression412.getTree());

					}
					break;
				case 2 :
					// JPA2.g:397:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3597);
					simple_entity_expression413=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression413.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:398:1: simple_entity_expression : ( identification_variable | input_parameter );
	public final JPA2Parser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
		JPA2Parser.simple_entity_expression_return retval = new JPA2Parser.simple_entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable414 =null;
		ParserRuleReturnScope input_parameter415 =null;


		try {
			// JPA2.g:399:5: ( identification_variable | input_parameter )
			int alt107=2;
			int LA107_0 = input.LA(1);
			if ( (LA107_0==WORD) ) {
				alt107=1;
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
					// JPA2.g:399:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3608);
					identification_variable414=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable414.getTree());

					}
					break;
				case 2 :
					// JPA2.g:400:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3616);
					input_parameter415=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter415.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:401:1: entity_type_expression : ( type_discriminator | entity_type_literal | input_parameter );
	public final JPA2Parser.entity_type_expression_return entity_type_expression() throws RecognitionException {
		JPA2Parser.entity_type_expression_return retval = new JPA2Parser.entity_type_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope type_discriminator416 =null;
		ParserRuleReturnScope entity_type_literal417 =null;
		ParserRuleReturnScope input_parameter418 =null;


		try {
			// JPA2.g:402:5: ( type_discriminator | entity_type_literal | input_parameter )
			int alt108=3;
			switch ( input.LA(1) ) {
			case 127:
				{
				alt108=1;
				}
				break;
			case WORD:
				{
				alt108=2;
				}
				break;
			case NAMED_PARAMETER:
			case 55:
			case 69:
				{
				alt108=3;
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
					// JPA2.g:402:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_entity_type_expression3627);
					type_discriminator416=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator416.getTree());

					}
					break;
				case 2 :
					// JPA2.g:403:7: entity_type_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_literal_in_entity_type_expression3635);
					entity_type_literal417=entity_type_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_literal417.getTree());

					}
					break;
				case 3 :
					// JPA2.g:404:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_entity_type_expression3643);
					input_parameter418=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter418.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:405:1: type_discriminator : 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' ;
	public final JPA2Parser.type_discriminator_return type_discriminator() throws RecognitionException {
		JPA2Parser.type_discriminator_return retval = new JPA2Parser.type_discriminator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal419=null;
		Token char_literal423=null;
		ParserRuleReturnScope general_identification_variable420 =null;
		ParserRuleReturnScope path_expression421 =null;
		ParserRuleReturnScope input_parameter422 =null;

		Object string_literal419_tree=null;
		Object char_literal423_tree=null;

		try {
			// JPA2.g:406:5: ( 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' )
			// JPA2.g:406:7: 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal419=(Token)match(input,127,FOLLOW_127_in_type_discriminator3654); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal419_tree = (Object)adaptor.create(string_literal419);
			adaptor.addChild(root_0, string_literal419_tree);
			}

			// JPA2.g:406:15: ( general_identification_variable | path_expression | input_parameter )
			int alt109=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA109_1 = input.LA(2);
				if ( (LA109_1==RPAREN) ) {
					alt109=1;
				}
				else if ( (LA109_1==60) ) {
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
				break;
			case 100:
			case 130:
				{
				alt109=1;
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
					// JPA2.g:406:16: general_identification_variable
					{
					pushFollow(FOLLOW_general_identification_variable_in_type_discriminator3657);
					general_identification_variable420=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable420.getTree());

					}
					break;
				case 2 :
					// JPA2.g:406:50: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_type_discriminator3661);
					path_expression421=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression421.getTree());

					}
					break;
				case 3 :
					// JPA2.g:406:68: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_type_discriminator3665);
					input_parameter422=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter422.getTree());

					}
					break;

			}

			char_literal423=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_type_discriminator3668); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal423_tree = (Object)adaptor.create(char_literal423);
			adaptor.addChild(root_0, char_literal423_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:407:1: functions_returning_numerics : ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' );
	public final JPA2Parser.functions_returning_numerics_return functions_returning_numerics() throws RecognitionException {
		JPA2Parser.functions_returning_numerics_return retval = new JPA2Parser.functions_returning_numerics_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal424=null;
		Token char_literal426=null;
		Token string_literal427=null;
		Token char_literal429=null;
		Token char_literal431=null;
		Token char_literal433=null;
		Token string_literal434=null;
		Token char_literal436=null;
		Token string_literal437=null;
		Token char_literal439=null;
		Token string_literal440=null;
		Token char_literal442=null;
		Token char_literal444=null;
		Token string_literal445=null;
		Token char_literal447=null;
		Token string_literal448=null;
		Token char_literal450=null;
		ParserRuleReturnScope string_expression425 =null;
		ParserRuleReturnScope string_expression428 =null;
		ParserRuleReturnScope string_expression430 =null;
		ParserRuleReturnScope arithmetic_expression432 =null;
		ParserRuleReturnScope arithmetic_expression435 =null;
		ParserRuleReturnScope arithmetic_expression438 =null;
		ParserRuleReturnScope arithmetic_expression441 =null;
		ParserRuleReturnScope arithmetic_expression443 =null;
		ParserRuleReturnScope path_expression446 =null;
		ParserRuleReturnScope identification_variable449 =null;

		Object string_literal424_tree=null;
		Object char_literal426_tree=null;
		Object string_literal427_tree=null;
		Object char_literal429_tree=null;
		Object char_literal431_tree=null;
		Object char_literal433_tree=null;
		Object string_literal434_tree=null;
		Object char_literal436_tree=null;
		Object string_literal437_tree=null;
		Object char_literal439_tree=null;
		Object string_literal440_tree=null;
		Object char_literal442_tree=null;
		Object char_literal444_tree=null;
		Object string_literal445_tree=null;
		Object char_literal447_tree=null;
		Object string_literal448_tree=null;
		Object char_literal450_tree=null;

		try {
			// JPA2.g:408:5: ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' )
			int alt111=7;
			switch ( input.LA(1) ) {
			case 102:
				{
				alt111=1;
				}
				break;
			case 104:
				{
				alt111=2;
				}
				break;
			case 75:
				{
				alt111=3;
				}
				break;
			case 121:
				{
				alt111=4;
				}
				break;
			case 107:
				{
				alt111=5;
				}
				break;
			case 119:
				{
				alt111=6;
				}
				break;
			case 98:
				{
				alt111=7;
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
					// JPA2.g:408:7: 'LENGTH(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal424=(Token)match(input,102,FOLLOW_102_in_functions_returning_numerics3679); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal424_tree = (Object)adaptor.create(string_literal424);
					adaptor.addChild(root_0, string_literal424_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3680);
					string_expression425=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression425.getTree());

					char_literal426=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3681); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal426_tree = (Object)adaptor.create(char_literal426);
					adaptor.addChild(root_0, char_literal426_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:409:7: 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal427=(Token)match(input,104,FOLLOW_104_in_functions_returning_numerics3689); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal427_tree = (Object)adaptor.create(string_literal427);
					adaptor.addChild(root_0, string_literal427_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3691);
					string_expression428=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression428.getTree());

					char_literal429=(Token)match(input,58,FOLLOW_58_in_functions_returning_numerics3692); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal429_tree = (Object)adaptor.create(char_literal429);
					adaptor.addChild(root_0, char_literal429_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3694);
					string_expression430=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression430.getTree());

					// JPA2.g:409:55: ( ',' arithmetic_expression )?
					int alt110=2;
					int LA110_0 = input.LA(1);
					if ( (LA110_0==58) ) {
						alt110=1;
					}
					switch (alt110) {
						case 1 :
							// JPA2.g:409:56: ',' arithmetic_expression
							{
							char_literal431=(Token)match(input,58,FOLLOW_58_in_functions_returning_numerics3696); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal431_tree = (Object)adaptor.create(char_literal431);
							adaptor.addChild(root_0, char_literal431_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3697);
							arithmetic_expression432=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression432.getTree());

							}
							break;

					}

					char_literal433=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3700); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal433_tree = (Object)adaptor.create(char_literal433);
					adaptor.addChild(root_0, char_literal433_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:410:7: 'ABS(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal434=(Token)match(input,75,FOLLOW_75_in_functions_returning_numerics3708); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal434_tree = (Object)adaptor.create(string_literal434);
					adaptor.addChild(root_0, string_literal434_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3709);
					arithmetic_expression435=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression435.getTree());

					char_literal436=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3710); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal436_tree = (Object)adaptor.create(char_literal436);
					adaptor.addChild(root_0, char_literal436_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:411:7: 'SQRT(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal437=(Token)match(input,121,FOLLOW_121_in_functions_returning_numerics3718); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal437_tree = (Object)adaptor.create(string_literal437);
					adaptor.addChild(root_0, string_literal437_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3719);
					arithmetic_expression438=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression438.getTree());

					char_literal439=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3720); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal439_tree = (Object)adaptor.create(char_literal439);
					adaptor.addChild(root_0, char_literal439_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:412:7: 'MOD(' arithmetic_expression ',' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal440=(Token)match(input,107,FOLLOW_107_in_functions_returning_numerics3728); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal440_tree = (Object)adaptor.create(string_literal440);
					adaptor.addChild(root_0, string_literal440_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3729);
					arithmetic_expression441=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression441.getTree());

					char_literal442=(Token)match(input,58,FOLLOW_58_in_functions_returning_numerics3730); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal442_tree = (Object)adaptor.create(char_literal442);
					adaptor.addChild(root_0, char_literal442_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3732);
					arithmetic_expression443=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression443.getTree());

					char_literal444=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3733); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal444_tree = (Object)adaptor.create(char_literal444);
					adaptor.addChild(root_0, char_literal444_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:413:7: 'SIZE(' path_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal445=(Token)match(input,119,FOLLOW_119_in_functions_returning_numerics3741); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal445_tree = (Object)adaptor.create(string_literal445);
					adaptor.addChild(root_0, string_literal445_tree);
					}

					pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3742);
					path_expression446=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression446.getTree());

					char_literal447=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3743); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal447_tree = (Object)adaptor.create(char_literal447);
					adaptor.addChild(root_0, char_literal447_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:414:7: 'INDEX(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal448=(Token)match(input,98,FOLLOW_98_in_functions_returning_numerics3751); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal448_tree = (Object)adaptor.create(string_literal448);
					adaptor.addChild(root_0, string_literal448_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_functions_returning_numerics3752);
					identification_variable449=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable449.getTree());

					char_literal450=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3753); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal450_tree = (Object)adaptor.create(char_literal450);
					adaptor.addChild(root_0, char_literal450_tree);
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
	// JPA2.g:415:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
	public final JPA2Parser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
		JPA2Parser.functions_returning_datetime_return retval = new JPA2Parser.functions_returning_datetime_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set451=null;

		Object set451_tree=null;

		try {
			// JPA2.g:416:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set451=input.LT(1);
			if ( (input.LA(1) >= 84 && input.LA(1) <= 86) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set451));
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
	// JPA2.g:419:1: functions_returning_strings : ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' );
	public final JPA2Parser.functions_returning_strings_return functions_returning_strings() throws RecognitionException {
		JPA2Parser.functions_returning_strings_return retval = new JPA2Parser.functions_returning_strings_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal452=null;
		Token char_literal454=null;
		Token char_literal456=null;
		Token char_literal458=null;
		Token string_literal459=null;
		Token char_literal461=null;
		Token char_literal463=null;
		Token char_literal465=null;
		Token string_literal466=null;
		Token string_literal469=null;
		Token char_literal471=null;
		Token string_literal472=null;
		Token char_literal473=null;
		Token char_literal475=null;
		Token string_literal476=null;
		Token char_literal478=null;
		ParserRuleReturnScope string_expression453 =null;
		ParserRuleReturnScope string_expression455 =null;
		ParserRuleReturnScope string_expression457 =null;
		ParserRuleReturnScope string_expression460 =null;
		ParserRuleReturnScope arithmetic_expression462 =null;
		ParserRuleReturnScope arithmetic_expression464 =null;
		ParserRuleReturnScope trim_specification467 =null;
		ParserRuleReturnScope trim_character468 =null;
		ParserRuleReturnScope string_expression470 =null;
		ParserRuleReturnScope string_expression474 =null;
		ParserRuleReturnScope string_expression477 =null;

		Object string_literal452_tree=null;
		Object char_literal454_tree=null;
		Object char_literal456_tree=null;
		Object char_literal458_tree=null;
		Object string_literal459_tree=null;
		Object char_literal461_tree=null;
		Object char_literal463_tree=null;
		Object char_literal465_tree=null;
		Object string_literal466_tree=null;
		Object string_literal469_tree=null;
		Object char_literal471_tree=null;
		Object string_literal472_tree=null;
		Object char_literal473_tree=null;
		Object char_literal475_tree=null;
		Object string_literal476_tree=null;
		Object char_literal478_tree=null;

		try {
			// JPA2.g:420:5: ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' )
			int alt117=5;
			switch ( input.LA(1) ) {
			case 83:
				{
				alt117=1;
				}
				break;
			case 122:
				{
				alt117=2;
				}
				break;
			case 126:
				{
				alt117=3;
				}
				break;
			case LOWER:
				{
				alt117=4;
				}
				break;
			case 129:
				{
				alt117=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 117, 0, input);
				throw nvae;
			}
			switch (alt117) {
				case 1 :
					// JPA2.g:420:7: 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal452=(Token)match(input,83,FOLLOW_83_in_functions_returning_strings3791); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal452_tree = (Object)adaptor.create(string_literal452);
					adaptor.addChild(root_0, string_literal452_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3792);
					string_expression453=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression453.getTree());

					char_literal454=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3793); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal454_tree = (Object)adaptor.create(char_literal454);
					adaptor.addChild(root_0, char_literal454_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3795);
					string_expression455=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression455.getTree());

					// JPA2.g:420:55: ( ',' string_expression )*
					loop112:
					while (true) {
						int alt112=2;
						int LA112_0 = input.LA(1);
						if ( (LA112_0==58) ) {
							alt112=1;
						}

						switch (alt112) {
						case 1 :
							// JPA2.g:420:56: ',' string_expression
							{
							char_literal456=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3798); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal456_tree = (Object)adaptor.create(char_literal456);
							adaptor.addChild(root_0, char_literal456_tree);
							}

							pushFollow(FOLLOW_string_expression_in_functions_returning_strings3800);
							string_expression457=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression457.getTree());

							}
							break;

						default :
							break loop112;
						}
					}

					char_literal458=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3803); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal458_tree = (Object)adaptor.create(char_literal458);
					adaptor.addChild(root_0, char_literal458_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:421:7: 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal459=(Token)match(input,122,FOLLOW_122_in_functions_returning_strings3811); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal459_tree = (Object)adaptor.create(string_literal459);
					adaptor.addChild(root_0, string_literal459_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3813);
					string_expression460=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression460.getTree());

					char_literal461=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3814); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal461_tree = (Object)adaptor.create(char_literal461);
					adaptor.addChild(root_0, char_literal461_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3816);
					arithmetic_expression462=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression462.getTree());

					// JPA2.g:421:63: ( ',' arithmetic_expression )?
					int alt113=2;
					int LA113_0 = input.LA(1);
					if ( (LA113_0==58) ) {
						alt113=1;
					}
					switch (alt113) {
						case 1 :
							// JPA2.g:421:64: ',' arithmetic_expression
							{
							char_literal463=(Token)match(input,58,FOLLOW_58_in_functions_returning_strings3819); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal463_tree = (Object)adaptor.create(char_literal463);
							adaptor.addChild(root_0, char_literal463_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3821);
							arithmetic_expression464=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression464.getTree());

							}
							break;

					}

					char_literal465=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3824); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal465_tree = (Object)adaptor.create(char_literal465);
					adaptor.addChild(root_0, char_literal465_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:422:7: 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal466=(Token)match(input,126,FOLLOW_126_in_functions_returning_strings3832); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal466_tree = (Object)adaptor.create(string_literal466);
					adaptor.addChild(root_0, string_literal466_tree);
					}

					// JPA2.g:422:14: ( ( trim_specification )? ( trim_character )? 'FROM' )?
					int alt116=2;
					int LA116_0 = input.LA(1);
					if ( (LA116_0==TRIM_CHARACTER||LA116_0==80||LA116_0==95||LA116_0==101||LA116_0==124) ) {
						alt116=1;
					}
					switch (alt116) {
						case 1 :
							// JPA2.g:422:15: ( trim_specification )? ( trim_character )? 'FROM'
							{
							// JPA2.g:422:15: ( trim_specification )?
							int alt114=2;
							int LA114_0 = input.LA(1);
							if ( (LA114_0==80||LA114_0==101||LA114_0==124) ) {
								alt114=1;
							}
							switch (alt114) {
								case 1 :
									// JPA2.g:422:16: trim_specification
									{
									pushFollow(FOLLOW_trim_specification_in_functions_returning_strings3835);
									trim_specification467=trim_specification();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification467.getTree());

									}
									break;

							}

							// JPA2.g:422:37: ( trim_character )?
							int alt115=2;
							int LA115_0 = input.LA(1);
							if ( (LA115_0==TRIM_CHARACTER) ) {
								alt115=1;
							}
							switch (alt115) {
								case 1 :
									// JPA2.g:422:38: trim_character
									{
									pushFollow(FOLLOW_trim_character_in_functions_returning_strings3840);
									trim_character468=trim_character();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_character468.getTree());

									}
									break;

							}

							string_literal469=(Token)match(input,95,FOLLOW_95_in_functions_returning_strings3844); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal469_tree = (Object)adaptor.create(string_literal469);
							adaptor.addChild(root_0, string_literal469_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3848);
					string_expression470=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression470.getTree());

					char_literal471=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3850); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal471_tree = (Object)adaptor.create(char_literal471);
					adaptor.addChild(root_0, char_literal471_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:423:7: 'LOWER' '(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal472=(Token)match(input,LOWER,FOLLOW_LOWER_in_functions_returning_strings3858); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal472_tree = (Object)adaptor.create(string_literal472);
					adaptor.addChild(root_0, string_literal472_tree);
					}

					char_literal473=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3860); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal473_tree = (Object)adaptor.create(char_literal473);
					adaptor.addChild(root_0, char_literal473_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3861);
					string_expression474=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression474.getTree());

					char_literal475=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3862); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal475_tree = (Object)adaptor.create(char_literal475);
					adaptor.addChild(root_0, char_literal475_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:424:7: 'UPPER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal476=(Token)match(input,129,FOLLOW_129_in_functions_returning_strings3870); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal476_tree = (Object)adaptor.create(string_literal476);
					adaptor.addChild(root_0, string_literal476_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3871);
					string_expression477=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression477.getTree());

					char_literal478=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3872); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal478_tree = (Object)adaptor.create(char_literal478);
					adaptor.addChild(root_0, char_literal478_tree);
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
	// JPA2.g:425:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
	public final JPA2Parser.trim_specification_return trim_specification() throws RecognitionException {
		JPA2Parser.trim_specification_return retval = new JPA2Parser.trim_specification_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set479=null;

		Object set479_tree=null;

		try {
			// JPA2.g:426:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set479=input.LT(1);
			if ( input.LA(1)==80||input.LA(1)==101||input.LA(1)==124 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set479));
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
	// JPA2.g:427:1: function_invocation : 'FUNCTION(' function_name ( ',' function_arg )* ')' ;
	public final JPA2Parser.function_invocation_return function_invocation() throws RecognitionException {
		JPA2Parser.function_invocation_return retval = new JPA2Parser.function_invocation_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal480=null;
		Token char_literal482=null;
		Token char_literal484=null;
		ParserRuleReturnScope function_name481 =null;
		ParserRuleReturnScope function_arg483 =null;

		Object string_literal480_tree=null;
		Object char_literal482_tree=null;
		Object char_literal484_tree=null;

		try {
			// JPA2.g:428:5: ( 'FUNCTION(' function_name ( ',' function_arg )* ')' )
			// JPA2.g:428:7: 'FUNCTION(' function_name ( ',' function_arg )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal480=(Token)match(input,96,FOLLOW_96_in_function_invocation3902); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal480_tree = (Object)adaptor.create(string_literal480);
			adaptor.addChild(root_0, string_literal480_tree);
			}

			pushFollow(FOLLOW_function_name_in_function_invocation3903);
			function_name481=function_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_name481.getTree());

			// JPA2.g:428:32: ( ',' function_arg )*
			loop118:
			while (true) {
				int alt118=2;
				int LA118_0 = input.LA(1);
				if ( (LA118_0==58) ) {
					alt118=1;
				}

				switch (alt118) {
				case 1 :
					// JPA2.g:428:33: ',' function_arg
					{
					char_literal482=(Token)match(input,58,FOLLOW_58_in_function_invocation3906); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal482_tree = (Object)adaptor.create(char_literal482);
					adaptor.addChild(root_0, char_literal482_tree);
					}

					pushFollow(FOLLOW_function_arg_in_function_invocation3908);
					function_arg483=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg483.getTree());

					}
					break;

				default :
					break loop118;
				}
			}

			char_literal484=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function_invocation3912); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal484_tree = (Object)adaptor.create(char_literal484);
			adaptor.addChild(root_0, char_literal484_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:429:1: function_arg : ( literal | path_expression | input_parameter | scalar_expression );
	public final JPA2Parser.function_arg_return function_arg() throws RecognitionException {
		JPA2Parser.function_arg_return retval = new JPA2Parser.function_arg_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal485 =null;
		ParserRuleReturnScope path_expression486 =null;
		ParserRuleReturnScope input_parameter487 =null;
		ParserRuleReturnScope scalar_expression488 =null;


		try {
			// JPA2.g:430:5: ( literal | path_expression | input_parameter | scalar_expression )
			int alt119=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA119_1 = input.LA(2);
				if ( (LA119_1==60) ) {
					alt119=2;
				}
				else if ( (synpred226_JPA2()) ) {
					alt119=1;
				}
				else if ( (true) ) {
					alt119=4;
				}

				}
				break;
			case 69:
				{
				int LA119_2 = input.LA(2);
				if ( (LA119_2==62) ) {
					int LA119_8 = input.LA(3);
					if ( (LA119_8==INT_NUMERAL) ) {
						int LA119_12 = input.LA(4);
						if ( (synpred228_JPA2()) ) {
							alt119=3;
						}
						else if ( (true) ) {
							alt119=4;
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
								new NoViableAltException("", 119, 8, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA119_2==INT_NUMERAL) ) {
					int LA119_9 = input.LA(3);
					if ( (synpred228_JPA2()) ) {
						alt119=3;
					}
					else if ( (true) ) {
						alt119=4;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 119, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA119_3 = input.LA(2);
				if ( (synpred228_JPA2()) ) {
					alt119=3;
				}
				else if ( (true) ) {
					alt119=4;
				}

				}
				break;
			case 55:
				{
				int LA119_4 = input.LA(2);
				if ( (LA119_4==WORD) ) {
					int LA119_11 = input.LA(3);
					if ( (LA119_11==136) ) {
						int LA119_13 = input.LA(4);
						if ( (synpred228_JPA2()) ) {
							alt119=3;
						}
						else if ( (true) ) {
							alt119=4;
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
								new NoViableAltException("", 119, 11, input);
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
							new NoViableAltException("", 119, 4, input);
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
				alt119=4;
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
					// JPA2.g:430:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_function_arg3923);
					literal485=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal485.getTree());

					}
					break;
				case 2 :
					// JPA2.g:431:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_function_arg3931);
					path_expression486=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression486.getTree());

					}
					break;
				case 3 :
					// JPA2.g:432:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_function_arg3939);
					input_parameter487=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter487.getTree());

					}
					break;
				case 4 :
					// JPA2.g:433:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_function_arg3947);
					scalar_expression488=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression488.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:434:1: case_expression : ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression );
	public final JPA2Parser.case_expression_return case_expression() throws RecognitionException {
		JPA2Parser.case_expression_return retval = new JPA2Parser.case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope general_case_expression489 =null;
		ParserRuleReturnScope simple_case_expression490 =null;
		ParserRuleReturnScope coalesce_expression491 =null;
		ParserRuleReturnScope nullif_expression492 =null;


		try {
			// JPA2.g:435:5: ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression )
			int alt120=4;
			switch ( input.LA(1) ) {
			case 81:
				{
				int LA120_1 = input.LA(2);
				if ( (LA120_1==131) ) {
					alt120=1;
				}
				else if ( (LA120_1==WORD||LA120_1==127) ) {
					alt120=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 120, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 82:
				{
				alt120=3;
				}
				break;
			case 112:
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
					// JPA2.g:435:7: general_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_case_expression_in_case_expression3958);
					general_case_expression489=general_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_case_expression489.getTree());

					}
					break;
				case 2 :
					// JPA2.g:436:7: simple_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_case_expression_in_case_expression3966);
					simple_case_expression490=simple_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_case_expression490.getTree());

					}
					break;
				case 3 :
					// JPA2.g:437:7: coalesce_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_coalesce_expression_in_case_expression3974);
					coalesce_expression491=coalesce_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, coalesce_expression491.getTree());

					}
					break;
				case 4 :
					// JPA2.g:438:7: nullif_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_nullif_expression_in_case_expression3982);
					nullif_expression492=nullif_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, nullif_expression492.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:439:1: general_case_expression : 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.general_case_expression_return general_case_expression() throws RecognitionException {
		JPA2Parser.general_case_expression_return retval = new JPA2Parser.general_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal493=null;
		Token string_literal496=null;
		Token string_literal498=null;
		ParserRuleReturnScope when_clause494 =null;
		ParserRuleReturnScope when_clause495 =null;
		ParserRuleReturnScope scalar_expression497 =null;

		Object string_literal493_tree=null;
		Object string_literal496_tree=null;
		Object string_literal498_tree=null;

		try {
			// JPA2.g:440:5: ( 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:440:7: 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal493=(Token)match(input,81,FOLLOW_81_in_general_case_expression3993); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal493_tree = (Object)adaptor.create(string_literal493);
			adaptor.addChild(root_0, string_literal493_tree);
			}

			pushFollow(FOLLOW_when_clause_in_general_case_expression3995);
			when_clause494=when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause494.getTree());

			// JPA2.g:440:26: ( when_clause )*
			loop121:
			while (true) {
				int alt121=2;
				int LA121_0 = input.LA(1);
				if ( (LA121_0==131) ) {
					alt121=1;
				}

				switch (alt121) {
				case 1 :
					// JPA2.g:440:27: when_clause
					{
					pushFollow(FOLLOW_when_clause_in_general_case_expression3998);
					when_clause495=when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause495.getTree());

					}
					break;

				default :
					break loop121;
				}
			}

			string_literal496=(Token)match(input,89,FOLLOW_89_in_general_case_expression4002); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal496_tree = (Object)adaptor.create(string_literal496);
			adaptor.addChild(root_0, string_literal496_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_general_case_expression4004);
			scalar_expression497=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression497.getTree());

			string_literal498=(Token)match(input,91,FOLLOW_91_in_general_case_expression4006); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal498_tree = (Object)adaptor.create(string_literal498);
			adaptor.addChild(root_0, string_literal498_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:441:1: when_clause : 'WHEN' conditional_expression 'THEN' scalar_expression ;
	public final JPA2Parser.when_clause_return when_clause() throws RecognitionException {
		JPA2Parser.when_clause_return retval = new JPA2Parser.when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal499=null;
		Token string_literal501=null;
		ParserRuleReturnScope conditional_expression500 =null;
		ParserRuleReturnScope scalar_expression502 =null;

		Object string_literal499_tree=null;
		Object string_literal501_tree=null;

		try {
			// JPA2.g:442:5: ( 'WHEN' conditional_expression 'THEN' scalar_expression )
			// JPA2.g:442:7: 'WHEN' conditional_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal499=(Token)match(input,131,FOLLOW_131_in_when_clause4017); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal499_tree = (Object)adaptor.create(string_literal499);
			adaptor.addChild(root_0, string_literal499_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_when_clause4019);
			conditional_expression500=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression500.getTree());

			string_literal501=(Token)match(input,123,FOLLOW_123_in_when_clause4021); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal501_tree = (Object)adaptor.create(string_literal501);
			adaptor.addChild(root_0, string_literal501_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_when_clause4023);
			scalar_expression502=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression502.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:443:1: simple_case_expression : 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' ;
	public final JPA2Parser.simple_case_expression_return simple_case_expression() throws RecognitionException {
		JPA2Parser.simple_case_expression_return retval = new JPA2Parser.simple_case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal503=null;
		Token string_literal507=null;
		Token string_literal509=null;
		ParserRuleReturnScope case_operand504 =null;
		ParserRuleReturnScope simple_when_clause505 =null;
		ParserRuleReturnScope simple_when_clause506 =null;
		ParserRuleReturnScope scalar_expression508 =null;

		Object string_literal503_tree=null;
		Object string_literal507_tree=null;
		Object string_literal509_tree=null;

		try {
			// JPA2.g:444:5: ( 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:444:7: 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal503=(Token)match(input,81,FOLLOW_81_in_simple_case_expression4034); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal503_tree = (Object)adaptor.create(string_literal503);
			adaptor.addChild(root_0, string_literal503_tree);
			}

			pushFollow(FOLLOW_case_operand_in_simple_case_expression4036);
			case_operand504=case_operand();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, case_operand504.getTree());

			pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4038);
			simple_when_clause505=simple_when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause505.getTree());

			// JPA2.g:444:46: ( simple_when_clause )*
			loop122:
			while (true) {
				int alt122=2;
				int LA122_0 = input.LA(1);
				if ( (LA122_0==131) ) {
					alt122=1;
				}

				switch (alt122) {
				case 1 :
					// JPA2.g:444:47: simple_when_clause
					{
					pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4041);
					simple_when_clause506=simple_when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause506.getTree());

					}
					break;

				default :
					break loop122;
				}
			}

			string_literal507=(Token)match(input,89,FOLLOW_89_in_simple_case_expression4045); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal507_tree = (Object)adaptor.create(string_literal507);
			adaptor.addChild(root_0, string_literal507_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_case_expression4047);
			scalar_expression508=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression508.getTree());

			string_literal509=(Token)match(input,91,FOLLOW_91_in_simple_case_expression4049); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal509_tree = (Object)adaptor.create(string_literal509);
			adaptor.addChild(root_0, string_literal509_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:445:1: case_operand : ( path_expression | type_discriminator );
	public final JPA2Parser.case_operand_return case_operand() throws RecognitionException {
		JPA2Parser.case_operand_return retval = new JPA2Parser.case_operand_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression510 =null;
		ParserRuleReturnScope type_discriminator511 =null;


		try {
			// JPA2.g:446:5: ( path_expression | type_discriminator )
			int alt123=2;
			int LA123_0 = input.LA(1);
			if ( (LA123_0==WORD) ) {
				alt123=1;
			}
			else if ( (LA123_0==127) ) {
				alt123=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 123, 0, input);
				throw nvae;
			}

			switch (alt123) {
				case 1 :
					// JPA2.g:446:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_case_operand4060);
					path_expression510=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression510.getTree());

					}
					break;
				case 2 :
					// JPA2.g:447:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_case_operand4068);
					type_discriminator511=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator511.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:448:1: simple_when_clause : 'WHEN' scalar_expression 'THEN' scalar_expression ;
	public final JPA2Parser.simple_when_clause_return simple_when_clause() throws RecognitionException {
		JPA2Parser.simple_when_clause_return retval = new JPA2Parser.simple_when_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal512=null;
		Token string_literal514=null;
		ParserRuleReturnScope scalar_expression513 =null;
		ParserRuleReturnScope scalar_expression515 =null;

		Object string_literal512_tree=null;
		Object string_literal514_tree=null;

		try {
			// JPA2.g:449:5: ( 'WHEN' scalar_expression 'THEN' scalar_expression )
			// JPA2.g:449:7: 'WHEN' scalar_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal512=(Token)match(input,131,FOLLOW_131_in_simple_when_clause4079); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal512_tree = (Object)adaptor.create(string_literal512);
			adaptor.addChild(root_0, string_literal512_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4081);
			scalar_expression513=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression513.getTree());

			string_literal514=(Token)match(input,123,FOLLOW_123_in_simple_when_clause4083); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal514_tree = (Object)adaptor.create(string_literal514);
			adaptor.addChild(root_0, string_literal514_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4085);
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
	// $ANTLR end "simple_when_clause"


	public static class coalesce_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "coalesce_expression"
	// JPA2.g:450:1: coalesce_expression : 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' ;
	public final JPA2Parser.coalesce_expression_return coalesce_expression() throws RecognitionException {
		JPA2Parser.coalesce_expression_return retval = new JPA2Parser.coalesce_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal516=null;
		Token char_literal518=null;
		Token char_literal520=null;
		ParserRuleReturnScope scalar_expression517 =null;
		ParserRuleReturnScope scalar_expression519 =null;

		Object string_literal516_tree=null;
		Object char_literal518_tree=null;
		Object char_literal520_tree=null;

		try {
			// JPA2.g:451:5: ( 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' )
			// JPA2.g:451:7: 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal516=(Token)match(input,82,FOLLOW_82_in_coalesce_expression4096); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal516_tree = (Object)adaptor.create(string_literal516);
			adaptor.addChild(root_0, string_literal516_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4097);
			scalar_expression517=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression517.getTree());

			// JPA2.g:451:36: ( ',' scalar_expression )+
			int cnt124=0;
			loop124:
			while (true) {
				int alt124=2;
				int LA124_0 = input.LA(1);
				if ( (LA124_0==58) ) {
					alt124=1;
				}

				switch (alt124) {
				case 1 :
					// JPA2.g:451:37: ',' scalar_expression
					{
					char_literal518=(Token)match(input,58,FOLLOW_58_in_coalesce_expression4100); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal518_tree = (Object)adaptor.create(char_literal518);
					adaptor.addChild(root_0, char_literal518_tree);
					}

					pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4102);
					scalar_expression519=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression519.getTree());

					}
					break;

				default :
					if ( cnt124 >= 1 ) break loop124;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(124, input);
					throw eee;
				}
				cnt124++;
			}

			char_literal520=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_coalesce_expression4105); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal520_tree = (Object)adaptor.create(char_literal520);
			adaptor.addChild(root_0, char_literal520_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:452:1: nullif_expression : 'NULLIF(' scalar_expression ',' scalar_expression ')' ;
	public final JPA2Parser.nullif_expression_return nullif_expression() throws RecognitionException {
		JPA2Parser.nullif_expression_return retval = new JPA2Parser.nullif_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal521=null;
		Token char_literal523=null;
		Token char_literal525=null;
		ParserRuleReturnScope scalar_expression522 =null;
		ParserRuleReturnScope scalar_expression524 =null;

		Object string_literal521_tree=null;
		Object char_literal523_tree=null;
		Object char_literal525_tree=null;

		try {
			// JPA2.g:453:5: ( 'NULLIF(' scalar_expression ',' scalar_expression ')' )
			// JPA2.g:453:7: 'NULLIF(' scalar_expression ',' scalar_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal521=(Token)match(input,112,FOLLOW_112_in_nullif_expression4116); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal521_tree = (Object)adaptor.create(string_literal521);
			adaptor.addChild(root_0, string_literal521_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4117);
			scalar_expression522=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression522.getTree());

			char_literal523=(Token)match(input,58,FOLLOW_58_in_nullif_expression4119); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal523_tree = (Object)adaptor.create(char_literal523);
			adaptor.addChild(root_0, char_literal523_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4121);
			scalar_expression524=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression524.getTree());

			char_literal525=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nullif_expression4122); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal525_tree = (Object)adaptor.create(char_literal525);
			adaptor.addChild(root_0, char_literal525_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:456:1: input_parameter : ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) );
	public final JPA2Parser.input_parameter_return input_parameter() throws RecognitionException {
		JPA2Parser.input_parameter_return retval = new JPA2Parser.input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal526=null;
		Token NAMED_PARAMETER528=null;
		Token string_literal529=null;
		Token WORD530=null;
		Token char_literal531=null;
		ParserRuleReturnScope numeric_literal527 =null;

		Object char_literal526_tree=null;
		Object NAMED_PARAMETER528_tree=null;
		Object string_literal529_tree=null;
		Object WORD530_tree=null;
		Object char_literal531_tree=null;
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
		RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
		RewriteRuleTokenStream stream_136=new RewriteRuleTokenStream(adaptor,"token 136");
		RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
		RewriteRuleSubtreeStream stream_numeric_literal=new RewriteRuleSubtreeStream(adaptor,"rule numeric_literal");

		try {
			// JPA2.g:457:5: ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
			int alt125=3;
			switch ( input.LA(1) ) {
			case 69:
				{
				alt125=1;
				}
				break;
			case NAMED_PARAMETER:
				{
				alt125=2;
				}
				break;
			case 55:
				{
				alt125=3;
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
					// JPA2.g:457:7: '?' numeric_literal
					{
					char_literal526=(Token)match(input,69,FOLLOW_69_in_input_parameter4135); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_69.add(char_literal526);

					pushFollow(FOLLOW_numeric_literal_in_input_parameter4137);
					numeric_literal527=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_literal.add(numeric_literal527.getTree());
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
					// 457:27: -> ^( T_PARAMETER[] '?' numeric_literal )
					{
						// JPA2.g:457:30: ^( T_PARAMETER[] '?' numeric_literal )
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
					// JPA2.g:458:7: NAMED_PARAMETER
					{
					NAMED_PARAMETER528=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter4160); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER528);

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
					// 458:23: -> ^( T_PARAMETER[] NAMED_PARAMETER )
					{
						// JPA2.g:458:26: ^( T_PARAMETER[] NAMED_PARAMETER )
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
					// JPA2.g:459:7: '${' WORD '}'
					{
					string_literal529=(Token)match(input,55,FOLLOW_55_in_input_parameter4181); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_55.add(string_literal529);

					WORD530=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter4183); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WORD.add(WORD530);

					char_literal531=(Token)match(input,136,FOLLOW_136_in_input_parameter4185); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_136.add(char_literal531);

					// AST REWRITE
					// elements: WORD, 136, 55
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 459:21: -> ^( T_PARAMETER[] '${' WORD '}' )
					{
						// JPA2.g:459:24: ^( T_PARAMETER[] '${' WORD '}' )
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
	// JPA2.g:461:1: literal : WORD ;
	public final JPA2Parser.literal_return literal() throws RecognitionException {
		JPA2Parser.literal_return retval = new JPA2Parser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD532=null;

		Object WORD532_tree=null;

		try {
			// JPA2.g:462:5: ( WORD )
			// JPA2.g:462:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD532=(Token)match(input,WORD,FOLLOW_WORD_in_literal4213); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD532_tree = (Object)adaptor.create(WORD532);
			adaptor.addChild(root_0, WORD532_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:464:1: constructor_name : WORD ;
	public final JPA2Parser.constructor_name_return constructor_name() throws RecognitionException {
		JPA2Parser.constructor_name_return retval = new JPA2Parser.constructor_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD533=null;

		Object WORD533_tree=null;

		try {
			// JPA2.g:465:5: ( WORD )
			// JPA2.g:465:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD533=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4225); if (state.failed) return retval;
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
	// $ANTLR end "constructor_name"


	public static class enum_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "enum_literal"
	// JPA2.g:467:1: enum_literal : WORD ;
	public final JPA2Parser.enum_literal_return enum_literal() throws RecognitionException {
		JPA2Parser.enum_literal_return retval = new JPA2Parser.enum_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD534=null;

		Object WORD534_tree=null;

		try {
			// JPA2.g:468:5: ( WORD )
			// JPA2.g:468:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD534=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal4237); if (state.failed) return retval;
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
	// $ANTLR end "enum_literal"


	public static class boolean_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "boolean_literal"
	// JPA2.g:470:1: boolean_literal : ( 'true' | 'false' );
	public final JPA2Parser.boolean_literal_return boolean_literal() throws RecognitionException {
		JPA2Parser.boolean_literal_return retval = new JPA2Parser.boolean_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set535=null;

		Object set535_tree=null;

		try {
			// JPA2.g:471:5: ( 'true' | 'false' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set535=input.LT(1);
			if ( (input.LA(1) >= 134 && input.LA(1) <= 135) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set535));
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
	// JPA2.g:475:1: field : ( WORD | 'GROUP' );
	public final JPA2Parser.field_return field() throws RecognitionException {
		JPA2Parser.field_return retval = new JPA2Parser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set536=null;

		Object set536_tree=null;

		try {
			// JPA2.g:476:5: ( WORD | 'GROUP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set536=input.LT(1);
			if ( input.LA(1)==GROUP||input.LA(1)==WORD ) {
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
	// $ANTLR end "field"


	public static class identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "identification_variable"
	// JPA2.g:478:1: identification_variable : WORD ;
	public final JPA2Parser.identification_variable_return identification_variable() throws RecognitionException {
		JPA2Parser.identification_variable_return retval = new JPA2Parser.identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD537=null;

		Object WORD537_tree=null;

		try {
			// JPA2.g:479:5: ( WORD )
			// JPA2.g:479:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD537=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable4286); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD537_tree = (Object)adaptor.create(WORD537);
			adaptor.addChild(root_0, WORD537_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:481:1: parameter_name : WORD ( '.' WORD )* ;
	public final JPA2Parser.parameter_name_return parameter_name() throws RecognitionException {
		JPA2Parser.parameter_name_return retval = new JPA2Parser.parameter_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD538=null;
		Token char_literal539=null;
		Token WORD540=null;

		Object WORD538_tree=null;
		Object char_literal539_tree=null;
		Object WORD540_tree=null;

		try {
			// JPA2.g:482:5: ( WORD ( '.' WORD )* )
			// JPA2.g:482:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD538=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4298); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD538_tree = (Object)adaptor.create(WORD538);
			adaptor.addChild(root_0, WORD538_tree);
			}

			// JPA2.g:482:12: ( '.' WORD )*
			loop126:
			while (true) {
				int alt126=2;
				int LA126_0 = input.LA(1);
				if ( (LA126_0==60) ) {
					alt126=1;
				}

				switch (alt126) {
				case 1 :
					// JPA2.g:482:13: '.' WORD
					{
					char_literal539=(Token)match(input,60,FOLLOW_60_in_parameter_name4301); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal539_tree = (Object)adaptor.create(char_literal539);
					adaptor.addChild(root_0, char_literal539_tree);
					}

					WORD540=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4304); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD540_tree = (Object)adaptor.create(WORD540);
					adaptor.addChild(root_0, WORD540_tree);
					}

					}
					break;

				default :
					break loop126;
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
	// JPA2.g:485:1: escape_character : ( '\\'.\\'' | STRING_LITERAL );
	public final JPA2Parser.escape_character_return escape_character() throws RecognitionException {
		JPA2Parser.escape_character_return retval = new JPA2Parser.escape_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set541=null;

		Object set541_tree=null;

		try {
			// JPA2.g:486:5: ( '\\'.\\'' | STRING_LITERAL )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set541=input.LT(1);
			if ( input.LA(1)==STRING_LITERAL||input.LA(1)==TRIM_CHARACTER ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set541));
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
	// JPA2.g:487:1: trim_character : TRIM_CHARACTER ;
	public final JPA2Parser.trim_character_return trim_character() throws RecognitionException {
		JPA2Parser.trim_character_return retval = new JPA2Parser.trim_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRIM_CHARACTER542=null;

		Object TRIM_CHARACTER542_tree=null;

		try {
			// JPA2.g:488:5: ( TRIM_CHARACTER )
			// JPA2.g:488:7: TRIM_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			TRIM_CHARACTER542=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_trim_character4334); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TRIM_CHARACTER542_tree = (Object)adaptor.create(TRIM_CHARACTER542);
			adaptor.addChild(root_0, TRIM_CHARACTER542_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:489:1: string_literal : STRING_LITERAL ;
	public final JPA2Parser.string_literal_return string_literal() throws RecognitionException {
		JPA2Parser.string_literal_return retval = new JPA2Parser.string_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL543=null;

		Object STRING_LITERAL543_tree=null;

		try {
			// JPA2.g:490:5: ( STRING_LITERAL )
			// JPA2.g:490:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL543=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_literal4345); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL543_tree = (Object)adaptor.create(STRING_LITERAL543);
			adaptor.addChild(root_0, STRING_LITERAL543_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:491:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
	public final JPA2Parser.numeric_literal_return numeric_literal() throws RecognitionException {
		JPA2Parser.numeric_literal_return retval = new JPA2Parser.numeric_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal544=null;
		Token INT_NUMERAL545=null;

		Object string_literal544_tree=null;
		Object INT_NUMERAL545_tree=null;

		try {
			// JPA2.g:492:5: ( ( '0x' )? INT_NUMERAL )
			// JPA2.g:492:7: ( '0x' )? INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:492:7: ( '0x' )?
			int alt127=2;
			int LA127_0 = input.LA(1);
			if ( (LA127_0==62) ) {
				alt127=1;
			}
			switch (alt127) {
				case 1 :
					// JPA2.g:492:8: '0x'
					{
					string_literal544=(Token)match(input,62,FOLLOW_62_in_numeric_literal4357); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal544_tree = (Object)adaptor.create(string_literal544);
					adaptor.addChild(root_0, string_literal544_tree);
					}

					}
					break;

			}

			INT_NUMERAL545=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal4361); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL545_tree = (Object)adaptor.create(INT_NUMERAL545);
			adaptor.addChild(root_0, INT_NUMERAL545_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:493:1: single_valued_object_field : WORD ;
	public final JPA2Parser.single_valued_object_field_return single_valued_object_field() throws RecognitionException {
		JPA2Parser.single_valued_object_field_return retval = new JPA2Parser.single_valued_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD546=null;

		Object WORD546_tree=null;

		try {
			// JPA2.g:494:5: ( WORD )
			// JPA2.g:494:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD546=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_object_field4373); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD546_tree = (Object)adaptor.create(WORD546);
			adaptor.addChild(root_0, WORD546_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:495:1: single_valued_embeddable_object_field : WORD ;
	public final JPA2Parser.single_valued_embeddable_object_field_return single_valued_embeddable_object_field() throws RecognitionException {
		JPA2Parser.single_valued_embeddable_object_field_return retval = new JPA2Parser.single_valued_embeddable_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD547=null;

		Object WORD547_tree=null;

		try {
			// JPA2.g:496:5: ( WORD )
			// JPA2.g:496:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD547=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_embeddable_object_field4384); if (state.failed) return retval;
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
	// $ANTLR end "single_valued_embeddable_object_field"


	public static class collection_valued_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "collection_valued_field"
	// JPA2.g:497:1: collection_valued_field : WORD ;
	public final JPA2Parser.collection_valued_field_return collection_valued_field() throws RecognitionException {
		JPA2Parser.collection_valued_field_return retval = new JPA2Parser.collection_valued_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD548=null;

		Object WORD548_tree=null;

		try {
			// JPA2.g:498:5: ( WORD )
			// JPA2.g:498:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD548=(Token)match(input,WORD,FOLLOW_WORD_in_collection_valued_field4395); if (state.failed) return retval;
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
	// $ANTLR end "collection_valued_field"


	public static class entity_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_name"
	// JPA2.g:499:1: entity_name : WORD ;
	public final JPA2Parser.entity_name_return entity_name() throws RecognitionException {
		JPA2Parser.entity_name_return retval = new JPA2Parser.entity_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD549=null;

		Object WORD549_tree=null;

		try {
			// JPA2.g:500:5: ( WORD )
			// JPA2.g:500:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD549=(Token)match(input,WORD,FOLLOW_WORD_in_entity_name4406); if (state.failed) return retval;
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
	// $ANTLR end "entity_name"


	public static class subtype_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "subtype"
	// JPA2.g:501:1: subtype : WORD ;
	public final JPA2Parser.subtype_return subtype() throws RecognitionException {
		JPA2Parser.subtype_return retval = new JPA2Parser.subtype_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD550=null;

		Object WORD550_tree=null;

		try {
			// JPA2.g:502:5: ( WORD )
			// JPA2.g:502:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD550=(Token)match(input,WORD,FOLLOW_WORD_in_subtype4417); if (state.failed) return retval;
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
	// $ANTLR end "subtype"


	public static class entity_type_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_type_literal"
	// JPA2.g:503:1: entity_type_literal : WORD ;
	public final JPA2Parser.entity_type_literal_return entity_type_literal() throws RecognitionException {
		JPA2Parser.entity_type_literal_return retval = new JPA2Parser.entity_type_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD551=null;

		Object WORD551_tree=null;

		try {
			// JPA2.g:504:5: ( WORD )
			// JPA2.g:504:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD551=(Token)match(input,WORD,FOLLOW_WORD_in_entity_type_literal4428); if (state.failed) return retval;
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
	// $ANTLR end "entity_type_literal"


	public static class function_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "function_name"
	// JPA2.g:505:1: function_name : STRING_LITERAL ;
	public final JPA2Parser.function_name_return function_name() throws RecognitionException {
		JPA2Parser.function_name_return retval = new JPA2Parser.function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL552=null;

		Object STRING_LITERAL552_tree=null;

		try {
			// JPA2.g:506:5: ( STRING_LITERAL )
			// JPA2.g:506:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL552=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_function_name4439); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL552_tree = (Object)adaptor.create(STRING_LITERAL552);
			adaptor.addChild(root_0, STRING_LITERAL552_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:507:1: state_field : WORD ;
	public final JPA2Parser.state_field_return state_field() throws RecognitionException {
		JPA2Parser.state_field_return retval = new JPA2Parser.state_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD553=null;

		Object WORD553_tree=null;

		try {
			// JPA2.g:508:5: ( WORD )
			// JPA2.g:508:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD553=(Token)match(input,WORD,FOLLOW_WORD_in_state_field4450); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD553_tree = (Object)adaptor.create(WORD553);
			adaptor.addChild(root_0, WORD553_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:509:1: result_variable : WORD ;
	public final JPA2Parser.result_variable_return result_variable() throws RecognitionException {
		JPA2Parser.result_variable_return retval = new JPA2Parser.result_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD554=null;

		Object WORD554_tree=null;

		try {
			// JPA2.g:510:5: ( WORD )
			// JPA2.g:510:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD554=(Token)match(input,WORD,FOLLOW_WORD_in_result_variable4461); if (state.failed) return retval;
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
	// $ANTLR end "result_variable"


	public static class superquery_identification_variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "superquery_identification_variable"
	// JPA2.g:511:1: superquery_identification_variable : WORD ;
	public final JPA2Parser.superquery_identification_variable_return superquery_identification_variable() throws RecognitionException {
		JPA2Parser.superquery_identification_variable_return retval = new JPA2Parser.superquery_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD555=null;

		Object WORD555_tree=null;

		try {
			// JPA2.g:512:5: ( WORD )
			// JPA2.g:512:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD555=(Token)match(input,WORD,FOLLOW_WORD_in_superquery_identification_variable4472); if (state.failed) return retval;
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
	// $ANTLR end "superquery_identification_variable"


	public static class date_time_timestamp_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_time_timestamp_literal"
	// JPA2.g:513:1: date_time_timestamp_literal : WORD ;
	public final JPA2Parser.date_time_timestamp_literal_return date_time_timestamp_literal() throws RecognitionException {
		JPA2Parser.date_time_timestamp_literal_return retval = new JPA2Parser.date_time_timestamp_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD556=null;

		Object WORD556_tree=null;

		try {
			// JPA2.g:514:5: ( WORD )
			// JPA2.g:514:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD556=(Token)match(input,WORD,FOLLOW_WORD_in_date_time_timestamp_literal4483); if (state.failed) return retval;
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
	// $ANTLR end "date_time_timestamp_literal"


	public static class pattern_value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "pattern_value"
	// JPA2.g:515:1: pattern_value : string_literal ;
	public final JPA2Parser.pattern_value_return pattern_value() throws RecognitionException {
		JPA2Parser.pattern_value_return retval = new JPA2Parser.pattern_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal557 =null;


		try {
			// JPA2.g:516:5: ( string_literal )
			// JPA2.g:516:7: string_literal
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_literal_in_pattern_value4494);
			string_literal557=string_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal557.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:517:1: collection_valued_input_parameter : input_parameter ;
	public final JPA2Parser.collection_valued_input_parameter_return collection_valued_input_parameter() throws RecognitionException {
		JPA2Parser.collection_valued_input_parameter_return retval = new JPA2Parser.collection_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter558 =null;


		try {
			// JPA2.g:518:5: ( input_parameter )
			// JPA2.g:518:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_collection_valued_input_parameter4505);
			input_parameter558=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter558.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:519:1: single_valued_input_parameter : input_parameter ;
	public final JPA2Parser.single_valued_input_parameter_return single_valued_input_parameter() throws RecognitionException {
		JPA2Parser.single_valued_input_parameter_return retval = new JPA2Parser.single_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter559 =null;


		try {
			// JPA2.g:520:5: ( input_parameter )
			// JPA2.g:520:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_single_valued_input_parameter4516);
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
	// $ANTLR end "single_valued_input_parameter"

	// $ANTLR start synpred20_JPA2
	public final void synpred20_JPA2_fragment() throws RecognitionException {
		// JPA2.g:115:48: ( field )
		// JPA2.g:115:48: field
		{
		pushFollow(FOLLOW_field_in_synpred20_JPA2873);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred20_JPA2

	// $ANTLR start synpred29_JPA2
	public final void synpred29_JPA2_fragment() throws RecognitionException {
		// JPA2.g:133:48: ( field )
		// JPA2.g:133:48: field
		{
		pushFollow(FOLLOW_field_in_synpred29_JPA21063);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred29_JPA2

	// $ANTLR start synpred32_JPA2
	public final void synpred32_JPA2_fragment() throws RecognitionException {
		// JPA2.g:150:7: ( scalar_expression )
		// JPA2.g:150:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred32_JPA21175);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred32_JPA2

	// $ANTLR start synpred33_JPA2
	public final void synpred33_JPA2_fragment() throws RecognitionException {
		// JPA2.g:151:7: ( simple_entity_expression )
		// JPA2.g:151:7: simple_entity_expression
		{
		pushFollow(FOLLOW_simple_entity_expression_in_synpred33_JPA21183);
		simple_entity_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred33_JPA2

	// $ANTLR start synpred38_JPA2
	public final void synpred38_JPA2_fragment() throws RecognitionException {
		// JPA2.g:163:7: ( path_expression )
		// JPA2.g:163:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred38_JPA21303);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred38_JPA2

	// $ANTLR start synpred39_JPA2
	public final void synpred39_JPA2_fragment() throws RecognitionException {
		// JPA2.g:164:7: ( identification_variable )
		// JPA2.g:164:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred39_JPA21311);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred39_JPA2

	// $ANTLR start synpred40_JPA2
	public final void synpred40_JPA2_fragment() throws RecognitionException {
		// JPA2.g:165:7: ( scalar_expression )
		// JPA2.g:165:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred40_JPA21329);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred40_JPA2

	// $ANTLR start synpred41_JPA2
	public final void synpred41_JPA2_fragment() throws RecognitionException {
		// JPA2.g:166:7: ( aggregate_expression )
		// JPA2.g:166:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred41_JPA21337);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred41_JPA2

	// $ANTLR start synpred44_JPA2
	public final void synpred44_JPA2_fragment() throws RecognitionException {
		// JPA2.g:172:7: ( path_expression )
		// JPA2.g:172:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred44_JPA21394);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred44_JPA2

	// $ANTLR start synpred45_JPA2
	public final void synpred45_JPA2_fragment() throws RecognitionException {
		// JPA2.g:173:7: ( scalar_expression )
		// JPA2.g:173:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred45_JPA21402);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred45_JPA2

	// $ANTLR start synpred46_JPA2
	public final void synpred46_JPA2_fragment() throws RecognitionException {
		// JPA2.g:174:7: ( aggregate_expression )
		// JPA2.g:174:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred46_JPA21410);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred46_JPA2

	// $ANTLR start synpred48_JPA2
	public final void synpred48_JPA2_fragment() throws RecognitionException {
		// JPA2.g:177:7: ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' )
		// JPA2.g:177:7: aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')'
		{
		pushFollow(FOLLOW_aggregate_expression_function_name_in_synpred48_JPA21429);
		aggregate_expression_function_name();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred48_JPA21431); if (state.failed) return;

		// JPA2.g:177:45: ( DISTINCT )?
		int alt134=2;
		int LA134_0 = input.LA(1);
		if ( (LA134_0==DISTINCT) ) {
			alt134=1;
		}
		switch (alt134) {
			case 1 :
				// JPA2.g:177:46: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred48_JPA21433); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_path_expression_in_synpred48_JPA21437);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred48_JPA21438); if (state.failed) return;

		}

	}
	// $ANTLR end synpred48_JPA2

	// $ANTLR start synpred50_JPA2
	public final void synpred50_JPA2_fragment() throws RecognitionException {
		// JPA2.g:179:7: ( 'COUNT' '(' ( DISTINCT )? count_argument ')' )
		// JPA2.g:179:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
		{
		match(input,COUNT,FOLLOW_COUNT_in_synpred50_JPA21472); if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred50_JPA21474); if (state.failed) return;

		// JPA2.g:179:18: ( DISTINCT )?
		int alt135=2;
		int LA135_0 = input.LA(1);
		if ( (LA135_0==DISTINCT) ) {
			alt135=1;
		}
		switch (alt135) {
			case 1 :
				// JPA2.g:179:19: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred50_JPA21476); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_count_argument_in_synpred50_JPA21480);
		count_argument();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred50_JPA21482); if (state.failed) return;

		}

	}
	// $ANTLR end synpred50_JPA2

	// $ANTLR start synpred62_JPA2
	public final void synpred62_JPA2_fragment() throws RecognitionException {
		// JPA2.g:204:25: ( general_identification_variable )
		// JPA2.g:204:25: general_identification_variable
		{
		pushFollow(FOLLOW_general_identification_variable_in_synpred62_JPA21780);
		general_identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred62_JPA2

	// $ANTLR start synpred70_JPA2
	public final void synpred70_JPA2_fragment() throws RecognitionException {
		// JPA2.g:218:7: ( general_derived_path '.' single_valued_object_field )
		// JPA2.g:218:7: general_derived_path '.' single_valued_object_field
		{
		pushFollow(FOLLOW_general_derived_path_in_synpred70_JPA21954);
		general_derived_path();
		state._fsp--;
		if (state.failed) return;

		match(input,60,FOLLOW_60_in_synpred70_JPA21955); if (state.failed) return;

		pushFollow(FOLLOW_single_valued_object_field_in_synpred70_JPA21956);
		single_valued_object_field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred70_JPA2

	// $ANTLR start synpred75_JPA2
	public final void synpred75_JPA2_fragment() throws RecognitionException {
		// JPA2.g:236:7: ( path_expression )
		// JPA2.g:236:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred75_JPA22108);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred75_JPA2

	// $ANTLR start synpred76_JPA2
	public final void synpred76_JPA2_fragment() throws RecognitionException {
		// JPA2.g:237:7: ( scalar_expression )
		// JPA2.g:237:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred76_JPA22116);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred76_JPA2

	// $ANTLR start synpred77_JPA2
	public final void synpred77_JPA2_fragment() throws RecognitionException {
		// JPA2.g:238:7: ( aggregate_expression )
		// JPA2.g:238:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred77_JPA22124);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred77_JPA2

	// $ANTLR start synpred78_JPA2
	public final void synpred78_JPA2_fragment() throws RecognitionException {
		// JPA2.g:241:7: ( arithmetic_expression )
		// JPA2.g:241:7: arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred78_JPA22143);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred78_JPA2

	// $ANTLR start synpred79_JPA2
	public final void synpred79_JPA2_fragment() throws RecognitionException {
		// JPA2.g:242:7: ( string_expression )
		// JPA2.g:242:7: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred79_JPA22151);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred79_JPA2

	// $ANTLR start synpred80_JPA2
	public final void synpred80_JPA2_fragment() throws RecognitionException {
		// JPA2.g:243:7: ( enum_expression )
		// JPA2.g:243:7: enum_expression
		{
		pushFollow(FOLLOW_enum_expression_in_synpred80_JPA22159);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred80_JPA2

	// $ANTLR start synpred81_JPA2
	public final void synpred81_JPA2_fragment() throws RecognitionException {
		// JPA2.g:244:7: ( datetime_expression )
		// JPA2.g:244:7: datetime_expression
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred81_JPA22167);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred81_JPA2

	// $ANTLR start synpred82_JPA2
	public final void synpred82_JPA2_fragment() throws RecognitionException {
		// JPA2.g:245:7: ( boolean_expression )
		// JPA2.g:245:7: boolean_expression
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred82_JPA22175);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred82_JPA2

	// $ANTLR start synpred83_JPA2
	public final void synpred83_JPA2_fragment() throws RecognitionException {
		// JPA2.g:246:7: ( case_expression )
		// JPA2.g:246:7: case_expression
		{
		pushFollow(FOLLOW_case_expression_in_synpred83_JPA22183);
		case_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred83_JPA2

	// $ANTLR start synpred86_JPA2
	public final void synpred86_JPA2_fragment() throws RecognitionException {
		// JPA2.g:253:8: ( 'NOT' )
		// JPA2.g:253:8: 'NOT'
		{
		match(input,NOT,FOLLOW_NOT_in_synpred86_JPA22243); if (state.failed) return;

		}

	}
	// $ANTLR end synpred86_JPA2

	// $ANTLR start synpred87_JPA2
	public final void synpred87_JPA2_fragment() throws RecognitionException {
		// JPA2.g:255:7: ( simple_cond_expression )
		// JPA2.g:255:7: simple_cond_expression
		{
		pushFollow(FOLLOW_simple_cond_expression_in_synpred87_JPA22258);
		simple_cond_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred87_JPA2

	// $ANTLR start synpred88_JPA2
	public final void synpred88_JPA2_fragment() throws RecognitionException {
		// JPA2.g:259:7: ( comparison_expression )
		// JPA2.g:259:7: comparison_expression
		{
		pushFollow(FOLLOW_comparison_expression_in_synpred88_JPA22295);
		comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred88_JPA2

	// $ANTLR start synpred89_JPA2
	public final void synpred89_JPA2_fragment() throws RecognitionException {
		// JPA2.g:260:7: ( between_expression )
		// JPA2.g:260:7: between_expression
		{
		pushFollow(FOLLOW_between_expression_in_synpred89_JPA22303);
		between_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred89_JPA2

	// $ANTLR start synpred90_JPA2
	public final void synpred90_JPA2_fragment() throws RecognitionException {
		// JPA2.g:261:7: ( in_expression )
		// JPA2.g:261:7: in_expression
		{
		pushFollow(FOLLOW_in_expression_in_synpred90_JPA22311);
		in_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred90_JPA2

	// $ANTLR start synpred91_JPA2
	public final void synpred91_JPA2_fragment() throws RecognitionException {
		// JPA2.g:262:7: ( like_expression )
		// JPA2.g:262:7: like_expression
		{
		pushFollow(FOLLOW_like_expression_in_synpred91_JPA22319);
		like_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred91_JPA2

	// $ANTLR start synpred92_JPA2
	public final void synpred92_JPA2_fragment() throws RecognitionException {
		// JPA2.g:263:7: ( null_comparison_expression )
		// JPA2.g:263:7: null_comparison_expression
		{
		pushFollow(FOLLOW_null_comparison_expression_in_synpred92_JPA22327);
		null_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred92_JPA2

	// $ANTLR start synpred93_JPA2
	public final void synpred93_JPA2_fragment() throws RecognitionException {
		// JPA2.g:264:7: ( empty_collection_comparison_expression )
		// JPA2.g:264:7: empty_collection_comparison_expression
		{
		pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred93_JPA22335);
		empty_collection_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred93_JPA2

	// $ANTLR start synpred94_JPA2
	public final void synpred94_JPA2_fragment() throws RecognitionException {
		// JPA2.g:265:7: ( collection_member_expression )
		// JPA2.g:265:7: collection_member_expression
		{
		pushFollow(FOLLOW_collection_member_expression_in_synpred94_JPA22343);
		collection_member_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred94_JPA2

	// $ANTLR start synpred113_JPA2
	public final void synpred113_JPA2_fragment() throws RecognitionException {
		// JPA2.g:294:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
		// JPA2.g:294:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred113_JPA22596);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:294:29: ( 'NOT' )?
		int alt138=2;
		int LA138_0 = input.LA(1);
		if ( (LA138_0==NOT) ) {
			alt138=1;
		}
		switch (alt138) {
			case 1 :
				// JPA2.g:294:30: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred113_JPA22599); if (state.failed) return;

				}
				break;

		}

		match(input,79,FOLLOW_79_in_synpred113_JPA22603); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred113_JPA22605);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred113_JPA22607); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred113_JPA22609);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred113_JPA2

	// $ANTLR start synpred115_JPA2
	public final void synpred115_JPA2_fragment() throws RecognitionException {
		// JPA2.g:295:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
		// JPA2.g:295:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred115_JPA22617);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:295:25: ( 'NOT' )?
		int alt139=2;
		int LA139_0 = input.LA(1);
		if ( (LA139_0==NOT) ) {
			alt139=1;
		}
		switch (alt139) {
			case 1 :
				// JPA2.g:295:26: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred115_JPA22620); if (state.failed) return;

				}
				break;

		}

		match(input,79,FOLLOW_79_in_synpred115_JPA22624); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred115_JPA22626);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred115_JPA22628); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred115_JPA22630);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred115_JPA2

	// $ANTLR start synpred128_JPA2
	public final void synpred128_JPA2_fragment() throws RecognitionException {
		// JPA2.g:313:8: ( path_expression )
		// JPA2.g:313:8: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred128_JPA22834);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred128_JPA2

	// $ANTLR start synpred136_JPA2
	public final void synpred136_JPA2_fragment() throws RecognitionException {
		// JPA2.g:323:7: ( identification_variable )
		// JPA2.g:323:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred136_JPA22936);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred136_JPA2

	// $ANTLR start synpred142_JPA2
	public final void synpred142_JPA2_fragment() throws RecognitionException {
		// JPA2.g:331:7: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) )
		// JPA2.g:331:7: string_expression comparison_operator ( string_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_string_expression_in_synpred142_JPA23005);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred142_JPA23007);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:331:45: ( string_expression | all_or_any_expression )
		int alt141=2;
		int LA141_0 = input.LA(1);
		if ( (LA141_0==AVG||LA141_0==COUNT||(LA141_0 >= LOWER && LA141_0 <= NAMED_PARAMETER)||(LA141_0 >= STRING_LITERAL && LA141_0 <= SUM)||LA141_0==WORD||LA141_0==55||LA141_0==69||(LA141_0 >= 81 && LA141_0 <= 83)||LA141_0==96||LA141_0==112||LA141_0==122||LA141_0==126||LA141_0==129) ) {
			alt141=1;
		}
		else if ( ((LA141_0 >= 76 && LA141_0 <= 77)||LA141_0==120) ) {
			alt141=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 141, 0, input);
			throw nvae;
		}

		switch (alt141) {
			case 1 :
				// JPA2.g:331:46: string_expression
				{
				pushFollow(FOLLOW_string_expression_in_synpred142_JPA23010);
				string_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:331:66: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred142_JPA23014);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred142_JPA2

	// $ANTLR start synpred145_JPA2
	public final void synpred145_JPA2_fragment() throws RecognitionException {
		// JPA2.g:332:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
		// JPA2.g:332:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred145_JPA23023);
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
		// JPA2.g:332:39: ( boolean_expression | all_or_any_expression )
		int alt142=2;
		int LA142_0 = input.LA(1);
		if ( (LA142_0==LPAREN||LA142_0==NAMED_PARAMETER||LA142_0==WORD||LA142_0==55||LA142_0==69||(LA142_0 >= 81 && LA142_0 <= 82)||LA142_0==96||LA142_0==112||(LA142_0 >= 134 && LA142_0 <= 135)) ) {
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
				// JPA2.g:332:40: boolean_expression
				{
				pushFollow(FOLLOW_boolean_expression_in_synpred145_JPA23034);
				boolean_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:332:61: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred145_JPA23038);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred145_JPA2

	// $ANTLR start synpred148_JPA2
	public final void synpred148_JPA2_fragment() throws RecognitionException {
		// JPA2.g:333:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
		// JPA2.g:333:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_enum_expression_in_synpred148_JPA23047);
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
		// JPA2.g:333:34: ( enum_expression | all_or_any_expression )
		int alt143=2;
		int LA143_0 = input.LA(1);
		if ( (LA143_0==LPAREN||LA143_0==NAMED_PARAMETER||LA143_0==WORD||LA143_0==55||LA143_0==69||(LA143_0 >= 81 && LA143_0 <= 82)||LA143_0==112) ) {
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
				// JPA2.g:333:35: enum_expression
				{
				pushFollow(FOLLOW_enum_expression_in_synpred148_JPA23056);
				enum_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:333:53: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred148_JPA23060);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred148_JPA2

	// $ANTLR start synpred150_JPA2
	public final void synpred150_JPA2_fragment() throws RecognitionException {
		// JPA2.g:334:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
		// JPA2.g:334:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred150_JPA23069);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred150_JPA23071);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:334:47: ( datetime_expression | all_or_any_expression )
		int alt144=2;
		int LA144_0 = input.LA(1);
		if ( (LA144_0==AVG||LA144_0==COUNT||(LA144_0 >= LPAREN && LA144_0 <= NAMED_PARAMETER)||LA144_0==SUM||LA144_0==WORD||LA144_0==55||LA144_0==69||(LA144_0 >= 81 && LA144_0 <= 82)||(LA144_0 >= 84 && LA144_0 <= 86)||LA144_0==96||LA144_0==112) ) {
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
				// JPA2.g:334:48: datetime_expression
				{
				pushFollow(FOLLOW_datetime_expression_in_synpred150_JPA23074);
				datetime_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:334:70: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred150_JPA23078);
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
		// JPA2.g:335:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
		// JPA2.g:335:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_entity_expression_in_synpred153_JPA23087);
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
		// JPA2.g:335:38: ( entity_expression | all_or_any_expression )
		int alt145=2;
		int LA145_0 = input.LA(1);
		if ( (LA145_0==NAMED_PARAMETER||LA145_0==WORD||LA145_0==55||LA145_0==69) ) {
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
				// JPA2.g:335:39: entity_expression
				{
				pushFollow(FOLLOW_entity_expression_in_synpred153_JPA23098);
				entity_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:335:59: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred153_JPA23102);
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
		// JPA2.g:336:7: ( entity_type_expression ( '=' | '<>' ) entity_type_expression )
		// JPA2.g:336:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
		{
		pushFollow(FOLLOW_entity_type_expression_in_synpred155_JPA23111);
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
		pushFollow(FOLLOW_entity_type_expression_in_synpred155_JPA23121);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred155_JPA2

	// $ANTLR start synpred162_JPA2
	public final void synpred162_JPA2_fragment() throws RecognitionException {
		// JPA2.g:347:7: ( arithmetic_term )
		// JPA2.g:347:7: arithmetic_term
		{
		pushFollow(FOLLOW_arithmetic_term_in_synpred162_JPA23202);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred162_JPA2

	// $ANTLR start synpred164_JPA2
	public final void synpred164_JPA2_fragment() throws RecognitionException {
		// JPA2.g:350:7: ( arithmetic_factor )
		// JPA2.g:350:7: arithmetic_factor
		{
		pushFollow(FOLLOW_arithmetic_factor_in_synpred164_JPA23231);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred164_JPA2

	// $ANTLR start synpred170_JPA2
	public final void synpred170_JPA2_fragment() throws RecognitionException {
		// JPA2.g:357:7: ( '(' arithmetic_expression ')' )
		// JPA2.g:357:7: '(' arithmetic_expression ')'
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred170_JPA23300); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred170_JPA23301);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred170_JPA23302); if (state.failed) return;

		}

	}
	// $ANTLR end synpred170_JPA2

	// $ANTLR start synpred173_JPA2
	public final void synpred173_JPA2_fragment() throws RecognitionException {
		// JPA2.g:360:7: ( aggregate_expression )
		// JPA2.g:360:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred173_JPA23326);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred173_JPA2

	// $ANTLR start synpred175_JPA2
	public final void synpred175_JPA2_fragment() throws RecognitionException {
		// JPA2.g:362:7: ( function_invocation )
		// JPA2.g:362:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred175_JPA23342);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred175_JPA2

	// $ANTLR start synpred180_JPA2
	public final void synpred180_JPA2_fragment() throws RecognitionException {
		// JPA2.g:369:7: ( aggregate_expression )
		// JPA2.g:369:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred180_JPA23393);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred180_JPA2

	// $ANTLR start synpred182_JPA2
	public final void synpred182_JPA2_fragment() throws RecognitionException {
		// JPA2.g:371:7: ( function_invocation )
		// JPA2.g:371:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred182_JPA23409);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred182_JPA2

	// $ANTLR start synpred183_JPA2
	public final void synpred183_JPA2_fragment() throws RecognitionException {
		// JPA2.g:374:7: ( path_expression )
		// JPA2.g:374:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred183_JPA23428);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred183_JPA2

	// $ANTLR start synpred186_JPA2
	public final void synpred186_JPA2_fragment() throws RecognitionException {
		// JPA2.g:377:7: ( aggregate_expression )
		// JPA2.g:377:7: aggregate_expression
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
		// JPA2.g:379:7: ( function_invocation )
		// JPA2.g:379:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred188_JPA23468);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred188_JPA2

	// $ANTLR start synpred189_JPA2
	public final void synpred189_JPA2_fragment() throws RecognitionException {
		// JPA2.g:380:7: ( date_time_timestamp_literal )
		// JPA2.g:380:7: date_time_timestamp_literal
		{
		pushFollow(FOLLOW_date_time_timestamp_literal_in_synpred189_JPA23476);
		date_time_timestamp_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred189_JPA2

	// $ANTLR start synpred226_JPA2
	public final void synpred226_JPA2_fragment() throws RecognitionException {
		// JPA2.g:430:7: ( literal )
		// JPA2.g:430:7: literal
		{
		pushFollow(FOLLOW_literal_in_synpred226_JPA23923);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred226_JPA2

	// $ANTLR start synpred228_JPA2
	public final void synpred228_JPA2_fragment() throws RecognitionException {
		// JPA2.g:432:7: ( input_parameter )
		// JPA2.g:432:7: input_parameter
		{
		pushFollow(FOLLOW_input_parameter_in_synpred228_JPA23939);
		input_parameter();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred228_JPA2

	// Delegated rules

	public final boolean synpred32_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred32_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred226_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred226_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred182_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred182_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred38_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred38_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred142_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred142_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred173_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred173_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred136_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred136_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred145_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred145_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred170_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred170_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred115_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred115_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred48_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred48_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred113_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred113_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred75_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred75_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred228_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred228_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred29_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred29_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred20_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred20_JPA2_fragment(); // can never throw exception
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


	protected DFA39 dfa39 = new DFA39(this);
	protected DFA45 dfa45 = new DFA45(this);
	static final String DFA39_eotS =
		"\14\uffff";
	static final String DFA39_eofS =
		"\14\uffff";
	static final String DFA39_minS =
		"\1\6\1\27\2\uffff\1\13\1\65\1\37\1\uffff\1\16\1\37\1\0\1\16";
	static final String DFA39_maxS =
		"\1\140\1\27\2\uffff\2\65\1\74\1\uffff\1\65\1\74\1\0\1\65";
	static final String DFA39_acceptS =
		"\2\uffff\1\1\1\3\3\uffff\1\2\4\uffff";
	static final String DFA39_specialS =
		"\12\uffff\1\0\1\uffff}>";
	static final String[] DFA39_transitionS = {
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

	static final short[] DFA39_eot = DFA.unpackEncodedString(DFA39_eotS);
	static final short[] DFA39_eof = DFA.unpackEncodedString(DFA39_eofS);
	static final char[] DFA39_min = DFA.unpackEncodedStringToUnsignedChars(DFA39_minS);
	static final char[] DFA39_max = DFA.unpackEncodedStringToUnsignedChars(DFA39_maxS);
	static final short[] DFA39_accept = DFA.unpackEncodedString(DFA39_acceptS);
	static final short[] DFA39_special = DFA.unpackEncodedString(DFA39_specialS);
	static final short[][] DFA39_transition;

	static {
		int numStates = DFA39_transitionS.length;
		DFA39_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA39_transition[i] = DFA.unpackEncodedString(DFA39_transitionS[i]);
		}
	}

	protected class DFA39 extends DFA {

		public DFA39(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 39;
			this.eot = DFA39_eot;
			this.eof = DFA39_eof;
			this.min = DFA39_min;
			this.max = DFA39_max;
			this.accept = DFA39_accept;
			this.special = DFA39_special;
			this.transition = DFA39_transition;
		}
		@Override
		public String getDescription() {
			return "176:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			TokenStream input = (TokenStream)_input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA39_10 = input.LA(1);
						 
						int index39_10 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred48_JPA2()) ) {s = 2;}
						else if ( (synpred50_JPA2()) ) {s = 7;}
						 
						input.seek(index39_10);
						if ( s>=0 ) return s;
						break;
			}
			if (state.backtracking>0) {state.failed=true; return -1;}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 39, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	static final String DFA45_eotS =
		"\15\uffff";
	static final String DFA45_eofS =
		"\1\uffff\1\5\2\uffff\1\5\4\uffff\4\5";
	static final String DFA45_minS =
		"\1\65\1\5\2\65\1\5\2\uffff\2\37\4\5";
	static final String DFA45_maxS =
		"\1\u0082\1\74\2\65\1\72\2\uffff\2\37\1\74\3\72";
	static final String DFA45_acceptS =
		"\5\uffff\1\1\1\2\6\uffff";
	static final String DFA45_specialS =
		"\15\uffff}>";
	static final String[] DFA45_transitionS = {
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

	static final short[] DFA45_eot = DFA.unpackEncodedString(DFA45_eotS);
	static final short[] DFA45_eof = DFA.unpackEncodedString(DFA45_eofS);
	static final char[] DFA45_min = DFA.unpackEncodedStringToUnsignedChars(DFA45_minS);
	static final char[] DFA45_max = DFA.unpackEncodedStringToUnsignedChars(DFA45_maxS);
	static final short[] DFA45_accept = DFA.unpackEncodedString(DFA45_acceptS);
	static final short[] DFA45_special = DFA.unpackEncodedString(DFA45_specialS);
	static final short[][] DFA45_transition;

	static {
		int numStates = DFA45_transitionS.length;
		DFA45_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA45_transition[i] = DFA.unpackEncodedString(DFA45_transitionS[i]);
		}
	}

	protected class DFA45 extends DFA {

		public DFA45(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 45;
			this.eot = DFA45_eot;
			this.eof = DFA45_eof;
			this.min = DFA45_min;
			this.max = DFA45_max;
			this.accept = DFA45_accept;
			this.special = DFA45_special;
			this.transition = DFA45_transition;
		}
		@Override
		public String getDescription() {
			return "198:1: orderby_item : ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) );";
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
	public static final BitSet FOLLOW_join_in_joined_clause719 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fetch_join_in_joined_clause723 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_range_variable_declaration735 = new BitSet(new long[]{0x0020000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_range_variable_declaration738 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration742 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_join771 = new BitSet(new long[]{0x0020000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_join773 = new BitSet(new long[]{0x0020000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_join776 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join780 = new BitSet(new long[]{0x0000000000000002L,0x0008000000000000L});
	public static final BitSet FOLLOW_115_in_join783 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_join785 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_fetch_join816 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_FETCH_in_fetch_join818 = new BitSet(new long[]{0x0020000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join820 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFT_in_join_spec834 = new BitSet(new long[]{0x0000000040080000L});
	public static final BitSet FOLLOW_OUTER_in_join_spec838 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_INNER_in_join_spec844 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_JOIN_in_join_spec849 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression863 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression865 = new BitSet(new long[]{0x0020000000004002L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression868 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression869 = new BitSet(new long[]{0x0020000000004002L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression873 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_125_in_join_association_path_expression908 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression910 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression912 = new BitSet(new long[]{0x0020000000004000L,0x0000000000004000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression915 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_join_association_path_expression916 = new BitSet(new long[]{0x0020000000004000L,0x0000000000004000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression920 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_join_association_path_expression923 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_subtype_in_join_association_path_expression925 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_join_association_path_expression927 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_join_association_path_expression960 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_collection_member_declaration973 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration974 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_declaration976 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration978 = new BitSet(new long[]{0x0020000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_collection_member_declaration981 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration985 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_qualified_identification_variable1014 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_92_in_qualified_identification_variable1022 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_qualified_identification_variable1023 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_qualified_identification_variable1024 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_100_in_map_field_identification_variable1031 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1032 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1033 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_130_in_map_field_identification_variable1037 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1038 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1039 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_path_expression1053 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_path_expression1055 = new BitSet(new long[]{0x0020000000004002L});
	public static final BitSet FOLLOW_field_in_path_expression1058 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_path_expression1059 = new BitSet(new long[]{0x0020000000004002L});
	public static final BitSet FOLLOW_field_in_path_expression1063 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_general_identification_variable1102 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_general_identification_variable1110 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_update_clause1123 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
	public static final BitSet FOLLOW_118_in_update_clause1125 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1127 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_update_clause1130 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1132 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_path_expression_in_update_item1160 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_update_item1162 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6818945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_new_value_in_update_item1164 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_new_value1175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_new_value1183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_111_in_new_value1191 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_delete_clause1203 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_select_clause1230 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6832945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_item_in_select_clause1234 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_select_clause1237 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6832945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_item_in_select_clause1239 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_select_expression_in_select_item1282 = new BitSet(new long[]{0x0020000000000002L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_select_item1286 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_result_variable_in_select_item1290 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_select_expression1303 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1311 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1329 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_select_expression1337 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_113_in_select_expression1345 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_select_expression1347 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1348 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_select_expression1349 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_constructor_expression_in_select_expression1357 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_109_in_constructor_expression1368 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_constructor_name_in_constructor_expression1370 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_constructor_expression1372 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1374 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_constructor_expression1377 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1379 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_constructor_expression1383 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_constructor_item1394 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_constructor_item1402 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_constructor_item1410 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_constructor_item1418 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1429 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1431 = new BitSet(new long[]{0x0020000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1433 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_aggregate_expression1437 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1438 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_aggregate_expression1472 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1474 = new BitSet(new long[]{0x0020000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1476 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_count_argument_in_aggregate_expression1480 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1482 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_aggregate_expression1517 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_count_argument1554 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_count_argument1558 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_132_in_where_clause1571 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_where_clause1573 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_groupby_clause1595 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_groupby_clause1597 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1599 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_groupby_clause1602 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1604 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_path_expression_in_groupby_item1638 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_groupby_item1642 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HAVING_in_having_clause1653 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_having_clause1655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderby_clause1666 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_orderby_clause1668 = new BitSet(new long[]{0x0020000000000000L,0x0000001000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1670 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_orderby_clause1673 = new BitSet(new long[]{0x0020000000000000L,0x0000001000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1675 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1709 = new BitSet(new long[]{0x0000000000000022L});
	public static final BitSet FOLLOW_ASC_in_orderby_item1712 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1744 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_DESC_in_orderby_item1747 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_orderby_variable1776 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_orderby_variable1780 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_orderby_variable1784 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_subquery1798 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_117_in_subquery1800 = new BitSet(new long[]{0x4AA0000607C40A40L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_simple_select_clause_in_subquery1802 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_subquery_from_clause_in_subquery1804 = new BitSet(new long[]{0x000000008000C000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_subquery1807 = new BitSet(new long[]{0x000000008000C000L});
	public static final BitSet FOLLOW_groupby_clause_in_subquery1812 = new BitSet(new long[]{0x0000000080008000L});
	public static final BitSet FOLLOW_having_clause_in_subquery1817 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_subquery1823 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_95_in_subquery_from_clause1873 = new BitSet(new long[]{0x0020000000010000L,0x2000000000000000L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1875 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_subquery_from_clause1878 = new BitSet(new long[]{0x0020000000010000L,0x2000000000000000L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1880 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1918 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1926 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_subselect_identification_variable_declaration1928 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration1930 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_join_in_subselect_identification_variable_declaration1933 = new BitSet(new long[]{0x00000000001A0002L});
	public static final BitSet FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1943 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1954 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_path_expression1955 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_path_expression1956 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1964 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_path_expression1965 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_path_expression1966 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_derived_path_in_general_derived_path1977 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_treated_derived_path_in_general_derived_path1985 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_general_derived_path1987 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_general_derived_path1988 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_simple_derived_path2006 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_125_in_treated_derived_path2023 = new BitSet(new long[]{0x0020000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_general_derived_path_in_treated_derived_path2024 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_78_in_treated_derived_path2026 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_subtype_in_treated_derived_path2028 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_treated_derived_path2030 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_derived_collection_member_declaration2041 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2043 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_collection_member_declaration2044 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2046 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_derived_collection_member_declaration2048 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_collection_member_declaration2051 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause2064 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause2068 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_simple_select_expression2108 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_select_expression2116 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression2124 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_select_expression2132 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_scalar_expression2143 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_scalar_expression2151 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_scalar_expression2159 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_scalar_expression2167 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_scalar_expression2175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_scalar_expression2183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_scalar_expression2191 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2203 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_OR_in_conditional_expression2207 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2209 = new BitSet(new long[]{0x0000000010000002L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2223 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_conditional_term2227 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2229 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_NOT_in_conditional_factor2243 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_primary_in_conditional_factor2247 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_conditional_primary2258 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_conditional_primary2282 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_conditional_primary2283 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_conditional_primary2284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression2295 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_simple_cond_expression2303 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_simple_cond_expression2311 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_simple_cond_expression2319 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression2327 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2335 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression2343 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression2351 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression2359 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2372 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2380 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2388 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2396 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2404 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_70_in_date_between_macro_expression2416 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2418 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2420 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_between_macro_expression2422 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_date_between_macro_expression2424 = new BitSet(new long[]{0x0E00000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2427 = new BitSet(new long[]{0x4000000000040000L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2435 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_between_macro_expression2439 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_date_between_macro_expression2441 = new BitSet(new long[]{0x0E00000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2444 = new BitSet(new long[]{0x4000000000040000L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2452 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_between_macro_expression2456 = new BitSet(new long[]{0x0000000000000000L,0x0010140200800000L,0x0000000000000020L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2458 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2481 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_72_in_date_before_macro_expression2493 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2495 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2497 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_before_macro_expression2499 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2502 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2506 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_date_after_macro_expression2521 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2523 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2525 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_after_macro_expression2527 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2530 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2534 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2537 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_73_in_date_equals_macro_expression2549 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2551 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2553 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_date_equals_macro_expression2555 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2558 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2562 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2565 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_74_in_date_today_macro_expression2577 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2579 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2581 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2583 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2596 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2599 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_between_expression2603 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2605 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2607 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2609 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2617 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2620 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_between_expression2624 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2626 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2628 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2630 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2638 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2641 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_between_expression2645 = new BitSet(new long[]{0x00A0000407800240L,0x0001000100760020L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2647 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2649 = new BitSet(new long[]{0x00A0000407800240L,0x0001000100760020L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2651 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2663 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_type_discriminator_in_in_expression2667 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_identification_variable_in_in_expression2671 = new BitSet(new long[]{0x0000000008010000L});
	public static final BitSet FOLLOW_NOT_in_in_expression2675 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_IN_in_in_expression2679 = new BitSet(new long[]{0x0080000004800000L,0x0000000000000020L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2695 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_in_item_in_in_expression2697 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_in_expression2700 = new BitSet(new long[]{0x00A0000004000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_in_item_in_in_expression2702 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2706 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_in_expression2722 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_valued_input_parameter_in_in_expression2738 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2754 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2756 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2758 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_in_item2786 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_single_valued_input_parameter_in_in_item2790 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2801 = new BitSet(new long[]{0x0000000008000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_NOT_in_like_expression2804 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_103_in_like_expression2808 = new BitSet(new long[]{0x0080000204000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_pattern_value_in_like_expression2811 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
	public static final BitSet FOLLOW_input_parameter_in_like_expression2815 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
	public static final BitSet FOLLOW_93_in_like_expression2818 = new BitSet(new long[]{0x0000000A00000000L});
	public static final BitSet FOLLOW_escape_character_in_like_expression2820 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_null_comparison_expression2834 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression2838 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_null_comparison_expression2842 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_99_in_null_comparison_expression2845 = new BitSet(new long[]{0x0000000008000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_NOT_in_null_comparison_expression2848 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_111_in_null_comparison_expression2852 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression2863 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_99_in_empty_collection_comparison_expression2865 = new BitSet(new long[]{0x0000000008000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_NOT_in_empty_collection_comparison_expression2868 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_90_in_empty_collection_comparison_expression2872 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_or_value_expression_in_collection_member_expression2883 = new BitSet(new long[]{0x0000000008000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_NOT_in_collection_member_expression2887 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_105_in_collection_member_expression2891 = new BitSet(new long[]{0x0020000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_114_in_collection_member_expression2894 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_expression2898 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_or_value_expression2909 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2917 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_entity_or_value_expression2925 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_or_value_expression2936 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_or_value_expression2944 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simple_entity_or_value_expression2952 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_exists_expression2964 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_94_in_exists_expression2968 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_subquery_in_exists_expression2970 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_all_or_any_expression2981 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_subquery_in_all_or_any_expression2994 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3005 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3007 = new BitSet(new long[]{0x00A0000607C00240L,0x45010001000E3020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3010 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3014 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3023 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3025 = new BitSet(new long[]{0x00A0000004800000L,0x0101000100063020L,0x00000000000000C0L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3034 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3038 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3047 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3049 = new BitSet(new long[]{0x00A0000004800000L,0x0101000000063020L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3056 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3060 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3069 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3071 = new BitSet(new long[]{0x00A0000407800240L,0x0101000100763020L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3074 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3078 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3087 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3089 = new BitSet(new long[]{0x00A0000004000000L,0x0100000000003020L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3098 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3102 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3111 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_comparison_expression3113 = new BitSet(new long[]{0x00A0000004000000L,0x8000000000000020L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3121 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3129 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3131 = new BitSet(new long[]{0x4AA0000407840240L,0x0381094500063820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3134 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3138 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3202 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3210 = new BitSet(new long[]{0x0A00000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_expression3212 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3231 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3239 = new BitSet(new long[]{0x2100000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_term3241 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3250 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor3273 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_arithmetic_primary3284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary3292 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary3300 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_arithmetic_primary3301 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary3302 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary3310 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary3318 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary3326 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_arithmetic_primary3334 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_arithmetic_primary3342 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_arithmetic_primary3350 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_string_expression3361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_string_expression3369 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_string_expression3377 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_strings_in_string_expression3385 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_string_expression3393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_string_expression3401 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_string_expression3409 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_string_expression3417 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_datetime_expression3428 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_datetime_expression3436 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_expression3444 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_datetime_expression3452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_datetime_expression3460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_datetime_expression3468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_datetime_expression3476 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_datetime_expression3484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_boolean_expression3495 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_literal_in_boolean_expression3503 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_boolean_expression3511 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_boolean_expression3519 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_boolean_expression3527 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_boolean_expression3535 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_enum_expression3546 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_literal_in_enum_expression3554 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_enum_expression3562 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_enum_expression3570 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_enum_expression3578 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_expression3589 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3597 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3608 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3616 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_entity_type_expression3627 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_literal_in_entity_type_expression3635 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_entity_type_expression3643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_127_in_type_discriminator3654 = new BitSet(new long[]{0x00A0000004000000L,0x0000001000000020L,0x0000000000000004L});
	public static final BitSet FOLLOW_general_identification_variable_in_type_discriminator3657 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_path_expression_in_type_discriminator3661 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_input_parameter_in_type_discriminator3665 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_type_discriminator3668 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_functions_returning_numerics3679 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3680 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3681 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_104_in_functions_returning_numerics3689 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3691 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_numerics3692 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3694 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_numerics3696 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3697 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3700 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_functions_returning_numerics3708 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3709 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3710 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_121_in_functions_returning_numerics3718 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3719 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3720 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_107_in_functions_returning_numerics3728 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3729 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_numerics3730 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3732 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3733 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_119_in_functions_returning_numerics3741 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3742 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3743 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_98_in_functions_returning_numerics3751 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_functions_returning_numerics3752 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3753 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_83_in_functions_returning_strings3791 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3792 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3793 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3795 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3798 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3800 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3803 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_122_in_functions_returning_strings3811 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3813 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3814 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3816 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_functions_returning_strings3819 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3821 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3824 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_126_in_functions_returning_strings3832 = new BitSet(new long[]{0x00A0000E07C00240L,0x54010021800F0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings3835 = new BitSet(new long[]{0x0000000800000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_trim_character_in_functions_returning_strings3840 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_functions_returning_strings3844 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3848 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3850 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LOWER_in_functions_returning_strings3858 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3860 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3861 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3862 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_129_in_functions_returning_strings3870 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3871 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3872 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_96_in_function_invocation3902 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_function_name_in_function_invocation3903 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_58_in_function_invocation3906 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_function_arg_in_function_invocation3908 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_function_invocation3912 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_function_arg3923 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_function_arg3931 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_function_arg3939 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_function_arg3947 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_case_expression_in_case_expression3958 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_case_expression_in_case_expression3966 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coalesce_expression_in_case_expression3974 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullif_expression_in_case_expression3982 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_general_case_expression3993 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression3995 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression3998 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_89_in_general_case_expression4002 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_general_case_expression4004 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_91_in_general_case_expression4006 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_when_clause4017 = new BitSet(new long[]{0x4AA000060FC40240L,0xE6810945407E0FE0L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_when_clause4019 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_when_clause4021 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_when_clause4023 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_simple_case_expression4034 = new BitSet(new long[]{0x0020000000000000L,0x8000000000000000L});
	public static final BitSet FOLLOW_case_operand_in_simple_case_expression4036 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4038 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4041 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_89_in_simple_case_expression4045 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_case_expression4047 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_91_in_simple_case_expression4049 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_case_operand4060 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_case_operand4068 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_simple_when_clause4079 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4081 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_simple_when_clause4083 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4085 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_82_in_coalesce_expression4096 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4097 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_coalesce_expression4100 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4102 = new BitSet(new long[]{0x0400000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_coalesce_expression4105 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_112_in_nullif_expression4116 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4117 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_nullif_expression4119 = new BitSet(new long[]{0x4AA0000607C40240L,0xC6810945007E0820L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4121 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_nullif_expression4122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_69_in_input_parameter4135 = new BitSet(new long[]{0x4000000000040000L});
	public static final BitSet FOLLOW_numeric_literal_in_input_parameter4137 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter4160 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_55_in_input_parameter4181 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_WORD_in_input_parameter4183 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_136_in_input_parameter4185 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_literal4213 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4225 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_literal4237 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_identification_variable4286 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4298 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_60_in_parameter_name4301 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4304 = new BitSet(new long[]{0x1000000000000002L});
	public static final BitSet FOLLOW_TRIM_CHARACTER_in_trim_character4334 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string_literal4345 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_62_in_numeric_literal4357 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal4361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_object_field4373 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_embeddable_object_field4384 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_collection_valued_field4395 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_name4406 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_subtype4417 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_type_literal4428 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_function_name4439 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_state_field4450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_result_variable4461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_superquery_identification_variable4472 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_date_time_timestamp_literal4483 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_pattern_value4494 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_collection_valued_input_parameter4505 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_single_valued_input_parameter4516 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred20_JPA2873 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred29_JPA21063 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred32_JPA21175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_synpred33_JPA21183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred38_JPA21303 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred39_JPA21311 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred40_JPA21329 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred41_JPA21337 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred44_JPA21394 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred45_JPA21402 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred46_JPA21410 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_synpred48_JPA21429 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred48_JPA21431 = new BitSet(new long[]{0x0020000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred48_JPA21433 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_path_expression_in_synpred48_JPA21437 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred48_JPA21438 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_synpred50_JPA21472 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred50_JPA21474 = new BitSet(new long[]{0x0020000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred50_JPA21476 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_count_argument_in_synpred50_JPA21480 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred50_JPA21482 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_synpred62_JPA21780 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_synpred70_JPA21954 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_60_in_synpred70_JPA21955 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_synpred70_JPA21956 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred75_JPA22108 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred76_JPA22116 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred77_JPA22124 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred78_JPA22143 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred79_JPA22151 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred80_JPA22159 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred81_JPA22167 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred82_JPA22175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_synpred83_JPA22183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_synpred86_JPA22243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_synpred87_JPA22258 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_synpred88_JPA22295 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_synpred89_JPA22303 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_synpred90_JPA22311 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_synpred91_JPA22319 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_synpred92_JPA22327 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred93_JPA22335 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_synpred94_JPA22343 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred113_JPA22596 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_synpred113_JPA22599 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_synpred113_JPA22603 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred113_JPA22605 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred113_JPA22607 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred113_JPA22609 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred115_JPA22617 = new BitSet(new long[]{0x0000000008000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_NOT_in_synpred115_JPA22620 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_synpred115_JPA22624 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred115_JPA22626 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred115_JPA22628 = new BitSet(new long[]{0x00A0000607C00240L,0x44010001000E0020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred115_JPA22630 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred128_JPA22834 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred136_JPA22936 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred142_JPA23005 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred142_JPA23007 = new BitSet(new long[]{0x00A0000607C00240L,0x45010001000E3020L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred142_JPA23010 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred142_JPA23014 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred145_JPA23023 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred145_JPA23025 = new BitSet(new long[]{0x00A0000004800000L,0x0101000100063020L,0x00000000000000C0L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred145_JPA23034 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred145_JPA23038 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred148_JPA23047 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred148_JPA23049 = new BitSet(new long[]{0x00A0000004800000L,0x0101000000063020L});
	public static final BitSet FOLLOW_enum_expression_in_synpred148_JPA23056 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred148_JPA23060 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred150_JPA23069 = new BitSet(new long[]{0x8000000000000000L,0x000000000000001FL});
	public static final BitSet FOLLOW_comparison_operator_in_synpred150_JPA23071 = new BitSet(new long[]{0x00A0000407800240L,0x0101000100763020L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred150_JPA23074 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred150_JPA23078 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred153_JPA23087 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred153_JPA23089 = new BitSet(new long[]{0x00A0000004000000L,0x0100000000003020L});
	public static final BitSet FOLLOW_entity_expression_in_synpred153_JPA23098 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred153_JPA23102 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred155_JPA23111 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
	public static final BitSet FOLLOW_set_in_synpred155_JPA23113 = new BitSet(new long[]{0x00A0000004000000L,0x8000000000000020L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred155_JPA23121 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred162_JPA23202 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred164_JPA23231 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred170_JPA23300 = new BitSet(new long[]{0x4AA0000407840240L,0x0281094500060820L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred170_JPA23301 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred170_JPA23302 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred173_JPA23326 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred175_JPA23342 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred180_JPA23393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred182_JPA23409 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred183_JPA23428 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred186_JPA23452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred188_JPA23468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_synpred189_JPA23476 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred226_JPA23923 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_synpred228_JPA23939 = new BitSet(new long[]{0x0000000000000002L});
}
