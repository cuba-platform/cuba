// $ANTLR 3.5.2 JPA2.g 2015-12-14 16:16:55

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
		"INNER", "INT_NUMERAL", "JOIN", "LEFT", "LINE_COMMENT", "LOWER", "LPAREN", 
		"MAX", "MIN", "NAMED_PARAMETER", "OR", "ORDER", "OUTER", "RPAREN", "RUSSIAN_SYMBOLS", 
		"STRING_LITERAL", "SUM", "TRIM_CHARACTER", "T_AGGREGATE_EXPR", "T_COLLECTION_MEMBER", 
		"T_CONDITION", "T_GROUP_BY", "T_ID_VAR", "T_JOIN_VAR", "T_ORDER_BY", "T_ORDER_BY_FIELD", 
		"T_PARAMETER", "T_QUERY", "T_SELECTED_ENTITY", "T_SELECTED_FIELD", "T_SELECTED_ITEM", 
		"T_SELECTED_ITEMS", "T_SIMPLE_CONDITION", "T_SOURCE", "T_SOURCES", "WORD", 
		"WS", "'${'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", "'0x'", "'<'", 
		"'<='", "'<>'", "'='", "'>'", "'>='", "'?'", "'@BETWEEN'", "'@DATEAFTER'", 
		"'@DATEBEFORE'", "'@DATEEQUALS'", "'@TODAY'", "'ABS('", "'ALL'", "'ANY'", 
		"'AS'", "'BETWEEN'", "'BOTH'", "'CASE'", "'COALESCE('", "'CONCAT('", "'CURRENT_DATE'", 
		"'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'DAY'", "'DELETE'", "'ELSE'", 
		"'EMPTY'", "'END'", "'ENTRY('", "'ESCAPE'", "'EXISTS'", "'FROM'", "'FUNCTION('", 
		"'HOUR'", "'IN'", "'INDEX('", "'IS'", "'KEY('", "'LEADING'", "'LENGTH('", 
		"'LIKE'", "'LOCATE('", "'MEMBER'", "'MINUTE'", "'MOD('", "'MONTH'", "'NEW'", 
		"'NOT'", "'NOW'", "'NULL'", "'NULLIF('", "'OBJECT'", "'OF'", "'ON'", "'SECOND'", 
		"'SELECT'", "'SET'", "'SIZE('", "'SOME'", "'SQRT('", "'SUBSTRING('", "'THEN'", 
		"'TRAILING'", "'TREAT('", "'TRIM('", "'TYPE'", "'UPDATE'", "'UPPER('", 
		"'VALUE('", "'WHEN'", "'WHERE'", "'YEAR'", "'false'", "'true'", "'}'"
	};
	public static final int EOF=-1;
	public static final int T__53=53;
	public static final int T__54=54;
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
	public static final int INNER=16;
	public static final int INT_NUMERAL=17;
	public static final int JOIN=18;
	public static final int LEFT=19;
	public static final int LINE_COMMENT=20;
	public static final int LOWER=21;
	public static final int LPAREN=22;
	public static final int MAX=23;
	public static final int MIN=24;
	public static final int NAMED_PARAMETER=25;
	public static final int OR=26;
	public static final int ORDER=27;
	public static final int OUTER=28;
	public static final int RPAREN=29;
	public static final int RUSSIAN_SYMBOLS=30;
	public static final int STRING_LITERAL=31;
	public static final int SUM=32;
	public static final int TRIM_CHARACTER=33;
	public static final int T_AGGREGATE_EXPR=34;
	public static final int T_COLLECTION_MEMBER=35;
	public static final int T_CONDITION=36;
	public static final int T_GROUP_BY=37;
	public static final int T_ID_VAR=38;
	public static final int T_JOIN_VAR=39;
	public static final int T_ORDER_BY=40;
	public static final int T_ORDER_BY_FIELD=41;
	public static final int T_PARAMETER=42;
	public static final int T_QUERY=43;
	public static final int T_SELECTED_ENTITY=44;
	public static final int T_SELECTED_FIELD=45;
	public static final int T_SELECTED_ITEM=46;
	public static final int T_SELECTED_ITEMS=47;
	public static final int T_SIMPLE_CONDITION=48;
	public static final int T_SOURCE=49;
	public static final int T_SOURCES=50;
	public static final int WORD=51;
	public static final int WS=52;

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
	// JPA2.g:77:1: ql_statement : select_statement ;
	public final JPA2Parser.ql_statement_return ql_statement() throws RecognitionException {
		JPA2Parser.ql_statement_return retval = new JPA2Parser.ql_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope select_statement1 =null;


		try {
			// JPA2.g:78:5: ( select_statement )
			// JPA2.g:78:7: select_statement
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_select_statement_in_ql_statement427);
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
		ParserRuleReturnScope select_clause2 =null;
		ParserRuleReturnScope from_clause3 =null;
		ParserRuleReturnScope where_clause4 =null;
		ParserRuleReturnScope groupby_clause5 =null;
		ParserRuleReturnScope having_clause6 =null;
		ParserRuleReturnScope orderby_clause7 =null;

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
			sl=(Token)match(input,117,FOLLOW_117_in_select_statement442); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_117.add(sl);

			pushFollow(FOLLOW_select_clause_in_select_statement444);
			select_clause2=select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_clause.add(select_clause2.getTree());
			pushFollow(FOLLOW_from_clause_in_select_statement446);
			from_clause3=from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_from_clause.add(from_clause3.getTree());
			// JPA2.g:81:46: ( where_clause )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==132) ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// JPA2.g:81:47: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_select_statement449);
					where_clause4=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause4.getTree());
					}
					break;

			}

			// JPA2.g:81:62: ( groupby_clause )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==GROUP) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// JPA2.g:81:63: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_select_statement454);
					groupby_clause5=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause5.getTree());
					}
					break;

			}

			// JPA2.g:81:80: ( having_clause )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==HAVING) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// JPA2.g:81:81: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_select_statement459);
					having_clause6=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause6.getTree());
					}
					break;

			}

			// JPA2.g:81:97: ( orderby_clause )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==ORDER) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// JPA2.g:81:98: orderby_clause
					{
					pushFollow(FOLLOW_orderby_clause_in_select_statement464);
					orderby_clause7=orderby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause7.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: groupby_clause, select_clause, from_clause, where_clause, orderby_clause, having_clause
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
	// JPA2.g:84:1: update_statement : 'UPDATE' update_clause ( where_clause )? ;
	public final JPA2Parser.update_statement_return update_statement() throws RecognitionException {
		JPA2Parser.update_statement_return retval = new JPA2Parser.update_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal8=null;
		ParserRuleReturnScope update_clause9 =null;
		ParserRuleReturnScope where_clause10 =null;

		Object string_literal8_tree=null;

		try {
			// JPA2.g:85:5: ( 'UPDATE' update_clause ( where_clause )? )
			// JPA2.g:85:7: 'UPDATE' update_clause ( where_clause )?
			{
			root_0 = (Object)adaptor.nil();


			string_literal8=(Token)match(input,128,FOLLOW_128_in_update_statement520); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal8_tree = (Object)adaptor.create(string_literal8);
			adaptor.addChild(root_0, string_literal8_tree);
			}

			pushFollow(FOLLOW_update_clause_in_update_statement522);
			update_clause9=update_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, update_clause9.getTree());

			// JPA2.g:85:30: ( where_clause )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==132) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// JPA2.g:85:31: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_update_statement525);
					where_clause10=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, where_clause10.getTree());

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
	// $ANTLR end "update_statement"


	public static class delete_statement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "delete_statement"
	// JPA2.g:86:1: delete_statement : 'DELETE' 'FROM' delete_clause ( where_clause )? ;
	public final JPA2Parser.delete_statement_return delete_statement() throws RecognitionException {
		JPA2Parser.delete_statement_return retval = new JPA2Parser.delete_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal11=null;
		Token string_literal12=null;
		ParserRuleReturnScope delete_clause13 =null;
		ParserRuleReturnScope where_clause14 =null;

		Object string_literal11_tree=null;
		Object string_literal12_tree=null;

		try {
			// JPA2.g:87:5: ( 'DELETE' 'FROM' delete_clause ( where_clause )? )
			// JPA2.g:87:7: 'DELETE' 'FROM' delete_clause ( where_clause )?
			{
			root_0 = (Object)adaptor.nil();


			string_literal11=(Token)match(input,86,FOLLOW_86_in_delete_statement538); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal11_tree = (Object)adaptor.create(string_literal11);
			adaptor.addChild(root_0, string_literal11_tree);
			}

			string_literal12=(Token)match(input,93,FOLLOW_93_in_delete_statement540); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal12_tree = (Object)adaptor.create(string_literal12);
			adaptor.addChild(root_0, string_literal12_tree);
			}

			pushFollow(FOLLOW_delete_clause_in_delete_statement542);
			delete_clause13=delete_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, delete_clause13.getTree());

			// JPA2.g:87:37: ( where_clause )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==132) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// JPA2.g:87:38: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_delete_statement545);
					where_clause14=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, where_clause14.getTree());

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
	// $ANTLR end "delete_statement"


	public static class from_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "from_clause"
	// JPA2.g:89:1: from_clause : fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) ;
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
		RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
		RewriteRuleTokenStream stream_93=new RewriteRuleTokenStream(adaptor,"token 93");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
		RewriteRuleSubtreeStream stream_identification_variable_declaration_or_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration_or_collection_member_declaration");

		try {
			// JPA2.g:90:6: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) )
			// JPA2.g:90:8: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )*
			{
			fr=(Token)match(input,93,FOLLOW_93_in_from_clause562); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_93.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_from_clause564);
			identification_variable_declaration15=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration15.getTree());
			// JPA2.g:90:54: ( ',' identification_variable_declaration_or_collection_member_declaration )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==56) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// JPA2.g:90:55: ',' identification_variable_declaration_or_collection_member_declaration
					{
					char_literal16=(Token)match(input,56,FOLLOW_56_in_from_clause567); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_56.add(char_literal16);

					pushFollow(FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause569);
					identification_variable_declaration_or_collection_member_declaration17=identification_variable_declaration_or_collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable_declaration_or_collection_member_declaration.add(identification_variable_declaration_or_collection_member_declaration17.getTree());
					}
					break;

				default :
					break loop7;
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
			// 91:6: -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
			{
				// JPA2.g:91:9: ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
				// JPA2.g:91:72: ( identification_variable_declaration_or_collection_member_declaration )*
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
	// JPA2.g:92:1: identification_variable_declaration_or_collection_member_declaration : ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) );
	public final JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return identification_variable_declaration_or_collection_member_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return retval = new JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable_declaration18 =null;
		ParserRuleReturnScope collection_member_declaration19 =null;

		RewriteRuleSubtreeStream stream_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule collection_member_declaration");

		try {
			// JPA2.g:93:6: ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) )
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==WORD) ) {
				alt8=1;
			}
			else if ( (LA8_0==96) ) {
				alt8=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 8, 0, input);
				throw nvae;
			}

			switch (alt8) {
				case 1 :
					// JPA2.g:93:8: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration603);
					identification_variable_declaration18=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration18.getTree());

					}
					break;
				case 2 :
					// JPA2.g:94:8: collection_member_declaration
					{
					pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration612);
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
					// 94:38: -> ^( T_SOURCE collection_member_declaration )
					{
						// JPA2.g:94:41: ^( T_SOURCE collection_member_declaration )
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
	// JPA2.g:96:1: identification_variable_declaration : range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) ;
	public final JPA2Parser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_return retval = new JPA2Parser.identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope range_variable_declaration20 =null;
		ParserRuleReturnScope joined_clause21 =null;

		RewriteRuleSubtreeStream stream_range_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration");
		RewriteRuleSubtreeStream stream_joined_clause=new RewriteRuleSubtreeStream(adaptor,"rule joined_clause");

		try {
			// JPA2.g:97:6: ( range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) )
			// JPA2.g:97:8: range_variable_declaration ( joined_clause )*
			{
			pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration636);
			range_variable_declaration20=range_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range_variable_declaration.add(range_variable_declaration20.getTree());
			// JPA2.g:97:35: ( joined_clause )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==INNER||(LA9_0 >= JOIN && LA9_0 <= LEFT)) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// JPA2.g:97:35: joined_clause
					{
					pushFollow(FOLLOW_joined_clause_in_identification_variable_declaration638);
					joined_clause21=joined_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_joined_clause.add(joined_clause21.getTree());
					}
					break;

				default :
					break loop9;
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
			// 98:6: -> ^( T_SOURCE range_variable_declaration ( joined_clause )* )
			{
				// JPA2.g:98:9: ^( T_SOURCE range_variable_declaration ( joined_clause )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_1);
				adaptor.addChild(root_1, stream_range_variable_declaration.nextTree());
				// JPA2.g:98:68: ( joined_clause )*
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
	// JPA2.g:99:1: joined_clause : ( join | fetch_join );
	public final JPA2Parser.joined_clause_return joined_clause() throws RecognitionException {
		JPA2Parser.joined_clause_return retval = new JPA2Parser.joined_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope join22 =null;
		ParserRuleReturnScope fetch_join23 =null;


		try {
			// JPA2.g:99:15: ( join | fetch_join )
			int alt10=2;
			switch ( input.LA(1) ) {
			case LEFT:
				{
				int LA10_1 = input.LA(2);
				if ( (LA10_1==OUTER) ) {
					int LA10_4 = input.LA(3);
					if ( (LA10_4==JOIN) ) {
						int LA10_3 = input.LA(4);
						if ( (LA10_3==WORD||LA10_3==125) ) {
							alt10=1;
						}
						else if ( (LA10_3==FETCH) ) {
							alt10=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 10, 3, input);
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
								new NoViableAltException("", 10, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA10_1==JOIN) ) {
					int LA10_3 = input.LA(3);
					if ( (LA10_3==WORD||LA10_3==125) ) {
						alt10=1;
					}
					else if ( (LA10_3==FETCH) ) {
						alt10=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 10, 3, input);
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
							new NoViableAltException("", 10, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INNER:
				{
				int LA10_2 = input.LA(2);
				if ( (LA10_2==JOIN) ) {
					int LA10_3 = input.LA(3);
					if ( (LA10_3==WORD||LA10_3==125) ) {
						alt10=1;
					}
					else if ( (LA10_3==FETCH) ) {
						alt10=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 10, 3, input);
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
							new NoViableAltException("", 10, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case JOIN:
				{
				int LA10_3 = input.LA(2);
				if ( (LA10_3==WORD||LA10_3==125) ) {
					alt10=1;
				}
				else if ( (LA10_3==FETCH) ) {
					alt10=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 10, 3, input);
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
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}
			switch (alt10) {
				case 1 :
					// JPA2.g:99:17: join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_join_in_joined_clause665);
					join22=join();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join22.getTree());

					}
					break;
				case 2 :
					// JPA2.g:99:24: fetch_join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_fetch_join_in_joined_clause669);
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
	// JPA2.g:100:1: range_variable_declaration : entity_name ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) ;
	public final JPA2Parser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
		JPA2Parser.range_variable_declaration_return retval = new JPA2Parser.range_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal25=null;
		ParserRuleReturnScope entity_name24 =null;
		ParserRuleReturnScope identification_variable26 =null;

		Object string_literal25_tree=null;
		RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");
		RewriteRuleSubtreeStream stream_entity_name=new RewriteRuleSubtreeStream(adaptor,"rule entity_name");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:101:6: ( entity_name ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) )
			// JPA2.g:101:8: entity_name ( 'AS' )? identification_variable
			{
			pushFollow(FOLLOW_entity_name_in_range_variable_declaration681);
			entity_name24=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_entity_name.add(entity_name24.getTree());
			// JPA2.g:101:20: ( 'AS' )?
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0==76) ) {
				alt11=1;
			}
			switch (alt11) {
				case 1 :
					// JPA2.g:101:21: 'AS'
					{
					string_literal25=(Token)match(input,76,FOLLOW_76_in_range_variable_declaration684); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_76.add(string_literal25);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_range_variable_declaration688);
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
			// 102:6: -> ^( T_ID_VAR[$identification_variable.text] entity_name )
			{
				// JPA2.g:102:9: ^( T_ID_VAR[$identification_variable.text] entity_name )
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
	// JPA2.g:103:1: join : join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression ) ;
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
		RewriteRuleTokenStream stream_115=new RewriteRuleTokenStream(adaptor,"token 115");
		RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");
		RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");

		try {
			// JPA2.g:104:6: ( join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression ) )
			// JPA2.g:104:8: join_spec join_association_path_expression ( 'AS' )? identification_variable ( 'ON' conditional_expression )?
			{
			pushFollow(FOLLOW_join_spec_in_join717);
			join_spec27=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_spec.add(join_spec27.getTree());
			pushFollow(FOLLOW_join_association_path_expression_in_join719);
			join_association_path_expression28=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression28.getTree());
			// JPA2.g:104:51: ( 'AS' )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==76) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// JPA2.g:104:52: 'AS'
					{
					string_literal29=(Token)match(input,76,FOLLOW_76_in_join722); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_76.add(string_literal29);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_join726);
			identification_variable30=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable30.getTree());
			// JPA2.g:104:83: ( 'ON' conditional_expression )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==115) ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// JPA2.g:104:84: 'ON' conditional_expression
					{
					string_literal31=(Token)match(input,115,FOLLOW_115_in_join729); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_115.add(string_literal31);

					pushFollow(FOLLOW_conditional_expression_in_join731);
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
			// 105:6: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression )
			{
				// JPA2.g:105:9: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text, $conditional_expression.text] join_association_path_expression )
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
	// JPA2.g:106:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
	public final JPA2Parser.fetch_join_return fetch_join() throws RecognitionException {
		JPA2Parser.fetch_join_return retval = new JPA2Parser.fetch_join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal34=null;
		ParserRuleReturnScope join_spec33 =null;
		ParserRuleReturnScope join_association_path_expression35 =null;

		Object string_literal34_tree=null;

		try {
			// JPA2.g:107:6: ( join_spec 'FETCH' join_association_path_expression )
			// JPA2.g:107:8: join_spec 'FETCH' join_association_path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_join_spec_in_fetch_join762);
			join_spec33=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec33.getTree());

			string_literal34=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join764); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal34_tree = (Object)adaptor.create(string_literal34);
			adaptor.addChild(root_0, string_literal34_tree);
			}

			pushFollow(FOLLOW_join_association_path_expression_in_fetch_join766);
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
	// JPA2.g:108:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
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
			// JPA2.g:109:6: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
			// JPA2.g:109:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:109:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
			int alt15=3;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==LEFT) ) {
				alt15=1;
			}
			else if ( (LA15_0==INNER) ) {
				alt15=2;
			}
			switch (alt15) {
				case 1 :
					// JPA2.g:109:9: ( 'LEFT' ) ( 'OUTER' )?
					{
					// JPA2.g:109:9: ( 'LEFT' )
					// JPA2.g:109:10: 'LEFT'
					{
					string_literal36=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec780); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal36_tree = (Object)adaptor.create(string_literal36);
					adaptor.addChild(root_0, string_literal36_tree);
					}

					}

					// JPA2.g:109:18: ( 'OUTER' )?
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0==OUTER) ) {
						alt14=1;
					}
					switch (alt14) {
						case 1 :
							// JPA2.g:109:19: 'OUTER'
							{
							string_literal37=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec784); if (state.failed) return retval;
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
					// JPA2.g:109:31: 'INNER'
					{
					string_literal38=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec790); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal38_tree = (Object)adaptor.create(string_literal38);
					adaptor.addChild(root_0, string_literal38_tree);
					}

					}
					break;

			}

			string_literal39=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec795); if (state.failed) return retval;
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
	// JPA2.g:112:1: join_association_path_expression : ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name );
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
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_125=new RewriteRuleTokenStream(adaptor,"token 125");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_subtype=new RewriteRuleSubtreeStream(adaptor,"rule subtype");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:113:6: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | entity_name )
			int alt20=3;
			int LA20_0 = input.LA(1);
			if ( (LA20_0==WORD) ) {
				int LA20_1 = input.LA(2);
				if ( (LA20_1==58) ) {
					alt20=1;
				}
				else if ( (LA20_1==EOF||(LA20_1 >= GROUP && LA20_1 <= INNER)||(LA20_1 >= JOIN && LA20_1 <= LEFT)||LA20_1==ORDER||LA20_1==RPAREN||LA20_1==WORD||LA20_1==56||LA20_1==76||LA20_1==132) ) {
					alt20=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 20, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA20_0==125) ) {
				alt20=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 20, 0, input);
				throw nvae;
			}

			switch (alt20) {
				case 1 :
					// JPA2.g:113:8: identification_variable '.' ( field '.' )* ( field )?
					{
					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression809);
					identification_variable40=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable40.getTree());
					char_literal41=(Token)match(input,58,FOLLOW_58_in_join_association_path_expression811); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal41);

					// JPA2.g:113:36: ( field '.' )*
					loop16:
					while (true) {
						int alt16=2;
						int LA16_0 = input.LA(1);
						if ( (LA16_0==WORD) ) {
							int LA16_1 = input.LA(2);
							if ( (LA16_1==58) ) {
								alt16=1;
							}

						}
						else if ( (LA16_0==GROUP) ) {
							int LA16_3 = input.LA(2);
							if ( (LA16_3==58) ) {
								alt16=1;
							}

						}

						switch (alt16) {
						case 1 :
							// JPA2.g:113:37: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression814);
							field42=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field42.getTree());
							char_literal43=(Token)match(input,58,FOLLOW_58_in_join_association_path_expression815); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_58.add(char_literal43);

							}
							break;

						default :
							break loop16;
						}
					}

					// JPA2.g:113:48: ( field )?
					int alt17=2;
					int LA17_0 = input.LA(1);
					if ( (LA17_0==WORD) ) {
						int LA17_1 = input.LA(2);
						if ( (synpred18_JPA2()) ) {
							alt17=1;
						}
					}
					else if ( (LA17_0==GROUP) ) {
						int LA17_3 = input.LA(2);
						if ( (LA17_3==EOF||(LA17_3 >= GROUP && LA17_3 <= INNER)||(LA17_3 >= JOIN && LA17_3 <= LEFT)||LA17_3==ORDER||LA17_3==RPAREN||LA17_3==WORD||LA17_3==56||LA17_3==76||LA17_3==132) ) {
							alt17=1;
						}
					}
					switch (alt17) {
						case 1 :
							// JPA2.g:113:48: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression819);
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
					// 114:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:114:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable40!=null?input.toString(identification_variable40.start,identification_variable40.stop):null)), root_1);
						// JPA2.g:114:73: ( field )*
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
					// JPA2.g:115:9: 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')'
					{
					string_literal45=(Token)match(input,125,FOLLOW_125_in_join_association_path_expression854); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_125.add(string_literal45);

					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression856);
					identification_variable46=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable46.getTree());
					char_literal47=(Token)match(input,58,FOLLOW_58_in_join_association_path_expression858); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal47);

					// JPA2.g:115:46: ( field '.' )*
					loop18:
					while (true) {
						int alt18=2;
						int LA18_0 = input.LA(1);
						if ( (LA18_0==GROUP||LA18_0==WORD) ) {
							int LA18_1 = input.LA(2);
							if ( (LA18_1==58) ) {
								alt18=1;
							}

						}

						switch (alt18) {
						case 1 :
							// JPA2.g:115:47: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression861);
							field48=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field48.getTree());
							char_literal49=(Token)match(input,58,FOLLOW_58_in_join_association_path_expression862); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_58.add(char_literal49);

							}
							break;

						default :
							break loop18;
						}
					}

					// JPA2.g:115:58: ( field )?
					int alt19=2;
					int LA19_0 = input.LA(1);
					if ( (LA19_0==GROUP||LA19_0==WORD) ) {
						alt19=1;
					}
					switch (alt19) {
						case 1 :
							// JPA2.g:115:58: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression866);
							field50=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field50.getTree());
							}
							break;

					}

					string_literal51=(Token)match(input,76,FOLLOW_76_in_join_association_path_expression869); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_76.add(string_literal51);

					pushFollow(FOLLOW_subtype_in_join_association_path_expression871);
					subtype52=subtype();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subtype.add(subtype52.getTree());
					char_literal53=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_join_association_path_expression873); if (state.failed) return retval; 
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
					// 116:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// JPA2.g:116:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable46!=null?input.toString(identification_variable46.start,identification_variable46.stop):null)), root_1);
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
				case 3 :
					// JPA2.g:117:8: entity_name
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_name_in_join_association_path_expression906);
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
	// JPA2.g:120:1: collection_member_declaration : 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
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
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_96=new RewriteRuleTokenStream(adaptor,"token 96");
		RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");

		try {
			// JPA2.g:121:5: ( 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
			// JPA2.g:121:7: 'IN' '(' path_expression ')' ( 'AS' )? identification_variable
			{
			string_literal55=(Token)match(input,96,FOLLOW_96_in_collection_member_declaration919); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_96.add(string_literal55);

			char_literal56=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration920); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal56);

			pushFollow(FOLLOW_path_expression_in_collection_member_declaration922);
			path_expression57=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_path_expression.add(path_expression57.getTree());
			char_literal58=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration924); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal58);

			// JPA2.g:121:35: ( 'AS' )?
			int alt21=2;
			int LA21_0 = input.LA(1);
			if ( (LA21_0==76) ) {
				alt21=1;
			}
			switch (alt21) {
				case 1 :
					// JPA2.g:121:36: 'AS'
					{
					string_literal59=(Token)match(input,76,FOLLOW_76_in_collection_member_declaration927); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_76.add(string_literal59);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_collection_member_declaration931);
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
			// 122:5: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
			{
				// JPA2.g:122:8: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
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
	// JPA2.g:124:1: qualified_identification_variable : ( map_field_identification_variable | 'ENTRY(' identification_variable ')' );
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
			// JPA2.g:125:5: ( map_field_identification_variable | 'ENTRY(' identification_variable ')' )
			int alt22=2;
			int LA22_0 = input.LA(1);
			if ( (LA22_0==99||LA22_0==130) ) {
				alt22=1;
			}
			else if ( (LA22_0==90) ) {
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
					// JPA2.g:125:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_qualified_identification_variable960);
					map_field_identification_variable61=map_field_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map_field_identification_variable61.getTree());

					}
					break;
				case 2 :
					// JPA2.g:126:7: 'ENTRY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal62=(Token)match(input,90,FOLLOW_90_in_qualified_identification_variable968); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal62_tree = (Object)adaptor.create(string_literal62);
					adaptor.addChild(root_0, string_literal62_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_qualified_identification_variable969);
					identification_variable63=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable63.getTree());

					char_literal64=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_qualified_identification_variable970); if (state.failed) return retval;
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
	// JPA2.g:127:1: map_field_identification_variable : ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' );
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
			// JPA2.g:127:35: ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' )
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0==99) ) {
				alt23=1;
			}
			else if ( (LA23_0==130) ) {
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
					// JPA2.g:127:37: 'KEY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal65=(Token)match(input,99,FOLLOW_99_in_map_field_identification_variable977); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal65_tree = (Object)adaptor.create(string_literal65);
					adaptor.addChild(root_0, string_literal65_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable978);
					identification_variable66=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable66.getTree());

					char_literal67=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable979); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal67_tree = (Object)adaptor.create(char_literal67);
					adaptor.addChild(root_0, char_literal67_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:127:72: 'VALUE(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal68=(Token)match(input,130,FOLLOW_130_in_map_field_identification_variable983); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal68_tree = (Object)adaptor.create(string_literal68);
					adaptor.addChild(root_0, string_literal68_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable984);
					identification_variable69=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable69.getTree());

					char_literal70=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable985); if (state.failed) return retval;
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
	// JPA2.g:130:1: path_expression : identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
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
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:131:5: ( identification_variable '.' ( field '.' )* ( field )? -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
			// JPA2.g:131:8: identification_variable '.' ( field '.' )* ( field )?
			{
			pushFollow(FOLLOW_identification_variable_in_path_expression999);
			identification_variable71=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable71.getTree());
			char_literal72=(Token)match(input,58,FOLLOW_58_in_path_expression1001); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_58.add(char_literal72);

			// JPA2.g:131:36: ( field '.' )*
			loop24:
			while (true) {
				int alt24=2;
				int LA24_0 = input.LA(1);
				if ( (LA24_0==WORD) ) {
					int LA24_1 = input.LA(2);
					if ( (LA24_1==58) ) {
						alt24=1;
					}

				}
				else if ( (LA24_0==GROUP) ) {
					int LA24_3 = input.LA(2);
					if ( (LA24_3==58) ) {
						alt24=1;
					}

				}

				switch (alt24) {
				case 1 :
					// JPA2.g:131:37: field '.'
					{
					pushFollow(FOLLOW_field_in_path_expression1004);
					field73=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field73.getTree());
					char_literal74=(Token)match(input,58,FOLLOW_58_in_path_expression1005); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(char_literal74);

					}
					break;

				default :
					break loop24;
				}
			}

			// JPA2.g:131:48: ( field )?
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==WORD) ) {
				int LA25_1 = input.LA(2);
				if ( (synpred27_JPA2()) ) {
					alt25=1;
				}
			}
			else if ( (LA25_0==GROUP) ) {
				int LA25_3 = input.LA(2);
				if ( (LA25_3==EOF||(LA25_3 >= AND && LA25_3 <= ASC)||LA25_3==DESC||(LA25_3 >= GROUP && LA25_3 <= INNER)||(LA25_3 >= JOIN && LA25_3 <= LEFT)||(LA25_3 >= OR && LA25_3 <= ORDER)||LA25_3==RPAREN||LA25_3==WORD||(LA25_3 >= 54 && LA25_3 <= 57)||LA25_3==59||(LA25_3 >= 61 && LA25_3 <= 66)||(LA25_3 >= 76 && LA25_3 <= 77)||LA25_3==87||LA25_3==89||LA25_3==93||LA25_3==96||LA25_3==98||LA25_3==102||LA25_3==104||LA25_3==109||LA25_3==123||(LA25_3 >= 131 && LA25_3 <= 132)) ) {
					alt25=1;
				}
			}
			switch (alt25) {
				case 1 :
					// JPA2.g:131:48: field
					{
					pushFollow(FOLLOW_field_in_path_expression1009);
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
			// 132:5: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
			{
				// JPA2.g:132:8: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable71!=null?input.toString(identification_variable71.start,identification_variable71.stop):null)), root_1);
				// JPA2.g:132:68: ( field )*
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
	// JPA2.g:137:1: general_identification_variable : ( identification_variable | map_field_identification_variable );
	public final JPA2Parser.general_identification_variable_return general_identification_variable() throws RecognitionException {
		JPA2Parser.general_identification_variable_return retval = new JPA2Parser.general_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable76 =null;
		ParserRuleReturnScope map_field_identification_variable77 =null;


		try {
			// JPA2.g:138:5: ( identification_variable | map_field_identification_variable )
			int alt26=2;
			int LA26_0 = input.LA(1);
			if ( (LA26_0==WORD) ) {
				alt26=1;
			}
			else if ( (LA26_0==99||LA26_0==130) ) {
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
					// JPA2.g:138:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_general_identification_variable1048);
					identification_variable76=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable76.getTree());

					}
					break;
				case 2 :
					// JPA2.g:139:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_general_identification_variable1056);
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
	// JPA2.g:140:1: update_clause : entity_name ( ( 'AS' )? identification_variable )? 'SET' update_item ( ',' update_item )* ;
	public final JPA2Parser.update_clause_return update_clause() throws RecognitionException {
		JPA2Parser.update_clause_return retval = new JPA2Parser.update_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal79=null;
		Token string_literal81=null;
		Token char_literal83=null;
		ParserRuleReturnScope entity_name78 =null;
		ParserRuleReturnScope identification_variable80 =null;
		ParserRuleReturnScope update_item82 =null;
		ParserRuleReturnScope update_item84 =null;

		Object string_literal79_tree=null;
		Object string_literal81_tree=null;
		Object char_literal83_tree=null;

		try {
			// JPA2.g:141:5: ( entity_name ( ( 'AS' )? identification_variable )? 'SET' update_item ( ',' update_item )* )
			// JPA2.g:141:7: entity_name ( ( 'AS' )? identification_variable )? 'SET' update_item ( ',' update_item )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_name_in_update_clause1067);
			entity_name78=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_name78.getTree());

			// JPA2.g:141:19: ( ( 'AS' )? identification_variable )?
			int alt28=2;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==WORD||LA28_0==76) ) {
				alt28=1;
			}
			switch (alt28) {
				case 1 :
					// JPA2.g:141:20: ( 'AS' )? identification_variable
					{
					// JPA2.g:141:20: ( 'AS' )?
					int alt27=2;
					int LA27_0 = input.LA(1);
					if ( (LA27_0==76) ) {
						alt27=1;
					}
					switch (alt27) {
						case 1 :
							// JPA2.g:141:21: 'AS'
							{
							string_literal79=(Token)match(input,76,FOLLOW_76_in_update_clause1071); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal79_tree = (Object)adaptor.create(string_literal79);
							adaptor.addChild(root_0, string_literal79_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_identification_variable_in_update_clause1075);
					identification_variable80=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable80.getTree());

					}
					break;

			}

			string_literal81=(Token)match(input,118,FOLLOW_118_in_update_clause1079); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal81_tree = (Object)adaptor.create(string_literal81);
			adaptor.addChild(root_0, string_literal81_tree);
			}

			pushFollow(FOLLOW_update_item_in_update_clause1081);
			update_item82=update_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, update_item82.getTree());

			// JPA2.g:141:72: ( ',' update_item )*
			loop29:
			while (true) {
				int alt29=2;
				int LA29_0 = input.LA(1);
				if ( (LA29_0==56) ) {
					alt29=1;
				}

				switch (alt29) {
				case 1 :
					// JPA2.g:141:73: ',' update_item
					{
					char_literal83=(Token)match(input,56,FOLLOW_56_in_update_clause1084); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal83_tree = (Object)adaptor.create(char_literal83);
					adaptor.addChild(root_0, char_literal83_tree);
					}

					pushFollow(FOLLOW_update_item_in_update_clause1086);
					update_item84=update_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, update_item84.getTree());

					}
					break;

				default :
					break loop29;
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
	// $ANTLR end "update_clause"


	public static class update_item_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "update_item"
	// JPA2.g:142:1: update_item : ( identification_variable '.' ) ( single_valued_embeddable_object_field '.' )* single_valued_object_field '=' new_value ;
	public final JPA2Parser.update_item_return update_item() throws RecognitionException {
		JPA2Parser.update_item_return retval = new JPA2Parser.update_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal86=null;
		Token char_literal88=null;
		Token char_literal90=null;
		ParserRuleReturnScope identification_variable85 =null;
		ParserRuleReturnScope single_valued_embeddable_object_field87 =null;
		ParserRuleReturnScope single_valued_object_field89 =null;
		ParserRuleReturnScope new_value91 =null;

		Object char_literal86_tree=null;
		Object char_literal88_tree=null;
		Object char_literal90_tree=null;

		try {
			// JPA2.g:143:5: ( ( identification_variable '.' ) ( single_valued_embeddable_object_field '.' )* single_valued_object_field '=' new_value )
			// JPA2.g:143:7: ( identification_variable '.' ) ( single_valued_embeddable_object_field '.' )* single_valued_object_field '=' new_value
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:143:7: ( identification_variable '.' )
			// JPA2.g:143:8: identification_variable '.'
			{
			pushFollow(FOLLOW_identification_variable_in_update_item1100);
			identification_variable85=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable85.getTree());

			char_literal86=(Token)match(input,58,FOLLOW_58_in_update_item1101); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal86_tree = (Object)adaptor.create(char_literal86);
			adaptor.addChild(root_0, char_literal86_tree);
			}

			}

			// JPA2.g:143:35: ( single_valued_embeddable_object_field '.' )*
			loop30:
			while (true) {
				int alt30=2;
				int LA30_0 = input.LA(1);
				if ( (LA30_0==WORD) ) {
					int LA30_1 = input.LA(2);
					if ( (LA30_1==58) ) {
						alt30=1;
					}

				}

				switch (alt30) {
				case 1 :
					// JPA2.g:143:36: single_valued_embeddable_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_embeddable_object_field_in_update_item1104);
					single_valued_embeddable_object_field87=single_valued_embeddable_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_embeddable_object_field87.getTree());

					char_literal88=(Token)match(input,58,FOLLOW_58_in_update_item1105); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal88_tree = (Object)adaptor.create(char_literal88);
					adaptor.addChild(root_0, char_literal88_tree);
					}

					}
					break;

				default :
					break loop30;
				}
			}

			pushFollow(FOLLOW_single_valued_object_field_in_update_item1108);
			single_valued_object_field89=single_valued_object_field();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field89.getTree());

			char_literal90=(Token)match(input,64,FOLLOW_64_in_update_item1110); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal90_tree = (Object)adaptor.create(char_literal90);
			adaptor.addChild(root_0, char_literal90_tree);
			}

			pushFollow(FOLLOW_new_value_in_update_item1112);
			new_value91=new_value();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, new_value91.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:144:1: new_value : ( scalar_expression | simple_entity_expression | 'NULL' );
	public final JPA2Parser.new_value_return new_value() throws RecognitionException {
		JPA2Parser.new_value_return retval = new JPA2Parser.new_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal94=null;
		ParserRuleReturnScope scalar_expression92 =null;
		ParserRuleReturnScope simple_entity_expression93 =null;

		Object string_literal94_tree=null;

		try {
			// JPA2.g:145:5: ( scalar_expression | simple_entity_expression | 'NULL' )
			int alt31=3;
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
			case 55:
			case 57:
			case 60:
			case 73:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 84:
			case 94:
			case 97:
			case 101:
			case 103:
			case 106:
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
				alt31=1;
				}
				break;
			case WORD:
				{
				int LA31_2 = input.LA(2);
				if ( (synpred33_JPA2()) ) {
					alt31=1;
				}
				else if ( (synpred34_JPA2()) ) {
					alt31=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 31, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 67:
				{
				int LA31_3 = input.LA(2);
				if ( (LA31_3==60) ) {
					int LA31_8 = input.LA(3);
					if ( (LA31_8==INT_NUMERAL) ) {
						int LA31_9 = input.LA(4);
						if ( (synpred33_JPA2()) ) {
							alt31=1;
						}
						else if ( (synpred34_JPA2()) ) {
							alt31=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 31, 9, input);
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
								new NoViableAltException("", 31, 8, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA31_3==INT_NUMERAL) ) {
					int LA31_9 = input.LA(3);
					if ( (synpred33_JPA2()) ) {
						alt31=1;
					}
					else if ( (synpred34_JPA2()) ) {
						alt31=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 31, 9, input);
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
							new NoViableAltException("", 31, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA31_4 = input.LA(2);
				if ( (synpred33_JPA2()) ) {
					alt31=1;
				}
				else if ( (synpred34_JPA2()) ) {
					alt31=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 31, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 53:
				{
				int LA31_5 = input.LA(2);
				if ( (LA31_5==WORD) ) {
					int LA31_10 = input.LA(3);
					if ( (LA31_10==136) ) {
						int LA31_11 = input.LA(4);
						if ( (synpred33_JPA2()) ) {
							alt31=1;
						}
						else if ( (synpred34_JPA2()) ) {
							alt31=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 31, 11, input);
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
								new NoViableAltException("", 31, 10, input);
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
							new NoViableAltException("", 31, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 111:
				{
				alt31=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 31, 0, input);
				throw nvae;
			}
			switch (alt31) {
				case 1 :
					// JPA2.g:145:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_new_value1123);
					scalar_expression92=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression92.getTree());

					}
					break;
				case 2 :
					// JPA2.g:146:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_new_value1131);
					simple_entity_expression93=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression93.getTree());

					}
					break;
				case 3 :
					// JPA2.g:147:7: 'NULL'
					{
					root_0 = (Object)adaptor.nil();


					string_literal94=(Token)match(input,111,FOLLOW_111_in_new_value1139); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal94_tree = (Object)adaptor.create(string_literal94);
					adaptor.addChild(root_0, string_literal94_tree);
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
	// JPA2.g:148:1: delete_clause : entity_name ( ( 'AS' )? identification_variable )? ;
	public final JPA2Parser.delete_clause_return delete_clause() throws RecognitionException {
		JPA2Parser.delete_clause_return retval = new JPA2Parser.delete_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal96=null;
		ParserRuleReturnScope entity_name95 =null;
		ParserRuleReturnScope identification_variable97 =null;

		Object string_literal96_tree=null;

		try {
			// JPA2.g:149:5: ( entity_name ( ( 'AS' )? identification_variable )? )
			// JPA2.g:149:7: entity_name ( ( 'AS' )? identification_variable )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_name_in_delete_clause1150);
			entity_name95=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_name95.getTree());

			// JPA2.g:149:19: ( ( 'AS' )? identification_variable )?
			int alt33=2;
			int LA33_0 = input.LA(1);
			if ( (LA33_0==WORD||LA33_0==76) ) {
				alt33=1;
			}
			switch (alt33) {
				case 1 :
					// JPA2.g:149:20: ( 'AS' )? identification_variable
					{
					// JPA2.g:149:20: ( 'AS' )?
					int alt32=2;
					int LA32_0 = input.LA(1);
					if ( (LA32_0==76) ) {
						alt32=1;
					}
					switch (alt32) {
						case 1 :
							// JPA2.g:149:21: 'AS'
							{
							string_literal96=(Token)match(input,76,FOLLOW_76_in_delete_clause1154); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal96_tree = (Object)adaptor.create(string_literal96);
							adaptor.addChild(root_0, string_literal96_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_identification_variable_in_delete_clause1158);
					identification_variable97=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable97.getTree());

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
	// $ANTLR end "delete_clause"


	public static class select_clause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "select_clause"
	// JPA2.g:150:1: select_clause : ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) ;
	public final JPA2Parser.select_clause_return select_clause() throws RecognitionException {
		JPA2Parser.select_clause_return retval = new JPA2Parser.select_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal98=null;
		Token char_literal100=null;
		ParserRuleReturnScope select_item99 =null;
		ParserRuleReturnScope select_item101 =null;

		Object string_literal98_tree=null;
		Object char_literal100_tree=null;
		RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleSubtreeStream stream_select_item=new RewriteRuleSubtreeStream(adaptor,"rule select_item");

		try {
			// JPA2.g:151:5: ( ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) )
			// JPA2.g:151:7: ( 'DISTINCT' )? select_item ( ',' select_item )*
			{
			// JPA2.g:151:7: ( 'DISTINCT' )?
			int alt34=2;
			int LA34_0 = input.LA(1);
			if ( (LA34_0==DISTINCT) ) {
				alt34=1;
			}
			switch (alt34) {
				case 1 :
					// JPA2.g:151:8: 'DISTINCT'
					{
					string_literal98=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause1172); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal98);

					}
					break;

			}

			pushFollow(FOLLOW_select_item_in_select_clause1176);
			select_item99=select_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_item.add(select_item99.getTree());
			// JPA2.g:151:33: ( ',' select_item )*
			loop35:
			while (true) {
				int alt35=2;
				int LA35_0 = input.LA(1);
				if ( (LA35_0==56) ) {
					alt35=1;
				}

				switch (alt35) {
				case 1 :
					// JPA2.g:151:34: ',' select_item
					{
					char_literal100=(Token)match(input,56,FOLLOW_56_in_select_clause1179); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_56.add(char_literal100);

					pushFollow(FOLLOW_select_item_in_select_clause1181);
					select_item101=select_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_select_item.add(select_item101.getTree());
					}
					break;

				default :
					break loop35;
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
			// 152:5: -> ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
			{
				// JPA2.g:152:8: ^( T_SELECTED_ITEMS[] ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:152:48: ( 'DISTINCT' )?
				if ( stream_DISTINCT.hasNext() ) {
					adaptor.addChild(root_1, stream_DISTINCT.nextNode());
				}
				stream_DISTINCT.reset();

				// JPA2.g:152:62: ( ^( T_SELECTED_ITEM[] select_item ) )*
				while ( stream_select_item.hasNext() ) {
					// JPA2.g:152:62: ^( T_SELECTED_ITEM[] select_item )
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
	// JPA2.g:153:1: select_item : select_expression ( ( 'AS' )? result_variable )? ;
	public final JPA2Parser.select_item_return select_item() throws RecognitionException {
		JPA2Parser.select_item_return retval = new JPA2Parser.select_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal103=null;
		ParserRuleReturnScope select_expression102 =null;
		ParserRuleReturnScope result_variable104 =null;

		Object string_literal103_tree=null;

		try {
			// JPA2.g:154:5: ( select_expression ( ( 'AS' )? result_variable )? )
			// JPA2.g:154:7: select_expression ( ( 'AS' )? result_variable )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_select_expression_in_select_item1224);
			select_expression102=select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, select_expression102.getTree());

			// JPA2.g:154:25: ( ( 'AS' )? result_variable )?
			int alt37=2;
			int LA37_0 = input.LA(1);
			if ( (LA37_0==WORD||LA37_0==76) ) {
				alt37=1;
			}
			switch (alt37) {
				case 1 :
					// JPA2.g:154:26: ( 'AS' )? result_variable
					{
					// JPA2.g:154:26: ( 'AS' )?
					int alt36=2;
					int LA36_0 = input.LA(1);
					if ( (LA36_0==76) ) {
						alt36=1;
					}
					switch (alt36) {
						case 1 :
							// JPA2.g:154:27: 'AS'
							{
							string_literal103=(Token)match(input,76,FOLLOW_76_in_select_item1228); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal103_tree = (Object)adaptor.create(string_literal103);
							adaptor.addChild(root_0, string_literal103_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_result_variable_in_select_item1232);
					result_variable104=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable104.getTree());

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
	// JPA2.g:155:1: select_expression : ( path_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression );
	public final JPA2Parser.select_expression_return select_expression() throws RecognitionException {
		JPA2Parser.select_expression_return retval = new JPA2Parser.select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal109=null;
		Token char_literal110=null;
		Token char_literal112=null;
		ParserRuleReturnScope path_expression105 =null;
		ParserRuleReturnScope identification_variable106 =null;
		ParserRuleReturnScope scalar_expression107 =null;
		ParserRuleReturnScope aggregate_expression108 =null;
		ParserRuleReturnScope identification_variable111 =null;
		ParserRuleReturnScope constructor_expression113 =null;

		Object string_literal109_tree=null;
		Object char_literal110_tree=null;
		Object char_literal112_tree=null;
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");

		try {
			// JPA2.g:156:5: ( path_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression )
			int alt38=6;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA38_1 = input.LA(2);
				if ( (synpred41_JPA2()) ) {
					alt38=1;
				}
				else if ( (synpred42_JPA2()) ) {
					alt38=2;
				}
				else if ( (synpred43_JPA2()) ) {
					alt38=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 38, 1, input);
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
			case 53:
			case 55:
			case 57:
			case 60:
			case 67:
			case 73:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 84:
			case 97:
			case 101:
			case 103:
			case 106:
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
				alt38=3;
				}
				break;
			case COUNT:
				{
				int LA38_16 = input.LA(2);
				if ( (synpred43_JPA2()) ) {
					alt38=3;
				}
				else if ( (synpred44_JPA2()) ) {
					alt38=4;
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
				if ( (synpred43_JPA2()) ) {
					alt38=3;
				}
				else if ( (synpred44_JPA2()) ) {
					alt38=4;
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
			case 94:
				{
				int LA38_18 = input.LA(2);
				if ( (synpred43_JPA2()) ) {
					alt38=3;
				}
				else if ( (synpred44_JPA2()) ) {
					alt38=4;
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
			case 113:
				{
				alt38=5;
				}
				break;
			case 108:
				{
				alt38=6;
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
					// JPA2.g:156:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_select_expression1245);
					path_expression105=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression105.getTree());

					}
					break;
				case 2 :
					// JPA2.g:157:7: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_select_expression1253);
					identification_variable106=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable106.getTree());
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
					// 157:31: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
					{
						// JPA2.g:157:34: ^( T_SELECTED_ENTITY[$identification_variable.text] )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_ENTITY, (identification_variable106!=null?input.toString(identification_variable106.start,identification_variable106.stop):null)), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// JPA2.g:158:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_select_expression1271);
					scalar_expression107=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression107.getTree());

					}
					break;
				case 4 :
					// JPA2.g:159:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_select_expression1279);
					aggregate_expression108=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression108.getTree());

					}
					break;
				case 5 :
					// JPA2.g:160:7: 'OBJECT' '(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal109=(Token)match(input,113,FOLLOW_113_in_select_expression1287); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal109_tree = (Object)adaptor.create(string_literal109);
					adaptor.addChild(root_0, string_literal109_tree);
					}

					char_literal110=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression1289); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal110_tree = (Object)adaptor.create(char_literal110);
					adaptor.addChild(root_0, char_literal110_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_select_expression1290);
					identification_variable111=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable111.getTree());

					char_literal112=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression1291); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal112_tree = (Object)adaptor.create(char_literal112);
					adaptor.addChild(root_0, char_literal112_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:161:7: constructor_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_constructor_expression_in_select_expression1299);
					constructor_expression113=constructor_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_expression113.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:162:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
	public final JPA2Parser.constructor_expression_return constructor_expression() throws RecognitionException {
		JPA2Parser.constructor_expression_return retval = new JPA2Parser.constructor_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal114=null;
		Token char_literal116=null;
		Token char_literal118=null;
		Token char_literal120=null;
		ParserRuleReturnScope constructor_name115 =null;
		ParserRuleReturnScope constructor_item117 =null;
		ParserRuleReturnScope constructor_item119 =null;

		Object string_literal114_tree=null;
		Object char_literal116_tree=null;
		Object char_literal118_tree=null;
		Object char_literal120_tree=null;

		try {
			// JPA2.g:163:5: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
			// JPA2.g:163:7: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal114=(Token)match(input,108,FOLLOW_108_in_constructor_expression1310); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal114_tree = (Object)adaptor.create(string_literal114);
			adaptor.addChild(root_0, string_literal114_tree);
			}

			pushFollow(FOLLOW_constructor_name_in_constructor_expression1312);
			constructor_name115=constructor_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name115.getTree());

			char_literal116=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression1314); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal116_tree = (Object)adaptor.create(char_literal116);
			adaptor.addChild(root_0, char_literal116_tree);
			}

			pushFollow(FOLLOW_constructor_item_in_constructor_expression1316);
			constructor_item117=constructor_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item117.getTree());

			// JPA2.g:163:51: ( ',' constructor_item )*
			loop39:
			while (true) {
				int alt39=2;
				int LA39_0 = input.LA(1);
				if ( (LA39_0==56) ) {
					alt39=1;
				}

				switch (alt39) {
				case 1 :
					// JPA2.g:163:52: ',' constructor_item
					{
					char_literal118=(Token)match(input,56,FOLLOW_56_in_constructor_expression1319); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal118_tree = (Object)adaptor.create(char_literal118);
					adaptor.addChild(root_0, char_literal118_tree);
					}

					pushFollow(FOLLOW_constructor_item_in_constructor_expression1321);
					constructor_item119=constructor_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item119.getTree());

					}
					break;

				default :
					break loop39;
				}
			}

			char_literal120=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression1325); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal120_tree = (Object)adaptor.create(char_literal120);
			adaptor.addChild(root_0, char_literal120_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:164:1: constructor_item : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.constructor_item_return constructor_item() throws RecognitionException {
		JPA2Parser.constructor_item_return retval = new JPA2Parser.constructor_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression121 =null;
		ParserRuleReturnScope scalar_expression122 =null;
		ParserRuleReturnScope aggregate_expression123 =null;
		ParserRuleReturnScope identification_variable124 =null;


		try {
			// JPA2.g:165:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt40=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA40_1 = input.LA(2);
				if ( (synpred47_JPA2()) ) {
					alt40=1;
				}
				else if ( (synpred48_JPA2()) ) {
					alt40=2;
				}
				else if ( (true) ) {
					alt40=4;
				}

				}
				break;
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 53:
			case 55:
			case 57:
			case 60:
			case 67:
			case 73:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 84:
			case 97:
			case 101:
			case 103:
			case 106:
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
				alt40=2;
				}
				break;
			case COUNT:
				{
				int LA40_16 = input.LA(2);
				if ( (synpred48_JPA2()) ) {
					alt40=2;
				}
				else if ( (synpred49_JPA2()) ) {
					alt40=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 40, 16, input);
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
				int LA40_17 = input.LA(2);
				if ( (synpred48_JPA2()) ) {
					alt40=2;
				}
				else if ( (synpred49_JPA2()) ) {
					alt40=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 40, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 94:
				{
				int LA40_18 = input.LA(2);
				if ( (synpred48_JPA2()) ) {
					alt40=2;
				}
				else if ( (synpred49_JPA2()) ) {
					alt40=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 40, 18, input);
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
					new NoViableAltException("", 40, 0, input);
				throw nvae;
			}
			switch (alt40) {
				case 1 :
					// JPA2.g:165:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_constructor_item1336);
					path_expression121=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression121.getTree());

					}
					break;
				case 2 :
					// JPA2.g:166:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_constructor_item1344);
					scalar_expression122=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression122.getTree());

					}
					break;
				case 3 :
					// JPA2.g:167:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_constructor_item1352);
					aggregate_expression123=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression123.getTree());

					}
					break;
				case 4 :
					// JPA2.g:168:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_constructor_item1360);
					identification_variable124=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable124.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:169:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );
	public final JPA2Parser.aggregate_expression_return aggregate_expression() throws RecognitionException {
		JPA2Parser.aggregate_expression_return retval = new JPA2Parser.aggregate_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal126=null;
		Token DISTINCT127=null;
		Token char_literal129=null;
		Token string_literal130=null;
		Token char_literal131=null;
		Token DISTINCT132=null;
		Token char_literal134=null;
		ParserRuleReturnScope aggregate_expression_function_name125 =null;
		ParserRuleReturnScope path_expression128 =null;
		ParserRuleReturnScope count_argument133 =null;
		ParserRuleReturnScope function_invocation135 =null;

		Object char_literal126_tree=null;
		Object DISTINCT127_tree=null;
		Object char_literal129_tree=null;
		Object string_literal130_tree=null;
		Object char_literal131_tree=null;
		Object DISTINCT132_tree=null;
		Object char_literal134_tree=null;
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_COUNT=new RewriteRuleTokenStream(adaptor,"token COUNT");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_count_argument=new RewriteRuleSubtreeStream(adaptor,"rule count_argument");
		RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
		RewriteRuleSubtreeStream stream_aggregate_expression_function_name=new RewriteRuleSubtreeStream(adaptor,"rule aggregate_expression_function_name");

		try {
			// JPA2.g:170:5: ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation )
			int alt43=3;
			alt43 = dfa43.predict(input);
			switch (alt43) {
				case 1 :
					// JPA2.g:170:7: aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')'
					{
					pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1371);
					aggregate_expression_function_name125=aggregate_expression_function_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name125.getTree());
					char_literal126=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1373); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal126);

					// JPA2.g:170:45: ( DISTINCT )?
					int alt41=2;
					int LA41_0 = input.LA(1);
					if ( (LA41_0==DISTINCT) ) {
						alt41=1;
					}
					switch (alt41) {
						case 1 :
							// JPA2.g:170:46: DISTINCT
							{
							DISTINCT127=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1375); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT127);

							}
							break;

					}

					pushFollow(FOLLOW_path_expression_in_aggregate_expression1379);
					path_expression128=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_path_expression.add(path_expression128.getTree());
					char_literal129=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1380); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal129);

					// AST REWRITE
					// elements: LPAREN, aggregate_expression_function_name, DISTINCT, path_expression, RPAREN
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 171:5: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
					{
						// JPA2.g:171:8: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:171:93: ( 'DISTINCT' )?
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
					// JPA2.g:172:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
					{
					string_literal130=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1414); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COUNT.add(string_literal130);

					char_literal131=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1416); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal131);

					// JPA2.g:172:18: ( DISTINCT )?
					int alt42=2;
					int LA42_0 = input.LA(1);
					if ( (LA42_0==DISTINCT) ) {
						alt42=1;
					}
					switch (alt42) {
						case 1 :
							// JPA2.g:172:19: DISTINCT
							{
							DISTINCT132=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1418); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT132);

							}
							break;

					}

					pushFollow(FOLLOW_count_argument_in_aggregate_expression1422);
					count_argument133=count_argument();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_count_argument.add(count_argument133.getTree());
					char_literal134=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1424); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal134);

					// AST REWRITE
					// elements: LPAREN, RPAREN, DISTINCT, COUNT, count_argument
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 173:5: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
					{
						// JPA2.g:173:8: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_COUNT.nextNode());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// JPA2.g:173:66: ( 'DISTINCT' )?
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
					// JPA2.g:174:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_aggregate_expression1459);
					function_invocation135=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation135.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:175:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
	public final JPA2Parser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
		JPA2Parser.aggregate_expression_function_name_return retval = new JPA2Parser.aggregate_expression_function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set136=null;

		Object set136_tree=null;

		try {
			// JPA2.g:176:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set136=input.LT(1);
			if ( input.LA(1)==AVG||input.LA(1)==COUNT||(input.LA(1) >= MAX && input.LA(1) <= MIN)||input.LA(1)==SUM ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set136));
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
	// JPA2.g:177:1: count_argument : ( identification_variable | path_expression );
	public final JPA2Parser.count_argument_return count_argument() throws RecognitionException {
		JPA2Parser.count_argument_return retval = new JPA2Parser.count_argument_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable137 =null;
		ParserRuleReturnScope path_expression138 =null;


		try {
			// JPA2.g:178:5: ( identification_variable | path_expression )
			int alt44=2;
			int LA44_0 = input.LA(1);
			if ( (LA44_0==WORD) ) {
				int LA44_1 = input.LA(2);
				if ( (LA44_1==RPAREN) ) {
					alt44=1;
				}
				else if ( (LA44_1==58) ) {
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

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 44, 0, input);
				throw nvae;
			}

			switch (alt44) {
				case 1 :
					// JPA2.g:178:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_count_argument1496);
					identification_variable137=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable137.getTree());

					}
					break;
				case 2 :
					// JPA2.g:178:33: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_count_argument1500);
					path_expression138=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression138.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:179:1: where_clause : wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) ;
	public final JPA2Parser.where_clause_return where_clause() throws RecognitionException {
		JPA2Parser.where_clause_return retval = new JPA2Parser.where_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token wh=null;
		ParserRuleReturnScope conditional_expression139 =null;

		Object wh_tree=null;
		RewriteRuleTokenStream stream_132=new RewriteRuleTokenStream(adaptor,"token 132");
		RewriteRuleSubtreeStream stream_conditional_expression=new RewriteRuleSubtreeStream(adaptor,"rule conditional_expression");

		try {
			// JPA2.g:180:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) )
			// JPA2.g:180:7: wh= 'WHERE' conditional_expression
			{
			wh=(Token)match(input,132,FOLLOW_132_in_where_clause1513); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_132.add(wh);

			pushFollow(FOLLOW_conditional_expression_in_where_clause1515);
			conditional_expression139=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_conditional_expression.add(conditional_expression139.getTree());
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
			// 180:40: -> ^( T_CONDITION[$wh] conditional_expression )
			{
				// JPA2.g:180:43: ^( T_CONDITION[$wh] conditional_expression )
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
	// JPA2.g:181:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
	public final JPA2Parser.groupby_clause_return groupby_clause() throws RecognitionException {
		JPA2Parser.groupby_clause_return retval = new JPA2Parser.groupby_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal140=null;
		Token string_literal141=null;
		Token char_literal143=null;
		ParserRuleReturnScope groupby_item142 =null;
		ParserRuleReturnScope groupby_item144 =null;

		Object string_literal140_tree=null;
		Object string_literal141_tree=null;
		Object char_literal143_tree=null;
		RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
		RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");

		try {
			// JPA2.g:182:5: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
			// JPA2.g:182:7: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
			{
			string_literal140=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1537); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_GROUP.add(string_literal140);

			string_literal141=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1539); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal141);

			pushFollow(FOLLOW_groupby_item_in_groupby_clause1541);
			groupby_item142=groupby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item142.getTree());
			// JPA2.g:182:33: ( ',' groupby_item )*
			loop45:
			while (true) {
				int alt45=2;
				int LA45_0 = input.LA(1);
				if ( (LA45_0==56) ) {
					alt45=1;
				}

				switch (alt45) {
				case 1 :
					// JPA2.g:182:34: ',' groupby_item
					{
					char_literal143=(Token)match(input,56,FOLLOW_56_in_groupby_clause1544); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_56.add(char_literal143);

					pushFollow(FOLLOW_groupby_item_in_groupby_clause1546);
					groupby_item144=groupby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item144.getTree());
					}
					break;

				default :
					break loop45;
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
			// 183:5: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
			{
				// JPA2.g:183:8: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new GroupByNode(T_GROUP_BY), root_1);
				adaptor.addChild(root_1, stream_GROUP.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:183:49: ( groupby_item )*
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
	// JPA2.g:184:1: groupby_item : ( path_expression | identification_variable );
	public final JPA2Parser.groupby_item_return groupby_item() throws RecognitionException {
		JPA2Parser.groupby_item_return retval = new JPA2Parser.groupby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression145 =null;
		ParserRuleReturnScope identification_variable146 =null;


		try {
			// JPA2.g:185:5: ( path_expression | identification_variable )
			int alt46=2;
			int LA46_0 = input.LA(1);
			if ( (LA46_0==WORD) ) {
				int LA46_1 = input.LA(2);
				if ( (LA46_1==58) ) {
					alt46=1;
				}
				else if ( (LA46_1==EOF||LA46_1==HAVING||LA46_1==ORDER||LA46_1==RPAREN||LA46_1==56) ) {
					alt46=2;
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

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 46, 0, input);
				throw nvae;
			}

			switch (alt46) {
				case 1 :
					// JPA2.g:185:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_groupby_item1580);
					path_expression145=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression145.getTree());

					}
					break;
				case 2 :
					// JPA2.g:185:25: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_groupby_item1584);
					identification_variable146=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable146.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:186:1: having_clause : 'HAVING' conditional_expression ;
	public final JPA2Parser.having_clause_return having_clause() throws RecognitionException {
		JPA2Parser.having_clause_return retval = new JPA2Parser.having_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal147=null;
		ParserRuleReturnScope conditional_expression148 =null;

		Object string_literal147_tree=null;

		try {
			// JPA2.g:187:5: ( 'HAVING' conditional_expression )
			// JPA2.g:187:7: 'HAVING' conditional_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal147=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1595); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal147_tree = (Object)adaptor.create(string_literal147);
			adaptor.addChild(root_0, string_literal147_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_having_clause1597);
			conditional_expression148=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression148.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:188:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
	public final JPA2Parser.orderby_clause_return orderby_clause() throws RecognitionException {
		JPA2Parser.orderby_clause_return retval = new JPA2Parser.orderby_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal149=null;
		Token string_literal150=null;
		Token char_literal152=null;
		ParserRuleReturnScope orderby_item151 =null;
		ParserRuleReturnScope orderby_item153 =null;

		Object string_literal149_tree=null;
		Object string_literal150_tree=null;
		Object char_literal152_tree=null;
		RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
		RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");

		try {
			// JPA2.g:189:5: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
			// JPA2.g:189:7: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
			{
			string_literal149=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1608); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ORDER.add(string_literal149);

			string_literal150=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1610); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal150);

			pushFollow(FOLLOW_orderby_item_in_orderby_clause1612);
			orderby_item151=orderby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item151.getTree());
			// JPA2.g:189:33: ( ',' orderby_item )*
			loop47:
			while (true) {
				int alt47=2;
				int LA47_0 = input.LA(1);
				if ( (LA47_0==56) ) {
					alt47=1;
				}

				switch (alt47) {
				case 1 :
					// JPA2.g:189:34: ',' orderby_item
					{
					char_literal152=(Token)match(input,56,FOLLOW_56_in_orderby_clause1615); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_56.add(char_literal152);

					pushFollow(FOLLOW_orderby_item_in_orderby_clause1617);
					orderby_item153=orderby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item153.getTree());
					}
					break;

				default :
					break loop47;
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
			// 190:5: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
			{
				// JPA2.g:190:8: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByNode(T_ORDER_BY), root_1);
				adaptor.addChild(root_1, stream_ORDER.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// JPA2.g:190:49: ( orderby_item )*
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
	// JPA2.g:191:1: orderby_item : ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) );
	public final JPA2Parser.orderby_item_return orderby_item() throws RecognitionException {
		JPA2Parser.orderby_item_return retval = new JPA2Parser.orderby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal155=null;
		Token string_literal157=null;
		ParserRuleReturnScope orderby_variable154 =null;
		ParserRuleReturnScope orderby_variable156 =null;

		Object string_literal155_tree=null;
		Object string_literal157_tree=null;
		RewriteRuleTokenStream stream_ASC=new RewriteRuleTokenStream(adaptor,"token ASC");
		RewriteRuleTokenStream stream_DESC=new RewriteRuleTokenStream(adaptor,"token DESC");
		RewriteRuleSubtreeStream stream_orderby_variable=new RewriteRuleSubtreeStream(adaptor,"rule orderby_variable");

		try {
			// JPA2.g:192:5: ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) )
			int alt49=2;
			alt49 = dfa49.predict(input);
			switch (alt49) {
				case 1 :
					// JPA2.g:192:7: orderby_variable ( 'ASC' )?
					{
					pushFollow(FOLLOW_orderby_variable_in_orderby_item1651);
					orderby_variable154=orderby_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable154.getTree());
					// JPA2.g:192:24: ( 'ASC' )?
					int alt48=2;
					int LA48_0 = input.LA(1);
					if ( (LA48_0==ASC) ) {
						alt48=1;
					}
					switch (alt48) {
						case 1 :
							// JPA2.g:192:25: 'ASC'
							{
							string_literal155=(Token)match(input,ASC,FOLLOW_ASC_in_orderby_item1654); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ASC.add(string_literal155);

							}
							break;

					}

					// AST REWRITE
					// elements: ASC, orderby_variable
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 193:6: -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? )
					{
						// JPA2.g:193:9: ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);
						adaptor.addChild(root_1, stream_orderby_variable.nextTree());
						// JPA2.g:193:65: ( 'ASC' )?
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
					// JPA2.g:194:7: orderby_variable 'DESC'
					{
					pushFollow(FOLLOW_orderby_variable_in_orderby_item1686);
					orderby_variable156=orderby_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable156.getTree());
					string_literal157=(Token)match(input,DESC,FOLLOW_DESC_in_orderby_item1689); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DESC.add(string_literal157);

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
					// 195:5: -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' )
					{
						// JPA2.g:195:8: ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' )
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
	// JPA2.g:196:1: orderby_variable : ( path_expression | general_identification_variable | result_variable );
	public final JPA2Parser.orderby_variable_return orderby_variable() throws RecognitionException {
		JPA2Parser.orderby_variable_return retval = new JPA2Parser.orderby_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression158 =null;
		ParserRuleReturnScope general_identification_variable159 =null;
		ParserRuleReturnScope result_variable160 =null;


		try {
			// JPA2.g:197:5: ( path_expression | general_identification_variable | result_variable )
			int alt50=3;
			int LA50_0 = input.LA(1);
			if ( (LA50_0==WORD) ) {
				int LA50_1 = input.LA(2);
				if ( (LA50_1==58) ) {
					alt50=1;
				}
				else if ( (synpred65_JPA2()) ) {
					alt50=2;
				}
				else if ( (true) ) {
					alt50=3;
				}

			}
			else if ( (LA50_0==99||LA50_0==130) ) {
				alt50=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 50, 0, input);
				throw nvae;
			}

			switch (alt50) {
				case 1 :
					// JPA2.g:197:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_orderby_variable1718);
					path_expression158=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression158.getTree());

					}
					break;
				case 2 :
					// JPA2.g:197:25: general_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_identification_variable_in_orderby_variable1722);
					general_identification_variable159=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable159.getTree());

					}
					break;
				case 3 :
					// JPA2.g:197:59: result_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_result_variable_in_orderby_variable1726);
					result_variable160=result_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, result_variable160.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:199:1: subquery : lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
	public final JPA2Parser.subquery_return subquery() throws RecognitionException {
		JPA2Parser.subquery_return retval = new JPA2Parser.subquery_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token lp=null;
		Token rp=null;
		Token string_literal161=null;
		ParserRuleReturnScope simple_select_clause162 =null;
		ParserRuleReturnScope subquery_from_clause163 =null;
		ParserRuleReturnScope where_clause164 =null;
		ParserRuleReturnScope groupby_clause165 =null;
		ParserRuleReturnScope having_clause166 =null;

		Object lp_tree=null;
		Object rp_tree=null;
		Object string_literal161_tree=null;
		RewriteRuleTokenStream stream_117=new RewriteRuleTokenStream(adaptor,"token 117");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
		RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");

		try {
			// JPA2.g:200:5: (lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
			// JPA2.g:200:7: lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
			{
			lp=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subquery1740); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(lp);

			string_literal161=(Token)match(input,117,FOLLOW_117_in_subquery1742); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_117.add(string_literal161);

			pushFollow(FOLLOW_simple_select_clause_in_subquery1744);
			simple_select_clause162=simple_select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause162.getTree());
			pushFollow(FOLLOW_subquery_from_clause_in_subquery1746);
			subquery_from_clause163=subquery_from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause163.getTree());
			// JPA2.g:200:65: ( where_clause )?
			int alt51=2;
			int LA51_0 = input.LA(1);
			if ( (LA51_0==132) ) {
				alt51=1;
			}
			switch (alt51) {
				case 1 :
					// JPA2.g:200:66: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_subquery1749);
					where_clause164=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause164.getTree());
					}
					break;

			}

			// JPA2.g:200:81: ( groupby_clause )?
			int alt52=2;
			int LA52_0 = input.LA(1);
			if ( (LA52_0==GROUP) ) {
				alt52=1;
			}
			switch (alt52) {
				case 1 :
					// JPA2.g:200:82: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_subquery1754);
					groupby_clause165=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause165.getTree());
					}
					break;

			}

			// JPA2.g:200:99: ( having_clause )?
			int alt53=2;
			int LA53_0 = input.LA(1);
			if ( (LA53_0==HAVING) ) {
				alt53=1;
			}
			switch (alt53) {
				case 1 :
					// JPA2.g:200:100: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_subquery1759);
					having_clause166=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause166.getTree());
					}
					break;

			}

			rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1765); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(rp);

			// AST REWRITE
			// elements: 117, where_clause, subquery_from_clause, simple_select_clause, having_clause, groupby_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 201:6: -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
			{
				// JPA2.g:201:9: ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);
				adaptor.addChild(root_1, stream_117.nextNode());
				adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
				adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
				// JPA2.g:201:90: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// JPA2.g:201:106: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// JPA2.g:201:124: ( having_clause )?
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
	// JPA2.g:202:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
	public final JPA2Parser.subquery_from_clause_return subquery_from_clause() throws RecognitionException {
		JPA2Parser.subquery_from_clause_return retval = new JPA2Parser.subquery_from_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token fr=null;
		Token char_literal168=null;
		ParserRuleReturnScope subselect_identification_variable_declaration167 =null;
		ParserRuleReturnScope subselect_identification_variable_declaration169 =null;

		Object fr_tree=null;
		Object char_literal168_tree=null;
		RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
		RewriteRuleTokenStream stream_93=new RewriteRuleTokenStream(adaptor,"token 93");
		RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");

		try {
			// JPA2.g:203:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
			// JPA2.g:203:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
			{
			fr=(Token)match(input,93,FOLLOW_93_in_subquery_from_clause1815); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_93.add(fr);

			pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1817);
			subselect_identification_variable_declaration167=subselect_identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration167.getTree());
			// JPA2.g:203:63: ( ',' subselect_identification_variable_declaration )*
			loop54:
			while (true) {
				int alt54=2;
				int LA54_0 = input.LA(1);
				if ( (LA54_0==56) ) {
					alt54=1;
				}

				switch (alt54) {
				case 1 :
					// JPA2.g:203:64: ',' subselect_identification_variable_declaration
					{
					char_literal168=(Token)match(input,56,FOLLOW_56_in_subquery_from_clause1820); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_56.add(char_literal168);

					pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1822);
					subselect_identification_variable_declaration169=subselect_identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration169.getTree());
					}
					break;

				default :
					break loop54;
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
			// 204:5: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
			{
				// JPA2.g:204:8: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				// JPA2.g:204:35: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
				while ( stream_subselect_identification_variable_declaration.hasNext() ) {
					// JPA2.g:204:35: ^( T_SOURCE subselect_identification_variable_declaration )
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
	// JPA2.g:206:1: subselect_identification_variable_declaration : ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration );
	public final JPA2Parser.subselect_identification_variable_declaration_return subselect_identification_variable_declaration() throws RecognitionException {
		JPA2Parser.subselect_identification_variable_declaration_return retval = new JPA2Parser.subselect_identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal172=null;
		ParserRuleReturnScope identification_variable_declaration170 =null;
		ParserRuleReturnScope derived_path_expression171 =null;
		ParserRuleReturnScope identification_variable173 =null;
		ParserRuleReturnScope join174 =null;
		ParserRuleReturnScope derived_collection_member_declaration175 =null;

		Object string_literal172_tree=null;

		try {
			// JPA2.g:207:5: ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration )
			int alt56=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA56_1 = input.LA(2);
				if ( (LA56_1==WORD||LA56_1==76) ) {
					alt56=1;
				}
				else if ( (LA56_1==58) ) {
					alt56=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 56, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 125:
				{
				alt56=2;
				}
				break;
			case 96:
				{
				alt56=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 56, 0, input);
				throw nvae;
			}
			switch (alt56) {
				case 1 :
					// JPA2.g:207:7: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1860);
					identification_variable_declaration170=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration170.getTree());

					}
					break;
				case 2 :
					// JPA2.g:208:7: derived_path_expression 'AS' identification_variable ( join )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1868);
					derived_path_expression171=derived_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_path_expression171.getTree());

					string_literal172=(Token)match(input,76,FOLLOW_76_in_subselect_identification_variable_declaration1870); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal172_tree = (Object)adaptor.create(string_literal172);
					adaptor.addChild(root_0, string_literal172_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration1872);
					identification_variable173=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable173.getTree());

					// JPA2.g:208:60: ( join )*
					loop55:
					while (true) {
						int alt55=2;
						int LA55_0 = input.LA(1);
						if ( (LA55_0==INNER||(LA55_0 >= JOIN && LA55_0 <= LEFT)) ) {
							alt55=1;
						}

						switch (alt55) {
						case 1 :
							// JPA2.g:208:61: join
							{
							pushFollow(FOLLOW_join_in_subselect_identification_variable_declaration1875);
							join174=join();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, join174.getTree());

							}
							break;

						default :
							break loop55;
						}
					}

					}
					break;
				case 3 :
					// JPA2.g:209:7: derived_collection_member_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1885);
					derived_collection_member_declaration175=derived_collection_member_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_collection_member_declaration175.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:210:1: derived_path_expression : ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field );
	public final JPA2Parser.derived_path_expression_return derived_path_expression() throws RecognitionException {
		JPA2Parser.derived_path_expression_return retval = new JPA2Parser.derived_path_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal177=null;
		Token char_literal180=null;
		ParserRuleReturnScope general_derived_path176 =null;
		ParserRuleReturnScope single_valued_object_field178 =null;
		ParserRuleReturnScope general_derived_path179 =null;
		ParserRuleReturnScope collection_valued_field181 =null;

		Object char_literal177_tree=null;
		Object char_literal180_tree=null;

		try {
			// JPA2.g:211:5: ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field )
			int alt57=2;
			int LA57_0 = input.LA(1);
			if ( (LA57_0==WORD) ) {
				int LA57_1 = input.LA(2);
				if ( (synpred73_JPA2()) ) {
					alt57=1;
				}
				else if ( (true) ) {
					alt57=2;
				}

			}
			else if ( (LA57_0==125) ) {
				int LA57_2 = input.LA(2);
				if ( (synpred73_JPA2()) ) {
					alt57=1;
				}
				else if ( (true) ) {
					alt57=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 57, 0, input);
				throw nvae;
			}

			switch (alt57) {
				case 1 :
					// JPA2.g:211:7: general_derived_path '.' single_valued_object_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1896);
					general_derived_path176=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path176.getTree());

					char_literal177=(Token)match(input,58,FOLLOW_58_in_derived_path_expression1897); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal177_tree = (Object)adaptor.create(char_literal177);
					adaptor.addChild(root_0, char_literal177_tree);
					}

					pushFollow(FOLLOW_single_valued_object_field_in_derived_path_expression1898);
					single_valued_object_field178=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field178.getTree());

					}
					break;
				case 2 :
					// JPA2.g:212:7: general_derived_path '.' collection_valued_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1906);
					general_derived_path179=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path179.getTree());

					char_literal180=(Token)match(input,58,FOLLOW_58_in_derived_path_expression1907); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal180_tree = (Object)adaptor.create(char_literal180);
					adaptor.addChild(root_0, char_literal180_tree);
					}

					pushFollow(FOLLOW_collection_valued_field_in_derived_path_expression1908);
					collection_valued_field181=collection_valued_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field181.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:213:1: general_derived_path : ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* );
	public final JPA2Parser.general_derived_path_return general_derived_path() throws RecognitionException {
		JPA2Parser.general_derived_path_return retval = new JPA2Parser.general_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal184=null;
		ParserRuleReturnScope simple_derived_path182 =null;
		ParserRuleReturnScope treated_derived_path183 =null;
		ParserRuleReturnScope single_valued_object_field185 =null;

		Object char_literal184_tree=null;

		try {
			// JPA2.g:214:5: ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* )
			int alt59=2;
			int LA59_0 = input.LA(1);
			if ( (LA59_0==WORD) ) {
				alt59=1;
			}
			else if ( (LA59_0==125) ) {
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
					// JPA2.g:214:7: simple_derived_path
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_derived_path_in_general_derived_path1919);
					simple_derived_path182=simple_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_derived_path182.getTree());

					}
					break;
				case 2 :
					// JPA2.g:215:7: treated_derived_path ( '.' single_valued_object_field )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_treated_derived_path_in_general_derived_path1927);
					treated_derived_path183=treated_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, treated_derived_path183.getTree());

					// JPA2.g:215:27: ( '.' single_valued_object_field )*
					loop58:
					while (true) {
						int alt58=2;
						int LA58_0 = input.LA(1);
						if ( (LA58_0==58) ) {
							int LA58_1 = input.LA(2);
							if ( (LA58_1==WORD) ) {
								int LA58_3 = input.LA(3);
								if ( (LA58_3==76) ) {
									int LA58_4 = input.LA(4);
									if ( (LA58_4==WORD) ) {
										int LA58_6 = input.LA(5);
										if ( (LA58_6==RPAREN) ) {
											int LA58_7 = input.LA(6);
											if ( (LA58_7==76) ) {
												int LA58_8 = input.LA(7);
												if ( (LA58_8==WORD) ) {
													int LA58_9 = input.LA(8);
													if ( (LA58_9==RPAREN) ) {
														alt58=1;
													}

												}

											}
											else if ( (LA58_7==58) ) {
												alt58=1;
											}

										}

									}

								}
								else if ( (LA58_3==58) ) {
									alt58=1;
								}

							}

						}

						switch (alt58) {
						case 1 :
							// JPA2.g:215:28: '.' single_valued_object_field
							{
							char_literal184=(Token)match(input,58,FOLLOW_58_in_general_derived_path1929); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal184_tree = (Object)adaptor.create(char_literal184);
							adaptor.addChild(root_0, char_literal184_tree);
							}

							pushFollow(FOLLOW_single_valued_object_field_in_general_derived_path1930);
							single_valued_object_field185=single_valued_object_field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field185.getTree());

							}
							break;

						default :
							break loop58;
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
	// JPA2.g:217:1: simple_derived_path : superquery_identification_variable ;
	public final JPA2Parser.simple_derived_path_return simple_derived_path() throws RecognitionException {
		JPA2Parser.simple_derived_path_return retval = new JPA2Parser.simple_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope superquery_identification_variable186 =null;


		try {
			// JPA2.g:218:5: ( superquery_identification_variable )
			// JPA2.g:218:7: superquery_identification_variable
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_superquery_identification_variable_in_simple_derived_path1948);
			superquery_identification_variable186=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable186.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:220:1: treated_derived_path : 'TREAT(' general_derived_path 'AS' subtype ')' ;
	public final JPA2Parser.treated_derived_path_return treated_derived_path() throws RecognitionException {
		JPA2Parser.treated_derived_path_return retval = new JPA2Parser.treated_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal187=null;
		Token string_literal189=null;
		Token char_literal191=null;
		ParserRuleReturnScope general_derived_path188 =null;
		ParserRuleReturnScope subtype190 =null;

		Object string_literal187_tree=null;
		Object string_literal189_tree=null;
		Object char_literal191_tree=null;

		try {
			// JPA2.g:221:5: ( 'TREAT(' general_derived_path 'AS' subtype ')' )
			// JPA2.g:221:7: 'TREAT(' general_derived_path 'AS' subtype ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal187=(Token)match(input,125,FOLLOW_125_in_treated_derived_path1965); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal187_tree = (Object)adaptor.create(string_literal187);
			adaptor.addChild(root_0, string_literal187_tree);
			}

			pushFollow(FOLLOW_general_derived_path_in_treated_derived_path1966);
			general_derived_path188=general_derived_path();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path188.getTree());

			string_literal189=(Token)match(input,76,FOLLOW_76_in_treated_derived_path1968); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal189_tree = (Object)adaptor.create(string_literal189);
			adaptor.addChild(root_0, string_literal189_tree);
			}

			pushFollow(FOLLOW_subtype_in_treated_derived_path1970);
			subtype190=subtype();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subtype190.getTree());

			char_literal191=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_treated_derived_path1972); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal191_tree = (Object)adaptor.create(char_literal191);
			adaptor.addChild(root_0, char_literal191_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:222:1: derived_collection_member_declaration : 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field ;
	public final JPA2Parser.derived_collection_member_declaration_return derived_collection_member_declaration() throws RecognitionException {
		JPA2Parser.derived_collection_member_declaration_return retval = new JPA2Parser.derived_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal192=null;
		Token char_literal194=null;
		Token char_literal196=null;
		ParserRuleReturnScope superquery_identification_variable193 =null;
		ParserRuleReturnScope single_valued_object_field195 =null;
		ParserRuleReturnScope collection_valued_field197 =null;

		Object string_literal192_tree=null;
		Object char_literal194_tree=null;
		Object char_literal196_tree=null;

		try {
			// JPA2.g:223:5: ( 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field )
			// JPA2.g:223:7: 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field
			{
			root_0 = (Object)adaptor.nil();


			string_literal192=(Token)match(input,96,FOLLOW_96_in_derived_collection_member_declaration1983); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal192_tree = (Object)adaptor.create(string_literal192);
			adaptor.addChild(root_0, string_literal192_tree);
			}

			pushFollow(FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration1985);
			superquery_identification_variable193=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable193.getTree());

			char_literal194=(Token)match(input,58,FOLLOW_58_in_derived_collection_member_declaration1986); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal194_tree = (Object)adaptor.create(char_literal194);
			adaptor.addChild(root_0, char_literal194_tree);
			}

			// JPA2.g:223:49: ( single_valued_object_field '.' )*
			loop60:
			while (true) {
				int alt60=2;
				int LA60_0 = input.LA(1);
				if ( (LA60_0==WORD) ) {
					int LA60_1 = input.LA(2);
					if ( (LA60_1==58) ) {
						alt60=1;
					}

				}

				switch (alt60) {
				case 1 :
					// JPA2.g:223:50: single_valued_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_object_field_in_derived_collection_member_declaration1988);
					single_valued_object_field195=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field195.getTree());

					char_literal196=(Token)match(input,58,FOLLOW_58_in_derived_collection_member_declaration1990); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal196_tree = (Object)adaptor.create(char_literal196);
					adaptor.addChild(root_0, char_literal196_tree);
					}

					}
					break;

				default :
					break loop60;
				}
			}

			pushFollow(FOLLOW_collection_valued_field_in_derived_collection_member_declaration1993);
			collection_valued_field197=collection_valued_field();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_field197.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:225:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
	public final JPA2Parser.simple_select_clause_return simple_select_clause() throws RecognitionException {
		JPA2Parser.simple_select_clause_return retval = new JPA2Parser.simple_select_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal198=null;
		ParserRuleReturnScope simple_select_expression199 =null;

		Object string_literal198_tree=null;
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleSubtreeStream stream_simple_select_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_expression");

		try {
			// JPA2.g:226:5: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
			// JPA2.g:226:7: ( 'DISTINCT' )? simple_select_expression
			{
			// JPA2.g:226:7: ( 'DISTINCT' )?
			int alt61=2;
			int LA61_0 = input.LA(1);
			if ( (LA61_0==DISTINCT) ) {
				alt61=1;
			}
			switch (alt61) {
				case 1 :
					// JPA2.g:226:8: 'DISTINCT'
					{
					string_literal198=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause2006); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal198);

					}
					break;

			}

			pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause2010);
			simple_select_expression199=simple_select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_expression.add(simple_select_expression199.getTree());
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
			// 227:5: -> ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
			{
				// JPA2.g:227:8: ^( T_SELECTED_ITEMS[] ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectedItemsNode(T_SELECTED_ITEMS), root_1);
				// JPA2.g:227:48: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);
				// JPA2.g:227:86: ( 'DISTINCT' )?
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
	// JPA2.g:228:1: simple_select_expression : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.simple_select_expression_return simple_select_expression() throws RecognitionException {
		JPA2Parser.simple_select_expression_return retval = new JPA2Parser.simple_select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression200 =null;
		ParserRuleReturnScope scalar_expression201 =null;
		ParserRuleReturnScope aggregate_expression202 =null;
		ParserRuleReturnScope identification_variable203 =null;


		try {
			// JPA2.g:229:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt62=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA62_1 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt62=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt62=2;
				}
				else if ( (true) ) {
					alt62=4;
				}

				}
				break;
			case INT_NUMERAL:
			case LOWER:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 53:
			case 55:
			case 57:
			case 60:
			case 67:
			case 73:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 84:
			case 97:
			case 101:
			case 103:
			case 106:
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
				alt62=2;
				}
				break;
			case COUNT:
				{
				int LA62_16 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt62=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt62=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 62, 16, input);
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
				int LA62_17 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt62=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt62=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 62, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 94:
				{
				int LA62_18 = input.LA(2);
				if ( (synpred79_JPA2()) ) {
					alt62=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt62=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 62, 18, input);
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
					new NoViableAltException("", 62, 0, input);
				throw nvae;
			}
			switch (alt62) {
				case 1 :
					// JPA2.g:229:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_simple_select_expression2050);
					path_expression200=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression200.getTree());

					}
					break;
				case 2 :
					// JPA2.g:230:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_simple_select_expression2058);
					scalar_expression201=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression201.getTree());

					}
					break;
				case 3 :
					// JPA2.g:231:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression2066);
					aggregate_expression202=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression202.getTree());

					}
					break;
				case 4 :
					// JPA2.g:232:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_select_expression2074);
					identification_variable203=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable203.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:233:1: scalar_expression : ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression );
	public final JPA2Parser.scalar_expression_return scalar_expression() throws RecognitionException {
		JPA2Parser.scalar_expression_return retval = new JPA2Parser.scalar_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope arithmetic_expression204 =null;
		ParserRuleReturnScope string_expression205 =null;
		ParserRuleReturnScope enum_expression206 =null;
		ParserRuleReturnScope datetime_expression207 =null;
		ParserRuleReturnScope boolean_expression208 =null;
		ParserRuleReturnScope case_expression209 =null;
		ParserRuleReturnScope entity_type_expression210 =null;


		try {
			// JPA2.g:234:5: ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression )
			int alt63=7;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 55:
			case 57:
			case 60:
			case 73:
			case 97:
			case 101:
			case 103:
			case 106:
			case 119:
			case 121:
				{
				alt63=1;
				}
				break;
			case WORD:
				{
				int LA63_2 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt63=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt63=5;
				}
				else if ( (true) ) {
					alt63=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA63_5 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt63=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt63=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 63, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 67:
				{
				int LA63_6 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt63=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt63=5;
				}
				else if ( (true) ) {
					alt63=7;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA63_7 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt63=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt63=5;
				}
				else if ( (true) ) {
					alt63=7;
				}

				}
				break;
			case 53:
				{
				int LA63_8 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt63=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt63=5;
				}
				else if ( (true) ) {
					alt63=7;
				}

				}
				break;
			case COUNT:
				{
				int LA63_16 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 63, 16, input);
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
				int LA63_17 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 63, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 94:
				{
				int LA63_18 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt63=5;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 63, 18, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 79:
				{
				int LA63_19 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt63=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt63=5;
				}
				else if ( (synpred86_JPA2()) ) {
					alt63=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 63, 19, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 80:
				{
				int LA63_20 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt63=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt63=5;
				}
				else if ( (synpred86_JPA2()) ) {
					alt63=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 63, 20, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 112:
				{
				int LA63_21 = input.LA(2);
				if ( (synpred81_JPA2()) ) {
					alt63=1;
				}
				else if ( (synpred82_JPA2()) ) {
					alt63=2;
				}
				else if ( (synpred83_JPA2()) ) {
					alt63=3;
				}
				else if ( (synpred84_JPA2()) ) {
					alt63=4;
				}
				else if ( (synpred85_JPA2()) ) {
					alt63=5;
				}
				else if ( (synpred86_JPA2()) ) {
					alt63=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 63, 21, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 81:
			case 122:
			case 126:
			case 129:
				{
				alt63=2;
				}
				break;
			case 82:
			case 83:
			case 84:
				{
				alt63=4;
				}
				break;
			case 134:
			case 135:
				{
				alt63=5;
				}
				break;
			case 127:
				{
				alt63=7;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 63, 0, input);
				throw nvae;
			}
			switch (alt63) {
				case 1 :
					// JPA2.g:234:7: arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_scalar_expression2085);
					arithmetic_expression204=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression204.getTree());

					}
					break;
				case 2 :
					// JPA2.g:235:7: string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_scalar_expression2093);
					string_expression205=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression205.getTree());

					}
					break;
				case 3 :
					// JPA2.g:236:7: enum_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_scalar_expression2101);
					enum_expression206=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression206.getTree());

					}
					break;
				case 4 :
					// JPA2.g:237:7: datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_scalar_expression2109);
					datetime_expression207=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression207.getTree());

					}
					break;
				case 5 :
					// JPA2.g:238:7: boolean_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_scalar_expression2117);
					boolean_expression208=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression208.getTree());

					}
					break;
				case 6 :
					// JPA2.g:239:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_scalar_expression2125);
					case_expression209=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression209.getTree());

					}
					break;
				case 7 :
					// JPA2.g:240:7: entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_scalar_expression2133);
					entity_type_expression210=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression210.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:241:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
	public final JPA2Parser.conditional_expression_return conditional_expression() throws RecognitionException {
		JPA2Parser.conditional_expression_return retval = new JPA2Parser.conditional_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal212=null;
		ParserRuleReturnScope conditional_term211 =null;
		ParserRuleReturnScope conditional_term213 =null;

		Object string_literal212_tree=null;

		try {
			// JPA2.g:242:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
			// JPA2.g:242:7: ( conditional_term ) ( 'OR' conditional_term )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:242:7: ( conditional_term )
			// JPA2.g:242:8: conditional_term
			{
			pushFollow(FOLLOW_conditional_term_in_conditional_expression2145);
			conditional_term211=conditional_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term211.getTree());

			}

			// JPA2.g:242:26: ( 'OR' conditional_term )*
			loop64:
			while (true) {
				int alt64=2;
				int LA64_0 = input.LA(1);
				if ( (LA64_0==OR) ) {
					alt64=1;
				}

				switch (alt64) {
				case 1 :
					// JPA2.g:242:27: 'OR' conditional_term
					{
					string_literal212=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression2149); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal212_tree = (Object)adaptor.create(string_literal212);
					adaptor.addChild(root_0, string_literal212_tree);
					}

					pushFollow(FOLLOW_conditional_term_in_conditional_expression2151);
					conditional_term213=conditional_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term213.getTree());

					}
					break;

				default :
					break loop64;
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
	// JPA2.g:243:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
	public final JPA2Parser.conditional_term_return conditional_term() throws RecognitionException {
		JPA2Parser.conditional_term_return retval = new JPA2Parser.conditional_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal215=null;
		ParserRuleReturnScope conditional_factor214 =null;
		ParserRuleReturnScope conditional_factor216 =null;

		Object string_literal215_tree=null;

		try {
			// JPA2.g:244:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
			// JPA2.g:244:7: ( conditional_factor ) ( 'AND' conditional_factor )*
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:244:7: ( conditional_factor )
			// JPA2.g:244:8: conditional_factor
			{
			pushFollow(FOLLOW_conditional_factor_in_conditional_term2165);
			conditional_factor214=conditional_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor214.getTree());

			}

			// JPA2.g:244:28: ( 'AND' conditional_factor )*
			loop65:
			while (true) {
				int alt65=2;
				int LA65_0 = input.LA(1);
				if ( (LA65_0==AND) ) {
					alt65=1;
				}

				switch (alt65) {
				case 1 :
					// JPA2.g:244:29: 'AND' conditional_factor
					{
					string_literal215=(Token)match(input,AND,FOLLOW_AND_in_conditional_term2169); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal215_tree = (Object)adaptor.create(string_literal215);
					adaptor.addChild(root_0, string_literal215_tree);
					}

					pushFollow(FOLLOW_conditional_factor_in_conditional_term2171);
					conditional_factor216=conditional_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor216.getTree());

					}
					break;

				default :
					break loop65;
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
	// JPA2.g:245:1: conditional_factor : ( 'NOT' )? conditional_primary ;
	public final JPA2Parser.conditional_factor_return conditional_factor() throws RecognitionException {
		JPA2Parser.conditional_factor_return retval = new JPA2Parser.conditional_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal217=null;
		ParserRuleReturnScope conditional_primary218 =null;

		Object string_literal217_tree=null;

		try {
			// JPA2.g:246:5: ( ( 'NOT' )? conditional_primary )
			// JPA2.g:246:7: ( 'NOT' )? conditional_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:246:7: ( 'NOT' )?
			int alt66=2;
			int LA66_0 = input.LA(1);
			if ( (LA66_0==109) ) {
				int LA66_1 = input.LA(2);
				if ( (synpred89_JPA2()) ) {
					alt66=1;
				}
			}
			switch (alt66) {
				case 1 :
					// JPA2.g:246:8: 'NOT'
					{
					string_literal217=(Token)match(input,109,FOLLOW_109_in_conditional_factor2185); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal217_tree = (Object)adaptor.create(string_literal217);
					adaptor.addChild(root_0, string_literal217_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_conditional_primary_in_conditional_factor2189);
			conditional_primary218=conditional_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_primary218.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:247:1: conditional_primary : ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' );
	public final JPA2Parser.conditional_primary_return conditional_primary() throws RecognitionException {
		JPA2Parser.conditional_primary_return retval = new JPA2Parser.conditional_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal220=null;
		Token char_literal222=null;
		ParserRuleReturnScope simple_cond_expression219 =null;
		ParserRuleReturnScope conditional_expression221 =null;

		Object char_literal220_tree=null;
		Object char_literal222_tree=null;
		RewriteRuleSubtreeStream stream_simple_cond_expression=new RewriteRuleSubtreeStream(adaptor,"rule simple_cond_expression");

		try {
			// JPA2.g:248:5: ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' )
			int alt67=2;
			int LA67_0 = input.LA(1);
			if ( (LA67_0==AVG||LA67_0==COUNT||LA67_0==INT_NUMERAL||LA67_0==LOWER||(LA67_0 >= MAX && LA67_0 <= NAMED_PARAMETER)||(LA67_0 >= STRING_LITERAL && LA67_0 <= SUM)||LA67_0==WORD||LA67_0==53||LA67_0==55||LA67_0==57||LA67_0==60||(LA67_0 >= 67 && LA67_0 <= 73)||(LA67_0 >= 79 && LA67_0 <= 84)||LA67_0==92||LA67_0==94||LA67_0==97||LA67_0==101||LA67_0==103||LA67_0==106||LA67_0==109||LA67_0==112||LA67_0==119||(LA67_0 >= 121 && LA67_0 <= 122)||(LA67_0 >= 126 && LA67_0 <= 127)||LA67_0==129||(LA67_0 >= 134 && LA67_0 <= 135)) ) {
				alt67=1;
			}
			else if ( (LA67_0==LPAREN) ) {
				int LA67_17 = input.LA(2);
				if ( (synpred90_JPA2()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 67, 0, input);
				throw nvae;
			}

			switch (alt67) {
				case 1 :
					// JPA2.g:248:7: simple_cond_expression
					{
					pushFollow(FOLLOW_simple_cond_expression_in_conditional_primary2200);
					simple_cond_expression219=simple_cond_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_simple_cond_expression.add(simple_cond_expression219.getTree());
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
					// 249:5: -> ^( T_SIMPLE_CONDITION[] simple_cond_expression )
					{
						// JPA2.g:249:8: ^( T_SIMPLE_CONDITION[] simple_cond_expression )
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
					// JPA2.g:250:7: '(' conditional_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal220=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_primary2224); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal220_tree = (Object)adaptor.create(char_literal220);
					adaptor.addChild(root_0, char_literal220_tree);
					}

					pushFollow(FOLLOW_conditional_expression_in_conditional_primary2225);
					conditional_expression221=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression221.getTree());

					char_literal222=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_primary2226); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal222_tree = (Object)adaptor.create(char_literal222);
					adaptor.addChild(root_0, char_literal222_tree);
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
	// JPA2.g:251:1: simple_cond_expression : ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
	public final JPA2Parser.simple_cond_expression_return simple_cond_expression() throws RecognitionException {
		JPA2Parser.simple_cond_expression_return retval = new JPA2Parser.simple_cond_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope comparison_expression223 =null;
		ParserRuleReturnScope between_expression224 =null;
		ParserRuleReturnScope in_expression225 =null;
		ParserRuleReturnScope like_expression226 =null;
		ParserRuleReturnScope null_comparison_expression227 =null;
		ParserRuleReturnScope empty_collection_comparison_expression228 =null;
		ParserRuleReturnScope collection_member_expression229 =null;
		ParserRuleReturnScope exists_expression230 =null;
		ParserRuleReturnScope date_macro_expression231 =null;


		try {
			// JPA2.g:252:5: ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
			int alt68=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA68_1 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred93_JPA2()) ) {
					alt68=3;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}
				else if ( (synpred95_JPA2()) ) {
					alt68=5;
				}
				else if ( (synpred96_JPA2()) ) {
					alt68=6;
				}
				else if ( (synpred97_JPA2()) ) {
					alt68=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case STRING_LITERAL:
				{
				int LA68_2 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 67:
				{
				int LA68_3 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}
				else if ( (synpred95_JPA2()) ) {
					alt68=5;
				}
				else if ( (synpred97_JPA2()) ) {
					alt68=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA68_4 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}
				else if ( (synpred95_JPA2()) ) {
					alt68=5;
				}
				else if ( (synpred97_JPA2()) ) {
					alt68=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 53:
				{
				int LA68_5 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}
				else if ( (synpred95_JPA2()) ) {
					alt68=5;
				}
				else if ( (synpred97_JPA2()) ) {
					alt68=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 81:
				{
				int LA68_6 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 122:
				{
				int LA68_7 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 7, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 126:
				{
				int LA68_8 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LOWER:
				{
				int LA68_9 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 9, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 129:
				{
				int LA68_10 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case COUNT:
				{
				int LA68_11 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 11, input);
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
				int LA68_12 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 12, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 94:
				{
				int LA68_13 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 79:
				{
				int LA68_14 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 14, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 80:
				{
				int LA68_15 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 15, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 112:
				{
				int LA68_16 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 16, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LPAREN:
				{
				int LA68_17 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}
				else if ( (synpred94_JPA2()) ) {
					alt68=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 17, input);
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
				alt68=1;
				}
				break;
			case 82:
			case 83:
			case 84:
				{
				int LA68_19 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 19, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 127:
				{
				int LA68_20 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred93_JPA2()) ) {
					alt68=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 20, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 55:
			case 57:
				{
				int LA68_21 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 21, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 60:
				{
				int LA68_22 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 22, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA68_23 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 23, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 101:
				{
				int LA68_24 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 24, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 103:
				{
				int LA68_25 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 25, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 73:
				{
				int LA68_26 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 26, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 121:
				{
				int LA68_27 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 27, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 106:
				{
				int LA68_28 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 28, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 119:
				{
				int LA68_29 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 29, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 97:
				{
				int LA68_30 = input.LA(2);
				if ( (synpred91_JPA2()) ) {
					alt68=1;
				}
				else if ( (synpred92_JPA2()) ) {
					alt68=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 68, 30, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 92:
			case 109:
				{
				alt68=8;
				}
				break;
			case 68:
			case 69:
			case 70:
			case 71:
			case 72:
				{
				alt68=9;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 68, 0, input);
				throw nvae;
			}
			switch (alt68) {
				case 1 :
					// JPA2.g:252:7: comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression2237);
					comparison_expression223=comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression223.getTree());

					}
					break;
				case 2 :
					// JPA2.g:253:7: between_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_between_expression_in_simple_cond_expression2245);
					between_expression224=between_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression224.getTree());

					}
					break;
				case 3 :
					// JPA2.g:254:7: in_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_in_expression_in_simple_cond_expression2253);
					in_expression225=in_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression225.getTree());

					}
					break;
				case 4 :
					// JPA2.g:255:7: like_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_like_expression_in_simple_cond_expression2261);
					like_expression226=like_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression226.getTree());

					}
					break;
				case 5 :
					// JPA2.g:256:7: null_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression2269);
					null_comparison_expression227=null_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression227.getTree());

					}
					break;
				case 6 :
					// JPA2.g:257:7: empty_collection_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2277);
					empty_collection_comparison_expression228=empty_collection_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression228.getTree());

					}
					break;
				case 7 :
					// JPA2.g:258:7: collection_member_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression2285);
					collection_member_expression229=collection_member_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression229.getTree());

					}
					break;
				case 8 :
					// JPA2.g:259:7: exists_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exists_expression_in_simple_cond_expression2293);
					exists_expression230=exists_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression230.getTree());

					}
					break;
				case 9 :
					// JPA2.g:260:7: date_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression2301);
					date_macro_expression231=date_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_macro_expression231.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:263:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
	public final JPA2Parser.date_macro_expression_return date_macro_expression() throws RecognitionException {
		JPA2Parser.date_macro_expression_return retval = new JPA2Parser.date_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope date_between_macro_expression232 =null;
		ParserRuleReturnScope date_before_macro_expression233 =null;
		ParserRuleReturnScope date_after_macro_expression234 =null;
		ParserRuleReturnScope date_equals_macro_expression235 =null;
		ParserRuleReturnScope date_today_macro_expression236 =null;


		try {
			// JPA2.g:264:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
			int alt69=5;
			switch ( input.LA(1) ) {
			case 68:
				{
				alt69=1;
				}
				break;
			case 70:
				{
				alt69=2;
				}
				break;
			case 69:
				{
				alt69=3;
				}
				break;
			case 71:
				{
				alt69=4;
				}
				break;
			case 72:
				{
				alt69=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 69, 0, input);
				throw nvae;
			}
			switch (alt69) {
				case 1 :
					// JPA2.g:264:7: date_between_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2314);
					date_between_macro_expression232=date_between_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression232.getTree());

					}
					break;
				case 2 :
					// JPA2.g:265:7: date_before_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2322);
					date_before_macro_expression233=date_before_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression233.getTree());

					}
					break;
				case 3 :
					// JPA2.g:266:7: date_after_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2330);
					date_after_macro_expression234=date_after_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression234.getTree());

					}
					break;
				case 4 :
					// JPA2.g:267:7: date_equals_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2338);
					date_equals_macro_expression235=date_equals_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression235.getTree());

					}
					break;
				case 5 :
					// JPA2.g:268:7: date_today_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2346);
					date_today_macro_expression236=date_today_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_today_macro_expression236.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:270:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
	public final JPA2Parser.date_between_macro_expression_return date_between_macro_expression() throws RecognitionException {
		JPA2Parser.date_between_macro_expression_return retval = new JPA2Parser.date_between_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal237=null;
		Token char_literal238=null;
		Token char_literal240=null;
		Token string_literal241=null;
		Token set242=null;
		Token char_literal244=null;
		Token string_literal245=null;
		Token set246=null;
		Token char_literal248=null;
		Token set249=null;
		Token char_literal250=null;
		ParserRuleReturnScope path_expression239 =null;
		ParserRuleReturnScope numeric_literal243 =null;
		ParserRuleReturnScope numeric_literal247 =null;

		Object string_literal237_tree=null;
		Object char_literal238_tree=null;
		Object char_literal240_tree=null;
		Object string_literal241_tree=null;
		Object set242_tree=null;
		Object char_literal244_tree=null;
		Object string_literal245_tree=null;
		Object set246_tree=null;
		Object char_literal248_tree=null;
		Object set249_tree=null;
		Object char_literal250_tree=null;

		try {
			// JPA2.g:271:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
			// JPA2.g:271:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal237=(Token)match(input,68,FOLLOW_68_in_date_between_macro_expression2358); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal237_tree = (Object)adaptor.create(string_literal237);
			adaptor.addChild(root_0, string_literal237_tree);
			}

			char_literal238=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2360); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal238_tree = (Object)adaptor.create(char_literal238);
			adaptor.addChild(root_0, char_literal238_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2362);
			path_expression239=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression239.getTree());

			char_literal240=(Token)match(input,56,FOLLOW_56_in_date_between_macro_expression2364); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal240_tree = (Object)adaptor.create(char_literal240);
			adaptor.addChild(root_0, char_literal240_tree);
			}

			string_literal241=(Token)match(input,110,FOLLOW_110_in_date_between_macro_expression2366); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal241_tree = (Object)adaptor.create(string_literal241);
			adaptor.addChild(root_0, string_literal241_tree);
			}

			// JPA2.g:271:48: ( ( '+' | '-' ) numeric_literal )?
			int alt70=2;
			int LA70_0 = input.LA(1);
			if ( (LA70_0==55||LA70_0==57) ) {
				alt70=1;
			}
			switch (alt70) {
				case 1 :
					// JPA2.g:271:49: ( '+' | '-' ) numeric_literal
					{
					set242=input.LT(1);
					if ( input.LA(1)==55||input.LA(1)==57 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2377);
					numeric_literal243=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal243.getTree());

					}
					break;

			}

			char_literal244=(Token)match(input,56,FOLLOW_56_in_date_between_macro_expression2381); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal244_tree = (Object)adaptor.create(char_literal244);
			adaptor.addChild(root_0, char_literal244_tree);
			}

			string_literal245=(Token)match(input,110,FOLLOW_110_in_date_between_macro_expression2383); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal245_tree = (Object)adaptor.create(string_literal245);
			adaptor.addChild(root_0, string_literal245_tree);
			}

			// JPA2.g:271:89: ( ( '+' | '-' ) numeric_literal )?
			int alt71=2;
			int LA71_0 = input.LA(1);
			if ( (LA71_0==55||LA71_0==57) ) {
				alt71=1;
			}
			switch (alt71) {
				case 1 :
					// JPA2.g:271:90: ( '+' | '-' ) numeric_literal
					{
					set246=input.LT(1);
					if ( input.LA(1)==55||input.LA(1)==57 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set246));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2394);
					numeric_literal247=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal247.getTree());

					}
					break;

			}

			char_literal248=(Token)match(input,56,FOLLOW_56_in_date_between_macro_expression2398); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal248_tree = (Object)adaptor.create(char_literal248);
			adaptor.addChild(root_0, char_literal248_tree);
			}

			set249=input.LT(1);
			if ( input.LA(1)==85||input.LA(1)==95||input.LA(1)==105||input.LA(1)==107||input.LA(1)==116||input.LA(1)==133 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set249));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			char_literal250=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2423); if (state.failed) return retval;
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
	// JPA2.g:273:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_before_macro_expression_return date_before_macro_expression() throws RecognitionException {
		JPA2Parser.date_before_macro_expression_return retval = new JPA2Parser.date_before_macro_expression_return();
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
			// JPA2.g:274:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:274:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal251=(Token)match(input,70,FOLLOW_70_in_date_before_macro_expression2435); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal251_tree = (Object)adaptor.create(string_literal251);
			adaptor.addChild(root_0, string_literal251_tree);
			}

			char_literal252=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2437); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal252_tree = (Object)adaptor.create(char_literal252);
			adaptor.addChild(root_0, char_literal252_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2439);
			path_expression253=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression253.getTree());

			char_literal254=(Token)match(input,56,FOLLOW_56_in_date_before_macro_expression2441); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal254_tree = (Object)adaptor.create(char_literal254);
			adaptor.addChild(root_0, char_literal254_tree);
			}

			// JPA2.g:274:45: ( path_expression | input_parameter )
			int alt72=2;
			int LA72_0 = input.LA(1);
			if ( (LA72_0==WORD) ) {
				alt72=1;
			}
			else if ( (LA72_0==NAMED_PARAMETER||LA72_0==53||LA72_0==67) ) {
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
					// JPA2.g:274:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2444);
					path_expression255=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression255.getTree());

					}
					break;
				case 2 :
					// JPA2.g:274:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2448);
					input_parameter256=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter256.getTree());

					}
					break;

			}

			char_literal257=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2451); if (state.failed) return retval;
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
	// $ANTLR end "date_before_macro_expression"


	public static class date_after_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_after_macro_expression"
	// JPA2.g:276:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_after_macro_expression_return date_after_macro_expression() throws RecognitionException {
		JPA2Parser.date_after_macro_expression_return retval = new JPA2Parser.date_after_macro_expression_return();
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
			// JPA2.g:277:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:277:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal258=(Token)match(input,69,FOLLOW_69_in_date_after_macro_expression2463); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal258_tree = (Object)adaptor.create(string_literal258);
			adaptor.addChild(root_0, string_literal258_tree);
			}

			char_literal259=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2465); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal259_tree = (Object)adaptor.create(char_literal259);
			adaptor.addChild(root_0, char_literal259_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2467);
			path_expression260=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression260.getTree());

			char_literal261=(Token)match(input,56,FOLLOW_56_in_date_after_macro_expression2469); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal261_tree = (Object)adaptor.create(char_literal261);
			adaptor.addChild(root_0, char_literal261_tree);
			}

			// JPA2.g:277:44: ( path_expression | input_parameter )
			int alt73=2;
			int LA73_0 = input.LA(1);
			if ( (LA73_0==WORD) ) {
				alt73=1;
			}
			else if ( (LA73_0==NAMED_PARAMETER||LA73_0==53||LA73_0==67) ) {
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
					// JPA2.g:277:45: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2472);
					path_expression262=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression262.getTree());

					}
					break;
				case 2 :
					// JPA2.g:277:63: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2476);
					input_parameter263=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter263.getTree());

					}
					break;

			}

			char_literal264=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2479); if (state.failed) return retval;
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
	// $ANTLR end "date_after_macro_expression"


	public static class date_equals_macro_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date_equals_macro_expression"
	// JPA2.g:279:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
	public final JPA2Parser.date_equals_macro_expression_return date_equals_macro_expression() throws RecognitionException {
		JPA2Parser.date_equals_macro_expression_return retval = new JPA2Parser.date_equals_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal265=null;
		Token char_literal266=null;
		Token char_literal268=null;
		Token char_literal271=null;
		ParserRuleReturnScope path_expression267 =null;
		ParserRuleReturnScope path_expression269 =null;
		ParserRuleReturnScope input_parameter270 =null;

		Object string_literal265_tree=null;
		Object char_literal266_tree=null;
		Object char_literal268_tree=null;
		Object char_literal271_tree=null;

		try {
			// JPA2.g:280:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// JPA2.g:280:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal265=(Token)match(input,71,FOLLOW_71_in_date_equals_macro_expression2491); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal265_tree = (Object)adaptor.create(string_literal265);
			adaptor.addChild(root_0, string_literal265_tree);
			}

			char_literal266=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2493); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal266_tree = (Object)adaptor.create(char_literal266);
			adaptor.addChild(root_0, char_literal266_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2495);
			path_expression267=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression267.getTree());

			char_literal268=(Token)match(input,56,FOLLOW_56_in_date_equals_macro_expression2497); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal268_tree = (Object)adaptor.create(char_literal268);
			adaptor.addChild(root_0, char_literal268_tree);
			}

			// JPA2.g:280:45: ( path_expression | input_parameter )
			int alt74=2;
			int LA74_0 = input.LA(1);
			if ( (LA74_0==WORD) ) {
				alt74=1;
			}
			else if ( (LA74_0==NAMED_PARAMETER||LA74_0==53||LA74_0==67) ) {
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
					// JPA2.g:280:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2500);
					path_expression269=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression269.getTree());

					}
					break;
				case 2 :
					// JPA2.g:280:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2504);
					input_parameter270=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter270.getTree());

					}
					break;

			}

			char_literal271=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2507); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal271_tree = (Object)adaptor.create(char_literal271);
			adaptor.addChild(root_0, char_literal271_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:282:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
	public final JPA2Parser.date_today_macro_expression_return date_today_macro_expression() throws RecognitionException {
		JPA2Parser.date_today_macro_expression_return retval = new JPA2Parser.date_today_macro_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal272=null;
		Token char_literal273=null;
		Token char_literal275=null;
		ParserRuleReturnScope path_expression274 =null;

		Object string_literal272_tree=null;
		Object char_literal273_tree=null;
		Object char_literal275_tree=null;

		try {
			// JPA2.g:283:5: ( '@TODAY' '(' path_expression ')' )
			// JPA2.g:283:7: '@TODAY' '(' path_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal272=(Token)match(input,72,FOLLOW_72_in_date_today_macro_expression2519); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal272_tree = (Object)adaptor.create(string_literal272);
			adaptor.addChild(root_0, string_literal272_tree);
			}

			char_literal273=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2521); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal273_tree = (Object)adaptor.create(char_literal273);
			adaptor.addChild(root_0, char_literal273_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2523);
			path_expression274=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression274.getTree());

			char_literal275=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2525); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal275_tree = (Object)adaptor.create(char_literal275);
			adaptor.addChild(root_0, char_literal275_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:286:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
	public final JPA2Parser.between_expression_return between_expression() throws RecognitionException {
		JPA2Parser.between_expression_return retval = new JPA2Parser.between_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal277=null;
		Token string_literal278=null;
		Token string_literal280=null;
		Token string_literal283=null;
		Token string_literal284=null;
		Token string_literal286=null;
		Token string_literal289=null;
		Token string_literal290=null;
		Token string_literal292=null;
		ParserRuleReturnScope arithmetic_expression276 =null;
		ParserRuleReturnScope arithmetic_expression279 =null;
		ParserRuleReturnScope arithmetic_expression281 =null;
		ParserRuleReturnScope string_expression282 =null;
		ParserRuleReturnScope string_expression285 =null;
		ParserRuleReturnScope string_expression287 =null;
		ParserRuleReturnScope datetime_expression288 =null;
		ParserRuleReturnScope datetime_expression291 =null;
		ParserRuleReturnScope datetime_expression293 =null;

		Object string_literal277_tree=null;
		Object string_literal278_tree=null;
		Object string_literal280_tree=null;
		Object string_literal283_tree=null;
		Object string_literal284_tree=null;
		Object string_literal286_tree=null;
		Object string_literal289_tree=null;
		Object string_literal290_tree=null;
		Object string_literal292_tree=null;

		try {
			// JPA2.g:287:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
			int alt78=3;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 55:
			case 57:
			case 60:
			case 73:
			case 97:
			case 101:
			case 103:
			case 106:
			case 119:
			case 121:
				{
				alt78=1;
				}
				break;
			case WORD:
				{
				int LA78_2 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case LPAREN:
				{
				int LA78_5 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case 67:
				{
				int LA78_6 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA78_7 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case 53:
				{
				int LA78_8 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case COUNT:
				{
				int LA78_16 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA78_17 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case 94:
				{
				int LA78_18 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case 79:
				{
				int LA78_19 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case 80:
				{
				int LA78_20 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case 112:
				{
				int LA78_21 = input.LA(2);
				if ( (synpred116_JPA2()) ) {
					alt78=1;
				}
				else if ( (synpred118_JPA2()) ) {
					alt78=2;
				}
				else if ( (true) ) {
					alt78=3;
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 81:
			case 122:
			case 126:
			case 129:
				{
				alt78=2;
				}
				break;
			case 82:
			case 83:
			case 84:
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
					// JPA2.g:287:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2538);
					arithmetic_expression276=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression276.getTree());

					// JPA2.g:287:29: ( 'NOT' )?
					int alt75=2;
					int LA75_0 = input.LA(1);
					if ( (LA75_0==109) ) {
						alt75=1;
					}
					switch (alt75) {
						case 1 :
							// JPA2.g:287:30: 'NOT'
							{
							string_literal277=(Token)match(input,109,FOLLOW_109_in_between_expression2541); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal277_tree = (Object)adaptor.create(string_literal277);
							adaptor.addChild(root_0, string_literal277_tree);
							}

							}
							break;

					}

					string_literal278=(Token)match(input,77,FOLLOW_77_in_between_expression2545); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal278_tree = (Object)adaptor.create(string_literal278);
					adaptor.addChild(root_0, string_literal278_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2547);
					arithmetic_expression279=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression279.getTree());

					string_literal280=(Token)match(input,AND,FOLLOW_AND_in_between_expression2549); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal280_tree = (Object)adaptor.create(string_literal280);
					adaptor.addChild(root_0, string_literal280_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2551);
					arithmetic_expression281=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression281.getTree());

					}
					break;
				case 2 :
					// JPA2.g:288:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_between_expression2559);
					string_expression282=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression282.getTree());

					// JPA2.g:288:25: ( 'NOT' )?
					int alt76=2;
					int LA76_0 = input.LA(1);
					if ( (LA76_0==109) ) {
						alt76=1;
					}
					switch (alt76) {
						case 1 :
							// JPA2.g:288:26: 'NOT'
							{
							string_literal283=(Token)match(input,109,FOLLOW_109_in_between_expression2562); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal283_tree = (Object)adaptor.create(string_literal283);
							adaptor.addChild(root_0, string_literal283_tree);
							}

							}
							break;

					}

					string_literal284=(Token)match(input,77,FOLLOW_77_in_between_expression2566); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal284_tree = (Object)adaptor.create(string_literal284);
					adaptor.addChild(root_0, string_literal284_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2568);
					string_expression285=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression285.getTree());

					string_literal286=(Token)match(input,AND,FOLLOW_AND_in_between_expression2570); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal286_tree = (Object)adaptor.create(string_literal286);
					adaptor.addChild(root_0, string_literal286_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2572);
					string_expression287=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression287.getTree());

					}
					break;
				case 3 :
					// JPA2.g:289:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_between_expression2580);
					datetime_expression288=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression288.getTree());

					// JPA2.g:289:27: ( 'NOT' )?
					int alt77=2;
					int LA77_0 = input.LA(1);
					if ( (LA77_0==109) ) {
						alt77=1;
					}
					switch (alt77) {
						case 1 :
							// JPA2.g:289:28: 'NOT'
							{
							string_literal289=(Token)match(input,109,FOLLOW_109_in_between_expression2583); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal289_tree = (Object)adaptor.create(string_literal289);
							adaptor.addChild(root_0, string_literal289_tree);
							}

							}
							break;

					}

					string_literal290=(Token)match(input,77,FOLLOW_77_in_between_expression2587); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal290_tree = (Object)adaptor.create(string_literal290);
					adaptor.addChild(root_0, string_literal290_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2589);
					datetime_expression291=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression291.getTree());

					string_literal292=(Token)match(input,AND,FOLLOW_AND_in_between_expression2591); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal292_tree = (Object)adaptor.create(string_literal292);
					adaptor.addChild(root_0, string_literal292_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2593);
					datetime_expression293=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression293.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:290:1: in_expression : ( path_expression | type_discriminator ) ( 'NOT' )? 'IN' ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter ) ;
	public final JPA2Parser.in_expression_return in_expression() throws RecognitionException {
		JPA2Parser.in_expression_return retval = new JPA2Parser.in_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal296=null;
		Token string_literal297=null;
		Token char_literal298=null;
		Token char_literal300=null;
		Token char_literal302=null;
		ParserRuleReturnScope path_expression294 =null;
		ParserRuleReturnScope type_discriminator295 =null;
		ParserRuleReturnScope in_item299 =null;
		ParserRuleReturnScope in_item301 =null;
		ParserRuleReturnScope subquery303 =null;
		ParserRuleReturnScope collection_valued_input_parameter304 =null;

		Object string_literal296_tree=null;
		Object string_literal297_tree=null;
		Object char_literal298_tree=null;
		Object char_literal300_tree=null;
		Object char_literal302_tree=null;

		try {
			// JPA2.g:291:5: ( ( path_expression | type_discriminator ) ( 'NOT' )? 'IN' ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter ) )
			// JPA2.g:291:7: ( path_expression | type_discriminator ) ( 'NOT' )? 'IN' ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter )
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:291:7: ( path_expression | type_discriminator )
			int alt79=2;
			int LA79_0 = input.LA(1);
			if ( (LA79_0==WORD) ) {
				alt79=1;
			}
			else if ( (LA79_0==127) ) {
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
					// JPA2.g:291:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_in_expression2605);
					path_expression294=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression294.getTree());

					}
					break;
				case 2 :
					// JPA2.g:291:26: type_discriminator
					{
					pushFollow(FOLLOW_type_discriminator_in_in_expression2609);
					type_discriminator295=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator295.getTree());

					}
					break;

			}

			// JPA2.g:291:46: ( 'NOT' )?
			int alt80=2;
			int LA80_0 = input.LA(1);
			if ( (LA80_0==109) ) {
				alt80=1;
			}
			switch (alt80) {
				case 1 :
					// JPA2.g:291:47: 'NOT'
					{
					string_literal296=(Token)match(input,109,FOLLOW_109_in_in_expression2613); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal296_tree = (Object)adaptor.create(string_literal296);
					adaptor.addChild(root_0, string_literal296_tree);
					}

					}
					break;

			}

			string_literal297=(Token)match(input,96,FOLLOW_96_in_in_expression2617); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal297_tree = (Object)adaptor.create(string_literal297);
			adaptor.addChild(root_0, string_literal297_tree);
			}

			// JPA2.g:292:13: ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter )
			int alt82=3;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==LPAREN) ) {
				int LA82_1 = input.LA(2);
				if ( (LA82_1==117) ) {
					alt82=2;
				}
				else if ( (LA82_1==NAMED_PARAMETER||LA82_1==WORD||LA82_1==53||LA82_1==67) ) {
					alt82=1;
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
			else if ( (LA82_0==NAMED_PARAMETER||LA82_0==53||LA82_0==67) ) {
				alt82=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 82, 0, input);
				throw nvae;
			}

			switch (alt82) {
				case 1 :
					// JPA2.g:292:15: '(' in_item ( ',' in_item )* ')'
					{
					char_literal298=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2633); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal298_tree = (Object)adaptor.create(char_literal298);
					adaptor.addChild(root_0, char_literal298_tree);
					}

					pushFollow(FOLLOW_in_item_in_in_expression2635);
					in_item299=in_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item299.getTree());

					// JPA2.g:292:27: ( ',' in_item )*
					loop81:
					while (true) {
						int alt81=2;
						int LA81_0 = input.LA(1);
						if ( (LA81_0==56) ) {
							alt81=1;
						}

						switch (alt81) {
						case 1 :
							// JPA2.g:292:28: ',' in_item
							{
							char_literal300=(Token)match(input,56,FOLLOW_56_in_in_expression2638); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal300_tree = (Object)adaptor.create(char_literal300);
							adaptor.addChild(root_0, char_literal300_tree);
							}

							pushFollow(FOLLOW_in_item_in_in_expression2640);
							in_item301=in_item();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item301.getTree());

							}
							break;

						default :
							break loop81;
						}
					}

					char_literal302=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2644); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal302_tree = (Object)adaptor.create(char_literal302);
					adaptor.addChild(root_0, char_literal302_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:293:15: subquery
					{
					pushFollow(FOLLOW_subquery_in_in_expression2660);
					subquery303=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery303.getTree());

					}
					break;
				case 3 :
					// JPA2.g:294:15: collection_valued_input_parameter
					{
					pushFollow(FOLLOW_collection_valued_input_parameter_in_in_expression2676);
					collection_valued_input_parameter304=collection_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_valued_input_parameter304.getTree());

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
	// JPA2.g:295:1: in_item : ( literal | single_valued_input_parameter );
	public final JPA2Parser.in_item_return in_item() throws RecognitionException {
		JPA2Parser.in_item_return retval = new JPA2Parser.in_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal305 =null;
		ParserRuleReturnScope single_valued_input_parameter306 =null;


		try {
			// JPA2.g:296:5: ( literal | single_valued_input_parameter )
			int alt83=2;
			int LA83_0 = input.LA(1);
			if ( (LA83_0==WORD) ) {
				alt83=1;
			}
			else if ( (LA83_0==NAMED_PARAMETER||LA83_0==53||LA83_0==67) ) {
				alt83=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 83, 0, input);
				throw nvae;
			}

			switch (alt83) {
				case 1 :
					// JPA2.g:296:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_in_item2689);
					literal305=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal305.getTree());

					}
					break;
				case 2 :
					// JPA2.g:296:17: single_valued_input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_single_valued_input_parameter_in_in_item2693);
					single_valued_input_parameter306=single_valued_input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_input_parameter306.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:297:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? ;
	public final JPA2Parser.like_expression_return like_expression() throws RecognitionException {
		JPA2Parser.like_expression_return retval = new JPA2Parser.like_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal308=null;
		Token string_literal309=null;
		Token string_literal312=null;
		ParserRuleReturnScope string_expression307 =null;
		ParserRuleReturnScope pattern_value310 =null;
		ParserRuleReturnScope input_parameter311 =null;
		ParserRuleReturnScope escape_character313 =null;

		Object string_literal308_tree=null;
		Object string_literal309_tree=null;
		Object string_literal312_tree=null;

		try {
			// JPA2.g:298:5: ( string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? )
			// JPA2.g:298:7: string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_expression_in_like_expression2704);
			string_expression307=string_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression307.getTree());

			// JPA2.g:298:25: ( 'NOT' )?
			int alt84=2;
			int LA84_0 = input.LA(1);
			if ( (LA84_0==109) ) {
				alt84=1;
			}
			switch (alt84) {
				case 1 :
					// JPA2.g:298:26: 'NOT'
					{
					string_literal308=(Token)match(input,109,FOLLOW_109_in_like_expression2707); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal308_tree = (Object)adaptor.create(string_literal308);
					adaptor.addChild(root_0, string_literal308_tree);
					}

					}
					break;

			}

			string_literal309=(Token)match(input,102,FOLLOW_102_in_like_expression2711); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal309_tree = (Object)adaptor.create(string_literal309);
			adaptor.addChild(root_0, string_literal309_tree);
			}

			// JPA2.g:298:41: ( pattern_value | input_parameter )
			int alt85=2;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==STRING_LITERAL) ) {
				alt85=1;
			}
			else if ( (LA85_0==NAMED_PARAMETER||LA85_0==53||LA85_0==67) ) {
				alt85=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 85, 0, input);
				throw nvae;
			}

			switch (alt85) {
				case 1 :
					// JPA2.g:298:42: pattern_value
					{
					pushFollow(FOLLOW_pattern_value_in_like_expression2714);
					pattern_value310=pattern_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value310.getTree());

					}
					break;
				case 2 :
					// JPA2.g:298:58: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_like_expression2718);
					input_parameter311=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter311.getTree());

					}
					break;

			}

			// JPA2.g:298:74: ( 'ESCAPE' escape_character )?
			int alt86=2;
			int LA86_0 = input.LA(1);
			if ( (LA86_0==91) ) {
				alt86=1;
			}
			switch (alt86) {
				case 1 :
					// JPA2.g:298:75: 'ESCAPE' escape_character
					{
					string_literal312=(Token)match(input,91,FOLLOW_91_in_like_expression2721); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal312_tree = (Object)adaptor.create(string_literal312);
					adaptor.addChild(root_0, string_literal312_tree);
					}

					pushFollow(FOLLOW_escape_character_in_like_expression2723);
					escape_character313=escape_character();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, escape_character313.getTree());

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
	// JPA2.g:299:1: null_comparison_expression : ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' ;
	public final JPA2Parser.null_comparison_expression_return null_comparison_expression() throws RecognitionException {
		JPA2Parser.null_comparison_expression_return retval = new JPA2Parser.null_comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal316=null;
		Token string_literal317=null;
		Token string_literal318=null;
		ParserRuleReturnScope path_expression314 =null;
		ParserRuleReturnScope input_parameter315 =null;

		Object string_literal316_tree=null;
		Object string_literal317_tree=null;
		Object string_literal318_tree=null;

		try {
			// JPA2.g:300:5: ( ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' )
			// JPA2.g:300:7: ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL'
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:300:7: ( path_expression | input_parameter )
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==WORD) ) {
				alt87=1;
			}
			else if ( (LA87_0==NAMED_PARAMETER||LA87_0==53||LA87_0==67) ) {
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
					// JPA2.g:300:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_null_comparison_expression2737);
					path_expression314=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression314.getTree());

					}
					break;
				case 2 :
					// JPA2.g:300:26: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_null_comparison_expression2741);
					input_parameter315=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter315.getTree());

					}
					break;

			}

			string_literal316=(Token)match(input,98,FOLLOW_98_in_null_comparison_expression2744); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal316_tree = (Object)adaptor.create(string_literal316);
			adaptor.addChild(root_0, string_literal316_tree);
			}

			// JPA2.g:300:48: ( 'NOT' )?
			int alt88=2;
			int LA88_0 = input.LA(1);
			if ( (LA88_0==109) ) {
				alt88=1;
			}
			switch (alt88) {
				case 1 :
					// JPA2.g:300:49: 'NOT'
					{
					string_literal317=(Token)match(input,109,FOLLOW_109_in_null_comparison_expression2747); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal317_tree = (Object)adaptor.create(string_literal317);
					adaptor.addChild(root_0, string_literal317_tree);
					}

					}
					break;

			}

			string_literal318=(Token)match(input,111,FOLLOW_111_in_null_comparison_expression2751); if (state.failed) return retval;
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
	// JPA2.g:301:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
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
			// JPA2.g:302:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
			// JPA2.g:302:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression2762);
			path_expression319=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression319.getTree());

			string_literal320=(Token)match(input,98,FOLLOW_98_in_empty_collection_comparison_expression2764); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal320_tree = (Object)adaptor.create(string_literal320);
			adaptor.addChild(root_0, string_literal320_tree);
			}

			// JPA2.g:302:28: ( 'NOT' )?
			int alt89=2;
			int LA89_0 = input.LA(1);
			if ( (LA89_0==109) ) {
				alt89=1;
			}
			switch (alt89) {
				case 1 :
					// JPA2.g:302:29: 'NOT'
					{
					string_literal321=(Token)match(input,109,FOLLOW_109_in_empty_collection_comparison_expression2767); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal321_tree = (Object)adaptor.create(string_literal321);
					adaptor.addChild(root_0, string_literal321_tree);
					}

					}
					break;

			}

			string_literal322=(Token)match(input,88,FOLLOW_88_in_empty_collection_comparison_expression2771); if (state.failed) return retval;
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
	// JPA2.g:303:1: collection_member_expression : entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
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
			// JPA2.g:304:5: ( entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
			// JPA2.g:304:7: entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_or_value_expression_in_collection_member_expression2782);
			entity_or_value_expression323=entity_or_value_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_or_value_expression323.getTree());

			// JPA2.g:304:35: ( 'NOT' )?
			int alt90=2;
			int LA90_0 = input.LA(1);
			if ( (LA90_0==109) ) {
				alt90=1;
			}
			switch (alt90) {
				case 1 :
					// JPA2.g:304:36: 'NOT'
					{
					string_literal324=(Token)match(input,109,FOLLOW_109_in_collection_member_expression2786); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal324_tree = (Object)adaptor.create(string_literal324);
					adaptor.addChild(root_0, string_literal324_tree);
					}

					}
					break;

			}

			string_literal325=(Token)match(input,104,FOLLOW_104_in_collection_member_expression2790); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal325_tree = (Object)adaptor.create(string_literal325);
			adaptor.addChild(root_0, string_literal325_tree);
			}

			// JPA2.g:304:53: ( 'OF' )?
			int alt91=2;
			int LA91_0 = input.LA(1);
			if ( (LA91_0==114) ) {
				alt91=1;
			}
			switch (alt91) {
				case 1 :
					// JPA2.g:304:54: 'OF'
					{
					string_literal326=(Token)match(input,114,FOLLOW_114_in_collection_member_expression2793); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal326_tree = (Object)adaptor.create(string_literal326);
					adaptor.addChild(root_0, string_literal326_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_path_expression_in_collection_member_expression2797);
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
	// JPA2.g:305:1: entity_or_value_expression : ( path_expression | simple_entity_or_value_expression );
	public final JPA2Parser.entity_or_value_expression_return entity_or_value_expression() throws RecognitionException {
		JPA2Parser.entity_or_value_expression_return retval = new JPA2Parser.entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression328 =null;
		ParserRuleReturnScope simple_entity_or_value_expression329 =null;


		try {
			// JPA2.g:306:5: ( path_expression | simple_entity_or_value_expression )
			int alt92=2;
			int LA92_0 = input.LA(1);
			if ( (LA92_0==WORD) ) {
				int LA92_1 = input.LA(2);
				if ( (LA92_1==58) ) {
					alt92=1;
				}
				else if ( (LA92_1==104||LA92_1==109) ) {
					alt92=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 92, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA92_0==NAMED_PARAMETER||LA92_0==53||LA92_0==67) ) {
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
					// JPA2.g:306:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_or_value_expression2808);
					path_expression328=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression328.getTree());

					}
					break;
				case 2 :
					// JPA2.g:307:7: simple_entity_or_value_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2816);
					simple_entity_or_value_expression329=simple_entity_or_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_or_value_expression329.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:308:1: simple_entity_or_value_expression : ( identification_variable | input_parameter | literal );
	public final JPA2Parser.simple_entity_or_value_expression_return simple_entity_or_value_expression() throws RecognitionException {
		JPA2Parser.simple_entity_or_value_expression_return retval = new JPA2Parser.simple_entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable330 =null;
		ParserRuleReturnScope input_parameter331 =null;
		ParserRuleReturnScope literal332 =null;


		try {
			// JPA2.g:309:5: ( identification_variable | input_parameter | literal )
			int alt93=3;
			int LA93_0 = input.LA(1);
			if ( (LA93_0==WORD) ) {
				int LA93_1 = input.LA(2);
				if ( (synpred135_JPA2()) ) {
					alt93=1;
				}
				else if ( (true) ) {
					alt93=3;
				}

			}
			else if ( (LA93_0==NAMED_PARAMETER||LA93_0==53||LA93_0==67) ) {
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
					// JPA2.g:309:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_or_value_expression2827);
					identification_variable330=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable330.getTree());

					}
					break;
				case 2 :
					// JPA2.g:310:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_or_value_expression2835);
					input_parameter331=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter331.getTree());

					}
					break;
				case 3 :
					// JPA2.g:311:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_simple_entity_or_value_expression2843);
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
	// JPA2.g:312:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
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
			// JPA2.g:313:5: ( ( 'NOT' )? 'EXISTS' subquery )
			// JPA2.g:313:7: ( 'NOT' )? 'EXISTS' subquery
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:313:7: ( 'NOT' )?
			int alt94=2;
			int LA94_0 = input.LA(1);
			if ( (LA94_0==109) ) {
				alt94=1;
			}
			switch (alt94) {
				case 1 :
					// JPA2.g:313:8: 'NOT'
					{
					string_literal333=(Token)match(input,109,FOLLOW_109_in_exists_expression2855); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal333_tree = (Object)adaptor.create(string_literal333);
					adaptor.addChild(root_0, string_literal333_tree);
					}

					}
					break;

			}

			string_literal334=(Token)match(input,92,FOLLOW_92_in_exists_expression2859); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal334_tree = (Object)adaptor.create(string_literal334);
			adaptor.addChild(root_0, string_literal334_tree);
			}

			pushFollow(FOLLOW_subquery_in_exists_expression2861);
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
	// JPA2.g:314:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
	public final JPA2Parser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
		JPA2Parser.all_or_any_expression_return retval = new JPA2Parser.all_or_any_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set336=null;
		ParserRuleReturnScope subquery337 =null;

		Object set336_tree=null;

		try {
			// JPA2.g:315:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
			// JPA2.g:315:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
			{
			root_0 = (Object)adaptor.nil();


			set336=input.LT(1);
			if ( (input.LA(1) >= 74 && input.LA(1) <= 75)||input.LA(1)==120 ) {
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
			pushFollow(FOLLOW_subquery_in_all_or_any_expression2885);
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
	// JPA2.g:316:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
	public final JPA2Parser.comparison_expression_return comparison_expression() throws RecognitionException {
		JPA2Parser.comparison_expression_return retval = new JPA2Parser.comparison_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set343=null;
		Token set347=null;
		Token set355=null;
		Token set359=null;
		ParserRuleReturnScope string_expression338 =null;
		ParserRuleReturnScope comparison_operator339 =null;
		ParserRuleReturnScope string_expression340 =null;
		ParserRuleReturnScope all_or_any_expression341 =null;
		ParserRuleReturnScope boolean_expression342 =null;
		ParserRuleReturnScope boolean_expression344 =null;
		ParserRuleReturnScope all_or_any_expression345 =null;
		ParserRuleReturnScope enum_expression346 =null;
		ParserRuleReturnScope enum_expression348 =null;
		ParserRuleReturnScope all_or_any_expression349 =null;
		ParserRuleReturnScope datetime_expression350 =null;
		ParserRuleReturnScope comparison_operator351 =null;
		ParserRuleReturnScope datetime_expression352 =null;
		ParserRuleReturnScope all_or_any_expression353 =null;
		ParserRuleReturnScope entity_expression354 =null;
		ParserRuleReturnScope entity_expression356 =null;
		ParserRuleReturnScope all_or_any_expression357 =null;
		ParserRuleReturnScope entity_type_expression358 =null;
		ParserRuleReturnScope entity_type_expression360 =null;
		ParserRuleReturnScope arithmetic_expression361 =null;
		ParserRuleReturnScope comparison_operator362 =null;
		ParserRuleReturnScope arithmetic_expression363 =null;
		ParserRuleReturnScope all_or_any_expression364 =null;

		Object set343_tree=null;
		Object set347_tree=null;
		Object set355_tree=null;
		Object set359_tree=null;

		try {
			// JPA2.g:317:5: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
			int alt101=7;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA101_1 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred144_JPA2()) ) {
					alt101=2;
				}
				else if ( (synpred147_JPA2()) ) {
					alt101=3;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (synpred152_JPA2()) ) {
					alt101=5;
				}
				else if ( (synpred154_JPA2()) ) {
					alt101=6;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case LOWER:
			case STRING_LITERAL:
			case 81:
			case 122:
			case 126:
			case 129:
				{
				alt101=1;
				}
				break;
			case 67:
				{
				int LA101_3 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred144_JPA2()) ) {
					alt101=2;
				}
				else if ( (synpred147_JPA2()) ) {
					alt101=3;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (synpred152_JPA2()) ) {
					alt101=5;
				}
				else if ( (synpred154_JPA2()) ) {
					alt101=6;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA101_4 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred144_JPA2()) ) {
					alt101=2;
				}
				else if ( (synpred147_JPA2()) ) {
					alt101=3;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (synpred152_JPA2()) ) {
					alt101=5;
				}
				else if ( (synpred154_JPA2()) ) {
					alt101=6;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case 53:
				{
				int LA101_5 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred144_JPA2()) ) {
					alt101=2;
				}
				else if ( (synpred147_JPA2()) ) {
					alt101=3;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (synpred152_JPA2()) ) {
					alt101=5;
				}
				else if ( (synpred154_JPA2()) ) {
					alt101=6;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case COUNT:
				{
				int LA101_11 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA101_12 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case 94:
				{
				int LA101_13 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred144_JPA2()) ) {
					alt101=2;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case 79:
				{
				int LA101_14 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred144_JPA2()) ) {
					alt101=2;
				}
				else if ( (synpred147_JPA2()) ) {
					alt101=3;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case 80:
				{
				int LA101_15 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred144_JPA2()) ) {
					alt101=2;
				}
				else if ( (synpred147_JPA2()) ) {
					alt101=3;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case 112:
				{
				int LA101_16 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred144_JPA2()) ) {
					alt101=2;
				}
				else if ( (synpred147_JPA2()) ) {
					alt101=3;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA101_17 = input.LA(2);
				if ( (synpred141_JPA2()) ) {
					alt101=1;
				}
				else if ( (synpred144_JPA2()) ) {
					alt101=2;
				}
				else if ( (synpred147_JPA2()) ) {
					alt101=3;
				}
				else if ( (synpred149_JPA2()) ) {
					alt101=4;
				}
				else if ( (true) ) {
					alt101=7;
				}

				}
				break;
			case 134:
			case 135:
				{
				alt101=2;
				}
				break;
			case 82:
			case 83:
			case 84:
				{
				alt101=4;
				}
				break;
			case 127:
				{
				alt101=6;
				}
				break;
			case INT_NUMERAL:
			case 55:
			case 57:
			case 60:
			case 73:
			case 97:
			case 101:
			case 103:
			case 106:
			case 119:
			case 121:
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
					// JPA2.g:317:7: string_expression comparison_operator ( string_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_comparison_expression2896);
					string_expression338=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression338.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression2898);
					comparison_operator339=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator339.getTree());

					// JPA2.g:317:45: ( string_expression | all_or_any_expression )
					int alt95=2;
					int LA95_0 = input.LA(1);
					if ( (LA95_0==AVG||LA95_0==COUNT||(LA95_0 >= LOWER && LA95_0 <= NAMED_PARAMETER)||(LA95_0 >= STRING_LITERAL && LA95_0 <= SUM)||LA95_0==WORD||LA95_0==53||LA95_0==67||(LA95_0 >= 79 && LA95_0 <= 81)||LA95_0==94||LA95_0==112||LA95_0==122||LA95_0==126||LA95_0==129) ) {
						alt95=1;
					}
					else if ( ((LA95_0 >= 74 && LA95_0 <= 75)||LA95_0==120) ) {
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
							// JPA2.g:317:46: string_expression
							{
							pushFollow(FOLLOW_string_expression_in_comparison_expression2901);
							string_expression340=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression340.getTree());

							}
							break;
						case 2 :
							// JPA2.g:317:66: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2905);
							all_or_any_expression341=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression341.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// JPA2.g:318:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_comparison_expression2914);
					boolean_expression342=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression342.getTree());

					set343=input.LT(1);
					if ( (input.LA(1) >= 63 && input.LA(1) <= 64) ) {
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
					// JPA2.g:318:39: ( boolean_expression | all_or_any_expression )
					int alt96=2;
					int LA96_0 = input.LA(1);
					if ( (LA96_0==LPAREN||LA96_0==NAMED_PARAMETER||LA96_0==WORD||LA96_0==53||LA96_0==67||(LA96_0 >= 79 && LA96_0 <= 80)||LA96_0==94||LA96_0==112||(LA96_0 >= 134 && LA96_0 <= 135)) ) {
						alt96=1;
					}
					else if ( ((LA96_0 >= 74 && LA96_0 <= 75)||LA96_0==120) ) {
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
							// JPA2.g:318:40: boolean_expression
							{
							pushFollow(FOLLOW_boolean_expression_in_comparison_expression2925);
							boolean_expression344=boolean_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression344.getTree());

							}
							break;
						case 2 :
							// JPA2.g:318:61: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2929);
							all_or_any_expression345=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression345.getTree());

							}
							break;

					}

					}
					break;
				case 3 :
					// JPA2.g:319:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_comparison_expression2938);
					enum_expression346=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression346.getTree());

					set347=input.LT(1);
					if ( (input.LA(1) >= 63 && input.LA(1) <= 64) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set347));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// JPA2.g:319:34: ( enum_expression | all_or_any_expression )
					int alt97=2;
					int LA97_0 = input.LA(1);
					if ( (LA97_0==LPAREN||LA97_0==NAMED_PARAMETER||LA97_0==WORD||LA97_0==53||LA97_0==67||(LA97_0 >= 79 && LA97_0 <= 80)||LA97_0==112) ) {
						alt97=1;
					}
					else if ( ((LA97_0 >= 74 && LA97_0 <= 75)||LA97_0==120) ) {
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
							// JPA2.g:319:35: enum_expression
							{
							pushFollow(FOLLOW_enum_expression_in_comparison_expression2947);
							enum_expression348=enum_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression348.getTree());

							}
							break;
						case 2 :
							// JPA2.g:319:53: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2951);
							all_or_any_expression349=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression349.getTree());

							}
							break;

					}

					}
					break;
				case 4 :
					// JPA2.g:320:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_comparison_expression2960);
					datetime_expression350=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression350.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression2962);
					comparison_operator351=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator351.getTree());

					// JPA2.g:320:47: ( datetime_expression | all_or_any_expression )
					int alt98=2;
					int LA98_0 = input.LA(1);
					if ( (LA98_0==AVG||LA98_0==COUNT||(LA98_0 >= LPAREN && LA98_0 <= NAMED_PARAMETER)||LA98_0==SUM||LA98_0==WORD||LA98_0==53||LA98_0==67||(LA98_0 >= 79 && LA98_0 <= 80)||(LA98_0 >= 82 && LA98_0 <= 84)||LA98_0==94||LA98_0==112) ) {
						alt98=1;
					}
					else if ( ((LA98_0 >= 74 && LA98_0 <= 75)||LA98_0==120) ) {
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
							// JPA2.g:320:48: datetime_expression
							{
							pushFollow(FOLLOW_datetime_expression_in_comparison_expression2965);
							datetime_expression352=datetime_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression352.getTree());

							}
							break;
						case 2 :
							// JPA2.g:320:70: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2969);
							all_or_any_expression353=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression353.getTree());

							}
							break;

					}

					}
					break;
				case 5 :
					// JPA2.g:321:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_expression_in_comparison_expression2978);
					entity_expression354=entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression354.getTree());

					set355=input.LT(1);
					if ( (input.LA(1) >= 63 && input.LA(1) <= 64) ) {
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
					// JPA2.g:321:38: ( entity_expression | all_or_any_expression )
					int alt99=2;
					int LA99_0 = input.LA(1);
					if ( (LA99_0==NAMED_PARAMETER||LA99_0==WORD||LA99_0==53||LA99_0==67) ) {
						alt99=1;
					}
					else if ( ((LA99_0 >= 74 && LA99_0 <= 75)||LA99_0==120) ) {
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
							// JPA2.g:321:39: entity_expression
							{
							pushFollow(FOLLOW_entity_expression_in_comparison_expression2989);
							entity_expression356=entity_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression356.getTree());

							}
							break;
						case 2 :
							// JPA2.g:321:59: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2993);
							all_or_any_expression357=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression357.getTree());

							}
							break;

					}

					}
					break;
				case 6 :
					// JPA2.g:322:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3002);
					entity_type_expression358=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression358.getTree());

					set359=input.LT(1);
					if ( (input.LA(1) >= 63 && input.LA(1) <= 64) ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set359));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression3012);
					entity_type_expression360=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression360.getTree());

					}
					break;
				case 7 :
					// JPA2.g:323:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3020);
					arithmetic_expression361=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression361.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3022);
					comparison_operator362=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator362.getTree());

					// JPA2.g:323:49: ( arithmetic_expression | all_or_any_expression )
					int alt100=2;
					int LA100_0 = input.LA(1);
					if ( (LA100_0==AVG||LA100_0==COUNT||LA100_0==INT_NUMERAL||(LA100_0 >= LPAREN && LA100_0 <= NAMED_PARAMETER)||LA100_0==SUM||LA100_0==WORD||LA100_0==53||LA100_0==55||LA100_0==57||LA100_0==60||LA100_0==67||LA100_0==73||(LA100_0 >= 79 && LA100_0 <= 80)||LA100_0==94||LA100_0==97||LA100_0==101||LA100_0==103||LA100_0==106||LA100_0==112||LA100_0==119||LA100_0==121) ) {
						alt100=1;
					}
					else if ( ((LA100_0 >= 74 && LA100_0 <= 75)||LA100_0==120) ) {
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
							// JPA2.g:323:50: arithmetic_expression
							{
							pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3025);
							arithmetic_expression363=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression363.getTree());

							}
							break;
						case 2 :
							// JPA2.g:323:74: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3029);
							all_or_any_expression364=all_or_any_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, all_or_any_expression364.getTree());

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
	// JPA2.g:325:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
	public final JPA2Parser.comparison_operator_return comparison_operator() throws RecognitionException {
		JPA2Parser.comparison_operator_return retval = new JPA2Parser.comparison_operator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set365=null;

		Object set365_tree=null;

		try {
			// JPA2.g:326:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set365=input.LT(1);
			if ( (input.LA(1) >= 61 && input.LA(1) <= 66) ) {
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
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:332:1: arithmetic_expression : ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term );
	public final JPA2Parser.arithmetic_expression_return arithmetic_expression() throws RecognitionException {
		JPA2Parser.arithmetic_expression_return retval = new JPA2Parser.arithmetic_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set368=null;
		ParserRuleReturnScope arithmetic_term366 =null;
		ParserRuleReturnScope arithmetic_term367 =null;
		ParserRuleReturnScope arithmetic_term369 =null;

		Object set368_tree=null;

		try {
			// JPA2.g:333:5: ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term )
			int alt102=2;
			switch ( input.LA(1) ) {
			case 55:
			case 57:
				{
				int LA102_1 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case WORD:
				{
				int LA102_2 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 60:
				{
				int LA102_3 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
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
				if ( (synpred161_JPA2()) ) {
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
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 67:
				{
				int LA102_6 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
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
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 53:
				{
				int LA102_8 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 101:
				{
				int LA102_9 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 103:
				{
				int LA102_10 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 73:
				{
				int LA102_11 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 121:
				{
				int LA102_12 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 106:
				{
				int LA102_13 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 119:
				{
				int LA102_14 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 97:
				{
				int LA102_15 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
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
				if ( (synpred161_JPA2()) ) {
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
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 94:
				{
				int LA102_18 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 79:
				{
				int LA102_19 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 80:
				{
				int LA102_20 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
					alt102=1;
				}
				else if ( (true) ) {
					alt102=2;
				}

				}
				break;
			case 112:
				{
				int LA102_21 = input.LA(2);
				if ( (synpred161_JPA2()) ) {
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
					// JPA2.g:333:7: arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3093);
					arithmetic_term366=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term366.getTree());

					}
					break;
				case 2 :
					// JPA2.g:334:7: arithmetic_term ( '+' | '-' ) arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3101);
					arithmetic_term367=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term367.getTree());

					set368=input.LT(1);
					if ( input.LA(1)==55||input.LA(1)==57 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set368));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3111);
					arithmetic_term369=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term369.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:335:1: arithmetic_term : ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor );
	public final JPA2Parser.arithmetic_term_return arithmetic_term() throws RecognitionException {
		JPA2Parser.arithmetic_term_return retval = new JPA2Parser.arithmetic_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set372=null;
		ParserRuleReturnScope arithmetic_factor370 =null;
		ParserRuleReturnScope arithmetic_factor371 =null;
		ParserRuleReturnScope arithmetic_factor373 =null;

		Object set372_tree=null;

		try {
			// JPA2.g:336:5: ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor )
			int alt103=2;
			switch ( input.LA(1) ) {
			case 55:
			case 57:
				{
				int LA103_1 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case WORD:
				{
				int LA103_2 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 60:
				{
				int LA103_3 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case INT_NUMERAL:
				{
				int LA103_4 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case LPAREN:
				{
				int LA103_5 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 67:
				{
				int LA103_6 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA103_7 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 53:
				{
				int LA103_8 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 101:
				{
				int LA103_9 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 103:
				{
				int LA103_10 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 73:
				{
				int LA103_11 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 121:
				{
				int LA103_12 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 106:
				{
				int LA103_13 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 119:
				{
				int LA103_14 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 97:
				{
				int LA103_15 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case COUNT:
				{
				int LA103_16 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA103_17 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 94:
				{
				int LA103_18 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 79:
				{
				int LA103_19 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 80:
				{
				int LA103_20 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

				}
				break;
			case 112:
				{
				int LA103_21 = input.LA(2);
				if ( (synpred163_JPA2()) ) {
					alt103=1;
				}
				else if ( (true) ) {
					alt103=2;
				}

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
					// JPA2.g:336:7: arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3122);
					arithmetic_factor370=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor370.getTree());

					}
					break;
				case 2 :
					// JPA2.g:337:7: arithmetic_factor ( '*' | '/' ) arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3130);
					arithmetic_factor371=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor371.getTree());

					set372=input.LT(1);
					if ( input.LA(1)==54||input.LA(1)==59 ) {
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
					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3141);
					arithmetic_factor373=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor373.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:338:1: arithmetic_factor : ( ( '+' | '-' ) )? arithmetic_primary ;
	public final JPA2Parser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
		JPA2Parser.arithmetic_factor_return retval = new JPA2Parser.arithmetic_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set374=null;
		ParserRuleReturnScope arithmetic_primary375 =null;

		Object set374_tree=null;

		try {
			// JPA2.g:339:5: ( ( ( '+' | '-' ) )? arithmetic_primary )
			// JPA2.g:339:7: ( ( '+' | '-' ) )? arithmetic_primary
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:339:7: ( ( '+' | '-' ) )?
			int alt104=2;
			int LA104_0 = input.LA(1);
			if ( (LA104_0==55||LA104_0==57) ) {
				alt104=1;
			}
			switch (alt104) {
				case 1 :
					// JPA2.g:
					{
					set374=input.LT(1);
					if ( input.LA(1)==55||input.LA(1)==57 ) {
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
					}
					break;

			}

			pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor3164);
			arithmetic_primary375=arithmetic_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_primary375.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:340:1: arithmetic_primary : ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | subquery );
	public final JPA2Parser.arithmetic_primary_return arithmetic_primary() throws RecognitionException {
		JPA2Parser.arithmetic_primary_return retval = new JPA2Parser.arithmetic_primary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal378=null;
		Token char_literal380=null;
		ParserRuleReturnScope path_expression376 =null;
		ParserRuleReturnScope numeric_literal377 =null;
		ParserRuleReturnScope arithmetic_expression379 =null;
		ParserRuleReturnScope input_parameter381 =null;
		ParserRuleReturnScope functions_returning_numerics382 =null;
		ParserRuleReturnScope aggregate_expression383 =null;
		ParserRuleReturnScope case_expression384 =null;
		ParserRuleReturnScope function_invocation385 =null;
		ParserRuleReturnScope subquery386 =null;

		Object char_literal378_tree=null;
		Object char_literal380_tree=null;

		try {
			// JPA2.g:341:5: ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | subquery )
			int alt105=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt105=1;
				}
				break;
			case INT_NUMERAL:
			case 60:
				{
				alt105=2;
				}
				break;
			case LPAREN:
				{
				int LA105_4 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt105=3;
				}
				else if ( (true) ) {
					alt105=9;
				}

				}
				break;
			case NAMED_PARAMETER:
			case 53:
			case 67:
				{
				alt105=4;
				}
				break;
			case 73:
			case 97:
			case 101:
			case 103:
			case 106:
			case 119:
			case 121:
				{
				alt105=5;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt105=6;
				}
				break;
			case 94:
				{
				int LA105_17 = input.LA(2);
				if ( (synpred172_JPA2()) ) {
					alt105=6;
				}
				else if ( (synpred174_JPA2()) ) {
					alt105=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 105, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 79:
			case 80:
			case 112:
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
					// JPA2.g:341:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_arithmetic_primary3175);
					path_expression376=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression376.getTree());

					}
					break;
				case 2 :
					// JPA2.g:342:7: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary3183);
					numeric_literal377=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal377.getTree());

					}
					break;
				case 3 :
					// JPA2.g:343:7: '(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal378=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary3191); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal378_tree = (Object)adaptor.create(char_literal378);
					adaptor.addChild(root_0, char_literal378_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_arithmetic_primary3192);
					arithmetic_expression379=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression379.getTree());

					char_literal380=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary3193); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal380_tree = (Object)adaptor.create(char_literal380);
					adaptor.addChild(root_0, char_literal380_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:344:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_arithmetic_primary3201);
					input_parameter381=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter381.getTree());

					}
					break;
				case 5 :
					// JPA2.g:345:7: functions_returning_numerics
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary3209);
					functions_returning_numerics382=functions_returning_numerics();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics382.getTree());

					}
					break;
				case 6 :
					// JPA2.g:346:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary3217);
					aggregate_expression383=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression383.getTree());

					}
					break;
				case 7 :
					// JPA2.g:347:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_arithmetic_primary3225);
					case_expression384=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression384.getTree());

					}
					break;
				case 8 :
					// JPA2.g:348:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_arithmetic_primary3233);
					function_invocation385=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation385.getTree());

					}
					break;
				case 9 :
					// JPA2.g:349:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_arithmetic_primary3241);
					subquery386=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery386.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:350:1: string_expression : ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | subquery );
	public final JPA2Parser.string_expression_return string_expression() throws RecognitionException {
		JPA2Parser.string_expression_return retval = new JPA2Parser.string_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression387 =null;
		ParserRuleReturnScope string_literal388 =null;
		ParserRuleReturnScope input_parameter389 =null;
		ParserRuleReturnScope functions_returning_strings390 =null;
		ParserRuleReturnScope aggregate_expression391 =null;
		ParserRuleReturnScope case_expression392 =null;
		ParserRuleReturnScope function_invocation393 =null;
		ParserRuleReturnScope subquery394 =null;


		try {
			// JPA2.g:351:5: ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | subquery )
			int alt106=8;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt106=1;
				}
				break;
			case STRING_LITERAL:
				{
				alt106=2;
				}
				break;
			case NAMED_PARAMETER:
			case 53:
			case 67:
				{
				alt106=3;
				}
				break;
			case LOWER:
			case 81:
			case 122:
			case 126:
			case 129:
				{
				alt106=4;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt106=5;
				}
				break;
			case 94:
				{
				int LA106_13 = input.LA(2);
				if ( (synpred179_JPA2()) ) {
					alt106=5;
				}
				else if ( (synpred181_JPA2()) ) {
					alt106=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 106, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 79:
			case 80:
			case 112:
				{
				alt106=6;
				}
				break;
			case LPAREN:
				{
				alt106=8;
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
					// JPA2.g:351:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_string_expression3252);
					path_expression387=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression387.getTree());

					}
					break;
				case 2 :
					// JPA2.g:352:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_string_expression3260);
					string_literal388=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal388.getTree());

					}
					break;
				case 3 :
					// JPA2.g:353:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_string_expression3268);
					input_parameter389=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter389.getTree());

					}
					break;
				case 4 :
					// JPA2.g:354:7: functions_returning_strings
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_strings_in_string_expression3276);
					functions_returning_strings390=functions_returning_strings();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings390.getTree());

					}
					break;
				case 5 :
					// JPA2.g:355:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_string_expression3284);
					aggregate_expression391=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression391.getTree());

					}
					break;
				case 6 :
					// JPA2.g:356:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_string_expression3292);
					case_expression392=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression392.getTree());

					}
					break;
				case 7 :
					// JPA2.g:357:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_string_expression3300);
					function_invocation393=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation393.getTree());

					}
					break;
				case 8 :
					// JPA2.g:358:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_string_expression3308);
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
	// $ANTLR end "string_expression"


	public static class datetime_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "datetime_expression"
	// JPA2.g:359:1: datetime_expression : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | date_time_timestamp_literal | subquery );
	public final JPA2Parser.datetime_expression_return datetime_expression() throws RecognitionException {
		JPA2Parser.datetime_expression_return retval = new JPA2Parser.datetime_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression395 =null;
		ParserRuleReturnScope input_parameter396 =null;
		ParserRuleReturnScope functions_returning_datetime397 =null;
		ParserRuleReturnScope aggregate_expression398 =null;
		ParserRuleReturnScope case_expression399 =null;
		ParserRuleReturnScope function_invocation400 =null;
		ParserRuleReturnScope date_time_timestamp_literal401 =null;
		ParserRuleReturnScope subquery402 =null;


		try {
			// JPA2.g:360:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | date_time_timestamp_literal | subquery )
			int alt107=8;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA107_1 = input.LA(2);
				if ( (synpred182_JPA2()) ) {
					alt107=1;
				}
				else if ( (synpred188_JPA2()) ) {
					alt107=7;
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
				break;
			case NAMED_PARAMETER:
			case 53:
			case 67:
				{
				alt107=2;
				}
				break;
			case 82:
			case 83:
			case 84:
				{
				alt107=3;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt107=4;
				}
				break;
			case 94:
				{
				int LA107_8 = input.LA(2);
				if ( (synpred185_JPA2()) ) {
					alt107=4;
				}
				else if ( (synpred187_JPA2()) ) {
					alt107=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 107, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 79:
			case 80:
			case 112:
				{
				alt107=5;
				}
				break;
			case LPAREN:
				{
				alt107=8;
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
					// JPA2.g:360:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_datetime_expression3319);
					path_expression395=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression395.getTree());

					}
					break;
				case 2 :
					// JPA2.g:361:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_datetime_expression3327);
					input_parameter396=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter396.getTree());

					}
					break;
				case 3 :
					// JPA2.g:362:7: functions_returning_datetime
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_datetime_in_datetime_expression3335);
					functions_returning_datetime397=functions_returning_datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime397.getTree());

					}
					break;
				case 4 :
					// JPA2.g:363:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_datetime_expression3343);
					aggregate_expression398=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression398.getTree());

					}
					break;
				case 5 :
					// JPA2.g:364:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_datetime_expression3351);
					case_expression399=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression399.getTree());

					}
					break;
				case 6 :
					// JPA2.g:365:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_datetime_expression3359);
					function_invocation400=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation400.getTree());

					}
					break;
				case 7 :
					// JPA2.g:366:7: date_time_timestamp_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_time_timestamp_literal_in_datetime_expression3367);
					date_time_timestamp_literal401=date_time_timestamp_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_time_timestamp_literal401.getTree());

					}
					break;
				case 8 :
					// JPA2.g:367:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_datetime_expression3375);
					subquery402=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery402.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:368:1: boolean_expression : ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | subquery );
	public final JPA2Parser.boolean_expression_return boolean_expression() throws RecognitionException {
		JPA2Parser.boolean_expression_return retval = new JPA2Parser.boolean_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression403 =null;
		ParserRuleReturnScope boolean_literal404 =null;
		ParserRuleReturnScope input_parameter405 =null;
		ParserRuleReturnScope case_expression406 =null;
		ParserRuleReturnScope function_invocation407 =null;
		ParserRuleReturnScope subquery408 =null;


		try {
			// JPA2.g:369:5: ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | subquery )
			int alt108=6;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt108=1;
				}
				break;
			case 134:
			case 135:
				{
				alt108=2;
				}
				break;
			case NAMED_PARAMETER:
			case 53:
			case 67:
				{
				alt108=3;
				}
				break;
			case 79:
			case 80:
			case 112:
				{
				alt108=4;
				}
				break;
			case 94:
				{
				alt108=5;
				}
				break;
			case LPAREN:
				{
				alt108=6;
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
					// JPA2.g:369:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_boolean_expression3386);
					path_expression403=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression403.getTree());

					}
					break;
				case 2 :
					// JPA2.g:370:7: boolean_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_literal_in_boolean_expression3394);
					boolean_literal404=boolean_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal404.getTree());

					}
					break;
				case 3 :
					// JPA2.g:371:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_boolean_expression3402);
					input_parameter405=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter405.getTree());

					}
					break;
				case 4 :
					// JPA2.g:372:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_boolean_expression3410);
					case_expression406=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression406.getTree());

					}
					break;
				case 5 :
					// JPA2.g:373:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_boolean_expression3418);
					function_invocation407=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation407.getTree());

					}
					break;
				case 6 :
					// JPA2.g:374:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_boolean_expression3426);
					subquery408=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery408.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:375:1: enum_expression : ( path_expression | enum_literal | input_parameter | case_expression | subquery );
	public final JPA2Parser.enum_expression_return enum_expression() throws RecognitionException {
		JPA2Parser.enum_expression_return retval = new JPA2Parser.enum_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression409 =null;
		ParserRuleReturnScope enum_literal410 =null;
		ParserRuleReturnScope input_parameter411 =null;
		ParserRuleReturnScope case_expression412 =null;
		ParserRuleReturnScope subquery413 =null;


		try {
			// JPA2.g:376:5: ( path_expression | enum_literal | input_parameter | case_expression | subquery )
			int alt109=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA109_1 = input.LA(2);
				if ( (LA109_1==58) ) {
					alt109=1;
				}
				else if ( (LA109_1==EOF||LA109_1==AND||(LA109_1 >= GROUP && LA109_1 <= INNER)||(LA109_1 >= JOIN && LA109_1 <= LEFT)||(LA109_1 >= OR && LA109_1 <= ORDER)||LA109_1==RPAREN||LA109_1==WORD||LA109_1==56||(LA109_1 >= 63 && LA109_1 <= 64)||LA109_1==76||LA109_1==87||LA109_1==89||LA109_1==93||LA109_1==123||(LA109_1 >= 131 && LA109_1 <= 132)) ) {
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
			case NAMED_PARAMETER:
			case 53:
			case 67:
				{
				alt109=3;
				}
				break;
			case 79:
			case 80:
			case 112:
				{
				alt109=4;
				}
				break;
			case LPAREN:
				{
				alt109=5;
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
					// JPA2.g:376:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_enum_expression3437);
					path_expression409=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression409.getTree());

					}
					break;
				case 2 :
					// JPA2.g:377:7: enum_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_literal_in_enum_expression3445);
					enum_literal410=enum_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal410.getTree());

					}
					break;
				case 3 :
					// JPA2.g:378:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_enum_expression3453);
					input_parameter411=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter411.getTree());

					}
					break;
				case 4 :
					// JPA2.g:379:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_enum_expression3461);
					case_expression412=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression412.getTree());

					}
					break;
				case 5 :
					// JPA2.g:380:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_enum_expression3469);
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
	// $ANTLR end "enum_expression"


	public static class entity_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "entity_expression"
	// JPA2.g:381:1: entity_expression : ( path_expression | simple_entity_expression );
	public final JPA2Parser.entity_expression_return entity_expression() throws RecognitionException {
		JPA2Parser.entity_expression_return retval = new JPA2Parser.entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression414 =null;
		ParserRuleReturnScope simple_entity_expression415 =null;


		try {
			// JPA2.g:382:5: ( path_expression | simple_entity_expression )
			int alt110=2;
			int LA110_0 = input.LA(1);
			if ( (LA110_0==WORD) ) {
				int LA110_1 = input.LA(2);
				if ( (LA110_1==58) ) {
					alt110=1;
				}
				else if ( (LA110_1==EOF||LA110_1==AND||(LA110_1 >= GROUP && LA110_1 <= INNER)||(LA110_1 >= JOIN && LA110_1 <= LEFT)||(LA110_1 >= OR && LA110_1 <= ORDER)||LA110_1==RPAREN||LA110_1==56||(LA110_1 >= 63 && LA110_1 <= 64)||LA110_1==123||LA110_1==132) ) {
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
			else if ( (LA110_0==NAMED_PARAMETER||LA110_0==53||LA110_0==67) ) {
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
					// JPA2.g:382:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_expression3480);
					path_expression414=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression414.getTree());

					}
					break;
				case 2 :
					// JPA2.g:383:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3488);
					simple_entity_expression415=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression415.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:384:1: simple_entity_expression : ( identification_variable | input_parameter );
	public final JPA2Parser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
		JPA2Parser.simple_entity_expression_return retval = new JPA2Parser.simple_entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable416 =null;
		ParserRuleReturnScope input_parameter417 =null;


		try {
			// JPA2.g:385:5: ( identification_variable | input_parameter )
			int alt111=2;
			int LA111_0 = input.LA(1);
			if ( (LA111_0==WORD) ) {
				alt111=1;
			}
			else if ( (LA111_0==NAMED_PARAMETER||LA111_0==53||LA111_0==67) ) {
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
					// JPA2.g:385:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3499);
					identification_variable416=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable416.getTree());

					}
					break;
				case 2 :
					// JPA2.g:386:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3507);
					input_parameter417=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter417.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:387:1: entity_type_expression : ( type_discriminator | entity_type_literal | input_parameter );
	public final JPA2Parser.entity_type_expression_return entity_type_expression() throws RecognitionException {
		JPA2Parser.entity_type_expression_return retval = new JPA2Parser.entity_type_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope type_discriminator418 =null;
		ParserRuleReturnScope entity_type_literal419 =null;
		ParserRuleReturnScope input_parameter420 =null;


		try {
			// JPA2.g:388:5: ( type_discriminator | entity_type_literal | input_parameter )
			int alt112=3;
			switch ( input.LA(1) ) {
			case 127:
				{
				alt112=1;
				}
				break;
			case WORD:
				{
				alt112=2;
				}
				break;
			case NAMED_PARAMETER:
			case 53:
			case 67:
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
					// JPA2.g:388:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_entity_type_expression3518);
					type_discriminator418=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator418.getTree());

					}
					break;
				case 2 :
					// JPA2.g:389:7: entity_type_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_literal_in_entity_type_expression3526);
					entity_type_literal419=entity_type_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_literal419.getTree());

					}
					break;
				case 3 :
					// JPA2.g:390:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_entity_type_expression3534);
					input_parameter420=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter420.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// JPA2.g:391:1: type_discriminator : 'TYPE' ( general_identification_variable | path_expression | input_parameter ) ;
	public final JPA2Parser.type_discriminator_return type_discriminator() throws RecognitionException {
		JPA2Parser.type_discriminator_return retval = new JPA2Parser.type_discriminator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal421=null;
		ParserRuleReturnScope general_identification_variable422 =null;
		ParserRuleReturnScope path_expression423 =null;
		ParserRuleReturnScope input_parameter424 =null;

		Object string_literal421_tree=null;

		try {
			// JPA2.g:392:5: ( 'TYPE' ( general_identification_variable | path_expression | input_parameter ) )
			// JPA2.g:392:7: 'TYPE' ( general_identification_variable | path_expression | input_parameter )
			{
			root_0 = (Object)adaptor.nil();


			string_literal421=(Token)match(input,127,FOLLOW_127_in_type_discriminator3545); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal421_tree = (Object)adaptor.create(string_literal421);
			adaptor.addChild(root_0, string_literal421_tree);
			}

			// JPA2.g:392:13: ( general_identification_variable | path_expression | input_parameter )
			int alt113=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA113_1 = input.LA(2);
				if ( (LA113_1==EOF||LA113_1==AND||(LA113_1 >= GROUP && LA113_1 <= INNER)||(LA113_1 >= JOIN && LA113_1 <= LEFT)||(LA113_1 >= OR && LA113_1 <= ORDER)||LA113_1==RPAREN||LA113_1==WORD||LA113_1==56||(LA113_1 >= 63 && LA113_1 <= 64)||LA113_1==76||LA113_1==87||LA113_1==89||LA113_1==93||LA113_1==96||LA113_1==109||LA113_1==123||(LA113_1 >= 131 && LA113_1 <= 132)) ) {
					alt113=1;
				}
				else if ( (LA113_1==58) ) {
					alt113=2;
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
			case 99:
			case 130:
				{
				alt113=1;
				}
				break;
			case NAMED_PARAMETER:
			case 53:
			case 67:
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
					// JPA2.g:392:14: general_identification_variable
					{
					pushFollow(FOLLOW_general_identification_variable_in_type_discriminator3547);
					general_identification_variable422=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable422.getTree());

					}
					break;
				case 2 :
					// JPA2.g:392:48: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_type_discriminator3551);
					path_expression423=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression423.getTree());

					}
					break;
				case 3 :
					// JPA2.g:392:66: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_type_discriminator3555);
					input_parameter424=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter424.getTree());

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
	// $ANTLR end "type_discriminator"


	public static class functions_returning_numerics_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "functions_returning_numerics"
	// JPA2.g:393:1: functions_returning_numerics : ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' );
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
			// JPA2.g:394:5: ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' )
			int alt115=7;
			switch ( input.LA(1) ) {
			case 101:
				{
				alt115=1;
				}
				break;
			case 103:
				{
				alt115=2;
				}
				break;
			case 73:
				{
				alt115=3;
				}
				break;
			case 121:
				{
				alt115=4;
				}
				break;
			case 106:
				{
				alt115=5;
				}
				break;
			case 119:
				{
				alt115=6;
				}
				break;
			case 97:
				{
				alt115=7;
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
					// JPA2.g:394:7: 'LENGTH(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal425=(Token)match(input,101,FOLLOW_101_in_functions_returning_numerics3567); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal425_tree = (Object)adaptor.create(string_literal425);
					adaptor.addChild(root_0, string_literal425_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3568);
					string_expression426=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression426.getTree());

					char_literal427=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3569); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal427_tree = (Object)adaptor.create(char_literal427);
					adaptor.addChild(root_0, char_literal427_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:395:7: 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal428=(Token)match(input,103,FOLLOW_103_in_functions_returning_numerics3577); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal428_tree = (Object)adaptor.create(string_literal428);
					adaptor.addChild(root_0, string_literal428_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3579);
					string_expression429=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression429.getTree());

					char_literal430=(Token)match(input,56,FOLLOW_56_in_functions_returning_numerics3580); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal430_tree = (Object)adaptor.create(char_literal430);
					adaptor.addChild(root_0, char_literal430_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3582);
					string_expression431=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression431.getTree());

					// JPA2.g:395:55: ( ',' arithmetic_expression )?
					int alt114=2;
					int LA114_0 = input.LA(1);
					if ( (LA114_0==56) ) {
						alt114=1;
					}
					switch (alt114) {
						case 1 :
							// JPA2.g:395:56: ',' arithmetic_expression
							{
							char_literal432=(Token)match(input,56,FOLLOW_56_in_functions_returning_numerics3584); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal432_tree = (Object)adaptor.create(char_literal432);
							adaptor.addChild(root_0, char_literal432_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3585);
							arithmetic_expression433=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression433.getTree());

							}
							break;

					}

					char_literal434=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3588); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal434_tree = (Object)adaptor.create(char_literal434);
					adaptor.addChild(root_0, char_literal434_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:396:7: 'ABS(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal435=(Token)match(input,73,FOLLOW_73_in_functions_returning_numerics3596); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal435_tree = (Object)adaptor.create(string_literal435);
					adaptor.addChild(root_0, string_literal435_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3597);
					arithmetic_expression436=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression436.getTree());

					char_literal437=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3598); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal437_tree = (Object)adaptor.create(char_literal437);
					adaptor.addChild(root_0, char_literal437_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:397:7: 'SQRT(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal438=(Token)match(input,121,FOLLOW_121_in_functions_returning_numerics3606); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal438_tree = (Object)adaptor.create(string_literal438);
					adaptor.addChild(root_0, string_literal438_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3607);
					arithmetic_expression439=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression439.getTree());

					char_literal440=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3608); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal440_tree = (Object)adaptor.create(char_literal440);
					adaptor.addChild(root_0, char_literal440_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:398:7: 'MOD(' arithmetic_expression ',' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal441=(Token)match(input,106,FOLLOW_106_in_functions_returning_numerics3616); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal441_tree = (Object)adaptor.create(string_literal441);
					adaptor.addChild(root_0, string_literal441_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3617);
					arithmetic_expression442=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression442.getTree());

					char_literal443=(Token)match(input,56,FOLLOW_56_in_functions_returning_numerics3618); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal443_tree = (Object)adaptor.create(char_literal443);
					adaptor.addChild(root_0, char_literal443_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3620);
					arithmetic_expression444=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression444.getTree());

					char_literal445=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3621); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal445_tree = (Object)adaptor.create(char_literal445);
					adaptor.addChild(root_0, char_literal445_tree);
					}

					}
					break;
				case 6 :
					// JPA2.g:399:7: 'SIZE(' path_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal446=(Token)match(input,119,FOLLOW_119_in_functions_returning_numerics3629); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal446_tree = (Object)adaptor.create(string_literal446);
					adaptor.addChild(root_0, string_literal446_tree);
					}

					pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3630);
					path_expression447=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression447.getTree());

					char_literal448=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3631); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal448_tree = (Object)adaptor.create(char_literal448);
					adaptor.addChild(root_0, char_literal448_tree);
					}

					}
					break;
				case 7 :
					// JPA2.g:400:7: 'INDEX(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal449=(Token)match(input,97,FOLLOW_97_in_functions_returning_numerics3639); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal449_tree = (Object)adaptor.create(string_literal449);
					adaptor.addChild(root_0, string_literal449_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_functions_returning_numerics3640);
					identification_variable450=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable450.getTree());

					char_literal451=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3641); if (state.failed) return retval;
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
	// JPA2.g:401:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
	public final JPA2Parser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
		JPA2Parser.functions_returning_datetime_return retval = new JPA2Parser.functions_returning_datetime_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set452=null;

		Object set452_tree=null;

		try {
			// JPA2.g:402:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set452=input.LT(1);
			if ( (input.LA(1) >= 82 && input.LA(1) <= 84) ) {
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
	// JPA2.g:405:1: functions_returning_strings : ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' );
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
			// JPA2.g:406:5: ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER' '(' string_expression ')' | 'UPPER(' string_expression ')' )
			int alt121=5;
			switch ( input.LA(1) ) {
			case 81:
				{
				alt121=1;
				}
				break;
			case 122:
				{
				alt121=2;
				}
				break;
			case 126:
				{
				alt121=3;
				}
				break;
			case LOWER:
				{
				alt121=4;
				}
				break;
			case 129:
				{
				alt121=5;
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
					// JPA2.g:406:7: 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal453=(Token)match(input,81,FOLLOW_81_in_functions_returning_strings3679); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal453_tree = (Object)adaptor.create(string_literal453);
					adaptor.addChild(root_0, string_literal453_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3680);
					string_expression454=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression454.getTree());

					char_literal455=(Token)match(input,56,FOLLOW_56_in_functions_returning_strings3681); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal455_tree = (Object)adaptor.create(char_literal455);
					adaptor.addChild(root_0, char_literal455_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3683);
					string_expression456=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression456.getTree());

					// JPA2.g:406:55: ( ',' string_expression )*
					loop116:
					while (true) {
						int alt116=2;
						int LA116_0 = input.LA(1);
						if ( (LA116_0==56) ) {
							alt116=1;
						}

						switch (alt116) {
						case 1 :
							// JPA2.g:406:56: ',' string_expression
							{
							char_literal457=(Token)match(input,56,FOLLOW_56_in_functions_returning_strings3686); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal457_tree = (Object)adaptor.create(char_literal457);
							adaptor.addChild(root_0, char_literal457_tree);
							}

							pushFollow(FOLLOW_string_expression_in_functions_returning_strings3688);
							string_expression458=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression458.getTree());

							}
							break;

						default :
							break loop116;
						}
					}

					char_literal459=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3691); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal459_tree = (Object)adaptor.create(char_literal459);
					adaptor.addChild(root_0, char_literal459_tree);
					}

					}
					break;
				case 2 :
					// JPA2.g:407:7: 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal460=(Token)match(input,122,FOLLOW_122_in_functions_returning_strings3699); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal460_tree = (Object)adaptor.create(string_literal460);
					adaptor.addChild(root_0, string_literal460_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3701);
					string_expression461=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression461.getTree());

					char_literal462=(Token)match(input,56,FOLLOW_56_in_functions_returning_strings3702); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal462_tree = (Object)adaptor.create(char_literal462);
					adaptor.addChild(root_0, char_literal462_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3704);
					arithmetic_expression463=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression463.getTree());

					// JPA2.g:407:63: ( ',' arithmetic_expression )?
					int alt117=2;
					int LA117_0 = input.LA(1);
					if ( (LA117_0==56) ) {
						alt117=1;
					}
					switch (alt117) {
						case 1 :
							// JPA2.g:407:64: ',' arithmetic_expression
							{
							char_literal464=(Token)match(input,56,FOLLOW_56_in_functions_returning_strings3707); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal464_tree = (Object)adaptor.create(char_literal464);
							adaptor.addChild(root_0, char_literal464_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3709);
							arithmetic_expression465=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression465.getTree());

							}
							break;

					}

					char_literal466=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3712); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal466_tree = (Object)adaptor.create(char_literal466);
					adaptor.addChild(root_0, char_literal466_tree);
					}

					}
					break;
				case 3 :
					// JPA2.g:408:7: 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal467=(Token)match(input,126,FOLLOW_126_in_functions_returning_strings3720); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal467_tree = (Object)adaptor.create(string_literal467);
					adaptor.addChild(root_0, string_literal467_tree);
					}

					// JPA2.g:408:14: ( ( trim_specification )? ( trim_character )? 'FROM' )?
					int alt120=2;
					int LA120_0 = input.LA(1);
					if ( (LA120_0==TRIM_CHARACTER||LA120_0==78||LA120_0==93||LA120_0==100||LA120_0==124) ) {
						alt120=1;
					}
					switch (alt120) {
						case 1 :
							// JPA2.g:408:15: ( trim_specification )? ( trim_character )? 'FROM'
							{
							// JPA2.g:408:15: ( trim_specification )?
							int alt118=2;
							int LA118_0 = input.LA(1);
							if ( (LA118_0==78||LA118_0==100||LA118_0==124) ) {
								alt118=1;
							}
							switch (alt118) {
								case 1 :
									// JPA2.g:408:16: trim_specification
									{
									pushFollow(FOLLOW_trim_specification_in_functions_returning_strings3723);
									trim_specification468=trim_specification();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification468.getTree());

									}
									break;

							}

							// JPA2.g:408:37: ( trim_character )?
							int alt119=2;
							int LA119_0 = input.LA(1);
							if ( (LA119_0==TRIM_CHARACTER) ) {
								alt119=1;
							}
							switch (alt119) {
								case 1 :
									// JPA2.g:408:38: trim_character
									{
									pushFollow(FOLLOW_trim_character_in_functions_returning_strings3728);
									trim_character469=trim_character();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_character469.getTree());

									}
									break;

							}

							string_literal470=(Token)match(input,93,FOLLOW_93_in_functions_returning_strings3732); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal470_tree = (Object)adaptor.create(string_literal470);
							adaptor.addChild(root_0, string_literal470_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3736);
					string_expression471=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression471.getTree());

					char_literal472=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3738); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal472_tree = (Object)adaptor.create(char_literal472);
					adaptor.addChild(root_0, char_literal472_tree);
					}

					}
					break;
				case 4 :
					// JPA2.g:409:7: 'LOWER' '(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal473=(Token)match(input,LOWER,FOLLOW_LOWER_in_functions_returning_strings3746); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal473_tree = (Object)adaptor.create(string_literal473);
					adaptor.addChild(root_0, string_literal473_tree);
					}

					char_literal474=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_functions_returning_strings3748); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal474_tree = (Object)adaptor.create(char_literal474);
					adaptor.addChild(root_0, char_literal474_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3749);
					string_expression475=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression475.getTree());

					char_literal476=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3750); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal476_tree = (Object)adaptor.create(char_literal476);
					adaptor.addChild(root_0, char_literal476_tree);
					}

					}
					break;
				case 5 :
					// JPA2.g:410:7: 'UPPER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal477=(Token)match(input,129,FOLLOW_129_in_functions_returning_strings3758); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal477_tree = (Object)adaptor.create(string_literal477);
					adaptor.addChild(root_0, string_literal477_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3759);
					string_expression478=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression478.getTree());

					char_literal479=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3760); if (state.failed) return retval;
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
	// JPA2.g:411:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
	public final JPA2Parser.trim_specification_return trim_specification() throws RecognitionException {
		JPA2Parser.trim_specification_return retval = new JPA2Parser.trim_specification_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set480=null;

		Object set480_tree=null;

		try {
			// JPA2.g:412:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
			// JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set480=input.LT(1);
			if ( input.LA(1)==78||input.LA(1)==100||input.LA(1)==124 ) {
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
	// JPA2.g:413:1: function_invocation : 'FUNCTION(' function_name ( ',' function_arg )* ')' ;
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
			// JPA2.g:414:5: ( 'FUNCTION(' function_name ( ',' function_arg )* ')' )
			// JPA2.g:414:7: 'FUNCTION(' function_name ( ',' function_arg )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal481=(Token)match(input,94,FOLLOW_94_in_function_invocation3790); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal481_tree = (Object)adaptor.create(string_literal481);
			adaptor.addChild(root_0, string_literal481_tree);
			}

			pushFollow(FOLLOW_function_name_in_function_invocation3791);
			function_name482=function_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_name482.getTree());

			// JPA2.g:414:32: ( ',' function_arg )*
			loop122:
			while (true) {
				int alt122=2;
				int LA122_0 = input.LA(1);
				if ( (LA122_0==56) ) {
					alt122=1;
				}

				switch (alt122) {
				case 1 :
					// JPA2.g:414:33: ',' function_arg
					{
					char_literal483=(Token)match(input,56,FOLLOW_56_in_function_invocation3794); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal483_tree = (Object)adaptor.create(char_literal483);
					adaptor.addChild(root_0, char_literal483_tree);
					}

					pushFollow(FOLLOW_function_arg_in_function_invocation3796);
					function_arg484=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg484.getTree());

					}
					break;

				default :
					break loop122;
				}
			}

			char_literal485=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function_invocation3800); if (state.failed) return retval;
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
	// JPA2.g:415:1: function_arg : ( literal | path_expression | input_parameter | scalar_expression );
	public final JPA2Parser.function_arg_return function_arg() throws RecognitionException {
		JPA2Parser.function_arg_return retval = new JPA2Parser.function_arg_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal486 =null;
		ParserRuleReturnScope path_expression487 =null;
		ParserRuleReturnScope input_parameter488 =null;
		ParserRuleReturnScope scalar_expression489 =null;


		try {
			// JPA2.g:416:5: ( literal | path_expression | input_parameter | scalar_expression )
			int alt123=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA123_1 = input.LA(2);
				if ( (LA123_1==58) ) {
					alt123=2;
				}
				else if ( (synpred225_JPA2()) ) {
					alt123=1;
				}
				else if ( (true) ) {
					alt123=4;
				}

				}
				break;
			case 67:
				{
				int LA123_2 = input.LA(2);
				if ( (LA123_2==60) ) {
					int LA123_8 = input.LA(3);
					if ( (LA123_8==INT_NUMERAL) ) {
						int LA123_12 = input.LA(4);
						if ( (synpred227_JPA2()) ) {
							alt123=3;
						}
						else if ( (true) ) {
							alt123=4;
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
								new NoViableAltException("", 123, 8, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA123_2==INT_NUMERAL) ) {
					int LA123_9 = input.LA(3);
					if ( (synpred227_JPA2()) ) {
						alt123=3;
					}
					else if ( (true) ) {
						alt123=4;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 123, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA123_3 = input.LA(2);
				if ( (synpred227_JPA2()) ) {
					alt123=3;
				}
				else if ( (true) ) {
					alt123=4;
				}

				}
				break;
			case 53:
				{
				int LA123_4 = input.LA(2);
				if ( (LA123_4==WORD) ) {
					int LA123_11 = input.LA(3);
					if ( (LA123_11==136) ) {
						int LA123_13 = input.LA(4);
						if ( (synpred227_JPA2()) ) {
							alt123=3;
						}
						else if ( (true) ) {
							alt123=4;
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
								new NoViableAltException("", 123, 11, input);
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
							new NoViableAltException("", 123, 4, input);
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
			case 55:
			case 57:
			case 60:
			case 73:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 84:
			case 94:
			case 97:
			case 101:
			case 103:
			case 106:
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
					// JPA2.g:416:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_function_arg3811);
					literal486=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal486.getTree());

					}
					break;
				case 2 :
					// JPA2.g:417:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_function_arg3819);
					path_expression487=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression487.getTree());

					}
					break;
				case 3 :
					// JPA2.g:418:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_function_arg3827);
					input_parameter488=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter488.getTree());

					}
					break;
				case 4 :
					// JPA2.g:419:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_function_arg3835);
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
	// JPA2.g:420:1: case_expression : ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression );
	public final JPA2Parser.case_expression_return case_expression() throws RecognitionException {
		JPA2Parser.case_expression_return retval = new JPA2Parser.case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope general_case_expression490 =null;
		ParserRuleReturnScope simple_case_expression491 =null;
		ParserRuleReturnScope coalesce_expression492 =null;
		ParserRuleReturnScope nullif_expression493 =null;


		try {
			// JPA2.g:421:5: ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression )
			int alt124=4;
			switch ( input.LA(1) ) {
			case 79:
				{
				int LA124_1 = input.LA(2);
				if ( (LA124_1==131) ) {
					alt124=1;
				}
				else if ( (LA124_1==WORD||LA124_1==127) ) {
					alt124=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 124, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 80:
				{
				alt124=3;
				}
				break;
			case 112:
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
					// JPA2.g:421:7: general_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_case_expression_in_case_expression3846);
					general_case_expression490=general_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_case_expression490.getTree());

					}
					break;
				case 2 :
					// JPA2.g:422:7: simple_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_case_expression_in_case_expression3854);
					simple_case_expression491=simple_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_case_expression491.getTree());

					}
					break;
				case 3 :
					// JPA2.g:423:7: coalesce_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_coalesce_expression_in_case_expression3862);
					coalesce_expression492=coalesce_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, coalesce_expression492.getTree());

					}
					break;
				case 4 :
					// JPA2.g:424:7: nullif_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_nullif_expression_in_case_expression3870);
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
	// JPA2.g:425:1: general_case_expression : 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' ;
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
			// JPA2.g:426:5: ( 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:426:7: 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal494=(Token)match(input,79,FOLLOW_79_in_general_case_expression3881); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal494_tree = (Object)adaptor.create(string_literal494);
			adaptor.addChild(root_0, string_literal494_tree);
			}

			pushFollow(FOLLOW_when_clause_in_general_case_expression3883);
			when_clause495=when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause495.getTree());

			// JPA2.g:426:26: ( when_clause )*
			loop125:
			while (true) {
				int alt125=2;
				int LA125_0 = input.LA(1);
				if ( (LA125_0==131) ) {
					alt125=1;
				}

				switch (alt125) {
				case 1 :
					// JPA2.g:426:27: when_clause
					{
					pushFollow(FOLLOW_when_clause_in_general_case_expression3886);
					when_clause496=when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause496.getTree());

					}
					break;

				default :
					break loop125;
				}
			}

			string_literal497=(Token)match(input,87,FOLLOW_87_in_general_case_expression3890); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal497_tree = (Object)adaptor.create(string_literal497);
			adaptor.addChild(root_0, string_literal497_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_general_case_expression3892);
			scalar_expression498=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression498.getTree());

			string_literal499=(Token)match(input,89,FOLLOW_89_in_general_case_expression3894); if (state.failed) return retval;
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
	// JPA2.g:427:1: when_clause : 'WHEN' conditional_expression 'THEN' scalar_expression ;
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
			// JPA2.g:428:5: ( 'WHEN' conditional_expression 'THEN' scalar_expression )
			// JPA2.g:428:7: 'WHEN' conditional_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal500=(Token)match(input,131,FOLLOW_131_in_when_clause3905); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal500_tree = (Object)adaptor.create(string_literal500);
			adaptor.addChild(root_0, string_literal500_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_when_clause3907);
			conditional_expression501=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression501.getTree());

			string_literal502=(Token)match(input,123,FOLLOW_123_in_when_clause3909); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal502_tree = (Object)adaptor.create(string_literal502);
			adaptor.addChild(root_0, string_literal502_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_when_clause3911);
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
	// JPA2.g:429:1: simple_case_expression : 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' ;
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
			// JPA2.g:430:5: ( 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' )
			// JPA2.g:430:7: 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal504=(Token)match(input,79,FOLLOW_79_in_simple_case_expression3922); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal504_tree = (Object)adaptor.create(string_literal504);
			adaptor.addChild(root_0, string_literal504_tree);
			}

			pushFollow(FOLLOW_case_operand_in_simple_case_expression3924);
			case_operand505=case_operand();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, case_operand505.getTree());

			pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression3926);
			simple_when_clause506=simple_when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause506.getTree());

			// JPA2.g:430:46: ( simple_when_clause )*
			loop126:
			while (true) {
				int alt126=2;
				int LA126_0 = input.LA(1);
				if ( (LA126_0==131) ) {
					alt126=1;
				}

				switch (alt126) {
				case 1 :
					// JPA2.g:430:47: simple_when_clause
					{
					pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression3929);
					simple_when_clause507=simple_when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause507.getTree());

					}
					break;

				default :
					break loop126;
				}
			}

			string_literal508=(Token)match(input,87,FOLLOW_87_in_simple_case_expression3933); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal508_tree = (Object)adaptor.create(string_literal508);
			adaptor.addChild(root_0, string_literal508_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_case_expression3935);
			scalar_expression509=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression509.getTree());

			string_literal510=(Token)match(input,89,FOLLOW_89_in_simple_case_expression3937); if (state.failed) return retval;
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
	// JPA2.g:431:1: case_operand : ( path_expression | type_discriminator );
	public final JPA2Parser.case_operand_return case_operand() throws RecognitionException {
		JPA2Parser.case_operand_return retval = new JPA2Parser.case_operand_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression511 =null;
		ParserRuleReturnScope type_discriminator512 =null;


		try {
			// JPA2.g:432:5: ( path_expression | type_discriminator )
			int alt127=2;
			int LA127_0 = input.LA(1);
			if ( (LA127_0==WORD) ) {
				alt127=1;
			}
			else if ( (LA127_0==127) ) {
				alt127=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 127, 0, input);
				throw nvae;
			}

			switch (alt127) {
				case 1 :
					// JPA2.g:432:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_case_operand3948);
					path_expression511=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression511.getTree());

					}
					break;
				case 2 :
					// JPA2.g:433:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_case_operand3956);
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
	// JPA2.g:434:1: simple_when_clause : 'WHEN' scalar_expression 'THEN' scalar_expression ;
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
			// JPA2.g:435:5: ( 'WHEN' scalar_expression 'THEN' scalar_expression )
			// JPA2.g:435:7: 'WHEN' scalar_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal513=(Token)match(input,131,FOLLOW_131_in_simple_when_clause3967); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal513_tree = (Object)adaptor.create(string_literal513);
			adaptor.addChild(root_0, string_literal513_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause3969);
			scalar_expression514=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression514.getTree());

			string_literal515=(Token)match(input,123,FOLLOW_123_in_simple_when_clause3971); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal515_tree = (Object)adaptor.create(string_literal515);
			adaptor.addChild(root_0, string_literal515_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause3973);
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
	// JPA2.g:436:1: coalesce_expression : 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' ;
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
			// JPA2.g:437:5: ( 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' )
			// JPA2.g:437:7: 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal517=(Token)match(input,80,FOLLOW_80_in_coalesce_expression3984); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal517_tree = (Object)adaptor.create(string_literal517);
			adaptor.addChild(root_0, string_literal517_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_coalesce_expression3985);
			scalar_expression518=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression518.getTree());

			// JPA2.g:437:36: ( ',' scalar_expression )+
			int cnt128=0;
			loop128:
			while (true) {
				int alt128=2;
				int LA128_0 = input.LA(1);
				if ( (LA128_0==56) ) {
					alt128=1;
				}

				switch (alt128) {
				case 1 :
					// JPA2.g:437:37: ',' scalar_expression
					{
					char_literal519=(Token)match(input,56,FOLLOW_56_in_coalesce_expression3988); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal519_tree = (Object)adaptor.create(char_literal519);
					adaptor.addChild(root_0, char_literal519_tree);
					}

					pushFollow(FOLLOW_scalar_expression_in_coalesce_expression3990);
					scalar_expression520=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression520.getTree());

					}
					break;

				default :
					if ( cnt128 >= 1 ) break loop128;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(128, input);
					throw eee;
				}
				cnt128++;
			}

			char_literal521=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_coalesce_expression3993); if (state.failed) return retval;
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
	// JPA2.g:438:1: nullif_expression : 'NULLIF(' scalar_expression ',' scalar_expression ')' ;
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
			// JPA2.g:439:5: ( 'NULLIF(' scalar_expression ',' scalar_expression ')' )
			// JPA2.g:439:7: 'NULLIF(' scalar_expression ',' scalar_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal522=(Token)match(input,112,FOLLOW_112_in_nullif_expression4004); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal522_tree = (Object)adaptor.create(string_literal522);
			adaptor.addChild(root_0, string_literal522_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4005);
			scalar_expression523=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression523.getTree());

			char_literal524=(Token)match(input,56,FOLLOW_56_in_nullif_expression4007); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal524_tree = (Object)adaptor.create(char_literal524);
			adaptor.addChild(root_0, char_literal524_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression4009);
			scalar_expression525=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression525.getTree());

			char_literal526=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nullif_expression4010); if (state.failed) return retval;
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
	// JPA2.g:442:1: input_parameter : ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) );
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
		RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
		RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
		RewriteRuleTokenStream stream_136=new RewriteRuleTokenStream(adaptor,"token 136");
		RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
		RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
		RewriteRuleSubtreeStream stream_numeric_literal=new RewriteRuleSubtreeStream(adaptor,"rule numeric_literal");

		try {
			// JPA2.g:443:5: ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
			int alt129=3;
			switch ( input.LA(1) ) {
			case 67:
				{
				alt129=1;
				}
				break;
			case NAMED_PARAMETER:
				{
				alt129=2;
				}
				break;
			case 53:
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
					// JPA2.g:443:7: '?' numeric_literal
					{
					char_literal527=(Token)match(input,67,FOLLOW_67_in_input_parameter4023); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_67.add(char_literal527);

					pushFollow(FOLLOW_numeric_literal_in_input_parameter4025);
					numeric_literal528=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_literal.add(numeric_literal528.getTree());
					// AST REWRITE
					// elements: 67, numeric_literal
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 443:27: -> ^( T_PARAMETER[] '?' numeric_literal )
					{
						// JPA2.g:443:30: ^( T_PARAMETER[] '?' numeric_literal )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_67.nextNode());
						adaptor.addChild(root_1, stream_numeric_literal.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// JPA2.g:444:7: NAMED_PARAMETER
					{
					NAMED_PARAMETER529=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter4048); if (state.failed) return retval; 
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
					// 444:23: -> ^( T_PARAMETER[] NAMED_PARAMETER )
					{
						// JPA2.g:444:26: ^( T_PARAMETER[] NAMED_PARAMETER )
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
					// JPA2.g:445:7: '${' WORD '}'
					{
					string_literal530=(Token)match(input,53,FOLLOW_53_in_input_parameter4069); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_53.add(string_literal530);

					WORD531=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter4071); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WORD.add(WORD531);

					char_literal532=(Token)match(input,136,FOLLOW_136_in_input_parameter4073); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_136.add(char_literal532);

					// AST REWRITE
					// elements: 53, WORD, 136
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 445:21: -> ^( T_PARAMETER[] '${' WORD '}' )
					{
						// JPA2.g:445:24: ^( T_PARAMETER[] '${' WORD '}' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_53.nextNode());
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
	// JPA2.g:447:1: literal : WORD ;
	public final JPA2Parser.literal_return literal() throws RecognitionException {
		JPA2Parser.literal_return retval = new JPA2Parser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD533=null;

		Object WORD533_tree=null;

		try {
			// JPA2.g:448:5: ( WORD )
			// JPA2.g:448:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD533=(Token)match(input,WORD,FOLLOW_WORD_in_literal4101); if (state.failed) return retval;
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
	// JPA2.g:450:1: constructor_name : WORD ;
	public final JPA2Parser.constructor_name_return constructor_name() throws RecognitionException {
		JPA2Parser.constructor_name_return retval = new JPA2Parser.constructor_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD534=null;

		Object WORD534_tree=null;

		try {
			// JPA2.g:451:5: ( WORD )
			// JPA2.g:451:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD534=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4113); if (state.failed) return retval;
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
	// JPA2.g:453:1: enum_literal : WORD ;
	public final JPA2Parser.enum_literal_return enum_literal() throws RecognitionException {
		JPA2Parser.enum_literal_return retval = new JPA2Parser.enum_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD535=null;

		Object WORD535_tree=null;

		try {
			// JPA2.g:454:5: ( WORD )
			// JPA2.g:454:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD535=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal4125); if (state.failed) return retval;
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
	// JPA2.g:456:1: boolean_literal : ( 'true' | 'false' );
	public final JPA2Parser.boolean_literal_return boolean_literal() throws RecognitionException {
		JPA2Parser.boolean_literal_return retval = new JPA2Parser.boolean_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set536=null;

		Object set536_tree=null;

		try {
			// JPA2.g:457:5: ( 'true' | 'false' )
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
	// JPA2.g:461:1: field : ( WORD | 'GROUP' );
	public final JPA2Parser.field_return field() throws RecognitionException {
		JPA2Parser.field_return retval = new JPA2Parser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set537=null;

		Object set537_tree=null;

		try {
			// JPA2.g:462:5: ( WORD | 'GROUP' )
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
	// JPA2.g:464:1: identification_variable : WORD ;
	public final JPA2Parser.identification_variable_return identification_variable() throws RecognitionException {
		JPA2Parser.identification_variable_return retval = new JPA2Parser.identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD538=null;

		Object WORD538_tree=null;

		try {
			// JPA2.g:465:5: ( WORD )
			// JPA2.g:465:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD538=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable4174); if (state.failed) return retval;
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
	// JPA2.g:467:1: parameter_name : WORD ( '.' WORD )* ;
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
			// JPA2.g:468:5: ( WORD ( '.' WORD )* )
			// JPA2.g:468:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD539=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4186); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD539_tree = (Object)adaptor.create(WORD539);
			adaptor.addChild(root_0, WORD539_tree);
			}

			// JPA2.g:468:12: ( '.' WORD )*
			loop130:
			while (true) {
				int alt130=2;
				int LA130_0 = input.LA(1);
				if ( (LA130_0==58) ) {
					alt130=1;
				}

				switch (alt130) {
				case 1 :
					// JPA2.g:468:13: '.' WORD
					{
					char_literal540=(Token)match(input,58,FOLLOW_58_in_parameter_name4189); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal540_tree = (Object)adaptor.create(char_literal540);
					adaptor.addChild(root_0, char_literal540_tree);
					}

					WORD541=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4192); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD541_tree = (Object)adaptor.create(WORD541);
					adaptor.addChild(root_0, WORD541_tree);
					}

					}
					break;

				default :
					break loop130;
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
	// JPA2.g:471:1: escape_character : ( '\\'.\\'' | STRING_LITERAL );
	public final JPA2Parser.escape_character_return escape_character() throws RecognitionException {
		JPA2Parser.escape_character_return retval = new JPA2Parser.escape_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set542=null;

		Object set542_tree=null;

		try {
			// JPA2.g:472:5: ( '\\'.\\'' | STRING_LITERAL )
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
	// JPA2.g:473:1: trim_character : TRIM_CHARACTER ;
	public final JPA2Parser.trim_character_return trim_character() throws RecognitionException {
		JPA2Parser.trim_character_return retval = new JPA2Parser.trim_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRIM_CHARACTER543=null;

		Object TRIM_CHARACTER543_tree=null;

		try {
			// JPA2.g:474:5: ( TRIM_CHARACTER )
			// JPA2.g:474:7: TRIM_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			TRIM_CHARACTER543=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_trim_character4222); if (state.failed) return retval;
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
	// JPA2.g:475:1: string_literal : STRING_LITERAL ;
	public final JPA2Parser.string_literal_return string_literal() throws RecognitionException {
		JPA2Parser.string_literal_return retval = new JPA2Parser.string_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL544=null;

		Object STRING_LITERAL544_tree=null;

		try {
			// JPA2.g:476:5: ( STRING_LITERAL )
			// JPA2.g:476:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL544=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_literal4233); if (state.failed) return retval;
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
	// JPA2.g:477:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
	public final JPA2Parser.numeric_literal_return numeric_literal() throws RecognitionException {
		JPA2Parser.numeric_literal_return retval = new JPA2Parser.numeric_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal545=null;
		Token INT_NUMERAL546=null;

		Object string_literal545_tree=null;
		Object INT_NUMERAL546_tree=null;

		try {
			// JPA2.g:478:5: ( ( '0x' )? INT_NUMERAL )
			// JPA2.g:478:7: ( '0x' )? INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			// JPA2.g:478:7: ( '0x' )?
			int alt131=2;
			int LA131_0 = input.LA(1);
			if ( (LA131_0==60) ) {
				alt131=1;
			}
			switch (alt131) {
				case 1 :
					// JPA2.g:478:8: '0x'
					{
					string_literal545=(Token)match(input,60,FOLLOW_60_in_numeric_literal4245); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal545_tree = (Object)adaptor.create(string_literal545);
					adaptor.addChild(root_0, string_literal545_tree);
					}

					}
					break;

			}

			INT_NUMERAL546=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal4249); if (state.failed) return retval;
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
	// JPA2.g:479:1: single_valued_object_field : WORD ;
	public final JPA2Parser.single_valued_object_field_return single_valued_object_field() throws RecognitionException {
		JPA2Parser.single_valued_object_field_return retval = new JPA2Parser.single_valued_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD547=null;

		Object WORD547_tree=null;

		try {
			// JPA2.g:480:5: ( WORD )
			// JPA2.g:480:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD547=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_object_field4261); if (state.failed) return retval;
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
	// JPA2.g:481:1: single_valued_embeddable_object_field : WORD ;
	public final JPA2Parser.single_valued_embeddable_object_field_return single_valued_embeddable_object_field() throws RecognitionException {
		JPA2Parser.single_valued_embeddable_object_field_return retval = new JPA2Parser.single_valued_embeddable_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD548=null;

		Object WORD548_tree=null;

		try {
			// JPA2.g:482:5: ( WORD )
			// JPA2.g:482:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD548=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_embeddable_object_field4272); if (state.failed) return retval;
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
	// JPA2.g:483:1: collection_valued_field : WORD ;
	public final JPA2Parser.collection_valued_field_return collection_valued_field() throws RecognitionException {
		JPA2Parser.collection_valued_field_return retval = new JPA2Parser.collection_valued_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD549=null;

		Object WORD549_tree=null;

		try {
			// JPA2.g:484:5: ( WORD )
			// JPA2.g:484:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD549=(Token)match(input,WORD,FOLLOW_WORD_in_collection_valued_field4283); if (state.failed) return retval;
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
	// JPA2.g:485:1: entity_name : WORD ;
	public final JPA2Parser.entity_name_return entity_name() throws RecognitionException {
		JPA2Parser.entity_name_return retval = new JPA2Parser.entity_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD550=null;

		Object WORD550_tree=null;

		try {
			// JPA2.g:486:5: ( WORD )
			// JPA2.g:486:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD550=(Token)match(input,WORD,FOLLOW_WORD_in_entity_name4294); if (state.failed) return retval;
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
	// JPA2.g:487:1: subtype : WORD ;
	public final JPA2Parser.subtype_return subtype() throws RecognitionException {
		JPA2Parser.subtype_return retval = new JPA2Parser.subtype_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD551=null;

		Object WORD551_tree=null;

		try {
			// JPA2.g:488:5: ( WORD )
			// JPA2.g:488:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD551=(Token)match(input,WORD,FOLLOW_WORD_in_subtype4305); if (state.failed) return retval;
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
	// JPA2.g:489:1: entity_type_literal : WORD ;
	public final JPA2Parser.entity_type_literal_return entity_type_literal() throws RecognitionException {
		JPA2Parser.entity_type_literal_return retval = new JPA2Parser.entity_type_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD552=null;

		Object WORD552_tree=null;

		try {
			// JPA2.g:490:5: ( WORD )
			// JPA2.g:490:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD552=(Token)match(input,WORD,FOLLOW_WORD_in_entity_type_literal4316); if (state.failed) return retval;
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
	// JPA2.g:491:1: function_name : WORD ;
	public final JPA2Parser.function_name_return function_name() throws RecognitionException {
		JPA2Parser.function_name_return retval = new JPA2Parser.function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD553=null;

		Object WORD553_tree=null;

		try {
			// JPA2.g:492:5: ( WORD )
			// JPA2.g:492:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD553=(Token)match(input,WORD,FOLLOW_WORD_in_function_name4327); if (state.failed) return retval;
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
	// $ANTLR end "function_name"


	public static class state_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "state_field"
	// JPA2.g:493:1: state_field : WORD ;
	public final JPA2Parser.state_field_return state_field() throws RecognitionException {
		JPA2Parser.state_field_return retval = new JPA2Parser.state_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD554=null;

		Object WORD554_tree=null;

		try {
			// JPA2.g:494:5: ( WORD )
			// JPA2.g:494:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD554=(Token)match(input,WORD,FOLLOW_WORD_in_state_field4338); if (state.failed) return retval;
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
	// JPA2.g:495:1: result_variable : WORD ;
	public final JPA2Parser.result_variable_return result_variable() throws RecognitionException {
		JPA2Parser.result_variable_return retval = new JPA2Parser.result_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD555=null;

		Object WORD555_tree=null;

		try {
			// JPA2.g:496:5: ( WORD )
			// JPA2.g:496:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD555=(Token)match(input,WORD,FOLLOW_WORD_in_result_variable4349); if (state.failed) return retval;
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
	// JPA2.g:497:1: superquery_identification_variable : WORD ;
	public final JPA2Parser.superquery_identification_variable_return superquery_identification_variable() throws RecognitionException {
		JPA2Parser.superquery_identification_variable_return retval = new JPA2Parser.superquery_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD556=null;

		Object WORD556_tree=null;

		try {
			// JPA2.g:498:5: ( WORD )
			// JPA2.g:498:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD556=(Token)match(input,WORD,FOLLOW_WORD_in_superquery_identification_variable4360); if (state.failed) return retval;
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
	// JPA2.g:499:1: date_time_timestamp_literal : WORD ;
	public final JPA2Parser.date_time_timestamp_literal_return date_time_timestamp_literal() throws RecognitionException {
		JPA2Parser.date_time_timestamp_literal_return retval = new JPA2Parser.date_time_timestamp_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD557=null;

		Object WORD557_tree=null;

		try {
			// JPA2.g:500:5: ( WORD )
			// JPA2.g:500:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD557=(Token)match(input,WORD,FOLLOW_WORD_in_date_time_timestamp_literal4371); if (state.failed) return retval;
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
	// JPA2.g:501:1: pattern_value : string_literal ;
	public final JPA2Parser.pattern_value_return pattern_value() throws RecognitionException {
		JPA2Parser.pattern_value_return retval = new JPA2Parser.pattern_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal558 =null;


		try {
			// JPA2.g:502:5: ( string_literal )
			// JPA2.g:502:7: string_literal
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_literal_in_pattern_value4382);
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
	// JPA2.g:503:1: collection_valued_input_parameter : input_parameter ;
	public final JPA2Parser.collection_valued_input_parameter_return collection_valued_input_parameter() throws RecognitionException {
		JPA2Parser.collection_valued_input_parameter_return retval = new JPA2Parser.collection_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter559 =null;


		try {
			// JPA2.g:504:5: ( input_parameter )
			// JPA2.g:504:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_collection_valued_input_parameter4393);
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
	// JPA2.g:505:1: single_valued_input_parameter : input_parameter ;
	public final JPA2Parser.single_valued_input_parameter_return single_valued_input_parameter() throws RecognitionException {
		JPA2Parser.single_valued_input_parameter_return retval = new JPA2Parser.single_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter560 =null;


		try {
			// JPA2.g:506:5: ( input_parameter )
			// JPA2.g:506:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_single_valued_input_parameter4404);
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

	// $ANTLR start synpred18_JPA2
	public final void synpred18_JPA2_fragment() throws RecognitionException {
		// JPA2.g:113:48: ( field )
		// JPA2.g:113:48: field
		{
		pushFollow(FOLLOW_field_in_synpred18_JPA2819);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred18_JPA2

	// $ANTLR start synpred27_JPA2
	public final void synpred27_JPA2_fragment() throws RecognitionException {
		// JPA2.g:131:48: ( field )
		// JPA2.g:131:48: field
		{
		pushFollow(FOLLOW_field_in_synpred27_JPA21009);
		field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred27_JPA2

	// $ANTLR start synpred33_JPA2
	public final void synpred33_JPA2_fragment() throws RecognitionException {
		// JPA2.g:145:7: ( scalar_expression )
		// JPA2.g:145:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred33_JPA21123);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred33_JPA2

	// $ANTLR start synpred34_JPA2
	public final void synpred34_JPA2_fragment() throws RecognitionException {
		// JPA2.g:146:7: ( simple_entity_expression )
		// JPA2.g:146:7: simple_entity_expression
		{
		pushFollow(FOLLOW_simple_entity_expression_in_synpred34_JPA21131);
		simple_entity_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred34_JPA2

	// $ANTLR start synpred41_JPA2
	public final void synpred41_JPA2_fragment() throws RecognitionException {
		// JPA2.g:156:7: ( path_expression )
		// JPA2.g:156:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred41_JPA21245);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred41_JPA2

	// $ANTLR start synpred42_JPA2
	public final void synpred42_JPA2_fragment() throws RecognitionException {
		// JPA2.g:157:7: ( identification_variable )
		// JPA2.g:157:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred42_JPA21253);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred42_JPA2

	// $ANTLR start synpred43_JPA2
	public final void synpred43_JPA2_fragment() throws RecognitionException {
		// JPA2.g:158:7: ( scalar_expression )
		// JPA2.g:158:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred43_JPA21271);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred43_JPA2

	// $ANTLR start synpred44_JPA2
	public final void synpred44_JPA2_fragment() throws RecognitionException {
		// JPA2.g:159:7: ( aggregate_expression )
		// JPA2.g:159:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred44_JPA21279);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred44_JPA2

	// $ANTLR start synpred47_JPA2
	public final void synpred47_JPA2_fragment() throws RecognitionException {
		// JPA2.g:165:7: ( path_expression )
		// JPA2.g:165:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred47_JPA21336);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred47_JPA2

	// $ANTLR start synpred48_JPA2
	public final void synpred48_JPA2_fragment() throws RecognitionException {
		// JPA2.g:166:7: ( scalar_expression )
		// JPA2.g:166:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred48_JPA21344);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred48_JPA2

	// $ANTLR start synpred49_JPA2
	public final void synpred49_JPA2_fragment() throws RecognitionException {
		// JPA2.g:167:7: ( aggregate_expression )
		// JPA2.g:167:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred49_JPA21352);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred49_JPA2

	// $ANTLR start synpred51_JPA2
	public final void synpred51_JPA2_fragment() throws RecognitionException {
		// JPA2.g:170:7: ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' )
		// JPA2.g:170:7: aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')'
		{
		pushFollow(FOLLOW_aggregate_expression_function_name_in_synpred51_JPA21371);
		aggregate_expression_function_name();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred51_JPA21373); if (state.failed) return;

		// JPA2.g:170:45: ( DISTINCT )?
		int alt140=2;
		int LA140_0 = input.LA(1);
		if ( (LA140_0==DISTINCT) ) {
			alt140=1;
		}
		switch (alt140) {
			case 1 :
				// JPA2.g:170:46: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred51_JPA21375); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_path_expression_in_synpred51_JPA21379);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred51_JPA21380); if (state.failed) return;

		}

	}
	// $ANTLR end synpred51_JPA2

	// $ANTLR start synpred53_JPA2
	public final void synpred53_JPA2_fragment() throws RecognitionException {
		// JPA2.g:172:7: ( 'COUNT' '(' ( DISTINCT )? count_argument ')' )
		// JPA2.g:172:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
		{
		match(input,COUNT,FOLLOW_COUNT_in_synpred53_JPA21414); if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred53_JPA21416); if (state.failed) return;

		// JPA2.g:172:18: ( DISTINCT )?
		int alt141=2;
		int LA141_0 = input.LA(1);
		if ( (LA141_0==DISTINCT) ) {
			alt141=1;
		}
		switch (alt141) {
			case 1 :
				// JPA2.g:172:19: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred53_JPA21418); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_count_argument_in_synpred53_JPA21422);
		count_argument();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred53_JPA21424); if (state.failed) return;

		}

	}
	// $ANTLR end synpred53_JPA2

	// $ANTLR start synpred65_JPA2
	public final void synpred65_JPA2_fragment() throws RecognitionException {
		// JPA2.g:197:25: ( general_identification_variable )
		// JPA2.g:197:25: general_identification_variable
		{
		pushFollow(FOLLOW_general_identification_variable_in_synpred65_JPA21722);
		general_identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred65_JPA2

	// $ANTLR start synpred73_JPA2
	public final void synpred73_JPA2_fragment() throws RecognitionException {
		// JPA2.g:211:7: ( general_derived_path '.' single_valued_object_field )
		// JPA2.g:211:7: general_derived_path '.' single_valued_object_field
		{
		pushFollow(FOLLOW_general_derived_path_in_synpred73_JPA21896);
		general_derived_path();
		state._fsp--;
		if (state.failed) return;

		match(input,58,FOLLOW_58_in_synpred73_JPA21897); if (state.failed) return;

		pushFollow(FOLLOW_single_valued_object_field_in_synpred73_JPA21898);
		single_valued_object_field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred73_JPA2

	// $ANTLR start synpred78_JPA2
	public final void synpred78_JPA2_fragment() throws RecognitionException {
		// JPA2.g:229:7: ( path_expression )
		// JPA2.g:229:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred78_JPA22050);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred78_JPA2

	// $ANTLR start synpred79_JPA2
	public final void synpred79_JPA2_fragment() throws RecognitionException {
		// JPA2.g:230:7: ( scalar_expression )
		// JPA2.g:230:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred79_JPA22058);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred79_JPA2

	// $ANTLR start synpred80_JPA2
	public final void synpred80_JPA2_fragment() throws RecognitionException {
		// JPA2.g:231:7: ( aggregate_expression )
		// JPA2.g:231:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred80_JPA22066);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred80_JPA2

	// $ANTLR start synpred81_JPA2
	public final void synpred81_JPA2_fragment() throws RecognitionException {
		// JPA2.g:234:7: ( arithmetic_expression )
		// JPA2.g:234:7: arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred81_JPA22085);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred81_JPA2

	// $ANTLR start synpred82_JPA2
	public final void synpred82_JPA2_fragment() throws RecognitionException {
		// JPA2.g:235:7: ( string_expression )
		// JPA2.g:235:7: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred82_JPA22093);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred82_JPA2

	// $ANTLR start synpred83_JPA2
	public final void synpred83_JPA2_fragment() throws RecognitionException {
		// JPA2.g:236:7: ( enum_expression )
		// JPA2.g:236:7: enum_expression
		{
		pushFollow(FOLLOW_enum_expression_in_synpred83_JPA22101);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred83_JPA2

	// $ANTLR start synpred84_JPA2
	public final void synpred84_JPA2_fragment() throws RecognitionException {
		// JPA2.g:237:7: ( datetime_expression )
		// JPA2.g:237:7: datetime_expression
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred84_JPA22109);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred84_JPA2

	// $ANTLR start synpred85_JPA2
	public final void synpred85_JPA2_fragment() throws RecognitionException {
		// JPA2.g:238:7: ( boolean_expression )
		// JPA2.g:238:7: boolean_expression
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred85_JPA22117);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred85_JPA2

	// $ANTLR start synpred86_JPA2
	public final void synpred86_JPA2_fragment() throws RecognitionException {
		// JPA2.g:239:7: ( case_expression )
		// JPA2.g:239:7: case_expression
		{
		pushFollow(FOLLOW_case_expression_in_synpred86_JPA22125);
		case_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred86_JPA2

	// $ANTLR start synpred89_JPA2
	public final void synpred89_JPA2_fragment() throws RecognitionException {
		// JPA2.g:246:8: ( 'NOT' )
		// JPA2.g:246:8: 'NOT'
		{
		match(input,109,FOLLOW_109_in_synpred89_JPA22185); if (state.failed) return;

		}

	}
	// $ANTLR end synpred89_JPA2

	// $ANTLR start synpred90_JPA2
	public final void synpred90_JPA2_fragment() throws RecognitionException {
		// JPA2.g:248:7: ( simple_cond_expression )
		// JPA2.g:248:7: simple_cond_expression
		{
		pushFollow(FOLLOW_simple_cond_expression_in_synpred90_JPA22200);
		simple_cond_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred90_JPA2

	// $ANTLR start synpred91_JPA2
	public final void synpred91_JPA2_fragment() throws RecognitionException {
		// JPA2.g:252:7: ( comparison_expression )
		// JPA2.g:252:7: comparison_expression
		{
		pushFollow(FOLLOW_comparison_expression_in_synpred91_JPA22237);
		comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred91_JPA2

	// $ANTLR start synpred92_JPA2
	public final void synpred92_JPA2_fragment() throws RecognitionException {
		// JPA2.g:253:7: ( between_expression )
		// JPA2.g:253:7: between_expression
		{
		pushFollow(FOLLOW_between_expression_in_synpred92_JPA22245);
		between_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred92_JPA2

	// $ANTLR start synpred93_JPA2
	public final void synpred93_JPA2_fragment() throws RecognitionException {
		// JPA2.g:254:7: ( in_expression )
		// JPA2.g:254:7: in_expression
		{
		pushFollow(FOLLOW_in_expression_in_synpred93_JPA22253);
		in_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred93_JPA2

	// $ANTLR start synpred94_JPA2
	public final void synpred94_JPA2_fragment() throws RecognitionException {
		// JPA2.g:255:7: ( like_expression )
		// JPA2.g:255:7: like_expression
		{
		pushFollow(FOLLOW_like_expression_in_synpred94_JPA22261);
		like_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred94_JPA2

	// $ANTLR start synpred95_JPA2
	public final void synpred95_JPA2_fragment() throws RecognitionException {
		// JPA2.g:256:7: ( null_comparison_expression )
		// JPA2.g:256:7: null_comparison_expression
		{
		pushFollow(FOLLOW_null_comparison_expression_in_synpred95_JPA22269);
		null_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred95_JPA2

	// $ANTLR start synpred96_JPA2
	public final void synpred96_JPA2_fragment() throws RecognitionException {
		// JPA2.g:257:7: ( empty_collection_comparison_expression )
		// JPA2.g:257:7: empty_collection_comparison_expression
		{
		pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred96_JPA22277);
		empty_collection_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred96_JPA2

	// $ANTLR start synpred97_JPA2
	public final void synpred97_JPA2_fragment() throws RecognitionException {
		// JPA2.g:258:7: ( collection_member_expression )
		// JPA2.g:258:7: collection_member_expression
		{
		pushFollow(FOLLOW_collection_member_expression_in_synpred97_JPA22285);
		collection_member_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred97_JPA2

	// $ANTLR start synpred116_JPA2
	public final void synpred116_JPA2_fragment() throws RecognitionException {
		// JPA2.g:287:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
		// JPA2.g:287:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred116_JPA22538);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:287:29: ( 'NOT' )?
		int alt144=2;
		int LA144_0 = input.LA(1);
		if ( (LA144_0==109) ) {
			alt144=1;
		}
		switch (alt144) {
			case 1 :
				// JPA2.g:287:30: 'NOT'
				{
				match(input,109,FOLLOW_109_in_synpred116_JPA22541); if (state.failed) return;

				}
				break;

		}

		match(input,77,FOLLOW_77_in_synpred116_JPA22545); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred116_JPA22547);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred116_JPA22549); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred116_JPA22551);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred116_JPA2

	// $ANTLR start synpred118_JPA2
	public final void synpred118_JPA2_fragment() throws RecognitionException {
		// JPA2.g:288:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
		// JPA2.g:288:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred118_JPA22559);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:288:25: ( 'NOT' )?
		int alt145=2;
		int LA145_0 = input.LA(1);
		if ( (LA145_0==109) ) {
			alt145=1;
		}
		switch (alt145) {
			case 1 :
				// JPA2.g:288:26: 'NOT'
				{
				match(input,109,FOLLOW_109_in_synpred118_JPA22562); if (state.failed) return;

				}
				break;

		}

		match(input,77,FOLLOW_77_in_synpred118_JPA22566); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred118_JPA22568);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred118_JPA22570); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred118_JPA22572);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred118_JPA2

	// $ANTLR start synpred135_JPA2
	public final void synpred135_JPA2_fragment() throws RecognitionException {
		// JPA2.g:309:7: ( identification_variable )
		// JPA2.g:309:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred135_JPA22827);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred135_JPA2

	// $ANTLR start synpred141_JPA2
	public final void synpred141_JPA2_fragment() throws RecognitionException {
		// JPA2.g:317:7: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) )
		// JPA2.g:317:7: string_expression comparison_operator ( string_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_string_expression_in_synpred141_JPA22896);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred141_JPA22898);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:317:45: ( string_expression | all_or_any_expression )
		int alt147=2;
		int LA147_0 = input.LA(1);
		if ( (LA147_0==AVG||LA147_0==COUNT||(LA147_0 >= LOWER && LA147_0 <= NAMED_PARAMETER)||(LA147_0 >= STRING_LITERAL && LA147_0 <= SUM)||LA147_0==WORD||LA147_0==53||LA147_0==67||(LA147_0 >= 79 && LA147_0 <= 81)||LA147_0==94||LA147_0==112||LA147_0==122||LA147_0==126||LA147_0==129) ) {
			alt147=1;
		}
		else if ( ((LA147_0 >= 74 && LA147_0 <= 75)||LA147_0==120) ) {
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
				// JPA2.g:317:46: string_expression
				{
				pushFollow(FOLLOW_string_expression_in_synpred141_JPA22901);
				string_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:317:66: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred141_JPA22905);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred141_JPA2

	// $ANTLR start synpred144_JPA2
	public final void synpred144_JPA2_fragment() throws RecognitionException {
		// JPA2.g:318:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
		// JPA2.g:318:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred144_JPA22914);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 63 && input.LA(1) <= 64) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:318:39: ( boolean_expression | all_or_any_expression )
		int alt148=2;
		int LA148_0 = input.LA(1);
		if ( (LA148_0==LPAREN||LA148_0==NAMED_PARAMETER||LA148_0==WORD||LA148_0==53||LA148_0==67||(LA148_0 >= 79 && LA148_0 <= 80)||LA148_0==94||LA148_0==112||(LA148_0 >= 134 && LA148_0 <= 135)) ) {
			alt148=1;
		}
		else if ( ((LA148_0 >= 74 && LA148_0 <= 75)||LA148_0==120) ) {
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
				// JPA2.g:318:40: boolean_expression
				{
				pushFollow(FOLLOW_boolean_expression_in_synpred144_JPA22925);
				boolean_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:318:61: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred144_JPA22929);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred144_JPA2

	// $ANTLR start synpred147_JPA2
	public final void synpred147_JPA2_fragment() throws RecognitionException {
		// JPA2.g:319:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
		// JPA2.g:319:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_enum_expression_in_synpred147_JPA22938);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 63 && input.LA(1) <= 64) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:319:34: ( enum_expression | all_or_any_expression )
		int alt149=2;
		int LA149_0 = input.LA(1);
		if ( (LA149_0==LPAREN||LA149_0==NAMED_PARAMETER||LA149_0==WORD||LA149_0==53||LA149_0==67||(LA149_0 >= 79 && LA149_0 <= 80)||LA149_0==112) ) {
			alt149=1;
		}
		else if ( ((LA149_0 >= 74 && LA149_0 <= 75)||LA149_0==120) ) {
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
				// JPA2.g:319:35: enum_expression
				{
				pushFollow(FOLLOW_enum_expression_in_synpred147_JPA22947);
				enum_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:319:53: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred147_JPA22951);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred147_JPA2

	// $ANTLR start synpred149_JPA2
	public final void synpred149_JPA2_fragment() throws RecognitionException {
		// JPA2.g:320:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
		// JPA2.g:320:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred149_JPA22960);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred149_JPA22962);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// JPA2.g:320:47: ( datetime_expression | all_or_any_expression )
		int alt150=2;
		int LA150_0 = input.LA(1);
		if ( (LA150_0==AVG||LA150_0==COUNT||(LA150_0 >= LPAREN && LA150_0 <= NAMED_PARAMETER)||LA150_0==SUM||LA150_0==WORD||LA150_0==53||LA150_0==67||(LA150_0 >= 79 && LA150_0 <= 80)||(LA150_0 >= 82 && LA150_0 <= 84)||LA150_0==94||LA150_0==112) ) {
			alt150=1;
		}
		else if ( ((LA150_0 >= 74 && LA150_0 <= 75)||LA150_0==120) ) {
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
				// JPA2.g:320:48: datetime_expression
				{
				pushFollow(FOLLOW_datetime_expression_in_synpred149_JPA22965);
				datetime_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:320:70: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred149_JPA22969);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred149_JPA2

	// $ANTLR start synpred152_JPA2
	public final void synpred152_JPA2_fragment() throws RecognitionException {
		// JPA2.g:321:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
		// JPA2.g:321:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_entity_expression_in_synpred152_JPA22978);
		entity_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 63 && input.LA(1) <= 64) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// JPA2.g:321:38: ( entity_expression | all_or_any_expression )
		int alt151=2;
		int LA151_0 = input.LA(1);
		if ( (LA151_0==NAMED_PARAMETER||LA151_0==WORD||LA151_0==53||LA151_0==67) ) {
			alt151=1;
		}
		else if ( ((LA151_0 >= 74 && LA151_0 <= 75)||LA151_0==120) ) {
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
				// JPA2.g:321:39: entity_expression
				{
				pushFollow(FOLLOW_entity_expression_in_synpred152_JPA22989);
				entity_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// JPA2.g:321:59: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred152_JPA22993);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred152_JPA2

	// $ANTLR start synpred154_JPA2
	public final void synpred154_JPA2_fragment() throws RecognitionException {
		// JPA2.g:322:7: ( entity_type_expression ( '=' | '<>' ) entity_type_expression )
		// JPA2.g:322:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
		{
		pushFollow(FOLLOW_entity_type_expression_in_synpred154_JPA23002);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 63 && input.LA(1) <= 64) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		pushFollow(FOLLOW_entity_type_expression_in_synpred154_JPA23012);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred154_JPA2

	// $ANTLR start synpred161_JPA2
	public final void synpred161_JPA2_fragment() throws RecognitionException {
		// JPA2.g:333:7: ( arithmetic_term )
		// JPA2.g:333:7: arithmetic_term
		{
		pushFollow(FOLLOW_arithmetic_term_in_synpred161_JPA23093);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred161_JPA2

	// $ANTLR start synpred163_JPA2
	public final void synpred163_JPA2_fragment() throws RecognitionException {
		// JPA2.g:336:7: ( arithmetic_factor )
		// JPA2.g:336:7: arithmetic_factor
		{
		pushFollow(FOLLOW_arithmetic_factor_in_synpred163_JPA23122);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred163_JPA2

	// $ANTLR start synpred169_JPA2
	public final void synpred169_JPA2_fragment() throws RecognitionException {
		// JPA2.g:343:7: ( '(' arithmetic_expression ')' )
		// JPA2.g:343:7: '(' arithmetic_expression ')'
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred169_JPA23191); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred169_JPA23192);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred169_JPA23193); if (state.failed) return;

		}

	}
	// $ANTLR end synpred169_JPA2

	// $ANTLR start synpred172_JPA2
	public final void synpred172_JPA2_fragment() throws RecognitionException {
		// JPA2.g:346:7: ( aggregate_expression )
		// JPA2.g:346:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred172_JPA23217);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred172_JPA2

	// $ANTLR start synpred174_JPA2
	public final void synpred174_JPA2_fragment() throws RecognitionException {
		// JPA2.g:348:7: ( function_invocation )
		// JPA2.g:348:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred174_JPA23233);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred174_JPA2

	// $ANTLR start synpred179_JPA2
	public final void synpred179_JPA2_fragment() throws RecognitionException {
		// JPA2.g:355:7: ( aggregate_expression )
		// JPA2.g:355:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred179_JPA23284);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred179_JPA2

	// $ANTLR start synpred181_JPA2
	public final void synpred181_JPA2_fragment() throws RecognitionException {
		// JPA2.g:357:7: ( function_invocation )
		// JPA2.g:357:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred181_JPA23300);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred181_JPA2

	// $ANTLR start synpred182_JPA2
	public final void synpred182_JPA2_fragment() throws RecognitionException {
		// JPA2.g:360:7: ( path_expression )
		// JPA2.g:360:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred182_JPA23319);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred182_JPA2

	// $ANTLR start synpred185_JPA2
	public final void synpred185_JPA2_fragment() throws RecognitionException {
		// JPA2.g:363:7: ( aggregate_expression )
		// JPA2.g:363:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred185_JPA23343);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred185_JPA2

	// $ANTLR start synpred187_JPA2
	public final void synpred187_JPA2_fragment() throws RecognitionException {
		// JPA2.g:365:7: ( function_invocation )
		// JPA2.g:365:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred187_JPA23359);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred187_JPA2

	// $ANTLR start synpred188_JPA2
	public final void synpred188_JPA2_fragment() throws RecognitionException {
		// JPA2.g:366:7: ( date_time_timestamp_literal )
		// JPA2.g:366:7: date_time_timestamp_literal
		{
		pushFollow(FOLLOW_date_time_timestamp_literal_in_synpred188_JPA23367);
		date_time_timestamp_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred188_JPA2

	// $ANTLR start synpred225_JPA2
	public final void synpred225_JPA2_fragment() throws RecognitionException {
		// JPA2.g:416:7: ( literal )
		// JPA2.g:416:7: literal
		{
		pushFollow(FOLLOW_literal_in_synpred225_JPA23811);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred225_JPA2

	// $ANTLR start synpred227_JPA2
	public final void synpred227_JPA2_fragment() throws RecognitionException {
		// JPA2.g:418:7: ( input_parameter )
		// JPA2.g:418:7: input_parameter
		{
		pushFollow(FOLLOW_input_parameter_in_synpred227_JPA23827);
		input_parameter();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred227_JPA2

	// Delegated rules

	public final boolean synpred179_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred179_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred135_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred135_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred65_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred65_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred152_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred152_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred27_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred27_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred18_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred18_JPA2_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred225_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred225_JPA2_fragment(); // can never throw exception
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


	protected DFA43 dfa43 = new DFA43(this);
	protected DFA49 dfa49 = new DFA49(this);
	static final String DFA43_eotS =
		"\14\uffff";
	static final String DFA43_eofS =
		"\14\uffff";
	static final String DFA43_minS =
		"\1\6\1\26\2\uffff\1\13\1\63\1\35\1\uffff\1\16\1\35\1\0\1\16";
	static final String DFA43_maxS =
		"\1\136\1\26\2\uffff\2\63\1\72\1\uffff\1\63\1\72\1\0\1\63";
	static final String DFA43_acceptS =
		"\2\uffff\1\1\1\3\3\uffff\1\2\4\uffff";
	static final String DFA43_specialS =
		"\12\uffff\1\0\1\uffff}>";
	static final String[] DFA43_transitionS = {
			"\1\2\2\uffff\1\1\15\uffff\2\2\7\uffff\1\2\75\uffff\1\3",
			"\1\4",
			"",
			"",
			"\1\5\47\uffff\1\6",
			"\1\6",
			"\1\7\34\uffff\1\10",
			"",
			"\1\11\16\uffff\1\12\25\uffff\1\11",
			"\1\12\34\uffff\1\13",
			"\1\uffff",
			"\1\11\16\uffff\1\12\25\uffff\1\11"
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

	protected class DFA43 extends DFA {

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
		@Override
		public String getDescription() {
			return "169:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			TokenStream input = (TokenStream)_input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA43_10 = input.LA(1);
						 
						int index43_10 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred51_JPA2()) ) {s = 2;}
						else if ( (synpred53_JPA2()) ) {s = 7;}
						 
						input.seek(index43_10);
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

	static final String DFA49_eotS =
		"\15\uffff";
	static final String DFA49_eofS =
		"\1\uffff\1\5\2\uffff\1\5\4\uffff\4\5";
	static final String DFA49_minS =
		"\1\63\1\5\2\63\1\5\2\uffff\2\35\4\5";
	static final String DFA49_maxS =
		"\1\u0082\1\72\2\63\1\70\2\uffff\2\35\1\72\3\70";
	static final String DFA49_acceptS =
		"\5\uffff\1\1\1\2\6\uffff";
	static final String DFA49_specialS =
		"\15\uffff}>";
	static final String[] DFA49_transitionS = {
			"\1\1\57\uffff\1\2\36\uffff\1\3",
			"\1\5\4\uffff\1\6\55\uffff\1\5\1\uffff\1\4",
			"\1\7",
			"\1\10",
			"\1\5\4\uffff\1\6\3\uffff\1\11\44\uffff\1\11\4\uffff\1\5",
			"",
			"",
			"\1\12",
			"\1\13",
			"\1\5\4\uffff\1\6\55\uffff\1\5\1\uffff\1\14",
			"\1\5\4\uffff\1\6\55\uffff\1\5",
			"\1\5\4\uffff\1\6\55\uffff\1\5",
			"\1\5\4\uffff\1\6\3\uffff\1\11\44\uffff\1\11\4\uffff\1\5"
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

	protected class DFA49 extends DFA {

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
		@Override
		public String getDescription() {
			return "191:1: orderby_item : ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) );";
		}
	}

	public static final BitSet FOLLOW_select_statement_in_ql_statement427 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_117_in_select_statement442 = new BitSet(new long[]{0x12A8000183E20A40L,0xC68314A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_clause_in_select_statement444 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
	public static final BitSet FOLLOW_from_clause_in_select_statement446 = new BitSet(new long[]{0x000000000800C002L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_select_statement449 = new BitSet(new long[]{0x000000000800C002L});
	public static final BitSet FOLLOW_groupby_clause_in_select_statement454 = new BitSet(new long[]{0x0000000008008002L});
	public static final BitSet FOLLOW_having_clause_in_select_statement459 = new BitSet(new long[]{0x0000000008000002L});
	public static final BitSet FOLLOW_orderby_clause_in_select_statement464 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_128_in_update_statement520 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_update_clause_in_update_statement522 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_update_statement525 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_86_in_delete_statement538 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
	public static final BitSet FOLLOW_93_in_delete_statement540 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_delete_clause_in_delete_statement542 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_delete_statement545 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_93_in_from_clause562 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause564 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_56_in_from_clause567 = new BitSet(new long[]{0x0008000000000000L,0x0000000100000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause569 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration603 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration612 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration636 = new BitSet(new long[]{0x00000000000D0002L});
	public static final BitSet FOLLOW_joined_clause_in_identification_variable_declaration638 = new BitSet(new long[]{0x00000000000D0002L});
	public static final BitSet FOLLOW_join_in_joined_clause665 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fetch_join_in_joined_clause669 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_range_variable_declaration681 = new BitSet(new long[]{0x0008000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_range_variable_declaration684 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration688 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_join717 = new BitSet(new long[]{0x0008000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_join719 = new BitSet(new long[]{0x0008000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_join722 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join726 = new BitSet(new long[]{0x0000000000000002L,0x0008000000000000L});
	public static final BitSet FOLLOW_115_in_join729 = new BitSet(new long[]{0x12A8000183E20240L,0xC68124A2501F83F8L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_join731 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_fetch_join762 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_FETCH_in_fetch_join764 = new BitSet(new long[]{0x0008000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join766 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFT_in_join_spec780 = new BitSet(new long[]{0x0000000010040000L});
	public static final BitSet FOLLOW_OUTER_in_join_spec784 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INNER_in_join_spec790 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_JOIN_in_join_spec795 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression809 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_join_association_path_expression811 = new BitSet(new long[]{0x0008000000004002L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression814 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_join_association_path_expression815 = new BitSet(new long[]{0x0008000000004002L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression819 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_125_in_join_association_path_expression854 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression856 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_join_association_path_expression858 = new BitSet(new long[]{0x0008000000004000L,0x0000000000001000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression861 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_join_association_path_expression862 = new BitSet(new long[]{0x0008000000004000L,0x0000000000001000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression866 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_join_association_path_expression869 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_subtype_in_join_association_path_expression871 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_join_association_path_expression873 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_join_association_path_expression906 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_96_in_collection_member_declaration919 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration920 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_declaration922 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration924 = new BitSet(new long[]{0x0008000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_collection_member_declaration927 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration931 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_qualified_identification_variable960 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_90_in_qualified_identification_variable968 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_qualified_identification_variable969 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_qualified_identification_variable970 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_99_in_map_field_identification_variable977 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable978 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable979 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_130_in_map_field_identification_variable983 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable984 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable985 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_path_expression999 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_path_expression1001 = new BitSet(new long[]{0x0008000000004002L});
	public static final BitSet FOLLOW_field_in_path_expression1004 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_path_expression1005 = new BitSet(new long[]{0x0008000000004002L});
	public static final BitSet FOLLOW_field_in_path_expression1009 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_general_identification_variable1048 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_general_identification_variable1056 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_update_clause1067 = new BitSet(new long[]{0x0008000000000000L,0x0040000000001000L});
	public static final BitSet FOLLOW_76_in_update_clause1071 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_update_clause1075 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
	public static final BitSet FOLLOW_118_in_update_clause1079 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1081 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_56_in_update_clause1084 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1086 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_update_item1100 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_update_item1101 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_single_valued_embeddable_object_field_in_update_item1104 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_update_item1105 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_update_item1108 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_64_in_update_item1110 = new BitSet(new long[]{0x12A8000183E20240L,0xC68184A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_new_value_in_update_item1112 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_new_value1123 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_new_value1131 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_111_in_new_value1139 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_delete_clause1150 = new BitSet(new long[]{0x0008000000000002L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_delete_clause1154 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_delete_clause1158 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_select_clause1172 = new BitSet(new long[]{0x12A8000183E20240L,0xC68314A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_item_in_select_clause1176 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_56_in_select_clause1179 = new BitSet(new long[]{0x12A8000183E20240L,0xC68314A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_item_in_select_clause1181 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_select_expression_in_select_item1224 = new BitSet(new long[]{0x0008000000000002L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_select_item1228 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_result_variable_in_select_item1232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_select_expression1245 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1253 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_select_expression1279 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_113_in_select_expression1287 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_select_expression1289 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1290 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_select_expression1291 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_constructor_expression_in_select_expression1299 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_108_in_constructor_expression1310 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_constructor_name_in_constructor_expression1312 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_constructor_expression1314 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1316 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_56_in_constructor_expression1319 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1321 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_constructor_expression1325 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_constructor_item1336 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_constructor_item1344 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_constructor_item1352 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_constructor_item1360 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1371 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1373 = new BitSet(new long[]{0x0008000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1375 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_path_expression_in_aggregate_expression1379 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1380 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_aggregate_expression1414 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1416 = new BitSet(new long[]{0x0008000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1418 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_count_argument_in_aggregate_expression1422 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1424 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_aggregate_expression1459 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_count_argument1496 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_count_argument1500 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_132_in_where_clause1513 = new BitSet(new long[]{0x12A8000183E20240L,0xC68124A2501F83F8L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_where_clause1515 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_groupby_clause1537 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_groupby_clause1539 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1541 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_56_in_groupby_clause1544 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1546 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_path_expression_in_groupby_item1580 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_groupby_item1584 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HAVING_in_having_clause1595 = new BitSet(new long[]{0x12A8000183E20240L,0xC68124A2501F83F8L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_having_clause1597 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderby_clause1608 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_orderby_clause1610 = new BitSet(new long[]{0x0008000000000000L,0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1612 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_56_in_orderby_clause1615 = new BitSet(new long[]{0x0008000000000000L,0x0000000800000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1617 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1651 = new BitSet(new long[]{0x0000000000000022L});
	public static final BitSet FOLLOW_ASC_in_orderby_item1654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1686 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_DESC_in_orderby_item1689 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_orderby_variable1718 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_orderby_variable1722 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_orderby_variable1726 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_subquery1740 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_117_in_subquery1742 = new BitSet(new long[]{0x12A8000183E20A40L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_simple_select_clause_in_subquery1744 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
	public static final BitSet FOLLOW_subquery_from_clause_in_subquery1746 = new BitSet(new long[]{0x000000002000C000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_subquery1749 = new BitSet(new long[]{0x000000002000C000L});
	public static final BitSet FOLLOW_groupby_clause_in_subquery1754 = new BitSet(new long[]{0x0000000020008000L});
	public static final BitSet FOLLOW_having_clause_in_subquery1759 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_subquery1765 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_93_in_subquery_from_clause1815 = new BitSet(new long[]{0x0008000000000000L,0x2000000100000000L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1817 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_56_in_subquery_from_clause1820 = new BitSet(new long[]{0x0008000000000000L,0x2000000100000000L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1822 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1860 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1868 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_subselect_identification_variable_declaration1870 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration1872 = new BitSet(new long[]{0x00000000000D0002L});
	public static final BitSet FOLLOW_join_in_subselect_identification_variable_declaration1875 = new BitSet(new long[]{0x00000000000D0002L});
	public static final BitSet FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1885 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1896 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_derived_path_expression1897 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_path_expression1898 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1906 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_derived_path_expression1907 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_path_expression1908 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_derived_path_in_general_derived_path1919 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_treated_derived_path_in_general_derived_path1927 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_general_derived_path1929 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_general_derived_path1930 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_simple_derived_path1948 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_125_in_treated_derived_path1965 = new BitSet(new long[]{0x0008000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_general_derived_path_in_treated_derived_path1966 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_treated_derived_path1968 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_subtype_in_treated_derived_path1970 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_treated_derived_path1972 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_96_in_derived_collection_member_declaration1983 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration1985 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_derived_collection_member_declaration1986 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_collection_member_declaration1988 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_derived_collection_member_declaration1990 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_collection_member_declaration1993 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause2006 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause2010 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_simple_select_expression2050 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_select_expression2058 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression2066 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_select_expression2074 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_scalar_expression2085 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_scalar_expression2093 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_scalar_expression2101 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_scalar_expression2109 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_scalar_expression2117 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_scalar_expression2125 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_scalar_expression2133 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2145 = new BitSet(new long[]{0x0000000004000002L});
	public static final BitSet FOLLOW_OR_in_conditional_expression2149 = new BitSet(new long[]{0x12A8000183E20240L,0xC68124A2501F83F8L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2151 = new BitSet(new long[]{0x0000000004000002L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2165 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_conditional_term2169 = new BitSet(new long[]{0x12A8000183E20240L,0xC68124A2501F83F8L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2171 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_109_in_conditional_factor2185 = new BitSet(new long[]{0x12A8000183E20240L,0xC68124A2501F83F8L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_primary_in_conditional_factor2189 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_conditional_primary2200 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_conditional_primary2224 = new BitSet(new long[]{0x12A8000183E20240L,0xC68124A2501F83F8L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_conditional_primary2225 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_conditional_primary2226 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression2237 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_simple_cond_expression2245 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_simple_cond_expression2253 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_simple_cond_expression2261 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression2269 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2277 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression2285 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression2293 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression2301 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2322 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2330 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2346 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_68_in_date_between_macro_expression2358 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2360 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2362 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_date_between_macro_expression2364 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_date_between_macro_expression2366 = new BitSet(new long[]{0x0380000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2369 = new BitSet(new long[]{0x1000000000020000L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2377 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_date_between_macro_expression2381 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_date_between_macro_expression2383 = new BitSet(new long[]{0x0380000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2386 = new BitSet(new long[]{0x1000000000020000L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2394 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_date_between_macro_expression2398 = new BitSet(new long[]{0x0000000000000000L,0x00100A0080200000L,0x0000000000000020L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2400 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2423 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_70_in_date_before_macro_expression2435 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2437 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2439 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_date_before_macro_expression2441 = new BitSet(new long[]{0x0028000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2444 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2448 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2451 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_69_in_date_after_macro_expression2463 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2465 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2467 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_date_after_macro_expression2469 = new BitSet(new long[]{0x0028000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2472 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2476 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2479 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_date_equals_macro_expression2491 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2493 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2495 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_date_equals_macro_expression2497 = new BitSet(new long[]{0x0028000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2500 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2504 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2507 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_72_in_date_today_macro_expression2519 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2521 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2523 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2525 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2538 = new BitSet(new long[]{0x0000000000000000L,0x0000200000002000L});
	public static final BitSet FOLLOW_109_in_between_expression2541 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_77_in_between_expression2545 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2547 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2549 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2559 = new BitSet(new long[]{0x0000000000000000L,0x0000200000002000L});
	public static final BitSet FOLLOW_109_in_between_expression2562 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_77_in_between_expression2566 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2568 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2570 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2572 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2580 = new BitSet(new long[]{0x0000000000000000L,0x0000200000002000L});
	public static final BitSet FOLLOW_109_in_between_expression2583 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_77_in_between_expression2587 = new BitSet(new long[]{0x0028000103C00240L,0x00010000401D8008L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2589 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2591 = new BitSet(new long[]{0x0028000103C00240L,0x00010000401D8008L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2593 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2605 = new BitSet(new long[]{0x0000000000000000L,0x0000200100000000L});
	public static final BitSet FOLLOW_type_discriminator_in_in_expression2609 = new BitSet(new long[]{0x0000000000000000L,0x0000200100000000L});
	public static final BitSet FOLLOW_109_in_in_expression2613 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
	public static final BitSet FOLLOW_96_in_in_expression2617 = new BitSet(new long[]{0x0020000002400000L,0x0000000000000008L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2633 = new BitSet(new long[]{0x0028000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_in_item_in_in_expression2635 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_56_in_in_expression2638 = new BitSet(new long[]{0x0028000002000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_in_item_in_in_expression2640 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2644 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_in_expression2660 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_valued_input_parameter_in_in_expression2676 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_in_item2689 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_single_valued_input_parameter_in_in_item2693 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2704 = new BitSet(new long[]{0x0000000000000000L,0x0000204000000000L});
	public static final BitSet FOLLOW_109_in_like_expression2707 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
	public static final BitSet FOLLOW_102_in_like_expression2711 = new BitSet(new long[]{0x0020000082000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_pattern_value_in_like_expression2714 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
	public static final BitSet FOLLOW_input_parameter_in_like_expression2718 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
	public static final BitSet FOLLOW_91_in_like_expression2721 = new BitSet(new long[]{0x0000000280000000L});
	public static final BitSet FOLLOW_escape_character_in_like_expression2723 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_null_comparison_expression2737 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression2741 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_98_in_null_comparison_expression2744 = new BitSet(new long[]{0x0000000000000000L,0x0000A00000000000L});
	public static final BitSet FOLLOW_109_in_null_comparison_expression2747 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_111_in_null_comparison_expression2751 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression2762 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_98_in_empty_collection_comparison_expression2764 = new BitSet(new long[]{0x0000000000000000L,0x0000200001000000L});
	public static final BitSet FOLLOW_109_in_empty_collection_comparison_expression2767 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
	public static final BitSet FOLLOW_88_in_empty_collection_comparison_expression2771 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_or_value_expression_in_collection_member_expression2782 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_109_in_collection_member_expression2786 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_104_in_collection_member_expression2790 = new BitSet(new long[]{0x0008000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_114_in_collection_member_expression2793 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_expression2797 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_or_value_expression2808 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2816 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_or_value_expression2827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_or_value_expression2835 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simple_entity_or_value_expression2843 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_109_in_exists_expression2855 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
	public static final BitSet FOLLOW_92_in_exists_expression2859 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_subquery_in_exists_expression2861 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_all_or_any_expression2872 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_subquery_in_all_or_any_expression2885 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression2896 = new BitSet(new long[]{0xE000000000000000L,0x0000000000000007L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2898 = new BitSet(new long[]{0x0028000183E00240L,0x4501000040038C08L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression2901 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2905 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2914 = new BitSet(new long[]{0x8000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_comparison_expression2916 = new BitSet(new long[]{0x0028000002400000L,0x0101000040018C08L,0x00000000000000C0L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2925 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2929 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression2938 = new BitSet(new long[]{0x8000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_comparison_expression2940 = new BitSet(new long[]{0x0028000002400000L,0x0101000000018C08L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression2947 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2951 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2960 = new BitSet(new long[]{0xE000000000000000L,0x0000000000000007L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2962 = new BitSet(new long[]{0x0028000103C00240L,0x01010000401D8C08L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2965 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2969 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression2978 = new BitSet(new long[]{0x8000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_comparison_expression2980 = new BitSet(new long[]{0x0028000002000000L,0x0100000000000C08L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression2989 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2993 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3002 = new BitSet(new long[]{0x8000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_comparison_expression3004 = new BitSet(new long[]{0x0028000002000000L,0x8000000000000008L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression3012 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3020 = new BitSet(new long[]{0xE000000000000000L,0x0000000000000007L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3022 = new BitSet(new long[]{0x12A8000103C20240L,0x038104A240018E08L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3025 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3029 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3093 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3101 = new BitSet(new long[]{0x0280000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_expression3103 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3130 = new BitSet(new long[]{0x0840000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_term3132 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor3164 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_arithmetic_primary3175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary3183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary3191 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_arithmetic_primary3192 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary3193 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary3201 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary3209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary3217 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_arithmetic_primary3225 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_arithmetic_primary3233 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_arithmetic_primary3241 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_string_expression3252 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_string_expression3260 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_string_expression3268 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_strings_in_string_expression3276 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_string_expression3284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_string_expression3292 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_string_expression3300 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_string_expression3308 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_datetime_expression3319 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_datetime_expression3327 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_expression3335 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_datetime_expression3343 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_datetime_expression3351 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_datetime_expression3359 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_datetime_expression3367 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_datetime_expression3375 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_boolean_expression3386 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_literal_in_boolean_expression3394 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_boolean_expression3402 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_boolean_expression3410 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_boolean_expression3418 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_boolean_expression3426 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_enum_expression3437 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_literal_in_enum_expression3445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_enum_expression3453 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_enum_expression3461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_enum_expression3469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_expression3480 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3488 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3499 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3507 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_entity_type_expression3518 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_literal_in_entity_type_expression3526 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_entity_type_expression3534 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_127_in_type_discriminator3545 = new BitSet(new long[]{0x0028000002000000L,0x0000000800000008L,0x0000000000000004L});
	public static final BitSet FOLLOW_general_identification_variable_in_type_discriminator3547 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_type_discriminator3551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_type_discriminator3555 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_functions_returning_numerics3567 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3568 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3569 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_functions_returning_numerics3577 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3579 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_functions_returning_numerics3580 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3582 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_56_in_functions_returning_numerics3584 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3585 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3588 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_73_in_functions_returning_numerics3596 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3597 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3598 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_121_in_functions_returning_numerics3606 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3607 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3608 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_106_in_functions_returning_numerics3616 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3617 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_functions_returning_numerics3618 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3620 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3621 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_119_in_functions_returning_numerics3629 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3630 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3631 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_97_in_functions_returning_numerics3639 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_functions_returning_numerics3640 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3641 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_functions_returning_strings3679 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3680 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_functions_returning_strings3681 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3683 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_56_in_functions_returning_strings3686 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3688 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3691 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_122_in_functions_returning_strings3699 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3701 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_functions_returning_strings3702 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3704 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_56_in_functions_returning_strings3707 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3709 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3712 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_126_in_functions_returning_strings3720 = new BitSet(new long[]{0x0028000383E00240L,0x540100106003C008L,0x0000000000000002L});
	public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings3723 = new BitSet(new long[]{0x0000000200000000L,0x0000000020000000L});
	public static final BitSet FOLLOW_trim_character_in_functions_returning_strings3728 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
	public static final BitSet FOLLOW_93_in_functions_returning_strings3732 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3736 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3738 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LOWER_in_functions_returning_strings3746 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_functions_returning_strings3748 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3749 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3750 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_129_in_functions_returning_strings3758 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3759 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3760 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_94_in_function_invocation3790 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_function_name_in_function_invocation3791 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_56_in_function_invocation3794 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_function_arg_in_function_invocation3796 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_function_invocation3800 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_function_arg3811 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_function_arg3819 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_function_arg3827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_function_arg3835 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_case_expression_in_case_expression3846 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_case_expression_in_case_expression3854 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coalesce_expression_in_case_expression3862 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullif_expression_in_case_expression3870 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_79_in_general_case_expression3881 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression3883 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L,0x0000000000000008L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression3886 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L,0x0000000000000008L});
	public static final BitSet FOLLOW_87_in_general_case_expression3890 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_general_case_expression3892 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
	public static final BitSet FOLLOW_89_in_general_case_expression3894 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_when_clause3905 = new BitSet(new long[]{0x12A8000183E20240L,0xC68124A2501F83F8L,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_when_clause3907 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_when_clause3909 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_when_clause3911 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_79_in_simple_case_expression3922 = new BitSet(new long[]{0x0008000000000000L,0x8000000000000000L});
	public static final BitSet FOLLOW_case_operand_in_simple_case_expression3924 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression3926 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L,0x0000000000000008L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression3929 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L,0x0000000000000008L});
	public static final BitSet FOLLOW_87_in_simple_case_expression3933 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_case_expression3935 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
	public static final BitSet FOLLOW_89_in_simple_case_expression3937 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_case_operand3948 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_case_operand3956 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_simple_when_clause3967 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause3969 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_simple_when_clause3971 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause3973 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_80_in_coalesce_expression3984 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression3985 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_coalesce_expression3988 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression3990 = new BitSet(new long[]{0x0100000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_coalesce_expression3993 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_112_in_nullif_expression4004 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4005 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_56_in_nullif_expression4007 = new BitSet(new long[]{0x12A8000183E20240L,0xC68104A2401F8208L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression4009 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_nullif_expression4010 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_67_in_input_parameter4023 = new BitSet(new long[]{0x1000000000020000L});
	public static final BitSet FOLLOW_numeric_literal_in_input_parameter4025 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter4048 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_53_in_input_parameter4069 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_WORD_in_input_parameter4071 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_136_in_input_parameter4073 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_literal4101 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_literal4125 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_identification_variable4174 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4186 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_58_in_parameter_name4189 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4192 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_TRIM_CHARACTER_in_trim_character4222 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string_literal4233 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_60_in_numeric_literal4245 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal4249 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_object_field4261 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_embeddable_object_field4272 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_collection_valued_field4283 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_name4294 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_subtype4305 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_type_literal4316 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_function_name4327 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_state_field4338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_result_variable4349 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_superquery_identification_variable4360 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_date_time_timestamp_literal4371 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_pattern_value4382 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_collection_valued_input_parameter4393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_single_valued_input_parameter4404 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred18_JPA2819 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_field_in_synpred27_JPA21009 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred33_JPA21123 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_synpred34_JPA21131 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred41_JPA21245 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred42_JPA21253 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred43_JPA21271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred44_JPA21279 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred47_JPA21336 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred48_JPA21344 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred49_JPA21352 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_synpred51_JPA21371 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred51_JPA21373 = new BitSet(new long[]{0x0008000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred51_JPA21375 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_path_expression_in_synpred51_JPA21379 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred51_JPA21380 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_synpred53_JPA21414 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred53_JPA21416 = new BitSet(new long[]{0x0008000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred53_JPA21418 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_count_argument_in_synpred53_JPA21422 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred53_JPA21424 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_synpred65_JPA21722 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_synpred73_JPA21896 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_synpred73_JPA21897 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_synpred73_JPA21898 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred78_JPA22050 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred79_JPA22058 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred80_JPA22066 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred81_JPA22085 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred82_JPA22093 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred83_JPA22101 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred84_JPA22109 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred85_JPA22117 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_synpred86_JPA22125 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_109_in_synpred89_JPA22185 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_synpred90_JPA22200 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_synpred91_JPA22237 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_synpred92_JPA22245 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_synpred93_JPA22253 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_synpred94_JPA22261 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_synpred95_JPA22269 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred96_JPA22277 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_synpred97_JPA22285 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred116_JPA22538 = new BitSet(new long[]{0x0000000000000000L,0x0000200000002000L});
	public static final BitSet FOLLOW_109_in_synpred116_JPA22541 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_77_in_synpred116_JPA22545 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred116_JPA22547 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred116_JPA22549 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred116_JPA22551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred118_JPA22559 = new BitSet(new long[]{0x0000000000000000L,0x0000200000002000L});
	public static final BitSet FOLLOW_109_in_synpred118_JPA22562 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_77_in_synpred118_JPA22566 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred118_JPA22568 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred118_JPA22570 = new BitSet(new long[]{0x0028000183E00240L,0x4401000040038008L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred118_JPA22572 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred135_JPA22827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred141_JPA22896 = new BitSet(new long[]{0xE000000000000000L,0x0000000000000007L});
	public static final BitSet FOLLOW_comparison_operator_in_synpred141_JPA22898 = new BitSet(new long[]{0x0028000183E00240L,0x4501000040038C08L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred141_JPA22901 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred141_JPA22905 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred144_JPA22914 = new BitSet(new long[]{0x8000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_synpred144_JPA22916 = new BitSet(new long[]{0x0028000002400000L,0x0101000040018C08L,0x00000000000000C0L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred144_JPA22925 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred144_JPA22929 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred147_JPA22938 = new BitSet(new long[]{0x8000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_synpred147_JPA22940 = new BitSet(new long[]{0x0028000002400000L,0x0101000000018C08L});
	public static final BitSet FOLLOW_enum_expression_in_synpred147_JPA22947 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred147_JPA22951 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred149_JPA22960 = new BitSet(new long[]{0xE000000000000000L,0x0000000000000007L});
	public static final BitSet FOLLOW_comparison_operator_in_synpred149_JPA22962 = new BitSet(new long[]{0x0028000103C00240L,0x01010000401D8C08L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred149_JPA22965 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred149_JPA22969 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred152_JPA22978 = new BitSet(new long[]{0x8000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_synpred152_JPA22980 = new BitSet(new long[]{0x0028000002000000L,0x0100000000000C08L});
	public static final BitSet FOLLOW_entity_expression_in_synpred152_JPA22989 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred152_JPA22993 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred154_JPA23002 = new BitSet(new long[]{0x8000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_set_in_synpred154_JPA23004 = new BitSet(new long[]{0x0028000002000000L,0x8000000000000008L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred154_JPA23012 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred161_JPA23093 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred163_JPA23122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred169_JPA23191 = new BitSet(new long[]{0x12A8000103C20240L,0x028104A240018208L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred169_JPA23192 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred169_JPA23193 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred172_JPA23217 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred174_JPA23233 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred179_JPA23284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred181_JPA23300 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred182_JPA23319 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred185_JPA23343 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred187_JPA23359 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_synpred188_JPA23367 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred225_JPA23811 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_synpred227_JPA23827 = new BitSet(new long[]{0x0000000000000002L});
}
