// $ANTLR 3.5.2 JPA2.g 2018-09-12 09:43:16

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
		"'UPDATE'", "'UPPER('", "'USER_TIMEZONE'", "'VALUE('", "'WEEK'", "'WHERE'", 
		"'YEAR'", "'false'", "'true'", "'}'"
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
	public static final int T__147=147;
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
	// JPA2.g:90:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? EOF -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
	public final JPA2Parser.select_statement_return select_statement() throws RecognitionException {
		JPA2Parser.select_statement_return retval = new JPA2Parser.select_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token sl=null;
		Token EOF10=null;
		ParserRuleReturnScope select_clause4 =null;
		ParserRuleReturnScope from_clause5 =null;
		ParserRuleReturnScope where_clause6 =null;
		ParserRuleReturnScope groupby_clause7 =null;
		ParserRuleReturnScope having_clause8 =null;
		ParserRuleReturnScope orderby_clause9 =null;

		Object sl_tree=null;
		Object EOF10_tree=null;
		RewriteRuleTokenStream stream_129=new RewriteRuleTokenStream(adaptor,"token 129");
		RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
		RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
		RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");

		try {
			// JPA2.g:91:6: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? EOF -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
			// JPA2.g:91:8: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? EOF
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
			if ( (LA2_0==143) ) {
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

			EOF10=(Token)match(input,EOF,FOLLOW_EOF_in_select_statement560); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_EOF.add(EOF10);

			// AST REWRITE
			// elements: orderby_clause, from_clause, groupby_clause, having_clause, where_clause, select_clause
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
		ParserRuleReturnScope update_clause11 =null;
		ParserRuleReturnScope where_clause12 =null;

		Object up_tree=null;
		RewriteRuleTokenStream stream_138=new RewriteRuleTokenStream(adaptor,"token 138");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_update_clause=new RewriteRuleSubtreeStream(adaptor,"rule update_clause");

		try {
			// JPA2.g:95:5: (up= 'UPDATE' update_clause ( where_clause )? -> ^( T_QUERY[$up] update_clause ( where_clause )? ) )
			// JPA2.g:95:7: up= 'UPDATE' update_clause ( where_clause )?
			{
			up=(Token)match(input,138,FOLLOW_138_in_update_statement616); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_138.add(up);

			pushFollow(FOLLOW_update_clause_in_update_statement618);
			update_clause11=update_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_clause.add(update_clause11.getTree());
			// JPA2.g:95:33: ( where_clause )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==143) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// JPA2.g:95:34: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_update_statement621);
					where_clause12=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause12.getTree());
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
		ParserRuleReturnScope delete_clause13 =null;
		ParserRuleReturnScope where_clause14 =null;

		Object dl_tree=null;
		RewriteRuleTokenStream stream_96=new RewriteRuleTokenStream(adaptor,"token 96");
		RewriteRuleSubtreeStream stream_delete_clause=new RewriteRuleSubtreeStream(adaptor,"rule delete_clause");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");

		try {
			// JPA2.g:98:5: (dl= 'DELETE' delete_clause ( where_clause )? -> ^( T_QUERY[$dl] delete_clause ( where_clause )? ) )
			// JPA2.g:98:7: dl= 'DELETE' delete_clause ( where_clause )?
			{
			dl=(Token)match(input,96,FOLLOW_96_in_delete_statement657); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_96.add(dl);

			pushFollow(FOLLOW_delete_clause_in_delete_statement659);
			delete_clause13=delete_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_delete_clause.add(delete_clause13.getTree());
			// JPA2.g:98:33: ( where_clause )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==143) ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					// JPA2.g:98:34: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_delete_statement662);
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
		Token char_literal16=null;
		ParserRuleReturnScope identification_variable_declaration15 =null;
		ParserRuleReturnScope identification_variable_declaration_or_collection_member_declaration17 =null;

		Object fr_tree=null;
		Object char_literal16_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_103=new RewriteRuleTokenStream(adaptor,"token 103");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
		RewriteRuleSubtreeStream stream_identification_variable_declaration_or_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration_or_collection_member_declaration");

		try {
			// JPA2.g:102:6: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) )
			// JPA2.g:102:8: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )*
			{
			fr=(Token)match(input,103,FOLLOW_103_in_from_clause700); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_103.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_from_clause702);
			identification_variable_declaration15=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration15.getTree());
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
					char_literal16=(Token)match(input,66,FOLLOW_66_in_from_clause705); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal16);

					pushFollow(FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause707);
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

		ParserRuleReturnScope identification_variable_declaration18 =null;
		ParserRuleReturnScope collection_member_declaration19 =null;

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


					pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration741);
					identification_variable_declaration18=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration18.getTree());

					}
					break;
				case 2 :
					// JPA2.g:106:8: collection_member_declaration
					{
					pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration750);
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

		ParserRuleReturnScope range_variable_declaration20 =null;
		ParserRuleReturnScope joined_clause21 =null;

		RewriteRuleSubtreeStream stream_range_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration");
		RewriteRuleSubtreeStream stream_joined_clause=new RewriteRuleSubtreeStream(adaptor,"rule joined_clause");

		try {
			// JPA2.g:109:6: ( range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) )
			// JPA2.g:109:8: range_variable_declaration ( joined_clause )*
			{
			pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration774);
			range_variable_declaration20=range_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range_variable_declaration.add(range_variable_declaration20.getTree());
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
					pushFollow(FOLLOW_joined_clause_in_identification_variable_declaration776);
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

		ParserRuleReturnScope joined_clause22 =null;


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
					pushFollow(FOLLOW_joined_clause_in_join_section807);
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
	// JPA2.g:113:1: joined_clause : ( join | fetch_join );
	public final JPA2Parser.joined_clause_return joined_clause() throws RecognitionException {
		JPA2Parser.joined_clause_return retval = new JPA2Parser.joined_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope join23 =null;
		ParserRuleReturnScope fetch_join24 =null;


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


					pushFollow(FOLLOW_join_in_joined_clause815);
					join23=join();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join23.getTree());

					}
					break;
				case 2 :
					// JPA2.g:113:24: fetch_join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_fetch_join_in_joined_clause819);
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
	// JPA2.g:114:1: range_variable_declaration : entity_name ( AS )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) ;
	public final JPA2Parser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
		JPA2Parser.range_variable_declaration_return retval = new JPA2Parser.range_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AS26=null;
		ParserRuleReturnScope entity_name25 =null;
		ParserRuleReturnScope identification_variable27 =null;

		Object AS26_tree=null;
		RewriteRuleTokenStream stream_AS=new RewriteRuleTokenStream(adaptor,"token AS");
		RewriteRuleSubtreeStream stream_entity_name=new RewriteRuleSubtreeStream(adaptor,"rule entity_name");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:115:6: ( entity_name ( AS )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) )
			// JPA2.g:115:8: entity_name ( AS )? identification_variable
			{
			pushFollow(FOLLOW_entity_name_in_range_variable_declaration831);
			entity_name25=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_entity_name.add(entity_name25.getTree());
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
					AS26=(Token)match(input,AS,FOLLOW_AS_in_range_variable_declaration834); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS26);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_range_variable_declaration838);
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
			// 116:6: -> ^( T_ID_VAR[$identification_variable.text] entity_name )
			{
				// JPA2.g:116:9: ^( T_ID_VAR[$identification_variable.text] entity_name )
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
	// JPA2.g:117:1: join : join_spec join_association_path_expression ( AS )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? ) ;
	public final JPA2Parser.join_return join() throws RecognitionException {
		JPA2Parser.join_return retval = new JPA2Parser.join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AS30=null;
		Token string_literal32=null;
		ParserRuleReturnScope join_spec28 =null;
		ParserRuleReturnScope join_association_path_expression29 =null;
		ParserRuleReturnScope identification_variable31 =null;
		ParserRuleReturnScope conditional_expression33 =null;

		Object AS30_tree=null;
		Object string_literal32_tree=null;
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
			pushFollow(FOLLOW_join_spec_in_join867);
			join_spec28=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_spec.add(join_spec28.getTree());
			pushFollow(FOLLOW_join_association_path_expression_in_join869);
			join_association_path_expression29=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression29.getTree());
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
					AS30=(Token)match(input,AS,FOLLOW_AS_in_join872); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS30);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_join876);
			identification_variable31=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable31.getTree());
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
					string_literal32=(Token)match(input,125,FOLLOW_125_in_join879); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_125.add(string_literal32);

					pushFollow(FOLLOW_conditional_expression_in_join881);
					conditional_expression33=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression33.getTree());
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
			// 119:6: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? )
			{
				// JPA2.g:119:9: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ( conditional_expression )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new JoinVariableNode(T_JOIN_VAR, (join_spec28!=null?input.toString(join_spec28.start,join_spec28.stop):null), (identification_variable31!=null?input.toString(identification_variable31.start,identification_variable31.stop):null)), root_1);
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

		Token string_literal35=null;
		ParserRuleReturnScope join_spec34 =null;
		ParserRuleReturnScope join_association_path_expression36 =null;

		Object string_literal35_tree=null;

		try {
			// JPA2.g:121:6: ( join_spec 'FETCH' join_association_path_expression )
			// JPA2.g:121:8: join_spec 'FETCH' join_association_path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_join_spec_in_fetch_join915);
			join_spec34=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec34.getTree());

			string_literal35=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join917); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal35_tree = (Object)adaptor.create(string_literal35);
			adaptor.addChild(root_0, string_literal35_tree);
			}

			pushFollow(FOLLOW_join_association_path_expression_in_fetch_join919);
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
	// JPA2.g:122:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
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
					string_literal37=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec933); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal37_tree = (Object)adaptor.create(string_literal37);
					adaptor.addChild(root_0, string_literal37_tree);
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
							string_literal38=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec937); if (state.failed) return retval;
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
					// JPA2.g:123:31: 'INNER'
					{
					string_literal39=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec943); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal39_tree = (Object)adaptor.create(string_literal39);
					adaptor.addChild(root_0, string_literal39_tree);
					}

					}
					break;

			}

			string_literal40=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec948); if (state.failed) return retval;
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
	// JPA2.g:126:1: join_association_path_expression : ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? AS subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name );
	public final JPA2Parser.join_association_path_expression_return join_association_path_expression() throws RecognitionException {
		JPA2Parser.join_association_path_expression_return retval = new JPA2Parser.join_association_path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal42=null;
		Token char_literal44=null;
		Token string_literal46=null;
		Token char_literal48=null;
		Token char_literal50=null;
		Token AS52=null;
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
		Object AS52_tree=null;
		Object char_literal54_tree=null;
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
				else if ( (LA22_1==EOF||LA22_1==AS||(LA22_1 >= GROUP && LA22_1 <= HAVING)||LA22_1==INNER||(LA22_1 >= JOIN && LA22_1 <= LEFT)||LA22_1==ORDER||LA22_1==RPAREN||LA22_1==SET||LA22_1==WORD||LA22_1==66||LA22_1==107||LA22_1==143) ) {
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
					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression962);
					identification_variable41=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable41.getTree());
					char_literal42=(Token)match(input,68,FOLLOW_68_in_join_association_path_expression964); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_68.add(char_literal42);

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
						case 123:
							{
							int LA18_14 = input.LA(2);
							if ( (LA18_14==68) ) {
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
						case 142:
						case 144:
							{
							int LA18_15 = input.LA(2);
							if ( (LA18_15==68) ) {
								alt18=1;
							}

							}
							break;
						}
						switch (alt18) {
						case 1 :
							// JPA2.g:127:37: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression967);
							field43=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field43.getTree());
							char_literal44=(Token)match(input,68,FOLLOW_68_in_join_association_path_expression968); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_68.add(char_literal44);

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
						case 123:
						case 126:
						case 128:
						case 129:
						case 142:
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
							if ( (LA19_4==EOF||LA19_4==AS||(LA19_4 >= GROUP && LA19_4 <= HAVING)||LA19_4==INNER||(LA19_4 >= JOIN && LA19_4 <= LEFT)||LA19_4==ORDER||LA19_4==RPAREN||LA19_4==SET||LA19_4==WORD||LA19_4==66||LA19_4==107||LA19_4==143) ) {
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
							pushFollow(FOLLOW_field_in_join_association_path_expression972);
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
					// 128:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:128:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable41!=null?input.toString(identification_variable41.start,identification_variable41.stop):null)), root_1);
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
					string_literal46=(Token)match(input,135,FOLLOW_135_in_join_association_path_expression1007); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_135.add(string_literal46);

					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression1009);
					identification_variable47=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable47.getTree());
					char_literal48=(Token)match(input,68,FOLLOW_68_in_join_association_path_expression1011); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_68.add(char_literal48);

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
						case 123:
							{
							int LA20_14 = input.LA(2);
							if ( (LA20_14==68) ) {
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
						case 142:
						case 144:
							{
							int LA20_15 = input.LA(2);
							if ( (LA20_15==68) ) {
								alt20=1;
							}

							}
							break;
						}
						switch (alt20) {
						case 1 :
							// JPA2.g:129:47: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression1014);
							field49=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field49.getTree());
							char_literal50=(Token)match(input,68,FOLLOW_68_in_join_association_path_expression1015); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_68.add(char_literal50);

							}
							break;

						default :
							break loop20;
						}
					}

					// JPA2.g:129:58: ( field )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( (LA21_0==AVG||LA21_0==CASE||LA21_0==COUNT||LA21_0==GROUP||(LA21_0 >= MAX && LA21_0 <= MIN)||LA21_0==ORDER||LA21_0==SUM||LA21_0==WORD||LA21_0==95||LA21_0==99||LA21_0==103||LA21_0==105||(LA21_0 >= 113 && LA21_0 <= 114)||LA21_0==116||LA21_0==123||LA21_0==126||(LA21_0 >= 128 && LA21_0 <= 129)||LA21_0==142||LA21_0==144) ) {
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
							pushFollow(FOLLOW_field_in_join_association_path_expression1019);
							field51=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field51.getTree());
							}
							break;

					}

					AS52=(Token)match(input,AS,FOLLOW_AS_in_join_association_path_expression1022); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS52);

					pushFollow(FOLLOW_subtype_in_join_association_path_expression1024);
					subtype53=subtype();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subtype.add(subtype53.getTree());
					char_literal54=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_join_association_path_expression1026); if (state.failed) return retval; 
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
					// 130:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:130:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable47!=null?input.toString(identification_variable47.start,identification_variable47.stop):null)), root_1);
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


					pushFollow(FOLLOW_entity_name_in_join_association_path_expression1059);
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
	// JPA2.g:134:1: collection_member_declaration : 'IN' '(' path_expression ')' ( AS )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
	public final JPA2Parser.collection_member_declaration_return collection_member_declaration() throws RecognitionException {
		JPA2Parser.collection_member_declaration_return retval = new JPA2Parser.collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal56=null;
		Token char_literal57=null;
		Token char_literal59=null;
		Token AS60=null;
		ParserRuleReturnScope path_expression58 =null;
		ParserRuleReturnScope identification_variable61 =null;

		Object string_literal56_tree=null;
		Object char_literal57_tree=null;
		Object char_literal59_tree=null;
		Object AS60_tree=null;
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
			string_literal56=(Token)match(input,IN,FOLLOW_IN_in_collection_member_declaration1072); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_IN.add(string_literal56);

			char_literal57=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration1073); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal57);

			pushFollow(FOLLOW_path_expression_in_collection_member_declaration1075);
			path_expression58=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_path_expression.add(path_expression58.getTree());
			char_literal59=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration1077); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal59);

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
					AS60=(Token)match(input,AS,FOLLOW_AS_in_collection_member_declaration1080); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AS.add(AS60);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_collection_member_declaration1084);
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
			// 136:5: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
			{
				// JPA2.g:136:8: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
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
	// JPA2.g:138:1: qualified_identification_variable : ( map_field_identification_variable | 'ENTRY(' identification_variable ')' );
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
			// JPA2.g:139:5: ( map_field_identification_variable | 'ENTRY(' identification_variable ')' )
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==108||LA24_0==141) ) {
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


					pushFollow(FOLLOW_map_field_identification_variable_in_qualified_identification_variable1113);
					map_field_identification_variable62=map_field_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map_field_identification_variable62.getTree());

					}
					break;
				case 2 :
					// JPA2.g:140:7: 'ENTRY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal63=(Token)match(input,98,FOLLOW_98_in_qualified_identification_variable1121); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal63_tree = (Object)adaptor.create(string_literal63);
					adaptor.addChild(root_0, string_literal63_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_qualified_identification_variable1122);
					identification_variable64=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable64.getTree());

					char_literal65=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_qualified_identification_variable1123); if (state.failed) return retval;
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
	// JPA2.g:141:1: map_field_identification_variable : ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' );
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
			// JPA2.g:141:35: ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' )
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==108) ) {
				alt25=1;
			}
			else if ( (LA25_0==141) ) {
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


					string_literal66=(Token)match(input,108,FOLLOW_108_in_map_field_identification_variable1130); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal66_tree = (Object)adaptor.create(string_literal66);
					adaptor.addChild(root_0, string_literal66_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1131);
					identification_variable67=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable67.getTree());

					char_literal68=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1132); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal68_tree = (Object)adaptor.create(char_literal68);
					adaptor.addChild(root_0, char_literal68_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:141:72: 'VALUE(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal69=(Token)match(input,141,FOLLOW_141_in_map_field_identification_variable1136); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal69_tree = (Object)adaptor.create(string_literal69);
					adaptor.addChild(root_0, string_literal69_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable1137);
					identification_variable70=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable70.getTree());

					char_literal71=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable1138); if (state.failed) return retval;
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
	// JPA2.g:144:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
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
		RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:145:5: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
			// JPA2.g:145:8: identification_variable '.' ( field '.' )* ( field )?
			{
			pushFollow(FOLLOW_identification_variable_in_path_expression1152);
			identification_variable72=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable72.getTree());
			char_literal73=(Token)match(input,68,FOLLOW_68_in_path_expression1154); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_68.add(char_literal73);

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
				case 123:
					{
					int LA26_14 = input.LA(2);
					if ( (LA26_14==68) ) {
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
				case 142:
				case 144:
					{
					int LA26_15 = input.LA(2);
					if ( (LA26_15==68) ) {
						alt26=1;
					}

					}
					break;
				}
				switch (alt26) {
				case 1 :
					// JPA2.g:145:37: field '.'
					{
					pushFollow(FOLLOW_field_in_path_expression1157);
					field74=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field74.getTree());
					char_literal75=(Token)match(input,68,FOLLOW_68_in_path_expression1158); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_68.add(char_literal75);

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
				case 123:
				case 126:
				case 128:
				case 129:
				case 142:
				case 144:
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
						case 143:
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
					if ( (LA27_4==EOF||(LA27_4 >= AND && LA27_4 <= ASC)||LA27_4==DESC||(LA27_4 >= ELSE && LA27_4 <= END)||(LA27_4 >= GROUP && LA27_4 <= INNER)||(LA27_4 >= JOIN && LA27_4 <= LEFT)||(LA27_4 >= NOT && LA27_4 <= ORDER)||LA27_4==RPAREN||LA27_4==SET||LA27_4==THEN||(LA27_4 >= WHEN && LA27_4 <= WORD)||(LA27_4 >= 64 && LA27_4 <= 67)||LA27_4==69||(LA27_4 >= 71 && LA27_4 <= 76)||LA27_4==87||LA27_4==100||LA27_4==103||LA27_4==107||LA27_4==111||LA27_4==113||(LA27_4 >= 121 && LA27_4 <= 122)||LA27_4==127||LA27_4==143) ) {
						alt27=1;
					}
					}
					break;
				case ORDER:
					{
					int LA27_5 = input.LA(2);
					if ( (LA27_5==EOF||(LA27_5 >= AND && LA27_5 <= ASC)||LA27_5==DESC||(LA27_5 >= ELSE && LA27_5 <= END)||(LA27_5 >= GROUP && LA27_5 <= INNER)||(LA27_5 >= JOIN && LA27_5 <= LEFT)||(LA27_5 >= NOT && LA27_5 <= ORDER)||LA27_5==RPAREN||LA27_5==SET||LA27_5==THEN||(LA27_5 >= WHEN && LA27_5 <= WORD)||(LA27_5 >= 64 && LA27_5 <= 67)||LA27_5==69||(LA27_5 >= 71 && LA27_5 <= 76)||LA27_5==87||LA27_5==100||LA27_5==103||LA27_5==107||LA27_5==111||LA27_5==113||(LA27_5 >= 121 && LA27_5 <= 122)||LA27_5==127||LA27_5==143) ) {
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
						case 143:
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
					pushFollow(FOLLOW_field_in_path_expression1162);
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
			// 146:5: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
			{
				// JPA2.g:146:8: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable72!=null?input.toString(identification_variable72.start,identification_variable72.stop):null)), root_1);
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

		ParserRuleReturnScope identification_variable77 =null;
		ParserRuleReturnScope map_field_identification_variable78 =null;


		try {
			// JPA2.g:152:5: ( identification_variable | map_field_identification_variable )
			int alt28=2;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==GROUP||LA28_0==WORD) ) {
				alt28=1;
			}
			else if ( (LA28_0==108||LA28_0==141) ) {
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


					pushFollow(FOLLOW_identification_variable_in_general_identification_variable1201);
					identification_variable77=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable77.getTree());

					}
					break;
				case 2 :
					// JPA2.g:153:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_general_identification_variable1209);
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
	// JPA2.g:156:1: update_clause : identification_variable_declaration SET update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* ) ;
	public final JPA2Parser.update_clause_return update_clause() throws RecognitionException {
		JPA2Parser.update_clause_return retval = new JPA2Parser.update_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token SET80=null;
		Token char_literal82=null;
		ParserRuleReturnScope identification_variable_declaration79 =null;
		ParserRuleReturnScope update_item81 =null;
		ParserRuleReturnScope update_item83 =null;

		Object SET80_tree=null;
		Object char_literal82_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_SET=new RewriteRuleTokenStream(adaptor,"token SET");
		RewriteRuleSubtreeStream stream_update_item=new RewriteRuleSubtreeStream(adaptor,"rule update_item");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:157:5: ( identification_variable_declaration SET update_item ( ',' update_item )* -> ^( T_SOURCES identification_variable_declaration SET update_item ( ',' update_item )* ) )
			// JPA2.g:157:7: identification_variable_declaration SET update_item ( ',' update_item )*
			{
			pushFollow(FOLLOW_identification_variable_declaration_in_update_clause1222);
			identification_variable_declaration79=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration79.getTree());
			SET80=(Token)match(input,SET,FOLLOW_SET_in_update_clause1224); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_SET.add(SET80);

			pushFollow(FOLLOW_update_item_in_update_clause1226);
			update_item81=update_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_update_item.add(update_item81.getTree());
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
					char_literal82=(Token)match(input,66,FOLLOW_66_in_update_clause1229); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal82);

					pushFollow(FOLLOW_update_item_in_update_clause1231);
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
			// elements: identification_variable_declaration, 66, SET, update_item, update_item
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
				while ( stream_66.hasNext()||stream_update_item.hasNext() ) {
					adaptor.addChild(root_1, stream_66.nextNode());
					adaptor.addChild(root_1, stream_update_item.nextTree());
				}
				stream_66.reset();
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
	// JPA2.g:159:1: update_item : path_expression '=' new_value ;
	public final JPA2Parser.update_item_return update_item() throws RecognitionException {
		JPA2Parser.update_item_return retval = new JPA2Parser.update_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal85=null;
		ParserRuleReturnScope path_expression84 =null;
		ParserRuleReturnScope new_value86 =null;

		Object char_literal85_tree=null;

		try {
			// JPA2.g:160:5: ( path_expression '=' new_value )
			// JPA2.g:160:7: path_expression '=' new_value
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_update_item1273);
			path_expression84=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression84.getTree());

			char_literal85=(Token)match(input,74,FOLLOW_74_in_update_item1275); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal85_tree = (Object)adaptor.create(char_literal85);
			adaptor.addChild(root_0, char_literal85_tree);
			}

			pushFollow(FOLLOW_new_value_in_update_item1277);
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
	// JPA2.g:161:1: new_value : ( scalar_expression | simple_entity_expression | 'NULL' );
	public final JPA2Parser.new_value_return new_value() throws RecognitionException {
		JPA2Parser.new_value_return retval = new JPA2Parser.new_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal89=null;
		ParserRuleReturnScope scalar_expression87 =null;
		ParserRuleReturnScope simple_entity_expression88 =null;

		Object string_literal89_tree=null;

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
				if ( (LA30_6==68) ) {
					alt30=1;
				}
				else if ( (LA30_6==EOF||LA30_6==66||LA30_6==143) ) {
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


					pushFollow(FOLLOW_scalar_expression_in_new_value1288);
					scalar_expression87=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression87.getTree());

					}
					break;
				case 2 :
					// JPA2.g:163:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_new_value1296);
					simple_entity_expression88=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression88.getTree());

					}
					break;
				case 3 :
					// JPA2.g:164:7: 'NULL'
					{
					root_0 = (Object)adaptor.nil();


					string_literal89=(Token)match(input,119,FOLLOW_119_in_new_value1304); if (state.failed) return retval;
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
	// JPA2.g:166:1: delete_clause : fr= 'FROM' identification_variable_declaration -> ^( T_SOURCES[$fr] identification_variable_declaration ) ;
	public final JPA2Parser.delete_clause_return delete_clause() throws RecognitionException {
		JPA2Parser.delete_clause_return retval = new JPA2Parser.delete_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token fr=null;
		ParserRuleReturnScope identification_variable_declaration90 =null;

		Object fr_tree=null;
		RewriteRuleTokenStream stream_103=new RewriteRuleTokenStream(adaptor,"token 103");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");

		try {
			// JPA2.g:167:5: (fr= 'FROM' identification_variable_declaration -> ^( T_SOURCES[$fr] identification_variable_declaration ) )
			// JPA2.g:167:7: fr= 'FROM' identification_variable_declaration
			{
			fr=(Token)match(input,103,FOLLOW_103_in_delete_clause1318); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_103.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_delete_clause1320);
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

		Token string_literal91=null;
		Token char_literal93=null;
		ParserRuleReturnScope select_item92 =null;
		ParserRuleReturnScope select_item94 =null;

		Object string_literal91_tree=null;
		Object char_literal93_tree=null;
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
					string_literal91=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause1348); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal91);

					}
					break;

			}

			pushFollow(FOLLOW_select_item_in_select_clause1352);
			select_item92=select_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_item.add(select_item92.getTree());
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
					char_literal93=(Token)match(input,66,FOLLOW_66_in_select_clause1355); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal93);

					pushFollow(FOLLOW_select_item_in_select_clause1357);
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

		Token AS96=null;
		ParserRuleReturnScope select_expression95 =null;
		ParserRuleReturnScope result_variable97 =null;

		Object AS96_tree=null;

		try {
			// JPA2.g:173:5: ( select_expression ( ( AS )? result_variable )? )
			// JPA2.g:173:7: select_expression ( ( AS )? result_variable )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_select_expression_in_select_item1400);
			select_expression95=select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, select_expression95.getTree());

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
							AS96=(Token)match(input,AS,FOLLOW_AS_in_select_item1404); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							AS96_tree = (Object)adaptor.create(AS96);
							adaptor.addChild(root_0, AS96_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_result_variable_in_select_item1408);
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
	// JPA2.g:174:1: select_expression : ( path_expression ( ( '+' | '-' | '*' | '/' ) scalar_expression )? | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression );
	public final JPA2Parser.select_expression_return select_expression() throws RecognitionException {
		JPA2Parser.select_expression_return retval = new JPA2Parser.select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set99=null;
		Token string_literal104=null;
		Token char_literal105=null;
		Token char_literal107=null;
		ParserRuleReturnScope path_expression98 =null;
		ParserRuleReturnScope scalar_expression100 =null;
		ParserRuleReturnScope identification_variable101 =null;
		ParserRuleReturnScope scalar_expression102 =null;
		ParserRuleReturnScope aggregate_expression103 =null;
		ParserRuleReturnScope identification_variable106 =null;
		ParserRuleReturnScope constructor_expression108 =null;

		Object set99_tree=null;
		Object string_literal104_tree=null;
		Object char_literal105_tree=null;
		Object char_literal107_tree=null;
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


					pushFollow(FOLLOW_path_expression_in_select_expression1421);
					path_expression98=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression98.getTree());

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
							set99=input.LT(1);
							if ( (input.LA(1) >= 64 && input.LA(1) <= 65)||input.LA(1)==67||input.LA(1)==69 ) {
								input.consume();
								if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set99));
								state.errorRecovery=false;
								state.failed=false;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							pushFollow(FOLLOW_scalar_expression_in_select_expression1440);
							scalar_expression100=scalar_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression100.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:176:7: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_select_expression1450);
					identification_variable101=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable101.getTree());
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
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_ENTITY, (identification_variable101!=null?input.toString(identification_variable101.start,identification_variable101.stop):null)), root_1);
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


					pushFollow(FOLLOW_scalar_expression_in_select_expression1468);
					scalar_expression102=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression102.getTree());

					}
					break;
				case 4 :
					// JPA2.g:178:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_select_expression1476);
					aggregate_expression103=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression103.getTree());

					}
					break;
				case 5 :
					// JPA2.g:179:7: 'OBJECT' '(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal104=(Token)match(input,123,FOLLOW_123_in_select_expression1484); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal104_tree = (Object)adaptor.create(string_literal104);
					adaptor.addChild(root_0, string_literal104_tree);
					}

					char_literal105=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression1486); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal105_tree = (Object)adaptor.create(char_literal105);
					adaptor.addChild(root_0, char_literal105_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_select_expression1487);
					identification_variable106=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable106.getTree());

					char_literal107=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression1488); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal107_tree = (Object)adaptor.create(char_literal107);
					adaptor.addChild(root_0, char_literal107_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:180:7: constructor_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_constructor_expression_in_select_expression1496);
					constructor_expression108=constructor_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_expression108.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal109=null;
		Token char_literal111=null;
		Token char_literal113=null;
		Token char_literal115=null;
		ParserRuleReturnScope constructor_name110 =null;
		ParserRuleReturnScope constructor_item112 =null;
		ParserRuleReturnScope constructor_item114 =null;

		Object string_literal109_tree=null;
		Object char_literal111_tree=null;
		Object char_literal113_tree=null;
		Object char_literal115_tree=null;

		try {
			// JPA2.g:182:5: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
			// JPA2.g:182:7: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal109=(Token)match(input,117,FOLLOW_117_in_constructor_expression1507); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal109_tree = (Object)adaptor.create(string_literal109);
			adaptor.addChild(root_0, string_literal109_tree);
			}

			pushFollow(FOLLOW_constructor_name_in_constructor_expression1509);
			constructor_name110=constructor_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name110.getTree());

			char_literal111=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression1511); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal111_tree = (Object)adaptor.create(char_literal111);
			adaptor.addChild(root_0, char_literal111_tree);
			}

			pushFollow(FOLLOW_constructor_item_in_constructor_expression1513);
			constructor_item112=constructor_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item112.getTree());

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
					char_literal113=(Token)match(input,66,FOLLOW_66_in_constructor_expression1516); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal113_tree = (Object)adaptor.create(char_literal113);
					adaptor.addChild(root_0, char_literal113_tree);
					}

					pushFollow(FOLLOW_constructor_item_in_constructor_expression1518);
					constructor_item114=constructor_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item114.getTree());

					}
					break;

				default :
					break loop37;
				}
			}

			char_literal115=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression1522); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal115_tree = (Object)adaptor.create(char_literal115);
			adaptor.addChild(root_0, char_literal115_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope path_expression116 =null;
		ParserRuleReturnScope scalar_expression117 =null;
		ParserRuleReturnScope aggregate_expression118 =null;
		ParserRuleReturnScope identification_variable119 =null;


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


					pushFollow(FOLLOW_path_expression_in_constructor_item1533);
					path_expression116=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression116.getTree());

					}
					break;
				case 2 :
					// JPA2.g:185:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_constructor_item1541);
					scalar_expression117=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression117.getTree());

					}
					break;
				case 3 :
					// JPA2.g:186:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_constructor_item1549);
					aggregate_expression118=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression118.getTree());

					}
					break;
				case 4 :
					// JPA2.g:187:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_constructor_item1557);
					identification_variable119=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable119.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token char_literal121=null;
		Token DISTINCT122=null;
		Token char_literal124=null;
		Token string_literal125=null;
		Token char_literal126=null;
		Token DISTINCT127=null;
		Token char_literal129=null;
		ParserRuleReturnScope aggregate_expression_function_name120 =null;
		ParserRuleReturnScope arithmetic_expression123 =null;
		ParserRuleReturnScope count_argument128 =null;
		ParserRuleReturnScope function_invocation130 =null;

		Object char_literal121_tree=null;
		Object DISTINCT122_tree=null;
		Object char_literal124_tree=null;
		Object string_literal125_tree=null;
		Object char_literal126_tree=null;
		Object DISTINCT127_tree=null;
		Object char_literal129_tree=null;
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
					pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1568);
					aggregate_expression_function_name120=aggregate_expression_function_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name120.getTree());
					char_literal121=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1570); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal121);

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
							DISTINCT122=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1572); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT122);

							}
							break;

					}

					pushFollow(FOLLOW_arithmetic_expression_in_aggregate_expression1576);
					arithmetic_expression123=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_arithmetic_expression.add(arithmetic_expression123.getTree());
					char_literal124=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1577); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal124);

					// AST REWRITE
					// elements: aggregate_expression_function_name, RPAREN, arithmetic_expression, LPAREN, DISTINCT
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
					string_literal125=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1611); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COUNT.add(string_literal125);

					char_literal126=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1613); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal126);

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
							DISTINCT127=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1615); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT127);

							}
							break;

					}

					pushFollow(FOLLOW_count_argument_in_aggregate_expression1619);
					count_argument128=count_argument();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_count_argument.add(count_argument128.getTree());
					char_literal129=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1621); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal129);

					// AST REWRITE
					// elements: LPAREN, count_argument, COUNT, RPAREN, DISTINCT
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


					pushFollow(FOLLOW_function_invocation_in_aggregate_expression1656);
					function_invocation130=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation130.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token set131=null;

		Object set131_tree=null;

		try {
			// JPA2.g:195:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set131=input.LT(1);
			if ( input.LA(1)==AVG||input.LA(1)==COUNT||(input.LA(1) >= MAX && input.LA(1) <= MIN)||input.LA(1)==SUM ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set131));
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

		ParserRuleReturnScope identification_variable132 =null;
		ParserRuleReturnScope path_expression133 =null;


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


					pushFollow(FOLLOW_identification_variable_in_count_argument1693);
					identification_variable132=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable132.getTree());

					}
					break;
				case 2 :
					// JPA2.g:197:33: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_count_argument1697);
					path_expression133=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression133.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
		ParserRuleReturnScope conditional_expression134 =null;

		Object wh_tree=null;
		RewriteRuleTokenStream stream_143=new RewriteRuleTokenStream(adaptor,"token 143");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");

		try {
			// JPA2.g:199:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) )
			// JPA2.g:199:7: wh= 'WHERE' conditional_expression
			{
			wh=(Token)match(input,143,FOLLOW_143_in_where_clause1710); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_143.add(wh);

			pushFollow(FOLLOW_conditional_expression_in_where_clause1712);
			conditional_expression134=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression134.getTree());
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

		Token string_literal135=null;
		Token string_literal136=null;
		Token char_literal138=null;
		ParserRuleReturnScope groupby_item137 =null;
		ParserRuleReturnScope groupby_item139 =null;

		Object string_literal135_tree=null;
		Object string_literal136_tree=null;
		Object char_literal138_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");

		try {
			// JPA2.g:201:5: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
			// JPA2.g:201:7: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
			{
			string_literal135=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1734); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_GROUP.add(string_literal135);

			string_literal136=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1736); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal136);

			pushFollow(FOLLOW_groupby_item_in_groupby_clause1738);
			groupby_item137=groupby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item137.getTree());
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
					char_literal138=(Token)match(input,66,FOLLOW_66_in_groupby_clause1741); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal138);

					pushFollow(FOLLOW_groupby_item_in_groupby_clause1743);
					groupby_item139=groupby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item139.getTree());
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

		ParserRuleReturnScope path_expression140 =null;
		ParserRuleReturnScope identification_variable141 =null;
		ParserRuleReturnScope extract_function142 =null;


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


					pushFollow(FOLLOW_path_expression_in_groupby_item1777);
					path_expression140=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression140.getTree());

					}
					break;
				case 2 :
					// JPA2.g:204:25: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_groupby_item1781);
					identification_variable141=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable141.getTree());

					}
					break;
				case 3 :
					// JPA2.g:204:51: extract_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extract_function_in_groupby_item1785);
					extract_function142=extract_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extract_function142.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal143=null;
		ParserRuleReturnScope conditional_expression144 =null;

		Object string_literal143_tree=null;

		try {
			// JPA2.g:206:5: ( 'HAVING' conditional_expression )
			// JPA2.g:206:7: 'HAVING' conditional_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal143=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1796); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal143_tree = (Object)adaptor.create(string_literal143);
			adaptor.addChild(root_0, string_literal143_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_having_clause1798);
			conditional_expression144=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression144.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal145=null;
		Token string_literal146=null;
		Token char_literal148=null;
		ParserRuleReturnScope orderby_item147 =null;
		ParserRuleReturnScope orderby_item149 =null;

		Object string_literal145_tree=null;
		Object string_literal146_tree=null;
		Object char_literal148_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");

		try {
			// JPA2.g:208:5: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
			// JPA2.g:208:7: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
			{
			string_literal145=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1809); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ORDER.add(string_literal145);

			string_literal146=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1811); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal146);

			pushFollow(FOLLOW_orderby_item_in_orderby_clause1813);
			orderby_item147=orderby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item147.getTree());
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
					char_literal148=(Token)match(input,66,FOLLOW_66_in_orderby_clause1816); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal148);

					pushFollow(FOLLOW_orderby_item_in_orderby_clause1818);
					orderby_item149=orderby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item149.getTree());
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

		ParserRuleReturnScope orderby_variable150 =null;
		ParserRuleReturnScope sort151 =null;
		ParserRuleReturnScope sortNulls152 =null;

		RewriteRuleSubtreeStream stream_sortNulls=new RewriteRuleSubtreeStream(adaptor,"rule sortNulls");
		RewriteRuleSubtreeStream stream_orderby_variable=new RewriteRuleSubtreeStream(adaptor,"rule orderby_variable");
		RewriteRuleSubtreeStream stream_sort=new RewriteRuleSubtreeStream(adaptor,"rule sort");

		try {
			// JPA2.g:211:5: ( orderby_variable ( sort )? ( sortNulls )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( sort )? ( sortNulls )? ) )
			// JPA2.g:211:7: orderby_variable ( sort )? ( sortNulls )?
			{
			pushFollow(FOLLOW_orderby_variable_in_orderby_item1852);
			orderby_variable150=orderby_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable150.getTree());
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
					pushFollow(FOLLOW_sort_in_orderby_item1854);
					sort151=sort();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sort.add(sort151.getTree());
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
					pushFollow(FOLLOW_sortNulls_in_orderby_item1857);
					sortNulls152=sortNulls();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sortNulls.add(sortNulls152.getTree());
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

		ParserRuleReturnScope path_expression153 =null;
		ParserRuleReturnScope general_identification_variable154 =null;
		ParserRuleReturnScope result_variable155 =null;
		ParserRuleReturnScope scalar_expression156 =null;
		ParserRuleReturnScope aggregate_expression157 =null;


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
			case 141:
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


					pushFollow(FOLLOW_path_expression_in_orderby_variable1892);
					path_expression153=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression153.getTree());

					}
					break;
				case 2 :
					// JPA2.g:214:25: general_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_identification_variable_in_orderby_variable1896);
					general_identification_variable154=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable154.getTree());

					}
					break;
				case 3 :
					// JPA2.g:214:59: result_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_result_variable_in_orderby_variable1900);
					result_variable155=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable155.getTree());

					}
					break;
				case 4 :
					// JPA2.g:214:77: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_orderby_variable1904);
					scalar_expression156=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression156.getTree());

					}
					break;
				case 5 :
					// JPA2.g:214:97: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_orderby_variable1908);
					aggregate_expression157=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression157.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token set158=null;

		Object set158_tree=null;

		try {
			// JPA2.g:216:5: ( ( 'ASC' | 'DESC' ) )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set158=input.LT(1);
			if ( input.LA(1)==ASC||input.LA(1)==DESC ) {
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

		Token set159=null;

		Object set159_tree=null;

		try {
			// JPA2.g:218:5: ( ( 'NULLS FIRST' | 'NULLS LAST' ) )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set159=input.LT(1);
			if ( (input.LA(1) >= 121 && input.LA(1) <= 122) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set159));
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
		Token string_literal160=null;
		ParserRuleReturnScope simple_select_clause161 =null;
		ParserRuleReturnScope subquery_from_clause162 =null;
		ParserRuleReturnScope where_clause163 =null;
		ParserRuleReturnScope groupby_clause164 =null;
		ParserRuleReturnScope having_clause165 =null;

		Object lp_tree=null;
		Object rp_tree=null;
		Object string_literal160_tree=null;
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
			lp=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subquery1955); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(lp);

			string_literal160=(Token)match(input,129,FOLLOW_129_in_subquery1957); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_129.add(string_literal160);

			pushFollow(FOLLOW_simple_select_clause_in_subquery1959);
			simple_select_clause161=simple_select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause161.getTree());
			pushFollow(FOLLOW_subquery_from_clause_in_subquery1961);
			subquery_from_clause162=subquery_from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause162.getTree());
			// JPA2.g:220:65: ( where_clause )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==143) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// JPA2.g:220:66: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_subquery1964);
					where_clause163=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause163.getTree());
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
					pushFollow(FOLLOW_groupby_clause_in_subquery1969);
					groupby_clause164=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause164.getTree());
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
					pushFollow(FOLLOW_having_clause_in_subquery1974);
					having_clause165=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause165.getTree());
					}
					break;

			}

			rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1980); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(rp);

			// AST REWRITE
			// elements: groupby_clause, having_clause, 129, simple_select_clause, where_clause, subquery_from_clause
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
		Token char_literal167=null;
		ParserRuleReturnScope subselect_identification_variable_declaration166 =null;
		ParserRuleReturnScope subselect_identification_variable_declaration168 =null;

		Object fr_tree=null;
		Object char_literal167_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_103=new RewriteRuleTokenStream(adaptor,"token 103");
		RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");

		try {
			// JPA2.g:223:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
			// JPA2.g:223:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
			{
			fr=(Token)match(input,103,FOLLOW_103_in_subquery_from_clause2030); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_103.add(fr);

			pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause2032);
			subselect_identification_variable_declaration166=subselect_identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration166.getTree());
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
					char_literal167=(Token)match(input,66,FOLLOW_66_in_subquery_from_clause2035); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal167);

					pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause2037);
					subselect_identification_variable_declaration168=subselect_identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration168.getTree());
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

		Token AS171=null;
		ParserRuleReturnScope identification_variable_declaration169 =null;
		ParserRuleReturnScope derived_path_expression170 =null;
		ParserRuleReturnScope identification_variable172 =null;
		ParserRuleReturnScope join173 =null;
		ParserRuleReturnScope derived_collection_member_declaration174 =null;

		Object AS171_tree=null;

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


					pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration2075);
					identification_variable_declaration169=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration169.getTree());

					}
					break;
				case 2 :
					// JPA2.g:228:7: derived_path_expression AS identification_variable ( join )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration2083);
					derived_path_expression170=derived_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_path_expression170.getTree());

					AS171=(Token)match(input,AS,FOLLOW_AS_in_subselect_identification_variable_declaration2085); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AS171_tree = (Object)adaptor.create(AS171);
					adaptor.addChild(root_0, AS171_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration2087);
					identification_variable172=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable172.getTree());

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
							pushFollow(FOLLOW_join_in_subselect_identification_variable_declaration2090);
							join173=join();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, join173.getTree());

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


					pushFollow(FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration2100);
					derived_collection_member_declaration174=derived_collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_collection_member_declaration174.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token char_literal176=null;
		Token char_literal179=null;
		ParserRuleReturnScope general_derived_path175 =null;
		ParserRuleReturnScope single_valued_object_field177 =null;
		ParserRuleReturnScope general_derived_path178 =null;
		ParserRuleReturnScope collection_valued_field180 =null;

		Object char_literal176_tree=null;
		Object char_literal179_tree=null;

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


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression2111);
					general_derived_path175=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path175.getTree());

					char_literal176=(Token)match(input,68,FOLLOW_68_in_derived_path_expression2112); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal176_tree = (Object)adaptor.create(char_literal176);
					adaptor.addChild(root_0, char_literal176_tree);
					}

					pushFollow(FOLLOW_single_valued_object_field_in_derived_path_expression2113);
					single_valued_object_field177=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field177.getTree());

					}
					break;
				case 2 :
					// JPA2.g:232:7: general_derived_path '.' collection_valued_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression2121);
					general_derived_path178=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path178.getTree());

					char_literal179=(Token)match(input,68,FOLLOW_68_in_derived_path_expression2122); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal179_tree = (Object)adaptor.create(char_literal179);
					adaptor.addChild(root_0, char_literal179_tree);
					}

					pushFollow(FOLLOW_collection_valued_field_in_derived_path_expression2123);
					collection_valued_field180=collection_valued_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field180.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token char_literal183=null;
		ParserRuleReturnScope simple_derived_path181 =null;
		ParserRuleReturnScope treated_derived_path182 =null;
		ParserRuleReturnScope single_valued_object_field184 =null;

		Object char_literal183_tree=null;

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


					pushFollow(FOLLOW_simple_derived_path_in_general_derived_path2134);
					simple_derived_path181=simple_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_derived_path181.getTree());

					}
					break;
				case 2 :
					// JPA2.g:235:7: treated_derived_path ( '.' single_valued_object_field )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_treated_derived_path_in_general_derived_path2142);
					treated_derived_path182=treated_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, treated_derived_path182.getTree());

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
							char_literal183=(Token)match(input,68,FOLLOW_68_in_general_derived_path2144); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal183_tree = (Object)adaptor.create(char_literal183);
							adaptor.addChild(root_0, char_literal183_tree);
							}

							pushFollow(FOLLOW_single_valued_object_field_in_general_derived_path2145);
							single_valued_object_field184=single_valued_object_field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field184.getTree());

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

		ParserRuleReturnScope superquery_identification_variable185 =null;


		try {
			// JPA2.g:238:5: ( superquery_identification_variable )
			// JPA2.g:238:7: superquery_identification_variable
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_superquery_identification_variable_in_simple_derived_path2163);
			superquery_identification_variable185=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable185.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal186=null;
		Token AS188=null;
		Token char_literal190=null;
		ParserRuleReturnScope general_derived_path187 =null;
		ParserRuleReturnScope subtype189 =null;

		Object string_literal186_tree=null;
		Object AS188_tree=null;
		Object char_literal190_tree=null;

		try {
			// JPA2.g:241:5: ( 'TREAT(' general_derived_path AS subtype ')' )
			// JPA2.g:241:7: 'TREAT(' general_derived_path AS subtype ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal186=(Token)match(input,135,FOLLOW_135_in_treated_derived_path2180); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal186_tree = (Object)adaptor.create(string_literal186);
			adaptor.addChild(root_0, string_literal186_tree);
			}

			pushFollow(FOLLOW_general_derived_path_in_treated_derived_path2181);
			general_derived_path187=general_derived_path();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path187.getTree());

			AS188=(Token)match(input,AS,FOLLOW_AS_in_treated_derived_path2183); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			AS188_tree = (Object)adaptor.create(AS188);
			adaptor.addChild(root_0, AS188_tree);
			}

			pushFollow(FOLLOW_subtype_in_treated_derived_path2185);
			subtype189=subtype();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subtype189.getTree());

			char_literal190=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_treated_derived_path2187); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal190_tree = (Object)adaptor.create(char_literal190);
			adaptor.addChild(root_0, char_literal190_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal191=null;
		Token char_literal193=null;
		Token char_literal195=null;
		ParserRuleReturnScope superquery_identification_variable192 =null;
		ParserRuleReturnScope single_valued_object_field194 =null;
		ParserRuleReturnScope collection_valued_field196 =null;

		Object string_literal191_tree=null;
		Object char_literal193_tree=null;
		Object char_literal195_tree=null;

		try {
			// JPA2.g:243:5: ( 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field )
			// JPA2.g:243:7: 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field
			{
			root_0 = (Object)adaptor.nil();


			string_literal191=(Token)match(input,IN,FOLLOW_IN_in_derived_collection_member_declaration2198); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal191_tree = (Object)adaptor.create(string_literal191);
			adaptor.addChild(root_0, string_literal191_tree);
			}

			pushFollow(FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2200);
			superquery_identification_variable192=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable192.getTree());

			char_literal193=(Token)match(input,68,FOLLOW_68_in_derived_collection_member_declaration2201); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal193_tree = (Object)adaptor.create(char_literal193);
			adaptor.addChild(root_0, char_literal193_tree);
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
					pushFollow(FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2203);
					single_valued_object_field194=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field194.getTree());

					char_literal195=(Token)match(input,68,FOLLOW_68_in_derived_collection_member_declaration2205); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal195_tree = (Object)adaptor.create(char_literal195);
					adaptor.addChild(root_0, char_literal195_tree);
					}

					}
					break;

				default :
					break loop58;
				}
			}

			pushFollow(FOLLOW_collection_valued_field_in_derived_collection_member_declaration2208);
			collection_valued_field196=collection_valued_field();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field196.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal197=null;
		ParserRuleReturnScope simple_select_expression198 =null;

		Object string_literal197_tree=null;
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
					string_literal197=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause2221); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal197);

					}
					break;

			}

			pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause2225);
			simple_select_expression198=simple_select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_expression.add(simple_select_expression198.getTree());
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

		ParserRuleReturnScope path_expression199 =null;
		ParserRuleReturnScope scalar_expression200 =null;
		ParserRuleReturnScope aggregate_expression201 =null;
		ParserRuleReturnScope identification_variable202 =null;


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


					pushFollow(FOLLOW_path_expression_in_simple_select_expression2265);
					path_expression199=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression199.getTree());

					}
					break;
				case 2 :
					// JPA2.g:250:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_simple_select_expression2273);
					scalar_expression200=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression200.getTree());

					}
					break;
				case 3 :
					// JPA2.g:251:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression2281);
					aggregate_expression201=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression201.getTree());

					}
					break;
				case 4 :
					// JPA2.g:252:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_select_expression2289);
					identification_variable202=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable202.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope arithmetic_expression203 =null;
		ParserRuleReturnScope string_expression204 =null;
		ParserRuleReturnScope enum_expression205 =null;
		ParserRuleReturnScope datetime_expression206 =null;
		ParserRuleReturnScope boolean_expression207 =null;
		ParserRuleReturnScope case_expression208 =null;
		ParserRuleReturnScope entity_type_expression209 =null;


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
			case 145:
			case 146:
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


					pushFollow(FOLLOW_arithmetic_expression_in_scalar_expression2300);
					arithmetic_expression203=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression203.getTree());

					}
					break;
				case 2 :
					// JPA2.g:255:7: string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_scalar_expression2308);
					string_expression204=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression204.getTree());

					}
					break;
				case 3 :
					// JPA2.g:256:7: enum_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_scalar_expression2316);
					enum_expression205=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression205.getTree());

					}
					break;
				case 4 :
					// JPA2.g:257:7: datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_scalar_expression2324);
					datetime_expression206=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression206.getTree());

					}
					break;
				case 5 :
					// JPA2.g:258:7: boolean_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_scalar_expression2332);
					boolean_expression207=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression207.getTree());

					}
					break;
				case 6 :
					// JPA2.g:259:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_scalar_expression2340);
					case_expression208=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression208.getTree());

					}
					break;
				case 7 :
					// JPA2.g:260:7: entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_scalar_expression2348);
					entity_type_expression209=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression209.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal211=null;
		ParserRuleReturnScope conditional_term210 =null;
		ParserRuleReturnScope conditional_term212 =null;

		Object string_literal211_tree=null;

		try {
			// JPA2.g:262:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
			// JPA2.g:262:7: ( conditional_term ) ( 'OR' conditional_term )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:262:7: ( conditional_term )
			// JPA2.g:262:8: conditional_term
			{
			pushFollow(FOLLOW_conditional_term_in_conditional_expression2360);
			conditional_term210=conditional_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term210.getTree());

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
					string_literal211=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression2364); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal211_tree = (Object)adaptor.create(string_literal211);
					adaptor.addChild(root_0, string_literal211_tree);
					}

					pushFollow(FOLLOW_conditional_term_in_conditional_expression2366);
					conditional_term212=conditional_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term212.getTree());

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

		Token string_literal214=null;
		ParserRuleReturnScope conditional_factor213 =null;
		ParserRuleReturnScope conditional_factor215 =null;

		Object string_literal214_tree=null;

		try {
			// JPA2.g:264:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
			// JPA2.g:264:7: ( conditional_factor ) ( 'AND' conditional_factor )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:264:7: ( conditional_factor )
			// JPA2.g:264:8: conditional_factor
			{
			pushFollow(FOLLOW_conditional_factor_in_conditional_term2380);
			conditional_factor213=conditional_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor213.getTree());

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
					string_literal214=(Token)match(input,AND,FOLLOW_AND_in_conditional_term2384); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal214_tree = (Object)adaptor.create(string_literal214);
					adaptor.addChild(root_0, string_literal214_tree);
					}

					pushFollow(FOLLOW_conditional_factor_in_conditional_term2386);
					conditional_factor215=conditional_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor215.getTree());

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

		Token string_literal216=null;
		ParserRuleReturnScope conditional_primary217 =null;

		Object string_literal216_tree=null;

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
					string_literal216=(Token)match(input,NOT,FOLLOW_NOT_in_conditional_factor2400); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal216_tree = (Object)adaptor.create(string_literal216);
					adaptor.addChild(root_0, string_literal216_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_conditional_primary_in_conditional_factor2404);
			conditional_primary217=conditional_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_primary217.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token char_literal219=null;
		Token char_literal221=null;
		ParserRuleReturnScope simple_cond_expression218 =null;
		ParserRuleReturnScope conditional_expression220 =null;

		Object char_literal219_tree=null;
		Object char_literal221_tree=null;
		RewriteRuleSubtreeStream stream_simple_cond_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_cond_expression");

		try {
			// JPA2.g:268:5: ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' )
			int alt65=2;
			int LA65_0 = input.LA(1);
			if ( (LA65_0==AVG||LA65_0==CASE||LA65_0==COUNT||LA65_0==GROUP||LA65_0==INT_NUMERAL||LA65_0==LOWER||(LA65_0 >= MAX && LA65_0 <= NOT)||(LA65_0 >= STRING_LITERAL && LA65_0 <= SUM)||LA65_0==WORD||LA65_0==63||LA65_0==65||LA65_0==67||LA65_0==70||(LA65_0 >= 77 && LA65_0 <= 84)||(LA65_0 >= 89 && LA65_0 <= 94)||(LA65_0 >= 101 && LA65_0 <= 102)||LA65_0==104||LA65_0==106||LA65_0==110||LA65_0==112||LA65_0==115||LA65_0==120||LA65_0==130||(LA65_0 >= 132 && LA65_0 <= 133)||(LA65_0 >= 135 && LA65_0 <= 137)||LA65_0==139||(LA65_0 >= 145 && LA65_0 <= 146)) ) {
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
					pushFollow(FOLLOW_simple_cond_expression_in_conditional_primary2415);
					simple_cond_expression218=simple_cond_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_simple_cond_expression.add(simple_cond_expression218.getTree());
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


					char_literal219=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_primary2439); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal219_tree = (Object)adaptor.create(char_literal219);
					adaptor.addChild(root_0, char_literal219_tree);
					}

					pushFollow(FOLLOW_conditional_expression_in_conditional_primary2440);
					conditional_expression220=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression220.getTree());

					char_literal221=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_primary2441); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal221_tree = (Object)adaptor.create(char_literal221);
					adaptor.addChild(root_0, char_literal221_tree);
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

		ParserRuleReturnScope comparison_expression222 =null;
		ParserRuleReturnScope between_expression223 =null;
		ParserRuleReturnScope in_expression224 =null;
		ParserRuleReturnScope like_expression225 =null;
		ParserRuleReturnScope null_comparison_expression226 =null;
		ParserRuleReturnScope empty_collection_comparison_expression227 =null;
		ParserRuleReturnScope collection_member_expression228 =null;
		ParserRuleReturnScope exists_expression229 =null;
		ParserRuleReturnScope date_macro_expression230 =null;


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


					pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression2452);
					comparison_expression222=comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression222.getTree());

					}
					break;
				case 2 :
					// JPA2.g:273:7: between_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_between_expression_in_simple_cond_expression2460);
					between_expression223=between_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression223.getTree());

					}
					break;
				case 3 :
					// JPA2.g:274:7: in_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_in_expression_in_simple_cond_expression2468);
					in_expression224=in_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression224.getTree());

					}
					break;
				case 4 :
					// JPA2.g:275:7: like_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_like_expression_in_simple_cond_expression2476);
					like_expression225=like_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression225.getTree());

					}
					break;
				case 5 :
					// JPA2.g:276:7: null_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression2484);
					null_comparison_expression226=null_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression226.getTree());

					}
					break;
				case 6 :
					// JPA2.g:277:7: empty_collection_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2492);
					empty_collection_comparison_expression227=empty_collection_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression227.getTree());

					}
					break;
				case 7 :
					// JPA2.g:278:7: collection_member_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression2500);
					collection_member_expression228=collection_member_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression228.getTree());

					}
					break;
				case 8 :
					// JPA2.g:279:7: exists_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exists_expression_in_simple_cond_expression2508);
					exists_expression229=exists_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression229.getTree());

					}
					break;
				case 9 :
					// JPA2.g:280:7: date_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression2516);
					date_macro_expression230=date_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_macro_expression230.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope date_between_macro_expression231 =null;
		ParserRuleReturnScope date_before_macro_expression232 =null;
		ParserRuleReturnScope date_after_macro_expression233 =null;
		ParserRuleReturnScope date_equals_macro_expression234 =null;
		ParserRuleReturnScope date_today_macro_expression235 =null;


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


					pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2529);
					date_between_macro_expression231=date_between_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression231.getTree());

					}
					break;
				case 2 :
					// JPA2.g:285:7: date_before_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2537);
					date_before_macro_expression232=date_before_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression232.getTree());

					}
					break;
				case 3 :
					// JPA2.g:286:7: date_after_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2545);
					date_after_macro_expression233=date_after_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression233.getTree());

					}
					break;
				case 4 :
					// JPA2.g:287:7: date_equals_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2553);
					date_equals_macro_expression234=date_equals_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression234.getTree());

					}
					break;
				case 5 :
					// JPA2.g:288:7: date_today_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2561);
					date_today_macro_expression235=date_today_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_today_macro_expression235.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:290:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ( ',' 'USER_TIMEZONE' )? ')' ;
	public final JPA2Parser.date_between_macro_expression_return date_between_macro_expression() throws RecognitionException {
		JPA2Parser.date_between_macro_expression_return retval = new JPA2Parser.date_between_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal236=null;
		Token char_literal237=null;
		Token char_literal239=null;
		Token string_literal240=null;
		Token set241=null;
		Token char_literal243=null;
		Token string_literal244=null;
		Token set245=null;
		Token char_literal247=null;
		Token set248=null;
		Token char_literal249=null;
		Token string_literal250=null;
		Token char_literal251=null;
		ParserRuleReturnScope path_expression238 =null;
		ParserRuleReturnScope numeric_literal242 =null;
		ParserRuleReturnScope numeric_literal246 =null;

		Object string_literal236_tree=null;
		Object char_literal237_tree=null;
		Object char_literal239_tree=null;
		Object string_literal240_tree=null;
		Object set241_tree=null;
		Object char_literal243_tree=null;
		Object string_literal244_tree=null;
		Object set245_tree=null;
		Object char_literal247_tree=null;
		Object set248_tree=null;
		Object char_literal249_tree=null;
		Object string_literal250_tree=null;
		Object char_literal251_tree=null;

		try {
			// JPA2.g:291:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ( ',' 'USER_TIMEZONE' )? ')' )
			// JPA2.g:291:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ( ',' 'USER_TIMEZONE' )? ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal236=(Token)match(input,78,FOLLOW_78_in_date_between_macro_expression2573); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal236_tree = (Object)adaptor.create(string_literal236);
			adaptor.addChild(root_0, string_literal236_tree);
			}

			char_literal237=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2575); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal237_tree = (Object)adaptor.create(char_literal237);
			adaptor.addChild(root_0, char_literal237_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2577);
			path_expression238=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression238.getTree());

			char_literal239=(Token)match(input,66,FOLLOW_66_in_date_between_macro_expression2579); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal239_tree = (Object)adaptor.create(char_literal239);
			adaptor.addChild(root_0, char_literal239_tree);
			}

			string_literal240=(Token)match(input,118,FOLLOW_118_in_date_between_macro_expression2581); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal240_tree = (Object)adaptor.create(string_literal240);
			adaptor.addChild(root_0, string_literal240_tree);
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
					set241=input.LT(1);
					if ( input.LA(1)==65||input.LA(1)==67 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2592);
					numeric_literal242=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal242.getTree());

					}
					break;

			}

			char_literal243=(Token)match(input,66,FOLLOW_66_in_date_between_macro_expression2596); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal243_tree = (Object)adaptor.create(char_literal243);
			adaptor.addChild(root_0, char_literal243_tree);
			}

			string_literal244=(Token)match(input,118,FOLLOW_118_in_date_between_macro_expression2598); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal244_tree = (Object)adaptor.create(string_literal244);
			adaptor.addChild(root_0, string_literal244_tree);
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
					set245=input.LT(1);
					if ( input.LA(1)==65||input.LA(1)==67 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set245));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2609);
					numeric_literal246=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal246.getTree());

					}
					break;

			}

			char_literal247=(Token)match(input,66,FOLLOW_66_in_date_between_macro_expression2613); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal247_tree = (Object)adaptor.create(char_literal247);
			adaptor.addChild(root_0, char_literal247_tree);
			}

			set248=input.LT(1);
			if ( input.LA(1)==95||input.LA(1)==105||input.LA(1)==114||input.LA(1)==116||input.LA(1)==128||input.LA(1)==144 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set248));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			// JPA2.g:291:181: ( ',' 'USER_TIMEZONE' )?
			int alt70=2;
			int LA70_0 = input.LA(1);
			if ( (LA70_0==66) ) {
				alt70=1;
			}
			switch (alt70) {
				case 1 :
					// JPA2.g:291:182: ',' 'USER_TIMEZONE'
					{
					char_literal249=(Token)match(input,66,FOLLOW_66_in_date_between_macro_expression2639); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal249_tree = (Object)adaptor.create(char_literal249);
					adaptor.addChild(root_0, char_literal249_tree);
					}

					string_literal250=(Token)match(input,140,FOLLOW_140_in_date_between_macro_expression2641); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal250_tree = (Object)adaptor.create(string_literal250);
					adaptor.addChild(root_0, string_literal250_tree);
					}

					}
					break;

			}

			char_literal251=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2645); if (state.failed) return retval;
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
	// $ANTLR end "date_between_macro_expression"


	public static class date_before_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_before_macro_expression"
	// JPA2.g:293:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' ;
	public final JPA2Parser.date_before_macro_expression_return date_before_macro_expression() throws RecognitionException {
		JPA2Parser.date_before_macro_expression_return retval = new JPA2Parser.date_before_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal252=null;
		Token char_literal253=null;
		Token char_literal255=null;
		Token char_literal258=null;
		Token string_literal259=null;
		Token char_literal260=null;
		ParserRuleReturnScope path_expression254 =null;
		ParserRuleReturnScope path_expression256 =null;
		ParserRuleReturnScope input_parameter257 =null;

		Object string_literal252_tree=null;
		Object char_literal253_tree=null;
		Object char_literal255_tree=null;
		Object char_literal258_tree=null;
		Object string_literal259_tree=null;
		Object char_literal260_tree=null;

		try {
			// JPA2.g:294:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' )
			// JPA2.g:294:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal252=(Token)match(input,80,FOLLOW_80_in_date_before_macro_expression2657); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal252_tree = (Object)adaptor.create(string_literal252);
			adaptor.addChild(root_0, string_literal252_tree);
			}

			char_literal253=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2659); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal253_tree = (Object)adaptor.create(char_literal253);
			adaptor.addChild(root_0, char_literal253_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2661);
			path_expression254=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression254.getTree());

			char_literal255=(Token)match(input,66,FOLLOW_66_in_date_before_macro_expression2663); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal255_tree = (Object)adaptor.create(char_literal255);
			adaptor.addChild(root_0, char_literal255_tree);
			}

			// JPA2.g:294:45: ( path_expression | input_parameter )
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
					// JPA2.g:294:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2666);
					path_expression256=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression256.getTree());

					}
					break;
				case 2 :
					// JPA2.g:294:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2670);
					input_parameter257=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter257.getTree());

					}
					break;

			}

			// JPA2.g:294:81: ( ',' 'USER_TIMEZONE' )?
			int alt72=2;
			int LA72_0 = input.LA(1);
			if ( (LA72_0==66) ) {
				alt72=1;
			}
			switch (alt72) {
				case 1 :
					// JPA2.g:294:82: ',' 'USER_TIMEZONE'
					{
					char_literal258=(Token)match(input,66,FOLLOW_66_in_date_before_macro_expression2674); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal258_tree = (Object)adaptor.create(char_literal258);
					adaptor.addChild(root_0, char_literal258_tree);
					}

					string_literal259=(Token)match(input,140,FOLLOW_140_in_date_before_macro_expression2676); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal259_tree = (Object)adaptor.create(string_literal259);
					adaptor.addChild(root_0, string_literal259_tree);
					}

					}
					break;

			}

			char_literal260=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2680); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal260_tree = (Object)adaptor.create(char_literal260);
			adaptor.addChild(root_0, char_literal260_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:296:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' ;
	public final JPA2Parser.date_after_macro_expression_return date_after_macro_expression() throws RecognitionException {
		JPA2Parser.date_after_macro_expression_return retval = new JPA2Parser.date_after_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal261=null;
		Token char_literal262=null;
		Token char_literal264=null;
		Token char_literal267=null;
		Token string_literal268=null;
		Token char_literal269=null;
		ParserRuleReturnScope path_expression263 =null;
		ParserRuleReturnScope path_expression265 =null;
		ParserRuleReturnScope input_parameter266 =null;

		Object string_literal261_tree=null;
		Object char_literal262_tree=null;
		Object char_literal264_tree=null;
		Object char_literal267_tree=null;
		Object string_literal268_tree=null;
		Object char_literal269_tree=null;

		try {
			// JPA2.g:297:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' )
			// JPA2.g:297:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal261=(Token)match(input,79,FOLLOW_79_in_date_after_macro_expression2692); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal261_tree = (Object)adaptor.create(string_literal261);
			adaptor.addChild(root_0, string_literal261_tree);
			}

			char_literal262=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2694); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal262_tree = (Object)adaptor.create(char_literal262);
			adaptor.addChild(root_0, char_literal262_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2696);
			path_expression263=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression263.getTree());

			char_literal264=(Token)match(input,66,FOLLOW_66_in_date_after_macro_expression2698); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal264_tree = (Object)adaptor.create(char_literal264);
			adaptor.addChild(root_0, char_literal264_tree);
			}

			// JPA2.g:297:44: ( path_expression | input_parameter )
			int alt73=2;
			int LA73_0 = input.LA(1);
			if ( (LA73_0==GROUP||LA73_0==WORD) ) {
				alt73=1;
			}
			else if ( (LA73_0==NAMED_PARAMETER||LA73_0==63||LA73_0==77) ) {
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
					// JPA2.g:297:45: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2701);
					path_expression265=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression265.getTree());

					}
					break;
				case 2 :
					// JPA2.g:297:63: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2705);
					input_parameter266=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter266.getTree());

					}
					break;

			}

			// JPA2.g:297:80: ( ',' 'USER_TIMEZONE' )?
			int alt74=2;
			int LA74_0 = input.LA(1);
			if ( (LA74_0==66) ) {
				alt74=1;
			}
			switch (alt74) {
				case 1 :
					// JPA2.g:297:81: ',' 'USER_TIMEZONE'
					{
					char_literal267=(Token)match(input,66,FOLLOW_66_in_date_after_macro_expression2709); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal267_tree = (Object)adaptor.create(char_literal267);
					adaptor.addChild(root_0, char_literal267_tree);
					}

					string_literal268=(Token)match(input,140,FOLLOW_140_in_date_after_macro_expression2711); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal268_tree = (Object)adaptor.create(string_literal268);
					adaptor.addChild(root_0, string_literal268_tree);
					}

					}
					break;

			}

			char_literal269=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2715); if (state.failed) return retval;
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
	// $ANTLR end "date_after_macro_expression"


	public static class date_equals_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_equals_macro_expression"
	// JPA2.g:299:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' ;
	public final JPA2Parser.date_equals_macro_expression_return date_equals_macro_expression() throws RecognitionException {
		JPA2Parser.date_equals_macro_expression_return retval = new JPA2Parser.date_equals_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal270=null;
		Token char_literal271=null;
		Token char_literal273=null;
		Token char_literal276=null;
		Token string_literal277=null;
		Token char_literal278=null;
		ParserRuleReturnScope path_expression272 =null;
		ParserRuleReturnScope path_expression274 =null;
		ParserRuleReturnScope input_parameter275 =null;

		Object string_literal270_tree=null;
		Object char_literal271_tree=null;
		Object char_literal273_tree=null;
		Object char_literal276_tree=null;
		Object string_literal277_tree=null;
		Object char_literal278_tree=null;

		try {
			// JPA2.g:300:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')' )
			// JPA2.g:300:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ( ',' 'USER_TIMEZONE' )? ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal270=(Token)match(input,81,FOLLOW_81_in_date_equals_macro_expression2727); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal270_tree = (Object)adaptor.create(string_literal270);
			adaptor.addChild(root_0, string_literal270_tree);
			}

			char_literal271=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2729); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal271_tree = (Object)adaptor.create(char_literal271);
			adaptor.addChild(root_0, char_literal271_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2731);
			path_expression272=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression272.getTree());

			char_literal273=(Token)match(input,66,FOLLOW_66_in_date_equals_macro_expression2733); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal273_tree = (Object)adaptor.create(char_literal273);
			adaptor.addChild(root_0, char_literal273_tree);
			}

			// JPA2.g:300:45: ( path_expression | input_parameter )
			int alt75=2;
			int LA75_0 = input.LA(1);
			if ( (LA75_0==GROUP||LA75_0==WORD) ) {
				alt75=1;
			}
			else if ( (LA75_0==NAMED_PARAMETER||LA75_0==63||LA75_0==77) ) {
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
					// JPA2.g:300:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2736);
					path_expression274=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression274.getTree());

					}
					break;
				case 2 :
					// JPA2.g:300:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2740);
					input_parameter275=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter275.getTree());

					}
					break;

			}

			// JPA2.g:300:81: ( ',' 'USER_TIMEZONE' )?
			int alt76=2;
			int LA76_0 = input.LA(1);
			if ( (LA76_0==66) ) {
				alt76=1;
			}
			switch (alt76) {
				case 1 :
					// JPA2.g:300:82: ',' 'USER_TIMEZONE'
					{
					char_literal276=(Token)match(input,66,FOLLOW_66_in_date_equals_macro_expression2744); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal276_tree = (Object)adaptor.create(char_literal276);
					adaptor.addChild(root_0, char_literal276_tree);
					}

					string_literal277=(Token)match(input,140,FOLLOW_140_in_date_equals_macro_expression2746); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal277_tree = (Object)adaptor.create(string_literal277);
					adaptor.addChild(root_0, string_literal277_tree);
					}

					}
					break;

			}

			char_literal278=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2750); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal278_tree = (Object)adaptor.create(char_literal278);
			adaptor.addChild(root_0, char_literal278_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:302:1: date_today_macro_expression : '@TODAY' '(' path_expression ( ',' 'USER_TIMEZONE' )? ')' ;
	public final JPA2Parser.date_today_macro_expression_return date_today_macro_expression() throws RecognitionException {
		JPA2Parser.date_today_macro_expression_return retval = new JPA2Parser.date_today_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal279=null;
		Token char_literal280=null;
		Token char_literal282=null;
		Token string_literal283=null;
		Token char_literal284=null;
		ParserRuleReturnScope path_expression281 =null;

		Object string_literal279_tree=null;
		Object char_literal280_tree=null;
		Object char_literal282_tree=null;
		Object string_literal283_tree=null;
		Object char_literal284_tree=null;

		try {
			// JPA2.g:303:5: ( '@TODAY' '(' path_expression ( ',' 'USER_TIMEZONE' )? ')' )
			// JPA2.g:303:7: '@TODAY' '(' path_expression ( ',' 'USER_TIMEZONE' )? ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal279=(Token)match(input,83,FOLLOW_83_in_date_today_macro_expression2762); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal279_tree = (Object)adaptor.create(string_literal279);
			adaptor.addChild(root_0, string_literal279_tree);
			}

			char_literal280=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2764); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal280_tree = (Object)adaptor.create(char_literal280);
			adaptor.addChild(root_0, char_literal280_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2766);
			path_expression281=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression281.getTree());

			// JPA2.g:303:36: ( ',' 'USER_TIMEZONE' )?
			int alt77=2;
			int LA77_0 = input.LA(1);
			if ( (LA77_0==66) ) {
				alt77=1;
			}
			switch (alt77) {
				case 1 :
					// JPA2.g:303:37: ',' 'USER_TIMEZONE'
					{
					char_literal282=(Token)match(input,66,FOLLOW_66_in_date_today_macro_expression2769); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal282_tree = (Object)adaptor.create(char_literal282);
					adaptor.addChild(root_0, char_literal282_tree);
					}

					string_literal283=(Token)match(input,140,FOLLOW_140_in_date_today_macro_expression2771); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal283_tree = (Object)adaptor.create(string_literal283);
					adaptor.addChild(root_0, string_literal283_tree);
					}

					}
					break;

			}

			char_literal284=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2775); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal284_tree = (Object)adaptor.create(char_literal284);
			adaptor.addChild(root_0, char_literal284_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal286=null;
		Token string_literal287=null;
		Token string_literal289=null;
		Token string_literal292=null;
		Token string_literal293=null;
		Token string_literal295=null;
		Token string_literal298=null;
		Token string_literal299=null;
		Token string_literal301=null;
		ParserRuleReturnScope arithmetic_expression285 =null;
		ParserRuleReturnScope arithmetic_expression288 =null;
		ParserRuleReturnScope arithmetic_expression290 =null;
		ParserRuleReturnScope string_expression291 =null;
		ParserRuleReturnScope string_expression294 =null;
		ParserRuleReturnScope string_expression296 =null;
		ParserRuleReturnScope datetime_expression297 =null;
		ParserRuleReturnScope datetime_expression300 =null;
		ParserRuleReturnScope datetime_expression302 =null;

		Object string_literal286_tree=null;
		Object string_literal287_tree=null;
		Object string_literal289_tree=null;
		Object string_literal292_tree=null;
		Object string_literal293_tree=null;
		Object string_literal295_tree=null;
		Object string_literal298_tree=null;
		Object string_literal299_tree=null;
		Object string_literal301_tree=null;

		try {
			// JPA2.g:307:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
			int alt81=3;
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
			case 77:
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
			case 63:
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
			case 104:
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
			case CASE:
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
			case 90:
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
			case 120:
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
			case 89:
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
			case 102:
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
			case 82:
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
			case 91:
			case 133:
			case 136:
			case 139:
				{
				alt81=2;
				}
				break;
			case 92:
			case 93:
			case 94:
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
					// JPA2.g:307:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2788);
					arithmetic_expression285=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression285.getTree());

					// JPA2.g:307:29: ( 'NOT' )?
					int alt78=2;
					int LA78_0 = input.LA(1);
					if ( (LA78_0==NOT) ) {
						alt78=1;
					}
					switch (alt78) {
						case 1 :
							// JPA2.g:307:30: 'NOT'
							{
							string_literal286=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2791); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal286_tree = (Object)adaptor.create(string_literal286);
							adaptor.addChild(root_0, string_literal286_tree);
							}

							}
							break;

					}

					string_literal287=(Token)match(input,87,FOLLOW_87_in_between_expression2795); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal287_tree = (Object)adaptor.create(string_literal287);
					adaptor.addChild(root_0, string_literal287_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2797);
					arithmetic_expression288=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression288.getTree());

					string_literal289=(Token)match(input,AND,FOLLOW_AND_in_between_expression2799); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal289_tree = (Object)adaptor.create(string_literal289);
					adaptor.addChild(root_0, string_literal289_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2801);
					arithmetic_expression290=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression290.getTree());

					}
					break;
				case 2 :
					// JPA2.g:308:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_between_expression2809);
					string_expression291=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression291.getTree());

					// JPA2.g:308:25: ( 'NOT' )?
					int alt79=2;
					int LA79_0 = input.LA(1);
					if ( (LA79_0==NOT) ) {
						alt79=1;
					}
					switch (alt79) {
						case 1 :
							// JPA2.g:308:26: 'NOT'
							{
							string_literal292=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2812); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal292_tree = (Object)adaptor.create(string_literal292);
							adaptor.addChild(root_0, string_literal292_tree);
							}

							}
							break;

					}

					string_literal293=(Token)match(input,87,FOLLOW_87_in_between_expression2816); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal293_tree = (Object)adaptor.create(string_literal293);
					adaptor.addChild(root_0, string_literal293_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2818);
					string_expression294=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression294.getTree());

					string_literal295=(Token)match(input,AND,FOLLOW_AND_in_between_expression2820); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal295_tree = (Object)adaptor.create(string_literal295);
					adaptor.addChild(root_0, string_literal295_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2822);
					string_expression296=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression296.getTree());

					}
					break;
				case 3 :
					// JPA2.g:309:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_between_expression2830);
					datetime_expression297=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression297.getTree());

					// JPA2.g:309:27: ( 'NOT' )?
					int alt80=2;
					int LA80_0 = input.LA(1);
					if ( (LA80_0==NOT) ) {
						alt80=1;
					}
					switch (alt80) {
						case 1 :
							// JPA2.g:309:28: 'NOT'
							{
							string_literal298=(Token)match(input,NOT,FOLLOW_NOT_in_between_expression2833); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal298_tree = (Object)adaptor.create(string_literal298);
							adaptor.addChild(root_0, string_literal298_tree);
							}

							}
							break;

					}

					string_literal299=(Token)match(input,87,FOLLOW_87_in_between_expression2837); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal299_tree = (Object)adaptor.create(string_literal299);
					adaptor.addChild(root_0, string_literal299_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2839);
					datetime_expression300=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression300.getTree());

					string_literal301=(Token)match(input,AND,FOLLOW_AND_in_between_expression2841); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal301_tree = (Object)adaptor.create(string_literal301);
					adaptor.addChild(root_0, string_literal301_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2843);
					datetime_expression302=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression302.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token NOT306=null;
		Token IN307=null;
		Token char_literal308=null;
		Token char_literal310=null;
		Token char_literal312=null;
		Token char_literal315=null;
		Token char_literal317=null;
		ParserRuleReturnScope path_expression303 =null;
		ParserRuleReturnScope type_discriminator304 =null;
		ParserRuleReturnScope identification_variable305 =null;
		ParserRuleReturnScope in_item309 =null;
		ParserRuleReturnScope in_item311 =null;
		ParserRuleReturnScope subquery313 =null;
		ParserRuleReturnScope collection_valued_input_parameter314 =null;
		ParserRuleReturnScope path_expression316 =null;

		Object NOT306_tree=null;
		Object IN307_tree=null;
		Object char_literal308_tree=null;
		Object char_literal310_tree=null;
		Object char_literal312_tree=null;
		Object char_literal315_tree=null;
		Object char_literal317_tree=null;

		try {
			// JPA2.g:311:5: ( ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' ) )
			// JPA2.g:311:7: ( path_expression | type_discriminator | identification_variable ) ( NOT )? IN ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:311:7: ( path_expression | type_discriminator | identification_variable )
			int alt82=3;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==GROUP||LA82_0==WORD) ) {
				int LA82_1 = input.LA(2);
				if ( (LA82_1==68) ) {
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
			else if ( (LA82_0==137) ) {
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
					// JPA2.g:311:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_in_expression2855);
					path_expression303=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression303.getTree());

					}
					break;
				case 2 :
					// JPA2.g:311:26: type_discriminator
					{
					pushFollow(FOLLOW_type_discriminator_in_in_expression2859);
					type_discriminator304=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator304.getTree());

					}
					break;
				case 3 :
					// JPA2.g:311:47: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_in_expression2863);
					identification_variable305=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable305.getTree());

					}
					break;

			}

			// JPA2.g:311:72: ( NOT )?
			int alt83=2;
			int LA83_0 = input.LA(1);
			if ( (LA83_0==NOT) ) {
				alt83=1;
			}
			switch (alt83) {
				case 1 :
					// JPA2.g:311:73: NOT
					{
					NOT306=(Token)match(input,NOT,FOLLOW_NOT_in_in_expression2867); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT306_tree = (Object)adaptor.create(NOT306);
					adaptor.addChild(root_0, NOT306_tree);
					}

					}
					break;

			}

			IN307=(Token)match(input,IN,FOLLOW_IN_in_in_expression2871); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN307_tree = (Object)adaptor.create(IN307);
			adaptor.addChild(root_0, IN307_tree);
			}

			// JPA2.g:312:13: ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter | '(' path_expression ')' )
			int alt85=4;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==LPAREN) ) {
				switch ( input.LA(2) ) {
				case 129:
					{
					alt85=2;
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
			else if ( (LA85_0==NAMED_PARAMETER||LA85_0==63||LA85_0==77) ) {
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
					// JPA2.g:312:15: '(' in_item ( ',' in_item )* ')'
					{
					char_literal308=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2887); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal308_tree = (Object)adaptor.create(char_literal308);
					adaptor.addChild(root_0, char_literal308_tree);
					}

					pushFollow(FOLLOW_in_item_in_in_expression2889);
					in_item309=in_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item309.getTree());

					// JPA2.g:312:27: ( ',' in_item )*
					loop84:
					while (true) {
						int alt84=2;
						int LA84_0 = input.LA(1);
						if ( (LA84_0==66) ) {
							alt84=1;
						}

						switch (alt84) {
						case 1 :
							// JPA2.g:312:28: ',' in_item
							{
							char_literal310=(Token)match(input,66,FOLLOW_66_in_in_expression2892); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal310_tree = (Object)adaptor.create(char_literal310);
							adaptor.addChild(root_0, char_literal310_tree);
							}

							pushFollow(FOLLOW_in_item_in_in_expression2894);
							in_item311=in_item();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item311.getTree());

							}
							break;

						default :
							break loop84;
						}
					}

					char_literal312=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2898); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal312_tree = (Object)adaptor.create(char_literal312);
					adaptor.addChild(root_0, char_literal312_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:313:15: subquery
					{
					pushFollow(FOLLOW_subquery_in_in_expression2914);
					subquery313=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery313.getTree());

					}
					break;
				case 3 :
					// JPA2.g:314:15: collection_valued_input_parameter
					{
					pushFollow(FOLLOW_collection_valued_input_parameter_in_in_expression2930);
					collection_valued_input_parameter314=collection_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_input_parameter314.getTree());

					}
					break;
				case 4 :
					// JPA2.g:315:15: '(' path_expression ')'
					{
					char_literal315=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2946); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal315_tree = (Object)adaptor.create(char_literal315);
					adaptor.addChild(root_0, char_literal315_tree);
					}

					pushFollow(FOLLOW_path_expression_in_in_expression2948);
					path_expression316=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression316.getTree());

					char_literal317=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2950); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal317_tree = (Object)adaptor.create(char_literal317);
					adaptor.addChild(root_0, char_literal317_tree);
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

		ParserRuleReturnScope string_literal318 =null;
		ParserRuleReturnScope numeric_literal319 =null;
		ParserRuleReturnScope single_valued_input_parameter320 =null;
		ParserRuleReturnScope enum_function321 =null;


		try {
			// JPA2.g:322:5: ( string_literal | numeric_literal | single_valued_input_parameter | enum_function )
			int alt86=4;
			switch ( input.LA(1) ) {
			case STRING_LITERAL:
				{
				alt86=1;
				}
				break;
			case INT_NUMERAL:
			case 70:
				{
				alt86=2;
				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
				{
				alt86=3;
				}
				break;
			case 82:
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
					// JPA2.g:322:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_in_item2978);
					string_literal318=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal318.getTree());

					}
					break;
				case 2 :
					// JPA2.g:322:24: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_in_item2982);
					numeric_literal319=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal319.getTree());

					}
					break;
				case 3 :
					// JPA2.g:322:42: single_valued_input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_single_valued_input_parameter_in_in_item2986);
					single_valued_input_parameter320=single_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_input_parameter320.getTree());

					}
					break;
				case 4 :
					// JPA2.g:322:74: enum_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_function_in_in_item2990);
					enum_function321=enum_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_function321.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal323=null;
		Token string_literal324=null;
		Token string_literal328=null;
		ParserRuleReturnScope string_expression322 =null;
		ParserRuleReturnScope string_expression325 =null;
		ParserRuleReturnScope pattern_value326 =null;
		ParserRuleReturnScope input_parameter327 =null;
		ParserRuleReturnScope escape_character329 =null;

		Object string_literal323_tree=null;
		Object string_literal324_tree=null;
		Object string_literal328_tree=null;

		try {
			// JPA2.g:324:5: ( string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? )
			// JPA2.g:324:7: string_expression ( 'NOT' )? 'LIKE' ( string_expression | pattern_value | input_parameter ) ( 'ESCAPE' escape_character )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_expression_in_like_expression3001);
			string_expression322=string_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression322.getTree());

			// JPA2.g:324:25: ( 'NOT' )?
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==NOT) ) {
				alt87=1;
			}
			switch (alt87) {
				case 1 :
					// JPA2.g:324:26: 'NOT'
					{
					string_literal323=(Token)match(input,NOT,FOLLOW_NOT_in_like_expression3004); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal323_tree = (Object)adaptor.create(string_literal323);
					adaptor.addChild(root_0, string_literal323_tree);
					}

					}
					break;

			}

			string_literal324=(Token)match(input,111,FOLLOW_111_in_like_expression3008); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal324_tree = (Object)adaptor.create(string_literal324);
			adaptor.addChild(root_0, string_literal324_tree);
			}

			// JPA2.g:324:41: ( string_expression | pattern_value | input_parameter )
			int alt88=3;
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
			case 77:
				{
				int LA88_3 = input.LA(2);
				if ( (LA88_3==70) ) {
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
			case 63:
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
					// JPA2.g:324:42: string_expression
					{
					pushFollow(FOLLOW_string_expression_in_like_expression3011);
					string_expression325=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression325.getTree());

					}
					break;
				case 2 :
					// JPA2.g:324:62: pattern_value
					{
					pushFollow(FOLLOW_pattern_value_in_like_expression3015);
					pattern_value326=pattern_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value326.getTree());

					}
					break;
				case 3 :
					// JPA2.g:324:78: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_like_expression3019);
					input_parameter327=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter327.getTree());

					}
					break;

			}

			// JPA2.g:324:94: ( 'ESCAPE' escape_character )?
			int alt89=2;
			int LA89_0 = input.LA(1);
			if ( (LA89_0==100) ) {
				alt89=1;
			}
			switch (alt89) {
				case 1 :
					// JPA2.g:324:95: 'ESCAPE' escape_character
					{
					string_literal328=(Token)match(input,100,FOLLOW_100_in_like_expression3022); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal328_tree = (Object)adaptor.create(string_literal328);
					adaptor.addChild(root_0, string_literal328_tree);
					}

					pushFollow(FOLLOW_escape_character_in_like_expression3024);
					escape_character329=escape_character();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, escape_character329.getTree());

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

		Token string_literal333=null;
		Token string_literal334=null;
		Token string_literal335=null;
		ParserRuleReturnScope path_expression330 =null;
		ParserRuleReturnScope input_parameter331 =null;
		ParserRuleReturnScope join_association_path_expression332 =null;

		Object string_literal333_tree=null;
		Object string_literal334_tree=null;
		Object string_literal335_tree=null;

		try {
			// JPA2.g:326:5: ( ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL' )
			// JPA2.g:326:7: ( path_expression | input_parameter | join_association_path_expression ) 'IS' ( 'NOT' )? 'NULL'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:326:7: ( path_expression | input_parameter | join_association_path_expression )
			int alt90=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA90_1 = input.LA(2);
				if ( (LA90_1==68) ) {
					int LA90_5 = input.LA(3);
					if ( (synpred146_JPA2()) ) {
						alt90=1;
					}
					else if ( (true) ) {
						alt90=3;
					}

				}
				else if ( (LA90_1==107) ) {
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
			case 63:
			case 77:
				{
				alt90=2;
				}
				break;
			case 135:
				{
				alt90=3;
				}
				break;
			case GROUP:
				{
				int LA90_4 = input.LA(2);
				if ( (LA90_4==68) ) {
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
					// JPA2.g:326:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_null_comparison_expression3038);
					path_expression330=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression330.getTree());

					}
					break;
				case 2 :
					// JPA2.g:326:26: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_null_comparison_expression3042);
					input_parameter331=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter331.getTree());

					}
					break;
				case 3 :
					// JPA2.g:326:44: join_association_path_expression
					{
					pushFollow(FOLLOW_join_association_path_expression_in_null_comparison_expression3046);
					join_association_path_expression332=join_association_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression332.getTree());

					}
					break;

			}

			string_literal333=(Token)match(input,107,FOLLOW_107_in_null_comparison_expression3049); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal333_tree = (Object)adaptor.create(string_literal333);
			adaptor.addChild(root_0, string_literal333_tree);
			}

			// JPA2.g:326:83: ( 'NOT' )?
			int alt91=2;
			int LA91_0 = input.LA(1);
			if ( (LA91_0==NOT) ) {
				alt91=1;
			}
			switch (alt91) {
				case 1 :
					// JPA2.g:326:84: 'NOT'
					{
					string_literal334=(Token)match(input,NOT,FOLLOW_NOT_in_null_comparison_expression3052); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal334_tree = (Object)adaptor.create(string_literal334);
					adaptor.addChild(root_0, string_literal334_tree);
					}

					}
					break;

			}

			string_literal335=(Token)match(input,119,FOLLOW_119_in_null_comparison_expression3056); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal335_tree = (Object)adaptor.create(string_literal335);
			adaptor.addChild(root_0, string_literal335_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal337=null;
		Token string_literal338=null;
		Token string_literal339=null;
		ParserRuleReturnScope path_expression336 =null;

		Object string_literal337_tree=null;
		Object string_literal338_tree=null;
		Object string_literal339_tree=null;

		try {
			// JPA2.g:328:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
			// JPA2.g:328:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression3067);
			path_expression336=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression336.getTree());

			string_literal337=(Token)match(input,107,FOLLOW_107_in_empty_collection_comparison_expression3069); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal337_tree = (Object)adaptor.create(string_literal337);
			adaptor.addChild(root_0, string_literal337_tree);
			}

			// JPA2.g:328:28: ( 'NOT' )?
			int alt92=2;
			int LA92_0 = input.LA(1);
			if ( (LA92_0==NOT) ) {
				alt92=1;
			}
			switch (alt92) {
				case 1 :
					// JPA2.g:328:29: 'NOT'
					{
					string_literal338=(Token)match(input,NOT,FOLLOW_NOT_in_empty_collection_comparison_expression3072); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal338_tree = (Object)adaptor.create(string_literal338);
					adaptor.addChild(root_0, string_literal338_tree);
					}

					}
					break;

			}

			string_literal339=(Token)match(input,97,FOLLOW_97_in_empty_collection_comparison_expression3076); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal339_tree = (Object)adaptor.create(string_literal339);
			adaptor.addChild(root_0, string_literal339_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal341=null;
		Token string_literal342=null;
		Token string_literal343=null;
		ParserRuleReturnScope entity_or_value_expression340 =null;
		ParserRuleReturnScope path_expression344 =null;

		Object string_literal341_tree=null;
		Object string_literal342_tree=null;
		Object string_literal343_tree=null;

		try {
			// JPA2.g:330:5: ( entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
			// JPA2.g:330:7: entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_or_value_expression_in_collection_member_expression3087);
			entity_or_value_expression340=entity_or_value_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_or_value_expression340.getTree());

			// JPA2.g:330:35: ( 'NOT' )?
			int alt93=2;
			int LA93_0 = input.LA(1);
			if ( (LA93_0==NOT) ) {
				alt93=1;
			}
			switch (alt93) {
				case 1 :
					// JPA2.g:330:36: 'NOT'
					{
					string_literal341=(Token)match(input,NOT,FOLLOW_NOT_in_collection_member_expression3091); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal341_tree = (Object)adaptor.create(string_literal341);
					adaptor.addChild(root_0, string_literal341_tree);
					}

					}
					break;

			}

			string_literal342=(Token)match(input,113,FOLLOW_113_in_collection_member_expression3095); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal342_tree = (Object)adaptor.create(string_literal342);
			adaptor.addChild(root_0, string_literal342_tree);
			}

			// JPA2.g:330:53: ( 'OF' )?
			int alt94=2;
			int LA94_0 = input.LA(1);
			if ( (LA94_0==124) ) {
				alt94=1;
			}
			switch (alt94) {
				case 1 :
					// JPA2.g:330:54: 'OF'
					{
					string_literal343=(Token)match(input,124,FOLLOW_124_in_collection_member_expression3098); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal343_tree = (Object)adaptor.create(string_literal343);
					adaptor.addChild(root_0, string_literal343_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_path_expression_in_collection_member_expression3102);
			path_expression344=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression344.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope path_expression345 =null;
		ParserRuleReturnScope simple_entity_or_value_expression346 =null;
		ParserRuleReturnScope subquery347 =null;


		try {
			// JPA2.g:332:5: ( path_expression | simple_entity_or_value_expression | subquery )
			int alt95=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA95_1 = input.LA(2);
				if ( (LA95_1==68) ) {
					alt95=1;
				}
				else if ( (LA95_1==NOT||LA95_1==113) ) {
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
			case 63:
			case 77:
				{
				alt95=2;
				}
				break;
			case GROUP:
				{
				int LA95_3 = input.LA(2);
				if ( (LA95_3==68) ) {
					alt95=1;
				}
				else if ( (LA95_3==NOT||LA95_3==113) ) {
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
					// JPA2.g:332:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_or_value_expression3113);
					path_expression345=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression345.getTree());

					}
					break;
				case 2 :
					// JPA2.g:333:7: simple_entity_or_value_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression3121);
					simple_entity_or_value_expression346=simple_entity_or_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_or_value_expression346.getTree());

					}
					break;
				case 3 :
					// JPA2.g:334:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_entity_or_value_expression3129);
					subquery347=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery347.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope identification_variable348 =null;
		ParserRuleReturnScope input_parameter349 =null;
		ParserRuleReturnScope literal350 =null;


		try {
			// JPA2.g:336:5: ( identification_variable | input_parameter | literal )
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
			case 63:
			case 77:
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
					// JPA2.g:336:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_or_value_expression3140);
					identification_variable348=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable348.getTree());

					}
					break;
				case 2 :
					// JPA2.g:337:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_or_value_expression3148);
					input_parameter349=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter349.getTree());

					}
					break;
				case 3 :
					// JPA2.g:338:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_simple_entity_or_value_expression3156);
					literal350=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal350.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal351=null;
		Token string_literal352=null;
		ParserRuleReturnScope subquery353 =null;

		Object string_literal351_tree=null;
		Object string_literal352_tree=null;

		try {
			// JPA2.g:340:5: ( ( 'NOT' )? 'EXISTS' subquery )
			// JPA2.g:340:7: ( 'NOT' )? 'EXISTS' subquery
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:340:7: ( 'NOT' )?
			int alt97=2;
			int LA97_0 = input.LA(1);
			if ( (LA97_0==NOT) ) {
				alt97=1;
			}
			switch (alt97) {
				case 1 :
					// JPA2.g:340:8: 'NOT'
					{
					string_literal351=(Token)match(input,NOT,FOLLOW_NOT_in_exists_expression3168); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal351_tree = (Object)adaptor.create(string_literal351);
					adaptor.addChild(root_0, string_literal351_tree);
					}

					}
					break;

			}

			string_literal352=(Token)match(input,101,FOLLOW_101_in_exists_expression3172); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal352_tree = (Object)adaptor.create(string_literal352);
			adaptor.addChild(root_0, string_literal352_tree);
			}

			pushFollow(FOLLOW_subquery_in_exists_expression3174);
			subquery353=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery353.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token set354=null;
		ParserRuleReturnScope subquery355 =null;

		Object set354_tree=null;

		try {
			// JPA2.g:342:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
			// JPA2.g:342:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
			{
			root_0 = (Object)adaptor.nil();


			set354=input.LT(1);
			if ( (input.LA(1) >= 85 && input.LA(1) <= 86)||input.LA(1)==131 ) {
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
			pushFollow(FOLLOW_subquery_in_all_or_any_expression3198);
			subquery355=subquery();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery355.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal358=null;
		Token set362=null;
		Token set366=null;
		Token set374=null;
		Token set378=null;
		ParserRuleReturnScope string_expression356 =null;
		ParserRuleReturnScope comparison_operator357 =null;
		ParserRuleReturnScope string_expression359 =null;
		ParserRuleReturnScope all_or_any_expression360 =null;
		ParserRuleReturnScope boolean_expression361 =null;
		ParserRuleReturnScope boolean_expression363 =null;
		ParserRuleReturnScope all_or_any_expression364 =null;
		ParserRuleReturnScope enum_expression365 =null;
		ParserRuleReturnScope enum_expression367 =null;
		ParserRuleReturnScope all_or_any_expression368 =null;
		ParserRuleReturnScope datetime_expression369 =null;
		ParserRuleReturnScope comparison_operator370 =null;
		ParserRuleReturnScope datetime_expression371 =null;
		ParserRuleReturnScope all_or_any_expression372 =null;
		ParserRuleReturnScope entity_expression373 =null;
		ParserRuleReturnScope entity_expression375 =null;
		ParserRuleReturnScope all_or_any_expression376 =null;
		ParserRuleReturnScope entity_type_expression377 =null;
		ParserRuleReturnScope entity_type_expression379 =null;
		ParserRuleReturnScope arithmetic_expression380 =null;
		ParserRuleReturnScope comparison_operator381 =null;
		ParserRuleReturnScope arithmetic_expression382 =null;
		ParserRuleReturnScope all_or_any_expression383 =null;

		Object string_literal358_tree=null;
		Object set362_tree=null;
		Object set366_tree=null;
		Object set374_tree=null;
		Object set378_tree=null;

		try {
			// JPA2.g:344:5: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
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
			case 91:
			case 133:
			case 136:
			case 139:
				{
				alt105=1;
				}
				break;
			case 77:
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
			case 63:
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
			case 104:
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
			case CASE:
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
			case 90:
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
			case 120:
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
			case 89:
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
			case 102:
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
			case 82:
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
			case 92:
			case 93:
			case 94:
				{
				alt105=4;
				}
				break;
			case 137:
				{
				alt105=6;
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
					// JPA2.g:344:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_comparison_expression3209);
					string_expression356=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression356.getTree());

					// JPA2.g:344:25: ( comparison_operator | 'REGEXP' )
					int alt98=2;
					int LA98_0 = input.LA(1);
					if ( ((LA98_0 >= 71 && LA98_0 <= 76)) ) {
						alt98=1;
					}
					else if ( (LA98_0==127) ) {
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
							// JPA2.g:344:26: comparison_operator
							{
							pushFollow(FOLLOW_comparison_operator_in_comparison_expression3212);
							comparison_operator357=comparison_operator();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator357.getTree());

							}
							break;
						case 2 :
							// JPA2.g:344:48: 'REGEXP'
							{
							string_literal358=(Token)match(input,127,FOLLOW_127_in_comparison_expression3216); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal358_tree = (Object)adaptor.create(string_literal358);
							adaptor.addChild(root_0, string_literal358_tree);
							}

							}
							break;

					}

					// JPA2.g:344:58: ( string_expression | all_or_any_expression )
					int alt99=2;
					int LA99_0 = input.LA(1);
					if ( (LA99_0==AVG||LA99_0==CASE||LA99_0==COUNT||LA99_0==GROUP||(LA99_0 >= LOWER && LA99_0 <= NAMED_PARAMETER)||(LA99_0 >= STRING_LITERAL && LA99_0 <= SUM)||LA99_0==WORD||LA99_0==63||LA99_0==77||LA99_0==82||(LA99_0 >= 89 && LA99_0 <= 91)||LA99_0==102||LA99_0==104||LA99_0==120||LA99_0==133||LA99_0==136||LA99_0==139) ) {
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
							// JPA2.g:344:59: string_expression
							{
							pushFollow(FOLLOW_string_expression_in_comparison_expression3220);
							string_expression359=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression359.getTree());

							}
							break;
						case 2 :
							// JPA2.g:344:79: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3224);
							all_or_any_expression360=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression360.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:345:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_comparison_expression3233);
					boolean_expression361=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression361.getTree());

					set362=input.LT(1);
					if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
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
					// JPA2.g:345:39: ( boolean_expression | all_or_any_expression )
					int alt100=2;
					int LA100_0 = input.LA(1);
					if ( (LA100_0==CASE||LA100_0==GROUP||LA100_0==LPAREN||LA100_0==NAMED_PARAMETER||LA100_0==WORD||LA100_0==63||LA100_0==77||LA100_0==82||(LA100_0 >= 89 && LA100_0 <= 90)||LA100_0==102||LA100_0==104||LA100_0==120||(LA100_0 >= 145 && LA100_0 <= 146)) ) {
						alt100=1;
					}
					else if ( ((LA100_0 >= 85 && LA100_0 <= 86)||LA100_0==131) ) {
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
							// JPA2.g:345:40: boolean_expression
							{
							pushFollow(FOLLOW_boolean_expression_in_comparison_expression3244);
							boolean_expression363=boolean_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression363.getTree());

							}
							break;
						case 2 :
							// JPA2.g:345:61: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3248);
							all_or_any_expression364=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression364.getTree());

							}
							break;

					}

					}
					break;
				case 3 :
					// JPA2.g:346:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_comparison_expression3257);
					enum_expression365=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression365.getTree());

					set366=input.LT(1);
					if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
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
					// JPA2.g:346:34: ( enum_expression | all_or_any_expression )
					int alt101=2;
					int LA101_0 = input.LA(1);
					if ( (LA101_0==CASE||LA101_0==GROUP||LA101_0==LPAREN||LA101_0==NAMED_PARAMETER||LA101_0==WORD||LA101_0==63||LA101_0==77||LA101_0==90||LA101_0==120) ) {
						alt101=1;
					}
					else if ( ((LA101_0 >= 85 && LA101_0 <= 86)||LA101_0==131) ) {
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
							// JPA2.g:346:35: enum_expression
							{
							pushFollow(FOLLOW_enum_expression_in_comparison_expression3266);
							enum_expression367=enum_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression367.getTree());

							}
							break;
						case 2 :
							// JPA2.g:346:53: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3270);
							all_or_any_expression368=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression368.getTree());

							}
							break;

					}

					}
					break;
				case 4 :
					// JPA2.g:347:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_comparison_expression3279);
					datetime_expression369=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression369.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3281);
					comparison_operator370=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator370.getTree());

					// JPA2.g:347:47: ( datetime_expression | all_or_any_expression )
					int alt102=2;
					int LA102_0 = input.LA(1);
					if ( (LA102_0==AVG||LA102_0==CASE||LA102_0==COUNT||LA102_0==GROUP||(LA102_0 >= LPAREN && LA102_0 <= NAMED_PARAMETER)||LA102_0==SUM||LA102_0==WORD||LA102_0==63||LA102_0==77||LA102_0==82||(LA102_0 >= 89 && LA102_0 <= 90)||(LA102_0 >= 92 && LA102_0 <= 94)||LA102_0==102||LA102_0==104||LA102_0==120) ) {
						alt102=1;
					}
					else if ( ((LA102_0 >= 85 && LA102_0 <= 86)||LA102_0==131) ) {
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
							// JPA2.g:347:48: datetime_expression
							{
							pushFollow(FOLLOW_datetime_expression_in_comparison_expression3284);
							datetime_expression371=datetime_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression371.getTree());

							}
							break;
						case 2 :
							// JPA2.g:347:70: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3288);
							all_or_any_expression372=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression372.getTree());

							}
							break;

					}

					}
					break;
				case 5 :
					// JPA2.g:348:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_expression_in_comparison_expression3297);
					entity_expression373=entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression373.getTree());

					set374=input.LT(1);
					if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
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
					// JPA2.g:348:38: ( entity_expression | all_or_any_expression )
					int alt103=2;
					int LA103_0 = input.LA(1);
					if ( (LA103_0==GROUP||LA103_0==NAMED_PARAMETER||LA103_0==WORD||LA103_0==63||LA103_0==77) ) {
						alt103=1;
					}
					else if ( ((LA103_0 >= 85 && LA103_0 <= 86)||LA103_0==131) ) {
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
							// JPA2.g:348:39: entity_expression
							{
							pushFollow(FOLLOW_entity_expression_in_comparison_expression3308);
							entity_expression375=entity_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression375.getTree());

							}
							break;
						case 2 :
							// JPA2.g:348:59: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3312);
							all_or_any_expression376=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression376.getTree());

							}
							break;

					}

					}
					break;
				case 6 :
					// JPA2.g:349:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3321);
					entity_type_expression377=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression377.getTree());

					set378=input.LT(1);
					if ( (input.LA(1) >= 73 && input.LA(1) <= 74) ) {
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
					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3331);
					entity_type_expression379=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression379.getTree());

					}
					break;
				case 7 :
					// JPA2.g:350:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3339);
					arithmetic_expression380=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression380.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3341);
					comparison_operator381=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator381.getTree());

					// JPA2.g:350:49: ( arithmetic_expression | all_or_any_expression )
					int alt104=2;
					int LA104_0 = input.LA(1);
					if ( (LA104_0==AVG||LA104_0==CASE||LA104_0==COUNT||LA104_0==GROUP||LA104_0==INT_NUMERAL||(LA104_0 >= LPAREN && LA104_0 <= NAMED_PARAMETER)||LA104_0==SUM||LA104_0==WORD||LA104_0==63||LA104_0==65||LA104_0==67||LA104_0==70||LA104_0==77||LA104_0==82||LA104_0==84||(LA104_0 >= 89 && LA104_0 <= 90)||LA104_0==102||LA104_0==104||LA104_0==106||LA104_0==110||LA104_0==112||LA104_0==115||LA104_0==120||LA104_0==130||LA104_0==132) ) {
						alt104=1;
					}
					else if ( ((LA104_0 >= 85 && LA104_0 <= 86)||LA104_0==131) ) {
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
							// JPA2.g:350:50: arithmetic_expression
							{
							pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3344);
							arithmetic_expression382=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression382.getTree());

							}
							break;
						case 2 :
							// JPA2.g:350:74: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3348);
							all_or_any_expression383=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression383.getTree());

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

		Token set384=null;

		Object set384_tree=null;

		try {
			// JPA2.g:353:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set384=input.LT(1);
			if ( (input.LA(1) >= 71 && input.LA(1) <= 76) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set384));
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

		Token set386=null;
		ParserRuleReturnScope arithmetic_term385 =null;
		ParserRuleReturnScope arithmetic_term387 =null;
		ParserRuleReturnScope arithmetic_term388 =null;

		Object set386_tree=null;

		try {
			// JPA2.g:360:5: ( arithmetic_term ( ( '+' | '-' ) arithmetic_term )+ | arithmetic_term )
			int alt107=2;
			switch ( input.LA(1) ) {
			case 65:
			case 67:
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
			case 70:
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
			case 77:
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
			case 63:
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
			case 110:
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
			case 112:
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
			case 84:
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
			case 132:
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
			case 115:
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
			case 130:
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
			case 106:
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
			case 104:
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
			case CASE:
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
			case 90:
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
			case 120:
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
			case 89:
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
			case 102:
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
			case 82:
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
					// JPA2.g:360:7: arithmetic_term ( ( '+' | '-' ) arithmetic_term )+
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3412);
					arithmetic_term385=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term385.getTree());

					// JPA2.g:360:23: ( ( '+' | '-' ) arithmetic_term )+
					int cnt106=0;
					loop106:
					while (true) {
						int alt106=2;
						int LA106_0 = input.LA(1);
						if ( (LA106_0==65||LA106_0==67) ) {
							alt106=1;
						}

						switch (alt106) {
						case 1 :
							// JPA2.g:360:24: ( '+' | '-' ) arithmetic_term
							{
							set386=input.LT(1);
							if ( input.LA(1)==65||input.LA(1)==67 ) {
								input.consume();
								if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set386));
								state.errorRecovery=false;
								state.failed=false;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3423);
							arithmetic_term387=arithmetic_term();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term387.getTree());

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
					// JPA2.g:361:7: arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3433);
					arithmetic_term388=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term388.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token set390=null;
		ParserRuleReturnScope arithmetic_factor389 =null;
		ParserRuleReturnScope arithmetic_factor391 =null;
		ParserRuleReturnScope arithmetic_factor392 =null;

		Object set390_tree=null;

		try {
			// JPA2.g:363:5: ( arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+ | arithmetic_factor )
			int alt109=2;
			switch ( input.LA(1) ) {
			case 65:
			case 67:
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
			case 70:
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
			case 77:
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
			case 63:
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
			case 110:
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
			case 112:
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
			case 84:
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
			case 132:
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
			case 115:
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
			case 130:
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
			case 106:
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
			case 104:
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
			case CASE:
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
			case 90:
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
			case 120:
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
			case 89:
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
			case 102:
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
			case 82:
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
					// JPA2.g:363:7: arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3444);
					arithmetic_factor389=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor389.getTree());

					// JPA2.g:363:25: ( ( '*' | '/' ) arithmetic_factor )+
					int cnt108=0;
					loop108:
					while (true) {
						int alt108=2;
						int LA108_0 = input.LA(1);
						if ( (LA108_0==64||LA108_0==69) ) {
							alt108=1;
						}

						switch (alt108) {
						case 1 :
							// JPA2.g:363:26: ( '*' | '/' ) arithmetic_factor
							{
							set390=input.LT(1);
							if ( input.LA(1)==64||input.LA(1)==69 ) {
								input.consume();
								if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set390));
								state.errorRecovery=false;
								state.failed=false;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3456);
							arithmetic_factor391=arithmetic_factor();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor391.getTree());

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
					// JPA2.g:364:7: arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3466);
					arithmetic_factor392=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor392.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token set393=null;
		ParserRuleReturnScope arithmetic_primary394 =null;

		Object set393_tree=null;

		try {
			// JPA2.g:366:5: ( ( ( '+' | '-' ) )? arithmetic_primary )
			// JPA2.g:366:7: ( ( '+' | '-' ) )? arithmetic_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:366:7: ( ( '+' | '-' ) )?
			int alt110=2;
			int LA110_0 = input.LA(1);
			if ( (LA110_0==65||LA110_0==67) ) {
				alt110=1;
			}
			switch (alt110) {
				case 1 :
					// JPA2.g:
					{
					set393=input.LT(1);
					if ( input.LA(1)==65||input.LA(1)==67 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set393));
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

			pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor3489);
			arithmetic_primary394=arithmetic_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary394.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token char_literal398=null;
		Token char_literal400=null;
		ParserRuleReturnScope path_expression395 =null;
		ParserRuleReturnScope decimal_literal396 =null;
		ParserRuleReturnScope numeric_literal397 =null;
		ParserRuleReturnScope arithmetic_expression399 =null;
		ParserRuleReturnScope input_parameter401 =null;
		ParserRuleReturnScope functions_returning_numerics402 =null;
		ParserRuleReturnScope aggregate_expression403 =null;
		ParserRuleReturnScope case_expression404 =null;
		ParserRuleReturnScope function_invocation405 =null;
		ParserRuleReturnScope extension_functions406 =null;
		ParserRuleReturnScope subquery407 =null;

		Object char_literal398_tree=null;
		Object char_literal400_tree=null;

		try {
			// JPA2.g:368:5: ( path_expression | decimal_literal | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
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
			case 70:
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
			case 63:
			case 77:
				{
				alt111=5;
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
			case 104:
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
			case CASE:
			case 90:
			case 120:
				{
				alt111=8;
				}
				break;
			case 82:
			case 89:
			case 102:
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
					// JPA2.g:368:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_arithmetic_primary3500);
					path_expression395=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression395.getTree());

					}
					break;
				case 2 :
					// JPA2.g:369:7: decimal_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_decimal_literal_in_arithmetic_primary3508);
					decimal_literal396=decimal_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, decimal_literal396.getTree());

					}
					break;
				case 3 :
					// JPA2.g:370:7: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary3516);
					numeric_literal397=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal397.getTree());

					}
					break;
				case 4 :
					// JPA2.g:371:7: '(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal398=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary3524); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal398_tree = (Object)adaptor.create(char_literal398);
					adaptor.addChild(root_0, char_literal398_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_arithmetic_primary3525);
					arithmetic_expression399=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression399.getTree());

					char_literal400=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary3526); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal400_tree = (Object)adaptor.create(char_literal400);
					adaptor.addChild(root_0, char_literal400_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:372:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_arithmetic_primary3534);
					input_parameter401=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter401.getTree());

					}
					break;
				case 6 :
					// JPA2.g:373:7: functions_returning_numerics
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary3542);
					functions_returning_numerics402=functions_returning_numerics();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics402.getTree());

					}
					break;
				case 7 :
					// JPA2.g:374:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary3550);
					aggregate_expression403=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression403.getTree());

					}
					break;
				case 8 :
					// JPA2.g:375:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_arithmetic_primary3558);
					case_expression404=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression404.getTree());

					}
					break;
				case 9 :
					// JPA2.g:376:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_arithmetic_primary3566);
					function_invocation405=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation405.getTree());

					}
					break;
				case 10 :
					// JPA2.g:377:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_arithmetic_primary3574);
					extension_functions406=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions406.getTree());

					}
					break;
				case 11 :
					// JPA2.g:378:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_arithmetic_primary3582);
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

		ParserRuleReturnScope path_expression408 =null;
		ParserRuleReturnScope string_literal409 =null;
		ParserRuleReturnScope input_parameter410 =null;
		ParserRuleReturnScope functions_returning_strings411 =null;
		ParserRuleReturnScope aggregate_expression412 =null;
		ParserRuleReturnScope case_expression413 =null;
		ParserRuleReturnScope function_invocation414 =null;
		ParserRuleReturnScope extension_functions415 =null;
		ParserRuleReturnScope subquery416 =null;


		try {
			// JPA2.g:380:5: ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | extension_functions | subquery )
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
			case 63:
			case 77:
				{
				alt112=3;
				}
				break;
			case LOWER:
			case 91:
			case 133:
			case 136:
			case 139:
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
			case 104:
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
			case CASE:
			case 90:
			case 120:
				{
				alt112=6;
				}
				break;
			case 82:
			case 89:
			case 102:
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
					// JPA2.g:380:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_string_expression3593);
					path_expression408=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression408.getTree());

					}
					break;
				case 2 :
					// JPA2.g:381:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_string_expression3601);
					string_literal409=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal409.getTree());

					}
					break;
				case 3 :
					// JPA2.g:382:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_string_expression3609);
					input_parameter410=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter410.getTree());

					}
					break;
				case 4 :
					// JPA2.g:383:7: functions_returning_strings
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_strings_in_string_expression3617);
					functions_returning_strings411=functions_returning_strings();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings411.getTree());

					}
					break;
				case 5 :
					// JPA2.g:384:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_string_expression3625);
					aggregate_expression412=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression412.getTree());

					}
					break;
				case 6 :
					// JPA2.g:385:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_string_expression3633);
					case_expression413=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression413.getTree());

					}
					break;
				case 7 :
					// JPA2.g:386:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_string_expression3641);
					function_invocation414=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation414.getTree());

					}
					break;
				case 8 :
					// JPA2.g:387:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_string_expression3649);
					extension_functions415=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions415.getTree());

					}
					break;
				case 9 :
					// JPA2.g:388:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_string_expression3657);
					subquery416=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery416.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope path_expression417 =null;
		ParserRuleReturnScope input_parameter418 =null;
		ParserRuleReturnScope functions_returning_datetime419 =null;
		ParserRuleReturnScope aggregate_expression420 =null;
		ParserRuleReturnScope case_expression421 =null;
		ParserRuleReturnScope function_invocation422 =null;
		ParserRuleReturnScope extension_functions423 =null;
		ParserRuleReturnScope date_time_timestamp_literal424 =null;
		ParserRuleReturnScope subquery425 =null;


		try {
			// JPA2.g:390:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | extension_functions | date_time_timestamp_literal | subquery )
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
			case 63:
			case 77:
				{
				alt113=2;
				}
				break;
			case 92:
			case 93:
			case 94:
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
			case 104:
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
			case CASE:
			case 90:
			case 120:
				{
				alt113=5;
				}
				break;
			case 82:
			case 89:
			case 102:
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
					// JPA2.g:390:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_datetime_expression3668);
					path_expression417=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression417.getTree());

					}
					break;
				case 2 :
					// JPA2.g:391:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_datetime_expression3676);
					input_parameter418=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter418.getTree());

					}
					break;
				case 3 :
					// JPA2.g:392:7: functions_returning_datetime
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_datetime_in_datetime_expression3684);
					functions_returning_datetime419=functions_returning_datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime419.getTree());

					}
					break;
				case 4 :
					// JPA2.g:393:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_datetime_expression3692);
					aggregate_expression420=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression420.getTree());

					}
					break;
				case 5 :
					// JPA2.g:394:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_datetime_expression3700);
					case_expression421=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression421.getTree());

					}
					break;
				case 6 :
					// JPA2.g:395:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_datetime_expression3708);
					function_invocation422=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation422.getTree());

					}
					break;
				case 7 :
					// JPA2.g:396:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_datetime_expression3716);
					extension_functions423=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions423.getTree());

					}
					break;
				case 8 :
					// JPA2.g:397:7: date_time_timestamp_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_time_timestamp_literal_in_datetime_expression3724);
					date_time_timestamp_literal424=date_time_timestamp_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_time_timestamp_literal424.getTree());

					}
					break;
				case 9 :
					// JPA2.g:398:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_datetime_expression3732);
					subquery425=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery425.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope path_expression426 =null;
		ParserRuleReturnScope boolean_literal427 =null;
		ParserRuleReturnScope input_parameter428 =null;
		ParserRuleReturnScope case_expression429 =null;
		ParserRuleReturnScope function_invocation430 =null;
		ParserRuleReturnScope extension_functions431 =null;
		ParserRuleReturnScope subquery432 =null;


		try {
			// JPA2.g:400:5: ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | extension_functions | subquery )
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
			case 63:
			case 77:
				{
				alt114=3;
				}
				break;
			case CASE:
			case 90:
			case 120:
				{
				alt114=4;
				}
				break;
			case 104:
				{
				alt114=5;
				}
				break;
			case 82:
			case 89:
			case 102:
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
					// JPA2.g:400:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_boolean_expression3743);
					path_expression426=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression426.getTree());

					}
					break;
				case 2 :
					// JPA2.g:401:7: boolean_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_literal_in_boolean_expression3751);
					boolean_literal427=boolean_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal427.getTree());

					}
					break;
				case 3 :
					// JPA2.g:402:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_boolean_expression3759);
					input_parameter428=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter428.getTree());

					}
					break;
				case 4 :
					// JPA2.g:403:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_boolean_expression3767);
					case_expression429=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression429.getTree());

					}
					break;
				case 5 :
					// JPA2.g:404:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_boolean_expression3775);
					function_invocation430=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation430.getTree());

					}
					break;
				case 6 :
					// JPA2.g:405:7: extension_functions
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extension_functions_in_boolean_expression3783);
					extension_functions431=extension_functions();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extension_functions431.getTree());

					}
					break;
				case 7 :
					// JPA2.g:406:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_boolean_expression3791);
					subquery432=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery432.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope path_expression433 =null;
		ParserRuleReturnScope enum_literal434 =null;
		ParserRuleReturnScope input_parameter435 =null;
		ParserRuleReturnScope case_expression436 =null;
		ParserRuleReturnScope subquery437 =null;


		try {
			// JPA2.g:408:5: ( path_expression | enum_literal | input_parameter | case_expression | subquery )
			int alt115=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA115_1 = input.LA(2);
				if ( (LA115_1==68) ) {
					alt115=1;
				}
				else if ( (LA115_1==EOF||(LA115_1 >= AND && LA115_1 <= ASC)||LA115_1==DESC||(LA115_1 >= ELSE && LA115_1 <= END)||(LA115_1 >= GROUP && LA115_1 <= HAVING)||LA115_1==INNER||(LA115_1 >= JOIN && LA115_1 <= LEFT)||(LA115_1 >= OR && LA115_1 <= ORDER)||LA115_1==RPAREN||LA115_1==SET||LA115_1==THEN||(LA115_1 >= WHEN && LA115_1 <= WORD)||LA115_1==66||(LA115_1 >= 73 && LA115_1 <= 74)||LA115_1==103||(LA115_1 >= 121 && LA115_1 <= 122)||LA115_1==143) ) {
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
			case 63:
			case 77:
				{
				alt115=3;
				}
				break;
			case CASE:
			case 90:
			case 120:
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
					// JPA2.g:408:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_enum_expression3802);
					path_expression433=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression433.getTree());

					}
					break;
				case 2 :
					// JPA2.g:409:7: enum_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_literal_in_enum_expression3810);
					enum_literal434=enum_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal434.getTree());

					}
					break;
				case 3 :
					// JPA2.g:410:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_enum_expression3818);
					input_parameter435=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter435.getTree());

					}
					break;
				case 4 :
					// JPA2.g:411:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_enum_expression3826);
					case_expression436=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression436.getTree());

					}
					break;
				case 5 :
					// JPA2.g:412:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_enum_expression3834);
					subquery437=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery437.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope path_expression438 =null;
		ParserRuleReturnScope simple_entity_expression439 =null;


		try {
			// JPA2.g:414:5: ( path_expression | simple_entity_expression )
			int alt116=2;
			int LA116_0 = input.LA(1);
			if ( (LA116_0==GROUP||LA116_0==WORD) ) {
				int LA116_1 = input.LA(2);
				if ( (LA116_1==68) ) {
					alt116=1;
				}
				else if ( (LA116_1==EOF||LA116_1==AND||(LA116_1 >= GROUP && LA116_1 <= HAVING)||LA116_1==INNER||(LA116_1 >= JOIN && LA116_1 <= LEFT)||(LA116_1 >= OR && LA116_1 <= ORDER)||LA116_1==RPAREN||LA116_1==SET||LA116_1==THEN||LA116_1==66||(LA116_1 >= 73 && LA116_1 <= 74)||LA116_1==143) ) {
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
			else if ( (LA116_0==NAMED_PARAMETER||LA116_0==63||LA116_0==77) ) {
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
					// JPA2.g:414:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_expression3845);
					path_expression438=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression438.getTree());

					}
					break;
				case 2 :
					// JPA2.g:415:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3853);
					simple_entity_expression439=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression439.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope identification_variable440 =null;
		ParserRuleReturnScope input_parameter441 =null;


		try {
			// JPA2.g:417:5: ( identification_variable | input_parameter )
			int alt117=2;
			int LA117_0 = input.LA(1);
			if ( (LA117_0==GROUP||LA117_0==WORD) ) {
				alt117=1;
			}
			else if ( (LA117_0==NAMED_PARAMETER||LA117_0==63||LA117_0==77) ) {
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
					// JPA2.g:417:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3864);
					identification_variable440=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable440.getTree());

					}
					break;
				case 2 :
					// JPA2.g:418:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3872);
					input_parameter441=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter441.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope type_discriminator442 =null;
		ParserRuleReturnScope entity_type_literal443 =null;
		ParserRuleReturnScope input_parameter444 =null;


		try {
			// JPA2.g:420:5: ( type_discriminator | entity_type_literal | input_parameter )
			int alt118=3;
			switch ( input.LA(1) ) {
			case 137:
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
			case 63:
			case 77:
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
					// JPA2.g:420:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_entity_type_expression3883);
					type_discriminator442=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator442.getTree());

					}
					break;
				case 2 :
					// JPA2.g:421:7: entity_type_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_literal_in_entity_type_expression3891);
					entity_type_literal443=entity_type_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_literal443.getTree());

					}
					break;
				case 3 :
					// JPA2.g:422:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_entity_type_expression3899);
					input_parameter444=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter444.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal445=null;
		Token char_literal449=null;
		ParserRuleReturnScope general_identification_variable446 =null;
		ParserRuleReturnScope path_expression447 =null;
		ParserRuleReturnScope input_parameter448 =null;

		Object string_literal445_tree=null;
		Object char_literal449_tree=null;

		try {
			// JPA2.g:424:5: ( 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')' )
			// JPA2.g:424:7: 'TYPE(' ( general_identification_variable | path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal445=(Token)match(input,137,FOLLOW_137_in_type_discriminator3910); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal445_tree = (Object)adaptor.create(string_literal445);
			adaptor.addChild(root_0, string_literal445_tree);
			}

			// JPA2.g:424:15: ( general_identification_variable | path_expression | input_parameter )
			int alt119=3;
			switch ( input.LA(1) ) {
			case GROUP:
			case WORD:
				{
				int LA119_1 = input.LA(2);
				if ( (LA119_1==RPAREN) ) {
					alt119=1;
				}
				else if ( (LA119_1==68) ) {
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
			case 108:
			case 141:
				{
				alt119=1;
				}
				break;
			case NAMED_PARAMETER:
			case 63:
			case 77:
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
					// JPA2.g:424:16: general_identification_variable
					{
					pushFollow(FOLLOW_general_identification_variable_in_type_discriminator3913);
					general_identification_variable446=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable446.getTree());

					}
					break;
				case 2 :
					// JPA2.g:424:50: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_type_discriminator3917);
					path_expression447=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression447.getTree());

					}
					break;
				case 3 :
					// JPA2.g:424:68: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_type_discriminator3921);
					input_parameter448=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter448.getTree());

					}
					break;

			}

			char_literal449=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_type_discriminator3924); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal449_tree = (Object)adaptor.create(char_literal449);
			adaptor.addChild(root_0, char_literal449_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal450=null;
		Token char_literal452=null;
		Token string_literal453=null;
		Token char_literal455=null;
		Token char_literal457=null;
		Token char_literal459=null;
		Token string_literal460=null;
		Token char_literal462=null;
		Token string_literal463=null;
		Token char_literal465=null;
		Token string_literal466=null;
		Token char_literal468=null;
		Token char_literal470=null;
		Token string_literal471=null;
		Token char_literal473=null;
		Token string_literal474=null;
		Token char_literal476=null;
		ParserRuleReturnScope string_expression451 =null;
		ParserRuleReturnScope string_expression454 =null;
		ParserRuleReturnScope string_expression456 =null;
		ParserRuleReturnScope arithmetic_expression458 =null;
		ParserRuleReturnScope arithmetic_expression461 =null;
		ParserRuleReturnScope arithmetic_expression464 =null;
		ParserRuleReturnScope arithmetic_expression467 =null;
		ParserRuleReturnScope arithmetic_expression469 =null;
		ParserRuleReturnScope path_expression472 =null;
		ParserRuleReturnScope identification_variable475 =null;

		Object string_literal450_tree=null;
		Object char_literal452_tree=null;
		Object string_literal453_tree=null;
		Object char_literal455_tree=null;
		Object char_literal457_tree=null;
		Object char_literal459_tree=null;
		Object string_literal460_tree=null;
		Object char_literal462_tree=null;
		Object string_literal463_tree=null;
		Object char_literal465_tree=null;
		Object string_literal466_tree=null;
		Object char_literal468_tree=null;
		Object char_literal470_tree=null;
		Object string_literal471_tree=null;
		Object char_literal473_tree=null;
		Object string_literal474_tree=null;
		Object char_literal476_tree=null;

		try {
			// JPA2.g:426:5: ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' )
			int alt121=7;
			switch ( input.LA(1) ) {
			case 110:
				{
				alt121=1;
				}
				break;
			case 112:
				{
				alt121=2;
				}
				break;
			case 84:
				{
				alt121=3;
				}
				break;
			case 132:
				{
				alt121=4;
				}
				break;
			case 115:
				{
				alt121=5;
				}
				break;
			case 130:
				{
				alt121=6;
				}
				break;
			case 106:
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
					// JPA2.g:426:7: 'LENGTH(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal450=(Token)match(input,110,FOLLOW_110_in_functions_returning_numerics3935); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal450_tree = (Object)adaptor.create(string_literal450);
					adaptor.addChild(root_0, string_literal450_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3936);
					string_expression451=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression451.getTree());

					char_literal452=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3937); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal452_tree = (Object)adaptor.create(char_literal452);
					adaptor.addChild(root_0, char_literal452_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:427:7: 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal453=(Token)match(input,112,FOLLOW_112_in_functions_returning_numerics3945); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal453_tree = (Object)adaptor.create(string_literal453);
					adaptor.addChild(root_0, string_literal453_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3947);
					string_expression454=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression454.getTree());

					char_literal455=(Token)match(input,66,FOLLOW_66_in_functions_returning_numerics3948); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal455_tree = (Object)adaptor.create(char_literal455);
					adaptor.addChild(root_0, char_literal455_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3950);
					string_expression456=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression456.getTree());

					// JPA2.g:427:55: ( ',' arithmetic_expression )?
					int alt120=2;
					int LA120_0 = input.LA(1);
					if ( (LA120_0==66) ) {
						alt120=1;
					}
					switch (alt120) {
						case 1 :
							// JPA2.g:427:56: ',' arithmetic_expression
							{
							char_literal457=(Token)match(input,66,FOLLOW_66_in_functions_returning_numerics3952); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal457_tree = (Object)adaptor.create(char_literal457);
							adaptor.addChild(root_0, char_literal457_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3953);
							arithmetic_expression458=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression458.getTree());

							}
							break;

					}

					char_literal459=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3956); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal459_tree = (Object)adaptor.create(char_literal459);
					adaptor.addChild(root_0, char_literal459_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:428:7: 'ABS(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal460=(Token)match(input,84,FOLLOW_84_in_functions_returning_numerics3964); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal460_tree = (Object)adaptor.create(string_literal460);
					adaptor.addChild(root_0, string_literal460_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3965);
					arithmetic_expression461=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression461.getTree());

					char_literal462=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3966); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal462_tree = (Object)adaptor.create(char_literal462);
					adaptor.addChild(root_0, char_literal462_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:429:7: 'SQRT(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal463=(Token)match(input,132,FOLLOW_132_in_functions_returning_numerics3974); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal463_tree = (Object)adaptor.create(string_literal463);
					adaptor.addChild(root_0, string_literal463_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3975);
					arithmetic_expression464=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression464.getTree());

					char_literal465=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3976); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal465_tree = (Object)adaptor.create(char_literal465);
					adaptor.addChild(root_0, char_literal465_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:430:7: 'MOD(' arithmetic_expression ',' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal466=(Token)match(input,115,FOLLOW_115_in_functions_returning_numerics3984); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal466_tree = (Object)adaptor.create(string_literal466);
					adaptor.addChild(root_0, string_literal466_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3985);
					arithmetic_expression467=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression467.getTree());

					char_literal468=(Token)match(input,66,FOLLOW_66_in_functions_returning_numerics3986); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal468_tree = (Object)adaptor.create(char_literal468);
					adaptor.addChild(root_0, char_literal468_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3988);
					arithmetic_expression469=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression469.getTree());

					char_literal470=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3989); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal470_tree = (Object)adaptor.create(char_literal470);
					adaptor.addChild(root_0, char_literal470_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:431:7: 'SIZE(' path_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal471=(Token)match(input,130,FOLLOW_130_in_functions_returning_numerics3997); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal471_tree = (Object)adaptor.create(string_literal471);
					adaptor.addChild(root_0, string_literal471_tree);
					}

					pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3998);
					path_expression472=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression472.getTree());

					char_literal473=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3999); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal473_tree = (Object)adaptor.create(char_literal473);
					adaptor.addChild(root_0, char_literal473_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:432:7: 'INDEX(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal474=(Token)match(input,106,FOLLOW_106_in_functions_returning_numerics4007); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal474_tree = (Object)adaptor.create(string_literal474);
					adaptor.addChild(root_0, string_literal474_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_functions_returning_numerics4008);
					identification_variable475=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable475.getTree());

					char_literal476=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics4009); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal476_tree = (Object)adaptor.create(char_literal476);
					adaptor.addChild(root_0, char_literal476_tree);
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

		Token set477=null;

		Object set477_tree=null;

		try {
			// JPA2.g:434:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set477=input.LT(1);
			if ( (input.LA(1) >= 92 && input.LA(1) <= 94) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set477));
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

		Token string_literal478=null;
		Token char_literal480=null;
		Token char_literal482=null;
		Token char_literal484=null;
		Token string_literal485=null;
		Token char_literal487=null;
		Token char_literal489=null;
		Token char_literal491=null;
		Token string_literal492=null;
		Token string_literal495=null;
		Token char_literal497=null;
		Token string_literal498=null;
		Token char_literal499=null;
		Token char_literal501=null;
		Token string_literal502=null;
		Token char_literal504=null;
		ParserRuleReturnScope string_expression479 =null;
		ParserRuleReturnScope string_expression481 =null;
		ParserRuleReturnScope string_expression483 =null;
		ParserRuleReturnScope string_expression486 =null;
		ParserRuleReturnScope arithmetic_expression488 =null;
		ParserRuleReturnScope arithmetic_expression490 =null;
		ParserRuleReturnScope trim_specification493 =null;
		ParserRuleReturnScope trim_character494 =null;
		ParserRuleReturnScope string_expression496 =null;
		ParserRuleReturnScope string_expression500 =null;
		ParserRuleReturnScope string_expression503 =null;

		Object string_literal478_tree=null;
		Object char_literal480_tree=null;
		Object char_literal482_tree=null;
		Object char_literal484_tree=null;
		Object string_literal485_tree=null;
		Object char_literal487_tree=null;
		Object char_literal489_tree=null;
		Object char_literal491_tree=null;
		Object string_literal492_tree=null;
		Object string_literal495_tree=null;
		Object char_literal497_tree=null;
		Object string_literal498_tree=null;
		Object char_literal499_tree=null;
		Object char_literal501_tree=null;
		Object string_literal502_tree=null;
		Object char_literal504_tree=null;

		try {
			// JPA2.g:438:5: ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' )
			int alt127=5;
			switch ( input.LA(1) ) {
			case 91:
				{
				alt127=1;
				}
				break;
			case 133:
				{
				alt127=2;
				}
				break;
			case 136:
				{
				alt127=3;
				}
				break;
			case LOWER:
				{
				alt127=4;
				}
				break;
			case 139:
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
					// JPA2.g:438:7: 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal478=(Token)match(input,91,FOLLOW_91_in_functions_returning_strings4047); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal478_tree = (Object)adaptor.create(string_literal478);
					adaptor.addChild(root_0, string_literal478_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4048);
					string_expression479=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression479.getTree());

					char_literal480=(Token)match(input,66,FOLLOW_66_in_functions_returning_strings4049); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal480_tree = (Object)adaptor.create(char_literal480);
					adaptor.addChild(root_0, char_literal480_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4051);
					string_expression481=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression481.getTree());

					// JPA2.g:438:55: ( ',' string_expression )*
					loop122:
					while (true) {
						int alt122=2;
						int LA122_0 = input.LA(1);
						if ( (LA122_0==66) ) {
							alt122=1;
						}

						switch (alt122) {
						case 1 :
							// JPA2.g:438:56: ',' string_expression
							{
							char_literal482=(Token)match(input,66,FOLLOW_66_in_functions_returning_strings4054); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal482_tree = (Object)adaptor.create(char_literal482);
							adaptor.addChild(root_0, char_literal482_tree);
							}

							pushFollow(FOLLOW_string_expression_in_functions_returning_strings4056);
							string_expression483=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression483.getTree());

							}
							break;

						default :
							break loop122;
						}
					}

					char_literal484=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4059); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal484_tree = (Object)adaptor.create(char_literal484);
					adaptor.addChild(root_0, char_literal484_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:439:7: 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal485=(Token)match(input,133,FOLLOW_133_in_functions_returning_strings4067); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal485_tree = (Object)adaptor.create(string_literal485);
					adaptor.addChild(root_0, string_literal485_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4069);
					string_expression486=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression486.getTree());

					char_literal487=(Token)match(input,66,FOLLOW_66_in_functions_returning_strings4070); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal487_tree = (Object)adaptor.create(char_literal487);
					adaptor.addChild(root_0, char_literal487_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings4072);
					arithmetic_expression488=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression488.getTree());

					// JPA2.g:439:63: ( ',' arithmetic_expression )?
					int alt123=2;
					int LA123_0 = input.LA(1);
					if ( (LA123_0==66) ) {
						alt123=1;
					}
					switch (alt123) {
						case 1 :
							// JPA2.g:439:64: ',' arithmetic_expression
							{
							char_literal489=(Token)match(input,66,FOLLOW_66_in_functions_returning_strings4075); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal489_tree = (Object)adaptor.create(char_literal489);
							adaptor.addChild(root_0, char_literal489_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings4077);
							arithmetic_expression490=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression490.getTree());

							}
							break;

					}

					char_literal491=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4080); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal491_tree = (Object)adaptor.create(char_literal491);
					adaptor.addChild(root_0, char_literal491_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:440:7: 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal492=(Token)match(input,136,FOLLOW_136_in_functions_returning_strings4088); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal492_tree = (Object)adaptor.create(string_literal492);
					adaptor.addChild(root_0, string_literal492_tree);
					}

					// JPA2.g:440:14: ( ( trim_specification )? ( trim_character )? 'FROM' )?
					int alt126=2;
					int LA126_0 = input.LA(1);
					if ( (LA126_0==TRIM_CHARACTER||LA126_0==88||LA126_0==103||LA126_0==109||LA126_0==134) ) {
						alt126=1;
					}
					switch (alt126) {
						case 1 :
							// JPA2.g:440:15: ( trim_specification )? ( trim_character )? 'FROM'
							{
							// JPA2.g:440:15: ( trim_specification )?
							int alt124=2;
							int LA124_0 = input.LA(1);
							if ( (LA124_0==88||LA124_0==109||LA124_0==134) ) {
								alt124=1;
							}
							switch (alt124) {
								case 1 :
									// JPA2.g:440:16: trim_specification
									{
									pushFollow(FOLLOW_trim_specification_in_functions_returning_strings4091);
									trim_specification493=trim_specification();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification493.getTree());

									}
									break;

							}

							// JPA2.g:440:37: ( trim_character )?
							int alt125=2;
							int LA125_0 = input.LA(1);
							if ( (LA125_0==TRIM_CHARACTER) ) {
								alt125=1;
							}
							switch (alt125) {
								case 1 :
									// JPA2.g:440:38: trim_character
									{
									pushFollow(FOLLOW_trim_character_in_functions_returning_strings4096);
									trim_character494=trim_character();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_character494.getTree());

									}
									break;

							}

							string_literal495=(Token)match(input,103,FOLLOW_103_in_functions_returning_strings4100); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal495_tree = (Object)adaptor.create(string_literal495);
							adaptor.addChild(root_0, string_literal495_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4104);
					string_expression496=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression496.getTree());

					char_literal497=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4106); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal497_tree = (Object)adaptor.create(char_literal497);
					adaptor.addChild(root_0, char_literal497_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:441:7: 'LOWER' '(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal498=(Token)match(input,LOWER,FOLLOW_LOWER_in_functions_returning_strings4114); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal498_tree = (Object)adaptor.create(string_literal498);
					adaptor.addChild(root_0, string_literal498_tree);
					}

					char_literal499=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings4116); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal499_tree = (Object)adaptor.create(char_literal499);
					adaptor.addChild(root_0, char_literal499_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4117);
					string_expression500=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression500.getTree());

					char_literal501=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4118); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal501_tree = (Object)adaptor.create(char_literal501);
					adaptor.addChild(root_0, char_literal501_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:442:7: 'UPPER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal502=(Token)match(input,139,FOLLOW_139_in_functions_returning_strings4126); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal502_tree = (Object)adaptor.create(string_literal502);
					adaptor.addChild(root_0, string_literal502_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings4127);
					string_expression503=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression503.getTree());

					char_literal504=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings4128); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal504_tree = (Object)adaptor.create(char_literal504);
					adaptor.addChild(root_0, char_literal504_tree);
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

		Token set505=null;

		Object set505_tree=null;

		try {
			// JPA2.g:444:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set505=input.LT(1);
			if ( input.LA(1)==88||input.LA(1)==109||input.LA(1)==134 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set505));
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

		Token string_literal506=null;
		Token char_literal508=null;
		Token char_literal510=null;
		ParserRuleReturnScope function_name507 =null;
		ParserRuleReturnScope function_arg509 =null;

		Object string_literal506_tree=null;
		Object char_literal508_tree=null;
		Object char_literal510_tree=null;

		try {
			// JPA2.g:446:5: ( 'FUNCTION(' function_name ( ',' function_arg )* ')' )
			// JPA2.g:446:7: 'FUNCTION(' function_name ( ',' function_arg )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal506=(Token)match(input,104,FOLLOW_104_in_function_invocation4158); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal506_tree = (Object)adaptor.create(string_literal506);
			adaptor.addChild(root_0, string_literal506_tree);
			}

			pushFollow(FOLLOW_function_name_in_function_invocation4159);
			function_name507=function_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_name507.getTree());

			// JPA2.g:446:32: ( ',' function_arg )*
			loop128:
			while (true) {
				int alt128=2;
				int LA128_0 = input.LA(1);
				if ( (LA128_0==66) ) {
					alt128=1;
				}

				switch (alt128) {
				case 1 :
					// JPA2.g:446:33: ',' function_arg
					{
					char_literal508=(Token)match(input,66,FOLLOW_66_in_function_invocation4162); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal508_tree = (Object)adaptor.create(char_literal508);
					adaptor.addChild(root_0, char_literal508_tree);
					}

					pushFollow(FOLLOW_function_arg_in_function_invocation4164);
					function_arg509=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg509.getTree());

					}
					break;

				default :
					break loop128;
				}
			}

			char_literal510=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function_invocation4168); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal510_tree = (Object)adaptor.create(char_literal510);
			adaptor.addChild(root_0, char_literal510_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope literal511 =null;
		ParserRuleReturnScope path_expression512 =null;
		ParserRuleReturnScope input_parameter513 =null;
		ParserRuleReturnScope scalar_expression514 =null;


		try {
			// JPA2.g:448:5: ( literal | path_expression | input_parameter | scalar_expression )
			int alt129=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA129_1 = input.LA(2);
				if ( (LA129_1==68) ) {
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
				if ( (LA129_2==68) ) {
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
			case 77:
				{
				int LA129_3 = input.LA(2);
				if ( (LA129_3==70) ) {
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
			case 63:
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
					// JPA2.g:448:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_function_arg4179);
					literal511=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal511.getTree());

					}
					break;
				case 2 :
					// JPA2.g:449:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_function_arg4187);
					path_expression512=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression512.getTree());

					}
					break;
				case 3 :
					// JPA2.g:450:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_function_arg4195);
					input_parameter513=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter513.getTree());

					}
					break;
				case 4 :
					// JPA2.g:451:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_function_arg4203);
					scalar_expression514=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression514.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope general_case_expression515 =null;
		ParserRuleReturnScope simple_case_expression516 =null;
		ParserRuleReturnScope coalesce_expression517 =null;
		ParserRuleReturnScope nullif_expression518 =null;


		try {
			// JPA2.g:453:5: ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression )
			int alt130=4;
			switch ( input.LA(1) ) {
			case CASE:
				{
				int LA130_1 = input.LA(2);
				if ( (LA130_1==WHEN) ) {
					alt130=1;
				}
				else if ( (LA130_1==GROUP||LA130_1==WORD||LA130_1==137) ) {
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
			case 90:
				{
				alt130=3;
				}
				break;
			case 120:
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
					// JPA2.g:453:7: general_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_case_expression_in_case_expression4214);
					general_case_expression515=general_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_case_expression515.getTree());

					}
					break;
				case 2 :
					// JPA2.g:454:7: simple_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_case_expression_in_case_expression4222);
					simple_case_expression516=simple_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_case_expression516.getTree());

					}
					break;
				case 3 :
					// JPA2.g:455:7: coalesce_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_coalesce_expression_in_case_expression4230);
					coalesce_expression517=coalesce_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, coalesce_expression517.getTree());

					}
					break;
				case 4 :
					// JPA2.g:456:7: nullif_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_nullif_expression_in_case_expression4238);
					nullif_expression518=nullif_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, nullif_expression518.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token CASE519=null;
		Token ELSE522=null;
		Token END524=null;
		ParserRuleReturnScope when_clause520 =null;
		ParserRuleReturnScope when_clause521 =null;
		ParserRuleReturnScope scalar_expression523 =null;

		Object CASE519_tree=null;
		Object ELSE522_tree=null;
		Object END524_tree=null;

		try {
			// JPA2.g:458:5: ( CASE when_clause ( when_clause )* ELSE scalar_expression END )
			// JPA2.g:458:7: CASE when_clause ( when_clause )* ELSE scalar_expression END
			{
			root_0 = (Object)adaptor.nil();


			CASE519=(Token)match(input,CASE,FOLLOW_CASE_in_general_case_expression4249); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			CASE519_tree = (Object)adaptor.create(CASE519);
			adaptor.addChild(root_0, CASE519_tree);
			}

			pushFollow(FOLLOW_when_clause_in_general_case_expression4251);
			when_clause520=when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause520.getTree());

			// JPA2.g:458:24: ( when_clause )*
			loop131:
			while (true) {
				int alt131=2;
				int LA131_0 = input.LA(1);
				if ( (LA131_0==WHEN) ) {
					alt131=1;
				}

				switch (alt131) {
				case 1 :
					// JPA2.g:458:25: when_clause
					{
					pushFollow(FOLLOW_when_clause_in_general_case_expression4254);
					when_clause521=when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause521.getTree());

					}
					break;

				default :
					break loop131;
				}
			}

			ELSE522=(Token)match(input,ELSE,FOLLOW_ELSE_in_general_case_expression4258); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ELSE522_tree = (Object)adaptor.create(ELSE522);
			adaptor.addChild(root_0, ELSE522_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_general_case_expression4260);
			scalar_expression523=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression523.getTree());

			END524=(Token)match(input,END,FOLLOW_END_in_general_case_expression4262); if (state.failed) return retval;
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

		Token WHEN525=null;
		Token THEN527=null;
		ParserRuleReturnScope conditional_expression526 =null;
		ParserRuleReturnScope scalar_expression528 =null;

		Object WHEN525_tree=null;
		Object THEN527_tree=null;

		try {
			// JPA2.g:460:5: ( WHEN conditional_expression THEN scalar_expression )
			// JPA2.g:460:7: WHEN conditional_expression THEN scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			WHEN525=(Token)match(input,WHEN,FOLLOW_WHEN_in_when_clause4273); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WHEN525_tree = (Object)adaptor.create(WHEN525);
			adaptor.addChild(root_0, WHEN525_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_when_clause4275);
			conditional_expression526=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression526.getTree());

			THEN527=(Token)match(input,THEN,FOLLOW_THEN_in_when_clause4277); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			THEN527_tree = (Object)adaptor.create(THEN527);
			adaptor.addChild(root_0, THEN527_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_when_clause4279);
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

		Token CASE529=null;
		Token ELSE533=null;
		Token END535=null;
		ParserRuleReturnScope case_operand530 =null;
		ParserRuleReturnScope simple_when_clause531 =null;
		ParserRuleReturnScope simple_when_clause532 =null;
		ParserRuleReturnScope scalar_expression534 =null;

		Object CASE529_tree=null;
		Object ELSE533_tree=null;
		Object END535_tree=null;

		try {
			// JPA2.g:462:5: ( CASE case_operand simple_when_clause ( simple_when_clause )* ELSE scalar_expression END )
			// JPA2.g:462:7: CASE case_operand simple_when_clause ( simple_when_clause )* ELSE scalar_expression END
			{
			root_0 = (Object)adaptor.nil();


			CASE529=(Token)match(input,CASE,FOLLOW_CASE_in_simple_case_expression4290); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			CASE529_tree = (Object)adaptor.create(CASE529);
			adaptor.addChild(root_0, CASE529_tree);
			}

			pushFollow(FOLLOW_case_operand_in_simple_case_expression4292);
			case_operand530=case_operand();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, case_operand530.getTree());

			pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4294);
			simple_when_clause531=simple_when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause531.getTree());

			// JPA2.g:462:44: ( simple_when_clause )*
			loop132:
			while (true) {
				int alt132=2;
				int LA132_0 = input.LA(1);
				if ( (LA132_0==WHEN) ) {
					alt132=1;
				}

				switch (alt132) {
				case 1 :
					// JPA2.g:462:45: simple_when_clause
					{
					pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression4297);
					simple_when_clause532=simple_when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause532.getTree());

					}
					break;

				default :
					break loop132;
				}
			}

			ELSE533=(Token)match(input,ELSE,FOLLOW_ELSE_in_simple_case_expression4301); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ELSE533_tree = (Object)adaptor.create(ELSE533);
			adaptor.addChild(root_0, ELSE533_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_case_expression4303);
			scalar_expression534=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression534.getTree());

			END535=(Token)match(input,END,FOLLOW_END_in_simple_case_expression4305); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			END535_tree = (Object)adaptor.create(END535);
			adaptor.addChild(root_0, END535_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		ParserRuleReturnScope path_expression536 =null;
		ParserRuleReturnScope type_discriminator537 =null;


		try {
			// JPA2.g:464:5: ( path_expression | type_discriminator )
			int alt133=2;
			int LA133_0 = input.LA(1);
			if ( (LA133_0==GROUP||LA133_0==WORD) ) {
				alt133=1;
			}
			else if ( (LA133_0==137) ) {
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
					// JPA2.g:464:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_case_operand4316);
					path_expression536=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression536.getTree());

					}
					break;
				case 2 :
					// JPA2.g:465:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_case_operand4324);
					type_discriminator537=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator537.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token WHEN538=null;
		Token THEN540=null;
		ParserRuleReturnScope scalar_expression539 =null;
		ParserRuleReturnScope scalar_expression541 =null;

		Object WHEN538_tree=null;
		Object THEN540_tree=null;

		try {
			// JPA2.g:467:5: ( WHEN scalar_expression THEN scalar_expression )
			// JPA2.g:467:7: WHEN scalar_expression THEN scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			WHEN538=(Token)match(input,WHEN,FOLLOW_WHEN_in_simple_when_clause4335); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WHEN538_tree = (Object)adaptor.create(WHEN538);
			adaptor.addChild(root_0, WHEN538_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4337);
			scalar_expression539=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression539.getTree());

			THEN540=(Token)match(input,THEN,FOLLOW_THEN_in_simple_when_clause4339); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			THEN540_tree = (Object)adaptor.create(THEN540);
			adaptor.addChild(root_0, THEN540_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause4341);
			scalar_expression541=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression541.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal542=null;
		Token char_literal544=null;
		Token char_literal546=null;
		ParserRuleReturnScope scalar_expression543 =null;
		ParserRuleReturnScope scalar_expression545 =null;

		Object string_literal542_tree=null;
		Object char_literal544_tree=null;
		Object char_literal546_tree=null;

		try {
			// JPA2.g:469:5: ( 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' )
			// JPA2.g:469:7: 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal542=(Token)match(input,90,FOLLOW_90_in_coalesce_expression4352); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal542_tree = (Object)adaptor.create(string_literal542);
			adaptor.addChild(root_0, string_literal542_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4353);
			scalar_expression543=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression543.getTree());

			// JPA2.g:469:36: ( ',' scalar_expression )+
			int cnt134=0;
			loop134:
			while (true) {
				int alt134=2;
				int LA134_0 = input.LA(1);
				if ( (LA134_0==66) ) {
					alt134=1;
				}

				switch (alt134) {
				case 1 :
					// JPA2.g:469:37: ',' scalar_expression
					{
					char_literal544=(Token)match(input,66,FOLLOW_66_in_coalesce_expression4356); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal544_tree = (Object)adaptor.create(char_literal544);
					adaptor.addChild(root_0, char_literal544_tree);
					}

					pushFollow(FOLLOW_scalar_expression_in_coalesce_expression4358);
					scalar_expression545=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression545.getTree());

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

			char_literal546=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_coalesce_expression4361); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal546_tree = (Object)adaptor.create(char_literal546);
			adaptor.addChild(root_0, char_literal546_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal547=null;
		Token char_literal549=null;
		Token char_literal551=null;
		ParserRuleReturnScope scalar_expression548 =null;
		ParserRuleReturnScope scalar_expression550 =null;

		Object string_literal547_tree=null;
		Object char_literal549_tree=null;
		Object char_literal551_tree=null;

		try {
			// JPA2.g:471:5: ( 'NULLIF(' scalar_expression ',' scalar_expression ')' )
			// JPA2.g:471:7: 'NULLIF(' scalar_expression ',' scalar_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal547=(Token)match(input,120,FOLLOW_120_in_nullif_expression4372); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal547_tree = (Object)adaptor.create(string_literal547);
			adaptor.addChild(root_0, string_literal547_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4373);
			scalar_expression548=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression548.getTree());

			char_literal549=(Token)match(input,66,FOLLOW_66_in_nullif_expression4375); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal549_tree = (Object)adaptor.create(char_literal549);
			adaptor.addChild(root_0, char_literal549_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4377);
			scalar_expression550=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression550.getTree());

			char_literal551=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nullif_expression4378); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal551_tree = (Object)adaptor.create(char_literal551);
			adaptor.addChild(root_0, char_literal551_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal552=null;
		Token WORD554=null;
		Token char_literal555=null;
		Token INT_NUMERAL556=null;
		Token char_literal557=null;
		Token INT_NUMERAL558=null;
		Token char_literal559=null;
		Token char_literal560=null;
		ParserRuleReturnScope function_arg553 =null;
		ParserRuleReturnScope extract_function561 =null;
		ParserRuleReturnScope enum_function562 =null;

		Object string_literal552_tree=null;
		Object WORD554_tree=null;
		Object char_literal555_tree=null;
		Object INT_NUMERAL556_tree=null;
		Object char_literal557_tree=null;
		Object INT_NUMERAL558_tree=null;
		Object char_literal559_tree=null;
		Object char_literal560_tree=null;

		try {
			// JPA2.g:474:5: ( 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')' | extract_function | enum_function )
			int alt137=3;
			switch ( input.LA(1) ) {
			case 89:
				{
				alt137=1;
				}
				break;
			case 102:
				{
				alt137=2;
				}
				break;
			case 82:
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
					// JPA2.g:474:7: 'CAST(' function_arg WORD ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal552=(Token)match(input,89,FOLLOW_89_in_extension_functions4390); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal552_tree = (Object)adaptor.create(string_literal552);
					adaptor.addChild(root_0, string_literal552_tree);
					}

					pushFollow(FOLLOW_function_arg_in_extension_functions4392);
					function_arg553=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg553.getTree());

					WORD554=(Token)match(input,WORD,FOLLOW_WORD_in_extension_functions4394); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD554_tree = (Object)adaptor.create(WORD554);
					adaptor.addChild(root_0, WORD554_tree);
					}

					// JPA2.g:474:33: ( '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')' )*
					loop136:
					while (true) {
						int alt136=2;
						int LA136_0 = input.LA(1);
						if ( (LA136_0==LPAREN) ) {
							alt136=1;
						}

						switch (alt136) {
						case 1 :
							// JPA2.g:474:34: '(' INT_NUMERAL ( ',' INT_NUMERAL )* ')'
							{
							char_literal555=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_extension_functions4397); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal555_tree = (Object)adaptor.create(char_literal555);
							adaptor.addChild(root_0, char_literal555_tree);
							}

							INT_NUMERAL556=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4398); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							INT_NUMERAL556_tree = (Object)adaptor.create(INT_NUMERAL556);
							adaptor.addChild(root_0, INT_NUMERAL556_tree);
							}

							// JPA2.g:474:49: ( ',' INT_NUMERAL )*
							loop135:
							while (true) {
								int alt135=2;
								int LA135_0 = input.LA(1);
								if ( (LA135_0==66) ) {
									alt135=1;
								}

								switch (alt135) {
								case 1 :
									// JPA2.g:474:50: ',' INT_NUMERAL
									{
									char_literal557=(Token)match(input,66,FOLLOW_66_in_extension_functions4401); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									char_literal557_tree = (Object)adaptor.create(char_literal557);
									adaptor.addChild(root_0, char_literal557_tree);
									}

									INT_NUMERAL558=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_extension_functions4403); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									INT_NUMERAL558_tree = (Object)adaptor.create(INT_NUMERAL558);
									adaptor.addChild(root_0, INT_NUMERAL558_tree);
									}

									}
									break;

								default :
									break loop135;
								}
							}

							char_literal559=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4408); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal559_tree = (Object)adaptor.create(char_literal559);
							adaptor.addChild(root_0, char_literal559_tree);
							}

							}
							break;

						default :
							break loop136;
						}
					}

					char_literal560=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extension_functions4412); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal560_tree = (Object)adaptor.create(char_literal560);
					adaptor.addChild(root_0, char_literal560_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:475:7: extract_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_extract_function_in_extension_functions4420);
					extract_function561=extract_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, extract_function561.getTree());

					}
					break;
				case 3 :
					// JPA2.g:476:7: enum_function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_function_in_extension_functions4428);
					enum_function562=enum_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_function562.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal563=null;
		Token string_literal565=null;
		Token char_literal567=null;
		ParserRuleReturnScope date_part564 =null;
		ParserRuleReturnScope function_arg566 =null;

		Object string_literal563_tree=null;
		Object string_literal565_tree=null;
		Object char_literal567_tree=null;

		try {
			// JPA2.g:479:5: ( 'EXTRACT(' date_part 'FROM' function_arg ')' )
			// JPA2.g:479:7: 'EXTRACT(' date_part 'FROM' function_arg ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal563=(Token)match(input,102,FOLLOW_102_in_extract_function4440); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal563_tree = (Object)adaptor.create(string_literal563);
			adaptor.addChild(root_0, string_literal563_tree);
			}

			pushFollow(FOLLOW_date_part_in_extract_function4442);
			date_part564=date_part();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part564.getTree());

			string_literal565=(Token)match(input,103,FOLLOW_103_in_extract_function4444); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal565_tree = (Object)adaptor.create(string_literal565);
			adaptor.addChild(root_0, string_literal565_tree);
			}

			pushFollow(FOLLOW_function_arg_in_extract_function4446);
			function_arg566=function_arg();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg566.getTree());

			char_literal567=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_extract_function4448); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal567_tree = (Object)adaptor.create(char_literal567);
			adaptor.addChild(root_0, char_literal567_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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

		Token string_literal568=null;
		Token char_literal569=null;
		Token char_literal571=null;
		ParserRuleReturnScope enum_value_literal570 =null;

		Object string_literal568_tree=null;
		Object char_literal569_tree=null;
		Object char_literal571_tree=null;
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_82=new RewriteRuleTokenStream(adaptor,"token 82");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_enum_value_literal=new RewriteRuleSubtreeStream(adaptor,"rule enum_value_literal");

		try {
			// JPA2.g:482:5: ( '@ENUM' '(' enum_value_literal ')' -> ^( T_ENUM_MACROS[$enum_value_literal.text] ) )
			// JPA2.g:482:7: '@ENUM' '(' enum_value_literal ')'
			{
			string_literal568=(Token)match(input,82,FOLLOW_82_in_enum_function4460); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_82.add(string_literal568);

			char_literal569=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_enum_function4462); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal569);

			pushFollow(FOLLOW_enum_value_literal_in_enum_function4464);
			enum_value_literal570=enum_value_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_enum_value_literal.add(enum_value_literal570.getTree());
			char_literal571=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_enum_function4466); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal571);

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
				root_1 = (Object)adaptor.becomeRoot(new EnumConditionNode(T_ENUM_MACROS, (enum_value_literal570!=null?input.toString(enum_value_literal570.start,enum_value_literal570.stop):null)), root_1);
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

		Token set572=null;

		Object set572_tree=null;

		try {
			// JPA2.g:485:5: ( 'EPOCH' | 'YEAR' | 'QUARTER' | 'MONTH' | 'WEEK' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set572=input.LT(1);
			if ( input.LA(1)==95||input.LA(1)==99||input.LA(1)==105||input.LA(1)==114||input.LA(1)==116||input.LA(1)==126||input.LA(1)==128||input.LA(1)==142||input.LA(1)==144 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set572));
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

		Token char_literal573=null;
		Token NAMED_PARAMETER575=null;
		Token string_literal576=null;
		Token WORD577=null;
		Token char_literal578=null;
		ParserRuleReturnScope numeric_literal574 =null;

		Object char_literal573_tree=null;
		Object NAMED_PARAMETER575_tree=null;
		Object string_literal576_tree=null;
		Object WORD577_tree=null;
		Object char_literal578_tree=null;
		RewriteRuleTokenStream stream_77=new RewriteRuleTokenStream(adaptor,"token 77");
		RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
		RewriteRuleTokenStream stream_147=new RewriteRuleTokenStream(adaptor,"token 147");
		RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
		RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
		RewriteRuleSubtreeStream stream_numeric_literal=new RewriteRuleSubtreeStream(adaptor,"rule numeric_literal");

		try {
			// JPA2.g:489:5: ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
			int alt138=3;
			switch ( input.LA(1) ) {
			case 77:
				{
				alt138=1;
				}
				break;
			case NAMED_PARAMETER:
				{
				alt138=2;
				}
				break;
			case 63:
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
					// JPA2.g:489:7: '?' numeric_literal
					{
					char_literal573=(Token)match(input,77,FOLLOW_77_in_input_parameter4533); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_77.add(char_literal573);

					pushFollow(FOLLOW_numeric_literal_in_input_parameter4535);
					numeric_literal574=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_literal.add(numeric_literal574.getTree());
					// AST REWRITE
					// elements: 77, numeric_literal
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
					NAMED_PARAMETER575=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter4558); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NAMED_PARAMETER.add(NAMED_PARAMETER575);

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
					string_literal576=(Token)match(input,63,FOLLOW_63_in_input_parameter4579); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_63.add(string_literal576);

					WORD577=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter4581); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WORD.add(WORD577);

					char_literal578=(Token)match(input,147,FOLLOW_147_in_input_parameter4583); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_147.add(char_literal578);

					// AST REWRITE
					// elements: 63, 147, WORD
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
	// JPA2.g:493:1: literal : WORD ;
	public final JPA2Parser.literal_return literal() throws RecognitionException {
		JPA2Parser.literal_return retval = new JPA2Parser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD579=null;

		Object WORD579_tree=null;

		try {
			// JPA2.g:494:5: ( WORD )
			// JPA2.g:494:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD579=(Token)match(input,WORD,FOLLOW_WORD_in_literal4611); if (state.failed) return retval;
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
	// $ANTLR end "literal"


	public static class constructor_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "constructor_name"
	// JPA2.g:496:1: constructor_name : WORD ( '.' WORD )* ;
	public final JPA2Parser.constructor_name_return constructor_name() throws RecognitionException {
		JPA2Parser.constructor_name_return retval = new JPA2Parser.constructor_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD580=null;
		Token char_literal581=null;
		Token WORD582=null;

		Object WORD580_tree=null;
		Object char_literal581_tree=null;
		Object WORD582_tree=null;

		try {
			// JPA2.g:497:5: ( WORD ( '.' WORD )* )
			// JPA2.g:497:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD580=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4623); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD580_tree = (Object)adaptor.create(WORD580);
			adaptor.addChild(root_0, WORD580_tree);
			}

			// JPA2.g:497:12: ( '.' WORD )*
			loop139:
			while (true) {
				int alt139=2;
				int LA139_0 = input.LA(1);
				if ( (LA139_0==68) ) {
					alt139=1;
				}

				switch (alt139) {
				case 1 :
					// JPA2.g:497:13: '.' WORD
					{
					char_literal581=(Token)match(input,68,FOLLOW_68_in_constructor_name4626); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal581_tree = (Object)adaptor.create(char_literal581);
					adaptor.addChild(root_0, char_literal581_tree);
					}

					WORD582=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4629); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD582_tree = (Object)adaptor.create(WORD582);
					adaptor.addChild(root_0, WORD582_tree);
					}

					}
					break;

				default :
					break loop139;
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

		Token WORD583=null;

		Object WORD583_tree=null;

		try {
			// JPA2.g:500:5: ( WORD )
			// JPA2.g:500:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD583=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal4643); if (state.failed) return retval;
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

		Token set584=null;

		Object set584_tree=null;

		try {
			// JPA2.g:503:5: ( 'true' | 'false' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set584=input.LT(1);
			if ( (input.LA(1) >= 145 && input.LA(1) <= 146) ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set584));
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
	// JPA2.g:507:1: field : ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | 'AS' | 'MEMBER' | 'CASE' | 'OBJECT' | date_part );
	public final JPA2Parser.field_return field() throws RecognitionException {
		JPA2Parser.field_return retval = new JPA2Parser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD585=null;
		Token string_literal586=null;
		Token string_literal587=null;
		Token string_literal588=null;
		Token string_literal589=null;
		Token string_literal590=null;
		Token string_literal591=null;
		Token string_literal592=null;
		Token string_literal593=null;
		Token string_literal594=null;
		Token string_literal595=null;
		Token string_literal596=null;
		Token string_literal597=null;
		Token string_literal598=null;
		ParserRuleReturnScope date_part599 =null;

		Object WORD585_tree=null;
		Object string_literal586_tree=null;
		Object string_literal587_tree=null;
		Object string_literal588_tree=null;
		Object string_literal589_tree=null;
		Object string_literal590_tree=null;
		Object string_literal591_tree=null;
		Object string_literal592_tree=null;
		Object string_literal593_tree=null;
		Object string_literal594_tree=null;
		Object string_literal595_tree=null;
		Object string_literal596_tree=null;
		Object string_literal597_tree=null;
		Object string_literal598_tree=null;

		try {
			// JPA2.g:508:5: ( WORD | 'SELECT' | 'FROM' | 'GROUP' | 'ORDER' | 'MAX' | 'MIN' | 'SUM' | 'AVG' | 'COUNT' | 'AS' | 'MEMBER' | 'CASE' | 'OBJECT' | date_part )
			int alt140=15;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt140=1;
				}
				break;
			case 129:
				{
				alt140=2;
				}
				break;
			case 103:
				{
				alt140=3;
				}
				break;
			case GROUP:
				{
				alt140=4;
				}
				break;
			case ORDER:
				{
				alt140=5;
				}
				break;
			case MAX:
				{
				alt140=6;
				}
				break;
			case MIN:
				{
				alt140=7;
				}
				break;
			case SUM:
				{
				alt140=8;
				}
				break;
			case AVG:
				{
				alt140=9;
				}
				break;
			case COUNT:
				{
				alt140=10;
				}
				break;
			case AS:
				{
				alt140=11;
				}
				break;
			case 113:
				{
				alt140=12;
				}
				break;
			case CASE:
				{
				alt140=13;
				}
				break;
			case 123:
				{
				alt140=14;
				}
				break;
			case 95:
			case 99:
			case 105:
			case 114:
			case 116:
			case 126:
			case 128:
			case 142:
			case 144:
				{
				alt140=15;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 140, 0, input);
				throw nvae;
			}
			switch (alt140) {
				case 1 :
					// JPA2.g:508:7: WORD
					{
					root_0 = (Object)adaptor.nil();


					WORD585=(Token)match(input,WORD,FOLLOW_WORD_in_field4676); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD585_tree = (Object)adaptor.create(WORD585);
					adaptor.addChild(root_0, WORD585_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:508:14: 'SELECT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal586=(Token)match(input,129,FOLLOW_129_in_field4680); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal586_tree = (Object)adaptor.create(string_literal586);
					adaptor.addChild(root_0, string_literal586_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:508:25: 'FROM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal587=(Token)match(input,103,FOLLOW_103_in_field4684); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal587_tree = (Object)adaptor.create(string_literal587);
					adaptor.addChild(root_0, string_literal587_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:508:34: 'GROUP'
					{
					root_0 = (Object)adaptor.nil();


					string_literal588=(Token)match(input,GROUP,FOLLOW_GROUP_in_field4688); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal588_tree = (Object)adaptor.create(string_literal588);
					adaptor.addChild(root_0, string_literal588_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:508:44: 'ORDER'
					{
					root_0 = (Object)adaptor.nil();


					string_literal589=(Token)match(input,ORDER,FOLLOW_ORDER_in_field4692); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal589_tree = (Object)adaptor.create(string_literal589);
					adaptor.addChild(root_0, string_literal589_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:508:54: 'MAX'
					{
					root_0 = (Object)adaptor.nil();


					string_literal590=(Token)match(input,MAX,FOLLOW_MAX_in_field4696); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal590_tree = (Object)adaptor.create(string_literal590);
					adaptor.addChild(root_0, string_literal590_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:508:62: 'MIN'
					{
					root_0 = (Object)adaptor.nil();


					string_literal591=(Token)match(input,MIN,FOLLOW_MIN_in_field4700); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal591_tree = (Object)adaptor.create(string_literal591);
					adaptor.addChild(root_0, string_literal591_tree);
					}

					}
					break;
				case 8 :
					// JPA2.g:508:70: 'SUM'
					{
					root_0 = (Object)adaptor.nil();


					string_literal592=(Token)match(input,SUM,FOLLOW_SUM_in_field4704); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal592_tree = (Object)adaptor.create(string_literal592);
					adaptor.addChild(root_0, string_literal592_tree);
					}

					}
					break;
				case 9 :
					// JPA2.g:508:78: 'AVG'
					{
					root_0 = (Object)adaptor.nil();


					string_literal593=(Token)match(input,AVG,FOLLOW_AVG_in_field4708); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal593_tree = (Object)adaptor.create(string_literal593);
					adaptor.addChild(root_0, string_literal593_tree);
					}

					}
					break;
				case 10 :
					// JPA2.g:508:86: 'COUNT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal594=(Token)match(input,COUNT,FOLLOW_COUNT_in_field4712); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal594_tree = (Object)adaptor.create(string_literal594);
					adaptor.addChild(root_0, string_literal594_tree);
					}

					}
					break;
				case 11 :
					// JPA2.g:508:96: 'AS'
					{
					root_0 = (Object)adaptor.nil();


					string_literal595=(Token)match(input,AS,FOLLOW_AS_in_field4716); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal595_tree = (Object)adaptor.create(string_literal595);
					adaptor.addChild(root_0, string_literal595_tree);
					}

					}
					break;
				case 12 :
					// JPA2.g:508:103: 'MEMBER'
					{
					root_0 = (Object)adaptor.nil();


					string_literal596=(Token)match(input,113,FOLLOW_113_in_field4720); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal596_tree = (Object)adaptor.create(string_literal596);
					adaptor.addChild(root_0, string_literal596_tree);
					}

					}
					break;
				case 13 :
					// JPA2.g:508:114: 'CASE'
					{
					root_0 = (Object)adaptor.nil();


					string_literal597=(Token)match(input,CASE,FOLLOW_CASE_in_field4724); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal597_tree = (Object)adaptor.create(string_literal597);
					adaptor.addChild(root_0, string_literal597_tree);
					}

					}
					break;
				case 14 :
					// JPA2.g:509:7: 'OBJECT'
					{
					root_0 = (Object)adaptor.nil();


					string_literal598=(Token)match(input,123,FOLLOW_123_in_field4732); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal598_tree = (Object)adaptor.create(string_literal598);
					adaptor.addChild(root_0, string_literal598_tree);
					}

					}
					break;
				case 15 :
					// JPA2.g:509:18: date_part
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_part_in_field4736);
					date_part599=date_part();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_part599.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:511:1: identification_variable : ( WORD | 'GROUP' );
	public final JPA2Parser.identification_variable_return identification_variable() throws RecognitionException {
		JPA2Parser.identification_variable_return retval = new JPA2Parser.identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set600=null;

		Object set600_tree=null;

		try {
			// JPA2.g:512:5: ( WORD | 'GROUP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set600=input.LT(1);
			if ( input.LA(1)==GROUP||input.LA(1)==WORD ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set600));
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
	// JPA2.g:514:1: parameter_name : WORD ( '.' WORD )* ;
	public final JPA2Parser.parameter_name_return parameter_name() throws RecognitionException {
		JPA2Parser.parameter_name_return retval = new JPA2Parser.parameter_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD601=null;
		Token char_literal602=null;
		Token WORD603=null;

		Object WORD601_tree=null;
		Object char_literal602_tree=null;
		Object WORD603_tree=null;

		try {
			// JPA2.g:515:5: ( WORD ( '.' WORD )* )
			// JPA2.g:515:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD601=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4764); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD601_tree = (Object)adaptor.create(WORD601);
			adaptor.addChild(root_0, WORD601_tree);
			}

			// JPA2.g:515:12: ( '.' WORD )*
			loop141:
			while (true) {
				int alt141=2;
				int LA141_0 = input.LA(1);
				if ( (LA141_0==68) ) {
					alt141=1;
				}

				switch (alt141) {
				case 1 :
					// JPA2.g:515:13: '.' WORD
					{
					char_literal602=(Token)match(input,68,FOLLOW_68_in_parameter_name4767); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal602_tree = (Object)adaptor.create(char_literal602);
					adaptor.addChild(root_0, char_literal602_tree);
					}

					WORD603=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4770); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD603_tree = (Object)adaptor.create(WORD603);
					adaptor.addChild(root_0, WORD603_tree);
					}

					}
					break;

				default :
					break loop141;
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
	// JPA2.g:518:1: escape_character : ( '\\'.\\'' | STRING_LITERAL );
	public final JPA2Parser.escape_character_return escape_character() throws RecognitionException {
		JPA2Parser.escape_character_return retval = new JPA2Parser.escape_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set604=null;

		Object set604_tree=null;

		try {
			// JPA2.g:519:5: ( '\\'.\\'' | STRING_LITERAL )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set604=input.LT(1);
			if ( input.LA(1)==STRING_LITERAL||input.LA(1)==TRIM_CHARACTER ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set604));
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
	// JPA2.g:520:1: trim_character : TRIM_CHARACTER ;
	public final JPA2Parser.trim_character_return trim_character() throws RecognitionException {
		JPA2Parser.trim_character_return retval = new JPA2Parser.trim_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRIM_CHARACTER605=null;

		Object TRIM_CHARACTER605_tree=null;

		try {
			// JPA2.g:521:5: ( TRIM_CHARACTER )
			// JPA2.g:521:7: TRIM_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			TRIM_CHARACTER605=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_trim_character4800); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TRIM_CHARACTER605_tree = (Object)adaptor.create(TRIM_CHARACTER605);
			adaptor.addChild(root_0, TRIM_CHARACTER605_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:522:1: string_literal : STRING_LITERAL ;
	public final JPA2Parser.string_literal_return string_literal() throws RecognitionException {
		JPA2Parser.string_literal_return retval = new JPA2Parser.string_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL606=null;

		Object STRING_LITERAL606_tree=null;

		try {
			// JPA2.g:523:5: ( STRING_LITERAL )
			// JPA2.g:523:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL606=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_literal4811); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL606_tree = (Object)adaptor.create(STRING_LITERAL606);
			adaptor.addChild(root_0, STRING_LITERAL606_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:524:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
	public final JPA2Parser.numeric_literal_return numeric_literal() throws RecognitionException {
		JPA2Parser.numeric_literal_return retval = new JPA2Parser.numeric_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal607=null;
		Token INT_NUMERAL608=null;

		Object string_literal607_tree=null;
		Object INT_NUMERAL608_tree=null;

		try {
			// JPA2.g:525:5: ( ( '0x' )? INT_NUMERAL )
			// JPA2.g:525:7: ( '0x' )? INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:525:7: ( '0x' )?
			int alt142=2;
			int LA142_0 = input.LA(1);
			if ( (LA142_0==70) ) {
				alt142=1;
			}
			switch (alt142) {
				case 1 :
					// JPA2.g:525:8: '0x'
					{
					string_literal607=(Token)match(input,70,FOLLOW_70_in_numeric_literal4823); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal607_tree = (Object)adaptor.create(string_literal607);
					adaptor.addChild(root_0, string_literal607_tree);
					}

					}
					break;

			}

			INT_NUMERAL608=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal4827); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL608_tree = (Object)adaptor.create(INT_NUMERAL608);
			adaptor.addChild(root_0, INT_NUMERAL608_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:526:1: decimal_literal : INT_NUMERAL '.' INT_NUMERAL ;
	public final JPA2Parser.decimal_literal_return decimal_literal() throws RecognitionException {
		JPA2Parser.decimal_literal_return retval = new JPA2Parser.decimal_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token INT_NUMERAL609=null;
		Token char_literal610=null;
		Token INT_NUMERAL611=null;

		Object INT_NUMERAL609_tree=null;
		Object char_literal610_tree=null;
		Object INT_NUMERAL611_tree=null;

		try {
			// JPA2.g:527:5: ( INT_NUMERAL '.' INT_NUMERAL )
			// JPA2.g:527:7: INT_NUMERAL '.' INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			INT_NUMERAL609=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_decimal_literal4839); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL609_tree = (Object)adaptor.create(INT_NUMERAL609);
			adaptor.addChild(root_0, INT_NUMERAL609_tree);
			}

			char_literal610=(Token)match(input,68,FOLLOW_68_in_decimal_literal4841); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal610_tree = (Object)adaptor.create(char_literal610);
			adaptor.addChild(root_0, char_literal610_tree);
			}

			INT_NUMERAL611=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_decimal_literal4843); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			INT_NUMERAL611_tree = (Object)adaptor.create(INT_NUMERAL611);
			adaptor.addChild(root_0, INT_NUMERAL611_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:528:1: single_valued_object_field : WORD ;
	public final JPA2Parser.single_valued_object_field_return single_valued_object_field() throws RecognitionException {
		JPA2Parser.single_valued_object_field_return retval = new JPA2Parser.single_valued_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD612=null;

		Object WORD612_tree=null;

		try {
			// JPA2.g:529:5: ( WORD )
			// JPA2.g:529:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD612=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_object_field4854); if (state.failed) return retval;
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
	// $ANTLR end "single_valued_object_field"


	public static class single_valued_embeddable_object_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "single_valued_embeddable_object_field"
	// JPA2.g:530:1: single_valued_embeddable_object_field : WORD ;
	public final JPA2Parser.single_valued_embeddable_object_field_return single_valued_embeddable_object_field() throws RecognitionException {
		JPA2Parser.single_valued_embeddable_object_field_return retval = new JPA2Parser.single_valued_embeddable_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD613=null;

		Object WORD613_tree=null;

		try {
			// JPA2.g:531:5: ( WORD )
			// JPA2.g:531:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD613=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_embeddable_object_field4865); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD613_tree = (Object)adaptor.create(WORD613);
			adaptor.addChild(root_0, WORD613_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:532:1: collection_valued_field : WORD ;
	public final JPA2Parser.collection_valued_field_return collection_valued_field() throws RecognitionException {
		JPA2Parser.collection_valued_field_return retval = new JPA2Parser.collection_valued_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD614=null;

		Object WORD614_tree=null;

		try {
			// JPA2.g:533:5: ( WORD )
			// JPA2.g:533:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD614=(Token)match(input,WORD,FOLLOW_WORD_in_collection_valued_field4876); if (state.failed) return retval;
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
	// $ANTLR end "collection_valued_field"


	public static class entity_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_name"
	// JPA2.g:534:1: entity_name : WORD ;
	public final JPA2Parser.entity_name_return entity_name() throws RecognitionException {
		JPA2Parser.entity_name_return retval = new JPA2Parser.entity_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD615=null;

		Object WORD615_tree=null;

		try {
			// JPA2.g:535:5: ( WORD )
			// JPA2.g:535:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD615=(Token)match(input,WORD,FOLLOW_WORD_in_entity_name4887); if (state.failed) return retval;
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
	// $ANTLR end "entity_name"


	public static class subtype_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "subtype"
	// JPA2.g:536:1: subtype : WORD ;
	public final JPA2Parser.subtype_return subtype() throws RecognitionException {
		JPA2Parser.subtype_return retval = new JPA2Parser.subtype_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD616=null;

		Object WORD616_tree=null;

		try {
			// JPA2.g:537:5: ( WORD )
			// JPA2.g:537:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD616=(Token)match(input,WORD,FOLLOW_WORD_in_subtype4898); if (state.failed) return retval;
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
	// $ANTLR end "subtype"


	public static class entity_type_literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_type_literal"
	// JPA2.g:538:1: entity_type_literal : WORD ;
	public final JPA2Parser.entity_type_literal_return entity_type_literal() throws RecognitionException {
		JPA2Parser.entity_type_literal_return retval = new JPA2Parser.entity_type_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD617=null;

		Object WORD617_tree=null;

		try {
			// JPA2.g:539:5: ( WORD )
			// JPA2.g:539:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD617=(Token)match(input,WORD,FOLLOW_WORD_in_entity_type_literal4909); if (state.failed) return retval;
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
	// $ANTLR end "entity_type_literal"


	public static class function_name_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "function_name"
	// JPA2.g:540:1: function_name : STRING_LITERAL ;
	public final JPA2Parser.function_name_return function_name() throws RecognitionException {
		JPA2Parser.function_name_return retval = new JPA2Parser.function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL618=null;

		Object STRING_LITERAL618_tree=null;

		try {
			// JPA2.g:541:5: ( STRING_LITERAL )
			// JPA2.g:541:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL618=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_function_name4920); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STRING_LITERAL618_tree = (Object)adaptor.create(STRING_LITERAL618);
			adaptor.addChild(root_0, STRING_LITERAL618_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:542:1: state_field : WORD ;
	public final JPA2Parser.state_field_return state_field() throws RecognitionException {
		JPA2Parser.state_field_return retval = new JPA2Parser.state_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD619=null;

		Object WORD619_tree=null;

		try {
			// JPA2.g:543:5: ( WORD )
			// JPA2.g:543:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD619=(Token)match(input,WORD,FOLLOW_WORD_in_state_field4931); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD619_tree = (Object)adaptor.create(WORD619);
			adaptor.addChild(root_0, WORD619_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:544:1: result_variable : WORD ;
	public final JPA2Parser.result_variable_return result_variable() throws RecognitionException {
		JPA2Parser.result_variable_return retval = new JPA2Parser.result_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD620=null;

		Object WORD620_tree=null;

		try {
			// JPA2.g:545:5: ( WORD )
			// JPA2.g:545:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD620=(Token)match(input,WORD,FOLLOW_WORD_in_result_variable4942); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD620_tree = (Object)adaptor.create(WORD620);
			adaptor.addChild(root_0, WORD620_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:546:1: superquery_identification_variable : WORD ;
	public final JPA2Parser.superquery_identification_variable_return superquery_identification_variable() throws RecognitionException {
		JPA2Parser.superquery_identification_variable_return retval = new JPA2Parser.superquery_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD621=null;

		Object WORD621_tree=null;

		try {
			// JPA2.g:547:5: ( WORD )
			// JPA2.g:547:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD621=(Token)match(input,WORD,FOLLOW_WORD_in_superquery_identification_variable4953); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD621_tree = (Object)adaptor.create(WORD621);
			adaptor.addChild(root_0, WORD621_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:548:1: date_time_timestamp_literal : WORD ;
	public final JPA2Parser.date_time_timestamp_literal_return date_time_timestamp_literal() throws RecognitionException {
		JPA2Parser.date_time_timestamp_literal_return retval = new JPA2Parser.date_time_timestamp_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD622=null;

		Object WORD622_tree=null;

		try {
			// JPA2.g:549:5: ( WORD )
			// JPA2.g:549:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD622=(Token)match(input,WORD,FOLLOW_WORD_in_date_time_timestamp_literal4964); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD622_tree = (Object)adaptor.create(WORD622);
			adaptor.addChild(root_0, WORD622_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:550:1: pattern_value : string_literal ;
	public final JPA2Parser.pattern_value_return pattern_value() throws RecognitionException {
		JPA2Parser.pattern_value_return retval = new JPA2Parser.pattern_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal623 =null;


		try {
			// JPA2.g:551:5: ( string_literal )
			// JPA2.g:551:7: string_literal
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_literal_in_pattern_value4975);
			string_literal623=string_literal();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal623.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:552:1: collection_valued_input_parameter : input_parameter ;
	public final JPA2Parser.collection_valued_input_parameter_return collection_valued_input_parameter() throws RecognitionException {
		JPA2Parser.collection_valued_input_parameter_return retval = new JPA2Parser.collection_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter624 =null;


		try {
			// JPA2.g:553:5: ( input_parameter )
			// JPA2.g:553:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_collection_valued_input_parameter4986);
			input_parameter624=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter624.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:554:1: single_valued_input_parameter : input_parameter ;
	public final JPA2Parser.single_valued_input_parameter_return single_valued_input_parameter() throws RecognitionException {
		JPA2Parser.single_valued_input_parameter_return retval = new JPA2Parser.single_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter625 =null;


		try {
			// JPA2.g:555:5: ( input_parameter )
			// JPA2.g:555:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_single_valued_input_parameter4997);
			input_parameter625=input_parameter();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter625.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:556:1: enum_value_literal : WORD ( '.' WORD )* ;
	public final JPA2Parser.enum_value_literal_return enum_value_literal() throws RecognitionException {
		JPA2Parser.enum_value_literal_return retval = new JPA2Parser.enum_value_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD626=null;
		Token char_literal627=null;
		Token WORD628=null;

		Object WORD626_tree=null;
		Object char_literal627_tree=null;
		Object WORD628_tree=null;

		try {
			// JPA2.g:557:5: ( WORD ( '.' WORD )* )
			// JPA2.g:557:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD626=(Token)match(input,WORD,FOLLOW_WORD_in_enum_value_literal5008); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD626_tree = (Object)adaptor.create(WORD626);
			adaptor.addChild(root_0, WORD626_tree);
			}

			// JPA2.g:557:12: ( '.' WORD )*
			loop143:
			while (true) {
				int alt143=2;
				int LA143_0 = input.LA(1);
				if ( (LA143_0==68) ) {
					alt143=1;
				}

				switch (alt143) {
				case 1 :
					// JPA2.g:557:13: '.' WORD
					{
					char_literal627=(Token)match(input,68,FOLLOW_68_in_enum_value_literal5011); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal627_tree = (Object)adaptor.create(char_literal627);
					adaptor.addChild(root_0, char_literal627_tree);
					}

					WORD628=(Token)match(input,WORD,FOLLOW_WORD_in_enum_value_literal5014); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD628_tree = (Object)adaptor.create(WORD628);
					adaptor.addChild(root_0, WORD628_tree);
					}

					}
					break;

				default :
					break loop143;
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
		pushFollow(FOLLOW_field_in_synpred21_JPA2972);
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
		pushFollow(FOLLOW_field_in_synpred30_JPA21162);
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
		pushFollow(FOLLOW_scalar_expression_in_synpred33_JPA21288);
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
		pushFollow(FOLLOW_simple_entity_expression_in_synpred34_JPA21296);
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
		pushFollow(FOLLOW_path_expression_in_synpred43_JPA21421);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:175:23: ( ( '+' | '-' | '*' | '/' ) scalar_expression )?
		int alt150=2;
		int LA150_0 = input.LA(1);
		if ( ((LA150_0 >= 64 && LA150_0 <= 65)||LA150_0==67||LA150_0==69) ) {
			alt150=1;
		}
		switch (alt150) {
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
				pushFollow(FOLLOW_scalar_expression_in_synpred43_JPA21440);
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
		pushFollow(FOLLOW_identification_variable_in_synpred44_JPA21450);
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
		pushFollow(FOLLOW_scalar_expression_in_synpred45_JPA21468);
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
		pushFollow(FOLLOW_aggregate_expression_in_synpred46_JPA21476);
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
		pushFollow(FOLLOW_path_expression_in_synpred49_JPA21533);
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
		pushFollow(FOLLOW_scalar_expression_in_synpred50_JPA21541);
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
		pushFollow(FOLLOW_aggregate_expression_in_synpred51_JPA21549);
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
		pushFollow(FOLLOW_aggregate_expression_function_name_in_synpred53_JPA21568);
		aggregate_expression_function_name();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred53_JPA21570); if (state.failed) return;

		// JPA2.g:189:45: ( DISTINCT )?
		int alt151=2;
		int LA151_0 = input.LA(1);
		if ( (LA151_0==DISTINCT) ) {
			alt151=1;
		}
		switch (alt151) {
			case 1 :
				// JPA2.g:189:46: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred53_JPA21572); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_arithmetic_expression_in_synpred53_JPA21576);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred53_JPA21577); if (state.failed) return;

		}

	}
	// $ANTLR end synpred53_JPA2

	// $ANTLR start synpred55_JPA2
	public final void synpred55_JPA2_fragment() throws RecognitionException {
		// JPA2.g:191:7: ( 'COUNT' '(' ( DISTINCT )? count_argument ')' )
		// JPA2.g:191:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
		{
		match(input,COUNT,FOLLOW_COUNT_in_synpred55_JPA21611); if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred55_JPA21613); if (state.failed) return;

		// JPA2.g:191:18: ( DISTINCT )?
		int alt152=2;
		int LA152_0 = input.LA(1);
		if ( (LA152_0==DISTINCT) ) {
			alt152=1;
		}
		switch (alt152) {
			case 1 :
				// JPA2.g:191:19: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred55_JPA21615); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_count_argument_in_synpred55_JPA21619);
		count_argument();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred55_JPA21621); if (state.failed) return;

		}

	}
	// $ANTLR end synpred55_JPA2

	// $ANTLR start synpred67_JPA2
	public final void synpred67_JPA2_fragment() throws RecognitionException {
		// JPA2.g:214:7: ( path_expression )
		// JPA2.g:214:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred67_JPA21892);
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
		pushFollow(FOLLOW_general_identification_variable_in_synpred68_JPA21896);
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
		pushFollow(FOLLOW_result_variable_in_synpred69_JPA21900);
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
		pushFollow(FOLLOW_scalar_expression_in_synpred70_JPA21904);
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
		pushFollow(FOLLOW_general_derived_path_in_synpred80_JPA22111);
		general_derived_path();
		state._fsp--;
		if (state.failed) return;

		match(input,68,FOLLOW_68_in_synpred80_JPA22112); if (state.failed) return;

		pushFollow(FOLLOW_single_valued_object_field_in_synpred80_JPA22113);
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
		pushFollow(FOLLOW_path_expression_in_synpred85_JPA22265);
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
		pushFollow(FOLLOW_scalar_expression_in_synpred86_JPA22273);
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
		pushFollow(FOLLOW_aggregate_expression_in_synpred87_JPA22281);
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
		pushFollow(FOLLOW_arithmetic_expression_in_synpred88_JPA22300);
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
		pushFollow(FOLLOW_string_expression_in_synpred89_JPA22308);
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
		pushFollow(FOLLOW_enum_expression_in_synpred90_JPA22316);
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
		pushFollow(FOLLOW_datetime_expression_in_synpred91_JPA22324);
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
		pushFollow(FOLLOW_boolean_expression_in_synpred92_JPA22332);
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
		pushFollow(FOLLOW_case_expression_in_synpred93_JPA22340);
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
		match(input,NOT,FOLLOW_NOT_in_synpred96_JPA22400); if (state.failed) return;

		}

	}
	// $ANTLR end synpred96_JPA2

	// $ANTLR start synpred97_JPA2
	public final void synpred97_JPA2_fragment() throws RecognitionException {
		// JPA2.g:268:7: ( simple_cond_expression )
		// JPA2.g:268:7: simple_cond_expression
		{
		pushFollow(FOLLOW_simple_cond_expression_in_synpred97_JPA22415);
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
		pushFollow(FOLLOW_comparison_expression_in_synpred98_JPA22452);
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
		pushFollow(FOLLOW_between_expression_in_synpred99_JPA22460);
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
		pushFollow(FOLLOW_in_expression_in_synpred100_JPA22468);
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
		pushFollow(FOLLOW_like_expression_in_synpred101_JPA22476);
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
		pushFollow(FOLLOW_null_comparison_expression_in_synpred102_JPA22484);
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
		pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred103_JPA22492);
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
		pushFollow(FOLLOW_collection_member_expression_in_synpred104_JPA22500);
		collection_member_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred104_JPA2

	// $ANTLR start synpred128_JPA2
	public final void synpred128_JPA2_fragment() throws RecognitionException {
		// JPA2.g:307:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
		// JPA2.g:307:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred128_JPA22788);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:307:29: ( 'NOT' )?
		int alt154=2;
		int LA154_0 = input.LA(1);
		if ( (LA154_0==NOT) ) {
			alt154=1;
		}
		switch (alt154) {
			case 1 :
				// JPA2.g:307:30: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred128_JPA22791); if (state.failed) return;

				}
				break;

		}

		match(input,87,FOLLOW_87_in_synpred128_JPA22795); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred128_JPA22797);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred128_JPA22799); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred128_JPA22801);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred128_JPA2

	// $ANTLR start synpred130_JPA2
	public final void synpred130_JPA2_fragment() throws RecognitionException {
		// JPA2.g:308:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
		// JPA2.g:308:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred130_JPA22809);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:308:25: ( 'NOT' )?
		int alt155=2;
		int LA155_0 = input.LA(1);
		if ( (LA155_0==NOT) ) {
			alt155=1;
		}
		switch (alt155) {
			case 1 :
				// JPA2.g:308:26: 'NOT'
				{
				match(input,NOT,FOLLOW_NOT_in_synpred130_JPA22812); if (state.failed) return;

				}
				break;

		}

		match(input,87,FOLLOW_87_in_synpred130_JPA22816); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred130_JPA22818);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred130_JPA22820); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred130_JPA22822);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred130_JPA2

	// $ANTLR start synpred143_JPA2
	public final void synpred143_JPA2_fragment() throws RecognitionException {
		// JPA2.g:324:42: ( string_expression )
		// JPA2.g:324:42: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred143_JPA23011);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred143_JPA2

	// $ANTLR start synpred144_JPA2
	public final void synpred144_JPA2_fragment() throws RecognitionException {
		// JPA2.g:324:62: ( pattern_value )
		// JPA2.g:324:62: pattern_value
		{
		pushFollow(FOLLOW_pattern_value_in_synpred144_JPA23015);
		pattern_value();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred144_JPA2

	// $ANTLR start synpred146_JPA2
	public final void synpred146_JPA2_fragment() throws RecognitionException {
		// JPA2.g:326:8: ( path_expression )
		// JPA2.g:326:8: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred146_JPA23038);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred146_JPA2

	// $ANTLR start synpred154_JPA2
	public final void synpred154_JPA2_fragment() throws RecognitionException {
		// JPA2.g:336:7: ( identification_variable )
		// JPA2.g:336:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred154_JPA23140);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred154_JPA2

	// $ANTLR start synpred161_JPA2
	public final void synpred161_JPA2_fragment() throws RecognitionException {
		// JPA2.g:344:7: ( string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression ) )
		// JPA2.g:344:7: string_expression ( comparison_operator | 'REGEXP' ) ( string_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_string_expression_in_synpred161_JPA23209);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:344:25: ( comparison_operator | 'REGEXP' )
		int alt157=2;
		int LA157_0 = input.LA(1);
		if ( ((LA157_0 >= 71 && LA157_0 <= 76)) ) {
			alt157=1;
		}
		else if ( (LA157_0==127) ) {
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
				// JPA2.g:344:26: comparison_operator
				{
				pushFollow(FOLLOW_comparison_operator_in_synpred161_JPA23212);
				comparison_operator();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:344:48: 'REGEXP'
				{
				match(input,127,FOLLOW_127_in_synpred161_JPA23216); if (state.failed) return;

				}
				break;

		}

		// JPA2.g:344:58: ( string_expression | all_or_any_expression )
		int alt158=2;
		int LA158_0 = input.LA(1);
		if ( (LA158_0==AVG||LA158_0==CASE||LA158_0==COUNT||LA158_0==GROUP||(LA158_0 >= LOWER && LA158_0 <= NAMED_PARAMETER)||(LA158_0 >= STRING_LITERAL && LA158_0 <= SUM)||LA158_0==WORD||LA158_0==63||LA158_0==77||LA158_0==82||(LA158_0 >= 89 && LA158_0 <= 91)||LA158_0==102||LA158_0==104||LA158_0==120||LA158_0==133||LA158_0==136||LA158_0==139) ) {
			alt158=1;
		}
		else if ( ((LA158_0 >= 85 && LA158_0 <= 86)||LA158_0==131) ) {
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
				// JPA2.g:344:59: string_expression
				{
				pushFollow(FOLLOW_string_expression_in_synpred161_JPA23220);
				string_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:344:79: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred161_JPA23224);
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
		// JPA2.g:345:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
		// JPA2.g:345:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred164_JPA23233);
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
		int alt159=2;
		int LA159_0 = input.LA(1);
		if ( (LA159_0==CASE||LA159_0==GROUP||LA159_0==LPAREN||LA159_0==NAMED_PARAMETER||LA159_0==WORD||LA159_0==63||LA159_0==77||LA159_0==82||(LA159_0 >= 89 && LA159_0 <= 90)||LA159_0==102||LA159_0==104||LA159_0==120||(LA159_0 >= 145 && LA159_0 <= 146)) ) {
			alt159=1;
		}
		else if ( ((LA159_0 >= 85 && LA159_0 <= 86)||LA159_0==131) ) {
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
				// JPA2.g:345:40: boolean_expression
				{
				pushFollow(FOLLOW_boolean_expression_in_synpred164_JPA23244);
				boolean_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:345:61: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred164_JPA23248);
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
		// JPA2.g:346:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
		// JPA2.g:346:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_enum_expression_in_synpred167_JPA23257);
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
		int alt160=2;
		int LA160_0 = input.LA(1);
		if ( (LA160_0==CASE||LA160_0==GROUP||LA160_0==LPAREN||LA160_0==NAMED_PARAMETER||LA160_0==WORD||LA160_0==63||LA160_0==77||LA160_0==90||LA160_0==120) ) {
			alt160=1;
		}
		else if ( ((LA160_0 >= 85 && LA160_0 <= 86)||LA160_0==131) ) {
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
				// JPA2.g:346:35: enum_expression
				{
				pushFollow(FOLLOW_enum_expression_in_synpred167_JPA23266);
				enum_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:346:53: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred167_JPA23270);
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
		// JPA2.g:347:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
		// JPA2.g:347:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred169_JPA23279);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred169_JPA23281);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:347:47: ( datetime_expression | all_or_any_expression )
		int alt161=2;
		int LA161_0 = input.LA(1);
		if ( (LA161_0==AVG||LA161_0==CASE||LA161_0==COUNT||LA161_0==GROUP||(LA161_0 >= LPAREN && LA161_0 <= NAMED_PARAMETER)||LA161_0==SUM||LA161_0==WORD||LA161_0==63||LA161_0==77||LA161_0==82||(LA161_0 >= 89 && LA161_0 <= 90)||(LA161_0 >= 92 && LA161_0 <= 94)||LA161_0==102||LA161_0==104||LA161_0==120) ) {
			alt161=1;
		}
		else if ( ((LA161_0 >= 85 && LA161_0 <= 86)||LA161_0==131) ) {
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
				// JPA2.g:347:48: datetime_expression
				{
				pushFollow(FOLLOW_datetime_expression_in_synpred169_JPA23284);
				datetime_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:347:70: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred169_JPA23288);
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
		// JPA2.g:348:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
		// JPA2.g:348:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_entity_expression_in_synpred172_JPA23297);
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
		int alt162=2;
		int LA162_0 = input.LA(1);
		if ( (LA162_0==GROUP||LA162_0==NAMED_PARAMETER||LA162_0==WORD||LA162_0==63||LA162_0==77) ) {
			alt162=1;
		}
		else if ( ((LA162_0 >= 85 && LA162_0 <= 86)||LA162_0==131) ) {
			alt162=2;
		}

		else {
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 162, 0, input);
			throw nvae;
		}

		switch (alt162) {
			case 1 :
				// JPA2.g:348:39: entity_expression
				{
				pushFollow(FOLLOW_entity_expression_in_synpred172_JPA23308);
				entity_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:348:59: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred172_JPA23312);
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
		// JPA2.g:349:7: ( entity_type_expression ( '=' | '<>' ) entity_type_expression )
		// JPA2.g:349:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
		{
		pushFollow(FOLLOW_entity_type_expression_in_synpred174_JPA23321);
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
		pushFollow(FOLLOW_entity_type_expression_in_synpred174_JPA23331);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred174_JPA2

	// $ANTLR start synpred183_JPA2
	public final void synpred183_JPA2_fragment() throws RecognitionException {
		// JPA2.g:360:7: ( arithmetic_term ( ( '+' | '-' ) arithmetic_term )+ )
		// JPA2.g:360:7: arithmetic_term ( ( '+' | '-' ) arithmetic_term )+
		{
		pushFollow(FOLLOW_arithmetic_term_in_synpred183_JPA23412);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:360:23: ( ( '+' | '-' ) arithmetic_term )+
		int cnt163=0;
		loop163:
		while (true) {
			int alt163=2;
			int LA163_0 = input.LA(1);
			if ( (LA163_0==65||LA163_0==67) ) {
				alt163=1;
			}

			switch (alt163) {
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
				pushFollow(FOLLOW_arithmetic_term_in_synpred183_JPA23423);
				arithmetic_term();
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
	// $ANTLR end synpred183_JPA2

	// $ANTLR start synpred186_JPA2
	public final void synpred186_JPA2_fragment() throws RecognitionException {
		// JPA2.g:363:7: ( arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+ )
		// JPA2.g:363:7: arithmetic_factor ( ( '*' | '/' ) arithmetic_factor )+
		{
		pushFollow(FOLLOW_arithmetic_factor_in_synpred186_JPA23444);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:363:25: ( ( '*' | '/' ) arithmetic_factor )+
		int cnt164=0;
		loop164:
		while (true) {
			int alt164=2;
			int LA164_0 = input.LA(1);
			if ( (LA164_0==64||LA164_0==69) ) {
				alt164=1;
			}

			switch (alt164) {
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
				pushFollow(FOLLOW_arithmetic_factor_in_synpred186_JPA23456);
				arithmetic_factor();
				state._fsp--;
				if (state.failed) return;

				}
				break;

			default :
				if ( cnt164 >= 1 ) break loop164;
				if (state.backtracking>0) {state.failed=true; return;}
				EarlyExitException eee = new EarlyExitException(164, input);
				throw eee;
			}
			cnt164++;
		}

		}

	}
	// $ANTLR end synpred186_JPA2

	// $ANTLR start synpred190_JPA2
	public final void synpred190_JPA2_fragment() throws RecognitionException {
		// JPA2.g:369:7: ( decimal_literal )
		// JPA2.g:369:7: decimal_literal
		{
		pushFollow(FOLLOW_decimal_literal_in_synpred190_JPA23508);
		decimal_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred190_JPA2

	// $ANTLR start synpred191_JPA2
	public final void synpred191_JPA2_fragment() throws RecognitionException {
		// JPA2.g:370:7: ( numeric_literal )
		// JPA2.g:370:7: numeric_literal
		{
		pushFollow(FOLLOW_numeric_literal_in_synpred191_JPA23516);
		numeric_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred191_JPA2

	// $ANTLR start synpred192_JPA2
	public final void synpred192_JPA2_fragment() throws RecognitionException {
		// JPA2.g:371:7: ( '(' arithmetic_expression ')' )
		// JPA2.g:371:7: '(' arithmetic_expression ')'
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred192_JPA23524); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred192_JPA23525);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred192_JPA23526); if (state.failed) return;

		}

	}
	// $ANTLR end synpred192_JPA2

	// $ANTLR start synpred195_JPA2
	public final void synpred195_JPA2_fragment() throws RecognitionException {
		// JPA2.g:374:7: ( aggregate_expression )
		// JPA2.g:374:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred195_JPA23550);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred195_JPA2

	// $ANTLR start synpred197_JPA2
	public final void synpred197_JPA2_fragment() throws RecognitionException {
		// JPA2.g:376:7: ( function_invocation )
		// JPA2.g:376:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred197_JPA23566);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred197_JPA2

	// $ANTLR start synpred203_JPA2
	public final void synpred203_JPA2_fragment() throws RecognitionException {
		// JPA2.g:384:7: ( aggregate_expression )
		// JPA2.g:384:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred203_JPA23625);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred203_JPA2

	// $ANTLR start synpred205_JPA2
	public final void synpred205_JPA2_fragment() throws RecognitionException {
		// JPA2.g:386:7: ( function_invocation )
		// JPA2.g:386:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred205_JPA23641);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred205_JPA2

	// $ANTLR start synpred207_JPA2
	public final void synpred207_JPA2_fragment() throws RecognitionException {
		// JPA2.g:390:7: ( path_expression )
		// JPA2.g:390:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred207_JPA23668);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred207_JPA2

	// $ANTLR start synpred210_JPA2
	public final void synpred210_JPA2_fragment() throws RecognitionException {
		// JPA2.g:393:7: ( aggregate_expression )
		// JPA2.g:393:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred210_JPA23692);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred210_JPA2

	// $ANTLR start synpred212_JPA2
	public final void synpred212_JPA2_fragment() throws RecognitionException {
		// JPA2.g:395:7: ( function_invocation )
		// JPA2.g:395:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred212_JPA23708);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred212_JPA2

	// $ANTLR start synpred214_JPA2
	public final void synpred214_JPA2_fragment() throws RecognitionException {
		// JPA2.g:397:7: ( date_time_timestamp_literal )
		// JPA2.g:397:7: date_time_timestamp_literal
		{
		pushFollow(FOLLOW_date_time_timestamp_literal_in_synpred214_JPA23724);
		date_time_timestamp_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred214_JPA2

	// $ANTLR start synpred252_JPA2
	public final void synpred252_JPA2_fragment() throws RecognitionException {
		// JPA2.g:448:7: ( literal )
		// JPA2.g:448:7: literal
		{
		pushFollow(FOLLOW_literal_in_synpred252_JPA24179);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred252_JPA2

	// $ANTLR start synpred253_JPA2
	public final void synpred253_JPA2_fragment() throws RecognitionException {
		// JPA2.g:449:7: ( path_expression )
		// JPA2.g:449:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred253_JPA24187);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred253_JPA2

	// $ANTLR start synpred254_JPA2
	public final void synpred254_JPA2_fragment() throws RecognitionException {
		// JPA2.g:450:7: ( input_parameter )
		// JPA2.g:450:7: input_parameter
		{
		pushFollow(FOLLOW_input_parameter_in_synpred254_JPA24195);
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
		"\32\uffff";
	static final String DFA41_eofS =
		"\32\uffff";
	static final String DFA41_minS =
		"\1\7\1\33\2\uffff\2\7\1\43\1\uffff\1\5\17\43\1\0\1\5";
	static final String DFA41_maxS =
		"\1\150\1\33\2\uffff\2\u0084\1\104\1\uffff\1\u0090\17\105\1\0\1\u0090";
	static final String DFA41_acceptS =
		"\2\uffff\1\1\1\3\3\uffff\1\2\22\uffff";
	static final String DFA41_specialS =
		"\30\uffff\1\0\1\uffff}>";
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
			"\16\1\17\3\uffff\1\15\1\uffff\1\30\3\uffff\1\20\25\uffff\1\11\2\uffff"+
			"\2\2\1\uffff\1\2\1\uffff\1\2\31\uffff\1\27\3\uffff\1\27\3\uffff\1\13"+
			"\1\uffff\1\27\7\uffff\1\24\1\27\1\uffff\1\27\6\uffff\1\26\2\uffff\1\27"+
			"\1\uffff\1\27\1\12\14\uffff\1\27\1\uffff\1\27",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\30\34\uffff\2\2\1\uffff\1\2\1\31\1\2",
			"\1\uffff",
			"\1\23\1\uffff\1\21\1\uffff\1\25\1\uffff\1\22\6\uffff\1\14\11\uffff\1"+
			"\16\1\17\3\uffff\1\15\1\uffff\1\30\3\uffff\1\20\25\uffff\1\11\2\uffff"+
			"\2\2\1\uffff\1\2\1\uffff\1\2\31\uffff\1\27\3\uffff\1\27\3\uffff\1\13"+
			"\1\uffff\1\27\7\uffff\1\24\1\27\1\uffff\1\27\6\uffff\1\26\2\uffff\1\27"+
			"\1\uffff\1\27\1\12\14\uffff\1\27\1\uffff\1\27"
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
						int LA41_24 = input.LA(1);
						 
						int index41_24 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred53_JPA2()) ) {s = 2;}
						else if ( (synpred55_JPA2()) ) {s = 7;}
						 
						input.seek(index41_24);
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
	public static final BitSet FOLLOW_129_in_select_statement534 = new BitSet(new long[]{0xA00000C07C442A80L,0x092945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_select_clause_in_select_statement536 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_from_clause_in_select_statement538 = new BitSet(new long[]{0x00000002000C0000L,0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_where_clause_in_select_statement541 = new BitSet(new long[]{0x00000002000C0000L});
	public static final BitSet FOLLOW_groupby_clause_in_select_statement546 = new BitSet(new long[]{0x0000000200080000L});
	public static final BitSet FOLLOW_having_clause_in_select_statement551 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_orderby_clause_in_select_statement556 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_select_statement560 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_138_in_update_statement616 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_update_clause_in_update_statement618 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_where_clause_in_update_statement621 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_96_in_delete_statement657 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_delete_clause_in_delete_statement659 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_where_clause_in_delete_statement662 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_from_clause700 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause702 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_from_clause705 = new BitSet(new long[]{0x2000000000100000L});
	public static final BitSet FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause707 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration741 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration750 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration774 = new BitSet(new long[]{0x0000000001A00002L});
	public static final BitSet FOLLOW_joined_clause_in_identification_variable_declaration776 = new BitSet(new long[]{0x0000000001A00002L});
	public static final BitSet FOLLOW_joined_clause_in_join_section807 = new BitSet(new long[]{0x0000000001A00002L});
	public static final BitSet FOLLOW_join_in_joined_clause815 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fetch_join_in_joined_clause819 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_range_variable_declaration831 = new BitSet(new long[]{0x2000000000040020L});
	public static final BitSet FOLLOW_AS_in_range_variable_declaration834 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration838 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_join867 = new BitSet(new long[]{0x2000000000040000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_join_association_path_expression_in_join869 = new BitSet(new long[]{0x2000000000040020L});
	public static final BitSet FOLLOW_AS_in_join872 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_join876 = new BitSet(new long[]{0x0000000000000002L,0x2000000000000000L});
	public static final BitSet FOLLOW_125_in_join879 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000060BB4L});
	public static final BitSet FOLLOW_conditional_expression_in_join881 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_fetch_join915 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_FETCH_in_fetch_join917 = new BitSet(new long[]{0x2000000000040000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join919 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFT_in_join_spec933 = new BitSet(new long[]{0x0000000400800000L});
	public static final BitSet FOLLOW_OUTER_in_join_spec937 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_INNER_in_join_spec943 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_JOIN_in_join_spec948 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression962 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_join_association_path_expression964 = new BitSet(new long[]{0x2000008230040AA2L,0x4816028880000000L,0x0000000000014003L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression967 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_join_association_path_expression968 = new BitSet(new long[]{0x2000008230040AA2L,0x4816028880000000L,0x0000000000014003L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression972 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_135_in_join_association_path_expression1007 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression1009 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_join_association_path_expression1011 = new BitSet(new long[]{0x2000008230040AA0L,0x4816028880000000L,0x0000000000014003L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression1014 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_join_association_path_expression1015 = new BitSet(new long[]{0x2000008230040AA0L,0x4816028880000000L,0x0000000000014003L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression1019 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_AS_in_join_association_path_expression1022 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_subtype_in_join_association_path_expression1024 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_join_association_path_expression1026 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_join_association_path_expression1059 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_collection_member_declaration1072 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration1073 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_declaration1075 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration1077 = new BitSet(new long[]{0x2000000000040020L});
	public static final BitSet FOLLOW_AS_in_collection_member_declaration1080 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration1084 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_qualified_identification_variable1113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_98_in_qualified_identification_variable1121 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_qualified_identification_variable1122 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_qualified_identification_variable1123 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_108_in_map_field_identification_variable1130 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1131 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1132 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_141_in_map_field_identification_variable1136 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable1137 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable1138 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_path_expression1152 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_path_expression1154 = new BitSet(new long[]{0x2000008230040AA2L,0x4816028880000000L,0x0000000000014003L});
	public static final BitSet FOLLOW_field_in_path_expression1157 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_path_expression1158 = new BitSet(new long[]{0x2000008230040AA2L,0x4816028880000000L,0x0000000000014003L});
	public static final BitSet FOLLOW_field_in_path_expression1162 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_general_identification_variable1201 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_general_identification_variable1209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_update_clause1222 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_SET_in_update_clause1224 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1226 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_update_clause1229 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1231 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_path_expression_in_update_item1273 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_74_in_update_item1275 = new BitSet(new long[]{0xA00000C07C440A80L,0x018945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_new_value_in_update_item1277 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_new_value1288 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_new_value1296 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_119_in_new_value1304 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_delete_clause1318 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_delete_clause1320 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_select_clause1348 = new BitSet(new long[]{0xA00000C07C440A80L,0x092945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_select_item_in_select_clause1352 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_select_clause1355 = new BitSet(new long[]{0xA00000C07C440A80L,0x092945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_select_item_in_select_clause1357 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_select_expression_in_select_item1400 = new BitSet(new long[]{0x2000000000000022L});
	public static final BitSet FOLLOW_AS_in_select_item1404 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_result_variable_in_select_item1408 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_select_expression1421 = new BitSet(new long[]{0x0000000000000002L,0x000000000000002BL});
	public static final BitSet FOLLOW_set_in_select_expression1424 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1440 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_select_expression1476 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_123_in_select_expression1484 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_select_expression1486 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1487 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_select_expression1488 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_constructor_expression_in_select_expression1496 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_117_in_constructor_expression1507 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_constructor_name_in_constructor_expression1509 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_constructor_expression1511 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1513 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_constructor_expression1516 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1518 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_constructor_expression1522 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_constructor_item1533 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_constructor_item1541 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_constructor_item1549 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_constructor_item1557 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1568 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1570 = new BitSet(new long[]{0xA000008078442A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1572 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_aggregate_expression1576 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1577 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_aggregate_expression1611 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1613 = new BitSet(new long[]{0x2000000000042000L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1615 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_count_argument_in_aggregate_expression1619 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1621 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_aggregate_expression1656 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_count_argument1693 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_count_argument1697 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_143_in_where_clause1710 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000060BB4L});
	public static final BitSet FOLLOW_conditional_expression_in_where_clause1712 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_groupby_clause1734 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_BY_in_groupby_clause1736 = new BitSet(new long[]{0x2000000000040000L,0x0000004000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1738 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_groupby_clause1741 = new BitSet(new long[]{0x2000000000040000L,0x0000004000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1743 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_path_expression_in_groupby_item1777 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_groupby_item1781 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extract_function_in_groupby_item1785 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HAVING_in_having_clause1796 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000060BB4L});
	public static final BitSet FOLLOW_conditional_expression_in_having_clause1798 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderby_clause1809 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_BY_in_orderby_clause1811 = new BitSet(new long[]{0xA00000C07C440A80L,0x010955407E14204AL,0x0000000000062B34L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1813 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_orderby_clause1816 = new BitSet(new long[]{0xA00000C07C440A80L,0x010955407E14204AL,0x0000000000062B34L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1818 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1852 = new BitSet(new long[]{0x0000000000001042L,0x0600000000000000L});
	public static final BitSet FOLLOW_sort_in_orderby_item1854 = new BitSet(new long[]{0x0000000000000002L,0x0600000000000000L});
	public static final BitSet FOLLOW_sortNulls_in_orderby_item1857 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_orderby_variable1892 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_orderby_variable1896 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_orderby_variable1900 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_orderby_variable1904 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_orderby_variable1908 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_subquery1955 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_129_in_subquery1957 = new BitSet(new long[]{0xA00000C07C442A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_simple_select_clause_in_subquery1959 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_subquery_from_clause_in_subquery1961 = new BitSet(new long[]{0x00000008000C0000L,0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_where_clause_in_subquery1964 = new BitSet(new long[]{0x00000008000C0000L});
	public static final BitSet FOLLOW_groupby_clause_in_subquery1969 = new BitSet(new long[]{0x0000000800080000L});
	public static final BitSet FOLLOW_having_clause_in_subquery1974 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_subquery1980 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_subquery_from_clause2030 = new BitSet(new long[]{0x2000000000100000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause2032 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_subquery_from_clause2035 = new BitSet(new long[]{0x2000000000100000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause2037 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration2075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration2083 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_AS_in_subselect_identification_variable_declaration2085 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration2087 = new BitSet(new long[]{0x0000000001A00002L});
	public static final BitSet FOLLOW_join_in_subselect_identification_variable_declaration2090 = new BitSet(new long[]{0x0000000001A00002L});
	public static final BitSet FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration2100 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression2111 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_derived_path_expression2112 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_path_expression2113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression2121 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_derived_path_expression2122 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_path_expression2123 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_derived_path_in_general_derived_path2134 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_treated_derived_path_in_general_derived_path2142 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_general_derived_path2144 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_general_derived_path2145 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_simple_derived_path2163 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_135_in_treated_derived_path2180 = new BitSet(new long[]{0x2000000000000000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_general_derived_path_in_treated_derived_path2181 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_AS_in_treated_derived_path2183 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_subtype_in_treated_derived_path2185 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_treated_derived_path2187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_derived_collection_member_declaration2198 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration2200 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_derived_collection_member_declaration2201 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_collection_member_declaration2203 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_derived_collection_member_declaration2205 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_collection_member_declaration2208 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause2221 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause2225 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_simple_select_expression2265 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_select_expression2273 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression2281 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_select_expression2289 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_scalar_expression2300 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_scalar_expression2308 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_scalar_expression2316 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_scalar_expression2324 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_scalar_expression2332 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_scalar_expression2340 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_scalar_expression2348 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2360 = new BitSet(new long[]{0x0000000100000002L});
	public static final BitSet FOLLOW_OR_in_conditional_expression2364 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000060BB4L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2366 = new BitSet(new long[]{0x0000000100000002L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2380 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_conditional_term2384 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000060BB4L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2386 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_NOT_in_conditional_factor2400 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000060BB4L});
	public static final BitSet FOLLOW_conditional_primary_in_conditional_factor2404 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_conditional_primary2415 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_conditional_primary2439 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000060BB4L});
	public static final BitSet FOLLOW_conditional_expression_in_conditional_primary2440 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_conditional_primary2441 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression2452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_simple_cond_expression2460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_simple_cond_expression2468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_simple_cond_expression2476 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression2484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2492 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression2500 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression2508 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression2516 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2529 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2537 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2545 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2553 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2561 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_78_in_date_between_macro_expression2573 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2575 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2577 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_between_macro_expression2579 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
	public static final BitSet FOLLOW_118_in_date_between_macro_expression2581 = new BitSet(new long[]{0x0000000000000000L,0x000000000000000EL});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2584 = new BitSet(new long[]{0x0000000000400000L,0x0000000000000040L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2592 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_between_macro_expression2596 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
	public static final BitSet FOLLOW_118_in_date_between_macro_expression2598 = new BitSet(new long[]{0x0000000000000000L,0x000000000000000EL});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2601 = new BitSet(new long[]{0x0000000000400000L,0x0000000000000040L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2609 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_between_macro_expression2613 = new BitSet(new long[]{0x0000000000000000L,0x0014020080000000L,0x0000000000010001L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2615 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_between_macro_expression2639 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_140_in_date_between_macro_expression2641 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2645 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_80_in_date_before_macro_expression2657 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2659 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2661 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_before_macro_expression2663 = new BitSet(new long[]{0xA000000040040000L,0x0000000000002000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2666 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2670 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_before_macro_expression2674 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_140_in_date_before_macro_expression2676 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2680 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_79_in_date_after_macro_expression2692 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2694 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2696 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_after_macro_expression2698 = new BitSet(new long[]{0xA000000040040000L,0x0000000000002000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2701 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2705 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_after_macro_expression2709 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_140_in_date_after_macro_expression2711 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2715 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_date_equals_macro_expression2727 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2729 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2731 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_equals_macro_expression2733 = new BitSet(new long[]{0xA000000040040000L,0x0000000000002000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2736 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2740 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_equals_macro_expression2744 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_140_in_date_equals_macro_expression2746 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2750 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_83_in_date_today_macro_expression2762 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2764 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2766 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_date_today_macro_expression2769 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_140_in_date_today_macro_expression2771 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2775 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2788 = new BitSet(new long[]{0x0000000080000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2791 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_between_expression2795 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2797 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2799 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2801 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2809 = new BitSet(new long[]{0x0000000080000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2812 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_between_expression2816 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2818 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2820 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2822 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2830 = new BitSet(new long[]{0x0000000080000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_NOT_in_between_expression2833 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_between_expression2837 = new BitSet(new long[]{0xA000008078040A80L,0x0100014076042000L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2839 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2841 = new BitSet(new long[]{0xA000008078040A80L,0x0100014076042000L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2843 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2855 = new BitSet(new long[]{0x0000000080100000L});
	public static final BitSet FOLLOW_type_discriminator_in_in_expression2859 = new BitSet(new long[]{0x0000000080100000L});
	public static final BitSet FOLLOW_identification_variable_in_in_expression2863 = new BitSet(new long[]{0x0000000080100000L});
	public static final BitSet FOLLOW_NOT_in_in_expression2867 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_IN_in_in_expression2871 = new BitSet(new long[]{0x8000000048000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2887 = new BitSet(new long[]{0x8000004040400000L,0x0000000000042040L});
	public static final BitSet FOLLOW_in_item_in_in_expression2889 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_in_expression2892 = new BitSet(new long[]{0x8000004040400000L,0x0000000000042040L});
	public static final BitSet FOLLOW_in_item_in_in_expression2894 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2898 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_in_expression2914 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_valued_input_parameter_in_in_expression2930 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2946 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2948 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2950 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_in_item2978 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_in_item2982 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_single_valued_input_parameter_in_in_item2986 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_function_in_in_item2990 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_like_expression3001 = new BitSet(new long[]{0x0000000080000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_NOT_in_like_expression3004 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_111_in_like_expression3008 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_like_expression3011 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
	public static final BitSet FOLLOW_pattern_value_in_like_expression3015 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
	public static final BitSet FOLLOW_input_parameter_in_like_expression3019 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
	public static final BitSet FOLLOW_100_in_like_expression3022 = new BitSet(new long[]{0x0000024000000000L});
	public static final BitSet FOLLOW_escape_character_in_like_expression3024 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_null_comparison_expression3038 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression3042 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_null_comparison_expression3046 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_107_in_null_comparison_expression3049 = new BitSet(new long[]{0x0000000080000000L,0x0080000000000000L});
	public static final BitSet FOLLOW_NOT_in_null_comparison_expression3052 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
	public static final BitSet FOLLOW_119_in_null_comparison_expression3056 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression3067 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_107_in_empty_collection_comparison_expression3069 = new BitSet(new long[]{0x0000000080000000L,0x0000000200000000L});
	public static final BitSet FOLLOW_NOT_in_empty_collection_comparison_expression3072 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
	public static final BitSet FOLLOW_97_in_empty_collection_comparison_expression3076 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_or_value_expression_in_collection_member_expression3087 = new BitSet(new long[]{0x0000000080000000L,0x0002000000000000L});
	public static final BitSet FOLLOW_NOT_in_collection_member_expression3091 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
	public static final BitSet FOLLOW_113_in_collection_member_expression3095 = new BitSet(new long[]{0x2000000000040000L,0x1000000000000000L});
	public static final BitSet FOLLOW_124_in_collection_member_expression3098 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_expression3102 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_or_value_expression3113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression3121 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_entity_or_value_expression3129 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_or_value_expression3140 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_or_value_expression3148 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simple_entity_or_value_expression3156 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_exists_expression3168 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_101_in_exists_expression3172 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_subquery_in_exists_expression3174 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_all_or_any_expression3185 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_subquery_in_all_or_any_expression3198 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3209 = new BitSet(new long[]{0x0000000000000000L,0x8000000000001F80L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3212 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E642000L,0x0000000000000928L});
	public static final BitSet FOLLOW_127_in_comparison_expression3216 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E642000L,0x0000000000000928L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression3220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3233 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_comparison_expression3235 = new BitSet(new long[]{0xA000000048040200L,0x0100014006642000L,0x0000000000060008L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression3244 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3248 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3257 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_comparison_expression3259 = new BitSet(new long[]{0xA000000048040200L,0x0100000004602000L,0x0000000000000008L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression3266 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3270 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3279 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001F80L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3281 = new BitSet(new long[]{0xA000008078040A80L,0x0100014076642000L,0x0000000000000008L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression3284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3288 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3297 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_comparison_expression3299 = new BitSet(new long[]{0xA000000040040000L,0x0000000000602000L,0x0000000000000008L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression3308 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3312 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3321 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_comparison_expression3323 = new BitSet(new long[]{0xA000000040000000L,0x0000000000002000L,0x0000000000000200L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3331 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3339 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001F80L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3341 = new BitSet(new long[]{0xA000008078440A80L,0x010945400674204AL,0x000000000000001CL});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3344 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3348 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3412 = new BitSet(new long[]{0x0000000000000000L,0x000000000000000AL});
	public static final BitSet FOLLOW_set_in_arithmetic_expression3415 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3423 = new BitSet(new long[]{0x0000000000000002L,0x000000000000000AL});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3433 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3444 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000021L});
	public static final BitSet FOLLOW_set_in_arithmetic_term3447 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3456 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000021L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3466 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor3489 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_arithmetic_primary3500 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_decimal_literal_in_arithmetic_primary3508 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary3516 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary3524 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_arithmetic_primary3525 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary3526 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary3534 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary3542 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary3550 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_arithmetic_primary3558 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_arithmetic_primary3566 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_arithmetic_primary3574 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_arithmetic_primary3582 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_string_expression3593 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_string_expression3601 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_string_expression3609 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_strings_in_string_expression3617 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_string_expression3625 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_string_expression3633 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_string_expression3641 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_string_expression3649 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_string_expression3657 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_datetime_expression3668 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_datetime_expression3676 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_expression3684 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_datetime_expression3692 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_datetime_expression3700 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_datetime_expression3708 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_datetime_expression3716 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_datetime_expression3724 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_datetime_expression3732 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_boolean_expression3743 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_literal_in_boolean_expression3751 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_boolean_expression3759 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_boolean_expression3767 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_boolean_expression3775 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extension_functions_in_boolean_expression3783 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_boolean_expression3791 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_enum_expression3802 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_literal_in_enum_expression3810 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_enum_expression3818 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_enum_expression3826 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_enum_expression3834 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_expression3845 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3853 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3864 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3872 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_entity_type_expression3883 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_literal_in_entity_type_expression3891 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_entity_type_expression3899 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_137_in_type_discriminator3910 = new BitSet(new long[]{0xA000000040040000L,0x0000100000002000L,0x0000000000002000L});
	public static final BitSet FOLLOW_general_identification_variable_in_type_discriminator3913 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_path_expression_in_type_discriminator3917 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_input_parameter_in_type_discriminator3921 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_type_discriminator3924 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_110_in_functions_returning_numerics3935 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3936 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3937 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_112_in_functions_returning_numerics3945 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3947 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_numerics3948 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3950 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_numerics3952 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3953 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3956 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_84_in_functions_returning_numerics3964 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3965 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3966 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_132_in_functions_returning_numerics3974 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3975 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3976 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_115_in_functions_returning_numerics3984 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3985 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_numerics3986 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3988 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3989 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_130_in_functions_returning_numerics3997 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3998 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3999 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_106_in_functions_returning_numerics4007 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_identification_variable_in_functions_returning_numerics4008 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics4009 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_91_in_functions_returning_strings4047 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4048 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_strings4049 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4051 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_strings4054 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4056 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4059 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_133_in_functions_returning_strings4067 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4069 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_strings4070 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings4072 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_functions_returning_strings4075 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings4077 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4080 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_136_in_functions_returning_strings4088 = new BitSet(new long[]{0xA00002C07C040A80L,0x010021C00F042000L,0x0000000000000960L});
	public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings4091 = new BitSet(new long[]{0x0000020000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_trim_character_in_functions_returning_strings4096 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_103_in_functions_returning_strings4100 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4104 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4106 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LOWER_in_functions_returning_strings4114 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings4116 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4117 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4118 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_139_in_functions_returning_strings4126 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings4127 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings4128 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_104_in_function_invocation4158 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_function_name_in_function_invocation4159 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_function_invocation4162 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_function_arg_in_function_invocation4164 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_function_invocation4168 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_function_arg4179 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_function_arg4187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_function_arg4195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_function_arg4203 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_case_expression_in_case_expression4214 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_case_expression_in_case_expression4222 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coalesce_expression_in_case_expression4230 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullif_expression_in_case_expression4238 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CASE_in_general_case_expression4249 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4251 = new BitSet(new long[]{0x1000000000004000L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression4254 = new BitSet(new long[]{0x1000000000004000L});
	public static final BitSet FOLLOW_ELSE_in_general_case_expression4258 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_general_case_expression4260 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_END_in_general_case_expression4262 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WHEN_in_when_clause4273 = new BitSet(new long[]{0xA00000C0FC440A80L,0x010945607E1FE04AL,0x0000000000060BB4L});
	public static final BitSet FOLLOW_conditional_expression_in_when_clause4275 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_THEN_in_when_clause4277 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_when_clause4279 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CASE_in_simple_case_expression4290 = new BitSet(new long[]{0x2000000000040000L,0x0000000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_case_operand_in_simple_case_expression4292 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4294 = new BitSet(new long[]{0x1000000000004000L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression4297 = new BitSet(new long[]{0x1000000000004000L});
	public static final BitSet FOLLOW_ELSE_in_simple_case_expression4301 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_case_expression4303 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_END_in_simple_case_expression4305 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_case_operand4316 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_case_operand4324 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WHEN_in_simple_when_clause4335 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4337 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_THEN_in_simple_when_clause4339 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause4341 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_90_in_coalesce_expression4352 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4353 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_coalesce_expression4356 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression4358 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_coalesce_expression4361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_120_in_nullif_expression4372 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4373 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_nullif_expression4375 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4377 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_nullif_expression4378 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_89_in_extension_functions4390 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_function_arg_in_extension_functions4392 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_WORD_in_extension_functions4394 = new BitSet(new long[]{0x0000000808000000L});
	public static final BitSet FOLLOW_LPAREN_in_extension_functions4397 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4398 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_66_in_extension_functions4401 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_extension_functions4403 = new BitSet(new long[]{0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4408 = new BitSet(new long[]{0x0000000808000000L});
	public static final BitSet FOLLOW_RPAREN_in_extension_functions4412 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_extract_function_in_extension_functions4420 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_function_in_extension_functions4428 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_extract_function4440 = new BitSet(new long[]{0x0000000000000000L,0x4014020880000000L,0x0000000000014001L});
	public static final BitSet FOLLOW_date_part_in_extract_function4442 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_103_in_extract_function4444 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_function_arg_in_extract_function4446 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_extract_function4448 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_82_in_enum_function4460 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_enum_function4462 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_enum_value_literal_in_enum_function4464 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_enum_function4466 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_77_in_input_parameter4533 = new BitSet(new long[]{0x0000000000400000L,0x0000000000000040L});
	public static final BitSet FOLLOW_numeric_literal_in_input_parameter4535 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter4558 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_63_in_input_parameter4579 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_WORD_in_input_parameter4581 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
	public static final BitSet FOLLOW_147_in_input_parameter4583 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_literal4611 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4623 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_constructor_name4626 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4629 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_WORD_in_enum_literal4643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_field4676 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_129_in_field4680 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_field4684 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_field4688 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_field4692 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MAX_in_field4696 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MIN_in_field4700 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SUM_in_field4704 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AVG_in_field4708 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_field4712 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AS_in_field4716 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_113_in_field4720 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CASE_in_field4724 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_123_in_field4732 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_part_in_field4736 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4764 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_parameter_name4767 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4770 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_TRIM_CHARACTER_in_trim_character4800 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string_literal4811 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_70_in_numeric_literal4823 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal4827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_decimal_literal4839 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_decimal_literal4841 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_decimal_literal4843 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_object_field4854 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_embeddable_object_field4865 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_collection_valued_field4876 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_name4887 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_subtype4898 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_type_literal4909 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_function_name4920 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_state_field4931 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_result_variable4942 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_superquery_identification_variable4953 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_date_time_timestamp_literal4964 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_pattern_value4975 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_collection_valued_input_parameter4986 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_single_valued_input_parameter4997 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_value_literal5008 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_enum_value_literal5011 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_WORD_in_enum_value_literal5014 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_field_in_synpred21_JPA2972 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred30_JPA21162 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred33_JPA21288 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_synpred34_JPA21296 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred43_JPA21421 = new BitSet(new long[]{0x0000000000000002L,0x000000000000002BL});
	public static final BitSet FOLLOW_set_in_synpred43_JPA21424 = new BitSet(new long[]{0xA00000C07C440A80L,0x010945407E14204AL,0x0000000000060B34L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred43_JPA21440 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred44_JPA21450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred45_JPA21468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred46_JPA21476 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred49_JPA21533 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred50_JPA21541 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred51_JPA21549 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_synpred53_JPA21568 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred53_JPA21570 = new BitSet(new long[]{0xA000008078442A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred53_JPA21572 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred53_JPA21576 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred53_JPA21577 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_synpred55_JPA21611 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred55_JPA21613 = new BitSet(new long[]{0x2000000000042000L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred55_JPA21615 = new BitSet(new long[]{0x2000000000040000L});
	public static final BitSet FOLLOW_count_argument_in_synpred55_JPA21619 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred55_JPA21621 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred67_JPA21892 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_synpred68_JPA21896 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_synpred69_JPA21900 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred70_JPA21904 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_synpred80_JPA22111 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_68_in_synpred80_JPA22112 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_synpred80_JPA22113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred85_JPA22265 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred86_JPA22273 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred87_JPA22281 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred88_JPA22300 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred89_JPA22308 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred90_JPA22316 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred91_JPA22324 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred92_JPA22332 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_synpred93_JPA22340 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_synpred96_JPA22400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_synpred97_JPA22415 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_synpred98_JPA22452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_synpred99_JPA22460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_synpred100_JPA22468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_synpred101_JPA22476 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_synpred102_JPA22484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred103_JPA22492 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_synpred104_JPA22500 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred128_JPA22788 = new BitSet(new long[]{0x0000000080000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_NOT_in_synpred128_JPA22791 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_synpred128_JPA22795 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred128_JPA22797 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred128_JPA22799 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred128_JPA22801 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred130_JPA22809 = new BitSet(new long[]{0x0000000080000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_NOT_in_synpred130_JPA22812 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_synpred130_JPA22816 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_synpred130_JPA22818 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred130_JPA22820 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E042000L,0x0000000000000920L});
	public static final BitSet FOLLOW_string_expression_in_synpred130_JPA22822 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred143_JPA23011 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_pattern_value_in_synpred144_JPA23015 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred146_JPA23038 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred154_JPA23140 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred161_JPA23209 = new BitSet(new long[]{0x0000000000000000L,0x8000000000001F80L});
	public static final BitSet FOLLOW_comparison_operator_in_synpred161_JPA23212 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E642000L,0x0000000000000928L});
	public static final BitSet FOLLOW_127_in_synpred161_JPA23216 = new BitSet(new long[]{0xA00000C07C040A80L,0x010001400E642000L,0x0000000000000928L});
	public static final BitSet FOLLOW_string_expression_in_synpred161_JPA23220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred161_JPA23224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred164_JPA23233 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_synpred164_JPA23235 = new BitSet(new long[]{0xA000000048040200L,0x0100014006642000L,0x0000000000060008L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred164_JPA23244 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred164_JPA23248 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred167_JPA23257 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_synpred167_JPA23259 = new BitSet(new long[]{0xA000000048040200L,0x0100000004602000L,0x0000000000000008L});
	public static final BitSet FOLLOW_enum_expression_in_synpred167_JPA23266 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred167_JPA23270 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred169_JPA23279 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001F80L});
	public static final BitSet FOLLOW_comparison_operator_in_synpred169_JPA23281 = new BitSet(new long[]{0xA000008078040A80L,0x0100014076642000L,0x0000000000000008L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred169_JPA23284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred169_JPA23288 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred172_JPA23297 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_synpred172_JPA23299 = new BitSet(new long[]{0xA000000040040000L,0x0000000000602000L,0x0000000000000008L});
	public static final BitSet FOLLOW_entity_expression_in_synpred172_JPA23308 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred172_JPA23312 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred174_JPA23321 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
	public static final BitSet FOLLOW_set_in_synpred174_JPA23323 = new BitSet(new long[]{0xA000000040000000L,0x0000000000002000L,0x0000000000000200L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred174_JPA23331 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred183_JPA23412 = new BitSet(new long[]{0x0000000000000000L,0x000000000000000AL});
	public static final BitSet FOLLOW_set_in_synpred183_JPA23415 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred183_JPA23423 = new BitSet(new long[]{0x0000000000000002L,0x000000000000000AL});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred186_JPA23444 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000021L});
	public static final BitSet FOLLOW_set_in_synpred186_JPA23447 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred186_JPA23456 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000021L});
	public static final BitSet FOLLOW_decimal_literal_in_synpred190_JPA23508 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_synpred191_JPA23516 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred192_JPA23524 = new BitSet(new long[]{0xA000008078440A80L,0x010945400614204AL,0x0000000000000014L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred192_JPA23525 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred192_JPA23526 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred195_JPA23550 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred197_JPA23566 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred203_JPA23625 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred205_JPA23641 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred207_JPA23668 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred210_JPA23692 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred212_JPA23708 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_synpred214_JPA23724 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred252_JPA24179 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred253_JPA24187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_synpred254_JPA24195 = new BitSet(new long[]{0x0000000000000002L});

    @Override
    public void emitErrorMessage(String msg) {
        //do nothing
    }

	protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
		throw new MismatchedTokenException(ttype, input);
	}
}
